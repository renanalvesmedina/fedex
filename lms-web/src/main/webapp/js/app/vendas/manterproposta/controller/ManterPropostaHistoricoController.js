var ManterPropostaHistoricoController = [
	"$scope",
	"TableFactory",
	function($scope, TableFactory) {

		$scope.initializeAbaHistorico = function () {
			$scope.historicoTableParams = new TableFactory({
				service: $scope.rest.doPost,
				method: "findHistoricoEfetivacaoList",
				remotePagination: true
			});

			if ($scope.data && $scope.data.id) {
				$scope.historicoTableParams.load({id: $scope.data.id});
			} else {
				$scope.historicoTableParams.clear();
			}
		};

		$scope.initializeAbaHistorico();
	}
];

