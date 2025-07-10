package com.trabalho.devweb.infrastructure.controllers;

import com.trabalho.devweb.domain.Account;

public class StatementsController {
    @WebServlet(name = "StatementController", urlPatterns = "/extrato")
    public class StatementsController extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            Account account = (Account) request.getSession().getAttribute("account");

            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            request.getRequestDispatcher("/statement.jsp").forward(request, response);
        }
    }

}