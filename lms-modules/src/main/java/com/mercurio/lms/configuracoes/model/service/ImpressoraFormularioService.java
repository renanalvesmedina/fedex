package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.dao.ImpressoraFormularioDAO;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de servi�o para CRUD: 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.impressoraFormularioService"
 */
public class ImpressoraFormularioService extends CrudService<ImpressoraFormulario, Long> {
	private ControleFormularioService controleFormularioService;

	/**
	 * Recupera uma inst�ncia de <code>ImpressoraFormulario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ImpressoraFormulario findById(java.lang.Long id) {
		return (ImpressoraFormulario)super.findById(id);
	}

	/**
	 * @param idControleFormulario
	 * @return
	 */
	public List<ImpressoraFormulario> findByControleFormulario(java.lang.Long idControleFormulario){
		return getImpressoraFormularioDAO().findByControleFormulario(idControleFormulario);
	}

	protected void beforeRemoveById(Long id) {
		/** Carrega a impressora formul�rio */
		ImpressoraFormulario imf = findById((Long)id);
		
		/** Caso situacao do controle formulario seja E, lan�a a exce��o */
		if(imf.getControleFormulario().getTpSituacaoFormulario().getValue().equalsIgnoreCase("E")){
			throw new BusinessException("LMS-27086");
		}
		super.beforeRemoveById(id);
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return getImpressoraFormularioDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}

	@Override
	protected ImpressoraFormulario beforeStore(ImpressoraFormulario bean) {
		//Find de Controle Formul�rio para valida��o, nas regras
		ControleFormulario cf = controleFormularioService.findById(bean.getControleFormulario().getIdControleFormulario());
		bean.setControleFormulario(cf);

		/** Caso situacao do controle formulario seja E, lan�a a exce��o */
		if(cf.getTpSituacaoFormulario().getValue().equalsIgnoreCase("E")){
			throw new BusinessException("LMS-27086");
		}

		/** Caso exista alguma impressora formul�rio cadastrada com um controle de formul�rio ativo, lan�a exce��o. 
		 *  Somente uma IMPRESSORA_FORMULARIO pode existir com um CONTROLE_FORMULARIO ativo */
		if (bean.getIdImpressoraFormulario() == null && getImpressoraFormularioDAO().verificarExistenciaControleFormularioAtivo(bean.getImpressora().getIdImpressora(), bean.getControleFormulario().getTpFormulario().getValue())) {
			throw new BusinessException("LMS-27102", new Object [] {bean.getControleFormulario().getTpFormulario().getValue()});
		}
		
		// valida Formul�rios. �tem 3.1
		validarFormularios(bean);

		// valida Selo Fiscal. �tem 3.3 
		validarSeloFiscal(bean);

		// valida �ltima Impress�o. �tem 3.4
		validarUltimaImpressao(bean);

		verificarIntervaloFormularios(bean);

		//item 3.6
		validarIntervaloControleFormulario(bean);

		return bean;
	}

	/**
	 * @author Robson Edemar Gehl
	 * @param impressora
	 */
	public void validarIntervaloControleFormulario(ImpressoraFormulario impressora){
		if(controleFormularioService.verificarIntervaloFilhosByImpressora(impressora)){
			throw new BusinessException("LMS-27037");
		}
	}

	/**
	 * Verifica se o intervalo de formul�rio da ImpressoraFormulario informada esta sendo utilizado por outro formul�rio.
	 * @param impressora
	 */
	public void verificarIntervaloFormularios(ImpressoraFormulario impressora){
		if (getImpressoraFormularioDAO().verificarIntervaloFormularios(impressora)){
			throw new BusinessException("LMS-27010");
		}
	}

