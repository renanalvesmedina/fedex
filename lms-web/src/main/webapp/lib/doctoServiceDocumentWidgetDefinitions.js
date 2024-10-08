var doctoserviceDocumentWidgetDefinitions = new Array();

var CTR_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"CTR", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialCTR",
									url:"/municipios/manterFiliais.do",
									propertyMappings:[
										{ 
						 					modelProperty:"sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
											inlineQuery:true
										}, 
										{
											modelProperty:"sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial"
										},
										{ 
											modelProperty:"pessoa.nmFantasia", 
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
										}
									]
								}, 
								documentNumber:{
									property:"conhecimento", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrConhecimento", 
									service:"lms.expedicao.conhecimentoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberCTR",
									url:"/expedicao/pesquisarConhecimento.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{ 
											modelProperty:"nrConhecimento", 
											criteriaProperty:"conhecimento.nrConhecimento",
											inlineQuery:true
										},
										{ 
											modelProperty:"tpDocumentoServico", 
											criteriaProperty:"tpDocumentoServico",
											inlineQuery:true
										},
										{ 
											modelProperty:"nrConhecimento", 
											relatedProperty:"conhecimento.nrConhecimento",
											disabled:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
											criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											inlineQuery:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
									]
								}
							};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = CTR_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var CTE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"CTE", 
		filial:{
			property:"filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialCTE",
			url:"/municipios/manterFiliais.do",
			propertyMappings:[
			                  { 
			                	  modelProperty:"sgFilial", 
			                	  criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
			                	  inlineQuery:true
			                  }, 
			                  {
			                	  modelProperty:"sgFilial", 
			                	  relatedProperty:"filialByIdFilialOrigem.sgFilial"
			                  },
			                  { 
			                	  modelProperty:"pessoa.nmFantasia", 
			                	  relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
			                  }
			                  ]
		}, 
		documentNumber:{
			property:"conhecimento", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"nrConhecimento", 
			service:"lms.expedicao.conhecimentoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberCTE",
			url:"/expedicao/pesquisarConhecimento.do", 
			maxChars:"8", 
			mask:"00000000",
			propertyMappings:[
			                  { 
			                	  modelProperty:"nrConhecimento", 
			                	  criteriaProperty:"conhecimento.nrConhecimento",
			                	  inlineQuery:true
			                  },
			                  { 
			                	  modelProperty:"tpDocumentoServico", 
			                	  criteriaProperty:"tpDocumentoServico",
			                	  inlineQuery:true
			                  },
			                  { 
			                	  modelProperty:"nrConhecimento", 
			                	  relatedProperty:"conhecimento.nrConhecimento",
			                	  disabled:false
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.idFilial", 
			                	  criteriaProperty:"filialByIdFilialOrigem.idFilial",
			                	  inlineQuery:true
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.sgFilial", 
			                	  criteriaProperty:"filialByIdFilialOrigem.sgFilial",
			                	  inlineQuery:true
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
			                	  criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
			                	  inlineQuery:false
			                  },
			                  {
			                	  modelProperty:"filialByIdFilialOrigem.sgFilial", 
			                	  relatedProperty:"filialByIdFilialOrigem.sgFilial",
			                	  blankFill:false
			                  },
			                  { 
			                	  modelProperty:"filialByIdFilialOrigem.idFilial",
			                	  relatedProperty:"filialByIdFilialOrigem.idFilial",
			                	  blankFill:false
			                  },
			                  {
			                	  modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
			                	  relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
			                	  blankFill:false
			                  }
			                  ]
		}
};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = CTE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var CRT_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"CRT", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialCRT",
									url:"/municipios/manterFiliais.do",
									propertyMappings:[
										{ 
						 					modelProperty:"sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
											inlineQuery:true
										}, 
										{
											modelProperty:"sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial"
										},
										{ 
											modelProperty:"pessoa.nmFantasia", 
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
										}
									]
								}, 
								documentNumber:{
									property:"ctoInternacional", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrCrt", 
									service:"lms.expedicao.ctoInternacionalService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberCRT",
									url:"/expedicao/manterCRT.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{
											modelProperty:"nrCrt", 
											criteriaProperty:"ctoInternacional.nrCrt", 
											inlineQuery:true
										}, 
										{
											modelProperty:"nrCrt", 
											relatedProperty:"ctoInternacional.nrCrt",
											disabled:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
											criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											inlineQuery:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
										
									]
								}
							};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = CRT_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var NFT_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"NFT", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialCTR",
									url:"/municipios/manterFiliais.do",
									propertyMappings:[
										{ 
						 					modelProperty:"sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
											inlineQuery:true
										}, 
										{
											modelProperty:"sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial"
										},
										{ 
											modelProperty:"pessoa.nmFantasia", 
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
										}
									]
								}, 
								documentNumber:{
									property:"conhecimento", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrConhecimento", 
									service:"lms.expedicao.conhecimentoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberNFT",
									url:"/expedicao/pesquisarConhecimento.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{ 
											modelProperty:"nrConhecimento", 
											criteriaProperty:"conhecimento.nrConhecimento",
											inlineQuery:true
										},
										{ 
											modelProperty:"tpDocumentoServico", 
											criteriaProperty:"tpDocumentoServico",
											inlineQuery:true
										},
										{ 
											modelProperty:"nrConhecimento", 
											relatedProperty:"conhecimento.nrConhecimento",
											disabled:false
										},
										
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
											criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											inlineQuery:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
									]
								}
							};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = NFT_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var NTE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"NTE", 
		filial:{
			property:"filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialNTE",
			url:"/municipios/manterFiliais.do",
			propertyMappings:[
				{ 
 					modelProperty:"sgFilial", 
					criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
					inlineQuery:true
				}, 
				{
					modelProperty:"sgFilial", 
					relatedProperty:"filialByIdFilialOrigem.sgFilial"
				},
				{ 
					modelProperty:"pessoa.nmFantasia", 
					relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
				}
			]
		}, 
		documentNumber:{
			property:"conhecimento", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"nrConhecimento", 
			service:"lms.expedicao.conhecimentoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberNTE",
			url:"/expedicao/pesquisarConhecimento.do", 
			maxChars:"8", 
			mask:"00000000",
			propertyMappings:[
				{ 
					modelProperty:"nrConhecimento", 
					criteriaProperty:"conhecimento.nrConhecimento",
					inlineQuery:true
				},
				{ 
					modelProperty:"tpDocumentoServico", 
					criteriaProperty:"tpDocumentoServico",
					inlineQuery:true
				},
				{ 
					modelProperty:"nrConhecimento", 
					relatedProperty:"conhecimento.nrConhecimento",
					disabled:false
				},
				
				{ 
					modelProperty:"filialByIdFilialOrigem.idFilial", 
					criteriaProperty:"filialByIdFilialOrigem.idFilial",
					inlineQuery:true
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.sgFilial", 
					criteriaProperty:"filialByIdFilialOrigem.sgFilial",
					inlineQuery:true
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
					criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
					inlineQuery:false
				},
				{
					modelProperty:"filialByIdFilialOrigem.sgFilial", 
					relatedProperty:"filialByIdFilialOrigem.sgFilial",
					blankFill:false
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.idFilial",
					relatedProperty:"filialByIdFilialOrigem.idFilial",
					blankFill:false
				},
				{ 
					modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
					relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
					blankFill:false
				}
			]
		}
	};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = NTE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;
								
