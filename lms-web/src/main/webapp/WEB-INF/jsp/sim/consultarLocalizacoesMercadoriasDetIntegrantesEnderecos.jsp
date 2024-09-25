<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction" onPageLoadCallBack="realizaPaginacao">
			<adsm:grid property="enderecoPessoa" onRowClick="returnFalse();"  service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedEnderecoIntegrante" idProperty="idEnderecoPessoa" selectionMode="none" showGotoBox="false" unique="true" gridHeight="400" showPagging="false" minimumHeight="true" scrollBars="vertical">
				<adsm:gridColumn width="250" title="endereco" property="enderecoCompleto"/>
				<adsm:gridColumn width="150" title="tipoEndereco" property="tpEndereco" align="left" isDomain="true"/>
				<adsm:gridColumn width="100" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
				<adsm:gridColumn width="100" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>
				<adsm:gridColumn title="branco" property="endPes" image="/images/fone.gif" openPopup="true" link="javascript:executaPopup"  width="50" align="center" linkIdProperty="" />
			</adsm:grid>
			
			<adsm:buttonBar freeLayout="false" >
				<adsm:button caption="fechar" onclick="self.close()" disabled="false"/>		
			</adsm:buttonBar>
			
			<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="1" idProperty="idDoctoServico" >
			  <adsm:hidden property="idCliente" />
			  <adsm:hidden property="pessoa.nrIdentificacao" />
			  <adsm:hidden property="pessoa.nmPessoa" />
			  <adsm:hidden property="labelPessoaTemp" />
			</adsm:form>
			
</adsm:window>
 
<script>
function returnFalse(){
	return false;
}

function realizaPaginacao_cb(){
	onPageLoad_cb();
	
	var u = new URL(parent.location.href);
	
	var idCliente = u.parameters["idCliente"];
	var nrIdentificacao  = u.parameters["nrIdentificacao"];
   	var nmPessoa = u.parameters["nmPessoa"];
   	var tipoCliente = u.parameters["tipoCliente"];
	
	if(idCliente!= null){
		setElementValue("idCliente",idCliente);
		document.getElementById("idCliente").masterLink="true";
	}   	
   	if(nrIdentificacao!= null){
		setElementValue("pessoa.nrIdentificacao",nrIdentificacao);
		document.getElementById("pessoa.nrIdentificacao").masterLink="true";
	}
		
    if(nmPessoa!= null){
    	setElementValue("pessoa.nmPessoa",nmPessoa);
    	document.getElementById("pessoa.nmPessoa").masterLink="true";
    }
    	
    if(tipoCliente != null)	
		setElementValue("labelPessoaTemp",tipoCliente);
   	
	var dataId = new Array();
	setNestedBeanPropertyValue(dataId,"idIntegrante",idCliente);
	enderecoPessoaGridDef.executeSearch(dataId);
	
}	

function executaPopup(data){
		var idEnderecoPessoa = data;
        var idEnderecoPessoa = enderecoPessoaGridDef.getDataRowById(data).idEnderecoPessoa;
        var isTelaLocMerc = "true";
        var nrIdentificacao  = getElementValue("pessoa.nrIdentificacao");
   	    var nmPessoa = getElementValue("pessoa.nmPessoa");
   	    var tipoCliente = getElementValue("labelPessoaTemp");
   	    var idCliente = getElementValue("idCliente");
        showModalDialog('configuracoes/manterTelefonesPessoa.do?cmd=list&idEnderecoPessoa='+idEnderecoPessoa+'&isTelaLocMerc='+isTelaLocMerc+'&flagAbaIntegrantes='+"true"+'&nrIdentificacao='+nrIdentificacao+'&nmPessoa='+nmPessoa+'&tipoCliente='+tipoCliente+'&idCliente='+idCliente+'&mode=modal',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');

 }

</script>
