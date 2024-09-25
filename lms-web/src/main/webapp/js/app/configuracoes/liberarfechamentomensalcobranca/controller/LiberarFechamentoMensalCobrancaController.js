
var LiberarFechamentoMensalCobrancaController = [
	"$scope","modalService",
 	function($scope, modalService) {
		$scope.setConstructor({
			rest: "/configuracoes/liberarFechamentoMensalCobranca"
		});
		
		$scope.liberarFechamento = function() {
			modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-27120"), windowClass: 'modal-confirm'})
			.then(function() {
				$scope.rest.doPost("liberarFechamento")
				.then(function(response) {
					$scope.addAlerts([{msg: $scope.getMensagem("LMS-27126", [$scope.competencia]), type: MESSAGE_SEVERITY.SUCCESS}]);
					$scope.competencia = response.competencia;
					$scope.liberarAcaoFechamento = !response.fechamentoHabilitado;
				});
			}); 
		};
		
		$scope.rest.doGet("findCompetencia").then(function(response) {
			$scope.competencia = response.competencia;
			$scope.liberarAcaoFechamento = !response.fechamentoHabilitado;
		});
	}
];
