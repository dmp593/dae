package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import javax.ejb.EJBException;

public class MyEntityNotFoundException extends EJBException {

    public MyEntityNotFoundException(String message) {
        super(message);
    }
}
