<%-- 
    Document   : redefinirsenha
    Created on : 08/11/2018, 10:28:25
    Author     : alissonfernando
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <title>Redefinir Senha</title>

        <!-- Bootstrap core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../estilo.css">

        <!-- Custom styles for this template -->
        <link href="css/blog-home.css" rel="stylesheet">
    </head>
    <body>
        <script> 
            let response = ${resp.length() > 0 ? String.format("'%s'",resp) : "''"};
            if(response.length > 0) {
                alert(response);
            }
        </script>

    <!-- Navigation -->
    <div class="navbar navbar-expand-lg bg-dark fixed-top">
        <a href="#"> <h1 style="color: white; font-size: 35px; padding: 6px; font-weight: 400;">Projeto POST - Redefinir Senha</h1></a> 
    </div>

    <div class="container" style="padding: 15px; margin-top: 100px;">
      <div class="row justify-content-center"> 
        <div class="col-md-6">
            <h3>Redefinir Senha:</h3>
            <div class="form-group">
                <form action="Servlet" method="post">
                    <label for="email_login">E-mail:</label>
                    <input type="text" name="email_login" class="form-control" placeholder="Digite seu email" required> <p></p>
                
                    <label for="senha_login">Nova Senha:</label>
                    <input type="password" name="novasenha" class="form-control" placeholder="Digite sua senha" required> <p></p>
                    
                    <button class="btn btn-primary" type="submit" name="btnalterarsenha">Alterar</button> 
                </form>
            </div>
        </div>   
      </div>
    </div>   	

    <!-- Footer -->
    <footer class="footer py-4 bg-dark fixed-bottom">
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
