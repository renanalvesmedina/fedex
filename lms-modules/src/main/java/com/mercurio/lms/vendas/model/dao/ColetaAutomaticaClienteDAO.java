package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.ColetaAutomaticaCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ColetaAutomaticaClienteDAO extends BaseCrudDao<ColetaAutomaticaCliente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ColetaAutomaticaCliente.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
	   lazyFindById.put("enderecoPessoa",FetchMode.JOIN);
	   lazyFindById.put("enderecoPessoa.tipoLogradouro", FetchMode.JOIN);
       lazyFindById.put("servico",FetchMode.JOIN);
       lazyFindById.put("naturezaProduto",FetchMode.JOIN);
	}
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
        lazyFindPaginated.put("servico",FetchMode.JOIN);
        lazyFindPaginated.put("naturezaProduto",FetchMode.JOIN);
    }
    
    /**
     * Retorna as coletas automáticas de todos os clientes com coletas no diaSemana passado
     * por parâmetro (Domingo = 1, Segunda-feira = 2, ...) e que a filial que atende 
     * operacional seja igual à filial passada por parâmetro.
     * @param filial
     * @param diaSemana
     * @return List: Coletas automáticas de clientes.
     */
    public List findColetasAutomaticasClienteByDiaSemanaAndFilialAtendeOperacional(Filial filial, int diaSemana) {
        DetachedCriteria detColAut = DetachedCriteria.forClass(ColetaAutomaticaCliente.class)
        	.add(Restrictions.or(
        		Restrictions.eq("tpDiaSemana", Integer.valueOf(diaSemana).toString()),
        		Restrictions.isNull("tpDiaSemana")
        	))
        	.createAlias("cliente","c")
        	.createAlias("c.filialByIdFilialAtendeOperacional","f")
        	.add(Restrictions.eq("f.id", filial.getIdFilial()));
        return super.findByDetachedCriteria(detColAut);
    }
    
    /**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE cliente.id = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }
}