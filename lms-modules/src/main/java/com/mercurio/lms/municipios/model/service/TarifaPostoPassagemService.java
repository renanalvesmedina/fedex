package com.mercurio.lms.municipios.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.TarifaPostoPassagem;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.municipios.model.dao.TarifaPostoPassagemDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.tarifaPostoPassagemService"
 */
public class TarifaPostoPassagemService extends CrudService<TarifaPostoPassagem, Long> {

	private TipoPagamentoPostoService tipoPagamentoPostoService;
	private ValorTarifaPostoPassagemService valorTarifaPostoPassagemService;
	private MoedaService moedaService;
	private VigenciaService vigenciaService;
	/**
	 * Recupera uma instância de <code>TarifaPostoPassagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TarifaPostoPassagem findById(java.lang.Long id) {
        return (TarifaPostoPassagem)super.findById(id);
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

    public TarifaPostoPassagem beforeStore(TarifaPostoPassagem bean) {
    	vigenciaService.validaVigenciaBeforeStore((TarifaPostoPassagem)bean,Integer.valueOf(1));
    	TarifaPostoPassagem obj = super.beforeStore(bean);
    	TarifaPostoPassagem tpp = (TarifaPostoPassagem)bean;
    	List rs = tipoPagamentoPostoService.getPostoPassagemVigente(tpp.getPostoPassagem().getIdPostoPassagem(),tpp.getDtVigenciaInicial(),tpp.getDtVigenciaFinal());
    	if (rs == null || rs.size() == 0)
    		throw new BusinessException("LMS-29034");
    	
    	if (getTarifaPostoPassagemDAO().findBeforeStore(tpp))
    		throw new BusinessException("LMS-00003");

    	return obj;
    }



    protected void beforeRemoveByIds(List ids) {
    	TarifaPostoPassagem bean = null;
    	for(int x = 0; x < ids.size(); x++) {
    		bean = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	TarifaPostoPassagem bean = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(bean);
    	super.beforeRemoveById(id);
    }
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map store(Map map) {
    	TypedFlatMap flat = (TypedFlatMap)map;
    	TarifaPostoPassagem bean = new TarifaPostoPassagem();
    	if (flat.getLong("idTarifaPostoPassagem") != null)
    		bean = findById(flat.getLong("idTarifaPostoPassagem"));

    	PostoPassagem postoPassagem = new PostoPassagem();
		postoPassagem.setIdPostoPassagem(flat.getLong("postoPassagem.idPostoPassagem"));
		bean.setPostoPassagem(postoPassagem);
		bean.setIdTarifaPostoPassagem(flat.getLong("idTarifaPostoPassagem"));
		bean.setDtVigenciaFinal(flat.getYearMonthDay("dtVigenciaFinal"));
		bean.setDtVigenciaInicial(flat.getYearMonthDay("dtVigenciaInicial"));
		bean.setTpFormaCobranca(flat.getDomainValue("tpFormaCobranca"));
		
    	this.store(bean);
   		ValorTarifaPostoPassagem vtpp;
   		if (flat.getLong("valorTarifaPostoPassagem.idValorTarifaPostoPassagem") != null)
   			vtpp = valorTarifaPostoPassagemService.findById(flat.getLong("valorTarifaPostoPassagem.idValorTarifaPostoPassagem"));
   		else
   			vtpp = new ValorTarifaPostoPassagem();   

   		MoedaPais mp = new MoedaPais();
   				  mp.setIdMoedaPais(flat.getLong("valorTarifaPostoPassagem.moedaPais.idMoedaPais"));
   		vtpp.setMoedaPais(mp);
   		vtpp.setVlTarifa(flat.getBigDecimal("valorTarifaPostoPassagem.vlTarifa"));
   		vtpp.setTarifaPostoPassagem(bean);
   		getValorTarifaPostoPassagemService().deleteVTPPByTPPandId(bean.getIdTarifaPostoPassagem(),vtpp.getIdValorTarifaPostoPassagem());
   		valorTarifaPostoPassagemService.store(vtpp);
   		
   		TypedFlatMap result = new TypedFlatMap();
    	result.put("idTarifaPostoPassagem",bean.getIdTarifaPostoPassagem());   		
   		result.put("valorTarifaPostoPassagem.idValorTarifaPostoPassagem",vtpp.getIdValorTarifaPostoPassagem());
   		result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean,Integer.valueOf(1)));
    	return result;
    }

    public Map store(TarifaPostoPassagem bean, ItemList items) {
    	if (!items.hasItems())
			throw new BusinessException("LMS-29035");
    	boolean rollbackMasterId = (bean.getIdTarifaPostoPassagem() == null);
    	this.beforeStore(bean);
    	try {
			getTarifaPostoPassagemDAO().store(bean,items);
		} catch (HibernateOptimisticLockingFailureException e) {
			throw new BusinessException("LMS-00012");
		} catch (RuntimeException e) {
            bean.setIdTarifaPostoPassagem(null);
            bean.setVersao(null);
            this.rollbackMasterState(bean,rollbackMasterId,e);
            items.rollbackItemsState();
            throw e;
		}

		
		Map rMap = new HashMap();
		rMap.put("idTarifaPostoPassagem",bean.getIdTarifaPostoPassagem());
		rMap.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		rMap.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean,Integer.valueOf(1)));
		return rMap;
    }
    
    public List findByPostoPassagem(Long idPostoPassagem,Boolean isVigentes) {
    	return getTarifaPostoPassagemDAO().findByPostoPassagem(idPostoPassagem,isVigentes);
    }

	public List findValorTarifaPostoPassagem(Long masterId) {
		return getTarifaPostoPassagemDAO().findValorTarifaPostoPassagemByTarifaPostoPassagemId(masterId);
	}

	public Integer getRowCountValorTarifaPostoPassagem(Long masterId) {
		return getTarifaPostoPassagemDAO().getRowCountValorTarifaPostoPassagem(masterId);
	}
	/** Busca todas as tarifaPostoPassagem do idPostoPassagem informado que estejam vigente
	 * @author Samuel Herrmann
	 * @param idPostoPassagem
	 * @return
	 */
	public List findPostoPassagemVigente(Long idPostoPassagem) {
		return getTarifaPostoPassagemDAO().findPostoPassagemVigente(idPostoPassagem);
	}
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTarifaPostoPassagemDAO(TarifaPostoPassagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TarifaPostoPassagemDAO getTarifaPostoPassagemDAO() {
        return (TarifaPostoPassagemDAO) getDao();
    }

	public TipoPagamentoPostoService getTipoPagamentoPostoService() {
		return tipoPagamentoPostoService;
	}

	public void setTipoPagamentoPostoService(
			TipoPagamentoPostoService tipoPagamentoPostoService) {
		this.tipoPagamentoPostoService = tipoPagamentoPostoService;
	}

	public ValorTarifaPostoPassagemService getValorTarifaPostoPassagemService() {
		return valorTarifaPostoPassagemService;
	}

	public void setValorTarifaPostoPassagemService(
			ValorTarifaPostoPassagemService valorTarifaPostoPassagemService) {
		this.valorTarifaPostoPassagemService = valorTarifaPostoPassagemService;
	}

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	 /*
     *  Não deve permitir alteração de datas de vigência do posto de
	    passagem(pai) para datas fora dos intervalos  dos
	    registro filhos cadastrados em tipo de pagamento .
     */
    public boolean findFilhosVigentesByVigenciaPai(Long idPostoPassagem, YearMonthDay dtInicioVigenciaPai,YearMonthDay dtFimVigenciaPai){
    	return getTarifaPostoPassagemDAO().findFilhosVigentesByVigenciaPai(idPostoPassagem,dtInicioVigenciaPai,dtFimVigenciaPai);
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	
}