package it.l_soft.orderMngr.rest.dbUtils;

public class Locations extends DBInterface
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8318476614637643700L;
	protected int idLocation;
	protected String x;
	protected String y;
	protected String z;
	protected String wh;

	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "Locations";
		idColName = "idLocation";
	}

	public Locations()
	{
		setNames();
	}

	public Locations(DBConnection conn, int id) throws Exception
	{
		getLocations(conn, id);
	}

	public void getLocations(DBConnection conn, int id) throws Exception
	{
		setNames();
		String sql = "SELECT * " +
					 "FROM " + tableName + " " +
					 "WHERE " + idColName + " = " + id;
		this.populateObject(conn, sql, this);
	}

	public static Locations getLocationsByCoordinates(DBConnection conn, String wh, String x, String y, String z) throws Exception
	{
		String sql = "SELECT * " +
					 "FROM Locations " +
					 "WHERE wh = '" + wh + "' AND x = '" + x + "' AND y = '" + y + "' AND z = '" + z + "'";
		Locations location = new Locations();
		location.populateObject(conn, sql, location);
		return(location);
	}

	public int getIdLocation() {
		return idLocation;
	}

	public void setIdLocation(int idLocation) {
		this.idLocation = idLocation;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}

	public String getWh() {
		return wh;
	}

	public void setWh(String wh) {
		this.wh = wh;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
