
var ManterLoteCobrancaFaturaController = [
		"$scope",
		"$rootScope",
		"ManterLoteCobrancaFactory",
		"$stateParams",
		"TableFactory",
		"$modal",
		"modalService",
		function($scope, $rootScope, ManterLoteCobrancaFactory,
				$stateParams, TableFactory, $modal, modalService) {

			$scope.listItemLoteCobrancaTableParams = new TableFactory(
					{
						service : ManterLoteCobrancaFactory.findItensLoteCobranca
					});
			$scope.listItemLoteCobrancaTableParams.load({
				"id" : $stateParams.id
			});
			
		
			$scope.modalInstance = null;
			
			$scope.detailDadoModelo = function(row){
				$scope.openModalDados(row.id,false);
			};
			
			$scope.removeByIds = function() {
				var ids = [];
				console.log("$scope.listItemLoteCobrancaTableParams: "+$scope.listItemLoteCobrancaTableParams);
				$.each($scope.listItemLoteCobrancaTableParams.selected, function() {
					ids.push(this[$scope.getIdProperty()]);
				});
				if (ids.length === 0) {
					$scope.addAlerts([ { msg : $scope.getMensagem("erSemRegistro"), type : "warning" } ]);
				} else {
					$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
						$rootScope.showLoading = true;
						$scope.rest.doPost("removeFaturasByIds", ids).then(function(data) {
							$scope.showSuccessMessage();
							$scope.listItemLoteCobrancaTableParams.load({"id" : $stateParams.id});
						});
					})['finally'](function() {
						$rootScope.showLoading = false;
					});
				}
			}
			$scope.openModalImportar = function(id){
				$rootScope.isPopup = true;
				$scope.modalInstance = $modal.open({
					controller : ManterLoteCobrancaFaturaImportPopupController,
					templateUrl : contextPath
							+ "js/common/modal/view/modal-template.html",
					windowClass : "modal-detail",
					resolve : {
						modalParams : function() {
							return {
								"scope" : $scope,
								"id" : $stateParams.id
							};
						}
					}
				});

				$scope.modalInstance.result.then(function() {
				}, function() {
					$scope.listItemLoteCobrancaTableParams.load({
						"id" : $stateParams.id
					});
				})['finally'](function() {
					$rootScope.isPopup = false;
				});
			};
			
			$scope.openModalDados = function(id, novo) {
				$rootScope.isPopup = true;
				$scope.modalInstance = $modal.open({
					controller : ManterLoteCobrancaFaturaPopupController,
					templateUrl : contextPath
							+ "js/common/modal/view/modal-template.html",
					windowClass : "modal-detail",
					resolve : {
						modalParams : function() {
							return {
								"scope" : $scope,
								"novo" : novo,
								"id" : id
							};
						}
					}
							});	

				$scope.modalInstance.result.then(function() {
						}, function() {
					$scope.listItemLoteCobrancaTableParams.load({
						"scope": $scope,
						"id" : $stateParams.id
						});
				})['finally'](function() {
					$rootScope.isPopup = false;
				});
					};

		} ];