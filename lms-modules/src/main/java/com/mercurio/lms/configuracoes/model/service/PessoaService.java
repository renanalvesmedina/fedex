package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;
import com.mercurio.lms.carregamento.model.PrestadorServico;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EmpresaCobranca;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.PessoaDAO;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.OperadoraMct;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.Pessoa99;
import com.mercurio.lms.expedicao.model.service.Pessoa99Service;
import com.mercurio.lms.integracao.model.service.MunicipioVinculoService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Concessionaria;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.municipios.model.PontoParada;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.sgr.model.Escolta;
import com.mercurio.lms.sgr.model.GerenciadoraRisco;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Despachante;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.pessoaService"
 */
public class PessoaService extends CrudService<Pessoa, Long> {
	private InscricaoEstadualService inscricaoEstadualService;
	private EnderecoPessoaService enderecoPessoaService;
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private RegionalFilialService regionalFilialService;
	private MunicipioVinculoService municipioVinculoService;
	private ParametroGeralService parametroGeralService;
	private Pessoa99Service pessoa99Service;
	private TipoTributacaoIEService tipoTributacaoIEService;

	/**
	 * Recupera uma instância de <code>Pessoa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Pessoa findById(java.lang.Long id) {
		return getPessoaDAO().findById(id);
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}

	public java.io.Serializable storeSimple(Pessoa bean) {
		return super.store(bean);
	}

	@Override
	protected Pessoa beforeInsert(Pessoa bean) {
		((Pessoa)bean).setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		return super.beforeInsert(bean);
	}

	@Override
	protected Pessoa beforeStore(Pessoa bean) {
		Pessoa pessoa = (Pessoa) bean;
		String nrIdentificacao = pessoa.getNrIdentificacao();
		if(StringUtils.isNotEmpty(nrIdentificacao)) {
			nrIdentificacao = PessoaUtils.validateIdentificacao(nrIdentificacao);
			pessoa.setNrIdentificacao(nrIdentificacao);
			//Bloqueio de cadastro/manutencao de clientes "9999"
			if(!SessionUtils.isIntegrationRunning()) {
				if("CNPJ".equals(pessoa.getTpIdentificacao().getValue())) {
					if(nrIdentificacao.startsWith("9999")) {
						throw new BusinessException("LMS-01130", new Object[]{"CNPJ"});
					}
				}
			}
		}
		// Caso o blAtualizacaoCoutasse seja null, seta para FALSE.
		if (pessoa.getBlAtualizacaoCountasse() == null) {
			pessoa.setBlAtualizacaoCountasse(Boolean.FALSE);
		}

		return super.beforeStore(bean);
	}

	public java.io.Serializable store(Pessoa bean) {
		return super.store(bean);
	}

	public java.io.Serializable store(Cliente cliente) {
		Pessoa pessoa = cliente.getPessoa();

		if (pessoa.getIdPessoa() != null) {
			Pessoa pessoaOriginal = this.findById(pessoa.getIdPessoa());

			//seta os dados que vem da tela
			pessoaOriginal.setNmPessoa(pessoa.getNmPessoa());
			pessoaOriginal.setNmFantasia(pessoa.getNmFantasia());
			pessoaOriginal.setTpIdentificacao(pessoa.getTpIdentificacao());
			pessoaOriginal.setNrIdentificacao(pessoa.getNrIdentificacao());
			pessoaOriginal.setTpPessoa(pessoa.getTpPessoa());
			pessoaOriginal.setDsEmail(pessoa.getDsEmail());
			pessoaOriginal.setNrRg(pessoa.getNrRg());
			pessoaOriginal.setDtEmissaoRg(pessoa.getDtEmissaoRg());
			pessoaOriginal.setDsOrgaoEmissorRg(pessoa.getDsOrgaoEmissorRg());
			pessoaOriginal.setNrInscricaoMunicipal(pessoa.getNrInscricaoMunicipal());
			pessoaOriginal.setUnidadeFederativaExpedicaoRg(pessoa.getUnidadeFederativaExpedicaoRg());
			//pessoaOriginal.setEnderecoPessoa(pessoa.getEnderecoPessoa());
			pessoa = pessoaOriginal;
			cliente.setPessoa(pessoa);
		}
		Filial filialComercial = filialService.findByIdInitLazyProperties(cliente.getFilialByIdFilialAtendeComercial().getIdFilial(),
				false);
		cliente.setFilialByIdFilialAtendeComercial(filialComercial);

		Filial filialOperacional = filialService.findByIdInitLazyProperties(cliente.getFilialByIdFilialAtendeOperacional().getIdFilial(),
				false);
		cliente.setFilialByIdFilialAtendeOperacional(filialOperacional);

		Filial filialCobranca = filialService.findByIdInitLazyProperties(cliente.getFilialByIdFilialCobranca().getIdFilial(), false);
		cliente.setFilialByIdFilialCobranca(filialCobranca);

		validateAlteracaoCliente(cliente);

		return this.store(pessoa);
	}

	public void validadeCliente(Cliente cliente){
		
		Filial filialComercial = filialService.findById(cliente.getFilialByIdFilialAtendeComercial().getIdFilial());
		cliente.setFilialByIdFilialAtendeComercial(filialComercial);

		Filial filialOperacional = filialService.findById(cliente.getFilialByIdFilialAtendeOperacional().getIdFilial());
		cliente.setFilialByIdFilialAtendeOperacional(filialOperacional);

		Filial filialCobranca = filialService.findById(cliente.getFilialByIdFilialCobranca().getIdFilial());
		cliente.setFilialByIdFilialCobranca(filialCobranca);
		
		validateAlteracaoDadosCliente(cliente);		
	}
	
	public java.io.Serializable store(Aeroporto aeroporto) {
		Pessoa pessoa = aeroporto.getPessoa();
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setDhInclusao(pessoaOriginal.getDhInclusao());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	public java.io.Serializable store(Beneficiario beneficiario) {
		Pessoa pessoa = beneficiario.getPessoa();
		validatePersonCommon(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Concessionaria concessionaria) {
		Pessoa pessoa = concessionaria.getPessoa();
		validatePersonCommon(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Empresa empresa) {
		Pessoa pessoa = empresa.getPessoa();
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setDhInclusao(pessoaOriginal.getDhInclusao());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	public java.io.Serializable store(EmpresaCobranca empresaCobranca) {
		Pessoa pessoa = empresaCobranca.getPessoa();

		if("F".equals(pessoa.getTpPessoa().getValue())) {
			throw new BusinessException("LMS-27004");
		}

		validatePersonCommon(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(FilialCiaAerea filialCiaAerea) {
		Pessoa pessoa = filialCiaAerea.getPessoa();
		validatePersonCommon(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Motorista motorista) {
		Pessoa pessoa = motorista.getPessoa();
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setTpPessoa(pessoaOriginal.getTpPessoa());
			pessoa.setDsEmail(pessoaOriginal.getDsEmail());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setDhInclusao(pessoaOriginal.getDhInclusao());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	public java.io.Serializable store(OperadoraMct operadoraMct) {
		Pessoa pessoa = operadoraMct.getPessoa();
		validatePersonCommon(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Proprietario proprietario) {
		Pessoa pessoa = proprietario.getPessoa();
		validatePersonCommon(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Despachante despachante) {
		Pessoa pessoa = despachante.getPessoa();
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setTpIdentificacao(pessoaOriginal.getTpIdentificacao());
			pessoa.setNrIdentificacao(pessoaOriginal.getNrIdentificacao());
			pessoa.setTpPessoa(pessoaOriginal.getTpPessoa());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	public java.io.Serializable store(Filial filial) {
		Pessoa pessoa = filial.getPessoa();
		Pessoa pessoaOriginal = null;

		String nrIdentificacao = PessoaUtils.clearIdentificacao(pessoa.getNrIdentificacao());
		if(!"LO".equals(filial.getLastHistoricoFilial().getTpFilial().getValue())
			&& StringUtils.isBlank(nrIdentificacao)
		) {
			throw new BusinessException("LMS-29158");
		}
		pessoa.setNrIdentificacao(nrIdentificacao);

		//Se a pessoa ja existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = this.findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	public java.io.Serializable store(OperadoraCartaoPedagio operadoraCartaoPedagio) {
		Pessoa pessoa = operadoraCartaoPedagio.getPessoa();
		validateNmPessoaDsEmail(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(PontoParada pontoParada) {
		Pessoa pessoa = pontoParada.getPessoa();
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			//pessoa.setNmPessoa(pessoaOriginal.getNmPessoa());
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setTpIdentificacao(pessoaOriginal.getTpIdentificacao());
			pessoa.setNrIdentificacao(pessoaOriginal.getNrIdentificacao());
			pessoa.setTpPessoa(pessoaOriginal.getTpPessoa());
			pessoa.setDsEmail(pessoaOriginal.getDsEmail());
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setDhInclusao(pessoaOriginal.getDhInclusao());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	public java.io.Serializable store(PrestadorServico prestadorServico) {
		Pessoa pessoa = prestadorServico.getPessoa();
		validateNmPessoaDsEmail(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(ReguladoraSeguro reguladoraSeguro) {
		Pessoa pessoa = reguladoraSeguro.getPessoa();
		validateNmPessoaDsEmail(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Seguradora seguradora) {
		Pessoa pessoa = seguradora.getPessoa();
		validateNmPessoaDsEmail(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Escolta escolta) {
		Pessoa pessoa = escolta.getPessoa();
		validateNmPessoaDsEmail(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(GerenciadoraRisco gerenciadoraRisco) {
		Pessoa pessoa = gerenciadoraRisco.getPessoa();
		validateNmPessoaDsEmail(pessoa);
		return this.store(pessoa);
	}

	public java.io.Serializable store(Terminal terminal) {
		Pessoa pessoa = terminal.getPessoa();
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setTpIdentificacao(pessoaOriginal.getTpIdentificacao());
			pessoa.setNrIdentificacao(pessoaOriginal.getNrIdentificacao());
			pessoa.setTpPessoa(pessoaOriginal.getTpPessoa());
			pessoa.setDsEmail(pessoaOriginal.getDsEmail());
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setDhInclusao(pessoaOriginal.getDhInclusao());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
		return this.store(pessoa);
	}

	private void validateNmPessoaDsEmail(Pessoa pessoa) {
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setTpIdentificacao(pessoaOriginal.getTpIdentificacao());
			pessoa.setNrIdentificacao(pessoaOriginal.getNrIdentificacao());
			pessoa.setTpPessoa(pessoaOriginal.getTpPessoa());
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
	}

	private void validatePersonCommon(Pessoa pessoa) {
		Pessoa pessoaOriginal = null;

		//Se a pessoa jah existe, fazer update dos campos que vem da tela.
		if(pessoa.getIdPessoa() != null) {
			//Pessoa que vem do banco
			pessoaOriginal = findById(pessoa.getIdPessoa());

			//copia os dados que nao vem da tela
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setNrRg(pessoaOriginal.getNrRg());
			pessoa.setDtEmissaoRg(pessoaOriginal.getDtEmissaoRg());
			pessoa.setDsOrgaoEmissorRg(pessoaOriginal.getDsOrgaoEmissorRg());
			pessoa.setNrInscricaoMunicipal(pessoaOriginal.getNrInscricaoMunicipal());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			pessoa.setUnidadeFederativaExpedicaoRg(pessoaOriginal.getUnidadeFederativaExpedicaoRg());
			verifyPersonModified(pessoa, pessoaOriginal);
		}
	}

	private void verifyPersonModified(Pessoa pessoaNova, Pessoa pessoaOriginal) {
		//Verifica se o usuario vem da integracao
		if(!SessionUtils.isIntegrationRunning()) {
			//Se a pessoa foi modificada
			boolean isEqual = true;
			if(pessoaOriginal != null) {
				isEqual = new EqualsBuilder()
					.append(pessoaNova.getNmPessoa(), pessoaOriginal.getNmPessoa())
					.append(pessoaNova.getNmFantasia(), pessoaOriginal.getNmFantasia())
					.append(pessoaNova.getTpIdentificacao(), pessoaOriginal.getTpIdentificacao())
					.append(pessoaNova.getNrIdentificacao(), pessoaOriginal.getNrIdentificacao())
					.append(pessoaNova.getTpPessoa(), pessoaOriginal.getTpPessoa())
					.append(pessoaNova.getDsEmail(), pessoaOriginal.getDsEmail())
					.append(pessoaNova.getNrRg(), pessoaOriginal.getNrRg())
					.append(pessoaNova.getDtEmissaoRg(), pessoaOriginal.getDtEmissaoRg())
					.append(pessoaNova.getDsOrgaoEmissorRg(), pessoaOriginal.getDsOrgaoEmissorRg())
					.append(pessoaNova.getNrInscricaoMunicipal(), pessoaOriginal.getNrInscricaoMunicipal())
					.append(pessoaNova.getEnderecoPessoa(), pessoaOriginal.getEnderecoPessoa())
					.isEquals();
			}
			if(!isEqual) {
				validatePersonModified(pessoaOriginal.getIdPessoa());
			}
		}
		getPessoaDAO().getHibernateTemplate().evict(pessoaOriginal);
	}

	/**
	 * Valida se a pessoa pode ser alterado pelo usuario logado
	 * 
	 * @param idPessoa
	 */
	//Método público para ser utilizado pelas classes que "especializam" pessoa
	public void validateAlteracaoPessoa(Long idPessoa) {
		//Verifica se o usuario vem da integracao
		if(SessionUtils.isIntegrationRunning()) {
			return;
		}
		validatePersonModified(idPessoa);
	}

