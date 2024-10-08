package com.mercurio.lms.tabelaprecos.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoAnexo;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.manterTabelasPrecoAction"
 */

public class ManterTabelasPrecoAction extends CrudAction {

	ParametroGeralService parametroGeralService;
	private FilialService filialService;
	private HistoricoWorkflowService historicoWorkflowService;
	private WorkflowPendenciaService workflowPendenciaService;
	
	public void setTabelaPreco(TabelaPrecoService tabelaPrecoService) {
		this.defaultService = tabelaPrecoService;
	}

	public TabelaPrecoService getTabelaPreco() {
		return (TabelaPrecoService)this.defaultService;
	}
	
	public TypedFlatMap findTabelaPrecoAnexoById(TypedFlatMap criteria) {
		TabelaPrecoAnexo tabelaPrecoAnexo = getTabelaPreco().findTabelaPrecoAnexoById(criteria.getLong("idTabelaPrecoAnexo"));
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("idTabelaPrecoAnexo", tabelaPrecoAnexo.getIdTabelaPrecoAnexo());
		map.put("dsAnexo", tabelaPrecoAnexo.getDsAnexo());
		map.put("dcArquivo", Base64Util.encode(tabelaPrecoAnexo.getDcArquivo()));
		map.put("idTabelaPreco", tabelaPrecoAnexo.getTabelaPreco().getIdTabelaPreco());
		return map;
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginatedTabelaPrecoAnexo(TypedFlatMap criteria) {
    	criteria.put("idTabelaPreco", criteria.getLong("idTabelaPreco"));
    	return  getTabelaPreco().findPaginatedTabelaPrecoAnexo(new PaginatedQuery(criteria));
    }

    public Integer getRowCountTabelaPrecoAnexo(TypedFlatMap criteria){
    	return getTabelaPreco().getRowCountTabelaPrecoAnexo(criteria);
    }
	
    public Serializable storeTabelaPrecoAnexo(TypedFlatMap map){
    	return getTabelaPreco().storeTabelaPrecoAnexo(map);
    }
    
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsTabelaPrecoAnexo(List ids){
    	getTabelaPreco().removeByIdsTabelaPrecoAnexo(ids);
    }
    
	public void removeById(java.lang.Long id) {
		getTabelaPreco().removeById(id);
    }

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getTabelaPreco().removeByIds(ids);
    }
	
	public TypedFlatMap atualizarTabelaCustoTnt(TypedFlatMap parametros) {
		TypedFlatMap result = new TypedFlatMap();
		
		String tpServico = parametros.getString("tpServico");
		String retornoAtualiza = getTabelaPreco().atualizaTabelaCustos(tpServico);
		result.put("retornoAtualiza", retornoAtualiza);
		return result;
	}
	
	public TypedFlatMap findAtualizacaoAutomatica() {
		TypedFlatMap result = new TypedFlatMap();
		String atualizacaoAutomatica = (String)parametroGeralService.findConteudoByNomeParametro("ATUALIZACAO_AUTOMATICA_TABELA_PRECO", false);
		result.put("atualizacaoAutomatica", "S".equals(atualizacaoAutomatica));
		return result;
	}
	
    public Serializable findById(java.lang.Long id) {
    	TypedFlatMap map = getTabelaPreco().findByIdMap(id);
    	ParametroGeral pg = parametroGeralService.findByNomeParametro("USUARIO_LIBERADOR_TABELA_PRECO", false);
    	String liberador =  pg != null && !"".equals(pg.getDsParametro()) ? pg.getDsConteudo() : null;
    	Boolean temPendenciaWorkflow = historicoWorkflowService.validateWorkflowPendenciaAprovacao(
    			id, TabelaHistoricoWorkflow.TABELA_PRECO, CampoHistoricoWorkflow.TBPR);
    	map.put("temPendenciaWorkflow", temPendenciaWorkflow);
    	map.put("blLiberador", (SessionUtils.getUsuarioLogado() != null && SessionUtils.getUsuarioLogado().getNrMatricula() != null && SessionUtils.getUsuarioLogado().getNrMatricula().equals(liberador)) ? "1" : "");// string vazia em js == false
    	map.put("blPossueParcela", CollectionUtils.isNotEmpty(getTabelaPreco().findParcelasByTabelaPreco(id)) ? Boolean.TRUE : Boolean.FALSE);
    	return (Serializable) map;
    }
    
    public List findLookupFilial(TypedFlatMap tfm) {
		return getFilialService().findLookupFilial(tfm);
	}

