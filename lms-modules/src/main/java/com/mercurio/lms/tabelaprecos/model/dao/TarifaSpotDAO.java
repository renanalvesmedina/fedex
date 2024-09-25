package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TarifaSpotDAO extends BaseCrudDao<TarifaSpot, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TarifaSpot.class;
    }

   protected void initFindByIdLazyProperties(Map lazyFindById) {
	   super.initFindByIdLazyProperties(lazyFindById);
	   lazyFindById.put("filial", FetchMode.JOIN);
	   lazyFindById.put("filial.pessoa", FetchMode.JOIN);
	   lazyFindById.put("moeda", FetchMode.JOIN);
	   lazyFindById.put("empresa", FetchMode.JOIN);
	   lazyFindById.put("empresa.pessoa", FetchMode.JOIN);
	   lazyFindById.put("aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
	   lazyFindById.put("aeroportoByIdAeroportoOrigem.pessoa", FetchMode.JOIN);
	   lazyFindById.put("aeroportoByIdAeroportoDestino", FetchMode.JOIN);
	   lazyFindById.put("aeroportoByIdAeroportoDestino.pessoa", FetchMode.JOIN);
	   lazyFindById.put("usuarioByIdUsuarioSolicitante", FetchMode.JOIN);
	   lazyFindById.put("usuarioByIdUsuarioLiberador", FetchMode.JOIN);
   }

   protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
	   super.initFindPaginatedLazyProperties(lazyFindPaginated);
	   lazyFindPaginated.put("filial", FetchMode.JOIN);
	   lazyFindPaginated.put("moeda", FetchMode.JOIN);
	   lazyFindPaginated.put("empresa", FetchMode.JOIN);
	   lazyFindPaginated.put("empresa.pessoa", FetchMode.JOIN);
	   lazyFindPaginated.put("aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
	   lazyFindPaginated.put("aeroportoByIdAeroportoDestino", FetchMode.JOIN);
   }

   public TarifaSpot findByDsSenha(String dsSenha) {
	   DetachedCriteria dc = createDetachedCriteria()
		   .createAlias("filial", "f")
		   .createAlias("aeroportoByIdAeroportoOrigem", "ao")
		   .createAlias("aeroportoByIdAeroportoDestino", "ad")
		   .add(Restrictions.eq("dsSenha", StringUtils.leftPad(dsSenha, 8, "0")));

	   return (TarifaSpot) getAdsmHibernateTemplate().findUniqueResult(dc);
   }


   public TarifaSpot findById(Long idTarifaSpot, boolean useLock) {
	   if (idTarifaSpot == null) {
		   throw new IllegalArgumentException("idTarifaSpot não pode ser null ou vazio");
	   }

	   StringBuilder query = new StringBuilder();
	   query.append("FROM ").append(TarifaSpot.class.getName()).append(" ts ");
	   query.append("WHERE ts.idTarifaSpot = ?");

	   TarifaSpot tarifaSpot;
	   if (useLock) {
		   tarifaSpot = (TarifaSpot) getAdsmHibernateTemplate().findUniqueResultForUpdate(query.toString(), new Object[]{idTarifaSpot}, "ts");
	   } else {
		   tarifaSpot = (TarifaSpot) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idTarifaSpot});
	   }
	   return tarifaSpot;
   }
}