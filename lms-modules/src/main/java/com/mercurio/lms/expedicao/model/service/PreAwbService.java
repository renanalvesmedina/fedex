/**
 * 
 */
package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.dao.AwbDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.service.TarifaSpotService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de servico para CRUD.
 * 
 * @author Luis Carlos Poletto
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.preAWBService"
 */
public class PreAwbService extends CrudService<Awb, Long> {
	
	private static final String TP_COLETA_ENTREGA_DIRETA_AEROPORTO = "DA";
	
	private EmitirDocumentoService emitirDocumentoService;
	private CtoAwbService ctoAwbService;
	private DimensaoService dimensaoService;
	private TarifaSpotService tarifaSpotService;
	private WorkflowPendenciaService workFlowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ClienteService clienteService;
	private AwbService awbService;
	private PedidoColetaService pedidoColetaService;
	private ManifestoEntregaService manifestoEntregaService;

	public void validatePreAwb(Awb awb) {
		List<CtoAwb> ctoAwbs = awb.getCtoAwbs();
		boolean existeAlgumConhecimentosComAwb = false;
		
		if (ctoAwbs != null && !ctoAwbs.isEmpty()){
			for (CtoAwb ctoAwb : ctoAwbs) {
				if (ctoAwb.getAwb() != null){
					existeAlgumConhecimentosComAwb = true;
				}						
			}
		}

		if (!existeAlgumConhecimentosComAwb){
			throw new BusinessException("LMS-04175");
		}

		boolean hasTomadorDoServicoComSeguroDiferenciado = false;

		for(Iterator iter = ctoAwbs.iterator(); iter.hasNext();){
			CtoAwb ctoAwb = (CtoAwb) iter.next();
			hasTomadorDoServicoComSeguroDiferenciado = clienteService.findTomadorDoServicoComSeguroDiferenciado(ctoAwb.getConhecimento().getIdDoctoServico());
			if(hasTomadorDoServicoComSeguroDiferenciado && (awb.getNrLvSeguro() == null || "".equals(awb.getNrLvSeguro()))){
				throw new BusinessException("LMS-04442");
			}
		}
		
		if(awb.getPsCubado() == null) {
			awb.setPsCubado(awb.getPsTotal());
		}
	}

	@Override
	protected Awb beforeInsert(Awb bean) {
		Awb awb = bean;

		//Gera numero do awb
		emitirDocumentoService.generateProximoNumero(awb, ConstantesExpedicao.PRE_AWB);
		awb.setDhDigitacao(JTDateTimeUtils.getDataHoraAtual());

		return super.beforeInsert(bean);
	}

	public void storePreAwb(Awb awb) {
		if(awb.getCiaFilialMercurio() == null) {
			throw new BusinessException("LMS-04182");
		}
		prepareCollectionsToStore(awb);

		if(awb.getTarifaSpot() != null){
			tarifaSpotService.generateProximoNrUtilizacoes(awb.getTarifaSpot().getIdTarifaSpot());
		}
		this.store(awb);
		
		//LMS-7766:executar manifesto de entrega automatico 
		if(findTpColetaByCtos(awb)){
			this.executeManifestoEntregaByControleCargaConhecimento(awb.getCtoAwbs()); 
		}

		this.storeCtosAwbSemModalAereoEComClienteTomadorNaoLiberado(awb);
	}
	
	private boolean findTpColetaByCtos(Awb awb) {
		if(!awb.getCtoAwbs().isEmpty()){
			Conhecimento conhecimento = awb.getCtoAwbs().get(0).getConhecimento();
			PedidoColeta pedidoColeta = pedidoColetaService.findPedidoColetaByIdDoctoServicoAndTpColeta(conhecimento.getIdDoctoServico(), TP_COLETA_ENTREGA_DIRETA_AEROPORTO);
			return pedidoColeta != null && pedidoColeta.getIdPedidoColeta() != null;
		}
		return false;
	}

	private void executeManifestoEntregaByControleCargaConhecimento(List<CtoAwb> ctoAwbs) {
		List<DoctoServico> docs = getDoctosServicoFromCto(ctoAwbs);
		getManifestoEntregaService().generateManifestoAutomaticoColetaComEntregaDiretaAeroporto(docs);
	}
	
