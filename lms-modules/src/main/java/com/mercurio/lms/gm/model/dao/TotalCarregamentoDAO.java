package com.mercurio.lms.gm.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.TotalCarregamento;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TotalCarregamentoDAO extends BaseCrudDao<TotalCarregamento, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TotalCarregamento.class;
	}
	
	/**
	 * Busca TotalCarregamento pelo Mapa de Carregamento.
	 * 
	 * @author Samuel Alves
	 * @param String mapa.
	 * @return TotalCarrgamento, nulo, caso negativo.
	 */
	public TotalCarregamento findTotalByMapaCarregamento(Long mapa){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("mapaCarregamento", mapa));
		List<TotalCarregamento> listTotal = findByDetachedCriteria(dc);
		if(listTotal!=null && listTotal.size()>0){
			return listTotal.get(0);
		}else{
			return null;
		}	 
	}
	
	public void store(TotalCarregamento totalCarregamento) {
		super.store(totalCarregamento);
	}
	
	private void storeAll(List totalCarregamentoList) {
		getAdsmHibernateTemplate().saveOrUpdateAll(totalCarregamentoList);
		getAdsmHibernateTemplate().flush();
	}
	
	/**
	 * Busca totalVolume do TotalCarregamento pelo idCabecalhoCarregamento.
	 * 
	 * @author Samuel Alves
	 * @param String mapa.
	 * @return TotalCarrgamento, nulo, caso negativo.
	 */
	public Long findTotalCarregamentoByIdCabecalhoCarregamento(Long idCabecalhoCarregamento){
		SqlTemplate sql = new SqlTemplate();

        sql.addProjection("tc.totalVolume");

        sql.addFrom(TotalCarregamento.class.getName() + " tc ");
        
        sql.addCriteria("tc.cabecalhoCarregamento.idCabecalhoCarregamento","=",idCabecalhoCarregamento);
        
        Object ret = this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
        return (ret != null ? (Long) ret : null);
	}

	/**
	 * Carrega os dados mostrados na tela de consultar MpC
	 * LMS-2790
	 * @param criteria
	 * @return Map<String,Object>
	 */
	public Object[] findDadosCabecalhoConsultarMpC(Map criteria) {
		List param = new ArrayList();
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT tc.TOTAL_VOLUMES AS totalVolumes,\n")
			.append(" 	(SELECT COUNT(v.ID_VOLUME)   FROM volume v   WHERE v.mapa_carregamento = ?   AND v.codigo_status       =7   ) AS volumesConferidos,\n")
				.append("   (   (SELECT COUNT(v.ID_VOLUME)   FROM volume v   WHERE v.mapa_carregamento = ?   AND v.codigo_status      IN(0, 4, 5, 7)   )    \n")
					.append("   +   \n")
						.append("		(SELECT COUNT(dc.codigo_volume)   FROM detalhe_carregamento dc   ")
		.append(" WHERE dc.mapa_carregamento = ?   AND NOT EXISTS     (SELECT 1 FROM volume v WHERE v.codigo_volume = dc.codigo_volume     )   )) AS totalFalta")
		.append(" FROM TOTAL_CARREGAMENTO tc\n")
		.append(" WHERE tc.mapa_carregamento = ?"); 		
		
		param.add(criteria.get("mapaCarregamento")); // do count volumeConferidos
		param.add(criteria.get("mapaCarregamento")); // do count volumeConferidos
		param.add(criteria.get("mapaCarregamento"));// do count faltas
		param.add(criteria.get("mapaCarregamento"));// da condição principal
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("totalVolumes", Hibernate.LONG);
				sqlQuery.addScalar("volumesConferidos", Hibernate.LONG);
				sqlQuery.addScalar("totalFalta", Hibernate.LONG);
			}
		};
		return (Object[])this.getAdsmHibernateTemplate().findByIdBySql(sql.toString(), param.toArray(), configureSqlQuery);
	}
}
