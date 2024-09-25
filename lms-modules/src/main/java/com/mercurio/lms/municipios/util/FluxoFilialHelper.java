package com.mercurio.lms.municipios.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.dto.CopiaFluxoFilialDto;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;

public class FluxoFilialHelper {

	public static String transformSiglasAsString(Set<String> sgFiliais) {
		StringBuffer strSiglas = new StringBuffer();
		for (Iterator<String> it = sgFiliais.iterator(); it.hasNext();) {
			String sgFilial = (String) it.next();
			strSiglas.append(sgFilial);
			if (it.hasNext()) {
				strSiglas.append(" - ");
			}
		}
		return StringUtils.left(strSiglas.toString(), 250);// tamanho total do campos FLUXO_FILIAL.DS_FLUXO_FILIAL
	}

	public static FluxoFilial clonarFluxo(FluxoFilial fluxoFilial, YearMonthDay dtVigenciaInicial, CopiaFluxoFilialDto copiaFluxoFilialDto) {
		FluxoFilial result = new FluxoFilial();
		result.setIdFluxoFilial(fluxoFilial.getIdFluxoFilial());
		result.setNrDistancia(fluxoFilial.getNrDistancia());
		result.setNrPrazo(fluxoFilial.getNrPrazo());
		result.setNrGrauDificuldade(fluxoFilial.getNrGrauDificuldade());
		result.setDtVigenciaFinal(fluxoFilial.getDtVigenciaFinal());
		result.setBlDomingo(fluxoFilial.getBlDomingo());
		result.setBlSegunda(fluxoFilial.getBlSegunda());
		result.setBlTerca(fluxoFilial.getBlTerca());
		result.setBlQuarta(fluxoFilial.getBlQuarta());
		result.setBlQuinta(fluxoFilial.getBlQuinta());
		result.setBlSexta(fluxoFilial.getBlSexta());
		result.setBlSabado(fluxoFilial.getBlSabado());
		result.setDsFluxoFilial(fluxoFilial.getDsFluxoFilial());
		result.setNrPrazoTotal(fluxoFilial.getNrPrazoTotal());
		result.setServico(fluxoFilial.getServico());
		result.setFilialByIdFilialDestino(fluxoFilial.getFilialByIdFilialDestino());
		result.setFilialByIdFilialOrigem(fluxoFilial.getFilialByIdFilialOrigem());
		result.setFilialByIdFilialParceira(fluxoFilial.getFilialByIdFilialParceira());
		result.setFilialByIdFilialReembarcadora(fluxoFilial.getFilialByIdFilialReembarcadora());
		result.setBlFluxoSubcontratacao(fluxoFilial.getBlFluxoSubcontratacao());
		result.setEmpresaSubcontratada(fluxoFilial.getEmpresaSubcontratada());
		result.setAtualizacaoClone(copiaFluxoFilialDto != null);
		if (copiaFluxoFilialDto != null) {
			if (copiaFluxoFilialDto != null && copiaFluxoFilialDto.hasFiliaisRemovidas()) {
				result.setFilialByIdFilialReembarcadora(copiaFluxoFilialDto.getFiliailReembarque(fluxoFilial));
			}
			if (copiaFluxoFilialDto.getFluxoFilialClone().hasFilialReembarcadora() 
					&& copiaFluxoFilialDto.getFluxoFilialOrigem().hasFilialReembarcadora() 
					&& (!copiaFluxoFilialDto.getFluxoFilialOrigem().getFilialByIdFilialReembarcadora().equals(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora())
							|| copiaFluxoFilialDto.getFluxoFilialOrigem().getFilialByIdFilialReembarcadora().equals(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora()))
					&& !fluxoFilial.getFilialByIdFilialOrigem().equals(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialOrigem())
					&& !fluxoFilial.getFilialByIdFilialOrigem().equals(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialDestino())) {
				result.setFilialByIdFilialReembarcadora(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialOrigem());
			}else if (copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora() != null 
					&& copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialOrigem().equals(fluxoFilial.getFilialByIdFilialOrigem())
					&& !copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora().equals(fluxoFilial.getFilialByIdFilialDestino())
					&& !copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora().equals(fluxoFilial.getFilialByIdFilialOrigem())){
						result.setFilialByIdFilialReembarcadora(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora());
			}else if (copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora() != null 
					&& !copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialOrigem().equals(fluxoFilial.getFilialByIdFilialOrigem())
					&& !copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora().equals(fluxoFilial.getFilialByIdFilialDestino())
					&& !copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialReembarcadora().equals(fluxoFilial.getFilialByIdFilialOrigem())) {
				result.setFilialByIdFilialReembarcadora(copiaFluxoFilialDto.getFluxoFilialClone().getFilialByIdFilialOrigem());
			}else if (CollectionUtils.isNotEmpty(fluxoFilial.getOrdemFilialFluxos()) && result.getFilialByIdFilialReembarcadora() == null) {
				for (Iterator<OrdemFilialFluxo> iterator = fluxoFilial.getOrdemFilialFluxos().iterator(); iterator.hasNext();) {
					OrdemFilialFluxo ordemFilialFluxo = (OrdemFilialFluxo) iterator.next();
					if(fluxoFilial.getFilialByIdFilialReembarcadora().equals(ordemFilialFluxo.getFilial())
							&& iterator.hasNext()) {
						OrdemFilialFluxo proximoOrdemFilialFluxo = (OrdemFilialFluxo)iterator.next();
						if (!proximoOrdemFilialFluxo.getFilial().equals(fluxoFilial.getFilialByIdFilialDestino())) {
							result.setFilialByIdFilialReembarcadora(proximoOrdemFilialFluxo.getFilial());
						}
					}
				}
			}
		}
		
		result.setFluxoReembarque(fluxoFilial.getFluxoReembarque());
		result.setFluxo(fluxoFilial.getFluxo());
		result.setBlPorto(fluxoFilial.getBlPorto());
		result.setBlClone(true);
		
		result.setIdFluxoFilial(null);
		result.setDoctoServicos(new ArrayList());
		result.setOrdemFilialFluxos(new ArrayList());
		result.setDtVigenciaInicial(dtVigenciaInicial);
		
		return result;
	}

	public static YearMonthDay getDtVigenciaFinal(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		if(dtVigenciaFinal.minusDays(1).isBefore(dtVigenciaInicial)) {
			return dtVigenciaInicial;
		} 
		return dtVigenciaFinal.minusDays(1);
	}
	
}
