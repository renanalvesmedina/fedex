
(function(tabelaDePrecosModule){
	
	tabelaDePrecosModule.service("ManterMarkupService", [
		"$resource", 
		"MarkupService", 
		function($resource, MarkupService) { 
       	 
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
        			
        			resource = MarkupService;
        			return resource; 
        		}
        		 
        	}; 
        }
	]);
	
})(tabelaDePrecosModule);