var NFS_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"NFS", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialNFS",
									url:"/municipios/manterFiliais.do",
									propertyMappings:[
										{ 
						 					modelProperty:"sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
											inlineQuery:true
										}, 
										{
											modelProperty:"sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial"
										},
										{ 
											modelProperty:"pessoa.nmFantasia", 
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
										}
									]
								}, 
								documentNumber:{
									property:"notaFiscalServico", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrNotaFiscalServico", 
									service:"lms.expedicao.notaFiscalServicoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberNFS",
									url:"/expedicao/digitarPreNotaFiscalServico.do", 
									maxChars:"8", 
									mask:"00000000", 
									propertyMappings:[
										{ 
											modelProperty:"nrNotaFiscalServico", 
											criteriaProperty:"notaFiscalServico.nrNotaFiscalServico", 
											inlineQuery:true
										}, 
										{
											modelProperty:"nrNotaFiscalServico", 
											relatedProperty:"notaFiscalServico.nrNotaFiscalServico",
											disabled:false
										},
										{ 
											modelProperty:"filial.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filial.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filial.pessoa.nmFantasia", 
											criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											inlineQuery:false
										},
										{
											modelProperty:"filial.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filial.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filial.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
									]
								}
							};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = NFS_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var NSE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
		type:"NSE", 
		filial:{
			property:"filialByIdFilialOrigem", 
			idProperty:"idFilial", 
			criteriaProperty:"sgFilial", 
			service:"lms.municipios.filialService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentFilialNFS",
			url:"/municipios/manterFiliais.do",
			propertyMappings:[
				{ 
 					modelProperty:"sgFilial", 
					criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
					inlineQuery:true
				}, 
				{
					modelProperty:"sgFilial", 
					relatedProperty:"filialByIdFilialOrigem.sgFilial"
				},
				{ 
					modelProperty:"pessoa.nmFantasia", 
					relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
				}
			]
		}, 
		documentNumber:{
			property:"notaFiscalServico", 
			idProperty:"idDoctoServico", 
			criteriaProperty:"nrNotaFiscalServico", 
			service:"lms.expedicao.notaFiscalServicoService.findLookup", 
			actionServiceMethod:"findLookupServiceDocumentNumberNFS",
			url:"/expedicao/digitarPreNotaFiscalServico.do", 
			maxChars:"8", 
			mask:"00000000", 
			propertyMappings:[
				{ 
					modelProperty:"nrNotaFiscalServico", 
					criteriaProperty:"notaFiscalServico.nrNotaFiscalServico", 
					inlineQuery:true
				}, 
				{
					modelProperty:"nrNotaFiscalServico", 
					relatedProperty:"notaFiscalServico.nrNotaFiscalServico",
					disabled:false
				},
				{ 
					modelProperty:"filial.idFilial", 
					criteriaProperty:"filialByIdFilialOrigem.idFilial",
					inlineQuery:true
				},
				{ 
					modelProperty:"filial.sgFilial", 
					criteriaProperty:"filialByIdFilialOrigem.sgFilial",
					inlineQuery:true
				},
				{ 
					modelProperty:"filial.pessoa.nmFantasia", 
					criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
					inlineQuery:false
				},
				{
					modelProperty:"filial.sgFilial", 
					relatedProperty:"filialByIdFilialOrigem.sgFilial",
					blankFill:false
				},
				{ 
					modelProperty:"filial.idFilial",
					relatedProperty:"filialByIdFilialOrigem.idFilial",
					blankFill:false
				},
				{ 
					modelProperty:"filial.pessoa.nmFantasia",
					relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
					blankFill:false
				}
			]
		}
	};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = NSE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var MDA_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"MDA", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialMDA",
									url:"/municipios/manterFiliais.do",
									propertyMappings:[
										{ 
						 					modelProperty:"sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
											inlineQuery:true
										}, 
										{
											modelProperty:"sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial"
										},
										{ 
											modelProperty:"pessoa.nmFantasia", 
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
										}
									]
								}, 
								documentNumber:{
									property:"mda", 
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
											criteriaProperty:"mda.nrDoctoServico", 
											inlineQuery:true
										}, 
										{
											modelProperty:"nrDoctoServico", 
											relatedProperty:"mda.nrDoctoServico",
											disabled:false
										},
																				
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
										
									]
								}
							};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = MDA_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var RRE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"RRE", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialRRE",
									url:"/municipios/manterFiliais.do",
									propertyMappings:[
										{ 
						 					modelProperty:"sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial", 
											inlineQuery:true
										}, 
										{
											modelProperty:"sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial"
										},
										{ 
											modelProperty:"pessoa.nmFantasia", 
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia"
										}
									]
								}, 
								documentNumber:{
									property:"reciboReembolso", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrReciboReembolso", 
									service:"lms.entrega.reciboReembolsoService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberRRE",
									url:"/entrega/manterReembolsos.do", 
									maxChars:"8", 
									mask:"00000000",
									propertyMappings:[
										{ 
						 					modelProperty:"nrReciboReembolso", 
											criteriaProperty:"reciboReembolso.nrReciboReembolso", 
											inlineQuery:true
										}, 
										{
											modelProperty:"nrReciboReembolso", 
											relatedProperty:"reciboReembolso.nrReciboReembolso",
											disabled:false
										},
										
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
											criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											inlineQuery:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
									]
								}
							};							
							
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = RRE_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;

