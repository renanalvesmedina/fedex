
(function(tabelaDePrecosModule){
	
	tabelaDePrecosModule.service("ReajusteTabelaDePrecoService", [
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
        			
        			var extraActions = [  
						{
							name: "findParcelas",
							method: "POST",
							url: "findParcelas"
						}
        			];
        			
        			resource = ResourceFactory.create("/tabeladeprecos/reajustetabeladepreco", extraActions, idProperty);
        			return resource;
        		}
        		
        	};
        }
	]);
	
})(tabelaDePrecosModule);

