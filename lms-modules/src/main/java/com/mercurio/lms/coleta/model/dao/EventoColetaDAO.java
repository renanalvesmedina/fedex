package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoColetaDAO extends BaseCrudDao<EventoColeta, Long>
{
    protected void initFindByIdLazyProperties(Map map) 
    {
    	map.put("usuario", FetchMode.JOIN);
    	map.put("ocorrenciaColeta", FetchMode.JOIN);
    	map.put("pedidoColeta", FetchMode.JOIN);
    	map.put("meioTransporteRodoviario", FetchMode.JOIN);    	
    	map.put("meioTransporteRodoviario.meioTransporte", FetchMode.SELECT);    	
    }

    protected void initFindPaginatedLazyProperties(Map map) 
    {
    	map.put("usuario", FetchMode.JOIN);
    	map.put("ocorrenciaColeta", FetchMode.JOIN);
    	map.put("pedidoColeta", FetchMode.JOIN);
    	map.put("meioTransporteRodoviario", FetchMode.JOIN);
    	map.put("meioTransporteRodoviario.meioTransporte", FetchMode.SELECT);
    }
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoColeta.class;
    }

	public Integer getRowCountByIdPedidoColeta(Long idPedidoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(EventoColeta.class)
			.setProjection(Projections.rowCount())
        	.createAlias("pedidoColeta","pc")
			.add(Restrictions.eq("pc.idPedidoColeta", idPedidoColeta));
		List result = super.findByDetachedCriteria(dc);

		return (Integer) result.get(0);
	}

	/**
	 * Apaga uma entidade através do Id do Pedido de Coleta.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */	
    public void removeByIdPedidoColeta(Serializable idPedidoColeta) {
        String sql = "delete from " + EventoColeta.class.getName() + " as ec " +
		 			 " where " +
		 			 "ec.pedidoColeta.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idPedidoColeta);
    }	
   
    public DateTime findDhEventoByMeioTransporte(Long idMeioTransporte) {
        SqlTemplate sql = new SqlTemplate() ;
		
        StringBuffer sb = new StringBuffer();
		sb.append(PedidoColeta.class.getName()).append(" as pc ");
		sb.append(" inner join pc.eventoColetas as ec " );
		sb.append(" inner join pc.manifestoColeta mc " );
		sb.append(" inner join mc.controleCarga cc ");
		sb.append(" inner join cc.meioTransporteByIdTransportado mt ");

		sql.addFrom(sb.toString()); 	   
    	
		StringBuffer sb1 = new StringBuffer();
		sb1.append("(select max(ec1.idEventoColeta)").
		    append("   from EventoColeta ec1 ").
		    append("  inner join ec1.pedidoColeta pc1").
		    append("  where pc1.idPedidoColeta = pc.idPedidoColeta)");
       
        sql.addCustomCriteria("ec.idEventoColeta = " + sb1.toString());
        sql.addCustomCriteria("ec.tpEventoColeta = 'TR'");
		
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		sql.addCustomCriteria("cc.tpStatusControleCarga not in ('FE,CA')");
		
		sql.addProjection("ec.dhEvento desc");
		List result = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		return (result.size() > 0 ? (DateTime)result.get(0) : null) ;
    }

    /**
     * Retorna lista de dh_evento dos eventos de coleta transmitidos (TR) e que não tenham evento mais recente que TR para um determinado 
     * pedido_coleta  
     * @param idMeioTransporte
     * @return lista de dhEvento TR 
     */
	public List<EventoColeta> findEventoTransmitidoByMeioTransporte(Long idMeioTransporte){
		
        StringBuffer sql = new StringBuffer();
		sql.append(" select eventoColeta from " + EventoColeta.class.getName() + " eventoColeta ");
		sql.append(" inner join eventoColeta.pedidoColeta pedidoColeta ");
		sql.append(" inner join pedidoColeta.manifestoColeta manifestoColeta " );
		sql.append(" inner join manifestoColeta.controleCarga controleCarga " );
		sql.append(" inner join controleCarga.meioTransporteByIdTransportado meioTransporteByIdTransportado ");
		sql.append(" where controleCarga.tpControleCarga = 'C' ");
		sql.append(" and controleCarga.tpStatusControleCarga not in ('FE','CA') ");
		sql.append(" and pedidoColeta.tpStatusColeta not in ('AB','NM','CA') ");
		sql.append(" and meioTransporteByIdTransportado.id = " + idMeioTransporte );
		sql.append(" and eventoColeta.tpEventoColeta = 'TR' ");
		sql.append(" and pedidoColeta.blConfirmacaoVol = 'N' ");
        
		sql.append(" and exists ( SELECT 1 ");
		sql.append("              FROM " + EventoColeta.class.getName() + " ec_aux ");
		sql.append("              WHERE ec_aux.pedidoColeta = pedidoColeta ");
		sql.append("                AND ec_aux.meioTransporteRodoviario.id = controleCarga.meioTransporteByIdTransportado.id" );
		sql.append("                AND ec_aux.tpEventoColeta = 'TR' ");
		sql.append("                AND NOT EXISTS ( SELECT 1 ");
		sql.append("        		                  FROM " + EventoColeta.class.getName() + " ec2_aux ");
		sql.append("                                  WHERE ec2_aux.pedidoColeta.id = ec_aux.pedidoColeta.id ");
		sql.append("                                  AND ec2_aux.meioTransporteRodoviario.id = ec_aux.meioTransporteRodoviario.id ");
		sql.append("                                  AND ec2_aux.tpEventoColeta <> 'TR' ");
		sql.append("                                  AND ec2_aux.dhEvento.value > ec_aux.dhEvento.value ");
		sql.append("                                ) ");
		sql.append("              ) "); 
		
		
		List<EventoColeta> eventoColetaList = getAdsmHibernateTemplate().find(sql.toString());
		
		return eventoColetaList;
	}
	

    
    
    public DateTime findDhEventoByPedidoColeta(Long idPedidoColeta, String tpEventoColeta) {
    	StringBuffer query = new StringBuffer()
	    	.append("SELECT ec.dhEvento ")
	    	.append("FROM "+EventoColeta.class.getName()+" ec ")
	    	.append("WHERE ec.pedidoColeta.id = ? ")
	    	.append("  AND ec.tpEventoColeta = ?");

		return (DateTime) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idPedidoColeta, tpEventoColeta});
    }
    
    /**
     * Verifica se existe o tipo de EventoColeta para o PedidoColeta em questão
     * @param idPedidoColeta
     * @param tpEventoColeta
     * @return
     */
    public boolean validateEventoColetaByIdPedidoColeta(Long idPedidoColeta, String tpEventoColeta) {
    	StringBuffer s = new StringBuffer()
	    	.append("select count(*) from ").append(EventoColeta.class.getName()).append(" ec ")
	    	.append("where ec.pedidoColeta.id = ? ")
	    	.append("and ec.tpEventoColeta = ?");

    	Long result = (Long) getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s.toString())
    	.setParameter(0, idPedidoColeta)
    	.setParameter(1, tpEventoColeta)
    	.uniqueResult();
    	return (result.intValue() > 0);
    }
    
    public List findEventoColetaByIdPedidoColeta(Long idPedidoColeta){
    	SqlTemplate sql = new SqlTemplate();
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append(EventoColeta.class.getName()).append(" as EVCO ");
    	sb.append("left join EVCO.ocorrenciaColeta as OCCO ");
    	sql.addFrom(sb.toString());
    	
    	sql.addCriteria("EVCO.pedidoColeta.id", "=", idPedidoColeta );
    	sql.addCustomCriteria("EVCO.tpEventoColeta in ('TR', 'EX') ");
    	
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new Map ( ")
    			.append("EVCO.tpEventoColeta as status , ")
    			.append("OCCO.dsDescricaoResumida as dsResumida, ")
    			.append("EVCO.meioTransporteRodoviario as meioTransporteRodoviario) ");
    	sql.addProjection(projecao.toString());
    	
    	sql.addOrderBy("EVCO.dhEvento.value desc");
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    }
    
  
    
    public List findEventoColetaTransmitidaByIdPedidoColeta(Long idPedidoColeta){
    	SqlTemplate sql = new SqlTemplate();
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(EventoColeta.class.getName()).append(" as EVCO ");
    	sb.append("left join EVCO.ocorrenciaColeta as OCCO ");
    	sql.addFrom(sb.toString());
    	
    	sql.addCriteria("EVCO.pedidoColeta.id", "=", idPedidoColeta );
    	sql.addCustomCriteria("EVCO.tpEventoColeta in ('TR') ");
    	
    	StringBuilder projecao = new StringBuilder();
    	projecao.append("new Map ( ")
    			.append("EVCO.tpEventoColeta as status , ")
    			.append("EVCO.dhEvento as dhEvento , ")
    			.append("OCCO.dsDescricaoResumida as dsResumida, ")
    			.append("EVCO.meioTransporteRodoviario as meioTransporteRodoviario) ");
    	sql.addProjection(projecao.toString());
    	
    	sql.addOrderBy("EVCO.dhEvento.value desc");
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    }
    
    public List findEventoColetaManifestadaByIdPedidoColeta(Long idPedidoColeta){
    	SqlTemplate sql = new SqlTemplate();
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append(EventoColeta.class.getName()).append(" as EVCO ");
    	sb.append("left join EVCO.ocorrenciaColeta as OCCO ");
    	sql.addFrom(sb.toString());
    	
    	sql.addCriteria("EVCO.pedidoColeta.id", "=", idPedidoColeta );
    	sql.addCustomCriteria("EVCO.tpEventoColeta in ('MA') ");
    	
    	StringBuilder projecao = new StringBuilder();
    	projecao.append("new Map ( ")
    			.append("EVCO.tpEventoColeta as status , ")
    			.append("EVCO.dhEvento as dhEvento , ")
    			.append("OCCO.dsDescricaoResumida as dsResumida, ")
    			.append("EVCO.meioTransporteRodoviario as meioTransporteRodoviario) ");
    	sql.addProjection(projecao.toString());
    	
    	sql.addOrderBy("EVCO.dhEvento.value desc");
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    }
    
    
    
    
    
    /**
     * Busca o último evento de coleta do tipo "manifestada" a partir do id do pedido coleta.
     * @param idPedidoColeta
     * @return
     */
    public EventoColeta findLastEventoColetaManifestada(Long idPedidoColeta){
    	StringBuilder str = new StringBuilder();
    	str.append("select ec ");
    	str.append("from EventoColeta ec ") ;
    	str.append("inner join ec.pedidoColeta pc ");
    	str.append("inner join pc.manifestoColeta mc "); 
    	str.append("where ");
    	str.append("ec.tpEventoColeta = 'MA' ");
    	str.append("and pc.id = :idPedidoColeta ");
    	str.append("order by ec.dhEvento.value desc ");
    	
    	List eventos = getAdsmHibernateTemplate().findByNamedParam(str.toString(), "idPedidoColeta", idPedidoColeta);
    	if (eventos.isEmpty()) return null;
    	return (EventoColeta) eventos.get(0);
    }

    public List findEventoColetaByIdPedidoColeta(Long idPedidoColeta, String tpEventoColeta){
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" from EventoColeta as ec ");
    	sql.append(" where ec.pedidoColeta.idPedidoColeta = :idPedidoColeta ");
    	sql.append(" and ec.tpEventoColeta = :tpEventoColeta  ");
    	sql.append(" order by ec.dhEvento ");
    	
    	Map param = new HashedMap();
    	param.put("idPedidoColeta", idPedidoColeta);
    	param.put("tpEventoColeta", tpEventoColeta);
    	
    	
    	List eventos = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), param);
    	
    	return eventos;
    }

	/**
	 * Retorna um count de eventoColeta à partir do Id e tpEvento
	 * @param idPedidoColeta
	 * @param tpEventoColeta
	 * @return qtdeEventoColeta
	 */
    public Integer findQtdEventoColeta(Long idPedidoColeta, String tpEventoColeta) {
		StringBuilder sql = new StringBuilder();

		sql.append(" from Evento_Coleta ec ");
		sql.append(" where ec.id_Pedido_Coleta = :idPedidoColeta ");
		sql.append(" and ec.tp_Evento_Coleta = :tpEventoColeta  ");

		Map param = new HashedMap();
		param.put("idPedidoColeta", idPedidoColeta);
		param.put("tpEventoColeta", tpEventoColeta);

		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), param);
	}

}