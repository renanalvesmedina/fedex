<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterComites" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/workflow/manterComites" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/workflow/manterComites" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
