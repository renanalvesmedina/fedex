package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescontoDAO extends BaseCrudDao<Desconto, Long>
{
    
    private JdbcTemplate jdbcTemplate;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
    	
        return Desconto.class;
    }

    @Override
    public Desconto findById(Long id) {
    	return super.findById(id);
    }
    /**
     * Retorna o desconto com o devedor_doc_serv_fat.
     * 
     * @param Long idDesconto
     * @return Desconto
     * */    
    public Desconto findByIdWithDevedor(Long idDesconto){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("d");
    	sql.addFrom(Desconto.class.getName(),"d " +
    										 "join fetch d.devedorDocServFat as dev " );   	
    	sql.addCriteria("d.idDesconto","=",idDesconto);
    	List lstDesconto = this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    	if (lstDesconto.size() == 1){
    		return (Desconto)lstDesconto.get(0);
    	} else {
    		return null;
    	} 
    }
    
    /**
     * Retorna a lista de desconto por devedorDocServFat.
     * 
     * @author Mickaël Jalbert
     * @param Long idDoctoServico
     * @return List
     * */
    public List findByDevedorDocServFatSituacao(Long idDevedorDocServFat, String tpSituacaoAprovacao){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("d");
    	
    	sql.addInnerJoin(Desconto.class.getName(),"d ");
    	sql.addLeftOuterJoin("fetch d.reciboDesconto", "recdes");
    	sql.addLeftOuterJoin("fetch recdes.filial", "filrec");
    	sql.addLeftOuterJoin("fetch d.demonstrativoDesconto", "demdes");
    	sql.addLeftOuterJoin("fetch demdes.filial", "fildem");
    	
    	
    	
    	sql.addCriteria("d.devedorDocServFat.id","=",idDevedorDocServFat);
    	sql.addCriteria("d.tpSituacaoAprovacao","=",tpSituacaoAprovacao);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    
    /**
     * Retorna o desconto a partir de um idDoctoServico.
     * @param idDoctoServico
     * @return
     */
    public Desconto findByDoctoServico(Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "desc");
		dc.createAlias("desc.devedorDocServFat","dds");
		dc.createAlias("dds.doctoServico","doc");
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("desc.vlDesconto"), "vlDesconto");
		dc.setProjection(pl);
		
		dc.add(Restrictions.eq("doc.id", idDoctoServico));
		
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Desconto.class));
		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result != null && !result.isEmpty()) {
			return (Desconto) result.get(0);
		}
		return null;
    }
    
    /**
     * Retorna a lista de desconto por fatura.
     * 
     * @author Mickaël Jalbert
     * @since 19/04/2006
     * 
     * @param Long idFatura
     * @return List
     * */    
    public List findByFatura(Long idFatura){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("distinct des");
    	
    	sql.addInnerJoin(Desconto.class.getName(), "des");
    	sql.addInnerJoin("des.devedorDocServFat", "dev");
    	sql.addInnerJoin("dev.itemFaturas", "itefat");
    	sql.addInnerJoin("itefat.fatura", "fat");
    	sql.addCriteria("fat.id","=",idFatura);
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());    	    
    }
    
    /**
     * Retorna a lista de desconto do reciboDesconto informado.
     * 
     * @author Mickaël Jalbert
     * @since 16/01/2007
     * 
     * @param idReciboDesconto
     * 
     * @return lista de desconto
     */ 
    public List findByReciboDesconto(Long idReciboDesconto){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("des");
    	
    	sql.addInnerJoin(Desconto.class.getName(), "des");
    	sql.addCriteria("des.reciboDesconto.id","=",idReciboDesconto);
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());    	    
    }    
    
    /**
     * Retorna true se existe um desconto pendente de aprovação ligado a fatura informada.
     * 
     * @author Mickaël Jalbert
     * @since 28/09/2006
     * 
     * @param Long idFatura
     * @return Boolean
     * */    
    public Boolean existsDescontoPendenteByFatura(Long idFatura){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("count(des.id)");
    	
    	sql.addInnerJoin(Desconto.class.getName(), "des");
    	sql.addInnerJoin("des.devedorDocServFat", "dev");
    	sql.addInnerJoin("dev.itemFaturas", "itefat");
    	sql.addInnerJoin("itefat.fatura", "fat");
    	sql.addCriteria("fat.id","=",idFatura);
    	sql.addCriteria("des.tpSituacaoAprovacao","=","E");
    	
    	Integer qtDescontos = (Integer)this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0);
    	
    	if (qtDescontos > 0){
    		return Boolean.TRUE;
    	} else {
    		return Boolean.FALSE;
    	}
    }    
    
    @Override
    public void removeById(Long id) {
    	super.removeById(id);
    }
    
    /**
     * Busca os dados que serão mostrados na listagem
     * @param map Critérios de pesquisa
     * @param findDef Dados de paginação
     * @return ResultSetPage Objeto com os dados a serem mostrados na grid e dados de paginação
     */
    public ResultSetPage findPaginated(Map map, FindDefinition findDef) {
        TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stConhecimento      = montaQueryConhecimento(tfm);
        
        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("ID_DESCONTO", Hibernate.LONG);
                sqlQuery.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);
                sqlQuery.addScalar("TP_IDENTIFICACAO", Hibernate.STRING);
                sqlQuery.addScalar("NM_PESSOA", Hibernate.STRING);
                sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
                sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
                sqlQuery.addScalar("NR_DOCUMENTO", Hibernate.STRING);
                sqlQuery.addScalar("DS_MOTIVO_DESCONTO", Hibernate.STRING);
                sqlQuery.addScalar("DS_VALOR_DOMINIO", Hibernate.STRING);
                sqlQuery.addScalar("DS_TP_DOCUMENTO_SERVICO", Hibernate.STRING);
            }
        };
        
        stConhecimento.addProjection("D.ID_DESCONTO, " +
                "CP.NR_IDENTIFICACAO, " +
                "CP.TP_IDENTIFICACAO, " +
                "CP.NM_PESSOA, " +
                "DS.TP_DOCUMENTO_SERVICO, " +
                "FC.SG_FILIAL, " +
                "DS.NR_DOCTO_SERVICO AS NR_DOCUMENTO, " +
                PropertyVarcharI18nProjection.createProjection("MD.DS_MOTIVO_DESCONTO_I","DS_MOTIVO_DESCONTO") + ", " +
                PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","DS_VALOR_DOMINIO") + ", " +
                PropertyVarcharI18nProjection.createProjection("VDDS.DS_VALOR_DOMINIO_I","DS_TP_DOCUMENTO_SERVICO"));
        
        stConhecimento.addOrderBy("NM_PESSOA");
        stConhecimento.addOrderBy("DS_TP_DOCUMENTO_SERVICO");
        stConhecimento.addOrderBy("SG_FILIAL");
        stConhecimento.addOrderBy("NR_DOCUMENTO");
        
        return getAdsmHibernateTemplate().findPaginatedBySql(stConhecimento.getSql(),
                                                             findDef.getCurrentPage(),
                                                             findDef.getPageSize(),
                                                             stConhecimento.getCriteria(),
                                                             configSql);
    }
    
    /**
     * Busca quantos registros serão mostrados na listagem
     * @param map Critérios de pesquisa
     * @return Inteiro que indica a quantidade de registros retornados na pesquisa
     */
    public Integer getRowCount(Map map) {
        
        TypedFlatMap tfm = (TypedFlatMap) map;
        
        SqlTemplate stConhecimento      = montaQueryConhecimento(tfm);
        
        return getAdsmHibernateTemplate().getRowCountBySql(stConhecimento.getSql(),
                                                           stConhecimento.getCriteria());
        
    }
    
    /**
     * Monta a query de pesquisa dos Conhecimentos
     * @param tfm Critérios de Pesquisa
     * @return String da query
     */
    private SqlTemplate montaQueryConhecimento(TypedFlatMap tfm) {
        SqlTemplate sql = new SqlTemplate();        
        
        
        sql.addFrom("DESCONTO D " +
                    "   INNER JOIN MOTIVO_DESCONTO MD    ON D.ID_MOTIVO_DESCONTO = MD.ID_MOTIVO_DESCONTO " +
                    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON D.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
                    "   INNER JOIN FILIAL FIL_COBRANCA       ON DDSF.ID_FILIAL = FIL_COBRANCA.ID_FILIAL " +
                    "   INNER JOIN DOCTO_SERVICO DS          ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN CLIENTE C                 ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
                    "   INNER JOIN PESSOA CP                 ON C.ID_CLIENTE = CP.ID_PESSOA " +
                    "   INNER JOIN MOEDA M                   ON DS.ID_MOEDA = M.ID_MOEDA " +
                    "   INNER JOIN FILIAL FC                 ON DS.ID_FILIAL_ORIGEM = FC.ID_FILIAL");
        sql.addFrom("VALOR_DOMINIO VD " +
                    "   INNER JOIN DOMINIO DOM               ON VD.ID_DOMINIO = DOM.ID_DOMINIO");
        sql.addFrom("VALOR_DOMINIO VDDS " +
                    "   INNER JOIN DOMINIO DOMDS             ON VDDS.ID_DOMINIO = DOMDS.ID_DOMINIO");
        
        sql.addCustomCriteria("DDSF.TP_SITUACAO_COBRANCA IN ( 'P', 'C' )");  
        
        sql.addCustomCriteria("LOWER(DOM.NM_DOMINIO) = 'dm_status_workflow'");
        sql.addCustomCriteria("LOWER(DOMDS.NM_DOMINIO) = 'dm_tipo_documento_servico'");
        sql.addCustomCriteria("LOWER(VD.VL_VALOR_DOMINIO) = LOWER(D.TP_SITUACAO_APROVACAO)");
        sql.addCustomCriteria("LOWER(VDDS.VL_VALOR_DOMINIO) = LOWER(DS.TP_DOCUMENTO_SERVICO)");
		sql.addCriteriaIn("DS.TP_DOCUMENTO_SERVICO", new Object[] { "CTR", "NFT", "NFS", "CTE", "NTE", "NSE" });
        
        if(tfm.get("devedorDocServFat") != null){
        	sql.addCriteria("DDSF.ID_DEVEDOR_DOC_SERV_FAT","=", ((Map)tfm.get("devedorDocServFat")).get("idDevedorDocServFat") );	
        }                
        
        if(tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico") != null){
        	sql.addCriteria("DS.TP_DOCUMENTO_SERVICO","=", tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico"));        	
        }

        if(tfm.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial") != null){        	
        	sql.addCriteria("FC.ID_FILIAL","=", tfm.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"));
        }
        
        if(tfm.get("motivoDesconto") != null){
        	sql.addCriteria("D.ID_MOTIVO_DESCONTO","=",  ((Map)tfm.get("motivoDesconto")).get("idMotivoDesconto") );
        }
        
        if(tfm.getLong("motivoDesconto.idMotivoDesconto") != null){
        	sql.addCriteria("MD.ID_MOTIVO_DESCONTO","=",tfm.getLong("motivoDesconto.idMotivoDesconto"));        	
        }
        
        if(tfm.get("cliente") != null){
        	sql.addCriteria("DDSF.ID_CLIENTE","=", ((Map)tfm.get("cliente")).get("idCliente") );
        }        

        if(tfm.get("filialCobranca") != null){
        	sql.addCriteria("FIL_COBRANCA.ID_FILIAL","=",((Map)tfm.get("filialCobranca")).get("idFilial"));
        }        

        sql.addCriteria("D.TP_SITUACAO_APROVACAO","=", tfm.getString("situacaoAprovacao"));
        sql.addCriteria("D.TP_SETOR_CAUSADOR_ABATIMENTO","=",tfm.getLong("setorCausadorAbatimento"));
        
        
        return sql;
    }
    
    /**
     * Monta o sql para a busca dos dados do cliente e do valor do documento com a associação da tabela de CONHECIMENTO (CTRC)
     * @param tfm Critérios de pesquisa
     * @return SqlTemplate com o sql montado e os critérios setados
     */
    public SqlTemplate sqlDadosClienteConhecimento(TypedFlatMap tfm){
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("CP.NR_IDENTIFICACAO, " +
        		          "CP.TP_IDENTIFICACAO, " +
                          "CP.NM_PESSOA, " +
                          "M.DS_SIMBOLO, " +
                          "M.SG_MOEDA, " +
                          "M.ID_MOEDA, " +
                          "DDSF.ID_DEVEDOR_DOC_SERV_FAT, " +
                          "DDSF.VL_DEVIDO, " +
                          "DDSF.TP_SITUACAO_COBRANCA, " +
                          "C.ID_CLIENTE, " +
                          "SER.ID_SERVICO, " +
                          "SER.TP_MODAL, " +
                          "SER.TP_ABRANGENCIA," +
                          "DEVEDOR_FILIAL.ID_FILIAL, " +
                          "DEVEDOR_FILIAL.SG_FILIAL SG_FILIAL_COBRANCA, " +
                          "PES_FIL_COBRANCA.NM_FANTASIA NM_FILIAL_COBRANCA, " +
                          "CON.NR_CONHECIMENTO NR_DOCUMENTO");        
        
        sql.addFrom("DOCTO_SERVICO DS " +
                    "   INNER JOIN MOEDA M                   ON DS.ID_MOEDA = M.ID_MOEDA " +
                    "   INNER JOIN CONHECIMENTO CON          ON DS.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO " +
                    "   INNER JOIN FILIAL FC                 ON CON.ID_FILIAL_ORIGEM = FC.ID_FILIAL" +
                    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN CLIENTE C                 ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
                    "   INNER JOIN PESSOA CP                 ON C.ID_CLIENTE = CP.ID_PESSOA " +
                    "   INNER JOIN SERVICO SER               ON DS.ID_SERVICO = SER.ID_SERVICO " +
                    "   INNER JOIN FILIAL DEVEDOR_FILIAL     ON DDSF.ID_FILIAL = DEVEDOR_FILIAL.ID_FILIAL " +
                    "	INNER JOIN PESSOA PES_FIL_COBRANCA   ON DEVEDOR_FILIAL.ID_FILIAL = PES_FIL_COBRANCA.ID_PESSOA");                    
        
        sql.addCustomCriteria("CON.TP_SITUACAO_CONHECIMENTO IN ( 'E', 'B' )");        
        sql.addCriteria("CON.ID_CONHECIMENTO","=",tfm.getString("idConhecimento"));
        sql.addCriteria("DS.TP_DOCUMENTO_SERVICO","=", tfm.getString("tpDocumentoServico"));
        sql.addCriteria("FC.ID_FILIAL","=", tfm.getLong("idFilial"));
        
        return sql;
        
    }
    
    /**
     * Monta o sql para a busca dos dados do cliente e do valor do documento com a associação da tabela de CTO_INTERNACIONAL (CRT)
     * @param tfm Critérios de pesquisa
     * @return SqlTemplate com o sql montado e os critérios setados
     * @deprecated Este método ficou deprecated pois documentos internacionais não recebem desconto antes de serem faturados
     */
    public SqlTemplate sqlDadosClienteCtoInternacional(TypedFlatMap tfm){
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("CP.NR_IDENTIFICACAO, " +
        		          "CP.TP_IDENTIFICACAO, " +
                          "CP.NM_PESSOA, " +
                          "M.DS_SIMBOLO, " +
                          "M.SG_MOEDA, " +
                          "M.ID_MOEDA, " +
                          "DDSF.ID_DEVEDOR_DOC_SERV_FAT, " +
                          "DDSF.VL_DEVIDO, " +
                          "DDSF.TP_SITUACAO_COBRANCA, " +
                          "C.ID_CLIENTE, " +
                          "SER.ID_SERVICO, " +
                          "SER.TP_MODAL, " +
                          "SER.TP_ABRANGENCIA, " +
                          "DEVEDOR_FILIAL.ID_FILIAL," +
                          "CI.NR_CRT NR_DOCUMENTO");         
        
        sql.addFrom("DOCTO_SERVICO DS " +
                    "   INNER JOIN MOEDA M                   ON DS.ID_MOEDA = M.ID_MOEDA " +
                    "   INNER JOIN CTO_INTERNACIONAL CI      ON DS.ID_DOCTO_SERVICO = CI.ID_CTO_INTERNACIONAL " +
                    "   INNER JOIN FILIAL FC                 ON CI.ID_FILIAL_ORIGEM = FC.ID_FILIAL" +
                    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN CLIENTE C                 ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
                    "   INNER JOIN PESSOA CP                 ON C.ID_CLIENTE = CP.ID_PESSOA " +
                    "   INNER JOIN SERVICO SER               ON DS.ID_SERVICO = SER.ID_SERVICO " +
                    "   INNER JOIN FILIAL DEVEDOR_FILIAL     ON DDSF.ID_FILIAL = DEVEDOR_FILIAL.ID_FILIAL");                    
        
        sql.addCustomCriteria("CI.TP_SITUACAO_CRT = 'E'");        
        sql.addCriteria("CI.ID_CTO_INTERNACIONAL","=",tfm.getString("idConhecimento"));
        sql.addCriteria("DS.TP_DOCUMENTO_SERVICO","=", tfm.getString("tpDocumentoServico"));
        sql.addCriteria("FC.ID_FILIAL","=", tfm.getLong("idFilial"));   
        
        return sql;
        
        
    }
    
    /**
     * Monta o sql para a busca dos dados do cliente e do valor do documento com a associação da tabela de NOTAS_FISCAIS_SERVICO (NFS)
     * @param tfm Critérios de pesquisa
     * @return SqlTemplate com o sql montado e os critérios setados
     */
    public SqlTemplate sqlDadosClienteNotasFiscais(TypedFlatMap tfm){
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("CP.NR_IDENTIFICACAO, " +
        		          "CP.TP_IDENTIFICACAO, " +
                          "CP.NM_PESSOA, " +
                          "M.DS_SIMBOLO, " +
                          "M.SG_MOEDA, " +
                          "M.ID_MOEDA, " +
                          "DDSF.ID_DEVEDOR_DOC_SERV_FAT, " +
                          "DDSF.VL_DEVIDO, " +
                          "DDSF.TP_SITUACAO_COBRANCA, " +
                          "C.ID_CLIENTE, " +
                          "SER.ID_SERVICO, " +
                          "SER.TP_MODAL, " +
                          "SER.TP_ABRANGENCIA, " +
                          "DEVEDOR_FILIAL.ID_FILIAL, " +
                          "DEVEDOR_FILIAL.SG_FILIAL SG_FILIAL_COBRANCA, " +
                          "PES_FIL_COBRANCA.NM_FANTASIA NM_FILIAL_COBRANCA, " +
                          "NFS.NR_NOTA_FISCAL_SERVICO NR_DOCUMENTO ");        
        
        sql.addFrom("DOCTO_SERVICO DS " +
                    "   INNER JOIN MOEDA M                   ON DS.ID_MOEDA = M.ID_MOEDA " +
                    "   INNER JOIN NOTA_FISCAL_SERVICO NFS   ON DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO " +
                    "   INNER JOIN FILIAL FC                 ON NFS.ID_FILIAL_ORIGEM = FC.ID_FILIAL" +
                    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN CLIENTE C                 ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
                    "   INNER JOIN PESSOA CP                 ON C.ID_CLIENTE = CP.ID_PESSOA " +
                    "   INNER JOIN SERVICO SER               ON DS.ID_SERVICO = SER.ID_SERVICO " +
                    "   INNER JOIN FILIAL DEVEDOR_FILIAL     ON DDSF.ID_FILIAL = DEVEDOR_FILIAL.ID_FILIAL" +
                    "	INNER JOIN PESSOA PES_FIL_COBRANCA   ON DEVEDOR_FILIAL.ID_FILIAL = PES_FIL_COBRANCA.ID_PESSOA");                     
        
        sql.addCustomCriteria("NFS.TP_SITUACAO_NF = 'E'");        
        sql.addCriteria("NFS.ID_NOTA_FISCAL_SERVICO","=",tfm.getString("idConhecimento"));
        sql.addCriteria("DS.TP_DOCUMENTO_SERVICO","=", tfm.getString("tpDocumentoServico"));
        sql.addCriteria("FC.ID_FILIAL","=", tfm.getLong("idFilial")); 
        
        return sql;
    }
    
    /**
     * Busca os dados do responsável e o valor do documento
     * @param tfm Critérios de pesquisa
     * @return dados necessários para desconto
     */
    public List findDadosClienteEValores(TypedFlatMap tfm){
        
        SqlTemplate sqlConhecimento      = sqlDadosClienteConhecimento(tfm);
        SqlTemplate sqlNotasFiscais      = sqlDadosClienteNotasFiscais(tfm);
        
        sqlConhecimento.addCriteriaValue(sqlNotasFiscais.getCriteria());
        
        List descontos = jdbcTemplate.queryForList(sqlConhecimento.getSql() + 
      		                                       " \nUNION\n " +
      		                                       sqlNotasFiscais.getSql(),
      		                                       sqlConhecimento.getCriteria());
        
        return descontos;        

    }
    
    /**
     * Busca um desconto pelo seu ID
     * @param idDesconto Identificador do Desconto
     * @return Desconto encontrado
     */
    public List findByIdList(Long idDesconto) {

        SqlTemplate stConhecimento      = montaQueryFindByIdConhecimento((Long)idDesconto);
        SqlTemplate stNotasFiscais      = montaQueryFindByIdNotaFiscalServico((Long)idDesconto);
        
        stConhecimento.addCriteriaValue(stNotasFiscais.getCriteria());
        
        List retorno = jdbcTemplate.queryForList(stConhecimento.getSql() + 
                                                 " UNION " + 
                                                 stNotasFiscais.getSql(),
                                                 stConhecimento.getCriteria());
        
        return retorno;
        
    }
    
    /**
     * Monta o sql para a busca do Desconto pelo ID com a associação da tabela de CONHECIMENTO (CTRC)
     * @param idDesconto Identificador do Desconto
     * @return SqlTemplate com o sql montado e os critérios setados
     */
    private SqlTemplate montaQueryFindByIdConhecimento(Long idDesconto){
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("D.ID_DESCONTO, " +
                          "DS.ID_DOCTO_SERVICO, " +
                          "DS.TP_DOCUMENTO_SERVICO, " +
                          "DSF.ID_FILIAL, " +
                          "DSF.SG_FILIAL, " +
                          "CON.NR_CONHECIMENTO NR_DOCUMENTO, " +
                          "MD.ID_MOTIVO_DESCONTO, " +
                          PropertyVarcharI18nProjection.createProjection("MD.DS_MOTIVO_DESCONTO_I","DS_MOTIVO_DESCONTO") + ", " +
                          "C.ID_CLIENTE, " +
                          "CP.TP_IDENTIFICACAO, " +
                          "CP.NR_IDENTIFICACAO, " +
                          "CP.NM_PESSOA, " +
                          "D.TP_SITUACAO_APROVACAO, " +
                          "D.TP_SETOR_CAUSADOR_ABATIMENTO, " +
                          "D.VL_DESCONTO, " +
                          "M.ID_MOEDA, " +
                          "M.SG_MOEDA, " +
                          "M.DS_SIMBOLO, " +
                          "DDSF.VL_DEVIDO, " +
                          "D.OB_DESCONTO, " +
                          "D.OB_ACAO_CORRETIVA, " +
                          "SER.ID_SERVICO, " +
                          "SER.TP_MODAL, " +
                          "SER.TP_ABRANGENCIA, " +
                          "D.ID_PENDENCIA, " +
                          "DDSF.ID_DEVEDOR_DOC_SERV_FAT, " +
                          "FD.ID_FILIAL ID_FILIAL_DEVEDOR, " +
                          "FD.SG_FILIAL SG_FILIAL_DEVEDOR, " +
                          "PES_FIL_DEVEDOR.NM_FANTASIA NM_FILIAL_DEVEDOR," +
        				  "D.NR_VERSAO");
        sql.addFrom("DESCONTO D " +
                    "   INNER JOIN MOTIVO_DESCONTO MD    ON D.ID_MOTIVO_DESCONTO = MD.ID_MOTIVO_DESCONTO " +
                    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON D.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
                    "   INNER JOIN DOCTO_SERVICO DS          ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN FILIAL DSF                ON DS.ID_FILIAL_ORIGEM = DSF.ID_FILIAL " +
                    "   INNER JOIN SERVICO SER               ON DS.ID_SERVICO = SER.ID_SERVICO " +
                    "   INNER JOIN CLIENTE C                 ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
                    "   INNER JOIN PESSOA CP                 ON C.ID_CLIENTE = CP.ID_PESSOA " +
                    "   INNER JOIN MOEDA M                   ON DS.ID_MOEDA = M.ID_MOEDA " +
                    "   INNER JOIN CONHECIMENTO CON          ON DS.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO " +
                    "   INNER JOIN FILIAL FD                 ON DDSF.ID_FILIAL = FD.ID_FILIAL " +
                    "	INNER JOIN PESSOA PES_FIL_DEVEDOR    ON FD.ID_FILIAL = PES_FIL_DEVEDOR.ID_PESSOA " );
        
        sql.addCriteria("D.ID_DESCONTO","=", idDesconto);
		sql.addCriteriaIn("DS.TP_DOCUMENTO_SERVICO", new Object[] { "CTR", "NFT", "CTE", "NTE" });
        
        return sql;
    }

    /**
     * Monta o sql para a busca do Desconto pelo ID com a associação da tabela de NOTAS_FISCAIS_SERVICO (NFS)
     * @param idDesconto Identificador do Desconto
     * @return SqlTemplate com o sql montado e os critérios setados
     */
    private SqlTemplate montaQueryFindByIdNotaFiscalServico(Long idDesconto){
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("D.ID_DESCONTO, " +
                          "DS.ID_DOCTO_SERVICO, " +
                          "DS.TP_DOCUMENTO_SERVICO, " +
                          "DSF.ID_FILIAL, " +
                          "DSF.SG_FILIAL, " +
                          "NFS.NR_NOTA_FISCAL_SERVICO, " +
                          "MD.ID_MOTIVO_DESCONTO, " +
                          PropertyVarcharI18nProjection.createProjection("MD.DS_MOTIVO_DESCONTO_I","DS_MOTIVO_DESCONTO") + ", " +
                          "C.ID_CLIENTE, " +
                          "CP.TP_IDENTIFICACAO, " +
                          "CP.NR_IDENTIFICACAO, " +
                          "CP.NM_PESSOA, " +
                          "D.TP_SITUACAO_APROVACAO, " +
                          "D.TP_SETOR_CAUSADOR_ABATIMENTO, " +
                          "D.VL_DESCONTO, " +
                          "M.ID_MOEDA, " +
                          "M.SG_MOEDA, " +
                          "M.DS_SIMBOLO, " +
                          "DDSF.VL_DEVIDO, " +
                          "D.OB_DESCONTO, " +
                          "D.OB_ACAO_CORRETIVA, " +
                          "SER.ID_SERVICO, " +
                          "SER.TP_MODAL, " +
                          "SER.TP_ABRANGENCIA, " +
                          "D.ID_PENDENCIA, " +
                          "DDSF.ID_DEVEDOR_DOC_SERV_FAT, " +
                          "FD.ID_FILIAL ID_FILIAL_DEVEDOR, " +
                          "FD.SG_FILIAL SG_FILIAL_DEVEDOR, " +
                          "PES_FIL_DEVEDOR.NM_FANTASIA NM_FILIAL_DEVEDOR," +
                          "D.NR_VERSAO");
        
        sql.addFrom("DESCONTO D " +
                    "   INNER JOIN MOTIVO_DESCONTO MD    ON D.ID_MOTIVO_DESCONTO = MD.ID_MOTIVO_DESCONTO " +
                    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON D.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
                    "   INNER JOIN DOCTO_SERVICO DS          ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
                    "   INNER JOIN FILIAL DSF                ON DS.ID_FILIAL_ORIGEM = DSF.ID_FILIAL " +
                    "   INNER JOIN SERVICO SER               ON DS.ID_SERVICO = SER.ID_SERVICO " +
                    "   INNER JOIN CLIENTE C                 ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
                    "   INNER JOIN PESSOA CP                 ON C.ID_CLIENTE = CP.ID_PESSOA " +
                    "   INNER JOIN MOEDA M                   ON DS.ID_MOEDA = M.ID_MOEDA " +
                    "   INNER JOIN NOTA_FISCAL_SERVICO NFS   ON DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO " +
                    "   INNER JOIN FILIAL FD                 ON DDSF.ID_FILIAL = FD.ID_FILIAL " +
                    "	INNER JOIN PESSOA PES_FIL_DEVEDOR    ON FD.ID_FILIAL = PES_FIL_DEVEDOR.ID_PESSOA ");
        
        sql.addCriteria("D.ID_DESCONTO","=", idDesconto);
		sql.addCustomCriteria("(DS.TP_DOCUMENTO_SERVICO = 'NFS' OR DS.TP_DOCUMENTO_SERVICO = 'NSE')");
        
        return sql;
    }
    
    /**
     * Retorna a soma do valor devido e do valor do desconto por fatura.
     * 
     * @param Long idFatura
     * @return List
     * */
    public TypedFlatMap findSomaByFatura(Long idFatura){
    	TypedFlatMap retorno = new TypedFlatMap();
    	SqlTemplate sql = new SqlTemplate();
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery(){
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("qtDocumento", Hibernate.LONG);
                sqlQuery.addScalar("vlDesconto", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlTotal", Hibernate.BIG_DECIMAL);
            }
    	};

    	
    	sql.addProjection("count(DEV.ID_DEVEDOR_DOC_SERV_FAT)", "qtDocumento");
    	sql.addProjection("sum(nvl(DES.VL_DESCONTO, 0))", "vlDesconto");
    	sql.addProjection("sum(nvl(DEV.VL_DEVIDO, 0))", "vlTotal");
    	
    	sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DEV");
    	sql.addFrom("DESCONTO", "DES");
    	sql.addFrom("ITEM_FATURA", "IFAT");
    	
    	sql.addJoin("DEV.ID_DEVEDOR_DOC_SERV_FAT", "IFAT.ID_DEVEDOR_DOC_SERV_FAT");
    	sql.addJoin("DEV.ID_DEVEDOR_DOC_SERV_FAT", "DES.ID_DEVEDOR_DOC_SERV_FAT(+)");
    	
    	sql.addCriteria("DES.TP_SITUACAO_APROVACAO(+)", "!=", "R");
    	sql.addCriteria("IFAT.ID_FATURA", "=", idFatura);
    	
    	Object[] obj = (Object[])this.getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), csq);
    	
    	if (obj != null){
    		retorno.put("qtDocumento", obj[0]);
    		retorno.put("vlDesconto", obj[1]);
    		retorno.put("vlTotal", obj[2]);
    	}
    	
    	return retorno;
    }
    
    /**
     * Retorna a soma do valor dos descontos aprovados por fatura.
     * 
     * @author Mickaël Jalbert
     * @since 04/01/2006
     * 
     * @param Long idFatura
     * @return List
     * */
    public BigDecimal findSomaAprovadoByFatura(Long idFatura){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("SUM(des.vlDesconto)");
    	hql.addInnerJoin(Desconto.class.getName(), "des");
    	hql.addInnerJoin("des.devedorDocServFat", "dev");
    	hql.addInnerJoin("dev.itemFaturas", "ifat");
    	hql.addInnerJoin("ifat.fatura", "fat");
    	
    	hql.addCriteria("des.tpSituacaoAprovacao", "=", "A");
    	
    	hql.addCustomCriteria("fat.id = " + idFatura);
    	
    	List lstSoma = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (lstSoma.size() > 0){
    		if (lstSoma.get(0) == null){
    			return BigDecimal.ZERO;
    		} else {
    			return (BigDecimal)lstSoma.get(0);
    		}
    	} else {
    		return BigDecimal.ZERO;
    	}
    }    

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
	 * Busca o número de registros que será exibido na grid
	 * @param tfm Critérios de pesquisa : tpDesconto e idReciboDemonstrativo
	 * @return Inteiro representando o número de resultados da pesquisa para a grid
	 */
    public Integer getRowCountByReciboDemonstrativo(TypedFlatMap tfm) {
    	
    	Integer retorno  = null;
    	
    	SqlTemplate queryReciboDesconto = null;
		SqlTemplate queryDemonstrativoDesconto = null;
		
		DomainValue tipoDesconto = tfm.getDomainValue("tpDesconto");
    	
    	if( tipoDesconto != null && !tipoDesconto.getValue().equals("") ){
			
			if( tipoDesconto.getValue().equals("R") ){
				
				queryReciboDesconto = mountQuerySQLReciboDesconto(tfm);
				
				queryReciboDesconto.addProjection(" DISTINCT RD.ID_RECIBO_DESCONTO AS ID_TIPO_DESCONTO");
				
				retorno = getAdsmHibernateTemplate().getRowCountBySql("FROM ( " +
                        											  queryReciboDesconto.getSql() + " ) ",
											                          queryReciboDesconto.getCriteria());
			} else if( tipoDesconto.getValue().equals("D") ){
				
				queryDemonstrativoDesconto = mountQuerySQLDemonstrativoDesconto(tfm);
				
				queryDemonstrativoDesconto.addProjection(" DISTINCT DD.ID_DEMONSTRATIVO_DESCONTO AS ID_TIPO_DESCONTO");
				
				retorno = getAdsmHibernateTemplate().getRowCountBySql("FROM ( " +
																	  queryDemonstrativoDesconto.getSql() + " ) ",
																	  queryDemonstrativoDesconto.getCriteria());
			}
			
    	} else {
			
			queryReciboDesconto = mountQuerySQLReciboDesconto(tfm);
			queryDemonstrativoDesconto = mountQuerySQLDemonstrativoDesconto(tfm);
			
			queryReciboDesconto.addProjection("DISTINCT 1,RD.ID_RECIBO_DESCONTO AS ID_TIPO_DESCONTO");
			queryDemonstrativoDesconto.addProjection(" DISTINCT 2,DD.ID_DEMONSTRATIVO_DESCONTO AS ID_TIPO_DESCONTO");
			
			queryReciboDesconto.addCriteriaValue(queryDemonstrativoDesconto.getCriteria());    	
    	
			retorno = getAdsmHibernateTemplate().getRowCountBySql("FROM (\n "+
																  queryReciboDesconto.getSql() + 
    			                                                  "\n UNION \n " +
    			                                                  queryDemonstrativoDesconto.getSql() + " ) ",
    			                                                  queryReciboDesconto.getCriteria());
    	}
    	
    	return retorno;
    	
	}

    /**
	 * Faz a pesquisa que será exibida na grid
	 * @param tfm Critérios de pesquisa : tpDesconto e idReciboDemonstrativo
	 * @return ResultSetPage Dados da pesquisa e dados de paginação
	 */
	public ResultSetPage findPaginatedByReciboDemonstrativo(TypedFlatMap tfm) {
		
		ResultSetPage retorno  = null;
		FindDefinition findDef = FindDefinition.createFindDefinition(tfm);
		
		SqlTemplate queryReciboDesconto = null;
		SqlTemplate queryDemonstrativoDesconto = null;
		
		DomainValue tipoDesconto = tfm.getDomainValue("tpDesconto");
		
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("ID_TIPO_DESCONTO", Hibernate.LONG);
                sqlQuery.addScalar("DS_TIPO_DESCONTO", Hibernate.STRING);
                sqlQuery.addScalar("FILIAL_ORIGEM", Hibernate.STRING);
                sqlQuery.addScalar("NR_DESCONTO", Hibernate.LONG);
                sqlQuery.addScalar("SITUACAO", Hibernate.STRING);
                sqlQuery.addScalar("SITUACAO_APROVACAO", Hibernate.STRING);
                sqlQuery.addScalar("DT_EMISSAO", Hibernate.DATE);	                
                sqlQuery.addScalar("VL_DESCONTO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("SG_MOEDA", Hibernate.STRING);
                sqlQuery.addScalar("DS_SIMBOLO", Hibernate.STRING);
                sqlQuery.addScalar("TIPO_DESCONTO", Hibernate.STRING);
            }
        };
		
		if( tipoDesconto != null && !tipoDesconto.getValue().equals("") ){
			
			if( tipoDesconto.getValue().equals("R") ){
				
				queryReciboDesconto = mountQuerySQLReciboDesconto(tfm);
				
				queryReciboDesconto.addProjection(" DISTINCT RD.ID_RECIBO_DESCONTO AS ID_TIPO_DESCONTO, " +
												  PropertyVarcharI18nProjection.createProjection("VD1.DS_VALOR_DOMINIO_I","DS_TIPO_DESCONTO") + ", " +
										          "F.SG_FILIAL AS FILIAL_ORIGEM, " +
										          "RD.NR_RECIBO_DESCONTO AS NR_DESCONTO, " +
										          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
										          PropertyVarcharI18nProjection.createProjection("VD3.DS_VALOR_DOMINIO_I","SITUACAO_APROVACAO") + ", " +
										          "RD.DT_EMISSAO AS DT_EMISSAO, " +
										          "RD.VL_RECIBO_DESCONTO AS VL_DESCONTO, " +
										          "M.SG_MOEDA AS SG_MOEDA, " +
										          "M.DS_SIMBOLO AS DS_SIMBOLO, " +
										          "'R' AS TIPO_DESCONTO");
				
				retorno = getAdsmHibernateTemplate().findPaginatedBySql(queryReciboDesconto.getSql() + 
														                "ORDER BY DS_TIPO_DESCONTO, " +
														                "         FILIAL_ORIGEM, " +
														                "         NR_DESCONTO ",
															            findDef.getCurrentPage(),
															            findDef.getPageSize(),
															            queryReciboDesconto.getCriteria(),
															            configSql);
				
			} else if( tipoDesconto.getValue().equals("D") ){
				
				queryDemonstrativoDesconto = mountQuerySQLDemonstrativoDesconto(tfm);
				
				queryDemonstrativoDesconto.addProjection(" DISTINCT DD.ID_DEMONSTRATIVO_DESCONTO AS ID_TIPO_DESCONTO, " +
						PropertyVarcharI18nProjection.createProjection("VD1.DS_VALOR_DOMINIO_I","DS_TIPO_DESCONTO") + ", " +
														 "F.SG_FILIAL AS FILIAL_ORIGEM, " +
														 "DD.NR_DEMONSTRATIVO_DESCONTO AS NR_DESCONTO, " +
														 PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
														 "null SITUACAO_APROVACAO, " +
														 "DD.DT_EMISSAO AS DT_EMISSAO, " +
														 "DD.VL_DEMONSTRATIVO_DESCONTO AS VL_DESCONTO, " +
														 "M.SG_MOEDA AS SG_MOEDA, " +
														 "M.DS_SIMBOLO AS DS_SIMBOLO, " +
										          		 "'D' AS TIPO_DESCONTO");
				
				retorno = getAdsmHibernateTemplate().findPaginatedBySql(queryDemonstrativoDesconto.getSql() + 
															            "ORDER BY DS_TIPO_DESCONTO, " +
															            "         FILIAL_ORIGEM, " +
															            "         NR_DESCONTO ",
															            findDef.getCurrentPage(),
															            findDef.getPageSize(),
															            queryDemonstrativoDesconto.getCriteria(),
															            configSql);
			}
			
		} else {
			
			queryReciboDesconto = mountQuerySQLReciboDesconto(tfm);
			queryDemonstrativoDesconto = mountQuerySQLDemonstrativoDesconto(tfm);
			
			queryReciboDesconto.addCriteriaValue(queryDemonstrativoDesconto.getCriteria());
			
			queryReciboDesconto.addProjection(" DISTINCT RD.ID_RECIBO_DESCONTO AS ID_TIPO_DESCONTO, " +
					PropertyVarcharI18nProjection.createProjection("VD1.DS_VALOR_DOMINIO_I","DS_TIPO_DESCONTO") + ", " +
									          "F.SG_FILIAL AS FILIAL_ORIGEM, " +
									          "RD.NR_RECIBO_DESCONTO AS NR_DESCONTO, " +
									          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
									          PropertyVarcharI18nProjection.createProjection("VD3.DS_VALOR_DOMINIO_I","SITUACAO_APROVACAO") + ", " +
									          "RD.DT_EMISSAO AS DT_EMISSAO, " +
									          "RD.VL_RECIBO_DESCONTO AS VL_DESCONTO, " +
									          "M.SG_MOEDA AS SG_MOEDA, " +
											  "M.DS_SIMBOLO AS DS_SIMBOLO, " +
										      "'R' AS TIPO_DESCONTO");
			
			queryDemonstrativoDesconto.addProjection(" DISTINCT DD.ID_DEMONSTRATIVO_DESCONTO AS ID_TIPO_DESCONTO, " +
					PropertyVarcharI18nProjection.createProjection("VD1.DS_VALOR_DOMINIO_I","DS_TIPO_DESCONTO") + ", " +
											          "F.SG_FILIAL AS FILIAL_ORIGEM, " +
											          "DD.NR_DEMONSTRATIVO_DESCONTO AS NR_DESCONTO, " +
											          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
											          "null SITUACAO_APROVACAO, " +
											          "DD.DT_EMISSAO AS DT_EMISSAO, " +
											          "DD.VL_DEMONSTRATIVO_DESCONTO AS VL_DESCONTO, " +
											          "M.SG_MOEDA AS SG_MOEDA, " +
													  "M.DS_SIMBOLO AS DS_SIMBOLO, " +
										              "'D' AS TIPO_DESCONTO");
			
			retorno = getAdsmHibernateTemplate().findPaginatedBySql(queryReciboDesconto.getSql() + 
													           		"\nUNION\n " +
													           		queryDemonstrativoDesconto.getSql() + " \n" +
													           		"ORDER BY DS_TIPO_DESCONTO, " +
													           		"         FILIAL_ORIGEM, " +
													           		"         NR_DESCONTO ",
													           		findDef.getCurrentPage(),
													           		findDef.getPageSize(),
													           		queryReciboDesconto.getCriteria(),
													           		configSql);
		}
    	
		return retorno;
	}
	
	/**
	 * Monta a query do findPaginatedByReciboDemonstrativo sendo que esta é para o Recibo Desconto
	 * @param tfm Critérios de pesquisa
	 * @return SqlTemplate com a query e os dados setados
	 */
	private SqlTemplate mountQuerySQLReciboDesconto(TypedFlatMap tfm) {
		
		SqlTemplate sql = new SqlTemplate();
		

		sql.addFrom(
				" RECIBO_DESCONTO RD, DESCONTO D, FILIAL F, PESSOA P, DEVEDOR_DOC_SERV_FAT DDSF, DOCTO_SERVICO DS, " +
				" MOEDA M, VALOR_DOMINIO VD1, DOMINIO D1, VALOR_DOMINIO VD2, DOMINIO D2, VALOR_DOMINIO VD3, DOMINIO D3 ");

		sql.addJoin("D.ID_RECIBO_DESCONTO(+) ",			" RD.ID_RECIBO_DESCONTO");
		sql.addJoin("F.ID_FILIAL ",						" RD.ID_FILIAL");
		sql.addJoin("P.ID_PESSOA ",						" F.ID_FILIAL");
		sql.addJoin("DDSF.ID_DEVEDOR_DOC_SERV_FAT(+) ",	" D.ID_DEVEDOR_DOC_SERV_FAT");
		sql.addJoin(" DS.ID_DOCTO_SERVICO(+) ",			" DDSF.ID_DOCTO_SERVICO");
		sql.addJoin("M.ID_MOEDA(+) ",					" DS.ID_MOEDA");
		sql.addJoin("VD3.VL_VALOR_DOMINIO(+) ",			" RD.TP_SITUACAO_APROVACAO");
		sql.addJoin("D3.ID_DOMINIO(+) ",				" VD3.ID_DOMINIO ");
		sql.addJoin(" VD1.ID_DOMINIO",					" D1.ID_DOMINIO");
		sql.addJoin(" VD2.ID_DOMINIO ",					" D2.ID_DOMINIO");
		
		
		sql.addCriteria("D1.NM_DOMINIO","=", "DM_RECIBO_DEMONSTRATIVO");
		sql.addCriteria("D2.NM_DOMINIO","=", "DM_STATUS_RECIBO_DESCONTO");
	    sql.addCustomCriteria(" (D3.NM_DOMINIO = 'DM_STATUS_WORKFLOW' or D3.ID_DOMINIO IS NULL ) ");
	
		sql.addCriteria("VD1.VL_VALOR_DOMINIO","=", "R");
		sql.addCustomCriteria("VD2.VL_VALOR_DOMINIO = RD.TP_SITUACAO_RECIBO_DESCONTO");
		
		sql.addCriteria("RD.ID_RECIBO_DESCONTO","=",tfm.getLong("idReciboDemonstrativo"));
		
		sql.addCriteria("F.ID_FILIAL","=", tfm.getLong("filial.idFilial"));
		sql.addCriteria("RD.NR_RECIBO_DESCONTO","=", tfm.getLong("nrDesconto"));
		
		if( tfm.getDomainValue("tpSituacao") != null ){
			sql.addCriteria("RD.TP_SITUACAO_RECIBO_DESCONTO","=", tfm.getDomainValue("tpSituacao").getValue());
		}
		
		if( tfm.getYearMonthDay("dtEmissaoInicial") != null ){
			
			if( tfm.getYearMonthDay("dtEmissaoFinal") != null ){
				sql.addCustomCriteria("RD.DT_EMISSAO BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoFinal"));
			} else {
				sql.addCustomCriteria("RD.DT_EMISSAO >= ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoInicial"));
			}
			
		} else if( tfm.getYearMonthDay("dtEmissaoFinal") != null ){
			sql.addCustomCriteria("RD.DT_EMISSAO <= ?");
			sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoFinal"));
		}
		
		if( tfm.getBigDecimal("vlDescontoInicial") != null ){
			
			if( tfm.getBigDecimal("vlDescontoFinal") != null ){
				sql.addCustomCriteria("RD.VL_RECIBO_DESCONTO BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoInicial"));
				sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoFinal"));
			} else {
				sql.addCustomCriteria("RD.VL_RECIBO_DESCONTO >= ?");
				sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoInicial"));
			}
			
		} else if( tfm.getBigDecimal("vlDescontoFinal") != null ){
			sql.addCustomCriteria("RD.VL_RECIBO_DESCONTO <= ?");
			sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoFinal"));
		}
		
		if( tfm.getDomainValue("tpSituacaoAprovacao") != null ){
			sql.addCriteria("RD.TP_SITUACAO_APROVACAO","=", tfm.getDomainValue("tpSituacaoAprovacao").getValue());
		}		  
		
		return sql;
		
	}
	
	/**
	 * Busca o Recibo ou Demonstrativo Desconto
	 * @param idReciboDemonstrativo Identificador do ReciboDesconto ou do DemonstrativoDesconto
	 * @param tpDescontoSelecionado Tipo de desconto <code>R</code> Recibo, <code>D</code> Desconto
	 * @return Array de Objects
	 */
	public Object[] findByIdFromReciboDemonstrativo(Long idReciboDemonstrativo, String tpDescontoSelecionado) {
		
		SqlTemplate queryReciboDesconto = null;
		SqlTemplate queryDemonstrativoDesconto = null;
		
        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("ID_TIPO_DESCONTO", Hibernate.LONG);
                sqlQuery.addScalar("DS_TIPO_DESCONTO", Hibernate.STRING);
                sqlQuery.addScalar("FILIAL_ORIGEM", Hibernate.STRING);
                sqlQuery.addScalar("NR_DESCONTO", Hibernate.LONG);
                sqlQuery.addScalar("SITUACAO", Hibernate.STRING);
                sqlQuery.addScalar("SITUACAO_APROVACAO", Hibernate.STRING);
                sqlQuery.addScalar("DT_EMISSAO", Hibernate.DATE);
                sqlQuery.addScalar("VL_DESCONTO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("OBSERVACAO", Hibernate.STRING);
                sqlQuery.addScalar("ID_FILIAL", Hibernate.LONG);
                sqlQuery.addScalar("NM_FILIAL", Hibernate.STRING);
                sqlQuery.addScalar("TP_TIPO_DESCONTO", Hibernate.STRING);
            }
        };
		
        Object[] retorno = null;
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idReciboDemonstrativo",idReciboDemonstrativo);		
		
		if( tpDescontoSelecionado != null ){
			
			if( tpDescontoSelecionado.equals("R") ){
				
				queryReciboDesconto = mountQuerySQLReciboDesconto(tfm);
				
				queryReciboDesconto.addProjection("RD.ID_RECIBO_DESCONTO AS ID_TIPO_DESCONTO, " +
						PropertyVarcharI18nProjection.createProjection("VD1.DS_VALOR_DOMINIO_I","DS_TIPO_DESCONTO") + ", " +
										          "F.SG_FILIAL AS FILIAL_ORIGEM, " +
										          "RD.NR_RECIBO_DESCONTO AS NR_DESCONTO, " +
										          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
										          PropertyVarcharI18nProjection.createProjection("VD3.DS_VALOR_DOMINIO_I","SITUACAO_APROVACAO") + ", " +
										          "RD.DT_EMISSAO AS DT_EMISSAO, " +
										          "RD.VL_RECIBO_DESCONTO AS VL_DESCONTO, " +
										          "RD.OB_RECIBO_DESCONTO AS OBSERVACAO," +
										          "F.ID_FILIAL AS ID_FILIAL," +
										          "P.NM_FANTASIA AS NM_FILIAL, " +
										          "'R' AS TP_TIPO_DESCONTO ");
				
				retorno = (Object[]) getAdsmHibernateTemplate().findByIdBySql(queryReciboDesconto.getSql(), 
								                        		          queryReciboDesconto.getCriteria(),
								                        		          configSql);
				
			} else if( tpDescontoSelecionado.equals("D") ){
				
				queryDemonstrativoDesconto = mountQuerySQLDemonstrativoDesconto(tfm);
				
				queryDemonstrativoDesconto.addProjection("DD.ID_DEMONSTRATIVO_DESCONTO AS ID_TIPO_DESCONTO, " +
						PropertyVarcharI18nProjection.createProjection("VD1.DS_VALOR_DOMINIO_I","DS_TIPO_DESCONTO") + ", " +
												          "F.SG_FILIAL AS FILIAL_ORIGEM, " +
												          "DD.NR_DEMONSTRATIVO_DESCONTO AS NR_DESCONTO, " +
												          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","SITUACAO") + ", " +
												          "null SITUACAO_APROVACAO, " +
												          "DD.DT_EMISSAO AS DT_EMISSAO, " +
												          "DD.VL_DEMONSTRATIVO_DESCONTO AS VL_DESCONTO, " +
												          "DD.OB_DEMONSTRATIVO_DESCONTO AS OBSERVACAO, " +
												          "F.ID_FILIAL AS ID_FILIAL," +
												          "P.NM_FANTASIA AS NM_FILIAL, " +
										          		  "'D' AS TP_TIPO_DESCONTO ");
				
				retorno = (Object[]) getAdsmHibernateTemplate().findByIdBySql(queryDemonstrativoDesconto.getSql(),
																          queryDemonstrativoDesconto.getCriteria(),
																          configSql);
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Monta a query do findPaginatedByReciboDemonstrativo sendo que esta é para o Demonstrativo Desconto
	 * @param tfm Critérios de pesquisa
	 * @return SqlTemplate com a query e os dados setados
	 */
	private SqlTemplate mountQuerySQLDemonstrativoDesconto(TypedFlatMap tfm) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom("DEMONSTRATIVO_DESCONTO DD " +
				    "	LEFT OUTER JOIN DESCONTO D ON D.ID_DEMONSTRATIVO_DESCONTO = DD.ID_DEMONSTRATIVO_DESCONTO " +
				    "   INNER JOIN FILIAL F ON DD.ID_FILIAL = F.ID_FILIAL " +
				    "   INNER JOIN PESSOA P ON F.ID_FILIAL = P.ID_PESSOA " +
				    "	LEFT OUTER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON D.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
				    "	LEFT OUTER JOIN DOCTO_SERVICO DS ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
				    "	LEFT OUTER JOIN MOEDA M ON DS.ID_MOEDA = M.ID_MOEDA, " +
				    "VALOR_DOMINIO VD1 " +
				    "   INNER JOIN DOMINIO D1 ON VD1.ID_DOMINIO = D1.ID_DOMINIO," +
				    "VALOR_DOMINIO VD2 " +
				    "   INNER JOIN DOMINIO D2 ON VD2.ID_DOMINIO = D2.ID_DOMINIO");
		
		sql.addCriteria("D1.NM_DOMINIO","=", "DM_RECIBO_DEMONSTRATIVO");
		sql.addCriteria("D2.NM_DOMINIO","=", "DM_STATUS_RECIBO_DESCONTO");
		
		sql.addCriteria("VD1.VL_VALOR_DOMINIO","=", "D");
		sql.addCustomCriteria("VD2.VL_VALOR_DOMINIO = DD.TP_SITUACAO_DEMONSTRATIVO_DESC");
		
		sql.addCriteria("DD.ID_DEMONSTRATIVO_DESCONTO","=",tfm.getLong("idReciboDemonstrativo"));
		
		sql.addCriteria("F.ID_FILIAL","=", tfm.getLong("filial.idFilial"));
		sql.addCriteria("DD.NR_DEMONSTRATIVO_DESCONTO","=", tfm.getLong("nrDesconto"));
		
		if( tfm.getDomainValue("tpSituacao") != null ){
			sql.addCriteria("DD.TP_SITUACAO_DEMONSTRATIVO_DESC","=", tfm.getDomainValue("tpSituacao").getValue());
		}
		
		/** Caso o campo tpSituacaoAprovacao seja informado, não deve buscar demostrativoDesconto */
		DomainValue dv = tfm.getDomainValue("tpSituacaoAprovacao");
		if(dv != null && StringUtils.isNotBlank(dv.getValue())){
			sql.addCriteria("1", "=", "2");
		}
		
		if( tfm.getYearMonthDay("dtEmissaoInicial") != null ){
			
			if( tfm.getYearMonthDay("dtEmissaoFinal") != null ){
				sql.addCustomCriteria("TRUNC(DD.DT_EMISSAO) BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoFinal"));
			} else {
				sql.addCustomCriteria("TRUNC(DD.DT_EMISSAO) >= ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoInicial"));
			}
			
		} else if( tfm.getYearMonthDay("dtEmissaoFinal") != null ){
			sql.addCustomCriteria("TRUNC(DD.DT_EMISSAO) <= ?");
			sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoFinal"));
		}
		
		if( tfm.getBigDecimal("vlDescontoInicial") != null ){
			
			if( tfm.getBigDecimal("vlDescontoFinal") != null ){
				sql.addCustomCriteria("DD.VL_DEMONSTRATIVO_DESCONTO BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoInicial"));
				sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoFinal"));
			} else {
				sql.addCustomCriteria("DD.VL_DEMONSTRATIVO_DESCONTO >= ?");
				sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoInicial"));
			}
			
		} else if( tfm.getBigDecimal("vlDescontoFinal") != null ){
			sql.addCustomCriteria("DD.VL_DEMONSTRATIVO_DESCONTO <= ?");
			sql.addCriteriaValue(tfm.getBigDecimal("vlDescontoFinal"));
		}
		
		return sql;
		
	}

	/**
	 * Busca os tipos de Desconto (Recibo e Demonstrativo)
	 * @param idReciboDemonstrativo Identificador do Recibo ou Demonstrativo Desconto
	 * @param tpDesconto String do tipo de desconto <code>R</code> para Recibo Desconto <code>D</code> para Demonstrativo Desconto
	 * @return Lista de tipos de desconto
	 */
	public List findDescontosByReciboDesconto(Long idReciboDemonstrativo, String tpDesconto) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("d");
		sql.addLeftOuterJoin(Desconto.class.getName(),"d");
		
		if( tpDesconto.equals("R") ){		
			sql.addLeftOuterJoin("d.reciboDesconto","tpDesconto");
		} else if( tpDesconto.equals("D") ){
			sql.addLeftOuterJoin("d.demonstrativoDesconto","tpDesconto");
		}
		
		sql.addCriteria("tpDesconto.id","=",idReciboDemonstrativo);
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
	}
	
	/**
	 * Busca quantos dados serão exibidos na listagem da aba Documento de Serviço
	 * @param tfm Critérios de pesquisa
	 * @return Inteiro representando a quantidade de dados resultante da pesquisa
	 */
	public Integer getRowCountDadosListagemAbaDocumentosServico(TypedFlatMap tfm) {
		
		SqlTemplate sql = montaQueryHqlDoctoServicoFromReciboDemonstrativo(tfm.getLong("idReciboDemonstrativo"), tfm.getString("tpTipoDesconto"));
		
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
		
	}	

	/**
	 * Busca dados para a listagem da aba Documento de Serviço
	 * @param idReciboDemonstrativo Identificador do tipo de desconto (Recibo ou Demonstrativo)
	 * @param tpDesconto Tipo de desconto (Recibo ou Demonstrativo)
	 * @return Lista de dados
	 */
	public ResultSetPage findDadosListagemAbaDocumentosServico(TypedFlatMap tfm) {
		
		SqlTemplate sql = montaQueryHqlDoctoServicoFromReciboDemonstrativo(tfm.getLong("idReciboDemonstrativo"), tfm.getString("tpTipoDesconto"));
		
		sql.addProjection("new Map( td.id as idReciboDemonstrativo, " +
				          "			ds.tpDocumentoServico as tpDocumentoServico," +
				          "         fds.sgFilial as sgFilialOrigem, " +
				          "         fds.idFilial as idFilialOrigem, " +
				          "         ds.nrDoctoServico as nrDocumento, " +
				          "         p.tpIdentificacao as tpIdentificacao, " +
				          "         p.nrIdentificacao as nrIdentificacao, " +
				          "         p.nmPessoa as nmCliente, " +
				          "         '' as tpSituacaoAprovacao, " +
				          "         d.vlDesconto as vlDesconto, " +
				          "         d.id as idDesconto, " +
				          "         ddsf.vlDevido as vlDevido, " +
				          "         md.dsMotivoDesconto as motivoDesconto, " +
				          "         d.obDesconto as observacao," +
				          "         '" + tfm.getString("tpTipoDesconto") + "' as tpTipoDesconto  )");
		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description",LocaleContextHolder.getLocale()));
		sql.addOrderBy("fds.sgFilial");
		sql.addOrderBy("ds.nrDoctoServico");
		sql.addOrderBy("p.nrIdentificacao");
		sql.addOrderBy("p.nmPessoa");
		
		FindDefinition findDef = FindDefinition.createFindDefinition(tfm);
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(), sql.getCriteria());
		
	}
	
    /**
     * @author edenilsonf
     * @param idDoctoServico
     * @return true se existe descontos para esse documento de serviço
     */
    public boolean validatePossuiDesconto(Long idDoctoServico){
		
    	SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("d");
		sql.addFrom(Desconto.class.getName(),"d");
		
		//Não se pode transferir débitos que já possuam descontos: Aprovado ou Em Aprovação
		sql.addCustomCriteria("( (d.tpSituacaoAprovacao = 'A') or ( d.tpSituacaoAprovacao = 'E' ) ) ");
		
		sql.addCriteria("d.vlDesconto",">",new BigDecimal(0));
		sql.addCriteria("d.devedorDocServFat.doctoServico.id","=",idDoctoServico);
		
		List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		return ( retorno != null && !retorno.isEmpty() );

    }


	private SqlTemplate montaQueryHqlDoctoServicoFromReciboDemonstrativo(Long idReciboDemonstrativo, String tpDesconto) {
			
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer query = new StringBuffer();
		
		query.append(Desconto.class.getName() + " as d ");
		
		if( tpDesconto.equals("R") ){		
			query.append("	left outer join d.reciboDesconto as td");
		} else if( tpDesconto.equals("D") ){
			query.append("	left outer join d.demonstrativoDesconto as td");
		}
		
		query.append("	inner join td.filial as ftd ");
		query.append("	inner join d.motivoDesconto as md");
		query.append("	inner join d.devedorDocServFat as ddsf");
		query.append("	inner join ddsf.cliente as c");
		query.append("	inner join c.pessoa as p");
		query.append("	inner join ddsf.doctoServico as ds");
		query.append("	inner join ds.filialByIdFilialOrigem as fds");
		
		sql.addFrom(query.toString());
		
		sql.addFrom(DomainValue.class.getName(),"dv " +
				    "	inner join dv.domain as domain ");
		
		sql.addJoin("dv.value","ds.tpDocumentoServico");
		
		sql.addCriteria("domain.name","=","DM_TIPO_DOCUMENTO_SERVICO");
		sql.addCriteria("td.id","=",idReciboDemonstrativo);
		
		return sql;
	}
	
	/**
	 * Atualiza os descontos da fatura informada com a situação informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 22/11/2006
	 * 
	 * @param Long idFatura
	 * @param String tpSituacaoAprovacao
	 */
	public void updateDescontoByIdFatura(Long idFatura, String tpSituacaoAprovacao){
    	StringBuilder sb = new StringBuilder()

    	.append(" UPDATE 	" + Desconto.class.getName() + " AS d \n")
		.append(" SET 		d.tpSituacaoAprovacao = ? \n") 
		.append(" WHERE		d.idPendencia IS NULL")
		.append(" AND 	d.id IN ( \n") 
		.append("		SELECT 		des.id \n")
		.append("		FROM 		" + ItemFatura.class.getName() + " AS ifat, \n")
		.append("					" + Fatura.class.getName() + " AS fat, \n")
		.append("		 			" + DevedorDocServFat.class.getName() + " AS dev, \n")
		.append("		 			" + Desconto.class.getName() + " AS des \n")
		.append("		WHERE      	fat.tpSituacaoFatura != ? \n")
		.append("		AND 	   	fat.id = ? \n")
		.append("		AND 	   	ifat.fatura.id = fat.id \n")
		.append("		AND 	   	ifat.devedorDocServFat.id = dev.id \n")
		.append("		AND 	   	des.devedorDocServFat.id = dev.id \n")
		.append(" ) \n");
    	
    	Object[] values = new Object[]{tpSituacaoAprovacao, "CA", idFatura};
    	Type[] types = new Type[]{Hibernate.STRING, Hibernate.STRING, Hibernate.LONG};

    	executeUpdate(sb.toString(), values, types);
	}
	
	/**
	 * Remover os descontos pendente da fatura informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 22/11/2006
	 * 
	 * @param Long idFatura
	 */
	public void removeDescontoByIdFatura(Long idFatura){
		StringBuilder sb = new StringBuilder()

    	.append(" DELETE FROM " + Desconto.class.getName() + " AS d \n")
		.append(" WHERE		d.id IN ( \n") 
		.append("		SELECT 		des.id \n")
		.append("		FROM 		" + ItemFatura.class.getName() + " AS ifat, \n")
		.append("					" + Fatura.class.getName() + " AS fat, \n")
		.append("		 			" + DevedorDocServFat.class.getName() + " AS dev, \n")
		.append("		 			" + Desconto.class.getName() + " AS des \n")
		.append("		WHERE      	fat.id = ? \n")
		.append("		AND 	   	ifat.fatura.id = fat.id \n")
		.append("		AND 	   	ifat.devedorDocServFat.id = dev.id \n")
		.append("		AND 	   	des.devedorDocServFat.id = dev.id \n")
		.append("		AND 	   	des.idPendencia is null \n")
		.append(" ) \n");
    	
    	Object[] values = new Object[]{idFatura};
    	Type[] types = new Type[]{Hibernate.LONG};
    	
    	executeUpdate(sb.toString(), values, types);
	}
	
    /**
     * Retorna o desconto do devedor informado
     * 
     * @author Mickaël Jalbert
     * @since 22/12/2006
     * 
     * @param Long idDevedorDocServFat
     * @return Desconto
     */
    public Desconto findByIdDevedorDocServFatDisconnected(Long idDevedorDocServFat){
    	DetachedCriteria dc = DetachedCriteria.forClass(Desconto.class, "des");
    	dc.createAlias("des.devedorDocServFat", "dev");
    	dc.createAlias("des.motivoDesconto", "mot");
    	
    	dc.add(Restrictions.eq("dev.id", idDevedorDocServFat));
    	
    	Desconto desconto = (Desconto) getAdsmHibernateTemplate().findUniqueResult(dc);
    	
    	if(desconto != null){
    		this.getSession().evict(desconto);
    	}
    	
    		return desconto;
    	}
	
	public void executeUpdate(final String hql, final Object[] values, final Type[] types){
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameters(values, types);
				if (StringUtils.isNotBlank(hql)){
					query.executeUpdate();
				}
				
				return null;
			}
		});
	}	
	
	/**
	 * Retorna o desconto através do id devedor docto fatura
	 * 
	 * @param idDevedor
	 * @return Desconto
	 */
	public Desconto findDescontoByIdDevedor(Long idDevedor){
		
		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("devedorDocServFat", "devedor")
		.add(Restrictions.eq("devedor.id", idDevedor));
		
		List<Desconto> list = findByDetachedCriteria(dc);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}	
	
	/**
	 * Salva o desconto.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 27/07/2007
	 *
	 * @param desconto
	 * @return
	 *
	 */
    public Desconto storeBasic(Desconto desconto){
		getAdsmHibernateTemplate().saveOrUpdate(desconto);
		
		return desconto;
    }

	public Long findbyIdPendencia(Long idPendencia) {
		
		SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("d.idDesconto");
    	hql.addInnerJoin(Desconto.class.getName(), "d");
    	
    	hql.addCustomCriteria("d.idPendencia = " + idPendencia);
    	
    	List lId = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lId.isEmpty() ){
    		return (Long)lId.get(0);
    	}
    	
		return null;
	}

	public BigDecimal findValorDesconto(Long idDesconto) {
		SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("des.vlDesconto");
    	hql.addInnerJoin(Desconto.class.getName(), "des");
    	
    	hql.addCriteria("des.id", "=", idDesconto);
    	
    	return (BigDecimal)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    	
	}

	public Desconto findByIntegracao(Long idDevedorDocServFat) {
		StringBuffer sb = new StringBuffer()
		.append(" from Desconto as d ")
		.append("where d.devedorDocServFat.id = ? ");

		List<Desconto> result = getAdsmHibernateTemplate().find(sb.toString(), new Object[]{idDevedorDocServFat});
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	
    public List findByPendentesDevedorDocServFatSituacao(Long idDevedorDocServFat){ 
    	SqlTemplate sql = new SqlTemplate(); 
    	sql.addProjection("d"); 
     
    	sql.addInnerJoin(Desconto.class.getName(),"d "); 
    	sql.addLeftOuterJoin("fetch d.reciboDesconto", "recdes"); 
    	sql.addLeftOuterJoin("fetch recdes.filial", "filrec"); 
    	sql.addLeftOuterJoin("fetch d.demonstrativoDesconto", "demdes"); 
    	sql.addLeftOuterJoin("fetch demdes.filial", "fildem"); 
    	sql.addCriteria("d.devedorDocServFat.id","=",idDevedorDocServFat);
    	sql.addCustomCriteria("(d.tpSituacaoAprovacao = 'E' or d.tpSituacaoAprovacao = 'R')"); 
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()); 
    } 

    /**
     * Busca desconto do documento de serviço
     */
	public List<Desconto> findDescontoByDoctoServico(Long idDoctoServico){
		StringBuffer hql = new StringBuffer("");
		String tpSituacaoAprovacao = "A";
		
		hql.append("SELECT d");
		hql.append("  FROM " + Desconto.class.getName() + " d");
		hql.append(" INNER JOIN d.devedorDocServFat dev");
		hql.append(" INNER JOIN dev.doctoServico doc");
		hql.append(" WHERE d.tpSituacaoAprovacao = ? "); 
		hql.append("   AND doc.idDoctoServico = ? "); 
		
		List<Desconto> descontoList = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{tpSituacaoAprovacao, idDoctoServico});
				
		return descontoList;
	}
	
}