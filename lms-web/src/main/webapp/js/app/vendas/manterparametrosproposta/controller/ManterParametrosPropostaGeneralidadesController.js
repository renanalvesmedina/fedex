var ManterParametrosPropostaGeneralidadesController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"TableFactory",
	function($scope, $rootScope, $stateParams, TableFactory) {
		$scope.generalidadeCliente = {};
		$scope.generalidadeClienteFiltro = {};
		$scope.generalidades = {};
		$scope.generalidadeCliente = {};

		$scope.generalidadeClienteTable = new TableFactory({
			service: $scope.rest.doPost,
			method: "findGeneralidadeClienteTable",
			remotePagination: true
		});

		$scope.findGeneralidadeClienteTable = function(){
			var generalidadeClienteFiltroDTO = {idParametroCliente: $stateParams.id, tabelaPreco: $scope.data.tabelaPreco};
			$scope.generalidadeClienteTable.load(generalidadeClienteFiltroDTO);
		};


		$scope.findCombogeneralidades = function(){
			$rootScope.showLoading = true;
			var generalidadeClienteFiltroDTO = {tabelaPreco: $scope.data.tabelaPreco};

			$scope.rest.doPost("findComboGeneralidades", generalidadeClienteFiltroDTO).then(function(data) {
				$scope.generalidades = data.generalidades;
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.initializeAbaGeneralidades = function(params){
			if ($scope.$parent.parametroPropostaDTO) {
				$scope.data = $scope.$parent.parametroPropostaDTO;
			}
			
			$scope.setDisableBotoesCrud(!!$scope.data.tpGeracaoProposta && "O" == $scope.data.tpGeracaoProposta.value);
			
			$scope.findCombogeneralidades();
			$scope.findGeneralidadeClienteTable();

			$scope.generalidadeCliente.idParametroCliente = $stateParams.id;
		};

		$scope.removeGeneralidadeClienteTableByIds = function(){
			var ids = [];
			$.each($scope.generalidadeClienteTable.selected, function() {
				ids.push(this.idGeneralidadeCliente);
			});

			if (ids.length === 0) {
				$scope.addAlerts([ {msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING } ]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(
					function() {
						$rootScope.showLoading = true;
						$scope.generalidadeClienteFiltro.idSimulacao = $scope.$parent.idSimulacao;
						$scope.generalidadeClienteFiltro.ids = ids;

						$scope.rest.doPost("removeGeneralidadeClienteByIds", $scope.generalidadeClienteFiltro).then(function(data) {
								$rootScope.showLoading = false;
								$scope.showSuccessMessage();
								$scope.findGeneralidadeClienteTable();
						}, function() {
							$rootScope.showLoading = false;
						});
					});
			}
		};

		$scope.detailGeneralidadeCliente = function(row){
			$scope.rest.doPost("findGeneralidadeClienteById",row.id).then(function(data){
				$scope.generalidadeCliente = data;
			});

		};

		$scope.clearFilterGeneralidade = function(){
			$scope.generalidadeCliente = {};
			$scope.generalidadeCliente.idParametroCliente = $stateParams.id;
			$scope.findGeneralidadeClienteTable();
		};

		$scope.storeGeneralidadeCliente = function(){
			$rootScope.showLoading = true;

			$scope.rest.doPost("storeGeneralidadeCliente",$scope.generalidadeCliente).then(function(data){
				$rootScope.showLoading = false;
				$scope.showSuccessMessage();
				$scope.clearFilterGeneralidade();
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.tpIndicadorChange = function(){
			if(angular.isDefined($scope.generalidadeCliente.tpIndicador) && $scope.generalidadeCliente.tpIndicador != null && $scope.generalidadeCliente.tpIndicador.value == "T"){
				$scope.generalidadeCliente.vlGeneralidade = 0;
				$scope.valorGeneralidadeDisabled = true;
			} else {
				$scope.valorGeneralidadeDisabled = false;
			}
		};
		
		$scope.tpIndicadorMinimoChange = function(){
			if(angular.isDefined($scope.generalidadeCliente.tpIndicadorMinimo) && $scope.generalidadeCliente.tpIndicadorMinimo != null && $scope.generalidadeCliente.tpIndicadorMinimo.value == "T"){
				$scope.generalidadeCliente.vlMinimo = 0;
				$scope.psMinimoDisabled = true;
			} else {
				$scope.psMinimoDisabled = false;
			}
		};

		$scope.initializeAbaGeneralidades($stateParams);
	}
];

