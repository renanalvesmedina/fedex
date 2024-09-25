
var ManterProprietarioDescontoController = [	
	"$scope", 
	"RestAccessFactory",
	"TableFactory",	 
	function($scope, RestAccessFactory, TableFactory) {
		function initializeAbaDesconto() {
			$scope.rest = RestAccessFactory.create("/fretecarreteirocoletaentrega/descontoRfc");
			
			$scope.descontoTableParams = new TableFactory({
				service: $scope.rest.doPost, method: "find"
			});
			
			if ($scope.data && $scope.data.idProprietario) {
				var filter = {};
				filter.proprietario = {};
				filter.proprietario.idProprietario = $scope.data.idProprietario;
				
				$scope.descontoTableParams.load(filter);
			} else {
				$scope.descontoTableParams.clear();	
			}
		}
		
		initializeAbaDesconto();
	}
];
