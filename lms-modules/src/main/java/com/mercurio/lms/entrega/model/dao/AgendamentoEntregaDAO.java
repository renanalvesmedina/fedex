package com.mercurio.lms.entrega.model.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AgendamentoEntregaDAO extends BaseCrudDao<AgendamentoEntrega, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AgendamentoEntrega.class;
    }    
    
	public AgendamentoEntrega findById(Long id) {
		
		AgendamentoEntrega agendamentoEntrega = (AgendamentoEntrega)getAdsmHibernateTemplate().get(AgendamentoEntrega.class, id);
		if (agendamentoEntrega.getTurno() != null) {
			Hibernate.initialize(agendamentoEntrega.getTurno().getFilial().getPessoa());
		}
		
		Hibernate.initialize(agendamentoEntrega.getFilial().getPessoa());
		
		if (agendamentoEntrega.getFilial().getTurnos() != null) {
			Hibernate.initialize(agendamentoEntrega.getFilial().getTurnos());
		}

		Hibernate.initialize(agendamentoEntrega.getUsuarioByIdUsuarioCriacao());

		if (agendamentoEntrega.getMotivoAgendamentoByIdMotivoCancelamento() != null) {
			Hibernate.initialize(agendamentoEntrega.getMotivoAgendamentoByIdMotivoCancelamento());			
		}
		
		if (agendamentoEntrega.getMotivoAgendamentoByIdMotivoReagendamento() != null) {
			Hibernate.initialize(agendamentoEntrega.getMotivoAgendamentoByIdMotivoReagendamento());			
		}
		
		if (agendamentoEntrega.getReagendamento() != null) {
			Hibernate.initialize(agendamentoEntrega.getReagendamento());
		}
		
		if (agendamentoEntrega.getAgendamentoDoctoServicos() != null) {
			for (Iterator iter = agendamentoEntrega.getAgendamentoDoctoServicos().iterator(); iter.hasNext();) {
				AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico) iter.next();
				agendamentoDoctoServico.getIdAgendamentoDoctoServico();

				AgendamentoEntrega agendamentoEntregaAgendamentoDoctoServico = agendamentoDoctoServico.getAgendamentoEntrega();
				Hibernate.initialize(agendamentoEntregaAgendamentoDoctoServico.getFilial());
				
				Hibernate.initialize(agendamentoDoctoServico.getDoctoServico());
				DoctoServico doctoServico = agendamentoDoctoServico.getDoctoServico(); 

				if (doctoServico.getServico() != null) {
					Hibernate.initialize(doctoServico.getServico().getTipoServico());
				}
				
				if (doctoServico.getFilialDestinoOperacional() != null) {
					Hibernate.initialize(doctoServico.getFilialDestinoOperacional().getPessoa());
				}

				if (doctoServico.getFilialByIdFilialOrigem() != null) {
					Hibernate.initialize(doctoServico.getFilialByIdFilialOrigem().getPessoa());
				}
				
				if (doctoServico.getClienteByIdClienteDestinatario() != null) {
					Hibernate.initialize(doctoServico.getClienteByIdClienteDestinatario().getPessoa());
				}

				if (doctoServico.getClienteByIdClienteRemetente() != null) {
					Hibernate.initialize(doctoServico.getClienteByIdClienteRemetente().getPessoa());
				}
				
				if (doctoServico.getLocalizacaoMercadoria() != null) {
					Hibernate.initialize(doctoServico.getLocalizacaoMercadoria());
				}
				
				if (doctoServico.getManifestoEntregaDocumentos() != null) {
					for (Iterator iterator = doctoServico.getManifestoEntregaDocumentos().iterator(); iterator.hasNext();) {
						ManifestoEntregaDocumento manifestoEntregaDocumento = (ManifestoEntregaDocumento) iterator.next();
						Hibernate.initialize(manifestoEntregaDocumento.getOcorrenciaEntrega());
						Hibernate.initialize(manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem());
					}
				}
				
			}
		}				
		
		return agendamentoEntrega;
	}

	/**
	 * Busca um Agendamento de Entrega aberto para o documento de serviço informado.
	 * 
	 * @param idDoctoServico id de documento de serviço
	 * @return instância de AgendamentoDoctoServico
	 */
	public AgendamentoEntrega findAgendamentoAberto(Long idDoctoServico) {
		String sql = new StringBuilder()
			.append("SELECT AE ")
			.append("FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS ")
			.append("INNER JOIN ADS.agendamentoEntrega AE ")
			.append("WHERE NOT EXISTS ( ")
			.append("    SELECT ADS2.id ")
			.append("    FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS2 ")
			.append("    WHERE ADS2.agendamentoEntrega.id = ADS.agendamentoEntrega.id ")
			.append("      AND ADS2.id <> ADS.id ")
			.append("      AND NOT EXISTS ( ")
			.append("        SELECT DS3.id ")
			.append("        FROM ")
			.append(		 DoctoServico.class.getName()).append(" DS3, ")
			.append(		 LocalizacaoMercadoria.class.getName()).append(" LM3 ")
			.append("        WHERE LM3.id = DS3.localizacaoMercadoria.id ")
			.append("          AND DS3.id = ADS2.doctoServico.id ")
			.append("          AND LM3.cdLocalizacaoMercadoria = ? ")
			.append("      ) ")
			.append("  ) ")
			.append("  AND AE.tpSituacaoAgendamento = ? ")
			.append("  AND ADS.doctoServico.id = ?")
			.toString();
		
		Object[] params = new Object[] {
			(short)1,
			ConstantesEntrega.TP_SITUACAO_AGENDAMENTO_ABERTO,
			idDoctoServico
		};
		
		return (AgendamentoEntrega) getAdsmHibernateTemplate().findUniqueResult(sql, params);
	}
	
	/**
	 * Busca um Agendamento de Entrega aberto para o documento de serviço informado.
	 * 
	 * @param idDoctoServico id de documento de serviço
	 * @return instância de AgendamentoEntrega
	 */
	public AgendamentoEntrega findAgendamentoAbertoDoctoServico(Long idDoctoServico) {
		String sql = new StringBuilder()
			.append("SELECT AE ")
			.append("FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS ")
			.append("INNER JOIN ADS.agendamentoEntrega AE ")
			.append("WHERE AE.tpSituacaoAgendamento = 'A' ")
			.append("  AND ADS.doctoServico.id = ?")
			.toString();
		return (AgendamentoEntrega)getAdsmHibernateTemplate().findUniqueResult(sql, new Object[]{idDoctoServico});
	}

	public AgendamentoEntrega findAgendamentoEntregaByIdDoctoServicoDhEnvio(Long idDoctoServico, DateTime dhEnvio) {
		String sql = new StringBuilder()
			.append("SELECT AE ")
			.append("FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS ")
			.append("INNER JOIN ADS.agendamentoEntrega AE ")
			.append("WHERE ADS.doctoServico.id = ? ")
			.append(" AND AE.dhEnvio.value < ? ")
			.toString();
		
		List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico, dhEnvio});
		if (!list.isEmpty()){
			return (AgendamentoEntrega) list.get(0);
		}
		return null;
	}
	
   public AgendamentoEntrega findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamento(Long idDoctoServico, String tpSituacaoAgendamento) {
        String sql = new StringBuilder()
            .append("SELECT AE ")
            .append("FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS ")
            .append("INNER JOIN ADS.agendamentoEntrega AE ")
            .append("WHERE ADS.doctoServico.id = ? ")
            .append(" AND AE.tpSituacaoAgendamento = ? ")
            .toString();
        
        List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico, tpSituacaoAgendamento});
        if (!list.isEmpty()){
            return (AgendamentoEntrega) list.get(0);
        }
        return null;
    
    }
	
	public AgendamentoEntrega findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamentoDhContato(Long idDoctoServico, String tpSituacaoAgendamento, DateTime dhContato) {
		String sql = new StringBuilder()
			.append("SELECT AE ")
			.append("FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS ")
			.append("INNER JOIN ADS.agendamentoEntrega AE ")
			.append("WHERE ADS.doctoServico.id = ? ")
			.append(" AND AE.tpSituacaoAgendamento = ? ")
			.append(" AND AE.dhContato.value > ? ")
			.toString();
		
		List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico, tpSituacaoAgendamento, dhContato});
		if (!list.isEmpty()){
			return (AgendamentoEntrega) list.get(0);
		}
		return null;
	
	}
	

	/**
     * Método que retorna uma lista de map com dados dos agendamentos de entrega relacionados ao documento de serviço com id igual ao
     * passado como parametro   
     * 
     * @param idDoctoServico
     * @return
	 * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> findListMapAgendamentoEntregaByIdDoctoServico(Long idDoctoServico) {
    	SqlTemplate sql = new SqlTemplate();
    	List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		
		sql.addProjection("AE.ID_AGENDAMENTO_ENTREGA", "idAgendamentoEntrega");
		sql.addProjection("AE.DT_AGENDAMENTO", "dtAgendamento");
		sql.addProjection("TO_CHAR(AE.DH_CONTATO,'dd/mm/yy')", "dhContato");
		sql.addProjection("TO_CHAR(AE.DH_FECHAMENTO,'dd/mm/yy')", "dhFechamento");
		sql.addProjection("AE.TP_SITUACAO_AGENDAMENTO", "tpSituacaoAgendamento");
		sql.addProjection("AE.ID_MOTIVO_REAGENDAMENTO", "idMotivoAgendamento");
    	
    	
    	sql.addFrom("AGENDAMENTO_ENTREGA", "AE");
    	sql.addFrom("AGENDAMENTO_DOCTO_SERVICO", "ADS");
    	
    	sql.addJoin("AE.ID_AGENDAMENTO_ENTREGA", "ADS.ID_AGENDAMENTO_ENTREGA");
    	sql.addCriteria("ADS.ID_DOCTO_SERVICO", "=", idDoctoServico);

		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idAgendamentoEntrega",Hibernate.LONG);
				sqlQuery.addScalar("dtAgendamento",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dhContato",Hibernate.STRING);
				sqlQuery.addScalar("dhFechamento",Hibernate.STRING);
				
				Properties propertiesTpSituacaoAgendamento = new Properties();
				propertiesTpSituacaoAgendamento.put("domainName","DM_SITUACAO_AGENDA");
				sqlQuery.addScalar("tpSituacaoAgendamento",Hibernate.custom(DomainCompositeUserType.class,propertiesTpSituacaoAgendamento));
				sqlQuery.addScalar("idMotivoAgendamento",Hibernate.LONG);
				
			}
		};
		
		List<Object[]> listRetorno = getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1),Integer.valueOf(10000),sql.getCriteria(),csq).getList();
		//criado para fazer o parse da data que vem como string do banco para o date para dai então construir o yearmonthday evitando aplicação de fuso horario e timezone na conversão
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		if(listRetorno != null) {
			for(Object[] coluna : listRetorno) {
				Map<String, Object> mapaColuna = new HashMap<String, Object>();
				mapaColuna.put("idAgendamentoEntrega", coluna[0]);
				mapaColuna.put("dtAgendamento", coluna[1]);
				
				YearMonthDay dhContato = null;
				YearMonthDay dhFechamento = null;
				if(coluna[2] != null) {
					try {
						dhContato = new YearMonthDay(sdf.parse((String) coluna[2]));
					} catch (ParseException e) {
						log.error(e);
						throw new RuntimeException("Erro ao converter data " + dhContato);
					}
				}
				if(coluna[3] != null) {
					try {
						dhFechamento = new YearMonthDay(sdf.parse((String) coluna[3]));
					} catch (ParseException e) {
						log.error(e);
						throw new RuntimeException("Erro ao converter data " + dhFechamento);
					}
				}				
				
				mapaColuna.put("dhContato", dhContato);
				mapaColuna.put("dhFechamento", dhFechamento);				
				mapaColuna.put("tpSituacaoAgendamento", coluna[4]);
				mapaColuna.put("idMotivoAgendamento", coluna[5]);
				retorno.add(mapaColuna);
			}
		}
		return retorno;
    }
    
    public void excluiAnexos(Long idAgendamentoEntrega){
    	getJdbcTemplate().execute(" delete from agendamento_anexo where id_agendamento_entrega = " + idAgendamentoEntrega);
    }

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Pessoa findClienteDestinatarioByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		StringBuilder sql = new StringBuilder();
		sql.append(" select p ")
		.append("   FROM ")
		.append(AgendamentoMonitCCT.class.getName()).append(" agend_monit ")
		.append(" join agend_monit.monitoramentoCCT.clienteDestinatario.pessoa p ")
		.append(" where agend_monit.agendamentoEntrega.id = ? ");

		List lista = getAdsmHibernateTemplate().find(sql.toString(), idAgendamentoEntrega);
		
		if(lista != null && !lista.isEmpty()){
			return (Pessoa) lista.get(0);
		}
		return null;
	}
	
	public Pessoa findClienteRemetenteByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		StringBuilder sql = new StringBuilder();
		sql.append(" select p ")
		.append("   FROM ")
		.append(AgendamentoMonitCCT.class.getName()).append(" agend_monit ")
		.append(" join agend_monit.monitoramentoCCT.clienteRemetente.pessoa p ")
		.append(" where agend_monit.agendamentoEntrega.id = ? ");
		
		List lista = getAdsmHibernateTemplate().find(sql.toString(), idAgendamentoEntrega);
		
		if(lista != null  && !lista.isEmpty()){
			return (Pessoa) lista.get(0);
		}
		return null;
	}
	
	public Cliente findClienteRemetByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		StringBuilder sql = new StringBuilder();
		sql.append(" select cliente ")
		.append("   FROM ")
		.append(AgendamentoMonitCCT.class.getName()).append(" agend_monit ")
		.append(" join agend_monit.monitoramentoCCT.clienteRemetente cliente ")
		.append(" where agend_monit.agendamentoEntrega.id = ? ");
		
		List lista = getAdsmHibernateTemplate().find(sql.toString(), idAgendamentoEntrega);
		
		if(lista != null  && !lista.isEmpty()){
			return (Cliente) lista.get(0);
		}
		return null;
	}
	
	public Cliente findClienteDestByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		StringBuilder sql = new StringBuilder();
		sql.append(" select cliente ")
		.append("   FROM ")
		.append(AgendamentoMonitCCT.class.getName()).append(" agend_monit ")
		.append(" join agend_monit.monitoramentoCCT.clienteDestinatario cliente ")
		.append(" where agend_monit.agendamentoEntrega.id = ? ");

		List lista = getAdsmHibernateTemplate().find(sql.toString(), idAgendamentoEntrega);
		
		if(lista != null && !lista.isEmpty()){
			return (Cliente) lista.get(0);
		}
		return null;
	}
	
	public List<String> findNotasMonitoramentoCCTByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		StringBuilder sql = new StringBuilder();
		sql.append(" select m_cct.nrChave ")
		.append("   FROM ")
		.append(AgendamentoEntrega.class.getName()).append(" ae, ")
		.append(AgendamentoMonitCCT.class.getName()).append(" a_m_cct, ")
		.append(MonitoramentoCCT.class.getName()).append(" m_cct ")
		.append(" where ae.idAgendamentoEntrega =  a_m_cct.agendamentoEntrega.idAgendamentoEntrega ")
		.append(" and a_m_cct.monitoramentoCCT.idMonitoramentoCCT = m_cct.idMonitoramentoCCT ")
		.append(" and ae.idAgendamentoEntrega= ? ");

		List<String> lista = getAdsmHibernateTemplate().find(sql.toString(), idAgendamentoEntrega);
		return lista;
	}

	public Map findDadosGeraisRelatorioAgendamento(Long idAgendamentoEntrega) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(p_rem.nmPessoa 	 		as ds_remetente, " +
								 " p_dest.nmPessoa   		as ds_destinatario, " +
								 " p_rem.nrIdentificacao    as nr_cnpj_remetente, "+
								 " p_dest.nrIdentificacao   as nr_cnpj_destinatario, " +
								 " ae.dtAgendamento 	  	as dt_entrega, "+ 
								 " ae.dhContato				as dt_inclu_agend, " +
								 " ae.hrPreferenciaInicial  as hr_entrega )" )
				.append("   FROM ")
				.append(AgendamentoEntrega.class.getName()).append(" ae, ")
				.append(AgendamentoMonitCCT.class.getName()).append(" a_m_cct, ")
				.append(MonitoramentoCCT.class.getName()).append(" m_cct, ")
				.append(Pessoa.class.getName()).append(" p_rem, ")
				.append(Pessoa.class.getName()).append(" p_dest ")
				.append(" where ae.idAgendamentoEntrega =  a_m_cct.agendamentoEntrega.idAgendamentoEntrega ")
				.append(" and a_m_cct.monitoramentoCCT.idMonitoramentoCCT = m_cct.idMonitoramentoCCT ")
				.append(" and m_cct.clienteRemetente.idCliente = p_rem.idPessoa ")
				.append(" and m_cct.clienteDestinatario.idCliente = p_dest.idPessoa ")
				.append(" and ae.idAgendamentoEntrega= ? ");

		List lista =  getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idAgendamentoEntrega });
		
		if(lista != null && !lista.isEmpty()){
			HashMap<String, Object> resultado = (HashMap<String, Object>) lista.get(0);
			return resultado;
		}else{
			return null;
		}
		
	}

}
