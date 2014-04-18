package org.imgedit.webservice;

import org.imgedit.common.Env;


public class WebServer extends Env {

    private static final String SPRING_CONFIGURATION_FILE = "META-INF/webservice.xml";


    public static void main(String[] args) {
        ArgumentsStorage.setAll(args);
        configureLogging();
        runSpring(SPRING_CONFIGURATION_FILE);
    }
}
