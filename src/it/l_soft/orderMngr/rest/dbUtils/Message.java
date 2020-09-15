package it.l_soft.orderMngr.rest.dbUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class Message extends DBInterface {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6087690497004025576L;
	static public final int MSG_LOGON = 1;
    static public final int MSG_LOGOFF = 2;
    static public final int MSG_BROADCAST = 3;
    static public final int MSG_PRIVATE = 4;
    static public final int MSG_ADD_USER = 5;
    static public final int MSG_RMV_USER = 6;
    static public final int MSG_LOGON_CONF = 7;
    static public final int MSG_LOG_WITH_TOKEN = 8;
    static public final int MSG_HISTORY = 9;
    static public final int MSG_LOGON_DENY = 10;
    static public final int MSG_KEEP_ALIVE = 11;
    static public final int MSG_KEEP_ALIVE_RESPONSE = 12;
    
    protected int idMessage;
    protected Date timestamp;
    protected int type;
    protected String recipient;
    protected String sender;
    protected String text;
    protected String token;
    protected String senderToken;
    protected String recipientToken;

	private void setNames()
	{
		tableName = "Message";
		idColName = "idMessage";
	}

	public Message()
	{
		setNames();
	}

	public Message(DBConnection conn, int id) throws Exception
	{
		getMessage(conn, id);
	}

	public void getMessage(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

    public Message(int type, String sender, String recipient, String text, String recipientToken, String senderToken, String token)
    {
    	this.type = type;
    	this.recipient = recipient;
    	this.sender = sender;
    	this.text = text;
    	this.recipientToken = recipientToken;
    	this.senderToken = senderToken;
    	this.token = token;
    }

    static public String getMessageTypeString(int type)
    {
    	switch(type)
    	{
    	case MSG_LOGON:
    		return("MSG_LOGON");
    		
        case MSG_LOGOFF:
    		return("MSG_LOGOFF");
    		
        case MSG_BROADCAST:
    		return("MSG_BROADCAST");
    		
        case MSG_PRIVATE:
    		return("MSG_PRIVATE");
    		
        case MSG_ADD_USER:
    		return("MSG_ADD_USER");
    		
        case MSG_RMV_USER:
    		return("MSG_RMV_USER");
    		
        case MSG_LOGON_CONF:
    		return("MSG_LOGON_CONF");
    		
        case MSG_LOG_WITH_TOKEN:
    		return("MSG_LOG_WITH_TOKEN");
    		
        case MSG_HISTORY:
    		return("MSG_HISTORY");
    		
        case MSG_LOGON_DENY:
    		return("MSG_LOGON_DENY");

        case MSG_KEEP_ALIVE:
    		return("MSG_KEEP_ALIVE");
    	}
		return("UNKNOWN");
    }
    
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public int getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String toJSONString(boolean isJsonObj)
	{
		String json =
				"{" +
				"  \"type\" : " + type + ", " +
				"  \"sender\" : \"" + sender + "\", " +
				"  \"recipient\" : \"" + recipient + "\", " +
				"  \"text\" : " +
				(!isJsonObj ? "\"" : "") + 
				text.replaceAll("\"", "\\\"") + 
				(!isJsonObj ? "\"" : "") + ", " +
				"  \"senderToken\" : \"" + senderToken + "\", " +
				"  \"recipientToken\" : \"" + recipientToken + "\", " +
				"  \"token\" : \"" + token + "\" " +
				"}";
		return json;
	}
	
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderToken() {
		return senderToken;
	}

	public void setSenderToken(String senderToken) {
		this.senderToken = senderToken;
	}

	public String getRecipientToken() {
		return recipientToken;
	}

	public void setRecipientToken(String recipientToken) {
		this.recipientToken = recipientToken;
	}

	public void insert(DBConnection conn) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	String sql = "";
    	/*
    	 * Populating a ResultSetMetaData object to obtain table columns to be used in the query.
    	 */
		sql = "INSERT INTO Message " +
			  "SET timestamp = '" + sdf.format(new Date()) + "', " +
			  "    type = " + this.type + ", " +
			  "    sender = '" + this.sender + "', " +
			  "    recipient = '" + this.recipient + "', " + 
			  "    senderToken = '" + this.senderToken + "', " +
			  "    recipientToken = '" + this.recipientToken + "', " + 
			  "    text = '" + this.text + "'";
		conn.executeQuery(sql, false);
	}
	
	@SuppressWarnings("unchecked")
	public static String getHistory(DBConnection conn, String token) throws Exception
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sql = "SELECT * FROM Message a " +
					 "WHERE (((a.senderToken = '" + token + "') OR " +
					 "        (a.recipientToken = '" + token + "') OR " +
					 "        ((a.senderToken != '" + token + "') AND (a.recipient = 'broadcast'))) AND " +
					 "       (a.timestamp >= CURDATE()))";
		String history = "";
		
		ArrayList<Message> mList = (ArrayList<Message>) populateCollection(conn, sql, Message.class);
		Date previousTimestamp = new Date();
		String recipient;
		String colorClass;
		for(Message item : mList)
		{
			if (previousTimestamp.compareTo(item.getTimestamp()) == 0)
				continue;
			previousTimestamp = item.getTimestamp();
			if (item.getSenderToken().compareTo(token) == 0)
			{
				recipient = "=> " + item.getRecipient();
				colorClass = "greenStatement";
			}
			else
			{
				recipient =  "<= " + item.getSender();
				colorClass = (item.getRecipient().compareTo("broadcast") == 0 ? "blueStatement" : "redStatement");
			}
			history += "<span class='" + colorClass + "'>[" + sdf.format(item.getTimestamp()) + "] ";
			history += recipient + ": " + item.getText() + "</span><br>";
		}
		history = new String(Base64.getEncoder().encode(history.getBytes()));
		return history;
	}
}
