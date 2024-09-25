package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.municipios.model.MotivoParada;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoParadaDAO extends BaseCrudDao<MotivoParada, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoParada.class;
    }

    /**
     * 
     */
    public List findListByCriteria(Map criterions) {
		List listaOrder = new ArrayList();
		listaOrder.add("dsMotivoParada:asc");
		return super.findListByCriteria(criterions,listaOrder);
	}

    /**
     * 
     * @param idPontoParadaTrecho
     * @return
     */
    public List findByIdPontoParadaTrecho(Long idPontoParadaTrecho) {
    	StringBuffer sql = new StringBuffer()
			.append("select new Map( ")
			.append("mp.idMotivoParada as idMotivoParada, ")
			.append("mp.dsMotivoParada as dsMotivoParada) ")
			.append("from ").append(MotivoParada.class.getName()).append(" mp ")
			.append("inner join mp.motivoParadaPontoTrechos as mppt ")
			.append("where mppt.pontoParadaTrecho.id = ? ")
			.append("order by ")
			.append(OrderVarcharI18n.hqlOrder("mp.dsMotivoParada",LocaleContextHolder.getLocale(), true));
    	return getAdsmHibernateTemplate().find(sql.toString(), idPontoParadaTrecho);
    }
}