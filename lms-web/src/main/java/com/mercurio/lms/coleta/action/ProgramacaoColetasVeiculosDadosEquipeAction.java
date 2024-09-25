package com.mercurio.lms.coleta.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.IntegranteEqOperacService;
import com.mercurio.lms.util.FormatUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.programacaoColetasVeiculosDadosEquipeAction"
 */

public class ProgramacaoColetasVeiculosDadosEquipeAction {
	
	private ControleCargaService controleCargaService;
	private IntegranteEqOperacService integranteEqOperacService;
	private EquipeOperacaoService equipeOperacaoService;

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public IntegranteEqOperacService getIntegranteEqOperacService() {
		return integranteEqOperacService;
	}
	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public EquipeOperacaoService getEquipeOperacaoService() {
		return equipeOperacaoService;
	}
	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.equipeOperacaoService = equipeOperacaoService;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
    public Map findById(java.lang.Long id) {
    	ControleCarga controleCarga = getControleCargaService().findById(id);
		TypedFlatMap map = new TypedFlatMap();
		map.put("idControleCarga", controleCarga.getIdControleCarga());
    	map.put("nrControleCarga", controleCarga.getNrControleCarga());
    	map.put("filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
    	if (controleCarga.getMeioTransporteByIdTransportado() != null) {
	    	map.put("meioTransporteByIdTransportado.nrFrota", controleCarga.getMeioTransporteByIdTransportado().getNrFrota());
	    	map.put("meioTransporteByIdTransportado.nrIdentificador", controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador());
    	}
    	return map;
    }
    
    /**
     * 
     * @param criteria
     * @return
     */
    public List findPaginatedDadosEquipe(TypedFlatMap criteria) {
    	List lista = getEquipeOperacaoService().
    			findEquipeOperacaoByIdControleCarga(criteria.getLong("controleCarga.idControleCarga"), Boolean.TRUE);
    	
    	FilterList filter = new FilterList(lista) {
			public Map filterItem(Object item) {
				EquipeOperacao equipeOperacao = (EquipeOperacao)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idEquipeOperacao", equipeOperacao.getIdEquipeOperacao());
		    	typedFlatMap.put("dhInicioOperacao", equipeOperacao.getDhInicioOperacao());
		    	typedFlatMap.put("dhFimOperacao", equipeOperacao.getDhFimOperacao());
		    	if (equipeOperacao.getEquipe() != null) {
		    		typedFlatMap.put("equipe.dsEquipe", equipeOperacao.getEquipe().getDsEquipe());
		    	}
		    	if (equipeOperacao.getControleCarga() != null) {
		    		typedFlatMap.put("controleCarga.idControleCarga", equipeOperacao.getControleCarga().getIdControleCarga());
		    	}
				return typedFlatMap;
			}
    	};
    	List listaRetorno = (List)filter.doFilter();
    	return listaRetorno;
    }


    /**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedIntegrantes(Map criteria) {
		criteria.put("_currentPage", "1");
		criteria.put("_pageSize", "1000");
    	ResultSetPage rsp = getIntegranteEqOperacService().findPaginated(criteria);
    	FilterList filter = new FilterList(rsp.getList()) {
			public Map filterItem(Object item) {
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac)item;
    			TypedFlatMap map = new TypedFlatMap();
	    		map.put("idIntegranteEqOperac", integranteEqOperac.getIdIntegranteEqOperac());
	    		map.put("tpIntegrante", integranteEqOperac.getTpIntegrante());
	    		if (integranteEqOperac.getCargoOperacional() != null) {
	    			map.put("cargoOperacional.dsCargo", integranteEqOperac.getCargoOperacional().getDsCargo());
	    		}
	    		if (integranteEqOperac.getUsuario() != null) {
	    			map.put("pessoa.nmPessoa", integranteEqOperac.getUsuario().getNmUsuario());
	    			map.put("usuario.nrMatricula", integranteEqOperac.getUsuario().getNrMatricula());
	    			map.put("pessoa.nrIdentificacaoFormatado", "");
	    		}
	    		if (integranteEqOperac.getPessoa() != null) {
		    		map.put("pessoa.nmPessoa", integranteEqOperac.getPessoa().getNmPessoa());
			    	map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(
			    			integranteEqOperac.getPessoa().getTpIdentificacao(), 
			    			integranteEqOperac.getPessoa().getNrIdentificacao()));
	    		}
	    		if (integranteEqOperac.getEmpresa() != null) {
	    			map.put("empresa.pessoa.nmPessoa", integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
	    		}
				return map;
			}
    	};
    	rsp.setList((List)filter.doFilter());
    	return rsp;
    }
}