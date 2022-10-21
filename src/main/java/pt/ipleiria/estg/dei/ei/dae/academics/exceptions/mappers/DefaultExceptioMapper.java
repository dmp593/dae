package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultExceptioMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        var error = new ErrorDTO("Ups... looks like something went wrong. Please, contact our team for support");
        return Response.serverError().entity(error).build();
    }
}
