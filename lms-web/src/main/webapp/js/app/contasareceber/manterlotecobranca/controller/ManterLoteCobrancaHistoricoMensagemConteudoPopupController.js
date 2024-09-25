
var ManterLoteCobrancaHistoricoMensagemConteudoPopupController = [
	"$q", 
	"$scope",
	"$rootScope",
	"$controller",
	"$modalInstance", 
	"modalParams", 
	"ManterLoteCobrancaFactory",
	"TableFactory",
	"$sce",
	function($q, $scope,$rootScope, $controller, $modalInstance, modalParams,ManterLoteCobrancaFactory, TableFactory, $sce) {		
		$scope.title = "Conteudo";
		$scope.innerTemplate = contextPath+"js/app/contasareceber/manterlotecobranca/view/manterLoteCobrancaHistoricoMensagemConteudoPopup.html";
		$scope.modalParams = modalParams;
		     		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");
		};		
		
		$scope.initializePopup = function(){
			$scope.historicoMsgConteudoTableParams = new TableFactory({
				remotePagination: false
			});

			$scope.listHistoricoMsgConteudo = {};
		};
		
		$scope.initializePopup();

		var rest = modalParams.rest;

		$scope.findHistoricoMensagemEvento = function(id) {
			$rootScope.showLoading = true;
			rest.doGet("findHistoricoMensagemConteudo/"+id).then(function(dados) {
				$scope.listHistoricoMsgConteudo = dados;
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.findHistoricoMensagemEvento(modalParams.monit_id);

		$scope.toTrustedHTML = function(){
			if ($scope.listHistoricoMsgConteudo[0] != undefined){
				return $sce.trustAsHtml( $scope.listHistoricoMsgConteudo[0].dcMensagem );
			}
		};
	}
];

