/**
 * 
 */
package com.mercurio.lms.tabelaprecos.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.GeneralidadeService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.simularNovosPrecosGeneralidadesAction"
 */
public class SimularNovosPrecosGeneralidadesAction extends CrudAction {
	
	private static long cont = 0;
	
	private GeneralidadeService generalidadeService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	
	public List findGeneralidadesByTabela(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaBase.idTabelaPreco");
		Long idTabelaPrecoGerada = criteria.getLong("tabelaPreco.idTabelaPreco");
		List result = tabelaPrecoParcelaService.findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco("G", "G", idTabelaPreco);
		if (result != null && result.size() > 0) {
			for (Iterator it = result.iterator(); it.hasNext();) {
				TypedFlatMap tabelaPrecoParcela = (TypedFlatMap) it.next();
				String cdParcelaPreco = tabelaPrecoParcela.getString("cdParcelaPreco");
				if (cdParcelaPreco.equals(ConstantesExpedicao.CD_ADVALOREM_1) ||
					cdParcelaPreco.equals(ConstantesExpedicao.CD_ADVALOREM_2) ||
					(idTabelaPrecoGerada == null && 
							(cdParcelaPreco.equals(ConstantesExpedicao.CD_GRIS) ||
							 cdParcelaPreco.equals(ConstantesExpedicao.CD_PEDAGIO) ||
							 cdParcelaPreco.equals(ConstantesExpedicao.CD_TDE)))) {
					it.remove();
				} 
			}
		}
		return result;
	}

	public TypedFlatMap findValoresGeneralidade(TypedFlatMap criteria) {
		Long idTabelaPrecoParcela = criteria.getLong("generalidade.idTabelaPrecoParcela");
		Long idTabelaPreco = criteria.getLong("tabelaBase.idTabelaPreco");
		TypedFlatMap result = getGeneralidadeService().findByIdTabelaPrecoParcelaIdTabelaPreco(idTabelaPrecoParcela, idTabelaPreco);
		if(result != null) {
			result.put("resetValues", criteria.get("resetValues"));
		}
		return result;
	}

	public TypedFlatMap findById(Long idGeneralidade) {
		Generalidade generalidade = null;
		TypedFlatMap result = new TypedFlatMap();
		if (idGeneralidade.longValue() < 0) {
			generalidade = findByIdInSession(idGeneralidade);
			result.put("generalidade.idTabelaPrecoParcela", generalidade.getTabelaPrecoParcela().getIdTabelaPrecoParcela());
		} else {
			generalidade = getGeneralidadeService().findById(idGeneralidade);
			Long idTabelaBase = generalidade.getTabelaPrecoParcela().getTabelaPreco().getTabelaPreco().getIdTabelaPreco();
			Long idParcelaPreco = generalidade.getTabelaPrecoParcela().getParcelaPreco().getIdParcelaPreco();
			result.put("generalidade.idTabelaPrecoParcela", getTabelaPrecoParcelaService().findByIdTabelaPrecoIdParcelaPreco(idTabelaBase, idParcelaPreco));
		}
		result.put("idGeneralidade", generalidade.getIdGeneralidade());
		result.put("parcelaPreco.dsParcelaPreco", generalidade.getTabelaPrecoParcela().getParcelaPreco().getDsParcelaPreco());
		result.put("vlGeneralidadeReajustado", generalidade.getVlGeneralidade());
		result.put("vlMinimoReajustado", generalidade.getVlMinimo());
		return result;
	}
	
