package it.l_soft.orderMngr.rest.dbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mysql.cj.jdbc.MysqlDataSource;

import it.l_soft.orderMngr.rest.ApplicationProperties;

public class DBConnection 
{
	final Logger log = Logger.getLogger(this.getClass());
	private DataSource ds = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsm = null;
    private Statement st = null;
	private Connection conn = null;
	private boolean isJournal = false;
	
	public DBConnection() throws Exception  
	{
		try {
			// 
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		} 
		catch(Exception e)
		{
			log.warn("Exception " + e.getMessage(), e);
		} 
		
		String retVal = null;
 		Exception e1 = null;
		try
		{
			// ApplicationProperties prop = new ApplicationProperties();
			// String connectionURL = "jdbc:mysql://" + prop.getDbHost() + "/" + prop.getDbName();
			// conn = DriverManager.getConnection(connectionURL, prop.getDbUser(), prop.getDbPasswd());
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/orderMngr");
			conn = ds.getConnection();
			st = conn.createStatement();
			// log.debug("Created connection (" + conn + ") and statement (" + st + ")");
			st.executeQuery("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED");
		}
		catch(SQLException e) 
		{
			retVal = "Error on database connection (" + e.getMessage() + ")";
			e1 = e;
		}
		if (retVal != null)
		{
			try 
			{
				finalize();
			} 
			catch(Throwable e) 
			{
				// No action required
				;
			}
			throw new Exception(e1);
		}
		try
		{
			st.executeQuery("SELECT * FROM Journal");
//			isJournal = true;
			isJournal = false;
		}
		catch(Exception e)
		{
			log.error("Exception " + e.getMessage(), e);
		}
	}
    
	public DBConnection(ApplicationProperties ap) throws Exception  
	{
		try {
			// 
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		} 
		catch(Exception e)
		{
			log.error("Exception " + e.getMessage(), e);
		} 
		
		String retVal = null;
 		Exception e1 = null;
		try
		{
			ds = new MysqlDataSource();
			((MysqlDataSource) ds).setURL("jdbc:mysql://192.168.60.183:3306/orderMngr?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
			((MysqlDataSource) ds).setUser("orderMngr");
			((MysqlDataSource) ds).setPassword("Omh1nppa");
			
			conn = ds.getConnection();
			st = conn.createStatement();
			// log.debug("Creted connection (" + conn + ") and statement (" + st + ")");
			st.executeQuery("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED");
		}
		catch(SQLException e) 
		{
			retVal = "Error on database connection (" + e.getMessage() + ")";
			e1 = e;
		}
		
		if (retVal != null)
		{
			try 
			{
				finalize();
			} 
			catch(Throwable e) 
			{
				// No action required
				;
			}
			throw new Exception(e1);
		}

		try
		{
			st.executeQuery("SELECT * FROM Journal");
//			isJournal = true;
			isJournal = false;
		}
		catch(Exception e)
		{
			log.error("Exception " + e.getMessage(), e);
		}
	}

	
	@SuppressWarnings("deprecation")
	protected void finalize() 
	{
		// log.debug("Closing resources");
		try 
		{
			ds = null;
			if (rs != null)
			{
				rs.close();
			}
			if (st != null)
			{
				st.close();
			}
			if(conn != null)
			{
				conn.close();
				conn = null;
			}
		}
		catch(Exception e)
		{
			;
		}
	}
	
	public void executeQuery(String sql, boolean logStatement) throws Exception
	{
		if (logStatement) 
		{
			log.debug("executing query '"  + sql + "'");
		}

		StringTokenizer stok = new StringTokenizer(sql);
		String queryType = "";
		if (stok != null)
		{
			queryType = stok.nextToken().toUpperCase();
		}
		try
		{
			if ((queryType.compareTo("INSERT") == 0) ||
				(queryType.compareTo("DELETE") == 0) ||
				(queryType.compareTo("UPDATE") == 0) ||
				(queryType.compareTo("START") == 0) ||
				(queryType.compareTo("COMMIT") == 0) ||
				(queryType.compareTo("ROLLBACK") == 0))
			{
				if (isJournal &&
					((queryType.compareTo("INSERT") == 0) ||
					 (queryType.compareTo("DELETE") == 0) ||
					 (queryType.compareTo("UPDATE") == 0)))
				{
					String table = stok.nextToken();
					if ((queryType.compareTo("INSERT") == 0) || (queryType.compareTo("DELETE") == 0))
					{
						table = stok.nextToken();
					}
					String statement = sql.replaceAll("\'", "\\\\'");
					st.execute("INSERT INTO Journal (idJournal, tableName, actionPerformed, statement) " +
							   "       VALUES(0, '" + table + "', '" + queryType + "', '" + statement + "')");
				}
				st.execute(sql);
			}
			else
			{
				rs = st.executeQuery(sql);
				rsm = rs.getMetaData();
				if (logStatement) 
				{
					if ((rs.getType() == ResultSet.TYPE_SCROLL_INSENSITIVE) &&
						(rs.getConcurrency() == ResultSet.CONCUR_READ_ONLY))
					{
						rs.last();
						log.debug("Done. Retrived " + rs.getRow() + " rows");
						rs.beforeFirst();
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error("Exception " + e.getMessage() + " catched on statemet '" + sql + "'", e);		
			throw new Exception(e);
		}
	}

	public ResultSetMetaData getRsm() {
		return rsm;
	}

	public ResultSet getRs() {
		return rs;
	}

	public Statement getSt()
	{
		return st;
	}
}
