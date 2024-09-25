package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.dao.ServicoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.servicoService"
 */
public class ServicoService extends CrudService<Servico, Long> {
    
		
	/**
	 * Recupera uma instância de <code>Servico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    @Override
	public Servico findById(java.lang.Long id) {
        return (Servico)super.findById(id);
    }
    
	/**
	 * Retorna um Servico que corresponde à sigla informada.
	 * @param sigla
	 * @return
	 */
    public Servico findServicoBySigla(String sigla){
    	return getServicoDAO().findServicoBySigla(sigla);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    @Override
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
	@Override
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
    @Override
	public java.io.Serializable store(Servico bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setServicoDAO(ServicoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ServicoDAO getServicoDAO() {
        return (ServicoDAO) getDao();
    }
    
	/**
	 * Busca o servico do documento de servico informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 09/05/2006
	 * 
	 * @param Long idDoctoServico
	 * @return Servico
	 * */
    public Servico findByDoctoServico(Long idDoctoServico) {
    	return getServicoDAO().findByDoctoServico(idDoctoServico);
    }    
    
    public List findIdsServicosAtivos() {
    	return getServicoDAO().findIdsServicosAtivos();
    }
    
    public List findIdsServicosAtivosByModal(String tpModal) {
    	return getServicoDAO().findIdsServicosAtivosByModal(tpModal);
    }
    
    public List findByTpAbrangencia(String tpAbrangencia) {
    	return getServicoDAO().findByTpAbrangencia(tpAbrangencia);
    }
    
    public List findAllAtivo() {
    	return getServicoDAO().findByTpAbrangencia(null);
    }

    public List findListByCriteria(Map criteria, List order) {
    	return getServicoDAO().findListByCriteria(criteria, order);
    }
    
    /**
	 * Busca uma entidade servico de acordo com os filtros
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/01/2007
	 *
	 * @param idTipoServico
	 * @param tpAbrangencia
	 * @param tpModal
	 * @return
	 *
	 */
	public Servico findServicoByIdTpServTpAbrangTpModal( String idTipoServico, String tpAbrangencia, String tpModal ){
		return getServicoDAO().findServicoByIdTpServTpAbrangTpModal(idTipoServico, tpAbrangencia, tpModal);
	}
	
	public Servico findServicoByTpAbrangTpModal(String tpAbrangencia, String tpModal ){
		return getServicoDAO().findServicoByTpAbrangTpModal(tpAbrangencia, tpModal);
	}
	
	public Servico findServicoByFluxoFilial() {
    	return getServicoDAO().findServicoByFluxoFilial();
    }
	
	public List findServicoByTpSituacaoTpModalTpAbrangencia(String tpSituacao, String tpModal, String tpAbrangencia ){
		return getServicoDAO().findServicoByTpSituacaoTpModalTpAbrangencia(tpSituacao, tpModal, tpAbrangencia );
	}
 
	public List<Servico> findChosen() {
		return getServicoDAO().findChosen();
	}
	
}
