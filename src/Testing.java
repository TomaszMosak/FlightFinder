import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class Testing {
	FlightItinerary fi = new FlightItinerary();

	

	@Test
	public void leastCostTest() {
		IItinerary i = fi.leastCost("EDI", "DXB");
		assertEquals(2,i.totalHop());
		assertEquals(369,i.totalCost());
	}
	@Test
	public void leastHopsTest() {
		IItinerary i = fi.leastHops("EDI", "HKG");
		assertEquals(2,i.totalHop());
	}
	
	@Test
	public void leastHopsExcludingAirportsTest() {
		List<String> ex = new ArrayList<String>();
		ex.add("CDG");
		IItinerary i = fi.leastHops("EDI", "HKG", ex);
		Iterator<String> it = i.getStops().iterator();
		boolean found = false;
		while(it.hasNext()) {
			String temp = it.next();
			if(temp.equals("SAW")) {
				found = true;
			}
		}
		assertFalse(found);
	}
	@Test
	public void leastCostExcludingAirportsTest() {
		List<String> ex = new ArrayList<String>();
		ex.add("CDG");
		IItinerary i = fi.leastHops("EDI", "HKG", ex);
		Iterator<String> it = i.getStops().iterator();
		boolean found = false;
		while(it.hasNext()) {
			String temp = it.next();
			if(temp.equals("SAW")) {
				found = true;
			}
		}
		assertFalse(found);
	}
}
