<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	
	<adsm:form action="/entrega/consultarPosicaoReembolsos" idProperty="idDoctoServico" >
		<adsm:hidden property="nrDoctoServico"/>
		<adsm:hidden property="tpDoctoServico"/>
		<adsm:hidden property="sgFilialDoctoServico"/>
		<adsm:hidden property="idDoctoServicoPrinc"/>
		
		<adsm:hidden property="idDoctoServicoReemb"/>
		<adsm:hidden property="idFilialReemb"/>
		<adsm:hidden property="nmFantasiaFilReemb"/>
		
		<adsm:textbox disabled="true" property="sgFilialReemb" label="reciboReembolso" labelWidth="15%" width="20%" size="6" dataType="text">
		<adsm:textbox disabled="true" property="nrReciboReembolso" dataType="integer" size="8" mask="00000000"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="dhEmissao" label="dataEmissao" size="22" maxLength="50" disabled="true" labelWidth="15%" width="20%" />
		
		<adsm:textbox dataType="text" property="vlReembolso" label="valorReembolso" size="18" maxLength="50" disabled="true" labelWidth="15%" width="15%" />
	    
		<adsm:textbox disabled="true" property="nrIdentificacaoRemFormatado" label="remetente" labelWidth="15%" width="35%" size="18" dataType="text">
		<adsm:textbox disabled="true" property="nmPessoaRem" dataType="text" size="25" />
		</adsm:textbox>
		
		<adsm:textbox disabled="true" property="nrIdentificacaoDestFormatado" label="destinatario" labelWidth="15%" width="35%" size="18" dataType="text">
		<adsm:textbox disabled="true" property="nmPessoaDest" dataType="text" size="25" />
		</adsm:textbox>
		
		<adsm:buttonBar freeLayout="true" >
			<adsm:button caption="informacoesReembolso" action="/entrega/consultarPosicaoReembolsos" cmd="main" id="informacoesReembolso">
				<adsm:linkProperty src="idDoctoServicoReemb" target="reciboReembolso.idDoctoServico"/>
				<adsm:linkProperty src="sgFilialReemb" target="reciboReembolso.filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="nrReciboReembolso" target="reciboReembolso.nrReciboReembolso"/>
				<adsm:linkProperty src="idDoctoServicoPrinc" target="doctoServico.idDoctoServico"/>
				<adsm:linkProperty src="tpDoctoServico" target="doctoServico.tpDocumentoServico"/>
				<adsm:linkProperty src="sgFilialDoctoServico" target="doctoServico.filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="nrDoctoServico" target="doctoServico.nrDoctoServico"/>
				<adsm:linkProperty src="sgFilialReemb" target="filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="idFilialReemb" target="filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="nmFantasiaFilReemb" target="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			</adsm:button>	
		</adsm:buttonBar>
	</adsm:form>
	
	
	<adsm:grid title="cheques" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedChequesByIdReembolso" onRowClick="returnFalse" property="chequeReembolso" idProperty="idChequeReembolso" selectionMode="none" showPagging="false" scrollBars="vertical" gridHeight="70" minimumHeight="true" showGotoBox="false"  unique="false" >
			<adsm:gridColumn width="120" title="banco" property="nrBanco" dataType="integer"/>
			<adsm:gridColumn width="120" title="agencia" property="nrAgencia" align="right" dataType="integer"/>
			<adsm:gridColumn width="120" title="cheque" property="nrCheque" align="right" dataType="integer"/>
			<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valor" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn width="150" title="" property="vlCheque" align="right" dataType="currency"/>
			<adsm:gridColumn width="150" title="data" property="dtCheque" dataType="JTDate" />
	</adsm:grid>
		
</adsm:window>		

<script>
document.getElementById('informacoesReembolso').onclick=function() {
	parent.parent.parent.parent.redirectPage(contextRoot + '/entrega/consultarPosicaoReembolsos.do?cmd=main' + buildLinkPropertiesQueryString_informacoesReembolso())
};

function returnFalse(){
	return false;
}

function findReembolso(){
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	var nrDoctoServico =  parent.parent.document.getElementById("nrDoctoServico").value;
	var tpDoctoServico =  parent.parent.document.getElementById("tpDocumentoServicoLinkProperty").value;
	var sgFilialDoctoServico =  parent.parent.document.getElementById("dsSgFilial").value;
	setElementValue("nrDoctoServico",nrDoctoServico);
	setElementValue("tpDoctoServico", tpDoctoServico);
	setElementValue("sgFilialDoctoServico",sgFilialDoctoServico);
	setElementValue("idDoctoServicoPrinc",idDoctoServico);
	
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findReembolsoByIdReembolsado", "myOnDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
  	
  	
}

function  myOnDataLoad_cb(data,exception){	
	onDataLoad_cb(data,exception);	
   	var idDoctoServicoReemb = getNestedBeanPropertyValue(data,"idDoctoServicoReemb");
   	var dataR = new Array();
	setNestedBeanPropertyValue(dataR,"idDoctoServico",idDoctoServicoReemb);
	chequeReembolsoGridDef.executeSearch(dataR);
  	
}

function returnFalse(){
	return false;
}
</script>
