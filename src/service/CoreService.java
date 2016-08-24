package service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import messages.TextMessage;
import utils.MessageUtil;

public class CoreService {
	/**
	 * 处理微信发来的信息
	 * 
	 * @param request
	 * @return xml
	 */
	public static String processRequest(HttpServletRequest request){
		//xml格式的消息数据
		String respXml = null;
		//默认返回的文本消息内容
		String respContent = MessageUtil.DEFAULT_RESP_ANSWER;
		try {
			//调用parseXml方法解析请求消息
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			String fromUserName = requestMap.get("FromUserName");
			String toUserName = requestMap.get("ToUserName");
			String msgType = requestMap.get("MsgType");
			String createTime = requestMap.get("CreateTime");
			
			//文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
				String content = requestMap.get("Content");
				respContent = ChatService.chat(fromUserName, createTime, content);
			}
			//回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setContent(respContent);
			//将文本消息对象转换成XML
			respXml = MessageUtil.messageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respXml;
	}
	
}
