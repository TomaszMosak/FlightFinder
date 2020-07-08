import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsUnweightedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Itinerary implements IFlightItinerary {

	//easier to code with these being here
	private List<AirportVertex> vertices;
	private SimpleDirectedWeightedGraph<AirportVertex, RouteEdge> graph;
	HashSet<String[]> airlines;
	HashSet<String[]> airports;
	HashSet<String[]> routes;

	

	@Override
	//populates the itinerary
	public boolean populate(HashSet<String[]> airlines, HashSet<String[]> airports, HashSet<String[]> routes) {
		this.airlines = airlines;
		this.airports = airports;
		this.routes = routes;
		return true;
	}

	@Override
	public IItinerary leastCost(String to, String from) throws FlightItineraryException {
		//new lists of all the stops and flights
		List<AirportVertex> stops = new ArrayList<AirportVertex>();
		List<RouteEdge> flights = new ArrayList<RouteEdge>();

		//dummy airportvertex start and destinations
		AirportVertex avStart = null;
		AirportVertex avDestination = null;
		int startLoc = 0;
		int DestinationLocation = 0;
		
		//seperates the start airports to the ones that are destinations
		for (int i = 0; i < vertices.size(); i++) {
			if (from.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				startLoc = i;
				break;
			}
		}
		for (int i = 0; i < vertices.size(); i++) {
			if (to.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				DestinationLocation = i;
				break;
			}
		}
		avStart = vertices.get(startLoc);
		avDestination = vertices.get(DestinationLocation);
		// creates pathFinder aka shortest path finder
		DijkstraShortestPath<AirportVertex, RouteEdge> PathFind = new DijkstraShortestPath<AirportVertex, RouteEdge>(
				graph);
		GraphPath<AirportVertex, RouteEdge> path = PathFind.getPath(avStart, avDestination);
		// Set flight and stop data
		flights = path.getEdgeList();
		boolean firstRoute = true;
		for (RouteEdge r : flights) {
			if (firstRoute) {
				stops.add(graph.getEdgeSource(r));
				firstRoute = false;
			}
			stops.add(graph.getEdgeTarget(r));
		}
		System.out.println(String.format("%s \t %s \t\t\t %s \t\t %s \t\t %s \t\t %s", "Leg", "Leave", "At", "On",
				"Arrive", "At"));
		for (int i = 0; i < path.getLength(); i++) {
			String depTime;
			String arrTime;
			if (path.getEdgeList().get(i).getData().getDepartureTime() < 1000) {
				depTime = "0" + path.getEdgeList().get(i).getData().getDepartureTime();
			} else {
				depTime = "" + path.getEdgeList().get(i).getData().getDepartureTime();
			}
			if (path.getEdgeList().get(i).getData().getArrivalTime() < 1000) {
				arrTime = "0" + path.getEdgeList().get(i).getData().getArrivalTime();
			} else {
				arrTime = "" + path.getEdgeList().get(i).getData().getArrivalTime();
			}
			System.out.println(String.format("%s \t %s \t\t %s \t\t %s \t %s \t\t %s", i + 1,
					graph.getEdgeSource(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeSource(path.getEdgeList().get(i)).getCode(),
					depTime, path.getEdgeList().get(i).getData().getFlightNumber(),
					graph.getEdgeTarget(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeTarget(path.getEdgeList().get(i)).getCode(),
					arrTime));
		}
		InterfaceHelp si = new InterfaceHelp();
		si.setData(stops, flights);
		System.out.println(String.format("Total Time: %d hrs, %02d min", TimeUnit.MILLISECONDS.toHours(si.totalTime()),
				TimeUnit.MILLISECONDS.toMinutes(si.totalTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(si.totalTime()))));
		System.out.println(String.format("Time In Air: %d hrs, %02d min", TimeUnit.MILLISECONDS.toHours(si.airTime()),
				TimeUnit.MILLISECONDS.toMinutes(si.airTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(si.airTime()))));
		System.out.println("Total Cost: £" + si.totalCost());
		System.out.println("Total Changeovers: " + si.totalHop());
		return si;

	}

	@Override
	public IItinerary leastHop(String to, String from) throws FlightItineraryException {
		List<AirportVertex> stops = new ArrayList<AirportVertex>();
		List<RouteEdge> flights = new ArrayList<RouteEdge>();
		Graph<AirportVertex, RouteEdge> unweightedGraph = new AsUnweightedGraph<AirportVertex, RouteEdge>(graph);
		AirportVertex avStart = null;
		AirportVertex avDestination = null;
		int startLoc = 0;
		int DestinationLocation = 0;

		for (int i = 0; i < vertices.size(); i++) {
			if (from.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				startLoc = i;
				break;
			}
		}
		for (int i = 0; i < vertices.size(); i++) {
			if (to.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				DestinationLocation = i;
				break;
			}
		}
		avStart = vertices.get(startLoc);
		avDestination = vertices.get(DestinationLocation);
		DijkstraShortestPath<AirportVertex, RouteEdge> PathFind = new DijkstraShortestPath<AirportVertex, RouteEdge>(
				unweightedGraph);
		GraphPath<AirportVertex, RouteEdge> path = PathFind.getPath(avStart, avDestination);
		flights = path.getEdgeList();
		boolean firstRoute = true;
		for (RouteEdge r : flights) {
			if (firstRoute) {
				stops.add(graph.getEdgeSource(r));
				firstRoute = false;
			}
			stops.add(graph.getEdgeTarget(r));
		}
		InterfaceHelp si = new InterfaceHelp();
		si.setData(stops, flights);

		System.out.println(String.format("%s \t %s \t\t\t %s \t\t %s \t\t %s \t\t %s", "Leg", "Leave", "At", "On",
				"Arrive", "At"));
		for (int i = 0; i < path.getLength(); i++) {
			String depTime;
			String arrTime;
			if (path.getEdgeList().get(i).getData().getDepartureTime() < 100) {
				depTime = "00" + path.getEdgeList().get(i).getData().getDepartureTime();
			} else if (path.getEdgeList().get(i).getData().getDepartureTime() < 1000) {
				depTime = "0" + path.getEdgeList().get(i).getData().getDepartureTime();
			} else {
				depTime = "" + path.getEdgeList().get(i).getData().getDepartureTime();
			}

			if (path.getEdgeList().get(i).getData().getArrivalTime() < 100) {
				arrTime = "00" + path.getEdgeList().get(i).getData().getArrivalTime();
			} else if (path.getEdgeList().get(i).getData().getArrivalTime() < 1000) {
				arrTime = "0" + path.getEdgeList().get(i).getData().getArrivalTime();
			} else {
				arrTime = "" + path.getEdgeList().get(i).getData().getArrivalTime();
			}
			System.out.println(String.format("%s \t %s \t\t %s \t\t %s \t %s \t\t %s", i + 1,
					graph.getEdgeSource(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeSource(path.getEdgeList().get(i)).getCode(),
					depTime, path.getEdgeList().get(i).getData().getFlightNumber(),
					graph.getEdgeTarget(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeTarget(path.getEdgeList().get(i)).getCode(),
					arrTime));
		}

		System.out.println("Air Time: " + String.format("%d hrs, %02d min", TimeUnit.MILLISECONDS.toHours(si.airTime()),
				TimeUnit.MILLISECONDS.toMinutes(si.airTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(si.airTime()))));

		System.out.println("Total Time: " + String.format("%d hrs, %02d min",
				TimeUnit.MILLISECONDS.toHours(si.totalTime()), TimeUnit.MILLISECONDS.toMinutes(si.totalTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(si.totalTime()))));
		System.out.println("Total Cost: £" + si.totalCost());
		System.out.println("Total Changeovers: " + si.totalHop());
		return si;
	}

	
	//calls the cheapest route and sets the output
	@Override
	public IItinerary leastCost(String to, String from, List<String> excluding) throws FlightItineraryException {
		List<AirportVertex> stops = new ArrayList<AirportVertex>();
		List<RouteEdge> flights = new ArrayList<RouteEdge>();
		AirportVertex avStart = null;
		AirportVertex avDestination = null;
		int startLoc = 0;
		int DestinationLocation = 0;

		for (int i = 0; i < vertices.size(); i++) {
			if (from.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				startLoc = i;
				break;
			}
		}
		for (int i = 0; i < vertices.size(); i++) {
			if (to.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				DestinationLocation = i;
				break;
			}
		}
		avStart = vertices.get(startLoc);
		avDestination = vertices.get(DestinationLocation);
		GraphPath<AirportVertex, RouteEdge> path = null;

		path = getPathExc(avStart, avDestination, excluding);
		
		
		
		// Set flight and stop data
		flights = path.getEdgeList();
		boolean firstRoute = true;
		for (RouteEdge r : flights) {
			if (firstRoute) {
				stops.add(graph.getEdgeSource(r));
				firstRoute = false;
			}
			stops.add(graph.getEdgeTarget(r));
		}
		//formatting
		System.out.println(String.format("%s \t %s \t\t\t %s \t\t %s \t\t %s \t\t %s", "Leg", "Leave", "At", "On",
				"Arrive", "At"));
		for (int i = 0; i < path.getLength(); i++) {
			String depTime;
			String arrTime;
			if (path.getEdgeList().get(i).getData().getDepartureTime() < 1000) {
				depTime = "0" + path.getEdgeList().get(i).getData().getDepartureTime();
			} else {
				depTime = "" + path.getEdgeList().get(i).getData().getDepartureTime();
			}
			if (path.getEdgeList().get(i).getData().getArrivalTime() < 1000) {
				arrTime = "0" + path.getEdgeList().get(i).getData().getArrivalTime();
			} else {
				arrTime = "" + path.getEdgeList().get(i).getData().getArrivalTime();
			}
			System.out.println(String.format("%s \t %s \t\t %s \t\t %s \t %s \t\t %s", i + 1,
					graph.getEdgeSource(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeSource(path.getEdgeList().get(i)).getCode(),
					depTime, path.getEdgeList().get(i).getData().getFlightNumber(),
					graph.getEdgeTarget(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeTarget(path.getEdgeList().get(i)).getCode(),
					arrTime));
		}
		InterfaceHelp si = new InterfaceHelp();
		si.setData(stops, flights);
		System.out.println(String.format("%d hrs, %02d min", TimeUnit.MILLISECONDS.toHours(si.totalTime()),
				TimeUnit.MILLISECONDS.toMinutes(si.totalTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(si.totalTime()))));
		System.out.println(String.format("%d hrs, %02d min", TimeUnit.MILLISECONDS.toHours(si.airTime()),
				TimeUnit.MILLISECONDS.toMinutes(si.airTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(si.airTime()))));
		System.out.println("Total Cost: £" + si.totalCost());
		System.out.println("Total Changeovers: " + si.totalHop());
		return si;
	}
	
	//exclusion
	public GraphPath<AirportVertex, RouteEdge> getPathExcHops(AirportVertex start, AirportVertex target, List<String> excluding) {
		Graph<AirportVertex, RouteEdge> unweightedGraph = new AsUnweightedGraph<AirportVertex, RouteEdge>(graph);
		AllDirectedPaths<AirportVertex, RouteEdge> PathFind2 = new AllDirectedPaths<AirportVertex, RouteEdge>(unweightedGraph);
		List<GraphPath<AirportVertex, RouteEdge>> paths = bubbleSort(PathFind2.getAllPaths(start, target, true, 4));
		List<AirportVertex> exc = new ArrayList<AirportVertex>();
		Iterator<AirportVertex> airIt = vertices.iterator();
		while(airIt.hasNext()) {
			AirportVertex av = airIt.next();
			for(String s : excluding) {
				if(av.getShortName().toLowerCase().equals(s.toLowerCase())) {
					exc.add(av);
				}
			}
		}
		Iterator<GraphPath<AirportVertex, RouteEdge>> it = paths.iterator();
		
		//makes sure the excluded airport isn't used
		while(it.hasNext()) {
			boolean usePath = true;
			Iterator<AirportVertex> excIt = exc.iterator();

			GraphPath<AirportVertex, RouteEdge> temp = it.next();
			int counter = 0;

			while(excIt.hasNext()) {
				
				AirportVertex av = excIt.next();
				for(int i = 0; i < temp.getEdgeList().size(); i++) {
					if(graph.getEdgeSource(temp.getEdgeList().get(i)) == exc.get(counter) || graph.getEdgeTarget(temp.getEdgeList().get(i)) == exc.get(counter)) {
						usePath = false;
					}
					System.out.println(i + ": " + usePath);
				}
				counter++;

			}
			if(usePath) {
			return temp;
			}
		}
		System.out.println("Cannot find ath");
		return null;
	}
	
	//exclusion once again (2 ways of getting routes)
	public GraphPath<AirportVertex, RouteEdge> getPathExc(AirportVertex start, AirportVertex target, List<String> excluding) {
		AllDirectedPaths<AirportVertex, RouteEdge> PathFind2 = new AllDirectedPaths<AirportVertex, RouteEdge>(graph);
		List<GraphPath<AirportVertex, RouteEdge>> paths = bubbleSort(PathFind2.getAllPaths(start, target, true, 4));
		List<AirportVertex> exc = new ArrayList<AirportVertex>();
		Iterator<AirportVertex> airIt = vertices.iterator();
		while(airIt.hasNext()) {
			AirportVertex av = airIt.next();
			for(String s : excluding) {
				if(av.getShortName().toLowerCase().equals(s.toLowerCase())) {
					exc.add(av);
				}
			}
		}
		Iterator<GraphPath<AirportVertex, RouteEdge>> it = paths.iterator();
		List<GraphPath<AirportVertex, RouteEdge>> availablePaths = new ArrayList<GraphPath<AirportVertex, RouteEdge>>();
		while(it.hasNext()) {
			boolean usePath = true;
			Iterator<AirportVertex> excIt = exc.iterator();
			
			GraphPath<AirportVertex, RouteEdge> temp = it.next();
			int counter = 0;

			while(excIt.hasNext()) {
				
				AirportVertex av = excIt.next();
				for(int i = 0; i < temp.getEdgeList().size(); i++) {
					if(graph.getEdgeSource(temp.getEdgeList().get(i)) == exc.get(counter) || graph.getEdgeTarget(temp.getEdgeList().get(i)) == exc.get(counter)) {
						usePath = false;
					}
				}
				counter++;

			}
			if(usePath) {
			availablePaths.add(temp);
			}
		}
		GraphPath<AirportVertex, RouteEdge> tempPath = null;
		for(int i = 0; i < availablePaths.size(); i++) {
			for(int j = 1; j < availablePaths.size()-i; j++) {
				if(availablePaths.get(j-1).getWeight() > availablePaths.get(j).getWeight()) {
					tempPath = availablePaths.get(j-1);
					availablePaths.set(j-1, availablePaths.get(j));
					availablePaths.set(j, tempPath);
				}
			}
		}
		return availablePaths.get(0);
	}
	
	//bubble sort
	static List<GraphPath<AirportVertex, RouteEdge>> bubbleSort(List<GraphPath<AirportVertex, RouteEdge>> paths) {  
        int n = paths.size();  
        GraphPath<AirportVertex, RouteEdge> temp = null; 
        GraphPath<AirportVertex, RouteEdge> temp1 = null; 
         for(int i=0; i < n; i++){  
                 for(int j=1; j < (n-i); j++){  
                          if(paths.get(j-1).getLength() > paths.get(j).getLength()){  
                                 //swaps elements  
                                 temp = paths.get(j-1);
                                 temp1 = paths.get(j);
                                 paths.set(j, temp);
                                 paths.set(j-1, temp1);
                         }  
                          
                 }  
         }
		return paths;
	}

	//calls the route with least amount of changeovers and creates the output
	@Override
	public IItinerary leastHop(String to, String from, List<String> excluding) throws FlightItineraryException {
		List<AirportVertex> stops = new ArrayList<AirportVertex>();
		List<RouteEdge> flights = new ArrayList<RouteEdge>();
		Graph<AirportVertex, RouteEdge> unweightedGraph = new AsUnweightedGraph<AirportVertex, RouteEdge>(graph);
		AirportVertex avStart = null;
		AirportVertex avDestination = null;
		int startLoc = 0;
		int DestinationLocation = 0;

		
		for (int i = 0; i < vertices.size(); i++) {
			if (from.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				startLoc = i;
				break;
			}
		}
		
		for (int i = 0; i < vertices.size(); i++) {
			if (to.toLowerCase().equals(vertices.get(i).getCode().toLowerCase())) {
				DestinationLocation = i;
				break;
			}
		}
		
		avStart = vertices.get(startLoc);
		avDestination = vertices.get(DestinationLocation);
		
		
		GraphPath<AirportVertex, RouteEdge> path = getPathExcHops(avStart, avDestination, excluding);
		flights = path.getEdgeList();
		//finds the start point
		boolean firstRoute = true;
		for (RouteEdge route : flights) {
			if (firstRoute) {
				stops.add(graph.getEdgeSource(route));
				firstRoute = false;
			}
			stops.add(graph.getEdgeTarget(route));
		}
		InterfaceHelp help = new InterfaceHelp();
		help.setData(stops, flights);

		//formatting
		System.out.println(String.format("%s \t %s \t\t\t %s \t\t %s \t\t %s \t\t %s", "Leg", "Leave", "At", "On","Arrive", "At"));
		for (int i = 0; i < path.getLength(); i++) {
			//24 hour clock - always 4 digits
			String depTime;
			String arrTime;
			if (path.getEdgeList().get(i).getData().getDepartureTime() < 100) {
				depTime = "00" + path.getEdgeList().get(i).getData().getDepartureTime();
			} else if (path.getEdgeList().get(i).getData().getDepartureTime() < 1000) {
				depTime = "0" + path.getEdgeList().get(i).getData().getDepartureTime();
			} else {
				depTime = "" + path.getEdgeList().get(i).getData().getDepartureTime();
			}

			if (path.getEdgeList().get(i).getData().getArrivalTime() < 100) {
				arrTime = "00" + path.getEdgeList().get(i).getData().getArrivalTime();
			} else if (path.getEdgeList().get(i).getData().getArrivalTime() < 1000) {
				arrTime = "0" + path.getEdgeList().get(i).getData().getArrivalTime();
			} else {
				arrTime = "" + path.getEdgeList().get(i).getData().getArrivalTime();
			}
			System.out.println(String.format("%s \t %s \t\t %s \t\t %s \t %s \t\t %s", i + 1,
					graph.getEdgeSource(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeSource(path.getEdgeList().get(i)).getCode(),
					depTime, path.getEdgeList().get(i).getData().getFlightNumber(),
					graph.getEdgeTarget(path.getEdgeList().get(i)).getShortName() + " "
							+ graph.getEdgeTarget(path.getEdgeList().get(i)).getCode(),
					arrTime));
		}

		System.out.println("Air Time: " + String.format("%d hrs, %02d min", TimeUnit.MILLISECONDS.toHours(help.airTime()),
				TimeUnit.MILLISECONDS.toMinutes(help.airTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(help.airTime()))));

		System.out.println("Total Time: " + String.format("%d hrs, %02d min",
				TimeUnit.MILLISECONDS.toHours(help.totalTime()), TimeUnit.MILLISECONDS.toMinutes(help.totalTime())
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(help.totalTime()))));
		System.out.println("Total Cost: £" + help.totalCost());
		System.out.println("Total Changeovers: " + help.totalHop());
		return help;
	}

	
	//NOT IMPLEMENTED FUNCTIONS - SORRY! :'(
	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlightItineraryException {
		return null;
	}

	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlightItineraryException {
		return null;
	}

	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlightItineraryException {
		return null;
	}
	//NOT IMPLEMENTED FUNCTIONS - SORRY! :'(
	
	

	public void setData() {
		this.vertices = new ArrayList<AirportVertex>();
		this.graph = new SimpleDirectedWeightedGraph<AirportVertex, RouteEdge>(RouteEdge.class);

		// FlightReader
		FlightsReader fr = null;
		try {
			fr = new FlightsReader(FlightsReader.MOREAIRLINECODES);
		} catch (FileNotFoundException | FlightItineraryException e) {
			System.out.println("Error initialising the flight reader");
			System.exit(0);
		}

		//nice and easy populate function creating a iterator over the files which adds to the graph
		populate(fr.getAirlines(), fr.getAirports(), fr.getRoutes());
		
		// AirportVertex holding all the information about the airports aka the Vertices
		for (String[] temp : airports) {
			AirportVertex av = new AirportVertex(temp[0], temp[1], temp[2]);
			graph.addVertex(av);
			// Add the vertex to av - AirportVertex
			vertices.add(av);
		}

		// Same as above but for routes aka the edges
		Iterator<String[]> it = fr.getRoutes().iterator();

		
		//figures out which part of the routes are the start of the route and which are the end
		while (it.hasNext()) {
			RouteEdge route = new RouteEdge();
			int StartEdge = 0;
			int EndEdge = 0;
			String[] temp = it.next();
			for (int i = 0; i < vertices.size(); i++) {
				if (vertices.get(i).getCode().equals(temp[1])) {
					StartEdge = i;
					break;
				}
			}
			for (int i = 0; i < vertices.size(); i++) {
				if (vertices.get(i).getCode().equals(temp[3])) {
					EndEdge = i;
					break;
				}
			}


			//adds start edges and vertices together & end edges and destination vertices together
			graph.addEdge(vertices.get(StartEdge), vertices.get(EndEdge), route);
			// saves this info as a 'FLIGHT'
			route.setData(new Flight(temp[0], Integer.parseInt(temp[2]), Integer.parseInt(temp[4]),
					Double.parseDouble(temp[5])));
			// sets weight as the price
			graph.setEdgeWeight(route, route.getData().getPrice());
		}
	}
	

}
