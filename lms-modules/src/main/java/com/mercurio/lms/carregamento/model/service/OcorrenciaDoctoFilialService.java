package com.mercurio.lms.carregamento.model.service;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.OcorrenciaDoctoFilial;
import com.mercurio.lms.carregamento.model.dao.OcorrenciaDoctoFilialDAO;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class OcorrenciaDoctoFilialService extends CrudService<OcorrenciaDoctoFilial, Long> {

	private DoctoServicoService doctoServicoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private MdaService mdaService;
	private UsuarioLMSService usuarioLMSService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private OcorrenciaDoctoFilialDAO ocorrenciaDoctoFilialDAO;
	private ConhecimentoService conhecimentoService;
	
	public java.io.Serializable store(OcorrenciaDoctoFilial bean) {
        return super.store(bean);
    }
	
	public void storeFilialLocalizacaoDoctoServico(Long idDoctoServico, String observacao){
		
		DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
    	
		OcorrenciaDoctoFilial ocorrenciaDoctoFilial = montarOcorrenciaDoctoFilial(observacao, doctoServico);

		getDao().store(ocorrenciaDoctoFilial);

		Filial filialLogada = SessionUtils.getFilialSessao();
		List<VolumeNotaFiscal> listaVolumesNotaFiscal = volumeNotaFiscalService.findByIdConhecimentoAndIdLocalizacaoFilial(idDoctoServico, doctoServico.getFilialLocalizacao().getIdFilial());
		for (VolumeNotaFiscal volumeNotaFiscal : listaVolumesNotaFiscal) {
			if (volumeNotaFiscal.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria().compareTo(doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria()) == 0) {
				volumeNotaFiscal.setLocalizacaoFilial(filialLogada);
				volumeNotaFiscalService.store(volumeNotaFiscal);
			}
		}

		doctoServico.setFilialLocalizacao(filialLogada);
		doctoServico.setObComplementoLocalizacao(filialLogada.getSgFilial() + " - " + filialLogada.getPessoa().getNmFantasia());
		doctoServicoService.store(doctoServico);		

		String nrDocumento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + doctoServico.getNrDoctoServico();
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("308"), idDoctoServico,
						filialLogada.getIdFilial(), nrDocumento,
						JTDateTimeUtils.getDataHoraAtual(), doctoServico.getTpDocumentoServico().getValue());
	}

	private OcorrenciaDoctoFilial montarOcorrenciaDoctoFilial(String observacao,
			DoctoServico doctoServico) {
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
    	OcorrenciaDoctoFilial ocorrenciaDoctoFilial = new OcorrenciaDoctoFilial();
		ocorrenciaDoctoFilial.setDoctoServico(doctoServico);
		ocorrenciaDoctoFilial.setFilialDoctoOrigem(doctoServico.getFilialByIdFilialOrigem());
		ocorrenciaDoctoFilial.setFilialOcorrencia(SessionUtils.getFilialSessao());
		ocorrenciaDoctoFilial.setFilialDoctoLocalizacao(doctoServico.getFilialLocalizacao());
		ocorrenciaDoctoFilial.setFilialDoctoDestino(doctoServico.getFilialByIdFilialDestino());
		ocorrenciaDoctoFilial.setLocalizacaoMercadoria(doctoServico.getLocalizacaoMercadoria());
		ocorrenciaDoctoFilial.setUsuarioOcorrencia(usuarioLMSService.findById(usuarioLogado.getIdUsuario()));
		ocorrenciaDoctoFilial.setDhOcorrencia(dataHoraAtual);
		ocorrenciaDoctoFilial.setObOcorrencia(observacao);
		return ocorrenciaDoctoFilial;
	}
	
	/**
	 * Lista ocorrências de alteração da filial de localização do documento de serviço.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @param idDoctoServico
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idFilialOcorrencia
	 * @return
	 */
	public List<OcorrenciaDoctoFilial> findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(YearMonthDay dataInicial, YearMonthDay dataFinal, Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino, Long idFilialOcorrencia, TypedFlatMap criteria) {
		List<OcorrenciaDoctoFilial> ocorrencias = getOcorrenciaDoctoFilialDAO().findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(dataInicial, dataFinal, idDoctoServico, idFilialOrigem, idFilialDestino, idFilialOcorrencia, criteria);
		return ocorrencias;
	}
	
	public ResultSetPage findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(TypedFlatMap criteria) {
		return getOcorrenciaDoctoFilialDAO().findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(criteria);
	}
	
	/**
	 * Valida código de barras
	 * 
	 * @param idDoctoServico
	 * @param codigoBarras
	 */
	public void validateCodigoBarras(Long idDoctoServico, String codigoBarras) {
	
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		
		if (("CTR".equals(doctoServico.getTpDocumentoServico().getValue()) 
				|| "CTE"
				.equals(doctoServico.getTpDocumentoServico().getValue()))
				&& StringUtils.isBlank(codigoBarras)) {
			throw new BusinessException("LMS-05374");
		}
		
		//1
		Boolean naoValido = "MDA".equals(doctoServico.getTpDocumentoServico().getValue()) && StringUtils.isNotBlank(codigoBarras);
			
		//2
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServicoAndTpSituacaoDocumento(idDoctoServico, "A");
		
		naoValido |= "CTE".equals(doctoServico.getTpDocumentoServico().getValue())
				&& StringUtils.isNotBlank(codigoBarras)
				&& !codigoBarras.equals(monitoramentoDocEletronico.getNrChave());
				
		//3
		if("CTR".equals(doctoServico.getTpDocumentoServico().getValue())) {
			Conhecimento conhecimento = (Conhecimento) doctoServico;
			naoValido |= !codigoBarras.equals(conhecimento.getNrCodigoBarras().toString());
		}
		
		if (naoValido) {
			throw new BusinessException("LMS-05373");
		}
	}
	
	
	
	/**
	 * Valida alteração da localização do documento de serviço
	 * 
	 * @param idDoctoServico
	 * @param observacao
	 */
	public void validateAlteracaoLocalizacaoDoctoServico(Long idDoctoServico, String observacao) {
		
		if (StringUtils.isBlank(observacao)) {
			throw new BusinessException("LMS-05375");
		}
		
		if (idDoctoServico == null) {
			throw new BusinessException("LMS-05379");
		}
		
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		Boolean doctoNaoEmitido = false;
		//1
		if("CTE".equals(doctoServico.getTpDocumentoServico().getValue()) || "CTR".equals(doctoServico.getTpDocumentoServico().getValue())) {
			Conhecimento conhecimento = (Conhecimento) doctoServico;
			doctoNaoEmitido = !"E".equals(conhecimento.getTpSituacaoConhecimento().getValue());
		}
		
		//2
		Mda mda = mdaService.findMdaByIdDoctoServico(idDoctoServico);
		doctoNaoEmitido |= "MDA".equals(doctoServico.getTpDocumentoServico().getValue()) 
				&& mda != null && !"E".equals(mda.getTpStatusMda().getValue());
		
		if (doctoNaoEmitido) {
			throw new BusinessException("LMS-05380");
		}
		
		//1
		Boolean doctoNaoValido = doctoServico.getFilialLocalizacao().getIdFilial().compareTo(SessionUtils.getFilialSessao().getIdFilial()) == 0;

		//2
		if(Boolean.FALSE.equals(doctoNaoValido)) {

			Short cdLocalizacao = doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
			doctoNaoValido = cdLocalizacao.compareTo(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA) == 0;
		}

		//3
			if("CTE".equals(doctoServico.getTpDocumentoServico().getValue()) || "CTR".equals(doctoServico.getTpDocumentoServico().getValue())) {
			Conhecimento conhecimento = (Conhecimento) doctoServico;
			doctoNaoValido |= "CI".equals(conhecimento.getTpConhecimento()) || "CF".equals(conhecimento.getTpConhecimento());
		}
		
		if (doctoNaoValido) {
			throw new BusinessException("LMS-05377");
		}
		
	}
	
	public Integer getRowCount(YearMonthDay dataInicial, YearMonthDay dataFinal, Long idDoctoServico, Long idFilialOrigem, Long idFilialDestino, Long idFilialOcorrencia) {
		return getOcorrenciaDoctoFilialDAO().getRowCount(dataInicial, dataFinal, idDoctoServico, idFilialOrigem,
				idFilialDestino, idFilialOcorrencia);
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setOcorrenciaDoctoFilialDAO(OcorrenciaDoctoFilialDAO dao) {
    	this.ocorrenciaDoctoFilialDAO = dao;
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private OcorrenciaDoctoFilialDAO getOcorrenciaDoctoFilialDAO() {
        return this.ocorrenciaDoctoFilialDAO;
    }

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public MonitoramentoDocEletronicoService getMonitoramentoDocEletronicoService() {
		return monitoramentoDocEletronicoService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public MdaService getMdaService() {
		return mdaService;
	}

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}	
	
}
