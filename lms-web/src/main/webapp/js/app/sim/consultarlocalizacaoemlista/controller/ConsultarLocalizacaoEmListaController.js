var ConsultarLocalizacaoEmListaController = [
	"$rootScope",
	"$scope",
	"$state",
	"modalService",
 	function($rootScope, $scope, $state, modalService) {
		$scope.setConstructor({
			rest: "/sim/consultarLocalizacaoEmLista"
		});

		var valorInicialFilter = function() {
            $scope.filter.dtEmissaoInicial = Utils.Date.formatMomentAsISO8601(moment().subtract('days', 31));
            $scope.filter.dtEmissaoFinal = Utils.Date.formatMomentAsISO8601(moment());
		};

		valorInicialFilter();

        $scope.abreDetalhe = function(o) {
             $state.go($state.get("app.sim_consultarLocalizacoesMercadorias.consultarLocalizacoesMercadorias.cad"), {id: o.id});
        };

		$scope.findComValidacao = function() {
			$rootScope.showLoading = true;
			$scope.listTableParams.clear();
			$scope.listTableParams.load($scope.filter).then(function(list) {
				$rootScope.showLoading = false;
				verificaRowCount(list);
			},function() {
				$rootScope.showLoading = false;
				
			})['finally'](function() {
                $rootScope.showLoading = false;
                
            });
		};

		$scope.resetFilter = function() {
			$scope.clearFilter();
			valorInicialFilter();
		};

	    var verificaRowCount = function(list) {
			if(list != undefined && list.length == 1) {
				$scope.abreDetalhe(list[0]);
			}
	    };

	}
];
