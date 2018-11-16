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

        <title>Formulário</title>

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
        <div class="navbar navbar-expand-lg bg-dark fixed-top">
            <a href="#"> <h1 style="color: white; font-size: 35px; padding: 6px; font-weight: 400;">Projeto POST - Formulário</h1></a> 
        </div>

        <div class="container" style="padding: 15px; margin-top: 20px;">
            <div class="row">
                <div class="col-sm-8 border-right">
                    <h3>Cadastre-se:</h3>

                    <div class="form-group">
                        <form method="post" action="Servlet">
                            <label for="nome">Nome:</label>
                            <input type="text" name="nome" class="form-control" placeholder="Digite seu nome completo" required> <p></p>

                            <label for="email">E-mail:</label>
                            <input type="text" name="email" class="form-control" placeholder="Digite seu email" required> <p></p>

                            <label for="username">Nome de usuário:</label>
                            <input type="text" name="username" class="form-control" placeholder="Digite seu nome de usuário" required> <p></p>

                            <label for="senha">Senha:</label>
                            <input type="password" name="senha" class="form-control" placeholder="Digite sua senha" required> <p></p>

                            <label for="confirm_senha">Confirmar Senha:</label>
                            <input type="password" name="confirm_senha" class="form-control" placeholder="Digite sua senha" required> <p></p>

                            <button class="btn btn-secondary" name="btncadastrar" type="submit">Cadastrar</button>
                        </form>
                    </div>
                </div>   

                <div class="col-sm-4" >
                    <h3>Logar:</h3>
                    <div class="form-group">
                        <form action="Servlet" method="post">
                            <label for="email_login">E-mail:</label>
                            <input type="text" name="email_login" class="form-control" placeholder="Digite seu login" required> <p></p>

                            <label for="senha_login">Senha:</label> <a href="redefinirsenha.jsp" style="float: right;">Esqueceu sua senha?</a>
                            <input type="password" name="senha_login" class="form-control" placeholder="Digite sua senha" required> <p></p>

                            <button class="btn btn-primary" name="btnlogar" type="submit">Logar</button> 
                        </form>
                    </div>
                </div>   
            </div>
        </div>   	

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
