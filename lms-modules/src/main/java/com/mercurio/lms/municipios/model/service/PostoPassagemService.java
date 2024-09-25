package com.mercurio.lms.municipios.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EixosTipoMeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.municipios.model.Concessionaria;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.municipios.model.TarifaPostoPassagem;
import com.mercurio.lms.municipios.model.TipoPagamentoPosto;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.municipios.model.dao.PostoPassagemDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.postoPassagemService"
 */
public class PostoPassagemService extends CrudService<PostoPassagem, Long> {

	private TarifaPostoPassagemService tarifaPostoPassagemService;
	private ValorTarifaPostoPassagemService valorTarifaPostoPassagemService;
	private ConversaoMoedaService conversaoMoedaService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private ConfiguracoesFacade configuracoesFacades;
	private RodoviaService rodoviaService;
	private TipoPagamentoPostoService tipoPagamentoPostoService;
	private VigenciaService vigenciaService;
	private MoedaPaisService moedaPaisService; 
	private EixosTipoMeioTransporteService eixosTipoMeioTransporteService;

	
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public TipoPagamentoPostoService getTipoPagamentoPostoService() {
		return tipoPagamentoPostoService;
	}
	public void setTipoPagamentoPostoService(TipoPagamentoPostoService tipoPagamentoPostoService) {
		this.tipoPagamentoPostoService = tipoPagamentoPostoService;
	}	
	public void setConfiguracoesFacades(ConfiguracoesFacade configuracoesFacades) {
		this.configuracoesFacades = configuracoesFacades;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	public RodoviaService getRodoviaService() {
		return rodoviaService;
	}
	public void setRodoviaService(RodoviaService rodoviaService) {
		this.rodoviaService = rodoviaService;
	}
	public void setEixosTipoMeioTransporteService(EixosTipoMeioTransporteService eixosTipoMeioTransporteService) {
		this.eixosTipoMeioTransporteService = eixosTipoMeioTransporteService;
	}
	public TarifaPostoPassagemService getTarifaPostoPassagemService() {
		return tarifaPostoPassagemService;
	}
	public void setTarifaPostoPassagemService(TarifaPostoPassagemService tarifaPostoPassagemService) {
		this.tarifaPostoPassagemService = tarifaPostoPassagemService;
	}
	public ValorTarifaPostoPassagemService getValorTarifaPostoPassagemService() {
		return valorTarifaPostoPassagemService;
	}
	public void setValorTarifaPostoPassagemService(ValorTarifaPostoPassagemService valorTarifaPostoPassagemService) {
		this.valorTarifaPostoPassagemService = valorTarifaPostoPassagemService;
	}
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public TipoMeioTransporteService getTipoMeioTransporteService() {
		return tipoMeioTransporteService;
	}
	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacades = configuracoesFacade;
	}

	
	
