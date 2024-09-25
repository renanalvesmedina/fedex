package com.mercurio.lms.coleta.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.service.RegiaoFilialRotaColEntService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.consultarColetasDadosColetaAction"
 */

public class ConsultarColetasDadosColetaAction {
	
	private PedidoColetaService pedidoColetaService;
	private RegiaoFilialRotaColEntService regiaoFilialRotaColEntService;
	private DetalheColetaService detalheColetaService;
	private EventoColetaService eventoColetaService;
	private AwbService awbService;
	
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public void setRegiaoFilialRotaColEntService(RegiaoFilialRotaColEntService regiaoFilialRotaColEntService) {
		this.regiaoFilialRotaColEntService = regiaoFilialRotaColEntService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}

    public AwbService getAwbService() {
		return awbService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public TypedFlatMap findById(java.lang.Long id) {
    	PedidoColeta pedidoColeta = pedidoColetaService.findById(id);
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
    	map.put("nrColeta", pedidoColeta.getNrColeta());
    	if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
    		map.put("filialByIdFilialResponsavel.sgFilial", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
    		map.put("filialByIdFilialResponsavel.pessoa.nmFantasia", pedidoColeta.getFilialByIdFilialResponsavel().getPessoa().getNmFantasia());
    	}
    	if (pedidoColeta.getFilialByIdFilialSolicitante() != null) {
    		map.put("filialByIdFilialSolicitante.sgFilial", pedidoColeta.getFilialByIdFilialSolicitante().getSgFilial());
    		map.put("filialByIdFilialSolicitante.pessoa.nmFantasia", pedidoColeta.getFilialByIdFilialSolicitante().getPessoa().getNmFantasia());
    	}
    	map.put("dhPedidoColeta", pedidoColeta.getDhPedidoColeta());
    	map.put("nmSolicitante", pedidoColeta.getUsuario().getNmUsuario());
    	if (pedidoColeta.getCliente() != null) {
    		map.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pedidoColeta.getCliente().getPessoa()));
    		map.put("cliente.pessoa.nmPessoa", pedidoColeta.getCliente().getPessoa().getNmPessoa());
    	}
    	map.put("enderecoComComplemento", pedidoColeta.getEnderecoComComplemento());
    	if (pedidoColeta.getMunicipio() != null) {
    		map.put("municipio.nmMunicipio", pedidoColeta.getMunicipio().getNmMunicipio());
    	}
    	map.put("blProdutoDiferenciado", pedidoColeta.getBlProdutoDiferenciado());
    	map.put("nrCep", pedidoColeta.getNrCep());
    	map.put("tpStatusColeta.description", pedidoColeta.getTpStatusColeta().getDescription().toString());
    	map.put("tpPedidoColeta.description", pedidoColeta.getTpPedidoColeta().getDescription().toString());
    	map.put("tpModoPedidoColeta.description", pedidoColeta.getTpModoPedidoColeta().getDescription().toString());
    	map.put("dhColetaDisponivel", pedidoColeta.getDhColetaDisponivel());
    	map.put("dtPrevisaoColeta", pedidoColeta.getDtPrevisaoColeta());
    	map.put("hrLimiteColeta", pedidoColeta.getHrLimiteColeta());
    	map.put("nrTelefoneCliente", pedidoColeta.getNrTelefoneCliente());
    	map.put("nmContatoCliente", pedidoColeta.getNmContatoCliente());
    	if (pedidoColeta.getRotaColetaEntrega() != null) {
    		map.put("rotaColetaEntrega.dsRota", pedidoColeta.getRotaColetaEntrega().getDsRota());
    	}
    	map.put("qtTotalVolumesVerificado", pedidoColeta.getQtTotalVolumesVerificado());
    	map.put("psTotalInformado", pedidoColeta.getPsTotalInformado());
    	map.put("psTotalVerificado", pedidoColeta.getPsTotalVerificado());
    	if (pedidoColeta.getMoeda() != null) {
    		map.put("moeda.dsSimbolo", pedidoColeta.getMoeda().getDsSimbolo());
    		map.put("moeda.sgMoeda", pedidoColeta.getMoeda().getSgMoeda());
    		map.put("moeda.siglaSimbolo", pedidoColeta.getMoeda().getSiglaSimbolo());
    	}
    	map.put("vlTotalVerificado", pedidoColeta.getVlTotalVerificado());
    	if (pedidoColeta.getManifestoColeta() != null) {
    		map.put("manifestoColeta.nrManifesto", pedidoColeta.getManifestoColeta().getNrManifesto());
    		map.put("manifestoColeta.filial.sgFilial", pedidoColeta.getManifestoColeta().getFilial().getSgFilial());
    		
    		if (pedidoColeta.getManifestoColeta().getControleCarga() != null) {
		        map.put("manifestoColeta.controleCarga.idControleCarga", 
		        		pedidoColeta.getManifestoColeta().getControleCarga().getIdControleCarga());
		        map.put("manifestoColeta.controleCarga.nrControleCarga", 
		        		pedidoColeta.getManifestoColeta().getControleCarga().getNrControleCarga());
		        map.put("manifestoColeta.controleCarga.filialByIdFilialOrigem.sgFilial", 
		        		pedidoColeta.getManifestoColeta().getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
		        
		        if (pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado() != null) {
		        	map.put("manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrIdentificador", 
		        			pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getNrIdentificador());
		        	map.put("manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrFrota", 
		        			pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getNrFrota());
		        }
    		}
    	}
    	map.put("obPedidoColeta", pedidoColeta.getObPedidoColeta());
    	map.put("sgFilialPedidoColeta", map.getString("filialByIdFilialResponsavel.sgFilial"));

    	if(pedidoColeta.getRotaColetaEntrega() != null){
    	    Map criteriosRegiao = new HashMap();
    	    criteriosRegiao.put("idRotaColetaEntrega", pedidoColeta.getRotaColetaEntrega().getIdRotaColetaEntrega());
    	    criteriosRegiao.put("dtVigente", JTDateTimeUtils.getDataAtual());
    	    List listaRegiaoColetaEntregaFil = regiaoFilialRotaColEntService.findByVigencia(criteriosRegiao);
    	    if (!listaRegiaoColetaEntregaFil.isEmpty()) {
    	        RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = (RegiaoFilialRotaColEnt)listaRegiaoColetaEntregaFil.get(0);
    	        map.put("dsRegiaoColetaEntregaFil", regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getDsRegiaoColetaEntregaFil());
    	    }
    	}
    	
    	StringBuilder obsCancelamento = new StringBuilder();
    	if (pedidoColeta.getTpStatusColeta().getValue().equals("CA")){
        	List<EventoColeta> eventosColeta = eventoColetaService.findEventoColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta(), "CA");
        	for (EventoColeta evento : eventosColeta) {
    			obsCancelamento.append(evento.getDsDescricao()).append(" ");
    		}
    	}
    	map.put("obsCancelamento", obsCancelamento.toString());
    	
