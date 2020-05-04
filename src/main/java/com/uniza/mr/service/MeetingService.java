package com.uniza.mr.service;

import com.uniza.mr.dao.MeetingPersistence;
import com.uniza.mr.exception.MRException;
import com.uniza.mr.model.Filter;
import com.uniza.mr.model.Meeting;
import com.uniza.mr.model.MeetingStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Path("/meeting")
@Tag(name = "Meeting Data", description = "Operations related to meeting management.")
@Consumes(MediaType.APPLICATION_JSON)
public class MeetingService {

    @Inject
    private MeetingPersistence dao;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "View a list of meetings")
    public List<Meeting> getMeetings() throws MRException {
        return dao.findAllMeetings();
    }

    @POST
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "View a list of filtered meetings")
    public List<Meeting> getFilteredMeetings(Filter filter) throws MRException {
        return dao.findFilteredMeetings(filter);
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a meeting")
    public Meeting createMeeting(Meeting meeting) throws MRException {
       return dao.persistMeeting(meeting);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a meeting by ID")
    public Meeting getMeeting(@PathParam("id") Long id) throws MRException { return dao.findMeeting(id); }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a meeting")
    public Meeting updateMeeting(Meeting meeting) throws MRException { return dao.updateMeeting(meeting); }

    @PUT
    @Path("/updateFamily")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a family of  meetings")
    public Meeting updateFamilyOfMeetings(Meeting meeting) throws MRException { return dao.updateFamilyOfMeetings(meeting); }

    @DELETE()
    @Path("/delete/{id}/{delChildrens}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete a meeting")
    public ArrayList<Long> deleteMeeting(@PathParam("id") Long id, @PathParam("delChildrens") Boolean delChildrens) throws MRException { return dao.deleteMeeting(id, delChildrens); }
}