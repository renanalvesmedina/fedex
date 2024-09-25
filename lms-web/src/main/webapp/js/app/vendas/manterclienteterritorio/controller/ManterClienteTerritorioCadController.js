var ManterClienteTerritorioCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"typeTab",
	"editTabState",
	"TableFactory",
	"$filter",
	function($rootScope, $scope, $stateParams, typeTab, editTabState, TableFactory, $filter) {
		$scope.initAbas($stateParams, typeTab, editTabState);

		$scope.listDetailTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "find"
		});

		$scope.salvar = function () {
			$rootScope.showLoading = true;
			$scope.rest.doPost("", $scope.data).then(function(response) {
				$rootScope.showLoading = false;
				$scope.atualizarTable();
				$scope.clearData();
				$scope.showSuccessMessage();
			},function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.atualizarTable = function () {
			if ($scope.data.territorio && $scope.data.territorio.id) {
				$rootScope.showLoading = true;

				$scope.listDetailTableParams.load({territorio:$scope.data.territorio}).then(function() {
					$rootScope.showLoading = false;
				},function() {
					$rootScope.showLoading = false;
				});
			} else {
				$scope.listDetailTableParams.clear();
			}
		};
		
		$scope.updateComissaoConquista = function($event) {
	        var checkbox = $event.target;
	        if (!checkbox.checked) {
				$scope.data.dtComissaoConquistaInicio = '';
				$scope.data.dtComissaoConquistaFim = '';
	        };
	    };

		$scope.$watch('data.territorio', function(data) {
			if(data){
				$scope.rest.doGet("findDadosTerritorio?id="+data.id).then(function(data) {
					
					$scope.data.filialNegociacao = {
						  idFilial: data.idFilial,
						  sgFilial: data.sgFilial, 
						  nmFilial: data.nmFilial
					};
					
					$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});
			} else {
				$scope.data.filialNegociacao = {
						idFilial : null,
						sgFilial : '   ',
						nmFilial : ' '
				};
			}
			
			$scope.atualizarTable();
			
		});

		$scope.$watch('data.cliente', function(data) {
			if (data) {
				$scope.rest.doGet("findDadosCliente?id=" + data.id).then(
						function(data) {
							$scope.data.tpCliente = data.tpCliente;
							$scope.data.tpSituacao = data.tpSituacao;

							$scope.data.filialResponsavel = {
								idFilial : data.idFilial,
								sgFilial : data.sgFilial,
								nmFilial : data.nmFilial
							};

							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});
			} else {
				$scope.data.tpCliente = null;
				$scope.data.tpSituacao = null;
				
				$scope.data.filialResponsavel = {
					idFilial : null,
					sgFilial : '   ',
					nmFilial : ''
				};
			}
		});	
		
		$scope.findDtInicioMesTNT = function() {
			$rootScope.showLoading = true;
			$scope.rest.doGet("findDtInicioMesTNT").then(function(response) {
				$scope.dadosPerfilAnalistaComissao = response;
				if ($scope.data.dtInicio == null) {
					$scope.data.dtInicio = $scope.dadosPerfilAnalistaComissao.dtInicio;
				}
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.findDtInicioMesTNT();
		
		$scope.atualizarTable();
	}
];
