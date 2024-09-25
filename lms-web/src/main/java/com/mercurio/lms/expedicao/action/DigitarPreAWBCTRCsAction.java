/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.CiaAereaClienteService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.expedicao.digitarPreAWBCTRCsAction"
 * 
 */
public class DigitarPreAWBCTRCsAction extends CrudAction {
	private static final String TP_PEDIDO_COLETA_ENTREGA_DIRETA_AEROPORTO = "DA";
	private ConhecimentoService conhecimentoService;
	private CtoAwbService ctoAwbService;
	private FilialService filialService;
	private DomainValueService domainValueService;
	private AeroportoService aeroportoService;
	private ConfiguracoesFacade configuracoesFacade;
	private static long cont = 0;
	private ClienteService clienteService;
	private CiaAereaClienteService ciaAereaClienteService;

	public List<TypedFlatMap> storeInSessionByPedidoColeta(TypedFlatMap criteria) {
		List<Conhecimento> list = conhecimentoService.findByIdPedidoColeta(criteria.getLong("pedidoColeta.idPedidoColeta"));
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>();
		
		for (Conhecimento conhecimento : list) {
			TypedFlatMap result = prepareStoreInSession(conhecimento);
			listRetorno.add(result);
			if (!StringUtils.isEmpty(result.getString("error"))) {
				listRetorno.clear();
				listRetorno.add(result);
				break;
			}
		}
		
		return listRetorno;
	}
	
	public TypedFlatMap storeInSession(TypedFlatMap parameters) {
		TypedFlatMap result = new TypedFlatMap();
		Conhecimento conhecimento = findConhecimento(parameters);		
		PedidoColeta pedidoColeta = conhecimento.getPedidoColeta();
		if (pedidoColeta != null && pedidoColeta.getTpPedidoColeta().getValue().equals(TP_PEDIDO_COLETA_ENTREGA_DIRETA_AEROPORTO)) {
			result.put("error", "LMS-04533");			
		} else {
			if (conhecimento != null) {
				result = prepareStoreInSession(conhecimento);
			} else if(parameters.getString("nrCodigoBarras") != null) {
				result.put("error", "LMS-00061");
			}
		}
		
		return result;
	}

