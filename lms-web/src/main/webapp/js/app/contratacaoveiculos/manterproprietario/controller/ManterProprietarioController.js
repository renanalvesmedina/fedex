
var ManterProprietarioController = [
	"$scope",
	"ManterEnderecoPessoaFactory",
	"ManterDadosBancariosFactory",
	"ManterContatoPessoaFactory",
	"ManterTelefonePessoaFactory",
	"FilialFactory", 
	"TableFactory",
	"domainService",
	function($scope, ManterEnderecoPessoaFactory, ManterDadosBancariosFactory, ManterContatoPessoaFactory, ManterTelefonePessoaFactory, FilialFactory, TableFactory, domainService) {
		$scope.setConstructor({
			rest: "/contratacaoveiculos/manterProprietario"
		});

		$scope.proprietario = {};
		
		/** Define factory das tabelas da tela */
		$scope.listTableParams = new TableFactory({ service: $scope.rest.doPost, method: "find" });
		$scope.anexosTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findAnexos" });
		$scope.contatoPessoaTableParams = new TableFactory({ service : ManterContatoPessoaFactory.find });
		$scope.telefonePessoaTableParams = new TableFactory({ service : ManterTelefonePessoaFactory.find });
		$scope.contaBancariaTableParams = new TableFactory({ service : ManterDadosBancariosFactory.find });
		$scope.enderecoPessoaTableParams = new TableFactory({ service : ManterEnderecoPessoaFactory.find });
		
		/** Carrega dados do combobox de dias uteis de pagamento semanal */
		$scope.populatePagamentoSemanal = function(){    
			if(!$scope.proprietario.pagamentoSemanal){
				$scope.rest.doGet("populatePagamentoSemanal").then(function(data) {
    				$scope.proprietario.pagamentoSemanal = data;
    			}); 
    		}
		};
			
		$scope.populatePagamentoSemanal();
		
		/** Carrega dados da filial do usuario logado */
		$scope.loadCurrentFilial = function(object){
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				object.filial = {};
    				object.filial = data;
    			}
    			
    			object.isMatriz = data.isMatriz;
			}, function() {
			});
		};
	}
];

