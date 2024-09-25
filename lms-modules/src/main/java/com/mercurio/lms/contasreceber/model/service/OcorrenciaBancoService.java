package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.dao.OcorrenciaBancoDAO;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.ocorrenciaBancoService"
 */
public class OcorrenciaBancoService extends CrudService<OcorrenciaBanco, Long> {

	/**
     * M�todo que busca as OcorrenciasBanco de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage
     */
	public ResultSetPage findPaginatedByOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
		return getOcorrenciaBancoDAO().findPaginatedByOcorrenciaRemessaRetornoBancos(criteria);
	}
	
	 /**
     * M�todo que retorna o n�mero de registros de acordo com os filtros passados
     * @param criteria
     * @return
     */
	public Integer getRowCountByOcorrenciaRemessaRetornoBancos(TypedFlatMap criteria){
		return getOcorrenciaBancoDAO().getRowCountByOcorrenciaRemessaRetornoBancos(criteria);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>OcorrenciaBanco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public OcorrenciaBanco findById(java.lang.Long id) {
        return (OcorrenciaBanco)super.findById(id);
    }
    
    public OcorrenciaBanco findByBancoAndNrOcorrencia(Long idBanco, Short nrOcorrencoaBanco){
    	return findByBancoNrOcorrenciaTpOcorrencia(idBanco, nrOcorrencoaBanco, null);
    }

    public OcorrenciaBanco findByBancoNrOcorrenciaTpOcorrencia(Long idBanco, Short nrOcorrencoaBanco, String tpOcorrencoaBanco){
    	List lsoOcorrencia = this.getOcorrenciaBancoDAO().findByBancoNrOcorrenciaTpOcorrencia(idBanco, nrOcorrencoaBanco, tpOcorrencoaBanco);
    	
    	if (lsoOcorrencia.size() == 1) {
    		return (OcorrenciaBanco) lsoOcorrencia.get(0);
    	} else {
    		return null;
    	}
    }
    
    /**
     * Busca as ocorrencias de banco do tipo tpOcorrencia relacionadas ao
     * cedente identificado por idCedente.
     * 
     * @param idCedente
     * @param tpOcorrencia
     * @return
     */
    public List findComboByIdCedente(Long idCedente, String tpOcorrencia) {
    	return getOcorrenciaBancoDAO().findComboByIdCedente(idCedente, tpOcorrencia);
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
    public java.io.Serializable store(OcorrenciaBanco bean) {
        return super.store(bean);
    }
    
   	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setOcorrenciaBancoDAO(OcorrenciaBancoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private OcorrenciaBancoDAO getOcorrenciaBancoDAO() {
        return (OcorrenciaBancoDAO) getDao();
    }

	public List<OcorrenciaBanco> findOcorrenciaBancoForRetornoBanco(Short nrOcorrenciaBanco, Long idCedente) {
		return getOcorrenciaBancoDAO().findOcorrenciaBancoForRetornoBanco(nrOcorrenciaBanco, idCedente);
	}
   }