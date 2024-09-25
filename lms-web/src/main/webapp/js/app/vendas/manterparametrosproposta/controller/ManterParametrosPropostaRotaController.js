var ManterParametrosPropostaRotaController = [
	"$scope",
	"$stateParams",
	"$rootScope",
	"$location",
	"editTabState",
	function($scope, $stateParams,$rootScope,$location, editTabState) {
		function initializeAbaRota(params){
			$scope.setDisableAbaRota(false);
			$scope.setDisableAbaParam(false);
			$scope.setDisableAbaTaxas(false);
			$scope.setDisableAbaGeneralidades(false);
			$scope.isOpenAccordionOrigem = true;
			$scope.isOpenAccordionDestino = true;

			$scope.setParametroPropostaDTO(null);
			
			$rootScope.currentTab.isChangesAbandoned = true;
			
			if (params.id) {
				
				$rootScope.showLoading = true;
				$scope.findById(params.id).then(function(parametroPropostaDTO) {
					$scope.data = parametroPropostaDTO;
					
					$scope.carregaCabecalho($scope.data);
					
					if ($scope.data.tpGeracaoProposta && $scope.data.tpGeracaoProposta.value == "O"){
						disabledByTpProposta = true;
					}
					
					$scope.setParametroPropostaDTO($scope.data);
					$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});
			} else {
				$scope.setDisableAbaParam(true);
				$scope.setDisableAbaTaxas(true);
				$scope.setDisableAbaGeneralidades(true);
				
				$rootScope.showLoading = true;
				$scope.rest.doPost("loadDadosDefault").then(function(parametroPropostaDTO) {
					var tpGeracaoProposta = $scope.data.tpGeracaoProposta;
					$scope.data = parametroPropostaDTO;
					$scope.data.tpGeracaoProposta = tpGeracaoProposta;
					
					$scope.carregaCabecalho($scope.data);
					
					if ($scope.data.tpGeracaoProposta && $scope.data.tpGeracaoProposta.value == "O"){
						disabledByTpProposta = true;
					}
					
					$scope.setParametroPropostaDTO($scope.data);
					$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});
			}

			carregaZonasCombo();
			carregaTiposLocalizacaoCombo();
		}

		$scope.zonas = [];
		$scope.tiposLocalizacao = [];
		$scope.gruposRegiaoOrigem = [];
		$scope.gruposRegiaoDestino = [];

		var TP_ROTA_ORIGEM = "O";
		var TP_ROTA_DESTINO = "D";

		$scope.CAMPO_ZONA= "Z";
		$scope.CAMPO_PAIS = "P";
		$scope.CAMPO_UF = "U";
		$scope.CAMPO_MUNICIPIO = "M";
		
		$scope.disabledByTpProposta = false;
		
		$scope.rotaDestinoZonaDisabled = false;
		$scope.rotaDestinoPaisDisabled = false;
		$scope.rotaDestinoUfDisabled = false;
		$scope.rotaDestinoMunicipioDisabled = false;
		$scope.rotaDestinoAeroportoDisabled = false;
		$scope.rotaDestinoTipoLocalizacaoDisabled = false;
		$scope.rotaDestinoGrupoRegiaoDisabled = false;


		initializeAbaRota($stateParams);
		
		function carregaZonasCombo() {
			$scope.rest.doPost("findZonasCombo").then(function(zonas) {
				$scope.zonas = zonas;
			});
		}

		function carregaGruposRegiaoCombo(idUnidadeFederativa,tpRota) {
			var idTabelaPreco = $scope.data.tabelaPreco.idTabelaPreco;
			$scope.rest.doPost("findGruposRegiaoCombo",{idUnidadeFederativa:idUnidadeFederativa, idTabelaPreco:idTabelaPreco}).then(function(gruposRegiao) {
				if (!gruposRegiao){
					gruposRegiao = [];
				}

				if (tpRota == TP_ROTA_ORIGEM){
					$scope.gruposRegiaoOrigem  = gruposRegiao;
				}else{
					$scope.gruposRegiaoDestino  = gruposRegiao;
				}
			});
		}

		function carregaTiposLocalizacaoCombo() {
			$scope.rest.doPost("findTiposLocalizacaoCombo").then(function(tiposLocalizacao) {
				$scope.tiposLocalizacao = tiposLocalizacao;
			});
		}



		$scope.limpaCamposRotaOrigem = function(campo){
			if (campo == $scope.CAMPO_ZONA){
				delete $scope.data.rotaOrigemPais;
				$scope.limpaCamposRotaOrigem($scope.CAMPO_PAIS);
			}

			if (campo == $scope.CAMPO_PAIS){
				delete  $scope.data.rotaOrigemUf;
				$scope.limpaCamposRotaOrigem($scope.CAMPO_UF);
			}

			if (campo == $scope.CAMPO_UF){
				delete $scope.data.rotaOrigemMunicipio;
				delete $scope.data.rotaOrigemGrupoRegiao;
			}
		};

		$scope.limpaCamposRotaDestino = function(campo){
			if (campo == $scope.CAMPO_ZONA){
				delete $scope.data.rotaDestinoPais;
				$scope.limpaCamposRotaDestino($scope.CAMPO_PAIS);
			}

			if (campo == $scope.CAMPO_PAIS){
				delete $scope.data.rotaDestinoUf;
				$scope.limpaCamposRotaDestino($scope.CAMPO_UF);
			}

			if (campo == $scope.CAMPO_UF){
				delete $scope.data.rotaDestinoMunicipio;
				delete $scope.data.rotaDestinoGrupoRegiao;
			}
		};

		$scope.continuar = function(){
			
			$scope.setParametroPropostaDTO($scope.data);
			var idParametro = !!$scope.data.id ? $scope.data.id : "";
			$location.path("/app/vendas/manterParametrosProposta/param/"+idParametro);
		};
		
		$scope.changeUFOrigem = function() {
			if ($scope.data.rotaOrigemUf && $scope.data.rotaOrigemUf.idUnidadeFederativa) {
				carregaGruposRegiaoCombo($scope.data.rotaOrigemUf.idUnidadeFederativa,TP_ROTA_ORIGEM);
			} else {
				delete $scope.data.rotaOrigemUf;
				$scope.gruposRegiaoOrigem = [];
			}
			$scope.limpaCamposRotaOrigem($scope.CAMPO_UF);
		};
		

		$scope.changeUFDestino = function() {
			if ($scope.data.rotaDestinoUf && $scope.data.rotaDestinoUf.idUnidadeFederativa) {
				carregaGruposRegiaoCombo($scope.data.rotaDestinoUf.idUnidadeFederativa,TP_ROTA_DESTINO);
			} else {
				delete $scope.data.rotaDestinoUf;
				$scope.gruposRegiaoDestino = [];
			}
			$scope.limpaCamposRotaDestino($scope.CAMPO_UF);
		};
		
		$scope.changeFilialDestino = function(){
			
		};
		
		$scope.changeFilialOrigem = function(){
			
		};

	}


];

