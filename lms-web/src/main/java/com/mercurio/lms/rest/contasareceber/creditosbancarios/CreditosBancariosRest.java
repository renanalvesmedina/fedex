package com.mercurio.lms.rest.contasareceber.creditosbancarios;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.CreditoBancarioEntity;
import com.mercurio.lms.contasreceber.model.service.CreditoBancarioService;
import com.mercurio.lms.contasreceber.model.service.RetornoOcorrenciaBoletoBancoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.BeanUtils;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.configuracoes.dto.BancoSuggestDTO;
import com.mercurio.lms.rest.contasareceber.creditosbancarios.dto.CreditosBancariosDTO;
import com.mercurio.lms.rest.contasareceber.creditosbancarios.dto.CreditosBancariosFilterDTO;
import com.mercurio.lms.rest.contasareceber.creditosbancarios.dto.LiberarCreditoBancarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.utils.ExportUtils;
import com.mercurio.lms.util.session.SessionUtils;
 
@Path("/contasareceber/creditosBancarios") 
public class CreditosBancariosRest extends LmsBaseCrudReportRest<CreditosBancariosDTO, CreditosBancariosDTO, CreditosBancariosFilterDTO> { 
 
	private static final Integer LIMIT_CSV_REPORT = 5000;

	@InjectInJersey	
	private CreditoBancarioService creditoBancarioService;

	@InjectInJersey
	private RetornoOcorrenciaBoletoBancoService retornoOcorrenciaBoletoBancoService; 

	@InjectInJersey
	private FilialService filialService; 

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	
	@Override 
	protected CreditosBancariosDTO findById(Long id) { 
		CreditoBancarioEntity creditoBancario = (CreditoBancarioEntity) creditoBancarioService.findById(id);
		Filial f = filialService.findByIdInitLazyProperties(creditoBancario.getFilial().getIdFilial(), true);
		
		return converteParaCreditoBancarioDTO(creditoBancario, f.getPessoa().getNmFantasia());
	} 
 
	@Override 
	protected Long store(CreditosBancariosDTO dto) { 
		
		//LMSA-2234
		if((null != dto.getTpClassificacao() && "OU".equals(dto.getTpClassificacao().getValue())) && (null == dto.getObCreditoBancario() || dto.getObCreditoBancario().isEmpty())) {
			throw new BusinessException("LMS-36376");
		}

		CreditoBancarioEntity creditoBancario = (CreditoBancarioEntity) creditoBancarioService
				.store(convertDtoToMap(dto), SessionUtils.getUsuarioLogado());
		
		return creditoBancario.getIdCreditoBancario();
	} 
 
	@Override 
	protected void removeById(Long id) { 
		creditoBancarioService.removeById(id);
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
		for (Long id : ids) {
			removeById(id);
		}
	} 
 
	@Override 
	protected List<CreditosBancariosDTO> find(CreditosBancariosFilterDTO filter) { 
		validateCamposObrigatorios(filter);
		
		Map<String, Object> map = convertFilterToMap(filter);
		List<CreditoBancarioEntity> creditosBancarios = creditoBancarioService.find(map);
		
		return converteParaCreditoBancarioDTO(creditosBancarios);
	} 
 
	@Override 
	protected Integer count(CreditosBancariosFilterDTO filter) {

		validateCamposObrigatorios(filter);
		
		return creditoBancarioService.findCount(convertFilterToMap(filter));
	} 

	@Override 
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> map = new ArrayList<Map<String,String>>();
		
		map.add(createColumn("filial"));
		map.add(createColumn("banco"));
		map.add(createColumn("dtCredito"));
		map.add(createColumn("vlCredito"));
		map.add(createColumn("vlSaldo", "saldo"));
		map.add(createColumn("tpModalidade", "modalidade"));
		map.add(createColumn("tpOrigem"));
		map.add(createColumn("tpClassificacao", "classificacao"));
		map.add(createColumn("tpSituacao"));
		map.add(createColumn("dsCpfCnpj", "cpfCnpj"));
		map.add(createColumn("dsNomeRazaoSocial", "nomeRazaoSocial"));
		map.add(createColumn("dsBoleto", "numeroBoleto"));
		map.add(createColumn("obCreditoBancario", "observacoes"));
		map.add(createColumn("dhAlteracao", "dtAlteracao"));
		map.add(createColumn("usuario"));
		
