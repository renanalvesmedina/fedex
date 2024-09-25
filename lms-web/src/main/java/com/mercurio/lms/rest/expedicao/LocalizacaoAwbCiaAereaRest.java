package com.mercurio.lms.rest.expedicao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.expedicao.model.service.LocalizacaoAwbCiaAereaService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.rest.expedicao.dto.LocalizacaoAwbCiaAereaDTO;
import com.mercurio.lms.rest.expedicao.dto.LocalizacaoAwbCiaAereaFilterDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;

@Path("/expedicao/localizacaoAwbCiaAerea")
public class LocalizacaoAwbCiaAereaRest extends BaseCrudRest<LocalizacaoAwbCiaAereaDTO, LocalizacaoAwbCiaAereaDTO, LocalizacaoAwbCiaAereaFilterDTO> {
	
	@InjectInJersey LocalizacaoAwbCiaAereaService localizacaoAwbCiaAereaService;
	@InjectInJersey ParametroGeralService parametroGeralService;
	@InjectInJersey VolumeNotaFiscalService volumeNotaFiscalService;
	@InjectInJersey ReportExecutionManager reportExecutionManager;

	@Override
	protected Integer count(LocalizacaoAwbCiaAereaFilterDTO filter) {
		return localizacaoAwbCiaAereaService.getRowCountLocalizacaoAwbCiaAerea(getTypedFlatMapWithPaginationInfo(filter));
	}

	@Override
	protected List<LocalizacaoAwbCiaAereaDTO> find(LocalizacaoAwbCiaAereaFilterDTO filter) {
		PaginatedQuery paginatedQuery = new PaginatedQuery(getCriteria(filter));
		return convertToLocalizacaoAwbCiaAereaDTO(localizacaoAwbCiaAereaService.findPaginated(paginatedQuery));
	}
	
	private TypedFlatMap getCriteria(LocalizacaoAwbCiaAereaFilterDTO filter) {
		TypedFlatMap typedFlatMap = getTypedFlatMapWithPaginationInfo(filter);
		
		if (filter.getEmpresa() != null) {
			if (filter.getEmpresa().getIdEmpresa() != null) {
				typedFlatMap.put("empresa.idEmpresa", filter.getEmpresa().getIdEmpresa());
			} 
		}
		
		if (StringUtils.isNotBlank(filter.getDsTracking())) {
			typedFlatMap.put("dsTracking", filter.getDsTracking());
		}
		
		if (filter.getTpLocalizacaoAtual() != null && StringUtils.isNotBlank(filter.getTpLocalizacaoAtual().getValue())) {
			typedFlatMap.put("tpLocalizacaoAtual.value", filter.getTpLocalizacaoAtual().getValue());
		}
		
		if (filter.getTpLocalizacaoCiaAerea() != null && StringUtils.isNotBlank(filter.getTpLocalizacaoCiaAerea().getValue())) {
			typedFlatMap.put("tpLocalizacaoCiaAerea.value", filter.getTpLocalizacaoCiaAerea().getValue());
		}
		
		return typedFlatMap;
	}
	

