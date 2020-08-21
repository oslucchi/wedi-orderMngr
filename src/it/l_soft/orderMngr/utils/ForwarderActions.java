package it.l_soft.orderMngr.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.l_soft.orderMngr.rest.ApplicationProperties;
import it.l_soft.orderMngr.rest.dbUtils.Shipments;

public abstract class ForwarderActions {

	Map<String, double[]> costArray = new HashMap<String, double[]>();
	int[] insuranceCost;
		
	public void populateCostArray(String province, double ...cost)
	{
		costArray.put(province, cost);
	}
	
	public void populateInsuranceCost(int ...cost)
	{
		this.insuranceCost = cost;
	}
	
	public abstract double getShipmentCost(String province, int len, int width, int heigth, double weigth);
	public abstract int getInsuranceCost(double buyValue);
	public abstract void generatePickRequest(ArrayList<Shipments> shipmentList, Date pickupDate, ApplicationProperties ap) throws Exception;
}
