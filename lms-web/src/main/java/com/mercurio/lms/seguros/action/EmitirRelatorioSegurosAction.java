package com.mercurio.lms.seguros.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contasreceber.model.service.NotaDebitoNacionalService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.RodoviaService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.seguros.model.TipoSinistro;
import com.mercurio.lms.seguros.model.service.RelatorioSegurosService;
import com.mercurio.lms.seguros.model.service.TipoSeguroService;
import com.mercurio.lms.seguros.model.service.TipoSinistroService;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.emitirRelatorioSegurosAction"
 */
public class EmitirRelatorioSegurosAction {

	private DomainValueService domainValueService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private MeioTransporteService meioTransporteService;
	private TipoSinistroService tipoSinistroService;
	private TipoSeguroService tipoSeguroService;
	private RodoviaService rodoviaService;
	private MunicipioService municipioService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoEntregaService manifestoEntregaService;
	private ManifestoInternacionalService manifestoInternacionalService; 
	private UnidadeFederativaService unidadeFederativaService;
	private AeroportoService aeroportoService;
	private UsuarioLMSService usuarioLMSService;
	private ManifestoService manifestoService;
	private NaturezaProdutoService naturezaProdutoService;
	private MotivoAberturaNcService motivoAberturaNcService;
	private PessoaService pessoaService;
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConhecimentoService conhecimentoService;
	private NotaFiscalServicoService notaFiscalServicoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private NotaDebitoNacionalService notaDebitoNacionalService;
	private ServicoService servicoService;
	private RelatorioSegurosService relatorioSegurosService;
	private ReportExecutionManager reportExecutionManager;
	
	/**
	 * COMBOS & LOOKUPS 
	 */
    public List findTipoManifesto() {
    	List<String>dominiosValidos = new ArrayList<String>();
    	dominiosValidos.add("EN");
    	dominiosValidos.add("VN");
    	dominiosValidos.add("VI");
    	dominiosValidos.add("EP");
    	return this.domainValueService.findByDomainNameAndValues("DM_TAG_MANIFESTO", dominiosValidos);
    }
    
    public List findLookupManifestoDocumentFilialEN(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }

    public List findLookupManifestoDocumentFilialVN(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }

    public List findLookupManifestoDocumentFilialVI(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }
    
    public List findLookupManifestoDocumentFilialEP(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }
    
