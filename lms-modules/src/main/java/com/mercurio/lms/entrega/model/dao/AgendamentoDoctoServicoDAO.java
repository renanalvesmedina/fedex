package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AgendamentoDoctoServicoDAO extends BaseCrudDao<AgendamentoDoctoServico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AgendamentoDoctoServico.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idDoctoServico
     * @return <List of AgendamentoDoctoServico>
     */
	public List findAgendamentosNaoAbertos(Long idDoctoServico) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("agendamentoEntrega", "ae");
		dc.createAlias("doctoServico", "ds");
		dc.add(Restrictions.eq("ds.id", idDoctoServico));
		dc.add(Restrictions.ne("ae.tpSituacaoAgendamento", "A"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

    /**
     * Método que retorna um List de AgendamentoDoctoServico usando como filtro o ID do DoctoServico
     * @param idDoctoServico
     * @return List
     */
    public List findAgendamentoByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads join fetch ads.agendamentoEntrega ae ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    /**
     * Método que retorna um List de AgendamentoDoctoServico Ativos e Abertos do DoctoServico
     * @author André Valadas
     * 
     * @param idDoctoServico
     * @return
     */
    public List<AgendamentoDoctoServico> findAgendamentosAtivos(final Long idDoctoServico) {
		final SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads join fetch ads.agendamentoEntrega ae ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		hql.addCriteria("ads.tpSituacao", "=", ConstantesVendas.SITUACAO_ATIVO);//Ativos
		hql.addCriteria("ae.tpSituacaoAgendamento", "=", ConstantesVendas.SITUACAO_ATIVO);//Abertos
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    /**
     * Método que retorna um AgendamentoDoctoServico usando como filtro ID do DoctoServico e tpSituacao
     * @param idDoctoServico
     * @param tpSituacao
     * @return AgendamentoDoctoServico
     */
    public AgendamentoDoctoServico findAgendamentoByIdDoctoServico(Long idDoctoServico, String tpSituacao) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads join fetch ads.agendamentoEntrega ae ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		hql.addCriteria("ads.agendamentoEntrega.tpSituacaoAgendamento", "=", tpSituacao);
		
		return (AgendamentoDoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }
    
    public AgendamentoDoctoServico findAgendamentoByIdDoctoServicoTpSituacao(Long idDoctoServico, String tpSituacao) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads join fetch ads.agendamentoEntrega ae ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		hql.addCriteria("ads.agendamentoEntrega.tpSituacaoAgendamento", "=", tpSituacao);
		
		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()){
			return (AgendamentoDoctoServico) list.get(0);
		}
		return null;
    }
    
    @SuppressWarnings("unchecked")
	public List<AgendamentoDoctoServico> findAgendamentoByIdDoctoServicoAndTpSituacao(Long idDoctoServico, String... tpSituacoes) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads join fetch ads.agendamentoEntrega ae ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		hql.addCriteriaIn("ads.agendamentoEntrega.tpSituacaoAgendamento", tpSituacoes);
		hql.addCustomCriteria("ads.agendamentoEntrega.dhFechamento IS NOT NULL");
		hql.addCustomCriteria("ads.agendamentoEntrega.dhContato IS NOT NULL");
		
		return (List<AgendamentoDoctoServico>) getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    /**
     * Método que retorna um AgendamentoDoctoServico usando como filtro ID do DoctoServico e tpSituacao
     * @param idDoctoServico
     * @param tpSituacao
     * @return AgendamentoDoctoServico
     */
    public AgendamentoDoctoServico findAgendamentoByIdDoctoServicoAndTipoAgendamento(Long idDoctoServico, String tpAgendamento) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		hql.addCriteria("ads.agendamentoEntrega.tpAgendamento", "=", tpAgendamento);
		
		return (AgendamentoDoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }
    
    private StringBuffer getSelect() {
		StringBuffer hql = new StringBuffer();
		
		hql.append("SELECT FILIAL_AGENDAMENTO.SG_FILIAL 		AS sgFilial,");
		hql.append("	   PESSOA_FILIAL_AGENDAMENTO.NM_FANTASIA 	AS nmFantasia,");
		hql.append("	   TISE.DS_TIPO_SERVICO_I 			AS dsTipoServico,");
		hql.append("	   FILIAL_ORIGEM.SG_FILIAL 			AS sgFilialOrigem,");
		hql.append("	   DOSE.NR_DOCTO_SERVICO 			AS nrDoctoServico,");
		hql.append("	   DOSE.TP_DOCUMENTO_SERVICO 			AS tpDocumentoServico,");
		hql.append("	   FILIAL_DESTINO.SG_FILIAL 			AS sgFilialDestino,");
		hql.append(" 	   PESSOA_FILIAL_DESTINO.NM_FANTASIA 		AS nmFantasiaDestino,");
		hql.append("	   REMETENTE.NR_IDENTIFICACAO 			AS nrIdentificacaoRemetente,");
		hql.append("	   REMETENTE.TP_IDENTIFICACAO 			AS tpIdentificacaoRemetente,");
		hql.append("	   REMETENTE.NM_PESSOA 				AS remetente,");
		hql.append("	   DESTINATARIO.NR_IDENTIFICACAO 		AS nrIdentificacaoDestinatario,");
		hql.append("	   DESTINATARIO.TP_IDENTIFICACAO 		AS tpIdentificacaoDestinatario,");
		hql.append("	   DESTINATARIO.NM_PESSOA 			AS destinatario,");
		hql.append("	   AGEN.TP_AGENDAMENTO 				AS tpAgendamento,");
		hql.append("	   AGEN.DT_AGENDAMENTO 				AS dtAgendamento,");
		hql.append("	   AGEN.HR_PREFERENCIA_INICIAL 			AS horarioInicial,");
		hql.append("	   AGEN.HR_PREFERENCIA_FINAL 			AS horarioFinal,");
		hql.append("	   AGEN.TP_SITUACAO_AGENDAMENTO 		AS tpSituacaoAgendamento,");
		hql.append("	   TURNO.DS_TURNO 				AS dsTurno,");
		hql.append("	   USUARIO_CRIACAO.NM_USUARIO 			AS agendadoPor,");
		hql.append("	   (SELECT MIN(NOFC.NR_NOTA_FISCAL) 		AS nrNotaFiscal ");
		hql.append(" 			   FROM NOTA_FISCAL_CONHECIMENTO NOFC ");
		hql.append(" 			   WHERE NOFC.ID_CONHECIMENTO = DOSE.ID_DOCTO_SERVICO ) AS nrNotaFiscal, ");
		hql.append("	   AGEN.ID_AGENDAMENTO_ENTREGA 			AS idAgendamentoEntrega, ");
	   	hql.append("	   AGEN.DH_CONTATO 				AS dhContato, ");
	   	hql.append("	   '' 			        		AS nrChave ");
		
		return hql;		
    }
    
    private StringBuffer getSelectForNFE() {
		StringBuffer hql = new StringBuffer();
		
		hql.append("SELECT FILIAL_AGENDAMENTO.SG_FILIAL 	  AS sgFilial, ");
		hql.append("	   PESSOA_FILIAL_AGENDAMENTO.NM_FANTASIA  AS nmFantasia, ");
		hql.append("	   '' 					  AS dsTipoServico, ");
		hql.append("	   FILIAL_ORIGEM.SG_FILIAL 		  AS sgFilialOrigem, ");
		hql.append("	   '' 					  AS nrDoctoServico, ");
		hql.append("	   '' 					  AS tpDocumentoServico, ");
		hql.append("	   FILIAL_DESTINO.SG_FILIAL 		  AS sgFilialDestino, ");
		hql.append(" 	   PESSOA_FILIAL_DESTINO.NM_FANTASIA 	  AS nmFantasiaDestino, ");
		hql.append("	   REMETENTE.NR_IDENTIFICACAO 		  AS nrIdentificacaoRemetente, ");
		hql.append("	   REMETENTE.TP_IDENTIFICACAO 		  AS tpIdentificacaoRemetente, ");
		hql.append("	   REMETENTE.NM_PESSOA 			  AS remetente, ");
		hql.append("	   DESTINATARIO.NR_IDENTIFICACAO 	  AS nrIdentificacaoDestinatario, ");
		hql.append("	   DESTINATARIO.TP_IDENTIFICACAO 	  AS tpIdentificacaoDestinatario, ");
		hql.append("	   DESTINATARIO.NM_PESSOA 		  AS destinatario, ");
		hql.append("	   AGEN.TP_AGENDAMENTO 			  AS tpAgendamento, ");
		hql.append("	   AGEN.DT_AGENDAMENTO 			  AS dtAgendamento, ");
		hql.append("	   AGEN.HR_PREFERENCIA_INICIAL 		  AS horarioInicial, ");
		hql.append("	   AGEN.HR_PREFERENCIA_FINAL 		  AS horarioFinal, ");
		hql.append("	   AGEN.TP_SITUACAO_AGENDAMENTO 	  AS tpSituacaoAgendamento, ");
		hql.append("	   TURNO.DS_TURNO 			  AS dsTurno, ");
		hql.append("	   USUARIO_CRIACAO.NM_USUARIO 		  AS agendadoPor, ");
		hql.append("	   '' 					  AS nrNotaFiscal, ");
		hql.append("	   AGEN.ID_AGENDAMENTO_ENTREGA 		  AS idAgendamentoEntrega, ");
	   	hql.append("	   AGEN.DH_CONTATO 			  AS dhContato, ");
	   	hql.append("	   MONI.NR_CHAVE 			  AS nrChave ");
		
		return hql;		
    }
    
    private StringBuffer getFrom(){
		StringBuffer hql = new StringBuffer();
		
		hql.append(" FROM AGENDAMENTO_ENTREGA		AGEN, ");
		hql.append("	  AGENDAMENTO_DOCTO_SERVICO	AGDS, ");
		hql.append("	  DOCTO_SERVICO 		DOSE, ");
		hql.append("	  FILIAL 			FILIAL_AGENDAMENTO, ");
		hql.append("	  FILIAL 			FILIAL_ORIGEM, ");
		hql.append("	  TURNO 			TURNO, ");
		hql.append("	  USUARIO 			USUARIO_CRIACAO, ");
		hql.append("	  FILIAL 			FILIAL_DESTINO, ");
		hql.append("	  CLIENTE 			CLRE, ");
		hql.append("	  CLIENTE 			CLDE, ");
		hql.append("	  PESSOA 			REMETENTE, ");
		hql.append("	  PESSOA 			DESTINATARIO, ");
		hql.append("	  PESSOA 			PESSOA_FILIAL_AGENDAMENTO, ");
		hql.append("	  PESSOA 			PESSOA_FILIAL_DESTINO, ");
		hql.append("	  SERVICO 			SERVICO, ");
		hql.append("	  TIPO_SERVICO 			TISE "); 
		
    	return hql; 
    }
    
    private StringBuffer getFromForNFE(){
		StringBuffer hql = new StringBuffer();
		
		hql.append(" FROM AGENDAMENTO_ENTREGA	AGEN, ");
		hql.append(" 	  AGENDAMENTO_MONIT_CCT AMON, ");
		hql.append(" 	  MONITORAMENTO_CCT 	MONI, ");
		hql.append(" 	  FILIAL 		FILIAL_AGENDAMENTO, ");
		hql.append(" 	  FILIAL 		FILIAL_ORIGEM, ");
		hql.append(" 	  TURNO 		TURNO, ");
		hql.append(" 	  USUARIO 		USUARIO_CRIACAO, ");
		hql.append(" 	  FILIAL 		FILIAL_DESTINO, ");
		hql.append(" 	  CLIENTE 		CLRE, ");
		hql.append(" 	  CLIENTE 		CLDE, ");
		hql.append(" 	  PESSOA 		REMETENTE, ");
		hql.append("	  PESSOA 		DESTINATARIO, ");
		hql.append("	  PESSOA 		PESSOA_FILIAL_AGENDAMENTO, ");
		hql.append("	  PESSOA 		PESSOA_FILIAL_DESTINO ");

		return hql; 
    }
    
    //AGENDAMENTO_MONIT_CCT-->MONITORAMENTO_CCT.NR_CHAVE
    private StringBuffer getWhere(TypedFlatMap criteria, Map parameters) {
    	
		StringBuffer hql = new StringBuffer();
		
		hql.append("WHERE AGDS.ID_AGENDAMENTO_ENTREGA = AGEN.ID_AGENDAMENTO_ENTREGA");
		hql.append("  AND AGDS.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO");
		hql.append("  AND AGEN.ID_FILIAL = FILIAL_AGENDAMENTO.ID_FILIAL");
		hql.append("  AND AGEN.ID_TURNO = TURNO.ID_TURNO(+)");
		hql.append("  AND AGEN.ID_USUARIO_CRIACAO = USUARIO_CRIACAO.ID_USUARIO");
		hql.append("  AND PESSOA_FILIAL_AGENDAMENTO.ID_PESSOA = FILIAL_AGENDAMENTO.ID_FILIAL");
		hql.append("  AND FILIAL_ORIGEM.ID_FILIAL = DOSE.ID_FILIAL_ORIGEM");
		hql.append("  AND FILIAL_DESTINO.ID_FILIAL(+) = DOSE.ID_FILIAL_DESTINO_OPERACIONAL");
		hql.append("  AND PESSOA_FILIAL_DESTINO.ID_PESSOA(+) = FILIAL_DESTINO.ID_FILIAL");
		hql.append("  AND CLRE.ID_CLIENTE(+) = DOSE.ID_CLIENTE_REMETENTE");
		hql.append("  AND REMETENTE.ID_PESSOA(+) = CLRE.ID_CLIENTE");
		hql.append("  AND CLDE.ID_CLIENTE(+) = DOSE.ID_CLIENTE_DESTINATARIO");
		hql.append("  AND DESTINATARIO.ID_PESSOA(+) = CLDE.ID_CLIENTE");
		hql.append("  AND SERVICO.ID_SERVICO(+) = DOSE.ID_SERVICO");
		hql.append("  AND TISE.ID_TIPO_SERVICO(+) = SERVICO.ID_TIPO_SERVICO ");

		if ( criteria.getLong("filialOrigem.idFilial") != null ) {
			
			hql.append("AND FILIAL_ORIGEM.ID_FILIAL = :filialOrigem.idFilial ");
			parameters.put("filialOrigem.idFilial", criteria.get("filialOrigem.idFilial"));
			
		}

		if ( criteria.getLong("filialDestino.idFilial") != null ) {
			
			hql.append("AND FILIAL_DESTINO.ID_FILIAL = :filialDestino.idFilial ");
			parameters.put("filialDestino.idFilial", criteria.get("filialDestino.idFilial"));
			
		}
		
		
		if ( criteria.getLong("destinatario.idCliente") != null ) {
			
			hql.append("AND CLDE.ID_CLIENTE = :destinatario.idCliente ");
			parameters.put("destinatario.idCliente", criteria.get("destinatario.idCliente"));
			
		}
		
		if ( criteria.getLong("remetente.idCliente") != null) {
			
			hql.append("AND CLRE.ID_CLIENTE = :remetente.idCliente ");
			parameters.put("remetente.idCliente", criteria.get("remetente.idCliente"));
			
		}
		
		if ( criteria.getLong("idTipoServico") != null ) {
			
			hql.append("AND SERVICO.ID_TIPO_SERVICO = :idTipoServico ");
			parameters.put("idTipoServico", criteria.get("idTipoServico"));
			
		}
		
		if ( criteria.getLong("filial.idFilial") != null ) {
			
			hql.append("AND FILIAL_AGENDAMENTO.ID_FILIAL = :filial.idFilial ");
			parameters.put("filial.idFilial", criteria.get("filial.idFilial"));
			
		}
		
		if ( criteria.getYearMonthDay("periodoAgendamentoInicial") != null && criteria.getYearMonthDay("periodoAgendamentoFinal") != null ) {
			
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) BETWEEN TRUNC(:periodoAgendamentoInicial) AND TRUNC(:periodoAgendamentoFinal) ");
			parameters.put("periodoAgendamentoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoInicial")));
			parameters.put("periodoAgendamentoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoFinal")));
			
		} else if ( criteria.getYearMonthDay("periodoAgendamentoInicial") != null ) {
			
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) >= TRUNC(:periodoAgendamentoInicial) ");
			parameters.put("periodoAgendamentoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoInicial")) );
			
		} else if ( criteria.getYearMonthDay("periodoAgendamentoFinal") != null ) {
			
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) = TRUNC(:periodoAgendamentoFinal) ");
			parameters.put("periodoAgendamentoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoFinal")) );
			
		}
									   
		if ( criteria.getYearMonthDay("periodoContatoInicial") != null && criteria.getYearMonthDay("periodoContatoFinal") != null ) {
			
			hql.append("AND TRUNC(AGEN.DH_CONTATO) BETWEEN TRUNC(:periodoContatoInicial) AND TRUNC(:periodoContatoFinal) ");
			parameters.put("periodoContatoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoInicial")));
			parameters.put("periodoContatoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoFinal")));
			
		} else if ( criteria.getYearMonthDay("periodoContatoInicial") != null ) {
			
			hql.append("AND TRUNC(AGEN.DH_CONTATO) >= TRUNC(:periodoContatoInicial) ");
			parameters.put("periodoContatoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoInicial")) );
			
		} else if ( criteria.getYearMonthDay("periodoContatoFinal") != null ) {
			
			hql.append("AND TRUNC(AGEN.DH_CONTATO) = TRUNC(:periodoContatoFinal) ");			
			parameters.put("periodoContatoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoFinal")) );
			
		}		
		
		if ( !criteria.getString("tpSituacaoAgendamento").trim().equals("") ) {
			
			hql.append("AND AGEN.TP_SITUACAO_AGENDAMENTO = :tpSituacaoAgendamento ");
			parameters.put("tpSituacaoAgendamento", criteria.get("tpSituacaoAgendamento"));
			
		}

		if ( !criteria.getString("blCartao").trim().equals("")) {
			
			hql.append("AND AGEN.BL_CARTAO = :blCartao ");
			parameters.put("blCartao", criteria.get("blCartao"));
			
		}

		if ( !criteria.getString("turno").trim().equals("") ) {
			hql.append("AND AGEN.ID_TURNO = :turno ");
			parameters.put("turno", criteria.get("turno"));
		}

		if ( !criteria.getString("tpAgendamento").trim().equals("") ) {
			hql.append("AND AGEN.TP_AGENDAMENTO = :tpAgendamento ");
			parameters.put("tpAgendamento", criteria.get("tpAgendamento"));
		}
		
		if ( criteria.getString("doctoServico.tpDocumentoServico") != null && !criteria.getString("doctoServico.tpDocumentoServico").equals("")) {
			hql.append("AND DOSE.TP_DOCUMENTO_SERVICO = :doctoServico.tpDocumentoServico ");
			parameters.put("doctoServico.tpDocumentoServico", criteria.getString("doctoServico.tpDocumentoServico"));
		} else {
			hql.append("AND DOSE.TP_DOCUMENTO_SERVICO IN ('CRT', 'CTR', 'MDA', 'NFT')");
		}

		if ( criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null ) {
			hql.append("AND DOSE.ID_FILIAL_ORIGEM = :doctoServico.filialByIdFilialOrigem.idFilial ");
			parameters.put("doctoServico.filialByIdFilialOrigem.idFilial", criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
		}

		if (criteria.getLong("idDoctoServico") != null) {
			Long idDoctoServico = null;
			idDoctoServico = criteria.getLong("idDoctoServico");
			
			hql.append("AND DOSE.ID_DOCTO_SERVICO = :doctoServico.idDoctoServico ");
			parameters.put("doctoServico.idDoctoServico", idDoctoServico);
		}		

		if ( criteria.getLong("usuario.idUsuario") != null ) {
			hql.append("AND AGEN.ID_USUARIO_CRIACAO = :usuario.idUsuario ");
			parameters.put("usuario.idUsuario", criteria.getLong("usuario.idUsuario"));
		}		
		
    	return hql;
    }
    
    @SuppressWarnings("unchecked")
	private StringBuffer getWhereForNFE(TypedFlatMap criteria, Map parameters) {
		StringBuffer hql = new StringBuffer();
		hql.append(" WHERE AMON.ID_AGENDAMENTO_ENTREGA = AGEN.ID_AGENDAMENTO_ENTREGA ");
		hql.append("   AND AMON.ID_MONITORAMENTO_CCT = MONI.ID_MONITORAMENTO_CCT"); 
		hql.append("   AND AGEN.ID_FILIAL = FILIAL_AGENDAMENTO.ID_FILIAL "); 
		hql.append("   AND AGEN.ID_TURNO = TURNO.ID_TURNO(+) "); 
		hql.append("   AND AGEN.ID_USUARIO_CRIACAO = USUARIO_CRIACAO.ID_USUARIO "); 
		hql.append("   AND PESSOA_FILIAL_AGENDAMENTO.ID_PESSOA = FILIAL_AGENDAMENTO.ID_FILIAL "); 
		hql.append("   AND CLRE.ID_CLIENTE(+) = MONI.ID_CLIENTE_REM "); 
		hql.append("   AND CLDE.ID_CLIENTE(+) = MONI.ID_CLIENTE_DEST "); 
		hql.append("   AND FILIAL_ORIGEM.ID_FILIAL = CLRE.ID_FILIAL_ATENDE_OPERACIONAL"); 
		hql.append("   AND FILIAL_DESTINO.ID_FILIAL(+) = CLDE.ID_FILIAL_ATENDE_OPERACIONAL"); 
		hql.append("   AND PESSOA_FILIAL_DESTINO.ID_PESSOA(+) = FILIAL_DESTINO.ID_FILIAL "); 
		hql.append("   AND REMETENTE.ID_PESSOA(+) = CLRE.ID_CLIENTE"); 
		hql.append("   AND DESTINATARIO.ID_PESSOA(+) = CLDE.ID_CLIENTE "); 
		
		if ( criteria.getLong("filialOrigem.idFilial") != null ) {
			hql.append("AND FILIAL_ORIGEM.ID_FILIAL = :filialOrigem.idFilial ");
			parameters.put("filialOrigem.idFilial", criteria.get("filialOrigem.idFilial"));
		}

		if ( criteria.getLong("filialDestino.idFilial") != null ) {
			hql.append("AND FILIAL_DESTINO.ID_FILIAL = :filialDestino.idFilial ");
			parameters.put("filialDestino.idFilial", criteria.get("filialDestino.idFilial"));
		}
		
		if ( criteria.getLong("destinatario.idCliente") != null ) {
			hql.append("AND CLDE.ID_CLIENTE = :destinatario.idCliente ");
			parameters.put("destinatario.idCliente", criteria.get("destinatario.idCliente"));
		}
		
		if ( criteria.getLong("remetente.idCliente") != null) {
			hql.append("AND CLRE.ID_CLIENTE = :remetente.idCliente ");
			parameters.put("remetente.idCliente", criteria.get("remetente.idCliente"));
		}
		
		if ( criteria.getLong("filial.idFilial") != null ) {
			hql.append("AND FILIAL_AGENDAMENTO.ID_FILIAL = :filial.idFilial ");
			parameters.put("filial.idFilial", criteria.get("filial.idFilial"));
		}
		
		if ( criteria.getYearMonthDay("periodoAgendamentoInicial") != null && criteria.getYearMonthDay("periodoAgendamentoFinal") != null ) {
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) BETWEEN TRUNC(:periodoAgendamentoInicial) AND TRUNC(:periodoAgendamentoFinal) ");
			parameters.put("periodoAgendamentoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoInicial")));
			parameters.put("periodoAgendamentoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoFinal")));
		} 
		else if ( criteria.getYearMonthDay("periodoAgendamentoInicial") != null ) {
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) >= TRUNC(:periodoAgendamentoInicial) ");
			parameters.put("periodoAgendamentoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoInicial")) );
		} 
		else if ( criteria.getYearMonthDay("periodoAgendamentoFinal") != null ) {
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) = TRUNC(:periodoAgendamentoFinal) ");
			parameters.put("periodoAgendamentoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoFinal")) );
		}
									   
		if ( criteria.getYearMonthDay("periodoContatoInicial") != null && criteria.getYearMonthDay("periodoContatoFinal") != null ) {
			hql.append("AND TRUNC(AGEN.DH_CONTATO) BETWEEN TRUNC(:periodoContatoInicial) AND TRUNC(:periodoContatoFinal) ");
			parameters.put("periodoContatoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoInicial")));
			parameters.put("periodoContatoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoFinal")));
		} 
		else if ( criteria.getYearMonthDay("periodoContatoInicial") != null ) {
			hql.append("AND TRUNC(AGEN.DH_CONTATO) >= TRUNC(:periodoContatoInicial) ");
			parameters.put("periodoContatoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoInicial")) );
		} 
		else if ( criteria.getYearMonthDay("periodoContatoFinal") != null ) {
			hql.append("AND TRUNC(AGEN.DH_CONTATO) = TRUNC(:periodoContatoFinal) ");			
			parameters.put("periodoContatoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoContatoFinal")) );
		}		
		
		if ( !criteria.getString("tpSituacaoAgendamento").trim().equals("") ) {
			hql.append("AND AGEN.TP_SITUACAO_AGENDAMENTO = :tpSituacaoAgendamento ");
			parameters.put("tpSituacaoAgendamento", criteria.get("tpSituacaoAgendamento"));
		}

		if ( !criteria.getString("blCartao").trim().equals("")) {
			hql.append("AND AGEN.BL_CARTAO = :blCartao ");
			parameters.put("blCartao", criteria.get("blCartao"));
		}

		if ( !criteria.getString("turno").trim().equals("") ) {
			hql.append("AND AGEN.ID_TURNO = :turno ");
			parameters.put("turno", criteria.get("turno"));
		}

		if ( !criteria.getString("tpAgendamento").trim().equals("") ) {
			hql.append("AND AGEN.TP_AGENDAMENTO = :tpAgendamento ");
			parameters.put("tpAgendamento", criteria.get("tpAgendamento"));
		}	

		if ( criteria.getLong("usuario.idUsuario") != null ) {
			hql.append("AND AGEN.ID_USUARIO_CRIACAO = :usuario.idUsuario ");
			parameters.put("usuario.idUsuario", criteria.getLong("usuario.idUsuario"));
		}	
		
		if( criteria.getString("nrChaveNFe") != null ) {
			hql.append("AND MONI.NR_CHAVE LIKE :mont_cct.nrChave ");
			parameters.put("mont_cct.nrChave", criteria.getString("nrChaveNFe"));
		}
		
    	return hql;
    }
    
    private StringBuffer getSelectConsulta() {
		StringBuffer hql = new StringBuffer();
		
		hql.append("SELECT FILIAL_AGENDAMENTO.SG_FILIAL 			AS sgFilial," + // 1
				   "	   PESSOA_FILIAL_AGENDAMENTO.NM_FANTASIA 	AS nmFantasia," + // 2
				   "	   " + PropertyVarcharI18nProjection.createProjection("SERVICO.DS_SERVICO_I", "dsTipoServico") +  "," + // 3
				   "	   FILIAL_ORIGEM.SG_FILIAL AS sgFilialOrigem," + // 4
				   "	   DOSE.NR_DOCTO_SERVICO 					AS nrDoctoServico," + // 5
				   "	   DOSE.TP_DOCUMENTO_SERVICO 				AS tpDocumentoServico," + // 6
				   "	   FILIAL_DESTINO.SG_FILIAL 				AS sgFilialDestino," + // 7
				   " 	   PESSOA_FILIAL_DESTINO.NM_FANTASIA 		AS nmFantasiaDestino," + // 8
				   "	   REMETENTE.NR_IDENTIFICACAO 				AS nrIdentificacaoRemetente," + // 9
				   "	   REMETENTE.TP_IDENTIFICACAO 				AS tpIdentificacaoRemetente," + // 10
				   "	   REMETENTE.NM_PESSOA 						AS remetente," + // 11
				   "	   DESTINATARIO.NR_IDENTIFICACAO 			AS nrIdentificacaoDestinatario," + // 12
				   "	   DESTINATARIO.TP_IDENTIFICACAO 			AS tpIdentificacaoDestinatario," + // 13
				   "	   DESTINATARIO.NM_PESSOA 					AS destinatario," + // 14
				   "	   AGEN.TP_AGENDAMENTO 						AS tpAgendamento," + // 15
				   "	   AGEN.DT_AGENDAMENTO 						AS dtAgendamento," + // 16
				   "	   AGEN.HR_PREFERENCIA_INICIAL 				AS horarioInicial," + // 17
				   "	   AGEN.HR_PREFERENCIA_FINAL 				AS horarioFinal," + // 18
				   "	   AGEN.TP_SITUACAO_AGENDAMENTO 			AS tpSituacaoAgendamento," + // 19
				   "	   TURNO.DS_TURNO 							AS dsTurno," + // 20
				   "	   USUARIO_CRIACAO.NM_USUARIO 				AS agendadoPor," + // 21
				   "	   (SELECT MIN(NOFC.NR_NOTA_FISCAL) 		AS nrNotaFiscal " + // 22
				   " 			   FROM NOTA_FISCAL_CONHECIMENTO NOFC " +
				   " 			   WHERE NOFC.ID_CONHECIMENTO = DOSE.ID_DOCTO_SERVICO ) AS nrNotaFiscal, " +
				   "	   AGEN.ID_AGENDAMENTO_ENTREGA 						AS idAgendamentoEntrega, " + // 23
				   "	   DOSE.DT_PREV_ENTREGA 						    AS dtPrevEntrega, " + // 24
				   "	   DESTINATARIO.TP_PESSOA 						    AS tpDestinatario, " + // 25
	   			   "	   AGEN.OB_AGENDAMENTO_ENTREGA 						AS obAgendamentoEntrega, " + // 26
	   			   "	   MAED.DH_OCORRENCIA 								AS dhBaixa, " + // 27
		   		   "		CASE WHEN " +
		   		   "				(AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) " +
		   		   "			THEN SEMI_REBOQUE.NR_FROTA " +
		   		   "			ELSE NULL END " +
		   		   "			AS semiReboqueNrFrota, " + // 28
		   		   "		CASE WHEN " +
		   		   "				(AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) " +
		   		   "			THEN SEMI_REBOQUE.NR_IDENTIFICADOR " +
		   		   "			ELSE NULL END " +
		   		   "			AS semiReboqueNrIdentificador, " + // 29
		   		   "		CASE WHEN " +
		   		   "				(AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) " +
		   		   "			THEN MEIO_TRANSPORTE.NR_FROTA " +
		   		   "			ELSE NULL END " +
		   		   "			AS meioTransporteNrFrota, " + // 30
		   		   "		CASE WHEN " +
		   		   "				(AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) " +
		   		   "			THEN MEIO_TRANSPORTE.NR_IDENTIFICADOR " +
		   		   "			ELSE NULL END " +
		   		   "			AS meioTransporteNrIdentificador, " + // 31
				   "	   AGDS.ID_AGENDAMENTO_DOCTO_SERVICO AS idAgendamentoDoctoServico, " + // 32
	   			   "	   DOSE.ID_DOCTO_SERVICO AS idDoctoServico, " + // 33
		   		   "		CASE WHEN " +
		   		   "				(AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) " +
		   		   "			THEN (SELECT TO_CHAR(MAED3.DH_OCORRENCIA, 'DD/MM/RRRR') " +
				   " 			   FROM MANIFESTO_ENTREGA_DOCUMENTO MAED3," +
				   "			   MANIFESTO MANI3, " +
				   "			   OCORRENCIA_ENTREGA OCEN3 " +
				   " 			   WHERE MAED3.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO " +
				   "			   AND   MANI3.ID_MANIFESTO = MAED3.ID_MANIFESTO_ENTREGA " +
				   "			   AND   OCEN3.ID_OCORRENCIA_ENTREGA = MAED3.ID_OCORRENCIA_ENTREGA" +
				   "			   AND   OCEN3.TP_OCORRENCIA = 'E'" +
				   "			   ) " +
		   		   "			ELSE NULL END " +
		   		   "			AS dataEntrega, " +  // 34
		   		   "		CASE WHEN " +
		   		   "				(AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) " +
		   		   "			THEN (SELECT MAED3.DH_OCORRENCIA " +
				   " 			   FROM MANIFESTO_ENTREGA_DOCUMENTO MAED3," +
				   "			   MANIFESTO MANI3, " +
				   "			   OCORRENCIA_ENTREGA OCEN3 " +
				   " 			   WHERE MAED3.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO " +
				   "			   AND   MANI3.ID_MANIFESTO = MAED3.ID_MANIFESTO_ENTREGA " +
				   "			   AND   OCEN3.ID_OCORRENCIA_ENTREGA = MAED3.ID_OCORRENCIA_ENTREGA" +
				   "			   AND   OCEN3.TP_OCORRENCIA = 'E'" +
				   "			   ) " +
		   		   "			ELSE NULL END " +
		   		   "			AS horaEntrega, " +  // 35			   
		   		   "		AGEN.BL_CARTAO AS blCartao, " +  // 37
				   //Mostrar controle de carga e manifesto de entrega somente se o documento foi entregue
				   //(OCORRENCIA_ENTREGA.TP_OCORRENCIA = 'E')) ou está manifestado 
				   //(MANIFESTO_ENTREGA_DOCUMENTO.ID_OCORRENCIA is NULL)
				   "		CASE WHEN " +
				   "				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
				   "			THEN COCA.NR_CONTROLE_CARGA " +
				   "			ELSE NULL END " +
				   "			AS controleCarga, " + // 38
	   			   "		CASE WHEN " +
	   			   "				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
	   			   "			THEN FILIAL_ORIGEM_CONTROLE_CARGA.SG_FILIAL " +
	   			   "			ELSE NULL END " +
	   			   "			AS sgFilialControleCarga, " + // 39
		   		   "		CASE WHEN " +
		   		   "				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
		   		   "			THEN MAEN.NR_MANIFESTO_ENTREGA " +
		   		   "			ELSE NULL END " +
		   		   "			AS manifestoEntrega, " + // 40
			       "		CASE WHEN " +
			       "				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND ((ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL))) " +
			       "			THEN FILIAL_MANIFESTO_ENTREGA.SG_FILIAL " +
			       "			ELSE NULL END " +
			       "			AS sgFilialManifestoEntrega " ); // 42
		return hql;		
    }
    
    private StringBuffer getFromConsulta(){
		StringBuffer hql = new StringBuffer();
		
		hql.append("FROM AGENDAMENTO_DOCTO_SERVICO	AGDS, " +
				"		 AGENDAMENTO_ENTREGA		AGEN," +
				"		 DOCTO_SERVICO 				DOSE," +
				
				"		 MANIFESTO_ENTREGA_DOCUMENTO    MAED," +
				"		 MANIFESTO_ENTREGA				MAEN," +
				"		 FILIAL							FILIAL_MANIFESTO_ENTREGA," +
				"		 MANIFESTO						MANI," +
				"		 OCORRENCIA_ENTREGA				OCEN," +
				"		 CONTROLE_CARGA					COCA," +
				"		 FILIAL							FILIAL_ORIGEM_CONTROLE_CARGA," +
				"		 MEIO_TRANSPORTE				MEIO_TRANSPORTE," +
				"		 MEIO_TRANSPORTE				SEMI_REBOQUE," +
				
				"		 FILIAL 					FILIAL_AGENDAMENTO," +
				"		 FILIAL 					FILIAL_ORIGEM," +
				"	     TURNO 						TURNO," +
				"		 USUARIO 					USUARIO_CRIACAO," +
				"		 FILIAL 					FILIAL_DESTINO," +
				"		 CLIENTE 					CLRE,  " +
				"		 CLIENTE 					CLDE,  " +
				"		 PESSOA 					REMETENTE," +
				"		 PESSOA 					DESTINATARIO," +
				"	     PESSOA 					PESSOA_FILIAL_AGENDAMENTO," +
				"	     PESSOA 					PESSOA_FILIAL_DESTINO," +
				"	 	 SERVICO 					SERVICO," +
				"		 TIPO_SERVICO 				TISE "); 
		
    	return hql; 
    }
    
    private StringBuffer getWhereConsulta(TypedFlatMap criteria, Map parameters) {
    	
		StringBuffer hql = new StringBuffer();
		
		hql.append("WHERE AGDS.ID_AGENDAMENTO_ENTREGA = AGEN.ID_AGENDAMENTO_ENTREGA" +
				"	AND   AGDS.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO" +
				"	AND   AGEN.ID_FILIAL = FILIAL_AGENDAMENTO.ID_FILIAL" +
				"	AND   AGEN.ID_TURNO = TURNO.ID_TURNO(+)" +
				"	AND   AGEN.ID_USUARIO_CRIACAO = USUARIO_CRIACAO.ID_USUARIO" +
				
				"   AND   MAED.ID_DOCTO_SERVICO(+) = DOSE.ID_DOCTO_SERVICO" +
				"   AND   MAEN.ID_MANIFESTO_ENTREGA(+) = MAED.ID_MANIFESTO_ENTREGA" +
				"   AND   FILIAL_MANIFESTO_ENTREGA.ID_FILIAL(+) = MAEN.ID_FILIAL" +
				"   AND   MANI.ID_MANIFESTO(+) = MAEN.ID_MANIFESTO_ENTREGA" +
				"   AND   COCA.ID_CONTROLE_CARGA(+) = MANI.ID_CONTROLE_CARGA" +
				"   AND   FILIAL_ORIGEM_CONTROLE_CARGA.ID_FILIAL(+) = COCA.ID_FILIAL_ORIGEM" +
				"   AND   MEIO_TRANSPORTE.ID_MEIO_TRANSPORTE(+) = COCA.ID_TRANSPORTADO" +
				"   AND   SEMI_REBOQUE.ID_MEIO_TRANSPORTE(+) = COCA.ID_SEMI_REBOCADO" +
				"   AND   OCEN.ID_OCORRENCIA_ENTREGA(+) = MAED.ID_OCORRENCIA_ENTREGA" +
				
				"	AND   PESSOA_FILIAL_AGENDAMENTO.ID_PESSOA = FILIAL_AGENDAMENTO.ID_FILIAL" +
				"	AND   FILIAL_ORIGEM.ID_FILIAL = DOSE.ID_FILIAL_ORIGEM" +
				"	AND   FILIAL_DESTINO.ID_FILIAL(+) = DOSE.ID_FILIAL_DESTINO_OPERACIONAL" +
				"	AND   PESSOA_FILIAL_DESTINO.ID_PESSOA(+) = FILIAL_DESTINO.ID_FILIAL" +
				"	AND   CLRE.ID_CLIENTE(+) = DOSE.ID_CLIENTE_REMETENTE" +
				"	AND   REMETENTE.ID_PESSOA(+) = CLRE.ID_CLIENTE" +
				"	AND   CLDE.ID_CLIENTE(+) = DOSE.ID_CLIENTE_DESTINATARIO" +
				"	AND   DESTINATARIO.ID_PESSOA(+) = CLDE.ID_CLIENTE" +
				"	AND   SERVICO.ID_SERVICO(+) = DOSE.ID_SERVICO" +
				"	AND   TISE.ID_TIPO_SERVICO(+) = SERVICO.ID_TIPO_SERVICO ");

		// Regra para trazer o manifesto mais recente
		hql.append(" AND (MAEN.DH_EMISSAO IS NULL " +
				"         OR MAEN.DH_EMISSAO = ( SELECT MAX(MAEN2.DH_EMISSAO) " +
				"		   FROM MANIFESTO_ENTREGA_DOCUMENTO	MAED2," +
				"		   MANIFESTO_ENTREGA MAEN2" +
				"		   WHERE MAED2.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO" +
				"          AND   MAEN2.ID_MANIFESTO_ENTREGA = MAED2.ID_MANIFESTO_ENTREGA" +				
				"		 ) " +
				" ) ");

		if ( criteria.getString("formaConsulta").equals("N") ) { // Não Manifestados
			hql.append(" AND ( ");
			hql.append("      NOT EXISTS ( SELECT 1 ");
			hql.append("				   FROM MANIFESTO_ENTREGA_DOCUMENTO MAED ");
			hql.append("				   WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO  ");
			hql.append("				 ) ");
			hql.append("	  OR (");
			hql.append("	       EXISTS ( SELECT 1 ");
			hql.append("	       			FROM MANIFESTO_ENTREGA_DOCUMENTO MAED, ");
			hql.append("	       			     MANIFESTO MANI ");
			hql.append("				    WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO  ");
			hql.append("				    AND   MAED.ID_MANIFESTO_ENTREGA = MANI.ID_MANIFESTO ");
			hql.append("				    AND   MANI.TP_STATUS_MANIFESTO = 'CA' ");
			hql.append("				  )");
			hql.append("	     )");
			hql.append("	  OR (");
			hql.append("	       EXISTS ( SELECT 1 ");
			hql.append("	       			FROM MANIFESTO_ENTREGA_DOCUMENTO MAED, ");
			hql.append("	       			     OCORRENCIA_ENTREGA OCEN ");
			hql.append("				    WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO  ");
			hql.append("				    AND   MAED.ID_OCORRENCIA_ENTREGA = OCEN.ID_OCORRENCIA_ENTREGA ");
			hql.append("				    AND   OCEN.TP_OCORRENCIA <> 'E' ");
			hql.append("					AND   NOT EXISTS ( SELECT 1 ");
			hql.append("	    	     		 			   FROM MANIFESTO_ENTREGA_DOCUMENTO MAED, ");
			hql.append("	       		      		  				MANIFESTO MANI, ");
			hql.append("	       		      		  			    OCORRENCIA_ENTREGA OCEN ");
			hql.append("				 		 			   WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO  ");
			hql.append("				 		 			   AND   MAED.ID_MANIFESTO_ENTREGA = MANI.ID_MANIFESTO ");
			hql.append("				 		               AND   MANI.TP_STATUS_MANIFESTO <> 'CA' ");
			hql.append("				 		               AND   MAED.ID_OCORRENCIA_ENTREGA = OCEN.ID_OCORRENCIA_ENTREGA ");
			hql.append("				 		               AND   OCEN.TP_OCORRENCIA = 'E' ");
			hql.append("									)");
			hql.append("				)");
			hql.append("	     )");
			hql.append("	 )");
			hql.append(" AND AGEN.TP_SITUACAO_AGENDAMENTO NOT IN ('C','R') ");  
			
		} else if ( criteria.getString("formaConsulta").equals("M") ) { // Manifestados
			hql.append(" AND ( ");
			hql.append("		EXISTS ( SELECT 1 ");
			hql.append("	    	     FROM MANIFESTO_ENTREGA_DOCUMENTO MAED, ");
			hql.append("	       		      MANIFESTO MANI ");
			hql.append("				 WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO  ");
			hql.append("				 AND   MAED.ID_MANIFESTO_ENTREGA = MANI.ID_MANIFESTO ");
			hql.append("				 AND   MAED.TP_SITUACAO_DOCUMENTO IN ('PBAI', 'PBRC', 'PBCO') ");
			hql.append("				 AND   MANI.TP_STATUS_MANIFESTO <> 'CA' ");
			hql.append("				)");
			hql.append("	 )");
			hql.append(" AND AGEN.TP_SITUACAO_AGENDAMENTO NOT IN ('C','R') ");
		} else if ( criteria.getString("formaConsulta").equals("E") ) { // Entregue
			hql.append(" AND ( ");
			hql.append("		EXISTS ( SELECT 1 ");
			hql.append("	    	     FROM MANIFESTO_ENTREGA_DOCUMENTO MAED, ");
			hql.append("	       		      MANIFESTO MANI, ");
			hql.append("	       		      OCORRENCIA_ENTREGA OCEN ");
			hql.append("				 WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO  ");
			hql.append("				 AND   MAED.ID_MANIFESTO_ENTREGA = MANI.ID_MANIFESTO ");
			hql.append("				 AND   MANI.TP_STATUS_MANIFESTO <> 'CA' ");
			hql.append("				 AND   MAED.ID_OCORRENCIA_ENTREGA = OCEN.ID_OCORRENCIA_ENTREGA ");
			hql.append("				 AND   OCEN.TP_OCORRENCIA = 'E' ");
			hql.append("				)");
			hql.append("	 )");
			hql.append(" AND AGEN.TP_SITUACAO_AGENDAMENTO NOT IN ('C','R') ");
		}

		if ( criteria.getString("entregas").equals("FDAG") ) { // Fora da Data Agendada
			
			hql.append(" AND TO_CHAR(AGEN.DT_AGENDAMENTO, 'DDMMRRRR' ) <> TO_CHAR(MAED.DH_OCORRENCIA, 'DDMMRRRR')");
			hql.append(" AND OCEN.TP_OCORRENCIA = 'E'");
			
		} else if ( criteria.getString("entregas").equals("FTAG") ) { // Fora Turno Agendado

			hql.append(" AND ( ");
			hql.append("      ( ");
			hql.append("       AGEN.ID_TURNO IS NULL ");
			hql.append("       AND TO_CHAR(MAED.DH_OCORRENCIA, 'DDMMRRRR') != TO_CHAR(AGEN.DT_AGENDAMENTO, 'DDMMRRRR') ");
			hql.append(" 	   AND OCEN.TP_OCORRENCIA = 'E'");
			hql.append("      )");			
			hql.append("      OR");			
			hql.append("      (");
			hql.append(" 		TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') NOT BETWEEN TO_CHAR(TURNO.HR_TURNO_INICIAL, 'HHMI') AND TO_CHAR(TURNO.HR_TURNO_FINAL, 'HHMI') ");
			hql.append(" 		AND OCEN.TP_OCORRENCIA = 'E'");
			hql.append("      )");			
			hql.append("     )");			

		} else if ( criteria.getString("entregas").equals("FHAG") ) { // Fora Horário Agendado

			hql.append(" AND ( ");
			hql.append("       AGEN.HR_PREFERENCIA_INICIAL IS NOT NULL ");
			hql.append("       AND TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') NOT BETWEEN TO_CHAR(AGEN.HR_PREFERENCIA_INICIAL, 'HHMI') AND TO_CHAR(AGEN.HR_PREFERENCIA_FINAL, 'HHMI') ");
			hql.append(" 	   AND OCEN.TP_OCORRENCIA = 'E'");
			hql.append("     )");			
			
		} else if ( criteria.getString("entregas").equals("FDTH") ) { // Fora da Data/Turno/Horário Agendado
			
			hql.append(" AND ( ");
			hql.append(" 		( TO_CHAR(AGEN.DT_AGENDAMENTO, 'DDMMRRRR' ) <> TO_CHAR(MAED.DH_OCORRENCIA, 'DDMMRRRR')");

			hql.append(" 		AND OCEN.TP_OCORRENCIA = 'E' )");

			hql.append(" OR ( ");
			hql.append("       AGEN.ID_TURNO IS NOT NULL ");
			hql.append(" 	   AND TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') NOT BETWEEN TO_CHAR(TURNO.HR_TURNO_INICIAL, 'HHMI') AND TO_CHAR(TURNO.HR_TURNO_FINAL, 'HHMI') ");
			hql.append("     )");			
			
			hql.append(" OR ( ");
			hql.append("       AGEN.HR_PREFERENCIA_INICIAL IS NOT NULL ");
			hql.append("       AND TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') NOT BETWEEN TO_CHAR(AGEN.HR_PREFERENCIA_INICIAL, 'HHMI') AND TO_CHAR(AGEN.HR_PREFERENCIA_FINAL, 'HHMI') ");
			hql.append("     )");			
			hql.append("    )");			
			
		} else if ( criteria.getString("entregas").equals("DDAG") ) { // Dentro da Data Agendada
			
			hql.append(" AND TO_CHAR(AGEN.DT_AGENDAMENTO, 'DDMMRRRR' ) = TO_CHAR(MAED.DH_OCORRENCIA, 'DDMMRRRR')");
			hql.append(" AND OCEN.TP_OCORRENCIA = 'E'");
			
		} else if ( criteria.getString("entregas").equals("DTAG") ) { // Dentro do Turno Agendado
			
			hql.append(" AND ( ");
			hql.append("      ( ");
			hql.append("       AGEN.ID_TURNO IS NULL ");
			hql.append("       AND TO_CHAR(MAED.DH_OCORRENCIA, 'DDMMRRRR') = TO_CHAR(AGEN.DT_AGENDAMENTO, 'DDMMRRRR') ");
			hql.append(" 	   AND OCEN.TP_OCORRENCIA = 'E' ");
			hql.append("      )");			
			hql.append("      OR");			
			hql.append("      (");
			hql.append(" 		TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') BETWEEN TO_CHAR(TURNO.HR_TURNO_INICIAL, 'HHMI') AND TO_CHAR(TURNO.HR_TURNO_FINAL, 'HHMI') ");
			hql.append(" 		AND OCEN.TP_OCORRENCIA = 'E'");
			hql.append("      )");			
			hql.append("     )");			
			
		} else if ( criteria.getString("entregas").equals("DHAG") ) { // Dentro do Horario Agendado
			
			hql.append(" AND ( ");
			hql.append("       AGEN.HR_PREFERENCIA_INICIAL IS NOT NULL ");
			hql.append("       AND TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') BETWEEN TO_CHAR(AGEN.HR_PREFERENCIA_INICIAL, 'HHMI') AND TO_CHAR(AGEN.HR_PREFERENCIA_FINAL, 'HHMI') ");
			hql.append(" 	   AND OCEN.TP_OCORRENCIA = 'E'");
			hql.append("     )");			
			
		} else if ( criteria.getString("entregas").equals("DDTH") ) { // Dentro da Data/Turno/Horario Agendado

			hql.append(" AND ( ");
			hql.append(" 	  ( TO_CHAR(AGEN.DT_AGENDAMENTO, 'DDMMRRRR' ) = TO_CHAR(MAED.DH_OCORRENCIA, 'DDMMRRRR')");
			hql.append(" 	    AND OCEN.TP_OCORRENCIA = 'E' ) ");

			hql.append(" OR ( ");
			hql.append("       AGEN.ID_TURNO IS NOT NULL ");
			hql.append(" 	   AND TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') BETWEEN TO_CHAR(TURNO.HR_TURNO_INICIAL, 'HHMI') AND TO_CHAR(TURNO.HR_TURNO_FINAL, 'HHMI') ");
			hql.append("     )");			

			hql.append(" OR ( ");
			hql.append("       AGEN.HR_PREFERENCIA_INICIAL IS NOT NULL ");
			hql.append("       AND TO_CHAR(MAED.DH_OCORRENCIA, 'HHMI') BETWEEN TO_CHAR(AGEN.HR_PREFERENCIA_INICIAL, 'HHMI') AND TO_CHAR(AGEN.HR_PREFERENCIA_FINAL, 'HHMI') ");
			hql.append("     )");			
			hql.append("    )");			
			
		}
		
		if ( criteria.getLong("notaFiscalCliente.idNotaFiscalConhecimento") != null ) {
			
			hql.append("AND DOSE.ID_DOCTO_SERVICO = ( SELECT NOFC.ID_CONHECIMENTO " + 
													   " FROM NOTA_FISCAL_CONHECIMENTO NOFC" + 
													   " WHERE NOFC.ID_NOTA_FISCAL_CONHECIMENTO = :notaFiscalConhecimento.idNotaFiscalConhecimento ) ");
			parameters.put("notaFiscalConhecimento.idNotaFiscalConhecimento", criteria.get("notaFiscalCliente.idNotaFiscalConhecimento"));
			
		}		   
		   
		if ( criteria.getLong("controleCarga.idControleCarga") != null ) {
			
			hql.append("AND :controleCarga.idControleCarga = MANI.ID_CONTROLE_CARGA ");
			parameters.put("controleCarga.idControleCarga", criteria.get("controleCarga.idControleCarga"));
			
		}

		if ( criteria.getLong("manifestoEntrega.idManifestoEntrega") != null ) {
			
			hql.append("AND :manifestoEntrega.idManifestoEntrega = MAEN.ID_MANIFESTO_ENTREGA ");
			parameters.put("manifestoEntrega.idManifestoEntrega", criteria.get("manifestoEntrega.idManifestoEntrega"));
			
		}
		
		if ( criteria.getLong("filialByIdFilialOrigem.idFilial") != null ) {
			
			hql.append("AND FILIAL_ORIGEM.ID_FILIAL = :filialByIdFilialOrigem.idFilial ");
			parameters.put("filialByIdFilialOrigem.idFilial", criteria.get("filialByIdFilialOrigem.idFilial"));
			
		}
		
		
		if ( criteria.getLong("filialByIdFilialDestino.idFilial") != null ) {
			
			hql.append("AND FILIAL_DESTINO.ID_FILIAL = :filialByIdFilialDestino.idFilial ");
			parameters.put("filialByIdFilialDestino.idFilial", criteria.get("filialByIdFilialDestino.idFilial"));
			
		}
		
		if ( criteria.getLong("filial.idFilial") != null ) {
			
			hql.append("AND FILIAL_AGENDAMENTO.ID_FILIAL = :filial.idFilial ");
			parameters.put("filial.idFilial", criteria.get("filial.idFilial"));
			
		}
		
		if ( criteria.getLong("remetente.idCliente") != null) {
			
			hql.append("AND CLRE.ID_CLIENTE = :remetente.idCliente ");
			parameters.put("remetente.idCliente", criteria.get("remetente.idCliente"));
			
		}

		if ( criteria.getLong("destinatario.idCliente") != null) {
			
			hql.append("AND CLDE.ID_CLIENTE = :destinatario.idCliente ");
			parameters.put("destinatario.idCliente", criteria.get("destinatario.idCliente"));
			
		}		
		
		if ( !criteria.getString("tpDestinatario").trim().equals("") ) {
			
			hql.append("AND DESTINATARIO.TP_PESSOA = :tpDestinatario ");
			parameters.put("tpDestinatario", criteria.get("tpDestinatario"));
			
		}
		
		if ( criteria.getLong("idTipoServico") != null ) {
			
			hql.append("AND SERVICO.ID_TIPO_SERVICO = :idTipoServico ");
			parameters.put("idTipoServico", criteria.get("idTipoServico"));
			
		}
		
		if ( criteria.getYearMonthDay("periodoDPEInicial") != null && criteria.getYearMonthDay("periodoDPEFinal") != null ) {
			
			hql.append("AND TRUNC(DOSE.DT_PREV_ENTREGA) BETWEEN TRUNC(:periodoDPEInicial) AND TRUNC(:periodoDPEFinal) ");
			parameters.put("periodoDPEInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoDPEInicial")));
			parameters.put("periodoDPEFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoDPEFinal")));
			
		} else if ( criteria.getYearMonthDay("periodoDPEInicial") != null ) {
			
			hql.append("AND TRUNC(DOSE.DT_PREV_ENTREGA) >= TRUNC(:periodoDPEInicial) ");
			parameters.put("periodoDPEInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoDPEInicial")) );
			
		} else if ( criteria.getYearMonthDay("periodoDPEFinal") != null ) {
			
			hql.append("AND TRUNC(DOSE.DT_PREV_ENTREGA) = TRUNC(:periodoDPEFinal) ");
			parameters.put("periodoDPEFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoDPEFinal")) );
			
		}

		if ( criteria.getYearMonthDay("periodoAgendamentoInicial") != null && criteria.getYearMonthDay("periodoAgendamentoFinal") != null ) {
			
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) BETWEEN TRUNC(:periodoAgendamentoInicial) AND TRUNC(:periodoAgendamentoFinal) ");
			parameters.put("periodoAgendamentoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoInicial")));
			parameters.put("periodoAgendamentoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoFinal")));
			
		} else if ( criteria.getYearMonthDay("periodoAgendamentoInicial") != null ) {
			
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) >= TRUNC(:periodoAgendamentoInicial) ");
			parameters.put("periodoAgendamentoInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoInicial")) );
			
		} else if ( criteria.getYearMonthDay("periodoAgendamentoFinal") != null ) {
			
			hql.append("AND TRUNC(AGEN.DT_AGENDAMENTO) = TRUNC(:periodoAgendamentoFinal) ");			
			parameters.put("periodoAgendamentoFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoAgendamentoFinal")) );
			
		}
		
		if ( criteria.getString("doctoServico.tpDocumentoServico") != null && !criteria.getString("doctoServico.tpDocumentoServico").equals("")) {
			hql.append("AND DOSE.TP_DOCUMENTO_SERVICO = :doctoServico.tpDocumentoServico ");
			parameters.put("doctoServico.tpDocumentoServico", criteria.getString("doctoServico.tpDocumentoServico"));
		} else {
			hql.append("AND DOSE.TP_DOCUMENTO_SERVICO IN ('CRT', 'CTR', 'MDA', 'NFT')");
		}

		if ( criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null ) {
			hql.append("AND DOSE.ID_FILIAL_ORIGEM = :doctoServico.filialByIdFilialOrigem.idFilial ");
			parameters.put("doctoServico.filialByIdFilialOrigem.idFilial", criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
		}
 
		if (criteria.getLong("idDoctoServico") != null) {
			Long idDoctoServico = null;
			idDoctoServico = criteria.getLong("idDoctoServico");
			
			hql.append("AND DOSE.ID_DOCTO_SERVICO = :doctoServico.idDoctoServico ");
			parameters.put("doctoServico.idDoctoServico", idDoctoServico);
		}			
		
    	return hql;
    }
    
	public ResultSetPage findPaginatedAgendamentoEntregaDoctoServico(TypedFlatMap criteria, FindDefinition findDef) {
		Map parameters = new HashMap();
		
		StringBuffer sql = new StringBuffer();
		
		// LMS-3252
		if(criteria.getString("tpDocumento").equals("NFE")) {
			sql.append(getSelectForNFE());
			sql.append(getFromForNFE());
			sql.append(getWhereForNFE(criteria, parameters));
			sql.append("ORDER BY AGEN.DT_AGENDAMENTO ");
		}
		else {
		sql.append(getSelect());
		sql.append(getFrom());
		sql.append(getWhere(criteria, parameters));
		sql.append("ORDER BY DOSE.TP_DOCUMENTO_SERVICO, DOSE.ID_FILIAL_ORIGEM, DOSE.NR_DOCTO_SERVICO, AGEN.DT_AGENDAMENTO ");		
		}
		
		sql.insert(0,"SELECT * FROM (").append(") ");
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("sgFilial",Hibernate.STRING);				
				sqlQuery.addScalar("nmFantasia",Hibernate.STRING);				
				sqlQuery.addScalar("dsTipoServico",Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOrigem",Hibernate.STRING);							
				sqlQuery.addScalar("nrDoctoServico",Hibernate.LONG);			
				sqlQuery.addScalar("tpDocumentoServico",Hibernate.STRING);							
				sqlQuery.addScalar("sgFilialDestino",Hibernate.STRING);										
				sqlQuery.addScalar("nmFantasiaDestino",Hibernate.STRING);				
				sqlQuery.addScalar("nrIdentificacaoRemetente",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoRemetente",Hibernate.STRING);				
				sqlQuery.addScalar("remetente",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoDestinatario",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoDestinatario",Hibernate.STRING);				
				sqlQuery.addScalar("destinatario",Hibernate.STRING);				
				sqlQuery.addScalar("tpAgendamento",Hibernate.STRING);				
				sqlQuery.addScalar("dtAgendamento", Hibernate.custom(JodaTimeYearMonthDayUserType.class));				
				sqlQuery.addScalar("horarioInicial",Hibernate.custom(JodaTimeTimeOfDayUserType.class));				
				sqlQuery.addScalar("horarioFinal",Hibernate.custom(JodaTimeTimeOfDayUserType.class));				
				sqlQuery.addScalar("tpSituacaoAgendamento",Hibernate.STRING);				
				sqlQuery.addScalar("dsTurno",Hibernate.STRING);				
				sqlQuery.addScalar("agendadoPor",Hibernate.STRING);
				sqlQuery.addScalar("nrNotaFiscal",Hibernate.LONG);
				sqlQuery.addScalar("idAgendamentoEntrega",Hibernate.LONG);				
				sqlQuery.addScalar("dhContato",Hibernate.custom(JodaTimeDateTimeUserType.class));				
				sqlQuery.addScalar("nrChave", Hibernate.STRING);				
			}
		};
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),findDef.getCurrentPage(),findDef.getPageSize(),parameters,confSql);
	}

	public Integer getRowCountAgendamentoEntregaDoctoServico(TypedFlatMap criteria) {
		Map parameters = new HashMap();

		StringBuffer hql = new StringBuffer();
		
		// LMS-3252
		if(criteria.getString("tpDocumento").equals("NFE")) {
			hql.append(getSelectForNFE());
			hql.append(getFromForNFE());
			hql.append(getWhereForNFE(criteria, parameters));	
		}
		else {
		hql.append(getSelect());
		hql.append(getFrom());
		hql.append(getWhere(criteria, parameters));
		}

		return getAdsmHibernateTemplate().getRowCountBySql(new StringBuffer(hql.toString()).insert(0,"FROM (").append(")").toString(), parameters);
	}

	public ResultSetPage findPaginatedConsultaAgendamentoEntregaDoctoServico(TypedFlatMap criteria, FindDefinition findDef) {
		Map parameters = new HashMap();

		StringBuffer sql = new StringBuffer();
		
		sql.append(getSelectConsulta());
		sql.append(getFromConsulta());
		sql.append(getWhereConsulta(criteria, parameters));
		
		sql.append(" ORDER BY AGEN.DT_AGENDAMENTO ");		
		
		sql.insert(0,"SELECT * FROM (").append(") ");
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("sgFilial",Hibernate.STRING);
				sqlQuery.addScalar("nmFantasia",Hibernate.STRING);
				sqlQuery.addScalar("dsTipoServico",Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOrigem",Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico",Hibernate.LONG);
				sqlQuery.addScalar("tpDocumentoServico",Hibernate.STRING);
				sqlQuery.addScalar("sgFilialDestino",Hibernate.STRING);
				sqlQuery.addScalar("nmFantasiaDestino",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoRemetente",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoRemetente",Hibernate.STRING);
				sqlQuery.addScalar("remetente",Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoDestinatario",Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoDestinatario",Hibernate.STRING);
				sqlQuery.addScalar("destinatario",Hibernate.STRING);
				sqlQuery.addScalar("tpAgendamento",Hibernate.STRING);
				sqlQuery.addScalar("dtAgendamento", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("horarioInicial",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("horarioFinal",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("tpSituacaoAgendamento",Hibernate.STRING);
				sqlQuery.addScalar("dsTurno",Hibernate.STRING);
				sqlQuery.addScalar("agendadoPor",Hibernate.STRING);
				sqlQuery.addScalar("nrNotaFiscal",Hibernate.LONG);
				sqlQuery.addScalar("idAgendamentoEntrega",Hibernate.LONG);
				
				sqlQuery.addScalar("dtPrevEntrega",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("tpDestinatario",Hibernate.STRING);
				sqlQuery.addScalar("obAgendamentoEntrega",Hibernate.STRING);
				sqlQuery.addScalar("dhBaixa",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("semiReboqueNrFrota",Hibernate.STRING);
				sqlQuery.addScalar("semiReboqueNrIdentificador",Hibernate.STRING);
				sqlQuery.addScalar("meioTransporteNrFrota",Hibernate.STRING);
				sqlQuery.addScalar("meioTransporteNrIdentificador",Hibernate.STRING);
				sqlQuery.addScalar("idAgendamentoDoctoServico",Hibernate.LONG);
				sqlQuery.addScalar("idDoctoServico",Hibernate.LONG);
				sqlQuery.addScalar("dataEntrega",Hibernate.STRING);
				sqlQuery.addScalar("horaEntrega",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("blCartao",Hibernate.STRING);
				sqlQuery.addScalar("controleCarga",Hibernate.LONG);
				sqlQuery.addScalar("sgFilialControleCarga",Hibernate.STRING);
				sqlQuery.addScalar("manifestoEntrega",Hibernate.LONG);
				sqlQuery.addScalar("sgFilialManifestoEntrega",Hibernate.STRING);
			}
		};
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),findDef.getCurrentPage(),findDef.getPageSize(),parameters,confSql);
	}

	public Integer getRowCountConsultaAgendamentoEntregaDoctoServico(TypedFlatMap criteria) {
		Map parameters = new HashMap();

		StringBuffer hql = new StringBuffer();
		hql.append(getSelectConsulta());
		hql.append(getFromConsulta());
		hql.append(getWhereConsulta(criteria, parameters));

		return getAdsmHibernateTemplate().getRowCountBySql(new StringBuffer(hql.toString()).insert(0,"FROM (").append(")").toString(), parameters); 
	}

	public List findPaginatedDoctoServico(TypedFlatMap criteria) {
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer sql = createDoctoServicoQuery(criteria, paramValues);

		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idDoctoServico",Hibernate.LONG);
				sqlQuery.addScalar("tpDocumentoServico",Hibernate.STRING);				
				sqlQuery.addScalar("filial_origem_sgfilial",Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico",Hibernate.LONG);							
				sqlQuery.addScalar("pere_tpIdentificacao",Hibernate.STRING);			
				sqlQuery.addScalar("pere_nrIdentificacao",Hibernate.STRING);							
				sqlQuery.addScalar("pere_nmPessoa",Hibernate.STRING);										
				sqlQuery.addScalar("pede_tpIdentificacao",Hibernate.STRING);				
				sqlQuery.addScalar("pede_nrIdentificacao",Hibernate.STRING);
				sqlQuery.addScalar("pede_nmPessoa",Hibernate.STRING);				
				sqlQuery.addScalar("dtPreventrega",Hibernate.custom(JodaTimeTimeOfDayUserType.class));
				sqlQuery.addScalar("idFilialDestino",Hibernate.LONG);
				sqlQuery.addScalar("idClienteRemetente",Hibernate.LONG);
				sqlQuery.addScalar("idClienteDestinatario",Hibernate.LONG);				
				sqlQuery.addScalar("idFilialDestinoOperacional",Hibernate.LONG);
				sqlQuery.addScalar("fiop_sgFilial", Hibernate.STRING);				
				sqlQuery.addScalar("fiop_pessoa_nmFantasia",Hibernate.STRING);
				sqlQuery.addScalar("nrNotaFiscal",Hibernate.LONG);
				sqlQuery.addScalar("lome_idLocalizacaoMercadoria",Hibernate.LONG);
				sqlQuery.addScalar("lome_dsLocalizacaoMercadoria",Hibernate.STRING);
			}
		};
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(100), paramValues.toArray(), confSql).getList();
	}
	private StringBuffer createDoctoServicoQuery(TypedFlatMap criteria, List<Object> paramValues) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT dose.id_docto_servico as idDoctoServico,\n ");
		hql.append("	   dose.tp_documento_servico as tpDocumentoServico,\n ");
		hql.append("	   filial_origem.sg_filial as filial_origem_sgfilial,\n ");
		hql.append("	   dose.nr_docto_servico as nrDoctoServico,\n ");
		hql.append("	   pessoa_remetente.tp_identificacao as pere_tpIdentificacao,\n ");
		hql.append("	   pessoa_remetente.nr_identificacao as pere_nrIdentificacao,\n ");
		hql.append("	   pessoa_remetente.nm_pessoa as pere_nmPessoa,\n ");
		hql.append("	   pessoa_destinatario.tp_identificacao as pede_tpIdentificacao,\n ");
		hql.append("	   pessoa_destinatario.nr_identificacao as pede_nrIdentificacao,\n ");
		hql.append("	   pessoa_destinatario.nm_pessoa as pede_nmPessoa,\n ");
		hql.append("	   dose.dt_prev_entrega as dtPreventrega,\n ");
		hql.append("	   dose.id_filial_destino as idFilialDestino,\n ");
		hql.append("	   clre.id_cliente as idClienteRemetente,\n ");
		hql.append("	   clde.id_cliente as idClienteDestinatario,\n ");
		hql.append("	   filial_destino_operacional.id_filial as idFilialDestinoOperacional,\n ");
		hql.append("	   filial_destino_operacional.sg_filial as fiop_sgFilial,\n "); 
		hql.append("	   pfdo.nm_fantasia as fiop_pessoa_nmFantasia,\n ");
		hql.append("	   (SELECT MIN(nofc.nr_nota_fiscal) as nrNotaFiscal\n ");
		hql.append(" 		FROM nota_fiscal_conhecimento nofc\n ");
		hql.append(" 		WHERE nofc.id_conhecimento = dose.id_docto_servico ) as nrNotaFiscal,\n ");
		hql.append("	   lome.id_localizacao_mercadoria as lome_idLocalizacaoMercadoria,\n ");
		hql.append("	   lome.ds_localizacao_mercadoria_i as lome_dsLocalizacaoMercadoria\n ");
		hql.append(" FROM docto_servico dose,\n");
		hql.append("	  filial filial_destino_operacional,\n");
		hql.append("	  filial filial_origem,\n");
		hql.append("	  filial filial_destino,\n");
		hql.append("	  cliente clre,\n");
		hql.append("	  cliente clde,\n");
		hql.append("	  pessoa pfdo,\n");  // pessoa filial destino operacional
		hql.append("	  pessoa pessoa_remetente,\n");
		hql.append("	  pessoa pessoa_destinatario,\n");
		hql.append("	  servico servico,\n");
		hql.append("	  localizacao_mercadoria lome\n");				
		hql.append(" WHERE filial_destino_operacional.id_filial(+) = dose.id_filial_destino_operacional\n");
		hql.append("  AND filial_origem.id_filial = dose.id_filial_origem\n");
		hql.append("  AND filial_destino.id_filial(+) = dose.id_filial_destino\n");
		hql.append("  AND lome.id_localizacao_mercadoria(+) = dose.id_localizacao_mercadoria\n");
		hql.append("  AND clre.id_cliente(+) = dose.id_cliente_remetente\n");
		hql.append("  AND clde.id_cliente(+) = dose.id_cliente_destinatario\n");
		hql.append("  AND clde.id_cliente = dose.id_cliente_destinatario\n");
		hql.append("  AND pfdo.id_pessoa = filial_destino_operacional.id_filial\n");
		hql.append("  AND pessoa_remetente.id_pessoa = clre.id_cliente\n");
		hql.append("  AND pessoa_destinatario.id_pessoa = clde.id_cliente\n");
		hql.append("  AND servico.id_servico(+) = dose.id_servico\n"); 

		Boolean blAgendamento = criteria.getBoolean("blAgendamento");
		if(blAgendamento != null) {
			if(blAgendamento) {
				hql.append(" AND (EXISTS(\n");
				hql.append("   SELECT 1\n");
				hql.append("     FROM serv_adicional_doc_serv sads\n");
				hql.append("    WHERE sads.id_Docto_Servico = dose.id_Docto_Servico\n");
				hql.append("      AND sads.id_Servico_Adicional = ? \n");
				hql.append("   )\n");
				hql.append(" )\n");
			} else {
				hql.append(" AND (NOT EXISTS(SELECT 1 \n");
				hql.append("                 FROM serv_adicional_doc_serv sads \n");
				hql.append("                 WHERE sads.id_Docto_Servico = dose.id_Docto_Servico \n");
				hql.append("                   AND sads.id_Servico_Adicional = ? \n");
				hql.append("				) \n");
				hql.append("     )\n");
			}
		}
		paramValues.add(criteria.getLong("ADIC_SERV"));

		hql.append(" AND (\n");
		hql.append("      (NOT EXISTS(SELECT 1 \n");
		hql.append("                 FROM agendamento_docto_servico agds \n");
		hql.append("                 WHERE agds.id_Docto_Servico = dose.id_Docto_Servico \n"); 
		hql.append("				) \n");
		hql.append("      )\n");
		hql.append("      OR\n");
		hql.append("       (\n");
		hql.append("        (EXISTS(SELECT 1 \n");
		hql.append("                 FROM agendamento_docto_servico agds, \n");
		hql.append("                 agendamento_entrega agen \n");
		hql.append("                 WHERE agds.id_Docto_Servico = dose.id_Docto_Servico \n"); 
		hql.append("                   AND agen.id_agendamento_entrega = agds.id_agendamento_entrega\n"); 
		hql.append("                   AND agen.tp_situacao_agendamento = 'C'\n"); 
		hql.append("				) \n");
		hql.append("        )\n");
		hql.append("        AND\n");
		hql.append("        (NOT EXISTS(SELECT 1 \n");
		hql.append("                 FROM agendamento_docto_servico agds, \n");
		hql.append("                 agendamento_entrega agen \n");
		hql.append("                 WHERE agds.id_Docto_Servico = dose.id_Docto_Servico \n"); 
		hql.append("                   AND agen.id_agendamento_entrega = agds.id_agendamento_entrega\n"); 
		hql.append("                   AND agen.tp_situacao_agendamento = 'A'\n"); 
		hql.append("				) \n");
		hql.append("        )\n");
		hql.append("       )\n");
		hql.append("     )\n");

		hql.append(" AND (NOT EXISTS(SELECT 1\n");
		hql.append("	   		 FROM evento_documento_servico eds\n");
		hql.append("	   			INNER JOIN evento evn ON(eds.id_evento = evn.id_evento)\n");
		hql.append("	   		 WHERE eds.id_docto_servico = dose.id_Docto_Servico\n");
		hql.append("	    	   AND eds.bl_evento_cancelado = 'N'\n");
		hql.append("	    	   AND evn.cd_evento = '21'\n");
		hql.append("	    	   AND eds.dh_evento = (\n");
		hql.append("	      	 		SELECT MAX(eds2.dh_evento) FROM evento_documento_servico eds2\n");
		hql.append("	      				INNER JOIN evento evn2 ON(eds2.id_evento = evn2.id_evento)\n");
		hql.append("	      			WHERE eds2.id_docto_servico = dose.id_docto_servico\n");
		hql.append("	      			  AND eds2.bl_evento_cancelado = 'N'\n");
		hql.append("	      			)\n");
		hql.append("			 ) \n");
		hql.append("	 )\n");

		hql.append(" AND ( ( dose.id_Filial_origem = ?) or ( dose.id_Filial_destino_operacional = ?) ) " );
		paramValues.add(criteria.getLong("filialAgendamento.idFilial"));
		paramValues.add(criteria.getLong("filialAgendamento.idFilial"));

		if (criteria.getLong("localizacaoMercadoria.idLocalizacaoMercadoria") != null) {
			hql.append(" AND lome.id_localizacao_mercadoria = ? ");
			paramValues.add(criteria.getLong("localizacaoMercadoria.idLocalizacaoMercadoria"));
		}
		if (criteria.getLong("filialOrigem.idFilial") != null) {
			hql.append(" AND dose.id_filial_origem = ? ");
			paramValues.add(criteria.getLong("filialOrigem.idFilial"));
		}
		if (criteria.getLong("filialDestino.idFilial") != null) {
			hql.append(" AND dose.id_Filial_destino = ? ");
			paramValues.add(criteria.getLong("filialDestino.idFilial"));
		}
		if (criteria.getLong("remetente.idCliente") != null) {
			hql.append(" AND dose.id_Cliente_remetente = ? ");
			paramValues.add(criteria.getLong("remetente.idCliente"));
		}
		if (criteria.getLong("destinatario.idCliente") != null) {
			hql.append(" AND dose.id_Cliente_destinatario = ? ");
			paramValues.add(criteria.getLong("destinatario.idCliente"));
		}
		if (criteria.getLong("tipoServico.idTipoServico") != null) {
			hql.append(" AND servico.id_Tipo_Servico = ? ");
			paramValues.add(criteria.getString("tipoServico.idTipoServico"));
		}
		if (criteria.getLong("filialByIdFilialOrigem.idFilial") != null) {
			hql.append(" AND filial_origem.id_Filial = ? ");
			paramValues.add(criteria.getLong("filialByIdFilialOrigem.idFilial"));
		}
		if (StringUtils.isNotBlank(criteria.getString("doctoServico.tpDocumentoServico"))) {
			hql.append(" AND DOSE.TP_DOCUMENTO_SERVICO = ? " );
			paramValues.add(criteria.getString("doctoServico.tpDocumentoServico"));
		} else {
			hql.append(" AND DOSE.TP_DOCUMENTO_SERVICO IN ('CRT','CTR','MDA','NFT')");
		}
		if (criteria.getLong("idDoctoServico") != null) {
			hql.append(" AND dose.id_Docto_Servico = ? ");
			paramValues.add(criteria.getLong("idDoctoServico"));
		}
		hql.append(" \nORDER BY dose.dt_Prev_Entrega, \ndose.tp_Documento_Servico, \nFilial_Origem.sg_Filial, \ndose.nr_Docto_Servico ");
		return hql;
	}

	public Boolean findManifestoSemOcorrenciaLancada(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(" maed.idManifestoEntregaDocumento " +
						  " from   ManifestoEntregaDocumento maed " +
						  " where  maed.doctoServico.idDoctoServico = " + idDoctoServico + " " +
						  " and    maed.ocorrenciaEntrega is null " +
						  " and    maed.tpSituacaoDocumento <> 'CANC'");

		List list = getAdsmHibernateTemplate().find(sql.getSql());
		if (list != null && list.size() > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}			
	}

	public AgendamentoDoctoServico findByIdCustom(Long id) {
		AgendamentoDoctoServico agendamentoDoctoServico = (AgendamentoDoctoServico)getAdsmHibernateTemplate().get(AgendamentoDoctoServico.class, id);
		if (agendamentoDoctoServico.getAgendamentoEntrega().getTurno() != null) {
			Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getTurno().getFilial().getPessoa());
		}
		
		Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getFilial().getPessoa());
		
		if (agendamentoDoctoServico.getAgendamentoEntrega().getFilial().getTurnos() != null) {
			Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getFilial().getTurnos());
		}

		Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getUsuarioByIdUsuarioCriacao());

		if (agendamentoDoctoServico.getAgendamentoEntrega().getMotivoAgendamentoByIdMotivoCancelamento() != null) {
			Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getMotivoAgendamentoByIdMotivoCancelamento());			
		}
		
		if (agendamentoDoctoServico.getAgendamentoEntrega().getMotivoAgendamentoByIdMotivoReagendamento() != null) {
			Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getMotivoAgendamentoByIdMotivoReagendamento());			
		}
		
		if (agendamentoDoctoServico.getAgendamentoEntrega().getReagendamento() != null) {
			Hibernate.initialize(agendamentoDoctoServico.getAgendamentoEntrega().getReagendamento());
		}
		
		agendamentoDoctoServico.getIdAgendamentoDoctoServico();
		
		Hibernate.initialize(agendamentoDoctoServico.getDoctoServico());
		DoctoServico doctoServico = agendamentoDoctoServico.getDoctoServico(); 
		
		if (doctoServico.getServico() != null) {
			Hibernate.initialize(doctoServico.getServico().getTipoServico());
		}
		
		if (doctoServico.getFilialDestinoOperacional() != null) {
			Hibernate.initialize(doctoServico.getFilialDestinoOperacional().getPessoa());
		}
		
		if (doctoServico.getFilialByIdFilialOrigem() != null) {
			Hibernate.initialize(doctoServico.getFilialByIdFilialOrigem());
		}
		
		if (doctoServico.getClienteByIdClienteDestinatario() != null) {
			Hibernate.initialize(doctoServico.getClienteByIdClienteDestinatario().getPessoa());
		}
		
		if (doctoServico.getClienteByIdClienteRemetente() != null) {
			Hibernate.initialize(doctoServico.getClienteByIdClienteRemetente().getPessoa());
		}
		
		if (doctoServico.getManifestoEntregaDocumentos() != null) {
			for (Iterator iterator = doctoServico.getManifestoEntregaDocumentos().iterator(); iterator.hasNext();) {
				ManifestoEntregaDocumento manifestoEntregaDocumento = (ManifestoEntregaDocumento) iterator.next();
				Hibernate.initialize(manifestoEntregaDocumento.getOcorrenciaEntrega());
				Hibernate.initialize(manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getControleCarga());
				Hibernate.initialize(manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem());
			}
		}
				
		return agendamentoDoctoServico;
	}		

	public List findListDoctoServicoByIdAgendamentoEntrega(TypedFlatMap criteria) {

		Map parameters = new HashMap();
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" \nselect \n" +
				"	   dose.id_docto_servico as idDoctoServico,\n " +
				"	   dose.tp_documento_servico as tpDocumentoServico,\n " +
				"	   filial_origem.sg_filial as filial_origem_sgfilial,\n " +
				"	   dose.nr_docto_servico as nrDoctoServico,\n " +
				"	   (select min(nofc.nr_nota_fiscal) as nrNotaFiscal\n " +
				" 			   from nota_fiscal_conhecimento nofc\n " +
				" 			   where nofc.id_conhecimento = dose.id_docto_servico ) as nrNotaFiscal,\n " +
				"		CASE WHEN " +
				"				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
				"			THEN COCA.NR_CONTROLE_CARGA " +
				"			ELSE NULL END " +
				"			AS nrControleCarga,\n " + // 38
				"		CASE WHEN " +
				"				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
				"			THEN maen.nr_manifesto_entrega " +
				"			ELSE NULL END " +
				"			as nrManifestoEntrega,\n " +
//				"	   decode(ocen.tp_ocorrencia, 'E', maed.dh_ocorrencia, null) as dhOcorrencia,\n " +				
				"		CASE WHEN " +
				"				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
		   		"			THEN maed.dh_ocorrencia " +
				"			ELSE NULL END " +
		   		"           as dhOcorrencia,\n " +				
				"		CASE WHEN " +
				"				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
				"			THEN filial_origem_coca.sg_filial " +
				"			ELSE NULL END " +
				"			as filial_origem_coca_sgfilial,\n " +
				"		CASE WHEN " +
				"				((AGEN.TP_SITUACAO_AGENDAMENTO in ('A', 'F')) AND (ocen.tp_ocorrencia = 'E') OR (maed.id_ocorrencia_entrega IS NULL)) " +
				"			THEN filial_maen.sg_filial " +
				"			ELSE NULL END " +
				"			as filial_maen_sgfilial\n " +
				" from agendamento_docto_servico agds,\n" +
				"      AGENDAMENTO_ENTREGA		AGEN,\n" +
				"	   docto_servico dose,\n" +
				"	   filial filial_origem,\n" +
				"	   filial filial_origem_coca,\n" +
				"	   filial filial_maen,\n" +
				"	   manifesto_entrega_documento maed,\n" +
				"	   manifesto_entrega maen,\n" +
				"	   manifesto mani,\n" +
				"	   controle_carga coca,\n" +
				"	   ocorrencia_entrega ocen\n" +
				" where AGDS.ID_AGENDAMENTO_ENTREGA = AGEN.ID_AGENDAMENTO_ENTREGA " +
				" and agds.id_docto_servico = dose.id_docto_servico " +
				" and maed.id_docto_servico(+) = dose.id_docto_servico " +
				" and maen.id_manifesto_entrega(+) = maed.id_manifesto_entrega " +
				" and filial_maen.id_filial(+) = maen.id_filial " +
				" and mani.id_manifesto(+) = maen.id_manifesto_entrega " +
				" and coca.id_controle_carga(+) = mani.id_controle_carga " +
				" and filial_origem_coca.id_filial(+) = coca.id_filial_origem " +
				" and ocen.id_ocorrencia_entrega(+) = maed.id_ocorrencia_entrega " +
				" AND (MAEN.DH_EMISSAO IS NULL " +
				"         OR MAEN.DH_EMISSAO = ( SELECT MAX(MAEN2.DH_EMISSAO) " +
				"		   FROM MANIFESTO_ENTREGA_DOCUMENTO	MAED2," +
				"		   MANIFESTO_ENTREGA MAEN2" +
				"		   WHERE MAED2.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO" +
				"          AND   MAEN2.ID_MANIFESTO_ENTREGA = MAED2.ID_MANIFESTO_ENTREGA" +				
				"		 ) " +
				" ) " +
				" and filial_origem.id_filial = dose.id_filial_origem\n" );
		
		if (criteria.getLong("idAgendamentoEntrega") != null) {
			hql.append(" and agds.id_agendamento_entrega = " + criteria.getLong("idAgendamentoEntrega"));
		}		
		
		hql.append(" \norder by dose.dt_Prev_Entrega, \ndose.tp_Documento_Servico, \nFilial_Origem.sg_Filial, \ndose.nr_Docto_Servico ");
		
		hql.insert(0,"SELECT * FROM (").append(") ");
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idDoctoServico",Hibernate.LONG);				
				sqlQuery.addScalar("tpDocumentoServico",Hibernate.STRING);				
				sqlQuery.addScalar("filial_origem_sgfilial",Hibernate.STRING);				
				sqlQuery.addScalar("nrDoctoServico",Hibernate.LONG);				
				sqlQuery.addScalar("nrNotaFiscal",Hibernate.LONG);
				sqlQuery.addScalar("nrControleCarga",Hibernate.LONG);
				sqlQuery.addScalar("nrManifestoEntrega",Hibernate.LONG);
				sqlQuery.addScalar("dhOcorrencia",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("filial_origem_coca_sgfilial",Hibernate.STRING);
				sqlQuery.addScalar("filial_maen_sgfilial",Hibernate.STRING);
			}
			
		};
		
		return getAdsmHibernateTemplate().findPaginatedBySql(hql.toString(), Integer.valueOf(1), Integer.valueOf(10000), parameters, confSql).getList();
	}
	
	/**
	 * Busca um Agendamento de Documento de Serviço a partir dos parâmtetros informados.
	 * 
	 * @param idDoctoServico Identificador do Documento de Serviço.
	 * @param tpAgendamento Tipo de Agendamento
	 * @param dhContato Data/hora do contato. Este parâmetro é opcional.
	 * @return uma instância de AgendamentoDoctoServico caso encontrado. Senão, retora null.
	 */
	public AgendamentoDoctoServico findAgendamentoDoctoServico(Long idDoctoServico ,
			String tpAgendamento, DateTime dhContato) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"ADS");
		// join com agendamento de entrega
		dc.createAlias("ADS.agendamentoEntrega","AE");
		dc.add(Restrictions.eq("ADS.doctoServico.id",idDoctoServico));
		dc.add(Restrictions.eq("AE.tpAgendamento",tpAgendamento));
		
		if (dhContato != null) {
			dc.add(Restrictions.eq("AE.dhContato.value",dhContato));
		}
		
		return (AgendamentoDoctoServico) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Busca uma lista de Agendamento de Documento de Serviço NÃO Cancelados a partir do idDoctoServico
	 * @param idDoctoServico
	 * @return lista de Agendamento de Documento de Serviço
	 */
    public List<AgendamentoDoctoServico> findAgendamentoByIdDoctoServicoJoinFilial(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads join fetch ads.agendamentoEntrega ae "  
				+ " join fetch ae.filial f "
				+ " join fetch f.pessoa p ");		
		hql.addCriteria("ads.doctoServico.id", "=", idDoctoServico);
		hql.addCriteria("ae.tpSituacaoAgendamento", "!=", "C");
		hql.addOrderBy("ae.dtAgendamento", "desc");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
    
    /**
	 * Busca uma lista de Agendamento de Documento de Serviço NÃO Cancelados a partir do idDoctoServico
	 * @param idDoctoServico
	 * @return lista de Agendamento de Documento de Serviço
	 */
    @SuppressWarnings("unchecked")
	public List<AgendamentoDoctoServico> findNaoCanceladosByIdDoctoServico(Long idDoctoServico) {
		StringBuilder sb = new StringBuilder();
    	sb.append("from " + getPersistentClass().getName() + " agdo ");
    	sb.append("join fetch agdo.agendamentoEntrega agen ");
    	sb.append("where ");
    	sb.append("	   agdo.doctoServico.idDoctoServico = ? ");
    	sb.append("and agen.tpSituacaoAgendamento != 'C' ");
    	sb.append("order by agen.dtAgendamento desc ");		
		
		return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{idDoctoServico});
    }
    
    @SuppressWarnings("unchecked")
	public String findDataAgendamentoByIdDoctoServico(Long idDoctoServico) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select agen.hrPreferenciaInicial, agen.hrPreferenciaFinal, agen.dtAgendamento, turno.dsTurno ");
    	sb.append("from " + getPersistentClass().getName() + " agdo ");
    	sb.append("join  agdo.agendamentoEntrega agen ");
    	sb.append("left outer join agen.turno turno ");
    	sb.append("where ");
    	sb.append("	   agdo.doctoServico.idDoctoServico = ? ");
    	sb.append("and agen.tpAgendamento != 'TA' ");
    	sb.append("and agen.tpSituacaoAgendamento = 'A' ");
    	sb.append("order by agen.dtAgendamento desc ");
		
    	List<Object> valores = getAdsmHibernateTemplate().find(sb.toString(), new Object[]{idDoctoServico});
    	
    	if (CollectionUtils.isEmpty(valores)) {
    		return null;
    	}
    	
    	Object[] horarios = (Object[]) valores.get(0);
    	
    	String progrDtTurno = "";
		if (horarios[2] != null) {
			YearMonthDay data = (YearMonthDay) horarios[2];
			progrDtTurno = JTFormatUtils.format(data);
			if (horarios[3] != null)
				progrDtTurno = progrDtTurno + " - " + horarios[3].toString();
		} else if (horarios[3] != null) {
			progrDtTurno = horarios[3].toString();
		}
    	
    	String retorno = "";
    	String progrHorario = "";
		if (horarios[0] != null) {
			TimeOfDay hrPreferenciaInicial = (TimeOfDay) horarios[0];
			progrHorario = JTFormatUtils.format(hrPreferenciaInicial);
			if (horarios[1] != null) {
				TimeOfDay hrPreferenciaFinal = (TimeOfDay) horarios[1];
				progrHorario = progrHorario + " às " + JTFormatUtils.format(hrPreferenciaFinal);
			}
		} else if (horarios[1] != null) {
			TimeOfDay hrPreferenciaFinal = (TimeOfDay) horarios[1];
			progrHorario = JTFormatUtils.format(hrPreferenciaFinal);
		}
		if (StringUtils.isNotEmpty(progrHorario)) {
			retorno = progrDtTurno + " " + progrHorario;
		} else {
			retorno = progrDtTurno;
		}
    	
		return retorno;
    }
    
	public AgendamentoDoctoServico findAgendamentoDoctoServicoByIdAgendamentoEntregaAndIdDoctoServico(Long idAgendamentoEntrega, Long idDoctoServico) {

		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(AgendamentoDoctoServico.class.getName() + " ads");
		hql.addCriteria("ads.doctoServico.idDoctoServico", "=", idDoctoServico);
		hql.addCriteria("ads.agendamentoEntrega.idAgendamentoEntrega", "=", idAgendamentoEntrega);
		hql.addCriteria("ads.agendamentoEntrega.tpSituacaoAgendamento", "=", "A");
		return (AgendamentoDoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
 		
	}
}