	private void validatePersonModified(Long idPessoa) {
		//Se for filial permite alteracao
		if(filialService.isFilial(idPessoa)) {
			return;
		}

		Map<String, Object> mapCliente = clienteService.findTpClienteIdFilialComercial(idPessoa);
		//Se a pessoa nao for um cliente
		if(mapCliente == null) {
			return;
		}
		Cliente cliente = new Cliente();
		cliente.setTpCliente((DomainValue)mapCliente.get("tpCliente"));

		Filial filialComercial = new Filial();
		filialComercial.setIdFilial((Long)mapCliente.get("idFilialComercial"));
		filialComercial.setDtImplantacaoLMS((YearMonthDay)mapCliente.get("dtImplantacaoFilialComercial"));
		cliente.setFilialByIdFilialAtendeComercial(filialComercial);

		Filial filialOperacional = new Filial();
		filialOperacional.setIdFilial((Long)mapCliente.get("idFilialOperacional"));
		filialOperacional.setDtImplantacaoLMS((YearMonthDay)mapCliente.get("dtImplantacaoFilialOperacional"));
		cliente.setFilialByIdFilialAtendeOperacional(filialOperacional);

		Filial filialCobranca = new Filial();
		filialCobranca.setIdFilial((Long)mapCliente.get("idFilialCobranca"));
		filialCobranca.setDtImplantacaoLMS((YearMonthDay)mapCliente.get("dtImplantacaoFilialCobranca"));
		cliente.setFilialByIdFilialCobranca(filialCobranca);

		Regional regionalComercial = new Regional();
		regionalComercial.setIdRegional((Long)mapCliente.get("idRegionalComercial"));
		cliente.setRegionalComercial(regionalComercial);

		Regional regionalOperacional = new Regional();
		regionalOperacional.setIdRegional((Long)mapCliente.get("idRegionalOperacional"));
		cliente.setRegionalOperacional(regionalOperacional);

		Regional regionalFinanceira = new Regional();
		regionalFinanceira.setIdRegional((Long)mapCliente.get("idRegionalFinanceira"));
		cliente.setRegionalFinanceiro(regionalFinanceira);

		validateAlteracaoCliente(cliente);
	}

