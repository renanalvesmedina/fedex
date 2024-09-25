package com.mercurio.lms.vol.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vol.model.VolContatos;
import com.mercurio.lms.vol.model.VolEmailsRecusa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolEmailsRecusaDAO extends BaseCrudDao<VolEmailsRecusa, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolEmailsRecusa.class;
    }

    public List<VolContatos> findContatoByIdRecusa(Long idVolRecusas){       	  
		StringBuilder sql = new StringBuilder();
		sql.append("select re.volContatos from "+VolEmailsRecusa.class.getName()+" re" +
				" where re.volRecusa.idRecusa=?");
		return this.getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idVolRecusas});	
   

    }
}

