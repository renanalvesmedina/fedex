package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.integration.convert.Sigla2FilialConverter;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdReciboNumeracao;
import com.mercurio.lms.ppd.model.dao.PpdReciboDAO;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.DisposicaoService;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class PpdReciboService extends CrudService<PpdRecibo, Long> {
	private FilialService filialService;	
	private PpdAtendimentoFilialService atendimentoFilialService;			
	private ConfiguracoesFacade configuracoesFacade;
	private PpdReciboNumeracaoService reciboNumeracaoService;
	private ConhecimentoService conhecimentoService;
	private PpdCorporativoService corporativoService;
	private DoctoServicoService doctoServicoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private NaoConformidadeService naoConformidadeService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private DomainValueService domainValueService;
	private DisposicaoService disposicaoService;
	
	private static final String tpStatusNaoConformidade = "CAN";
	
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}

    public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
    
    public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
    
    public void setDisposicaoService(DisposicaoService disposicaoService) {
		this.disposicaoService = disposicaoService;
	}
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setCorporativoService(PpdCorporativoService corporativoService) {
		this.corporativoService = corporativoService;
	}

	public Serializable storeImportacaoGrm(PpdRecibo bean) {
		return super.store(bean);
	}
	
	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public Serializable store(PpdRecibo bean) {

		//Valida filiais de composição do débito	
		boolean validaFiliaisComp = true;
		if(bean.getSgFilialComp1() != null && !bean.getSgFilialComp1().trim().equals("")) {
			Filial filialComp1 = filialService.findFilialBySgFilialLegado(bean.getSgFilialComp1());
			if(filialComp1 == null) {
				validaFiliaisComp = false;
			}
		}
		if(bean.getSgFilialComp2() != null && !bean.getSgFilialComp2().trim().equals("")) {
			Filial filialComp2 = filialService.findFilialBySgFilialLegado(bean.getSgFilialComp2());
			if(filialComp2 == null) {
				validaFiliaisComp = false;
			}
		}
		if(bean.getSgFilialComp3() != null && !bean.getSgFilialComp3().trim().equals("")) {
			Filial filialComp3 = filialService.findFilialBySgFilialLegado(bean.getSgFilialComp3());
			if(filialComp3 == null) {
				validaFiliaisComp = false;
			}
		}
		if(!validaFiliaisComp)
			throw new BusinessException("PPD-02026",
					new String[] {configuracoesFacade.getMensagem("composicaoDebito")}); 		
		//Valida filiais de localização da ocorrência		
		boolean validaFiliaisOcorrencia = true;
		if(bean.getSgFilialLocal1() != null && !bean.getSgFilialLocal1().trim().equals("")) {
			Filial filialLocal1 = filialService.findFilialPessoaBySgFilial(bean.getSgFilialLocal1(),true);
			if(filialLocal1 == null) {
				validaFiliaisOcorrencia = false;
			}
		}
		if(bean.getSgFilialLocal2() != null && !bean.getSgFilialLocal2().trim().equals("")) {
			Filial filialLocal2 = filialService.findFilialPessoaBySgFilial(bean.getSgFilialLocal2(),true);
			if(filialLocal2 == null) {
				validaFiliaisOcorrencia = false;
			}
		}
		if(!validaFiliaisOcorrencia)
			throw new BusinessException("PPD-02026", 
					new String[] {configuracoesFacade.getMensagem("localizacaoOcorrencia")});		
		//Busca o próximo número de recibo de acordo com a filial
		if(bean.getNrRecibo() == null) {
			Filial filial = bean.getFilial();
			PpdReciboNumeracao numeracao = reciboNumeracaoService.findByIdFilial(filial.getIdFilial());
			Long maxNrRecibo = configuracoesFacade.incrementaParametroSequencial(bean.getFilial().getIdFilial(), "NR_REC_INDENIZACAO", true);
			
			if(numeracao == null) {
				numeracao = new PpdReciboNumeracao();
				numeracao.setFilial(filial);
			}
			numeracao.setNrRecibo(maxNrRecibo);	
			reciboNumeracaoService.store(numeracao);
			bean.setNrRecibo(numeracao.getNrRecibo());
		}
		
		//Localizar documento de serviço no sistema LMS:
		if(bean.getIdRecibo() == null){
			storeLmsData(bean);
		}
		
		//Grava o recibo
		return super.store(bean);
	}
	
	/**
     * Faz a integração com o LMS
     * 
     * @param recibo
     */
    private void storeLmsData(PpdRecibo recibo) {
    	Sigla2FilialConverter filialConverter = new Sigla2FilialConverter();
    	Map idFilialLegado = filialConverter.getMapFilialSigla(); 
		String sgFilialOrigemLeg = recibo.getSgFilialOrigem();
    	Long idFilialOrigemLMS = (Long) idFilialLegado.get(sgFilialOrigemLeg); 

    	DoctoServico doctoServico = findDoctoServicoByRecibo(recibo);
		
		/*
		 * Caso conhecimento não for encontrado no LMS, emitir mensagem: PPD-02002
		 */
		if (null == doctoServico) {
			throw new BusinessException("PPD-02002");
		}
		
		/*
		 * caso código da localização do documento (docto_servico -> localizacao_mercadoria.cd_localizacao_mercadoria) 
		 * for diferente de 39 (Cliente indenizado) e 70 (Em processo indenizatório):
		 * 	1.	Criar evento para o documento de serviço utilizando a rotina 10.05.01.07 Incluir Eventos Rastreabilidade 
		 * Internacional (IncluirEventosRastreabilidadeInternacionalService. executeInsereEventos), passando os parâmetros:
		 * 		1.	tipo de documento: ‘CRT’
		 * 		2.	número documento: sigla da filial (sigla de 3 letras) concatenado com o número de origem do documento
		 * 		3.	código do evento: 35
		 * 		4.	filial: filial do usuário da sessão
		 * 		5.	data do evento: data atual
		 * 		6.	observação: null
		 * 
		 */
			
		Short cdLocalizacaoMercadoria = null;
		
		if (doctoServico != null) {
			cdLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria() == null ? null : doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
		}

		DateTime now = JTDateTimeUtils.getDataHoraAtual();
		//inserir ocorrenciaDoctoServico
		insereOcorrenciaDoctoServico(Short.valueOf("97"), doctoServico, now);
		
		if (null == cdLocalizacaoMercadoria || (cdLocalizacaoMercadoria.intValue() != 39 && cdLocalizacaoMercadoria.intValue() != 70 )) {
			Filial filial = filialService.findById(idFilialOrigemLMS);
			String nrDocumento = filial.getSgFilial() + " " + doctoServico.getNrDoctoServico();
			Short cdEvento = 35;

			Usuario usuario = SessionUtils.getUsuarioLogado();
			Filial filialSessao = SessionUtils.getFilialSessao();

			incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, doctoServico.getIdDoctoServico(), filialSessao.getIdFilial(), 
					nrDocumento, now, null, null, doctoServico.getTpDocumentoServico().getValue());
		}
		
		/*	2.	Localizar NAO_CONFORMIDADE onde:
		 * 		1.	ID_FILIAL == id encontado no ítem 1
		 * 		2.	NR_NAO_CONFORMIDADE == PPD_RECIBOS.NR_RNC
		 * 		3.  TP_STATUS_NAO_CONFOIRMIDADE != 'CAN' 
		 */
		NaoConformidade naoConformidade = naoConformidadeService.findByIdDoctoServicoAndStatusNaoConformidade(doctoServico.getIdDoctoServico(), tpStatusNaoConformidade);
			
		/*
		 * Se não conformidade não for encontrada, emitir mensagem: PPD-02001
		 */
		if(null == naoConformidade){
			if(recibo.getTpIndenizacao() != null && ("1".equals(recibo.getTpIndenizacao().getValue()) || "2".equals(recibo.getTpIndenizacao().getValue())) ){
			throw new BusinessException("PPD-02001"); 
		}
		}

		/*
		 *	Caso contrário:
		 *		1.	Alterar NAO_CONFORMIDADE.TP_STATUS_NAO_CONFORMIDADADE para 'ROI'
		 *		2.  Inserir um registro de DISPOSICAO 
		 */
		else {
			naoConformidadeService.updateTpStatusNaoConformidade(naoConformidade.getNrNaoConformidade(), naoConformidade.getFilial().getIdFilial(), "ROI");
		
			
			List<OcorrenciaNaoConformidade> ocorrencias =  ocorrenciaNaoConformidadeService.findOcorrenciasByIdNaoConformidade(naoConformidade.getIdNaoConformidade());
				
			for(OcorrenciaNaoConformidade ocorrencia: ocorrencias) {
				String tpStatusOcorrencia = ocorrencia.getTpStatusOcorrenciaNc().getValue();
				
				ocorrencia.setTpStatusOcorrenciaNc(domainValueService.findDomainValueByValue("DM_STATUS_OCORRENCIA_NC", "F"));
				ocorrenciaNaoConformidadeService.store(ocorrencia);
				
				if(!tpStatusOcorrencia.equals("F")){
					disposicaoService.insertDisposicao(recibo, ocorrencia);
				}
			}
		}
	}
	    
	public PpdRecibo findById(Long id) {
		return getDAO().findById(id);
	}
	
	public ResultSetPage<PpdRecibo> findPaginated(PaginatedQuery paginatedQuery) {		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		
		//Filtro pelo grupo de atendimento
		if(criteria.get("idGrupoAtendimento") != null) {
			paginatedQuery.addCriteria("idsFiliais", 
					atendimentoFilialService.findIdsFiliaisByIdGrupoAtendimento((Long)criteria.get("idGrupoAtendimento")));
		}			
		
		return getDAO().findPaginated(paginatedQuery);
	}		
	
	public List<PpdRecibo> findExcel(Map<String,Object> criteria) {						
		//Filtro pelo grupo de atendimento
		if(criteria.get("idGrupoAtendimento") != null) {
			criteria.put("idsFiliais", 
					atendimentoFilialService.findIdsFiliaisByIdGrupoAtendimento((Long)criteria.get("idGrupoAtendimento")));
		}					
		return getDAO().findExcel(criteria);
	}
	
	public Integer getRowCount(Map criteria) {
		return getDAO().getRowCount(criteria);
	}
	
	public void removeById(Long id) {				
		super.removeById(id);
	}
	
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}	
	
	public PpdRecibo findByRecibo(Long idFilialRecibo, Long nrRecibo, YearMonthDay dtRecibo) {
		return getDAO().findByRecibo(idFilialRecibo, nrRecibo, dtRecibo);
	}	
	
	public PpdRecibo findByRecibo(Long idFilialRecibo, Long nrRecibo, Long nrCTRC) {
		return getDAO().findByRecibo(idFilialRecibo, nrRecibo, nrCTRC);
	}	

	public List<PpdRecibo> findByConhecimento(String sgFilialOrigem, Long nrCtrc, YearMonthDay dtCtrc) {
		return getDAO().findByConhecimento(sgFilialOrigem, nrCtrc, dtCtrc);
	}	
	
	public List<PpdRecibo> findByIdConhecimento(Long idConhecimento) {
		Conhecimento ctrc = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);
		
		if(ctrc == null)
			return null;
				
		String sgFilial = filialService.findSgFilialLegadoByIdFilial(ctrc.getFilialByIdFilialOrigem().getIdFilial());
		String nrCtrcStr = ctrc.getNrDoctoServico().toString();
		if(nrCtrcStr.length() > 6) {
			nrCtrcStr = nrCtrcStr.substring(nrCtrcStr.length() - 6);
		}			
		Long nrCtrc = Long.parseLong(nrCtrcStr);
		YearMonthDay dtCtrc = ctrc.getDhEmissao().toYearMonthDay();			
		return this.findByConhecimento(sgFilial, nrCtrc, dtCtrc);			
	}
	
	//Gets e Sets do DAO e das Services auxiliares
	public void setDAO(PpdReciboDAO dao) {
		setDao(dao);
	}
	
	private PpdReciboDAO getDAO() {
		return (PpdReciboDAO) getDao();		
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setAtendimentoFilialService(
			PpdAtendimentoFilialService atendimentoFilialService) {
		this.atendimentoFilialService = atendimentoFilialService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setReciboNumeracaoService(
			PpdReciboNumeracaoService reciboNumeracaoService) {
		this.reciboNumeracaoService = reciboNumeracaoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}		
	
    public List<PpdRecibo> findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto( String tpStatusIndenizacao,
			YearMonthDay dtPagamentoEfetuado, Boolean blEmailPagto){
    	return getDAO().findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto(
    			tpStatusIndenizacao, dtPagamentoEfetuado, blEmailPagto);
    }
    
    public DoctoServico findDoctoServicoByRecibo(PpdRecibo recibo) {

		Long nrDoctoServico = null;
		
		Map<String,Object> dadosCtrc = corporativoService.findConhecimento(recibo.getSgFilialOrigem(), recibo.getNrCtrc(), recibo.getDtEmissaoCtrc());
		
		if(dadosCtrc != null){
			nrDoctoServico = LongUtils.getLong((BigDecimal) dadosCtrc.get("nrCtrcCompleto"));
		}
		
		Long idDoctoServico = corporativoService.findIdDoctoServico(recibo.getSgFilialOrigem(), nrDoctoServico);
		
		DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
		
		return doctoServico;
    }

    public void insereOcorrenciaDoctoServico(Short cdOcorrencia, DoctoServico doctoServico, DateTime datahora)
    {
    	OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(cdOcorrencia);
    	int contOcorr = ocorrenciaDoctoServicoService.findCountOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());

    	//só executa registro de ocorrência se ocorr for de bloqueio e não existir ocorr em aberto ou se ocorr for de liberação e só existir 1 ocorr em aberto
    	if (((ocorrencia.getTpOcorrencia().getValue().equals("B")) && (contOcorr == 0)) || ((ocorrencia.getTpOcorrencia().getValue().equals("L")) && (contOcorr == 1)))
    	{
    		boolean executaOcorrencia = false;
    		
    		if (ocorrencia.getTpOcorrencia().getValue().equals("L"))
    		{
    			OcorrenciaDoctoServico ocorrenciaDoctoServicoBuscado = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
    			
    			//só libera ocorrência de bloqueio de indenização (97)
    			if (ocorrenciaDoctoServicoBuscado.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia().equals(Short.valueOf("97")))
    			{
    				executaOcorrencia = true;
    			}
    		}
    		else
    		{
    			executaOcorrencia = true;
    		}
    		
    		if (executaOcorrencia)
    		{
    			ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrencia, doctoServico, datahora);
    		}
    	}
    }
}