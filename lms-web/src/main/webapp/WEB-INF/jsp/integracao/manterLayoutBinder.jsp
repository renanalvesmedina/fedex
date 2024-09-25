<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLayoutBinder" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem"      id="pesq" src="/integracao/manterLayoutBinder" cmd="list" height="440" />
			<adsm:tab title="detalhamento"  id="cad" src="/integracao/manterLayoutBinder"  cmd="cad"  height="440" />
		</adsm:tabGroup>
</adsm:window>