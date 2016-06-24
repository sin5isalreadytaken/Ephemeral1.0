package messages;

public class Base {
	protected String ToUserName;
	protected String FromUserName;
	protected long CreateTime;
	protected String MsgType;
	
	public String getToUserName(){
		return ToUserName;
	}
	public void setToUserName(String toUserName){
		this.ToUserName = toUserName;
	}
	
	public String getFromUserName(){
		return FromUserName;
	}
	public void setFromUserName(String fromUserName){
		this.FromUserName = fromUserName;
	}
	
	public long getCreateTime(){
		return CreateTime;
	}
	public void setCreateTime(long createTIme){
		this.CreateTime = createTIme;
	}
	
	public String getMsgType(){
		return MsgType;
	}
	public void setMsgType(String msgType){
		this.MsgType = msgType;
	}
}
