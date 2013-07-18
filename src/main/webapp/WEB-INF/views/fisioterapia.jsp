<jsp:include page="cabecalho.jsp" />

      <div class="row roxo_claro">
        <h2 id="cabecalho">Fisioterapia</h2>
      </div>
      
      <div class="row roxo_claro">
        <div class="span5">
          <div id="myCarousel" class="carousel slide">
            <ol class="carousel-indicators">
              <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
              <li data-target="#myCarousel" data-slide-to="1"></li>
              <li data-target="#myCarousel" data-slide-to="2"></li>
            </ol>
            <div class="carousel-inner">
              <div class="item active">
                <img src="img/fisioterapia.jpg" class="img-rounded">
                <!-- <div class="carousel-caption">
                  <p>Fisioterapia</p>
                </div> -->
              </div>
              <div class="item">
                <img src="img/fisioterapia2.jpg" class="img-rounded">
                <!-- <div class="carousel-caption">
                  <p>Fisioterapia</p>
                </div> -->
              </div>
              <div class="item">
                <img src="img/fisioterapia3.jpg" class="img-rounded">
                <!-- <div class="carousel-caption">
                  <p>Fisioterapia</p>
                </div> -->
              </div>
              <div class="item">
                <img src="img/fisioterapia4.jpg" class="img-rounded">
                <!-- <div class="carousel-caption">
                  <p>Fisioterapia</p>
                </div> -->
              </div>
            </div>
            <a class="left carousel-control" href="#myCarousel" data-slide="prev">&lsaquo;</a>
            <a class="right carousel-control" href="#myCarousel" data-slide="next">&rsaquo;</a>
          </div>     
        </div>
        <div class="span6">
			<p>Segundo a resolu&ccedil;&atilde;o n&uacute;mero 80 do COFFITO &ldquo;a fisioterapia &eacute; uma ci&ecirc;ncia aplicada cujo objetivo de estudo &eacute; o movimento humano em todas as suas formas de express&atilde;o e potencialidades, quer nas suas altera&ccedil;&otilde;es patol&oacute;gicas, quer nas suas repercuss&otilde;es ps&iacute;quicas e org&acirc;nicas, com o objetivo de preservar, manter, desenvolver ou restaurar a integridade de &oacute;rg&atilde;os, sistema ou fun&ccedil;&atilde;o&rdquo;.</p>
			<p>Na reabilita&ccedil;&atilde;o atua nas especialidades de: neurologia, ortopedia, reumatologia, uroginecologia, oncologia, pr&eacute; e p&oacute;s-operat&oacute;rios em geral, cardiologia, pneumologia e pediatria.</p>
        </div>
      </div>

<jsp:include page="rodape.jsp" />

<script type="text/javascript">
   $('.carousel').carousel({
     interval: 5000
   });
   $("#fisioterapia").addClass("active");
 </script>