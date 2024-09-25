<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMotoristas" type="main" onPageLoad="setAbas">
	   
	      <adsm:i18nLabels>
                <adsm:include key="liberar"/>
                <adsm:include key="bloquear"/>
                <adsm:include key="bloqueado"/>
                <adsm:include key="liberado"/>
   			</adsm:i18nLabels>
   			
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterMotoristas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterMotoristas" cmd="cad"/>
			<adsm:tab title="anexo" masterTabId="cad" disabled="true" id="anexo" src="/contratacaoVeiculos/manterMotoristas" cmd="anexo" autoLoad="false"/>
		</adsm:tabGroup>
</adsm:window>

<script>

function setAbas(){
	 var url = new URL(document.location.href);
	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
	if ( idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){		
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.setDisabledTab("cad", false);
		tabGroup.selectTab('cad',null,true);
		//tabGroup.getTab("cad").properties.idMotorista= url.parameters['idProcessoWorkflow'];
	}else{
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');		
	} 
}
</script>