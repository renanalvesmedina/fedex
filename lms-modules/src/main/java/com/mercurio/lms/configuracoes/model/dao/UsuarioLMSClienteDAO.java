/**
 * 
 */
package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ClienteUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 * 
 * @author juliosce
 *
 */
public class UsuarioLMSClienteDAO extends BaseCrudDao<ClienteUsuario, Long> {

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#getPersistentClass()
	 */
	@Override
	protected Class getPersistentClass() {
		return ClienteUsuario.class;
	}
	
	/**
	 * Retorna registros para a lookup perfil que o usuário logado tem acesso, verificação feita no objeto PerfilUsuario.
	 * 
	 * @param map
	 * @param usuarioSessao
	 * @return List
	 */
	public List findLookupPerfil( TypedFlatMap map, Usuario usuarioSessao ){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection( "p" );		
		if( usuarioSessao.isBlAdminCliente() ){
			hql.addFrom( PerfilUsuario.class.getName() + " as pu join pu.perfil as p ");
			hql.addCriteria("pu.usuario.idUsuario", "=", usuarioSessao.getIdUsuario());
		}else{
			hql.addFrom( Perfil.class.getName() + " as  p ");
		}
		hql.addCriteria("lower(p.dsPerfil)", "like", map.get("dsPerfil").toString().toLowerCase() );
		
		return getHibernateTemplate().find( hql.getSql(),hql.getCriteria() );
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedAdminCliente(Map criteria, FindDefinition findDef) {
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		SqlTemplate sql = this.getSqlFindPaginated((TypedFlatMap) criteria);

		final String projection = "new Map(cl.idCliente as idCliente," +
										" pe.nmPessoa as pessoa_nmPessoa," +
										" pe.tpPessoa as pessoa_tpPessoa," +
										" pe.nrIdentificacao as pessoa_nrIdentificacao," +
										" pe.tpIdentificacao as pessoa_tpIdentificacao, " +
										" pe.nmFantasia as pessoa_nmFantasia," +
										" cl.nrConta as nrConta," +
										" cl.tpCliente as tpCliente," +
										" cl.tpSituacao as tpSituacao, " +
										" filc.id as filialByIdFilialCobranca_idFilial," +
										" filc.sgFilial as filialByIdFilialCobranca_sgFilial, " +
										" pesfilc.nmFantasia as filialByIdFilialCobranca_pessoa_nmFantasia)";

		sql.addProjection(projection.toString());

		StringBuffer joins = new StringBuffer();
		joins.append(" join fetch cl.pessoa as pe ");
		joins.append(" join fetch cl.filialByIdFilialCobranca as filc ");
		joins.append(" join fetch filc.pessoa as pesfilc ");

		if (criteria.containsKey("filialByIdFilialAtendeOperacional")){
			String filialOperacional = (String) ((Map)criteria.get("filialByIdFilialAtendeOperacional")).get("idFilial");
			if(StringUtils.isNotBlank(filialOperacional)) {
				sql.addCriteria("fOperacional.idFilial","=", filialOperacional, Long.class);
				joins.append(" join cl.filialByIdFilialAtendeOperacional fOperacional ");
			}
		}
		
		sql.addFrom( ClienteUsuario.class.getName() + " as cu join cu.cliente as cl" + joins.toString() );
		sql.addCriteria("cu.usuarioLMS.idUsuario", "=", usuarioSessao.getIdUsuario());
		
		sql.addOrderBy("pe.nmPessoa");
		sql.addOrderBy("pe.tpIdentificacao");
		sql.addOrderBy("pe.nrIdentificacao");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria()); 
		List list = rsp.getList();
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(list));
		return rsp;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountAdminCliente(TypedFlatMap criteria) {
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();
		SqlTemplate sql = this.getSqlFindPaginated(criteria);
		sql.addProjection("count(cl.idCliente)");

		StringBuilder joins = new StringBuilder();
		joins.append(" join cl.pessoa as pe ");

		if (criteria.containsKey("filialByIdFilialAtendeOperacional")){
			String filialOperacional = (String) ((Map)criteria.get("filialByIdFilialAtendeOperacional")).get("idFilial");
			if(StringUtils.isNotBlank(filialOperacional)) {
				sql.addCriteria("fOperacional.idFilial", "=", filialOperacional, Long.class);
				joins.append(" join cl.filialByIdFilialAtendeOperacional fOperacional ");
			}
		}

		sql.addFrom( ClienteUsuario.class.getName() + " as cu join cu.cliente as cl" + joins.toString());
		sql.addCriteria("cu.usuarioLMS.idUsuario", "=", usuarioSessao.getIdUsuario());		
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}
	
