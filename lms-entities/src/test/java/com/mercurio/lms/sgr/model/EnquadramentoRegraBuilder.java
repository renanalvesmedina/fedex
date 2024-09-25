package com.mercurio.lms.sgr.model;

import java.util.ArrayList;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.MoedaBuilder;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

public class EnquadramentoRegraBuilder {

	private static final DomainValue TP_ABRANGENCIA_NACIONAL = new DomainValue(ConstantesGerRisco.TP_ABRANGENCIA_NACIONAL);
	private static final DomainValue TP_ABRANGENCIA_INTERNACIONAL = new DomainValue(ConstantesGerRisco.TP_ABRANGENCIA_INTERNACIONAL);

	private static final DomainValue TP_OPERACAO_COLETA = new DomainValue(ConstantesGerRisco.TP_OPERACAO_COLETA);
	private static final DomainValue TP_OPERACAO_ENTREGA = new DomainValue(ConstantesGerRisco.TP_OPERACAO_ENTREGA);
	private static final DomainValue TP_OPERACAO_VIAGEM = new DomainValue(ConstantesGerRisco.TP_OPERACAO_VIAGEM);

	private static final DomainValue TP_CRITERIO_FILIAL = new DomainValue(ConstantesGerRisco.TP_CRITERIO_FILIAL);
	private static final DomainValue TP_CRITERIO_MUNICIPIO = new DomainValue(ConstantesGerRisco.TP_CRITERIO_MUNICIPIO);
	private static final DomainValue TP_CRITERIO_UNIDADE_FEDERATIVA = new DomainValue(ConstantesGerRisco.TP_CRITERIO_UNIDADE_FEDERATIVA);
	private static final DomainValue TP_CRITERIO_PAIS = new DomainValue(ConstantesGerRisco.TP_CRITERIO_PAIS);

	private static final DomainValue TP_INTEGRANTE_FRETE_REMETENTE = new DomainValue(ConstantesGerRisco.TP_INTEGRANTE_FRETE_REMETENTE);
	private static final DomainValue TP_INTEGRANTE_FRETE_DESTINATARIO = new DomainValue(ConstantesGerRisco.TP_INTEGRANTE_FRETE_DESTINATARIO);
	private static final DomainValue TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO = new DomainValue(ConstantesGerRisco.TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO);

	public static EnquadramentoRegraBuilder newEnquadramentoRegra() {
		return new EnquadramentoRegraBuilder();
	}

	private EnquadramentoRegra regra;

	private EnquadramentoRegraBuilder() {
		regra = new EnquadramentoRegra();
		regra.setMoeda(MoedaBuilder.BRL());
	}

	public EnquadramentoRegraBuilder id(long id) {
		regra.setIdEnquadramentoRegra(id);
		return this;
	}

	public EnquadramentoRegraBuilder descricao(String descricao) {
		regra.setDsEnquadramentoRegra(new VarcharI18n(descricao));
		return this;
	}

	public EnquadramentoRegraBuilder nacional() {
		regra.setTpAbrangencia(TP_ABRANGENCIA_NACIONAL);
		return this;
	}

	public EnquadramentoRegraBuilder internacional() {
		regra.setTpAbrangencia(TP_ABRANGENCIA_INTERNACIONAL);
		return this;
	}

	public EnquadramentoRegraBuilder coleta() {
		regra.setTpOperacao(TP_OPERACAO_COLETA);
		return this;
	}

	public EnquadramentoRegraBuilder entrega() {
		regra.setTpOperacao(TP_OPERACAO_ENTREGA);
		return this;
	}

	public EnquadramentoRegraBuilder viagem() {
		regra.setTpOperacao(TP_OPERACAO_VIAGEM);
		return this;
	}

	public EnquadramentoRegraBuilder naturezaProduto(NaturezaProduto naturezaProduto) {
		regra.setNaturezaProduto(naturezaProduto);
		return this;
	}

	public EnquadramentoRegraBuilder filialOrigem(Filial filial) {
		criterioOrigem(TP_CRITERIO_FILIAL);
		if (regra.getFilialEnquadramentosOrigem() == null) {
			regra.setFilialEnquadramentosOrigem(new ArrayList<Filial>());
		}
		regra.getFilialEnquadramentosOrigem().add(filial);
		return this;
	}

	public EnquadramentoRegraBuilder municipioOrigem(Municipio municipio) {
		criterioOrigem(TP_CRITERIO_MUNICIPIO);
		if (regra.getMunicipioEnquadramentosOrigem() == null) {
			regra.setMunicipioEnquadramentosOrigem(new ArrayList<Municipio>());
		}
		regra.getMunicipioEnquadramentosOrigem().add(municipio);
		return this;
	}

	public EnquadramentoRegraBuilder unidadeFederativaOrigem(UnidadeFederativa uf) {
		criterioOrigem(TP_CRITERIO_UNIDADE_FEDERATIVA);
		if (regra.getUnidadeFederativaEnquadramentosOrigem() == null) {
			regra.setUnidadeFederativaEnquadramentosOrigem(new ArrayList<UnidadeFederativa>());
		}
		regra.getUnidadeFederativaEnquadramentosOrigem().add(uf);
		return this;
	}

	public EnquadramentoRegraBuilder paisOrigem(Pais pais) {
		criterioOrigem(TP_CRITERIO_PAIS);
		if (regra.getPaisEnquadramentosOrigem() == null) {
			regra.setPaisEnquadramentosOrigem(new ArrayList<Pais>());
		}
		regra.getPaisEnquadramentosOrigem().add(pais);
		return this;
	}

