package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.carregamento.model.dao.AdiantamentoTrechoDAO;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.adiantamentoTrechoService"
 */
public class AdiantamentoTrechoService extends CrudService<AdiantamentoTrecho, Long> {


	/**
	 * Recupera uma instância de <code>AdiantamentoTrecho</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public AdiantamentoTrecho findById(java.lang.Long id) {
        return (AdiantamentoTrecho)super.findById(id);
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
    public java.io.Serializable store(AdiantamentoTrecho bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAdiantamentoTrechoDAO(AdiantamentoTrechoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AdiantamentoTrechoDAO getAdiantamentoTrechoDAO() {
        return (AdiantamentoTrechoDAO) getDao();
    }

    /**
     * @param items
     */
    public void storeAdiantamentoTrecho(ItemList items) {
    	getAdiantamentoTrechoDAO().storeAdiantamentoTrecho(items);
	}


    public List findByIdControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
    	return getAdiantamentoTrechoDAO().findByIdControleCarga(idControleCarga, idFilialOrigem, idFilialDestino);
    }
    
    /**
     * 
     * @param idControleCarga
     * @param listaPagamentos
     * @param listaPostos
     */
    public void storeAdiantamentoTrechoByControleCarga(Long idControleCarga, List listaAdiantamentos) {
		for (Iterator iterAdiantamentos = listaAdiantamentos.iterator(); iterAdiantamentos.hasNext();) {
			Map map = (Map)iterAdiantamentos.next();
			Long idAdiantamentoTrecho = (Long)map.get("idAdiantamentoTrecho");
			BigDecimal newPcFrete = (BigDecimal)map.get("pcFrete");
			BigDecimal newVlAdiantamento = (BigDecimal)map.get("vlAdiantamento");
			Long idPostoConveniado = LongUtils.getLong(map.get("postoConveniado"));
			
			
			AdiantamentoTrecho adiantamentoTrecho = findById(idAdiantamentoTrecho);
			if ( (newPcFrete != null && adiantamentoTrecho.getPcFrete() == null) ||
				 (newPcFrete == null && adiantamentoTrecho.getPcFrete() != null) ||
				 (newPcFrete.compareTo(adiantamentoTrecho.getPcFrete()) != 0) ||
				 (newVlAdiantamento != null && adiantamentoTrecho.getVlAdiantamento() == null) ||
				 (newVlAdiantamento == null && adiantamentoTrecho.getVlAdiantamento() != null) ||
			     (newVlAdiantamento.compareTo(adiantamentoTrecho.getVlAdiantamento()) != 0) ||
			     (idPostoConveniado != null && adiantamentoTrecho.getPostoConveniado() == null) ||
			     (idPostoConveniado == null && adiantamentoTrecho.getPostoConveniado() != null) ||
			     (adiantamentoTrecho.getPostoConveniado().getIdPostoConveniado().compareTo(idPostoConveniado) != 0) )
			{
				adiantamentoTrecho.setPcFrete(newPcFrete == null ? BigDecimalUtils.ZERO : newPcFrete);
				adiantamentoTrecho.setVlAdiantamento(newVlAdiantamento == null ? BigDecimalUtils.ZERO : newVlAdiantamento);
				PostoConveniado postoConveniado = null;
				if(idPostoConveniado != null){
					postoConveniado = new PostoConveniado();
					postoConveniado.setIdPostoConveniado(idPostoConveniado);
				}
				adiantamentoTrecho.setPostoConveniado(postoConveniado);
				super.store(adiantamentoTrecho);
			}
		}
    }
    
    public PostoConveniado findPostoConveniadoByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro){
    	return getAdiantamentoTrechoDAO().findPostoConveniadoByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
    }
    
    
    /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	getAdiantamentoTrechoDAO().removeByIdControleCarga(idControleCarga);
    }
}