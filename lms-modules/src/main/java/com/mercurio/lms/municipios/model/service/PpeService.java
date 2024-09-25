package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.ppeService"
 */
public class PpeService {
    
    public static final String NR_TEMPO_COLETA = "nrTempoColeta";
    public static final String NR_TEMPO_ENTREGA = "nrTempoEntrega";
    public static final String NR_TEMPO_TRANSFERENCIA = "nrTempoTransferencia";
    public static final String NR_PRAZO = "nrPrazo";
    
	private FilialService filialService;
	private MunicipioFilialService municipioFilialService;
	private MunicipioFilialCliOrigemService municipioFilialCliOrigemService;
	private MunicipioFilialIntervCepService municipioFilialIntervCepService;
	private MunicipioFilialSegmentoService municipioFilialSegmentoService;
	private MunicipioFilialUFOrigemService municipioFilialUFOrigemService;
	private MunicipioFilialFilOrigemService municipioFilialFilOrigemService;
	private AtendimentoClienteService atendimentoClienteService;
	private FluxoFilialService fluxoFilialService;
	private MunicipioService municipioService;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private SubstAtendimentoFilialService substAtendimentoFilialService;
	private RegionalFilialService regionalFilialService;

	/**
	 * Método criado para buscar a filial de coleta do munícipio.
	 * Retorna a filial responsável por efetuar a coleta do município.
	 * @param parameters
	 * @return Long
	 */
	public Long findFilialColetaMunicipio(Long idMunicipio, Long idServico, String cep){		 
		return findFilialAtendimentoMunicipio(
				idMunicipio,
				idServico,
				Boolean.TRUE,
				null,
				cep,
				null,
				null,
				null,
				null,
				null,
				null
		);
	}

	public String findFilialAtendimentoMunicipioTeste(TypedFlatMap parametros) {
		Long idMunicipio = null;
		if (!StringUtils.isBlank((String)parametros.get("municipio.idMunicipio")))
			idMunicipio = Long.valueOf((String)parametros.get("municipio.idMunicipio"));

		Long idServico =null;
		if (!StringUtils.isBlank((String)parametros.get("idServico")))
			idServico =Long.valueOf((String)parametros.get("idServico"));

		Boolean blIndicadorColeta=null;
		if (!StringUtils.isBlank((String)parametros.get("blIndicativoColeta")))
			blIndicadorColeta= Boolean.valueOf((String)parametros.get("blIndicativoColeta"));

		YearMonthDay dtConsulta = null;
		if (!StringUtils.isBlank((String)parametros.get("dtConsulta")))
			dtConsulta = (YearMonthDay)ReflectionUtils.toObject((String)parametros.get("dtConsulta"),YearMonthDay.class);

		String cep = null;
		if (!StringUtils.isBlank((String)parametros.get("cep")))
			cep = (String)parametros.get("cep");
		
		Long idCliente = null;
		if (!StringUtils.isBlank((String)parametros.get("cliente.idCliente")))
			idCliente = Long.valueOf((String)parametros.get("cliente.idCliente"));
		
		Long idClienteRemetente = null;
		if (!StringUtils.isBlank((String)parametros.get("remetente.idClienteRemetente")))
			idClienteRemetente = Long.valueOf((String)parametros.get("remetente.idClienteRemetente"));
		
		Long idSegmentoMercado = null;		
		if ( !StringUtils.isBlank((String)parametros.get("idSegmentoMercado")) )
			idSegmentoMercado =Long.valueOf((String)parametros.get("idSegmentoMercado"));
		
		Long idUnidadeFederativa = null;
		if (!StringUtils.isBlank((String)parametros.get("unidadeFederativa.idUnidadeFederativa")) )
			idUnidadeFederativa =Long.valueOf((String)parametros.get("unidadeFederativa.idUnidadeFederativa"));
		
		Long idFilial = null;
		if (!StringUtils.isBlank((String)parametros.get("filial.idFilial")))
			idFilial =Long.valueOf((String)parametros.get("filial.idFilial"));

		Long filial = findFilialAtendimentoMunicipio(
				idMunicipio,
				idServico,
				blIndicadorColeta,
				dtConsulta,
				cep,
				idCliente,
				idClienteRemetente,
				idSegmentoMercado,
				idUnidadeFederativa,
				idFilial, 
				null
		);

		Filial fil = filialService.findByIdInitLazyProperties(filial, false);
		return fil.getSiglaNomeFilial();
	}

