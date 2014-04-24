package org.imgedit.webservice;


import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Component("webServerHandlerStatistics")
@Aspect
public class WebServerHandlerStatistics {

    private static final Logger LOG = Logger.getLogger(WebServerHandlerStatistics.class);

    private long callsCounter;


    @Pointcut("within(org.imgedit.webservice.WebServerHandler) && @annotation(org.imgedit.webservice.MeasureStatistics)")
    public void messageReceived() {}


    @Around("messageReceived()")
    public Object doMeasure(ProceedingJoinPoint joinpoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ++callsCounter;
        boolean succeeded = false;
        Object result;
        try {
            result = joinpoint.proceed();
            succeeded = true;
        } finally {
            LOG.info(String.format("The request #%s was %s in %s sec.",
                    callsCounter, succeeded ? "processed successfully" : "failed",
                    (System.currentTimeMillis() - startTime) / 1000.0));
        }
        return result;
    }
}
