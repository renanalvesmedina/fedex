package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.PostoPassagemMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.PostoPassagemMunicipioDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.postoPassagemMunicipioService"
 */
public class PostoPassagemMunicipioService extends CrudService<PostoPassagemMunicipio, Long> {
	private MunicipioService municipioService;
	private MunicipioFilialService municipioFilialService;
	private VigenciaService vigenciaService;
	private PostoPassagemService postoPassagemService;
	
	/**
	 * Recupera uma instância de <code>PostoPassagemMunicipio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public PostoPassagemMunicipio findById(java.lang.Long id) {
        return (PostoPassagemMunicipio)super.findById(id);
    }
    
    public Map findByIdDetalhamento(java.lang.Long id) {
    	PostoPassagemMunicipio bean = (PostoPassagemMunicipio)super.findById(id);
    	TypedFlatMap flat = new TypedFlatMap();
		
		MunicipioFilial municipioFilial = bean.getMunicipioFilial();
		Filial filial = municipioFilial.getFilial();
		UnidadeFederativa unidadeFederativa = municipioFilial.getMunicipio().getUnidadeFederativa();
		PostoPassagem postoPassagem = bean.getPostoPassagem();
		
		flat.put("municipioFilial.filial.sgFilial",filial.getSgFilial());
		flat.put("municipioFilial.filial.idFilial",filial.getIdFilial());
		flat.put("municipioFilial.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		flat.put("municipioFilial.municipio.nmMunicipio",municipioFilial.getMunicipio().getNmMunicipio());
		flat.put("municipioFilial.idMunicipioFilial",municipioFilial.getIdMunicipioFilial());
		flat.put("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa",unidadeFederativa.getNmUnidadeFederativa());
		flat.put("municipioFilial.municipio.unidadeFederativa.pais.nmPais",unidadeFederativa.getPais().getNmPais());
		flat.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		flat.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
		flat.put("idPostoPassagemMunicipio",bean.getIdPostoPassagemMunicipio());
		flat.put("postoPassagem.idPostoPassagem",postoPassagem.getIdPostoPassagem());
		flat.put("postoPassagem.tpPostoPassagem.description",postoPassagem.getTpPostoPassagem().getDescription().getValue(LocaleContextHolder.getLocale()));
		Pessoa concessionaria = postoPassagem.getConcessionaria().getPessoa();
		flat.put("postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado",
				FormatUtils.formatIdentificacao(concessionaria.getTpIdentificacao(),concessionaria.getNrIdentificacao()));
		flat.put("postoPassagem.concessionaria.pessoa.nmPessoa",concessionaria.getNmPessoa());
		flat.put("postoPassagem.municipio.nmMunicipio",postoPassagem.getMunicipio().getNmMunicipio());
		flat.put("postoPassagem.tpSentidoCobranca.description",postoPassagem.getTpSentidoCobranca().getDescription().getValue(LocaleContextHolder.getLocale()));
		flat.put("postoPassagem.rodovia.sgRodovia",postoPassagem.getRodovia().getSgRodovia());
		flat.put("postoPassagem.nrKm",postoPassagem.getNrKm());
		flat.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
		
    	return flat;
    }
    
    public ResultSetPage findPaginated(Map criteria) {
    	FilterResultSetPage frsp = new FilterResultSetPage(super.findPaginated(criteria)) {
		
			public Map filterItem(Object item) {
				PostoPassagemMunicipio bean = (PostoPassagemMunicipio)item;
				TypedFlatMap flat = new TypedFlatMap();
				
				MunicipioFilial municipioFilial = bean.getMunicipioFilial();
				Filial filial = municipioFilial.getFilial();
				PostoPassagem postoPassagem = bean.getPostoPassagem();
				 
				flat.put("municipioFilial.filial.sgFilial",filial.getSgFilial());
				flat.put("municipioFilial.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
				flat.put("municipioFilial.municipio.nmMunicipio",municipioFilial.getMunicipio().getNmMunicipio());
				flat.put("postoPassagem.tpPostoPassagem",postoPassagem.getTpPostoPassagem());
				flat.put("postoPassagem.rodovia.sgRodovia",postoPassagem.getRodovia().getSgRodovia());
				flat.put("postoPassagem.nrKm",postoPassagem.getNrKm());
				flat.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
				flat.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
				flat.put("idPostoPassagemMunicipio",bean.getIdPostoPassagemMunicipio());
				return flat;
			}
		};
		return (ResultSetPage)frsp.doFilter();
    }
    
    /**
     * Conta a quantidade de postos de passagem entre o municipio e a filial, vigentes na data informada
     * @param idMunicipioFilial
     * @param dtVigencia
     * @return
     */
    public Integer findQtdPostosPassagemEntreMunicipioEFilial(Long idMunicipioFilial, YearMonthDay dtVigencia){
    	return getPostoPassagemMunicipioDAO().findQtdPostosPassagemEntreMunicipioEFilial(idMunicipioFilial, dtVigencia);
    }
    