		return map; 
	} 
 
	//LMSA-2234
	private List<Map<String, String>> getColumnsSaldo() {
		List<Map<String, String>> map = new ArrayList<Map<String,String>>();
		
		map.add(createColumn("idCreditoBancario"));
		map.add(createColumn("filial"));
		map.add(createColumn("banco"));
		map.add(createColumn("nmBanco"));
		map.add(createColumn("dtCredito"));
		map.add(createColumn("tpModalidade", "modalidade"));
		map.add(createColumn("tpOrigem"));
		map.add(createColumn("tpClassificacao", "classificacao"));
		map.add(createColumn("tpSituacao"));
		map.add(createColumn("dsCpfCnpj", "cpfCnpj"));
		map.add(createColumn("dsNomeRazaoSocial", "nomeRazaoSocial"));
		map.add(createColumn("dsBoleto", "numeroBoleto"));
		map.add(createColumn("obCreditoBancario", "observacoes"));
		map.add(createColumn("vlCredito"));
		map.add(createColumn("vlUtilizado"));
		map.add(createColumn("vlSaldo"));
			
		return map; 
	} 
	
	/**
	 * LMSA-2234
	 * 
	 * Já havia uma exportação csv que invocava este método e montava o relatório com outro cabeçalho.
	 * Por hora foi necessário 
	 * 
	 * @param list
	 * @return
	 */
	private Response createFileSaldo(List<Map<String, Object>> list) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		if (list.isEmpty()) {
			responseDTO.setInfo(getLabel("grid.paginacao.nenhum-registro").replace("<BR>", ""));
			return Response.ok(responseDTO).build();
		}
		
		try {
			responseDTO.setFileName(ExportUtils.exportCsv(getReportOutputDir(), "CSV", list, getColumnsSaldo()));
		} catch (IOException e) {
			responseDTO.setError(getLabel("fileReportError") + e.getMessage());
		}
		
		return Response.ok(responseDTO).build();
	}


	private Map<String, String> createColumn(String value, String title) {
		Map<String, String> coluna1 = new HashMap<String, String>();
		
		coluna1.put("title", getLabel(title));
		coluna1.put("column", value);
		
		return coluna1;
	}

	private Map<String, String> createColumn(String string) {
		return createColumn(string, string);
	}
	
	@Override
	@POST
	@Path("reportCsv")
	public Response reportCsvRest(CreditosBancariosFilterDTO filter) {
		if (!isValidLimit(filter, LIMIT_CSV_REPORT, count(filter))) {
			return getException();
		}
		
		return createFile(findDataForReport(filter));
	}


	//Jira LMSA-2234
	@POST
	@Path("reportSaldoCsv")
	public Response reportSaldoCsvRest(LiberarCreditoBancarioDTO filter) {
		String dtCorte = filter.getDataDeCorte().toString();
		List<Map<String, Object>> content = creditoBancarioService.findCreditosBancariosByDataCorte(dtCorte);

		return createFileSaldo(content);
		
	}	

	
	@Override 
	protected List<Map<String, Object>> findDataForReport(CreditosBancariosFilterDTO filter) {
		
		validateCamposObrigatorios(filter);
		
		List<Map<String, Object>> content = new ArrayList<Map<String,Object>>();
		
		for (CreditoBancarioEntity creditoBancario : creditoBancarioService.find(convertFilterToMap(filter))) {
			content.add(creditoBancario.toMap());
		}
		
		return content; 
	} 

	private void validateCamposObrigatorios(CreditosBancariosFilterDTO filter) {
		if(!isDtCreditoInicialPreenchido(filter)){
			throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("dtCredito") + " Inicial"});
		}
		if(!isDtCreditoFinalPreenchido(filter)){
			throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("dtCredito") + " Final"});
		}
		
		if(!isAoMenosUmCampoAdicionalPreenchido(filter)){
			throw new BusinessException("LMS-36368");
		}
	
	}

	private boolean isAoMenosUmCampoAdicionalPreenchido(
			CreditosBancariosFilterDTO filter) {
		return filter.getIdFilialCredito() != null ||
				filter.getBanco() != null ||
				filter.getDataAlteracaoInicial()!= null ||
				filter.getDataAlteracaoFinal() != null ||
				filter.getTpModalidade() != null ||
				filter.getTpOrigem() != null ||
				filter.getVlCreditoInicial() != null ||
				filter.getVlCreditoFinal() != null ||
				filter.getVlSaldoInicial() != null ||
				filter.getVlSaldoFinal() != null ||
				StringUtils.isNotBlank(filter.getDsCpfCnpj()) ||
				StringUtils.isNotBlank(filter.getDsBoleto()) ||
				StringUtils.isNotBlank(filter.getDsNomeRazaoSocial()) ||
				StringUtils.isNotBlank(filter.getObCreditoBancario()) ||
				filter.getTpSituacao() != null;
	}

	private boolean isDtCreditoFinalPreenchido(
			CreditosBancariosFilterDTO filter) {
		return filter.getDataCreditoFinal() != null;
	}

	private boolean isDtCreditoInicialPreenchido(CreditosBancariosFilterDTO filter) {
		return filter.getDataCreditoInicial() != null;
	}

	private List<CreditosBancariosDTO> converteParaCreditoBancarioDTO(List<CreditoBancarioEntity> creditosBancarios) {
		List<CreditosBancariosDTO> dtos = new ArrayList<CreditosBancariosDTO>();
		
		for (CreditoBancarioEntity creditoBancario : creditosBancarios) {
			dtos.add(converteParaCreditoBancarioDTO(creditoBancario, creditoBancario.getFilial().getSiglaNomeFilial()));
		}
		
		return dtos;
	}
	
	private CreditosBancariosDTO converteParaCreditoBancarioDTO(CreditoBancarioEntity creditoBancario, String filialNomeFantasia) {

		Filial f = creditoBancario.getFilial();
		FilialSuggestDTO filial = new FilialSuggestDTO(f.getIdFilial(), filialNomeFantasia, f.getSgFilial(), "", 0L);
		
		BancoSuggestDTO banco = new BancoSuggestDTO();
		banco.setIdBanco(creditoBancario.getBanco().getIdBanco());
		banco.setNrBanco(creditoBancario.getBanco().getNrBanco());
		banco.setNmBanco(creditoBancario.getBanco().getNmBanco());

		
		Long idUsuario = creditoBancario.getUsuario().getIdUsuario();
		String nmUsuario = creditoBancario.getUsuario().getUsuarioADSM().getNmUsuario();
		String nrMatricula = creditoBancario.getUsuario().getUsuarioADSM().getNrMatricula();
		
		UsuarioDTO usuario = new UsuarioDTO(idUsuario, nmUsuario, nrMatricula);
		
		Long id = creditoBancario.getIdCreditoBancario();
		YearMonthDay dtCredito = creditoBancario.getDtCredito();
		BigDecimal vlCredito = creditoBancario.getVlCredito();
		BigDecimal vlSaldo = creditoBancario.getSaldo();
		DomainValue tpModalidade = creditoBancario.getTpModalidade();
		DomainValue tpOrigem = creditoBancario.getTpOrigem();
		DomainValue tpClassificacao = creditoBancario.getTpClassificacao();
		DomainValue tpSituacao = creditoBancario.getTpSituacao();
		String dsBoleto = creditoBancario.getDsBoleto();
		String obCreditoBancario = creditoBancario.getObCreditoBancario();
		String dtAlteracao = creditoBancario.getDhAlteracao().toString(DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
		String dsCpfCnpj = creditoBancario.getDsCpfCnpj();
		String dsNomeRazaoSocial = creditoBancario.getDsNomeRazaoSocial();
		
		return new CreditosBancariosDTO(id, filial, banco, usuario, dtCredito,
				vlCredito, vlSaldo, tpModalidade, tpOrigem, tpClassificacao, tpSituacao,
				dsBoleto, obCreditoBancario, dtAlteracao, dsCpfCnpj,
				dsNomeRazaoSocial);
	}

	public CreditoBancarioService getCreditoBancarioService() {
		return creditoBancarioService;
	}
	
	public void setCreditoBancarioService(CreditoBancarioService creditoBancarioService) {
		this.creditoBancarioService = creditoBancarioService;
	}
	
	@POST
	@Path("carregarValoresPadrao")
	public Response carregarValoresPadrao() {
		TypedFlatMap retorno = new TypedFlatMap();
		Filial filialLogada = SessionUtils.getFilialSessao();
		
		retorno.put("idFilial", filialLogada.getIdFilial());
		retorno.put("sgFilial", filialLogada.getSgFilial());
		retorno.put("nmFilial", filialLogada.getPessoa().getNmFantasia());
		
		return Response.ok(retorno).build();
	}

	private Map<String, Object> convertFilterToMap(CreditosBancariosFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("idFilial", filter.getIdFilialCredito() == null ? null : filter.getIdFilialCredito().getId());
		map.put("dataCreditoInicial", filter.getDataCreditoInicial());
		map.put("dataCreditoFinal", filter.getDataCreditoFinal());
		map.put("tpModalidade", filter.getTpModalidade());
		map.put("tpOrigem", filter.getTpOrigem());
		map.put("tpClassificacao", filter.getTpClassificacao());
		map.put("vlCreditoInicial", filter.getVlCreditoInicial());
		map.put("vlCreditoFinal", filter.getVlCreditoFinal());
		map.put("dsCpfCnpj", filter.getDsCpfCnpj());
		map.put("dsNomeRazaoSocial", filter.getDsNomeRazaoSocial());
		map.put("idBanco", filter.getBanco() == null ? null : filter.getBanco().getIdBanco());
		map.put("dataAlteracaoInicial", filter.getDataAlteracaoInicial());
		map.put("dataAlteracaoFinal", filter.getDataAlteracaoFinal());
		map.put("vlSaldoInicial", filter.getVlSaldoInicial());
		map.put("vlSaldoFinal", filter.getVlSaldoFinal());
		map.put("dsBoleto", filter.getDsBoleto());
		map.put("obCreditoBancario", filter.getObCreditoBancario());
		map.put("tpSituacaoFilter", filter.getTpSituacao());

		return map;
	}

	private  Map<String, Object> convertDtoToMap(CreditosBancariosDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("idCreditoBancario", dto.getId());
		map.put("filial", dto.getFilial() == null ? null : dto.getFilial().getIdFilial());
		map.put("banco",  dto.getBanco() == null ? null : dto.getBanco().getIdBanco());
		map.put("usuario", dto.getUsuario() == null ? null : dto.getUsuario().getIdUsuario());
		map.put("dtCredito", dto.getDtCredito());
		map.put("vlCredito", dto.getVlCredito());
		map.put("saldo", dto.getSaldo());
		map.put("tpModalidade", dto.getTpModalidade());
		map.put("tpOrigem", dto.getTpOrigem());
		map.put("tpClassificacao", dto.getTpClassificacao());
		map.put("tpSituacao", dto.getTpSituacao());
		map.put("dsCpfCnpj", dto.getDsCpfCnpj());
		map.put("dsNomeRazaoSocial", dto.getDsNomeRazaoSocial());
		map.put("dsBoleto", dto.getDsBoleto());
		map.put("obCreditoBancario", dto.getObCreditoBancario());
		map.put("dtAlteracao", dto.getDtAlteracao());
		
		return map;
	}
	

	//LMSA-2234
	private File getReportOutputDir() {
		ReportExecutionManager reportExecutionManager = BeanUtils.getBean(ReportExecutionManager.class);
		return reportExecutionManager.getReportOutputDir();
	}

	
	@POST
	@Path("liberarCreditoBancario")
	public Response liberarCreditoBancario(LiberarCreditoBancarioDTO data){
		
		creditoBancarioService.executeLiberarCreditoBancario(
				data.getIdCreditoBancario(), data.getDataDeCorte());
		return  Response.ok().build();
	}

} 
