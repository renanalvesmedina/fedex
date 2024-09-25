package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.DiaFaturamento;
import com.mercurio.lms.vendas.model.dao.DiaFaturamentoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.diaFaturamentoService"
 */
public class DiaFaturamentoService extends CrudService<DiaFaturamento, Long> {

	/**
	 * Recupera uma inst�ncia de <code>DiaFaturamento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DiaFaturamento findById(java.lang.Long id) {
		return (DiaFaturamento)super.findById(id);
	}

	/**
	 * Retorna a lista de dia de faturamento da divisao informada no tipo de periodicidade informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/10/2006
	 * 
	 * @param Long idDivisao
	 * @param String tpPeriodicidade
	 * @return DiaFaturamento
	 */
	public List findByDivisaoTpPeriodicidade(Long idDivisao, String tpPeriodicidade){
		return getDiaFaturamentoDAO().findByDivisaoTpPeriodicidade(idDivisao, tpPeriodicidade);
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
	public java.io.Serializable store(DiaFaturamento bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDiaFaturamentoDAO(DiaFaturamentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DiaFaturamentoDAO getDiaFaturamentoDAO() {
		return (DiaFaturamentoDAO) getDao();
	}

    /**
     * Solicita��o CQPRO00005945/CQPRO00007478 da Integra��o.
     * @param tpModal
     * @param tpPeriodicidade
     * @param idDivisaoCliente
     * @return
     */
    public DiaFaturamento findDiaFaturamento(Long idDivisaoCliente, String tpModal) {
    	return getDiaFaturamentoDAO().findDiaFaturamento(idDivisaoCliente, tpModal);
    }

    /**
     * Retorna os Dias de Faturamento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<DiaFaturamento> findDiaFaturamento(Long idCliente) {
    	return getDiaFaturamentoDAO().findDiaFaturamento(idCliente);
    }
    
    /**
     * Retorna uma lista mapeada com os Dias de Faturamento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findDiaFaturamentoMapped(Long idCliente) {
    	return getDiaFaturamentoDAO().findDiaFaturamentoMapped(idCliente);
    }
    
    /**
     * Retorna os dias de  faturamento que possuem tpPeriodicidadeSolicitado
     * n�o nulo
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findDiaFaturamentoSolicitado(Long idCliente) {
    	return getDiaFaturamentoDAO().findDiaFaturamentoSolicitado(idCliente);
    }
    
    /**
     * Retorna os dias de  faturamento que possuem tpPeriodicidadeSolicitado
     * n�o nulo
     * @param idCliente
     * @return
     */
    public List<DiaFaturamento> findDiaFaturamentoByDivisao(Long idDivisaoCliente) {
    	return getDiaFaturamentoDAO().findDiaFaturamentoByDivisao(idDivisaoCliente);
    }
    
    /**
     * Retorna os dias de  faturamento que possuem tpPeriodicidadeSolicitado
     * n�o nulo
     * @param idCliente
     * @return
     */
    public TypedFlatMap findTabelaByIdServico(Long idServico) {
    	return getDiaFaturamentoDAO().findTabelaByIdServico(idServico);
    }
    
}