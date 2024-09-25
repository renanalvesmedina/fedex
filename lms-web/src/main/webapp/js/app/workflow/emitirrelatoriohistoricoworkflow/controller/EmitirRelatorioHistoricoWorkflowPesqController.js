var EmitirRelatorioHistoricoWorkflowPesqController = [
	"$rootScope", 
	"$scope",
	function($rootScope, $scope) {

		$scope.filter = {};

		$scope.getIntervaloMax = function() {
			if ($scope.filter.cliente) {
				return;
			} else {
				return 31;
			}
		};
		
		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
	
			$scope.rest.doPost("carregarValoresPadrao", {}).then(function(data) {
				$scope.filter.regional = data.regionais;
				$scope.filter.filial = data.filial;
				$scope.selecionarRegional(data.idRegional);
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.limpar = function() {
			$scope.filter = {};
			$scope.initializeAbaPesq();
		};

		$scope.find = function() {
			$rootScope.showLoading = true;

			$scope.rest.doPost("find", $scope.filter).then(function(data) {
				$rootScope.showLoading = false;

				if (data.reportLocator) {
					location.href = contextPath + "/viewBatchReport?" + data.reportLocator;
				}

			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.$watch('filter.filial', function(data){
			if(data && !data.isBuscaValoresPadrao){
    			$rootScope.showLoading = true;
    			$scope.rest.doPost("findRegionalFilial", data.idFilial).then(function(data) {
    				$scope.selecionarRegional(data.idRegional);
    				$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});

    		}
		});

		$scope.selecionarRegional = function(idRegional){
			if(idRegional){
				for(var i = 0; i < $scope.filter.regional.length; i++){
					if($scope.filter.regional[i].idRegional == idRegional){
						$scope.filter.idRegional = idRegional;
						break;
					}
				}
			}
		};

		$scope.limparFilal = function(){
			$scope.filter.filial = null;
		};
		
		$scope.initializeAbaPesq();

	}
];
	
