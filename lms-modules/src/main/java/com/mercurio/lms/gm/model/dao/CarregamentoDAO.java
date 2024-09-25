package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Carregamento;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte ao Hibernate em conjunto com o Spring. Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class CarregamentoDAO extends BaseCrudDao<Carregamento, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Carregamento.class;
	}

	public void store(Carregamento carregamento) {
		super.store(carregamento);
	}

	private void storeAll(List carregamentoList) {
		getAdsmHibernateTemplate().saveOrUpdateAll(carregamentoList);
		getAdsmHibernateTemplate().flush();
	}

	/**
	 * Busca Carregamento pelo id.
	 * 
	 * @param Long
	 *            id.
	 * @return CabecalhoCarregamento, nulo, caso negativo.
	 */
	public Carregamento findCarregamentoByid(Long id) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idCarregamento", id));
		List<Carregamento> listCarregamento = findByDetachedCriteria(dc);
		if (listCarregamento != null && listCarregamento.size() > 0) {
			return listCarregamento.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Busca o carregamento de acordo com a placa passada e com os status 1,2 e 6(Aberto, Carregando, Concluído). Este DAO é usado na tela de embarque do MWW-GM para verificar o status do carregamento.
	 * 
	 * Demanda LMS-1538
	 * 
	 * @author mxavier@voiza.com.br
	 * @param placaVeiculo
	 * @return
	 */
	public Carregamento findCarregamentoByPlacaVeiculo(String placaVeiculo) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("placaVeiculo", placaVeiculo));
		dc.add(Restrictions.or(Restrictions.or(Restrictions.eq("codigoStatus", new DomainValue("1")), Restrictions.eq("codigoStatus", new DomainValue("2"))), Restrictions.eq("codigoStatus", new DomainValue("6"))));
		List<Carregamento> listCarregamento = findByDetachedCriteria(dc);
		if (listCarregamento != null && listCarregamento.size() > 0) {
			return listCarregamento.get(0);
		} else {
			return null;
		}
	}

}
