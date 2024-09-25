
var ManterReciboComplementarController = [	
	"$scope", 
	"RestAccessFactory",
	"TableFactory",	 
	function($scope, RestAccessFactory, TableFactory) {
		function initializeAbaComplementar() {
			$scope.rest = RestAccessFactory.create("/fretecarreteiroviagem/manterReciboComplementar");
			
			$scope.complementarTableParams = new TableFactory({
				service: $scope.rest.doPost, method: "find"
			});
			
			if ($scope.dados && $scope.dados.idReciboFreteCarreteiro) {				
				$scope.complementarTableParams.load({ reciboComplementado: { idReciboFreteCarreteiro: $scope.dados.idReciboFreteCarreteiro }});
			} else {
				$scope.complementarTableParams.clear();	
			}
		}
		
		initializeAbaComplementar();
	}
];
