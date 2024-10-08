package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.report.RelatorioEventosColetaService;
import com.mercurio.lms.municipios.model.service.FilialService;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.coleta.consultarEventosColetaAction"
 */

public class ConsultarEventosColetaAction extends ReportActionSupport {

    private FilialService filialService;
    private PedidoColetaService pedidoColetaService;
    private EventoColetaService eventoColetaService;

    /**
     * busca a filial pela sigla
     * @param map
     * @return
     */
    public List findLookupBySgFilial(Map map) {
        return getFilialService().findLookupBySgFilial((String)map.get("sgFilial"), (String)map.get("tpAcesso"));
    }
    
    /**
     * busca um pedido coleta pelo id
     * @param map
     * @return
     */
    public TypedFlatMap findPedidoColetaById(Long id) {
        PedidoColeta pedidoColeta = getPedidoColetaService().findById(id);
        TypedFlatMap camposUtilizados = new TypedFlatMap();
        camposUtilizados.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
        camposUtilizados.put("filialByIdFilialResponsavel.sgFilial", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
        camposUtilizados.put("filialByIdFilialResponsavel.idFilial", pedidoColeta.getFilialByIdFilialResponsavel().getIdFilial());
        camposUtilizados.put("nrColeta", pedidoColeta.getNrColeta());
        return camposUtilizados;
    }
    
    /**
     * lookup de pedido coleta 
     * @param map
     * @return
     */
    public List findLookupPedidoColeta(Map criteria) {
        List listPedidosColeta = getPedidoColetaService().findLookup(criteria);
        List listRetorno = new ArrayList();
        TypedFlatMap map = null;
        for (Iterator iter = listPedidosColeta.iterator(); iter.hasNext();) {
        	PedidoColeta pedidocoleta = (PedidoColeta) iter.next();
        	map = new TypedFlatMap();
        	map.put("idPedidoColeta", pedidocoleta.getIdPedidoColeta());
			map.put("filialByIdFilialResponsavel.sgFilial", pedidocoleta.getFilialByIdFilialResponsavel().getSgFilial());
			map.put("filialByIdFilialResponsavel.idFilial", pedidocoleta.getFilialByIdFilialResponsavel().getIdFilial());
			map.put("nrColeta", pedidocoleta.getNrColeta());
			listRetorno.add(map);
		}
        return listRetorno;
    }
    
    /**
     * find para consulta eventos de coleta 
     * @param map
     * @return
     */
    public ResultSetPage findPaginatedConsultaEventosColeta(Map map) {
        ResultSetPage rsp = getEventoColetaService().findPaginated(map); 
        List eventosColeta = rsp.getList();
        List result = new ArrayList();
        for (Iterator iter = eventosColeta.iterator(); iter.hasNext();) {
        	EventoColeta eventoColeta = (EventoColeta) iter.next();
        	TypedFlatMap tfm = new TypedFlatMap();
        	tfm.put("idPedidoColeta", eventoColeta.getPedidoColeta().getIdPedidoColeta());
        	tfm.put("dhEvento", eventoColeta.getDhEvento());
        	tfm.put("tpEventoColeta", eventoColeta.getTpEventoColeta());
        	if (eventoColeta.getMeioTransporteRodoviario()!=null){
        		tfm.put("meioTransporteRodoviario.meioTransporte.nrFrota", eventoColeta.getMeioTransporteRodoviario().getMeioTransporte().getNrFrota());
        	}	
        	tfm.put("ocorrenciaColeta.dsDescricaoCompleta", eventoColeta.getOcorrenciaColeta().getDsDescricaoCompleta());
        	tfm.put("usuario.nmUsuario", eventoColeta.getUsuario().getNmUsuario());
        	result.add(tfm);
		}
        rsp.setList(result);
        return rsp;
    }
    
    public Integer getRowCountConsultaEventosColeta(TypedFlatMap map) {
    	Long idPedidoColeta = map.getLong("pedidoColeta.idPedidoColeta");
        return getEventoColetaService().getRowCount(idPedidoColeta);
    }
    
    public void setRelatorioEventosColetaService(RelatorioEventosColetaService relatorioEventosColetaService) {
        this.reportServiceSupport = relatorioEventosColetaService;
    }
    
    public FilialService getFilialService() {
        return filialService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public PedidoColetaService getPedidoColetaService() {
        return pedidoColetaService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public EventoColetaService getEventoColetaService() {
        return eventoColetaService;
    }

    public void setEventoColetaService(EventoColetaService eventoColetaService) {
        this.eventoColetaService = eventoColetaService;
    }
}