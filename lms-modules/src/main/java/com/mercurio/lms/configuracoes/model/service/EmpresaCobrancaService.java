package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EmpresaCobranca;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.EmpresaCobrancaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.empresaCobrancaService"
 */
public class EmpresaCobrancaService extends CrudService<EmpresaCobranca, Long> {
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private ContatoService contatoService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Valida��o do Concluir Cadastro.<BR>
	 * Deve existir pelo menos 1 (um) Endere�o e 1 (um) Contato.
	 * @author Robson Edemar Gehl
	 * @see validateEndereco
	 * @see validateContato
	 * @param empresa
	 */
	public void validateConcluirCadastro(EmpresaCobranca empresa){
		validateEndereco(empresa);
		validateContato(empresa);
	}

	/**
	 * Valida��o de Endere�o para Empresa de Cobran�a.<BR>
	 * Deve haver pelo menos 1 (um) Endere�o para a empresa.
	 * @param empresa
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-01019") n�o existe Endere�o para a Empresa 
	 */
	public void validateEndereco(EmpresaCobranca empresa){
		if(Boolean.FALSE.equals(
				enderecoPessoaService.validateQuantidadeMinima(empresa.getPessoa(), 1))
		){
			throw new BusinessException("LMS-01019");
		}
	}

	/**
	 * Valida��o do Contato para Empresa de Cobran�a.<BR>
	 * Deve haver pelo menos um Contato para a empresa.
	 * @author Robson Edemar Gehl
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-01048")
	 * @param empresa
	 */
	public void validateContato(EmpresaCobranca empresa){
		if (Boolean.FALSE.equals(contatoService.validateQuantidadeMinima(empresa.getPessoa(), 1))) {
			throw new BusinessException("LMS-00048");
		}
	}

	/**
	 * Verifica se j� existe uma Empresa Cobran�a com o N�mero e Tipo de Identifica��o informado.
	 * @param map deve conter o numero e tipo de identifica��o; 
	 */
	public void verificaExistenciaEmpresaCobranca(EmpresaCobranca empresa){
		if (getEmpresaCobrancaDAO().verificaExistenciaEmpresaCobranca(empresa)){
			throw new BusinessException("LMS-27021");	
		}
	}

	/**
	 * Verifica se j� existe uma Pessoa e uma empresa cobranca com o N�mero e Tipo de Identifica��o informado.
	 * @param map deve conter o numero e tipo de identifica��o; 
	 */
	public Pessoa validateEmpresaCobranca(Map map) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.putAll(map);

		Pessoa pessoa = configuracoesFacade.getPessoa(tfm.getString("nrIdentificacao"), tfm.getString("tpIdentificacao"));
		if(pessoa != null){
			/** Valida se j� existe uma empresa cobran�a para a pessoa em quest�o*/
			if(getEmpresaCobrancaDAO().findEmpresaCobrancaById((Long)pessoa.getIdPessoa())){
				throw new BusinessException("LMS-27021");	
			}
			return pessoa;
		} else {
			return null;
		}
	}

	/**
	 * Store da Empresa realiza um store da Pessoa, antes de salvar/atualizar dados. Tamb�m � realizado um teste com o Tipo de Pessoa,
	 * permitindo Store de apenas Pessoas J�ridicas.
	 */
	@Override
	protected EmpresaCobranca beforeStore(EmpresaCobranca bean) {
		EmpresaCobranca empresaCobranca =(EmpresaCobranca) bean;
		verificaExistenciaEmpresaCobranca(empresaCobranca);

		pessoaService.store(empresaCobranca);

		return super.beforeStore(bean);
	}

	@Override
	protected EmpresaCobranca beforeInsert(EmpresaCobranca bean) {
		((EmpresaCobranca) bean).getPessoa().setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		return super.beforeInsert(bean);
	}

	/**
	 * Recupera uma inst�ncia de <code>EmpresaCobranca</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public EmpresaCobranca findById(java.lang.Long id) {
		return (EmpresaCobranca)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
		//this.removiById(id);
	}

	/**
	 * @param id
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeEmpresaCobrancaById(java.lang.Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id);	
		} catch (Exception e) {
			//ignora de Pessoa
		}
	}

	/**
	 * @param id
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeEmpresaCobrancaByIds(List ids) {
		this.removeByIds(ids);
		for (Iterator iter = ids.iterator(); iter.hasNext(); ){
			try {
				pessoaService.removeById((Long)iter.next());	
			} catch(Exception e) {
				//sem tratamentos, simplesmente ignora. Pois, provavelmente, h� dependencias da Pessoa.
			}
		}
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
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
	public java.io.Serializable store(EmpresaCobranca bean) {
		return super.store(bean);
	}

	/**
	 * Retorna 'true' se a pessoa informada � uma empresa cobranca ativa sen�o, retorna 'false'.
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isEmpresaCobranca(Long idPessoa){
		return getEmpresaCobrancaDAO().isEmpresaCobranca(idPessoa);
	} 

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setEmpresaCobrancaDAO(EmpresaCobrancaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private EmpresaCobrancaDAO getEmpresaCobrancaDAO() {
		return (EmpresaCobrancaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}