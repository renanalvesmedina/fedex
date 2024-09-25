<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTarifasPostoPassagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" onShow="desabledTabDf2" id="pesq" src="/municipios/manterTarifaPostoPassagem" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterTarifaPostoPassagem" cmd="cad" height="440" />
			<adsm:tab title="valoresTipoMeioTransporte" disabled="true" id="val" src="/municipios/manterTarifaPostoPassagem" copyMasterTabProperties="true" onShow="findData" masterTabId="cad" cmd="val" height="440"/>
		</adsm:tabGroup>
</adsm:window>