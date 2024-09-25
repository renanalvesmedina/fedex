package com.mercurio.lms.rest.contasareceber.diascortefaturamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.DiaCorteFaturamento;
import com.mercurio.lms.contasreceber.model.service.DiaCorteFaturamentoService;
import com.mercurio.lms.contasreceber.model.service.FaturamentoAutomaticoService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contasareceber.diascortefaturamento.dto.DiasCorteFaturamentoDTO;
import com.mercurio.lms.rest.contasareceber.diascortefaturamento.dto.DiasCorteFaturamentoFilterDTO;
import com.mercurio.lms.util.JTFormatUtils;
 
@Path("/contasareceber/diasCorteFaturamento") 
public class DiasCorteFaturamentoRest extends LmsBaseCrudReportRest<DiasCorteFaturamentoDTO, DiasCorteFaturamentoDTO, DiasCorteFaturamentoFilterDTO> { 
 
	@InjectInJersey DiaCorteFaturamentoService diaCorteFaturamentoService;
	@InjectInJersey FaturamentoAutomaticoService faturamentoAutomaticoService;
	
	@GET
	@Path("mostraSQL")
	public Response mostraSQL() {
		String sql = faturamentoAutomaticoService.executeFaturamentoAutomaticoSql();
		return Response.ok(sql, "application/octet-stream")
				.header("content-disposition",
						"attachment; filename = faturamento_automatico.sql").build();
	}
	
