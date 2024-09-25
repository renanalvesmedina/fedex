package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EmbarqueDAO extends BaseCrudDao<Carregamento, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Carregamento.class;
	}
	
	/**
	 * Busca Carregamento pelo nrFrota.
	 * 
	 * @author Samuel Alves
	 * @param String nrFrota numero da frota.
	 * @return Carregamento, nulo, caso negativo.
	 */
	public List<Carregamento> findCarregamentoByFrotaVeiculo(String nrFrota ) {
		Validate.notEmpty(nrFrota, "nrFrota cannot be null");
		
		return getAdsmHibernateTemplate().findByCriteria(
				  DetachedCriteria.forClass(Carregamento.class).add(Restrictions.ilike("frotaVeiculo", nrFrota, MatchMode.ANYWHERE)));
	}
	
	/**
	 * Busca uma Lista de Carregamentos pela doca.
	 * 
	 * @author Samuel Alves
	 * @param String doca numero da frota.
	 * @return List <Carregamento>, nulo, caso negativo.
	 */
	public List<Carregamento> findCarregamentoByDoca(String doca) {
		Validate.notEmpty(doca, "doca cannot be null");
		List<Carregamento> listCarregamento = null;
		listCarregamento = getAdsmHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Carregamento.class).add(Restrictions.eq("docaCarregamento", doca)));
		return listCarregamento;
	}
	
	/**
	 * Busca Carregamento pelo ID.
	 * @param idCarregamento
	 * @return
	 */
	public Carregamento findCarregamentoByid(Long idCarregamento) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idCarregamento",idCarregamento));
		
		List<Carregamento> listCarregamento = findByDetachedCriteria(dc);
		if(listCarregamento!=null && listCarregamento.size()>0){
			return listCarregamento.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca Carregamento do tipo GM Direto
	 * @author André Valadas
	 * 
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public Carregamento findCarregamentoGMDireto(final Long idMonitoramentoDescarga) {
		final SqlTemplate sql = new SqlTemplate();
        sql.addProjection("distinct c");
        sql.addFrom(Carregamento.class.getName() + " c " +
                   "    inner join c.volumes vols ");
        sql.addFrom(VolumeNotaFiscal.class.getName() + " vnf " +
                   "    inner join vnf.monitoramentoDescarga md ");
        sql.addJoin("vols.codigoVolume","vnf.nrVolumeColeta");
        sql.addCriteria("c.tipoCarregamento","=","D");//Direto
        sql.addCriteria("md.id","=",idMonitoramentoDescarga);            

        return (Carregamento)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}


	/**
	 * Busca Carregamento do tipo GM Normal
	 * 
	 * @param idMonitoramentoDescarga <code></code>
	 * @return <code>Carregamento</code>
	 */
	public Carregamento findCarregamentoGMNormal(Long idMonitoramentoDescarga) {
		final SqlTemplate sql = new SqlTemplate();
        sql.addProjection("c");
        sql.addFrom(Carregamento.class.getName() + " c " +
                   "    inner join c.volumes vols ");
        sql.addFrom(VolumeNotaFiscal.class.getName() + " vnf " +
                   "    inner join vnf.monitoramentoDescarga md ");
        sql.addJoin("vols.codigoVolume","vnf.nrVolumeColeta");
        sql.addCriteria("c.tipoCarregamento","=","N"); //Normal
        sql.addCriteria("md.id","=",idMonitoramentoDescarga);            

        return (Carregamento)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Busca uma Lista de Carregamentos pela frota e meio transpote.
	 * 
	 * @author Samuel Alves
	 * @param String frota.
	 * @return List <Carregamento>, nulo, caso negativo.
	 */
	public List<Carregamento> findCarregamentoByRota(String docaCarregamento, String rotaCarregamento) {
		Validate.notEmpty(docaCarregamento, "docaCarregamento cannot be null");
		Validate.notEmpty(rotaCarregamento, "rotaCarregamento cannot be null");
		
		return getAdsmHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Carregamento.class)
				.add(Restrictions.or(Restrictions.eq("codigoStatus", "1"), Restrictions.eq("codigoStatus", "2")))
				.add(Restrictions.eq("docaCarregamento", docaCarregamento))
				.add(Restrictions.eq("rotaCarregamento", rotaCarregamento)));
	}
	
	/**
	 * Busca uma Lista de Carregamentos pela frota e meio transpote.
	 * 
	 * @author William Silva
	 * @param String frota.
	 * @return List <Carregamento>, nulo, caso negativo.
	 */
	public List<Carregamento> findCarregamentoByRota(String rotaCarregamento) {
		Validate.notEmpty(rotaCarregamento, "rotaCarregamento cannot be null");
		
		return getAdsmHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Carregamento.class)
				.add(Restrictions.or(Restrictions.eq("codigoStatus", "1"), Restrictions.eq("codigoStatus", "2")))
				.add(Restrictions.eq("rotaCarregamento", rotaCarregamento)));
	}
}