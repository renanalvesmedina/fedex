package com.mercurio.lms.portaria.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.service.ControleEsteiraService;
import com.mercurio.lms.expedicao.model.service.ManifestoEletronicoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.processo.ChegadaPortaria;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.portaria.model.service.utils.InformarChegadaSaidaUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;

/**
 * LMSA-768 - Classe de serviços para processo "Informar Chegada na Portaria".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 * @spring.bean id="lms.portaria.newInformarChegadaService"
 * 
 */
public class NewInformarChegadaService {

	public static final String NR_IDENTIFICACAO = "nrIdentificacao";
	private NewSaidaChegadaDAO newSaidaChegadaDAO;
	private ManifestoEletronicoService manifestoEletronicoService;
	private ControleQuilometragemService controleQuilometragemService;
	private IntegracaoJmsService integracaoJmsService;
	private ControleEsteiraService controleEsteiraService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private LacreControleCargaService lacreControleCargaService;
	private OrdemSaidaService ordemSaidaService;
	private FilialService filialService;
	private InformarChegadaService informarChegadaService;
	
	/**
	 * Procede o encerramento dos {@link ManifestoEletronico}s relacionados a um
	 * {@link ControleCarga}.
	 * 
	 * @param controleCarga
	 */
	public void encerrarManifestosEletronicos(ControleCarga controleCarga) {
		manifestoEletronicoService.encerrarMdfesAutorizados(controleCarga.getIdControleCarga());
	}
	
	
	/***
	 * Metodo que executa a geração do xml para o sorter
	 * 
	 * @param controleCarga
	 */
	public void geraXmlSorterPortaria(ControleCarga controleCarga) {
		controleEsteiraService.executeGerarXmlSorterPortaria(controleCarga.getIdControleCarga());
	}	


	/**
	 * Processa informação de quilometragem do {@link MeioTransporte}.
	 * 
	 * @param filial
	 * @param meioTransporte
	 * @param nrQuilometragem
	 * @param blVirouHodometro
	 * @param controleCarga
	 */
	public void processarQuilometragemMeioTransporte(
			Filial filial, MeioTransporte meioTransporte, Integer nrQuilometragem, Boolean blVirouHodometro, ControleCarga controleCarga, String obQuilometragem) {
		controleQuilometragemService.storeInformarQuilometragemMeioTransporte(
				filial.getIdFilial(),
				true,
				meioTransporte.getIdMeioTransporte(),
				false,
				nrQuilometragem,
				blVirouHodometro,
				controleCarga.getIdControleCarga(),
				null,
				obQuilometragem);
	}

	/**
	 * Envia mensagens JMS produzidas por eventos.
	 * 
	 * @param topic
	 *            {@link Queues} destino para as mensagens.
	 * @param mensagens
	 *            {@link List} de mensagens a processar.
	 */
	public void processarMensagens(VirtualTopics topic, List<Serializable> mensagens) {
		JmsMessageSender sender = integracaoJmsService.createMessage(topic);
		for (Serializable mensagem : mensagens) {
			sender.addMsg(mensagem);
		}
		integracaoJmsService.storeMessage(sender);
	}
	
	public Map<String, Object> findDados(TypedFlatMap parametros) {
		String idControleTemp = parametros.getString("idControleTemp");
		Long idControle = InformarChegadaSaidaUtils.getIdControle(idControleTemp);
		String tpChegada = InformarChegadaSaidaUtils.getTipo(idControleTemp);		
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		if (ConstantesPortaria.TP_VIAGEM.equals(tpChegada) || ConstantesPortaria.TP_COLETA_ENTREGA.equals(tpChegada)) {
			retorno = preencheDadosViagemEColetaEntrega(idControle, tpChegada);			
		} else if (ConstantesPortaria.TP_ORDEM_SAIDA.equals(tpChegada)) {
			retorno = preencheDadosOrdemSaida(idControle);
		}
		
		retorno.put("blInformaKmPortaria", getFilialService().findBlInformaKmPortaria(parametros.getLong("idFilial")));
		retorno.put("dhChegada", JTDateTimeUtils.getDataHoraAtual());
		retorno.put("tpChegada", tpChegada);
						
		return retorno;
	}


