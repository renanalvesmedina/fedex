<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarBaixaEntregas" type="main"> 
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="baixasPendentes" id="pesq" src="/entrega/registrarBaixaEntregas" cmd="proc"/>
		<adsm:tab title="volumes" id="vol" src="/entrega/registrarBaixaEntregas" cmd="vol" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
