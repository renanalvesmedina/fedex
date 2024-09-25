
var ManterLoteCobrancaFaturaImportPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	"ManterLoteCobrancaFactory",
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams,ManterLoteCobrancaFactory) {		
		$scope.fileUploadParams = {};
		
		angular.extend(this, $controller(ManterLoteCobrancaFaturaController, {"$scope": $scope,"modalParams":modalParams,"$modalInstance":$modalInstance}));
				
		$scope.title = "Importar Faturas";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/manterlotecobranca/view/manterLoteCobrancaFaturaImportPopup.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};	
		$scope.listaErros = {};
		
		$scope.importarTRT = function(){
			$rootScope.showLoading = true;

			var formData = new FormData();
			formData.append("id", angular.toJson(modalParams.id)); 
            if(!angular.isUndefined($scope.fileUploadParams.selectedFiles)){
                formData.append("arquivo" , $scope.fileUploadParams.selectedFiles[0]);
            }

            modalParams.scope.rest.doPostMultipart("importarFaturas", formData).then(
					function(response) {
						$scope.showSuccessMessage();
	      				$scope.fileUploadParams.clear();
	      				$scope.listaErros = response;
						$scope.showSuccessMessage();

						ManterLoteCobrancaFactory.findItemLoteCobranca(modalParams.id).then(function(response){
							modalParams.scope.data_modelo = response;
						});
						
					}, function(erro) {
					})['finally'](function() {
				$rootScope.showLoading = false;
			});
		};
		
		
//		if(!modalParams.novo){	
//			//modalParams.id
//			ManterLoteCobrancaFactory.findItemLoteCobranca(modalParams.id).then(function(response){
//				$scope.data_modelo = response;
//			});
//		}
	}
];

