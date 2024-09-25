 package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.DM_TP_CALCULO_TABELA_COLETA_ENTREGA;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.LongUtils;
 
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TabelaColetaEntregaDAO extends BaseCrudDao<TabelaColetaEntrega, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
	   return TabelaColetaEntrega.class;
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
        fetchModes.put("tipoMeioTransporte", FetchMode.JOIN);
        fetchModes.put("tipoTabelaColetaEntrega", FetchMode.JOIN);
        fetchModes.put("solicitacaoContratacao",FetchMode.JOIN);
        fetchModes.put("filial",FetchMode.JOIN);
        fetchModes.put("filial.pessoa",FetchMode.JOIN);
        fetchModes.put("cliente",FetchMode.JOIN);
        fetchModes.put("cliente.pessoa",FetchMode.JOIN);
        fetchModes.put("rotaColetaEntrega",FetchMode.JOIN);
        fetchModes.put("parcelaTabelaCes",FetchMode.SELECT);
	}
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
    	fetchModes.put("tipoMeioTransporte", FetchMode.JOIN);
    	fetchModes.put("tipoTabelaColetaEntrega", FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchModes);
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idMeioTransporte
     * @param idTipoMeioTransporte
     * @param idTipoTabelaColetaEntrega
     * @param idFilial
     * @param dtVigenciaInicial
     * @param tpRegistro
     * @return <b>TabelaColetaEntrega</b>
     */
    public TabelaColetaEntrega findTabelaColetaEntrega(Long idMeioTransporte, Long idTipoMeioTransporte, Long idTipoTabelaColetaEntrega, Long idFilial, YearMonthDay dtVigenciaInicial, String tpRegistro) {
    	DetachedCriteria dc = createDetachedCriteria();
    	if(LongUtils.hasValue(idMeioTransporte)) {
    		dc.add(Restrictions.eq("meioTransporteRodoviario.id", idMeioTransporte));
    	}
    	if(LongUtils.hasValue(idTipoTabelaColetaEntrega)) {
    		dc.add(Restrictions.eq("tipoTabelaColetaEntrega.id", idTipoTabelaColetaEntrega));
    	}
    	dc.add(Restrictions.eq("tipoMeioTransporte.id", idTipoMeioTransporte));
    	dc.add(Restrictions.eq("filial.id", idFilial));
    	dc.add(Restrictions.eq("dtVigenciaInicial", dtVigenciaInicial));
    	dc.add(Restrictions.eq("tpRegistro", tpRegistro));
		return (TabelaColetaEntrega) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idSolicitacaoContratacao
     * @return <b>TabelaColetaEntrega</b>
     */
    public TabelaColetaEntrega findBySolicitacaoContratacao(Long idSolicitacaoContratacao) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("solicitacaoContratacao.id", idSolicitacaoContratacao));
		return (TabelaColetaEntrega) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public int removeByIds(List ids) {
    	for(Iterator i = ids.iterator(); i.hasNext();)
    		removeById((Long)i.next());
    	return ids.size();
    }

    public void removeById(Long id) {
    	TabelaColetaEntrega bean = (TabelaColetaEntrega)getAdsmHibernateTemplate().load(getPersistentClass(),id);
    	bean.getParcelaTabelaCes().clear();
    	getAdsmHibernateTemplate().delete(bean);
    }

    private SqlTemplate createFindDefault(TypedFlatMap criteria) {
    	SqlTemplate sql = new SqlTemplate();
    	//FROM E JOINS
    	sql.addFrom((new StringBuffer(TabelaColetaEntrega.class.getName())).append(" AS TCE ")
		    			.append("LEFT JOIN TCE.meioTransporteRodoviario AS MTR ")
		    			.append("LEFT JOIN MTR.meioTransporte AS MT ")
		    			.append("LEFT JOIN MT.meioTranspProprietarios AS MTP ")
		    			.append("LEFT JOIN MTP.proprietario AS PR ")
		    			.append("LEFT JOIN PR.pessoa AS PS ")
			    		.append("INNER JOIN TCE.solicitacaoContratacao AS SC ")
			    		.append("INNER JOIN TCE.filial AS F ").toString());

    	sql.addCustomCriteria(new StringBuffer("((MTP.id = (select max(MTP2.id) from ")
    				.append(MeioTranspProprietario.class.getName()).append(" AS MTP2 ")
    				.append("INNER JOIN MTP2.meioTransporte MT2 ")
    				.append("WHERE ((MTP2.dtVigenciaInicial >= TCE.dtVigenciaInicial AND MTP2.dtVigenciaInicial < TCE.dtVigenciaFinal) OR ( ")
    				.append("MTP2.dtVigenciaInicial <= TCE.dtVigenciaInicial AND MTP2.dtVigenciaFinal > TCE.dtVigenciaInicial)) ")
    				.append("AND MT2.id = MT.id)) OR MTP.id is null)").toString());

    	if (criteria.getLong("idTabelaColetaEntrega") != null) {
    		sql.addCriteria("TCE.idTabelaColetaEntrega","=",criteria.getLong("idTabelaColetaEntrega"));
    		return sql;
    	}

		if (!StringUtils.isBlank(criteria.getString("vigentes"))) {
			if (criteria.getString("vigentes").equals("S")) {
				sql.addCustomCriteria("(TCE.dtVigenciaInicial <= ? and TCE.dtVigenciaFinal >= ? )");
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			} else if (criteria.getString("vigentes").equals("N")) {
				sql.addCustomCriteria("(TCE.dtVigenciaInicial > ? or TCE.dtVigenciaFinal < ? )");
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			}
		}
        
    	sql.addCriteria("F.idFilial","=",criteria.getLong("filial.idFilial"));
    	sql.addCriteria("MTR.idMeioTransporte","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
    	sql.addCriteria("PR.idProprietario","=",criteria.getLong("proprietario.idProprietario"));
    	sql.addCriteria("SC.idSolicitacaoContratacao","=",criteria.getLong("solicitacaoContratacao.idSolicitacaoContratacao"));
    	sql.addCriteria("TCE.dtVigenciaInicial","<=",criteria.getYearMonthDay("dtVigenciaInicial"));
    	sql.addCriteria("TCE.dtVigenciaFinal",">=",criteria.getYearMonthDay("dtVigenciaFinal"));
    	sql.addCriteria("SC.tpSituacaoContratacao","=","AP");
    	
    	return sql;
    }
    
    public List  findByObject(TabelaColetaEntrega tabelaColetaEntrega) {
    	DetachedCriteria dc = DetachedCriteria.forClass(TabelaColetaEntrega.class,"PTC") ;
 
	    	if (tabelaColetaEntrega.getFilial() != null){
	    		dc.createAlias("PTC.filial", "FLA");
	    		dc.setFetchMode("PTC.filial",FetchMode.JOIN);
	    		dc.add(Restrictions.eq("FLA.idFilial",tabelaColetaEntrega.getFilial().getIdFilial()));
	    	}
	    	if (tabelaColetaEntrega.getMeioTransporteRodoviario() != null){
	    		dc.createAlias("PTC.meioTransporteRodoviario", "MTR");
	    		dc.setFetchMode("PTC.meioTransporteRodoviario",FetchMode.JOIN);
	    		dc.add(Restrictions.eq("MTR.idMeioTransporte",tabelaColetaEntrega.getMeioTransporteRodoviario().getIdMeioTransporte()));
	    	}  
	    	if (tabelaColetaEntrega.getTipoTabelaColetaEntrega() != null){
	    		dc.createAlias("PTC.tipoTabelaColetaEntrega", "TCE");
	    		dc.setFetchMode("PTC.tipoTabelaColetaEntrega",FetchMode.JOIN);
	    		dc.add(Restrictions.eq("TCE.idTipoTabelaColetaEntrega",tabelaColetaEntrega.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega()));
	    	}
	    	if (tabelaColetaEntrega.getIdTabelaColetaEntrega() != null){
	    		dc.add(Restrictions.eq("PTC.idTabelaColetaEntrega",tabelaColetaEntrega.getIdTabelaColetaEntrega()));
	    	}
	    	if (tabelaColetaEntrega.getTpRegistro() != null){
	    		dc.add(Restrictions.eq("PTC.tpRegistro",tabelaColetaEntrega.getTpRegistro()));
	    	}
	    	if (tabelaColetaEntrega.getTpSituacaoAprovacao() != null){
	    		dc.add(Restrictions.eq("PTC.tpSituacaoAprovacao",tabelaColetaEntrega.getTpSituacaoAprovacao()));
	    	}
    	return  getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public TabelaColetaEntrega findByIdCustom(Long id) {
    	SqlTemplate sql = new SqlTemplate();
    	//FROM E JOINS
    	sql.addFrom((new StringBuffer(TabelaColetaEntrega.class.getName())).append(" AS TCE ")
		    			.append("LEFT JOIN FETCH TCE.meioTransporteRodoviario AS MTR ")
		    			.append("LEFT JOIN FETCH MTR.meioTransporte AS MT ")
		    			.append("LEFT JOIN FETCH MT.meioTranspProprietarios AS MTP ")
		    			.append("LEFT JOIN FETCH MTP.proprietario AS PR ")
		    			.append("LEFT JOIN FETCH PR.pessoa AS PS ")
			    		.append("INNER JOIN FETCH TCE.solicitacaoContratacao AS SC ")
			    		.append("INNER JOIN FETCH TCE.filial AS F ")
			    		.append("INNER JOIN FETCH F.pessoa AS PF ").toString());

    	sql.addCustomCriteria(new StringBuffer("((MTP.id = (select max(MTP2.id) from ")
				.append(MeioTranspProprietario.class.getName()).append(" AS MTP2 ")
				.append("INNER JOIN MTP2.meioTransporte MT2 ")  
				.append("WHERE ((MTP2.dtVigenciaInicial >= TCE.dtVigenciaInicial AND MTP2.dtVigenciaInicial < TCE.dtVigenciaFinal) OR ( ")
				.append("MTP2.dtVigenciaInicial <= TCE.dtVigenciaInicial AND MTP2.dtVigenciaFinal > TCE.dtVigenciaInicial)) ")
				.append("AND MT2.id = MT.id)) OR (MTP.id is null))").toString());
				
    	sql.addCriteria("TCE.idTabelaColetaEntrega","=",id);

    	return (TabelaColetaEntrega)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(false),sql.getCriteria());
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	SqlTemplate sql = createFindDefault(criteria);
		
    	//Projections
		sql.addProjection("new Map(TCE.dtVigenciaFinal AS dtVigenciaFinal");
		sql.addProjection("TCE.dtVigenciaInicial AS dtVigenciaInicial");
		sql.addProjection("TCE.idTabelaColetaEntrega AS idTabelaColetaEntrega");
		sql.addProjection("SC.nrSolicitacaoContratacao AS nrContratacao");
		sql.addProjection("PS.nmPessoa AS proprietarioNome");
		sql.addProjection("F.sgFilial AS sgFilial");
		sql.addProjection("PS.tpIdentificacao AS proprietarioTpIdentificacao");  
		sql.addProjection("PS.nrIdentificacao AS proprietarioNrIdentificacao");
		sql.addProjection("SC.nrIdentificacaoMeioTransp AS nrIdentificador)");
		
		//Order
		sql.addOrderBy("MT.nrIdentificador");
		sql.addOrderBy("TCE.dtVigenciaInicial");

    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }

    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate hql = this.getSqlTemplate(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }
    public SqlTemplate getSqlTemplate(TypedFlatMap criteria){
        SqlTemplate sql = new SqlTemplate();
        
        // Relacionamentos
        sql.addFrom((new StringBuffer(TabelaColetaEntrega.class.getName())).append(" AS TCE ")
                .append("LEFT JOIN TCE.tipoMeioTransporte AS TMT ")
                .append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
                .append("INNER JOIN TCE.filial AS F ")
                .append("INNER JOIN F.pessoa AS PF ")
                .append("LEFT JOIN TCE.cliente AS C ")
                .append("LEFT JOIN C.pessoa AS CP ")
                .append("LEFT JOIN TCE.rotaColetaEntrega AS R ").toString());

        // Critério de pesquisa
        sql.addCriteria("F.idFilial", "=", criteria.getLong("filial.idFilial"));
        sql.addCriteria("TCE.tpRegistro", "=", "A");
        sql.addCriteria("TCE.dtVigenciaInicial", ">=", criteria.getYearMonthDay("dtVigenciaInicial"));
        sql.addCriteria("TCE.dtVigenciaFinal", "<=", criteria.getYearMonthDay("dtVigenciaFinal"));
        
        sql.addCriteria("TCE.tipoMeioTransporte.id", "=", criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
        sql.addCriteria("TTCE.idTipoTabelaColetaEntrega.id", "=", criteria.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
        
        sql.addCriteria("TCE.rotaColetaEntrega.id", "=", criteria.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
        
        sql.addCriteria("TCE.tpCalculo", "=", criteria.getString("tpCalculo"));
        
        
		if (!StringUtils.isBlank(criteria.getString("vigentes"))) {
			if (criteria.getString("vigentes").equals("S")) {
				sql.addCustomCriteria("(TCE.dtVigenciaInicial <= ? and TCE.dtVigenciaFinal >= ? )");
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			} else if (criteria.getString("vigentes").equals("N")) {
				sql.addCustomCriteria("(TCE.dtVigenciaInicial > ? or TCE.dtVigenciaFinal < ? )");
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			}
		}
        
        // Order By
        sql.addOrderBy("TTCE.dsTipoTabelaColetaEntrega");
        sql.addOrderBy("TMT.dsTipoMeioTransporte");
        sql.addOrderBy("TCE.dtVigenciaInicial");
        return sql;
        
    } 
    // Find para ser usado na tela de listagem, com orderby.
    public ResultSetPage findOrdenedPaginated(TypedFlatMap criteria) {
        SqlTemplate sql = getSqlTemplate(criteria);
        sql.addProjection("new map(TCE.idTabelaColetaEntrega AS idTabelaColetaEntrega");
        sql.addProjection("TMT.dsTipoMeioTransporte AS tipoMeioTransporte_dsTipoMeioTransporte");
        sql.addProjection("TTCE.dsTipoTabelaColetaEntrega AS tipoTabelaColetaEntrega_dsTipoTabelaColetaEntrega");
        sql.addProjection("TCE.tpCalculo AS tpCalculo");
        sql.addProjection("R.nrRota AS rotaColetaEntrega_nrRota");
        sql.addProjection("R.dsRota AS rotaColetaEntrega_dsRota");
        sql.addProjection("CP.nmPessoa AS cliente_nmPessoa");
        sql.addProjection("CP.nrIdentificacao AS cliente_nrIdentificacao");
        sql.addProjection("CP.tpIdentificacao AS cliente_tpIdentificacao");
        sql.addProjection("TCE.dtVigenciaInicial AS dtVigenciaInicial");
        sql.addProjection("TCE.dtVigenciaFinal AS dtVigenciaFinal)");
        
        FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

        return  getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
  
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate sql = createFindDefault(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }
  
	public List validateVigenciaTipoTabelaMeioTransporte(Long idFilial, Long idTabelaColetaEntrega, Long idTipoTabelaColetaEntrega, Long idTipoMeioTransporte, String tpCalculo, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, String tpRegistro) {
    	DetachedCriteria dc = DetachedCriteria.forClass(TabelaColetaEntrega.class,"TCE")
		.createAlias("TCE.tipoMeioTransporte","tmt")
		.createAlias("TCE.tipoTabelaColetaEntrega","ttce")
		.createAlias("TCE.filial","f")
		.add(Restrictions.eq("ttce.idTipoTabelaColetaEntrega",idTipoTabelaColetaEntrega))
		.add(Restrictions.eq("tmt.idTipoMeioTransporte",idTipoMeioTransporte))
		.add(Restrictions.eq("f.idFilial",idFilial))
    	.add(Restrictions.eq("TCE.tpCalculo",tpCalculo))
    	.add(Restrictions.eq("TCE.tpRegistro",tpRegistro));
		if (idTabelaColetaEntrega != null) {
			dc.add(Restrictions.ne("TCE.idTabelaColetaEntrega",idTabelaColetaEntrega));
		}
		
		// Monta vigencia padrao
		dc = JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List validateVigenciaTipoTabelaMeioTransporte(Long idFilial,
			Long idTabelaColetaEntrega, Long idTipoTabelaColetaEntrega,
			Long idTipoMeioTransporte, String tpCalculo,
			Long idRotaColetaEntrega, Long idCliente,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	DetachedCriteria dc = DetachedCriteria.forClass(TabelaColetaEntrega.class,"TCE")
		.createAlias("TCE.tipoTabelaColetaEntrega","ttce")
		.createAlias("TCE.filial","f")
		.add(Restrictions.eq("ttce.idTipoTabelaColetaEntrega",idTipoTabelaColetaEntrega))
		.add(Restrictions.eq("f.idFilial",idFilial))
    	.add(Restrictions.eq("TCE.tpCalculo",tpCalculo))
    	.add(Restrictions.eq("TCE.rotaColetaEntrega.id", idRotaColetaEntrega))
    	.add( idCliente == null?  Restrictions.isNull("TCE.cliente.id"): Restrictions.eq("TCE.cliente.id",idCliente))
    	.add(idTipoMeioTransporte == null?Restrictions.isNull("TCE.tipoMeioTransporte"):Restrictions.eq("TCE.tipoMeioTransporte.id",idTipoMeioTransporte));
		if (idTabelaColetaEntrega != null) {
			dc.add(Restrictions.ne("TCE.idTabelaColetaEntrega",idTabelaColetaEntrega));
		}
		
		// Monta vigencia padrao
		dc = JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * Find tabela de frete de agregados vigentes na data da consulta
	 * @param idFilial
	 * @param tpRegistro
	 * @param dtConsulta
	 * @param idRotaColetaEntrega 
	 * @param tpCalculo 
	 * @return
	 */ 
	public List findTabelaColetaEntregaVigentes(Long idFilial, String tpRegistro, Long idMeioTransporte, YearMonthDay dtConsulta, Long idTipoMeioTransporte, Long idRotaColetaEntrega, String tpCalculo){
    	DetachedCriteria dc = DetachedCriteria.forClass(TabelaColetaEntrega.class,"tce")
		.createAlias("tce.filial","f")
		.add(Restrictions.eq("tce.tpRegistro",tpRegistro))
		.add(Restrictions.eq("f.idFilial",idFilial))
		.add(Restrictions.eq("tce.tpCalculo", tpCalculo));
    	if(idRotaColetaEntrega != null){
    		dc.add(Restrictions.eq("tce.rotaColetaEntrega.id", idRotaColetaEntrega));
    	}
    	if (idMeioTransporte != null){
    		dc.add(Restrictions.or(
    				Restrictions.and(
    						Restrictions.eq("tce.meioTransporteRodoviario.id",idMeioTransporte), 
    						Restrictions.eq("tce.tpSituacaoAprovacao","A")
    				),
    				Restrictions.and(
    						Restrictions.eq("tce.tpCalculo",DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_2.toString()),// calculo 1 pode nao ter transporte rodoviario null, calculo do tipo 2 permite
    						Restrictions.isNull("tce.meioTransporteRodoviario")
    				))
    		);
    	}    
    	if (idTipoMeioTransporte != null){
    		dc.add(Restrictions.or(
    				Restrictions.eq("tce.tipoMeioTransporte.id",idTipoMeioTransporte),
    				
    				Restrictions.and(
    						Restrictions.eq("tce.tpCalculo",DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_2.toString()),// calculo 1 pode nao ter transporte rodoviario null, calculo do tipo 2 permite
    						Restrictions.isNull("tce.tipoMeioTransporte")
    				))
    		);
    	}
    	
    	
		dc = JTVigenciaUtils.getDetachedVigencia(dc, dtConsulta, dtConsulta);

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List<Map<String, Long>> findTabelaColetaEntregaTipo2ByIdControleCarga(Long idControleCarga) {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT ");
		query.append(" TABELA_COLETA_ENTREGA.ID_TABELA_COLETA_ENTREGA as ID_TABELA_COLETA_ENTREGA ");
		query.append(" , TABELA_COLETA_ENTREGA.ID_MOEDA_PAIS as ID_MOEDA_PAIS");
		query.append(" , TABELA_COLETA_ENTREGA_CC.ID_TABELA_COLETA_ENTREGA_CC as ID_TABELA_COLETA_ENTREGA_CC");
		query.append(" , TABELA_COLETA_ENTREGA.ID_CLIENTE as  ID_CLIENTE ");
		query.append(" FROM CONTROLE_CARGA ");
		query.append(" INNER JOIN TABELA_COLETA_ENTREGA_CC on TABELA_COLETA_ENTREGA_CC.id_controle_carga = controle_carga.id_controle_carga ");
		query.append(" INNER JOIN TABELA_COLETA_ENTREGA on TABELA_COLETA_ENTREGA.id_TABELA_COLETA_ENTREGA = TABELA_COLETA_ENTREGA_CC.id_TABELA_COLETA_ENTREGA ");
		query.append(" WHERE  ");
		query.append(" TABELA_COLETA_ENTREGA.TP_CALCULO = 'C2' "); 
		query.append(" AND TP_REGISTRO = 'A' ");
		query.append(" AND CONTROLE_CARGA.ID_CONTROLE_CARGA = ");
		query.append(idControleCarga);
		
		query.append(" AND ((TABELA_COLETA_ENTREGA.ID_TIPO_MEIO_TRANSPORTE is null ");
		query.append(" AND TABELA_COLETA_ENTREGA.ID_CLIENTE is null) ");

		query.append(" OR ( ");
		query.append(" EXISTS (SELECT 1 FROM MANIFESTO ");
		query.append(" inner join PRE_MANIFESTO_DOCUMENTO on manifesto.id_manifesto = PRE_MANIFESTO_DOCUMENTO.id_manifesto ");
		query.append(" inner join docto_servico on PRE_MANIFESTO_DOCUMENTO.id_docto_servico = docto_servico.id_docto_servico ");
		query.append(" where  controle_carga.id_controle_carga = MANIFESTO.id_controle_carga ");
		query.append(" and TABELA_COLETA_ENTREGA.ID_CLIENTE = docto_servico.ID_CLIENTE_DESTINATARIO) ");
		query.append(" and TABELA_COLETA_ENTREGA.ID_TIPO_MEIO_TRANSPORTE is null ");
		query.append(" ) ");

		query.append(" or ( ");
		query.append(" TABELA_COLETA_ENTREGA.ID_CLIENTE  is null "); 
		query.append(" and exists( ");
		query.append(" select 1 from veiculo_controle_carga ");
		query.append(" inner join meio_transporte on meio_transporte.id_meio_transporte = veiculo_controle_carga.id_meio_transporte ");
		query.append(" inner join  modelo_meio_transporte on meio_transporte.id_modelo_meio_transporte = modelo_meio_transporte.id_modelo_meio_transporte ");
		query.append(" where modelo_meio_transporte.id_tipo_meio_transporte = TABELA_COLETA_ENTREGA.ID_TIPO_MEIO_TRANSPORTE  ");
		query.append(" and veiculo_controle_carga.id_controle_carga = controle_carga.id_controle_carga)) ");

		query.append(" or ( ");
		query.append(" exists (select 1 from MANIFESTO ");
		query.append(" inner join PRE_MANIFESTO_DOCUMENTO on manifesto.id_manifesto = PRE_MANIFESTO_DOCUMENTO.id_manifesto ");
		query.append(" inner join docto_servico on PRE_MANIFESTO_DOCUMENTO.id_docto_servico = docto_servico.id_docto_servico ");
		query.append(" where  controle_carga.id_controle_carga = MANIFESTO.id_controle_carga ");
		query.append(" and TABELA_COLETA_ENTREGA.ID_CLIENTE = docto_servico.ID_CLIENTE_DESTINATARIO) ");
		query.append(" and exists( ");
		query.append(" select 1 from veiculo_controle_carga ");
		query.append(" inner join meio_transporte on meio_transporte.id_meio_transporte = veiculo_controle_carga.id_meio_transporte ");
		query.append(" inner join  modelo_meio_transporte on meio_transporte.id_modelo_meio_transporte = modelo_meio_transporte.id_modelo_meio_transporte ");
		query.append(" where modelo_meio_transporte.id_tipo_meio_transporte = TABELA_COLETA_ENTREGA.ID_TIPO_MEIO_TRANSPORTE "); 
		query.append(" and veiculo_controle_carga.id_controle_carga = controle_carga.id_controle_carga ) ");
		query.append(" )) ");
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_TABELA_COLETA_ENTREGA",Hibernate.LONG);
				sqlQuery.addScalar("ID_MOEDA_PAIS",Hibernate.LONG);
				sqlQuery.addScalar("ID_TABELA_COLETA_ENTREGA_CC",Hibernate.LONG);
				sqlQuery.addScalar("ID_CLIENTE",Hibernate.LONG);
			}
		};
		
		List l = getAdsmHibernateTemplate().findPaginatedBySql(query.toString(), Integer.valueOf(1), Integer.valueOf(10000), new HashMap(), confSql).getList();
		List<Map<String, Long>> lTabelaColetaEntrega = new ArrayList<Map<String, Long>>();
		for (Object idArray: l) {
			Map<String, Long> entry = new HashMap<String, Long>();
			entry.put("idTabelaColetaEntrega", (Long) ((Object[]) idArray)[0]);
			entry.put("idMoedaPais", (Long) ((Object[]) idArray)[1]);
			entry.put("idTabelaColetaEntregaCC", (Long) ((Object[]) idArray)[2]);
			entry.put("idCliente", (Long) ((Object[]) idArray)[3]);
			
			lTabelaColetaEntrega.add(entry);
		}
		return lTabelaColetaEntrega;
	}

	public List<TabelaColetaEntrega> findByIdNotaCreditoAndVlDefinidoParcelaCE(Long idNotaCredito, BigDecimal vlDefinido) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append("select tace ");
		hql.append("from TabelaColetaEntrega tace ");
		hql.append("join tace.parcelaTabelaCes as patc ");
		hql.append("join patc.notaCreditoParcelas as nocp ");
		hql.append("join nocp.notaCredito as nocr ");
		hql.append("where nocr.idNotaCredito = ? ");
		params.add(idNotaCredito);
		if (vlDefinido != null) {
			hql.append(" and patc.vlDefinido > ? ");
			params.add(vlDefinido);
		}
		return (List<TabelaColetaEntrega>) getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
	}

	public DomainValue findTpRegistroTabelaColetaEntregaByIdNotaCredito(Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append("select distinct tace.tpRegistro ");
		hql.append("from TabelaColetaEntrega tace ");
		hql.append("join tace.parcelaTabelaCes as patc ");
		hql.append("join patc.notaCreditoParcelas as nocp ");
		hql.append("join nocp.notaCredito as nocr ");
		hql.append("where nocr.idNotaCredito = ? ");
		params.add(idNotaCredito);
		return (DomainValue) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}
	
	public List<TabelaColetaEntrega> findTabelaColetaEntregaComParcelaPFOrPVSemEntregaByIdControleCarga(ControleCarga controleCarga) {
		StringBuilder hqlExists = new StringBuilder();
		hqlExists.append("SELECT ptc ");
		hqlExists.append("FROM ");
		hqlExists.append(ParcelaTabelaCe.class.getSimpleName() + " ptc, ");
		hqlExists.append(NotaCreditoParcela.class.getSimpleName() + " ncp ");
		hqlExists.append("WHERE ");
		hqlExists.append("nc = ncp.notaCredito AND "); //NOTA_CREDITO.id_nota_credito = NOTA_CREDITO_PARCELA.id_nota_credito
		hqlExists.append("tce = ptc.tabelaColetaEntrega AND ");
		hqlExists.append("ptc = ncp.parcelaTabelaCe AND ");//PARCELA_TABELA_CE.id_parcela_tabela_ce = NOTA_CREDITO_PARCELA.id_parcela_tabela_ce
		hqlExists.append("ptc.tpParcela IN ('PV', 'PF') AND ");//PARCELA_TABELA_CE.tp_parcela IN ( 'PV', 'PF' )
		hqlExists.append("ptc.vlDefinido > 0");//PARCELA_TABELA_CE.vl_definido > 0
		
		StringBuilder hqlNotExists = new StringBuilder();
		hqlNotExists.append("SELECT ncd ");
		hqlNotExists.append("FROM ");
		hqlNotExists.append(NotaCreditoDocto.class.getSimpleName() + " ncd ");
		hqlNotExists.append("WHERE ");
		hqlNotExists.append("ncd.notaCredito = nc"); //NOTA_CREDITO.id_nota_credito = NOTA_CREDITO_DOCTO.id_nota_credito
		
		StringBuilder hql = new StringBuilder();		
		hql.append("SELECT tce ");
		hql.append("FROM ");
		hql.append(TabelaColetaEntrega.class.getSimpleName() + " tce, ");
		hql.append(NotaCredito.class.getSimpleName() + " nc ");
		hql.append("WHERE EXISTS (").append(hqlExists).append(") ");
		hql.append("AND ");
		hql.append("NOT EXISTS (").append(hqlNotExists).append(") ");
		hql.append("AND ");
		hql.append("nc.controleCarga.idControleCarga = ?");
		
		return getAdsmHibernateTemplate().find(hql.toString(), (Long)controleCarga.getIdControleCarga());
	}
	
	// LMS-4153 - item 19
	public List<TabelaColetaEntrega> findTabelaColetaEntregaComParcelaDHByControleCarga(ControleCarga controleCarga) {
		StringBuilder hqlExists = new StringBuilder();
		hqlExists.append("SELECT ptc ");
		hqlExists.append("FROM ");
		hqlExists.append(ParcelaTabelaCe.class.getSimpleName() + " ptc, ");
		hqlExists.append(NotaCreditoParcela.class.getSimpleName() + " ncp ");
		hqlExists.append("WHERE ");
		hqlExists.append("nc = ncp.notaCredito AND "); //NOTA_CREDITO.id_nota_credito = NOTA_CREDITO_PARCELA.id_nota_credito
		hqlExists.append("tce = ptc.tabelaColetaEntrega AND ");
		hqlExists.append("ptc = ncp.parcelaTabelaCe AND ");//PARCELA_TABELA_CE.id_parcela_tabela_ce = NOTA_CREDITO_PARCELA.id_parcela_tabela_ce
		hqlExists.append("ptc.tpParcela IN ('DH') AND ");//PARCELA_TABELA_CE.tp_parcela IN ( 'DH' )
		hqlExists.append("ptc.vlDefinido > 0");//PARCELA_TABELA_CE.vl_definido > 0
		
		StringBuilder hql = new StringBuilder();		
		hql.append("SELECT tce ");
		hql.append("FROM ");
		hql.append(TabelaColetaEntrega.class.getSimpleName() + " tce, ");
		hql.append(NotaCredito.class.getSimpleName() + " nc ");
		hql.append("WHERE EXISTS (").append(hqlExists).append(") ");
		hql.append("AND ");
		hql.append("nc.controleCarga.idControleCarga = ?");
		
		return getAdsmHibernateTemplate().find(hql.toString(), (Long)controleCarga.getIdControleCarga());
	}
	
}