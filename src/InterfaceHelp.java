import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InterfaceHelp implements IItinerary {

	protected List<AirportVertex> stops;
	protected List<RouteEdge> flights;

	public void setData(List<AirportVertex> stops, List<RouteEdge> flights) {
		this.stops = stops;
		this.flights = flights;
	}

	@Override
	public List<String> getStops() {
		List<String> stopNames = new ArrayList<String>();
		for (AirportVertex av : stops) {
			stopNames.add(av.getCode().toLowerCase());
		}
		return stopNames;

	}

	@Override
	public List<String> getFlights() {
		List<String> flightNames = new ArrayList<String>();
		for (RouteEdge r : flights) {
			flightNames.add(r.getData().getFlightNumber());
		}
		return flightNames;
	}

	@Override
	public int totalHop() {
		return flights.size();
	}

	@Override
	public int totalCost() {
		int price = 0;
		for(int i = 0; i < flights.size(); i++) {
			price += flights.get(i).getData().getPrice();
		}
		return price;
	}

	
	//airtime calculations
	@Override
	public int airTime() {
		int totalTime = 0;
		//theres probably a better way to code this but it works!
		for (int i = 0; i < flights.size(); i++) {
			String time1 = "" + flights.get(i).getData().getDepartureTime();
			
			if (time1.length() < 4)
				time1 = "0" + time1;
			if (time1.length() <= 3)
				time1 = "0" + time1;
			if (time1.length() <= 2)
				time1 = "0" + time1;
			String time2 = "" + flights.get(i).getData().getArrivalTime();
			
			if (time2.length() < 4)
				time2 = "0" + time2;
			if (time2.length() <= 3)
				time2 = "0" + time1;
			if (time2.length() <= 2)
				time2 = "0" + time1;
			SimpleDateFormat format = new SimpleDateFormat("HHmm");
			
			//in case the flight went overnight
			Date date1 = null;
			Date date2 = null;

			try {
				date1 = format.parse(time1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				date2 = format.parse(time2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long difference = 0;
			if(date2.getTime() > date1.getTime()) difference = date2.getTime() - date1.getTime();
			if(date2.getTime() < date1.getTime()) difference = (date2.getTime() - date1.getTime()) + 24*60*60*1000;
			totalTime += difference;
		}
		return totalTime;
	}

	//roaming time at the airport
	@Override
	public int connectingTime() {
		int totalTime = 0;
		for (int i = 0; i < flights.size()-1; i++) {
			String time1 = "" + flights.get(i).getData().getArrivalTime();
			if (time1.length() < 3)
				time1 = "00" + time1;
			if (time1.length() < 4)
				time1 = "0" + time1;
			
			String time2 = "" + flights.get(i+1).getData().getDepartureTime();
			if (time1.length() < 3)
				time1 = "00" + time1;
			if (time2.length() < 4)
				time2 = "0" + time2;
			SimpleDateFormat format = new SimpleDateFormat("HHmm");
			Date date1 = null;
			Date date2 = null;

			try {
				date1 = format.parse(time1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				date2 = format.parse(time2);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long difference = 0;
			difference = date2.getTime() - date1.getTime();
			if(difference < 0) {
				difference += 24*60*60*1000;
			}
			totalTime += difference;
		}
		return totalTime;
	}

	@Override
	public int totalTime() {
		return airTime() + connectingTime();
	}
}
