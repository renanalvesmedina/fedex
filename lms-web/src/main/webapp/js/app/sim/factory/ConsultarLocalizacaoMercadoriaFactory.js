
(function(consultarlocalizacoesmercadoriasModule){
	
	consultarlocalizacoesmercadoriasModule.factory("ConsultarLocalizacaoMercadoriaFactory", [
		"$http", 
		"$q", 
		function($http, $q) {
            	return {
            		findDocumentos: function(filter) {
            			return $http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findDocumentos", filter)
            			.then(function(response) {
            				return response.data;
            			});
            		},
            		tipoServico : function() {
            			return $http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findTipoServico")
            			.then(function(response){
            				return response.data;
            			});
            		},
              		doctoServico : function(value) {
            			return $http.get(contextPath+"rest/expedicao/doctoServico/findDoctoServicoSuggest?value="+value)
            			.then(function(response){
            				return response.data;
            			});
              		},
              		findByIdDetalhamentoNotasFiscais: function(idDoctoServico) {
              			return $http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/notasFiscais?idDoctoServico="+idDoctoServico)
            			.then(function(response){
            				return response.data;
            			});
	          		},	
	          		findByIdDetalhamentoVolumes: function(filtro) {
              			return $http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/volumes", filtro)
            			.then(function(response){
            				return response.data;
            			});
	          		},	
                    findByIdDetalhamentoAbaPrincipalEventosVolume: function (filtro) {
                		var deferred = $q.defer();
            			$http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/volumes/eventos", filtro).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                		
                    },
                    findByIdDetalhamentoAbaPrincipalNotasFiscaisOperadas: function (filtro) {
                    	var deferred = $q.defer();
                    	$http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/notasFiscaisOperadas", filtro).then(function(response) {
                    		deferred.resolve(response.data);
                    	});
                    	return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoIntegrantesEnderecos: function (filtro) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/integrantes/enderecos?idPessoa=" + filtro.filtros.idPessoa + "&geral=" + filtro.filtros.geral).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                		
                    },
                	findByIdDetalhamento: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/detalhe?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                		
                    },
                    findByIdDetalhamentoAbaPrincipal: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/aba/principal/informacoesBasicas?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                		
                    },
                    findByIdDetalhamentoIntegrantes: function(idDoctoServico) {
            			return $http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/integrantes?idDoctoServico=" + idDoctoServico).then(function(response) {
            				return response.data;
            			});
                    },                   
                    findVolumes: function(filter) {
            			return $http.post(contextPath+"rest/carregamento/dispositivoUnitizacao/findVolumes", filter)
            			.then(function(response){
            				return response.data;
            			});
            		},    
            		
                    findCooperadaByIdConhecimento: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/parceiras/principal?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },                    
                    findPaginatedIntegrantesAbaParcerias: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/parceiras/integrantes?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedNotaFiscalAbaParcerias: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/parceiras/notasFiscais?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedDadosFrete: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/parceiras/dadosFrete?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findDadosCalculoByIdConhecimento: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/parceiras/dadosCalculo?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },                    
                    findOutrosByIdConhecimento: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/parceiras/outros?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoComplementosObservacoes: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/observacoes?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                	findByIdDetalhamentoComplementosEmbalagens: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/embalagens?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoComplementosDoctoServico: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/doctoServico?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoComplementosNotasFiscais: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/notasFiscais?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoComplementosAgendamentos: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/agendamentos?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoComplementosAgendamentosFindById: function(idAgendamento) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/agendamentos/findById?idAgendamento=" + idAgendamento).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetalhamentoComplementosOutros: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/complementos/outros?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findDadosCalculoFrete: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/frete/calculo?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedParcelaPreco: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/frete/parcela/tabela?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedCalculoServico: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/frete/calculoServico/tabela?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findTotaisCalculoServico: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/frete/calculoServico?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },         
                    findTipoTributacaoIcms: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/frete/impostos/tributacao?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },  
                    findPaginatedImpostos: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/frete/impostos?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },  
                    
                    findPaginatedDevedorDocServFatByDoctoServico: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/cobranca/tabela?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findDevedorDocServFatDetail: function(idDevedorDocServFat) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/cobranca?idDevedorDocServFat=" + idDevedorDocServFat).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedControleCarga: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/cc?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    }, 
                    findManifestoColetaByIdDoctoServico: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/manifesto/coleta?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    }, 
                    findPaginatedManifestoViagem: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/manifesto/viagem?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    }, 
                    findPaginatedManifestoEntrega: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/manifesto/entrega?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    },
                    findPaginatedManifestoEntregaNotasFiscais: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/manifesto/entrega/notasFiscais?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findAwbByIdDoctoServico: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/awb/infobasica?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedEventos: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/eventos?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedTrackingAwb: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/trackingAwb?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedHistoricoAwb: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/historicoAwb?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findByIdDetailAbaRNC: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/rnc?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    }, 
                    findPaginatedOcorrenciaNaoConformidade: function(idNaoConformidade) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/rnc/tabela?idNaoConformidade=" + idNaoConformidade).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findIndenizacaoById: function(idDoctoServicoIndenizacao) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/indenizacoes?idDoctoServicoIndenizacao=" + idDoctoServicoIndenizacao).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    }, 
                    findPaginatedFilialDebitada: function(idDoctoServicoIndenizacao) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/indenizacoes/tabela?idDoctoServicoIndenizacao=" + idDoctoServicoIndenizacao).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    }, 
                    findPaginatedEventosRim: function(filtro) {
                		var deferred = $q.defer();
            			$http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/indenizacoes/eventos", filtro).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },
                    findPaginatedBloqueiosLiberacoes: function(idDoctoServico) {
                		var deferred = $q.defer();
            			$http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findById/bloqueios?idDoctoServico=" + idDoctoServico).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    	
                    },  
            		visualizar: function(filter) {
            			return $http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/visualizar", filter)
            			.then(function(response) {
            				return response.data;
            			});
            		},
            		imprimirInformacoesVolumes: function(filter) {
            			return $http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/imprimirInformacoesVolumes", filter)
            			.then(function(response) {
            				return response.data;
            			});
            		},
					findPendenciaSituacaoWorkflow: function(idDoctoServico) {
						var deferred = $q.defer();
						$http.get(contextPath+"rest/workflow/workflow/findPendenciaSituacaoWorkflow?idDoctoServico="+idDoctoServico).then(function(response) {
							deferred.resolve(response.data);
						});
						return deferred.promise;
					},
            		imprimirRNC: function(filter) {
            			return $http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/imprimirRNC", filter)
            			.then(function(response) {
            				return response.data;
            			});
            		},
            		findRespostaAbasDetalhamento: function(idDoctoServico) {
            			return $http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findRespostaAbasDetalhamento?idDoctoServico="+idDoctoServico)
            			.then(function(response) {
            				return response.data;
            			});
            		},
            		findClienteCCTByUsuario: function() {
            			return $http.get(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findClienteCCTByUsuario")
            			.then(function(response) {
            				return response.data;
            			});
            		},
                    findRemarks: function (filtro) {
                		var deferred = $q.defer();
            			$http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/findRemarks", filtro).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    },
                    storeRemark: function (remark) {
                		var deferred = $q.defer();
            			$http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/storeRemark", remark).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    },
					storeBlAgendamento: function (agendamentoBloqueio) {
                		var deferred = $q.defer();
            			$http.post(contextPath+"rest/sim/consultarLocalizacaoMercadoria/storeBlAgendamento", agendamentoBloqueio).then(function(response) {
            				deferred.resolve(response.data);
            			});
                		return deferred.promise;
                    }
            	};
        }
	]);

})(consultarlocalizacoesmercadoriasModule);