	/**
	 * Método que retorna uma parte do sql do findPaginated
	 * 
	 * @param Map criteria
	 * @return SqlTemplate sql
	 * */
	private SqlTemplate getSqlFindPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(DomainValue.class.getName() + " dv join dv.domain do");		
		sql.addJoin("nvl(pe.tpIdentificacao, 'CNPJ')","dv.value"); 
		sql.addCustomCriteria("do.name = 'DM_TIPO_IDENTIFICACAO'");

		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			sql.addCriteria("lower(pe.nrIdentificacao)", "like", nrIdentificacao.toLowerCase());
		}
		sql.addCriteria("pe.tpIdentificacao", "=", criteria.getString("pessoa.tpIdentificacao"));
		String nmPessoa = criteria.getString("pessoa.nmPessoa");
		if (StringUtils.isNotBlank(nmPessoa)) {
			sql.addCriteria("lower(pe.nmPessoa)", "like", nmPessoa.toLowerCase());
		}
		sql.addCriteria("pe.tpPessoa", "like", criteria.getString("pessoa.tpPessoa"));
		String nmFantasia = criteria.getString("nmFantasia");
		if (StringUtils.isNotBlank(nmFantasia)) {
			sql.addCriteria("lower(pe.nmFantasia)", "like", nmFantasia.toLowerCase());
		}
		sql.addCriteria("cl.nrConta", "=", criteria.getLong("nrConta"));
		sql.addCriteria("cl.tpCliente", "like", criteria.getString("tpCliente"));
		sql.addCriteria("cl.tpSituacao", "like", criteria.getString("tpSituacao"));
		sql.addCriteria("cl.usuariosCliente.usuarioLMS.id", "=", criteria.getString("usuariosCliente.usuarioLMS.idUsuario"));
		
		return sql;
	} 

	/**
	 * Faz a busca em todos os clientes que o usuário tem acesso, ou se o usuário tiver acesso irrestrito a clientes,
	 * faz a busca em todos os clientes armazenados.
	 * 
	 * @param map
	 * @param usuarioSessao
	 * @return List
	 */
	public List findLookupClientesAtivos( TypedFlatMap map, Usuario usuarioSessao ){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection( "c" );
		if(!usuarioSessao.getBlIrrestritoCliente()) {
			hql.addFrom( ClienteUsuario.class.getName() + " as cu join cu.cliente as c join fetch c.pessoa as p");
			hql.addCriteria("cu.usuarioLMS.idUsuario", "=", usuarioSessao.getIdUsuario());
		} else {
			hql.addFrom( Cliente.class.getName() + " as c join fetch c.pessoa as p ");
		}
		hql.addCriteria("c.pessoa.nrIdentificacao", "like",
				PessoaUtils.validateIdentificacao(map.get("pessoa.nrIdentificacao").toString()));

		return getHibernateTemplate().find( hql.getSql(),hql.getCriteria() );		
	}

	/**
	 * Verifica se o usuaário logado possui cliente associado, se sim retorna este usuário.
	 * 
	 * @param idUsuario
	 * @return UsuarioLMS
	 */
	public Cliente findClienteAssociadoByUsuarioSessao( Long idUsuario){
		final String hql = "select c from "+UsuarioLMS.class.getName()+ " as u" +
				" join u.cliente as c "+
				" inner join fetch c.pessoa as p where u.id = ?";
		
		return (Cliente) getAdsmHibernateTemplate().findUniqueResult( hql, new Object[] {idUsuario} );
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedUsuarioLmsCliente( TypedFlatMap criteria, FindDefinition findDef ){
		String usuarioHql = " new Map( uadsm.nmUsuario as nmUsuario," +
									 " uadsm.idUsuario as idUsuario,"+
									 " uadsm.login as login," +
									 " uadsm.blAtivo as blAtivo, " +
									 " uadsm.dhCadastro as dhCadastro" +
									 " )"; 
		
		SqlTemplate hql = montaHqlPaginateUsuario(usuarioHql, criteria);
		hql.addOrderBy(" uadsm.nmUsuario asc ");

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),
														findDef.getCurrentPage(), 
														findDef.getPageSize(),
														hql.getCriteria());		
	}

	/**
	 * 
	 * @param map
	 * @return Integer
	 */
	public Integer getRowCountUsuarioLmsCliente( TypedFlatMap criteria ){
		String usuarioHql = "count(u) as quantidade ";
		SqlTemplate hql = montaHqlPaginateUsuario(usuarioHql, criteria);
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult( hql.getSql(), hql.getCriteria());

		return result.intValue();
	}

	/**
	 * 
	 * @param projection
	 * @param criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate montaHqlPaginateUsuario(String projection, TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(UsuarioLMS.class.getName()+ " as u" +
					" join u.cliente as c" +
					" join u.usuarioADSM as uadsm "
					);

		if (criteria.get("clienteDoUsuario.idCliente") != null) {
			hql.addCriteria("c.idCliente", "=", criteria.get("clienteDoUsuario.idCliente"), Long.class);
		}

		final String nmUsuario = (String) criteria.get("nmUsuario");
		if (StringUtils.isNotBlank(nmUsuario)) {
			hql.addCriteria("upper(uadsm.nmUsuario)", "like", nmUsuario.toUpperCase());
		}
		
		if (criteria.get("dhCadastro") != null) {
			hql.addCriteria("uadsm.dhCadastro.value", ">=", criteria.getDateTime("dhCadastro"));
		}
		
		hql.addCriteria("uadsm.blAtivo", "=", criteria.get("blAtivo"));
		
		final String login = (String)criteria.get("login");
		if (StringUtils.isNotBlank(login)) {
			hql.addCriteria("upper(uadsm.login)", "like", login.toUpperCase().trim());
		}
		
		hql.addProjection(projection);

		return hql;
	}

}
