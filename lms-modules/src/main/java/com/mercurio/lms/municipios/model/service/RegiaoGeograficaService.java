package com.mercurio.lms.municipios.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.RegiaoGeografica;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.RegiaoGeograficaDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.regiaoGeograficaService"
 */
public class RegiaoGeograficaService extends CrudService<RegiaoGeografica, Long> {


	/**
	 * Recupera uma inst�ncia de <code>RegiaoGeografica</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public RegiaoGeografica findById(java.lang.Long id) {
        return (RegiaoGeografica)super.findById(id);
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
    public java.io.Serializable store(RegiaoGeografica bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRegiaoGeograficaDAO(RegiaoGeograficaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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