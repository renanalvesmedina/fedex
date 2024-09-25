
var ManterRecibosDeOutrasEmpresasCadController = [ 
	"$scope",
	"$stateParams",
	"FilialFactory",
	"UsuarioLmsFactory",
	"translateService", 
	function($scope, $stateParams, FilialFactory, UsuarioLmsFactory, translateService) {

		/** Carrega dados da filial do usuario logado */
		$scope.loadCurrentFilial = function() {
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
				$scope.data.filial = {};
				$scope.data.filial = data;
			}, function() {

			});
		};
		
		/** Carrega dados do usuário logado */
		$scope.loadCurrentUsuario = function() {
			UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {					
				$scope.data.usuario = {};
				$scope.data.usuario = data;					
			}, function() {

			});
		};
		
		/** valida a data de emissão do recibo */
		$scope.$watch('data.dtEmissaoRecibo', function(data) {
			if(data > new Date()) {
				$scope.addAlerts([{msg: "LMS-23008", type: MESSAGE_SEVERITY.DANGER}]);
			}
		});
		
		/** valida o número do recibo */
		$scope.$watch('data.nrRecibo', function(data) {
			if(data < 0) {
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-24035", [nrRecibo.children[0].innerHTML]), type: MESSAGE_SEVERITY.DANGER}]);
			}
		});
		
		/** valida o valor do INSS */
		$scope.$watch('data.vlInss', function(data) {
			if(data < 0) {
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-24035", [vlInss.children[0].innerHTML]), type: MESSAGE_SEVERITY.DANGER}]);
			}
		});
		
		/** valida o valor da remuneração */
		$scope.$watch('data.vlRemuneracao', function(data) {
			if(data < 0) {
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-24035", [vlRemuneracao.children[0].innerHTML]), type: MESSAGE_SEVERITY.DANGER}]);
			}
		});
		
		/** validação do CPF ou CNPJ */
		$scope.validaCpfCnpj = function() {
			if($scope.data.nrEmpregador.length !== 11 && $scope.data.tipoIdentificadorEmpregador.dsIdentificador === "CPF") {
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-01130", [$scope.data.tipoIdentificadorEmpregador.dsIdentificador]), type: MESSAGE_SEVERITY.DANGER}]);
			}else if($scope.data.nrEmpregador.length !== 14 && $scope.data.tipoIdentificadorEmpregador.dsIdentificador === "CNPJ") {
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-01130", [$scope.data.tipoIdentificadorEmpregador.dsIdentificador]), type: MESSAGE_SEVERITY.DANGER}]);
			}
		};
		
		if ($stateParams.id) {
			$scope.populateDataById($stateParams.id);
		} else {
			$scope.setData({});
		}
		
		$scope.loadCurrentFilial();
		$scope.loadCurrentUsuario();
	}
];
