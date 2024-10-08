package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoExcluirService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDescargaService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.swt.excluirPreCTRCAction"
 */
@Assynchronous(name = "excluirPreCTRCAction")
public class ExcluirPreCTRCAction extends CrudAction {
	private ConhecimentoService conhecimentoService;
	private ConhecimentoExcluirService conhecimentoExcluirService;
	private ClienteService clienteService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;

	public Serializable removePreCTRC(TypedFlatMap criteria){
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		conhecimentoExcluirService.removePreCTRC(idDoctoServico, SessionUtils.getFilialSessao().getIdFilial());
		return null;
	}

	public List<Map<String, Object>> findDadosRemetente(Map<String, Object> criteria) {
		List<Map<String, Object>> result = findCliente((Long) criteria.get("idCliente"), (String) criteria.get("nrIdentificacao"));
		if(!result.isEmpty()) {
			Map<String, Object> cliente = (Map<String, Object>) result.get(0);
			Map<String, Object> pessoa = (Map<String, Object>) cliente.remove("pessoa");
			cliente.put("nrIdentificacao", pessoa.get("nrIdentificacao"));
			cliente.put("nmPessoa", pessoa.get("nmPessoa"));
		}
		return result;
	}
	
	private List<Map<String, Object>> findCliente(Long idCliente, String nrIdentificacao) {
		List<Map<String, Object>> result = null;
		if(idCliente != null){
			result = clienteService.findLookupClienteEndereco(idCliente);
		} else {
			result = clienteService.findClienteByNrIdentificacao(nrIdentificacao);
		}
		if(!result.isEmpty()) {
			Map<String, Object> cliente = (Map<String, Object>)result.get(0);
			Map<String, Object> pessoa = (Map<String, Object>) cliente.get("pessoa");
			pessoa.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
		}
		return result;
	}
	
	public List findLookupMonitoramentoDescarga(Map parameters) {
		String nrPlaca = (String) parameters.get("nrPlaca");
		String nrFrota = (String) parameters.get("nrFrota");
		List<Map> result = new LinkedList<Map>();
		List<Map> monitoramentos = monitoramentoDescargaService.findByDescargasComPreCtrc(SessionUtils.getFilialSessao().getIdFilial(), nrPlaca, nrFrota);
		if(monitoramentos != null && monitoramentos.size() > 0) {
			result.add(monitoramentos.get(0));
			return result;
		}
		return null;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return conhecimentoExcluirService.findPaginated(criteria);
	}

	public Map findById(Long id) {
		return conhecimentoExcluirService.findById(id);
	}

	public List findConhecimento(TypedFlatMap criteria) {
		Long nrConhecimento = criteria.getLong("conhecimento.nrConhecimento");
		Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
		List l = conhecimentoService.findByNrConhecimentoIdFilialOrigem(nrConhecimento, idFilialOrigem, "P", ConstantesExpedicao.CONHECIMENTO_NACIONAL);
		return conhecimentoService.findEnderecosClientesConhecimentos(l);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return conhecimentoExcluirService.getRowCount(criteria);
	}

    /**
     * LMS-3054
     * Busca os pr�-CTEs mais velhos que o valor do par�metro
     * geral "TMP_MONIT_DESC_COL_EXC" para posterior exclus�o
     * via agendamento/batch.
     * 
     * @param qtdHoras
     * @return 
     */
	@AssynchronousMethod(name = "expedicao.executeLimpaMonitoramentosAntigos", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeLimpaMonitoramentosAntigos() {

		//Par�metro geral "TMP_MONIT_DESC_COL_EXC": quantas horas para tr�s considerar para excluir um CTe. 
		BigDecimal horasParam = (BigDecimal)configuracoesFacade.getValorParametro("TMP_MONIT_DESC_COL_EXC");
		DateTime dt = JTDateTimeUtils.getDataHoraAtual();
		dt = dt.minusHours(horasParam.intValue());

		List<Long> idConhecimentos = conhecimentoService.findCtesAntigosRemover(dt);

		//Workaround: A exclus�o � permitida apenas para a filial do user logado.
		Filial filialUL = filialService.findFilialUsuarioLogado();

		for (Long idCTe : idConhecimentos) {
			conhecimentoExcluirService.removePreCTRC(idCTe, filialUL.getIdFilial());
		}
	}

	public void setConhecimentoExcluirService(ConhecimentoExcluirService conhecimentoExcluirService) {
		this.conhecimentoExcluirService = conhecimentoExcluirService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}

	public void setMonitoramentoDescargaService(
			MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
