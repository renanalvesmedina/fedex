CTR_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
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
									property:"doctoServico.nrDoctoServico",
									 
									maxChars:"8", 
									mask:"00000000"
								},
								verifierDigit:{
									property:"doctoServico2", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"dvConhecimento", 
									service:"lms.expedicao.conhecimentoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberCTR",
									url:"/expedicao/pesquisarConhecimento.do", 
									maxChars:"1", 
									mask:"0",
									propertyMappings:[
										{
											modelProperty:"idDoctoServico", 
											relatedProperty:"doctoServico.idDoctoServico",
											disabled:false
										},
										{ 
											modelProperty:"tpDocumentoServico", 
											criteriaValue:"CTR",
											inlineQuery:true
										},										{
											modelProperty:"dvConhecimento", 
											criteriaProperty:"doctoServico2.dvConhecimento", 
											inlineQuery:true
										}, 
										{
											modelProperty:"dvConhecimento", 
											relatedProperty:"doctoServico2.dvConhecimento",
											disabled:false
										},
										{ 
											modelProperty:"nrDoctoServico", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											inlineQuery:true
										},
										{ //Finalidade: Preencher campo de n�mero na tela aberta pela lookup
											modelProperty:"nrConhecimento", 
											criteriaProperty:"doctoServico.nrDoctoServico",
											inlineQuery:false, //False, pois n�o � necess�ria para pesquisa direta.
											disable:false //Nesse caso, disable sem o D no fim.
										},										
										{ 
											modelProperty:"nrDoctoServico", 
											relatedProperty:"doctoServico.nrDoctoServico",
											disabled:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
                                            modelProperty:"filialByIdFilialOrigem.idFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                                            blankFill:false
                                        },
                                        { 
                                            modelProperty:"filialByIdFilialOrigem.sgFilial", 
                                            relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                                            blankFill:false
                                        }
									]
								}
							};
serviceDocumentWidgetDefinitions[0] = CTR_SERVICE_DOCUMENT_WIDGET_DEFINITION;

CTE_SERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"CTE", 
		filial:{
			property:"doctoServico.filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialCTR",
			url:"/municipios/manterFiliais.do"
		}, 
		documentNumber:{
			property:"doctoServico.nrDoctoServico",
			 
			maxChars:"8", 
			mask:"00000000"
		},
		verifierDigit:{
			property:"doctoServico2", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"dvConhecimento", 
			service:"lms.expedicao.conhecimentoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberCTR",
			url:"/expedicao/pesquisarConhecimento.do", 
			maxChars:"1", 
			mask:"0",
			propertyMappings:[
				{
					modelProperty:"idDoctoServico", 
					relatedProperty:"doctoServico.idDoctoServico",
					disabled:false
				},
				{ 
					modelProperty:"tpDocumentoServico", 
					criteriaValue:"CTE",
					inlineQuery:true
				},										{
					modelProperty:"dvConhecimento", 
					criteriaProperty:"doctoServico2.dvConhecimento", 
					inlineQuery:true
				}, 
				{
					modelProperty:"dvConhecimento", 
					relatedProperty:"doctoServico2.dvConhecimento",
					disabled:false
				},
				{ 
					modelProperty:"nrDoctoServico", 
					criteriaProperty:"doctoServico.nrDoctoServico",
					inlineQuery:true
				},
				{ //Finalidade: Preencher campo de n�mero na tela aberta pela lookup
					modelProperty:"nrConhecimento", 
					criteriaProperty:"doctoServico.nrDoctoServico",
					inlineQuery:false, //False, pois n�o � necess�ria para pesquisa direta.
					disable:false //Nesse caso, disable sem o D no fim.
				},										
				{ 
					modelProperty:"nrDoctoServico", 
					relatedProperty:"doctoServico.nrDoctoServico",
					disabled:false
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.idFilial", 
					criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
					inlineQuery:true
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.sgFilial", 
					criteriaProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
					inlineQuery:true
				},
				{ 
                    modelProperty:"filialByIdFilialOrigem.idFilial", 
                    relatedProperty:"doctoServico.filialByIdFilialOrigem.idFilial",
                    blankFill:false
                },
                { 
                    modelProperty:"filialByIdFilialOrigem.sgFilial", 
                    relatedProperty:"doctoServico.filialByIdFilialOrigem.sgFilial",
                    blankFill:false
                }
			]
		}
	};
serviceDocumentWidgetDefinitions[1] = CTE_SERVICE_DOCUMENT_WIDGET_DEFINITION;