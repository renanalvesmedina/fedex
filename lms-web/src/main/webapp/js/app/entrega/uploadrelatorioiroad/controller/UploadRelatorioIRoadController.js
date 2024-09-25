var UploadRelatorioIRoadController = [
	"$rootScope", 
	"$scope", 
	function($rootScope, $scope) {
		
		$scope.fileUploadParams = {};
		$scope.data = {};
		
		$scope.tiposArquivo = ["PATHFIND","ROUTEASY"];
		
		$scope.setConstructor({
			rest: "/entrega/uploadRelatorioIRoad"
		});
		
		$scope.myPostSelectFunction = function($files){
			if($files){
				$("#anexoRelatorio").removeClass("lms-invalid");
				$scope.data.resultadoProcesso = "";
			}	
		};
		
		$scope.upload = function() {

			$rootScope.showLoading = true;

				
			var formData = new FormData();
			if(!angular.isUndefined($scope.fileUploadParams.selectedFiles)){
				formData.append("arquivoUpload" , $scope.fileUploadParams.selectedFiles[0]);
	        }else{
	        	$scope.addAlerts([{msg: window.AlertsConstants.CAMPO_OBRIGATORIO, type: MESSAGE_SEVERITY.WARNING}]);
				$("#anexoRelatorio").addClass("lms-invalid");
				$rootScope.showLoading = false;
				return;
	        }
			
			formData.append("tpArquivo",$scope.data.tipoArquivo);
						
            $scope.rest.doPostMultipart("upload", formData).then(function (response) {
  				$scope.showSuccessMessage();
  				$scope.fileUploadParams.clear();
 				
		
  				$scope.data.resultadoProcesso = response;
				
            })['finally'](function(){
               	$rootScope.showLoading = false;
            });
			
			
		};

	}
];