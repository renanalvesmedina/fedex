package com.mercurio.lms.tabelaprecos.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.tabelaprecos.model.service.SubtipoTabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TipoTabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.simularNovosPrecosCiasAereasAction"
 */

public class SimularNovosPrecosCiasAereasAction extends CrudAction {
	
	private TipoTabelaPrecoService tipoTabelaPrecoService;
	private SubtipoTabelaPrecoService subtipoTabelaPrecoService;
	private EmpresaService empresaService;
	private VigenciaService vigenciaService;
	private UsuarioService usuarioService;
	private AeroportoService aeroportoService;
	private ProdutoEspecificoService produtoEspecificoService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public List findTipoTabelaPreco(TypedFlatMap criteria) {
		List tipos = tipoTabelaPrecoService.findByTpSituacaoTpTipoTabelaPreco("A", "C");
		if (tipos != null && tipos.size() > 0) {
			for (Iterator it = tipos.iterator(); it.hasNext();) {
				TypedFlatMap tipo = (TypedFlatMap) it.next();

				String tpTipoTabelaPreco = tipo.getString("tpTipoTabelaPreco.value");
				String dsIdentificacao = tipo.getString("dsIdentificacao");
				String dsDescricao = tpTipoTabelaPreco + " - " + dsIdentificacao;
				
				tipo.put("dsDescricao", dsDescricao);
			}
		}
		return tipos;
	}
	
	public List findSubtipoTabelaPreco(TypedFlatMap criteria) {
		return subtipoTabelaPrecoService.find(criteria);
	}
	
	public List findCiasAereas(TypedFlatMap criteria) {
		return empresaService.findCiaAerea(criteria);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		criteria.put("tipoTabelaPreco.tpTipoTabelaPreco", "C");
		ResultSetPage rsp = getTabelaPrecoService().findPaginatedSimulacao(criteria);
		List result = rsp.getList();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				TypedFlatMap tabela = (TypedFlatMap) result.get(i);

				String tpTipoTabelaPreco = tabela.getString("tipoTabelaPreco.tpTipoTabelaPreco.value");
				Integer nrVersao = tabela.getInteger("tipoTabelaPreco.nrVersao");
				String tpSubtipoTabelaPreco = tabela.getString("subtipoTabelaPreco.tpSubtipoTabelaPreco");

				String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);

