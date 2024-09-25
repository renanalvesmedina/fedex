package com.mercurio.lms.indenizacoes.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.indenizacoes.report.EmitirRelatorioIndenizacoesExcelService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.seguros.model.TipoSinistro;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.seguros.model.service.TipoSeguroService;
import com.mercurio.lms.seguros.model.service.TipoSinistroService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

public class EmitirRelatorioIndenizacoesExcelAction extends ReportActionSupport {

	private RegionalService regionalService;
	private FilialService filialService;
	private UsuarioLMSService usuarioLMSService;
	private NaturezaProdutoService naturezaProdutoService;
	private ProcessoSinistroService processoSinistroService;
	private TipoSeguroService tipoSeguroService;
	private TipoSinistroService tipoSinistroService;
	private MotivoAberturaNcService motivoAberturaNcService;
	private PessoaService pessoaService;
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private ServicoService servicoService;
	
	public TypedFlatMap findDefaultValues() {
		TypedFlatMap map = new TypedFlatMap();
		map.put("dtEmissaoInicial", JTFormatUtils.format(JTDateTimeUtils.getDataAtual().minusDays(30)));
		map.put("dtEmissaoFinal", JTFormatUtils.format(JTDateTimeUtils.getDataAtual()));

		return map;
	}

	@SuppressWarnings("rawtypes")
	public List findComboRegional(TypedFlatMap tfm) {
		FilterList filter = new FilterList(getRegionalService().findRegionaisVigentesPorPeriodo(JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual())) {
			public Map filterItem(Object item) {
				Regional regional = (Regional) item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idRegional", regional.getIdRegional());
				typedFlatMap.put("siglaDescricao", regional.getSgRegional() + " - " + regional.getDsRegional());
				return typedFlatMap;
			}
		};
		return (List) filter.doFilter();
	}

