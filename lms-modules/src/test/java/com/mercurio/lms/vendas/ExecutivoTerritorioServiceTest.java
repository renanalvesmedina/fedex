package com.mercurio.lms.vendas;

import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.EXCEPTION_STORE_CONFLITO_VIGENCIAS;
import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.EXCEPTION_STORE_SEM_CARGO;
import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.EXCEPTION_STORE_SEM_TERRITORIO;
import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.EXCEPTION_STORE_SEM_USUARIO;
import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.EXCEPTION_STORE_SEM_VIGENCIA_INICIAL;
import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.EXCEPTION_STORE_VIGENCIA_FINAL_ANTERIOR_VIGENCIA_INICIAL;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.dao.ExecutivoTerritorioDAO;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;

@PrepareForTest({SessionContext.class, SessionUtils.class, JTDateTimeUtils.class, ExecutivoTerritorioDAO.class, ExecutivoTerritorioService.class}) 
public class ExecutivoTerritorioServiceTest extends PowerMockTestCase {

	private static final YearMonthDay HOJE =  new YearMonthDay();
	private static final YearMonthDay ANTEONTEM = HOJE.minusDays(2);
	private static final YearMonthDay ONTEM = HOJE.minusDays(1);
	private static final YearMonthDay AMANHA = HOJE.plusDays(1);
	
	private static DomainValue CARGO_1 = new DomainValue("c1");
	private static DomainValue CARGO_2 = new DomainValue("c2");
	private static UsuarioLMS USUARIO_1 = buildUsuario(1l);
	private static UsuarioLMS USUARIO_2 = buildUsuario(2l);
	private static Territorio TERRITORIO_1 = buildTerritorio1();
	
	private ExecutivoTerritorioDAO executivoTerritorioDAO;
	private ExecutivoTerritorioService executivoTerritorioService;
	private FakeDAO fakeDAO;
	
	@ObjectFactory
	public IObjectFactory setObjectFactory() {
		return new PowerMockObjectFactory();
	}

	@BeforeMethod
	public void setup() throws Exception {
		PowerMockito.mockStatic(SessionContext.class);
		PowerMockito.mockStatic(SessionUtils.class);
		PowerMockito.mockStatic(JTDateTimeUtils.class);

		initMocks(this);

		PowerMockito.when(JTDateTimeUtils.getDataHoraAtual()).thenReturn(new DateTime());
		PowerMockito.when(JTDateTimeUtils.getDataAtual()).thenReturn(new YearMonthDay());
		
		executivoTerritorioService = PowerMockito.spy(new ExecutivoTerritorioService());
		
		fakeDAO = new FakeDAO();
		executivoTerritorioDAO = PowerMockito.spy(new ExecutivoTerritorioDAO());
		executivoTerritorioService.setExecutivoDao(executivoTerritorioDAO);
		
		overwriteFind();
		overwriteFindById();
		overwriteFindComVigenciaAtiva();
		overwriteCallInheritedStore();
		
	}
	
	@Test
	public void givenNenhumFuncionarioNoTerritorio1_whenCadastrarFuncionario1ComCargo1NoTerritorio1_thenSucesso() throws Exception {
		ExecutivoTerritorio etCargo1Funcionario1 = buildExecutivoTerritorio(CARGO_1, USUARIO_1, HOJE, AMANHA);
		executivoTerritorioService.store(etCargo1Funcionario1);
	}
	
