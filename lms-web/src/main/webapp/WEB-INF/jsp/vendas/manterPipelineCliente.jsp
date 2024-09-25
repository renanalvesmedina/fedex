<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPipelineCliente" type="main">
  	<adsm:tabGroup selectedTab="0">
  		<adsm:tab title="listagem" id="pesq" src="/vendas/manterPipelineCliente" cmd="list"/>
 	 	<adsm:tab title="detalhamento" id="cad" src="/vendas/manterPipelineCliente" cmd="cad"/>
		<adsm:tab title="etapas" id="etapas" src="/vendas/manterPipelineCliente" cmd="etapas" onShow="myOnShow" disabled="true"/>
		<adsm:tab title="propostasTabelas" id="propostasTabelas" src="/vendas/manterPipelineCliente" cmd="propostasTabelas" disabled="true" onShow="onShow"/>
	</adsm:tabGroup>
</adsm:window>