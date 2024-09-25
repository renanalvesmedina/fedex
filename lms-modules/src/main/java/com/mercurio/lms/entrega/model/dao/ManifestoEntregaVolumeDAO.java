package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoEntregaVolumeDAO extends BaseCrudDao<ManifestoEntregaVolume, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ManifestoEntregaVolume.class;
	}

	/**
	 * 
	 * @param idManifesto
	 * @return
	 */
	public List<ManifestoEntregaVolume> findByManifesto(Long idManifesto){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());						
		dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);	
		dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
		
		dc.add(Restrictions.eq("manifestoEntrega.id", idManifesto));
		
		return this.findByDetachedCriteria(dc);				
	}


	/**
	 * Busca Manifesto de Entrega Volume pendentes pelo Manifesto e DoctoServico
	 * 
	 * @param idManifesto
	 * @param idDoctoServico
	 * @return
	 */
	public List<ManifestoEntregaVolume> findManifestoEntregaVolumePendentesByManifestoDoctoServico(Long idManifesto, Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mev");
		dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
		
		dc.add(Restrictions.eq("manifestoEntrega.id", idManifesto));
		dc.add(Restrictions.eq("mev.doctoServico.id", idDoctoServico));
		dc.add(Restrictions.isNull("mev.ocorrenciaEntrega"));
		
		return this.findByDetachedCriteria(dc);				
	}
	
	/**
	 * Busca Manifesto de Entrega Volume pelo Manifesto e DoctoServico
	 * 
	 * @param idManifesto
	 * @param idDoctoServico
	 * @return
	 */
	public List<ManifestoEntregaVolume> findManifestoEntregaVolumeByManifestoDoctoServico(Long idManifesto, Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mev");
		dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
		
		dc.add(Restrictions.eq("manifestoEntrega.id", idManifesto));
		dc.add(Restrictions.eq("mev.doctoServico.id", idDoctoServico));
		
		return this.findByDetachedCriteria(dc);				
	}
	
	/**
	 * Remove os registros associados ao manifesto de entrega recebido por parâmetro.
	 * 
	 * @param idManifestoEntrega
	 */
	public void removeByIdManifestoEntrega(Long idManifestoEntrega){
		String sql = "delete from " + ManifestoEntregaVolume.class.getName() + " as mev where mev.manifestoEntrega.id = :id ";
		getAdsmHibernateTemplate().removeById(sql, idManifestoEntrega);
	}
	
	/**
	 * Realiza uma busca a partir do id do controle de carga informado
	 * @param idControleCarga
	 * @return
	 */
	public List<ManifestoEntregaVolume> findManifestoEntregaVolumesPendentes(Long idControleCarga) {
		String sql = "select manifestoEntregaVolume from " + createHQLEntregasVolumesPendentes();
		
		List param = new ArrayList();
		param.add(idControleCarga);
		
		return getAdsmHibernateTemplate().find(sql, param.toArray());		
	}
	/**
	 * Realiza uma busca paginada a partir do id do controle de carga informado
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage<ManifestoEntregaVolume> findPaginatedEntregasVolumesPendentes(Long idControleCarga, FindDefinition findDefinition) {
		String sql = "select manifestoEntregaVolume from " + createHQLEntregasVolumesPendentes();
		
		List param = new ArrayList();
		param.add(idControleCarga);
		
		return getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());		
	}
	
	
	public Integer getRowCountEntregasVolumesPendentes(Long idControleCarga ){
		String sql = "select count(*) from " + createHQLEntregasVolumesPendentes();
		
		List param = new ArrayList();
		param.add(idControleCarga);
		
		return Integer.parseInt(getAdsmHibernateTemplate().find(sql.replace("fetch", ""), param.toArray()).get(0).toString());
	}
	
		
	private String createHQLEntregasVolumesPendentes(){		
		
		StringBuilder sql = new StringBuilder();
		sql.append( getPersistentClass().getName() + " manifestoEntregaVolume ")
		 	.append(" inner join fetch manifestoEntregaVolume.manifestoEntrega  manifestoEntrega ")
		 	.append(" inner join fetch manifestoEntrega.manifesto  manifesto ")
 			.append(" inner join fetch manifesto.controleCarga controleCarga ")
			.append(" inner join fetch manifestoEntregaVolume.volumeNotaFiscal volumeNotaFiscal ")
 			.append(" left join fetch volumeNotaFiscal.localizacaoMercadoria localizacao ")
	 		.append(" inner join fetch volumeNotaFiscal.notaFiscalConhecimento notaFiscalConhecimento ")
		 	.append(" inner join fetch notaFiscalConhecimento.conhecimento conhecimento ")
		 	.append(" inner join fetch conhecimento.clienteByIdClienteDestinatario destinatario ")
		 	.append(" inner join fetch destinatario.pessoa pessoa ")
		    .append(" inner join fetch conhecimento.filialByIdFilialOrigem filialByIdFilialOrigem ")
			.append(" inner join fetch conhecimento.moeda moeda ")		
			.append(" where controleCarga.id = ? ")
			.append(" and manifestoEntregaVolume.dhOcorrencia is null ")
			.append(" and conhecimento.id not in( ")
			.append(" 	select docDoctoServico.id ")
			.append("	from " + ManifestoEntregaDocumento.class.getName() + " manifestoEntregaDocumento ")
			.append(" 	inner join manifestoEntregaDocumento.manifestoEntrega docManifestoEntrega ")
			.append(" 	inner join manifestoEntregaDocumento.doctoServico docDoctoServico ")			
			.append(" 	where ")
			.append(" 		docManifestoEntrega.id = manifestoEntrega.id ")						
			.append(" )");
		
		return sql.toString();
	}
	
	
	
	/**
	 * Monta do Criteria utilizado na pesquisa de manifestos de entrega atraves do id do carregamento descarga 
	 * @param idCarregamentoDescarga
	 * @return
	 */
	private DetachedCriteria getDetachedCriteria(Long idCarregamentoDescarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaVolume.class)
		.setFetchMode("manifestoEntrega", FetchMode.JOIN)
			.createAlias("manifestoEntrega", "me")
			.createAlias("me.manifesto", "manifesto")
			.setFetchMode("manifesto", FetchMode.JOIN)
			.createAlias("manifesto.controleCarga", "controleCarga")
			.setFetchMode("controleCarga", FetchMode.JOIN)
			.createAlias("controleCarga.carregamentoDescargas", "carregamentoDescarga")
			.setFetchMode("carregamentoDescarga", FetchMode.JOIN)
			.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
		
		return dc;
	}
	
	
	
	/**
	 * Busca os manifestos de entrega de um determinado carregamento descarga
	 * @param idCarregamentoDescarga
	 * @return
	 */
	public List findManifestoEntregaVolumes(Long idCarregamentoDescarga) {
		return super.findByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga));		
	}
		
	
	
	/**
	 * Busca a quantidade de manifestos de entrega de um determinado carregamento descarga
	 * @param idCarregamentoDescarga
	 * @return
	 */
	public Integer getRowCountManifestoEntregaVolumes(Long idCarregamentoDescarga) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga)
				.setProjection(Projections.rowCount()));
	}

	public List<ManifestoEntregaVolume> findByManifestoEntregaDocumento(
			Long idManifestoEntregaDocumento) {
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaVolume.class)
		.setFetchMode("volumeNotaFiscal", FetchMode.JOIN)
		.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN)
		.add(Restrictions.eq("manifestoEntregaDocumento.id", idManifestoEntregaDocumento));
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public List<ManifestoEntregaVolume> findByManifestoAndDoctoServico(
			Long idManifesto, Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaVolume.class)
		.setFetchMode("ocorrenciaEntrega", FetchMode.JOIN)
		.setFetchMode("ocorrenciaEntrega.evento", FetchMode.JOIN)
		.add(Restrictions.eq("manifestoEntrega.id", idManifesto))
		.add(Restrictions.eq("doctoServico.id", idDoctoServico));
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}	
	
	/**
	 * Método responsável por consultar os manifesto de Entrega de volume , para um determinado conjunto de Ids volume
	 * @param idsVolume Id Volume
	 * @param idManifesto Id do Manifesto
	 * @return Lista de manisfesto de Volumes
	 */
	public List<ManifestoEntregaVolume> findByIdsVolumeAndIdManifesto(List<Long> idsVolume, Long idManifesto){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass()) 		
    		
    		.setFetchMode("ocorrenciaEntrega", FetchMode.JOIN)
			.setFetchMode("ocorrenciaEntrega.evento", FetchMode.JOIN) 
    		.setFetchMode("manifestoEntrega.ocorrenciaEntrega", FetchMode.JOIN)
    		.setFetchMode("manifestoEntrega", FetchMode.JOIN)    		
    		.setFetchMode("volumeNotaFiscal", FetchMode.JOIN)     		
        	
    		.add(Restrictions.in("volumeNotaFiscal.id", idsVolume))
    		.add(Restrictions.eq("manifestoEntrega.id", idManifesto));      	    
    	
    	return getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
    /**
     * Retorna o ManifestoEntregaVolume a partir do id do Volume e do Controle de carga
     * Utilizado para verificar se o volume pertence ao controle de carga
     * @param idVolume
     * @param idControleCarga
     * @return
     */
    public ManifestoEntregaVolume findByIdVolumeAndIdControleCarga(Long idVolume, Long idControleCarga){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
    		.createAlias("manifestoEntrega.manifesto", "manifesto")
    		.createAlias("manifesto.controleCarga", "controleCarga")
    	
    		.setFetchMode("manifestoEntrega.ocorrenciaEntrega", FetchMode.JOIN)
    		.setFetchMode("manifestoEntrega", FetchMode.JOIN)
    		.setFetchMode("manifesto", FetchMode.JOIN)
    		.setFetchMode("controleCarga", FetchMode.JOIN)
    		.setFetchMode("volumeNotaFiscal", FetchMode.JOIN)
     		.setFetchMode("manifesto.filialByIdFilialOrigem", FetchMode.JOIN)
        	
    		.add(Restrictions.eq("volumeNotaFiscal.id", idVolume))
    		.add(Restrictions.eq("controleCarga.id", idControleCarga));    	        	    
    	
    	return (ManifestoEntregaVolume) this.getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    
    
    /**
     * Retorna o ManifestoEntregaVolume a partir do id do idFilial, id do manifesto e da lista de códigos da 
     * localização do VolumeNotaFiscal
     * @param idFilial
     * @param idManifesto
     * @param idDoctoServico 
     * @return List<ManifestoNacionalVolume> 
     */
    public List<ManifestoEntregaVolume> findByDoctoServicoAndManifestoAndLocalizacaoVolume(Long idFilial, 
    																				   Long idManifesto,
    																				   Long idDoctoServico, 
    																				   List<Short> cdsLocalizacaoMercadoria) {
    	
    	Criteria criteria = this.getSession().createCriteria(getPersistentClass());	
    	criteria.createAlias("volumeNotaFiscal.localizacaoMercadoria", "localizacao");    	
    	criteria.createAlias("volumeNotaFiscal.notaFiscalConhecimento", "notaFiscalConhecimento");
    	criteria.createAlias("notaFiscalConhecimento.conhecimento", "conhecimento"); 
     	criteria.createAlias("manifestoEntrega.manifesto", "manifesto");
    	criteria.createAlias("manifesto.filialByIdFilialOrigem", "filial");
    	
    	criteria.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);
    	criteria.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);
    	criteria.setFetchMode("conhecimento", FetchMode.JOIN); 
    	criteria.setFetchMode("localizacao", FetchMode.JOIN);
    	criteria.setFetchMode("manifestoEntrega", FetchMode.JOIN);
    	criteria.setFetchMode("manifesto", FetchMode.JOIN);
    	criteria.setFetchMode("filial", FetchMode.JOIN);
    	
		criteria.add(Restrictions.eq("manifesto.id", idManifesto));
		criteria.add(Restrictions.eq("filial.id", idFilial));
		criteria.add(Restrictions.eq("conhecimento.id", idDoctoServico)); 
		criteria.add(Restrictions.in("localizacao.cdLocalizacaoMercadoria", cdsLocalizacaoMercadoria));
		
		return criteria.list();		
    }

    
    
    
	public List<ManifestoEntregaVolume> findByControleCargaAndFilialOrigem(Long idControleCarga, Long idFilial) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.createAlias("manifestoEntrega.manifesto", "manifesto");
    	dc.createAlias("manifesto.filialByIdFilialOrigem", "filial");
    	dc.createAlias("manifesto.controleCarga", "cc");
    	
    	dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);  
    	dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
    	dc.setFetchMode("manifesto", FetchMode.JOIN);
    	dc.setFetchMode("filial", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", FetchMode.JOIN);    
    	dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento.filialOrigem", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
    	dc.setFetchMode("volumeNotaFiscal.dispositivoUnitizacao", FetchMode.JOIN);
    	dc.setFetchMode("cc", FetchMode.JOIN);    	    
    	 
    	dc.add(Restrictions.eq("cc.id", idControleCarga));
    	dc.add(Restrictions.eq("filial.id", idFilial)); 
    	
    	return this.findByDetachedCriteria(dc);
	}
	
	
	public List<ManifestoEntregaVolume> findByControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("volumeNotaFiscal", "volumeNotaFiscal");
		dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);  
		dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento", FetchMode.JOIN);
		dc.createAlias("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", "conhecimento");
		dc.setFetchMode("conhecimento", FetchMode.JOIN);    
		dc.setFetchMode("conhecimento.filialOrigem", FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal.localizacaoMercadoria", FetchMode.JOIN);
		dc.setFetchMode("volumeNotaFiscal.dispositivoUnitizacao", FetchMode.JOIN);
				
		dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
		dc.createAlias("manifestoEntrega.manifesto", "manifesto");
		dc.setFetchMode("manifesto", FetchMode.JOIN);			
		dc.createAlias("manifesto.controleCarga", "cc");
		dc.setFetchMode("cc", FetchMode.JOIN);    	    
		 
		dc.add(Restrictions.eq("cc.id", idControleCarga));
		dc.add(Restrictions.eq("conhecimento.id", idDoctoServico)); 
		dc.add(Restrictions.ne("volumeNotaFiscal.tpVolume", ConstantesExpedicao.TP_VOLUME_MESTRE));
	
		return this.findByDetachedCriteria(dc);	
	} 
	
	@SuppressWarnings("unchecked")
    public List<ManifestoEntregaVolume> findManifestoEntregaVolumeByIdNotaFiscalConhecimentoIdManifestoEntregaDoc(Long idNotaFiscalConhecimento, Long idManifestoEntregaDocumento) {
	    StringBuilder hql = new StringBuilder();
        hql.append("select mev ")
        .append(" from " + ManifestoEntregaVolume.class.getName() + " mev ")
        .append(" inner join fetch mev.volumeNotaFiscal as vnf ")
        .append(" where vnf.notaFiscalConhecimento.id = ? ")
        .append(" and mev.manifestoEntregaDocumento.id = ? ");

        return (List<ManifestoEntregaVolume>) getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idNotaFiscalConhecimento, idManifestoEntregaDocumento });
    }

	public List<Map<String, Object>> findDadosVolumeEntregaParcial(
			Long idManifestoEntrega, Long idDoctoServico,
			Short cdOcorrenciaEntrega, Integer nrNota) {
		
		StringBuilder sql = new StringBuilder()
			.append("select vnf.ID_NOTA_FISCAL_CONHECIMENTO AS idNotaFiscalConhecimento, vnf.ID_VOLUME_NOTA_FISCAL AS idVolumeNotaFiscal, ")
					.append(" ev.ID_EVENTO_VOLUME as idEventoVolume, oe.CD_OCORRENCIA_ENTREGA as cdOcorrenciaEntrega, ")
					.append(" nfc.nr_nota_fiscal as nrNotaFiscal")
					.append(" from ") 
						.append(" manifesto_entrega_volume mev ") 
						.append(" ,volume_nota_fiscal vnf ")
						.append(" ,evento_volume ev ")
						.append(" ,ocorrencia_entrega oe ")
						.append(" ,nota_fiscal_conhecimento nfc")
					.append(" where ")
					  .append(" vnf.id_volume_nota_fiscal = mev.id_volume_nota_fiscal ")
					  .append(" and vnf.id_volume_nota_fiscal = ev.ID_VOLUME_NOTA_FISCAL ")
					  .append(" and ev.ID_OCORRENCIA_ENTREGA = oe.id_ocorrencia_entrega ")
					  .append(" and nfc.ID_NOTA_FISCAL_CONHECIMENTO = vnf.ID_NOTA_FISCAL_CONHECIMENTO ")
					  .append(" and mev.ID_MANIFESTO_ENTREGA = :idManifestoEntrega  ")
					  .append(" and mev.ID_DOCTO_SERVICO = :idDoctoServico ")
					  .append(" and oe.CD_OCORRENCIA_ENTREGA = :cdOcorrenciaEntrega ");
			
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idManifestoEntrega", idManifestoEntrega);
		params.put("idDoctoServico", idDoctoServico);
		params.put("cdOcorrenciaEntrega", cdOcorrenciaEntrega);
		
		if (nrNota != null){
			params.put("nrNotaFiscal", nrNota);
			sql.append(" and nfc.NR_NOTA_FISCAL = :nrNotaFiscal ");
		}
			
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idNotaFiscalConhecimento", Hibernate.LONG);
				sqlQuery.addScalar("idVolumeNotaFiscal", Hibernate.LONG);
				sqlQuery.addScalar("idEventoVolume", Hibernate.LONG);
				sqlQuery.addScalar("cdOcorrenciaEntrega", Hibernate.SHORT);
				sqlQuery.addScalar("nrNotaFiscal", Hibernate.STRING);
			}
		});
		
	}
}