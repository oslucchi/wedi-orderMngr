package it.l_soft.orderMngr.webSocketServer;

import java.io.IOException;
import java.util.Date;

import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.rest.dbUtils.DBConnection;
import it.l_soft.orderMngr.rest.dbUtils.DBInterface;
import it.l_soft.orderMngr.rest.dbUtils.Message;
import it.l_soft.orderMngr.rest.dbUtils.UsersData;
import it.l_soft.orderMngr.utils.JavaJSONMapper;

@ServerEndpoint("/endpoint")
public class WSServer extends Thread {
	final Logger log = Logger.getLogger(this.getClass());
	private static Users users;
	private DBConnection conn = null;

	public void run() 
    {
		log.trace("webSocketServer started");
    }
	
	public WSServer()
	{
	}
	
	public void setUsers(Users usersLoc)
	{
		users = usersLoc;
	}
	
	public void doShutdown()
	{
		log.trace("Requested to shutdown");
		Thread.currentThread().interrupt();
	}

	private void broadcastMsg(SessionData sender, String text, JsonObject object) throws IOException
	{
		Message msgOut = null;
	    try 
	    {
			for(SessionData item : users.getList())
			{
				if (item.getUd().getToken().compareTo(sender.getUd().getToken()) != 0)
				{
					msgOut = new Message(Message.MSG_BROADCAST, object.getString("sender"), "broadcast", 
										 object.getString("text"), item.getUd().getToken(), sender.getUd().getToken(), "");
					msgOut.insert(conn);
					log.debug("Sending message: " + msgOut.toJSONString(false));
					if (item.getSession().isOpen())
					{
						item.getSession().getBasicRemote().sendText(msgOut.toJSONString(false));
					}
					else
					{
						log.error("Unable to send to session " + item.getSession().getId() + 
								  " token '" + item.getUd().getToken() + "', " +
								  "the session is closed despite been marked as opened");
						item.getUd().setActive(false);
					}
				}
			}
		}
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void privateMsg(SessionData sender, String text, JsonObject object) throws IOException
	{
		Message msgOut = null;
		String recipientToken = object.getString("recipientToken");
		try
		{
			conn = DBInterface.connect();
			SessionData item = Users.getSessionData(recipientToken);
			
			if (item != null)
			{
				msgOut = new Message(Message.MSG_PRIVATE, object.getString("sender"), object.getString("recipient"), 
						 			 object.getString("text"), recipientToken, sender.getUd().getToken(), "");
				msgOut.insert(conn);
				if (item.getUd().isActive())
				{
					log.debug("Sending message: " + msgOut.toJSONString(false));
					if (item.getSession().isOpen())
					{
						item.getSession().getBasicRemote().sendText(msgOut.toJSONString(false));
					}
					else
					{
						log.error("Unable to send to session " + item.getSession().getId() + 
								  " token '" + item.getUd().getToken() + "', " +
								  "the session is closed despite been marked as opened");
						item.getUd().setActive(false);
					}
				}
				else
				{
					log.debug("Message archived as the session is marked close");
				}
			}
		}
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void broadcastNewUser(SessionData newUser) throws IOException
	{
	    try 
	    {
			for(SessionData item : users.getList())
			{
				if ((item.getSession().getId().compareTo(newUser.getSession().getId()) != 0) && item.getUd().isActive())
				{
					Message msg = new Message(Message.MSG_ADD_USER, "server", "broadcast", 
											  JavaJSONMapper.JavaToJSON(newUser.getUd()), "", "", "");
					log.debug("Sending message: " + msg.toJSONString(true));
					if (item.getSession().isOpen())
					{
						item.getSession().getBasicRemote().sendText(msg.toJSONString(true));
					}
					else
					{
						log.error("Unable to send to session " + item.getSession().getId() + 
								  " token '" + item.getUd().getToken() + "', " +
								  "the session is closed despite been marked as opened");
						item.getUd().setActive(false);
					}
				}
			}
		}
	    catch (Exception e) 
	    {
			e.printStackTrace(); 
		}
	}

	private void sendUserlist(SessionData sender, String token) throws Exception
	{
	    try 
	    {
			for(SessionData item : users.getList())
			{
				if (item.getUd().isActive() && (item.getSession().getId().compareTo(sender.getSession().getId()) != 0))
				{
					Message msg = new Message(Message.MSG_ADD_USER, "server", 
											  sender.getUd().getAccount(), JavaJSONMapper.JavaToJSON(item.getUd()), 
											  "", "", "");
					log.debug("Sending message: " + msg.toJSONString(true));
					if (sender.getSession().isOpen())
					{
						sender.getSession().getBasicRemote().sendText(msg.toJSONString(true));
					}
					else
					{
						log.error("Unable to send to session " + sender.getSession().getId() + 
								  " token '" + sender.getUd().getToken() + "', " +
								  "the session is closed despite been marked as opened");
						sender.getUd().setActive(false);
					}
				}
			}
		}
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMsgHistory(SessionData currentUser) throws Exception
	{
		UsersData ud = currentUser.getUd();
		if (ud == null)
		{
			log.error("Unable to find user for session id '" + currentUser.getSession().getId() + "'. Aborting");
			return;
		}
		try
		{
			conn = DBInterface.connect();
			Message msg = new Message(Message.MSG_HISTORY, "server", 
									  ud.getAccount(), Message.getHistory(conn, ud.getToken()), "", "", "");
			log.debug("Sending message: " + msg.toJSONString(false));
			if (currentUser.getSession().isOpen())
			{
				currentUser.getSession().getBasicRemote().sendText(msg.toJSONString(false));
			}
			else
			{
				log.error("Unable to send to session " + currentUser.getSession().getId() + 
						  " token '" + currentUser.getUd().getToken() + "', " +
						  "the session is closed despite been marked as opened");
				currentUser.getUd().setActive(false);
			}
		}
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void logonConfirm(SessionData sd, UsersData ud) throws Exception
	{
		Message msgOut = new Message(Message.MSG_LOGON_CONF, "server", ud.getAccount(), ud.getToken(), "", "", ud.getToken());
		log.debug("Sending msg: " + msgOut.toJSONString(false));
		sd.getSession().getBasicRemote().sendText(msgOut.toJSONString(false));
		sendUserlist(sd, ud.getToken());
		sendMsgHistory(sd);
		broadcastNewUser(sd);
	}
	
	private void broadcastLeftUser(SessionData leftUser) throws IOException
	{
		if (leftUser == null)
			return;
		
	    try 
	    {
			for(SessionData item : users.getList())
			{
				if (item.getUd().isActive() &&
					(item.getSession().getId().compareTo(leftUser.getSession().getId()) != 0))
				{
					Message msg = new Message(Message.MSG_RMV_USER, "server", "broadcast", 
											  JavaJSONMapper.JavaToJSON(leftUser.getUd()), "", "", leftUser.getUd().getToken());
					log.debug("Sending message: " + msg.toJSONString(true));
					if (item.getSession().isOpen())
					{
						item.getSession().getBasicRemote().sendText(msg.toJSONString(true));
					}
					else
					{
						log.error("Unable to send to session " + item.getSession().getId() + 
								  " token '" + item.getUd().getToken() + "', " +
								  "the session is closed despite been marked as opened");
						item.getUd().setActive(false);
					}
				}
			}
		}
	    catch (Exception e) 
	    {
			e.printStackTrace(); 
		}
	}
	
	private void checkSessionStatus(SessionData sd)
	{
		Date now = new Date();
		try 
		{
			for(SessionData item : users.getList())
			{
				if (item.getUd().isActive() && (now.getTime() - item.getLastKeepAlive().getTime() > 120000))
				{
					item.getUd().setActive(false);
					item.getSession().close();
					broadcastLeftUser(item);
				}
			}
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void onOpen(Session session)
	{
		log.trace("onOpen::" + session.getId());
	}

	@OnClose
	public void onClose(Session session) throws IOException, Exception
	{
		log.trace("onClose::" +  session.getId());
		users.removeUser(session.getId());
		broadcastLeftUser(users.getSessionData(session));
	}

	@OnMessage
	public void onMessage(String message, Session session) throws Exception {
		log.debug("onMessage::From=" + session.getId() + " Message=" + message);
		UsersData ud = null;
		SessionData sd = null;
		JsonObject object = null;
		conn = DBInterface.connect();
		try {
			log.trace("Getting message object from json");
			object = JavaJSONMapper.StringToJSON(message);
			log.debug("Received a " + Message.getMessageTypeString(object.getInt("type")) + " message");
			switch(object.getInt("type"))
			{
			case Message.MSG_LOGON:
			case Message.MSG_LOG_WITH_TOKEN:
				if ((object.getString("token") != null) && (object.getString("token").compareTo("") != 0))
				{
					// the client is claiming to have a valid token
					// Check if that token has logged in the current session before
					log.debug("Client willing to connect with token " + object.getString("token"));
					if ((sd = Users.getSessionData(object.getString("token"))) == null)
					{
						// token not found in the current session. Check if it was used by a user before
						// and load data from DB
						if ((ud = UsersData.getUsersDataByToken(conn, object.getString("token"))) != null)
						{ 
							// Valid data found, add the client to the current session using the DB recorded account
							sd = users.addUser(session, ud.getAccount(), ud.getToken());
							ud = sd.getUd();
						}
					}
					else
					{
						sd.setSession(session);
						ud = sd.getUd();
					}
				}
				else if ((object.getString("sender") != null) && (object.getString("sender").compareTo("") != 0))
				{
					// The customer is willing to logon specifying his account.
					// Perform same checks as above. This is for future use if we want to have a password 
					// protected access in place
					log.debug("Client willing to connect with auth: user '" + object.getString("sender") + "'");
					if ((sd = Users.getSessionDataByAccount(object.getString("sender"))) == null)
					{
						// account not found in the current session. Check if it was used by a user before
						// and load data from DB
						if ((ud = UsersData.getUsersDataByAccount(conn, object.getString("sender"))) != null)
						{ 
							// Valid data found, add the client to the current session using the DB recorded account
							sd = users.addUser(session, ud.getAccount(), ud.getToken());
							ud = sd.getUd();
						}
					}
					else
					{
						sd.setSession(session);
						ud = sd.getUd();
					}
				}
				
				if (sd == null)
				{
					// likely the client never connect before.
					// Add a new entry to the current session and DB
					sd = users.addUser(session, object.getString("sender"), object.getString("token"));
					ud = sd.getUd();
					ud.insert(conn, "idUsersData", ud);
				}
				ud.setActive(true);
				logonConfirm(sd, ud);
				sd.setLastKeepAlive(new Date());
				break;
								
			case Message.MSG_LOGOFF:
				break;
								
			case Message.MSG_PRIVATE:
				if ((sd = Users.getSessionData(object.getString("token"))) == null)
				{
					log.error("It should never happen! No session data stored despite been connected");
					return;
				}
				privateMsg(sd, message, object);
				break;
				
			case Message.MSG_BROADCAST:
				if ((sd = Users.getSessionData(object.getString("token"))) == null)
				{
					log.error("It should never happen! No session data stored despite been connected");
					return;
				}
				broadcastMsg(sd, message, object);
				break;

			case Message.MSG_KEEP_ALIVE:
				if ((sd = Users.getSessionData(object.getString("token"))) == null)
				{
					log.error("It should never happen! No session data stored despite been connected");
					return;
				}
				log.debug("sent by: " + sd.getUd().getAccount());
				Message msg = new Message(Message.MSG_KEEP_ALIVE_RESPONSE, "server", 
						  sd.getUd().getAccount(), "PONG", "", "", "");
				session.getBasicRemote().sendText(msg.toJSONString(false));
				sd.setLastKeepAlive(new Date());
				log.debug("keep alive replied");
				checkSessionStatus(sd);
				break;
			}
		} 
		catch (IOException e) 
		{
			log.error("Exception: " + e.getMessage(), e);
		}
		finally
		{
			DBInterface.disconnect(conn);
		}

	}

	@OnError
	public void onError(Throwable t) {
		log.error("onError::" + t.getMessage());
	}
}