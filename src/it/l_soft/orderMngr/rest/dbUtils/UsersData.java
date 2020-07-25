package it.l_soft.orderMngr.rest.dbUtils;


public class UsersData extends DBInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7459360408436589664L;
	protected String account = "";
	protected String token = "";
	protected boolean selected = false;
	protected boolean active = false;
	
	private void setNames()
	{
		tableName = "UsersData";
		idColName = "idUsersData";
	}

	public UsersData()
	{
		setNames();
	}

	public UsersData(DBConnection conn, int id) throws Exception
	{
		getUsersData(conn, id);
	}

	public void getUsersData(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static UsersData getUsersDataByToken(DBConnection conn, String token) throws Exception
	{
		UsersData ud = new UsersData();
		try
		{
			String sql = "SELECT * " +
						 "FROM UsersData " +
						 "WHERE token = '" + token + "'";
			ud = (UsersData) populateByQuery(conn, sql, UsersData.class);
			return ud;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static UsersData getUsersDataByAccount(DBConnection conn, String account) throws Exception
	{
		UsersData ud = new UsersData();
		try
		{
			String sql = "SELECT * " +
						 "FROM UsersData " +
						 "WHERE account = '" + account + "'";
			ud = (UsersData) populateByQuery(conn, sql, UsersData.class);
			return ud;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public String getAccount()
	{
		return account;
	}
	public String getToken()
	{
		return token;
	}
	public boolean isSelected()
	{
		return selected;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
