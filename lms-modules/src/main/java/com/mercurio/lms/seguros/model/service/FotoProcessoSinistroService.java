package com.mercurio.lms.seguros.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.FotoProcessoSinistro;
import com.mercurio.lms.seguros.model.dao.FotoProcessoSinistroDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.fotoProcessoSinistroService"
 */
public class FotoProcessoSinistroService extends CrudService<FotoProcessoSinistro, Long> {


	/**
	 * Recupera uma inst�ncia de <code>FotoProcessoSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public FotoProcessoSinistro findById(java.lang.Long id) {
        return (FotoProcessoSinistro)super.findById(id);
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
	 */
    public void removeByIds(List ids) {
    	for (Iterator it = ids.iterator(); it.hasNext(); ) {
    		Long id = (Long)it.next();
    		FotoProcessoSinistro fotoProcessoSinistro = this.findById(id);
    		getFotoProcessoSinistroDAO().removeFotoProcessoSinistro(fotoProcessoSinistro);
    	}
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FotoProcessoSinistro bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setFotoProcessoSinistroDAO(FotoProcessoSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private FotoProcessoSinistroDAO getFotoProcessoSinistroDAO() {
        return (FotoProcessoSinistroDAO) getDao();
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
    	return getFotoProcessoSinistroDAO().findPaginatedCustom(FindDefinition.createFindDefinition(tfm), tfm);
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getFotoProcessoSinistroDAO().getRowCountCustom(tfm);
    }

   }