var ManterParametrosPropostaTaxasController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"TableFactory",
	function($scope, $rootScope, $stateParams, TableFactory) {
		$scope.taxaClienteFiltro = {};
		$scope.taxas = {};

		$scope.taxaClienteTable = new TableFactory({
			service: $scope.rest.doPost,
			method: "findTaxaClienteTable",
			remotePagination: true
		});

		$scope.findTaxaClienteTable = function(){
			var taxaClienteFiltroDTO = {idParametroCliente: $scope.data.id, tabelaPreco: $scope.data.tabelaPreco};
			$scope.taxaClienteTable.load(taxaClienteFiltroDTO);
		};

		$scope.findComboTaxas = function(){
			$rootScope.showLoading = true;
			var taxaClienteFiltroDTO = {tabelaPreco: $scope.data.tabelaPreco};

			$scope.rest.doPost("findComboTaxas", taxaClienteFiltroDTO).then(function(data) {
				$scope.taxas = data.taxas;
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.initializeAbaTaxas = function(params){
			$scope.taxaCliente = {};
			if ($scope.$parent.parametroPropostaDTO) {
				$scope.data = $scope.$parent.parametroPropostaDTO;
				$scope.findComboTaxas();
				$scope.findTaxaClienteTable();
			}
			
			$scope.setDisableBotoesCrud(!!$scope.data.tpGeracaoProposta && "O" == $scope.data.tpGeracaoProposta.value);

			$scope.inicializarCampos();
		};

		$scope.inicializarCampos = function(){
			$scope.vlValorDisabled = true;
			$scope.psMinimoDisabled = true;
			$scope.vlExcedenteDisabled = true;
			$scope.taxaCliente.tpIndicador = {};
			$scope.taxaCliente.tpIndicador.value = "T";
			$scope.taxaCliente.vlValor = 0;
			$scope.taxaCliente.psMinimo = 0;
			$scope.taxaCliente.vlExcedente = 0;
		};

		$scope.clearFilterTaxaCliente = function(){
			$scope.initializeAbaTaxas($stateParams);
			$("#taxaClienteForm").removeClass("submitted");
		};

		$scope.tpIndicadorChange = function(){
			if(angular.isDefined($scope.taxaCliente.tpIndicador) && $scope.taxaCliente.tpIndicador != null && $scope.taxaCliente.tpIndicador.value == "T"){
				$scope.inicializarCampos();
			} else {
				$scope.vlValorDisabled = false;
				$scope.psMinimoDisabled = false;
				$scope.vlExcedenteDisabled = false;
			}
		};

		$scope.vlValorValidate = function(modelValue, viewValue){
			var retorno = {isValid: true, messageKey: 'LMS-01075'};

			if(angular.isDefined($scope.taxaCliente.tpIndicador) && angular.isDefined(viewValue)){
				var tpIndicador = $scope.taxaCliente.tpIndicador.value;

				if( ((tpIndicador == "A" || tpIndicador == "V") && parseInt(viewValue, 10) < 0)
						|| (tpIndicador == "D" && (parseInt(viewValue, 10) < 0 ||  parseInt(viewValue, 10) > 100))){
					retorno.isValid = false;
				}
			}

			return retorno;
		};

		$scope.psMinimoValidate = function(modelValue, viewValue){
			var retorno = {isValid: true, messageKey: 'LMS-01076'};
			if(angular.isDefined(viewValue) && parseInt(viewValue, 10) < 0){
				retorno.isValid = false;
			}
			return retorno;
		};

		$scope.vlExcedenteValidate = function(modelValue, viewValue){
			var retorno = {isValid: true, messageKey: 'LMS-01077'};
			if(angular.isDefined(viewValue) && parseInt(viewValue, 10) < 0){
				retorno.isValid = false;
			}
			return retorno;
		};

		$scope.removeTaxaClienteTableByIds = function(){
			var ids = [];
			$.each($scope.taxaClienteTable.selected, function() {
				ids.push(this.idTaxaCliente);
			});

			if (ids.length === 0) {
				$scope.addAlerts([ {msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING } ]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(
					function() {
						$rootScope.showLoading = true;
						$scope.taxaClienteFiltro.idSimulacao = $scope.simulacao.id;
						$scope.taxaClienteFiltro.ids = ids;

						$scope.rest.doPost("removeTaxaClienteByIds", $scope.taxaClienteFiltro).then(function(data) {
								$rootScope.showLoading = false;
								$scope.showSuccessMessage();
								$scope.findTaxaClienteTable();
						}, function() {
							$rootScope.showLoading = false;
						});
					});
			}
		};

		$scope.detailTaxaCliente = function(row){
			$rootScope.showLoading = true;
			$scope.rest.doPost("findTaxaClienteById?idTaxaCliente=" + row.id).then(function(taxaClienteDTO) {
				$scope.taxaCliente = taxaClienteDTO;
				$scope.tpIndicadorChange();
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.storeTaxaCliente = function(){
			$rootScope.showLoading = true;

			$scope.taxaCliente.idSimulacao =  $scope.simulacao.id;
			$scope.taxaCliente.idParametroCliente =  $scope.data.id;

			$scope.rest.doPost("storeTaxaCliente", $scope.taxaCliente).then(function(data) {
				$rootScope.showLoading = false;
				$scope.showSuccessMessage();
				$scope.clearFilterTaxaCliente();
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.initializeAbaTaxas($stateParams);
	}
];

