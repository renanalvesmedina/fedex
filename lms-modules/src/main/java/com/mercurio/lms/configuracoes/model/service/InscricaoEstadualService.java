package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.InscricaoEstadualDAO;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.inscricaoEstadualService"
 */
public class InscricaoEstadualService extends CrudService<InscricaoEstadual, Long> {
	private TipoTributacaoIEService tipoTributacaoIEService;
	private ConfiguracoesFacade configuracoesFacade;
	private EnderecoPessoaService enderecoPessoaService;
	private PessoaService pessoaService;
	private EspecializacaoPessoaService especializacaoPessoaService;
	private PerfilUsuarioService perfilUsuarioService;
	private ParametroGeralService parametroGeralService;	

	/**
	 * Recupera uma inst�ncia de <code>InscricaoEstadual</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public InscricaoEstadual findById(java.lang.Long id) {
		return (InscricaoEstadual)super.findById(id);
	}

	protected void beforeRemoveById(Long id) {
		InscricaoEstadual inscricaoEstadual = (InscricaoEstadual)findById(id);

		//Se � a �ltima inscri��o estadual, n�o pode apagar ela se ela pretence a uma dos tipos de pessoa informado.
		if (isLastInscricaoPessoa(inscricaoEstadual.getPessoa().getIdPessoa())){
			validateInscricaoEstadual(inscricaoEstadual.getPessoa().getIdPessoa());

		// Caso n�o seja a �ltima inscricaoEstadual, mas seja a �ltima ativa
		}else if(inscricaoEstadual.getTpSituacao().getValue().equals("A") 
				&& isLastInscricaoEstadualActive(inscricaoEstadual.getPessoa().getIdPessoa(), inscricaoEstadual.getIdInscricaoEstadual())){
			validateInscricaoEstadual(inscricaoEstadual.getPessoa().getIdPessoa());
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(inscricaoEstadual.getPessoa().getIdPessoa());

		super.beforeRemoveById(id);
	}

	/**
	 * Se a inscri��p pretence a um dos tipo de pessoa informado, lan�ar uma exception.
	 * 
	 * @author Micka�l Jalbert
	 * @since 24/08/2006
	 *  
	 * @param idPessoa
	 */
	private void validateInscricaoEstadual(Long idPessoa) {
		List<Short> lstCdEspecializacao = new ArrayList<Short>();

		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_EMPRESA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL_CIA_AEREA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CON_POSTO_PASSAGEM);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_OPERADORA_MCT);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CLIENTE);

		Short cdEspecializacao = especializacaoPessoaService.isEspecializado(idPessoa, lstCdEspecializacao);
		if (cdEspecializacao != null) {
			throw new BusinessException("LMS-27070", new Object[]{especializacaoPessoaService.getLabel(cdEspecializacao)});
		}
	}

	/**
	 * 
	 * Se usu�rio logado estiver ligado aos perfis tribut�rios da �MTZ�: 
	 * Acessa tabela PERFIL_USUARIO onde ID_USUARIO seja igual ao ID do usu�rio logado e 
	 * verifica se PERFIL_USUARIO.ID_PERFIL pertence ao par�metro geral �ID_PERFIS_TRIBUTOS_MTZ� 
	 * 
	 * @return
	 */	
	public Boolean perfilMatrizUsuarioLogado(){
		
		Boolean valido = Boolean.FALSE;
		
		List<PerfilUsuario> listaPerfisUsuario = perfilUsuarioService. findByIdUsuarioPerfilUsuario(SessionUtils
				.getUsuarioLogado().getIdUsuario());
		
		String perfisMatriz = String.valueOf(parametroGeralService.findConteudoByNomeParametro("ID_PERFIS_TRIBUTOS_MTZ", false));		
		if(listaPerfisUsuario != null && !listaPerfisUsuario.isEmpty() && StringUtils.isNotBlank(perfisMatriz) ){
									
			List<String> listIdsPerfisMatriz = Arrays.asList(perfisMatriz.split(";"));						
			String idPerfilLogado = null;
			for(PerfilUsuario perfil : listaPerfisUsuario){
				if(perfil.getPerfil() != null && perfil.getPerfil().getIdPerfil() != null){
					idPerfilLogado = String.valueOf(perfil.getPerfil().getIdPerfil());
					if(idPerfilLogado != null && listIdsPerfisMatriz.contains(idPerfilLogado)){
						valido = Boolean.TRUE;
						break;
					}
				}
			}
		}
		return valido;
	}	
	
	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		InscricaoEstadual ie = findById(id);
		validateBlAtualizacaoCountasse(ie.getPessoa().getIdPessoa());
		//Esta chamada abaixo que j� existia foi comentada , pois est� 
		//errado passar o id da IE para este m�todo
		//validateBlAtualizacaoCountasse(id);
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
		InscricaoEstadual ie;
		for (Iterator<Long> iter = ids.iterator(); iter.hasNext();) {
			ie = findById(iter.next());
			validateBlAtualizacaoCountasse(ie.getPessoa().getIdPessoa());
			removeById(ie.getIdInscricaoEstadual());
		}
	}

	private void  validarBlIndicadorPadrao(InscricaoEstadual inscricaoEstadual) {
		if(!getInscricaoEstadualDAO().validarBlIndicadorPadrao(inscricaoEstadual.getIdInscricaoEstadual(),inscricaoEstadual.getPessoa().getIdPessoa(), false)) {
			throw new BusinessException("LMS-27033");
		}
	}

	/**
	 * Retorna true se a pessoa N�O tem indicador financeiro;
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 * */
	public boolean findIndicadorPadrao(Long idInscricaoEstadual, Long idPessoa){
		return getInscricaoEstadualDAO().validarBlIndicadorPadrao(idInscricaoEstadual,idPessoa, true);
	}

	/**
	 * Fun��o respons�vel por buscar a inscri��o estadual padr�o e ativa
	 * 
	 * @param Long idPessoa 
	 * @return retorna a incri��o estadual cadastrada da pessoa informada ou null se n�o encontrar 
	 */
	public InscricaoEstadual findIeByIdPessoaAtivoPadrao(Long idPessoa) {
			return getInscricaoEstadualDAO().findIeByIdPessoaAtivoPadrao(idPessoa);
	}
	
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
	public TypedFlatMap store(InscricaoEstadual bean) {
		TypedFlatMap tfm = new TypedFlatMap();

		beforeStore(bean);
		
		if (!"ISENTO".equals(bean.getNrInscricaoEstadual())) {
		    List<InscricaoEstadual> listInscricaoAtiva = findOutraInscricaoEstadual(bean.getIdInscricaoEstadual(), bean.getPessoa(), bean.getNrInscricaoEstadual());
		    if (!listInscricaoAtiva.isEmpty()) {
		        throw new BusinessException("LMS-00002");
		    }
		}
		
		if("I".equals(bean.getTpSituacao().getValue())){
			tipoTributacaoIEService.atualizarDataFimVigencia(bean.getIdInscricaoEstadual());
		}
		
		pessoaService.validateAlteracaoPessoa(((InscricaoEstadual)bean).getPessoa().getIdPessoa());

		Long id = (Long) storeInscricaoEstadual(bean);
		String msg = configuracoesFacade.getMensagem("LMS-23021");

		tfm.put("idInscricaoEstadual",id);
		tfm.put("aviso",msg);

		return tfm;
	}

	private List<InscricaoEstadual> findOutraInscricaoEstadual(Long idInscricaoEstadual, Pessoa pessoa, String nrInscricaoEstadual) {
        return getInscricaoEstadualDAO().findOutraInscricaoEstadual(idInscricaoEstadual, pessoa, nrInscricaoEstadual);
    }

	protected InscricaoEstadual beforeUpdate(InscricaoEstadual bean) {
		InscricaoEstadual inscricaoEstadual = (InscricaoEstadual)bean;

		if (inscricaoEstadual.getTpSituacao().getValue().equals("I")
				&& isLastInscricaoEstadualActive(inscricaoEstadual.getPessoa().getIdPessoa(), inscricaoEstadual.getIdInscricaoEstadual())){
			validateInscricaoEstadual(inscricaoEstadual.getPessoa().getIdPessoa());
		}

		return super.beforeUpdate(bean);
	}

	@Override
	protected InscricaoEstadual beforeStore(InscricaoEstadual bean) {
		//Valida se o usuario logado pode alterar a pessoa
		
		return super.beforeStore(bean);
	}

	/**
	 * @author Samuel Herrmann
	 * Fun��o responsavel por buscar a ultima inscricao estadual que esteja ativa
	 * @param idPessoa
	 * @return retorna a ultima incri��o estadual cadastrada da pessoa informada, null em se n�o encontrar 
	 */
	public InscricaoEstadual getLastInscricaoEstadualByPessoa(Long idPessoa) {
		return getInscricaoEstadualDAO().getLastInscricaoEstadualByPessoa(idPessoa);
	}

	/**
	 * Fun��o responsavel por buscar as inscric�es estaduais ativas
	 * 
	 * @author Samuel Alves
	 * @param String identificacao
	 * @return retorna a ultima incri��o estadual cadastrada da pessoa informada, null em se n�o encontrar 
	 */
	public List<InscricaoEstadual> findLastInscricaoEstadualByIdentificacaoPessoa(String identificacao) {
		return getInscricaoEstadualDAO().findLastInscricaoEstadualByIdentificacaoPessoa(identificacao);
	}

	/**
	 * Busca a Unidade Federativa associada a pessoa :
	 * 
	 * se ela for pessoa Jur�dica busca o endere�o comercial (se possuir)<br>
	 * se ela for pessoa F�sica busca o endere�o residencial <br>
	 * 
	 * Alterado por Jos� Rodrigo Moraes em 31/07/2006
	 *  
	 * @author Alexandre Menezes
	 * @param idPessoa Identificador da pessoa
	 * @return Map
	 */
	public Map<String, Object> findUnidadeFederativa(Long idPessoa) {
		Pessoa p = configuracoesFacade.getPessoa(idPessoa);

		Map<String, Object>  tfm = enderecoPessoaService.findEnderecoByIdPessoaTpPessoa(p.getTpPessoa().getValue(),p.getIdPessoa());
		if( tfm.isEmpty() ){
			tfm = new HashMap<String, Object> ();			
		}
		tfm.put("tpPessoa", p.getTpPessoa().getValue());

		return tfm;
	}

	public InscricaoEstadual findByPessoaIndicadorPadrao(Long idPessoa, Boolean indicadorPadrao) {
		return getInscricaoEstadualDAO().findByPessoaIndicadorPadrao(idPessoa, indicadorPadrao);
	} 

	/**
	 * Verifica se InscricaoEstadual � Isento.<BR>
	 * @param id
	 * @return true se for isento; false, caso contr�rio.
	 */
	public boolean validateIsento(Long id){
		return getInscricaoEstadualDAO().validateIsento(id);
	}

	public List findByPessoa(Long idPessoa) {
		return getInscricaoEstadualDAO().findByPessoa(idPessoa);
	}
	
	public List findInscricaoByPessoa(Long idPessoa) {
		return getInscricaoEstadualDAO().findInscricaoByPessoa(idPessoa);
	}

	/**
	 * Implementa��o para validar a regra que somente uma
	 * Inscri��o Estadual pode ser a "Inscri��o Padr�o"
	 * 
	 * Verifica��es na tabela TIPO_TRIBUTACAO_IE
	 * 
	 * @param bean InscricaoEstadual
	 * @param isentoInscricao Indica que o chackbox isento de inscri��o foi ou n�o marcado na tela
	 * @return Mensagem LMS-27064 - Aviso de tributa��es cadastradas com vig�ncia futura
	 */	
	public java.io.Serializable storeInscricaoEstadual(InscricaoEstadual ie) {
		boolean atualizacao = ie.getIdInscricaoEstadual() != null;

		validateBlAtualizacaoCountasse(ie.getPessoa().getIdPessoa());

		
		//Regra 3.4 - 1
		if (ie.getBlIndicadorPadrao().booleanValue()) {
		   validarBlIndicadorPadrao(ie); 	
		}

		Long id = (Long) super.store(ie);

		if(!atualizacao) {
			TipoTributacaoIE nova = new TipoTributacaoIE();
			nova.setInscricaoEstadual(ie);
			nova.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
			nova.setBlIsencaoExportacoes(Boolean.FALSE);
			nova.setBlAceitaSubstituicao(Boolean.TRUE);
			nova.setBlIncentivada(Boolean.FALSE);
			nova.setDtVigenciaFinal(null);
			if( ie.getNrInscricaoEstadual().equalsIgnoreCase("isento") ){
				nova.setTpSituacaoTributaria(new DomainValue("NC"));
			} else {
				nova.setTpSituacaoTributaria(new DomainValue("CO"));
			}
			tipoTributacaoIEService.store(nova, true);
		}
		return id;
	}

	/**
	 * Metodo chamado via EDI para n�o passar pelas validacoes de filial de usuario logado.
	 * 
	 * @param inscricaoEstadual
	 * @return
	 */

	public Serializable storeBypassValidations(InscricaoEstadual inscricaoEstadual){
		boolean atualiza = inscricaoEstadual.getIdInscricaoEstadual() != null;
		
		//Usa diretamente o store do dao para n�o chamar o beforeUpdate nem o beforeStore com metodos
		//de valida��o de altera��es e filiais logadas.
		getDao().store(inscricaoEstadual);
		
		Serializable value = getDao().getIdentifier(inscricaoEstadual); 
		
		if (!atualiza){
			TipoTributacaoIE tipoTributacao = new TipoTributacaoIE();
			tipoTributacao.setInscricaoEstadual(inscricaoEstadual);
			tipoTributacao.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
			if ("ISENTO".equalsIgnoreCase(inscricaoEstadual.getNrInscricaoEstadual())){
				tipoTributacao.setTpSituacaoTributaria(new DomainValue("NC"));
			}else{
				tipoTributacao.setTpSituacaoTributaria(new DomainValue("CO"));
			}
			tipoTributacao.setBlAceitaSubstituicao(true);
			tipoTributacao.setBlIncentivada(false);
			tipoTributacao.setBlIsencaoExportacoes(false);
			
			tipoTributacaoIEService.storeSkipValidations(tipoTributacao);
		}
		return value;
	}
	
	public List<InscricaoEstadual> findInscricaoEstadualAtivaByPessoa(Long idPessoa) {
		return getInscricaoEstadualDAO().findInscricaoEstadualAtivaByPessoa(idPessoa);
	}
	
	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 31/07/2007
	 *
	 * @param ie
	 * @throws BusinessException
	 */
	public void validateBlAtualizacaoCountasse(Long id) {
		Pessoa p = pessoaService.findById(id);
		// Caso o blAtualizacaoCountasse seja true e a filial do usuario logado n�o seja MTZ, lan�a a exception. 
		if (p.getBlAtualizacaoCountasse() &&
				!SessionUtils.isFilialSessaoMatriz()) {
			throw new BusinessException("LMS-27096");
		}
		getInscricaoEstadualDAO().getAdsmHibernateTemplate().evict(p);
	}
	
	/**
	 * Informa se � a �ltima inscri��o estadual da pessoa.
	 * 
	 * @author Micka�l Jalbert
	 * @since 24/08/2006
	 * 
	 * @param Contato contato
	 * @return boolean
	 */
	public boolean isLastInscricaoPessoa(Long idPessoa){
		List lstInscricao = findByPessoa(idPessoa);
		if (lstInscricao.size() < 2){
			return true;
		}
		return false;
	} 	

	/**
	 * Busca as incricaoEstadual ativas da pessoa
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/09/2006
	 *
	 * @param idPessoa
	 * @return
	 *
	 */
	public boolean isLastInscricaoEstadualActive(Long idPessoa, Long idInscricaoEstadual) {
		boolean retorno = false;
		List lst = getInscricaoEstadualDAO().findInscricaoEstadualActiveByPessoa(idPessoa, idInscricaoEstadual);
		if(lst != null && lst.size() == 0)
			retorno = true;

		return retorno;
	}

	/**
	 * Salva a Inscri��o estadual sem valida��o alguma.
	 * M�todo criado para a integra��o.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 27/11/2007
	 *
	 * @param ie
	 * @return
	 */
	public java.io.Serializable storeBasic(InscricaoEstadual ie){
		getDao().store(ie);   
		return getDao().getIdentifier(ie);
	}
	
	/**
	 * Carrega o InscricaoEstadual de acordo com os filtros
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/12/2006
	 *
	 * @param idPessoa
	 * @param nrInscricaoEstadual
	 * @return
	 */
	public InscricaoEstadual findByNrInscricaoEstadual( Long idPessoa, String nrInscricaoEstadual ){
		return getInscricaoEstadualDAO().findByNrInscricaoEstadual(idPessoa, nrInscricaoEstadual);
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria){
		return getInscricaoEstadualDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}	
	
	public Integer getRowCountCustom(TypedFlatMap criteria){
		return getInscricaoEstadualDAO().getRowCountCustom(criteria);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setInscricaoEstadualDAO(InscricaoEstadualDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private InscricaoEstadualDAO getInscricaoEstadualDAO() {
		return (InscricaoEstadualDAO) getDao();
	}

	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setEspecializacaoPessoaService(EspecializacaoPessoaService especializacaoPessoaService) {
		this.especializacaoPessoaService = especializacaoPessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}	
}