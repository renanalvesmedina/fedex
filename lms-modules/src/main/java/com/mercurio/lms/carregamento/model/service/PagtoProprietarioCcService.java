package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.PagtoProprietarioCc;
import com.mercurio.lms.carregamento.model.dao.PagtoProprietarioCcDAO;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.GerarRateioFreteCarreteiroService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.pagtoProprietarioCcService"
 */
public class PagtoProprietarioCcService extends CrudService<PagtoProprietarioCc, Long> {

	private MoedaService moedaService;
	private GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService;

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	
	public void setGerarRateioFreteCarreteiroService(
			GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService) {
		this.gerarRateioFreteCarreteiroService = gerarRateioFreteCarreteiroService;
	}


	/**
	 * Recupera uma instância de <code>PagtoProprietarioCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public PagtoProprietarioCc findById(java.lang.Long id) {
        return (PagtoProprietarioCc)super.findById(id);
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
    public java.io.Serializable store(PagtoProprietarioCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPagtoProprietarioCcDAO(PagtoProprietarioCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PagtoProprietarioCcDAO getPagtoProprietarioCcDAO() {
        return (PagtoProprietarioCcDAO) getDao();
    }
    

    /**
     * 
     * @param idControleCarga
     * @param idProprietario
     * @return
     */
    public Long validateExistePagtoProprietarioCcByControleCargaByProprietario(Long idControleCarga, Long idProprietario) {
    	return getPagtoProprietarioCcDAO().
    		validateExistePagtoProprietarioCcByControleCargaByProprietario(idControleCarga, idProprietario);
    }
    
    /**
     * 
     * @param map
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedPagtoProprietarioCc(Long idControleCarga, FindDefinition findDefinition) {
		ResultSetPage rsp = getPagtoProprietarioCcDAO().findPaginatedPagtoProprietarioCc(idControleCarga, findDefinition);
		List lista = new AliasToTypedFlatMapResultTransformer().transformListResult(rsp.getList());
		rsp.setList(lista);
	    return rsp;
    }
    
    /**
     * 
     * @param map
     * @return
     */
    public Integer getRowCountPagtoProprietarioCc(Long idControleCarga) {
    	return getPagtoProprietarioCcDAO().getRowCountPagtoProprietarioCc(idControleCarga);
    }
    
    /**
     * 
     * @param idPagtoProprietarioCc
     * @return
     */
    public PagtoProprietarioCc findByIdByControleCarga(Long idPagtoProprietarioCc) {
		Map map = getPagtoProprietarioCcDAO().findByIdByControleCarga(idPagtoProprietarioCc);
		List lista = new ArrayList();
		lista.add(map);
		lista = new AliasToNestedBeanResultTransformer(PagtoProprietarioCc.class).transformListResult(lista);
	    return (PagtoProprietarioCc)lista.get(0);
    }


    public void storeByControleCarga(Long idPagtoProprietarioCc, Long idMoeda, BigDecimal vlPagamento) {
    	PagtoProprietarioCc bean = findById(idPagtoProprietarioCc);
    	
    	Long idMoedaAnterior = null;
    	Long idMoedaNova = idMoeda;
    	BigDecimal valorAnterior = bean.getVlPagamento();
    	BigDecimal valorNovo = vlPagamento;
    	
    	bean.setMoeda(moedaService.findById(idMoeda));
    	bean.setVlPagamento(valorNovo);
    	store(bean);
    	getPagtoProprietarioCcDAO().getAdsmHibernateTemplate().flush();
    	
    	//Ao informar ou alterar algum registro em "pagamento proprietário" e o CC estiver
    	//com o status de FE, chama novamente a rotina de rateio de frete carreteiro.
    	if (bean.getMoeda()!=null){
    		idMoedaAnterior = bean.getMoeda().getIdMoeda();
    	}
    	if (bean.getControleCarga().getTpStatusControleCarga().getValue().equals("FE")){
    		if (!CompareUtils.eqNull(valorAnterior, valorNovo) || !CompareUtils.eqNull(idMoedaAnterior, idMoedaNova)){
    			gerarRateioFreteCarreteiroService.execute(bean.getControleCarga().getIdControleCarga());
    		}
    	}
    }
}