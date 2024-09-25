package com.mercurio.lms.tributos.model.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ObservacaoICMSPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.mocks.ClienteServiceMocker;
import com.mercurio.lms.mocks.ConfiguracoesFacadeMocker;
import com.mercurio.lms.mocks.UnidadeFederativaServiceMocker;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.ExcecaoICMSIntegrantesContribuintes;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;
import com.mercurio.lms.vendas.model.Cliente;

@Test
public class CalcularIcmsServiceTest {
	
	private static final String PC_CRED_PRESUMIDO_BA = "0.25";
	private static final String PARAMETER_PC_CRED_PRESUMIDO_BA = "PC_CRED_PRESUMIDO_BA";
	private static final Long ID_PESSOA = Long.valueOf(88888888);
	private static final String PARAMETER_UFS_90 = "UFS_UTILIZAM_CODIGO_90";
	private static final String PARAMETER_UTILIZA_CODIGO_90_ST = "BL_UTILIZA_CODIGO_90_ST";
	private static final BigDecimal BIG_VALUE = BigDecimal.valueOf(999999999);
	private static final String UF_PORTO_ALEGRE = "POA";
	private static final String PARAMETER_PREENCHE_241 = "UFS_PREENCHE_CAMPO_241";
	private static final String VALID_UFS = "MG;BA;SP";
	private static final String UF_MINAS_GERAIS = "MG";
	private CalcularIcmsService service;
	private static final Long ID_UNIDADE_FEDERATIVA = Long.valueOf(99999);
	private static final Long ID_BRASIL = Long.valueOf(77777);
	private static final Long ID_URUGUAI = Long.valueOf(66666);
	
	@Mock ParametroGeralService parametroService;
	@Mock UnidadeFederativaService ufService;

	@Mock InscricaoEstadualService inscricaoEstadualService;
	@Mock TipoTributacaoIEService tipoTributacaoIEService;
	@Mock ObservacaoICMSPessoaService observacaoICMSPessoaService;
	@Mock ObservacaoICMSService observacaoICMSService;
	@Mock DomainValueService domainValueService;
	@Mock TipoTributacaoIcmsService tipoTributacaoIcmsService;
	@Mock ConfiguracoesFacade configuracoesFacade;

