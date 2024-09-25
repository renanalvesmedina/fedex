<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarBaixaEntregasOnTime" type="main"> 
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="baixasPendentes" id="pesq" src="/entrega/registrarBaixaEntregasOnTime" cmd="proc"/>
		<!-- adsm:tab title="volumesManifesto" id="vol" src="/entrega/consultarAgendamentos" cmd="doc" disabled="true"/-->
	</adsm:tabGroup>
</adsm:window>
