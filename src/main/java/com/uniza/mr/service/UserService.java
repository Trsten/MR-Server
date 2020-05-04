package com.uniza.mr.service;

import com.uniza.mr.dao.UserPersistence;
import com.uniza.mr.model.User;
import com.uniza.mr.exception.MRException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@Path("/user")
@Tag(name = "User Data", description = "Operations related to reference data management.")
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {

    @Inject
    private UserPersistence dao;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "View a list of users")
    public List<User> getUsers() throws MRException {
        return dao.findAllUsers();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add user")
    public User createUser(User user) throws MRException { return dao.persistUser(user); }

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get user by email")
    public User getUser(@PathParam("email") String email) throws MRException {
        return dao.findUser(email);
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update user's data")
    public User updateUser(User user) throws MRException { return dao.updateUser(user); }

    @DELETE()
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete user")
    public Long deleteUser(@PathParam("id") Long id) throws MRException { return dao.deleteUser(id); }

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "authentication user ")
    public User logIn(User user) throws MRException { return dao.logIn(user); }
}
