// CTR e NFT possuem uma propriedade a mais (tpDocumentoServico) pois as duas especializações são gravadas na tabela de conhecimento.
// Alem disso a propriedade serve para selecionar o tipo (combo) de documento correto na popup.

var serviceDocumentWidgetDefinitions = new Array();

var CTR_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"CTR", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialCTR",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.expedicao.conhecimentoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberCTR",
									url:"/expedicao/pesquisarConhecimento.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{ 
											modelProperty:"tpDocumentoServico", 
											criteriaValue:"CTR",
											inlineQuery:true
										},										
										{ 
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											inlineQuery:true
										},
										{ //Finalidade: Preencher campo de número na tela aberta pela lookup
											modelProperty:"nrConhecimento", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											disable:false, //Para criteria, disable sem o D
											inlineQuery:false //False, pois não é necessária para pesquisa direta.
										},											
										{ 
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
                                        
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = CTR_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var CTE_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"CTE", 
		filial:{
			property:"doctoServico.filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialCTE",
			url:"/municipios/manterFiliais.do"
		}, 
		documentNumber:{
			property:"doctoServico", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"nrDoctoServico", 
			service:"lms.expedicao.conhecimentoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberCTE",
			url:"/expedicao/pesquisarConhecimento.do", 
			maxChars:"8", 
			mask:"00000000",
			propertyMappings:[
			                  { 
			                	  modelProperty:"tpDocumentoServico", 
			                	  criteriaValue:"CTE",
			                	  inlineQuery:true
			                  },										
			                  { 
			                	  modelProperty:"nrDoctoServico", 
			                	  criteriaProperty:"doctoServico.nrDoctoServico",
			                	  inlineQuery:true
			                  },
			                  { //Finalidade: Preencher campo de número na tela aberta pela lookup
			                	  modelProperty:"nrConhecimento", 
			                	  criteriaProperty:"doctoServico.nrDoctoServico",
			                	  disable:false, //Para criteria, disable sem o D
			                	  inlineQuery:false //False, pois não é necessária para pesquisa direta.
			                  },											
			                  { 
			                	  modelProperty:"nrDoctoServico", 
			                	  relatedProperty:"doctoServico.nrDoctoServico",
			                	  disabled:false //Para related, disabled com D
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.idFilial", 
			                	  criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
			                	  inlineQuery:true
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.idFilial", 
			                	  relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
			                	  blankFill:false
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.sgFilial", 
			                	  criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
			                	  inlineQuery:false
			                  },                                        
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.sgFilial", 
			                	  relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
			                	  blankFill:false
			                  }
			                  
			                  ]
		}
};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = CTE_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var CRT_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"CRT", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialCRT",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.expedicao.ctoInternacionalService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberCRT",
									url:"/expedicao/manterCRT.do", 
									maxChars:"6", 
									mask:"000000",
									propertyMappings:[
										{
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											inlineQuery:true
										},
										{ //Finalidade: Preencher campo de número na tela aberta pela lookup
											modelProperty:"nrCrt", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											disable:false, //Para criteria, disable sem o D
											inlineQuery:false //False, pois não é necessária para pesquisa direta.
										},										
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = CRT_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var NFS_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"NFS", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialNFS",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.expedicao.notaFiscalServicoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberNFS",
									url:"/expedicao/digitarPreNotaFiscalServico.do", 
									maxChars:"8", 
									mask:"00000000", 
									propertyMappings:[
										{ 
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											inlineQuery:true
										},
										{ //Finalidade: Preencher campo de número na tela aberta pela lookup
											modelProperty:"nrNotaFiscalServico", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											disable:false, //Para criteria, disable sem o D
											inlineQuery:false //False, pois não é necessária para pesquisa direta.
										},										
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = NFS_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var NSE_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"NSE", 
		filial:{
			property:"doctoServico.filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialNFS",
			url:"/municipios/manterFiliais.do"
		}, 
		documentNumber:{
			property:"doctoServico", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"nrDoctoServico", 
			service:"lms.expedicao.notaFiscalServicoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberNFS",
			url:"/expedicao/digitarPreNotaFiscalServico.do", 
			maxChars:"8", 
			mask:"00000000", 
			propertyMappings:[
				{ 
					modelProperty:"nrDoctoServico", 
					criteriaProperty:"doctoServico.nrDoctoServico", 
					inlineQuery:true
				},
				{ //Finalidade: Preencher campo de número na tela aberta pela lookup
					modelProperty:"nrNotaFiscalServico", 
					criteriaProperty:"doctoServico.nrDoctoServico", 
					disable:false, //Para criteria, disable sem o D
					inlineQuery:false //False, pois não é necessária para pesquisa direta.
				},										
				{
					modelProperty:"nrDoctoServico", 
					relatedProperty:"doctoServico.nrDoctoServico",
					disabled:false //Para related, disabled com D
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.idFilial", 
					criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
					inlineQuery:true
				},
				{ 
                    modelProperty:"filialByIdFilialOrigem.idFilial", 
                    relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                    blankFill:false
                },
				{ 
					modelProperty:"filialByIdFilialOrigem.sgFilial", 
					criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
					inlineQuery:false
				},                                        
                { 
                    modelProperty:"filialByIdFilialOrigem.sgFilial", 
                    relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                    blankFill:false
                }
			]
		}
	};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = NSE_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var NFT_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"NFT", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialNFT",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.expedicao.conhecimentoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberNFT",
									url:"/expedicao/pesquisarConhecimento.do", 
									maxChars:"8", 
									mask:"00000000", 
									propertyMappings:[
										{ 
											modelProperty:"tpDocumentoServico", 
											criteriaValue:"NFT",
											inlineQuery:true
										},										
										{ 
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											inlineQuery:true
										}, 
										{ //Finalidade: Preencher campo de número na tela aberta pela lookup
											modelProperty:"nrConhecimento", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											disable:false, //Para criteria, disable sem o D
											inlineQuery:false //False, pois não é necessária para pesquisa direta.
										},											
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = NFT_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var NTE_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"NTE", 
		filial:{
			property:"doctoServico.filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialNFT",
			url:"/municipios/manterFiliais.do"
		}, 
		documentNumber:{
			property:"doctoServico", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"nrDoctoServico", 
			service:"lms.expedicao.conhecimentoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberNFT",
			url:"/expedicao/pesquisarConhecimento.do", 
			maxChars:"8", 
			mask:"00000000", 
			propertyMappings:[
				{ 
					modelProperty:"tpDocumentoServico", 
					criteriaValue:"NTE",
					inlineQuery:true
				},										
				{ 
					modelProperty:"nrDoctoServico", 
					criteriaProperty:"doctoServico.nrDoctoServico", 
					inlineQuery:true
				}, 
				{ //Finalidade: Preencher campo de número na tela aberta pela lookup
					modelProperty:"nrConhecimento", 
					criteriaProperty:"doctoServico.nrDoctoServico",
					disable:false, //Para criteria, disable sem o D
					inlineQuery:false //False, pois não é necessária para pesquisa direta.
				},											
				{
					modelProperty:"nrDoctoServico", 
					relatedProperty:"doctoServico.nrDoctoServico",
					disabled:false //Para related, disabled com D
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.idFilial", 
					criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
					inlineQuery:true
				},
				{ 
                    modelProperty:"filialByIdFilialOrigem.idFilial", 
                    relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                    blankFill:false
                },
				{ 
					modelProperty:"filialByIdFilialOrigem.sgFilial", 
					criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
					inlineQuery:false
				},                                        
                { 
                    modelProperty:"filialByIdFilialOrigem.sgFilial", 
                    relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                    blankFill:false
                }
			]
		}
	};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = NTE_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var MDA_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"MDA", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialMDA",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.pendencia.mdaService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberMDA",
									url:"/pendencia/consultarMDA.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{ 
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											disable:false, //Para criteria, disable sem o D
											inlineQuery:true
										}, 
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = MDA_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var RRE_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"RRE", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialRRE",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.entrega.reciboReembolsoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberRRE",
									url:"/entrega/manterReembolsos.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{ 
						 					modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											inlineQuery:true
										},
										{ //Finalidade: Preencher campo de número na tela aberta pela lookup
						 					modelProperty:"nrReciboReembolso", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											disable:false, //Para criteria, disable sem o D											
											inlineQuery:false //False, pois não é necessária para pesquisa direta.
										}, 										 
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = RRE_SERVICE_DOCUMENT_WIDGET_DEFINITION;

var NDN_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"NDN", 
								filial:{
									property:"doctoServico.filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialNDN",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"doctoServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrDoctoServico", 
									service:"lms.contasreceber.notaDebitoNacionalService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberNDN",
									url:"/contasReceber/manterNotasDebitoNacionais.do", 
									maxChars:"10", 
									propertyMappings:[
										{ 
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											inlineQuery:true
										}, 
										{  //Finalidade: Preencher campo de número na tela aberta pela lookup
											modelProperty:"nrNotaDebitoNac", 
											criteriaProperty:"doctoServico.nrDoctoServico", 
											disable:false, //Para criteria, disable sem o D
											inlineQuery:false //False, pois não é necessária para pesquisa direta.
										}, 
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false //Para related, disabled com D
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:false
										},                                        
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[serviceDocumentWidgetDefinitions.length] = NDN_SERVICE_DOCUMENT_WIDGET_DEFINITION;