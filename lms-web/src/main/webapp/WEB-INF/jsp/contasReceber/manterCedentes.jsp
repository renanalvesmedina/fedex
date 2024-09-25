<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterCedentes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterCedentes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterCedentes" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>