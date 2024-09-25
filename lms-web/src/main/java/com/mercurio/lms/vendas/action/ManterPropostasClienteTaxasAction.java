/**
 * 
 */
package com.mercurio.lms.vendas.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.model.service.TaxaClienteService;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 * @author Luis Carlos Poletto
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterPropostasClienteTaxasAction"
 */
public class ManterPropostasClienteTaxasAction extends CrudAction {
	
	private ParcelaPrecoService parcelaPrecoService;
	
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	
	private TaxaClienteService taxaClienteService;
	
	private SimulacaoService simulacaoService;

	public TypedFlatMap findById(Long idTaxa) {
		Map taxaCliente = (Map) getTaxaClienteService().findByIdMap(idTaxa);
		
		if (taxaCliente != null) {
			TypedFlatMap result = new TypedFlatMap();
			result.put("idTaxaCliente", taxaCliente.get("idTaxaCliente"));
			result.put("taxaCliente.idTaxaCliente", taxaCliente.get("idTaxaCliente"));
			result.put("tpTaxaIndicador", taxaCliente.get("tpTaxaIndicador"));
			result.put("vlTaxa", taxaCliente.get("vlTaxa"));
			result.put("pcReajTaxa", taxaCliente.get("pcReajTaxa"));
			result.put("psMinimo", taxaCliente.get("psMinimo"));
			result.put("vlExcedente", taxaCliente.get("vlExcedente"));
			result.put("pcReajVlExcedente", taxaCliente.get("pcReajVlExcedente"));
			
			HashMap parcelaPreco = (HashMap) taxaCliente.get("parcelaPreco");
			if (parcelaPreco != null) {
				result.put("parcelaPreco.nmParcelaPreco", parcelaPreco.get("nmParcelaPreco"));
				result.put("parcelaPreco.idParcelaPreco", parcelaPreco.get("idParcelaPreco"));
			}
			
			return result;
		}
		return null;
	}
	
	public TypedFlatMap store(TypedFlatMap data) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		getSimulacaoService().validaUpdateSimulacao(simulacao);
		
		TaxaCliente taxaCliente = new TaxaCliente();
		Long idTaxaCliente = data.getLong("taxaCliente.idTaxaCliente");
		if (idTaxaCliente != null) {
			taxaCliente.setIdTaxaCliente(idTaxaCliente);
		}
		
		Long idParametroCliente = data.getLong("parametroCliente.idParametroCliente");
		if (idParametroCliente != null) {
			ParametroCliente parametroCliente = new ParametroCliente();
			parametroCliente.setIdParametroCliente(idParametroCliente);
			taxaCliente.setParametroCliente(parametroCliente);
		}
		
		Long idParcelaPreco = data.getLong("parcelaPreco.idParcelaPreco");
		if (idParcelaPreco != null) {
			ParcelaPreco parcelaPreco = new ParcelaPreco();
			parcelaPreco.setIdParcelaPreco(idParcelaPreco);
			taxaCliente.setParcelaPreco(parcelaPreco);
		}
		
		taxaCliente.setTpTaxaIndicador(new DomainValue(data.getString("tpTaxaIndicador")));
		taxaCliente.setVlTaxa(data.getBigDecimal("vlTaxa"));
		taxaCliente.setPsMinimo(data.getBigDecimal("psMinimo"));
		taxaCliente.setVlExcedente(data.getBigDecimal("vlExcedente"));
		taxaCliente.setPcReajTaxa(null);
		taxaCliente.setPcReajVlExcedente(null);
		
		simulacao = getTaxaClienteService().storeProposta(taxaCliente, simulacao.getIdSimulacao());
		SimulacaoUtils.setSimulacaoInSession(simulacao);
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("taxaCliente.idTaxaCliente", taxaCliente.getIdTaxaCliente());
		return result;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		getSimulacaoService().validateExclusao(simulacao);
		
		getTaxaClienteService().removeByIds(ids);
		
		getSimulacaoService().storePendenciaAprovacaoProposta(simulacao, false);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getTaxaClienteService().findPaginatedByParametroClienteProposta(criteria); 
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		
		List result = rsp.getList();
		if (result != null && result.size() > 0) {
			List newResult = new ArrayList(result.size());
			
			for (int i = 0; i < result.size(); i++) {
				TaxaCliente tc = (TaxaCliente) result.get(i);
				
				String vlTaxa = FormatUtils.formatValorComIndicador(
						tc.getTpTaxaIndicador().getValue(), 
						tc.getVlTaxa(),
						simulacao.getTabelaPreco().getMoeda().getDsSimbolo(),
						"DM_INDICADOR_PARAMETRO_CLIENTE");
				
				TypedFlatMap taxaCliente = new TypedFlatMap();
				taxaCliente.put("idTaxaCliente", tc.getIdTaxaCliente());
				taxaCliente.put("tpTaxaIndicador", tc.getTpTaxaIndicador());
				taxaCliente.put("vlTaxa", vlTaxa);
				taxaCliente.put("psMinimo", tc.getPsMinimo());
				taxaCliente.put("vlExcedente", tc.getVlExcedente());
				taxaCliente.put("parcelaPreco.nmParcelaPreco", tc.getParcelaPreco().getNmParcelaPreco());
				taxaCliente.put("pcReajTaxa", tc.getPcReajTaxa());
				taxaCliente.put("pcReajVlExcedente", tc.getPcReajVlExcedente());
				
				newResult.add(taxaCliente);
			}
			rsp.setList(newResult);
		}
		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getTaxaClienteService().getRowCountByParametroClienteProposta(criteria);
	}
	
	public List findTaxaCliente(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		return tabelaPrecoParcelaService.findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco("T", "T", idTabelaPreco);
	}
	
	/**
	 * @return Returns the parcelaPrecoService.
	 */
	public ParcelaPrecoService getParcelaPrecoService() {
		return parcelaPrecoService;
	}

	/**
	 * @param parcelaPrecoService The parcelaPrecoService to set.
	 */
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	/**
	 * @return Returns the taxaClienteService.
	 */
	public TaxaClienteService getTaxaClienteService() {
		return taxaClienteService;
	}

	/**
	 * @param taxaClienteService The taxaClienteService to set.
	 */
	public void setTaxaClienteService(TaxaClienteService taxaClienteService) {
		this.taxaClienteService = taxaClienteService;
	}

	/**
	 * @return Returns the simulacaoService.
	 */
	public SimulacaoService getSimulacaoService() {
		return simulacaoService;
	}

	/**
	 * @param simulacaoService The simulacaoService to set.
	 */
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}

	/**
	 * @param tabelaPrecoParcelaService The tabelaPrecoParcelaService to set.
	 */
	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
	
}
