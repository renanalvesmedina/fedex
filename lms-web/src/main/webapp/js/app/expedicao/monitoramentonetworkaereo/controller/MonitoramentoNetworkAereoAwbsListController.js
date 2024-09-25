
var MonitoramentoNetworkAereoAwbsListController = [
       	"$scope",
       	"$rootScope",
    	"$interval",
    	"$location",
    	"TableFactory",
    	"modalService",
     	function($scope, $rootScope, $interval, $location, TableFactory, modalService) {
		
		$scope.setConstructor({
			rest: "/expedicao/networkAereo"
		});
		
		var promise;
		
		$scope.buttonText = 'atualizar';
		$scope.atualizar = function() {
			if ($scope.buttonText === 'atualizar') {
				$scope.buttonText = 'pararAtualizacao';
				promise = $interval(refresh_control, $scope.intervalAtualizacao);
			} else {
				$scope.buttonText = 'atualizar';
				$interval.cancel(promise);
			}
		};
		
		$scope.listMonitoramentoAwbTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findMonitoramentoNetworkAereoAwb"
		});
		
		function refresh_control(){
		    $scope.listMonitoramentoAwbTableParams.load($scope.filterAwb);
		};
		
		$scope.initializeAbaAcompanhamento = function () {
			
			$scope.controleAbas.exibeAcompanhamento = true;
			$scope.controleAbas.exibeAcompanhamentoAwb = true;
			
			$scope.listMonitoramentoAwbTableParams.load($scope.filterAwb);
			$scope.atualizar();
			
		};
		
		$scope.initializeAbaAcompanhamento();
	}
];