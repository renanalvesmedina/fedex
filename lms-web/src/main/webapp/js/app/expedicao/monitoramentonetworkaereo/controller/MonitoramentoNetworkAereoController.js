
var MonitoramentoNetworkAereoController = [
	"$scope",
	"$rootScope",
	"$location",
	"modalService",
 	function($scope, $rootScope, $location, modalService) {
		
		$scope.setConstructor({
			rest: "/expedicao/networkAereo"
		});
		
		$scope.controleAbas = {
					exibeAcompanhamento:false,
					exibeAcompanhamentoAwb:false
				};
		
		$scope.tmp = {tpStatusAwb : null};
		$scope.filterAwb = {};
		$scope.intervalAtualizacao;
		$scope.filterModalBloqLib = {};
		$scope.isLocalizacaoRetiradoAeroporto;
		
		$scope.$watch('tmp.awb', function (awb) {
			$scope.filter.awb = awb;
		}, true);
		
		$scope.getFilter = function() {
			if(!$scope.filter){
				return;
			}
			
			return $scope.filter;
		};
		
		$scope.clearFilter = function() { 
			$scope.filter = {};
			$scope.tmp = {tpStatusAwb : null};
			$scope.initializeAbaPesq();
		};
		
		
		$scope.abrirListagemPorCiaAerea = function(){
			$rootScope.showLoading = true;
			$location.path("/app/expedicao/monitoramentoNetworkAereo/acompanhamento");
		};
		
		$scope.initializeAbaPesq = function () {
			var dateMoment = moment();
			
			$scope.controleAbas.exibeAcompanhamento = false;
			$scope.controleAbas.exibeAcompanhamentoAwb = false;
			
			$scope.filter.dtAtualizacaoInicial = Utils.Date.formatMomentAsISO8601(dateMoment);
    		$scope.filter.dtAtualizacaoFinal = Utils.Date.formatMomentAsISO8601(dateMoment);
    		$scope.intervalAtualizacao = $scope.getIntervalAtualizacao();
		};
		
		$scope.getIntervalAtualizacao = function(){
			$scope.rest.doPost("getIntervalAtualizacao", {}).then(function(response) {
				$scope.intervalAtualizacao = response;
			},function() {
				
			});
		};				

		$scope.openModalBloqueioLiberacao = function(row) {
			var rest = $scope.rest;
			
			if(row.awb === null){
				return;
			} else {
				var myController = ["$scope", "$modalInstance", "TableFactory", function($scope, $modalInstance, TableFactory) {
					$rootScope.isPopup = true;
					$scope.title = "bloqueiosLiberacoes";
					$scope.innerTemplate = contextPath+"js/app/expedicao/monitoramentonetworkaereo/view/monitoramentoNetworkBloqLibModal.html";
					
					$scope.ocorrenciasPendencia = {};
					$scope.data = {};
					$scope.data.dtHrOcorrencia = moment();
					$scope.data.awbSelecionado = row.awb;
					$scope.data.nmCiaAerea = row.nmCiaAerea;
					$scope.isLocalizacaoRetiradoAeroporto = row.ultimoStatus !== null && row.ultimoStatus.value === "RE";
					
					$scope.listAwbOcorrenciaPendenciaTableParams = new TableFactory({
						service: rest.doPost,
						method: "findAwbOcorrenciaAwbPendenciaByAwb"
					});					
					
					rest.doPost("findOcorrenciaPendenciaAtiva").then(function(ocorrencias) {
						$scope.ocorrenciasPendencia = ocorrencias;
					});
					
					$scope.close = function() {
						$modalInstance.dismiss("cancel");
					};
					
					$scope.validateDtHrOcorrencia = function(modelValue, viewValue) {
						return {
							isValid: moment(modelValue) < moment(),
							messageKey: 'LMS-17055'
						};
					};
					
					$scope.validateOcorrenciaPendencia = function(){
						$rootScope.showLoading = true;
						var awbOcorrenciaPendenciaDTO = {};
						awbOcorrenciaPendenciaDTO.idAwb = row.idAwb;
						awbOcorrenciaPendenciaDTO.idCiaAerea = row.idCiaAerea;
						awbOcorrenciaPendenciaDTO.idOcorrenciaPendencia = $scope.data.idOcorrenciaPendencia;
						
						rest.doPost("validateOcorrenciaPendencia", awbOcorrenciaPendenciaDTO).then(function(response) {
							$rootScope.isPopup = false;
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
							$scope.data.idOcorrenciaPendencia = {};
							awbOcorrenciaPendenciaDTO.idOcorrenciaPendencia = {};
						});											
					};
					$scope.storeAwbOcorrenciaPendencia = function() {
						$rootScope.showLoading = true;
						var awbOcorrenciaPendenciaDTO = {};
						awbOcorrenciaPendenciaDTO.idAwb = row.idAwb;
						awbOcorrenciaPendenciaDTO.idCiaAerea = row.idCiaAerea;
						awbOcorrenciaPendenciaDTO.idOcorrenciaPendencia = $scope.data.idOcorrenciaPendencia;
						awbOcorrenciaPendenciaDTO.dhOcorrenciaPendencia = $scope.data.dtHrOcorrencia; 
						
						rest.doPost("storeAwbOcorrenciaPendencia", awbOcorrenciaPendenciaDTO).then(function(response) {
							$scope.listAwbOcorrenciaPendenciaTableParams.load(row.idAwb);
							$scope.showSuccessMessage();
							awbOcorrenciaPendenciaDTO.idOcorrenciaPendencia = {};
							$rootScope.isPopup = false;
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});
						
					};
				
					$scope.listAwbOcorrenciaPendenciaTableParams.load(row.idAwb);
				}];
				
				modalService.open({controller: myController, windowClass: "my-modal"});
			}
		};
		
		
		$scope.openModalEventos = function(row) {
			var rest = $scope.rest;
			
			if(row.awb === null){
				return;
			} else {
				var myController = ["$scope", "$modalInstance", "TableFactory", function($scope, $modalInstance, TableFactory) {
					$rootScope.isPopup = true;
					$scope.title = "eventos";
					$scope.innerTemplate = contextPath+"js/app/expedicao/monitoramentonetworkaereo/view/monitoramentoNetworkAereoIncluirEventos.html";
					
					$scope.localizacoes = {};
					$scope.data = {};
					$scope.data.dtEvento = moment();
					$scope.data.today = moment();
					$scope.data.awbSelecionado = row.awb;
					$scope.data.ciaAereaSelecionada = row.nmCiaAerea;
					$scope.isLocalizacaoRetiradoAeroporto = row.ultimoStatus !== null && row.ultimoStatus.value === "RE";
					
					$scope.listEventosTableParams = new TableFactory({
						service: rest.doPost,
						method: "findTrackingByCiaAereaAndAwb"
					});					
					
					rest.doPost("findLocalizacoesByCiaAereaAwb", {idAwb:row.idAwb, idCiaAerea:row.idCiaAerea}).then(function(localizacoesList) {
						$scope.localizacoes = localizacoesList;
					});
					
					$scope.close = function() {
						$modalInstance.dismiss("cancel");
					};
					
					$scope.validateDtEvento = function(modelValue, viewValue) {
						return {
							isValid: moment(modelValue) < moment(),
							messageKey: 'LMS-10071'
						};
					};
					
					$scope.storeEventosLocalizacao = function() {
						$rootScope.showLoading = true;
						var eventosLocalizacaoDTO = {};
						eventosLocalizacaoDTO.idAwb = row.idAwb;
						eventosLocalizacaoDTO.idLocalizacao = $scope.data.idLocalizacao;
						eventosLocalizacaoDTO.dtEvento = $scope.data.dtEvento; 
						
						rest.doPost("storeEventosLocalizacao", eventosLocalizacaoDTO).then(function(response) {
							$scope.listEventosTableParams.load(row.idAwb);
							$scope.showSuccessMessage();
							eventosLocalizacaoDTO.idLocalizacao = {};
							$rootScope.isPopup = false;
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});					
						
					};
					
					
					$scope.listEventosTableParams.load(row.idAwb);
				}];
				modalService.open({controller: myController, windowClass: "my-modal"});
				
			}
		};
		
		$scope.initializeAbaPesq();
		
	}
];
