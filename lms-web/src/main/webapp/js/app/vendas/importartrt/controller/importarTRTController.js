var importarTRTController = [
                         	"$scope",
                        	"$rootScope",
                        	"$state",
                        	"$http",
 	function($scope, $rootScope, $state, $http) {
		$scope.setConstructor({
			rest: "/vendas/importartrt"
		});

		$rootScope.showLoading = false;

    	$scope.fileUploadParams = {};

    	$scope.data.vigenciaInicialMinima = moment().add(1, 'day');
    	
    	$scope.listaErros = {};

		$scope.myPostSelectFunction = function($files){
			if($files){
				$("#anexoData").removeClass("lms-invalid");
			}
		}

		$scope.importarTRT = function(){
			$rootScope.showLoading = true;

			 var formData = new FormData();
			 
			 formData.append("vigenciaInicial", $scope.data.vigenciaInicial);

            if(!angular.isUndefined($scope.fileUploadParams.selectedFiles)){
                formData.append("arquivo" , $scope.fileUploadParams.selectedFiles[0]);
            }

            $scope.rest.doPostMultipart("importar", formData).then(
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
