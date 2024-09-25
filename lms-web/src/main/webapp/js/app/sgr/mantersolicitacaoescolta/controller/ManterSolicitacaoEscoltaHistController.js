
var ManterSolicitacaoEscoltaHistController = [
		"$scope",
		"$stateParams",
		"TableFactory",
		function($scope, $stateParams, TableFactory) {

			$scope.historicoTableParams = new TableFactory({
				service : $scope.rest.doPost,
				method : "findHistorico"
			});
			$scope.historicoTableParams.load($stateParams.id);

		}
];
