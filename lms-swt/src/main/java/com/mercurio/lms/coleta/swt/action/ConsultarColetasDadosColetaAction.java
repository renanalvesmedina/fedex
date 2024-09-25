package com.mercurio.lms.coleta.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.service.RegiaoFilialRotaColEntService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.swt.consultarColetasDadosColetaAction"
 */

public class ConsultarColetasDadosColetaAction {
	
	private PedidoColetaService pedidoColetaService;
	private RegiaoFilialRotaColEntService regiaoFilialRotaColEntService;
	private DetalheColetaService detalheColetaService;
	
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public void setRegiaoFilialRotaColEntService(RegiaoFilialRotaColEntService regiaoFilialRotaColEntService) {
		this.regiaoFilialRotaColEntService = regiaoFilialRotaColEntService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}


    public Map findById(Map criteria) {
    	PedidoColeta pedidoColeta = pedidoColetaService.findById((Long)criteria.get("idPedidoColeta"));
    	Map map = new HashMap();
    	map.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());
    	map.put("nrColeta", pedidoColeta.getNrColeta());
    	if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
    		map.put("sgFilialResponsavel", pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
    		map.put("nmFantasiaFilialResponsavel", pedidoColeta.getFilialByIdFilialResponsavel().getPessoa().getNmFantasia());
    	}
    	if (pedidoColeta.getFilialByIdFilialSolicitante() != null) {
    		map.put("sgFilialSolicitante", pedidoColeta.getFilialByIdFilialSolicitante().getSgFilial());
    		map.put("nmFantasiaFilialSolicitante", pedidoColeta.getFilialByIdFilialSolicitante().getPessoa().getNmFantasia());
    	}
    	map.put("dhPedidoColeta", pedidoColeta.getDhPedidoColeta());
    	map.put("nmSolicitante", pedidoColeta.getUsuario().getNmUsuario());
    	if (pedidoColeta.getCliente() != null) {
    		map.put("nrIdentificacaoCliente", FormatUtils.formatIdentificacao(pedidoColeta.getCliente().getPessoa()));
    		map.put("nmPessoaCliente", pedidoColeta.getCliente().getPessoa().getNmPessoa());
    	}
    	map.put("endereco", pedidoColeta.getEnderecoComComplemento());
    	if (pedidoColeta.getMunicipio() != null) {
    		map.put("nmMunicipio", pedidoColeta.getMunicipio().getNmMunicipio());
    	}
    	map.put("nrCep", pedidoColeta.getNrCep());
    	map.put("tpStatusColeta", pedidoColeta.getTpStatusColeta().getDescription().toString());
    	map.put("tpPedidoColeta", pedidoColeta.getTpPedidoColeta().getDescription().toString());
    	map.put("tpModoPedidoColeta", pedidoColeta.getTpModoPedidoColeta().getDescription().toString());
    	map.put("dhColetaDisponivel", pedidoColeta.getDhColetaDisponivel());
    	map.put("dtPrevisaoColeta", pedidoColeta.getDtPrevisaoColeta());
    	map.put("hrLimiteColeta", pedidoColeta.getHrLimiteColeta());
    	map.put("nrTelefoneCliente", pedidoColeta.getNrTelefoneCliente());
    	map.put("nmContatoCliente", pedidoColeta.getNmContatoCliente());
    	if (pedidoColeta.getRotaColetaEntrega() != null) {
    		map.put("dsRotaColetaEntrega", pedidoColeta.getRotaColetaEntrega().getDsRota());
    	}
    	map.put("qtTotalVolumesVerificado", pedidoColeta.getQtTotalVolumesVerificado());
    	map.put("psTotalInformado", pedidoColeta.getPsTotalInformado());
    	map.put("psTotalVerificado", pedidoColeta.getPsTotalVerificado());
    	if (pedidoColeta.getMoeda() != null) {
    		map.put("siglaSimbolo", pedidoColeta.getMoeda().getSiglaSimbolo());
    	}
    	map.put("vlTotalVerificado", pedidoColeta.getVlTotalVerificado());
    	if (pedidoColeta.getManifestoColeta() != null) {
    		map.put("nrManifestoColeta", pedidoColeta.getManifestoColeta().getNrManifesto());
    		map.put("sgFilialManifestoColeta", pedidoColeta.getManifestoColeta().getFilial().getSgFilial());
    		
    		if (pedidoColeta.getManifestoColeta().getControleCarga() != null) {
		        map.put("idControleCarga", 
		        		pedidoColeta.getManifestoColeta().getControleCarga().getIdControleCarga());
		        map.put("nrControleCarga", 
		        		pedidoColeta.getManifestoColeta().getControleCarga().getNrControleCarga());
		        map.put("sgFilialOrigemControleCarga", 
		        		pedidoColeta.getManifestoColeta().getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
		        
		        if (pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado() != null) {
		        	map.put("nrIdentificadorTransportado", 
		        			pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getNrIdentificador());
		        	map.put("nrFrotaTransportado", 
		        			pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getNrFrota());
		        }
    		}
    	}
    	map.put("obPedidoColeta", pedidoColeta.getObPedidoColeta());
    	map.put("sgFilialPedidoColeta", (String)map.get("sgFilialResponsavel"));

    	Map criteriosRegiao = new HashMap();
    	criteriosRegiao.put("idRotaColetaEntrega", pedidoColeta.getRotaColetaEntrega().getIdRotaColetaEntrega());
    	criteriosRegiao.put("dtVigente", JTDateTimeUtils.getDataAtual());
    	List listaRegiaoColetaEntregaFil = regiaoFilialRotaColEntService.findByVigencia(criteriosRegiao);
    	if (!listaRegiaoColetaEntregaFil.isEmpty()) {
    		RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = (RegiaoFilialRotaColEnt)listaRegiaoColetaEntregaFil.get(0);
    		map.put("dsRegiaoColetaEntregaFil", regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getDsRegiaoColetaEntregaFil());
    	}
    	return map;
    }

    
    public ResultSetPage findPaginatedDetalhesColeta(Map criteria) {
		Long idPedidoColeta = (Long)criteria.get("idPedidoColeta");
    	ResultSetPage rsp = detalheColetaService.findPaginatedByDetalhesColeta(idPedidoColeta, FindDefinition.createFindDefinition(criteria));
    	List list = rsp.getList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		TypedFlatMap tfm = (TypedFlatMap)iter.next();
    		Map map = new HashMap();
    		
    		map.put("idDetalheColeta", tfm.get("idDetalheColeta"));
    		map.put("nmMunicipio", tfm.get("municipio.nmMunicipio"));
    		map.put("sgFilialDestino", tfm.get("filial.sgFilial"));
    		map.put("dsNaturezaProduto", tfm.get("naturezaProduto.dsNaturezaProduto"));
    		map.put("tpIdentificacaoCliente", tfm.get("cliente.pessoa.tpIdentificacao.value"));
    		
    		String nrIdentificacao = tfm.getString("cliente.pessoa.nrIdentificacao");
            if (nrIdentificacao != null && !nrIdentificacao.equals("")) {
            	map.put("nrIdentificacaoFormatadoCliente", 
                	FormatUtils.formatIdentificacao(tfm.getString("cliente.pessoa.tpIdentificacao.value"), nrIdentificacao));
            }
            
    		map.put("nmPessoaCliente", tfm.get("cliente.pessoa.nmPessoa"));
    		map.put("sgServico", tfm.get("servico.sgServico"));
    		map.put("tpFrete", tfm.getVarcharI18n("tpFrete.description").toString());
    		map.put("psAforado", tfm.get("psAforado"));
    		map.put("qtVolumes", tfm.get("qtVolumes"));
    		
    		map.put("siglaSimbolo", tfm.getString("moeda.sgMoeda") + " " + tfm.getString("moeda.dsSimbolo"));
    		
    		map.put("vlMercadoria", tfm.get("vlMercadoria"));
    		map.put("sgFilialCotacao", tfm.get("cotacao.filial.sgFilial"));
    		map.put("nrCotacao", tfm.get("cotacao.nrCotacao"));
    		map.put("sgPaisCtoInternacional", tfm.get("ctoInternacional.sgPais"));
    		map.put("nrCrt", tfm.get("ctoInternacional.nrCrt"));
    		map.put("obPedidoColeta", tfm.get("pedidoColeta.obPedidoColeta"));
    		
    		List listMap = new ArrayList();
    		listMap.add(map);
    		
    		rsp.setList(listMap);
    	}
    	
    	return rsp;
    }


    public Integer getRowCountDetalhesColeta(Map criteria) {
    	Long idPedidoColeta = (Long)criteria.get("idPedidoColeta");
    	return detalheColetaService.getRowCountByDetalhesColeta(idPedidoColeta);
    }
}