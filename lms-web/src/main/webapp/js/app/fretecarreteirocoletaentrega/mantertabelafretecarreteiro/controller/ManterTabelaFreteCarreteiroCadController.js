
var ManterTabelaFreteCarreteiroCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"TableFactory",
	"$modal",
	"modalService",
	function($rootScope, $scope, $stateParams, $state, TableFactory, $modal, modalService) {
		$scope.tabelafretecarreteiro.currentDataType = $scope.dataType['geral'];
		
		$scope.isInformacoesDescontaFrete = true;
		$scope.isInformacoesFrequencia = true;
		$scope.isInformacoesUsuario = true;
		$scope.isInformacoesPremio = true;
		
		$scope.clientesTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findListTabelaFcValores" });
		
		/** Executa store do desconto */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: "warning"}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			var tabelaFcValoresDTO = !$scope.data.listTabelaFcValoresRest ? null : $scope.data.listTabelaFcValoresRest[0];
			
			if(tabelaFcValoresDTO){
				$scope.checkTabelaGeral(tabelaFcValoresDTO);
			} else {							
				$rootScope.showLoading = true;
				
				$scope.rest.doGet("findTabelaFcValoresGeral?id=" + $scope.data.idTabelaFreteCarreteiroCe).then(function(data) {							
					$rootScope.showLoading = false;
											
					$scope.data.listTabelaFcValoresRest = [data];
					
					$scope.checkTabelaGeral(data);
				}, function() {
					$rootScope.showLoading = false;
				});
			}		
		};
		
		$scope.checkTabelaGeral = function(tabelaFcValores){
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("checkZeroTabelaFcValores", tabelaFcValores).then(function(data) {	
				$rootScope.showLoading = false;
				
				if(data === 'true'){
					modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25111"), windowClass: 'modal-confirm'})
					.then(function() {
						$scope.doStore();
					});
				} else {
					$scope.doStore();
				}
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.doStore = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("store", $scope.getBean()).then(function(data) {				
				$scope.processStore(data);
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.getBean = function() {			
			return $scope.data;
		};  
		
		/** Processa resultados de inserir/atualizar */
		$scope.processStore = function(data){			
			$scope.data = data;
			$state.transitionTo($state.current, {id: data["idTabelaFreteCarreteiroCe"]}, {
				reload: false,
				inherit: false,
				notify: true
			});
			
			$scope.setData(data);
			
			$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
		};
				
		$scope.limparDados = function() {
			$scope.clearData();
						
			$scope.setData($scope.data);
			
			$scope.loadDefaultValues();		
		};
						
		$scope.loadDefaultValues = function(){			
			var defaultValue = 0.00;
			
			$scope.data = {};			
			$scope.data.disabled = false;
			$scope.data.listTabelaFcValoresRest = [$scope.createItemTabelaValor()];					
			$scope.data.pcPremioCte = defaultValue;
			$scope.data.pcPremioEvento = defaultValue;
			$scope.data.pcPremioDiaria = defaultValue;
			$scope.data.pcPremioVolume = defaultValue;
			$scope.data.pcPremioSaida = defaultValue;
			$scope.data.pcPremioFreteBruto = defaultValue;
			$scope.data.pcPremioFreteLiq = defaultValue;
			$scope.data.pcPremioMercadoria = defaultValue;
									
			$scope.loadCurrentFilial($scope.data);		
		};
		
		$scope.editGeral = function($event){
			$event.preventDefault();
			$event.stopPropagation();
			
			var tabelaFcValoresDTO = !$scope.data.listTabelaFcValoresRest ? null : $scope.data.listTabelaFcValoresRest[0];
			
			if(tabelaFcValoresDTO){
				$rootScope.isPopup = true;
				
				var modalInstance = $scope.openGeral(tabelaFcValoresDTO);
				
				modalInstance.result.then(function(data) {}, function(data) {				
					
				})['finally'](function() {
					$rootScope.isPopup = false;
				});				
			} else {							
				$rootScope.showLoading = true;
				
				$scope.rest.doGet("findTabelaFcValoresGeral?id=" + $scope.data.idTabelaFreteCarreteiroCe).then(function(data) {							
					$rootScope.showLoading = false;
											
					$scope.data.listTabelaFcValoresRest = [data];
					
					$rootScope.isPopup = true;
					
					var modalInstance = $scope.openGeral(data);
					
					modalInstance.result.then(function(data) {}, function(data) {				
						
					})['finally'](function() {
						$rootScope.isPopup = false;
					});
				}, function() {
					$rootScope.showLoading = false;
				});
			}
		};
				
		var VigenciaController = ["$rootScope","$scope", "$modalInstance", "modalParams", function($rootScope, $scope, $modalInstance, modalParams) {						
			$scope.title = 'vigencia';
			$scope.innerTemplate = contextPath+"js/app/fretecarreteirocoletaentrega/mantertabelafretecarreteiro/view/popup/manterTabelaFreteCarreteiroVigencia.html";								
			
			$scope.data = modalParams.data;
			$scope.isMatriz = modalParams.isMatriz;
			$scope.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
			
			$scope.$watch('data.dhVigenciaInicial', function (value) {
    			if (!value) {
    				$scope.data.dhVigenciaFinal = null;
    			}
    		}, true);
			
			$scope.$watch('data.dhVigenciaFinal', function (value) {
    			if (value) {
    				if(moment().isAfter($scope.data.dhVigenciaFinal)){
    					$scope.addAlerts([{msg: "LMS-00007", type: "warning"}]);    				
    					$scope.data.dhVigenciaFinal = null;
    				} else {
    					$rootScope.clearAlerts();
    				}
    			}
    		}, true);
			
			$scope.close = function() {
				$modalInstance.dismiss("close");
			};       
			
			$scope.inativar = function(){
				if(!$scope.data.dhVigenciaFinal){
					$scope.addAlerts([{msg: "LMS-25116", type: "danger"}]);
					return;
				}
				
				$modalInstance.dismiss({ idTabelaFreteCarreteiroCe: $scope.data.idTabelaFreteCarreteiroCe, dhVigenciaFinal: $scope.data.dhVigenciaFinal });				
			};
		}];
		
		$scope.editVigencia = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			$rootScope.isPopup = true;
			
			var modalInstance = $modal.open({
				controller: VigenciaController,
				templateUrl: contextPath+"js/common/modal/view/modal-template.html",
				windowClass: "modal-vigencia",
				resolve: {
	    			modalParams: function() {
        		    	return { data: $scope.data, isMatriz: $scope.tabelafretecarreteiro.isMatriz };
	    			}
	    		}}			
			);
			
			modalInstance.result.then(function(data) {}, function(data) {
				if(data.idTabelaFreteCarreteiroCe){
					$rootScope.showLoading = true;
					
					$scope.rest.doPost('inativar', data).then(function(data) {
						$rootScope.showLoading = false;

						$scope.processStore(data);
					}, function() {
						$rootScope.showLoading = false;
					});
				}				
			})['finally'](function() {
				$rootScope.isPopup = false;
			}); 			  						   			    			    			
		};
		
		$scope.createTabelaFreteCarreteiro = function(){
			$scope.loadDefaultValues();
			
			$scope.setData($scope.data);			
		};	
		
		$scope.remover = function(){			    			
			$scope.confirm($scope.getMensagem("erExcluirRegistroAtual")).then(function() {    					
				$rootScope.showLoading = true;
				
				$scope.rest.doDelete($scope.data.idTabelaFreteCarreteiroCe).then(function(data) {
    				$rootScope.showLoading = false;
    				
					$scope.showSuccessMessage();
					
					$scope.limparDados();
				}, function() {
					$rootScope.showLoading = false;
				});        				
			});
		};
		
		$scope.clonarTabelaFreteCarreteiroCe = function(){
			modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25123"), windowClass: 'modal-confirm'})
			.then(function() {
				$scope.rest.doGet("clonarTabelaFreteCarreteiroCe?id=" + $scope.data.idTabelaFreteCarreteiroCe).then(function(data) {							
					$rootScope.showLoading = false;
											
					$scope.processStore(data);
				}, function() {
					$rootScope.showLoading = false;
				}); 
			});
		};
		
		/** Processa inicializacao da aba cadastro */
		$scope.initializeAbaCad = function(params) {
			if (params.id) {
				$scope.findTabelaFreteCarreteiroCe(params.id);
    		} else {
    			$scope.createTabelaFreteCarreteiro();
    		}
		};
		
		$scope.initializeAbaCad($stateParams);
	}	
];
