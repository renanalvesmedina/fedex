package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ObservacaoICMSPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.dto.DadosInscricaoEstadualColetivaDto;
import com.mercurio.lms.tributos.dto.InscricaoEstadualColetivaFilterDto;
import com.mercurio.lms.tributos.model.AliquotaIcms;
import com.mercurio.lms.tributos.model.EmbasamentoLegalIcms;
import com.mercurio.lms.tributos.model.ExcecaoICMSCliente;
import com.mercurio.lms.tributos.model.ExcecaoICMSIntegrantesContribuintes;
import com.mercurio.lms.tributos.model.ParametroSubstituicaoTrib;
import com.mercurio.lms.tributos.model.TipoTributacaoUf;
import com.mercurio.lms.tributos.model.dao.ExcecaoICMSIntegrantesContribuintesDAO;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;
import com.mercurio.lms.tributos.model.util.InscricaoEstadualColetivaHelper;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * Classe de serviço para CRUD: Não inserir documentação após ou remover a tag
 * do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.tributos.calcularIcmsService"
 */
public class CalcularIcmsService {

	private ConfiguracoesFacade configuracoesFacade;
	private InscricaoEstadualService inscricaoEstadualService;
	private ExcecaoICMSClienteService excecaoICMSClienteService;
	private AliquotaIcmsService aliquotaIcmsService;
	private ParametroSubstituicaoTribService parametroSubstituicaoTribService;
	private UnidadeFederativaService unidadeFederativaService;
	private ClienteService clienteService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private TipoTributacaoUfService tipoTributacaoUfService;
	private ObservacaoICMSPessoaService observacaoICMSPessoaService;
	private ObservacaoICMSService observacaoICMSService;
	private DomainValueService domainValueService;
	private TipoTributacaoIcmsService tipoTributacaoIcmsService;
	private CodigoFiscalOperacaoService codigoFiscalOperacaoService;
	private ParametroGeralService parametroGeralService;
	private InscricaoEstadualColetivaService inscricaoEstadualColetivaService;
	private ExcecaoICMSIntegrantesContribuintesDAO excecaoICMSIntegrantesContribuintesDAO;

	/**
	 * Método que calcular o valor do ICMS.
	 *
	 * @author Mickaël Jalbert
	 * @since 30/05/2006
	 *
	 * @author André Valadas
	 * @since 05/05/2009
	 *
	 * @param CalculoFrete calculoFrete
	 * @return CalculoFrete
	 */
	public CalcularIcmsParam calcularIcms(CalculoFrete calculoFrete) {
		CalcularIcmsParam cip = initializeCalcularIcmsParam(calculoFrete);
		cip.clearEmbLegaisMastersaf();

		/**
		 * Regra 2:
		 * 	- Valida se o remetente é isento de ICMS na exportação internacional.
		 */
		if (validateIsencaoExportacao(cip)) {
			exitCalcularIcms(cip);
			return cip;
		}

		/**
		 * Regra 3
		 *	- Valida se o conhecimneto é do cliente que já pagou o ICMS.
		 */
		if (validateExcecaoIcmsCliente(cip)) {
			exitCalcularIcms(cip);
			return cip;
		}

		/**
		 * Regra 4
		 * 	- Busca o aliquota ICMS se existe.
		 */
		validateAliquotaFrete(cip);

		/**
		 * Regra 5 & 6
		 * 	- Verifica se o conhecimento é uma substituição (o governo paga uma parte ou todo o ICMS)
		 */
		if (validateSubstituicao(cip)) {
			exitCalcularIcms(cip);
			return cip;
		}

		/**
		 * Regra 7
		 * 	- Determina se no estado do remetente se paga o Icms em cima dos pedágios
		 */
		validateUfPedagio(cip);

		/** Calculo dos Valores*/
		calcularValorParcelas(cip);
		calcularValorTotal(cip);
		calcularValorTotalIcms(cip);
		mountObservacao(cip);

		return cip;
	}

