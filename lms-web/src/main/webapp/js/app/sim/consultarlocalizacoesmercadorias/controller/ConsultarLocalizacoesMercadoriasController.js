   															
var ConsultarLocalizacoesMercadoriasController = [
	"$scope", 
	"$location", 
	"ConsultarLocalizacaoMercadoriaFactory", 
	"domainService", 
	"TableFactory",
	function($scope, $location, ConsultarLocalizacaoMercadoriaFactory, domainService, TableFactory) {
		
		$scope.setConstructor({
			rest: "/sim/consultarLocalizacaoMercadoria"
		});

    		domainService.findDomain("DM_MODAL").then(function(values) {
    			if (!$scope.domains) {
    				$scope.domains = [];
    			}
    			
    			$scope.domains.DM_MODAL = values;
    		});
    		
    		$scope.loadMensagens("LMS-10047");
    		
    		$scope.filtro = {};
    		$scope.filtro.filtros = {};
    		$scope.tmp = {};
    		
    		$scope.setDataPadrao = function() {
    			var dateMoment = moment();
    			$scope.tmp.periodoFinal = Utils.Date.formatMomentAsISO8601(dateMoment);
	    		$scope.tmp.periodoInicial = Utils.Date.formatMomentAsISO8601(dateMoment.subtract(1, 'days'));
    		};
    		
    		$scope.setDataPadrao();

    		$scope.consultou = false;
    		
    		$scope.findClienteCCTByUsuario = function() {
    			$scope.showTabLoading = true;
				ConsultarLocalizacaoMercadoriaFactory.findClienteCCTByUsuario().then(function(data) {
					$scope.showTabLoading = false;
					if(data.nrIdentificacaoRemetente){
						$scope.isFiltroRemetenteOpen = true;
						$scope.tmp.remetente=data;
					}
				}, function() {
					$scope.showTabLoading = false;
				});
    		};
    		$scope.findClienteCCTByUsuario();
    		
    		$scope.maskNumeroDocumento = /./;
    		
    		$scope.list = [];

    		/* Aba Listagem - Table */
    		$scope.listTableParams = new TableFactory(
				{
					service: ConsultarLocalizacaoMercadoriaFactory.findDocumentos
				}
			);    		
    		
    		$scope.consultar = function() {
    			$scope.loadFiltros();
    			if($scope.validateForm()){
    				$scope.consultando = true;
    				$scope.listTableParams.load($scope.filtro).then(function(data) {
    					$scope.consultando = false;
    	    			if (data.length > 1) {
    						$location.path("/app/consultarLocalizacaoMercadoria/listagem");
    					} else if (data.length == 1) {
    						var row = data[0];
    						$location.path("/app/consultarLocalizacaoMercadoria/cad/" + row.idDoctoServico);
    					} else {
    						var msg = $scope.getMensagem("grid.paginacao.nenhum-registro");
    						msg = msg.replace("<BR>", "");
    						$scope.addAlerts([{msg: msg}]);
    					}
    				}, function() {
    					$scope.consultando = false;
    				});
    			} else {
    				$scope.addAlerts([{msg: $scope.getMensagem("LMS-10047")}]);
    			}
    		};
    		
    		$scope.exportarExcel = function() {
    			$scope.loadFiltros(true);
    			if($scope.validateForm()) {
    				$scope.exportando = true;
    				ConsultarLocalizacaoMercadoriaFactory.findDocumentos($scope.filtro).then(function(data) {
    					$scope.exportando = false;
    					location.href = contextPath+"rest/report/"+data.fileName;
    				}, function() {
    					$scope.exportando = false;
    				});
    			}else{
    				$scope.addAlerts([{msg: $scope.getMensagem("LMS-10047")}]);
    			}
    		};
    		
    		$scope.limpar = function() {
    			$scope.tmp = {};
    			$scope.list = [];
    			$scope.listTableParams.clear();    	
    			$scope.clearAlerts();
    			$scope.setDataPadrao();
    		};
    		
    		$scope.validateForm = function(){
    			if(!$scope.filtro.filtros.idAgendamentoEntrega){
    				if($scope.filtro.filtros.idDoctoServico || 
    					$scope.filtro.filtros.cae ||
    					$scope.filtro.filtros.pedidoColeta || 
    					$scope.filtro.filtros.controleCarga ||
    					$scope.filtro.filtros.manifestoColeta ||
    					$scope.filtro.filtros.manifestoEntrega ||
    					$scope.filtro.filtros.awb ||
    					($scope.filtro.filtros.nfCliente && $scope.filtro.filtros.volumeColeta)){
    						return true;
    				}else if(($scope.filtro.filtros.filialOrigem && $scope.filtro.filtros.filialDestino)
    					&& $scope.filtro.filtros.periodoInicial && $scope.filtro.filtros.periodoFinal){
    						if($scope.validaDiferencaDias($scope.filtro.filtros.periodoInicial, $scope.filtro.filtros.periodoFinal, 1)){
    							return true;
    						}
    						return false;
    				}else if(($scope.filtro.filtros.remetente ) && 
        					($scope.filtro.filtros.periodoInicial  && $scope.filtro.filtros.periodoFinal )){
        						if($scope.validaDiferencaDias($scope.filtro.filtros.periodoInicial, $scope.filtro.filtros.periodoFinal, 7)){
        							return true;
        						}
        						return false;
        			}else if(($scope.filtro.filtros.nfCliente && $scope.filtro.filtros.remetente) ||
    					($scope.filtro.filtros.nfCliente && $scope.filtro.filtros.destinatario) ||
    					($scope.filtro.filtros.nfCliente && $scope.filtro.filtros.consignatario ) ||
    					($scope.filtro.filtros.nfCliente && $scope.filtro.filtros.redespacho ) ||
    					($scope.filtro.filtros.nfCliente && $scope.filtro.filtros.responsavelFrete ) ||
    					($scope.filtro.filtros.remetente && $scope.filtro.filtros.destinatario ) ||
    					($scope.filtro.filtros.consignatario && $scope.filtro.filtros.destinatario ) ||
    					($scope.filtro.filtros.responsavelFrete && $scope.filtro.filtros.destinatario ) ||
    					($scope.filtro.filtros.remetente  && $scope.filtro.filtros.consignatario ) ||
    					($scope.filtro.filtros.responsavelFrete && $scope.filtro.filtros.consignatario ) ||
    					($scope.filtro.filtros.remetente  && $scope.filtro.filtros.responsavelFrete) && 
    					($scope.filtro.filtros.periodoInicial  && $scope.filtro.filtros.periodoFinal )){
    						if($scope.validaDiferencaDias($scope.filtro.filtros.periodoInicial, $scope.filtro.filtros.periodoFinal, 7)){
    							return true;
    						}
    						return false;
    				}else if(($scope.filtro.filtros.destinatario ) ||
        					($scope.filtro.filtros.consignatario ) ||
        					($scope.filtro.filtros.responsavelFrete ) && 
        					($scope.filtro.filtros.periodoInicial  && $scope.filtro.filtros.periodoFinal )){
        						if($scope.validaDiferencaDias($scope.filtro.filtros.periodoInicial, $scope.filtro.filtros.periodoFinal, 7)){
        							return true;
        						}
        						return false;
        				}
    				return false;
    			}else{
    				return true;
    			}
    		};
    		
    		$scope.loadFiltros = function(report) {
    			if (report) {
    				$scope.filtro.report = true;
    				$scope.filtro.columns = [
    				             {title: $scope.$eval("'documentoServico' |  translate"), column: "dsDoctoServico"},
    				             {title: $scope.$eval("'cae' |  translate"), column: "nrCae"},
    				             {title: $scope.$eval("'finalidade' |  translate"), column: "finalidade"},
    				             {title: $scope.$eval("'filialDestino2' |  translate"), column: "sgFilialDestino"},
    				             {title: $scope.$eval("'awb' |  translate"), column: "serieNroAwb"},
    				             {title: $scope.$eval("'dataEmissao' |  translate"), column: "dhEmissao"},
    				             {title: $scope.$eval("'dataPrevista' |  translate"), column: "dtPrevista"},
    				             {title: $scope.$eval("'dtSaidaViagem' |  translate"), column: "dtSaidaViagem"},
    				             {title: $scope.$eval("'dtChegadaViagem' |  translate"), column: "dtChegadaViagem"},
    				             {title: $scope.$eval("'dtSaidaEntrega' |  translate"), column: "dtSaidaEntrega"},
    				             {title: $scope.$eval("'dataEntrega' |  translate"), column: "dtEntrega"},
    				             {title: $scope.$eval("'localizacao' |  translate"), column: "dsLocMerc"},
    				             {title: $scope.$eval("'notaFiscal' |  translate"), column: "nf"},
    				             {title: $scope.$eval("'identificacaoRemetente' |  translate"), column: "remDsIdentificacao"},
    				             {title: $scope.$eval("'remetente' |  translate"), column: "remNmPessoa"},
    				             {title: $scope.$eval("'identificacaoDestinatario' |  translate"), column: "destDsIdentificacao"},
    				             {title: $scope.$eval("'destinatario' |  translate"), column: "destNmPessoa"},
    				             {title: $scope.$eval("'identificacaoConsignatario' |  translate"), column: "consDsIdentificacao"},
    				             {title: $scope.$eval("'recebedor' |  translate"), column: "consNmPessoa"},
    				             {title: $scope.$eval("'identificacaoRedespacho' |  translate"), column: "redesDsIdentificacao"},
    				             {title: $scope.$eval("'expedidor' |  translate"), column: "redesNmPessoa"},
    				             {title: $scope.$eval("'identificacaoResponsavelFrete' |  translate"), column: "respDsIdentificacao"},
    				             {title: $scope.$eval("'responsavelFrete' |  translate"), column: "respNmPessoa"},
    				             {title: $scope.$eval("'dataAgendamento' |  translate"), column: "dtAgen"}
    				             ];
    			} else {
    				$scope.filtro.report = false;
    			}
    			
    			$scope.filtro.filtros.modal = $scope.getDomainValue($scope.tmp.modal);
    			$scope.filtro.filtros.abrangencia = $scope.getDomainValue($scope.tmp.abrangencia);
    			$scope.filtro.filtros.cae = $scope.tmp.cae ? $scope.tmp.cae.nrCae : null;
    			$scope.filtro.filtros.caeNmFilial = $scope.tmp.cae ? $scope.tmp.cae.nmFilial : null;
    			$scope.filtro.filtros.caeIdFilial = $scope.tmp.cae ? $scope.tmp.cae.idFilial : null;
    			$scope.filtro.filtros.tipoServico = $scope.tmp.tipoServico ? $scope.tmp.tipoServico.idTipoServico : null;
    			$scope.filtro.filtros.finalidade = $scope.getDomainValue($scope.tmp.finalidade);
    			$scope.filtro.filtros.filialOrigem = $scope.tmp.filialOrigem ? $scope.tmp.filialOrigem.idFilial : null;
    			$scope.filtro.filtros.filialDestino = $scope.tmp.filialDestino ? $scope.tmp.filialDestino.idFilial : null;
    			$scope.filtro.filtros.idDoctoServico = $scope.tmp.doctoServico ? $scope.tmp.doctoServico.idDoctoServico : null;
    			$scope.filtro.filtros.cotacao = $scope.tmp.cotacao ? $scope.tmp.cotacao.idCotacao : null;
    			$scope.filtro.filtros.pedidoColeta = $scope.tmp.pedidoColeta ? $scope.tmp.pedidoColeta.idPedidoColeta : null;
    			$scope.filtro.filtros.nfCliente = $scope.tmp.nfCliente;
    			$scope.filtro.filtros.periodoInicial = $scope.tmp.periodoInicial;
    			$scope.filtro.filtros.periodoFinal = $scope.tmp.periodoFinal;
    			$scope.filtro.filtros.remetente = $scope.getIdCliente($scope.tmp.remetente);
    			$scope.filtro.filtros.destinatario = $scope.getIdCliente($scope.tmp.destinatario);
    			$scope.filtro.filtros.consignatario = $scope.getIdCliente($scope.tmp.consignatario);
    			$scope.filtro.filtros.redespacho = $scope.getIdCliente($scope.tmp.redespacho);
    			$scope.filtro.filtros.responsavelFrete = $scope.getIdCliente($scope.tmp.responsavelFrete);
    			$scope.filtro.filtros.controleCarga = $scope.tmp.controleCarga ? $scope.tmp.controleCarga.idControleCarga : null;
    			$scope.filtro.filtros.manifestoColeta = $scope.tmp.manifestoColeta ? $scope.tmp.manifestoColeta.idManifestoColeta : null;
    			$scope.filtro.filtros.manifestoViagemNacional = $scope.tmp.manifestoViagemNacional ? $scope.tmp.manifestoViagemNacional.idManifestoViagem : null;
    			$scope.filtro.filtros.nrFormulario = $scope.tmp.nrFormulario;
    			$scope.filtro.filtros.manifestoEntrega = $scope.tmp.manifestoEntrega ? $scope.tmp.manifestoEntrega.idManifestoEntrega : null;
    			$scope.filtro.filtros.awb = $scope.tmp.awb ? $scope.tmp.awb.idAwb : null;
    			$scope.filtro.filtros.idInformacaoDoctoCliente = $scope.tmp.informacaoDoctoCliente ? $scope.tmp.informacaoDoctoCliente.idInformacaoDoctoCliente : null;
    			$scope.filtro.filtros.nrDocumentoCliente = $scope.tmp.nrDocumentoCliente;
    			$scope.filtro.filtros.volumeColeta = $scope.tmp.volumeColeta;
    			
    			if ($scope.tmp.informacaoDoctoCliente && $scope.tmp.informacaoDoctoCliente.tpCampo) {
    				if ($scope.tmp.informacaoDoctoCliente.tpCampo == 'D' || $scope.tmp.informacaoDoctoCliente.tpCampo == 'Z') {
    					var date = $scope.tmp.nrDocumentoCliente;
    					if ($scope.tmp.informacaoDoctoCliente.tpCampo == 'Z') {
    						date.setHours($scope.tmp.nrDocumentoClienteTime.getHours());
    						date.setMinutes($scope.tmp.nrDocumentoClienteTime.getMinutes());
    						$scope.filtro.filtros.nrDocumentoCliente = $scope.convertDateTimeToString(date);
    					} else {
    						$scope.filtro.filtros.nrDocumentoCliente = $scope.convertDateToString(date);
    					}
    				}
    				if ($scope.tmp.informacaoDoctoCliente.tpCampo == 'H') {
    					$scope.filtro.filtros.nrDocumentoCliente = $scope.convertTimeToString($scope.tmp.nrDocumentoClienteTime);
    				}
    			}
    		};
    		
    		$scope.getDomainValue = function(domain) {
    			if (domain) {
    				return domain.value;
    			}
    			return null;
    		};
    		
    		$scope.getIdCliente = function(cliente) {
    			if (cliente) {
    				return cliente.idCliente;
    			}
    			return null;
    		};
    		
    		$scope.convertDateToString = function(date) {
    			if (date) {
    				return moment(date).format('YYYY-MM-DD');

    			}
    			return null;
    		};
    		
    		$scope.convertDateTimeToString = function(date) {
    			if (date) {
    				return moment(date).format('YYYY-MM-DD HH:mm');
    				
    			}
    			return null;
    		};
    		
    		$scope.convertTimeToString = function(date) {
    			if (date) {
    				return moment(date).format('HH:mm');
    				
    			}
    			return null;
    		};
    		
    		$scope.validaDiferencaDias = function(dataInicial, dataFinal, dias){
    			if (dataInicial && dataFinal && dias) {
    				 var oneDay = 24*60*60*1000; 
    				 var firstDate = new Date(dataInicial);
    				 var secondDate = new Date(dataFinal);
    		
    				 var diffDays = Math.round(Math.abs((firstDate.getTime() - secondDate.getTime())/(oneDay)));
    				 return diffDays <= dias;
    			} else {
    				return false;
    			}
    		};
    		
    		$scope.abreDetalhe = function(o) {
    			$location.path("/app/consultarLocalizacaoMercadoria/cad/" + o.idDoctoServico);
    		};
    		
    		$scope.$watch("tmp.doctoServico", function (value) {
    		    if (value) {
		    		$scope.tmp.modal = value.modal;
		    		$scope.tmp.abrangencia = value.abrangencia;
		    		$scope.tmp.tipoServico = value.tipoServico;
		    		$scope.tmp.filialOrigem = value.filialOrigem;
		    		$scope.tmp.filialDestino = value.filialDestino;
		    		$scope.tmp.finalidade = value.finalidade;
		    		$scope.tmp.nfCliente = value.nfCliente;
		    		$scope.tmp.pedidoColeta = value.pedidoColeta;
		    		$scope.tmp.remetente = value.remetente;
		    		$scope.tmp.destinatario = value.destinatario;
		    		$scope.tmp.periodoInicial = value.dhEmissao;
		    		$scope.tmp.periodoFinal = value.dhEmissao;
    		    }
    		  }, true);
    		
    		$scope.$watch('tmp.remetente', function (value) {
    			if (value) {
	    			var filtro = {'idCliente': value.id};
	    			
	    			if ($scope.tmp.modal) {
	    				filtro.modal = $scope.tmp.modal.value;
	    			}
	    			if ($scope.tmp.abrangencia) {
	    				filtro.abrangencia = $scope.tmp.abrangencia.value;
	    			}
	    			
	    			$scope.rest.doPost("findInformacoesCliente", filtro).then(function (response) {
	    				
	    				if (response.length == 1) {
	    					$scope.tmp.informacaoDoctoCliente = response[0];
	    				} else {
	    					$scope.tmp.informacaoDoctoCliente = null;
	    				}
	
	    			}, function () {
	    				$scope.tmp.informacaoDoctoCliente = null;
	    			});
    			}
    		}, true);
    		
    		$scope.$watch('tmp.informacaoDoctoCliente', function (value) {
    	    	$scope.tmp.nrDocumentoCliente = null;
    	    	$scope.tmp.nrDocumentoClienteTime = null;
    			if (value) {
    				if (value.tpCampo == 'A') {
    					$scope.maskNumeroDocumento = /./;
    				} else if (value.tpCampo == 'N') {
    					var decimal = false;
    			        if (value.dsFormatacao) {
    			            if (value.dsFormatacao.indexOf('.') > -1 || value.dsFormatacao.indexOf(',') > -1) {
    			                decimal = true;
    			            }
    			        }
    			        if (decimal) {
    			        	$scope.maskNumeroDocumento = "decimal";
    			        } else {
    			        	$scope.maskNumeroDocumento = "integer";
    			        }
    				}
    			} else {
    				$scope.maskNumeroDocumento = /./;
    			}
    		}, true);
    		
    		var clearPeriodo = function(value) {
    			if (value && !$scope.tmp.doctoServico) {
    				$scope.tmp.periodoInicial = null;
    				$scope.tmp.periodoFinal = null;
    			}
    		};
    		    		
    		$scope.$watch("tmp.cotacao", clearPeriodo, true);
    		$scope.$watch("tmp.pedidoColeta", clearPeriodo, true);
    		$scope.$watch("tmp.controleCarga", clearPeriodo, true);
    		$scope.$watch("tmp.manifestoColeta", clearPeriodo, true);
    		$scope.$watch("tmp.manifestoViagemNacional", clearPeriodo, true);
    		$scope.$watch("tmp.manifestoEntrega", clearPeriodo, true);
    		$scope.$watch("tmp.awb", clearPeriodo, true);
    		
    		$scope.clickAbaListagem = function(evt) {
    			if (! $scope.list.length) {
    				evt.preventDefault();
    				evt.stopPropagation();
    			}
    		};

	} 
];
