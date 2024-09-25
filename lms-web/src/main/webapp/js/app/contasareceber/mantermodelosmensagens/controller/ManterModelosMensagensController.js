
var ManterModelosMensagensController = [
                                    	"$scope",
                                    	"$rootScope",
                                    	"$stateParams",
                                    	"$state",
                                    	"$location",
                                    	"$modal",
                                    	"TableFactory",
                                    	"ManterDadosModeloMensagemFactory",
                                     	function($scope,$rootScope,$stateParams,$state,$location,$modal,TableFactory,ManterDadosModeloMensagemFactory) {

		$scope.items = [];
		
		$scope.fileUploadParams = {};
		$scope.fileUploadParams1 = {};
		
		$scope.listDadosModeloTableParams = new TableFactory({ service : ManterDadosModeloMensagemFactory.findDadosModeloMensagem});
		
		$scope.setConstructor({
			rest: "/contasareceber/modelosMensagens"
		});
		
		$scope.detailItem = function(row){
			$scope.detail(row);
			$scope.listDadosModeloTableParams.load({id: row.idModeloMensagem});
		};
		
		$scope.addEvent = function(){
			$scope.items[$scope.items.length] = "teste";
		};
	
		$scope.storeFiles = function() {
			$rootScope.showLoading = true;

			var formData = new FormData();
			formData.append("data", angular.toJson($scope.data));
			
			if (!angular.isUndefined($scope.fileUploadParams.selectedFiles)) {
				formData.append("dcAssuntoExist","true");
				formData.append("dcAssunto",$scope.fileUploadParams.selectedFiles[0]);
			}
			if (!angular.isUndefined($scope.fileUploadParams1.selectedFiles)) {
				formData.append("dcCorpoExist","true");
				formData.append("dcCorpo",$scope.fileUploadParams1.selectedFiles[0]);
			}
			$scope.rest.doPostMultipart("storeFiles", formData).then(
					function(response) {
						if ( !$scope.data.idModeloMensagem ){
							  $stateParams = {id:response};
								$state.current.data.view.tabs[2].$stateParams = {id:response};
								$state.current.data.view.tabs[2].disabled = false;
								$state.current.data.view.tabs[2].url = "/dados/"+response;
								window.location = window.location+response;
							}
							$scope.showSuccessMessage();
						
					}, function(erro) {
						// tratamento de erro
					})['finally'](function() {
				$rootScope.showLoading = false;
			});

		};
	}
];

