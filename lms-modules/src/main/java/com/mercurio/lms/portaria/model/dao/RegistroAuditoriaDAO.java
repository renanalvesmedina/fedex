package com.mercurio.lms.portaria.model.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.portaria.model.RegistroAuditoria;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistroAuditoriaDAO extends BaseCrudDao<RegistroAuditoria, Long> {

	private final StringBuffer querySequencialNrRegistroAuditoria = new StringBuffer()
	.append("select count(*) ")
	.append("from " + RegistroAuditoria.class.getName() + " ra ")
	.append("where ra.filial.id = ? ");
	
	private final StringBuffer queryEmpresasIntegrantesByIdControleCarga = new StringBuffer()
	.append(" select new map (eo.idEquipeOperacao as idEquipeOperacao, eo.versao as versao, p.nmPessoa as usuario_funcionario_codPessoa_nome, p.nrIdentificacao as funcionario_chapa, p.nrIdentificacao as idChapa, ieo.tpIntegrante as tpIntegrante, e.id as idEmpresa) ")
	.append(" from " + IntegranteEqOperac.class.getName() + " ieo ")
	.append(" join ieo.equipeOperacao eo ")
	.append(" join eo.controleCarga cc ")
	.append(" join ieo.empresa e ")
	.append(" join e.pessoa p ")
	.append(" where cc.id = ? ")
	.append(" and ieo.tpIntegrante = ? ")
	.append(" and eo.dhInicioOperacao.value =  ")
	.append(" 		(select max(eo_.dhInicioOperacao.value) ")
	.append(" 		 from EquipeOperacao eo_ ")
	.append(" 		 where eo_.controleCarga.idControleCarga = cc.id) ");
	
	
	private final StringBuffer queryEmpresasIntegrantesByIdEquipeOperacao= new StringBuffer()
	.append(" select new map (eo.idEquipeOperacao as idEquipeOperacao, eo.versao as versao, p.nmPessoa as usuario_funcionario_codPessoa_nome, p.nrIdentificacao as funcionario_chapa, p.nrIdentificacao as idChapa, ieo.tpIntegrante as tpIntegrante, e.id as idEmpresa) ")
	.append(" from " + IntegranteEqOperac.class.getName() + " ieo ")
	.append(" join ieo.equipeOperacao eo ")	
	.append(" join ieo.empresa e ")
	.append(" join e.pessoa p ")
	.append(" where eo.id = ? ")
	.append(" and ieo.tpIntegrante = ? ")
	.append(" and eo.dhInicioOperacao.value =  ")
	.append(" 		(select max(eo_.dhInicioOperacao.value) ")
	.append(" 		 from EquipeOperacao eo_ ")
	.append(" 		 where eo_.id = eo.id) ");
	
	private final StringBuffer queryFuncionariosIntegrantesByIdControleCarga = new StringBuffer()
	.append(" select new map (eo.idEquipeOperacao as idEquipeOperacao, eo.versao as versao, f.nmFuncionario as usuario_funcionario_codPessoa_nome, f.nrMatricula as funcionario_chapa, f.nrMatricula as idChapa, ieo.tpIntegrante as tpIntegrante, u.id as idUsuario) ")
	.append(" from " + IntegranteEqOperac.class.getName() + " ieo ")
	.append(" join ieo.equipeOperacao eo ")
	.append(" join eo.controleCarga cc ")
	.append(" join ieo.usuario u ")
	.append(" join u.vfuncionario f ")
	.append(" where cc.id = ? ")
	.append(" and ieo.tpIntegrante = ? ")
	.append(" and eo.dhInicioOperacao.value =  ")
	.append(" 		(select max(eo_.dhInicioOperacao.value) ")
	.append(" 		 from EquipeOperacao eo_ ")
	.append(" 		 where eo_.controleCarga.idControleCarga = cc.id) ");

	private final StringBuffer queryFuncionariosIntegrantesByIdEquipeOperacao = new StringBuffer()
	.append(" select new map (eo.idEquipeOperacao as idEquipeOperacao, eo.versao as versao, f.nmFuncionario as usuario_funcionario_codPessoa_nome, f.nrMatricula as funcionario_chapa, f.nrMatricula as idChapa, ieo.tpIntegrante as tpIntegrante, u.id as idUsuario) ")
	.append(" from " + IntegranteEqOperac.class.getName() + " ieo ")
	.append(" join ieo.equipeOperacao eo ")	
	.append(" join ieo.usuario u ")
	.append(" join u.vfuncionario f ")
	.append(" where eo.id = ? ")
	.append(" and ieo.tpIntegrante = ? ")
	.append(" and eo.dhInicioOperacao.value =  ")
	.append(" 		(select max(eo_.dhInicioOperacao.value) ")
	.append(" 		 from EquipeOperacao eo_ ")
	.append(" 		 where eo_.id = eo.id) ");
	
	private final StringBuffer queryDhEmissaoFromLastEventoEmitidoByIdControleCarga = new StringBuffer()
	.append(" select ecc.dhEvento from " + EventoControleCarga.class.getName() + " ecc ")
	.append("  where ecc.id = ( select max(ecc2.id) ")
	.append("                     from " + EventoControleCarga.class.getName() + " ecc2 ")
	.append("                    where ecc2.controleCarga.id = ? ")
	.append("                      and ecc2.tpEventoControleCarga = ? ) ");
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegistroAuditoria.class;
    }

    /**
     * Retorna a data do último evento (emitido) 
     * do controle de carga recebido.
     * @param idControleCarga 
     * @return
     * @author luisfco
     */
    public DateTime findDhEmissaoFromLastEventoEmitidoByIdControleCarga(Long idControleCarga) {
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryDhEmissaoFromLastEventoEmitidoByIdControleCarga.toString());
    	q.setParameter(0, idControleCarga);
    	q.setParameter(1, "EM");
    	return (DateTime) q.uniqueResult();
    }
    
    /**
     * Retorna os funcionarios integrantes de IntegranteEqOperac 
     * de acordo com o id do controle de carga.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
    public List findFuncionariosIntegrantesByIdControleCarga(Long idControleCarga) {
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryFuncionariosIntegrantesByIdControleCarga.toString());
    	q.setParameter(0, idControleCarga);
    	q.setParameter(1, "F");
    	
    	return q.list();
    }
         
    
    public List findFuncionariosIntegrantesByIdEquipeOperacao(Long idEquipeOperacao) {
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryFuncionariosIntegrantesByIdEquipeOperacao.toString());
    	q.setParameter(0, idEquipeOperacao);
    	q.setParameter(1, "F");
    	
    	return q.list();
    }
    
    
    /**
     * Retorna as empresas integrantes de IntegranteEqOperac 
     * de acordo com o id do controle de carga.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
    public List findEmpresasIntegrantesByIdControleCarga(Long idControleCarga) {    	
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryEmpresasIntegrantesByIdControleCarga.toString());
    	q.setParameter(0, idControleCarga);
    	q.setParameter(1, "T");
    	
    	return q.list();
    }
    
    public List findEmpresasIntegrantesByIdEquipeOperacao(Long idEquipeOperacao) {    	
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryEmpresasIntegrantesByIdEquipeOperacao.toString());
    	q.setParameter(0, idEquipeOperacao);
    	q.setParameter(1, "T");
    	
    	return q.list();
    }
    
    public List findEquipeByIdRegistroAuditoria(Long idRegistroAuditoria){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	ProjectionList pl = Projections.projectionList()
    						.add(Projections.property("us.nmUsuario"), "usuario.funcionario.codPessoa.nome")    						
    						.add(Projections.property("us.idUsuario"), "usuario.idUsuario")
    						.add(Projections.property("ea.idEquipeAuditoria"), "idEquipe");
    	
    	dc.setProjection(pl);
			
    	dc.createAlias("equipeAuditorias", "ea");
    	dc.createAlias("ea.usuario", "us");
    	dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
    	dc.add(Restrictions.idEq(idRegistroAuditoria));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    
    
    
	/**
	* Retorna nrRegistroAuditoria sequencial por filial
	* @param idFilial
	* @return
	* @author luisfco
	*/
	public Integer getSequencialNrRegistroAuditoria(Long idFilial) {
	   
	   Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(querySequencialNrRegistroAuditoria.toString());
	   q.setParameter(0, idFilial);
	   return (Integer)q.uniqueResult();
	}
	
	private SqlTemplate createFindStructure(TypedFlatMap tfm, SqlTemplate sql) {
				
		StringBuffer sqlFrom = new StringBuffer();
		
		sqlFrom.append(getPersistentClass().getName() + " ra ");
		sqlFrom.append(" inner join ra.filial fil inner join fil.pessoa pes");
		sqlFrom.append(" inner join ra.controleCarga con");
		sqlFrom.append(" inner join con.filialByIdFilialOrigem con_filial");
		sqlFrom.append(" inner join ra.meioTransporteRodoviario mtr");
		sqlFrom.append(" inner join mtr.meioTransporte mt");
		sqlFrom.append(" left join ra.semiReboque sr");
		sqlFrom.append(" left join sr.meioTransporte mtSr");						
				
		sql.addFrom(sqlFrom.toString());
		 
		sql.addCriteria("ra.idRegistroAuditoria", "=", tfm.getLong("idRegistroAuditoria"));				
		sql.addCriteria("fil.idFilial", "=", tfm.getLong("filial.idFilial"));		
		sql.addCriteria("ra.nrRegistroAuditoria", "=", tfm.getInteger("nrRegistroAuditoria"));		
		sql.addCriteria("trunc(cast(ra.dhRegistroAuditoria.value as date))", ">=", tfm.getYearMonthDay("dtRegistroInicial"));		
		sql.addCriteria("trunc(cast(ra.dhRegistroAuditoria.value as date))", "<=", tfm.getYearMonthDay("dtRegistroFinal"));		
		sql.addCriteria("con.idControleCarga", "=", tfm.getLong("controleCarga.idControleCarga"));	
		sql.addCriteria("mt.idMeioTransporte", "=", tfm.getLong("meioTransporteRodoviario.idMeioTransporte"));	
		sql.addCriteria("mtSr.idMeioTransporte", "=", tfm.getLong("semiReboque.idMeioTransporte"));		
		sql.addCriteria("ra.tpResultado", "=", tfm.getDomainValue("tpResultado") != null ? tfm.getDomainValue("tpResultado").getValue() : null);	 
		sql.addCriteria("trunc(cast(ra.dhLiberacao.value as date))", ">=", tfm.getYearMonthDay("dtLiberacaoInicial"));
		sql.addCriteria("trunc(cast(ra.dhLiberacao.value as date))", "<=", tfm.getYearMonthDay("dtLiberacaoFinal"));

		if ((tfm.getDomainValue("meioTransporteLiberado") != null) && 
			("S".equals(tfm.getDomainValue("meioTransporteLiberado").getValue()))) {			
				sql.addCustomCriteria("(ra.tpResultado = 'A' or (ra.tpResultado = 'R' and ra.dhLiberacao.value is not null))");			
			 
		} else if ((tfm.getDomainValue("meioTransporteLiberado") != null) && 
				   ("N".equals(tfm.getDomainValue("meioTransporteLiberado").getValue()))) {			
			sql.addCustomCriteria("ra.tpResultado = 'R'");
			sql.addCustomCriteria("ra.dhLiberacao.value is null");			
		}
		
		return sql;
	}
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap tfm, FindDefinition findDef) {
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(ra.idRegistroAuditoria", "idRegistroAuditoria");
		sql.addProjection("fil.sgFilial", "filial_sgFilial");
		sql.addProjection("pes.nmFantasia", "filial_pessoa_nmFantasia");
		sql.addProjection("ra.nrRegistroAuditoria", "nrRegistroAuditoria");
		sql.addProjection("ra.dhRegistroAuditoria", "dhRegistroAuditoria");
		sql.addProjection("con_filial.sgFilial", "controleCarga_filialByIdFilialOrigem_sgFilial");
		sql.addProjection("con.nrControleCarga", "controleCarga_nrControleCarga");
		sql.addProjection("mt.nrIdentificador", "meioTransporteRodoviario_nrIdentificador");
		sql.addProjection("mt.nrFrota", "meioTransporteRodoviario_nrFrota");
		sql.addProjection("mtSr.nrIdentificador", "semiReboque_nrIdentificador");
		sql.addProjection("mtSr.nrFrota", "semiReboque_nrFrota");
		sql.addProjection("ra.tpResultado", "tpResultado");
		sql.addProjection("ra.dhLiberacao", "dhLiberacao)");
						
		createFindStructure(tfm, sql);
				
		sql.addOrderBy("fil.sgFilial");
		sql.addOrderBy("ra.nrRegistroAuditoria");
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true),  findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}
	
	public Integer getRowCountCustom(TypedFlatMap tfm) {
		
		SqlTemplate sql = new SqlTemplate();
				
		createFindStructure(tfm, sql);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(true), sql.getCriteria());

	}
	
	public RegistroAuditoria findByIdCustom(Long idRegistroAuditoria) {
				
		return (RegistroAuditoria) findById(idRegistroAuditoria);
	}
	
	    
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("equipeOperacao", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);		
		lazyFindById.put("meioTransporteRodoviario", FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
		lazyFindById.put("semiReboque", FetchMode.JOIN);
		lazyFindById.put("semiReboque.meioTransporte", FetchMode.JOIN);				
		lazyFindById.put("usuario", FetchMode.JOIN);
		lazyFindById.put("usuarioLiberacao", FetchMode.JOIN);
		lazyFindById.put("controleCarga", FetchMode.JOIN);
		lazyFindById.put("pendencia", FetchMode.JOIN);
		lazyFindById.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindById.put("controleCarga.filialByIdFilialOrigem.pessoa", FetchMode.JOIN);		
		lazyFindById.put("controleCarga.filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindById.put("controleCarga.filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		lazyFindById.put("controleCarga.motorista", FetchMode.JOIN);
		lazyFindById.put("controleCarga.motorista.pessoa", FetchMode.JOIN);	
	}
	
}