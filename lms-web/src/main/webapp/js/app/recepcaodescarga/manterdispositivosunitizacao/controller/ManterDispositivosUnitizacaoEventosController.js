
var ManterDispositivosUnitizacaoEventosController = [
	"$scope", 
	"TableFactory", 
	function($scope, TableFactory) {

		$scope.eventosTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findEventos",
				remotePagination: true
		});
		
		if ($scope.data.id) {
			$scope.eventosTableParams.load({id: $scope.data.id});
		} else {
			$scope.eventosTableParams.clear();	
		}
	}
];
