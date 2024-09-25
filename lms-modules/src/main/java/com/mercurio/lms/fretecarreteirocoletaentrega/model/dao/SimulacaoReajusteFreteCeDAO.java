package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SimulacaoReajusteFreteCeDAO extends BaseCrudDao<SimulacaoReajusteFreteCe, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SimulacaoReajusteFreteCe.class;
    }

    protected void initFindListLazyProperties(Map fetchModes) {
    	fetchModes.put("tipoMeioTransporte",FetchMode.JOIN);
    	fetchModes.put("tipoTabelaColetaEntrega",FetchMode.JOIN);
    	fetchModes.put("filialReajustes",FetchMode.SELECT);
    	fetchModes.put("filialReajustes.filial",FetchMode.SELECT);
    	fetchModes.put("filialReajustes.filial.pessoa",FetchMode.SELECT);
    	fetchModes.put("parcelaReajustes",FetchMode.JOIN);
    	super.initFindListLazyProperties(fetchModes);
    }
   

    
    public List findViewParameters(TypedFlatMap parameters) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("distinct F.sgFilial");
    	hql.addProjection("P.nmFantasia");
    	
    	hql.addFrom(new StringBuffer(TabelaColetaEntrega.class.getName()).append(" AS TCE ")
    		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
    		.append("INNER JOIN TCE.tipoMeioTransporte AS TMT ")
    		.append("INNER JOIN TCE.filial AS F ")
    		.append("INNER JOIN TCE.moedaPais AS MP ")
    		.append("INNER JOIN F.pessoa AS P").toString());
    	
    	
    	hql.addCriteria("TTCE.id","=",parameters.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
		hql.addCriteria("TMT.id","=",parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		hql.addCriteria("MP.id","=",parameters.getLong("moedaPais.idMoedaPais"));
		
		hql.addCriteria("TCE.dtVigenciaInicial",">=",parameters.getYearMonthDay("dtEmissaoInicial"));
		hql.addCriteria("TCE.dtVigenciaInicial","<=",parameters.getYearMonthDay("dtEmissaoFinal"));
		

		List filiaisT = parameters.getList("filiaisT");
		List filiaisF = parameters.getList("filiaisF");
		
		if (filiaisT != null && filiaisT.size() > 0) {
			
			TypedFlatMap filial = (TypedFlatMap)filiaisT.get(0);
			StringBuffer criterias = new StringBuffer("?");
			hql.addCriteriaValue(filial.getLong("filial.idFilial"));

			for(int i = 1; i < filiaisT.size(); i++) {
				filial = (TypedFlatMap)filiaisT.get(i);
				criterias.append(",?");
				hql.addCriteriaValue(filial.getLong("filial.idFilial"));
			}
			hql.addCustomCriteria(new StringBuffer("F.idFilial IN (").append(criterias).append(")").toString());
		}
		
		if (filiaisF != null && filiaisF.size() > 0) {
			TypedFlatMap filial = (TypedFlatMap)filiaisF.get(0);
			StringBuffer criterias = new StringBuffer("?");
			hql.addCriteriaValue(filial.getLong("filial.idFilial"));

			for(int i = 1; i < filiaisF.size(); i++) {
				filial = (TypedFlatMap)filiaisF.get(i);
				criterias.append(",?");
				hql.addCriteriaValue(filial.getLong("filial.idFilial"));
			}
			hql.addCustomCriteria(new StringBuffer("F.idFilial NOT IN (").append(criterias).append(")").toString());
		}
    	
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    
    
    public List findEfetivarReajuste(TypedFlatMap parameters) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("TCE");
    	
    	hql.addFrom(new StringBuffer(TabelaColetaEntrega.class.getName()).append(" AS TCE ")
    		.append("INNER JOIN TCE.tipoTabelaColetaEntrega AS TTCE ")
    		.append("INNER JOIN TCE.tipoMeioTransporte AS TMT ")
    		.append("INNER JOIN TCE.filial AS F ")
    		.append("INNER JOIN TCE.moedaPais AS MP ")
    		.append("INNER JOIN F.pessoa AS P").toString());
    	
    	hql.addCriteria("TTCE.id","=",parameters.getLong("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"));
		hql.addCriteria("TMT.id","=",parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
		hql.addCriteria("MP.id","=",parameters.getLong("moedaPais.idMoedaPais"));
		
    	
		hql.addCriteria("TCE.dtVigenciaInicial","<=",parameters.getYearMonthDay("dtEmissaoInicial"));
		hql.addCriteria("TCE.dtVigenciaFinal",">=",parameters.getYearMonthDay("dtEmissaoInicial"));
		

		List filiaisT = parameters.getList("filiaisT");
		List filiaisF = parameters.getList("filiaisF");
		
		if (filiaisT != null && filiaisT.size() > 0) {
			
			TypedFlatMap filial = (TypedFlatMap)filiaisT.get(0);
			StringBuffer criterias = new StringBuffer("?");
			hql.addCriteriaValue(filial.getLong("filial.idFilial"));

			for(int i = 1; i < filiaisT.size(); i++) {
				filial = (TypedFlatMap)filiaisT.get(i);
				criterias.append(",?");
				hql.addCriteriaValue(filial.getLong("filial.idFilial"));
			}
			hql.addCustomCriteria(new StringBuffer("F.idFilial IN (").append(criterias).append(")").toString());
		}
		
		if (filiaisF != null && filiaisF.size() > 0) {
			TypedFlatMap filial = (TypedFlatMap)filiaisF.get(0);
			StringBuffer criterias = new StringBuffer("?");
			hql.addCriteriaValue(filial.getLong("filial.idFilial"));

			for(int i = 1; i < filiaisF.size(); i++) {
				filial = (TypedFlatMap)filiaisF.get(i);
				criterias.append(",?");
				hql.addCriteriaValue(filial.getLong("filial.idFilial"));
			}
			hql.addCustomCriteria(new StringBuffer("F.idFilial NOT IN (").append(criterias).append(")").toString());
		}
    	
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }

}