package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ContatoDAO extends BaseCrudDao<Contato, Long> {

    public static final String NOME_CONTATO="nmContato";
    public static final String DS_FUNCAO= "dsFuncao";
    public static final String DS_EMAIL= "dsEmail";
    public static final String FONE_CEL= "foneCel";
    public static final String FONE_RES= "foneRes";
    public static final String FONE_COM= "foneCom";
    
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Contato.class;
	}

	/**
	 * Consulta todos os contatos da pessoa informada, comparando com a quantidade mínima.<BR>
	 *@author Robson Edemar Gehl
	 * @param pessoa
	 * @param quantidadeMinima
	 * @return Boolean.TRUE se quantidade de contatos >= quantidade mínima informada; Boolean.FALSE, caso contrário.
	 */
	public Boolean validateQuantidadeMinima(Pessoa pessoa, long quantidadeMinima) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("pessoa", "p");
		dc.add(Restrictions.eq("p.id", pessoa.getIdPessoa()));
		dc.setProjection(Projections.count("idContato"));
		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return Boolean.valueOf(count.longValue() >= quantidadeMinima);
	}
	
	/**
	 * Busca contatos da pessoa
	 * @param map
	 * @return List
	 */
	public List findComboContatos(Map map) {
		TypedFlatMap tfm = (TypedFlatMap) map;
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.projectionList()
				.add(Projections.property("idContato"), "idContato")
				.add(Projections.property("nmContato"), "nmContato")
				);
		Long idPessoa = tfm.getLong("idPessoa");
		if (idPessoa != null) {
			dc.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		}
		
		dc.addOrder(Order.asc("nmContato"));
		
		//transforma a projection do HBM em um Map
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * busca o id do Contato
	 * @param email
	 * @return idContato
	 */
	public List findIdContatoByEmail(String email) {
		SqlTemplate sql = this.createHQLEmail(email);

		StringBuffer projecao = new StringBuffer();
		projecao.append("con");

		sql.addProjection(projecao.toString());

		return getAdsmHibernateTemplate().find(sql.getSql(), email);
	}

	/**
	 * cria o HQL para localizar o idContato pelo dsEmail
	 * @param email
	 * @return idContato
	 */
	private SqlTemplate createHQLEmail(String email) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(Contato.class.getName(), "as con");
		sql.addCriteria("con.dsEmail", "=", email.toString());

		return sql;
	}

	public List findContatoByIdMonitoramentoDocEletronic(Long idMonitoramentoDocEletronic, String tpContato) {
		SqlTemplate sql = new SqlTemplate();

        sql.addProjection("con");

        sql.addFrom(Contato.class.getName() + " con ");
        sql.addFrom(DevedorDocServ.class.getName() + " dds ");
        sql.addFrom(MonitoramentoDocEletronico.class.getName() + " mde ");
        
        sql.addJoin("con.pessoa.id","dds.cliente.id");
        sql.addJoin("dds.doctoServico.id","mde.doctoServico.id");
        
        sql.addCriteria("mde.id","=",idMonitoramentoDocEletronic);
   
		if(tpContato != null ){
			sql.addCriteria("con.tpContato", "=", tpContato);
		}

		List<Contato> contatoList = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
		return contatoList;
	}

	/**
	 * Método responsável por popular a combo de telefoneContato
	 * @param idPessoa
	 * @return List de Contatos
	 */
	public List findComboContato(Long idPessoa) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(c.nmContato as nmContato," + 
							"te.nrTelefone as nrTelefone, " +
							"tc.nrRamal as nrRamal, " +
							"tc.idTelefoneContato as idTelefoneContato)");

		hql.addFrom(TelefoneContato.class.getName() + " tc " +
				"JOIN tc.contato c " + 
				"JOIN tc.telefoneEndereco te ");
 
		hql.addCriteria("c.pessoa.idPessoa", "=", idPessoa);

		List lst = getHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		for (Iterator iter = lst.iterator(); iter.hasNext();) {
			Map element = (Map) iter.next();
			
			String nrTelefone = (String)element.remove("nrTelefone");
			String nrRamal = (String)element.remove("nrRamal");
			String nmContato = (String)element.remove("nmContato");
			
			if(nmContato != null && nrTelefone != null && nrRamal != null) {
				element.put("contato", nmContato + " - " + nrTelefone + " - " + nrRamal);
			}else if(nmContato != null && nrTelefone != null && nrRamal == null) {
				element.put("contato", nmContato + " - " + nrTelefone);
			}else{
				element.put("contato", "");
			}
		}
		return lst;
	}

	public List<Contato> findContato(Long idUsuario, String tpContato, Long idPessoa) {

		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("usuario", "u")
		.add(Restrictions.eq("u.idUsuario", idUsuario))
		.add(Restrictions.eq("tpContato", tpContato))
		.add(Restrictions.eq("pessoa.idPessoa", idPessoa));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public Contato findContato(Long idContato, Long idPessoa, String tpContato) {
		
		DetachedCriteria dc = createDetachedCriteria()
			.add(Restrictions.eq("idContato", idContato))
			.add(Restrictions.eq("tpContato", tpContato))
			.add(Restrictions.eq("pessoa.idPessoa", idPessoa));

		return (Contato)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public Contato findContatoByIdPessoaAndTipoContato(Long idPessoa, String tpContato) {
		List<Contato> contatos = findContatosByIdPessoaAndTipoContato(idPessoa, tpContato);
		return contatos == null || contatos.isEmpty() ? null : contatos.get(0);
	}

	public List<Contato> findContatosByIdPessoaAndTipoContato(Long idPessoa, String tpContato) {
		DetachedCriteria dc = createDetachedCriteria()
			.add(Restrictions.eq("tpContato", tpContato))
			.add(Restrictions.eq("pessoa.idPessoa", idPessoa));

		List<Contato> contatos = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return contatos;
	}

	public Contato findContatoById(Long id) {
		final String hql = "from " + Contato.class.getName() + " as contato join fetch contato.pessoa as pessoa WHERE contato.idContato = ? ";
		return  (Contato) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{id});
    }

    public List<Object[]> findContatoCrm(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.nm_Contato as ").append(NOME_CONTATO);
        sql.append(", c.ds_Funcao as ").append(DS_FUNCAO);
        sql.append(", c.ds_Email as ").append(DS_EMAIL);
        sql.append(", ").append(getSqlSubselectTeleforneContato("E", FONE_CEL));
        sql.append(", ").append(getSqlSubselectTeleforneContato("C", FONE_COM));
        sql.append(", ").append(getSqlSubselectTeleforneContato("R", FONE_RES));
        sql.append(" FROM CONTATO c ");
        sql.append(" join cliente on cliente.id_cliente = c.id_pessoa ");
        sql.append(" join filial_crm on cliente.id_filial_atende_comercial = filial_crm.id_filial_crm ");
        sql.append(" WHERE c.id_pessoa in ( :idCliente ) ");
        sql.append(" and filial_crm where dt_implantacao < sysdate ");
        return this.getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQueryContato());
    }
    
    private String getSqlSubselectTeleforneContato(String tipo, String alias) {
        StringBuilder sql = new StringBuilder();
        sql.append("(select te.nr_Ddd || '-' || te.nr_Telefone ");
        sql.append(" FROM Telefone_Contato tc ");
        sql.append(" , Telefone_Endereco te ");
        sql.append(" WHERE tc.id_contato = te.id_pessoa ");
        sql.append(" AND tc.id_telefone_Endereco = te.id_telefone_Endereco ");
        sql.append(" AND te.tp_Telefone = '").append(tipo).append("' ");
        sql.append(" AND ROWNUM =1) AS ").append(alias);
        return sql.toString();
    }

    private ConfigureSqlQuery configureSqlQueryContato() {
        return new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar(NOME_CONTATO, Hibernate.STRING);
                sqlQuery.addScalar(DS_FUNCAO, Hibernate.STRING);
                sqlQuery.addScalar(DS_EMAIL, Hibernate.STRING);
                sqlQuery.addScalar(FONE_CEL, Hibernate.STRING);
                sqlQuery.addScalar(FONE_CEL, Hibernate.STRING);
                sqlQuery.addScalar(FONE_COM, Hibernate.STRING);
            }
        };
    }

	public Contato findContatoByEmail(String email) {
		DetachedCriteria dc = createDetachedCriteria()
				.add(Restrictions.eq("dsEmail", email.toLowerCase()))
				.add(Restrictions.eq("tpContato", "RD"));

		List<Contato> contatos = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return contatos == null || contatos.isEmpty() ? null : contatos.get(0);
	}

	public Object[] findContatoTelefoneFilialByIdFilialTpContato(Long idFilial, String tpContato) {
		  StringBuilder sql = new StringBuilder();
	        sql.append(" select CO.NM_CONTATO, FL.SG_FILIAL, CO.DS_EMAIL, TE.NR_DDD, TE.NR_TELEFONE ");
	        sql.append(" from CONTATO CO ");
	        sql.append(" join TELEFONE_ENDERECO TE on CO.ID_PESSOA = TE.ID_PESSOA ");
	        sql.append(" join filial fl on CO.ID_PESSOA = FL.ID_FILIAL ");
	        sql.append(" where ");
	        sql.append("	CO.ID_PESSOA = :idFilial ");
	        sql.append("	and CO.TP_CONTATO = :tpContato ");
	        sql.append("	and rownum = 1 ");
	        
	        ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
				@Override
				public void configQuery(SQLQuery sqlQuery) {
					sqlQuery.addScalar("NM_CONTATO", Hibernate.STRING);
					sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
					sqlQuery.addScalar("DS_EMAIL", Hibernate.STRING);
					sqlQuery.addScalar("NR_DDD", Hibernate.STRING);
					sqlQuery.addScalar("NR_TELEFONE", Hibernate.STRING);
					
				}
			};
			Map params = new HashMap();
			params.put("idFilial", idFilial);
			params.put("tpContato", tpContato);
	        
			List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery); 
			return (result != null && !result.isEmpty()) ? result.get(0) : null;
		
	}

}