	@Test
	public void givenNenhumFuncionarioNoTerritorio1_whenCadastrarFuncionario1ComCargo1SemVigenciaFinalNoTerritorio1_thenSucesso() throws Exception {
		ExecutivoTerritorio etCargo1Funcionario1 = buildExecutivoTerritorio(CARGO_1, USUARIO_1, HOJE, null);
		executivoTerritorioService.store(etCargo1Funcionario1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_CARGO)
	public void givenNenhumFuncionarioNoTerritorio1_whenCadastrarFuncionario1SemCargoNoTerritorio1_thenExcecao() throws Exception {
		ExecutivoTerritorio etFuncionario1 = buildExecutivoTerritorio(null, USUARIO_1, HOJE, AMANHA);
		executivoTerritorioService.store(etFuncionario1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_USUARIO)
	public void givenNenhumFuncionarioNoTerritorio1_whenCadastrarExecutivoSemFuncionarioNoTerritorio1_thenExcecao() throws Exception {
		ExecutivoTerritorio etCargo1 = buildExecutivoTerritorio(CARGO_1, null, HOJE, AMANHA);
		executivoTerritorioService.store(etCargo1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_VIGENCIA_FINAL_ANTERIOR_VIGENCIA_INICIAL)
	public void givenNenhumFuncionarioNoTerritorio1_whenCadastrarFuncionario1ComCargo1ComVigenciaFinalAnteriorAVigenciaInicialNoTerritorio1_thenExcecao() throws Exception {
		ExecutivoTerritorio etCargo1Funcionario1 = buildExecutivoTerritorio(CARGO_1, USUARIO_1, AMANHA, HOJE);
		executivoTerritorioService.store(etCargo1Funcionario1);
	}
	
	@Test(expectedExceptions = BusinessException.class, expectedExceptionsMessageRegExp = ".*" + EXCEPTION_STORE_SEM_TERRITORIO)
	public void givenNenhumExecutivoTerritorio_whenCadastrarFuncionario1ComCargo1SEMTERRITORIO_thenExcecao() throws Exception {
		ExecutivoTerritorio etCargo1Funcionario1 = buildExecutivoTerritorio(CARGO_1, USUARIO_1, HOJE, AMANHA);
		etCargo1Funcionario1.setTerritorio(null);
		executivoTerritorioService.store(etCargo1Funcionario1);
	}

	
	@Test
	public void givenUmFuncionarioComCargo1NoTerritorio1_whenCadastrarOutroFuncionarioComCargo1NoTerritorio1SemConflitoEntreVigencias_thenSucesso() throws Exception {
		/* condi��o inicial */
		ExecutivoTerritorio etCargo1Funcionario1 = buildExecutivoTerritorio(CARGO_1, USUARIO_1, ANTEONTEM, ONTEM); 
		executivoTerritorioService.store(etCargo1Funcionario1);
		
		/* teste a ser feito */
		ExecutivoTerritorio etCargo1Funcionario2 = buildExecutivoTerritorio(CARGO_1, USUARIO_2, HOJE, AMANHA);
		executivoTerritorioService.store(etCargo1Funcionario2);
	}
	
	@Test
	public void givenFuncionario1ComCargo1EFuncionario2ComCargo2_whenAlterarFuncionario2ParaCargo1SemVigenciaConflitante_thenExcecao_thenSucesso() throws Exception {
		/* condi��o inicial */
		ExecutivoTerritorio etCargo1Funcionario1 = buildExecutivoTerritorio(CARGO_1, USUARIO_1, ANTEONTEM, ONTEM);
		executivoTerritorioService.store(etCargo1Funcionario1);
		ExecutivoTerritorio etCargo1Funcionario2 = buildExecutivoTerritorio(CARGO_2, USUARIO_2, HOJE, AMANHA);
		executivoTerritorioService.store(etCargo1Funcionario2);
		
		/* teste a ser feito */
		etCargo1Funcionario2.setTpExecutivo(CARGO_1);
		executivoTerritorioService.store(etCargo1Funcionario2);
	}
	
	private void overwriteFind() {
		doAnswer(new Answer<List<ExecutivoTerritorio>>() {
			@Override
			public List<ExecutivoTerritorio> answer(InvocationOnMock invocation) throws Throwable {
				return fakeDAO.daofind((ExecutivoTerritorio) invocation.getArguments()[0]);
			}
		}).when(executivoTerritorioDAO).find(any(ExecutivoTerritorio.class));
	}
	
	private void overwriteFindComVigenciaAtiva() {
		doAnswer(new Answer<List<ExecutivoTerritorio>>() {
			@Override
			public List<ExecutivoTerritorio> answer(InvocationOnMock invocation) throws Throwable {
				return fakeDAO.daofindComVigenciaAtiva((Long) invocation.getArguments()[0], (Long) invocation.getArguments()[1], (DomainValue) invocation.getArguments()[2]);
			}
		}).when(executivoTerritorioDAO).findComVigenciaAtiva(anyLong(), anyLong(), any(DomainValue.class));
	}
	
	private void overwriteFindById() {
		doAnswer(new Answer<ExecutivoTerritorio>() {
			@Override
			public ExecutivoTerritorio answer(InvocationOnMock invocation) throws Throwable {
				return fakeDAO.daofindById((Long) invocation.getArguments()[0]);
			}
		}).when(executivoTerritorioDAO).findById(anyLong());
	}
	
	private void overwriteCallInheritedStore() throws Exception {
		PowerMockito.doAnswer(new Answer<java.io.Serializable>() {
			@Override
			public Serializable answer(InvocationOnMock invocation) throws Throwable {
				return fakeDAO.getNextExecutivoTerritorioId();
			}
		}).when(executivoTerritorioDAO).getIdentifier(any(ExecutivoTerritorio.class));
		
		PowerMockito.doAnswer(new Answer<java.io.Serializable>() {
			@Override
			public Serializable answer(InvocationOnMock invocation) throws Throwable {
				return fakeDAO.daoStoreFake((ExecutivoTerritorio) invocation.getArguments()[0]);
			}
		}).when(executivoTerritorioDAO, "store", any(Object.class));
	}
	
	private static UsuarioLMS buildUsuario(Long idUsuario) {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(idUsuario);
		
		Funcionario funcionario = new Funcionario();
		funcionario.setUsuario(usuario);
		funcionario.setNmFuncionario("Funcion�rio " + idUsuario);
		
		UsuarioADSM usuarioADSM = new UsuarioADSM();
		usuarioADSM.setIdUsuario(idUsuario);
		usuarioADSM.setFuncionario(funcionario);
		
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(idUsuario);
		usuarioLMS.setUsuarioADSM(usuarioADSM);
		
		return usuarioLMS;
	}
	
	private ExecutivoTerritorio buildExecutivoTerritorio(DomainValue tpExecutivo, UsuarioLMS usuario, YearMonthDay inicioVigencia, YearMonthDay fimVigencia) {
		ExecutivoTerritorio executivoTerritorio = new ExecutivoTerritorio();
		
		executivoTerritorio.setTerritorio(TERRITORIO_1);
		executivoTerritorio.setTpExecutivo(tpExecutivo);
		executivoTerritorio.setUsuario(usuario);
		executivoTerritorio.setDtVigenciaInicial(inicioVigencia);
		executivoTerritorio.setDtVigenciaFinal(fimVigencia);
		
		return executivoTerritorio;
	}
	
	private static Territorio buildTerritorio1() {
		Territorio territorio1Completo = new Territorio();
		territorio1Completo.setRegional(buildRegional1());
		territorio1Completo.setNmTerritorio("Territ�rio 1".toUpperCase());
		return territorio1Completo;
	}
	
	private static Regional buildRegional1() {
		Regional regional1 = new Regional();
		regional1.setIdRegional(1l);
		regional1.setDsRegional("Regional 1");
		return regional1;
	}
	
	private class FakeDAO {
		private ThreadLocal<Map<Long, ExecutivoTerritorio>> fakeRepository;
		
		private FakeDAO() {
			fakeRepository = new ThreadLocal<Map<Long, ExecutivoTerritorio>>();
			fakeRepository.set(new HashMap<Long, ExecutivoTerritorio>());
		}
		
		private ExecutivoTerritorio daofindById(Long idExecutivoTerritorio) {
			Map<Long, ExecutivoTerritorio> repo = fakeRepository.get();
			return repo.get(idExecutivoTerritorio); 
		}
		
		private List<ExecutivoTerritorio> daofindComVigenciaAtiva(Long idUsuario, Long idTerritorio, DomainValue tpExecutivo) {
			List<ExecutivoTerritorio> repo = new ArrayList(fakeRepository.get().values());
			List<ExecutivoTerritorio> result = new ArrayList<ExecutivoTerritorio>();
			for (ExecutivoTerritorio executivoTerritorio : repo) {
				if (!HOJE.isBefore(executivoTerritorio.getDtVigenciaInicial()) && !HOJE.isAfter(executivoTerritorio.getDtVigenciaFinal())) {//ativo
					if ((idUsuario == null || idUsuario.equals(executivoTerritorio.getUsuario().getIdUsuario()))
							&& (idTerritorio == null || idTerritorio.equals(executivoTerritorio.getTerritorio().getIdTerritorio()))
							&& (tpExecutivo == null || tpExecutivo.equals(executivoTerritorio.getTpExecutivo()))) {//criteria
						result.add(executivoTerritorio);
					}
				}
			}
			return result;
		}
		
		private List<ExecutivoTerritorio> daofind(ExecutivoTerritorio et) {
			List<ExecutivoTerritorio> repo = new ArrayList(fakeRepository.get().values());
			List<ExecutivoTerritorio> result = new ArrayList<ExecutivoTerritorio>();
			
			for (ExecutivoTerritorio executivoTerritorio : repo) {
				Long idExecutivoTerritorio = et.getIdExecutivoTerritorio();
				YearMonthDay dtVigenciaInicial = et.getDtVigenciaInicial();
				YearMonthDay dtVigenciaFinal = et.getDtVigenciaFinal();
				DomainValue tpExecutivo = et.getTpExecutivo();
				Long idTerritorio = et.getTerritorio() != null ? et.getTerritorio().getIdTerritorio() : null;;
				Long idUsuario = et.getUsuario() != null ? et.getUsuario().getIdUsuario() : null;
				
				if ((idExecutivoTerritorio == null || idExecutivoTerritorio.equals(executivoTerritorio.getIdExecutivoTerritorio()))
						&& (dtVigenciaInicial == null || dtVigenciaInicial.equals(executivoTerritorio.getDtVigenciaInicial()))
						&& (dtVigenciaFinal == null || dtVigenciaFinal.equals(executivoTerritorio.getDtVigenciaFinal()))
						&& (tpExecutivo == null || tpExecutivo.equals(executivoTerritorio.getTpExecutivo()))
						&& (idTerritorio == null || idTerritorio.equals(executivoTerritorio.getTerritorio().getIdTerritorio()))
						&& (idUsuario == null || idUsuario.equals(executivoTerritorio.getUsuario().getIdUsuario()))) {
					result.add(executivoTerritorio);
				}
			}
			return result;
		}
		
		private java.io.Serializable getNextExecutivoTerritorioId() {
			return (long) fakeRepository.get().size() + 1;
		}
		
		private java.io.Serializable daoStoreFake(ExecutivoTerritorio executivoTerritorio) {
			Map<Long, ExecutivoTerritorio> repo = fakeRepository.get();
			Long idExecutivoTerritorio = executivoTerritorio.getIdExecutivoTerritorio();
			
			if (idExecutivoTerritorio == null) {
				idExecutivoTerritorio = (Long) getNextExecutivoTerritorioId();
				executivoTerritorio.setIdExecutivoTerritorio(idExecutivoTerritorio);
			} else {
				if (!repo.containsKey(idExecutivoTerritorio)) {
					throw new RuntimeException("Registro removido");
				}
			}
			
			repo.remove(idExecutivoTerritorio);
			repo.put(idExecutivoTerritorio, executivoTerritorio);
			return idExecutivoTerritorio;
		}
	}
}
