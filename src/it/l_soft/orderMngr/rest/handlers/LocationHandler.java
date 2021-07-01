package it.l_soft.orderMngr.rest.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
import it.l_soft.orderMngr.rest.dbUtils.DBConnection;
import it.l_soft.orderMngr.rest.dbUtils.DBInterface;
import it.l_soft.orderMngr.rest.dbUtils.Locations;
import it.l_soft.orderMngr.rest.dbUtils.Stock;
import it.l_soft.orderMngr.utils.JavaJSONMapper;

@Path("/locations")
public class LocationHandler {
	@Context
	private ServletContext context;
	
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());
	boolean useExtension = false;
	
	DBConnection conn = null;
	
	@GET
	@Path("/{coordinates}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getActivesBySstatus(@PathParam("coordinates") String coordinates, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);

		Locations location;
		ArrayList<Stock> stockList = null;
		try 
		{
			conn = DBInterface.connect();
			location = Locations.getLocationsByCoordinates(conn, 
														   coordinates.substring(0, 6),
														   coordinates.substring(6, 9),
														   coordinates.substring(9, 12),
														   coordinates.substring(12, 14));
			if (location != null)
			{
				stockList = Stock.getStockByLocation(conn, location.getIdLocation());
			}		
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
		jsonResponse.put("stockList", stockList);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@POST
	@Path("/move-stock")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response moveStock(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		Locations location;
		ArrayList<Stock> stockList = null;
		String coordinates = jsonIn.getString("coordinates");
		try 
		{
			conn = DBInterface.connect();
			location = Locations.getLocationsByCoordinates(conn, 
														   coordinates.substring(0, 6),
														   coordinates.substring(6, 9),
														   coordinates.substring(9, 12),
														   coordinates.substring(12, 14));
			if (location != null)
			{
				// Insert new stock with same article and quantity
				// Remove old stockId
			}		
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
		jsonResponse.put("stockList", stockList);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}		return Response.status(Response.Status.OK).entity("").build();
	}
}
