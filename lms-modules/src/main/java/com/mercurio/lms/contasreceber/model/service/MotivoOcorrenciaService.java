package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.dao.MotivoOcorrenciaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.motivoOcorrenciaService"
 */
public class MotivoOcorrenciaService extends CrudService<MotivoOcorrencia, Long> {


	/**
	 * Recupera uma inst�ncia de <code>MotivoOcorrencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public MotivoOcorrencia findById(java.lang.Long id) {
        return (MotivoOcorrencia)super.findById(id);
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
    public java.io.Serializable store(MotivoOcorrencia bean) {
        return super.store(bean);
    }
    
    
    /**
     * Retorna a liste de motivo de ocorrencia com o tipo de motivo de ocorrencia = 'C' (Cancelado)
     * 
     * @author Micka�l Jalbert
     * @since 25/04/2006
     * 
     * @return List
     * */
    public List findMotivoOcorrenciaDeCancelamento(){
    	return getMotivoOcorrenciaDAO().findByTpMotivoOcorrencia("C");
    }
    
    /**
     * Retorna a liste de motivo de ocorrencia com o tipo de motivo de ocorrencia = 'V' (Vencimento)
     * 
     * @author Micka�l Jalbert
     * @since 0102/2007
     * 
     * @return List
     * */
    public List findMotivoOcorrenciaDeVencimento(){
    	return getMotivoOcorrenciaDAO().findByTpMotivoOcorrencia("V");
    }   

    public List findMotivoOcorrenciaDeSustacao(){
    	return getMotivoOcorrenciaDAO().findByTpMotivoOcorrencia("S");
    }

    public List findMotivoOcorrenciaDeBloqueio(){
    	return getMotivoOcorrenciaDAO().findByTpMotivoOcorrencia("B");
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotivoOcorrenciaDAO(MotivoOcorrenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MotivoOcorrenciaDAO getMotivoOcorrenciaDAO() {
        return (MotivoOcorrenciaDAO) getDao();
    }
    
    /**
     * M�todo de listagem da grid
     * @param dsMotivoOcorrencia Descri��o do motivo ocorr�ncia
     * @param tpMotivoOcorrencia Tipo do Motivo Ocorr�ncia
     * @param tpSituacao Situa��o do Motivo Ocorr�ncia
     * @return ResultSetPage contendo os resultados da pesquisa e dados de pagina��o
     */
    public ResultSetPage findPaginatedMotivoOcorrencia(VarcharI18n dsMotivoOcorrencia, String tpMotivoOcorrencia, String tpSituacao, FindDefinition findDef) {    	
    	return this.getMotivoOcorrenciaDAO().findPaginatedMotivoOcorrencia(dsMotivoOcorrencia, tpMotivoOcorrencia, tpSituacao, findDef);
    }

    /**
	 * Conta quantos registros ser�o exibidos na listagem
	 * @param dsMotivoOcorrencia Descri��o do motivo ocorr�ncia
     * @param tpMotivoOcorrencia Tipo do Motivo Ocorr�ncia
     * @param tpSituacao Situa��o do Motivo Ocorr�ncia
	 * @return Inteiro informando quantos registros ser�o exibidos na listagem
	 */
    public Integer getRowCountMotivoOcorrencia(VarcharI18n dsMotivoOcorrencia, String tpMotivoOcorrencia, String tpSituacao) {
		return this.getMotivoOcorrenciaDAO().getRowCountMotivoOcorrencia(dsMotivoOcorrencia, tpMotivoOcorrencia, tpSituacao);
	}
    
}