<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterReajustesEspecificos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroViagem/manterReajustesEspecificosTabelasFrete" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroViagem/manterReajustesEspecificosTabelasFrete" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
