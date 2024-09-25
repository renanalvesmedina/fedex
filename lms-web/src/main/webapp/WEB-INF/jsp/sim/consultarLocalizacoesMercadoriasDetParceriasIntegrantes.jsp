<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	
			<adsm:grid property="clienteCooperado" idProperty="idRegistro"  unique="true" gridHeight="180" scrollBars="vertical" showGotoBox="false" showPagging="false" selectionMode="none" onRowClick="returnFalse" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedIntegrantesAbaParcerias">
				<adsm:gridColumn width="170" title="tipo" property="tipoCliente"/>
				<adsm:gridColumn width="50" title="identificacao" property="tpIdentificacao" align="left" isDomain="true"/>
				<adsm:gridColumn width="150" title="" property="nrIdentificacao" align="right"/>
				<adsm:gridColumn width="200" title="nome" property="nmPessoa"/>
				<adsm:gridColumn width="150" title="municipio" property="municipio"/>
				<%--adsm:gridColumn title="branco" property="endPes" image="/images/popup.gif" openPopup="true" link="javascript:executaPopup"  width="50" align="center" linkIdProperty="" /--%>
			</adsm:grid>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="0" idProperty="idDoctoServico">		
	</adsm:form>
</adsm:window> 
<script>

function findPaginatedIntegrantesAbaParcerias(){
	clienteCooperadoGridDef.resetGrid;
	var idDoctoServico =  parent.parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
		
    var data = new Array();
	setNestedBeanPropertyValue(data, "idDoctoServico", idDoctoServico);
	
	clienteCooperadoGridDef.executeSearch(data);
	
}

function returnFalse(){
	return false;
}

/*
function executaPopup(data){
        var idCliente = data;
        var nrIdentificacao = clienteCooperadoGridDef.getDataRowById(data).nrIdentificacao;
        var nmPessoa = clienteCooperadoGridDef.getDataRowById(data).nmPessoa;
        var idCliente = clienteCooperadoGridDef.getDataRowById(data).idCliente;
        var tipoCliente = clienteCooperadoGridDef.getDataRowById(data).tipoCliente;
        var isTelaLocMerc = "true";
        showModalDialog('configuracoes/manterEnderecoPessoa.do?cmd=main&idCliente='+idCliente+'&nrIdentificacao='+nrIdentificacao+'&isTelaLocMerc='+isTelaLocMerc+'&nmPessoa='+nmPessoa+'&tipoCliente='+tipoCliente+'&flagAbaIntegrantes='+"true",window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');

 }*/

</script>  