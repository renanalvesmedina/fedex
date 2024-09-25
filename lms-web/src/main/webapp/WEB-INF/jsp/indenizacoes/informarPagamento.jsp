<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="informarPagamento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="pagamento" id="pagamento" src="/indenizacoes/informarPagamento" cmd="list"/>
		<adsm:tab title="pagamentoLote" id="pagamentoLote" src="/indenizacoes/informarPagamento" cmd="pagamentoLote"/>
	</adsm:tabGroup>
</adsm:window>