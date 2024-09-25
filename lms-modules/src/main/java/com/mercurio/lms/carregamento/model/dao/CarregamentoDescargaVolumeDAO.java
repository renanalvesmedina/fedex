package com.mercurio.lms.carregamento.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CarregamentoDescargaVolumeDAO extends BaseCrudDao<CarregamentoDescargaVolume, Long> {

	@Override
    protected final Class getPersistentClass() {
        return CarregamentoDescargaVolume.class;
    }
    
	public void removeByCarregamentoDescargaByVolumeNotaFiscal(final CarregamentoDescarga carregamentoDescarga, final VolumeNotaFiscal volumeNotaFiscal) {

		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = " delete CarregamentoDescargaVolume cdv " +
								   " where cdv.carregamentoDescarga.idCarregamentoDescarga = :idCarregamentoDescarga" +
								   " and cdv.volumeNotaFiscal.idVolumeNotaFiscal = :idVolumeNotaFiscal" ;
				query = session.createQuery(hqlDelete);
				query.setLong("idCarregamentoDescarga", carregamentoDescarga.getIdCarregamentoDescarga());
				query.setLong("idVolumeNotaFiscal", volumeNotaFiscal.getIdVolumeNotaFiscal());
				
				query.executeUpdate();
		
				return null;
			}
		});
    }
	
	public void removeByCarregamentoDescargaNaFilialQuandoCarregamento(final CarregamentoDescarga carregamentoDescarga) {

		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = " delete CarregamentoDescargaVolume cdv  " +
								   " where cdv.carregamentoDescarga.idCarregamentoDescarga in (select cd.idCarregamentoDescarga from CarregamentoDescarga cd where cd.idCarregamentoDescarga= :idCarregamentoDescarga and cd.tpOperacao = 'C')" +
								   " AND cdv.tpScan = 'LM'";
				query = session.createQuery(hqlDelete);
				query.setLong("idCarregamentoDescarga", carregamentoDescarga.getIdCarregamentoDescarga());
				
				query.executeUpdate();
		
				return null;
			}
		});
    }
	
	public void removeByIdControleCarga(final Long idControleCarga) {

		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = " delete CarregamentoDescargaVolume cdv " +
								   " where cdv.carregamentoDescarga.idCarregamentoDescarga in (select cd.idCarregamentoDescarga from CarregamentoDescarga cd where cd.controleCarga.idControleCarga = :idControleCarga)";
				query = session.createQuery(hqlDelete);
				query.setLong("idControleCarga", idControleCarga);
				
				query.executeUpdate();
		
				return null;
			}
		});
    }
	
	 public List<CarregamentoDescargaVolume> findByControleCargaByVolumeNotaFiscalByCarregamentoDescarga(Long idControleCarga, Long idVolumeNotaFiscal, Long idCarregamentoDescarga, String tpOperacao) {
    	SqlTemplate sql = new SqlTemplate();   
    	
    	sql.addFrom(CarregamentoDescargaVolume.class.getName() + " as carregamentoDescargaVolume ");
		
		sql.addProjection("carregamentoDescargaVolume");
		
		String tpStatusOperacao = "F";
		if ("C".equals(tpOperacao)) {
			tpStatusOperacao = "O";
		}
		
		// LMS-5464 - ET: 03.01.01.09 - CARREGA_DESC_VOLUME.TP_SCAN in ('SF' ou 'SS')
		sql.addCustomCriteria("carregamentoDescargaVolume.tpScan in ('SF', 'SS')");
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.tpStatusOperacao", "=", tpStatusOperacao);
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.tpOperacao", "=", tpOperacao);
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.filial.idFilial", "=", SessionUtils.getFilialSessao().getIdFilial());
		
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.controleCarga.idControleCarga", "=", idControleCarga);
		if (idVolumeNotaFiscal != null) {
			sql.addCriteria("carregamentoDescargaVolume.volumeNotaFiscal.idVolumeNotaFiscal", "=", idVolumeNotaFiscal);
		}
		if (idCarregamentoDescarga != null) {
			sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.idCarregamentoDescarga", "=", idCarregamentoDescarga);
		}

    	
    	return (List<CarregamentoDescargaVolume>) super.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
	
	public List<CarregamentoDescargaVolume> findByControleCargaByOperacao(Long idControleCarga, String tpOperacao) {
    	SqlTemplate sql = new SqlTemplate();   
    	
    	sql.addFrom(CarregamentoDescargaVolume.class.getName() + " as carregamentoDescargaVolume ");
		
		sql.addProjection("carregamentoDescargaVolume");

		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.tpOperacao", "=", tpOperacao);
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.filial.idFilial", "=", SessionUtils.getFilialSessao().getIdFilial());
		
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.controleCarga.idControleCarga", "=", idControleCarga);
    	
		return (List<CarregamentoDescargaVolume>) super.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public boolean validateVolumeDescarregado(Long idCarregamentoDescarga, Long idVolumeNotaFiscal) {
		SqlTemplate sql = new SqlTemplate();   
    	
    	sql.addFrom(CarregamentoDescargaVolume.class.getName() + " as carregamentoDescargaVolume ");
		
		sql.addProjection("carregamentoDescargaVolume");
		
		sql.addCriteria("carregamentoDescargaVolume.volumeNotaFiscal.idVolumeNotaFiscal", "=", idVolumeNotaFiscal);
		sql.addCriteria("carregamentoDescargaVolume.carregamentoDescarga.idCarregamentoDescarga", "=", idCarregamentoDescarga);
    	
		List<CarregamentoDescargaVolume> list = (List<CarregamentoDescargaVolume>) super.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
		return list != null && !list.isEmpty();
	}
	
}