	/**
	 * 
	 * @param idMunicipio
	 * @param idServico
	 * @param blIndicadorColeta
	 * @param dtConsulta
	 * @param cep
	 * @param idCliente
	 * @param idClienteRemetente
	 * @param idSegmento
	 * @param idUfOrigem
	 * @param idFilialOrigem
	 * @return Long Id da Filial responsavel pelo atendimento
	 */
	public Long findFilialAtendimentoMunicipio(
			Long idMunicipio,
			Long idServico,
			Boolean blIndicadorColeta,
			YearMonthDay dtConsulta, 
			String cep,
			Long idCliente,
			Long idClienteRemetente,
			Long idSegmento,
			Long idUfOrigem,
			Long idFilialOrigem, 
			Long idNaturezaProduto
	) {
		Map<String, Object> retorno = findAtendimentoMunicipio(
				idMunicipio,
				idServico,
				blIndicadorColeta,
				dtConsulta,
				cep,
				idCliente,
				idClienteRemetente,
				idSegmento,
				idUfOrigem,
				idFilialOrigem,
				"N", 
				idNaturezaProduto,
				null
		);
		if (retorno != null)
			return (Long) retorno.get("idFilial");

		return null;
	}

	/**
	 * 
	 * @param idMunicipio
	 * @param idServico
	 * @param blIndicadorColeta
	 * @param dtConsulta
	 * @return Long Id da Filial responsavel pelo atendimento
	 */
	public Long findFilialAtendimentoMunicipio(
			Long idMunicipio,
			Long idServico,
			Boolean blIndicadorColeta) {
		return findFilialAtendimentoMunicipio(
				idMunicipio,
				idServico,
				blIndicadorColeta,
				JTDateTimeUtils.getDataAtual(),
				null,
				null,
				null,
				null,
				null,
				null,
				null);
	}
	
	/**
	 * Retorna a filial responsavel por efetuar o atendimento do municipio e o tipo de operacao
	 * @param idMunicipio
	 * @param idServico
	 * @param blIndicadorColeta
	 * @param dtConsulta
	 * @param cep
	 * @param idCliente
	 * @param idSegmento
	 * @param idUfOrigem
	 * @param idFilialOrigem
	 * @param blGeracaoMCD
	 * @return Map [idFilial=Filial responsavel pelo atendimento (Long); 
	 * 				tpOperacao=Tipo de operacao (String); 
	 * 				idTipoLocalizacaoMunicipio=Tipo de Localizacao do municipio (Long);
	 * 				sgFilial=Sigla da filial responsavel pelo atendimento (String);
	 * 				nmFilial=Nome da filial responsavel pelo atendimento (String);
	 * 				nrTempoColeta=Tempo de coleta da filial (Long);
	 * 				nrTempoEntrega=Tempo de entrega da filial (Long)]
	 */
	public Map<String, Object> findAtendimentoMunicipio(
			Long idMunicipio,
			Long idServico,
			Boolean blIndicadorColeta,
			YearMonthDay dtConsulta,
			String nrCep,
			Long idCliente,
			Long idClienteRemetente,
			Long idSegmento,
			Long idUfOrigem,
			Long idFilialOrigem,
			String blGeracaoMCD,
			Long idNaturezaProduto,
			Boolean substiuiFilial){

		/*Informa a data atual caso a mesma for nula*/
		if (dtConsulta == null){
			dtConsulta = JTDateTimeUtils.getDataAtual();
		}	

		/*Mapa com os dados de retorno dos parametros*/
		Map<String, Object> result = getFilialAtendimento(idMunicipio, idServico,
				blIndicadorColeta, dtConsulta, nrCep, idCliente,
				idClienteRemetente, idSegmento, idUfOrigem, idFilialOrigem,
				blGeracaoMCD);

		/*Se nao encontrar resultado, lanca excecao*/
		if(result == null) {
			final Municipio municipio = municipioService.findByIdInitLazyProperties(idMunicipio, false);			
			throw new BusinessException("LMS-29080", new Object[]{municipio.getNmMunicipio()});
		}
		
		final Long idFilial = (Long)result.get("idFilial"); 
		final Long idRegionalOrigem = (Long)result.get("idRegionalOrigem");

		if( Boolean.TRUE.equals(substiuiFilial) ){
		/*Retorna os dados da filial substituta , caso encontrar a mesma*/
			final Filial filialSubstituta = substAtendimentoFilialService.findFilialSubstituta(new DomainValue("EN"), null, idFilial, idNaturezaProduto, idServico, idUfOrigem, idRegionalOrigem, idFilialOrigem, idClienteRemetente, idMunicipio,true);

		if(filialSubstituta != null){
				Regional regional = regionalFilialService.findLastRegionalVigente(filialSubstituta.getIdFilial());
				Long idRegional = null;
				if( regional != null ){
					idRegional = regional.getIdRegional();
				}

				result.put("idFilial", filialSubstituta.getIdFilial());
				result.put("idRegionalOrigem", idRegional);			
				result.put("sgFilial", filialSubstituta.getSgFilial());
				result.put("nmFilial", filialSubstituta.getPessoa().getNmFantasia());
			}
		}
		return result;
	}

