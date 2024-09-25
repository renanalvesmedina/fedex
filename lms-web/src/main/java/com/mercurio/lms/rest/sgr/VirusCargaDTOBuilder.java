package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.rest.sgr.dto.VirusCargaDTO;
import com.mercurio.lms.sgr.model.VirusCarga;

public final class VirusCargaDTOBuilder extends BaseDTOBuilder {

	public static List<VirusCargaDTO> convertVirusCargaDTO(List<VirusCarga> list) {
		List<VirusCargaDTO> dtos = new ArrayList<VirusCargaDTO>();
		for (VirusCarga bean : list) {
			dtos.add(buildVirusCargaDTO(bean));
		}
		return dtos;
	}

	public static VirusCargaDTO buildVirusCargaDTO(VirusCarga bean) {
		VirusCargaDTO dto = new VirusCargaDTO();
		dto.setId(bean.getIdVirusCarga());
		dto.setDhAtivacao(bean.getDhAtivacao());
		dto.setDhInclusao(bean.getDhInclusao());
		dto.setCliente(buildClienteSuggestDTO(bean.getCliente()));
		dto.setNrChave(bean.getNrChave());
		dto.setDsSerie(bean.getDsSerie());
		dto.setNrIscaCarga(bean.getNrIscaCarga());
		dto.setNrNotaFiscal(bean.getNrNotaFiscal());
		dto.setNrVolume(bean.getNrVolume());
		return dto;
	}

	private VirusCargaDTOBuilder() {
		throw new AssertionError();
	}

}
