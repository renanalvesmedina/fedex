package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.ObservacaoICMS;
import com.mercurio.lms.tributos.model.dao.ObservacaoICMSDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.observacaoICMSService"
 */
public class ObservacaoICMSService extends CrudService<ObservacaoICMS, Long> {


	/**
	 * Recupera uma inst�ncia de <code>Object</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ObservacaoICMS findById(java.lang.Long id) {
        return (ObservacaoICMS)super.findById(id);
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
    public java.io.Serializable store(ObservacaoICMS bean) {
        return super.store(bean);
    }
    
    
    /**
     * Verifica se possue alguma tributacao vigente
     * @param idUfOrigem
     * @param idTipoTributacao
     * @param tpObservacaoIcms
     * @param dtVigencia
     * @return
     */
    public List findVigenteByTipoTributacao(Long idUfOrigem, Long idTipoTributacao, String tpObservacaoIcms, YearMonthDay dtVigencia){
    	return getObservacaoICMSDAO().findVigenteByTipoTributacao(idUfOrigem, idTipoTributacao, tpObservacaoIcms, dtVigencia);
    }

    /**
     * Obbtem a lista com todas as Observacoes da descricao tributacao icms
     * @param id
     * @return
     */
    public List findListByIdDescricaoTributacao(Long id){
    	return getObservacaoICMSDAO().findListByIdDescricaoTributacao(id);
    }
    
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setObservacaoICMSDAO(ObservacaoICMSDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ObservacaoICMSDAO getObservacaoICMSDAO() {
        return (ObservacaoICMSDAO) getDao();
    }
   }