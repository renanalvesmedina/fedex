package com.mercurio.lms.municipios.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.TipoPagamentoPosto;
import com.mercurio.lms.municipios.model.dao.TipoPagamentoPostoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.tipoPagamentoPostoService"
 */
public class TipoPagamentoPostoService extends CrudService<TipoPagamentoPosto, Long> {


	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;	
	/**
	 * Recupera uma instância de <code>TipoPagamentoPosto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TipoPagamentoPosto findById(java.lang.Long id) {
        return (TipoPagamentoPosto)super.findById(id);
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
    public java.io.Serializable store(TipoPagamentoPosto bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map map) {
    	TipoPagamentoPosto bean = new TipoPagamentoPosto();
        ReflectionUtils.copyNestedBean(bean,map);

        vigenciaService.validaVigenciaBeforeStore(bean);

        super.store(bean);
        TypedFlatMap result = new TypedFlatMap();
        result.put("idTipoPagamentoPosto",bean.getIdTipoPagamentoPosto());
        result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
        return result;
    }
    
    public TipoPagamentoPosto beforeStore(TipoPagamentoPosto bean) {
    	TipoPagamentoPosto obj = super.beforeStore(bean);
    	TipoPagamentoPosto tpp = (TipoPagamentoPosto)bean;
    	List rs = getTipoPagamentoPostoDAO().getPostoPassagemVigente(tpp.getPostoPassagem().getIdPostoPassagem(),tpp.getDtVigenciaInicial(),tpp.getDtVigenciaFinal());
    	if (rs == null || rs.size() == 0)
    		throw new BusinessException("LMS-29034");
    	rs = getTipoPagamentoPostoDAO().findTpPostoPassagemEquals(tpp);
    	if (rs != null && rs.size() != 0)
    		throw new BusinessException("LMS-00003");
    	
    	return obj;
    }
    
    public TipoPagamentoPosto findByPostoPassagemAndMenorPrioridade(Long idPostoPassagem,YearMonthDay vigenteEm, Boolean blNaoConsiderarSemParar) {
    	Long idPagtoPPSemParar=null;
    	if (blNaoConsiderarSemParar != null && blNaoConsiderarSemParar){
    		idPagtoPPSemParar = Long.valueOf( ((BigDecimal)configuracoesFacade.getValorParametro("ID_PAGTO_PP_SEM_PARAR")).longValue() );
    	}	
    	return getTipoPagamentoPostoDAO().findByPostoPassagemAndMenorPrioridade(idPostoPassagem,vigenteEm, blNaoConsiderarSemParar, idPagtoPPSemParar);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoPagamentoPostoDAO(TipoPagamentoPostoDAO dao) {
        setDao( dao );
    }
    
    
    public List getPostoPassagemVigente(Long idPostoPassagem, YearMonthDay dtInicial, YearMonthDay dtFinal) {
    	return getTipoPagamentoPostoDAO().getPostoPassagemVigente(idPostoPassagem,dtInicial,dtFinal);
    }
    
    protected void beforeRemoveByIds(List ids) {
    	TipoPagamentoPosto bean = null;
    	for(int x = 0; x < ids.size(); x++) {
    		bean = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	TipoPagamentoPosto bean = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	super.beforeRemoveById(id);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoPagamentoPostoDAO getTipoPagamentoPostoDAO() {
        return (TipoPagamentoPostoDAO) getDao();
    }
    
    /*
     *  Não deve permitir alteração de datas de vigência do posto de
	    passagem(pai) para datas fora dos intervalos  dos
	    registro filhos cadastrados em tipo de pagamento .
     */
    public boolean findFilhosVigentesByVigenciaPai(Long idPostoPassagem, YearMonthDay dtInicioVigenciaPai,YearMonthDay dtFimVigenciaPai){
    	return getTipoPagamentoPostoDAO().findFilhosVigentesByVigenciaPai(idPostoPassagem,dtInicioVigenciaPai,dtFimVigenciaPai);
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
    
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}