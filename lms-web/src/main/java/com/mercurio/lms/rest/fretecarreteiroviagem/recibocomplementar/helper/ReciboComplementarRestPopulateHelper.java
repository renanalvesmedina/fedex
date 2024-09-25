package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.fretecarreteiroviagem.model.AnexoReciboFc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.configuracoes.MoedaDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.configuracoes.dto.MoedaPaisDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MotoristaSuggestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboHelperConstants;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.helper.ReciboColetaEntregaHelper;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto.ReciboComplementarDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto.ReciboComplementarFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.workflow.PendenciaDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Responsável pela manipulação de conversões de dados do rest.
 *
 */
public class ReciboComplementarRestPopulateHelper {

	/**
	 * Default private constructor
	 */
	private ReciboComplementarRestPopulateHelper(){
		
	}
	
	public static TypedFlatMap getFilterMap(TypedFlatMap criteria, ReciboComplementarFilterDTO filter){
		criteria.put("dhEmissaoInicial", filter.getDhEmissaoInicial());
		criteria.put("dhEmissaoFinal", filter.getDhEmissaoFinal());
		criteria.put("dtPagtoRealInicial", filter.getDtPagtoRealInicial());
		criteria.put("dtPagtoRealFinal", filter.getDtPagtoRealFinal());
		criteria.put("dtProgramadaPagtoInicial", filter.getDtProgramadaPagtoInicial());
		criteria.put("dtProgramadaPagtoFinal", filter.getDtProgramadaPagtoFinal());		
		
		criteria.put("idReciboFreteCarreteiro", filter.getIdReciboFreteCarreteiro());
		criteria.put("nrReciboFreteCarreteiro", filter.getNrReciboFreteCarreteiro());			
		criteria.put("relacaoPagamento.nrRelacaoPagamento", filter.getNrRelacaoPagamento());
		
		if(filter.getTpReciboFreteCarreteiro() != null){
			criteria.put("tpReciboFreteCarreteiro", filter.getTpReciboFreteCarreteiro().getValue());
		}
		
		if(filter.getTpSituacaoRecibo() != null){
			criteria.put("tpSituacaoRecibo", filter.getTpSituacaoRecibo().getValue());
		}
		
		if(filter.getReciboComplementado() != null){
			criteria.put("reciboComplementado.idReciboFreteCarreteiro", filter.getReciboComplementado().getIdReciboFreteCarreteiro());
			criteria.put("reciboComplementado.nrReciboFreteCarreteiro", filter.getReciboComplementado().getNrReciboFreteCarreteiro());			
		}
		
		if(filter.getControleCarga() != null){
			criteria.put("controleCarga.idControleCarga", filter.getControleCarga().getIdControleCarga());
		}
		
		if(filter.getProprietario() != null){
			criteria.put("proprietario.idProprietario", filter.getProprietario().getIdProprietario());
		}
		
		if(filter.getFilial() != null){
			criteria.put("filial.idFilial", filter.getFilial().getIdFilial());
		}
		
		if(filter.getMeioTransporte() != null){
			criteria.put("meioTransporteRodoviario.idMeioTransporte", filter.getMeioTransporte().getIdMeioTransporte());
		}
		
		return criteria;
	}
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param workflow
	 * @param anexos 
	 * 
	 * @return ReciboComplementarDTO
	 */
	public static ReciboComplementarDTO getReciboComplementarDTO(
			ReciboFreteCarreteiro reciboFreteCarreteiro, boolean workflow) {		
		ReciboComplementarDTO reciboComplementarDTO = new ReciboComplementarDTO();	
		
		getDadosRecibo(reciboFreteCarreteiro, reciboComplementarDTO);		
		getFilialDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getProprietarioDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getPendenciaDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getControleCargaDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getReciboComplementadoDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getMeioTransporteRodoviarioDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getModeloMarcaMeioTransporteDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getFilialDestinoDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getUsuarioDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getMoedaPaisDTO(reciboFreteCarreteiro, reciboComplementarDTO);
		getMotoristaDTO(reciboFreteCarreteiro, reciboComplementarDTO);
				
		defineDisabled(reciboFreteCarreteiro, reciboComplementarDTO, workflow);
		
		return reciboComplementarDTO;
	}
	
