
var ManterCreditosBancariosController = [
	"$rootScope",                                         
	"$scope", 
	"modalService",
 	function($rootScope, $scope, modalService) {
		$scope.setConstructor({
			rest: "/contasareceber/creditosBancarios"
		});

		$scope.maxIntervalDtCredito = 31;
		
		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
	
			$scope.getFilter = function() {
				if(!$scope.filter){
					return;
				}
				
				return $scope.filter;
			};

			$scope.rest.doPost("carregarValoresPadrao", {}).then(function(data) {
				$scope.nmFilial = data.nmFilial;
				$scope.idFilial = data.idFilial;
				$scope.filter.idFilialCredito = {sgFilial:data.sgFilial,nmFilial:data.nmFilial, id:data.idFilial};
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});

		};

		function getScope() {
			return $scope;
		}
		
		$scope.exportSaldoModal = function() {
			$rootScope.isPopup = true;
			var myController = ["$scope", "$modalInstance", function($scope, $modalInstance) {
				
				var parentScope = getScope();
				$scope.title = $rootScope.getMensagem("exportarSaldoExcel");
				$scope.innerTemplate = contextPath+"js/app/contasareceber/mantercreditosbancarios/view/modalExportSaldoCsv.html";

				$scope.close = function() {
					$modalInstance.dismiss("cancel");
				};
				$scope.data={};
				$scope.data.dataDeCorte = Utils.Date.formatMomentAsISO8601(moment());
				$scope.dataDeCorteMax = Utils.Date.formatMomentAsISO8601(moment());

				$scope.exportarSaldo = function(){

					var dto = {dataDeCorte:$scope.data.dataDeCorte};
					
					$rootScope.showLoading = true;
					parentScope.rest.doPost("liberarCreditoBancario", dto).then(function(response) {
						$scope.showSuccessMessage();
						$rootScope.showLoading = false;
						$scope.close()
					},function(error) {
						$rootScope.showLoading = false;
					});  
				};
				
				$scope.exportSaldoCsv = function() {
					$rootScope.showLoading = true;
					
					var dto = {dataDeCorte:$scope.data.dataDeCorte};
					
					getScope().rest.doPost("reportSaldoCsv", dto).then(function(data) {
						$rootScope.showLoading = false;
						if (data.fileName) {
							location.href = contextPath+"rest/report/"+data.fileName;
						}
					},function() {
						$rootScope.showLoading = false;
					});
				};

			}];
			
			modalService.open({controller: myController, windowClass: "export-Saldo-Csv"})
			['finally'](function() {
				$rootScope.isPopup = false;
			});
		};


		$scope.initializeAbaPesq();
	
	}
];
