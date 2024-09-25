<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:grid property="adiantamentos" idProperty="idReciboFreteCarreteiro" selectionMode="none" unique="true"
			service="lms.fretecarreteiroviagem.manterRecibosAction.findGridAdiantamentos"
			onRowClick="onRowClickGrid"
			showPagging="false" gridHeight="355" scrollBars="vertical" > 
			
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numeroRecibo" property="sgFilial" width="60" />
			<adsm:gridColumn title="" property="nrReciboFreteCarreteiro" dataType="integer" mask="0000000000" width="25" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="valorAdiantamento" property="dsMoeda_01" width="70" />
		<adsm:gridColumn title="" property="vlBruto" dataType="currency" width="80" />
		
		<adsm:gridColumn title="dataEmissao" property="dhEmissao" width="140" dataType="JTDateTimeZone" />
		<adsm:gridColumn title="situacao" property="tpSituacaoRecibo" />
		
		<adsm:gridColumn title="dataPagamentoSugerida" property="dtSugeridaPagto" width="140" dataType="JTDate" />
		<adsm:gridColumn title="dataPagamentoReal" property="dtPagtoReal" width="120" dataType="JTDate" />
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	function onRowClickGrid() {
		return false;
	}

	var ultimoId = -1;
	function detalhamentoAba() {
		var idCad = getCadProperty("idReciboFreteCarreteiro");
		if (idCad == ultimoId)
			return false;
		ultimoId = idCad;

		carregaGridPrincipal(idCad);
	}
	
	function carregaGridPrincipal(id) {
		if (id != undefined && id != ''){
			var data = new Array();
			
			var idProprietario = getCadProperty("proprietario.idProprietario");
			var idControleCarga = getCadProperty("controleCarga.idControleCarga");
			
			setNestedBeanPropertyValue(data, "idReciboFreteCarreteiro", id);
			setNestedBeanPropertyValue(data, "proprietario.idProprietario", idProprietario);
			setNestedBeanPropertyValue(data, "controleCarga.idControleCarga", idControleCarga);
			
			adiantamentosGridDef.executeSearch(data);
		} else
			adiantamentosGridDef.resetGrid();
	}
	
	function getCadProperty(name) {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		
		return tabDet.getFormProperty(name);
	}
//-->
</script>