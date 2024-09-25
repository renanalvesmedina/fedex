<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAutoridadeLista" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"	   id="pesq" src="/seguranca/manterAutoridadeLista" cmd="list" />
		<adsm:tab title="detalhamento" id="cad"  src="/seguranca/manterAutoridadeLista" cmd="cad"  />
	</adsm:tabGroup>
</adsm:window>
