package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.dao.PromotorClienteDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.promotorClienteService"
 */
public class PromotorClienteService extends CrudService<PromotorCliente, Long> {

	/**
	 * Recupera uma inst�ncia de <code>PromotorCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public PromotorCliente findById(java.lang.Long id) {
		return (PromotorCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
     * M�todo utilizado pela Integra��o
	 * @author Andre Valadas
	 * 
     * @param idCliente
     * @param idUsuario
     * @return <PromotorCliente>
     */
	public PromotorCliente findPromotorCliente(Long idCliente, Long idUsuario) {
		return getPromotorClienteDAO().findPromotorCliente(idCliente, idUsuario);
	}
	
	/**
     * M�todo utilizado pela Integra��o
	 * @author Anibal Maffioletti de Deus
	 * 
     * @param idCliente
     * @return List<PromotorCliente>
     */
	public List<PromotorCliente> findPromotorCliente(Long idCliente) {
		return getPromotorClienteDAO().findPromotorCliente(idCliente);
	}
	
	/**
	 * Obtem lista de Promotor cliente atrav�s do idCliente
	 * @param idCliente
	 * @return
	 */
	public List<PromotorCliente> findPromotorClienteByIdCliente(Long idCliente) {
		return getPromotorClienteDAO().findPromotorClienteByIdCliente(idCliente);
	}
	
	public List<PromotorCliente> findByIdClienteAndTpModalAndDtAtual(Long idCliente, String tpModal, DateTime dtAtual) {
		return getPromotorClienteDAO().findByIdClienteAndTpModalAndDtAtual(idCliente, tpModal, dtAtual);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected PromotorCliente beforeStore(PromotorCliente bean) {
		Cliente cliente = bean.getCliente();

		Long idCliente = cliente.getIdCliente();
		DomainValue tpModal = bean.getTpModal();
		DomainValue tpAbrangencia = bean.getTpAbrangencia();

		/** Valida Modal e Abrangencia */
		Integer nbrRow = getPromotorClienteDAO().verifyModalAbrangencia(idCliente, tpModal, tpAbrangencia);
		if (IntegerUtils.gtZero(nbrRow)) {
			throw new BusinessException("LMS-01151");
		}

		YearMonthDay dtInicioPromotor = bean.getDtInicioPromotor(); 
		/** Valida data vigencia do promotor por sua filial regiao */
		Usuario promotor = getPromotorClienteDAO().findUsuarioRegionalFilialVigente(bean.getUsuario().getIdUsuario(), dtInicioPromotor);
		if (promotor == null) {
			throw new BusinessException("LMS-01152");
		}

		
		/** Verificar se o promotor que est� sendo cadastrado j� possui um registro com o mesmo 
		 *  modal, mesma abrang�ncia e vigente para o cliente pesquisado 
		**/
		List<PromotorCliente> listPromotorCliente = getPromotorClienteDAO().findPromotorCliente(bean.getIdPromotorCliente(), idCliente, bean.getUsuario().getIdUsuario(), tpModal, tpAbrangencia);
		if (listPromotorCliente != null) {
			for (PromotorCliente promotorCliente : listPromotorCliente) {
				if (promotorCliente.getDtFimPromotor() == null 
						|| bean.getDtInicioPromotor().isBefore(promotorCliente.getDtFimPromotor()) 
						|| bean.getDtInicioPromotor().equals(promotorCliente.getDtFimPromotor())) {
					throw new BusinessException("LMS-00003");
				}
			}
		}
		
		/** Validar dtInicioPromotor */
		YearMonthDay dtGeracao = getPromotorClienteDAO().findClienteDtGeracao(idCliente);
		try{
			if (dtInicioPromotor.isBefore(cliente.getPessoa().getDhInclusao().toYearMonthDay())) {
				throw new BusinessException("LMS-01015");
			}
			
			YearMonthDay dtReconquista = bean.getDtReconquista();
			/** Validar dtReconquista */
			if (dtReconquista != null) {
				if (dtReconquista.isBefore(dtInicioPromotor) || dtReconquista.equals(dtInicioPromotor)) {
					throw new BusinessException("LMS-01016");
				}
			}
		}catch(NullPointerException npe){
			//apenas para o caso da dtInicioPromotor vier nula, nao faz nada			
		}
		
		try{
			if (bean.getPcReconquista().doubleValue() < 0 || bean.getPcReconquista().doubleValue() > 2){
				throw new BusinessException("LMS-01172");
			}
		}catch (NullPointerException npe){}

		// Setar dtPrimeiroPromotor   
		YearMonthDay dtPrimeiroPromotor = bean.getDtPrimeiroPromotor();
		if (dtPrimeiroPromotor == null) {
			if ((dtPrimeiroPromotor = getPromotorClienteDAO().findDtPrimeiroPromotor(idCliente)) != null) {
				bean.setDtPrimeiroPromotor(dtPrimeiroPromotor); 	
			} else {
				bean.setDtPrimeiroPromotor(dtInicioPromotor);	
			}
		}
		return super.beforeStore(bean);
	}

	public ResultSetPage findPaginatedOtimizado(TypedFlatMap criteria) {
		return getPromotorClienteDAO().findPaginatedOtimizado(criteria);
	}

	public Integer getRowCountOtimizado(TypedFlatMap criteria) {
		return getPromotorClienteDAO().getRowCountOtimizado(criteria);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
	public PromotorCliente store(PromotorCliente bean) {
		super.store(bean);
		return bean;
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setPromotorClienteDAO(PromotorClienteDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private PromotorClienteDAO getPromotorClienteDAO() {
		return (PromotorClienteDAO) getDao();
	}

}
