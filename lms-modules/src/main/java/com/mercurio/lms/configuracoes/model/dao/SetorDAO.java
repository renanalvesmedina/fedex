package com.mercurio.lms.configuracoes.model.dao;

import org.hibernate.Query;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.configuracoes.model.Usuario;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SetorDAO extends BaseCrudDao<Setor, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected Class getPersistentClass() {
        return Setor.class;
    }

    /**
     * Busca o setor de um usu�rio.
     * @param usuario
     * @return
     * @deprecated N�o mais utilizado. Setor do usu�rio n�o estar� no RH.
     */
    public Setor findSetorByUsuario(Usuario usuario) {

		StringBuffer sb = new StringBuffer()
		.append(" select new " + Setor.class.getName() + "(s.idSetor, s.dsSetor)")
		.append(" from " + Setor.class.getName() + " as s")
		.append(", " + Funcionario.class.getName() + " as f")
		.append(" where s.cdSetorRh = f.cdSetor and f.usuario.idUsuario = ?");
		
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString());
		q.setParameter(0, usuario.getIdUsuario());
		return (Setor) q.uniqueResult();
	}
}