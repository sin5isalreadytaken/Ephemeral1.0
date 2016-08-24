package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ChatService;
import service.CoreService;
import utils.PrintWriterHelp;
import utils.SignUtil;

/**
 * Servlet implementation class CoreServlet
 */
@WebServlet("/CoreServlet")
public class CoreServlet extends HttpServlet {
	
	/**
	 * 如果在web.xml中给Servlet配置了load-on-startup元素，web应用程序启东市就会加载该Servlet并调用它的init()方法
	 * 创建索引
	 */
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		File indexDir = new File(ChatService.getIndexDir());
		//如果索引目录不存在则创建索引
		if (!indexDir.exists()){
			ChatService.createIndex();
		}
	}

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		PrintWriter out = PrintWriterHelp.getPrintWriter(request, response);
		if (SignUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
		out.flush();
		out.close();
		out = null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		
		PrintWriter out = PrintWriterHelp.getPrintWriter(request, response);
		if (SignUtil.checkSignature(signature, timestamp, nonce)){
			String respXml = CoreService.processRequest(request);
			out.print(respXml);
		}
		out.flush();
		out.close();
		out = null;
	}

}
