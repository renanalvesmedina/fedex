package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoDescontoRfc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.DescontoRfcDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.DescontoRfcFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.ParcelaDescontoRfcDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.TipoDescontoRfcDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.workflow.PendenciaDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class DescontoRfcRestPopulateHelper {

	private static final String ID_TIPO_DESCONTO_RFC = "idTipoDescontoRfc";
	private static final String ID_FILIAL = "idFilial";
	private static final String ID_PROPRIETARIO = "idProprietario";

	/**
	 * Default private constructor
	 */
	private DescontoRfcRestPopulateHelper(){
		
	}
	
	public static TypedFlatMap getFilterMap(TypedFlatMap criteria, DescontoRfcFilterDTO filter){
		criteria.put("nrDescontoRfc", filter.getNrDescontoRfc());
		criteria.put("dtAtualizacaoInicial", filter.getDtAtualizacaoInicial());
		criteria.put("dtAtualizacaoFinal", filter.getDtAtualizacaoFinal());
		criteria.put("dtInicioDescontoInicial", filter.getDtInicioDescontoInicial());
		criteria.put("dtInicioDescontoFinal", filter.getDtInicioDescontoFinal());
		
		if(filter.getTpSituacao() != null){
			criteria.put("tpSituacao", filter.getTpSituacao().getValue());
		}
		
		if(filter.getTpOperacao() != null){
			criteria.put("tpOperacao", filter.getTpOperacao().getValue());
		}
		
		if(filter.getProprietario() != null){
			criteria.put(ID_PROPRIETARIO, filter.getProprietario().getIdProprietario());
		}
		
		if(filter.getFilial() != null){
			criteria.put(ID_FILIAL, filter.getFilial().getIdFilial());
		}
		
		if(filter.getTipoDescontoRfc() != null){
			criteria.put(ID_TIPO_DESCONTO_RFC, MapUtils.getLong(filter.getTipoDescontoRfc(), ID_TIPO_DESCONTO_RFC));
		}
		
		if(filter.getMeioTransporte() != null){
			criteria.put("idMeioTransporte", filter.getMeioTransporte().getIdMeioTransporte());
		}
		
		return criteria;
	}
	
	/**
	 * @param descontoRfc
	 * @return DescontoRfcDTO
	 */
	public static DescontoRfcDTO getDescontoRfcDTO(DescontoRfc descontoRfc, boolean workflow) {
		if(descontoRfc == null){
			return null;
		}
		
		DescontoRfcDTO descontoRfcDTO = new DescontoRfcDTO();		 		
		getDescontoRfcDTO(descontoRfc, descontoRfcDTO);		
		getFilialDTO(descontoRfc, descontoRfcDTO);		
		getProprietarioDTO(descontoRfc, descontoRfcDTO);		
		getReciboFreteCarreteiroDTO(descontoRfc, descontoRfcDTO);		
		getPendenciaDTO(descontoRfc, descontoRfcDTO);		
		getTipoDescontoRfcDTO(descontoRfc, descontoRfcDTO);		
		getControleCargaDTO(descontoRfc, descontoRfcDTO);		
		getMeioTransporteDTO(descontoRfc, descontoRfcDTO);		
		getListParcelasDescontoDTO(descontoRfc, descontoRfcDTO);
		
		defineDisabled(descontoRfcDTO, workflow);
		
		return descontoRfcDTO;
	}
	
	/**
	 * 
	 * @param descontoRfcDTO
	 * 
	 * @return DescontoRfc
	 */
	public static DescontoRfc getDescontoRfc(DescontoRfcDTO descontoRfcDTO){
		DescontoRfc descontoRfc = new DescontoRfc();
		descontoRfc.setIdDescontoRfc(descontoRfcDTO.getIdDescontoRfc());
		descontoRfc.setDtInicioDesconto(descontoRfcDTO.getDtInicioDesconto());		
		descontoRfc.setBlParcelado(descontoRfcDTO.getBlParcelado());
		descontoRfc.setPcDesconto(descontoRfcDTO.getPcDesconto());
		descontoRfc.setNrIdentificacaoSemiReboque(descontoRfcDTO.getNrIdentificacaoSemiReboque());
		descontoRfc.setQtDias(descontoRfcDTO.getQtDias());
		descontoRfc.setQtParcelas(descontoRfcDTO.getQtParcelas());
		descontoRfc.setTpOperacao(descontoRfcDTO.getTpOperacao());
		descontoRfc.setVlSaldoDevedor(descontoRfcDTO.getVlSaldoDevedor());
		descontoRfc.setVlTotalDesconto(descontoRfcDTO.getVlTotalDesconto());
		descontoRfc.setVlFixoParcela(descontoRfcDTO.getVlFixoParcela());
		descontoRfc.setTpSituacao(descontoRfcDTO.getTpSituacao());		
		descontoRfc.setDtAtualizacao(JTDateTimeUtils.getDataAtual());
		descontoRfc.setNrDescontoRfc(descontoRfcDTO.getNrDescontoRfc());
		descontoRfc.setObDesconto(descontoRfcDTO.getObDesconto());
		descontoRfc.setBlPrioritario(descontoRfcDTO.isPrioritario() ? new DomainValue("S") : new DomainValue("N"));
		
		getFilial(descontoRfcDTO, descontoRfc);		
		getReciboFreteCarreteiro(descontoRfcDTO, descontoRfc);		
		getProprietario(descontoRfcDTO, descontoRfc);		
		getTipoDescontoRfc(descontoRfcDTO, descontoRfc);		
		getControleCarga(descontoRfcDTO, descontoRfc);		
		getMeioTransporte(descontoRfcDTO, descontoRfc);
		getParcelas(descontoRfcDTO, descontoRfc);
		
		return descontoRfc;
	}
	
	/**
	 * 
	 * @param resultSetPage
	 * @return List<DescontoRfcDTO>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<DescontoRfcDTO> getListDescontoRfcDTO(ResultSetPage resultSetPage){
		List<DescontoRfcDTO> result = new ArrayList<DescontoRfcDTO>();
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> desconto : list) {
			DescontoRfcDTO descontoRfcDTO = new DescontoRfcDTO();
			descontoRfcDTO.setIdDescontoRfc(MapUtils.getLong(desconto, "idDescontoRfc"));
			descontoRfcDTO.setNrDescontoRfc(MapUtils.getLong(desconto, "nrDescontoRfc"));
			descontoRfcDTO.setVlTotalDesconto(RestPopulateUtils.getBigDecimal(desconto, "vlTotalDesconto"));
			descontoRfcDTO.setVlSaldoDevedor(RestPopulateUtils.getBigDecimal(desconto, "vlSaldoDevedor"));
			descontoRfcDTO.setDtInicioDesconto(RestPopulateUtils.getYearMonthDayFromISO8601(desconto, "dtInicioDesconto"));
			descontoRfcDTO.setDtAtualizacao(RestPopulateUtils.getYearMonthDayFromISO8601(desconto, "dtAtualizacao"));
			descontoRfcDTO.setTpSituacao((DomainValue) MapUtils.getObject(desconto, "tpSituacao"));
						
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setNmFilial(MapUtils.getString(desconto, "nmFantasia"));
			filialDTO.setSgFilial(MapUtils.getString(desconto, "sgFilial"));
			
			descontoRfcDTO.setFilial(filialDTO);
			
			TipoDescontoRfcDTO tipoDescontoRfcDTO = new TipoDescontoRfcDTO();			
			tipoDescontoRfcDTO.setDsTipoDescontoRfc(MapUtils.getString(desconto, "dsTipoDescontoRfc"));
			
			descontoRfcDTO.setTipoDescontoRfc(tipoDescontoRfcDTO);
			
			ProprietarioDTO proprietario = new ProprietarioDTO();			
			proprietario.setNmPessoa(MapUtils.getString(desconto, "nmPessoa"));
			
			descontoRfcDTO.setProprietario(proprietario);
				
			/*
			 * Necessário informar o ID para o framework.
			 */
			descontoRfcDTO.setId(descontoRfcDTO.getIdDescontoRfc());
			
			result.add(descontoRfcDTO);
		}
		
		return result;
	}
	
	/**
	 * Popula uma entidade AnexoDescontoRfc.
	 * 
	 * @param dados
	 * @param data
	 * 
	 * @return AnexoDescontoRfc
	 * 
	 * @throws IOException
	 */
	public static AnexoDescontoRfc getAnexoDescontoRfc(Map<String, Object> dados, byte[] data) {						
		AnexoDescontoRfc anexoDesconto = new AnexoDescontoRfc();
		anexoDesconto.setDsAnexo(MapUtils.getString(dados, "dsAnexo"));
		anexoDesconto.setDhCriacao(RestPopulateUtils.getYearMonthDayFromISO8601(dados, "dhCriacao").toDateTimeAtCurrentTime());
		anexoDesconto.setDcArquivo(data);

		UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		anexoDesconto.setUsuarioLMS(usuarioLMS);
    	
    	return anexoDesconto;
	}

	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getParcelas(DescontoRfcDTO descontoRfcDTO,
			DescontoRfc descontoRfc) {
		List<ParcelaDescontoRfc> listParcelaDescontoRfc = new ArrayList<ParcelaDescontoRfc>();
		
		List<ParcelaDescontoRfcDTO> parcelas = descontoRfcDTO.getParcelas();
		
		for (ParcelaDescontoRfcDTO parcelaDescontoRfcDTO : parcelas) {
			ParcelaDescontoRfc parcelaDescontoRfc = new ParcelaDescontoRfc();
			parcelaDescontoRfc.setDescontoRfc(descontoRfc);
			
			parcelaDescontoRfc.setIdParcelaDescontoRfc(parcelaDescontoRfcDTO.getIdParcelaDescontoRfc());
			parcelaDescontoRfc.setNrParcela(parcelaDescontoRfcDTO.getNumeroParcela());
			parcelaDescontoRfc.setVlParcela(parcelaDescontoRfcDTO.getValor());
			parcelaDescontoRfc.setDtParcela(parcelaDescontoRfcDTO.getData());
			parcelaDescontoRfc.setObParcela(parcelaDescontoRfcDTO.getDescricaoParcela());		
			
			listParcelaDescontoRfc.add(parcelaDescontoRfc);
		}
		
		descontoRfc.setListParcelaDescontoRfc(listParcelaDescontoRfc);
	}
	
	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getFilial(DescontoRfcDTO descontoRfcDTO,
			DescontoRfc descontoRfc) {
		Filial filial = new Filial();
		filial.setIdFilial(descontoRfcDTO.getFilial().getIdFilial());
		
		descontoRfc.setFilial(filial);
	}

	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getMeioTransporte(DescontoRfcDTO descontoRfcDTO,
			DescontoRfc descontoRfc) {
		if(descontoRfcDTO.getMeioTransporte() == null){
			return;
			
		}
		Long idMeioTransporte = descontoRfcDTO.getMeioTransporte().getIdMeioTransporte();

		if(idMeioTransporte != null){
			MeioTransporte meioTransporte = new MeioTransporte();
			meioTransporte.setIdMeioTransporte(idMeioTransporte);
			
			descontoRfc.setMeioTransporte(meioTransporte);
		}
	}

	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getControleCarga(DescontoRfcDTO descontoRfcDTO,
			DescontoRfc descontoRfc) {
		if(descontoRfcDTO.getControleCarga() == null){
			return;
			
		}
		
		Long idControleCarga = descontoRfcDTO.getControleCarga().getIdControleCarga();

		if(idControleCarga != null){
			ControleCarga controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(idControleCarga);
			
			descontoRfc.setControleCarga(controleCarga);
		}
	}

	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getTipoDescontoRfc(DescontoRfcDTO descontoRfcDTO,
			DescontoRfc descontoRfc) {
		if(descontoRfcDTO.getTipoDescontoRfc() == null){
			return;
		}
		
		Long idTipoDescontoRfc = descontoRfcDTO.getTipoDescontoRfc().getIdTipoDescontoRfc();

		if(idTipoDescontoRfc != null){
			TipoDescontoRfc tipoDescontoRfc = new TipoDescontoRfc();
			tipoDescontoRfc.setIdTipoDescontoRfc(idTipoDescontoRfc);
			
			descontoRfc.setTipoDescontoRfc(tipoDescontoRfc);
		}
	}

	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getProprietario(DescontoRfcDTO descontoRfcDTO,DescontoRfc descontoRfc) {
		ProprietarioDTO proprietarioDTO = descontoRfcDTO.getProprietario();
		
		Long idProprietario = proprietarioDTO.getIdProprietario();
		
		if(idProprietario != null){		
			Proprietario proprietario = new Proprietario();
			proprietario.setIdProprietario(idProprietario);
			proprietario.setDtVigenciaInicial(proprietarioDTO.getDtVigenciaInicial());
			proprietario.setDtVigenciaFinal(proprietarioDTO.getDtVigenciaFinal());
			proprietario.setTpSituacao(proprietarioDTO.getTpSituacao());
			
			Pessoa pessoa = new Pessoa();
			pessoa.setNmPessoa(proprietarioDTO.getNmPessoa());
			pessoa.setNrIdentificacao(proprietarioDTO.getNrIdentificacao());
			
			proprietario.setPessoa(pessoa);
			
			descontoRfc.setProprietario(proprietario);
		}
	}

	/**
	 * @param descontoRfcDTO
	 * @param descontoRfc
	 */
	private static void getReciboFreteCarreteiro(DescontoRfcDTO descontoRfcDTO,
			DescontoRfc descontoRfc) {
		if(descontoRfcDTO.getRecibo() == null){
			return;
		}
		
		Long idReciboFreteCarreteiro = descontoRfcDTO.getRecibo().getIdReciboFreteCarreteiro();
		
		if(idReciboFreteCarreteiro != null){		
			ReciboFreteCarreteiro reciboFreteCarreteiro = new ReciboFreteCarreteiro();
			reciboFreteCarreteiro.setIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
			
			descontoRfc.setReciboFreteCarreteiro(reciboFreteCarreteiro);
		}
	}
		
	/**
	 * Define estado dos botões e campos da tela.
	 * 
	 * @param descontoRfcDTO
	 * @param workflow
	 */
	private static void defineDisabled(DescontoRfcDTO descontoRfcDTO, boolean workflow){
		PendenciaDTO pendencia = descontoRfcDTO.getPendencia();
		
		boolean matriz = SessionUtils.isFilialSessaoMatriz();	
		boolean cancelado = descontoRfcDTO.getIdDescontoRfc() != null && "C".equals(descontoRfcDTO.getTpSituacao().getValue());
		boolean finalizado = descontoRfcDTO.getIdDescontoRfc() != null && "F".equals(descontoRfcDTO.getTpSituacao().getValue());
		boolean reprovado = pendencia != null && ConstantesWorkflow.REPROVADO.equals(pendencia.getTpSituacaoPendencia().getValue());
		boolean emAprovacao = pendencia != null && ConstantesWorkflow.EM_APROVACAO.equals(pendencia.getTpSituacaoPendencia().getValue());		
		boolean filialReprovado = !matriz && reprovado;
		boolean matrizAtivo = matriz && !emAprovacao;
		
		boolean disabled = getDisabled(workflow, reprovado, cancelado);		
		boolean cancelar = getCancelar(workflow, cancelado, finalizado, filialReprovado, matrizAtivo); 
				
		descontoRfcDTO.setCancelar(cancelar);
		descontoRfcDTO.setDisabled(disabled);
		descontoRfcDTO.setWorkflow(workflow);
	}


	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getMeioTransporteDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getMeioTransporte() != null){		
			MeioTransporte meioTransporte = descontoRfc.getMeioTransporte();
			
			MeioTransporteSuggestDTO meioTransporteDTO = new MeioTransporteSuggestDTO();
			meioTransporteDTO.setIdMeioTransporte(meioTransporte.getIdMeioTransporte());
			meioTransporteDTO.setNrIdentificador(meioTransporte.getNrIdentificador());
			meioTransporteDTO.setNrFrota(meioTransporte.getNrFrota());
			
			descontoRfcDTO.setMeioTransporte(meioTransporteDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getControleCargaDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getControleCarga() != null){		
			ControleCarga controleCarga = descontoRfc.getControleCarga();
			
			ControleCargaSuggestDTO controleCargaDTO = new ControleCargaSuggestDTO();
			controleCargaDTO.setIdControleCarga(controleCarga.getIdControleCarga());
			controleCargaDTO.setNrControleCarga(controleCarga.getNrControleCarga());
			controleCargaDTO.setDhGeracao(controleCarga.getDhGeracao());						
			controleCargaDTO.setSgFilial(controleCarga.getFilialByIdFilialOrigem().getSgFilial());
			
			descontoRfcDTO.setControleCarga(controleCargaDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getTipoDescontoRfcDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getTipoDescontoRfc() != null){		
			TipoDescontoRfc tipoDescontoRfc = descontoRfc.getTipoDescontoRfc();
			
			TipoDescontoRfcDTO tipoDescontoRfcDTO = new TipoDescontoRfcDTO();
			tipoDescontoRfcDTO.setIdTipoDescontoRfc(tipoDescontoRfc.getIdTipoDescontoRfc());
			tipoDescontoRfcDTO.setDsTipoDescontoRfc(tipoDescontoRfc.getDsTipoDescontoRfc());
			
			descontoRfcDTO.setTipoDescontoRfc(tipoDescontoRfcDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getDescontoRfcDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		descontoRfcDTO.setIdDescontoRfc(descontoRfc.getIdDescontoRfc());    		
		descontoRfcDTO.setNrDescontoRfc(descontoRfc.getNrDescontoRfc());    		
		descontoRfcDTO.setDtAtualizacao(descontoRfc.getDtAtualizacao());
		descontoRfcDTO.setDtInicioDesconto(descontoRfc.getDtInicioDesconto());    		
		descontoRfcDTO.setObDesconto(descontoRfc.getObDesconto());    		
		descontoRfcDTO.setNrIdentificacaoSemiReboque(descontoRfc.getNrIdentificacaoSemiReboque());
		descontoRfcDTO.setTpOperacao(descontoRfc.getTpOperacao());
		descontoRfcDTO.setVlSaldoDevedor(descontoRfc.getVlSaldoDevedor());
		descontoRfcDTO.setVlFixoParcela(descontoRfc.getVlFixoParcela());
		descontoRfcDTO.setVlTotalDesconto(descontoRfc.getVlTotalDesconto());    		
		descontoRfcDTO.setQtDias(descontoRfc.getQtDias());
		descontoRfcDTO.setQtParcelas(descontoRfc.getQtParcelas());
		descontoRfcDTO.setPcDesconto(descontoRfc.getPcDesconto());
		descontoRfcDTO.setTpOperacao(descontoRfc.getTpOperacao());
		descontoRfcDTO.setTpSituacao(descontoRfc.getTpSituacao());
		descontoRfcDTO.setBlParcelado(descontoRfc.getBlParcelado());
		descontoRfcDTO.setPrioritario(descontoRfc.getBlPrioritario() == null ? false : "S".equals(descontoRfc.getBlPrioritario().getValue()));
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getReciboFreteCarreteiroDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getReciboFreteCarreteiro() != null){		
			ReciboFreteCarreteiro reciboFreteCarreteiro = descontoRfc.getReciboFreteCarreteiro();
			
			ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiroDTO = new ReciboFreteCarreteiroSuggestDTO();
			reciboFreteCarreteiroDTO.setIdReciboFreteCarreteiro(reciboFreteCarreteiro.getIdReciboFreteCarreteiro());
			reciboFreteCarreteiroDTO.setNrReciboFreteCarreteiro(reciboFreteCarreteiro.getNrReciboFreteCarreteiro());			
			reciboFreteCarreteiroDTO.setSgFilial(reciboFreteCarreteiro.getFilial().getSgFilial());
						
			descontoRfcDTO.setRecibo(reciboFreteCarreteiroDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getListParcelasDescontoDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getListParcelaDescontoRfc() != null){	
			List<ParcelaDescontoRfcDTO> listParcelaDescontoRfcDTO = new ArrayList<ParcelaDescontoRfcDTO>();
			
			List<ParcelaDescontoRfc> listParcelaDescontoRfc = descontoRfc.getListParcelaDescontoRfc();
			
			for (ParcelaDescontoRfc parcelaDescontoRfc : listParcelaDescontoRfc) {
				ParcelaDescontoRfcDTO parcelaDescontoRfcDTO = new ParcelaDescontoRfcDTO();
				parcelaDescontoRfcDTO.setNumeroParcela(parcelaDescontoRfc.getNrParcela());				
				parcelaDescontoRfcDTO.setData(parcelaDescontoRfc.getDtParcela());
				parcelaDescontoRfcDTO.setValor(parcelaDescontoRfc.getVlParcela());
				parcelaDescontoRfcDTO.setDescricaoParcela(parcelaDescontoRfc.getObParcela());
				
				if(parcelaDescontoRfc.getReciboFreteCarreteiro() != null){
					ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiroDTO = new ReciboFreteCarreteiroSuggestDTO();
					reciboFreteCarreteiroDTO.setIdReciboFreteCarreteiro(parcelaDescontoRfc.getReciboFreteCarreteiro().getIdReciboFreteCarreteiro());
					reciboFreteCarreteiroDTO.setNrReciboFreteCarreteiro(parcelaDescontoRfc.getReciboFreteCarreteiro().getNrReciboFreteCarreteiro());					
					reciboFreteCarreteiroDTO.setSgFilial(parcelaDescontoRfc.getReciboFreteCarreteiro().getFilial().getSgFilial());
					
					parcelaDescontoRfcDTO.setRecibo(reciboFreteCarreteiroDTO);
				}				
				
				listParcelaDescontoRfcDTO.add(parcelaDescontoRfcDTO);
			}
			
			descontoRfcDTO.setParcelas(listParcelaDescontoRfcDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getPendenciaDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getPendencia() != null){		
			Pendencia pendencia = descontoRfc.getPendencia();
			
			PendenciaDTO pendenciaDTO = new PendenciaDTO();
			pendenciaDTO.setIdPendencia(pendencia.getIdPendencia());
			pendenciaDTO.setDsPendencia(pendencia.getDsPendencia());
			pendenciaDTO.setTpSituacaoPendencia(pendencia.getTpSituacaoPendencia());
			
			descontoRfcDTO.setPendencia(pendenciaDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getProprietarioDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getProprietario() != null){		
			Proprietario proprietario = descontoRfc.getProprietario();
			
			ProprietarioDTO proprietarioDTO = new ProprietarioDTO();
			proprietarioDTO.setIdProprietario(proprietario.getIdProprietario());
			proprietarioDTO.setNmPessoa( proprietario.getPessoa().getNmPessoa());
			proprietarioDTO.setNrIdentificacao(proprietario.getPessoa().getNrIdentificacao());
			proprietarioDTO.setTpSituacao(proprietario.getTpSituacao());
			
			descontoRfcDTO.setProprietario(proprietarioDTO);
		}
	}

	/**
	 * @param descontoRfc
	 * @param descontoRfcDTO
	 */
	private static void getFilialDTO(DescontoRfc descontoRfc,
			DescontoRfcDTO descontoRfcDTO) {
		if(descontoRfc.getFilial() != null){		
			Filial filial = descontoRfc.getFilial();
			
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setIdFilial(filial.getIdFilial());
			filialDTO.setNmFilial(filial.getPessoa().getNmFantasia());
			filialDTO.setSgFilial(filial.getSgFilial());
			
			descontoRfcDTO.setFilial(filialDTO);
		}
	}
	
	/**
	 * Só é possivel habilitar o botão cancelar:
	 * <p>
	 * Se o registro já não estiver cancelado<br>
	 * Se for matriz e não estiver em aprovação de workflow<br>
	 * Se não for matriz e estiver reprovado.
	 * 
	 * @param workflow
	 * @param cancelado
	 * @param finalizado
	 * @param filialReprovado
	 * @param matrizAtivo
	 * @return boolean
	 */
	private static boolean getCancelar(boolean workflow, boolean cancelado, boolean finalizado,
			boolean filialReprovado, boolean matrizAtivo) {
		if(workflow || cancelado || finalizado){
			return true;
		}
		
		if(filialReprovado){
			return false;
		}
		
		if(matrizAtivo){
			return false;
		}
		
		return true;
	}

	/**
	 * O cadastro fica desabilitado para alteração quando o desconto já foi
	 * ativado pela matriz.
	 * 
	 * @param workflow
	 * @param reprovado
	 * @return boolean
	 */
	private static boolean getDisabled(boolean workflow, boolean reprovado, boolean cancelado) {
		return workflow || !reprovado || cancelado;
	}

	/**
	 *   
	 * @param resultSetPage
	 * @return List<Map<String, Object>> 
	 */
	public static List<Map<String, Object>> getListForReport(ResultSetPage<Map<String, Object>> resultSetPage) {		
		if(resultSetPage == null || resultSetPage.getList().isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> map : list) {
			DomainValue tpIdentificacao = (DomainValue) map.get("tpIdentificacao");
			String nrIdentificacao = (String) map.get("nrIdentificacao");
			
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));			
			map.put("nrDescontoRfc", FormatUtils.formatLongWithZeros(MapUtils.getLong(map, "nrDescontoRfc"), "0000000000"));
			map.put("vlTotalDesconto", getValorFormatado((BigDecimal) MapUtils.getObject(map, "vlTotalDesconto")));
			map.put("vlSaldoDevedor", getValorFormatado((BigDecimal) MapUtils.getObject(map, "vlSaldoDevedor")));
		}
		
		return list;
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
}