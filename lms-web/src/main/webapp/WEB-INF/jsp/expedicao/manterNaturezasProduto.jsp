<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterNaturezasProduto" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterNaturezasProduto" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterNaturezasProduto" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
