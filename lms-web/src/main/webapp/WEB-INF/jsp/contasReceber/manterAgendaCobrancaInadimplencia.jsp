<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterAgendaCobrancaInadimplencia" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterAgendaCobrancaInadimplencia" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterAgendaCobrancaInadimplencia" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