	/**
	 * 
	 * @param reciboComplementarDTO
	 * 
	 * @return ReciboFreteCarreteiro
	 */
	public static ReciboFreteCarreteiro getReciboFreteCarreteiro(ReciboComplementarDTO reciboComplementarDTO){
		ReciboFreteCarreteiro reciboFreteCarreteiro = new ReciboFreteCarreteiro();
		
		getDadosRecibo(reciboComplementarDTO, reciboFreteCarreteiro);
		getProprietario(reciboComplementarDTO, reciboFreteCarreteiro);
		getFilial(reciboComplementarDTO, reciboFreteCarreteiro);
		getFilialDestino(reciboComplementarDTO, reciboFreteCarreteiro);
		getControleCarga(reciboComplementarDTO, reciboFreteCarreteiro);
		getMeioTransporteRodoviario(reciboComplementarDTO, reciboFreteCarreteiro);
		getReciboComplementado(reciboComplementarDTO, reciboFreteCarreteiro);
		getPendencia(reciboComplementarDTO, reciboFreteCarreteiro);
		getUsuario(reciboComplementarDTO, reciboFreteCarreteiro);
		getMoedaPais(reciboComplementarDTO, reciboFreteCarreteiro);
		getMotorista(reciboComplementarDTO, reciboFreteCarreteiro);
		
		return reciboFreteCarreteiro;
	}

	/**
	 * @param reciboComplementarDTO
	 * @param reciboFreteCarreteiro
	 */
	private static void getDadosRecibo(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		reciboFreteCarreteiro.setIdReciboFreteCarreteiro(reciboComplementarDTO.getIdReciboFreteCarreteiro());
		reciboFreteCarreteiro.setNrReciboFreteCarreteiro(reciboComplementarDTO.getNrReciboFreteCarreteiro());
		reciboFreteCarreteiro.setTpReciboFreteCarreteiro(reciboComplementarDTO.getTpReciboFreteCarreteiro());
		reciboFreteCarreteiro.setTpSituacaoRecibo(reciboComplementarDTO.getTpSituacaoRecibo());
		reciboFreteCarreteiro.setTpSituacaoWorkflow(reciboComplementarDTO.getTpSituacaoWorkflow());
		reciboFreteCarreteiro.setBlAdiantamento(reciboComplementarDTO.getBlAdiantamento());
		reciboFreteCarreteiro.setVlBruto(reciboComplementarDTO.getVlBruto());
		reciboFreteCarreteiro.setPcAliquotaIssqn(reciboComplementarDTO.getPcAliquotaIssqn());
		reciboFreteCarreteiro.setVlIssqn(reciboComplementarDTO.getVlIssqn());
		reciboFreteCarreteiro.setPcAliquotaInss(reciboComplementarDTO.getPcAliquotaInss());
		reciboFreteCarreteiro.setVlSalarioContribuicao(reciboComplementarDTO.getVlSalarioContribuicao());
		reciboFreteCarreteiro.setVlInss(reciboComplementarDTO.getVlInss());
		reciboFreteCarreteiro.setVlOutrasFontes(reciboComplementarDTO.getVlOutrasFontes());
		reciboFreteCarreteiro.setPcAliquotaIrrf(reciboComplementarDTO.getPcAliquotaIrrf());
		reciboFreteCarreteiro.setVlIrrf(reciboComplementarDTO.getVlIrrf());
		reciboFreteCarreteiro.setVlPremio(reciboComplementarDTO.getVlPremio());
		reciboFreteCarreteiro.setVlPostoPassagem(reciboComplementarDTO.getVlPostoPassagem());
		reciboFreteCarreteiro.setVlDiaria(reciboComplementarDTO.getVlDiaria());
		reciboFreteCarreteiro.setPcAdiantamentoFrete(reciboComplementarDTO.getPcAdiantamentoFrete());
		reciboFreteCarreteiro.setVlLiquido(reciboComplementarDTO.getVlLiquido());
		reciboFreteCarreteiro.setVlDesconto(reciboComplementarDTO.getVlDesconto());
		reciboFreteCarreteiro.setDhGeracaoMovimento(reciboComplementarDTO.getDhGeracaoMovimento());
		reciboFreteCarreteiro.setDhEmissao(reciboComplementarDTO.getDhEmissao());
		reciboFreteCarreteiro.setDtSugeridaPagto(reciboComplementarDTO.getDtSugeridaPagto());
		reciboFreteCarreteiro.setDtProgramadaPagto(reciboComplementarDTO.getDtProgramadaPagto());
		reciboFreteCarreteiro.setDtPagtoReal(reciboComplementarDTO.getDtPagtoReal());
		reciboFreteCarreteiro.setDtContabilizacao(reciboComplementarDTO.getDtContabilizacao());
		reciboFreteCarreteiro.setDtCalculoInss(reciboComplementarDTO.getDtCalculoInss());
		reciboFreteCarreteiro.setNrNfCarreteiro(reciboComplementarDTO.getNrNfCarreteiro());
		reciboFreteCarreteiro.setObReciboFreteCarreteiro(reciboComplementarDTO.getObReciboFreteCarreteiro());
	}
	
