package com.mercurio.lms.contasreceber.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.report.ComprovanteEntregaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Hector Julian Esnaola Junior
 * 
 * @spring.bean id="lms.contasreceber.emitirComprovanteEntregaAction"
 */

public class EmitirComprovanteEntregaAction extends ReportActionSupport {

	/** Inversion of control - Spring (DocumentosFaturarService) */
	public void setComprovanteEntregaService(ComprovanteEntregaService comprovanteEntregaService) {
		this.reportServiceSupport = comprovanteEntregaService;
	}
	
	private FaturaService faturaService;
	public void setFaturaService(FaturaService faturaService){
		this.faturaService = faturaService;
	}
	
	private FilialService filialService;
	public void setFilialService(FilialService filialService){
		this.filialService = filialService;
	}
	
    public List findFaturaByFilial(TypedFlatMap map){
        Long nrFatura = map.getLong("nrFatura");
        Long sgFilial = map.getLong("idFilial");                
        return this.faturaService.findByNrFaturaByFilial(nrFatura, sgFilial);
    }	

    public List findFilial(Map map){
		return this.filialService.findLookup(map);
	}
    
    /**
     * Busca a filial de acordo a sigla passada digitada na lookup
     * @param criteria
     * @return List
     */
    public List findLookupFilial(TypedFlatMap criteria){ 
        return filialService.findLookupBySgFilial(criteria.getString("sgFilial"), criteria.getString("tpAcesso"));
    }
 
    /**
	 * Retorna a filial do usuário logado 
	 *
	 * @author José Rodrigo Moraes
	 * @since 25/01/2007
	 *
	 * @return idFilialUsuarioLogado Identificador da filial do usuário logado
	 */
	public TypedFlatMap findFilialUsuarioLogado(){
		TypedFlatMap tfm = new TypedFlatMap();
		Filial filial = SessionUtils.getFilialSessao();		
		tfm.put("filialByIdFilial.idFilial",filial.getIdFilial());
		tfm.put("filialByIdFilial.sgFilial",filial.getSgFilial());
		tfm.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		return tfm;
	}

}
