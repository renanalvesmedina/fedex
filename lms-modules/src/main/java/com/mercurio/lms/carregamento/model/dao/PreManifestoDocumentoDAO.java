package com.mercurio.lms.carregamento.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PreManifestoDocumentoDAO extends BaseCrudDao<PreManifestoDocumento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PreManifestoDocumento.class;
    }
    
    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do DoctoServico
     * @param idDoctoServico
     * @return
     */
    public List findPreManifestoDocumentoByIdDoctoServico(Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class);
		dc.add( Restrictions.eq("doctoServico.id", idDoctoServico));
		return super.findByDetachedCriteria(dc);
    }     
    

    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do Manifesto
     * @param idManifesto
     * @return
     */
    public List findPreManifestoDocumentoByIdManifesto(Long idManifesto, String... tpDocumentoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class);
		dc.createAlias("doctoServico", "doctoServico");
		dc.createAlias("manifesto", "manifesto");
		dc.add( Restrictions.eq("manifesto.id", idManifesto));
		if (tpDocumentoServico != null && tpDocumentoServico.length > 0) {
			dc.add( Restrictions.in("doctoServico.tpDocumentoServico", tpDocumentoServico));
		}
		dc.addOrder(Order.asc("nrOrdem"));
		return super.findByDetachedCriteria(dc);
    }

    /**
     * Busca quantidade de documentos
     * 
     * @author André Valadas
     * @param idManifesto
     * @param tpDocumentoServico
     * @return
     */
    public Integer getRowCountPreManifestoDocumento(final Long idManifesto, final String tpDocumentoServico) {
		final DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class);
		dc.setProjection(Projections.rowCount());
		dc.createAlias("doctoServico", "doctoServico");
		dc.createAlias("manifesto", "manifesto");
		dc.add( Restrictions.eq("manifesto.id", idManifesto));
		if (StringUtils.isNotBlank(tpDocumentoServico)) {
			dc.add( Restrictions.eq("doctoServico.tpDocumentoServico", tpDocumentoServico));
		}
		return (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    /**
     * Busca todos os manifestosDoctoServico relacionados com um manifesto em questao.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDoctoServico
     * @param idFilialOrigem
     * @return
     */
    public Integer getRowCountPreManifestoDoctoServicoByIdManifesto(Long idManifesto, Long idDoctoServico, String tpDoctoServico, Long idFilialOrigem){
    	
    	SqlTemplate sql = getPreManifestosDoctoServicoByidManifestoSqlTemplate(idManifesto, idDoctoServico, tpDoctoServico, idFilialOrigem);
    	
    	//Projections
    	sql.addProjection("count(*) as rowcount");
		
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
    	return result.intValue();
    }
    
    /**
     * Busca todos os manifestosDoctoServico relacionados com um manifesto em questao.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDoctoServico
     * @param idFilialDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedPreManifestosDoctoServicoByidManifesto(
    		Long idManifesto, 
    		Long idDoctoServico, 
    		String tpDoctoServico, 
    		Long idFilialDoctoServico, 
    		FindDefinition findDefinition){
    	
    	SqlTemplate sql = getPreManifestosDoctoServicoByidManifestoSqlTemplate(idManifesto, idDoctoServico, tpDoctoServico, idFilialDoctoServico);
    	
    	//Projjections
    	StringBuffer projecao = new StringBuffer();
		projecao.append("new map(preManifestoDocumento.idPreManifestoDocumento as idPreManifestoDocumento, ");
		projecao.append("doctoServico.idDoctoServico as idDoctoServico, ");
		projecao.append("doctoServico.nrDoctoServico as nrDoctoServico, ");
		projecao.append("doctoServico.vlMercadoria as vlMercadoria, ");
		projecao.append("doctoServico.vlTotalDocServico as vlTotalDocServico, ");
		projecao.append("doctoServico.dtPrevEntrega as dtPrevEntrega, ");
		projecao.append("doctoServico.qtVolumes as qtVolumes, ");
		projecao.append("doctoServico.psReal as psReal, ");
		projecao.append("doctoServico.tpDocumentoServico as tpDocumento, ");
		projecao.append("moeda.sgMoeda as sgMoeda, ");
		projecao.append("moeda.dsSimbolo as dsSimbolo, ");
		projecao.append("moeda.sgMoeda as sgMoedaFrete, ");
		projecao.append("moeda.dsSimbolo as dsSimboloFrete, ");
		projecao.append("servico.sgServico as sgServico, ");
		projecao.append("filialOrigem.sgFilial as sgFilialOrigem, ");
		projecao.append("filialDestino.sgFilial as sgFilialDestino, ");
		projecao.append("pessoaFilialDestino.nmFantasia as nmFantasiaFilialDestino, ");
		projecao.append("pessoaClienteRemetente.nmPessoa as clienteRemetente, ");
		projecao.append("pessoaClienteDestinatario.nmPessoa as clienteDestinatario) ");
		
		sql.addProjection(projecao.toString());		
		
		sql.addOrderBy("filialOrigem.sgFilial");
    	sql.addOrderBy("doctoServico.nrDoctoServico");
    	
    	return getAdsmHibernateTemplate()
    		.findPaginated(sql.getSql(true), findDefinition.getCurrentPage(),findDefinition.getPageSize(), sql.getCriteria());
    	
    }
    
    public Integer getRowCountPreManifestosDoctoServicoByidManifestoSQL(Long idManifesto, Long idDoctoServico, String tpDoctoServico){
    	
    	StringBuffer sql = this.mountSLQPreManifestosDoctoServico(idManifesto, idDoctoServico, tpDoctoServico );
    	
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[] {});
    }
    
    
    public ResultSetPage findPaginatedPreManifestosDoctoServicoByidManifestoSQL( 
    		Long idManifesto, 
    		Long idDoctoServico, 
    		String tpDoctoServico, 
    		FindDefinition findDefinition){
    	
    	StringBuffer sql = mountSLQPreManifestosDoctoServico(idManifesto, idDoctoServico, tpDoctoServico );
    	
       	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("dvDoctoServico", Hibernate.STRING);
    			sqlQuery.addScalar("idPreManifestoDocumento", Hibernate.LONG);
    			sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
    			sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
    			sqlQuery.addScalar("vlMercadoria",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vlTotalDocServico",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("dtPrevEntrega",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
    			sqlQuery.addScalar("qtVolumes",Hibernate.LONG);
    			sqlQuery.addScalar("psReal",Hibernate.BIG_DECIMAL);
    			
				Properties propertiesTpDocumentoServico = new Properties();
				propertiesTpDocumentoServico.put("domainName","DM_TIPO_DOCUMENTO_SERVICO");
    			sqlQuery.addScalar("tpDocumento",Hibernate.custom(DomainCompositeUserType.class,propertiesTpDocumentoServico));
    			sqlQuery.addScalar("sgMoeda",Hibernate.STRING);
    			sqlQuery.addScalar("dsSimbolo",Hibernate.STRING);
    			sqlQuery.addScalar("sgMoedaFrete",Hibernate.STRING);
    			sqlQuery.addScalar("dsSimboloFrete",Hibernate.STRING);
    			sqlQuery.addScalar("sgServico",Hibernate.STRING); 
    			sqlQuery.addScalar("sgFilialOrigem",Hibernate.STRING);
    			sqlQuery.addScalar("sgFilialDestino",Hibernate.STRING);
    			sqlQuery.addScalar("nmFantasiaFilialDestino",Hibernate.STRING);
    			sqlQuery.addScalar("clienteRemetente",Hibernate.STRING);
    			sqlQuery.addScalar("clienteDestinatario",Hibernate.STRING);
    		}
    	};
    	
    	return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDefinition.getCurrentPage(), 
    	    							findDefinition.getPageSize(), new Object[] {}, csq);
    }
    
    private StringBuffer mountSLQPreManifestosDoctoServico( 
    		Long idManifesto, 
    		Long idDoctoServico, 
    		String tpDoctoServico){
    	
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append( "SELECT ( CASE ")
    		.append("			WHEN (DOCSER.TP_DOCUMENTO_SERVICO = 'CTR') THEN ")
    		.append("				' - ' || conh.dv_conhecimento ")
    		.append(" 		  END ) AS dvDoctoServico, ")
    		.append("    PMD.ID_PRE_MANIFESTO_DOCUMENTO AS idPreManifestoDocumento, ")
    		.append("    DOCSER.ID_DOCTO_SERVICO AS idDoctoServico, ")
    		.append("    DOCSER.NR_DOCTO_SERVICO AS nrDoctoServico, ")
    		.append("    DOCSER.VL_MERCADORIA AS vlMercadoria, ")
    		.append("    DOCSER.VL_TOTAL_DOC_SERVICO AS vlTotalDocServico, ")
    		.append("    DOCSER.DT_PREV_ENTREGA AS dtPrevEntrega, ")
    		.append("    DOCSER.QT_VOLUMES AS qtVolumes, ")
    		.append("    DOCSER.PS_REAL AS psReal, ")
    		.append("	 DOCSER.TP_DOCUMENTO_SERVICO AS tpDocumento, ")
    		.append("    MOEDA.SG_MOEDA AS sgMoeda, ")
    		.append("	 MOEDA.DS_SIMBOLO AS dsSimbolo, ")
    		.append("	 MOEDA.SG_MOEDA AS sgMoedaFrete, ")
    		.append("    MOEDA.DS_SIMBOLO AS dsSimboloFrete, ")
    		.append("	 SERVICO.SG_SERVICO AS sgServico, ")
    		.append("	 F_ORIGEM.SG_FILIAL AS sgFilialOrigem, ")
    		.append("    F_DESTINO.SG_FILIAL AS sgFilialDestino, ")
    		.append("	 P_FILIAL_DEST.NM_FANTASIA AS nmFantasiaFilialDestino, ")
    		.append("	 PESSOA_REM.NM_PESSOA AS clienteRemetente, ")
    		.append("    PESSOA_DES.NM_PESSOA AS clienteDestinatario ")
    		
    		//FROM
    		.append("FROM PRE_MANIFESTO_DOCUMENTO PMD, MANIFESTO MANIFESTO, DOCTO_SERVICO DOCSER, SERVICO SERVICO, ")
    		.append("     MOEDA MOEDA, FILIAL F_ORIGEM, FILIAL F_DESTINO, PESSOA P_FILIAL_DEST, CLIENTE CLIENTE_REM, ")
    		.append("     PESSOA PESSOA_REM, CLIENTE CLIENTE_DES, PESSOA PESSOA_DES, CONHECIMENTO CONH ")
    		
    		//WHERE
    		.append("WHERE PMD.ID_MANIFESTO=MANIFESTO.ID_MANIFESTO ")
    		.append("	   AND PMD.ID_DOCTO_SERVICO=DOCSER.ID_DOCTO_SERVICO ")
    		.append("	   AND DOCSER.id_docto_servico=CONH.ID_CONHECIMENTO(+) ")
    		.append("  	   AND DOCSER.ID_SERVICO=SERVICO.ID_SERVICO ")
    		.append("      AND DOCSER.ID_MOEDA=MOEDA.ID_MOEDA(+)  ")
    		.append("      AND DOCSER.ID_FILIAL_ORIGEM=F_ORIGEM.ID_FILIAL(+) ")
    		.append("      AND DOCSER.ID_FILIAL_DESTINO=F_DESTINO.ID_FILIAL(+) ")
    		.append("      AND F_DESTINO.ID_FILIAL=P_FILIAL_DEST.ID_PESSOA(+) ")
    		.append("      AND DOCSER.ID_CLIENTE_REMETENTE=CLIENTE_REM.ID_CLIENTE(+) ")
    		.append("      AND CLIENTE_REM.ID_CLIENTE=PESSOA_REM.ID_PESSOA(+) ")
    		.append("      AND DOCSER.ID_CLIENTE_DESTINATARIO=CLIENTE_DES.ID_CLIENTE(+) ")
    		.append("      AND CLIENTE_DES.ID_CLIENTE=PESSOA_DES.ID_PESSOA(+) ")
    	
			//Criteria
    		.append(" AND MANIFESTO.ID_MANIFESTO = " + idManifesto );
			
			if (idDoctoServico!=null) {
				sql.append(" DOCSER.ID_DOCTO_SERVICO = " + idDoctoServico );
			}
			
			if ( StringUtils.isNotBlank(tpDoctoServico) ){
				sql.append(" DOCSER.TP_DOCUMENTO_SERVICO = " + tpDoctoServico );
			}
			
			sql.append(" ORDER BY 	F_ORIGEM.SG_FILIAL, DOCSER.NR_DOCTO_SERVICO ");

			return sql;
    }
    
    /**
     * Gera a consulta para captura de manifesto docto servico por id manifesto.
     * 
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDoctoServico
     * @param idFilialOrigem
     * @return
     */
    private SqlTemplate getPreManifestosDoctoServicoByidManifestoSqlTemplate(
    		Long idManifesto, 
    		Long idDoctoServico, 
    		String tpDoctoServico, 
    		Long idFilialDoctoServico){
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(PreManifestoDocumento.class.getName() + " preManifestoDocumento " +
				"join preManifestoDocumento.manifesto manifesto " +
				"join preManifestoDocumento.doctoServico doctoServico " +
				"join doctoServico.servico servico " +
  				"left join doctoServico.moeda moeda " +
  				"left join doctoServico.filialByIdFilialOrigem filialOrigem " +
  				"left join doctoServico.filialByIdFilialDestino filialDestino " +
  				"left join filialDestino.pessoa pessoaFilialDestino " +
  				"left join doctoServico.clienteByIdClienteRemetente clienteRemetente " +
  				"left join clienteRemetente.pessoa pessoaClienteRemetente " +
				"left join doctoServico.clienteByIdClienteDestinatario clienteDestinatario " +
  				"left join clienteDestinatario.pessoa pessoaClienteDestinatario " +
				"left join clienteRemetente.pessoa pessoaRemetente");
		
		//Criteria
		sql.addCriteria("manifesto.idManifesto", "=", idManifesto);
		
		if (idDoctoServico!=null) {
			sql.addCriteria("doctoServico.idDoctoServico", "=", idDoctoServico);
		}
		
		if ((tpDoctoServico!=null) && (!tpDoctoServico.equals(""))) { 
			sql.addCriteria("doctoServico.tpDocumentoServico", "=", tpDoctoServico);
		}
		
		if (idFilialDoctoServico!=null) {
			sql.addCriteria("filialOrigem.idFilial", "=", idFilialDoctoServico);		
		}
		
		return sql;
    }
    
    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do Manifesto
     * @param idManifesto
     * @param idDoctoServico
     * @return
     */
    public PreManifestoDocumento findPreManifestoDocumentoByIdManifestoDocto(Long idManifesto,Long idDoctoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class);
		dc.add( Restrictions.eq("manifesto.id", idManifesto));
		dc.add( Restrictions.eq("doctoServico.id", idDoctoServico));
		dc.addOrder(Order.desc("nrOrdem"));
		return (PreManifestoDocumento)getAdsmHibernateTemplate().findUniqueResult(dc);		
    }
    
    public PreManifestoDocumento findPreManifestoDocumentoByIdManifesto(Long idManifesto,Integer nrOrdem) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class);
		dc.add( Restrictions.eq("manifesto.id", idManifesto));
		dc.add( Restrictions.eq("nrOrdem", nrOrdem));
		return (PreManifestoDocumento)getAdsmHibernateTemplate().findUniqueResult(dc);		
    }
    
    /**
     * Retorna um <code>PreManifestoDocumento</code> que possui um alias com
     * manifesto e controleCarga.
     * 
     * @param idPreManifestoDocumento
     * 
     * @return
     */
    public PreManifestoDocumento findPreManifestoDocumento(Long idPreManifestoDocumento) {
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PreManifestoDocumento.class)
    		.createAlias("manifesto", "manifesto")
    		.createAlias("manifesto.controleCarga", "controleCarga")
    		.add(Restrictions.eq("idPreManifestoDocumento", idPreManifestoDocumento));
    	
    	return (PreManifestoDocumento) getAdsmHibernateTemplate().findByDetachedCriteria(detachedCriteria).get(0);
    }
    
    /**
     * verifica se existe uma controle de carga para o documento de servico 
     * @param idDoctoServico
     * @return
     */
    public boolean findCCByIdDoctoServico(Long idDoctoServico) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("1");
    	
    	hql.addFrom(PreManifestoDocumento.class.getName()+ " pmd " +
    			"join pmd.doctoServico ds " +
    			"join pmd.manifesto man " +
    			"join man.controleCarga cc ");
    	
    	hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
    	hql.addCriteria("cc.tpStatusControleCarga","<>", "CA");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
    }

    /**
     * 
     * @param idManifesto
     * @return
     */
    public Integer findMaxNrOrdemByIdManifesto(Long idManifesto){
    	StringBuffer sql = new StringBuffer()
    	.append("select new map(pmd.nrOrdem as nrOrdem) ")
    	.append("from ")
    	.append(PreManifestoDocumento.class.getName() + " as pmd ")
    	.append("where ")
    	.append("pmd.manifesto.id = ? ")
    	.append("and pmd.nrOrdem = ")
    		.append("(select max(pmd1.nrOrdem) ")
    		.append("from ")
    		.append(PreManifestoDocumento.class.getName() + " as pmd1 ")
    		.append("where ")
        	.append("pmd1.manifesto.id = ?) ");
    
    	List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idManifesto, idManifesto});
    	if (!list.isEmpty()) {
    		return (Integer) ((Map)list.get(0)).get("nrOrdem");
		} else {
			return Integer.valueOf(0);
		}
    	
    }


    /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	// remove pre manifesto volume 
    	StringBuffer sql = new StringBuffer()
    	.append("delete from ")
    	.append(PreManifestoVolume.class.getName()).append(" as pmv ")
    	.append(" where exists(select 1 from ")
    	.append(PreManifestoDocumento.class.getName()).append(" as pmd ")
    	.append(" where pmd = pmv.preManifestoDocumento and exists(select 1 from  ")
  		.append(Manifesto.class.getName()).append(" m ")
  		.append("where m.controleCarga.id = ? and m =pmd.manifesto  )) ");
    	
    	List param = new ArrayList();
    	param.add(idControleCarga);

    	super.executeHql(sql.toString(), param);
    	
    	sql = new StringBuffer()
    	.append("delete from ")
    	.append(PreManifestoDocumento.class.getName()).append(" as pmd ")
    	.append(" where exists ")
		.append("(select m.id as idManifesto from ")
		.append(Manifesto.class.getName()).append(" m ")
		.append("where m.controleCarga.id = ? and pmd.manifesto =m) ");
    	


    	super.executeHql(sql.toString(), param);
    }
    
    /**
     * Método que retorna uma list de objetos de PreManifestoDocumento pelo ID do Controle de Carga
     * @param idControleCarga
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PreManifestoDocumento> findByControleCarga(Long idControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class);
		dc.createAlias("manifesto.controleCarga", "controleCarga");
		dc.setFetchMode("manifesto", FetchMode.JOIN);
		dc.setFetchMode("doctoServico", FetchMode.JOIN);
		dc.setFetchMode("doctoServico.filialByIdFilialDestino", FetchMode.JOIN);		
		dc.setFetchMode("doctoServico.filialByIdFilialOrigem", FetchMode.JOIN);		
		dc.setFetchMode("controleCarga", FetchMode.JOIN);
		
		dc.add( Restrictions.eq("controleCarga.id", idControleCarga));		
		return super.findByDetachedCriteria(dc);
    }
    
    public List<Map> findDoctoServicoDivergenteEntrega(Long idControleCarga) {
    	StringBuilder sqlParciais = new StringBuilder()
    	.append("select new map(con.idDoctoServico as idDoctoServico, ")
	    	.append("f.sgFilial as sgFilialDocumento, ")
	    	.append("con.nrConhecimento as nrConhecimento, ")
	    	.append("con.qtVolumes as qtVolumes, ")
	    	.append("sum(cdv.qtVolumes) as qtVolumesCarregados) ")
    	.append("from ")
    	.append(Conhecimento.class.getName() + " as con ")
    	.append("join con.filialOrigem f ")
    	.append("join con.preManifestoDocumentos pmd ")
    	.append("join con.notaFiscalConhecimentos nfc ")
    	.append("join nfc.volumeNotaFiscais vnf ")
    	.append("join vnf.carregamentoDescargaVolumes cdv ")
    	.append("join cdv.carregamentoDescarga cd ")
    	.append("join cd.controleCarga cc ")
    	.append("join pmd.manifesto m ")
    	.append("where ")
    	.append("cc.idControleCarga = ? ")
    	.append("and cd.tpOperacao = 'C' ")
    	.append("and m.tpStatusManifesto = 'EC' ")
    	.append("and m.controleCarga.id = cc.idControleCarga ")
    		.append("group by con.idDoctoServico, ")
    		.append("f.sgFilial, ")
    		.append("con.nrConhecimento, ")    		
    		.append("con.qtVolumes ");
    
    	List<Map> listParciais = getAdsmHibernateTemplate().find(sqlParciais.toString(), new Object[] {idControleCarga});

    	StringBuilder sqlMesmoAWB = new StringBuilder()
    	.append("select new map(con.idDoctoServico as idDoctoServico, ")
	    	.append("f.sgFilial as sgFilialDocumento, ")
	    	.append("con.nrConhecimento as nrConhecimento, ")
	    	.append("con.qtVolumes as qtVolumes, ")
	    	.append("ca.awb.id as idAwb, ")
	    	.append("0 as qtVolumesCarregados) ")
    	.append("from ")
    	.append(Conhecimento.class.getName() + " as con ")
    	.append("join con.filialOrigem f ")
    	.append("join con.ctoAwbs ca ")
    	.append("where con.tpSituacaoConhecimento != 'C' ")
    	.append("and ca.awb.id in (")
    		.append("select awb2.idAwb ")
			.append("from ")
	    	.append(Conhecimento.class.getName() + " as con2 ")
	    	.append("join con2.ctoAwbs ca2 ")
	    	.append("join con2.preManifestoDocumentos p ")
	    	.append("join ca2.awb awb2 ")
	    	.append("join con2.notaFiscalConhecimentos n ")
	    	.append("join n.volumeNotaFiscais vnf ")
	    	.append("join vnf.carregamentoDescargaVolumes cdv ")
	    	.append("join cdv.carregamentoDescarga cd ")
	    	.append("join cd.controleCarga cc ")
	    	.append("join p.manifesto m ")
	    	.append("where ")
	    		.append("cc.idControleCarga = ? ")
	    		.append("and cd.tpOperacao = 'C' ")
		    	.append("and m.tpStatusManifesto = 'EC' ")
		    	.append("and m.controleCarga.id = cc.idControleCarga ")
		    	.append("and m.filialByIdFilialOrigem.id = awb2.filialByIdFilialOrigem.id ")
	    		.append("and awb2.tpStatusAwb = 'P' ")
	    		.append("and m.controleCarga.id = cc.idControleCarga ")
    	.append(")");

    	List<Map> listMesmoAWB = getAdsmHibernateTemplate().find(sqlMesmoAWB.toString(), new Object[] {idControleCarga});
    	
    	for(Map conAWB : listMesmoAWB){
    		boolean add = true;
    		for(Map conParcial : listParciais){
    			if(((Long)conAWB.get("idDoctoServico")).equals((Long)conParcial.get("idDoctoServico"))){
    				conParcial.put("idAwb", conAWB.get("idAwb"));
    				add = false;
    				break;
        		}
    		}
    		
    		if(add){
    			listParciais.add(conAWB);
    		}
    	}
    	
    	return listParciais;
	}
    
    public void removePreManifestoDocumento(PreManifestoDocumento preManifestoDocumento){
    	getAdsmHibernateTemplate().delete(preManifestoDocumento);
    }
    
    public void storePreManifestoDocumento(PreManifestoDocumento preManifestoDocumento){
    	getAdsmHibernateTemplate().saveOrUpdate(preManifestoDocumento);
    }
    
	public Integer validateDoctoServico(Long idManifesto, Long idConhecimento) {
		if (idManifesto == null || idConhecimento == null) {
			return null;
		}
		StringBuilder hql = new StringBuilder("select pmd from PRE_MANIFESTO_DOCUMENTO pmd");
		hql.append(" where pmd.id_manifesto = ? and");
		hql.append(" pmd.id_docto_servico = ?");

		Integer value = (Integer) getAdsmHibernateTemplate().getRowCountBySql(hql.toString(), new Object[] {idManifesto , idConhecimento});
		
		return value;
		
	}
    
	public boolean validateExisteDocumentosNaoEmitidos(Long idControleCarga, List<Long> idsManifesto){
		
		SqlTemplate hql = new SqlTemplate(); 
    	hql.addProjection("1");
    	
    	hql.addFrom(PreManifestoDocumento.class.getName()+ " pmd " +
    			"inner join pmd.doctoServico ds " +
    			"inner join ds.pedidoColeta pc " +
    			"inner join ds.clienteByIdClienteRemetente rem " +
    			"inner join pmd.manifesto m " +
    			"inner join m.controleCarga cc ");
    	
    	hql.addCriteria("cc.idControleCarga","=", idControleCarga);//
    	hql.addCriteriaIn("m.idManifesto", idsManifesto);
    	hql.addCriteria("ds.tpSituacaoConhecimento","<>", "E");
    	
    	return !getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).isEmpty();		
	}
    
	public BigDecimal findPsRealTotalByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT SUM(ds.psReal) ")
		.append("FROM " + ControleCarga.class.getName() + " cc, ")
		.append(Manifesto.class.getName() + " m, ")
		.append(PreManifestoDocumento.class.getName() + " pmd, ")
		.append(DoctoServico.class.getName() + " ds ")
		.append("WHERE m.controleCarga = cc ")
		.append("AND pmd.manifesto = m ")
		.append("AND pmd.doctoServico = ds ")
		.append("AND cc.idControleCarga = ? ")
		.append("AND m.tpStatusManifesto IN ('PM', 'CC', 'EC')");

		return (BigDecimal)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga});		
	}

	
    
}