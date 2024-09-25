package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "GERACAO_XML_SORTER")
public class GeracaoXmlSorter implements Serializable {
	private static final long serialVersionUID = 1L;                                                                                                                                                                                  
	                                                                                                                                                                                   
	private Long idGeracaoXmlSorter;
	private Integer nrHorasInicio;
	private Integer nrHorasFim;
	private Cliente clienteRemetente;
	private Filial filialOrigem;
	private Filial filialDestino;

	public GeracaoXmlSorter() {
	}

	@Id
	@SequenceGenerator(name = "GERACAO_XML_SORTER_SEQ", sequenceName = "GERACAO_XML_SORTER_SQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERACAO_XML_SORTER_SEQ")
	@Column(name = "ID_GERACAO_XML_SORTER", nullable = false)
	public Long getIdGeracaoXmlSorter() {
		return idGeracaoXmlSorter;
	}

	public void setIdGeracaoXmlSorter(Long idGeracaoXmlSorter) {
		this.idGeracaoXmlSorter = idGeracaoXmlSorter;
	}

	@Column(name="NR_HORAS_INICIO", length=3, nullable = false)
	public Integer getNrHorasInicio() {
		return nrHorasInicio;
	}

	public void setNrHorasInicio(Integer nrHorasInicio) {
		this.nrHorasInicio = nrHorasInicio;
	}

	@Column(name="NR_HORAS_FIM", length=3, nullable = false)
	public Integer getNrHorasFim() {
		return nrHorasFim;
	}

	public void setNrHorasFim(Integer nrHorasFim) {
		this.nrHorasFim = nrHorasFim;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_REMETENTE", nullable = false)
	public Cliente getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(Cliente clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_ORIGEM", nullable = false)
	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_DESTINO", nullable = false)
	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

}
