package com.mercurio.lms.rest.contasareceber.excecaonegativacao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.ExcecaoNegativacaoSerasa;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.service.EnviarCobrancaFaturasEmailService;
import com.mercurio.lms.contasreceber.model.service.ExcecaoNegativacaoSerasaService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto.ExcecaoNegativacaoDTO;
import com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto.ExcecaoNegativacaoFilterDTO;
import com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto.FaturaDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
 
@Path("/contasareceber/excecaoNegativacao") 
public class ExcecaoNegativacaoRest extends LmsBaseCrudReportRest<ExcecaoNegativacaoDTO, ExcecaoNegativacaoDTO, ExcecaoNegativacaoFilterDTO> { 
 
//	INICIO Botão de Enviar Faturas (TESTE)
	@InjectInJersey EnviarCobrancaFaturasEmailService enviarCobrancaFaturasEmailService;
	
	@GET
	@Path("enviarFaturas")
	public void enviarFaturas() {
		enviarCobrancaFaturasEmailService.executeEnviarFaturas();
	}
//	FIM Botão de Enviar Faturas (TESTE)
	
	@InjectInJersey private ExcecaoNegativacaoSerasaService excecaoNegativacaoSerasaService;
	@InjectInJersey private FaturaService faturaService;
	@InjectInJersey private FilialService filialService;
	
	@POST
	@Path("importar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public List<String> importar(FormDataMultiPart formDataMultiPart) throws IOException{
		String arquivo = getCharacterLobUserTypeFromForm(formDataMultiPart, "arquivo");
		try {
			return excecaoNegativacaoSerasaService.executeImportar(arquivo);
		} 
		catch (BusinessException e) {
				throw e;
		}
		catch (Exception e) {
			throw new BusinessException("LMS-36193",e);
		}
	}
	
	@POST
	@Path("findFilialSuggest")
	@SuppressWarnings("unchecked")
	public Response findFaturaSuggest(Map<String, Object> data) {
		String nrFatura = MapUtils.getString(data, "value");
		Map<String, Object> map = MapUtils.getMap(data, "filial");
		String idFilial = MapUtils.getString(map, "idFilial");
		
		List<Map<String, Object>> registros = faturaService.findByNrFaturaIdFilialOrigem(Long.parseLong(nrFatura), Long.parseLong(idFilial));
		List<FaturaDTO> faturas = this.converteListaFaturas(registros);

		return Response.ok(faturas).build();
	}
	
	private List<FaturaDTO> converteListaFaturas(List<Map<String, Object>> registros) {
		List<FaturaDTO> faturas = new ArrayList<FaturaDTO>();
		for (Map<String, Object> registro : registros) {
			String nrFatura = (String) String.valueOf(registro.get("nrFatura"));
			faturas.add(new FaturaDTO(
					(Long) registro.get("idFatura"), 
					nrFatura + ""));
		}
		return faturas;
	}
	
