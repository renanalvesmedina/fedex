<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaGrid();
}
</script>
<adsm:window title="horarioSaidaViagem" service="lms.coleta.rotasDestinosColetaAction" onPageLoad="carregaPagina" >

	<adsm:grid property="detalheColetas" idProperty="idDetalheColeta" selectionMode="none" gridHeight="120" 
			   showPagging="false" scrollBars="vertical" unique="true" title="horarioSaidaViagem" 
			   service="lms.coleta.rotasDestinosColetaAction.findPaginated"
			   rowCountService=""
			   onRowClick="detalheColetas_OnClick"
			   >
		<adsm:gridColumn title="destino" property="filial.sgFilial" width="11%" />
		<adsm:gridColumn title="rota" property="rota.dsRota" width="30%" />
		<adsm:gridColumn title="saida" property="trechoRotaIdaVolta.strHrSaida" width="10%" align="center" />
		<adsm:gridColumn title="dom" property="trechoRotaIdaVolta.blDomingo" width="7%" renderMode="image-check" />
		<adsm:gridColumn title="seg" property="trechoRotaIdaVolta.blSegunda" width="7%" renderMode="image-check" />
		<adsm:gridColumn title="ter" property="trechoRotaIdaVolta.blTerca" width="7%" renderMode="image-check" />
		<adsm:gridColumn title="qua" property="trechoRotaIdaVolta.blQuarta" width="7%" renderMode="image-check" />
		<adsm:gridColumn title="qui" property="trechoRotaIdaVolta.blQuinta" width="7%" renderMode="image-check" />
		<adsm:gridColumn title="sex" property="trechoRotaIdaVolta.blSexta" width="7%" renderMode="image-check" />
		<adsm:gridColumn title="sab" property="trechoRotaIdaVolta.blSabado" width="7%" renderMode="image-check" />
		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/coleta/programacaoColetas" idProperty="idDetalheColeta" >
		<adsm:hidden property="idPedidoColeta" />
	
	</adsm:form>

</adsm:window>

<script>

function povoaGrid() {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "pedidoColeta.idPedidoColeta", getElementValue('idPedidoColeta'));
      detalheColetasGridDef.executeSearch(filtro);
      return false;
}


function detalheColetas_OnClick(idDetalheColeta) {
	return false;
}
</script>