	private TypedFlatMap prepareStoreInSession(Conhecimento conhecimento) {
		TypedFlatMap result = new TypedFlatMap();
		result.put("error", "");
		result.put("nrConhecimento", conhecimento.getNrConhecimento());
		result.put("idFilial", conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		Awb awb = AwbUtils.getAwbInSession();

		LocalizacaoMercadoria lm = conhecimento.getLocalizacaoMercadoria();
		if(lm != null && lm.getCdLocalizacaoMercadoria() != null) {
			Short cdLocMercadoria = conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
			if(!ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(cdLocMercadoria) &&
			   !ConstantesSim.CD_MERCADORIA_NO_TERMINAL.equals(cdLocMercadoria) &&
			   !ConstantesSim.CD_MERCADORIA_NO_TERMINAL_MERCADORIA_RETORNADA.equals(cdLocMercadoria)) {
				result.put("error", "LMS-04164");
				return result;
			}
		} else {
			result.put("error", "LMS-04164");
			return result;
		}

		Long idFilialLoc = conhecimento.getFilialLocalizacao().getIdFilial();
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if(!idFilialSessao.equals(idFilialLoc)) {
			result.put("error", "LMS-04164");
			return result;
		}

		boolean isAwbRemovidoSessao = false;

		List ctoAwbs = ctoAwbService.findByIdDoctoServico(conhecimento.getIdDoctoServico());
		if(ctoAwbs != null && !ctoAwbs.isEmpty()) {
			CtoAwb ctoAwb = (CtoAwb) ctoAwbs.get(0);
			
			List<Awb> awbList = ctoAwbService.findAwbByCtoAwb(ctoAwb.getIdCtoAwb());
			
			if (awbList != null && !awbList.isEmpty()){
				
				List<CtoAwb> ctoAwbList = awb.getCtoAwbs();
				
				if (ctoAwbList != null && !ctoAwbList.isEmpty()){
					for (CtoAwb ctoAwbSessao : ctoAwbList) {
						if (ctoAwb.getIdCtoAwb().equals(ctoAwbSessao.getIdCtoAwb()) && ctoAwbSessao.getAwb() == null) {
							isAwbRemovidoSessao = true;								
						}
					}
				}
			
				if (!isAwbRemovidoSessao){
					Awb awbByCtoAwb = awbList.get(0);
					result.put("error", "LMS-04441");
					result.put("errorParam", true);
					result = ctoAwbService.customizaMensagem(awbByCtoAwb, result);						
					return result;
				}

			}											
		}
		
		String tpModal = conhecimento.getServico().getTpModal().getValue();
		if(!ConstantesExpedicao.MODAL_AEREO.equals(tpModal) && 
				!clienteService.findClientePossuiLiberacaoRodoNoAereo(conhecimento.getIdDoctoServico())) {
			result.put("confirm", "true");
			return result;
		}

		if (!containsConhecimento(conhecimento, awb)) {
			storeConhecimento(conhecimento, awb);
			somaAcumuladores(conhecimento, awb);
		} else {
			result.put("error", "LMS-04166");
			return result;
		}
		
		AwbUtils.setAwbInSession(awb);
		return result;
	}

	public List findTipoDocumento(TypedFlatMap criteria) {
		List tpSituacoes = domainValueService.findByDomainNameAndValues("DM_TIPO_COD_SERV_CONHECIMENTO"
				, Arrays.asList(ConstantesExpedicao.CONHECIMENTO_NACIONAL,ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		return tpSituacoes;
	}

	public TypedFlatMap storeInSessionNaoAereo(TypedFlatMap data) {
		Conhecimento conhecimento = findConhecimento(data);
		data.clear();
		data.put("error", "");

		Awb awb = AwbUtils.getAwbInSession();
		if (!containsConhecimento(conhecimento, awb)) {
			storeConhecimento(conhecimento, awb);
			somaAcumuladores(conhecimento, awb);
		} else {
			data.put("error", "LMS-04166");
		}
		AwbUtils.setAwbInSession(awb);

		return data;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		List ctos = this.getCtoAwbInSession();

		if(ids != null) {
			for(Iterator iter = ids.iterator(); iter.hasNext();) {
				Long id = (Long) iter.next();
				removeCtoAwb(ctos, id);				
			}
			setCwoAwbInSession(ctos);
		}
	}
	
	private void setCwoAwbInSession(List ctoAwbs) {
		Awb awb = AwbUtils.getAwbInSession();
		
		if (awb != null){
			awb.setCtoAwbs(ctoAwbs);
		AwbUtils.setAwbInSession(awb);
	}
	}

	private void removeCtoAwb(List ctoAwbs, Long id){
		String tpDocumentoServico = ExpedicaoUtils.getTpDocumentoInSession();
		
		for(Iterator iter = ctoAwbs.iterator(); iter.hasNext();) {
			CtoAwb ctoAwb = (CtoAwb) iter.next();
			
			if(CompareUtils.eq(id, ctoAwb.getIdCtoAwb())) {
				if(ConstantesExpedicao.AIRWAY_BILL.equals(tpDocumentoServico)) {
					subtraiAcumuladores(ctoAwb.getConhecimento(), AwbUtils.getAwbInSession());
					ctoAwb.setAwb(null);					
				} else {
					iter.remove();
				}
			}
		}
	}
	
	private List getCtoAwbInSession(){
		List<CtoAwb> ctoAwb = null;
		
		if(AwbUtils.getAwbInSession().getCtoAwbs() != null){
			ctoAwb = AwbUtils.getAwbInSession().getCtoAwbs();
		}
		
		if(ctoAwb == null) {
			ctoAwb = new ArrayList<CtoAwb>();
		}
		
		return ctoAwb;
		
	}
	
	public void removeById(java.lang.Long id) {
		List ctoAwb = this.getCtoAwbInSession();
		removeCtoAwb(ctoAwb, id);
		setCwoAwbInSession(ctoAwb);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = ResultSetPage.EMPTY_RESULTSET;
		Awb awb = AwbUtils.getAwbInSession();
		List ctoAwbs = awb.getCtoAwbs();
		if(ctoAwbs != null && !ctoAwbs.isEmpty()) {
			FindDefinition def = FindDefinition.createFindDefinition(criteria);

			Comparator ordenador = new Comparator() {
				public int compare(Object arg0, Object arg1) {
					return 0;
				}
			};

			rsp = MasterDetailAction.getResultSetPage(ctoAwbs, def.getCurrentPage(), def.getPageSize(), ordenador);

			Iterator itList = rsp.getList().iterator();
    		List newList = new ArrayList();

    		while(itList.hasNext()) {
    			CtoAwb ctoAwb = (CtoAwb) itList.next();
    			if(ctoAwb.getAwb() == null) {
    				continue;
    			}
    			Conhecimento conhecimento = ctoAwb.getConhecimento();
    			conhecimento = conhecimentoService.findById(conhecimento.getIdDoctoServico());
    			
    			Map map = new HashMap();

    			map.put("sgAeroportoDestino", "");
    			Aeroporto aeroportoDestino = null;

    			String tpModal = conhecimento.getServico().getTpModal().getValue();
    			if(!ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
    				// eh rodoviario, pega o aeroporto da filial de destino
    				Filial filialDestino = conhecimento.getFilialByIdFilialDestino(); 
    				if(filialDestino != null) {
    					aeroportoDestino = filialDestino.getAeroporto();
    				}
    			} else {
    				// eh aereo, pega o aeroporto de destino
    				aeroportoDestino = conhecimento.getAeroportoByIdAeroportoDestino();
    			}

    			if(aeroportoDestino != null) {
    				aeroportoDestino = aeroportoService.findById(aeroportoDestino.getIdAeroporto());
					map.put("sgAeroportoDestino", aeroportoDestino.getSgAeroporto());    						
				}

    			map.put("psReal", conhecimento.getPsReal());
    			map.put("idCtoAwb", Long.valueOf(ctoAwb.getIdCtoAwb().longValue()));
    			map.put("psAforado", conhecimento.getPsAforado());
    			map.put("qtVolumes", conhecimento.getQtVolumes());
    			map.put("vlMercadoria", conhecimento.getVlMercadoria());
    			
    			Filial filialOrigem = filialService.findById(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
    			
    			if(ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(conhecimento.getTpDocumentoServico().getValue())){
    				map.put("nrConhecimentoFormatado", ConhecimentoUtils.formatConhecimento(filialOrigem.getSgFilial(), conhecimento.getNrConhecimento(), conhecimento.getDvConhecimento()));
    			} else {
    				map.put("nrConhecimentoFormatado", ConhecimentoUtils.formatConhecimento(filialOrigem.getSgFilial(), conhecimento.getNrConhecimento(), null));    				
    			}
    			newList.add(map);
    		}
    		rsp.setList(newList);
		}
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		List ctoAwbs = AwbUtils.getAwbInSession().getCtoAwbs();
		if(ctoAwbs != null && !ctoAwbs.isEmpty()) {
			return Integer.valueOf(ctoAwbs.size());
		}
		return IntegerUtils.ZERO;
	}

	public void consolidarCarga(TypedFlatMap data) {
		List received = data.getList("conhecimentos");
		if (received == null || received.isEmpty()) {
			throw new BusinessException("LMS-04159");
		}
		Awb awb = AwbUtils.getAwbInSession();
		List ctoAwbs = awb.getCtoAwbs();
		if (ctoAwbs != null) {
			ctoAwbs.clear();
		}

		for (int i = 0; i < received.size(); i++) {
			Long idConhecimento = Long.valueOf((String) received.get(i));
			Conhecimento conhecimento = conhecimentoService.findByIdPreAwb(idConhecimento);
			storeConhecimento(conhecimento, awb);
		}

		awb.setPsCubado(data.getBigDecimal("psCubadoTotal"));
		awb.setQtVolumes(data.getInteger("qtVolumesTotal"));
		awb.setPsTotal(data.getBigDecimal("psRealTotal"));

		AwbUtils.setAwbInSession(awb);
	}

	public List findFilialConhecimento(TypedFlatMap criteria) {
		return filialService.findLookupBySgFilial(criteria.getString("sgFilial"), criteria.getString("tpAcesso"));
	}

	public List findDoctoServico(TypedFlatMap criteria) {
		List result = new ArrayList();
		Conhecimento conhecimento = findConhecimento(criteria);
		if(conhecimento != null) {
			Map mapReturn = new HashMap();
			mapReturn.put("dvConhecimento", conhecimento.getDvConhecimento());
			mapReturn.put("idDoctoServico", conhecimento.getIdDoctoServico());
			Long nrConhecimento = criteria.getLong("nrConhecimento");
			mapReturn.put("nrConhecimento", FormatUtils.formatDecimal("00000000", nrConhecimento));

			Aeroporto aeroportoDestino = null;
			String tpModal = conhecimento.getServico().getTpModal().getValue();
			if(!ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
				// eh rodoviario, pega o aeroporto da filial de destino
				Filial filialDestino = conhecimento.getFilialByIdFilialDestino(); 
				if(filialDestino != null) {
					aeroportoDestino = filialDestino.getAeroporto();
				}
			} else {
				// eh aereo, pega o aeroporto de destino
				aeroportoDestino = conhecimento.getAeroportoByIdAeroportoDestino();
			}
			if (aeroportoDestino != null) {
				mapReturn.put("sgAeroporto", aeroportoDestino.getSgAeroporto());
				Pessoa p = aeroportoDestino.getPessoa();
				if (p != null) {
					mapReturn.put("nmPessoa", p.getNmPessoa());
				}
			}
			result.add(mapReturn);
		} else {
			throw new BusinessException("LMS-00061");
		}
		return result;
	}

	public TypedFlatMap getAcumuladores() {
		Awb awb = AwbUtils.getAwbInSession();

		TypedFlatMap result = new TypedFlatMap();
		result.put("psRealTotal", awb.getPsTotal());
		result.put("psCubadoTotal", awb.getPsCubado());
		result.put("qtVolumesTotal", awb.getQtVolumes());

		return result;
	}

	private Conhecimento findConhecimento(TypedFlatMap parameters) {
		Long nrConhecimento = parameters.getLong("nrConhecimento");
		Long idFilialOrigem = parameters.getLong("filialByIdFilialOrigem.idFilial");
		String tpDocumentoServico = parameters.getString("tpDocumentoServico");
		String nrCodigoBarras = parameters.getString("nrCodigoBarras");

		if (nrConhecimento != null && idFilialOrigem != null) {
			return conhecimentoService.findConhecimentoByNrConhecimentoIdFilial(nrConhecimento, idFilialOrigem, tpDocumentoServico);
		} else if(StringUtils.isNotBlank(nrCodigoBarras)) {
			return conhecimentoService.findByNrChaveDocEletronico(nrCodigoBarras);
		}
		
		return null;
	}

	private void storeConhecimento(Conhecimento conhecimento, Awb awb) {
		CtoAwb ctoAwb = new CtoAwb();
		ctoAwb.setAwb(awb);
		ctoAwb.setConhecimento(conhecimento);
		ctoAwb.setIdCtoAwb(Long.valueOf(--cont));

		awb.addCtoAwb(ctoAwb);
	}

	private boolean containsConhecimento(Conhecimento conhecimento, Awb awb) {
		List<CtoAwb> ctoAwbs = awb.getCtoAwbs();

		if (ctoAwbs != null) {
			for (CtoAwb ctoAwb : ctoAwbs) {
				Conhecimento cto = ctoAwb.getConhecimento();
				if(cto.getIdDoctoServico().equals(conhecimento.getIdDoctoServico()) && ctoAwb.getAwb() != null) {
					return true;
				}
			}
		}
		return false;
	}

	private void somaAcumuladores(Conhecimento conhecimento, Awb awb) {
		awb.setQtVolumes(IntegerUtils.addNull(conhecimento.getQtVolumes(), awb.getQtVolumes()));
		awb.setPsTotal(BigDecimalUtils.add(conhecimento.getPsReal(), awb.getPsTotal()));
		awb.setPsCubado(BigDecimalUtils.add(conhecimento.getPsAforado(), awb.getPsCubado()));
	}

	private void subtraiAcumuladores(Conhecimento conhecimento, Awb awb) {
		Integer qtVolumes = IntegerUtils.defaultInteger(conhecimento.getQtVolumes());
		BigDecimal psReal = BigDecimalUtils.defaultBigDecimal(conhecimento.getPsReal());
		BigDecimal psCubado = BigDecimalUtils.defaultBigDecimal(conhecimento.getPsAforado());

		Integer qtVolumesTotal = IntegerUtils.defaultInteger(awb.getQtVolumes());
		BigDecimal psRealTotal = BigDecimalUtils.defaultBigDecimal(awb.getPsTotal());
		BigDecimal psCubadoTotal = BigDecimalUtils.defaultBigDecimal(awb.getPsCubado());

		awb.setQtVolumes(IntegerUtils.subtract(qtVolumesTotal, qtVolumes));
		awb.setPsTotal(psRealTotal.subtract(psReal));
		awb.setPsCubado(psCubadoTotal.subtract(psCubado));
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public CiaAereaClienteService getCiaAereaClienteService() {
		return ciaAereaClienteService;
	}

	public void setCiaAereaClienteService(
			CiaAereaClienteService ciaAereaClienteService) {
		this.ciaAereaClienteService = ciaAereaClienteService;
	}

}