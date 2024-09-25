
var MonitoramentoNetworkAereoLiberacaoFiscalListController = [
	"$scope",
	"$interval",
	"$location",
	"domainService",
	"TableFactory",
 	function($scope, $interval, $location, domainService, TableFactory) {
		
		$scope.setConstructor({
			rest: "/expedicao/networkAereo"
		});
		
		function refresh_control(){
		    console.log("refresh_control");
		    $scope.listMonitoramentoLiberacaoFiscalTableParams.load($scope.filterAwb);
		}
		
		
		var promise;
		
		$scope.buttonText = 'atualizar';
		$scope.atualizar = function() {
			if ($scope.buttonText === 'atualizar') {
				$scope.buttonText = 'pararAtualizacao';
				promise = $interval(refresh_control, 480000);
			} else {
				$scope.buttonText = 'atualizar';
				$interval.cancel(promise);
			}
		};
		
		$scope.listMonitoramentoLiberacaoFiscalTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findMonitoramentoNetworkAereoAwb"
		});
		
		$scope.initializeAbaLiberacaoFiscal = function () {
			
			$scope.controleAbas.exibeAcompanhamento = true;
			$scope.controleAbas.exibeAcompanhamentoAwb = false;
			$scope.controleAbas.exibeLiberacaoFiscal = true;
			
			$scope.listMonitoramentoLiberacaoFiscalTableParams.load($scope.filterAwb);
			$scope.atualizar();

		};		
		$scope.initializeAbaLiberacaoFiscal();	
	}
];