				tabela.put("tabelaPrecoString", tabelaPrecoString);
			}
		}
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		criteria.put("tipoTabelaPreco.tpTipoTabelaPreco", "C");
		return getTabelaPrecoService().getRowCountSimulacao(criteria);
	}
	
	public List findLookupTabelaPreco(Map criteria) {
		List result = getTabelaPrecoService().findLookup(criteria);
		if (result != null && result.size() > 0) {
			for (Iterator it = result.iterator(); it.hasNext();) {
				TabelaPreco tabelaPreco = (TabelaPreco) it.next();
				TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
				DomainValue tpTipoTabelaPreco = tipoTabelaPreco.getTpTipoTabelaPreco();
				
				if (!"C".equals(tpTipoTabelaPreco.getValue())) {
					it.remove();
				}
			}
		}
		
		return result;
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getTabelaPrecoService().removeByIdsProposta(ids, false);
	}
	
	public void removeById(java.lang.Long id) {
		getTabelaPrecoService().removeById(id);
	}
	
	public void validaDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		vigenciaService.validateInicioVigencia(dtVigenciaInicial, "LMS-30040");
	}
	
	public List findLookupUsuarioFuncionario(TypedFlatMap parameters) {

		String nrMatricula = parameters.getString("nrMatricula");

		return usuarioService.findLookupUsuarioFuncionario(null,
				nrMatricula, null, null, null, null, true);
	}
	
	public TypedFlatMap findDadosSessao() {
		TypedFlatMap result = new TypedFlatMap();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		result.put("funcionario.idUsuario", usuario.getIdUsuario());
		result.put("funcionario.nrMatricula", usuario.getNrMatricula());
		result.put("funcionario.nmFuncionario", usuario.getNmUsuario());
		return result;
	}

	public List findLookupAeroporto(Map criteria) {
		return aeroportoService.findLookupAeroporto(criteria);
	}

	/**
	 * Efetiva a nova tabela.
	 * @param map
	 */
	public void efetivarTabela(TypedFlatMap map) {
		YearMonthDay dtVigenciaInicial = map.getYearMonthDay("dtVigenciaInicial");
		Long idTabelaPreco = map.getLong("idTabelaPreco");
		Long idSubtipoTabelaPreco = map.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco");
		DomainValue tpTipoTabelaPreco = new DomainValue(map.getString("tipoTabelaPreco.tpTipoTabelaPreco"));

		getTabelaPrecoService().executeEfetivarTabela(idTabelaPreco, idSubtipoTabelaPreco, tpTipoTabelaPreco, Boolean.TRUE, dtVigenciaInicial);	
	}

	public List findLookupProdutoEspecifico(TypedFlatMap criteria) {
		return produtoEspecificoService.findAllAtivo();
	}

	public TypedFlatMap gerarSimulacaoCiaAerea(TypedFlatMap data) {
		Long idTabelaPreco = data.getLong("tabelaPreco.idTabelaPreco");
		Long idTabelaBase = data.getLong("tabelaBase.idTabelaPreco");
		BigDecimal pcReajuste = data.getBigDecimal("pcReajuste");
		String tpTarifaReajuste = data.getString("tpTarifaReajuste");
		YearMonthDay dtVigenciaInicial = data.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal = data.getYearMonthDay("dtVigenciaFinal");
		Long idSubtipoTabelaBase = data.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco");
		Long idUsuario = data.getLong("funcionario.idUsuario");
		List produtosEspecificos = data.getList("produtosEspecificos");
		List aeroportos = data.getList("aeroportos");
		List reajustes = getReajustesEspecificosInSession();
		
		TabelaPreco tabelaPreco = getTabelaPrecoService()
				.generateSimulacaoCiaAerea(idTabelaBase, pcReajuste, tpTarifaReajuste,
						reajustes, aeroportos, produtosEspecificos);
		
		if (idTabelaPreco != null) {
			TabelaPreco persistida = (TabelaPreco) super.findById(idTabelaPreco);
			
			persistida.setDtVigenciaInicial(dtVigenciaInicial);
			persistida.setPcReajuste(pcReajuste);
			persistida.setDtGeracao(JTDateTimeUtils.getDataAtual());
			persistida.setTpTarifaReajuste(new DomainValue(tpTarifaReajuste));
			
			prepareTabelaPrecoParcelasEdit(persistida, tabelaPreco.getTabelaPrecoParcelas());
			
			getTabelaPrecoService().storeTabelaPrecoCiaAerea(persistida, true);
			
			tabelaPreco = persistida;
		} else {
			generateTabelaPreco(tabelaPreco, idTabelaBase, idSubtipoTabelaBase,
					idUsuario, pcReajuste, dtVigenciaInicial, dtVigenciaFinal, tpTarifaReajuste);
			
			prepareTabelaPrecoParcelas(tabelaPreco);
			
			getTabelaPrecoService().storeTabelaPrecoCiaAerea(tabelaPreco, true);
		}
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("resultado", configuracoesFacade.getMensagem("LMS-30039"));
		result.put("tabelaPreco.idTabelaPreco", tabelaPreco.getIdTabelaPreco());
		result.put("tabelaPreco.dtGeracao", tabelaPreco.getDtGeracao());
		return result;
	}
	
	public void reconfiguraSessao() {
		SessionContext.remove(ConstantesTabelaPrecos.REAJUSTES_ESPECIFICOS_IN_SESSION);
		SessionContext.remove(ConstantesTabelaPrecos.PRODUTOS_ESPECIFICOS_IN_SESSION);
		SessionContext.remove(ConstantesTabelaPrecos.AEROPORTOS_IN_SESSION);
	}
	
	public TypedFlatMap findClienteByIdTabelaPreco(Long idTabelaPreco) {
		return getTabelaPrecoService().findClienteByIdTabelaPreco(idTabelaPreco);
	}
	
	public List findSubtiposTabelaPrecos() {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("tpTipoTabelaPreco", "C");
		return subtipoTabelaPrecoService.findByTpTipoTabelaPreco(criteria);
	}
	
	public TypedFlatMap findById(Long id) {
		TypedFlatMap result = getTabelaPrecoService().findByIdMap(id);

		Long idTabelaBase = result.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaBase != null) {
			TypedFlatMap tabelaBase = getTabelaPrecoService().findByIdMap(idTabelaBase);
			if (tabelaBase != null) {
				result.put("tabelaBase.tabelaPrecoString", tabelaBase.get("tabelaPrecoString"));
				result.put("tabelaBase.dsDescricao", tabelaBase.get("dsDescricao"));
				result.put("tabelaBase.idTabelaPreco", tabelaBase.get("idTabelaPreco"));
			}
		}
		
		result.put("empresaByIdEmpresaCadastrada.idEmpresa", result.remove("tipoTabelaPreco.empresaByIdEmpresaCadastrada.idEmpresa"));
		result.put("empresaByIdEmpresaCadastrada.pessoa.nmPessoa", result.remove("tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa"));
		result.put("tabelaPreco.idTabelaPreco", result.getLong("idTabelaPreco"));
		result.put("tipoTabelaPreco.idTabelaPreco", result.getLong("idTabelaPreco"));
		result.put("funcionario.idUsuario", result.remove("usuario.idUsuario"));
		result.put("funcionario.nmFuncionario", result.remove("usuario.nmUsuario"));
		result.put("funcionario.nrMatricula", result.remove("usuario.nrMatricula"));
		result.put("cliente.pessoa.nmPessoa", result.remove("tipoTabelaPreco.cliente.pessoa.nmPessoa"));
		result.put("cliente.pessoa.nrIdentificacao", result.remove("tipoTabelaPreco.cliente.pessoa.nrIdentificacao"));

		return result;
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
	
	private void generateTabelaPreco(TabelaPreco tabelaPreco,
			Long idTabelaBase, Long idSubtipoTabelaBase, Long idUsuario,
			BigDecimal pcReajuste, YearMonthDay dtVigenciaInicial,
			YearMonthDay dtVigenciaFinal, String tpTarifaReajuste) {
		
		TabelaPreco tabelaBase = (TabelaPreco) getTabelaPrecoService().findByIdTabelaPreco(idTabelaBase);
		TipoTabelaPreco tipoTabelaBase = tabelaBase.getTipoTabelaPreco();
		
		TipoTabelaPreco tipoTabelaPreco = storeTipoTabelaPreco(tipoTabelaBase);
		
		if (idSubtipoTabelaBase != null) {
			SubtipoTabelaPreco subtipoTabelaPreco = new SubtipoTabelaPreco();
			subtipoTabelaPreco.setIdSubtipoTabelaPreco(idSubtipoTabelaBase);
			tabelaPreco.setSubtipoTabelaPreco(subtipoTabelaPreco);
		}
		
		if (idUsuario != null) {
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(idUsuario);
			tabelaPreco.setUsuario(usuario);
		}
		
		tabelaPreco.setDtGeracao(JTDateTimeUtils.getDataAtual());
		tabelaPreco.setBlEfetivada(Boolean.FALSE);
		tabelaPreco.setTipoTabelaPreco(tipoTabelaPreco);
		tabelaPreco.setMoeda(tabelaBase.getMoeda());
		tabelaPreco.setDtVigenciaInicial(dtVigenciaInicial);
		tabelaPreco.setDtVigenciaFinal(dtVigenciaFinal);
		tabelaPreco.setPcReajuste(pcReajuste);
		tabelaPreco.setPsMinimo(tabelaBase.getPsMinimo());
		tabelaPreco.setTabelaPreco(tabelaBase);
		tabelaPreco.setTpCalculoFretePeso(tabelaBase.getTpCalculoFretePeso());
		tabelaPreco.setDsDescricao(tabelaBase.getDsDescricao());
		tabelaPreco.setTpTarifaReajuste(new DomainValue(tpTarifaReajuste));
	}
	
	private TipoTabelaPreco storeTipoTabelaPreco(TipoTabelaPreco tipoTabelaBase) {
		Long idServico = null;
		Long idCliente = null;
		if (tipoTabelaBase.getServico() != null) {
			idServico = tipoTabelaBase.getServico().getIdServico();
		}
		if (tipoTabelaBase.getCliente() != null) {
			idCliente = tipoTabelaBase.getCliente().getIdCliente();
		}
		TipoTabelaPreco found = tipoTabelaPrecoService.findByTipoTabelaBase(
				tipoTabelaBase.getTpTipoTabelaPreco().getValue(), 
				null,
				tipoTabelaBase.getEmpresaByIdEmpresaCadastrada().getIdEmpresa(), 
				idServico,
				idCliente);

		if(found != null) {
			return found;
		}

		TipoTabelaPreco tipoTabelaPreco = new TipoTabelaPreco();

		tipoTabelaPreco.setDsIdentificacao(tipoTabelaBase.getDsIdentificacao());
		tipoTabelaPreco.setTpTipoTabelaPreco(tipoTabelaBase.getTpTipoTabelaPreco());
		tipoTabelaPreco.setTpSituacao(new DomainValue("A"));
		tipoTabelaPreco.setEmpresaByIdEmpresaCadastrada(tipoTabelaBase.getEmpresaByIdEmpresaCadastrada());
		tipoTabelaPreco.setEmpresaByIdEmpresaLogada(tipoTabelaBase.getEmpresaByIdEmpresaLogada());
		tipoTabelaPreco.setCliente(tipoTabelaBase.getCliente());
		tipoTabelaPreco.setServico(tipoTabelaBase.getServico());

		tipoTabelaPrecoService.store(tipoTabelaPreco);
		return tipoTabelaPreco;
	}
	
	private void prepareTabelaPrecoParcelas(TabelaPreco tabelaPreco) {
		List tabelaPrecoParcelas = tabelaPreco.getTabelaPrecoParcelas();
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			for (int i = 0; i < tabelaPrecoParcelas.size(); i++) {
				TabelaPrecoParcela tpp = (TabelaPrecoParcela) tabelaPrecoParcelas.get(i);
				tpp.setIdTabelaPrecoParcela(null);
				tpp.setTabelaPreco(tabelaPreco);
			}
		}
	}

	private void prepareTabelaPrecoParcelasEdit(TabelaPreco tabelaPreco, List tabelaPrecoParcelas) {
		if (tabelaPrecoParcelas != null && tabelaPrecoParcelas.size() > 0) {
			List tpps = new ArrayList();
			for (int i = 0; i < tabelaPrecoParcelas.size(); i++) {
				TabelaPrecoParcela tpp = (TabelaPrecoParcela) tabelaPrecoParcelas.get(i);
				tpp.setIdTabelaPrecoParcela(null);
				tpp.setTabelaPreco(tabelaPreco);
				tpps.add(tpp);
			}
			if (tpps.size() > 0) {
				tabelaPreco.setTabelaPrecoParcelas(tpps);
			}
		}
	}
	
	
	/*
	 * Getters e setters
	 */

	/**
	 * @param subtipoTabelaPrecoService The subtipoTabelaPrecoService to set.
	 */
	public void setSubtipoTabelaPrecoService(
			SubtipoTabelaPrecoService subtipoTabelaPrecoService) {
		this.subtipoTabelaPrecoService = subtipoTabelaPrecoService;
	}

	/**
	 * @param tipoTabelaPrecoService The tipoTabelaPrecoService to set.
	 */
	public void setTipoTabelaPrecoService(
			TipoTabelaPrecoService tipoTabelaPrecoService) {
		this.tipoTabelaPrecoService = tipoTabelaPrecoService;
	}

	/**
	 * @param empresaService The empresaService to set.
	 */
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

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
	 * @param vigenciaService The vigenciaService to set.
	 */
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * @param usuarioService The usuarioService to set.
	 */
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	/**
	 * @param aeroportoService The aeroportoService to set.
	 */
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	/**
	 * @param produtoEspecificoService The produtoEspecificoService to set.
	 */
	public void setProdutoEspecificoService(
			ProdutoEspecificoService produtoEspecificoService) {
		this.produtoEspecificoService = produtoEspecificoService;
	}

	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}


}
