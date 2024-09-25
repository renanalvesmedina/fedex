package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.util.ControladorEstadoCalculoDiasUteisBloqueioAgendamento;
import com.mercurio.lms.entrega.util.EstadoAgendamento;
import com.mercurio.lms.entrega.util.EstadoBloqueio;
import com.mercurio.lms.entrega.util.EstadoDia;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Feriado;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.service.ConsultarMCDService;
import com.mercurio.lms.municipios.model.service.FeriadoService;
import com.mercurio.lms.municipios.model.service.OperacaoServicoLocalizaService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe responsável pelo calculo de dias úteis, bloqueio e agendamento de um documento de serviço
 * 
 * @author rtavares
 * @spring.bean id="lms.entrega.calcularDiasUteisBloqueioAgendamentoService"
 */
public class CalcularDiasUteisBloqueioAgendamentoService {
	
	Long ID_EVENTO = Long.valueOf("348");
	Long ID_MOTIVO_AGENDAMENTO_CLIENTE = Long.valueOf("21");

	private Logger log = LogManager.getLogger(this.getClass());
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ConhecimentoService conhecimentoService;
	private FeriadoService feriadoService;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private ConsultarMCDService consultarMCDService;	
	private DoctoServicoService doctoServicoService;
	private AgendamentoEntregaService agendamentoEntregaService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private ControleCargaService controleCargaService;
	
	/**
	 * Método para rotina - Calcula os dias úteis bloqueio e agendamento de um documento de serviço
	 * Esse método é utilizado pela tela swt Recalcula dias do documento de serviço
	 *
	 * 
	 * 
	 * @param idDocumentoServico Long com o id do documento que se quer fazer os calculos
	 * @param salva Boolean indicando se deve atualizar o documento de serviço. Caso seja TRUE ou null o documento é atualizado caso seja FALSE o documento não é atualizado
	 * @return Map<String, Object> mapa que contem o documento de serviço com os valores de dias e data projetados calculados e a lista de ControladorEstadoCalculoDiasUteisBloqueioAgendamento
	 */
	public Map<String, Object> executeCalcularDiasUteisBloqueioAgendamento(Long idDocumentoServico, Boolean salva) {
		if(idDocumentoServico == null) {
			throw new IllegalArgumentException("O id do documento de Serviço passado como parametro não pode ser nullo");
		}
		Map<String, Object> retorno = new HashMap<String, Object>();
		DoctoServico documentoServico = doctoServicoService.findById(idDocumentoServico);		
		List<ControladorEstadoCalculoDiasUteisBloqueioAgendamento> listaControlador = this.executeCalcularDiasUteisBloqueioAgendamento(documentoServico, salva);
		
		//No retorno é colocado o documento de serviço e a lista de ControladorEstadoCalculoDiasUteisBloqueioAgendamento para ser utilizado na tela swt
		retorno.put("doctoServico", documentoServico);
		retorno.put("listaControlador", listaControlador);
		
		return retorno;
	}	
	