	@Override
	protected Long store(LocalizacaoAwbCiaAereaDTO localizacaoAwbCiaAereaDTO) {
		return localizacaoAwbCiaAereaService.store(convertLocalizacaoAwbCiaAerea(localizacaoAwbCiaAereaDTO));
	}
	
	
	private LocalizacaoAwbCiaAerea convertLocalizacaoAwbCiaAerea(LocalizacaoAwbCiaAereaDTO localizacaoAwbCiaAereaDTO) {
		LocalizacaoAwbCiaAerea lac = new LocalizacaoAwbCiaAerea();		
		
		if(localizacaoAwbCiaAereaDTO.getId() != null) {
			lac = localizacaoAwbCiaAereaService.findById(localizacaoAwbCiaAereaDTO.getId());
		}
		
		Empresa ciaAerea = new Empresa();
		ciaAerea.setIdEmpresa(localizacaoAwbCiaAereaDTO.getCiaAerea().getIdEmpresa());
		lac.setCiaAerea(ciaAerea);
		
		lac.setDsTracking(localizacaoAwbCiaAereaDTO.getDsTracking());
		lac.setTpLocalizacaoCiaAerea(localizacaoAwbCiaAereaDTO.getTpLocalizacaoCiaAerea());
		lac.setTpLocalizacaoAtual(localizacaoAwbCiaAereaDTO.getTpLocalizacaoAtual());
		
		return lac;
	}
	
	
	private List<LocalizacaoAwbCiaAereaDTO> convertToLocalizacaoAwbCiaAereaDTO (List<Map<String, Object>> list) {
		List<LocalizacaoAwbCiaAereaDTO> listLocalizacaoAwbCiaAereaDTO = new ArrayList<LocalizacaoAwbCiaAereaDTO>();
		for (Map<String, Object> item : list) {
			LocalizacaoAwbCiaAereaDTO localizacaoAwbCiaAereaDTO = new LocalizacaoAwbCiaAereaDTO();
			localizacaoAwbCiaAereaDTO.setId((Long) item.get("idLocalizacaoAwbCiaAerea"));
			localizacaoAwbCiaAereaDTO.setDsTracking((String) item.get("dsTracking"));
			localizacaoAwbCiaAereaDTO.setTpLocalizacaoAtual(new DomainValue((String) item.get("tpLocalizacaoAtual")));
			localizacaoAwbCiaAereaDTO.setTpLocalizacaoCiaAerea(new DomainValue((String) item.get("tpLocalizacaoCiaAerea")));
			
			EmpresaDTO ciaAerea = new EmpresaDTO();
			ciaAerea.setIdEmpresa((Long) item.get("idEmpresa"));
			ciaAerea.setNmPessoa((String) item.get("nmCiaAerea"));
			localizacaoAwbCiaAereaDTO.setCiaAerea(ciaAerea);
			
			listLocalizacaoAwbCiaAereaDTO.add(localizacaoAwbCiaAereaDTO);
		}
		return listLocalizacaoAwbCiaAereaDTO;
	}

	@Override
	protected void removeById(Long idLocalizacaoAwbCiaAerea) {
		localizacaoAwbCiaAereaService.removeById(idLocalizacaoAwbCiaAerea);		
	}

	@Override
	protected void removeByIds(List<Long> idLocalizacaoAwbCiaAereaList) {
		localizacaoAwbCiaAereaService.removeByIds(idLocalizacaoAwbCiaAereaList);		
	}

	@Override
	protected LocalizacaoAwbCiaAereaDTO findById(Long id) {
		LocalizacaoAwbCiaAerea lac = localizacaoAwbCiaAereaService.findById(id);
		
		LocalizacaoAwbCiaAereaDTO localizacaoAwbCiaAereaDTO = new LocalizacaoAwbCiaAereaDTO();
		
		localizacaoAwbCiaAereaDTO.setId(lac.getIdLocalizacaoAwbCiaAerea());
		
		EmpresaDTO ciaAerea = new EmpresaDTO();
		ciaAerea.setIdEmpresa(lac.getCiaAerea().getIdEmpresa());
		ciaAerea.setNmPessoa(lac.getCiaAerea().getPessoa().getNmPessoa());
		ciaAerea.setNrIdentificacao(lac.getCiaAerea().getPessoa().getNrIdentificacao());
		localizacaoAwbCiaAereaDTO.setCiaAerea(ciaAerea);
		
		localizacaoAwbCiaAereaDTO.setDsTracking(lac.getDsTracking());
		localizacaoAwbCiaAereaDTO.setTpLocalizacaoAtual(lac.getTpLocalizacaoAtual());
		localizacaoAwbCiaAereaDTO.setTpLocalizacaoCiaAerea(lac.getTpLocalizacaoCiaAerea());
		
		return localizacaoAwbCiaAereaDTO;
	}

	
}
