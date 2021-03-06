package it.l_soft.orderMngr.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import it.l_soft.orderMngr.rest.ApplicationProperties;
import it.l_soft.orderMngr.rest.dbUtils.Shipments;

public class Cesped extends ForwarderActions {
	final Logger log = Logger.getLogger(this.getClass());

	public Cesped() {
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
		populateCostArray("MB", 7.2, 6.9, 6.4, 5.7);
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
		populateCostArray("PS", 14.5, 14.2, 13.7, 12.9);
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
		
		populateInsuranceCost(5, 7, 10, 15);
	}

	@Override
	public double getShipmentCost(String province, int len, int width, int heigth, double weigth)
	{
		double volume = len * width * heigth / 1000000.0;
		if (volume * 250 > weigth)
			weigth = volume * 250;
		weigth = Math.ceil(weigth / 100) * 100;
		log.debug("Calculating costs for: '" + province + "', on volumetric weigth: " + weigth);
		
		try
		{
			if (weigth < 500)
				return costArray.get(province.toUpperCase())[0] * weigth / 100; 
			else if (weigth< 1000)
				return costArray.get(province.toUpperCase())[1] * weigth / 100;
			else if (weigth < 2000)
				return costArray.get(province.toUpperCase())[2] * weigth / 100;
			else
				return costArray.get(province.toUpperCase())[3] * weigth / 100;
		}
		catch(Exception e)
		{
			log.error("Error calculating costs for: '" + province + "', on volumetric weigth: " + weigth, e);
		}
		return(0);
	}

	@Override
	public int getInsuranceCost(double buyValue)
	{
		return insuranceCost[0];
	}
	
	@Override
	public void generatePickRequest(ArrayList<Shipments> shipmentList, Date pickupDate, ApplicationProperties ap) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.ITALIAN);
		String tableString =  "<table>\n" +
							  "  <tr>" +
							  "    <th>Cliente</th>" +
							  "    <th>Indirizzo</th>" +
							  "    <th>PV</th>" +
							  "    <th>DDT</th>" +
							  "    <th>Assicuraz.</th>" +
							  "    <th style='text-align:center;'>Note</th>" +
							  "    <th>Lun</th>" +
							  "    <th>Lar</th>" +
							  "    <th>Alt</th>" +
							  "    <th>Kg</th>" +
							  "  </tr>\n";
		for( int i = 0; i < shipmentList.size(); i++)
		{
			Shipments shipment = shipmentList.get(i);
			if (shipment.isSelected())
			{
				tableString +=  "<tr>" + 
								"  <td>" + shipment.getCustomer() + "</td>" +
								"  <td>" + shipment.getAddress() + " - " + shipment.getZipCode() + " " + shipment.getCity() + "</td>" +
								"  <td>" + shipment.getProvince() + "</td>" +
								"  <td>" + shipment.getDdt() + "</td>" +
								"  <td>" + 
									(shipment.getInsurance().compareTo("") != 0 ? 
											"<span style='color:red'>" + shipment.getInsurance() + "</span>" :
											"" ) +
								"  </td>" +
								"  <td>" + shipment.getNote() + "</td>" +
								"  <td align='right'>" + shipment.getLength() + "</td>" +
								"  <td align='right'>" + shipment.getWidth() + "</td>" +
								"  <td align='right'>" + shipment.getHeigth() + "</td>" +
								"  <td align='right'>" + shipment.getWeigth() + "</td>" +
								"</tr>\n";
			}
		}
		tableString += "</table>\n";
		
		BufferedReader reader = new BufferedReader(new FileReader(ap.getContext().getRealPath("/resources/emailText.txt")));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) 
		{
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		// delete the last new line separator
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		reader.close();

		pickupDate = new Date(pickupDate.getTime() + TimeZone.getDefault().getOffset(pickupDate.getTime()));
		String mailBody = stringBuilder.toString();
		mailBody = mailBody.replaceAll("TABLE", tableString).replaceAll("PICKDATE", sdf.format((pickupDate)));
		Mailer.sendMail(mailBody);  
	}

}
