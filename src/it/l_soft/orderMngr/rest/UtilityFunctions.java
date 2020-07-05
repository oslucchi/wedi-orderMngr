package it.l_soft.orderMngr.rest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterResolution;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.rest.dbUtils.CustomerDelivery;
import it.l_soft.orderMngr.rest.dbUtils.DBConnection;
import it.l_soft.orderMngr.rest.dbUtils.DBInterface;
import it.l_soft.orderMngr.rest.dbUtils.OrderShipment;
import it.l_soft.orderMngr.rest.dbUtils.Orders;
import it.l_soft.orderMngr.rest.dbUtils.Shipments;
import it.l_soft.orderMngr.utils.JavaJSONMapper;
import it.l_soft.orderMngr.utils.Labels;
import it.l_soft.orderMngr.utils.Labels.PackageLabel;
import it.l_soft.orderMngr.utils.Mailer;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

@Path("/utils")
public class UtilityFunctions {
	ApplicationProperties ap = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(UtilityFunctions.class);
	DBConnection conn = null;

	@POST
	@Path("/createShipments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createShipments(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		String forwarder;
		
		ArrayList<Orders> orders = new ArrayList<Orders>();
		ArrayList<OrderShipment> pallets  = new ArrayList<OrderShipment>();
		ArrayList<Shipments> shipmentList = new ArrayList<Shipments>();
		CustomerDelivery cd = null;

		String sql = "";
		
		try
		{
			forwarder = jsonIn.getString("forwarder");
		}
		catch(Exception e)
		{
			log.error("Body is wrong: '" + body + "'", e);
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}
	
		try
		{
			conn = DBInterface.connect();
			sql = "status ='RDY' and forwarder = '" + forwarder + "'";
			orders = (ArrayList<Orders>) Orders.getOrders(conn, sql, languageId);
			
			Shipments previousShipment = null;
			for(Orders item : orders)
			{
				int collo = 1;
				cd = new CustomerDelivery(conn, item.getIdCustomerDelivery());
				Shipments shipment = new Shipments();
				pallets = OrderShipment.getOrderShipmentByOrderId(conn, item.getIdOrder());
				if (pallets.size() != 0)
				{
					for(OrderShipment pallet : pallets)
					{
						previousShipment = shipment;
						shipment = new Shipments();
						shipment.setCustomer(item.getCustomerDescription());
						shipment.setAddress(cd.getAddress() + " - " + cd.getZipCode() + " " + cd.getCity());
						shipment.setProvince(cd.getProvince());
						shipment.setLength(pallet.getPalletLength());
						shipment.setWidth(pallet.getPalletWidth());
						shipment.setHeigth(pallet.getPalletHeigth());
						shipment.setWeigth(pallet.getPalletWeigth());
						shipment.setDdt(item.getTransportDocNum());
						shipment.setOrderValue(0);
						shipment.setInsurance("");
						previousShipment.setNote((pallet.getNote() != null ? pallet.getNote() + " - " : "" ) + (collo++) + "' collo");
						shipment.setNote((pallet.getNote() != null ? pallet.getNote() + " - " : "" ) + collo + "' collo");
						shipmentList.add(shipment);
					}
				}
			}
			
		}
        catch (Exception e)
        {
			log.error("Exception: '" + e.getMessage() + "' getting list of shipments for " + forwarder, e);
            e.printStackTrace();
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}
		
		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("shipmentList", shipmentList);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	private void pickRequestToCES(ArrayList<Shipments> shipmentList, Date pickupDate) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.ITALIAN);
		String tableString =  "<table>" +
							  "  <tr>" +
							  "    <th>Cliente</th>" +
							  "    <th>Indirizzo</th>" +
							  "    <th>PV</th>" +
							  "    <th>DDT</th>" +
							  "    <th>Assicuraz.</th>" +
							  "    <th style='text-align:center;'>Note</th>" +
							  "    <th>Lun</th>" +
							  "    <th>Lar</th>" +
							  "    <th>Alt</th>" +
							  "    <th>Kg</th>" +
							  "  </tr>";
		for( int i = 0; i < shipmentList.size(); i++)
		{
			Shipments shipment = shipmentList.get(i);
			if (shipment.isSelected())
			{
				tableString +=  "<tr>" + 
								"  <td>" + shipment.getCustomer() + "</td>" +
								"  <td>" + shipment.getAddress() + "</td>" +
								"  <td>" + shipment.getProvince() + "</td>" +
								"  <td>" + shipment.getDdt() + "</td>" +
								"  <td>" + shipment.getInsurance() + "</td>" +
								"  <td>" + shipment.getNote() + "</td>" +
								"  <td>" + shipment.getLength() + "</td>" +
								"  <td>" + shipment.getWidth() + "</td>" +
								"  <td>" + shipment.getHeigth() + "</td>" +
								"  <td>" + shipment.getWeigth() + "</td>" +
								"</tr>";
			}
		}
		tableString += "</table>";
		
