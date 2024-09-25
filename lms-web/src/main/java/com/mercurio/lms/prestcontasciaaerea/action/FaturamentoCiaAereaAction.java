package com.mercurio.lms.prestcontasciaaerea.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.prestcontasciaaerea.model.FaturamentoCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.service.FaturamentoCiaAereaService;
/**
* @spring.bean id="lms.prestcontasciaaerea.faturamentoCiaAereaAction"
*/
public class FaturamentoCiaAereaAction  extends CrudAction {
	private DomainValueService domainValueService;
	private CiaFilialMercurioService ciaFilialMercurioService;

	public void setFaturamentoCiaAereaService( FaturamentoCiaAereaService faturamentoCiaAereaService) {
		this.defaultService = faturamentoCiaAereaService;
	}

	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = ((FaturamentoCiaAereaService)defaultService).findPaginated(criteria);
		List lista = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			FaturamentoCiaAerea ps = (FaturamentoCiaAerea)iter.next();
			Map map = new HashMap();
			map.put("idFaturamentoCiaAerea" , ps.getIdFaturamentoCiaAerea());
			map.put("nmciaFilialMercurio" , ps.getCiaFilialMercurio().getEmpresa().getPessoa().getNmPessoa());
			map.put("filialsgFilial" , ps.getCiaFilialMercurio().getFilial().getSgFilial());
			map.put("filialnmFantasia" , ps.getCiaFilialMercurio().getFilial().getPessoa().getNmFantasia());
			
			map.put("comissao" , ps.getPcComissao());
			map.put("periocidade" , ps.getTpPeriodicidade().getDescriptionAsString());
			
			map.put("dtVigenciaFinal" , ps.getDtVigenciaFinal());
			map.put("dtVigenciaInicial" , ps.getDtVigenciaInicial());
			
			if(ps.getTpPeriodicidade().getValue().equals("S")){
				DomainValue dv = this.domainValueService.findDomainValueByValue("DM_DIAS_SEMANA", ps.getDdFaturamento().toString()) ;
				map.put("ddFaturamento" , dv.getDescription() );
			} else if(ps.getTpPeriodicidade().getValue().equals("D")){
				map.put("ddFaturamento" , null );
			} else {
				map.put("ddFaturamento" , ps.getDdFaturamento() );
			}
			
			lista.add(map);
		}
		rsp.setList(lista);
		return rsp;
	}
	
    public FaturamentoCiaAerea findById(java.lang.Long id) {
    	return ((FaturamentoCiaAereaService)defaultService).findById(id);
    }
    
    public Serializable store(TypedFlatMap criteria) {
    	FaturamentoCiaAerea bean = new FaturamentoCiaAerea();
    	bean.setIdFaturamentoCiaAerea(criteria.getLong("idFaturamentoCiaAerea"));
    	
    	CiaFilialMercurio cfm = getCiaFilialMercurioService().findByIdCiaAereaIdFilial(criteria.getLong("ciaFilialMercurio.empresa.idEmpresa" ), criteria.getLong("ciaFilialMercurio.filial.idFilial"));
    	if(cfm == null){
    		throw new BusinessException("LMS-37008");
    	}
    	bean.setCiaFilialMercurio(cfm);
    	
    	bean.setNrPrazoPagamento(criteria.getByte("nrPrazoPagamento"));
    	bean.setDtVigenciaFinal(criteria.getYearMonthDay("dtVigenciaFinal"));
    	bean.setDtVigenciaInicial(criteria.getYearMonthDay("dtVigenciaInicial"));
    	bean.setTpPeriodicidade(criteria.getDomainValue("tpPeriodicidade"));
    	bean.setPcComissao(criteria.getBigDecimal("pcComissao"));
    	
    	bean.setDdFaturamento(criteria.getByte("ddFaturamento"));
    	if (bean.getDdFaturamento() == null) {
    		bean.setDdFaturamento(criteria.getByte("tpDiaSemana"));
		}
    	    	    	    
    	return ((FaturamentoCiaAereaService)defaultService).store(bean);
    }
    
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	super.removeByIds(ids);
    }
    
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }
    
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setCiaFilialMercurioService(CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}

	public CiaFilialMercurioService getCiaFilialMercurioService() {
		return ciaFilialMercurioService;
	}
}