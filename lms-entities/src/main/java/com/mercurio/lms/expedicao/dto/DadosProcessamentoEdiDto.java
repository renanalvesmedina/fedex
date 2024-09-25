package com.mercurio.lms.expedicao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DadosProcessamentoEdiDto  implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long idTipoLogradouro;
        private String unidadeFederativa;
        private Boolean blControladoExercito;
        private Boolean blObrigaSerie;
        private Boolean blDivisao;
        private String  descricaoEndereco;
        private Long idUnidadeFederativa;
        private Long idCliente;
        private String tipoCliente;
        private Boolean blControladoPoliciaCivil;
        private String nomePessoa;
        private Boolean blProdutoPerigoso;
        private String descricaoBairro;
        private Boolean blDificuldadeEntrega;
        private String nomePais;
        private String nrEndereco;
        private Boolean blNumeroVolumeEDI;
        private String  numeroCep;
        private Long idMunicipio;
        private String nomeMunicipio;
        private String numeroIdentificacao;
        private String descricaoTipoLogradouro;
        private Boolean blControladoPoliciaFederal;
        private Long idFilial;
        private List<ProcessarNotaPedidoColetaDto> pedidoColeta;
        private List<DivisaoClienteDto> divisaoCliente;
        private List<InscricaoEstadualDto> inscricaoEstadual;

        public Long getIdTipoLogradouro() {
                return idTipoLogradouro;
        }

        public void setIdTipoLogradouro(Long idTipoLogradouro) {
                this.idTipoLogradouro = idTipoLogradouro;
        }

        public String getUnidadeFederativa() {
                return unidadeFederativa;
        }

        public void setUnidadeFederativa(String unidadeFederativa) {
                this.unidadeFederativa = unidadeFederativa;
        }

        public Boolean getBlControladoExercito() {
                return blControladoExercito;
        }

        public void setBlControladoExercito(Boolean blControladoExercito) {
                this.blControladoExercito = blControladoExercito;
        }

        public Boolean getBlObrigaSerie() {
                return blObrigaSerie;
        }

        public void setBlObrigaSerie(Boolean blObrigaSerie) {
                this.blObrigaSerie = blObrigaSerie;
        }

        public Boolean getBlDivisao() {
                return blDivisao;
        }

        public void setBlDivisao(Boolean blDivisao) {
                this.blDivisao = blDivisao;
        }

        public String getDescricaoEndereco() {
                return descricaoEndereco;
        }

        public void setDescricaoEndereco(String descricaoEndereco) {
                this.descricaoEndereco = descricaoEndereco;
        }

        public Long getIdUnidadeFederativa() {
                return idUnidadeFederativa;
        }

        public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
                this.idUnidadeFederativa = idUnidadeFederativa;
        }

        public Long getIdCliente() {
                return idCliente;
        }

        public void setIdCliente(Long idCliente) {
                this.idCliente = idCliente;
        }

        public String getTipoCliente() {
                return tipoCliente;
        }

        public void setTipoCliente(String tipoCliente) {
                this.tipoCliente = tipoCliente;
        }

        public Boolean getBlControladoPoliciaCivil() {
                return blControladoPoliciaCivil;
        }

        public void setBlControladoPoliciaCivil(Boolean blControladoPoliciaCivil) {
                this.blControladoPoliciaCivil = blControladoPoliciaCivil;
        }

        public String getNomePessoa() {
                return nomePessoa;
        }

        public void setNomePessoa(String nomePessoa) {
                this.nomePessoa = nomePessoa;
        }

        public Boolean getBlProdutoPerigoso() {
                return blProdutoPerigoso;
        }

        public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
                this.blProdutoPerigoso = blProdutoPerigoso;
        }

        public String getDescricaoBairro() {
                return descricaoBairro;
        }

        public void setDescricaoBairro(String descricaoBairro) {
                this.descricaoBairro = descricaoBairro;
        }

        public Boolean getBlDificuldadeEntrega() {
                return blDificuldadeEntrega;
        }

        public void setBlDificuldadeEntrega(Boolean blDificuldadeEntrega) {
                this.blDificuldadeEntrega = blDificuldadeEntrega;
        }

        public String getNomePais() {
                return nomePais;
        }

        public void setNomePais(String nomePais) {
                this.nomePais = nomePais;
        }

        public String getNrEndereco() {
                return nrEndereco;
        }

        public void setNrEndereco(String nrEndereco) {
                this.nrEndereco = nrEndereco;
        }

        public Boolean getBlNumeroVolumeEDI() {
                return blNumeroVolumeEDI;
        }

        public void setBlNumeroVolumeEDI(Boolean blNumeroVolumeEDI) {
                this.blNumeroVolumeEDI = blNumeroVolumeEDI;
        }

        public String getNumeroCep() {
                return numeroCep;
        }

        public void setNumeroCep(String numeroCep) {
                this.numeroCep = numeroCep;
        }

        public Long getIdMunicipio() {
                return idMunicipio;
        }

        public void setIdMunicipio(Long idMunicipio) {
                this.idMunicipio = idMunicipio;
        }

        public String getNomeMunicipio() {
                return nomeMunicipio;
        }

        public void setNomeMunicipio(String nomeMunicipio) {
                this.nomeMunicipio = nomeMunicipio;
        }

        public String getNumeroIdentificacao() {
                return numeroIdentificacao;
        }

        public void setNumeroIdentificacao(String numeroIdentificacao) {
                this.numeroIdentificacao = numeroIdentificacao;
        }

        public String getDescricaoTipoLogradouro() {
                return descricaoTipoLogradouro;
        }

        public void setDescricaoTipoLogradouro(String descricaoTipoLogradouro) {
                this.descricaoTipoLogradouro = descricaoTipoLogradouro;
        }

        public Boolean getBlControladoPoliciaFederal() {
                return blControladoPoliciaFederal;
        }

        public void setBlControladoPoliciaFederal(Boolean blControladoPoliciaFederal) {
                this.blControladoPoliciaFederal = blControladoPoliciaFederal;
        }

        public Long getIdFilial() {
                return idFilial;
        }

        public void setIdFilial(Long idFilial) {
                this.idFilial = idFilial;
        }

        public List<ProcessarNotaPedidoColetaDto> getPedidoColeta() {
                return pedidoColeta;
        }

        public void setPedidoColeta(List<ProcessarNotaPedidoColetaDto> pedidoColeta) {
                this.pedidoColeta = pedidoColeta;
        }

        public List<InscricaoEstadualDto> getInscricaoEstadual() {
                return inscricaoEstadual;
        }

        public void setInscricaoEstadual(List<InscricaoEstadualDto> inscricaoEstadual) {
                this.inscricaoEstadual = inscricaoEstadual;
        }

        public List<DivisaoClienteDto> getDivisaoCliente() {
                return divisaoCliente;
        }

        public void setDivisaoCliente(List<DivisaoClienteDto> divisaoCliente) {
                this.divisaoCliente = divisaoCliente;
        }
}