	private void criterioOrigem(DomainValue tpCriterio) {
		DomainValue tpCriterio2 = regra.getTpCriterioOrigem();
		if (!tpCriterio.equals(tpCriterio2) && tpCriterio2 != null) {
			throw new IllegalStateException("Critério de origem já definido (" + tpCriterio2 + ").");
		}
		regra.setTpCriterioOrigem(tpCriterio);
	}

	public EnquadramentoRegraBuilder filialDestino(Filial filial) {
		criterioDestino(TP_CRITERIO_FILIAL);
		if (regra.getFilialEnquadramentosDestino() == null) {
			regra.setFilialEnquadramentosDestino(new ArrayList<Filial>());
		}
		regra.getFilialEnquadramentosDestino().add(filial);
		return this;
	}

	public EnquadramentoRegraBuilder municipioDestino(Municipio municipio) {
		criterioDestino(TP_CRITERIO_MUNICIPIO);
		if (regra.getMunicipioEnquadramentosDestino() == null) {
			regra.setMunicipioEnquadramentosDestino(new ArrayList<Municipio>());
		}
		regra.getMunicipioEnquadramentosDestino().add(municipio);
		return this;
	}

	public EnquadramentoRegraBuilder unidadeFederativaDestino(UnidadeFederativa uf) {
		criterioDestino(TP_CRITERIO_UNIDADE_FEDERATIVA);
		if (regra.getUnidadeFederativaEnquadramentosDestino() == null) {
			regra.setUnidadeFederativaEnquadramentosDestino(new ArrayList<UnidadeFederativa>());
		}
		regra.getUnidadeFederativaEnquadramentosDestino().add(uf);
		return this;
	}

	public EnquadramentoRegraBuilder paisDestino(Pais pais) {
		criterioDestino(TP_CRITERIO_PAIS);
		if (regra.getPaisEnquadramentosDestino() == null) {
			regra.setPaisEnquadramentosDestino(new ArrayList<Pais>());
		}
		regra.getPaisEnquadramentosDestino().add(pais);
		return this;
	}

	private void criterioDestino(DomainValue tpCriterio) {
		DomainValue tpCriterio2 = regra.getTpCriterioDestino();
		if (!tpCriterio.equals(tpCriterio2) && tpCriterio2 != null) {
			throw new IllegalStateException("Critério de destino já definido (" + tpCriterio2 + ").");
		}
		regra.setTpCriterioDestino(tpCriterio);
	}

	public EnquadramentoRegraBuilder clienteRemetente(Cliente cliente) {
		return clienteEnquadramento(cliente, TP_INTEGRANTE_FRETE_REMETENTE);
	}

	public EnquadramentoRegraBuilder clienteDestinatario(Cliente cliente) {
		return clienteEnquadramento(cliente, TP_INTEGRANTE_FRETE_DESTINATARIO);
	}

	public EnquadramentoRegraBuilder clienteRemetenteDestinatario(Cliente cliente) {
		return clienteEnquadramento(cliente, TP_INTEGRANTE_FRETE_REMETENTE_DESTINATARIO);
	}

	private EnquadramentoRegraBuilder clienteEnquadramento(Cliente cliente, DomainValue tpIntegranteFrete) {
		if (regra.getClienteEnquadramentos() == null) {
			regra.setClienteEnquadramentos(new ArrayList<ClienteEnquadramento>());
		} else {
			for (ClienteEnquadramento enquadramento : regra.getClienteEnquadramentos()) {
				if (cliente.equals(enquadramento.getCliente())) {
					throw new IllegalStateException("Cliente já incluído na regra de enquadramento.");
				}
			}
		}
		ClienteEnquadramento enquadramento = new ClienteEnquadramento();
		enquadramento.setCliente(cliente);
		enquadramento.setTpIntegranteFrete(tpIntegranteFrete);
		regra.getClienteEnquadramentos().add(enquadramento);
		return this;
	}

	public EnquadramentoRegraBuilder faixa(FaixaDeValor faixa) {
		if (regra.getFaixaDeValors() == null) {
			regra.setFaixaDeValors(new ArrayList<FaixaDeValor>());
		} else {
			// TODO
		}
		regra.getFaixaDeValors().add(faixa);
		return this;
	}

	public EnquadramentoRegraBuilder regraGeral() {
		regra.setBlRegraGeral(true);
		return this;
	}

	public EnquadramentoRegraBuilder apoliceSeguro(ApoliceSeguro apoliceSeguro) {
		if (regra.getSeguroCliente() != null) {
			throw new IllegalStateException("Regra de enquadramento já possui seguro do cliente.");
		}
		regra.setApoliceSeguro(apoliceSeguro);
		return this;
	}

	public EnquadramentoRegraBuilder seguroCliente(SeguroCliente seguroCliente) {
		if (regra.getApoliceSeguro() != null) {
			throw new IllegalStateException("Regra de enquadramento já possui apólice de seguro.");
		}
		regra.setSeguroCliente(seguroCliente);
		return this;
	}

	public EnquadramentoRegra build() {
		return regra;
	}

	public static EnquadramentoRegra REGRA_1() {
		return newEnquadramentoRegra()
				.id(1)
				.build();
	}

	public static EnquadramentoRegra REGRA_2() {
		return newEnquadramentoRegra()
				.id(2)
				.build();
	}

	public static EnquadramentoRegra REGRA_GERAL() {
		return newEnquadramentoRegra()
				.id(3)
				.regraGeral()
				.build();
	}

}
