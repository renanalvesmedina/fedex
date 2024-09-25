/**
 * 
 */
package com.mercurio.lms.tabelaprecos.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.tabelaprecos.model.service.ValorTaxaService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.simularNovosPrecosTaxasAction"
 */
public class SimularNovosPrecosTaxasAction extends CrudAction {
	
	private static long cont = 0;
	
	private ValorTaxaService valorTaxaService;
	
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;

	public List findTaxasByTabela(Long idTabelaPreco) {
		return getTabelaPrecoParcelaService().findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco("T", "T", idTabelaPreco);
	}
	
	public TypedFlatMap findValoresTaxa(TypedFlatMap criteria) {
		Long idTabelaPrecoParcela = criteria.getLong("taxa.idTabelaPrecoParcela");
		Long idTabelaPreco = criteria.getLong("tabelaBase.idTabelaPreco");
		TypedFlatMap result = getValorTaxaService().findByIdTabelaPrecoParcelaIdTabelaPreco(idTabelaPrecoParcela, idTabelaPreco);
		result.put("resetValues", criteria.get("resetValues"));
		return result;
	}
	
	public TypedFlatMap findById(Long idValorTaxa) {
		ValorTaxa valorTaxa = null;
		TypedFlatMap result = new TypedFlatMap();
		if (idValorTaxa.longValue() < 0) {
			valorTaxa = findByIdInSession(idValorTaxa);
			result.put("taxa.idTabelaPrecoParcela", valorTaxa.getTabelaPrecoParcela().getIdTabelaPrecoParcela());
		} else {
			valorTaxa = getValorTaxaService().findById(idValorTaxa);
			Long idTabelaBase = valorTaxa.getTabelaPrecoParcela().getTabelaPreco().getTabelaPreco().getIdTabelaPreco();
			Long idParcelaPreco = valorTaxa.getTabelaPrecoParcela().getParcelaPreco().getIdParcelaPreco();
			result.put("taxa.idTabelaPrecoParcela", getTabelaPrecoParcelaService().findByIdTabelaPrecoIdParcelaPreco(idTabelaBase, idParcelaPreco));
		}
		result.put("idValorTaxa", valorTaxa.getIdValorTaxa());
		result.put("parcelaPreco.dsParcelaPreco", valorTaxa.getTabelaPrecoParcela().getParcelaPreco().getDsParcelaPreco());
		result.put("vlExcedenteReajustado", valorTaxa.getVlExcedente());
		result.put("vlTaxaReajustado", valorTaxa.getVlTaxa());
		result.put("psTaxado", valorTaxa.getPsTaxado());
		return result;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			return getValorTaxaService().findPaginatedByIdTabelaPreco(criteria);
		}
		// busca os dados da sessao
		return findPaginatedInSession(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			return getValorTaxaService().getRowCountByIdTabelaPreco(criteria);
		}
		// busca os dados da sessao
		List taxas = getTaxasInSession();
		return Integer.valueOf(taxas.size());
	}
	
	public void store(TypedFlatMap data) {
		Long idTabelaPrecoParcela = data.getLong("taxa.idTabelaPrecoParcela");
		if (idTabelaPrecoParcela != null) {
			
			if ("true".equals(data.getString("_edit"))) {
				removeTabelaPrecoParcelaInSession(idTabelaPrecoParcela);
			} 
			
			verifyStore(idTabelaPrecoParcela);
			TabelaPrecoParcela tabelaPrecoParcela = getTabelaPrecoParcelaService().findById(idTabelaPrecoParcela);
			
			TabelaPrecoParcela tpp = new TabelaPrecoParcela();
			// na hora de salvar no banco deve-se retirar este id daqui, ele
			// so sera utilizado para evitar que a mesma tabelaPrecoParcela
			// seja inserida mais de uma vez no momento da inclusao
			tpp.setIdTabelaPrecoParcela(tabelaPrecoParcela.getIdTabelaPrecoParcela());
			tpp.setParcelaPreco(tabelaPrecoParcela.getParcelaPreco());
			
			ValorTaxa valorTaxa = new ValorTaxa();
			valorTaxa.setIdValorTaxa(Long.valueOf(--cont));
			valorTaxa.setPsTaxado(data.getBigDecimal("psTaxado"));
			valorTaxa.setVlExcedente(data.getBigDecimal("vlExcedenteReajustado"));
			valorTaxa.setVlTaxa(data.getBigDecimal("vlTaxaReajustado"));
			
			valorTaxa.setTabelaPrecoParcela(tpp);
			tpp.setValorTaxa(valorTaxa);
			
			List taxas = getTaxasInSession();
			taxas.add(tpp);
			setTaxasInSession(taxas);
		}
	}
	
	public TypedFlatMap removeTaxas(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		List ids = criteria.getList("ids");
		List newIds = new ArrayList();
		for (int i = 0; i < ids.size(); i++) {
			String id = (String) ids.get(i);
			newIds.add(Long.valueOf(id));
		}
		if (idTabelaPreco != null) {
			removeByIds(newIds);
		} else {
			removeByIdsInSession(newIds);
		}
		return null;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getValorTaxaService().removeByIds(ids);
	}
	
	private ValorTaxa findByIdInSession(Long idValorTaxa) {
		List taxas = getTaxasInSession();
		for (int i = 0; i < taxas.size(); i++) {
			TabelaPrecoParcela tpp = (TabelaPrecoParcela) taxas.get(i);
			if (idValorTaxa.equals(tpp.getValorTaxa().getIdValorTaxa())) {
				return tpp.getValorTaxa();
			}
		}
		
		return null;
	}
	
	private ResultSetPage findPaginatedInSession(TypedFlatMap criteria) {
		ResultSetPage rsp = ResultSetPage.EMPTY_RESULTSET;
		List taxas = getTaxasInSession();
		if (taxas != null && taxas.size() > 0) {
			FindDefinition def = FindDefinition.createFindDefinition(criteria);
			
			Comparator ordenador = new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return 0;
				}
			};
			
			rsp = MasterDetailAction.getResultSetPage(taxas, def.getCurrentPage(), def.getPageSize(), ordenador);
			
			List newList = new ArrayList();
			List oldList = rsp.getList();
			for (int i = 0; i < oldList.size(); i++) {
				TabelaPrecoParcela tpp = (TabelaPrecoParcela) oldList.get(i);
				TypedFlatMap parcela = new TypedFlatMap();
				parcela.put("idValorTaxa", tpp.getValorTaxa().getIdValorTaxa());
				parcela.put("parcelaPreco.dsParcelaPreco", tpp.getParcelaPreco().getDsParcelaPreco());
				parcela.put("valorTaxa.vlTaxa", tpp.getValorTaxa().getVlTaxa());
				parcela.put("valorTaxa.psTaxado", tpp.getValorTaxa().getPsTaxado());
				parcela.put("valorTaxa.vlExcedente", tpp.getValorTaxa().getVlExcedente());
				
				newList.add(parcela);
			}
			rsp.setList(newList);
		}
		return rsp;
	}
	
	private void removeByIdsInSession(List ids) {
		for (int i = 0; i < ids.size(); i++) {
			Long id = (Long) ids.get(i);
			removeByIdInSession(id);
		}
	}
	
	private void removeByIdInSession(Long id) {
		List taxas = getTaxasInSession();
		for (Iterator it = taxas.iterator(); it.hasNext();) {
			TabelaPrecoParcela tpp = (TabelaPrecoParcela) it.next();
			if (id.equals(tpp.getValorTaxa().getIdValorTaxa())) {
				it.remove();
			}
		}
	}
	
	private void verifyStore(Long idTabelaPrecoParcela) {
		List taxas = getTaxasInSession();
		TabelaPrecoParcela tpp = new TabelaPrecoParcela();
		tpp.setIdTabelaPrecoParcela(idTabelaPrecoParcela);
		
		if (taxas.indexOf(tpp) >= 0) {
			throw new BusinessException("LMS-30044");
		}
	}
	
	private void removeTabelaPrecoParcelaInSession(Long idTabelaPrecoParcela) {
		List taxas = getTaxasInSession();
		TabelaPrecoParcela tpp = new TabelaPrecoParcela();
		tpp.setIdTabelaPrecoParcela(idTabelaPrecoParcela);
		
		int index = taxas.indexOf(tpp); 
		if (index >= 0) {
			taxas.remove(index);
			setTaxasInSession(taxas);
		}
	}
	
	private List getTaxasInSession() {
		List taxas = (List) SessionContext.get(ConstantesTabelaPrecos.TAXAS_IN_SESSION);
		if (taxas == null) {
			taxas = new ArrayList();
			setTaxasInSession(taxas);
		}
		return taxas;
	}
	
	private void setTaxasInSession(List taxas) {
		SessionContext.set(ConstantesTabelaPrecos.TAXAS_IN_SESSION, taxas);
	}

	/**
	 * @return Returns the valorTaxaService.
	 */
	public ValorTaxaService getValorTaxaService() {
		return valorTaxaService;
	}

	/**
	 * @param valorTaxaService The valorTaxaService to set.
	 */
	public void setValorTaxaService(ValorTaxaService valorTaxaService) {
		this.valorTaxaService = valorTaxaService;
	}

	/**
	 * @return Returns the tabelaPrecoParcelaService.
	 */
	public TabelaPrecoParcelaService getTabelaPrecoParcelaService() {
		return tabelaPrecoParcelaService;
	}

	/**
	 * @param tabelaPrecoParcelaService The tabelaPrecoParcelaService to set.
	 */
	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
	
}
