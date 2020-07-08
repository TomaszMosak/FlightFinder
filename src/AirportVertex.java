
public class AirportVertex {

	protected String airportCode;
	protected String airportName;
	protected String airportLongName;

	public AirportVertex(String code, String shortName, String longName) {
		this.airportCode = code;
		this.airportName = shortName;
		this.airportLongName = longName;
	}

	//Defines the 3 ways an airport is called (Code, Short, Long)
	public String getCode() {
		return this.airportCode;
	}

	public String getShortName() {
		return this.airportName;
	}

	public String getLongName() {
		return this.airportLongName;
	}
}
