package com.mercurio.lms.contratacaoveiculos.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataIntegrityViolationException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.AnexoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.FotoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.dao.MotoristaDAO;
import com.mercurio.lms.contratacaoveiculos.model.util.MotoristaConstants;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.motoristaService"
 */
public class MotoristaService extends CrudService<Motorista, Long> {
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoService;	
	private ContatoService contatoService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private TelefoneContatoService telefoneContaoService;
	private UsuarioService usuarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private MunicipioService municipioService;
	private ExecuteWorkflowMotoristaService executeWorkflowMotoristaService;
	
	/**
	 * Recupera uma instância de <code>Motorista</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Motorista findById(java.lang.Long id) {
		return (Motorista) super.findById(id);
	}

	public Map<String, Object> findByIdDetalhamento(java.lang.Long id) {
		Motorista motorista = (Motorista) super.findById(id);			
		TypedFlatMap mapMotorista = bean2map(motorista);

		if (!motorista.getTpVinculo().getValue().equals(MotoristaConstants.TIPO_VINCULO_FUNCIONARIO.getValue()))
			mapMotorista = getMotoristaDAO().findReferencias(mapMotorista);

		consultaLocalizacaoMotorista(mapMotorista.getLong("idMotorista"), Boolean.FALSE, mapMotorista);		

		String blVencida = "N";
		if (motorista.getDtVencimentoHabilitacao() != null)
		if (motorista.getDtVencimentoHabilitacao().compareTo(JTDateTimeUtils.getDataAtual())< 0) blVencida="S";

		mapMotorista.put("blVencida", blVencida);

		return mapMotorista;
	}

	public java.io.Serializable store(Motorista bean) {
		return super.store(bean);
	}

	//traz as referencias dos agregados,eventuais caso tenham voltado a ser agregados ou eventuais
	public Map<String, Object> findReferencias(Long idMotorista){
		TypedFlatMap motorista = new TypedFlatMap();
		motorista.put("idMotorista", idMotorista);
		return getMotoristaDAO().findReferencias(motorista);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getMotoristaDAO().getRowCount(criteria);
		return rowCountCustom;
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		ResultSetPage rsp = this.getMotoristaDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		rsp.setList(bean2Map(rsp.getList()));
		return rsp;
	}

	private List<TypedFlatMap> bean2Map(List<Map<String, Object>> result) {
		List<TypedFlatMap> newResult = new ArrayList<TypedFlatMap>();
		for(Map<String, Object> linha : result) { 
			TypedFlatMap map = new TypedFlatMap();
			map.put("idMotorista", linha.get("idMotorista"));
			map.put("filial.sgFilial", linha.get("filial_sgFilial"));
			map.put("filial.pessoa.nmFantasia", linha.get("filial_pessoa_nmFantasia"));
			map.put("pessoa.tpIdentificacao", linha.get("pessoa_tpIdentificacao"));
			
			String tpIdentificacao =  ((DomainValue)linha.get("pessoa_tpIdentificacao")) == null ? "" : ((DomainValue)linha.get("pessoa_tpIdentificacao")).getValue();
			map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(tpIdentificacao,
																						((String)linha.get("pessoa_nrIdentificacao"))));
			map.put("pessoa.nmPessoa" , linha.get("pessoa_nmPessoa"));
			map.put("usuarioMotorista.nrMatricula", linha.get("usuarioMotorista_nrMatricula"));
			map.put("tpVinculo", linha.get("tpVinculo"));
			map.put("tpSituacao", linha.get("tpSituacao"));

			newResult.add(map); 
		}
		return newResult;
	}

	public Pessoa validateIdentificacao(Map<String, Object> map) {
		Pessoa pessoa = configuracoesFacade.getPessoa((String)(map.get("nrIdentificacao")), (String) map.get("tpIdentificacao"));
		if (pessoa != null) {
			Object pessoaEspecializada = configuracoesFacade.getPessoa(pessoa.getIdPessoa(), Motorista.class, false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-26016");
			}
		}
		return pessoa;
	}

	/**
	 * 
	 * @param idMotorista
	 */
	public void validateCNHMotorista(Long idMotorista) {
		Motorista motorista = this.findById(idMotorista);
		if (motorista.getDtVencimentoHabilitacao() == null || motorista.getDtVencimentoHabilitacao().isBefore(JTDateTimeUtils.getDataAtual()))
			throw new BusinessException("LMS-05369");
                if( "I".equalsIgnoreCase(motorista.getTpSituacao().getValue())){
                    throw new BusinessException("LMS-00051", new String[]{"Motorista"});
                }
	}

	/**
	 * Busca os dados do usuario logado na sessao
	 * @return Map com os dados de funcionario e filial do usuario logado
	 */
	public Map<String, Object> findDadosUsuarioLogado(){
		Usuario usuario = SessionUtils.getUsuarioLogado();
		Filial filial = SessionUtils.getFilialSessao();
		String codigoCargoMotorista = (String)configuracoesFacade.getValorParametro("CD_CARGO_MOTORISTA");
		
		HashMap<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("idUsuario", usuario.getIdUsuario());
		retorno.put("nrMatricula", usuario.getNrMatricula());
		retorno.put("nmUsuario", usuario.getNmUsuario());
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("nmFantasia", filial.getPessoa().getNmFantasia());
		retorno.put("cargo", codigoCargoMotorista);

		return retorno;
	}
	
	public Map<String, Object> findFilialUsuarioLogado(){
		HashMap<String, Object> retorno = new HashMap<String, Object>();		
		Filial filial = SessionUtils.getFilialSessao();
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("nmFantasia", filial.getPessoa().getNmFantasia());
		return retorno;
	}