var NDN_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION = {
								type:"NDN", 
								filial:{
									property:"filialByIdFilialOrigem", 
									idProperty:"idFilial", 
									criteriaProperty:"sgFilial", 
									service:"lms.municipios.filialService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentFilialNDN",
									url:"/municipios/manterFiliais.do"
								}, 
								documentNumber:{
									property:"notaDebitoNacional", 
									idProperty:"idDoctoServico", 
									criteriaProperty:"nrNotaDebitoNac", 
									service:"lms.contasreceber.notaDebitoNacionalService.findLookup", 
									actionServiceMethod:"findLookupServiceDocumentNumberNDN",
									url:"/contasReceber/manterNotasDebitoNacionais.do", 
									mask:"0000000000",
									maxChars:"10", 
									propertyMappings:[
										{ 
											modelProperty:"nrNotaDebitoNac", 
											criteriaProperty:"notaDebitoNacional.nrNotaDebitoNac", 
											inlineQuery:true
										}, 
										{
											modelProperty:"nrNotaDebitoNac", 
											relatedProperty:"notaDebitoNacional.nrNotaDebitoNac",
											disabled:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial", 
											criteriaProperty:"filialByIdFilialOrigem.idFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											criteriaProperty:"filialByIdFilialOrigem.sgFilial",
											inlineQuery:true
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", 
											criteriaProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											inlineQuery:false
										},									
										{
											modelProperty:"filialByIdFilialOrigem.sgFilial", 
											relatedProperty:"filialByIdFilialOrigem.sgFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.idFilial",
											relatedProperty:"filialByIdFilialOrigem.idFilial",
											blankFill:false
										},
										{ 
											modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											relatedProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",
											blankFill:false
										}
									]
								}
							};
doctoserviceDocumentWidgetDefinitions[doctoserviceDocumentWidgetDefinitions.length] = NDN_DOCTOSERVICE_DOCUMENT_WIDGET_DEFINITION;