<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTurnosAgendamento" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/entrega/manterTurnosAgendamento" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/entrega/manterTurnosAgendamento" cmd="cad" />
		</adsm:tabGroup>
</adsm:window>