    	return map;
    }

    
    public ResultSetPage findPaginatedDetalhesColeta(TypedFlatMap criteria) {
		Long idPedidoColeta = criteria.getLong("pedidoColeta.idPedidoColeta");
    	ResultSetPage rsp = detalheColetaService.findPaginatedByDetalhesColeta(idPedidoColeta, FindDefinition.createFindDefinition(criteria));
    	List list = rsp.getList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		String nrIdentificacao = map.getString("cliente.pessoa.nrIdentificacao");
            if (nrIdentificacao != null && !nrIdentificacao.equals("")) {
            	map.put("cliente.pessoa.nrIdentificacaoFormatado",   
                	FormatUtils.formatIdentificacao(map.getString("cliente.pessoa.tpIdentificacao.value"), nrIdentificacao));
            }
            map.put("moeda.siglaSimbolo", map.getString("moeda.sgMoeda") + " " + map.getString("moeda.dsSimbolo"));
            
            Long idDoctoServico = map.getLong("idDoctoServico");
            if(idDoctoServico != null){
            	map.put("awb", awbService.findPreAwbAwbByIdDoctoServico(idDoctoServico));
            }
    	}
    	return rsp;
    }


    public Integer getRowCountDetalhesColeta(Map criteria) {
		Map mapPedidoColeta = (Map)criteria.get("pedidoColeta");
		Long idPedidoColeta = Long.valueOf((String)mapPedidoColeta.get("idPedidoColeta"));
    	return detalheColetaService.getRowCountByDetalhesColeta(idPedidoColeta);
    }
    
    /**
     * <p>Busca a nao conformidade utilando o idOcorrenciaNaoConformidade informado pelo
     * processo do workflow.</p>
     * <p>Duvidas olhar a E.T. 02.05.01.02(CadastrarPedidoColeta) onde o workflow e gerado.</p> 
     * 
     * @param criteria
     * @return TypedFlatMap
     */
    public TypedFlatMap findByIdProcessoWorkflow(TypedFlatMap criteria) {
    	
    	TypedFlatMap result = new TypedFlatMap();
    	Long idPedidoColeta = criteria.getLong("idProcessoWorkflow");
    	
    	if (idPedidoColeta!=null) {
    		result = this.findById(idPedidoColeta);
    	}
    	
    	return result;
    }
    
	public EventoColetaService getEventoColetaService() {
		return eventoColetaService;
	}
	public void setEventoColetaService(EventoColetaService eventoColetaService) {
		this.eventoColetaService = eventoColetaService;
	}
    
    
}