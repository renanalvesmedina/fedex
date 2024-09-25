
var ManterExcecaoNegativacaoPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams) {		
		// Inicializa a super classe ManterExcecaoNegativacaoController.
		angular.extend(this, $controller(ManterDadosModelosMensagensController, {"$scope": $scope,"modalParams":modalParams,"$modalInstance":$modalInstance}));
		
		$scope.title = "Carregar planilha";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/manterexcecaonegativacao/view/manterExcecaoNegativacaoPopup.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		

		$rootScope.showLoading = false;

    	$scope.fileUploadParams = {};
    	
    	$scope.listaErros = {};

		$scope.myPostSelectFunction = function($files){
			if($files){
				$("#anexoData").removeClass("lms-invalid");
			}
		};

		$scope.importarTRT = function(){
			$rootScope.showLoading = true;

			 var formData = new FormData();
			 
            if(!angular.isUndefined($scope.fileUploadParams.selectedFiles)){
                formData.append("arquivo" , $scope.fileUploadParams.selectedFiles[0]);
            }

            modalParams.novo.rest.doPostMultipart("importar", formData).then(
       		 function (response) {
      				$scope.showSuccessMessage();
      				$scope.fileUploadParams.clear();
      				$scope.listaErros = response;
                },
                function (erro) {
      				$scope.listaErros = {};
                }
            )['finally'](function(){
           	 $rootScope.showLoading = false;
            });
		};

		$scope.limparAnexos = function(){
			$scope.fileUploadParams.clear();
			$("#anexoData").removeClass("lms-invalid");
			$("#anexosForm").removeClass("submitted");
		};	
		
	}
];