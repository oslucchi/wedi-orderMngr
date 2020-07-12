package it.l_soft.orderMngr.rest.handlers;

import java.util.HashMap;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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

@Path("/customerDelivery")
public class CustumerDeliveryHandler {
	@Context
	private ServletContext context;
	
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());
	boolean useExtension = false;
	
	DBConnection conn = null;
	
	@PUT
	@Path("/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomerDelivery(String body, @PathParam("id") int idOrder, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		CustomerDelivery customerDelivery = 
				(CustomerDelivery) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("customerDelivery"), CustomerDelivery.class);
		try 
		{
			conn = DBInterface.connect();
			DBInterface.TransactionStart(conn);
			customerDelivery.update(conn, "idCustomerDelivery");
			DBInterface.TransactionCommit(conn);
		}
		catch(Exception e)
		{
			DBInterface.TransactionRollback(conn);
			log.error("Exception '" + e.getMessage(), e);
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
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
}
