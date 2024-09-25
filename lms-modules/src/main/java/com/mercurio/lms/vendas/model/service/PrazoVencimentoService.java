package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.param.DataVencimentoParam;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.dao.PrazoVencimentoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.prazoVencimentoService"
 */
public class PrazoVencimentoService extends CrudService<PrazoVencimento, Long> {
	private DiaVencimentoService diaVencimentoService;
	private DivisaoClienteService divisaoClienteService;

	/**
	 * Recupera uma inst�ncia de <code>PrazoVencimento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public PrazoVencimento findById(java.lang.Long id) {
		PrazoVencimento retorno = getPrazoVencimentoDAO().findById(id);
		retorno.getDiasVencimento();
		return retorno;
	}

    /**
     * Solicita��o CQPRO00005946/CQPRO00007479 da Integra��o.
     * @param tpFrete
     * @param tpModal
     * @param nrPrazoPagamento
     * @param idDivisaoCliente
     * @return
     */
    public PrazoVencimento findPrazoVencimento(Long idDivisaoCliente, String tpFrete, String tpModal) {
    	return getPrazoVencimentoDAO().findPrazoVencimento(idDivisaoCliente, tpFrete, tpModal);
    }

    public PrazoVencimento findPrazoVencimento(Long idDivisaoCliente, String tpModal) {
    	return getPrazoVencimentoDAO().findPrazoVencimento(idDivisaoCliente, null, tpModal);
    }

    /**
     * Retorna os Prazos de Vencimento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<PrazoVencimento> findPrazoVencimento(Long idCliente) {
    	return getPrazoVencimentoDAO().findPrazoVencimento(idCliente);
    }

    /**
     * Retorna a lista de PrazoVencimento que possuem nrPrazoPagamentoSolicitado diferente de nulo
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findPrazoVencimentoSolicitado(Long idCliente) {
    	return getPrazoVencimentoDAO().findPrazoVencimentoSolicitado(idCliente);
    }

    /**
     * Retorna uma lista mapeada com os Prazos de Vencimento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findPrazoVencimentoMapped(Long idCliente) {
    	return getPrazoVencimentoDAO().findPrazoVencimentoMapped(idCliente);
    }

	/**
	 * Retorna o PrazoVencimento da divis�o informada por tpModal, tpAbrangencia e tpFrete.
	 * 
	 * @param Long idDivisaoCliente
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * @param String tpFrete
	 * @return PrazoVencimento
	 * */
	public PrazoVencimento findPrazoVencimentoByCriteria(DataVencimentoParam dataVencimentoParam){
		PrazoVencimento prazoVencimento = null;
		
		Object[] obj = getPrazoVencimentoDAO().findPrazoVencimentoByCriteria(dataVencimentoParam);
		
		if (obj != null) {
			prazoVencimento = new PrazoVencimento();
			prazoVencimento.setIdPrazoVencimento((Long)obj[0]);
			prazoVencimento.setNrPrazoPagamento(((Long)obj[1]).shortValue());
			prazoVencimento.setTpDiaSemana(new DomainValue(String.valueOf(obj[2])));
		}
		
		return prazoVencimento;
	}

	/**
	 * Retorna o PrazoVencimento da divis�o informada onde os outros campos s�o nulos.
	 * 
	 * @param Long idDivisaoCliente
	 * @return PrazoVencimento
	 * */
	public PrazoVencimento findPrazoVencimentoPadrao(Long idDivisao){
		List lstPrazoVencimento = getPrazoVencimentoDAO().findPrazoVencimentoPadrao(idDivisao);
		
		if (!lstPrazoVencimento.isEmpty()){
			return (PrazoVencimento) lstPrazoVencimento.get(0);
		} else {
			return null;
		}
	}    

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		getPrazoVencimentoDAO().removeById(id, true);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		getPrazoVencimentoDAO().removeByIds(ids, true);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids, boolean loadBeforeDelete) {
		getPrazoVencimentoDAO().removeByIds(ids, loadBeforeDelete);
	}

	@Override
	protected PrazoVencimento beforeStore(PrazoVencimento bean) {

		PrazoVencimento prazoVencimento = ((PrazoVencimento)bean);

		/** Exclui os prazos de vencimento */
		if(bean != null && prazoVencimento.getIdPrazoVencimento() != null){
			diaVencimentoService.removeByIdPrazoVencimento(prazoVencimento.getIdPrazoVencimento(), Boolean.TRUE);
		}
		return super.beforeStore(bean);
	}

	public TypedFlatMap findFilialNrPrazoCobranca(Long idDivisaoCliente) {
		
		DivisaoCliente divisaoCliente = divisaoClienteService.findById(idDivisaoCliente);
		Cliente cliente = divisaoCliente.getCliente();
		Filial filialCobranca = cliente.getFilialByIdFilialCobranca();

		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("nrPrazoCobranca", filialCobranca.getNrPrazoCobranca());
		return toReturn;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PrazoVencimento bean) {
        return super.store(bean);
    }

	/** 
	 * STORE especifico para aprova��o de Analise de Credito, evitando outras regras de negocio
	 * @param bean
	 */
	public void storeBasic(PrazoVencimento bean) {
        getPrazoVencimentoDAO().storeBasic(bean);
    }

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @param Inst�ncia do DAO.
	 */
	public void setPrazoVencimentoDAO(PrazoVencimentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @return Inst�ncia do DAO.
	 */
	private PrazoVencimentoDAO getPrazoVencimentoDAO() {
		return (PrazoVencimentoDAO) getDao();
	}

	public ResultSetPage findPaginated(Map criteria) {
		return getPrazoVencimentoDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
	}

	public void setDiaVencimentoService(DiaVencimentoService diaVencimentoService) {
		this.diaVencimentoService = diaVencimentoService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

}