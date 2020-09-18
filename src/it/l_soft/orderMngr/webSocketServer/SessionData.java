package it.l_soft.orderMngr.webSocketServer;

import java.util.Date;

import javax.websocket.Session;

import it.l_soft.orderMngr.rest.dbUtils.UsersData;

public class SessionData {
	private Session session = null;
	private UsersData ud = null;
	private Date lastKeepAlive = null;
	
	public Session getSession()
	{
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public UsersData getUd() {
		return ud;
	}
	public void setUd(UsersData ud) {
		this.ud = ud;
	}
	public Date getLastKeepAlive() {
		return lastKeepAlive;
	}
	public void setLastKeepAlive(Date lastKeepAlive) {
		this.lastKeepAlive = lastKeepAlive;
	}
}
