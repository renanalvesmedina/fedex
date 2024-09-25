package com.mercurio.lms.expedicao.model.service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.cte.model.v300.CTe;
import com.mercurio.lms.cte.model.v300.ComData;
import com.mercurio.lms.cte.model.v300.Comp;
import com.mercurio.lms.cte.model.v300.Compl;
import com.mercurio.lms.cte.model.v300.Dest;
import com.mercurio.lms.cte.model.v300.DestChoice;
import com.mercurio.lms.cte.model.v300.DocAnt;
import com.mercurio.lms.cte.model.v300.EmiDocAnt;
import com.mercurio.lms.cte.model.v300.EmiDocAntChoice;
import com.mercurio.lms.cte.model.v300.EmiDocAntSequence;
import com.mercurio.lms.cte.model.v300.Emit;
import com.mercurio.lms.cte.model.v300.EnderDest;
import com.mercurio.lms.cte.model.v300.EnderEmit;
import com.mercurio.lms.cte.model.v300.EnderExped;
import com.mercurio.lms.cte.model.v300.EnderReceb;
import com.mercurio.lms.cte.model.v300.EnderReme;
import com.mercurio.lms.cte.model.v300.EnderToma;
import com.mercurio.lms.cte.model.v300.Entrega;
import com.mercurio.lms.cte.model.v300.EntregaChoice;
import com.mercurio.lms.cte.model.v300.EntregaChoice2;
import com.mercurio.lms.cte.model.v300.Exped;
import com.mercurio.lms.cte.model.v300.ExpedChoice;
import com.mercurio.lms.cte.model.v300.Fluxo;
import com.mercurio.lms.cte.model.v300.ICMS;
import com.mercurio.lms.cte.model.v300.ICMS00;
import com.mercurio.lms.cte.model.v300.ICMS20;
import com.mercurio.lms.cte.model.v300.ICMS45;
import com.mercurio.lms.cte.model.v300.ICMS60;
import com.mercurio.lms.cte.model.v300.ICMS90;
import com.mercurio.lms.cte.model.v300.ICMSOutraUF;
import com.mercurio.lms.cte.model.v300.ICMSUFFim;
import com.mercurio.lms.cte.model.v300.IdDocAnt;
import com.mercurio.lms.cte.model.v300.IdDocAntEle;
import com.mercurio.lms.cte.model.v300.IdDocAntPap;
import com.mercurio.lms.cte.model.v300.Ide;
import com.mercurio.lms.cte.model.v300.IdeChoice;
import com.mercurio.lms.cte.model.v300.Imp;
import com.mercurio.lms.cte.model.v300.InfCTeNorm;
import com.mercurio.lms.cte.model.v300.InfCarga;
import com.mercurio.lms.cte.model.v300.InfCte;
import com.mercurio.lms.cte.model.v300.InfCteChoice;
import com.mercurio.lms.cte.model.v300.InfCteComp;
import com.mercurio.lms.cte.model.v300.InfCteSub;
import com.mercurio.lms.cte.model.v300.InfCteSubChoice;
import com.mercurio.lms.cte.model.v300.InfCTeSupl;
import com.mercurio.lms.cte.model.v300.InfDoc;
import com.mercurio.lms.cte.model.v300.InfModal;
import com.mercurio.lms.cte.model.v300.InfNF;
import com.mercurio.lms.cte.model.v300.InfNFe;
import com.mercurio.lms.cte.model.v300.InfOutros;
import com.mercurio.lms.cte.model.v300.InfQ;
import com.mercurio.lms.cte.model.v300.ObsCont;
import com.mercurio.lms.cte.model.v300.Pass;
import com.mercurio.lms.cte.model.v300.Receb;
import com.mercurio.lms.cte.model.v300.RecebChoice;
import com.mercurio.lms.cte.model.v300.RefNF;
import com.mercurio.lms.cte.model.v300.RefNFChoice;
import com.mercurio.lms.cte.model.v300.Rem;
import com.mercurio.lms.cte.model.v300.RemChoice;
import com.mercurio.lms.cte.model.v300.Rodo;
import com.mercurio.lms.cte.model.v300.SemHora;
import com.mercurio.lms.cte.model.v300.Toma3;
import com.mercurio.lms.cte.model.v300.Toma4;
import com.mercurio.lms.cte.model.v300.Toma4Choice;
import com.mercurio.lms.cte.model.v300.Toma4Sequence;
import com.mercurio.lms.cte.model.v300.TomaICMS;
import com.mercurio.lms.cte.model.v300.VPrest;
import com.mercurio.lms.cte.model.v300.types.CSTType;
import com.mercurio.lms.cte.model.v300.types.CUnidType;
import com.mercurio.lms.cte.model.v300.types.IndIETomaType;
import com.mercurio.lms.cte.model.v300.types.RetiraType;
import com.mercurio.lms.cte.model.v300.types.TAmb;
import com.mercurio.lms.cte.model.v300.types.TCodUfIBGE;
import com.mercurio.lms.cte.model.v300.types.TDocAssoc;
import com.mercurio.lms.cte.model.v300.types.TFinCTe;
import com.mercurio.lms.cte.model.v300.types.TModCT;
import com.mercurio.lms.cte.model.v300.types.TModDoc;
import com.mercurio.lms.cte.model.v300.types.TModNF;
import com.mercurio.lms.cte.model.v300.types.TModTransp;
import com.mercurio.lms.cte.model.v300.types.TProcEmi;
import com.mercurio.lms.cte.model.v300.types.TUF_sem_EX;
import com.mercurio.lms.cte.model.v300.types.TUf;
import com.mercurio.lms.cte.model.v300.types.TomaType;
import com.mercurio.lms.cte.model.v300.types.TpDocType;
import com.mercurio.lms.cte.model.v300.types.TpEmisType;
import com.mercurio.lms.cte.model.v300.types.TpHorType;
import com.mercurio.lms.cte.model.v300.types.TpImpType;
import com.mercurio.lms.cte.model.v300.types.TpPerType;
import com.mercurio.lms.cte.model.v300.types.TpServType;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.AliquotaFundoCombatePobreza;
import com.mercurio.lms.tributos.model.AliquotaIcmsInterna;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.service.AliquotaFundoCombatePobrezaService;
import com.mercurio.lms.tributos.model.service.AliquotaIcmsInternaService;
import com.mercurio.lms.tributos.model.service.CalcularIcmsService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CastorSingleton;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DeParaXmlCteService;

/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.gerarConhecimentoEletronicoXML300Service"
 */
public class GerarConhecimentoEletronicoXML300Service {
	
