package it.l_soft.orderMngr.rest.dbUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class OrderShipment extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;
	protected int idOrderShipment;
	protected int idOrder;
	protected int palletLength;
	protected int palletWidth;
	protected int palletHeigth;
	protected int palletWeigth;
	protected int numberOfItemsToShip;
	protected double forwarderCost;
	protected double clientCost;
	protected String note;
	
	private void setNames()
	{
		tableName = "OrderShipment";
		idColName = "idOrderShipment";
	}

	public OrderShipment()
	{
		setNames();
	}

	public OrderShipment(DBConnection conn, int id) throws Exception
	{
		getOrderShipment(conn, id);
	}

	public void getOrderShipment(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static void deleteOrderShipment(DBConnection conn, int idOrder, int languageCode) throws Exception {
		Logger log = Logger.getLogger(OrderShipment.class);
		String sql = "DELETE FROM OrderShipment " +
					 "WHERE idOrder = " + idOrder;
		log.trace("Deleting order shipment: " + sql);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<OrderShipment> getOrderShipmentByOrderId(DBConnection conn, int idOrder) throws Exception
	{
		String sql = "SELECT * " +
					 "FROM OrderShipment " +
					 "WHERE idOrder = " + idOrder;
		return (ArrayList<OrderShipment>) DBInterface.populateCollection(conn, sql, OrderShipment.class);
	}

	public int getIdOrderShipment() {
		return idOrderShipment;
	}

	public void setIdOrderShipment(int idOrderShipment) {
		this.idOrderShipment = idOrderShipment;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getPalletLength() {
		return palletLength;
	}

	public void setPalletLength(int palletLength) {
		this.palletLength = palletLength;
	}

	public int getPalletWidth() {
		return palletWidth;
	}

	public void setPalletWidth(int palletWidth) {
		this.palletWidth = palletWidth;
	}

	public int getPalletHeigth() {
		return palletHeigth;
	}

	public void setPalletHeigth(int palletHeigth) {
		this.palletHeigth = palletHeigth;
	}

	public int getPalletWeigth() {
		return palletWeigth;
	}

	public void setPalletWeigth(int palletWeigth) {
		this.palletWeigth = palletWeigth;
	}

	public int getNumberOfItemsToShip() {
		return numberOfItemsToShip;
	}

	public void setNumberOfItemsToShip(int numberOfItemsToShip) {
		this.numberOfItemsToShip = numberOfItemsToShip;
	}

	public double getForwarderCost() {
		return forwarderCost;
	}

	public void setForwarderCost(double forwarderCost) {
		this.forwarderCost = forwarderCost;
	}

	public double getClientCost() {
		return clientCost;
	}

	public void setClientCost(double clientCost) {
		this.clientCost = clientCost;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
