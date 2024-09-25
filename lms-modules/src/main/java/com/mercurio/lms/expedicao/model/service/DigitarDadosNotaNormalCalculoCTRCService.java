package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.gm.model.service.CarregamentoService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.digitarDadosNotaNormalCalculoCTRCService"
 */
public class DigitarDadosNotaNormalCalculoCTRCService {

	private static final String ATIVO = "A";
	private static final String ROTAS_VERDES = "Rotas Verdes";
	private static final String DIVISAO_CENTRO_OESTE = "Centro-Oeste e Norte";
	private static final String REFATURADO = "RF";
	private ConhecimentoNormalService conhecimentoNormalService;
	private ConhecimentoService conhecimentoService;
	private DivisaoClienteService divisaoClienteService;
	private CalcularFreteService calcularFreteService;
	private CotacaoService cotacaoService;
	private FilialService filialService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private DpeService dpeService;
	private VolumeService volumeService;
	private CarregamentoService carregamentoService;
	private ServicoService servicoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private DoctoServicoService doctoServicoService;
	private ClienteService clienteService;
	private static final Short CD_LOCALIZACAO_MERCADORIA = 24;
	private CalculoFreteService calculoFreteService;
	private RestricaoRotaService restricaoRotaService;
	private DoctoServicoPPEPadraoService doctoServicoPPEPadraoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ConhecimentoDevolucaoService conhecimentoDevolucaoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private NotaFiscalOperadaService notaFiscalOperadaService; 
	
	public Map findDivisaoClienteByCTRC(Map parameters) {
		List list = findDivisaoCliente(parameters);
		if (list == null) { 
			return parameters;
		} 
		
		parameters.put("listDivisao", list);
			
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);

		if(list.size() > 1){
			for (Object obj : list) {
				Map map = (Map) obj;
				String nmDivisao = (String) map.get("dsDivisaoCliente");
				String tpSituacao = (String) map.get("tpSituacao");
				if (nmDivisao.toUpperCase().contains(ROTAS_VERDES.toUpperCase()) && tpSituacao.equalsIgnoreCase(ATIVO)){
					Long idDivisaoCliente = map.get("idDivisaoCliente") != null ? Long.valueOf(map.get("idDivisaoCliente").toString()) : null;
					ParametroCliente parametroCliente = calculoFreteService.findParametroCliente(idDivisaoCliente, calculoFrete.getIdServico(), calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino());
					if(parametroCliente != null && parametroCliente.getTabelaPreco() != null){
						calculoFrete.setTabelaPreco(parametroCliente.getTabelaPreco());
						TarifaPrecoRota tarifaPrecoRota = restricaoRotaService.findTarifaPrecoRota(calculoFrete);
						if( tarifaPrecoRota != null && tarifaPrecoRota.getTarifaPreco() != null){
							calculoFrete.setTabelaPreco(null);
							List returnList = new ArrayList();
							returnList.add(map);
							parameters.put("listDivisao", returnList);
							return parameters;
						}
					}
				}
			}
			if (filialService.validateFilialCentroOesteNorteByNrIdentificacaoCliente((Long) parameters.get("idUnidadeFederativaDestinatario"))) {
				for (Object obj : list) {
					Map map = (Map) obj;
					String nmDivisao = (String) map.get("dsDivisaoCliente");
				if (nmDivisao.toUpperCase().contains(DIVISAO_CENTRO_OESTE.toUpperCase())){
				List returnList = new ArrayList();
				returnList.add(map);
				parameters.put("listDivisao", returnList);
					return parameters;
			}
		}
		}
		} 
		
