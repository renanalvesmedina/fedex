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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.fotoProcessoSinistroService"
 */
public class FotoProcessoSinistroService extends CrudService<FotoProcessoSinistro, Long> {


	/**
	 * Recupera uma instância de <code>FotoProcessoSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public FotoProcessoSinistro findById(java.lang.Long id) {
        return (FotoProcessoSinistro)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
    public void removeByIds(List ids) {
    	for (Iterator it = ids.iterator(); it.hasNext(); ) {
    		Long id = (Long)it.next();
    		FotoProcessoSinistro fotoProcessoSinistro = this.findById(id);
    		getFotoProcessoSinistroDAO().removeFotoProcessoSinistro(fotoProcessoSinistro);
    	}
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FotoProcessoSinistro bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFotoProcessoSinistroDAO(FotoProcessoSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
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