package it.l_soft.orderMngr.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import it.l_soft.orderMngr.rest.ApplicationProperties;
import it.l_soft.orderMngr.rest.dbUtils.Shipments;

public class GLS extends ForwarderActions {

	public GLS() {
		populateCostArray("AO", 15, 14.7, 14.2, 13.4);
		populateCostArray("TO", 10, 9.7, 9.3, 8.5);
		populateCostArray("AT", 10.5, 10.2, 9.7, 9);
		populateCostArray("AL", 10.5, 10.2, 9.7, 9);
		populateCostArray("NO", 9.1, 8.8, 8.3, 7.6);
		populateCostArray("VB", 11, 10.7, 10.2, 9.5);
		populateCostArray("BI", 10.5, 10.2, 9.7, 9);
		populateCostArray("VC", 10.5, 10.2, 9.7, 9);
		populateCostArray("CN", 11, 10.7, 10.2, 9.5);
		populateCostArray("GE", 12.1, 11.8, 11.4, 10.6);
		populateCostArray("SV", 12, 11.7, 11.2, 10.4);
		populateCostArray("SP", 13, 12.7, 12.2, 11.4);
		populateCostArray("IM", 13.5, 13.2, 12.7, 11.9);
		populateCostArray("MI", 7.2, 6.9, 6.4, 5.7);
		populateCostArray("LO", 7.2, 6.9, 6.4, 5.7);
		populateCostArray("PV", 9.1, 8.8, 8.3, 7.6);
		populateCostArray("BG", 8.7, 8.4, 7.8, 7.1);
		populateCostArray("BS", 9.1, 8.8, 8.3, 7.6);
		populateCostArray("CO", 9.1, 8.8, 8.3, 7.6);
		populateCostArray("LC", 8.7, 8.4, 7.8, 7.1);
		populateCostArray("VA", 9.1, 8.8, 8.3, 7.6);
		populateCostArray("MN", 11.4, 11.2, 10.7, 9.9);
		populateCostArray("CR", 11.4, 11.2, 10.7, 9.9);
		populateCostArray("SO", 11.9, 11.6, 11.2, 10.4);
		populateCostArray("PD", 9, 8.7, 8.2, 7.5);
		populateCostArray("RO", 9.8, 9.6, 9.2, 8.4);
		populateCostArray("VE", 9.4, 9.2, 9.2, 8.4);
		populateCostArray("TV", 9, 8.7, 8.2, 7.5);
		populateCostArray("VR", 9, 8.7, 8.2, 7.5);
		populateCostArray("VI", 10, 9.7, 9.2, 8.4);
		populateCostArray("BL", 10.8, 10.5, 10, 9.4);
		populateCostArray("BZ", 11.2, 11, 10.5, 9.8);
		populateCostArray("TN", 10.3, 10, 9.6, 8.9);
		populateCostArray("UD", 9.4, 9.2, 8.7, 7.9);
		populateCostArray("GO", 9.4, 9.2, 8.7, 7.9);
		populateCostArray("TS", 10.3, 10, 9.6, 8.9);
		populateCostArray("PN", 9.4, 9.2, 8.7, 7.9);
		populateCostArray("BO", 9.1, 8.8, 8.3, 7.6);
		populateCostArray("MO", 10, 9.7, 9.3, 8.5);
		populateCostArray("FE", 10.5, 10.2, 9.7, 9);
		populateCostArray("RE", 10, 9.7, 9.3, 8.5);
		populateCostArray("FC", 10.5, 10.2, 9.7, 9);
		populateCostArray("RA", 10.5, 10.2, 9.7, 9);
		populateCostArray("RN", 10.5, 10.2, 9.7, 9);
		populateCostArray("PR", 10, 9.7, 9.3, 8.5);
		populateCostArray("PC", 10, 9.7, 9.3, 8.5);
		populateCostArray("FI", 11.5, 11.2, 10.7, 9.9);
		populateCostArray("AR", 12.5, 12.2, 11.7, 10.9);
		populateCostArray("PO", 11.5, 11.2, 10.7, 9.9);
		populateCostArray("PT", 12.5, 12.2, 11.7, 10.9);
		populateCostArray("SI", 13.5, 13.2, 12.7, 11.9);
		populateCostArray("GR", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("MS", 14, 13.7, 13.2, 12.4);
		populateCostArray("LU", 14, 13.7, 13.2, 12.4);
		populateCostArray("LI", 14, 13.7, 13.2, 12.4);
		populateCostArray("PI", 13.5, 13.2, 12.7, 11.9);
		populateCostArray("PG", 14, 13.7, 13.2, 12.4);
		populateCostArray("TR", 15, 14.7, 14.2, 13.4);
		populateCostArray("AN", 14, 13.7, 13.2, 12.4);
		populateCostArray("MC", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("FR", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("AP", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("PU", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("AQ", 16, 15.7, 15.2, 14.4);
		populateCostArray("PE", 15, 14.7, 14.2, 13.4);
		populateCostArray("TE", 15, 14.7, 14.2, 13.4);
		populateCostArray("CH", 15, 14.7, 14.2, 13.4);
		populateCostArray("CB", 17, 16.7, 16.2, 15.4);
		populateCostArray("IS", 17, 16.7, 16.2, 15.4);
		populateCostArray("RM", 14, 13.7, 13.2, 12.4);
		populateCostArray("FR", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("LT", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("VT", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("RI", 14.5, 14.2, 13.7, 12.9);
		populateCostArray("NA", 16, 15.7, 15.2, 14.4);
		populateCostArray("CE", 17.5, 17.2, 16.7, 15.9);
		populateCostArray("SA", 17.5, 17.2, 16.7, 15.9);
		populateCostArray("BN", 17.5, 17.2, 16.7, 15.9);
		populateCostArray("AV", 17.5, 17.2, 16.7, 15.9);
		populateCostArray("BA", 17.5, 17.2, 16.7, 15.9);
		populateCostArray("BT", 19.5, 19.2, 18.7, 17.9);
		populateCostArray("BR", 19.5, 19.2, 18.7, 17.9);
		populateCostArray("FG", 19.5, 19.2, 18.7, 17.9);
		populateCostArray("LE", 19.5, 19.2, 18.7, 17.9);
		populateCostArray("TA", 19.5, 19.2, 18.7, 17.9);
		populateCostArray("PZ", 21, 20.7, 20.2, 19.4);
		populateCostArray("MT", 21, 20.7, 20.2, 19.4);
		populateCostArray("CZ", 22.5, 22.2, 21.7, 20.9);
		populateCostArray("RC", 22.5, 22.2, 21.7, 20.9);
		populateCostArray("VV", 22.5, 22.2, 21.7, 20.9);
		populateCostArray("CS", 22.5, 22.2, 21.7, 20.9);
		populateCostArray("KR", 22.5, 22.2, 21.7, 20.9);
		populateCostArray("PA", 24, 23.7, 23.2, 22.4);
		populateCostArray("TP", 24, 23.7, 23.2, 22.4);
		populateCostArray("AG", 24, 23.7, 23.2, 22.4);
		populateCostArray("CL", 24, 23.7, 23.2, 22.4);
		populateCostArray("EN", 24, 23.7, 23.2, 22.4);
		populateCostArray("CT", 24, 23.7, 23.2, 22.4);
		populateCostArray("RG", 24, 23.7, 23.2, 22.4);
		populateCostArray("SR", 24, 23.7, 23.2, 22.4);
		populateCostArray("ME", 24, 23.7, 23.2, 22.4);
		populateCostArray("CA", 24, 23.7, 23.2, 22.4);
		populateCostArray("OR", 24, 23.7, 23.2, 22.4);
		populateCostArray("NU", 24, 23.7, 23.2, 22.4);
		populateCostArray("SS", 24, 23.7, 23.2, 22.4);
		populateCostArray("OT", 24, 23.7, 23.2, 22.4);
	}

	@Override
	public double getShipmentCost(String province, int len, int width, int heigth, double weigth) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInsuranceCost(double buyValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void generatePickRequest(ArrayList<Shipments> shipmentList, Date pickupDate, ApplicationProperties ap) throws Exception 
	{
		SimpleDateFormat ddmmyy = new SimpleDateFormat("ddMMyy", Locale.ITALIAN);
//		StringBuilder sb = new StringBuilder();
        File file = new File("/archive/Dev/Projects/wedi/wedi-orderMngr/WebContent/spool/GLSTEMP");
        file.createNewFile();

		Formatter fmt = new Formatter(file);
		
		for( int i = 0; i < shipmentList.size(); i++)
		{
			Shipments shipment = shipmentList.get(i);
			if (!shipment.isSelected())
				continue;
			fmt.format("%-35.35s%-35.35s%-30.30s%5.5s%2.2s", 
					   shipment.getCustomer(),
					   shipment.getAddress(),
					   shipment.getCity(),
					   shipment.getZipCode(),
					   shipment.getProvince());

			fmt.format("%10.10s%6.6s%05d%s", 
					   shipment.getDdt(),
					   ddmmyy.format(shipment.getDdtDate()),
					   shipment.getNumOfItems(),
					   "00");
			fmt.format("%06d", 
					   shipment.getWeigth());
			fmt.format("0000000000%-40.40sF%15.15s", 
					   shipment.getNote(),
					   "");
			
			fmt.format("%011.2f%011.1f%12.12s%-600.600s%40.40s%06d01       %06d%02d", 
					   shipment.getInsuranceCost(),
					   shipment.getVolumetricWeigth(),
					   "",
					   shipment.getIdOrder(),
					   "",
					   shipment.getIdOrder(),
					   shipment.getIdOrder(),
					   shipment.getNumOfItems());
			
			String mailAlert = ((shipment.getCustomerMail() == null) || (shipment.getCustomerMail().trim().compareTo("") == 0) ? 
										"" : shipment.getCustomerMail() + ",") + 
							   "logistica.ordini@wedi.it";
			// mailAlert.replaceAll(";", ",");
			fmt.format("%-70.70s%20.20s%17.17s%33.33s%6.6s%-40.40s%4.4s%s%12.12s\n",
					   mailAlert,
					   "",
					   "",
					   "",
					   ddmmyy.format(new Date()),
					   "ritiro possibile dalle 14:30",
					   "",
					   (shipment.getInsurance() == null ? "" : "A"),
					   "");
		}
		fmt.close();
	}
}
