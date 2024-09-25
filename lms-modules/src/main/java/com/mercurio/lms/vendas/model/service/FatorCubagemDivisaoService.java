package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.FatorCubagemDivisao;
import com.mercurio.lms.vendas.model.dao.FatorCubagemDivisaoDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * @spring.bean id="lms.vendas.fatorCubagemDivisaoService"
 */
public class FatorCubagemDivisaoService extends CrudService<FatorCubagemDivisao, Long> {

	
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = getFatorCubagemDivisaoDAO().findPaginated((TypedFlatMap)criteria, FindDefinition.createFindDefinition(criteria));
        rsp.setList(clearDate(rsp.getList()));
		return rsp;
	}
	
    private List<FatorCubagemDivisao> clearDate(List<FatorCubagemDivisao> list) {
        for (FatorCubagemDivisao fcd: list) {
            if (JTDateTimeUtils.MAX_YEARMONTHDAY.equals(fcd.getDtVigenciaFinal())) {
                fcd.setDtVigenciaFinal(null);
            }
        }
        return list;
    }

	public Integer getRowCount(TypedFlatMap criteria) {
		return getFatorCubagemDivisaoDAO().getRowCount(criteria);
	}

	@Override
	protected FatorCubagemDivisao beforeInsert(FatorCubagemDivisao bean) {
	    bean.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		
		YearMonthDay dtVigenciaInicial =  bean.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = bean.getDtVigenciaFinal();
		
		validaVigenciaInicial(dtVigenciaInicial);
		validaVigenciaFinal(dtVigenciaInicial, dtVigenciaFinal);
		
		FatorCubagemDivisao outro = getFatorCubagemDivisaoDAO().findFatorCubagemDivisaoVigenteMaxima(bean.getDivisaoCliente().getIdDivisaoCliente());
		
		if (outro != null) {
			outro.setDtVigenciaFinal(bean.getDtVigenciaInicial().minusDays(1));
			getFatorCubagemDivisaoDAO().store(outro);
		}
		
		
		validaCubagemVigente(bean);
		

		return super.beforeInsert(bean);
	}
	
	@Override
	protected FatorCubagemDivisao beforeUpdate(FatorCubagemDivisao bean) {
        
	    validaCubagemVigente(bean);
        
        return super.beforeUpdate(bean);
	}

    private void validaCubagemVigente(FatorCubagemDivisao bean) {
        List<FatorCubagemDivisao> outros = getFatorCubagemDivisaoDAO().findFatorCubagemConflitoByIdDivisaoCliente(bean.getDivisaoCliente().getIdDivisaoCliente(), bean.getIdFatorCubagemDivisao(), bean.getDtVigenciaInicial(), bean.getDtVigenciaFinal());
        if (outros != null && ! outros.isEmpty()) {
            throw new BusinessException("LMS-00047");
        }
    }


	private void validaVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		if (dtVigenciaInicial.compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
			throw new BusinessException("LMS-00006");
		}
	}
	
	private void validaVigenciaFinal(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		if (dtVigenciaFinal != null && dtVigenciaFinal.compareTo(dtVigenciaInicial) < 0) {
			throw new BusinessException("LMS-00008");
		}
	}
	
	public FatorCubagemDivisaoDAO getFatorCubagemDivisaoDAO() {
		return (FatorCubagemDivisaoDAO) getDao();
	}
	
	public void setFatorCubagemDivisaoDAO(FatorCubagemDivisaoDAO dao) {
		setDao( dao );
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	@Override
	public Serializable store(FatorCubagemDivisao bean) {
		return super.store(bean);
	}
	
	@Override
	protected Serializable findById(Long id) {
		return super.findById(id);
	}

	public FatorCubagemDivisao findFatorCubagemVigenteByIdDivisaoCliente(Long idDivisaoCliente) {
		return getFatorCubagemDivisaoDAO().findFatorCubagemVigenteByIdDivisaoCliente(idDivisaoCliente);
	}
}
