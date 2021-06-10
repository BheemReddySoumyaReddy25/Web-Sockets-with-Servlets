package com.websockets;

import java.io.IOException;
//import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
/**
 * Servlet implementation class UserServlet
 */

@WebServlet("/login")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UserServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    static RequestDispatcher rd;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		//PrintWriter printWriter = response.getWriter();
		
		HttpSession session = request.getSession(true);
		
		String username = request.getParameter("username");
		
		session.setAttribute("username", username);
		
		if(username!=null && username.equals("doctor")) {
			rd = request.getRequestDispatcher("/doctor.jsp");
			rd.forward(request, response);
		}
		else if(username!=null && username.equals("ambulance")) {
			rd = request.getRequestDispatcher("/ambulance.jsp");
			rd.forward(request, response);
		}
		else if(username!=null) {
			rd = request.getRequestDispatcher("/patient.jsp");
			rd.forward(request, response);
		}
	}

}
