<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="45" idProperty="idDoctoServico">
		<adsm:hidden property="idNaoConformidade"/>
		<adsm:hidden property="sgFilialNaoConformidade"/>
		
		<adsm:textbox width="32%" labelWidth="18%" size="3" dataType="text" property="sgFilial" label="numeroRegistro" disabled="true" >
		<adsm:textbox size="20" dataType="integer" property="nrNaoConformidade" disabled="true" mask="00000000"/>
		</adsm:textbox>
		
		
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="tpStatusNaoConformidade" label="status" disabled="true" />
		<adsm:textbox width="32%" labelWidth="18%" size="22" dataType="text" property="dhEmissao" label="dataEmissao" disabled="true" />
		<adsm:textbox width="32%" labelWidth="18%" size="3" dataType="text" property="filResponsavel" label="filialResponsavel" disabled="true">
		<adsm:textbox dataType="text" property="nmFantasiaResp" size="30" disabled="true"/>
		</adsm:textbox>
		<adsm:buttonBar freeLayout="true">
				<adsm:button id="imprimirEspelhoRNC" caption="imprimirEspelhoRNC" disabled="false" onclick="reportButton()"/>
				<%--adsm:button caption="imprimirEspelhoRNC" action="/rnc/emitirRNCPesq" id="imprimirEspelhoRNC" cmd="main" disabled="false">
					<adsm:linkProperty src="idNaoConformidade" target="naoConformidade.idNaoConformidade"/>
					<adsm:linkProperty src="nrNaoConformidade" target="naoConformidade.nrNaoConformidade"/>
					<adsm:linkProperty src="sgFilialNaoConformidade" target="naoConformidade.filial.sgFilial"/>
				</adsm:button--%>
		</adsm:buttonBar>	
	</adsm:form>
	<adsm:grid property="ocorrenciaNaoConformidade" idProperty="idOcorrenciaNaoConformidade" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedOcorrenciaNaoConformidade" onRowClick="returnFalse" showPagging="false" selectionMode="none" showGotoBox="false" unique="false" scrollBars="vertical" minimumHeight="true" gridHeight="115" >
		<adsm:gridColumnGroup separatorType="RNC">	   
			<adsm:gridColumn title="numero" property="sgFilialNaoConform" width="50" />
			<adsm:gridColumn title="" property="nrOcorrenciaNc" width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
							
		<adsm:gridColumn width="130" title="motivoAbertura" property="dsMotivoAbertura" />
		<adsm:gridColumn width="80" title="volumes" property="qtVolumes" dataType="integer"/>
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="valorOcorrencia" property="sgMoeda" width="50" />
			<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="100" title="" property="vlOcorrenciaNc" dataType="currency"/>
		<adsm:gridColumn width="150" title="dataOcorrencia" property="dhInclusao" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="90" title="situacao" property="tpStatusOcorrenciaNc" isDomain="true"/>

		<adsm:gridColumn title="branco" property="endPes" image="/images/popup.gif" openPopup="true" link="javascript:executaNavegacao"  width="50" align="center" linkIdProperty="" />
	</adsm:grid>
	
	
	
	
</adsm:window>  
<script>

function reportButton() {	
		var data = new Array();
		setNestedBeanPropertyValue(data,"naoConformidade.idNaoConformidade",getElementValue('idNaoConformidade')) ;
		setNestedBeanPropertyValue(data,"naoConformidade.nrNaoConformidade",getElementValue('nrNaoConformidade')) ;
		setNestedBeanPropertyValue(data,"naoConformidade.filial.sgFilial",getElementValue('sgFilialNaoConformidade')) ;
		
		var sdo = createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.executeRNC", "openPdf", data);
    	executeReportWindowed(sdo, "pdf");
}


function returnFalse(){
	return false;
}

function findByIdDetalhamentoRNC(){
	
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
	
    _serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findByIdDetailAbaRNC", "myOnDataLoad", {idDoctoServico:idDoctoServico}));
  	xmit();
}

function myOnDataLoad_cb(data,exception){
	onDataLoad_cb(data,exception);
	var idNaoConformidade = getNestedBeanPropertyValue(data,"idNaoConformidade");
	if(idNaoConformidade != null){
		var dataPesq = new Array();
		setNestedBeanPropertyValue(dataPesq,"idNaoConformidade",idNaoConformidade);
		ocorrenciaNaoConformidadeGridDef.executeSearch(dataPesq);
	}	
}

function executaNavegacao(data){
        var idOcorrenciaNaoConformidade = ocorrenciaNaoConformidadeGridDef.getDataRowById(data).idOcorrenciaNaoConformidade;
        var parametros = '&idOcorrenciaNaoConformidadeLocMerc=' + idOcorrenciaNaoConformidade;
        parent.parent.parent.redirectPage(contextRoot + '/rnc/manterOcorrenciasNaoConformidade.do?cmd=main' + parametros);
}	

 
</script> 