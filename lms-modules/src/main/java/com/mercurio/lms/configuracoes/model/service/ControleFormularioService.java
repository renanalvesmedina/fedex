package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.dao.ControleFormularioDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.controleFormularioService"
 */
public class ControleFormularioService extends CrudService<ControleFormulario, Long> {
	private ImpressoraFormularioService impressoraFormularioService;
	private UsuarioService usuarioService;
	
	/**
	 * Recupera uma inst�ncia de <code>ControleFormulario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ControleFormulario findById(java.lang.Long id) {
		return (ControleFormulario)super.findById(id);
	}

	/**
	 * Valida��o da Data de Recebimento do Controle de formul�rio.<BR>
	 * Quando existir estoque origem, a data deve ser menor ou igual a data corrente ou igual a data de recebimento do estoque origem.<BR>
	 * Quando n�o existir estoque origem, apenas a data deve ser menor ou igual a data corrente.
	 * 
	 * @author Robson Edemar Gehl
	 * @param cf
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27032") data recebimento maior que data corrente ou menor que a data do estoque original
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27023") data recebimento maior que data corrente
	 */
	public void validateDataRecebimentoEstoqueOriginal(ControleFormulario cf){
		if (cf.getControleFormulario() != null){
			if ( !getControleFormularioDAO().validateDataRecebimentoEstoqueOriginal(cf)
					|| (cf.getDtRecebimento().compareTo(JTDateTimeUtils.getDataAtual()) > 0)){
				throw new BusinessException("LMS-27032");
			}
		} else {
			if ( cf.getDtRecebimento().compareTo(JTDateTimeUtils.getDataAtual()) > 0){
				throw new BusinessException("LMS-27023");
			}
		}
	}

	/**
	 * 
	 */
	public ResultSetPage findPaginated(Map criteria) {
		List<String> included = new ArrayList<String>();
		included.add("idControleFormulario");
		included.add("empresa.pessoa.nmPessoa");
		included.add("filial.sgFilial");
		included.add("tpFormulario");
		included.add("tpSituacaoFormulario");
		included.add("nrFormularioInicial");
		included.add("nrFormularioFinal");
		included.add("dtRecebimento");
		included.add("controleFormOrigem");
		included.add("controleForm");

		//Uso espec�fico em Vinculo com Impressora
		included.add("cdSerie");
		included.add("nrSeloFiscalInicial");
		included.add("nrSeloFiscalFinal");
		included.add("filial.idFilial");
		included.add("nrCodigoBarrasInicial");
		included.add("nrCodigoBarrasFinal");

		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
		return rsp;
	}

	public Integer getRowCount(Map criteria) {
		return getControleFormularioDAO().getRowCount(criteria);
	}

	/**
	 * Valida Controle de Formul�rio ao remover.<BR>
	 * N�o � permitido remover um Controle Formul�rio encerrado.
	 *@author Robson Edemar Gehl
	 * @param ids
	 */
	public void validateRemoveControleFormularioEncerrado(List<Long> ids) {
		if (!getControleFormularioDAO().validateControleFormularioEncerrado(ids)) {
			throw new BusinessException("LMS-27052");
		}
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		validateRemoveControleFormularioEncerrado(ids);
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		validateRemoveControleFormularioEncerrado(ids);
		super.removeByIds(ids);
	}

	/**
	 * Valida o intervalo do formul�rio com o intervalo dos respectivos filhos, na atualiza��o. <BR>
	 * N�o � permitido um intervalo que n�o compreende um controle de formul�rio filho.
	 * @author Robson Edemar Gehl
	 * @param cf Controle de Formul�rio que est� sendo atualizado
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27035")
	 */
	public void validarIntervaloFilhos(ControleFormulario cf){
		if (cf.getIdControleFormulario() == null) return;

		if (!getControleFormularioDAO().validarIntervaloFilhos(cf)){
			throw new BusinessException("LMS-27035");
		}
	}

	/**
	 * Valida Tipo de Documento de Controle de Formul�rio.<BR>
	 * Se h� v�nculos de Controle de Formul�rios (filhos) o Tipo de Documento n�o pode ser alterado.
	 *@author Robson Edemar Gehl
	 * @param cf
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27040") h� v�nculo(s) de Controle de Formul�rio
	 */
	public void validateTipoDocumento(ControleFormulario cf){
		//Se existem filhos, n�o pode alterar tipo de documento
		if (getControleFormularioDAO().isExisteFilhos(cf.getIdControleFormulario())){
			if (!getControleFormularioDAO().validateTipoDocumento(cf)){
				throw new BusinessException("LMS-27040");
			}
		}
	}

	/**
	 * 
	 * @author Robson Edemar Gehl
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeUpdate(java.lang.Object)
	 */
	@Override
	protected ControleFormulario beforeUpdate(ControleFormulario bean) {
		validateTipoDocumento(bean);
		validarIntervaloFilhos(bean);

		//REGRA 3.5 ITEM 2
		validaIntervaloSelosImpressora(bean);

		return bean;
	}

	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected ControleFormulario beforeStore(ControleFormulario bean) {
		validateDataRecebimentoEstoqueOriginal(bean);
		validarFilialControleFormulario(bean);
		//n�o precisa validar os intervalos se o controle est� sendo encerrado
		if (!bean.getTpSituacaoFormulario().getValue().equals("E")){
			validarIntervaloFormularios(bean);
		}
		

		impressoraFormularioService.verificarIntervaloForm(bean);
		impressoraFormularioService.verificarIntervaloFormByControleForm(bean);

		return super.beforeStore(bean);		
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
		impressoraFormularioService.validaIntervaloSelosImpressora(cf);
	}
	
	
	/**
	 * A filial do Controle de Formul�rio n�o pode ser a mesma filial do Controle de Formul�rio pai (Estoque Origem).
	 * @author Robson Edemar Gehl
	 * @param cf
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27036")
	 */
	public void validarFilialControleFormulario(ControleFormulario cf){
		if (!getControleFormularioDAO().validarFilialControleFormulario(cf)){
			throw new BusinessException("LMS-27036");	
		}
	}
	
