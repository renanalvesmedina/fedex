var ManterPropostaAnexoController = [
	"$scope",
	"$rootScope",
	"$state",
	"$http",
	"TableFactory",
	function($scope, $rootScope, $state, $http, TableFactory) {
		$scope.fileUploadParams = {};

		$scope.anexosTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findSimulacaoAnexoList",
			remotePagination: true
		});

		$scope.findAnexos = function(){
			$scope.anexosTableParams.load({id: $scope.data.id});

			var tpSituacaoAprovacao = $scope.data.tpSituacaoAprovacao ? $scope.data.tpSituacaoAprovacao.value : null;
			if ($scope.data.disableAll || (tpSituacaoAprovacao && (tpSituacaoAprovacao == "M" || tpSituacaoAprovacao == "F"))) {
				$scope.modoVisualizacao = true;
			} else {
				$scope.modoVisualizacao = false;
			}

		};

		$scope.downloadFile = function(id) {
			$rootScope.showLoading = true;
			$scope.rest.doGet("executeDownloadSimulacaoAnexo?idSimulacaoAnexo=" + id).then(function(data) {
				if(data != null){
					var params = "";
					params += "table="+ data.table;
					params += "&blobColumn="+ data.blobColumn;
					params += "&idColumn="+ data.idColumn;
					params += "&id="+ data.id;

					window.open(contextPath+'attachment?' + params);
				}

				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.myPostSelectFunction = function($files){
			if($files){
				$("#anexoData").removeClass("lms-invalid");
			}
		};

		$scope.salvarAnexo = function(){

			if (angular.isUndefined($scope.fileUploadParams)
					|| angular.isUndefined($scope.fileUploadParams.selectedFiles)
						|| $scope.fileUploadParams.selectedFiles.length == 0){
				$scope.addAlerts([{msg: window.AlertsConstants.CAMPO_OBRIGATORIO, type: MESSAGE_SEVERITY.WARNING}]);
				$("#anexoData").addClass("lms-invalid");
				return false;
			}

			$rootScope.showLoading = true;
			$scope.simulacaoAnexo.simulacao = {};
			$scope.simulacaoAnexo.simulacao.idSimulacao = $scope.data.id;

			$http({
                method: 'POST',
                url: contextPath+'rest/vendas/proposta/storeSimulacaoAnexo',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("simulacaoAnexo", angular.toJson(data));
                    formData.append("arquivo", $scope.fileUploadParams.selectedFiles[0]);
                    return formData;
                },
                data: $scope.simulacaoAnexo
            }).
            success(function (data, status, headers, config) {
            	$scope.limparAnexos();
            	$rootScope.showLoading = false;
            	$scope.showSuccessMessage();
            	$scope.findAnexos();
            }).
            error(function (data, status, headers, config) {
            	$rootScope.showLoading = false;
            });
		};

		$scope.removeAnexosByIds = function() {
			var ids = [];
			$.each($scope.anexosTableParams.selected, function() {
				ids.push(this.idSimulacaoAnexo);
			});

			if (ids.length === 0) {
				$scope.addAlerts([ {msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING } ]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(
					function() {
						$rootScope.showLoading = true;
						$scope.rest.doPost("removeSimulacaoAnexoByIds", ids).then(function(data) {
								$rootScope.showLoading = false;
								$scope.showSuccessMessage();
								$scope.findAnexos();
						}, function() {
							$rootScope.showLoading = false;
						});

					});
			}
		};

		$scope.limparAnexos = function(){
			$scope.simulacaoAnexo = {};
			$scope.fileUploadParams.clear();
			$("#anexoData").removeClass("lms-invalid");
			$("#anexosForm").removeClass("submitted");
		};

		$scope.findAnexos();
	}
];

