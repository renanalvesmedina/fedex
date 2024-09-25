
var ManterContatoPessoaPopupController = [
	"$rootScope",
	"$scope",
	"$controller", 
	"ManterContatoPessoaFactory", 
	"$modalInstance", 
	"modalParams", 
	function($rootScope, $scope, $controller, ManterContatoPessoaFactory, $modalInstance, modalParams) {    		
		
		// Inicializa a super classe ManterContatoPessoaController.
		angular.extend(this, $controller(ManterContatoPessoaController, {$scope: $scope}));
		    		
		$scope.popup = true;
		
		$scope.title = $scope.getMensagem("contato");
		$scope.innerTemplate = contextPath+"js/app/configuracoes/mantercontatopessoa/view/manterContatoPessoaCad.html";
		$scope.modalParams = modalParams;
		
 		$scope.tipoContato = {}; 
		    		
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
		};			
		
		/** Inicializa a modal para visualizar/editar um registro */
		$scope.find = function(id){
			$rootScope.showLoading = true;
			ManterContatoPessoaFactory.findById(id).then(function(data) {	    		    		
				$scope.dados = data;
				$scope.setTipoContato(data.tipoContatos);
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};	
		
		$scope.setTipoContato = function(tipos){
			$scope.tipoContato.list = tipos;
		};
		
		/** Define comportamento da modal */
		if(modalParams.novo){
			$scope.create($scope.modalParams);
		} else {
			$scope.find($scope.modalParams.id);
		}
		
		$scope.loadDefaultValues();
	}
];

