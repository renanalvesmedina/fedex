package com.mercurio.lms.edi.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.expedicao.model.DoctoServico;

/**
 * LMSA-6520: LMSA-6534
 */
@Entity
@Table(name = "CONHECIMENTO_FEDEX")
public class ConhecimentoFedex implements java.io.Serializable {

    private static final long serialVersionUID = -5921046595270388254L;
    
    private Long idConhecimentoFedex;
    private ControleCarga controleCarga;
    private DoctoServico doctoServico;
    
    private String cnpjRemetente;
    private String nomeRemetente;
    private String cnpjDestinatario;
    private String nomeDestinatario;
    private String cnpjTomador;
    private String nomeTomador;

    private String siglaFilialOrigem;
    private String siglaFilialDestino;
    
    private String tipoServico;
    private String tipoMoeda;
    
    private String tipoDocumento;
    private String tipoFrete;
    private String numeroConhecimento;
    
    private String numeroSerie;
    private String chaveCTEFedex;
    
    private Date dataEmissao;
    private Date dataPrevisaoEntrega;
    
    private BigDecimal valorMercadoria;
    
    private BigDecimal qtdvolume;
    private BigDecimal pesoReal;
    private BigDecimal pesoAferido;
    private BigDecimal pesoCubado;
    private BigDecimal pesoCalculoFrete;
    
    private String chaveMdfe;
    private String placaVeiculo;
    private String placaCarreta;
    
    private String numeroIdentificacaoMotorista;
    private String nomeMotorista;
    
    private String numeroManifestoViagemFedex;
    
    private String naturezaProduto;
    private String especieDocumento;
    
    private Date dataLogEDI;
    
    private Long idPais;
    
    private String cteDescarregado;
    
	private List<ConhecimentoVolumeFedex> volumes;

	public ConhecimentoFedex() {
	}

	@Id
	@Column(name = "ID_CONHECIMENTO_FEDEX", nullable = false)
    public Long getIdConhecimentoFedex() {
        return idConhecimentoFedex;
    }

    public void setIdConhecimentoFedex(Long idConhecimentoFedex) {
        this.idConhecimentoFedex = idConhecimentoFedex;
    }

    @ManyToOne
    @JoinColumn(name = "ID_CONTROLE_CARGA")
    public ControleCarga getControleCarga() {
        return controleCarga;
    }