	public void store(TypedFlatMap data) {
		Long idTabelaPrecoParcela = data.getLong("generalidade.idTabelaPrecoParcela");
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
			
			Generalidade generalidade = new Generalidade();
			generalidade.setIdGeneralidade(Long.valueOf(--cont));
			generalidade.setVlGeneralidade(data.getBigDecimal("vlGeneralidadeReajustado"));
			generalidade.setVlMinimo(data.getBigDecimal("vlMinimoReajustado"));
			
			generalidade.setTabelaPrecoParcela(tpp);
			tpp.setGeneralidade(generalidade);
			
			List generalidades = getGeneralidadesInSession();
			generalidades.add(tpp);
			setGeneralidadesInSession(generalidades);
		}
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			return getGeneralidadeService().findPaginatedByIdTabelaPreco(criteria);
		}
		// busca os dados da sessao
		return findPaginatedInSession(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			return getGeneralidadeService().getRowCountByIdTabelaPreco(criteria);
		}
		// busca os dados da sessao
		List taxas = getGeneralidadesInSession();
		return Integer.valueOf(taxas.size());
	}
	
	public TypedFlatMap removeGeneralidades(TypedFlatMap criteria) {
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
		getGeneralidadeService().removeByIds(ids);
	}
	
	private ResultSetPage findPaginatedInSession(TypedFlatMap criteria) {
		ResultSetPage rsp = ResultSetPage.EMPTY_RESULTSET;
		List generalidades = getGeneralidadesInSession();
		if (generalidades != null && generalidades.size() > 0) {
			FindDefinition def = FindDefinition.createFindDefinition(criteria);
			
			Comparator ordenador = new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return 0;
				}
			};
			
			rsp = MasterDetailAction.getResultSetPage(generalidades, def.getCurrentPage(), def.getPageSize(), ordenador);
			
			List newList = new ArrayList();
			List oldList = rsp.getList();
			for (int i = 0; i < oldList.size(); i++) {
				TabelaPrecoParcela tpp = (TabelaPrecoParcela) oldList.get(i);
				TypedFlatMap parcela = new TypedFlatMap();
				parcela.put("idGeneralidade", tpp.getGeneralidade().getIdGeneralidade());
				parcela.put("parcelaPreco.dsParcelaPreco", tpp.getParcelaPreco().getDsParcelaPreco());
				parcela.put("generalidade.vlGeneralidade", tpp.getGeneralidade().getVlGeneralidade());
				parcela.put("generalidade.vlMinimo", tpp.getGeneralidade().getVlMinimo());
				
				newList.add(parcela);
			}
			rsp.setList(newList);
		}
		return rsp;
	}
	
	private Generalidade findByIdInSession(Long idGeneralidade) {
		List taxas = getGeneralidadesInSession();
		for (int i = 0; i < taxas.size(); i++) {
			TabelaPrecoParcela tpp = (TabelaPrecoParcela) taxas.get(i);
			if (idGeneralidade.equals(tpp.getGeneralidade().getIdGeneralidade())) {
				return tpp.getGeneralidade();
			}
		}
		
		return null;
	}
	
	private void removeByIdsInSession(List ids) {
		for (int i = 0; i < ids.size(); i++) {
			Long id = (Long) ids.get(i);
			removeByIdInSession(id);
		}
	}
	
	private void removeByIdInSession(Long id) {
		List taxas = getGeneralidadesInSession();
		for (Iterator it = taxas.iterator(); it.hasNext();) {
			TabelaPrecoParcela tpp = (TabelaPrecoParcela) it.next();
			if (id.equals(tpp.getGeneralidade().getIdGeneralidade())) {
				it.remove();
			}
		}
	}
	
	private void verifyStore(Long idTabelaPrecoParcela) {
		List generalidades = getGeneralidadesInSession();
		TabelaPrecoParcela tpp = new TabelaPrecoParcela();
		tpp.setIdTabelaPrecoParcela(idTabelaPrecoParcela);
		
		if (generalidades.indexOf(tpp) >= 0) {
			throw new BusinessException("LMS-30045");
		}
	}
	
	private void removeTabelaPrecoParcelaInSession(Long idTabelaPrecoParcela) {
		List taxas = getGeneralidadesInSession();
		TabelaPrecoParcela tpp = new TabelaPrecoParcela();
		tpp.setIdTabelaPrecoParcela(idTabelaPrecoParcela);
		
		int index = taxas.indexOf(tpp); 
		if (index >= 0) {
			taxas.remove(index);
			setGeneralidadesInSession(taxas);
		}
	}
	
	private List getGeneralidadesInSession() {
		List generalidades = (List) SessionContext.get(ConstantesTabelaPrecos.GENERALIDADES_IN_SESSION);
		if (generalidades == null) {
			generalidades = new ArrayList();
			setGeneralidadesInSession(generalidades);
		}
		return generalidades;
	}
	
	private void setGeneralidadesInSession(List generalidades) {
		SessionContext.set(ConstantesTabelaPrecos.GENERALIDADES_IN_SESSION, generalidades);
	}
	

	/**
	 * @return Returns the generalidadeService.
	 */
	public GeneralidadeService getGeneralidadeService() {
		return generalidadeService;
	}

	/**
	 * @param generalidadeService The generalidadeService to set.
	 */
	public void setGeneralidadeService(GeneralidadeService generalidadeService) {
		this.generalidadeService = generalidadeService;
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
