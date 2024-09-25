package com.mercurio.lms.rest.contasareceber.motivoinadimplencia;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.MotivoInadimplencia;
import com.mercurio.lms.contasreceber.model.service.MotivoInadimplenciaService;
import com.mercurio.lms.rest.contasareceber.motivoinadimplencia.dto.MotivoInadimplenciaDTO;
import com.mercurio.lms.rest.contasareceber.motivoinadimplencia.dto.MotivoInadimplenciaFilterDTO;
 
@Path("/contasareceber/motivoInadimplencia") 
public class MotivoInadimplenciaRest extends BaseCrudRest<MotivoInadimplenciaDTO, MotivoInadimplenciaDTO, MotivoInadimplenciaFilterDTO> { 
 
	@InjectInJersey MotivoInadimplenciaService motivoInadimplenciaService;
 
	@Override 
	protected MotivoInadimplenciaDTO findById(Long id) { 
		MotivoInadimplencia entity = motivoInadimplenciaService.findMotivoInadimplenciaById(id);
		MotivoInadimplenciaDTO motivoInadimplenciaDTO = new MotivoInadimplenciaDTO();
		motivoInadimplenciaDTO.setId(entity.getIdMotivoInadimplencia());
		motivoInadimplenciaDTO.setDescricao(entity.getDescricao());
		motivoInadimplenciaDTO.setTpSituacao(entity.getTpSituacao());
		return motivoInadimplenciaDTO; 
	} 
 
	@Override 
	protected Long store(MotivoInadimplenciaDTO bean) { 
		MotivoInadimplencia motivoInadimplencia = null;
		if(bean.getId() == null){
			motivoInadimplencia = new MotivoInadimplencia();
		} else {
			motivoInadimplencia = motivoInadimplenciaService.findMotivoInadimplenciaById(bean.getId());
		}
		
		return (Long) motivoInadimplenciaService.store(bean.build(motivoInadimplencia)); 
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	@Override 
	protected List<MotivoInadimplenciaDTO> find(MotivoInadimplenciaFilterDTO filter) { 
		List<MotivoInadimplencia> motivoInadimplenciaBD = motivoInadimplenciaService.findAll(convertFilterToTypedFlatMap(filter));
		List<MotivoInadimplenciaDTO> motivosInadimplenciaDTO = new ArrayList<MotivoInadimplenciaDTO>();
		
		for(MotivoInadimplencia m : motivoInadimplenciaBD) {
			MotivoInadimplenciaDTO motivoInadimplenciaDTO = new MotivoInadimplenciaDTO();
			motivoInadimplenciaDTO.setId(m.getIdMotivoInadimplencia());
			motivoInadimplenciaDTO.setDescricao(m.getDescricao());
			motivoInadimplenciaDTO.setTpSituacao(m.getTpSituacao());
			
			motivosInadimplenciaDTO.add(motivoInadimplenciaDTO);
		} 
		
		return motivosInadimplenciaDTO; 
	} 
 
	private TypedFlatMap convertFilterToTypedFlatMap(MotivoInadimplenciaFilterDTO filter) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getId() != null) tfm.put("id_motivo_inadimplencia", filter.getId());
		if (filter.getTpSituacao() != null) tfm.put("tp_situacao", filter.getTpSituacao().getValue());
		
		return tfm;
	}
		
	@Override 
	protected Integer count(MotivoInadimplenciaFilterDTO filter) { 
		return 1; 
	} 
 
} 
