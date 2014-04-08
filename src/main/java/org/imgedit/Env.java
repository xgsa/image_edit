package org.imgedit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;


public class Env {

    protected static final String LOG_PROPERTIES_FILENAME = "log4j.properties";

    protected static final Logger LOG = Logger.getLogger(Env.class);


    protected static void configureLogging() {
        File logProperties = new File(LOG_PROPERTIES_FILENAME);
        String logSourceStr;
        if (logProperties.exists()) {
            PropertyConfigurator.configure(LOG_PROPERTIES_FILENAME);
            logSourceStr = String.format("from the '%s' file", LOG_PROPERTIES_FILENAME);
        } else {
            BasicConfigurator.configure();
            logSourceStr = "by default";
        }
        LOG.info(String.format("Hi! The application was started, logging was configured %s!", logSourceStr));
    }
}
