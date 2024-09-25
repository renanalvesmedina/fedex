
var ManterVirusCargaController = [
	"$scope",
	function($scope) {
		$scope.setConstructor({
			rest: "/sgr/virusCarga"
		});
		$scope.$watch("filter.controleCarga", function() {
			if ($scope.filter.controleCarga) {
				delete $scope.filter.nrIscaCarga;
				delete $scope.filter.nrChave;
				delete $scope.filter.nrVolume;
				delete $scope.filter.cliente;
				delete $scope.filter.dhInclusaoInicial;
				delete $scope.filter.dhInclusaoFinal;
				delete $scope.filter.nrNotaFiscal;
				delete $scope.filter.dhAtivacaoInicial;
				delete $scope.filter.dhAtivacaoFinal;
			}
		});
		$scope.$watch("filter.nrChave", function() {
			if (angular.isDefined($scope.filter.nrChave)) {
				$scope.filter.nrChave = $scope.filter.nrChave.replace(/\D/g, "");
			}
		});
		$scope.$watch("filter.nrNotaFiscal", function() {
			if (!$scope.filter.nrNotaFiscal) {
				delete $scope.filter.dsSerie;
			}
		});
		$scope.removeByIds = function() {
			var ids = [];
			$.each($scope.listTableParams.selected, function() {
				ids.push(this.id);
			});
			if (ids.length == 0) {
				$scope.addAlerts([{ msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING }]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
					$scope.excluindo = true;
					$scope.rest.doPost("removeByIds", ids).then(function(data) {
						$scope.excluindo = false;
						$scope.showSuccessMessage();
						$scope.listTableParams.load($scope.filter);
					}, function() {
						$scope.excluindo = false;
					});
				});
			}
		};
	}
];