	private Map<String, Object> preencheDadosViagemEColetaEntrega(Long idControle, String tpChegada) {
		Long idControleCarga;
		Integer versao = null;
		
		if (ConstantesPortaria.TP_VIAGEM.equals(tpChegada)) {
			ControleTrecho ct = getControleTrechoService().findById(idControle);
			versao = ct.getVersao();
			idControleCarga = ct.getControleCarga().getIdControleCarga();				
		} else {
			idControleCarga = idControle;
			idControle = null;
		}

		Map<String, Object> retorno = new HashMap<String, Object>();
		List result = getControleCargaService().findDadosChegadaSaida(idControleCarga, tpChegada);
		if (!result.isEmpty()) {
			retorno = (Map) result.get(0);
			retorno.put("tpControleCarga", ((DomainValue) retorno.get("tpControleCarga")).getValue());
			retorno.put("idControleCarga", idControleCarga);
			retorno.put("idControleTrecho", idControle);

			String dsRota = (String) retorno.get("dsRota");
			if(dsRota == null) {
				dsRota = (String) retorno.get("dsRotaControleCarga");
			}
			Integer nrRota = retorno.get("nrRota") != null ? ((Short)retorno.get("nrRota")).intValue() : null;
			if(nrRota == null) {
				nrRota = (Integer) retorno.get("nrRotaIdaVolta");
			}
			retorno.put("dsRota", InformarChegadaSaidaUtils.formatarRota(nrRota, dsRota));
			
			retorno.put("versao", versao);
			retorno.put("lacres", getLacreControleCargaService().findByControleCarga(idControleCarga));
			if (retorno.get(NR_IDENTIFICACAO) != null) {
				retorno.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)retorno.get("tpIdentificacao"), (String) retorno.get(NR_IDENTIFICACAO)));
			}
		}
		
		return retorno;
	}

	private Map<String, Object> preencheDadosOrdemSaida(Long idControle) {
		List result = getOrdemSaidaService().findDadosChegadaSaida(idControle);	
		Map<String, Object> retorno = (Map) result.get(0);
		retorno.put("idOrdemServico", idControle); 
		retorno.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)retorno.get("tpIdentificacao"), (String) retorno.get(NR_IDENTIFICACAO)));
		return retorno;
	}
	
	public Map<String, Object> validateAndFindDados(TypedFlatMap parametros) {

		Map<String,Object> validacao = new HashMap<String,Object>();

		if (ConstantesPortaria.SUBTIPO_VIAGEM.equals((String) parametros.get("subTipo"))) {
			getInformarChegadaService().validaTransito(new TypedFlatMap(parametros));
			getInformarChegadaService().validarBloqueio(new TypedFlatMap(parametros));
			getInformarChegadaService().validarOperacaoFedex(new TypedFlatMap(parametros));
			validacao = getInformarChegadaService().validateTempoEsperaConhecimento(new TypedFlatMap(parametros));

		} else if (ConstantesPortaria.SUBTIPO_COLETA_ENTREGA.equals(parametros.get("subTipo").toString())) {
			getInformarChegadaService().validarBloqueio(new TypedFlatMap(parametros));
		}
		
		Map<String,Object> retorno =  findDados(parametros);

		if(validacao.containsKey("validado") && !(Boolean)validacao.get("validado")){
			retorno.put("mensagemConfirmacao",validacao.get("mensagemConfirmacao"));
		}

		return retorno;
	}

	public void executeSalvar(TypedFlatMap parametros) {
		parametros.put(ConstantesPortaria.PARAMETRO_BL_PORT_CHEGADA_NOVO, getParametroPortChegadaNovo(parametros.getLong("idFilial")));
		ChegadaPortaria.createChegadaPortaria(this, newSaidaChegadaDAO, parametros);
	}
	
	private boolean getParametroPortChegadaNovo(Long idFilialUsuario) {
		Object conteudoParametro = getConteudoParametroFilialService().findConteudoByNomeParametro(idFilialUsuario, ConstantesPortaria.PARAMETRO_BL_PORT_CHEGADA_NOVO, false);
		if (conteudoParametro != null) {
			return ConstantesPortaria.SIM.equals(conteudoParametro);
		}
		return false;
	}


	public NewSaidaChegadaDAO getNewSaidaChegadaDAO() {
		return newSaidaChegadaDAO;
	}


	public void setNewSaidaChegadaDAO(NewSaidaChegadaDAO newSaidaChegadaDAO) {
		this.newSaidaChegadaDAO = newSaidaChegadaDAO;
	}


	public ManifestoEletronicoService getManifestoEletronicoService() {
		return manifestoEletronicoService;
	}


	public void setManifestoEletronicoService(
			ManifestoEletronicoService manifestoEletronicoService) {
		this.manifestoEletronicoService = manifestoEletronicoService;
	}


	public ControleQuilometragemService getControleQuilometragemService() {
		return controleQuilometragemService;
	}


	public void setControleQuilometragemService(
			ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}


	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}


	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}


	public ControleEsteiraService getControleEsteiraService() {
		return controleEsteiraService;
	}


	public void setControleEsteiraService(
			ControleEsteiraService controleEsteiraService) {
		this.controleEsteiraService = controleEsteiraService;
	}


	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}


	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}


	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}


	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}


	public ControleTrechoService getControleTrechoService() {
		return controleTrechoService;
	}


	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}


	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}


	public void setLacreControleCargaService(
			LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}


	public OrdemSaidaService getOrdemSaidaService() {
		return ordemSaidaService;
	}


	public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
		this.ordemSaidaService = ordemSaidaService;
	}


	public FilialService getFilialService() {
		return filialService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public InformarChegadaService getInformarChegadaService() {
		return informarChegadaService;
	}


	public void setInformarChegadaService(
			InformarChegadaService informarChegadaService) {
		this.informarChegadaService = informarChegadaService;
	}

}
