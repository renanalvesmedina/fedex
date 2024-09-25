package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadrao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadraoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaCreditoDAO extends BaseCrudDao<NotaCredito, Long>{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaCredito.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idFilial
     * @param nrNotaCredito
     * @return <b>NotaCredito</b>
     */
    public NotaCredito findNotaCredito(Long idFilial, Long nrNotaCredito) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("filial.id", idFilial));
    	dc.add(Restrictions.eq("nrNotaCredito", nrNotaCredito));
		return (NotaCredito)getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    @SuppressWarnings("unchecked")
	public List<NotaCredito> findByIdControleCarga(Long idControleCarga) {
    	DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
    	criteria.setFetchMode("controleCarga", FetchMode.JOIN);
    	criteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));

    	return getAdsmHibernateTemplate().findByCriteria(criteria);
    }

    public boolean hasNotaCreditoEmitidaControleCarga(Long idControleCarga) {
    	StringBuffer hql = new StringBuffer(" from ");
    	hql.append(NotaCredito.class.getName()).append(" nc where nc.controleCarga.id = ? ");
    	hql.append(" and dhEmissao is not null");

    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{ idControleCarga }) > 0;
	}

    public boolean hasTipoParcelaNoControleCarga(Long idControleCarga, DomainValue tipoParcela) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" nc ");
        hql.append(" join nc.notaCreditoParcelas ncp ");
        hql.append(" join ncp.parcelaTabelaCe ptce ");
        hql.append(" where nc.controleCarga.id = ? ");
        hql.append(" and ptce.tpParcela = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(), new Object[]{ idControleCarga, tipoParcela.getValue() }) > 0;
    }

    public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate hql = createSqlTemplate(criteria);
    	setProjectionsPaginated(hql);
    	StringBuffer sql = new StringBuffer(getAdsmHibernateTemplate().generateSQLFromHQL(hql.getSql()));
    	sql.insert(0,"FROM (").append(")");
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(),hql.getCriteria());
    }  
    private void setProjectionsPaginated(SqlTemplate sql) {
    	//0
    	sql.addProjection("distinct TCE.tpRegistro");
    	sql.addProjection("TTCE.dsTipoTabelaColetaEntrega");
    	sql.addProjection("NC.nrNotaCredito");
    	sql.addProjection("MT.nrIdentificador");
    	sql.addProjection("MT.nrFrota");
    	sql.addProjection("PS.nrIdentificacao");
    	sql.addProjection("PS.tpIdentificacao");
    	sql.addProjection("PS.nmPessoa");
    	sql.addProjection("NC.dhGeracao");
    	sql.addProjection("NC.dhEmissao");
    	//1
    	sql.addProjection("NC.tpSituacaoAprovacao");
    	sql.addProjection("NC.idNotaCredito");
    	sql.addProjection("FI.idFilial");
    	sql.addProjection("FI.sgFilial");
    	sql.addProjection("PFI.nmFantasia");
    	sql.addProjection("MT.idMeioTransporte");
    	sql.addProjection("PS.idPessoa");
    	sql.addProjection("MOMT.dsModeloMeioTransporte");
    	sql.addProjection("MAMT.dsMarcaMeioTransporte");
    	sql.addProjection("MO.sgMoeda");
    	//2
    	sql.addProjection("MO.dsSimbolo");
    	sql.addProjection("NC.vlDescontoSugerido");
    	sql.addProjection("NC.vlDesconto");
    	sql.addProjection("NC.vlAcrescimoSugerido");
    	sql.addProjection("NC.vlAcrescimo");
    	//2.5
    	sql.addProjection("RFC.nrReciboFreteCarreteiro");
    	sql.addProjection("NC.vlDescUsoEquipamento");
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	SqlTemplate sql = createSqlTemplate(criteria);
    	setProjectionsPaginated(sql);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }

    public List findByIdCustom(Long idNotaCredito) {
    	TypedFlatMap criteria = new TypedFlatMap();
    	criteria.put("idNotaCredito",idNotaCredito);
    	SqlTemplate sql = createSqlTemplate(criteria);
    	
    	//Marcados os indices das projections para identificar melhor a leitura delas na action.
    	sql.addProjection("distinct FI.sgFilial"); 	//0
		sql.addProjection("PFI.nmFantasia");		//1
    	sql.addProjection("NC.nrNotaCredito");		//2
    	sql.addProjection("MT.nrFrota");			//3
    	sql.addProjection("MT.nrIdentificador");	//4

    	sql.addProjection("PS.nrIdentificacao");	//5
    	sql.addProjection("PS.tpIdentificacao");	//6	
    	sql.addProjection("PS.nmPessoa");			//7
    	sql.addProjection("TCE.tpRegistro");		//8
    	sql.addProjection("TTCE.dsTipoTabelaColetaEntrega"); //9
    	
    	sql.addProjection("MO.sgMoeda || ' ' || MO.dsSimbolo");//10 
    	sql.addProjection("NC.vlDescontoSugerido");//11
    	sql.addProjection("NC.vlDesconto");//12
    	sql.addProjection("NC.vlAcrescimoSugerido");//13
    	sql.addProjection("NC.vlAcrescimo");//14
    	
    	
    	sql.addProjection("NC.tpSituacaoAprovacao");//15 
    	sql.addProjection("NC.dhGeracao");//16
    	sql.addProjection("NC.dhEmissao");//17
    	sql.addProjection("NC.obNotaCredito");//18
    	sql.addProjection("NC.idNotaCredito");//19
    	
    	sql.addProjection("RFC.idReciboFreteCarreteiro");//20
    	sql.addProjection("RFC.tpSituacaoRecibo");//21
    	sql.addProjection("PR.idProprietario");//22
    	sql.addProjection("TCE.idTabelaColetaEntrega");//23
    	sql.addProjection("RFC.nrReciboFreteCarreteiro");//24
    	
    	sql.addProjection("FI.idFilial");//25
    	sql.addProjection("PS.tpPessoa");//26
    	sql.addProjection("PR.tpProprietario");//27
    	
    	sql.addProjection("CC.nrControleCarga");//28
    	sql.addProjection("CCFO.idFilial", "idFilialOrigemControleCarga");//29
    	sql.addProjection("CCFO.sgFilial", "sgFilialOrigemControleCarga");//30
    	sql.addProjection("CCFOP.nmFantasia", "nmFilialOrigemControleCarga");//31
    	
    	sql.addProjection("NC.vlDescUsoEquipamento");//32
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    public void executeUpdateParcelas(List parcelas) {
    	for(Iterator i = parcelas.iterator(); i.hasNext();) {
    		NotaCreditoParcela beanTemp = (NotaCreditoParcela)i.next();
    		getAdsmHibernateTemplate().evict(beanTemp);
    		NotaCreditoParcela bean = (NotaCreditoParcela)getAdsmHibernateTemplate().load(NotaCreditoParcela.class,beanTemp.getIdNotaCreditoParcela());
    		bean.setQtNotaCreditoParcela(beanTemp.getQtNotaCreditoParcela());
    		bean.setVlNotaCreditoParcela(beanTemp.getVlNotaCreditoParcela());
    		getAdsmHibernateTemplate().update(bean);
    	}
    	getAdsmHibernateTemplate().flush();
    }
    
    private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
    	SqlTemplate sql = new SqlTemplate();

    	StringBuffer from = new StringBuffer(NotaCredito.class.getName()).append(" AS NC ")
    		.append("INNER JOIN NC.controleCarga AS CC ")
    		.append("INNER JOIN NC.moedaPais AS MP ")
    		.append("INNER JOIN MP.moeda AS MO ")
    		.append("LEFT JOIN NC.reciboFreteCarreteiro AS RFC ")
    		.append("LEFT JOIN NC.notaCreditoParcelas AS NCP ")
    		.append("LEFT JOIN NCP.parcelaTabelaCe AS PTCE ")
    		.append("LEFT JOIN PTCE.tabelaColetaEntrega AS TCE ")
    		.append("LEFT JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
    		.append("INNER JOIN NC.filial AS FI ")
    		.append("INNER JOIN FI.pessoa AS PFI ")
    		.append("INNER JOIN CC.meioTransporteByIdTransportado AS MT ")
    		.append("INNER JOIN MT.modeloMeioTransporte AS MOMT ")
    		.append("INNER JOIN MOMT.marcaMeioTransporte AS MAMT ")
    		.append("INNER JOIN CC.proprietario AS PR ")
    		.append("INNER JOIN PR.pessoa AS PS ")
    		.append("INNER JOIN CC.filialByIdFilialOrigem AS CCFO ")
    		.append("INNER JOIN CCFO.pessoa AS CCFOP ");
    	
    	sql.addFrom(from.toString());
    	
    	if (criteria.getLong("idNotaCredito") != null) {
    		sql.addCriteria("NC.idNotaCredito","=",criteria.getLong("idNotaCredito"));
    		return sql;
    	}
    	if (criteria.getBoolean("isDhEmissao") != null &&
    			criteria.getBoolean("isDhEmissao").booleanValue())
    		sql.addCustomCriteria("NC.dhEmissao.value is not null");
    	
    	if (StringUtils.isNotBlank(criteria.getString("tpProprietario")))
    		sql.addCriteria("PR.tpProprietario","=",criteria.getString("tpProprietario"));
    	
    	sql.addCriteria("FI.id","=",criteria.getLong("filial.idFilial"));
    	
    	if (StringUtils.isNotBlank(criteria.getString("filial.sgFilial"))) {
    		sql.addCustomCriteria("LOWER(FI.sgFilial) LIKE LOWER(?)");
    		sql.addCriteriaValue(criteria.getString("filial.sgFilial"));
    	}
    	
    	sql.addCriteria("NC.nrNotaCredito","=",criteria.getLong("nrNotaCredito"));
    	sql.addCriteria("MT.id","=",criteria.getLong("controleCargas.meioTransporte.idMeioTransporte"));
    	sql.addCriteria("PR.id","=",criteria.getLong("controleCargas.proprietario.idProprietario"));
    	if (StringUtils.isNotBlank(criteria.getString("tabela")))
    		sql.addCriteria("TCE.tpRegistro","=",criteria.getString("tabela"));
    	
    	
    	sql.addCriteria("TTCE.id","=",criteria.getLong("tpTabela"));  

    	if (StringUtils.isNotBlank(criteria.getString("tpSituacaoAprovacao")))
    		sql.addCriteria("NC.tpSituacaoAprovacao","=",criteria.getString("tpSituacaoAprovacao"));

    	if (StringUtils.isNotBlank(criteria.getString("situacaoNotaCredito"))) {    	
    		if ("N".equals(criteria.getString("situacaoNotaCredito")))
    			sql.addCustomCriteria("NC.dhEmissao.value is null");
    		else
    			sql.addCustomCriteria("NC.dhEmissao.value is not null");
    	}
    	
    	sql.addCriteria("RFC.idReciboFreteCarreteiro","=",criteria.getLong("reciboFreteCarreteiro.idReciboFreteCarreteiro"));
    	
    	if (criteria.getYearMonthDay("dhGeracaoInicial") != null)
    		sql.addCriteria("NC.dhGeracao.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhGeracaoInicial")));
    	
    	if (criteria.getYearMonthDay("dhGeracaoFinal") != null)
    		sql.addCriteria("NC.dhGeracao.value","<",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhGeracaoFinal").plusDays(1)));
    	
    	if (criteria.getYearMonthDay("dhEmissaoInicial") != null)
    		sql.addCriteria("NC.dhEmissao.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhEmissaoInicial")));
    	
    	if (criteria.getYearMonthDay("dhEmissaoFinal") != null)
    		sql.addCriteria("NC.dhEmissao.value","<",JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dhEmissaoFinal").plusDays(1)));
    	
    	sql.addCustomCriteria("NC.tpNotaCredito IS NULL");
    	
    	sql.addOrderBy("NC.nrNotaCredito");
    	return sql;
    }
    
    public List findParcelasNotaCredito(Long idNotaCredito) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("NCP");
    	sql.addFrom(new StringBuffer(NotaCreditoParcela.class.getName()).append(" AS NCP ")
				.append("INNER JOIN fetch NCP.notaCredito AS NC ")
    			.append("INNER JOIN fetch NCP.parcelaTabelaCe AS PTCE ")
				.append("LEFT OUTER JOIN fetch NCP.faixaPesoParcelaTabelaCE AS FPP ")
				.append("INNER JOIN fetch PTCE.tabelaColetaEntrega AS TCL ").toString());
    	sql.addCriteria("NC.id","=",idNotaCredito);
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public List findLookupCuston(TypedFlatMap criteria) {
    	SqlTemplate sql = createSqlTemplate(criteria);
    	//FILIAL
    	sql.addProjection("distinct FI.sgFilial");
    	sql.addProjection("FI.idFilial");
    	sql.addProjection("PFI.nmFantasia");
    	//MEIO TRANSPORTE
    	sql.addProjection("MT.idMeioTransporte");
    	sql.addProjection("MT.nrIdentificador");
    	sql.addProjection("MT.nrFrota");
    	//PROPRIETARIO
    	sql.addProjection("PR.idProprietario");
    	sql.addProjection("PS.nrIdentificacao");
    	sql.addProjection("PS.tpIdentificacao");
    	sql.addProjection("PS.nmPessoa");
    	//NOTA DE CREDITO
    	sql.addProjection("NC.nrNotaCredito");    	
    	sql.addProjection("NC.idNotaCredito");
    	//MODELO E MARCA
    	sql.addProjection("MOMT.dsModeloMeioTransporte");
    	sql.addProjection("MAMT.dsMarcaMeioTransporte");
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public Map findValorAcrescimoDesconto(Long idNotaCredito){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map(nc.vlDesconto as vlDesconto, nc.vlAcrescimo as vlAcrescimo, m.dsSimbolo as dsSimbolo)");
    	hql.addFrom(NotaCredito.class.getName()+" nc " +
    			"join nc.moedaPais mp " +
    			"join mp.moeda m ");
    	hql.addCriteria("nc.idNotaCredito","=", idNotaCredito);
    	
    	return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    	
    }


    public Boolean validateNotaCreditoNaoEmitida(final Long idFilial, final Long idProprietario, final Long idTransportado) {
    	SqlTemplate hql = new SqlTemplate();

    	hql.addProjection("count(*)");
    	hql.addInnerJoin(NotaCredito.class.getName(), "nc");
    	hql.addInnerJoin("nc.controleCargas", "cc");
    	hql.addCriteria("cc.proprietario.idProprietario", "=", idProprietario);
    	hql.addCriteria("cc.meioTransporteByIdTransportado.idMeioTransporte", "=", idTransportado);
    	hql.addCriteria("nc.filial.idFilial", "=", idFilial);
    	hql.addCustomCriteria("nc.dhEmissao is null");
    	hql.addCustomCriteria("nc.tpNotaCredito is null");

    	return getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria()).equals(Long.parseLong("0"));
    }
    
    
	public List<NotaCredito> findNotasCreditosByIdControleCargaAndIgnoreIdNotaCredito(Long idControleCarga, Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
    	hql.append("select nocr ");
    	hql.append("from NotaCredito as nocr ");
    	hql.append("join nocr.controleCarga as coca ");
    	hql.append("where coca.idControleCarga = ? ");
    	params.add(idControleCarga);
    	if (idNotaCredito != null) {
    		hql.append("and nocr.idNotaCredito != ? ");
    		params.add(idNotaCredito);
		}
    	return (List<NotaCredito>) getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
	}

	// LMS 4153 
	public List<NotaCredito> findNotaCreditoComParcelaDHByNotaCreditoControleCarga(ControleCarga controleCarga, NotaCredito notaCredito) {
		StringBuilder hqlExists = new StringBuilder();
		hqlExists.append("SELECT ptc ");
		hqlExists.append("FROM ");
		hqlExists.append(ParcelaTabelaCe.class.getSimpleName() + " ptc, ");
		hqlExists.append(TabelaColetaEntrega.class.getSimpleName() + " tce, ");
		hqlExists.append(NotaCreditoParcela.class.getSimpleName() + " ncp ");
		hqlExists.append("WHERE tce = ptc.tabelaColetaEntrega ");
		hqlExists.append("AND ptc = ncp.parcelaTabelaCe "); 			// PARCELA_TABELA_CE.id_parcela_tabela_ce = NOTA_CREDITO_PARCELA.id_parcela_tabela_ce
		hqlExists.append("AND nc = ncp.notaCredito "); 				    // NOTA_CREDITO.id_nota_credito = NOTA_CREDITO_PARCELA.id_nota_credito
		hqlExists.append("AND nc.controleCarga.idControleCarga = ? "); 	// NC_NOVA.id_controle_carga = NOTA_CREDITO.id_controle_carga
		hqlExists.append("AND nc.idNotaCredito != ? "); 				// NOTA_CREDITO.id_nota_credito != NC_NOVA.id_nota_credito
		hqlExists.append("AND ptc.tpParcela IN ('DH') "); 				// PARCELA_TABELA_CE.tp_parcela IN ( 'DH' )
		hqlExists.append("AND ptc.vlDefinido > 0 "); 					// PARCELA_TABELA_CE.vl_definido > 0
		hqlExists.append("AND ncp.qtNotaCreditoParcela > 0 ");			// NOTA_CREDITO_PARCELA.qt_nota_credito_parcela > 0
		
		StringBuilder hql = new StringBuilder();		
		hql.append("SELECT nc ");
		hql.append("FROM ");
		hql.append(NotaCredito.class.getSimpleName() + " nc ");
		hql.append("WHERE EXISTS (").append(hqlExists).append(") ");
		hql.append("AND nc.controleCarga.idControleCarga = ?");
		
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {(Long)controleCarga.getIdControleCarga(), (Long)notaCredito.getIdNotaCredito(), (Long)controleCarga.getIdControleCarga()});
	}
	
	public void removeByIdsAnexoNotaCredito(List ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoNotaCredito.class.getName() + " WHERE idAnexoNotaCredito IN (:id)", ids);
	}

	public AnexoNotaCredito findAnexoNotaCreditoById(Long idAnexoNotaCredito) {
		return (AnexoNotaCredito) getAdsmHibernateTemplate().load(AnexoNotaCredito.class, idAnexoNotaCredito);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<AnexoNotaCredito> findPaginatedAnexoNotaCredito(PaginatedQuery paginatedQuery) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoNotaCredito AS idAnexoNotaCredito,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhInclusao,");
		hql.append(" usuario.usuarioADSM.nmUsuario as nmUsuario)");
		hql.append(" FROM AnexoNotaCredito AS anexo");
		hql.append("  INNER JOIN anexo.notaCredito notaCredito");
		hql.append("  INNER JOIN anexo.usuario usuario");
		hql.append(" WHERE notaCredito.idNotaCredito = :idNotaCredito");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
	}
	
	public Integer getRowCountAnexoNotaCredito(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_nota_credito WHERE id_nota_credito = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{criteria.get("idNotaCredito")});
	}

	public Boolean validateNotaCreditoNaoEmitidaPadrao(Long idFilial,Long idProprietario, Long idTransportado) {
		Map<String,Object> parans = new HashMap<String, Object>(); 
		parans.put("idFilial", idFilial);
		
		StringBuffer sql = new StringBuffer(" from ");
		sql.append(" Nota_Credito nc ,");
		sql.append("   controle_Carga cc");
		sql.append(" WHERE cc.id_controle_carga = nc.id_controle_carga");
		
		if(idProprietario != null){
			sql.append(" AND cc.id_Proprietario     = :idProprietario ");
			parans.put("idProprietario", idProprietario);
		}
		sql.append(" AND nc.id_Filial           = :idFilial ");
		
		if(idTransportado != null){
			sql.append(" AND cc.id_transportado     = :idTransportado ");
			parans.put("idTransportado", idTransportado);
		}
		
		sql.append(" AND nc.dh_Emissao         IS NULL ");
		sql.append(" AND nc.tp_nota_credito    IS NOT NULL ");
		sql.append(" AND nc.vl_total 	> 0    ");

        return getAdsmHibernateTemplate().getRowCountBySql(sql.toString() , parans ).equals(Integer.valueOf(0));

	}
}