	/**
	 * Recupera uma instância de <code>PostoPassagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public PostoPassagem findById(java.lang.Long id) {
        return (PostoPassagem)super.findById(id);
    }

    
    public Map findByIdDetalhamento(java.lang.Long id) {
    	PostoPassagem bean = (PostoPassagem)super.findById(id);  
    	TypedFlatMap result = new TypedFlatMap();
    	Concessionaria concessionaria = bean.getConcessionaria();
		Pessoa pessoaCon = concessionaria.getPessoa();
    	Municipio municipio = bean.getMunicipio();
    	UnidadeFederativa uf = municipio.getUnidadeFederativa();
    	Rodovia rodovia = bean.getRodovia();
    	Pais pais = uf.getPais();
    	
    	result.put("idPostoPassagem",bean.getIdPostoPassagem());
    	result.put("tpPostoPassagem",bean.getTpPostoPassagem().getValue());
		result.put("municipio.idMunicipio",municipio.getIdMunicipio());
    	result.put("municipio.nmMunicipio",municipio.getNmMunicipio());
    	result.put("municipio.unidadeFederativa.idUnidadeFederativa",uf.getIdUnidadeFederativa());
		result.put("municipio.unidadeFederativa.sgUnidadeFederativa",uf.getSgUnidadeFederativa());
		result.put("municipio.unidadeFederativa.nmUnidadeFederativa",uf.getNmUnidadeFederativa());
		result.put("municipio.unidadeFederativa.pais.idPais",pais.getIdPais());
		result.put("municipio.unidadeFederativa.pais.nmPais",pais.getNmPais().getValue(LocaleContextHolder.getLocale()));
		
    	if (rodovia != null) {
			result.put("rodovia.sgRodovia",rodovia.getSgRodovia());
	    	result.put("rodovia.idRodovia",rodovia.getIdRodovia());
	    	result.put("rodovia.dsRodovia",rodovia.getDsRodovia());
    	}
	    result.put("nrKm",bean.getNrKm());
    	result.put("nrLatitude",bean.getNrLatitude());
    	result.put("nrLongitude",bean.getNrLongitude());
    	result.put("tpSentidoCobranca",bean.getTpSentidoCobranca().getValue());
    	result.put("concessionaria.pessoa.nrIdentificacao",FormatUtils.formatIdentificacao(pessoaCon.getTpIdentificacao(),
    				pessoaCon.getNrIdentificacao()));
    	result.put("concessionaria.pessoa.nmPessoa",pessoaCon.getNmPessoa());
    	result.put("concessionaria.idConcessionaria",concessionaria.getIdConcessionaria());
    	result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
    	result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
    	result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));

    	return result;
    }
    
    protected void beforeRemoveByIds(List ids) {
    	PostoPassagem pp = null;
    	for(int x = 0; x < ids.size(); x++) {
    		pp = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(pp);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	PostoPassagem pp = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(pp);
    	super.beforeRemoveById(id);
    }
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    } 

    /**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PostoPassagem bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
    	PostoPassagem postoPassagem = new PostoPassagem();

        ReflectionUtils.copyNestedBean(postoPassagem,bean);

        vigenciaService.validaVigenciaBeforeStore(postoPassagem);

        super.store(postoPassagem);
        TypedFlatMap result = new TypedFlatMap();
        result.put("idPostoPassagem",postoPassagem.getIdPostoPassagem());
        result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(postoPassagem));
        return result;
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage rsp = getPostoPassagemDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    	List rs = new ArrayList();
    	for (Iterator ie = rsp.getList().iterator(); ie.hasNext();) {
    		TypedFlatMap flat = new TypedFlatMap();
    		Map temp = (Map)ie.next();
    		for (Iterator ie2 = temp.keySet().iterator(); ie2.hasNext();) {
    			String key = (String)ie2.next();
    			flat.put(key.replace('_','.'),temp.get(key));
    		}
    		flat.put("concessionaria.pessoa.nrIdentificacaoFormatado",FormatUtils.formatIdentificacao(flat.getDomainValue("concessionaria.pessoa.tpIdentificacao").getValue(),flat.getString("concessionaria.pessoa.nrIdentificacao")));
    		TarifaPostoPassagem tpp = findTarifaByTpMeioTranp(flat.getLong("idPostoPassagem"),null,null,JTDateTimeUtils.getDataAtual());
    		if (tpp != null)
    			flat.put("tpFormaCobranca",tpp.getTpFormaCobranca());
    		
    		rs.add(flat);
    	}
    	
    	rsp.setList(rs);
    	return rsp;
    } 
    /**
     * ROTINA: ET 29.02.01.23 Busca Valor Pedágio Data
     * Retorna o valor de do pedagio para o tipo de meio de transporte na data informada e o tipo de pagamento do posto
     * @param idPostoPassagem
     * @param idTipoMeioTransp
     * @param qtEixosMeioTransp
     * @param idTipoMeioTranspComposto
     * @param qtEixosMeioTranpComposto
     * @param dtCalculo
     * @param idMoedaPais
     * @param blNaoConsiderarSemParar
     * @return TypedFlatMap  vlPostoPassagem instance of BigDecimal
     * 						 idTipoPagamentoPosto instance of Long
     */
    public TypedFlatMap findVlByTpMeioTransporte(Long idPostoPassagem, 
    										     Long idTipoMeioTransp, 
    										     Integer qtEixos, 
    										     YearMonthDay dtCalculo, 
    										     Long idMoedaPais, 
    										     Boolean blNaoConsiderarSemParar) {
    	if (idPostoPassagem == null || idTipoMeioTransp == null)
    		throw new IllegalArgumentException("As propriedades \"idPostoPassagem\" e \"idTipoMeioTransp\" são obrigatórias.");

    	//1
    	if (dtCalculo == null)
    		dtCalculo = JTDateTimeUtils.getDataAtual();
    	
    	TipoMeioTransporte tpMeioTransp = (TipoMeioTransporte)getPostoPassagemDAO().getAdsmHibernateTemplate().get(TipoMeioTransporte.class,idTipoMeioTransp);
    	//3
    	if (tpMeioTransp.getTpMeioTransporte().getValue().equals("R") && qtEixos == null) {
    		throw new BusinessException("LMS-29137");
    	}
    	
    	TarifaPostoPassagem tarifa = findTarifaByTpMeioTranp(idPostoPassagem,null,null,dtCalculo);
    	
    	BigDecimal vlTarifa = null;
    	MoedaPais moedaVlTarifa = null;
    	TypedFlatMap result = new TypedFlatMap();
    	
    	if (tarifa == null){
    		vlTarifa = BigDecimal.ZERO;
    		result.put("vlPostoPassagem",vlTarifa);
    		result.put("moedaPais",moedaVlTarifa);
        	return result;
    	}
     	
    	if (tarifa.getTpFormaCobranca().getValue().equals("FI") || tarifa.getTpFormaCobranca().getValue().equals("EI")) {
    		ValorTarifaPostoPassagem valorTarifaPostoPassagem = ((ValorTarifaPostoPassagem)tarifa.getValorTarifaPostoPassagems().get(0));
    		if (idMoedaPais != null) {
    			MoedaPais moedaPaisO = moedaPaisService.findById(valorTarifaPostoPassagem.getMoedaPais().getIdMoedaPais());
    			MoedaPais moedaPaisD = moedaPaisService.findById(idMoedaPais);
    			vlTarifa = conversaoMoedaService.findConversaoMoeda(moedaPaisO.getPais().getIdPais(),moedaPaisO.getMoeda().getIdMoeda(),
    					moedaPaisD.getPais().getIdPais(),moedaPaisD.getMoeda().getIdMoeda(),dtCalculo,valorTarifaPostoPassagem.getVlTarifa());
    			moedaVlTarifa = moedaPaisD;
    		}else{
    			moedaVlTarifa = moedaPaisService.findById(valorTarifaPostoPassagem.getMoedaPais().getIdMoedaPais());
    			vlTarifa = valorTarifaPostoPassagem.getVlTarifa();
    		}
    		if (tarifa.getTpFormaCobranca().getValue().equals("EI")) {
    			BigDecimal eixos = BigDecimal.ZERO;
    			if (qtEixos != null)
    				eixos = eixos.add(BigDecimal.valueOf(qtEixos.longValue()));

    			vlTarifa = vlTarifa.multiply(eixos);
    		}
    	}else{
    		ValorTarifaPostoPassagem valorTarifaPostoPassagem = getPostoPassagemDAO().findValorByTpMeioTranp(idPostoPassagem,idTipoMeioTransp,qtEixos,dtCalculo);
    		
    		if (valorTarifaPostoPassagem == null) {
    			vlTarifa = BigDecimal.valueOf(0);
    		}else{

	    		if (idMoedaPais != null) {
		    		MoedaPais moedaPaisO = moedaPaisService.findById(valorTarifaPostoPassagem.getMoedaPais().getIdMoedaPais());
		    		MoedaPais moedaPaisD = moedaPaisService.findById(idMoedaPais);
		    		vlTarifa = conversaoMoedaService.findConversaoMoeda(moedaPaisO.getPais().getIdPais(),moedaPaisO.getMoeda().getIdMoeda(),
		    				moedaPaisD.getPais().getIdPais(),moedaPaisD.getMoeda().getIdMoeda(),dtCalculo,valorTarifaPostoPassagem.getVlTarifa());
		    		moedaVlTarifa = moedaPaisD;
				}else{
					moedaVlTarifa = moedaPaisService.findById(valorTarifaPostoPassagem.getMoedaPais().getIdMoedaPais());
					vlTarifa = valorTarifaPostoPassagem.getVlTarifa();
				}
    		}
    	}
    	TipoPagamentoPosto tpPagamento = tipoPagamentoPostoService.findByPostoPassagemAndMenorPrioridade(idPostoPassagem,dtCalculo,blNaoConsiderarSemParar);
    	if (tpPagamento == null){
    		PostoPassagem postoPassagem = (PostoPassagem)getPostoPassagemDAO().getAdsmHibernateTemplate().load(PostoPassagem.class,idPostoPassagem);
    		String parametroExcessaoPostoPassagem = postoPassagem.getTpPostoPassagem().getDescription().toString().concat(". ".concat(postoPassagem.getMunicipio().getNmMunicipio().concat("-".concat(postoPassagem.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa().concat("-".concat(postoPassagem.getMunicipio().getUnidadeFederativa().getPais().getNmPais().toString().concat(" - ".concat(postoPassagem.getRodovia().getSgRodovia().concat(" - Km ".concat(postoPassagem.getNrKm().toString()))))))))));
    		throw new BusinessException("LMS-29141",new Object[]{parametroExcessaoPostoPassagem});
    		
    	}
    	
    	result.put("vlPostoPassagem",vlTarifa);
    	result.put("moedaPais",moedaVlTarifa);
    	result.put("idTipoPagamentoPosto",tpPagamento.getIdTipoPagamentoPosto());

    	return result;
    }
    
