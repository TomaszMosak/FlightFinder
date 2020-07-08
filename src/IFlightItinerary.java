
import java.util.HashSet;
import java.util.List;

public interface IFlightItinerary {

	/**
	 * Populates the graph with the airlines, airports and routes information.
	 * Returns true if the operation was successful.
	 */
	boolean populate(HashSet<String[]> airlines, HashSet<String[]> airports, HashSet<String[]> routes);

	/**
	 * Returns the cheapest flight itinerary from one airport (airport code) to
	 * another
	 */
	IItinerary leastCost(String to, String from) throws FlightItineraryException;

	/**
	 * Returns least connections flight itinerary from one airport (airport code) to
	 * another
	 */
	IItinerary leastHop(String to, String from) throws FlightItineraryException;

	/**
	 * Returns the cheapest flight itinerary from one airport (airport code) to
	 * another, excluding a list of airport (airport codes)
	 */
	IItinerary leastCost(String to, String from, List<String> excluding) throws FlightItineraryException;

	/**
	 * Returns least connections flight itinerary from one airport (airport code) to
	 * another, excluding a list of airport (airport codes)
	 */
	IItinerary leastHop(String to, String from, List<String> excluding) throws FlightItineraryException;

	/**
	 * Returns the airport code of the best airport for the meet up of two people
	 * located in two different airports (airport codes) accordingly to the
	 * itineraries costs
	 */
	String leastCostMeetUp(String at1, String at2) throws FlightItineraryException;

	/**
	 * Returns the airport code of the best airport for the meet up of two people
	 * located in two different airports (airport codes) accordingly to the number
	 * of connections
	 */
	String leastHopMeetUp(String at1, String at2) throws FlightItineraryException;

	/**
	 * Returns the airport code of the best airport for the earliest meet up of two
	 * people located in two different airports (airport codes) when departing at a
	 * given time
	 */
	String leastTimeMeetUp(String at1, String at2, String startTime) throws FlightItineraryException;

}
