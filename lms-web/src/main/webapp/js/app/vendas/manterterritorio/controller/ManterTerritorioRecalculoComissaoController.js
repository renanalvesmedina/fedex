var ManterTerritorioRecalculoComissaoController = [
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
		
		$scope.listRecalculoComissao = new TableFactory({
			service: $scope.rest.doPost,
			method: "findRecalculoComissao"
		});

		function atualizarRecalculoComissaoTable() {
			$rootScope.showLoading = true;
			$scope.listRecalculoComissao.load($scope.data.id).then(function() {})
				['finally'](function() {
					$rootScope.showLoading = false;
				});
		}
		
		function getRecalculoToSave() {
			var recalculoComissao = [];

			$scope.listRecalculoComissao.list.forEach(function(row) {
				recalculoComissao.push({
					executivoTerritorio : row.executivoTerritorio,
					idComissaoGarantida : row.idComissaoGarantida, 
					vlComissaoGarantida : row.vlComissaoGarantida,
					dtInicio : row.dtInicio,
					dtFim : row.dtFim,
					dtVigenciaEquipeVendasInicial : row.dtVigenciaEquipeVendasInicial,
					dtVigenciaEquipeVendasFinal : row.dtVigenciaEquipeVendasFinal,
					idDiferencaComissao : row.idDiferencaComissao,
					vlDiferencaComissao : row.vlDiferencaComissao,
					dtCompetencia : row.dtCompetencia,
					tpTeto : row.tpTeto,
					dsObservacao : row.dsObservacao,
					usuario : row.usuario
				});
			});

			return recalculoComissao;
		}
		
		$scope.salvarRecalculo = function () {
			$rootScope.showLoading = true;
			$scope.rest.doPost("salvarRecalculo", getRecalculoToSave())
				.then(function(response) {
					atualizarRecalculoComissaoTable();
					$scope.showSuccessMessage();
				})
				['finally'](function() {
					$rootScope.showLoading = false;
				});
		};

		atualizarRecalculoComissaoTable();
		$scope.dataAtual = moment();
	}
];
