package com.mercurio.lms.portaria.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.Portaria;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.service.PortariaService;
import com.mercurio.lms.portaria.model.service.TerminalService;
import com.mercurio.lms.util.JTVigenciaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.portaria.manterPortariasAction"
 */

public class ManterPortariasAction extends CrudAction {
	public void setService(PortariaService serviceService) {
		this.defaultService = serviceService;
	}

    private FilialService filialService;
    private TerminalService terminalService;
    
    public Serializable store(TypedFlatMap beanMap) {
    	Filial filial = new Filial();
    		   filial.setIdFilial(beanMap.getLong("terminal.filial.idFilial"));
    	
		Terminal terminal = new Terminal();
			     terminal.setIdTerminal(beanMap.getLong("terminal.idTerminal"));
			     terminal.setFilial(filial);
			     
		Portaria bean = new Portaria();
				 bean.setIdPortaria(beanMap.getLong("idPortaria"));
				 bean.setDtVigenciaInicial(beanMap.getYearMonthDay("dtVigenciaInicial"));
				 bean.setDtVigenciaFinal(beanMap.getYearMonthDay("dtVigenciaFinal"));
				 bean.setDsPortaria(beanMap.getString("dsPortaria"));
				 bean.setNrPortaria(beanMap.getByte("nrPortaria"));
				 bean.setBlPadraoFilial(beanMap.getBoolean("blPadraoFilial"));
				 bean.setTerminal(terminal);
				 bean.setTpFuncao(beanMap.getDomainValue("tpFuncao"));
				 
    	getPortariaService().store(bean);
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("idPortaria",bean.getIdPortaria());
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
    	retorno.put("acaoVigenciaAtual",acaoVigencia);
    	
    	return retorno;
    }

	/**
	 * @return
	 */
	private PortariaService getPortariaService() {
		return (PortariaService)defaultService;
	}

    public void removeById(java.lang.Long id) {
        getPortariaService().removeById(id);
    }
    
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getPortariaService().removeByIds(ids);
    }

    public Map findById(java.lang.Long id) {
    	Portaria portaria = getPortariaService().findById(id);
    	Terminal terminal = portaria.getTerminal();
    	Filial filial = terminal.getFilial();
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("idPortaria",portaria.getIdPortaria());
    	retorno.put("terminal.filial.idFilial",filial.getIdFilial());
    	retorno.put("terminal.filial.sgFilial",filial.getSgFilial());
    	retorno.put("terminal.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
    	retorno.put("terminal.idTerminal",terminal.getIdTerminal());
    	retorno.put("idTerminalTemp",terminal.getIdTerminal());
    	retorno.put("terminal.dtVigenciaInicial",terminal.getDtVigenciaInicial());
    	retorno.put("terminal.dtVigenciaFinal",terminal.getDtVigenciaFinal());
    	retorno.put("blPadraoFilial",portaria.getBlPadraoFilial());
    	retorno.put("nrPortaria",portaria.getNrPortaria());
    	retorno.put("dsPortaria",portaria.getDsPortaria());
    	retorno.put("tpFuncao.value",portaria.getTpFuncao().getValue());
    	retorno.put("dtVigenciaInicial",portaria.getDtVigenciaInicial());
    	retorno.put("dtVigenciaFinal",portaria.getDtVigenciaFinal());
    	
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(portaria);
    	retorno.put("acaoVigenciaAtual",acaoVigencia);
    	
    	return retorno;
    }
    
    public TypedFlatMap findFilialUsuarioLogado() {
    	Filial f = filialService.getFilialUsuarioLogado();
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idFilial",f.getIdFilial());
    	result.put("sgFilial",f.getSgFilial());
    	result.put("pessoa.nmFantasia",f.getPessoa().getNmPessoa());
    	return result;
    }
        
    public ResultSetPage findPaginated(Map criteria) {
    	return ((PortariaService)this.defaultService).findPaginated(criteria);
    }
    public Integer getRowCount(Map criteria) {
    	return ((PortariaService)this.defaultService).getRowCount(criteria);
    }
    
    /**
     * findLookup de filiais.
     * @param criteria
     * @return
     */
    public List findLookupFilial(Map criteria) {
		return filialService.findLookupFilial(criteria);
	}
	
    /**
     * find da combo de terminais vigentes.
     * @param criteria
     * @return
     */
	public List findTerminaisComboVigentes(TypedFlatMap criteria) {
		if (criteria.getLong("idTerminal") != null){
			List result = terminalService.findTerminalVigenteByFilial(criteria.getLong("filial.idFilial"), criteria.getLong("idTerminal"));
			return result;
		}
		return terminalService.findTerminalVigenteByFilial(criteria.getLong("filial.idFilial"));			
	}
	
	/**
     * find da combo de terminais.
     * @param criteria
     * @return
     */
	public List findTerminaisCombo(TypedFlatMap criteria) {
		return terminalService.findCombo(criteria);			
	}
    
	public FilialService getFilialService() {
		return filialService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public TerminalService getTerminalService() {
		return terminalService;
	}

	public void setTerminalService(TerminalService terminalService) {
		this.terminalService = terminalService;
	}
	
}
