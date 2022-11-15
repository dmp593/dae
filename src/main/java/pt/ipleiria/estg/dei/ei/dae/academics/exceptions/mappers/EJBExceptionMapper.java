package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.ErrorDTO;

import jakarta.ejb.EJBException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {
    @Override
    public Response toResponse(EJBException e) {
        var cause = e.getCausedByException();

        if (cause instanceof EntityExistsException) {
            return EntityExistsExceptionMapper.getResponse((EntityExistsException) cause);
        }

        if (cause instanceof EntityNotFoundException) {
            return EntityNotFoundExceptionMapper.getResponse((EntityNotFoundException) cause);
        }

        return Response.serverError().entity(new ErrorDTO(e.getMessage())).build();
    }
}
