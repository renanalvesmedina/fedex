
var ReajusteTabelaDePrecoDetailController = [
    "$rootScope" ,
    "$http" , 
	"$scope" ,
	"translateService",
	"$stateParams", 
	function($rootScope, $http, $scope, translateService, $stateParams) {
		

    	if ($stateParams.id) {
			$scope.rest.doGet($stateParams.id).then(function(data) {
				$scope.setData(data);
			},function() {
				$rootScope.showLoading = false;
			});
			
		} else {
			$scope.data = {};
			$scope.data.selection = []; 
			$scope.setData($scope.data);
		}
		
		$scope.executarReajuste = function() {
			$rootScope.showLoading = true; 
			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/efetivar', $scope.data).then(function(response){
				atualizarTelaEfetivacao(response.data.dtEfetivacao);
				toastr.success($scope.getMensagem("LMS-00054"));
				$rootScope.showLoading = false; 
			}, function() {
				$rootScope.showLoading = false; 
			}); 
		};
    		
		function atualizarTelaEfetivacao(dtEfetivacao){
			$scope.data.dtEfetivacao = dtEfetivacao;
			$scope.data.efetivado = true;
		}

		$scope.loadMensagens = function(chaves) {
    		translateService.getMensagens(chaves);
    	};
    	
    	$scope.loadMensagens("LMS-00054");
    	
    	$scope.getMensagem = function(chave, params) {
    		return translateService.getMensagem(chave, params);
    	};
		
		$scope.$watch('data.tabelaBase', function(data){
			if(data && data.idTabelaPreco){
				$scope.data.listParcelas = [];
				$scope.data.idTabelaBase = data.idTabelaPreco;
				getAllParcelas(data.idTabelaPreco);
				getDescricaoTabelaPreco(data.idTabelaPreco)
			}
		});
				
    	$scope.toggleCheckboxParcela = function(item) {
		    var idx = $scope.data.selection.indexOf(item);
		    if (idx > -1) {
		      $scope.data.selection.splice(idx, 1);
		      $('#checkedAll').prop('checked', false);
		    } else {
		      $scope.data.selection.push(item);
		    }
		};
		  
		$scope.isCheckboxSelected = function(item){
		    return $scope.data.selection.indexOf(item.id) > -1;
    	};
		  
		$scope.selectAllParcelas = function(){
		    $scope.data.selection = [];
		    
		    if($('#checkedAll').is(':checked')) {
		      angular.forEach($scope.data.listParcelas, function (value) {
		           $scope.data.selection.push(value.id);
		      });
		    }
		};		
		
		function getAllParcelas(idTabelaBase){
    		$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/findParcelas',  idTabelaBase).then(function(response){
  				$scope.data.listParcelas =  response.data;
  			}); 
		}

		function getDescricaoTabelaPreco(idTabelaBase){
			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/findDescricaoTabelaPreco',  idTabelaBase).then(function(response){
				if(response.data !=null && response.data.descricao != null) {
					$scope.data.descricao = response.data.descricao;
				}
			});
		}
		
    }
];