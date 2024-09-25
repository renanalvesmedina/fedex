
var ManterExcecaoNegativacaoController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"$modal",
 	function($scope, $rootScope, $stateParams, $modal) {
		
		$scope.setConstructor({
			rest: "/contasareceber/excecaoNegativacao"
		});
		
		$scope.carregarPlanilhas = function(){
			$scope.abrirpopup(false);
		};
		
		$scope.abrirpopup = function(novo) {
			$rootScope.isPopup = true;
			$scope.modalInstance = $modal.open({
				controller : ManterExcecaoNegativacaoPopupController,
				templateUrl : contextPath
						+ "js/common/modal/view/modal-template.html",
				windowClass : "modal-detail",
				resolve : {
					modalParams : function() {
						return {
							"novo" : $scope
						};
					}
				}
			});

			
		};

		$scope.enviarFaturas = function() {
			$rootScope.showLoading = false;
			window.open(contextPath+"/rest/contasareceber/excecaoNegativacao/enviarFaturas");
		};
	}
];