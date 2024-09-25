<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterExcecoesRegraGeral" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroViagem/manterExcecoesRegraGeralTabelasFrete" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroViagem/manterExcecoesRegraGeralTabelasFrete" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
