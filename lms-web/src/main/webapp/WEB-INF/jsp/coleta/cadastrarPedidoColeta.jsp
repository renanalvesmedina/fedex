<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="cadastrarPedidoColeta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="pedidoColetaTitulo" id="pedidoColeta" src="/coleta/cadastrarPedidoColeta" cmd="pedidoColeta"/>
		<adsm:tab title="detalheColeta" id="detalheColeta" disabled="true" src="/coleta/cadastrarPedidoColeta" cmd="detalheColeta" boxWidth="130" masterTabId="pedidoColeta" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>