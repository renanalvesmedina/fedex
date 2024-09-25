
var ManterFornecedorEscoltaController = [
	"$scope",
	"$rootScope",
	function($scope, $rootScope) {
		$scope.setConstructor({
			rest : "/sgr/fornecedorEscolta"
		});

		$scope.onBlurNrIdentificacao = onBlurNrIdentificacao;
		$scope.onChangeDsTelefone = onChangeDsTelefone;

		function onBlurNrIdentificacao() {
			var nrIdentificacao = $scope.data.nrIdentificacao;
			
			if (nrIdentificacao !== '' && nrIdentificacao !== undefined){
				$rootScope.showLoading = true;
				$scope.rest.doGet("findNmPessoaByNrIdentificacao/" + nrIdentificacao).then(function(data) {
					$scope.data.nmPessoa = data;
				})['finally'](function(){
					$rootScope.showLoading = false;
				});
			}
		}

		function onChangeDsTelefone() {
			if (!$scope.data.dsTelefone1) {
				$scope.data.dsTelefone1 = $scope.data.dsTelefone2;
				delete $scope.data.dsTelefone2;
			}
			if (!$scope.data.dsTelefone2) {
				$scope.data.dsTelefone2 = $scope.data.dsTelefone3;
				delete $scope.data.dsTelefone3;
			}
		}
	}
];
