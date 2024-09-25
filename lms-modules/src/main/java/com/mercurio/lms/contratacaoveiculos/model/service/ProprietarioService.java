package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.contratacaoveiculos.model.AnexoProprietario;
import com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.dao.ProprietarioDAO;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.DescontoRfcService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.ValidateUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.proprietarioService"
 */
public class ProprietarioService extends CrudService<Proprietario, Long> {
	private static final String LMS_26160 = "LMS-26160";
	private PessoaService pessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ContaBancariaService contaBancariaService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	private BeneficiarioProprietarioService beneficiarioProprietarioService;
	private EnderecoPessoaService enderecoPessoaService;
	private ExecuteWorkflowProprietarioService executeWorkflowProprietarioService;  
	private DescontoRfcService descontoRfcService;

	
	/**
	 * Recupera uma inst�ncia de <code>Proprietario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
	public Proprietario findById(java.lang.Long id) {
		return (Proprietario)super.findById(id);
	}

	/**
	 * O m�todo findById retorno erro caso n�o exista, este deve retornar null
	 * 
	 * @param idProprietario
	 * @return
	 */
	public Proprietario findByIdReturnNull(final Long idProprietario){
		return getProprietarioDAO().findByIdReturnNull(idProprietario);
	}

	/**
	 * Recupera um Map de <code>Proprietario</code> a partir do ID, 
	 * e o valor do bot�o de bloqueio (bloquear/desbloquear).
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Map do proprietario
	 */
	public Map<String, Object> findByIdDetalhamento(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Proprietario pro = (Proprietario) super.findById(id);
		if (pro != null) {
			ReflectionUtils.copyNestedBean(map, pro);
			map.put("nrAntt",FormatUtils.formatLongWithZeros(pro.getNrAntt(), "00000000000000"));
			if ( verificaBloqueiosVigentes(pro) )
				map.put("valorBotaoBloqueio", "desbloquear");
			else
				map.put("valorBotaoBloqueio", "bloquear");
		}
		return map;
	}

	/**
	 * Verifica se proprietario possui bloqueios vigentes. 
	 * Em caso afirmativo retorna <i>desbloquear</i>.
	 * Em caso negativo, retorna <i>bloquear</i>.
	 * @param idProprietario
	 * @author luisfco
	 */
	public Map<String, Object> validateBloqueiosVigentes(Long idProprietario) {
		Map<String, Object> map = new HashMap<String, Object>();
		Proprietario pro = (Proprietario) super.findById(idProprietario);
		if (pro != null && verificaBloqueiosVigentes(pro) )
			map.put("valorBotaoBloqueio", "desbloquear");
		else
			map.put("valorBotaoBloqueio", "bloquear");
		return map;
	}

	public List findLookupProprietario(String nrIdentificacao, String tpSituacao) {
		return findLookupProprietario(nrIdentificacao, tpSituacao, null);
	}

