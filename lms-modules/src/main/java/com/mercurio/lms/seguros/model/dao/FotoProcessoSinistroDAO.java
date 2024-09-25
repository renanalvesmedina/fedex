package com.mercurio.lms.seguros.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.FotoProcessoSinistro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FotoProcessoSinistroDAO extends BaseCrudDao<FotoProcessoSinistro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FotoProcessoSinistro.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("foto", FetchMode.JOIN);
    }

    private SqlTemplate getFindPaginatedQuery(TypedFlatMap tfm) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("new Map(fps.idFotoProcessoSinistro", "idFotoProcessoSinistro");
    	sql.addProjection("fps.dsFoto",                         "dsFoto");
    	sql.addProjection("fo.idFoto",                            "foto_foto)");
    	sql.addFrom(FotoProcessoSinistro.class.getName(), "fps join fps.foto fo");
    	sql.addCriteria("fps.processoSinistro.id", "=", tfm.getLong("idProcessoSinistro"));
    	sql.addOrderBy("fps.dsFoto");
    	return sql;
    }
    
    public ResultSetPage findPaginatedCustom(FindDefinition fd, TypedFlatMap tfm) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    }
    
    public void removeFotoProcessoSinistro(FotoProcessoSinistro fotoProcessoSinistro) {
    	getAdsmHibernateTemplate().delete(fotoProcessoSinistro);
    }
   
}