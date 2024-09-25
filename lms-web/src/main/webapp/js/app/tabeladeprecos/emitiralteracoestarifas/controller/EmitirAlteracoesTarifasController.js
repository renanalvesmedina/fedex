
var EmitirAlteracoesTarifasController = [
	"$scope",
	"$rootScope",
 	function($scope, $rootScope) {
		$scope.setConstructor({
			rest: "/tabeladeprecos/alteracoesTarifas"
		});

		$scope.data = {};
		$scope.intervaloMaximoPeriodo = 365;

		$scope.limpar = function(){
			$scope.data = {};
			$("#relatorioForm").removeClass("submitted");
		};

		$scope.find = function() {
			$rootScope.showLoading = true;

			$scope.rest.doPost("find", $scope.data).then(function(data) {
				$rootScope.showLoading = false;

				if (data.reportLocator) {
					location.href = contextPath + "/viewBatchReport?" + data.reportLocator;
				}

			}, function() {
				$rootScope.showLoading = false;
			});
		};
	}
];
