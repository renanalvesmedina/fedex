<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window title="manterMotivosCancelamentoProrrogacaoVencimento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterMotivosCancelamentoProrrogacaoVencimento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterMotivosCancelamentoProrrogacaoVencimento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
