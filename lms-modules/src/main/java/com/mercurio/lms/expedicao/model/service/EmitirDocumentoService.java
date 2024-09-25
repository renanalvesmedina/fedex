package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.ParametroPais;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.configuracoes.model.service.ParametroPaisService;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.expedicao.model.ManifestoInternacional;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.dao.EmitirDoctoServicoDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.emitirDocumentoService"
 */
public class EmitirDocumentoService {
	private ConfiguracoesFacade configuracoesFacade;
	private EmitirDoctoServicoDAO emitirDoctoServicoDAO;
	private ImpressoraService impressoraService;
	private ParametroPaisService parametroPaisService;
	private CiaFilialMercurioService ciaFilialMercurioService;
	private ImpressoraFormularioService impressoraFormularioService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NFEConjugadaService nfeConjugadaService;

	public void generateProximoNumero(CtoInternacional ctoInternacional) {
		ParametroPais parametroPais = parametroPaisService.findByIdPais(ctoInternacional.getPaisOrigem().getIdPais(), true);

		Long nrUltimoCrt = parametroPais.getNrUltimoCrt();
		if( (nrUltimoCrt == null) || CompareUtils.eq(nrUltimoCrt, LongUtils.ZERO)) {
			nrUltimoCrt = LongUtils.ONE;
		} else {
			nrUltimoCrt = LongUtils.incrementValue(nrUltimoCrt);
		}
		parametroPais.setNrUltimoCrt(nrUltimoCrt);

		parametroPaisService.store(parametroPais);

		ctoInternacional.setNrCrt(nrUltimoCrt);
	}

