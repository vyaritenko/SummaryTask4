package ua.nure.yaritenko.SummaryTask4.web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ContextListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ContextListener.class);
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log("Servlet context destruction starts");
        // no op
        log("Servlet context destruction finished");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log("Servlet context initialization starts");
        ServletContext servletContext = servletContextEvent.getServletContext();
        initLog4J(servletContext);
        initCommandContainer();
        log("Servlet context initialization finished");
        //initializing resource bundle
        // obtain file name with locales descriptions
        String localesFileName = servletContext.getInitParameter("locales");
        // obtain reale path on server
        String localesFileRealPath = servletContext.getRealPath(localesFileName);
        // locad descriptions
        Properties locales = new Properties();
        try {
            locales.load(new FileInputStream(localesFileRealPath));
        } catch (IOException e) {
            e.printStackTrace();
            //write to LOG
        }
        // save descriptions to servlet context
        servletContext.setAttribute("locales", locales);
        locales.list(System.out);
        LOG.trace("Set the session attribute: locales --> " + locales);
    }
    /**
     * Initializes log4j framework.
     *
     * @param servletContext
     */
    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");
        try {
            PropertyConfigurator.configure(servletContext.getRealPath("log4j.properties"));
            LOG.debug("Log4j has been initialized");
        } catch (Exception ex) {
            log("Cannot configure Log4j");
            ex.printStackTrace();
        }
        log("Log4J initialization finished");
    }

    /**
     * Initializes CommandContainer.
     *
     */
    private void initCommandContainer() {

        // initialize commands container
        // just load class to JVM
        try {
            Class.forName("ua.nure.yaritenko.SummaryTask4.web.command.CommandContainer");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Cannot initialize Command Container");
        }
    }

    private void log(String msg) {
        System.out.println("[ContextListener] " + msg);
    }

}
