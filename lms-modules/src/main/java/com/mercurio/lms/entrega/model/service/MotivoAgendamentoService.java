package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.MotivoAgendamento;
import com.mercurio.lms.entrega.model.dao.MotivoAgendamentoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.motivoAgendamentoService"
 */
public class MotivoAgendamentoService extends CrudService<MotivoAgendamento, Long> {


	/**
	 * Recupera uma instância de <code>MotivoAgendamento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public MotivoAgendamento findById(java.lang.Long id) {
        return (MotivoAgendamento)super.findById(id);
    }

    
    public List find(Map criteria) {
    	List orderBy = new ArrayList();
    	orderBy.add("dsMotivoAgendamento");    	
    	return this.getMotivoAgendamentoDAO().findListByCriteria(criteria, orderBy);
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
    public java.io.Serializable store(MotivoAgendamento bean) {
        return super.store(bean);
    }

    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotivoAgendamentoDAO(MotivoAgendamentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotivoAgendamentoDAO getMotivoAgendamentoDAO() {
        return (MotivoAgendamentoDAO) getDao();
    }
	
    /**
	 * Solicitação CQPRO00005922 da integração.
	 * Método que retorna uma instancia da classe MotivoAgendamento de acordo com a descrição passada por parâmetro.
	 * @param dsCausaNaoConformidade
	 * @return
	 */
	public MotivoAgendamento findMotivoAgendamento(String dsMotivoAgendamento){
		return getMotivoAgendamentoDAO().findMotivoAgendamento(dsMotivoAgendamento);
	}
}