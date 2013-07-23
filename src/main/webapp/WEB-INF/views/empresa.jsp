<jsp:include page="cabecalho.jsp" />

      <div class="row roxo_claro">
        <h2 id="cabecalho">Empresa</h2>
      </div>
      
      <div class="row roxo_claro">
        <div class="span5">
          <div id="myCarousel" class="carousel slide">
            <div class="carousel-inner">
              <div class="item active">
                <img src="img/empresa.jpg" class="img-rounded">
              </div>
              <div class="item">
                <img src="img/empresa.jpg" class="img-rounded">
              </div>
              <div class="item">
                <img src="img/empresa.jpg" class="img-rounded">
              </div>
            </div>
            <a class="left carousel-control" href="#myCarousel" data-slide="prev">&lsaquo;</a>
            <a class="right carousel-control" href="#myCarousel" data-slide="next">&rsaquo;</a>
          </div>     
        </div>
        <div class="span6">
        	<p>Pensando no bem estar e na melhoria da qualidade de vida de nossos clientes oferecemos um atendimento global, cuidando da sa&uacute;de e da beleza utilizando para isso recursos fisioterap&ecirc;uticos e profissionais capacitados para oferecer qualidade e seguran&ccedil;a.</p>
			<p>Possu&iacute;mos salas de atendimentos individual e climatizadas, preparadas para atender com total conforto e individualidade.</p>
			<p>Para voc&ecirc; que quer cuidar do seu corpo, ficar linda e relaxar pagando muito pouco por momentos especiais e ainda receber elogios venha conhecer o que temos para lhe oferecer, fa&ccedil;a uma avalia&ccedil;&atilde;o sem custo.</p>
        </div>
      </div>

<jsp:include page="rodape.jsp" />

 <script type="text/javascript">
   $('.carousel').carousel({
     interval: 5000
   });
   $("#empresa").addClass("active");
 </script>