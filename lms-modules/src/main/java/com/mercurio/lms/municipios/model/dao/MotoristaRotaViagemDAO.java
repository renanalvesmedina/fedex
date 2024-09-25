package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.MotoristaRotaViagem;
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
public class MotoristaRotaViagemDAO extends BaseCrudDao<MotoristaRotaViagem, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotoristaRotaViagem.class;
    }
    
    protected void initFindByIdLazyProperties(Map map) {
    	map.put("motorista",FetchMode.JOIN);
    	map.put("motorista.pessoa",FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("motorista",FetchMode.JOIN);
    	map.put("motorista.pessoa",FetchMode.JOIN);
    }
    
    public boolean validateDuplicated(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
    		Long idMotorista, Long idRotaViagem) {
    	DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),id,dtVigenciaInicial,dtVigenciaFinal);

    	dc.createAlias("motorista","m");
    	dc.createAlias("rotaViagem","r");
    	dc.add(Restrictions.eq("m.idMotorista",idMotorista));
    	dc.add(Restrictions.eq("r.idRotaViagem",idRotaViagem));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
    }
    
    public List findToConsultarRotas(Long idRotaViagem) {
    	StringBuffer hql = new StringBuffer()
    		.append(" select new Map( ")
    		.append("  P.tpIdentificacao as motorista_pessoa_tpIdentificacao, ")
    		.append("  P.nrIdentificacao as motorista_pessoa_nrIdentificacao, ")
    		.append("  P.nmPessoa as motorista_pessoa_nmPessoa, ")    		
    		.append("  MOT.dtVigenciaInicial as dtVigenciaInicial, ")
    		.append("  MOT.dtVigenciaFinal as dtVigenciaFinal ")
    		.append(" ) ")
    		.append(" from " + MotoristaRotaViagem.class.getName() + " MOT ")
    		.append(" left join MOT.motorista as M ")
    		.append(" left join M.pessoa as P ")
    		.append(" left join MOT.rotaViagem as RV ")
    		.append(" where RV.idRotaViagem = ? ")
    		.append(" order by P.nmPessoa ");
    		
    	
    	return getAdsmHibernateTemplate().find(hql.toString(),idRotaViagem);
    }

    
    /**
     * 
     * @param idRotaViagem
     * @return
     */
    public List findByIdRotaIdaVolta(Long idRotaViagem) {
    	StringBuffer hql = new StringBuffer()
    		.append("select new Map( ")
    		.append("mrv.idMotoristaRotaViagem as idMotoristaRotaViagem, ")
    		.append("motorista.idMotorista as motorista_idMotorista,")
    		.append("pessoa.tpIdentificacao as motorista_pessoa_tpIdentificacao,")
    		.append("pessoa.nrIdentificacao as motorista_pessoa_nrIdentificacao, ")
    		.append("motorista.tpVinculo as motorista_tpVinculo, ")
    		.append("pessoa.nmPessoa as motorista_pessoa_nmPessoa) ")    		
    		.append("from ").append(MotoristaRotaViagem.class.getName()).append(" as mrv ")
    		.append("inner join mrv.motorista as motorista ")
    		.append("inner join motorista.pessoa as pessoa ")
    		.append("inner join mrv.rotaViagem as rv ")
    		.append("inner join rv.rotaIdaVoltas as riv ")
    		.append("where riv.id = ? ")
    		.append("and motorista.tpSituacao = 'A' ")
    		.append("and ? between mrv.dtVigenciaInicial and mrv.dtVigenciaFinal ");

    	List param = new ArrayList();
    	param.add(idRotaViagem);
    	param.add(JTDateTimeUtils.getDataAtual());

    	return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
    }
}