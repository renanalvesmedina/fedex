
var ManterSolicitacaoEscoltaController = [
		"$scope",
		function($scope) {
			$scope.populateExigencias = populateExigencias;
			$scope.validateQuilometragem = validateQuilometragem;
			$scope.clearOrigem = clearOrigem;
			$scope.clearDestino = clearDestino;

			$scope.setConstructor({
				rest : "/sgr/solicitacaoEscolta"
			});

			function populateExigencias() {
				$scope.rest.doGet("findExigencias").then(function(data) {
					$scope.exigenciasDataSource = data;
				});
			}

			function validateQuilometragem(modelValue, viewValue) {
				var a = $scope.filter.nrKmSolicitacaoEscoltaInicial;
				var b = $scope.filter.nrKmSolicitacaoEscoltaFinal;
				return {
					isValid : !a || !b || a <= b,
					messageParams : [ "Intervalo de quilometragem" ]
				};
			}

			function clearOrigem() {
				delete $scope.filter.filialOrigem;
				delete $scope.filter.clienteOrigem;
				delete $scope.filter.aeroportoOrigem;
			}

			function clearDestino() {
				delete $scope.filter.filialDestino;
				delete $scope.filter.clienteDestino;
				delete $scope.filter.aeroportoDestino;
				delete $scope.filter.aeroportoDestino;
				delete $scope.filter.rotaColetaEntrega;
			}

			$scope.populateExigencias();
		}
];
