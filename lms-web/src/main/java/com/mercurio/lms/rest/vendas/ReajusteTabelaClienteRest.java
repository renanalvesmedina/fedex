package com.mercurio.lms.rest.vendas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.vendas.dto.ReajusteTabelaClienteDTO;
import com.mercurio.lms.rest.vendas.dto.ReajusteTabelaClienteFilterDTO;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ReajusteCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ReajusteClienteService;

@Path("/vendas/reajustetabelacliente")
public class ReajusteTabelaClienteRest extends BaseCrudRest<ReajusteTabelaClienteDTO, ReajusteTabelaClienteDTO, ReajusteTabelaClienteFilterDTO> { 

	private static final String EFETIVADO = "S";
	private static final int FISRT = 0;
	
	@InjectInJersey
	private ReajusteClienteService reajusteClienteService;
	
	@Override
	protected Integer count(ReajusteTabelaClienteFilterDTO filter) {
		return find(filter).size();
	}
	
	@Override
	protected List<ReajusteTabelaClienteDTO> find(ReajusteTabelaClienteFilterDTO filter) {
		List<Map<String,Object>> list = reajusteClienteService.findReajusteService(filter.getId(), filter.getNrIdentificacaoEmpregador(), filter.getIdFilial(), filter.getDataReajuste());
		return createListReajusteTabelaClienteDTO(list);
	}
	
	private List<ReajusteTabelaClienteDTO> createListReajusteTabelaClienteDTO(List<Map<String,Object>> list){
		List<ReajusteTabelaClienteDTO> retVal = new ArrayList<ReajusteTabelaClienteDTO>();
		for (Map<String,Object> map : list) {
			retVal.add(createReajusteTabelaClienteDTO(map));
		}
		return retVal;
	}
	
	@Override
	protected ReajusteTabelaClienteDTO findById(Long id) {
		List<Map<String,Object>> list = reajusteClienteService.findByIdReajusteService(id);
		List<ReajusteTabelaClienteDTO> listDto = createListReajusteTabelaClienteDTO(list);

		return CollectionUtils.isEmpty(listDto) ? new ReajusteTabelaClienteDTO() : listDto.get(FISRT);
	}
	
	@GET
	@Path("findDivisaoCliente")
	public List<Map<String, Object>> listDivisoesClientes(@QueryParam("nrIdentificacao") String nrIdentificacao){
		return reajusteClienteService.listDivisoesCliente(nrIdentificacao);
	}
	
	@GET
	@Path("findNomeFantasiaCliente")
	public String findNomeFantasiaCliente(@QueryParam("nrIdentificacao") String nrIdentificacao){
		return reajusteClienteService.findNomeFantasiaCliente(nrIdentificacao);
	}
	
	@GET
	@Path("findTabelaBaseCliente")
	public String findTabelaBaseCliente(@QueryParam("idDivisaoCliente") Long idDivisaoCliente){
		return reajusteClienteService.getTabelaBase(idDivisaoCliente);
	}
	
	@GET
	@Path("findPercentualSugerido")
	public BigDecimal findPercentualSugerido(@QueryParam("idTabelaDivisaocliente") Long idTabelaDivisaocliente, @QueryParam("idTabelaNova") Long idTabelaNova){
		return reajusteClienteService.getPercentualSugerido(idTabelaDivisaocliente, idTabelaNova);
	}
	
	@POST
	@Path("efetivarReajuste")
	public Response efetivarReajuste(ReajusteTabelaClienteDTO dto){
		reajusteClienteService.executeEfetivarReajuste(dto.getId(), dto.getIdTabDivisaoCliente(), dto.getDataReajuste(), dto.getPercAcordado(), dto.getJustificativa());
		return Response.ok().build();
	}
	
	@POST
	@Path("aprovarReajuste")
	public Response aprovarReajuste(ReajusteTabelaClienteDTO dto){
		reajusteClienteService.executeWorkflow(dto.getId());
		return Response.ok().build();
	}
	
	@GET
	@Path("efetivarAsyncReajuste")
	public Response efetivarAsyncReajuste(){
		reajusteClienteService.executeAsyncEfetivacao();
		return Response.ok().build();
	}
	
	@POST
	@Path("listGeneralidadesObrig")
	public List<Map<String, Object>> listGeneralidadesObrigatorias(ReajusteTabelaClienteDTO dto){
		return reajusteClienteService.listGeneralidadesObrigatorias(dto.getIdTabDivisaoCliente(), dto.getDataReajuste());
	}
	
