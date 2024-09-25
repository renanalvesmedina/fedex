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
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;

/**
 * 
 * Fachada para acesso a m�todos comuns do m�dulo de Configura��es.
 * 
 * @author Felipe Cuozzo
 *
 */
public interface ConfiguracoesFacade {

	/**
	 * Incrementa o valor de um par�metro geral.
	 * Esta rotina pode realizar um <code>lock</code> pessimista no registro do par�metro, �
	 * de responsabilidade do m�todo chamador liberar o <code>lock</code>.
	 *
	 * @param nomeParametro nome do parametro.
	 * @param lockParametro se <code>true</code> indica que deve ser adquirido lock pessimista. 
	 * 
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @return Valor do parametro incrementado em + 1.
	 */
	Long decrementaParametroSequencial(String nomeParametro, boolean lockParametro);

	/**
	 * Decrementa o valor de um par�metro geral.
	 * Esta rotina pode realizar um <code>lock</code> pessimista no registro do par�metro, �
	 * de responsabilidade do m�todo chamador liberar o <code>lock</code>.
	 *
	 * @param nomeParametro nome do parametro.
	 * @param lockParametro se <code>true</code> indica que deve ser adquirido lock pessimista. 
	 * 
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @return Valor do parametro decrementado em 1.
	 */
	Long incrementaParametroSequencial(String nomeParametro, boolean lockParametro);

	/**
	 * Incrementa o valor de um par�metro por filial.
	 * Esta rotina pode realizar um <code>lock</code> pessimista no registro do par�metro, �
	 * de responsabilidade do m�todo chamador liberar o <code>lock</code>.
	 *
	 * @param nomeParametro nome do parametro.
	 * @param idFilial identificador da filial.
	 * @param lockParametro se <code>true</code> indica que deve ser adquirido lock pessimista. 
	 * 
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @return Valor do parametro incrementado em + 1.
	 */
	Long incrementaParametroSequencial(Long idFilial, String nomeParametro, boolean lockParametro);

	/**
	 * Decrementa o valor de um par�metro por filial.
	 * Esta rotina pode realizar um <code>lock</code> pessimista no registro do par�metro, �
	 * de responsabilidade do m�todo chamador liberar o <code>lock</code>.
	 *
	 * @param nomeParametro nome do parametro.
	 * @param idFilial identificador da filial.
	 * @param lockParametro se <code>true</code> indica que deve ser adquirido lock pessimista. 
	 * 
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @throws java.lang.IllegalStateException caso o valor do par�metro seja maior que zero (0).
	 * @return Valor do parametro incrementado em - 1.
	 */
	Long decrementaParametroSequencial(Long idFilial, String nomeParametro, boolean lockParametro);

	/**
	 * Retorna o valor de uma par�metro geral do sistema.
	 * 
	 * @param nomeParametro nome do parametro.
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @return valor do parametro
	 */
	Object getValorParametro(String nomeParametro); 

	/**
	 * Retorna o valor de uma par�metro geral do sistema sem lan�ar exce��o caso o mesmo n�o exista.
	 * 
	 * @param nomeParametro nome do parametro.
	 * @return valor do parametro
	 */
	Object getValorParametroWithoutException(String nomeParametro); 
	
	/**
	 * Retorna o valor de uma par�metro por filial do sistema.
	 * 
	 * @param idFilial identificador da filial.
	 * @param nomeParametro nome do parametro.
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @return valor do parametro
	 */
	Object getValorParametro(Long idFilial, String nomeParametro); 
	
	/**
	 * Retorna o valor de uma par�metro por filial do sistema. Caso nao encontre, lanca uma excecao 
	 * 
	 * @param idFilial identificador da filial.
	 * @param nomeParametro nome do parametro.
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @return valor do parametro
	 */
	Object getValorParametroWithException(Long idFilial, String nomeParametro);
	
	/**
	 * Armazena o valor de um parametro geral do sistema.
	 * 
	 * @param nomeParametro nome do parametro.
	 * @param valorParametro valor do parametro.
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @throws ClassCastException Caso o tipo do parametro seja diferente do configurado.
	 */
	void storeValorParametro(String nomeParametro, Object valorParametro);

	/**
	 * Armazena o valor de um parametro geral do sistema.
	 * 
	 * @param idFilial identificador da filial.
	 * @param nomeParametro nome do parametro.
	 * @param valorParametro valor do parametro.
	 * @throws com.mercurio.adsm.framework.BusinessException Caso n�o exista um par�metro com o nome informado.
	 * @throws ClassCastException Caso o tipo do parametro seja diferente do configurado.
	 */
	void storeValorParametro(Long idFilial, String nomeParametro, Object valorParametro);

	/**
	 * Converte um valor em uma moeda de um pa�s para o valor de outra moeda em 
	 * outro pais.
	 *
	 * @param idPaisOrigem Indica o pais de origem.
	 * @param idMoedaOrigem Indica a moeda de origem.
	 * @param idPaisDestino Indica o pais de destino.
	 * @param idMoedaDestino Indica a moeda de destino.
	 * @param dataCotacao Data da cota��o a ser recuperada para a convers�o.
	 * @param valor Valor a ser convertido.
	 * @return Valor original convertido para a cota��o da moeda na data informada.
	 */
	BigDecimal converteValorMoeda(
		Long idPaisOrigem,
		Long idMoedaOrigem,
		Long idPaisDestino,
		Long idMoedaDestino, 
		Date dataCotacao, 
		BigDecimal valor);

