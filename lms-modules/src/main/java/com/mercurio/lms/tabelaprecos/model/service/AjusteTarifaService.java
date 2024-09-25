package com.mercurio.lms.tabelaprecos.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.tabelaprecos.model.AjusteTarifa;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.AjusteTarifaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.ajusteTarifaService"
 */
public class AjusteTarifaService extends CrudService<AjusteTarifa, Long> {

	@Override
	public List find(Map criteria) {
		return super.find(criteria);
	}
	
	@Override
	public Serializable findById(Long id) {

		Map mapaAjustTarifa 			= getAjusteTarifaDAO().detail(id);
		Map mapaTabelaPreco 			= (Map) mapaAjustTarifa.get("tabelaPrecoByIdTabelaPreco");
		Map mapaTipoTabelaPreco 		= (Map) mapaTabelaPreco.get("tipoTabelaPreco");
		Map mapaSubTipoTabelaPreco  	= (Map)	mapaTabelaPreco.get("subtipoTabelaPreco");
		Map mapaDomainTipoTabelaPreco  	= (Map) mapaTipoTabelaPreco.get("tpTipoTabelaPreco");

		TabelaPreco 		tabelapreco 		= new TabelaPreco();
		TipoTabelaPreco 	tipotabelapreco 	= new TipoTabelaPreco();
		SubtipoTabelaPreco 	subtipotabelapreco 	= new SubtipoTabelaPreco();
		DomainValue 		dv 					= new DomainValue((String)mapaDomainTipoTabelaPreco.get("value"));
		
		tipotabelapreco.setTpTipoTabelaPreco(dv);
		tipotabelapreco.setNrVersao((Integer)mapaTipoTabelaPreco.get("nrVersao"));
		
		subtipotabelapreco.setTpSubtipoTabelaPreco((String)mapaSubTipoTabelaPreco.get("tpSubtipoTabelaPreco"));

		tabelapreco.setIdTabelaPreco((Long)mapaTabelaPreco.get("idTabelaPreco"));
		tabelapreco.setTipoTabelaPreco(tipotabelapreco);
		tabelapreco.setSubtipoTabelaPreco(subtipotabelapreco);
		
		mapaTabelaPreco.put("tabelaPrecoString", tabelapreco.getTabelaPrecoString());
		return (Serializable) mapaAjustTarifa;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {		
		return super.findPaginated(criteria);
	}
  
	public AjusteTarifaDAO getAjusteTarifaDAO() {
		return (AjusteTarifaDAO) getDao();
	}
	
	public void setAjusteTarifaDAO(AjusteTarifaDAO dao) {
		setDao(dao);
	}
	
	/**
	 * Através da rota de origem e destino verifica se existe ajuste a ser feito
	 * 
	 * @param origem
	 * @param destino
	 * @param tabelaPreco 
	 * @return
	 */
	public AjusteTarifa findByRota(RestricaoRota origem, RestricaoRota destino, TabelaPreco tabelaPreco){
		
		AjusteTarifa ajusteTarifa = getAjusteTarifaDAO().findByRota(origem,destino,tabelaPreco);
		if(ajusteTarifa == null){
			ajusteTarifa = getAjusteTarifaDAO().findByRota(null,destino,tabelaPreco);
			if(ajusteTarifa == null){
				ajusteTarifa = getAjusteTarifaDAO().findByRota(origem,null,tabelaPreco);
	}	
		}						
		return ajusteTarifa;
	}	
	
	@Override
	public Serializable store(AjusteTarifa bean) {
		return super.store(bean);
	}
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
}