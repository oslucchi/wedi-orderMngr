package it.l_soft.orderMngr.rest.dbUtils;

public class Shipments extends DBInterface {
	private static final long serialVersionUID = -4849479160608801245L;
	protected int idOrder;
	protected String customer;
	protected String address;
	protected String province;
	protected String ddt;
	protected String note;
	protected String insurance;
	protected int length;
	protected int width;
	protected int heigth;
	protected int weigth;
	protected double orderValue;
	protected boolean selected = false;
	
	private void setNames()
	{
		tableName = "Shipments";
		idColName = "";
	}

	public Shipments()
	{
		setNames();
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDdt() {
		return ddt;
	}

	public void setDdt(String ddt) {
		this.ddt = ddt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeigth() {
		return heigth;
	}

	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}

	public int getWeigth() {
		return weigth;
	}

	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public double getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(double orderValue) {
		this.orderValue = orderValue;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	
}
