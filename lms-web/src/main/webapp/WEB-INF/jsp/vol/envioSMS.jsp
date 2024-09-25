<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="envioSMS" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="envio" src="/vol/envioSMS" cmd="list"/>
		<adsm:tab title="historico" onShow="findDados" id="historico" src="/vol/envioSMS" cmd="historico" />
	</adsm:tabGroup>
</adsm:window>