	@Override 
	protected List<Map<String, String>> getColumns() { 
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("dataCorte", "dtCorte"));
		list.add(getColumn("tpFaturarSemanal", "blSemanal"));
		list.add(getColumn("tpFaturarDecendial", "blDecendial"));
		list.add(getColumn("tpFaturarQuinzenal", "blQuinzenal"));
		list.add(getColumn("tpFaturarMensal", "blMensal"));
		list.add(getColumn("dataHoraAlteracao", "dataHoraAlteracao"));
		list.add(getColumn("usuarioAlteracao", "usuarioAlteracao"));
		return list;
	} 
	
	private TypedFlatMap convertFilterToTypedFlatMap(DiasCorteFaturamentoFilterDTO filter) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getId() != null) tfm.put("id_dia_corte_faturamento", filter.getId());
		if (filter.getDataCorteInicial() != null) tfm.put("dt_corte_inicial", filter.getDataCorteInicial());
		if (filter.getDataCorteFinal() != null) tfm.put("dt_corte_final", filter.getDataCorteFinal());
		if (filter.getDataAlteracaoInicial() != null) tfm.put("dt_alteracao_inicial", filter.getDataAlteracaoInicial());
		if (filter.getDataAlteracaoFinal() != null) tfm.put("dt_alteracao_final", filter.getDataAlteracaoFinal());
		
		return tfm;
	}
 
	@Override 
	protected List<Map<String, Object>> findDataForReport( DiasCorteFaturamentoFilterDTO filter) { 
		return convertObjectToMap(diaCorteFaturamentoService.findAll(convertFilterToTypedFlatMap(filter)));
	} 
 
	@Override 
	protected DiasCorteFaturamentoDTO findById(Long id) { 
		DiaCorteFaturamento entity = diaCorteFaturamentoService.findDiaCorteById(id);
		DiasCorteFaturamentoDTO diasCorteFaturamentoDTO = new DiasCorteFaturamentoDTO();
		diasCorteFaturamentoDTO.setId(entity.getIdDiaCorteFaturamento());
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(entity.getUsuario().getIdUsuario());
		usuarioDTO.setNmUsuario(entity.getUsuario().getNmUsuario());
		diasCorteFaturamentoDTO.setUsuario(usuarioDTO);
		diasCorteFaturamentoDTO.setDataCorte(entity.getDtCorte());
		diasCorteFaturamentoDTO.setTpFaturarSemanal(entity.getBlSemanal());
		diasCorteFaturamentoDTO.setTpFaturarDecendial(entity.getBlDecendial());
		diasCorteFaturamentoDTO.setTpFaturarQuinzenal(entity.getBlQuinzenal());
		diasCorteFaturamentoDTO.setTpFaturarMensal(entity.getBlMensal());
		diasCorteFaturamentoDTO.setObservacao(entity.getObDiaCorteFaturamento());
		diasCorteFaturamentoDTO.setDataAlteracao(entity.getDhAlteracao());
		
		return diasCorteFaturamentoDTO; 
	} 
 
	@Override 
	protected Long store(DiasCorteFaturamentoDTO bean) { 
		DiaCorteFaturamento diaCorteFaturamento = null;
		if(bean.getId() == null){
			diaCorteFaturamento = new DiaCorteFaturamento();
		} else {
			diaCorteFaturamento = diaCorteFaturamentoService.findDiaCorteById(bean.getId());
		}
		diaCorteFaturamento.setDhAlteracao(new DateTime());
		return (Long) diaCorteFaturamentoService.store(bean.build(diaCorteFaturamento));
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	@Override 
	protected List<DiasCorteFaturamentoDTO> find(DiasCorteFaturamentoFilterDTO filter) { 
		List<DiaCorteFaturamento> diasCorteBD = diaCorteFaturamentoService.findAll(convertFilterToTypedFlatMap(filter));
		List<DiasCorteFaturamentoDTO> diasCortesDTO = new ArrayList<DiasCorteFaturamentoDTO>();
		
		for(DiaCorteFaturamento d : diasCorteBD) {
			DiasCorteFaturamentoDTO diaCorteDTO = new DiasCorteFaturamentoDTO();
			diaCorteDTO.setId(d.getIdDiaCorteFaturamento());
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setId(d.getUsuario().getIdUsuario());
			usuarioDTO.setNmUsuario(d.getUsuario().getNmUsuario());
			diaCorteDTO.setUsuario(usuarioDTO);
			
			if (d.getDtCorte() != null) { 
				diaCorteDTO.setDataCorteFormatada(JTFormatUtils.format(d.getDtCorte(), "dd/MM/yyyy"));
			}
			diaCorteDTO.setDataCorte(d.getDtCorte());
			
			diaCorteDTO.setTpFaturarSemanal(d.getBlSemanal());
			diaCorteDTO.setTpFaturarDecendial(d.getBlDecendial());
			diaCorteDTO.setTpFaturarQuinzenal(d.getBlQuinzenal());
			diaCorteDTO.setTpFaturarMensal(d.getBlMensal());
			diaCorteDTO.setObservacao(d.getObDiaCorteFaturamento());
			
			if (d.getDhAlteracao() != null) { 
				diaCorteDTO.setDataAlteracaoFormatada(JTFormatUtils.format(d.getDhAlteracao(), "dd/MM/yyyy HH:mm"));
			}
			diaCorteDTO.setDataAlteracao(d.getDhAlteracao());
			
			diasCortesDTO.add(diaCorteDTO);
		}
		
		return diasCortesDTO; 
	} 
 
	@Override 
	protected Integer count(DiasCorteFaturamentoFilterDTO filter) { 
		return 1;
	} 
	
	private List<Map<String, Object>> convertObjectToMap(List<DiaCorteFaturamento> diaCorteFaturamentos) {
		List<Map<String, Object>> excecoesMap = new ArrayList<Map<String,Object>>();
		for(DiaCorteFaturamento d : diaCorteFaturamentos){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dtCorte", d.getDtCorte());
			map.put("blSemanal", d.getBlSemanal().getDescriptionAsString());
			map.put("blDecendial", d.getBlDecendial().getDescriptionAsString());
			map.put("blQuinzenal", d.getBlQuinzenal().getDescriptionAsString());
			map.put("blMensal", d.getBlMensal().getDescriptionAsString());
			map.put("dataHoraAlteracao", d.getDhAlteracao());
			map.put("usuarioAlteracao", d.getUsuario().getNmUsuario());
			
			excecoesMap.add(map);
		}
		return excecoesMap;
	}
 
} 
