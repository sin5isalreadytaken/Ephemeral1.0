package messages;

public class VideoMessage extends VoiceAndVideoMessage{
	private String ThumbMediaId;
	
	public String getThumbMediaId(){
		return ThumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId){
		this.ThumbMediaId = thumbMediaId;
	}

}
