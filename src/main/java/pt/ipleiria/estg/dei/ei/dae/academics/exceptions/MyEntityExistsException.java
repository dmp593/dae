package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import javax.ejb.EJBException;

public class MyEntityExistsException extends EJBException {

    public MyEntityExistsException(String message) {
        super(message);
    }
}