		BufferedReader reader = new BufferedReader(new FileReader(ap.getContext().getRealPath("/resources/emailText.txt")));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) 
		{
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		// delete the last new line separator
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		reader.close();

		String mailBody = stringBuilder.toString();
		mailBody = mailBody.replaceAll("TABLE", tableString).replaceAll("PICKDATE", sdf.format((pickupDate)));
		Mailer.sendMail(mailBody);  
	}
	
	@POST
	@Path("/submitShipmentPickupRequest")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response submitShipmentPickupRequest(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		JsonArray jsonShipments;
		String forwarder;
		ArrayList<Shipments> shipmentList = new ArrayList<Shipments>();
		Shipments shipment;
		Date pickupDate;
		
		try
		{
			jsonShipments = jsonIn.getJsonArray("shipmentList");
			for(int i = 0; i < jsonShipments.size(); i++)
			{
				shipment = (Shipments) JavaJSONMapper.JSONToJava(jsonShipments.getJsonObject(i), Shipments.class);
				shipmentList.add(shipment);
			}
			forwarder = jsonIn.getString("forwarder");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			pickupDate = df.parse(jsonIn.getString("pickupDate"));
			
			switch(forwarder)
			{
			case "CES":
				pickRequestToCES(shipmentList, pickupDate);
				break;
				
			case "TWS":
				break;
			}
		}
		catch(Exception e)
		{
			log.error("Exception: '" + e.getMessage() + "' getting JSON shipments data to submit pick request", e);
            e.printStackTrace();
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}

		return Response.status(Response.Status.OK).entity("").build();
	}
	
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
		String orderRefERP;
		int numberOfItems;
		int copies;
		try
		{
			shipTo = jsonIn.getString("shipTo");
			address = jsonIn.getString("address");
			zipCityProvince = jsonIn.getString("zipCityProvince");
			forwarder = jsonIn.getString("forwarder");
			orderRefERP = jsonIn.getString("orderRefERP");
			numberOfItems = jsonIn.getInt("numberOfItems");
			copies = jsonIn.getInt("copies");
		}
		catch(Exception e)
		{
			log.error("Body is wrong: '" + body + "'", e);
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}

		ArrayList<PackageLabel> pkgLab = 
				Labels.packageLabels(ap, shipTo, address, zipCityProvince, forwarder, orderRefERP, numberOfItems, true);
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
			printReportToPrinter(ap.getLabelsPrinterName(), jasperPrint, orderRefERP, copies);
		} 
        catch (Exception e)
        {
			log.error("Exception: '" + e.getMessage() + "' creating jasper report", e);
            e.printStackTrace();
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");			
		}
		return Response.status(Response.Status.OK).build();
	}
	
	private void printReportToPrinter(String printerName, JasperPrint jasperPrint, String orderRefERP, int copies) throws Exception
	{
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		if (services.length == 0)
		{
			log.error("No printers available");
			throw(new Exception("Nessuna stampante configurata nel sistema"));
		}
		int selectedService = -1;
		log.debug("Willing to print on " + printerName);
		for(int i = 0; i < services.length;i++)
		{
			if(services[i].getName().toUpperCase().compareTo(printerName.toUpperCase()) == 0)
			{
				log.debug("Found " + services[i].getName());
				selectedService = i;
				break;
			}
		}
		if (selectedService == -1)
		{
			log.trace("Printer " + printerName + " is not available or not configured");
			throw(new Exception("Impossibile stampare su " + printerName));
		}
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
//		printRequestAttributeSet.add(MediaSizeName.ISO_A5);
		printRequestAttributeSet.add(new Copies(copies));
		if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) 
		{ 
			printRequestAttributeSet.add(OrientationRequested.LANDSCAPE); 
		}
		else 
		{ 
			printRequestAttributeSet.add(OrientationRequested.PORTRAIT); 
		}
		// this resolution solved the problem
		printRequestAttributeSet.add(new PrinterResolution(300, 300, ResolutionSyntax.DPI));
		
		
		JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
        pdfExporter.exportReport();
        
        
        File file = new File("/shares/orderMngr/spool/lab-" + orderRefERP + ".pdf");
        file.createNewFile();

        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        Files.setPosixFilePermissions(file.toPath(), perms);
        
        try (FileOutputStream fos = new FileOutputStream(file))
        {
    	   fos.write(pdfReportStream.toByteArray());
    	   fos.close(); //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
    	}
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
//        response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
//
//        OutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(pdfReportStream.toByteArray());
//        responseOutputStream.close();
        pdfReportStream.close();

		JRPrintServiceExporter exporter;
		exporter = new JRPrintServiceExporter();
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintService(services[selectedService]);
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setDisplayPageDialog(false);
		configuration.setDisplayPrintDialog(false);

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setConfiguration(configuration);

		log.debug(exporter);
		
		log.trace("attributes and configuration set. Now printing");
		exporter.exportReport();
	}
}