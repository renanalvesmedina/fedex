var ReajusteTabelaClienteAbaDetailController = [
	"$rootScope",
	"$scope", 
	"$http",
	"$stateParams", 
	function($rootScope, $scope, $http, $stateParams) {
		
		if ($stateParams.id) {
			$scope.rest.doGet($stateParams.id).then(function(data) {
				$scope.setData(data);
			},function() { $rootScope.showLoading = false;});
		} else {
			$scope.aux = {};
			$scope.setData({});
			$scope.aux.pessoa = {idPessoa: "cnpj", dsPessoa: "CNPJ"};
		}
		
		$scope.$watch('aux.tabelaNova', function(data){
			if($scope.aux.tabelaNova && $scope.aux.tabelaNova.idTabelaPreco){
				$scope.data.idTabelaNova = $scope.aux.tabelaNova.idTabelaPreco;
				$scope.data.tabelaNova =  $scope.aux.tabelaNova.tabelaPreco;
			}
		});
		
		$scope.changePessoaDetalhe = function(value) {
			$scope.data.nrIdentificacao = null;
			$scope.data.nome = '';
			$scope.listaDivisoes = [];
			$scope.data.tabelaAtual = '';
		};
		
		$scope.changeNrIdentificacao = function(){
			$scope.data.nome = '';
			$scope.listaDivisoes = [];
			$scope.data.tabelaAtual = '';
			
			$scope.rest.doGet("findDivisaoCliente?nrIdentificacao="+$scope.data.nrIdentificacao).then(function(response) {
				$scope.listaDivisoes = response;
			});
			
			$scope.rest.doGet("findNomeFantasiaCliente?nrIdentificacao="+$scope.data.nrIdentificacao).then(function(response) {
				$scope.data.nome = response;
			});
		};
		
		
		$scope.changeDivisaoCliente = function(){
			$scope.data.tabelaAtual = '';
			if($scope.aux.divisaoCliente){
				$scope.rest.doGet("findTabelaBaseCliente?idDivisaoCliente="+$scope.aux.divisaoCliente.id).then(function(response) {
					$scope.data.tabelaAtual = response;
				});
				
				$scope.data.idDivisaoCliente = $scope.aux.divisaoCliente.id;
				$scope.data.idTabDivisaoCliente = $scope.aux.divisaoCliente.idTabDivisaoCliente;
			}
		};
		
		$scope.findPcSugerido = function(){
			if(!$scope.data.id && $scope.data && $scope.data.idTabDivisaoCliente && $scope.data.idTabelaNova){
				$scope.data.percSugerido = '';
				var params = "idTabelaDivisaocliente="+$scope.data.idTabDivisaoCliente+"&idTabelaNova="+$scope.data.idTabelaNova;
				$scope.rest.doGet("findPercentualSugerido?"+params).then(function(response) {
					$scope.data.percSugerido = response;
				});
			}
		};

		$scope.$watch('data.idTabDivisaoCliente', function(data){
			if(!$scope.data.id){
				$scope.findPcSugerido();
			}
		});
		
		$scope.$watch('data.idTabelaNova', function(data){
			if(!$scope.data.id){
				$scope.findPcSugerido();
			}
		});
		
		$scope.efetivar = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("efetivarReajuste", $scope.data).then(function(response) {
				$scope.reload();
				toastr.success("Sucesso");
			}, function() {
  				$rootScope.showLoading = false; 
			})['finally'](function(){
               	$rootScope.showLoading = false;
            });
		};
		
		
		$scope.aprovar = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("aprovarReajuste", $scope.data).then(function(response) {
				$scope.reload();			
			}, function() {
  				$rootScope.showLoading = false; 
			})['finally'](function(){
               	$rootScope.showLoading = false;
            });
		}
		
		$scope.reload = function(){
			$scope.rest.doGet($stateParams.id).then(function(data) {
				$scope.setData(data);
				$rootScope.showLoading = false; 
			},function() { 
				$rootScope.showLoading = false; 
			})['finally'](function(){
               	$rootScope.showLoading = false;
            });			
		};
		
		$scope.efetivarAsync = function(){
			$scope.rest.doGet("efetivarAsyncReajuste").then(function(response) {
				toastr.success("Sucesso");
			});
		};
		
		
	}
];