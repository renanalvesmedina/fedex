/**
 * 
 */
package com.mercurio.lms.configuracoes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;

/**
 * @author Felipe Cuozzo
 * @spring.bean id="lms.configuracoesFacade"
 */
public class ConfiguracoesFacadeImpl implements ConfiguracoesFacade {
	private ConteudoParametroFilialService conteudoParamFilialService;
	private ParametroGeralService paramGeralService;
	private PessoaService pessoaService;
	private MoedaService moedaService;
	private MoedaPaisService moedaPaisService;
	private DomainValueService domainValueService;
	private ContatoService contatoService;
	private RecursoMensagemService recursoMensagemService;
	
	public ConfiguracoesFacadeImpl() {
		
	}

	public ContatoService getContatoService() {
		return contatoService;
	}
	
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
	public Contato findContatoByIdPessoaAndTipoContato(Long idPessoa, String tpContato) {
		return contatoService.findContatoByIdPessoaAndTipoContato(idPessoa, tpContato);
	}

	public List<Contato> findContatosByIdPessoaAndTipoContato(Long idPessoa, String tpContato) {
		return contatoService.findContatosByIdPessoaAndTipoContato(idPessoa, tpContato);
	}
	
	/** 
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#incrementaParametroSequencial(String, boolean)
	 */
	public Long incrementaParametroSequencial(String nomeParametro, boolean lockParametro) {
		return this.paramGeralService.generateValorParametroSequencial(nomeParametro, lockParametro, +1);
	}

	/** 
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#decrementaParametroSequencial(String, boolean)
	 */
	public Long decrementaParametroSequencial(String nomeParametro, boolean lockParametro) {
		return paramGeralService.generateValorParametroSequencial(nomeParametro, lockParametro, -1);
	}

	/**
	 * Método que retorna as moedas do pais informado, ativo.
	 * 
	 * @param Long idPais
	 * @param Boolean blSomenteAtivos
	 * @return List
	 * */
	public List<Moeda> getMoeda(Long idPais, Boolean blSomenteAtivos) {
		return this.moedaService.findMoedaByPais(idPais, blSomenteAtivos);
	}

	/**
	 * Retorna uma moeda pelo código ISO numérico
	 * 
	 * @param nrIsoCode
	 * @return Moeda
	 */
	public Moeda getMoeda(Short nrIsoCode) {
		return moedaService.findMoedaByNrIsoCode(nrIsoCode);
	}

	/**
	 * Método que retorna as moedaPais do pais informado, ativo.
	 * 
	 * @param Long idPais
	 * @param Boolean blSomenteAtivos
	 * @return List
	 * */
	public List<MoedaPais> getMoedasPais(Long idPais, Boolean blSomenteAtivos) {
		return this.moedaPaisService.findByPais(idPais, blSomenteAtivos);
	}

	/** 
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#incrementaParametroSequencial(Long, String, boolean)
	 */
	public Long incrementaParametroSequencial(Long idFilial, String nomeParametro, boolean lockParametro) {
		return conteudoParamFilialService.generateProximoValorParametroSequencial(idFilial, nomeParametro, lockParametro);
	}

	/** 
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#decrementaParametroSequencial(Long, String, boolean)
	 */
	public Long decrementaParametroSequencial(Long idFilial, String nomeParametro, boolean lockParametro) {
		return conteudoParamFilialService.generateValorParametroSequencialAnterior(idFilial, nomeParametro, lockParametro);
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getValorParametro(java.lang.String)
	 */
	public Object getValorParametro(String nomeParametro) {
		return this.paramGeralService.findConteudoByNomeParametro(nomeParametro, false);
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getValorParametroWithoutException(java.lang.String)
	 */
	public Object getValorParametroWithoutException(String nomeParametro) {
		return this.paramGeralService.findConteudoByNomeParametroWithoutException(nomeParametro, false);
	}
	
	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getValorParametro(java.lang.Long, java.lang.String)
	 */
	public Object getValorParametro(Long idFilial, String nomeParametro) {
		return this.conteudoParamFilialService.findConteudoByNomeParametro(idFilial, nomeParametro, false);
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getValorParametro(java.lang.Long, java.lang.String)
	 */
	public Object getValorParametroWithException(Long idFilial, String nomeParametro) {		
		return this.conteudoParamFilialService.findConteudoByNomeParametroWithException(idFilial, nomeParametro, false);
	}	

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#storeValorParametro(java.lang.String, java.lang.Object)
	 */
	public void storeValorParametro(String nomeParametro, Object valorParametro) {
		this.paramGeralService.storeValorParametro(nomeParametro, valorParametro);
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#storeValorParametro(java.lang.Long, java.lang.String, java.lang.Object)
	 */
	public void storeValorParametro(Long idFilial, String nomeParametro, Object valorParametro) {
		this.conteudoParamFilialService.storeValorParametro(idFilial, nomeParametro, valorParametro);
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#converteValorMoeda(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.util.Date, java.math.BigDecimal)
	 */
	public BigDecimal converteValorMoeda(
			Long idPaisOrigem,
			Long idMoedaOrigem,
			Long idPaisDestino,
			Long idMoedaDestino,
			Date dataCotacao,
			BigDecimal valor
	) {
		throw new UnsupportedOperationException("método ainda não disponivel");
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getValorCotacaoMoeda(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.util.Date)
	 */
	public BigDecimal getValorCotacaoMoeda(
			Long idPaisOrigem,
			Long idMoedaOrigem,
			Long idPaisDestino,
			Long idMoedaDestino,
			Date dataCotacao
	) {
		throw new UnsupportedOperationException("método ainda não disponivel");
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getMensagem(String)
	 */
	public String getMensagem(String chave) {
		return recursoMensagemService.findByChave(chave);
	}

	/**
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getMensagem(String, Object[])
	 */
	public String getMensagem(String chave, Object[] argumentos) {
		return recursoMensagemService.findByChave(chave, argumentos);
	}

	public Pessoa getPessoa(Long id) {
		return pessoaService.findById(id);
	}

	public Object getPessoa(Long id, Class classeEspecializacao, boolean fetchPessoa) {
		return pessoaService.findByIdEspecializacao(id, classeEspecializacao, fetchPessoa);
	}

	public Object getPessoa(String nrIdentificacao, String tpIdentificacao, Class classeEspecializacao, boolean fetchPessoa) {
		return pessoaService.findByIdentificacao(tpIdentificacao, nrIdentificacao, classeEspecializacao, fetchPessoa);
	}

	public Pessoa getPessoa(String nrIdentificacao, String tpIdentificacao) {
		return pessoaService.findByIdentificacao(tpIdentificacao, nrIdentificacao);
	}

	public DomainValue getDomainValue(String domainName, String domainValue) {
		return domainValueService.findDomainValueByValue(domainName, domainValue);
	}

	/* 
	 * SETTERs
	 */

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParamFilialService = conteudoParametroFilialService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.paramGeralService = parametroGeralService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}
}
