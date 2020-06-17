package it.l_soft.orderMngr.rest.dbUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Customers extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;

	protected int idCustomers;
	protected String refERP;
	protected String description;

	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "Customers";
		idColName = "idCustomers";
	}

	public Customers()
	{
		setNames();
	}

	public Customers(DBConnection conn, int id) throws Exception
	{
		getCustomers(conn, id);
	}

	public void getCustomers(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static Customers getCustomersbyERPRef(DBConnection conn, String refERP) throws Exception
	{
		Customers customer = new Customers();
		String sql = "SELECT * " +
					 "FROM Customers " +
					 "WHERE refERP = '" + refERP + "'";
		customer.populateObject(conn, sql, customer);
		return customer;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Customers> getCustomers(DBConnection conn, String whereCondition, int languageCode) throws Exception {
		Logger log = Logger.getLogger(Customers.class);
		String sql = "SELECT * " +
					 "FROM Customers " +
					 "WHERE " + whereCondition;
		log.trace("Querying: " + sql);
		return (ArrayList<Customers>) DBInterface.populateCollection(conn, sql, Customers.class);
	}


	public int getIdCustomers() {
		return idCustomers;
	}

	public void setIdCustomers(int idCustomers) {
		this.idCustomers = idCustomers;
	}

	public String getRefERP() {
		return refERP;
	}

	public void setRefERP(String refERP) {
		this.refERP = refERP;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
