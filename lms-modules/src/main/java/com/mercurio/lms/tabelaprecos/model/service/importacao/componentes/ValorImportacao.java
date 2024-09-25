package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;

public class ValorImportacao extends ItemImportacao {

	private String valor;
	private TagImportacao tagReferencia;

	public ValorImportacao(Integer linha, Integer coluna, String valor, TagImportacao tagReferencia) {
		super(linha, coluna);
		this.valor = valor;
		if (StringUtils.isNotBlank(this.valor)) {
			this.valor = this.valor.trim();
		}
		this.tagReferencia = tagReferencia;
	}


	public String valorString() {
		if(valor == null){
			return "";
		}
		return valor;
	}

	public String tag() {
		return this.tagReferencia.tag();
	}

	TagImportacao tagReferancia(){
		return this.tagReferencia;
	}


	public BigDecimal valorBigDecimal(){
		BigDecimal retorno = null;
		try {
			retorno = new BigDecimal(ConversorPontoFlutuante.converte(this.valor));
		} catch (NumberFormatException e) {
			throw new ImportacaoException(this.colunaItem, this.linhaItem, String.format("O valor '%s' não é um valor válido para a tag '%s'. Era esperado um número de ponto flutuante.", this.valor, this.tag()));
		}
		return retorno;
	}

	public BigDecimal valorBigDecimal(BigDecimal valorPadrao){
		BigDecimal retorno = valorPadrao;
		if (StringUtils.isBlank(this.valor)) {
			return retorno;
		}
		return this.valorBigDecimal();
	}

	public Long valorLong(){
		Pattern pattern = Pattern.compile("([\\d]+)([.,])?(0)?");
		Matcher matcher = pattern.matcher(valor);
		if(matcher.matches()){
			return Long.valueOf(matcher.group(1));
		}
		throw new ImportacaoException(this.colunaItem, this.linhaItem, String.format("O valor '%s' não é um valor válido para a tag '%s'. Era esperado um número inteiro.", this.valor, this.tag()));
	}

	public Short valorShort() {
		Pattern pattern = Pattern.compile("([\\d]+)([.,])?(0)?");
		Matcher matcher = pattern.matcher(valor);
		if(matcher.matches()){
			return Short.valueOf(matcher.group(1));
		}
		throw new ImportacaoException(this.colunaItem, this.linhaItem, String.format("O valor '%s' não é um valor válido para a tag '%s'. Era esperado um número inteiro.", this.valor, this.tag()));
	}

	public boolean preenchido() {
		return StringUtils.isNotBlank(this.valor);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!o.getClass().isAssignableFrom(ValorImportacao.class)) {
			return false;
		}
		ValorImportacao outroTag = (ValorImportacao) o;
		return new EqualsBuilder().append(this.valor, outroTag.valor).append(this.tagReferencia, outroTag.tagReferencia).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.valor).append(this.tagReferencia).toHashCode();
	}

	@Override
	public String toString() {
		return String.format("Linha: %3d; Coluna: %3d; Valor: %s; Tag: %s", this.linhaItem, this.colunaItem, this.valor, this.tagReferencia.tag());
	}


	public boolean mesmaTag(String tag) {
		return this.tagReferencia.igual(tag);
	}
	
	public static boolean estahInformado(ValorImportacao valor) {
		return valor != null && valor.preenchido();
	}

	public static String valorString(ValorImportacao valor) {
		if (valor == null) {
			return null;
		}
		return valor.valorString();
	}
	
	public static Long valorLong(ValorImportacao valor) {
		if (valor == null) {
			return null;
		}
		return valor.valorLong();
	}


	public static ValorImportacao novoNulo() {
		return new ValorImportacaoNulo();
	}
	
}

class ConversorPontoFlutuante {
	private static final Pattern PADRAO = Pattern.compile("(\\d*.\\d+)|(\\d*,\\d+)");
	
	public static String converte(String texto) {
		Matcher matcher = PADRAO.matcher(texto);
		if (!matcher.matches()) {
			return "#";
		}
		return texto.replaceAll("(\\d*)(,)(\\d+)", "$1.$3");
	}
}

class ValorImportacaoNulo extends ValorImportacao {
	
	public ValorImportacaoNulo() {
		super(null, null, null, null);
		
	}
	
	@Override
	public String tag() {
		return null;
	}

	@Override
	TagImportacao tagReferancia(){
		return null;
	}

	@Override
	public String valorString() {
		return null;
	}
	
	@Override
	public BigDecimal valorBigDecimal(){
		return null;
	}
	
	@Override
	public BigDecimal valorBigDecimal(BigDecimal valorPadrao){
		return valorPadrao;
	}
	
	@Override
	public Long valorLong() {
		return null;
	}
	
	@Override
	public Short valorShort() {
		return null;
	}
	
	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}
	
	@Override
	public boolean mesmaTag(String tag) {
		return false;
	}
	
	@Override
	public boolean preenchido() {
		return false;
	}
}