	@Override 
	protected List<Map<String, String>> getColumns() { 
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("fatura", "fatura"));
		list.add(getColumn("dtVigenciaInicial", "dtVigenciaInicial"));
		list.add(getColumn("dtVigenciaFinal", "dtVigenciaFinal"));
		list.add(getColumn("observacoes", "observacoes"));
		list.add(getColumn("dataHoraAlteracao", "dataHoraAlteracao"));
		list.add(getColumn("usuarioAlteracao", "usuarioAlteracao"));
		return list;
	} 
	
	private TypedFlatMap convertFilterToTypedFlatMap(ExcecaoNegativacaoFilterDTO filter) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getId() != null) tfm.put("id_excecao_negativacao", filter.getId());
		if (SessionUtils.getUsuarioLogado().getIdUsuario() != null) tfm.put("id_usuario", SessionUtils.getUsuarioLogado().getIdUsuario());
		if (filter.getDtVigente() != null) tfm.put("dt_vigencia", filter.getDtVigente());
		if (filter.getFilial() != null) tfm.put("id_filial", filter.getFilial().getId());
		if (filter.getFatura() != null) tfm.put("id_fatura", filter.getFatura().getId());
		
		return tfm;
	}
 
	@Override 
	protected List<Map<String, Object>> findDataForReport( ExcecaoNegativacaoFilterDTO filter) { 
		return convertObjectToMap(excecaoNegativacaoSerasaService.findAll(convertFilterToTypedFlatMap(filter)));
	} 
 
	@Override 
	protected ExcecaoNegativacaoDTO findById(Long id) { 
		ExcecaoNegativacaoSerasa entity = excecaoNegativacaoSerasaService.findExcecaoById(id);
		ExcecaoNegativacaoDTO excecaoNegativacaoDTO = new ExcecaoNegativacaoDTO();
		excecaoNegativacaoDTO.setId(entity.getIdExcecaoNegativacaoSerasa());
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(entity.getUsuario().getIdUsuario());
		usuarioDTO.setNmUsuario(entity.getUsuario().getNmUsuario());
		excecaoNegativacaoDTO.setUsuario(usuarioDTO);
		
		Filial filial = filialService.findById(entity.getFatura().getFilialByIdFilial().getIdFilial());
		FilialSuggestDTO filialDTO = new FilialSuggestDTO();
		filialDTO.setId(filial.getIdFilial());
		filialDTO.setIdFilial(filial.getIdFilial());
		filialDTO.setSgFilial(filial.getSgFilial());
		excecaoNegativacaoDTO.setFilial(filialDTO);
		
		FaturaDTO faturaDTO = new FaturaDTO();
		faturaDTO.setId(entity.getFatura().getIdFatura());
		faturaDTO.setNrFatura(String.valueOf(entity.getFatura().getNrFatura()));
		excecaoNegativacaoDTO.setFatura(faturaDTO);
		
		excecaoNegativacaoDTO.setDtVigenciaInicial(entity.getDtVigenciaInicial());
		excecaoNegativacaoDTO.setDtVigenciaFinal(entity.getDtVigenciaFinal());
		excecaoNegativacaoDTO.setObExcecaoNegativacaoSerasa(entity.getObExcecaoNegativacaoSerasa());
		excecaoNegativacaoDTO.setDataAlteracao(entity.getDhAlteracao());
		
		return excecaoNegativacaoDTO; 
	} 
 
	@Override 
	protected Long store(ExcecaoNegativacaoDTO bean) { 
		ExcecaoNegativacaoSerasa excecaoNegativacaoSerasa = null;
		if(bean.getId() == null){
			excecaoNegativacaoSerasa = new ExcecaoNegativacaoSerasa();
		} else {
			excecaoNegativacaoSerasa = excecaoNegativacaoSerasaService.findExcecaoById(bean.getId());
		}
		excecaoNegativacaoSerasa.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		
		Fatura fatura = faturaService.findById(bean.getFatura().getId());
		excecaoNegativacaoSerasa.setFatura(fatura);
		
		return (Long) excecaoNegativacaoSerasaService.store(bean.build(excecaoNegativacaoSerasa));
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	@Override 
	protected List<ExcecaoNegativacaoDTO> find(ExcecaoNegativacaoFilterDTO filter) { 
		List<ExcecaoNegativacaoSerasa> excecaoNegativacaoBD = excecaoNegativacaoSerasaService.findAll(convertFilterToTypedFlatMap(filter));
		List<ExcecaoNegativacaoDTO> excecoesNegativacaoDTO = new ArrayList<ExcecaoNegativacaoDTO>();
		
		for(ExcecaoNegativacaoSerasa e : excecaoNegativacaoBD) {
			ExcecaoNegativacaoDTO excecaoNegativacaoDTO = new ExcecaoNegativacaoDTO();
			excecaoNegativacaoDTO.setId(e.getIdExcecaoNegativacaoSerasa());
			
			excecaoNegativacaoDTO.setDataAlteracao(e.getDhAlteracao());
			if (e.getDhAlteracao() != null) { 
				excecaoNegativacaoDTO.setDataAlteracaoFormatada(JTFormatUtils.format(e.getDhAlteracao(), "dd/MM/yyyy HH:mm"));
			}
			
			excecaoNegativacaoDTO.setDtVigenciaInicial(e.getDtVigenciaInicial());
			if (e.getDtVigenciaInicial() != null) { 
				excecaoNegativacaoDTO.setDtVigenciaInicialFormatada(JTFormatUtils.format(e.getDtVigenciaInicial(), "dd/MM/yyyy"));
			}
			
			excecaoNegativacaoDTO.setDtVigenciaFinal(e.getDtVigenciaFinal());
			if (e.getDtVigenciaFinal() != null) { 
				excecaoNegativacaoDTO.setDtVigenciaFinalFormatada(JTFormatUtils.format(e.getDtVigenciaFinal(), "dd/MM/yyyy"));
			}
			
			FaturaDTO faturaDTO = new FaturaDTO();
			Fatura f = faturaService.findById(e.getFatura().getIdFatura());
			faturaDTO.setId(f.getIdFatura());
			faturaDTO.setNrFatura(String.valueOf(f.getNrFatura()));
			excecaoNegativacaoDTO.setFatura(faturaDTO);
			
			Filial filial = filialService.findById(f.getFilialByIdFilial().getIdFilial());
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setId(filial.getIdFilial());
			filialDTO.setIdFilial(filial.getIdFilial());
			filialDTO.setSgFilial(filial.getSgFilial());
			excecaoNegativacaoDTO.setFilial(filialDTO);
			
			
			excecaoNegativacaoDTO.setObExcecaoNegativacaoSerasa(e.getObExcecaoNegativacaoSerasa());
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setId(e.getUsuario().getIdUsuario());
			usuarioDTO.setNmUsuario(e.getUsuario().getNmUsuario());
			excecaoNegativacaoDTO.setUsuario(usuarioDTO);
			
			excecoesNegativacaoDTO.add(excecaoNegativacaoDTO);
		}
		
		return excecoesNegativacaoDTO; 
	} 
 
	@Override 
	protected Integer count(ExcecaoNegativacaoFilterDTO filter) { 
		return 1;
	} 
	
	private List<Map<String, Object>> convertObjectToMap(List<ExcecaoNegativacaoSerasa> excecoesNegativacaoSerasa) {
		List<Map<String, Object>> excecoesMap = new ArrayList<Map<String,Object>>();
		for(ExcecaoNegativacaoSerasa e : excecoesNegativacaoSerasa){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fatura", e.getFatura().getNrFatura());
			map.put("dtVigenciaInicial", e.getDtVigenciaInicial());
			map.put("dtVigenciaFinal", e.getDtVigenciaFinal());
			map.put("observacoes", e.getObExcecaoNegativacaoSerasa());
			map.put("usuarioAlteracao", e.getUsuario().getNmUsuario());
			map.put("dataHoraAlteracao", e.getDhAlteracao());
			
			excecoesMap.add(map);
		}
		return excecoesMap;
	}
 
} 
