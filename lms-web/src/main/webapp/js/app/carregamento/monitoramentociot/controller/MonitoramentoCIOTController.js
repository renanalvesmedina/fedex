var MonitoramentoCIOTController = [
	"$scope",
	"$rootScope",
 	function($scope, $rootScope) {
		$scope.setConstructor({
			rest: "/carregamento/monitoramentoCIOT"
		});

		$scope.reenviar = function(){
			$rootScope.showLoading = true;

			$scope.rest.doPost("reenviar", $scope.data).then(function(data) {
				$scope.data.tpSituacao = data.tpSituacao;
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.habilitarBtnReenviar = function(){
			var habilitarBtnReenviar = false;

			if(angular.isDefined($scope.data) && angular.isDefined($scope.data.tpSituacao)){
				var tpSituacao = $scope.data.tpSituacao.value;
				var tpStatusControleCarga = $scope.data.tpStatusControleCarga.value;

				if(tpSituacao == 'R'){
					if(tpStatusControleCarga == 'ED' || tpStatusControleCarga == 'AD' || tpStatusControleCarga == 'FE' || tpStatusControleCarga == 'CA'){
						habilitarBtnReenviar = false;
					} else {
						habilitarBtnReenviar = true;
					}

				} else if(tpSituacao == 'M' || tpSituacao == 'O' || tpSituacao == 'D' ){
					habilitarBtnReenviar = true;
				}
			}

			return habilitarBtnReenviar;
		};
	}
];
