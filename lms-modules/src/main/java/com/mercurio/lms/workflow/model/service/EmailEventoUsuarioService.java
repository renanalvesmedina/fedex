package com.mercurio.lms.workflow.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.workflow.model.EmailEventoUsuario;
import com.mercurio.lms.workflow.model.FilialEmailEventoUsuario;
import com.mercurio.lms.workflow.model.dao.EmailEventoUsuarioDAO;
/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.workflow.emailEventoUsuarioService"
 */

public class EmailEventoUsuarioService extends CrudService<EmailEventoUsuario, Long> {
	
	private FilialEmailEventoUsuarioService filialEmailEventoUsuarioService;
		
	private FilialService filialService;
	

	/**
	 * Recupera uma instância de <code>EmailEventoUsuarioService</code> a
	 * partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */

	public EmailEventoUsuario findById(java.lang.Long id) {
		Map pai = new HashMap();
		Map filho = new HashMap();
		
		filho.put("idEmailEventoUsuario",id);
		pai.put("emailEventoUsuario",filho);
		
		EmailEventoUsuario email = (EmailEventoUsuario) super.findById(id);
		
		List filhos = getFilialEmailEventoUsuarioService().find(pai);
		
		for (Iterator iter = filhos.iterator(); iter.hasNext();) {
			
			FilialEmailEventoUsuario element = (FilialEmailEventoUsuario) iter.next();			
			Filial filial = new Filial();
			
			filial.setIdFilial(element.getFilial().getIdFilial());
			filial.setSgFilial(element.getFilial().getSgFilial());
			
			element.setFilial(filial);			
			
		}
		
		email.setFilialEmailEventoUsuario(filhos);
		
		return email; 
	}
	
	public ResultSetPage findPaginatedEventoUsuario(TypedFlatMap criteria) {
		return getEmailEventoUsuarioDAO().findPaginatedEventoUsuario(criteria,FindDefinition.createFindDefinition(criteria));
	}
	
	/**
	 * Retorna a lista de EmailEventoUsuario por Evento do Workflow
	 * 
	 * @param Long idEvento
	 * @param Long idFilial
	 * @return List
	 * */	
	public List findByEvento(Long idEvento, Long idFilial){
		return this.getEmailEventoUsuarioDAO().findByEvento(idEvento, idFilial);		
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}
/** Remover os filhos, recebendo uma lista de ID´S (grid)
 * 
 */
	protected void beforeRemoveByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			EmailEventoUsuario eeu = this.findById(id);		
			if (eeu != null) {				
				List nova = eeu.getFilialEmailEventoUsuario();
				for (Iterator iterNovo = nova.iterator(); iterNovo.hasNext();) {
					Long idFilial = ((FilialEmailEventoUsuario) iterNovo.next()).getIdFilialEmailEventoUsuario();
					filialEmailEventoUsuarioService.removeById(idFilial);					
				}
			}
		}
		super.beforeRemoveByIds(ids);
	}
/**
 * Remover os filhos
 */
	
	public void beforeRemoveById(Long id) {
		if (id != null) {
			
									
			HashMap pai = new HashMap();
			HashMap filho = new HashMap();
			
			filho.put("idEmailEventoUsuario",id);
			pai.put("emailEventoUsuario",filho);		
			
			List filhos = getFilialEmailEventoUsuarioService().find(pai);
			for (Iterator iter = filhos.iterator(); iter.hasNext();) {
				Long idFilial = ((FilialEmailEventoUsuario) iter.next()).getIdFilialEmailEventoUsuario();
				this.filialEmailEventoUsuarioService.removeById(idFilial);
			}			
		}
		super.beforeRemoveById(id);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getEmailEventoUsuarioDAO().getRowCount(criteria);
	}

		
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(EmailEventoUsuario bean) {    	
        return super.store(bean);
    }    

    public java.io.Serializable store(EmailEventoUsuario emailEvento, TypedFlatMap bean) {
    	this.store(emailEvento);
    	List idsListBox = (List) bean.get("filialEmailEventoUsuario");
    	
    	if (idsListBox != null && idsListBox.size() > 0) {
    		for (Iterator iter = idsListBox.iterator(); iter.hasNext();) {
    			
    			TypedFlatMap filialIter = (TypedFlatMap) iter.next();
    			Filial filial = new Filial();
    			FilialEmailEventoUsuario filialEmailEventoUsuario = new FilialEmailEventoUsuario();
    			
    			filial.setIdFilial((Long.valueOf((String) filialIter.get("idFilial"))));
    			
    			filialEmailEventoUsuario.setIdFilial(filial.getIdFilial());
    			filialEmailEventoUsuario.setFilial(filial);
    			filialEmailEventoUsuario.setEmailEventoUsuario(emailEvento);
    			
    			this.getFilialEmailEventoUsuarioService().store(filialEmailEventoUsuario);
    		}
    	}
    	return emailEvento.getIdEmailEventoUsuario();
    }    

	public void removeByFilial (Serializable id) {
		this.getEmailEventoUsuarioDAO().removeByFilial(id);		
	}

	private EmailEventoUsuarioDAO getEmailEventoUsuarioDAO() {
		return (EmailEventoUsuarioDAO) getDao();
	}

	public void setEmailEventoUsuarioDAO(EmailEventoUsuarioDAO dao) {
		setDao( dao );
	}

	public FilialEmailEventoUsuarioService getFilialEmailEventoUsuarioService() {
		return filialEmailEventoUsuarioService;
	}

	public void setFilialEmailEventoUsuarioService(
			FilialEmailEventoUsuarioService filialEmailEventoUsuarioService) {
		this.filialEmailEventoUsuarioService = filialEmailEventoUsuarioService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}