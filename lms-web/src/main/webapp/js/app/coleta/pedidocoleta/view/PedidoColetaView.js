var PedidoColetaView =  { 
	name : "pedidoColeta", 
	title : "pedidoColeta", 
	controller : PedidoColetaController, 
	tabs : [ { 
		name : "list", 
		title : "listagem", 
		url : "/", 
		listTab: true 
	}, { 
		name : "remetente", 
		title : "pedidoColetaTitulo", 
		url : "/detalhe/:id", 
		editTab: true,
		controller : PedidoColetaCadController
	}] 
}; 
 
ColetaRotas.views.push(PedidoColetaView); 
