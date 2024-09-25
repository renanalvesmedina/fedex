package com.mercurio.lms.test.expedicao.utils;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.expedicao.util.EnderecoPessoaUtils;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class EnderecoPessoaUtilsTest {
	
	private EnderecoPessoa createEnderecoPessoa(String tipoUso) {
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		Pessoa pessoa = new Pessoa();
		pessoa.setNmFantasia("Nome Fantasia");
		TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
		telefoneEndereco.setTpUso(new DomainValue(tipoUso));
		telefoneEndereco.setNrDdd("051");
		telefoneEndereco.setNrDdi("044");
		telefoneEndereco.setNrTelefone("33333333");
		pessoa.setTelefoneEnderecos(Arrays.asList(telefoneEndereco));
		enderecoPessoa.setPessoa(pessoa);
		TipoLogradouro tipoLogradouro = new TipoLogradouro();
		tipoLogradouro.setDsTipoLogradouro(new VarcharI18n("tipo logradouro"));
		enderecoPessoa.setTipoLogradouro(tipoLogradouro);
		enderecoPessoa.setDsEndereco("endereco");
		enderecoPessoa.setNrEndereco("500");
		enderecoPessoa.setDsComplemento("apto 1301");
		enderecoPessoa.setNrCep("93847476");
		Municipio municipio = new Municipio();
		municipio.setNmMunicipio("Nome municipio");
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		unidadeFederativa.setSgUnidadeFederativa("SIGLA");
		Pais pais = new Pais();
		pais.setSgPais("BR");
		unidadeFederativa.setPais(pais);
		municipio.setUnidadeFederativa(unidadeFederativa);
		enderecoPessoa.setMunicipio(municipio);
		return enderecoPessoa;
	}
	
	@Test
	public void testMountEnderecoFilialTipoUsoFF(){
		EnderecoPessoa enderecoPessoa = createEnderecoPessoa("FF");
		
		Assert.assertEquals(EnderecoPessoaUtils.mountEnderecoFilial(enderecoPessoa), "Nome Fantasia: tipo logradouro endereco, 500 - apto 1301 CEP 93847476 - Nome municipio - SIGLA - Telefone +044 (051) 3333-3333 - Fax +044 (051) 3333-3333");
	}
	
	@Test
	public void testMountEnderecoFilialTipoUsoFA(){
		EnderecoPessoa enderecoPessoa = createEnderecoPessoa("FA");
		
		Assert.assertEquals(EnderecoPessoaUtils.mountEnderecoFilial(enderecoPessoa), "Nome Fantasia: tipo logradouro endereco, 500 - apto 1301 CEP 93847476 - Nome municipio - SIGLA - Fax +044 (051) 3333-3333");
	}
	
	@Test
	public void testMountEnderecoFilialTipoUsoFO(){
		EnderecoPessoa enderecoPessoa = createEnderecoPessoa("FO");
		
		Assert.assertEquals(EnderecoPessoaUtils.mountEnderecoFilial(enderecoPessoa), "Nome Fantasia: tipo logradouro endereco, 500 - apto 1301 CEP 93847476 - Nome municipio - SIGLA - Telefone +044 (051) 3333-3333");
	}


}
