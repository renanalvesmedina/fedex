package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.TelefoneContato;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TelefoneContatoDAO extends BaseCrudDao<TelefoneContato, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TelefoneContato.class;
    }

    protected void initFindPaginatedLazyProperties(Map fetchModes) {
        fetchModes.put("telefoneEndereco", FetchMode.JOIN);
        fetchModes.put("telefoneEndereco.pessoa", FetchMode.JOIN);        
        fetchModes.put("contato", FetchMode.SELECT);        
    }
    
    protected void initFindByIdLazyProperties(Map fetchModes) {
        fetchModes.put("telefoneEndereco.pessoa", FetchMode.SELECT);
        fetchModes.put("contato", FetchMode.SELECT);         
    }      


    public List findTelefoneByContatoAndTpUso(Long idContato, String[] tpUso) {
    	ProjectionList pl = Projections.projectionList()
				    	 .add(Projections.property("T.nrDdi"))
				    	 .add(Projections.property("T.nrDdd"))
    					 .add(Projections.property("T.nrTelefone"));
    					 
    	DetachedCriteria dc = DetachedCriteria.forClass(TelefoneContato.class)
    					   .setProjection(pl)
		 				   .createAlias("telefoneEndereco","T")
		 				   .createAlias("contato","C");
	 					   dc.add(Restrictions.in("T.tpUso",tpUso));
		 				   dc.add(Restrictions.eq("C.idContato",idContato));
    	
		 return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    /**
     * Ádamo B. Azambuja
     * Método para pesquisar todos telefones contato de um contato
     * Criado este método devido ao retorno do metodo: findTelefoneByContatoAndTpUso nao retornar um objeto.
     * ver forma de uso do metodo em: VersaoDescritivoPceService.findDataAlertPCE
     * */
    public List findTelefoneByContato(TypedFlatMap criteria) {
    	DetachedCriteria dc = DetachedCriteria.forClass(TelefoneContato.class,"TC")
				.createAlias("TC.contato","CTC")
				.createAlias("TC.telefoneEndereco","TEN") 
				.add(Restrictions.eq("CTC.idContato",criteria.getLong("contato.idContato")))
				.add(Restrictions.eq("TEN.pessoa.idPessoa",criteria.getLong("cliente.idCliente"))	);
				return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

    /**
     * Criado para buscar o TelefoneContato sem os dados já recebidos da tela que o chamou
     *
     * @author José Rodrigo Moraes
     * @since 04/12/2006
     *
     * @param id
     * @return
     */
	public TelefoneContato findByIdCustomized(Long id) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("tc");
		
		sql.addInnerJoin(getPersistentClass().getName(),"tc");
		sql.addInnerJoin("fetch tc.contato","con");
		sql.addInnerJoin("fetch tc.telefoneEndereco","te");
		
		sql.addCriteria("tc.id","=",id);
		
		List <TelefoneContato> retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		TelefoneContato tc = null;
		
		if( retorno != null && !retorno.isEmpty() ){
			tc = (TelefoneContato) retorno.get(0);
		}
		
		return tc;
	}
	
	
	public List<TelefoneContato> findByIdTelefoneEndereco(Long id) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("tc");
		
		sql.addInnerJoin(getPersistentClass().getName(),"tc");
		sql.addInnerJoin("fetch tc.contato","con");
		sql.addInnerJoin("fetch tc.telefoneEndereco","te");		
		sql.addCriteria("te.idTelefoneEndereco","=",id);
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		
	}

}