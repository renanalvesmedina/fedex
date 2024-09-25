
var ManterSolicitacaoEscoltaCadController = [
		"$scope",
		"$stateParams",
		"FilialFactory",
		"UsuarioLmsFactory",
		function($scope, $stateParams, FilialFactory, UsuarioLmsFactory) {
			$scope.validateVlCarga = validateVlCarga;
			$scope.clearOrigem = clearOrigem;
			$scope.clearDestino = clearDestino;

			function init() {
				if ($stateParams.id) {
					$scope.populateDataById($stateParams.id);
	    		} else {
	    			$scope.setData({});
	    			FilialFactory.findFilialUsuarioLogado().then(function(data) {
	    				$scope.data.filialSolicitante = data;
	    			});
	    			UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {
	    				$scope.data.nmUsuarioSolicitacao = data.nmUsuario;
	    			});
	    		}
				$scope.populateExigencias();
			}

			function validateVlCarga(modelValue, viewValue) {
				var a = $scope.data.vlCargaCliente;
				var b = $scope.data.vlCargaTotal;
				return {
					isValid : !a || !b || a <= b,
					messageParams : [ "Valor de carga" ]
				};
			}

			function clearOrigem() {
				delete $scope.data.filialOrigem;
				delete $scope.data.clienteOrigem;
				delete $scope.data.aeroportoOrigem;
			}

			function clearDestino() {
				delete $scope.data.filialDestino;
				delete $scope.data.clienteDestino;
				delete $scope.data.aeroportoDestino;
				delete $scope.data.rotaColetaEntrega;
			}

			init();
		}
];
