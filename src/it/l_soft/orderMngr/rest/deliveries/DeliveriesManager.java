package it.l_soft.orderMngr.rest.deliveries;

import java.util.HashMap;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.rest.ApplicationProperties;
import it.l_soft.orderMngr.rest.JsonHandler;
import it.l_soft.orderMngr.rest.Utils;
import it.l_soft.orderMngr.rest.dbUtils.CustomerDelivery;
import it.l_soft.orderMngr.rest.dbUtils.DBConnection;
import it.l_soft.orderMngr.rest.dbUtils.DBInterface;
import it.l_soft.orderMngr.utils.JavaJSONMapper;

@Path("/deliveries")
public class DeliveriesManager {
	@Context
	private ServletContext context;
	
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());
	boolean useExtension = false;
	
	DBConnection conn = null;
	
	@GET
	@Path("/order/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDeliveryDetails(@PathParam("id") int idOrder, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		CustomerDelivery customerDelivery = new CustomerDelivery();

		log.trace("get CustomerDelivery");
		try 
		{
			conn = DBInterface.connect();
			customerDelivery = CustomerDelivery.getCustomerDeliveryByOrder(conn, idOrder);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("customerDelivery", customerDelivery);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDelivery(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		CustomerDelivery cd = (CustomerDelivery) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("customerDelivery"), CustomerDelivery.class);

		try 
		{
			conn = DBInterface.connect();
			cd.update(conn, "idCustomerDelivery");
		}
		catch(Exception e)
		{
			log.error("Exception '" + e.getMessage(), e);
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
		}
		finally
		{
			DBInterface.disconnect(conn);
		}

		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("customerDelivery", cd);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}
}
