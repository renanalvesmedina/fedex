
//função Global, não é um controller.
var configureLoginController = function($rootScope, $scope, SecurityFactory, loginSuccess) {

	$rootScope.showLoading = false;

	$scope.alterarSenha = false;
	$scope.divLogin = "";

	$scope.logar = function($event) {
		
		$scope.submitted = true;
		
		if (jQuery("#formLogin").hasClass("ng-invalid")) {
			$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);
			$event.preventDefault();
			return false;
		}
		
		$rootScope.showLoading = true;

		SecurityFactory.login($scope.usuario.login, $scope.usuario.senha)
		.then(function(usuarioLogado) {
			if (usuarioLogado) {
				if (usuarioLogado.key == "ADSM_SUGGEST_PASSWD_CHANGE_EXCEPTION_KEY") {
					if (confirm(usuarioLogado.error)) {
						$scope.alterarSenha = true;
						$scope.submit = $scope.change;
					} else {
						$scope.logar($event);
    				}
				} else {
					loginSuccess(usuarioLogado);
				}
			}
			$rootScope.showLoading = false;
			
		}, function(response) {
			$rootScope.showLoading = false;
        })['finally'](function() {
            $rootScope.showLoading = false;
        });

	};

	$scope.change  = function($event) {
		
		if (jQuery("form").hasClass("ng-invalid")) {
			$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);
			$event.preventDefault();
			return false;
		}
		
		if ($scope.usuario.senhaNova != $scope.usuario.senhaConfirmada) {
			var m = $scope.getMensagem("senhaNaoVerifica");
			$scope.addAlerts([{msg: m, type: MESSAGE_SEVERITY.WARNING}]);
			$scope.usuario.senhaNova = null;
			$scope.usuario.senhaConfirmada = null;
	        return false;
	    }
		
		$rootScope.showLoading = true;

		SecurityFactory.change($scope.usuario.senhaNova)
		.then(function(usuarioLogado) {
			if (usuarioLogado) {
				loginSuccess(usuarioLogado);
			}
			$rootScope.showLoading = false;
		}, function(response) {
			$scope.usuario.senhaNova = null;
			$scope.usuario.senhaConfirmada = null;
			$rootScope.showLoading = false;
        })['finally'](function() {
            $rootScope.showLoading = false;
        });

	};

	$scope.submit = $scope.logar;

};
