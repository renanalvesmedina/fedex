package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.MacroZona;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolumeNotaFiscal implements Serializable {
	private static final long serialVersionUID = 1L;
	/** identifier field */
	private Long idVolumeNotaFiscal;
	/** nullable persistent field */
	private Long nrConhecimento;
	/** persistent field */
	private Integer nrSequencia;
	/** nullable persistent field */
	private String nrVolumeColeta;
	/** nullable persistent field */
	private String nrVolumeEmbarque;
	/** nullable persistent field */
	private BigDecimal psAferido;
	/** persistent field */
	private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;
	/** persistent field */
	private com.mercurio.lms.expedicao.model.MonitoramentoDescarga monitoramentoDescarga;
	/** persistent field */
	private Integer qtVolumes;
	/** persistent field */
	private String tpVolume;
	/** persistent field */
	private Integer nrSequenciaPalete;
	private Integer nrDimensao1Cm;
	private Integer nrDimensao2Cm;
	private Integer nrDimensao3Cm;
	private Double nrCubagemM3; 
	private DateTime dhPesagem;
	/** persistent field */
	private com.mercurio.lms.sim.model.LocalizacaoMercadoria localizacaoMercadoria;
	/** persistent field */
	private com.mercurio.lms.municipios.model.Filial localizacaoFilial;
	/** identifier field */
	private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao;
	/** identifier field */
	private MacroZona macroZona;
	/** Bag */
	private List<PreManifestoVolume> preManifestoVolumes;
	/** Bag */
	private List<ManifestoEntregaVolume> manifestoEntregaVolumes;
	/** Bag */
	private List<ManifestoNacionalVolume> manifestoNacionalVolumes;
	/** Bag */
	private List<CarregamentoDescargaVolume> carregamentoDescargaVolumes;
	/** persistent field */
	private String dsMac;
	/** persistent field */
	private DateTime dhEmissao;
	/** identifier field */
	private UsuarioLMS usuario;
	/** identifier field */
	private Filial filialEmissao;
	/** nullable persistent field */
	private DomainValue tpOrigemPeso;
	/** nullable persistent field */
	private DomainValue tpOrigemDimensoes;
	/** nullable persistent field */
	private Integer nrDimensao1Sorter;
	/** nullable persistent field */
	private Integer nrDimensao2Sorter;
	/** nullable persistent field */
	private Integer nrDimensao3Sorter;
	/** nullable persistent field */
	private BigDecimal psAferidoSorter;
	private Integer nrSeqNoPalete;

	public VolumeNotaFiscal() {
	}

	public VolumeNotaFiscal(Long idVolumeNotaFiscal, Long nrConhecimento, Integer nrSequencia, String nrVolumeColeta, String nrVolumeEmbarque, BigDecimal psAferido, NotaFiscalConhecimento notaFiscalConhecimento, MonitoramentoDescarga monitoramentoDescarga, Integer qtVolumes, String tpVolume, Integer nrSequenciaPalete, Integer nrDimensao1Cm, Integer nrDimensao2Cm, Integer nrDimensao3Cm, Double nrCubagemM3, DateTime dhPesagem, LocalizacaoMercadoria localizacaoMercadoria, Filial localizacaoFilial, DispositivoUnitizacao dispositivoUnitizacao, MacroZona macroZona, List<PreManifestoVolume> preManifestoVolumes, List<ManifestoEntregaVolume> manifestoEntregaVolumes, List<ManifestoNacionalVolume> manifestoNacionalVolumes, List<CarregamentoDescargaVolume> carregamentoDescargaVolumes, String dsMac, DateTime dhEmissao, UsuarioLMS usuario, Filial filialEmissao, DomainValue tpOrigemPeso, DomainValue tpOrigemDimensoes, Integer nrDimensao1Sorter, Integer nrDimensao2Sorter, Integer nrDimensao3Sorter, BigDecimal psAferidoSorter, Integer nrSeqNoPalete) {
		this.idVolumeNotaFiscal = idVolumeNotaFiscal;
		this.nrConhecimento = nrConhecimento;
		this.nrSequencia = nrSequencia;
		this.nrVolumeColeta = nrVolumeColeta;
		this.nrVolumeEmbarque = nrVolumeEmbarque;
		this.psAferido = psAferido;
		this.notaFiscalConhecimento = notaFiscalConhecimento;
		this.monitoramentoDescarga = monitoramentoDescarga;
		this.qtVolumes = qtVolumes;
		this.tpVolume = tpVolume;
		this.nrSequenciaPalete = nrSequenciaPalete;
		this.nrDimensao1Cm = nrDimensao1Cm;
		this.nrDimensao2Cm = nrDimensao2Cm;
		this.nrDimensao3Cm = nrDimensao3Cm;
		this.nrCubagemM3 = nrCubagemM3;
		this.dhPesagem = dhPesagem;
		this.localizacaoMercadoria = localizacaoMercadoria;
		this.localizacaoFilial = localizacaoFilial;
		this.dispositivoUnitizacao = dispositivoUnitizacao;
		this.macroZona = macroZona;
		this.preManifestoVolumes = preManifestoVolumes;
		this.manifestoEntregaVolumes = manifestoEntregaVolumes;
		this.manifestoNacionalVolumes = manifestoNacionalVolumes;
		this.carregamentoDescargaVolumes = carregamentoDescargaVolumes;
		this.dsMac = dsMac;
		this.dhEmissao = dhEmissao;
		this.usuario = usuario;
		this.filialEmissao = filialEmissao;
		this.tpOrigemPeso = tpOrigemPeso;
		this.tpOrigemDimensoes = tpOrigemDimensoes;
		this.nrDimensao1Sorter = nrDimensao1Sorter;
		this.nrDimensao2Sorter = nrDimensao2Sorter;
		this.nrDimensao3Sorter = nrDimensao3Sorter;
		this.psAferidoSorter = psAferidoSorter;
		this.nrSeqNoPalete = nrSeqNoPalete;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VolumeNotaFiscal)) {
			return false;
		}
		VolumeNotaFiscal castOther = (VolumeNotaFiscal) other;
		return new EqualsBuilder().append(this.getIdVolumeNotaFiscal(), castOther.getIdVolumeNotaFiscal()).isEquals();
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public DateTime getDhPesagem() {
		return dhPesagem;
	}

	public com.mercurio.lms.carregamento.model.DispositivoUnitizacao getDispositivoUnitizacao() {
		return dispositivoUnitizacao;
	}

	public String getDsMac() {
		return dsMac;
	}

	public Filial getFilialEmissao() {
		return filialEmissao;
	}

	public Long getIdVolumeNotaFiscal() {
		return this.idVolumeNotaFiscal;
	}

	public com.mercurio.lms.municipios.model.Filial getLocalizacaoFilial() {
		return localizacaoFilial;
	}

	public com.mercurio.lms.sim.model.LocalizacaoMercadoria getLocalizacaoMercadoria() {
		return localizacaoMercadoria;
	}

	public MacroZona getMacroZona() {
		return macroZona;
	}

	/**
	 * @return the manifestoEntregaVolumes
	 */
	public List<ManifestoEntregaVolume> getManifestoEntregaVolumes() {
		return manifestoEntregaVolumes;
	}

	/**
	 * @return the manifestoNacionalVolumes
	 */
	public List<ManifestoNacionalVolume> getManifestoNacionalVolumes() {
		return manifestoNacionalVolumes;
	}

	public com.mercurio.lms.expedicao.model.MonitoramentoDescarga getMonitoramentoDescarga() {
		return this.monitoramentoDescarga;
	}

	public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
		return this.notaFiscalConhecimento;
	}

	public Long getNrConhecimento() {
		return this.nrConhecimento;
	}

	public Integer getNrDimensao1Cm() {
		return nrDimensao1Cm;
	}

	public Integer getNrDimensao2Cm() {
		return nrDimensao2Cm;
	}

	public Integer getNrDimensao3Cm() {
		return nrDimensao3Cm;
	}

	public Integer getNrSequencia() {
		return this.nrSequencia;
	}

	public Integer getNrSequenciaPalete() {
		return nrSequenciaPalete;
	}

	public String getNrVolumeColeta() {
		return this.nrVolumeColeta;
	}

	public String getNrVolumeEmbarque() {
		return this.nrVolumeEmbarque;
	}

	public List<PreManifestoVolume> getPreManifestoVolumes() {
		return preManifestoVolumes;
	}

	public BigDecimal getPsAferido() {
		return this.psAferido;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public String getTpVolume() {
		return tpVolume;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdVolumeNotaFiscal()).toHashCode();
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public void setDhPesagem(DateTime dhPesagem) {
		this.dhPesagem = dhPesagem;
	}

	public void setDispositivoUnitizacao(com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao) {
		this.dispositivoUnitizacao = dispositivoUnitizacao;
	}

	public void setDsMac(String dsMac) {
		this.dsMac = dsMac;
	}

	public void setFilialEmissao(Filial filialEmissao) {
		this.filialEmissao = filialEmissao;
	}

	public void setIdVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		this.idVolumeNotaFiscal = idVolumeNotaFiscal;
	}

	public void setLocalizacaoFilial(com.mercurio.lms.municipios.model.Filial localizacaoFilial) {
		this.localizacaoFilial = localizacaoFilial;
	}

	public void setLocalizacaoMercadoria(com.mercurio.lms.sim.model.LocalizacaoMercadoria localizacaoMercadoria) {
		this.localizacaoMercadoria = localizacaoMercadoria;
	}

	public void setMacroZona(MacroZona macroZona) {
		this.macroZona = macroZona;
	}

	/**
	 * @param manifestoEntregaVolumes
	 *            the manifestoEntregaVolumes to set
	 */
	public void setManifestoEntregaVolumes(List<ManifestoEntregaVolume> manifestoEntregaVolumes) {
		this.manifestoEntregaVolumes = manifestoEntregaVolumes;
	}

	/**
	 * @param manifestoNacionalVolumes
	 *            the manifestoNacionalVolumes to set
	 */
	public void setManifestoNacionalVolumes(List<ManifestoNacionalVolume> manifestoNacionalVolumes) {
		this.manifestoNacionalVolumes = manifestoNacionalVolumes;
	}

	public void setMonitoramentoDescarga(com.mercurio.lms.expedicao.model.MonitoramentoDescarga monitoramentoDescarga) {
		this.monitoramentoDescarga = monitoramentoDescarga;
	}

	public void setNotaFiscalConhecimento(com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
		this.notaFiscalConhecimento = notaFiscalConhecimento;
	}

	public void setNrConhecimento(Long nrConhecimento) {
		this.nrConhecimento = nrConhecimento;
	}

	public void setNrDimensao1Cm(Integer nrDimensao1Cm) {
		this.nrDimensao1Cm = nrDimensao1Cm;
	}

	public void setNrDimensao2Cm(Integer nrDimensao2Cm) {
		this.nrDimensao2Cm = nrDimensao2Cm;
	}

	public void setNrDimensao3Cm(Integer nrDimensao3Cm) {
		this.nrDimensao3Cm = nrDimensao3Cm;
	}

	public void setNrSequencia(Integer nrSequencia) {
		this.nrSequencia = nrSequencia;
	}

	public void setNrSequenciaPalete(Integer nrSequenciaPalete) {
		this.nrSequenciaPalete = nrSequenciaPalete;
	}

	public void setNrVolumeColeta(String nrVolumeColeta) {
		this.nrVolumeColeta = nrVolumeColeta;
	}

	public void setNrVolumeEmbarque(String nrVolumeEmbarque) {
		this.nrVolumeEmbarque = nrVolumeEmbarque;
	}

	public void setPreManifestoVolumes(List<PreManifestoVolume> preManifestoVolumes) {
		this.preManifestoVolumes = preManifestoVolumes;
	}

	public void setPsAferido(BigDecimal psAferido) {
		this.psAferido = psAferido;
	}
	
	public Double getNrCubagemM3() {
		return nrCubagemM3;
	}
	
	public void setNrCubagemM3(Double nrCubagemM3) {
		this.nrCubagemM3 = nrCubagemM3;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public void setTpVolume(String tpVolume) {
		this.tpVolume = tpVolume;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idVolumeNotaFiscal", getIdVolumeNotaFiscal()).toString();
	}

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_VOLUME_NOTA_FISCAL", idVolumeNotaFiscal)
				.append("ID_NOTA_FISCAL_CONHECIMENTO", notaFiscalConhecimento != null ? notaFiscalConhecimento.getIdNotaFiscalConhecimento() : null)
				.append("NR_CONHECIMENTO", nrConhecimento)
				.append("NR_SEQUENCIA", nrSequencia)
				.append("NR_VOLUME_EMBARQUE", nrVolumeEmbarque)
				.append("PS_AFERIDO", psAferido)
				.append("ID_MONITORAMENTO_DESCARGA", monitoramentoDescarga != null ? monitoramentoDescarga.getIdMonitoramentoDescarga() : null)
				.append("QT_VOLUMES", qtVolumes)
				.append("TP_VOLUME", tpVolume)
				.append("NR_SEQUENCIA_PALETE", nrSequenciaPalete)
				.append("NR_DIMENSAO_1_CM", nrDimensao1Cm)
				.append("NR_DIMENSAO_2_CM", nrDimensao2Cm)
				.append("NR_DIMENSAO_3_CM", nrDimensao3Cm)
				.append("DH_PESAGEM", dhPesagem)
				.append("ID_LOCALIZACAO_MERCADORIA", localizacaoMercadoria != null ? localizacaoMercadoria.getIdLocalizacaoMercadoria() : null)
				.append("ID_LOCALIZACAO_FILIAL", localizacaoFilial != null ? localizacaoFilial.getIdFilial() : null)
				.append("ID_DISPOSITIVO_UNITIZACAO", dispositivoUnitizacao != null ? dispositivoUnitizacao.getIdDispositivoUnitizacao() : null)
				.append("ID_MACRO_ZONA", macroZona != null ? macroZona.getIdMacroZona() : null)
				.append("NR_VOLUME_COLETA", nrVolumeColeta)
				.append("DS_MAC", dsMac)
				.append("DH_EMISSAO", dhEmissao)
				.append("ID_USUARIO_EMISSAO", usuario != null ? usuario.getIdUsuario() : null)
				.append("ID_FILIAL_EMISSAO", filialEmissao != null ? filialEmissao.getIdFilial() : null)
				.append("NR_CUBAGEM", nrCubagemM3)
				.append("TP_ORIGEM_PESO", tpOrigemPeso != null ? tpOrigemPeso.getValue() : null)
				.append("TP_ORIGEM_DIMENSOES", tpOrigemDimensoes != null ? tpOrigemDimensoes.getValue() : null)
				.append("NR_DIMENSAO_1_SORTER", nrDimensao1Sorter)
				.append("NR_DIMENSAO_2_SORTER", nrDimensao2Sorter)
				.append("NR_DIMENSAO_3_SORTER", nrDimensao3Sorter)
				.append("PS_AFERIDO_SORTER", psAferidoSorter)
				.append("NR_SEQ_NO_PALETE", nrSeqNoPalete)
				.toString();
	}

	public DomainValue getTpOrigemPeso() {
		return tpOrigemPeso;
	}

	public void setTpOrigemPeso(DomainValue tpOrigemPeso) {
		this.tpOrigemPeso = tpOrigemPeso;
	}

	public DomainValue getTpOrigemDimensoes() {
		return tpOrigemDimensoes;
	}

	public void setTpOrigemDimensoes(DomainValue tpOrigemDimensoes) {
		this.tpOrigemDimensoes = tpOrigemDimensoes;
	}

	public Integer getNrDimensao1Sorter() {
		return nrDimensao1Sorter;
	}

	public void setNrDimensao1Sorter(Integer nrDimensao1Sorter) {
		this.nrDimensao1Sorter = nrDimensao1Sorter;
	}

	public Integer getNrDimensao2Sorter() {
		return nrDimensao2Sorter;
	}

	public void setNrDimensao2Sorter(Integer nrDimensao2Sorter) {
		this.nrDimensao2Sorter = nrDimensao2Sorter;
	}

	public Integer getNrDimensao3Sorter() {
		return nrDimensao3Sorter;
	}

	public void setNrDimensao3Sorter(Integer nrDimensao3Sorter) {
		this.nrDimensao3Sorter = nrDimensao3Sorter;
	}

	public BigDecimal getPsAferidoSorter() {
		return psAferidoSorter;
	}

	public void setPsAferidoSorter(BigDecimal psAferidoSorter) {
		this.psAferidoSorter = psAferidoSorter;
	}
	public Integer getNrSeqNoPalete() {
		return nrSeqNoPalete;
	}
	public void setNrSeqNoPalete(Integer nrSeqNoPalete) {
		this.nrSeqNoPalete = nrSeqNoPalete;
	}

	public List<CarregamentoDescargaVolume> getCarregamentoDescargaVolumes() {
		return carregamentoDescargaVolumes;
	}

	public void setCarregamentoDescargaVolumes(
			List<CarregamentoDescargaVolume> carregamentoDescargaVolumes) {
		this.carregamentoDescargaVolumes = carregamentoDescargaVolumes;
	}
}