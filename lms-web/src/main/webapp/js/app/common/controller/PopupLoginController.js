
var PopupLoginController = ["$rootScope", "$scope", "$modalInstance", "$state", "$stateParams", "SecurityFactory", "usuario",
                            function($rootScope, $scope, $modalInstance, $state, $stateParams, SecurityFactory, usuario) {
	
	$scope.usuario = usuario;
	
	var usuarioAtual = $scope.usuarioLogado ? $scope.usuarioLogado.login : null;

	configureLoginController($rootScope, $scope, SecurityFactory, function(usuarioLogado) {
		
		if (usuarioLogado) {
			$modalInstance.close();

			if (usuarioAtual == usuarioLogado.login) {
				$state.transitionTo($state.current, $stateParams, {
					reload: true,
					inherit: false,
					notify: true
				});
			} else {
				$state.transitionTo("app.home", {}, {
					reload: true,
					inherit: false,
					notify: true
				});
			}
		}
		
	});
	
}];
