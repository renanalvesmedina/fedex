<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLocalizacoesMercadoria" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/sim/manterLocalizacoesMercadoria" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/sim/manterLocalizacoesMercadoria" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
