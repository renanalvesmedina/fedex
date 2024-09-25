package com.mercurio.lms.municipios.model.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureRestrictionsBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.IntervaloCep;
import com.mercurio.lms.municipios.model.IntervaloCepUF;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IntervaloCepDAO extends BaseCrudDao<IntervaloCep, Long>
{

	private ConfigureRestrictionsBuilder rbIntervaloCep;
	
	protected void initDao() throws Exception {
		rbIntervaloCep = new ConfigureRestrictionsBuilder() {

        public void configure(RestrictionsBuilder rb) {

             List campos = new ArrayList(2);
             campos.add("nrCepInicial");
             campos.add("nrCepFinal");
    		
            //Implementação do RestrictionBuilder customizado para tratar o filtro nos campos indicados
    		RestrictionBuilder restrict = new RestrictionBuilder() {
    			public void buildRestriction(DetachedCriteria dc, String ownerProperty, Map values) {
    				if (ownerProperty.equalsIgnoreCase("")) {
    					if (values.get("nrCepInicial") != null && !"".equals(values.get("nrCepInicial"))){    						
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
	
	public ResultSetPage findPaginated(Map criteria) {
		 RestrictionsBuilder restrictions = new RestrictionsBuilder(IntervaloCep.class, false);
		 
	     return super.findPaginated(criteria, FindDefinition.createFindDefinition(criteria), restrictions, rbIntervaloCep);

	}
	
	public Integer getRowCount(Map criteria) {
		RestrictionsBuilder restrictions = new RestrictionsBuilder(IntervaloCep.class, false);
		
		return super.getRowCount(criteria, null, restrictions, rbIntervaloCep);
	}
	
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipio",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipio",FetchMode.JOIN);
		fetchModes.put("municipio.municipioDistrito",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return IntervaloCep.class;
    }
    
    //metodo chamado no MunFilIntervaloCepDAO
    public boolean verificaIntervaloCepsMunicipio(Long idMunicipio, String nrCepInicial, String nrCepFinal){

    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.and(
    			Restrictions.le("nrCepInicial",nrCepInicial),
				Restrictions.ge("nrCepFinal",nrCepFinal)));
    	
    	DetachedCriteria dcMunicipio = dc.createCriteria("municipio");
    	dcMunicipio.add(Restrictions.eq("idMunicipio",idMunicipio));
    	
    	
    	return findByDetachedCriteria(dcMunicipio).size()>0;
    }
    
    public List verificaIntervaloCep(String nrCepInicial, String nrCepFinal, Long idMunicipio, Long idIntervaloCep){
    	
    	StringBuffer hql = new StringBuffer();
    	
    	hql.append("select ic.municipio.nmMunicipio as nmMunicipio");
    	hql.append(" from IntervaloCep ic inner join ic.municipio mu inner join mu.unidadeFederativa uf ");
    	hql.append(" ,Municipio m");
    	      	   	
    	hql.append(" where ");
    	
    	if (idIntervaloCep != null){
    		hql.append(" ic.idIntervaloCep != :idIntervaloCep and");
    	}
    	  
    	hql.append(" m.idMunicipio = :idMunicipio");
    	hql.append(" and uf.idUnidadeFederativa = m.unidadeFederativa.idUnidadeFederativa");
    	
    	hql.append(" and ((ic.nrCepInicial <= :nrCepInicial and ic.nrCepFinal >= :nrCepFinal)");
    	hql.append(" or (ic.nrCepInicial between :nrCepInicial  and :nrCepFinal) or (ic.nrCepFinal between :nrCepInicial  and :nrCepFinal))");
    
    	ArrayList arrayCampos = new ArrayList();
    	if (idIntervaloCep != null)
    		arrayCampos.add("idIntervaloCep");
    	arrayCampos.add("idMunicipio");
    	arrayCampos.add("nrCepInicial");
    	arrayCampos.add("nrCepFinal");
    	
    	String[] campos = new String[arrayCampos.size()];
    	campos = (String[]) arrayCampos.toArray(campos);
    	 
    	ArrayList values = new ArrayList();
    	if (idIntervaloCep != null)
    		values.add(idIntervaloCep);
    	values.add(idMunicipio);
    	values.add(nrCepInicial);
    	values.add(nrCepFinal);
      	
    	List result = this.getAdsmHibernateTemplate().findByNamedParam(hql.toString(), campos, values.toArray());
    	
    	return result;
    	
    }
    
    /**
     * Verifica se o intervalo de CEPs do municipio esta dentro do intervalo de CEPS da UF 
     * @param nrCepInicial
     * @param nrCepFinal
     * @param idMunicipio
     * @return TRUE ou FALSe
     */
    public boolean verificaIntervaloCepUF(String nrCepInicial, String nrCepFinal, Long idMunicipio){
    	
		DetachedCriteria dc = DetachedCriteria.forClass(IntervaloCepUF.class);
		dc.add( Restrictions.le("nrCepInicial", nrCepInicial));
		dc.add( Restrictions.ge("nrCepFinal", nrCepFinal)); 
		dc.setProjection(Projections.rowCount());
		
		DetachedCriteria criteria = dc.createCriteria("unidadeFederativa");
		
		DetachedCriteria criteriaM = criteria.createCriteria("municipios");
		
		criteriaM.add(	Restrictions.eq("idMunicipio", idMunicipio) );
		
		Integer num = (Integer)findByDetachedCriteria(dc).get(0);

    	return ( !(num.intValue() > 0) );
    	 
    } 
    
    /**
     * Valida se o intervalo de ceps informado esta dentro de algum intervalo de ceps do municipio
     * @param nrCepInicial
     * @param nrCepFinal
     * @param idMunicipio
     * @return TRUE se o intervalo esta dentro de algum intervalo do municipio, FALSE caso contrario
     */
    public boolean validaIntervalo(String nrCepInicial, String nrCepFinal, Long idMunicipio){
    	
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.add(Restrictions.eq("municipio.idMunicipio",idMunicipio));
    	dc.add(Restrictions.le("nrCepInicial",nrCepInicial));
    	dc.add(Restrictions.ge("nrCepFinal",nrCepFinal));
    	dc.add(Restrictions.eq("tpSituacao","A"));
    	
    	dc.setProjection(Projections.rowCount());
    	
    	Integer num = (Integer)findByDetachedCriteria(dc).get(0);

    	return (num.intValue() > 0);
    	
    }
        
    public List findIntervaloCepByMunicipio(Long idMunicipio){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("new Map(" +
    			"int.nrCepInicial as nrCepInicial, " +
    			"int.nrCepFinal as nrCepFinal)" );
    	
    	hql.addFrom("IntervaloCep int " +
    			"join int.municipio mun");
    	
    	hql.addCriteria("mun.idMunicipio","=",idMunicipio);
    	hql.addCriteria("int.tpSituacao","=","A");
    	
    	hql.addOrderBy("int.nrCepInicial");
        	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    	
    public List findByCep(String nrCep,String sgPais) {

    	DetachedCriteria dc = createDetachedCriteria();

    	dc.createCriteria("municipio");
    	dc.createCriteria("municipio.unidadeFederativa");
    	dc.createCriteria("municipio.unidadeFederativa.pais", "pais");
    	dc.add(Restrictions.le("nrCepInicial",nrCep));
    	dc.add(Restrictions.ge("nrCepFinal",nrCep));
    	dc.add(Restrictions.eq("pais.sgPais",sgPais));
    	dc.add(Restrictions.eq("tpSituacao","A"));

    	return findByDetachedCriteria(dc);
    }

	/**
     * CQPRO00023601
     * Valida se o cep é válido no terrítório nacional
     *
     * @return true ou false.
     */
    public boolean validateCep(String nrCep, Long idPais) {                
          StringBuffer sb = new StringBuffer();
          
          sb.append(" select 1");
          sb.append(" from ").append(IntervaloCep.class.getName()).append(" as i, ");
          sb.append(Municipio.class.getName()).append(" as m, ");
          sb.append(UnidadeFederativa.class.getName()).append(" as u, ");
          sb.append(Pais.class.getName()).append(" as p");
          sb.append(" where ? >= i.nrCepInicial");
          sb.append(" and ? <= i.nrCepFinal");
          sb.append(" and i.municipio.idMunicipio = m.idMunicipio");
          sb.append(" and m.tpSituacao = 'A'");
          sb.append(" and m.unidadeFederativa.idUnidadeFederativa = u.idUnidadeFederativa");
          sb.append(" and u.tpSituacao = 'A'");
          sb.append(" and u.pais.idPais = p.idPais");
          sb.append(" and p.tpSituacao = 'A'");
          sb.append(" and p.idPais=?");
          
          List result = getAdsmHibernateTemplate().find(sb.toString(), new Object[]{nrCep,nrCep,idPais});
          
          if(result.isEmpty()){
                return false;
          }
          else{
                return true;
          }
    }
}