package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.NotaFiscalColeta;
import com.mercurio.lms.configuracoes.model.Contato;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalColetaDAO extends BaseCrudDao<NotaFiscalColeta, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalColeta.class;
    }

    
    public void removeByIdDetalheColeta(Serializable idDetalheColeta) {
        String sql = "delete from " + NotaFiscalColeta.class.getName() + " as nfc" +
        			 " where " +
        			 " nfc.detalheColeta.id = :id";
        
        getAdsmHibernateTemplate().removeById(sql, idDetalheColeta);
    }    
    
    @SuppressWarnings("unchecked")
	public List<Contato> findContatosFromNotaFiscalColeta(String nrChave) {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT co ");
    	sql.append(" FROM ").append(NotaFiscalColeta.class.getName()).append(" n ");
    	sql.append(" , ").append(Contato.class.getName()).append(" co ");
    	sql.append(" JOIN n.detalheColeta d ");
    	sql.append(" JOIN d.cliente c ");
    	sql.append(" WHERE c.id = co.pessoa.id ");
    	sql.append(" AND n.nrChave = '").append(nrChave).append("'");
    	sql.append(" and co.tpContato in ('AC', 'AD') ");
    	return (List<Contato>) this.getAdsmHibernateTemplate().find(sql.toString());
    }
    
}