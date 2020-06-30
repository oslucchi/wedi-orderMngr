package it.l_soft.orderMngr.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.ApplicationPath;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.rest.ApplicationProperties;

@ApplicationPath("/")
public class OrderMngr implements ServletContextListener {
	ApplicationProperties prop;
	final Logger log = Logger.getLogger(this.getClass());

	@Override
    public void contextDestroyed(ServletContextEvent sce){
        try {
        	;
        } 
        catch(Exception e) {
    		log.error("Exception " + e.getMessage(), e);    		
        }
    }

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
		prop = ApplicationProperties.getInstance();
		log.debug("arg0 servlet context " + arg0.getServletContext());
		prop.setContext(arg0.getServletContext());
    }
}