	/**
	 * Consulta a filial pela sigla informada
	 */
	public List findLookupFilial(Map criteria) {
		FilterList filter = new FilterList(this.getFilialService().findLookup(criteria)) {
			public Map filterItem(Object item) {
				Filial filial = (Filial) item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idFilial", filial.getIdFilial());
				typedFlatMap.put("sgFilial", filial.getSgFilial());
				typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List) filter.doFilter();
	}

	/**
	 * Busca os usuários para a lookup de funcionário
	 * 
	 * @param tfm
	 *            Critérios de pesquisa como usuario.idUsuario e outros dados
	 *            como usuario.nmUsuario
	 * @return Lista de funcionários
	 */
	public List findLookupUsuarioFuncionario(TypedFlatMap tfm) {
		ConsultarUsuarioLMSParam cup = new ConsultarUsuarioLMSParam();

		cup.setNrMatricula(tfm.getString("nrMatricula"));
		cup.setNmUsuario(tfm.getString("nmUsuario"));
		cup.setTpCategoriaUsuario(tfm.getString("tpCategoriaUsuario"));

		return usuarioLMSService.findLookupSistema(cup);
	}

	public List findNaturezaProduto(Map criteria) {
		List retorno = new ArrayList();
		List listNaturezasProduto = naturezaProdutoService.find(criteria);
		for (Iterator iter = listNaturezasProduto.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			NaturezaProduto naturezaProduto = (NaturezaProduto) iter.next();
			map.put("idNaturezaProduto", naturezaProduto.getIdNaturezaProduto());
			map.put("dsNaturezaProduto", naturezaProduto.getDsNaturezaProduto());
			map.put("tpSituacao", naturezaProduto.getTpSituacao());
			retorno.add(map);
		}
		return retorno;
	}

	public List findLookupProcessoSinistro(TypedFlatMap tfm) {
		return this.processoSinistroService.findLookup(tfm);
	}

	/**
	 * Busca os dados da combo de Tipo de Seguro
	 */
	public List findComboTipoSeguro(Map criteria) {
		FilterList filter = new FilterList(this.getTipoSeguroService().findOrderBySgTipo(criteria)) {
			public Map filterItem(Object item) {
				TipoSeguro tipoSeguro = (TipoSeguro) item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idTipoSeguro", tipoSeguro.getIdTipoSeguro());
				typedFlatMap.put("sgTipo", tipoSeguro.getSgTipo());
				typedFlatMap.put("tpSituacao", tipoSeguro.getTpSituacao());
				return typedFlatMap;
			}
		};
		return (List) filter.doFilter();
	}

	/**
	 * Busca os dados da combo de Tipo de Sinistro
	 */
	public List findComboTipoSinistro(Map criteria) {
		FilterList filter = new FilterList(this.getTipoSinistroService().findOrderByDsTipo(criteria)) {
			public Map filterItem(Object item) {
				TipoSinistro tipoSinistro = (TipoSinistro) item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idTipoSinistro", tipoSinistro.getIdTipoSinistro());
				typedFlatMap.put("dsTipo", tipoSinistro.getDsTipo());
				typedFlatMap.put("tpSituacao", tipoSinistro.getTpSituacao());
				return typedFlatMap;
			}
		};
		return (List) filter.doFilter();
	}

    public List findComboMotivoAberturaNc(Map criteria) {
    	if (criteria==null) {
    		criteria = new HashMap(1);
    	}
    	
    	criteria.put("blPermiteIndenizacao", Boolean.TRUE);
    	FilterList filter = new FilterList(getMotivoAberturaNcService().findOrderByDsMotivoAbertura(criteria)) {
			public Map filterItem(Object item) {
				MotivoAberturaNc maNC = (MotivoAberturaNc)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idMotivoAberturaNc", maNC.getIdMotivoAberturaNc());
	    		typedFlatMap.put("dsMotivoAbertura", maNC.getDsMotivoAbertura());
	    		typedFlatMap.put("tpSituacao", maNC.getTpSituacao());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }
    
    public List findLookupPessoa(TypedFlatMap tfm) {
    	Map map = new HashMap(1);
    	map.put("nrIdentificacao", tfm.getString("pessoa.nrIdentificacao"));

    	return getPessoaService().findLookup(map);
    }
    
    /***************************************************************************************
     * Documento Servico Methods
     ***************************************************************************************/
    public List findTipoDocumentoServico(Map criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("CTR");
    	dominiosValidos.add("CRT");
    	dominiosValidos.add("NFT");
    	dominiosValidos.add("NFS");
    	dominiosValidos.add("NTE");
    	dominiosValidos.add("NSE");
    	dominiosValidos.add("CTE");
    	List retorno = this.domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
    	return retorno;
    }
    
    /**
     * FindLookup para filial do tipo de DoctoServico Escolhido.
     */ 
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	List list = this.filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
    
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFS(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNSE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    
    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupDoctoServico(List list) {
    	
    	if (list!=null && list.size()==1) {
    		DoctoServico doctoServico = (DoctoServico)list.remove(0);
    		Filial filial = filialService.findById(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
    		TypedFlatMap result = new TypedFlatMap();
    		result.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
    		result.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
    		result.put("doctoServico.filialByIdFilialOrigem.idFilial", filial.getIdFilial());    		
    		result.put("doctoServico.filialByIdFilialOrigem.sgFilial", filial.getSgFilial());
    		list.add(result);
    	}    	
    	return list;    	
    }
    
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNSE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNFS(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria); 
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	List retorno = ctoInternacionalService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }    
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	List retorno = mdaService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	List retorno = reciboReembolsoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

   	public List findComboServico(Map criteria) {
   		FilterList filter = new FilterList(servicoService.find(criteria)) {
			public Map filterItem(Object item) {
				Servico servico = (Servico) item;
				TypedFlatMap map = new TypedFlatMap();
				map.put("idServico", servico.getIdServico());
				map.put("dsServico", servico.getDsServico());
				map.put("tpSituacao.value", servico.getTpSituacao().getValue());
				return map;
			}
		};
		return (List) filter.doFilter();
	}

	public void setEmitirRelatorioIndenizacoesExcelService(EmitirRelatorioIndenizacoesExcelService emitirRelatorioIndenizacoesExcel) {
		this.reportServiceSupport = emitirRelatorioIndenizacoesExcel;
	}
	
	public RegionalService getRegionalService() {
		return regionalService;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void setProcessoSinistroService(ProcessoSinistroService processoSinistroService) {
		this.processoSinistroService = processoSinistroService;
	}

	public TipoSeguroService getTipoSeguroService() {
		return tipoSeguroService;
	}

	public void setTipoSeguroService(TipoSeguroService tipoSeguroService) {
		this.tipoSeguroService = tipoSeguroService;
	}

	public TipoSinistroService getTipoSinistroService() {
		return tipoSinistroService;
	}

	public void setTipoSinistroService(TipoSinistroService tipoSinistroService) {
		this.tipoSinistroService = tipoSinistroService;
	}

	public MotivoAberturaNcService getMotivoAberturaNcService() {
		return motivoAberturaNcService;
	}

	public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
		this.motivoAberturaNcService = motivoAberturaNcService;
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public CtoInternacionalService getCtoInternacionalService() {
		return ctoInternacionalService;
	}

	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public MdaService getMdaService() {
		return mdaService;
	}

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	public ReciboReembolsoService getReciboReembolsoService() {
		return reciboReembolsoService;
	}

	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	public NaturezaProdutoService getNaturezaProdutoService() {
		return naturezaProdutoService;
	}

	public ProcessoSinistroService getProcessoSinistroService() {
		return processoSinistroService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

}