	public Map<String, Object> getFilialAtendimento(Long idMunicipio,
			Long idServico, Boolean blIndicadorColeta, YearMonthDay dtConsulta,
			String nrCep, Long idCliente, Long idClienteRemetente,
			Long idSegmento, Long idUfOrigem, Long idFilialOrigem,
			String blGeracaoMCD) {
		Map<String, Object> result = null;

		/*Obtem a lista de OperacaoServicoLocaliza através de idMunicipio, blIndicadorColeta, idServico, dtConsulta*/
		final List<OperacaoServicoLocaliza> operacoesServicoLocaliza = operacaoServicoLocalizaService.findOperacaoServicoLocaliza(idMunicipio, blIndicadorColeta, idServico, dtConsulta);
		
		/*Obtem os dados de atendimento*/ 
		for (final OperacaoServicoLocaliza operacaoServicoLocaliza : operacoesServicoLocaliza) {

			Long idOperacaoServicoLocaliza = operacaoServicoLocaliza.getIdOperacaoServicoLocaliza();
			MunicipioFilial municipioFilial = operacaoServicoLocaliza.getMunicipioFilial();
			Filial filial = municipioFilial.getFilial();

			Regional regional = regionalFilialService.findLastRegionalVigente(filial.getIdFilial());
			Long idRegionalOrigem = null;
			if( regional != null ){
				idRegionalOrigem = regional.getIdRegional();
			}
			
			Map<String, Object> atendimentoMap = new HashMap<String, Object>(10);
			atendimentoMap.put("idFilial", filial.getIdFilial());
			atendimentoMap.put("idRegionalOrigem", idRegionalOrigem);			
			atendimentoMap.put("tpOperacao", operacaoServicoLocaliza.getTpOperacao().getValue());
			atendimentoMap.put("idTipoLocalizacaoMunicipio", operacaoServicoLocaliza.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio());
			atendimentoMap.put("dsTipoLocalizacaoMunicipio", operacaoServicoLocaliza.getTipoLocalizacaoMunicipio().getDsTipoLocalizacaoMunicipio().getValue());
			atendimentoMap.put("idTipoLocalizacaoComercial", operacaoServicoLocaliza.getTipoLocalizacaoMunicipioFob().getIdTipoLocalizacaoMunicipio());
			atendimentoMap.put("sgFilial", filial.getSgFilial());
			atendimentoMap.put("nmFilial", filial.getPessoa().getNmFantasia());
			atendimentoMap.put("nrTempoColeta", operacaoServicoLocaliza.getNrTempoColeta());
			atendimentoMap.put("nrTempoEntrega", operacaoServicoLocaliza.getNrTempoEntrega());
			atendimentoMap.put("nrDiasAtendidos", getNrDiasAtendidos(operacaoServicoLocaliza));

			Long idMunicipioFilial = municipioFilial.getIdMunicipioFilial();

			/*Se o atendimento nao possuir restricao*/
			if (!municipioFilial.getBlRestricaoAtendimento().booleanValue() || ("S".equals(blGeracaoMCD))){
				
				/*Se o atendimento for especifico*/
				if (!operacaoServicoLocaliza.getBlAtendimentoGeral().booleanValue()){
					
					/*Se foi informado cliente e este eh atendido*/
					if (idCliente != null && atendimentoClienteService.validateExisteAtendimentoCliente(idCliente, idOperacaoServicoLocaliza, dtConsulta)) {
						result = atendimentoMap;
						break;
					}
				} else {
					result = atendimentoMap;
				}
				
			} else {

				/*Se foi informado cliente e indicador de coleta = 'true'*/
				if (idClienteRemetente != null && !blIndicadorColeta.booleanValue()){

					/*Verifica se existe registro em MUNICIPIO_FILIAL_CLI_ORIGEM*/
					if (municipioFilialCliOrigemService.verificaExisteMunicipioFilialCliente(idMunicipioFilial, idClienteRemetente, dtConsulta)) {
						result = atendimentoMap;
						break;
					}
				}

				/*Se foi informado CEP, verifica se existe registro em MUNICIPIO_FILIAL_INTERV_CEP*/
				if(nrCep != null && municipioFilialIntervCepService.verificaExisteMunicipioFilialCep(idMunicipioFilial, nrCep, dtConsulta)) {
					
					/*Se for atendimento especifico e se existir atendimento para o cliente*/
					if (!operacaoServicoLocaliza.getBlAtendimentoGeral().booleanValue() && idCliente != null){
						if (atendimentoClienteService.validateExisteAtendimentoCliente(idCliente, idOperacaoServicoLocaliza, dtConsulta)) {
							result = atendimentoMap;
							break;
						} else
							continue;
					} else {
						result = atendimentoMap;
						break;
					}					
				}

				/*Se o indicador de coleta for falso*/
				if (!blIndicadorColeta.booleanValue()){
					
					/*Se o segmento for informado, verifica se existe registro em MUNICIPIO_FILIAL_SEGMENTO*/
					if (idSegmento != null && municipioFilialSegmentoService.verificaExisteMunicipioFilialSegmento(idMunicipioFilial, idSegmento, dtConsulta)){
						result = atendimentoMap;
						break;
					}

					/*Se a UF de origem foi informada, verifica se existe registro em MUNICIPIO_FILIAL_UF_ORIGEM*/
					if (idUfOrigem != null && municipioFilialUFOrigemService.verificaExisteMunicipioFilialUfOrigem(idMunicipioFilial, idUfOrigem, dtConsulta)){
						result = atendimentoMap;
						break;
					}

					/*Se a filial de origem foi informada, verifica se existe registro em MUNICIPIO_FILIAL_FIL_ORIGEM*/
					if (idFilialOrigem != null && municipioFilialFilOrigemService.verificaExisteMunicipioFilialFilOrigem(idMunicipioFilial, idFilialOrigem, dtConsulta)){
						result = atendimentoMap;
						break;
					}
				}			

			}/*else*/

		}/*for*/
		return result;
	}

	
	/**
	 * Retorna o prazo padrao de entrega
	 * 
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idServico
	 * @param idCliente
	 * @param cepOrigem
	 * @param cepDestino
	 * @param idSegmento
	 * @return
	 */
	public Map<String, Object> executeCalculoPPE(
			Long idMunicipioOrigem,
			Long idMunicipioDestino,
			Long idServico,
			Long idCliente,
			String cepOrigem,
			String cepDestino,
			Long idSegmento
	) {
		return executeCalculoPPE(
				idMunicipioOrigem,
				idMunicipioDestino,
				idServico,
				idCliente,
				cepOrigem,
				cepDestino,
				idSegmento,
				null,
				"S"
		);
	}

