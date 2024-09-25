
var ExemploTelaComponentesFileController = [
	"$scope",
	"$rootScope",
	"$http",
 	function($scope, $rootScope, $http) {

		/* --------------------- UpLoad ---------------------- */

		$scope.fileUploadParams = {};
		$scope.dados = {};
		$scope.storeFiles = function(){
			$rootScope.showLoading = true;

			 var formData = new FormData();
             formData.append("dados", angular.toJson($scope.dados));

             if(!angular.isUndefined($scope.fileUploadParams.selectedFiles)){
                   formData.append("qtdArquivos", $scope.fileUploadParams.selectedFiles.length);

                   for (var i = 0; i < $scope.fileUploadParams.selectedFiles.length; i++) {
                       formData.append("arquivo_" + i, $scope.fileUploadParams.selectedFiles[i]);
                   }
             }

             $scope.rest.doPostMultipart("storeFiles", formData).then(
        		 function (response) {
       				$scope.showSuccessMessage();
       				$scope.fileUploadParams.clear();
       				$scope.dados = {};
                 },
                 function (erro) {
                	 //tratamento de erro
                 }
             )['finally'](function(){
            	 $rootScope.showLoading = false;
             });

		};

		/* ------------------- Fim UpLoad -------------------- */

	}
];
