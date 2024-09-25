package com.mercurio.lms.carregamento.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.OcorrenciaDoctoFilial;
import com.mercurio.lms.carregamento.model.service.OcorrenciaDoctoFilialService;
import com.mercurio.lms.carregamento.report.ManterFilialLocalizacaoDoctoServicoService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTFormatUtils;

public class ManterFilialLocalizacaoDoctoServicoAction extends CrudAction {

	private FilialService filialService;
	private DoctoServicoService doctoServicoService;
	private ServicoService servicoService;
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private UsuarioLMSService usuarioLMSService;
	private ReportExecutionManager reportExecutionManager;
	private ManterFilialLocalizacaoDoctoServicoService manterFilialLocalizacaoDoctoServicoService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private MdaService mdaService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private RecursoMensagemService recursoMensagemService;
	
	@Override
	public Integer getRowCount(Map criteria) {
		YearMonthDay dhInicial = (YearMonthDay) criteria.get("periodoInicial");
		YearMonthDay dhFinal = (YearMonthDay) criteria.get("periodoFinal");
		
		if((Long)criteria.get("idDoctoServico") == null) {
			if (dhInicial != null && dhFinal != null) {
				if(dhFinal.isAfter(dhInicial.plusDays(60))) {
					throw new BusinessException("LMS-05372");
				}
			} else {
				throw new BusinessException("LMS-05371");
			}
		}
		
		return getOcorrenciaDoctoFilialService().getRowCount(dhInicial, 
				dhFinal, 
				(Long)criteria.get("idDoctoServico"), 
				(Long)criteria.get("idFilialO"), 
				(Long)criteria.get("idFilialDestino"), 
				(Long)criteria.get("idFilialOcorrencia"));
	}
	
	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public Map findDoctoServico(Map criteria){
		Map result = new HashedMap();
		DoctoServico doctoServico = doctoServicoService.findDoctoServicoById((Long)criteria.get("idDoctoServico"));
		if(doctoServico != null) {
			if(doctoServico.getFilialByIdFilialDestino() != null) {
				result.put("sgFilialDestino", doctoServico.getFilialByIdFilialDestino().getSgFilial());
				result.put("nmFantasiaFilialDestino", doctoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());
			}
			if(doctoServico.getServico() != null)
				result.put("servico", doctoServico.getServico().getDsServico());
			
			if(!criteria.get("cboDocumento").equals("MDA")) {
				result.put("tpFrete", conhecimentoService.findTpFreteByIdConhecimento((Long)criteria.get("idDoctoServico")).getDescriptionAsString());
			}
			
			if(doctoServico.getLocalizacaoMercadoria() != null) {
				String complementacao = "";
				if(doctoServico.getObComplementoLocalizacao() != null)
					complementacao = doctoServico.getObComplementoLocalizacao();
				result.put("localizacao", doctoServico.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria() + " " + complementacao);
			}
			result.put("volumes", doctoServico.getQtVolumes());
			
			Integer qtNotas = doctoServicoService.getRowCountDoctoServicoWithNFConhecimentoToInteger((Long)criteria.get("idDoctoServico"));
			if(qtNotas == null)
				qtNotas = 0;
			result.put("qtNotas", qtNotas);
			
			result.put("dhEmissao", JTFormatUtils.format(doctoServico.getDhEmissao()));
		}		
		return result;
	}
	
	public void storeFilialLocalizacaoDoctoServico(TypedFlatMap map) {

		Long idDoctoServico = (Long)map.get("idDoctoServico");
		String observacao = map.getString("observacao");

		getOcorrenciaDoctoFilialService().validateAlteracaoLocalizacaoDoctoServico(idDoctoServico, observacao);
		
		getOcorrenciaDoctoFilialService().storeFilialLocalizacaoDoctoServico(idDoctoServico, observacao);
	}