	/**
	 * Valida se o remetente é isento de Icms na exportação internacional.
	 *
	 * Regra 2
	 * */
	private Boolean validateIsencaoExportacao(CalcularIcmsParam cip) {
		cip.setDtEmissao(JTDateTimeUtils.getDataAtual());

		Boolean exportacao = isExportacao(cip);

		//Se o pais de origem e de destinatário são diferente (exportação)
		if (exportacao){

			//Buscar o Tipo de Tributação ICMS da UF de Origem
			Long idInscricaoEstadualResponsavel = null;
			if (cip.getBlExisteResponsavel()) {
				idInscricaoEstadualResponsavel = inscricaoEstadualService.findByPessoaIndicadorPadrao(cip.getIdResponsavel(), Boolean.TRUE).getIdInscricaoEstadual();
			}
			TipoTributacaoUf tipoTributacaoUf = tipoTributacaoUfService.findTipoTributacao(
					cip.getCalculoFrete().getTpFrete(),
					idInscricaoEstadualResponsavel,
					cip.getTpSituacaoTributariaResponsavel(),
					cip.getIdUfFilialOrigem(),
					cip.getIdUfDestino(),
					cip.getDtEmissao());

			//Se existe, sair da rotina
			if (tipoTributacaoUf != null) {
				cip.setIdTipoTributacaoIcms(tipoTributacaoUf.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
				cip.setVlDevido(cip.getCalculoFrete().getVlTotal());
				// Retorna OBS ICMS Tipo Tributacao
				cip.addObIcms(cip.getObservacaoICMSTipoTributacao("E"));
				// Incrementa EmbLegalMastersaf Tipo Tributacao
				cip.addAllEmbLegalMastersafByObservacaoICMSTipoTributacao();
				return Boolean.TRUE;
			}

			//Se existe Inscrição estadual
			if (cip.getIdIeRemetente() != null) {
				//Se a uf de origem é a mesma que a do remetente E
				//que a Inscrição estadual está isenta de imposta na exportação E
				//que o tipo de frete for 'CIF' (paga na origem)
				if (cip.getIdUfFilialOrigem().equals(cip.getIdUfRemetente()) &&
						cip.getTipoTributacaoIERemetente() != null &&
						cip.getTipoTributacaoIERemetente().getBlIsencaoExportacoes().equals(Boolean.TRUE) &&
						cip.getTpFrete().equals(ConstantesExpedicao.TP_FRETE_CIF) &&
						cip.getIdIeRemetente().equals(idInscricaoEstadualResponsavel)) {

					cip.setIdTipoTributacaoIcms(((BigDecimal)configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ISENTO")).longValue());
					cip.setVlDevido(cip.getCalculoFrete().getVlTotal());

					// Retorna OBS ICMS Pessoa
					List<String> lstObIcms = cip.getObservacaoICMSPessoaRemetente("E");
					//Se encontrou uma observação especifica para esse cliente
					if (lstObIcms != null && !lstObIcms.isEmpty()) {
						cip.addObIcms(lstObIcms);
						//Incrementa EmbLegalMastersaf Pessoa
						cip.addAllEmbLegalMastersafByObservacaoICMSPessoaRemetente();
					} else {
						throw new BusinessException("LMS-23024");
					}
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	protected boolean isExportacao(CalcularIcmsParam cip) {
		return ( cip.getPaisDestinatario() != null && !cip.getUfFilialOrigem().getPais().getIdPais().equals(cip.getPaisDestinatario().getIdPais()) ) || Boolean.TRUE.equals(cip.getCalculoFrete().getDadosCliente().getBlMercadoriaExportacao());
	}

	/**
	 * Valida se o conhecimneto é do cliente que já pagou o Icms
	 *
	 * Regra 3
	 */
	private Boolean validateExcecaoIcmsCliente(CalcularIcmsParam cip) {
		cip.setDtEmissao(JTDateTimeUtils.getDataAtual());

		ExcecaoICMSCliente excecaoICMSCliente = null;
		Long vlParametroTributoDevido = LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_DEVIDO_OUTRA_UF"));
		boolean isMultioperacao = isTomadorSubcontratacaoMultioperacao(cip.getResponsavel().getPessoa().getNrIdentificacao());

		//Se existe responsável e remetente, pesquisar se existe uma excecao
		if (cip.getBlExisteResponsavel() && cip.getBlExisteRemetente()) {
			excecaoICMSCliente = excecaoICMSClienteService.findExcecaoICMSCliente(
					cip.getResponsavel().getPessoa().getNrIdentificacao(),
					cip.getIeRemetente().getPessoa().getNrIdentificacao(),
					cip.getTpFrete(),
					cip.getIdUfFilialOrigem(),
					cip.getIdUfDestino(),
					cip.getCalculoFrete().getDoctoServico().getFilialByIdFilialOrigem().getIdFilial(),
					cip.getCalculoFrete().getIdNaturezaProduto(),
					vlParametroTributoDevido,
					cip.getDtEmissao());
		}

		//Se existe exceçãoICMS
		if (excecaoICMSCliente != null) {

			// Valida se deve executar a parte de exceção de icms.
			if (!executeExcecaoIcmsCliente(excecaoICMSCliente, cip)) {
				return Boolean.FALSE;
			}

			cip.setIdTipoTributacaoIcms(excecaoICMSCliente.getTipoTributacaoIcms().getIdTipoTributacaoIcms());

			/*Atributo utilizado para verificar execcao de ICMS na regra 12*/
			cip.setIdTipoTributacaoExcecao(excecaoICMSCliente.getTipoTributacaoIcms().getIdTipoTributacaoIcms());

			cip.setVlDevido(cip.getCalculoFrete().getVlTotal());

			// Seta no calculo de frete o blSubcontratação da exceção de icms.
			cip.getCalculoFrete().setBlSubcontratacao(excecaoICMSCliente.getBlSubcontratacao());

			//Se tem subcontratação
			if (excecaoICMSCliente.getBlSubcontratacao().equals(Boolean.TRUE)) {
				if(!isMultioperacao || (cip.getCdCtrcContratante()!= null && isMultioperacao)){
					List<String> lstObservacao = cip.getObservacaoICMSTipoTributacao("S");
					// Incrementa EmbLegalMastersaf Tipo Tributacao
					cip.addAllEmbLegalMastersafByObservacaoICMSTipoTributacao();

					//Adicionar todas a observações antes da última que concatena informações extras
					for (int i = 0; i < lstObservacao.size()-1; i++) {
						cip.addObIcms((String)lstObservacao.get(i));
					}

					// Complementa ultima observação da lista
					mountObservacoes(cip, lstObservacao, excecaoICMSCliente);

					if (!cip.getIdTipoTributacaoIcms().equals(((BigDecimal)configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_NORMAL")).longValue())) {
						return Boolean.TRUE;
					}
				}
			} else {
				// Compara tributo com Parametro Geral
				if(!excecaoICMSCliente.getTipoTributacaoIcms().getIdTipoTributacaoIcms().equals(vlParametroTributoDevido)) {
					if (StringUtils.isBlank(excecaoICMSCliente.getDsRegimeEspecial())) {
						cip.addObIcms(cip.getObservacaoICMSTipoTributacao("T"));
						// Incrementa EmbLegalMastersaf Tipo Tributacao
						cip.addAllEmbLegalMastersafByObservacaoICMSTipoTributacao();
					} else {
						cip.addObIcms(excecaoICMSCliente.getDsRegimeEspecial());
						cip.addEmbLegalMastersaf(excecaoICMSCliente.getCdEmbLegalMastersaf());
					}
					return Boolean.TRUE;

				} else {
					cip.setObservacao(excecaoICMSCliente.getDsRegimeEspecial());
					cip.addEmbLegalMastersaf(excecaoICMSCliente.getCdEmbLegalMastersaf());
				}
			}
		}
		return Boolean.FALSE;
	}

	public boolean isTomadorSubcontratacaoMultioperacao(String nrIdentificacaoResponsavel) {
		String cnpjMultioperacao = (String)(configuracoesFacade.getValorParametro("CNPJ_PERMITE_MULTIOPERACAO"));
		if(cnpjMultioperacao!= null){
			return nrIdentificacaoResponsavel.substring(0,8).equals(cnpjMultioperacao);
		}else{
			return false;
		}
	}

	/**
	 * @author Hector Julian Esnaola Junior
	 * @since 30/10/2007
	 *
	 * @param cip
	 * @param lstObservacao
	 */
	private void mountObservacoes(
			CalcularIcmsParam cip,
			List<String> lstObservacao,
			ExcecaoICMSCliente excecaoIcmsCliente
	) {
		StringBuilder observacaoSb = new StringBuilder()
				.append(lstObservacao.get(lstObservacao.size()-1))
				.append(cip.getResponsavel().getPessoa().getNmFantasia())
				.append(" CNPJ ")
				.append(FormatUtils.formatIdentificacao(cip.getResponsavel().getPessoa()))
				.append(" IE ")
				.append(inscricaoEstadualService
						.findByPessoaIndicadorPadrao(cip.getIdResponsavel(), Boolean.TRUE)
						.getNrInscricaoEstadual());

		// Caso exista cdCtrcContratante, concatena o mesmo.
		if (cip.getCdCtrcContratante() != null) {
			observacaoSb.append(" CT-e ").append(cip.getCdCtrcContratante());
		}
		cip.addObIcms(observacaoSb.toString());

		// Caso dsRegimeEspecial não seja null, cria uma nova observação.
		if (excecaoIcmsCliente.getDsRegimeEspecial() != null) {
			observacaoSb = new StringBuilder().append(excecaoIcmsCliente.getDsRegimeEspecial());
			cip.addObIcms(observacaoSb.toString());
		} else {
			//caso nulo, remove a referencia
			//FIXME: Depurar esse processo para validar
			cip.getCalculoFrete().getEmbLegaisMastersaf().remove(excecaoIcmsCliente.getCdEmbLegalMastersaf());
		}
	}

	/**
	 * Valida se deve executar a parte de exceção de icms.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 30/10/2007
	 *
	 * @param excecaoICMSCliente
	 * @param cip
	 * @return
	 *
	 */
	public Boolean executeExcecaoIcmsCliente(ExcecaoICMSCliente excecaoICMSCliente, CalcularIcmsParam cip) {

		// Caso o Devedor do frete não seja pessoa juridica, não validar as exceções de icms.
		if (!"CNPJ".equalsIgnoreCase(cip.getResponsavel()
				.getPessoa().getTpIdentificacao().getValue())) {
			return Boolean.FALSE;
		}

		// Caso exista remeteExcecaoIcmsCli e, o remetente do frete não seja pessoa juridica, 
		// não validar as exceções de icms.
		if (excecaoICMSCliente.getRemetentesExcecaoICMSCli() != null &&
				!excecaoICMSCliente.getRemetentesExcecaoICMSCli().isEmpty()) {
			if (cip.getIeRemetente().getPessoa().getTpIdentificacao() != null &&
					!"CNPJ".equalsIgnoreCase(cip.getIeRemetente().getPessoa().getTpIdentificacao().getValue())) {
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * Verifica Tipo da UF de Origem
	 * 	- Compara tributo com Parametro Geral
	 */
	private Long getIdUFOrigemAliquota(CalcularIcmsParam cip) {
		if(cip.getIdTipoTributacaoIcms() != null
				&& !cip.getIdTipoTributacaoIcms().equals(LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_DEVIDO_OUTRA_UF")))) {
			return cip.getIdUfOrigem();
			// Caso esteja vindo da rotina de calculo de cotação.
		} else if (cip.getCalculoFrete().getBlCotacao()
				&& cip.getIdUfFilialRemetente() != null) {
			return cip.getIdUfFilialRemetente();
		} else {
			return cip.getIdUfFilialOrigem();
		}
	}

	/**
	 * Busca o aliquota icms se existe
	 *
	 * Regra 4
	 * */
	private void validateAliquotaFrete(CalcularIcmsParam cip) {
		String tpSituacaoTributariaDestinatario = null;
		Long idUfRemetente = cip.getIdUfRemetente();
		Long idUfDestinatario = cip.getIdUfDestinatario();

		//Verifica Tipo da UF de Origem
		Long idUfOrigemAliquota = this.getIdUFOrigemAliquota(cip);

		//Se foi informado um remetente e se o tipo de pessoa é 'física' e que a uf do remetente é diferente 
		//da uf de origem, assumir 'Não contribuinte' 

		if (    !idUfRemetente.equals(idUfOrigemAliquota)
				|| cip.getIdTipoTributacaoIcms() != null
				&& cip.getIdTipoTributacaoIcms().equals(LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_DEVIDO_OUTRA_UF")))
		) {
			cip.setTpSituacaoTributariaRemetente("NC");
		} else {
			// Valida Tipo Tributação da IE Rementente
			String tpSituacaoTributariaRemetente = cip.getTpSituacaoTributariaRemetente();
			if (StringUtils.isBlank(tpSituacaoTributariaRemetente)) {
				throw new BusinessException("LMS-23028", new Object[]{
						configuracoesFacade.getMensagem("remetente")});
			}
			cip.setTpSituacaoTributariaRemetente(tpSituacaoTributariaRemetente);
		}

		//Se foi informado um destinatario e se o tipo de pessoa é 'física' e que a uf do destinatário é diferente
		//da uf de destino (uma outra empresa termina o transporte ou uma coisa desse tipo), assumir 'Não contribuinte'

		/*Solicitação feita pelo Joelson em 01/07/2009*/
		if(cip.getDestinatario() != null){
			idUfDestinatario = cip.getDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
		}

		if (!cip.getIdUfDestino().equals( idUfDestinatario )) {
			tpSituacaoTributariaDestinatario = "NC";
		} else {
			tpSituacaoTributariaDestinatario = cip.getTpSituacaoTributariaDestinatario();
			if (StringUtils.isBlank(tpSituacaoTributariaDestinatario)) {
				throw new BusinessException("LMS-23028", new Object[]{
						configuracoesFacade.getMensagem("destinatario")});
			}
		}

		if(isentaExcecaoICMS(cip, idUfDestinatario, idUfOrigemAliquota)){
			return;
		}

		/**
		 * LMS-3176
		 *
		 * Para frete interestadual, verificar se o cliente remetente possuir 
		 * uma IE substituta na UF de destino, então considerar o destinatário 
		 * como contribuinte do ICMS (caso Natura)
		 *
		 * */

		if(idUfOrigemAliquota != null && cip.getIdUfDestino() != null
				&& idUfOrigemAliquota != cip.getIdUfDestino()){
			if(cip.getIdRemetente()!= null) {
				InscricaoEstadualColetivaFilterDto filterDto = InscricaoEstadualColetivaHelper.buildFilter(cip.getIdRemetente()
						, cip.getDtEmissao()
						, cip.getIdUfDestino()
						, SessionUtils.getPaisSessao().getIdPais());

				DadosInscricaoEstadualColetivaDto dadosDto = inscricaoEstadualColetivaService.findDadosIEColetativaByFilter(filterDto);

				if (dadosDto != null) {
					tpSituacaoTributariaDestinatario = "CO";
					String msgObservacaoDocto = configuracoesFacade.getMensagem("obsIcmsDoctoServicoIEColetiva");
					InscricaoEstadualColetivaHelper.buildObservacao(dadosDto, cip, msgObservacaoDocto);
				}
			}
		}

		// Valida pelo DEVEDOR DO FRETE
		Long idIEResponsavel = null;
		String tpSituacaoTributariaDevedor = null;
		if(cip.getBlExisteResponsavel()) {
			InscricaoEstadual ieResponsavel = inscricaoEstadualService.findByPessoaIndicadorPadrao(cip.getIdResponsavel(), Boolean.TRUE);
			if(ieResponsavel == null){
				throw new BusinessException("LMS-01193", new Object[]{ cip.getResponsavel().getPessoa().getNrIdentificacao() });
			}
			idIEResponsavel = ieResponsavel.getIdInscricaoEstadual();
		}
		if(ConstantesExpedicao.TP_FRETE_CIF.equals(cip.getTpFrete())
				&& LongUtils.hasValue(idIEResponsavel)
				&& CompareUtils.neNull(cip.getIdRemetente(), cip.getIdResponsavel())) {

			// Verifica dados do Devedor/responsavel pelo frete
			if (!idUfOrigemAliquota.equals(cip.getResponsavel().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa())) {
				tpSituacaoTributariaDevedor = "NC"; // Não Contribuinte
			} else {
				tpSituacaoTributariaDevedor = cip.getTpSituacaoTributariaResponsavel();
				if (StringUtils.isBlank(tpSituacaoTributariaDevedor)) {
					throw new BusinessException("LMS-23028", new Object[]{
							configuracoesFacade.getMensagem("responsavel")});
				}
			}

			/*Seta o tomador*/
			cip.setTpSituacaoTributariaTomador(tpSituacaoTributariaDevedor);

			/**
			 * Devedor/Destinatário
			 */
			/** Verifica Aliquota para UF Origem/UF Destino */
			if (verifyAliquotaUFOrigemUFDestino(idUfOrigemAliquota, cip, tpSituacaoTributariaDevedor, tpSituacaoTributariaDestinatario, "LMS-23031")) {
				return;
			}
			/** Verifica Aliquota para UF Origem/Regiao Geografica Destino */
			if (verifyAliquotaUFOrigemRGDestino(idUfOrigemAliquota, cip, tpSituacaoTributariaDevedor, tpSituacaoTributariaDestinatario, "LMS-23035")) {
				return;
			}
			/** Verifica Aliquota para UF Origem/UF Destino ou Regiao Geografica Destino */
			if (verifyAliquotaUFOrigemUFDestinoRGDestino(idUfOrigemAliquota, cip, tpSituacaoTributariaDevedor, tpSituacaoTributariaDestinatario, "LMS-23036")) {
				return;
			}
		} else if(ConstantesExpedicao.TP_FRETE_FOB.equals(cip.getTpFrete())
				&& LongUtils.hasValue(idIEResponsavel)
				&& CompareUtils.neNull(cip.getIdDestinatario(), cip.getIdResponsavel())) {

			// Verifica dados do Devedor/responsavel pelo frete
			if (!cip.getIdUfDestino().equals(cip.getResponsavel().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa())) {
				tpSituacaoTributariaDevedor = "NC"; // Não Contribuinte
			} else {
				tpSituacaoTributariaDevedor = cip.getTpSituacaoTributariaResponsavel();
				if (StringUtils.isBlank(tpSituacaoTributariaDevedor)) {
					throw new BusinessException("LMS-23028", new Object[]{
							configuracoesFacade.getMensagem("responsavel")});
				}
			}


			BigDecimal param = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("ID_TIPO_TRIBUTACAO_ISENTO", false);
			Long idTipoTributacaoIsento = null;
			if (param !=null){
				idTipoTributacaoIsento = param.longValue();
			}


			/*Seta o tomador*/
			cip.setTpSituacaoTributariaTomador(tpSituacaoTributariaDevedor);

			/**
			 * Remetente/Devedor
			 */
			/** Verifica Aliquota para UF Origem/UF Destino */
			if (verifyAliquotaUFOrigemUFDestino(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDevedor, "LMS-23032")) {
				if (cip.getIdTipoTributacaoIcms() != null && !cip.getIdTipoTributacaoIcms().equals(idTipoTributacaoIsento)){
					AliquotaIcms aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
							idUfOrigemAliquota,
							cip.getIdUfDestino(),
							cip.getTpSituacaoTributariaRemetente(),
							tpSituacaoTributariaDestinatario,
							cip.getTpFrete(),
							cip.getDtEmissao());
					if (aliquotaIcms != null){
						if (aliquotaIcms.getPcAliquota() != null && BigDecimalUtils.gtZero(aliquotaIcms.getPcAliquota())){
							cip.setPcAliquota(aliquotaIcms.getPcAliquota());
							cip.setPcEmbute(aliquotaIcms.getPcEmbute());

						}
					}else{
						throwExceptionMessageUnidadeFederativa(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDestinatario, "LMS-23015");
					}
				}

				return;
			}
			/** Verifica Aliquota para UF Origem/Regiao Geografica Destino */
			if (verifyAliquotaUFOrigemRGDestino(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDevedor, "LMS-23037")) {
				if (cip.getIdTipoTributacaoIcms() != null && !cip.getIdTipoTributacaoIcms().equals(idTipoTributacaoIsento)){
					Long idPais = this.obterPaisTransacao(cip).getIdPais();
					AliquotaIcms aliquotaIcms = aliquotaIcmsService.findByRegiaoGeograficaDestinoVigente(
							idUfOrigemAliquota, cip.getIdUfDestino(), idPais, cip.getTpSituacaoTributariaRemetente(),
							tpSituacaoTributariaDestinatario, cip.getTpFrete(), cip.getDtEmissao());
					if (aliquotaIcms != null && BigDecimalUtils.gtZero(aliquotaIcms.getPcAliquota())){
						cip.setPcAliquota(aliquotaIcms.getPcAliquota());
						cip.setPcEmbute(aliquotaIcms.getPcEmbute());
					}else{
						throwExceptionMessageRegiaoGeografica(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDestinatario, "LMS-23039");
					}
				}
				return;
			}
			/** Verifica Aliquota para UF Origem/UF Destino ou Regiao Geografica Destino */
			if (verifyAliquotaUFOrigemUFDestinoRGDestino(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDevedor, "LMS-23038")) {
				if (cip.getIdTipoTributacaoIcms() != null && !cip.getIdTipoTributacaoIcms().equals(idTipoTributacaoIsento)){
					AliquotaIcms aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
							idUfOrigemAliquota,
							cip.getTpSituacaoTributariaRemetente(),
							tpSituacaoTributariaDestinatario,
							cip.getTpFrete(),
							cip.getDtEmissao());
					if(aliquotaIcms != null && BigDecimalUtils.gtZero(aliquotaIcms.getPcAliquota())) {
						cip.setPcAliquota(aliquotaIcms.getPcAliquota());
						cip.setPcEmbute(aliquotaIcms.pcEmbuteCalculado());
					}else{
						throwBusinessExceptionUnidadeFederativaRegiaoGeografica(
								idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(),
								tpSituacaoTributariaDestinatario, "LMS-23040");
					}
				}
				return;
			}
		} else {

			/*Seta o tomador do frete dependendo do tipo de frete*/
			if(ConstantesExpedicao.TP_FRETE_CIF.equals(cip.getTpFrete())){
				cip.setTpSituacaoTributariaTomador(cip.getTpSituacaoTributariaRemetente());
			}else{
				cip.setTpSituacaoTributariaTomador(tpSituacaoTributariaDestinatario);
			}

			/**
			 * Remetente/Destinatário
			 */
			/** Verifica Aliquota para UF Origem/UF Destino */
			if (verifyAliquotaUFOrigemUFDestino(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDestinatario, "LMS-23015")) {
				return;
			}
			/** Verifica Aliquota para UF Origem/Regiao Geografica Destino */
			if (verifyAliquotaUFOrigemRGDestino(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDestinatario, "LMS-23039")) {
				return;
			}
			/** Verifica Aliquota para UF Origem/UF Destino ou Regiao Geografica Destino */
			if (verifyAliquotaUFOrigemUFDestinoRGDestino(idUfOrigemAliquota, cip, cip.getTpSituacaoTributariaRemetente(), tpSituacaoTributariaDestinatario, "LMS-23040")) {
				return;
			}
		}
	}

	private Boolean isentaExcecaoICMS(CalcularIcmsParam cip, Long idUfDestinatario, Long idUfOrigemAliquota) {
		List<ExcecaoICMSIntegrantesContribuintes> excecoes = excecaoICMSIntegrantesContribuintesDAO.findExcecaoICMSContribuintes(idUfOrigemAliquota, idUfDestinatario, cip.getDtEmissao());
		String parametoGeralSituacaoGeral =  (String) parametroGeralService.findConteudoByNomeParametro("TP_SIT_TRIBUT_ACEITA_ISENTO", false);
		Long idTributacaoIsento = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("ID_TIPO_TRIBUTACAO_ISENTO",false)).longValue();

		ExcecaoICMSIntegrantesContribuintes excessaoIsenta = findExcecaoICMSemExcecoes(cip,excecoes,parametoGeralSituacaoGeral);

		if(excessaoIsenta == null){
			return false;
		}

		setValoresCIPparaIsentarICMS(cip, idTributacaoIsento,
				excessaoIsenta);
		return true;

	}

	private void setValoresCIPparaIsentarICMS(CalcularIcmsParam cip,
											  Long idTributacaoIsento,
											  ExcecaoICMSIntegrantesContribuintes excessaoIsenta) {
		cip.setIdTipoTributacaoIcms(idTributacaoIsento);
		cip.addObIcms( excessaoIsenta.getEmbasamentoLegalIcms().getDsEmbLegalResumido()  );
		cip.addEmbLegalMastersaf(excessaoIsenta.getEmbasamentoLegalIcms().getCdEmbLegalMasterSaf());
		cip.setVlDevido(cip.getCalculoFrete().getVlTotal());
		cip.setPcEmbute(BigDecimalUtils.ZERO);
	}

	ExcecaoICMSIntegrantesContribuintes findExcecaoICMSemExcecoes(CalcularIcmsParam cip,
																  List<ExcecaoICMSIntegrantesContribuintes> excecoes, String parametoGeralSituacaoGeral) {

		if ( excecoes != null ){
			for(ExcecaoICMSIntegrantesContribuintes excecao : excecoes ){
				String tpSituacaoTributariaIntegrante = "";
				if ( "R".equals(excecao.getTpIntegranteFrete().getValue()) ){
					tpSituacaoTributariaIntegrante  = cip.getTipoTributacaoIERemetente().getTpSituacaoTributaria().getValue();
				}else if ( "D".equals(excecao.getTpIntegranteFrete().getValue()) ){
					tpSituacaoTributariaIntegrante  = cip.getTipoTributacaoIEDestinatario().getTpSituacaoTributaria().getValue();
				}
				// Verifica se Tipo do Frete do Integrante Isento for igual ao parâmetro informado tp_frete  (“FOB” ou “CIF”) ou Nulo e
				// se o tipo de Situação Tributária do Integrante for diferente das situações cadastradas no Parâmetro Geral “TP_SIT_TRIBUT_ACEITA_ISENTO”
				// então popula os campos para formar a informação de Observação de Isento na emissão do documento e sai da rotina não calculando o ICMS.
				if ( (excecao.getTpFrete() == null || ConstantesExpedicao.TP_FRETE_FOB.equals(excecao.getTpFrete().getValue()) || ConstantesExpedicao.TP_FRETE_CIF.equals(excecao.getTpFrete().getValue()))
						&& !checkParametroGeralContainsSituacaoTributariaIntegrante(tpSituacaoTributariaIntegrante, parametoGeralSituacaoGeral)){
					return  excecao;
				}

			}
		}
		return null;
	}

	protected boolean checkParametroGeralContainsSituacaoTributariaIntegrante(String tpSituacaoTributariaIntegrante, String parametroGeralSituacaoGeral){
		String[] parametros = splitParametroGeral(parametroGeralSituacaoGeral);
		for (String parametro : parametros) {
			if(tpSituacaoTributariaIntegrante.equals(parametro)){
				return true;
			}
		}
		return false;
	}

	private String[] splitParametroGeral(String conteudoParametroGeral){
		return conteudoParametroGeral != null ? conteudoParametroGeral.split(";") : new String[0];
	}

	/**
	 * Obtem a situacão tributaria do recebedor
	 * @param  cip
	 * @return String
	 */
	private String obterSituacaoTributariaRecebedor(CalcularIcmsParam cip){
		String tpSituacaoTributaria = null;
		if(!cip.getIdUfDestino().equals(cip.getRecebedor().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa()) ){
			tpSituacaoTributaria = "NC"; // Não Contribuinte
		}else{
			tpSituacaoTributaria = cip.getTpSituacaoTributariaRecebedor();
			if (StringUtils.isBlank(tpSituacaoTributaria)) {
				throw new BusinessException("LMS-23028", new Object[]{configuracoesFacade.getMensagem("recebedor")});
			}
		}
		return tpSituacaoTributaria;
	}

	/**
	 * Verifica se existe Aliquota para UF Origem/UF Destino
	 *
	 * @param idUfOrigemAliquota
	 * @param cip
	 * @param tpSituacaoTributariaRementente
	 * @param tpSituacaoTributariaDestinatario
	 * @param errorKey
	 * @return
	 */
	private Boolean verifyAliquotaUFOrigemUFDestino(Long idUfOrigemAliquota, CalcularIcmsParam cip, String tpSituacaoTributariaRementente, String tpSituacaoTributariaDestinatario, String errorKey) {
		if (aliquotaIcmsService.findExistsAliquotaIcms(idUfOrigemAliquota, cip.getIdUfDestino(), cip.getDtEmissao())) {
			// Busca Por Situação Tributária
			AliquotaIcms aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
					idUfOrigemAliquota,
					cip.getIdUfDestino(),
					tpSituacaoTributariaRementente,
					tpSituacaoTributariaDestinatario,
					cip.getTpFrete(),
					cip.getDtEmissao());
			if(aliquotaIcms != null) {
				cip.setIdTipoTributacaoIcms(aliquotaIcms.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
				cip.setPcAliquota(aliquotaIcms.getPcAliquota());
				cip.setPcEmbute(aliquotaIcms.pcEmbuteCalculado());

				EmbasamentoLegalIcms embasamento = aliquotaIcms.getEmbasamento();
				if(embasamento != null) {
					cip.addObIcms(embasamento.getDsEmbLegalResumido());
					cip.addEmbLegalMastersaf(embasamento.getCdEmbLegalMasterSaf());
				}
				return Boolean.TRUE;
			} else {
				throwExceptionMessageUnidadeFederativa(idUfOrigemAliquota, cip,
						tpSituacaoTributariaRementente,
						tpSituacaoTributariaDestinatario, errorKey);
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * @param idUfOrigemAliquota
	 * @param cip
	 * @param tpSituacaoTributariaRementente
	 * @param tpSituacaoTributariaDestinatario
	 * @param errorKey
	 */
	private void throwExceptionMessageUnidadeFederativa(Long idUfOrigemAliquota,
														CalcularIcmsParam cip, String tpSituacaoTributariaRementente,
														String tpSituacaoTributariaDestinatario, String errorKey) {
		// Monta Exceção
		String dsTributacaoDevedor = "";
		if (StringUtils.isNotBlank(tpSituacaoTributariaRementente)) {
			dsTributacaoDevedor = domainValueService.findDomainValueByValue("DM_SITUACAO_TRIBUTARIA", tpSituacaoTributariaRementente).getDescription().getValue();
		}
		String dsTributacaoDestinatario = "";
		if (StringUtils.isNotBlank(cip.getTpSituacaoTributariaDestinatario())) {
			dsTributacaoDestinatario = domainValueService.findDomainValueByValue("DM_SITUACAO_TRIBUTARIA", tpSituacaoTributariaDestinatario).getDescription().getValue();
		}

		throw new BusinessException(errorKey, new Object[]{
				unidadeFederativaService.findById(idUfOrigemAliquota).getSgUnidadeFederativa(),
				cip.getUfDestino().getSgUnidadeFederativa(),
				dsTributacaoDevedor,
				dsTributacaoDestinatario,
				domainValueService.findDomainValueByValue("DM_TIPO_FRETE", cip.getTpFrete()).getDescription()});
	}

	/**
	 * Verifica se existe Aliquota para UF Origem/RG Destino
	 *
	 * @param idUfOrigemAliquota
	 * @param cip
	 * @param tpSituacaoTributariaRementente
	 * @param tpSituacaoTributariaDestinatario
	 * @param errorKey
	 * @return
	 */
	private Boolean verifyAliquotaUFOrigemRGDestino(Long idUfOrigemAliquota, CalcularIcmsParam cip, String tpSituacaoTributariaRementente, String tpSituacaoTributariaDestinatario, String errorKey) {

		Long idPais = this.obterPaisTransacao(cip).getIdPais();

		// Busca ALIQUOTA para a UF Origem/Regiao Geografica Destino
		AliquotaIcms aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
				idUfOrigemAliquota,
				cip.getIdUfDestino(),
				idPais,
				cip.getDtEmissao());
		if(aliquotaIcms != null) {
			// Busca Por Situação Tributária
			aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
					idUfOrigemAliquota,
					cip.getIdUfDestino(),
					idPais,
					tpSituacaoTributariaRementente,
					tpSituacaoTributariaDestinatario,
					cip.getTpFrete(),
					cip.getDtEmissao());
			if(aliquotaIcms != null) {
				cip.setIdTipoTributacaoIcms(aliquotaIcms.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
				cip.setPcAliquota(aliquotaIcms.getPcAliquota());
				cip.setPcEmbute(aliquotaIcms.pcEmbuteCalculado());

				EmbasamentoLegalIcms embasamento = aliquotaIcms.getEmbasamento();
				if(embasamento != null) {
					cip.addObIcms(embasamento.getDsEmbLegalResumido());
					cip.addEmbLegalMastersaf(embasamento.getCdEmbLegalMasterSaf());
				}
				return Boolean.TRUE;
			} else {
				throwExceptionMessageRegiaoGeografica(idUfOrigemAliquota, cip,
						tpSituacaoTributariaRementente,
						tpSituacaoTributariaDestinatario, errorKey);
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * @param idUfOrigemAliquota
	 * @param cip
	 * @param tpSituacaoTributariaRementente
	 * @param tpSituacaoTributariaDestinatario
	 * @param errorKey
	 */
	private void throwExceptionMessageRegiaoGeografica(Long idUfOrigemAliquota,
													   CalcularIcmsParam cip, String tpSituacaoTributariaRementente,
													   String tpSituacaoTributariaDestinatario, String errorKey) {
		// Monta Exceção
		String dsTributacaoDevedor = "";
		if (StringUtils.isNotBlank(tpSituacaoTributariaRementente)) {
			dsTributacaoDevedor = domainValueService.findDomainValueByValue("DM_SITUACAO_TRIBUTARIA", tpSituacaoTributariaRementente).getDescription().getValue();
		}
		String dsTributacaoDestinatario = "";
		if (StringUtils.isNotBlank(cip.getTpSituacaoTributariaDestinatario())) {
			dsTributacaoDestinatario = domainValueService.findDomainValueByValue("DM_SITUACAO_TRIBUTARIA", tpSituacaoTributariaDestinatario).getDescription().getValue();
		}

		throw new BusinessException(errorKey, new Object[]{
				unidadeFederativaService.findById(idUfOrigemAliquota).getSgUnidadeFederativa(),
				unidadeFederativaService.findById(cip.getIdUfDestino()).getRegiaoGeografica().getDsRegiaoGeografica().getValue(),
				dsTributacaoDevedor,
				dsTributacaoDestinatario,
				domainValueService.findDomainValueByValue("DM_TIPO_FRETE", cip.getTpFrete()).getDescription()});
	}
	/**
	 * Verifica se existe Aliquota para UF Origem/UF Destino ou RG Destino
	 *
	 * @param idUfOrigemAliquota
	 * @param cip
	 * @param tpSituacaoTributariaDestinatario
	 * @param tpSituacaoTributariaRementente
	 * @param errorKey
	 * @return
	 */
	private Boolean verifyAliquotaUFOrigemUFDestinoRGDestino(Long idUfOrigemAliquota, CalcularIcmsParam cip, String tpSituacaoTributariaRementente, String tpSituacaoTributariaDestinatario, String errorKey) {
		// Busca ALIQUOTA para a UF Origem/UF Destino ou REGIAO GEOGRAFICA DESTINO
		AliquotaIcms aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
				idUfOrigemAliquota,
				cip.getDtEmissao());
		if(aliquotaIcms != null) {
			// Busca Por Situação Tributária
			aliquotaIcms = aliquotaIcmsService.findAliquotaIcms(
					idUfOrigemAliquota,
					tpSituacaoTributariaRementente,
					tpSituacaoTributariaDestinatario,
					cip.getTpFrete(),
					cip.getDtEmissao());
			if(aliquotaIcms != null) {
				cip.setIdTipoTributacaoIcms(aliquotaIcms.getTipoTributacaoIcms().getIdTipoTributacaoIcms());
				cip.setPcAliquota(aliquotaIcms.getPcAliquota());
				cip.setPcEmbute(aliquotaIcms.pcEmbuteCalculado());

				EmbasamentoLegalIcms embasamento = aliquotaIcms.getEmbasamento();
				if(embasamento != null) {
					cip.addObIcms(embasamento.getDsEmbLegalResumido());
					cip.addEmbLegalMastersaf(embasamento.getCdEmbLegalMasterSaf());
				}
				return Boolean.TRUE;
			}
		}

		throwBusinessExceptionUnidadeFederativaRegiaoGeografica(
				idUfOrigemAliquota, cip, tpSituacaoTributariaRementente,
				tpSituacaoTributariaDestinatario, errorKey);

		return Boolean.FALSE;
	}

	/**
	 * @param idUfOrigemAliquota
	 * @param cip
	 * @param tpSituacaoTributariaRementente
	 * @param tpSituacaoTributariaDestinatario
	 * @param errorKey
	 */
	private void throwBusinessExceptionUnidadeFederativaRegiaoGeografica(
			Long idUfOrigemAliquota, CalcularIcmsParam cip,
			String tpSituacaoTributariaRementente,
			String tpSituacaoTributariaDestinatario, String errorKey) {
		//Caso nao encontre AliquotaICMS lança Exceção
		String dsTributacaoDevedor = "";
		if (StringUtils.isNotBlank(tpSituacaoTributariaRementente)) {
			dsTributacaoDevedor = domainValueService.findDomainValueByValue("DM_SITUACAO_TRIBUTARIA", tpSituacaoTributariaRementente).getDescription().getValue();
		}
		String dsTributacaoDestinatario = "";
		if (StringUtils.isNotBlank(cip.getTpSituacaoTributariaDestinatario())) {
			dsTributacaoDestinatario = domainValueService.findDomainValueByValue("DM_SITUACAO_TRIBUTARIA", tpSituacaoTributariaDestinatario).getDescription().getValue();
		}

		throw new BusinessException(errorKey, new Object[]{
				unidadeFederativaService.findById(idUfOrigemAliquota).getSgUnidadeFederativa(),
				dsTributacaoDevedor,
				dsTributacaoDestinatario,
				domainValueService.findDomainValueByValue("DM_TIPO_FRETE", cip.getTpFrete()).getDescription()});
	}


	/**
	 * Obtem o pais da transação, idependente se for pelo LMS ou
	 * por acesso externo
	 *
	 * @param  cip
	 * @return Pais
	 */
	private Pais obterPaisTransacao(CalcularIcmsParam cip){

		if(cip.getCalculoFrete().getDadosCliente().getFilialTransacao() == null){
			return SessionUtils.getPaisSessao();
		}else{
			Municipio municipio = cip.getCalculoFrete().getDadosCliente()
					.getFilialTransacao().getPessoa().getEnderecoPessoa().getMunicipio();

			return municipio.getUnidadeFederativa().getPais();
		}
	}



	/**
	 * Verifica se o conhecimento é uma substituição (o governo paga uma parte ou todo o ICMS)
	 *
	 * Regra 5 & 6
	 * */
	private Boolean validateSubstituicao(CalcularIcmsParam cip) {
		cip.setBlAceitaSubstituicao(Boolean.TRUE);
		cip.setBlEmbuteIcmsParcelasIE(Boolean.FALSE);
		Long idUFOrigemAliquota = this.getIdUFOrigemAliquota(cip);

		//Se o tipo de conhecimento for 'Complemento ICMS', não pode calcular o ICMS porque o valor
		//já é o resto do ICMS do documento que foi mal calculado
		BigDecimal valorParametroTipoTributacaoNormal = (BigDecimal)configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_NORMAL");
		if (cip.getTpConhecimento() != null && cip.getTpConhecimento().equals("CI")) {
			cip.setIdTipoTributacaoIcms(LongUtils.getLong(valorParametroTipoTributacaoNormal));
			cip.setVlBase(BigDecimal.ZERO);
			cip.setVlRetencaoST(BigDecimal.ZERO);
			cip.setPcRetencaoST(BigDecimal.ZERO);

			/*Adicionado em 23/04/2010 - Solicitado por Joelson*/
			cip.clearObIcms();
			cip.clearEmbLegaisMastersaf();

			return Boolean.TRUE;
		}

		//Se o porcentagem de aliquota é maior que 0
		BigDecimal valorParametroTipoTributacaoSt = (BigDecimal)configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ST");
		if (cip.getPcAliquota().compareTo(BigDecimal.ZERO) > 0) {

			//Se tem tipoTributacaoIcms e que o tipo de frete = 'CIF' e
			//que a uf de origem é igual a uf do ie remetente
			if (cip.getTipoTributacaoIERemetente() != null &&
					cip.getTipoTributacaoIERemetente().getTipoTributacaoIcms() != null &&
					cip.getTpFrete().equals(ConstantesExpedicao.TP_FRETE_CIF) &&
					idUFOrigemAliquota.equals(cip.getIeRemetente().getUnidadeFederativa().getIdUnidadeFederativa()) &&
					cip.getRemetente().getIdCliente().equals(cip.getResponsavel().getIdCliente())) {

				cip.setIdTipoTributacaoIcms(cip.getTipoTributacaoIERemetente().getTipoTributacaoIcms().getIdTipoTributacaoIcms());

				//Se o tipoTributacaoIcms é diferente ao parametro geral ST e diferente de NORMAL.
				if (!cip.getTipoTributacaoIERemetente().getTipoTributacaoIcms().getIdTipoTributacaoIcms()
						.equals((valorParametroTipoTributacaoSt).longValue()) &&
						!cip.getTipoTributacaoIERemetente().getTipoTributacaoIcms().getIdTipoTributacaoIcms()
								.equals(LongUtils.getLong(valorParametroTipoTributacaoNormal))) {

					cip.setVlDevido(cip.getCalculoFrete().getVlTotal());

					List<String> lstObIcms = cip.getObservacaoICMSPessoaRemetente("T");

					//Se encontrou uma observação especifica para esse cliente
					if (lstObIcms != null && !lstObIcms.isEmpty()) {
						cip.addObIcms(lstObIcms);
						//Incrementa EmbLegalMastersaf Pessoa
						cip.addAllEmbLegalMastersafByObservacaoICMSPessoaRemetente();
					} else {
						throw new BusinessException("LMS-23041", new Object[]{
								FormatUtils.formatIdentificacao(cip.getIeRemetente().getPessoa()),
								cip.getIeRemetente().getNrInscricaoEstadual()});
					}
					return Boolean.TRUE;
				} else {
					cip.setBlAceitaSubstituicao(cip.getTipoTributacaoIERemetente().getBlAceitaSubstituicao());
					cip.setBlEmbuteIcmsParcelasIE(Boolean.TRUE);
				}
			}

			// Caso esteja vindo da rotina de calculo de cotação.
			if (cip.getCalculoFrete().getBlCotacao()) {
				// Se a UF da filial que atende o remetente (CASO QUE É UMA COTAÇÃO A VARIAVEL cip.getIdUfFilialRemetente() != NULO)
				// é diferente da UF do remetente
				if (cip.getIdUfFilialRemetente() != null
						&& !cip.getIdUfFilialRemetente().equals(cip.getIdUfOrigem())
						&& CompareUtils.eqNull(cip.getIdTipoTributacaoIcms(), LongUtils.getLong(valorParametroTipoTributacaoSt))) {

					cip.setIdTipoTributacaoIcms(LongUtils.getLong(valorParametroTipoTributacaoNormal));

					/*Adicionado em 23/04/2010 - Solicitado por Joelson*/
					cip.clearObIcms();
					cip.clearEmbLegaisMastersaf();

				}
				// Se a UF da filial DE ORIGEM é diferente da UF do remetente (tem uma outra empresa que faz uma parte do transporte ou a filial
				// atende um municipio de um outro estado)
			} else if (!cip.getIdUfFilialOrigem().equals(cip.getIdUfRemetente())
					&& CompareUtils.eqNull(cip.getIdTipoTributacaoIcms(), LongUtils.getLong(valorParametroTipoTributacaoSt))) {

				cip.setIdTipoTributacaoIcms(LongUtils.getLong(valorParametroTipoTributacaoNormal));

				/*Adicionado em 23/04/2010 - Solicitado por Joelson*/
				cip.clearObIcms();
				cip.clearEmbLegaisMastersaf();
			}
		}
		//Se o tipoTributacaoIcms é igual ao parametro geral
		if (cip.getIdTipoTributacaoIcms() != null
				&& CompareUtils.eqNull(cip.getIdTipoTributacaoIcms(), LongUtils.getLong(valorParametroTipoTributacaoSt))
		) {
			ParametroSubstituicaoTrib parametroSubstituicaoTrib = parametroSubstituicaoTribService.findVigenteByUf(idUFOrigemAliquota, cip.getDtEmissao());
			if (parametroSubstituicaoTrib == null) {
				throw new BusinessException("LMS-23016");
			}

			String tpCliente = ConstantesVendas.CLIENTE_EVENTUAL;
			if (parametroSubstituicaoTrib.getBlAplicarClientesEspeciais().equals(Boolean.TRUE) && cip.getBlExisteResponsavel()) {
				tpCliente = cip.getResponsavel().getTpCliente().getValue();
			}

			//Se a situação é de aplicar nos clientes especiais e que o cliente é especial OU
			//que ao tipo de tributação é de substituição e que não tem parametros especiais
			if ( ( parametroSubstituicaoTrib.getBlAplicarClientesEspeciais().equals(Boolean.TRUE) &&  (ConstantesVendas.CLIENTE_ESPECIAL.equals(tpCliente) || ConstantesVendas.CLIENTE_FILIAL.equals(tpCliente)) )
					|| parametroSubstituicaoTrib.getBlAplicarClientesEspeciais().equals(Boolean.FALSE)) {

				if (cip.getBlEmbuteIcmsParcelasIE()) {
					cip.setBlEmbuteIcmsParcelas(Boolean.TRUE);
				} else {
					cip.setBlEmbuteIcmsParcelas(parametroSubstituicaoTrib.getBlEmbuteICMSParcelas());
				}

				cip.setBlImpDadosCalcCtrc(parametroSubstituicaoTrib.getBlImpDadosCalcCTRC());
				cip.setBlImprimeMemoCalcCte(parametroSubstituicaoTrib.getBlImprimeMemoCalcCte());
				cip.setPcRetencaoST(parametroSubstituicaoTrib.getPcRetencao());
				//Senão o tipo de tributação vira normal
			} else {
				cip.setIdTipoTributacaoIcms(LongUtils.getLong(valorParametroTipoTributacaoNormal));
				cip.setBlEmbuteIcmsParcelas(Boolean.TRUE);
				cip.setBlImpDadosCalcCtrc(Boolean.TRUE);
				cip.setBlImprimeMemoCalcCte(Boolean.FALSE);

				/*Adicionado em 23/04/2010 - Solicitado por Joelson*/
				cip.clearObIcms();
				cip.clearEmbLegaisMastersaf();

			}


			/**
			 * Nova regra by Joelson
			 * Para a UF “MG” aplicar ST para “ME/EPP/Simples Nacional Contribuinte” somente se o ramo de atividade for estabelecimento industrial
			 * @since 16/06/2009
			 */
			Long idPais = this.obterPaisTransacao(cip).getIdPais();
			UnidadeFederativa unidadeFederativa = unidadeFederativaService.findUnidadeFederativa(idUFOrigemAliquota, idPais);
			if("ME".equals(cip.getTpSituacaoTributariaTomador()) && "MG".equals(unidadeFederativa.getSgUnidadeFederativa())) {
				Long cdCfop = null;
				if(cip.getBlExisteResponsavel()) {
					cdCfop = codigoFiscalOperacaoService.findCdCfopRamoAtividadeByCliente(cip.getIdResponsavel());
				}
				if(CompareUtils.neNull(cdCfop, Long.valueOf(352))) {
					cip.setIdTipoTributacaoIcms(LongUtils.getLong(valorParametroTipoTributacaoNormal));
					cip.setBlEmbuteIcmsParcelas(Boolean.TRUE);
					cip.setBlImpDadosCalcCtrc(Boolean.TRUE);
					cip.setBlImprimeMemoCalcCte(Boolean.FALSE);

					/*Adicionado em 23/04/2010 - Solicitado por Joelson*/
					cip.clearObIcms();
					cip.clearEmbLegaisMastersaf();
				}
			}
		} else {
			cip.setBlEmbuteIcmsParcelas(Boolean.TRUE);
			cip.setBlImpDadosCalcCtrc(Boolean.TRUE);
			cip.setBlImprimeMemoCalcCte(Boolean.FALSE);
		}
		return Boolean.FALSE;
	}

	/**
	 * Determina se no estado do remetente se paga o Icms em cima dos pedágios
	 *
	 * Regra 7
	 * */
	private void validateUfPedagio(CalcularIcmsParam cip) {
		/*Se a uf de origem tem incidencia do ICMS no pedágio ou se não existe responsavel,
		retorna o valor da uf, senão retorna o parametro do cliente*/
		UnidadeFederativa ufAliquota = unidadeFederativaService.findById(this.getIdUFOrigemAliquota(cip));

		/*Aplicar icms pedágio*/
		Boolean blIcmsPedagio = ufAliquota.getBlIcmsPedagio();

		if (Boolean.FALSE.equals(blIcmsPedagio)) {
			if (!cip.getBlExisteResponsavel()){
				cip.setBlIncidenciaIcmsPedagio(Boolean.FALSE);
			} else {
				cip.setBlIncidenciaIcmsPedagio(cip.getResponsavel().getBlIcmsPedagio());
			}
		}else {
			cip.setBlIncidenciaIcmsPedagio(Boolean.TRUE);
		}
	}

	/**
	 * Atribui o valor para embutir nas parcelas.
	 * */
	private void calcularValorParcelas(CalcularIcmsParam cip) {
		cip.setVlTotalParcelas(BigDecimal.ZERO);

		//Calcular as parcelas normais
		for(ParcelaServico parcelaServico : cip.getCalculoFrete().getParcelas()) {
			calcularValorParcelasSingular(cip, parcelaServico);
			cip.addVlTotalParcelas(parcelaServico.getVlParcela());
		}

		//Calcular os serviços adicionais
		if (cip.getCalculoFrete().getServicosAdicionais() != null) {
			for(ParcelaServico parcelaServico : cip.getCalculoFrete().getServicosAdicionais()) {
				calcularValorParcelasSingular(cip, parcelaServico);
				cip.addVlTotalServicosAdicionais(parcelaServico.getVlParcela());
			}
		}
	}

	/**
	 * Atualiza o valor da parcela informada
	 *
	 * Regra 8
	 * */
	private void calcularValorParcelasSingular(CalcularIcmsParam cip, ParcelaServico parcelaServico) {

		BigDecimal vlIcmsParcela;
		CalculoFrete calculoFrete = cip.getCalculoFrete();

		if(parcelaServico.getVlBrutoParcela() == null) {
			parcelaServico.setVlBrutoParcela(parcelaServico.getVlParcela());
		}

		if(calculoFrete.getTabelaPreco() != null && Boolean.TRUE.equals(calculoFrete.getTabelaPreco().getBlIcmsDestacado())
				&& CompareUtils.gt(calculoFrete.getTributo().getPcAliquota(), BigDecimalUtils.ZERO) && parcelaServico.getVlParcela() != null) {

			cip.setVlBase(cip.getVlBase().add(parcelaServico.getVlParcela()));
			cip.setVlTotalParcelasComIcms(cip.getVlTotalParcelasComIcms().add(parcelaServico.getVlParcela()));

		}else {
			/* Se a parcela é um pedágio e que o estado é incluido no icms
			   ou que a parcela não é pedágio, calcular o novo valor da parcela */
			if (parcelaServico.getVlBrutoParcela() != null && ((parcelaServico.getParcelaPreco().getCdParcelaPreco().equals(ConstantesExpedicao.CD_PEDAGIO)
					&& cip.getBlIncidenciaIcmsPedagio().equals(Boolean.TRUE))
					|| !parcelaServico.getParcelaPreco().getCdParcelaPreco().equals(ConstantesExpedicao.CD_PEDAGIO)
			)) {
				vlIcmsParcela = parcelaServico.getVlBrutoParcela().multiply( cip.getPcEmbute().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP);
			} else {
				vlIcmsParcela = BigDecimalUtils.ZERO;

				cip.addVlParcelaPedagioSemIncidencia(parcelaServico.getVlBrutoParcela());
			}

			EmbuteIcmsParcela(cip, parcelaServico, vlIcmsParcela);
		}
	}

	private void EmbuteIcmsParcela(CalcularIcmsParam cip,
								   ParcelaServico parcelaServico, BigDecimal vlIcmsParcela) {
		if (cip.getBlEmbuteIcmsParcelas().equals(Boolean.TRUE) && cip.getBlAceitaSubstituicao().equals(Boolean.TRUE)) {
			/* Atribuir o valor total da parcela COM o valor ICMS */
			parcelaServico.setVlParcela(BigDecimalUtils.defaultBigDecimal(parcelaServico.getVlBrutoParcela()).add(vlIcmsParcela));

			if(CompareUtils.gt(vlIcmsParcela, BigDecimalUtils.ZERO)) {
				cip.setVlBase(cip.getVlBase().add(parcelaServico.getVlParcela()));
			}
		} else if (cip.getBlEmbuteIcmsParcelas().equals(Boolean.FALSE)) {
			/* Usado só no caso de observação de substituição tributária */
			cip.setVlTotalParcelasComIcms(cip.getVlTotalParcelasComIcms().add(parcelaServico.getVlParcela().add(vlIcmsParcela)));
		}
	}

	/**
	 * Calcula o valor total das parcelas/serviços adicionais e desconto
	 *
	 * Regra 10
	 */
	private void calcularValorTotal(CalcularIcmsParam cip) {

		/*Obtem o parametro do cliente através do cálculo de frete*/
		ParametroCliente parametroCliente = cip.getCalculoFrete().getParametroCliente();

		BigDecimal vlDesconto = null;

		/*Se tem que usar o desconto da tabela do cliente*/
		if ((parametroCliente == null) || Boolean.FALSE.equals(cip.getCalculoFrete().getBlDescontoTotal())) {
			vlDesconto = cip.getCalculoFrete().getVlDesconto();
			/*Se a tabela parametro_cliente tem desconto*/
		} else if (BigDecimalUtils.hasValue(parametroCliente.getPcDescontoFreteTotal())) {
			vlDesconto = cip.getVlTotalParcelas().multiply(BigDecimalUtils.percent(parametroCliente.getPcDescontoFreteTotal()));
		}

		/*Seta Zero caso o valor do desconto for nulo*/
		if (vlDesconto == null) {
			vlDesconto = BigDecimalUtils.ZERO;
		} else {
			vlDesconto = BigDecimalUtils.round(vlDesconto);
		}

		/*Se é um cálculo de cortesia*/
		if (ConstantesExpedicao.CALCULO_CORTESIA.equals(cip.getCalculoFrete().getTpCalculo())) {
			vlDesconto = cip.getVlTotalParcelas().multiply(BigDecimalUtils.percent((BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.PC_DESCONTO_FRETE_CORTESIA)));
		}

		/*Se o cálculo for manual*/
		if(ConstantesExpedicao.CALCULO_MANUAL.equals(cip.getCalculoFrete().getTpCalculo())) {

			vlDesconto =  BigDecimalUtils.round(BigDecimalUtils.defaultBigDecimal(cip.getCalculoFrete().getVlDesconto()));

			BigDecimal valorFrete = cip.getVlTotalParcelas().add(cip.getVlTotalServicosAdicionais()).subtract(vlDesconto);

			cip.getCalculoFrete().setVlTotalParcelas(valorFrete);
			cip.getCalculoFrete().setVlTotal(valorFrete);
		}else{

			/*Total do frete*/
			cip.getCalculoFrete().setVlTotal(cip.getVlTotalParcelas().add(cip.getVlTotalServicosAdicionais()).subtract(vlDesconto));
		}

		/*Valor do Desconto*/
		cip.getCalculoFrete().setVlDesconto(vlDesconto);

		/*Valor Base*/
		cip.setVlBase(cip.getCalculoFrete().getVlTotal().subtract(cip.getVlParcelaPedagioSemIncidencia()));
	}

	/**
	 * Calcular o valor toal do Icms
	 *
	 * Regra 11
	 * */
	private void calcularValorTotalIcms(CalcularIcmsParam cip) {
		cip.setVlIcmsTotal(cip.getVlBase().setScale(2, RoundingMode.HALF_UP).multiply(cip.getPcAliquota().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP));
	}

	/**
	 * Monta a observação
	 *
	 * Regra 12
	 * */
	private void mountObservacao(CalcularIcmsParam cip) {

		Long valorParametro = ((BigDecimal)configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ST")).longValue();

		/*Se o tipoTributacaoIcms é igual ao parametro geral ID_TIPO_TRIBUTACAO_ST*/
		if (cip.getIdTipoTributacaoIcms() != null && cip.getIdTipoTributacaoIcms().equals(valorParametro)) {

			cip.addObIcms(cip.getObservacaoICMSTipoTributacao(this.getIdUFOrigemAliquota(cip), "T", Boolean.FALSE));

			/*Incrementa EmbLegalMastersaf Tipo Tributacao*/
			cip.addAllEmbLegalMastersafByObservacaoICMSTipoTributacao();

			/*Se não embute os valores nas parcelas ou se tem substituição*/
			if (cip.getBlEmbuteIcmsParcelas().equals(Boolean.FALSE) || cip.getBlAceitaSubstituicao().equals(Boolean.FALSE)) {

				/*((valor total das parcelas com icms - valor pedagio sem incidencia - valor desconto) * pcAliquota / 100) * pcRetenção / 100*/
				cip.setVlIcmsTotal(((cip.getVlTotalParcelasComIcms().subtract(cip.getVlParcelaPedagioSemIncidencia().subtract(cip.getVlDesconto()))).multiply(cip.getPcAliquota().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.DOWN)).multiply(cip.getPcRetencaoST().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP));

				cip.addObIcms(configuracoesFacade.getMensagem("resultadoIcms") + FormatUtils.formatDecimal("#,##0.00", cip.getVlIcmsTotal()));
				cip.setVlDevido(cip.getCalculoFrete().getVlTotal());
				cip.setPcAliquota(BigDecimalUtils.ZERO);
				cip.setVlIcmsTotal(BigDecimalUtils.ZERO);
				cip.setVlBase(BigDecimalUtils.ZERO);
				cip.setPcRetencaoST(BigDecimalUtils.ZERO);
				cip.setVlRetencaoST(BigDecimalUtils.ZERO);

				/*Sai da rotina*/
				return;

			} else {

				/*Calcula valor total da retensão*/
				cip.setVlRetencaoST(cip.getVlIcmsTotal().multiply(cip.getPcRetencaoST().divide(BigDecimalUtils.HUNDRED)).setScale(2, RoundingMode.HALF_UP));

				/*Calcula valor devido*/
				cip.setVlDevido(cip.getCalculoFrete().getVlTotal().subtract(cip.getVlRetencaoST()));

				if( validateUtilizacaoCodigo90ST(this.getIdUFOrigemAliquota(cip)) ){
					cip.addObIcms("ICMS ST no valor de " + FormatUtils.formatDecimal( "#,##0.00", cip.getVlRetencaoST() ));
				}

				/*Se o tipo da situação tributaria do remetente for iguala CI e o percentual de retensão for diferente de 100*/
				if (cip.getTpSituacaoTributariaRemetente().equals("CI") && !cip.getPcRetencaoST().equals(BigDecimalUtils.HUNDRED)) {
					cip.addObIcms(FormatUtils.formatDecimal("#,##0.00", cip.getPcRetencaoST()) + "% " +
							FormatUtils.formatDecimal("#,##0.00", cip.getVlRetencaoST()) + " substituto, "+
							FormatUtils.formatDecimal("#,##0.00", BigDecimalUtils.HUNDRED.subtract(cip.getPcRetencaoST())) + "% " +
							FormatUtils.formatDecimal("#,##0.00", cip.getVlIcmsTotal().subtract(cip.getVlRetencaoST())) +
							" substituído");
				}
			}/*else*/

			/*Verifica se deve imprimir os valores calculados*/
			if (cip.getBlImprimeMemoCalcCte()) {
				cip.addObIcms("ICMS " + FormatUtils.formatDecimal("#,##0.00", cip.getPcAliquota()) + "% " + FormatUtils.formatDecimal("#,##0.00", cip.getVlIcmsTotal()) +
						" - Retenção ICMS " + FormatUtils.formatDecimal("#,##0.00", cip.getPcRetencaoST()) +
						"% " + FormatUtils.formatDecimal("#,##0.00", cip.getVlRetencaoST()) +
						" - Valor liq. a pg " + FormatUtils.formatDecimal("#,##0.00", cip.getVlDevido()));
			}

			if (!cip.getBlImpDadosCalcCtrc()) {
				cip.setPcAliquota(BigDecimalUtils.ZERO);
				cip.setVlIcmsTotal(BigDecimalUtils.ZERO);
				cip.setVlBase(BigDecimalUtils.ZERO);

			}

		} else {

			/*Zera o valor e o percentual*/
			cip.setVlRetencaoST(BigDecimalUtils.ZERO);
			cip.setPcRetencaoST(BigDecimalUtils.ZERO);

			/*Obtem o valor devido atraves do valor dotal do calculo do frete*/
			cip.setVlDevido(cip.getCalculoFrete().getVlTotal());

			/*Verifica se o id do tipo da tributação é igual ao parametro geral ID_TIPO_TRIBUTACAO_DEVIDO_OUTRA_UF*/
			if (cip.getIdTipoTributacaoExcecao() != null
					&& cip.getIdTipoTributacaoExcecao().equals(LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_DEVIDO_OUTRA_UF")))) {

				cip.addObIcms(cip.getObservacao()+" B.Calc "+ BigDecimalUtils.round(cip.getVlBase()) +" ICMS "+cip.getVlIcmsTotal());

				/*Seta ISENTO através do id parametro geral ID_TIPO_TRIBUTACAO_ISENTO*/

				/*Seta ISENTO através do id parametro geral ID_TIPO_TRIBUTACAO_ISENTO*/
				cip.setIdTipoTributacaoIcms(LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_ISENTO")));
			}

			/*Verifica se o id do tipo da tributação não é igual ao parametro geral ID_TIPO_TRIBUTACAO_NORMAL*/
			if (!cip.getIdTipoTributacaoIcms().equals(LongUtils.getLong(configuracoesFacade.getValorParametro("ID_TIPO_TRIBUTACAO_NORMAL")))) {
				cip.setVlBase(BigDecimal.ZERO);
				cip.setVlIcmsTotal(BigDecimal.ZERO);
				cip.setPcAliquota(BigDecimal.ZERO);
			}

			/*Adiciona observação*/
			cip.addObIcms(cip.getObservacaoICMSTipoTributacao("T", Boolean.FALSE));

			/*Incrementa EmbLegalMastersaf Tipo Tributacao*/
			cip.addAllEmbLegalMastersafByObservacaoICMSTipoTributacao();

		}/*else*/
	}

	/**
	 * Método chamado na hora de sair do método 'calcularIcms'
	 */
	private void exitCalcularIcms(CalcularIcmsParam cip) {

		cip.getCalculoFrete().setVlTotalParcelas();
		cip.getCalculoFrete().setVlTotalServicosAdicionais();

		calcularValorTotal(cip);

		cip.setVlDevido(cip.getVlTotalParcelas());

		cip.setVlBase(BigDecimal.ZERO);

		//Se o tipo de conhecimento é 'Complemento ICMS'
		if (cip.getTpConhecimento() != null && cip.getTpConhecimento().equals("CI")) {
			cip.setVlIcmsTotal(cip.getVlTotalParcelas());
			if(!cip.getCalculoFrete().gerarParcelaFretePesoCtrcCompleto()) {
				cip.setVlIcmsTotal(cip.getVlComplementoIcms());
			}
		} else {
			cip.setPcAliquota(BigDecimal.ZERO);
		}
	}

	private CalcularIcmsParam initializeCalcularIcmsParam(CalculoFrete calculoFrete) {
		CalcularIcmsParam cip = new CalcularIcmsParam(
				calculoFrete,
				unidadeFederativaService,
				clienteService,
				inscricaoEstadualService,
				tipoTributacaoIEService,
				observacaoICMSPessoaService,
				observacaoICMSService,
				domainValueService,
				tipoTributacaoIcmsService,
				configuracoesFacade
		);
		return cip;
	}

	/** Regra adicionada no Jira LMS-2979
	 Se o parâmetro geral “UFS_UTILIZAM_CODIGO_90” for igual a “S”
	 E SG_UNIDADE_FEDERATIVA (encontrada anteriormente na regra 7) pertencer ao parâmetro geral “UFS_UTILIZAM_CODIGO_90”
	 observacao = “ICMS ST no valor de ” + vlRetencaoST
	 obIcms = obIcms + observacao
	 Fim Se
	 **/
	public boolean validateUtilizacaoCodigo90ST(Long idUnidadeFederativa){
		String blUtilizaCodigo90ST = (String)configuracoesFacade.getValorParametro("BL_UTILIZA_CODIGO_90_ST");
		if( "S".equals(blUtilizaCodigo90ST) ){
			String ufsUtilizaCodigo90 = (String)configuracoesFacade.getValorParametro("UFS_UTILIZAM_CODIGO_90");
			return isUFOnList(idUnidadeFederativa, ufsUtilizaCodigo90);
		}
		return false;
	}

	private boolean isUFOnList(Long idUnidadeFederativa, String ufsUtilizaCodigo90) {
		if( ufsUtilizaCodigo90 != null ){
			UnidadeFederativa ufAliquota = unidadeFederativaService.findById(idUnidadeFederativa);
			String[] listUFSUtilizaCodigo90 = ufsUtilizaCodigo90.split(";");
			if( ufAliquota != null && ArrayUtils.contains(listUFSUtilizaCodigo90, ufAliquota.getSgUnidadeFederativa() ) ){
				return true;
			}
		}
		return false;
	}

	public boolean validateUtilizacaoCodigo90ValorCredito(Long idUnidadeFederativa){
		String ufsThathasValorCredito = parametroGeralService.findSimpleConteudoByNomeParametro("UFS_PREENCHE_CAMPO_241");
		return isUFOnList(idUnidadeFederativa, ufsThathasValorCredito);
	}

	public BigDecimal calcICMSValorCredito(final Conhecimento conhecimento) {
		BigDecimal vlIcmsSubstituicaoTributaria = conhecimento.getVlIcmsSubstituicaoTributaria();
		BigDecimal vlImposto = conhecimento.getVlImposto();
		String credPresumidoBA = parametroGeralService.findSimpleConteudoByNomeParametro("PC_CRED_PRESUMIDO_BA");

		if (vlIcmsSubstituicaoTributaria != null && vlImposto != null) {
			if (vlImposto.equals(BigDecimal.ZERO)) {
				return conhecimento.getVlIcmsSubstituicaoTributaria().multiply(new BigDecimal(credPresumidoBA));
			}
			return conhecimento.getVlImposto().subtract(conhecimento.getVlIcmsSubstituicaoTributaria());
		}

		return BigDecimal.ZERO;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setAliquotaIcmsService(AliquotaIcmsService aliquotaIcmsService) {
		this.aliquotaIcmsService = aliquotaIcmsService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setExcecaoICMSClienteService(ExcecaoICMSClienteService excecaoICMSClienteService) {
		this.excecaoICMSClienteService = excecaoICMSClienteService;
	}
	public void setObservacaoICMSPessoaService(ObservacaoICMSPessoaService observacaoICMSPessoaService) {
		this.observacaoICMSPessoaService = observacaoICMSPessoaService;
	}
	public void setObservacaoICMSService(ObservacaoICMSService observacaoICMSService) {
		this.observacaoICMSService = observacaoICMSService;
	}
	public void setParametroSubstituicaoTribService(ParametroSubstituicaoTribService parametroSubstituicaoTribService) {
		this.parametroSubstituicaoTribService = parametroSubstituicaoTribService;
	}
	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setTipoTributacaoUfService(TipoTributacaoUfService tipoTributacaoUfService) {
		this.tipoTributacaoUfService = tipoTributacaoUfService;
	}
	public void setTipoTributacaoIcmsService(TipoTributacaoIcmsService tipoTributacaoIcmsService) {
		this.tipoTributacaoIcmsService = tipoTributacaoIcmsService;
	}

	public void setCodigoFiscalOperacaoService(
			CodigoFiscalOperacaoService codigoFiscalOperacaoService) {
		this.codigoFiscalOperacaoService = codigoFiscalOperacaoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setInscricaoEstadualColetivaService(
			InscricaoEstadualColetivaService inscricaoEstadualColetivaService) {
		this.inscricaoEstadualColetivaService = inscricaoEstadualColetivaService;
	}
	public void setExcecaoICMSIntegrantesContribuintesDAO(
			ExcecaoICMSIntegrantesContribuintesDAO excecaoICMSIntegrantesContribuintesDAO) {
		this.excecaoICMSIntegrantesContribuintesDAO = excecaoICMSIntegrantesContribuintesDAO;
	}
}
