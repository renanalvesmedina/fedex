package com.mercurio.lms.coleta.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.coleta.emitirColetasAbertasAction"
 */

public class EmitirColetasAbertasAction {

    private FilialService filialService;
    private ServicoService servicoService;
    private RotaColetaEntregaService rotaColetaEntregaService;
    private ReportExecutionManager reportExecutionManager;
    private PedidoColetaService pedidoColetaService;
    
	public String executeExportacaoCsv(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(pedidoColetaService.executeExportacaoCsv(parameters, reportExecutionManager.generateOutputDir()));
	}

    /**
     * M�todo para verificar se o usuario logado tem acesso a filial selecionada
     */
    public List findLookupFiliaisPorUsuario(Map map) {
        List listFilialLookup = getFilialService().findLookup(map);

        return listFilialLookup;
    }

    /**
     * Verifica se o usuario tem acesso a uma �nica filial e retorna essa filial
     * para preencher a lookup de filiais
     */
    public TypedFlatMap verificaAcessoFilial(Map map) {
    	TypedFlatMap retorno = new TypedFlatMap();
        if (SessionUtils.getFiliaisUsuarioLogado().size() == 1) {
            Filial filial = (Filial) SessionUtils.getFiliaisUsuarioLogado().get(0);
            retorno.put("filial.idFilial", filial.getIdFilial() );
            retorno.put("filial.sgFilial", filial.getSgFilial());
            retorno.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());

        }
        return retorno;
    }

    /**
     * M�todo que popula a combo de servico
     * 
     * @param map
     * @return list
     */
    public List findComboServico(Map map) {
        return getServicoService().find(map);
    }

    /**
     * M�todo que busca a rota de coleta/entrega
     * 
     * @param map
     * @return list
     */
    public List findLookupRotaColetaEntrega(Map map) {
        ((Map)map.get("filial")).remove("sgFilial");
        ((Map)map.get("filial")).remove("pessoa");
        return getRotaColetaEntregaService().findLookup(map);
    }

    public FilialService getFilialService() {
        return filialService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public ServicoService getServicoService() {
        return servicoService;
    }

    public void setServicoService(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    public RotaColetaEntregaService getRotaColetaEntregaService() {
        return rotaColetaEntregaService;
    }

    public void setRotaColetaEntregaService(
            RotaColetaEntregaService rotaColetaEntregaService) {
        this.rotaColetaEntregaService = rotaColetaEntregaService;
    }

    public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

}