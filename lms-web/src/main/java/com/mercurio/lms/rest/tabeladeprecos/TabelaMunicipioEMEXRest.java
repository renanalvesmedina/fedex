package com.mercurio.lms.rest.tabeladeprecos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.tabeladeprecos.dto.TabelaMunicipioEMEXDTO;
import com.mercurio.lms.rest.tabeladeprecos.dto.TabelaMunicipioEMEXFilterDTO;
import com.mercurio.lms.rest.tabeladeprecos.dto.TabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.TabelaMunicipioEMEX;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaMunicipioEMEXService;


@Path("/tabeladeprecos/tabelaMunicipioEMEX")
public class TabelaMunicipioEMEXRest extends BaseCrudRest<TabelaMunicipioEMEXDTO, TabelaMunicipioEMEXDTO, TabelaMunicipioEMEXFilterDTO>{

	@InjectInJersey
	TabelaMunicipioEMEXService tabelaMunicipioEMEXService; 
	
	@InjectInJersey
	MunicipioService municipioService; 
	
	@Override
	protected void removeById(Long id) {
		tabelaMunicipioEMEXService.removeById(id);
	}
	
	@Override
	protected void removeByIds(List<Long> ids) {
		tabelaMunicipioEMEXService.removeByIds(ids);
	}
	
	@Override
	protected Integer count(TabelaMunicipioEMEXFilterDTO filterDto) {
		return 0;
	}
	
	@Override
	protected List<TabelaMunicipioEMEXDTO> find(TabelaMunicipioEMEXFilterDTO filterDto) {
		Long idTabelaPreco = filterDto.getTabelaPreco() != null ? filterDto.getTabelaPreco().getIdTabelaPreco() : null;
		Long idMunicipio = filterDto.getMunicipio() != null ? filterDto.getMunicipio().getIdMunicipio() : null;
		
		List<TabelaMunicipioEMEX> list = tabelaMunicipioEMEXService.findByIdTabelaPrecoIdMunicipio(idTabelaPreco, idMunicipio);
		List<TabelaMunicipioEMEXDTO> dtoList = new ArrayList<TabelaMunicipioEMEXDTO>();
		for (TabelaMunicipioEMEX tabelaMunicipioEMEX : list){
			if (filterDto.getUnidadeFederativa() != null){
				if (filterDto.getUnidadeFederativa() != null && tabelaMunicipioEMEX.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa().equals(filterDto.getUnidadeFederativa().getIdUnidadeFederativa())){
					dtoList.add(convertToDTO(tabelaMunicipioEMEX));
				}
			} else {
				dtoList.add(convertToDTO(tabelaMunicipioEMEX));
			}
		}
		return dtoList;
	}
	
	
	private Map<String, Object> extractCriteriaFromFilter(TabelaMunicipioEMEXFilterDTO filterDto) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (filterDto.getTabelaPreco() != null){
			criteria.put("tabelaPreco.idTabelaPreco", filterDto.getTabelaPreco().getIdTabelaPreco());
		}
		
		if (filterDto.getMunicipio() != null){
			criteria.put("municipio.idMunicipio", filterDto.getMunicipio().getIdMunicipio());
		}
		
		return criteria;
	}

	@Override
	protected TabelaMunicipioEMEXDTO findById(Long id) {
		return convertToDTO(tabelaMunicipioEMEXService.findById(id));
	}
	
	private TabelaMunicipioEMEXDTO convertToDTO(TabelaMunicipioEMEX tabelaMunicipioEMEX){
		
		if (tabelaMunicipioEMEX ==null){
			return null;
		}
		TabelaMunicipioEMEXDTO dto = new TabelaMunicipioEMEXDTO();
		
		dto.setId(tabelaMunicipioEMEX.getIdTabelaMunicipioEMEX());
		
		TabelaPreco tabelaPreco = tabelaMunicipioEMEX.getTabelaPreco();
		if (tabelaPreco != null){
			dto.setTabelaPreco(new TabelaPrecoDTO());
			dto.getTabelaPreco().setIdTabelaPreco(tabelaPreco.getIdTabelaPreco());
			String nmTabela = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPrecoNrVersao().concat("-").concat(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
			dto.getTabelaPreco().setTabelaPreco(nmTabela);
		}
		
		Municipio municipio = tabelaMunicipioEMEX.getMunicipio();
		if (municipio != null){
			dto.setMunicipio(new MunicipioDTO());
			dto.getMunicipio().setIdMunicipio(municipio.getIdMunicipio());
			dto.getMunicipio().setNmMunicipio(municipio.getNmMunicipio());
			UnidadeFederativa uf = municipio.getUnidadeFederativa();
			dto.setUnidadeFederativa(new UnidadeFederativaDTO(uf.getIdUnidadeFederativa(), uf.getSgUnidadeFederativa(), uf.getNmUnidadeFederativa()));
		}
		
		return dto;
	}
	
	@Override
	protected Long store(TabelaMunicipioEMEXDTO dto) {
		
		List<TabelaMunicipioEMEX> listEntity = new ArrayList<TabelaMunicipioEMEX>();
		
		
		if (dto.getTodosMunicipios() != null && dto.getTodosMunicipios()){
			listEntity = convertToListEntity(dto);
		} else {
			listEntity.add(convertToEntity(dto, dto.getMunicipio().getIdMunicipio()));
		}
		
		return tabelaMunicipioEMEXService.store(listEntity);
	}
	
	private List<TabelaMunicipioEMEX> convertToListEntity(TabelaMunicipioEMEXDTO dto){
		List<TabelaMunicipioEMEX> listEntity = new ArrayList<TabelaMunicipioEMEX>();
		
		List<Municipio> listMunicipios = municipioService.findByIdUnidadeFederativaComDistrito(dto.getUnidadeFederativa().getIdUnidadeFederativa());
		
		for (Municipio municipio : listMunicipios) {
			listEntity.add(convertToEntity(dto, municipio.getIdMunicipio()));
		}
			
		return listEntity;
	}
	
	private TabelaMunicipioEMEX convertToEntity(TabelaMunicipioEMEXDTO dto, Long idMunicipio){
		TabelaMunicipioEMEX tabelaMunicipioEMEX = new TabelaMunicipioEMEX();
		tabelaMunicipioEMEX.setMunicipio(new Municipio());
		tabelaMunicipioEMEX.getMunicipio().setIdMunicipio(idMunicipio);
		
		tabelaMunicipioEMEX.setTabelaPreco(new TabelaPreco());
		tabelaMunicipioEMEX.getTabelaPreco().setIdTabelaPreco(dto.getTabelaPreco().getIdTabelaPreco());
		return tabelaMunicipioEMEX;
	}

}
