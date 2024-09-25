package com.mercurio.lms.sim.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.LocalizacaoMercadoriaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.localizacaoMercadoriaService"
 */
public class LocalizacaoMercadoriaService extends CrudService<LocalizacaoMercadoria, Long> {


	/**
	 * Recupera uma inst�ncia de <code>LocalizacaoMercadoria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public LocalizacaoMercadoria findById(java.lang.Long id) {
        return (LocalizacaoMercadoria)super.findById(id);
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
    public java.io.Serializable store(LocalizacaoMercadoria bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setLocalizacaoMercadoriaDAO(LocalizacaoMercadoriaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private LocalizacaoMercadoriaDAO getLocalizacaoMercadoriaDAO() {
        return (LocalizacaoMercadoriaDAO) getDao();
    }
    
    /**
     * M�todo que busca o ID da LocalizacaoMercadoria q possui o c�digo informado
     * @param cdLocalizacaoMercadoria
     * @return
     */
    public LocalizacaoMercadoria findLocalizacaoMercadoriaByCodigo(Short cdLocalizacaoMercadoria) {
    	return this.getLocalizacaoMercadoriaDAO().findLocalizacaoMercadoriaByCodigo(cdLocalizacaoMercadoria);
    }
 
    /**
     * 
     * @param codigos
     * @return
     */
    public List findByCodigosLocalizacaoMercadoria(List codigos, String tpSituacao) {
    	return getLocalizacaoMercadoriaDAO().findByCodigosLocalizacaoMercadoria(codigos, tpSituacao);
    }
    
    public boolean validateIfIsEntregaRealizada(LocalizacaoMercadoria localizacao) {
        if (localizacao != null) {
            return ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA.equals(localizacao.getCdLocalizacaoMercadoria());
        }

        return false;
    }
    
    public Map<String,Object> findLocalizacaoSimplificada(String tpCliente, String nrIdentificacao, String tpDocumento, String nrDocumento) {
    	return getLocalizacaoMercadoriaDAO().findLocalizacaoSimplificada(tpCliente, nrIdentificacao, tpDocumento, nrDocumento);
    }

    public Long findIdLocalizacaoMercadoriaByCdLocalizacaoMercadoria(Short cdLocalizacaoMercadoria){
        return getLocalizacaoMercadoriaDAO()
                .findIdLocalizacaoMercadoriaByCdLocalizacaoMercadoria(cdLocalizacaoMercadoria);
    }
   
}