	/**
	 * 
	 * @param resultSetPage
	 * @return List<ReciboComplementarDTO>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<ReciboComplementarDTO> getListReciboComplementarDTO(ResultSetPage resultSetPage){
		List<ReciboComplementarDTO> result = new ArrayList<ReciboComplementarDTO>();
		
		List<ReciboFreteCarreteiro> list = resultSetPage.getList();
		
		for (ReciboFreteCarreteiro reciboFreteCarreteiro : list) {
			ReciboComplementarDTO reciboComplementarDTO = new ReciboComplementarDTO();	
			
			getDadosRecibo(reciboFreteCarreteiro, reciboComplementarDTO);		
			getFilialDTO(reciboFreteCarreteiro, reciboComplementarDTO);
			getProprietarioDTO(reciboFreteCarreteiro, reciboComplementarDTO);
			getControleCargaDTO(reciboFreteCarreteiro, reciboComplementarDTO);
			getReciboComplementadoDTO(reciboFreteCarreteiro, reciboComplementarDTO);
			getMeioTransporteRodoviarioDTO(reciboFreteCarreteiro, reciboComplementarDTO);
			getMoedaPaisDTO(reciboFreteCarreteiro, reciboComplementarDTO);
			
			/*
			 * Necessário informar o ID para o framework.
			 */
			reciboComplementarDTO.setId(reciboComplementarDTO.getIdReciboFreteCarreteiro());
			
