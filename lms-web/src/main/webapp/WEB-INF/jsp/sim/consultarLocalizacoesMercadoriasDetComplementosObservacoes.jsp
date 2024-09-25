<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form  action="/sim/consultarLocalizacoesMercadorias" height="210" idProperty="idDoctoServico">
			<adsm:grid onRowClick="returnFalse" idProperty="idObservacaoDoctoServico" property="observacaoDoctoServico" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedComplObservacoes" selectionMode="none" showPagging="false" showGotoBox="false" unique="true" gridHeight="100" >
				<adsm:gridColumn width="32%" title="observacoes" property="dsObservacaoDoctoServico"/>
			</adsm:grid>
	</adsm:form>
</adsm:window>   
<script>
	function returnFalse(){
		return false;
	}
	function findPaginatedComplObservacoes(){	
		observacaoDoctoServicoGridDef.resetGrid;
	 	var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
	 	var data = new Array();
		setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
		observacaoDoctoServicoGridDef.executeSearch(data);
	}
</script>