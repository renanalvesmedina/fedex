package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
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
public class MeioTranspRodoMotoristaDAO extends BaseCrudDao<MeioTranspRodoMotorista, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTranspRodoMotorista.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("motorista",FetchMode.JOIN);
    	lazyFindPaginated.put("motorista.pessoa",FetchMode.JOIN);
    	lazyFindPaginated.put("meioTransporteRodoviario",FetchMode.JOIN);
    	lazyFindPaginated.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("motorista",FetchMode.JOIN);
    	lazyFindById.put("motorista.pessoa",FetchMode.JOIN);
    	lazyFindById.put("meioTransporteRodoviario",FetchMode.JOIN);
    	lazyFindById.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
    }
    
    /**
     * Valida se já existe algum registro vigente com mesmo Meio de Transporte para um motorista.
     * 
     * @param idMeioTranspRodoMotorista Caso deseja-se excluir uma associação da validação.
     * @param idMeioTransporte
     * @return
     */
    public boolean validateApenasUmVigente(Long idMeioTranspRodoMotorista, Long idMeioTransporte, 
    		YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {    	
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(
    			getPersistentClass(),idMeioTranspRodoMotorista,dtVigenciaInicial,dtVigenciaFinal);

    	dc.createAlias("meioTransporteRodoviario","mt");
    	dc.add(Restrictions.eq("mt.idMeioTransporte",idMeioTransporte));
    	return (getAdsmHibernateTemplate().findByDetachedCriteria(dc)).size() > 0;
    }
    
    public MeioTranspRodoMotorista findRelacaoVigente(Long idMotorista, Long idMeioTransporte, 
    		YearMonthDay dtReferencia) {
    	if (idMotorista == null && idMeioTransporte == null)
    		throw new IllegalArgumentException("Deve ser informado idMotorista ou idMeioTransporte.");
    	if (dtReferencia == null)
    		throw new IllegalArgumentException("Deve ser informado dtReferencia.");
    	    	
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("MTRM");
    	hql.addFrom(new StringBuffer().append(MeioTranspRodoMotorista.class.getName())
    			.append(" MTRM ")
    			.append(" inner join fetch MTRM.meioTransporteRodoviario MTR ")
    			.append(" inner join fetch MTR.meioTransporte ")
    			.append(" inner join fetch MTRM.motorista MOT ")
    			.append(" inner join fetch MOT.pessoa")
    			.toString());
    	
    	hql.addCriteria("MOT.id","=",idMotorista);
    	hql.addCriteria("MTR.id","=",idMeioTransporte);
    	
    	hql.addCriteria("MTRM.dtVigenciaInicial","<=",dtReferencia);
    	hql.addCriteria("MTRM.dtVigenciaFinal",">=",dtReferencia);
    	
    	return (MeioTranspRodoMotorista)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
    }

	public List<Motorista> findMotoristaVigenteBySolicitacaoContratacao(SolicitacaoContratacao solicitacaoContratacao) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("MTRM.motorista");
		hql.addFrom(new StringBuffer().append(MeioTranspRodoMotorista.class.getName())
    			.append(" MTRM ")
    			.append(" inner join MTRM.meioTransporteRodoviario MTR ")
    			.append(" inner join MTR.meioTransporte ")    			
    			.toString());
		hql.addCriteria("MTR.meioTransporte.nrIdentificador", "=",solicitacaoContratacao.getNrIdentificacaoMeioTransp());
		hql.addCriteria("MTRM.dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
    	hql.addCriteria("MTRM.dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
	    return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }
}