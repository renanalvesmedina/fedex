package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.edi.dto.ClienteLayoutEDIDTO;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.*;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SegmentoMercado;
import com.mercurio.lms.vendas.model.dao.ClienteDAO;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.*;

import static com.mercurio.lms.util.BigDecimalUtils.ZERO;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.clienteService"
 */
public class ClienteService extends CrudService<Cliente, Long> {
	private static final int MAX_LENGTH_SEGMENTO_MERCADO = 30;
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ContatoService contatoService;
	private InscricaoEstadualService inscricaoEstadualService;
	private ConfiguracoesFacade configuracoesFacade;
	private HistoricoWorkflowService historicoWorkflowService;
	private RegionalFilialService regionalFilialService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private MunicipioService municipioService;
	private PaisService paisService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;

	/**
	 * Recupera uma instância de <code>Cliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Cliente findById(java.lang.Long id) {
		return (Cliente)super.findById(id);
	}

	@Override
	public Cliente findByIdInitLazyProperties(Long id, boolean lazyProperties) {
		return (Cliente) super.findByIdInitLazyProperties(id, lazyProperties);
	}

	/**
	 * Retorna o cliente com a pessoa 'fetchada';
	 * 
	 * @author Mickaël Jalbert
	 * @since 30/05/2006
	 * 
	 * @param Long idCliente
	 * @return Cliente
	 * */
	public Cliente findByIdComPessoa(Long idCliente) {
		return getClienteDAO().findByIdComPessoa(idCliente);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param nrIdentificacao
	 * @return Cliente
	 */
	public Cliente findByNrIdentificacao(String nrIdentificacao) {
		return getClienteDAO().findByNrIdentificacaoValidaDadosBasicos(nrIdentificacao, Boolean.FALSE);
	}

	@SuppressWarnings("unchecked")
	public List<Long> findBydNrsIdentificacao(List<String> nrsIdentificacao){
		return getClienteDAO().findClienteByNrIdentificacao(nrsIdentificacao);
	}
	
	public Cliente findByNrIdentificacaoValidaDadosBasicos(String nrIdentificacao) {
		return getClienteDAO().findByNrIdentificacaoValidaDadosBasicos(nrIdentificacao, Boolean.TRUE);
	}

	public Cliente findByNumeroIdentificacao(String nrIdentificacao) {
		return getClienteDAO().findByNumeroIdentificacao(nrIdentificacao);
	}

	public List<Long> findIdsClienteCCT(){
		return getClienteDAO().findIdsClienteCCT();
	}
	
	public Cliente findByNumeroIdentificacaoParaCCT(String nrIdentificacao) {
		return getClienteDAO().findByNumeroIdentificacaoParaCCT(nrIdentificacao);
	}
	
	public void executeAprovacaoWKFilialComercial(Long idCliente){
		getClienteDAO().executeAprovacaoWKFilialComercial(idCliente);
	}

	public void executeReprovacaoWKFilialComercial(Long idCliente){
		getClienteDAO().executeReprovacaoWKFilialComercial(idCliente);
	}
	
	public void executeAprovacaoWKFilialOperacional(Long idCliente){
		getClienteDAO().executeAprovacaoWKFilialOperacional(idCliente);
	}

	public void executeReprovacaoWKFilialOperacional(Long idCliente){
		getClienteDAO().executeReprovacaoWKFilialOperacional(idCliente);
	}
	
	public void executeAprovacaoWKFilialFinanceiro(Long idCliente){
		getClienteDAO().executeAprovacaoWKFilialFinanceiro(idCliente);
	}

	public void executeReprovacaoWKFilialFinanceiro(Long idCliente){
		getClienteDAO().executeReprovacaoWKFilialFinanceiro(idCliente);
	}
	
	/**
	 * Busca um único cliente pelo CNPJ parcial(primeiro da lista)<br/> 
	 * Utilizado para extrair informações gerais sobre um CNPJ parcial
	 * 
	 * @author Vagner Huzalo
	 * @since 10/12/2007
	 * 
	 * @param cnpjParcial - CNPJ parcial com 8 dígitos
	 * @return <code>Cliente</code>
	 */
	public Cliente findByCNPJParcial(String cnpjParcial){
	
		return getClienteDAO().findByCNPJParcial(cnpjParcial);
	}
	
	/**
	 * Busca um Cliente a partir de um id de Pedido Coleta
	 * Usando em casos onde se precisa performance na busca, não precisando nesses casos
	 * buscar o Pedido Coleta para depois dar um "get" em cliente.
	 * @param idPedidoColeta
	 * @return
	 */
	public Cliente findByIdPedidoColeta(Long idPedidoColeta){
		return getClienteDAO().findByIdPedidoColeta(idPedidoColeta);
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @deprecated
	 * @see com.mercurio.lms.vendas.model.service.ManterClienteService#getRowCount(Map)
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	@Override
	public Integer getRowCount(Map criteria) {
		return getClienteDAO().getRowCount(new TypedFlatMap(criteria)); //, def, null, null);
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 * @deprecated
	 * @see com.mercurio.lms.vendas.model.service.ManterClienteService#findPaginated(Map)
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public ResultSetPage findPaginated(TypedFlatMap criteria) {	
		return null;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void removeById(Long id) {
		super.removeById(id);
		try {
			pessoaService.removeById(id);	
		} catch(Exception e) {
			//ignora de Pessoa
		}
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 * 
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
		for(Long id : ids) {
			try {
				pessoaService.removeById(id);	
			} catch(Exception e) {
				//sem tratamentos, simplesmente ignora. Pois, provavelmente, há dependencias da Pessoa.
			}
		}
	}

	/**
	 * Salva pessoa antes de salvar cliente
	 * @param bean entidade a ser armazenada (Cliente)
	 * @return entidade que foi armazenada. 
	 */
	@Override
	protected Cliente beforeStore(Cliente cliente) {
		return beforeStore(cliente, null);
	}
	protected Cliente beforeStore(Cliente cliente, Usuario usuarioAtualizacao) {
		// atribuir valor para o boolean responsável de frete
		cliente.setBlResponsavelFrete(Boolean.TRUE);		

		// atribui o id do usuario que fez o update
		cliente.setUsuarioByIdUsuarioAlteracao(usuarioAtualizacao == null ? (Usuario) SessionContext.getUser() : usuarioAtualizacao);	

		// se o responsável for nulo, atribuir ele mesmo
		if (cliente.getCliente() == null) {
			cliente.setCliente(cliente);
		}			

		//Executar o beforeStore padrão (beforeInsert e beforeUpdate)
		cliente = beforeStoreLocal(cliente, usuarioAtualizacao);
		return cliente;
	}
	
	/**
	 * método que efetua mesma chamada do ancestral 'beforeStore', porem passando o Usuario de atualizacao, para que nao seja pega usuario de sessao
	 * @param bean
	 * @param usuarioAtualizacao
	 * @return
	 * LMSA-6786
	 */
    protected Cliente beforeStoreLocal(Cliente bean, Usuario usuarioAtualizacao) {
        Serializable id = this.getDao().getIdentifier(bean);
        Cliente beanReturn = bean;
        if (id == null) {
            beanReturn = beforeInsert(bean, usuarioAtualizacao);
        } else {
            beanReturn = beforeUpdate(bean);
        }
        return beanReturn;
    }
	

	/**
	 * Seta a data atual no campo dhInclusao
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeInsert(java.lang.Object)
	 */
	protected Cliente beforeInsert(Cliente cliente, Usuario usuarioAtualizacao) {
		String nrIdentificacao = cliente.getPessoa().getNrIdentificacao();
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			Object pessoa = configuracoesFacade.getPessoa(nrIdentificacao, cliente.getPessoa().getTpIdentificacao().getValue(), Cliente.class, true);
			if(pessoa != null) {
				throw new BusinessException("LMS-01022");
			}
		}

		// incluir pessoa se não existe
		cliente.getPessoa().setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

		// atribui o id do usuário de inserção
		cliente.setUsuarioByIdUsuarioInclusao(usuarioAtualizacao == null ? SessionUtils.getUsuarioLogado() : usuarioAtualizacao);

		// atribui a data de inserção
		cliente.setDtGeracao(JTDateTimeUtils.getDataAtual());

		Long nrConta = this.getClienteDAO().findNewNrConta();
		if (nrConta != null) {
			cliente.setNrConta(nrConta);
		}

		return super.beforeInsert(cliente);
	}
	@Override
	protected Cliente beforeInsert(Cliente cliente) {
		return beforeInsert(cliente, null);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(Cliente bean) {
		return store(bean, null);
	}
	public java.io.Serializable store(Cliente bean, Usuario usuarioAtualizacao) {
		// os eventos before podem ter criado um objeto diferente para
		// ser persistido portando salva este objeto.
		Cliente storeBean = beforeStore(bean, usuarioAtualizacao);
		
		storeBean.setDefaultTpFormaCobranca();
		storeBean.setDefaultBlEmissaoDiaNaoUtil();
		storeBean.setDefaultBlEmissaoSabado();
		storeBean.setDefaultBldesconsiderarDificuldade();
		storeBean.setDefaultBlGerarParcelaFreteValorLiquido();
		
		this.getClienteDAO().store(storeBean);

		return storeBean.getIdCliente();
	}

	public java.io.Serializable saveClienteBasico(Cliente cliente) {
		return saveClienteBasico(cliente, null);
	}
	
	@SuppressWarnings("unchecked")
	public java.io.Serializable saveClienteBasico(Cliente cliente, Usuario usuarioAtualizacao) {
		Pessoa pessoa = cliente.getPessoa();
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();

		if(pessoa.getIdPessoa() == null) {
			pessoa.setBlAtualizacaoCountasse(Boolean.FALSE);
			pessoaService.store(cliente);
		} else {
			pessoa = pessoaService.findById(pessoa.getIdPessoa());
			pessoa.setNmPessoa(cliente.getPessoa().getNmPessoa());
			cliente.setPessoa(pessoa);
		}

		if (enderecoPessoa.getIdEnderecoPessoa() == null) {	
			pessoa.setEnderecoPessoa(null);
			//enderecoPessoaService.store(enderecoPessoa, usuarioAtualizacao);
			enderecoPessoaService.store(enderecoPessoa, null, false, usuarioAtualizacao);

			if (pessoa.getContatosByIdPessoaContatado() != null) {
				List<Contato> contatos = pessoa.getContatosByIdPessoaContatado();
				if(contatos != null) {
					for(Contato contato : contatos) {
						contatoService.store(contato);
					}
				}
			}

			if (enderecoPessoa.getTelefoneEnderecos() != null
					&& !enderecoPessoa.getTelefoneEnderecos().isEmpty()
			) {
				List<TelefoneEndereco> telefones = enderecoPessoa.getTelefoneEnderecos();
				for(TelefoneEndereco telefoneEndereco : telefones) {
					telefoneEnderecoService.store(telefoneEndereco);
				}
			}
			pessoaService.store(cliente);
		}

		List<InscricaoEstadual> ies = pessoa.getInscricaoEstaduais();
		if(ies != null) {
			for(InscricaoEstadual inscricaoEstadual : ies) {
				inscricaoEstadualService.store(inscricaoEstadual);
			}
		}

		setFilialRegionalResponsavel(cliente);

		cliente.setBlDificuldadeEntrega(Boolean.FALSE);
		cliente.setBlRetencaoComprovanteEntrega(Boolean.FALSE);
		cliente.setBlDivulgaLocalizacao(Boolean.FALSE);
		
		if (cliente.getBlEmissorNfe() == null){
			cliente.setBlEmissorNfe(Boolean.TRUE);
		}

		return store(cliente, usuarioAtualizacao);
	}

	public Serializable saveClienteBasico(TypedFlatMap parameters) {
		return this.saveCliente(parameters, Boolean.TRUE);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void saveEnderecoCliente(TypedFlatMap parameters, Pessoa pessoa){
		setEnderecoPessoa(parameters, pessoa);
		EnderecoPessoa ep = pessoa.getEnderecoPessoa();
		//atribui null no enderecopessoa para evitar erro de referencia circular ao salvar... 
		pessoa.setEnderecoPessoa(null);
		if(ep.getIdEnderecoPessoa() == null) {
			TipoEnderecoPessoa tipoEnderecoPessoa = getTipoEnderecoPessoa(pessoa.getTpPessoa().getValue(), (String) parameters.remove("origem"));
			tipoEnderecoPessoa.setEnderecoPessoa(ep);
			List tipoEnderecoPessoas = new ArrayList();
			tipoEnderecoPessoas.add(tipoEnderecoPessoa);
			ep.setTipoEnderecoPessoas(tipoEnderecoPessoas);

			setTelefoneEndereco(parameters, pessoa, pessoa.getTpPessoa().getValue(), ep);
			setContato(parameters, pessoa);
			
		}
		Long id = (Long)enderecoPessoaService.store(ep,null,false);
		ep.setIdEnderecoPessoa(id);
		
		//Atribui novamente o endereço à pessoa para, até que enfim, salvar. 
		pessoa.setEnderecoPessoa(ep);
		pessoaService.store(pessoa);
		
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Serializable saveCliente(TypedFlatMap parameters, Boolean validaSituacaoTributaria) {
		String tpSituacaoTributaria = (String)parameters.get("tpSituacaoTributaria");
		String nrInscricaoEstadual = (String)parameters.get("nrInscricaoEstadual");
		if (validaSituacaoTributaria != null && validaSituacaoTributaria.compareTo(Boolean.TRUE) == 0){
			if (!validateSituacaoTributaria(tpSituacaoTributaria, nrInscricaoEstadual)) {
				return null;
			}			
		}		
		
		String origem = (String) parameters.remove("origem");
		Pessoa pessoa = getPessoa(parameters);
		String tpPessoa = pessoa.getTpPessoa().getValue();

		setEnderecoPessoa(parameters, pessoa);
		EnderecoPessoa ep = pessoa.getEnderecoPessoa();
		if(ep.getIdEnderecoPessoa() == null) {
			TipoEnderecoPessoa tipoEnderecoPessoa = getTipoEnderecoPessoa(tpPessoa, origem);
			tipoEnderecoPessoa.setEnderecoPessoa(ep);
			List tipoEnderecoPessoas = new ArrayList();
			tipoEnderecoPessoas.add(tipoEnderecoPessoa);
			ep.setTipoEnderecoPessoas(tipoEnderecoPessoas);

			setTelefoneEndereco(parameters, pessoa, tpPessoa, ep);
			setContato(parameters, pessoa);
		}
		
		Municipio municipio = municipioService.findById(ep.getMunicipio().getIdMunicipio());
		
		String nrIdentificacao = parameters.getString("nrIdentificacao");
		String tpIdentificacao = parameters.getString("tpIdentificacao");
		Pais pais1 = parameters.get("idPais") == null ? null : paisService.findById((Long)parameters.get("idPais")); 
		Pais pais2 = municipio 									== null ? null 
				   : municipio.getUnidadeFederativa() 			== null ? null 
			       : municipio.getUnidadeFederativa().getPais() == null ? null 
			       : municipio.getUnidadeFederativa().getPais();
		
		if((pais1 != null && ConstantesAmbiente.SG_PAIS_BRASIL.equals(pais1.getSgPais())) || (pais2 != null && ConstantesAmbiente.SG_PAIS_BRASIL.equals(pais2.getSgPais()))){
			/* LMS-1245 */
			ep.setNrCep(StringUtils.leftPad(ep.getNrCep(), 8, '0'));

			boolean empty = nrIdentificacao == null || "".equals(nrIdentificacao) || tpIdentificacao == null || "".equals(tpIdentificacao);
			boolean valid = "CNPJ".equals(parameters.get("tpIdentificacao")) || "CPF".equals(parameters.get("tpIdentificacao"));
			if (empty || !valid) {
				throw new BusinessException("LMS-04229");
			}
		}
		
		InscricaoEstadual ie = storeInscricaoEstadual(parameters, pessoa, ep.getMunicipio().getUnidadeFederativa(), tpPessoa);
		if (ie != null) {
			List ies = new ArrayList();
			ies.add(ie);
			pessoa.setInscricaoEstaduais(ies);
		}
		Cliente c = getCliente(pessoa, origem);

		Long idRamoAtividade = parameters.getLong("idRamoAtividade");
		if (idRamoAtividade != null) {
			RamoAtividade ramoAtividade = new RamoAtividade();
			ramoAtividade.setIdRamoAtividade(idRamoAtividade);
			c.setRamoAtividade(ramoAtividade);
		}

		saveClienteBasico(c);

		Map inscricaoEstadual = new HashMap();
		inscricaoEstadual.put("idInscricaoEstadual", ie.getIdInscricaoEstadual());
		inscricaoEstadual.put("nrInscricaoEstadual", ie.getNrInscricaoEstadual());
		List inscricoes = new ArrayList();
		inscricoes.add(inscricaoEstadual);

		UnidadeFederativa uf = ep.getMunicipio().getUnidadeFederativa();
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idCliente", c.getIdCliente());
		retorno.put("idPessoa", pessoa.getIdPessoa());
		retorno.put("nmPessoa", pessoa.getNmPessoa());
		retorno.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa));
		retorno.put("idInscricaoEstadual", ie.getIdInscricaoEstadual());
		retorno.put("nrCep", ep.getNrCep());
		retorno.put("nmMunicipio", municipio.getNmMunicipio());
		retorno.put("idMunicipio", municipio.getIdMunicipio());
		retorno.put("sgUnidadeFederativa", municipio.getUnidadeFederativa().getSgUnidadeFederativa());
		retorno.put("idUnidadeFederativa", uf.getIdUnidadeFederativa());
		retorno.put("dsComplemento", ep.getDsComplemento());
		
		if(ep.getNrEndereco() != null){
			retorno.put("nrEndereco", ep.getNrEndereco().trim());
		}
		
		if (ep.getTipoLogradouro().getDsTipoLogradouro() != null) {
			retorno.put("dsEndereco", ep.getTipoLogradouro().getDsTipoLogradouro().getValue() + " " + ep.getDsEndereco());
		} else {
			retorno.put("dsEndereco", ep.getDsEndereco());
		}
		retorno.put("dsTipoLogradouro", ep.getTipoLogradouro().getDsTipoLogradouro());
		retorno.put("inscricaoEstadual", inscricoes);
		retorno.put("tpCliente", c.getTpCliente().getValue());

		return retorno;
	}
	
	private InscricaoEstadual storeInscricaoEstadual(TypedFlatMap parameters, Pessoa p, UnidadeFederativa uf, String tpPessoa) {
		InscricaoEstadual insc = inscricaoEstadualService.findByPessoaIndicadorPadrao(p.getIdPessoa(), Boolean.TRUE);
		String nrInscricaoEstadual = parameters.getString("nrInscricaoEstadual");
		if(StringUtils.isNotBlank(nrInscricaoEstadual)) {
			nrInscricaoEstadual = nrInscricaoEstadual.toUpperCase();
		}
		if(insc != null && (insc.getNrInscricaoEstadual().equals(nrInscricaoEstadual) || (StringUtils.isBlank(nrInscricaoEstadual) && "ISENTO".equals(insc.getNrInscricaoEstadual())))
		) {
			return insc;
		}
		InscricaoEstadual ie = new InscricaoEstadual();
		ie.setPessoa(p);
		ie.setUnidadeFederativa(uf);
		// TODO Validar alterações da InscricaoEstadual
		if(insc != null) {
			ie.setBlIndicadorPadrao(FALSE);
		} else {
			ie.setBlIndicadorPadrao(Boolean.TRUE);
		}
		// TODO Validar alterações da InscricaoEstadual
		ie.setTpSituacao(new DomainValue("A"));
		if("F".equalsIgnoreCase(tpPessoa) || StringUtils.isBlank(nrInscricaoEstadual)) {
			ie.setNrInscricaoEstadual("ISENTO");
		} else {
			ie.setNrInscricaoEstadual(nrInscricaoEstadual);
			// TODO Validar alterações da InscricaoEstadual
		}
		
		if (Boolean.TRUE.equals(parameters.getBoolean("atualizaIETipoTributacao"))){
			TipoTributacaoIE tipoTributacao = new TipoTributacaoIE();
			tipoTributacao.setInscricaoEstadual(ie);
			tipoTributacao.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
			if ("ISENTO".equalsIgnoreCase(ie.getNrInscricaoEstadual())){
				tipoTributacao.setTpSituacaoTributaria(new DomainValue("NC"));
			}else{
				tipoTributacao.setTpSituacaoTributaria(new DomainValue("CO"));
			}
			tipoTributacao.setBlAceitaSubstituicao(true);
			tipoTributacao.setBlIncentivada(false);
			tipoTributacao.setBlIsencaoExportacoes(false);
		}
		return ie;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTelefoneEndereco(TypedFlatMap parameters, Pessoa p, String tpPessoa, EnderecoPessoa ep) {
		TelefoneEndereco te = null;
		Integer ddd = parameters.getInteger("nrDdd");
		Long numero = parameters.getLong("nrTelefone");
		if(ddd != null && numero != null) {
			te = new TelefoneEndereco();
			DomainValue tpUsuTelefone = new DomainValue("FO");
			DomainValue tpTelefone = null;
			if("F".equalsIgnoreCase(tpPessoa))
				tpTelefone = new DomainValue("R");
			else
				tpTelefone = new DomainValue("C");
			te.setTpTelefone(tpTelefone);
			te.setTpUso(tpUsuTelefone);
			te.setNrDdd(ddd.toString());
			te.setNrTelefone(numero.toString());
			te.setEnderecoPessoa(ep);
			te.setPessoa(p);

			List telefoneEnderecos = new ArrayList();
			telefoneEnderecos.add(te);
			ep.setTelefoneEnderecos(telefoneEnderecos);
			p.setTelefoneEnderecos(telefoneEnderecos);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setContato(TypedFlatMap parameters, Pessoa p) {
		Contato c = null;
		String nmContato = parameters.getString("nmContato");
		if(StringUtils.isNotBlank(nmContato)) {
			DomainValue tpContato = new DomainValue("CN");
			c = new Contato();
			c.setTpContato(tpContato);
			c.setNmContato(nmContato);
			c.setPessoa(p);
			List contatos = new ArrayList();
			contatos.add(c);
			p.setContatosByIdPessoaContatado(contatos);
		}
	}
	
	private Pessoa getPessoa(TypedFlatMap parameters) {
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(parameters.getLong("idPessoa"));
		pessoa.setNrIdentificacao(parameters.getString("nrIdentificacao"));
		pessoa.setNmPessoa(parameters.getString("nmPessoa"));
		
		if (StringUtils.isNotBlank(parameters.getString("tpPessoa"))) {
		pessoa.setTpPessoa(new DomainValue(parameters.getString("tpPessoa")));
		}else{
			pessoa.setTpPessoa(new DomainValue("J"));
		}
		String tpIdentificacao = parameters.getString("tpIdentificacao");
		if (StringUtils.isNotBlank(tpIdentificacao)) {
			pessoa.setTpIdentificacao(new DomainValue(tpIdentificacao));
		}
		return pessoa;
	}
	
	private Cliente getCliente(Pessoa pessoa, String origem) {
		Cliente cliente = new Cliente();
		cliente.setPessoa(pessoa);
		cliente.setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_POTENCIAL));
		cliente.setTpSituacao(new DomainValue(ConstantesVendas.SITUACAO_ATIVO));
		cliente.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		cliente.setCliente(cliente);
		
		Filial filialSessao = SessionUtils.getFilialSessao();
		cliente.setFilialByIdFilialCobranca(filialSessao);
		cliente.setFilialByIdFilialAtendeOperacional(filialSessao);
		cliente.setFilialByIdFilialAtendeComercial(filialSessao);

		cliente.setBlSeparaFaturaModal(TRUE);		
		cliente.setBlPermiteCte(FALSE);
		cliente.setBlObrigaSerie(FALSE);
		cliente.setBlGeraReciboFreteEntrega(FALSE);
		cliente.setBlPermanente(FALSE);
		cliente.setBlResponsavelFrete(FALSE);
		cliente.setBlBaseCalculo(FALSE);
		cliente.setBlCobraReentrega(TRUE);
		cliente.setBlCobraDevolucao(TRUE);
		cliente.setBlColetaAutomatica(FALSE);
		cliente.setBlFobDirigido(FALSE);
		cliente.setBlFobDirigidoAereo(FALSE);
		cliente.setBlPesoAforadoPedagio(FALSE);
		cliente.setBlIcmsPedagio(FALSE);
		cliente.setBlIndicadorProtesto(FALSE);
		cliente.setBlMatriz(FALSE);
		cliente.setBlCobrancaCentralizada(FALSE);
		cliente.setBlFaturaDocsEntregues(FALSE);
		cliente.setBlPreFatura(FALSE);
		cliente.setBlRessarceFreteFob(FALSE);
		cliente.setBlEmiteBoletoCliDestino(FALSE);
		cliente.setBlAgrupaFaturamentoMes(FALSE);
		cliente.setBlAgrupaNotas(FALSE);
		cliente.setBlCadastradoColeta(FALSE);
		cliente.setBlOperadorLogistico(FALSE);
		cliente.setBlFronteiraRapida(FALSE);
		cliente.setBlAgendamentoPessoaFisica(FALSE);
		cliente.setBlAgendamentoPessoaJuridica(FALSE);
		cliente.setBlFaturaDocsConferidos(FALSE);
		cliente.setDtGeracao(JTDateTimeUtils.getDataAtual());
		cliente.setPcDescontoFreteCif(ZERO);
		cliente.setPcDescontoFreteFob(ZERO);
		cliente.setNrCasasDecimaisPeso(Short.valueOf("2"));
		cliente.setBlObrigaRecebedor(FALSE);
		cliente.setTpDificuldadeColeta(new DomainValue("0"));
		cliente.setTpDificuldadeEntrega(new DomainValue("0"));
		cliente.setTpDificuldadeClassificacao(new DomainValue("0"));
		cliente.setTpFrequenciaVisita(new DomainValue("M"));
		cliente.setTpFormaArredondamento(new DomainValue("P"));
		cliente.setTpCobranca(new DomainValue("4"));
		cliente.setTpPesoCalculo(new DomainValue("A"));
		cliente.setPcJuroDiario(ZERO);
		cliente.setBlFaturaDocReferencia(FALSE);
		cliente.setBlDificuldadeEntrega(FALSE);
		cliente.setBlRetencaoComprovanteEntrega(FALSE);
		cliente.setBlDivulgaLocalizacao(FALSE);
		cliente.setBlMtzLiberaRIM(FALSE);
		cliente.setBlValidaCobrancDifTdeDest(FALSE);
		cliente.setBlCobrancaTdeDiferenciada(FALSE);

		if("col".equalsIgnoreCase(origem)) {
			cliente.setBlCadastradoColeta(TRUE);
		} else {
			cliente.setBlCadastradoColeta(FALSE);
		}
		return cliente;
	}
	
	private TipoEnderecoPessoa getTipoEnderecoPessoa(String tpPessoa, String origem) {
		DomainValue tpEndereco = null;
		if("col".equalsIgnoreCase(origem)) {
			tpEndereco = new DomainValue("COL");
		} else {
			if("F".equalsIgnoreCase(tpPessoa))
				tpEndereco = new DomainValue("RES");
			else
				tpEndereco = new DomainValue("COM");
		}
		TipoEnderecoPessoa tep = new TipoEnderecoPessoa();
		tep.setTpEndereco(tpEndereco);
		return tep;
	}
	
	private void setEnderecoPessoa(TypedFlatMap parameters, Pessoa pessoa) {
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();

		Long idEnderecoPessoa = parameters.getLong("idEnderecoPessoa");
		enderecoPessoa.setIdEnderecoPessoa(idEnderecoPessoa);
		if(idEnderecoPessoa == null) {
			enderecoPessoa.setDsBairro(parameters.getString("dsBairro"));
			enderecoPessoa.setDsComplemento(parameters.getString("dsComplemento"));
			enderecoPessoa.setDsEndereco(parameters.getString("dsEndereco"));
			enderecoPessoa.setNrCep(parameters.getString("nrCep"));
			
			if(parameters.get("nrEndereco") != null){
				enderecoPessoa.setNrEndereco(parameters.get("nrEndereco").toString().trim());
			}
			
			enderecoPessoa.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		}

		Long idMunicipio = parameters.getLong("idMunicipio");
		if (idMunicipio != null) {
			Municipio municipio = new Municipio();
			municipio.setIdMunicipio(idMunicipio);
			enderecoPessoa.setMunicipio(municipio);
		}

		Long idUnidadeFederativa = parameters.getLong("idUnidadeFederativa");
		if (idUnidadeFederativa != null) {
			UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
			unidadeFederativa.setIdUnidadeFederativa(idUnidadeFederativa);
			enderecoPessoa.getMunicipio().setUnidadeFederativa(unidadeFederativa);
		}

		Long idTipoLogradouro = parameters.getLong("idTipoLogradouro");
		if (idTipoLogradouro != null) {
			TipoLogradouro tipoLogradouro = new TipoLogradouro();
			tipoLogradouro.setIdTipoLogradouro(idTipoLogradouro);
			String dsTipoLogradouro = parameters.getString("dsTipoLogradouro");
			if (StringUtils.isNotBlank(dsTipoLogradouro)) {
				tipoLogradouro.setDsTipoLogradouro(new VarcharI18n(dsTipoLogradouro));
			}
			enderecoPessoa.setTipoLogradouro(tipoLogradouro);
		}

		pessoa.setEnderecoPessoa(enderecoPessoa);
		enderecoPessoa.setPessoa(pessoa);
	}
	
	private boolean validateSituacaoTributaria(String tpSituacaoTributaria, String nrInscricaoEstadual) {
		Boolean isIsento = isInscricaoEstadualIsento(nrInscricaoEstadual);		
		if (tipoTributacaoIEService.validateParametroGeralTpSituacaoTributariaAceitaIsento(tpSituacaoTributaria, isIsento)
		&& tipoTributacaoIEService.validateParametroGeralTpSituacaoTributariaAlerta(tpSituacaoTributaria, isIsento)) return true;
		return false;
	}

	private boolean isInscricaoEstadualIsento(String nrInscricaoEstadual) {
		return nrInscricaoEstadual.equalsIgnoreCase("isento");
	}
	
	/**
	 * @author José Rodrigo Moraes
	 * @since 14/06/2006
	 * 
	 * Método utilizado para buscar somente os dados necessários para a lookup de cliente
	 * 
	 * @param nrIdentificacao Número de identificacao do cliente
	 * @param tpIdentificacao Tipo de identificação do cliente
	 * @return Lista com clientes aninhados
	 */
	@SuppressWarnings("rawtypes")
	public List findLookupSimplificado(String nrIdentificacao, String tpIdentificacao) {
		return getClienteDAO().findLookupSimplificado(nrIdentificacao, tpIdentificacao, null, null);
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 14/06/2006
	 * 
	 * Método utilizado para buscar somente os dados necessários para a lookup de cliente
	 * Normalmente utilizado nas abas Cad, pois pode validar se o cliente está Ativo
	 * 
	 * @param nrIdentificacao Número de identificacao do cliente
	 * @param tpIdentificacao Tipo de identificação do cliente
	 * @param tpSituacao Situacao do Cliente
	 * @return Lista com clientes aninhados
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupSimplificadoAbaCad(String nrIdentificacao, String tpIdentificacao, String tpSituacao, String tpCliente) {
		List retorno = new ArrayList();

		if (!StringUtils.isBlank(nrIdentificacao)) {
			List clientes = new ArrayList();

			clientes = getClienteDAO().findLookupSimplificado(nrIdentificacao,tpIdentificacao, tpSituacao, tpCliente);

			for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				Cliente cliente = (Cliente) iter.next();
	
				TypedFlatMap map = new TypedFlatMap();
				map.put("pessoa.nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				map.put("idCliente", cliente.getIdCliente());
				map.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
				map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa().getTpIdentificacao(),
																						   cliente.getPessoa().getNrIdentificacao()));
				map.put("pessoa.tpIdentificacao",cliente.getPessoa().getTpIdentificacao());
				map.put("pessoa.idPessoa",cliente.getPessoa().getIdPessoa());
	
				if (StringUtils.isNotBlank(tpSituacao)) {
					map.put("tpSituacao",cliente.getTpSituacao());
				}
				retorno.add(map);
			}
		}
		
		return retorno;	
	}

	@SuppressWarnings("rawtypes")
	public List findLookupByNomeCliente(Map criteria) {
		return super.findLookup(criteria);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findLookup(Map criteria) {
		Map pessoa = (Map) criteria.get("pessoa");
		String nrIdentificacao = MapUtils.getString(pessoa, "nrIdentificacao");
		if (!StringUtils.isBlank(nrIdentificacao)) {
			pessoa.put("nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}

		List resultado = super.findLookup(criteria);
		if(resultado != null && resultado.size() > 0 ) {
			if (resultado != null && !resultado.isEmpty()) {
				Pessoa p = ((Cliente)resultado.get(0)).getPessoa();
				p.setNrIdentificacao(FormatUtils.formatIdentificacao(p.getTpIdentificacao(), p.getNrIdentificacao()));
			}
		} 
		return resultado;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupClientesAtivos(Map criteria) {
		criteria.put("tpSituacao", "A");
		return findLookup(criteria);
	}

	@SuppressWarnings("rawtypes")
	public List findClientesByEnderecoVigente(Long idMunicipio) {
		return getClienteDAO().findClientesByEnderecoVigente(idMunicipio);
	}

	/**
	 * Método que retorna uma list de clientes.
	 * Possui telefone como criteria, sendo opcional.
	 * @param TypedFlatMap criteria
	 * @return ResultSetPage
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginatedClientesByTelefone(TypedFlatMap criteria) {
		if (!StringUtils.isBlank(criteria.getString("pessoa.nrIdentificacao"))){
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.clearIdentificacao(criteria.getString("pessoa.nrIdentificacao")));
		}
		ResultSetPage rsp = this.getClienteDAO().findPaginatedClientesByTelefone(criteria, FindDefinition.createFindDefinition(criteria));

		List list = rsp.getList();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			TypedFlatMap map = new TypedFlatMap((Map) it.next());
			Map pessoa = (Map) map.get("pessoa");
			Map tpIdentificacaoMap = (Map) pessoa.get("tpIdentificacao");
			pessoa.put("enderecoFormatado", formatEndereco(map));

			if(tpIdentificacaoMap != null && !StringUtils.isBlank((String)pessoa.get("nrIdentificacao"))) {
				String tpIdentificacao = (String) tpIdentificacaoMap.get("value");
				String nmIdentificacao = (String) pessoa.get("nrIdentificacao");
				pessoa.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(tpIdentificacao, nmIdentificacao));
			}
		}
		return rsp;
	}

	@SuppressWarnings("rawtypes")
	private String formatEndereco(TypedFlatMap pessoa) {
		Map end = (Map) pessoa.get("enderecoPessoa");
		Map log = (Map) end.get("tipoLogradouro");
		Map mun = (Map) end.get("municipio");
		Map uf  = (Map) mun.get("unidadeFederativa");
		String dsEndereco = (String) end.get("dsEndereco");
		String nrEndereco = (String) end.get("nrEndereco");
		String dsComplemento = (String) end.get("dsComplemento");
		VarcharI18n dsTipoLogradouroI18n = (VarcharI18n)log.get("dsTipoLogradouro");
		String dsTipoLogradouro = null;
		if (dsTipoLogradouroI18n != null) {
			dsTipoLogradouro = dsTipoLogradouroI18n.getValue();
		}
		String nmMunicipio = (String) mun.get("nmMunicipio");
		String sgUnidadeFederativa = (String) uf.get("sgUnidadeFederativa");
		
		if (StringUtils.isNotBlank(dsTipoLogradouro) &&
				StringUtils.isNotBlank(dsEndereco) &&
				StringUtils.isNotBlank(nrEndereco)) {
			return FormatUtils.formatEnderecoPessoa(
					dsTipoLogradouro, dsEndereco, nrEndereco,
					dsComplemento, null, nmMunicipio, sgUnidadeFederativa);
		}
		return "";
	}

	/**
	 * Método que retorna a quantidade de clientes. 
	 * Possui telefone como criteria, sendo opcional. 
	 * @param TypedFlatMap criteria
	 * @return Integer
	 */
	public Integer getRowCountClientesByTelefone(TypedFlatMap criteria) {
		if (!StringUtils.isBlank(criteria.getString("pessoa.nrIdentificacao"))){
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.clearIdentificacao(criteria.getString("pessoa.nrIdentificacao")));
		}		
		return this.getClienteDAO().getRowCountClientesByTelefone(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Cliente findById2(Long idCliente) {
		return getClienteDAO().findById2(idCliente);
	}

	public Cliente findByIdComFilialCobranca(Long idCliente) {
		return getClienteDAO().findByIdComFilialCobranca(idCliente);
	}	

	/**
	 * @see validateCobrancaCentralizada(Cliente cliente)
	 * */
	public Boolean validateCobrancaCentralizada(Long idCliente) {
		Cliente cliente = findByIdComFilialCobranca(idCliente);
		return validateCobrancaCentralizada(cliente);
	}

	/**
	 * Valida se a filial de cobrança é igual a filial da sessão, o cliente informado tem que ter
	 * FilialByIdFilialCobranca 'fetchado'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 19/04/2006
	 * 
	 * @param Cliente cliente
	 * @return Boolean
	 * 
	 * @exception LMS-36100 'Cliente possui cobrança centralizada na filial {X}'
	 * */
	public Boolean validateCobrancaCentralizada(Cliente cliente) {
		//Se tem cobrança centralizada
		if (cliente.getBlCobrancaCentralizada().equals(Boolean.TRUE)) {
			//Se a filial de cobrança for diferente da filial da sessão
			if (!cliente.getFilialByIdFilialCobranca().equals(SessionUtils.getFilialSessao())) {
				throw new BusinessException("LMS-36100", new Object[]{cliente.getFilialByIdFilialCobranca().getSgFilial()});
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * Retorna o campo pcJuroDiario do cliente informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 25/07/2006
	 * 
	 * @param Long idCliente
	 * @return BigDecimal
	 */
	public BigDecimal findPcJuroDiario(Long idCliente) {
		return getClienteDAO().findPcJuroDiario(idCliente);
	}

	/**
	 * Retorna 'true' se a pessoa informada é um cliente ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isCliente(Long idPessoa){
		return getClienteDAO().isCliente(idPessoa);
	} 

	/**
	 * Retorna 'true' se a pessoa informada é um cliente especial ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isClienteEspecial(Long idPessoa){
		return getClienteDAO().isClienteEspecial(idPessoa);
	} 

	/**
	 * Retorna a lista de id dos cliente que fazem parte do grupo economico informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/10/2006
	 * 
	 * @param Long idGrupoEconomico
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List findIdClienteByGrupoEconomico(Long idGrupoEconomico){
		return getClienteDAO().findIdClienteByGrupoEconomico(idGrupoEconomico);
	}

	public Integer getRowCountByIdPessoa(Long idPessoa) {
		return getClienteDAO().getRowCountByIdPessoa(idPessoa);
	}

	@SuppressWarnings("rawtypes")
	public List findClienteByNrIdentificacao(String nrIndentificacao) {
		return findLookupClienteEndereco(null, nrIndentificacao, Boolean.FALSE);
	}

	@SuppressWarnings("rawtypes")
	public List findLookupClienteEndereco(Long idCliente) {
		return findLookupClienteEndereco(idCliente, null, Boolean.FALSE);
	}

	@SuppressWarnings("rawtypes")
	public List findClienteByNrIdentificacaoValidaDadosBasicos(String nrIndentificacao) {
		return findLookupClienteEndereco(null, nrIndentificacao, Boolean.TRUE);
	}
	
	@SuppressWarnings("rawtypes")
	public List findLookupClienteEnderecoValidaDadosBasicos(Long idCliente) {
		return findLookupClienteEndereco(idCliente, null, Boolean.TRUE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List findLookupClienteEndereco(Long idCliente, String nrIndentificacao, Boolean validaDadosBasicos) {
		if(idCliente == null) {
			if(StringUtils.isBlank(nrIndentificacao)) {
				throw new IllegalArgumentException("Os parametros 'idCliente' e 'nrIndentificacao' não podem ser nulos.");
			}
			nrIndentificacao = PessoaUtils.validateIdentificacao(nrIndentificacao);
			if(StringUtils.isBlank(nrIndentificacao)) {
				throw new BusinessException("LMS-00049");
			}
		}
		List<Map> clientes = getClienteDAO().findLookupClienteEndereco(idCliente, nrIndentificacao, validaDadosBasicos);
		for(Map cliente : clientes) {
			Map pessoa = (Map) cliente.get("pessoa");
			String nrIdentificacao = MapUtils.getString(pessoa, "nrIdentificacao");
			String tpIdentificacao = MapUtilsPlus.getStringOnMap(pessoa, "tpIdentificacao", "value", null);
			if(StringUtils.isNotBlank(nrIdentificacao) && StringUtils.isNotBlank(tpIdentificacao)){
				pessoa.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
			}
		}
		return clientes;
	}
	
	@SuppressWarnings("rawtypes")
	public List findComboRangeData(Long idCliente){
		return getClienteDAO().findComboRangeData(idCliente);
	}

	@SuppressWarnings("rawtypes")
	public List findLookupCliente(String nrIndentificacao) {
		return findLookupCliente(nrIndentificacao, null);
	}

	@SuppressWarnings("rawtypes")
	public List findLookupCliente(String nrIndentificacao, String tpCliente) {
		return findLookupCliente(nrIndentificacao, tpCliente, ConstantesVendas.SITUACAO_ATIVO);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupCliente(String nrIndentificacao, String tpCliente, String tpSituacao) {
		nrIndentificacao = PessoaUtils.validateIdentificacao(nrIndentificacao);
		if(StringUtils.isBlank(nrIndentificacao)) {
			throw new BusinessException("LMS-00049");
		}
		List<Map> clientes = getClienteDAO().findLookupCliente(nrIndentificacao, tpCliente, tpSituacao);
		for(Map cliente : clientes) {
			Map pessoa = (Map) cliente.get("pessoa");
			pessoa.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao((String)((Map)pessoa.get("tpIdentificacao")).get("value"), (String)pessoa.get("nrIdentificacao")));
		}
		return clientes;
	}

	public List<Cliente> findLookupClienteCustom(String nrIndentificacao, String nrIndentificacaoCustom) {
		if(StringUtils.isNotBlank(nrIndentificacao)) {
			nrIndentificacao = PessoaUtils.validateIdentificacao(nrIndentificacao);
		}
		if(StringUtils.isNotBlank(nrIndentificacaoCustom)) {
			nrIndentificacaoCustom = StringUtils.left(PessoaUtils.clearIdentificacao(nrIndentificacaoCustom), 9);
		}
		if(StringUtils.isBlank(nrIndentificacao) && StringUtils.isBlank(nrIndentificacaoCustom)) {
			throw new BusinessException("LMS-00049");
		}
		return getClienteDAO().findLookupClienteCustom(nrIndentificacao, nrIndentificacaoCustom);
	}

	public Cliente findClienteResponsavelByIdCliente(Long idCliente) {
		return getClienteDAO().findClienteResponsavelByIdCliente(idCliente); 
	}

	public SegmentoMercado findSegmentoMercadoByIdCliente(Long idCliente) {
		return getClienteDAO().findSegmentoMercadoByIdCliente(idCliente);
	}

	public Integer getRowCountResponsavelDiferente(Long idCliente) {
		return getClienteDAO().getRowCountResponsavelDiferente(idCliente);
	}

	@SuppressWarnings("rawtypes")
	public List findClientesByUsuario(Usuario usuario) {
		return getClienteDAO().findClientesByUsuario(usuario);
	}
	
	public List<Object[]> findCnpjAutorizadoByUser(Long idUsuario) {
		return getClienteDAO().findCnpjAutorizadoByUser(idUsuario);
	}

	public Object findClientePadraoByUsuario(Usuario usuario) {
		return getClienteDAO().findClientePadraoByUsuario(usuario);
	}

	/**
	 * Método de busca de um cliente pelo número de identificação
	 * @param nrIdentificacao Número de identificação
	 * @return Cliente
	 */
	public Cliente findClienteByNrIdentificacaoForDepositoContaCorrente(String nrIdentificacao) {
		return getClienteDAO().findClienteByNrIdentificacaoForDepositoContaCorrente(nrIdentificacao);
	}

	@SuppressWarnings("rawtypes")
	public List findClienteByNrIdentificacaoForAgrupamento(String nrIdentificacao){
		return getClienteDAO().findClienteByNrIdentificacaoForAgrupamento(nrIdentificacao);
	}

	public Cliente findClienteByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		return getClienteDAO().findByIdClienteTpSituacao(tpSituacaoParametro, dtVigenciaFinal, idCliente, idDivisaoCliente, idTabelaDivisaoCliente);
	}

	@SuppressWarnings("rawtypes")
	public List findLookupByUsuarioLogado(TypedFlatMap m) {
		return getClienteDAO().findLookupByUsuarioLogado(m);
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedByUsuarioLogado(TypedFlatMap m) {
		return getClienteDAO().findPaginatedByUsuarioLogado(m);
	}

	public Integer getRowCountByUsuarioLogado(TypedFlatMap m) {
		return getClienteDAO().getRowCountByUsuarioLogado(m);
	}

	public boolean findTomadorDoServicoComSeguroDiferenciado(Long idDoctoServico){ 
		return getClienteDAO().findTomadorDoServicoComSeguroDiferenciado(idDoctoServico);
	}

	/**
	 * Verifica a existencia de filiais relacionadas ao cliente matriz
	 * recebido como parametro. Caso existam ao menos uma filial uma excecao
	 * sera lancada com a mensagem representada pela <code>errorKey</code>
	 * 
	 * @author Luis Carlos Poletto
	 * 
	 * @param idCliente identificador do cliente matriz
	 * @param errorKey chave da mensagem de erro
	 */
	@SuppressWarnings("rawtypes")
	public void validateFiliaisMatriz(Long idCliente, String errorKey) {
		List filiais = findFiliaisMatriz(idCliente);
		if (filiais != null && !filiais.isEmpty()) {
			// se encontrou exibe a mensagem de erro
			throw new BusinessException(errorKey);
		}
	}

	/**
	 * Busca os identificadores das filiais deste cliente.
	 * 
	 * @param idCliente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findFiliaisMatriz(Long idCliente) {
		return getClienteDAO().findFiliaisClienteEspecial(idCliente);
	}

	/**
	 * Retorna o tipo de cliente e o id da filial comercial do cliente informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 15/01/2007
	 * 
	 * @param idUsuario
	 * @return map(tpCliente, idFilialComercial)
	 */
	@SuppressWarnings("rawtypes")
	public Map findTpClienteIdFilialComercial(Long idCliente) {
		return getClienteDAO().findTpClienteIdFilialComercial(idCliente);
	}

	/**
	 * Define, de acordo com o endereço, a filial e a regional responsáveis pelo cliente.
	 * @param cliente
	 */
	public void setFilialRegionalResponsavel(Cliente cliente) {
		if(cliente.getPessoa().getEnderecoPessoa() == null) {
			return;
		}
		// Busca Regional e Filial responsáveis.
		// Este par regional/filial é atribuido ao cliente que está sendo atualizado.
		
		/**
		 * Retirada da exceção foi uma  solicitação do Joelson em 29/04/2009
		 * Quest 18301
		 */
		RegionalFilial regionalFilial = regionalFilialService.findRegionalFilialResponsavel(
				cliente.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio(),
				Boolean.TRUE
		);
		if(regionalFilial == null) {
			return;
			//throw new BusinessException("LMS-29102");
		}
		Regional regional = regionalFilial.getRegional();
		Filial filial = regionalFilial.getFilial();

		if(validateAlteracaoFilialERegional(cliente, CampoHistoricoWorkflow.FCOM)){
			cliente.setRegionalComercial(regional);
			cliente.setFilialByIdFilialAtendeComercial(filial);
			cliente.setFilialByIdFilialComercialSolicitada(filial);
		}
		
		if(validateAlteracaoFilialERegional(cliente, CampoHistoricoWorkflow.FOPE)){
			cliente.setRegionalOperacional(regional);
			cliente.setFilialByIdFilialAtendeOperacional(filial);
			cliente.setFilialByIdFilialOperacionalSolicitada(filial);
		}
		
		if(validateAlteracaoFilialERegional(cliente, CampoHistoricoWorkflow.FCOB)){
			cliente.setRegionalFinanceiro(regional);
			if (filial.getFilialByIdFilialResponsavel() != null) {
				cliente.setFilialByIdFilialCobranca(filial.getFilialByIdFilialResponsavel());
				cliente.setFilialByIdFilialCobrancaSolicitada(filial.getFilialByIdFilialResponsavel());
			} else {
				cliente.setFilialByIdFilialCobranca(filial);
				cliente.setFilialByIdFilialCobrancaSolicitada(filial);
			}
		}
				
	}

	private Boolean validateAlteracaoFilialERegional(Cliente cliente, CampoHistoricoWorkflow campoHistoricoWorkflow){
		if(cliente.getIdCliente() != null && cliente.getTpCliente() != null && ClienteUtils.isParametroClienteEspecial(cliente.getTpCliente().getValue()) 
				&& historicoWorkflowService.validateHistoricoWorkflow(cliente.getIdCliente(), TabelaHistoricoWorkflow.CLIENTE, campoHistoricoWorkflow)){
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}
	
	public Long findIdClienteMatriz(Long idCliente){
		return getClienteDAO().findIdClienteMatriz(idCliente);
	}
	
	/**
	 * Verifica se a pessoa é cliente e, caso positivo, realiza a alterações necessárias.
	 * @param id
	 */
	public void executeChangeCliente(Long idPessoa, Usuario usuarioAtualizacao) {
		Cliente cliente = this.findById2(idPessoa);
		if(cliente == null) {
			return;
		}
		this.setFilialRegionalResponsavel(cliente);
		this.store(cliente, usuarioAtualizacao);
	}

	/**
	 * Obtem a lista de cliente através do idfilial
	 * @param idFilial
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> findListClientesByFilial(Long idFilial){
		return getClienteDAO().findListClientesByFilial(idFilial);
	}
	
	/**
	 * Obtem a lista de clientes através do idMunicipio (ID_ENDERECO_PESSOA.ID_MUNICIPIO) 
	 * @param idMunicipio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> findListClientesByMunicipio(Long idMunicipio){
		return getClienteDAO().findListClientesByMunicipio(idMunicipio);
	}
	
	public Cliente findClienteByIdInitialazeResponsavelFrete(Long idCliente) {
		Cliente clienteLoaded = findById(idCliente);
		if (clienteLoaded != null) {
			Hibernate.initialize(clienteLoaded.getCliente());
			if (clienteLoaded.getCliente() != null) {
				Hibernate.initialize(clienteLoaded.getCliente().getPessoa());
			}
		}
		return clienteLoaded;
	}
	
	public List<Contato> findContatosFromIdDestinatario(Long idCliente){
		return getClienteDAO().findContatosFromIdDestinatario(idCliente);
	}

	public boolean findClientePossuiLiberacaoRodoNoAereo(Long idDoctoServico){
		return getClienteDAO().findClientePossuiLiberacaoRodoNoAereo(idDoctoServico);
	}
	
	/**
	 * Método que valida se o nrIdentificacao passado como parametro pertence a um cliente GM
	 * @param nrIdentificacao
	 * @return
	 */
	public boolean validateClienteGM(String nrIdentificacao) {
		Cliente clienteRemetente = this.findByNumeroIdentificacao(nrIdentificacao);
		//Verifica se o cliente que está sendo processado é um cliente GM,
		//para tanto verificar se o ID do cliente informado para processamento consta no campo
		//PARAMETRO_GERAL.DS_CONTEÚDO para PARAMETRO_GERAL.NM_PARAMETRO = "IDS_GM"
		String dsIdsGM = (String) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_IDS_GM);

		Set<Long> idsGM = new HashSet<Long>();
		try {
			for (String id : dsIdsGM.split(";")) {
				idsGM.add(Long.valueOf(id));
			}
		} catch (Exception e) {
			throw new IllegalStateException("PARAMETRO IDS_GM NAO ESTA NO FORMATO ID;ID;ID;ID");
		}
		
		return idsGM.contains(clienteRemetente.getIdCliente());
	}
	
	private final ClienteDAO getClienteDAO() {
		return (ClienteDAO) getDao();
	}
	public void setClienteDAO(ClienteDAO dao) {
		setDao( dao );
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}
	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public NotaFiscalEletronicaService getNotaFiscalEletronicaService() {
		return notaFiscalEletronicaService;
	}

	public void setNotaFiscalEletronicaService(
			NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

    public List<Object[]> findCustomersByNrIdentificacao(String nrsIdentificacao) {
        List<Object[]> retornoFindLocation = getClienteDAO().findCustomersByNrIdentificacao(nrsIdentificacao);

        return retornoFindLocation;
    }
    
    public List<Map<String, Object>> findOrganization(Map<String, Object> params) {

        List<Object[]> retornoFindLocation = getClienteDAO().findOrganization(params);

        List<Map<String, Object>> listaRetorno = new ArrayList<Map<String, Object>>();
        if (retornoFindLocation != null && !retornoFindLocation.isEmpty()) {
            for (Object[] data : retornoFindLocation) {
                Map<String, Object> cliente = new HashMap<String, Object>();
                cliente.put(ClienteDAO.ID_CLIENTE, data[0]);
                cliente.put(ClienteDAO.NR_IDENTIFICACAO, data[1]);
                cliente.put(ClienteDAO.SEGMENTO_MERCADO, getSegmentoMercado((String) data[2]));
                cliente.put(ClienteDAO.COUNTRY, data[3]);
                cliente.put(ClienteDAO.ADDRESS1, data[4]);
                cliente.put(ClienteDAO.ADDRESS2, data[5]);
                cliente.put(ClienteDAO.CITY, data[6]);
                cliente.put(ClienteDAO.POSTAL_CODE, data[7]);
                cliente.put(ClienteDAO.STATE, data[8]);
                cliente.put(ClienteDAO.COUNTY, data[9]);
                cliente.put(ClienteDAO.COTACAO_ABERTA, data[10]);
                cliente.put(ClienteDAO.FATURAMENTO_MENSAL, data[11]);
                cliente.put(ClienteDAO.MEDIA_EMBARQUE_SEMANA, data[12]);
                cliente.put(ClienteDAO.QTDS_RNC_ABERTAS, data[13]);
                cliente.put(ClienteDAO.QTDS_RNC_EM_ABERTO, data[14]);
                cliente.put(ClienteDAO.TIPO_CLIENTE, data[15]);
                cliente.put(ClienteDAO.TIPO_PESSOA, data[16]);
                cliente.put(ClienteDAO.NOME_PESSOA, data[17]);
                cliente.put(ClienteDAO.NOME_FANTASIA, data[18]);

                listaRetorno.add(cliente);
            }
        }

        return listaRetorno;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> findClienteDivisao(Map criteria){
    	Map<String,Object> parameters = new HashMap<String, Object>();
		if(criteria.get("identificacao") != null){
			parameters.put("identificacao", ((Map)criteria.get("identificacao")).get("nrIdentificacao"));
		}
		
		parameters = getValue(criteria, "filial",      parameters);
		parameters = getValue(criteria, "reajuste",    parameters);
		parameters = getValue(criteria, "divisao",     parameters);
		parameters = getValue(criteria, "modal",       parameters);
		parameters = getValue(criteria, "tpCliente",   parameters);
		parameters = getValue(criteria, "estrategico", parameters);
		
    	return getClienteDAO().findClienteDivisao(parameters);
    }
    
    @SuppressWarnings("rawtypes")
	private Map<String,Object> getValue(Map<String,Object> params, String key, Map<String,Object> parameters){
		if(params.get(key) != null){
			parameters.put(key, ((Map)params.get(key)).get("id"));
		}
		
		return parameters;
	}
    
    private String getSegmentoMercado(String segmentoMercado) {
        return truncate(removeAccentsAndPontuaction(segmentoMercado), MAX_LENGTH_SEGMENTO_MERCADO);
    }

    private String truncate(String key, int maxLength) {
        String valor = key != null ? key : "";
        return valor.substring(0, Math.min(valor.length(), maxLength));
    }

    public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
    	this.historicoWorkflowService = historicoWorkflowService;
    }

    private String removeAccentsAndPontuaction(String text) {
        return text == null ? null : Normalizer.normalize(text, Form.NFD).replaceAll("[-/]", " ").replaceAll("[^a-zA-Z ]", "");
    }

	public List<Cliente> findClienteSuggest(String nrIdentificacao,
			String nmPessoa, Long idCliente, String tpPessoa, List<String> notINrIdentificacao) {
		return getClienteDAO().findClienteSuggest(nrIdentificacao, nmPessoa, idCliente, tpPessoa, notINrIdentificacao);
	}
	
	public boolean findClienteGerarParcelaFreteVlLiquidoXmlCte(Long idCliente){
		return getClienteDAO().findClienteGerarParcelaFreteVlLiquidoXmlCte(idCliente);
	}
	
	public List<Cliente> findAllClienteSuggest(String nrIdentificacao,
			String nmPessoa, Long idCliente, String tpPessoa, List<String> notINrIdentificacao) {
		return getClienteDAO()
				.findAllClienteSuggest(nrIdentificacao, nmPessoa, idCliente, tpPessoa, notINrIdentificacao);
	}

	
	public boolean validatePessoaByNrIdentificacao(String nrIdentificacao) {
		return pessoaService.validatePessoaByNrIdentificacao(nrIdentificacao);
	}

	public Boolean validateIsDanfeSimplificada(Long cnpjCliente) {
		return getClienteDAO().isNfeConjulgada(cnpjCliente);
	}

	public Boolean validateIsDanfeSimplificadaByIdColeta(String idColeta) {
		return getClienteDAO().isNfeConjulgadaByIdColeta(idColeta);
	}

	@Transactional
	public List<ClienteLayoutEDIDTO> getDiretorioCliente() {
		List<ClienteLayoutEDIDTO> clientes = new ArrayList<>();
		getClienteDAO().findDiretoriosClientes().forEach(cliente -> clientes.add(new ClienteLayoutEDIDTO(cliente[0].toString(), cliente[1].toString())));
		return clientes;
	}
 }
