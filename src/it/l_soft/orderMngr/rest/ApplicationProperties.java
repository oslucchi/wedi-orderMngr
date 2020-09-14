package it.l_soft.orderMngr.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

public class ApplicationProperties {
	// site specific
	private String redirectRegistrationCompleted = "";
	private String redirectHome	= "";
	private String redirectOnLogin = "";
	private int sessionExpireTime = 0;
	private String defaultLang = "";
	private String noAuthorizationRequired = "";
	private String noAuthorizationRequiredRoot = "";
	private String URLFilterFiles = "";
	private String URLFilterFolders = "";	
	private boolean useCoars = false;
	private double[] insuranceMinOrderValue;

	// package properties
	private String mailSmtpHost = "";
	private String mailFrom = "";
	private String statusMailFrom = "";
	private String mailUser = "";
	private String mailPassword = "";
	private String mailTo = "";
	private String mailCC = "";

	private String labelsPrinterName = "";

	private ServletContext context;
	
	private static ApplicationProperties instance = null;
	
	final Logger log = Logger.getLogger(this.getClass());
	
	public static ApplicationProperties getInstance()
	{
		if (instance == null)
		{
			instance = new ApplicationProperties();
		}
		return(instance);
	}
	
	private ApplicationProperties()
	{
		String variable = "";
		log.trace("ApplicationProperties start");
		Properties properties = new Properties();
    	try 
    	{
    		log.debug("path of abs / '" + ApplicationProperties.class.getResource("/").getPath() + "'");
        	InputStream in = ApplicationProperties.class.getResourceAsStream("/resources/package.properties");
        	if (in == null)
        	{
        		log.error("resource path not found");
        		return;
        	}
        	properties.load(in);
	    	in.close();
		}
    	catch(IOException e) 
    	{
			log.warn("Exception " + e.getMessage(), e);
    		return;
		}
    	
    	defaultLang = properties.getProperty("defaultLang");
    	noAuthorizationRequired = properties.getProperty("noAuthorizationRequired");
    	noAuthorizationRequiredRoot = properties.getProperty("noAuthorizationRequiredRoot");
		useCoars = Boolean.parseBoolean(properties.getProperty("useCoars"));
		
		try
    	{
    		variable = "sessionExpireTime";
    		sessionExpireTime = Integer.parseInt(properties.getProperty("sessionExpireTime"));
    	}
    	catch(NumberFormatException e)
    	{
    		log.error("The format for the variable '" + variable + "' is incorrect (" +
    					 properties.getProperty("sessionExpireTime") + ")", e);
    	}

    	String envConf = System.getProperty("envConf");
    	try 
    	{
    		properties = new Properties();
    		String siteProps = "/resources/site." + (envConf == null ? "dev" : envConf) + ".properties";
    		log.debug("Use " + siteProps);
        	InputStream in = ApplicationProperties.class.getResourceAsStream(siteProps);        	
			properties.load(in);
	    	in.close();
		}
    	catch(IOException e) 
    	{
			log.error("Exception " + e.getMessage(), e);
    		return;
		}
		labelsPrinterName = properties.getProperty("labelsPrinterName");
		mailSmtpHost = properties.getProperty("mailSmtpHost");
    	mailFrom = properties.getProperty("mailFrom");
    	statusMailFrom = properties.getProperty("statusMailFrom");
    	mailUser = properties.getProperty("mailUser");
    	mailPassword = properties.getProperty("mailPassword");
    	mailTo = properties.getProperty("mailTo");
    	mailCC = properties.getProperty("mailCC");
    	redirectRegistrationCompleted = properties.getProperty("redirectRegistrationCompleted");
    	redirectHome = properties.getProperty("redirectHome");
    	redirectOnLogin = properties.getProperty("redirectOnLogin");
    	String[] s = properties.getProperty("insuranceMinOrderValue").split(" "); 
    	insuranceMinOrderValue = new double[s.length];
    	int i = 0;
    	for(String item : properties.getProperty("insuranceMinOrderValue").split(" "))
    	{
        	insuranceMinOrderValue[i++] = Double.valueOf(item);
    	}
	}

	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public String getMailUser() {
		return mailUser;
	}

	public String getMailPassword() {
		return mailPassword;
	}
	
	public String getMailFrom() {
		return mailFrom;
	}

	public String getStatusMailFrom() {
		return statusMailFrom;
	}

	public int getSessionExpireTime() {
		return sessionExpireTime;
	}

	public String getRedirectRegistrationCompleted() {
		return redirectRegistrationCompleted;
	}

	public String getRedirectHome() {
		return redirectHome;
	}

	public String getRedirectOnLogin() {
		return redirectOnLogin;
	}

	public String getDefaultLang() {
		return defaultLang;
	}

	public String getNoAuthorizationRequired() {
		return noAuthorizationRequired;
	}

	public boolean isUseCoars() {
		return useCoars;
	}

	public String getNoAuthorizationRequiredRoot() {
		return noAuthorizationRequiredRoot;
	}

	public ServletContext getContext() {
		return context;
	}		

	public void setContext(ServletContext context) {
		this.context = context;
	}

	public String getLabelsPrinterName() {
		return labelsPrinterName;
	}

	public String[] getURLFilterFiles() {
		return URLFilterFiles.split(",");
	}

	public String[] getURLFilterFolders() {
		return URLFilterFolders.split(",");
	}

	public String getMailTo() {
		return mailTo;
	}

	public String getMailCC() {
		return mailCC;
	}

	public double[] getInsuranceMinOrderValue() {
		return insuranceMinOrderValue;
	}
}