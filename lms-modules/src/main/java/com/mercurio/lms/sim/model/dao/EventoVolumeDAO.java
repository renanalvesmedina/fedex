package com.mercurio.lms.sim.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.sim.model.EventoVolume;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoVolumeDAO extends BaseCrudDao<EventoVolume, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<EventoVolume> getPersistentClass() {
		return EventoVolume.class;
	}

	public List<EventoVolume> findByEventoByVolume(Long idEvento, Long idVolume) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("evento.idEvento", idEvento));
		dc.add(Restrictions.eq("volumeNotaFiscal.idVolumeNotaFiscal", idVolume));
		dc.add(Restrictions.eq("blEventoCancelado", Boolean.FALSE));
		dc.addOrder(Order.desc("dhEvento"));

		return findByDetachedCriteria(dc);
	}

	public List<EventoVolume> findEventoVolume(Long idVolume, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.volumeNotaFiscal.idVolumeNotaFiscal", idVolume));;
		dc.add(Restrictions.eq("ev.blEventoCancelado", blEventoCancelado));
		dc.add(Restrictions.eq("e.cdEvento", cdEvento));
		dc.add(Restrictions.eq("e.tpEvento", tpEvento));
		return findByDetachedCriteria(dc);
	}

	/**
	 * Procura evento(s) no volumeNotaFiscal informado que seja igual ao código do evento
	 * passado por parâmetro.
	 * @param idVolume
	 * @param cdEvento
	 * @return
	 */
	public List<EventoVolume> findEventoVolume(Long idVolume, Short cdEvento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.volumeNotaFiscal.idVolumeNotaFiscal", idVolume));;
		dc.add(Restrictions.eq("e.cdEvento", cdEvento));
		return findByDetachedCriteria(dc);
	}

	/**
	 * Procura evento(s) no volumeNotaFiscal informado que seja igual ao código e a filial do evento informado. 
	 * passado por parâmetro.
	 * @param idVolume
	 * @param cdEvento
	 * @return
	 */
	public List<EventoVolume> findEventoVolume(Long idVolume, Long idFilialEvento, Short[] cdEvento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dc.createAlias("ev.evento", "e");
		dc.add(Restrictions.eq("ev.volumeNotaFiscal.idVolumeNotaFiscal", idVolume));
		dc.add(Restrictions.eq("ev.filial.id", idFilialEvento));
		dc.add(Restrictions.in("e.cdEvento", cdEvento));
		return findByDetachedCriteria(dc);
	}

	public List<Long> findIdsByIdVolume(Long idVolume) {
		String sql = "select pojo.idEventoVolume " +
			"from "+ EventoVolume.class.getName() + " as pojo " +
			"join pojo.volumeNotaFiscal as vnf " +
			"where vnf.idVolumeNotaFiscal = :idVolume ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idVolume", idVolume);
	}

	/**
	 * Busca Ultimo Evento do Volume
	 * @author Andre Valadas
	 * @param idVolume
	 */
	public EventoVolume findUltimoEventoVolume(Long idVolume, String tpEvento, Boolean blEventoCancelado) {
		//Busca data do último evento
		DetachedCriteria dcMax = DetachedCriteria.forClass(getPersistentClass(), "evb");
		dcMax.setProjection(Projections.max("evb.dhEvento.value"));
		dcMax.createAlias("evb.evento", "eb");
		dcMax.add(Restrictions.eqProperty("evb.volumeNotaFiscal.idVolumeNotaFiscal", "eva.volumeNotaFiscal.idVolumeNotaFiscal"));
		dcMax.add(Restrictions.eqProperty("evb.blEventoCancelado", "eva.blEventoCancelado"));
		dcMax.add(Restrictions.eqProperty("eb.tpEvento", "ea.tpEvento"));

		//Busca EventoVolume
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eva");
		dc.createAlias("eva.evento", "ea");
		dc.add(Restrictions.eq("eva.volumeNotaFiscal.idVolumeNotaFiscal", idVolume));
		dc.add(Restrictions.eq("eva.blEventoCancelado", blEventoCancelado));
		dc.add(Restrictions.eq("ea.tpEvento", tpEvento));
		dc.add(Subqueries.propertyEq("eva.dhEvento.value", dcMax));

		return (EventoVolume)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public EventoVolume findUltimoEventoVolumeDeDescargaByFilial(Long idVolume, Long idFilial) {
		//Busca data do último evento
		DetachedCriteria dcMax = DetachedCriteria.forClass(getPersistentClass(), "ev");
		dcMax.createAlias("ev.evento", "eb");
		dcMax.add(Restrictions.eq("ev.volumeNotaFiscal.idVolumeNotaFiscal", idVolume));
		dcMax.add(Restrictions.eq("ev.filial.idFilial", idFilial));
		dcMax.addOrder(Order.desc("ev.id"));
		
		List<EventoVolume> eventosVolume = (List<EventoVolume>) getAdsmHibernateTemplate().findByCriteria(dcMax);
		if (eventosVolume != null && !eventosVolume.isEmpty()) {
			return eventosVolume.get(0);
		}
		return null ;
	}

	public void removeByIdVolume(Serializable idVolume) {
		String sql = "delete from " + EventoVolume.class.getName() + " as ev where ev.volumeNotaFiscal.idVolumeNotaFiscal = :idVolume";
		getAdsmHibernateTemplate().removeById(sql, idVolume);
	}
	
	/**
	 * Método que retorna a maior dhEvento da tabela de EventoVolume com 
	 * o ID do Volume e IDs de LocalizacaoMercadoria
	 * @param idVolume
	 * @param idLocalizacaoMercadoria
	 * @return
	 */
	public DateTime findMaiorDhEventoByIdVolumeByIdsLocalizacaoMercadoria(Long idVolume, List<Long> idsLocalizacaoMercadoria) {
		StringBuffer hql = new StringBuffer();

		hql.append(" select max(ev.dhEvento.value) \n");
		hql.append("   from " + EventoVolume.class.getName() + " as ev \n");
		hql.append("   join ev.evento as evn \n");
		hql.append("  where ev.volumeNotaFiscal.idVolumeNotaFiscal = :idVolume \n");
		hql.append("    and evn.localizacaoMercadoria.idLocalizacaoMercadoria" +
								" in (:idsLocalizacaoMercadoria) \n");

		Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		query.setParameter("idVolume", idVolume);
		query.setParameterList("idsLocalizacaoMercadoria", idsLocalizacaoMercadoria);

		return (DateTime) query.uniqueResult();
	}
	
	/**
	 * Método que busca todos os eventos do documento de serviço não cancelados cujo evento 
	 * associado possua localização associada
	 * @author Andresa Vargas
	 * 
	 * @param Long idVolume
	 * @return
	 */
	public List<EventoVolume> findEventosVolumeNaoCancelados(Long idVolume){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev")
			.createAlias("ev.volumeNotaFiscal", "vol")
			.createAlias("ev.evento", "e")
			.add(Restrictions.eq("ev.blEventoCancelado", Boolean.FALSE))
			.add(Restrictions.eq("vol.idVolumeNotaFiscal", idVolume))
			.add(Restrictions.isNotNull("e.localizacaoMercadoria.idLocalizacaoMercadoria"));
		dc.addOrder(Order.desc("ev.dhEvento.value"));
		return findByDetachedCriteria(dc);
	}

	/**
	 * find para buscar o Evento do Documento de Serviço conforme os critérios enviados
	 * 
	 * @author Andresa Vargas
	 * 
	 * @param nrDocumento
	 * @param cdEvento
	 * @param idFilial
	 * @param dhEvento
	 * 
	 * @return EventoVolume
	 */
	public EventoVolume findEventoVolume(String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ev")
			.createAlias("ev.volumeNotaFiscal", "vol")
			.createAlias("ev.evento", "e")
			.createAlias("ev.filial", "f")
			.add(Restrictions.eq("ev.nrDocumento", nrDocumento))
			.add(Restrictions.eq("e.cdEvento", cdEvento))
			.add(Restrictions.eq("f.idFilial", idFilial))
			.add(Restrictions.eq("ev.dhEvento.value", dhEvento));
		return (EventoVolume)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Retorna a lista de eventos não cancelados do documento informado filtrando por o código do evento
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/02/2007
	 * 
	 * @param idVolume
	 * @param cdEvento
	 * 
	 * @return
	 */
	public List<EventoVolume> findEventoVolume(Long idVolume, Short[] cdEvento) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("e");
		hql.addFrom(EventoVolume.class.getName(), "e");
		hql.addCriteria("e.volumeNotaFiscal.idVolumeNotaFiscal", "=", idVolume);
		hql.addCriteriaIn("e.evento.cdEvento", cdEvento);
		hql.addCriteria("e.blEventoCancelado", "=", Boolean.FALSE);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

    /**
     * 
     * @param idVolume
     * @param blOrigemCancelamentoRIM
     * @return
     */
    public EventoVolume findEventoVolumeByLastDhEventoByIdVolume(Long idVolume, Boolean blOrigemCancelamentoRIM) {
		StringBuffer hql = new StringBuffer()
		.append(" select ev ")
		.append("from ").append(EventoVolume.class.getName()).append(" as ev ")
		.append("inner join fetch ev.evento as evento ")
		.append("where ev.volumeNotaFiscal.idVolumeNotaFiscal = ? ");

		if (blOrigemCancelamentoRIM != null && blOrigemCancelamentoRIM) {
			hql.append("and evento.cdEvento in (35, 132) ");
		}

		hql.append("order by ev.dhEvento.value desc ");

		List<EventoVolume> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idVolume});
		if (lista.isEmpty())
			return null;

		return lista.get(0);
	}
    
    public ResultSetPage<EventoVolume> findPaginatedByIdVolume(Long idVolumeNotaFiscal, FindDefinition findDefinition) {    	
    	List<Object> param = new ArrayList();
    	
    	StringBuilder sql = new StringBuilder()
    	.append("from ")
    	.append(EventoVolume.class.getName()).append(" as evtVol ")
    	.append("left join fetch evtVol.evento as evt ")
    	.append("left join fetch evt.descricaoEvento as dsEvt ")
    	.append("left join fetch evtVol.usuario as usu ")
    	.append("left join fetch evtVol.filial as fil ")
    	.append("where ")
    	.append("evtVol.volumeNotaFiscal.idVolumeNotaFiscal = ? ")
    	.append("order by evtVol.dhEvento.value desc ");
		
    	param.add(idVolumeNotaFiscal);
    	    	        
    	    	
    	return getAdsmHibernateTemplate().findPaginated(
    			sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());     	
	}	
    
    public ResultSetPage<EventoVolume> findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();				
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, this.getHqlPaginated(criteria));
	}
    
    public ResultSetPage<Map<String, Object>> findPaginatedBySql(PaginatedQuery paginatedQuery) {    	
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("ID_EVENTO_VOLUME", Hibernate.LONG);
    			sqlQuery.addScalar("DH_INCLUSAO", Hibernate.custom(JodaTimeDateTimeUserType.class));
    			sqlQuery.addScalar("CD_EVENTO", Hibernate.STRING);
    			sqlQuery.addScalar("DS_DESCRICAO_EVENTO", Hibernate.STRING);
    			sqlQuery.addScalar("OB_COMPLEMENTO", Hibernate.STRING);
    			sqlQuery.addScalar("TP_SCAN", Hibernate.STRING);
    			sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
    			sqlQuery.addScalar("NM_USUARIO", Hibernate.STRING);
    			sqlQuery.addScalar("CD_OCORRENCIA_ENTREGA", Hibernate.LONG);
    			sqlQuery.addScalar("DS_OCORRENCIA_ENTREGA", Hibernate.STRING);
    		}
    	};    	
    	

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(this.getSqlPaginated(), paginatedQuery.getCurrentPage(), 
														paginatedQuery.getPageSize(), paginatedQuery.getCriteria(), csq);
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Object[] obj : (List<Object[]>)rsp.getList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idEventoVolume", obj[0]);
			map.put("dhInclusao", obj[1]);
			map.put("cdEvento", obj[2] == null ? "" : obj[2]);
			map.put("dsEvento", obj[3] == null ? "" : obj[3]);
			map.put("obComplemento", obj[4]);
			map.put("tpScan", obj[5]);
			map.put("sgFilial", obj[6]);
			map.put("nmUsuario", obj[7]);
			map.put("cdOcorrenciaEntrega", obj[8] == null ? "" : obj[8]);
			map.put("dsOcorrenciaEntrega", obj[9] == null ? "" : obj[9]);
			list.add(map);
		}
		rsp.setList(list);
		
		return rsp;
	}
    
    private String getSqlPaginated(){
    	StringBuilder sql = new StringBuilder();
    	sql.append("SELECT ");
    	sql.append("EVENTO.ID_EVENTO_VOLUME, ");
    	sql.append("EVENTO.DH_INCLUSAO, ");
    	sql.append("EVENTO.CD_EVENTO, ");
    	sql.append("vi18n(EVENTO.DS_DESCRICAO_EVENTO) DS_DESCRICAO_EVENTO, ");
    	sql.append("EVENTO.OB_COMPLEMENTO, ");
    	sql.append("EVENTO.TP_SCAN, ");
    	sql.append("EVENTO.SG_FILIAL, ");
    	sql.append("EVENTO.NM_USUARIO NM_USUARIO, ");
    	sql.append("EVENTO.CD_OCORRENCIA_ENTREGA, ");
    	sql.append("vi18n(EVENTO.DS_OCORRENCIA_ENTREGA) DS_OCORRENCIA_ENTREGA ");
    	sql.append("FROM ");
    	
    	sql.append("(SELECT ");
    	sql.append("EVV.ID_EVENTO_VOLUME ID_EVENTO_VOLUME, ");
    	sql.append("EVV.DH_INCLUSAO DH_INCLUSAO, ");
    	sql.append("TO_CHAR(EVT.CD_EVENTO) CD_EVENTO, ");
    	sql.append("DEV.DS_DESCRICAO_EVENTO_I DS_DESCRICAO_EVENTO, ");
    	sql.append("EVV.OB_COMPLEMENTO OB_COMPLEMENTO, ");
    	sql.append("EVV.TP_SCAN TP_SCAN, ");
    	sql.append("FIL.SG_FILIAL SG_FILIAL, ");
    	sql.append("USU.NM_USUARIO NM_USUARIO, ");
    	sql.append("OE.CD_OCORRENCIA_ENTREGA CD_OCORRENCIA_ENTREGA, ");
    	sql.append("OE.DS_OCORRENCIA_ENTREGA_I DS_OCORRENCIA_ENTREGA ");
    	sql.append("FROM EVENTO_VOLUME EVV ");
    	sql.append("JOIN EVENTO EVT ON EVV.ID_EVENTO = EVT.ID_EVENTO ");
    	sql.append("JOIN DESCRICAO_EVENTO DEV ON EVT.ID_DESCRICAO_EVENTO = DEV.ID_DESCRICAO_EVENTO ");
    	sql.append("JOIN FILIAL FIL ON EVV.ID_FILIAL = FIL.ID_FILIAL ");
    	sql.append("JOIN USUARIO USU ON EVV.ID_USUARIO = USU.ID_USUARIO ");
    	sql.append("LEFT JOIN OCORRENCIA_ENTREGA OE ON EVV.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA ");
    	sql.append("WHERE EVV.ID_VOLUME_NOTA_FISCAL = :idVolumeNotaFiscal ");

    	sql.append("UNION ");

    	sql.append("SELECT ");
    	sql.append("NULL ID_EVENTO_VOLUME, ");
    	sql.append("ECC.DH_EVENTO DH_INCLUSAO, ");
    	sql.append("ECC.TP_EVENTO_CONTROLE_CARGA CD_EVENTO, ");
    	sql.append("VD.DS_VALOR_DOMINIO_I DS_DESCRICAO_EVENTO, ");
    	sql.append("NULL OB_COMPLEMENTO, ");
    	sql.append("'LM' TP_SCAN, ");
    	sql.append("FIL.SG_FILIAL SG_FILIAL, ");
    	sql.append("USU.NM_USUARIO NM_USUARIO, ");
    	sql.append("NULL CD_OCORRENCIA_ENTREGA, ");
    	sql.append("NULL DS_OCORRENCIA_ENTREGA ");
    	sql.append("FROM EVENTO_CONTROLE_CARGA ECC ");
    	sql.append("JOIN FILIAL FIL ON ECC.ID_FILIAL = FIL.ID_FILIAL ");
    	sql.append("JOIN USUARIO USU ON ECC.ID_USUARIO = USU.ID_USUARIO ");
    	sql.append("JOIN VALOR_DOMINIO VD ON ECC.TP_EVENTO_CONTROLE_CARGA = VD.VL_VALOR_DOMINIO AND VD.ID_DOMINIO = 50 ");
    	sql.append("JOIN MANIFESTO MAN ON ECC.ID_CONTROLE_CARGA = MAN.ID_CONTROLE_CARGA ");
    	sql.append("JOIN MANIFESTO_NACIONAL_VOLUME MNV ON MAN.ID_MANIFESTO = MNV.ID_MANIFESTO_VIAGEM_NACIONAL ");
    	sql.append("WHERE ECC.TP_EVENTO_CONTROLE_CARGA IN ('CP') ");
    	sql.append("AND  MNV.ID_VOLUME_NOTA_FISCAL = :idVolumeNotaFiscal ");
    	sql.append("AND ECC.ID_FILIAL IN ( ");
    	sql.append("	SELECT FRC.ID_FILIAL FROM FILIAL_ROTA_CC FRC ");
    	sql.append("	WHERE FRC.ID_CONTROLE_CARGA = MAN.ID_CONTROLE_CARGA AND FRC.NR_ORDEM <= ( ");
    	sql.append("		SELECT FRC.NR_ORDEM FROM FILIAL_ROTA_CC FRC ");
    	sql.append("		WHERE FRC.ID_CONTROLE_CARGA = MAN.ID_CONTROLE_CARGA AND FRC.ID_FILIAL = MAN.ID_FILIAL_DESTINO ");
    	sql.append("		) ");
    	sql.append("	) ");
    	sql.append(") EVENTO ");
    	sql.append("ORDER BY EVENTO.DH_INCLUSAO ");
    	return sql.toString();
    }
    
			
	@Override		
	public Integer getRowCount(Map criteria) {				
		return getAdsmHibernateTemplate().getRowCountForQuery(this.getHqlPaginated(criteria), criteria);
	}
	
	public String getHqlPaginated(Map<String,Object> criteria) {
		StringBuilder hql = new StringBuilder();
		hql.append("from " + EventoVolume.class.getName() + " as eventoVolume ");
    	hql.append("inner join fetch eventoVolume.evento as evento ");
    	hql.append("inner join fetch evento.descricaoEvento as descricaoEvento ");
    	hql.append("inner join fetch eventoVolume.usuario as usuario ");
    	hql.append("inner join fetch eventoVolume.filial as filial ");
    	hql.append("left join fetch eventoVolume.ocorrenciaEntrega as ocorrenciaEntrega ");
		hql.append("where 1=1 ");
		
		List param = new ArrayList();
		if(criteria.get("idVolumeNotaFiscal") != null) {
			hql.append("and eventoVolume.volumeNotaFiscal.id = :idVolumeNotaFiscal ");			
		}
    	hql.append("order by eventoVolume.dhEvento.value ");						
		return hql.toString();
	}
        
	public List<Short> findCdEventoByIdVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		StringBuffer hql = new StringBuffer()
				.append(" select evento.cdEvento ")
				.append("from ").append(EventoVolume.class.getName()).append(" as ev ")
				.append("inner join ev.evento as evento ")
				.append("where ev.volumeNotaFiscal.idVolumeNotaFiscal = ? ");

		List<Short> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idVolumeNotaFiscal});
		return lista;
	}

	public void executeCancelaEventoByIdEvento(Long idEventoVolume) {
		String udate = "update evento_volume set BL_EVENTO_CANCELADO = 'S' where ID_EVENTO_VOLUME = :idEventoVolume";
		Map param = new HashMap<String, Object>();
		param.put("idEventoVolume", idEventoVolume);
		getAdsmHibernateTemplate().executeUpdateBySql(udate, param);
	}

	public Long findQuantidadeEventoVolumeByIdVolumeByIdFilialByCdEvento(Long idVolumeNotaFiscal, Long idFilial, Long idEvento) {

		String hql = " select count(*) " +
				"from " + EventoVolume.class.getName() + " as ev " +
				"where ev.volumeNotaFiscal.idVolumeNotaFiscal = ? " +
				"and ev.filial.idFilial = ? " +
				"and ev.evento.idEvento = ? ";

		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{idVolumeNotaFiscal, idFilial, idEvento});
	}
    
}
