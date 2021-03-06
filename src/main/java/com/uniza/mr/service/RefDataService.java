package com.uniza.mr.service;

import com.uniza.mr.dao.RefDataPersistence;
import com.uniza.mr.exception.MRException;
import com.uniza.mr.model.AttendantStatus;
import com.uniza.mr.model.MeetingSchedule;
import com.uniza.mr.model.MeetingStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("/refdata")
@Tag(name = "Reference Data", description = "Operations related to reference data management.")
@Consumes(MediaType.APPLICATION_JSON)
public class RefDataService {

    @Inject
    private RefDataPersistence dao;

    @GET
    @Path("/meetingStatus/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "View a list of meeting statuses")
    public List<MeetingStatus> getMeetingStatuses() throws MRException {
        return dao.findAllMeetingStatuses();
    }

    @POST
    @Path("/meetingStatus/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a meeting status")
    public MeetingStatus createMeetingStatus(MeetingStatus status) throws MRException { return dao.persistMeetingStatus(status); }

    @GET
    @Path("/meetingStatus/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a meeting status by ID")
    public MeetingStatus getMeetingStatus(@PathParam("id") Long id) throws MRException { return dao.findMeetingStatus(id); }

    @PUT
    @Path("/meetingStatus/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a meeting status")
    public MeetingStatus updateMeetingStatus(MeetingStatus status) throws MRException { return dao.updateMeetingStatus(status); }

    @DELETE()
    @Path("/meetingStatus/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete a meeting status")
    public Long deleteMeetingStatus(@PathParam("id") Long id) throws MRException { return dao.deleteMeetingStatus(id); }

    /*  Attendant Status  */

    @GET
    @Path("/attendantStatus/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "View a list of attendant statuses")
    public List<AttendantStatus> getAttendantStatuses() throws MRException {
        return dao.findAllAttendantStatuses();
    }

    @POST
    @Path("/attendantStatus/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add an attendant status")
    public AttendantStatus createAttendantStatus(AttendantStatus status) throws MRException { return dao.persistAttendantStatus(status); }

    @GET
    @Path("/attendantStatus/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get an attendant status by ID")
    public AttendantStatus getAttendantStatus(@PathParam("id") Long id) throws MRException { return dao.findAttendantStatus(id); }

    @PUT
    @Path("/attendantStatus/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update an attendant status")
    public AttendantStatus updateAttendantStatus(AttendantStatus status) throws MRException { return dao.updateAttendantStatus(status); }

    @DELETE()
    @Path("/attendantStatus/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete an attendant status")
    public Long deleteAttendantStatus(@PathParam("id") Long id) throws MRException { return dao.deleteAttendantStatus(id); }


    /*  Meeting Schedule    */

    @GET
    @Path("/meetingSchedule/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "View a list of meeting schedules")
    public List<MeetingSchedule> getMeetingSchedule() throws MRException {
        return dao.findAllMeetingSchedules();
    }

    @POST
    @Path("/meetingSchedule/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a meeting schedule")
    public MeetingSchedule createMeetingSchedule(MeetingSchedule schedule) throws MRException { return dao.persistMeetingSchedule(schedule); }

    @GET
    @Path("/meetingSchedule/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a meeting schedule")
    public MeetingSchedule getMeetingSchedules(@PathParam("id") Long id) throws MRException { return dao.findMeetingSchedule(id); }

    @PUT
    @Path("/meetingSchedule/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a meeting schedule")
    public MeetingSchedule updateMeetingSchedule(MeetingSchedule schedule) throws MRException { return dao.updateMeetingSchedule(schedule); }

    @DELETE()
    @Path("/meetingSchedule/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete a meeting schedule")
    public MeetingSchedule deleteMeetingSchedule(@PathParam("id") Long id) throws MRException { return dao.deleteMeetingSchedule(id); }
}