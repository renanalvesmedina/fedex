package com.mercurio.lms.workflow.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.workflow.model.Mensagem;
import com.mercurio.lms.workflow.model.dao.MensagemDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.mensagemService"
 */
public class MensagemService extends CrudService<Mensagem, Long> {


	/**
	 * Recupera uma inst�ncia de <code>TipoEvento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Mensagem findById(java.lang.Long id) {
        return (Mensagem)super.findById(id);
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
    public java.io.Serializable store(Mensagem bean) {
        return super.store(bean);
    }
    
    /**
     * Insere uma mensagem quando � novo, ou altera a mensagem adicionando o 
     * valor nrPendencia no valor no banco.
     * 
     * @param Mensagem mensagem
     * */    
    public java.io.Serializable storeMensagemWithPendencia(Mensagem mensagem) {
    	Mensagem mensagemTemp = this.findByUsuario(mensagem.getUsuario().getIdUsuario());
    	if (mensagemTemp != null) {
    		Short nrPendencia = Short.valueOf(Integer.valueOf(mensagem.getNrPendencia().shortValue()+mensagemTemp.getNrPendencia().shortValue()).shortValue());
    		mensagem.setNrPendencia(nrPendencia);
    		mensagem.setIdMensagem(mensagemTemp.getIdMensagem());
    		this.getMensagemDAO().getSessionFactory().getCurrentSession().evict(mensagemTemp);
    	} 
        return super.store(mensagem);
    }
    
    public Mensagem findByUsuario(Long idUsuario){
    	Map criteria = new HashMap();
    	criteria.put("usuario.idUsuario", idUsuario);
    	List list = (List)this.find(criteria);
		if (!list.isEmpty()) {
			return (Mensagem)list.get(0);
		} else {
			return null;
		}
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMensagemDAO(MensagemDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MensagemDAO getMensagemDAO() {
        return (MensagemDAO) getDao();
    }
   }