package it.l_soft.orderMngr.rest.dbUtils;

import java.util.Date;

public class Shipments extends DBInterface {
	private static final long serialVersionUID = -4849479160608801245L;
	protected int idOrder;
	protected String customer;
	protected String address;
	protected String city;
	protected String province;
	protected String zipCode;
	protected String ddt;
	protected Date ddtDate;
	protected String note;
	protected String insurance;
	protected double insuranceCost;
	protected int length;
	protected int width;
	protected int heigth;
	protected int weigth;
	protected double volumetricWeigth;
	protected double orderValue;
	protected String orderReference;
	protected int numOfItems;
	protected String customerMail;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Date getDdtDate() {
		return ddtDate;
	}

	public void setDdtDate(Date ddtDate) {
		this.ddtDate = ddtDate;
	}

	public double getInsuranceCost() {
		return insuranceCost;
	}

	public void setInsuranceCost(double insuranceCost) {
		this.insuranceCost = insuranceCost;
	}

	public double getVolumetricWeigth() {
		return volumetricWeigth;
	}

	public void setVolumetricWeigth(double volumetricWeigth) {
		this.volumetricWeigth = volumetricWeigth;
	}

	public String getOrderReference() {
		return orderReference;
	}

	public void setOrderReference(String orderReference) {
		this.orderReference = orderReference;
	}

	public int getNumOfItems() {
		return numOfItems;
	}

	public void setNumOfItems(int numOfItems) {
		this.numOfItems = numOfItems;
	}

	public String getCustomerMail() {
		return customerMail;
	}

	public void setCustomerMail(String customerMail) {
		this.customerMail = customerMail;
	}
	
}