	public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
		return usuarioService.findLookupUsuario(tfm);
	}
	
	public List	findLookupMotoristaInstrutor(TypedFlatMap tfm){
		return usuarioService.findLookupMotoristaInstrutor(tfm);
	}
	
	public List<Contato> findReferenciasByIdMotorista(Long idMotorista) {
		return getMotoristaDAO().findReferenciasByIdMotorista(idMotorista);
	}
	
	public List findReferenciasByIdMeioTransporte(Long idMeioTransporte) {
		return getMotoristaDAO().findReferenciasByIdMeioTransporte(idMeioTransporte);
	}
	
	/**
	 * Exclui o motorista (chamar removePessoaById após o commit deste método, a exemplo de ManterMotoristaAction.removeById)
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		//Excluir o motorista antes dos contatos, por que do contrário as regras do contato não permitem a exclusão
		getMotoristaDAO().removeMotoristaComplete(id);
	}
	
	/**
	 * Exclui a pessoa 
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removePessoaById(Long id) {
		
		List<Contato> list = findReferenciasByIdMotorista(id);
		for (Contato contato : list) {
			contatoService.removeByIdCascade(contato.getIdContato());
		}
		
		pessoaService.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			removeById(id);
		}
	}
	
	public Motorista storeWorkflow(Motorista bean) {
		return super.beforeStore(bean);
	}

	protected Motorista beforeStore(Motorista bean) {
		Motorista motorista = (Motorista) bean;

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		if (motorista.getDtEmissao().compareTo(today) > 0 )
			throw new BusinessException("LMS-26019");
		if(!SessionUtils.isIntegrationRunning()){
		if (motorista.getDtVencimentoHabilitacao() != null)
		if (motorista.getDtEmissao().compareTo(motorista.getDtVencimentoHabilitacao()) > 0)
			throw new BusinessException("LMS-26069");
		}
		if(!SessionUtils.isIntegrationRunning()){
		if (motorista.getDtVencimentoHabilitacao() != null)
		if(motorista.getDtVencimentoHabilitacao().compareTo(JTDateTimeUtils.getDataAtual())<= 0)
			throw new BusinessException("LMS-26008");
		}	
		if (motorista.getDtNascimento().compareTo(today.minusYears(18)) > 0)
			throw new BusinessException("LMS-26021");

		if (motorista.getPessoa().getDtEmissaoRg() != null && motorista.getPessoa().getDtEmissaoRg().compareTo(today) > 0)
			throw new BusinessException("LMS-26022");

		if (motorista.getDtEmissaoCarteiraProfission() != null && motorista.getDtEmissaoCarteiraProfission().compareTo(today) > 0)
			throw new BusinessException("LMS-26085");

		//regra 3.24
		if(motorista.getUsuarioMotorista()!= null){
			if(getMotoristaDAO().findUsuarioMotoristaById(motorista.getIdMotorista(),motorista.getUsuarioMotorista().getIdUsuario()))
				throw new BusinessException("LMS-26027");
		}

		/** Salva pessoa do pojo motorista */
		pessoaService.store(motorista);

		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Serializable storeMap(TypedFlatMap map) {
		Motorista motorista = map2bean(map);		
		boolean isNovo = motorista.getIdMotorista() == null;

		EnderecoPessoa endereco = motorista.getPessoa().getEnderecoPessoa();
		motorista.getPessoa().setEnderecoPessoa(null);
		
		boolean isFuncionario = MotoristaConstants.TIPO_VINCULO_FUNCIONARIO.getValue().equals(motorista.getTpVinculo().getValue());
		
		setTpSituacao(motorista, isFuncionario);
		
		Long idMotorista = null;
		if(motorista.getPessoa().getIdPessoa() == null) {		
			idMotorista = (Long) super.store(motorista);
		} else {
			idMotorista = (Long) store(motorista);
		}

		getMotoristaDAO().getAdsmHibernateTemplate().flush();

		endereco.setPessoa(motorista.getPessoa());
		Long idEndereco = (Long) enderecoService.store(endereco);
		motorista.getPessoa().setEnderecoPessoa(endereco);
		idMotorista = (Long) super.store(motorista);
		
		TypedFlatMap retorno = new TypedFlatMap();
		
		retorno.put("idMotorista", idMotorista);
		retorno.put("idEndereco", idEndereco);

		//Se o vinculo do motorista for diferente de FUNCIONARIO, entao salva as referencias pessoais e profissionais
		if (!isFuncionario){
			Map<String, Object> idsReferencias = storeReferencias(motorista, map);
			retorno.putAll(idsReferencias);
			try {
				getMotoristaDAO().getAdsmHibernateTemplate().flush();
			} catch (DataIntegrityViolationException ex){
				throw new BusinessException("LMS-26070");
			}
		}
		
		executeWorkflow(motorista,isNovo);
		
    	getMotoristaDAO().getAdsmHibernateTemplate().flush();			
    	getMotoristaDAO().getAdsmHibernateTemplate().refresh(motorista);
		
		getUsuarioAlteracao(motorista, retorno);
		getDtAtualizacao(motorista, retorno);
		getDadosWorkflow(motorista, retorno);
		getTpSituacao(motorista, retorno);
		getBlBloqueio(motorista, retorno);
		
		return retorno;
	}

	/**
	 * 
	 * @param motorista
	 * @param retorno
	 * @param isFuncionario
	 */
	private void setTpSituacao(Motorista motorista, boolean isFuncionario) {		
		if(motorista.getIdMotorista() == null){
			if(SessionUtils.isFilialSessaoMatriz() || isFuncionario){
				motorista.setTpSituacao(new DomainValue(MotoristaConstants.STATUS_ATIVO.getValue()));
			} else {
				motorista.setTpSituacao(new DomainValue(MotoristaConstants.STATUS_INATIVO.getValue()));
			}	
		}
	}
	
	/**
	 * 
	 * @param motorista
	 * @param retorno
	 * @param isFuncionario
	 */
	private void getTpSituacao(Motorista motorista, TypedFlatMap retorno) {
		if(motorista.getTpSituacao() == null){
			return;
		}
		
		retorno.put("tpSituacao", motorista.getTpSituacao().getValue());
	}
	
	private void getBlBloqueio(Motorista motorista, TypedFlatMap retorno) {
		if(this.getMotoristaDAO().verificaBloqueio(motorista.getIdMotorista())) {
			retorno.put("blBloqueio", "S");
		}else{
			retorno.put("blBloqueio", "N");
		}
	}

	/**
	 * @param motorista
	 * @param retorno
	 */
	private void getDtAtualizacao(Motorista motorista, TypedFlatMap retorno) {
		retorno.put("dtAtualizacao", motorista.getDtAtualizacao());
	}

	/**
	 * Executa ação de workflow caso seja necessário.
	 * 
	 * @param motorista
	 * @param isNovo 
	 * @param retorno
	 */
	private void executeWorkflow(Motorista motorista, boolean isNovo) {
		if(!isGenerateWorflow(MotoristaConstants.TIPO_VINCULO_FUNCIONARIO.getValue().equals(motorista.getTpVinculo().getValue()))){
			return;
		}
		
		executeWorkflowMotoristaService.executeWorkflowPendencia(motorista,isNovo);		
	}

	/**
	 * @param motorista
	 * @param retorno
	 */
	private void getDadosWorkflow(Motorista motorista, TypedFlatMap map) {
		boolean isCadDisabled = false;
		
		Pendencia pendencia = motorista.getPendencia();
		
		if(pendencia != null){		
			boolean isAProvado = ConstantesWorkflow.EM_APROVACAO.equals(pendencia.getTpSituacaoPendencia().getValue());
			map.put("dsPendencia", motorista.getPendencia().getTpSituacaoPendencia().getDescriptionAsString());
			
			Short nrTipoEvento = motorista.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
			
			if(isCadastroNovo(nrTipoEvento) && isAProvado){
				isCadDisabled = true;
			}
		}		
		
		
		map.put("desabilitaCad", isCadDisabled);
	}
	
	private boolean isCadastroNovo(Short nrTipoEvento) {
		return ConstantesWorkflow.NR2611_APROVACAO_CADASTRO_MOTORISTA_CE.equals(nrTipoEvento) || ConstantesWorkflow.NR2614_APROVACAO_CADASTRO_MOTORISTA_VI.equals(nrTipoEvento);
	}
	
	/**
	 * Verifica se é necessário gerar workflow.
	 * 
	 * @param isFuncionario
	 * @return boolean
	 */
	private boolean isGenerateWorflow(boolean isFuncionario) {
		if(SessionUtils.isFilialSessaoMatriz() || isFuncionario){
			return false;
		}
		
		return true;
	}

	/**
	 * Mapeia os itens de um TypedFlatMap num pojo de Motorista
	 * @param map
	 * @return
	 */
	private Motorista map2bean(TypedFlatMap map){
		Long idMotorista = map.getLong("idMotorista");

		Motorista motorista = null;

		if (idMotorista != null) {
			motorista = (Motorista) getMotoristaDAO().getAdsmHibernateTemplate().get(Motorista.class, idMotorista);
			motorista.setUsuarioAlteracao(SessionUtils.getUsuarioLogado());
		} else {
			motorista = new Motorista();
			motorista.setIdMotorista(null);
		}		

		motorista.setTpVinculo(map.getDomainValue("tpVinculo"));
		motorista.setTpSexo(map.getDomainValue("tpSexo"));
		motorista.setDsClasse(map.getString("dsClasse"));
		motorista.setDtVencimentoHabilitacao(map.getYearMonthDay("dtVencimentoHabilitacao"));
		motorista.setDtNascimento(map.getYearMonthDay("dtNascimento"));
		motorista.setNrCarteiraHabilitacao(map.getLong("nrCarteiraHabilitacao"));
		motorista.setDtEmissao(map.getYearMonthDay("dtEmissao"));
		motorista.setTpCorPele(map.getDomainValue("tpCorPele"));
		motorista.setTpCorCabelo(map.getDomainValue("tpCorCabelo"));
		motorista.setTpCorOlhos(map.getDomainValue("tpCorOlhos"));
		motorista.setBlPossuiBarba(map.getBoolean("blPossuiBarba"));
		motorista.setTpSituacao(map.getDomainValue("tpSituacao"));
		motorista.setNrProntuario(map.getInteger("nrProntuario"));
		motorista.setNrCarteiraProfissional(map.getLong("nrCarteiraProfissional"));
		motorista.setNrSerieCarteiraProfissional(map.getLong("nrSerieCarteiraProfissional"));
		motorista.setDtEmissaoCarteiraProfission(map.getYearMonthDay("dtEmissaoCarteiraProfission"));
		motorista.setNmPai(map.getString("nmPai"));
		motorista.setNmMae(map.getString("nmMae"));
		motorista.setDtAtualizacao(JTDateTimeUtils.getDataAtual());		

		Pessoa pessoa = pessoaService.findByIdentificacao(map.getDomainValue("pessoa.tpIdentificacao").getValue(), PessoaUtils.clearIdentificacao(map.getString("pessoa.nrIdentificacao")));
		if(pessoa == null) {
			pessoa = new Pessoa();		
		}
		
		pessoa.setTpPessoa(map.getDomainValue("pessoa.tpPessoa"));
		pessoa.setNrIdentificacao(PessoaUtils.clearIdentificacao(map.getString("pessoa.nrIdentificacao")));
		pessoa.setNmPessoa(map.getString("pessoa.nmPessoa"));
		pessoa.setTpIdentificacao(map.getDomainValue("pessoa.tpIdentificacao"));
		if(map.getLong("pessoa.idPessoa") != null) {
		pessoa.setIdPessoa(map.getLong("pessoa.idPessoa"));
		} 
		pessoa.setNrRg(map.getString("pessoa.nrRg"));
		pessoa.setDtEmissaoRg(map.getYearMonthDay("pessoa.dtEmissaoRg"));
		pessoa.setDsOrgaoEmissorRg(map.getString("pessoa.dsOrgaoEmissorRg"));

		motorista.setPessoa(pessoa);

		//Endereco
		EnderecoPessoa endereco = new EnderecoPessoa();
		endereco.setIdEnderecoPessoa(map.getLong("endereco.idEndereco"));
		endereco.setNrCep(map.getString("endereco.nrCep"));
		TipoLogradouro tipoLogradouro = new TipoLogradouro();
		tipoLogradouro.setIdTipoLogradouro(map.getLong("endereco.tipoLogradouro.idTipoLogradouro"));
		endereco.setTipoLogradouro(tipoLogradouro);
		endereco.setDsEndereco(map.getString("endereco.dsEndereco"));
		
		if(map.getString("endereco.nrEndereco") != null){
			endereco.setNrEndereco(map.getString("endereco.nrEndereco").trim());
		}
		
		endereco.setDsComplemento(map.getString("endereco.dsComplemento"));
		endereco.setDsBairro(map.getString("endereco.dsBairro"));
		Municipio municipio = municipioService.findById(map.getLong("endereco.municipio.idMunicipio"));
		endereco.setMunicipio(municipio);
		endereco.setDtVigenciaInicial(map.getYearMonthDay("endereco.dtVigenciaInicial"));
		endereco.setDtVigenciaFinal(map.getYearMonthDay("endereco.dtVigenciaFinal"));
		
		endereco.setPessoa(pessoa);		
		pessoa.setEnderecoPessoa(endereco);		

		String imFotoMotorista = map.getString("imFotoMotorista");
		List<FotoMotorista> fotos = motorista.getFotoMotoristas();
		if (StringUtils.isNotBlank(imFotoMotorista)) {

			FotoMotorista fmt = null;

			if (fotos == null){
				fotos = new ArrayList<FotoMotorista>();
				motorista.setFotoMotoristas(fotos);
			}
			if (!fotos.isEmpty()) {
				fmt = fotos.get(0);
			} else {			
				fmt = new FotoMotorista();
				fmt.setMotorista(motorista);
				fotos.add(fmt);
			}
			try {
				fmt.setImFotoMotorista(Base64Util.decode(imFotoMotorista));
			} catch (IOException e) {
				throw new InfrastructureException(e.getMessage());
			}		
		} else if (fotos != null) {
			fotos.clear();
		}

		if (map.getLong("unidadeFederativa.idUnidadeFederativa") != null) {
			UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
			unidadeFederativa.setIdUnidadeFederativa(map.getLong("unidadeFederativa.idUnidadeFederativa"));
			motorista.setUnidadeFederativa(unidadeFederativa);
		}

		if (map.getLong("municipioNaturalidade.idMunicipio") != null) {
			Municipio municipioNaturalidade = new Municipio();
			municipioNaturalidade.setIdMunicipio(map.getLong("municipioNaturalidade.idMunicipio"));
			motorista.setMunicipioNaturalidade(municipioNaturalidade);
		}

		if (map.getLong("localEmissaoIdentidade.idMunicipio") != null){
			Municipio localEmissaoIdentidade = new Municipio();
			localEmissaoIdentidade.setIdMunicipio(map.getLong("localEmissaoIdentidade.idMunicipio"));
			motorista.setLocalEmissaoIdentidade(localEmissaoIdentidade);
		}

		if (map.getLong("usuario.idUsuario") != null) {
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(map.getLong("usuario.idUsuario"));
			motorista.setUsuario(usuario);
		}

		if (map.getLong("usuarioMotorista.idUsuario") != null){
			Usuario usuarioMotorista = new Usuario();			
			usuarioMotorista.setIdUsuario(map.getLong("usuarioMotorista.idUsuario"));
			motorista.setUsuarioMotorista(usuarioMotorista);			
		}

		if (map.getLong("filial.idFilial") != null){
			Filial filial = new Filial();
			filial.setIdFilial(map.getLong("filial.idFilial"));
			motorista.setFilial(filial);
		}
		if(map.getDomainValue("tpOperacao") != null){
			motorista.setTpOperacao(map.getDomainValue("tpOperacao"));
		}

		return motorista; 
	}

	/**
	 * Mapeia o pojo de Motorista para um TypedFlatMap
	 * @param motorista
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TypedFlatMap bean2map(Motorista motorista){
		TypedFlatMap map = new TypedFlatMap();				

		map.put("idMotorista", motorista.getIdMotorista());		
		map.put("tpVinculo", motorista.getTpVinculo().getValue());		
		if (motorista.getTpSexo() != null)
		map.put("tpSexo", motorista.getTpSexo().getValue());		
		map.put("dsClasse", motorista.getDsClasse());
		map.put("dtVencimentoHabilitacao", motorista.getDtVencimentoHabilitacao());		
		map.put("dtNascimento", motorista.getDtNascimento());		
		map.put("nrCarteiraHabilitacao", motorista.getNrCarteiraHabilitacao());		
		map.put("dtEmissao", motorista.getDtEmissao());		
		if (motorista.getTpCorPele() != null)
		map.put("tpCorPele", motorista.getTpCorPele().getValue());
		if (motorista.getTpCorCabelo() != null)
		map.put("tpCorCabelo", motorista.getTpCorCabelo().getValue());		
		if (motorista.getTpCorOlhos() != null)
		map.put("tpCorOlhos", motorista.getTpCorOlhos().getValue());		
		map.put("blPossuiBarba", motorista.getBlPossuiBarba());			
		map.put("nrProntuario", motorista.getNrProntuario());		
		map.put("nrCarteiraProfissional", motorista.getNrCarteiraProfissional());		
		map.put("nrSerieCarteiraProfissional", motorista.getNrSerieCarteiraProfissional());		
		map.put("dtEmissaoCarteiraProfission", motorista.getDtEmissaoCarteiraProfission());
		
		getDtAtualizacao(motorista, map);		
		getDadosWorkflow(motorista, map);			
		getUsuarioAlteracao(motorista, map);
		getTpSituacao(motorista, map);
		
		map.put("nmPai", motorista.getNmPai());		
		map.put("nmMae", motorista.getNmMae());

		List<FotoMotorista> fotoMotoristas = motorista.getFotoMotoristas();
		if (fotoMotoristas != null && !fotoMotoristas.isEmpty()) {
			FotoMotorista fotoMotorista = fotoMotoristas.get(0);
			
			/*
			 * Pode acontecer de a base sofrer alguma alteração manual ou importação de
			 * registros e danificar o arquivo da foto.
			 */
			if(fotoMotorista != null && fotoMotorista.getImFotoMotorista() != null){
				map.put("imFotoMotorista", Base64Util.encode(fotoMotorista.getImFotoMotorista()));
				map.put("idFotoMotorista", fotoMotorista.getIdFotoMotorista());
			}		
		}

		Pessoa pessoa = motorista.getPessoa();
		map.put("pessoa.tpPessoa", pessoa.getTpPessoa().getValue());
		map.put("pessoa.nmPessoa", pessoa.getNmPessoa());
		map.put("pessoa.nrIdentificacao", pessoa.getNrIdentificacao());
		map.put("pessoa.tpIdentificacao", pessoa.getTpIdentificacao().getValue());
		map.put("pessoa.idPessoa", pessoa.getIdPessoa());		
		map.put("pessoa.nrRg", pessoa.getNrRg());
		map.put("pessoa.dsOrgaoEmissorRg", pessoa.getDsOrgaoEmissorRg());
		map.put("pessoa.dtEmissaoRg", pessoa.getDtEmissaoRg());

		EnderecoPessoa enderecoPadrao = pessoa.getEnderecoPessoa();
		if(enderecoPadrao != null) {
			map.put("endereco.idEndereco", enderecoPadrao.getIdEnderecoPessoa());
			map.put("endereco.municipio.unidadeFederativa.pais.idPais", enderecoPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais());
			map.put("endereco.municipio.unidadeFederativa.pais.nmPais", enderecoPadrao.getMunicipio().getUnidadeFederativa().getPais().getNmPais());
			map.put("endereco.nrCep", enderecoPadrao.getNrCep());
			map.put("endereco.nrCepLookup.nrCep", enderecoPadrao.getNrCep());
			map.put("endereco.municipio.unidadeFederativa.pais.blCepAlfanumerico", enderecoPadrao.getMunicipio().getUnidadeFederativa().getPais().getBlCepAlfanumerico());			
			map.put("endereco.nrCepLookup.cepCriteria", enderecoPadrao.getNrCep());
			map.put("endereco.tipoLogradouro.idTipoLogradouro", enderecoPadrao.getTipoLogradouro().getIdTipoLogradouro());			
			map.put("endereco.dsEndereco", enderecoPadrao.getDsEndereco());
			map.put("endereco.nrEndereco", enderecoPadrao.getNrEndereco());
			map.put("endereco.dsComplemento", enderecoPadrao.getDsComplemento());
			map.put("endereco.dsBairro", enderecoPadrao.getDsBairro());				
			map.put("endereco.municipio.idMunicipio", enderecoPadrao.getMunicipio().getIdMunicipio());
			map.put("endereco.municipio.nmMunicipio", enderecoPadrao.getMunicipio().getNmMunicipio());
			map.put("endereco.municipio.unidadeFederativa.idUnidadeFederativa", enderecoPadrao.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
			map.put("endereco.municipio.unidadeFederativa.sgUnidadeFederativa", enderecoPadrao.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			map.put("endereco.municipio.unidadeFederativa.nmUnidadeFederativa", enderecoPadrao.getMunicipio().getUnidadeFederativa().getNmUnidadeFederativa());
			map.put("endereco.dtVigenciaInicial", enderecoPadrao.getDtVigenciaInicial());
			map.put("endereco.dtVigenciaFinal", enderecoPadrao.getDtVigenciaFinal());			
		}

		UnidadeFederativa unidadeFederativa = motorista.getUnidadeFederativa();
		if (unidadeFederativa != null){			
			map.put("unidadeFederativa.idUnidadeFederativa", unidadeFederativa.getIdUnidadeFederativa());
			map.put("unidadeFederativa.nmUnidadeFederativa", unidadeFederativa.getNmUnidadeFederativa());
			map.put("unidadeFederativa.sgUnidadeFederativa", unidadeFederativa.getSgUnidadeFederativa());
			map.put("unidadeFederativa.pais.nmPais", unidadeFederativa.getPais().getNmPais());
		}

		Municipio municipioNaturalidade = motorista.getMunicipioNaturalidade();	
		if (municipioNaturalidade != null) {			
			map.put("municipioNaturalidade.idMunicipio", municipioNaturalidade.getIdMunicipio());
			map.put("municipioNaturalidade.nmMunicipio", municipioNaturalidade.getNmMunicipio());
			map.put("sgUnidadeFederativa", municipioNaturalidade.getUnidadeFederativa().getSgUnidadeFederativa());
			map.put("nmPais", municipioNaturalidade.getUnidadeFederativa().getPais().getNmPais());			
		}

		Municipio localEmissaoIdentidade = motorista.getLocalEmissaoIdentidade();
		if (localEmissaoIdentidade != null){			
			map.put("localEmissaoIdentidade.idMunicipio", localEmissaoIdentidade.getIdMunicipio());
			map.put("localEmissaoIdentidade.nmMunicipio", localEmissaoIdentidade.getNmMunicipio());
			map.put("localEmissaoIdentidade.sgUFEmissao", localEmissaoIdentidade.getUnidadeFederativa().getSgUnidadeFederativa());
		}

		Usuario usuario = motorista.getUsuario();
		if (usuario != null) {
			map.put("usuario.idUsuario", usuario.getIdUsuario());
			map.put("usuario.nrMatricula", usuario.getNrMatricula());
			map.put("usuario.nmUsuario", usuario.getNmUsuario());						
		}

		Usuario usuarioMotorista = motorista.getUsuarioMotorista();
		if (usuarioMotorista != null){			
			map.put("usuarioMotorista.idUsuario", usuarioMotorista.getIdUsuario());
			map.put("funcionario.nrMatricula", usuarioMotorista.getNrMatricula());			
			map.put("funcionario.nmFuncionario", usuarioMotorista.getNmUsuario());
		}
		

		Filial filial = motorista.getFilial(); 
		if (filial != null){
			map.put("filial.idFilial", filial.getIdFilial());
			map.put("filial.sgFilial", filial.getSgFilial());
			map.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());			
		}	
		
		if(motorista.getTpOperacao() != null){
			map.put("tpOperacao", motorista.getTpOperacao().getValue());			
		}
		return map;
	}

	/**
	 * @param motorista
	 * @param map
	 */
	private void getUsuarioAlteracao(Motorista motorista, TypedFlatMap map) {
		Usuario usuarioAlteracao = motorista.getUsuarioAlteracao();
		if (usuarioAlteracao != null){			
			map.put("usuarioAlteracao.idUsuario", usuarioAlteracao.getIdUsuario());
			map.put("usuarioAlteracao.nrMatricula", usuarioAlteracao.getNrMatricula());			
			map.put("usuarioAlteracao.nmUsuario", usuarioAlteracao.getNmUsuario());
		}
	}
	
	/**
	 * Verifica se o motorista possui registro de liberacao vigente
	 * @param motorista
	 * @return TRUE se o motorista ativo possui registro de liberacao vigente e FALSE caso contrario
	 */
	public boolean verificaLiberacaoMotorista(Motorista motorista) {
		if (!motorista.getTpSituacao().getValue().equals("I") &&
			!this.getMotoristaDAO().verificaLiberacaoMotorista(motorista.getIdMotorista()))
				return false;
		else
			return true;
	}

	/**
	 * Salva as referencias pessoais e profissionais do motorista
	 * @param motorista
	 * @param map
	 */
	private Map<String, Object> storeReferencias(Motorista motorista, Map<String, Object> map) {
		Map<String, Object> ids = new HashMap<String, Object>();

		for (int i=1; i <= 6; i++){

			Long idContato = null;
			if (map.get("ref_idContato_" + i) != null && ((String) map.get("ref_idContato_" + i)).length() > 0)
				idContato = Long.valueOf((String) map.get("ref_idContato_" + i));

			String nmReferencia = (String) map.get("ref_nome_" + i);
			if((nmReferencia == null || nmReferencia.isEmpty()) && (i != 1 && i != 4)  ){
				continue;
			}
			
			String emailReferencia = (String) map.get("ref_email_" + i);

			Long idTelefone = null;
			if (map.get("ref_idTelefone_" + i) != null && ((String) map.get("ref_idTelefone_" + i)).length() > 0)
				idTelefone = Long.valueOf((String)map.get("ref_idTelefone_" + i));
			String tpReferencia = (String) map.get("ref_tpTelefone_" + i);
			String usoReferencia = (String) map.get("ref_uso_" + i);
			String ddiReferencia = (String) map.get("ref_nrDdi_" + i);
			String dddReferencia = (String) map.get("ref_nrDdd_" + i);
			String telefoneReferencia = (String) map.get("ref_nrTelefone_" + i);

			Contato contato = new Contato();

			contato.setTpContato(new DomainValue((i <= 3)? "RP" : "RS"));
			contato.setIdContato(idContato);
			contato.setPessoa(motorista.getPessoa());
			contato.setUsuario(motorista.getUsuarioMotorista());
			contato.setNmContato(nmReferencia);
			contato.setDsEmail(emailReferencia);			
			ids.put("ref_idContato_" + i, contatoService.store(contato));

			TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
			telefoneEndereco.setIdTelefoneEndereco(idTelefone);			
			telefoneEndereco.setPessoa(motorista.getPessoa());
			telefoneEndereco.setTpTelefone(new DomainValue(tpReferencia));
			telefoneEndereco.setTpUso(new DomainValue(usoReferencia));
			telefoneEndereco.setNrDdi(ddiReferencia); 
			telefoneEndereco.setNrDdd(dddReferencia);
			telefoneEndereco.setNrTelefone(telefoneReferencia);
			ids.put("ref_idTelefone_" + i, telefoneEnderecoService.store(telefoneEndereco));

			if (map.get("ref_idTelefoneContato_" + i) == null || ((String) map.get("ref_idTelefoneContato_" + i)).length() <= 0) {				
				TelefoneContato telefoneContato = new TelefoneContato();				
				telefoneContato.setContato(contato);				
				telefoneContato.setTelefoneEndereco(telefoneEndereco);
				ids.put("ref_idTelefoneContato_" + i, telefoneContaoService.store(telefoneContato));
			} else {
				ids.put("ref_idTelefoneContato_" + i, map.get("ref_idTelefoneContato_" + i));
			}
		} 
		return ids;
	}

	/**
	 * Verifica ondo o motorista encontra-se atualmente e retorna sua situacao atual
	 * @param idMotorista Id do motorista
	 * @param validaCarregamento Se for TRUE levanta excecao sempre que o motorista nao puder ser associado, Se for FALSE retorna a situacao
	 * @return Localizacao atual do motorista
	 */
	private void consultaLocalizacaoMotorista(Long idMotorista, Boolean validaCarregamento, Map<String, Object> result) {
		String localizacao = null;
		String excecao = "LMS-26009";
		Boolean blMotoristaBloqueado = Boolean.FALSE;

		Motorista motorista = (Motorista) getMotoristaDAO().get(Motorista.class, idMotorista);
		// 5 - Se a situação do motorista for inativo
		//
		//CQPRO00029166: Mensagem (LMS-00051) lançada na tela, como alert, para não interromper o fluxo, permitir o
		// preenchimento da tela, edição e alteração da situação do motorista.
		//


		// 1 - Se o motorista estiver associado a um controle de cargas 
		List<ControleCarga> controlesCarga = this.getMotoristaDAO().consultaControleCarga(idMotorista);
		if(!controlesCarga.isEmpty()) {
			ControleCarga controleCarga = controlesCarga.get(0);
			Filial filial = controleCarga.getFilialByIdFilialAtualizaStatus();

			localizacao = configuracoesFacade.getMensagem("associadoControleCargas") +
				": " + controleCarga.getTpStatusControleCarga().getDescription().getValue() +
				". " + configuracoesFacade.getMensagem("naFilial") +
				": " + filial.getPessoa().getNmPessoa() +
				" - " + filial.getSgFilial();

		// 2 - Se o motorista nao possuir registro de liberacao da reguladora vigente
		} else if (!this.getMotoristaDAO().verificaLiberacaoMotorista(idMotorista)){
			localizacao = configuracoesFacade.getMensagem("pendenteLiberacaoReguladora");		

		// 3 - Se o motorista possuir registro de bloqueio vigente
		} else if (this.getMotoristaDAO().verificaBloqueio(idMotorista)) {
			localizacao = configuracoesFacade.getMensagem("bloqueado");

		} else if (validaCarregamento.booleanValue()) {

			// 4 - Se o CNH do motorista estiver vencido (somente quando validaCarregamento == false)
			if (motorista.getDtVencimentoHabilitacao() != null)
				if(motorista.getDtVencimentoHabilitacao().isBefore(JTDateTimeUtils.getDataAtual()))
				throw new BusinessException("LMS-26008");

				throw new BusinessException(excecao);

		// 6 - Caso contrario...
		} else {
			localizacao = configuracoesFacade.getMensagem("liberado");
		}
		
		if (this.getMotoristaDAO().verificaBloqueio(idMotorista)) {
			blMotoristaBloqueado = Boolean.TRUE;
		}

		result.put("localizacao", localizacao);
		if(blMotoristaBloqueado)
			result.put("blBloqueio", "S");
		else
			result.put("blBloqueio", "N");

	}

	public Map<String, Object> findLocalizacaoMotorista(Long idMotorista) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		consultaLocalizacaoMotorista(idMotorista, Boolean.FALSE, retorno);

		return retorno;
	}

	public List findLookup(Map criteria) {
		String nrIdentificacao = null;
		Map<String, Object> pessoa = (Map<String, Object>)criteria.get("pessoa");
		if (pessoa!=null) {
			nrIdentificacao = (String)pessoa.get("nrIdentificacao");
		}
		pessoa.put("nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		return super.findLookup(criteria);
	}

	public List<TypedFlatMap> findLookupAsPaginated(TypedFlatMap criteria) {
		criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(criteria.getString("pessoa.nrIdentificacao")));
		List list = getMotoristaDAO().findLookupAsPaginated(criteria);
		// se e somente se for encontrado apenas um registro, atualiza os properties.
		if (list.size() == 1) {
			list = bean2Map(list);
		}
		return list;
	}

	public List findLookupMotorista(TypedFlatMap criteria) {
		criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(criteria.getString("pessoa.nrIdentificacao")));
		List result = getMotoristaDAO().findLookupPersonalizado(criteria);
		return new AliasToNestedBeanResultTransformer(Motorista.class).transformListResult(result);
	}
	
	public List findLookupInstrutorMotorista(TypedFlatMap criteria) {
		criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(criteria.getString("pessoa.nrIdentificacao")));
		List result = getMotoristaDAO().findLookupInstrutorPersonalizado(criteria);
		return new AliasToNestedBeanResultTransformer(Motorista.class).transformListResult(result);
	}

	/**
	 * Localiza uma lista de resultados a partir dos critérios de busca 
	 * informados. Permite especificar regras de ordenação.
	 * 
	 * @param criterions Critérios de busca.
	 * @param lista com criterios de ordenação. Deve ser uma java.lang.String no formato
	 * <code>nomePropriedade:asc</code> ou <code>associacao_.nomePropriedade:desc</code>.
	 * A utilização de <code>asc</code> ou <code>desc</code> é opcional sendo o padrão <code>asc</code>.
	 * @return Lista de resultados sem paginação.
	 */ 
	public List<Motorista> findListByCriteria(Map<String, Object> criteria, List listaOrdenacao) {
		return getMotoristaDAO().findListByCriteria(criteria, listaOrdenacao);
	}

	/**
	 * 
	 * @param idEquipeOperacao
	 * @return
	 */
	public List findMotoristaControleCarga(Long idEquipeOperacao) {
		if (idEquipeOperacao == null)
			return Collections.EMPTY_LIST;

		Map<String, Object> mapEquipeOperacao = new HashMap<String, Object>();
		mapEquipeOperacao.put("idEquipeOperacao", idEquipeOperacao);

		Map<String, Object> mapIntegranteEqOperac = new HashMap<String, Object>();
		mapIntegranteEqOperac.put("equipeOperacao", mapEquipeOperacao);

		Map<String, Object> mapUsuarioMotorista = new HashMap<String, Object>();
		mapUsuarioMotorista.put("integranteEqOperacs", mapIntegranteEqOperac);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usuarioMotorista", mapUsuarioMotorista);
		map.put("tpSituacao", MotoristaConstants.STATUS_ATIVO.getValue());

		List<String> campoOrdenacao = new ArrayList<String>();
		campoOrdenacao.add("pessoa_.nmPessoa:asc");

		List<Motorista> list = findListByCriteria(map, campoOrdenacao);
		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>(list.size());
		for(Motorista motorista : list) {
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idMotorista", motorista.getIdMotorista());
			typedFlatMap.put("pessoa.nmPessoa", motorista.getPessoa().getNmPessoa());
			retorno.add(typedFlatMap);
		}
		return retorno;
	}

	/**
	 * faz o update do blTermoCompromisso para true quando é emitido o pdf de "Termo Compromisso"
	 * @param idMotorista
	 * @param blTermoComp
	 */
	public void updateBlTermoComp(Long idMotorista, boolean blTermoComp) {
		getMotoristaDAO().updateBlTermoComp(idMotorista, blTermoComp);
	}

	/**
	 * Retorna 'true' se a pessoa informada é um motorista ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isMotorista(Long idPessoa){
		return getMotoristaDAO().isMotorista(idPessoa);
	}

	/**
	 * Retorna 'true' se a pessoa informada é um motorista ativo e eventual ou agregado senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isMotoristaEventual(Long idPessoa){
		return getMotoristaDAO().isMotorista(idPessoa, MotoristaConstants.TIPO_VINCULO_FUNCIONARIO.getValue());
	}

	public ResultSetPage<AnexoMotorista> findPaginatedAnexoMotorista(PaginatedQuery paginatedQuery) {
		return getMotoristaDAO().findPaginatedAnexoMotorista(paginatedQuery);
	}
	
	public Integer getRowCountAnexoMotorista(TypedFlatMap criteria) {
		return getMotoristaDAO().getRowCountAnexoMotorista(criteria);
	}
	
	/**
	 * Executa o store de um anexo no banco, como também atualiza a entidade do
	 * motorista com a data e usuário da alteração.
	 * 
	 * @param map
	 * 
	 * @return Serializable
	 */
	public Serializable storeAnexoMotorista(TypedFlatMap map){
		Long idMotorista = map.getLong("idMotorista");
				
		/*
		 * Atualiza alteração do registro.
		 */
		getMotoristaDAO().updateMotoristaAlteracao(idMotorista, SessionUtils.getUsuarioLogado().getIdUsuario());
		
		/*
		 * Grava anexo.
		 */
		Motorista motorista = findById(idMotorista);
		
    	storeAnexo(map, motorista);
    	
    	/*
    	 * Executa workflow se necessário.
    	 */
    	executeWorkflow(motorista,false);
    	    	
    	/*
    	 * Retorna os dados atualizados para a tela. 
    	 */
    	getMotoristaDAO().getAdsmHibernateTemplate().flush();			
    	getMotoristaDAO().getAdsmHibernateTemplate().refresh(motorista);
    	
    	TypedFlatMap result = new TypedFlatMap();    	
    	getUsuarioAlteracao(motorista, result);
		getDtAtualizacao(motorista, result);
		getDadosWorkflow(motorista, result);
		getTpSituacao(motorista, result);
    	    	
    	return result;
	}

	/**
	 * Persiste um anexo.
	 * 
	 * @param map
	 * @param motorista
	 */
	private void storeAnexo(TypedFlatMap map, Motorista motorista) {
		AnexoMotorista anexoMotorista = new AnexoMotorista();
		anexoMotorista.setIdAnexoMotorista(map.getLong("idAnexoMotorista"));
    	anexoMotorista.setMotorista(motorista);
    	
    	UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	anexoMotorista.setUsuario(usuarioLMS);
    	
    	try {
    		anexoMotorista.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
    	} catch (IOException e) {
			throw new InfrastructureException(e);
		}
    	
    	anexoMotorista.setDsAnexo(map.getString("dsAnexo"));    	
    	anexoMotorista.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	getMotoristaDAO().store(anexoMotorista);
	}
	
	public AnexoMotorista findAnexoMotoristaById(Long idAnexoMotorista) {
		AnexoMotorista anexoMotorista = getMotoristaDAO().findAnexoMotoristaById(idAnexoMotorista);
		if(anexoMotorista != null){
			Hibernate.initialize(anexoMotorista);
		}
		return anexoMotorista;
	}
	
	/**
	 * Executa a remoção de um anexo no banco, como também atualiza a entidade do
	 * motorista com a data e usuário da alteração.
	 * 
	 * @param map
	 * 
	 * @return Serializable
	 */
	public Serializable removeByIdsAnexoMotorista(TypedFlatMap map) {
		Long idMotorista = map.getLong("idMotorista");
		
		/*
		 * Remove anexo(s).
		 */
		getMotoristaDAO().removeByIdsAnexoMotorista(getAnexoIds(map));		
		
		/*
		 * Atualiza alteração do registro.
		 */		
		getMotoristaDAO().updateMotoristaAlteracao(idMotorista, SessionUtils.getUsuarioLogado().getIdUsuario());
		
		/*
		 * Executa workflow se necessário.
		 */
		Motorista motorista = findById(idMotorista);
				
		executeWorkflow(motorista,false);
	
		/*
    	 * Retorna os dados atualizados para a tela. 
    	 */
		getMotoristaDAO().getAdsmHibernateTemplate().flush();			
    	getMotoristaDAO().getAdsmHibernateTemplate().refresh(motorista);
    	
		TypedFlatMap result = new TypedFlatMap();		
		getUsuarioAlteracao(motorista, result);
		getDtAtualizacao(motorista, result);
		getDadosWorkflow(motorista, result);
		getTpSituacao(motorista, result);
		
		return result;
	}

	/**
	 * Converte uma lista de ids de remoção de String para Long.
	 * 
	 * @param map
	 * @return List<Long>
	 */
	@SuppressWarnings("unchecked")
	private List<Long> getAnexoIds(TypedFlatMap map) {
		List<Long> listaIds = new ArrayList<Long>();						
		List<String> list = map.getList("ids");
		
		for (String id : list) {
			listaIds.add(Long.parseLong(id));
		}
		
		return listaIds;
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final MotoristaDAO getMotoristaDAO() {
		return (MotoristaDAO) getDao();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMotoristaDAO(MotoristaDAO dao) {
		setDao(dao);
	}

	public void setPessoaService(PessoaService pessoa) {
		this.pessoaService = pessoa;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setTelefoneContaoService(TelefoneContatoService telefoneContaoService) {
		this.telefoneContaoService = telefoneContaoService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setEnderecoService(EnderecoPessoaService enderecoService) {
		this.enderecoService = enderecoService;
	}

	public ExecuteWorkflowMotoristaService getExecuteWorkflowMotoristaService() {
		return executeWorkflowMotoristaService;
	}

	public void setExecuteWorkflowMotoristaService(
			ExecuteWorkflowMotoristaService executeWorkflowMotoristaService) {
		this.executeWorkflowMotoristaService = executeWorkflowMotoristaService;
	}	
}