var demonstrativoPagamentoComissaoController = [
	"$rootScope",
	"$scope",
	"$stateParams",	
	"$http",
 	function($rootScope, $scope, $stateParams, $http) {
		
		$scope.setConstructor({
			rest: "/vendas/demonstrativoPagamentoComissao"
		});

		$scope.limpar = function() {
			$scope.filter = {};
			$scope.initializeAbaPesq();
		};

		
		$scope.find = function() {
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("find", $scope.filter).then(function(data) {
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.relatorioDemonstrativoField = function() {
			if ($scope.filter.dtInicio == null || $scope.filter.tpModal == null || ($scope.filter.filial == null && $scope.filter.executivo == null)) {
				$scope.showMessage("LMS-36337", MESSAGE_SEVERITY.WARNING);
				return;
			}
			
			$rootScope.showLoading = true;
			var o = {};
			
			if ($scope.filter.filial != null) {
				o.idFilial = $scope.filter.filial.idFilial; 
			}
			
			if ($scope.filter.tpModal != null) {
				o.tpModal = $scope.filter.tpModal.value;
			}
			
			if ($scope.filter.executivo != null) {
				o.idExecutivo = $scope.filter.executivo.idUsuario;
			}

			o.competencia = $scope.filter.dtInicio;
			
			return $http.post(contextPath+"rest/vendas/demonstrativoPagamentoComissao/relatorioDemonstrativoField", o)
    			.then(function(response) {
    				$rootScope.showLoading = false;
    				location.href = contextPath+"/viewBatchReport?"+response.data.fileName;
    				return response.data;
    			}, function(response){
    				$rootScope.showLoading = false;
    			});
		};

		$scope.relatorioGerentesVendaFilial = function() {
			if ($scope.filter.dtInicio == null || $scope.filter.filial == null) {
				$scope.showMessage("LMS-36337", MESSAGE_SEVERITY.WARNING);
				return;
			}
			
			
			$rootScope.showLoading = true;

			var o = {};
			
			if ($scope.filter.filial != null) {
				o.idFilial = $scope.filter.filial.idFilial; 
			}
			
			if ($scope.filter.tpModal != null) {
				o.tpModal = $scope.filter.tpModal.value;
			}
			
			if ($scope.filter.executivo != null) {
				o.idExecutivo = $scope.filter.executivo.idUsuario;
			}

			o.competencia = $scope.filter.dtInicio;

			return $http.post(contextPath+"rest/vendas/demonstrativoPagamentoComissao/relatorioGerentesVendaFilial", o)
    			.then(function(response) {
    				$rootScope.showLoading = false;
    				location.href = contextPath+"/viewBatchReport?"+response.data.fileName;
    				return response.data;
    			}, function(response){
    				$rootScope.showLoading = false;
    			});
		};


		
		$scope.relatorioGerentesRegionais = function() {
			if ($scope.filter.dtInicio == null || ($scope.filter.filial == null && $scope.filter.executivo == null)) {
				$scope.showMessage("LMS-36337", MESSAGE_SEVERITY.WARNING);
				return;
			}
			
			$rootScope.showLoading = true;

			var o = {};
			
			if ($scope.filter.filial != null) {
				o.idFilial = $scope.filter.filial.idFilial; 
			}
			
			if ($scope.filter.tpModal != null) {
				o.tpModal = $scope.filter.tpModal.value;
			}
			
			if ($scope.filter.executivo != null) {
				o.idExecutivo = $scope.filter.executivo.idUsuario;
			}

			o.competencia = $scope.filter.dtInicio;

			return $http.post(contextPath+"rest/vendas/demonstrativoPagamentoComissao/relatorioGerentesRegionais", o)
    			.then(function(response) {
    				$rootScope.showLoading = false;
    				location.href = contextPath+"/viewBatchReport?"+response.data.fileName;
    				return response.data;
    			}, function(response){
    				$rootScope.showLoading = false;
    			});
		};
		
		
		$scope.relatorioExecutivoInterno = function() {
			if ($scope.filter.dtInicio == null || ($scope.filter.filial == null && $scope.filter.executivo == null)) {
				$scope.showMessage("LMS-36337", MESSAGE_SEVERITY.WARNING);
				return;
			}

			$rootScope.showLoading = true;

			var o = {};
			
			if ($scope.filter.filial != null) {
				o.idFilial = $scope.filter.filial.idFilial; 
			}
			
			if ($scope.filter.tpModal != null) {
				o.tpModal = $scope.filter.tpModal.value;
			}
			
			if ($scope.filter.executivo != null) {
				o.idExecutivo = $scope.filter.executivo.idUsuario;
			}

			o.competencia = $scope.filter.dtInicio;

			return $http.post(contextPath+"rest/vendas/demonstrativoPagamentoComissao/relatorioExecutivoInterno", o)
    			.then(function(response) {
    				$rootScope.showLoading = false;
    				location.href = contextPath+"/viewBatchReport?"+response.data.fileName;
    				return response.data;
    			}, function(response){
    				$rootScope.showLoading = false;
    			});
		};

		$scope.$watch('filter.executivo', function (executivo) {
			$scope.atualizarFilial();
		}, true);

		$scope.atualizarFilial = function() {  
			$scope.rest.doPost("atualizaFilial", $scope.filter).then(function(data) {
				if (data.idFilial != null) {
					$scope.filter.filial = data;	
				} 
			})["finally"](function() {
			});
		};
		
		$scope.initializeFields = function () {
			$rootScope.showLoading = true;

			$scope.rest.doPost("carregarValoresPadrao", {}).then(function(data) {
				$scope.filter.filial = {sgFilial:data.sgFilial,nmFilial:data.nmFilial,idFilial:data.idFilial};

				$scope.filter.executivo = {nmUsuario:data.nmUsuario, login:data.login, nrMatricula:data.nrMatricula};
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		$scope.initializeFields();
		
	}
];