	/**
	 * Método para rotina - Calcula os dias úteis bloqueio e agendamento de um documento de serviço
	 * 	Esse método recebe um documento de serviço, e a partir desse objeto passado faz os cálculos de dias uteis, bloqueio e agendamento.
	 * 
	 * @param documentoServico  documento que se quer fazer os calculos
	 * @param salva Boolean indicando se deve atualizar o documento de serviço. Caso seja TRUE ou null o documento é atualizado caso seja FALSE o documento não é atualizado
	 * @return
	 */
	public List<ControladorEstadoCalculoDiasUteisBloqueioAgendamento> executeCalcularDiasUteisBloqueioAgendamento(DoctoServico documentoServico, Boolean salva) {
		Conhecimento conhecimento = conhecimentoService.findById(documentoServico.getIdDoctoServico());
		
		if(conhecimento != null && !conhecimento.getTpSituacaoConhecimento().getValue().equals("E")){
			throw new BusinessException("LMS-09141");
		}
		
		if(documentoServico == null) {
			throw new IllegalArgumentException("O documento de Serviço passado como parametro não pode ser nullo");
		}
		
		List<ControladorEstadoCalculoDiasUteisBloqueioAgendamento> retorno = new ArrayList<ControladorEstadoCalculoDiasUteisBloqueioAgendamento>();

		// item 2 - Inicialização dos objetos
		DateTime dataOcorrencia = this.getDataEntregaDocumentoServico(documentoServico);
		YearMonthDay dataFinal = dataOcorrencia != null ? dataOcorrencia.toYearMonthDay() : new DateTime().toYearMonthDay();
		ControladorEstadoCalculoDiasUteisBloqueioAgendamento controladorEstado = new ControladorEstadoCalculoDiasUteisBloqueioAgendamento();
		Long idMunicipioEntrega = conhecimentoService.findById(documentoServico.getIdDoctoServico()).getMunicipioByIdMunicipioEntrega().getIdMunicipio();

		controladorEstado.setDia(JTDateTimeUtils.setHour(JTDateTimeUtils.getInicioDoDia(documentoServico.getDhEmissao()), 0));
		controladorEstado.setQuantidadeDiasPrevisto(documentoServico.getNrDiasPrevEntrega());
		controladorEstado.setNrDiasRealEntrega(Short.valueOf("-1"));

		documentoServico.setNrDiasRealEntrega(Short.valueOf("-1"));
		documentoServico.setNrDiasBloqueio(Short.valueOf("0"));
		documentoServico.setNrDiasAgendamento(Short.valueOf("0"));
		documentoServico.setNrDiasTentativasEntregas(Short.valueOf("0"));
		
		// mapa que armaneza o estado do dia sendo iterado no calculo e as
		// listas com todos os feriados vigentes respectivamente da cidade da
		// filial de destino e da cidade de entrega.
		Map<String, Object> mapaEstadoDiaEFeriados = new HashMap<String, Object>();

		//Mapa que armazena informações dos agendamentos de entrega relacionados ao docto de serviço do calculo com informações para verificar o agendamento. Feito dessa forma devido a problemas de timezone com as datas
		List<Map<String,Object>> mapaAgendamentoEntregas = agendamentoEntregaService.findAgendamentoEntregaByIdDoctoServico(documentoServico.getIdDoctoServico());
		
		//Map que armazena os dados das ocorrencias de bloqueio relacionadas ao documento feito dessa forma devido a problemas de timezone com as datas
		List<Map<String,Object>> mapaOcorrenciaBloqueio = ocorrenciaDoctoServicoService.findListMapOcorrenciaByIdDoctoServico(documentoServico.getIdDoctoServico());
		
		// item 3
		// Os dias são atualizados dentro do método calcularDiasAgendamentoBloqueioRealEntregaEdiasPrevisto
		while(JTDateTimeUtils.comparaData(controladorEstado.getDia().toYearMonthDay(), dataFinal) <= 0
					|| (dataOcorrencia == null && controladorEstado.getQuantidadeDiasPrevisto() > 0)) {

			if( JTDateTimeUtils.comparaData(controladorEstado.getDia().toYearMonthDay(), JTDateTimeUtils.getDataAtual() ) > 0 ){
				break;
			}
			
			calcularDiasAgendamentoBloqueioRealEntregaEdiasPrevisto(controladorEstado, documentoServico, idMunicipioEntrega, dataOcorrencia, retorno, 
					mapaEstadoDiaEFeriados, mapaAgendamentoEntregas, mapaOcorrenciaBloqueio);
		}		

		//item 4		
		documentoServico.setNrDiasTentativasEntregas(this.calculaDiasDeDescontoPorTentativaDeEntrega(documentoServico, idMunicipioEntrega));
		
		//item 5
		documentoServico.setDtProjetadaEntrega(controladorEstado.getDia().toYearMonthDay());
		if(documentoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA)) {
			documentoServico.setNrDiasRealEntrega((short)(documentoServico.getNrDiasRealEntrega() - documentoServico.getNrDiasTentativasEntregas()));
			if(documentoServico.getNrDiasRealEntrega() < 1) {
				documentoServico.setNrDiasRealEntrega(Short.valueOf("1"));
			} 
		} else {
			documentoServico.setNrDiasRealEntrega(null);
		}
		
		// item 6
		if(salva == null || salva) {
			doctoServicoService.store(documentoServico);
		} else{
			doctoServicoService.evict(documentoServico);
		}
		
