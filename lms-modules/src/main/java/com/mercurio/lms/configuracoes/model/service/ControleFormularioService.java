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
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.controleFormularioService"
 */
public class ControleFormularioService extends CrudService<ControleFormulario, Long> {
	private ImpressoraFormularioService impressoraFormularioService;
	private UsuarioService usuarioService;
	
	/**
	 * Recupera uma instância de <code>ControleFormulario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ControleFormulario findById(java.lang.Long id) {
		return (ControleFormulario)super.findById(id);
	}

	/**
	 * Validação da Data de Recebimento do Controle de formulário.<BR>
	 * Quando existir estoque origem, a data deve ser menor ou igual a data corrente ou igual a data de recebimento do estoque origem.<BR>
	 * Quando não existir estoque origem, apenas a data deve ser menor ou igual a data corrente.
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

		//Uso específico em Vinculo com Impressora
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
	 * Valida Controle de Formulário ao remover.<BR>
	 * Não é permitido remover um Controle Formulário encerrado.
	 *@author Robson Edemar Gehl
	 * @param ids
	 */
	public void validateRemoveControleFormularioEncerrado(List<Long> ids) {
		if (!getControleFormularioDAO().validateControleFormularioEncerrado(ids)) {
			throw new BusinessException("LMS-27052");
		}
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		validateRemoveControleFormularioEncerrado(ids);
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		validateRemoveControleFormularioEncerrado(ids);
		super.removeByIds(ids);
	}

	/**
	 * Valida o intervalo do formulário com o intervalo dos respectivos filhos, na atualização. <BR>
	 * Não é permitido um intervalo que não compreende um controle de formulário filho.
	 * @author Robson Edemar Gehl
	 * @param cf Controle de Formulário que está sendo atualizado
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27035")
	 */
	public void validarIntervaloFilhos(ControleFormulario cf){
		if (cf.getIdControleFormulario() == null) return;

		if (!getControleFormularioDAO().validarIntervaloFilhos(cf)){
			throw new BusinessException("LMS-27035");
		}
	}

	/**
	 * Valida Tipo de Documento de Controle de Formulário.<BR>
	 * Se há vínculos de Controle de Formulários (filhos) o Tipo de Documento não pode ser alterado.
	 *@author Robson Edemar Gehl
	 * @param cf
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27040") há vínculo(s) de Controle de Formulário
	 */
	public void validateTipoDocumento(ControleFormulario cf){
		//Se existem filhos, não pode alterar tipo de documento
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
		//não precisa validar os intervalos se o controle está sendo encerrado
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
	 * A filial do Controle de Formulário não pode ser a mesma filial do Controle de Formulário pai (Estoque Origem).
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
	 * Valida intervalo de formulário do Controle de Formulário.<BR>
	 * Intervalo não pode estar contido em outro Controle de Formulário; se houver estoque de origem, o intervalo deve estar contido no mesmo;
	 * o intervalo não pode estar contido em outro Controle de Formulário de mesmo estoque origem (irmãos). <BR> 
	 * As verificações de intervalos são feitas pelos Controle de Formulários do mesmo Tipo de Documento.
	 * @author Robson Edemar Gehl
	 * @param cf
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27006") intersecciona intervalo já cadastrado
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27007") não está contido no intervalo do Controle de Formulário pai.
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27008") intersecciona com intervalo de irmãos (do mesmo controle de formulário pai)
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
	 * Verifica se existe algum Controle de Formulário filho (do ControleFormulario informado) que intersecciona o intervalo da Impressora.
	 * @param impressora
	 * @return
	 */
	public boolean verificarIntervaloFilhosByImpressora(ImpressoraFormulario impressora){
		return getControleFormularioDAO().verificarIntervaloFilhosByImpressora(impressora);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
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
	 * O usuário logado deve ter permissão de acesso à filial responsável comercialmente pelo cliente (ID_FILIAL_ATENDE_COMERCIAL)
	 */
	public Boolean validatePermissaoUsuarioAcessoFilial(Long idFilial){
		return usuarioService.verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	/**
	 * Verifica antes de excluir os controles de formulários se o usuário logado tem permissão de exclusão
	 * frente a filial do controle informado.
	 * 
	 * @param ids Lista de identificadores de controles de formulários para serem excluídos.
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27049") - Usuário sem permissão de exclusão sobre a filial
	 */
	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		/** Valida se os controleFormulario selecionados para exclusão são da filial do usuário */
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
	 * Método responsável por buscar controleFormulario de acordo com os idsControleFormulario e com a filial da sessão do usuário 
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setControleFormularioDAO(ControleFormularioDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
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
