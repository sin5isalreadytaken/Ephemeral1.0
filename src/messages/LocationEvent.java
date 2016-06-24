package messages;

public class LocationEvent extends BaseEvent{
	private String Latitude;
	private String Longitude;
	private String Precision;
	
	public String getLatitude(){
		return Latitude;
	}
	public void setLatitude(String latitude){
		this.Latitude = latitude;
	}
	
	public String getLongitude(){
		return Longitude;
	}
	public void setLongitude(String longitude){
		this.Longitude = longitude;
	}
	
	public String getPrecision(){
		return Precision;
	}
	public void setPrecision(String precision){
		this.Precision = precision;
	}

}
