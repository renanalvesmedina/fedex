package com.mercurio.lms.recepcaodescarga.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.recepcaodescarga.swt.descarregarVeiculoDocumentosColetasAction"
 */
public class DescarregarVeiculoDocumentosColetasAction extends CrudAction {
	private DetalheColetaService detalheColetaService;


	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}

	/**
	 * Busca a quantidade de dados da grid de carregamentos
	 */
	public Integer getRowCount(TypedFlatMap criteria) {
		Long idManifestoColeta = criteria.getLong("idManifestoColeta");
		return detalheColetaService.getRowCountDetalheColetaByIdManifestoColeta(idManifestoColeta);
	}	

	/**
	 * Busca os registros da grid de carregamento.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {		
		Long idManifestoColeta = criteria.getLong("idManifestoColeta");
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		ResultSetPage resultSetPage = detalheColetaService.findPaginatedDetalheColetaByIdManifestoColeta(idManifestoColeta, findDefinition);
		List listDetalhesColeta = resultSetPage.getList();
		List retorno = new ArrayList();
		for (Iterator iter = listDetalhesColeta.iterator(); iter.hasNext();) {
			Map map = new HashMap();
			DetalheColeta dc = (DetalheColeta) iter.next();
			map.put("idPedidoColeta", dc.getPedidoColeta().getIdPedidoColeta());
			map.put("idCliente", dc.getPedidoColeta().getCliente().getIdCliente());
			map.put("sgFilial", dc.getPedidoColeta().getFilialByIdFilialResponsavel().getSgFilial());
			map.put("nrColeta", dc.getPedidoColeta().getNrColeta());
			map.put("nmPessoa", dc.getPedidoColeta().getCliente().getPessoa().getNmPessoa());
			if (dc.getFilial()!=null){
				map.put("sgFilialDestino", dc.getFilial().getSgFilial());
			}
			map.put("dhPedidoColeta", dc.getPedidoColeta().getDhPedidoColeta());
			map.put("psMercadoria", dc.getPsMercadoria());
			map.put("qtVolumes", dc.getQtVolumes());
			map.put("sgMoeda", dc.getMoeda().getSgMoeda());
			map.put("dsSimbolo", dc.getMoeda().getDsSimbolo());
			map.put("vlMercadoria", dc.getVlMercadoria());
			map.put("tpPedidoColeta", dc.getPedidoColeta().getTpPedidoColeta());
			map.put("tpModoPedidoColeta", dc.getPedidoColeta().getTpModoPedidoColeta());
			map.put("tpStatusColeta", dc.getPedidoColeta().getTpStatusColeta());
			map.put("nmUsuario", dc.getPedidoColeta().getUsuario().getNmUsuario());
			retorno.add(map);
		}
		resultSetPage.setList(retorno);
		return resultSetPage; 
	}
}
