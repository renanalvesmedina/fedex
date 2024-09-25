<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirRelacaoCargaAerea" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="dadosGerais" id="pesq" src="/expedicao/emitirRelacaoCargaAerea" cmd="pesq"/>
		<adsm:tab title="awbs" id="awb" src="/expedicao/emitirRelacaoCargaAerea" cmd="awb"/>
	</adsm:tabGroup>
</adsm:window>