	/**
	 * Retorna o prazo padrao de entrega
	 * 
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idServico
	 * @param idCliente
	 * @param cep
	 * @param idSegmento
	 * @param blGeracaoMCD 
	 * @return Prazo previsto de entrega em horas
	 */
	public Map<String, Object> executeCalculoPPE(
			Long idMunicipioOrigem,
			Long idMunicipioDestino,
			Long idServico,
			Long idCliente,
			String cepOrigem,
			String cepDestino,
			Long idSegmento,
			YearMonthDay dtConsulta,
			String blGeracaoMCD
	) {
		if (dtConsulta == null)
			dtConsulta = JTDateTimeUtils.getDataAtual();

		Map<String, Object> tempoColetaFilialOrigem = findAtendimentoMunicipio(
				idMunicipioOrigem,
				idServico,
				Boolean.TRUE,
				dtConsulta,
				cepOrigem,
				idCliente,
				null,
				idSegmento,
				null,
				null,
				blGeracaoMCD,
				null,
				null
		);
		Long idFilialOrigem = (Long) tempoColetaFilialOrigem.get("idFilial");
		Long nrTempoColeta = (Long) tempoColetaFilialOrigem.get("nrTempoColeta");

		Municipio municipioOrigem = municipioService.findById(idMunicipioOrigem);

		Map<String, Object> tempoEntregaFilialDestino = findAtendimentoMunicipio(
				idMunicipioDestino,
				idServico,
				Boolean.FALSE,
				dtConsulta,
				cepDestino,
				idCliente,
				null,
				idSegmento,
				municipioOrigem.getUnidadeFederativa().getIdUnidadeFederativa(),
				idFilialOrigem,
				blGeracaoMCD,
				null,
				null
		);

		Long idFilialDestino = (Long) tempoEntregaFilialDestino.get("idFilial");
		Long nrTempoEntrega = (Long) tempoEntregaFilialDestino.get("nrTempoEntrega");
		Long nrTempoEntregaTotal = determinaTempoEntrega((Long)tempoEntregaFilialDestino.get("nrDiasAtendidos"), nrTempoEntrega);

		Long nrTempoTransferencia = LongUtils.ZERO;
		nrTempoTransferencia = calcularTempoDeslocamentoEntreFiliais(idFilialOrigem, idFilialDestino, idServico, dtConsulta, null);

		long tempoColeta = (nrTempoColeta != null) ? nrTempoColeta.longValue() : 0;
		long tempoEntrega = (nrTempoEntregaTotal != null) ? nrTempoEntregaTotal.intValue() : 0;
		long tempoTransferencia = (nrTempoTransferencia != null) ? nrTempoTransferencia.longValue() : 0;

		long tempoTotal = (tempoColeta + tempoEntrega + tempoTransferencia); 

		tempoTotal = (tempoTotal > 0) ? tempoTotal/60 : 0;
		tempoColeta = (tempoColeta > 0) ? tempoColeta/60 : 0;
		tempoEntrega = (tempoEntrega > 0) ? tempoEntrega/60 : 0;
		tempoTransferencia = (tempoTransferencia > 0) ? tempoTransferencia/60 : 0;

		final Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put(NR_TEMPO_COLETA,  Long.valueOf(tempoColeta));
		retorno.put(NR_TEMPO_ENTREGA,  Long.valueOf(tempoEntrega));
		retorno.put(NR_TEMPO_TRANSFERENCIA,  Long.valueOf(tempoTransferencia));
		retorno.put(NR_PRAZO,  Long.valueOf(tempoTotal));
		
		return retorno;
	}