	private List<DoctoServico> getDoctosServicoFromCto(List<CtoAwb> ctoAwbs) {
		List<DoctoServico> docs = new ArrayList<DoctoServico>();
		for (CtoAwb ctoAwb : ctoAwbs){
			docs.add(ctoAwb.getConhecimento());
		}
		return docs;
	}

	private void prepareCollectionsToStore(Awb awb) {
		List<CtoAwb> ctoAwbs = awb.getCtoAwbs();
		if (ctoAwbs != null && !ctoAwbs.isEmpty()) {
			for (Iterator iter = ctoAwbs.iterator(); iter.hasNext();) {
				CtoAwb ctoAwb = (CtoAwb) iter.next();
				if(ctoAwb.getAwb() == null) {
					ctoAwbService.removeById(ctoAwb.getIdCtoAwb());
					iter.remove();
				} else if(CompareUtils.le(ctoAwb.getIdCtoAwb(), LongUtils.ZERO)) {
					ctoAwb.setIdCtoAwb(null);
					ctoAwb.setAwb(awb);
				}
			}
		}

		List<Dimensao> dimensoes = awb.getDimensoes();
		if (dimensoes != null && !dimensoes.isEmpty()) {
			for (Iterator iter = dimensoes.iterator(); iter.hasNext();) {
				Dimensao dimensao = (Dimensao) iter.next();
				if(dimensao.getAwb() == null) {
					dimensaoService.removeById(dimensao.getIdDimensao());
					iter.remove();
				} else if(CompareUtils.le(dimensao.getIdDimensao(), LongUtils.ZERO)) {
					dimensao.setIdDimensao(null);
					dimensao.setAwb(awb);
				}
			}
		}
	}

	@Override
	public java.io.Serializable store(Awb awb) {
		return super.store(awb);
	}

	/**
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getAwbDAO().removeByIds(ids, true);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getAwbDAO().findPaginatedPreAwb(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getAwbDAO().getRowCountPreAwb(criteria);
	}

	@Override
	public Awb findById(Long id) {
		return getAwbDAO().findById(id);
	}

	/**
	 * Cria mapa para tela de edicao de Pre-Awb
	 * @param idAwb
	 * @param result
	 * @return Awb para edicao
	 */
	public Awb findToUpdate(Long idAwb, TypedFlatMap result) {
		Awb awb = this.findById(idAwb);

		AwbUtils.getPreAwb(awb, result);
		awb.setCtoAwbs(ctoAwbService.findByIdAwb(idAwb));
		awb.setDimensoes(dimensaoService.findByIdAwb(idAwb));

		return awb;
	}

	public void setAwbDAO(AwbDAO dao) {
		setDao( dao );
	}
	private AwbDAO getAwbDAO() {
		return (AwbDAO) getDao();
	}

	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}
	public void setDimensaoService(DimensaoService dimensaoService) {
		this.dimensaoService = dimensaoService;
	}
	public void setTarifaSpotService(TarifaSpotService tarifaSpotService) {
		this.tarifaSpotService = tarifaSpotService;
	}
	
	public void storeCtosAwbSemModalAereoEComClienteTomadorNaoLiberado(Awb awb){
		boolean hasCtosComPendencia = awbService.findCtosAwbSemModalAereoEComClienteTomadorNaoLiberado(awb);
		if(hasCtosComPendencia){
			String idAwb[] = {String.valueOf(awb.getIdAwb())};
			workFlowPendenciaService.generatePendencia(SessionUtils.getFilialSessao().getIdFilial(), 
					ConstantesWorkflow.NR405_CONHECIMENTO_RODOVIARIO_VINCULADO_A_AWB,
					awb.getIdAwb(), configuracoesFacade.getMensagem("pendenciaAwbConhecimentoRodoviario", idAwb),
							JTDateTimeUtils.getDataHoraAtual());
		}
	}
	

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public void setWorkFlowPendenciaService(
			WorkflowPendenciaService workFlowPendenciaService) {
		this.workFlowPendenciaService = workFlowPendenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public ManifestoEntregaService getManifestoEntregaService() {
		return manifestoEntregaService;
	}

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

}