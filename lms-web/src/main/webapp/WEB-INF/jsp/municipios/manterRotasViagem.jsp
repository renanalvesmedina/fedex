<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterRotasViagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterRotasViagem" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterRotasViagem" cmd="cad"/>
			<%--adsm:tab title="rota" id="rota" src="/municipios/manterRotasViagem" cmd="rota" onShow="tabShowRotas"
					copyMasterTabProperties="true" masterTabId="cad" disabled="true" /--%>
			<adsm:tab title="rotaIdaTitulo" id="ida" src="/municipios/manterRotasViagem" cmd="ida" onShow="tabShowRotas"
					copyMasterTabProperties="true" masterTabId="cad" disabled="true" />
			<adsm:tab title="rotaVoltaTitulo" id="volta" src="/municipios/manterRotasViagem" cmd="volta" onShow="tabShowRotas"
					copyMasterTabProperties="true" masterTabId="cad" disabled="true" />
		</adsm:tabGroup>
</adsm:window>
