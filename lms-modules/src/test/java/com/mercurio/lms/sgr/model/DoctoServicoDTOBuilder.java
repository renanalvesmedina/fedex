package com.mercurio.lms.sgr.model;

import java.math.BigDecimal;

import com.mercurio.lms.configuracoes.model.MoedaBuilder;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PaisBuilder;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.vendas.model.Cliente;

public class DoctoServicoDTOBuilder {

	public static DoctoServicoDTOBuilder newDoctoServicoDTO() {
		return new DoctoServicoDTOBuilder();
	}

	private DoctoServicoDTO documento;

	private DoctoServicoDTOBuilder() {
		documento = new DoctoServicoDTO();
		documento.setIdMoeda(MoedaBuilder.BRL().getIdMoeda());
		documento.setIdPais(PaisBuilder.BRASIL().getIdPais());
	}

	public DoctoServicoDTOBuilder valor(double valor) {
		documento.setVlMercadoria(BigDecimal.valueOf(valor));
		return this;
	}

	public DoctoServicoDTOBuilder coleta() {
		documento.setTpOperacao("C");
		return this;
	}

	public DoctoServicoDTOBuilder entrega() {
		documento.setTpOperacao("E");
		return this;
	}

	public DoctoServicoDTOBuilder viagem() {
		documento.setTpOperacao("V");
		return this;
	}

	public DoctoServicoDTOBuilder remetente(Cliente cliente) {
		documento.setIdClienteRemetente(cliente.getIdCliente());
		documento.setNrIdentificacaoRemetente(cliente.getPessoa().getNrIdentificacao());
		documento.setNmPessoaRemetente(cliente.getPessoa().getNmPessoa());
		return this;
	}

	public DoctoServicoDTOBuilder destinatario(Cliente cliente) {
		documento.setIdClienteDestinatario(cliente.getIdCliente());
		documento.setNrIdentificacaoDestinatario(cliente.getPessoa().getNrIdentificacao());
		documento.setNmPessoaDestinatario(cliente.getPessoa().getNmPessoa());
		return this;
	}

	public DoctoServicoDTOBuilder naturezaProduto(NaturezaProduto naturezaProduto) {
		documento.setIdNaturezaProduto(naturezaProduto.getIdNaturezaProduto());
		documento.setDsNaturezaProduto(naturezaProduto.getDsNaturezaProduto().getValue());
		return this;
	}

	public DoctoServicoDTOBuilder nacional() {
		documento.setTpAbrangencia("N");
		return this;
	}

	public DoctoServicoDTOBuilder internacional() {
		documento.setTpAbrangencia("I");
		return this;
	}

	public DoctoServicoDTOBuilder filialOrigem(Filial filial) {
		documento.setIdFilialOrigem(filial.getIdFilial());
		documento.setSgFilialOrigem(filial.getSgFilial());
		return this;
	}

	public DoctoServicoDTOBuilder municipioOrigem(Municipio municipio) {
		documento.setIdMunicipioOrigem(municipio.getIdMunicipio());
		documento.setNmMunicipioOrigem(municipio.getNmMunicipio());
		return this;
	}
	
	public DoctoServicoDTOBuilder unidadeFederativaOrigem(UnidadeFederativa uf) {
		documento.setIdUnidadeFederativaOrigem(uf.getIdUnidadeFederativa());
		documento.setSgUnidadeFederativaOrigem(uf.getSgUnidadeFederativa());
		return this;
	}

	public DoctoServicoDTOBuilder paisOrigem(Pais pais) {
		documento.setIdPaisOrigem(pais.getIdPais());
		documento.setNmPaisOrigem(pais.getNmPais().getValue());
		return this;
	}

	public DoctoServicoDTOBuilder filialDestino(Filial filial) {
		documento.setIdFilialDestino(filial.getIdFilial());
		documento.setSgFilialDestino(filial.getSgFilial());
		return this;
	}

	public DoctoServicoDTOBuilder municipioDestino(Municipio municipio) {
		documento.setIdMunicipioDestino(municipio.getIdMunicipio());
		documento.setNmMunicipioDestino(municipio.getNmMunicipio());
		return this;
	}
	
	public DoctoServicoDTOBuilder unidadeFederativaDestino(UnidadeFederativa uf) {
		documento.setIdUnidadeFederativaDestino(uf.getIdUnidadeFederativa());
		documento.setSgUnidadeFederativaDestino(uf.getSgUnidadeFederativa());
		return this;
	}

	public DoctoServicoDTOBuilder paisDestino(Pais pais) {
		documento.setIdPaisDestino(pais.getIdPais());
		documento.setNmPaisDestino(pais.getNmPais().getValue());
		return this;
	}

	public DoctoServicoDTOBuilder aeroporto() {
		documento.setTpPedidoColeta("AE");
		return this;
	}

	public DoctoServicoDTOBuilder awb(long awb) {
		documento.setIdAwb(awb);
		return this;
	}

	public DoctoServicoDTO build() {
		return documento;
	}

}
