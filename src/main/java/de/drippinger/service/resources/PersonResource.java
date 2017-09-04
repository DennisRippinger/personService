package de.drippinger.service.resources;

import de.drippinger.service.PersonService;
import de.drippinger.service.dto.PersonDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/persons")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    private PersonService personService;

    @GET
    @Path("/{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        PersonDto person = personService.findOne(id)
            .orElseThrow(() -> new WebApplicationException("Person not found", NOT_FOUND));

        return Response.status(Response.Status.OK).entity(person).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PersonDto person, @Context UriInfo uriInfo) {
        try {
            PersonDto savedPerson = personService.save(person);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Long.toString(savedPerson.getId()));
            return Response.created(builder.build()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id: \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, PersonDto person) {
        try {
            personService.findOne(id)
                .orElseThrow(() -> new WebApplicationException("Person not found, cannot update.", NOT_FOUND));
            person.setId(id);

            personService.save(person);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id: \\d+}")
    public Response deleteById(@PathParam("id") Long id) {
        boolean success = personService.delete(id);

        if (success) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(NOT_FOUND).entity("Person not found, cannot delete.").build();
        }
    }

}
