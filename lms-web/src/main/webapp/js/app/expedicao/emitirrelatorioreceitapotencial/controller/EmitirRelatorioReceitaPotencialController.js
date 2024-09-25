
var EmitirRelatorioReceitaPotencialController = [
	"$rootScope",
	"$scope", 
	"domainService", 
 	function($rootScope, $scope, domainService) {
		$scope.setConstructor({
			rest: "/expedicao/relatorioReceitaPotencial"
		});
		
		domainService.findDomain("DM_MODAL").then(function(values) {
			if (!$scope.domains) {
				$scope.domains = [];
			}
			
			$scope.domains.DM_MODAL = values;
		});

		$scope.getIntervaloMax = function() {
			if ($scope.filter.cliente) {
				return;
			} else {
				return 31;
			}
		};

		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
	
			$scope.getFilter = function() {
				if(!$scope.filter){
					return;
				}
				
				return $scope.filter;
			};

			$scope.rest.doPost("carregarValoresPadrao", {}).then(function(data) {
				$scope.filter.regionais = data.regionais;
				$scope.nmFilial = data.nmFilial;
				$scope.idFilial = data.idFilial;
				$scope.filter.idFilialCobranca = {sgFilial:data.sgFilial,nmFilial:data.nmFilial};
				$scope.filter.idRegional= data.idRegional;
				$scope.filter.servicosAdicionais = data.servicosAdicionais;
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		$scope.initializeAbaPesq();

		$scope.getDomainValue = function(domain) {
			if (domain) {
				return domain.value;
			}
			return null;
		};

		$scope.exportCsv = function() {
			$rootScope.showLoading = true;
			$scope.rest.doPost("reportCsv", $scope.getFilter()).then(function(data) {
				$rootScope.showLoading = false;
				if (data.fileName) {
					location.href = contextPath+"rest/report/"+data.fileName;
				}
			},function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.limpar = function() {
			$scope.filter = {};
			$scope.initializeAbaPesq();			
		};
	}
];
