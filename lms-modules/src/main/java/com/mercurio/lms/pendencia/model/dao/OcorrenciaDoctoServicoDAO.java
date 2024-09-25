package com.mercurio.lms.pendencia.model.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.LiberacaoBloqueio;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

import jersey.repackaged.com.google.common.base.Joiner;

/**
 * DAO pattern.   
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean 
 */
public class OcorrenciaDoctoServicoDAO extends BaseCrudDao<OcorrenciaDoctoServico, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @SuppressWarnings("rawtypes")
    protected final Class getPersistentClass() {
        return OcorrenciaDoctoServico.class;
    }

    
    @SuppressWarnings("rawtypes")
    public List findVerificaComunicadoApreensao(Long idDoctoServico) {
    	DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class);
    	dc.add( Restrictions.eq("doctoServico.idDoctoServico", idDoctoServico) );
    	dc.add( Restrictions.isNull("dhLiberacao") );
    	List retorno = findByDetachedCriteria(dc);
    	return retorno;
    }
   
    public TypedFlatMap findByIdToComunicadoApreensaoAoCliente(Long idOcorrenciaDoctoServico) {
        StringBuilder sb = new StringBuilder();

    	sb.append(" select new map(");
    	sb.append("remetente.idPessoa as doctoServico_clienteByIdClienteRemetente_pessoa_idPessoa, ");
		sb.append("remetente.nrIdentificacao as doctoServico_clienteByIdClienteRemetente_pessoa_nrIdentificacao, ");
		sb.append("remetente.tpIdentificacao as doctoServico_clienteByIdClienteRemetente_pessoa_tpIdentificacao, ");
		sb.append("remetente.nmPessoa as doctoServico_clienteByIdClienteRemetente_pessoa_nmPessoa, " );
		sb.append("remetente.dsEmail as doctoServico_clienteByIdClienteRemetente_pessoa_dsEmail, ");
		
		sb.append("destinatario.idPessoa as doctoServico_clienteByIdClienteDestinatario_pessoa_idPessoa, ");
		sb.append("destinatario.nrIdentificacao as doctoServico_clienteByIdClienteDestinatario_pessoa_nrIdentificacao, ");
		sb.append("destinatario.tpIdentificacao as doctoServico_clienteByIdClienteDestinatario_pessoa_tpIdentificacao, ");
		sb.append("destinatario.nmPessoa as doctoServico_clienteByIdClienteDestinatario_pessoa_nmPessoa, ");
		sb.append("destinatario.dsEmail as doctoServico_clienteByIdClienteDestinatario_pessoa_dsEmail, ");
		
		sb.append("ocorrenciaPendencia.cdOcorrencia as ocorrenciaPendenciaByIdOcorBloqueio_cdOcorrencia, ");
		sb.append("ocorrenciaPendencia.dsOcorrencia as ocorrenciaPendenciaByIdOcorBloqueio_dsOcorrencia, ");
		sb.append("ocorrenciaDoctoServico.dhBloqueio as dhBloqueio,  ");
		
		sb.append("comunicadoApreensao.idComunicadoApreensao as comunicadoApreensao_idComunicadoApreensao, ");
		sb.append("comunicadoApreensao.nrTermoApreensao as comunicadoApreensao_nrTermoApreensao, ");
		sb.append("comunicadoApreensao.dtOcorrencia as comunicadoApreensao_dtOcorrencia, ");
		sb.append("moeda.idMoeda as comunicadoApreensao_moeda_idMoeda, ");
		sb.append("moeda.sgMoeda as comunicadoApreensao_moeda_sgMoeda, ");
		sb.append("moeda.dsSimbolo as comunicadoApreensao_moeda_dsSimbolo, ");
		sb.append("comunicadoApreensao.vlMulta as comunicadoApreensao_vlMulta, ");
		sb.append("comunicadoApreensao.dsMotivoAlegado as comunicadoApreensao_dsMotivoAlegado");
		sb.append(" )");
		
		sb.append(" from " + OcorrenciaDoctoServico.class.getName() + " ocorrenciaDoctoServico ");
		sb.append(" join ocorrenciaDoctoServico.doctoServico doctoServico ");
		sb.append(" left join ocorrenciaDoctoServico.comunicadoApreensao comunicadoApreensao ");
		sb.append(" left join comunicadoApreensao.moeda moeda ");

		sb.append(" join doctoServico.clienteByIdClienteRemetente clienteRemetente ");
		sb.append(" join clienteRemetente.pessoa remetente");
		
		sb.append(" join doctoServico.clienteByIdClienteDestinatario clienteDestinatario ");
		sb.append(" join clienteDestinatario.pessoa destinatario");
		
		sb.append(" join ocorrenciaDoctoServico.ocorrenciaPendenciaByIdOcorBloqueio ocorrenciaPendencia");
		
		sb.append(" where ");
		sb.append(" ocorrenciaDoctoServico.idOcorrenciaDoctoServico = ?");
		
    	List<Long> param = new ArrayList<Long>();
    	param.add( idOcorrenciaDoctoServico );
    	
    	@SuppressWarnings("rawtypes")
    	Map l  = (Map)getAdsmHibernateTemplate().findUniqueResult(sb.toString(), param.toArray());
    	TypedFlatMap retorno = new AliasToTypedFlatMapResultTransformer().transformeTupleMap(l);
    		
    	return retorno;
    }
    
    /**
     * Solicitação CQPRO00005860 da Integração.
     * Retorna uma instância de OcorrenciaDoctoServico com base em uma ocorrencia de bloqueio e sua data/hora;
     *
     * @param idOcorrenciaPendenciaByIdOcorBloqueio
     * @param dhBloqueio
     * @return
     */
    public OcorrenciaDoctoServico findByOcorrenciaPendenciaByIdOcorBloqueio( Long idOcorrenciaPendenciaByIdOcorBloqueio, DateTime dhBloqueio ){
    	DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
    	dc.createAlias("ods.ocorrenciaPendenciaByIdOcorBloqueio", "obl");
    	dc.add(Restrictions.eq("obl.id", idOcorrenciaPendenciaByIdOcorBloqueio));
    	dc.add(Restrictions.eq("ods.dhBloqueio.value", dhBloqueio.toDate()));
    	return (OcorrenciaDoctoServico)getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    /**
     * Return a <code>OcorrenciaDoctoServico</code> by the id of <code>DoctoServico</code>.
     * 
     * @param idDoctoServico
     * @return OcorrenciaDoctoServico
     */
    @SuppressWarnings("rawtypes")
    public OcorrenciaDoctoServico findLastOcorrenciaDoctoServicoByIdDoctoServico(Long idDoctoServico) {
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods")
    		.add(Restrictions.eq("ods.doctoServico.id", idDoctoServico))
    		.addOrder(Order.desc("ods.dhBloqueio.value"));
    	List ocorrenciasDoctoServico = this.findByDetachedCriteria(detachedCriteria);
        if (!ocorrenciasDoctoServico.isEmpty()) {
    		return (OcorrenciaDoctoServico)ocorrenciasDoctoServico.get(0);
    	}
    	return null;    	
    }
    
    @SuppressWarnings("rawtypes")
    public Integer getRowCountDoctoServicoWithBloqueio(Long idDoctoServico, List dates){
    	SqlTemplate hql = new SqlTemplate();
    	    	
        hql.addFrom(new StringBuilder(OcorrenciaDoctoServico.class.getName()).append(" ODS ")
    			.append("INNER JOIN ODS.doctoServico DS ").toString());
    	
    	hql.addCriteria("DS.id","=", idDoctoServico);
    	
        StringBuilder createCriteria = new StringBuilder();
    	String token = null;
    	
    	for(Iterator i = dates.iterator(); i.hasNext();) {
    		Object temp = i.next();
    		DateTime date;
            if (temp instanceof DateTime) {
    			date = (DateTime)temp;
            } else {
                date = JTDateTimeUtils.yearMonthDayToDateTime((YearMonthDay) temp);
            }
    		
            if (token == null) {
    			token = " OR ";
            } else {
    			createCriteria.append(token);
            }
    		
    		createCriteria.append(" ( ? between ODS.dhBloqueio.value  AND nvl(ODS.dhLiberacao.value, ?)) ");
    		
    		hql.addCriteriaValue(date);
    		hql.addCriteriaValue(date);
    	}
    	
    	hql.addCustomCriteria(createCriteria.toString());
    		
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
    }

    public boolean ocorrenciaHasBloqueio(Long idDoctoServico, DateTime data) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("1");
    	hql.addFrom(OcorrenciaDoctoServico.class.getName()+ " ods " +
    			"left outer join ods.doctoServico ds");
    	hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
    	hql.addCustomCriteria("ods.dhLiberacao.value is not null");
    	hql.addCriteria("ods.dhBloqueio.value", "<=", data);
    	hql.addCriteria("ods.dhLiberacao.value", ">=", data);
    	
        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
    }
    
    
    /**
     * verifica se existe alguma ocorrencia para o documento de servico 
     *
     * @param idDoctoServico
     * @return
     */
    public boolean findOcorDsByIdDoctoServico(Long idDoctoServico) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("1");
    	hql.addFrom(OcorrenciaDoctoServico.class.getName()+ " ods " +
    			"left outer join ods.doctoServico ds");
    	hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
    	
        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
    }
    
    /**
     * Busca a ocorrenciaDoctoServico ocorrencia com dhLiberacao vazio (ocorrencia em aberto)
     * para o documento de servico informado. Deverá existir somente uma. 
     *
     * @param idDoctoServico
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(Long idDoctoServico) {
    	DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
    	dc.createAlias("ods.doctoServico", "ds");
    	dc.add(Restrictions.eq("ds.id", idDoctoServico));
    	dc.add(Restrictions.isNull("ods.dhLiberacao.value"));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    /**
     * Busca a ocorrenciaDoctoServico ocorrencia com dhLiberacao vazio (ocorrencia em aberto)
     * para o documento de servico informado. Deverá existir somente uma para cada id.
     *
     * @param idsDoctoServico
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findOcorrenciaDoctoServicoEmAbertoByIdsDoctoServico(List<Long> idsDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
        dc.createAlias("ods.doctoServico", "ds");
        dc.add(Restrictions.in("ds.id", idsDoctoServico));
        dc.add(Restrictions.isNull("ods.dhLiberacao.value"));
        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public List findSimplifiedOcorrenciaDoctoServicoEmAbertoByIdsDoctoServico(List<Long> idsDoctoServico) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID_OCORRENCIA_DOCTO_SERVICO,ID_DOCTO_SERVICO FROM OCORRENCIA_DOCTO_SERVICO WHERE ID_DOCTO_SERVICO IN (");
        sql.append(Joiner.on(",").join(idsDoctoServico));
        sql.append(") AND OCORRENCIA_DOCTO_SERVICO.DH_LIBERACAO IS NULL");

        Map<String, Object> params = new HashMap<String, Object>();

        ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_OCORRENCIA_DOCTO_SERVICO", Hibernate.LONG);
                sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
            }
        };

        return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    }

    /**
     * SELECT ID_DOCTO_SERVICO,COUNT(ID_DOCTO_SERVICO) AS MORE_THAN_ONE_REG FROM OCORRENCIA_DOCTO_SERVICO WHERE ID_DOCTO_SERVICO IN (
     * <p>
     * ) AND OCORRENCIA_DOCTO_SERVICO.DH_LIBERACAO IS NULL GROUP BY ID_DOCTO_SERVICO ORDER BY MORE_THAN_ONE_REG DESC;
     *
     * @param idDoctoServico
     * @param cdOcorrencias
     * @param isInOperator
     * @return
     */

	@SuppressWarnings("unchecked")
	public List<OcorrenciaDoctoServico> findOcorrenciaDoctoServicoByCdOcorrencia(
			long idDoctoServico, Short[] cdOcorrencias, boolean isInOperator) {		
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
    	dc.createAlias("ods.doctoServico", "ds");
    	dc.createAlias("ods.ocorrenciaPendenciaByIdOcorBloqueio", "op");
    	dc.createAlias("op.liberacaoBloqueiosByIdOcorrenciaBloqueio", "ob");
    	
    	dc.setFetchMode("op", FetchMode.JOIN);
    	dc.setFetchMode("ob", FetchMode.JOIN);    	
    	
    	dc.add(Restrictions.eq("ds.id", idDoctoServico));
    	dc.add(Restrictions.isNull("ods.ocorrenciaPendenciaByIdOcorLiberacao.id"));
    	
    	if(isInOperator){
    		dc.add(Restrictions.in("op.cdOcorrencia", cdOcorrencias));
        } else {
    		dc.add(Restrictions.not(Restrictions.in("op.cdOcorrencia", cdOcorrencias)));
    	}    	

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<OcorrenciaDoctoServico> findOcorrenciaBloqueioDoctoServicoByCdOcorrencia(
			long idDoctoServico, Short[] cdOcorrencias) {		
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
    	dc.createAlias("ods.doctoServico", "ds");
    	dc.createAlias("ods.ocorrenciaPendenciaByIdOcorBloqueio", "op");
    	dc.setFetchMode("op", FetchMode.JOIN);    	
    
    	dc.add(Restrictions.eq("ds.id", idDoctoServico));
    	dc.add(Restrictions.in("op.cdOcorrencia", cdOcorrencias));

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<OcorrenciaDoctoServico>  findBloqueioDoctoServicoPorPeriodoLiberacao(Long idDoctoServico, YearMonthDay dtInicial, YearMonthDay dtFinal) {
        StringBuilder sb = new StringBuilder();

		sb.append(" from " + OcorrenciaDoctoServico.class.getName() + " ods ");
		sb.append(" where ");
		sb.append(" ods.doctoServico.idDoctoServico = ? ");
		sb.append(" and ((trunc(ods.dhLiberacao.value) >= ? ");
		sb.append(" and trunc(ods.dhLiberacao.value) <= ?) ");
		sb.append(" or ods.dhLiberacao.value is null) ");
		sb.append(" order by ods.dhBloqueio.value desc ");
		
    	List<Object> param = new ArrayList<Object>();
    	param.add( idDoctoServico );
    	param.add( dtInicial );
    	param.add( dtFinal );
    	
    	return (List<OcorrenciaDoctoServico>) getAdsmHibernateTemplate().find(sb.toString(), param.toArray());
    }
	
	public Boolean executeVerificarLiberacaoByCodigo(Short cdOcorrencia) {
    	List<OcorrenciaPendencia> listaLiberacao =  this.findOcorrenciaLiberacaoByOcorrenciaBloqueadaELiberadas(cdOcorrencia, new Short[]{95});
    	if (listaLiberacao != null && !listaLiberacao.isEmpty()) {
    		return Boolean.TRUE;
    	} else {
    		return Boolean.FALSE;
    	}
	}
	
	public OcorrenciaPendencia findOcorrenciaLiberacaoByOcorrenciaBloqueada(Short cdOcorrencia) {
    	List<OcorrenciaPendencia> lista = this.findOcorrenciaLiberacaoByOcorrenciaBloqueadaELiberadas(cdOcorrencia, new Short[]{91, 94, 95, 204, 203});
    	if (lista != null && !lista.isEmpty()) {
    		return lista.get(0);
    	}
    	return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<OcorrenciaPendencia> findOcorrenciaLiberacaoByOcorrenciaBloqueadaELiberadas(Short cdOcorrenciaBloqueada, Short[] cdOcorrenciasLiberadas) {
		DetachedCriteria dc = DetachedCriteria.forClass(LiberacaoBloqueio.class, "lb");
		Projection projection = Projections.property("lb.ocorrenciaPendenciaByIdOcorrenciaLiberacao");
		dc.setProjection(projection);
    	dc.createAlias("lb.ocorrenciaPendenciaByIdOcorrenciaLiberacao", "opl");
    	dc.createAlias("lb.ocorrenciaPendenciaByIdOcorrenciaBloqueio", "opb");
    	dc.setFetchMode("opl", FetchMode.JOIN);
    	dc.setFetchMode("opb", FetchMode.JOIN);
    
    	dc.add(Restrictions.eq("opb.cdOcorrencia", cdOcorrenciaBloqueada));
    	dc.add(Restrictions.in("opl.cdOcorrencia", cdOcorrenciasLiberadas));

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	
	@SuppressWarnings("rawtypes")
	public DateTime findMaxDataLiberacaoOcorrencia(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
		
		Projection projection = Projections.max("ods.dhLiberacao.value");
		
		dc.setProjection(projection);
   
    	dc.add(Restrictions.eq("ods.doctoServico.idDoctoServico", idDoctoServico));
    	
    	List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	
    	if (lista != null && !lista.isEmpty()) {
    		Timestamp tt = (Timestamp) lista.get(0);
    		
    		return new DateTime(tt.getTime());
    	}
    	return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<OcorrenciaDoctoServico> findOcorrenciaByControleCargaManifestoEntrega(Long idControleCarga){
        StringBuilder sb = new StringBuilder();

    	sb.append("select ocorrenciaDoctoServico ");
    	sb.append("from  ");
    	sb.append(OcorrenciaDoctoServico.class.getName());
    	sb.append(" ");
    	sb.append("ocorrenciaDoctoServico  ");
    	sb.append("inner join ocorrenciaDoctoServico.doctoServico doctoServico ");
    	sb.append("inner join doctoServico.manifestoEntregaDocumentos manifestoEntregaDocumentos ");
    	sb.append("inner join manifestoEntregaDocumentos.manifestoEntrega manifestoEntrega ");
    	sb.append("inner join manifestoEntrega.manifesto manifesto ");
    	sb.append("inner join manifesto.controleCarga controleCarga ");
    	sb.append("where ");
    	sb.append("controleCarga.idControleCarga = ? ");
		
    	List<Object> param = new ArrayList<Object>();
    	param.add(idControleCarga);
    	
    	return getAdsmHibernateTemplate().find(sb.toString(), param.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<OcorrenciaDoctoServico> findOcorrenciaSemLiberacaoBloqueioDoctoServicoByCdOcorrencia(
			long idDoctoServico, Short[] cdOcorrencias) {		
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaDoctoServico.class, "ods");
    	dc.createAlias("ods.doctoServico", "ds");
    	dc.add(Restrictions.isNull("ods.dhLiberacao"));
    	dc.createAlias("ods.ocorrenciaPendenciaByIdOcorBloqueio", "op");
    	dc.setFetchMode("op", FetchMode.JOIN);    	
    
    	dc.add(Restrictions.eq("ds.id", idDoctoServico));
    	dc.add(Restrictions.in("op.cdOcorrencia", cdOcorrencias));

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * Método que retorna uma lista de map com dados das ocorrencias relacionados ao documento de serviço com id igual ao
     * passado como parametro   
     * 
	 * @param idDoctoServico
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findListMapOcorrenciaByIdDoctoServico(Long idDoctoServico){
    	SqlTemplate sql = new SqlTemplate();
    	List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		
		sql.addProjection("ODS.ID_OCORRENCIA_DOCTO_SERVICO", "idOcorrenciaDoctoServico");
		sql.addProjection("TO_CHAR(ODS.DH_BLOQUEIO,'dd/mm/yy')", "dhBloqueio");
		sql.addProjection("TO_CHAR(ODS.DH_LIBERACAO,'dd/mm/yy')", "dhLiberacao");
    	    	
    	sql.addFrom("OCORRENCIA_DOCTO_SERVICO", "ODS");
    	sql.addCriteria("ODS.ID_DOCTO_SERVICO", "=", idDoctoServico);

		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idOcorrenciaDoctoServico",Hibernate.LONG);
				sqlQuery.addScalar("dhBloqueio",Hibernate.STRING);
				sqlQuery.addScalar("dhLiberacao",Hibernate.STRING);
}
		};

		List<Object[]> listRetorno = getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1),Integer.valueOf(100000),sql.getCriteria(),csq).getList();
		//criado para fazer o parse da data que vem como string do banco para o date para dai então construir o yearmonthday evitando aplicação de fuso horario e timezone na conversão
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        if (listRetorno == null) {
            return retorno;
        }
			for(Object[] coluna : listRetorno) {
				Map<String, Object> mapaColuna = new HashMap<String, Object>();
				mapaColuna.put("idOcorrenciaDoctoServico", coluna[0]);
				
				YearMonthDay dhBloqueio = null;
				YearMonthDay dhLiberacao = null;
				if(coluna[1] != null) {
					try {
						dhBloqueio = new YearMonthDay(sdf.parse((String) coluna[1]));
					} catch (ParseException e) {
						log.error(e);
						throw new RuntimeException("Erro ao converter data " + dhBloqueio);
					}
				}
				if(coluna[2] != null) {
					try {
						dhLiberacao = new YearMonthDay(sdf.parse((String) coluna[2]));
					} catch (ParseException e) {
						log.error(e);
						throw new RuntimeException("Erro ao converter data " + dhLiberacao);
					}
				}
				
				mapaColuna.put("dhBloqueio", dhBloqueio);
				mapaColuna.put("dhLiberacao", dhLiberacao);
				
				retorno.add(mapaColuna);
			}

		return retorno;    	
	}
}