		return parameters;
	}

	private List findDivisaoCliente(Map parameters) {
		CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		Cliente cliente = calculoFrete.getClienteBase();
		if(ClienteUtils.isParametroClienteEspecial(cliente.getTpCliente().getValue())) {
			List divisoes = divisaoClienteService.findDivisaoClienteByIdServico(cliente.getIdCliente(), calculoFrete.getIdServico(), "A");
			if (divisoes != null && !divisoes.isEmpty()) {
				List<Map> result = new ArrayList<Map>();
				for (int i = 0; i < divisoes.size(); i++) {
					DivisaoCliente divisaoCliente = (DivisaoCliente) divisoes.get(i);
					Map<String, Object> dc = new HashMap<String, Object>();
					dc.put("idDivisaoCliente", divisaoCliente.getIdDivisaoCliente());
					dc.put("dsDivisaoCliente", divisaoCliente.getDsDivisaoCliente());
					dc.put("tpSituacao", divisaoCliente.getTpSituacao().getValue());
					if( divisaoCliente.getNaturezaProduto() != null){
						dc.put("idNaturezaProduto", divisaoCliente.getNaturezaProduto().getIdNaturezaProduto());
					}
					result.add(dc);
				}
				return result;
			}
		}
		return null;
	}

	/**
	 * Grava CTRC 1º Fase com o pré calculo de frete.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public Serializable storeCtrcPrimeiraFasePreCalculoFrete(Map parameters) {
		HashMap retorno = conhecimentoNormalService.validateExistenciaPCE((Conhecimento) parameters.get("conhecimento"));
		if(retorno == null) {
			return storeCtrcSegundaFasePreCalculoFrete(parameters);
		}
		return retorno;
	}
	
	/**
	 * Grava CTRC 2º Fase sem executar o calculo de frete.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public Serializable storeCtrcSegundaFasePreCalculoFrete(Map parameters) {
		
		final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		final Map mapMeioTransporte = (HashMap)parameters.get("meioTransporte");
		final Map<String, String> mapSituacaoDescarga = new HashMap<String, String>();
		final MeioTransporte meioTransporte = conhecimentoNormalService.createMeioTransporte(conhecimento, mapMeioTransporte, mapSituacaoDescarga);
		final CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		final Boolean blBloqueiaSubcontratada = (Boolean) parameters.get("blBloqueiaSubcontratada");
		final List<Map> listNotasRefaturadasdata = (List<Map>) parameters.get("listNotasRefaturadas");

		if(parameters.get("nrProcessamento") != null) {
			conhecimento.setNrProcessamento(MapUtils.getLong(parameters, "nrProcessamento").toString());
		}

		Serializable serializable = conhecimentoNormalService
				.storeConhecimentoSemCalculoFrete
					(conhecimento, calculoFrete, meioTransporte, ConstantesExpedicao.TIPO_ICMS,
					mapSituacaoDescarga.get("tpSituacaoDescarga"), conhecimento.calculaQtVolumesTotalFrota(),
					blBloqueiaSubcontratada);

		conhecimentoService.flushConhecimento();

		boolean haveNrChaveNfe = checkNfe(conhecimento.getNotaFiscalConhecimentos());
		
		if(haveNrChaveNfe){
			Long idClienteRemetente = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
			Cliente cliente = clienteService.findById(idClienteRemetente);
			if(cliente.getBlEmissorNfe() == null || !cliente.getBlEmissorNfe()){
				cliente.setBlEmissorNfe(true);
				clienteService.store(cliente);
			}
		}
		
		Conhecimento cto = conhecimentoService.findById((Long)serializable);
		Frete frete = new Frete();
		frete.setConhecimento(cto);
		frete.setCalculoFrete(calculoFrete);
		frete.setTpFrete(cto.getTpCalculoPreco().getValue());
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setBlCalculaServicosAdicionais(Boolean.TRUE);
		calculoFrete.setIdDoctoServico(cto.getIdDoctoServico());
		Map map = calcularFreteService.executeCalcularFreteCTRC(frete);
		
		//LMS-4068 validaValorFreteIcmsCteSubstituto; grava parcelas
		if(ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())){
			validaValorFreteIcmsCteSubstituto(conhecimento, map);
		}else{
			cto.setParcelaDoctoServicos(null); // Calculo de parcelas deve ser calculado somente ao pesar o produto e não na digitação e nem via EDI
		}
		
		
		//LMS-3715
		cto.setVlImpostoPesoDeclarado(cto.getVlImposto());
		cto.setVlIcmsSubstituicaoTributariaPesoDeclarado(cto.getVlIcmsSubstituicaoTributaria());
		
		cto.setVlFretePesoDeclarado((BigDecimal)map.get("vlTotalCtrc"));
		//LMS-463 - Atualiza volumes e data de previsão de entrega.
		if(cto.getBlIndicadorEdi()){
			if (ConhecimentoUtils.isLiberaEtiquetaEdi(cto)){
				cto.setPsAferido(cto.getPsReal());
				if (cto.getDtPrevEntrega() == null){
					Map mapDpe = dpeService.executeCalculoDPE(cto.getClienteByIdClienteRemetente(), 
							cto.getClienteByIdClienteDestinatario(), 
							cto.getClienteByIdClienteBaseCalculo(), 
							cto.getClienteByIdClienteConsignatario(), 
							cto.getClienteByIdClienteRedespacho(), 
							null, 
							cto.getServico().getIdServico(), 
							cto.getMunicipioByIdMunicipioColeta().getIdMunicipio(), 
							cto.getFilialByIdFilialOrigem().getIdFilial(), 
							cto.getFilialByIdFilialDestino().getIdFilial(), 
							cto.getMunicipioByIdMunicipioEntrega().getIdMunicipio(), 
							cto.getNrCepColeta(), 
							cto.getNrCepEntrega(), 
							conhecimento.getDhEmissao() == null ? null : conhecimento.getDhEmissao());
					if (mapDpe != null){
						Long nrDias = (Long)mapDpe.get("nrPrazo");
						if (nrDias != null){
							cto.setNrDiasPrevEntrega(Short.valueOf(nrDias.shortValue()));
						}
						cto.setDtPrevEntrega((YearMonthDay)mapDpe.get("dtPrazoEntrega"));
						// LMS-8779
						doctoServicoPPEPadraoService.updateDoctoServicoPPEPadrao(cto.getIdDoctoServico(),(Long) mapDpe.get("nrDiasColeta"),(Long) mapDpe.get("nrDiasEntrega"),(Long) mapDpe.get("nrDiasTransferencia"));
					}
				}
				
				//Gera os volumes para poder fechar o conhecimento 
				LocalizacaoMercadoria localizacaoMercadoria = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(CD_LOCALIZACAO_MERCADORIA);
				for (NotaFiscalConhecimento nfc :cto.getNotaFiscalConhecimentos()){
					for (VolumeNotaFiscal vnf:(List<VolumeNotaFiscal>)nfc.getVolumeNotaFiscais()){
						if(vnf.getNrVolumeEmbarque() == null){
							vnf.setNrVolumeEmbarque(vnf.getNrVolumeColeta());
						}
						
						if (Boolean.TRUE.equals(cto.getBlPaletizacao())&&
								ConhecimentoUtils.isLiberaEtiquetaEdi(cto)) {
							vnf.setTpVolume(ConstantesExpedicao.TP_VOLUME_UNITARIO);
						}
						
						vnf.setLocalizacaoMercadoria(localizacaoMercadoria);
						vnf.setLocalizacaoFilial(cto.getFilialByIdFilialOrigem());
						vnf.setPsAferido(BigDecimal.ZERO);
					}
				}
				
			}
		}
		
		cto.setVlFretePesoDeclarado((BigDecimal)map.get("vlTotalCtrc"));
		
		conhecimentoNormalService.validateCotacaoByCTRC(cto, calculoFrete);				
		conhecimentoService.store(cto);
		
		boolean isEntregaParcial = false;
		if(ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue()) && 
		        cto.getDoctoServicoOriginal()!= null){
		    isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(cto.getDoctoServicoOriginal().getIdDoctoServico());
		}
		
		if(!isEntregaParcial){
		    /* LMS-1211 */ 
		    //LMS-4068 Não validar para conhecimento substituto / 04.01.01.01N4 Digitar conhecimento substituto
		    if(cto.getDoctoServicoOriginal() != null && (cto.getTpConhecimento() == null  
		            || !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(cto.getTpConhecimento().getValue()))
		            ){
		        DoctoServico doctosOriginal = doctoServicoService.findDoctoServicoById(cto.getDoctoServicoOriginal().getIdDoctoServico());
		        if(doctosOriginal.getBlBloqueado()){
		            Short[] cdOcorrencias = new Short[]{
		                    ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_REFATURADA,
		                    ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_REFATURADA_LEG
		            };
		            List<OcorrenciaDoctoServico> ocorrenciaDoctoList = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoByCdOcorrencia(doctosOriginal.getIdDoctoServico(), cdOcorrencias, false);
		            if(ocorrenciaDoctoList.size() > 0){
		                OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(ConstantesExpedicao.CD_OCORRENCIA_MERCADORIA_REFATURADA);
		                ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(doctosOriginal.getIdDoctoServico(), ocorrenciaPendencia.getIdOcorrenciaPendencia(), null, null);
		            }
		        }
		        else{
		            Short cdLocalizacaoMercadoria = localizacaoMercadoriaService.findById(doctosOriginal.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()).getCdLocalizacaoMercadoria();
		            if(cdLocalizacaoMercadoria.shortValue() != ConstantesExpedicao.CD_MERCADORIA_REFATURADA.shortValue()){
		                throw new BusinessException("LMS-04373");
		            }	
		        }
		    }
		}
		
		//LMS-7141
		if(cto.getTpDocumentoServico().getValue().equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE) 
				|| cto.getTpDocumentoServico().getValue().equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA)){
			if(cto.getDevedorDocServs() != null && !cto.getDevedorDocServs().isEmpty() && cto.getDevedorDocServs().get(0).getCliente().getIdCliente().longValue() == SessionUtils.getFilialSessao().getIdFilial().longValue()){
				throw new BusinessException("LMS-04518");
			}
		}
		
		if(cto.getCotacoes() != null) {
			for (Cotacao cotacao : cto.getCotacoes()) {
				cotacao.setDoctoServico(cto);
			}
			cotacaoService.storeAll(cto.getCotacoes());
		}
		//LMS-2672
		if(parameters.get("liberarEmbarqueSemLabelingSemEDI") != null){
			conhecimentoNormalService.validateContingenciaSemLabelingSemEDI(cto);
		}else{
		    conhecimentoNormalService.validateContingencia(cto);
		}
		
		generateNotasFiscaisOperadas(listNotasRefaturadasdata, cto);
		
		return serializable;
	}

    private void generateNotasFiscaisOperadas(final List<Map> listNotasRefaturadasdata, Conhecimento cto) {
        if(CollectionUtils.isNotEmpty(listNotasRefaturadasdata)){
		    for(Map mapNotaFiscalRefaturada: listNotasRefaturadasdata){
		        Long idNotaFiscalCtoOriginal = (Long) mapNotaFiscalRefaturada.get("id");
		        Boolean blRefaturarNf = (Boolean) mapNotaFiscalRefaturada.get("blRefaturarNf");

		        if(blRefaturarNf){
		            NotaFiscalOperada nfo = new NotaFiscalOperada();
		            nfo.setDoctoServico(cto);
		            nfo.setTpSituacao(new DomainValue(REFATURADO));
		            nfo.setNotaFiscalConhecimentoOriginal(notaFiscalConhecimentoService.findById(idNotaFiscalCtoOriginal));
		            notaFiscalOperadaService.store(nfo);
		        }
		    }
		}
    }

	private boolean checkNfe(List<NotaFiscalConhecimento> notaFiscalConhecimentos) {
		for(NotaFiscalConhecimento notaFiscalConhecimento : notaFiscalConhecimentos){
			String nrChave = notaFiscalConhecimento.getNrChave();
			if(!StringUtils.isEmpty(nrChave)){
				return true;
			}
		}
		return false;
	}

	private void validaValorFreteIcmsCteSubstituto(Conhecimento conhecimento, Map<String, Object> map) {
		if( map != null && !map.isEmpty()){
			
			BigDecimal vlTotalFrete = map.get("vlTotalCtrc") != null && map.get("vlTotalCtrc") instanceof BigDecimal ? (BigDecimal)map.get("vlTotalCtrc") : null;    
			BigDecimal vlIcms = map.get("vlImposto") != null && map.get("vlImposto") instanceof BigDecimal ? (BigDecimal)map.get("vlImposto") : null;    
			
			if(vlTotalFrete != null && conhecimento.getDoctoServicoOriginal() != null &&
				( (conhecimento.getDoctoServicoOriginal().getVlTotalDocServico() != null && vlTotalFrete.compareTo(conhecimento.getDoctoServicoOriginal().getVlTotalDocServico()) > 0 )  
					|| (conhecimento.getDoctoServicoOriginal().getVlImposto() != null && vlIcms.compareTo(conhecimento.getDoctoServicoOriginal().getVlImposto()) > 0) )  ){
				throw new BusinessException("LMS-04450");
			}
		}
	}

	/**
	 * Grava CTRC 1º Fase com o calculo de frete.
	 * @return
	 */
	public Serializable storeCtrcPrimeiraFaseCalculoFrete(Map parameters) {
		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		
		HashMap retorno = null;
		
		try {
			retorno = conhecimentoNormalService.validateExistenciaPCE(conhecimento);
		} catch (BusinessException e) {
			String key = e.getMessageKey();
			if("LMS-01097".equals(key)) {
				retorno = new HashMap<String, Object>();
				retorno.put("idVersaoDescritivoPce", e.getMessageArguments()[0]);
			} else throw e;
		}
		
		if(retorno == null) {
			return storeCtrcSegundaFaseCalculoFrete(parameters);
		}
		return retorno;
	}
	
	/**
	 * Grava CTRC 2º Fase executando o calculo de frete.
	 * @return
	 */
	public Serializable storeCtrcSegundaFaseCalculoFrete(Map parameters) {
		final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		final CalculoFrete calculoFrete = (CalculoFrete) parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		final Map mapMeioTransporte = (HashMap)parameters.get("meioTransporte");

		/** Busca Meio de Transporte */
		final Map<String, String> mapSituacaoDescarga = new HashMap<String, String>();
		final MeioTransporte meioTransporte = conhecimentoNormalService.createMeioTransporte(conhecimento, mapMeioTransporte, mapSituacaoDescarga);
		ExpedicaoUtils.setCalculoFreteInSession(calculoFrete);
		Serializable result = conhecimentoNormalService.storeConhecimento(conhecimento, meioTransporte, ConstantesExpedicao.TIPO_ICMS, mapSituacaoDescarga.get("tpSituacaoDescarga"));
		ExpedicaoUtils.removeCalculoFreteFromSession();

		//LMS-2672
		if(parameters.get("liberarEmbarqueSemLabelingSemEDI") != null){
			conhecimentoNormalService.validateContingenciaSemLabelingSemEDI(conhecimento);
		}else{
		conhecimentoNormalService.validateContingencia(conhecimento);
		}

		return result;
	}
		

	public Conhecimento executeClienteGM(Conhecimento conhecimento) {
		ArrayList<VolumeNotaFiscal> listVolumesNotaFiscal = new ArrayList<VolumeNotaFiscal>();
		if(listVolumesNotaFiscal != null && listVolumesNotaFiscal.size()>0){
			for(VolumeNotaFiscal vnf : listVolumesNotaFiscal){
				Volume volume = (Volume) volumeService.findVolumeByCodigoBarras(vnf.getNrVolumeColeta());
				if(volume != null){
					Carregamento carregamento = (Carregamento) carregamentoService.findById(volume.getCarregamento().getIdCarregamento());
					if(carregamento != null){
						if(carregamento.getTipoCarregamento().equals("D")){
							
							// procurar servico com tipo modal = rodoviário e abrangencia = nacional
							Servico servico = servicoService.findServicoByTpAbrangTpModal("N", "R");
							
							conhecimento.setServico(servico);
							conhecimento.setFilialOrigem(SessionUtils.getFilialSessao());
						}
					}else{
						break;
					}
				}else{
					break;
				}
			}
		}
		
		return conhecimento;
	}

	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}	
	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}
	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}
	public void setVolumeService(VolumeService volumeService) {
		this.volumeService = volumeService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		this.calculoFreteService = calculoFreteService;
	}
	public void setRestricaoRotaService(RestricaoRotaService restricaoRotaService) {
		this.restricaoRotaService = restricaoRotaService;
	}
	public void setDoctoServicoPPEPadraoService(DoctoServicoPPEPadraoService doctoServicoPPEPadraoService) {
		this.doctoServicoPPEPadraoService = doctoServicoPPEPadraoService;
	}
    public void setEventoDocumentoServicoService(
            EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }
    public void setConhecimentoDevolucaoService(
            ConhecimentoDevolucaoService conhecimentoDevolucaoService) {
        this.conhecimentoDevolucaoService = conhecimentoDevolucaoService;
    }
    public void setNotaFiscalConhecimentoService(
            NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }
    public void setNotaFiscalOperadaService(
            NotaFiscalOperadaService notaFiscalOperadaService) {
        this.notaFiscalOperadaService = notaFiscalOperadaService;
    }
}
