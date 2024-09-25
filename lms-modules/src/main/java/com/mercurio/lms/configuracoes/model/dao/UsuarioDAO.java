package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.FilialUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.dto.ContatoDto;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Substituto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class UsuarioDAO extends BaseCrudDao<Usuario, Long> {

	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("vfuncionario",FetchMode.JOIN);
	}
 
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("vfuncionario",FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Usuario.class;
    }

	/**
	 * Verifica se o usuário tem a acesso à filial passada por parâmetro
	 * Existem duas maneiras de verificar o acesso:
	 * 1 - FILIAL_USUARIO: Vínculo direto do usuário com a filial
	 * 2 - FILIAL_REGIONAL: Vínculo do usuário com a regional da filial passada por parâmetro
	 */
	public Boolean verificaAcessoFilialRegionalUsuarioLogado(Long idUsuario, Long idFilial){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(fu)");
		sql.addInnerJoin(FilialUsuario.class.getName(), "fu");
		sql.addInnerJoin("fu.empresaUsuario", "eu");

		sql.addCriteria("eu.usuario.id","=",idUsuario);
		sql.addCriteria("fu.filial.id","=",idFilial);

    	List numeroRegistro = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
 	
    	return Boolean.valueOf((((Long)numeroRegistro.get(0)).intValue() > 0));
	}

	/**
	 * Retorna a lista de usuarios do perfil informado filtrando pela filial.
	 * 
	 * author Mickaël Jalbert
	 * @since 23/09/2006
	 * 
	 * @param idPerfil
	 * @param idFilial
	 * @param idEmpresa
	 * @return lista de usuarios
	 * */
	public List findUsuariosByPerfil(Long idPerfil, Long idFilial, Long idEmpresa){
		final String hql = "select distinct u from "+Perfil.class.getName()+" as p " +
										" join p.perfilUsuarios pu " +
										" join pu.usuario usa, " +
										EmpresaUsuario.class.getName() + " as eu " +
										" join eu.usuario u " +
										" left outer join eu.filiaisUsuario fu " +
										" left outer join fu.filial fi " +
										" left outer join eu.regionalUsuario as rep " +
										" left outer join rep.regional.regionalFiliais as refp " + 
								" where ( ( fi.id = ? and fu.blAprovaWorkflow = 'S' ) " +
								"			 or ( eu.blIrrestritoFilial = 'S' ) " +
								"	 		 or ( refp.filial.id = ? " +
								"					and	( refp.dtVigenciaInicial <= sysdate and refp.dtVigenciaFinal >= sysdate) " +
								"					and rep.blAprovaWorkflow = 'S' " +
								"					and eu.empresa.id = ? ) " +
								"		)" +
								"    and p.idPerfil = ? " +
								"    and eu.empresa.id = ?" +
								"    and usa.id = u.id "+
								" order by u.dsEmail";
		
		return this.getAdsmHibernateTemplate().find(hql, new Object[] {
				idFilial, 
				idFilial, 
				idEmpresa, 
				idPerfil, 
				idEmpresa});		
	}
	
    /**
     * Obtém o usuário e o funcionario associado se ele existir.
     * 
     * @param id
     * @return
     */
    public Usuario findById(Long id) {
    	
    	StringBuilder sql = new StringBuilder();
    	sql.append(" from ").append(Usuario.class.getName()).append(" as u ");
    	sql.append(" left join fetch u.vfuncionario as vfuncionario ");
    	sql.append(" left join fetch vfuncionario.filial as filial ");
    	sql.append(" left join fetch filial.pessoa as pessoa ");
    	sql.append(" where u.id = ? ");
    	
		return (Usuario)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{id});
	}
    
    /**
     * Obtém o usuário sem fazer fetch em relacionamentos.
     * 
     * @param id
     * @return
     */
    public Usuario get(Long idUsuario) {
    	// Atencao deve fazer o find usando HQL porque via Session ele carrega o UsuarioADSM devido a
    	// implementação das interfaces
		return (Usuario)getAdsmHibernateTemplate().findUniqueResult("from "+Usuario.class.getName()+" u where u.id = ?", new Object[]{idUsuario});
	}

	public void storeMeuPerfil(final TypedFlatMap m, final EmpresaUsuario empresaUsuario) {
		
		final Usuario usuarioLogado = SessionUtils.getUsuarioLogado();

		boolean usuarioClienteNaoAdmin = !usuarioLogado.isBlAdminCliente() && usuarioLogado.getCliente() != null;
		
		if (!usuarioClienteNaoAdmin) {
			final String updateEmpresaUsuario = "update " + EmpresaUsuario.class.getName() + " eu " + 
												" set eu.filialPadrao.id = ? " +
												" where eu.idEmpresaUsuario = ?";
			final String updateUsuarioADSM = "update " + UsuarioLMS.class.getName() + " ulms " + 
											 " set ulms.empresaPadrao.id = ? " +
											 " where ulms.idUsuario = ?";

			getAdsmHibernateTemplate().bulkUpdate(updateEmpresaUsuario, 
												new Object[]{m.getLong("filialPadrao.idFilial"),
															empresaUsuario.getIdEmpresaUsuario()});
			getAdsmHibernateTemplate().bulkUpdate(updateUsuarioADSM, 
												new Object[]{m.getLong("empresaPadrao.idEmpresa"),
															usuarioLogado.getIdUsuario()});
		}
		
		Locale locale = m.getLocale("locale");
		// somente atualiza no banco o Locale se ele mudou
		if (!locale.equals(usuarioLogado.getLocale())) {
			final String updateLocale = "update " + UsuarioADSM.class.getName() + " set locale = ? where idUsuario = ?";
			getAdsmHibernateTemplate().bulkUpdate(updateLocale, 
													new Object[]{locale, usuarioLogado.getIdUsuario()});
		}
		
	}
	
	private static void mapFiller(Map target, String[] keys, Object[] values) {
		
		for (int i = 0; i < keys.length; i++)
			if (null != values[i])
				target.put(keys[i], values[i].toString());
	}
	
	
	public List findSuggestUsuarioFuncionario(String nrMatricula, String nmFuncionario){
		SqlTemplate sql = getSqlTemplate(null, nrMatricula, nmFuncionario, null, null, "A", null, null, true);
		return getHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	
    /**
     * Consulta de usuarios/funcionarios da lookup.
     * 
     * @param idUsuario
     * @param login
     * @param nrMatricula
     * @param idFilial
     * @param cdFuncao
     * @param cdCargo
     * @param cdSetor
     * @param joinFuncionario
     * @return 
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupUsuarioFuncionario(Long idUsuario, String login, String nrMatricula, Long idFilial, String[] cdFuncao, String cdCargo, String cdSetor, String nrCpf, String [] tpsituacoes, boolean joinFuncionario) {

    	DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class, "usu");

		//Se idUsuario informado, filtra pelo Usuario
		if (idUsuario != null){
			dc.add(Restrictions.eq("usu.idUsuario", idUsuario));
		}
		
		//Se idChapa informada, filtra pela Chapa do funcionario
		if (nrMatricula != null){
			dc.add(Restrictions.eq("usu.nrMatricula", nrMatricula));	
		}
		
		//Se idChapa informada, filtra pela Chapa do funcionario
		if (login != null){
			dc.add(Restrictions.ilike("usu.login", login));	
		}
		
		if (joinFuncionario){
			dc.createAlias("usu.vfuncionario", "func");
			
			//Se idFilial informada, filtra pela filial do funcionario
			if (idFilial != null){
				dc.add(Restrictions.eq("func.filial.id", idFilial));
			}
			
			//Se cdFuncao informada, filtra pela funcao do funcionario
			if (cdFuncao != null && cdFuncao.length > 0){
				dc.add(Restrictions.in("func.cdFuncao", cdFuncao));
			}
			
			//Se cdCargo informado, filtra pelo cargo do funcionario
			if (cdCargo != null){
				dc.add(Restrictions.eq("func.cdCargo", cdCargo));
			}
			 
			//Se cdSetor informado, filtra pelo setor do funcionario
			if (cdSetor != null){
				dc.add(Restrictions.eq("func.cdSetor", cdSetor));
			}
			
			dc.add(Restrictions.not(Restrictions.in("func.tpSituacaoFuncionario",tpsituacoes)));
			
			if (nrCpf != null && !"".equals(nrCpf)){
				dc.add(Restrictions.eq("func.nrCpf", nrCpf));
			}
			
		}
		
		ProjectionList projections = createProjectorUsuarioFuncionario();
	

		dc.setProjection(projections);
		//transforma resultado em Map
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
    	
    }

	/**
	 * LMS 7128
	 * @return
	 */
	private ProjectionList createProjectorUsuarioFuncionario() {
		//Projections para uso especifico na lookup
		//Caso haja a necessidade de adicionar uma nova projection, favor adicionar ao comentario na tag return 
		ProjectionList projections = Projections.projectionList()
			.add(Projections.alias(Projections.property("usu.nrMatricula"),"nrMatricula"))
			.add(Projections.alias(Projections.property("usu.idUsuario"),"idUsuario"))
			.add(Projections.alias(Projections.property("usu.nmUsuario"),"nmUsuario"))
			.add(Projections.alias(Projections.property("usu.login"),"login"))
			.add(Projections.alias(Projections.property("func.dsFuncao"),"dsFuncao"))
			.add(Projections.alias(Projections.property("func.nrCpf"),"nrCpf"))
			.add(Projections.alias(Projections.property("func.dtNascimento"),"dtNascimento"))
			.add(Projections.alias(Projections.property("func.tpSexo"),"tpSexo"))
			.add(Projections.alias(Projections.property("func.nrRg"),"nrRg"))
			.add(Projections.alias(Projections.property("func.dsOrgaoEmissor"),"dsOrgaoEmissor"))
			.add(Projections.alias(Projections.property("func.dtEmissaoRg"),"dtEmissaoRg"))
			.add(Projections.alias(Projections.property("func.nrCnh"),"nrCnh"))
			.add(Projections.alias(Projections.property("func.tpCategoriaCnh"),"tpCategoriaCnh"))
			.add(Projections.alias(Projections.property("func.dtVencimentoHabilitacao"),"dtVencimentoHabilitacao"))
			.add(Projections.alias(Projections.property("func.tpSituacaoFuncionario"), "tpsituacoes"))
			.add(Projections.alias(Projections.property("func.dsEmail"),"dsEmail"));
		return projections;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupMotoristaInstrutor(String nrMatricula, String [] tpsituacoes) {

		DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class, "usu");
	      dc.add(Restrictions.eq("usu.nrMatricula", nrMatricula)); 
	   dc.createAlias("usu.vfuncionario", "func");
	      
	   Criterion restricaoMotorista =  
	     Restrictions.eq("func.cdCargo", "026");

	   Criterion restricaoInstrutorMotorista = Restrictions.and( 
	     Restrictions.eq("func.cdCargo", "025"),
	     Restrictions.in("func.cdFuncao", new String[] { "025.0002", "025.04" })
	   );
	   
	   // Motorista ou Instrutor motorista
	   dc.add(Restrictions.or(restricaoMotorista, restricaoInstrutorMotorista));
	   
	   dc.add(Restrictions.not(Restrictions.in("func.tpSituacaoFuncionario",tpsituacoes)));
			 
		ProjectionList projections = createProjectorUsuarioFuncionario();
		
		dc.setProjection(projections);
		//transforma resultado em Map
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
    	
    }
    /**
     * Andresa Vargas
     * 
     * Retorna os dados da consulta generica do usuario/funcionario
     * 
     * @param idFilial
     * @param nrMatricula
     * @param nmFuncionario
     * @param cdCargo
     * @param cdFuncao
     * @param joinFuncionario
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedCustom(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String[] cdFuncao, 
    		String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario, FindDefinition findDef) {
    	SqlTemplate sql = getSqlTemplate(idFilial, nrMatricula, nmFuncionario, cdCargo, cdFuncao, tpSituacaoFuncionario, cdSetor, nrCpf, joinFuncionario);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
    	
    }
    
    /**
     * Andresa Vargas
     * 
     * Retorna o numero de registros para efetuar a paginacao
     * 
     * @param idFilial
     * @param nrMatricula
     * @param nmFuncionario
     * @param cdCargo
     * @param cdFuncao
     * @param tpSituacaoFuncionario
     * @param joinFuncionario
     * @return
     */
    public Integer getRowCountCustom(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String[] cdFuncao, String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario) {
    	SqlTemplate sql = getSqlTemplate(idFilial, nrMatricula, nmFuncionario, cdCargo, cdFuncao, tpSituacaoFuncionario, cdSetor, nrCpf, joinFuncionario);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }
    
    /**
     * Hector Junior
     * 
     * Retorna os dados da consulta de promotores
     * 
     * @param idFilial
     * @param nrMatricula
     * @param nmFuncionario
     * @param cdCargo
     * @param cdFuncao
     * @param joinFuncionario
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedPromotor(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String cdFuncao, 
    		String tpSituacaoFuncionario, FindDefinition findDef, Object[] cdPromotor) {
    	SqlTemplate sql = getSqlTemplate(idFilial, nrMatricula, nmFuncionario, cdCargo, new String[]{cdFuncao}, tpSituacaoFuncionario, null, null, true);
    	//QUEST CQPRO00029024, somente filtrar por funcionario ativo
    	//sql.addCriteriaIn("vfunc.cdFuncao", cdPromotor);
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
    }
    
    /**
     * Hector Junior
     * 
     * Retorna os dados da consulta de promotores
     * 
     * @param idFilial
     * @param nrMatricula
     * @param nmFuncionario
     * @param cdCargo
     * @param cdFuncao
     * @param tpSituacaoFuncionario
     * @param joinFuncionario
     * @return
     */
    public Integer getRowCountPromotor(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String cdFuncao, String tpSituacaoFuncionario, Object[] cdPromotor) {
    	SqlTemplate sql = getSqlTemplate(idFilial, nrMatricula, nmFuncionario, cdCargo, new String[] {cdFuncao}, tpSituacaoFuncionario, null, null, true);
    	sql.addCriteriaIn("vfunc.cdFuncao", cdPromotor);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }

    /**
     * Andresa Vargas
     * 
     * Monta SQL para consulta de usuarios/funcionarios 
     * 
     * @param idFilial  
     * @param nrMatricula
     * @param nmFuncionario
     * @param cdCargo
     * @param cdFuncao
     * @param tpSituacaoFuncionario
     * @param joinFuncionario
     * @return
     */
    private SqlTemplate getSqlTemplate(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String[] cdFuncao, 
    		String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario){
    	SqlTemplate sql = new SqlTemplate();
    	
    	StringBuffer sb = new StringBuffer();
    	
    	if (joinFuncionario){
	        sb.append(new StringBuffer(Usuario.class.getName()).append(" AS usu ")
				.append("INNER JOIN FETCH usu.vfuncionario AS vfunc ")
	    		.append("INNER JOIN FETCH vfunc.filial AS f ")
	    		.append("INNER JOIN FETCH f.pessoa AS p ")
	    		);
	        
	    	sql.addCriteria("vfunc.filial.idFilial","=",idFilial);
	    	if(cdCargo != null && !cdCargo.isEmpty()) 
	    	sql.addCriteria("vfunc.cdCargo","=",cdCargo);
	    	if(cdFuncao != null && cdFuncao.length > 0)
	    		sql.addCriteriaIn("vfunc.cdFuncao",cdFuncao);
	    	sql.addCriteria("vfunc.cdSetor","=",cdSetor);
	    	sql.addCriteria("vfunc.nrCpf","=",nrCpf);
	    	//condição incluída para atender exclusivamente as necessidades da Retirada de Equipamentos (VOL)
	    	if(tpSituacaoFuncionario.equalsIgnoreCase("A")) {
	    		sql.addCriteria("vfunc.tpSituacaoFuncionario", "=", tpSituacaoFuncionario);
	    	} else {
	    		sql.addCustomCriteria("vfunc.tpSituacaoFuncionario not in ('D','I')");
	    	}

	    } else {
	    	sb.append(new StringBuffer(Usuario.class.getName()).append(" AS usu "));
    	}
    	
    	sql.addFrom(sb.toString());

    	sql.addCriteria("usu.nrMatricula","like",nrMatricula);
    	
    	if (nmFuncionario != null ){
    	sql.addCriteria("lower(usu.nmUsuario)","like",nmFuncionario.toLowerCase());
    	}

    	sql.addOrderBy("usu.nrMatricula");
    	
    	return sql; 

    }
    
    /**
     * Find lookup de promotor
     * @param nrMatricula
     * @param situacoes
     * @return List promotores
     */
    public List findLookupFuncionarioPromotor(String nrMatricula, String[] situacoes){
    	DetachedCriteria dc = DetachedCriteria.forClass(Usuario.class, "usu");
    	dc.createAlias("usu.vfuncionario", "func");
    	dc.createAlias("func.filial","filial");
    	dc.createAlias("filial.regionalFiliais","regionalFilial");
    	dc.createAlias("regionalFilial.regional","regional");

		if (nrMatricula != null){
			dc.add(Restrictions.eq("usu.nrMatricula", nrMatricula));	
		}

		dc.add(Restrictions.not(Restrictions.in("func.tpSituacaoFuncionario", situacoes)));
		dc.add(Restrictions.ge("regionalFilial.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		dc.add(Restrictions.le("regionalFilial.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));

		//Projections para uso especifico na lookup
		//Caso haja a necessidade de adicionar uma nova projection, favor adicionar ao comentario na tag return 
		ProjectionList projections = Projections.projectionList()
			.add(Projections.alias(Projections.property("usu.nrMatricula"),"nrMatricula"))
			.add(Projections.alias(Projections.property("usu.idUsuario"),"idUsuario"))
			.add(Projections.alias(Projections.property("usu.nmUsuario"),"nmUsuario"))
			.add(Projections.alias(Projections.property("func.dsFuncao"),"dsFuncao"))
			.add(Projections.alias(Projections.property("regional.idRegional"),"idRegional"));

		dc.setProjection(projections);
		//transforma resultado em Map
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
    }

    /**
     * Busca um usuario apartir de seu login.
     * 
     * @param login
     * @return List
     */
    public List findUsuarioByLogin(String login) {
    	
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Usuario.class)
			.add(Restrictions.eq("login", login));
    	
    	return this.findByDetachedCriteria(detachedCriteria);
    }

	/**
	 * Busca um usuario apartir de seu login.
	 *
	 * @param loginIdFedex
	 * @return List
	 */
	public List findUsuarioByLoginIdFedex(String loginIdFedex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Usuario.class)
				.add(Restrictions.eq("loginIdFedex", loginIdFedex));

		return this.findByDetachedCriteria(detachedCriteria);
	}

	/**
	 * Busca um usuario apartir de seu login.
	 *
	 * @param loginIdFedex
	 * @return List
	 */
	/*public List findUsuarioByLoginIdFedex(String loginIdFedex) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Usuario.class)
				.add(Restrictions.eq("loginIdFedex", loginIdFedex));

		return this.findByDetachedCriteria(detachedCriteria);
	}*/

    /**
     * Busca um usuario cliente apartir de seu login.
     * 
     * @param login
     * @return Object[]
     */
    public Object[] findUsuarioClienteByLogin(String login) {
    	StringBuilder sql = new StringBuilder();
		sql.append(" SELECT USUAD.LOGIN, USUAD.NM_USUARIO, USUCLI.ID_USUARIO_CLIENTE, USUAD.ID_USUARIO, ")
		.append(" USUAD.TP_CATEGORIA_USUARIO, USUAD.DS_EMAIL AS EMAIL, USUAD.NR_DDD AS DDD, USUAD.NR_FONE AS FONE FROM USUARIO_ADSM USUAD ")
		.append(" LEFT JOIN USUARIO_CLIENTE USUCLI ON USUAD.ID_USUARIO = USUCLI.ID_USUARIO WHERE USUAD.LOGIN = :login");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("LOGIN", Hibernate.STRING);
				sqlQuery.addScalar("NM_USUARIO", Hibernate.STRING);
				sqlQuery.addScalar("ID_USUARIO_CLIENTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_USUARIO", Hibernate.LONG);
				sqlQuery.addScalar("TP_CATEGORIA_USUARIO", Hibernate.STRING);
				sqlQuery.addScalar("EMAIL", Hibernate.STRING);
				sqlQuery.addScalar("DDD", Hibernate.STRING);
				sqlQuery.addScalar("FONE", Hibernate.STRING);
			}
		};
		
		Object[] retorno = null;
		params.put("login", login);
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
		if (result != null && !result.isEmpty()){
			retorno = result.get(0);
		}
		
		return retorno;    	
    }
 
	/**
	 * Retorna o usuario substituto do usuario ou do perfil informado filtrando pela filial.
	 *
	 * @param List<Usuario> idsUsuario
	 * @param Long idPerfil
	 * @param Long idFilial
	 * @param Long idEmpresa
	 * @return List lista de usuarios
	 * */
	public List findSubstitutoByUsuarios(List<Usuario> idsUsuario, Long idPerfil, Long idFilial, Long idEmpresa){
		SqlTemplate sql = new SqlTemplate();		
		sql.addProjection("distinct u");
		sql.addFrom(Substituto.class.getName(), "su " +
				"join su.usuarioByIdUsuarioSubstituto as u");
		sql.addFrom(FilialUsuario.class.getName(), "fu join fu.filial fi "+
													"left outer join fu.empresaUsuario eu " +
											"left outer join eu.usuario ulms " +
											"left outer join eu.empresa emp ");		
		
		sql.addCustomCriteria("((fi.id = ? and ulms.id = u.id and fu.blAprovaWorkflow = 'S' and emp.id = ?) or (eu.blIrrestritoFilial = 'S' and emp.id = ?))");
		sql.addCriteriaValue(idFilial);
		sql.addCriteriaValue(idEmpresa);
		sql.addCriteriaValue(idEmpresa);		
		if (idsUsuario != null && idsUsuario.size() > 0){
			String strIdsUsuario = new String();
			
			for(Iterator<Usuario> iter = idsUsuario.iterator(); iter.hasNext();){
				Usuario usuario = iter.next();
				strIdsUsuario += usuario.getIdUsuario();
				
				if (iter.hasNext()){
					strIdsUsuario += ", ";
				}
			}
			if (idPerfil != null){
				sql.addCustomCriteria("(su.usuarioByIdUsuarioSubstituido IN ("+strIdsUsuario+") or su.perfilSubstituido.id = ?)");
				sql.addCriteriaValue(idPerfil);
			} else {
				sql.addCustomCriteria("su.usuarioByIdUsuarioSubstituido IN ("+strIdsUsuario+")");
			}			
		} else {
			sql.addCriteria("su.perfilSubstituido.id","=",idPerfil);
		}
		sql.addCustomCriteria("TRUNC(CURRENT_DATE) BETWEEN su.dtSubstituicaoInicial and su.dtSubstituicaoFinal");
		return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	/**
	 * Retorna o usuario de acordo com a matricula informada, ou null caso não encontre.
	 * 
	 * @param nrMatricula uma matricula valida
	 * @return
	 * @throws java.lang.IllegalArgumentException caso matricula seja vazia
	 */
    public Usuario findByNrMatricula(String nrMatricula) {
    	if (StringUtils.isBlank(nrMatricula)) {
    		throw new IllegalArgumentException("nrMatricula cannot be null");
    	}
    	final String hql = " from " + Usuario.class.getName() + " u " +
    					   " where u.nrMatricula = ? ";
    			
    	return (Usuario)getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{nrMatricula});
    }

    /**
     * Retorna usuario no formato para lookup sem fazer join com <code>Funcionario</code>
     * @author Vagner Huzalo
     * @since 27/12/2007
     * @param nrMatricula
     * @return
     */
    public List findLookupByNrMatricula(String nrMatricula){
    	Usuario usuario = findByNrMatricula(nrMatricula);
    	List retorno = new ArrayList();
    	if (usuario!=null){
    		retorno.add(usuario);
    	}
    	return retorno;
    }
	
	public Boolean findUsuarioHasPerfil(Long idUsuario, Long idPerfil) {
		StringBuilder sqlPerfil = new StringBuilder();
		

		sqlPerfil.append(" SELECT 1 FROM PERFIL ");
		sqlPerfil.append(" WHERE  ");
		sqlPerfil.append(" ID_PERFIL = ? ");
		sqlPerfil.append(" AND EXISTS ( ");
		sqlPerfil.append("   SELECT 1 FROM PERFIL_USUARIO WHERE ");
		sqlPerfil.append("   PERFIL_USUARIO.ID_USUARIO = ? and ");
		
		sqlPerfil.append("   (PERFIL_USUARIO.ID_PERFIL = PERFIL.ID_PERFIL ");
		  
		sqlPerfil.append("   OR  EXISTS( ");
		sqlPerfil.append("     SELECT 1 FROM PERFIL_HERDADO ");
		sqlPerfil.append("     WHERE  PERFIL_HERDADO.ID_PERFIL_PAI = PERFIL.ID_PERFIL ");
		sqlPerfil.append("     START WITH PERFIL_HERDADO.ID_PERFIL_FILHO = PERFIL_USUARIO.ID_PERFIL ");
		sqlPerfil.append("     CONNECT BY PRIOR PERFIL_HERDADO.ID_PERFIL_FILHO  = PERFIL_HERDADO.ID_PERFIL_PAI "); 
		sqlPerfil.append("   )) ");
		
		sqlPerfil.append(" )");
		
		
		return getAdsmHibernateTemplate().getRowCountBySql(sqlPerfil.toString(), new Object[]{idPerfil, idUsuario }) > 0;
	}

	public Boolean findUsuarioHasPerfil(Long idUsuario, String dsPerfil) {
		StringBuilder sqlPerfil = new StringBuilder();
		

		sqlPerfil.append(" SELECT 1 FROM PERFIL ");
		sqlPerfil.append(" WHERE  ");
		sqlPerfil.append(" DS_PERFIL = ? ");
		sqlPerfil.append(" AND EXISTS ( ");
		sqlPerfil.append("   SELECT 1 FROM PERFIL_USUARIO WHERE ");
		sqlPerfil.append("   PERFIL_USUARIO.ID_USUARIO = ? and ");
		
		sqlPerfil.append("   (PERFIL_USUARIO.ID_PERFIL = PERFIL.ID_PERFIL ");
		  
		sqlPerfil.append("   OR  EXISTS( ");
		sqlPerfil.append("     SELECT 1 FROM PERFIL_HERDADO ");
		sqlPerfil.append("     WHERE  PERFIL_HERDADO.ID_PERFIL_PAI = PERFIL.ID_PERFIL ");
		sqlPerfil.append("     START WITH PERFIL_HERDADO.ID_PERFIL_FILHO = PERFIL_USUARIO.ID_PERFIL ");
		sqlPerfil.append("     CONNECT BY PRIOR PERFIL_HERDADO.ID_PERFIL_FILHO  = PERFIL_HERDADO.ID_PERFIL_PAI "); 
		sqlPerfil.append("   )) ");
		
		sqlPerfil.append(" )");
		
		
		return getAdsmHibernateTemplate().getRowCountBySql(sqlPerfil.toString(), new Object[]{dsPerfil, idUsuario }) > 0;
	}
	
	
	/**
	 * Responsavel por retornar dados do contato do cliente 
	 * @param idUsuario
	 * @return
	 */
	
	public ContatoDto findContato(Long idCliente) {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT	 																");
		sql.append(" USR.NM_USUARIO AS \"nmUsuario\" ,                                   	");
		sql.append(" USR.DS_EMAIL AS \"dsEmail\",                                           ");
		sql.append(" USR.LOGIN AS \"login\",                                           ");
		sql.append(" USR.NR_DDD AS \"nrDdd\",                                            	");
		sql.append(" USR.NR_FONE AS \"nrFone\",                                        		");
		sql.append(" C.ID_FILIAL_ATENDE_COMERCIAL AS \"idFilialAtendeComercial\"       		");
		sql.append(" FROM                                                                   ");
		sql.append(" PESSOA P,                                                              ");
		sql.append(" CLIENTE C,                                                             ");
		sql.append(" PROMOTOR_CLIENTE PC,                                                   ");
		sql.append(" USUARIO_ADSM USR                                                       ");
		sql.append(" WHERE                                                                  ");
		sql.append(" C.ID_CLIENTE = :idCliente	                                            ");
		sql.append(" AND C.ID_CLIENTE = P.ID_PESSOA                                         ");
		sql.append(" AND PC.ID_CLIENTE = P.ID_PESSOA                                        ");
		sql.append(" AND PC.DT_INICIO_PROMOTOR <= SYSDATE                                   ");
		sql.append(" AND (PC.DT_FIM_PROMOTOR >= SYSDATE OR PC.DT_FIM_PROMOTOR IS NULL)      ");
		sql.append(" AND USR.ID_USUARIO = PC.ID_USUARIO                                     ");
		sql.append(" AND ROWNUM = 1                                                         ");
		sql.append(" ORDER BY PC.TP_MODAL DESC                                              ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.aliasToBean(ContatoDto.class));
		query.setParameter("idCliente", idCliente);
		
		return (ContatoDto) query.uniqueResult();
	}

}