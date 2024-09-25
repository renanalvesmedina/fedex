var ManterGruposEconomicosCadController = [
	"$scope", 
	"$rootScope",
	"$state",
	"$stateParams",
 	function($scope,$rootScope,$state,$stateParams) {
		
		function initializeAbaCad(params){
			if (params.id) {
				$rootScope.showLoading = true;
				$scope.findById(params.id).then(function(data) {
					$scope.setData(data);
					$rootScope.showLoading = false;
				},function() {
					$scope.limparDados();
					$rootScope.showLoading = false;
				});
			} else {
				$scope.loadDefaultValues();
			}
		}
		
		$scope.limparDados = function() {
			$scope.clearData();
			$scope.setData($scope.data);
			$scope.loadDefaultValues();
		};
						
		$scope.loadDefaultValues = function(){			
			$scope.data = {};
			$scope.data.tpSituacao = {};
			$scope.data.tpSituacao.value = 'A';
		};
		
		$scope.atualizaDescricao = function(){
			if ($scope.data.cliente) {
				$scope.data.descricao = $scope.data.cliente.nmPessoa;
			} else {
				$scope.data.descricao = "";
			}
		}
		
		$scope.storeCad = function($event) {
			$rootScope.showLoading = true;

			$scope.rest.doPost("", $scope.data).then(function(response) {
				$rootScope.showLoading = false;
				$state.transitionTo($state.current, {id: response[$scope.getIdProperty()]}, {
					reload: false,
					inherit: false,
					notify: true
				});
				initializeAbaCad($stateParams);
				$scope.showSuccessMessage();
			},function() {
				$rootScope.showLoading = false;
			});
		}
		
		initializeAbaCad($stateParams);
		
	}
];
