package it.l_soft.orderMngr.utils;

import java.util.ArrayList;

import it.l_soft.orderMngr.rest.ApplicationProperties;

public class Labels {
	private static ArrayList<PackageLabel>  pkgLab = new ArrayList<PackageLabel>();
	
	public static class PackageLabel {
		private String name;
		private String address;
		private String zipCityProvince;
		private String forwarder;
		private String orderRefERP;
		
		public PackageLabel(String name, String address, String zipCityProvince, String forwarder, String orderRefERP)
		{
			this.name = name;
			this.address = address;
			this.zipCityProvince = zipCityProvince;
			this.forwarder = forwarder;
			this.orderRefERP = orderRefERP;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getZipCityProvince() {
			return zipCityProvince;
		}
		public void setZipCityProvince(String zipCityProvince) {
			this.zipCityProvince = zipCityProvince;
		}
		public String getForwarder() {
			return forwarder;
		}
		public void setForwarder(String forwarder) {
			this.forwarder = forwarder;
		}
		public String getOrderRefERP() {
			return orderRefERP;
		}
		public void setOrderRefERP(String orderRefERP) {
			this.orderRefERP = orderRefERP;
		}
	}
	
	public Labels()
	{
		return;
	}
	
	public static ArrayList<PackageLabel> packageLabels(ApplicationProperties ap, String name, String address, 
							  String zipCityProvince, String forwarder, String orderRefERP, int copies, boolean reset)
	{
		if (reset)
		{
			pkgLab = new ArrayList<Labels.PackageLabel>();
		}
		for(int i = 1; i <= copies; i++)
		{
			pkgLab.add(new PackageLabel(name, address, zipCityProvince, 
										forwarder + " " + i + "/" + copies, orderRefERP));
		}
		return pkgLab;
	}	
}
