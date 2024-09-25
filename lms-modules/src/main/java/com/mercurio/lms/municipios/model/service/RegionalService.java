package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.RHFuncao;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.RHFuncaoService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.dao.RegionalDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.regionalService"
 */
public class RegionalService extends CrudService<Regional, Long> {
	private RegionalFilialService regionalFilialService;
	private RHFuncaoService funcaoService;
	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;

	//busca todas as regionais com data de vigencia vigente
	public List findRegionaisVigentesByEmpresa( TypedFlatMap criteria ) {
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		List<Map<String, Object>> list = null;
		final Long idEmpresa = criteria.getLong("idEmpresa");
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			Empresa empresa = new Empresa();
			empresa.setIdEmpresa( idEmpresa );
			list = findRegionaisByUsuarioEmpresa( usuarioSessao, empresa );
		} else {
			if( idEmpresa != null ){
				list = getRegionalDAO().findRegionaisVigentesByEmpresa(idEmpresa );
			}
		}
		List<Map<String, Object>> lista = null;
		if(list != null) {
			lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
			for(Map<String, Object> map : lista) {
				String siglaDescricao = (String)map.get("sgRegional")+ " - " + map.get("dsRegional");
				map.put("siglaDescricao",siglaDescricao);
			}
		}
		return lista;
	}

	//busca todas as regionais com data de vigencia vigente
	public List<Map<String, Object>> findRegionaisVigentes( ) {
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		List<Map<String, Object>> list = null;
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			list = findRegionaisByUsuario( usuarioSessao );
		} else {
			list = getRegionalDAO().findRegionaisVigentes( );
		}
		List<Map<String, Object>> lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		for(Map<String, Object> map : lista) {
			String siglaDescricao =(String)map.get("sgRegional")+ " - " + map.get("dsRegional");
			map.put("siglaDescricao",siglaDescricao);
			if(map.get("dtVigenciaInicial")!= null)
				map.put("dtVigenciaInicial",JTFormatUtils.format((YearMonthDay)map.get("dtVigenciaInicial")));
			if(map.get("dtVigenciaFinal")!= null)
				map.put("dtVigenciaFinal",JTFormatUtils.format((YearMonthDay)map.get("dtVigenciaFinal")));
		}
		return lista;
	}

	/**
	 * Busca as regionais que o usuário logado possui acesso
	 * @return Lista com as regionais que o usuário logado possui acesso.
	 */
	public List<Regional> findRegionaisVigentesByIdUsuario(Long idUsuario) {		
		return getRegionalDAO().findRegionaisVigentesByIdUsuario(idUsuario);				
	}
	
	public List<Map<String, Object>> findRegionaisByUsuario(Usuario usuario) {		
		return getRegionalDAO().findRegionaisByUsuario(usuario);
	}

	public List<Regional> findRegionalByIdRegional(Long idRegional, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getRegionalDAO().findRegionalByIdRegional(idRegional,dtVigenciaInicial,dtVigenciaFinal);
	}

	/**
	 * Recupera uma instância de <code>Regional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Map<String, Object> findByIdEValidaVigencia(java.lang.Long id) {
		Regional regionalBusca = (Regional) super.findByIdInitLazyProperties(id, true);
		Map<String, Object> regionalMap = new HashMap<String, Object>();
		if ( regionalBusca.getUsuarioFaturamento() != null ){
			regionalMap.put("idFaturamento",regionalBusca.getUsuarioFaturamento().getIdUsuario());
			regionalMap.put("nomeFaturamento",regionalBusca.getUsuarioFaturamento().getNmUsuario());
		}
		ReflectionUtils.copyNestedBean(regionalMap, regionalBusca);
		Map<String, Object> usuario = (Map<String, Object>) regionalMap.get("usuario");
		Map<String, Object> func = (Map<String, Object>) usuario.remove("funcionario");
		regionalMap.put("funcionario", func);
		
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(regionalBusca);

		regionalMap.put("acaoVigenciaAtual", acaoVigencia);
		return regionalMap;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public List findLookup(Map criteria) {
		return super.findLookup(criteria);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Regional bean) {
		vigenciaService.validaVigenciaBeforeStore(bean);
		return super.store(bean);
	}

	/**
	 * Método que consulta os dados do codigo e funcao do CD_GERENTE_REGIONAL
	 * 
	 * @param keyParameter
	 * @return
	 */
	public Map<String, Object> findParameterFuncionario(String keyParameter) {
		String codigo =(String)configuracoesFacade.getValorParametro(keyParameter);
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("codigo",codigo);
		List<RHFuncao> rs = funcaoService.find(criteria);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cd_funcao",codigo); 
		if (rs.size() > 0) {
			RHFuncao funcao = rs.get(0);
			result.put("ds_funcao", funcao.getNome());
		}
		return result;
	}

	/**
	 * Andresa Vargas
	 * 
	 * @param sgRegional
	 * @param dsRegional
	 * @param idUsuario
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 * @see lms.com.mercurio.lms.municipios.model.dao.RegionalDAO.findPaginatedCustom
	 */
	public ResultSetPage findPaginatedCustom(String sgRegional, String dsRegional, Long idUsuario, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, FindDefinition findDef) {
		return this.getRegionalDAO().findPaginatedCustom(sgRegional, dsRegional, idUsuario, dtVigenciaInicial, dtVigenciaFinal, findDef);
	}

	/**
	 * Andresa Vargas
	 * 
	 * 
	 * @param sgRegional
	 * @param dsRegional
	 * @param idUsuario
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @param findDef
	 * @return
	 * @see lms.com.mercurio.lms.municipios.model.dao.RegionalDAO.getRowCountCustom
	 */
	public Integer getRowCountCustom(String sgRegional, String dsRegional, Long idUsuario, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return this.getRegionalDAO().getRowCountCustom(sgRegional, dsRegional, idUsuario, dtVigenciaInicial, dtVigenciaFinal);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		Regional regional = (Regional)getRegionalDAO().getAdsmHibernateTemplate().load(Regional.class,id);
		if(regional != null)
			JTVigenciaUtils.validaVigenciaRemocao(regional);
		super.beforeRemoveById(id);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		for(Long id : ids) {
			Regional regional = (Regional)getRegionalDAO().getAdsmHibernateTemplate().load(Regional.class, id);
			if(regional != null)
				JTVigenciaUtils.validaVigenciaRemocao(regional);
		}
		super.beforeRemoveByIds(ids);
	}

	@Override
	protected Regional beforeStore(Regional bean) {
		if (existeFilialVigenteDeMesmaSigla(bean))
			throw new BusinessException("LMS-00003");

		if(bean.getDtVigenciaInicial() != null && regionalFilialService.findFilhosVigentesByVigenciaPai(bean.getIdRegional(),bean.getDtVigenciaInicial(),null))
			throw new BusinessException("LMS-29110");

		if(bean.getDtVigenciaFinal() != null && regionalFilialService.findFilhosVigentesByVigenciaPai(bean.getIdRegional(),null,bean.getDtVigenciaFinal()))
			throw new BusinessException("LMS-29106");

		return super.beforeStore(bean);
	}

	public List<Map<String, Object>> findRegional() {
		List<Map<String, Object>> list = getRegionalDAO().findRegionais();
		List<Map<String, Object>> lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		for(Map<String, Object> map : lista) {
			String siglaDescricao = (String)map.get("sgRegional")+ " - " + map.get("dsRegional");
			map.put("siglaDescricao", siglaDescricao);
		}
		return lista;
	}

	/**
	 * Verifica se já existe filial vigente com a mesma sigla.
	 * Utilliza comparação de sigla com ignoreCase (ilike).
	 * @param regional
	 * @return True em caso afirmativo.
	 * @author luisfco
	 */
	public boolean existeFilialVigenteDeMesmaSigla(Regional regional) {
		return getRegionalDAO().existeFilialVigenteDeMesmaSigla(regional);
	}

	public Regional findById(Long id) {
		return (Regional)super.findById(id);
	}

	public List<Map<String, Object>> findRegionaisByUsuarioEmpresa(Usuario usuario, Empresa empresa ) {		
		return getRegionalDAO().findRegionaisByUsuarioEmpresa(usuario, empresa );
	}

	public Object findRegionalPadraoByUsuario(Usuario usuario) {
		return getRegionalDAO().findRegionalPadraoByUsuario(usuario);
	}

	public List<Regional> findByUsuarioLogado(TypedFlatMap m) {
		List<Regional> listReg = SessionUtils.getRegionaisFiliaisUsuarioLogado();

		Set<Regional> set = new TreeSet<Regional>(new Comparator<Regional>() {
			public int compare(Regional r1, Regional r2) {
				if (r1 != null && r2 != null) {
					return r1.getSgRegional().compareTo( r2.getSgRegional() );
				}
				return -1;
			}
		});

		set.addAll(listReg);		
		return new ArrayList<Regional>(set);
	}

	public List<Regional> findRegionaisByFiliais(List<Long> filiais) {
		return this.getRegionalDAO().findRegionaisByFiliais(filiais);
	}

	/**
	 * Método que busca as regionais que tenham iniciado sua vigência inicial 
	 * na dataInicial e que sua vigencia final vá até a dataFinal informada
	 * ou que esteje vigente. 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return list
	 */
	public List<Regional> findRegionaisVigentesPorPeriodo(YearMonthDay dataInicial, YearMonthDay dataFinal) {
		return getRegionalDAO().findRegionaisVigentesPorPeriodo(dataInicial, dataFinal);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setRegionalDAO(RegionalDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private RegionalDAO getRegionalDAO() {
		return (RegionalDAO) getDao();
	}
	public void setFuncaoService(RHFuncaoService funcaoService) {
		this.funcaoService = funcaoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

	public List<Regional> findRegionaisValidas() {
		return getRegionalDAO().findRegionaisValidas();
	}

	public List<Regional> findChosen() {
		return getRegionalDAO().findChosen();
	}
	
	public Regional findRegionalAtivaByIdFilial(Long idFilial) {
		return this.getRegionalDAO().findRegionalAtivaByIdFilial(idFilial);
	}

	public Map<String, String> findContatoRegionalByIdRegionalIdMonitoramentoMensagem(Long idRegional, Long idMonitMens) {
		return getRegionalDAO().findContatoRegionalByIdRegionalIdMonitoramentoMensagem(idRegional, idMonitMens);
		
	}

}