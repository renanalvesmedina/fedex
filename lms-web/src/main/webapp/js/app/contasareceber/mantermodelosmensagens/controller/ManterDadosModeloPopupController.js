
var ManterDadosModeloPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	"ManterDadosModeloMensagemFactory",
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams,ManterDadosModeloMensagemFactory) {		
		// Inicializa a super classe ManterEnderecoPessoaController.
		angular.extend(this, $controller(ManterDadosModelosMensagensController, {"$scope": $scope,"modalParams":modalParams,"$modalInstance":$modalInstance}));
				
		$scope.title = "Dados modelo mensagem";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/mantermodelosmensagens/view/manterModelosMensagensDadosPopup.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		
		
		/** Define comportamento da modal */
		
		if(!modalParams.novo){	
			//modalParams.id
			ManterDadosModeloMensagemFactory.findDadoModeloMensagem(modalParams.id).then(function(response){
				$scope.data_modelo = response;
			});
		}
	}
];