	public void generateProximoNumero(Conhecimento conhecimento) {
		final String tpDocumentoServico = conhecimento.getTpDoctoServico().getValue();
		final Long idFilial = SessionUtils.getFilialSessao().getIdFilial();

		Long nrConhecimento = null;
		Integer dvConhecimento = null;
		if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDocumentoServico) 
				|| ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico)) {
			
			String nomeParametro;
		if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDocumentoServico)) {
				nomeParametro = "NR_CONHECIMENTO";
			} else {
				nomeParametro = "NR_CTE";
			}
			
			if ((conhecimento.getNrConhecimento() == null || conhecimento.getNrConhecimento().intValue() < 0)) {
				String tpSituacaoConhecimento = conhecimento.getTpSituacaoConhecimento().getValue();
				if("P".equals(tpSituacaoConhecimento)) {
					if(conhecimento.getNrConhecimento() == null) {
						// LMS-968
						nrConhecimento = conteudoParametroFilialService.generateNrConhecimentoTmp();
					} else if (conhecimento.isGenerateUniqueNumber()) {
						nrConhecimento = configuracoesFacade.incrementaParametroSequencial(idFilial, nomeParametro, true);
						dvConhecimento = ConhecimentoUtils.getDigitoVerificador(nrConhecimento);
						conhecimento.setGenerateUniqueNumber(false);
					}
				} else {
					nrConhecimento = configuracoesFacade.incrementaParametroSequencial(idFilial, nomeParametro, true);
					dvConhecimento = ConhecimentoUtils.getDigitoVerificador(nrConhecimento);
				}
			} else if (conhecimento.getNrConhecimento() != null && conhecimento.getNrConhecimento().intValue() > 0) {
				nrConhecimento = conhecimento.getNrConhecimento();
				dvConhecimento = ConhecimentoUtils.getDigitoVerificador(nrConhecimento);
			}			
		} else if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoServico) 
				|| ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) {
			
			String nomeParametro;
			if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoServico)) {
				nomeParametro = "NR_NOTA_FISCAL";
			}else{
				nomeParametro = "NR_NFE";
			}
			
			if ((conhecimento.getNrConhecimento() == null || conhecimento.getNrConhecimento().intValue() < 0)) {
				String tpSituacaoConhecimento = conhecimento.getTpSituacaoConhecimento().getValue();
				if("P".equals(tpSituacaoConhecimento)) {
			if (conhecimento.getNrConhecimento() == null){
						nrConhecimento = conteudoParametroFilialService.generateNrConhecimentoTmp();
					} else if (conhecimento.isGenerateUniqueNumber()) {
						nrConhecimento = configuracoesFacade.incrementaParametroSequencial(idFilial, nomeParametro, true);
						conhecimento.setGenerateUniqueNumber(false);
					}
			}else{
					nrConhecimento = configuracoesFacade.incrementaParametroSequencial(idFilial, nomeParametro, true);
				}
			} else if (conhecimento.getNrConhecimento() != null && conhecimento.getNrConhecimento().intValue() > 0) {
				nrConhecimento = conhecimento.getNrConhecimento();
			}
		}

		/* Seta os dados */
		if(nrConhecimento != null) {
			conhecimento.setNrConhecimento(nrConhecimento);
			conhecimento.setDvConhecimento(dvConhecimento);
			conhecimento.setNrDoctoServico(nrConhecimento);
		}
	}

	public void generateProximoNumero(NotaFiscalServico notaFiscalServico) {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		generateProximoNumero(notaFiscalServico, idFilial);
	}

	public void generateProximoNumero(NotaFiscalServico notaFiscalServico, Long idFilial) {
		Long nrNotaFiscal = null;

		if( ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(notaFiscalServico.getTpNotaFiscalServico().getValue()) ){
			nrNotaFiscal = configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_NFE", true);
		}else if( ConstantesExpedicao.NOTA_FISCAL_SERVICO.equalsIgnoreCase(notaFiscalServico.getTpNotaFiscalServico().getValue()) ){
			nrNotaFiscal = configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_NOTA_FISCAL", true);			
		}

		notaFiscalServico.setNrNotaFiscalServico(nrNotaFiscal);
		notaFiscalServico.setNrDoctoServico(nrNotaFiscal);
	}

	public Long generateProximoNumeroRPS() {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		return generateProximoNumeroRPS(idFilial);		
	}
	
	public Long generateProximoNumeroRPS(Long idFilial) {
		if(nfeConjugadaService.isAtivaNfeConjugada(idFilial)){
			return configuracoesFacade.incrementaParametroSequencial(idFilial, NFEConjugadaService.PARAM_NR_NFE_CONJUGADA, true);
		}
		
		return  configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_RPS", true);
	}
	
	public void generateProximoNumero(Awb awb, String tpDocumentoServico) {

		if(ConstantesExpedicao.PRE_AWB.equals(tpDocumentoServico)) {
			Long nrAwb = configuracoesFacade.decrementaParametroSequencial("NR_PRE_AWB", true);
			awb.setNrAwb(nrAwb);
		} else if(ConstantesExpedicao.AIRWAY_BILL.equals(tpDocumentoServico)) {
			CiaFilialMercurio ciaFilialMercurio = ciaFilialMercurioService.findByIdInitLazyProperties(awb.getCiaFilialMercurio().getIdCiaFilialMercurio(), true);
			Integer nrUltimoAwb = ciaFilialMercurio.getNrUltimoAwb();
			if( (nrUltimoAwb == null) || CompareUtils.eq(nrUltimoAwb, IntegerUtils.ZERO)) {
				nrUltimoAwb = IntegerUtils.ONE;
			} else {
				nrUltimoAwb = IntegerUtils.incrementValue(nrUltimoAwb);
			}
			ciaFilialMercurio.setNrUltimoAwb(nrUltimoAwb);
			Integer dvUltimoAwb = ciaFilialMercurio.getDvUltimoAwb();
			if(dvUltimoAwb == null) {
				dvUltimoAwb = IntegerUtils.ZERO;
			} else {
				dvUltimoAwb = IntegerUtils.incrementValue(dvUltimoAwb);
				if(dvUltimoAwb==7){
					dvUltimoAwb=0;
				}
			}
			ciaFilialMercurio.setDvUltimoAwb(dvUltimoAwb);
			ciaFilialMercurioService.store(ciaFilialMercurio);

			awb.setCiaFilialMercurio(ciaFilialMercurio);
			awb.setDsSerie(IntegerUtils.defaultInteger(ciaFilialMercurio.getNrSerieAwb()).toString());
			awb.setNrAwb(LongUtils.getLong(nrUltimoAwb));
			awb.setDvAwb(dvUltimoAwb);
		}
	}

	public void generateProximoNumero(SolicitacaoContratacao solicitacaoContratacao) {
		Long idFilial = solicitacaoContratacao.getFilial().getIdFilial();

		Long nrSolicitacaoContratacao = configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_SOLICITACAO_CONTR", true);
		solicitacaoContratacao.setNrSolicitacaoContratacao(nrSolicitacaoContratacao);
	}

	public void generateProximoNumero(ManifestoViagemNacional manifestoViagemNacional) {
		String tpManifesto = manifestoViagemNacional.getManifesto().getTpManifesto().getValue();

		if(!"V".equals(tpManifesto)) {
			throw new BusinessException("");//TODO
		}
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Integer nrManifesto = IntegerUtils.getInteger(configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_MANIF_VIAGEM_NAC", true));

		manifestoViagemNacional.setNrManifestoOrigem(nrManifesto);
	}

	public void generateProximoNumero(ManifestoInternacional manifestoInternacional) {
		String tpDocumentoServico = manifestoInternacional.getTpMic().getValue();
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		String nmParametro = null;

		if(ConstantesExpedicao.MANIFESTO_ITERNACIONAL_CARGA.equals(tpDocumentoServico)) {
			nmParametro = "NR_MIC";
		} else if("MID".equals(tpDocumentoServico)) {
			nmParametro = "NR_MIC_DTA";
		}

		Long nrMic = configuracoesFacade.incrementaParametroSequencial(idFilial, nmParametro, true);
		manifestoInternacional.setNrManifestoInt(nrMic);
	}

	public void findProximoFormulario(ImpressoraFormulario impressoraFormulario) {
		// incrementa numero do formulario
		impressoraFormulario.setNrUltimoFormulario(LongUtils.incrementValue(impressoraFormulario.getNrUltimoFormulario()));

		// verifica se utiliza selo fiscal
		if(impressoraFormulario.getControleFormulario().getNrSeloFiscalInicial() != null 
				&& impressoraFormulario.getNrUltimoSeloFiscal() != null
				&& impressoraFormulario.getNrSeloFiscalFinal() != null) {
			// incrementa numero do selo fiscal
			impressoraFormulario.setNrUltimoSeloFiscal(LongUtils.incrementValue(impressoraFormulario.getNrUltimoSeloFiscal()));
			if(CompareUtils.gt(impressoraFormulario.getNrUltimoSeloFiscal(), impressoraFormulario.getNrSeloFiscalFinal())) {
				throw new BusinessException("LMS-04137");
			}
		}
	}

	/**
	 * Busca ImpressoraFormulario fazendo validações
	 * 
	 * @param idFilial
	 * @param dsMacAddress
	 * @param tpFormulario
	 * @param nrProximoFormulario
	 * @param nrProximoCodigoBarras Caso informado e esteja cadastrado no Controle de Formularios, valida se está condizente com o número do formulário
	 * @return
	 */
	public ImpressoraFormulario findImpressoraFormulario(Long idFilial, String dsMacAddress, String tpFormulario, Long nrProximoFormulario, Long nrProximoCodigoBarras, Boolean validarCodigoBarras) {
		if(validarCodigoBarras == null){
			validarCodigoBarras = Boolean.TRUE;
		}
		
		Impressora impressora = impressoraService.findImpressoraUsuario(idFilial, dsMacAddress, "M");

		ImpressoraFormulario impressoraFormulario = emitirDoctoServicoDAO.findImpressoraFormulario(idFilial, impressora.getIdImpressora(), tpFormulario, nrProximoFormulario);
		if(impressoraFormulario == null) {
			throw new BusinessException("LMS-04113");
		}
		
		impressoraFormularioService.validateNumeroProximoFormulario(impressora, tpFormulario, nrProximoFormulario);
		
		//O formulario de NFT e NFS seguirao a mesma numeracao
		if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpFormulario)) {
			tpFormulario = ConstantesExpedicao.NOTA_FISCAL_SERVICO;
		}
		
		if(!CompareUtils.eq(LongUtils.incrementValue(impressoraFormulario.getNrUltimoFormulario()), nrProximoFormulario)) {
			throw new BusinessException("LMS-04114", new Object[]{nrProximoFormulario});
		}
		//Se for conhecimento valida se nrAidf esta preenchido
		if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpFormulario) && impressoraFormulario.getControleFormulario().getNrAidf() == null) {
			throw new BusinessException("LMS-04135");
		}
		
		//Jira LMS-537 - Validação da sequencia do código de barras com o controle do formulário
		//LMS-3489 - essas validações não devem ser aplicadas para NFS.
		if(validarCodigoBarras && impressoraFormulario.getControleFormulario().getNrCodigoBarrasInicial() != null){
			if(nrProximoCodigoBarras == null){
				throw new BusinessException("LMS-04362");
			}
			Long diferenca = nrProximoFormulario - impressoraFormulario.getControleFormulario().getNrFormularioInicial();
			Long nrCodigoBarrasEsperado = impressoraFormulario.getControleFormulario().getNrCodigoBarrasInicial()/10 + diferenca; 
			if(nrCodigoBarrasEsperado.compareTo(nrProximoCodigoBarras/10) != 0){
				throw new BusinessException("LMS-04338");
			}
		}
		
		return impressoraFormulario;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setEmitirDoctoServicoDAO(EmitirDoctoServicoDAO emitirDoctoServicoDAO) {
		this.emitirDoctoServicoDAO = emitirDoctoServicoDAO;
	}
	public void setImpressoraService(ImpressoraService impressoraService) {
		this.impressoraService = impressoraService;
	}
	public void setParametroPaisService(ParametroPaisService parametroPaisService) {
		this.parametroPaisService = parametroPaisService;
	}
	public void setCiaFilialMercurioService(CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
	}
	public void setImpressoraFormularioService(ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}
}