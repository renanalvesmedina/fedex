<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
			<adsm:grid property="cliente" idProperty="idRegistro" selectionMode="none" onRowClick="returnFalse" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedIntegrantes" showGotoBox="false" unique="true" gridHeight="220" showPagging="false" minimumHeight="true" scrollBars="vertical">
				<adsm:gridColumn width="150" title="tipo" property="tipoCliente"/>
				<adsm:gridColumn width="50" title="identificacao" property="tpIdentificacao" align="left" isDomain="true"/>
				<adsm:gridColumn width="150" title="" property="nrIdentificacao" align="right"/>
				<adsm:gridColumn width="150" title="nome" property="nmPessoa"/>
				<adsm:gridColumn width="150" title="municipio" property="municipio"/>
				<adsm:gridColumn title="branco" property="endPes" image="/images/popup.gif" openPopup="true" link="javascript:executaPopup"  width="50" align="center" linkIdProperty="" />
			</adsm:grid>
			<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="1" idProperty="idDoctoServico" >
			</adsm:form>
</adsm:window> 
<script>

function findPaginatedIntegrantes(){
	clienteGridDef.resetGrid;
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
		
    var data = new Array();
	setNestedBeanPropertyValue(data, "idDoctoServico", idDoctoServico);
	
	clienteGridDef.executeSearch(data);
	
}

function returnFalse(){
	return false;
}

 function executaPopup(data){
        var idCliente = data;
        var nrIdentificacao = clienteGridDef.getDataRowById(data).nrIdentificacao;
        var nmPessoa = clienteGridDef.getDataRowById(data).nmPessoa;
        var idCliente = clienteGridDef.getDataRowById(data).idCliente;
        var tipoCliente = clienteGridDef.getDataRowById(data).tipoCliente;
        var isTelaLocMerc = "true";
        
        showModalDialog('sim/consultarLocalizacoesMercadorias.do?cmd=integrantesEnderecos&idCliente='+idCliente+'&isTelaLocMerc='+isTelaLocMerc+'&nrIdentificacao='+nrIdentificacao+'&nmPessoa='+nmPessoa+'&tipoCliente='+tipoCliente+'&flagAbaIntegrantes='+"true",window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');

 }





</script>  