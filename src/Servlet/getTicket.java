package Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.WeiXinService;

/**
 * Servlet implementation class getTicket
 */
@WebServlet("/getTicket")
public class getTicket extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public getTicket() {
        super();
        // TODO Auto-generated constructor stub
    }
    //»ñÈ¡¶þÎ¬Âë
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out=response.getWriter();
        String ticket=WeiXinService.getQrCodeTicket();
		out.print(ticket);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
