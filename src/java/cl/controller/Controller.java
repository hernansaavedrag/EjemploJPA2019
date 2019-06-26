package cl.controller;

import cl.model.ServicioLocal;
import java.io.IOException;
import java.io.PrintWriter;
import cl.entities.*;
import java.util.*;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sistemas
 */
@WebServlet(name = "Controller", urlPatterns = {"/control.do"})
public class Controller extends HttpServlet {

    @EJB
    private ServicioLocal servicio;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String bt = request.getParameter("bt");
        switch (bt) {
            case "addcat":
                addcat(request, response);
                break;
            case "editcat":
                editcat(request, response);
                break;
            case "adduser":
                this.adduser(request, response);
                break;
            case "addprod":
                this.addprod(request, response);
                break;
            case "editprodes":
                this.editprodes(request, response);
                break;
            case "iniciar":
                this.iniciarSesion(request, response);
                break;
            case "addcar":
                this.addcar(request, response);
                break;

        }

    }

    protected void addcar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        Producto p = servicio.buscarProducto(Integer.parseInt(codigo));
        
        ArrayList<Producto> carro = (ArrayList) request.getSession().getAttribute("carro");
        
        if(carro == null){
            carro = new ArrayList<>();
        }
        
       if (!carro.contains(p)) {
           carro.add(p);
           request.getSession().setAttribute("carro", carro);
       }

       response.sendRedirect("venta.jsp");
       
    }

    protected void iniciarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rut = request.getParameter("rut");
        String clave = request.getParameter("clave");

        Usuario user = servicio.iniciarSesion(rut, Hash.md5(clave));

        if (user != null) {

            if (user.getTipo().equals("admin")) {
                request.getSession().setAttribute("admin", user);
                response.sendRedirect("admin.jsp");

            } else if (user.getTipo().equals("vendedor")) {
                request.getSession().setAttribute("vendedor", user);
                response.sendRedirect("vendedor.jsp");
            } else {
                request.getSession().setAttribute("cliente", user);
                response.sendRedirect("cliente.jsp");
            }
        } else {
            request.setAttribute("msg", "usuario incorrecto");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }

    }

    protected void addprod(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String precio = request.getParameter("precio");
        String stock = request.getParameter("stock");
        String codcat = request.getParameter("codcat");

        Categoria cat = servicio.buscarCategoria(Integer.parseInt(codcat));

        Producto newprod = new Producto();
        newprod.setNombre(nombre);
        newprod.setPrecio(Integer.parseInt(precio));
        newprod.setStock(Integer.parseInt(stock));
        newprod.setEstado(1);
        newprod.setCodigocategoria(cat);
        servicio.insertar(newprod);

        cat.getProductoList().add(newprod);
        servicio.sincronizar(cat);

        response.sendRedirect("producto.jsp");

    }

    protected void editprodes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cod = request.getParameter("codigo");
        String est = request.getParameter("estado");
        String pre = request.getParameter("precio");

        int codigo = Integer.parseInt(cod);
        int estado = Integer.parseInt(est);
        int precio = Integer.parseInt(pre);

        servicio.editarProducto(codigo, precio, 0, estado);

        response.sendRedirect("producto.jsp");

    }

    protected void adduser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rut = request.getParameter("rut");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String clave = request.getParameter("clave");
        String tipo = request.getParameter("tipo");

        if (servicio.buscarUsuario(rut) == null) {
            Usuario newUser = new Usuario();
            newUser.setRut(rut);
            newUser.setNombre(nombre);
            newUser.setApellido(apellido);
            newUser.setEmail(email);
            newUser.setTipo(tipo);
            newUser.setClave(Hash.md5(clave));
            servicio.insertar(newUser);
            response.sendRedirect("usuario.jsp");
        } else {
            request.setAttribute("msg", "Rut ya ingresado");
            request.getRequestDispatcher("usuario.jsp").forward(request, response);
        }

    }

    protected void addcat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String desc = request.getParameter("desc");

        Categoria newCat = new Categoria();
        newCat.setNombre(nombre);
        newCat.setDescripcion(desc);
        newCat.setEstado(1);

        servicio.insertar(newCat);

        response.sendRedirect("categoria.jsp");

    }

    protected void editcat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cod = request.getParameter("codigo");
        String est = request.getParameter("estado");

        int codigo = Integer.parseInt(cod);
        int estado = Integer.parseInt(est);

        servicio.editarCategoria(codigo, estado);

        response.sendRedirect("categoria.jsp");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
