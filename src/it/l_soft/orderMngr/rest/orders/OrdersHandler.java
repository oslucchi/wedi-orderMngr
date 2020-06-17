package it.l_soft.orderMngr.rest.orders;

import java.util.ArrayList;
import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
import it.l_soft.orderMngr.rest.dbUtils.Articles;
import it.l_soft.orderMngr.rest.dbUtils.DBConnection;
import it.l_soft.orderMngr.rest.dbUtils.DBInterface;
import it.l_soft.orderMngr.rest.dbUtils.OrderDetails;
import it.l_soft.orderMngr.rest.dbUtils.OrderNotes;
import it.l_soft.orderMngr.rest.dbUtils.OrderShipment;
import it.l_soft.orderMngr.rest.dbUtils.Orders;
import it.l_soft.orderMngr.utils.Cesped;
import it.l_soft.orderMngr.utils.ForwaderCostCalculation;
import it.l_soft.orderMngr.utils.JavaJSONMapper;

@Path("/orders")
public class OrdersHandler {
	@Context
	private ServletContext context;
	
	ApplicationProperties prop = ApplicationProperties.getInstance();
	final Logger log = Logger.getLogger(this.getClass());
	boolean useExtension = false;
	
	DBConnection conn = null;
	
	@POST
	@Path("/byStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getActivesBySstatus(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		String statusSet = jsonIn.getString("statusSet");

		ArrayList<Orders> orders;
		try 
		{
			conn = DBInterface.connect();
			orders = Orders.getOrdersByStatusSet(conn, statusSet, languageId);
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
		jsonResponse.put("orderList", orders);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@GET
	@Path("/allDetails/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllDetails(@PathParam("id") int id, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		ArrayList<OrderShipment> orderShipments = new ArrayList<OrderShipment>();
		OrderNotes orderNotes = new OrderNotes();
		ArrayList<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
		ArrayList<Articles> orderArticles = new ArrayList<Articles>();

		try 
		{
			conn = DBInterface.connect();
			orderDetails = OrderDetails.getAllOrderDetails(conn, id);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				DBInterface.disconnect(conn);
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
		}

		try 
		{
			orderShipments = OrderShipment.getOrderShipmentbyOrderId(conn, id);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				DBInterface.disconnect(conn);
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
		}

		try 
		{
			orderNotes = new OrderNotes(conn, id);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				DBInterface.disconnect(conn);
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
		}

		try 
		{
			orderArticles = Orders.getOrderArticles(conn, id);
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
		jsonResponse.put("orderDetails", orderDetails);
		jsonResponse.put("orderShipments", orderShipments);
		jsonResponse.put("orderNotes", orderNotes);
		jsonResponse.put("orderArticles", orderArticles);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@GET
	@Path("/shipments/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getShipments(@PathParam("id") int id, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		ArrayList<OrderShipment> orderShipments = new ArrayList<OrderShipment>();
		try 
		{
			conn = DBInterface.connect();
			orderShipments = OrderShipment.getOrderShipmentbyOrderId(conn, id);
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
		jsonResponse.put("orderShipments", orderShipments);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}
	
	@GET
	@Path("/notes/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNotes(@PathParam("id") int id, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		OrderNotes orderNotes;
		try 
		{
			conn = DBInterface.connect();
			orderNotes = new OrderNotes(conn, id);
		}
		catch(Exception e)
		{
			if (e.getMessage().compareTo("No record found") != 0)
			{
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
			orderNotes = new OrderNotes();
		}
		finally
		{
			DBInterface.disconnect(conn);
		}
		
		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("orderNotes", orderNotes);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@POST
	@Path("/shipmentCost")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getShipmentCost(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		
		Object forwarder = null;
		switch(jsonIn.getString("forwarder"))
		{
		case "CES":
			forwarder = new Cesped();
			break;
		case "TWS":
			break;
		default:
			return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, null, languageId, "generic.execError");
		}
		double cost = ((ForwaderCostCalculation) forwarder).getShipmentCost(
															jsonIn.getString("province"),
															jsonIn.getInt("len"),
															jsonIn.getInt("width"),
															jsonIn.getInt("height"),
															(double)jsonIn.getInt("weight"));
		
		
		HashMap<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("shipmentCost", cost);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@GET
	@Path("/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOrderDetails(@HeaderParam("Language") String language, @PathParam("id") int id)
	{
		int languageId = Utils.setLanguageId(language);
		ArrayList<OrderDetails> orderDetails;
		try 
		{
			conn = DBInterface.connect();
			orderDetails = OrderDetails.getAllOrderDetails(conn, id);
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
		jsonResponse.put("orderDetails", orderDetails);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@GET
	@Path("/{id}/articles")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getOrderArticles(@HeaderParam("Language") String language, @PathParam("id") int id)
	{
		int languageId = Utils.setLanguageId(language);
		ArrayList<Articles> orderArticles;
		try 
		{
			conn = DBInterface.connect();
			orderArticles = Orders.getOrderArticles(conn, id);
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
		jsonResponse.put("orderArticles", orderArticles);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@POST
	@Path("/insert")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insert(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		int	idOrder;
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		Orders order = (Orders) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("order"), Orders.class);
		ArrayList<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
		JsonArray jOrderDetails = jsonIn.getJsonArray("orderDetails");
		ArrayList<Object> objList = JavaJSONMapper.JSONArrayToJava(jOrderDetails, OrderDetails.class);
		
		try 
		{
			conn = DBInterface.TransactionStart();
			order.setIdOrder(0);
			idOrder = order.insertAndReturnId(conn, "idOrders", order);
			for (int i = 0; i < objList.size(); i++) 
			{
				orderDetailsList.add((OrderDetails) objList.get(i));
				orderDetailsList.get(i).setIdOrder(idOrder);				
			}
			OrderDetails.insertCollection(conn, orderDetailsList, "idOrderDetails", OrderDetails.class);
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
		jsonResponse.put("idOrder", idOrder);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	private double updateForwarderCost(Orders order)
	{
		Object forwarder = null;
		switch(order.getForwarder())
		{
		case "CES":
			if ((order.getPalletLength() == 0) || 
				(order.getPalletWidth() == 0) ||
				(order.getPalletHeigth() == 0) ||
				(order.getPalletWeigth() == 0))
			{
				return 0;
			}
			forwarder = new Cesped();
			break;
			
		case "TWS":
			return 0;

		default:
			return 0;
		}
		double cost = ((ForwaderCostCalculation) forwarder).getShipmentCost(
															order.getCustomerDeliveryProvince(),
															order.getPalletLength(),
															order.getPalletWidth(),
															order.getPalletHeigth(),
															(double)order.getPalletWeigth());
		
		return cost;
	}
	
	private int updateInsuranceCost(Orders order, double buyValue)
	{
		Object forwarder = null;
		switch(order.getForwarder())
		{
		case "CES":
			if ((order.getPalletLength() == 0) || 
				(order.getPalletWidth() == 0) ||
				(order.getPalletHeigth() == 0) ||
				(order.getPalletWeigth() == 0))
			{
				return 0;
			}
			forwarder = new Cesped();
			break;
			
		case "TWS":
			return 0;

		default:
			return 0;
		}
		int cost = ((ForwaderCostCalculation) forwarder).getInsuranceCost(buyValue);
		
		return cost;
	}

	@PUT
	@Path("/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrder(String body, @PathParam("id") int idOrder, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		Orders order = (Orders) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("order"), Orders.class);

		try 
		{
			conn = DBInterface.connect();
			if (order.getForwarder() != null)
			{
				order.setForwarderCost(updateForwarderCost(order));
				if (jsonIn.getJsonNumber("buyValue") != null)
				{
					String s;
					double value;
					try
					{
						s = String.valueOf(jsonIn.getInt("buyValue", 0));
						value = Double.parseDouble(s);
					}
					catch(Exception e)
					{
						value = 0;
					}
					order.setInsuranceCost(updateInsuranceCost(order, value));
				}
			}
			
			order.update(conn, "idOrder");
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
		jsonResponse.put("order", order);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@PUT
	@Path("notes/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrderNote(String body, @PathParam("id") int idOrder, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		int id;
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		OrderNotes orderNotes = (OrderNotes) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("orderNote"), OrderNotes.class);

		try 
		{
			conn = DBInterface.connect();
			log.debug("order note id from parms " + orderNotes.getIdOrderNotes());
			if (orderNotes.getIdOrderNotes() == 0)
			{
				if (orderNotes.getNote().length() != 0)
				{
					id = orderNotes.insertAndReturnId(conn, "idOrderNotes", orderNotes);
					log.debug("note has to be inserted (" + orderNotes.getNote() + ") got id " + id);
					orderNotes.setIdOrderNotes(id);
				}
				else
				{
					orderNotes = new OrderNotes();
				}
			}
			else
			{
				if (orderNotes.getNote().length() != 0)
				{
					log.debug("a note was already there, updating it (" + orderNotes.getNote() + ")");
					orderNotes.update(conn, "idOrderNotes");
				}
				else
				{
					log.debug("note is empty. deleting it");
					orderNotes.delete(conn, orderNotes.getIdOrderNotes());
					orderNotes = new OrderNotes();
				}
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
		jsonResponse.put("orderNotes", orderNotes);
		JsonHandler jh = new JsonHandler();
		if (jh.jasonize(jsonResponse, language) != Response.Status.OK)
		{
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(jh.json).build();
		}
		return Response.status(Response.Status.OK).entity(jh.json).build();
	}

	@POST
	@Path("/update/orderDetails/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrderDetails(String body, @PathParam("id") int idOrder, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		OrderDetails orderDetails = (OrderDetails) JavaJSONMapper.JSONToJava(jsonIn, OrderDetails.class);

		try 
		{
			conn = DBInterface.connect();
			orderDetails.update(conn, "idOrderDetails");
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

		return Response.status(Response.Status.OK).entity("").build();
	}
}
