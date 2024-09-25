/**
 * 
 */
package com.mercurio.lms.vendas.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.service.GeneralidadeClienteService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 * @author Luis Carlos Poletto
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterPropostasClienteGeneralidadesAction"
 */
public class ManterPropostasClienteGeneralidadesAction extends CrudAction {
	
	private ParcelaPrecoService parcelaPrecoService;
	
	private GeneralidadeClienteService generalidadeClienteService;
	
	private SimulacaoService simulacaoService;
	
	
	public List findGeneralidadesCliente(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
    	Long idPedagio = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO);
    	Long idGris = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_GRIS);
    	Long idAdvalorem1 = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_ADVALOREM_1);
    	Long idAdvalorem2 = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_ADVALOREM_2);
    	Long idTde = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_TDE);

    	Long idPedagioDocumento = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
       	Long idPedagioFaixaPeso = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
       	Long idPedagioFracao = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_FRACAO);
       	Long idPedagioPostoFracao = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

    	
    	return parcelaPrecoService.findGeneralidadesExcluindoAlgunsTipos(idTabelaPreco, new Long[]{idPedagio,idGris,idAdvalorem1,idAdvalorem2,idTde,idPedagioDocumento,idPedagioFaixaPeso,idPedagioFracao,idPedagioPostoFracao});

	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getGeneralidadeClienteService().findPaginatedByParametroClienteProposta(criteria); 
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		
		List result = rsp.getList();
		if (result != null && result.size() > 0) {
			List newResult = new ArrayList(result.size());
			
			for (int i = 0; i < result.size(); i++) {
				GeneralidadeCliente gc = (GeneralidadeCliente) result.get(i);
				
				String vlGeneralidade = FormatUtils.formatValorComIndicador(
						gc.getTpIndicador().getValue(), 
						gc.getVlGeneralidade(),
						simulacao.getTabelaPreco().getMoeda().getDsSimbolo(),
						"DM_INDICADOR_PARAMETRO_CLIENTE");
				
				TypedFlatMap generalidadeCliente = new TypedFlatMap();
				generalidadeCliente.put("idGeneralidadeCliente", gc.getIdGeneralidadeCliente());
				generalidadeCliente.put("tpIndicador", gc.getTpIndicador());
				generalidadeCliente.put("vlGeneralidade", vlGeneralidade);
				generalidadeCliente.put("parcelaPreco.nmParcelaPreco", gc.getParcelaPreco().getNmParcelaPreco());
				
				newResult.add(generalidadeCliente);
			}
			rsp.setList(newResult);
		}
		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getGeneralidadeClienteService().getRowCountByParametroClienteProposta(criteria);
	}
	
	public TypedFlatMap store(TypedFlatMap data) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		getSimulacaoService().validaUpdateSimulacao(simulacao);
		
		GeneralidadeCliente generalidadeCliente = new GeneralidadeCliente();
		Long idGeneralidadeCliente = data.getLong("generalidadeCliente.idGeneralidadeCliente");
		if (idGeneralidadeCliente != null) {
			generalidadeCliente.setIdGeneralidadeCliente(idGeneralidadeCliente);
		}
		
		Long idParametroCliente = data.getLong("parametroCliente.idParametroCliente");
		if (idParametroCliente != null) {
			ParametroCliente parametroCliente = new ParametroCliente();
			parametroCliente.setIdParametroCliente(idParametroCliente);
			generalidadeCliente.setParametroCliente(parametroCliente);
		}
		
		Long idParcelaPreco = data.getLong("parcelaPreco.idParcelaPreco");
		if (idParcelaPreco != null) {
			ParcelaPreco parcelaPreco = new ParcelaPreco();
			parcelaPreco.setIdParcelaPreco(idParcelaPreco);
			generalidadeCliente.setParcelaPreco(parcelaPreco);
		}
		
		generalidadeCliente.setTpIndicador(new DomainValue(data.getString("tpIndicador")));
		generalidadeCliente.setVlGeneralidade(data.getBigDecimal("vlGeneralidade"));
		generalidadeCliente.setPcReajGeneralidade(null);
		
		generalidadeCliente.setTpIndicadorMinimo(new DomainValue(data.getString("tpIndicadorMinimo")));
		generalidadeCliente.setVlMinimo(data.getBigDecimal("vlMinimo"));
		generalidadeCliente.setPcReajMinimo(new BigDecimal(0));
		
		simulacao = getGeneralidadeClienteService().storeProposta(generalidadeCliente, simulacao.getIdSimulacao());
		
		SimulacaoUtils.setSimulacaoInSession(simulacao);
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("generalidadeCliente.idGeneralidadeCliente", generalidadeCliente.getIdGeneralidadeCliente());
		return result;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		getSimulacaoService().validateExclusao(simulacao);
		
		getGeneralidadeClienteService().removeByIds(ids);
		
		getSimulacaoService().storePendenciaAprovacaoProposta(simulacao, false);
	}
	
	public TypedFlatMap findById(Long idGeneralidadeCliente) {
		Map generalidadeCliente = (Map) getGeneralidadeClienteService().findByIdMap(idGeneralidadeCliente);
		
		if (generalidadeCliente != null) {
			TypedFlatMap result = new TypedFlatMap();
			result.put("idGeneralidadeCliente", generalidadeCliente.get("idGeneralidadeCliente"));
			result.put("generalidadeCliente.idGeneralidadeCliente", generalidadeCliente.get("idGeneralidadeCliente"));
			result.put("tpIndicador", generalidadeCliente.get("tpIndicador"));
			result.put("vlGeneralidade", generalidadeCliente.get("vlGeneralidade"));
			result.put("tpIndicadorMinimo", generalidadeCliente.get("tpIndicadorMinimo"));
			result.put("vlMinimo", generalidadeCliente.get("vlMinimo"));
			
			HashMap parcelaPreco = (HashMap) generalidadeCliente.get("parcelaPreco");
			if (parcelaPreco != null) {
				result.put("parcelaPreco.idParcelaPreco", parcelaPreco.get("idParcelaPreco"));
				result.put("parcelaPreco.cdParcelaPreco", parcelaPreco.get("cdParcelaPreco"));
				result.put("parcelaPreco.nmParcelaPreco", parcelaPreco.get("nmParcelaPreco"));
			}
			
			return result;
		}
		return null;
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
	 * @return Returns the generalidadeClienteService.
	 */
	public GeneralidadeClienteService getGeneralidadeClienteService() {
		return generalidadeClienteService;
	}

	/**
	 * @param generalidadeClienteService The generalidadeClienteService to set.
	 */
	public void setGeneralidadeClienteService(
			GeneralidadeClienteService generalidadeClienteService) {
		this.generalidadeClienteService = generalidadeClienteService;
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

}
