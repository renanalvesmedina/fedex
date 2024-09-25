<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="208" idProperty="idDoctoServico">
	
        <adsm:section caption="complementoDocumentoServico"/>
			<adsm:grid onRowClick="returnFalse" property="dadosComplemento" idProperty="idDadosComplemento" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedDadosCompl" showGotoBox="false" showPagging="false" selectionMode="none" unique="false" gridHeight="100">
				<adsm:gridColumn width="34%" title="descricao" property="descricao"/>
				<adsm:gridColumn width="33%" title="informacao" property="informacao"/>
				<adsm:gridColumn width="33%" title="conteudo" property="conteudo"/>
			</adsm:grid>
        <adsm:section caption="complementoNotasFiscais"/>
			<adsm:grid onRowClick="returnFalse" property="notaFiscalConhecimento" idProperty="idNotaFiscalConhecimento" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedComplNF" selectionMode="none" showPagging="false" showGotoBox="false" unique="false" gridHeight="100" >
				<adsm:gridColumn width="25%" title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="00000000"/>
				<adsm:gridColumn width="25%" title="descricao" property="descricao"/>
				<adsm:gridColumn width="25%" title="informacao" property="informacao"/>
				<adsm:gridColumn width="25%" title="conteudo" property="conteudo"/>
			</adsm:grid>
	</adsm:form>
</adsm:window> 
<script>
function returnFalse(){
	return false;
}

function findPaginatedComplDados(){
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	var data = new Array();
	setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
	dadosComplementoGridDef.executeSearch(data);
	notaFiscalConhecimentoGridDef.executeSearch(data);
}
</script>  