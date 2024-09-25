package com.mercurio.lms.rnc.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.rnc.model.dao.NotaOcorrenciaNcDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.notaOcorrenciaNcService"
 */
public class NotaOcorrenciaNcService extends CrudService<NotaOcorrenciaNc, Long> {


	/**
	 * Recupera uma inst�ncia de <code>NotaOcorrenciaNc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public NotaOcorrenciaNc findById(java.lang.Long id) {
        return (NotaOcorrenciaNc)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(NotaOcorrenciaNc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setNotaOcorrenciaNcDAO(NotaOcorrenciaNcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private NotaOcorrenciaNcDAO getNotaOcorrenciaNcDAO() {
        return (NotaOcorrenciaNcDAO) getDao();
    }
    
    
	/**
     * Busca todas os registros de NotaOcorrenciaNc que est�o relacionados a OcorrenciaNaoConformidade passada por
     * par�metro.
	 * 
	 * @param idOcorrenciaNaoConformidade
	 * @return
	 */
	public List findNotaOcorrenciaNcByOcorrenciaNaoConformidade(java.lang.Long idOcorrenciaNaoConformidade) {
		List lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(getNotaOcorrenciaNcDAO().findNotaOcorrenciaNcByOcorrenciaNaoConformidade(idOcorrenciaNaoConformidade));
		return lista;
	}
}