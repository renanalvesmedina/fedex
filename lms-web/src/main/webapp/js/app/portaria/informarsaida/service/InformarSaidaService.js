
(function(portariaModule){
	
	portariaModule.service("InformarSaidaService", [
		"$resource", 
		"ResourceFactory", 
		function($resource, ResourceFactory) {
        	
        	var resource;
        	var idProperty = "id";
        	
        	return {
        		
        		getIdProperty: function() {
        			return idProperty;
        		},
        		
        		getResource: function() {
        			if (resource) {
        				return resource;
        			}
        			var extraActions = [{
            				name: "saida",
            				method: "POST",
            				url: "viagem"
        				},
        				{
	            			name: "saidaColetaEntrega",
            				method: "POST",
            				url: "coletaEntrega"
        				}
        				];
        			resource = ResourceFactory.create("/portaria/informarsaida/saidas", extraActions, idProperty);
        			return resource;
        		}
        	};
        }
	]);
	
})(portariaModule);
