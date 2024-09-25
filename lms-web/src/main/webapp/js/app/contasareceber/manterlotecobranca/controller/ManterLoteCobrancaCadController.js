
var ManterLoteCobrancaCadController = [
                                   	"$scope",
                                   	"$rootScope",
                                   	"$stateParams",
                                   	"$state",
                                   	"$location",
                                   	"$modal",
                                   	"TableFactory",
                                   	"ManterLoteCobrancaFactory",
                                    	function($scope,$rootScope,$stateParams,$state,$location,$modal,TableFactory,ManterLoteCobrancaFactory) {

		$scope.setConstructor({
			rest: "/contasareceber/loteCobranca"
		});
		
		$scope.tmp = {};

		$scope.setDataPadrao = function() {

			if (!$stateParams.id) {
				$scope.rest.doGet("nrLote").then(function(data) {
					$scope.data.id = undefined;
					$scope.data.nrLote = data;
					$scope.data.tpLote = "";
					$scope.data.descricao = "";
					$scope.tmp.dataEnvioFormatada ="";
					var date = new Date();
					var hours = date.getHours();
				  var minutes = date.getMinutes();
				  var ampm = hours >= 12 ? 'pm' : 'am';
				  hours = hours % 12;
				  hours = hours ? hours : 12; // the hour '0' should be '12'
					if ( hours < 10 ){
						hours = "0"+hours;
					}
				  minutes = minutes < 10 ? '0'+minutes : minutes;
				  var strTime = hours + ':' + minutes ;
					$scope.tmp.dataAlteracaoFormatada =  date.getDate() + "/"+(date.getMonth()+1)+"/" + date.getFullYear() + "  " + strTime;
				},function() {
					$rootScope.showLoading = false;
				});
			} else {
				$scope.rest.doGet("findCustomById/" + $stateParams.id).then(function(response) {
					$scope.data.id = response.id;
					$scope.data.nrLote = response.nrLote;
					$scope.data.tpLote = response.tpLote;
					$scope.data.descricao = response.descricao;
					$scope.tmp.dataEnvio = response.dtEnvio;
					$scope.tmp.dataEnvioFormatada = response.dtEnvioFormatada;
					$scope.tmp.dataAlteracaoFormatada = response.dtAlteracaoFormatada;
					$("#tpLote").prop("disabled", true);

					if (response.dtEnvioFormatada != null) {
						$("#store").prop("disabled", true);	
					}
				}, function(error) {
					$rootScope.showLoading = false;
				});

			}
		};
		
		$scope.setDataPadrao();
		
		
		//Popup historico mensagem
		$scope.openModalDados = function(id, novo) {
			$rootScope.isPopup = true;
			$scope.modalInstance = $modal.open({
				controller : ManterLoteCobrancaHistoricoMensagemPopupController,
				templateUrl : contextPath
						+ "js/common/modal/view/modal-template.html",
				windowClass : "modal-detail",
				resolve : {
					modalParams : function() {
						return {
							"scope" : $scope,
							"novo" : novo,
							"id" : $stateParams.id
						};
					}
				}
			});
		};
		
		$scope.enviarLote = function() {
			$rootScope.showLoading = true;
			
			$scope.rest.doGet("enviarLote/"+$stateParams.id).then(function(dados) {
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.baixarArquivo = function() {
			$scope.downloading = true;

			$scope.rest.doGet("baixarArquivo/"+$stateParams.id).then(function(dados) {
				$scope.downloading = false;

				if(dados != null){
					var params = "";
					params += "table="+ dados.table;
					params += "&blobColumn="+ dados.blobColumn;
					params += "&idColumn="+ dados.idColumn;
					params += "&id="+ dados.id;

					window.open(contextPath+'attachment?' + params);
				}
			});
		};
	}
];

