
var MonitoramentoNetworkAereoListCiaAereaController = [
	"$scope",
	"$rootScope",
	"$interval",
	"$location",
	"domainService",
	"TableFactory",
	"$state",
 	function($scope, $rootScope, $interval, $location, domainService, TableFactory, $state) {
		
		function findDomainValueByValue(value) {
			var result = null;
			angular.forEach($scope.domainTipoLocalizacaoAWB, function(domainValue) {
				if (domainValue.value === value) {
					result = domainValue;
					return false;
				}
			});
			return result;
		}
		
		function setLocationAcompanhamentoAwb(situacao, row) {
			angular.copy($scope.filter, $scope.filterAwb);
			
			if(row.idCiaAerea != null) {
				$scope.filterAwb.ciaAerea = {idEmpresa : row.idCiaAerea};
			}
			
			if(situacao !== null) {
				$scope.filterAwb.situacao = situacao;
			}
			
			acompanhamentoAwbLocationPath();
		}
		
		function acompanhamentoAwbLocationPath() {
			$location.path("/app/expedicao/monitoramentoNetworkAereo/acompanhamentoAwb");
		}
		
		function refresh_control() {
  		    $scope.listMonitoramentoCiaAereaTableParams.load($scope.filter);  		  
		}
		
		
		var promise;
		
		$scope.domainTipoLocalizacaoAWB = null;
		var promiseTipoLocalizacaoAWB = domainService.findDomain('DM_TIPO_LOCALIZACAO_AWB');
		promiseTipoLocalizacaoAWB.then(function(data){
			$scope.domainTipoLocalizacaoAWB = data;
		});
		
		$scope.buttonText = 'atualizar';
		$scope.atualizar = function() {
			if ($scope.buttonText === 'atualizar') {
				$scope.buttonText = 'pararAtualizacao';
				$scope.listMonitoramentoCiaAereaTableParams.load($scope.filter);
				promise = $interval(refresh_control, $scope.intervalAtualizacao);
			} else {
				$scope.buttonText = 'atualizar';
				$interval.cancel(promise);
				promise = undefined;
			}
		};
		
		$scope.listMonitoramentoCiaAereaTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findMonitoramentoNetworkAereoCiaAerea"
		});
		
		$scope.initializeAbaAcompanhamento = function () {
			
			$scope.controleAbas.exibeAcompanhamento = true;
			$scope.controleAbas.exibeAcompanhamentoAwb = false;
			
			$rootScope.showLoading = true;
			$scope.listMonitoramentoCiaAereaTableParams.load($scope.filter);
			$scope.atualizar();
			$rootScope.showLoading = false;
		};
		
		$scope.openListAwb = function(row) {
			setLocationAcompanhamentoAwb(null, row);
		};
		
		$scope.openListOnlyAwb = function(row) {
			var SOMENTE_AWB = 'AWB';
			setLocationAcompanhamentoAwb({value:SOMENTE_AWB}, row);
		};
		
		$scope.openListAwbEmTransito = function(row) {
			var EM_TRANSITO_AEREO_VALUE = 'EV';
			setLocationAcompanhamentoAwb(findDomainValueByValue(EM_TRANSITO_AEREO_VALUE), row);
		};
		
		$scope.openListAwbAguardandoEmbarque = function(row) {
			var AGUARDANDO_EMBARQUE_VALUE = 'AE';
			setLocationAcompanhamentoAwb(findDomainValueByValue(AGUARDANDO_EMBARQUE_VALUE), row);
		};
		
		$scope.openListAwbAguardandoEntrega = function(row) {
			var AGUARDANDO_ENTREGA_AEROPORTO_VALUE = 'AN';
			setLocationAcompanhamentoAwb(findDomainValueByValue(AGUARDANDO_ENTREGA_AEROPORTO_VALUE), row);
		};
		
		$scope.openListAwbDisponivelRetirada = function(row) {
			var DISPONIVEL_RETIRADA_VALUE = 'DR';
			setLocationAcompanhamentoAwb(findDomainValueByValue(DISPONIVEL_RETIRADA_VALUE), row);
		};
		
		$scope.openListAwbRetirada = function(row) {
			var RETIRADA_AEROPORTO_VALUE = 'RE';
			setLocationAcompanhamentoAwb(findDomainValueByValue(RETIRADA_AEROPORTO_VALUE), row);
		};
		
		$scope.openListLiberacaoFiscal = function(row) {
			var EM_LIBERACAO_FISCAL = 'FS';
			setLocationAcompanhamentoAwb(findDomainValueByValue(EM_LIBERACAO_FISCAL), row);
		};
		
		$scope.initializeAbaAcompanhamento();	
	}
];