	/**
	 * Consulta o valor da cota��o a ser utilizado para a convers�o.
	 * Utilizar este m�todo quando for necess�rio realizar uma grande quantidade
	 * de convers�es de acordo com uma mesma data da cota��o. O retorno do m�todo
	 * deve ser multiplicado ao valor na moeda do pa�s origem para obter-se o valor
	 * convertido.
	 * 
	 * @param idPaisOrigem Indica o pais de origem.
	 * @param idMoedaOrigem Indica a moeda de origem.
	 * @param idPaisDestino Indica o pais de destino.
	 * @param idMoedaDestino Indica a moeda de destino.
	 * @param dataCotacao Data da cota��o a ser recuperada para a convers�o.
	 * @return Valor da cota��o de convers�o para a moeda no pa�s de destino.
	 */
	BigDecimal getValorCotacaoMoeda(
		Long idPaisOrigem,
		Long idMoedaOrigem,
		Long idPaisDestino,
		Long idMoedaDestino, 
		Date dataCotacao);

	/**
	 * Recupera uma mensagem do sistema a partir da tabela RECURSO_MENSAGEM.
	 * 
	 * @param chave Identificador da mensagem.
	 * @return 
	 */
	String getMensagem(String chave);

	/**
	 * Recupera uma mensagem do sistema a partir da tabela RECURSO_MENSAGEM.
	 * 
	 * @param chave Identificador da mensagem.
	 * @param argumentos Argumentos da mensagem.
	 * @return 
	 */
	String getMensagem(String chave, Object[] argumentos);

	/**
	 * Retorna a Pessoa.
	 * 
	 * @param nrIdentificacao
	 * @param tpIdentificacao
	 * @return A instancia de Pessoa ou <code>null</code> caso n�o seja encontrado.
	 * @throws IllegalArgumentException caso seja informado parametros vazios.
	 */
	Pessoa getPessoa(String nrIdentificacao, String tpIdentificacao);

	/**
	 * Retorna a Pessoa. 
	 * 
	 * @param id identificador da Pessoa.
	 * @return
	 */
	Pessoa getPessoa(Long id);

	/**
	 * 
	 * @param id
	 * @param classeEspecializacao
	 * @param fetchPessoa <code>true</code> indica que ser� feito o <code>fetch join</code> da associa��o com Pessoa.
	 * 
	 * @return
	 */
	Object getPessoa(Long id, Class classeEspecializacao, boolean fetchPessoa);

	/**
	 * 
	 * @param nrIdentificacao
	 * @param tpIdentificacao
	 * @param classeEspecializacao Deve ser uma classe que possua uma associa��o com Pessoa. 
	 * @param fetchPessoa <code>true</code> indica que ser� feito o <code>fetch join</code> da associa��o com Pessoa.
	 * 
	 * @return A classe especializada informada.
	 * @throws IllegalArgumentException caso seja informado parametros vazios.
	 */
	Object getPessoa(String nrIdentificacao, String tpIdentificacao, Class classeEspecializacao, boolean fetchPessoa);

	/**
	 * M�todo que retorna as moedas do pais informado, ativo.
	 * 
	 * @param Long idPais
	 * @param Boolean blSomenteAtivos
	 * @return List lista de moeda do pais informado.
	 * @see com.mercurio.lms.configuracoes.model.MoedaPais
	 * */
	List<Moeda> getMoeda(Long idPais, Boolean blSomenteAtivos);

	/**
	 * Retorna uma moeda pelo c�digo ISO num�rico
	 * 
	 * @param nrIsoCode
	 * @return Moeda
	 */
	public Moeda getMoeda(Short nrIsoCode);

	/**
	 * M�todo que retorna moedaPais's do pais informado, ativo.
	 * 
	 * @param Long idPais
	 * @param Boolean blSomenteAtivos
	 * @return List lista de moedaPais do pais informado.
	 * @see com.mercurio.lms.configuracoes.model.MoedaPais
	 * */
	List<MoedaPais> getMoedasPais(Long idPais, Boolean blSomenteAtivos);


	/**
	 * Retorna um dom�nio atrav�s do seu valor.
	 * 
	 * @param domainName O nome do dom�nio.
	 * @param domainValue O valor do dom�nio.
	 * 
	 * @return DomainValue O valor do dom�nio pesquisado.
	 */
	public DomainValue getDomainValue(String domainName, String domainValue);

	public Contato findContatoByIdPessoaAndTipoContato(Long idPessoa, String tpContato);

	public List<Contato> findContatosByIdPessoaAndTipoContato(Long idPessoa, String tpContato);
	
	// Setters dos servi�os utilizados pela fachada
	void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService);

	void setParametroGeralService(ParametroGeralService parametroGeralService);

	void setPessoaService(PessoaService pessoaService);

	void setMoedaService(MoedaService moedaService);

	void setMoedaPaisService(MoedaPaisService moedaPaisService);

	void setDomainValueService(DomainValueService domainValueService); 
	
	void setRecursoMensagemService(RecursoMensagemService recursoMensagemService);
}