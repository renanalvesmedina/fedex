package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ControleFormImpressora;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.dao.ControleFormImpressoraDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.controleFormImpressoraService"
 */
public class ControleFormImpressoraService extends CrudService<ControleFormImpressora, Long> {

	/**
	 * Recupera uma instância de <code>ControleFormImpressora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ControleFormImpressora findById(java.lang.Long id) {
		return (ControleFormImpressora)super.findById(id);
	}

	public List<ControleFormImpressora> findByControleFormulario(java.lang.Long idControleFormulario){
		return getControleFormImpressoraDAO().findByControleFormulario(idControleFormulario);
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
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	protected ControleFormImpressora beforeStore(ControleFormImpressora bean) {
		// valida Formulários
		validarFormularios(bean);
		// valida Série
		validarSerie(bean);
		// valida Selo Fiscal
		validarSeloFiscal(bean);
		// valida Última Impressão
		validarUltimaImpressao(bean);

		return bean;
	}

	/**
	 * Verifica se o intervalo de formulários está contido em algum vínculo com impressoras do Controle de Formulário informado.
	 * @param list Coleção de ControleFormImpressora
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 */
	public void verificarIntervaloFormByControleForm(ControleFormulario cf){
		List<ControleFormImpressora> list = findByControleFormulario(cf.getIdControleFormulario());
		for(ControleFormImpressora cfi : list) {
			if ( (cfi.getNrFormularioInicial().compareTo(cf.getNrFormularioInicial()) <= 0 
					&& cfi.getNrFormularioFinal().compareTo(cf.getNrFormularioInicial()) >= 0)
				|| (cfi.getNrFormularioInicial().compareTo(cf.getNrFormularioFinal()) <= 0
						&& cfi.getNrFormularioFinal().compareTo(cf.getNrFormularioFinal()) >= 0)){
				throw new BusinessException("LMS-00016");
			}
		}
	}

	/**
	 * Verifica se o intervalo de formulários está contido em algum vínculo com impressoras.
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 */
	public void verificarIntervaloForm(Long nrFormularioInicial, Long nrFormularioFinal){
		if (!getControleFormImpressoraDAO().verificaIntervaloForm(nrFormularioInicial, nrFormularioFinal)){
			throw new BusinessException("LMS-00017");
		}
	}
	
	/**
	 * Verifica se o intervalo dos Formulários informados está contido no intervalo de Formulários do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora Vínculo dos Formulários com Impressoras
	 * @throws BusinessException Formulários informados não estão contidos no intervalo de Formulários do Controle de Estoque selecionado
	 */
	private void validarFormularios(ControleFormImpressora controleFormImpressora) {
		long nrFormularioInicial = controleFormImpressora.getNrFormularioInicial().longValue();
		long nrFormularioFinal = controleFormImpressora.getNrFormularioFinal().longValue();
		long nrCrtlFormInicial = controleFormImpressora.getControleFormulario().getNrFormularioInicial().longValue();
		long nrCrtlFormFinal = controleFormImpressora.getControleFormulario().getNrFormularioFinal().longValue();

		boolean result = ( (nrFormularioInicial >= nrCrtlFormInicial) && (nrFormularioFinal <= nrCrtlFormFinal) );
		if(!result) {
			throw new BusinessException("LMS-00018");
		}
	}

	/**
	 * Verifica se a Série informada é a mesma do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora Vínculo dos Formulários com Impressoras
	 * @throws BusinessException Série informada não é a mesma do Controle de Estoque selecionado
	 */
	private void validarSerie(ControleFormImpressora controleFormImpressora) {
		boolean result = controleFormImpressora.getDsSerie().equals(controleFormImpressora.getControleFormulario().getCdSerie());
		if(!result) {
			throw new BusinessException("Série inválida.");
		}
	}

	/**
	 * Verifica se o Selo Fiscal está contido no intervalo de Selos Fiscais do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora Vínculo dos Formulários com Impressoras
	 * @throws BusinessException Selo Fiscal informado não está contido no intervalo de Selos Fiscais do Controle de Estoque selecionado
	 */
	private void validarSeloFiscal(ControleFormImpressora controleFormImpressora) {
		long nrSeloFiscal = controleFormImpressora.getNrSeloFiscalInicial().longValue();
		long nrSeloFiscalInicial = controleFormImpressora.getControleFormulario().getNrSeloFiscalInicial().longValue();
		long nrSeloFiscalFinal = controleFormImpressora.getControleFormulario().getNrSeloFiscalFinal().longValue();

		boolean result = ( (nrSeloFiscal >= nrSeloFiscalInicial) && (nrSeloFiscal <= nrSeloFiscalFinal) );
		if(!result) {
			throw new BusinessException("Selo Fiscal inválido.");
		}
	}

	/**
	 * Verifica se o Número da Última Impressão está contido no intervalo de Formulários informado
	 * 
	 * @param controleFormImpressora Vínculo dos Formulários com Impressoras
	 * @throws BusinessException Número da Última Impressão não está contido no intervalo de Formulários informado
	 */
	private void validarUltimaImpressao(ControleFormImpressora controleFormImpressora) {
		long nrUltimaImpressao = controleFormImpressora.getNrUltimaImpressao().longValue();
		long nrFormularioInicial = controleFormImpressora.getNrFormularioInicial().longValue();
		long nrFormularioFinal = controleFormImpressora.getNrFormularioFinal().longValue();

		boolean result = ( (nrUltimaImpressao >= nrFormularioInicial) && (nrUltimaImpressao <= nrFormularioFinal) );
		if(!result) {
			throw new BusinessException("Última Impressão inválida.");
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ControleFormImpressora bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setControleFormImpressoraDAO(ControleFormImpressoraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ControleFormImpressoraDAO getControleFormImpressoraDAO() {
		return (ControleFormImpressoraDAO) getDao();
	}

}