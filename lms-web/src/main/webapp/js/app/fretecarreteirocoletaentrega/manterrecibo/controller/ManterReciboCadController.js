
var ManterReciboCadController = [
"$scope", 
"$stateParams", 
function($scope, $stateParams) {

	function initializeAbaCad(params){
		$scope.anexosTableParams.clear();
		$scope.dados.btSalvar = true;
		$scope.dados.btCancelar = true;
		
		if (params.id) {
			if(params.id.indexOf('&idProcessoWorkflow=') > -1){
				  var idProcesso = params.id;						
				  var id = idProcesso.substring(idProcesso.indexOf('=') + 1, idProcesso.length);
					
				  $scope.rest.doGet('findByIdProcesso?id=' + id).then(
							function(data) {	   
							$scope.processFind(data);
							}
						);
			} else {
				$scope.rest.doGet('findById?id=' + params.id).then(function(data) {	    	
					$scope.processFind(data);
    			});
			}
		} else {
			$scope.dados = null;
		}
	}
	
		initializeAbaCad($stateParams);
	}
];

