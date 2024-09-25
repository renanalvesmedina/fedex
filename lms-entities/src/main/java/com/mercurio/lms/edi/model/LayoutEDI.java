package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;

public class LayoutEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long  idLayoutEdi;
	
	private String nmLayoutEdi;
	
	private String dsLayoutEdi;
	
	private DomainValue tpLayoutEdi;
	
	private Integer qtLayoutEdi;
	
	private TipoLayoutDocumento tipoLayoutDocumento;
	
	private TipoArquivoEDI tipoArquivoEDI;

	private List clienteEdiLayout;

	public Long getIdLayoutEdi() {
		return idLayoutEdi;
	}

	public void setIdLayoutEdi(Long idLayoutEdi) {
		this.idLayoutEdi = idLayoutEdi;
	}

	public String getNmLayoutEdi() {
		return nmLayoutEdi;
	}

	public void setNmLayoutEdi(String nmLayoutEdi) {
		this.nmLayoutEdi = nmLayoutEdi;
	}

	public String getDsLayoutEdi() {
		return dsLayoutEdi;
	}

	public void setDsLayoutEdi(String dsLayoutEdi) {
		this.dsLayoutEdi = dsLayoutEdi;
	}

	public DomainValue getTpLayoutEdi() {
		return tpLayoutEdi;
	}

	public void setTpLayoutEdi(DomainValue tpLayoutEdi) {
		this.tpLayoutEdi = tpLayoutEdi;
	}

	public Integer getQtLayoutEdi() {
		return qtLayoutEdi;
	}

	public void setQtLayoutEdi(Integer qtLayoutEdi) {
		this.qtLayoutEdi = qtLayoutEdi;
	}

	public TipoLayoutDocumento getTipoLayoutDocumento() {
		return tipoLayoutDocumento;
	}

	public void setTipoLayoutDocumento(TipoLayoutDocumento tipoLayoutDocumento) {
		this.tipoLayoutDocumento = tipoLayoutDocumento;
	}

	public TipoArquivoEDI getTipoArquivoEDI() {
		return tipoArquivoEDI;
	}

	public void setTipoArquivoEDI(TipoArquivoEDI tipoArquivoEDI) {
		this.tipoArquivoEDI = tipoArquivoEDI;
	}
	
	public void setClienteEdiLayout(List clienteEdiLayout) {
		this.clienteEdiLayout = clienteEdiLayout;
	}

	public List getClienteEdiLayout() {
		return clienteEdiLayout;
	}	
	
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		
		/*Id Layout*/
		bean.put("idLayoutEdi", this.getIdLayoutEdi());
		/*Nome do layout*/
		bean.put("nmLayoutEdi", this.getNmLayoutEdi());
		/*Descrição do layout*/
		bean.put("dsLayoutEdi", this.getDsLayoutEdi());
		/*Tipo de layout*/
		bean.put("tpLayoutEdi", this.getTpLayoutEdi().getValue());
		bean.put("nmTpLayoutEdi", this.getTpLayoutEdi().getDescription()
				.getValue());
		/*Quantidade de layout*/		
		bean.put("qtLayoutEdi", this.getQtLayoutEdi());		
		/*Tipo de layout do documento*/
		bean.put("idTipoLayoutDocumento", this.getTipoLayoutDocumento()
				.getIdTipoLayoutDocumento());
		bean.put("dsTipoLayoutDocumento", this.getTipoLayoutDocumento()
				.getDsTipoLayoutDocumento());
		/*Tipo extensao do arquivo*/
		bean.put("idTipoArquivoEDI", this.getTipoArquivoEDI()
				.getIdTipoArquivoEDI());
		
		return bean;
	}	
	
}
