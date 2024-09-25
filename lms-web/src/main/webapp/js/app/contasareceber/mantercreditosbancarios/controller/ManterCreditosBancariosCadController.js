
var ManterCreditosBancariosCadController = [
	"$rootScope",                                         
	"$scope",
	"$stateParams",
	"typeTab",
	"editTabState",
	"modalService",
 	function($rootScope, $scope, $stateParams, typeTab, editTabState, modalService) {
		$scope.initAbas($stateParams, typeTab, editTabState);
		
		$scope.disabilitarEdicaoDigitado = function () {
			if($stateParams.id && (!isTpSituacaoDeposito())){
				return true;
			}
			return false;
		};
		
		$scope.removeByIdParent = $scope.removeById;
		$scope.removeById = function (){
			
			if(!isTpSituacaoDeposito()){
				$scope.showMessage("LMS-36336", MESSAGE_SEVERITY.WARNING);				
			}
			else{
				$scope.removeByIdParent();
			}
			
		};
		
		$scope.liberarCreditoBancarioModal = function() {
			$rootScope.isPopup = true;
			var myController = ["$scope", "$modalInstance", function($scope, $modalInstance) {
				
				var parentScope = getScope();
				
				$scope.title = $rootScope.getMensagem("liberarCreditosBancarios");
				$scope.innerTemplate = contextPath+"js/app/contasareceber/mantercreditosbancarios/view/modalLiberarContent.html";


				$scope.close = function() {
					$modalInstance.dismiss("cancel");
				};
				$scope.data={};
				$scope.data.dataDeCorte = Utils.Date.formatMomentAsISO8601(moment());
				
				$scope.dataDeCorteMax = Utils.Date.formatMomentAsISO8601(moment());

				$scope.liberar = function(){

					var dto = {dataDeCorte:$scope.data.dataDeCorte, idCreditoBancario:parentScope.data.id};
					
					$rootScope.showLoading = true;
					parentScope.rest.doPost("liberarCreditoBancario", dto).then(function(response) {
						$scope.showSuccessMessage();
						$rootScope.showLoading = false;
						$scope.close()
					},function(error) {
						$rootScope.showLoading = false;
					});  
				}
			}];
			modalService.open({controller: myController, windowClass: "liberar-credito-modal"})
			['finally'](function() {
				$rootScope.isPopup = false;
			});
		};
		
		function initializeAbaPesq () {
			if (!$stateParams.id) {
				$scope.data.tpOrigem = {value: 'M'};
				$scope.data.tpSituacao = {value: 'D'};
			}
		};
		initializeAbaPesq();

		function isTpSituacaoDeposito(){
			return $scope.data.tpSituacao && $scope.data.tpSituacao.value === "D";
		}
		
		function getScope() {
			return $scope;
		}
	}
];
