package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CabecalhoCarregamentoDAO extends BaseCrudDao<CabecalhoCarregamento, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return CabecalhoCarregamento.class;
	}

	public void store(CabecalhoCarregamento cabecalhoCarregamento) {
		super.store(cabecalhoCarregamento);
	}
	
	private void storeAll(List cabecalhoCarregamentoList) {
		getAdsmHibernateTemplate().saveOrUpdateAll(cabecalhoCarregamentoList);
		getAdsmHibernateTemplate().flush();
	}
	
	/**
	 * Busca CabecalhoCarregamento pelo id.
	 * 
	 * @author Samuel Alves
	 * @param Long id.
	 * @return CabecalhoCarregamento, nulo, caso negativo.
	 */
	public CabecalhoCarregamento findCabecalhoByid(Long id){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idCabecalhoCarregamento",id));
		List<CabecalhoCarregamento> listCabecalho = findByDetachedCriteria(dc);
		if(listCabecalho!=null && listCabecalho.size()>0){
			return listCabecalho.get(0);
		}else{
			return null;
		}	 
	}
	
	/**
	 * Busca CabecalhoCarregamento pelo Mapa de Carregamento.
	 * 
	 * @author Samuel Alves
	 * @param String mapa.
	 * @return CabecalhoCarregamento, nulo, caso negativo.
	 */
	public CabecalhoCarregamento findCabecalhoByMapaCarregamento(Long mapa){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("mapaCarregamento", mapa));
		List<CabecalhoCarregamento> listCabecalho = findByDetachedCriteria(dc);
		if(listCabecalho!=null && listCabecalho.size()>0){
			return listCabecalho.get(0);
		}else{
			return null;
}
	}
}