	public Serializable store(TypedFlatMap parameters) {
		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.setBlEfetivada(parameters.getBoolean("blEfetivada"));
		tabelaPreco.setBlImprimeTabela(parameters.getBoolean("blImprimeTabela"));
		tabelaPreco.setDsDescricao(parameters.getString("dsDescricao"));
		tabelaPreco.setDtVigenciaInicial(parameters.getYearMonthDay("dtVigenciaInicial"));
		tabelaPreco.setDtVigenciaFinal(parameters.getYearMonthDay("dtVigenciaFinal"));
		tabelaPreco.setIdTabelaPreco(parameters.getLong("idTabelaPreco"));
		tabelaPreco.setTpCategoria(new DomainValue(parameters.getString("tpCategoria")));
		tabelaPreco.setTpServico(new DomainValue(parameters.getString("tpServico")));
		
		TipoTabelaPreco tipoTabelaPreco = new TipoTabelaPreco();
		tipoTabelaPreco.setIdTipoTabelaPreco(parameters.getLong("tipoTabelaPreco.idTipoTabelaPreco"));
		tipoTabelaPreco.setTpTipoTabelaPreco(new DomainValue(parameters.getString("tipoTabelaPreco.tpTipoTabelaPreco")));

		Servico servico = new Servico();
		servico.setIdServico(parameters.getLong("tipoTabelaPreco.servico.idServico"));
		servico.setDsServico(parameters.getVarcharI18n("tipoTabelaPreco.servico.dsServico"));
		tipoTabelaPreco.setServico(servico);

		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(parameters.getLong("tipoTabelaPreco.empresaByIdEmpresaCadastrada.idEmpresa"));
		tipoTabelaPreco.setEmpresaByIdEmpresaCadastrada(empresa);

		tabelaPreco.setTipoTabelaPreco(tipoTabelaPreco);
		tabelaPreco.setTpCalculoPedagio(new DomainValue(parameters.getString("tpCalculoPedagio")));		

		SubtipoTabelaPreco subtipoTabelaPreco = new SubtipoTabelaPreco();
		subtipoTabelaPreco.setIdSubtipoTabelaPreco(parameters.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"));
		subtipoTabelaPreco.setTpSubtipoTabelaPreco(parameters.getString("subtipoTabelaPreco.tpSubtipoTabelaPreco"));
		tabelaPreco.setSubtipoTabelaPreco(subtipoTabelaPreco);

		Moeda moeda = new Moeda();
		moeda.setIdMoeda(parameters.getLong("moeda.idMoeda"));
		tabelaPreco.setMoeda(moeda);
		
		Long idPendencia = parameters.getLong("idPendencia");
		if (LongUtils.hasValue(idPendencia)) {
			Pendencia pendencia = new Pendencia();
			pendencia.setIdPendencia(idPendencia);
			tabelaPreco.setPendencia(pendencia);
		}
		
		Long idPendenciaDesefetivacao = parameters.getLong("idPendenciaDesefetivacao");
		if (LongUtils.hasValue(idPendenciaDesefetivacao)) {
			Pendencia pendenciaDesefetivacao = new Pendencia();
			pendenciaDesefetivacao.setIdPendencia(idPendenciaDesefetivacao);
			tabelaPreco.setPendenciaDesefetivacao(pendenciaDesefetivacao);
		}
		
		Long idPendenciaEfetivacao = parameters.getLong("idPendenciaEfetivacao");
		if (LongUtils.hasValue(idPendenciaEfetivacao)) {
			Pendencia pendenciaEfetivacao = new Pendencia();
			pendenciaEfetivacao.setIdPendencia(idPendenciaEfetivacao);
			tabelaPreco.setPendenciaDesefetivacao(pendenciaEfetivacao);
		}

		tabelaPreco.setPcReajuste(parameters.getBigDecimal("pcReajuste"));
		tabelaPreco.setPcDescontoFreteMinimo(parameters.getBigDecimal("pcDescontoFreteMinimo"));
		tabelaPreco.setPsMinimo(parameters.getBigDecimal("psMinimo"));
		Long idTabelaPrecoOrigem = parameters.getLong("tabelaPreco.idTabelaPreco");
		if(idTabelaPrecoOrigem != null) {
			TabelaPreco tp = new TabelaPreco();
			tp.setIdTabelaPreco(idTabelaPrecoOrigem);
			tabelaPreco.setTabelaPreco(tp);
		}
		tabelaPreco.setTpCalculoFretePeso(parameters.getDomainValue("tpCalculoFretePeso"));
		tabelaPreco.setObTabelaPreco(parameters.getString("obTabelaPreco"));
		
		Long idTabelaPrecoCustoTnt = parameters.getLong("tabelaPrecoCustoTnt.idTabelaPreco");
		if (LongUtils.hasValue(idTabelaPrecoCustoTnt)) {
			TabelaPreco tpCustoTnt = new TabelaPreco();
			tpCustoTnt.setIdTabelaPreco(idTabelaPrecoCustoTnt);
			tabelaPreco.setTabelaPrecoCustoTnt(tpCustoTnt);
		}

		tabelaPreco.setBlIcmsDestacado(parameters.getBoolean("blIcmsDestacado"));

		getTabelaPreco().storeTabelaPreco(tabelaPreco, parameters.getBoolean("blDesativaTabelaAntiga"), parameters.getLong("idTabelaPrecoVigente"), parameters.getString("informacoesTabelaPreco"));
		
		String msgAtualizacaoAutomatica = null;
		if(CollectionUtils.isNotEmpty(getTabelaPreco().findParcelasByTabelaPreco(tabelaPreco.getIdTabelaPreco()))
			&& getTabelaPreco().validaAtualizacaoAutomatica(tabelaPreco)){
			msgAtualizacaoAutomatica = getTabelaPreco().atualizaTabelaCustos(tabelaPreco.getTpServico().getValue());
		}

			
		HashMap map = new HashMap(3);
		map.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());
		map.put("dtGeracao", tabelaPreco.getDtGeracao());
		map.put("msgAtualizacaoAutomatica", msgAtualizacaoAutomatica);
		map.put("temPendenciaWorkflow", historicoWorkflowService.validateWorkflowPendenciaAprovacao(tabelaPreco.getIdTabelaPreco(), TabelaHistoricoWorkflow.TABELA_PRECO, CampoHistoricoWorkflow.TBPR));
		map.put("blEfetivada", parameters.getBoolean("blEfetivada"));
		map.put("blPossuiParcela", CollectionUtils.isNotEmpty(getTabelaPreco().findParcelasByTabelaPreco(tabelaPreco.getIdTabelaPreco())) ? Boolean.TRUE : Boolean.FALSE);
		
		return map;
	}

