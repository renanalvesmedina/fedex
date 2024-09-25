package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.entrega.model.DocumentoMir;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DocumentoMirDAO extends BaseCrudDao<DocumentoMir, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DocumentoMir.class;
    }

    /**
     * Método que retora um DocumentoMir a partir do ID do ReciboReembolso
     * @param idReciboReembols
     * @return DocumentoMir
     */
    public DocumentoMir findDocumentoMirByIdReciboReembolso(Long idReciboReembolso, String tpMir) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer()
		.append("select dm ")
		.append("from ")
		.append(DocumentoMir.class.getName()).append(" dm join dm.mir m ")
		.append("where ")
		.append("dm.reciboReembolso.id = ? ");
		args.add(idReciboReembolso);
		if ( tpMir != null ) {
			hql.append("and m.tpMir = ? ");
			args.add(tpMir);
		}

		return (DocumentoMir) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), args.toArray());
    }
    
    /**
     * Método que retora um boolean a partir do ID do ReciboReembolso
     * @param idReciboReembols
     * @return boolean
     */
    public boolean findDocMirByIdReciboReembolso(Long idReciboReembolso) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(DocumentoMir.class.getName() + " dm join fetch dm.mir m ");		
		hql.addCriteria("dm.reciboReembolso.id", "=", idReciboReembolso);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).size()>0;
    }


}