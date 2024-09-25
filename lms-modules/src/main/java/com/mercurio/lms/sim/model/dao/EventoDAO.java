package com.mercurio.lms.sim.model.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.tracking.Event;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.dto.UltimoEventoDto;
import com.mercurio.lms.sim.model.Evento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoDAO extends BaseCrudDao<Evento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Evento.class;
    }

    protected void initFindListLazyProperties(Map lazyFindList) {
    	lazyFindList.put("descricaoEvento",FetchMode.JOIN);    	
    }
    
    /* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map FetchModes) {
		FetchModes.put("localizacaoMercadoria",FetchMode.JOIN);
		FetchModes.put("localEvento",FetchMode.JOIN);
		FetchModes.put("descricaoEvento",FetchMode.JOIN);
	}
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map FetchModes) {
		FetchModes.put("localizacaoMercadoria",FetchMode.JOIN);
		FetchModes.put("localEvento",FetchMode.JOIN);
		FetchModes.put("descricaoEvento",FetchMode.JOIN);
		FetchModes.put("cancelaEvento",FetchMode.JOIN);
		FetchModes.put("cancelaEvento.descricaoEvento",FetchMode.JOIN);
		FetchModes.put("cancelaEvento.localEvento",FetchMode.JOIN);
	}
	
	public boolean verificaEvento(Evento evento){
    	DetachedCriteria dc = createDetachedCriteria();
    	if (evento.getIdEvento() != null){
    		dc.add(Restrictions.ne("idEvento",evento.getIdEvento()));
    	}
    	if (evento.getLocalEvento()!=null)
    		dc.add(Restrictions.eq("localEvento.id",evento.getLocalEvento().getIdLocalEvento()));
    	dc.add(Restrictions.eq("tpEvento",evento.getTpEvento().getValue()));
    	dc.add(Restrictions.eq("descricaoEvento.id",evento.getDescricaoEvento().getIdDescricaoEvento()));
    	if (evento.getLocalizacaoMercadoria() != null)
    		dc.add(Restrictions.eq("localizacaoMercadoria.id",evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()));
    	dc.add(Restrictions.eq("cdEvento",evento.getCdEvento()));
    	return findByDetachedCriteria(dc).size()>0;
    }

	
	public List findEvento(Map criteria) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT E.idEvento, DE.dsDescricaoEvento, LM.dsLocalizacaoMercadoria, E.cdEvento, DE.cdDescricaoEvento FROM ")
    		.append(Evento.class.getName()).append(" AS E ")
    		.append("INNER JOIN E.descricaoEvento AS DE ")
    		.append("LEFT  JOIN E.localizacaoMercadoria AS LM ");
   		sb.append("WHERE E.cdEvento = ?");
  		return getAdsmHibernateTemplate().find(sb.toString(),Short.valueOf((String)criteria.get("cdEvento")));
    }
	
	public Object[] findEventoByIdDoctoServico(Map<String, Object> criteria) {
		
		
    	StringBuilder sql = new StringBuilder();
    	sql.append("select ")
	    	.append(" ev.cd_evento cd_evento,")
			.append(" man_ent_doc.id_manifesto_entrega_documento id_man,")
			.append(" c.id_conhecimento,")
			.append(" to_char(eds.dh_evento,'dd/mm/yyyy hh24:mi') as dh_evento,")
			.append(" to_char(doc.dh_emissao,'dd/mm/yyyy hh24:mi') as dh_emissao_doc")
		.append(" from")
			.append(" docto_servico doc,")
			.append(" evento_documento_servico eds,")
			.append(" conhecimento c,")
			.append(" manifesto_entrega_documento man_ent_doc, ")
			.append(" evento ev ")
		.append(" where ")
			.append(" man_ent_doc.id_docto_servico(+) = doc.id_docto_servico and man_ent_doc.tp_situacao_documento(+) <> 'CANC'")
			.append(" and eds.id_docto_servico(+) = doc.id_docto_servico")
			.append(" and c.id_conhecimento(+) = doc.id_docto_servico")
    		.append("  and ev.id_evento(+) = eds.id_evento ");

    	Map<String, Object> params = mountCriteriaEventoByIdDoctoServico(criteria, sql);
    	sql.append(" and rownum = 1");
    	
    	
    	ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("cd_evento", Hibernate.SHORT);
				sqlQuery.addScalar("id_man", Hibernate.LONG);
				sqlQuery.addScalar("dh_evento", Hibernate.STRING);
				sqlQuery.addScalar("dh_emissao_doc", Hibernate.STRING);
			}
		};
		Object[] obj = null;
		List<Object[]> listObj = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
		if(listObj != null && !listObj.isEmpty()){
			obj = listObj.get(0);
		}
  		return obj;
    }
	
	private Map<String, Object> mountCriteriaEventoByIdDoctoServico(Map<String, Object> criteria, StringBuilder sql) {
		Map<String, Object> params = new HashMap<String, Object>();
    	Long idDoctoServico =(Long) criteria.get("idDoctoServico");
    	Boolean isNotEventoCancelado = (Boolean) criteria.get("isNotEventoCancelado");
    	Boolean isManifestoEntrada = (Boolean) criteria.get("isManifestoEntrada");
    	Short cdEvento = (Short) criteria.get("cdEvento");
    	String tpDocumento = (String) criteria.get("tpDocumento");
    	Boolean isFilialOrigem = (Boolean) criteria.get("isFilialOrigem");
    	Boolean isFilialDestino = (Boolean) criteria.get("isFilialDestino");
    	Long idOcorrenciaEntrega =(Long) criteria.get("idOcorrenciaEntrega");
    	Boolean isNotNullIdEvento = (Boolean) criteria.get("isNotNullIdEvento");
    	Boolean isNotNullIdConhecimento = (Boolean) criteria.get("isNotNullIdConhecimento");
    	
    	if(idDoctoServico != null){
	    	sql.append(" and eds.id_docto_servico = :idDoctoServico");
	    	params.put("idDoctoServico", idDoctoServico);
    	}
    	if(isNotEventoCancelado != null && isNotEventoCancelado){
    		sql.append(" and eds.bl_evento_cancelado='N'");
    	}
    	if(idDoctoServico != null && (isManifestoEntrada != null && isManifestoEntrada)){
    		sql.append(" and man_ent_doc.id_docto_servico = :idDoctoServico");
    		params.put("idDoctoServico", idDoctoServico);
    	}
    	if(cdEvento != null){
    		sql.append(" and ev.cd_evento= :cdEvento");
    		params.put("cdEvento", cdEvento);
    	}
    	if(tpDocumento != null){
    		sql.append(" and eds.tp_documento= :tpDocumento");
    		params.put("tpDocumento", tpDocumento.toUpperCase());
    	}
    	if(isFilialOrigem != null && isFilialOrigem){
    		sql.append(" and eds.id_filial=doc.id_filial_origem");
    	}
    	if(isFilialDestino != null && isFilialDestino){
    		sql.append(" and eds.id_filial=doc.id_filial_destino");
    	}
    	if(idOcorrenciaEntrega != null){
    		sql.append(" and eds.id_ocorrencia_entrega= :idOcorrenciaEntrega");
    		params.put("idOcorrenciaEntrega", idOcorrenciaEntrega);
    	}
    	if(isNotNullIdConhecimento != null && isNotNullIdConhecimento){
    		sql.append(" and eds.id_evento is not null");
    	}
    	if(isNotNullIdEvento != null && isNotNullIdEvento){
    		sql.append(" and c.id_conhecimento is not null");
    	}
		return params;
	}
	
	public Long findEventoNaoConformidade(Map<String, Object> criteria){
		Map<String, Object> params = new HashMap<String, Object>();
		Long idDoctoServico =(Long) criteria.get("idDoctoServico");
		StringBuilder sql = new StringBuilder();
    	sql.append(" select")
    		.append(" onc.id_motivo_abertura_nc id_evento")
    	.append(" from ")
	    	.append(" nao_conformidade NC,")
	    	.append(" ocorrencia_nao_conformidade ONC")
    	.append(" where ")
	    	.append(" nc.tp_status_nao_conformidade <> 'CAN'")
	    	.append(" and nc.tp_status_nao_conformidade <> 'AGP'")
	    	.append(" and onc.id_nao_conformidade = nc.id_nao_conformidade")
	    	.append(" and onc.tp_status_ocorrencia_nc = 'F'");
    	if(idDoctoServico != null){
    		sql.append(" and nc.id_docto_servico= :idDoctoServico");
    		params.put("idDoctoServico", idDoctoServico);
    	}
    	
    	ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_evento", Hibernate.LONG);
			}
		};
    	
		Long idEvento = null;
		List<Object[]> listObj = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
		if(listObj != null && !listObj.isEmpty()){
			Object result = listObj.get(0);
			idEvento = (Long) result;
		}
  		return idEvento;
	}
	
	/**
	 * Find para retornar o evento de cancelaEvento
	 * @param cdEvento
	 * @return
	 */
	public Evento findByCdEvento(Short cdEvento){
		DetachedCriteria dc = createDetachedCriteria();
				
		// Restrições
		dc.add(Restrictions.eq("cdEvento", cdEvento));
		dc.setFetchMode("cancelaEvento", FetchMode.JOIN);
		dc.setFetchMode("descricaoEvento", FetchMode.JOIN);		
		dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
		
		// Retorna uma lista de resultados
		List result = findByDetachedCriteria(dc); 
		
		if (result.isEmpty())
			return null;
		else return (Evento)result.get(0);
	}
	/**
	 * Consulta o evento que possui o evento de cancelamento informado
	 * @param idEvento
	 * @return
	 */
	public Evento findEventoByEventoCancelamento(Long idEvento){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("cancelaEvento.id", idEvento));
		List result = findByDetachedCriteria(dc); 
		
		if (result.isEmpty())
			return null;
		else return (Evento)result.get(0);
	}
	
	public List findEventos(TypedFlatMap tfm){
    	DetachedCriteria dc = DetachedCriteria.forClass(Evento.class,"EVT")
    	.createAlias("EVT.descricaoEvento","DE").setFetchMode("EVT.descricaoEvento",FetchMode.JOIN)
   		.createAlias("EVT.localizacaoMercadoria","LM").setFetchMode("EVT.localizacaoMercadoria",FetchMode.JOIN);
		return findByDetachedCriteria(dc); 
	}
	
	/**
	 * Responsavel por retornar dados do ultimo evento
	 * @param nrDoctoServico
	 * @param sgFilial
	 * @param dhEmissao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UltimoEventoDto findUltimoEvento(Long nrDoctoServico, String sgFilial, Date dhEmissao) {
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT To_Char(DOC.DT_PREV_ENTREGA,'DD/MM/YYYY') AS \"prevChegada\",     ");
		sql.append(" 		LM.CD_LOCALIZACAO_MERCADORIA AS \"cdLocalizacaoMercadoria\",      ");
		sql.append(" 		DOC.ID_DOCTO_SERVICO AS \"idDoctoServico\",      				  ");
		sql.append(" CASE WHEN LM.DS_LOCALIZACAO_MERCADORIA_I IS NOT NULL                     ");
		sql.append("   THEN LM.DS_LOCALIZACAO_MERCADORIA_I                                    ");
		sql.append("   ELSE DE.DS_DESCRICAO_EVENTO_I                                          ");
		sql.append(" END AS \"dsEvento\"                                                      ");
		sql.append(" FROM DOCTO_SERVICO DOC,                                                  ");
		sql.append(" FILIAL F,                                                                ");
		sql.append(" LOCALIZACAO_MERCADORIA LM,                                               ");
		sql.append(" EVENTO_DOCUMENTO_SERVICO EDS,                                            ");
		sql.append(" EVENTO E,                                                                ");
		sql.append(" DESCRICAO_EVENTO DE,                                                     ");
		sql.append(" PESSOA P                                                                 ");
		sql.append(" WHERE DOC.NR_DOCTO_SERVICO = :nrDoctoServico                             ");
		sql.append(" AND F.ID_FILIAL = DOC.ID_FILIAL_ORIGEM                                   ");
		sql.append(" AND F.SG_FILIAL = :sgFilial                                              ");
		sql.append(" AND DOC.DH_EMISSAO = :dhEmissao				                          ");
		sql.append(" AND EDS.ID_DOCTO_SERVICO = DOC.ID_DOCTO_SERVICO                          ");
		sql.append(" AND EDS.BL_EVENTO_CANCELADO = 'N'                                        ");
		sql.append(" AND E.ID_EVENTO = EDS.ID_EVENTO                                          ");
		sql.append(" AND E.BL_EXIBE_CLIENTE = 'S'                                             ");
		sql.append(" AND DE.ID_DESCRICAO_EVENTO = E.ID_DESCRICAO_EVENTO                       ");
		sql.append(" AND LM.ID_LOCALIZACAO_MERCADORIA (+) = E.ID_LOCALIZACAO_MERCADORIA       ");
		sql.append(" AND P.ID_PESSOA = EDS.ID_FILIAL                                          ");
		sql.append(" ORDER BY EDS.DH_EVENTO DESC, EDS.ID_EVENTO_DOCUMENTO_SERVICO DESC        ");

		
		Query query = getSession().createSQLQuery(sql.toString());
		query.setResultTransformer(Transformers.aliasToBean(UltimoEventoDto.class));
		
		query.setParameter("nrDoctoServico", nrDoctoServico);
		query.setParameter("sgFilial", sgFilial);
		String strDateEmissao = new SimpleDateFormat("dd/MM/yy HH:mm:ss ").format(dhEmissao);
		query.setParameter("dhEmissao", strDateEmissao);
		
		List<UltimoEventoDto> lst = query.list();
		for (UltimoEventoDto ultimoEventoDto : lst) {
			//Caso o documento já esteja finalizado, retornar o evento de finalização
			if(ultimoEventoDto.getCdLocalizacaoMercadoria() != null && ultimoEventoDto.getCdLocalizacaoMercadoria().intValue() == 1 ){ 
				return ultimoEventoDto;
			}
		}
			 		
		//Caso não possua o evento finalizado deve retornar o ultimo evento publicado
		
		return (lst != null && lst.size() > 0? lst.get(0):null);
	}

	public Long findIdEventoByCdEvento(Short cdEvento){
		StringBuilder sql = new StringBuilder();

		sql.append(" select ev.idEvento ");
		sql.append(" from ").append(Evento.class.getName()).append(" as ev ");
		sql.append(" where ev.tpSituacao = 'A' and ev.cdEvento = :cdEvento ");

		Map criteria = new HashMap();
		criteria.put("cdEvento", cdEvento);

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);
	}
	
}