	public TypedFlatMap checkFilial(TypedFlatMap parameters) {
		String tpTipoTabelaPreco = parameters.getString("tpTipoTabelaPreco");
		if (!"D".equals(tpTipoTabelaPreco)) {
			return getBasicData();
		}
		return parameters;
	}
	
	/**
	 * Busca algums dos dados do usuario logado, que est� na sess�o.
	 */
	public TypedFlatMap getBasicData() {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("filial.idFilial", SessionUtils.getFilialSessao().getIdFilial());
		tfm.put("filial.sgFilial", SessionUtils.getFilialSessao().getSgFilial());
		tfm.put("filial.pessoa.nmFantasia", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
		return tfm;
	}
	
	public void findPossuiTarifaRota(TypedFlatMap parameters) {
		getTabelaPreco().findPossuiTarifaRota(parameters.getLong("idTabelaPreco"));
	}
	
	public TypedFlatMap gerarPendenciaWorkflow(TypedFlatMap parameters) {
		Long idPendencia = getTabelaPreco().generatePendenciaWorkflow(parameters);
		Boolean temPendenciaWorkflow = historicoWorkflowService.validateWorkflowPendenciaAprovacao(
				parameters.getLong("idTabelaPreco"), TabelaHistoricoWorkflow.TABELA_PRECO, CampoHistoricoWorkflow.TBPR);
		parameters.put("temPendenciaWorkflow", temPendenciaWorkflow);
		parameters.put("idPendencia", idPendencia);
		return parameters;
	}
	
	public TypedFlatMap gerarPendenciaDesefetivacao(TypedFlatMap parameters) {
		TabelaPreco tabelaPreco = getTabelaPreco().generateWorkflowDesefetivacao(parameters);
		parameters.put("idPendenciaDesefetivacao", tabelaPreco.getPendenciaDesefetivacao().getIdPendencia());
		return parameters;
	}
	
	public TypedFlatMap gerarPendenciaEfetivacao(TypedFlatMap parameters) {
		TabelaPreco tabelaPreco = getTabelaPreco().executeAtualizarVigenciasEGerarWK(parameters);
		parameters.put("idPendenciaEfetivacao", tabelaPreco.getPendenciaEfetivacao().getIdPendencia());
		parameters.put("idPendencia", tabelaPreco.getPendencia().getIdPendencia());
		parameters.put("dtVigenciaInicial", tabelaPreco.getDtVigenciaInicial());
		parameters.put("dtVigenciaFinal", tabelaPreco.getDtVigenciaFinal());
		return parameters;
	}
	
	public void cancelWorkflow(TypedFlatMap parameters) {
		getTabelaPreco().cancelWorkflow(parameters.getLong("idTabelaPreco"));
	}
	
	public TypedFlatMap reiniciarAlteracao(TypedFlatMap parameters) {
		getTabelaPreco().executeReiniciarAlteracao(parameters.getLong("idTabelaPreco"));
		parameters.put("idPendencia", null);
		parameters.put("temPendenciaAprovada", false);
		parameters.put("temPendenciaWorkflow", false);
		parameters.put("possuiParcela", true);
		return parameters;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public HistoricoWorkflowService getHistoricoWorkflowService() {
		return historicoWorkflowService;
	}

	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

}
