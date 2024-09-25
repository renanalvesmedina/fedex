package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.PontoParada;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PontoParadaDAO extends BaseCrudDao<PontoParada, Long>
{

	protected void initFindPaginatedLazyProperties(Map fetchMode) {
    	fetchMode.put("pessoa",FetchMode.JOIN);    	
    	fetchMode.put("municipio",FetchMode.JOIN);
    	fetchMode.put("municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchMode.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchMode.put("rodovia",FetchMode.JOIN);
    }

    protected void initFindByIdLazyProperties(Map fetchMode) {
    	fetchMode.put("pessoa",FetchMode.JOIN);
    	fetchMode.put("municipio",FetchMode.JOIN);
    	fetchMode.put("municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchMode.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchMode.put("rodovia",FetchMode.JOIN);
    }
    
    protected void initFindLookupLazyProperties(Map fetchMode) {
    	fetchMode.put("pessoa",FetchMode.JOIN);
    	fetchMode.put("municipio",FetchMode.JOIN);
    	fetchMode.put("municipio.unidadeFederativa",FetchMode.JOIN);
    	fetchMode.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
    	fetchMode.put("rodovia",FetchMode.JOIN);
    }
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PontoParada.class;
    }
    
    
    /**
     * @param idRotaIdaVolta
     * @return
     */
    public List findPontoParadaByRotaIdaVolta(Long idRotaIdaVolta) {
    	StringBuffer sql = new StringBuffer()
			.append("select new Map( ")
			.append("pp.idPontoParada as idPontoParada, ")
			.append("pp.nmPontoParada as nmPontoParada, ")
			.append("municipio.nmMunicipio as municipio_nmMunicipio, ")
			.append("municipio.idMunicipio as municipio_idMunicipio, ")
			.append("uf.idUnidadeFederativa as municipio_unidadeFederativa_idUnidadeFederativa, ")
			.append("uf.sgUnidadeFederativa as municipio_unidadeFederativa_sgUnidadeFederativa) ")
			.append("from ").append(PontoParada.class.getName()).append(" pp ")
			.append("inner join pp.municipio as municipio ")
			.append("inner join municipio.unidadeFederativa as uf ")
			.append("inner join pp.pontoParadaTrechos as ppt ")
			.append("inner join ppt.trechoRotaIdaVolta as triv ")
			.append("where triv.rotaIdaVolta.id = ? ")
			.append("and ? between ppt.dtVigenciaInicial and ppt.dtVigenciaFinal ")
			.append("order by ")
			.append(" pp.nmPontoParada");
 
    	List param = new ArrayList();
    	param.add(idRotaIdaVolta);
    	param.add(JTDateTimeUtils.getDataAtual());
    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }
}