	/**
	 * Verifica se o intervalo de formul�rios est� contido em algum v�nculo com impressoras do Controle de Formul�rio informado.
	 * @param list Cole��o de ControleFormImpressora
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 */
	public void verificarIntervaloFormByControleForm(ControleFormulario cf){
		List<ImpressoraFormulario> list = findByControleFormulario(cf.getIdControleFormulario());
		Long nrInicial = cf.getNrFormularioInicial();
		Long nrFinal = cf.getNrFormularioFinal();
		for(ImpressoraFormulario cfi : list) {
			boolean inicioFora = (cfi.getNrFormularioInicial().compareTo(nrInicial) < 0 || cfi.getNrFormularioFinal().compareTo(nrInicial) < 0);
			boolean finalFora = (cfi.getNrFormularioInicial().compareTo(nrFinal) > 0 || cfi.getNrFormularioFinal().compareTo(nrFinal) > 0);

			if (inicioFora || finalFora){
				throw new BusinessException("LMS-27009");
			}
		}
	}

	/**
	 * Verifica se o intervalo de formul�rios est� contido em algum v�nculo com impressoras, exceto o Controle Formul�rio em quest�o.<BR>
	 * A verifica��o considera apenas Controle de Formul�rio do mesmo Tipo de Documento e Empresa
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 */
	public void verificarIntervaloForm(ControleFormulario cf){
		if (!getImpressoraFormularioDAO().verificaIntervaloForm(cf)){
			throw new BusinessException("LMS-27009");
		}
	}

	/**
	 * Verifica se o intervalo dos Formul�rios informados est� contido no intervalo de Formul�rios do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException Formul�rios informados n�o est�o contidos no intervalo de Formul�rios do Controle de Estoque selecionado
	 */
	private void validarFormularios(ImpressoraFormulario impressoraFormulario) {
		long nrFormularioInicial = impressoraFormulario.getNrFormularioInicial().longValue();
		long nrFormularioFinal = impressoraFormulario.getNrFormularioFinal().longValue();

		ControleFormulario cf = impressoraFormulario.getControleFormulario();
		long nrCrtlFormInicial = cf.getNrFormularioInicial().longValue();
		long nrCrtlFormFinal = cf.getNrFormularioFinal().longValue();

		boolean result = ( (nrFormularioInicial >= nrCrtlFormInicial) && (nrFormularioFinal <= nrCrtlFormFinal) );
		if(!result) {
			throw new BusinessException("LMS-27025");
		}
	}

	/**
	 * Verifica se o Selo Fiscal est� contido no intervalo de Selos Fiscais do Controle de Estoque selecionado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException Selo Fiscal informado n�o est� contido no intervalo de Selos Fiscais do Controle de Estoque selecionado
	 */
	private void validarSeloFiscal(ImpressoraFormulario impressoraFormulario) {
		if (impressoraFormulario.getNrSeloFiscalInicial() != null){
			long nrSeloFiscal = impressoraFormulario.getNrSeloFiscalInicial().longValue();
			boolean result = false;

			if(impressoraFormulario.getControleFormulario().getNrSeloFiscalInicial() != null){
				long nrSeloFiscalInicial = impressoraFormulario.getControleFormulario().getNrSeloFiscalInicial().longValue();
				long nrSeloFiscalFinal = impressoraFormulario.getControleFormulario().getNrSeloFiscalFinal().longValue();

				result = ( (nrSeloFiscal >= nrSeloFiscalInicial) && (nrSeloFiscal <= nrSeloFiscalFinal) );
				if(!result) {
					throw new BusinessException("LMS-27026");
				}
			}

			if(!result) {
				throw new BusinessException("LMS-27026");
			}
		}
	}

	/**
	 * Verifica se o N�mero da �ltima Impress�o est� contido no intervalo de Formul�rios informado
	 * 
	 * @param controleFormImpressora V�nculo dos Formul�rios com Impressoras
	 * @throws BusinessException N�mero da �ltima Impress�o n�o est� contido no intervalo de Formul�rios informado
	 */
	private void validarUltimaImpressao(ImpressoraFormulario impressoraFormulario) {
		long nrUltimoFormulario = impressoraFormulario.getNrUltimoFormulario().longValue();
		long nrFormularioInicial = impressoraFormulario.getNrFormularioInicial().longValue();
		long nrFormularioFinal = impressoraFormulario.getNrFormularioFinal().longValue();

		boolean result = ( ((nrUltimoFormulario >= nrFormularioInicial) && (nrUltimoFormulario <= nrFormularioFinal)) 
				|| ((nrFormularioInicial-1) == nrUltimoFormulario) );
		if(!result) {
			throw new BusinessException("LMS-27027");
		}
	}

