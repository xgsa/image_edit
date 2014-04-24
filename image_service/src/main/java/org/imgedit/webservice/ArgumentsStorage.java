package org.imgedit.webservice;

import org.imgedit.common.Env;
import org.springframework.stereotype.Service;


@Service
public class ArgumentsStorage extends Env {

    private static String[] arguments;


    public String[] getAll() {
        return arguments;
    }

    static public void setAll(String[] arguments) {
        ArgumentsStorage.arguments = arguments;
    }

}
