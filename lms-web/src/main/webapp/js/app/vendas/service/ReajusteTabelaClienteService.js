(function(vendasModule){
	
	vendasModule.service("ReajusteTabelaClienteService", [
		"ResourceFactory",
		function(ResourceFactory) {
			var resource;
			var idProperty = "idReajusteTabelaCliente";
			
			return {
				getIdProperty: function() {
					return idProperty;
				},
				getResource: function() {
					if (resource) {
						return resource;
					}
					var extraActions = [{
						name: "findTeste",
						method: "POST",
						url: "findTeste"
					}];
					resource = ResourceFactory.create("vendas/reajustetabelacliente", extraActions, idProperty);
					
					return resource;
				}
			};
		}
	]);
	
})(vendasModule);
