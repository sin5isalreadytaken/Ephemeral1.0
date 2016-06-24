package messages;

public class VoiceMessage extends VoiceAndVideoMessage{
	private String Format;//voice recognitionresult, encode by utf-8
	private String Recognition;
	
	
	public String getFormat(){
		return Format;
	}
	public void setFormat(String format){
		this.Format = format;
	}
	
	public String getRecognition(){
		return Recognition;
	}
	public void setRecognition(String recognition){
		this.Recognition = recognition;
	}
}
