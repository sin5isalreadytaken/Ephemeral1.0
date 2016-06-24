package messages;

public class BaseEvent extends Base{
	protected String Event;
	
	public String getEvent(){
		return Event;
	}
	public void setEvent(String event){
		this.Event = event;
	}

}
