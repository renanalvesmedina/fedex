
var ManterEnderecoPessoaPopupController = [
	"$q", 
	"$scope",
	"$controller",
	"ManterEnderecoPessoaFactory",
	"$modalInstance", 
	"modalParams", 
	function($q, $scope, $controller, ManterEnderecoPessoaFactory, $modalInstance, modalParams) {		
		// Inicializa a super classe ManterEnderecoPessoaController.
		angular.extend(this, $controller(ManterEnderecoPessoaController, {$scope: $scope}));
				
		$scope.title = $scope.getMensagem("endereco");
		$scope.innerTemplate = contextPath+"js/app/configuracoes/manterenderecopessoa/view/manterEnderecoPessoaCad.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		
		
		/** Define comportamento da modal */
		if(modalParams.novo){	
			$scope.createEnderecoPessoa($scope.modalParams.idPessoa);
		} else {
			$scope.find($scope.modalParams.id);
		}
	}
];

