package com.mercurio.lms.rnc.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.CaractProdutoOcorrencia;
import com.mercurio.lms.rnc.model.CaracteristicaProduto;
import com.mercurio.lms.rnc.model.CausaNaoConformidade;
import com.mercurio.lms.rnc.model.DescricaoPadraoNc;
import com.mercurio.lms.rnc.model.FotoOcorrencia;
import com.mercurio.lms.rnc.model.ItemOcorrenciaNc;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.CaractProdutoOcorrenciaService;
import com.mercurio.lms.rnc.model.service.CaracteristicaProdutoService;
import com.mercurio.lms.rnc.model.service.CausaNaoConformidadeService;
import com.mercurio.lms.rnc.model.service.DescricaoPadraoNcService;
import com.mercurio.lms.rnc.model.service.FotoOcorrenciaService;
import com.mercurio.lms.rnc.model.service.ItemOcorrenciaNcService;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.manterOcorrenciasNaoConformidadeAction"
 */

public class ManterOcorrenciasNaoConformidadeAction {
	
	private DomainValueService domainValueService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private CaracteristicaProdutoService caracteristicaProdutoService;
	private FilialService filialService;
	private MotivoAberturaNcService motivoAberturaNcService;
	private ControleCargaService controleCargaService;
	private EmpresaService empresaService;
	private DescricaoPadraoNcService descricaoPadraoNcService;
	private ClienteService clienteService;
	private CausaNaoConformidadeService causaNaoConformidadeService;
	private MoedaService moedaService;
	private DoctoServicoService doctoServicoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ManifestoService manifestoService;
	private ManifestoEntregaService manifestoEntregaService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoInternacionalService manifestoInternacionalService;
	private FotoOcorrenciaService fotoOcorrenciaService;
	private CaractProdutoOcorrenciaService caractProdutoOcorrenciaService;
	private NaoConformidadeService naoConformidadeService;
	private ItemOcorrenciaNcService itemOcorrenciaNcService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private ConfiguracoesFacade configuracoesFacade;

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}
	
	public void setItemOcorrenciaNcService(ItemOcorrenciaNcService itemOcorrenciaNcService) {
		this.itemOcorrenciaNcService = itemOcorrenciaNcService;
	}
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public void setCaracteristicaProdutoService(CaracteristicaProdutoService caracteristicaProdutoService) {
		this.caracteristicaProdutoService = caracteristicaProdutoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
		this.motivoAberturaNcService = motivoAberturaNcService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setDescricaoPadraoNcService(DescricaoPadraoNcService descricaoPadraoNcService) {
		this.descricaoPadraoNcService = descricaoPadraoNcService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setCausaNaoConformidadeService(CausaNaoConformidadeService causaNaoConformidadeService) {
		this.causaNaoConformidadeService = causaNaoConformidadeService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public void setManifestoInternacionalService(ManifestoInternacionalService manifestoInternacionalService) {
		this.manifestoInternacionalService = manifestoInternacionalService;
	}
	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	public void setCaractProdutoOcorrenciaService(CaractProdutoOcorrenciaService caractProdutoOcorrenciaService) {
		this.caractProdutoOcorrenciaService = caractProdutoOcorrenciaService;
	}
	public void setFotoOcorrenciaService(FotoOcorrenciaService fotoOcorrenciaService) {
		this.fotoOcorrenciaService = fotoOcorrenciaService;
	}

	public void removeById(java.lang.Long id) {
		ocorrenciaNaoConformidadeService.removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	ocorrenciaNaoConformidadeService.removeByIds(ids);
    }

    public TypedFlatMap findById(java.lang.Long id) {
    	TypedFlatMap tfm = ocorrenciaNaoConformidadeService.findOcorrenciaNaoConformidadeById(id);
    	
    	Long idNaoConformidade = tfm.getLong("naoConformidade.idNaoConformidade");
    	NaoConformidade nc = getNaoConformidadeService().findById(idNaoConformidade);
    	if (nc != null && nc.getTpStatusNaoConformidade() != null)
    		tfm.put("naoConformidade.tpStatusNaoConformidade", nc.getTpStatusNaoConformidade().getValue());
    	
    	List listaNotas = tfm.getList("notaOcorrenciaNcs");
        if (listaNotas != null && !listaNotas.isEmpty()) {
        	Map mapNotaOcorrenciaNcs = (Map)listaNotas.iterator().next();
       		Map mapNotaFiscalConhecimento = (Map)mapNotaOcorrenciaNcs.get("notaFiscalConhecimento");
       		if (mapNotaFiscalConhecimento == null) {
       			tfm.put("notaOcorrenciaNcs2", listaNotas);
       			tfm.put("notaOcorrenciaNcs", null);
        	}
        }
    	return tfm;
    }


    public ResultSetPage findPaginated(Map criteria) {
    	ResultSetPage rsp = ocorrenciaNaoConformidadeService.findPaginated(criteria);
    	List resultado = new ArrayList();
    	for (Iterator iter=rsp.getList().iterator(); iter.hasNext();) {
    		OcorrenciaNaoConformidade bean = (OcorrenciaNaoConformidade)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
	    	typedFlatMap.put("idOcorrenciaNaoConformidade", bean.getIdOcorrenciaNaoConformidade());
	    	typedFlatMap.put("nrOcorrenciaNc", bean.getNrOcorrenciaNc());
	    	typedFlatMap.put("motivoAberturaNc.dsMotivoAbertura", bean.getMotivoAberturaNc().getDsMotivoAbertura());
	    	typedFlatMap.put("filialByIdFilialResponsavel.sgFilial", bean.getFilialByIdFilialResponsavel().getSgFilial());
	    	typedFlatMap.put("filialByIdFilialResponsavel.pessoa.nmFantasia", bean.getFilialByIdFilialResponsavel().getPessoa().getNmFantasia());
			typedFlatMap.put("dsOcorrenciaNc", bean.getDsOcorrenciaNc());
			typedFlatMap.put("qtVolumes", bean.getQtVolumes());
			typedFlatMap.put("nrRncLegado", bean.getNrRncLegado());
			typedFlatMap.put("filialByIdFilialLegado.sgFilial", bean.getFilialByIdFilialLegado().getSgFilial());
			Moeda moeda = bean.getMoeda();
			if (moeda != null) {
				typedFlatMap.put("moeda.dsSimbolo", moeda.getDsSimbolo());
				typedFlatMap.put("moeda.sgMoeda", moeda.getSgMoeda());
			}
	    	typedFlatMap.put("vlOcorrenciaNc", bean.getVlOcorrenciaNc());
	    	typedFlatMap.put("tpStatusOcorrenciaNc", bean.getTpStatusOcorrenciaNc());
	    	resultado.add(typedFlatMap);
    	}
        rsp.setList(resultado);
    	return rsp;
    }

    public Integer getRowCount(Map criteria) {
    	return ocorrenciaNaoConformidadeService.getRowCount(criteria);
    }

    public List findLookupFilial(Map criteria) {
    	return filialService.findLookup(criteria);
    }
    
    /**
     * 
     * @param criteria
     * @return
     */
    public List findMotivoAberturaNc(Map criteria) {
    	List lista = motivoAberturaNcService.findOrderByDsMotivoAbertura(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
			MotivoAberturaNc maNC = (MotivoAberturaNc)iter.next();
			TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idMotivoAberturaNc", maNC.getIdMotivoAberturaNc());
    		tfm.put("dsMotivoAbertura", maNC.getDsMotivoAbertura().toString());
	    	tfm.put("blExigeDocServico", maNC.getBlExigeDocServico());
	    	tfm.put("blExigeValor", maNC.getBlExigeValor());
	    	tfm.put("blExigeQtdVolumes", maNC.getBlExigeQtdVolumes());
			retorno.add(tfm);
    	}
    	return retorno;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findLookupControleCarga(Map criteria) {
    	FilterList filter = new FilterList(controleCargaService.findLookup(criteria)) {
			public Map filterItem(Object item) {
				ControleCarga cc = (ControleCarga)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idControleCarga", cc.getIdControleCarga());
		    	typedFlatMap.put("nrControleCarga", cc.getNrControleCarga());
		    	typedFlatMap.put("filialByIdFilialOrigem.idFilial", cc.getFilialByIdFilialOrigem().getIdFilial());
		    	typedFlatMap.put("filialByIdFilialOrigem.sgFilial", cc.getFilialByIdFilialOrigem().getSgFilial());
		    	if (cc.getMeioTransporteByIdTransportado() != null) {
			    	typedFlatMap.put("meioTransporteByIdTransportado.nrFrota", cc.getMeioTransporteByIdTransportado().getNrFrota());
			    	typedFlatMap.put("meioTransporteByIdTransportado.nrIdentificador", cc.getMeioTransporteByIdTransportado().getNrIdentificador());
		    	}
		    	if (cc.getMeioTransporteByIdSemiRebocado() != null) {
			    	typedFlatMap.put("meioTransporteByIdSemiRebocado.nrFrota", cc.getMeioTransporteByIdSemiRebocado().getNrFrota());
			    	typedFlatMap.put("meioTransporteByIdSemiRebocado.nrIdentificador", cc.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
		    	}
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }

    public List findLookupEmpresa(Map criteria) {
    	return empresaService.findLookup(criteria);
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findDescricaoPadraoNc(Map criteria) {
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsPadraoNc:asc");
    	FilterList filter = new FilterList(descricaoPadraoNcService.findListByCriteria(criteria, campoOrdenacao)) {
			public Map filterItem(Object item) {
				DescricaoPadraoNc descricaoPadraoNc = (DescricaoPadraoNc)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idDescricaoPadraoNc", descricaoPadraoNc.getIdDescricaoPadraoNc());
		    	typedFlatMap.put("dsPadraoNc", descricaoPadraoNc.getDsPadraoNc());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }


    public List findLookupCliente(Map criteria) {
    	return clienteService.findLookup(criteria);
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findCausaNaoConformidade(Map criteria) {
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsCausaNaoConformidade:asc");
    	FilterList filter = new FilterList(causaNaoConformidadeService.findListByCriteria(criteria, campoOrdenacao)) {
			public Map filterItem(Object item) {
				CausaNaoConformidade causaNaoConformidade = (CausaNaoConformidade)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idCausaNaoConformidade", causaNaoConformidade.getIdCausaNaoConformidade());
		    	typedFlatMap.put("dsCausaNaoConformidade", causaNaoConformidade.getDsCausaNaoConformidade());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findMoeda(Map criteria) {
    	List list = moedaService.find(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Moeda moeda = (Moeda)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMoeda", moeda.getIdMoeda());
    		typedFlatMap.put("siglaSimbolo", moeda.getSiglaSimbolo());
    		typedFlatMap.put("sgMoeda", moeda.getSgMoeda());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    
    public TypedFlatMap getDataUsuario() {
    	Filial filial = SessionUtils.getFilialSessao();
    	Usuario usuario = SessionUtils.getUsuarioLogado();
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("filial.idFilial", filial.getIdFilial());
    	tfm.put("usuario.idUsuario", usuario.getIdUsuario());
    	tfm.put("usuario.nmUsuario", usuario.getNmUsuario());
		return tfm;
    }

    
    public Map findDomainValueDescription(Map criteria) {
    	String value = (String)criteria.get("value");
    	String domainName = (String)criteria.get("domainName");
    	Map map = new HashMap();
    	map.put("dominio", domainValueService.findDomainValueDescription(domainName, value));
        return map;
    }

    public Map findManifestoComControleCargas(Long idDoctoServico) {
    	return ocorrenciaNaoConformidadeService.findManifestoComControleCargas(idDoctoServico);
    }


    
    public TypedFlatMap store(TypedFlatMap map) {
		List notaOcorrenciaNcs = map.getList("notaOcorrenciaNcs");
		if (notaOcorrenciaNcs == null || notaOcorrenciaNcs.isEmpty()) {
			notaOcorrenciaNcs = map.getList("notaOcorrenciaNcs2");
		}

		List listaNotaOcorrenciaNcs = new ArrayList();
		if (notaOcorrenciaNcs != null) {
			for (Iterator iter = notaOcorrenciaNcs.iterator(); iter.hasNext();) {
				TypedFlatMap mapNotas = (TypedFlatMap)iter.next();
				Long idNotaFiscalConhecimento = mapNotas.getLong("notaFiscalConhecimento.idNotaFiscalConhecimento");
				if (idNotaFiscalConhecimento != null) {
					NotaFiscalConhecimento nfConhecimento = notaFiscalConhecimentoService.findById(idNotaFiscalConhecimento);
					NotaOcorrenciaNc notaOcorrenciaNc = new NotaOcorrenciaNc();
					notaOcorrenciaNc.setNrNotaFiscal( mapNotas.getInteger("nrNotaFiscal") );
					notaOcorrenciaNc.setNotaFiscalConhecimento(nfConhecimento);
					listaNotaOcorrenciaNcs.add(notaOcorrenciaNc);
				}
				else {
					NotaOcorrenciaNc notaOcorrenciaNc = new NotaOcorrenciaNc();
					notaOcorrenciaNc.setNrNotaFiscal( mapNotas.getInteger("nrNotaFiscal") );
					listaNotaOcorrenciaNcs.add(notaOcorrenciaNc);
				}
			}
		}

		OcorrenciaNaoConformidade onc = ocorrenciaNaoConformidadeService.storeOcorrenciaNaoConformidade(
				map.getLong("manifesto.idManifesto"), map.getLong("naoConformidade.doctoServico.idDoctoServico"), 
				map.getLong("motivoAberturaNc.idMotivoAberturaNc"), map.getLong("controleCarga.idControleCarga"), 
				map.getLong("empresa.idEmpresa"), map.getLong("descricaoPadraoNc.idDescricaoPadraoNc"), 
				map.getLong("filialByIdFilialResponsavel.idFilial"), map.getLong("causaNaoConformidade.idCausaNaoConformidade"),
				map.getLong("moeda.idMoeda"), map.getString("dsOcorrenciaNc"), map.getBoolean("blCaixaReaproveitada"),
				map.getString("dsCaixaReaproveitada"), map.getString("dsCausaNc"), map.getBigDecimal("vlOcorrenciaNc"),
				map.getInteger("qtVolumes"), listaNotaOcorrenciaNcs, map.getLong("naoConformidade.idNaoConformidade"));

		TypedFlatMap mapRetorno = new TypedFlatMap();
		mapRetorno.put("idOcorrenciaNaoConformidade", onc.getIdOcorrenciaNaoConformidade());
		mapRetorno.put("nrOcorrenciaNc", onc.getNrOcorrenciaNc());
		mapRetorno.put("nrRncLegado", onc.getNrRncLegado());
		mapRetorno.put("filialByIdFilialLegado.sgFilial", onc.getFilialByIdFilialLegado().getSgFilial());
		mapRetorno.put("dhInclusao", onc.getDhInclusao());
		
    	if("AGP".equals(onc.getNaoConformidade().getTpStatusNaoConformidade().getValue())){
    		mapRetorno.put("lmsMensagemAgp", "LMS-12021 - " + configuracoesFacade.getMensagem("LMS-12021"));
    	}
		
		return mapRetorno;
	}

    
    public TypedFlatMap findDataDoctoServico(Long idDoctoServico) {
    	return doctoServicoService.findDoctoServicoByTpDocumento(idDoctoServico);
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findNotaFiscalConhecimento(Map criteria) {
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("nrNotaFiscal:asc");
    	FilterList filter = new FilterList(notaFiscalConhecimentoService.findListByCriteria(criteria, campoOrdenacao)) {
			public Map filterItem(Object item) {
				NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());
		    	typedFlatMap.put("nrNotaFiscal", notaFiscalConhecimento.getNrNotaFiscal());
				return typedFlatMap;
			}
    	};
    	List retorno = (List)filter.doFilter();
    	populateListaNotas(retorno);
    	return retorno;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findNotaFiscalMda(TypedFlatMap criteria) {
    	Long idMda = criteria.getLong("mda.idDoctoServico");
    	FilterList filter = new FilterList(notaFiscalConhecimentoService.findListByCriteriaByMda(idMda)) {
			public Map filterItem(Object item) {
				NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());
		    	typedFlatMap.put("nrNotaFiscal", notaFiscalConhecimento.getNrNotaFiscal());
				return typedFlatMap;
			}
    	};
    	List retorno = (List)filter.doFilter();
    	populateListaNotas(retorno);
    	return retorno;
    }
    
    private void populateListaNotas(List lista) {
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		map.put("notaFiscalConhecimento.idNotaFiscalConhecimento", map.getLong("idNotaFiscalConhecimento"));
    		map.remove("idNotaFiscalConhecimento");
    	}
    }

    
    public List findManifestoByRNC(Long idManifesto) {
    	return manifestoService.findManifestoByRNC(idManifesto);
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
    
    public List findLookupFilialByManifesto(Map criteria) {
    	List list = filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
    
    public List findLookupManifestoDocumentNumberEN(TypedFlatMap criteria) {
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

    /**
     * Busca os itens da NFe recebendo por parametro o id da <code>OcorrenciaNaoConformidade</code>
     * 
     * @param criteria
     * @return
     * @author WagnerFC
     */
    @SuppressWarnings("unchecked")
	public TypedFlatMap loadItensNFe(TypedFlatMap param) {
    	TypedFlatMap criteria = new TypedFlatMap();
    	criteria.put("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", param.get("idOcorrenciaNaoConformidade"));
    	
    	List<ItemOcorrenciaNc> items = itemOcorrenciaNcService.find(criteria);

    	List<TypedFlatMap> nfe = new ArrayList<TypedFlatMap>();
    	List<String> chavesNfe = new ArrayList<String>();
    	String cpfCnpj = new String();
    	String notaFiscal;
    	
    	for (ItemOcorrenciaNc itemOcorrenciaNc : items) {    		
    		notaFiscal = itemOcorrenciaNc.getNrChave().substring(25, 34);
    		
    		NotaFiscalEdiItem  iNFe = buscaItemChaveNFe(itemOcorrenciaNc.getNrChave(), BigDecimal.valueOf(itemOcorrenciaNc.getNrItem()));
    		
			TypedFlatMap item = new TypedFlatMap();
			item.put("itensNFe.descricao", iNFe.getDescricaoItem());
			item.put("itensNFe.qtdeAnterior", iNFe.getQtdeItem());
			item.put("itensNFe.valor", iNFe.getVlTotalItem());
			item.put("itensNFe.notaFiscal", notaFiscal);
			item.put("itensNFe.item", itemOcorrenciaNc.getNrItem());
			item.put("itensNFe.qtdInformada", itemOcorrenciaNc.getQtInformada());
			item.put("itensNFe.qtdNaoConformidade", itemOcorrenciaNc.getQtNaoConformidade());
			item.put("itensNFe.vlNaoConformidade", itemOcorrenciaNc.getVlNaoConformidade());
			
			nfe.add(item);
    		
    		if(!chavesNfe.contains(itemOcorrenciaNc.getNrChave())) {
	    		cpfCnpj = itemOcorrenciaNc.getNrChave().substring(6, 20);
	    		chavesNfe.add(itemOcorrenciaNc.getNrChave());
    		}
		}
    	
    	if(!cpfCnpj.isEmpty()) {
	    	List cliente = clienteService.findLookupCliente(cpfCnpj);
	    	criteria.put("cliente", cliente.isEmpty() ? null : cliente.get(0));
    	}
    	else {
    		criteria.put("cliente", "");
    	}
    	
    	criteria.put("nfe", nfe);
    	criteria.put("chavesNFe", chavesNfe);
    	
    	return criteria;
    }
    
    private NotaFiscalEdiItem buscaItemChaveNFe(String chave, BigDecimal nrItem) {
    	List<NotaFiscalEdiItem> itensNFe = notaFiscalEletronicaService.findNfeItensByNrChave(chave);
    	
    	for (NotaFiscalEdiItem item : itensNFe) {
    		if(item.getNumeroItem().equals(nrItem.intValue())) {
    			return item;
    		}
    	}
    	
    	return null;
    }
    
    public List findLookupManifestoDocumentNumberVN(Map criteria) {
    	return manifestoViagemNacionalService.findLookup(criteria);
    }

    public List findLookupManifestoDocumentNumberVI(Map criteria) {
    	return manifestoInternacionalService.findLookup(criteria);
    }
    

    
    public FotoOcorrencia findByIdFotoOcorrencia(Long id) {
    	return fotoOcorrenciaService.findById(id);
    }

    public Serializable storeFotoOcorrencia(FotoOcorrencia bean) {
    	return fotoOcorrenciaService.storeFotoOcorrencia(bean);
    }
    
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsFotoOcorrencia(List ids) {
    	fotoOcorrenciaService.removeByIds(ids);
    }

    public ResultSetPage findPaginatedFotoOcorrencia(Map criteria) {
    	return fotoOcorrenciaService.findPaginated(criteria);
    }

    public Integer getRowCountFotoOcorrencia(Map criteria) {
    	return fotoOcorrenciaService.getRowCount(criteria);
    }

    
    
    public CaractProdutoOcorrencia findByIdCaractProdutoOcorrencia(Long id) {
    	return caractProdutoOcorrenciaService.findById(id);
    }

    public Serializable storeCaractProdutoOcorrencia(CaractProdutoOcorrencia bean) {
    	return caractProdutoOcorrenciaService.store(bean);
    }
    
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsCaractProdutoOcorrencia(List ids) {
    	caractProdutoOcorrenciaService.removeByIds(ids);
    }

    public ResultSetPage findPaginatedCaractProdutoOcorrencia(Map criteria) {
    	return caractProdutoOcorrenciaService.findPaginated(criteria);
    }

    public Integer getRowCountCaractProdutoOcorrencia(Map criteria) {
    	return caractProdutoOcorrenciaService.getRowCount(criteria);
    }

    public List findLookupFilialByControleCarga(Map criteria) {
    	List list = filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public List findCaracteristicaProduto(Map criteria) {
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsCaracteristicaProduto:asc");
    	FilterList filter = new FilterList(caracteristicaProdutoService.findListByCriteria(criteria, campoOrdenacao)) {
			public Map filterItem(Object item) {
				CaracteristicaProduto caracteristicaProduto = (CaracteristicaProduto)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idCaracteristicaProduto", caracteristicaProduto.getIdCaracteristicaProduto());
		    	typedFlatMap.put("dsCaracteristicaProduto", caracteristicaProduto.getDsCaracteristicaProduto());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }

    
    public void validatePreConditions(TypedFlatMap map) {
    	ocorrenciaNaoConformidadeService.validatePreConditionsByOcorrenciaNaoConformidade(map.getLong("idNaoConformidade"),
														    	 map.getString("dsOcorrenciaNc"),
														    	 map.getLong("idDoctoServico"),
														    	 map.getInteger("qtVolumes"),
														    	 map.getBigDecimal("vlOcorrenciaNc"),
		    	 map.getLong("idManifesto"),
		    	 map.getLong("idMotivoAberturaNc"));
    }

    
    public List findTipoManifesto(Map criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("EN");
    	dominiosValidos.add("VN");
    	List retorno = domainValueService.findByDomainNameAndValues("DM_TAG_MANIFESTO", dominiosValidos);
    	return retorno;
    }
    
    /**
     * <p>Busca a nao conformidade utilando o idOcorrenciaNaoConformidade informado pelo
     * processo do workflow.</p>
     * <p>Duvidas olhar a E.T. 12.01.01.01(AbrirRNC) ou 12.01.01.02(ManterNaoConformidade), 
     * onde o workflow e gerado.</p> 
     * 
     * @param criteria
     * @return TypedFlatMap
     */
    public TypedFlatMap findByIdProcessoWorkflow(TypedFlatMap criteria) {
    	TypedFlatMap result = new TypedFlatMap();
    	Long idOcorrenciaNaoConformidade = criteria.getLong("idProcessoWorkflow");
    	if (idOcorrenciaNaoConformidade!=null) {
    		result = this.findById(idOcorrenciaNaoConformidade);
    	}
    	return result;
    }
    
    public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
    	this.naoConformidadeService = naoConformidadeService;
    }
    
    public NaoConformidadeService getNaoConformidadeService() {
		return naoConformidadeService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}