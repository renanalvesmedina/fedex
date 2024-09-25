package com.mercurio.lms.seguros.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.seguros.model.ApoliceSeguroAnexo;
import com.mercurio.lms.seguros.model.dao.ApoliceSeguroAnexoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.apoliceSeguroAnexoService"
 */
public class ApoliceSeguroAnexoService extends CrudService<ApoliceSeguroAnexo, Long>{

	public void setApoliceSeguroAnexoDAO(ApoliceSeguroAnexoDAO apoliceSeguroAnexoDAO) {
		setDao(apoliceSeguroAnexoDAO);
	}

	public ApoliceSeguroAnexoDAO getApoliceSeguroAnexoDAO() {
		return (ApoliceSeguroAnexoDAO) getDao();
	}
	
	public List<ApoliceSeguroAnexo> findAnexosByIdApoliceSeguro(Long idApoliceSeguro){
		return getApoliceSeguroAnexoDAO().findAnexosByIdApoliceSeguro(idApoliceSeguro);
	} 
	
	public Integer getRowCountAnexosByIdApoliceSeguro(Long idApoliceSeguro){
		return getApoliceSeguroAnexoDAO().getRowCountAnexosByIdApoliceSeguro(idApoliceSeguro);
	}
	
	public void prepareValuesToStore(ApoliceSeguroAnexo apoliceSeguroAnexo){
		if(apoliceSeguroAnexo.getIdApoliceSeguroAnexo() == null){
			apoliceSeguroAnexo.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
			apoliceSeguroAnexo.setUsuario(SessionUtils.getUsuarioLogado());
		} 
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ApoliceSeguroAnexo bean) {
        return super.store(bean);
    }
    
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
    
}
