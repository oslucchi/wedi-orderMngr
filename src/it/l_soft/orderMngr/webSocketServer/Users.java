package it.l_soft.orderMngr.webSocketServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.websocket.Session;

import it.l_soft.orderMngr.rest.dbUtils.UsersData;

public class Users {
	private static Users instance = null;
	private static Map<String, SessionData> list = new HashMap<String, SessionData>();

	public Users()
	{
	}
	
	public static Users getInstance()
	{
		if (instance == null)
		{
			instance = new Users();
		}
		return(instance);
	}
	
	public SessionData addUser(Session session, String account, String token) throws Exception
	{
		UsersData ud = new UsersData();
		ud.setAccount(account);
		if (token != null)
		{
			ud.setToken(token);
		}
		else
		{
			ud.setToken(UUID.randomUUID().toString());
		}
		ud.setSelected(false);
		ud.setActive(true);
		SessionData sd = new SessionData();
		sd.setSession(session);
		sd.setUd(ud);
		list.put(session.getId(), sd);
		return sd;
	}

	public void removeUser(String sessionId)
	{
		SessionData sd = list.get(sessionId);
		if (sd != null)
		{
			sd.getUd().setActive(false);
		}
	}
	
	public UsersData getAccount(Session session) throws Exception
	{
		SessionData sd = list.get(session.getId());
		return sd.getUd();
	}
	
	public static UsersData getAccountByToken(String token) throws Exception
	{
		for(SessionData item : list.values())
		{
			if (item.getUd().getToken().compareTo(token) == 0)
			{
				return item.getUd();
			}
		}
		return null;
	}
	
	public static SessionData getSessionDataByAccount(String account) throws Exception
	{
		for(SessionData item : list.values())
		{
			if (item.getUd().getAccount().compareTo(account) == 0)
			{
				return item;
			}
		}
		return null;
	}
	
	public static SessionData getSessionData(String token) throws Exception
	{
		for(SessionData item : list.values())
		{
			if (item.getUd().getToken().compareTo(token) == 0)
			{
				return item;
			}
		}
		return null;
	}
	
	public UsersData setAccount(Session session, String account) throws Exception
	{
		SessionData sd = list.get(session.getId());
		if (sd == null)
		{
			sd = addUser(session, account, null);
		}
		else
		{
			sd.getUd().setAccount(account);
			list.replace(session.getId(), sd);
		}
		return sd.getUd();
	}
	
	public SessionData getSessionData(Session session) throws Exception
	{
		return list.get(session.getId());
	}
	
	public ArrayList<SessionData> getList() throws Exception
	{
		Collection<SessionData> values = list.values(); 
		ArrayList<SessionData> list = new ArrayList<SessionData>(values);		         
		return list;
	}
}