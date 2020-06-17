package it.l_soft.orderMngr.rest.dbUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CustomerDelivery extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;

	protected int idCustomerDelivery;
	protected int idCustomer;
	protected String notes;
	protected String address;
	protected String zipCode;
	protected String city;
	protected String province;
	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "CustomerDelivery";
		idColName = "idCustomerDelivery";
	}

	public CustomerDelivery()
	{
		setNames();
	}

	public CustomerDelivery(DBConnection conn, int id) throws Exception
	{
		getOrderDetails(conn, id);
	}

	public void getOrderDetails(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<CustomerDelivery> getCustomerDelivery(DBConnection conn, String whereCondition, int languageCode) throws Exception {
		Logger log = Logger.getLogger(CustomerDelivery.class);
		String sql = "SELECT * " +
					 "FROM CustomerDelivery " +
					 "WHERE " + whereCondition;
		log.trace("Querying: " + sql);
		return (ArrayList<CustomerDelivery>) DBInterface.populateCollection(conn, sql, CustomerDelivery.class);
	}


	@SuppressWarnings("unchecked")
	public static ArrayList<CustomerDelivery> getCustomerDeliveryByAddress(DBConnection conn, int idCustomer, String address, int languageCode) throws Exception {
		Logger log = Logger.getLogger(CustomerDelivery.class);
		String sql = "SELECT * " +
					 "FROM CustomerDelivery " +
					 "WHERE address = '" + address.replaceAll("\'",  "\\\\'") + "' AND " +
					 "      idCustomer = " + idCustomer;
		log.trace("Querying: " + sql);
		return (ArrayList<CustomerDelivery>) DBInterface.populateCollection(conn, sql, CustomerDelivery.class);
	}

	public int getIdCustomerDelivery() {
		return idCustomerDelivery;
	}

	public void setIdCustomerDelivery(int idCustomerDelivery) {
		this.idCustomerDelivery = idCustomerDelivery;
	}

	public int getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
