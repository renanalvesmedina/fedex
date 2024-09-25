
var ManterMeuPerfilController = [
	"$scope",
	"$rootScope",
	"EmpresaFactory",
	"SecurityFactory",
 	function($scope, $rootScope, EmpresaFactory, SecurityFactory) {

		$scope.setConstructor({
			rest: "/configuracoes/meuPerfil"
		});

		$rootScope.showLoading = true;
		EmpresaFactory.findByUsuarioLogado().then(function(data) {
			$scope.empresasUsuarioLogado = data;
			$scope.data.empresa = {idEmpresa: $scope.usuarioLogado.idEmpresa};

			$scope.data.filial = {idFilial: $scope.usuarioLogado.idFilial
									,sgFilial: $scope.usuarioLogado.sgFilial
									,nmFilial: $scope.usuarioLogado.filial
								 };
		})['finally'](function() {
			$rootScope.showLoading = false;
		});

		$scope.change = function() {
			$scope.data.filial = null;
		};

		$scope.atualizarFilial = function() {
			var postParam = {idEmpresa: $scope.data.empresa.idEmpresa, idFilial: $scope.data.filial.idFilial};

			$scope.rest.doPost("atualizarFilial", postParam)
				.then(function(response) {
					SecurityFactory.getCurrentUser(true).then(function(currentUser) {
						$rootScope.usuarioLogado = currentUser;
						$scope.showSuccessMessage();
					});
				});
		};
	}
];
