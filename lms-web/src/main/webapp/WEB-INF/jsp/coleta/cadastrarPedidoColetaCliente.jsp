<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="cadastrarPedidoColetaCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="pedidoColetaTitulo" id="pedido" src="/coleta/cadastrarPedidoColetaCliente" cmd="pedido"/>
		<adsm:tab title="detalheColeta" id="detalhe" src="/coleta/cadastrarPedidoColetaCliente" cmd="detalhe" boxWidth="130"/>
		<adsm:tab title="restricoesColetaTitulo" id="restricoes" src="/coleta/cadastrarPedidoColetaCliente" cmd="restricoes" boxWidth="115"/>
	</adsm:tabGroup>

</adsm:window>
