var PedidoColetaCadController = [
	"$scope", 
	"$stateParams",
	"$rootScope",
	"typeTab",
	"modalService",
	"editTabState",
	"$locale",
	"TableFactory",
	"$filter",
 	function($scope, $stateParams, $rootScope, typeTab, modalService, editTabState, $locale, TableFactory, $filter) {

		$scope.detalheColeta = {};
		$scope.detalheColeta.notaFiscalColetas = {};
		$scope.detalheColeta.nrChaveNfe = {};
		
		$scope.rest.doPost("findMoeda").then(function(response) {
			$scope.moeda = response;
		});
		
		
		$scope.editando = false;
		
		$scope.dataAtual = moment();
		
		if ($stateParams.id) {
			$scope.editando = true;
		}
		
		$scope.testeSubmit = function(form) {
			console.log(form);
			form.submit();
		};
		
		$scope.componentesDisabled = {tpModoPedidoColeta: true, cliente: true, nmPessoa: true, nrNovoDddCliente: true,
				  nrNovoTelefoneCliente: true, sgfilialByIdFilialOrigem: true, nmMunicipio: true,
				  psTotalVerificado: true, qtTotalVolumesVerificado: true, tpStatusColeta: true, horarioCorte: true, tpPedidoColeta: true,
				  sgUnidadeFederativa: true, filial: true, rotaColetaEntrega: true, vlTotalInformado: true, qtTotalVolumesInformado: true, 
				  psTotalInformado: true, psTotalAforadoInformado: true, vlTotalVerificado: true, psTotalAforadoVerificado: true,
				  nmContatoCliente: false, dsInfColeta: false, nrDddCliente: false, nrTelefoneCliente: false, dhColetaDisponivel: false,
				  hrLimiteColeta: false, dtPrevisaoColeta: false, endereco: false, obPedidoColeta: false
				  

		};
		
		$scope.componentesDisabledDetalhe = {
				 servico: false, naturezaProduto:false, tpFrete:false, destino:false, municipio:false, localidadeEspecial:false, filialDestino:true,
				  uf:true, destinatario:false, /*destinatarioNmPessoa:false,*/ qtVolumes:false, moeda:false, vlMercadoria:false, aforado:false, blEntregaDireta:false,
				  psMercadoria:false, psAforado:false, tpStatusAwb:true, awb:true, tpDoctoServico:true, doctoServico:true, nrNotaFiscal:false,notaFiscal:false,nrChave:false,nrChaveNfe:false, obDetalheColeta:false, btnSalvarDetalhe:false, btnLimparDetalhe:false
		};
		$scope.componentesRequiredDetalhe = {
				  municipio:false, localidadeEspecial:false, destinatario:false, destinatarioNmPessoa:false, valor:true, servico:true, naturezaProduto:true, tpFrete:true, destino:true, qtVolumes:true, psMercadoria:true
		};
		
		$scope.componentesRequired = {tpModoPedidoColeta: false, cliente: false, nmContatoCliente: false, tpPedidoColeta: false, nrDddCliente: false, nrTelefoneCliente: false, 
								  dhColetaDisponivel: false, hrLimiteColeta: false, dtPrevisaoColeta: false, endereco: false, nrNovoDddCliente: false, nrNovoTelefoneCliente: false};
		
		$scope.setDisabled = function(obj, valor) {
			$scope.componentesDisabled[obj] = valor;
		};
		
		$scope.setDisabledDetalhe = function(obj, valor) {
			$scope.componentesDisabledDetalhe[obj] = valor;
		};
		
		$scope.setRequired = function(obj, valor) {
			$scope.componentesRequired[obj] = valor;
		};
		$scope.setRequiredDetalhe = function(obj, valor) {
			$scope.componentesRequiredDetalhe[obj] = valor;
		};
		
		$scope.desabilitaHabilitaCamposEditar = function(status) {
			
			if ($scope.status == "AB") {
				$scope.setDisabled("vlTotalInformado", false);
				$scope.setDisabled("qtTotalVolumesInformado", false);
				$scope.setDisabled("psTotalInformado", false);
				$scope.setDisabled("psTotalAforadoInformado", false);
			}
			
			if ($scope.status == "AB" || status == "NM") {
				$scope.setRequired("nmContatoCliente", true);
				$scope.setRequired("nrDddCliente", true);
				$scope.setRequired("nrTelefoneCliente", true);			
				$scope.setRequired("dhColetaDisponivel", true);			
				$scope.setRequired("hrLimiteColeta", true);
				$scope.setRequired("dtPrevisaoColeta", true);			
				$scope.setRequired("endereco", true);
			}
			
			if ($scope.status == "MA" || $scope.status == "TR") {
				$scope.setDisabled("endereco", true);
				$scope.setRequired("nmContatoCliente", true);
				$scope.setRequired("nrDddCliente", true);
				$scope.setRequired("nrTelefoneCliente", true);			
				$scope.setRequired("dhColetaDisponivel", true);			
				$scope.setRequired("hrLimiteColeta", true);
				$scope.setRequired("dtPrevisaoColeta", true);			
			}		
			
			if ($scope.status == "EX" || $scope.status == "AD" || $scope.status == "NT" || $scope.status == "ED") {
				$scope.setDisabled("nmContatoCliente", true);
				$scope.setDisabled("nrDddCliente", true);
				$scope.setDisabled("dsInfColeta", true);
				$scope.setDisabled("nrTelefoneCliente", true);
				$scope.setDisabled("dhColetaDisponivel", true);
				$scope.setDisabled("hrLimiteColeta", true);
				$scope.setDisabled("dtPrevisaoColeta", true);
				$scope.setDisabled("endereco", true);
			}
			
			if ($scope.status == "CA" || $scope.status == "FI") {
				$scope.setDisabled("nmContatoCliente", true);
				$scope.setDisabled("nrDddCliente", true);
				$scope.setDisabled("nrTelefoneCliente", true);
				$scope.setDisabled("dhColetaDisponivel", true);
				$scope.setDisabled("dsInfColeta", true);
				$scope.setDisabled("hrLimiteColeta", true);
				$scope.setDisabled("dtPrevisaoColeta", true);
				$scope.setDisabled("endereco", true);
				$scope.setDisabled("obPedidoColeta", true);
			}

		};
		
		$scope.desabilitaHabilitaCamposCadastrar = function() {
				
			for (var comp in $scope.componentesDisabled) {
				$scope.setDisabled(comp, false);
			}
			
			$scope.setDisabled('nrNovoDddCliente', true);
			$scope.setDisabled('nrNovoTelefoneCliente', true);
			$scope.setDisabled('horarioCorte', true);
			$scope.setDisabled('nmMunicipio', true);
			$scope.setDisabled('sgUnidadeFederativa', true);
			$scope.setDisabled('filial', true);
			$scope.setDisabled('rotaColetaEntrega', true);
			$scope.setDisabled('vlTotalInformado', true);
			$scope.setDisabled('qtTotalVolumesInformado', true);
			$scope.setDisabled('psTotalInformado', true);
			$scope.setDisabled('psTotalAforadoInformado', true);
			$scope.setDisabled('vlTotalVerificado', true);
			$scope.setDisabled('qtTotalVolumesVerificado', true);
			$scope.setDisabled('psTotalVerificado', true);
			$scope.setDisabled('psTotalAforadoVerificado', true);
			$scope.setDisabled('tpStatusColeta', true);
			
			//Required Cadastrar
			$scope.setRequired("tpModoPedidoColeta", true);
			$scope.setRequired("cliente", true);
			$scope.setRequired("nmContatoCliente", true);
			$scope.setRequired("tpPedidoColeta", true);
			$scope.setRequired("dhColetaDisponivel", true);
			$scope.setRequired("hrLimiteColeta", true);
			$scope.setRequired("dtPrevisaoColeta", true);
			$scope.setRequired("endereco", true);
			$scope.setRequired("filial", true);
				
		};
		
		$scope.populateDataByIdCustom = function(id) {
			$rootScope.showLoading = true;
			$scope.findById(id).then(function(data) {
				$scope.setData(data);
				$scope.listTabledetalheColeta.load(data);
				$scope.siglaNumeroColeta = $scope.data.sgFilialColeta + " " + $scope.data.nrColeta;
				$scope.status = $scope.data.tpStatusColeta.value;
				$scope.desabilitaHabilitaCamposEditar();
				initWindow();
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		(function(stateParams) {
			if (stateParams.id) {
				
				$scope.populateDataByIdCustom(stateParams.id);
				
    		} else {
    			data = {tpModoPedidoColeta: {value: 'TE'},
    					tpPedidoColeta: {value: 'NO'},
    					dhColetaDisponivel: Utils.Date.formatDateTime($scope.dataAtual),
    					tpStatusColeta: {value: 'AB'},
    					blClienteLiberadoManual: {value: false},
    					blAlteradoPosProgramacao: {value: false},
    					dtPrevisaoColeta: Utils.Date.formatMomentAsISO8601($scope.dataAtual)};
    			$scope.setData(data);
    			$scope.desabilitaHabilitaCamposCadastrar();
    		}
		})($stateParams);
	
		$scope.listTabledetalheColeta = new TableFactory({
			service : $scope.rest.doPost,
			method : "carregarGridDetalheColeta",
			remotePagination : false
		});
		$scope.listTabledetalheColeta.load({});
		
		

		$scope.liberarBloqueioCreditoColetaModal = function(idCliente) {

			modalService.open({
				controller : LiberarBloqueioCreditoColetaController,
				windowClass : 'my-modal',
				data:{idCliente : idCliente,
					  callback: function(dados) {
						  $scope.retornoLiberarBloqueioModal(dados);
					  }}
			});
		};
		
		$scope.retornoLiberarBloqueioModal = function(dados) {
			$scope.$parent.data.eventoColetaIdUsuario = dados.idUsuario;
			$scope.$parent.data.eventoColetaIdOcorrenciaColeta = dados.idOcorrenciaColeta;
			$scope.$parent.data.eventoColetaDsDescricao = dados.dsDescricao;
			$scope.$parent.data.buttonBloqueioCredito = dados.buttonBloqueioCredito;
		};
		
		$scope.validarBloqueioCreditoColeta = function() {
			$rootScope.showLoading = true;
			$scope.rest.doPost("bloqueioCliente", $scope.$parent.data.cliente.id).then(
					function(response) {
						if (response) {
							$scope.liberarBloqueioCreditoColetaModal($scope.$parent.data.cliente.id);
						}
					}, function() {
						$rootScope.showLoading = false;
					}
			)['finally'](function() {
				$rootScope.showLoading = false;
			});
			
		};
		
		$scope.verificaDataDisponibilidade = function(dados) {
			$rootScope.showLoading = true;
			$scope.rest.doPost("verificaDataDisponibilidade", dados).then(
					function(response) {
						if (response) {
							$scope.data.dtPrevisaoColeta = Utils.Date.formatMomentAsISO8601(Utils.Date.getMoment(response, $locale));
							$scope.setData($scope.data);
						}
					}, function() {
						$rootScope.showLoading = false;
					}
			)['finally'](function() {
				$rootScope.showLoading = false;
			});
			
		};
		
		$scope.visualizaRelatorioPedidoColeta = function() {
			$rootScope.showLoading = true;
			$scope.rest.doPost("visualizaRelatorioPedidoColeta", $scope.data.idPedidoColeta).then(
					function(data) {
						$rootScope.showLoading = false;
						if (data.reportLocator) {
							location.href = contextPath	+ "/viewBatchReport?" + data.reportLocator;
						}
					}, function() {
						$rootScope.showLoading = false;
					}
			)['finally'](function() {
				$rootScope.showLoading = false;
			});
		};
		

		$scope.setaContatoClienteEmSolicitante = function(nmContatoCliente) {
			$scope.data.nmSolicitante = nmContatoCliente;
			$scope.setData($scope.data);
		};
		
		$scope.carregarDadosDetalheColeta = function(detalheColeta){
			
			console.log("d");
			
			$scope.detalheColetaRef= detalheColeta;
			var result = angular.copy(detalheColeta);
			
			if(result.municipioFilial!=null){
				
				result.destino = {value:"M"}
				$scope.setDisabledDetalhe("municipio", false);
				
				$scope.setRequiredDetalhe("municipio", true);
				$scope.setDisabledDetalhe("localidadeEspecial", true);
				$scope.setRequiredDetalhe("localidadeEspecial", false);
			}
			if(result.localidadeEspecial!=null){
				result.destino = {
					    "id": null,
					    "value": "L",
					    "description": {
					      "updating": false,
					      "value": null
					    },
					    "status": true,
					    "domain": null,
					    "order": null,
					    "descriptionAsString": null
					  };
				$scope.setDisabledDetalhe("municipio", true);
				
				$scope.setRequiredDetalhe("municipio", false);
				$scope.setDisabledDetalhe("localidadeEspecial", false);
				$scope.setRequiredDetalhe("localidadeEspecial", true);
			}

			$scope.setDisabledDetalhe("btnSalvarDetalhe", false);
			$scope.setDisabledDetalhe("nrChave", false);
			$scope.setDisabledDetalhe("nrChaveNfe", false);
			desabilitaCamposByStatusColeta();
			
			if(result.psAforado != null && result.psAforado != "") {
				result.aforado= true;
			} else {
				result.aforado = false;
			}	
			
			$scope.detalheColeta = result;
			
		};		
		
		//Manter Pedido coleta detalhe coleta
		
		function desabilitaCamposByStatusColeta(){
			
			
			if($scope.data.tpPedidoColeta.value == "AE"){
				$scope.setDisabledDetalhe("blEntregaDireta", false);
				$scope.setDisabledDetalhe("btnSalvarDetalhe", false);
				$scope.setDisabledDetalhe("municipio", true);
				if($scope.status == "CA"){
					$scope.setDisabledDetalhe("btnSalvarDetalhe", true);
				}
			}else{
				$scope.setDisabledDetalhe("blEntregaDireta", true);
				if($scope.status == "EX" || $scope.status == "AD" || $scope.status == "NT" || $scope.status == "ED") {
					$scope.setDisabledDetalhe("servico", true);
					$scope.setDisabledDetalhe("municipio", true);
					
					$scope.setDisabledDetalhe("localidadeEspecial", true);			
					$scope.setDisabledDetalhe("vlMercadoria", false);
					$scope.setDisabledDetalhe("qtVolumes", false);
					$scope.setDisabledDetalhe("psMercadoria", false);
					$scope.setDisabledDetalhe("aforado", false);	
					$scope.setDisabledDetalhe("psAforado", false);
					$scope.setDisabledDetalhe("obDetalheColeta", false);	
				} 
				
				if($scope.status == "CA" || $scope.status == "FI") { 
					$scope.setDisabledDetalhe("servico", true);
					$scope.setDisabledDetalhe("municipio", true);
					
					$scope.setDisabledDetalhe("localidadeEspecial", true);
					$scope.setDisabledDetalhe("btnSalvarDetalhe", true);
					$scope.setDisabledDetalhe("aforado", true);
					$scope.setDisabledDetalhe("psAforado", true);		
				}
				
				if($scope.status != "EX" && $scope.status != "AD" && $scope.status != "NT" && 
				   $scope.status != "ED" && $scope.status != "CA" && $scope.status != "FI" ) {
					$scope.setDisabledDetalhe("servico", false);
					$scope.setDisabledDetalhe("naturezaProduto", false);
					$scope.setDisabledDetalhe("tpFrete", false);
					$scope.setDisabledDetalhe("destino", false);
					$scope.setDisabledDetalhe("destinatario", false);

					$scope.setDisabledDetalhe("moeda", false);
					$scope.setDisabledDetalhe("vlMercadoria", false);
					$scope.setDisabledDetalhe("qtVolumes", false);
					$scope.setDisabledDetalhe("psMercadoria", false);
					$scope.setDisabledDetalhe("aforado", false);		
					$scope.setDisabledDetalhe("psAforado", false);		
					$scope.setDisabledDetalhe("nrNotaFiscal", false);
					$scope.setDisabledDetalhe("notaFiscal", false);
					$scope.setDisabledDetalhe("obDetalheColeta", false);			
				}	
			}
		}
		
		function carregaDados() {
					
			if($scope.detalheColeta.idServico==null) {			
				$scope.setDisabledDetalhe("naturezaProduto", true);
				$scope.setDisabledDetalhe("tpFrete", true);
				$scope.setDisabledDetalhe("destino", true);
				$scope.setDisabledDetalhe("municipio", true);
				$scope.setDisabledDetalhe("localidadeEspecial", true);
				$scope.setDisabledDetalhe("destinatario", true);
				//setDisabledDetalhe("nmDestinatario", true);
				$scope.setDisabledDetalhe("moeda", true);
				$scope.setDisabledDetalhe("vlMercadoria", true);
				$scope.setDisabledDetalhe("qtVolumes", true);
				$scope.setDisabledDetalhe("psMercadoria", true);
				$scope.setDisabledDetalhe("aforado", true);
				$scope.setDisabledDetalhe("psAforado", true);
				$scope.setDisabledDetalhe("nrNotaFiscal", true);
				$scope.setDisabledDetalhe("notaFiscal", true);			
				$scope.setDisabledDetalhe("obDetalheColeta", true);
				$scope.setDisabledDetalhe("blEntregaDireta", true);
			}
			
			$scope.setRequiredDetalhe("municipio", true);
			
			if($scope.data.tpPedidoColeta.value == "DE") {
				$scope.setRequiredDetalhe("destinatario",true);
			}	
			
			$scope.setRequiredDetalhe("moeda",true);

		}
		
		function initWindow() {
			$scope.setDisabledDetalhe("btnLimparDetalhe", false);		
			
			$scope.setDisabledDetalhe("nrChaveNfe", true);
			$scope.setDisabledDetalhe("nrChave", true);
			desabilitaCamposByStatusColeta();
			
			if($scope.data.idPedidoColeta != null && $scope.detalheColeta.id == null) {
				$scope.setDisabledDetalhe("btnSalvarDetalhe", true);
			} else {
				$scope.setDisabledDetalhe("btnSalvarDetalhe", false);		
			}
			if ($scope.detalheColeta.id == null) {
				$scope.setDisabledDetalhe("servico", true);
			}
			carregaDados();
		}
		
		/**
		 * Habilita campos caso o serviço seja informado
		 */
		$scope.habilitaCampos = function() {
		
			if($scope.detalheColeta.servico != undefined) {
				$scope.setDisabledDetalhe("naturezaProduto", false);
				$scope.setDisabledDetalhe("tpFrete", false);
				$scope.setDisabledDetalhe("destino", false);
				$scope.setDisabledDetalhe("municipio", false);
				$scope.setDisabledDetalhe("localidadeEspecial", true);
				$scope.setDisabledDetalhe("destinatario", false);
				$scope.setDisabledDetalhe("moeda", false);
				$scope.setDisabledDetalhe("vlMercadoria", false);
				$scope.setDisabledDetalhe("qtVolumes", false);
				$scope.setDisabledDetalhe("psMercadoria", false);
				$scope.setDisabledDetalhe("aforado", false);		
				$scope.setDisabledDetalhe("psAforado", false);		
				$scope.setDisabledDetalhe("nrNotaFiscal", false);
				$scope.setDisabledDetalhe("notaFiscal", false);
				$scope.setDisabledDetalhe("obDetalheColeta", false);
			} else {
				$scope.setDisabledDetalhe("naturezaProduto", true);
				$scope.setDisabledDetalhe("tpFrete", true);
				$scope.setDisabledDetalhe("destino", true);
				$scope.setDisabledDetalhe("municipio", true);
				$scope.setDisabledDetalhe("localidadeEspecial.", true);
				$scope.setDisabledDetalhe("destinatario", true);
				$scope.setDisabledDetalhe("moeda", true);
				$scope.setDisabledDetalhe("vlMercadoria", true);
				$scope.setDisabledDetalhe("qtVolumes", true);
				$scope.setDisabledDetalhe("psMercadoria", true);
				$scope.setDisabledDetalhe("aforado", true);		
				$scope.setDisabledDetalhe("psAforado", true);		
				$scope.setDisabledDetalhe("nrNotaFiscal", true);
				$scope.setDisabledDetalhe("notaFiscal", true);
				$scope.setDisabledDetalhe("obDetalheColeta", true);
			}	
		};
		
		/**
		 * Verifica se o tipo de Frete é FOB e se o tipo cliente não for um cliente especial.
		 */
		$scope.verificaObrigatoriedadeDestinatario = function() {		
			var tipoFrete = $scope.detalheColeta.tpFrete.value;
			var tipoCliente = $scope.data.cliente.tpCliente;
			
			if(tipoFrete == "F" && (tipoCliente != "" && tipoCliente != "S")) {			
				$scope.setRequiredDetalhe("destinatario",true);				
			} else {				
				$scope.setRequiredDetalhe("destinatario",false);				
			}
		};
		
		/**
		 * Habilita lookup de municipio ou localidade especial de acordo com o escolhido na combobox.
		 */
		$scope.habilitaMunicipioOuLocalidade = function() {
			
			console.log($scope.detalheColeta.destino.value );
			
			if($scope.detalheColeta.destino.value == "M"){
				$scope.detalheColeta.localidadeEspecial = null;
				$scope.detalheColeta.municipioFilial = null;
				$scope.setDisabledDetalhe("municipio", false);
				$scope.setRequiredDetalhe("municipio", true);
				$scope.setDisabledDetalhe("localidadeEspecial", true);
				$scope.setRequiredDetalhe("localidadeEspecial", false);
			}else if($scope.detalheColeta.destino.value == "L"){
				$scope.detalheColeta.localidadeEspecial = null;
				$scope.detalheColeta.municipioFilial = null;
				$scope.setDisabledDetalhe("municipio", true);
				$scope.setRequiredDetalhe("municipio", false);
				$scope.setDisabledDetalhe("localidadeEspecial", false);
				$scope.setRequiredDetalhe("localidadeEspecial", true);
			}
					
		};
		
	
		
		$scope.desmarcaAforado = function() {
			$scope.detalheColeta.psAforado = null;
			$scope.detalheColeta.aforado = false;
			
		};
		
		$scope.limparDetalhe = function(){
			$scope.detalheColeta = {};
			$scope.nrNotaFiscal = {};
			$scope.nrChave = {};
			initWindow();
		};
		
		 $scope.validateChaveNfe = function(item) {
			 if (item) {
				var chaveNfe = item.nrChave;
				
				if(chaveNfe.length >= 44){
					if(!$scope.validateDigitoVerificadorNfe(chaveNfe)){
						return false;
					}
				}else{
					$scope.addAlerts([{msg: $scope.getMensagem("LMS-04400"), type: MESSAGE_SEVERITY.WARNING}]);
					return false;
				}
			 }
			return true;
		};
		
		 /**
	  	* Eventos de modificação de conteúdo do listbox
	  	*/
	  	$scope.contentChange = function(item){
			return $scope.removeNF(item);
		};
		
	  	/**
	  	* Remove a Nota Fiscal de acordo com a chave NFe removida
	  	*/
	  	$scope.removeNF = function(item){
	  		if (item) {
		  		var elementDisabled = $scope.componentesDisabledDetalhe["notaFiscal"];
		  		var notasFiscais = $scope.detalheColeta.notaFiscalColetas.untouched.concat($scope.detalheColeta.notaFiscalColetas.added.concat($scope.detalheColeta.notaFiscalColetas.updated));
		  		
		  		var notaFiscalColetasCopia = angular.copy($scope.detalheColeta.notaFiscalColetas);
		  		notaFiscalColetasCopia.untouched = $scope.removeNFInList(angular.copy($scope.detalheColeta.notaFiscalColetas.untouched), item);
		  		notaFiscalColetasCopia.added = $scope.removeNFInList(angular.copy($scope.detalheColeta.notaFiscalColetas.added), item);
		  		notaFiscalColetasCopia.updated = $scope.removeNFInList(angular.copy($scope.detalheColeta.notaFiscalColetas.updated), item);
		  		
		  		$scope.detalheColeta.notaFiscalColetas = notaFiscalColetasCopia;
				
				$scope.setDisabledDetalhe("notaFiscal", elementDisabled);
				$scope.setDisabledDetalhe("nrNotaFiscal", elementDisabled);
	  		}
			return true;
	  	};
	  	
	  	$scope.removeNFInList = function(notasFiscais, item) {
			var nfToCompare = parseInt(item.nrChave.substring(25, 34) , 10);
			
			for( var i = 0 ; i < notasFiscais.length ; i++ ){
				if(parseInt(notasFiscais[i].nrNotaFiscal,10) == nfToCompare){
					notasFiscais.splice(i, 1);
				}
			}
			
			return notasFiscais;
	  	};
	  	
		/**
		 * Valida o digito verificador da Chave Nfe
		 */
		$scope.validateDigitoVerificadorNfe = function(chaveNfe){
			var dvChaveNfe = chaveNfe.substring(chaveNfe.length - 1, chaveNfe.length);
			var chave = chaveNfe.substring(0, chaveNfe.length - 1);
			var calculoChaveNfe = $scope.modulo11(chave);
			
			if(dvChaveNfe == (calculoChaveNfe)){
				return true;
			}else{
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-04400"), type: MESSAGE_SEVERITY.WARNING}]);
				return false;
			}
		};
		
		$scope.modulo11 = function(chave){
			var n = new Array();
			var peso = 2;
			var soma = 0;

			n = chave.split('');
			
			for (var i = n.length-1; i >= 0; i--) {
				var value = n[i];
				soma = soma + value * peso++;
				if(peso == 10){
					peso = 2;
				}
			}
			
			var mod = soma % 11;
			var dv;
			
			if(mod == 0 || mod == 1){
				dv = 0;
			} else {
				dv = 11 - mod;
			}
			
			return dv;
		};
		
		$scope.setaValorNotaFiscal = function(item){
			if (item) {
				var chaveNfe = item.nrChave;
				
				var notasFiscais = $scope.detalheColeta.notaFiscalColetas.untouched.concat($scope.detalheColeta.notaFiscalColetas.added.concat($scope.detalheColeta.notaFiscalColetas.updated));
				var elementDisabled = $scope.componentesDisabledDetalhe["notaFiscal"];
				
				var nrNf = chaveNfe.substring(25, 34);
				
				var achouNF = false;
				for( var i = 0 ; i < notasFiscais.length ; i++ ){
					if(parseInt(notasFiscais[i].nrNotaFiscal,10) == parseInt(nrNf, 10)){
						achouNF = true;
					}
				}
				
				if (!achouNF) {
					var notaFiscalColetasCopia = angular.copy($scope.detalheColeta.notaFiscalColetas);
					notaFiscalColetasCopia.added.push({"nrNotaFiscal": parseInt(nrNf,10)});
					$scope.detalheColeta.notaFiscalColetas = notaFiscalColetasCopia;
				}
				
				$scope.setDisabledDetalhe("notaFiscal", elementDisabled);
				$scope.setDisabledDetalhe("nrNotaFiscal", elementDisabled);
			}
			
		};
		
		$scope.salvarDetalhe = function(row){
			
			for (var variavel in $scope.componentesRequiredDetalhe) {
				$scope.setRequiredDetalhe($scope.componentesRequiredDetalhe[variavel], false);
			}
			
			
			 
			 angular.forEach($scope.detalheColeta, function(value, key) {
				 if (key == "tpFrete") {
					 if (value!=null) {
						 row["tpFreteDescription"] = value.description.value;
					 }
				 } else if (key == "doctoServico") {
					 if (value!=null) {
						 row["tpDoctoSgFilial"] = value.tpDoctoServico;
					 }			 
				 }else if(key == "notaFiscalColetas"){
					 var notasFiscais = value.untouched.concat(value.added.concat(value.updated));
					 row[key]=[];
					 angular.forEach(notasFiscais, function(valor, chave){
						 var nota = {};
						 if(valor.idNotaFiscalColeta!=null){
							 nota.idNotaFiscalColeta = valor.idNotaFiscalColeta
						 }
						 if(valor.nrChave!=null){
							 nota.nrChave = valor.nrChave;
						 }
						 nota.nrNotaFiscal = valor.nrNotaFiscal;
							 row[key] = row[key].concat(nota);
						
					 });
					 
				 }else if(key == "nrChaveNfe"){
					 var chaves = value.untouched.concat(value.added.concat(value.updated));
					 angular.forEach(chaves, function(valor, chave){
						 var chaveNF = [];
						 if(valor.idNotaFiscalColeta!=null){
							 chaveNF.idNotaFiscalColeta = valor.idNotaFiscalColeta
						 }
						 if(valor.nrNotaFiscal!=null){
							 chaveNF.nrNotaFiscal = valor.nrNotaFiscal;
						 }
						 chaveNF.nrChave = valor.nrChave;
						
						row[key] = row[key].concat(chaveNF);
						 
						
					 });
				 }else{
					 row[key] = value;
				 }

				 
			 });
			 
			 console.log($scope.detalheColeta.municipioFilial);
			 $scope.saveDetalheColetaValidateNF(row);
		};
		
		$scope.saveDetalheColetaValidateNF = function(row){
			$scope.rest.doPost("saveDetalheColetaValidateNF",row).then(function(response) {
				$scope.detalheColeta = response;
				$scope.limparDetalhe(); 
				$scope.limparDetalhe(); 
			});
			console.log(row);
			console.log($scope.detalheColetaRef);
			console.log($scope.data);
		};
		
		$scope.customStore = function(){
			
			$scope.data.detalheColetasDTO = $scope.listTabledetalheColeta.list;
			$scope.store();
		};

		
	}
];
