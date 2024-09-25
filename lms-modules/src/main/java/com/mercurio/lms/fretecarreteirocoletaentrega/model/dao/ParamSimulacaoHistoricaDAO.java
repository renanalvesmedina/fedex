package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NcParcelaSimulacao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParamSimulacaoHistoricaDAO extends BaseCrudDao<ParamSimulacaoHistorica, Long>{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial",FetchMode.JOIN);
		fetchModes.put("filial.moeda",FetchMode.JOIN);
		fetchModes.put("filial.pessoa",FetchMode.JOIN);
		fetchModes.put("ncParcelaSimulacoes",FetchMode.SELECT);
		
		fetchModes.put("tipoMeioTransporte",FetchMode.JOIN);
		fetchModes.put("tipoTabelaColetaEntrega",FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario",FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
		super.initFindByIdLazyProperties(fetchModes);
	}
	
	protected void initFindListLazyProperties(Map fetchModes) {
		fetchModes.put("filial",FetchMode.JOIN);
		fetchModes.put("filial.pessoa",FetchMode.JOIN);
		
		fetchModes.put("tipoMeioTransporte",FetchMode.JOIN);
		fetchModes.put("tipoTabelaColetaEntrega",FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario",FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
		super.initFindListLazyProperties(fetchModes);
	}
    protected final Class getPersistentClass() {
        return ParamSimulacaoHistorica.class;
    }

    public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate hql = createFindToGrid(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
    }
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	SqlTemplate hql = createFindToGrid(criteria);
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
    }
    
    
    public List findNcParcelaSimulacao(Long idParamSimulacaoHistorica,String tpParcela) {
    	return getAdsmHibernateTemplate().findByDetachedCriteria(DetachedCriteria.forClass(NcParcelaSimulacao.class)
    		.add(Restrictions.eq("paramSimulacaoHistorica.id",idParamSimulacaoHistorica))
    		.add(Restrictions.eq("tpParcela",tpParcela)));
    }
    private SqlTemplate createFindToGrid(TypedFlatMap criteria) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addFrom(new StringBuffer(ParamSimulacaoHistorica.class.getName()).append(" AS PSH ")
    		.append("INNER JOIN fetch PSH.filial AS FI ")
    		.append("LEFT  JOIN fetch PSH.tipoMeioTransporte AS TMT ")
    		.append("LEFT  JOIN fetch PSH.tipoTabelaColetaEntrega AS TTCE ")
    		.append("LEFT  JOIN fetch PSH.meioTransporteRodoviario AS MTR ").toString());

    	if (criteria.getYearMonthDay("dtEmissaoInicial") != null)
    		hql.addCriteria("PSH.dhCriacao.value",">=",criteria.getYearMonthDay("dtEmissaoInicial").toDateTimeAtMidnight());
    	
    	if (criteria.getYearMonthDay("dtEmissaoFinal") != null)
    		hql.addCriteria("PSH.dhCriacao.value","<",criteria.getYearMonthDay("dtEmissaoFinal").plusDays(1).toDateTimeAtMidnight());
    	
    	if (criteria.getLong("idParamSimulacaoHistorica") != null) {
    		hql.addCriteria("PSH.id","=",criteria.getLong("idParamSimulacaoHistorica"));
    		return hql;
    	}
    	   
    	hql.addCriteria("FI.idFilial","=",criteria.getLong("filial.idFilial"));
    	hql.addCriteria("TMT.id","=",criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
    	hql.addCriteria("TTCE.id","=",criteria.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
    	hql.addCriteria("MTR.id","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
    	hql.addCriteria("PSH.blPercentual","=",criteria.getBoolean("blPercentual"));
    	
    	hql.addOrderBy("PSH.dsParamSimulacaoHistorica");
    	hql.addOrderBy("PSH.dhCriacao.value");
    	
    	return hql;
    }
    //3.4-0
    private SqlTemplate createFindQtdeDefault() {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("sum(NCP.qtNotaCreditoParcela)");
    	hql.addProjection("sum(NCP.qtNotaCreditoParcela)");
    	hql.addProjection("PTCE.tpParcela");
    	
    	hql.addGroupBy("PTCE.tpParcela");
    	return hql;
    }
    
    
    public void removeNcParcelaSimulacaoByIdParamSimulacao(Long idSimulacao) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("NPS");
    	hql.addFrom(new StringBuffer(NcParcelaSimulacao.class.getName()).append(" As NPS ")
    			.append("INNER JOIN NPS.paramSimulacaoHistorica AS PSH").toString());
    	hql.addCriteria("PSH.id","=",idSimulacao);
    	List rs = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    	for (Iterator i = rs.iterator(); i.hasNext();)
    		getAdsmHibernateTemplate().delete(i.next());
    	getAdsmHibernateTemplate().flush();
    }
    
    public List findParcelaTabelaCeByTpMeioTransTpTabColEnt(Long idTipoTabelaColetaEntrega, Long idTipoMeioTransporte, String tpParcela, YearMonthDay vigenteEm, Long idFilial) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ParcelaTabelaCe.class,"PTC")
    				.createAlias("tabelaColetaEntrega","TCE")
    				.createAlias("TCE.tipoMeioTransporte","TMT")
    				.createAlias("TCE.tipoTabelaColetaEntrega","TTCE")
    				.add(Restrictions.eq("TMT.id",idTipoMeioTransporte))
    				.add(Restrictions.eq("TCE.tpRegistro","A"))
    				.add(Restrictions.eq("TCE.filial.id",idFilial))
    				.add(Restrictions.eq("TTCE.id",idTipoTabelaColetaEntrega))
    				.add(Restrictions.and(Restrictions.le("TCE.dtVigenciaInicial",vigenteEm),Restrictions.or(Restrictions.isNull("TCE.dtVigenciaFinal"),Restrictions.ge("TCE.dtVigenciaFinal",vigenteEm))))
    				.add(Restrictions.eq("PTC.tpParcela",tpParcela));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    //3.4-1
    public Map findQtdeTpTabela(Long idTipoTabelaColetaEntrega, Long idFilial) {
    	SqlTemplate hql = createFindQtdeDefault();
    	hql.addFrom(new StringBuffer(NotaCreditoParcela.class.getName()).append(" AS NCP ")
    		.append("INNER JOIN NCP.parcelaTabelaCe AS PTCE ")
    		.append("INNER JOIN NCP.notaCredito AS NC ")
    		.append("INNER JOIN NC.filial AS FI ")
    		.append("INNER JOIN PTCE.tabelaColetaEntrega AS TCE ")
    		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ").toString());
    		
    	hql.addCriteria("FI.id","=",idFilial);
    	hql.addCriteria("TTCE.id","=",idTipoTabelaColetaEntrega);
    		
    	return executeReadResult(hql);
    }
    //3.4-2
    public Map findQtdeTpMeioTransp(Long idTipoMeioTransporte, Long idFilial, Long idTipoTabelaColetaEntrega) {
    	SqlTemplate hql = createFindQtdeDefault();
    	hql.addFrom(new StringBuffer(NotaCreditoParcela.class.getName()).append(" AS NCP ")
    		.append("INNER JOIN NCP.parcelaTabelaCe AS PTCE ")
    		.append("INNER JOIN NCP.notaCredito AS NC ")
    		.append("INNER JOIN NC.filial AS FI ")
    		.append("INNER JOIN NC.controleCargas AS CC ")
    		.append("INNER JOIN CC.meioTransporteByIdTransportado AS MT ")
    		.append("INNER JOIN MT.modeloMeioTransporte AS MMT ")
    		.append("INNER JOIN MMT.tipoMeioTransporte AS TMT ")
    		.append("INNER JOIN PTCE.tabelaColetaEntrega AS TCE ")
    		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
    		.toString());
    	
    	hql.addCriteria("TTCE.id","=",idTipoTabelaColetaEntrega);
    	hql.addCriteria("FI.id","=",idFilial);
    	hql.addCriteria("TMT.id","=",idTipoMeioTransporte);
    	hql.addCustomCriteria(new StringBuffer("CC.id = (SELECT max(CC2.id) FROM ")
    		.append(ControleCarga.class.getName()).append(" AS CC2 ")
    		.append("INNER JOIN CC2.notaCredito AS NC2 ")
    		.append("WHERE NC2.id = NC.id)").toString());
    		
    	return executeReadResult(hql);
    }
    //3.4-3
    public Map findQtdeIdentMeioTransp(Long idMeioTransporte, Long idFilial, Long idTipoTabelaColetaEntrega) {
    	SqlTemplate hql = createFindQtdeDefault();
    	hql.addFrom(new StringBuffer(NotaCreditoParcela.class.getName()).append(" AS NCP ")
    		.append("INNER JOIN NCP.parcelaTabelaCe AS PTCE ")
    		.append("INNER JOIN NCP.notaCredito AS NC ")
    		.append("INNER JOIN NC.filial AS FI ")
    		.append("INNER JOIN NC.controleCargas AS CC ")
    		.append("INNER JOIN CC.meioTransporteByIdTransportado AS MT ")
    		.append("INNER JOIN PTCE.tabelaColetaEntrega AS TCE ")
    		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
    		.toString());
    		
    	hql.addCriteria("TTCE.id","=",idTipoTabelaColetaEntrega);
    	hql.addCriteria("FI.id","=",idFilial);
    	hql.addCriteria("MT.id","=",idMeioTransporte);
    	hql.addCustomCriteria(new StringBuffer("CC.id = (SELECT max(CC2.id) FROM ")
    		.append(ControleCarga.class.getName()).append(" AS CC2 ")
    		.append("INNER JOIN CC2.notaCredito AS NC2 ")
    		.append("WHERE NC2.id = NC.id)").toString());
    		
    	return executeReadResult(hql);
    }
    //3.4-4
    public Map findQtdeNotasCredito(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idFilial, Long idTipoTabelaColetaEntrega) {
    	SqlTemplate hql = createFindQtdeDefault();
    	hql.addFrom(new StringBuffer(NotaCreditoParcela.class.getName()).append(" AS NCP ")
    		.append("INNER JOIN NCP.parcelaTabelaCe AS PTCE ")
    		.append("INNER JOIN NCP.notaCredito AS NC ")
    		.append("INNER JOIN NC.filial AS FI ")
    		.append("INNER JOIN PTCE.tabelaColetaEntrega AS TCE ")
    		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
    		.toString());
    		
    	hql.addCriteria("TTCE.id","=",idTipoTabelaColetaEntrega);
    	hql.addCriteria("FI.id","=",idFilial);
    	hql.addCriteria("NC.dhEmissao.value",">=",dtVigenciaInicial);
    	hql.addCriteria("NC.dhEmissao.value","<=",JTDateTimeUtils.maxYmd(dtVigenciaFinal));
 		
    	return executeReadResult(hql);
    }
    
    public Map findQtde(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idTipoMeioTransporte, Long idMeioTransporte, Long idTipoTabelaColetaEntrega) {
    	SqlTemplate hql = createFindQtdeDefault();
    	hql.addFrom(new StringBuffer(NotaCreditoParcela.class.getName()).append(" AS NCP ")
        		.append("INNER JOIN NCP.parcelaTabelaCe AS PTCE ")
        		.append("INNER JOIN NCP.notaCredito AS NC ")
        		.append("INNER JOIN NC.filial AS FI ")
        		.append("INNER JOIN NC.controleCargas AS CC ")
        		.append("INNER JOIN CC.meioTransporteByIdTransportado AS MT ")
        		.append("INNER JOIN MT.modeloMeioTransporte AS MMT ")
        		.append("INNER JOIN MMT.tipoMeioTransporte AS TMT ")
        		.append("INNER JOIN PTCE.tabelaColetaEntrega AS TCE ")
        		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
        		.toString());
    	
    	hql.addCriteria("TMT.id","=",idTipoMeioTransporte);
    	hql.addCustomCriteria(new StringBuffer("CC.id = (SELECT max(CC2.id) FROM ")
        		.append(ControleCarga.class.getName()).append(" AS CC2 ")
        		.append("INNER JOIN CC2.notaCredito AS NC2 ")
        		.append("WHERE NC2.id = NC.id)").toString());
    	
    	hql.addCriteria("MT.id","=",idMeioTransporte);
    	hql.addCriteria("TTCE.id","=",idTipoTabelaColetaEntrega);
    	hql.addCriteria("FI.id","=",idFilial);
    	hql.addCriteria("NC.dhEmissao.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(dtVigenciaInicial));
    	hql.addCriteria("NC.dhEmissao.value","<",JTDateTimeUtils.yearMonthDayToDateTime(JTDateTimeUtils.maxYmd(dtVigenciaFinal).plusDays(1)));

    	return executeReadResult(hql);
    }


    
    
    private Map executeReadResult(SqlTemplate hql) {
    	java.util.List rs = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    	HashMap result = new HashMap();
    	for (Iterator i = rs.iterator(); i.hasNext();) {
    		Object[] projections = (Object[])i.next();
    		result.put(new StringBuffer(((DomainValue)projections[2]).getValue()).append("_qt").toString(),projections[0]);
    		result.put(new StringBuffer(((DomainValue)projections[2]).getValue()).append("_vl").toString(),projections[1]);
    	}
    	return result;
    }
    
    
}