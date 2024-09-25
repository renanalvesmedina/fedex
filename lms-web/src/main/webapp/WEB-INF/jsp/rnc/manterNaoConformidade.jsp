<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterNaoConformidade" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/manterNaoConformidade" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/manterNaoConformidade" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
