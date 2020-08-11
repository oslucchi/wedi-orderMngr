package it.l_soft.orderMngr.webSocketServer;

import java.io.IOException;

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
		try
		{
			conn = DBInterface.connect();
			for(SessionData item : users.getList())
			{
				if (item.getSession().getId().compareTo(sender.getSession().getId()) != 0)
				{
					msgOut = new Message(Message.MSG_BROADCAST, object.getString("sender"), object.getString("recipient"), 
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

	private void sendUserlist(SessionData sender, String token) throws Exception
	{
	    try 
	    {
			for(SessionData item : users.getList())
			{
				if (item.getSession().getId().compareTo(sender.getSession().getId()) != 0)
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

	private void sendMsgHistory(SessionData recipient) throws Exception
	{
		UsersData ud = recipient.getUd();
		if (ud == null)
		{
			log.error("Unable to find user for session id '" + recipient.getSession().getId() + "'. Aborting");
			return;
		}
		try
		{
			conn = DBInterface.connect();
			Message msg = new Message(Message.MSG_HISTORY, "server", 
									  ud.getAccount(), Message.getHistory(conn, ud.getToken()), "", "", "");
			log.debug("Sending message: " + msg.toJSONString(false));
			if (recipient.getSession().isOpen())
			{
				recipient.getSession().getBasicRemote().sendText(msg.toJSONString(false));
			}
			else
			{
				log.error("Unable to send to session " + recipient.getSession().getId() + 
						  " token '" + recipient.getUd().getToken() + "', " +
						  "the session is closed despite been marked as opened");
				recipient.getUd().setActive(false);
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
		JsonObject object = JavaJSONMapper.StringToJSON(message);
		UsersData ud = null;
		SessionData sd = null;

		conn = DBInterface.connect();
		try {
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
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
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