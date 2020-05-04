package com.uniza.mr.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import com.uniza.mr.exception.MRFileException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.uniza.mr.model.EntityFileData;
import com.uniza.mr.model.EntityStorageData;
import com.uniza.mr.model.LocalDateAdapter;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for file upload
 * It's used in other business operations as common extension
 */
@Stateless
@Path("/file")
@Tag(name = "File operations", description = "Operations related to file upload and directory handling.")
public class FileUploadService {

    /* Allowed characters for uploaded file name */
    private static final Pattern PATTERN = Pattern.compile("[^A-Za-z0-9_\\-\\.]");
    /* Max. length of uploaded file name */
    private static final int MAX_LENGTH = 127;

    private static final String FILES_PATH = "C:" + File.separator +"mrfilestorage";

    @POST
    @Path("/delete/entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete file in existing entity")
    @SecurityRequirement(name = "bearerAuth")
    public Response deleteFile( EntityStorageData entityStorageData) throws MRFileException {
        if ( entityStorageData.getEntityId() == null || entityStorageData.getEntityDate() == null) {
            throw new MRFileException("Entity info not provided","Please specify entity ID and entity date");
        }
        /* storage location for entity */
        File directory = new File( buildTargetPath( entityStorageData));
        if ( !directory.exists()) {
            throw new MRFileException("Directory not found","Provided directory name not found on storage path.");
        }
        /* no files provided = delete all */
        if ( entityStorageData.getFileDataList() == null || entityStorageData.getFileDataList().isEmpty()) {
            File[] contents = directory.listFiles();
            if (contents != null) {
                for (File file : contents) {
                    if ( !file.delete()) {
                        throw new MRFileException("File delete failure","File ["+file.getName()+"] can not be deleted.");
                    }
                }
            }
            if ( !directory.delete()) {
                throw new MRFileException("Directory delete failure","Directory ["+directory.getAbsolutePath()+"] can not be deleted.");
            }
        }
        /* when file is specified */
        else {
            for (EntityFileData fileData : entityStorageData.getFileDataList()) {
                if ( fileData.getName() != null) {
                    File file = new File( directory + File.separator + fileData.getName());
                    if( !file.exists()) {
                        throw new MRFileException("File in directory not found","Expected file ["+file.getName()+"] in entity directory not found.");
                    }
                    if ( !file.delete()) {
                        throw new MRFileException("File delete failure","File ["+file.getName()+"] can not be deleted.");
                    }
                }
            }
            /* when all files in directory are deleted, remove directory as well
             * Note: do not keep empty directory */
            File[] contents = directory.listFiles();
            if ( contents != null && contents.length == 0) {
                if ( !directory.delete()) {
                    String dirName = "undefined";
                    try {
                        dirName = directory.getAbsolutePath().substring( directory.getAbsolutePath().lastIndexOf(File.separator));
                    } catch (Exception e) {}
                    throw new MRFileException("Directory delete failure","Directory ["+dirName+"] can not be deleted.");
                }
            }
        }
        return Response.status(Response.Status.OK)
                .header("message", "Delete operation successful.")
                .build();
    }

