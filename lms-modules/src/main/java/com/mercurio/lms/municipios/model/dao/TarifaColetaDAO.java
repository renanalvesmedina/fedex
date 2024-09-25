package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.TarifaColeta;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TarifaColetaDAO extends BaseCrudDao<TarifaColeta, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TarifaColeta.class;
    }

    protected void initFindPaginatedLazyProperties(Map fetchMode) {
    	fetchMode.put("filialByIdFilial",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilial.pessoa",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialColeta",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialColeta.pessoa",FetchMode.JOIN);
    	fetchMode.put("tarifaPreco",FetchMode.JOIN);
    	fetchMode.put("municipio",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchMode);
    }
    protected void initFindByIdLazyProperties(Map fetchMode) {
    	fetchMode.put("filialByIdFilial",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilial.pessoa",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialColeta",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialColeta.pessoa",FetchMode.JOIN);
    	fetchMode.put("tarifaPreco",FetchMode.JOIN);
    	fetchMode.put("municipio",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchMode);
    }
    
    public List verificaVigencia(TarifaColeta tc) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("filialByIdFilial","FI");
    	dc.createAlias("filialByIdFilialColeta","FIC");
    	dc.createAlias("municipio","MU");
    	dc.add(Restrictions.eq("FI.idFilial",tc.getFilialByIdFilial().getIdFilial()));
    	dc.add(Restrictions.eq("FIC.idFilial",tc.getFilialByIdFilialColeta().getIdFilial()));
    	dc.add(Restrictions.eq("MU.idMunicipio",tc.getMunicipio().getIdMunicipio()));
    	if (tc.getIdTarifaColeta() != null)
    		dc.add(Restrictions.ne("idTarifaColeta",tc.getIdTarifaColeta()));
    	
    	JTVigenciaUtils.getDetachedVigencia(dc,tc.getDtVigenciaInicial(),tc.getDtVigenciaFinal());
    	List rs = findByDetachedCriteria(dc);
    	return rs;
    }


}