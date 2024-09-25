package com.mercurio.lms.sgr.util;

import static org.codehaus.jackson.JsonGenerator.Feature.QUOTE_FIELD_NAMES;
import static org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS;
import static org.codehaus.jackson.map.SerializationConfig.Feature.INDENT_OUTPUT;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_EMPTY;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

/**
 * LMS-6850 - Classe utilitária para geração de JSON a partir de
 * {@link PlanoGerenciamentoRiscoDTO}. Define as interfaces e classes "MixIn"
 * para especificar as regras de conversão das entidades relacionadas e define
 * também um método estático para configurar e produzir o conteúdo JSON a partir
 * de instância de {@link PlanoGerenciamentoRiscoDTO}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 * @see MapperConfig#addMixInAnnotations(Class, Class)
 */
public final class EnquadramentoRegraObjectMapperUtil {

	private static final Logger LOGGER = LogManager.getLogger(EnquadramentoRegraObjectMapperUtil.class);

	/**
	 * Interface "MixIn" para {@link VarcharI18n}.
	 */
	private interface VarcharI18nMixIn {
		@JsonValue
		String getValue();
	}

	/**
	 * Interface "MixIn" para {@link YearMonthDay}.
	 */
	private interface YearMonthDayMixIn {
		@JsonValue
		String toString();
	}

	/**
	 * Interface "MixIn" para {@link DomainValue}.
	 */
	private interface DomainValueMixIn {
		@JsonValue
		String getValue();
	}

	/**
	 * Interface "MixIn" para {@link Moeda}.
	 */
	private interface MoedaMixIn {
		@JsonValue
		String getSgMoeda();
	}

	/**
	 * Interface "MixIn" para {@link ReguladoraSeguro}.
	 */
	private interface ReguladoraSeguroMixIn {
		@JsonValue
		Long getIdReguladora();
	}

	/**
	 * Interface "MixIn" para {@link NaturezaProduto}.
	 */
	private interface NaturezaProdutoMixIn {
		@JsonValue
		VarcharI18n getDsNaturezaProduto();
	}

	/**
	 * Interface "MixIn" para {@link Municipio}.
	 */
	private interface MunicipioMixIn {
		@JsonValue
		String getNmMunicipio();
	}

	/**
	 * Interface "MixIn" para {@link Pais}.
	 */
	private interface PaisMixIn {
		@JsonValue
		String getSgPais();
	}

	/**
	 * Interface "MixIn" para {@link UnidadeFederativa}.
	 */
	private interface UnidadeFederativaMixIn {
		@JsonValue
		String getSgUnidadeFederativa();
	}

	/**
	 * Interface "MixIn" para {@link Filial}.
	 */
	private interface FilialMixIn {
		@JsonValue
		String getSgFilial();
	}

	/**
	 * Interface "MixIn" para {@link Cliente}.
	 */
	private interface ClienteMixIn {
		@JsonValue
		Long getIdCliente();
	}

	/**
	 * Classe "MixIn" para {@link ApoliceSeguro}.
	 */
	private abstract class ApoliceSeguroMixIn {
		@JsonIgnore
		protected TipoSeguro tipoSeguro;
		@JsonIgnore
		protected Seguradora seguradora;
		@JsonIgnore
		protected Pessoa segurado;
	}

	/**
	 * Classe "MixIn" para {@link SeguroCliente}.
	 */
	private abstract class SeguroClienteMixIn {
		@JsonIgnore
		protected byte[] dcCartaIsencao;
		@JsonIgnore
		protected TipoSeguro tipoSeguro;
		@JsonIgnore
		private Municipio municipioOrigem;
		@JsonIgnore
		private Municipio municipioDestino;
		@JsonIgnore
		protected Seguradora seguradora;
		@JsonIgnore
		protected Usuario usuarioAviso;
	}

	/**
	 * Interface "MixIn" para {@link TipoSeguro}.
	 */
	private interface TipoSeguroMixIn {
		@JsonValue
		Long getIdTipoSeguro();
	}

	/**
	 * Interface "MixIn" para {@link Seguradora}.
	 */
	private interface SeguradoraMixIn {
		@JsonValue
		Long getIdSeguradora();
	}

	/**
	 * Interface "MixIn" para {@link Pessoa}.
	 */
	private interface PessoaMixIn {
		@JsonValue
		String getNmPessoa();
	}
	
	/**
	 * Método auxiliar para criação do {@link ObjectMapper}, configuração do
	 * {@link SerializationConfig} e geração de JSON formatado ou compacto a
	 * partir de instância de {@link PlanoGerenciamentoRiscoDTO}.
	 * 
	 * @param prettyPrint
	 *            Determina saída formatada ou compacta.
	 * @param plano
	 *            Instância para geração de JSON.
	 * @return {@link String} com conteúdo JSON.
	 */
	public static String writeAsString(boolean prettyPrint, PlanoGerenciamentoRiscoDTO plano) {
		ObjectMapper objectMapper = new ObjectMapper()
				.setSerializationInclusion(NON_NULL)
				.setSerializationInclusion(NON_EMPTY)
				.disable(FAIL_ON_EMPTY_BEANS)
				.configure(INDENT_OUTPUT, prettyPrint)
				.configure(QUOTE_FIELD_NAMES, !prettyPrint);

		SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
		serializationConfig.addMixInAnnotations(VarcharI18n.class, VarcharI18nMixIn.class);
		serializationConfig.addMixInAnnotations(YearMonthDay.class, YearMonthDayMixIn.class);
		serializationConfig.addMixInAnnotations(DomainValue.class, DomainValueMixIn.class);
		serializationConfig.addMixInAnnotations(Moeda.class, MoedaMixIn.class);
		serializationConfig.addMixInAnnotations(ReguladoraSeguro.class, ReguladoraSeguroMixIn.class);
		serializationConfig.addMixInAnnotations(NaturezaProduto.class, NaturezaProdutoMixIn.class);
		serializationConfig.addMixInAnnotations(Municipio.class, MunicipioMixIn.class);
		serializationConfig.addMixInAnnotations(Pais.class, PaisMixIn.class);
		serializationConfig.addMixInAnnotations(UnidadeFederativa.class, UnidadeFederativaMixIn.class);
		serializationConfig.addMixInAnnotations(Filial.class, FilialMixIn.class);
		serializationConfig.addMixInAnnotations(Cliente.class, ClienteMixIn.class);
		serializationConfig.addMixInAnnotations(ApoliceSeguro.class, ApoliceSeguroMixIn.class);
		serializationConfig.addMixInAnnotations(SeguroCliente.class, SeguroClienteMixIn.class);
		serializationConfig.addMixInAnnotations(TipoSeguro.class, TipoSeguroMixIn.class);
		serializationConfig.addMixInAnnotations(Seguradora.class, SeguradoraMixIn.class);
		serializationConfig.addMixInAnnotations(Pessoa.class, PessoaMixIn.class);

		try {
			return objectMapper.writeValueAsString(plano);
		} catch (JsonGenerationException e) {
			LOGGER.error("", e);
		} catch (JsonMappingException e) {
			LOGGER.error("", e);
		} catch (IOException e) {
			LOGGER.error("", e);
		}
		return null;
	}

	private EnquadramentoRegraObjectMapperUtil() {
		throw new AssertionError();
	}

}
