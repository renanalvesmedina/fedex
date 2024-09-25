<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTabelasReferenciasFreteKm" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="freteCarreteiroViagem/manterTabelasReferenciasFreteKm" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="freteCarreteiroViagem/manterTabelasReferenciasFreteKm" cmd="cad"/>
			<adsm:tab title="valores" id="cheq" src="freteCarreteiroViagem/manterTabelasReferenciasFreteKm" cmd="vlr" copyMasterTabProperties="true" onShow="findData" masterTabId="cad" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>
