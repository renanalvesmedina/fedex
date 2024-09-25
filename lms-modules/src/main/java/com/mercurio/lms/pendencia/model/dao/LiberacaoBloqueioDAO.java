package com.mercurio.lms.pendencia.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.LiberacaoBloqueio;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LiberacaoBloqueioDAO extends BaseCrudDao<LiberacaoBloqueio, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LiberacaoBloqueio.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("ocorrenciaPendenciaByIdOcorrenciaLiberacao", FetchMode.JOIN);
    	lazyFindById.put("ocorrenciaPendenciaByIdOcorrenciaBloqueio", FetchMode.JOIN);
    }

    public ResultSetPage findPaginatedLiberacaoBloqueio(FindDefinition fd, TypedFlatMap tfm) {
	   DetachedCriteria dc = createDetachedCriteria()
	   .setProjection(Projections.projectionList()
		   .add(Projections.property("idLiberacaoBloqueio"), "idLiberacaoBloqueio")
		   .add(Projections.property("liberacao.cdOcorrencia"), "ocorrenciaPendenciaByIdOcorrenciaLiberacao_cdOcorrencia")
		   .add(Projections.property("liberacao.dsOcorrencia"), "ocorrenciaPendenciaByIdOcorrenciaLiberacao_dsOcorrencia")
		   .add(Projections.property("bloqueio.cdOcorrencia"), "ocorrenciaPendenciaByIdOcorrenciaBloqueio_cdOcorrencia")
		   .add(Projections.property("bloqueio.dsOcorrencia"), "ocorrenciaPendenciaByIdOcorrenciaBloqueio_dsOcorrencia"))
       .setResultTransformer(AliasToNestedMapResultTransformer.getInstance())		   
	   .createAlias("ocorrenciaPendenciaByIdOcorrenciaLiberacao", "liberacao")
	   .createAlias("ocorrenciaPendenciaByIdOcorrenciaBloqueio", "bloqueio")
	   .addOrder(Order.asc("bloqueio.cdOcorrencia"))
	   .addOrder(OrderVarcharI18n.asc("bloqueio.dsOcorrencia", LocaleContextHolder.getLocale()))
	   .addOrder(Order.asc("liberacao.cdOcorrencia"))
	   .addOrder(OrderVarcharI18n.asc("liberacao.dsOcorrencia", LocaleContextHolder.getLocale()));
	   
	   if (tfm.getLong("ocorrenciaPendenciaByIdOcorrenciaBloqueio.idOcorrenciaPendencia")!=null)
		   dc.add(Restrictions.eq("bloqueio.id", tfm.getLong("ocorrenciaPendenciaByIdOcorrenciaBloqueio.idOcorrenciaPendencia")));

	   if (tfm.getLong("ocorrenciaPendenciaByIdOcorrenciaLiberacao.idOcorrenciaPendencia")!=null)
		   dc.add(Restrictions.eq("liberacao.id", tfm.getLong("ocorrenciaPendenciaByIdOcorrenciaLiberacao.idOcorrenciaPendencia")));
		   
	   return findPaginatedByDetachedCriteria(dc, fd.getCurrentPage(), fd.getPageSize());
    }
   
}