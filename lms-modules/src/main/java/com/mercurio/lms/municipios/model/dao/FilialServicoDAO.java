package com.mercurio.lms.municipios.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.FilialServico;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialServicoDAO extends BaseCrudDao<FilialServico, Long>
{

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filial",FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("servico", FetchMode.JOIN);
	}


	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial",FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("servico", FetchMode.JOIN);
	}


	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialServico.class;
    }
    
    //Uma filial não pode possuir mais de uma vez o mesmo serviço vigente. 
    public boolean verificaFilialServicoVigente(FilialServico filialServico){
    	DetachedCriteria dc = createDetachedCriteria();
    	if (filialServico.getIdFilialServico() != null){
    		dc.add(Restrictions.ne("idFilialServico",filialServico.getIdFilialServico()));
    	}
    	dc=JTVigenciaUtils.getDetachedVigencia(dc,filialServico.getDtVigenciaInicial(), filialServico.getDtVigenciaFinal());
    	
    	DetachedCriteria dcFilial = dc.createCriteria("filial");
    	dcFilial.add(Restrictions.eq("idFilial",filialServico.getFilial().getIdFilial()));
    	DetachedCriteria dcServico = dc.createCriteria("servico");
    	dcServico.add(Restrictions.eq("idServico",filialServico.getServico().getIdServico()));
    	
    	return findByDetachedCriteria(dc).size()>0;
    }
        
}