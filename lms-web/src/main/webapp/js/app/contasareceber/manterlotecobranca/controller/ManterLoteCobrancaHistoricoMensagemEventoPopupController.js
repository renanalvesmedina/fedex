
var ManterLoteCobrancaHistoricoMensagemEventoPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	"ManterLoteCobrancaFactory",
	"TableFactory",
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams,ManterLoteCobrancaFactory, TableFactory) {		
		$scope.title = "Eventos";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/manterlotecobranca/view/manterLoteCobrancaHistoricoMensagemEventoPopup.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		
		
		$scope.initializePopup = function(){
			$scope.historicoMsgEventoTableParams = new TableFactory({
				remotePagination: false
			});

			$scope.listHistoricoMsgEvent = {};
		};
		
		$scope.initializePopup();

		var rest = modalParams.rest;

		$scope.findHistoricoMensagemEvento = function(id, $scope) {
			$rootScope.showLoading = true;
			rest.doGet("findHistoricoMensagemEvento/"+id).then(function(dados) {
				$scope.listHistoricoMsgEvent = dados;
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.findHistoricoMensagemEvento(modalParams.monit_id, $scope);
	}
];

