package it.l_soft.orderMngr.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.utils.JavaJSONMapper;
import it.l_soft.orderMngr.utils.Labels;
import it.l_soft.orderMngr.utils.Labels.PackageLabel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

@Path("/utils")
public class PrintManager {
	final Logger log = Logger.getLogger(PrintManager.class);

	@POST
	@Path("/printLabel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response printPackageLabels(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		
		String shipTo;
		String address;
		String zipCityProvince;
		String forwarder;
		int copies;
		try
		{
			shipTo = jsonIn.getString("shipTo");
			address = jsonIn.getString("address");
			zipCityProvince = jsonIn.getString("zipCityProvince");
			forwarder = jsonIn.getString("forwarder");
			copies = jsonIn.getInt("copies");
		}
		catch(Exception e)
		{
			log.error("Body is wrong: '" + body + "'");
			log.trace("Stacktrace: ", e);
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}

		ApplicationProperties ap = ApplicationProperties.getInstance();
		ArrayList<PackageLabel> pkgLab = 
				Labels.packageLabels(ap, shipTo, address, zipCityProvince, forwarder, copies, true);
		JasperPrint jasperPrint = new JasperPrint();
		try 
        {
			JRBeanCollectionDataSource beanColDataSource = new 
					JRBeanCollectionDataSource(pkgLab);

            Map<String, Object> param = new HashMap<String, Object>();
            
            String filepath = ap.getContext().getRealPath("resources/packageLab.jasper");
            log.trace("Creating jasperPrint");
			jasperPrint = JasperFillManager.fillReport(filepath, param, beanColDataSource);
			
			log.trace("printing the report");
			printReportToPrinter(ap.getLabelsPrinterName(), jasperPrint);
		} 
        catch (Exception e)
        {
			log.error("Exception: '" + e.getMessage() + "' creating jasper report");
			log.trace("Stacktrace: ", e);
            e.printStackTrace();
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}
		return Response.status(Response.Status.OK).build();
	}
	
	private void printReportToPrinter(String printerName, JasperPrint jasperPrint) throws JRException
	{
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		int selectedService = 0;
		for(int i = 0; i < services.length;i++){
			if(services[i].getName().toUpperCase().compareTo(printerName.toUpperCase()) == 0)
			{
				selectedService = i;
				break;
			}
		}
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(MediaSizeName.ISO_A5);
		printRequestAttributeSet.add(new Copies(1));
		if (jasperPrint.getOrientationValue() == net.sf.jasperreports.engine.type.OrientationEnum.LANDSCAPE) 
		{ 
		  printRequestAttributeSet.add(OrientationRequested.LANDSCAPE); 
		}
		else 
		{ 
		  printRequestAttributeSet.add(OrientationRequested.PORTRAIT); 
		}
		printRequestAttributeSet.add(OrientationRequested.LANDSCAPE); 
		
		JRPrintServiceExporter exporter;
		exporter = new JRPrintServiceExporter();
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintService(services[selectedService]);
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setDisplayPageDialog(false);
		configuration.setDisplayPrintDialog(false);

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setConfiguration(configuration);

		log.trace("attributes and configuration set. Now printing");
		exporter.exportReport();
	}
}