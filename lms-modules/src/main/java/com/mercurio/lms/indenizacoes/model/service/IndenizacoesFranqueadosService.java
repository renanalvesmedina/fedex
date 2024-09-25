package com.mercurio.lms.indenizacoes.model.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.AnexoLancamentoFranqueado;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.service.AnexoLancamentoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.ContaContabilFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FranquiaService;
import com.mercurio.lms.franqueados.model.service.LancamentoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoFranqueado;
import com.mercurio.lms.indenizacoes.model.dao.ReciboIndenizacaoFranqueadoDAO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class IndenizacoesFranqueadosService extends CrudService<ReciboIndenizacaoFranqueado, Long>{
	
	static final String QUEBRA_PAGINA = "\n";
	final String anexoIndenizacao = "Anexo da Indenização";

	private Logger log = LogManager.getLogger(this.getClass());
	private WorkflowPendenciaService workflowPendenciaService;
	private ContaContabilFranqueadoService contaContabilFranqueadoService;
	private LancamentoFranqueadoService lancamentoFranqueadoService;
	private ConfiguracoesFacade configuracoesFacade;
	private FranquiaService franquiaService;
	private DomainValueService domainValueService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private AnexoLancamentoFranqueadoService anexoLancamentoFranqueadoService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String executeWorkflow(List<Long> listIds, List<String> listTpSituacao) {
		Iterator<String> situacaoIt = listTpSituacao.iterator();
		for (Long id : listIds) {
			String status = situacaoIt.next();
			ReciboIndenizacaoFranqueado indenizacao = (ReciboIndenizacaoFranqueado) super.findById(id);
			
			if(status.equals(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_REPROVADO)){
				indenizacao.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_REPROVADO));
				store(indenizacao);
			} else if(status.equals(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO)){
				indenizacao.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO));
				store(indenizacao);
				
				String tipoConta = "";
				if(indenizacao.getTpMotivoIndenizacao().getValue().equals(ConstantesFranqueado.TP_MOTIVO_AV)){
					tipoConta =ConstantesFranqueado.CONTA_CONTABIL_IA;
				} else if(indenizacao.getTpMotivoIndenizacao().getValue().equals(ConstantesFranqueado.TP_MOTIVO_FA)){
					tipoConta = ConstantesFranqueado.CONTA_CONTABIL_IF;
				} else if(indenizacao.getTpMotivoIndenizacao().getValue().equals(ConstantesFranqueado.TP_MOTIVO_OU)){
					tipoConta = ConstantesFranqueado.CONTA_CONTABIL_IO;
				} 
				
				ContaContabilFranqueado contaContabil = contaContabilFranqueadoService.findContaByTipo(indenizacao.getDtCompetencia(), tipoConta, ConstantesFranqueado.TIPO_LANCAMENTO_DEBITO);	
				if(contaContabil != null){
					
					for (int i = 0; i < indenizacao.getNrParcelas().intValue(); i++) {
						LancamentoFranqueado lancamentoFranqueado = new LancamentoFranqueado();
						lancamentoFranqueado.setContaContabilFranqueado((ContaContabilFranqueado) contaContabil);
							
						lancamentoFranqueado.setFranquia(indenizacao.getFranquia());
						lancamentoFranqueado.setDtCompetencia(indenizacao.getDtCompetencia().plusMonths(i));
							
						BigDecimal pcIndenizacao = indenizacao.getNrPercentualIndenizacao().divide(BigDecimal.valueOf(100));
						BigDecimal valorLancamento = indenizacao.getNrValorIndenizado().multiply(pcIndenizacao).divide(indenizacao.getNrParcelas(),2,FranqueadoUtils.ROUND_TYPE);
						lancamentoFranqueado.setVlLancamento(valorLancamento);
						
						lancamentoFranqueado.setTpSituacaoPendencia(new DomainValue(ConstantesFranqueado.TP_SITUACAO_PENDENCIA_APROVADO));
			
						String sgFilial = indenizacao.getReciboIndenizacao().getFilial().getSgFilial();
						Integer nrReciboIndenizacao = indenizacao.getReciboIndenizacao().getNrReciboIndenizacao();
							
						//TODO-franqueados passar para constante 
						String formatted = String.format("BAIXA REFERENTE AO RECIBO DE INDENIZAÇÃO %s %s - PARCELA %s DE %s",sgFilial,nrReciboIndenizacao,i+1,indenizacao.getNrParcelas());
			
						lancamentoFranqueado.setDsLancamento(formatted);
						lancamentoFranqueado.setObLancamento(indenizacao.getDsIndenizacao());
		
						lancamentoFranqueado.setIdLancamentoFrq((Long)lancamentoFranqueadoService.storeAutomatico(lancamentoFranqueado));
						
						if (i == 0){
						    BigDecimal percentualIndenizacao = indenizacao.getNrPercentualIndenizacao().divide(new BigDecimal(100));
						    lancamentoFranqueado.setVlContabil(indenizacao.getNrValorIndenizado().multiply(percentualIndenizacao));
						}else{
						    lancamentoFranqueado.setVlContabil(BigDecimal.ZERO);
						}
						
						if( indenizacao.getDcArquivo() != null ){
							AnexoLancamentoFranqueado anexoLancamentoFranqueado = new AnexoLancamentoFranqueado();
							anexoLancamentoFranqueado.setLancamento(lancamentoFranqueado);
							anexoLancamentoFranqueado.setDsAnexo(anexoIndenizacao);
							anexoLancamentoFranqueado.setDcArquivo(indenizacao.getDcArquivo());
							anexoLancamentoFranqueado.setVersao(0);
							anexoLancamentoFranqueadoService.store(anexoLancamentoFranqueado);
						}
					}
				}
			}
		}
		return null;
	}
	
	public Pendencia generatePendencia(ReciboIndenizacaoFranqueado reciboIndenizacaoFranqueado){
		
		Pendencia pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacaoFranqueado.getFranquia().getIdFranquia(), 
				                                          ConstantesWorkflow.NR4603_RECIBO_INDENIZACAO_FRANQUEADO, 
				                                          reciboIndenizacaoFranqueado.getIdReciboIndenizacaoFrq(), 
				                                          gerarDescricao(reciboIndenizacaoFranqueado), 
				                                          JTDateTimeUtils.getDataHoraAtual());
		return pendencia;
	}
	
	private String gerarDescricao(ReciboIndenizacaoFranqueado reciboIndenizacaoFranqueado) {
		StringBuilder descricao = new StringBuilder();
		descricao.append(configuracoesFacade.getMensagem("franquia")).append(": ").append( reciboIndenizacaoFranqueado.getFranquia().getFilial().getSgFilial() );
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("rim")).append(": ").append( reciboIndenizacaoFranqueado.getReciboIndenizacao().getFilial().getSgFilial() )
			.append(" ").append(reciboIndenizacaoFranqueado.getReciboIndenizacao().getNrReciboIndenizacao());
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("competencia")).append(": ").append( reciboIndenizacaoFranqueado.getDtCompetencia().toString("MM/yyyy") );
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("valor")).append(": ").append( reciboIndenizacaoFranqueado.getNrValorIndenizado());
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("parcelas")).append(": ").append( reciboIndenizacaoFranqueado.getNrParcelas().intValue());
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("percentual")).append(": ").append( reciboIndenizacaoFranqueado.getNrPercentualIndenizacao().intValue() );
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("motivoIndenizacao")).append(": ").append( reciboIndenizacaoFranqueado.getTpMotivoIndenizacao().getDescription());
		descricao.append(QUEBRA_PAGINA);
		descricao.append(configuracoesFacade.getMensagem("descricao")).append(": ").append( reciboIndenizacaoFranqueado.getDsIndenizacao());

		

		return descricao.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findPaginatedAsTypedMap(TypedFlatMap map) {
		ResultSetPage result = super.findPaginated(map);
		List lacamentos = new ArrayList();
		
		if(result != null){
			for(int i=0; i< result.getList().size(); i++) {
				ReciboIndenizacaoFranqueado recibo = (ReciboIndenizacaoFranqueado) result.getList().get(i);
				
				TypedFlatMap lacamento = new TypedFlatMap();
				
				lacamento.put("sgFilial", recibo.getFranquia().getFilial().getSgFilial());
				lacamento.put("dtCompetencia", recibo.getDtCompetencia());
				lacamento.put("nrPercentualIndenizacao", recibo.getNrPercentualIndenizacao());
				lacamento.put("nrParcelas", recibo.getNrParcelas());
				lacamento.put("tpSituacaoPendencia", recibo.getTpSituacaoPendencia().getDescription());
				lacamento.put("nrValorIndenizado", recibo.getNrValorIndenizado());
				
				lacamento.putAll(map);
				
				lacamentos.add(lacamento);
			}
		}
		return lacamentos;
	}
	
	@SuppressWarnings("unchecked")
	public List<TypedFlatMap> findIndenizacoesFranqueadas(TypedFlatMap map) {
		List<Object[]> lista = getReciboIndenizacaoFranqueadoDAO().findIndenizacoesFranqueadas(map);

		List<DomainValue> domainMotivoIndenizacaoFrq = domainValueService.findDomainValues(ConstantesFranqueado.DM_MOTIVO_INDENIZACAO_FRQ);
		List<DomainValue> domainStatusWorkflow = domainValueService.findDomainValues(ConstantesFranqueado.DM_STATUS_WORKFLOW);
    	List<TypedFlatMap> result = new LinkedList<TypedFlatMap>();
    	if(lista != null){
    		for (Object[] item : lista) {
    			TypedFlatMap mapItem = mountIndenizacaoFranqueado(item,domainMotivoIndenizacaoFrq, domainStatusWorkflow);
    			
    			result.add(mapItem);
    		}
    	}
    	
    	return result;
	}

	private TypedFlatMap mountIndenizacaoFranqueado(Object[] item, List<DomainValue> domainMotivoIndenizacaoFrq, List<DomainValue> domainStatusWorkflow) {
		TypedFlatMap mapItem = new TypedFlatMap();
		

		mapItem.put("idReciboIndenizacao", item[0]);
		mapItem.put("sgFilial", item[1]);
		mapItem.put("nrReciboIndenizacao", item[1]+" "+item[2]);
		mapItem.put("sgFranquia", item[3]);
		mapItem.put("nrDocumento", item[4]);
		DomainValue domainByValue = getDomainByValue((String) item[5],domainMotivoIndenizacaoFrq);
		mapItem.put("tpMotivoIndenizacao",domainByValue.getDescriptionAsString());
		mapItem.put("tpMotivoIndenizacaoDomainValue", domainByValue);
		mapItem.put("nrDocumentos", item[6]);
		mapItem.put("vlTotalIndenizacao", item[7]);
		mapItem.put("idFranquia", item[8]);
		
		if(item[9] != null){
			DomainValue domainSituacaoPendenciaByValue = getDomainByValue((String) item[9],domainStatusWorkflow);
			mapItem.put("tpSituacaoPendencia",domainSituacaoPendenciaByValue.getDescriptionAsString());
			mapItem.put("tpSituacaoPendenciaDomainValue", domainSituacaoPendenciaByValue);
		}else{
			mapItem.put("tpSituacaoPendencia", item[9]);
		}
		
		return mapItem;
	}
	
	 private DomainValue getDomainByValue(String tpMotivoIndenizacaoFrq,List<DomainValue> domains) {
	    for (DomainValue domainValue : domains) {
			if(domainValue.getValue().equals(tpMotivoIndenizacaoFrq)){
				return domainValue;
			}
		}
		return null;
	 }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getComboBoxNrParcelas() {
    	ArrayList list = new ArrayList();
    	
    	for (int i = 1; i <= 10; i++) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("nrParcelas", String.valueOf(i));
    		list.add(map);
		}
    	
    	return list;
    }
    
    public void storeDescontos(ArrayList<TypedFlatMap> list) {
		for (TypedFlatMap item : list) {
			if (item.get("pcIndenizacao") != null && item.get("nrParcelas") != null) {
				
				String idReciboIndenizacao = item.get("idReciboIndenizacao").toString();
				String idFranquia = item.get("idFranquia").toString();
				YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMesAtual();
				String pcIndenizacao = item.get("pcIndenizacao").toString();
				Long tpMotivoIndenizacaoDomainValueId = Long.parseLong(item.get("tpMotivoIndenizacaoDomainValue.id").toString());
				String nrParcelas = item.get("nrParcelas").toString();
				String vlIndenizado = item.get("vlTotalIndenizacao").toString();
				String dsIndenizacao = item.getString("dsIndenizacao");
				
				ReciboIndenizacaoFranqueado reciboIndenizacaoFranqueado = new ReciboIndenizacaoFranqueado();
				reciboIndenizacaoFranqueado.setReciboIndenizacao(reciboIndenizacaoService.findReciboIndenizacaoById(Long.parseLong(idReciboIndenizacao)));
				reciboIndenizacaoFranqueado.setFranquia(franquiaService.findById(Long.parseLong(idFranquia)));
				reciboIndenizacaoFranqueado.setDtCompetencia(new YearMonthDay(dtCompetenciaInicial));
				reciboIndenizacaoFranqueado.setNrPercentualIndenizacao(BigDecimalUtils.getBigDecimal(pcIndenizacao.replace(",", ".")));
				reciboIndenizacaoFranqueado.setTpMotivoIndenizacao(domainValueService.findById(tpMotivoIndenizacaoDomainValueId));
				reciboIndenizacaoFranqueado.setNrParcelas(BigDecimalUtils.getBigDecimal(nrParcelas));
				reciboIndenizacaoFranqueado.setNrValorIndenizado(BigDecimalUtils.getBigDecimal(vlIndenizado.replace(",", ".")));
				reciboIndenizacaoFranqueado.setDsIndenizacao(dsIndenizacao);

				if(item.getString("hiddenAnexo") != null){
					try{
						reciboIndenizacaoFranqueado.setDcArquivo(Base64Util.decode(item.getString("hiddenAnexo")));
					} catch (IOException e) {
						log.error(e);
						throw new InfrastructureException(e.getMessage());
					}
				}
				
				super.store(reciboIndenizacaoFranqueado);
				
				Pendencia pendencia = generatePendencia(reciboIndenizacaoFranqueado);
				reciboIndenizacaoFranqueado.setPendencia(pendencia);
				
				updateReciboIndenizacaoFranqueado(reciboIndenizacaoFranqueado);
			}
		}
	}
    
    public void updateReciboIndenizacaoFranqueado(ReciboIndenizacaoFranqueado reciboIndenizacaoFranqueado) {
    	super.store(reciboIndenizacaoFranqueado);
    }
    
    public List<Map<String,Object>> findRelatorioIndezacoesFranqueado(Map<String,Object> parameters) {
    	return getReciboIndenizacaoFranqueadoDAO().findRelatorioIndezacoesFranqueado(parameters);
	}
    
    private ReciboIndenizacaoFranqueadoDAO getReciboIndenizacaoFranqueadoDAO() {
        return (ReciboIndenizacaoFranqueadoDAO) getDao();
    }

	public void setContaContabilFranqueadoService(
			ContaContabilFranqueadoService contaContabilFranqueadoService) {
		this.contaContabilFranqueadoService = contaContabilFranqueadoService;
	}
	
	public void setLancamentoFranqueadoService(
			LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.lancamentoFranqueadoService = lancamentoFranqueadoService;
	}
	
	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setReciboIndenizacaoFranqueadoDAO(ReciboIndenizacaoFranqueadoDAO reciboIndenizacaoFranqueadoDAO) {
		setDao(reciboIndenizacaoFranqueadoDAO);
	}

	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

	public void setAnexoLancamentoFranqueadoService(
			AnexoLancamentoFranqueadoService anexoLancamentoFranqueadoService) {
		this.anexoLancamentoFranqueadoService = anexoLancamentoFranqueadoService;
	}

}
