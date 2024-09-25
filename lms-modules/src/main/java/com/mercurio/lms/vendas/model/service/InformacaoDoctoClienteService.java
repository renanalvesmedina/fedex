package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.dao.InformacaoDoctoClienteDAO;

/**
 * Classe de serviço para CRUD: 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.informacaoDoctoClienteService"
 */
public class InformacaoDoctoClienteService extends CrudService<InformacaoDoctoCliente, Long> {
	
	/**
	 * Recupera uma instância de <code>InformacaoDctoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public InformacaoDoctoCliente findById(java.lang.Long id) {
		return (InformacaoDoctoCliente)super.findById(id);
	}

	public InformacaoDoctoCliente findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (InformacaoDoctoCliente) super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param dsCampo
	 * @return InformacaoDoctoCliente
	 */
	public InformacaoDoctoCliente findInformacaoDoctoCliente(Long idCliente, String dsCampo) {
		return getInformacaoDctoClienteDAO().findInformacaoDoctoCliente(idCliente, dsCampo);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(InformacaoDoctoCliente bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setInformacaoDctoClienteDAO(InformacaoDoctoClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private InformacaoDoctoClienteDAO getInformacaoDctoClienteDAO() {
		return (InformacaoDoctoClienteDAO) getDao();
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected InformacaoDoctoCliente beforeStore(InformacaoDoctoCliente bean) {
		if(bean.getTpCampo().getValue().equals("A")) {
			if( (bean.getDsFormatacao() != null && bean.getNrTamanho() != null)
					|| (bean.getDsFormatacao() == null && bean.getNrTamanho() == null)
			) {
				throw new BusinessException("LMS-01001");
			}
		} else if(bean.getTpCampo().getValue().equals("N")) {
			if( (bean.getDsFormatacao() != null && bean.getNrTamanho() != null)
					|| (bean.getDsFormatacao() == null && bean.getNrTamanho() == null)
			) {
				throw new BusinessException("LMS-01002");
			}
		} else if(bean.getTpCampo().getValue().equals("D")) {
			if( bean.getDsFormatacao() == null ) {
				throw new BusinessException("LMS-01003");
			}
		}
		return super.beforeStore(bean);
	}

	public Integer getRowCountByClienteTpModalTpAbrangenciaByBlRemetente(Long idCliente, String tpModal, String tpAbrangencia, Boolean blRemetente) {
		return getInformacaoDctoClienteDAO().getRowCountByClienteTpModalTpAbrangenciaByBlRemetente(idCliente, tpModal, tpAbrangencia, blRemetente);
	}

	public Integer getRowCountByClienteTpModalTpAbrangenciaByBlDestinatario(Long idCliente, String tpModal, String tpAbrangencia, Boolean blDestinatario) {
		return getInformacaoDctoClienteDAO().getRowCountByClienteTpModalTpAbrangenciaByBlDestinatario(idCliente, tpModal, tpAbrangencia, blDestinatario);
	}

	public Integer getRowCountByClienteTpModalTpAbrangenciaByBlDevedor(Long idCliente, String tpModal, String tpAbrangencia, Boolean blDevedor) {
		return getInformacaoDctoClienteDAO().getRowCountByClienteTpModalTpAbrangenciaByBlDevedor(idCliente, tpModal, tpAbrangencia, blDevedor);
	}
	
	public List findDadosByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia, Boolean blRemetente, Boolean blDestinatario, Boolean blDevedor) {
		return getInformacaoDctoClienteDAO().findDadosByClienteTpModalTpAbrangencia(idCliente, tpModal, tpAbrangencia, blRemetente, blDestinatario, blDevedor);
	}
	
	public List<Map<String, Object>> findDadosByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia) {
		return getInformacaoDctoClienteDAO().findDadosByClienteTpModalTpAbrangencia(idCliente,tpModal,tpAbrangencia);
	}
	public List<Map> findDadosByCliente(Long idCliente, String descricaoCampo) {
		return getInformacaoDctoClienteDAO().findDadosByCliente(idCliente,descricaoCampo);
	}

	public List<Map<String, Object>> findDadosByCliente(Long idCliente) {
		return getInformacaoDctoClienteDAO().findDadosByCliente(idCliente);
	}	
	
	public List<Long> findIdsByClienteTpModalTpAbrangenciaBlOpcional(Long idCliente, String tpModal, String tpAbrangencia, Boolean blOpcional) {
		return getInformacaoDctoClienteDAO().findIdsByClienteTpModalTpAbrangenciaBlOpcional(idCliente, tpModal, tpAbrangencia, blOpcional);
	}

	public InformacaoDoctoCliente findByIdClienteAndDsCampo(Long idCliente, String dsCampo) {
		return getInformacaoDctoClienteDAO().findByIdClienteAndDsCampo(idCliente, dsCampo);
	}
	
	// LMSA-6520
	public InformacaoDoctoCliente findByNrIdentificacaoClienteAndDsCampo(String nrIdentificacao, String dsCampo) {
		return getInformacaoDctoClienteDAO().findByNrIdentificacaoClienteAndDsCampo(nrIdentificacao, dsCampo);
	}
	
	public InformacaoDoctoCliente findByIdInformacaoDoctoClienteAndDsCampo(Long id, String dsCampo) {
		return getInformacaoDctoClienteDAO().findByIdInformacaoDoctoClienteAndDsCampo(id, dsCampo);
	}


}