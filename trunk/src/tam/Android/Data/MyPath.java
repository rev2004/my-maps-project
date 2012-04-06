package tam.Android.Data;

import java.util.ArrayList;

public class MyPath {
	public ArrayList<Step> steps = new ArrayList<Step>();
	private Route route = new Route();

	public MyPath() {

	}

	public void addStep(Step step) {
		// TODO Auto-generated method stub
		steps.add(step);
	}

	public void editRoute(Route r) {
		// TODO Auto-generated method stub
		this.route = r;
	}

	public Route getRoute() {
		return route;
	}
	
	public ArrayList<Step> getStep(){
		return steps;
	}
}
