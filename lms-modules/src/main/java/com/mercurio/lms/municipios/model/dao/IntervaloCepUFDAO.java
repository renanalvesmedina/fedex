package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureRestrictionsBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.lms.municipios.model.IntervaloCepUF;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IntervaloCepUFDAO extends BaseCrudDao<IntervaloCepUF, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return IntervaloCepUF.class;
	}

	private ConfigureRestrictionsBuilder rbIntervaloCepUf;
	
	protected void initDao() throws Exception {
		rbIntervaloCepUf = new ConfigureRestrictionsBuilder() {
			public void configure(RestrictionsBuilder rb) {

				List campos = new ArrayList(2);
				campos.add("nrCepInicial");
				campos.add("nrCepFinal");

				//Implementação do RestrictionBuilder customizado para tratar o filtro nos campos indicados
				RestrictionBuilder restrict = new RestrictionBuilder() {
					public void buildRestriction(DetachedCriteria dc, String ownerProperty, Map values) {
						if (ownerProperty.equalsIgnoreCase("")) {
							if (values.get("nrCepInicial") != null && !"".equals(values.get("nrCepInicial"))) {
								String nrCepInicial = (String)values.get("nrCepInicial");
								dc.add(Restrictions.ge("nrCepInicial",nrCepInicial.substring(0,nrCepInicial.length()-1)));
							}
							if (values.get("nrCepFinal") != null && !"".equals(values.get("nrCepFinal"))){
								String nrCepFinal = (String)values.get("nrCepFinal");
								dc.add(Restrictions.le("nrCepFinal",nrCepFinal.substring(0,nrCepFinal.length()-1)));
							}
						}
					}
				};
				rb.addCustomRestrictionBuilder(campos, restrict);
			}
		};
		super.initDao();
	}

	public ResultSetPage findPaginatedCustom(Map criteria) {
		RestrictionsBuilder restrictions = new RestrictionsBuilder(IntervaloCepUF.class, false);
		return super.findPaginated(criteria, FindDefinition.createFindDefinition(criteria), restrictions, rbIntervaloCepUf);
	}
	
	public Integer getRowCountCustom(Map criteria) {
		RestrictionsBuilder restrictions = new RestrictionsBuilder(IntervaloCepUF.class, false);
		return super.getRowCount(criteria, null, restrictions, rbIntervaloCepUf);
	}
	
	public boolean findIntervaloCepByUF(IntervaloCepUF intervaloCepUF) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select count(*)");
		hql.append(" from IntervaloCepUF ituf "+ 
				   " join ituf.unidadeFederativa "+
				   " uf join uf.pais pais");
		hql.append(" where ");

		hql.append(" pais.idPais = :idPais");

		hql.append(" and ((ituf.nrCepInicial <= :nrCepInicial and ituf.nrCepFinal >= :nrCepFinal) ");
		hql.append("or (ituf.nrCepInicial between :nrCepInicial  and :nrCepFinal) or (ituf.nrCepFinal between :nrCepInicial  and :nrCepFinal))");

		if(intervaloCepUF.getIdIntervaloCepUF()!= null)
			hql.append("and ituf.idIntervaloCepUF <> :idIntervaloCepUF");

		ArrayList arrayCampos = new ArrayList();
		arrayCampos.add("idPais");
		arrayCampos.add("nrCepInicial");
		arrayCampos.add("nrCepFinal");
	 	if (intervaloCepUF.getIdIntervaloCepUF()!= null)
			arrayCampos.add("idIntervaloCepUF");

		String[] campos = new String[arrayCampos.size()];
		campos = (String[]) arrayCampos.toArray(campos);

		ArrayList values = new ArrayList();
		values.add(intervaloCepUF.getUnidadeFederativa().getPais().getIdPais());
		values.add(intervaloCepUF.getNrCepInicial());
		values.add(intervaloCepUF.getNrCepFinal());
		if (intervaloCepUF.getIdIntervaloCepUF() != null)
			values.add(intervaloCepUF.getIdIntervaloCepUF());

	 	List result = this.getAdsmHibernateTemplate().findByNamedParam(hql.toString(), campos, values.toArray());

		return ((Long)result.get(0)).intValue() > 0;
	}

}
