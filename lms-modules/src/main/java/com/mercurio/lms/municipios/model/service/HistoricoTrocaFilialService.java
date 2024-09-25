package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.HistoricoTrocaFilial;
import com.mercurio.lms.municipios.model.dao.HistoricoTrocaFilialDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.historicoTrocaFilialService"
 */
public class HistoricoTrocaFilialService extends CrudService<HistoricoTrocaFilial, Long> {


	/**
	 * Recupera uma instância de <code>HistoricoTrocaFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public HistoricoTrocaFilial findById(java.lang.Long id) {
        return (HistoricoTrocaFilial)super.findById(id);
    }

    public HistoricoTrocaFilial findByIdDetalhadoFilial(java.lang.Long id) {
    	return getHistoricoTrocaFilialDAO().findByIdDetalhadoFilial(id);
    }
    
    public Map findDadosHistoricoTrocaFilial(Long idHistoricoTrocaFilial){
    	return getHistoricoTrocaFilialDAO().findDadosHistoricoTrocaFilial(idHistoricoTrocaFilial);
    }
     
    /**
     * Método que busca os registros em historico filial na data que o agendamento é executado
     * @return
     */
    public List findHistoricoTrocaFilialDataAtual(YearMonthDay dtInclusao){
    	return getHistoricoTrocaFilialDAO().findHistoricoTrocaFilialDataAtual(dtInclusao);
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
    public java.io.Serializable store(HistoricoTrocaFilial bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setHistoricoTrocaFilialDAO(HistoricoTrocaFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private HistoricoTrocaFilialDAO getHistoricoTrocaFilialDAO() {
        return (HistoricoTrocaFilialDAO) getDao();
    }
   }