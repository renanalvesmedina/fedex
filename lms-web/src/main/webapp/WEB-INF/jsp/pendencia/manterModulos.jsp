<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterModulos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/pendencia/manterModulos" cmd="pesq"/>
		<adsm:tab title="detalhamento" id="cad" src="/pendencia/manterModulos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
