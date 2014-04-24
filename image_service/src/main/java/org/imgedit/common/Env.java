package org.imgedit.common;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class Env {

    private static final String LOG_PROPERTIES_FILENAME = "/META-INF/log4j.properties";

    private static final Logger LOG = Logger.getLogger(Env.class);


    protected static void configureLogging() {
        // NOTE: Use BasicConfigurator.configure() for quick configuration
        PropertyConfigurator.configure(Env.class.getResource(LOG_PROPERTIES_FILENAME));
        LOG.info("Hi! The application was started, logging was configured!");
    }

    protected static void runSpring(String configurationFilePath) {
        AbstractApplicationContext ac = new FileSystemXmlApplicationContext(configurationFilePath);
        // As documentation recommends, finalize Spring IoC container gracefully
        ac.registerShutdownHook();
    }
}
