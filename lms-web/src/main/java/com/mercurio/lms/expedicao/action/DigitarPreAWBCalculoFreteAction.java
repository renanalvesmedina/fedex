/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.CalculoFreteCiaAerea;
import com.mercurio.lms.expedicao.model.service.CalculoFreteCiaAereaService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TarifaSpotService;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.expedicao.digitarPreAWBCalculoFreteAction"
 * 
 */
public class DigitarPreAWBCalculoFreteAction extends CrudAction {

	private AeroportoService aeroportoService;
	private CalculoFreteCiaAereaService calculoFreteCiaAereaService;
	private TarifaSpotService tarifaSpotService;

	public List calculaFreteAwb(TypedFlatMap parameters) {
		Awb awb = AwbUtils.getAwbInSession();

		List calculosFreteAWB = calculoFreteCiaAereaService.executeCalculosFreteAWB(awb);
		SessionContext.set(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculosFreteAWB);

		return createResult(calculosFreteAWB);
	}

	private List createResult(List<CalculoFreteCiaAerea> calculosFreteAWB) {
		if( (calculosFreteAWB == null) || (calculosFreteAWB.isEmpty()) ) {
			return null;
		}
		List result = new ArrayList();
		TypedFlatMap calculo = null;
		TabelaPreco tabelaPreco = null;

		sortList(calculosFreteAWB);
		for (CalculoFreteCiaAerea calculoFreteCiaAerea : calculosFreteAWB) {
			calculo = new TypedFlatMap();
			tabelaPreco = calculoFreteCiaAerea.getTabelaPreco();
			calculo.put("idCalculoFrete", calculoFreteCiaAerea.getIdCalculoFrete());
			calculo.put("vlTotal", calculoFreteCiaAerea.getVlTotal()); 
			calculo.put("nmPessoa", tabelaPreco.getTipoTabelaPreco().getEmpresaByIdEmpresaCadastrada().getPessoa().getNmPessoa());
			calculo.put("dsTabela", tabelaPreco.getDsDescricao());
			calculo.put("blTarifaSpot", calculoFreteCiaAerea.getBlTarifaSpot());
			calculo.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());

			result.add(calculo);
		}

		return result;
	}

	private void sortList(List list) {
		Comparator comparator = new Comparator() {
			public int compare(Object arg0, Object arg1) {
				CalculoFreteCiaAerea obj1 = (CalculoFreteCiaAerea) arg0;
				CalculoFreteCiaAerea obj2 = (CalculoFreteCiaAerea) arg1;
				return obj1.getVlTotal().compareTo(obj2.getVlTotal());
			}
		};
		Collections.sort(list, comparator);
	}

	public List findAeroporto(TypedFlatMap criteria) {
		return getAeroportoService().findLookupAeroporto(criteria);
	}

	/**
	 * @return Returns the aeroportoService.
	 */
	public AeroportoService getAeroportoService() {
		return aeroportoService;
	}

	/**
	 * @param aeroportoService
	 *            The aeroportoService to set.
	 */
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	/**
	 * @return Returns the calculoFreteCiaAereaService.
	 */
	public CalculoFreteCiaAereaService getCalculoFreteCiaAereaService() {
		return calculoFreteCiaAereaService;
	}

	/**
	 * @param calculoFreteCiaAereaService The calculoFreteCiaAereaService to set.
	 */
	public void setCalculoFreteCiaAereaService(
			CalculoFreteCiaAereaService calculoFreteCiaAereaService) {
		this.calculoFreteCiaAereaService = calculoFreteCiaAereaService;
	}

	/**
	 * @return Returns the tarifaSpotService.
	 */
	public TarifaSpotService getTarifaSpotService() {
		return tarifaSpotService;
	}

	/**
	 * @param tarifaSpotService The tarifaSpotService to set.
	 */
	public void setTarifaSpotService(TarifaSpotService tarifaSpotService) {
		this.tarifaSpotService = tarifaSpotService;
	}
}
