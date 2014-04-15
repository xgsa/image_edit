package org.imgedit.common;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Env {

    private static final String LOG_PROPERTIES_FILENAME = "/META-INF/log4j.properties";

    private static final Logger LOG = Logger.getLogger(Env.class);
    private static final Class<? extends Env> thisClass = new Env().getClass();


    protected static void configureLogging() {
        // NOTE: Use BasicConfigurator.configure() for quick configuration
        PropertyConfigurator.configure(thisClass.getResource(LOG_PROPERTIES_FILENAME));
        LOG.info("Hi! The application was started, logging was configured!");
    }
}
