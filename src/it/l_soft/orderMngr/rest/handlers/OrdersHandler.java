package it.l_soft.orderMngr.rest.handlers;

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
import it.l_soft.orderMngr.rest.dbUtils.CustomerDelivery;
import it.l_soft.orderMngr.rest.dbUtils.Customers;
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
		CustomerDelivery customerDelivery = new CustomerDelivery();
		Customers customer = new Customers();

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
			orderShipments = OrderShipment.getOrderShipmentByOrderId(conn, id);
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
				DBInterface.disconnect(conn);
				log.error("Exception '" + e.getMessage(), e);
				return Utils.jsonizeResponse(Response.Status.INTERNAL_SERVER_ERROR, e, languageId, "generic.execError");
			}
		}

		log.trace("get CustomerDelivery");
		try 
		{
			customerDelivery = CustomerDelivery.getCustomerDeliveryByOrder(conn, orderDetails.get(0).getIdOrder());
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

		log.trace("get Customers");
		try 
		{
			customer = Customers.getCustomersByOrderId(conn, id, languageId);
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
		jsonResponse.put("customerDelivery", customerDelivery);
		jsonResponse.put("customer", customer);
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
			orderShipments = OrderShipment.getOrderShipmentByOrderId(conn, id);
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
	
	@PUT
	@Path("/shipment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateShipments(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		OrderShipment shipment;
		try 
		{
			shipment = (OrderShipment) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("shipment"), OrderShipment.class);
			conn = DBInterface.connect();
			shipment.update(conn, "idOrderShipment");
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

	
	@POST
	@Path("/addShipment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addShipments(String body, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		OrderShipment shipment;
		try 
		{
			shipment = (OrderShipment) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("shipment"), OrderShipment.class);
			conn = DBInterface.connect();
			shipment.setIdOrderShipment(shipment.insertAndReturnId(conn, "idOrderShipment", shipment));
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
		jsonResponse.put("shipment", shipment);
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
		double cost = 0;
		try
		{
			cost = ((ForwaderCostCalculation) forwarder).getShipmentCost(
															jsonIn.getString("province"),
															jsonIn.getInt("len"),
															jsonIn.getInt("width"),
															jsonIn.getInt("height"),
															(double)jsonIn.getInt("weight"));
			
			
		}
		catch(Exception e)
		{
			log.warn("Error " + e.getMessage(), e);
		}
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
	@PUT
	@Path("/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrder(String body, @PathParam("id") int idOrder, @HeaderParam("Language") String language)
	{
		int languageId = Utils.setLanguageId(language);
		
		JsonObject jsonIn = JavaJSONMapper.StringToJSON(body);
		Orders order = (Orders) JavaJSONMapper.JSONToJava(jsonIn.getJsonObject("order"), Orders.class);
		JsonArray shipments = jsonIn.getJsonArray("shipments");
		try 
		{
			conn = DBInterface.connect();
			DBInterface.TransactionStart(conn);
			if (shipments != null)
			{
				for(int i = 0; i < shipments.size(); i++)
				{
					OrderShipment shipment = (OrderShipment) JavaJSONMapper.JSONToJava(((JsonObject) shipments.get(i)), OrderShipment.class);
					shipment.update(conn, "idOrderShipment");
				}
			}
			order.update(conn, "idOrder");
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
