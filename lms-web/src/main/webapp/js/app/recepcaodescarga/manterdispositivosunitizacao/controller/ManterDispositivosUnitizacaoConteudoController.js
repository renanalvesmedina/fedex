
var ManterDispositivosUnitizacaoConteudoController = [
	"$scope", 
	"TableFactory", 
	function($scope, TableFactory) {
		
		$scope.volumesTableParams = new TableFactory({
				service: $scope.rest.doPost,
				method: "findVolumes",
				remotePagination: true
			});
		$scope.dispositivosTableParams = new TableFactory({
				service: $scope.rest.doPost,
				method: "find",
				remotePagination: true
			});
		
		if ($scope.data.id) {
			$scope.volumesTableParams.load({id: $scope.data.id});
			$scope.dispositivosTableParams.load({dispositivoUnitizacaoPai:{id: $scope.data.id}});
		} else {
			$scope.volumesTableParams.clear();	
			$scope.dispositivosTableParams.clear();	
		}

	}
];
