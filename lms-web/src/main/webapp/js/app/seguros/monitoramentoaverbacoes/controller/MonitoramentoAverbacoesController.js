
var MonitoramentoAverbacoesController = [
	"$scope",
	"RestAccessFactory",
	"TableFactory",
    "$rootScope",
 	function($scope, RestAccessFactory, TableFactory, $rootScope) {

        $scope.intervaloMaximoPeriodo = 4;

        $scope.setConstructor({
            rest: "/seguros/monitoramentoAverbacoes"
        });
		
		$scope.clearFilter = function() {
			$scope.filter = {};
            $scope.listTableParams.clear();

            $scope.loadDefaultValues();

            $scope.setFilter($scope.filter);
		};
		
		$scope.consultarExportar = function(){
			if("C" == $scope.filter.acao){
				$scope.consultar();
			} else if("E" == $scope.filter.acao){
				$scope.exportCsv();
			}
		};

		$scope.consultar = function() {
            $rootScope.showLoading = true;
            $scope.listTableParams.clear();

            $scope.listTableParams.load($scope.filter).then(function() {
                $rootScope.showLoading = false;
                $scope.naoAverbado = $scope.filter.averbado.value === 'N';
            }, function() {
                $rootScope.showLoading = false;
            });
        };

        $scope.reenviar = function() {
            $rootScope.showLoading = true;

            var idsMonitoramentoAverbacao = [];

            angular.forEach($scope.listTableParams.selected, function(item) {
                idsMonitoramentoAverbacao.push(item.idAverbacaoDoctoServico);
            });

            $scope.rest.doPost("reenviar", idsMonitoramentoAverbacao)
                .then(function() {
                    $scope.consultar();
                    $rootScope.showLoading = false;
                }, function() {
                    $rootScope.showLoading = false;
                }
            )
        };
		
	}
];
