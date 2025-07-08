package com.mycompany.devbank;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "TransferenciaServlet", urlPatterns = {"/transferencias"})
public class TransferenciaServlet extends HttpServlet {

    private final int TAMANHO_PAGINA = 5;
    private final TransferenciaDAO dao = new TransferenciaDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String filtro = request.getParameter("filtro");
        if (filtro == null) filtro = "todos";

        String busca = request.getParameter("busca");
        if (busca == null) busca = "";

        int pagina = 1;
        try {
            pagina = Integer.parseInt(request.getParameter("pagina"));
            if (pagina < 1) pagina = 1;
        } catch (NumberFormatException e) {
            pagina = 1;
        }

        int totalRegistros = dao.contarTotal(filtro, busca);
        int totalPaginas = (int) Math.ceil((double) totalRegistros / TAMANHO_PAGINA);

        if (pagina > totalPaginas && totalPaginas > 0) {
            pagina = totalPaginas;
        }

        List<Transferencia> transferencias = dao.listarTransferencias(filtro, busca, pagina, TAMANHO_PAGINA);

        BigDecimal totalEnviado = dao.totalEnviado();
        BigDecimal totalRecebido = dao.totalRecebido();
        BigDecimal saldo = dao.saldo();
        List<DestinatarioFrequente> frequentes = dao.listarFrequentes();

        request.setAttribute("frequentes", frequentes);

        request.setAttribute("transferencias", transferencias);
        request.setAttribute("pagina", pagina);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("busca", busca);
        request.setAttribute("filtro", filtro);
        request.setAttribute("totalEnviado", totalEnviado);
        request.setAttribute("totalRecebido", totalRecebido);
        request.setAttribute("saldo", saldo);

        RequestDispatcher rd = request.getRequestDispatcher("transferencias.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
