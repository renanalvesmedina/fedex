package com.mercurio.lms.rest.tabeladeprecos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

public class MarkupMinimoProgressivoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long idTarifa;
	private String nomeTarifa;
	private Long idRota;
	private String nomeRota;
	private char tipoParcela = 'M'; //'M'=faixa progressiva,'P'=preço frete,'G'=generalidade,'T'=taxas ou 'S'=serviço adicional
	private YearMonthDay dataVigenciaInicial;
	private YearMonthDay dataVigenciaFinal;
	private List<ParcelaMinimoProgressivoDTO> listParcelas;
	private boolean selected;
	
	public MarkupMinimoProgressivoDTO() {
	}
	
	public MarkupMinimoProgressivoDTO(Long idTarifa, String nomeTarifa, Long idRota, String nomeRota, YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, List<ParcelaMinimoProgressivoDTO> listParcelas) {
		this.idTarifa = idTarifa;
		this.nomeTarifa = nomeTarifa;
		this.idRota = idRota;
		this.nomeRota = nomeRota;
		this.dataVigenciaInicial = dataVigenciaInicial;
		this.dataVigenciaFinal = dataVigenciaFinal;
		this.listParcelas = listParcelas;
	}

	public Long getIdTarifa() {
		return idTarifa;
	}

	public void setIdTarifa(Long idTarifa) {
		this.idTarifa = idTarifa;
	}

	public String getNomeTarifa() {
		return nomeTarifa;
	}

	public void setNomeTarifa(String nomeTarifa) {
		this.nomeTarifa = nomeTarifa;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	public String getNomeRota() {
		return nomeRota;
	}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}

	public char getTipoParcela() {
		return tipoParcela;
	}

	public void setTipoParcela(char tipoParcela) {
		this.tipoParcela = tipoParcela;
	}

	public List<ParcelaMinimoProgressivoDTO> getListParcelas() {
		return listParcelas;
	}

	public void setListParcelas(List<ParcelaMinimoProgressivoDTO> listParcelas) {
		this.listParcelas = listParcelas;
	}

	public void incluiParcela(ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO) {
		if (parcelaMinimoProgressivoDTO == null) {
			return;
		}
		if (this.listParcelas == null) {
			this.listParcelas = new ArrayList<ParcelaMinimoProgressivoDTO>();
		}
		this.listParcelas.add(parcelaMinimoProgressivoDTO);
	}
	
	public void incluiParcelas(List<ParcelaMinimoProgressivoDTO> listParcelaMinimoProgressivoDTO) {
		if(CollectionUtils.isNotEmpty(listParcelaMinimoProgressivoDTO)){
			for (ParcelaMinimoProgressivoDTO parcelaMinimoProgressivoDTO : listParcelaMinimoProgressivoDTO) {
				this.incluiParcela(parcelaMinimoProgressivoDTO);
			}
		}
	}

	public YearMonthDay getDataVigenciaInicial() {
		return dataVigenciaInicial;
	}

	public void setDataVigenciaInicial(YearMonthDay dataVigenciaInicial) {
		this.dataVigenciaInicial = dataVigenciaInicial;
	}

	public YearMonthDay getDataVigenciaFinal() {
		return dataVigenciaFinal;
	}

	public void setDataVigenciaFinal(YearMonthDay dataVigenciaFinal) {
		this.dataVigenciaFinal = dataVigenciaFinal;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	
}
