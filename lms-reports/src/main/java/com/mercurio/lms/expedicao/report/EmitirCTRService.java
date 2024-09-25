package com.mercurio.lms.expedicao.report;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.dao.EmitirCTRDAO;
import com.mercurio.lms.expedicao.model.service.*;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.model.service.AssynchronousProcessService;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;


/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.emitirCTRService"
 */
public class EmitirCTRService extends EmitirCTOService {
	private EmitirCTRDAO emitirCTRDAO;
	private ImpressoraFormularioService impressoraFormularioService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private ParametroGeralService parametroGeralService;
	private AssynchronousProcessService assynchronousProcessService;
	private EmissaoCtoService emissaoCtoService;
	private EmitirNFTService emitirNFTService;
	private FilialService filialService;
	private GerarConhecimentoEletronicoService gerarConhecimentoEletronicoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NFEConjugadaService nfeConjugadaService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ConhecimentoCancelarService conhecimentoCancelarService;
	private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private UnidadeFederativaService unidadeFederativaService;

	private static final String STATUS_SOM_EMITIDO = "2";
	private static final String STATUS_SOM_CANCELADO = "9";

	private static final String PARCEL = "PARCEL";
	private static final String FREIGHT = "FREIGHT";

	private Log log = LogFactory.getLog(EmitirCTRService.class);

	public List<Map> findCTRCsAEmitir(
		String tpDocumento,
		String tpOperacaoEmissao,
		String tpOpcaoEmissao,
		Long idFilial,
		Long nrProximoFormulario,
		Long nrConhecimento,
		String dsMacAddress,
		Long idMonitoramentoDescarga
	) {

		Boolean reentrega = findEmissaoReentrega(idMonitoramentoDescarga);

		List<Object[]> conhecimentos = getEmitirCTRDAO().findConhecimento(
				tpDocumento,
				tpOperacaoEmissao,
				tpOpcaoEmissao,
				nrConhecimento,
				idFilial,
				idMonitoramentoDescarga,
				new LinkedList<Map>(),
				nrProximoFormulario,
				reentrega);

		List<Map> listaConhecimentos = new LinkedList<Map>();
		Map<String, Object> dataConh = null;
		for (Object[] item : conhecimentos) {
			Conhecimento conhecimento = (Conhecimento) item[0];
			VolumeNotaFiscal volumeNotaFiscal = (VolumeNotaFiscal) item[1];
			dataConh = new HashMap<String, Object>();
			dataConh.put("idConhecimento", conhecimento.getIdDoctoServico());
			dataConh.put("idFilialOrigem", conhecimento.getFilialOrigem().getIdFilial());
			dataConh.put("nrConhecimento", volumeNotaFiscal.getNrConhecimento());
			dataConh.put("nmPessoa", StringUtils.left(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmPessoa(), 15));
			dataConh.put("sgRedFilialOrigem", filialService.findSgFilialLegadoByIdFilial(conhecimento.getFilialByIdFilialOrigem().getIdFilial()));
			listaConhecimentos.add(dataConh);
		}
		return listaConhecimentos;
	}


