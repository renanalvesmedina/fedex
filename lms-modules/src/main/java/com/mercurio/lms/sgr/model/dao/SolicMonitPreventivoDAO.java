
package com.mercurio.lms.sgr.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.sgr.model.ExigenciaSmp;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SolicMonitPreventivoDAO extends BaseCrudDao<SolicMonitPreventivo, Long> {

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("gerenciadoraRisco", FetchMode.JOIN);
		map.put("gerenciadoraRisco.pessoa", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map map) {
		map.put("filial", FetchMode.JOIN);
		map.put("gerenciadoraRisco", FetchMode.JOIN);
		map.put("gerenciadoraRisco.pessoa", FetchMode.JOIN);
		map.put("moedaPais", FetchMode.JOIN);
		map.put("moedaPais.moeda", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map map) {
		map.put("moedaPais", FetchMode.JOIN);
		map.put("moedaPais.moeda", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SolicMonitPreventivo.class;
    }
    
    /**
     * row count para Manter SMP 
     * @param tfm
     * @return
     * @author Rodrigo Antunes
     */
    public Integer getRowCountSMP(TypedFlatMap tfm) {
    	StringBuffer hqlRowCount = new StringBuffer("");
    	List criterios = new ArrayList();
    	hqlRowCount.append( this.addCriteriaToSMP(tfm, criterios) );
    	
    	Integer result = getAdsmHibernateTemplate().getRowCountForQuery(hqlRowCount.toString(), criterios.toArray());
        return result;
    }

    /**
     * Método de pesquisa para Manter SMP 
     * @param tfm
     * @param fd
     * @return
     * @author Rodrigo Antunes
     */
    public ResultSetPage findPaginatedSMP(TypedFlatMap tfm, FindDefinition fd) {
    	StringBuffer hql = new StringBuffer("");
    	hql.append( addProjectionsToSMP(false) );
    	
    	List criterios = new ArrayList();
    	hql.append( addCriteriaToSMP(tfm, criterios) );
    	hql.append(" order by f.sgFilial, smp.nrSmp");
    	
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.toString(), fd.getCurrentPage(), fd.getPageSize(), criterios.toArray() );
    	List retorno = new AliasToNestedBeanResultTransformer(SolicMonitPreventivo.class).transformListResult(rsp.getList());
    	
    	rsp.setList( retorno );
    	return rsp;
    }

	/**
	 * Método que seta as projeções para os métodos de pesquisa do SMP 
	 * (findPaginatedSMP e findSMPById).
	 * @return
	 * @author Rodrigo Antunes
	 */
	private String addProjectionsToSMP(boolean isFindById) {
		StringBuffer hql = new StringBuffer ("");

		hql.append(" select new map(");
		hql.append(" smp.idSolicMonitPreventivo as idSolicMonitPreventivo, ");
		hql.append(" smp.dhGeracao as dhGeracao, ");
		hql.append(" smp.nrSmp as nrSmp, ");
		hql.append(" smp.vlSmp as vlSmp, ");
		hql.append(" smp.nrSmpAnoGR as nrSmpAnoGR, ");
		hql.append(" smp.nrSmpGR as nrSmpGR, ");
		hql.append(" smp.tpStatusSmpGR as tpStatusSmpGR, ");
		hql.append(" smp.dsRetornoGR as dsRetornoGR, ");
		hql.append(" smp.tpStatusSmp as tpStatusSmp, ");
		hql.append(" cc as controleCarga, ");
		hql.append(" f.sgFilial as filial_sgFilial, ");
		hql.append(" focc.sgFilial as controleTrecho_controleCarga_filialByIdFilialOrigem_sgFilial, ");
		hql.append(" focc.idFilial as controleTrecho_controleCarga_filialByIdFilialOrigem_idFilial, ");// necessário para a tela de consultar veiculos manifestos
		hql.append(" cc.idControleCarga as controleTrecho_controleCarga_idControleCarga, ");
		hql.append(" cc.nrControleCarga as controleTrecho_controleCarga_nrControleCarga, ");
		hql.append(" transportado.nrFrota as meioTransporteByIdMeioTransporte_nrFrota, ");
		hql.append(" transportado.nrIdentificador as meioTransporteByIdMeioTransporte_nrIdentificador, ");
		hql.append(" foct.sgFilial as controleTrecho_filialByIdFilialOrigem_sgFilial, ");
		hql.append(" fdct.sgFilial as controleTrecho_filialByIdFilialDestino_sgFilial, ");
		// usado em consultar / visualizar SMP: tpControleCarga, dsRota
		hql.append(" cc.tpControleCarga as controleTrecho_controleCarga_tpControleCarga, ");
		hql.append(" rotacc.dsRota as controleTrecho_controleCarga_rota_dsRota, ");
		hql.append(" r.dsRota as controleTrecho_controleCarga_rotaIdaVolta_rota_dsRota, ");
		hql.append(" moeda.sgMoeda as moedaPais_moeda_sgMoeda, ");
		hql.append(" moeda.dsSimbolo as moedaPais_moeda_dsSimbolo ");
		
		// caso o método chamador seja o findById, deve adicionar esses campos
		if (isFindById) {
			hql.append(", mp.nmPessoa as motorista_pessoa_nmPessoa, ");
			hql.append(" riv.nrRota as controleTrecho_controleCarga_rotaIdaVolta_nrRota, ");
			hql.append(" rotacc.dsRota as controleCarga_rota_dsRota, ");
			hql.append(" foctp.nmFantasia as controleTrecho_filialByIdFilialOrigem_pessoa_nmFantasia, ");
			hql.append(" fdctp.nmFantasia as controleTrecho_filialByIdFilialDestino_pessoa_nmFantasia, ");
			hql.append(" reboque.nrFrota as meioTransporteByIdMeioSemiReboque_nrFrota, ");
			hql.append(" reboque.nrIdentificador as meioTransporteByIdMeioSemiReboque_nrIdentificador ");
		}
		
		hql.append(" )");
		return hql.toString();
	}	
	

	/**
	 * Cria os Alias para as pesquisas de SMP.
	 * @author Rodrigo Antunes
	 */
	private String createAliasToSMP(boolean isFindById) {
		StringBuffer hqlFrom = new StringBuffer("");
		hqlFrom.append(" from ").append(SolicMonitPreventivo.class.getName()).append(" smp ");
		hqlFrom.append(" join smp.filial f ");
    	hqlFrom.append(" left join smp.controleTrecho ct ");
    	hqlFrom.append(" join smp.controleCarga cc ");
    	hqlFrom.append(" left join ct.filialByIdFilialOrigem foct ");
    	hqlFrom.append(" left join ct.filialByIdFilialDestino fdct ");
    	hqlFrom.append(" join cc.filialByIdFilialOrigem focc ");
    	hqlFrom.append(" left join cc.filialByIdFilialDestino fdcc ");
    	hqlFrom.append(" left join cc.rota rotacc ");
    	hqlFrom.append(" join smp.meioTransporteByIdMeioTransporte transportado ");
    	hqlFrom.append(" left join smp.meioTransporteByIdMeioSemiReboque reboque ");
    	hqlFrom.append(" left join cc.rotaIdaVolta riv ");
		hqlFrom.append(" left join riv.rota r ");
		hqlFrom.append(" left join smp.moedaPais moedaPais ");
		hqlFrom.append(" left join moedaPais.moeda moeda ");

    	if (isFindById) {
    		hqlFrom.append(" join smp.motorista m ");
    		hqlFrom.append(" join m.pessoa mp ");
    		hqlFrom.append(" left join foct.pessoa foctp ");
    		hqlFrom.append(" left join fdct.pessoa fdctp ");
    		hqlFrom.append(" left join cc.rota rotacc ");
    	}
    	return hqlFrom.toString();
	}	
	
	
	/**
	 * findById customizado para SMP 
	 * @param idSMP
	 * @return
	 * @author Rodrigo Antunes
	 */
	public SolicMonitPreventivo findSMPById(Long idSMP) {
    	StringBuffer hql = new StringBuffer("");
    	hql.append( this.addProjectionsToSMP(true) );
    	hql.append( this.createAliasToSMP(true) );
    	
    	hql.append( "where" );
    	hql.append( " smp.idSolicMonitPreventivo = ?" );
    	
    	List param = new ArrayList();
    	param.add( idSMP );
    	
        List l  = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
    	List retorno = new AliasToNestedBeanResultTransformer(SolicMonitPreventivo.class).transformListResult(l);
    		
    	return (SolicMonitPreventivo)retorno.get(0);
	}


	/**
	 * Método que adiciona os criterios de pesquisa para consulta Manter SMP.
	 * Utilizado no find e no getRowCount
	 * @param tfm
	 * @param dc
	 * @author Rodrigo Antunes
	 */
	private String addCriteriaToSMP(TypedFlatMap tfm, List criterios) {
		DateTime dhGeracaoInicial = tfm.getDateTime("dataInicial");
    	DateTime dhGeracaoFinal = tfm.getDateTime("dataFinal");
    	BigDecimal vlFrotaMinimo = tfm.getBigDecimal("vlFrotaMinimo");
    	BigDecimal vlFrotaMaximo = tfm.getBigDecimal("vlFrotaMaximo");
    	
    	Long idFilialSMP = tfm.getLong("solicMonitPreventivo.filial.idFilial");
    	Integer nrSmp = tfm.getInteger("solicMonitPreventivo.nrSmp");
    	Integer idSmp = tfm.getInteger("solicMonitPreventivo.idSolicMonitPreventivo");
    	Integer nrSmpAnoGR = tfm.getInteger("solicMonitPreventivo.nrSmpAnoGR");
    	Integer nrSmpGR = tfm.getInteger("solicMonitPreventivo.nrSmpGR");
    	DomainValue tpStatusSmpGR = tfm.getDomainValue("solicMonitPreventivo.tpStatusSmpGR");
    	DomainValue tpStatusSmp = tfm.getDomainValue("solicMonitPreventivo.tpStatusSmp");
    	
    	Long idFilialOrigemControleTrecho = tfm.getLong("controleTrecho.filialByIdFilialOrigem.idFilial");
    	Long idFilialDestinoControleTrecho = tfm.getLong("controleTrecho.filialByIdFilialDestino.idFilial");
    	
    	Long idTransportado = tfm.getLong("meioTransporteByIdTransportado.idMeioTransporte");
    	Long idRebocado = tfm.getLong(".meioTransporteByIdSemiRebocado.idMeioTransporte");
    	Long idExigenciaGerRisco = tfm.getLong("exigenciaGerRisco.idExigenciaGerRisco");
    	
    	Long idTipoExigenciaGerRisco = tfm.getLong("tipoExigenciaGerRisco.idTipoExigenciaGerRisco");
    	Long idRotaIdaVolta = tfm.getLong("rotaIdaVolta.idRotaIdaVolta");
    	Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");
    	String tpControleCarga = tfm.getString("controleCarga.tpControleCarga");
    	Long idMoeda = tfm.getLong("moeda.idMoeda");
    	
    	StringBuffer hqlCriteria = new StringBuffer("");
    	
    	hqlCriteria.append(this.createAliasToSMP(false));
    	
    	SqlTemplate criteria = new SqlTemplate();
    	
    	if (idFilialSMP!=null) {
    		criteria.addCriteria("f.idFilial", "=", idFilialSMP);
    		criterios.add(idFilialSMP);
    	}

    	if (nrSmp!=null) {
    		criteria.addCriteria("smp.nrSmp", "=", nrSmp);
    		criterios.add(nrSmp);
    	}
    	
    	if (idSmp!=null) {
    		criteria.addCriteria("smp.idSolicMonitPreventivo", "=", idSmp);
    		criterios.add(idSmp);
    	}
    	
    	if (nrSmpAnoGR!=null) {
    		criteria.addCriteria("smp.nrSmpAnoGR", "=", nrSmpAnoGR);
    		criterios.add(nrSmpAnoGR);
    	}
    	
    	if (nrSmpGR!=null) {
    		criteria.addCriteria("smp.nrSmpGR", "=", nrSmpGR);
    		criterios.add(nrSmpGR);
    	}
    	
    	if(tpStatusSmpGR!=null && !tpStatusSmpGR.getValue().isEmpty()){
    		criteria.addCriteria("smp.tpStatusSmpGR", "=", tpStatusSmpGR);
    		criterios.add(tpStatusSmpGR);
    	}
    	
    	if(tpStatusSmp!=null && !tpStatusSmp.getValue().isEmpty()){
    		criteria.addCriteria("smp.tpStatusSmp", "=", tpStatusSmp);
    		criterios.add(tpStatusSmp);
    	}
    	
    	if (idTransportado!=null) {
    		criteria.addCriteria("transportado.id", "=", idTransportado);
    		criterios.add(idTransportado);
    	}

    	if (idRebocado!=null) {
    		criteria.addCriteria("reboque.id", "=", idRebocado);
    		criterios.add(idRebocado);
    	}
    	
    	if (idTipoExigenciaGerRisco!=null || idExigenciaGerRisco!=null) {
    		ArrayList criteriosSubQuery = new ArrayList();
    		
    		StringBuffer hqlSubQuery = new StringBuffer("");
    		hqlSubQuery.append(" select distinct (esmp.solicMonitPreventivo.id) ");
    		hqlSubQuery.append(" from "+ExigenciaSmp.class.getName() + " esmp");
    		hqlSubQuery.append(" join esmp.exigenciaGerRisco egr ");
    		hqlSubQuery.append(" join egr.tipoExigenciaGerRisco tegr ");
    		
    		hqlSubQuery.append(" where ");
    		boolean addAnd = false;
    		
        	if (idExigenciaGerRisco!=null) {
        		hqlSubQuery.append(" egr.idExigenciaGerRisco = ? ");
        		criteriosSubQuery.add( idExigenciaGerRisco );
        		addAnd = true;
        	}

        	if (idTipoExigenciaGerRisco!=null) {
        		if (addAnd)
        			hqlSubQuery.append(" and ");
        		
        		hqlSubQuery.append(" tegr.idTipoExigenciaGerRisco = ? ");
        		criteriosSubQuery.add( idTipoExigenciaGerRisco );
        	}

        	List l = getAdsmHibernateTemplate().find(hqlSubQuery.toString(), criteriosSubQuery.toArray());
        	StringBuffer resultIds = new StringBuffer();
        	
        	if (l!=null && !l.isEmpty()) {
        		for (Iterator iter = l.iterator(); iter.hasNext();) {
    				Long id = (Long) iter.next();
    				resultIds.append(id.toString());
    				if (iter.hasNext()) resultIds.append(", ");
    			}    		
        		
        	} else {
        		resultIds.append("-1");
        	}
    		criteria.addCustomCriteria("smp.idSolicMonitPreventivo in (" + resultIds.toString()+ ")");
    	}
    	
    	if (idFilialOrigemControleTrecho!=null) {
    		criteria.addCriteria("foct.idFilial", "=", idFilialOrigemControleTrecho);
    		criterios.add(idFilialOrigemControleTrecho);
    	}
    	
    	if (idFilialDestinoControleTrecho!=null) {
    		criteria.addCriteria("fdct.idFilial", "=", idFilialDestinoControleTrecho);
    		criterios.add(idFilialDestinoControleTrecho);
    	}
    	
    	if (idControleCarga!=null) {
    		criteria.addCriteria("cc.idControleCarga", "=", idControleCarga);
    		criterios.add(idControleCarga);
    	}

    	if (StringUtils.isNotBlank(tpControleCarga)) {
    		criteria.addCriteria("cc.tpControleCarga", "=", tpControleCarga);
    		criterios.add(tpControleCarga);
    		criteria.add(tpControleCarga);
    	}
    	
    	if (idRotaIdaVolta!=null) {
    		criteria.addCriteria("riv.idRotaIdaVolta", "=", idRotaIdaVolta);
    		criterios.add(idRotaIdaVolta);
    	}
    	
    	if (dhGeracaoInicial!=null) {
    		criteria.addCriteria("smp.dhGeracao.value", ">=", dhGeracaoInicial);
    		criterios.add(dhGeracaoInicial);
    	}
    	
    	if (dhGeracaoFinal!=null) {
    		criteria.addCriteria("smp.dhGeracao.value", "<=", dhGeracaoFinal);
    		criterios.add(dhGeracaoFinal);
    	}
    	
    	if (vlFrotaMinimo!=null) {
    		criteria.addCriteria("smp.vlSmp", ">=", vlFrotaMinimo);
    		criterios.add(vlFrotaMinimo);
    	}
    	
    	if (vlFrotaMaximo!=null) {
    		criteria.addCriteria("smp.vlSmp", "<=", vlFrotaMaximo);
    		criterios.add(vlFrotaMaximo);
    	}
    	
    	if (idMoeda!=null) {
    		criteria.addCriteria("moeda.idMoeda", "=", idMoeda);
    		criterios.add(idMoeda);
    	}
    	
    	hqlCriteria.append( criteria.getSql() );
    	return hqlCriteria.toString();
	}


    /*****************************************************************************************************************************
     * Métodos referentes à consulta de conteúdo de veículos (SMP) 
     *****************************************************************************************************************************/

	/**
	 * FindPaginated da Consulta de Conteúdo de Veiculos da SMP 
	 * @param tfm
	 * @param fd
	 * @return
	 */
    public ResultSetPage findConsultaConteudoVeiculos(TypedFlatMap tfm, FindDefinition fd) {
    	Long idSolicMonitPreventivo = tfm.getLong("smp.idSolicMonitPreventivo");
    	Long idMeioTransporte = tfm.getLong("meioTransporteRodoviario.idMeioTransporte");
    	Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");
    	
    	SqlTemplate sqlColeta = getSQLTemplateConsultaConteudoVeiculosColeta(idSolicMonitPreventivo, idMeioTransporte, idControleCarga);
    	SqlTemplate sqlViagem = getSQLTemplateConsultaConteudoVeiculosViagemEntrega(idSolicMonitPreventivo, idMeioTransporte, idControleCarga);
    	Object[] coletaCriteria = sqlColeta.getCriteria();
    	Object[] viagemCriteria = sqlViagem.getCriteria();
    	Object[] criteria = com.mercurio.lms.util.ArrayUtils.joinArrays(coletaCriteria, coletaCriteria.length, viagemCriteria, viagemCriteria.length);
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {              // NR.COLUNA
    			sqlQuery.addScalar("NR_MANIFESTO",Hibernate.LONG);                  // 0
    			sqlQuery.addScalar("ID_MANIFESTO",Hibernate.LONG);                  // 1
    			sqlQuery.addScalar("SG_FILIAL_MANIFESTO",Hibernate.STRING);         // 2
    			sqlQuery.addScalar("TP_COLETA_VIAGEM",Hibernate.STRING);            // 3
    			sqlQuery.addScalar("ID_CONTROLE_CARGA",Hibernate.LONG);             // 4
    			sqlQuery.addScalar("TP_STATUS",Hibernate.STRING);                   // 5
    			sqlQuery.addScalar("TP_MANIFESTO",Hibernate.STRING);                // 6
    			sqlQuery.addScalar("TP_ABRANGENCIA",Hibernate.STRING);              // 7
    			sqlQuery.addScalar("TP_MANIFESTO_VIAGEM",Hibernate.STRING);         // 8
    			sqlQuery.addScalar("TP_MANIFESTO_ENTREGA",Hibernate.STRING);        // 9
    			sqlQuery.addScalar("SG_MOEDA",Hibernate.STRING);                    //10
    			sqlQuery.addScalar("DS_SIMBOLO",Hibernate.STRING);                  //11
    			sqlQuery.addScalar("VL_TOTAL_NOTA_MANIFESTO",Hibernate.BIG_DECIMAL);//12
    			sqlQuery.addScalar("NR_CONTROLE_CARGA",Hibernate.LONG);				//13
    			sqlQuery.addScalar("SG_FILIAL_CC",Hibernate.STRING);				//14
    		}
    	};
    	
    	String sql = sqlColeta.getSql() + "\n UNION ALL \n"+sqlViagem.getSql() + "\nORDER BY SG_FILIAL_MANIFESTO, NR_MANIFESTO";
    	return getAdsmHibernateTemplate().findPaginatedBySql(sql, fd.getCurrentPage(), fd.getPageSize(), criteria, csq);
    }
    
	/**
	 * RowCount da Consulta de Conteúdo de Veiculos da SMP 
	 * @param tfm
	 * @return
	 */
    public Integer getRowCountConsultaConteudoVeiculos(TypedFlatMap tfm) {
    	Long idSolicMonitPreventivo = tfm.getLong("smp.idSolicMonitPreventivo");
    	Long idMeioTransporte = tfm.getLong("meioTransporteRodoviario.idMeioTransporte");
    	Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");

    	SqlTemplate sqlColeta = getSQLTemplateConsultaConteudoVeiculosColeta(idSolicMonitPreventivo, idMeioTransporte, idControleCarga);
    	SqlTemplate sqlViagem = getSQLTemplateConsultaConteudoVeiculosViagemEntrega(idSolicMonitPreventivo, idMeioTransporte, idControleCarga);

    	int rowCountColeta = getAdsmHibernateTemplate().getRowCountBySql(sqlColeta.getSql(false), sqlColeta.getCriteria()).intValue();
    	int rowCountViagem = getAdsmHibernateTemplate().getRowCountBySql(sqlViagem.getSql(false), sqlViagem.getCriteria()).intValue();
    	
    	return Integer.valueOf(rowCountColeta+rowCountViagem);
    }

    /**
     * Obtém o SqlTemplate da consulta em manifestos de coleta (Consulta de Conteúdo de Veiculos da SMP).
     * @param idSolicMonitPreventivo
     * @param idMeioTransporte
     * @param idControleCarga
     * @return
     */
    private SqlTemplate getSQLTemplateConsultaConteudoVeiculosColeta(Long idSolicMonitPreventivo, Long idMeioTransporte, Long idControleCarga) {
    	SqlTemplate sqlColeta = new SqlTemplate();

		sqlColeta.addProjection("MAN.NR_MANIFESTO",               "NR_MANIFESTO");
    	sqlColeta.addProjection("MAN.ID_MANIFESTO_COLETA",        "ID_MANIFESTO");
    	sqlColeta.addProjection("FIL.SG_FILIAL",                  "SG_FILIAL_MANIFESTO");
    	sqlColeta.addProjection("'C'",                            "TP_COLETA_VIAGEM");
    	sqlColeta.addProjection("CC.ID_CONTROLE_CARGA",           "ID_CONTROLE_CARGA");
		sqlColeta.addProjection("MAN.TP_STATUS_MANIFESTO_COLETA", "TP_STATUS");
		
		// CAMPOS NULOS
    	sqlColeta.addProjection("NULL",                           "TP_MANIFESTO");
    	sqlColeta.addProjection("NULL",                           "TP_ABRANGENCIA");
    	sqlColeta.addProjection("NULL",                           "TP_MANIFESTO_VIAGEM");
    	sqlColeta.addProjection("NULL",                           "TP_MANIFESTO_ENTREGA");
    	sqlColeta.addProjection("NULL",                           "SG_MOEDA");
    	sqlColeta.addProjection("NULL",                           "DS_SIMBOLO");
    	sqlColeta.addProjection("NULL",                           "VL_TOTAL_NOTA_MANIFESTO");

    	sqlColeta.addProjection("CC.NR_CONTROLE_CARGA",			  "NR_CONTROLE_CARGA");
    	sqlColeta.addProjection("FILIALCC.SG_FILIAL",			  "SG_FILIAL_CC");

		sqlColeta.addFrom("CONTROLE_CARGA CC"
				+" JOIN MANIFESTO_COLETA MAN       ON (MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA)"
				+" JOIN FILIAL FIL                 ON (FIL.ID_FILIAL         = MAN.ID_FILIAL_ORIGEM)"
				+" INNER JOIN FILIAL FILIALCC      ON (CC.ID_FILIAL_ORIGEM   = FILIALCC.ID_FILIAL)"
				+" LEFT JOIN SOLIC_MONIT_PREVENTIVO SMP ON (SMP.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA)");

    	if (idMeioTransporte != null) {
    		sqlColeta.addCriteria("CC.ID_TRANSPORTADO", "=", idMeioTransporte);
    		sqlColeta.addCustomCriteria("CC.TP_STATUS_CONTROLE_CARGA NOT IN ('CA','FE')");
    	}

    	sqlColeta.addCustomCriteria("MAN.TP_STATUS_MANIFESTO_COLETA NOT IN ('CA','FE')");
		sqlColeta.addCriteria("CC.ID_CONTROLE_CARGA", "=", idControleCarga);
   		sqlColeta.addCriteria("SMP.ID_SOLIC_MONIT_PREVENTIVO", "=", idSolicMonitPreventivo);
    	return sqlColeta;
    }


    /**
     * Obtém o SqlTemplate da consulta em manifestos de viagem e entrega (Consulta de Conteúdo de Veiculos da SMP).
     * @param idSolicMonitPreventivo
     * @param idMeioTransporte
     * @param idControleCarga
     * @return
     */
    private SqlTemplate getSQLTemplateConsultaConteudoVeiculosViagemEntrega(Long idSolicMonitPreventivo, Long idMeioTransporte, Long idControleCarga) {
    	SqlTemplate sqlViagemEntrega = new SqlTemplate();
    	String nrManifesto = new StringBuffer()
    	.append(" CASE ")
    	.append("  WHEN (MAN.TP_MANIFESTO='E' and ME.NR_MANIFESTO_ENTREGA is not null) THEN ME.NR_MANIFESTO_ENTREGA")
    	.append("  WHEN (MAN.TP_ABRANGENCIA='N' and MVN.NR_MANIFESTO_ORIGEM is not null) THEN MVN.NR_MANIFESTO_ORIGEM")
    	.append("  WHEN (MAN.TP_ABRANGENCIA='I' and MI.NR_MANIFESTO_INT is not null) THEN MI.NR_MANIFESTO_INT")
    	.append("  ELSE MAN.NR_PRE_MANIFESTO")
    	.append(" END")
    	.append(" AS NR_MANIFESTO").toString();
    	sqlViagemEntrega.addProjection(nrManifesto);
    	sqlViagemEntrega.addProjection("MAN.ID_MANIFESTO",         "ID_MANIFESTO");    	
    	sqlViagemEntrega.addProjection("FIL.SG_FILIAL",            "SG_FILIAL_MANIFESTO");
    	sqlViagemEntrega.addProjection("'V'",                      "TP_COLETA_VIAGEM");
    	sqlViagemEntrega.addProjection("CC.ID_CONTROLE_CARGA",     "ID_CONTROLE_CARGA");
    	sqlViagemEntrega.addProjection("MAN.TP_STATUS_MANIFESTO",  "TP_STATUS");
    	sqlViagemEntrega.addProjection("MAN.TP_MANIFESTO",         "TP_MANIFESTO");
    	sqlViagemEntrega.addProjection("MAN.TP_ABRANGENCIA",       "TP_ABRANGENCIA");
    	sqlViagemEntrega.addProjection("MAN.TP_MANIFESTO_VIAGEM",  "TP_MANIFESTO_VIAGEM");
    	sqlViagemEntrega.addProjection("MAN.TP_MANIFESTO_ENTREGA", "TP_MANIFESTO_ENTREGA");
    	sqlViagemEntrega.addProjection("MO.SG_MOEDA",              "SG_MOEDA");
    	sqlViagemEntrega.addProjection("MO.DS_SIMBOLO",            "DS_SIMBOLO");
    	sqlViagemEntrega.addProjection("MAN.VL_TOTAL_MANIFESTO",   "VL_TOTAL_NOTA_MANIFESTO");
    	sqlViagemEntrega.addProjection("CC.NR_CONTROLE_CARGA",     "NR_CONTROLE_CARGA");
    	sqlViagemEntrega.addProjection("FILIALCC.SG_FILIAL",	   "SG_FILIAL_CC");    	

    	sqlViagemEntrega.addFrom("CONTROLE_CARGA CC"
			  + " INNER JOIN MANIFESTO MAN                 ON (MAN.ID_CONTROLE_CARGA             = CC.ID_CONTROLE_CARGA)"
			  + " INNER JOIN FILIAL FIL                    ON (MAN.ID_FILIAL_ORIGEM              = FIL.ID_FILIAL)"
			  + " LEFT  JOIN MANIFESTO_VIAGEM_NACIONAL MVN ON (MVN.ID_MANIFESTO_VIAGEM_NACIONAL  = MAN.ID_MANIFESTO)"
			  + " LEFT  JOIN MANIFESTO_INTERNACIONAL MI    ON (MI.ID_MANIFESTO_INTERNACIONAL     = MAN.ID_MANIFESTO)"
			  + " LEFT  JOIN MANIFESTO_ENTREGA ME          ON (ME.ID_MANIFESTO_ENTREGA           = MAN.ID_MANIFESTO)"
			  + " INNER JOIN FILIAL FILIALCC               ON (CC.ID_FILIAL_ORIGEM               = FILIALCC.ID_FILIAL)"
			  + " LEFT JOIN  SOLIC_MONIT_PREVENTIVO SMP    ON (SMP.ID_CONTROLE_CARGA   			= CC.ID_CONTROLE_CARGA)"
			  + " LEFT JOIN MOEDA MO                       ON (MAN.ID_MOEDA                   = MO.ID_MOEDA)"
    	);

    	sqlViagemEntrega.addCriteria("CC.ID_CONTROLE_CARGA", "=", idControleCarga);
   		sqlViagemEntrega.addCriteria("SMP.ID_SOLIC_MONIT_PREVENTIVO", "=", idSolicMonitPreventivo);

    	if (idMeioTransporte != null) {
    		sqlViagemEntrega.addCriteria("CC.ID_TRANSPORTADO", "=", idMeioTransporte);
    		sqlViagemEntrega.addCustomCriteria("CC.TP_STATUS_CONTROLE_CARGA NOT IN ('CA','FE')");
    	}

    	sqlViagemEntrega.addCustomCriteria("MAN.TP_STATUS_MANIFESTO NOT IN ('FE','CA','DC','PM','ED')");
    	sqlViagemEntrega.addCustomCriteria("SMP.TP_STATUS_SMP <> 'CA'");
    	return sqlViagemEntrega;
    }


    /**
     * Obtém o SolicMonitPreventivo de acordo com o Controle de Cargas e a Filial recebidos.
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public SolicMonitPreventivo findByIdControleCargaAndFilial(Long idControleCarga, Long idFilial) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ")
    	.append(SolicMonitPreventivo.class.getName()).append(" smp ")
    	.append("where smp.controleCarga.id = ? and smp.filial.id = ? and smp.tpStatusSmp not in ('CA','FI')");
    	return (SolicMonitPreventivo)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga, idFilial});
    }

    
    /**
     * Obtém o SolicMonitPreventivo de acordo com o Controle de Cargas 
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public SolicMonitPreventivo findByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ")
    	.append(SolicMonitPreventivo.class.getName()).append(" smp ")
    	.append("where smp.controleCarga.id = ? and smp.tpStatusSmp not in ('CA','FI')");
    	return (SolicMonitPreventivo)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga});
    }
    
    
    /**
     * 
     * @param idControleCarga
     * @return
     */
    public List findSmpByControleCargaLocalTroca(Long idControleCarga) {
    	StringBuilder sql = new StringBuilder()
    	.append("select new map(smp.id as idSmp) ")
    	.append("from ")
    	.append(SolicMonitPreventivo.class.getName()).append(" smp ") 
    	.append("where ")
    	.append("smp.tpStatusSmp <> 'CA' ")
    	.append("and smp.controleTrecho.id in ( ")
	    	.append("select new map(ct1.id as idControleTrecho) ")
	    	.append("from ")
	    	.append(ControleTrecho.class.getName()).append(" ct1 ")
	    	.append("where ct1.controleCarga.id = ? ")
	    	.append("and ct1.dhChegada.value is null ")
    	.append(") ")
    	.append("and smp.filial.id in ( ")
	    	.append("select new map(ecc.filial.id as idFilialECC) ")
	    	.append("from ")
	    	.append(EventoControleCarga.class.getName()).append(" ecc ")
	    	.append("where ")
	    	.append("ecc.controleCarga.id = ? ")
	    	.append("and ecc.tpEventoControleCarga = 'FC' ")
	    	.append("and ecc.filial.id = ? ")
    	.append(") ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idControleCarga);
    	param.add(SessionUtils.getFilialSessao().getIdFilial());

    	List result = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    	return result;
    }

    
    /**
     * Altera o status para o Controle de Carga em questão.
     * 
     * @param idControleCarga
     */
    public void updateStatusSMPByIdControleCarga(Long idControleCarga, String statusSMP) {
    	StringBuffer sql = new StringBuffer()
	    	.append("update ")
	    	.append(SolicMonitPreventivo.class.getName()).append(" as smp ")
	    	.append(" set smp.tpStatusSmp = ? ")
	    	.append("where smp.controleCarga.id = ? ")
    		.append("and smp.tpStatusSmp = 'GE' ");

    	List param = new ArrayList();
    	param.add(statusSMP);
    	param.add(idControleCarga);

    	executeHql(sql.toString(), param);
    }

	
}
