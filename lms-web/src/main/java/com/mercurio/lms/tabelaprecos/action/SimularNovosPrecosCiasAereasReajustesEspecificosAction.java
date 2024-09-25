/**
 * 
 */
package com.mercurio.lms.tabelaprecos.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.LongUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.simularNovosPrecosCiasAereasReajustesEspecificosAction"
 */
public class SimularNovosPrecosCiasAereasReajustesEspecificosAction extends
		CrudAction {

	private static int cont = 0;
	
	private AeroportoService aeroportoService;
	
	/*
	 * Metodos publicos
	 */
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = ResultSetPage.EMPTY_RESULTSET;
		
		List reajustes = getReajustesEspecificosInSession();
		
		if (reajustes.size() > 0) {
			FindDefinition def = FindDefinition.createFindDefinition(criteria);
			
			Comparator ordenador = new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return 0;
				}
			};
			
			rsp = MasterDetailAction.getResultSetPage(reajustes, 
					def.getCurrentPage(), def.getPageSize(), ordenador);
		}
		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return Integer.valueOf(getReajustesEspecificosInSession().size());
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (int i = 0; i < ids.size(); i++) {
			Long idReajuste = (Long) ids.get(i);
			removeByIdFromSession(idReajuste);
		}
	}
	
	public TypedFlatMap findById(Long id) {
		List reajustes = getReajustesEspecificosInSession();
		for (int i = 0; i < reajustes.size(); i++) {
			TypedFlatMap reajuste = (TypedFlatMap) reajustes.get(i);
			
			if (id.equals(reajuste.get("idReajuste"))) {
				return reajuste;
			}
		}
		return null;
	}
	
	public Serializable storeInSession(TypedFlatMap data) {
		boolean insert = false;
		TypedFlatMap reajuste = null;
		List reajustes = getReajustesEspecificosInSession();
		
		Long idAeroportoOrigem = data.getLong("aeroportoByIdAeroportoOrigem.idAeroporto");
		Long idAeroportoDestino = data.getLong("aeroportoByIdAeroportoDestino.idAeroporto");
		
		Long idReajuste = data.getLong("reajuste.idReajuste");
		if (idReajuste == null) {
			idReajuste = Long.valueOf(--cont);
			reajuste = new TypedFlatMap();
			insert = true;
		} else {
			reajuste = findById(idReajuste);
		}
		
		// Regra 4.7: verificar se pelo menos um dos campos de aeroporto esta
		// preenchido, caso contrario exibir mensagem LMS-30046
		verifyParametersInsert(idAeroportoOrigem, idAeroportoDestino);
		
		// Regra #.#: verificar se a rota já foi cadastrada, caso positivo
		// exibir a mensagem LMS-30047
		verifyRotaInSession(idAeroportoOrigem, idAeroportoDestino, idReajuste);
		
		
		String sgAeroportoOrigem = data.getString("aeroportoOrigem.sgAeroporto");
		String nmPessoaOrigem = data.getString("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa");
		String siglaDescricaoOrigem = "";
		if (idAeroportoOrigem != null) {
			siglaDescricaoOrigem = sgAeroportoOrigem + " - " + nmPessoaOrigem;
		}
		
		String sgAeroportoDestino = data.getString("aeroportoDestino.sgAeroporto");
		String nmPessoaDestino = data.getString("aeroportoByIdAeroportoDestino.pessoa.nmPessoa");
		String siglaDescricaoDestino = "";
		if (idAeroportoDestino != null) {
			siglaDescricaoDestino = sgAeroportoDestino + " - " + nmPessoaDestino;
		}
		
		reajuste.put("idReajuste", idReajuste);
		reajuste.put("reajuste.idReajuste", idReajuste);
		reajuste.put("pcDesconto", data.getBigDecimal("pcDesconto"));
		
		reajuste.put("aeroportoByIdAeroportoOrigem.idAeroporto", idAeroportoOrigem);
		reajuste.put("aeroportoByIdAeroportoOrigem.sgAeroporto", sgAeroportoOrigem);
		reajuste.put("aeroportoOrigem.sgAeroporto", sgAeroportoOrigem);
		reajuste.put("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", nmPessoaOrigem);
		reajuste.put("aeroportoByIdAeroportoOrigem.siglaDescricao", siglaDescricaoOrigem);
		
		reajuste.put("aeroportoByIdAeroportoDestino.idAeroporto", idAeroportoDestino);
		reajuste.put("aeroportoByIdAeroportoDestino.sgAeroporto", sgAeroportoDestino);
		reajuste.put("aeroportoDestino.sgAeroporto", sgAeroportoDestino);
		reajuste.put("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", nmPessoaDestino);
		reajuste.put("aeroportoByIdAeroportoDestino.siglaDescricao", siglaDescricaoDestino);
		
		if (insert) {
			reajustes.add(reajuste);
		}
		
		setReajustesEspecificosInSession(reajustes);
		
		return idReajuste;
	}
	
	public List findLookupAeroporto(Map criteria) {
		return aeroportoService.findLookupAeroporto(criteria);
	}
	
	/*
	 * Metodos privados
	 */
	
	private List getReajustesEspecificosInSession() {
		List reajustes = (List) SessionContext.get(ConstantesTabelaPrecos.REAJUSTES_ESPECIFICOS_IN_SESSION);
		if (reajustes == null) {
			reajustes = new ArrayList();
			setReajustesEspecificosInSession(reajustes);
		}
		return reajustes;
	}
	
	private void setReajustesEspecificosInSession(List reajustesEspecificos) {
		SessionContext.set(ConstantesTabelaPrecos.REAJUSTES_ESPECIFICOS_IN_SESSION, reajustesEspecificos);
	}
	
	private void verifyParametersInsert(Long idAeroportoOrigem, Long idAeroportoDestino) {
		if (idAeroportoOrigem == null && idAeroportoDestino == null) {
			throw new BusinessException("LMS-30046");
		}
	}
	
	private void verifyRotaInSession(Long idAeroportoOrigem, Long idAeroportoDestino, Long idReajuste) {
		List reajustes = getReajustesEspecificosInSession();
		for (int i = 0; i < reajustes.size(); i++) {
			TypedFlatMap reajuste = (TypedFlatMap) reajustes.get(i);
			Long id = reajuste.getLong("idReajuste");
			if (id.equals(idReajuste)) {
				continue;
			}
			Long idOrigem = reajuste.getLong("aeroportoByIdAeroportoOrigem.idAeroporto");
			Long idDestino = reajuste.getLong("aeroportoByIdAeroportoDestino.idAeroporto");
			
			if (idOrigem == null) {
				idOrigem = LongUtils.ZERO;
			}
			if (idDestino == null) {
				idDestino = LongUtils.ZERO;
			}
			if (idAeroportoOrigem == null) {
				idAeroportoOrigem = LongUtils.ZERO;
			}
			if (idAeroportoDestino == null) {
				idAeroportoDestino = LongUtils.ZERO;
			}
			if (CompareUtils.eq(idOrigem, idAeroportoOrigem) && CompareUtils.eq(idDestino, idAeroportoDestino)) {
				throw new BusinessException("LMS-30047");
			}	
		}
	}
	
	
	private void removeByIdFromSession(Long idReajuste) {
		List reajustes = getReajustesEspecificosInSession();
		for (Iterator it = reajustes.iterator(); it.hasNext();) {
			TypedFlatMap reajuste = (TypedFlatMap) it.next();
			if (idReajuste.equals(reajuste.getLong("idReajuste"))) {
				it.remove();
				break;
			}
		}
	}
	
	/*
	 * Getters e setters
	 */
	
	/**
	 * @return Returns the tabelaPrecoService.
	 */
	public TabelaPrecoService getTabelaPrecoService() {
		return (TabelaPrecoService) super.defaultService;
	}

	/**
	 * @param tabelaPrecoService The tabelaPrecoService to set.
	 */
	public void setService(TabelaPrecoService serviceService) {
		super.defaultService = serviceService;
	}

	/**
	 * @param aeroportoService The aeroportoService to set.
	 */
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
}
