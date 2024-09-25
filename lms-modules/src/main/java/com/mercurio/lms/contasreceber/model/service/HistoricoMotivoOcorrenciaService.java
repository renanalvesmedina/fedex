package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.dao.HistoricoMotivoOcorrenciaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.historicoMotivoOcorrenciaService"
 */
public class HistoricoMotivoOcorrenciaService extends CrudService<HistoricoMotivoOcorrencia, Long> {


	/**
	 * Recupera uma inst�ncia de <code>HistoricoMotivoOcorrencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public HistoricoMotivoOcorrencia findById(java.lang.Long id) {
        return (HistoricoMotivoOcorrencia)super.findById(id);
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
    public java.io.Serializable store(HistoricoMotivoOcorrencia bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setHistoricoMotivoOcorrenciaDAO(HistoricoMotivoOcorrenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private HistoricoMotivoOcorrenciaDAO getHistoricoMotivoOcorrenciaDAO() {
        return (HistoricoMotivoOcorrenciaDAO) getDao();
    }
    
    
    
    
    /**M�todo que executa uma consulta para a tela:
     * Consultar Hist�rico das Ocorr�ncias do Boleto
     *
     * Busca os dados da Tarifa para o idBoleto
     * 
     *@author Diego Umpierre - LMS
     *@see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
     *@param idHistoricoBoleto identificador do Historico do Boleto, findDef FindDefinition
     *
     *@return ResultSetPage com o resultado da consulta de acordo com os parametros.
     */
    public ResultSetPage findPaginatedMotMov(Long idHistoricoBoleto, FindDefinition findDef) {
 		if (idHistoricoBoleto == null ){
 			return null;
 		}	
 		
		return getHistoricoMotivoOcorrenciaDAO().findPaginatedMotMov(idHistoricoBoleto, findDef); 
	}
    
    /**
     * Faz a contagem de tuplas da consulta findPaginatedMotMov
     * 
     * @param idHistoricoBoleto
     * @return
     * @author Diego Umpierre - LMS
     * @see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
     */
	public Integer getRowCountMotMov(Long  idHistoricoBoleto) {
 		if (idHistoricoBoleto == null ){
 			return null;
 		}	
 		
 		return getHistoricoMotivoOcorrenciaDAO().getRowCountMotMov(idHistoricoBoleto); 
	}
    
    
    
   }