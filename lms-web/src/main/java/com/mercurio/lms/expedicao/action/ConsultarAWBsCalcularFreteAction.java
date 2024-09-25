/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;

/**
 * @author Luis Carlos poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.consultarAWBsCalcularFreteAction"
 *
 */
public class ConsultarAWBsCalcularFreteAction extends CrudAction {

	public List findPaginated(TypedFlatMap criteria) {	
		List result = new ArrayList();
		Map parcela = null;
		
		parcela = getParcelaPrecoService().findParcelaByCdParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);
		adicionaParcela(result, criteria.getBigDecimal("vlFretePeso"), parcela);
		
		parcela = getParcelaPrecoService().findParcelaByCdParcelaPreco(ConstantesExpedicao.CD_TAXA_TERRESTRE);
		adicionaParcela(result, criteria.getBigDecimal("vlTaxaTerrestre"), parcela);
		
		parcela = getParcelaPrecoService().findParcelaByCdParcelaPreco(ConstantesExpedicao.CD_TAXA_COMBUSTIVEL);
		adicionaParcela(result, criteria.getBigDecimal("vlTaxaCombustivel"), parcela);
		
		return result;
	}
	
	private void adicionaParcela(List result, BigDecimal valor, Map parcela) {
		if (parcela != null && valor != null) {
			TypedFlatMap data = new TypedFlatMap();
			data.put("nmParcelaPreco", parcela.get("nmParcelaPreco"));
			data.put("vlParcelaPreco", valor);
			result.add(data);
		}
	}
	
	public void setService(ParcelaPrecoService serviceService) {
		super.defaultService = serviceService;
	}
	
	public ParcelaPrecoService getParcelaPrecoService() {
		return (ParcelaPrecoService) super.defaultService;
	}
	
}
