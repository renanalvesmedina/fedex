package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.HistoricoTrocaFilial;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoTrocaFilialDAO extends BaseCrudDao<HistoricoTrocaFilial, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HistoricoTrocaFilial.class;
    }

   
    public HistoricoTrocaFilial findByIdDetalhadoFilial(Long id){
    	
    	DetachedCriteria dc = createDetachedCriteria();
    	    	
    	ProjectionList projections = Projections.projectionList()
												.add(Projections.property("idHistoricoTrocaFilial"), "idHistoricoTrocaFilial")												
												.add(Projections.property("mfTroca.idMunicipioFilial"), "municipioFilialByIdMunicipioFilialTroca.idMunicipioFilial")
												.add(Projections.property("mfTroca.dtVigenciaInicial"), "municipioFilialByIdMunicipioFilialTroca.dtVigenciaInicial")
												.add(Projections.property("filialTroca.idFilial"), "municipioFilialByIdMunicipioFilialTroca.filial.idFilial");
    	
    	dc.setProjection(projections);
		dc.createAlias("municipioFilialByIdMunicipioFilialTroca", "mfTroca");
    	dc.createAlias("mfTroca.filial", "filialTroca");
    	
    	dc.add(Restrictions.idEq(id));
    	
    	dc.setResultTransformer(new AliasToNestedBeanResultTransformer(HistoricoTrocaFilial.class));
    	    	   	
    	return (HistoricoTrocaFilial) findByDetachedCriteria(dc).get(0);
    }
    
    /**
     * 
     * @param idHistoricoTrocaFilial
     * @return
     */
    public Map findDadosHistoricoTrocaFilial(Long idHistoricoTrocaFilial){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map(m.nmMunicipio", "nmMunicipio");
    	sql.addProjection("uf.sgUnidadeFederativa", "sgUnidadeFederativa");
    	sql.addProjection("uf.nmUnidadeFederativa", "nmUnidadeFederativa");
    	sql.addProjection("p.nmPais", "nmPais");
    	sql.addProjection("mf.idMunicipioFilial", "idMunicipioFilial");    	
    	sql.addProjection("filialTroca.sgFilial", "sgFilialTroca");
    	sql.addProjection("filialTroca.idFilial", "idFilialTroca");
    	sql.addProjection("pfTroca.nmFantasia", "nmFantasiaTroca");
    	sql.addProjection("mfTroca.blPadraoMcd", "blPadraoMcd");
    	sql.addProjection("mfTroca.nrDistanciaAsfalto", "nrDistanciaAsfalto");
    	sql.addProjection("mfTroca.nrDistanciaChao", "nrDistanciaChao");
    	sql.addProjection("mfTroca.nrGrauDificuldade", "nrGrauDificuldade");
    	sql.addProjection("mfTroca.blRestricaoAtendimento", "blRestricaoAtendimento");
    	sql.addProjection("mfTroca.blRecebeColetaEventual", "blRecebeColetaEventual");
    	sql.addProjection("mfTroca.dtVigenciaInicial", "dtVigenciaInicial");
    	sql.addProjection("mfTroca.dtVigenciaFinal", "dtVigenciaFinal)");
    	
    	sql.addInnerJoin(HistoricoTrocaFilial.class.getName(), "htf");
    	sql.addInnerJoin("htf.municipioFilialByIdMunicipioFilial", "mf");
    	sql.addInnerJoin("htf.municipioFilialByIdMunicipioFilialTroca", "mfTroca");
    	sql.addInnerJoin("mf.municipio", "m");
    	sql.addInnerJoin("m.unidadeFederativa", "uf");
    	sql.addInnerJoin("uf.pais", "p");    	
    	sql.addInnerJoin("mfTroca.filial", "filialTroca");
    	sql.addInnerJoin("filialTroca.pessoa", "pfTroca");    	
    	
    	sql.addCriteria("htf.id", "=", idHistoricoTrocaFilial);
    	
    	List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	return (Map) (result.isEmpty() ?  null : result.get(0));
    }
    
    public List findHistoricoTrocaFilialDataAtual(YearMonthDay dtInclusao){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("htf.idHistoricoTrocaFilial");
    	sql.addFrom(HistoricoTrocaFilial.class.getName(), "htf");
    	sql.addCriteria("htf.dtInclusao","=",dtInclusao);
    	
    	List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	return (List) (result.isEmpty() ?  null : result);
    	
    }
}