    private List findLookupFilialByManifesto(Map criteria) {
    	List list = this.filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idFilial", filial.getIdFilial());
    		tfm.put("sgFilial", filial.getSgFilial());
    		tfm.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(tfm);
    	}
    	return retorno;
    }
    
    public List findLookupManifestoDocumentNumberEN(TypedFlatMap criteria) {
    	criteria.put("tpManifestoEntrega", "EN");
    	criteria.put("manifesto.tpManifesto", "E");
    	List list = manifestoEntregaService.findLookupByTagManifesto(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		ManifestoEntrega manifestoEntrega = (ManifestoEntrega)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idManifestoEntrega", manifestoEntrega.getIdManifestoEntrega());
    		typedFlatMap.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
    		typedFlatMap.put("idFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getIdFilial());
    		typedFlatMap.put("sgFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
    		typedFlatMap.put("nmFantasiaFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    public List findLookupManifestoDocumentNumberEP(TypedFlatMap criteria) {
    	criteria.put("tpManifestoEntrega", "EP");
    	criteria.put("manifesto.tpManifesto", "E");
    	List list = manifestoEntregaService.findLookupByTagManifesto(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		ManifestoEntrega manifestoEntrega = (ManifestoEntrega)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idManifestoEntrega", manifestoEntrega.getIdManifestoEntrega());
    		typedFlatMap.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
    		typedFlatMap.put("idFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getIdFilial());
    		typedFlatMap.put("sgFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
    		typedFlatMap.put("nmFantasiaFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
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
    
    public List findComboMotivoAberturaNc(Map criteria) {
    	if (criteria==null) {
    		criteria = new HashMap(1);
    	}
    	
    	criteria.put("blPermiteIndenizacao", Boolean.TRUE);
    	FilterList filter = new FilterList(this.motivoAberturaNcService.findOrderByDsMotivoAbertura(criteria)) {
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

    	return this.pessoaService.findLookup(map);
    }
    
	public List findClienteLookup(Map criteria) {
		return this.clienteService.findLookup(criteria);
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
	
    public List findLookupManifestoDocumentNumberVN(Map criteria) {
    	Map manifesto = (Map)criteria.get("manifesto");
    	manifesto.put("tpManifesto", "V");
    	manifesto.put("tpAbrangencia", "N");
    	return this.manifestoViagemNacionalService.findLookup(criteria);
    }

    public List findLookupManifestoDocumentNumberVI(Map criteria) {
    	Map manifesto = (Map)criteria.get("manifesto");
    	manifesto.put("tpManifesto", "V");
    	manifesto.put("tpAbrangencia", "I");
    	return this.manifestoInternacionalService.findLookup(criteria);
    }
    
    public List findManifestoByRNC(Long idManifesto) {
    	return this.manifestoService.findManifestoByRNC(idManifesto); 
    }
    
    public List findLookupFilial(TypedFlatMap map) {
    	return this.filialService.findLookupBySgFilial((String)map.get("sgFilial"), (String)map.get("tpAcesso"));
    }
	
	public List findLookupControleCarga(TypedFlatMap map) {
		return this.controleCargaService.findLookup(map);
	}
	
    /**
     * Retorna a lista de tipos de docto de serviço para
     * combo de docto de serviço
     * @return
     */
	public List findTpDoctoServico() {
		String tpDocFat = (String)configuracoesFacade.getValorParametro("TP_DOCTO_SEG");

		String[] dm = tpDocFat.split(";");
        List dominiosValidos = Arrays.asList(dm);
	    List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
	    
		return retorno;
	}
	
	/**
     * FindLookup para filial do tipo de DoctoServico Escolhido.
     */ 
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
	
    public List findLookupServiceDocumentFilialNDN(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
	/** 
     * Busca a filial baseado no documento de serviço
     * @param criteria
     * @return
     */
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	
    	FilterList filter = new FilterList(filialService.findLookup(criteria)) {
			public Map filterItem(Object item) {
	    			Filial filial = (Filial)item;
	    			TypedFlatMap typedFlatMap = new TypedFlatMap();
		    		typedFlatMap.put("idFilial", filial.getIdFilial());
			    	typedFlatMap.put("sgFilial", filial.getSgFilial());
				return typedFlatMap;
			}
    	};
    	
    	return (List)filter.doFilter();
    }
    
    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	return this.conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNSE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFS(Map criteria) {
    	return notaFiscalServicoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	return this.conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	return this.ctoInternacionalService.findLookup(criteria);
    }    
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	return this.mdaService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	return this.reciboReembolsoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	return this.conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNDN(Map map) {
        return this.notaDebitoNacionalService.findLookup(map);
     }
	public List findLookupMeioTransporteRodoviario(Map map) {
		return this.meioTransporteService.findLookup(map);
	}
	
    /**
     * lookup de veiculo 
     * @param map
     * @return
     */
    public List findLookupMeioTransporte(Map map) {
    	return meioTransporteService.findLookup(map);
    }
    
	public List findComboTipoProcessoSinistro(TypedFlatMap tfm) {
		List<TypedFlatMap>retorno = new ArrayList<TypedFlatMap>();
		List tiposSinistro = this.tipoSinistroService.findOrderByDsTipo(tfm); 
		
		if (tiposSinistro != null && !tiposSinistro.isEmpty()) {
			for (Iterator iter = tiposSinistro.iterator(); iter.hasNext();) {
				TypedFlatMap map = new TypedFlatMap();
				TipoSinistro tipoSinistro = (TipoSinistro) iter.next();
				map.put("idTipoSinistro", tipoSinistro.getIdTipoSinistro());
	   			map.put("dsTipo", tipoSinistro.getDsTipo());
	   			map.put("tpSituacao.value", tipoSinistro.getTpSituacao().getValue());
	   			retorno.add(map);
			}
		}
		
		return retorno;
	}
	
	public List findComboTipoSeguro(TypedFlatMap tfm) {
		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
		List tiposSeguro = this.tipoSeguroService.findOrderBySgTipo(tfm); 
		for (Iterator iter = tiposSeguro.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			TipoSeguro tipoSeguro = (TipoSeguro) iter.next();
			map.put("idTipoSeguro", tipoSeguro.getIdTipoSeguro());
			map.put("sgTipo", tipoSeguro.getSgTipo());
			map.put("tpSituacao.value", tipoSeguro.getTpSituacao().getValue());			
			retorno.add(map);
		}
		return retorno;
	}
    
	public List findLookupRodovia(TypedFlatMap tfm) {
		return this.rodoviaService.findLookup(tfm);
	}
	
    public List findLookupMunicipio(TypedFlatMap map) {
    	return this.municipioService.findLookup(map);
    }
	
	public List findLookupUnidadeFederativa(Map criteria){
		return this.unidadeFederativaService.findLookup(criteria);
	}
	
	public List findLookupAeroporto(TypedFlatMap tfm) {
		return this.aeroportoService.findLookupAeroporto(tfm);
	}
    
	public List findLookupUsuarioFuncionario(TypedFlatMap tfm){

		ConsultarUsuarioLMSParam cup = new ConsultarUsuarioLMSParam();

		cup.setNrMatricula(tfm.getString("nrMatricula"));
		cup.setNmUsuario(tfm.getString("nmUsuario"));
		cup.setTpCategoriaUsuario(tfm.getString("tpCategoriaUsuario"));

		return usuarioLMSService.findLookupSistema(cup);
	}
	
	/**
	 * Geração do relatório CSV
	 * 
	 * Jira LMS-6158
	 * 
	 * @param params Campos da tela
	 * @return
	 * @throws Exception 
	 */
	public String executeReport(TypedFlatMap params) {
		try {
			return reportExecutionManager.generateReportLocator(relatorioSegurosService.executeReportCSV
					(params, reportExecutionManager.generateOutputDir()));
		} catch (BusinessException e) {
        	throw e;
		} catch (Exception e) {
			throw new ADSMException(e);
		}
	}
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setTipoSinistroService(TipoSinistroService tipoSinistroService) {
		this.tipoSinistroService = tipoSinistroService;
	}

	public void setTipoSeguroService(TipoSeguroService tipoSeguroService) {
		this.tipoSeguroService = tipoSeguroService;
	}

	public void setRodoviaService(RodoviaService rodoviaService) {
		this.rodoviaService = rodoviaService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setManifestoViagemNacionalService(
			ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}

	public void setManifestoEntregaService(
			ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public void setManifestoInternacionalService(
			ManifestoInternacionalService manifestoInternacionalService) {
		this.manifestoInternacionalService = manifestoInternacionalService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
		this.motivoAberturaNcService = motivoAberturaNcService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	public void setNotaDebitoNacionalService(NotaDebitoNacionalService notaDebitoNacionalService) {
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public void setRelatorioSegurosService(
			RelatorioSegurosService relatorioSegurosService) {
		this.relatorioSegurosService = relatorioSegurosService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
}
