import org.jgrapht.graph.DefaultWeightedEdge;

//allows data to be passed into the defaultweightededge
public class RouteEdge extends DefaultWeightedEdge {

	private static final long serialVersionUID = 1346606194893923571L;
	protected Flight data;

	public void setData(Flight flight) {
		this.data = flight;
	}

	public Flight getData() {
		return this.data;
	}
}
