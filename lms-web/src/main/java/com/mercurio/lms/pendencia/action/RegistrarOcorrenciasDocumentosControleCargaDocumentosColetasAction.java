package com.mercurio.lms.pendencia.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.pendencia.registrarOcorrenciasDocumentosControleCargaDocumentosColetasAction"
 */
public class RegistrarOcorrenciasDocumentosControleCargaDocumentosColetasAction {
	private PedidoColetaService pedidoColetaService;
	private DetalheColetaService detalheColetaService;

	/**
	 * Busca a quantidade de dados da grid de carregamentos
	 */
	public Integer getRowCount(TypedFlatMap criteria) {		
		int count = 0;
		List listPedidoColeta = this.getPedidoColetaService().
								findPedidoColetaByIdManifestoColeta(criteria.getLong("manifestoColeta.idManifestoColeta"));
		for (Iterator iter = listPedidoColeta.iterator(); iter.hasNext();) {
			PedidoColeta pedidoColeta = (PedidoColeta) iter.next();
			List listDetalheColeta = this.getDetalheColetaService().findDetalheColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta());
			if (listDetalheColeta != null){
				count = count + listDetalheColeta.size();
			}
		}

		return listPedidoColeta.size();
	}	

	/**
	 * Busca os registros da grid de carregamento.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {		
		List listPedidoColeta = this.getPedidoColetaService().
								findPedidoColetaByIdManifestoColeta(criteria.getLong("manifestoColeta.idManifestoColeta"));
		
		List listColetas = new ArrayList();		    
		for (Iterator iter = listPedidoColeta.iterator(); iter.hasNext();) {
			PedidoColeta pedidoColeta = (PedidoColeta) iter.next();			
			
			List listDetalheColeta = this.getDetalheColetaService().findDetalheColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta());			
			for (Iterator iterator = listDetalheColeta.iterator(); iterator.hasNext();) {
				TypedFlatMap mapPedidoColeta = new TypedFlatMap();
				DetalheColeta detalheColeta = (DetalheColeta) iterator.next();
				
				mapPedidoColeta.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
				mapPedidoColeta.put("sgFilial", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
				mapPedidoColeta.put("nrColeta", pedidoColeta.getNrColeta());
				mapPedidoColeta.put("idCliente", pedidoColeta.getCliente().getIdCliente());
				mapPedidoColeta.put("nmPessoa", pedidoColeta.getCliente().getPessoa().getNmPessoa());
				mapPedidoColeta.put("tpPedidoColeta.value", pedidoColeta.getTpPedidoColeta().getValue());
				mapPedidoColeta.put("tpPedidoColeta.description", pedidoColeta.getTpPedidoColeta().getDescription());
				mapPedidoColeta.put("tpPedidoColeta.status", pedidoColeta.getTpPedidoColeta().getStatus());			
				mapPedidoColeta.put("dhPedidoColeta", pedidoColeta.getDhPedidoColeta());
				mapPedidoColeta.put("psMercadoria", detalheColeta.getPsMercadoria());
				mapPedidoColeta.put("qtVolumes", detalheColeta.getQtVolumes());
				mapPedidoColeta.put("sgMoeda", detalheColeta.getMoeda().getSgMoeda());
				mapPedidoColeta.put("dsSimbolo", detalheColeta.getMoeda().getDsSimbolo());
				mapPedidoColeta.put("vlMercadoria", detalheColeta.getVlMercadoria());
				if (detalheColeta.getFilial()!=null){
					mapPedidoColeta.put("sgFilialDestino", detalheColeta.getFilial().getSgFilial());
				}
				mapPedidoColeta.put("tpStatusColeta.value", pedidoColeta.getTpStatusColeta().getValue());			
				mapPedidoColeta.put("tpStatusColeta.description", pedidoColeta.getTpStatusColeta().getDescription());
				mapPedidoColeta.put("tpStatusColeta.status", pedidoColeta.getTpStatusColeta().getStatus());
				mapPedidoColeta.put("tpModoPedidoColeta.value", pedidoColeta.getTpModoPedidoColeta().getValue());
				mapPedidoColeta.put("tpModoPedidoColeta.description", pedidoColeta.getTpModoPedidoColeta().getDescription());
				mapPedidoColeta.put("tpModoPedidoColeta.status", pedidoColeta.getTpModoPedidoColeta().getStatus());
				mapPedidoColeta.put("nmUsuario", pedidoColeta.getUsuario().getNmUsuario());
				
				listColetas.add(mapPedidoColeta);
			}			
		}
			
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		ResultSetPage resultSetPage = new ResultSetPage(findDefinition.getCurrentPage(), listColetas);
		
		return resultSetPage;			
	}


	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public DetalheColetaService getDetalheColetaService() {
		return detalheColetaService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}
	
}
