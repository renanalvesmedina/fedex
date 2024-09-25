package com.mercurio.lms.tributos.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaInssPessoaFisica;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AliquotaInssPessoaFisicaDAO extends BaseCrudDao<AliquotaInssPessoaFisica, Long>
{

	/**
	 * Busca AliquotaInssPessoaFisica vigente.<BR>
	 *@author Robson Edemar Gehl
	 * @param data vigem em
	 * @return
	 */
	public AliquotaInssPessoaFisica findAliquotaVigente(YearMonthDay data){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.le("dtInicioVigencia", data));
		dc.addOrder(Order.desc("dtInicioVigencia"));
		List list = findByDetachedCriteria(dc);
		if (!list.isEmpty()){
			return (AliquotaInssPessoaFisica) list.get(0);
		}
		return null;
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AliquotaInssPessoaFisica.class;
    }

   


}