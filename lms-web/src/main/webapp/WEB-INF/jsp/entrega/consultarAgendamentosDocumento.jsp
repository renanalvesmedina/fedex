<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterAgendamentosAction" >
	<adsm:grid property="documentosServico" idProperty="idDoctoServico"
			service="lms.entrega.consultarAgendamentosAction.findDocumentosServico" onRowClick="onRowClickGrid();"
			selectionMode="none" scrollBars="both" showPagging="false" unique="true" gridHeight="355" >

		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialOrigem" width="70" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="30" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
			
 	 	<adsm:gridColumn title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="000000" width="70" />

		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn title="controleCargas" property="filial_origem_coca_sgfilial" width="70" />
			<adsm:gridColumn title="" property="nrControleCarga" dataType="integer" mask="00000000" width="70" />
		</adsm:gridColumnGroup>	
 
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="manifestoEntrega" property="filial_maen_sgfilial" width="80" />
			<adsm:gridColumn title="" property="nrManifestoEntrega" dataType="integer" mask="00000000" width="70" />
		</adsm:gridColumnGroup>	
		
		<adsm:gridColumn title="dataEntrega" property="dhOcorrencia" dataType="JTDateTimeZone" width="120" />
	</adsm:grid>
	<adsm:buttonBar freeLayout="false" />
</adsm:window>
<script> 
 
	function onRowClickGrid() {
		return false;
	}
	
	function initWindow(eventObj) {
		carregaGridPrincipal();
	}
	
	function carregaGridPrincipal() {
	
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("det");
		
		var idAgendamentoEntrega = tabDet.getFormProperty("idAgendamentoEntrega");

		if (idAgendamentoEntrega != undefined && idAgendamentoEntrega != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idAgendamentoEntrega", idAgendamentoEntrega);
			documentosServicoGridDef.executeSearch(data);
		} else 
			documentosServicoGridDef.resetGrid();
	}
</script>
