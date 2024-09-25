package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;
	
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
	
@Entity
@Table(name = "RECALCULO_FRETE")
@SequenceGenerator(name = "RECALCULO_FRETE_SEQ", sequenceName = "RECALCULO_FRETE_SQ")
public class RecalculoFrete  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECALCULO_FRETE_SEQ")
	@Column(name = "ID_RECALCULO_FRETE", nullable = false)	
	private Long idRecalculoFrete;
	
	@Column(name = "NR_PROCESSO", length = 10, nullable = false)	
	private Long nrProcesso;
		
	@Column(name = "DS_PROCESSO", length = 50, nullable = false)
	private String dsProcesso;
	
	@Column(name = "DT_INICIAL", nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")	
	private YearMonthDay dtInicial;
	
	@Column(name = "DT_FINAL", nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")	
	private YearMonthDay dtFinal;
	
	@Column(name = "TP_SITUACAO_PROCESSO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_RECALCULO") })
	private DomainValue tpSituacaoProcesso;
	
	@Column(name = "DT_INICIO" , nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")	
	private YearMonthDay dtInicio;
		
	@Column(name = "DT_FIM")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")	
	private YearMonthDay dtFim;	
			
	@Column(name = "NR_REGISTROS_PROCESSAR", length = 10, nullable = true)	
	private Long nrTotalRegistros;
			
	@Column(name = "NR_REGISTROS_PROCESSADOS", length = 10, nullable = true)
	private Long nrTotalRegistrosProcessados;
			
	@Column(name = "BL_UTILIZA_TAB_ATUAL", nullable = true)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")	
	private Boolean blTabelaAtual;
			
	@Column(name = "BL_UTILIZA_TAB_ORIGINAL", nullable = true)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")		
	private Boolean blTabelaOriginal;
			
	@Column(name = "BL_UTILIZA_ALIQ_ORIGINAL", nullable = true)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")		
	private Boolean blAliquotaOriginal;	
			
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_CALCULO")	
	private TabelaPreco tabelaPrecoCalculo;
			
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_FILTRO")	
	private TabelaPreco tabelaPrecoFiltro;
			
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_TABELA_PRECO")	
	private TipoTabelaPreco tipoTabelaPreco;
			
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_SUBTIPO_TABELA_PRECO")	
	private SubtipoTabelaPreco subtipoTabelaPreco;
			
	@OneToMany(mappedBy="recalculoFrete",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<ClienteProcessar> clientesProcessar;
			
	@OneToMany(mappedBy="recalculoFrete",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<ClienteNaoProcessar> clientesNaoProcessar;
			
	public Map<String,Object> getMap(){
			
		Map<String,Object> bean = new HashMap<String, Object>();
			
		bean.put("idRecalculoFrete", idRecalculoFrete);
		bean.put("nrProcesso", nrProcesso);
		bean.put("dsProcesso", dsProcesso);
		bean.put("dtInicio", dtInicio);		
		bean.put("dtInicial",dtInicial);
		bean.put("dtFinal",dtFinal);
		bean.put("tpSituacaoProcesso",tpSituacaoProcesso);
		bean.put("dtInicio",dtInicio);
		bean.put("dtFim",dtFim);
		bean.put("nrTotalRegistros",nrTotalRegistros);
		bean.put("nrTotalRegistrosProcessados",nrTotalRegistrosProcessados);
		bean.put("blTabelaAtual",blTabelaAtual);
		bean.put("blTabelaOriginal",blTabelaOriginal);
		bean.put("blAliquotaOriginal",blAliquotaOriginal);			
		bean.put("tpSituacao", tpSituacaoProcesso.getValue());
			
		/*Situação para a grid*/
		bean.put("tpSituacaoDes", tpSituacaoProcesso.getDescription()
				.getValue());
			
		if(tabelaPrecoCalculo != null){
			bean.put("idTabelaPrecoParametro",
					tabelaPrecoCalculo.getIdTabelaPreco());
			bean.put("tabelaPrecoParametroString",
					tabelaPrecoCalculo.getDsDescricao());
			}
			
		if(tabelaPrecoFiltro != null){
			bean.put("idTabelaPrecoFiltro",
					tabelaPrecoFiltro.getIdTabelaPreco());
			bean.put("tabelaPrecoFiltroString",
					tabelaPrecoFiltro.getDsDescricao());
			}			
			
		if(tipoTabelaPreco != null){
			bean.put("tpTabelaPreco", tipoTabelaPreco.getTpTipoTabelaPreco()
					.getValue());
			}			
			
		if(subtipoTabelaPreco != null){
			bean.put("idSubtipoTabelaPreco",
					subtipoTabelaPreco.getIdSubtipoTabelaPreco());
			}			
			
		return bean;
			}
			
	public List<Long> getClientes(Boolean processa){
			
		List<Long> list = new ArrayList<Long>();
		if(BooleanUtils.isTrue(processa)){
			if(clientesProcessar != null && !clientesProcessar.isEmpty()){
				for (ClienteProcessar cliente : getClientesProcessar()) {
					list.add(cliente.getIdClienteProcessar());
				}		
			}	
		}else{
			if(clientesNaoProcessar != null && !clientesNaoProcessar.isEmpty()){
				for (ClienteNaoProcessar cliente : clientesNaoProcessar) {
					list.add(cliente.getIdClienteNaoProcessar());
				}
			}
		}
		
		return list;
	}

	public Long getIdRecalculoFrete() {
		return idRecalculoFrete;
	}

	public void addClientesNaoProcessar(ClienteNaoProcessar cliente){
		if(clientesNaoProcessar == null){
			clientesNaoProcessar = new ArrayList<ClienteNaoProcessar>();			
	}
		clientesNaoProcessar.add(cliente);
	}
	
	public void addClienteProcessar(ClienteProcessar cliente){
		if(clientesProcessar == null){
			clientesProcessar = new ArrayList<ClienteProcessar>();
	}
		clientesProcessar.add(cliente);
	}

	public void setIdRecalculoFrete(Long idRecalculoFrete) {
		this.idRecalculoFrete = idRecalculoFrete;
	}

	public Long getNrProcesso() {
		return nrProcesso;
	}

	public void setNrProcesso(Long nrProcesso) {
		this.nrProcesso = nrProcesso;
	}

	public String getDsProcesso() {
		return dsProcesso;
		}

	public void setDsProcesso(String dsProcesso) {
		this.dsProcesso = dsProcesso;
	}

	public YearMonthDay getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(YearMonthDay dtInicial) {
		this.dtInicial = dtInicial;
	}

	public YearMonthDay getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(YearMonthDay dtFinal) {
		this.dtFinal = dtFinal;
	}

	public DomainValue getTpSituacaoProcesso() {
		return tpSituacaoProcesso;
	}

	public void setTpSituacaoProcesso(DomainValue tpSituacaoProcesso) {
		this.tpSituacaoProcesso = tpSituacaoProcesso;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}

	public Long getNrTotalRegistros() {
		return nrTotalRegistros;
	}

	public void setNrTotalRegistros(Long nrTotalRegistros) {
		this.nrTotalRegistros = nrTotalRegistros;
	}

	public Long getNrTotalRegistrosProcessados() {
		return nrTotalRegistrosProcessados;
	}

	public void setNrTotalRegistrosProcessados(Long nrTotalRegistrosProcessados) {
		this.nrTotalRegistrosProcessados = nrTotalRegistrosProcessados;
	}

	public Boolean getBlTabelaAtual() {
		return blTabelaAtual;
	}

	public void setBlTabelaAtual(Boolean blTabelaAtual) {
		this.blTabelaAtual = blTabelaAtual;
		}

	public Boolean getBlTabelaOriginal() {
		return blTabelaOriginal;
	}

	public void setBlTabelaOriginal(Boolean blTabelaOriginal) {
		this.blTabelaOriginal = blTabelaOriginal;
	}
	
	public Boolean getBlAliquotaOriginal() {
		return blAliquotaOriginal;
	}

	public void setBlAliquotaOriginal(Boolean blAliquotaOriginal) {
		this.blAliquotaOriginal = blAliquotaOriginal;
		}

	public TabelaPreco getTabelaPrecoCalculo() {
		return tabelaPrecoCalculo;
	}

	public void setTabelaPrecoCalculo(TabelaPreco tabelaPrecoCalculo) {
		this.tabelaPrecoCalculo = tabelaPrecoCalculo;
	}

	public TabelaPreco getTabelaPrecoFiltro() {
		return tabelaPrecoFiltro;
	}

	public void setTabelaPrecoFiltro(TabelaPreco tabelaPrecoFiltro) {
		this.tabelaPrecoFiltro = tabelaPrecoFiltro;
	}

	public TipoTabelaPreco getTipoTabelaPreco() {
		return tipoTabelaPreco;
	}

	public void setTipoTabelaPreco(TipoTabelaPreco tipoTabelaPreco) {
		this.tipoTabelaPreco = tipoTabelaPreco;
	}

	public SubtipoTabelaPreco getSubtipoTabelaPreco() {
		return subtipoTabelaPreco;
	}

	public void setSubtipoTabelaPreco(SubtipoTabelaPreco subtipoTabelaPreco) {
		this.subtipoTabelaPreco = subtipoTabelaPreco;
	}

	public List<ClienteProcessar> getClientesProcessar() {
		return clientesProcessar;
	}

	public void setClientesProcessar(List<ClienteProcessar> clientesProcessar) {
		this.clientesProcessar = clientesProcessar;
	}

	public List<ClienteNaoProcessar> getClientesNaoProcessar() {
		return clientesNaoProcessar;
}

	public void setClientesNaoProcessar(
			List<ClienteNaoProcessar> clientesNaoProcessar) {
		this.clientesNaoProcessar = clientesNaoProcessar;
	}

	}
