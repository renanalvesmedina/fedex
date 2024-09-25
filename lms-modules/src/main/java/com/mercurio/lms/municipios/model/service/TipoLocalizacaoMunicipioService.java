package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.dao.TipoLocalizacaoMunicipioDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.tipoLocalizacaoMunicipioService"
 */
public class TipoLocalizacaoMunicipioService extends CrudService<TipoLocalizacaoMunicipio, Long> {


	/**
	 * Recupera uma instância de <code>TipoLocalizacaoMunicipio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TipoLocalizacaoMunicipio findById(java.lang.Long id) {
        return (TipoLocalizacaoMunicipio)super.findById(id);
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
    public java.io.Serializable store(TipoLocalizacaoMunicipio bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoLocalizacaoMunicipioDAO(TipoLocalizacaoMunicipioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoLocalizacaoMunicipioDAO getTipoLocalizacaoMunicipioDAO() {
        return (TipoLocalizacaoMunicipioDAO) getDao();
    }
    
    /**
     * Solicitação CQPRO00005944 da Integração.
     * O método assume que a descrição passada para o 
     * método seja no idioma português/Brasil (pt_BR)
     * @param dsTipoLocalizacao
     * @return
     */
    public List findByDsTipoLocalizacaoMunicipio(String dsTipoLocalizacao){
    	return getTipoLocalizacaoMunicipioDAO().findTipoLocalizacaoMunicipio(dsTipoLocalizacao, null);
    }

    /**
     * Busca Tipo de Localização especifica, do Comercial(C) ou Operacional(O)
     * @author Andre Valadas
     * @param dsTipoLocalizacao
     * @param tpLocalizacao
     * @return
     */
    public TipoLocalizacaoMunicipio findTipoLocalizacaoMunicipio(String dsTipoLocalizacao, String tpLocalizacao){
    	List result = getTipoLocalizacaoMunicipioDAO().findTipoLocalizacaoMunicipio(dsTipoLocalizacao, tpLocalizacao);
    	if(!result.isEmpty()) {
    		return (TipoLocalizacaoMunicipio)result.get(0);
    	}
		return null;
    }
    
    public List findTipoLocalizacaoOperacional(){
    	return getTipoLocalizacaoMunicipioDAO().findTipoLocalizacaoOperacional();
    }

    public TipoLocalizacaoMunicipio findByIdMunicipio(Long idMunicipio){
    	return getTipoLocalizacaoMunicipioDAO().findByIdMunicipio(idMunicipio, false);
    }
    public TipoLocalizacaoMunicipio findTipoLocalizacaoMunicipioFob(Long idMunicipio){
    	return getTipoLocalizacaoMunicipioDAO().findByIdMunicipio(idMunicipio, true);
    }

	public List findByTipoPropostaFilial(Long[] tiposPropostaFilial) {
		return getTipoLocalizacaoMunicipioDAO().findByTipoPropostaFilial(tiposPropostaFilial);
	}
}