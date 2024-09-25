package com.mercurio.lms.sgr.dto;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * LMS-6850 - Data Transfer Object para resultado da busca de
 * {@link DoctoServico}s de um {@link ControleCarga} para enquadramento de
 * regras do Plano de Gerenciamento de Riscos. O formato do DTO corresponde ao
 * resultado da query em
 * {@link PlanoGerenciamentoRiscoDAO#findDoctoServico(Long)}, sendo que cada
 * coluna é mapeada para a respectiva propriedade deste DTO.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class DoctoServicoDTO {

	private BigDecimal vlMercadoria;
	private String tpOperacao;
	private Long idMoeda;
	private Long idPais;
	private Long idClienteRemetente;
	private String nrIdentificacaoRemetente;
	private String nmPessoaRemetente;
	private Long idClienteDestinatario;
	private String nrIdentificacaoDestinatario;
	private String nmPessoaDestinatario;
	private Long idNaturezaProduto;
	private String dsNaturezaProduto;
	private String tpAbrangencia;
	private Long idFilialOrigem;
	private String sgFilialOrigem;
	private Long idMunicipioOrigem;
	private String nmMunicipioOrigem;
	private Long idUnidadeFederativaOrigem;
	private String sgUnidadeFederativaOrigem;
	private Long idPaisOrigem;
	private String nmPaisOrigem;
	private Long idFilialDestino;
	private String sgFilialDestino;
	private Long idMunicipioDestino;
	private String nmMunicipioDestino;
	private Long idUnidadeFederativaDestino;
	private String sgUnidadeFederativaDestino;
	private Long idPaisDestino;
	private String nmPaisDestino;
	private String tpPedidoColeta;
	private Long idAwb;

	/**
	 * Construtor base utilizado exclusivamente para TDD.
	 * 
	 */
	public DoctoServicoDTO() {
		super();
	}

	/**
	 * Construtor especial para utilização com bloco de dados retornado por
	 * {@link PlanoGerenciamentoRiscoDAO#findDoctoServico(Long)}. Os atributos
	 * deverão ser obrigatoriamente setados na mesma ordem das colunas na query
	 * SQL.
	 * 
	 * @param data
	 *            Bloco de dados retornado por query SQL.
	 */
	public DoctoServicoDTO(Object[] data) {
		this();

		int i = 0;
		vlMercadoria = (BigDecimal) data[i++];
		tpOperacao = (String) data[i++];
		idMoeda = (Long) data[i++];
		idPais = (Long) data[i++];
		idClienteRemetente = (Long) data[i++];
		nrIdentificacaoRemetente = (String) data[i++];
		nmPessoaRemetente = (String) data[i++];
		idClienteDestinatario = (Long) data[i++];
		nrIdentificacaoDestinatario = (String) data[i++];
		nmPessoaDestinatario = (String) data[i++];
		idNaturezaProduto = (Long) data[i++];
		dsNaturezaProduto = (String) data[i++];
		tpAbrangencia = (String) data[i++];
		idFilialOrigem = (Long) data[i++];
		sgFilialOrigem = (String) data[i++];
		idMunicipioOrigem = (Long) data[i++];
		nmMunicipioOrigem = (String) data[i++];
		idUnidadeFederativaOrigem = (Long) data[i++];
		sgUnidadeFederativaOrigem = (String) data[i++];
		idPaisOrigem = (Long) data[i++];
		nmPaisOrigem = (String) data[i++];
		idFilialDestino = (Long) data[i++];
		sgFilialDestino = (String) data[i++];
		idMunicipioDestino = (Long) data[i++];
		nmMunicipioDestino = (String) data[i++];
		idUnidadeFederativaDestino = (Long) data[i++];
		sgUnidadeFederativaDestino = (String) data[i++];
		idPaisDestino = (Long) data[i++];
		nmPaisDestino = (String) data[i++];
		tpPedidoColeta = (String) data[i++];
		idAwb = (Long) data[i++];
	}

	/**
	 * Construtor de cópia utilizado na verificação geral de um
	 * {@link ControleCarga}. Copia todos os atributos necessários para o filtro
	 * de {@link EnquadramentoRegra}s na verificação geral:
	 * <ul>
	 * <li>Tipo de abrangência (nacional/internacional);
	 * <li>Tipo de operação (coleta/entrega/viagem);
	 * <li>{@link Pais} de origem e de destino;
	 * <li>{@link UnidadeFederativa} de origem e de destino;
	 * <li>{@link Municipio} de origem e de destino;
	 * <li>{@link Filial} de origem e de destino (id e sigla);
	 * </ul>
	 * Os atributos relacionados aos {@link Cliente} remetente e destinatário, à
	 * {@link NaturezaProduto} e outros desnecessários ao filtro de
	 * {@link EnquadramentoRegra}s são desconsiderados.
	 * 
	 * @param documento
	 *            Objeto para cópia.
	 */
	public DoctoServicoDTO(DoctoServicoDTO documento) {
		tpAbrangencia = documento.getTpAbrangencia();
		tpOperacao = documento.getTpOperacao();
		idPaisOrigem = documento.getIdPaisOrigem();
		idUnidadeFederativaOrigem = documento.getIdUnidadeFederativaOrigem();
		idMunicipioOrigem = documento.getIdMunicipioOrigem();
		idFilialOrigem = documento.getIdFilialOrigem();
		sgFilialOrigem = documento.getSgFilialOrigem();
		idPaisDestino = documento.getIdPaisDestino();
		idUnidadeFederativaDestino = documento.getIdUnidadeFederativaDestino();
		idMunicipioDestino = documento.getIdMunicipioDestino();
		idFilialDestino = documento.getIdFilialDestino();
		sgFilialDestino = documento.getSgFilialDestino();
	}

	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public String getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(String tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public Long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public Long getIdPais() {
		return idPais;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public Long getIdClienteRemetente() {
		return idClienteRemetente;
	}

	public void setIdClienteRemetente(Long idClienteRemetente) {
		this.idClienteRemetente = idClienteRemetente;
	}

	public String getNrIdentificacaoRemetente() {
		return nrIdentificacaoRemetente;
	}

	public void setNrIdentificacaoRemetente(String nrIdentificacaoRemetente) {
		this.nrIdentificacaoRemetente = nrIdentificacaoRemetente;
	}

	public String getNmPessoaRemetente() {
		return nmPessoaRemetente;
	}

	public void setNmPessoaRemetente(String nmPessoaRemetente) {
		this.nmPessoaRemetente = nmPessoaRemetente;
	}

	public Long getIdClienteDestinatario() {
		return idClienteDestinatario;
	}

	public void setIdClienteDestinatario(Long idClienteDestinatario) {
		this.idClienteDestinatario = idClienteDestinatario;
	}

	public String getNrIdentificacaoDestinatario() {
		return nrIdentificacaoDestinatario;
	}

	public void setNrIdentificacaoDestinatario(String nrIdentificacaoDestinatario) {
		this.nrIdentificacaoDestinatario = nrIdentificacaoDestinatario;
	}

	public String getNmPessoaDestinatario() {
		return nmPessoaDestinatario;
	}

	public void setNmPessoaDestinatario(String nmPessoaDestinatario) {
		this.nmPessoaDestinatario = nmPessoaDestinatario;
	}

	public Long getIdNaturezaProduto() {
		return idNaturezaProduto;
	}

	public void setIdNaturezaProduto(Long idNaturezaProduto) {
		this.idNaturezaProduto = idNaturezaProduto;
	}

	public String getDsNaturezaProduto() {
		return dsNaturezaProduto;
	}

	public void setDsNaturezaProduto(String dsNaturezaProduto) {
		this.dsNaturezaProduto = dsNaturezaProduto;
	}

	public String getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(String tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public Long getIdFilialOrigem() {
		return idFilialOrigem;
	}

	public void setIdFilialOrigem(Long idFilialOrigem) {
		this.idFilialOrigem = idFilialOrigem;
	}

	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}

	public void setSgFilialOrigem(String sgFilialOrigem) {
		this.sgFilialOrigem = sgFilialOrigem;
	}

	public Long getIdMunicipioOrigem() {
		return idMunicipioOrigem;
	}

	public void setIdMunicipioOrigem(Long idMunicipioOrigem) {
		this.idMunicipioOrigem = idMunicipioOrigem;
	}

	public String getNmMunicipioOrigem() {
		return nmMunicipioOrigem;
	}

	public void setNmMunicipioOrigem(String nmMunicipioOrigem) {
		this.nmMunicipioOrigem = nmMunicipioOrigem;
	}

	public Long getIdUnidadeFederativaOrigem() {
		return idUnidadeFederativaOrigem;
	}

	public void setIdUnidadeFederativaOrigem(Long idUnidadeFederativaOrigem) {
		this.idUnidadeFederativaOrigem = idUnidadeFederativaOrigem;
	}

	public String getSgUnidadeFederativaOrigem() {
		return sgUnidadeFederativaOrigem;
	}

	public void setSgUnidadeFederativaOrigem(String sgUnidadeFederativaOrigem) {
		this.sgUnidadeFederativaOrigem = sgUnidadeFederativaOrigem;
	}

	public Long getIdPaisOrigem() {
		return idPaisOrigem;
	}

	public void setIdPaisOrigem(Long idPaisOrigem) {
		this.idPaisOrigem = idPaisOrigem;
	}

	public String getNmPaisOrigem() {
		return nmPaisOrigem;
	}

	public void setNmPaisOrigem(String nmPaisOrigem) {
		this.nmPaisOrigem = nmPaisOrigem;
	}

	public Long getIdFilialDestino() {
		return idFilialDestino;
	}

	public void setIdFilialDestino(Long idFilialDestino) {
		this.idFilialDestino = idFilialDestino;
	}

	public String getSgFilialDestino() {
		return sgFilialDestino;
	}

	public void setSgFilialDestino(String sgFilialDestino) {
		this.sgFilialDestino = sgFilialDestino;
	}

	public Long getIdMunicipioDestino() {
		return idMunicipioDestino;
	}

	public void setIdMunicipioDestino(Long idMunicipioDestino) {
		this.idMunicipioDestino = idMunicipioDestino;
	}

	public String getNmMunicipioDestino() {
		return nmMunicipioDestino;
	}

	public void setNmMunicipioDestino(String nmMunicipioDestino) {
		this.nmMunicipioDestino = nmMunicipioDestino;
	}

	public Long getIdUnidadeFederativaDestino() {
		return idUnidadeFederativaDestino;
	}

	public void setIdUnidadeFederativaDestino(Long idUnidadeFederativaDestino) {
		this.idUnidadeFederativaDestino = idUnidadeFederativaDestino;
	}

	public String getSgUnidadeFederativaDestino() {
		return sgUnidadeFederativaDestino;
	}

	public void setSgUnidadeFederativaDestino(String sgUnidadeFederativaDestino) {
		this.sgUnidadeFederativaDestino = sgUnidadeFederativaDestino;
	}

	public Long getIdPaisDestino() {
		return idPaisDestino;
	}

	public void setIdPaisDestino(Long idPaisDestino) {
		this.idPaisDestino = idPaisDestino;
	}

	public String getNmPaisDestino() {
		return nmPaisDestino;
	}

	public void setNmPaisDestino(String nmPaisDestino) {
		this.nmPaisDestino = nmPaisDestino;
	}

	public String getTpPedidoColeta() {
		return tpPedidoColeta;
	}

	public void setTpPedidoColeta(String tpPedidoColeta) {
		this.tpPedidoColeta = tpPedidoColeta;
	}

	public Long getIdAwb() {
		return idAwb;
	}

	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("vlMercadoria", vlMercadoria)
				.append("tpOperacao", tpOperacao)
				.append("idMoeda", idMoeda)
				.append("idPais", idPais)
				.append("idClienteRemetente", idClienteRemetente)
				.append("nrIdentificacaoRemetente", nrIdentificacaoRemetente)
				.append("nmPessoaRemetente", nmPessoaRemetente)
				.append("idClienteDestinatario", idClienteDestinatario)
				.append("nrIdentificacaoDestinatario", nrIdentificacaoDestinatario)
				.append("nmPessoaDestinatario", nmPessoaDestinatario)
				.append("idNaturezaProduto", idNaturezaProduto)
				.append("dsNaturezaProduto", dsNaturezaProduto)
				.append("tpAbrangencia", tpAbrangencia)
				.append("idFilialOrigem", idFilialOrigem)
				.append("sgFilialOrigem", sgFilialOrigem)
				.append("idMunicipioOrigem", idMunicipioOrigem)
				.append("nmMunicipioOrigem", nmMunicipioOrigem)
				.append("idUnidadeFederativaOrigem", idUnidadeFederativaOrigem)
				.append("sgUnidadeFederativaOrigem", sgUnidadeFederativaOrigem)
				.append("idPaisOrigem", idPaisOrigem)
				.append("nmPaisOrigem", nmPaisOrigem)
				.append("idFilialDestino", idFilialDestino)
				.append("sgFilialDestino", sgFilialDestino)
				.append("idMunicipioDestino", idMunicipioDestino)
				.append("nmMunicipioDestino", nmMunicipioDestino)
				.append("idUnidadeFederativaDestino", idUnidadeFederativaDestino)
				.append("sgUnidadeFederativaDestino", sgUnidadeFederativaDestino)
				.append("idPaisDestino", idPaisDestino)
				.append("nmPaisDestino", nmPaisDestino)
				.append("tpPedidoColeta", tpPedidoColeta)
				.append("idAwb", idAwb)
				.toString();
	}

}
