package it.l_soft.orderMngr.rest.dbUtils;

import org.apache.log4j.Logger;


public class OrderNotes extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;
	protected int idOrderNotes;
	protected int idOrder;
	protected String note;
	
	private void setNames()
	{
		tableName = "OrderNotes";
		idColName = "idOrderNotes";
	}

	public OrderNotes()
	{
		setNames();
	}

	public OrderNotes(DBConnection conn, int id) throws Exception
	{
		getOrderNotes(conn, id);
	}

	public void getOrderNotes(DBConnection conn, int idOrder) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE idOrder = " + idOrder;
		this.populateObject(conn, sql, this);
	}

	public static void deleteOrderNotes(DBConnection conn, int idOrder, int languageCode) throws Exception {
		Logger log = Logger.getLogger(OrderNotes.class);
		String sql = "DELETE FROM OrderNotes " +
					 "WHERE idOrder = " + idOrder;
		log.trace("Deleting order notes: " + sql);
	}

	public int getIdOrderNotes() {
		return idOrderNotes;
	}

	public void setIdOrderNotes(int idOrderNotes) {
		this.idOrderNotes = idOrderNotes;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