	public Map<String, String> updateDoctoServicoByManifestoViagem(TypedFlatMap map) {

		Map<String, String> doctoServiceInvalidoList;
		String observacao = map.getString("observacao");
		Long idManifestoViagemNacional = map.getLong("idManifestoViagemNacional");

		if(idManifestoViagemNacional == null) {
			throw new BusinessException("LMS-05427");
		}

		if (StringUtils.isBlank(observacao)) {
			throw new BusinessException("LMS-05375");
		}

		doctoServiceInvalidoList = new HashMap<>();
		ManifestoViagemNacional manifestoViagemNacional = manifestoViagemNacionalService.findById(idManifestoViagemNacional);

		if (manifestoViagemNacional == null) {
			throw new BusinessException("LMS-05426");
		}else {

			List<ManifestoNacionalCto> manifestoNacionalCtos = manifestoNacionalCtoService.findConhecimentos(manifestoViagemNacional.getIdManifestoViagemNacional());

			for (ManifestoNacionalCto manifestoNacionalCto : manifestoNacionalCtos) {

				Long idDoctoServico = manifestoNacionalCto.getConhecimento().getIdDoctoServico();

				if(Boolean.TRUE.equals(validaDoctoServico(idDoctoServico, doctoServiceInvalidoList))) {
					getOcorrenciaDoctoFilialService().storeFilialLocalizacaoDoctoServico(idDoctoServico, observacao);
				}
			}
		}

		return doctoServiceInvalidoList;
	}