	@POST
	@Path("saveGeneralidadesObrig")
	public void saveGeneralidadesObrigatorias(List<Map<String,Object>> listParams){
		reajusteClienteService.saveGeneralidadesObrigatorias(listParams); 
	}
	
	@POST
	@Path("listFretePeso")
	public List<Map<String, Object>> listFretePeso(ReajusteTabelaClienteDTO dto){
		return reajusteClienteService.listFretePeso(dto.getIdTabDivisaoCliente(), dto.getDataReajuste());
	}
	
	@POST
	@Path("saveFretePeso")
	public void saveFretePeso(List<Map<String,Object>> listParams){
		reajusteClienteService.saveFretePeso(listParams); 
	}
	
	@POST
	@Path("listGeneralidade")
	public List<Map<String, Object>> listGeneralidade(ReajusteTabelaClienteDTO dto){
		return reajusteClienteService.listGeneralidades(dto.getIdTabDivisaoCliente(), dto.getDataReajuste());
	}
	
	@POST
	@Path("saveGeneralidade")
	public void saveGeneralidade(List<Map<String,Object>> listParams){
		reajusteClienteService.saveGeneralidades(listParams); 
	}
	
	@POST
	@Path("listTaxaCliente")
	public List<Map<String, Object>> listTaxaCliente(ReajusteTabelaClienteDTO dto){
		return reajusteClienteService.listTaxaCliente(dto.getIdTabDivisaoCliente(), dto.getDataReajuste());
	}
	
	@POST
	@Path("saveTaxaCliente")
	public void saveTaxaCliente(List<Map<String,Object>> listParams){
		reajusteClienteService.saveTaxaCliente(listParams); 
	}
	
	@POST
	@Path("listServAdicionalCliente")
	public List<Map<String, Object>> listServAdicionalCliente(ReajusteTabelaClienteDTO dto){
		return reajusteClienteService.listServAdicionalCliente(dto.getIdTabDivisaoCliente(), dto.getDataReajuste(), dto.getId());
	}
	
	@POST
	@Path("saveServAdicionalCliente")
	public void saveServicoAdicionalCliente(List<Map<String,Object>> listParams){
		reajusteClienteService.saveServicoAdicionalCliente(listParams); 
	}
	
	@Override
	protected void removeById(Long idReajusteCliente) {
		ReajusteTabelaClienteDTO reajuste = findById(idReajusteCliente);
		reajusteClienteService.removeReajusteCliente(reajuste.getId(), reajuste.getIdTabDivisaoCliente(), reajuste.getDataReajuste());
	}
	
	@Override
	protected void removeByIds(List<Long> arg0) {
	}
	
	@Override
	protected Long store(ReajusteTabelaClienteDTO dto) {
		return reajusteClienteService.saveReajusteCliente(createReajusteCliente(dto)); 
	}
	
	private boolean getBooleanValue(Object value){
		return value != null && EFETIVADO.equalsIgnoreCase(value.toString());
	}

	private ReajusteCliente createReajusteCliente(ReajusteTabelaClienteDTO dto){
		if(dto.getId() == null){
			return createNovoReajusteCliente(dto);
		}
		
		return createUpdateReajusteCliente(dto);
	}
	
	private ReajusteCliente createUpdateReajusteCliente(ReajusteTabelaClienteDTO dto){
		ReajusteCliente rc = new ReajusteCliente();
		rc.setIdReajusteCliente(dto.getId());
		rc.setDsJustificativa(dto.getJustificativa());
		rc.setDtInicioVigencia(dto.getDataReajuste());
		rc.setPcReajusteAcordado(dto.getPercAcordado());
		
		TabelaDivisaoCliente tdc =  new TabelaDivisaoCliente();
		tdc.setIdTabelaDivisaoCliente(dto.getIdTabDivisaoCliente());
		rc.setTabelaDivisaoCliente(tdc);
		
		return rc;
	}
	
