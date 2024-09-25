package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;



/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PreManifestoVolumeDAO extends BaseCrudDao<PreManifestoVolume, Long>
{


	@Override
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("doctoServico",FetchMode.JOIN);
		fetchModes.put("manifesto",FetchMode.JOIN);
		fetchModes.put("preManifestoDocumento",FetchMode.JOIN);
		fetchModes.put("volumeNotaFiscal",FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PreManifestoVolume.class;
    }              
    
    /**
     * Remove o Pre-Manifesto Volume a partir do id do Manifesto 
     * @param idManifesto
     */
    public void removeByIdManifesto(Long idManifesto){
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(PreManifestoVolume.class.getName()).append(" as pmv ")
			.append(" where ")
			.append( "pmv.manifesto.id = :id");
    	    	
    	getAdsmHibernateTemplate().removeById(sql.toString(), idManifesto);
    }
    
    /**
     * Remove o Pre-Manifesto Volume a partir do id do Pre-Manifesto Documento
     * @param idPreManifestoDocto
     */
    public void removeByIdPreManifestoDocto(Long idPreManifestoDocto) {
    	StringBuilder sql = new StringBuilder();
		sql.append(" delete from ").append(PreManifestoVolume.class.getName()).append(" as pmv ")
			.append(" where ")
			.append( "pmv.preManifestoDocumento.id = :id");
    
    	getAdsmHibernateTemplate().removeById(sql.toString(), idPreManifestoDocto);
	}
    
    
    /**
     * Retorna uma lista de Pré-Manifesto a partir do id do Manifesto
     */
    public List<PreManifestoVolume> findbyIdManifesto(Long idManifesto){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.add(Restrictions.eq("manifesto.id", idManifesto));
    	
    	return super.findByDetachedCriteria(dc);
    }
    
    /**
     * Retorna uma lista de Pré-Manifesto a partir do id do Manifesto
     */
    public List<PreManifestoVolume> findbyIdDoctoServico(Long idDoctoServico){
        
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.add(Restrictions.eq("doctoServico.idDoctoServico", idDoctoServico));
    	
    	return super.findByDetachedCriteria(dc);
    }
    
        
    /**
     * Retorna o PreManifestoVolume a partir do id do volume e do id do manifesto.
     * @param idVolume
     * @param idManifesto
     * @return PreManifestoVolume
     */
    public PreManifestoVolume findByVolumeAndManifesto(Long idVolume, Long idManifesto) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());				
		dc.add(Restrictions.eq("volumeNotaFiscal.id", idVolume)); 
		dc.add(Restrictions.eq("manifesto.id", idManifesto));
		List<PreManifestoVolume> result = super.findByDetachedCriteria(dc);
		if(result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
    }       
    
    /**
     * Retorna uma lista de preManifestoVolume que possuem PreManifestoDocumento nulo a partir do id do manifesto.     *
     *
     * @param idManifesto
     * @return PreManifestoVolume
     */
    public List<PreManifestoVolume> findVolumesSemPreManifDoctoByManifesto(Long idManifesto) {
		StringBuffer hql = new StringBuffer("");
		hql.append("SELECT pmv");
		hql.append("  FROM " + PreManifestoVolume.class.getName() + " pmv");
		hql.append("  JOIN pmv.manifesto mani");
		hql.append(" WHERE pmv.preManifestoDocumento is null "); 
		hql.append("   AND mani.idManifesto = ? ");
		List<PreManifestoVolume> preManifestoVolumes =  (List<PreManifestoVolume>)getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idManifesto} );    	

		return preManifestoVolumes;
    }
    
    /**
     * Retorna o PreManifestoVolume a partir do id do volume e do id do manifesto.
     * @param idDoctoServico
     * @param idManifesto
     * @return List<PreManifestoVolume> 
     */
    public List<PreManifestoVolume> findByDoctoServicoAndManifesto(Long idDoctoServico, Long idManifesto) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());				
		dc.add(Restrictions.eq("doctoServico.id", idDoctoServico)); 
		dc.add(Restrictions.eq("manifesto.id", idManifesto));
		return super.findByDetachedCriteria(dc);		
    }
    
    /**
     * Retorna o PreManifestoVolume a partir do id do DoctServico, id do manifesto e da lista de códigos da 
     * localização do VolumeNotaFiscal
     * @param idDoctoServico
     * @param idManifesto
     * @return List<PreManifestoVolume> 
     */
    public List<PreManifestoVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idDoctoServico, 
    																				   Long idManifesto,
    																				   List<Short> cdsLocalizacaoMercadoria) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());	
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
    	dc.createAlias("volumeNotaFiscal.localizacaoMercadoria", "localizacao");
    	
		dc.add(Restrictions.eq("doctoServico.id", idDoctoServico)); 
		dc.add(Restrictions.eq("manifesto.id", idManifesto));
		dc.add(Restrictions.in("localizacao.cdLocalizacaoMercadoria", cdsLocalizacaoMercadoria));
		
		return super.findByDetachedCriteria(dc);		
    }

    /**
     * Retorna uma lista PreManifestoVolume a partir do Controle de carga
     * Utilizado para retornar uma lista com todos os volumes pertencentes ao Controle de Carga
     * @param idManifesto
     * @param idDoctoServico
     * @return
     */
    public List<PreManifestoVolume> findByManifestoDoctoServico(Long idManifesto, Long idDoctoServico){
		StringBuffer hql = new StringBuffer("");
		hql.append("SELECT pmv");
		hql.append("  FROM " + PreManifestoVolume.class.getName() + " pmv");
		hql.append("  JOIN pmv.volumeNotaFiscal vnf");
		hql.append("  JOIN pmv.manifesto mani");
		hql.append(" WHERE pmv.preManifestoDocumento is not null "); 
		hql.append("   AND mani.idManifesto = ? ");
		hql.append("   AND pmv.doctoServico.id = ? ");

		List<PreManifestoVolume> preManifestoVolumes =  (List<PreManifestoVolume>)getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idManifesto,idDoctoServico} );
    	return preManifestoVolumes;
    }
    
    /**
     * Retorna o PreManifestoVolume a partir do id do Volume e do Controle de carga
     * Utilizado para verificar se o volume pertence ao controle de carga
     * @param idVolume
     * @param idControleCarga
     * @return
     */
    public PreManifestoVolume findByIdVolumeAndIdControleCarga(Long idVolume, Long idControleCarga){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.createAlias("manifesto.controleCarga", "cc");
    	    	    
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);
    	dc.setFetchMode("manifesto", FetchMode.JOIN);
    	dc.setFetchMode("manifesto.filialByIdFilialOrigem", FetchMode.JOIN);
    	dc.setFetchMode("cc", FetchMode.JOIN);    	
    	
    	dc.add(Restrictions.eq("volumeNotaFiscal.id", idVolume));
    	dc.add(Restrictions.eq("cc.id", idControleCarga));    	        	    
    	
    	return (PreManifestoVolume) this.getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    /**
     * Retorna uma lista PreManifestoVolume a partir do Controle de carga
     * Utilizado para retornar uma lista com todos os volumes pertencentes ao Controle de Carga
     * @param idControleCarga
     * @return
     */
    public List<PreManifestoVolume> findByControleCarga(Long idControleCarga){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.createAlias("manifesto.controleCarga", "cc");
    	
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);  
    	dc.setFetchMode("manifesto", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", FetchMode.JOIN);    
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento.filialOrigem", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
    	dc.setFetchMode("cc", FetchMode.JOIN);    	
    	 
    	dc.add(Restrictions.eq("cc.id", idControleCarga));    	        	    
    	
    	return this.findByDetachedCriteria(dc);
    }
    
    /**
     * Retorna uma lista PreManifestoVolume a partir de um Manifesto
     * Utilizado para retornar uma lista com todos os volumes pertencentes ao Manifesto
     * @param idManifesto
     * @return
     */
    public List<PreManifestoVolume> findByManifesto(Long idManifesto){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.createAlias("manifesto", "m");
    	
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);  
    	dc.setFetchMode("manifesto", FetchMode.JOIN);
    	 
    	dc.add(Restrictions.eq("m.id", idManifesto));    	        	    
    	
    	return this.findByDetachedCriteria(dc);
    }
    
    
    
    /**
     * Retorna uma lista PreManifestoVolume a partir de um Manifesto
     * Utilizado para retornar uma lista com todos os volumes, não entregues, pertencentes ao Manifesto
     * @param idManifesto
     * @return
     */
    public List<PreManifestoVolume> findVolumesNaoEntreguesByManifesto(Long idManifesto ){
		StringBuffer hql = new StringBuffer("");
		hql.append(" SELECT pmv ");
		hql.append(" FROM " + PreManifestoVolume.class.getName() + " pmv ");
		hql.append("  JOIN pmv.volumeNotaFiscal vmf ");
		hql.append("  JOIN pmv.manifesto mani ");
		hql.append("  JOIN vmf.localizacaoMercadoria localizacaoMercadoria  ");
		hql.append(" WHERE mani.idManifesto = ? ");
		hql.append("   AND localizacaoMercadoria.cdLocalizacaoMercadoria <> 1 "); 
		
		List<PreManifestoVolume> preManifestoVolumes =  (List<PreManifestoVolume>)getAdsmHibernateTemplate().find(hql.toString(), 
				new Object[]{idManifesto} );    	
    	return preManifestoVolumes;
    }

    /**
     * Retorna quantidade de registros encontrados 
     * 
     * @author André Valadas 
     * @param idControleCarga
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountPreManifestoVolume(final Long idControleCarga, final Long idDoctoServico){
    	final DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.setProjection(Projections.rowCount());
    	dc.createAlias("manifesto", "ma");
    	dc.createAlias("ma.controleCarga", "cc");
    	dc.add(Restrictions.eq("cc.id", idControleCarga));
    	dc.add(Restrictions.eq("doctoServico.id", idDoctoServico)); 

    	return (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    /**
     * Método que retorna uma list de objetos de PreManifestoVolume
     * Se withPreManifestoDocumento = false então retorna somente os que não possuem PreManifestoDocumento, ou seja
     * o idPreManifestoDocumento é null
     * @param idManifesto
     * @param withPreManifestoDocumento 
     * @return
     */
    public List findPreManifestoVolumeByIdManifesto(Long idManifesto, boolean withPreManifestoDocumento ) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoVolume.class);
		dc.setFetchMode("doctoServico", FetchMode.JOIN);
		dc.createAlias("doctoServico", "docto");
		
		dc.setFetchMode("docto.filialByIdFilialOrigem", FetchMode.JOIN);
		dc.createAlias("docto.filialByIdFilialOrigem", "filialByIdFilialOrigem");		
		
		dc.setFetchMode("manifesto", FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal",  FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
		
		dc.add( Restrictions.eq("manifesto.id", idManifesto));
				
		if( !withPreManifestoDocumento ){
			dc.add( Restrictions.isNull("preManifestoDocumento") );
		}

		dc.addOrder(Order.asc("docto.nrDoctoServico"));
		
		return super.findByDetachedCriteria(dc);
    }

	public Integer validateDoctoServico(Long idManifesto, Long idConhecimento) {
		if (idManifesto == null || idConhecimento == null) {
			return null;
		}
		StringBuilder hql = new StringBuilder("select pmv from PRE_MANIFESTO_VOLUME pmv");
		hql.append(" where pmv.id_manifesto = ? and");
		hql.append(" pmv.id_docto_servico = ?");
    
		return (Integer) getAdsmHibernateTemplate().getRowCountBySql(hql.toString(), new Object[] {idManifesto , idConhecimento});
    
	}

	public void removePreManifestoVolumeDoDocumentoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
    	.append(" delete ").append(getPersistentClass().getName())
    	.append(" where doctoServico.idDoctoServico = :id");
    	getAdsmHibernateTemplate().removeById(hql.toString(), idDoctoServico);
	}

	/**
	 * Verificar se o volume já está vinculado a outro pré-manifesto de viagem. LMS-3570
	 */
	public PreManifestoVolume findVolumeVinculadoPreManifestoViagem(Long idManifesto, Long idVolumeNotaFiscal) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT pmv");
		hql.append("  FROM " + PreManifestoVolume.class.getName() + " pmv");
		hql.append("  JOIN pmv.volumeNotaFiscal vnf"); 
		hql.append("  JOIN pmv.manifesto m");
		
		hql.append(" WHERE m.idManifesto <> ? "); 
		hql.append("   AND vnf.idVolumeNotaFiscal = ? ");
		
		// ----- JIRA LMS-5321 -----
		// ********16.1********
		hql.append("   AND (m.tpStatusManifesto NOT IN ('FE', 'CA')");
		hql.append("   AND m.filialByIdFilialOrigem = (SELECT ma.filialByIdFilialOrigem FROM " + Manifesto.class.getName() + " ma WHERE ma.idManifesto = ?) "); 
		
		// ********16.2********
		hql.append("  OR m.tpStatusManifesto NOT IN ('FE', 'CA', 'ED') ");
		hql.append("   AND m.filialByIdFilialDestino = (SELECT ma.filialByIdFilialOrigem FROM " + Manifesto.class.getName() + " ma WHERE ma.idManifesto = ?)) ");
		// ----- JIRA LMS-5321 -----
		
		PreManifestoVolume preManifestoVolume = (PreManifestoVolume) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object [] {idManifesto, idVolumeNotaFiscal, idManifesto, idManifesto});
				
		return preManifestoVolume;
	}
        
}