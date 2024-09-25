package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.DiaSemana;
import com.mercurio.lms.configuracoes.model.dao.DiaSemanaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.diaSemanaService"
 */
public class DiaSemanaService extends CrudService<DiaSemana, Long> {


	/**
	 * Recupera uma inst�ncia de <code>DiaSemana</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public DiaSemana findById(java.lang.Long id) {
        return (DiaSemana)super.findById(id);
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

    
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	/**
	 * Retorna o registro de dia semana do municipio informado.
	 * 
	 * @author Micka�l Jalbert
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * Insere para os sete dias da semana (1 a 7, domingo a s�bado respectivamente).
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(DiaSemana bean) {
    	return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setDiaSemanaDAO(DiaSemanaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private DiaSemanaDAO getDiaSemanaDAO() {
        return (DiaSemanaDAO) getDao();
    }

   }