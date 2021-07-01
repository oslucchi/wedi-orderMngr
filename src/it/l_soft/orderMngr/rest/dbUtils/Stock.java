package it.l_soft.orderMngr.rest.dbUtils;

import java.util.ArrayList;

public class Stock extends DBInterface
{	
	private static final long serialVersionUID = 5206242427876588544L;

	protected int idStock;
	protected int idLocation;
	protected int idArticle;
	protected double quantity;
	protected String articleCode;
	protected String articleDescription;

	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "Stock";
		idColName = "idStock";
	}

	public Stock()
	{
		setNames();
	}

	public Stock(DBConnection conn, int id) throws Exception
	{
		getStock(conn, id);
	}

	public void getStock(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Stock> getStockByLocation(DBConnection conn, int idLocation) throws Exception
	{
		String sql = "SELECT a.*, b.description AS articleDescription, b.refERP AS articleCode " +
					 "FROM Stock a INNER JOIN Articles b ON ("
					 + "	a.idArticle = b.idArticle"
					 + ")" +
					 "WHERE idLocation = " + idLocation;
		ArrayList<Stock> stockList = (ArrayList<Stock>) DBInterface.populateCollection(conn, sql, Stock.class);
		return(stockList);
	}

	
	public int getIdStock() {
		return idStock;
	}

	public void setIdStock(int idStock) {
		this.idStock = idStock;
	}

	public int getIdLocation() {
		return idLocation;
	}

	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
	}

	public int getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(int idArticle) {
		this.idArticle = idArticle;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	public String getArticleCode() {
		return articleCode;
	}

	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
}
