
public class Flight {

	protected int departureTime, arrivalTime, flightDuration;
	protected String FlightNumber;
	protected double price;
	protected String[] detailsRoute = new String[5];

	//Gets the details of each route into a nice list which we use to display all the extra info
	public Flight(String FlightNumber, int departureTime, int arrivalTime, double price) {
		detailsRoute[0] = this.FlightNumber = FlightNumber;
		detailsRoute[1] = Integer.toString(this.departureTime = departureTime);
		detailsRoute[3] = Integer.toString(this.arrivalTime = arrivalTime);
		detailsRoute[4] = Double.toString(this.price = price);
		detailsRoute[2] = Integer.toString(this.flightDuration = (arrivalTime - departureTime));

	}

	public void setFlightNumber(String FlightNumber) {
		this.FlightNumber = FlightNumber;
		detailsRoute[0] = FlightNumber;
	}

	public String getFlightNumber() {
		return this.FlightNumber;
	}

	public void setDepartureTime(int departureTime) {
		this.departureTime = departureTime;
		this.flightDuration = this.arrivalTime - this.departureTime;
		detailsRoute[1] = Integer.toString(departureTime);
		calculateFlightDuration();
	}

	public int getDepartureTime() {
		return this.departureTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
		this.flightDuration = this.arrivalTime - this.departureTime;
		detailsRoute[3] = Integer.toString(arrivalTime);
		calculateFlightDuration();
	}

	public int getArrivalTime() {
		return this.arrivalTime;
	}

	public void setPrice(double price) {
		this.price = price;
		detailsRoute[4] = Double.toString(price);
	}

	public double getPrice() {
		return this.price;
	}
	
	//gets the duration by taking away the departure time from the arrival time
	public void calculateFlightDuration() {
		this.flightDuration = getArrivalTime() - getDepartureTime();
		detailsRoute[2] = Integer.toString(this.flightDuration);
	}

	public int getFlightDuration() {
		return this.flightDuration;
	}

	//actually sets all the data for each route, nice wee loop!
	public void setdetailsRoute(String[] details) {
		for (int i = 0; i < details.length; i++) {
			detailsRoute[i] = details[i];
		}
		setFlightNumber(detailsRoute[0]);
		setDepartureTime(Integer.parseInt(detailsRoute[1]));
		setArrivalTime(Integer.parseInt(detailsRoute[3]));
		calculateFlightDuration();
		setPrice(Double.parseDouble(detailsRoute[4]));

	}

	public String[] getdetailsRoute() {
		return this.detailsRoute;
	}

}
