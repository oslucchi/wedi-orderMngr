package it.l_soft.orderMngr.rest.dbUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class OrderDetails extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;
	
	protected int idOrderDetails;
	protected int idOrder;
	protected int idArticle;
	protected double quantity;
	protected int piecesFromSqm;
	protected double cost;
	protected String articleRefERP;
	protected String articleCategory;
	protected String articleDescription;
	protected String articleUnityOfMeasure;
	protected double articleRateOfConversion;
	
	protected int sourceIssue;
	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "OrderDetails";
		idColName = "idOrderDetails";
	}

	public OrderDetails()
	{
		setNames();
	}

	public OrderDetails(DBConnection conn, int id) throws Exception
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
	
	public static void deleteOrderDetails(DBConnection conn, int idOrder, int languageCode) throws Exception {
		Logger log = Logger.getLogger(OrderDetails.class);
		String sql = "DELETE FROM OrderDetails " +
					 "WHERE idOrder = " + idOrder;
		executeStatement(conn, sql, false);
		log.trace("Deleting order details: " + sql);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<OrderDetails> getOrderDetails(DBConnection conn, String whereCondition, int languageCode) throws Exception {
		Logger log = Logger.getLogger(OrderDetails.class);
		String sql = "SELECT * " +
					 "FROM OrderDetails " +
					 "WHERE " + whereCondition;
		log.trace("Querying: " + sql);
		return (ArrayList<OrderDetails>) DBInterface.populateCollection(conn, sql, OrderDetails.class);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<OrderDetails> getAllOrderDetails(DBConnection conn, int id) throws Exception
	{
		String sql = "SELECT a.*, b.refERP AS articleRefERP, " +
					 "       b.description AS articleDescription, b.unityOfMeasure AS articleUnityOfMeasure, " +
					 "       b.category AS articleCategory, b.rateOfConversion AS articleRateOfConversion " +
					 "FROM OrderDetails a INNER JOIN Articles b ON " +
					 "     (a.idArticle = b.idArticle) " +
					 "WHERE idOrder = " + id;
		log.trace("Querying: " + sql);
		return (ArrayList<OrderDetails>) DBInterface.populateCollection(conn, sql, OrderDetails.class);
	}

	public int getIdOrderDetails() {
		return idOrderDetails;
	}

	public void setIdOrderDetails(int idOrderDetails) {
		this.idOrderDetails = idOrderDetails;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
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

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	public int getSourceIssue() {
		return sourceIssue;
	}

	public void setSourceIssue(int sourceIssue) {
		this.sourceIssue = sourceIssue;
	}

	public String getArticleRefERP() {
		return articleRefERP;
	}

	public void setArticleRefERP(String articleRefERP) {
		this.articleRefERP = articleRefERP;
	}

	public String getArticleCategory() {
		return articleCategory;
	}

	public void setArticleCategory(String articleCategory) {
		this.articleCategory = articleCategory;
	}

	public String getArticleDescription() {
		return articleDescription;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}

	public String getArticleUnityOfMeasure() {
		return articleUnityOfMeasure;
	}

	public void setArticleUnityOfMeasure(String articleUnityOfMeasure) {
		this.articleUnityOfMeasure = articleUnityOfMeasure;
	}

	public double getArticleRateOfConversion() {
		return articleRateOfConversion;
	}

	public void setArticleRateOfConversion(double articleRateOfConversion) {
		this.articleRateOfConversion = articleRateOfConversion
				;
	}

	public int getPiecesFromSqm() {
		return piecesFromSqm;
	}

	public void setPiecesFromSqm(int piecesFromSqm) {
		this.piecesFromSqm = piecesFromSqm;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


}
