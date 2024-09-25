
var ManterLoteCobrancaHistoricoMensagemPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	"ManterLoteCobrancaFactory",
	"TableFactory",
	"$modal",
	"$stateParams",
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams,ManterLoteCobrancaFactory,TableFactory, $modal, $stateParams) {		
		$scope.title = "Historico Mensagem";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/manterlotecobranca/view/manterLoteCobrancaHistoricoMensagemPopup.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		
		
		$scope.initializePopup = function(){
			$scope.historicoMsgTableParams = new TableFactory({
				remotePagination: false
			});

			$scope.listHistoricoMsg = {};
		};
		
		$scope.initializePopup();

		var rest = modalParams.scope.rest;

		$scope.findHistoricoMensagem = function(id) {
			$rootScope.showLoading = true;
			if(rest != undefined) {
				rest.doGet("findHistoricoMensagem/"+id).then(function(dados) {
					$scope.listHistoricoMsg = dados;
					$rootScope.showLoading = false;
				},function() {
					$rootScope.showLoading = false;
				});
			}
		};

		$scope.findHistoricoMensagem(modalParams.id);

		//Popup evento mensagem
		$scope.openEventos = function(id, novo) {
			$rootScope.isPopup = true;
			$scope.modalInstance = $modal.open({
				controller : ManterLoteCobrancaHistoricoMensagemEventoPopupController,
				templateUrl : contextPath
						+ "js/common/modal/view/modal-template.html",
				windowClass : "modal-detail",
				resolve : {
					modalParams : function() {
						return {
							"scope" : $scope,
							"rest" : rest,
							"novo" : novo,
							"monit_id" : $scope.listHistoricoMsg[0].idMonitoramentoMensagemEvento
						};
					}
				}
			});
		};
		
		//Popup conteudo mensagem
		$scope.openConteudos = function(id, novo) {
			$rootScope.isPopup = true;
			$scope.modalInstance = $modal.open({
				controller : ManterLoteCobrancaHistoricoMensagemConteudoPopupController,
				templateUrl : contextPath
						+ "js/common/modal/view/modal-template.html",
				windowClass : "modal-detail",
				resolve : {
					modalParams : function() {
						return {
							"scope" : $scope,
							"novo" : novo,
							"rest" : rest,
							"monit_id" : $scope.listHistoricoMsg[0].idMonitoramentoMensagemConteudo
						};
					}
				}
			});
		};
		
	}
];

