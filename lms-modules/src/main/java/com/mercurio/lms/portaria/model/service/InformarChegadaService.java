package com.mercurio.lms.portaria.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.FilialRotaCcService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.portaria.model.dao.SaidaChegadaDAO;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.portaria.informarChegadaService"
 */
public class InformarChegadaService {
	private SaidaChegadaDAO saidaChegadaDAO;
	private ControleTrechoService controleTrechoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private FilialRotaCcService filialRotaCcService;
	private EventoControleCargaService eventoControleCargaService;
	private ControleCargaService controleCargaService;
	private ConfiguracoesFacade configuracoesFacade;
	private BoxService boxService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private EventoVolumeService eventoVolumeService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private AcaoIntegracaoEventosService acaoIntegracaoEventosService;
    private RecursoMensagemService recursoMensagemService;
	
	public List<Map<String, Object>> findGridViagemChegada(Long idFilial){
		return getSaidaChegadaDAO().findGridViagemChegada(idFilial);
	}

	public List<Map<String, Object>> findControleCargaChegada(Long idFilial){
		List<Map<String, Object>> result = getSaidaChegadaDAO().findControleCargaChegada(idFilial);
		insereTipoColetaEntrega(result, "coletaEntrega");
		return result;
	}

	public List<Map<String, Object>> findOrdemSaidaChegada(Long idFilial){
		List<Map<String, Object>> result = getSaidaChegadaDAO().findOrdemSaidaChegada(idFilial);
		insereTipoColetaEntrega(result, "ordemDeSaida");
		return result;
	}
	
	private void insereTipoColetaEntrega(List<Map<String, Object>> list, String chave){
		String texto = configuracoesFacade.getMensagem(chave);
		if (list != null){
			for(Map<String, Object> map : list) {
				map.put("tipo", texto);
			}
		}
	}
	
	public boolean validaOperacaoFedex(TypedFlatMap parameters) {
		String controle = parameters.getString("idControleTemp");
		String[] tokens= controle.split("\\|");

		Long idControle = Long.valueOf(tokens[0]);

		ControleTrecho ct = controleTrechoService.findControleTrechoWithControleCargaById(idControle);
		
		Filial filialDestino = ct.getFilialByIdFilialDestino();
		
		parameters.put("idFilial", filialDestino.getIdFilial());
		
		return validarOperacaoFedex(parameters);
	}

	/**
	 * Verifica se o controle de carga está em trânsito ou em viagem.
     *
	 * @param parameters
	 * @return
	 */
	public boolean validaTransito(TypedFlatMap parameters) {
		String controle = parameters.getString("idControleTemp");
		String[] tokens= controle.split("\\|");

		Long idControle = Long.valueOf(tokens[0]);

		ControleTrecho ct = controleTrechoService.findControleTrechoWithControleCargaById(idControle);
		
		ControleCarga cc = ct.getControleCarga();
		
		if (!cc.getTpStatusControleCarga().getValue().equals("EV") && !cc.getTpStatusControleCarga().getValue().equals("TC")) {
			throw new BusinessException("LMS-06038");
		}
	
		return true;
	}
	
	/**
	 * LMS-3342: não deve permitir uma entrega com ocorrência de bloqueio em
	 * aberto e nem com data inferior a data de bloqueio e liberação.
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean validarBloqueio(TypedFlatMap parameters) {
		String[] tokens = parameters.getString("idControleTemp").split("\\|");
		
		ControleCarga controleCarga = null;
		Long idControleCarga = null;
		String tpControleCarga = null;
		
		if("V".equals(String.valueOf(tokens[1]))){
			ControleTrecho controleTrecho = controleTrechoService.findControleTrechoWithControleCargaById(Long.valueOf(tokens[0]));
			controleCarga = controleTrecho.getControleCarga();
		} else if("C".equals(String.valueOf(tokens[1]))){
			controleCarga = controleCargaService.findById(Long.valueOf(tokens[0]));
		}
		
		if(controleCarga != null){
			idControleCarga = controleCarga.getIdControleCarga();
			tpControleCarga = controleCarga.getTpControleCarga().getValue();
		}
			
		if ("C".equals(tpControleCarga)) {
			

			/*
			 * Se o controle de carga é de coleta/entrega
			 * (CONTROLE_CARGA.TP_CONTROLE_CARGA = 'C') e se existe algum
			 * documento de serviço com alguma ocorrência de bloqueio
			 * (DOCTO_SERVICO >> OCORRENCIA_DOCTO_SERVICO), se existir, verificar se: 
			 * - a ocorrência de liberação é nula e o documento de serviço não possui qualquer ocorrência de entrega
			 * (OCORRENCIA_DOCTO_SERVICO.dh_liberacao IS NULL AND DOCTO_SERVICO.id_docto_servico = MANIFESTO_ENTREGA_DOCUMENTO.id_docto_servico >> MANIFESTO_ENTREGA_DOCUMENTO.id_ocorrencia_entrega IS NULL), 
			 * então exibir a mensagem: LMS-06041.
			 * 
			 * - se a maior ocorrência de liberação do documento de serviço
			 * (OCORRENCIA_DOCTO_SERVICO.dh_liberacao) mais dois minutos é maior
			 * ou igual a data/hora/minuto/segundo que a atual; Se algum dos
			 * testes anteriores for verdadeiro, apresentar a mensagem: LMS-06042.
			 */
			List<OcorrenciaDoctoServico> ocorrencias = ocorrenciaDoctoServicoService.findOcorrenciaByControleCargaManifestoEntrega(idControleCarga);

