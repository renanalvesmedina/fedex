<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarRotasViagemAction" >
	<adsm:grid property="tipoMeioTranspRotaEvent" idProperty="idTipoMeioTranspRotaEvent" unique="true"
			service="lms.municipios.consultarRotasViagemAction.findMeioTranspEventual" onRowClick="onRowClickGrid();"
			selectionMode="none" scrollBars="vertical" showPagging="false" gridHeight="360" >
		<adsm:gridColumn title="tipoMeioTransporte" property="dsTipoMeioTransporte" />	
		<adsm:gridColumn title="valorPedagio" property="dsMoeda_01" width="60" />
		<adsm:gridColumn title="" property="vlPedagio" dataType="currency" width="80" />
		<adsm:gridColumn title="valorFrete" property="dsMoeda_02" width="60" />
		<adsm:gridColumn title="" property="vlFrete" dataType="currency" width="80" />
	</adsm:grid>
	<adsm:buttonBar freeLayout="false" />
</adsm:window>
<script>

	function onRowClickGrid() {
		return false;
	}

	var ultimoIdRota = -1;
	function detalhamentoAba() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("rtIda");
		
		var idRotaIdaVolta = tabDet.getFormProperty("idRotaIdaVolta");
		if (idRotaIdaVolta == ultimoIdRota)
			return false;
		ultimoIdRota = idRotaIdaVolta;

		carregaGridPrincipal(idRotaIdaVolta);
	}
	
	function carregaGridPrincipal(idRotaIdaVolta) {
		if (idRotaIdaVolta != undefined && idRotaIdaVolta != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idRotaIdaVolta", idRotaIdaVolta);
			tipoMeioTranspRotaEventGridDef.executeSearch(data);
		} else 
			tipoMeioTranspRotaEventGridDef.resetGrid();
	}
		
</script>
