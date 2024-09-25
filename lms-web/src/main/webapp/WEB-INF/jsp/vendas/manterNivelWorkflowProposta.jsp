<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterNivelWorkflowProposta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterNivelWorkflowProposta" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterNivelWorkflowProposta" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>