	@BeforeClass
	private void before() {
		service = new CalcularIcmsService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void calcICMS90ValorCreditoShouldReturnZeroWhenValuesAreNull() {
		mockParameterService(PARAMETER_PC_CRED_PRESUMIDO_BA, PC_CRED_PRESUMIDO_BA);
		Conhecimento conhecimento = createConhecimento(null, null);
		
		BigDecimal value = service.calcICMSValorCredito(conhecimento);
		
		Assert.assertEquals(value, BigDecimal.ZERO);
	}
	
	@Test
	public void calcICMS90ValorCreditoShouldCalcParameterBaWhenImpostoIsZero() {
		mockParameterService(PARAMETER_PC_CRED_PRESUMIDO_BA, PC_CRED_PRESUMIDO_BA);
		Conhecimento conhecimento = createConhecimento(BigDecimal.TEN, BigDecimal.ZERO);
		
		BigDecimal value = service.calcICMSValorCredito(conhecimento);
		
		Assert.assertEquals(value.toString(), "2.50");
	}
	
	@Test
	public void calcICMS90ValorCreditoShouldReturnZeroWhenValuesAreZero() {
		mockParameterService(PARAMETER_PC_CRED_PRESUMIDO_BA, PC_CRED_PRESUMIDO_BA);
		Conhecimento conhecimento = createConhecimento(BigDecimal.ZERO, BigDecimal.ZERO); 
		
		BigDecimal value = service.calcICMSValorCredito(conhecimento);
		
		Assert.assertEquals(value.toString(), "0.00");
	}
	
	@Test
	public void calcICMS90ValorCreditoShouldWork() {
		mockParameterService(PARAMETER_PC_CRED_PRESUMIDO_BA, PC_CRED_PRESUMIDO_BA);
		Conhecimento conhecimento = createConhecimento(BigDecimal.ONE, BigDecimal.TEN); 
		
		BigDecimal value = service.calcICMSValorCredito(conhecimento);
		
		Assert.assertEquals(value, BigDecimal.valueOf(9));
	}
	
	@Test
	public void calcICMS90ValorCreditoShouldWorkWithBigNumbers() {
		mockParameterService(PARAMETER_PC_CRED_PRESUMIDO_BA, PC_CRED_PRESUMIDO_BA);
		BigDecimal icmsSubstituicaoTributaria = BIG_VALUE.subtract(BigDecimal.ONE);
		Conhecimento conhecimento = createConhecimento(icmsSubstituicaoTributaria, BIG_VALUE);
		
		BigDecimal value = service.calcICMSValorCredito(conhecimento);
		
		Assert.assertEquals(value, BigDecimal.ONE);
	}

	private Conhecimento createConhecimento(BigDecimal icmsSubstituicaoTributaria, BigDecimal imposto) {
		Conhecimento conhecimento = new Conhecimento(); 
		
		conhecimento.setVlIcmsSubstituicaoTributaria(icmsSubstituicaoTributaria);
		conhecimento.setVlImposto(imposto);
		
		return conhecimento;
	}
	
	@Test
	public void validateUtilizacaoCodigo90ValorCreditoShouldReturnTrueWhenUfInList() {
		mockParameterService(PARAMETER_PREENCHE_241, VALID_UFS);
		service.setUnidadeFederativaService(createUfServiceMock(ID_UNIDADE_FEDERATIVA, UF_MINAS_GERAIS));
		
		boolean value = service.validateUtilizacaoCodigo90ValorCredito(ID_UNIDADE_FEDERATIVA);
		
		Assert.assertTrue(value);
	}

	@Test
	public void validateUtilizacaoCodigo90ValorCreditoShouldReturnFalseWhenUfOutOfList() {
		mockParameterService(PARAMETER_PREENCHE_241, VALID_UFS);
		service.setUnidadeFederativaService(createUfServiceMock(ID_UNIDADE_FEDERATIVA, UF_PORTO_ALEGRE));
		
		boolean value = service.validateUtilizacaoCodigo90ValorCredito(ID_UNIDADE_FEDERATIVA);
		
		Assert.assertFalse(value);
	}
	
	@Test
	public void validateUtilizacaoCodigo90ShouldReturnFalseWhenParameterIsNo() {
		service.setConfiguracoesFacade(new ConfiguracoesFacadeMocker().getValorParametro(PARAMETER_UTILIZA_CODIGO_90_ST, "N").mock());
		
		boolean value = service.validateUtilizacaoCodigo90ST(ID_UNIDADE_FEDERATIVA);
		
		Assert.assertFalse(value);
	}
	
	@Test
	public void validateUtilizacaoCodigo90ShouldReturnFalseWhenUfIsOutOfList() {
		service.setConfiguracoesFacade(createConfiguracoesFacadeMock());
		service.setUnidadeFederativaService(createUfServiceMock(ID_UNIDADE_FEDERATIVA, UF_PORTO_ALEGRE));
		
		boolean value = service.validateUtilizacaoCodigo90ST(ID_UNIDADE_FEDERATIVA);
		
		Assert.assertFalse(value);
	}
	
	@Test
	public void validateUtilizacaoCodigo90ShouldReturnTrueWhenUfIsOnList() {
		service.setConfiguracoesFacade(createConfiguracoesFacadeMock());
		service.setUnidadeFederativaService(createUfServiceMock(ID_UNIDADE_FEDERATIVA, UF_MINAS_GERAIS));
		
		boolean value = service.validateUtilizacaoCodigo90ST(ID_UNIDADE_FEDERATIVA);
		
		Assert.assertTrue(value);
	}
	
	@Test
	public void isExportacaoShouldReturnTrueWhenCountriesAreDifferents() {
		CalcularIcmsParam param = createCalcICMSParam(ID_BRASIL, ID_URUGUAI);
		boolean isExportacao = service.isExportacao(param);
		
		Assert.assertTrue(isExportacao);
	}
	
	@Test
	public void isExportacaoShouldReturnFalseWhenCountriesAreEqual() {
		CalcularIcmsParam param = createCalcICMSParam(ID_BRASIL, ID_BRASIL);
		boolean isExportacao = service.isExportacao(param);
		
		Assert.assertFalse(isExportacao);
	}

	@Test
	public void findExcecaoICMSemExcecoesShouldReturnExcecaoWhenTpFreteISCIF(){
		
		String parametoGeralSituacaoGeral = "CN;MN;NC;ON;PN";
		
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = getExcecoesICMSCIF();
		
		CalcularIcmsParam cip = Mockito.mock(CalcularIcmsParam.class);
		
		TipoTributacaoIE tipoTributacao = mockTipoTributacao(cip);
		
		mockSituacaoTributaria(tipoTributacao, "ME");
		
		ExcecaoICMSIntegrantesContribuintes excecaoIsentaResult = service.findExcecaoICMSemExcecoes(cip, excecoes, parametoGeralSituacaoGeral);

		Assert.assertEquals(excecaoIsentaResult, excecoes.get(0));
	}

	@Test
	public void findExcecaoICMSemExcecoesShouldReturnExcecaoWhenTpFreteIsFOB(){
		
		String parametoGeralSituacaoGeral = "CN;MN;NC;ON;PN";
		
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = getExcecoesICMSFOB();
		
		CalcularIcmsParam cip = Mockito.mock(CalcularIcmsParam.class);
		
		TipoTributacaoIE tipoTributacao = mockTipoTributacao(cip);
		
		mockSituacaoTributaria(tipoTributacao, "ME");
		
		ExcecaoICMSIntegrantesContribuintes excecaoIsentaResult = service.findExcecaoICMSemExcecoes(cip, excecoes, parametoGeralSituacaoGeral);

		Assert.assertEquals(excecaoIsentaResult, excecoes.get(0));
	}

	@Test
	public void findExcecaoICMSemExcecoesShouldReturnExcecaoWhenTpfreteISNull(){
		
		String parametoGeralSituacaoGeral = "CN;MN;NC;ON;PN";
		
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = getExcecoesICMSNull();
		
		CalcularIcmsParam cip = Mockito.mock(CalcularIcmsParam.class);
		
		TipoTributacaoIE tipoTributacao = mockTipoTributacao(cip);
		
		mockSituacaoTributaria(tipoTributacao, "ME");
		
		ExcecaoICMSIntegrantesContribuintes excecaoIsentaResult = service.findExcecaoICMSemExcecoes(cip, excecoes, parametoGeralSituacaoGeral);

		Assert.assertEquals(excecaoIsentaResult, excecoes.get(0));
	}

	@Test
	public void findExcecaoICMSemExcecoesShouldReturnNullWhenTpFreteIsEmpty(){
		
		String parametoGeralSituacaoGeral = "CN;MN;NC;ON;PN";
		
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = null;
		
		CalcularIcmsParam cip = Mockito.mock(CalcularIcmsParam.class);
		
		TipoTributacaoIE tipoTributacao = mockTipoTributacao(cip);
		
		mockSituacaoTributaria(tipoTributacao, "ME");
		
		ExcecaoICMSIntegrantesContribuintes excecaoIsentaResult = service.findExcecaoICMSemExcecoes(cip, excecoes, parametoGeralSituacaoGeral);

		Assert.assertEquals(excecaoIsentaResult, null);
	}
	
	
	@Test
	public void checkParametroGeralContainsSituacaoTributariaIntegrante(){
		Assert.assertTrue(service.checkParametroGeralContainsSituacaoTributariaIntegrante("NC", "NC"));
	}
	
	@Test
	public void checkParametroGeralContainsSituacaoTributariaIntegranteWithComa(){
		Assert.assertTrue(service.checkParametroGeralContainsSituacaoTributariaIntegrante("NC","NC;"));
	}
	
	@Test
	public void checkParametroGeralNotContainsSituacaoTributariaIntegrante(){
		Assert.assertFalse(service.checkParametroGeralContainsSituacaoTributariaIntegrante( "NC","NCS;"));
	}
	@Test
	public void checkParametroGeralNotContainsSituacaoTributariaIntegranteWithList(){
		Assert.assertFalse(service.checkParametroGeralContainsSituacaoTributariaIntegrante( "NC","NCS;BM;PA"));
	}
	
	@Test
	public void checkParametroGeralContainsSituacaoTributariaIntegranteWithMoreThanOne(){
		Assert.assertTrue(service.checkParametroGeralContainsSituacaoTributariaIntegrante("NC","NC;BM"));
	}
	@Test
	public void checkParametroGeralContainsSituacaoTributariaIntegranteWithMoreSecondTrue(){
		Assert.assertTrue(service.checkParametroGeralContainsSituacaoTributariaIntegrante("NC","BM;NC"));
	}
	@Test
	public void checkParametroGeralContainsSituacaoTributariaIntegranteWithoutParametroGeral(){
		Assert.assertFalse(service.checkParametroGeralContainsSituacaoTributariaIntegrante("NC",""));
	}
	
	@Test
	public void checkParametroGeralContainsSituacaoTributariaIntegranteWithParametroGeralEqualsNull(){
		Assert.assertFalse(service.checkParametroGeralContainsSituacaoTributariaIntegrante("NC",null));
	}
	
	private void mockSituacaoTributaria(TipoTributacaoIE tipoTributacao, String value) {
		DomainValue tpSituacaoTributaria = new DomainValue();
		tpSituacaoTributaria.setValue(value);
		when(tipoTributacao.getTpSituacaoTributaria()).thenReturn(tpSituacaoTributaria );
	}

	private TipoTributacaoIE mockTipoTributacao(CalcularIcmsParam cip) {
		TipoTributacaoIE tipoTributacao = Mockito.mock(TipoTributacaoIE.class);
		when(cip.getTipoTributacaoIERemetente()).thenReturn(tipoTributacao );
		return tipoTributacao;
	}

	List<ExcecaoICMSIntegrantesContribuintes> getExcecoesICMSCIF(){
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = new ArrayList<ExcecaoICMSIntegrantesContribuintes>();
		ExcecaoICMSIntegrantesContribuintes excecaoIsenta = new ExcecaoICMSIntegrantesContribuintes();
		
		DomainValue tpFrete = new DomainValue();
		tpFrete.setValue(ConstantesExpedicao.TP_FRETE_CIF);
		excecaoIsenta.setTpFrete(tpFrete );
		DomainValue tpIntegranteFrete = new DomainValue();
		tpIntegranteFrete.setValue("R");
		excecaoIsenta.setTpIntegranteFrete(tpIntegranteFrete );
		excecoes.add(excecaoIsenta);
		
		return excecoes; 
	}
	
	List<ExcecaoICMSIntegrantesContribuintes> getExcecoesICMSFOB(){
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = new ArrayList<ExcecaoICMSIntegrantesContribuintes>();
		ExcecaoICMSIntegrantesContribuintes excecaoIsenta = new ExcecaoICMSIntegrantesContribuintes();
		
		DomainValue tpFrete = new DomainValue();
		tpFrete.setValue(ConstantesExpedicao.TP_FRETE_FOB);
		excecaoIsenta.setTpFrete(tpFrete );
		DomainValue tpIntegranteFrete = new DomainValue();
		tpIntegranteFrete.setValue("R");
		excecaoIsenta.setTpIntegranteFrete(tpIntegranteFrete );
		excecoes.add(excecaoIsenta);
		
		return excecoes; 
	}

	
	List<ExcecaoICMSIntegrantesContribuintes> getExcecoesICMSNull(){
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = new ArrayList<ExcecaoICMSIntegrantesContribuintes>();
		ExcecaoICMSIntegrantesContribuintes excecaoIsenta = new ExcecaoICMSIntegrantesContribuintes();
		
		DomainValue tpIntegranteFrete = new DomainValue();
		tpIntegranteFrete.setValue("R");
		excecaoIsenta.setTpIntegranteFrete(tpIntegranteFrete );
		excecoes.add(excecaoIsenta);
		
		return excecoes; 
	}
	
	
	private CalculoFrete createCalcFrete() {
		CalculoFrete calculoFrete = new  CalculoFrete();
		calculoFrete.setDoctoServico(createConhecimento(ID_BRASIL));
		
		DoctoServicoDadosCliente dadosCliente = new DoctoServicoDadosCliente();
		dadosCliente.setIdClienteDestinatario(ID_PESSOA);
		
		calculoFrete.setDadosCliente(dadosCliente);
		return calculoFrete;
	}

	private CalcularIcmsParam createCalcICMSParam(Long idPaisOrigem, Long idPaisDestino) {
		CalculoFrete calculoFrete = createCalcFrete();
		
		return new CalcularIcmsParam(
				calculoFrete,
				new UnidadeFederativaServiceMocker().findById(ID_UNIDADE_FEDERATIVA, idPaisOrigem).mock(),
				new ClienteServiceMocker().findByIdComPessoa(ID_PESSOA, createCliente(idPaisDestino)) .mock(),
				inscricaoEstadualService,
				tipoTributacaoIEService,
				observacaoICMSPessoaService,
				observacaoICMSService,
				domainValueService,
				tipoTributacaoIcmsService,
				configuracoesFacade
		);
	}

	private Conhecimento createConhecimento(Long idPais) {
		Conhecimento conhecimento = new Conhecimento();
		Filial filialByIdFilialOrigem = new Filial();
		
		conhecimento.setFilialByIdFilialOrigem(filialByIdFilialOrigem);
		filialByIdFilialOrigem.setPessoa(createPessoaWithContry(idPais));
		
		return conhecimento;
	}
	
	private Cliente createCliente(Long idPais) {
		Cliente cliente = new Cliente();
		
		cliente.setPessoa(createPessoaWithContry(idPais));
		
		return cliente;
	}

	private Pessoa createPessoaWithContry(Long idPais) {
		Pessoa pessoa = new Pessoa();
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		Municipio municipio = new Municipio();
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		Pais pais = new Pais();
		
		unidadeFederativa.setIdUnidadeFederativa(ID_UNIDADE_FEDERATIVA);
		pais.setIdPais(idPais);
		unidadeFederativa.setPais(pais);
		municipio.setUnidadeFederativa(unidadeFederativa);
		enderecoPessoa.setMunicipio(municipio);
		pessoa.setEnderecoPessoa(enderecoPessoa);
		
		return pessoa;
	}

	private ConfiguracoesFacade createConfiguracoesFacadeMock() {
		return new ConfiguracoesFacadeMocker()
			.getValorParametro(PARAMETER_UTILIZA_CODIGO_90_ST, "S")
			.getValorParametro(PARAMETER_UFS_90, VALID_UFS)
			.mock();
	}

	private UnidadeFederativaService createUfServiceMock(Long idUf, String uf) {
		return new UnidadeFederativaServiceMocker()
			.findById(idUf, uf)
			.mock();
	}
	
	private void mockParameterService(String parameterName, String validUfs) {
		when(parametroService.findSimpleConteudoByNomeParametro(parameterName)).thenReturn(validUfs);
		service.setParametroGeralService(parametroService);
	}
	
}
