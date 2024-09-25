<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarRotasViagemAction" >
	<adsm:grid property="meioTransporteRotaViagem" idProperty="idMeioTransporteRotaViagem"
			service="lms.municipios.consultarRotasViagemAction.findMeioTranspExpressa" onRowClick="onRowClickGrid();"
			selectionMode="none" scrollBars="both" showPagging="false" unique="true" gridHeight="355" >

		<adsm:gridColumn width="60" title="identificacao" property="tpIdentificacao" align="left" isDomain="true" />
		<adsm:gridColumn width="100" title="" property="nrIdentificacaoFormatado" dataType="text" align="right" />
		
		<adsm:gridColumn title="proprietario" property="nmProprietario" align="left" width="150" />	 	
		<adsm:gridColumn title="meioTransporte" property="meioTransporte_nrFrota" width="75" />
		<adsm:gridColumn title="" property="meioTransporte_nrIdentificador" align="left" width="75" />		
		<adsm:gridColumn title="tipoMeioTransporte" property="dsTipoMeioTransporte" width="180" />
		<adsm:gridColumn title="marca" property="dsMarcaMeioTransporte" width="120" />
		<adsm:gridColumn title="modelo" property="dsModeloMeioTransporte" width="120" />
		<adsm:gridColumn title="ano" property="meioTransporte_nrAnoFabricao" dataType="integer" width="50" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="100" />
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
		
		var idRotaViagem = tabDet.getFormProperty("idRotaViagem");
		if (idRotaViagem == ultimoIdRota)
			return false;
		ultimoIdRota = idRotaViagem;

		carregaGridPrincipal(idRotaViagem);
	}
	
	function carregaGridPrincipal(idRotaViagem) {
		if (idRotaViagem != undefined && idRotaViagem != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idRotaViagem", idRotaViagem);
			meioTransporteRotaViagemGridDef.executeSearch(data);
		} else 
			meioTransporteRotaViagemGridDef.resetGrid();
	}
		
</script>
