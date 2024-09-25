 package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.AplicaReajusteRota;
import com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota;
import com.mercurio.lms.fretecarreteiroviagem.model.service.SimulacaoReajusteRotaService;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.MeioTransporteRotaViagem;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SimulacaoReajusteRotaDAO extends BaseCrudDao<SimulacaoReajusteRota, Long>
{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SimulacaoReajusteRota.class;
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	SqlTemplate hql = createHQLGrid(criteria);
    	hql.addProjection("new Map(SRR.idSimulacaoReajusteRota","idSimulacaoReajusteRota");
    	hql.addProjection("SRR.dsSimulacaoReajusteRota","dsSimulacaoReajusteRota");
    	hql.addProjection("SRR.tpSituacaoRota","tpSituacaoRota");
    	hql.addProjection("SRR.tpRota","tpRota");
    	hql.addProjection("TMT.dsTipoMeioTransporte","tipoMeioTransporte_dsTipoMeioTransporte");
    	hql.addProjection("MT.nrFrota","meioTransporte_nrFrota");
    	hql.addProjection("MT.nrIdentificador","meioTransporte_nrIdentificador");
    	hql.addProjection("PP.nrIdentificacao","proprietario_nrIdentificacao");
    	hql.addProjection("PP.tpIdentificacao","proprietario_tpIdentificacao");
    	hql.addProjection("PP.nmPessoa","proprietario_nmPessoa");
    	hql.addProjection("M.sgMoeda || ' ' || M.dsSimbolo","moedaPais_dsMoeda");
    	hql.addProjection("SRR.tpReajuste","tpReajuste");
    	hql.addProjection("SRR.vlReajuste","vlReajuste");
    	hql.addProjection("SRR.dtVigenciaInicial","dtVigenciaInicial");
    	hql.addProjection("SRR.dtVigenciaFinal","dtVigenciaFinal)");
    	
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
    }

    
    public void deleteFilhos(Long idSimulacao) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(AplicaReajusteRota.class.getName(),"ARR");
    	sql.addCriteria("ARR.simulacaoReajusteRota.id","=",idSimulacao);
    	List result = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	if (!result.isEmpty())
    		getAdsmHibernateTemplate().deleteAll(result);
    	getAdsmHibernateTemplate().flush();
   }
   
   public Map findByIdCustom(Long idSimulacao) {
	   SqlTemplate hql = new SqlTemplate();
	   hql.addProjection("new map(SRR.dsSimulacaoReajusteRota","dsSimulacaoReajusteRota");
	   hql.addProjection("SRR.idSimulacaoReajusteRota","idSimulacaoReajusteRota");
	   hql.addProjection("SRR.tpSituacaoRota","tpSituacaoRota");
	   hql.addProjection("SRR.tpReajuste","tpReajuste");
	   hql.addProjection("SRR.vlReajuste","vlReajuste");
	   hql.addProjection("SRR.dtVigenciaInicial","dtVigenciaInicial");
	   hql.addProjection("SRR.tpRota","tpRota");
	   hql.addProjection("SRR.dtVigenciaFinal","dtVigenciaFinal");
	   hql.addProjection("PA.id","moedaPais_pais_idPais");
	   hql.addProjection("PA.nmPais","moedaPais_pais_nmPais");
	   hql.addProjection("MP.idMoedaPais","moedaPais_idMoedaPais");
	   
	   hql.addProjection("RO.id","regionalOrigem_idRegional");
	   hql.addProjection("RD.id","regionalDestino_idRegional");
	   
	   hql.addProjection("FO.id","filialOrigem_idFilial");
	   hql.addProjection("FO.sgFilial","filialOrigem_sgFilial");
	   hql.addProjection("PFO.nmFantasia","filialOrigem_pessoa_nmFantasia");
	   
	   hql.addProjection("FD.id","filialDestino_idFilial");
	   hql.addProjection("FD.sgFilial","filialDestino_sgFilial");
	   hql.addProjection("PFD.nmFantasia","filialDestino_pessoa_nmFantasia");
	   
	   hql.addProjection("TMT.id","tipoMeioTransporte_idTipoMeioTransporte");
	   
	   hql.addProjection("MT.id","meioTransporte2_idMeioTransporte");
	   hql.addProjection("MT.nrFrota","meioTransporte2_nrFrota");
	   hql.addProjection("MT.nrIdentificador","meioTransporte_nrIdentificador");
	   hql.addProjection("MT.id","meioTransporte_idMeioTransporte");
	   
	   hql.addProjection("P.id","proprietario_idProprietario");
	   hql.addProjection("PP.nrIdentificacao","proprietario_pessoa_nrIdentificacao");
	   hql.addProjection("PP.tpIdentificacao","proprietario_pessoa_tpIdentificacao");
	   hql.addProjection("PP.nmPessoa","proprietario_pessoa_nmPessoa)");
	   
	   hql.addFrom(new StringBuffer(SimulacaoReajusteRota.class.getName()).append(" AS SRR ")
	    		.append("LEFT JOIN SRR.tipoMeioTransporte TMT ")
	    		.append("LEFT JOIN SRR.meioTransporte MT ")
	    		.append("LEFT JOIN SRR.proprietario P ")
	    		.append("LEFT JOIN SRR.regionalOrigem RO ")
	    		.append("LEFT JOIN SRR.regionalDestino RD ")
	    		.append("LEFT JOIN SRR.filialOrigem FO ")
	    		.append("LEFT JOIN SRR.filialDestino FD ")
	    		.append("LEFT JOIN FO.pessoa PFO ")
	    		.append("LEFT JOIN FD.pessoa PFD ")
	    		.append("LEFT JOIN P.pessoa PP ")
	    		.append("LEFT JOIN SRR.moedaPais MP ")
	    		.append("LEFT JOIN MP.pais PA ").toString());
	   hql.addCriteria("SRR.id","=",idSimulacao);
	   
	   return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
   }
   public List findRotasBySimulacao(Long idSimulacao) {
	   SqlTemplate sql = new SqlTemplate();
	   sql.addProjection("new Map(RIV.idRotaIdaVolta","rotaIdaVolta_idRotaIdaVolta");
	   sql.addProjection("ARR.blAplicacao","blAplicacao");
	   sql.addProjection("RIV.nrRota","rotaIdaVolta_nrRota");
	   sql.addProjection("R.dsRota","rotaViagem_dsRota)");
	   
   	   sql.addFrom(new StringBuffer(AplicaReajusteRota.class.getName()).append(" ARR ")
   	   		.append("INNER JOIN ARR.rotaIdaVolta RIV ")
   	   		.append("INNER JOIN RIV.rota R ").toString());
   	   sql.addCriteria("ARR.simulacaoReajusteRota.id","=",idSimulacao);

   	   return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
   }
   public SqlTemplate createHQLGrid(TypedFlatMap criteria) {
    	SqlTemplate hql = new SqlTemplate();
    
    	hql.addFrom(new StringBuffer(SimulacaoReajusteRota.class.getName()).append(" AS SRR ")
    		.append("LEFT JOIN SRR.tipoMeioTransporte TMT ")
    		.append("LEFT JOIN SRR.meioTransporte MT ")
    		.append("LEFT JOIN SRR.proprietario P ")
    		.append("LEFT JOIN P.pessoa PP ")
    		.append("LEFT JOIN SRR.moedaPais MP ")
    		.append("LEFT JOIN MP.moeda M ").toString());
    		
    	hql.addCriteria("SRR.regionalOrigem.id","=",criteria.getLong("regionalOrigem.idRegional"));
    	hql.addCriteria("SRR.filialOrigem.id","=",criteria.getLong("filialOrigem.idFilial"));
    	hql.addCriteria("SRR.regionalDestino.id","=",criteria.getLong("regionalDestino.idRegional"));
    	hql.addCriteria("SRR.filialDestino.id","=",criteria.getLong("filialDestino.idFilial"));

    	hql.addCriteria("TMT.id","=",criteria.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
    	hql.addCriteria("MT.id","=",criteria.getLong("meioTransporte.idMeioTransporte"));
    	hql.addCriteria("P.id","=",criteria.getLong("proprietario.idProprietario"));
    	
    	hql.addCriteria("SRR.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
    	hql.addCriteria("SRR.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));
    	
    	if (StringUtils.isNotBlank(criteria.getString("tpSituacaoRota")))
    		hql.addCriteria("SRR.tpSituacaoRota","=",criteria.getString("tpSituacaoRota"));
    	
    	if (StringUtils.isNotBlank(criteria.getString("tpRota"))) {
    		hql.addCriteria("SRR.tpRota","=",criteria.getString("tpRota"));
	    }
    	if (criteria.getLong("rotaIdaVolta.idRotaIdaVolta") != null) { 
    		hql.addCustomCriteria(new StringBuilder("EXISTS( SELECT ARR.id FROM ").append(AplicaReajusteRota.class.getName()).append(" AS ARR ")
    	    		.append("WHERE ARR.rotaIdaVolta.id = ? AND ARR.simulacaoReajusteRota.id = SRR.id)").toString());
    	    hql.addCriteriaValue(criteria.getLong("rotaIdaVolta.idRotaIdaVolta"));
    	}
    	
    	if (StringUtils.isNotBlank(criteria.getString("dsSimulacaoReajusteRota")))
    		hql.addCustomCriteria("upper(SRR.dsSimulacaoReajusteRota) like upper('" + criteria.getString("dsSimulacaoReajusteRota") + "')");
    	
    	
    	hql.addOrderBy("SRR.tpRota");
    	hql.addOrderBy("SRR.dtVigenciaInicial","DESC");
    	return hql;
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate hql = createHQLGrid(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());
    }
    public SqlTemplate createHQLRotasByCriteria(Long idRegionalOrigem, Long idFilialOrigem, Long idRegionalDestino, Long idFilialDestino,
    		String tpRota, Long idTipoMeioTransporte, Long idMoedaPais, Long idMeioTransporte, Long idProprietario, List idsRotaIdaVolta, Boolean blAplica, YearMonthDay dtVigencia, int opcaoVigencia,Boolean addFetchs) {
    	
    	if (dtVigencia == null)
    		throw new IllegalArgumentException("dtVigencia não pode ser nulla");
    	if (idsRotaIdaVolta != null && !idsRotaIdaVolta.isEmpty() && blAplica == null)
    		throw new IllegalArgumentException("Se informado o campo idsRotaIdaVolta deve tambem informar o blAplica");

    	SqlTemplate hql = new SqlTemplate();
    	if (addFetchs != null && addFetchs.booleanValue())
	    	hql.addFrom(new StringBuffer(RotaIdaVolta.class.getName()).append(" RIV ")
	    		.append("INNER JOIN FETCH RIV.rota R ")
	    		.append("INNER JOIN FETCH RIV.rotaViagem RV ").toString());
    	
    	hql.addCriteria("RV.tpRota","=",tpRota);
    	hql.addCriteria("RIV.moedaPais.id","=",idMoedaPais);
    	hql.addCriteria("RV.tipoMeioTransporte.id","=",idTipoMeioTransporte);
    	
    	if (opcaoVigencia == SimulacaoReajusteRotaService.VIGENTE_DESPOIS_DE)
    		hql.addCriteria("RV.dtVigenciaInicial",">=",dtVigencia);
    	else{
    		hql.addCustomCriteria("(RV.dtVigenciaInicial < ? AND RV.dtVigenciaFinal > ?)");
    		hql.addCriteriaValue(dtVigencia);
    		hql.addCriteriaValue(dtVigencia);
    	}
    	
    	if (idsRotaIdaVolta != null && !idsRotaIdaVolta.isEmpty()) {
    		StringBuffer customCriteria = new StringBuffer("RIV.nrRota ");
    		if (!blAplica.booleanValue())
    			customCriteria.append(" NOT ");
    		customCriteria.append(" IN( ");
    		String token = "";
    		for(Iterator i = idsRotaIdaVolta.iterator(); i.hasNext();) {
    			customCriteria.append(token).append(" ?");
    			hql.addCriteriaValue(i.next());
    			if (StringUtils.isBlank(token))
    				token = ",";
    		}
    		hql.addCustomCriteria(customCriteria.append(")").toString());
    	}
    	
    	if (idMeioTransporte != null) {
    		hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT MTRV_S.id FROM ").append(MeioTransporteRotaViagem.class.getName()).append(" AS MTRV_S ")
    				.append("INNER JOIN MTRV_S.meioTransporteRodoviario MTR_MTRV_S ")
    			.append("WHERE MTR_MTRV_S.meioTransporte.id = ? AND MTRV_S.rotaViagem.id = RV.id AND MTRV_S.dtVigenciaInicial <= ? AND MTRV_S.dtVigenciaFinal >= ?)").toString());
    		hql.addCriteriaValue(idMeioTransporte);
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    	}
    	if (idProprietario != null) {
    		hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT MTRV_S.id FROM ").append(MeioTransporteRotaViagem.class.getName()).append(" AS MTRV_S ")
    				.append("INNER JOIN MTRV_S.meioTransporteRodoviario MTR_MTRV_S ")
    				.append("INNER JOIN MTR_MTRV_S.meioTransporte MT_MTRV_S ")
    				.append("INNER JOIN MT_MTRV_S.meioTranspProprietarios MTP_MTRV_S ")
    				.append("WHERE MTP_MTRV_S.proprietario.id = ? AND MTRV_S.rotaViagem.id = RV.id ")
    				.append("AND MTRV_S.dtVigenciaInicial <= ? AND MTRV_S.dtVigenciaFinal >= ? AND MTP_MTRV_S.dtVigenciaInicial <= ? AND MTP_MTRV_S.dtVigenciaFinal >= ?)").toString());
    		hql.addCriteriaValue(idProprietario);
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    	}
    	if (idRegionalDestino != null) {
    		hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT FR.id FROM ").append(FilialRota.class.getName()).append(" AS FR ")
    				.append("INNER JOIN FR.filial F_FR ")
    				.append("INNER JOIN F_FR.regionalFiliais RF_FR ")
    				.append("WHERE RF_FR.regional.id = ?  AND FR.rota.id = R.id AND FR.blDestinoRota = ? ")
    				.append("AND RF_FR.dtVigenciaInicial <= ? AND RF_FR.dtVigenciaFinal >= ?)").toString());
    		hql.addCriteriaValue(idRegionalDestino);
    		hql.addCriteriaValue(Boolean.TRUE);
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    	}
    	if (idRegionalOrigem != null) {
    		hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT FR.id FROM ").append(FilialRota.class.getName()).append(" AS FR ")
    				.append("INNER JOIN FR.filial F_FR ")
    				.append("INNER JOIN F_FR.regionalFiliais RF_FR ")
    				.append("WHERE RF_FR.regional.id = ? AND FR.rota.id = R.id AND FR.blOrigemRota = ? ")
    				.append("AND RF_FR.dtVigenciaInicial <= ? AND RF_FR.dtVigenciaFinal >= ?)").toString());
    		hql.addCriteriaValue(idRegionalOrigem);
    		hql.addCriteriaValue(Boolean.TRUE);
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    		hql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
    	}
    	if (idFilialDestino != null) {
    		hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT FR.id FROM ").append(FilialRota.class.getName()).append(" AS FR ")
    				.append("WHERE FR.filial.id = ? AND FR.rota.id = R.id AND FR.blDestinoRota = ? )").toString());
    		hql.addCriteriaValue(idFilialDestino);
    		hql.addCriteriaValue(Boolean.TRUE);
    		
    	}
    	if (idFilialOrigem != null) {
    		hql.addCustomCriteria(new StringBuffer("EXISTS (SELECT FR.id FROM ").append(FilialRota.class.getName()).append(" AS FR ")
    				.append("WHERE FR.filial.id = ? AND FR.rota.id = R.id AND FR.blOrigemRota = ? )").toString());
    		hql.addCriteriaValue(idFilialOrigem);
    		hql.addCriteriaValue(Boolean.TRUE);
    	}
    	if (addFetchs != null && addFetchs.booleanValue())
    		hql.addOrderBy("RV.id");
    	return hql;

    }
    		

    public Long findFilialOrigem(Long idRotaIdaVolta) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("F.id");
    	hql.addFrom(new StringBuffer(RotaIdaVolta.class.getName()).append(" RIV ")
    		.append("INNER JOIN RIV.rota R ")
    		.append("INNER JOIN R.filialRotas FR ")
    		.append("INNER JOIN FR.filial F ").toString());
    	hql.addCriteria("RIV.id","=",idRotaIdaVolta);
    	hql.addCriteria("FR.blOrigemRota","=",Boolean.TRUE);
    	return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
    }
    public List findRotasByCriterias(Long idRegionalOrigem, Long idFilialOrigem, Long idRegionalDestino, Long idFilialDestino,
    		String tpRota, Long idTipoMeioTransporte, Long idMoedaPais, Long idMeioTransporte, Long idProprietario, List idsRotaIdaVolta, Boolean blAplica, YearMonthDay dtVigencia, int opcaoVigencia) {
    		
    	SqlTemplate hql = createHQLRotasByCriteria(idRegionalOrigem, idFilialOrigem, idRegionalDestino, idFilialDestino, tpRota, idTipoMeioTransporte, idMoedaPais, idMeioTransporte, idProprietario, idsRotaIdaVolta, blAplica, dtVigencia, opcaoVigencia, Boolean.TRUE);
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    
    public List findRotasByCriteriasVigenciaMaior(Long idRegionalOrigem, Long idFilialOrigem, Long idRegionalDestino, Long idFilialDestino,
    		String tpRota, Long idTipoMeioTransporte, Long idMoedaPais, Long idMeioTransporte, Long idProprietario, List idsRotaIdaVolta, Boolean blAplica, YearMonthDay dtVigencia) {
    	int opcaoVigencia = SimulacaoReajusteRotaService.VIGENTE_DESPOIS_DE;
    	SqlTemplate hql = createHQLRotasByCriteria(idRegionalOrigem, idFilialOrigem, idRegionalDestino, idFilialDestino, tpRota, idTipoMeioTransporte, idMoedaPais, idMeioTransporte, idProprietario, idsRotaIdaVolta, blAplica, dtVigencia, opcaoVigencia, Boolean.FALSE);
    	
    	
    	hql.addFrom(new StringBuffer(RotaIdaVolta.class.getName()).append(" RIV ")
	    		.append("INNER JOIN RIV.rota R ")
	    		.append("INNER JOIN RIV.rotaViagem RV ").toString());
    	
    	hql.addProjection("DISTINCT RIV.nrRota");
    	hql.addProjection("R.dsRota");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());

    }
}