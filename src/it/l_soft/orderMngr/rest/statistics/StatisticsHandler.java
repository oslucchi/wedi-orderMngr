package it.l_soft.orderMngr.rest.statistics;

import java.time.Instant;
import java.util.HashMap;

import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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
import it.l_soft.orderMngr.rest.dbUtils.PackagingStats;
import it.l_soft.orderMngr.utils.JavaJSONMapper;

@Path("/statistics")
public class StatisticsHandler {
	@Context
	private ServletContext context;
	
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());
	boolean useExtension = false;
	
	DBConnection conn = null;

	@PUT
	@Path("/packaging")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setPackaingTimeAttributes(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body).getJsonObject("attributes");

		PackagingStats pkgStats;
		try 
		{
			conn = DBInterface.connect();
			pkgStats = PackagingStats.getOrderPackagingStats(conn, jsonIn.getInt("idOrder"));
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				DBInterface.disconnect(conn);
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
			pkgStats = new PackagingStats();
			pkgStats.setIdOrder(jsonIn.getInt("idOrder"));
			try 
			{
				pkgStats.setIdPackagingStats(pkgStats.insertAndReturnId(conn, "idPackagingStats", pkgStats));
			}
			catch(Exception e1)
			{
				log.error("Exception '" + e1.getMessage(), e1);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e1, languageId, "generic.execError");
			}
		}

		try 
		{
			switch(jsonIn.getString("what"))
			{
			case "autoStart":
				pkgStats.setAutoStartTime((double) Instant.now().toEpochMilli());
				break;
			case "autoEnd":
				pkgStats.setAutoEndTime((double) Instant.now().toEpochMilli());
				break;
			case "manual":
				pkgStats.setManualTime(jsonIn.getInt("value"));
				break;
			}
			pkgStats.update(conn, "idPackagingStats");
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
		jsonResponse.put("pkgStats", pkgStats);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

//	@PUT
//	@Path("/packaging")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response updateOrder(String body, @HeaderParam("Language") String language)
//	{
//		int languageId = Utils.setLanguageId(language);
//		
//		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body).getJsonObject("attributes");
//		PackagingStats pkgStats;
//
//		try 
//		{
//			conn = DBInterface.connect();
//			pkgStats = new PackagingStats(conn, jsonIn.getInt("idOrder"));
//			switch(jsonIn.getString("what"))
//			{
//			case "autoStart":
//				pkgStats.setAutoStartTime(new Date());
//				break;
//			case "autoEnd":
//				pkgStats.setAutoEndTime(new Date());
//				break;
//			case "manual":
//				pkgStats.setManualTime(jsonIn.getInt("value"));
//				break;
//			}
//			pkgStats.update(conn, "idPackagingStats");
//		}
//		catch(Exception e)
//		{
//			log.error("Exception '" + e.getMessage(), e);
//			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
//		}
//		finally
//		{
//			DBInterface.disconnect(conn);
//		}
//		HashMap<String, Object> jsonResponse = new HashMap<>();
//		jsonResponse.put("pkgStats", pkgStats);
//		JsonHandler jh = new JsonHandler();
//		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
//		{
//			return Response.status(Response.Status.UNAUTHORIZED)
//					.entity(jh.json).build();
//		}
//		return Response.status(Response.Status.OK).entity(jh.json).build();
//	}
}