	private ReajusteCliente createNovoReajusteCliente(ReajusteTabelaClienteDTO dto){
		ReajusteCliente rc = new ReajusteCliente();
		rc.setIdReajusteCliente(dto.getId());
		rc.setBlEfetivado(dto.getEfetivado());
		rc.setDsJustificativa(dto.getJustificativa());
		rc.setDtAprovacao(dto.getDataEfetivacao());
		rc.setDtInicioVigencia(dto.getDataReajuste());
		rc.setPcReajusteSugerido(dto.getPercSugerido());
		rc.setPcReajusteAcordado(dto.getPercAcordado());
		rc.setBlEfetivado(false);
		
		Filial filial = new Filial();
		filial.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		rc.setFilial(filial);
		
		TabelaDivisaoCliente tdc =  new TabelaDivisaoCliente();
		tdc.setIdTabelaDivisaoCliente(dto.getIdTabDivisaoCliente());
		rc.setTabelaDivisaoCliente(tdc);
		
		DivisaoCliente dc = new DivisaoCliente();
		dc.setIdDivisaoCliente(dto.getIdDivisaoCliente());
		rc.setDivisaoCliente(dc);

		TabelaPreco tp = new TabelaPreco();
		tp.setIdTabelaPreco(dto.getIdTabelaNova());
		rc.setTabelaPreco(tp);
		
		Cliente c = new Cliente();
		c.setIdCliente(reajusteClienteService.findIdClienteByIdentificacao(dto.getNrIdentificacao()));
		rc.setCliente(c);
		
		Long nrReajuste = reajusteClienteService.executeGetNrReajuste(filial.getIdFilial());
		rc.setNrReajuste(nrReajuste == null ? 1 : nrReajuste + 1);
		
		rc.setTpSituacaoAprovacao(dto.getSituacaoWorkflow() == null ? null : new DomainValue(dto.getSituacaoWorkflow())); 
		
		return rc;
	}
	
	private ReajusteTabelaClienteDTO createReajusteTabelaClienteDTO(Map<String,Object> map){
		ReajusteTabelaClienteDTO dto = new ReajusteTabelaClienteDTO();  
		dto.setDataReajuste( map.get("dataReajuste") == null ? null : new YearMonthDay(((Date)map.get("dataReajuste")).getTime()));
		dto.setDataEfetivacao( map.get("dataEfetivacao") == null ? null : new YearMonthDay(((Date)map.get("dataEfetivacao")).getTime()));
		dto.setSgFilial((String) map.get("sgFilial")); 
		dto.setDivisaoCliente((String) map.get("divisaoCliente"));
		dto.setNome((String) map.get("nome"));
		dto.setNrIdentificacao((String) map.get("nrIdentificacao"));
		dto.setPercAcordado((BigDecimal) map.get("percAcordado"));
		dto.setPercSugerido((BigDecimal) map.get("percSugerido"));
		dto.setTabelaAtual((String) map.get("tabelaAtual"));
		dto.setTabelaNova((String) map.get("tabelaNova"));
		dto.setNumeroReajuste((String) map.get("numeroReajusteFilial"));
		dto.setId((Long) map.get("id")); 
		dto.setEfetivado( getBooleanValue(map.get("efetivado") ));
		dto.setSituacaoWorkflow(map.get("situacaoWorkflow") == null ? null : (String) map.get("situacaoWorkflow"));
		dto.setJustificativa(map.get("justificativa") == null ? null : (String) map.get("justificativa"));
		dto.setIdDivisaoCliente((Long) map.get("idDivisaoCliente"));
		dto.setNrReajuste((Long) map.get("nrReajuste"));
		dto.setIdTabDivisaoCliente((Long) map.get("idTabDivisaoCliente"));
		dto.setIdFilial((Long) map.get("idFilial"));
		dto.setIdCliente((Long) map.get("idCliente"));
		dto.setIdTabelaNova((Long) map.get("idTabelaPreco"));
		dto.setIdUsuario(map.get("idUsuario") == null ? null : (Long) map.get("idUsuario"));
		dto.setNomeUsuario(map.get("nomeUsuario") == null ? null : (String) map.get("nomeUsuario"));
		dto.setSituacaoWorkflowDesc(map.get("situacaoWorkflowDesc") == null ? "" : (String) map.get("situacaoWorkflowDesc"));
		dto.setIdPendeciaAprovacao(map.get("idPendenciaAprovacao") == null ? null : (Long) map.get("idPendenciaAprovacao"));
		return dto;
	}
	
	public void setReajusteClienteService(ReajusteClienteService reajusteClienteService) {
		this.reajusteClienteService = reajusteClienteService;
	}
	
}