package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.DiaSemana;
import com.mercurio.lms.configuracoes.model.dao.DiaSemanaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.diaSemanaService"
 */
public class DiaSemanaService extends CrudService<DiaSemana, Long> {


	/**
	 * Recupera uma instância de <code>DiaSemana</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DiaSemana findById(java.lang.Long id) {
        return (DiaSemana)super.findById(id);
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

    
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	/**
	 * Retorna o registro de dia semana do municipio informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param Long idMunicipio
	 * @return DiaSemana
	 */
	public DiaSemana findByMunicipio(Long idMunicipio) {
		List lstDiaSemana = getDiaSemanaDAO().findByCriterio(idMunicipio);
		
		if (lstDiaSemana != null && !lstDiaSemana.isEmpty()) {
			return (DiaSemana)lstDiaSemana.get(0);
		} else {
			return null;
		}
	}	
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * Insere para os sete dias da semana (1 a 7, domingo a sábado respectivamente).
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(DiaSemana bean) {
    	return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDiaSemanaDAO(DiaSemanaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DiaSemanaDAO getDiaSemanaDAO() {
        return (DiaSemanaDAO) getDao();
    }

   }