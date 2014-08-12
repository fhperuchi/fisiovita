<jsp:include page="cabecalho.jsp" />

      <div class="row roxo_claro">
        <h2 id="cabecalho">Contato</h2>
      </div>
      
      <div class="row roxo_claro">
        <div class="span4">
			<img src="img/contato.jpg" class="img-rounded">
        </div>
        
        <div class="span6">
	  		
	  		<!-- area de mensagens -->
	  		<div class="alert" style="display: none;">
	  		</div>

        	<h4 class="roxo">Entre em contato por e-mail</h4>
			<form id="formContato" name="formContato" method="post" class="navbar-form pull-left" style="width: 700px">
			  <p>
			  <label>Interesse</label> 
			  <select id="interesse" name="interesse" class="span2">
			  	<option value="Fisioterapia;carinaballoni@terra.com.br,contato@spaziofisiovita.com.br">Fisioterapia</option>
			  	<option value="Est&eacute;tica;carinaballoni@terra.com.br,contato@spaziofisiovita.com.br">Est&eacute;tica</option>
			  	<option value="Pilates;fhperuchi@ciandt.com,fhperuchi@gmail.com,daysardenha@bol.com.br,contato@spaziofisiovita.com.br">Pilates</option>
			  	<option value="RPG;carinaballoni@terra.com.br,contato@spaziofisiovita.com.br">RPG</option>
			  	<option value="Nutricionista;rodolfonutri@hotmail.com,contato@spaziofisiovita.com.br">Nutricionista</option>
			  	<option value="Outro;daysardenha@gmail.com.br,carinaballoni@terra.com.br,renatanogueron@yahoo.com.br,fhperuchi@gmail.com,rodolfonutri@hotmail.com,contato@spaziofisiovita.com.br">Outro</option>
			  </select>
			  </p>
			  <p><input id="nome" name="nome" type="text" required class="span4" placeholder="Nome completo"></p>
			  <p><input id="email" name="email" type="email" required class="span4" placeholder="e-mail"></p>
			  <p><input id="telefone" name="telefone" type="text" class="span4" placeholder="Telefone ou celular"></p>
			  <p><textarea id="mensagem" name="mensagem" rows="5" cols="200" class="span5" placeholder="Mensagem"></textarea><p>
			  <p><button id="btnEnviar" type="button" class="btn">Enviar</button></p>
			</form>
			<br>
			<br>
			<h4 class="roxo">Entre em contato por telefone</h4>
			<p><strong>Cl&iacute;nica: </strong>(19) 3452-3530 ou (19) 3713-1680</p>
		  	<p><strong>Fisioterapia / Est&eacute;tica / RPG: </strong>Carina Balloni - (19) 99266-8434</p>
			<p><strong>Pilates: </strong>Dayane Sardenha - (19) 99751-9107</p>
		  	<!-- <p><strong>RPG: </strong>Renata Franco Ribeiro - (19) 99661-2738<p> -->
		  	<p><strong>Nutricionista: </strong>Rodolfo Scatolon - (19) 3039-1581<p>
        </div>
      </div>

<jsp:include page="rodape.jsp" />

<script type="text/javascript">
   $("#contato").addClass("active");
   
   $("#telefone").mask("(99) 9999-9999?9");
      
   $(".alert").click(function() {
      $(".alert").hide();
   });
   
   $("#btnEnviar").click(function() {
	    $(".alert").hide();
		$.post("/enviarEmail", $("#formContato").serialize()).done(
			function(data) {
				$(".alert").html(data);
				$(".alert").show();
			}
		).fail(
			function() { 
				alert("Ocorreu um erro!"); 
			}
		);	
	});
 </script>