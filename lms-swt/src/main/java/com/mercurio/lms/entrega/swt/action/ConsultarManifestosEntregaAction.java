package com.mercurio.lms.entrega.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.swt.consultarManifestosEntregaAction"
 */

public class ConsultarManifestosEntregaAction extends CrudAction {

	private FilialService filialService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ManifestoEntregaService manifestoEntregaService;
	private MeioTransporteService meioTransporteService;
    private HistoricoFilialService historicoFilialService;
	
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	
	
	/**
     * findLookup de filiais.
     * @param criteria
     * @return
     */
    public List findLookupFilial(Map criteria) {
		List lista = filialService.findLookupFilial(criteria);
		if(!lista.isEmpty() && lista.size()== 1){
			Map mapRetorno = (Map)lista.get(0);
			Map mapNovo = new HashMap();
			mapNovo.put("idFilial", (Long)mapRetorno.get("idFilial"));
			mapNovo.put("sgFilial", mapRetorno.get("sgFilial").toString());
			mapNovo.put("nmFantasia", ((Map)mapRetorno.get("pessoa")).get("nmFantasia").toString());
			lista.clear();
			lista.add(mapNovo);
		}
		return lista;
	}
    
    /**
     * findLookup de Rota Coleta/entrega.
     * @param criteria
     * @return
     */
    public List findLookupRotaColetaEntrega(Map criteria){
    	Map idFilial = new HashMap();
    	idFilial.put("idFilial", criteria.get("idFilial"));
    	criteria.put("filial", idFilial);
    	criteria.remove("idFilial");
    	
    	List list = rotaColetaEntregaService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega)iter.next();
    		Map map = new HashMap();

       		map.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());    		
    		map.put("nrRota", rotaColetaEntrega.getNrRota());
    		map.put("dsRota", rotaColetaEntrega.getDsRota());
    		retorno.add(map);
    	}
    	return retorno;		
	}
    
    /**
     * findLookup de Meio de transporte.
     * @param criteria
     * @return
     */
    public List findLookupMeioTransporte(Map criteria) {
    	return meioTransporteService.findLookup(criteria);
    }
    
    
    public ResultSetPage findPaginatedLookup(TypedFlatMap criteria) {
    	TypedFlatMap map = this.montaCriteriaFindPaginated(criteria);
    	ResultSetPage rsp = manifestoEntregaService.findPaginatedLookup(map);
    	List novaLista= new ArrayList();
    	for(Iterator iter= rsp.getList().iterator();iter.hasNext();){
    		TypedFlatMap mapRetorno = (TypedFlatMap)iter.next();
    		Map mapNovo= new HashMap();
    		mapNovo.put("idManifestoEntrega", mapRetorno.getLong("idManifestoEntrega"));
    		mapNovo.put("tpManifestoEntrega", mapRetorno.getDomainValue("manifesto.tpManifestoEntrega"));
    		mapNovo.put("sgFilialManifesto", mapRetorno.getString("filial.sgFilial"));
    		mapNovo.put("nrManifestoEntrega", mapRetorno.getInteger("nrManifestoEntrega"));
    		mapNovo.put("dhEmissaoManifesto", mapRetorno.getDateTime("manifesto.dhEmissaoManifesto"));
    		
    		if(mapRetorno.get("manifesto.controleCarga.rotaColetaEntrega.nrRota")!= null)
    			mapNovo.put("nrRota", mapRetorno.getShort("manifesto.controleCarga.rotaColetaEntrega.nrRota"));
    		
    		mapNovo.put("dsRota", mapRetorno.getString("manifesto.controleCarga.rotaColetaEntrega.dsRota"));
    		mapNovo.put("nrFrota", mapRetorno.getString("manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota"));
    		mapNovo.put("nrIdentificador", mapRetorno.getString("manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
    		mapNovo.put("tpStatusManifesto", mapRetorno.getDomainValue("manifesto.tpStatusManifesto"));
    		novaLista.add(mapNovo);
    		
    	}
    	rsp.setList(novaLista);
    	return rsp;
    }
    
    public Integer getRowCountLookup(TypedFlatMap criteria) {
    	TypedFlatMap map = this.montaCriteriaFindPaginated(criteria);
    	return manifestoEntregaService.getRowCountLookup(map);
    }

    
	private TypedFlatMap montaCriteriaFindPaginated(TypedFlatMap criteria){
		if(criteria.getLong("idFilial")!= null)
			criteria.put("filial.idFilial", criteria.getLong("idFilial"));
		
		if(criteria.getDomainValue("tpManifestoEntrega")!= null)
			criteria.put("manifesto.tpManifestoEntrega", criteria.getDomainValue("tpManifestoEntrega").getValue());
		
		if(criteria.getDomainValue("tpStatusManifesto")!= null)
			criteria.put("manifesto.tpStatusManifesto", criteria.getDomainValue("tpStatusManifesto").getValue());
		
		if(criteria.getInteger("nrManifestoEntrega")!= null)
			criteria.put("nrManifestoEntrega", criteria.getInteger("nrManifestoEntrega"));
		
		if(criteria.getLong("idMeioTransporte")!= null)
			criteria.put("manifesto.controleCarga.meioTransporteByIdTransportado.idMeioTransporte", criteria.getLong("idMeioTransporte"));
		
		if(criteria.getLong("idRotaColetaEntrega")!= null)
			criteria.put("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", criteria.getLong("idRotaColetaEntrega"));
		
		if(criteria.getYearMonthDay("dhEmisssaoManifestoInicial")!= null)
			criteria.put("manifesto.dhEmisssaoManifestoInicial",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhEmisssaoManifestoInicial")));
		
		if(criteria.getYearMonthDay("dhEmisssaoManifestoFinal")!= null)
			criteria.put("manifesto.dhEmisssaoManifestoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhEmisssaoManifestoFinal")));
		
		if(criteria.getLong("idControleCarga")!= null)
			criteria.put("manifesto.controleCarga.idControleCarga", criteria.getLong("idControleCarga"));
		
		return criteria;
	}


    /**
     * Valida se a filial (filial do usu�rio) � MTZ (matriz)
     * @param idFilial Identificador da filial do usu�rio
     * @return <code>true</code> se a filial do usu�rio for MTZ, </code>false</code> caso contr�rio
     */
    public Map validateFilialUsuarioMatriz(Map criteria) {
    	Map mapResult = new HashMap();
    	mapResult.put("isMatriz", Boolean.valueOf(historicoFilialService.validateFilialUsuarioMatriz((Long)criteria.get("idFilial"))));
    	return mapResult;
    }    
}