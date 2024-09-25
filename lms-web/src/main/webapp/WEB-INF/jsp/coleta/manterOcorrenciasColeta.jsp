<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrenciasColeta" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/coleta/manterOcorrenciasColeta" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/coleta/manterOcorrenciasColeta" cmd="cad" />
		</adsm:tabGroup>
</adsm:window>