(function(workflowModule){
	
	workflowModule.factory("WorkflowFactory", [
		"$http", 
		function($http) {
        	return {            		
        		findWorkflow: function(id) {
          			var url = contextPath+'rest/workflow/workflow/findWorkflow?id='+id;              			
        			return $http.get(url).then(function(response){
        				return response.data;
        			});
          		}
        	};
        }
	]);
	
})(workflowModule);
