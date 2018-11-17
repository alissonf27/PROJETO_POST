<%-- 
    Document   : feed
    Created on : 29/10/2018, 17:29:22
    Author     : alissonfernando
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <title>Feed</title>

        <!-- Bootstrap core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../estilo.css">

        <!-- Custom styles for this template -->
        <link href="css/blog-home.css" rel="stylesheet">
    </head>
    <body>
        <script>
            let response = ${resp.length() > 0 ? String.format("'%s'",resp) : "''"};
            if (response.length > 0) {
                alert(response);
            }
        </script>

        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
            <div class="container">
                <a class="navbar-brand" style="font-size: 35px;" href="#">Projeto POST - Feed</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="dropdown">
                    <img class="img-user" src="default-user.png">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        ${username}
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a class="dropdown-item" href="#">Perfil</a>
                        <a class="dropdown-item" href="#">Posts</a>
                        <a class="dropdown-item" href="formulario.jsp">Sair</a>
                    </div>
                </div>
            </div>
        </nav>

        <!-- Page Content -->
        <div class="container">

            <div class="row">

                <!-- Blog Entries Column -->
                <div class="col-md-8 border-right" style="padding-top: 10px;"><br>
                    
                    <!-- Blog Post -->
                    <%@ page import="java.util.List, java.util.Collections, java.util.ArrayList" %>
                    <%@ page import="Controller.PostJpaController" %>
                    <%@ page import="Entidades.Post, Entidades.Category, Entidades.User" %>
                    <%@ page import="javax.persistence.EntityManagerFactory, javax.persistence.Persistence" %>
                    <% 
                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PROJETO_POSTPU");
                        PostJpaController postJpa = new PostJpaController(emf);
                        List<Post> listPost = new ArrayList<>();
                        listPost = postJpa.findPostEntities();
                        Collections.reverse(listPost);
                    %>
                    
                    <%for(Post p : listPost){ %>
                        
                        <div class="card mb-4">
                        <div class="card-header">
                            <a href="#"><%= p.getIdAuthor().getUsername() %></a>
                        </div>
                        <div class="card-body">
                            <blockquote class="blockquote mb-0">
                                <p><%= p.getContent() %></p>
                                <footer class="blockquote-footer"><%= p.getIdCategory().getDescription() %></footer>
                            </blockquote>
                        </div>
                        </div>
                    <%} %>
                </div>

                <!-- Sidebar Widgets Column -->
                <div class="col-md-4" style="padding-top: 10px;">

                    <!-- Search Widget -->
                    <div class="card border-success my-4">
                        <h5 class="card-header border-success">Pesquisar</h5>
                        <div class="card-body">
                            <div class="input-group">
                                <input type="text" class="form-control" placeholder="Pesquisar por..."></input>
                                <button class="btn btn-secondary" type="button"> Q
                                    <span class="glyphicon glyphicon-search"></span>
                                </button>
                                <span class="glyphicon glyphicon-search"></span>
                            </div>
                        </div>
                    </div>

                    <!-- Categories Widget -->
                    <div class="card border-info my-4">
                        <h5 class="card-header border-info">Menu de Navegação</h5>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-lg-6">
                                    <ul class="list-unstyled mb-0">
                                        <li>
                                            <a href="#">Web Design</a>
                                        </li>
                                        <li>
                                            <a href="#">HTML</a>
                                        </li>
                                        <li>
                                            <a href="#">Freebies</a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="col-lg-6">
                                    <ul class="list-unstyled mb-0">
                                        <li>
                                            <a href="#">JavaScript</a>
                                        </li>
                                        <li>
                                            <a href="#">CSS</a>
                                        </li>
                                        <li>
                                            <a href="#">Tutorials</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <button type="button" class="btn-addPost" data-toggle="modal" data-target="#janela">
                    <a href="#"><img src="plus-circle-solid.svg"></a>
                </button>

                <form class="modal fade " id="janela" action="Servlet" method="post">
                    <div class="modal-dialog modal-md">
                        <div class="modal-content">

                            <div class="modal-header">
                                <h3 class="modal-title">Criar um post</h3>
                                <button type="button" class="close" data-dismiss="modal">
                                    <span>&times;</span>
                                </button>
                            </div>

                            <div class="modal-body">
                                <form> 
                                    <div class="form-group">
                                        <label for="content_post">Conteúdo:</label>
                                        <textarea type="text" name="content_post" class="form-control" placeholder="Conteúdo do post..."></textarea>
                                    </div>

                                    <div class="form-group">
                                        <label for="data_criacao">Data de criação:</label>
                                        <input type="datetime-local" name="data_criacao" class="form-control" placeholder="Data de criação">
                                    </div>

                                    <div class="form-group">
                                        <label for="data_publicacao">Data de publicação:</label>
                                        <input type="datetime-local" name="data_publicacao" class="form-control" placeholder="Data de criação">
                                    </div>

                                    <div class="form-group">
                                        <label for="categoria">Categoria:</label>
                                        <select name="categoria" class="form-control places"><option value="">Selecione...</option><option value="Java">Java</option><option value="C#">C#</option><option value="Python">Python</option></select>
                                    </div>
                                </form>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">
                                    Cancelar
                                </button>

                                <button type="submit" class="btn btn-primary" name="addPost">
                                    Postar
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

        </div>
        <!-- /.row -->

    </div>
    <!-- /.container -->

    <!-- Footer -->
    <footer class="py-4 bg-dark">
        <div class="container">
            <p class="m-0 text-center text-white">Copyright &copy; A. Fernando 2018</p>
        </div>
        <!-- /.container -->
    </footer>

    <!-- Bootstrap core JavaScript -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

</body>

</html>
