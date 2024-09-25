package com.mercurio.lms.entrega.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaEntregaDAO extends BaseCrudDao<OcorrenciaEntrega, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaEntrega.class; 
    }

    protected void initFindPaginatedLazyProperties(Map fetchModes){
    	fetchModes.put("evento",FetchMode.JOIN);
    }
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("evento",FetchMode.JOIN);
    }
    protected void initFindLookupLazyProperties(Map lazyFindLookup) {
    	lazyFindLookup.put("evento",FetchMode.JOIN);
    }
    
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate sql = getSql(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }
    
    private SqlTemplate getSql(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();

        // Relacionamentos
        sql.addFrom((new StringBuffer(OcorrenciaEntrega.class.getName())).append(" AS OE ")
                .append("LEFT JOIN FETCH OE.evento AS EVT ")
                .append("LEFT JOIN FETCH EVT.descricaoEvento AS DSE ")
                .append("LEFT JOIN FETCH EVT.localizacaoMercadoria AS LEM ")
                .toString());

        // Critério de pesquisa
        sql.addCriteria("OE.cdOcorrenciaEntrega","=",criteria.getShort("cdOcorrenciaEntrega"));
        sql.addCriteria("lower("+PropertyVarcharI18nProjection.createProjection("OE.dsOcorrenciaEntrega")+")","like",criteria.getString("dsOcorrenciaEntrega").toLowerCase());
        sql.addCriteria("OE.tpOcorrencia", "=", criteria.getString("tpOcorrencia"));
        sql.addCriteria("OE.evento.id", "=", criteria.getLong("evento.idEvento"));
        sql.addCriteria("OE.blDescontoDpe", "=", criteria.getBoolean("blDescontoDpe"));
   		sql.addCriteria("OE.blOcasionadoMercurio", "=", criteria.getBigDecimal("blOcasionadoMercurio"));
   		sql.addCriteria("OE.blContabilizarEntrega", "=", criteria.getBoolean("blContabilizarEntrega"));
   		sql.addCriteria("OE.tpSituacao", "=", criteria.getString("tpSituacao"));
   		sql.addOrderBy("OE.cdOcorrenciaEntrega"); 
   		return sql;
    }

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		SqlTemplate sql = getSql(criteria);
   		
   		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
   		return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	} 

	public Evento findEventoAssociado(Long idOcorrenciaEntrega){
		SqlTemplate sqlTemplate = new SqlTemplate();
		
		sqlTemplate.addProjection("evt");		
		sqlTemplate.addFrom(getPersistentClass().getName(), "oe inner join oe.evento evt");							
		sqlTemplate.addCriteria("oe.id", "=", idOcorrenciaEntrega);
				
		List result = getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());
		
		return (Evento) (result.isEmpty() ? null : result.get(0));
	}

	private SqlTemplate createHqlTotaisPorMotivo(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
		StringBuffer from = new StringBuffer()
			.append(OcorrenciaEntrega.class.getName() + "        as OCEN ")
			.append(" inner join OCEN.manifestoEntregaDocumentos as MEDO ")
			.append(" inner join MEDO.manifestoEntrega           as MAEN ")
			.append(" inner join MAEN.filial                     as FILI ")
		;		
		sql.addFrom(from.toString());
	
		sql.addCriteria("FILI.idFilial","=",criteria.getLong("filial.idFilial"));
		sql.addCriteria("MEDO.dhOcorrencia.value",">=",criteria.getYearMonthDay("dataInicial"));
		sql.addCriteria("MEDO.dhOcorrencia.value","<=",criteria.getYearMonthDay("dataFinal"));
		return sql;
	}


	public Integer getRowCountTotaisPorMotivo(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlTotaisPorMotivo(criteria);
		sql.addProjection("COUNT(distinct OCEN.cdOcorrenciaEntrega) as rowcount");
		Integer i = Integer.valueOf(getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0).toString());		
		return i;
	}


	public ResultSetPage findPaginatedTotaisPorMotivo(TypedFlatMap criteria){
		SqlTemplate sql = createHqlTotaisPorMotivo(criteria);
		
		sql.addProjection("distinct new map(OCEN.idOcorrenciaEntrega","idOcorrenciaEntrega");
		sql.addProjection("OCEN.cdOcorrenciaEntrega","cdOcorrenciaEntrega");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("OCEN.dsOcorrenciaEntrega"),"dsOcorrenciaEntrega");
		sql.addProjection("count(MEDO.idManifestoEntregaDocumento)","qtde)");
						
		sql.addGroupBy("OCEN.idOcorrenciaEntrega");
		sql.addGroupBy("OCEN.cdOcorrenciaEntrega");
		sql.addGroupBy(PropertyVarcharI18nProjection.createProjection("OCEN.dsOcorrenciaEntrega"));
		sql.addOrderBy("OCEN.cdOcorrenciaEntrega");
		
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}
	
	/**
     * Consulta todas as ocorrências de entrega associadas a um determinado documento de serviço.
     * @param idDoctoServico
     * @param tpOcorrencia
     * @return List lista da entidade OcorrenciaEntrega
     */
	public List findOcorrenciasEntegaByDoctoServicoAndOcorrencia(Long idDoctoServico, String tpOcorrencia) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("OE");
				
		sql.addInnerJoin(ManifestoEntregaDocumento.class.getName(), "MED");
		sql.addInnerJoin("MED.ocorrenciaEntrega", "OE");
		
		sql.addCriteria("OE.tpOcorrencia","=",tpOcorrencia);
		sql.addCriteria("MED.doctoServico.id", "=", idDoctoServico);
		
		return (List)getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public OcorrenciaEntrega findOcorrenciaEntregaByIdDoctoServico(Long idDoctoServico) {
		StringBuffer sql = new StringBuffer()
		.append("select oe ")
		.append("from ")
		.append(EventoDocumentoServico.class.getName()).append(" as eds ")
		.append("inner join eds.ocorrenciaEntrega as oe ")
		.append("where ")
		.append("eds.doctoServico.id = ? ")
		.append("order by eds.dhEvento.value desc");
		
		List result = (List)getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico});
		return result.isEmpty() ? null : (OcorrenciaEntrega)result.get(0);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaNaoCanceladaByIdDoctoServico(Long idDoctoServico) {
		StringBuffer sql = new StringBuffer()
		.append("select oe ")
		.append("from ")
		.	append(EventoDocumentoServico.class.getName()).append(" as eds ")
			.append("inner join eds.ocorrenciaEntrega as oe ")

		.append("where ")
			.append("eds.doctoServico.id = ? ")
			.append("and eds.blEventoCancelado = 'N' ")
		
		.append("order by eds.dhEvento.value desc");
		
		List result = (List)getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico});
		return result.isEmpty() ? null : (OcorrenciaEntrega)result.get(0);
	}

	/**
	 * Solicitação CQPRO00006012 da integração.
	 * Método que retorna uma instancia da classe OcorrenciaEntrega de acordo com código da ocorrência de entrega.
	 * @param cdOcorrenciaEntrega
	 * @return
	 */
	public OcorrenciaEntrega findOcorrenciaEntrega(Short cdOcorrenciaEntrega){
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaEntrega.class, "oe");
		dc.setFetchMode("evento", FetchMode.JOIN);
		dc.add(Restrictions.eq("oe.cdOcorrenciaEntrega", cdOcorrenciaEntrega));
		return (OcorrenciaEntrega)getAdsmHibernateTemplate().findUniqueResult(dc);
	}


	public OcorrenciaEntrega findOcorrenciaEntregaByCodigoTipo(Short cdOcorrenciaEntrega, Object[] tpOcorrencia ){
		DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaEntrega.class, "oe");
		dc.add(Restrictions.eq("oe.cdOcorrenciaEntrega", cdOcorrenciaEntrega));
		dc.add(Restrictions.in("oe.tpOcorrencia", tpOcorrencia));
		return (OcorrenciaEntrega)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * FIXME - SQL a ser validado
	 * 
	 * Metodo que bisca se tem embarque GM e se existir se existe algum sem etiqueta.
	 * @param idControleCarga
	 * @param idFilialDestino
	 * @return
	 */
	public boolean existsVolEtiquetadoGMDireto(Long idControleCarga) {
		boolean naoTemEtiqueta = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("select vnf ");
		sql.append("from ControleCarga cc, ");
		sql.append("Manifesto man, ");
		sql.append("ManifestoViagemNacional mvn, ");
		sql.append("ManifestoNacionalCto mnc, ");
		sql.append("VolumeNotaFiscal vnf, ");
		sql.append("NotaFiscalConhecimento nfc, ");
		sql.append("Volume v ");
		sql.append("where cc.id = ? "); // PARAM 1
		sql.append("and cc = man.controleCarga ");
		sql.append("and man.id = mvn.manifesto.id ");
		sql.append("and mvn.id = mnc.manifestoViagemNacional.id ");
		sql.append("and mnc.conhecimento.id = nfc.conhecimento.id ");
		sql.append("and nfc.id = vnf.notaFiscalConhecimento.id ");
		sql.append("and vnf.nrVolumeColeta = v.codigoVolume ");
		sql.append("and v.codigoStatus = ? ");  // PARAM 2
		sql.append("and v.carregamento.tipoCarregamento = ? ");  // PARAM 3
		
		Object[] values = {idControleCarga, "6", "D"}; // PARAMETROS (?)

		List list = (List)getAdsmHibernateTemplate().find(sql.toString(), values);
		
		if (!list.isEmpty()) {
			for (Object object : list) {
				VolumeNotaFiscal teste = (VolumeNotaFiscal) object;
				
				if (teste.getNrVolumeEmbarque() == null) {
					naoTemEtiqueta = true;
					break;
				}
			}
		}
		return naoTemEtiqueta;		
	}
	
	/**
	 * FIXME - SQL a ser validado
	 * 
	 * Metodo que bisca se tem embarque GM e se existir se existe algum sem etiqueta.
	 * @param idControleCarga
	 * @param idFilialDestino
	 * @return
	 */
	public boolean existsVolGM(Long idControleCarga) {
		boolean existeVolumeGM = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("select vnf ");
		sql.append("from ControleCarga cc, ");
		sql.append("Manifesto man, ");
		sql.append("ManifestoViagemNacional mvn, ");
		sql.append("ManifestoNacionalCto mnc, ");
		sql.append("VolumeNotaFiscal vnf, ");
		sql.append("NotaFiscalConhecimento nfc, ");
		sql.append("Volume v ");
		sql.append("where cc.id = ? "); // PARAM 1
		sql.append("and cc = man.controleCarga ");
		sql.append("and man.id = mvn.manifesto.id ");
		sql.append("and mvn.id = mnc.manifestoViagemNacional.id ");
		sql.append("and mnc.conhecimento.id = nfc.conhecimento.id ");
		sql.append("and nfc.id = vnf.notaFiscalConhecimento.id ");
		sql.append("and vnf.nrVolumeColeta = v.codigoVolume ");
		sql.append("and v.codigoStatus = ? ");  // PARAM 2
		sql.append("and v.carregamento.tipoCarregamento = ? ");  // PARAM 3
		
		Object[] values = {idControleCarga, "6", "D"}; // PARAMETROS (?)

		List list = (List)getAdsmHibernateTemplate().find(sql.toString(), values);
		
		if (list != null && !list.isEmpty()){
			existeVolumeGM = true;
		}
		return existeVolumeGM;		
	}
	
	/**
	 * Método que retorna os Documentos de Serviço (vinculados a um Manifesto de Viagem cujo tipo é entrega direta) 
	 * de um Controle de Carga e que não possuem uma Ocorrência de Entrega.  
	 * @param idControleCarga
	 * @param idFilialDestino
	 * @return
	 */
	public boolean existsDoctosServicoManifestoViagemSemOcorrenciaEntrega(Long idControleCarga, Long idFilialDestino) {
	   	StringBuilder sql = new StringBuilder();
	   	sql.append("select ds from ");
	   	sql.append(sqlDoctosServicoManifestoViagem());
		sql.append("AND not exists (select 1 from ");
		sql.append("OcorrenciaEntrega oe, "); 
		sql.append("EventoDocumentoServico eds ");
		sql.append("where ds.id = eds.doctoServico.id ");
		sql.append("and eds.ocorrenciaEntrega.id = oe.id) ");
		Object[] values = {"V", "ED", idFilialDestino, idControleCarga};

	   	return !getAdsmHibernateTemplate().find(sql.toString(), values).isEmpty();
	}
	
	public List<Long> findIdsDoctosServicoManifestoViagemEntregaParcial(Long idControleCarga, Long idFilialDestino) {
	   	StringBuilder sql = new StringBuilder()
	   		.append(" SELECT ds.id ")
	   		.append("   FROM EventoDocumentoServico eds, OcorrenciaEntrega oe, ")
	   		.append(sqlDoctosServicoManifestoViagem())
        	.append("    AND eds.doctoServico.idDoctoServico = ds.id ")
        	.append("    AND eds.ocorrenciaEntrega = oe.idOcorrenciaEntrega ")
        	.append("    AND oe.cdOcorrenciaEntrega = 102 ")
        	.append("    AND eds.blEventoCancelado = 'N' ");
	   		
		Object[] values = {"V", "ED", idFilialDestino, idControleCarga};
	   	return getAdsmHibernateTemplate().find(sql.toString(), values);
	}
	
	private String sqlDoctosServicoManifestoViagem() {
	   	return new StringBuilder()
			.append("ControleCarga cc, ")
			.append("DoctoServico ds, ")
			.append("Manifesto man, ")
			.append("ManifestoViagemNacional mvn, ")
			.append("ManifestoNacionalCto mnc ")
			.append("where cc = man.controleCarga ")
			.append("and cc.tpControleCarga = ? ")
			.append("and man.id = mvn.manifesto.id ")
			.append("and mvn.id = mnc.manifestoViagemNacional.id ")
			.append("and mnc.conhecimento.id = ds.id ")
			.append("and man.tpManifestoViagem = ? ")
			.append("and man.tpStatusManifesto <> 'CA' ")
			.append("and man.filialByIdFilialDestino.id = ? ")
			.append("and cc.id = ? ")
			.toString();
	}
	
	/**
	 * Método que retorna os Documentos de Serviço (vinculados a um Manifesto de Entrega) 
	 * de um Controle de Carga e que não possuem uma Ocorrência de Entrega.  
	 * @param idControleCarga
	 * @return
	 */
	public boolean existsDoctosServicoManifestoEntregaSemOcorrenciaEntrega(Long idControleCarga) {
		StringBuilder sql = new StringBuilder();
		sql.append("select med from ");
		sql.append(sqlDoctosServicoManifestoEntrega());
		sql.append("and not exists (select 1 from ");
		sql.append("OcorrenciaEntrega oe "); 
		sql.append("where med.ocorrenciaEntrega.id = oe.id )");
		Object[] values = {"C", idControleCarga};
		
	   	return !getAdsmHibernateTemplate().find(sql.toString(), values).isEmpty();
	}
	
	public List<Long> findIdsDoctoServicoManifestoEntregaParcial(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
			.append(" select med.doctoServico.id ")
	   		.append(" from OcorrenciaEntrega oe, ")
			.append(sqlDoctosServicoManifestoEntrega())
	    	.append(" and med.ocorrenciaEntrega.id = oe.idOcorrenciaEntrega ")
	    	.append(" and oe.cdOcorrenciaEntrega = 102 ");
    	
		Object[] values = {"C", idControleCarga};
	   	return getAdsmHibernateTemplate().find(sql.toString(), values);
	}
	
	private String sqlDoctosServicoManifestoEntrega() {
	   	return new StringBuilder()
			.append("ControleCarga cc, ")
			.append("Manifesto man, ")
			.append("ManifestoEntrega me, ")
			.append("ManifestoEntregaDocumento med ")
			.append("where cc.id = man.controleCarga.id ")
			.append("and man.id = me.manifesto.id ")
			.append("and med.manifestoEntrega.id = me.id ")
			.append("and man.tpStatusManifesto <> 'CA' ")
			.append("and cc.tpControleCarga = ? ")
			.append("and cc.id = ? ")
			.toString();
	}

	public OcorrenciaEntrega findOcorrenciaEntregaByIdDoctoServicoByIdManifesto(Long idDoctoServico, Long idManifestoEntrega) {
		StringBuffer sql = new StringBuffer()
		.append("select oe ")
		.append("from ")
		.append(ManifestoEntregaDocumento.class.getName()).append(" as manifestoEntegaDoc ")
		.append("inner join manifestoEntegaDoc.ocorrenciaEntrega as oe ")
		.append("where ")
		.append("manifestoEntegaDoc.doctoServico.id = ? ")
		.append("and manifestoEntegaDoc.manifestoEntrega.id = ? ");

		List result = (List)getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico, idManifestoEntrega});
		return result.isEmpty() ? null : (OcorrenciaEntrega)result.get(0);
	}
	

	/**
	 * Método que retorna a existencia de vinculo de Volume e Manifesto de Entrega .
	 * @param idControleCarga
	 * @return
	 */
	public Boolean existsVolumeManifestoEntregaSemOcorrenciaEntrega(Long idControleCarga) {
	   	StringBuilder sql = new StringBuilder()
	   		.append("select mev from ")
	   		.append(sqlVolumeManifestoEntrega())
			.append("and not exists (select 1 from ")
			.append("OcorrenciaEntrega oe ")
			.append("where mev.ocorrenciaEntrega.id = oe.id )");
		Object[] values = {"C", idControleCarga};
		
	   	return !getAdsmHibernateTemplate().find(sql.toString(), values).isEmpty();
	}
	
	public List<Long> findIdsDoctoServicoVolumeManifestoEntregaParcial(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
			.append(" SELECT mev.doctoServico.id ")
	   		.append("   FROM EventoDocumentoServico eds, OcorrenciaEntrega oe, ")
			.append(sqlVolumeManifestoEntrega())
	    	.append("   AND eds.doctoServico.idDoctoServico = mev.doctoServico.id ")
	    	.append("   AND eds.ocorrenciaEntrega = oe.idOcorrenciaEntrega ")
	    	.append("   AND oe.cdOcorrenciaEntrega = 102 ")
	    	.append("   AND eds.blEventoCancelado = 'N' ");
    	
		Object[] values = {"C", idControleCarga};
	   	return getAdsmHibernateTemplate().find(sql.toString(), values);
	}
	
	public String sqlVolumeManifestoEntrega() {
	   	return new StringBuilder()
		   	.append("ControleCarga cc, ")
		   	.append("Manifesto man, ")
		   	.append("ManifestoEntrega me, ")
		   	.append("ManifestoEntregaVolume mev ")
		   	.append("where cc.id = man.controleCarga.id ")
		   	.append("and man.id = me.manifesto.id ")
		   	.append("and man.tpStatusManifesto <> 'CA' ")
		   	.append("and mev.manifestoEntrega.id = me.id ")
		   	.append("and cc.tpControleCarga = ? ")
		   	.append("and cc.id = ? ")
		   	.toString();
	}
	
	public OcorrenciaEntrega findLastOcorrenciaEntregaByIdVolumeAndFilial(Long idVolume, Long idFilial){
		StringBuffer sql = new StringBuffer();
		sql.append("select oe from ");
		sql.append(OcorrenciaEntrega.class.getName()).append(" as oe ");
		sql.append("join oe.eventoVolumes as ev ");
		sql.append("join ev.volumeNotaFiscal as vnf ");
		sql.append("where vnf.id = ? ");
		sql.append("and ev.filial.id = ? ");
		sql.append("order by ev.dhEvento.value desc");
		
		List list = (List)getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idVolume, idFilial});
				
		return (list.isEmpty() ? null : (OcorrenciaEntrega) list.get(0));
	}
	
    /**	 * Retorna código de ocorrência das ocorrências ativas
	 * JIRA: EDI-1061
	 * 
	 * @return List código de ocorrencia de entrega
	 * 	 
	 **/
	public List findCdOcorrenciaEntregaAtiva() {		
		SqlTemplate hql = new SqlTemplate();
		String tpSituacao = "A";
		hql.addProjection(" new map( oe.cdOcorrenciaEntrega as cdOcorrenciaEntrega )");			
		hql.addFrom(OcorrenciaEntrega.class.getName() + " oe ");		
		hql.addCustomCriteria(" oe.tpSituacao = ?", tpSituacao);
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());		
	}
		
	public OcorrenciaEntrega findOcorrenciaEntregaDoctoServico(
			Long idDoctoServico, Long idManifesto, String tpStatusManifesto,
			List<String> tpManifestoEntregaList) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT oe ");
		sql.append(" FROM ");
		sql.append(OcorrenciaEntrega.class.getSimpleName() + " oe ");
		sql.append(" join oe.manifestoEntregaDocumentos med ");
		sql.append(" join med.manifestoEntrega me ");
		sql.append(" join me.manifesto m ");
		sql.append("where ");
		sql.append(" m.tpManifestoEntrega in (:tpManifestoEntregaList) ");
		sql.append("and m.tpStatusManifesto != :tpStatusManifesto ");
		sql.append("and med.doctoServico.idDoctoServico = :idDoctoServico ");
		sql.append("and m.idManifesto = :idManifesto ");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("tpManifestoEntregaList", tpManifestoEntregaList);
		param.put("tpStatusManifesto", tpStatusManifesto);
		param.put("idDoctoServico", idDoctoServico);
		param.put("idManifesto", idManifesto);
		
		return (OcorrenciaEntrega) getAdsmHibernateTemplate().findUniqueResult(
				sql.toString(), param);
	}
	
	public List<OcorrenciaEntrega> findAllOcorrenciaEntregaAtivo() {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT oe ")
		.append("FROM ")
		.append(OcorrenciaEntrega.class.getSimpleName()).append(" oe ")
		.append("WHERE ")
		.append("	 oe.tpSituacao = 'A'")
		.append("AND oe.cdOcorrenciaEntrega not in(")
		.append(ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA).append(", ")
		.append(ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA_AEROPORTO)
		.append(")")
		.append("ORDER BY oe.cdOcorrenciaEntrega ");
		
		return getAdsmHibernateTemplate().find(sql.toString());
	}
	
	public List<OcorrenciaEntrega> findOcorrenciaEntregaRegistrarBaixaNotaAtivo() {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append(" SELECT oe ")
		.append(" FROM ")
		.append(OcorrenciaEntrega.class.getSimpleName()).append(" oe ")
		.append(" WHERE ")
		.append("	 oe.tpSituacao = 'A' ")
		.append(" AND (oe.cdOcorrenciaEntrega = ").append(ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA)
		.append(" 		OR oe.blOcasionadoMercurio = 'N' )")
		.append(" ORDER BY oe.cdOcorrenciaEntrega ");
		
		return getAdsmHibernateTemplate().find(sql.toString());
	}
	
	public List<OcorrenciaEntrega> findOcorrenciaEntregaByIdDoctoServicoCdOcorrenciaEntregaNaoCancelado(Long idDoctoServico, Short cdOcorrenciaEntrega) {
		StringBuffer sql = new StringBuffer()
		.append("select oe ")
		.append("from ")
		.append(EventoDocumentoServico.class.getName()).append(" as eds ")
		.append("inner join eds.ocorrenciaEntrega as oe ")
		.append("where eds.doctoServico.id = ? ")
		.append("and oe.cdOcorrenciaEntrega = ? ")
		.append("and eds.blEventoCancelado = 'N' ")
		.append("order by eds.dhEvento.value desc");
		
		return (List)getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico, cdOcorrenciaEntrega});
	}

	public Long findIdOcorrenciaEntregaByCdOcorrenciaEntrega(Short cdOcorrenciaEntrega){
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT oe.idOcorrenciaEntrega FROM ");
		sql.append(OcorrenciaEntrega.class.getSimpleName()).append(" oe ");
		sql.append(" WHERE oe.tpSituacao = 'A' ");
		sql.append(" AND oe.cdOcorrenciaEntrega = :cdOcorrenciaEntrega");

		Map criteria = new HashMap();
		criteria.put("cdOcorrenciaEntrega", cdOcorrenciaEntrega);

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);
	}
	
}