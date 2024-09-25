<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="consultarRotas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="list" src="/municipios/consultarRotas" cmd="list"/>
			<adsm:tab title="rotaIdaTitulo" id="rtIda" src="/municipios/consultarRotas" boxWidth="60" cmd="rtIda" disabled="true" />
			<adsm:tab title="rotaVoltaTitulo" id="rtVolta" src="/municipios/consultarRotas" boxWidth="65" cmd="rtVolta"
					onShow="detalhamentoAba" disabled="true" />
				
			<%--adsm:tab title="transportesRotaEventualTitulo" id="event" src="/municipios/consultarRotas"
					onShow="detalhamentoAba" boxWidth="170" cmd="event" disabled="true" /--%>
			<adsm:tab title="meioTransporteRotaTitulo" id="expr" src="/municipios/consultarRotas"
					onShow="detalhamentoAba" boxWidth="180" cmd="expr" disabled="true" />
			<adsm:tab title="motoristasRotaTitulo" id="mot" src="/municipios/consultarRotas"
					onShow="detalhamentoAba" boxWidth="130" cmd="mot" disabled="true" />
		</adsm:tabGroup>
</adsm:window>
 