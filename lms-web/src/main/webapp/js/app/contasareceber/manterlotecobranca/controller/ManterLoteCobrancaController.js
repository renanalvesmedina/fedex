
var ManterLoteCobrancaController = [
                "$scope",
				"$rootScope",
				"$stateParams",
				"$modal",
			 	function($scope, $rootScope, $stateParams, $modal) {
	
	$scope.setConstructor({
		rest: "/contasareceber/loteCobranca"
	});

	$scope.consultar = function() {
		if($scope.filter.nrLote					== undefined &&
           $scope.filter.tpLote 				== undefined &&
		   $scope.filter.tpLoteEnviado			== undefined &&
		   $scope.filter.dtAlteracaoLoteFinal	== undefined &&
		   $scope.filter.descricao				== undefined &&
		   $scope.filter.fatura					== undefined &&
		   $scope.filter.dtAlteracaoLoteInicial == undefined &&
		   $scope.filter.dtAlteracaoLoteFinal 	== undefined &&
           $scope.filter.dtEnvioLoteInicial 	== undefined &&
		   $scope.filter.dtEnvioLoteFinal		== undefined) {

			$scope.addAlerts([{msg: $scope.getMensagem("LMS-00055"), type: "danger"}]);

		} else if (
			($scope.filter.dtEnvioLoteInicial || $scope.filter.dtEnvioLoteFinal) &&
            (!$scope.filter.dtEnvioLoteInicial || !$scope.filter.dtEnvioLoteFinal)) {

            $scope.addAlerts([{msg: $scope.getMensagem("LMS-05363"), type: "danger"}]);
		} else {
            $scope.listTableParams.load($scope.filter);
        }
	}
}];

