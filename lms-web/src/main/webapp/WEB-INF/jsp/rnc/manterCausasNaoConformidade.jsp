<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCausasNaoConformidade" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/manterCausasNaoConformidade" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/manterCausasNaoConformidade" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
