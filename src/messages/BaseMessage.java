package messages;

public class BaseMessage extends Base{
	protected long MsgId;
	
	public long getMsgId(){
		return MsgId;
	}
	public void setMsgId(long msgId){
		this.MsgId = msgId;
	}

}
