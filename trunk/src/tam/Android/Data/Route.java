package tam.Android.Data;

public class Route {
	public String summary;
	public String distance;
	public String duration;
	
	public Route(String summary, String distance, String duration){
		this.summary = summary;
		this.distance = distance;
		this.duration = duration;
	}
	
	public Route(Route t)
	{
		this(t.summary, t.distance, t.duration);
	}
	
	public Route()
	{
		summary = distance = duration = "";
	}
}
