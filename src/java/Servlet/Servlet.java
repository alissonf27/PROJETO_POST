package Servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Controller.RoleJpaController;
import Controller.UserJpaController;
import Entidades.Role;
import Entidades.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alissonfernando
 */
@WebServlet(urlPatterns = {"/feed/Servlet"})
public class Servlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet servlet_formulario</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet servlet_formulario at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        if (request.getParameter("btnlogar") != null){
            logarUsuario(request, response);
        } else if (request.getParameter("btncadastrar") != null){
            cadastrarUsuario(request, response);
        } else if (request.getParameter("btnalterarsenha") != null){
            redefinirSenha(request, response);
        }
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
    
    public void cadastrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            String username = request.getParameter("username");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String confirm_senha = request.getParameter("confirm_senha");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("PROJETO_POSTPU");
            UserJpaController objJpa = new UserJpaController(emf);
            RoleJpaController roleJpa = new RoleJpaController(emf);

            User user = new User();
            user.setUsername(username);
            user.setName(nome);
            user.setEmail(email);
            user.setPassword(senha);
            user.setRole(roleJpa.findRole(1));

            String result;
            if (senha.equals(confirm_senha)) {
                objJpa.create(user);
                if(objJpa.findUser(user.getIduser()).getEmail().equals(user.getEmail())){
                    result = "Usuário já cadastrado!";
                } else {
                    result = "Usuário cadastrado com sucesso!";
                }
            } else {
                result = "Senha não confirmada!";
            }
            
            request.setAttribute("resp", result);
            request.getRequestDispatcher("/feed/formulario.jsp").forward(request, response);
            processRequest(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void logarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            String email = request.getParameter("email_login");
            String senha = request.getParameter("senha_login");
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("PROJETO_POSTPU");
            UserJpaController userJpa = new UserJpaController(emf);
            RoleJpaController roleJpa = new RoleJpaController(emf);
            
            List<User> listUser = new ArrayList();
            listUser = userJpa.findUserEntities();
            
            String username= "";
            String result= "Email ou senha inválido!";
            for(User u : listUser){
                if(userJpa.findUser(u.getIduser()).getEmail().equals(email) && userJpa.findUser(u.getIduser()).getPassword().equals(senha)){
                    result = "Logado com sucesso!";
                    username = u.getUsername();
                    request.setAttribute("username", username);
                    request.setAttribute("resp", result);
                    request.getRequestDispatcher("/feed/feed.jsp").forward(request, response);
                    break;
                }
            }
            
            request.setAttribute("resp", result);
            request.getRequestDispatcher("/feed/formulario.jsp").forward(request, response);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void redefinirSenha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            String email = request.getParameter("email_login");
            String novasenha = request.getParameter("novasenha");
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("PROJETO_POSTPU");
            UserJpaController userJpa = new UserJpaController(emf);
            RoleJpaController roleJpa = new RoleJpaController(emf);
            
            List<User> listUser = new ArrayList();
            listUser = userJpa.findUserEntities();
            
            User usr;
            String result= "Email inválido!";
            for(User u : listUser){
                if(userJpa.findUser(u.getIduser()).getEmail().equals(email)){
                    result = "Senha redefinida com sucesso!";
                    usr = new User(u);
                    usr.setPassword(novasenha);
                    userJpa.edit(usr);
                    request.setAttribute("resp", result);
                    request.getRequestDispatcher("/feed/formulario.jsp").forward(request, response);
                    break;
                } else {
                    continue;
                }
            }
            
            request.setAttribute("resp", result);
            request.getRequestDispatcher("/feed/redefinirsenha.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}