	/**
	 * Valida intervalo de formul�rio do Controle de Formul�rio.<BR>
	 * Intervalo n�o pode estar contido em outro Controle de Formul�rio; se houver estoque de origem, o intervalo deve estar contido no mesmo;
	 * o intervalo n�o pode estar contido em outro Controle de Formul�rio de mesmo estoque origem (irm�os). <BR> 
	 * As verifica��es de intervalos s�o feitas pelos Controle de Formul�rios do mesmo Tipo de Documento.
	 * @author Robson Edemar Gehl
	 * @param cf
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27006") intersecciona intervalo j� cadastrado
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27007") n�o est� contido no intervalo do Controle de Formul�rio pai.
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27008") intersecciona com intervalo de irm�os (do mesmo controle de formul�rio pai)
	 * 
	 */
	public void validarIntervaloFormularios(ControleFormulario cf) {
		if(cf.getControleFormulario() == null) {
			//Item 3.2
			if(getControleFormularioDAO().verificarIntervaloFormularios(cf)) {
				throw new BusinessException("LMS-27006");
			}
		} else {
			if(getControleFormularioDAO().verificarIntervaloPai(
					cf.getControleFormulario().getIdControleFormulario(),
					cf.getNrFormularioInicial(), cf.getNrFormularioFinal())
			) {
				throw new BusinessException("LMS-27007");
			}
			if(getControleFormularioDAO().verificarIntervaloPaiOrigem(cf)) {
				throw new BusinessException("LMS-27008");
			}
		}
	}

	/**
	 * Verifica se existe algum Controle de Formul�rio filho (do ControleFormulario informado) que intersecciona o intervalo da Impressora.
	 * @param impressora
	 * @return
	 */
	public boolean verificarIntervaloFilhosByImpressora(ImpressoraFormulario impressora){
		return getControleFormularioDAO().verificarIntervaloFilhosByImpressora(impressora);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ControleFormulario bean) {
		validaInformacaoCodigoBarras(bean);
		Long s = (Long)super.store(bean);
		impressoraFormularioService.updateSerie(bean);
		return s;
	}

	private void validaInformacaoCodigoBarras(ControleFormulario bean) {
		if (bean.getNrCodigoBarrasInicial() != null && bean.getNrCodigoBarrasFinal() != null 
				&& !CompareUtils.lt(bean.getNrCodigoBarrasInicial(), bean.getNrCodigoBarrasFinal())) {
			throw new BusinessException("LMS-27103");
		}
	}
	
	/**
	 * O usu�rio logado deve ter permiss�o de acesso � filial respons�vel comercialmente pelo cliente (ID_FILIAL_ATENDE_COMERCIAL)
	 */
	public Boolean validatePermissaoUsuarioAcessoFilial(Long idFilial){
		return usuarioService.verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	/**
	 * Verifica antes de excluir os controles de formul�rios se o usu�rio logado tem permiss�o de exclus�o
	 * frente a filial do controle informado.
	 * 
	 * @param ids Lista de identificadores de controles de formul�rios para serem exclu�dos.
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27049") - Usu�rio sem permiss�o de exclus�o sobre a filial
	 */
	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		/** Valida se os controleFormulario selecionados para exclus�o s�o da filial do usu�rio */
		List<ControleFormulario> controleFormularios = findControleFormularioComFilialIgualFilialSessao(ids);
		if(controleFormularios.size() != ids.size()){
			throw new BusinessException("LMS-27073");
		}

		for(ControleFormulario cf : controleFormularios) {
			if(Boolean.FALSE.equals(validatePermissaoUsuarioAcessoFilial(cf.getFilial().getIdFilial()))) {
				throw new BusinessException("LMS-27049");
			}
		}
		super.beforeRemoveByIds(ids);
	}

	public Long findFormularioNFServicoAtivoByNumeroFormulario(Long nrFormulario){
		return getControleFormularioDAO().findFormularioNFServicoAtivoByNumeroFormulario(nrFormulario);
	}

	/**
	 * M�todo respons�vel por buscar controleFormulario de acordo com os idsControleFormulario e com a filial da sess�o do usu�rio 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/08/2006
	 *
	 * @param idsControleFormulario
	 * @return
	 *
	 */
	public List<ControleFormulario> findControleFormularioComFilialIgualFilialSessao(List<Long> idsControleFormulario) {
		return getControleFormularioDAO().findControleFormularioComFilialIgualFilialSessao(idsControleFormulario);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setControleFormularioDAO(ControleFormularioDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ControleFormularioDAO getControleFormularioDAO() {
		return (ControleFormularioDAO) getDao();
	}

	public void setImpressoraFormularioService(ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}
