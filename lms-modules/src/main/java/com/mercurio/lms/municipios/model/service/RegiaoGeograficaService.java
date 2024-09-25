package com.mercurio.lms.municipios.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.RegiaoGeografica;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.RegiaoGeograficaDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.regiaoGeograficaService"
 */
public class RegiaoGeograficaService extends CrudService<RegiaoGeografica, Long> {


	/**
	 * Recupera uma instância de <code>RegiaoGeografica</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RegiaoGeografica findById(java.lang.Long id) {
        return (RegiaoGeografica)super.findById(id);
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
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(RegiaoGeografica bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRegiaoGeograficaDAO(RegiaoGeograficaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RegiaoGeograficaDAO getRegiaoGeograficaDAO() {
        return (RegiaoGeograficaDAO) getDao();
    }
    
	public List checkRegiaoCentroOesteNorteByIdUnidadeFederativa(UnidadeFederativa uf) {
		if (uf != null){
			return getRegiaoGeograficaDAO().checkRegiaoCentroOesteNorteByIdFilial(uf);
    }
		return null;
	}
	
	
	public List<RegiaoGeografica> findByPaisSessao(){
	    return getRegiaoGeograficaDAO().findByPais(SessionUtils.getPaisSessao());
	}
   }