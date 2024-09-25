
var ManterDadosModelosMensagensController = [
		"$scope",
		"$rootScope",
		"ManterDadosModeloMensagemFactory",
		"$stateParams",
		"TableFactory",
		"$modal",
		function($scope, $rootScope, ManterDadosModeloMensagemFactory,
				$stateParams, TableFactory, $modal) {
			$scope.data_modelo = {};
			$scope.data_modelo.idModeloMensagem = $stateParams.id;
			$scope.listDadosModeloTableParams = new TableFactory(
					{
						service : ManterDadosModeloMensagemFactory.findDadosModeloMensagem
					});
			$scope.listDadosModeloTableParams.load({
				"id" : $stateParams.id
			});
			
			$scope.modalInstance = null;
			
			$scope.detailDadoModelo = function(row){
				$scope.openModalDados(row.id,false);
			};
			
			$scope.openModalDados = function(id, novo) {
				$rootScope.isPopup = true;
				$scope.modalInstance = $modal.open({
					controller : ManterDadosModeloPopupController,
					templateUrl : contextPath
							+ "js/common/modal/view/modal-template.html",
					windowClass : "modal-detail",
					resolve : {
						modalParams : function() {
							return {
								"novo" : novo,
								"id" : id
							};
						}
					}
				});

				$scope.modalInstance.result.then(function() {
				}, function() {
					var params = {};
					$scope.listDadosModeloTableParams.load({
						"id" : $stateParams.id
					});
				})['finally'](function() {
					$rootScope.isPopup = false;
				});
			};

			$scope.storeDadosModeloMensagem = function() {
				ManterDadosModeloMensagemFactory.storeDadosModeloMensagem(
						$scope.data_modelo).then(function(data) {
					$scope.data_modelo = {};
					$scope.showSuccessMessage();
					$scope.close();
				}, function() {
					$scope.data_modelo = {};
					$scope.close();
				});
			};
		} ];