package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CiaFilialMercurioDAO extends BaseCrudDao<CiaFilialMercurio, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return CiaFilialMercurio.class;
    }

    @Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("empresa",FetchMode.JOIN);
    	lazyFindById.put("empresa.pessoa",FetchMode.JOIN);
    	lazyFindById.put("filial",FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa",FetchMode.JOIN);
    	lazyFindById.put("observacaoCiaAerea",FetchMode.JOIN);
    }
    
    @Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
    	lazyFindLookup.put("filial",FetchMode.JOIN);
    	lazyFindLookup.put("filial.pessoa",FetchMode.JOIN);
    	lazyFindLookup.put("empresa",FetchMode.JOIN);
    	lazyFindLookup.put("empresa.pessoa",FetchMode.JOIN);
    }

    public boolean verificaAssocFilialCiaAerea(CiaFilialMercurio bean) {
    	DetachedCriteria dc = DetachedCriteria.forClass(CiaFilialMercurio.class);
    	dc.createAlias("filial","filial_");
    	dc.createAlias("empresa","empresa_");
    	dc.add(Restrictions.eq("filial_.idFilial",bean.getFilial().getIdFilial()));
    	dc.add(Restrictions.eq("empresa_.idEmpresa",bean.getEmpresa().getIdEmpresa()));
    	if (bean.getIdCiaFilialMercurio() != null)
    		dc.add(Restrictions.ne("idCiaFilialMercurio",bean.getIdCiaFilialMercurio()));
    	dc = JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());
    	List l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	return l.size() > 0;
    }

    /**
     * Verifica se a filial de cia aérea está vigente no período de datas indicado.
     * autor Felipe Ferreira
     * @param bean
     * @return se registro for vigente.
     */
    public boolean verificaSeCiaFilialMercurioVigente(Long idCiaFilialMercurio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	DetachedCriteria dc = DetachedCriteria.forClass(CiaFilialMercurio.class);
    	dc.add(Restrictions.eq("idCiaFilialMercurio",idCiaFilialMercurio));
    	
    	dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
    	if (dtVigenciaFinal != null)
    		dc.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal",dtVigenciaFinal),Restrictions.isNull("dtVigenciaFinal")));
    	else
    		dc.add(Restrictions.isNull("dtVigenciaFinal"));
    	
    	List l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	return l.size() > 0;
    }   
    
    public CiaFilialMercurio findByIdCiaAereaIdFilial(Long idCiaAerea, Long idFilial) {
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

    	DetachedCriteria dc = DetachedCriteria.forClass(CiaFilialMercurio.class)
    	.add(Restrictions.eq("e.idEmpresa", idCiaAerea))
    	.add(Restrictions.eq("f.idFilial", idFilial))
    	.add(Restrictions.le("dtVigenciaInicial", dataAtual))
    	.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal", dataAtual), Restrictions.isNull("dtVigenciaFinal")))
    	.createAlias("empresa", "e")
    	.createAlias("filial", "f");

    	return (CiaFilialMercurio) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public CiaFilialMercurio findCiaFilialMercurioByIdPrestacaoConta(Long idPrestacaoConta){
    	DetachedCriteria dc = DetachedCriteria.forClass(CiaFilialMercurio.class)
    	.add(Restrictions.eq("c.idPrestacaoConta", idPrestacaoConta));

    	return (CiaFilialMercurio) getAdsmHibernateTemplate().findUniqueResult(dc);    	
    }
    
    public List findCiaAerea(TypedFlatMap criteria) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"CFM");
    	dc.createAlias("filial","f");
    	dc.createAlias("empresa","e");
    	dc.createAlias("e.pessoa","p");

    	dc.setFetchMode("e",FetchMode.DEFAULT);
    	dc.setFetchMode("p",FetchMode.DEFAULT);

		dc.setProjection(
				Projections.projectionList()
					.add(Projections.property("CFM.idCiaFilialMercurio"), "idCiaFilialMercurio")
					.add(Projections.property("e.id"), "empresa.idEmpresa")
					.add(Projections.property("e.tpSituacao"), "empresa.tpSituacao")
					.add(Projections.property("e.sgEmpresa"), "empresa.sgEmpresa")
					.add(Projections.property("p.nmPessoa"), "empresa.pessoa.nmPessoa")
					.add(Projections.property("f.sgFilial"), "filial.sgFilial")
		);

    	dc.add(Restrictions.eq("e.tpEmpresa",criteria.getString("tpEmpresa")));
    	dc.add(Restrictions.eq("f.idFilial",criteria.getLong("idFilial")));
		dc.add(Restrictions.le("CFM.dtVigenciaInicial", criteria.getYearMonthDay("dtAtual"))); 
		dc.add(Restrictions.ge("CFM.dtVigenciaFinal",  criteria.getYearMonthDay("dtAtual")));

		dc.addOrder(Order.asc("p.nmPessoa"));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public CiaFilialMercurio findById(Long idCiaFilialMercurio, boolean useLock) {

    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cfm");
    	dc.add(Restrictions.eq("cfm.id", idCiaFilialMercurio));

    	CiaFilialMercurio ciaFilialMercurio = (CiaFilialMercurio) getAdsmHibernateTemplate().findUniqueResult(dc);
		if(ciaFilialMercurio != null) {
			if(useLock) {
	    		getAdsmHibernateTemplate().lock(ciaFilialMercurio, LockMode.UPGRADE);
			}
		}
    	return ciaFilialMercurio;
    }
    
    public void updateNrPrestacaoContas(Long idCiaFilialMercurio, Long nrPrestacaoConta){
    	
    	String query = "UPDATE "+ CiaFilialMercurio.class.getName() + " cfm" +
    	" SET cfm.nrPrestacaoContas=(cfm.nrPrestacaoContas-1) WHERE cfm.idCiaFilialMercurio=? and cfm.nrPrestacaoContas = ?";
    	
    	getAdsmHibernateTemplate().bulkUpdate(query, new  Object[]{idCiaFilialMercurio,nrPrestacaoConta});    	
    }
    
    public Long findNrCcTomadorServico(Awb awb, Long idCiaAerea) {
    	Long nrTomadorServico = null;

    	if (awb.getClienteByIdClienteTomador() != null) {
	    	YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
	    	
	    	DetachedCriteria dc = DetachedCriteria.forClass(CiaFilialMercurio.class);
	    	dc.add(Restrictions.eq("filial.idFilial", awb.getClienteByIdClienteTomador().getIdCliente()));
	    	dc.add(Restrictions.eq("empresa.idEmpresa", idCiaAerea));
	    	dc.add(Restrictions.ge("dtVigenciaFinal", dtAtual));
	    	dc.add(Restrictions.le("dtVigenciaInicial", dtAtual));
	    	
	    	CiaFilialMercurio ciaFilialMercurio = (CiaFilialMercurio) getAdsmHibernateTemplate().findUniqueResult(dc);
	    	if (ciaFilialMercurio != null) {
	    		nrTomadorServico = ciaFilialMercurio.getVlIdentificadorCiaAerea();
	    	}
    	}
    	
    	return nrTomadorServico;
    }
 
    public Long findByIdFilialAndNrIdentificacaoBetweenDtVigencia(Long idFilial, String nrIdentificacao, YearMonthDay dtVigencia) {
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select cfm.id_cia_filial_mercurio ")
    	.append(" from empresa e, ") 
    	.append(" filial_cia_aerea fca, ")
    	.append(" pessoa pfca, ")
    	.append(" cia_filial_mercurio cfm ")
    	.append(" where e.id_empresa = fca.id_empresa ")
    	.append(" and   fca.id_filial_cia_aerea = pfca.id_pessoa ")
    	.append(" and   cfm.id_empresa = e.id_empresa ")
    	.append(" and   :dtVigencia between cfm.dt_vigencia_inicial and cfm.dt_vigencia_final ")
    	.append(" and   pfca.nr_identificacao = :nrIdentificacao ")
    	.append(" and   cfm.id_filial = :idFilial ");
    	
    	
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.addScalar("id_cia_filial_mercurio", Hibernate.LONG);
		
		query.setString("dtVigencia", dtVigencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
		query.setString("nrIdentificacao", nrIdentificacao);
		query.setLong("idFilial", idFilial);
    	
    	Object data = query.uniqueResult();
		if(data != null){
			return Long.valueOf(data.toString());
		}

		return null;
	}   

}