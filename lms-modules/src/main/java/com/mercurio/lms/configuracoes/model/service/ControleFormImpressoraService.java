package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ControleFormImpressora;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.dao.ControleFormImpressoraDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.controleFormImpressoraService"
 */
public class ControleFormImpressoraService extends CrudService<ControleFormImpressora, Long> {

	/**
	 * Recupera uma inst�ncia de <code>ControleFormImpressora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ControleFormImpressora findById(java.lang.Long id) {
		return (ControleFormImpressora)super.findById(id);
	}

	public List<ControleFormImpressora> findByControleFormulario(java.lang.Long idControleFormulario){
		return getControleFormImpressoraDAO().findByControleFormulario(idControleFormulario);
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
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	protected ControleFormImpressora beforeStore(ControleFormImpressora bean) {
		// valida Formul�rios
		validarFormularios(bean);
		// valida S�rie
		validarSerie(bean);
		// valida Selo Fiscal
		validarSeloFiscal(bean);
		// valida �ltima Impress�o
		validarUltimaImpressao(bean);

		return bean;
	}

	/**
	 * Verifica se o intervalo de formul�rios est� contido em algum v�nculo com impressoras do Controle de Formul�rio informado.
	 * @param list Cole��o de ControleFormImpressora
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
	 * Verifica se o intervalo de formul�rios est� contido em algum v�nculo com impressoras.
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 */
	public void verificarIntervaloForm(Long nrFormularioInicial, Long nrFormularioFinal){
		if (!getControleFormImpressoraDAO().verificaIntervaloForm(nrFormularioInicial, nrFormularioFinal)){
			throw new BusinessException("LMS-00017");
		}
	}
	
	/**
	 * Verifica se o intervalo dos Formul�rios informados est� contido no intervalo de Formul�rios do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException Formul�rios informados n�o est�o contidos no intervalo de Formul�rios do Controle de Estoque selecionado
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
	 * Verifica se a S�rie informada � a mesma do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException S�rie informada n�o � a mesma do Controle de Estoque selecionado
	 */
	private void validarSerie(ControleFormImpressora controleFormImpressora) {
		boolean result = controleFormImpressora.getDsSerie().equals(controleFormImpressora.getControleFormulario().getCdSerie());
		if(!result) {
			throw new BusinessException("S�rie inv�lida.");
		}
	}

	/**
	 * Verifica se o Selo Fiscal est� contido no intervalo de Selos Fiscais do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException Selo Fiscal informado n�o est� contido no intervalo de Selos Fiscais do Controle de Estoque selecionado
	 */
	private void validarSeloFiscal(ControleFormImpressora controleFormImpressora) {
		long nrSeloFiscal = controleFormImpressora.getNrSeloFiscalInicial().longValue();
		long nrSeloFiscalInicial = controleFormImpressora.getControleFormulario().getNrSeloFiscalInicial().longValue();
		long nrSeloFiscalFinal = controleFormImpressora.getControleFormulario().getNrSeloFiscalFinal().longValue();

		boolean result = ( (nrSeloFiscal >= nrSeloFiscalInicial) && (nrSeloFiscal <= nrSeloFiscalFinal) );
		if(!result) {
			throw new BusinessException("Selo Fiscal inv�lido.");
		}
	}

	/**
	 * Verifica se o N�mero da �ltima Impress�o est� contido no intervalo de Formul�rios informado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException N�mero da �ltima Impress�o n�o est� contido no intervalo de Formul�rios informado
	 */
	private void validarUltimaImpressao(ControleFormImpressora controleFormImpressora) {
		long nrUltimaImpressao = controleFormImpressora.getNrUltimaImpressao().longValue();
		long nrFormularioInicial = controleFormImpressora.getNrFormularioInicial().longValue();
		long nrFormularioFinal = controleFormImpressora.getNrFormularioFinal().longValue();

		boolean result = ( (nrUltimaImpressao >= nrFormularioInicial) && (nrUltimaImpressao <= nrFormularioFinal) );
		if(!result) {
			throw new BusinessException("�ltima Impress�o inv�lida.");
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ControleFormImpressora bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setControleFormImpressoraDAO(ControleFormImpressoraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ControleFormImpressoraDAO getControleFormImpressoraDAO() {
		return (ControleFormImpressoraDAO) getDao();
	}

}