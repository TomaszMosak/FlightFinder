import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlightItinerary {

	public static void main(String[] args) throws FileNotFoundException, FlightItineraryException {
		String in1, in2;
		Scanner inRead = new Scanner(System.in);
		System.out.println("Enter your starting location");
		in1 = inRead.nextLine().toLowerCase();
		System.out.println("Enter destination");
		in2 = inRead.nextLine().toLowerCase();
		inRead.close();
		
		//partA(in1, in2);
		//partB(in1, in2);
		partC(in1, in2);
	}

	public static void partA(String start, String target) {
		// create graph object with weight and direction
		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		// array of locations
		String[] locations = { "edinburgh", "heathrow", "sydney", "dubai", "kuala lumpur" };
		// flights are edges and vertices are the places
		addVerticesA(graph, locations);
		addEdgesA(graph, locations);
		

		// Find the shortest path between vertices
		DijkstraShortestPath<String, DefaultWeightedEdge> pathFinder = new DijkstraShortestPath<String, DefaultWeightedEdge>(
				graph);
		// Create a list of all the edges
		List<DefaultWeightedEdge> edgeList = pathFinder.getPath(start, target).getEdgeList();
		// Output
		System.out.println("Shortest (i.e. cheapest) path: ");
		// Loop through the edges to get the cheapest path shown
		for (int i = 0; i < edgeList.size(); i++) {
			String out = String.format("%d. %s -> %s", i + 1,
					capital(graph.getEdgeSource(edgeList.get(i)).toString()),
					capital(graph.getEdgeTarget(edgeList.get(i)).toString()));
			System.out.println(out);
		}
		// formatting for the price (decimal)
		DecimalFormat df = new DecimalFormat("#.00");
		String price = df.format(pathFinder.getPathWeight(start, target));
		System.out.println("Cost of shortest (i.e cheapest) path = £" + price);
	}

	// Method for capitalising first letter of string
	public static String capital(String s) {
		// Split word at spaces
		String[] words = s.split("\\s");
		String out = "";
		// capital the first letter of each word
		for (String str : words) {
			out += str.substring(0, 1).toUpperCase() + str.substring(1, str.length()) + "";
		}
		return out;
	}

	//creates the vertex's for the graph
	public static void addVerticesA(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph, String[] locations) {
		for (String i : locations) {
			graph.addVertex(i);
		}
	}

	//adds all the edges
	public static void addEdgesA(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph, String[] locations) {
		graph.setEdgeWeight(graph.addEdge(locations[0], locations[1]), 80);
		graph.setEdgeWeight(graph.addEdge(locations[1], locations[0]), 80);

		graph.setEdgeWeight(graph.addEdge(locations[0], locations[3]), 190);
		graph.setEdgeWeight(graph.addEdge(locations[3], locations[0]), 190);

		graph.setEdgeWeight(graph.addEdge(locations[3], locations[1]), 130);
		graph.setEdgeWeight(graph.addEdge(locations[1], locations[3]), 130);

		graph.setEdgeWeight(graph.addEdge(locations[1], locations[2]), 570);
		graph.setEdgeWeight(graph.addEdge(locations[2], locations[1]), 570);

		graph.setEdgeWeight(graph.addEdge(locations[3], locations[4]), 170);
		graph.setEdgeWeight(graph.addEdge(locations[4], locations[3]), 170);

		graph.setEdgeWeight(graph.addEdge(locations[2], locations[4]), 150);
		graph.setEdgeWeight(graph.addEdge(locations[4], locations[2]), 150);
	}


	public static void partB(String start, String target) throws FileNotFoundException, FlightItineraryException {
		Itinerary itin = new Itinerary();
		// makes graph
		itin.setData();
		System.out.println("Cheapest Route: ");
		// finds cheapest route
		itin.leastCost(target, start);
		}
	

	public static void partC(String start, String target) throws FileNotFoundException, FlightItineraryException {
		Itinerary iti = new Itinerary();
		//makes graph
		iti.setData();
		System.out.println("Least Changeovers: ");
		// finds route with least changeovers
		iti.leastHop(target, start);
	}
	public IItinerary leastCost(String start, String target) {
		//makes a new itinerary
		Itinerary iti = new Itinerary();
		//makes graph
		iti.setData();

		try {
			// Calculate cheapest route
			return iti.leastCost(target, start);
		} catch (FlightItineraryException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	
	
	//functions that help with the calculations for cheapest route and route with least changeovers
	
	//jumps
	public IItinerary leastHops(String start, String target) {
		Itinerary iti = new Itinerary();
		iti.setData();

		try {
			return iti.leastHop(target, start);
		} catch (FlightItineraryException e) {
			e.printStackTrace();
		}
		return null;
	} 
	public IItinerary leastHops(String start, String target, List<String> excluding) {
		Itinerary iti = new Itinerary();
		iti.setData();

		try {
			return iti.leastHop(target, start, excluding);
		} catch (FlightItineraryException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	
	//cheapest
	public IItinerary leastCost(String start, String target, List<String> excluding) {
		Itinerary iti = new Itinerary();
		iti.setData();

		try {
			return iti.leastCost(target, start, excluding);
		} catch (FlightItineraryException e) {
			e.printStackTrace();
		}
		return null;
	} 
}