	/**
	 * Busca a filial responsavel por executar a coleta e o tempo de coleta
	 * @param idMunicipioOrigem
	 * @param idServico 
	 * @return Map [idFilial= Filial de atendimento no municipio, nrTempoColeta=Tempo de coleta no municipio]
	 */
	public Map<String, Object> calcularTempoColetaFilialOrigem(Long idMunicipioOrigem, Long idServico, String cepOrigem){
		List<Object[]> atendimento = findAtendimentosVigentesByMunicipioServicoOperacao(idMunicipioOrigem, idServico, "C");

		Map<String, Object> retorno = null;
		if (!atendimento.isEmpty()) {
			Object[] objAtendimento = atendimento.get(0);
			retorno = new HashMap<String, Object>();
			retorno.put("idFilial", objAtendimento[1]);
			retorno.put("nrTempoColeta", objAtendimento[3]);
		} else {
			Municipio municipio = municipioService.findByIdInitLazyProperties(idMunicipioOrigem, false);
			throw new BusinessException("LMS-29079", new Object[]{municipio.getNmMunicipio()});
		}
		return retorno;
	}

	/**
	 * Retorna o tempo previsto de entrega e a filial responsavel por efetuar a entrega
	 * @param idMunicipioDestino
	 * @param idServico
	 * @param idFilialOrigem
	 * @param idCliente
	 * @param cep
	 * @param idSegmento
	 * @return Map [idFilial= Filial responsavel, nrTempoEntrega=Tempo previsto de entrega]
	 */
	public Map<String, Object> calcularTempoEntregaFilialDestino(Long idMunicipioDestino, Long idServico, Long idFilialOrigem, Long idCliente, String cep, Long idSegmento){
		Long filial_destino = null;
		Long nr_tempo_entrega_total = null;

		// Buscar atendimentos ao municipio vigentes na data atual
		List<Object[]> atendimentos = findAtendimentosVigentesByMunicipioServicoOperacao(idMunicipioDestino, idServico, "E");

		if (!atendimentos.isEmpty()) {			
			Object[] atendimento = atendimentos.get(0);
			filial_destino = (Long) atendimento[1];
		} else 	{
			Municipio municipio = municipioService.findByIdInitLazyProperties(idMunicipioDestino, false);
			throw new BusinessException("LMS-29079", new Object[]{municipio.getNmMunicipio()});
		}

		Map<String, Object> retorno = new HashMap<String, Object>(2);
		retorno.put("idFilial", filial_destino);
		retorno.put("nrTempoEntrega", nr_tempo_entrega_total);

		return retorno;
	}

