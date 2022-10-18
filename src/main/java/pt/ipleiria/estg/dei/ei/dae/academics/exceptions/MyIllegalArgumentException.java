package pt.ipleiria.estg.dei.ei.dae.academics.exceptions;

import javax.ejb.EJBException;

public class MyIllegalArgumentException extends EJBException {

    public MyIllegalArgumentException(String message) {
        super(message);
    }
}
