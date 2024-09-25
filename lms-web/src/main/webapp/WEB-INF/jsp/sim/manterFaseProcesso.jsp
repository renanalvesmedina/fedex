<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFaseProcesso" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/sim/manterFaseProcesso" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/sim/manterFaseProcesso" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
