package service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import utils.MessageUtil;
import messages.Base;
import messages.NewsRespond;
import messages.TextRespond;

//TODO 
public class CoreServiceOrigin {
	private static String fromUserName;
	private static String toUserName;
	
	public static String processRequest(HttpServletRequest request){
		String respXml = null;
		String respContent = "unknown message type!";//the default return
		try{
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			fromUserName = requestMap.get("FromUserName");
			toUserName = requestMap.get("ToUserName");
			String msgType = requestMap.get("MsgType");
			
			TextRespond textRespond = new TextRespond();
			setFromToTime(textRespond);
			textRespond.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			
			NewsRespond newsRespond = new NewsRespond();
			setFromToTime(newsRespond);
			newsRespond.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			
			
			//文本消息
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
				respContent = "您发送的是文本消�?";
				
			}
			//图片消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)){
				respContent = "您发送的是图片消�?";
			}
			//语音消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)){
				respContent = "您发送的是语音消�?";
				String mediaId = requestMap.get("MediaId");
				String format = requestMap.get("Format");
				String recognition = requestMap.get("Recognition");//TODO 语音接口�?
//				System.out.println(recognition);
				respContent += recognition;
			}
			//视频消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)){
				respContent = "您发送的是视频消�?";
			}
			//短视频消�?
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO)){
				respContent = "您发送的是短视频消息";
			}
			//地理位置消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
				respContent = "您发送的是地理位置消�?";
			}
			//链接消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)){
				respContent = "您发送的是链接消�?";
			}
			//事件推�??
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
				String eventType = requestMap.get("Event");
				
				//关注
				if(eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
					respContent = "谢谢关注";
				}
				//取消关注
				else if(eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)){
					//TODO unsubscribe
					respContent = "谢谢�?�?";
				}
				//扫描带参数二维码
				else if(eventType.equals(MessageUtil.EVENT_TYPE_SCAN)){
					//TODO scan
					respContent = "扫码事件";
				}
				//上报地理位置
				else if(eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)){
					//TODO location
					respContent = "上报地理位置事件";
				}
				//自定义菜�?
				else if(eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
					//TODO click menu
					respContent = "点击菜单事件";
				}
			}
			
			textRespond.setContent(respContent);
//			respXml = MessageUtil.messageToXml(textRespond);
			respXml = MessageUtil.messageToXml(newsRespond);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return respXml;
	}
	
	public static void setFromToTime(Base base){
		base.setToUserName(fromUserName);
		base.setFromUserName(toUserName);
		base.setCreateTime(new Date().getTime());
	}

}
