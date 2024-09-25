
var ManterDadosBancariosPopupController = [
    "$rootScope",
	"$scope", 
	"$controller",
	"ManterDadosBancariosFactory", 
	"$modalInstance", 
	"modalParams", 
	function($rootScope, $scope, $controller, ManterDadosBancariosFactory, $modalInstance, modalParams) {    		
		
		// Inicializa a super classe ManterDadosBancariosController.
		angular.extend(this, $controller(ManterDadosBancariosController, {$scope: $scope}));
		
		$scope.popup = true;
		    		
		$scope.title = $scope.getMensagem("dadosBancarios");
		$scope.innerTemplate = contextPath+"js/app/configuracoes/manterdadosbancarios/view/manterDadosBancariosCad.html";
		$scope.modalParams = modalParams;
		    		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");				
		};
		    		
		$scope.limparDados = function() {    			
			var idPessoa 		= $scope.dados.idPessoa;
			var nmPessoa 		= $scope.dados.nmPessoa; 
			var nrIdentificacao = $scope.dados.nrIdentificacao; 
			var tpIdentificacao = $scope.dados.tpIdentificacao;		
				
			$scope.dados = {};
			$scope.dados.idPessoa = idPessoa;
			$scope.dados.nmPessoa = nmPessoa;
			$scope.dados.nrIdentificacao = nrIdentificacao; 
			$scope.dados.tpIdentificacao = tpIdentificacao;		    					
		};
		
		/** Inicializa a modal para criar um novo registro */
		$scope.create = function(modalParams){
			$scope.dados = {};
							
			$scope.dados.idPessoa = modalParams.idPessoa;
			$scope.dados.nmPessoa = modalParams.nmPessoa;
			$scope.dados.nrIdentificacao = modalParams.nrIdentificacao; 
			$scope.dados.tpIdentificacao = modalParams.tpIdentificacao;		
			$scope.dados.dtVigenciaInicial =  Utils.Date.formatMomentAsISO8601(moment());		
		};			
		
		/** Inicializa a modal para visualizar/editar um registro */
		$scope.find = function(id){
			$rootScope.showLoading = true;
			ManterDadosBancariosFactory.findById(id).then(function(data) {
				$scope.dados = data;
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};			
		
		/** Define comportamento da modal */
		if(modalParams.novo){	
			$scope.create($scope.modalParams);
		} else {
			$scope.find($scope.modalParams.id);
		}
		
	}
];