			if (ocorrencias != null && !ocorrencias.isEmpty()) {
				for (OcorrenciaDoctoServico ocorrenciaDoctoServico : ocorrencias) {
					if(ocorrenciaDoctoServico.getDhLiberacao() == null && validateDocumentoSemOcorrenciaEntrega(ocorrenciaDoctoServico.getDoctoServico())){
						throw new BusinessException("LMS-06041");	
					}
				}
				
				/*
				 * Se a maior ocorrência de liberação do documento de serviço
				 * (OCORRENCIA_DOCTO_SERVICO.dh_liberacao) mais dois dois  minutos ou a maior ocorrência de bloqueio (OCORRENCIA_DOCTO_SERVICO.dh_bloqueio) mais dois minutos é
				 * maior ou igual a data/hora/minuto/segundo que a atual, então exibir a mensagem: LMS-06042.
				 */
				Boolean lancarExcecaoDataLiberacao = null;
				Collections.sort(ocorrencias, new ReverseComparator(new BeanComparator("dhLiberacao", new NullComparator(false))));
				OcorrenciaDoctoServico ocorrenciaMaiorDataLiberacao = ocorrencias.get(0);
				DateTime maiorDataLiberacao = ocorrenciaMaiorDataLiberacao.getDhLiberacao();
				if (maiorDataLiberacao != null) {
					maiorDataLiberacao = maiorDataLiberacao.plusMinutes(2);
					lancarExcecaoDataLiberacao = maiorDataLiberacao.compareTo(JTDateTimeUtils.getDataHoraAtual()) >= 0;
				} else {
					lancarExcecaoDataLiberacao = Boolean.FALSE;
				}
				
				Collections.sort(ocorrencias, new BeanComparator("dhBloqueio", new ReverseComparator(new ComparableComparator())));
				OcorrenciaDoctoServico ocorrenciaMaiorDataBloqueio = ocorrencias.get(0);
				DateTime maiorDataBloqueio = ocorrenciaMaiorDataBloqueio.getDhBloqueio();
				maiorDataBloqueio = maiorDataBloqueio.plusMinutes(2);
				Boolean lancarExcecaoDataBloqueio = maiorDataBloqueio.compareTo(JTDateTimeUtils.getDataHoraAtual()) >= 0;

				if (lancarExcecaoDataLiberacao || lancarExcecaoDataBloqueio) {
					throw new BusinessException("LMS-06042");
				}
			}


		} else if ("V".equals(tpControleCarga)) {
			
			/*-	Se o controle de carga for do tipo viagem (CONTROLE_CARGA = ‘V’)
			 * 	- verificar se existe o parâmetro geral (PARAMETRO_GERAL.NM_PARAMETRO_GERAL) PAR_VAL_FILIAL_CHEGADA, se existir:
			 * 		- verificar se o conteúdo do parâmetro (PARAMETRO_GERAL.DS_CONTEUDO) é igual a “S”, se for:
			 * 			- verificar se existe algum registro em FILIAL_ROTA_CC vinculado ao controle de carga (ID_CONTROLE_CARGA), se existir:
			 * 					- com base na lista de FILIAL_ROTA_CC filtradas pelo controle de carga verificar qual é a ordem da filial logada 
			 * 						no fluxo(FILIAL_ROTA_CC.ID_FILIAL >> FILIAL_ROTA_CC.NR_ORDEM)
			 * 					- verificar se todas as filiais da lista de FILIAL_ROTA_CC cujo NR_ORDEM for menor que a filial logada possuem evento de saída 
			 * 						na portaria (CONTROLE_CARGA.ID_CONTROLE_CARGA >> EVENTO_CONTROLE_CARGA.TP_EVENTO_CONTROLE_CARGA = ‘SP’ e EVENTO_CONTROLE_CARGA.ID_FILIAL), 
			 * 						se alguma filial não possuir o evento de saída na portaria, apresentar a mensagem: LMS-06043. 
			 * 						Sendo que o valor "{0}" deve ser substituído pela sigla (FILIAL_ROTA_CC.ID_FILIAL >> FILIAL.SG_FILIAL) da filial 
			 * 						com menor NR_ORDEM que não possui evento de saída na portaria.
			 * 
			 */
			String parametro = null;
			try {
				parametro = (String) configuracoesFacade.getValorParametro("PAR_VAL_FILIAL_CHEGADA");
			} catch (BusinessException e) {
				// silencia pois o parâmetro pode não existir.
			}

			if ("S".equals(parametro)) {
				List<FilialRotaCc> listaFilialRotaCc = filialRotaCcService.findByControleCarga(idControleCarga);
				if (listaFilialRotaCc != null && !listaFilialRotaCc.isEmpty()) {
					Byte nrOrdemFilialLogada = null;

					/*
					 * Localiza a nrOrdem da filial logada e remove a mesma da lista para evitar teste no próximo for.
					 */
					for (Iterator<FilialRotaCc> iterator = listaFilialRotaCc.iterator(); iterator.hasNext();) {
						FilialRotaCc filialRotaCc = iterator.next();
						if (filialRotaCc.getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
							nrOrdemFilialLogada = filialRotaCc.getNrOrdem();
							iterator.remove();
							break;
						}
					}

					if (nrOrdemFilialLogada != null) {
						List<FilialRotaCc> listaFiliaRotaCcSemEventoSaida = new ArrayList<FilialRotaCc>();
						for (FilialRotaCc filialRotaCc : listaFilialRotaCc) {
							if (filialRotaCc.getNrOrdem().compareTo(nrOrdemFilialLogada) < 0) {
								List<EventoControleCarga> eventos = eventoControleCargaService
										.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(filialRotaCc.getFilial().getIdFilial(),
												idControleCarga, "SP");
								if (eventos == null || eventos.isEmpty()) {
									listaFiliaRotaCcSemEventoSaida.add(filialRotaCc);
								}
							}
						}

						if (!listaFiliaRotaCcSemEventoSaida.isEmpty()) {
							Collections.sort(listaFiliaRotaCcSemEventoSaida, new BeanComparator("nrOrdem"));
							throw new BusinessException("LMS-06043", new String[] { listaFiliaRotaCcSemEventoSaida.get(0).getFilial().getSgFilial() });
						}
					}
				}
			}
		}

		return true;
	}
	
	public boolean validarOperacaoFedex(TypedFlatMap parameters) {
		
		if (null == parameters.get("integracaoFedex")) {

			String parametroFilial = null;
			try {
				parametroFilial = (String) configuracoesFacade.getValorParametro((Long) parameters.get("idFilial") ,"BLOQ_OPE_REALI_FEDEX");
			} catch (BusinessException e) {
				// silencia pois o parâmetro pode não existir.
			}

			if ("S".equals(parametroFilial)) {
				throw new BusinessException("LMS-06049");
			}
		
		}
		return true;
	}
	
	/**
	 * Valida se o atraso na chegada do veiculo esta dentro do tempo maximo permitido 
     *
	 * @param parameters
	 * @return
	 */
    public Map<String,Object> validateTempoEsperaConhecimento(TypedFlatMap parameters) {

        Map<String,Object> resultMap = new HashMap<String,Object>();

		String controle = parameters.getString("idControleTemp");
		String[] tokens= controle.split("\\|");

		Long idControle = Long.valueOf(tokens[0]);
		String tpSaida = tokens[1];

		BigDecimal qtdHorasEspera = (BigDecimal) configuracoesFacade.getValorParametro("TEMPO_MAXIMO_ENTRADA_MEIO_TRANSPORTE");

		DateTime cal = JTDateTimeUtils.getDataHoraAtual();
		cal.minusMinutes(qtdHorasEspera.intValue());

        if ("V".equals(tpSaida)) {
			ControleTrecho ct = controleTrechoService.findById(idControle);
			ControleCarga cc = ct.getControleCarga();
			
			if(ct.getManifestos() != null){
				for (Object manifesto : ct.getManifestos()) {
					Manifesto m = (Manifesto) manifesto;
					
					if( m != null && m.getTpStatusManifesto() != null && !"CA".equals(m.getTpStatusManifesto().getValue()) ){
						ManifestoViagemNacional manifestoViagemNacional = manifestoViagemNacionalService.findById(m.getIdManifesto());
					if(manifestoViagemNacional != null){
						Integer nrConhecimentos = manifestoNacionalCtoService.findConhecimentos(manifestoViagemNacional.getIdManifestoViagemNacional()).size();
						if(manifestoViagemNacional.getNrCto().compareTo(nrConhecimentos)!= 0){
							throw new BusinessException("LMS-06034");
						}
						
					}
				}
			}
			}
			
			if (ct.getDhPrevisaoChegada() != null && ct.getDhPrevisaoChegada().compareTo(cal) > 0){
                String mensagemErro = recursoMensagemService.findByChave("LMS-06016");
                resultMap.put("validado",false);
                resultMap.put("mensagemConfirmacao",mensagemErro.replace("{0}",qtdHorasEspera.toString()));
			}
            else{
                resultMap.put("validado",true);
            }
			
		}

        return resultMap;
	}

	private Boolean validateDocumentoSemOcorrenciaEntrega(DoctoServico doctoServico){
		List<ManifestoEntregaDocumento> manifestoSemOcorrenciaEntrega = manifestoEntregaDocumentoService.findManifestoSemOcorrenciaEntregaByIdDoctoServico(doctoServico.getIdDoctoServico(), new String[]{"TC", "TE"});
		return !manifestoSemOcorrenciaEntrega.isEmpty();
	}
	
	public List<Map<String, Object>> findGridChegadaSaida(Long idMeioTransporteTerceiro){
		List<Map<String, Object>> result = getSaidaChegadaDAO().findGridChegadaSaida(idMeioTransporteTerceiro);
		for(Map<String, Object> row : result) {
			String cpf = FormatUtils.completaDados(row.get("nrCpf"), "0", 11, 0, true);
			row.put("nrCpf", FormatUtils.formatCPF(cpf));
		}
		return result;
	}

	private SaidaChegadaDAO getSaidaChegadaDAO() {
		return saidaChegadaDAO;
	}

	public void setSaidaChegadaDAO(SaidaChegadaDAO saidaChegadaDAO) {
		this.saidaChegadaDAO = saidaChegadaDAO;
	}

	/**
	 * @param controleTrechoService The controleTrechoService to set.
	 */
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}


	/**
	 * @param controleCargaService The controleCargaService to set.
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public BoxService getBoxService() {
		return boxService;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
	
	/**
	 * @param manifestoViagemNacionalService the manifestoViagemNacionalService to set
	 */
	public void setManifestoViagemNacionalService(
			ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
}

	/**
	 * @param manifestoNacionalCtoService the manifestoNacionalCtoService to set
	 */
	public void setManifestoNacionalCtoService(
			ManifestoNacionalCtoService manifestoNacionalCtoService) {
		this.manifestoNacionalCtoService = manifestoNacionalCtoService;
	}

	/**
	 * @param volumeNotaFiscalService the volumeNotaFiscalService to set
	 */
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	/**
	 * @return the volumeNotaFiscalService
	 */
	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	/**
	 * @param eventoVolumeService the eventoVolumeService to set
	 */
	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}
	
	/**
	 * @return the eventoVolumeService
	 */
	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
}

	/**
	 * @param dispositivoUnitizacaoService the dispositivoUnitizacaoService to set
	 */
	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	/**
	 * @return the dispositivoUnitizacaoService
	 */
	public DispositivoUnitizacaoService getDispositivoUnitizacaoService() {
		return dispositivoUnitizacaoService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
		this.filialRotaCcService = filialRotaCcService;
	}
	
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	
	/**
	 * @param eventoDispositivoUnitizacaoService the eventoDispositivoUnitizacaoService to set
	 */
	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
		return manifestoEntregaDocumentoService;
	}

	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	
	/**
	 * @return the eventoDispositivoUnitizacaoService
	 */
	public EventoDispositivoUnitizacaoService getEventoDispositivoUnitizacaoService() {
		return eventoDispositivoUnitizacaoService;
	}

	public AcaoIntegracaoEventosService getAcaoIntegracaoEventosService() {
		return acaoIntegracaoEventosService;
	}

	public void setAcaoIntegracaoEventosService(AcaoIntegracaoEventosService acaoIntegracaoEventosService) {
		this.acaoIntegracaoEventosService = acaoIntegracaoEventosService;
	}

    public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
        this.recursoMensagemService = recursoMensagemService;
    }
}
