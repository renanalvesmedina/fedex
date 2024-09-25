package com.mercurio.lms.facade.radar.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;
import org.springframework.core.io.ClassPathResource;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.model.util.DateTimeUtils;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.AgenciaBancariaService;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.service.BoletoService;
import com.mercurio.lms.contasreceber.model.service.CedenteService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.expedicao.model.service.GerarConhecimentoEletronicoXMLService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.expedicao.reports.NFeJasperReportFiller;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.facade.radar.PosicaoFinanceiraFacade;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Adércio Reinan
 * @spring.bean id="lms.posicaoFinanceiraFacade"
 */
@ServiceSecurity
public class PosicaoFinanceiraFacadeImpl implements PosicaoFinanceiraFacade {
	
	private static final Logger LOGGER = LogManager.getLogger(PosicaoFinanceiraFacadeImpl.class);

	private static final String PC_MULTA_ATRASO_BOLETO = "PC_MULTA_ATRASO_BOLETO";
	
	private static final int NR_VIAS = 1;

	private static final String PDF = "pdf";
	private BancoService bancoService;
	private BoletoService boletoService;
	private AgenciaBancariaService agenciaService;
	private CedenteService cedenteService;
	private ClienteService clienteService;
	private FaturaService faturaService;
	private UsuarioService usuarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private PaisService paisService;
	private ReportExecutionManager reportExecutionManager;
	private ParametroGeralService parametroGeralService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;
	private FilialService filialService;
	private UsuarioADSMService usuarioADSMService;
	private MoedaService moedaService;
	private RecursoMensagemService recursoMensagemService;
	private ContatoService contatoService;
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "gerarXMLConhecimentoEletronico", authenticationRequired=false)
	public List<TypedFlatMap> gerarXMLConhecimentoEletronico(TypedFlatMap criteria) {
		Long idFatura = criteria.getLong("fatura.idFatura");
		return monitoramentoDocEletronicoService.gerarXMLConhecimentoEletronico(idFatura);
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findExistsConhecimentoEletronicoByCriteria", authenticationRequired=false)
	public TypedFlatMap findExistsConhecimentoEletronicoByCriteria(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idFatura", criteria.getLong("idFatura"));
		param.put("isCte", criteria.getBoolean("isCte"));
		param.put("isNte", criteria.getBoolean("isNte"));
		boolean isExist = monitoramentoDocEletronicoService.findExistsConhecimentoEletronicoByCriteria(param);
		map.put("isExist", isExist);
		return map;
	}

	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findByNomeParametro", authenticationRequired=false)
	public TypedFlatMap findByNomeParametro(TypedFlatMap criteria){
		TypedFlatMap map = new TypedFlatMap();
		String nomeParametroGeral = criteria.getString("nomeParametroGeral");
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(nomeParametroGeral);
		map.put("dsConteudo", parametroGeral.getDsConteudo());
		map.put("nmParametroGeral", parametroGeral.getNmParametroGeral());
		return map;
	}

	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "imprimirSegundaViaFatura", authenticationRequired=false)
	public TypedFlatMap imprimirSegundaViaFatura(TypedFlatMap criteria) throws Exception {
		Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
		Long idFatura = criteria.getLong("idFatura");
		Fatura fatura = faturaService.findFaturaByIdFatura(idFatura);
		criteria.put("faturaInicial", fatura.getNrFatura());
		criteria.put("faturaFinal", fatura.getNrFatura());
		criteria.put("modal", fatura.getTpModal().getValue());
		criteria.put("abrangencia", fatura.getTpAbrangencia().getValue());
		criteria.put("filial.idFilial", fatura.getFilialByIdFilial().getIdFilial());
		criteria.put("tpFormatoRelatorio", PDF);
		criteria.put("reemitir", Boolean.FALSE);
		SessionContext.setUser(usuario);
		SessionContext.set("PAIS_KEY", paisService.findPaisBySgPais("BRA"));
		Moeda moeda = moedaService.findById(fatura.getMoeda().getIdMoeda());
		SessionContext.set("MOEDA_KEY", moeda);
		Filial filial = filialService.findByIdBasic(fatura.getFilialByIdFilial().getIdFilial());
		SessionContext.set("FILIAL_KEY", filial);
		
		MultiReportCommand mrc = new MultiReportCommand("emitirSegundaViaFatura"); 
		mrc.addCommand("lms.contasreceber.emitirFaturasNacionaisService", criteria);

		File reportFile = this.reportExecutionManager.executeMultiReport(mrc);

		TypedFlatMap retorno = new TypedFlatMap();

		final byte[] inputFile = FileUtils.readFile(reportFile);
		retorno.put("faturaReport", inputFile);
		retorno.put("nrFatura", fatura.getNrFatura());
		retorno.put("sgFilial", filial.getSgFilial());
		return retorno;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "imprimirBoletoComDataFutura", authenticationRequired=false)
	public TypedFlatMap imprimirBoletoComDataFutura(TypedFlatMap criteria) throws Exception {
		Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
		SessionContext.setUser(usuario);
		SessionContext.set("PAIS_KEY", paisService.findPaisBySgPais("BRA"));
		
		Fatura fatura = faturaService.findFaturaByIdFatura(criteria.getLong("fatura.idFatura"));
		criteria.put("idBoleto", fatura.getBoleto().getIdBoleto());
		criteria.put("filial.idFilial", fatura.getFilialByIdFilial().getIdFilial());
		criteria.put("cedente.idCedente", fatura.getCedente().getIdCedente());
		criteria.put("tpFormatoRelatorio", "pdf");
		criteria.put("reemissao", Boolean.FALSE);
		Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
		criteria.put("boletoInicial", boleto.getNrBoleto());
		criteria.put("boletoFinal", boleto.getNrBoleto());
		
		calcularJurosBoleto(boleto, criteria);
		
		MultiReportCommand mrc = new MultiReportCommand("emitirBoletos"); 
		mrc.addCommand("lms.contasreceber.emitirBoletoService", criteria);

		File reportFile = this.reportExecutionManager.executeMultiReport(mrc);

		TypedFlatMap retorno = new TypedFlatMap();

		final byte[] inputFile = FileUtils.readFile(reportFile);
		retorno.put("boletoReport", inputFile);
		retorno.put("nrFatura", fatura.getNrFatura());
		Filial filial = filialService.findByIdBasic(fatura.getFilialByIdFilial().getIdFilial());
		retorno.put("sgFilialOrigemFatura",filial.getSgFilial());
		return retorno;
	}

	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findCnpjAutorizadoByUser", authenticationRequired=false)
	public List<TypedFlatMap> findCnpjAutorizadoByUser(TypedFlatMap criteria) {
		Long idUsuario = criteria.getLong("idUsuario");
		List<Object[]> listFromResult = clienteService.findCnpjAutorizadoByUser(idUsuario);
		return parseResultCnpjAutorizadoByUserToMap(listFromResult);
	}
	

	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "getRowCountFaturaByCriteria", authenticationRequired=false)
	public List<TypedFlatMap> getRowCountFaturaByCriteria(TypedFlatMap criteria) throws Exception {
		validateCriteriaFatura(criteria);
		List<Object[]> listCount =  faturaService.getRowCountFaturaByCriteria(criteria);
		return parseResultCountFaturaToMap(listCount);
	}
	
	private List<TypedFlatMap> parseResultCountFaturaToMap(List<Object[]> listFromResult) {
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());
		TypedFlatMap map = null;
		for (final Object[] objResult : listFromResult) {
			map = new TypedFlatMap();
			map.put("qtde_registros", objResult[0]);
			map.put("cnpj", objResult[1]);
			map.put("valor", objResult[2]);
			listRetorno.add(map);
		}

		return listRetorno;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findFaturaByCriteria", authenticationRequired=false)
	public List<TypedFlatMap> findFaturaByCriteria(TypedFlatMap criteria) {
		validateCriteriaFatura(criteria);
		List<Object[]> listFromResult = faturaService.findFaturaByCriteria(criteria);
		return parseResultFaturaByUserToMap(listFromResult);
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findFaturaById", authenticationRequired=false)
	public TypedFlatMap findFaturaById(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		Fatura fatura = faturaService.findById(criteria.getLong("idFatura"));//faturaService.findFaturaByIdFatura(criteria.getLong("idFatura"));
		Filial filial = filialService.findByIdBasic(fatura.getFilialByIdFilial().getIdFilial());
		map.put("idFatura", fatura.getIdFatura());
		map.put("nrFatura", fatura.getNrFatura());
		map.put("tpSituacaoFatura", fatura.getTpSituacaoFatura().getValue());
		map.put("nrIdentificacao", fatura.getCliente().getPessoa().getNrIdentificacao());
		map.put("blEnviaDocsFaturamentoNas", fatura.getCliente().getBlEnviaDocsFaturamentoNas());
		
		map.put("idPessoa", fatura.getCliente().getPessoa().getIdPessoa());
		map.put("sgFilial", filial.getSgFilial());
		return map;
	}
	
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findBoletoByIdFatura", authenticationRequired=false)
	public TypedFlatMap findBoletoByIdFatura(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Long idFatura = criteria.getLong("idFatura");
		Boleto boleto = boletoService.findByFatura(idFatura);
		if(boleto != null){
			tfm.put("idBoleto", boleto.getIdBoleto());
			tfm.put("tpSituacao", boleto.getTpSituacaoBoleto().getValue());
			tfm.put("dtVencimento", boleto.getDtVencimento());
			tfm.put("vlJurosDia", boleto.getVlJurosDia());
			tfm.put("vlTotal", boleto.getVlTotal());
			tfm.put("idCedente", boleto.getCedente().getIdCedente());
		}
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findBancoByNrBanco", authenticationRequired=false)
	public TypedFlatMap findBancoByNrBanco(TypedFlatMap criteria){
		TypedFlatMap tfm = new TypedFlatMap();
		String nrBanco = criteria.getString("nrBanco");
		Banco banco = bancoService.findByNrBanco(nrBanco);
		if(banco != null){
			tfm.put("nrBanco", banco.getNrBanco());
			tfm.put("idBanco", banco.getIdBanco());
			tfm.put("nmBanco", banco.getNmBanco());
			tfm.put("nmBancoNmPais", banco.getNmBancoNmPais());
		}
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findBoletoByNrBoleto", authenticationRequired=false)
	public TypedFlatMap findBoletoByNrBoleto(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		String nrBoleto = criteria.getString("nrBoleto");
		Boleto boleto = boletoService.findByNrBoleto(nrBoleto);
		if(boleto != null){
			tfm.put("nrBoleto", boleto.getNrBoleto());
			tfm.put("idCedente", boleto.getCedente().getIdCedente());
			tfm.put("idBoleto", boleto.getIdBoleto());
			tfm.put("idFatura", boleto.getFatura().getIdFatura());
			
		}
		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findCedenteByIdCedente", authenticationRequired=false)
	public TypedFlatMap findCedenteByIdCedente(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Long idCedente = criteria.getLong("idCedente");
		Cedente cedente = cedenteService.findById(idCedente);
		if(cedente != null){
			tfm.put("idAgenciaBancaria", cedente.getAgenciaBancaria().getIdAgenciaBancaria());
			tfm.put("idCedente", cedente.getIdCedente());
			tfm.put("nrContaCorrente", cedente.getNrContaCorrente());
		}
		return tfm;
	}

	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findAgenciaById", authenticationRequired=false)
	public TypedFlatMap findAgenciaById(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		Long idAgencia = criteria.getLong("idAgenciaBancaria");
		AgenciaBancaria agencia = agenciaService.findById(idAgencia);
		if(agencia != null){
			tfm.put("idAgenciaBancaria", agencia.getIdAgenciaBancaria());
			tfm.put("idBanco", agencia.getBanco().getIdBanco());
			tfm.put("nrAgencia", String.valueOf(agencia.getNrAgenciaBancaria()));
		}
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findCustomersByNrIdentificacao", authenticationRequired=false)
	public List<TypedFlatMap> findCustomersByNrIdentificacao(String nrsIdentificacao){
		List<Object[]> listCustomer = clienteService.findCustomersByNrIdentificacao(nrsIdentificacao);
		return parseResultCustomerToMap(listCustomer);
    }
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "executeEmitirCTEbyIdFatura", authenticationRequired=false)
	public TypedFlatMap executeEmitirCTEbyIdFatura(TypedFlatMap parameters){
		TypedFlatMap tfm = new TypedFlatMap();
		List<Long> listFaturas = new ArrayList<Long>();
		Long idFatura = parameters.getLong("idFatura");
		listFaturas.add(idFatura);

		try {
			File file = emiteCTEbyListFaturas(listFaturas);
			if(file != null){
				tfm.put("fileCte", FileUtils.readFile(file));
			}
		} catch (Exception e) {
			throw new ADSMException(e);
		}
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "executeEmitirNTEbyIdFatura", authenticationRequired=false)
	public TypedFlatMap executeEmitirNTEbyIdFatura(TypedFlatMap parameters){
		TypedFlatMap tfm = new TypedFlatMap();
		List<Long> listFaturas = new ArrayList<Long>();
		Long idFatura = parameters.getLong("idFatura");
		listFaturas.add(idFatura);
		List<Map<String, Object>> listCte = monitoramentoDocEletronicoService.findByListFatura(listFaturas, ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA);
		try{
			File file = emiteNTE(listCte);
			if(file != null){
				tfm.put("fileRps", FileUtils.readFile(file));
			}
		}catch (Exception e) {
			throw new ADSMException(e);
		}
		return tfm;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.posicaoFinanceira", processName = "findUserByLogin", authenticationRequired=false)
	public TypedFlatMap findUserByLogin(TypedFlatMap criteria){
		TypedFlatMap map = new TypedFlatMap();
		String login = criteria.getString("login");
		UsuarioADSM usuario = usuarioADSMService.findUsuarioADSMByLogin(login);
		map.put("idUsuarioCliente", usuario.getIdUsuario());
		map.put("idUsuario", usuario.getIdUsuario());
		map.put("login", usuario.getLogin());
		map.put("nomeUsuario", usuario.getNmUsuario());
		map.put("email", usuario.getDsEmail());
		map.put("ddd", usuario.getNrDdd());
		map.put("fone", usuario.getNrFone());
		map.put("tpCategoriaUsuario", usuario.getTpCategoriaUsuario().getValue());
		return map;
		
	}
	
	@Override
	@MethodSecurity
		(
			processGroup = "radar.posicaoFinanceira", 
			processName = "findContatosByIdPessoaAndTipoContato", authenticationRequired=false)
	public List<TypedFlatMap> findContatosByIdPessoaAndTipoContato(TypedFlatMap criteria) {
		List<TypedFlatMap> listMapEmail = new ArrayList<>();
		TypedFlatMap map = null;
		
		Long idPessoa = criteria.getLong("idPessoa");
		String tpContato = criteria.getString("tpContato");
		
		List<Contato> contatos = contatoService.findContatosByIdPessoaAndTipoContato(idPessoa, tpContato);
		
		for(Contato contato : contatos) {
			map = new TypedFlatMap();
			map.put("dsEmail", contato.getDsEmail());
			listMapEmail.add(map);
		}
		
		return listMapEmail;
	}

	
	@Override
	@MethodSecurity
		(
			processGroup = "radar.posicaoFinanceira", 
			processName = "storeMonitoramentoMensagem", authenticationRequired=false)
	public TypedFlatMap storeMonitoramentoMensagem(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		try {
			faturaService.storeMonitoramentoMensagem(criteria);
			map.put("erro", false);
		}catch (Exception e) {
			map.put("erro", true);
		}
		
		return map;
	}

	@Override
	@MethodSecurity
		(
			processGroup = "radar.posicaoFinanceira", 
			processName = "findRecursoMensagemByChave", authenticationRequired=false
		)
	public TypedFlatMap findRecursoMensagemByChave(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		String chave = criteria.getString("chave");
		String mensagem = recursoMensagemService.findByChave(chave);
		map.put("mensagem", mensagem);
		return map;
	}

	@Override
	@MethodSecurity
		(
			processGroup = "radar.posicaoFinanceira", 
			processName = "existeMonitoramentoMensagem", authenticationRequired=false
		)
	public TypedFlatMap existeMonitoramentoMensagem(TypedFlatMap criteria) {
		
		TypedFlatMap map = new TypedFlatMap();
		
		Long idFatura = criteria.getLong("idFatura");
		
		boolean existeMonitoramentoMensagem = faturaService.existeMonitoramentoMensagem(idFatura);
		
		map.put("existe", existeMonitoramentoMensagem);
		
		return map;
	}
	
	private File emiteCTEbyListFaturas(List<Long> faturas) {
			List<Map<String, Object>> listCtes = monitoramentoDocEletronicoService.findByListFatura(faturas, ConstantesExpedicao.CONHECIMENTO_ELETRONICO);
			if (listCtes == null || listCtes.isEmpty()) {
				return null;
			}
			
			gerarConhecimentoEletronicoXMLService.addListXmlCteComComplementos(listCtes);
			File result = null;
		
			Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
			SessionContext.setUser(usuario);
			final Usuario currentUser = SessionContext.getUser();
			Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? 
											currentUser.getLocale() : new Locale("pt","BR");
	
			try{
				HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
				String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
				
				JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);
				
				result =  runner.executeReport(listCtes, NR_VIAS); 
			}catch (Exception e) {
				throw new ADSMException(e);
			}
		return result;
		
	}
	
	private File emiteNTE(List<Map<String, Object>> listCtes) {
			File result = null;
			if (listCtes == null || listCtes.isEmpty()) {
				return null;
			}
			Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
			SessionContext.setUser(usuario);
			final Usuario currentUser = SessionContext.getUser();
			Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");
					
			try{
				HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
				String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
				
				JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);
	
				ClassPathResource jasperResourceNte = new ClassPathResource("com/mercurio/lms/expedicao/reports/impressaoRPST.jasper");
				JasperPrint jasperPrintNte = null;
				for (Map<String, Object> map : listCtes) {
					Map<String, Object> nfeInfs = (Map<String, Object>) map.get("nfeInfs");
					JasperPrint jasperPrint = NFeJasperReportFiller.executeFillXmlJasperReportRpst(1,
							(String)nfeInfs.get("nfeXML"),
							(List)nfeInfs.get("listNrNotas"),
							(Map<String, String>)nfeInfs.get("infRpst"),
							(List)nfeInfs.get("dsObservacaoDoctoServico"),
							currentUserLocale, jasperResourceNte.getInputStream(), host);
					if(jasperPrintNte == null){
						jasperPrintNte = jasperPrint;
					} else {
						jasperPrintNte.getPages().addAll(jasperPrint.getPages());
					}
				}
				result = runner.createPdf(jasperPrintNte, "nte");
				
			}catch (Exception e) {
				throw new ADSMException(e);
			}
		return result;
	}
	
	private List<TypedFlatMap> parseResultCustomerToMap(List<Object[]> listFromResult) {
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());
		TypedFlatMap map = null;
		for (final Object[] objResult : listFromResult) {
			map = new TypedFlatMap();
			map.put("id", objResult[0]);
			map.put("cpfCnpj", objResult[1]);
			listRetorno.add(map);
		}

		return listRetorno;
	}
	
	private List<TypedFlatMap> parseResultFaturaByUserToMap(List<Object[]> listFromResult) {
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());
		TypedFlatMap map = null;
		for (final Object[] objResult : listFromResult) {
			map = new TypedFlatMap();
			map.put("idFatura", objResult[0]);
			map.put("filialEmissora", objResult[1]);
			map.put("nmFantasiaFilialEmissora", objResult[2]);
			map.put("nrFatura", objResult[3]);
			map.put("dataEmissao", objResult[4]);
			map.put("dataVencimento", objResult[5]);
			map.put("dataLiquidacao", objResult[6]);
			map.put("cnpjDevedor", objResult[7]);
			map.put("nomeDevedor", objResult[8]);
			map.put("filialCobradora", objResult[9]);
			map.put("nmFantasiaFilialCobradora", objResult[10]);
			map.put("qtdDocumentos", objResult[11]);
			map.put("valorTotal", objResult[12]);
			map.put("nrBoleto", objResult[13]);
			map.put("situacao", objResult[14]);
			listRetorno.add(map);
		}

		return listRetorno;
	}
	
	private List<TypedFlatMap> parseResultCnpjAutorizadoByUserToMap(
			List<Object[]> listFromResult) {
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());
		TypedFlatMap map = null;
		for (final Object[] objResult : listFromResult) {
			map = new TypedFlatMap();
			map.put("idPessoa", objResult[0]);
			map.put("cpfcnpj", objResult[1]);
			listRetorno.add(map);
		}

		return listRetorno;
	}

	private void calcularJurosBoleto(Boleto boleto, TypedFlatMap criteria) {
		Long diasAtraso = calculaDiasEmAtraso(DateTimeUtils.convertYearMonthDayToString(boleto.getDtVencimento()),
				criteria.getString("boleto.dtVencFut"));
		BigDecimal moraMulta = BigDecimalUtils.ZERO;
		BigDecimal valorCobrado = BigDecimalUtils.ZERO; 
		if(diasAtraso > 0){
			ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(PC_MULTA_ATRASO_BOLETO);
			BigDecimal percetualMulta = new BigDecimal(parametroGeral.getDsConteudo());
			BigDecimal totalJuros = boleto.getVlJurosDia().multiply(new BigDecimal(diasAtraso));
			BigDecimal totalMulta = boleto.getVlTotal().multiply(percetualMulta.divide(BigDecimalUtils.HUNDRED));
			moraMulta = totalMulta.add(totalJuros);
			valorCobrado = boleto.getVlTotal().add(moraMulta);
			valorCobrado = BigDecimalUtils.round(valorCobrado);
			criteria.put("moraMulta", moraMulta);
			criteria.put("valorCobrado", valorCobrado);
		}
		
	}

	private Long calculaDiasEmAtraso(String dataVencBoleto, String dataVencFuturo){
		Calendar d1 = DateTimeUtils.convertStringToCalendar(dataVencFuturo);
		Calendar d2 = DateTimeUtils.convertStringToCalendar(dataVencBoleto);
		return DateTimeUtils.diferencaDias(d2.getTime(), d1.getTime());
	}

	private void validateCriteriaFatura(TypedFlatMap criteria) {
		if (criteria.getLong("nrFatura") == null && criteria.getLong("nrBoleto") == null 
				&& criteria.getLong("idFatura") == null  && criteria.getString("idsClientesSelecionados") == null) {
			throw new IllegalArgumentException("Parâmetros de busca incompleto.");
		}
		
	}

	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setAgenciaService(AgenciaBancariaService agenciaService) {
		this.agenciaService = agenciaService;
	}

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}


	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setUsuarioService(
			com.mercurio.lms.configuracoes.model.service.UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setGerarConhecimentoEletronicoXMLService(
			GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService) {
		this.gerarConhecimentoEletronicoXMLService = gerarConhecimentoEletronicoXMLService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public RecursoMensagemService getRecursoMensagemService() {
		return recursoMensagemService;
	}

	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public ContatoService getContatoService() {
		return contatoService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
	
		
}
