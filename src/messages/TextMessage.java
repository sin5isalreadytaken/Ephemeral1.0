package messages;

public class TextMessage extends BaseMessage{
	private String Content;
	
	public String getContent(){
		return Content;
	}
	public void setContent(String content){
		this.Content = content;
	}

}