	/**
	 * Valida se cliente pode ser alterado pelo usuario logado
	 * @param tpCliente
	 * @param idFilialComercial
	 * 
	 * @exception LMS-01156
	 * @exception LMS-01159
	 */
	private void validateAlteracaoCliente(Cliente cliente) {
		//Se vem da integracao nao valida alteracao pessoa
		if(SessionUtils.isIntegrationRunning()) {
			return;
		}
		//Se o cliente é 'S' (Especial) ou 'F' (Filial de cliente especial)
		if (ClienteUtils.isParametroClienteEspecial(cliente.getTpCliente().getValue())) {
			validateAlteracaoDadosCliente(cliente);
		}
	}
	
	/**
	 * Valida alteração de dados do cliente
	 * @param cliente
	 */
	private void validateAlteracaoDadosCliente(Cliente cliente){
	
			Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
			Long idFilialComercial = cliente.getFilialByIdFilialAtendeComercial().getIdFilial();
			Long idFilialOperacional = cliente.getFilialByIdFilialAtendeOperacional().getIdFilial();
			Long idFilialCobranca = cliente.getFilialByIdFilialCobranca().getIdFilial();
			YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

			//Verifica se eh usuario da matriz
			if(SessionUtils.isFilialSessaoMatriz()) {
				YearMonthDay dtImplantacaoFilialComercial = (YearMonthDay) configuracoesFacade.getValorParametro(idFilialComercial, "IMP_INT_V1");
				YearMonthDay dtImplantacaoFilialOperacional = (YearMonthDay) configuracoesFacade.getValorParametro(idFilialOperacional, "IMP_INT_V1");
				YearMonthDay dtImplantacaoFilialCobranca = (YearMonthDay) configuracoesFacade.getValorParametro(idFilialCobranca, "IMP_INT_V1");
				// Se filiais estao implantadas no LMS
				if( ( (dtImplantacaoFilialComercial == null)
						|| CompareUtils.gt(dtImplantacaoFilialComercial, dtToday) )
					|| ( (dtImplantacaoFilialOperacional == null)
						|| CompareUtils.gt(dtImplantacaoFilialOperacional, dtToday) )
					|| ( (dtImplantacaoFilialCobranca == null)
						|| CompareUtils.gt(dtImplantacaoFilialCobranca, dtToday) )
				) {
					throw new BusinessException("LMS-01159");
				}
			} else {
				//Se filiais sao diferentes da filial da sessao
				if (CompareUtils.ne(idFilialComercial, idFilialSessao)) {
					throw new BusinessException("LMS-01156");
				}
				YearMonthDay dtImplantacaoFilialSessao = (YearMonthDay) configuracoesFacade.getValorParametro(idFilialSessao, "IMP_INT_V1");
				if( (dtImplantacaoFilialSessao == null)
						|| CompareUtils.gt(dtImplantacaoFilialSessao, dtToday)
				) {
					throw new BusinessException("LMS-01159");
				}

				//Se regionais sao diferentes da regional da sessao
				Long idRegionalSessao = regionalFilialService.findLastRegionalVigente(idFilialSessao).getIdRegional();
				Long idRegionalComercial = cliente.getRegionalComercial().getIdRegional();
				if (CompareUtils.ne(idRegionalComercial, idRegionalSessao)) {
					throw new BusinessException("LMS-01156");
				}
			}
		}


	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setPessoaDAO(PessoaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PessoaDAO getPessoaDAO() {
		return (PessoaDAO) getDao();
	}

	/**
	 * Retorna a pessoa com apenas sua ÚLTIMA (id) inscrição estadual ativa.
	 *
	 * @param criteria.
	 * @return Pessoa.
	 */
	public Pessoa setLastIeForPessoa(Pessoa pessoa) {
		if (pessoa == null)
			return null;
		InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.getLastInscricaoEstadualByPessoa(pessoa.getIdPessoa());
		if (inscricaoEstadual != null)
			pessoa.setNrInscricaoEstadual(inscricaoEstadual.getNrInscricaoEstadual());
		return pessoa;
	}

	/**
	 * Busca pessoas com endereço padrão fora de vigência
	 * Rotina usada pela atualização automatica de endereço padrão
	 * @return List de Pessoa
	 */
	public List<Pessoa> findPessoasComEnderecoForaVigencia() {
		return getPessoaDAO().findPessoasComEnderecoForaVigencia();
	}

	/**
	 * Se tem um ou mais Endereço e Tipo Endereço por essa pessoa retorna true, senão false.
	 * 
	 * @return boolean
	 */
	public boolean verificaExistenciaEnderecoTipoEndereco(Long idPessoa){
		if (this.getPessoaDAO().verificaExistenciaEnderecoTipoEndereco(idPessoa).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Se a pessoa recebida tem mais de um endereço do tipo fornecido retorna true, senão false
	 * 
	 * @return boolean 
	 */
	public boolean verificaExistenciaEnderecoTipoEndereco(Long idPessoa, String tpEndereco) {
		if (this.getPessoaDAO().verificaExistenciaEnderecoTipoEndereco(idPessoa, tpEndereco).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Se tem um ou mais Endereço e Tipo Endereço por essa pessoa retorna true, senão false.
	 * 
	 * @return boolean
	 */
	public boolean verificaExistenciaEndereco(Long idPessoa){
		if (this.getPessoaDAO().verificaExistenciaEnderecoTipoEndereco(idPessoa).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Se tem um ou mais Inscrição Estadual e tipo endereço por essa pessoa retorna true, senão false.
	 * 
	 * @return boolean
	 */
	public boolean verifyInscricaoEstadualPadraoAtiva(Long idPessoa){
		if (this.getPessoaDAO().verifyInscricaoEstadualPadraoAtiva(idPessoa).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	} 

	/**
	 * Se tem um ou mais Telefone Endereço e tipo endereço por essa pessoa retorna true, senão false.
	 * 
	 * @return boolean
	 */
	public boolean verificaExistenciaTelefoneEndereco(Long idPessoa){
		if (this.getPessoaDAO().verificaExistenciaTelefoneEndereco(idPessoa).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Se tem um ou mais Contato e tipo endereço por essa pessoa retorna true, senão false.
	 * 
	 * @return boolean
	 */
	public boolean verificaExistenciaContato(Long idPessoa){
		if (this.getPessoaDAO().verificaExistenciaContato(idPessoa).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean verificaExistenciaContatoFaturamento(Long idPessoa){
		if (this.getPessoaDAO().verificaExistenciaContatoFaturamento(idPessoa).intValue() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Verifica se o número de identificação inicia com a String informada.<BR>
	 *@author Robson Edemar Gehl
	 * @param id
	 * @param startsWith
	 * @return
	 */
	public boolean validateNrIdentificacaoStartsWith(Long id, String startsWith){
		String nrIdentificacao = getPessoaDAO().findNrIdentificacao(id);
		if(nrIdentificacao != null) {
			return (nrIdentificacao.startsWith(startsWith));
		}
		return false;
	}

	/**
	 * Verifica se a pessoa é do tipo informado.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPessoa
	 * @param tpPessoa
	 * @return true se o tipo da pessoa informada for igual ao tipo informado.
	 */
	public boolean validateTipoPessoa(Long idPessoa, String tpPessoa){
		return getPessoaDAO().validateTipoPessoa(idPessoa, tpPessoa);
	}

	public Boolean validatePessoaByNrIdentificacao(String nrIdentificacao) {
		return getPessoaDAO().validatePessoaByNrIdentificacao(nrIdentificacao);
	}
	
	/**
	 * Verifica existência de Pessoa.
	 * É lançada uma Exception se a especialização da pessoa já estiver cadastrado.
	 * @param Class clazz que representa a especialização
	 * @param Map map
	 * @param String strExceptionCode
	 * @return Pessoa
	 * @deprecated
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getPessoa(Long, Class, boolean) 
	 * 
	 */
	public Pessoa findValidPessoa(Class clazz, Map map, String strExceptionCode){
		Integer numReg = this.getPessoaDAO().findValidPessoa(clazz, map);
		if (numReg.equals(Integer.valueOf(0))){
			List<Pessoa> list = this.find(map);
			if (list.size() > 0){
				return (Pessoa)list.get(0);
			}
		} else {
			throw new BusinessException(strExceptionCode);
		}
		return null;
	}
	
	/**
	 * 
	 * @param tpIdentificacao
	 * @param nrIdentificacao
	 * @param tpPessoa
	 * @return
	 * @deprecated 
	 * @see com.mercurio.lms.configuracoes.ConfiguracoesFacade#getPessoa(String, String)
	 */
	public Map findByIdentificacao(String tpIdentificacao, String nrIdentificacao, String tpPessoa) {
		Pessoa pessoa = findByIdentificacao(tpIdentificacao, nrIdentificacao);
		if (pessoa != null) {
			Map result = new HashMap();
			result.put("idPessoa",pessoa.getIdPessoa());
			result.put("nmPessoa",pessoa.getNmPessoa());
			result.put("tpIdentificacao",pessoa.getTpIdentificacao().getValue());
			result.put("dsEmail",pessoa.getDsEmail());
			return result;
		}else {
			return null;
		}
	}

	/**
	 * Método que busca uma List de Pessoa de acordo com o ID do TelefoneEndereco.
	 * 
	 * @param idTelefoneEndereco
	 * @return
	 */
	public List<Pessoa> findPessoaByTelefoneEndereco(Long idTelefoneEndereco) {
		return this.getPessoaDAO().findClientesByTelefoneEndereco(idTelefoneEndereco);
	}	

	/**
	 * Metodo que deve ser renomeado para 'findById' depois que for construida
	 * uma classe Action para tratar o cadastro de Pessoa. (falar com Felipe).
	 * 
	 * @param id
	 * @param classEspecializacao
	 * @return
	 */
	public Object findByIdEspecializacao(Long id, Class classEspecializacao, boolean fetchPessoa) {
		if (id == null) {
			throw new IllegalArgumentException("id não pode ser null");
		}
		if (classEspecializacao == null) {
			throw new IllegalArgumentException("classEspecializacao não pode ser null");
		}
		return getPessoaDAO().findById(id, classEspecializacao, fetchPessoa);		
	}

	public Pessoa findByIdentificacao(String tpIdentificacao, String nrIdentificacao) {
		return (Pessoa)findByIdentificacao(tpIdentificacao, nrIdentificacao, Pessoa.class, false);
	}

	public Object findByIdentificacao(String tpIdentificacao, String nrIdentificacao, Class classEspecializacao, boolean fetchPessoa) {
		if (tpIdentificacao == null) {
			throw new IllegalArgumentException("tpIdentificacao não pode ser null");
		}
		PessoaUtils.validateIdentificacao(nrIdentificacao);
		if (classEspecializacao == null) {
			throw new IllegalArgumentException("classEspecializacao não pode ser null");
		}
		return getPessoaDAO().findByIdentificacao(tpIdentificacao, nrIdentificacao, classEspecializacao, fetchPessoa);
	}

	/**
	 * Busca o endereço comercial ou residencial vigente, de acordo com o tipo da pessoa, e vincula a pessoa
	 * @param pessoa
	 */
	public void storeEnderecoPessoaAtual(Pessoa pessoa, EnderecoPessoa enderecoPessoaAtual){
		//Busca o endereco vigente
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(pessoa.getIdPessoa(), JTDateTimeUtils.getDataAtual());

		//Se o endereço já foi informado, e é diferente do endereço encontrado
		if (pessoa.getEnderecoPessoa() != null && !pessoa.getEnderecoPessoa().equals(enderecoPessoa)){
			storeEnderecoPessoaPadrao(pessoa, enderecoPessoa);
		}
		
		//Se não possui endereço padrão e a vigência inicial é superior a data atual exibie mensagem de erro
		if (enderecoPessoa == null && enderecoPessoaAtual.getDtVigenciaInicial().isAfter(JTDateTimeUtils.getDataAtual())){
			throw new BusinessException("LMS-27098");
		}
		
		//Se o endereço ainda não foi informado
		if (pessoa.getEnderecoPessoa() == null){
			storeEnderecoPessoaPadrao(pessoa, enderecoPessoa);
		}
	}
	
	/**
	 * Vincula a pessoa ao endereço
	 * @param pessoa
	 * @param enderecoPessoa
	 */
	public void storeEnderecoPessoaPadrao(Pessoa pessoa, EnderecoPessoa enderecoPessoa){
		pessoa.setEnderecoPessoa(enderecoPessoa);
		store(pessoa);
	}

	/**
	 * Método responsável por buscar nrCNPJParcialDev igual as primeiras 8 posições do nrIdentificacao da Pessoa 
	 * 
	 * @author HectorJ
	 * @since 31/05/2006
	 * 
	 * @param nrCNPJParcial
	 * @return List <Pessoa>
	 */
	public List findNrCNPJParcialEqualNrIdentificacaoPessoa(String nrCNPJParcial){
		List lst = getPessoaDAO().findNrCNPJParcialEqualNrIdentificacaoPessoa(StringUtils.leftPad(nrCNPJParcial,8,'0'));
		if(lst == null || lst.isEmpty()){
			throw new BusinessException("LMS-23017");
		}
		return lst;
	}
	
	public String findDsEmailByIdPessoa(Long idPessoa){
		return getPessoaDAO().findDsEmailByIdPessoa(idPessoa);
	}
	
	/**
	 * Carrega Pessoa de acordo com nrCnpj.
	 * 
	 * @author HectorJ
	 * @since 31/05/2006
	 * 
	 * @param nrCNPJParcial
	 * @return List <Pessoa>
	 */
	public List findPessoasByNrIdentificacao(String nrCnpj){
		return getPessoaDAO().findNrCNPJParcialEqualNrIdentificacaoPessoa(nrCnpj);
	}
	
	
	/**
	 * Busca pessoa pelo CNPJ parcial
	 * 
	 * @author Vagner Huzalo
	 * @since 06/12/2007
	 * 
	 * @param nrCNPJParcial
	 * @return <code>Pessoa</code>
	 */
	public Pessoa findByCNPJParcial(String nrCNPJParcial){
		return getPessoaDAO().findByCNPJParcial(nrCNPJParcial);
	}

	/**
	 * Retorna a lista de id de pessoa baseado no CNPJ parcial informado 
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/10/2006
	 * 
	 * @param String nrCNPJParcial
	 * @return List
	 */
	public List findIdPessoaByNrCNPJParcial(String nrCNPJParcial){
		return getPessoaDAO().findIdPessoaByNrCNPJParcial(nrCNPJParcial);
	}	

	/**
	 * Método que valida se o número de identificação informado é da Mercúrio.
	 * 
	 * @param String nrIdentificacao
	 * @return Boolean
	 */
	public Boolean validatePessoaFromMercurio(String nrIdentificacao){
		if(nrIdentificacao.length() >= 8 && nrIdentificacao.substring(0,8).equals(((BigDecimal)configuracoesFacade.getValorParametro("NR_CNPJ_MERCURIO")).toString())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Busca uma Pessoa pelo seu número
	 *
	 * @author José Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param nrIdentificacao Número de identificação da pessoa
	 * @return Pessoa
	 */
	public Pessoa findByNrIdentificacao(String nrIdentificacao) {
		return getPessoaDAO().findByNrIdentificacao(nrIdentificacao);
	}

	/**
	 * Retorna o id da pessoa do tipo de endereco informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idTipoEnderecoPessoa
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdTipoEnderecoPessoa(Long idTipoEnderecoPessoa){
		return getPessoaDAO().findIdPessoaByIdTipoEnderecoPessoa(idTipoEnderecoPessoa);
	}

	/**
	 * Retorna o id da pessoa da inscrição estadual informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idInscricaoEstadual
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdInscricaoEstadual(Long idInscricaoEstadual){
		return getPessoaDAO().findIdPessoaByIdInscricaoEstadual(idInscricaoEstadual);
	}

	/**
	 * Retorna o id da pessoa do contato de correspondencia informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idContatoCorrespondencia
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdContatoCorrespondencia(Long idContatoCorrespondencia){
		return getPessoaDAO().findIdPessoaByIdContatoCorrespondencia(idContatoCorrespondencia);
	}

	/**
	 * Retorna o id da pessoa do contato informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idContato
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdContato(Long idContato){
		return getPessoaDAO().findIdPessoaByIdContato(idContato);
	}

	/**
	 * Retorna o id da pessoa da observaçao icms pessoa informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idObservacaoICMSPessoa
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdObservacaoICMSPessoa(Long idObservacaoICMSPessoa){
		return getPessoaDAO().findIdPessoaByIdObservacaoICMSPessoa(idObservacaoICMSPessoa);
	}
	
	/**
	 * Se os 8 primeiros dígitos são da Cometo ou Araçatuba mas que os CNPJ
	 * são diferente, lança ume exception
	 * 
	 * @author Mickaël Jalbert
	 * @since 12/03/2007
	 * @param nrIdentificacao
	 */
	public void isCometaOrAracatuba(String nrIdentificacao){
		String cnpjCometa = ((BigDecimal)configuracoesFacade.getValorParametro("NR_CNPJ_MATRIZ_COMETA")).toString();
		String cnpjAracatuba = ((BigDecimal)configuracoesFacade.getValorParametro("NR_CNPJ_MATRIZ_ARACATUBA")).toString();
		
		//Se os 8 primeiros dígitos são da Cometa ou Araçatuba
		if (nrIdentificacao.substring(0,8).equals(cnpjCometa.substring(0,8)) ||
			nrIdentificacao.substring(0,8).equals(cnpjAracatuba.substring(0,8))){
			if (nrIdentificacao.compareTo(cnpjCometa) != 0 && nrIdentificacao.compareTo(cnpjAracatuba) != 0){
				throw new BusinessException("LMS-36233", new Object[]{"", FormatUtils.formatCNPJ(cnpjCometa), FormatUtils.formatCNPJ(cnpjAracatuba)});
			}
		}
	}

	@Override
	public List findLookup(Map criteria) {
		String nrIdentificacao = MapUtils.getString(criteria, "nrIdentificacao");
		if (!StringUtils.isBlank(nrIdentificacao)) {
			criteria.put("nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}
		return super.findLookup(criteria);
	}

	public Map findPessoaSOMById(Long idPessoa) {
		Map data = getPessoaDAO().findPessoaSOMById(idPessoa);
		if(data != null && data.size() > 0) {
			
			InscricaoEstadual ie = inscricaoEstadualService.findByPessoaIndicadorPadrao(idPessoa, true);
						
			DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
			if(ie != null) {
				data.put("nrInscricaoEstadual", ie.getNrInscricaoEstadual());
				TipoTributacaoIE tipoTributacaoIE = tipoTributacaoIEService.findTiposTributacaoIEVigente(ie.getIdInscricaoEstadual(), JTDateTimeUtils.getDataAtual());
			if(tipoTributacaoIE == null || tipoTributacaoIE.getTpSituacaoTributaria() == null) {
					if(ie.getNrInscricaoEstadual() != null && "ISENTO".equals(ie.getNrInscricaoEstadual())) {
						data.put("tpSituacaoTributaria", new DomainValue("NC"));
				} else {
						data.put("tpSituacaoTributaria", new DomainValue("CO"));
				}
			} else {
				data.put("tpSituacaoTributaria", tipoTributacaoIE.getTpSituacaoTributaria());
			}
			} else {
				data.put("nrInscricaoEstadual", "ISENTO");
				data.put("tpSituacaoTributaria", new DomainValue("NC"));
				
			}
			String sgRedFlAtendeComercial = filialService.findSgFilialLegadoByFilialTntOuCooperada((Long) data.get("idFlAtendeComercial"));
			String sgRegFlCobranca = filialService.findSgFilialLegadoByFilialTntOuCooperada((Long) data.get("idFlCobranca"));
			data.put("sgRedFlAtendeComercial", sgRedFlAtendeComercial);
			data.put("sgRegFlCobranca", sgRegFlCobranca);
			data.put("dtCadastro", sysDate);
			data.put("cepMunicipio", municipioVinculoService.findCepMunicipioSOMByIdMunicipio((Long) data.get("idMunicipio")));
			DomainValue tpPessoa = (DomainValue) data.get("tpPessoa");
			if(tpPessoa != null) {
				data.put("tpPessoa", tpPessoa.getValue());
			}
			DomainValue tpIdentificacao = (DomainValue) data.get("tpIdentificacao");
			if(data.get("nrIdentificacao") == null || "".equals(data.get("nrIdentificacao"))
					|| (tpIdentificacao != null && tpIdentificacao.getValue() != null && !"CPF".equals(tpIdentificacao.getValue()) && !"CNPJ".equals(tpIdentificacao.getValue()))) {
				Pessoa pessoa = getPessoaDAO().findById(idPessoa);
				
				Pessoa99 pessoa99 = pessoa99Service.findByPessoa(idPessoa);
				if(pessoa99 == null || pessoa99.getPessoa() == null || pessoa99.getPessoa().getIdPessoa() == null) {
					pessoa99 = new Pessoa99();
					pessoa99.setPessoa(pessoa);
					pessoa99.setNrIdentificacao99(gerarNrIdentificacao99());
					pessoa99Service.store(pessoa99);
				}
				data.put("nrIdentificacao", pessoa99.getNrIdentificacao99());
				data.put("tpPessoa", "J");
			}
		}
		return data;
	}
	
	public Pessoa findPessoaByIdMonitoramentoDocEletronic(Long idMonitoramentoDocEletronic, String tpContato){
		return getPessoaDAO().findPessoaByIdMonitoramentoDocEletronic(idMonitoramentoDocEletronic);
	}
	
	public List findPessoaByCnpjCpf(List<String> listaCnpjCpf){
		return getPessoaDAO().findPessoaByCnpjCpf(listaCnpjCpf);
	}
	
	@SuppressWarnings("rawtypes")
	public String findNomePessoaByCompetenciaIdFranquia(YearMonthDay dtCompetencia, Long idFranquia){
		if(idFranquia == null) return null;

    	List nomePessoaList = getPessoaDAO().findNomePessoaByCompetenciaIdFranquia(dtCompetencia, idFranquia);
    	if(nomePessoaList.isEmpty()) return null;
    	
    	return (String) nomePessoaList.get(0);
	}
	
	public String gerarNrIdentificacao99() {
		String sequencia = parametroGeralService.generateValorParametroSequencial("SEQUENCIA_99", false, 1).toString();
		sequencia = FormatUtils.fillNumberWithZero(sequencia, 6);
		
		String cnpj = "999" + sequencia.substring(0, 2) + "999" + sequencia.substring(2);
		int[] foundDv = {0,0};
		for (int j = 0; j < 2; j++) {
        	int sum = 0;
            int coeficient = 2;
            for (int i = 11 + j; i >= 0 ; i--){
            	int digit = Integer.parseInt(String.valueOf(cnpj.charAt(i)));               
                sum += digit * coeficient;
                coeficient ++;
                if (coeficient > 9) coeficient = 2;                
            }
            foundDv[j] = 11 - sum % 11;
            if (foundDv[j] >= 10) foundDv[j] = 0;
            cnpj += foundDv[j];
        }
		return cnpj;
	}

	/**
	 * Executa store da pessoa através de um proprietário.
	 * 
	 * @param proprietario
	 * 
	 * @return java.io.Serializable
	 */
	public java.io.Serializable storeByProprietario(Proprietario proprietario) {
		Pessoa pessoa = proprietario.getPessoa();
		
		Pessoa pessoaOriginal = null;

		if(pessoa.getIdPessoa() != null) {
			pessoaOriginal = findById(pessoa.getIdPessoa());

			/* Efetua cópia dos dados que já existem */
			pessoa.setNmFantasia(pessoaOriginal.getNmFantasia());
			pessoa.setEnderecoPessoa(pessoaOriginal.getEnderecoPessoa());
			
			verifyPersonModified(pessoa, pessoaOriginal);
		}	
		
		return this.store(pessoa);
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}

	/**
	 * @return the municipioVinculoService
	 */
	public MunicipioVinculoService getMunicipioVinculoService() {
		return municipioVinculoService;
	}

	/**
	 * @param municipioVinculoService the municipioVinculoService to set
	 */
	public void setMunicipioVinculoService(
			MunicipioVinculoService municipioVinculoService) {
		this.municipioVinculoService = municipioVinculoService;
	}

	/**
	 * @return the parametroGeralService
	 */
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	/**
	 * @param parametroGeralService the parametroGeralService to set
	 */
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	/**
	 * @return the pessoa99Service
	 */
	public Pessoa99Service getPessoa99Service() {
		return pessoa99Service;
	}

	/**
	 * @param pessoa99Service the pessoa99Service to set
	 */
	public void setPessoa99Service(Pessoa99Service pessoa99Service) {
		this.pessoa99Service = pessoa99Service;
	}

	public TipoTributacaoIEService getTipoTributacaoIEService() {
		return tipoTributacaoIEService;
	}

	public void setTipoTributacaoIEService(
			TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}

}