		return retorno;
	}
	
	/**
	 * Método utilizado na iteração do dia de emissão ou da quantidade de dias previsto.
	 *  Responsável por alterar o objeto de controle do estado do calculo e adicionar o clone do mesmo na lista que será utilizada
	 *  como retorno 
	 * 
	 * @param controladorEstado objeto que armazena o estado das variaveis necessárias para o calculo
	 * @param documentoServico Documento de serviço
	 * @param idMunicipioEntrega id do municipio de entrega
	 * @param dataOcorrencia data de ocorrencia 
	 * @param listaControladorEstado lista ao qual será adicionado o clone do controlador de estado
	 */
	private void calcularDiasAgendamentoBloqueioRealEntregaEdiasPrevisto(ControladorEstadoCalculoDiasUteisBloqueioAgendamento controladorEstado,
			DoctoServico documentoServico, Long idMunicipioEntrega, DateTime dataOcorrencia, List<ControladorEstadoCalculoDiasUteisBloqueioAgendamento> listaControladorEstado, 
			Map<String, Object> mapaStatusDiaEFeriados, List<Map<String,Object>> mapaAgendamentoEntregas, List<Map<String,Object>> mapaOcorrenciaBloqueio) {

		mapaStatusDiaEFeriados.remove("estadoDia");		
		Long idMunicipioFilialDestino = documentoServico.getFilialByIdFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
		
		// Verificar qual o estado do dia
		mapaStatusDiaEFeriados = getMapaStatusDiaEFeriados(controladorEstado, idMunicipioEntrega, dataOcorrencia, idMunicipioFilialDestino, mapaStatusDiaEFeriados);
		controladorEstado.setEstadoDia((EstadoDia) mapaStatusDiaEFeriados.get("estadoDia"));

		//verificar se é dia de bloqueio
		EstadoBloqueio estadoBloqueio = this.verificaDiaBloqueio(controladorEstado.getDia(), mapaOcorrenciaBloqueio);
		controladorEstado.setEstadoBloqueio(estadoBloqueio);
		
		if(controladorEstado.getEstadoBloqueio() != EstadoBloqueio.LIBERACAO_BLOQUEIO 
				&& (controladorEstado.getEstadoDia() != EstadoDia.DIA_UTIL && controladorEstado.getEstadoDia() != EstadoDia.DIA_PROJETADO)) {
			controladorEstado.setEstadoBloqueio(null);
		}
			
		// Verificar se é dia de agendamento
		EstadoAgendamento estadoAgendamento = this.verificaDiaAgendamento(controladorEstado.getDia(), controladorEstado, mapaAgendamentoEntregas);
		
		if(controladorEstado.getEstadoBloqueio() != null) {
			if( estadoAgendamento == EstadoAgendamento.LIBERACAO_AGENDAMENTO ){
				controladorEstado.setEstadoAgendamento( null );
			}
		}

		if(estadoAgendamento != null ) {
			controladorEstado.setEstadoAgendamento(estadoAgendamento);
			if(controladorEstado.getEstadoAgendamento() != EstadoAgendamento.LIBERACAO_AGENDAMENTO
					&& (controladorEstado.getEstadoDia() != EstadoDia.DIA_UTIL && controladorEstado.getEstadoDia() != EstadoDia.DIA_PROJETADO)){
				controladorEstado.setEstadoAgendamento(null);
			}
		} else if(controladorEstado.getEstadoAgendamento() != EstadoAgendamento.AGENDAMENTO_CLIENTE) {
			controladorEstado.setEstadoAgendamento(null);
		}
		
//		LMSA-2762
		//verifica se a data de saida é menor ou igual a data passada por parâmetro
		Boolean saidaPortariaDiaPosterior = this.verificaSaidaPortariaDiaPosterior(controladorEstado.getDia(), documentoServico.getIdDoctoServico());
		if(saidaPortariaDiaPosterior){
			documentoServico.setNrDiasRealEntrega((short)(documentoServico.getNrDiasRealEntrega() - 1));
		}
		
		//Conta a quantidade de dias de agendamento
		if(controladorEstado.getEstadoBloqueio() == null && (controladorEstado.getEstadoAgendamento() != null 
				&& controladorEstado.getEstadoDia() == EstadoDia.DIA_UTIL || controladorEstado.getEstadoAgendamento() == EstadoAgendamento.LIBERACAO_AGENDAMENTO)) {			
			documentoServico.setNrDiasAgendamento((short)(documentoServico.getNrDiasAgendamento() + 1));
			
			if(controladorEstado.getEstadoAgendamento() == EstadoAgendamento.LIBERACAO_AGENDAMENTO && controladorEstado.getEstadoDia() != EstadoDia.DIA_UTIL) {
				documentoServico.setNrDiasRealEntrega((short)(documentoServico.getNrDiasRealEntrega() - 1));
			}
		}
		
		//Conta a quantidade de dias de bloqueio
		if((controladorEstado.getEstadoBloqueio() != null && controladorEstado.getEstadoDia() == EstadoDia.DIA_UTIL)
				|| controladorEstado.getEstadoBloqueio() == EstadoBloqueio.LIBERACAO_BLOQUEIO) {			
			documentoServico.setNrDiasBloqueio((short)(documentoServico.getNrDiasBloqueio() + 1));
			if(controladorEstado.getEstadoBloqueio() == EstadoBloqueio.LIBERACAO_BLOQUEIO && controladorEstado.getEstadoDia() != EstadoDia.DIA_UTIL){
				documentoServico.setNrDiasRealEntrega((short)(documentoServico.getNrDiasRealEntrega() - 1));
			}
		}
		
		//Conta quantidade de dias úteis
		if(controladorEstado.getEstadoBloqueio() == null && controladorEstado.getEstadoAgendamento() == null
				&& controladorEstado.getEstadoDia() == EstadoDia.DIA_UTIL) {
			documentoServico.setNrDiasRealEntrega((short)(documentoServico.getNrDiasRealEntrega() + 1));
			controladorEstado.setNrDiasRealEntrega(documentoServico.getNrDiasRealEntrega());
		}
		
		//Conta a quantidade de dias previstos
		if(controladorEstado.getEstadoBloqueio() == null && controladorEstado.getEstadoAgendamento() == null
				&& (controladorEstado.getEstadoDia() == EstadoDia.DIA_UTIL || controladorEstado.getEstadoDia() == EstadoDia.DIA_PROJETADO)) {
			controladorEstado.setQuantidadeDiasPrevisto((short)(controladorEstado.getQuantidadeDiasPrevisto() - 1));
		}
			
		
		if(dataOcorrencia != null && JTDateTimeUtils.comparaData(dataOcorrencia.toYearMonthDay(), controladorEstado.getDia().toYearMonthDay()) == 0 && controladorEstado.getEstadoDia() != EstadoDia.DIA_UTIL) {			
			documentoServico.setNrDiasRealEntrega((short)(documentoServico.getNrDiasRealEntrega() + 1));
			controladorEstado.setNrDiasRealEntrega(documentoServico.getNrDiasRealEntrega());
		}
		
		//gera o clone e adiciona na lista de retorno
		try {
			ControladorEstadoCalculoDiasUteisBloqueioAgendamento clone = (ControladorEstadoCalculoDiasUteisBloqueioAgendamento) BeanUtils.cloneBean(controladorEstado);
			if(controladorEstado.getEstadoDia() != EstadoDia.DIA_UTIL){
				if(controladorEstado.getEstadoAgendamento() != EstadoAgendamento.LIBERACAO_AGENDAMENTO){
					clone.setEstadoAgendamento(null);
				}
				if(controladorEstado.getEstadoBloqueio() != EstadoBloqueio.LIBERACAO_BLOQUEIO) {
					clone.setEstadoBloqueio(null);
				}
			}
			listaControladorEstado.add(clone);
		} catch (Exception e) {				
			log.error(e);
		}		
		//Itera o dia caso seja diferente de null. Deve se testar pois este método é utilizado tanto quando se tem o Dia ou não
		if(controladorEstado.getDia() != null) {
			controladorEstado.setDia(controladorEstado.getDia().plus(Period.days(1)));
		}
	}

	/**
	 * Método que verifica o dia do controlador e adiciona ao map mapaEstadoDiaEFeriados que serve
	 * como controlador e armazenador das informações de estado dia e das listas de feriados que são armazenadas
	 * por questões de desempenho para evitar diversas requisições ao banco para trazer os feriados é feito uma unica vez a chamada
	 * buscando todos vigentes e armazenado no map. 
	 * 
	 * @param controladorEstado
	 * @param idMunicipioEntrega
	 * @param dataOcorrencia
	 * @param idMunicipioFilialDestino
	 * @param dtFeriadosMunicipioFilialDestino
	 * @param dtFeriadosMunicipioEntrega
	 */
	private Map<String, Object> getMapaStatusDiaEFeriados(ControladorEstadoCalculoDiasUteisBloqueioAgendamento controladorEstado, Long idMunicipioEntrega, 
			DateTime dataOcorrencia, Long idMunicipioFilialDestino, Map<String, Object> mapaEstadoDiaEFeriados) {

		List<Feriado> dtFeriadosMunicipioFilialDestino = null;
		List<Feriado> dtFeriadosMunicipioEntrega = null;
		
		//verifica se é fim de semana
		if(controladorEstado.getDia().getDayOfWeek() == DateTimeConstants.SATURDAY ||
				controladorEstado.getDia().getDayOfWeek() == DateTimeConstants.SUNDAY) {
			mapaEstadoDiaEFeriados.put("estadoDia",   EstadoDia.FIM_DE_SEMANA);
			return mapaEstadoDiaEFeriados;
		}

		// se o estado ainda é nulo verificar se é feriado
		if(!mapaEstadoDiaEFeriados.containsKey("estadoDia")) {
			if(!mapaEstadoDiaEFeriados.containsKey("dtFeriadosMunicipioFilialDestino")) {
				dtFeriadosMunicipioFilialDestino = feriadoService.findAllFeriadosByIdMunicipio(idMunicipioFilialDestino);
				mapaEstadoDiaEFeriados.put("dtFeriadosMunicipioFilialDestino", dtFeriadosMunicipioFilialDestino);
			}
			
			if(verificaFeriado((List<Feriado>) mapaEstadoDiaEFeriados.get("dtFeriadosMunicipioFilialDestino"), controladorEstado.getDia().toYearMonthDay())){
				mapaEstadoDiaEFeriados.put("estadoDia", EstadoDia.FERIADO);
				return mapaEstadoDiaEFeriados;
			} else {
				if(!mapaEstadoDiaEFeriados.containsKey("dtFeriadosMunicipioEntrega")) {
					dtFeriadosMunicipioEntrega = feriadoService.findAllFeriadosByIdMunicipio(idMunicipioEntrega);
					mapaEstadoDiaEFeriados.put("dtFeriadosMunicipioEntrega", dtFeriadosMunicipioEntrega);
				}
				if(verificaFeriado((List<Feriado>) mapaEstadoDiaEFeriados.get("dtFeriadosMunicipioEntrega"), controladorEstado.getDia().toYearMonthDay())){
					mapaEstadoDiaEFeriados.put("estadoDia", EstadoDia.FERIADO);
					return mapaEstadoDiaEFeriados;
				}
			}						
		} 
		// se o estado do dia ainda está nulo verificar se é um dia projetado
		if(!mapaEstadoDiaEFeriados.containsKey("estadoDia") && dataOcorrencia == null && 
				JTDateTimeUtils.comparaData(controladorEstado.getDia(), JTDateTimeUtils.getInicioDoDia(JTDateTimeUtils.getDataHoraAtual())) > 0) {
			mapaEstadoDiaEFeriados.put("estadoDia",  EstadoDia.DIA_PROJETADO);
			return mapaEstadoDiaEFeriados;
		} else {
			mapaEstadoDiaEFeriados.put("estadoDia",  EstadoDia.DIA_UTIL);
			return mapaEstadoDiaEFeriados;
		}
	}

	/**
	 * Método que verifica se o dia passado como parametro é um feriado
	 * O método itera a lista de feriados e verifica para cada registro se o dia passado como parametro esta entre a vigencia inicial e final e
	 * dd/MM do dtFeriado é igual ao dd/MM do dia testado 
	 * @param feriados
	 * @param dia
	 * @return
	 */
	private boolean verificaFeriado(List<Feriado> feriados, YearMonthDay dia) {
		if(feriados != null) {
			for(Feriado feriado: feriados) {
				if(JTDateTimeUtils.comparaData(feriado.getDtVigenciaInicial(), dia) <= 0
						&& (feriado.getDtVigenciaFinal() == null || JTDateTimeUtils.comparaData(feriado.getDtVigenciaFinal(), dia) >= 0)
						&& feriado.getDtFeriado().toString("dd/MM").equals(dia.toString("dd/MM"))) {
					return true;
				}
			}
		}
			
		return false;
	}
	
	/**  
	 * Retorna o dia de entrega do serviço
	 *  (Rotina Retorna data de entrega do documento de serviço)
	 * 
	 * @param documentoServico
	 * @return
	 */
	public DateTime getDataEntregaDocumentoServico(DoctoServico documentoServico) {
		DateTime retorno = null;
		retorno = manifestoEntregaDocumentoService.findMenorDhOcorrenciaByIdDoctoServico(documentoServico.getIdDoctoServico());
		if(retorno == null) {
			retorno = eventoDocumentoServicoService.findMenorDhEventoByIdEventoEIdDoctoServico(ID_EVENTO, documentoServico.getIdDoctoServico());
		}		
		return retorno;
	}
	
	/**
	 * Identifica se o dia passado por parâmetro é um dia de bloqueio, e que tipo de dia de bloqueio é.
	 * 
	 * Tipo de bloqueio existente no dia
	 * NULL: Não é dia de bloqueio
	 * BLOQUEIO: Dia de bloqueio
	 * LIBERACAO_BLOQUEIO: Dia de liberação de bloqueio
	 * 
	 * @param dia
	 * @param mapaOcorrenciaBloqueio map com os dados da ocorrencia(id, dhbloqueio e dhliberacao)
	 * @return
	 */
	private EstadoBloqueio verificaDiaBloqueio(DateTime dia, List<Map<String,Object>> mapaOcorrenciaBloqueio) {
		if(dia == null)
			throw new IllegalArgumentException("O dia passado como parametro não pode ser nullo");
		
		YearMonthDay dhLiberacao = findDhLiberacaoOcorrenciaByDoctoServicoByDataEntreBloqueioELiberacao(dia, mapaOcorrenciaBloqueio);

		if(dhLiberacao != null) {
			if(JTDateTimeUtils.comparaData(dia.toYearMonthDay(), dhLiberacao) != 0) {
				return EstadoBloqueio.BLOQUEIO;
			} 
			if(JTDateTimeUtils.comparaData(dia.toYearMonthDay(), dhLiberacao) == 0){
				return EstadoBloqueio.LIBERACAO_BLOQUEIO;
			}
		}
		
		return null;
	}
	
	/**
	 * Verifica se existe uma OcorrenciaDoctoServico relacionado ao documento de serviço passado como parametro
	 * onde o dia passado como parametro esteja entre a data de bloqueio + 1 e a data de liberação
	 * @param idDoctoServico
	 * @param dia
	 * @return
	 */
	public YearMonthDay findDhLiberacaoOcorrenciaByDoctoServicoByDataEntreBloqueioELiberacao(DateTime dia, List<Map<String,Object>> mapaOcorrenciaBloqueio) {
		YearMonthDay dhLiberacao =null;
		
		if(mapaOcorrenciaBloqueio != null && !mapaOcorrenciaBloqueio.isEmpty()) {
			for(Map<String,Object> mapOcorrencia  : mapaOcorrenciaBloqueio){
				
				if(mapOcorrencia.get("dhBloqueio")!= null && mapOcorrencia.get("dhLiberacao") != null){
					//para fazer a conversão sem o timzone pega o millis, soma mais um dia devido a regra
					YearMonthDay dtBloqueio = ((YearMonthDay)mapOcorrencia.get("dhBloqueio")).plusDays(1);
					YearMonthDay dtLiberacao = (YearMonthDay)mapOcorrencia.get("dhLiberacao");
					if(betweenYearMonthDay(dia.toYearMonthDay(), dtBloqueio, dtLiberacao)) {
						dhLiberacao = (YearMonthDay)mapOcorrencia.get("dhLiberacao");
					}		
				}
				
			}
		}
		
		return dhLiberacao;
	}
	
	/**
	 * Identifica se o dia passado por parâmetro é um dia de agendamento, e que tipo de dia de agendamento é
	 * 
	 * Tipo de agendamento existente no dia
	 * NULL: Não é dia de agendamento
	 * AGENDAMENTO_TNT: Dia de agendamento por responsabilidade da TNT
	 * AGENDAMENTO_CLIENTE: Dia de agendamento por responsabilidade do cliente
	 * LIBERACAO_AGENDAMENTO: Dia de liberação de agendamento
	 * 
	 * @param dia
	 * @param controladorEstado
	 * @param mapaAgendamentoEntregas map com os dados do agendamento de entrega (id, dtAgendamento, dhContato, dhFechamento, tpSituacaoAgendamento e idMotivoAgendamento)
	 * @return
	 */
	public EstadoAgendamento verificaDiaAgendamento(DateTime dia, ControladorEstadoCalculoDiasUteisBloqueioAgendamento controladorEstado,
			List<Map<String,Object>> mapaAgendamentoEntregas) {
		
		if(dia == null)
			throw new IllegalArgumentException("O dia passado como parametro não pode ser nullo");		
		
		List<Map<String,Object>> agendamentosEntrega = 
				findAgendamentosEntregaByDoctoServicoByDataEntreContatoEDtFechamentoEVerificaAgendamentoCliente(dia, mapaAgendamentoEntregas);
		
		if(agendamentosEntrega != null && !agendamentosEntrega.isEmpty()) {
			// A prioridade de agendamentoEntrega são os reagendamentos por conta do cliente caso exista, caso contrário pega o primeiro da lista de agendamentos
			Map<String,Object> agendamentoEntrega;
			if(!verificaExistenciaAgendamentoCliente(agendamentosEntrega)) {
				agendamentoEntrega = agendamentosEntrega.get(0);
			} else {
				return EstadoAgendamento.AGENDAMENTO_CLIENTE;
			}

			if(JTDateTimeUtils.comparaData(dia.toYearMonthDay(), new YearMonthDay(agendamentoEntrega.get("dtAgendamento"))) == 0) {
				return EstadoAgendamento.LIBERACAO_AGENDAMENTO;
			} else{
				return EstadoAgendamento.AGENDAMENTO_TNT;
			}
		}
		
		return null;
	}
	
	/**
	 * Método que retorna os agendamentos de entrega relacionados ao documento de serviço
     * passado como parametro onde: <br>
     *  1 - a data passada como parâmetro esteja entre a data de contato + 1 e a maior data entre a data de agendamento e a data de fechamento<br> 
     *  2 - possua data de fechamento <br>
     *  3 -  situação agendamento esteja em 'R' ou 'F'
     *  
     *  Seta na variavel existAgendamentoCliente passada como parametro o valor true caso exista algum agendamento do cliente
	 * @param documentoServico
	 * @param dia
	 * @param existAgendamentoCliente
	 * @return
	 */
    @SuppressWarnings("unchecked")
	private List<Map<String,Object>> findAgendamentosEntregaByDoctoServicoByDataEntreContatoEDtFechamentoEVerificaAgendamentoCliente(DateTime dia, 
			List<Map<String,Object>> mapaAgendamentoEntregas){
    	
    	List<Map<String,Object>> agendamentosEntrega = new ArrayList<Map<String,Object>>();    	
		if(mapaAgendamentoEntregas != null && !mapaAgendamentoEntregas.isEmpty()) {
			
			for(Map<String,Object> mapAgendamentoEntrega : mapaAgendamentoEntregas) {
				if(mapAgendamentoEntrega.get("dhContato")  != null && mapAgendamentoEntrega.get("dhFechamento") != null) {
					//para fazer a conversão sem o timzone pega o millis, soma mais um dia devido a regra
					YearMonthDay dtContato = ((YearMonthDay)mapAgendamentoEntrega.get("dhContato")).plusDays(1);
					YearMonthDay dtAgendamento = (YearMonthDay)mapAgendamentoEntrega.get("dtAgendamento");
					DomainValue tpSituacaoAgendamento = (DomainValue) mapAgendamentoEntrega.get("tpSituacaoAgendamento");
					if((tpSituacaoAgendamento.getValue().equals("R") || tpSituacaoAgendamento.getValue().equals("F"))
							&& betweenYearMonthDay(dia.toYearMonthDay(), dtContato, dtAgendamento)) {
						agendamentosEntrega.add(mapAgendamentoEntrega);
						if (tpSituacaoAgendamento.getValue().equals("R")
								&& mapAgendamentoEntrega.get("idMotivoAgendamento").equals(ID_MOTIVO_AGENDAMENTO_CLIENTE)) {
							mapAgendamentoEntrega.put("agendamentoCliente", Boolean.TRUE);
						}
					}
				}
			}
		}
		
		return agendamentosEntrega;
    }
    
    private boolean verificaExistenciaAgendamentoCliente(List<Map<String,Object>> mapaAgendamentoEntregas) {
    	for(Map<String,Object> mapAgendamentoEntrega : mapaAgendamentoEntregas) {
    		if(mapAgendamentoEntrega.containsKey("agendamentoCliente")) {
    			return Boolean.TRUE;
    		}
    	}
    	return false;
    }
	
	/**
	 * Método referente a Rotina Calcula dias de desconto por tentativa de entrega
	 * Essa rotina calcula quantos dias devem ser descontados dos dias reais de entrega devido a tentativas de entregas por responsabilidade do cliente.
	 * 
	 * @param documentoServico
	 * @param idMunicipioEntrega
	 * @return
	 */
	public Short calculaDiasDeDescontoPorTentativaDeEntrega(DoctoServico documentoServico, Long idMunicipioEntrega) {
		List<ManifestoEntregaDocumento> manifestos = manifestoEntregaDocumentoService.findManifestoByIdDoctoServicoSemOcorrenciaEntregaENaoOcasionadoTnt(documentoServico.getIdDoctoServico());
		Long sumNrTempoEntrega = 0L;
		
		if(manifestos != null) {
			for(ManifestoEntregaDocumento manifestoEntregaDocumento : manifestos ) {
				//LMS-5273 - Das operações serviço localização retornadas no passo anterior, selecionar a que tenha o mesmo serviço do documento de serviço
				List<OperacaoServicoLocaliza> operacoes = operacaoServicoLocalizaService.findOperacaoServicoLocalizaByIdFilialDestinoEIdMunicipioEntregaDoctoServico(
						documentoServico.getFilialByIdFilialDestino().getIdFilial(), idMunicipioEntrega, manifestoEntregaDocumento.getDhOcorrencia(), documentoServico.getServico());
				
				//LMS-5273 - Se não existir nenhuma operação com o mesmo serviço, selecionar a operação que tenha o serviço nulo 
				if (operacoes == null || operacoes.isEmpty()) {
					operacoes = operacaoServicoLocalizaService.findOperacaoServicoLocalizaByIdFilialDestinoEIdMunicipioEntregaDoctoServico(
							documentoServico.getFilialByIdFilialDestino().getIdFilial(), idMunicipioEntrega, manifestoEntregaDocumento.getDhOcorrencia(), null);
				}
				
				if(operacoes != null) {
					for(OperacaoServicoLocaliza operacao : operacoes) {				
						Long tempoConvertidoDia = operacao.getNrTempoEntrega()/(24*60);
						Long diasAtendimento = consultarMCDService.verificaDiasAtendimento(operacao);
						if(tempoConvertidoDia + diasAtendimento < 1) {
							sumNrTempoEntrega+= 1;
						}
						sumNrTempoEntrega += tempoConvertidoDia + diasAtendimento; 
					}
				}
			}
		}
		
		return sumNrTempoEntrega.shortValue();
	}
	
	/**
	 * Retorna o AgendamentoEntrega que é do tipo reagendamento e a responsabilidade é do cliente caso exista
	 * caso contrário retorna null
	 * @param agendamentosEntrega
	 * @return
	 */
	private AgendamentoEntrega getReagendamentoResposabilidadeCliente(List<AgendamentoEntrega> agendamentosEntrega) {
		for(AgendamentoEntrega agendamentoEntrega : agendamentosEntrega){
			if (agendamentoEntrega.getTpSituacaoAgendamento().getValue().equals("R")
					&& agendamentoEntrega.getMotivoAgendamentoByIdMotivoReagendamento().getIdMotivoAgendamento().equals(ID_MOTIVO_AGENDAMENTO_CLIENTE)) {
				return agendamentoEntrega;
			}
		}
		return null;
	}
	
	/**
	 * Verifica se a data passada como data1 está entre a data2 e a data3 dos parametros de entrada.
	 * 
	 * @param data1
	 * @param data2
	 * @param data3
	 * @return
	 */
	private Boolean betweenYearMonthDay(YearMonthDay data1, YearMonthDay data2, YearMonthDay data3) {
		if(JTDateTimeUtils.comparaData(data1, data2) >= 0 && JTDateTimeUtils.comparaData(data1, data3) <= 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private Boolean verificaSaidaPortariaDiaPosterior(DateTime dia, Long idDoctoServico) {
		if(dia == null)
			throw new IllegalArgumentException("O dia passado como parametro não pode ser nullo");
		
		return this.controleCargaService.verificaSaidaPortariaDiaPosterior(dia, idDoctoServico) ;
	}
	
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setFeriadoService(FeriadoService feriadoService) {
		this.feriadoService = feriadoService;
	}

	public void setOperacaoServicoLocalizaService(
			OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}

	public void setConsultarMCDService(ConsultarMCDService consultarMCDService) {
		this.consultarMCDService = consultarMCDService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setAgendamentoEntregaService(AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

}
