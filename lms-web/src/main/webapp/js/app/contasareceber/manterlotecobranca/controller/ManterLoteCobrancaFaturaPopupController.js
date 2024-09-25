
var ManterLoteCobrancaFaturaPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	"ManterLoteCobrancaFactory",
	"$stateParams",
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams,ManterLoteCobrancaFactory, $stateParams) {				
		// Inicializa a super classe ManterLoteCobrancaFaturaController.
		angular.extend(this, $controller(ManterLoteCobrancaFaturaController, {"$scope": $scope,"modalParams":modalParams,"$modalInstance":$modalInstance}));
				
		$scope.title = "Fatura";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/manterlotecobranca/view/manterLoteCobrancaFaturaPopup.html";
		$scope.modalParams = modalParams;

		$scope.data_modelo = {};
		$scope.data_modelo.idLoteCobranca = $stateParams.id;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		
		
		$scope.storeItemLoteCobranca = function() {
			ManterLoteCobrancaFactory.storeItemLoteCobranca(
				$scope.data_modelo).then(function(data) {
				$scope.data_modelo = {};
				$scope.showSuccessMessage();
				$scope.close();
			}, function() {
			});
		};
		/** Define comportamento da modal */
		if(!modalParams.novo){	
			//modalParams.id
			ManterLoteCobrancaFactory.findItemLoteCobranca(modalParams.id).then(function(response){
				$scope.data_modelo = response;
			});
		}
	}
];

