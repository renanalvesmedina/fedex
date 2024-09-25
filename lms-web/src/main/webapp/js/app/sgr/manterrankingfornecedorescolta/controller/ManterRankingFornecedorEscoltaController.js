
var ManterRankingFornecedorEscoltaController = [
	"$scope", 
	"$rootScope",
 	function($scope, $rootScope) {
		
		$scope.setConstructor({
			rest: "/sgr/rankingFornecedorEscolta"
		});
		
		$scope.populateDataById = function(id) {
			$rootScope.showLoading = true;
			$scope.findById(id).then(function(data) {
				$scope.setData(data);
				//FIXME: deveria ter uma maneira de não precisar replicar 
				//cada função do basecontroller quando é necessária uma regra mais específica
				if ($scope.data.fornecedoresEscolta) {
					$.each($scope.data.fornecedoresEscolta, function(i, e) {
						$scope.data.fornecedoresEscolta[i] = { data : { fornecedorEscolta : e }};
					});
				}
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.store = function()  {
			var data = angular.copy($scope.data);
			var list = data.fornecedoresEscolta;
			data.fornecedoresEscolta = [];
			if (list) {
				$.each(list.untouched.concat(list.added.concat(list.updated)), function(i, e) {
					data.fornecedoresEscolta.push(e.data.fornecedorEscolta);
				});
			}
			delete data.fornecedorEscolta;
			$rootScope.showLoading = true;
			$scope.rest.doPost("store", data).then(function(Response){
				$scope.showSuccessMessage();
			})['finally'](function() {
				$rootScope.showLoading = false;
			});
		}
		
		
		$scope.removeByIds = function() {
			var ids = [];
			$.each($scope.listTableParams.selected, function() {
				ids.push(this.id);
			});
			if (ids.length == 0) {
				$scope.addAlerts([{ msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING }]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
					$scope.excluindo = true;
					$scope.rest.doPost("removeByIds", ids).then(function(data) {
						$scope.excluindo = false;
						$scope.showSuccessMessage();
						$scope.listTableParams.load($scope.filter);
					}, function() {
						$scope.excluindo = false;
					});
				});
			}
		};
		
	}
];