    /**
     * Conta a quantidade de postos de passagem bidirecionais (ida e volta) entre o municipio e a filial, vigentes na data informada
     * @param idMunicipioFilial
     * @param dtVigencia
     * @return
     */
    public Integer findQtdPostosPassagemBidirecionalEntreMunicipioEFilial(Long idMunicipioFilial, YearMonthDay dtVigencia){
    	return getPostoPassagemMunicipioDAO().findQtdPostosPassagemBidirecionalEntreMunicipioEFilial(idMunicipioFilial, dtVigencia);
    }
    
    protected PostoPassagemMunicipio beforeStore(PostoPassagemMunicipio bean) {
    	PostoPassagemMunicipio ppm = (PostoPassagemMunicipio)bean;
    	
    	if (!getMunicipioFilialService().isMunicipioFilialVigente(ppm.getMunicipioFilial().getIdMunicipioFilial(), ppm.getDtVigenciaInicial(), ppm.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	if (postoPassagemService.findPostoPassagemByVigencias(ppm.getPostoPassagem().getIdPostoPassagem(),ppm.getDtVigenciaInicial(),ppm.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	if (!getPostoPassagemMunicipioDAO().isVigenciaValida(ppm))
    		throw new BusinessException("LMS-00003");
    	return super.beforeStore(bean);
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
    public java.io.Serializable store(PostoPassagemMunicipio bean) {
        return super.store(bean);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public TypedFlatMap storeMap(Map bean) {
    	PostoPassagemMunicipio postoPassagemMunicipio = new PostoPassagemMunicipio();

        ReflectionUtils.copyNestedBean(postoPassagemMunicipio,bean);

        vigenciaService.validaVigenciaBeforeStore(postoPassagemMunicipio);

        super.store(postoPassagemMunicipio);
        
        TypedFlatMap result = new TypedFlatMap();
        result.put("idPostoPassagemMunicipio",(Long)super.store(postoPassagemMunicipio));
        result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(postoPassagemMunicipio));
        return result;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO. 
     */
    public void setPostoPassagemMunicipioDAO(PostoPassagemMunicipioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PostoPassagemMunicipioDAO getPostoPassagemMunicipioDAO() {
        return (PostoPassagemMunicipioDAO) getDao();
    }
    
    protected void beforeRemoveByIds(List ids) {
    	PostoPassagemMunicipio ppm = null;
    	for(int x = 0; x < ids.size(); x++) {
    		ppm = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(ppm);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	PostoPassagemMunicipio ppm = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(ppm);
    	super.beforeRemoveById(id);
    }

    protected PostoPassagemMunicipio beforeInsert(PostoPassagemMunicipio bean) {
    	if (!municipioService.isMunicipioAtivo(((PostoPassagemMunicipio)bean).getMunicipioFilial().getMunicipio().getIdMunicipio()))
    		throw new BusinessException("LMS-29023");
    	return super.beforeInsert(bean);
    }

    /**
     * LMS-2537
     * Rotina para calculo de pontos de passagens entre um fluxo de carga 
     * 
     * @param idMunicipioOrigem
     * @param idMunicipioDestino
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param dtPesquisa
     * @param idFFServico
     * @return
     */
    public Integer countPostosPassagens(Long idMunicipioOrigem, Long idMunicipioDestino, Long idFilialOrigem, Long idFilialDestino,
			YearMonthDay dtPesquisa, Long idFFServico) {
    	return getPostoPassagemMunicipioDAO().countPostosPassagens(idMunicipioOrigem, idMunicipioDestino, idFilialOrigem, idFilialDestino, dtPesquisa, idFFServico);
    }

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	/**
	 * @return Returns the municipioFilialService.
	 */
	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}

	/**
	 * @param municipioFilialService The municipioFilialService to set.
	 */
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	
	public List findPostosPassagemVigentesByMunFil(Long idMunicipioFilial){
		return getPostoPassagemMunicipioDAO().findPostosPassagemVigentesByMunFil(idMunicipioFilial);
	}
	
	public List findByIdPostoPassagemMunicipio(Long idPostoPassagemMunicipio){
		return getPostoPassagemMunicipioDAO().findByIdPostoPassagemMunicipio(idPostoPassagemMunicipio);
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setPostoPassagemService(PostoPassagemService postoPassagemService) {
		this.postoPassagemService = postoPassagemService;
	}
	
	
   }