	private static final String GERA_ICMSUFFIM_CTE = "GERA_ICMSUFFIM_CTE";
	private static final String GERA_TAG_COMBATE_POBREZA_CTE = "GERA_TAG_COMBATE_POBREZA_CTE";
	private static final String INCLUIR_QRCODE_CTE = "INCLUIR_QRCODE_CTE";
	private static final String LINK_QRCODE_CTE = "LINK_QRCODE_CTE";
	
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ProprietarioService proprietarioService;
	private ParametroGeralService parametroGeralService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private CalcularIcmsService calcularIcmsService; 
	private DoctoServicoService doctoServicoService;
	private DeParaXmlCteService deParaXmlCteService;
	private ConfiguracoesFacade configuracoesFacade;
	private ContingenciaService contingenciaService;
	private UnidadeFederativaService unidadeFederativaService;
	private AliquotaFundoCombatePobrezaService aliquotaFundoCombatePobrezaService;
	private AliquotaIcmsInternaService aliquotaIcmsInternaService;
	private DomainValueService domainValueService;
	private ClienteService clienteService;
	private InscricaoEstadualService inscricaoEstadualService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDeParaXmlCteService(DeParaXmlCteService deParaXmlCteService) {
		this.deParaXmlCteService = deParaXmlCteService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	private static final  DateTimeFormatter fmtComHora = DateTimeFormat.forPattern("yyyy'-'MM'-'dd'T'HH':'mm':'ssZZ");
	private static final  DateTimeFormatter fmtSemHora = DateTimeFormat.forPattern("yyyy'-'MM'-'dd");
	private static final  DateTimeFormatter fmtAnoMes = DateTimeFormat.forPattern("yyMM");
	private static final  DecimalFormat formatDoisDecimais = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(new Locale("pt_br")));
	private static final  DecimalFormat formatTresDecimais = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(new Locale("pt_br")));
	private static final  DecimalFormat formatQuatroDecimais = new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(new Locale("pt_br")));
	private static final  String cdIbgeExterior = "9999999";
	private static final  String cnpjExterior = "00000000000000";
	private static final  String psMercadoria = "0.001";
	private final BigDecimal PIS = new BigDecimal("0.0165");
	private final BigDecimal COFINS = new BigDecimal("0.076");
	
	public String gerarXML(final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico, Contingencia emContingencia) {
		CTe cte = new CTe();
		StringWriter stringWriter;
		try {
			gerarInfCte(cte, conhecimento, monitoramentoDocEletronico, emContingencia);
			gerarInfCteSupl(cte, conhecimento, monitoramentoDocEletronico);
			stringWriter = new StringWriter();
			Marshaller marshaller = CastorSingleton.getInstance().getContext().createMarshaller();
			marshaller.setWriter(stringWriter);
			marshaller.setValidation(true);
			marshaller.setSuppressXSIType(true);
			marshaller.marshal(cte);
		} catch (XMLException e) {
			throw new BusinessException("LMS-04371", new Object[] { e.getMessage() });
		} catch (IOException e) {
			throw new BusinessException("LMS-04371", new Object[] { e.getMessage() });
		}
		
		StringBuffer xml = stringWriter.getBuffer();
		
		integracaoNDDigitalService.executeAdicionarB2B(xml, conhecimento);
		
		return xml.toString();
	}
	
	public void gerarICMSUFFim(Imp imp, Conhecimento cto) {
		ICMSUFFim icmsufFim = null;
		String geraICMSUfFimCte = String.valueOf(configuracoesFacade.getValorParametro(GERA_ICMSUFFIM_CTE));
		String geraTagCombatePobrezaCte = String.valueOf(configuracoesFacade.getValorParametro(GERA_TAG_COMBATE_POBREZA_CTE));
		
		if ("S".equalsIgnoreCase(geraICMSUfFimCte) || "S".equals(geraTagCombatePobrezaCte)) {
			icmsufFim = getICMSUFFimValoresPadrao(cto);
			UnidadeFederativa ufFim = getUFFim(cto);
			
			if("S".equalsIgnoreCase(geraICMSUfFimCte)){
				executePreenchimentoTagsICMSUFFim(cto, icmsufFim, ufFim.getIdUnidadeFederativa());
			}
			
			if("S".equals(geraTagCombatePobrezaCte)){
				executePreenchimentoTagsCombatePobreza(cto, icmsufFim, ufFim.getIdUnidadeFederativa());
			}
		}
		
		imp.setICMSUFFim(icmsufFim);
	}
	
	private ICMSUFFim getICMSUFFimValoresPadrao(Conhecimento cto){
		ICMSUFFim icmsufFim = new ICMSUFFim();
		icmsufFim.setVBCUFFim(formatDoisDecimais.format(BigDecimal.ZERO));
		icmsufFim.setPICMSUFFim(formatDoisDecimais.format(BigDecimal.ZERO));
		icmsufFim.setPICMSInter(formatDoisDecimais.format(BigDecimal.ZERO));
		icmsufFim.setVICMSUFFim(formatDoisDecimais.format(BigDecimal.ZERO));
		icmsufFim.setVICMSUFIni(formatDoisDecimais.format(BigDecimal.ZERO));
		icmsufFim.setPFCPUFFim(formatDoisDecimais.format(BigDecimal.ZERO));
		icmsufFim.setVFCPUFFim(formatDoisDecimais.format(BigDecimal.ZERO));
		return icmsufFim;
	}
	
	private void executePreenchimentoTagsICMSUFFim(Conhecimento cto, ICMSUFFim icmsufFim, Long idUFFim) {
		AliquotaIcmsInterna aii = aliquotaIcmsInternaService.findByIdUnidadeFederativa(idUFFim);
		Boolean hasTTINaoContribuinte = tipoTributacaoIEService.validateTipotributacaoIENaoContribuinte(cto.getClienteByIdClienteDestinatario().getIdCliente());

		if (BooleanUtils.isTrue(hasTTINaoContribuinte) && aii.getPcAliquota().compareTo(cto.getPcAliquotaIcms()) >= 0) {
			Long idUfFilialOrigem = cto.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();

			if ((BigDecimalUtils.hasValue(cto.getVlImposto()) || BigDecimalUtils.hasValue(cto.getVlIcmsSubstituicaoTributaria())) && !idUfFilialOrigem.equals(idUFFim)
					&& ConstantesExpedicao.TP_FRETE_FOB.equals(cto.getTpFrete().getValue())) {

				ParametroGeral pg = parametroGeralService.findPGPcIcmsUfFimByAno(Long.valueOf(cto.getDhEmissao().getYear()));
				icmsufFim.setVBCUFFim(formatDoisDecimais.format(cto.getVlBaseCalcImposto()));
				icmsufFim.setPICMSUFFim(formatDoisDecimais.format(aii.getPcAliquota()));
				icmsufFim.setPICMSInter(formatDoisDecimais.format(cto.getPcAliquotaIcms()));
				BigDecimal vICMSUFFim = calculateVlIcmsUfFim(cto, aii, pg);
				icmsufFim.setVICMSUFFim(formatDoisDecimais.format(vICMSUFFim));
				BigDecimal vICMSUFIni = calculateVlIcmsUFIni(cto, aii, vICMSUFFim);
				icmsufFim.setVICMSUFIni(formatDoisDecimais.format(vICMSUFIni));
			}
		}
	}

	private String getVersaoCTEConhecimento(final Conhecimento conhecimento) {
        return String.valueOf(conteudoParametroFilialService.findConteudoByNomeParametroWithException(conhecimento.getFilialByIdFilialOrigem().getIdFilial(), "Versão_XML_CTe", false));
    }

	private void executePreenchimentoTagsCombatePobreza(Conhecimento cto, ICMSUFFim icmsufFim, Long idUFFim) {
		AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza = aliquotaFundoCombatePobrezaService.findAliquotaFundoCombatePobrezaVigenteByUf(idUFFim);

		if (aliquotaFundoCombatePobreza != null) {
			icmsufFim.setPFCPUFFim(formatDoisDecimais.format(aliquotaFundoCombatePobreza.getPcAliquota()));
			icmsufFim.setVFCPUFFim(formatDoisDecimais.format(cto.getVlBaseCalcImposto().multiply(BigDecimalUtils.percent(aliquotaFundoCombatePobreza.getPcAliquota()))));
		} 
	}

	private UnidadeFederativa getUFFim(Conhecimento cto) {
		UnidadeFederativa ufFim = cto.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
		if (StringUtils.isNotBlank(cto.getDsEnderecoEntrega())) {
			ufFim = cto.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa();
		}
		
		return ufFim;
	}

	private BigDecimal calculateVlIcmsUFIni(Conhecimento cto, AliquotaIcmsInterna aii, BigDecimal vICMSUFFim) {
		return calcularteVlBaseCalcImposto(cto, aii).subtract(vICMSUFFim);
	}

	private BigDecimal calculateVlIcmsUfFim(Conhecimento cto, AliquotaIcmsInterna aii, ParametroGeral pg) {
		BigDecimal pcPartilhaUfFim = BigDecimalUtils.getBigDecimal(pg.getDsConteudo());
		
		return calcularteVlBaseCalcImposto(cto, aii).multiply(pcPartilhaUfFim.divide(BigDecimalUtils.HUNDRED));
	}

	private BigDecimal calcularteVlBaseCalcImposto(Conhecimento cto, AliquotaIcmsInterna aii) {
		BigDecimal vlBaseCalcImposto = cto.getVlBaseCalcImposto();
		BigDecimal dsPcAliquotaIcms = cto.getPcAliquotaIcms();
		BigDecimal aiiPcAliquota = aii.getPcAliquota();		
		return vlBaseCalcImposto.multiply(aiiPcAliquota.subtract(dsPcAliquotaIcms)).divide(BigDecimalUtils.HUNDRED, MathContext.DECIMAL128);
	}

	private void gerarInfCte(final CTe cte, final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico, Contingencia emContingencia) throws ValidationException {
		InfCte infCte = new InfCte();
		infCte.setVersao("3.00");
		
		Integer random;
		String chave = "";
		char  tpEmis = buscaTipoServico(conhecimento.getFilialByIdFilialOrigem().getIdFilial()).charAt(0);
		
		if(monitoramentoDocEletronico.getNrChave() == null){
			random = getRandom();
		
			chave = gerarChaveAcesso(conhecimento, random);
			monitoramentoDocEletronico.setNrChave(chave.substring(3, chave.length()));
		} else {
			chave = "CTe" + monitoramentoDocEletronico.getNrChave();
		
			if (emContingencia != null) {
				chave = ajustaChave(chave, conhecimento);
			}
			random = Integer.valueOf(chave.substring(38, 46)); 
		}
		
		infCte.setId(chave);
		
		//Add observação sobre a contingencia
		if (emContingencia != null) {
			DoctoServico doctoServico = doctoServicoService.findById(conhecimento.getIdDoctoServico());
			String dsObservacao = parametroGeralService.findSimpleConteudoByNomeParametro("OBS_CONTING_CTE");
			doctoServico.addObservacaoDoctoServico(ConhecimentoUtils.createObservacaoDocumentoServico(doctoServico, dsObservacao, true, null));
			
			monitoramentoDocEletronico.setDsObservacao(dsObservacao);
			monitoramentoDocEletronico.setNrChave(chave.substring(3, chave.length()));
		}
		//
		
		Long idPaisBrasil = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_PAIS_BRASIL"));
		
		String valorTotalTributo = calcularValorTotalTrib(conhecimento);
		
		gerarIde(infCte, conhecimento, random, tpEmis, chave.charAt(46), idPaisBrasil);
		gerarEmit(infCte, conhecimento);
		gerarRem(infCte, conhecimento, idPaisBrasil);
		gerarExped(infCte, conhecimento, idPaisBrasil);
		gerarReceb(infCte, conhecimento, idPaisBrasil);
		gerarDest(infCte, conhecimento, idPaisBrasil);
		gerarVPrest(infCte, conhecimento);
		gerarImp(infCte, conhecimento, valorTotalTributo);
		gerarCompl(infCte, conhecimento, valorTotalTributo);
		
		gerarInfCteChoice(infCte, conhecimento);
		
		cte.setInfCte(infCte);
	}
	
	private void gerarInfCteSupl(final CTe cte, final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico) throws ValidationException {
		
		String incluirQrcodeCte = (String) conteudoParametroFilialService.findConteudoByNomeParametro(conhecimento.getFilialByIdFilialOrigem().getIdFilial(), INCLUIR_QRCODE_CTE, false);
		
		if("S".equalsIgnoreCase(incluirQrcodeCte)){
			
			String linkQrCodeCte = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(conhecimento.getFilialByIdFilialOrigem().getIdFilial(), LINK_QRCODE_CTE, false);
			String chCTe = monitoramentoDocEletronico.getNrChave();
			String tpAmb = String.valueOf(parametroGeralService.findConteudoByNomeParametro("AMBIENTE_CTE", false));
			
			String qrCodCTe = linkQrCodeCte.replace("{0}", chCTe).replace("{1}", tpAmb);
			
			InfCTeSupl infCTeSupl = new InfCTeSupl();
			infCTeSupl.setQrCodCTe(qrCodCTe);
			cte.setInfCTeSupl(infCTeSupl);
		}
		
	}

	private void gerarInfCteSub(InfCTeNorm infCTeNorm, Conhecimento conhecimento) {
		
		if(conhecimento == null || conhecimento.getTpConhecimento() == null 
				|| !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())
				|| conhecimento.getDoctoServicoOriginal() == null){
			return; 
		}
		
		MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(conhecimento.getDoctoServicoOriginal().getIdDoctoServico());
		
		InfCteSub infCteSub = new InfCteSub();
		infCteSub.setChCte(mde.getNrChave());
		
		TomaICMS tomaICMS = new TomaICMS();
		tomaICMS.setRefNFe(conhecimento.getNrChaveNfAnulacao());
		
		InfCteSubChoice infCteSubChoice = new InfCteSubChoice();
		infCteSubChoice.setTomaICMS(tomaICMS);
		
		infCteSub.setInfCteSubChoice(infCteSubChoice);
		
		if(conhecimento.getNrNfAnulacao() != null){
			RefNF refNF = new RefNF();
			
			RefNFChoice refNFChoice = new RefNFChoice();			
			refNFChoice.setCNPJ(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
			refNF.setRefNFChoice(refNFChoice);
			
			refNF.setMod(TModDoc.VALUE_0);
			refNF.setSerie(conhecimento.getDsSerieAnulacao() != null ? conhecimento.getDsSerieAnulacao() : "0" );
			refNF.setNro(conhecimento.getNrNfAnulacao().toString());
			refNF.setValor(formatDoisDecimais.format(conhecimento.getVlNfAnulacao()));
			refNF.setDEmi(fmtSemHora.print(conhecimento.getDtEmissaoNfAnulacao()));
			
			tomaICMS.setRefNF(refNF);
		}
		
		infCTeNorm.setInfCteSub(infCteSub);
	}

	private String ajustaChave(String work, Conhecimento conhecimento) {
		String chave = "";
		
		String fistPart = work.substring(0, 37);
		String tpEmis = buscaTipoServico(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		String lastPArt = work.substring(38, 46);
		
		chave = fistPart + tpEmis + lastPArt;
		chave = chave + modulo11(chave.substring(3, chave.length()).toString());
		
		return chave;
	}

	
	
	/**
	 * Gera 'Abort' padrão NDDigital.
	 * 
	 * @param monitoramentoDocEletronico
	 * @param chave 
	 * @return
	 */
	public String gerarXmlAbort(final MonitoramentoDocEletronico monitoramentoDocEletronico) {
		StringBuilder sb = new StringBuilder();
		sb.append("<abort>");
		sb.append("<chave>");
		sb.append(monitoramentoDocEletronico.getNrChave());
		sb.append("</chave>");
		sb.append("</abort>");
		
		return sb.toString();
		
	}

	
	private void gerarReceb(final InfCte infCte, final Conhecimento conhecimento, final Long idPaisBrasil) {
		if(conhecimento.getClienteByIdClienteConsignatario() != null){
			Receb receb = new Receb();
			
			RecebChoice recebChoice = new RecebChoice();
			if(idPaisBrasil.equals(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
				if("J".equals(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getTpPessoa().getValue())){
					recebChoice.setCNPJ(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getNrIdentificacao());
				} else {
					recebChoice.setCPF(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getNrIdentificacao());
				}
			} else {
				recebChoice.setCNPJ(cnpjExterior);
			}
			receb.setRecebChoice(recebChoice);
			List<DadosComplemento> lstDadosComplementos = conhecimento.getDadosComplementos();
			for (DadosComplemento dadosComplemento : lstDadosComplementos) {
				if(dadosComplemento.getInformacaoDocServico() != null && "IE_CONSIGNATARIO".equals(dadosComplemento.getInformacaoDocServico().getDsCampo())){
					if(!infCte.getIde().getIndIEToma().equals(IndIETomaType.VALUE_9)){
						receb.setIE(dadosComplemento.getDsValorCampo());
					} else {
						if(!(infCte.getIde().getIdeChoice().getToma3() != null && infCte.getIde().getIdeChoice().getToma3().getToma().equals(TomaType.VALUE_2))){
							receb.setIE(dadosComplemento.getDsValorCampo());
						}
					}
					break;
				}
			}
			if(receb.getIE() == null && conhecimento.getClienteByIdClienteConsignatario().getPessoa().getInscricaoEstaduais() != null && !conhecimento.getClienteByIdClienteConsignatario().getPessoa().getInscricaoEstaduais().isEmpty()){	
				InscricaoEstadual inscricaoEstadual = findIEPadrao(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getInscricaoEstaduais());
				if(inscricaoEstadual != null) {
					if(!infCte.getIde().getIndIEToma().equals(IndIETomaType.VALUE_9)){
						receb.setIE(inscricaoEstadual.getNrInscricaoEstadual());
					} else {
						if(!(infCte.getIde().getIdeChoice().getToma3() != null && infCte.getIde().getIdeChoice().getToma3().getToma().equals(TomaType.VALUE_2))){
							receb.setIE(inscricaoEstadual.getNrInscricaoEstadual());
						}
					}
				}
			}
			receb.setXNome(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getNmPessoa().trim());
			String fone = getFone(conhecimento.getClienteByIdClienteConsignatario().getPessoa());
			receb.setFone(formatarTelefone(fone));
			gerarEnderReceb(receb, conhecimento);
			validaUfInscricaoEstadualReceb(receb, conhecimento);
			
			infCte.setReceb(receb);
		}
	}

	private void validaUfInscricaoEstadualReceb(final Receb receb, final Conhecimento conhecimento){
		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
		String uf = conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

		if(conteudoParametro.toString().indexOf(uf) >= 0){
			if(receb.getIE().length() == 9 && receb.getIE().charAt(0) == '0'){
				String ie = receb.getIE().substring(1);
				receb.setIE(ie);
			}
		}
	}

	private void gerarEnderReceb(final Receb receb, final Conhecimento conhecimento) {
		EnderReceb enderReceb = new EnderReceb();
		
		enderReceb.setXLgr(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getDsEndereco().trim());
		if(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getNrEndereco()!=null){
			enderReceb.setNro(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getNrEndereco().trim());
		}
		if(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getDsComplemento() != null){
			enderReceb.setXCpl(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getDsComplemento().trim());
		}
		String dsBairro = conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getDsBairro();
		if(dsBairro == null || dsBairro.length() < 2){
			enderReceb.setXBairro(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			enderReceb.setXBairro(dsBairro.trim());
		}

		enderReceb.setCMun(comporCMun(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge(), conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()));			
		enderReceb.setXMun(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio());
		enderReceb.setCEP(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getNrCep());
		enderReceb.setUF(TUf.fromValue(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));	
		enderReceb.setCPais(String.valueOf(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNrBacen()));
		enderReceb.setXPais(conhecimento.getClienteByIdClienteConsignatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais().toString());
		
		receb.setEnderReceb(enderReceb);
	}

	private String comporCMun(final Integer nrIbgeMunicipio, final Long nrIbgeEstado){
		return StringUtils.leftPad(String.valueOf(nrIbgeEstado), 2, '0') + StringUtils.leftPad(String.valueOf(nrIbgeMunicipio), 5, '0');
	}
	
	public static Integer getRandom(){
		return RandomUtils.nextInt(99999999);
	}

	public String gerarChaveAcesso(final Conhecimento conhecimento, final Integer random) {
		StringBuilder chave = new StringBuilder();
		chave.append("CTe");
		if(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge() == null){
			throw new IllegalStateException("[CTE] Número do IBGE não preenchido para " + conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		}
		chave.append(StringUtils.leftPad(String.valueOf(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()), 2, '0'));
		chave.append(fmtAnoMes.print(conhecimento.getDhEmissao()));
		chave.append(conhecimento.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
		chave.append("57");
		chave.append("000");
		chave.append(StringUtils.leftPad(String.valueOf(conhecimento.getNrConhecimento()), 9, '0'));
		
		chave.append(buscaTipoServico(conhecimento.getFilialByIdFilialOrigem().getIdFilial()));
		
		chave.append(StringUtils.leftPad(String.valueOf(random), 8, '0'));
		chave.append(modulo11(chave.substring(3, chave.length()).toString()));
		return chave.toString();
	}

	/**
	 * Verifica se a filial está em contingingência e o tipo de emissão que deve ser feita
	 * 
	 * @param idFilialOrigemDoctoServico id da filial de origem do documento de serviço
	 * @return
	 */
	private String buscaTipoServico(Long idFilialOrigemDoctoServico) {
		String tipoServico = "1";
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();

		Contingencia emContingencia = contingenciaService.findByFilial(idFilial, "A", "C");

		UnidadeFederativa uf = unidadeFederativaService.findByIdPessoa(idFilialOrigemDoctoServico);

		if (emContingencia != null) {
			if (uf.getUnidadeFederativaSefazVirtual() == null) {
				tipoServico = "8";
			} else {
				tipoServico = "7";
			}
		}

		return tipoServico;
	}

	private void gerarInfCteChoice(final InfCte infCte, final Conhecimento conhecimento) throws ValidationException {
		InfCteChoice infCteChoice = new InfCteChoice();
		if(("CF".equals(conhecimento.getTpConhecimento().getValue()) || "CI".equals(conhecimento.getTpConhecimento().getValue())) && conhecimento.getDoctoServicoOriginal() != null && "CTE".equals(conhecimento.getDoctoServicoOriginal().getTpDocumentoServico().getValue())){
			gerarInfCTeComp(infCteChoice, conhecimento);
		} else {
			gerarInfCTeNorm(infCte, infCteChoice, conhecimento);
		}
		infCte.setInfCteChoice(infCteChoice);
			
	}

	private void gerarInfCTeComp(final InfCteChoice infCteChoice, final Conhecimento conhecimento) {
		if(conhecimento.getDoctoServicoOriginal() == null){
			throw new IllegalStateException("[CTE] Para conhecimento de complemento um conhecimento original deve estar ligado");
		}
		
		InfCteComp vInfCteCompArray = new InfCteComp();
		
		MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(conhecimento.getDoctoServicoOriginal().getIdDoctoServico());
		
		vInfCteCompArray.setChCTe(mde.getNrChave());
		
		infCteChoice.setInfCteComp(vInfCteCompArray);
	}
	
	private void gerarInfCTeNorm(InfCte infCte, final InfCteChoice infCteChoice, final Conhecimento conhecimento) throws ValidationException {
		InfCTeNorm infCTeNorm = new InfCTeNorm();
		gerarInfCarga(infCTeNorm, conhecimento);

		gerarInfDoc(infCTeNorm, conhecimento);
		
		gerarInfModal(infCTeNorm, conhecimento);
		
		gerarInfCteSub(infCTeNorm, conhecimento);
		
		gerarDocAnt(infCTeNorm, conhecimento);
		
		infCteChoice.setInfCTeNorm(infCTeNorm); 
	}

	private void gerarDocAnt(InfCTeNorm infCTeNorm, Conhecimento conhecimento) {
		DocAnt docAnt = new DocAnt();
		EmiDocAnt[] emiDocAnt = null;

		if ((!BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario())
			&& !BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador()) && !BooleanUtils.isTrue(conhecimento.getBlRedespachoColeta()))
			|| verificaClienteDadoComplemento(conhecimento.getDadosComplementos(), conhecimento)){
				emiDocAnt = gerarEmiDocAnt(conhecimento);
		} else if(BooleanUtils.isTrue(conhecimento.getBlRedespachoColeta())) {
			emiDocAnt = gerarEmiDocAntRedespachoColeta(conhecimento);
		} else {
			emiDocAnt = gerarEmiDocAntRedespachoIntermediario(conhecimento);
		}
		
		if (emiDocAnt.length == 0) {
			return;
		}
		
		docAnt.setEmiDocAnt(emiDocAnt);
		infCTeNorm.setDocAnt(docAnt);
	}
	
	private EmiDocAnt[] gerarEmiDocAntRedespachoColeta(Conhecimento conhecimento) {
		DadosComplemento dadosComplemento = getDadoComplemento(conhecimento.getDadosComplementos(), "NR_CHAVE_DOCUMENTO_ANTERIOR");
		
		if (dadosComplemento == null) {
			return new EmiDocAnt[]{};
		}
		
		Cliente cliente = conhecimento.getClienteByIdClienteConsignatario(); 
		
		EmiDocAnt emiDocAnt = new EmiDocAnt();
		emiDocAnt.setEmiDocAntChoice(getEmiDocAntChoice(cliente));
		emiDocAnt.setEmiDocAntSequence(getEmiDocAntSequence(dadosComplemento, cliente));
		emiDocAnt.setXNome(cliente.getPessoa().getNmPessoa());
		
		IdDocAnt[] idDocAntArray = getIdDocAntCTe(conhecimento);
		emiDocAnt.setIdDocAnt(idDocAntArray);

		return new EmiDocAnt[]{emiDocAnt};
	}

	private EmiDocAnt[] gerarEmiDocAntRedespachoIntermediario(Conhecimento conhecimento) {
		List<EmiDocAnt> listEmiDocAnt = new ArrayList<EmiDocAnt>();
		Set<String> emissores = new HashSet<String>();
		
		if (BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador())){
			DadosComplemento dadosComplemento = getDadoComplemento(conhecimento.getDadosComplementos(), "NR_CHAVE_DOCUMENTO_ANTERIOR"); 
			String chave = dadosComplemento.getDsValorCampo();
			emissores.add(chave.substring(6, 20));
		}else{
			List<NotaFiscalConhecimento> lstNotasFiscais = conhecimento.getNotaFiscalConhecimentos();
			for (NotaFiscalConhecimento notaFiscalConhecimento : lstNotasFiscais) {
				String chave = notaFiscalConhecimento.getNrChave();
				emissores.add(chave.substring(6, 20));
			}
		}

		for (String nrIdentificacao : emissores) {
			Cliente cliente = clienteService.findByCNPJParcial(nrIdentificacao);
			
			EmiDocAnt emiDocAnt = new EmiDocAnt();
			emiDocAnt.setEmiDocAntChoice(getEmiDocAntChoice(cliente));
			emiDocAnt.setEmiDocAntSequence(getEmiDocAntSequence(null, cliente));
			emiDocAnt.setXNome(cliente.getPessoa().getNmPessoa());
			
			IdDocAnt[] idDocAntArray = getIdDocAntCTeRedespachoIntermediario(conhecimento, cliente);
			emiDocAnt.setIdDocAnt(idDocAntArray);
			listEmiDocAnt.add(emiDocAnt);
			
		}
		
		return listEmiDocAnt.toArray(new EmiDocAnt[0]);
	}
	
	private IdDocAnt[] getIdDocAntCTeRedespachoIntermediario(Conhecimento conhecimento, Cliente cliente) {
		IdDocAnt idDocAnt = new IdDocAnt();
		List<IdDocAntEle> lstIdDocAntEle = new ArrayList<IdDocAntEle>();
		
		if (BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador())){
			DadosComplemento dadosComplemento = getDadoComplemento(conhecimento.getDadosComplementos(), "NR_CHAVE_DOCUMENTO_ANTERIOR"); 
			IdDocAntEle idDocAntEle = new IdDocAntEle();
			idDocAntEle.setChCTe(dadosComplemento.getDsValorCampo());
			lstIdDocAntEle.add(idDocAntEle);
		}else{
			for (NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()) {
				if(nfc.getNrChave().substring(6, 20).equals(cliente.getPessoa().getNrIdentificacao())){
					IdDocAntEle idDocAntEle = new IdDocAntEle();
					idDocAntEle.setChCTe(nfc.getNrChave());
					lstIdDocAntEle.add(idDocAntEle);
				}
			}
		}
		
		idDocAnt.setIdDocAntEle(lstIdDocAntEle.toArray(new IdDocAntEle[0]));
		IdDocAnt[] vIdDocAntArray = new IdDocAnt[]{idDocAnt};
		return vIdDocAntArray;
	}

	private EmiDocAnt[] gerarEmiDocAnt(Conhecimento conhecimento) {
		DadosComplemento dadosComplemento = getDadoComplemento(conhecimento.getDadosComplementos(), "NR_DOCUMENTO_ANTERIOR");
		
		if (dadosComplemento == null) {
			return new EmiDocAnt[]{};
		}
		
		Cliente cliente = getCliente(conhecimento); 
		
		EmiDocAnt emiDocAnt = new EmiDocAnt();
		emiDocAnt.setEmiDocAntChoice(getEmiDocAntChoice(cliente));
		emiDocAnt.setEmiDocAntSequence(getEmiDocAntSequence(dadosComplemento, cliente));
		emiDocAnt.setXNome(cliente.getPessoa().getNmPessoa());
		
		DadosComplemento dCmptoTPDocAnterior = getDadoComplemento(conhecimento.getDadosComplementos(), "TP_DOCUMENTO_ANTERIOR");
		IdDocAnt[] idDocAntArray = isCTe(dCmptoTPDocAnterior) ? getIdDocAntCTe(conhecimento) : getIdDocAntNaoCTe(conhecimento, dadosComplemento, dCmptoTPDocAnterior);
		emiDocAnt.setIdDocAnt(idDocAntArray);
		
		return new EmiDocAnt[]{emiDocAnt};
	}

	private boolean isCTe(DadosComplemento dadosCmptoTPDocumentoAnterior) {
		if (dadosCmptoTPDocumentoAnterior != null) {
			return "13".equals(dadosCmptoTPDocumentoAnterior.getDsValorCampo());
	}
		return false;
	}

	private IdDocAnt[] getIdDocAntCTe(Conhecimento conhecimento) {
		IdDocAnt idDocAnt = new IdDocAnt();
		
		IdDocAntEle idDocAntEle = new IdDocAntEle();
		idDocAntEle.setChCTe(getDadoComplemento(conhecimento.getDadosComplementos(), "NR_CHAVE_DOCUMENTO_ANTERIOR").getDsValorCampo());
		IdDocAntEle[] vIdDocAntEleArray = new IdDocAntEle[]{idDocAntEle};
		idDocAnt.setIdDocAntEle(vIdDocAntEleArray);
		
		IdDocAnt[] vIdDocAntArray = new IdDocAnt[]{idDocAnt};
		return vIdDocAntArray;
	}

	private IdDocAnt[] getIdDocAntNaoCTe(Conhecimento conhecimento, DadosComplemento dadosComplemento, DadosComplemento dadosCmptoTPDocumentoAnterior) {
		IdDocAnt idDocAnt = new IdDocAnt();
		IdDocAntPap idDocAntPap = new IdDocAntPap();
		idDocAntPap.setTpDoc(TDocAssoc.fromValue(dadosCmptoTPDocumentoAnterior.getDsValorCampo()));
		idDocAntPap.setSerie(getDadoComplemento(conhecimento.getDadosComplementos(), "NR_SERIE_ANTERIOR").getDsValorCampo());
		idDocAntPap.setNDoc(dadosComplemento.getDsValorCampo());
		idDocAntPap.setDEmi(getDadoComplemento(conhecimento.getDadosComplementos(), "DT_EMISSAO_ANTERIOR").getDsValorCampo());
		IdDocAntPap[] vIdDocAntPapArray = new IdDocAntPap[]{idDocAntPap};
		idDocAnt.setIdDocAntPap(vIdDocAntPapArray);
		IdDocAnt[] vIdDocAntArray = new IdDocAnt[]{idDocAnt};
		return vIdDocAntArray;
	}
	
	private DadosComplemento getDadoComplemento(List<DadosComplemento> dadosComplementos, String dsCampo) {
		for (DadosComplemento dadosComplemento : dadosComplementos) {
			if (dadosComplemento.getInformacaoDocServico() != null && dsCampo.equals(dadosComplemento.getInformacaoDocServico().getDsCampo())) {
				return dadosComplemento;
			}
		}
		return null;
	}

	private EmiDocAntSequence getEmiDocAntSequence(DadosComplemento dadosComplemento, Cliente cliente) {
		EmiDocAntSequence emiDocAntSequence = new EmiDocAntSequence();
		emiDocAntSequence.setIE(getIE(dadosComplemento, cliente));
		emiDocAntSequence.setUF(TUf.fromValue(cliente.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		return emiDocAntSequence;
	}

	private String getIE(DadosComplemento dadosComplemento, Cliente cliente) {
		if(isIEfromDadosComplemento(dadosComplemento)){
			return dadosComplemento.getDsValorCampo();
		} else {
			String inscricaoEstadualDocumentAnterior = null;
                        InscricaoEstadual ie = findIEPadrao(cliente.getPessoa().getInscricaoEstaduais());
                        if(ie != null)
						inscricaoEstadualDocumentAnterior = ie.getNrInscricaoEstadual();
			return inscricaoEstadualDocumentAnterior;
		}
	}

    private InscricaoEstadual findIEPadrao(List<InscricaoEstadual> list) {
        InscricaoEstadual inscricaoEstadual = null;
        if(list != null){
            for (InscricaoEstadual ie : list) {
                if(Boolean.TRUE.equals(ie.getBlIndicadorPadrao())){
                    inscricaoEstadual = ie;
						break;
					}
				}
			}
        return inscricaoEstadual;
		}

	private boolean isIEfromDadosComplemento(DadosComplemento dadosComplemento) {
		return dadosComplemento != null && dadosComplemento.getInformacaoDocServico() != null && "IE_REDESPACHO".equals(dadosComplemento.getInformacaoDocServico().getDsCampo());
	}

	private EmiDocAntChoice getEmiDocAntChoice(Cliente cliente) {
		EmiDocAntChoice emiDocAntChoice = new EmiDocAntChoice();
		if ("J".equals(cliente.getPessoa().getTpPessoa().getValue())) {
			String cnpj = isClienteBrasileiro(cliente) ? cliente.getPessoa().getNrIdentificacao() : "00000000000000";
			emiDocAntChoice.setCNPJ(cnpj);
		} else {
			emiDocAntChoice.setCPF(cliente.getPessoa().getNrIdentificacao());
		}
		return emiDocAntChoice;
	}

	private boolean isClienteBrasileiro(Cliente cliente) {
		Long idPaisBrasil = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_PAIS_BRASIL"));
		return cliente.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais().equals(idPaisBrasil);
	}

	private Cliente getCliente(Conhecimento conhecimento) {
		Cliente result = conhecimento.getClienteByIdClienteRedespacho();
		if(result == null) {
			result = conhecimento.getDevedorDocServs().get(0).getCliente();
		}
		return result;
	}
	
	private void gerarInfModal(final InfCTeNorm infCTeNorm, final Conhecimento conhecimento) throws ValidationException {
		InfModal infModal = new InfModal();
		infModal.setVersaoModal("3.00");
		gerarRodo(infModal, conhecimento);
		
		infCTeNorm.setInfModal(infModal);
	}

	private void gerarRodo(final InfModal infModal, final Conhecimento conhecimento) throws ValidationException {
		Rodo rodo = new Rodo();
		Proprietario proprietario = proprietarioService.findByIdReturnNull(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		if(proprietario == null){
			proprietario = proprietarioService.findByIdReturnNull(Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_EMPRESA_MERCURIO")));
			if(proprietario == null){
				throw new IllegalStateException("[CTE] Sem proprietario para filial");
			}
		}
		rodo.setRNTRC(StringUtils.leftPad(String.valueOf(proprietario.getNrAntt()), 8, '0'));
		rodo.validate();
		infModal.setAnyObject(rodo);
	}

	private void gerarInfCarga(final InfCTeNorm infCTeNorm, final Conhecimento conhecimento) {
		InfCarga infCarga = new InfCarga();
		infCarga.setVCarga(formatDoisDecimais.format(conhecimento.getVlMercadoria()));
		if(conhecimento.getNaturezaProduto().getDsNaturezaProduto().toString().length() > 60){
			infCarga.setProPred(conhecimento.getNaturezaProduto().getDsNaturezaProduto().toString().substring(0,60));
		} else {
		infCarga.setProPred(conhecimento.getNaturezaProduto().getDsNaturezaProduto().toString());
		}
		infCarga.setXOutCat(null);
		ArrayList<InfQ> listInfQ = new ArrayList<InfQ>();
		InfQ infQ = new InfQ();
		listInfQ.add(infQ);
		infQ.setCUnid(CUnidType.VALUE_1);
		infQ.setTpMed("PESO DECLARADO");
		infQ.setQCarga(formatQuatroDecimais.format(conhecimento.getPsReal()));
		
		infQ = new InfQ();
		listInfQ.add(infQ);
		infQ.setCUnid(CUnidType.VALUE_1);
		infQ.setTpMed("PESO CUBADO");
		infQ.setQCarga(formatQuatroDecimais.format(conhecimento.getPsAforado()));
		
		if(conhecimento.getPsAferido() != null){
			infQ = new InfQ();
			listInfQ.add(infQ);
			infQ.setCUnid(CUnidType.VALUE_1);
			infQ.setTpMed("PESO AFERIDO");
			infQ.setQCarga(formatQuatroDecimais.format(conhecimento.getPsAferido()));
		}
		
		infQ = new InfQ();
		listInfQ.add(infQ);
		infQ.setCUnid(CUnidType.VALUE_1);
		infQ.setTpMed("PESO BASE DE CÁLCULO");
		infQ.setQCarga(formatQuatroDecimais.format(conhecimento.getPsReferenciaCalculo()));
		
		infQ = new InfQ();
		listInfQ.add(infQ);
		infQ.setCUnid(CUnidType.VALUE_3);
		infQ.setTpMed("QTDE DE VOLUMES");
		infQ.setQCarga(formatQuatroDecimais.format(conhecimento.getQtVolumes()));
		
		InfQ[] infQA = new InfQ[listInfQ.size()];
		infQA = listInfQ.toArray(infQA);
		
		infCarga.setInfQ(infQA);
		
		infCTeNorm.setInfCarga(infCarga);
	}

	private void gerarImp(final InfCte infCte, final Conhecimento conhecimento, String valorTotalTributo) {
		Imp imp = new Imp();
		imp.setVTotTrib(valorTotalTributo);
		gerarICMS(imp, conhecimento);
		gerarICMSUFFim(imp, conhecimento);
		infCte.setImp(imp);
	}

	private ObsCont[] popularObsCont(InfCte infCte, String valorTotalTributo, Conhecimento conhecimento) {
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("MSG_LEI_TRANSP", false);
		String msgLeiTransparencia = "";
		
		if (parametroGeral != null && parametroGeral.getDsConteudo() != null){
			msgLeiTransparencia = parametroGeral.getDsConteudo().replace("{0}", valorTotalTributo);
		}
		
		ObsCont obsCont = new ObsCont();
		obsCont.setXCampo(configuracoesFacade.getMensagem("leiTransparencia"));
		obsCont.setXTexto(msgLeiTransparencia);
		ParametroGeral parametroGeralModalA = parametroGeralService.findByNomeParametro("TAG_RAMO_AVERB_CTE_AEREO");
		ParametroGeral parametroGeralModalR = parametroGeralService.findByNomeParametro("TAG_RAMO_AVERB_CTE_RODOVIARIO");
		String ramo = "";
		String tpModal = conhecimento.getServico().getTpModal().getValue();
		if ("A".equals(tpModal)&& parametroGeralModalA != null){
			ramo = parametroGeralModalA.getDsConteudo();
		}
		else if ("R".equals(tpModal)&& parametroGeralModalR != null){
			ramo = parametroGeralModalR.getDsConteudo();
		}
		if (ramo == null){
			ramo = "";
		}
		
		ObsCont[] obsContRetorno = new ObsCont[]{obsCont};
		ObsCont obsContRamo = new ObsCont();
		if (!ramo.isEmpty()){
		obsContRamo.setXCampo(configuracoesFacade.getMensagem("ramo"));
		obsContRetorno = new ObsCont[]{obsCont,obsContRamo};
		} else {
			obsContRamo.setXCampo("");
		}	
		obsContRamo.setXTexto(ramo);		
		
		return obsContRetorno;				
	}

	private void gerarICMS(final Imp imp, final Conhecimento conhecimento) {
		ICMS iCMS = new ICMS();
		iCMS.setICMS00(gerarICMS00(conhecimento));
		iCMS.setICMS20(gerarICMS20(conhecimento));
		iCMS.setICMS45(gerarICMS45(conhecimento));
		iCMS.setICMS60(gerarICMS60(conhecimento));
		iCMS.setICMS90(gerarICMS90(conhecimento));
		iCMS.setICMSOutraUF(gerarICMSOutraUF(conhecimento));
		
		imp.setICMS(iCMS);
	}

	/**
	 * Método responsável por calcular o valor total do tributo. 
	 */
	public String calcularValorTotalTrib(Conhecimento conhecimento){
		BigDecimal valorTotalTribSoma = BigDecimal.ZERO;
		BigDecimal valorTotalTribMultipl = BigDecimal.ZERO;
		BigDecimal valorTotalTribDivisao = BigDecimal.ZERO;
		BigDecimal valorTotalTrib = BigDecimal.ZERO;
		
		BigDecimal valorLiquidoFrete = conhecimento.getVlLiquido();		
		BigDecimal valorLiqFreteMultPIS = (valorLiquidoFrete.multiply(PIS)).setScale(2, BigDecimal.ROUND_FLOOR);
		BigDecimal valorLiqFreteMultCOFINS = (valorLiquidoFrete.multiply(COFINS)).setScale(2, BigDecimal.ROUND_FLOOR); 		
		
		valorTotalTrib = valorLiqFreteMultPIS.add(valorLiqFreteMultCOFINS);
		
		BigDecimal valorImposto = conhecimento.getVlImposto();
		BigDecimal valorICMSST = conhecimento.getVlIcmsSubstituicaoTributaria();
		
		if (valorImposto.compareTo(BigDecimal.ZERO) == 0  && valorICMSST.compareTo(BigDecimal.ZERO) == 1){
			valorTotalTribMultipl = (valorICMSST.multiply(new BigDecimal(20))).setScale(2, BigDecimal.ROUND_FLOOR);
			valorTotalTribDivisao = (valorTotalTribMultipl.divide(new BigDecimal(80))).setScale(2, BigDecimal.ROUND_FLOOR);	
			valorTotalTrib = valorTotalTrib.add(valorTotalTribDivisao);
		}else{
			valorTotalTribSoma = valorTotalTrib.add(valorImposto);
			valorTotalTrib = valorTotalTribSoma.compareTo(valorICMSST) >= 0 ? valorTotalTribSoma.subtract(valorICMSST) : BigDecimal.ZERO;  	
		}
		
		if (valorTotalTrib.compareTo(BigDecimal.ZERO) == 0){
			valorTotalTrib = new BigDecimal("0.00");
		}
		
		return String.valueOf(valorTotalTrib);  		
	}
	
	private ICMS90 gerarICMS90(final Conhecimento conhecimento) {
		/**
			"Preencher este grupo quando (CTO.ID_TIPO_TRIBUTACAO_ICMS = PG.DS_CONTEUDO 
			para PG.NM_PARAMETRO_GERAL = ""ID_TRIB_ICMS_OUTROS"")
			OU
			quando (CTO.ID_TIPO_TRIBUTACAO_ICMS = PG.DS_CONTEUDO 
			para PG.NM_PARAMETRO_GERAL = """"ID_TRIB_ICMS_SUBS_TRIB"""" 
			e PG.DS_CONTEUDO para PG.NM_PARAMETRO_GERAL = """"BL_UTILIZA_CODIGO_90_ST"""" = """"S"""" 
			e UF.SG_UNIDADE_FEDERATIVA da filial logada pertencer ao parâmetro geral 
			“UFS_UTILIZAM_CODIGO_90”)"
		**/
		Long idTribIcmsOutros = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_OUTROS"));
		Long idTribIcmsSubsTrib = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_SUBS_TRIB"));
		if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsOutros) ||
				(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsSubsTrib) &&
				calcularIcmsService.validateUtilizacaoCodigo90ST(getIdUfFilialSessao()))){
			ICMS90 iCMS90 = new ICMS90();
			iCMS90.setCST(CSTType.VALUE_90);
			iCMS90.setPRedBC(null);
			iCMS90.setVBC(formatDoisDecimais.format(conhecimento.getVlBaseCalcImposto()));
			iCMS90.setPICMS(formatDoisDecimais.format(conhecimento.getPcAliquotaIcms()));
			iCMS90.setVICMS(formatDoisDecimais.format(conhecimento.getVlImposto()));
			iCMS90.setVCred(formatDoisDecimais.format(calcICMS90Vcred(conhecimento)));
			
			return iCMS90;
		}
		return null;
	}

	protected BigDecimal calcICMS90Vcred(final Conhecimento conhecimento) {
		BigDecimal vCred;
		if (calcularIcmsService.validateUtilizacaoCodigo90ValorCredito(getIdUfFilialSessao())) {
			vCred = calcularIcmsService.calcICMSValorCredito(conhecimento);
		} else {
			vCred = BigDecimal.ZERO;
		}
		return vCred;
	}

	private ICMSOutraUF gerarICMSOutraUF(Conhecimento conhecimento) {
		Long idTribIcmsOutraUf = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_OUTRA_UF"));
		if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsOutraUf)){
			ICMSOutraUF iCMSOutraUF = new ICMSOutraUF();
			iCMSOutraUF.setCST(CSTType.VALUE_90);
			iCMSOutraUF.setPRedBCOutraUF(formatDoisDecimais.format(BigDecimal.ZERO));
			iCMSOutraUF.setVBCOutraUF(formatDoisDecimais.format(conhecimento.getVlBaseCalcImposto()));
			iCMSOutraUF.setPICMSOutraUF(formatDoisDecimais.format(conhecimento.getPcAliquotaIcms()));
			iCMSOutraUF.setVICMSOutraUF(formatDoisDecimais.format(conhecimento.getVlImposto()));
			return iCMSOutraUF;
		}
		return null;
	}

	private ICMS60 gerarICMS60(final Conhecimento conhecimento) {
		/**
			"Preencher este grupo quando (CTO.ID_TIPO_TRIBUTACAO_ICMS = PG.DS_CONTEUDO 
			para PG.NM_PARAMETRO_GERAL = ""ID_TRIB_ICMS_SUBS_TRIB"" E PG.DS_CONTEUDO 
			para PG.NM_PARAMETRO_GERAL = ""BL_UTILIZA_CODIGO_90_ST"" = ""N"")
			OU
			quando CTO.ID_TIPO_TRIBUTACAO_ICMS = PG.DS_CONTEUDO 
			para PG.NM_PARAMETRO_GERAL = ""ID_TRIB_ICMS_SUBS_TRIB"" e (PG.DS_CONTEUDO 
			para PG.NM_PARAMETRO_GERAL = ""BL_UTILIZA_CODIGO_90_ST"" = ""S"" 
			e UF.SG_UNIDADE_FEDERATIVA da filial logada NÃO pertencer ao parâmetro geral “UFS_UTILIZAM_CODIGO_90”)"
		**/
		Long idTribIcmsSubsTrib = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_SUBS_TRIB"));
		String unidadesFederativas = parametroGeralService.findSimpleConteudoByNomeParametro("UFS_ICMS_ST_60");
		
		if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsSubsTrib) && 
				!calcularIcmsService.validateUtilizacaoCodigo90ST(getIdUfFilialSessao())){
			ICMS60 iCMS60 = new ICMS60();
			iCMS60.setCST(CSTType.VALUE_60);
			iCMS60.setVBCSTRet(formatDoisDecimais.format(conhecimento.getVlBaseCalcImposto()));
			iCMS60.setVICMSSTRet(formatDoisDecimais.format(getVICMSSTRe(unidadesFederativas, conhecimento)));
			iCMS60.setPICMSSTRet(formatDoisDecimais.format(conhecimento.getPcAliquotaIcms()));
			iCMS60.setVCred(formatDoisDecimais.format(calcularIcmsService.calcICMSValorCredito(conhecimento)));
			return iCMS60;
		}
		return null;
	}

	private BigDecimal getVICMSSTRe(String unidadesFederativas, Conhecimento conhecimento) {
		List<String> unidadesFederativasList = Arrays.asList(unidadesFederativas.split(";"));
		UnidadeFederativa unidadeFederativa = 
				conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
		
		if (unidadesFederativasList.contains(unidadeFederativa.getSgUnidadeFederativa())) {
			return conhecimento.getVlImposto();
		} else {
			return conhecimento.getVlIcmsSubstituicaoTributaria();
		}
	}

	private ICMS45 gerarICMS45(final Conhecimento conhecimento) {
		Long idTribIcmsIsencao = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_ISENCAO"));
		Long idTribIcmsNaoTrib = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_NAO_TRIB"));
		Long idTribIcmsDiferido = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_DIFERIDO"));
		
		if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsIsencao) 
				|| conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsNaoTrib) 
				|| conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsDiferido)){
			ICMS45 iCMS45 = new ICMS45();
			CSTType cstType;
			if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsIsencao)){
				cstType = CSTType.VALUE_40;
			} else if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsNaoTrib)){
				cstType = CSTType.VALUE_41;
			} else if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsDiferido)){
				cstType = CSTType.VALUE_51;
			} else {
				throw new IllegalStateException("[CTE] Tipo tributacao nao configurado para ICMS 45");
			}
			iCMS45.setCST(cstType);
			return iCMS45;
		}
		return null;
	}

	private ICMS20 gerarICMS20(final Conhecimento conhecimento) {
		Long idTribIcmsRedBc = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_RED_BC"));
		if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsRedBc)){
			ICMS20 iCMS20 = new ICMS20();
			iCMS20.setCST(CSTType.VALUE_20);
			iCMS20.setPRedBC("0");
			iCMS20.setVBC(formatDoisDecimais.format(conhecimento.getVlBaseCalcImposto()));
			iCMS20.setPICMS(formatDoisDecimais.format(conhecimento.getPcAliquotaIcms()));
			iCMS20.setVICMS(formatDoisDecimais.format(conhecimento.getVlImposto()));
			return iCMS20;
		}
		return null;
	}

	private ICMS00 gerarICMS00(final Conhecimento conhecimento) {
		Long idTribIcmsNormal = Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_TRIB_ICMS_NORMAL"));
		if(conhecimento.getTipoTributacaoIcms() != null && conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(idTribIcmsNormal)){
			ICMS00 iCMSS00 = new ICMS00();
			iCMSS00.setCST(CSTType.VALUE_0);
			iCMSS00.setVBC(formatDoisDecimais.format(conhecimento.getVlBaseCalcImposto()));
			iCMSS00.setPICMS(formatDoisDecimais.format(conhecimento.getPcAliquotaIcms()));
			iCMSS00.setVICMS(formatDoisDecimais.format(conhecimento.getVlImposto()));
			return iCMSS00;
		}
		return null;
	}

	private void gerarVPrest(final InfCte infCte, final Conhecimento conhecimento) {
		VPrest vPrest = new VPrest();
		
		vPrest.setVTPrest(formatDoisDecimais.format(conhecimento.getVlTotalDocServico()));
		vPrest.setVRec(formatDoisDecimais.format(conhecimento.getVlLiquido()));
		gerarComp(vPrest, conhecimento);
		
		infCte.setVPrest(vPrest);
	}

	private void gerarComp(final VPrest vPrest, final Conhecimento conhecimento) {
		
		Long idCliente = conhecimento.getDevedorDocServs().get(0).getCliente().getIdCliente();
		boolean blGerarParcValorLiquido = clienteService.findClienteGerarParcelaFreteVlLiquidoXmlCte(idCliente);
		
		List<ParcelaDoctoServico> lstParcelaDoctoServico = conhecimento.getParcelaDoctoServicos();
		Comp[] vCompArray = new Comp[lstParcelaDoctoServico.size() + 1];
		for (int i = 0; i < lstParcelaDoctoServico.size(); i++) {
			Comp comp = new Comp();
			
			ParcelaDoctoServico pds = lstParcelaDoctoServico.get(i);
			
			String xNome=pds.getParcelaPreco().getDsParcelaPreco().toString();
			if(xNome!=null){
				if(conhecimento.getClienteByIdClienteRemetente().getClienteMatriz()!=null){
					xNome=deParaXmlCteService.getByXNome(xNome,conhecimento.getClienteByIdClienteRemetente().getClienteMatriz().getIdCliente());
				}
				xNome=deParaXmlCteService.getByXNome(xNome,conhecimento.getClienteByIdClienteRemetente().getIdCliente());
			}
			if( xNome.length()> 15){
				comp.setXNome(StringUtils.stripEnd(xNome.substring(0, 15), null));
			} else {
				comp.setXNome(xNome);
			}
			
			if(blGerarParcValorLiquido && pds.getVlBrutoParcela() != null){
				comp.setVComp(formatDoisDecimais.format(pds.getVlBrutoParcela()));//pds.getVlBrutoParcela() representa o valor liquido
			}else{
				comp.setVComp(formatDoisDecimais.format(pds.getVlParcela()));//pds.getVlParcela() representa o valor bruto
			}
			
			vCompArray[i] = comp;
		}
		
		Comp compDesconto = new Comp();
		compDesconto.setXNome("Desconto");
		compDesconto.setVComp(formatDoisDecimais.format(conhecimento.getVlDesconto()));
		vCompArray[vCompArray.length-1] = compDesconto;
		
		vPrest.setComp(vCompArray);
		
	}

	private void gerarDest(final InfCte infCte, final Conhecimento conhecimento, final Long idPaisBrasil) {
		Dest dest = new Dest();
		inicializarInscricaoEstadualDestinatario(dest);
		DestChoice destChoice = new DestChoice();
		if(idPaisBrasil.equals(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
			if("J".equals(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getTpPessoa().getValue())){
				destChoice.setCNPJ(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao());
			} else {
				destChoice.setCPF(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao());
			}
		} else {
			destChoice.setCNPJ(cnpjExterior);
		}
		dest.setDestChoice(destChoice);
		if(conhecimento.getInscricaoEstadualDestinatario() != null){
			if(!infCte.getIde().getIndIEToma().equals(IndIETomaType.VALUE_9)){
				dest.setIE(conhecimento.getInscricaoEstadualDestinatario().getNrInscricaoEstadual());
			} else {
				if(!(infCte.getIde().getIdeChoice().getToma3() != null && infCte.getIde().getIdeChoice().getToma3().getToma().equals(TomaType.VALUE_3))){
					dest.setIE(conhecimento.getInscricaoEstadualDestinatario().getNrInscricaoEstadual());
				}
			}
		}
		dest.setXNome(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa().trim());
		String fone = getFone(conhecimento.getClienteByIdClienteDestinatario().getPessoa());
		dest.setFone(formatarTelefone(fone));
		if(conhecimento.getClienteByIdClienteDestinatario().getNrInscricaoSuframa() != null){
			dest.setISUF(String.valueOf(conhecimento.getClienteByIdClienteDestinatario().getNrInscricaoSuframa()));
		}
		gerarEnderDest(dest, conhecimento);
		dest.setEmail(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getDsEmail());
		validaUfInscricaoEstadualDest(dest, conhecimento);
		
		infCte.setDest(dest);
	}
	
	private void inicializarInscricaoEstadualDestinatario(Dest dest) {
		dest.setIE("");
	}

	private void validaUfInscricaoEstadualDest(final Dest dest, final Conhecimento conhecimento){
		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
		String uf = conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

		if(conteudoParametro.toString().indexOf(uf) >= 0){
			if(dest.getIE().length() == 9 && dest.getIE().charAt(0) == '0'){
				String ie = dest.getIE().substring(1);
				dest.setIE(ie);
			}
		}
	}

	private void gerarEnderDest(final Dest dest, final Conhecimento conhecimento) {
		EnderDest enderDest = new EnderDest();
		enderDest.setXLgr(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getDsEndereco().trim());
		if(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getNrEndereco()!=null){
			enderDest.setNro(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getNrEndereco().trim());
		}
		if(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getDsComplemento() != null){
			enderDest.setXCpl(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getDsComplemento().trim());
		}
		String dsBairro = conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getDsBairro();
		if(dsBairro == null || dsBairro.length() < 2){
			enderDest.setXBairro(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			enderDest.setXBairro(dsBairro.trim());
		}

		if (isClienteBrasileiro(conhecimento.getClienteByIdClienteDestinatario().getCliente())) {
			enderDest.setCMun(comporCMun(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge(), conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()));
		} else {
			enderDest.setCMun("9999999");
		}

		enderDest.setXMun(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio());
		enderDest.setCEP(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getNrCep());

		if (enderDest.getCMun().equals("9999999")) {
			enderDest.setUF(TUf.fromValue("EX"));
		} else {
			enderDest.setUF(TUf.fromValue(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));	
		}

		enderDest.setCPais(String.valueOf(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNrBacen()));
		enderDest.setXPais(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais().toString());
		
		dest.setEnderDest(enderDest);
	}

	private void gerarExped(final InfCte infCte, final Conhecimento conhecimento, final Long idPaisBrasil) {
		if(conhecimento.getClienteByIdClienteRedespacho() != null){
			Exped exped = new Exped();
			
			ExpedChoice expedChoice = new ExpedChoice();
			if(idPaisBrasil.equals(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
				if("J".equals(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getTpPessoa().getValue())){
					expedChoice.setCNPJ(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getNrIdentificacao());
				} else {
					expedChoice.setCPF(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getNrIdentificacao());
				}
			} else {
				expedChoice.setCNPJ(cnpjExterior);
			}
			exped.setExpedChoice(expedChoice);
			List<DadosComplemento> lstDadosComplementos = conhecimento.getDadosComplementos();
			for (DadosComplemento dadosComplemento : lstDadosComplementos) {
				if(isIEfromDadosComplemento(dadosComplemento)){
					if(!infCte.getIde().getIndIEToma().equals(IndIETomaType.VALUE_9)){
						exped.setIE(dadosComplemento.getDsValorCampo());
					} else {
						if(!(infCte.getIde().getIdeChoice().getToma3() != null && infCte.getIde().getIdeChoice().getToma3().getToma().equals(TomaType.VALUE_1))){
							exped.setIE(dadosComplemento.getDsValorCampo());
						}
					}
					break;
				}
			}
			if(exped.getIE() == null && conhecimento.getClienteByIdClienteRedespacho().getPessoa().getInscricaoEstaduais() != null && !conhecimento.getClienteByIdClienteRedespacho().getPessoa().getInscricaoEstaduais().isEmpty()){
				InscricaoEstadual inscricaoEstadual = findIEPadrao(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getInscricaoEstaduais());
				if(preencherInscricaoEstadual(inscricaoEstadual)){
					if(!infCte.getIde().getIndIEToma().equals(IndIETomaType.VALUE_9)){
						exped.setIE(inscricaoEstadual.getNrInscricaoEstadual());
					} else {
						if(!(infCte.getIde().getIdeChoice().getToma3() != null && infCte.getIde().getIdeChoice().getToma3().getToma().equals(TomaType.VALUE_1))){
							exped.setIE(inscricaoEstadual.getNrInscricaoEstadual());
						}
					}
				}
			}
			exped.setXNome(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getNmPessoa().trim());
			String fone = getFone(conhecimento.getClienteByIdClienteRedespacho().getPessoa());
			exped.setFone(formatarTelefone(fone));
			gerarEnderExped(exped, conhecimento);
			validaUfInscricaoEstadualExped(exped, conhecimento);
			
			infCte.setExped(exped);
		}
	}

	private void validaUfInscricaoEstadualExped(final Exped exped, final Conhecimento conhecimento){
		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
		String uf = conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

		if(conteudoParametro.toString().indexOf(uf) >= 0){
			if(exped.getIE().length() == 9 && exped.getIE().charAt(0) == '0'){
				String ie = exped.getIE().substring(1);
				exped.setIE(ie);
			}
		}
	}

	private void gerarEnderExped(final Exped exped, final Conhecimento conhecimento) {
		EnderExped enderExped = new EnderExped();
		
		enderExped.setXLgr(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getDsEndereco().trim());
		if(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getNrEndereco()!=null){
			enderExped.setNro(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getNrEndereco().trim());
		}
		if(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getDsComplemento() != null){
			enderExped.setXCpl(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getDsComplemento().trim());
		}
		String dsBairro = conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getDsBairro();
		if(dsBairro == null || dsBairro.length() < 2){
			enderExped.setXBairro(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			enderExped.setXBairro(dsBairro.trim());
		}
		enderExped.setCMun(comporCMun(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge(), conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()));
		enderExped.setXMun(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio());
		enderExped.setCEP(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getNrCep());
		enderExped.setUF(TUf.fromValue(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		enderExped.setCPais(String.valueOf(conhecimento.getClienteByIdClienteRedespacho().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNrBacen()));
		enderExped.setXPais(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais().toString());
		
		exped.setEnderExped(enderExped);
	}

	private void gerarRem(final InfCte infCte, final Conhecimento conhecimento, final Long idPaisBrasil) {
		Rem rem = new Rem();
		inicializarInscricaoEstadualRemetente(rem);
		RemChoice remChoice = new RemChoice();
		if(idPaisBrasil.equals(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
			if("J".equals(conhecimento.getClienteByIdClienteRemetente().getPessoa().getTpPessoa().getValue())){
				remChoice.setCNPJ(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
			} else {
				remChoice.setCPF(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
			}
		} else {
			remChoice.setCNPJ(cnpjExterior);
		}
		rem.setRemChoice(remChoice);
		if(conhecimento.getInscricaoEstadualRemetente() != null) {
			if(!infCte.getIde().getIndIEToma().equals(IndIETomaType.VALUE_9)){
				rem.setIE(conhecimento.getInscricaoEstadualRemetente().getNrInscricaoEstadual());
			} else {
				if(!(infCte.getIde().getIdeChoice().getToma3() != null && infCte.getIde().getIdeChoice().getToma3().getToma().equals(TomaType.VALUE_0))){
					rem.setIE(conhecimento.getInscricaoEstadualRemetente().getNrInscricaoEstadual());
				}
			}
		}
		rem.setXNome(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmPessoa().trim());
		if(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmFantasia() != null){
			rem.setXFant(conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmFantasia().trim());
		}
		String fone = getFone(conhecimento.getClienteByIdClienteRemetente().getPessoa());
		rem.setFone(formatarTelefone(fone));
		gerarEnderReme(rem, conhecimento);
		validaUfInscricaoEstadualRem(rem, conhecimento);		
		
		infCte.setRem(rem);
	}

	/**
	 * Correção do bug LMSA-5900
	 * 
	 * JIRA: http://lx-swjir01/jira/browse/LMSA-5900
	 * @param rem
	 */
	private void inicializarInscricaoEstadualRemetente(Rem rem) {
		rem.setIE("");
	}

	private void validaUfInscricaoEstadualRem(final Rem rem, final Conhecimento conhecimento){
		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
		String uf = conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

		if(conteudoParametro.toString().indexOf(uf) >= 0){
			if(rem.getIE().length() == 9 && rem.getIE().charAt(0) == '0'){
				String ie = rem.getIE().substring(1);
				rem.setIE(ie);
			}
		}
	}

	private void gerarInfDoc(final InfCTeNorm infCTeNorm, final Conhecimento conhecimento) {
		
		if (!Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario())) {
			
			List<NotaFiscalConhecimento> lstNotasFiscais = conhecimento.getNotaFiscalConhecimentos();
			
			if (!existsNF(lstNotasFiscais)) throw new IllegalStateException("[CTe] Conhecimento sem notas fiscais");
			
			InfDoc infDoc = new InfDoc();
			
			ordenarNfConhecimento(lstNotasFiscais); 
			
			if(isNF(lstNotasFiscais.get(0)))  infDoc.setInfNF(createNF(lstNotasFiscais));
			
			if(isNFe(lstNotasFiscais.get(0))) infDoc.setInfNFe(createNFE(lstNotasFiscais));
			
			if(isInfOutros(lstNotasFiscais.get(0))) infDoc.setInfOutros(createInfOutros(lstNotasFiscais));	
			
			infCTeNorm.setInfDoc(infDoc);		
		}
		
	}
	
	private boolean isNFe(NotaFiscalConhecimento nfc){
		return nfc.getNrChave() != null && !isInfOutros(nfc) ? true : false;
			}
			
	private boolean isNF(NotaFiscalConhecimento nfc){
		return !isNFe(nfc) && !isInfOutros(nfc) ? true : false;
	}
		
	private boolean isInfOutros(NotaFiscalConhecimento nfc){
		return nfc.getTpDocumento() != null && !"01".equals(nfc.getTpDocumento()) ? true : false;
	}
	
	private InfOutros[] createInfOutros(List<NotaFiscalConhecimento> lstNotasFiscais) {
		List<InfOutros> lst = new ArrayList<InfOutros>();
			for (NotaFiscalConhecimento nfc : lstNotasFiscais) {
			InfOutros infOutros = new InfOutros();
			infOutros.setTpDoc(TpDocType.fromValue(nfc.getTpDocumento()));
			infOutros.setDescOutros(getDescOutros(nfc));
			
			infOutros.setNDoc(nfc.getNrNotaFiscal() != null ? nfc.getNrNotaFiscal().toString() : null);
			infOutros.setDEmi(nfc.getDtEmissao() != null ? nfc.getDtEmissao().toString() : null);
			infOutros.setVDocFisc(nfc.getVlTotal() != null ? formatDoisDecimais.format(nfc.getVlTotal()) : null);
			
			lst.add(infOutros);
				}
		return lst.toArray(new InfOutros[0]);
	}

	private String getDescOutros(NotaFiscalConhecimento nfc) {
		return TpDocType.VALUE_99.toString().equals(nfc.getTpDocumento()) ? domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_CARGA", nfc.getTpDocumento()).getDescriptionAsString() : null;
	}
	
	private InfNF[] createNF(List<NotaFiscalConhecimento> lstNotasFiscais) {
			List<InfNF> lstInfNF = new ArrayList<InfNF>();
			for (NotaFiscalConhecimento nfc : lstNotasFiscais) {
			if(isNFe(nfc)) throw new IllegalStateException("[CTe] Conhecimento com NFEs e NFs");

				InfNF infNF = new InfNF();
				infNF.setNRoma(null);
				infNF.setNPed(null);
				infNF.setMod(TModNF.VALUE_1);
			infNF.setSerie(nfc.getDsSerie() == null ? "0" : nfc.getDsSerie().trim());
				infNF.setNDoc(String.valueOf(nfc.getNrNotaFiscal()));
				infNF.setDEmi(fmtSemHora.print(nfc.getDtEmissao()));
				infNF.setVBC(formatDoisDecimais.format(nfc.getVlBaseCalculo()));
				infNF.setVICMS(formatDoisDecimais.format(nfc.getVlIcms()));
				infNF.setVBCST(formatDoisDecimais.format(nfc.getVlBaseCalculoSt()));
				infNF.setVST(formatDoisDecimais.format(nfc.getVlIcmsSt()));
				infNF.setVProd(formatDoisDecimais.format(nfc.getVlTotalProdutos()));
				infNF.setVNF(formatDoisDecimais.format(nfc.getVlTotal()));
				infNF.setNCFOP(nfc.getNrCfop().toString());
			infNF.setNPeso(nfc.getPsMercadoria().compareTo(BigDecimal.ZERO) == 0 ? psMercadoria : formatTresDecimais.format(nfc.getPsMercadoria()));
			infNF.setPIN(nfc.getNrPinSuframa() != null ? nfc.getNrPinSuframa().toString() : null);
				
			lstInfNF.add(infNF);
				}
		return lstInfNF.toArray(new InfNF[0]);
	}
				
	private InfNFe[] createNFE(List<NotaFiscalConhecimento> lstNotasFiscais) {
		List<InfNFe> lstInfNFe = new ArrayList<InfNFe>();
		for (NotaFiscalConhecimento nfc : lstNotasFiscais) {
			if(isNF(nfc)) throw new IllegalStateException("[CTe] Conhecimento com NFEs e NFs");

			InfNFe infNFe = new InfNFe();
			infNFe.setChave(nfc.getNrChave());
			infNFe.setPIN(nfc.getNrPinSuframa() != null ? String.valueOf(nfc.getNrPinSuframa()) : null);
			
			lstInfNFe.add(infNFe);
				}
		return lstInfNFe.toArray(new InfNFe[0]);
	}
				
	private void ordenarNfConhecimento(List<NotaFiscalConhecimento> lstNotasFiscais) {
		Collections.sort(lstNotasFiscais, new Comparator<NotaFiscalConhecimento>(){
			public int compare(NotaFiscalConhecimento o1, NotaFiscalConhecimento o2) {
				return o1.getIdNotaFiscalConhecimento().compareTo(o2.getIdNotaFiscalConhecimento());
			}
			
		});
		}
		
	private boolean existsNF(List<NotaFiscalConhecimento> lstNotasFiscais) {
		return lstNotasFiscais.isEmpty() ? false : true;
	}

	private void gerarEnderReme(final Rem rem, final Conhecimento conhecimento) {
		EnderReme enderReme = new EnderReme();
		enderReme.setXLgr(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getDsEndereco().trim());
		if(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getNrEndereco()!=null){
			enderReme.setNro(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getNrEndereco().trim());
		}
		if(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getDsComplemento() != null){
			enderReme.setXCpl(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getDsComplemento().trim());
		}
		String dsBairro = conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getDsBairro();
		if(dsBairro == null || dsBairro.length() < 2){
			enderReme.setXBairro(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			enderReme.setXBairro(dsBairro.trim());
		}
		enderReme.setCMun(comporCMun(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge(), conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()));
		enderReme.setXMun(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio());
		enderReme.setCEP(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getNrCep());
		enderReme.setUF(TUf.fromValue(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		if(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNrBacen() == null){
			throw new IllegalStateException("[CTE] Número do BACEN não preenchido para " + conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais());
		}
		enderReme.setCPais(String.valueOf(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNrBacen()));
		enderReme.setXPais(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais().toString());
		
		rem.setEnderReme(enderReme);
	}

	private void gerarEmit(final InfCte infCte, final Conhecimento conhecimento) {
		Emit emit = new Emit();
		emit.setCNPJ(conhecimento.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
		if(conhecimento.getFilialByIdFilialOrigem().getPessoa().getInscricaoEstaduais() != null){
			for (InscricaoEstadual ie : conhecimento.getFilialByIdFilialOrigem().getPessoa().getInscricaoEstaduais()) {
				if(ie.getBlIndicadorPadrao()){
					emit.setIE(ie.getNrInscricaoEstadual());
					break;
		}
			}
		}
		emit.setXNome(conhecimento.getFilialByIdFilialOrigem().getPessoa().getNmPessoa().trim());
		if(conhecimento.getFilialByIdFilialOrigem().getPessoa().getNmFantasia() != null){
			emit.setXFant(conhecimento.getFilialByIdFilialOrigem().getPessoa().getNmFantasia().trim());
		}
		gerarEnderEmit(emit, conhecimento);
		validaUfInscricaoEstadualEmit(emit, conhecimento);
		
		infCte.setEmit(emit);
	}

	private void validaUfInscricaoEstadualEmit(final Emit emit, final Conhecimento conhecimento){
		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
		String uf = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

		if(conteudoParametro.toString().indexOf(uf) >= 0){
			if(emit.getIE().length() == 9 && emit.getIE().charAt(0) == '0'){
				String ie = emit.getIE().substring(1);
				emit.setIE(ie);
			}
		}
	}

	private void gerarEnderEmit(final Emit emit, final Conhecimento conhecimento) {
		EnderEmit enderEmit = new EnderEmit();
		EnderecoPessoa enderecoPessoa = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa();
		enderEmit.setXLgr(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro() + " " + enderecoPessoa.getDsEndereco().trim());
		if(enderecoPessoa.getNrEndereco()!=null){
			enderEmit.setNro(enderecoPessoa.getNrEndereco().trim());
		}
		if(enderecoPessoa.getDsComplemento() != null){
			enderEmit.setXCpl(enderecoPessoa.getDsComplemento().trim());
		}
		String dsBairro = enderecoPessoa.getDsBairro();
		if(dsBairro == null || dsBairro.length() < 2){
			enderEmit.setXBairro(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			enderEmit.setXBairro(dsBairro.trim());
		}
		enderEmit.setCMun(comporCMun(enderecoPessoa.getMunicipio().getCdIbge(), enderecoPessoa.getMunicipio().getUnidadeFederativa().getNrIbge()));
		enderEmit.setXMun(enderecoPessoa.getMunicipio().getNmMunicipio());
		enderEmit.setCEP(enderecoPessoa.getNrCep());
		enderEmit.setUF((TUF_sem_EX.fromValue(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa())));
		
		String fone = getFone(conhecimento.getFilialByIdFilialOrigem().getPessoa());
		enderEmit.setFone(formatarTelefone(fone));
		
		emit.setEnderEmit(enderEmit);
	}

	private void gerarCompl(final InfCte infCte, final Conhecimento conhecimento, String valorTotalTributo) {
		Compl compl = new Compl();
		compl.setXCaracAd(conhecimento.getTpConhecimento().getDescription().toString());
		if(conhecimento.getServico().getDsServico().toString().length() > 30){
		compl.setXCaracSer(conhecimento.getServico().getDsServico().toString().substring(0, 30));
		} else {
			compl.setXCaracSer(conhecimento.getServico().getDsServico().toString());
		}
		compl.setXEmi(conhecimento.getUsuarioByIdUsuarioInclusao().getLogin());
		gerarFluxo(compl, conhecimento);
		gerarEntrega(compl, conhecimento);
		compl.setOrigCalc(trunc(conhecimento.getMunicipioByIdMunicipioColeta().getNmMunicipio(),40));
		compl.setDestCalc(trunc(conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipio(),40));
		StringBuilder obs = new StringBuilder();
		
		List<ObservacaoDoctoServico> observacoes = conhecimento.getObservacaoDoctoServicos();
		//ordena a lista de observacoes
		
		if(observacoes != null){
			Collections.sort(observacoes, new Comparator<ObservacaoDoctoServico>() {
				public int compare(ObservacaoDoctoServico o1, ObservacaoDoctoServico o2) {
					boolean o1BlPrioriedade = o1.getBlPrioridade() == null ? false : o1.getBlPrioridade();
					boolean o2BlPrioriedade = o2.getBlPrioridade() == null ? false : o2.getBlPrioridade();
					
					return (o1BlPrioriedade && !o2BlPrioriedade) ? -1 : (!o1BlPrioriedade && o2BlPrioriedade) ? 1 : 0;
				}
			});
			
			for (ObservacaoDoctoServico obsDs : observacoes) {
				if(obsDs.getDsObservacaoDoctoServico().length() < 2000 - obs.length()){
					obs.append(obsDs.getDsObservacaoDoctoServico());
					obs.append("| ");
				} else {
					break;
				}
			}
			
			List<String> xObs=doctoServicoService.findXObsCTE(conhecimento.getIdDoctoServico());
			String sXObs = "";
			
			StringBuilder sb = new StringBuilder();
			for (String s : xObs){
				sb.append(s);
				sb.append("| ");
			}

			insertXObsByICMSUFFim(infCte, conhecimento, sb);			
			
			if(sb.length() > 0){
				sXObs = sXObs+sb.toString().trim();
			}
			
			if(obs.length() > 0){
				obs = obs.delete(obs.length() - 2, obs.length());
				sXObs = sXObs+obs.toString().trim();
			}
			
			if(sXObs.length()>0){
				compl.setXObs(sXObs);
			}
		}
		ObsCont[] obsCont = popularObsCont(infCte, valorTotalTributo, conhecimento);
		
		if(obsCont.length > 0){
			compl.setObsCont(obsCont);
			infCte.setCompl(compl);
			}
	}

	private void insertXObsByICMSUFFim(final InfCte infCte,	final Conhecimento conhecimento, StringBuilder sb) {
		ICMSUFFim icmsufFim = infCte.getImp().getICMSUFFim();
	
		if (icmsufFim != null) {
			UnidadeFederativa ufInicio = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa();
			UnidadeFederativa ufFim = getUFFim(conhecimento);
			String pattern = "#,##0.00";
			
			String obsICMSUfFim = getObservacaoICMSUFFim(icmsufFim, ufInicio, ufFim, pattern);
			sb.append(obsICMSUfFim);
			sb.append("| ");
			
			String obsFundoCombatePobreza = getObservacaoFundoCombatePobrebza(icmsufFim, ufFim, pattern);
			sb.append(obsFundoCombatePobreza);
			sb.append("| ");
		}
	}

	private String getObservacaoICMSUFFim(ICMSUFFim icmsufFim, UnidadeFederativa ufInicio, UnidadeFederativa ufFim, String pattern) {
		List<Object> l = new ArrayList<Object>();
		l.add(FormatUtils.formatBigDecimalWithPattern(new BigDecimal(icmsufFim.getVICMSUFIni()), pattern));
		l.add(ufInicio.getSgUnidadeFederativa());
		l.add(FormatUtils.formatBigDecimalWithPattern(new BigDecimal(icmsufFim.getVICMSUFFim()),  pattern));
		l.add(ufFim.getSgUnidadeFederativa());
		return configuracoesFacade.getMensagem("LMS-04542", l.toArray());
	}
	
	private String getObservacaoFundoCombatePobrebza(ICMSUFFim icmsufFim, UnidadeFederativa ufFim, String pattern) {
		List<Object> l = new ArrayList<Object>();
		l.add(FormatUtils.formatBigDecimalWithPattern(new BigDecimal(icmsufFim.getVFCPUFFim()),  pattern));
		l.add(ufFim.getSgUnidadeFederativa());
		return configuracoesFacade.getMensagem("LMS-04544", l.toArray());
	}
	
	private void gerarEntrega(final Compl compl, final Conhecimento conhecimento) {
		Entrega entrega = new Entrega();
		EntregaChoice entregaChoice = new EntregaChoice();
		gerarComData(entregaChoice, conhecimento);
		entrega.setEntregaChoice(entregaChoice);
		EntregaChoice2 entregaChoice2 = new EntregaChoice2();
		gerarSemHora(entregaChoice2, conhecimento);
		entrega.setEntregaChoice2(entregaChoice2);
		
		compl.setEntrega(entrega);
	}

	private void gerarSemHora(final EntregaChoice2 entregaChoice2, final Conhecimento conhecimento) {
		SemHora semHora = new SemHora();
		semHora.setTpHor(TpHorType.VALUE_0);
		entregaChoice2.setSemHora(semHora);
	}

	private void gerarComData(final EntregaChoice entregaChoice, final Conhecimento conhecimento) {
		ComData comData = new ComData();
		comData.setTpPer(TpPerType.VALUE_1);
		comData.setDProg(fmtSemHora.print(conhecimento.getDtPrevEntrega()));
		entregaChoice.setComData(comData);
		
	}

	private void gerarFluxo(final Compl compl, final Conhecimento conhecimento) {
		Fluxo fluxo = new Fluxo();
		fluxo.setXOrig(conhecimento.getFilialByIdFilialOrigem().getSgFilial());
		gerarPass(fluxo, conhecimento);
		fluxo.setXDest(conhecimento.getFilialByIdFilialDestino().getSgFilial());
		if(conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid() != null){
			fluxo.setXRota(StringUtils.leftPad(String.valueOf(conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid().getNrRota()), 3, '0'));
		}
		compl.setFluxo(fluxo);
	}


	private void gerarPass(final Fluxo fluxo, final Conhecimento conhecimento) {
		String[] filiais = conhecimento.getFluxoFilial().getDsFluxoFilial().split(" - ");
		if(filiais.length > 2){
			Pass[] vPassArray = new Pass[filiais.length-2];
			for (int i = 1; i< filiais.length-1; i++) {
				Pass pass = new Pass();
				pass.setXPass(filiais[i-1]);
				vPassArray[i-1] = pass;
			}
			fluxo.setPass(vPassArray);
		}
		
	}

	private void gerarIde(final InfCte infCte, final Conhecimento conhecimento, final Integer random, final char tEmis, final char dv, final Long idPaisBrasil) {
		TpEmisType tpEmis;
		Ide ide = new Ide();
		ide.setCUF(TCodUfIBGE.fromValue(String.valueOf(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge())));
		ide.setCCT(StringUtils.leftPad(String.valueOf(random), 8, '0'));
		
		ide.setCFOP(String.valueOf(conhecimento.getNrCfop()));
		ide.setNatOp("Serviço de Transporte");
		ide.setMod(TModCT.VALUE_57);
		ide.setSerie("0");
		ide.setNCT(String.valueOf(conhecimento.getNrConhecimento()));
		ide.setDhEmi(fmtComHora.print(conhecimento.getDhEmissao()));
		ide.setTpImp(TpImpType.VALUE_1);
		switch (tEmis) {
			case '7': tpEmis = TpEmisType.VALUE_7;
				break;
			case '8': tpEmis = TpEmisType.VALUE_8;
				break;
			default: tpEmis = TpEmisType.VALUE_1;
		}
		ide.setTpEmis(tpEmis);
		ide.setCDV(String.valueOf(dv));
		Object ambienteCte = parametroGeralService.findConteudoByNomeParametro("AMBIENTE_CTE", false);
		ide.setTpAmb(TAmb.fromValue(String.valueOf(ambienteCte)));
		TFinCTe tpCTe;
		if(("CF".equals(conhecimento.getTpConhecimento().getValue()) || "CI".equals(conhecimento.getTpConhecimento().getValue()))){
			tpCTe = TFinCTe.VALUE_1;
		} else if(ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())){
			tpCTe = TFinCTe.VALUE_3;
		} else {
			tpCTe = TFinCTe.VALUE_0;
		}
		ide.setTpCTe(tpCTe);
		ide.setProcEmi(TProcEmi.VALUE_0);
		ide.setVerProc("3.00");
		if(idPaisBrasil.equals(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
			ide.setCMunEnv(comporCMun(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge(), conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()));
			ide.setXMunEnv(String.valueOf(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio()));
			ide.setUFEnv(TUf.fromValue(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		} else {
			ide.setCMunEnv(cdIbgeExterior);
			ide.setXMunEnv(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais() + " " + String.valueOf(conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio()));
			ide.setUFEnv(TUf.EX);
		}
		ide.setModal(TModTransp.VALUE_1);
		TpServType tpServType;
		if(conhecimento.getNrCtrcSubcontratante() != null){
			tpServType = TpServType.VALUE_1;
		} else if(conhecimento.getClienteByIdClienteRedespacho() != null && Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario())){
			tpServType = TpServType.VALUE_3;
		} else if(conhecimento.getClienteByIdClienteRedespacho() != null || Boolean.TRUE.equals(conhecimento.getBlRedespachoColeta())){
			tpServType = TpServType.VALUE_2;
		} else {
			tpServType = TpServType.VALUE_0;
		}
		ide.setTpServ(tpServType);
		
		String tpConhecimento = conhecimento.getTpConhecimento().getValue();
		if ("RF".equals(tpConhecimento) || "DE".equals(tpConhecimento) || "DP".equals(tpConhecimento) || "RE".equals(tpConhecimento)) {
			EnderecoPessoa enderecoPessoa = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa();
			
			ide.setCMunIni(comporCMun(enderecoPessoa.getMunicipio().getCdIbge(), enderecoPessoa.getMunicipio().getUnidadeFederativa().getNrIbge()));			
			ide.setXMunIni(enderecoPessoa.getMunicipio().getNmMunicipio());
			ide.setUFIni(TUf.fromValue(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		}else{
			//Caso TP_CONHECIMENTO for igual a 'CF', 'CI' ou 'NO'
			Municipio municipioColeta = conhecimento.getMunicipioByIdMunicipioColeta();
			ide.setCMunIni(comporCMun(municipioColeta.getCdIbge(), municipioColeta.getUnidadeFederativa().getNrIbge()));

			ide.setXMunIni(municipioColeta.getNmMunicipio());
			ide.setUFIni(TUf.fromValue(municipioColeta.getUnidadeFederativa().getSgUnidadeFederativa()));			
		}
		
		ide.setCMunFim(comporCMun(conhecimento.getMunicipioByIdMunicipioEntrega().getCdIbge(), conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getNrIbge()));
		ide.setXMunFim(conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipio());
		ide.setUFFim(TUf.fromValue(conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getSgUnidadeFederativa()));
		ide.setRetira(RetiraType.VALUE_1);
		ide.setXDetRetira(null);
		gerarIndIEToma(ide, conhecimento);
		gerarIdeChoice(ide, conhecimento, idPaisBrasil);
		
		infCte.setIde(ide);
	}

	private void gerarIndIEToma(Ide ide, Conhecimento conhecimento) {
		DevedorDocServ devedorDocServ = conhecimento.getDevedorDocServs().get(0);
		
		Boolean hasPessoaFisica = "F".equals(devedorDocServ.getCliente().getPessoa().getTpPessoa().getValue());
		
		InscricaoEstadual inscricaoEstadual = devedorDocServ.getInscricaoEstadual();
		if(inscricaoEstadual == null){
			inscricaoEstadual = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(devedorDocServ.getCliente().getIdCliente());
		}
		
		Boolean temIE = inscricaoEstadual != null && inscricaoEstadual.getNrInscricaoEstadual() != null;
		Boolean temIEISENTO = temIE && "ISENTO".equalsIgnoreCase(inscricaoEstadual.getNrInscricaoEstadual());
		
		Boolean hasContribuinte = temIE && tipoTributacaoIEService.validateTipotributacaoIEContribuinteByIE(inscricaoEstadual.getIdInscricaoEstadual());
		Boolean hasNaoContribuinte = temIE && tipoTributacaoIEService.validateTipotributacaoIENaoContribuinteByIE(inscricaoEstadual.getIdInscricaoEstadual());
		Boolean naoTemTributacao = !hasContribuinte && !hasNaoContribuinte;
		
		if ((!temIE) || (temIEISENTO && naoTemTributacao) || (temIE && hasNaoContribuinte)) {
			ide.setIndIEToma(IndIETomaType.VALUE_9);
		} else if (temIEISENTO && hasContribuinte) {
			ide.setIndIEToma(IndIETomaType.VALUE_2);
		} else if (!temIEISENTO && hasContribuinte) {
			ide.setIndIEToma(IndIETomaType.VALUE_1);
		} else if (!hasPessoaFisica && naoTemTributacao){
			throw new IllegalStateException("[CTe] Incrição estadual não possue situação de tributação vigente.");
		}
	}
	
	private void gerarIdeChoice(final Ide ide, final Conhecimento conhecimento, final Long idPaisBrasil) {
		IdeChoice ideChoice = new IdeChoice();
		if("O".equals(conhecimento.getTpDevedorFrete().getValue())){
			gerarToma4(ideChoice, conhecimento, idPaisBrasil, ide);
		} else {
			gerarToma3(ideChoice, conhecimento);
		}
		
		ide.setIdeChoice(ideChoice);
	}

	private void gerarToma3(final IdeChoice ideChoice, final Conhecimento conhecimento) {
		Toma3 toma3 = new Toma3();
		TomaType tomaType;
		switch (conhecimento.getTpDevedorFrete().getValue().charAt(0)) {
		case 'R':
			tomaType = TomaType.VALUE_0;
			break;
		case 'P':
			tomaType = TomaType.VALUE_1;
			break;
		case 'C':
			tomaType = TomaType.VALUE_2;
			break;
		case 'D':
			tomaType = TomaType.VALUE_3;
			break;
		default:
			throw new IllegalStateException("[CTe] tpDevedorFrete não aceito pela rotina CTE");
		}
		
		toma3.setToma(tomaType);
		
		ideChoice.setToma3(toma3);
	}

	private void gerarToma4(final IdeChoice ideChoice, final Conhecimento conhecimento, final Long idPaisBrasil, Ide ide) {
		Toma4 toma4 = new Toma4();
		toma4.setToma(TomaType.VALUE_4);
		DevedorDocServ devedorDocServ = conhecimento.getDevedorDocServs().get(0);
		Toma4Choice toma4Choice = new Toma4Choice();
		if(idPaisBrasil.equals(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
			if("J".equals(devedorDocServ.getCliente().getPessoa().getTpPessoa().getValue())){
				toma4Choice.setCNPJ(devedorDocServ.getCliente().getPessoa().getNrIdentificacao());
			} else {
				toma4Choice.setCPF(devedorDocServ.getCliente().getPessoa().getNrIdentificacao());
			}
		} else {
			toma4Choice.setCNPJ(cnpjExterior);
		}
		toma4.setToma4Choice(toma4Choice);
		InscricaoEstadual ie = devedorDocServ.getInscricaoEstadual() != null ?
                        devedorDocServ.getInscricaoEstadual() :
                        findIEPadrao(devedorDocServ.getCliente().getPessoa().getInscricaoEstaduais());
		if( ie != null && !ide.getIndIEToma().equals(IndIETomaType.VALUE_9)){
                        toma4.setIE(ie.getNrInscricaoEstadual());
		}else {
			toma4.setIE("");	
		}
		
		Toma4Sequence toma4Sequence = new Toma4Sequence();
		toma4Sequence.setXNome(devedorDocServ.getCliente().getPessoa().getNmPessoa().trim());
		if(devedorDocServ.getCliente().getPessoa().getNmFantasia() != null){
			toma4Sequence.setXFant(devedorDocServ.getCliente().getPessoa().getNmFantasia().trim());
		}
		
		String fone = getFone(devedorDocServ.getCliente().getPessoa());
		toma4Sequence.setFone(formatarTelefone(fone));
		gerarEnderToma(toma4Sequence, conhecimento, devedorDocServ, idPaisBrasil);
		toma4Sequence.setEmail(devedorDocServ.getCliente().getPessoa().getDsEmail());
		validaUfInscricaoEstadualToma(toma4, devedorDocServ);
		
		toma4.setToma4Sequence(toma4Sequence);
		
		ideChoice.setToma4(toma4);
	}

	private void validaUfInscricaoEstadualToma(final Toma4 toma4, final DevedorDocServ devedorDocServ){
		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
		String uf = devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

		if(conteudoParametro.toString().indexOf(uf) >= 0){
			if(toma4.getIE().length() == 9 && toma4.getIE().charAt(0) == '0'){
				String ie = toma4.getIE().substring(1);
				toma4.setIE(ie);
			}
		}
	}
	
	private boolean preencherInscricaoEstadual(final InscricaoEstadual inscricaoEstadual) {
		TipoTributacaoIE tipoTributacaoIE = tipoTributacaoIEService.findTiposTributacaoIEVigente(inscricaoEstadual.getIdInscricaoEstadual(), JTDateTimeUtils.getDataAtual());
		String tpSituacaoTributaria = tipoTributacaoIE.getTpSituacaoTributaria().getValue();
		// Não preencher se TIPO_TRIBUTACAO_IE.TP_SITUACAO_TRIBUTARIA in (NC,DN,MN,PN,ON)
		return !(tpSituacaoTributaria.equals("NC") || tpSituacaoTributaria.equals("DN") || tpSituacaoTributaria.equals("MN") || tpSituacaoTributaria.equals("PN") || tpSituacaoTributaria.equals("ON"));
	}

	private void gerarEnderToma(final Toma4Sequence toma4Sequence, final Conhecimento conhecimento, final DevedorDocServ devedorDocServ, final Long idPaisBrasil) {
		EnderToma enderToma = new EnderToma();
		
		enderToma.setXLgr(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getDsEndereco().trim());
		if(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getNrEndereco()!=null){
			enderToma.setNro(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getNrEndereco().trim());
		}
		if(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getDsComplemento() != null){
			enderToma.setXCpl(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getDsComplemento().trim());
		}
		String dsBairro = devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getDsBairro();
		if(dsBairro == null || dsBairro.length() < 2){
			enderToma.setXBairro(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			enderToma.setXBairro(dsBairro.trim());
		}
		if(idPaisBrasil.equals(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getIdPais())){
			enderToma.setCMun(comporCMun(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge(), devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getNrIbge()));
			enderToma.setXMun(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio());
			enderToma.setCEP(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getNrCep());
			enderToma.setUF(TUf.fromValue(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		} else {
			enderToma.setCMun(cdIbgeExterior);
			enderToma.setXMun(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais() + " " + String.valueOf(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio()));
			enderToma.setUF(TUf.EX);
		}
		enderToma.setCPais(String.valueOf(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNrBacen()));
		enderToma.setXPais(devedorDocServ.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais().getNmPais().toString());
		
		toma4Sequence.setEnderToma(enderToma);
	}
	
	private static String formatarTelefone(String telefone){
		if(telefone == null){
			return null;
		}
		String fone = telefone.replaceAll("[^0-9]+","");
		if(fone.length() > 6){
			return fone;
		}
		return null;
	}
	
	private static int modulo11(String numero){
		char[] n = numero.toCharArray();
		int peso = 2;
		int soma = 0;
		for (int i = n.length-1; i >= 0; i--) {
			soma += Integer.valueOf(String.valueOf(n[i])) * peso++;
			if(peso == 10){
				peso = 2;
			}
		}
		int mod = soma % 11;
		int dv;
		
		if(mod == 0 || mod == 1){
			dv = 0;
		} else {
			dv = 11 - mod;
		}
		
		return dv;
	}

	private String trunc(String value,int length){
		if( value != null && value.length() > length ){
			return value.substring(0, length);
		}
		return value;
	}
	
	private Long getIdUfFilialSessao(){
		if( SessionUtils.getFilialSessao().getPessoa() != null ){
			Pessoa pessoa = SessionUtils.getFilialSessao().getPessoa();
			if( pessoa.getEnderecoPessoa() != null ){
				EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();
				if( enderecoPessoa.getMunicipio() != null ){
					Municipio municipio = enderecoPessoa.getMunicipio();
					if( municipio.getUnidadeFederativa() != null ){
						return municipio.getUnidadeFederativa().getIdUnidadeFederativa();
					}
				}
			}
		}
		return null;
	}
	
	private String getFone(final Pessoa pessoa) {
		for (EnderecoPessoa enderecoPessoa : pessoa.getEnderecoPessoas()) {
			for (TelefoneEndereco telefoneEndereco : enderecoPessoa.getTelefoneEnderecos()) {
				if (isTelefoneCliente(pessoa, telefoneEndereco)) {
					StringBuilder fone = new StringBuilder();
					fone.append(telefoneEndereco.getNrDdd()).append(telefoneEndereco.getNrTelefone());
					return fone.toString();
				}
			}
		}
		return null;
	}

	private boolean isTelefoneCliente(final Pessoa pessoa, TelefoneEndereco telefoneEndereco) {
		return isTelefone(telefoneEndereco) && (isTelefonePessoaJuridica(pessoa, telefoneEndereco) || isTelefonePessoaFisica(pessoa, telefoneEndereco));
	}

	private boolean isTelefone(TelefoneEndereco telefoneEndereco) {
		return "FO".equals(telefoneEndereco.getTpUso().getValue()) || "FF".equals(telefoneEndereco.getTpUso().getValue());
	}

	private boolean isTelefonePessoaFisica(final Pessoa pessoa, TelefoneEndereco telefoneEndereco) {
		return "F".equals(pessoa.getTpPessoa().getValue()) && "R".equals(telefoneEndereco.getTpTelefone().getValue());
	}

	private boolean isTelefonePessoaJuridica(final Pessoa pessoa, TelefoneEndereco telefoneEndereco) {
		return "J".equals(pessoa.getTpPessoa().getValue()) && "C".equals(telefoneEndereco.getTpTelefone().getValue());
	}
	
	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}
	
	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setCalcularIcmsService(CalcularIcmsService calcularIcmsService) {
		this.calcularIcmsService = calcularIcmsService;
	}

	public void setContingenciaService(ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}

	public void setAliquotaFundoCombatePobrezaService(AliquotaFundoCombatePobrezaService aliquotaFundoCombatePobrezaService) {
		this.aliquotaFundoCombatePobrezaService = aliquotaFundoCombatePobrezaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public AliquotaIcmsInternaService getAliquotaIcmsInternaService() {
		return aliquotaIcmsInternaService;
	}

	public void setAliquotaIcmsInternaService(AliquotaIcmsInternaService aliquotaIcmsInternaService) {
		this.aliquotaIcmsInternaService = aliquotaIcmsInternaService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

	private Boolean verificaClienteDadoComplemento(List<DadosComplemento> dadosComplemento, Conhecimento conhecimento) {
		String cnpjRaizFDX = parametroGeralService.findSimpleConteudoByNomeParametro("CNPJ_FDX_POS");
		Boolean remFDX = conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao().contains(cnpjRaizFDX);
		return remFDX && dadosComplemento.stream().anyMatch(dadoComplemento -> "POS".equals(dadoComplemento.getDsValorCampo()));
	}
}