	/**
	 * Busca a lista de propriet�rios de acordo com o n�mero de identifica��o, situa��o do propriet�rio e tipo de pessoa
	 * @param nrIdentificacao N�mero de identifica��o do propriet�rio
	 * @param tpSituacao Situa��o do propriet�rio
	 * @param tpPessoa Tipo de Pessoa
	 * @return Lista de propriet�rios
	 */
	public List<Map<String, Object>> findLookupProprietario(String nrIdentificacao, String tpSituacao, String tpPessoa) {
		nrIdentificacao = PessoaUtils.validateIdentificacao(nrIdentificacao);

		List<Map<String, Object>> rs = getProprietarioDAO().findLookupProprietario(nrIdentificacao, tpSituacao, tpPessoa);
		int i = 0;
		for(Map<String, Object> result : rs) {
			if (result.get("pessoa") != null) {
				Map<String, Object> mPessoa = (Map<String, Object>)result.get("pessoa");
				if (mPessoa.get("nrIdentificacao") != null && mPessoa.get("tpIdentificacao") != null)
					mPessoa.put("nrIdentificacaoFormatado",FormatUtils.formatIdentificacao((String)((Map<String, Object>)mPessoa.get("tpIdentificacao")).get("value"),(String)mPessoa.get("nrIdentificacao")));
				result.put("pessoa",mPessoa);
			}
			rs.set(i, result);
		}
		return rs;
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		ResultSetPage rsp = getProprietarioDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));
		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
			public Map<String, Object> filterItem(Object item) {
				AliasToTypedFlatMapResultTransformer alias = new AliasToTypedFlatMapResultTransformer();
				TypedFlatMap retorno = alias.transformeTupleMap((Map<String, Object>)item);

				retorno.put("filial.siglaNomeFilial",
						retorno.getString("filial.sgFilial") + " - " + retorno.getString("filial.pessoa.nmFantasia"));
				String tpIdentificacao = retorno.getString("pessoa.tpIdentificacao.value");
				String nrIdentificacao = retorno.getString("pessoa.nrIdentificacao");

				if (tpIdentificacao != null && nrIdentificacao != null) {
					retorno.put("pessoa.nrIdentificacaoFormatado",
							FormatUtils.formatIdentificacao(tpIdentificacao,nrIdentificacao));
				}

				List<BeneficiarioProprietario> list = beneficiarioProprietarioService.findBeneficiarioProprietarioVigente(null,retorno.getLong("idProprietario"));
				if (list.size() > 0) {
					BeneficiarioProprietario bp = list.get(0);

					String tpIdentificacaoBenef = bp.getBeneficiario().getPessoa().getTpIdentificacao().getValue();
					String nrIdentificacaoBenef = bp.getBeneficiario().getPessoa().getNrIdentificacao();

					retorno.put("beneficiario.pessoa.tpIdentificacao",bp.getBeneficiario().getPessoa().getTpIdentificacao());
					retorno.put("beneficiario.pessoa.nrIdentificacaoFormatado",FormatUtils.formatIdentificacao(tpIdentificacaoBenef,nrIdentificacaoBenef));
					retorno.put("beneficiario.pessoa.nmPessoa",bp.getBeneficiario().getPessoa().getNmPessoa());
				}
				return retorno;
			}
		};
		return (ResultSetPage)frsp.doFilter();
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getProprietarioDAO().getRowCountCustom(criteria);
	}

	public List findLookup(Map criteria) {
		String nrIdentificacao = null;
		Map<String, Object> pessoa = (Map<String, Object>)criteria.get("pessoa");
		if (pessoa!=null){
			nrIdentificacao = (String)pessoa.get("nrIdentificacao");
		}
		pessoa.put("nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		return super.findLookup(criteria);
	}

	protected Proprietario beforeStore(Proprietario bean) {
		Proprietario proprietario = (Proprietario) bean;

		Long idProprietario = proprietario.getIdProprietario();

		if((proprietario.getPessoa().getTpIdentificacao().getValue().equalsIgnoreCase("CPF")
			|| proprietario.getPessoa().getTpIdentificacao().getValue().equalsIgnoreCase("CNPJ"))
			&& proprietario.getNrAntt() == null)			
				throw new BusinessException("LMS-26017");

		// n�mero do PIS
		Long nrPis = proprietario.getNrPis();
		validateNumeroPIS(idProprietario, nrPis);

		if(bean.getBlCooperado() == null){
			bean.setBlCooperado(new DomainValue("N"));
		}
		
		if(bean.getBlNaoAtualizaDbi() == null){
			bean.setBlNaoAtualizaDbi(new DomainValue("N"));
		}
		
		if(bean.getBlRotaFixa() == null){
			bean.setBlRotaFixa(new DomainValue("N"));
		}
		
		if(bean.getBlMei() == null){
			bean.setBlMei(new DomainValue("N"));
		}
		
		return bean;
	}

	/**
	 * Valida n�mero do pis do propriet�rio. Verifica se na base de dados
	 * j� n�o possui um propriet�rio com o PIS informado que n�o seja o
	 * propriet�rio informado pelo id.
	 * @param idProprietario id do propriet�rio a ser descartado na valida��o
	 * @param nrPis n�mero do PIS que n�o pode ser encontrado outro propriet�rio
	 */
	private void validateNumeroPIS(Long idProprietario, Long nrPis) {
		// Se foi informado o n�mero do PIS, validamos se o
		// mesmo j� n�o est� associado a outro propriet�rio.
		if (nrPis != null) {
			// Tenta persistir um objeto
			Proprietario proprietario = findProprietario(nrPis);
			if (proprietario != null) {
				// Se n�o � uma inser��o e o propriet�rio encontrado n�o � o mesmo, lan�a exce��o.
				if (idProprietario == null || !idProprietario.equals(proprietario.getIdProprietario())) {
					throw new BusinessException("LMS-26103");
				}
				// ignora o propriet�rio encontrado da sess�o.
				getProprietarioDAO().getAdsmHibernateTemplate().evict(proprietario);
			}
		}
	}

	public Boolean hasPIS(Long idProprietario){
		return getProprietarioDAO().hasPIS(idProprietario);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	//FIXME alterar para retornar o id
	public HashMap<String, Object> store(Proprietario proprietario) {
		beforeStore(proprietario);

		HashMap<String, Object> map = new HashMap<String, Object>();
		String flag = "";

		Pessoa pessoa = proprietario.getPessoa();
		Long idPessoa = pessoa.getIdPessoa();
		String tpPessoa = pessoa.getTpPessoa().getValue();
        String tpSituacao = proprietario.getTpSituacao().getValue();

        if (idPessoa != null && "A".equals(tpSituacao)) {
        	// Se n�o existir endere�o padr�o (PROPRIETARIO -> PESSOA -> ENDERECO.PESSOA.ID_ENDERECO_PESSOA = NULL)
        	if (!enderecoPessoaService.hasEnderecoPessoaPadrao(idPessoa)) {
        	    //Retornar a situa��o para "N", incompleta
                proprietario.getTpSituacao().setValue("N");
			    // Emitir mensagem "LMS-26096 - O propriet�rio deve possuir um endere�o comercial caso pessoa jur�dica ou residencial se pessoa f�sica cadastrado."
			    // Abortar Opera��o
                flag = "LMS-26096";
            } else if ("F".equals(tpPessoa)) {
                //Se Pessoa F�sica (PESSOA.TP_PESSOA = "F"), ent�o:

                //Se n�o existir alguma conta banc�ria vigente (CONTA_BANCARIA.DT_VIGENCIA_INICIAL <= "HOJE" E CONTA_BANCARIA.DT_VIGENCIA_FINAL >= "HOJE") ent�o:
                if (!existeContaBancariaVigentePessoa(idPessoa)) {
                    // Retornar a situa��o para "N", incompleta
                	proprietario.getTpSituacao().setValue("N");
                	// Abortar Opera��o
                    flag = "LMS-26093";
				}				
            } else if ("J".equals(tpPessoa)) {
                //Se Pessoa Jur�dica (PESSOA.TP_PESSOA = �J�), ent�o:
    
                //Se n�o existir alguma conta banc�ria vigente (CONTA_BANCARIA.DT_VIGENCIA_INICIAL <= �HOJE� E CONTA_BANCARIA.DT_VIGENCIA_FINAL >= �HOJE�) e inscri��o estadual (PESSOA -> PROPRIETARIO -> INSCRICAO_ESTADUAL) ent�o:
                if (!existeContaBancariaVigentePessoa(idPessoa)) {
                    //Retornar a situa��o para "N", incompleta
					proprietario.getTpSituacao().setValue("N");
					// Emitir mensagem "LMS-26063 - Para ativar o propriet�rio deve ser informada a conta banc�ria ou associ�-lo a um benefici�rio e informar sua Inscri��o Estadual."
					// Abortar Opera��o
                    flag = "LMS-26063";
                } else if (!pessoaService.verifyInscricaoEstadualPadraoAtiva(idPessoa)) {
                    //Se n�o existir inscri��o estadual ativa (INSCRICAO_ESTADUAL.TP_SITUACAO = 'A') e padr�o (INSCRICAO_ESTADUAL.BL_INDICADOR_PADRAO = 'S') vinculada a propriet�rio, ent�o:

                    //Retornar a situa��o para "N", incompleta
                    proprietario.getTpSituacao().setValue("N");
                    // Emitir mensagem "LMS-01021 - Para concluir o cadastro deve ser informada uma Inscri��o Estadual com situa��o ativa e padr�o "
                    // Abortar Opera��o;
                    flag = "LMS-01021";
				}
            }
            if (StringUtils.isBlank(flag)) {
				proprietario.getTpSituacao().setValue("A");
			}
		}

		//store pessoa
		pessoaService.store(proprietario);


		// telefoneEndereco tamb�m referencia proprietario (pessoa)
		proprietario.getTelefoneEndereco().setPessoa(pessoa);

		//Seta a data de atualizacao
		proprietario.setDtAtualizacao(JTDateTimeUtils.getDataAtual());

		// chama direto o store() do dao, pois em caso contr�rio, 
		// chamaria novamente o beforeStore().
		getProprietarioDAO().store(proprietario);

		ReflectionUtils.copyNestedBean(map, proprietario);
		map.put("nrAntt",FormatUtils.formatLongWithZeros(proprietario.getNrAntt(),"00000000000000"));
		if (StringUtils.isNotBlank(flag)) {
		    map.put("flag", flag+" - "+configuracoesFacade.getMensagem(flag));
		}

		return map;
	}

	/**
	 * Retorna TelefoneEndereco do Proprietario.
	 * @param idProprietario � necess�rio que o id do propriet�rio realmente exista registrado no banco.
	 * @return TelefoneEndereco
	 */
	public TelefoneEndereco findTelefonePrincipal(Long idProprietario) {
		Proprietario proprietario = (Proprietario)getProprietarioDAO().getAdsmHibernateTemplate()
				.get(Proprietario.class,idProprietario);
		return proprietario.getTelefoneEndereco();
	}

	/**
	 *
	 * Verifica se existe alguma alguma outra conta banc�ria vigente 
	 * para a pessoa, al�m da conta banc�ria atual.
	 * @param idPessoa
	 * @param idContaBancaria
	 * @return True, em caso afirmativo.
	 */
	public boolean existeContaBancariaVigentePessoa(Long idPessoa) {
		return contaBancariaService.existeContaBancariaVigentePessoa(idPessoa); 
	}

	/**
	 * Obt�m os benefici�rios (bem como sua pessoa) relacionados a determinado proprietario.
	 * @param proprietario
	 * @return list com os beneficiarios do proprietario
	 * @author luisfco
	 */
	public List<BeneficiarioProprietario> findBeneficiariosByProprietario(Long idProprietario) {
		return getProprietarioDAO().findBeneficiariosByProprietario(idProprietario);
	}

	/**
	 * Verifica se a pessoa especificada � Jur�dica.
	 * Em uma inclus�o: Verifica se existe outra pessoa com o mesmo tipo e n�mero de identifica��o informados.
	 * @param criteria
	 * @return
	 */
	public List<TypedFlatMap> validateIdentificacao(TypedFlatMap map) {
		String tpIdentificacao = map.getString("tpIdentificacao");
		String nrIdentificacao = map.getString("nrIdentificacao");

		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>(1);
		
		Pessoa pessoa = validateEspecializacao(Proprietario.class, tpIdentificacao, nrIdentificacao);
		if (pessoa != null) {
			TypedFlatMap row = new TypedFlatMap();
			row.put("idPessoa",pessoa.getIdPessoa());
			row.put("nmPessoa",pessoa.getNmPessoa());
			row.put("dsEmail",pessoa.getDsEmail());
			row.put("tpIdentificacao",pessoa.getTpIdentificacao());
			row.put("nrIdentificacao",pessoa.getNrIdentificacao());
			row.put("nrRg",pessoa.getNrRg());
			row.put("dsOrgaoEmissorRg",pessoa.getDsOrgaoEmissorRg());
			row.put("dtEmissaoRg",pessoa.getDtEmissaoRg());

			TelefoneEndereco te = telefoneEnderecoService.findTelefoneEnderecoPadrao(pessoa.getIdPessoa());
			if (te != null) {
				row.put("idTelefoneEndereco",te.getIdTelefoneEndereco());
				row.put("tpTelefone",te.getTpTelefone().getValue());
				row.put("tpUso",te.getTpUso().getValue());
				row.put("nrDdi",te.getNrDdi());
				row.put("nrDdd",te.getNrDdd());
				row.put("nrTelefone",te.getNrTelefone());
			}
			retorno.add(row);
		}
		return retorno;
	}
	
	public Pessoa validateEspecializacao(Class clazz, String tpIdentificacao, String nrIdentificacao) {
		Pessoa pessoa = configuracoesFacade.getPessoa(nrIdentificacao,tpIdentificacao);
		if (pessoa != null) {
			Object pessoaEspecializada = configuracoesFacade.getPessoa(pessoa.getIdPessoa(),clazz,false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-29045");
			}
		}
		return pessoa;
	}

	public List<DomainValue> findDiasUteisPagamentoSemanal() {
		List<String> diasSemana = new ArrayList<String>(5);
		diasSemana.add("1");
		diasSemana.add("2");
		diasSemana.add("3");
		diasSemana.add("4");
		diasSemana.add("5");
		return domainValueService.findByDomainNameAndValues("DM_DIAS_SEMANA", diasSemana);
	}

	/**
	 * Verifica se h� bloqueios vigentes para o propriet�rio.
	 * @param pro Proprietario
	 * @return True, se n�o h� bloqueios vigentes.
	 */
	public boolean verificaBloqueiosVigentes(Proprietario pro) {
		return bloqueioMotoristaPropService.verificaBloqueiosVigentes(pro);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/** Remove o proprietario e tenta remover a pessoa.
	*
	* @param id indica a entidade que dever� ser removida.
	* 
	*/
	@Transactional(propagation = Propagation.NEVER)
	public void removeProprietarioById(Long id) {
		Proprietario p = this.findById(id);
		if (p.getPessoa().getContaBancarias() != null && !p.getPessoa().getContaBancarias().isEmpty()){
			throw new BusinessException("LMS-26067");
		}

		Long idTelefoneEndereco = p.getTelefoneEndereco().getIdTelefoneEndereco();
		this.removeById(id);

		try {
			telefoneEnderecoService.removeById(idTelefoneEndereco);
			pessoaService.removeById(id);
		} catch (Exception e) {
			// ignora erros de FK na pessoa
		}
	}

	/** Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades quee dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			this.removeProprietarioById(id);
		}
	}

	/**
	 * Retorna 'true' se a pessoa informada � um proprietario ativo sen�o, retorna 'false'.
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isProprietario(Long idPessoa) {
		return getProprietarioDAO().isProprietario(idPessoa);
	} 

	/**
	 * 
	 * @param idProprietario
	 * @return
	 */
	public List<Map<String, Object>> findDadosBancariosByIdProprietario(Long idProprietario){
		return getProprietarioDAO().findDadosBancariosByIdProprietario(idProprietario);
	}

	/**
	 * Busca propriet�rio cadastrado com o n�mero de PIS informado.
	 * 
	 * M�todo utilizado pela Integra��o
	 * @author Felipe Ferreira
	 * 
	 * @param nrPis n�mero do PIS para ser usado como crit�rio
	 * @return int�ncia de Proprietario
	 */
	public Proprietario findProprietario(Long nrPis) {
		return getProprietarioDAO().findProprietarioByPIS(nrPis);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setProprietarioDAO(ProprietarioDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ProprietarioDAO getProprietarioDAO() {
		return (ProprietarioDAO) getDao();
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}
	public void setContaBancariaService(ContaBancariaService contaBancariaService) {
		this.contaBancariaService = contaBancariaService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setBeneficiarioProprietarioService(BeneficiarioProprietarioService beneficiarioProprietarioService) {
		this.beneficiarioProprietarioService = beneficiarioProprietarioService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public ExecuteWorkflowProprietarioService getExecuteWorkflowProprietarioService() {
		return executeWorkflowProprietarioService;
	}
	public void setExecuteWorkflowProprietarioService(
			ExecuteWorkflowProprietarioService executeWorkflowProprietarioService) {
		this.executeWorkflowProprietarioService = executeWorkflowProprietarioService;
	}
	
	public List<Map<String, Object>> findProprietarioSuggest(String nrIdentificacao, String nmPessoa, Integer limiteRegistros) {
		return getProprietarioDAO().findProprietarioSuggest(nrIdentificacao, nmPessoa, limiteRegistros);
	}
	
	// LMS-5590
	public List<Map<String, Object>> findProprietarioSuggestCpf(String nrIdentificacao, String nmPessoa, Integer limiteRegistros) {
		return getProprietarioDAO().findProprietarioSuggestCpf(nrIdentificacao, nmPessoa, limiteRegistros);
	}
	
	/**
	 * Find para tela propriet�rio.
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedProprietario(TypedFlatMap criteria) {	
		return getProprietarioDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));
	}
	
	public Map<String, Object> findProprietarioById(Long id) {
		Proprietario pro = (Proprietario) super.findById(id);
		return ajustaRetorno(pro);
	}

	public Map<String, Object> findByIdProcesso(Long id) {
		Proprietario pro = (Proprietario)  getProprietarioDAO().findByIdProcesso(id);
		return ajustaRetorno(pro);		
	}

	private Map<String, Object> ajustaRetorno(Proprietario pro) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (pro != null) {
			map.put("proprietario", pro);
			map.put("bloquear", verificaBloqueiosVigentes(pro));
		}
		return map;
	}
	
	public void storeProprietarioCommon(Proprietario proprietario, boolean isNovo){		
		beforeStore(proprietario);
		
		validateProprietario(proprietario, isNovo);		
		
		pessoaService.storeByProprietario(proprietario);

		proprietario.getTelefoneEndereco().setPessoa(proprietario.getPessoa());

		proprietario.setDtAtualizacao(JTDateTimeUtils.getDataAtual());
		
		getProprietarioDAO().store(proprietario);
	}	
	
	/**
	 * Atualiza um propriet�rio, grava seus anexos e gera workflow.
	 * 
	 * @param proprietario
	 * @param listAnexoProprietario
	 * 
	 * @return Map<String, Object>
	 */
	public Map<String, Object> storeProprietario(Proprietario proprietario,
			List<AnexoProprietario> listAnexoProprietario) {
		boolean isNovo = proprietario.getIdProprietario() == null;
		
		storeProprietarioCommon(proprietario, isNovo);
		
		/*
		 * Solicita a atualiza��o de queries pendentes em cache e atualiza a
		 * entidade na session.
		 */
		getProprietarioDAO().getAdsmHibernateTemplate().flush();
		getProprietarioDAO().getAdsmHibernateTemplate().refresh(proprietario);

		/*
		 * Se foram inseridos novos anexos, salva todos eles.
		 */
		storeAnexoProprietario(proprietario, listAnexoProprietario);

		/*
		 * Executa workflow ao efetuar alguma altera��o no cadastro, de acordo
		 * com as regras estabelecidas.
		 */
		getExecuteWorkflowProprietarioService().executeWorkflowPendencia(
				proprietario.getPessoa(),
				ExecuteWorkflowProprietarioService.PROPRIETARIO, isNovo);

		return ajustaRetorno(proprietario);
	}

	/**
	 * Valida se pode salvar o propriet�rio. 
	 * 
	 * @param proprietario
	 * @param isNovo
	 */
	private void validateProprietario(Proprietario proprietario, boolean isNovo) {
		Pessoa pessoa = proprietario.getPessoa();
		Long idPessoa = pessoa.getIdPessoa();
		String tpPessoa = pessoa.getTpPessoa().getValue();
		String tpSituacao = proprietario.getTpSituacao().getValue();
		
		if(!isNovo){
			if ("A".equals(tpSituacao)) {
				/*
				 * Se n�o existir endere�o padr�o.
				 */
				if (!enderecoPessoaService.hasEnderecoPessoaPadrao(idPessoa)) {
					// LMS-5482
					proprietario.getTpSituacao().setValue("N");
					return;
				}
				
				/*
				 * Se n�o existir alguma conta banc�ria vigente ou se a pessoa for
				 * do tipo jur�dica e n�o existir inscri��o estadual ativa.
				 */
				if (!existeContaBancariaVigentePessoa(idPessoa)) {
					throwMensagemValidacao("LMS-26093");
				} else if ("J".equals(tpPessoa) && !pessoaService.verifyInscricaoEstadualPadraoAtiva(idPessoa)) {
					throwMensagemValidacao("LMS-01021");
				}
			}
			
			else if("I".equals(proprietario.getTpSituacao().getValue())){
				Boolean desconto = descontoRfcService.isDescontoByProprietariostatus(proprietario);
				
				if(desconto){
					throw new BusinessException(LMS_26160);								
				}
			}
		} else if (idPessoa == null) { /* Se for um novo propriet�rio */
			// LMS-5482
			proprietario.getTpSituacao().setValue("N");
			return;
		}
	}
	
	private void throwMensagemValidacao(String key){		
		throw new BusinessException("LMS-26159", new Object[] { configuracoesFacade.getMensagem(key) });
	}
	
	/**
	 * Grava os anexos do propriet�rio, se houverem.
	 * 
	 * @param proprietario
	 * @param listAnexoProprietario
	 */
	private void storeAnexoProprietario(Proprietario proprietario, List<AnexoProprietario> listAnexoProprietario) {
		if(listAnexoProprietario == null || listAnexoProprietario.isEmpty()){
			return;
		}
		
		/*
		 * Define a entidade do propriet�rio atualizada para cada anexo.
		 */
		for (AnexoProprietario anexoProprietario : listAnexoProprietario) {
			 anexoProprietario.setProprietario(proprietario);
		}
		
		getProprietarioDAO().store(listAnexoProprietario);
	}
	
	public List<Map<String, Object>> findAnexoProprietarioByIdProprietario(Long idProprietario) {
		return getProprietarioDAO().findAnexoProprietarioByIdProprietario(idProprietario);
    }

	public Integer getRowCountAnexoProprietarioByIdProprietario(Long idProprietario) {
		return getProprietarioDAO().getRowCountAnexoProprietarioByIdProprietario(idProprietario);
	}	
	
	public AnexoProprietario findAnexoProprietarioById(Long idAnexoProprietario) {
		AnexoProprietario anexoProprietario = getProprietarioDAO().findAnexoProprietarioById(idAnexoProprietario);
		if(anexoProprietario != null){
			Hibernate.initialize(anexoProprietario);
		}
		return anexoProprietario;
	}
	
	public void removeByIdsAnexoProprietario(List<Long> ids) {		
		getProprietarioDAO().removeByIdsAnexoProprietario(ids);
	}
	
	/**
	 * Valida o tipo da identifica��o informada para o propriet�rio.
	 * 
	 * @param bean
	 */
	public List<TypedFlatMap> validateTpIdentificacao(TypedFlatMap bean){
		validateCpfCnpj(bean);
		
		return validateIdentificacao(bean);
	}
	
	/**
	 * Valida se o CPF ou CNPJ informado � v�lido.
	 * 
	 * @param bean
	 */
	public void validateCpfCnpj(TypedFlatMap bean){
		String tpIdentificacao = bean.getString("tpIdentificacao");
		
		if(!"CPF".equals(tpIdentificacao) && !"CNPJ".equals(tpIdentificacao)){
			return;
		}

		String nrIdentificacao = String.valueOf(bean.get("nrIdentificacao"));
		
		if(!ValidateUtils.validateCpfOrCnpj(nrIdentificacao)){			
			throwValidateException(tpIdentificacao, FormatUtils.formatCpfCnpj(nrIdentificacao));
		}
	}

	/**
	 * Valida se o PIS informado � v�lido.
	 * 
	 * @param bean
	 */
	public void validatePis(TypedFlatMap bean) {
		String nrPis = String.valueOf(bean.get("nrPis"));
		
		if(!ValidateUtils.validatePis(nrPis)){			
			throwValidateException("PIS", nrPis);
		}
	}

	/**
	 * @param tpIdentificacao
	 * @param nrIdentificacao
	 */
	private void throwValidateException(String tpIdentificacao,
			String nrIdentificacao) {
		StringBuilder invalidMessage = new StringBuilder();
		
		invalidMessage.append(tpIdentificacao);
		invalidMessage.append(" ");
		invalidMessage.append(nrIdentificacao);
		
		throw new BusinessException("LMS-01130", new Object[]{ invalidMessage.toString() });
	}
	
	public void setDescontoRfcService(DescontoRfcService descontoRfcService) {
		this.descontoRfcService = descontoRfcService;
	}

}