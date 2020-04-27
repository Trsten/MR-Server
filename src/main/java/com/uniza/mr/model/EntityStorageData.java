package com.uniza.mr.model;

import com.uniza.mr.exception.MRException;
import com.uniza.mr.exception.MRFileException;
import java.time.LocalDate;
import java.util.List;

/**
 * Definition of data for handling files in EAP.
 * Files are external files (PDFs) stored in EAP together with root entity.
 */
public class EntityStorageData {
    /* Theoretically any entity ID in system
    *  1.) in case of null = new entity, not created yet*/
    private String entityId;
    /* Date of entity creation (File is stored in year/month/ID folders structure
    *  Note: When for example calendar event has 2 dates (from-to) we consider StartDate
    *        for folders creation */
    private LocalDate entityDate;
    /* Directory name
    *  1.) the name is generated as GUID
    *  2.) In case of new entity the attachment directory is stored in temp root
    *  3.) If entity exists then directory structure is build according the time stamp (creation resp. start time)
    *  4.) system returns this info back to client */
    private String directory;
    /* Collection of attachment IDs and file size in Bytes. Do not consider 1st data attachment.
    *  1.) check for number of attachments
    *  2.) check on file size limit and overall size limit of all files */
    private List<EntityFileData> fileDataList;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public LocalDate getEntityDate() {
        return entityDate;
    }

    public void setEntityDate(LocalDate entityDate) {
        this.entityDate = entityDate;
    }

    public List<EntityFileData> getFileDataList() {
        return fileDataList;
    }

    public void setFileDataList(List<EntityFileData> fileDataList) {
        this.fileDataList = fileDataList;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void  validate() throws MRFileException {
        if ( fileDataList == null || fileDataList.isEmpty()) {
            throw new MRFileException("F001 "," At leeast one file attachment has to be provided for upload.");
        }
    }
}