    public TarifaPostoPassagem findTarifaByTpMeioTranp(Long idPostoPassagem, Long idTipoMeioTransp, 
    			Integer qtEixos, YearMonthDay dtConsulta) {
    	List rs = getPostoPassagemDAO().findTarifasByTpMeioTranp(idPostoPassagem,idTipoMeioTransp,qtEixos,dtConsulta);
    	if (rs.size() > 0)
    		return (TarifaPostoPassagem)rs.get(0);
    	return null;
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	// TODO Auto-generated method stub
    	return getPostoPassagemDAO().getRowCount(criteria);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPostoPassagemDAO(PostoPassagemDAO dao) {
        setDao( dao );
    }
    
    /** 
     * Implementa o padrão de ListBox: <b>Valores de Postos de Passagem</b>    
     * @author Samuel Herrmann
     * @param idPostoPassagem
     * @return List (valores para a listBox)
     */
    @SuppressWarnings("unchecked")
	public List findValoresPostosPassagemList(Map map) {
    	Long idPostoPassagem = Long.valueOf((String)map.get("idPostoPassagem"));
    	List result = new ArrayList();
        List rsTarifas = tarifaPostoPassagemService.findPostoPassagemVigente(idPostoPassagem);
        TarifaPostoPassagem tpp = null;
        ValorTarifaPostoPassagem vtpp = null;
        Map resultMap = null;
    	for(Iterator ie = rsTarifas.iterator(); ie.hasNext();) {
    		tpp = (TarifaPostoPassagem)ie.next();
    		if (tpp.getTpFormaCobranca().getValue().equals("EI") || tpp.getTpFormaCobranca().getValue().equals("FI")) {
    			//TIPO EIXO OR FIXO
    			List rsValores = valorTarifaPostoPassagemService.findByTarifaPostoPassagem(tpp.getIdTarifaPostoPassagem());
    			vtpp = (ValorTarifaPostoPassagem)rsValores.get(0);
    			BigDecimal newVlTarifa = conversaoMoedaService.findConversaoMoeda(tpp.getPostoPassagem().getMunicipio().getUnidadeFederativa().getPais().getIdPais(),
    					vtpp.getMoedaPais().getMoeda().getIdMoeda(),SessionUtils.getPaisSessao().getIdPais(),SessionUtils.getMoedaSessao().getIdMoeda(),
    					JTDateTimeUtils.getDataAtual(),vtpp.getVlTarifa());
    					
    			if (tpp.getTpFormaCobranca().getValue().equals("FI")) {
    				resultMap = new HashMap();
    				resultMap.put("text",
    						(new StringBuffer(50)).append(configuracoesFacades.getMensagem("valorFixo")).append(": ")
    						.append(SessionUtils.getMoedaSessao().getSiglaSimbolo())
    						.append(" ").append(FormatUtils.formatDecimal("#,##0.00", newVlTarifa.doubleValue())).toString());
    				result.add(resultMap);
    			}else{
					
    				List eixos = eixosTipoMeioTransporteService.findSumEixosAllMeioTransportes();

					for(Iterator i = eixos.iterator(); i.hasNext();) {
						Object[] projections = (Object[])i.next();
						Integer qtEixos = (Integer)projections[0]; 
						String dsTipoMeioTransporte = (String)projections[1];
						String dsTipoMeioTransporteComposto = (String)projections[2];
						Long idTipoMeioTransporte = (Long)projections[3];
						
						StringBuffer dsFormaCobranca = new StringBuffer(dsTipoMeioTransporte);
						if (dsTipoMeioTransporteComposto != null)
							dsFormaCobranca.append(" + ").append(dsTipoMeioTransporteComposto);
						dsFormaCobranca.append(" (").append(qtEixos).append(") ");
					
						TypedFlatMap temp = findVlByTpMeioTransporte(idPostoPassagem,idTipoMeioTransporte,qtEixos,null,null,false);
						
						MoedaPais moedaPais = (MoedaPais)temp.get("moedaPais");
						
						Map row = new HashMap();
        				row.put("text",dsFormaCobranca.append(moedaPais.getMoeda().getSiglaSimbolo()).append(" ")
        						 .append(FormatUtils.formatDecimal("#,##0.00",temp.getBigDecimal("vlPostoPassagem"))).toString());
        				result.add(row);

					}
    			} 
    		}else if (tpp.getTpFormaCobranca().getValue().equals("TI")) {
    			//TIPO DE VEICULO

				List rsValores = valorTarifaPostoPassagemService.findByTarifaPostoPassagem(tpp.getIdTarifaPostoPassagem());
				for(Iterator ieSub = rsValores.iterator(); ieSub.hasNext();) {
					vtpp = (ValorTarifaPostoPassagem)ieSub.next();
					
					BigDecimal newVlTarifa = conversaoMoedaService.findConversaoMoeda(tpp.getPostoPassagem().getMunicipio().getUnidadeFederativa().getPais().getIdPais(),
	    					vtpp.getMoedaPais().getMoeda().getIdMoeda(),SessionUtils.getPaisSessao().getIdPais(),SessionUtils.getMoedaSessao().getIdMoeda(),
	    					JTDateTimeUtils.getDataAtual(),vtpp.getVlTarifa());
						
					TipoMeioTransporte tmt = vtpp.getTipoMeioTransporte();
					StringBuffer sb = new StringBuffer(tmt.getDsTipoMeioTransporte().toString());
					
					if (tmt.getTipoMeioTransporte() != null)
						sb.append(" + ").append(tmt.getTipoMeioTransporte().getDsTipoMeioTransporte().toString());
					
					sb.append(" (").append(vtpp.getQtEixos()).append(") ");
					
					String start = sb.toString();
					
					resultMap = new HashMap();
    				resultMap.put("text",
    						(new StringBuffer(start))
    						.append(" ").append(SessionUtils.getMoedaSessao().getSiglaSimbolo())
    						.append(" ").append(FormatUtils.formatDecimal("#,##0.00", newVlTarifa.doubleValue())).toString());
    				result.add(resultMap);
				}
    		}
    	}
        return result;
    }
    
    /**
     *	Altera a description do campo tpFormaCobranca(DomainValue) para seu respectivo valor.<br>
     * 	Efetua a consulta trazendo municipios e rodovias
     * @author Samuel Herrmann
     * @param criteria
     * @return
     */
    public List findLookupByFormaCobranca(Map criteria) {
    	Map criteria2 = new HashMap();
    	criteria2.put("name","DM_POSTO_PASSAGEM");
    	criteria.put("domain",criteria2);
    	return getPostoPassagemDAO().findLookupByFormaCobranca(criteria);
    }
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PostoPassagemDAO getPostoPassagemDAO() {
        return (PostoPassagemDAO) getDao();
    }


	//verifica se existe as mesmas informaçoes vigentes(municipio,rodovia,nrKm)
	protected PostoPassagem beforeStore(PostoPassagem bean) {
		PostoPassagem postoPassagem = (PostoPassagem)bean;
		if(getPostoPassagemDAO().findPostoPassagemVigente(postoPassagem)){
			throw new BusinessException("LMS-00003");
		}
		if (postoPassagem.getRodovia() != null && !rodoviaService.findRodoviaByUf(postoPassagem.getRodovia().getIdRodovia(),postoPassagem.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa()))
			throw new BusinessException("LMS-29075");
		
		//regra 3.3 - verifica se o posto de passagem está no intervalo de vigencia dos filhos
		if(getTipoPagamentoPostoService().findFilhosVigentesByVigenciaPai(postoPassagem.getIdPostoPassagem(),postoPassagem.getDtVigenciaInicial(),null)
				|| getTarifaPostoPassagemService().findFilhosVigentesByVigenciaPai(postoPassagem.getIdPostoPassagem(),postoPassagem.getDtVigenciaInicial(),null))
			throw new BusinessException("LMS-29095");

		if(postoPassagem.getDtVigenciaFinal() != null && (getTipoPagamentoPostoService().findFilhosVigentesByVigenciaPai(postoPassagem.getIdPostoPassagem(),null,postoPassagem.getDtVigenciaFinal())
				|| getTarifaPostoPassagemService().findFilhosVigentesByVigenciaPai(postoPassagem.getIdPostoPassagem(),null,postoPassagem.getDtVigenciaFinal())))
			throw new BusinessException("LMS-29094");
		
		return super.beforeStore(bean);
	}

	/**
	 * 
	 * @param idPostoPassagem
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean findPostoPassagemByVigencias(Long idPostoPassagem, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getPostoPassagemDAO().findPostoPassagemByVigencias(idPostoPassagem,dtVigenciaInicial,dtVigenciaFinal);
	}

	
    /**
     * Retorna uma lista (Map) de PostoPassagem. Utilizando o idRotaColetaEntrega como critério de pesquisa.
     * 
     * @param idRotaColetaEntrega
     * @return
     */
    public List findPostoPassagemByControleCarga (Long idRotaColetaEntrega) {
    	List result = getPostoPassagemDAO().findPostoPassagemByControleCarga(idRotaColetaEntrega);
    	return result;
    }
}