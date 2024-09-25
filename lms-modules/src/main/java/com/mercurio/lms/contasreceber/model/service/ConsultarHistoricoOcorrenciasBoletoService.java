package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.Boleto;



/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.consultarHistoricoOcorrenciasBoletoService"
 */
public class ConsultarHistoricoOcorrenciasBoletoService {


	
	
	/** Usando a service HistoricoBoletoService  */
	HistoricoBoletoService historicoBoletoService;
	
	/** Usando a service TarifaBoletoService  */
	TarifaBoletoService tarifaBoletoService;


	/** Usando a service HistoricoBoletoService  */
	HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService;

	

	/** Usando a service Boleto  */
	BoletoService boletoService;

	
	
	
    /**Metodo que faz a consulta do Historico da Movimentação de acordo com o idBoleto
     * 
     * @param idBoleto
     * @param findDef
     * @return
     * @author Diego Umpierre
     */
	public ResultSetPage findPaginatedHistMov(Long idBoleto, FindDefinition findDef) {
 		if (idBoleto == null ){
 			return null;
 		}	
		return getHistoricoBoletoService().findPaginatedHistMov(idBoleto, findDef); 
	}

	
	/**Faz a contagem de tuplas da consulta findPaginatedHistMov
	 * 
	 * @author Diego Umpierre
	 * @param idBoleto
	 * @return
	 */
	public Integer getRowCountHistMov(Long idBoleto) {
		return getHistoricoBoletoService().getRowCountHistMov( idBoleto );
	}

    
    
	
	
	
	
    /**Metodo que faz a consulta das Tarifas de acordo com o idBoleto
     * 
     * @param idBoleto
     * @param findDef
     * @return
     * @author Diego Umpierre
     */
	public ResultSetPage findPaginatedTarBol(Long idBoleto, FindDefinition findDef) {
 		if (idBoleto == null ){
 			return null;
 		}	
		return getTarifaBoletoService().findPaginatedTarBol(idBoleto, findDef); 
	}

	
	/**Faz a contagem de tuplas da consulta findPaginatedTarBol
	 * 
	 * @author Diego Umpierre
	 * @param idBoleto
	 * @return
	 */
	public Integer getRowCountTarBol(Long idBoleto) {
		return getTarifaBoletoService().getRowCountTarBol( idBoleto );
	}

	
    /**Metodo que faz a consulta dos Motivos da Movimentação de acordo com o idHistoricoBoleto
     * 
     * @param idHistoricoBoleto
     * @param findDef
     * @return
     * @author Diego Umpierre
     */
	public ResultSetPage findPaginatedMotMov(Long idHistoricoBoleto, FindDefinition findDef) {
 		if (idHistoricoBoleto == null ){
 			return null;
 		}	
 		ResultSetPage rsp = getHistoricoMotivoOcorrenciaService().findPaginatedMotMov(idHistoricoBoleto, findDef);

		

        Iterator iter = rsp.getList().iterator();
        List newList = new ArrayList(rsp.getList().size());

        while (iter.hasNext()){

                   Object[] obj = (Object[]) iter.next();

                   Map map = new HashMap();

                   map.put("nrMotivoOcorrenciaBanco",obj[0]);
                   map.put("dsMotivoOcorrencia",obj[1]);
                   newList.add(map);
        }
        rsp.setList(newList);
        return rsp;                     
		
	}

	
	/**Faz a contagem de tuplas da consulta findPaginatedMotMov
	 * 
	 * @author Diego Umpierre
	 * @param idHistoricoBoleto
	 * @return
	 */
	public Integer getRowCountMotMov(Long idHistoricoBoleto) {
		return getHistoricoMotivoOcorrenciaService().getRowCountMotMov( idHistoricoBoleto );
	}
	
	
	
	 /**Metodo que faz a consulta dos Motivos da Movimentação de acordo com o idHistoricoBoleto
     * 
     * @param idHistoricoBoleto
     * @param findDef
     * @return
     * @author Diego Umpierre
     */
	public Boleto findHistoricoOcorBoleto(Long idBoleto) {
		if (idBoleto == null ){
 			return null;
 		}	
 		return getBoletoService().findById(idBoleto);		
		
		
	}
 		
	
	

	/**
	 * @return Returns the tarifaBoletoService.
	 */
	public TarifaBoletoService getTarifaBoletoService() {
		return tarifaBoletoService;
	}


	/**
	 * @param tarifaBoletoService The tarifaBoletoService to set.
	 */
	public void setTarifaBoletoService(TarifaBoletoService tarifaBoletoService) {
		this.tarifaBoletoService = tarifaBoletoService;
	}


	/**
	 * Recuperando  a service HistoricoBoletoService
	 * @return
	 */
	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}

    
	/**Setando a service HistoricoBoletoService
	 * 
	 * @param historicoBoletoService
	 */
    public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}


	/**
	 * @return Returns the historicoMotivoOcorrenciaService.
	 */
	public HistoricoMotivoOcorrenciaService getHistoricoMotivoOcorrenciaService() {
		return historicoMotivoOcorrenciaService;
	}


	/**
	 * @param historicoMotivoOcorrenciaService The historicoMotivoOcorrenciaService to set.
	 */
	public void setHistoricoMotivoOcorrenciaService(
			HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService) {
		this.historicoMotivoOcorrenciaService = historicoMotivoOcorrenciaService;
	}


	/**
	 * @return Returns the boletoService.
	 */
	public BoletoService getBoletoService() {
		return boletoService;
	}

	

	/**
	 * @param boletoService The boletoService to set.
	 */
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

    
	
	
}

