package messages;

public class MenuEvent extends BaseEvent{
	protected String EventKey;
	
	public String getEventKey(){
		return EventKey;
	}
	public void setEventKey(String eventKey){
		this.EventKey = eventKey;
	}

}
