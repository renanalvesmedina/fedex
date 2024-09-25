package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ValorCruze;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorCruzeDAO extends BaseCrudDao<ValorCruze, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ValorCruze.class;
    }

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition findDef) {
    	SqlTemplate sql = getSqlTemplate(criteria);
    	
		sql.addProjection("new Map(" + 
				" vlCruze.idValorCruze as idValorCruze, " +
                " vlCruze.nrFaixaInicialPeso as nrFaixaInicialPeso, " +
                " vlCruze.nrFaixaFinalPeso as nrFaixaFinalPeso, " +
                " moeda.dsSimbolo as dsSimbolo, " +
                " moeda.sgMoeda as sgMoeda, " +
                " vlCruze.vlCruze as vlCruze, " +
                " vlCruze.dtVigenciaInicial as dtVigenciaInicial, " +
                " vlCruze.dtVigenciaFinal as dtVigenciaFinal " +
                ")");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}    

	public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate sql = getSqlTemplate(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

    private SqlTemplate getSqlTemplate(TypedFlatMap criteria){ 

    	SqlTemplate sql = new SqlTemplate();
    	StringBuffer sb = new StringBuffer();
    	
	        sb.append(new StringBuffer(getPersistentClass().getName()).append(" AS vlCruze ")
    		  .append("INNER JOIN FETCH vlCruze.moeda AS moeda ")
    		  .append("JOIN vlCruze.empresa AS empresa "));
  
    	sql.addFrom(sb.toString());
    	
    	YearMonthDay dataReferencia = criteria.getYearMonthDay("dataReferencia");
    	if(dataReferencia != null) {
    		sql.addCriteria("vlCruze.dtVigenciaInicial","<=",dataReferencia); 
    		sql.addCriteria("vlCruze.dtVigenciaFinal",">=",dataReferencia);
    	}
    	
    	sql.addCriteria("empresa.idEmpresa","=",criteria.getLong("idEmpresa"));
    	
    	sql.addOrderBy("vlCruze.nrFaixaInicialPeso");
    	sql.addOrderBy("vlCruze.dtVigenciaInicial");
    	
    	return sql; 
	}

    
    public ValorCruze findById(Long id) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vlCruze");
    	dc.createAlias("vlCruze.moeda", "moeda");
    	
		ProjectionList projections = Projections.projectionList()
		.add(Projections.alias(Projections.property("vlCruze.idValorCruze"),"idValorCruze"))
		.add(Projections.alias(Projections.property("vlCruze.nrFaixaInicialPeso"),"nrFaixaInicialPeso"))
		.add(Projections.alias(Projections.property("vlCruze.nrFaixaFinalPeso"),"nrFaixaFinalPeso"))
		.add(Projections.alias(Projections.property("moeda.idMoeda"),"moeda.idMoeda"))
		.add(Projections.alias(Projections.property("vlCruze.vlCruze"),"vlCruze"))
		.add(Projections.alias(Projections.property("vlCruze.dtVigenciaInicial"),"dtVigenciaInicial"))
		.add(Projections.alias(Projections.property("vlCruze.dtVigenciaFinal"),"dtVigenciaFinal"));

		dc.setProjection(projections);
		dc.add(Restrictions.eq("vlCruze.idValorCruze", id));	
		
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

		return (ValorCruze)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Retorna a lista de ValorCruze que ocorre sobreposição de faixas de peso da base com as faixas de peso do pojo. 
	 * 
	 * @param ValorCruze
	 * @return List
	 * */

    public List findSobreposicaoByFaixaPeso(ValorCruze valorCruze) {
    	YearMonthDay data = valorCruze.getDtVigenciaFinal(); 
    	if(data == null) {
    		data = JTDateTimeUtils.MAX_YEARMONTHDAY;
    	}
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vlCruze");
    	dc.createAlias("vlCruze.empresa", "empresa");
    	
		ProjectionList projections = Projections.projectionList()
		.add(Projections.alias(Projections.property("vlCruze.idValorCruze"),"idValorCruze"))
		.add(Projections.alias(Projections.property("vlCruze.nrFaixaInicialPeso"),"nrFaixaInicialPeso"))
		.add(Projections.alias(Projections.property("vlCruze.nrFaixaFinalPeso"),"nrFaixaFinalPeso"));

		dc.setProjection(projections);

		//idEmpresa Igual
		dc.add(Restrictions.eq("empresa.idEmpresa",valorCruze.getEmpresa().getIdEmpresa()));
		
		if(valorCruze.getIdValorCruze() != null) {
			dc.add(Restrictions.ne("vlCruze.idValorCruze",valorCruze.getIdValorCruze()));
		}

		//Não valida apenas os vigentes.
		dc.add(Restrictions.or(
				Restrictions.or(
					Restrictions.and(
						Restrictions.le("vlCruze.dtVigenciaInicial", valorCruze.getDtVigenciaInicial()), 
						Restrictions.ge("vlCruze.dtVigenciaFinal",  valorCruze.getDtVigenciaInicial()))
					,
					Restrictions.and(
							Restrictions.le("vlCruze.dtVigenciaInicial", data), 
							Restrictions.ge("vlCruze.dtVigenciaFinal",  data))
			    ),
			    Restrictions.and(
						Restrictions.ge("vlCruze.dtVigenciaInicial", valorCruze.getDtVigenciaInicial()), 
						Restrictions.le("vlCruze.dtVigenciaFinal",  data))
			   )
		
		);

		dc.add(Restrictions.or(
				Restrictions.or(
					Restrictions.and(
						Restrictions.le("vlCruze.nrFaixaInicialPeso", valorCruze.getNrFaixaInicialPeso()), 
						Restrictions.ge("vlCruze.nrFaixaFinalPeso",  valorCruze.getNrFaixaInicialPeso()))
					,
					Restrictions.and(
							Restrictions.le("vlCruze.nrFaixaInicialPeso", valorCruze.getNrFaixaFinalPeso()), 
							Restrictions.ge("vlCruze.nrFaixaFinalPeso",  valorCruze.getNrFaixaFinalPeso()))
			    ),
			    Restrictions.and(
						Restrictions.ge("vlCruze.nrFaixaInicialPeso", valorCruze.getNrFaixaInicialPeso()), 
						Restrictions.le("vlCruze.nrFaixaFinalPeso",  valorCruze.getNrFaixaFinalPeso()))
			   )
		
		);
		
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public List findByVigencia(YearMonthDay dtVigencia){

    	ProjectionList pl = Projections.projectionList()
    	.add(Projections.property("idValorCruze"),"idValorCruze")
    	.add(Projections.property("vlCruze"),"vlCruze")
    	.add(Projections.property("nrFaixaInicialPeso"),"nrFaixaInicialPeso")
    	.add(Projections.property("nrFaixaFinalPeso"),"nrFaixaFinalPeso")
    	;

    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vc")
    	.add(Restrictions.le("vc.dtVigenciaInicial", dtVigencia))
    	.add(Restrictions.ge("vc.dtVigenciaFinal", dtVigencia))
    	.setProjection(pl)
    	.setResultTransformer(new AliasToBeanResultTransformer(getPersistentClass()))
    	;

    	return findByDetachedCriteria(dc);
    }
}