	/**
	 * Busca a lista de conhecimentos (CTE) que já estão processados pela receita
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public Map<String, Object> findConhecimentoCTE(Long idMonitoramentoDescarga) {
		Map<String, Object> retorno = new HashMap<String, Object>();

		Integer countAProcessados = emitirCTRDAO.findNumberEmissaoLiberada(idMonitoramentoDescarga);

		retorno.put("countAProcessados", countAProcessados);

		List<Object[]> mde = emitirCTRDAO.findConhecimentoCTE(idMonitoramentoDescarga);

		if (mde != null && mde.size() > 0) {
			List<Map<String, Object>> cte = new ArrayList<Map<String, Object>>();
			for (Object[] m : mde) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tpSituacaoDocumento", m[0]);
					map.put("idConhecimento", m[1]);
					map.put("nrProtocolo", m[2]);

					Map<String, Object> mapComplemento = conhecimentoService.findComplementosXMLCTE((Long)m[1]);
					map.put("complementoXML", mapComplemento);
				cte.add(map);
				}
			retorno.put("ctes", cte);
		}

		return retorno;
	}

	/**
	 * Busca a lista de conhecimentos (CTE) que já estão processados pela receita
	 * @param idFatura
	 * @return
	 */
	public List<Map<String, Object>> findConhecimentoCTEbyIdFatura(Long idFatura) {
		List<Object[]> mde = emitirCTRDAO.findConhecimentoCTEbyIdFatura(idFatura);

		if (mde != null && mde.size() > 0) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Object[] m : mde) {
				if ("A".equals((String)m[0])) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tpSituacaoDocumento", m[0]);
					map.put("idConhecimento", m[1]);
					map.put("nrProtocolo", m[2]);
					result.add(map);
				} else {
					result.add(null);
				}
			}

			return result;
		}

		return null;
	}

	/**
	 * Busca a lista de conhecimentos (CTE) que já estão processados pela receita
	 * @param idCliente
	 * @return
	 */
	public List<Map<String, Object>> findConhecimentoCTEbyIdCliente(Long idCliente, YearMonthDay dhEmissao) {
		List<Object[]> mde = emitirCTRDAO.findConhecimentoCTEbyIdCliente(idCliente, dhEmissao);

		if (mde != null && mde.size() > 0) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Object[] m : mde) {
				if ("A".equals((String)m[0])) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tpSituacaoDocumento", m[0]);
					map.put("idConhecimento", m[1]);
					map.put("nrProtocolo", m[2]);
					result.add(map);
				} else {
					result.add(null);
				}
			}

			return result;
		}

		return null;
	}

	/**
	 *
	 * Executa a emissão do CTRC
	 *
	 * @param tpOperacaoEmissao
	 * @param tpOpcaoEmissao
	 * @param idFilial
	 * @param nrProximoFormulario
	 * @param nrProximoCodigoBarras Caso passado é validado com o número do formulário e atualizado em conhecimento.nrCodigoBarras
	 * @param nrConhecimento
	 * @param dsMacAddress
	 * @param idMonitoramentoDescarga
	 * @param token
	 * @param ctrcsDuplicados
	 */
	public Map<String, Object> executePreEmitirCTR(String tpOperacaoEmissao, String tpOpcaoEmissao, Long idFilial, Long nrConhecimento, Long idMonitoramentoDescarga) {

		/** LMS-414 - Busca Eventos de Cancelamento */
		final List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findEventoDoctoServicoByMonitoramento(idMonitoramentoDescarga, ConstantesSim.EVENTO_DOCUMENTO_CANCELADO);
		verifyCancelamentoObservacaoCTRC(eventos);

		Map<String, Object> map = new HashMap<String, Object>();

		MonitoramentoDescarga monitoramentoDescarga = null;
		if (idMonitoramentoDescarga != null) {
			monitoramentoDescarga = monitoramentoDescargaService.findById(idMonitoramentoDescarga);
		}
		Boolean semNumeroReservado = isEmissaoReentrega(monitoramentoDescarga);

		if(!isReemissao(tpOperacaoEmissao) && monitoramentoDescarga != null){
			Filial filial = filialService.findById(idFilial);
			if(!filial.getBlSorter() && !checkDescargaIniciada(monitoramentoDescarga)){
				validateSituacaoDescarga(monitoramentoDescarga);
			}
		}

		List<Object[]> conhecimentos = findConhecimentos(tpOperacaoEmissao,	tpOpcaoEmissao, idFilial, nrConhecimento, idMonitoramentoDescarga, semNumeroReservado);

		/*LMS-1031: Marca a situação do monitoramento como "Q" (Emissão iniciada)*/
		if(!isReemissao(tpOperacaoEmissao) && monitoramentoDescarga != null ){
			if(CollectionUtils.isNotEmpty(conhecimentos)){
				if(!checkDescargaIniciada( monitoramentoDescarga)){
				monitoramentoDescargaService.executeIniciarEmissao(idMonitoramentoDescarga);
			}
				generateLimiteCubagemObservacaoCTRC(conhecimentos);
		}
		}

		map.put("semNumeroReservado", semNumeroReservado);
		map.put("conhecimentos", conhecimentos);
		return map;

	}

	public Map<String, Object> executeEnvioEmissaoSefaz(String tpOperacaoEmissao, Long idMonitoramentoDescarga, Boolean semNumeroReservado, Conhecimento conhecimento,  VolumeNotaFiscal volumeNotaFiscal) {
		if (isNotaFiscal(conhecimento)) {
			String retorno = emitirNFTService.storeEmitirNotaFiscal(conhecimento.getIdDoctoServico(), null, tpOperacaoEmissao, semNumeroReservado);

			if (nfeConjugadaService.isAtivaNfeConjugada(conhecimento.getFilialByIdFilialOrigem().getIdFilial())) {
				Map<String, Object> map = new HashMap<String, Object>();
        		map.put("nfeConjugada", retorno);
        		return map;
			}

	        if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue())
	        		&& StringUtils.isNotBlank(retorno) && StringUtils.isNumeric(retorno)) {
	        	return executeMontarInfNte(conhecimento, retorno);
	        }

		} else if(ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue())){
			return executeEnvioCTEEmissaoSefaz(idMonitoramentoDescarga,semNumeroReservado, conhecimento, volumeNotaFiscal);
		}
		return null;
	}


	private Map<String, Object> executeEnvioCTEEmissaoSefaz(Long idMonitoramentoDescarga,
			Boolean semNumeroReservado, Conhecimento conhecimento,
			VolumeNotaFiscal volumeNotaFiscal) {
		if(idMonitoramentoDescarga == null) {
			idMonitoramentoDescarga = volumeNotaFiscal.getMonitoramentoDescarga() != null ? volumeNotaFiscal.getMonitoramentoDescarga().getIdMonitoramentoDescarga() : null;
		}

		String xml = gerarConhecimentoEletronicoService.generateConhecimentoEletronico(conhecimento.getIdDoctoServico(), volumeNotaFiscal ,semNumeroReservado, idMonitoramentoDescarga);

		if (xml != null && !(xml.length() <= 0)){
			Map<String, Object> mapCte = new HashMap<String, Object>();
			Map<String, String> map = new HashMap<String, String>();
			map.put(conhecimento.getIdDoctoServico().toString(), xml);
			mapCte.put("cte", map);
			return mapCte;
		}

		return null;
	}

	private void verifyCancelamentoObservacaoCTRC(final List<EventoDocumentoServico> eventos) {
		/** Caso encontrado cancela cancela conhecimento */
		for (final EventoDocumentoServico eventoDocumentoServico : eventos) {
			conhecimentoCancelarService.storeCancelamentoObservacaoLimiteCalculoFrete(eventoDocumentoServico.getDoctoServico().getIdDoctoServico(), eventoDocumentoServico.getObComplemento());
		}
	}

	private void generateLimiteCubagemObservacaoCTRC(final List<Object[]> conhecimentos) {
		for (Object[] m : conhecimentos) {
			Conhecimento conhecimento = (Conhecimento) m[0];
			generateObservacaoDoctoServico(conhecimento);
			generateObservacaoDoctoServicoParcelFreight(conhecimento);
			generateObservacaoServicoDifal(conhecimento);
		}
	}

	private void generateObservacaoDoctoServico(Conhecimento conhecimento){
		if(!BooleanUtils.isTrue(conhecimento.getBlDesconsiderouPesoCubado())){
			return;
		}
		String dsObservacao = parametroGeralService.findDsConteudoByNmParametro("OBS_DOCTO_DESC_PESO_CUBADO");
		ObservacaoDoctoServico observacao = ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, dsObservacao, Boolean.FALSE);
		observacaoDoctoServicoService.store(observacao);
	}

	private void generateObservacaoDoctoServicoParcelFreight(Conhecimento conhecimento){
		if(null == conhecimento.getTpCargaDocumento()){
			return;
		}

		String tpCargaDocumento = conhecimento.getTpCargaDocumento().getValue();
		String dsObservacao = "P".equals(tpCargaDocumento) ? PARCEL : FREIGHT;
		ObservacaoDoctoServico observacao = ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, dsObservacao, Boolean.TRUE);
		observacaoDoctoServicoService.store(observacao);
	}

	private void generateObservacaoServicoDifal(Conhecimento conhecimento) {

		BigDecimal vlImpostoDifal = conhecimento.getVlImpostoDifal();

		if(vlImpostoDifal != null && vlImpostoDifal.compareTo(new BigDecimal(0)) > 0) {

			String sgUfDestino = this.unidadeFederativaService.findSgUnidadeFederativaByIdDoctoServico(conhecimento.getIdDoctoServico());

			String mensagem = this.getConfiguracoesFacade().getMensagem("LMS-23049");
			String dsObservacao = MessageFormat.format(mensagem, vlImpostoDifal, sgUfDestino);

			ObservacaoDoctoServico observacao = ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, dsObservacao, Boolean.TRUE);
			observacaoDoctoServicoService.store(observacao);
		}

	}

	private String getTpDocumento(String tpOperacaoEmissao) {
		/*Obtem o tipo de documento*/
		String tpDocumento = ConstantesExpedicao.CONHECIMENTO_NACIONAL;
		if(tpOperacaoEmissao != null ){
			if( ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacaoEmissao) ){
				tpDocumento  = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
			} else if(ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_REEMISSAO_CTE.equals(tpOperacaoEmissao)){
				tpDocumento = ConstantesExpedicao.CONHECIMENTO_ELETRONICO;
			} else if(ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacaoEmissao)){
				tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA;
			}
		}
		return tpDocumento;
	}


	private List<Object[]> findConhecimentos(String tpOperacaoEmissao,
			String tpOpcaoEmissao, Long idFilial, Long nrConhecimento,
			Long idMonitoramentoDescarga, Boolean semNumeroReservado) {
		List<Object[]> conhecimentos = getEmitirCTRDAO().findConhecimento(
				getTpDocumento(tpOperacaoEmissao),
				tpOperacaoEmissao,
				tpOpcaoEmissao,
				nrConhecimento,
				idFilial,
				idMonitoramentoDescarga,
				null,
				null,
				semNumeroReservado);

		if ((conhecimentos == null || conhecimentos.size() == 0) && ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao)){
			throw new BusinessException("LMS-04385");
		}
		return conhecimentos;
	}



	private boolean isNotaFiscal(Conhecimento conhecimento) {
		return ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue()) || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue());
	}


	private Map<String, Object> executeMontarInfNte(Conhecimento conhecimento,	String retorno) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> nte = new HashMap<String, Object>();

		nte.put("tpSituacaoDocumento", conhecimento.getTpSituacaoConhecimento());
		nte.put("idConhecimento", conhecimento.getIdDoctoServico());
		nte.put("sgFilial", conhecimento.getFilialByIdFilialOrigem().getSgFilial());

		Map<String, Object> nteInfs = monitoramentoDocEletronicoService.executeMontarInfNte(conhecimento.getIdDoctoServico(), Long.valueOf(retorno));
		nte.put("nfeInfs", nteInfs);
		map.put("nte", nte);
		return map;
	}

	/**
	 *
	 * Executa a emissão do CTRC
	 *
	 * @param tpOperacaoEmissao
	 * @param tpOpcaoEmissao
	 * @param idFilial
	 * @param nrProximoFormulario
	 * @param nrProximoCodigoBarras Caso passado é validado com o número do formulário e atualizado em conhecimento.nrCodigoBarras
	 * @param nrConhecimento
	 * @param dsMacAddress
	 * @param idMonitoramentoDescarga
	 * @param token
	 * @param ctrcsDuplicados
	 */
	public void executeEmitirCTR(
		String tpOperacaoEmissao,
		String tpOpcaoEmissao,
		Long idFilial,
		Long nrProximoFormulario,
		Long nrProximoCodigoBarras,
		Long nrConhecimento,
		String dsMacAddress,
		Long idMonitoramentoDescarga,
		Double token,
		List<Map> ctrcsDuplicados,
		Boolean needConfirmation) {

		/* LMS-1031: Impede que sejam emitidos monitoramentos que estejam com a situação "Q" (Emissão iniciada)
		 * Regra deve ser executada apenas se for emissão por veiculo, ou seja, idMonitoramentoDescarga != null
		 * */

		if(!isReemissao(tpOperacaoEmissao) && idMonitoramentoDescarga != null){
			MonitoramentoDescarga monitoramentoDescarga = monitoramentoDescargaService.findById(idMonitoramentoDescarga);
			Filial filial = filialService.findById(idFilial);
			if(!filial.getBlSorter() && !checkDescargaIniciada(monitoramentoDescarga)){
				validateSituacaoDescarga(monitoramentoDescarga);
			}

		}

		/*Gera o token, necessários para monitoramento da emissao do CTRC*/
		assynchronousProcessService.generateToken(token, new HashMap());


		/*Verifica se a filial possue o LMS em funcionamento*/
		Boolean lmsImplantadoFilial = filialService.lmsImplantadoFilial(SessionUtils.getFilialSessao());

		/*Tipo do formulario*/
		String tpFormulario = "CTR";

		/*Obtem o tipo de documento*/
		String tpDocumento = ConstantesExpedicao.CONHECIMENTO_NACIONAL;
		if(tpOperacaoEmissao != null ){
			if( ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacaoEmissao) ){
				tpDocumento  = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
				/*Se o tipo de emissao for NFT define NFS para o tipo de formulário
				utilizando a mesma numeração que as notas ficais de serviço*/
				tpFormulario = ConstantesExpedicao.NOTA_FISCAL_SERVICO;
			} else if(ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_REEMISSAO_CTE.equals(tpOperacaoEmissao)){
				tpDocumento = ConstantesExpedicao.CONHECIMENTO_ELETRONICO;
			} else if(ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacaoEmissao)){
				tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA;
			}
		}

		/*Obtem a impressora matricial para impressao*/
		List forms = new ArrayList();

		ImpressoraFormulario impressoraFormulario = null;
		if( isImpressaoMatricial(tpDocumento)){
			impressoraFormulario = getEmitirDocumentoService().findImpressoraFormulario(idFilial, dsMacAddress, tpFormulario, nrProximoFormulario, nrProximoCodigoBarras, Boolean.TRUE);
		}

		/*Atualiza os documentos duplicados no SOM*/
		List<Long> idsCtrcsDuplicados = new LinkedList<Long>();
		for (Map map : ctrcsDuplicados) {
			idsCtrcsDuplicados.add((Long) map.get("idConhecimento"));
		}

		if(!lmsImplantadoFilial) {
			conhecimentoService.updateConhecimentosDuplicadosSom(idsCtrcsDuplicados);
		}

		Boolean semNumeroReservado = findEmissaoReentrega(idMonitoramentoDescarga);

		List<Object[]> conhecimentos;

		/*Obtem a lista de CTRC para emissao*/
	    conhecimentos = getEmitirCTRDAO().findConhecimento(
				tpDocumento,
				tpOperacaoEmissao,
				tpOpcaoEmissao,
				nrConhecimento,
				idFilial,
				idMonitoramentoDescarga,
				ctrcsDuplicados,
				nrProximoFormulario,
				semNumeroReservado);

		if ((conhecimentos == null || conhecimentos.size() == 0) && ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao)){
			throw new BusinessException("LMS-04385");
		}

		/*Verifica a quantidade de formulários disponiveis para impressao*/
		long nrFormularios = conhecimentos.size();
		long nrFormulariosDisponiveis = nrFormularios;
		if(isImpressaoMatricial(tpDocumento)){
			nrFormulariosDisponiveis = impressoraFormulario.getNrFormularioFinal().longValue() - impressoraFormulario.getNrUltimoFormulario().longValue();
			if(nrFormularios > nrFormulariosDisponiveis) {
				nrFormularios = nrFormulariosDisponiveis;
			}
		}

		/*Atualiza o Token para monitoramento*/
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("totalProcessar", nrFormularios);
		result.put("totalProcessado", Integer.valueOf(0));
		assynchronousProcessService.updateToken(token, result);

		/*LMS-1031: Marca a situação do monitoramento como "Q" (Emissão iniciada)*/
		if(! isReemissao(tpOperacaoEmissao) && idMonitoramentoDescarga != null ){
			if(CollectionUtils.isNotEmpty(conhecimentos)){
				if(!checkDescargaIniciada( monitoramentoDescargaService.findById(idMonitoramentoDescarga))){
				monitoramentoDescargaService.executeIniciarEmissao(idMonitoramentoDescarga);
			}
				generateLimiteCubagemObservacaoCTRC(conhecimentos);
		}
		}

		/*Se for REEMISSAO de CTR ou NFT verificar se o CTRC pode ser emitido*/
		Conhecimento conhecimento = null;
		Iterator it = null;
		if(isReemissao(tpOperacaoEmissao)) {
			it = conhecimentos.iterator();

			for(int i=0; i<nrFormularios; i++) {

				Object[] item = (Object[]) it.next();
				conhecimento = (Conhecimento) item[0];
				if(!"E".equals(conhecimento.getTpSituacaoConhecimento().getValue()) && !"C".equals(conhecimento.getTpSituacaoConhecimento().getValue())) {
					throw new BusinessException("LMS-04110");
				}

				result = (Map) assynchronousProcessService.getToken(token);
				result.put("totalProcessado", i + 1);
				assynchronousProcessService.updateToken(token, result);

			}/*for*/

		}/*if*/

		/*Inicia o processo de impressao do documento*/
		List<String> ctrcs = new ArrayList<String>();
		List<Map<String,Object>> ntes = new ArrayList<Map<String,Object>>();
		Map<String,String> ctes = new HashMap<String, String>();

		List<String> listMonitoramentoNfeConjugada = new ArrayList<String>();

		Map<String, String> errosEmissaoConhecimento = new HashMap<String, String>();

		it = conhecimentos.iterator();
		Boolean cteInutilizado = true;

		for(int i=0; i<nrFormularios; i++) {
			String statusConhSOM = null;
			Object[] item = (Object[]) it.next();

			/*Conhecimento*/
			conhecimento = (Conhecimento) item[0];

			/*Volume da nota*/
			VolumeNotaFiscal volumeNotaFiscal = (VolumeNotaFiscal) item[1];

			/*Obtem o monitoramento de descarga*/
			if(idMonitoramentoDescarga == null) {
				idMonitoramentoDescarga = volumeNotaFiscal.getMonitoramentoDescarga() != null ? volumeNotaFiscal.getMonitoramentoDescarga().getIdMonitoramentoDescarga() : null;
			}

			/*Copia dados da impressora para poder reverter caso a emissão do gere erro*/
			ImpressoraFormulario impressoraFormularioRecert = new ImpressoraFormulario();
			if(!ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(tpDocumento) && !ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(tpDocumento)){
				impressoraFormularioRecert.setNrUltimoFormulario(impressoraFormulario.getNrUltimoFormulario());
				impressoraFormularioRecert.setNrUltimoSeloFiscal(impressoraFormulario.getNrUltimoSeloFiscal());
				impressoraFormularioRecert.setNrUltimoCodigoBarras(impressoraFormulario.getNrUltimoCodigoBarras());

				/*Obtem o numero do formulario*/
				getEmitirDocumentoService().findProximoFormulario(impressoraFormulario);
			}

			/*Salva o documento , informa eventos identifica se é uma emissao*/
			try{
				if (isNotaFiscal(conhecimento)) {
					/*Emite NFT/NTE*/
					String retorno = emitirNFTService.storeEmitirNotaFiscal(conhecimento.getIdDoctoServico(), impressoraFormulario, tpOperacaoEmissao, semNumeroReservado);

					if (nfeConjugadaService.isAtivaNfeConjugada(conhecimento.getFilialByIdFilialOrigem().getIdFilial())) {
						listMonitoramentoNfeConjugada.add(retorno);
					} else {
	                    if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue())) {
	                    	//NOTA_FISCAL_TRANSPORTE_ELETRONICA retorna o id do monitoramento eletronico
	                    	if (StringUtils.isNotBlank(retorno) && StringUtils.isNumeric(retorno)) {

	                    		HashMap<String, Object> nte = new HashMap<String, Object>();

	                    		nte.put("tpSituacaoDocumento", conhecimento.getTpSituacaoConhecimento());
	                    		nte.put("idConhecimento", conhecimento.getIdDoctoServico());
	                    		nte.put("sgFilial", conhecimento.getFilialByIdFilialOrigem().getSgFilial());

	                    		Map<String, Object> nfeInfs = monitoramentoDocEletronicoService.executeMontarInfNte(conhecimento.getIdDoctoServico(), Long.valueOf(retorno));
	            				nte.put("nfeInfs", nfeInfs);

								ntes.add(nte);
	                    	}
	                    } else {
	                    	ctrcs.add(retorno);
	                    }
                    }
				} else if(ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue())){

					String xml = gerarConhecimentoEletronicoService.generateConhecimentoEletronico(conhecimento.getIdDoctoServico(), volumeNotaFiscal ,semNumeroReservado,idMonitoramentoDescarga);

					if( !"CANCELADO".equals(xml) ){
						cteInutilizado = false;
					}

					if (xml != null && !(xml.length() <= 0)){
						ctes.put(conhecimento.getIdDoctoServico().toString(),xml);
					}

				}else{
					emissaoCtoService.storeEmitirConhecimento(conhecimento.getIdDoctoServico()
							,impressoraFormulario
							,nrProximoCodigoBarras
							,tpOperacaoEmissao
							,idFilial
							,lmsImplantadoFilial
							,forms
							,ctrcs
							,statusConhSOM
							,volumeNotaFiscal
							,semNumeroReservado
							,Boolean.TRUE);

						if(nrProximoCodigoBarras != null){
							nrProximoCodigoBarras = CodigoBarrasService.incrementaCodigo(nrProximoCodigoBarras);
						}
				}
			} catch (Exception e) {
				log.error("Erro na emissão de conhecimentos", e);

				/*Caso ocorrer algum erro na emissao armazena os dados para log*/
				if (e instanceof BusinessException) {
					BusinessException be = (BusinessException)e;
					String mensagem = be.getMessageKey() + ": ";
					if (be.getMessageArguments() == null ) {
						mensagem += getConfiguracoesFacade().getMensagem(be.getMessageKey());
					} else {
						mensagem += getConfiguracoesFacade().getMensagem(be.getMessageKey(), be.getMessageArguments());
					}
					errosEmissaoConhecimento.put(conhecimento.getFilialByIdFilialOrigem().getSgFilial()+" "+conhecimento.getNrConhecimento(), mensagem);
				} else {
				errosEmissaoConhecimento.put(conhecimento.getFilialByIdFilialOrigem().getSgFilial()+" "+conhecimento.getNrConhecimento(), e.getMessage());
				}

				if(!ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue()) && !ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue())) {
				/*Reverte dados possivelmente alterados da impressora:*/
				impressoraFormulario.setNrUltimoFormulario(impressoraFormularioRecert.getNrUltimoFormulario());
				impressoraFormulario.setNrUltimoSeloFiscal(impressoraFormularioRecert.getNrUltimoSeloFiscal());
				impressoraFormulario.setNrUltimoCodigoBarras(impressoraFormularioRecert.getNrUltimoCodigoBarras());
		}
			}
			result = (Map) assynchronousProcessService.getToken(token);
			result.put("totalProcessado", i + 1);
			assynchronousProcessService.updateToken(token, result);

		}/*for*/

		/*Atualiza dados da impressora formulario*/
		if( nrFormularios > 0 && !ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(tpDocumento) && !ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(tpDocumento)) {
			impressoraFormularioService.store(impressoraFormulario);
		}

		/*Se nao foram encontrados erros na digitação do conhecimento, então libera o veiculo*/
		if(! isReemissao(tpOperacaoEmissao) && (errosEmissaoConhecimento.isEmpty() || ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao))){

			Boolean emissaoVeiculo   = "V".equals(tpOpcaoEmissao);
			Boolean emissaoDocumento = ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacaoEmissao) || ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao);

			/*Busca na frota se existem documentos CTR pendentes*/
			List listCTRVeiculo = getEmitirCTRDAO().findConhecimento(ConstantesExpedicao.CONHECIMENTO_NACIONAL, tpOperacaoEmissao, "V", null, idFilial, idMonitoramentoDescarga, ctrcsDuplicados, null, semNumeroReservado);

			/*Busca na frota se existem documentos NFT pendentes*/
			List listNFTVeiculo = getEmitirCTRDAO().findConhecimento(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE, tpOperacaoEmissao, "V", null, idFilial, idMonitoramentoDescarga, ctrcsDuplicados, null, semNumeroReservado);

			/*Busca na frota se existem documentos NTE pendentes*/
			List listNFEVeiculo = getEmitirCTRDAO().findConhecimento(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA, tpOperacaoEmissao, "V", null, idFilial, idMonitoramentoDescarga, ctrcsDuplicados, null, semNumeroReservado);

			/*Verifica se existem docs pendentes*/
			Boolean semDocsPendentes = CollectionUtils.isEmpty(listCTRVeiculo) && CollectionUtils.isEmpty(listNFTVeiculo)
						&& CollectionUtils.isEmpty(listNFEVeiculo);

			/*Verifica se existe formularios disponiveis*/
			Boolean existeFormularioDisponivel = nrFormularios > 0 && conhecimentos.size() <= nrFormulariosDisponiveis;

			/*Atualiza o monitoramento de descarga*/ //LMS-4048: A não ser que seja CTe
			if (emissaoDocumento && idMonitoramentoDescarga != null && errosEmissaoConhecimento.isEmpty() && cteInutilizado ) {

				if(semDocsPendentes || (existeFormularioDisponivel && semDocsPendentes) || emissaoVeiculo ){
					monitoramentoDescargaService.updateSituacaoMonitoramentoByIdMeioTransporte(idMonitoramentoDescarga, idFilial);
				}
			}
			result.put("semDocsPendentes", semDocsPendentes);
			/*Informa o tipo de documento utilizado na emissao*/
			result.put("tpDocumentoEmissao", tpDocumento);

			/*Informa a quantidade de NFT e CTR pendentes na emissão*/
			result.put("CTRPendentes", listCTRVeiculo.size());
			result.put("NFTPendentes", listNFTVeiculo.size());
			result.put("NFEPendentes", listNFEVeiculo.size());

			/*Informa se existem documentos pendentes para o veiculo*/
			Integer pendentes = listCTRVeiculo.size() + listNFTVeiculo.size() + listNFEVeiculo.size();

			result.put("ctrcsPendentesVeiculo", pendentes);

			Object nrVias = getConfiguracoesFacade().getValorParametro(idFilial, "NRO_VIAS_RPS");
			result.put( "nrViasRps", nrVias == null ? 1 : Integer.valueOf(nrVias.toString()) );

		}

		/*Passa informacoes para a tela e atualiza o token*/
		result.put("errosConhecimento", errosEmissaoConhecimento);
		result.put("ctrcs", ctrcs);
		result.put("ctes", ctes);
		result.put("ntes", ntes);
		result.put("forms", forms);
		result.put("listNfeConjugada", listMonitoramentoNfeConjugada);

		result.put("emissaoFinalizada", nrFormularios > 0 && conhecimentos.size() <= nrFormulariosDisponiveis);

		result.put("blProcessando", Boolean.FALSE);
		assynchronousProcessService.updateToken(token, result);
	}

	private void validateSituacaoDescarga(
			MonitoramentoDescarga monitoramentoDescarga) {
		if (monitoramentoDescarga != null &&
				!"P".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue()) &&
					!"M".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue()) &&
					!"S".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue())){
			throw new BusinessException("LMS-04365");
		}
	}


	private boolean checkDescargaIniciada(MonitoramentoDescarga monitoramentoDescarga) {
		return "G".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue());
	}


	public void executePendenciaNaoAprovado(Boolean needConfirmation, String tpOperacaoEmissao, Long idMonitoramentoDescarga ){
		if ( needConfirmation != null ){
			MonitoramentoDescarga monitoramentoDescarga = monitoramentoDescargaService.findById(idMonitoramentoDescarga);
			if( needConfirmation ){
				if("H".equals(tpOperacaoEmissao) || "E".equals(tpOperacaoEmissao) || "N".equals(tpOperacaoEmissao)){
					if( ("Balcao".equals(monitoramentoDescarga.getNrFrota()) && "M".equals(monitoramentoDescarga.getTpSituacaoDescarga().getValue()) )){
						List<Integer> listDoctosComPendencia = getDoctoServicoService().validateDoctoServicoComPendenciaAprovacao(idMonitoramentoDescarga);
						if (listDoctosComPendencia.size() > 0){
							throw new BusinessException("LMS-04431");
						}
					}
				}
			}else{
				monitoramentoDescargaService.inutilizarDocumentoNaoAprovado(monitoramentoDescarga, "E");
			}
			monitoramentoDescargaService.inutilizarDocumentoNaoAprovado(monitoramentoDescarga, "R");
		}
	}

	private boolean isReemissao(String tpOperacaoEmissao) {
		return ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_REEMISSAO_CTE.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacaoEmissao);
	}

	private boolean isImpressaoMatricial(String tpDocumento) {
		return ! ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(tpDocumento) && ! ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(tpDocumento);
	}

	/**
	 * Identifica se a emissao do conhecimento é Reentrega
	 *
	 * @param idMonitoramentoDescarga
	 * @return Boolean
	 */
	public Boolean findEmissaoReentrega(Long idMonitoramentoDescarga){
		Boolean reentrega = Boolean.FALSE;

		if(idMonitoramentoDescarga != null){
			MonitoramentoDescarga monitoramento = monitoramentoDescargaService.findById(idMonitoramentoDescarga);
			if(monitoramento != null && (("REEN".equals(monitoramento.getNrFrota()) && "REEN".equals(monitoramento.getNrPlaca()))) || ("COMP".equals(monitoramento.getNrFrota()) && "COMP".equals(monitoramento.getNrPlaca())
					//LMS-4068 / Também deve emitir substituto sem pesagem / 04.01.01.01N4 Digitar conhecimento substituto
					|| ((ConstantesExpedicao.NR_FROTA_SUBSTITUTO.equals(monitoramento.getNrFrota()) && ConstantesExpedicao.NR_FROTA_SUBSTITUTO.equals(monitoramento.getNrPlaca()))))
					){
				reentrega = Boolean.TRUE;
			}
		}

		return reentrega;
	}

	public Boolean isEmissaoReentrega(MonitoramentoDescarga monitoramento){
		return monitoramento != null
				&& (("REEN".equals(monitoramento.getNrFrota()) && "REEN"
						.equals(monitoramento.getNrPlaca())))
				|| ("COMP".equals(monitoramento.getNrFrota())
						&& "COMP".equals(monitoramento.getNrPlaca()) || ((ConstantesExpedicao.NR_FROTA_SUBSTITUTO
						.equals(monitoramento.getNrFrota()) && ConstantesExpedicao.NR_FROTA_SUBSTITUTO
						.equals(monitoramento.getNrPlaca()))));
	}

	public Map verificaStatusEmissao(Double token) {
		Map mapProcess = new HashMap();
		if (token != null) {
			mapProcess = (Map) assynchronousProcessService.getToken(token);
			if (mapProcess != null && mapProcess.get("blProcessando") != null && !(Boolean) mapProcess.get("blProcessando")) {
				assynchronousProcessService.removeToken(token);
			}
		}
		return mapProcess;
	}

	public List executeFindDadosReenvioSOM(Long idFilial, List<String> ctrcsStatusX) {
		List forms = new ArrayList();
		Integer tmpExc = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("TMP_BUSCA_CONH_REENVIO_SOM", false)).intValue();
		DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
		DateTime dhFiltro = sysDate.minusHours(tmpExc);
		getEmitirCTRDAO().updateConhecimentosErroSOM(idFilial, dhFiltro);
		List<Conhecimento> conhecimentosReenviarSOM = getEmitirCTRDAO().updateConhecimentosStatusXSOM(idFilial, dhFiltro,ctrcsStatusX);

		for (Conhecimento conhecimento : conhecimentosReenviarSOM) {
			Map mapForms = new HashMap();
			mapForms.put("nrFormulario", conhecimento.getNrFormulario());
			mapForms.put("nrConhecimento", conhecimento.getNrConhecimento());
			mapForms.put("dhEmissao", conhecimento.getDhEmissao());
			if("E".equals(conhecimento.getTpSituacaoConhecimento().getValue())) {
				mapForms.put("dataSOM", conhecimentoService.findDataToSOM(conhecimento.getIdDoctoServico(), STATUS_SOM_EMITIDO));
			} else {
				mapForms.put("dataSOM", conhecimentoService.findDataToSOM(conhecimento.getIdDoctoServico(), STATUS_SOM_CANCELADO));
			}
			forms.add(mapForms);
		}
		return forms;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setAssynchronousProcessService(AssynchronousProcessService assynchronousProcessService) {
		this.assynchronousProcessService = assynchronousProcessService;
	}
	public void setEmissaoCtoService(EmissaoCtoService emissaoCtoService) {
		this.emissaoCtoService = emissaoCtoService;
	}
	public void setEmitirNFTService(EmitirNFTService emitirNFTService) {
		this.emitirNFTService = emitirNFTService;
	}
	public void setGerarConhecimentoEletronicoService(
			GerarConhecimentoEletronicoService gerarConhecimentoEletronicoService) {
		this.gerarConhecimentoEletronicoService = gerarConhecimentoEletronicoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public EmitirCTRDAO getEmitirCTRDAO() {
		return emitirCTRDAO;
	}
	public void setEmitirCTRDAO(EmitirCTRDAO emitirCTRDAO) {
		this.emitirCTRDAO = emitirCTRDAO;
	}
	public void setImpressoraFormularioService(ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}
	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setConhecimentoCancelarService(ConhecimentoCancelarService conhecimentoCancelarService) {
		this.conhecimentoCancelarService = conhecimentoCancelarService;
	}

	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

}
