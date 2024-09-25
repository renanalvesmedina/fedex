<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterVersaoPontoLayoutBinder" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/integracao/manterVersaoPontoLayoutBinder" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/integracao/manterVersaoPontoLayoutBinder"  cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>