package com.mercurio.lms.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FormatUtilsTest {

	private static final String DNI = "DNI";
	private static final String RUT = "RUT";
	private static final String RUC = "RUC";
	private static final String CNPJ = "CNPJ";
	private static final String CPF = "CPF";
	
	@Test
	public void formatIdentificacaoWIthoutTipoIdentificacao(){
		String conteudo =  FormatUtils.formatIdentificacao("", "9834745");
		Assert.assertEquals(conteudo, "9834745");
	}
	
	@Test
	public void formatIdentificacaoShouldNotWorkWhenIdentificacaoIsNull() {
		String cpf =  FormatUtils.formatIdentificacao("", null);
		
		Assert.assertEquals(cpf, null);
	}

	@Test
	public void formatIdentificacaoShouldFormatCPF() {
		String cpf =  FormatUtils.formatIdentificacao(CPF, "25715705444");
		
		Assert.assertEquals(cpf, "257.157.054-44");
	}
	
	@Test
	public void formatIdentificacaoShouldNotFormatCPFWhenLenghtIsWrong() {
		String cpf =  FormatUtils.formatIdentificacao(CPF, "123456");
		
		Assert.assertEquals(cpf, "123456");
	}
	
	@Test
	public void  formatIdentificacaoShouldFormatCNPJ() {
		String cnpj =  FormatUtils.formatIdentificacao(CNPJ, "79495731000105");
		
		Assert.assertEquals(cnpj, "79.495.731/0001-05");
	}
	
	@Test
	public void  formatIdentificacaoShouldNotFormatCNPJWhenLenghtIsWrong() {
		String cnpj =  FormatUtils.formatIdentificacao(CNPJ, "456123421");
		
		Assert.assertEquals(cnpj, "456123421");
	}
	
	@Test
	public void  formatIdentificacaoShouldFormatRUC() {
		String ruc =  FormatUtils.formatIdentificacao(RUC, "212307530015");
		
		Assert.assertEquals(ruc, "21-230753-0015");
	}
	
	@Test
	public void  formatIdentificacaoShouldNotFormatRUCWhenLenghtIsWrong() {
		String ruc =  FormatUtils.formatIdentificacao(RUC, "212307530");
		
		Assert.assertEquals(ruc, "212307530");
	}
	
	@Test
	public void  formatIdentificacaoShouldFormatRUT() {
		String rut =  FormatUtils.formatIdentificacao(RUT, "590991406");
		
		Assert.assertEquals(rut, "59.099.140-6");
	}
	
	@Test
	public void  formatIdentificacaoShouldNotFormatRUTWhenLenghtIsWrong() {
		String rut =  FormatUtils.formatIdentificacao(RUT, "590991406999");
		
		Assert.assertEquals(rut, "590991406999");
	}	
	
	@Test
	public void  formatIdentificacaoShouldFormatDNI() {
		String dni =  FormatUtils.formatIdentificacao(DNI, "26223237");
		
		Assert.assertEquals(dni, "26.223.237");
	}
	
	@Test
	public void  formatIdentificacaoShouldNotFormatDNIWhenLenghtIsWrong() {
		String dni =  FormatUtils.formatIdentificacao(DNI, "262232379999");
		
		Assert.assertEquals(dni, "262232379999");
	}
	
	@Test
	public void  formatIdentificacaoShouldFormatCUIT() {
		String cuit =  FormatUtils.formatIdentificacao("CUIT", "30680560109");
		
		Assert.assertEquals(cuit, "30-68056010-9");
	}
	
	@Test
	public void  formatIdentificacaoShouldNotFormatCUITWhenLenghtIsWrong() {
		String cuit =  FormatUtils.formatIdentificacao("CUIT", "15");
		
		Assert.assertEquals(cuit, "15");
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void formatDecimalShouldThrowExceptionWhenMaskIsNull() {
		FormatUtils.formatDecimal(null, null);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void formatPesoShouldThrowExceptionWhenNumberIsNullAndFlagIsFalse() {
		FormatUtils.formatPeso(null, false);
	}

	@Test
	public void formatTelefone() {
		String string = FormatUtils.formatTelefone("81070329");
		
		Assert.assertEquals(string, "8107-0329");
	}

	@Test
	public void formatTelefoneWithDDD() {
		String string = FormatUtils.formatTelefoneWithDdd("5181070329");
		
		Assert.assertEquals(string, "(51) 8107-0329");
	}
	
	@Test
	public void formatAndConcatBySgFilialAndNrConhecimento(){
		String formated = FormatUtils.formatAndConcatBySgFilialAndNrConhecimento("FLN", 855L);
		
		Assert.assertEquals(formated, "FLN - 00000855");
	}
	
	@Test
	public void formatCepWithArgentina(){
		String cep = FormatUtils.formatCep("AR", "90540080");
		
		Assert.assertEquals(cep, "90540080");
	}
	
	@Test
	public void formatCepWithBrasil(){
		String cep = FormatUtils.formatCep("BRA", "90540080");
		
		Assert.assertEquals(cep, "90540-080");
	}
	
	@Test
	public void formatCepWithBrasilAndWrongCEP(){
		String cep = FormatUtils.formatCep("BRA", "905480");
		
		Assert.assertEquals(cep, "");
	}
	
	@Test
	public void formatEnderecoPessoaWithSampleData(){
		String endereco = FormatUtils.formatEnderecoPessoa("Av", "Sertório", "300", null);
		
		Assert.assertEquals(endereco, "Av Sertório, 300");
	}
	
	@Test
	public void formatEnderecoPessoaWithAllFields(){
		String endereco = FormatUtils.formatEnderecoPessoa("Av", "Sertório", "300", "cj 302", "Centro","Porto Alegre","RS");
		
		Assert.assertEquals(endereco, "Av Sertório, 300 - cj 302, Centro - Porto Alegre - RS");
	}
	
}
