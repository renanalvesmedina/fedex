
var AtualizacaoAutomaticaController = [
	"$scope", "$http", "$rootScope",
 	function($scope, $http, $rootScope) {
		$scope.setConstructor({
			rest: "/tabeladeprecos/atualizacaoAutomatica"
		});
		
		$scope.fileUploadParams = {};
		
		$scope.myPostSelectFunction = function($files){
			if($files){
				$("#anexoData").removeClass("lms-invalid");
			}
		};

		$scope.importacao = function(){
			$rootScope.showLoading = true;

			var formData = new FormData();
			if(!angular.isUndefined($scope.fileUploadParams.selectedFiles)){
				formData.append("arquivo" , $scope.fileUploadParams.selectedFiles[0]);
	        }
						
            $scope.rest.doPostMultipart("importar", formData).then(function (response) {
  				$scope.showSuccessMessage();
  				$scope.fileUploadParams.clear();
		
				if (response.fileName) {
					$scope.addAlerts([{msg: $scope.getMensagem("arquivoComErro"), type: "warning"}]);
					location.href = contextPath+"rest/report/"+response.fileName;
				}
				
            })['finally'](function(){
               	$rootScope.showLoading = false;
            });
            
		};

		$scope.atualizacaoManualRodoviario = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("atualizacaoManualRodoviario").then(function(response) {
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.atualizacaoManualAereo = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("atualizacaoManualAereo").then(function(response) {
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
	}
];
