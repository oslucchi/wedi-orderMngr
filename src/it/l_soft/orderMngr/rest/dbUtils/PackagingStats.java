package it.l_soft.orderMngr.rest.dbUtils;

import org.apache.log4j.Logger;

public class PackagingStats extends DBInterface
{	
	private static final long serialVersionUID = -4849479160608801245L;
	
	protected int idPackagingStats;
	protected int idOrder;
	protected double autoStartTime;
	protected double autoEndTime;
	protected int manualTime;
	
	private void setNames()
	{
		tableName = "PackagingStats";
		idColName = "idPackagingStats";
	}

	public PackagingStats()
	{
		setNames();
	}

	public PackagingStats(DBConnection conn, int id) throws Exception
	{
		getPackagingStats(conn, id);
	}
	
	public void getPackagingStats(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static PackagingStats getOrderPackagingStats(DBConnection conn, int idOrder) throws Exception {
		Logger log = Logger.getLogger(PackagingStats.class);
		String sql = "SELECT * " +
					 "FROM PackagingStats " +
					 "WHERE idOrder = " + idOrder;
		log.trace("Querying: " + sql);
		return (PackagingStats) DBInterface.populateByQuery(conn, sql, PackagingStats.class);
	}

	public int getIdPackagingStats() {
		return idPackagingStats;
	}

	public void setIdPackagingStats(int idPackagingStats) {
		this.idPackagingStats = idPackagingStats;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public double getAutoStartTime() {
		return autoStartTime;
	}

	public void setAutoStartTime(double autoStartTime) {
		this.autoStartTime = autoStartTime;
	}

	public double getAutoEndTime() {
		return autoEndTime;
	}

	public void setAutoEndTime(double autoEndTime) {
		this.autoEndTime = autoEndTime;
	}

	public int getManualTime() {
		return manualTime;
	}

	public void setManualTime(int manualTime) {
		this.manualTime = manualTime;
	}
}
