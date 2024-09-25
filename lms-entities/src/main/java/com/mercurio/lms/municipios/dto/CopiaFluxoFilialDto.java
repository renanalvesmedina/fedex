package com.mercurio.lms.municipios.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;

public class CopiaFluxoFilialDto {

	private FluxoFilial fluxoFilialOrigem;
	private FluxoFilial fluxoFilialClone;
	private List<Filial> filiaisRemovidas;
	
	public FluxoFilial getFluxoFilialOrigem() {
		return fluxoFilialOrigem;
	}
	public void setFluxoFilialOrigem(FluxoFilial fluxoFilialOrigem) {
		this.fluxoFilialOrigem = fluxoFilialOrigem;
	}
	public FluxoFilial getFluxoFilialClone() {
		return fluxoFilialClone;
	}
	public void setFluxoFilialClone(FluxoFilial fluxoFilialClone) {
		this.fluxoFilialClone = fluxoFilialClone;
	}
	public void setFiliaisRemovidas(List<Filial> filiaisRemovidas) {
		this.filiaisRemovidas = filiaisRemovidas;
	}
	public List<Filial> getFiliaisRemovidas() {
		return filiaisRemovidas;
	}
	public boolean hasFiliaisRemovidas() {
		return CollectionUtils.isNotEmpty(filiaisRemovidas);
	}
	public List<Filial> getFiliaisRestantes(List<OrdemFilialFluxo> ordemFilialFluxos) {
		List<Filial> result = new ArrayList<Filial>();
		for (OrdemFilialFluxo ordemFilialFluxo : ordemFilialFluxos) {
			if (!filiaisRemovidas.contains(ordemFilialFluxo.getFilial())) {
				result.add(ordemFilialFluxo.getFilial());
			}
		}
		return result;
	}
	public Filial getFiliailReembarque(FluxoFilial fluxoFilial) {
		List<Filial> filiaisRestantes = getFiliaisRestantes(fluxoFilial.getOrdemFilialFluxos());
		for (Filial filial : filiaisRestantes) {
			if(!filial.equals(fluxoFilial.getFilialByIdFilialOrigem()) 
					&& !filial.equals(fluxoFilial.getFilialByIdFilialDestino())) {
				return filial;
			}
		}
		return null;
	}
	
}
