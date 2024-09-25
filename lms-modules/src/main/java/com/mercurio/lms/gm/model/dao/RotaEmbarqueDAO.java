package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.RotaEmbarque;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaEmbarqueDAO extends BaseCrudDao<RotaEmbarque, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return RotaEmbarque.class;
	}
	
	/**
	 * Busca RotaEmbarque pela sigla da Rota.
	 * 
	 * @author Samuel Alves
	 * @param String siglaRotaEmbarque.
	 * @return RotaEmbarque, nulo, caso negativo.
	 */
	public RotaEmbarque findRotaBySigla(String siglaRota){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("siglaRota",siglaRota).ignoreCase());
		List<RotaEmbarque> listRotas = findByDetachedCriteria(dc);
		if(listRotas!=null && listRotas.size()>0){
			return listRotas.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca RotaEmbarque pela descricao da Rota.
	 * 
	 * @author Samuel Alves
	 * @param String nomeRotaEmbarque.
	 * @return RotaEmbarque, nulo, caso negativo.
	 */
	public RotaEmbarque findRotaByNome(String nomeRotaMaster){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nomeRota",nomeRotaMaster).ignoreCase());
		List<RotaEmbarque> listRotas = findByDetachedCriteria(dc);
		if(listRotas!=null && listRotas.size()>0){
			return listRotas.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca RotaEmbarque pela segla e descricao da Rota.
	 * 
	 * @author Samuel Alves
	 * @param String siglaRota.
	 * * @param String nomeRotaEmbarque.
	 * @return RotaEmbarque, nulo, caso negativo.
	 */
	public RotaEmbarque findRotaBySiglaNome(String siglaRota, String nomeRotaMaster){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.and(Restrictions.eq("siglaRota",siglaRota).ignoreCase(), Restrictions.eq("nomeRota",nomeRotaMaster).ignoreCase()));
		List<RotaEmbarque> listRotas = findByDetachedCriteria(dc);
		if(listRotas!=null && listRotas.size()>0){
			return listRotas.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca RotaEmbarque pela sigla da Rota.
	 * 
	 * @author Samuel Alves
	 * @param RotaEmbarque.
	 * @return RotaEmbarque, nulo, caso negativo.
	 */
	public RotaEmbarque findRotaByIdSigla(RotaEmbarque bean){
		DetachedCriteria dc = createDetachedCriteria();
		
		if(bean.getIdRotaEmbarque() != null){
			dc.add(Restrictions.ne("idRotaEmbarque", bean.getIdRotaEmbarque()));
		}
		
		dc.add(Restrictions.eq("siglaRota", bean.getSiglaRota()).ignoreCase());
		List<RotaEmbarque> listRotas = findByDetachedCriteria(dc);
		if(listRotas!=null && listRotas.size()>0){
			return listRotas.get(0);
		}else{
			return null;
		}
	}
	
}