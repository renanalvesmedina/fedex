package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.AgrupadorCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.EnvioCteCliente;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class EnvioCteClienteDAO extends BaseCrudDao<EnvioCteCliente, Long> {

    @Override
    protected final Class getPersistentClass() {
	return EnvioCteCliente.class;
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
	SqlTemplate hql = montaQueryPaginated(criteria);
	ResultSetPage rs = getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(),
		findDef.getPageSize(), hql.getCriteria());
	return rs;
    }

    public Integer getRowCount(TypedFlatMap criteria) {
	SqlTemplate hql = montaQueryPaginated(criteria);
	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
    }

    public SqlTemplate montaQueryPaginated(TypedFlatMap criteria) {
	SqlTemplate hql = new SqlTemplate();

	StringBuffer projecao = new StringBuffer("new Map(")
		.append("ecc.tpParametrizacao as tpParametrizacao, ")
		.append("pess.nrIdentificacao as nrIdentificacao, ")
		.append("pess.nmPessoa as nmPessoa, pess.tpIdentificacao as tpIdentificacao, ecc.id as idEnvioCteCliente)");
	hql.addProjection(projecao.toString());

	StringBuffer from = new StringBuffer("EnvioCteCliente ecc ").append("left outer join ecc.cliente cli ").append(
		"left outer join cli.pessoa pess ");
	hql.addFrom(from.toString());

	hql.addCriteria("cli.id", "=", criteria.getLong("idCliente"));

	return hql;
    }

    public SqlTemplate montaQueryCheques(Long idDoctoServico) {
	SqlTemplate hql = new SqlTemplate();
	hql.addProjection("new Map(" + "cheques.nrBanco as nrBanco, " + "cheques.nrAgencia as nrAgencia, "
		+ "cheques.dvAgencia as dvAgencia, " + "cheques.nrCheque as nrCheque, "
		+ "cheques.idChequeReembolso as idChequeReembolso, " + "cheques.dtCheque as data, "
		+ "cheques.vlCheque as valorCheque)");
	hql.addFrom("ReciboReembolso rrem " + "join rrem.chequeReembolsos cheques ");
	hql.addCriteria("rrem.idDoctoServico", "=", idDoctoServico);

	return hql;
    }

    public EnvioCteCliente findEnvioCteClienteByIdClienteTpEnvioEAndParametrizacaoC(Long idCliente) {
	if (idCliente == null) {
	    return null;
	}

	Criteria criteria = getSession().createCriteria(getPersistentClass(), "ecc");
	criteria.createAlias("ecc.cliente", "cli");
	criteria.add(Restrictions.eq("ecc.tpEnvio", "E"));
	criteria.add(Restrictions.eq("ecc.tpParametrizacao", "C"));
	criteria.add(Restrictions.eq("cli.idCliente", idCliente));
	return (EnvioCteCliente) criteria.uniqueResult();
    }

    public EnvioCteCliente findByIdClienteParametrizacaoC(Long idCliente) {
	if (idCliente == null) {
	    return null;
	}

	Criteria criteria = getSession().createCriteria(getPersistentClass(), "ecc");
	criteria.createAlias("ecc.cliente", "cli");
	criteria.add(Restrictions.eq("ecc.tpParametrizacao", "C"));
	criteria.add(Restrictions.eq("cli.idCliente", idCliente));
	return (EnvioCteCliente) criteria.uniqueResult();
    }

    public List findByCliente(Long idCliente) {
	if (idCliente == null) {
	    return null;
	}
	Criteria criteria = getSession().createCriteria(getPersistentClass(), "ecc");
	criteria.createAlias("ecc.cliente", "cli");
	criteria.add(Restrictions.eq("cli.idCliente", idCliente));
	return criteria.list();

    }
    
    public EnvioCteCliente findEnvioCteClienteByIdClienteETipoRelatorio(Long idCliente, String tipoRelatorio) {
    	if (idCliente == null) {
    	    return null;
    	}

    	Criteria criteria = getSession().createCriteria(getPersistentClass(), "ecc");
    	criteria.createAlias("ecc.cliente", "cli");
    	criteria.add(Restrictions.eq("ecc.tpParametrizacao", tipoRelatorio));
    	criteria.add(Restrictions.eq("cli.idCliente", idCliente));
    	return (EnvioCteCliente) criteria.uniqueResult();
     }

	public boolean findBlConfAgendamentoByTpParametrizacaoEnvioCliente(Cliente cliente) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "ecc");
		criteria.createAlias("ecc.cliente", "cli");
		criteria.add(Restrictions.eq("cli.idCliente", cliente.getIdCliente()));
		criteria.add(Restrictions.eq("ecc.tpParametrizacao", "A"));
		criteria.add(Restrictions.eq("ecc.blConfirmaAgendamento", true));
		
		EnvioCteCliente envio =  (EnvioCteCliente) criteria.uniqueResult();
		if(envio != null){
			return true;
		}else{
			return false;
		}
	}    
	
	public boolean findBlRecolheICMSByTpParametrizacaoEnvioCliente(Cliente cliente) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "ecc");
		criteria.createAlias("ecc.cliente", "cli");
		criteria.add(Restrictions.eq("cli.idCliente", cliente.getIdCliente()));
		criteria.add(Restrictions.eq("ecc.tpParametrizacao", "I"));
		
		EnvioCteCliente envio =  (EnvioCteCliente) criteria.uniqueResult();
		if(envio != null){
			return true;
		}else{
			return false;
		}
	}

	public void removeByAgrupadorClienteByIdParametrizacaoEnvio(List<Long> ids) {
		for(Long id : ids){
			StringBuilder sql = new StringBuilder()
	    	.append("delete from ")
	    	.append(AgrupadorCliente.class.getName()).append(" as ac ")
	    	.append(" where ac.envioCteCliente.idEnvioCteCliente = ? ");

			List<Long> param = new ArrayList<Long>();
			param.add(id);

			super.executeHql(sql.toString(), param);
		}	
	}

	public void storeAgrupadorCliente(AgrupadorCliente agrupadorCliente) {
		getAdsmHibernateTemplate().saveOrUpdate(agrupadorCliente);
	}

	public List<Map<String, Object>> findAgrupamentoClienteByIdParametrizacaoEnvio(Long idParametrizacaoEnvio){
		StringBuilder hql = new StringBuilder();
		hql.append(" select new Map(p.nrIdentificacao as nrIdentificacao) ")
		.append(" from ")
		.append(" com.mercurio.lms.vendas.model.AgrupadorCliente ac ")
		.append(" inner join ac.envioCteCliente ecc ")
		.append(" inner join ac.cliente c ")
		.append(" inner join c.pessoa p ")
		.append(" where ")
		.append(" ecc.idEnvioCteCliente = :idParametrizacaoEnvio");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idParametrizacaoEnvio", idParametrizacaoEnvio);

		return getAdsmHibernateTemplate().find(hql.toString(), parameters);
	}

	public boolean existeTabelaCliente(Long tpOcorrencia, String idCliente) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT ETC.ID_EDI_TABELA_OCOREN as idTabela ")
			.append(" FROM LMS_PD.EDI_TABELA_CLIENTE ETC, ")
			.append(" LMS_PD.PESSOA PES ")
			.append(" WHERE EXISTS (SELECT 1 FROM LMS_PD.EDI_TABELA_OCOREN_DET ETOD, ")
			.append(" LMS_PD.EDI_TABELA_OCOREN ETO ")
			.append(" WHERE ETOD.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN ")
			.append(" AND ETO.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN ")
			.append(" AND ETO.BL_WEBSERVICE = 'S' ")
			.append(" AND ETOD.TP_OCORRENCIA = :tpOcorrencia ) ")
			.append(" AND ETC.ID_CLIENTE = PES.ID_PESSOA ")
			.append(" AND PES.NR_IDENTIFICACAO  = :idCliente ");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("tpOcorrencia", tpOcorrencia);
		parameters.put("idCliente", idCliente);

		List<Object[]> hasTabela = getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("idTabela", Hibernate.LONG);
					}
				}
		);

		return !hasTabela.isEmpty();
	}

	public List<Object[]> findCliente(Long tpOcorrencia, String idCliente, Object idLms) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ETOD.CD_WS_BDE as cdWsBde ")
				.append(" FROM EDI_TABELA_CLIENTE ETC, ")
				.append(" EDI_TABELA_OCOREN_DET ETOD, ")
				.append(" LMS_PD.PESSOA PES, ")
				.append(" EDI_TABELA_OCOREN ETO ")
				.append(" WHERE ETOD.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN ")
				.append(" AND ETO.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN ")
				.append(" AND ETO.BL_WEBSERVICE = 'S' ")
				.append(" AND ETOD.TP_OCORRENCIA = :tpOcorrencia ")
				.append(" AND ETC.ID_CLIENTE = PES.ID_PESSOA ")
				.append(" AND PES.NR_IDENTIFICACAO  = :idCliente ")
				.append(" AND ETOD.ID_LMS = :idLms ");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("tpOcorrencia", tpOcorrencia);
		parameters.put("idCliente", idCliente);
		parameters.put("idLms", idLms);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("cdWsBde", Hibernate.STRING);
					}
				}
		);
	}

	public List<Object[]> findTabelaGenerica(Long tpOcorrencia, Object idLms) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ETOD.CD_WS_BDE as cdWsBde")
				.append(" FROM EDI_PD.EDI_TABELA_OCOREN ETO, ")
				.append(" EDI_PD.EDI_TABELA_OCOREN_DET ETOD ")
				.append(" WHERE ETO.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN ")
				.append(" AND ETO.NM_TABELA_OCOREN     = 'BDE_CTE' ")
				.append(" AND ETOD.TP_OCORRENCIA       = :tpOcorrencia ")
				.append(" AND ETOD.ID_LMS     = :idLms ");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("tpOcorrencia", tpOcorrencia);
		parameters.put("idLms", idLms);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("cdWsBde", Hibernate.STRING);
					}
				}
		);
	}

	public String existeConhecimentoCliente(String cliente, Long idParcela) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ETO.NM_TABELA_OCOREN ")
				.append(" FROM EDI_PD.EDI_TABELA_OCOREN ETO, ")
				.append(" EDI_PD.EDI_TABELA_CLIENTE ETC, ")
				.append(" EDI_PD.EDI_TABELA_OCOREN_DET ETOD ")
				.append(" WHERE ETO.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN ")
				.append(" AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN ")
				.append(" AND ETOD.TP_OCORRENCIA       = ? ")
				.append(" AND ETC.ID_CLIENTE  = ? ")
				.append(" AND ETOD.ID_LMS = ? ");

		List<String> param = new ArrayList<>();
		param.add(cliente);
		param.add(idParcela.toString());

		return getAdsmHibernateTemplate().find(hql.toString()).get(0).toString();
	}

	public List<Object[]> findValorDominio(Object valorDominio) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT VD.ID_VALOR_DOMINIO idValor")
			.append(" FROM VALOR_DOMINIO VD, ")
			.append(" DOMINIO D ")
			.append(" WHERE VD.ID_DOMINIO = D.ID_DOMINIO " )
			.append(" AND D.NM_DOMINIO = 'DM_TIPO_CONHECIMENTO' " )
			.append(" AND VD.VL_VALOR_DOMINIO = :valorDominio " );

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("valorDominio", valorDominio);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("idValor", Hibernate.LONG);
					}
				}
		);
	}

	public List<Object[]> findTpSituacaoDocumento(Long idDoctoServico) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT TP_SITUACAO_DOCUMENTO as tpSituacao")
				.append(" FROM MONITORAMENTO_DOC_ELETRONICO ")
				.append(" WHERE ID_DOCTO_SERVICO = :idDoctoServico ");
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idDoctoServico", idDoctoServico);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("tpSituacao", Hibernate.STRING);
					}}
				);
	}


	public List<Object[]> findConhecimentoByidDoctoServico (Long idDoctoServico) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ID_PARCELA_PRECO as idParcelaPreco")
				.append(" FROM PARCELA_DOCTO_SERVICO ")
				.append(" WHERE ID_DOCTO_SERVICO = :idDoctoServico ");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idDoctoServico", idDoctoServico);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("idParcelaPreco", Hibernate.LONG);
					}
				}
		);
	}

	public List<Object[]> findIdLms (Long idDoctoServico) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT ID_PARCELA_PRECO AS idParcelaPreco, VL_PARCELA as vlParcela ")
				.append(" FROM PARCELA_DOCTO_SERVICO ")
				.append(" WHERE ID_DOCTO_SERVICO = :idDoctoServico" );

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idDoctoServico", idDoctoServico);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("idParcelaPreco", Hibernate.LONG);
						sql.addScalar("vlParcela", Hibernate.BIG_DECIMAL);
					}
				}
		);
	}

	public List<Object[]> findTpConhecimento (Long idDoctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT TP_CONHECIMENTO as tpConhecimento ")
				.append(" FROM CONHECIMENTO ")
				.append(" WHERE ID_CONHECIMENTO = :idDoctoServico" );

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idDoctoServico", idDoctoServico);

		return getAdsmHibernateTemplate().findBySql(hql.toString(), parameters,
				new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sql) {
						sql.addScalar("tpConhecimento", Hibernate.STRING);
					}
				}
		);
	}
}