	/**
	 * Consulta todos os atendimentos vigentes
	 * @param idMunicipioOrigem
	 * @param idServico
	 * @return
	 */
	private List<Object[]> findAtendimentosVigentesByMunicipioServicoOperacao(Long idMunicipioDestino, Long idServico, String tpOperacao){
		return municipioFilialService.findAtendimentosVigentesByMunicipioServicoOperacao(idMunicipioDestino, idServico, tpOperacao);
	}

	/**
	 * Conta quantos dias estao marcados como 'S' no registro de Operacao X Servico X Localizacao
	 * @param operacaoServicoLocaliza
	 * @return Long com a quantidade de dias atendidos
	 */
	private Long getNrDiasAtendidos(OperacaoServicoLocaliza operacaoServicoLocaliza) {
		int result = 0;
		if(operacaoServicoLocaliza.getBlDomingo()) result++;
		if(operacaoServicoLocaliza.getBlSegunda()) result++;
		if(operacaoServicoLocaliza.getBlTerca()) result++;
		if(operacaoServicoLocaliza.getBlQuarta()) result++;
		if(operacaoServicoLocaliza.getBlQuinta()) result++;
		if(operacaoServicoLocaliza.getBlSexta()) result++;
		if(operacaoServicoLocaliza.getBlSabado()) result++;

		return Long.valueOf(result);
	}

	/**
	 * Determina o tempo de entrega total a partir do numero de dias atendidos
	 * @param nrDiasAtendidos
	 * @param nrTempoEntrega
	 * @return
	 */
	private Long determinaTempoEntrega(Long nrDiasAtendidos, Long nrTempoEntrega){
		Long retorno = null;
		long tempoEntrega = nrTempoEntrega != null ? nrTempoEntrega.longValue() : 0;

		if (nrDiasAtendidos.intValue() == 1) {
			retorno = Long.valueOf(120*60 + tempoEntrega);
		} else if (nrDiasAtendidos.intValue() == 2){
			retorno = Long.valueOf(48*60 + tempoEntrega);
		} else if (nrDiasAtendidos.intValue() == 3){
			retorno = Long.valueOf(24*60 + tempoEntrega);
		} else if (nrDiasAtendidos.intValue() > 3) {
			retorno = Long.valueOf(tempoEntrega);
		}
		return retorno;
	}

