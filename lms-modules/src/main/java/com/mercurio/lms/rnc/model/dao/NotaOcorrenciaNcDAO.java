package com.mercurio.lms.rnc.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaOcorrenciaNcDAO extends BaseCrudDao<NotaOcorrenciaNc, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaOcorrenciaNc.class;
    }

   
    /**
     * Busca todas os registros de NotaOcorrenciaNc que est�o relacionados a OcorrenciaNaoConformidade passada por
     * par�metro.
     * 
     * @param idOcorrenciaNaoConformidade
     * @return
     */
    public List findNotaOcorrenciaNcByOcorrenciaNaoConformidade(java.lang.Long idOcorrenciaNaoConformidade) {
		StringBuffer projecao = new StringBuffer()
			.append("new map(") 
	    	.append("notaOcorrenicaNc.idNotaOcorrenciaNc as idNotaOcorrenciaNc, ")
	    	.append("notaOcorrenicaNc.nrNotaFiscal as nrNotaFiscal, ")
	    	.append("notaFiscalConhecimento.idNotaFiscalConhecimento as notaFiscalConhecimento_idNotaFiscalConhecimento) ");

		StringBuffer from = new StringBuffer()
			.append(NotaOcorrenciaNc.class.getName())
			.append(" as notaOcorrenicaNc ")
			.append("left join notaOcorrenicaNc.notaFiscalConhecimento as notaFiscalConhecimento ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		sql.addCriteria("notaOcorrenicaNc.ocorrenciaNaoConformidade.id", "=", idOcorrenciaNaoConformidade);
		sql.addOrderBy("notaOcorrenicaNc.nrNotaFiscal");
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
}