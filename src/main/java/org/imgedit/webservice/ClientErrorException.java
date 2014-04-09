package org.imgedit.webservice;


public class ClientErrorException extends Exception {

    public ClientErrorException() {
        super();
    }

    public ClientErrorException(String message) {
        super(message);
    }
}