	public Long calcularTempoDeslocamentoEntreFiliais(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta){
		return calcularTempoDeslocamentoEntreFiliais(idFilialOrigem, idFilialDestino, idServico, dtConsulta, null);
	}

	/**
	 * Retorna o tempo previsto de entrega e a filial responsavel por efetuar a entrega
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @return
	 */
	public Long calcularTempoDeslocamentoEntreFiliais(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta, List<Long> idsAnteriores){
		List<Object[]> result = fluxoFilialService.findFluxoFilialByFilialDestinoOrigemServico(idFilialOrigem, idFilialDestino, idServico, dtConsulta);
		Long tempo_transferencia = null;

		if (idsAnteriores == null)
			idsAnteriores = new ArrayList<Long>();

		if (!result.isEmpty()){
			Object[] fluxo = (Object[]) result.get(0);
			Long idFilialReembarcadora = (Long) fluxo[0];
			Integer nrPrazo = (Integer) fluxo[1];			
			Long idFluxoFilial = (Long)fluxo[4];

			if (idFilialReembarcadora != null) {
				if (idsAnteriores.contains(idFluxoFilial)) {
					Filial f1 = filialService.findByIdInitLazyProperties((Long)fluxo[5], false);
					Filial f2 = filialService.findByIdInitLazyProperties((Long)fluxo[6], false);

					throw new BusinessException("LMS-29155", new Object[]{f1.getSiglaNomeFilial(), f2.getSiglaNomeFilial()});
				}

				idsAnteriores.add(idFluxoFilial);

				tempo_transferencia = calcularTempoDeslocamentoEntreFiliais(idFilialReembarcadora, idFilialDestino, idServico, dtConsulta, idsAnteriores);
				tempo_transferencia = Long.valueOf(((tempo_transferencia != null) ? tempo_transferencia.intValue() : 0) + nrPrazo.intValue());
			} else {
				tempo_transferencia = Long.valueOf(nrPrazo.intValue());
			}
		} else {
			Filial f1 = filialService.findByIdInitLazyProperties(idFilialOrigem, false);
			Filial f2 = filialService.findByIdInitLazyProperties(idFilialDestino, false);

			throw new BusinessException("LMS-29099", new Object[]{f1.getSiglaNomeFilial(), f2.getSiglaNomeFilial()});
		}
		return tempo_transferencia;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	public void setAtendimentoClienteService(AtendimentoClienteService atendimentoClienteService) {
		this.atendimentoClienteService = atendimentoClienteService;
	}
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}
	public void setMunicipioFilialCliOrigemService(MunicipioFilialCliOrigemService municipioFilialCliOrigemService) {
		this.municipioFilialCliOrigemService = municipioFilialCliOrigemService;
	}
	public void setMunicipioFilialIntervCepService(MunicipioFilialIntervCepService municipioFilialIntervCepService) {
		this.municipioFilialIntervCepService = municipioFilialIntervCepService;
	}
	public void setMunicipioFilialSegmentoService(MunicipioFilialSegmentoService municipioFilialSegmentoService) {
		this.municipioFilialSegmentoService = municipioFilialSegmentoService;
	}
	public void setMunicipioFilialUFOrigemService(MunicipioFilialUFOrigemService municipioFilialUFOrigemService) {
		this.municipioFilialUFOrigemService = municipioFilialUFOrigemService;
	}
	public void setMunicipioFilialFilOrigemService(MunicipioFilialFilOrigemService municipioFilialFilOrigemService) {
		this.municipioFilialFilOrigemService = municipioFilialFilOrigemService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setOperacaoServicoLocalizaService(OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}

	public SubstAtendimentoFilialService getSubstAtendimentoFilialService() {
		return substAtendimentoFilialService;
}

	public void setSubstAtendimentoFilialService(
			SubstAtendimentoFilialService substAtendimentoFilialService) {
		this.substAtendimentoFilialService = substAtendimentoFilialService;
	}

	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
}

}
