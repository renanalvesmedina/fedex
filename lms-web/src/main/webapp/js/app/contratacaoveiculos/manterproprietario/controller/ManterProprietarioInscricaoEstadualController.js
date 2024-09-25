
var ManterProprietarioInscricaoEstadualController = [	
	"$scope", 
	"RestAccessFactory",
	"TableFactory",	 
	function($scope, RestAccessFactory, TableFactory) {
		
		function initializeAbaInscricaoEstadual() {
			$scope.rest = RestAccessFactory.create("/configuracoes/manterInscricaoEstadual");
			
			$scope.inscricaoEstadualTableParams = new TableFactory({
				service: $scope.rest.doPost, method: "find"
			});
			
			if ($scope.data && $scope.data.idPessoa) {
				var filter = {};
				filter.idPessoa = $scope.data.idPessoa;
				
				$scope.inscricaoEstadualTableParams.load(filter);
			} else {
				$scope.inscricaoEstadualTableParams.clear();	
			}
		}
		
		initializeAbaInscricaoEstadual();
	}
];
