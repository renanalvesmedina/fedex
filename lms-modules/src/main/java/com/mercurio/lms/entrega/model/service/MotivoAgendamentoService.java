package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.MotivoAgendamento;
import com.mercurio.lms.entrega.model.dao.MotivoAgendamentoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.motivoAgendamentoService"
 */
public class MotivoAgendamentoService extends CrudService<MotivoAgendamento, Long> {


	/**
	 * Recupera uma inst�ncia de <code>MotivoAgendamento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
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
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(MotivoAgendamento bean) {
        return super.store(bean);
    }

    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotivoAgendamentoDAO(MotivoAgendamentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MotivoAgendamentoDAO getMotivoAgendamentoDAO() {
        return (MotivoAgendamentoDAO) getDao();
    }
	
    /**
	 * Solicita��o CQPRO00005922 da integra��o.
	 * M�todo que retorna uma instancia da classe MotivoAgendamento de acordo com a descri��o passada por par�metro.
	 * @param dsCausaNaoConformidade
	 * @return
	 */
	public MotivoAgendamento findMotivoAgendamento(String dsMotivoAgendamento){
		return getMotivoAgendamentoDAO().findMotivoAgendamento(dsMotivoAgendamento);
	}
}