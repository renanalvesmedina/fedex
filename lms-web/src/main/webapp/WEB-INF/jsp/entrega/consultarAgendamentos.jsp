<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarAgendamentos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/entrega/consultarAgendamentos" cmd="list" onShow="tabShow"/>
			<adsm:tab title="detalhamento" id="det" src="/entrega/consultarAgendamentos" cmd="det" disabled="true" />
			<adsm:tab title="documentos" id="doc" src="/entrega/consultarAgendamentos" cmd="doc" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>
