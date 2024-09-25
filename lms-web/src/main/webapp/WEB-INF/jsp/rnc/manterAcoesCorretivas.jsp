<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAcoesCorretivas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/manterAcoesCorretivas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/manterAcoesCorretivas" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