	public void validateNumeroProximoFormulario(Impressora impressora, String tpFormulario, Long nrProximoFormulario) {
		Long nrUltimoformulario = getImpressoraFormularioDAO().findNrUltimoFormulario(impressora.getIdImpressora(), tpFormulario);
		if(!CompareUtils.eq(LongUtils.incrementValue(nrUltimoformulario), nrProximoFormulario)) {
			throw new BusinessException("LMS-04114", new Object[]{nrProximoFormulario});
		}
	}	

	/**
	 * Servi�o para alterar a s�rie do v�nculo dos formul�rios com impressoras.<BR>
	 * Atualiza a s�rie de todas as ImpressoraFormulario do Controle de Formul�rio informado.
	 *@author Robson Edemar Gehl
	 * @param cf
	 */
	public void updateSerie(ControleFormulario cf){
		getImpressoraFormularioDAO().updateCdSerie(cf);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ImpressoraFormulario bean) {
		return super.store(bean);
	}

	/**
	 * Valida se existe alguma impressoraFormulario fora do intervalo de selos do controleFormulario correspondente
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 17/08/2006
	 *
	 * @param cf
	 *
	 */
	public void validaIntervaloSelosImpressora(ControleFormulario cf){
		ControleFormulario cfBanco = controleFormularioService.findById(cf.getIdControleFormulario());
		List<ImpressoraFormulario> impressoras = null;

		if(cfBanco.getNrSeloFiscalInicial() != null || cf.getNrSeloFiscalInicial() != null){
			impressoras = getImpressoraFormularioDAO().findImpressorasForaIntervaloSelosFormulario(cf, cfBanco);
		}

		if(impressoras != null && !impressoras.isEmpty())
			throw new BusinessException("LMS-27058");

		getImpressoraFormularioDAO().getAdsmHibernateTemplate().evict(cfBanco);
	}

	/**
	 * M�todo invocado antes de remover as impressoras
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 17/08/2006
	 *
	 * @param ids
	 */
	@Override
	public void beforeRemoveByIds(List<Long> ids) {
		/** Caso a lista de ids n�o esteja nula e n�o esteja vazia */
		if(ids != null && !ids.isEmpty()){
			/** Carrega a impressora formul�rio */
			ImpressoraFormulario imf = findById((Long)ids.get(0));

			/** Caso a situa��o do controle formul�rio seja encerrado, lan�a exce��o */
			if(imf.getControleFormulario().getTpSituacaoFormulario().getValue().equalsIgnoreCase("E")){
				throw new BusinessException("LMS-27086");
			}
		}

		/** Para excluir os registros selecionados, a filial do controleFormul�rio deve se igual a filial da sess�o do usu�rio */
		List<ImpressoraFormulario> impressoraFormularios = this.findImpressoraFormularioComFilialIgualFilialSessao(ids);
		if(impressoraFormularios.size() != ids.size()){
			throw new BusinessException("LMS-27073");
		}
		super.beforeRemoveByIds(ids);
	}

	/**
	 * M�todo respons�vel por buscar impressoraFormulario de acordo com os idsImpressoraFormulario e com a filial da sess�o do usu�rio 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/08/2006
	 *
	 * @param idsimpressoraFormulario
	 * @return
	 *
	 */
	public List<ImpressoraFormulario> findImpressoraFormularioComFilialIgualFilialSessao(List<Long> idsImpressoraFormulario){
		return getImpressoraFormularioDAO().findImpressoraFormularioComFilialIgualFilialSessao(idsImpressoraFormulario);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setImpressoraFormularioDAO(ImpressoraFormularioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ImpressoraFormularioDAO getImpressoraFormularioDAO() {
		return (ImpressoraFormularioDAO) getDao();
	}

	public void setControleFormularioService(ControleFormularioService controleFormularioService) {
		this.controleFormularioService = controleFormularioService;
	}
}