    @POST
    @Path("/info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get attached files of existing entity. Files info are added into request and returned as response.")
    @SecurityRequirement(name = "bearerAuth")
    public EntityStorageData getFiles( EntityStorageData entityStorageData) throws MRFileException {

        if ( entityStorageData.getEntityId() == null || entityStorageData.getEntityDate() == null) {
            throw new MRFileException("Entity info not provided","Please specify entity ID and entity date");
        }
        /* storage location for entity */

        File directory = new File( FILES_PATH + File.separator + entityStorageData.getEntityId());

        if ( !directory.exists()) {
            return entityStorageData;
        }

        List<EntityFileData> result = new ArrayList<>();
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File file : contents) {
                result.add( new EntityFileData( file.getName(), file.length()));
            }
        }
        entityStorageData.setFileDataList( result);
        return entityStorageData;
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Upload files to server. Target location depends on related entity. If entity exists, the system uses location according entity." +
            "If entity is newly created, the system uses temporary location.")
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "500",
                            description = "Missing attributes",
                            content = @Content(mediaType = "application/json")),
                    @APIResponse(
                            responseCode = "200",
                            description = "EntityStorageData  with populated data.",
                            content = @Content(mediaType = "application/json")) })
    @SecurityRequirement(name = "bearerAuth")
    public EntityStorageData uploadFile(IMultipartBody multipartBody) throws MRFileException {


        EntityStorageData uploadData = null;

        /* 1.) Attachments from message are parsed
         * - first is mandatory our data definition
         * - next attachment(s) are files */
        List<IAttachment> attachments = multipartBody.getAllAttachments();

        InputStream stream = null;
        int index = 0;
        try {
            for (Iterator<IAttachment> it = attachments.iterator(); it.hasNext();) {
                IAttachment attachment = it.next();
                if (attachment == null) {   //theoretically can't happen
                    continue;
                }
                DataHandler dataHandler = attachment.getDataHandler();
                stream = dataHandler.getInputStream();
                MultivaluedMap<String, String> map = attachment.getHeaders();
                String fileName = null;
                /* Analyze header data
                *  1.) Check 'Content-Disposition'
                *  2.) Attachments with files have file name there
                *  3.) Data attachment does not have it */
                String[] contentDisposition = map.getFirst("Content-Disposition").split(";");
                for (String line : contentDisposition) {

                    if (line != null && line.toLowerCase().contains("filename=")) {
                        String[] names = line.split("=");
                        fileName = names[1].trim().replaceAll("\"", "");
                        break;
                    }
                }

                /* First attachment must be data header
                *  1.) we need to run validation */
                if ( fileName == null && index == 0) {
                    uploadData = convertFromJson( stream);
                    if( uploadData == null) {
                        throw new MRFileException("5001","File upload error, Attachments with Eap data is invalid.");
                    }
                    uploadData.validate();
                    uploadData.setDirectory( buildTargetPath( uploadData));
                } else {
                    writeToFile( stream, uploadData.getDirectory(), fileName);
                    uploadData.getFileDataList().get( index).setUploaded( true);
                    index++;
                }
            }
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new MRFileException("999" + e.getClass().getSimpleName(),e.getMessage());
        } finally {

        }
        return uploadData;
    }

    @POST
    @Path("/download")
    @Produces("application/octet-stream")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Download file attached to given entity")
    @SecurityRequirement(name = "bearerAuth")
    @SuppressWarnings("unchecked")
    public Response downloadFile( EntityStorageData entityStorageData) throws MRFileException {
        if ( entityStorageData.getEntityId() == null || entityStorageData.getEntityDate() == null) {
            throw new MRFileException("Entity info not provided","Please specify entity ID and entity date.");
        }

        if ( entityStorageData.getFileDataList() == null || entityStorageData.getFileDataList().isEmpty()) {
            throw new MRFileException("File info not provided","Please specify file name in request object.");
        }

        /* storage location for entity */
        File directory = new File( buildTargetPath( entityStorageData));
        if ( !directory.exists()) {
            throw new MRFileException("Directory not found","Provided directory name not found in storage.");
        }

        EntityFileData fileData = entityStorageData.getFileDataList().get(0);
        if ( fileData.getName() == null || fileData.getName().trim().length() == 0) {
            throw new MRFileException("File info not provided","Please specify file name in request object.");
        }

        File file = new File(directory + File.separator + fileData.getName());
        if ( !file.exists()) {
            throw new MRFileException("File not found","Provided file name not found in storage.");
        }

        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new MRFileException("999" + e.getClass().getSimpleName(),e.getMessage());
        }

        return Response.status(Response.Status.OK)
                .header( HttpHeaders.CONTENT_DISPOSITION,"inline; filename=" + fileData.getName() + ".pdf")
                .header( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
                .header( HttpHeaders.CONTENT_LENGTH, data.length)
                .entity( data)
                .build();
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {

            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }

    /**
     * Save file
     * @param uploadedInputStream
     * @param uploadedFileLocation
     */
    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation, String fileName) throws MRFileException {
        OutputStream out = null;
        try {
            try {
                System.out.println("### uploadFileLocation-------> "+ uploadedFileLocation);
                File directory = new File( uploadedFileLocation);
                if ( !directory.exists()) {
                    /* create only last directory, parents must exist! */
                    directory.mkdir();
                }
                // Only a-z,A-Z,0-9,_. are allowed characters. Others are replaced with % (It's safe on Linux)
                // Max filename length = 127
                String targetPath = directory + File.separator + escapeStringAsFilename( fileName);

                int read = 0;
                byte[] bytes = new byte[1024];

                out = new FileOutputStream(new File( targetPath));
                while ((read = uploadedInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } finally {
                if (out!=null) out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new MRFileException("7001","Write file error, File xy write failure: "+e.getMessage());
        }
    }

    /**
     * Convert Attachment into POJO
     * @param attachmentStream Stream from attachment
     * @return EapFileData
     */
    private EntityStorageData convertFromJson(InputStream attachmentStream) throws MRFileException {

        EntityStorageData data = null;
        StringBuffer sbuffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(attachmentStream));
        String line = null;
        try {
            try {
                while ((line = br.readLine()) != null) {
                    sbuffer.append(line);
                }
            } finally {
                br.close();
            }

            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
            try {
                data = gson.fromJson(sbuffer.toString(), EntityStorageData.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        } catch (IOException | JsonSyntaxException ex) {
            throw new MRFileException("7002","Attachment data error, Invalid JSON data structure in attachment part 0");
        }
        return data;
    }

    /**
     * Build path to entity storage
     * @param storageData
     * @return
     */
    private String buildTargetPath(EntityStorageData storageData) throws MRFileException {
        String fullPath = null;
        /* new object = ID does not exist yet
         * 1.) directory comes in request (client wants to send files to the same folder in paralel request = must provide directory
         * 2.) directory is empty = auto generate here
         */
        if ( storageData.getEntityId() == null) {
            fullPath = FILES_PATH + File.separator +
                    (storageData.getDirectory() == null || storageData.getDirectory().trim().length() == 0 ?
                    storageData.getEntityId() : storageData.getDirectory());
        } else {
            /* Existing entity
             * 1.) create date base structure and check existence (if it does not exist -> create
             * 2.) if storageData contains also entity directory -> check existence -> create if needed
             */
            //LocalDate date = storageData.getEntityDate();
            fullPath = FILES_PATH + File.separator + storageData.getEntityId();
        }
        try {
            java.nio.file.Path path = Paths.get( fullPath);
            Files.createDirectories( path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MRFileException("F002 Path creation failed","Path ["+fullPath+"] "+e.getMessage());
        }
        return fullPath;
    }

    /**
     * Rename file name if required
     * Note: Original replacement with %xx is now only %
     * @param inFileName
     * @return Filename
     */
    private String escapeStringAsFilename(String inFileName) {

        StringBuffer sb = new StringBuffer();
        // Apply the regex.
        Matcher m = PATTERN.matcher(inFileName);

        while (m.find()) {
            // Convert matched character to percent-encoded.
            //String replacement = "%"+Integer.toHexString(m.group().charAt(0)).toUpperCase();
            String replacement = "%";
            m.appendReplacement(sb,replacement);
        }
        m.appendTail(sb);
        String encoded = sb.toString();

        // Truncate the string.
        int end = Math.min(encoded.length(),MAX_LENGTH);
        return encoded.substring(0,end);
    }

}
