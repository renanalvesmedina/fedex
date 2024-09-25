package com.mercurio.lms.rest.expedicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.BeanUtils;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.utils.ExportUtils;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.session.SessionUtils;
 
@Path("/expedicao/relatorioReceitaPotencial") 
public class RelatorioReceitaPotencialRest extends LmsBaseCrudReportRest<RelatorioReceitaPotencialDTO, RelatorioReceitaPotencialDTO, RelatorioReceitaPotencialFilterDTO> { 

	@InjectInJersey
	private RegionalFilialService regionalFilialService;

	@InjectInJersey
	private RegionalService regionalService;

	@InjectInJersey
	private ServicoService servicoService;

	@InjectInJersey
	private ParcelaPrecoService parcelaPrecoService;

	@InjectInJersey
	private PreFaturaServicoService preFaturaServicoService;

	@Override 
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> map = new ArrayList<Map<String,String>>();

		map.add(createColumn("nrDoctoServico", "Documento de Serviço"));
		map.add(createColumn("nrOrdemServico", "Ordem de Serviço"));
		map.add(createColumn("filialOrdemServico", "Filial O.S."));
		map.add(createColumn("sgRegionalExecucao", "Regional da Filial O.S."));
		map.add(createColumn("tpModal", "Modal"));
		map.add(createColumn("nrIdentificacaoPessoaDestino", "Cliente Destino"));
		map.add(createColumn("nomePessoaDestino", "Nome Cliente Destino"));
		map.add(createColumn("nmUsuarioPromotor", "Executivo"));
		map.add(createColumn("nrIdentificacaoTomador", "Cliente Tomador"));
		map.add(createColumn("nmPessoaTomador", "Nome Coiente Tomador/Devedor"));
		map.add(createColumn("filialTomador", "Filial Cliente Tomador/Devedor"));
		map.add(createColumn("dtExecucao", "Data Execução"));
		map.add(createColumn("dtSolicitacaoOrdemServico", "Data da O.S."));
		map.add(createColumn("nmParcelaPreco", "Serviço Adicional"));
		map.add(createColumn("vlCusto", "Valor Custo"));
		map.add(createColumn("vlCobrar", "Valor a Cobrar"));
		map.add(createColumn("vlTabela", "Valor Tabela Cheia"));
		
		return map; 
	} 
	
	private Map<String, String> createColumn(String column, String value) {
		Map<String, String> coluna1 = new HashMap<String, String>();
		
		coluna1.put("title", value);
		coluna1.put("column", column);
		
		return coluna1;
	}

	@Override 
	protected List<Map<String, Object>> findDataForReport(RelatorioReceitaPotencialFilterDTO filter) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		
		criteria.put("idRegional", filter.getIdRegional());
		criteria.put("idFilial", filter.getIdFilialCobranca() == null ? null : filter.getIdFilialCobranca().getId());
		criteria.put("tpModal", filter.getModal() == null ? null : filter.getModal().getValue());
		criteria.put("tpAbrangencia", filter.getAbrangencia() == null ? null : filter.getAbrangencia().getValue());
		criteria.put("idParcelaPreco", filter.getIdParcelaPreco());
		criteria.put("idCliente", filter.getCliente() == null ? null : filter.getCliente().getIdCliente());
		criteria.put("idUsuario", filter.getExecutivo() == null ? null : filter.getExecutivo().getIdCliente());
		criteria.put("dtInicial", filter.getPeriodoInicial());
		criteria.put("dtFinal", filter.getPeriodoFinal());
		
		List<Map<String, Object>> dadosRelatorio = preFaturaServicoService.findDadosRelatorioReceitaPotencial(criteria);
		
		return dadosRelatorio; 
	} 
 
	@POST
	@Path("carregarValoresPadrao")
	public Response carregarValoresPadrao() {
		TypedFlatMap retorno = new TypedFlatMap();
		Filial filialLogada = SessionUtils.getFilialSessao();
		
		retorno.put("idFilial", filialLogada.getIdFilial());
		retorno.put("sgFilial", filialLogada.getSgFilial());
		retorno.put("nmFilial", filialLogada.getPessoa().getNmFantasia());
		retorno.put("idRegional", regionalFilialService.findRegional(SessionUtils.getFilialSessao().getIdFilial()).getIdRegional());
		retorno.put("regionais", regionalService.findRegionaisVigentes());
		retorno.put("servicosAdicionais", parcelaPrecoService.findServicosAdicionaisParcela());
		
		return Response.ok(retorno).build();
	}

	@POST
	@Path("reportCsv")
	public Response reportCsvRest(RelatorioReceitaPotencialFilterDTO filter) {
		List<Map<String, Object>> list = findDataForReport(filter);
		ResponseDTO responseDTO = new ResponseDTO();
		if (list.isEmpty()) {
			responseDTO.setInfo(getLabel("grid.paginacao.nenhum-registro").replace("<BR>", ""));
			return Response.ok(responseDTO).build();
		}
		
		try {
			responseDTO.setFileName(ExportUtils.exportCsv(
					BeanUtils.getBean(ReportExecutionManager.class)
							.getReportOutputDir(), "CSV", list, getColumns()));
		} catch (IOException e) {
			responseDTO.setError(getLabel("fileReportError") + e.getMessage());
		}
		
		return Response.ok(responseDTO).build();
	}

	@Override 
	protected RelatorioReceitaPotencialDTO findById(Long id) { 
		return null; 
	} 
 
	@Override 
	protected Long store(RelatorioReceitaPotencialDTO bean) { 
		return null; 
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	@Override 
	protected List<RelatorioReceitaPotencialDTO> find(RelatorioReceitaPotencialFilterDTO filter) { 
		return null; 
	} 
 
	@Override 
	protected Integer count(RelatorioReceitaPotencialFilterDTO filter) { 
		return null; 
	} 

} 
