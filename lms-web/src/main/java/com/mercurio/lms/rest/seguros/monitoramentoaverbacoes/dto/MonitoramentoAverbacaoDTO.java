package com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.dto;

import java.util.List;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServicoMdfe;
import com.mercurio.lms.util.JTDateTimeUtils;

public class MonitoramentoAverbacaoDTO extends BaseDTO {

    private Long idAverbacaoDoctoServico;
    private String tpDoctoServico;
    private String sgFilial;
    private String nrDoctoServico;
    private String nmCliente;
    private String tpSituacaoDocumento;
    private String dtAverbacao;
    private String protocolo;
    private String nrAverbacao;
    private boolean averbado;
    private String dsRetorno;
    private String tipoMonitoramentoAverbacao;
    private String tpDocumentoServico;
    private String dsObservacaoSefaz;
    private String modal;
    private String blOperacaoSpitFire;

    public MonitoramentoAverbacaoDTO() {
    }

    public MonitoramentoAverbacaoDTO(AverbacaoDoctoServico averbacaoDoctoServico) {
        setIdAverbacaoDoctoServico(averbacaoDoctoServico.getIdAverbacaoDoctoServico());
        setTpDoctoServico(averbacaoDoctoServico.getDoctoServico().getTpDocumentoServico().getValue());
        setSgFilial(averbacaoDoctoServico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
        setNrDoctoServico(averbacaoDoctoServico.getDoctoServico().getNrDoctoServico().toString());
        setNmCliente(averbacaoDoctoServico.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
        setTpSituacaoDocumento(averbacaoDoctoServico.getDoctoServico().getTpSituacaoConhecimento().getDescription().getValue());
        setNrAverbacao(averbacaoDoctoServico.getNrAverbacao());
        setDsRetorno(averbacaoDoctoServico.getDsRetorno());
        setDtAverbacao(JTDateTimeUtils.formatDateTimeToString(averbacaoDoctoServico.getDhEnvio(), "dd/MM/yyy"));
        setProtocolo(averbacaoDoctoServico.getNrProtocolo());
        setAverbado(averbacaoDoctoServico.getBlAverbado());
        setTipoMonitoramentoAverbacao("CT-e");
        setTpDocumentoServico(averbacaoDoctoServico.getDoctoServico().getTpDocumentoServico().getValue());
        
        
        String modal = "Rodoviário";
		String tpModal = averbacaoDoctoServico.getDoctoServico().getServico().getTpModal().getValue();
		if ("A".equals(tpModal)){
			modal = "Aéreo";
		}
        setModal(modal);
        setDsObservacaoSefaz(averbacaoDoctoServico.getManifestoEletronico() == null ? "" : averbacaoDoctoServico.getManifestoEletronico().getDsObservacaoSefaz());
    }
    
    public MonitoramentoAverbacaoDTO(AverbacaoDoctoServicoMdfe averbacaoDoctoServicoMdfe) {
        setIdAverbacaoDoctoServico(averbacaoDoctoServicoMdfe.getIdAverbacaoDoctoServicoMdfe());
        setTpSituacaoDocumento(averbacaoDoctoServicoMdfe.getManifestoEletronico().getTpSituacao().getDescriptionAsString());
        Long nrProtocola = averbacaoDoctoServicoMdfe.getManifestoEletronico().getNrProtocolo();
        setNrAverbacao(nrProtocola == null ? "" : String.valueOf(nrProtocola));
        setProtocolo(averbacaoDoctoServicoMdfe.getNrProtocolo());
        setAverbado(averbacaoDoctoServicoMdfe.getBlAutorizado() || averbacaoDoctoServicoMdfe.getBlEncerrado());
        
        String tpWebService = averbacaoDoctoServicoMdfe.getTpWebservice();
        if(tpWebService.equals("E")){
            setDsRetorno("Autorizado");
            setDtAverbacao(JTDateTimeUtils.formatDateTimeToString(averbacaoDoctoServicoMdfe.getDhEnvioAutorizado()));
        } else if(tpWebService.equals("C")){
            setDsRetorno("Cancelado");
            setDtAverbacao(JTDateTimeUtils.formatDateTimeToString(averbacaoDoctoServicoMdfe.getDhEnvioCancelado()));
        } else if (tpWebService.equals("G")){
            setDsRetorno("Encerramento");
            setDtAverbacao(JTDateTimeUtils.formatDateTimeToString(averbacaoDoctoServicoMdfe.getDhEnvioEncerrado()));
        } else {
            setDsRetorno(tpWebService);
        }
        
        List<Manifesto> manifestos = averbacaoDoctoServicoMdfe.getManifestoEletronico().getControleCarga().getManifestos();
        Manifesto manifesto = manifestos.get(0);
       
        String modal = "Rodoviário";
		String tpModal = manifesto.getTpModal().getValue();
		if ("A".equals(tpModal)){
			modal = "Aéreo";
		}
        setModal(modal);
        
        setTipoMonitoramentoAverbacao("MDF-e");
        setDsObservacaoSefaz(averbacaoDoctoServicoMdfe.getManifestoEletronico() == null ? "" : averbacaoDoctoServicoMdfe.getManifestoEletronico().getDsObservacaoSefaz());
    
    }

    public Long getIdAverbacaoDoctoServico() {
        return idAverbacaoDoctoServico;
    }

    public void setIdAverbacaoDoctoServico(Long idAverbacaoDoctoServico) {
        this.idAverbacaoDoctoServico = idAverbacaoDoctoServico;
    }

    public String getTpDoctoServico() {
        return tpDoctoServico;
    }

    public void setTpDoctoServico(String tpDoctoServico) {
        this.tpDoctoServico = tpDoctoServico;
    }

    public String getSgFilial() {
        return sgFilial;
    }

    public void setSgFilial(String sgFilial) {
        this.sgFilial = sgFilial;
    }

    public String getNrDoctoServico() {
        return nrDoctoServico;
    }

    public void setNrDoctoServico(String nrDoctoServico) {
        this.nrDoctoServico = nrDoctoServico;
    }

    public String getNmCliente() {
        return nmCliente;
    }

    public void setNmCliente(String nmCliente) {
        this.nmCliente = nmCliente;
    }

    public String getTpSituacaoDocumento() {
        return tpSituacaoDocumento;
    }

    public void setTpSituacaoDocumento(String tpSituacaoDocumento) {
        this.tpSituacaoDocumento = tpSituacaoDocumento;
    }

    public String getDtAverbacao() {
        return dtAverbacao;
    }

    public void setDtAverbacao(String dtAverbacao) {
        this.dtAverbacao = dtAverbacao;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getNrAverbacao() {
        return nrAverbacao;
    }

    public void setNrAverbacao(String nrAverbacao) {
        this.nrAverbacao = nrAverbacao;
    }

    public boolean isAverbado() {
        return averbado;
    }

    public void setAverbado(boolean averbado) {
        this.averbado = averbado;
    }

    public String getDsRetorno() {
        return dsRetorno;
    }

    public void setDsRetorno(String dsRetorno) {
        this.dsRetorno = dsRetorno;
    }

    public String getTipoMonitoramentoAverbacao() {
        return tipoMonitoramentoAverbacao;
    }

    public void setTipoMonitoramentoAverbacao(String tipoMonitoramentoAverbacao) {
        this.tipoMonitoramentoAverbacao = tipoMonitoramentoAverbacao;
    }

    public String getTpDocumentoServico() {
        return tpDocumentoServico;
    }

    public void setTpDocumentoServico(String tpDocumentoServico) {
        this.tpDocumentoServico = tpDocumentoServico;
    }

    public String getDsObservacaoSefaz() {
        return dsObservacaoSefaz;
    }

    public void setDsObservacaoSefaz(String dsObservacaoSefaz) {
        this.dsObservacaoSefaz = dsObservacaoSefaz;
    }

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

    public String getBlOperacaoSpitFire() {
        return blOperacaoSpitFire;
    }

    public void setBlOperacaoSpitFire(String blOperacaoSpitFire) {
        this.blOperacaoSpitFire = blOperacaoSpitFire;
    }
}