	private Boolean validaDoctoServico(Long idDoctoServico, Map<String, String> doctoServiceInvalidoList) {

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

			Conhecimento conhecimento = (Conhecimento) doctoServico;
			String sgFilialOrigem = filialService.findSgFilialByIdFilial(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
			doctoServiceInvalidoList.put("CTE "+ sgFilialOrigem + " "+ conhecimento.getNrDoctoServico(),
					"LMS-05380: " + recursoMensagemService.findByChave("LMS-05380"));
			return false;
		}

		//1
		Boolean doctoNaoValido = doctoServico.getFilialLocalizacao().getIdFilial().compareTo(SessionUtils.getFilialSessao().getIdFilial()) == 0;

		//2
		if(Boolean.FALSE.equals(doctoNaoValido)) {

			LocalizacaoMercadoria localizacaoMercadoria = this.localizacaoMercadoriaService.findById(doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
			Short cdLocalizacao = localizacaoMercadoria.getCdLocalizacaoMercadoria();

			doctoNaoValido = cdLocalizacao.compareTo(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA) == 0;

		}

		//3
		if("CTE".equals(doctoServico.getTpDocumentoServico().getValue()) || "CTR".equals(doctoServico.getTpDocumentoServico().getValue())) {
			Conhecimento conhecimento = (Conhecimento) doctoServico;
			doctoNaoValido |= "CI".equals(conhecimento.getTpConhecimento()) || "CF".equals(conhecimento.getTpConhecimento());
		}

		if (doctoNaoValido) {

			Conhecimento conhecimento = (Conhecimento) doctoServico;
			String sgFilialOrigem = filialService.findSgFilialByIdFilial(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
			doctoServiceInvalidoList.put("CTE "+ sgFilialOrigem + " "+ conhecimento.getNrDoctoServico(),
					"LMS-05377: " + recursoMensagemService.findByChave("LMS-05377"));
			return false;
		}

		return true;

	}

	public List findLookupManifestoViagem(TypedFlatMap criteria){

		List manifestoViagemList = manifestoViagemNacionalService.findByNrManifestoOrigemByFilial(criteria.getInteger("nrManifestoOrigem"), criteria.getLong("idFilial"));
		if (manifestoViagemList.isEmpty()){
			return null;
		}
		else{
			TypedFlatMap map = new TypedFlatMap();
			List retorno = new ArrayList();

			ManifestoViagemNacional manifestoViagemNacional = (ManifestoViagemNacional) manifestoViagemList.get(0);
			map.put("idManifestoViagemNacional", manifestoViagemNacional.getIdManifestoViagemNacional());
			map.put("nrManifestoOrigem", manifestoViagemNacional.getNrManifestoOrigem());

			Filial filial = filialService.findByIdBasic(manifestoViagemNacional.getFilial().getIdFilial());
			map.put("sgFilialOrigem", filial.getSgFilial());
			map.put("idFilialOrigem", filial.getIdFilial());

			retorno.add(map);

			return retorno;
		}
	}
	
	public ResultSetPage findPaginated(Map criteria) {		
		YearMonthDay dhInicial = (YearMonthDay) criteria.get("periodoInicial");
		YearMonthDay dhFinal = (YearMonthDay) criteria.get("periodoFinal");
		
		if((Long)criteria.get("idDoctoServico") == null) {
			if (dhInicial != null && dhFinal != null) {
				if(dhFinal.isAfter(dhInicial.plusDays(60))) {
					throw new BusinessException("LMS-05372");
				}
			} else {
				throw new BusinessException("LMS-05371");
			}
		}
		
		ResultSetPage resultSetPage = getOcorrenciaDoctoFilialService().findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(prepareCriteria(criteria));
		List<OcorrenciaDoctoFilial> listOcorrencias = resultSetPage.getList();
		if(listOcorrencias != null) {
			List result = new ArrayList();
			for (OcorrenciaDoctoFilial ocorrencia : listOcorrencias) {
				Map map = new HashMap();
				map.put("idOcorrenciaDoctoFilial", ocorrencia.getIdOcorrenciaDoctoFilial());
				map.put("tpDoctoServico", ocorrencia.getDoctoServico().getTpDocumentoServico());
				map.put("sgFilialDoctoServico", filialService.findById(ocorrencia.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial()).getSgFilial());
				map.put("nrDoctoServico", ocorrencia.getDoctoServico().getNrDoctoServico());
				map.put("sgFilialDestino", ocorrencia.getFilialDoctoDestino().getSgFilial());
				map.put("sgFilialOcorrencia", ocorrencia.getFilialOcorrencia().getSgFilial());
				map.put("dataHoraOcorrencia", JTFormatUtils.format(ocorrencia.getDhOcorrencia(), "dd/MM/yyyy HH:mm"));
				map.put("usuarioOcorrencia", usuarioLMSService.findById(ocorrencia.getUsuarioOcorrencia().getIdUsuario()).getUsuarioADSM().getLogin());
				map.put("localizacao", ocorrencia.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
				map.put("observacao", ocorrencia.getObOcorrencia());
				
				result.add(map);
			}
			resultSetPage.setList(result);
		}
		
		return resultSetPage;
	}
	
	private TypedFlatMap prepareCriteria(Map criteria) {
		TypedFlatMap result = new TypedFlatMap();
		result.put("periodoInicial", criteria.get("periodoInicial"));
		result.put("periodoFinal", criteria.get("periodoFinal"));
		result.put("idFilialOcorrencia", criteria.get("idFilialOcorrencia"));
		result.put("idFilialDestino", criteria.get("idFilialDestino"));
		result.put("idFilialO", criteria.get("idFilialO"));
		result.put("idDoctoServico", criteria.get("idDoctoServico"));
		result.put("_currentPage", criteria.get("_currentPage"));
		result.put("_order", criteria.get("_order"));
		result.put("_pageSize", criteria.get("_pageSize"));
		return result;
	}
	
	public String exportar(Map parameters) throws Exception{
		
		if((Long)parameters.get("idDoctoServico") == null) {
			YearMonthDay periodoInicial = (YearMonthDay)parameters.get("periodoInicial");
			YearMonthDay periodoFinal = (YearMonthDay)parameters.get("periodoFinal");
			
			if (periodoInicial != null && periodoFinal != null) {
				if(periodoFinal.isAfter(periodoInicial.plusDays(60))) {
					throw new BusinessException("LMS-05372");
				}
			} else {
				throw new BusinessException("LMS-05371");
			}
		}
		
		return reportExecutionManager.generateReportLocator(manterFilialLocalizacaoDoctoServicoService, parameters);
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public ManterFilialLocalizacaoDoctoServicoService getManterFilialLocalizacaoDoctoServicoService() {
		return manterFilialLocalizacaoDoctoServicoService;
	}

	public void setManterFilialLocalizacaoDoctoServicoService(
			ManterFilialLocalizacaoDoctoServicoService manterFilialLocalizacaoDoctoServicoService) {
		this.manterFilialLocalizacaoDoctoServicoService = manterFilialLocalizacaoDoctoServicoService;
	}
	
	public OcorrenciaDoctoFilialService getOcorrenciaDoctoFilialService() {
		return (OcorrenciaDoctoFilialService) getDefaultService();
	}

	public void setOcorrenciaDoctoFilialService(OcorrenciaDoctoFilialService ocorrenciaDoctoFilialService) {
		setDefaultService(ocorrenciaDoctoFilialService);
	}

	public ManifestoViagemNacionalService getManifestoViagemNacionalService() {
		return this.manifestoViagemNacionalService;
	}

	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}

	public ManifestoNacionalCtoService getManifestoNacionalCtoService() {
		return manifestoNacionalCtoService;
	}

	public void setManifestoNacionalCtoService(ManifestoNacionalCtoService manifestoNacionalCtoService) {
		this.manifestoNacionalCtoService = manifestoNacionalCtoService;
	}

	public MdaService getMdaService() {
		return mdaService;
	}

	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}

	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public RecursoMensagemService getRecursoMensagemService() {
		return recursoMensagemService;
	}

	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}
}
