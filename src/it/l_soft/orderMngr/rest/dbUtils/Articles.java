package it.l_soft.orderMngr.rest.dbUtils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Articles extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;
	protected int idArticle;
	protected String refERP;
	protected String description;
	protected double length;
	protected double width;
	protected double heigth;
	protected double weigth;
	protected String category;
	protected String unityOfMeasure;
	protected double rateOfConversion;
	protected double grossPrice;
	protected double buyPrice;

	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "Articles";
		idColName = "idArticle";
	}

	public Articles()
	{
		setNames();
	}

	public Articles(DBConnection conn, int id) throws Exception
	{
		getArticles(conn, id);
	}

	public void getArticles(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static Articles getArticleByRefERP(DBConnection conn, String refERP) throws Exception
	{
		String sql = "SELECT * " +
					 "FROM Articles " +
					 "WHERE refERP = '" + refERP + "'";
		Articles article = new Articles();
		article.populateObject(conn, sql, article);
		return(article);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Articles> getArticles(DBConnection conn, String whereCondition, int languageCode) throws Exception {
		Logger log = Logger.getLogger(Articles.class);
		String sql = "SELECT * " +
					 "FROM Articles " +
					 "WHERE " + whereCondition;
		log.trace("Querying: " + sql);
		return (ArrayList<Articles>) DBInterface.populateCollection(conn, sql, Articles.class);
	}

	public int getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(int idArticle) {
		this.idArticle = idArticle;
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

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public double getHeigth() {
		return heigth;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	public double getWeigth() {
		return weigth;
	}

	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUnityOfMeasure() {
		return unityOfMeasure;
	}

	public void setUnityOfMeasure(String unityOfMeasure) {
		this.unityOfMeasure = unityOfMeasure;
	}

	public double getRateOfConversion() {
		return rateOfConversion;
	}

	public void setRateOfConversion(double rateOfConversion) {
		this.rateOfConversion = rateOfConversion;
	}

	public double getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(double grossPrice) {
		this.grossPrice = grossPrice;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