			result.add(reciboComplementarDTO);
		}
		
		return result;
	}
	
	/**
	 * Popula uma entidade AnexoReciboFreteCarreteiro.
	 * 
	 * @param dados
	 * @param data
	 * 
	 * @return AnexoReciboFreteCarreteiro
	 * 
	 * @throws IOException
	 */
	public static AnexoReciboFc getAnexoReciboComplementar(Map<String, Object> dados, byte[] data) {						
		AnexoReciboFc anexoReciboFc = new AnexoReciboFc();
		anexoReciboFc.setDescAnexo(MapUtils.getString(dados, "descAnexo"));
		anexoReciboFc.setDhCriacao(RestPopulateUtils.getYearMonthDayFromISO8601(dados, "dhCriacao").toDateTimeAtCurrentTime());
		anexoReciboFc.setDcArquivo(data);

		UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		anexoReciboFc.setUsuarioLMS(usuarioLMS);
    	
    	return anexoReciboFc;
	}

	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getProprietarioDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getProprietario() != null){		
			Proprietario proprietario = reciboFreteCarreteiro.getProprietario();
			
			ProprietarioDTO proprietarioDTO = new ProprietarioDTO();
			proprietarioDTO.setIdProprietario(proprietario.getIdProprietario());
			proprietarioDTO.setTpProprietario(proprietario.getTpProprietario());
			proprietarioDTO.setNmPessoa( proprietario.getPessoa().getNmPessoa());
			proprietarioDTO.setNrIdentificacao(proprietario.getPessoa().getNrIdentificacao());	
			proprietarioDTO.setTpIdentificacao(proprietario.getPessoa().getTpIdentificacao());									
			proprietarioDTO.setTpIdentificacaoFormatado(FormatUtils.formatIdentificacao(proprietarioDTO.getTpIdentificacao(), proprietarioDTO.getNrIdentificacao()));
			
			reciboComplementarDTO.setProprietario(proprietarioDTO);
		}
	}

	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getFilialDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getFilial() != null){		
			Filial filial = reciboFreteCarreteiro.getFilial();
			
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setIdFilial(filial.getIdFilial());
			filialDTO.setNmFilial(filial.getPessoa().getNmFantasia());
			filialDTO.setSgFilial(filial.getSgFilial());
						
			reciboComplementarDTO.setFilial(filialDTO);
		}
	}	
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getFilialDestinoDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getFilialDestino() != null){		
			Filial filial = reciboFreteCarreteiro.getFilial();
			
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setIdFilial(filial.getIdFilial());
			filialDTO.setNmFilial(filial.getPessoa().getNmFantasia());
			filialDTO.setSgFilial(filial.getSgFilial());
			
			reciboComplementarDTO.setFilialDestino(filialDTO);
		}
	}	
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getControleCargaDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getControleCarga() != null){		
			ControleCarga controleCarga = reciboFreteCarreteiro.getControleCarga();
			
			ControleCargaSuggestDTO controleCargaDTO = new ControleCargaSuggestDTO();
			controleCargaDTO.setIdControleCarga(controleCarga.getIdControleCarga());
			controleCargaDTO.setNrControleCarga(controleCarga.getNrControleCarga());
			controleCargaDTO.setSgFilial(controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			
			reciboComplementarDTO.setControleCarga(controleCargaDTO);
			
			/*
			 * Dados para a abertura de modal.
			 */
			reciboComplementarDTO.setIdControleCarga(controleCarga.getIdControleCarga());
			reciboComplementarDTO.setNrControleCarga(controleCarga.getNrControleCarga().toString());
			reciboComplementarDTO.setIdFilialControleCarga(controleCarga.getFilialByIdFilialOrigem().getIdFilial());
			reciboComplementarDTO.setSgFilialControleCarga(controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			reciboComplementarDTO.setNmFilialControleCarga(controleCarga.getFilialByIdFilialOrigem().getPessoa().getNmPessoa());
		}
	}	
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getReciboComplementadoDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getReciboComplementado() != null){		
			ReciboFreteCarreteiro reciboComplementado = reciboFreteCarreteiro.getReciboComplementado();
			
			ReciboComplementarDTO reciboComplementadoDTO = new ReciboComplementarDTO();
			reciboComplementadoDTO.setIdReciboFreteCarreteiro(reciboComplementado.getIdReciboFreteCarreteiro());
			reciboComplementadoDTO.setNrReciboFreteCarreteiro(reciboComplementado.getNrReciboFreteCarreteiro());
			reciboComplementadoDTO.setTpSituacaoRecibo(reciboComplementado.getTpSituacaoRecibo());
			reciboComplementadoDTO.setTpReciboFreteCarreteiro(reciboComplementado.getTpReciboFreteCarreteiro());
			
			getFilialDTO(reciboComplementado, reciboComplementadoDTO);
			
			reciboComplementadoDTO.setSgFilial(reciboComplementado.getFilial().getSgFilial());
			
			reciboComplementarDTO.setReciboComplementado(reciboComplementadoDTO);
		}
	}
	
	/**
	 * 
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getModeloMarcaMeioTransporteDTO(ReciboFreteCarreteiro reciboFreteCarreteiro, ReciboComplementarDTO reciboComplementarDTO){
		if(reciboComplementarDTO.getMeioTransporteRodoviario() == null){
			return;
		}
		
		MeioTransporteRodoviario meioTransporteRodoviario = reciboFreteCarreteiro.getMeioTransporteRodoviario();
		MeioTransporte meioTransporte = meioTransporteRodoviario.getMeioTransporte();
		ModeloMeioTransporte modeloMeioTransporte = meioTransporte.getModeloMeioTransporte();
		
		reciboComplementarDTO.getMeioTransporteRodoviario().setDsModeloMeioTransporte(modeloMeioTransporte.getDsModeloMeioTransporte());
		reciboComplementarDTO.getMeioTransporteRodoviario().setDsMarcaMeioTransporte(modeloMeioTransporte.getMarcaMeioTransporte().getDsMarcaMeioTransporte());
		reciboComplementarDTO.getMeioTransporteRodoviario().setDsMarcaMeioTransporte(modeloMeioTransporte.getMarcaMeioTransporte().getDsMarcaMeioTransporte());
	}
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getMeioTransporteRodoviarioDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getMeioTransporteRodoviario() != null){		
			MeioTransporteRodoviario meioTransporteRodoviario = reciboFreteCarreteiro.getMeioTransporteRodoviario();
			MeioTransporte meioTransporte = meioTransporteRodoviario.getMeioTransporte();
			
			/*
			 * Define meio transporte.
			 */
			MeioTransporteSuggestDTO meioTransporteDTO = new MeioTransporteSuggestDTO();
			meioTransporteDTO.setIdMeioTransporte(meioTransporte.getIdMeioTransporte());
			meioTransporteDTO.setNrIdentificador(meioTransporte.getNrIdentificador());
			meioTransporteDTO.setNrFrota(meioTransporte.getNrFrota());
			meioTransporteDTO.setIdMeioTransporte(meioTransporteRodoviario.getIdMeioTransporte());
			
			reciboComplementarDTO.setMeioTransporteRodoviario(meioTransporteDTO);
		}
	}
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getPendenciaDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getPendencia() != null){		
			Pendencia pendencia = reciboFreteCarreteiro.getPendencia();
			
			PendenciaDTO pendenciaDTO = new PendenciaDTO();
			pendenciaDTO.setIdPendencia(pendencia.getIdPendencia());
			pendenciaDTO.setDsPendencia(pendencia.getDsPendencia());
			pendenciaDTO.setTpSituacaoPendencia(pendencia.getTpSituacaoPendencia());
			
			reciboComplementarDTO.setPendencia(pendenciaDTO);
		}
	}
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getUsuarioDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getUsuario() != null){		
			Usuario usuario = reciboFreteCarreteiro.getUsuario();
			
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setIdUsuario(usuario.getIdUsuario());
			usuarioDTO.setNmUsuario(usuario.getNmUsuario());
			
			reciboComplementarDTO.setUsuario(usuarioDTO);
		}
	}
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getMoedaPaisDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getMoedaPais() != null){		
			MoedaPais moedaPais = reciboFreteCarreteiro.getMoedaPais();			
			Moeda moeda = moedaPais.getMoeda();
			
			MoedaDTO moedaDTO = new MoedaDTO();
			moedaDTO.setDsSimbolo(moeda.getDsSimbolo());
			moedaDTO.setSgMoeda(moeda.getSgMoeda());
			moedaDTO.setSiglaSimbolo(moeda.getSiglaSimbolo());
			
			MoedaPaisDTO moedaPaisDTO = new MoedaPaisDTO();
			moedaPaisDTO.setIdMoedaPais(moedaPais.getIdMoedaPais());
			moedaPaisDTO.setMoeda(moedaDTO);
			
			reciboComplementarDTO.setMoedaPais(moedaPaisDTO);
		}
	}
	
	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getMotoristaDTO(ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO) {
		if(reciboFreteCarreteiro.getMotorista() != null){		
			Motorista motorista = reciboFreteCarreteiro.getMotorista();
			
			MotoristaSuggestDTO motoristaDTO = new MotoristaSuggestDTO();
			motoristaDTO.setId(motorista.getIdMotorista());
			
			reciboComplementarDTO.setMotorista(motoristaDTO);
		}
	}

	/**
	 *   
	 * @param resultSetPage
	 * @return List<Map<String, Object>> 
	 */
	public static List<Map<String, Object>> getListForReport(ResultSetPage<ReciboFreteCarreteiro> resultSetPage) {		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		if(resultSetPage == null || resultSetPage.getList().isEmpty()){
			return result;
		}
		
		List<ReciboFreteCarreteiro> list = resultSetPage.getList();
		
		for (ReciboFreteCarreteiro reciboFreteCarreteiro : list) {			
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("tpReciboFreteCarreteiro", reciboFreteCarreteiro.getTpReciboFreteCarreteiro());
			map.put("sgFilialComplementado", reciboFreteCarreteiro.getReciboComplementado().getFilial().getSgFilial());
			map.put("nrReciboFreteCarreteiroComplementado", FormatUtils.formatLongWithZeros(reciboFreteCarreteiro.getReciboComplementado().getNrReciboFreteCarreteiro(), "0000000000"));   				             
			map.put("sgFilialRecibo", reciboFreteCarreteiro.getFilial().getSgFilial());
			map.put("nrReciboFreteCarreteiro", reciboFreteCarreteiro.getNrReciboFreteCarreteiro());
			map.put("dhEmissao", reciboFreteCarreteiro.getDhEmissao());
			map.put("tpSituacaoRecibo", reciboFreteCarreteiro.getTpSituacaoRecibo());
			map.put("nmPessoaProprietario", reciboFreteCarreteiro.getProprietario().getPessoa().getNmPessoa());
			map.put("nrFrota", reciboFreteCarreteiro.getMeioTransporteRodoviario().getMeioTransporte().getNrFrota());
			map.put("nrIdentificadorMeioTransporte", reciboFreteCarreteiro.getMeioTransporteRodoviario().getMeioTransporte().getNrIdentificador());
			map.put("dtPgtoReal", reciboFreteCarreteiro.getDtPagtoReal());
			map.put("sgMoeda", reciboFreteCarreteiro.getMoedaPais().getMoeda().getSgMoeda());
			map.put("dsSimbolo", reciboFreteCarreteiro.getMoedaPais().getMoeda().getDsSimbolo());
			map.put("vlLiquido", getValorFormatado(reciboFreteCarreteiro.getVlLiquido()));
			
			DomainValue tpIdentificacao = reciboFreteCarreteiro.getProprietario().getPessoa().getTpIdentificacao();
			String nrIdentificacao = reciboFreteCarreteiro.getProprietario().getPessoa().getNrIdentificacao();
			
			map.put("tpIdentificacaoProprietario", tpIdentificacao);
			map.put("nrIdentificacaoProprietario", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
			
			if(reciboFreteCarreteiro.getControleCarga() != null){
				map.put("sgFilialControleCarga", reciboFreteCarreteiro.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
				map.put("nrControleCarga", reciboFreteCarreteiro.getControleCarga().getNrControleCarga());
			}
			
			result.add(map);
		}
		
		return result;
	}

	/**
	 * Retorna dados genéricos do recibo. 
	 * 
	 * @param rfc
	 * @param parametro
	 * @param result
	 * @param reciboComplementado
	 * @param complementar
	 */
	private static void getDadosRecibo(ReciboFreteCarreteiro rfc, ReciboComplementarDTO reciboComplementarDTO) {		
		reciboComplementarDTO.setIdReciboFreteCarreteiro(rfc.getIdReciboFreteCarreteiro());
		reciboComplementarDTO.setNrReciboFreteCarreteiro(rfc.getNrReciboFreteCarreteiro());
		reciboComplementarDTO.setTpReciboFreteCarreteiro(rfc.getTpReciboFreteCarreteiro());
		reciboComplementarDTO.setTpSituacaoRecibo(rfc.getTpSituacaoRecibo());
		reciboComplementarDTO.setTpSituacaoWorkflow(rfc.getTpSituacaoWorkflow());
		reciboComplementarDTO.setBlAdiantamento(rfc.getBlAdiantamento());
		reciboComplementarDTO.setVlBruto(rfc.getVlBruto());
		reciboComplementarDTO.setPcAliquotaIssqn(rfc.getPcAliquotaIssqn());
		reciboComplementarDTO.setVlIssqn(rfc.getVlIssqn());
		reciboComplementarDTO.setPcAliquotaInss(rfc.getPcAliquotaInss());
		reciboComplementarDTO.setVlSalarioContribuicao(rfc.getVlSalarioContribuicao());
		reciboComplementarDTO.setVlInss(rfc.getVlInss());
		reciboComplementarDTO.setVlOutrasFontes(rfc.getVlOutrasFontes());
		reciboComplementarDTO.setPcAliquotaIrrf(rfc.getPcAliquotaIrrf());
		reciboComplementarDTO.setVlIrrf(rfc.getVlIrrf());
		reciboComplementarDTO.setVlPremio(rfc.getVlPremio());
		reciboComplementarDTO.setVlPostoPassagem(rfc.getVlPostoPassagem());
		reciboComplementarDTO.setVlDiaria(rfc.getVlDiaria());
		reciboComplementarDTO.setPcAdiantamentoFrete(rfc.getPcAdiantamentoFrete());
		reciboComplementarDTO.setVlLiquido(rfc.getVlLiquido());
		reciboComplementarDTO.setVlDesconto(rfc.getVlDesconto());
		reciboComplementarDTO.setDhGeracaoMovimento(rfc.getDhGeracaoMovimento());
		reciboComplementarDTO.setDhEmissao(rfc.getDhEmissao());
		reciboComplementarDTO.setDtSugeridaPagto(rfc.getDtSugeridaPagto());
		reciboComplementarDTO.setDtProgramadaPagto(rfc.getDtProgramadaPagto());
		reciboComplementarDTO.setDtPagtoReal(rfc.getDtPagtoReal());
		reciboComplementarDTO.setDtContabilizacao(rfc.getDtContabilizacao());
		reciboComplementarDTO.setDtCalculoInss(rfc.getDtCalculoInss());
		reciboComplementarDTO.setNrNfCarreteiro(rfc.getNrNfCarreteiro());
		reciboComplementarDTO.setObReciboFreteCarreteiro(rfc.getObReciboFreteCarreteiro());	
		reciboComplementarDTO.setDhEnvioJde(rfc.getDhEnvioJde());
	}

	/**
	 * @param reciboFreteCarreteiro
	 * @param reciboComplementarDTO
	 */
	private static void getProprietario(ReciboComplementarDTO reciboComplementarDTO, ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if(reciboComplementarDTO.getProprietario() == null){
			return;
		}
		
		ProprietarioDTO proprietarioDTO = reciboComplementarDTO.getProprietario();
		
		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario(proprietarioDTO.getIdProprietario());
		
		reciboFreteCarreteiro.setProprietario(proprietario);	
	}
	
	private static void getMoedaPais(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getMoedaPais() == null) {
			return;
		}
		
		MoedaPaisDTO moedaPaisDTO = reciboComplementarDTO.getMoedaPais();
		
		MoedaPais moedaPais = new MoedaPais();
		moedaPais.setIdMoedaPais(moedaPaisDTO.getIdMoedaPais());
		
		reciboFreteCarreteiro.setMoedaPais(moedaPais);
	}
	
	private static void getUsuario(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getUsuario() == null) {
			return;
		}
		
		UsuarioDTO usuarioDTO = reciboComplementarDTO.getUsuario();
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(usuarioDTO.getIdUsuario());
		
		reciboFreteCarreteiro.setUsuario(usuario);
	}
	
	private static void getMotorista(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getMotorista() == null) {
			return;
		}
		
		MotoristaSuggestDTO motoristaDTO = reciboComplementarDTO.getMotorista();
		
		Motorista motorista = new Motorista();
		motorista.setIdMotorista(motoristaDTO.getId());
		
		reciboFreteCarreteiro.setMotorista(motorista);
	}

	private static void getReciboComplementado(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getReciboComplementado() == null) {
			return;
		}
		
		ReciboComplementarDTO reciboComplementadoDTO = reciboComplementarDTO.getReciboComplementado();
		
		ReciboFreteCarreteiro reciboComplementado = new ReciboFreteCarreteiro();
		reciboComplementado.setIdReciboFreteCarreteiro(reciboComplementadoDTO.getIdReciboFreteCarreteiro());
		reciboComplementado.setTpSituacaoRecibo(reciboComplementadoDTO.getTpSituacaoRecibo());
		reciboComplementado.setTpReciboFreteCarreteiro(reciboComplementadoDTO.getTpReciboFreteCarreteiro());
		
		Filial filial = new Filial();
		filial.setSgFilial(reciboComplementadoDTO.getSgFilial());
		
		reciboComplementado.setFilial(filial);
		
		reciboFreteCarreteiro.setReciboComplementado(reciboComplementado);
	}

	private static void getControleCarga(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getControleCarga() == null) {
			return;
		}
		
		ControleCargaSuggestDTO controleCargaDTO = reciboComplementarDTO.getControleCarga();
		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(controleCargaDTO.getIdControleCarga());
		
		reciboFreteCarreteiro.setControleCarga(controleCarga);
	}

	private static void getMeioTransporteRodoviario(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getMeioTransporteRodoviario() == null) {
			return;
		}
		
		MeioTransporteSuggestDTO meioTransporteRodoviarioDTO = reciboComplementarDTO.getMeioTransporteRodoviario();
		
		MeioTransporte meioTransporte = new MeioTransporte();
		meioTransporte.setIdMeioTransporte(reciboComplementarDTO.getMeioTransporteRodoviario().getIdMeioTransporte());
		meioTransporte.setNrIdentificador(reciboComplementarDTO.getMeioTransporteRodoviario().getNrIdentificador());
		meioTransporte.setNrFrota(reciboComplementarDTO.getMeioTransporteRodoviario().getNrFrota());
		
		MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();
		meioTransporteRodoviario.setIdMeioTransporte(meioTransporteRodoviarioDTO.getIdMeioTransporte());
		meioTransporteRodoviario.setMeioTransporte(meioTransporte);
		
		reciboFreteCarreteiro.setMeioTransporteRodoviario(meioTransporteRodoviario);
	}

	private static void getFilial(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getFilial() == null) {
			return;
		}
		
		FilialSuggestDTO filialDTO = reciboComplementarDTO.getFilial();
		Filial filial = new Filial();
		filial.setIdFilial(filialDTO.getIdFilial());
		filial.setSgFilial(filialDTO.getSgFilial());
		
		reciboFreteCarreteiro.setFilial(filial);
	}
	
	private static void getFilialDestino(ReciboComplementarDTO reciboComplementarDTO,
			ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getFilial() == null) {
			return;
		}
		
		FilialSuggestDTO filialDTO = reciboComplementarDTO.getFilial();
		Filial filial = new Filial();
		filial.setIdFilial(filialDTO.getIdFilial());
		
		reciboFreteCarreteiro.setFilialDestino(filial);
	}

	private static void getPendencia(ReciboComplementarDTO reciboComplementarDTO, ReciboFreteCarreteiro reciboFreteCarreteiro) {
		if (reciboComplementarDTO.getPendencia() == null) {
			return;
		}
		
		PendenciaDTO pendenciaDTO = reciboComplementarDTO.getPendencia();
		Pendencia pendencia = new Pendencia();
		pendencia.setIdPendencia(pendenciaDTO.getIdPendencia());
		
		reciboFreteCarreteiro.setPendencia(pendencia);
	}
	
	/**
	 * Formata valor de acordo com máscara de moeda.
	 * 
	 * @param valor
	 * @return String
	 */
	private static String getValorFormatado(BigDecimal valor) {
		if (valor == null) {
			return FormatUtils.formatDecimal("###,###,##0.00", BigDecimal.ZERO);
		}
		
		return FormatUtils.formatDecimal("###,###,##0.00", valor);
	}
		
	/**
	 * Define estado dos botões/campos de tela.
	 * 
	 * @param reciboComplementarDTO
	 * @param workflow
	 * @param anexos 
	 */
	private static void defineDisabled(
			ReciboFreteCarreteiro reciboFreteCarreteiro,
			ReciboComplementarDTO reciboComplementarDTO, boolean workflow) {
		Map<String, Boolean> situacoes = getSituacoes(reciboFreteCarreteiro.getPendencia(), reciboFreteCarreteiro.getTpSituacaoRecibo().getValue(), workflow);	
		
		reciboComplementarDTO.setWorkflow(workflow);
		
		if ("C".equals(reciboComplementarDTO.getTpReciboFreteCarreteiro().getValue())) {
			defineDisabledColetaEntrega(reciboComplementarDTO, situacoes);
		} else {
			defineDisabledViagem(reciboComplementarDTO, situacoes);
		}
	}

	/**
	 * @param reciboComplementarDTO
	 * @param workflow
	 * 
	 * @return Map<String, Boolean>
	 */
	private static Map<String, Boolean> getSituacoes(Pendencia pendencia, String tpSituacaoRecibo, boolean workflow) {
		Map<String, Boolean> situacoes = ReciboComplementarRestHelper.getSituacoes(tpSituacaoRecibo, workflow, SessionUtils.isFilialSessaoMatriz());		
		
		situacoes.put(ReciboHelperConstants.EVENTO_2401.getValue(), isEvento2401(pendencia));
		situacoes.put(ReciboHelperConstants.EVENTO_2402.getValue(), isEvento2402(pendencia));

		return situacoes;
	}
	
	/**
	 * Verifica se a pendência é do tipo 2401.
	 * 
	 * @param pendencia
	 * @return boolean
	 */
	private static boolean isEvento2401(Pendencia pendencia){
		if(pendencia == null){
			return false;
		}
		
		Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
		
		return ConstantesWorkflow.NR2401_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_ANALISTAS.equals(nrTipoEvento);
	}
	
	/**
	 * Verifica se a pendência é do tipo 2402.
	 * 
	 * @param pendencia
	 * @return boolean
	 */
	private static boolean isEvento2402(Pendencia pendencia){
		if(pendencia == null){
			return false;
		}
		
		Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
		
		return ConstantesWorkflow.NR2402_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_COORDENACAO.equals(nrTipoEvento);
	}
	
	/**
	 * Define estado dos botões/campos de tela quando o tipo do recibo
	 * complementar for de viagem.
	 * 
	 * @param reciboComplementarDTO
	 * @param situacoes
	 */
	private static void defineDisabledViagem(ReciboComplementarDTO reciboComplementarDTO, Map<String, Boolean> situacoes) {
		reciboComplementarDTO.setBtAnexar(true);
		reciboComplementarDTO.setDtProgramadaPagtoC(true);		
		reciboComplementarDTO.setBtOcorrencias(ReciboComplementarRestHelper.isCancelado(situacoes));
		reciboComplementarDTO.setBtControleCarga(ReciboComplementarRestHelper.isCancelado(situacoes));				
		reciboComplementarDTO.setBtEmitir(ReciboComplementarRestHelper.isDesabilitaEmitirViagem(situacoes));
		reciboComplementarDTO.setBtCancelar(ReciboComplementarRestHelper.bloqueiaViagem(situacoes));
		reciboComplementarDTO.setBtSalvar(ReciboComplementarRestHelper.bloqueiaViagem(situacoes));
		reciboComplementarDTO.setBtLimpar(ReciboComplementarRestHelper.bloqueiaViagem(situacoes));
		reciboComplementarDTO.setTxtMoeda(ReciboComplementarRestHelper.bloqueiaViagem(situacoes));
		reciboComplementarDTO.setTxtNrNfCarreteiro(ReciboComplementarRestHelper.bloqueiaViagem(situacoes));
		reciboComplementarDTO.setTxtObservacao(ReciboComplementarRestHelper.bloqueiaViagem(situacoes));
		reciboComplementarDTO.setBtSituacao(!ReciboColetaEntregaHelper.isHabilitaComboSituacao(situacoes));
	}

	/**
	 * Define estado dos botões/campos de tela quando o tipo do recibo
	 * complementar for de coleta/entrega.
	 * 
	 * @param reciboComplementarDTO
	 * @param situacoes
	 */
	private static void defineDisabledColetaEntrega(ReciboComplementarDTO reciboComplementarDTO, Map<String, Boolean> situacoes) {
		reciboComplementarDTO.setBtEmitir(!ReciboComplementarRestHelper.isHabilitaEmitir(situacoes));		
		reciboComplementarDTO.setBtOcorrencias(ReciboComplementarRestHelper.isDesabilitaOcorrencias(situacoes));
		reciboComplementarDTO.setBtControleCarga(ReciboComplementarRestHelper.isDesabilitacontroleCarga(situacoes));
		reciboComplementarDTO.setBtCancelar(!ReciboComplementarRestHelper.isHabilitaBotaoCancelar(situacoes));
		reciboComplementarDTO.setBtSalvar(!ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		reciboComplementarDTO.setBtLimpar(!ReciboComplementarRestHelper.isHabilitaBotaoSalvarLimpar(situacoes));
		reciboComplementarDTO.setTxtNrNfCarreteiro(!ReciboComplementarRestHelper.isHabilitaNFCarreteiro(situacoes));
		reciboComplementarDTO.setTxtObservacao(!ReciboComplementarRestHelper.isHabilitaObservacao(situacoes));
		reciboComplementarDTO.setTxtMoeda(!ReciboComplementarRestHelper.isHabilitaTxtMoeda(situacoes));
		reciboComplementarDTO.setBtAnexar(!ReciboComplementarRestHelper.isHabilitaAnexos(situacoes));
		reciboComplementarDTO.setDtProgramadaPagtoC(!ReciboComplementarRestHelper.isHabilitaDataProgramada(situacoes));
		reciboComplementarDTO.setBtSituacao(!ReciboColetaEntregaHelper.isHabilitaComboSituacao(situacoes));
	}
}