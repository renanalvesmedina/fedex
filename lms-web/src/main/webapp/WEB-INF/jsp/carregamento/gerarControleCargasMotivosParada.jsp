<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	onPageLoad();
}
</script>
<adsm:window title="motivoParadaTitulo" service="lms.carregamento.motivoParadaControleCargaAction" 
			 onPageLoad="carregaPagina" onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:grid property="motivos" idProperty="idMotivoParada" 
			   scrollBars="vertical" gridHeight="140"
			   selectionMode="none" showPagging="false" onDataLoadCallBack="retornoGrid"
			   onRowClick="motivos_OnClick" >
		<adsm:gridColumn title="motivoParada" property="dsMotivoParada" width="100%" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" />
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/carregamento/gerarControleCargas" >
		<adsm:hidden property="idPontoParadaTrecho" />
	</adsm:form>
</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	povoaGrid();
}

function povoaGrid() {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "idPontoParadaTrecho", getElementValue('idPontoParadaTrecho'));
      motivosGridDef.executeSearch(filtro);
      return false;
}

function motivos_OnClick(id) {
	return false;
}

function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	setFocus(document.getElementById("botaoFechar"), true, true);
}
</script>