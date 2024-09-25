package com.mercurio.lms.seguros.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.seguros.model.AverbacaoAnexo;
import com.mercurio.lms.seguros.model.dao.AverbacoesAnexoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.averbacoesAnexoService"
 */
public class AverbacoesAnexoService extends CrudService<AverbacaoAnexo, Long>{

	private UsuarioLMSService usuarioLMSService;
	
	/**
	 * Recupera uma inst�ncia de <code>AverbacaoAnexo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public AverbacaoAnexo findById(java.lang.Long id) {
        return (AverbacaoAnexo)super.findById(id);
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
    public java.io.Serializable store(AverbacaoAnexo bean) {
        return super.store(bean);
    }
    
    public void prepareValuesToStore(AverbacaoAnexo averbacaoAnexo) {
		if(averbacaoAnexo.getIdAverbacaoAnexo() == null){
			UsuarioLMS user = usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());
			averbacaoAnexo.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
			averbacaoAnexo.setUsuario(user);
		} 
	}
    
    public List<AverbacaoAnexo> findAnexosByIdAverbacao(Long idAverbacao){
		return getAverbacoesAnexoDAO().findAnexosByIdAverbacao(idAverbacao);
	} 
    
    public Integer getRowCountAnexosByIdAverbacao(Long idAverbacao) {
    	return getAverbacoesAnexoDAO().getRowCountAnexosByIdAverbacao(idAverbacao);
    }
	
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setAverbacoesAnexoDAO(AverbacoesAnexoDAO dao){
    	setDao(dao);
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    public AverbacoesAnexoDAO getAverbacoesAnexoDAO(){
    	return (AverbacoesAnexoDAO) getDao();
    }

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
    
}
