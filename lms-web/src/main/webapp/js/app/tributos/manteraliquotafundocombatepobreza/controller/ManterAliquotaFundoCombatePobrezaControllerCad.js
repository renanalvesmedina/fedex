
var ManterAliquotaFundoCombatePobrezaControllerCad = [
	"$scope",
	"$rootScope",
	"$stateParams",
 	function($scope, $rootScope, $stateParams) {
	
		$scope.initializeAbaCad = function(params){
			$scope.popularComboUfDestino();
			$scope.initAbaCad(params);
			$scope.setarDadosPadrao();
		}
		
		$scope.setarDadosPadrao = function(){
			if (!$stateParams.id) {
				$scope.data.pcAliquota = 0;
				$scope.data.dtVigenciaInicial = Utils.Date.formatMomentAsISO8601(moment().add('d', 1));
			}
		}
		
		$scope.clearData = function(){
			$scope.$parent.clearData();
			$scope.setarDadosPadrao();
		}
		
		$scope.initializeAbaCad($stateParams);
	}
];
