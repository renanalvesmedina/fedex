package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.dao.TipoLocalizacaoMunicipioDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.tipoLocalizacaoMunicipioService"
 */
public class TipoLocalizacaoMunicipioService extends CrudService<TipoLocalizacaoMunicipio, Long> {


	/**
	 * Recupera uma inst�ncia de <code>TipoLocalizacaoMunicipio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public TipoLocalizacaoMunicipio findById(java.lang.Long id) {
        return (TipoLocalizacaoMunicipio)super.findById(id);
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
    public java.io.Serializable store(TipoLocalizacaoMunicipio bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoLocalizacaoMunicipioDAO(TipoLocalizacaoMunicipioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoLocalizacaoMunicipioDAO getTipoLocalizacaoMunicipioDAO() {
        return (TipoLocalizacaoMunicipioDAO) getDao();
    }
    
    /**
     * Solicita��o CQPRO00005944 da Integra��o.
     * O m�todo assume que a descri��o passada para o 
     * m�todo seja no idioma portugu�s/Brasil (pt_BR)
     * @param dsTipoLocalizacao
     * @return
     */
    public List findByDsTipoLocalizacaoMunicipio(String dsTipoLocalizacao){
    	return getTipoLocalizacaoMunicipioDAO().findTipoLocalizacaoMunicipio(dsTipoLocalizacao, null);
    }

    /**
     * Busca Tipo de Localiza��o especifica, do Comercial(C) ou Operacional(O)
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