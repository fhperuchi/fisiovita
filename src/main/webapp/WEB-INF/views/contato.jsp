<jsp:include page="cabecalho.jsp" />

      <div class="row roxo_claro">
        <h2 id="cabecalho">Contato</h2>
      </div>
      
      <div class="row roxo_claro">
        <div class="span4">
			<img src="img/contato.jpg" class="img-rounded">
        </div>
        <div class="span6">
        	<address>
			  <h4 class="roxo">Endere&ccedil;o</h4>
			  Rua Jo&atilde;o Jacon, 168 - Jardim Santa Cec&iacute;lia - 13480-685<br>
			  Limeira / S&atilde;o Paulo
			</address>
<!-- 			<hr> -->
        	<h4 class="roxo">Entre em contato por e-mail</h4>
			<form id="formContato" name="formContato" class="navbar-form pull-left" style="width: 700px">
			  <p>
			  <label>Interesse</label> 
			  <select id="interesse" name="interesse" class="span2">
			  	<option value="Fisioterapia;carinaballoni@terra.com.br">Fisioterapia</option>
			  	<option value="Pilates;daysardenha@bol.com.br,fhperuchi@ciandt.com">Pilates</option>
			  	<option value="Est&eacute;tica;carinaballoni@terra.com.br">Est&eacute;tica</option>
			  	<option value="RPG;renatanogueron@yahoo.com.br">RPG</option>
			  	<option value="Outro;daysardenha@bol.com.br,carinaballoni@terra.com.br,renatanogueron@yahoo.com.br">Outro</option>
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
		  	<p><strong>Fisioterapia / Est&eacute;tica: </strong>Carina Balloni - (19) 9377-0493</p>
			<p><strong>Pilates: </strong>Dayane Sardenha - (19) 9751-9107</p>
		  	<p><strong>RPG: </strong>Renata Franco Ribeiro - (19) 9661-2738<p>
        </div>
      </div>

<jsp:include page="rodape.jsp" />

<script type="text/javascript">
   
   $("#contato").addClass("active");
   
   $("#btnEnviar").click(function() {
		$.post("/enviarEmail", $("#formContato").serialize()).done(
			function(data) {
				alert("O e-mail foi enviado com sucesso! Em breve entraremos em contato.");
			}
		).fail(
			function() { 
				alert("Ocorreu um erro!"); 
			}
		);	
	});
 </script>