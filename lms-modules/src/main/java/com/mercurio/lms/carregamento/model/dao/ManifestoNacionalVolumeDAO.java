package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoNacionalVolumeDAO extends BaseCrudDao<ManifestoNacionalVolume, Long> {	

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ManifestoNacionalVolume.class;
	}
	
	
    /**
     * Retorna o ManifestoNacionalVolume a partir do id do Volume e do Controle de carga
     * Utilizado para verificar se o volume pertence ao controle de carga
     * @param idVolume
     * @param idControleCarga
     * @return
     */
    public ManifestoNacionalVolume findByIdVolumeAndIdControleCarga(Long idVolume, Long idControleCarga){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.setFetchMode("manifestoViagemNacional", FetchMode.JOIN);
    	dc.createAlias("manifestoViagemNacional.manifesto", "manifesto");
    	dc.setFetchMode("manifesto", FetchMode.JOIN);
    	dc.createAlias("manifesto.controleCarga", "cc");
    	dc.setFetchMode("cc", FetchMode.JOIN);    	
    	    	    
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);
    	dc.setFetchMode("manifesto.filialByIdFilialOrigem", FetchMode.JOIN);
    	
    	dc.add(Restrictions.eq("volumeNotaFiscal.id", idVolume));
    	dc.add(Restrictions.eq("cc.id", idControleCarga));    	        	    
    	
    	return (ManifestoNacionalVolume) this.getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    
    
    /**
     * Retorna o ManifestoNacionalVolume a partir do id do idFilial, id do manifesto e da lista de códigos da 
     * localização do VolumeNotaFiscal
     * @param idDoctoServico
     * @param idManifesto
     * @return List<ManifestoNacionalVolume> 
     */
    public List<ManifestoNacionalVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idDoctoServico, 
    																				   Long idManifesto,
    																				   List<Short> cdsLocalizacaoMercadoria) {

    	Map<String, Object> param = new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("FROM " + ManifestoNacionalVolume.class.getName() + " as mnv");
		hql.append("  JOIN mnv.volumeNotaFiscal as vnf");
		hql.append("  JOIN vnf.localizacaoMercadoria as lm");
		hql.append("  JOIN mnv.manifestoViagemNacional as mvn");
		hql.append("  JOIN mvn.manifesto as ma");
		hql.append("  JOIN ma.filialByIdFilialOrigem as fil");
		hql.append(" WHERE ma.idManifesto = :idManifesto");
		hql.append("   AND mnv.conhecimento.id = :idDoctoServico");
		hql.append("   AND lm.cdLocalizacaoMercadoria in (:cdsLocalizacaoMercadoria) ");
		
		param.put("idManifesto", idManifesto);
		param.put("idDoctoServico", idDoctoServico);
		param.put("cdsLocalizacaoMercadoria", cdsLocalizacaoMercadoria);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), param);
    }
    
    
    
    
    public List<ManifestoNacionalVolume> findByControleCargaAndFilialOrigem(Long idControleCarga, Long idFilial){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	
    	dc.setFetchMode("manifestoViagemNacional", FetchMode.JOIN);  
    	
    	dc.setFetchMode("manifestoViagemNacional.manifesto", FetchMode.JOIN);  
    	dc.createAlias("manifestoViagemNacional.manifesto", "manifesto");
    	
    	dc.setFetchMode("manifesto.filialByIdFilialOrigem", FetchMode.JOIN);  
    	dc.createAlias("manifesto.filialByIdFilialOrigem", "filial");

    	dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN);  
    	dc.createAlias("manifesto.controleCarga", "cc");
    	
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);  
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", FetchMode.JOIN);    
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento.filialOrigem", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.dispositivoUnitizacao", FetchMode.JOIN);
    	 
    	dc.add(Restrictions.eq("cc.id", idControleCarga));
    	dc.add(Restrictions.eq("filial.id", idFilial)); 
    	
    	return this.findByDetachedCriteria(dc);
    }
    
	
	
	/**
	 * Monta do Criteria utilizado na pesquisa de manifestos de viagem atraves do id do carregamento descarga 
	 * @param idCarregamentoDescarga
	 * @return
	 */
	private DetachedCriteria getDetachedCriteria(Long idCarregamentoDescarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoNacionalVolume.class)
		    .setFetchMode("manifestoViagemNacional", FetchMode.JOIN)
			.createAlias("manifestoViagemNacional", "mvn")
			
			.setFetchMode("mvn.manifesto", FetchMode.JOIN)
			.createAlias("mvn.manifesto", "manifesto")
			
			.setFetchMode("manifesto.controleCarga", FetchMode.JOIN)
			.createAlias("manifesto.controleCarga", "controleCarga")
			
			.setFetchMode("controleCarga.carregamentoDescargas", FetchMode.JOIN)
			.createAlias("controleCarga.carregamentoDescargas", "carregamentoDescarga")
			
			.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
		
		return dc;
	}	

	/**
	 * Busca os manifestos de viagem de um determinado carregamento descarga
	 * @param idCarregamentoDescarga
	 * @return
	 */
	public List findManifestoNacionalVolumes(Long idCarregamentoDescarga) {
		return super.findByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga));		
	}
		
	
	
	/**
	 * Busca a quantidade de manifestos de viagem de um determinado carregamento descarga
	 * @param idCarregamentoDescarga
	 * @return
	 */
	public Integer getRowCountManifestoNacionalVolumes(Long idCarregamentoDescarga) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga)
				.setProjection(Projections.rowCount()));
	}	  
	
	
	public List<ManifestoNacionalVolume> findByControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());				
		dc.createAlias("volumeNotaFiscal", "volumeNotaFiscal");
		dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);  		
		dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento", FetchMode.JOIN);
		dc.createAlias("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", "conhecimento");
		dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", FetchMode.JOIN);    
		dc.setFetchMode("conhecimento.filialOrigem", FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal.dispositivoUnitizacao", FetchMode.JOIN);
		
		dc.setFetchMode("manifestoViagemNacional", FetchMode.JOIN);
		dc.createAlias("manifestoViagemNacional.manifesto", "manifesto");
		dc.setFetchMode("manifestoViagemNacional.manifesto", FetchMode.JOIN);				
		dc.createAlias("manifesto.controleCarga", "cc");
		dc.setFetchMode("cc", FetchMode.JOIN);    	    
		 
		dc.add(Restrictions.eq("cc.id", idControleCarga));
		dc.add(Restrictions.eq("conhecimento.id", idDoctoServico)); 
		dc.add(Restrictions.ne("volumeNotaFiscal.tpVolume", ConstantesExpedicao.TP_VOLUME_MESTRE));
	
		return this.findByDetachedCriteria(dc);	
	}
	
	public List<ManifestoNacionalVolume> findByVolumeNotaFiscalAndFilialDestino(Long idVolumeNotaFiscal, Long idFilialDestino){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("volumeNotaFiscal", "volumeNotaFiscal");
		dc.setFetchMode("manifestoViagemNacional", FetchMode.JOIN);
		dc.createAlias("manifestoViagemNacional.manifesto", "manifesto");
		
		dc.add(Restrictions.eq("volumeNotaFiscal.idVolumeNotaFiscal", idVolumeNotaFiscal));
		dc.add(Restrictions.eq("manifesto.filialByIdFilialDestino.idFilial", idFilialDestino));
		dc.add(Restrictions.eq("manifesto.tpManifesto", "V"));
		dc.add(Restrictions.ne("manifesto.tpStatusManifesto", "CA"));
		return this.findByDetachedCriteria(dc);
	}

	/**
	 * Verifica se o volume está vinculado a outro manifesto de viagem
	 */
	public ManifestoNacionalVolume findVolumeVinculadoManifestoViagem(Long idManifesto, Long idVolumeNotaFiscal) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT mnv");
		hql.append("  FROM " + ManifestoNacionalVolume.class.getName() + " mnv");
		hql.append("  JOIN mnv.manifestoViagemNacional mvn"); //mnv.ID_MANIFESTO_VIAGEM_NACIONAL = mvn.ID_MANIFESTO_VIAGEM_NACIONAL
		hql.append("  JOIN mnv.volumeNotaFiscal vnf"); //vnf.ID_VOLUME_NOTA_FISCAL =  mnv.ID_VOLUME_NOTA_FISCAL
		hql.append("  JOIN mvn.manifesto m"); //m.ID_MANIFESTO = mnv.ID_MANIFESTO_VIAGEM_NACIONAL
		
		//mvn.ID_MANIFESTO_VIAGEM_NACIONAL != ID_MANIFESTO atual
		hql.append(" WHERE mvn.idManifestoViagemNacional <> ? ");
		//vnf.ID_VOLUME_NOTA_FISCAL => ID_VOLUME_NOTA_FISCAL do volume bipado
		hql.append("  AND vnf.idVolumeNotaFiscal = ? ");
		
		// ----- JIRA LMS-5321 -----
		// ********15.1********
		//mnv.ID_MANIFESTO_VIAGEM_NACIONAL => m.ID_MANIFESTO => m.TP_STATUS_MANIFESTO NOT IN('FE', 'CA')
		hql.append("  AND (m.tpStatusManifesto NOT IN ('FE', 'CA')");
		//m.ID_FILIAL_ORIGEM = ID_FILIAL_ORIGEM do pré-manifesto atual
		hql.append("   AND m.filialByIdFilialOrigem = (SELECT ma.filialByIdFilialOrigem FROM " + Manifesto.class.getName() + " ma WHERE ma.idManifesto = ?) "); 
		
		// ********15.2********
		//mnv.ID_MANIFESTO_VIAGEM_NACIONAL => m.ID_MANIFESTO => m.TP_STATUS_MANIFESTO NOT IN('FE', 'CA', 'ED')
		hql.append("  OR m.tpStatusManifesto NOT IN ('FE', 'CA', 'ED') ");
		//m.ID_FILIAL_DESTINO = ID_FILIAL_ORIGEM do pré-manifesto atual
		hql.append("   AND m.filialByIdFilialDestino = (SELECT ma.filialByIdFilialOrigem FROM " + Manifesto.class.getName() + " ma WHERE ma.idManifesto = ?)) ");
		// ----- JIRA LMS-5321 -----
		
		ManifestoNacionalVolume manifestoNacionalVolume = (ManifestoNacionalVolume) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object [] {idManifesto, idVolumeNotaFiscal, idManifesto, idManifesto});
				
		return manifestoNacionalVolume;
	}

	/**
	 * Remove o registro da tabela MANIFESTO_NACIONAL_VOLUME através do ID_VOLUME_NOTA_FISCAL e ID_MANIFESTO_VIAGEM_NACIONAL  
	 */
	public void removeManifestoNacionalVolumeById(Long idVolumeNotaFiscal, Long idManifestoViagemNacional) {
		List<Long> param = new ArrayList<Long>();
		
		StringBuffer hql = new StringBuffer("");
		hql.append("DELETE");
		hql.append("  FROM " + ManifestoNacionalVolume.class.getName() + " as mnv");
		hql.append("  JOIN mnv.volumeNotaFiscal as vnf");
		hql.append("  JOIN mnv.manifestoViagemNacional as mvn");
		hql.append(" WHERE vnf.idVolumeNotaFiscal = ?");
		hql.append("   AND mvn.idManifestoViagemNacional = ?");
		
		param.add(idVolumeNotaFiscal);
		param.add(idManifestoViagemNacional);
		
		super.executeHql(hql.toString(), param);		
	}

	/**
	 * Verifica se o manifesto viagem possui vínculo do documento de serviço
	 */
	public ManifestoNacionalVolume verificarManifestoViagemPossuiVinculoDocServico(Long idManifesto) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT mnv");
		hql.append("  FROM " + ManifestoNacionalVolume.class.getName() + " mnv");
		hql.append("  JOIN mnv.manifestoViagemNacional mvn");
		hql.append("  JOIN mnv.volumeNotaFiscal vnf");
		hql.append("  JOIN mnv.manifestoNacionalCto mnc");
		hql.append("  JOIN mnc.manifestoViagemNacional mvnc");
		hql.append(" WHERE mvn.idManifestoViagemNacional = ? "); //MANIFESTO_NACIONAL_VOLUME.ID_MANIFESTO_VIAGEM_NACIONAL = ID_MANIFESTO_VIAGEM_NAC que está sendo descarregado  
		hql.append("   AND vnf.idVolumeNotaFiscal = ? "); //MANIFESTO_NACIONAL_VOLUME.ID_VOLUME_NOTA_FISCAL = ID_VOLUME_NOTA_FISCAL do volume que foi lido
		
		ManifestoNacionalVolume manifestoNacionalVolume = (ManifestoNacionalVolume) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object [] {idManifesto});
		
		return manifestoNacionalVolume;
	}


	public ManifestoNacionalVolume findByIdManifestoViagemNacionalVinculadoDocServico(Long idManifestoViagemNacDescarregado, Long idVolumeNotaFiscalLido) {
		StringBuffer hql = new StringBuffer("");
		
		hql.append("SELECT mnv");
		hql.append("  FROM " + ManifestoNacionalVolume.class.getName() + " mnv");
		hql.append("  JOIN mnv.manifestoNacionalCto mnc");
		hql.append("  JOIN mnc.manifestoViagemNacional mvn");
		hql.append("  JOIN mnv.volumeNotaFiscal vnf");
		hql.append("  JOIN mnc.manifestoViagemNacional mvnc");
		hql.append(" WHERE mvn.idManifestoViagemNacional = ? ");//MANIFESTO_NACIONAL_VOLUME.ID_MANIFESTO_VIAGEM_NACIONAL = ID_MANIFESTO_VIAGEM_NAC que está sendo descarregado
		hql.append("   AND vnf.idVolumeNotaFiscal = ? ");//MANIFESTO_NACIONAL_VOLUME.ID_VOLUME_NOTA_FISCAL = ID_VOLUME_NOTA_FISCAL do volume que foi lido 
		hql.append("   AND mvnc.idManifestoViagemNacional = ? ");//MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = ID_MANIFESTO_VIAGEM_NAC  que está sendo descarregado. 
		
		ManifestoNacionalVolume manifestoNacionalVolume = 
				(ManifestoNacionalVolume) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), 
						new Object [] {idManifestoViagemNacDescarregado, idVolumeNotaFiscalLido, idManifestoViagemNacDescarregado});
		
		return manifestoNacionalVolume;
	}
	
}