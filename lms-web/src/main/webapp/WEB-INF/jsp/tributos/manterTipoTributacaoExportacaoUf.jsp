<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTipoTributacaoExportacaoUf" type="main">

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterTipoTributacaoExportacaoUf" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterTipoTributacaoExportacaoUf" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>