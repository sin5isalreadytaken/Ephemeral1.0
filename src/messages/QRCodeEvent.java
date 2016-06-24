package messages;

public class QRCodeEvent extends MenuEvent{
	private String Ticket;
	
	public String getTicket(){
		return Ticket;
	}
	public void setTicket(String ticket){
		this.Ticket = ticket;
	}

}
