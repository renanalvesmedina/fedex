package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.ImpostoCalculado;
import com.mercurio.lms.tributos.model.dao.ImpostoCalculadoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.impostoCalculadoService"
 */
public class ImpostoCalculadoService extends CrudService<ImpostoCalculado, Long> {

	
	/**
	 * @see findImpostoCalculoInss(Long idPessoa, Date dtBase, String tpImposto, String tpRecolhimento)
	 *@author Robson Edemar Gehl
	 * @param idPessoa
	 * @param dtBase
	 * @param tpImposto
	 * @return
	 */
	public ImpostoCalculado findImpostoCalculoInss(Long idPessoa, YearMonthDay dtBase, String tpImposto){
		return findImpostoCalculoInss(idPessoa, dtBase, tpImposto, null);
	}

	/**
	 * Busca o valor do imposto calculado para o primeiro dia do mes/ano informado.<BR>
	 * O Tipo de Imposto e o Tipo de Recolhimento s�o opcionais para filtro; caso n�o deseja filtrar, informe <i>null</i>.<BR> 
	 *@author Robson Edemar Gehl
	 * @param idPessoa
	 * @param dtBase
	 * @param tpImposto (opcional)
	 * @param tpRecolhimento (opcional)
	 * @return
	 */
	public ImpostoCalculado findImpostoCalculoInss(Long idPessoa, YearMonthDay dtBase, String tpImposto, String tpRecolhimento){
		YearMonthDay dtBaseTmp = new YearMonthDay(dtBase);
		dtBaseTmp = JTDateTimeUtils.setDay(dtBaseTmp,1);
		return getImpostoCalculadoDAO().findImpostoCalculoInss(idPessoa, dtBaseTmp, tpImposto, tpRecolhimento);
	}
	
	
	/**
	 * Recupera uma inst�ncia de <code>ImpostoCalculado</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ImpostoCalculado findById(java.lang.Long id) {
        return (ImpostoCalculado)super.findById(id);
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
    public java.io.Serializable store(ImpostoCalculado bean) {
        return super.store(bean);
    }

    /**
     * Exclui ImpostoCalculado de acordo com o idPessoa e a dtCompetenciaAnoMes.
     *
     * @author Hector Julian Esnaola Junior
     * @since 26/10/2007
     *
     * @param idPessoa
     * @param dtCompetenciaAnoMes
     * @return
     *
     */
    public Boolean removeByPessoaDtCompetencia(
    		Long idPessoa, 
    		YearMonthDay dtCompetenciaAnoMes) {
    	return getImpostoCalculadoDAO().removeByPessoaDtCompetencia(
    			idPessoa,
    			dtCompetenciaAnoMes);
    }
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setImpostoCalculadoDAO(ImpostoCalculadoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ImpostoCalculadoDAO getImpostoCalculadoDAO() {
        return (ImpostoCalculadoDAO) getDao();
    }
    
}