    public void setControleCarga(ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    @ManyToOne
    @JoinColumn(name = "ID_DOCTO_SERVICO")
    public DoctoServico getDoctoServico() {
        return doctoServico;
    }

    public void setDoctoServico(DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    @Column(name = "NR_CNPJ_REMETENTE", length = 14)
    public String getCnpjRemetente() {
        return cnpjRemetente;
    }

    public void setCnpjRemetente(String cnpjRemetente) {
        this.cnpjRemetente = cnpjRemetente;
    }

    @Column(name = "NM_REMETENTE", length = 40)
    public String getNomeRemetente() {
        return nomeRemetente;
    }

    public void setNomeRemetente(String nomeRemetente) {
        this.nomeRemetente = nomeRemetente;
    }

    @Column(name = "NR_CNPJ_DESTINATARIO", length = 14)
    public String getCnpjDestinatario() {
        return cnpjDestinatario;
    }

    public void setCnpjDestinatario(String cnpjDestinatario) {
        this.cnpjDestinatario = cnpjDestinatario;
    }

    @Column(name = "NM_DESTINATARIO", length = 40)
    public String getNomeDestinatario() {
        return nomeDestinatario;
    }

    public void setNomeDestinatario(String nomeDestinatario) {
        this.nomeDestinatario = nomeDestinatario;
    }

    @Column(name = "NR_CNPJ_TOMADOR", length = 14)
    public String getCnpjTomador() {
        return cnpjTomador;
    }

    public void setCnpjTomador(String cnpjTomador) {
        this.cnpjTomador = cnpjTomador;
    }

    @Column(name = "NM_TOMADOR", length = 40)
    public String getNomeTomador() {
        return nomeTomador;
    }

    public void setNomeTomador(String nomeTomador) {
        this.nomeTomador = nomeTomador;
    }

    @Column(name="SG_FILIAL_ORIGEM", length=4)
    public String getSiglaFilialOrigem() {
        return siglaFilialOrigem;
    }

    public void setSiglaFilialOrigem(String siglaFilialOrigem) {
        this.siglaFilialOrigem = siglaFilialOrigem;
    }

    @Column(name="SG_FILIAL_DESTINO", length=4)
    public String getSiglaFilialDestino() {
        return siglaFilialDestino;
    }

    public void setSiglaFilialDestino(String siglaFilialDestino) {
        this.siglaFilialDestino = siglaFilialDestino;
    }

    @Column(name="TP_SERVICO", length=1)
    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    @Column(name="TP_MOEDA", length=1)
    public String getTipoMoeda() {
        return tipoMoeda;
    }

    public void setTipoMoeda(String tipoMoeda) {
        this.tipoMoeda = tipoMoeda;
    }

    @Column(name="TP_DOCUMENTO", length=3)
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Column(name="TP_FRETE", length=3)
    public String getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    @Column(name="NR_CONHECIMENTO", length=9)
    public String getNumeroConhecimento() {
        return numeroConhecimento;
    }

    public void setNumeroConhecimento(String numeroConhecimento) {
        this.numeroConhecimento = numeroConhecimento;
    }

    @Column(name="NR_SERIE", length=3)
    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    @Column(name="NR_CHAVE_CTE_FEDEX", length=44)
    public String getChaveCTEFedex() {
        return chaveCTEFedex;
    }

    public void setChaveCTEFedex(String chaveCTEFedex) {
        this.chaveCTEFedex = chaveCTEFedex;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DT_EMISSAO")
    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DT_PREV_ENTREGA")
    public Date getDataPrevisaoEntrega() {
        return dataPrevisaoEntrega;
    }

    public void setDataPrevisaoEntrega(Date dataPrevisaoEntrega) {
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    @Column(name="VL_MERCADORIA", length=18)
    public BigDecimal getValorMercadoria() {
        return valorMercadoria;
    }

    public void setValorMercadoria(BigDecimal valorMercadoria) {
        this.valorMercadoria = valorMercadoria;
    }

    @Column(name="QT_VOLUMES", length=5)
    public BigDecimal getQtdvolume() {
        return qtdvolume;
    }

    public void setQtdvolume(BigDecimal qtdvolume) {
        this.qtdvolume = qtdvolume;
    }

    @Column(name="PS_REAL", length=18)
    public BigDecimal getPesoReal() {
        return pesoReal;
    }

    public void setPesoReal(BigDecimal pesoReal) {
        this.pesoReal = pesoReal;
    }

    @Column(name="PS_AFERIDO", length=18)
    public BigDecimal getPesoAferido() {
        return pesoAferido;
    }

    public void setPesoAferido(BigDecimal pesoAferido) {
        this.pesoAferido = pesoAferido;
    }

    @Column(name="PS_CUBADO", length=18)
    public BigDecimal getPesoCubado() {
        return pesoCubado;
    }

    public void setPesoCubado(BigDecimal pesoCubado) {
        this.pesoCubado = pesoCubado;
    }

    @Column(name="PS_CALCULO_FRETE", length=18)
    public BigDecimal getPesoCalculoFrete() {
        return pesoCalculoFrete;
    }

    public void setPesoCalculoFrete(BigDecimal pesoCalculoFrete) {
        this.pesoCalculoFrete = pesoCalculoFrete;
    }

    @Column(name="NR_CHAVE_MDFE", length=44)
    public String getChaveMdfe() {
        return chaveMdfe;
    }

    public void setChaveMdfe(String chaveMdfe) {
        this.chaveMdfe = chaveMdfe;
    }

    @Column(name="NR_PLACA_VEICULO", length=10)
    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    @Column(name="NR_PLACA_CARRETA", length=10)
    public String getPlacaCarreta() {
        return placaCarreta;
    }

    public void setPlacaCarreta(String placaCarreta) {
        this.placaCarreta = placaCarreta;
    }

    @Column(name="NR_IDENTIFICACAO_MOTORISTA", length=14)
    public String getNumeroIdentificacaoMotorista() {
        return numeroIdentificacaoMotorista;
    }

    public void setNumeroIdentificacaoMotorista(String numeroIdentificacaoMotorista) {
        this.numeroIdentificacaoMotorista = numeroIdentificacaoMotorista;
    }

    @Column(name="NM_MOTORISTA", length=30)
    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    @Column(name="NR_MANIFESTO_VIAGEM_FEDEX", length=20)
    public String getNumeroManifestoViagemFedex() {
        return numeroManifestoViagemFedex;
    }

    public void setNumeroManifestoViagemFedex(String numeroManifestoViagemFedex) {
        this.numeroManifestoViagemFedex = numeroManifestoViagemFedex;
    }

    @Column(name="DS_NATUREZA_PRODUTO", length=15)
    public String getNaturezaProduto() {
        return naturezaProduto;
    }

    public void setNaturezaProduto(String naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    @Column(name="DS_ESPECIE_DOCUMENTO", length=15)
    public String getEspecieDocumento() {
        return especieDocumento;
    }

    public void setEspecieDocumento(String especieDocumento) {
        this.especieDocumento = especieDocumento;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="DH_LOG_EDI")
    public Date getDataLogEDI() {
        return dataLogEDI;
    }

    public void setDataLogEDI(Date dataLogEDI) {
        this.dataLogEDI = dataLogEDI;
    }

    @Column(name="ID_PAIS")
    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    @Column(name="BL_CTE_DESCARREGADO")
    public String getCteDescarregado() {
        return cteDescarregado;
    }

    public void setCteDescarregado(String cteDescarregado) {
        this.cteDescarregado = cteDescarregado;
    }

    @OneToMany(mappedBy = "conhecimentoVolumeFedex")
    @Transient
    public List<ConhecimentoVolumeFedex> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<ConhecimentoVolumeFedex> volumes) {
        this.volumes = volumes;
    }
	
}
