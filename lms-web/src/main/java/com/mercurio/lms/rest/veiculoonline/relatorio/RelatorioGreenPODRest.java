package com.mercurio.lms.rest.veiculoonline.relatorio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.rest.BeanUtils;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.utils.ExportUtils;
import com.mercurio.lms.rest.veiculoonline.relatorio.dto.RelatorioGreenPODDTO;
import com.mercurio.lms.rest.veiculoonline.relatorio.dto.RelatorioGreenPODFilterDTO;
import com.mercurio.lms.rest.veiculoonline.relatorio.helper.RelatorioGreenPODRestHelper;
import com.mercurio.lms.veiculoonline.model.service.RelatorioGreenPODService;

@Path("/veiculoonline/relatorioGreenPOD")
public class RelatorioGreenPODRest extends LmsBaseCrudReportRest<RelatorioGreenPODDTO, RelatorioGreenPODDTO, RelatorioGreenPODFilterDTO> {

    @InjectInJersey
    private RelatorioGreenPODService relatorioGreenPODService;
    @InjectInJersey
    private ParametroGeralService parametroGeralService;
    
    @Override
    protected List<Map<String, String>> getColumns() {
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        listMap.add(getColumn("imagem", "existe_imagem")); 
        listMap.add(getColumn("dtEmissao", "data_emissao"));
        listMap.add(getColumn("filialEntrega", "filial_de_entrega"));
        listMap.add(getColumn("cnpjRemetente", "cnpj_remetente"));
        listMap.add(getColumn("cnpjDestinatario", "cnpj_destinatario"));
        listMap.add(getColumn("nrControleCarga", "nr_controle_carga"));
        listMap.add(getColumn("manifestoEntrega", "nr_manifesto_entrega"));
        listMap.add(getColumn("regional", "regional"));
        listMap.add(getColumn("municipioEntrega", "municipio_entrega"));
        listMap.add(getColumn("meioTransporteRastreado", "veiculo_rastreado")); 
        listMap.add(getColumn("placaVeiculo", "placa_veiculo"));
        listMap.add(getColumn("celular", "nr_celular"));
        listMap.add(getColumn("filialOrigem2", "filial_origem"));
        listMap.add(getColumn("cte", "ct_e"));
        listMap.add(getColumn("nroPedido", "nr_pedido_natura")); 
        listMap.add(getColumn("dataEntrega", "data_entrega")); 
        listMap.add(getColumn("horaEntrega", "hora_entrega")); 
        listMap.add(getColumn("baixaCelular", "baixa_celular")); 
        listMap.add(getColumn("nomeMotorista", "nome_motorista")); 
        listMap.add(getColumn("celularContato", "celular_contato")); 
        listMap.add(getColumn("horaLimite", "dh_inclusao_eds")); 
        listMap.add(getColumn("redespacho", "redespacho")); 
        listMap.add(getColumn("entregouDestinatario", "entregou_destinatario")); 
        
        return listMap;
    }

    @Override
    protected List<Map<String, Object>> findDataForReport(RelatorioGreenPODFilterDTO filterDTO) {
    	if(validarParametrosTela(filterDTO)){
    		throw new BusinessException("parametrosPesquisaNaoInformados");
    	}
    	ParametroGeral parametroIDSNatura = parametroGeralService.findByNomeParametro("IDS_NATURA");
    	Map<String, Object> criteria = RelatorioGreenPODRestHelper.toFilterMap(filterDTO,parametroIDSNatura);
    	return relatorioGreenPODService.findByParameters(criteria);
    }

    private boolean validarParametrosTela(RelatorioGreenPODFilterDTO filterDTO) {
    	return filterDTO.getDtPeriodoInicial() == null || filterDTO.getDtPeriodoFinal() == null;
    }

	@Override
    protected RelatorioGreenPODDTO findById(Long aLong) {
        return null;
    }

    @Override
    protected Long store(RelatorioGreenPODDTO monitoramentoAverbacaoDTO) {
        return null;
    }

    @Override
    protected void removeById(Long aLong) {
    	//Não faz nada, pois se trata apenas de uma geração de relatório.
    }

    @Override
    protected void removeByIds(List<Long> list) {
    	//Não faz nada, pois se trata apenas de uma geração de relatório.
    }

    @Override
    protected List<RelatorioGreenPODDTO> find(RelatorioGreenPODFilterDTO filterDTO) {
    	return new ArrayList<RelatorioGreenPODDTO>();
    }

    @Override
    protected Integer count(RelatorioGreenPODFilterDTO filterDTO) {
        return 0;
    }

    @Override
    protected Response createFile(List<Map<String, Object>> list) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (list.isEmpty()) {
            responseDTO.setInfo(getLabel("grid.paginacao.nenhum-registro").replace("<BR>", ""));
            return Response.ok(responseDTO).build();
        }

        try {
            String relatorio = "file.name:Relatorio_Green_POD_";
            responseDTO.setFileName(ExportUtils.exportCsv(getReportOutputDirectory(), relatorio, list, getColumns()));
        } catch (IOException e) {
            responseDTO.setError(getLabel("fileReportError") + e.getMessage());
        }

        return Response.ok(responseDTO).build();
    }
    
    protected File getReportOutputDirectory() {
            ReportExecutionManager reportExecutionManager = BeanUtils.getBean(ReportExecutionManager.class);
            return reportExecutionManager.getReportOutputDir();
    }
}