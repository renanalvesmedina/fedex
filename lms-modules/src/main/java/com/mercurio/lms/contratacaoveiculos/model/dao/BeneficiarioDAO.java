package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario;
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
public class BeneficiarioDAO extends BaseCrudDao<Beneficiario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Beneficiario.class;
    }
    
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("pessoa", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(fetchModes);
	}
    
	/**
     * Verifica a existencia da especialização com mesmo Numero e Tipo de Identificacao, exceto a mesma.
     * @param map
     * @return a existência de uma especialização
     */
    public boolean verificaExistenciaEspecializacao(Beneficiario beneficiario){
    	DetachedCriteria dc = createDetachedCriteria();

    	dc.createAlias("pessoa", "pessoa_");
    	dc.add(Restrictions.eq("pessoa_.tpPessoa", beneficiario.getPessoa().getTpPessoa()));
    	dc.add(Restrictions.eq("pessoa_.nrIdentificacao", beneficiario.getPessoa().getNrIdentificacao()));
    	dc.add(Restrictions.eq("pessoa_.tpIdentificacao", beneficiario.getPessoa().getTpIdentificacao().getValue()));

    	dc.setProjection( Projections.rowCount() );
    	
    	Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    	
    	return (result > 0);	
    }
    
    
    public List findBeneficiarioVigente(Long idBeneficiario,Date dtVigenciaInicial, Date dtVigenciaFinal) {
    	DetachedCriteria dc = DetachedCriteria.forClass(BeneficiarioProprietario.class);
    	dc.add(Restrictions.eq("beneficiario.idBeneficiario",idBeneficiario));
    	// VIGENTE
    	dc.add(Restrictions.le("dtVigenciaInicial",new Date(System.currentTimeMillis())));
    	dc.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal",new Date(System.currentTimeMillis())),Restrictions.isNull("dtVigenciaFinal")));
    	
    	if (dtVigenciaInicial != null){
    	dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
    	dc.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal",dtVigenciaInicial),Restrictions.isNull("dtVigenciaFinal")));
    	}
    	if (dtVigenciaFinal != null){
    		dc.add(Restrictions.ge("dtVigenciaFinal",dtVigenciaFinal));
    	}
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
	/**
	 * Retorna 'true' se a pessoa informada é um beneficiario ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isBeneficiario(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("count(be.id)");
		
		hql.addInnerJoin(Beneficiario.class.getName(), "be");
		hql.addInnerJoin("be.beneficiarioProprietarios", "bp");
		
		hql.addCriteria("be.id", "=", idPessoa);
		
		JTVigenciaUtils.getHqlVigenciaNotNull(hql
										   , "bp.dtVigenciaInicial"
										   , "bp.dtVigenciaFinal"
										   , JTDateTimeUtils.getDataAtual()
										   , null);
		
		List lstBeneficiario = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (((Integer)lstBeneficiario.get(0)) > 0){
			return true;
		} else {
			return false;
		}
	}    
}