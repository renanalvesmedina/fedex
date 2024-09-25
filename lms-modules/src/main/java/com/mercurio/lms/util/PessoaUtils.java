package com.mercurio.lms.util;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Municipio;

public abstract class PessoaUtils {

	/**
	 * Monta TypedFlatMap do Endereco Pessoa
	 * @param enderecoPessoa
	 * @return
	 */
	public static TypedFlatMap getEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		if (enderecoPessoa == null) {
			return null;
		}

		TypedFlatMap endereco = new TypedFlatMap();
		endereco.put("dsTipoLogradouro", enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().getValue());
		endereco.put("dsEndereco", enderecoPessoa.getDsEndereco());
		endereco.put("nrEndereco", enderecoPessoa.getNrEndereco());
		endereco.put("dsComplemento", enderecoPessoa.getDsComplemento());
		Municipio municipio = enderecoPessoa.getMunicipio();
		endereco.put("idMunicipio", municipio.getIdMunicipio());
		endereco.put("nmMunicipio", municipio.getNmMunicipio());
		endereco.put("idUnidadeFederativa", municipio.getUnidadeFederativa().getIdUnidadeFederativa());
		endereco.put("sgUnidadeFederativa", municipio.getUnidadeFederativa().getSgUnidadeFederativa());
		endereco.put("nrCep", enderecoPessoa.getNrCep());
		return endereco;
	}

	/**
	 * Valida identificação de pessoa
	 * @param nrIdentificacao
	 * @return
	 * @throws BusinessException("LMS-00049") Se nrIdentificacao for vazio
	 */
	public static String validateIdentificacao(String nrIdentificacao) {
		String result = clearIdentificacao(nrIdentificacao);
		if(StringUtils.isBlank(result)
			|| (result.endsWith("%") && (result.length() < 6))
			|| (result.length() < 5)
		) {
			throw new BusinessException("LMS-00049");
		}
		return result;
	}

	/**
	 * Limpa identificacao deixando apenas numeros, letras e o caracter '%'
	 * @param nrIdentificacao
	 * @return
	 */
	public static String clearIdentificacao(String nrIdentificacao) {
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			return nrIdentificacao.replaceAll("[^a-zA-Z0-9%]", "");
		}
		return "";
	}

	public static boolean isPessoaJuridica(Pessoa pessoa) {
		return isPessoaJuridica(pessoa.getTpPessoa().getValue());
	}
	public static boolean isPessoaJuridica(String tpPessoa) {
		return "J".equals(tpPessoa);
	}

	public static boolean isPessoaFisica(Pessoa pessoa) {
		return isPessoaFisica(pessoa.getTpPessoa().getValue());
	}
	public static boolean isPessoaFisica(String tpPessoa) {
		return "F".equals(tpPessoa);
	}
}
