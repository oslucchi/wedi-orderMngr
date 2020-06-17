package it.l_soft.orderMngr.utils;

import java.util.HashMap;
import java.util.Map;

public abstract class ForwaderCostCalculation {

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

}
