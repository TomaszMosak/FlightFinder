
import java.util.List;

public interface IItinerary {

	/** Returns the list of airports codes composing the itinerary */
	List<String> getStops();
	
	/** Returns the list of flight codes composing the itinerary */
	List<String> getFlights();
	
	/** Returns the number of connections of the itinerary */
	int totalHop();
	
	/** Returns the total cost of the itinerary */
	int totalCost();
	
	/** Returns the total time in flight of the itinerary */
	int airTime();
	
	/** Returns the total time in connection of the itinerary */
	int connectingTime();
	
	/** Returns the total travel time of the itinerary */
	int totalTime();
}
