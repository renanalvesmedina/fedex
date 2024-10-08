package com.mercurio.lms.vendas.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.alertaPceAction"
 */

public class AlertaPceAction extends CrudAction {
	
	private VersaoDescritivoPceService versaoDescritivoPceService;
	
	public VersaoDescritivoPceService getVersaoDescritivoPceService() {
		return versaoDescritivoPceService;
	}
	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountPCE(TypedFlatMap criteria) {
		//TODO: Fazer a funcao...
		return Integer.valueOf(0);
	}
	
	/**
	 * Captura uma lista de avisos de "PCE" para popular a grid da tela de listagem
	 * avisos PCE�s.
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap findPaginatedPCE(TypedFlatMap criteria) {
		
		List ids = criteria.getList("codigos");
		List gridData = new ArrayList();
		TypedFlatMap mapId;
		
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = Long.valueOf((String) iter.next());
			mapId = new TypedFlatMap();
			mapId.put("idVersaoDescritivoPce", id);
			gridData.add(versaoDescritivoPceService.findDataAlertPCE(mapId));
		}
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("list", gridData);
		
		return result;
	}

	public Long validateifExistPceByCriteria(TypedFlatMap map) {
		Long idCliente = map.getLong("idCliente");
		Long cdOcorrenciaPce = map.getLong("cdOcorrenciaPce");
		Long cdEventoPce = map.getLong("cdEventoPce");
		Long cdProcessoPce = map.getLong("cdProcessoPce");
		return versaoDescritivoPceService.validateifExistPceByCriteria(idCliente,cdProcessoPce,cdEventoPce,cdOcorrenciaPce);
	}
	public String findMensagemPce(TypedFlatMap map) {
		Long idCliente = map.getLong("idCliente");
		Long cdOcorrenciaPce = map.getLong("cdOcorrenciaPce");
		Long cdEventoPce = map.getLong("cdEventoPce");
		Long cdProcessoPce = map.getLong("cdProcessoPce");
		return versaoDescritivoPceService.executeFindMensagemPce(idCliente,cdProcessoPce,cdEventoPce,cdOcorrenciaPce);
	}
	
	public void executeConfirmaRecebimentoDoAlerta(Long idVersaoDescritivoPce) {
		versaoDescritivoPceService.executeConfirmaRecebimentoDoAlerta(idVersaoDescritivoPce,null);
	}
	
	/**
	 * Gera a confimacao do recebimento de pce�s apartir dos <code>ids</code> recebidos 
	 * da grid da tela de <b>alertPceList</b>. 
	 * 
	 * @param criteria
	 */
	public void executeConfirmaRecebimentoDoAlertaList(TypedFlatMap criteria) {
		List ids = criteria.getList("ids");
				
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = Long.valueOf((String) iter.next());
			versaoDescritivoPceService.executeConfirmaRecebimentoDoAlerta(id,null);
		}
	}
	
	public TypedFlatMap findDataAlertPCE(TypedFlatMap map) {
		return versaoDescritivoPceService.findDataAlertPCE(map);
	}
	
}
