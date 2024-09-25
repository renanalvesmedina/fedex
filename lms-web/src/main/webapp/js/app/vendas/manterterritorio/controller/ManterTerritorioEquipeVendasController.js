var ManterTerritorioEquipeVendasController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"typeTab",
	"editTabState",
	"TableFactory",
	"$filter",
	"$locale",
	function($rootScope, $scope, $stateParams, typeTab, editTabState, TableFactory, $filter, $locale) {
		$scope.initAbas($stateParams, typeTab, editTabState);

		$scope.listEquipeVendas = new TableFactory({
			service: $scope.rest.doPost,
			method: "findEquipeVendas"
		});

		$scope.listHistoricoEquipeVendas = new TableFactory({
			service: $scope.rest.doPost,
			method: "findHistoricoEquipeVendas"
		});

		function atualizarTable() {
			$rootScope.showLoading = true;
			$scope.listEquipeVendas.load($scope.data.id).then(function() {})
				['finally'](function() {
					$rootScope.showLoading = false;
				});
		}

		function atualizarTableHistorico() {
			$rootScope.showLoading = true;
			$scope.listHistoricoEquipeVendas.load($scope.data.id).then(function() {})
			['finally'](function() {
				$rootScope.showLoading = false;
			});
		}

		function getEquipeVendasToSave() {
			var equipeVendas = [];

			$scope.listEquipeVendas.list.forEach(function(row) {
				equipeVendas.push({
					id: row.id,
					periodoFinal : row.periodoFinal,
					periodoInicial : row.periodoInicial,
					territorio : {id: $scope.data.id},
					tpExecutivo : row.tpExecutivo,
					usuario : row.usuario
				});
			});

			return equipeVendas;
		}

		$scope.salvarEquipeVendas = function () {
			$rootScope.showLoading = true;
			$scope.rest.doPost("salvarEquipeVendas", getEquipeVendasToSave())
				.then(function(response) {
					atualizarTable();
					atualizarTableHistorico();
					$scope.showSuccessMessage();
				})
				['finally'](function() {
					$rootScope.showLoading = false;
				});
		};

		$scope.isAtivo = function(row) {
			var inicio = Utils.Date.getMoment(row.periodoInicial, $locale);
			var fim = row.periodoFinal != null ? Utils.Date.getMoment(row.periodoFinal, $locale) : null;

			return !$scope.dataAtual.isBefore(inicio, 'day') && (fim == null || !$scope.dataAtual.isAfter(fim, 'day'));
		};

		atualizarTable();
		atualizarTableHistorico();
		$scope.dataAtual = moment();
	}
];
