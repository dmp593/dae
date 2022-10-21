package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.ErrorDTO;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityExistsExceptionMapper implements ExceptionMapper<EntityExistsException> {
    @Override
    public Response toResponse(EntityExistsException e) {
        return getResponse(e);
    }

    protected static Response getResponse(EntityExistsException e) {
        var msgs = e.getMessage().split("#");

        var entity = msgs[0].substring(msgs[0].lastIndexOf('.') + 1);
        var pk = msgs[1].substring(0, msgs[1].length() - 1);

        var msg = entity + " already exists: " + pk;

        return Response.status(Response.Status.CONFLICT).entity(new ErrorDTO(msg)).build();
    }
}
