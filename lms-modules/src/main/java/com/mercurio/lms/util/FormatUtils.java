package com.mercurio.lms.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * Classe para funções utilitárias de formatação de dados.
 * 
 * @author Felipe Ferreira
 * 
 */
public class FormatUtils {

	private static final String DEFAULT_DECIMAL_FORMAT = "00000000";
	/** Atributo estático utilizada no método FormatUtils.completaDados para indicar a direção que se deseja que os dados sejam completados */
	public static final boolean DIREITA  = false;
	/** Atributo estático utilizada no método FormatUtils.completaDados para indicar a direção que se deseja que os dados sejam completados */
	public static final boolean ESQUERDA = true;

	// Determinam escala de horas nas funções que formatam tempo:
	public static final int ESCALA_HH = 0;
	public static final int ESCALA_HHH = 1;

	/**
     * Tipos de dados suportados pela arquitetura. Novos topos podem ser
     * adicionados conforme necessidade.
     * 
     * @see {@link #isNumeric(int)} {@link #getDefaultMaskByDatatype(int)}
     *      {@link #validateKeyPress(char, int)}
     *      {@link com.mercurio.adsm.swt.taglibs.AdsmTextbox#getValue()}
     *      {@link com.mercurio.adsm.swt.taglibs.AdsmTextbox#isValidElement()}
     */
	public static final int DATATYPE_UNDEFINED = 0;
	public static final int DATATYPE_TEXT = 1;
	public static final int DATATYPE_LONG = 2;
	public static final int DATATYPE_DECIMAL = 3;
	public static final int DATATYPE_JTDATE = 4; 
	public static final int DATATYPE_JTTIME = 5;
	public static final int DATATYPE_JTDATETIMEZONE = 6;
	public static final int DATATYPE_WEIGHT = 7;
	public static final int DATATYPE_CURRENCY = 8;
	public static final int DATATYPE_INTEGER = 9;
	public static final int DATATYPE_CUIT = 10;
	public static final int DATATYPE_RUC = 11;
	public static final int DATATYPE_DNI = 12;
	public static final int DATATYPE_RUT = 13;
	public static final int DATATYPE_CPF = 14;
	public static final int DATATYPE_CNPJ = 15;
	public static final int DATATYPE_PASSWORD = 16;
    public static final int DATATYPE_SHORT = 17;
    public static final int DATATYPE_PERCENT = 18;
    public static final int DATATYPE_NETWORK = 19;
    public static final int DATATYPE_MACADDRESS = 20;
	
	/**
	 * Recebe pessoa e retorna seu número de identificação formatado.
	 * @author Moacir Zardo Junior
	 * @param pessoa
	 * @return
	 */
	public static String formatIdentificacao(Pessoa pessoa){
		if (pessoa != null){
			DomainValue tpIdentificacao = pessoa.getTpIdentificacao();
			String nrIdentificacao = pessoa.getNrIdentificacao();
			return formatIdentificacao(tpIdentificacao, nrIdentificacao);
		}
		return "";
	}

	/**
	 * Recebe identificação e retorna seu número formatado de acordo com seu tipo.
	 * 
	 * @param tpIdentificacao
	 * @param conteudo
	 * @return
	 */
	public static String formatIdentificacao(String tpIdentificacao, String conteudo){
		//Se não existir tipo de identificação, não faz nenhuma formatação
		if (StringUtils.isBlank(tpIdentificacao)) {
			return conteudo;
		}

		if (conteudo != null && !"".equals(conteudo)) {
			//Se tipo de identificação for CNPJ
			if (tpIdentificacao.equals("CNPJ") && conteudo.length() == 14) {
				return formatCNPJ(conteudo);
			}
			//Se tipo de identificação for CPF
			if (tpIdentificacao.equals("CPF") && conteudo.length() == 11){
				return formatCPF(conteudo);
			}
			//Se tipo de identificação for DNI
			if (tpIdentificacao.equals("DNI") && conteudo.length() == 8){
				return formatDNI(conteudo);
			}
			//Se tipo de identificação for CUIT
			if (tpIdentificacao.equals("CUIT") && conteudo.length() == 11) {
				return formatCUIT(conteudo);
			}
			//Se tipo de identificação for RUT
			if ("RUT".equals(tpIdentificacao)) {
				return formatRUT(conteudo);
			}
			//Se tipo de identificação for RUC
			if ("RUC".equals(tpIdentificacao)) {
				return formatRUC(conteudo);
			}
		}

		return conteudo;
	}
	
	/**
	 * Recebe identificação e retorna seu número formatado de acordo com seu tipo, concatenado com o tipo.
	 * 
	 * @param tpIdentificacao
	 * @param conteudo
	 * @return
	 */
	public static String formatIdentificacaoComTipo(String tpIdentificacao, String conteudo) {
		String identificacaoFormatada = formatIdentificacao(tpIdentificacao, conteudo);
		return (tpIdentificacao == null || identificacaoFormatada == null) ? null : tpIdentificacao + " " + identificacaoFormatada;
	}

	/**
	 * Recebe identificação e retorna seu número formatado de acordo com seu tipo.
	 * 
	 * @param tpIdentificacao
	 * @param conteudo
	 * @return
	 */
	public static String formatIdentificacao(DomainValue tpIdentificacao, String conteudo){
		if (tpIdentificacao != null) {
			String value = tpIdentificacao.getValue();
			if (value != null) {
				return formatIdentificacao(value,conteudo);
			}
		}
		return conteudo;
	}

	/**
	 * Recebe identificação e retorna seu número formatado de acordo com seu tipo.
	 * 
	 * @param pessoa pessoa contendo um mapa tpIdentificacao
	 * @return
	 */
	public static String formatIdentificacao(Map<String, Object> pessoa) {
		Map<String, Object> tpIdentificacao = (Map<String, Object>) pessoa.get("tpIdentificacao");
		String conteudo = (String) pessoa.get("nrIdentificacao");
		if (tpIdentificacao != null) {
			return formatIdentificacao((String) tpIdentificacao.get("value"), conteudo);
		}
		return conteudo;
	}

	/**
	 * Filtra apenas os números e o caracter '%' de uma string.
	 * @param conteudo
	 * @return
	 */
	public static String filterNumber(String conteudo) {
		if(StringUtils.isNotBlank(conteudo)) {
			return conteudo.replaceAll("[\\D&&[^%]]", "");
		}
		return "";
	}

	/**
	 * Método responsável por concatenar a sigla da filial com um hífen, e com o número do conhecimento(com máscara)
	 * Ex:
	 *  argumentos:
	 * 	  sgFilial = "POA"
	 *    nrConhecimento = 20
	 * 	retorno:
	 * 	  "POA - 00000020"
	 * 
	 * @param sgFilial
	 * @param nrConhecimento
	 * @param nrZerosToMask
	 * @return String contendo a sigla da filial concatenada com o número do conhecimento(com máscara)
	 */
   public static String formatAndConcatBySgFilialAndNrConhecimento(String sgFilial, Long nrConhecimento){
		String format = String.valueOf(nrConhecimento.toString());
		for(int i = nrConhecimento.toString().length(); i < 8; i++){
			format = ("0" + format);
		}
		return (sgFilial + " - " + format);
	}

	/**
	 * Recebe o cep e retorna formatado caso pais seja Brasil.
	 * 
	 * @param tpIdentificacao
	 * @param conteudo
	 * @return
	 */
	public static String formatCep(String siglaPais, String cep){
		String retorno="";
		if (cep != null && cep != "") {
			if (siglaPais != null && siglaPais.equalsIgnoreCase("BRA")) {
				if(cep.length()==8){//formata cep
					retorno += cep.substring(0,5) + "-" + cep.substring(5,8);
				}
			} else {
				retorno = cep;
			}
		}
		return retorno;
	}

	/**
	 * Formatação para RUC.<BR>
	 * Exemplo de formatação:<BR>
	 * <ul>
	 * 	<li>entrada: 212307530015</li>
	 * 	<li>saída: 21-230753-0015</li>
	 * </ul>
	 * <i>Formata apenas quando o tamanho da String for igual a 11.<i>  
	 * @author Anibal Maffioletti de Deus
	 * @param rut
	 * @return rut formatado
	 */
	public static String formatRUC(String ruc) {
		if (ruc != null && ruc.length() ==  12){
			StringBuffer format = new StringBuffer()
				.append(ruc.substring(0,2)).append("-")
				.append(ruc.substring(2,8)).append("-")
				.append(ruc.substring(8));
			return format.toString();	
		}

		return ruc;
	}

	/**
	 * Formatação de CPF.
	 *@author Robson Edemar Gehl
	 * @param cpf
	 * @return
	 */
	public static String formatCPF(String cpf) {

		if (cpf != null && cpf.length() > 8){
			StringBuffer format = new StringBuffer()
				.append(cpf.substring(0,3)).append(".")
				.append(cpf.substring(3,6)).append(".")
				.append(cpf.substring(6,9)).append("-")
				.append(cpf.substring(9));
			return format.toString();	
		}
		return cpf;
	}

	/**
	 * Formatação de CNPJ.
	 * @author Robson Edemar Gehl
	 * @param cnpj
	 * @return String
	 */
	public static String formatCNPJ(String cnpj) {
		if(cnpj != null && cnpj.length() >= 12){
			StringBuffer format = new StringBuffer()
				.append(cnpj.substring(0,2)).append(".")
				.append(cnpj.substring(2,5)).append(".")
				.append(cnpj.substring(5,8)).append("/")
				.append(cnpj.substring(8,12)).append("-")
				.append(cnpj.substring(12));
			return format.toString();
		}
		return cnpj;
	}

	public static String formatCpfCnpj(String nrIdentificacao) {
		String retorno = null;
		if(StringUtils.isNotBlank(nrIdentificacao)){
			if(nrIdentificacao.length() < 12){
				retorno = formatCPF(nrIdentificacao);
			}else{
				retorno = formatCNPJ(nrIdentificacao);
			}
		}
		return retorno;
	}

	/**
	 * Formatação para CUIT.<BR>
	 * Exemplo de formatação:<BR>
	 * <ul>
	 * 	<li>entrada: 30680560109</li>
	 * 	<li>saída: 30-68056010-9</li>
	 * </ul>
	 * <i>Formata apenas quando o tamanho da String for igual a 12.<i>  
	 *@author Robson Edemar Gehl
	 * @param cuit
	 * @return cuit formatado
	 */
	public static String formatCUIT(String cuit) {
		StringBuffer format = new StringBuffer();
		if (cuit.length() == 11){
			format.append(cuit.substring(0,2)).append("-")
				.append(cuit.substring(2,10)).append("-")
				.append(cuit.substring(10));
			return format.toString();
		}
		return cuit;
	}

	/**
	 * Formatação para RUT.<BR>
	 * Exemplo de formatação:<BR>
	 * <ul>
	 * 	<li>entrada: 590991406</li>
	 * 	<li>saída: 59.099.140-6</li>
	 * </ul>
	 * <i>Formata apenas quando o tamanho da String for igual a 9.<i>  
	 *@author Robson Edemar Gehl
	 * @param rut
	 * @return rut formatado
	 */
	public static String formatRUT(String rut) {
		StringBuffer format = new StringBuffer();
		if (rut.length() == 9){
			format.append(rut.substring(0,2)).append(".")
				.append(rut.substring(2,5)).append(".")
				.append(rut.substring(5,8)).append("-")
				.append(rut.substring(8));
			return format.toString();
		}
		return rut;
	}

	/**
	 * Formatação para DNI.<BR>
	 * Exemplo de formatação:<BR>
	 * <ul>
	 * 	<li>entrada: 26223237</li>
	 * 	<li>saída: 26.223.237</li>
	 * </ul>
	 * <i>Formata apenas quando o tamanho da String for igual a 8.<i>  
	 *@author Mickaël Jalbert
	 * @param DNI
	 * @return DNI formatado
	 */
	public static String formatDNI(String dni) {
		StringBuffer format = new StringBuffer();
		if (dni.length() == 8){
			format.append(dni.substring(0,2)).append(".")
				.append(dni.substring(2,5)).append(".")
				.append(dni.substring(5));
			return format.toString();
		}
		return dni;
	}

	/**
	 * Formata o endereco da Pessoa no formato: Av. Sertório, 6500 - Expresso
	 * Mercúrio
	 * 
	 * @param dsTipoLogradouro Tipo do Logradouro (Av., Rua, etc...)
	 * @param dsEndereco Descrição do Endereço
	 * @param nrEndereco Número do Endereço
	 * @param dsComplemento Complemento
	 * @return String formatada do endereço da pessoa
	 */
	public static String formatEnderecoPessoa(String dsTipoLogradouro,
			String dsEndereco, String nrEndereco, String dsComplemento) {
		return formatEnderecoPessoa(dsTipoLogradouro, dsEndereco, nrEndereco,
				dsComplemento, null);
	}

	/**
	 * Formata o endereco da Pessoa no formato: Av. Sertório, 6500 - Expresso
	 * Mercúrio, Sarandi
	 * 
	 * @param dsTipoLogradouro
	 *            Tipo do Logradouro (Av., Rua, etc...)
	 * @param dsEndereco
	 *            Descrição do Endereço
	 * @param nrEndereco
	 *            Número do Endereço
	 * @param dsComplemento
	 *            Complemento
	 * @param dsBairro
	 *            Bairro
	 * @return String formatada do endereço da pessoa
	 */
	public static String formatEnderecoPessoa(String dsTipoLogradouro,
			String dsEndereco, String nrEndereco, String dsComplemento,
			String dsBairro) {
		return formatEnderecoPessoa(dsTipoLogradouro, dsEndereco, nrEndereco,
				dsComplemento, dsBairro, null, null);
	}
	
	/**
	 * Formata o endereco da pessoa no formato: Av, Sertório, 6500 - Expresso
	 * Mercurio, Sarandi - Porto Alegre - RS. <br/> Os campos
	 * <code>dsComplementoo</code>, <code>dsBairro</code>,
	 * <code>nmMunicipio</code> e <code>sgUnidadeFederativa</code> não são
	 * obrigatórios.
	 * 
	 * @param dsTipoLogradouro
	 *            Tipo do Logradouro (Av., Rua, etc...)
	 * @param dsEndereco
	 *            Descrição do Endereço
	 * @param nrEndereco
	 *            Número do Endereço
	 * @param dsComplemento
	 *            Complemento
	 * @param dsBairro
	 *            Bairro
	 * @param nmMunicipio
	 *            Nome do município
	 * @param sgUnidadeFederativa
	 *            Sigla da unidade federativa
	 * @return formatada do endereço da pessoa
	 */
	public static String formatEnderecoPessoa(String dsTipoLogradouro,
			String dsEndereco, String nrEndereco, String dsComplemento,
			String dsBairro, String nmMunicipio, String sgUnidadeFederativa) {
		
		StringBuilder endereco = new StringBuilder();
		endereco.append(dsTipoLogradouro);
		endereco.append(" ").append(dsEndereco);
		endereco.append(", ").append(nrEndereco);

		if (StringUtils.isNotBlank(dsComplemento)) {
			endereco.append(" - ").append(dsComplemento);
		}

		if (StringUtils.isNotBlank(dsBairro)) {
			endereco.append(", ").append(dsBairro);
		}
		
		if (StringUtils.isNotBlank(nmMunicipio)) {
			endereco.append(" - ").append(nmMunicipio);
		}
		
		if (StringUtils.isNotBlank(sgUnidadeFederativa)) {
			endereco.append(" - ").append(sgUnidadeFederativa);
		}

		return endereco.toString();
	}

	public static String formatDecimal(String mask, Number number,boolean retornaZeroSeNulo){
		if(retornaZeroSeNulo&&number == null) {
			return formatDecimal(mask, new BigDecimal("0.00"));
		}
		return formatDecimal(mask, number);
	}

	public static String formatPeso(Number number,boolean retornaZeroSeNulo){
		return formatDecimal("#,###0.000", number, retornaZeroSeNulo);
	}

	/**
	 * Formata o número passado 
	 * como parâmetro utilizando a máscara passada como parâmetro.
	 * autor Julio Cesar Fernandes Corrêa
	 * 10/01/2006
	 * @param mask Máscara
	 * @param number Número decimal a ser formatado.
	 * @return
	 */
	public static String formatDecimal(String mask, Number number) {
		if(StringUtils.isBlank(mask))
			throw new IllegalArgumentException("A máscara não pode ser nula ou vazia");
		NumberFormat format = new DecimalFormat(mask, new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
		return format.format(number);
	}

	public static String formatDecimalLocale(String mask, Number number) {
		if(StringUtils.isBlank(mask))
			throw new IllegalArgumentException("A máscara não pode ser nula ou vazia");
		NumberFormat format = new DecimalFormat(mask, new DecimalFormatSymbols(SessionUtils.getUsuarioLogado().getLocale()));
		return format.format(number);
	}

	/**
	 * Completa a String com o caracter informado para atingir o tamanho necessário, se a String for nula retorna uma String em branco
	 * do tamanho informado em <code>tamanhoFinal</code>
	 * @param principal Objeto a ser completado com o caracter informado
	 * @param caracter  Caracter que será repetido N vezes
	 * @param tamanhoFinal Tamanho do campo
	 * @param decimal Quantidade de casas decimais
	 * @param lado <code>true</code> Para preencher com o caracter passado por n vezes do lado esquerdo e \n<code>false</code> 
	 *        para preencher com o caracter passado por n vezes do lado direito
	 * @return String principal concatenada com o caracter passado replicado n vezes do lado informado
	 */
	public static String completaDados(Object principal, String caracter, int tamanhoFinal, int decimal, boolean lado){
		String retorno   = null;
		boolean esquerdo = true;
		int tamanhoDecimal = 0;

		if( principal == null ){
			return StringUtils.repeat(caracter, tamanhoFinal);
		} else {
			if( principal instanceof String ){
				retorno = (String) principal;
			} else {
				retorno = principal.toString();
			}
		}

		if( retorno.indexOf(".") != -1 ){
			tamanhoDecimal = retorno.substring(retorno.indexOf(".") + 1).length();
			retorno = retorno.substring(0, retorno.indexOf(".")) + retorno.substring(retorno.indexOf(".") + 1);
		} 

		if( lado == esquerdo ){
			if( decimal != 0 ){
				retorno = retorno + StringUtils.repeat(caracter, Math.abs(decimal - tamanhoDecimal));
			}
			retorno = StringUtils.repeat(caracter, tamanhoFinal - retorno.length() ) + retorno;
		} else {
			retorno = retorno + StringUtils.repeat(caracter, tamanhoFinal - retorno.length() );
		}
		return retorno;
	}

	/**
	 * Método criado para buscar um dado dentro de uma linha (string) lida de um arquivo.
	 * Se a linha for nula e/ou posicaoInicial e/ou tamanho forem 0 (zero) é retornado null</code>  
	 * Utilizado ao ler uma linha de um arquivo a ser importado
	 * @param linha Linha do arquivo
	 * @param posicaoInicial Posição inicial do dado
	 * @param tamanho Tamanho em caracteres do dado
	 * @param isValor Booleano que indica que o valor a ser lido é um valor (R$) ou qualquer outro valor com casas decimais
	 * @param numeroDecimais Número de casas decimais do dado (valor)
	 * @return Dado da posição informada
	 */
	public static String quebraLinha(String linha, int posicaoInicial, int tamanho, boolean isValor, int numeroDecimais){
		String retorno = null;

		if( linha == null || posicaoInicial == 0 || tamanho == 0){
			return null;
		}

		retorno = linha;
		if(isValor) {
			int posicaoDecimal = (posicaoInicial + (tamanho - (numeroDecimais+1)));			
			retorno = retorno.substring(posicaoInicial -1,posicaoDecimal) + "." + 
					  retorno.substring((posicaoInicial + (tamanho - 3)),(posicaoInicial + (tamanho - 1)));
		} else {
			retorno = retorno.substring(posicaoInicial - 1,(posicaoInicial + (tamanho - 1)));
		}
		return retorno;		
	}

	/**
	 * Formata o número do Documento de acordo com o seu tipo
	 * @param numero Número do documento de serviço
	 * @param tipo <code>CTR - para Conhecimento</code>, 
	 *             <code>CRT - para Cto Internacional</code>, 
	 *             <code>RNC - Registro de não conformidade</code>, 
	 *             <code>BDM - Baixa de Devolução de Mercadoria</code>, 
	 *             <code>DDE - Demostrativo de Desconto</code>, 
	 *             <code>RDE - Recibo de Desconto</code>, 
	 *             <code>TRA - Transferência</code>, 
	 *             <code>RCB - Relação de Cobrança</code>, 
	 *             <code>MVN - Manifesto Viagem Nacional</code>, 
	 *             <code>NFS - para Notas fiscais serviço</code>
	 *             <code>NFT - para Notas fiscais transporte</code>
	 *             <code>NDN - para Notas debito nacional</code>
	 *             <code>FAT - para Faturas</code>
	 *             <code>RRE - para Recibo Reembolso</code>
	 *             <code>MDA - para Minuta de despacho de acompanhamento</code>
	 *             <code>OS  - para Ordem de Serviço (Serviços Adicionais)</code>
	 *             <code>PFS - para Pré Fatura de Serviço (Serviços Adicionais)</code>
	 * @return String formatada no formato <code>00000011</code>
 	*/
	public static String formataNrDocumento(String numero, String tipo) {
		String nrDocumento = null;

		if (tipo.equalsIgnoreCase("CTR") || tipo.equalsIgnoreCase("CTRC") || tipo.equalsIgnoreCase("CTE")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if(tipo.equalsIgnoreCase("CRT")) {
			nrDocumento = completaDados(numero, "0", 6, 0, true);
		} else if( tipo.equalsIgnoreCase("RNC")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if( tipo.equalsIgnoreCase("BDM")) {
			nrDocumento = completaDados(numero, "0", 10, 0, true);
		} else if( tipo.equalsIgnoreCase("DDE")) {
			nrDocumento = completaDados(numero, "0", 10, 0, true);
		} else if( tipo.equalsIgnoreCase("RDE")) {
			nrDocumento = completaDados(numero, "0", 10, 0, true);
		} else if( tipo.equalsIgnoreCase("TRA")) {
			nrDocumento = completaDados(numero, "0", 10, 0, true);
		} else if( tipo.equalsIgnoreCase("RCB")) {
			nrDocumento = completaDados(numero, "0", 10, 0, true);
		} else if( tipo.equalsIgnoreCase("MVN")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if (tipo.equalsIgnoreCase("NFS") || tipo.equalsIgnoreCase("NSE")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if (tipo.equalsIgnoreCase("NFT") || tipo.equalsIgnoreCase("NTE")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if( tipo.equalsIgnoreCase("NDN")) {
			nrDocumento = completaDados(numero, "0", 10, 0, true);
		} else if( tipo.equalsIgnoreCase("FAT")) {
			nrDocumento = completaDados(numero, "0", 10, 0, ESQUERDA);
		} else if( tipo.equalsIgnoreCase("BOL")) {
			nrDocumento = completaDados(numero, "0", 13, 0, ESQUERDA);
		} else if( tipo.equalsIgnoreCase("RRE")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if( tipo.equalsIgnoreCase("MDA")) {
			nrDocumento = completaDados(numero, "0", 8, 0, true);
		} else if( tipo.equalsIgnoreCase("OS") || tipo.equalsIgnoreCase("PFS")) {
			nrDocumento = completaDados(numero, "0", 9, 0, true);
		}
		return nrDocumento;
	}

	/**
	 * @See formataNrDocumento
	 * @return String formatada no formato <code>00000011</code>
 	*/
	public static String formataNrDocumento(Long numero, String tipo) {		
		return formataNrDocumento(numero.toString(), tipo);
	}
	
	/**
	 * @See formataNrDocumento
	 * @return String formatada no formato <code>SAO 00000011</code>
 	*/
	public static String formataNrDocumento(String sgFilial, Long numero, String tipo) {
		return sgFilial + " " + formataNrDocumento(numero, tipo);
	}
	
	/**
	 * Formata o valor passado 
	 * como parâmetro de acordo com o seu indicador.
	 * Se o indicador for Acréscimo (A) ou Desconto (D), formata o valor e acrescenta o sufixo '%'.
	 * Se o indicador for Valor (V), formata o valor e acrescenta o prefixo simbolo passado como parâmetro.
	 * Se o indicador não for Acréscimo, Desconto ou Valor, apenas formata o valor. 
	 * autor Maurício Moraes
	 * 02/02/2006
	 * @param indicador Indicador
	 * @param valor Valor
	 * @param simbolo O símbolo de moeda a ser prefixado caso o Indicador seja Valor 
	 * @return
	 */
	public static String formatValorComIndicador(String mask, String indicador, Number valor, String simbolo) {
		String valorFormatado = formatDecimal(mask,valor,true);
		if("V".equals(indicador)) {
			return simbolo + " " + valorFormatado;
		} else if("A".equals(indicador)||"D".equals(indicador)) {
			return valorFormatado + "%";
		}
		return valorFormatado;
	}

	public static String formatValorComIndicador(String indicador, Number valor, String simbolo) {
		return formatValorComIndicador("#,##0.00", indicador, valor, simbolo);
	}

	/**
	 * Formata o valor passado como parâmetro de acordo com o seu indicador.
	 * <p>
	 * Se o indicador for Acréscimo (A) ou Desconto (D), formata o valor e
	 * acrescenta o sufixo '%'.
	 * <p>
	 * Se o indicador for Valor (V), formata o valor e acrescenta o prefixo
	 * simbolo passado como parâmetro.
	 * <p>
	 * Se o indicador for Peso (P), formata o valor e acrescenta o sufixo 'kg'.
	 * Para se ter certeza que o indicador (P) significa peso deve-se averiguar
	 * qual o dominio ele pertence, visto que em alguns domínios o indicador (P)
	 * significa pontos.
	 * <p>
	 * Se o indicador não for Acréscimo, Desconto, Valor ou Peso, apenas formata
	 * o valor.
	 * <p>
	 * 
	 * @param indicador
	 * @param valor
	 * @param simbolo
	 * @return
	 */
	public static String formatValorComIndicador(String mask, String indicador, Number valor, String simbolo, String dominio) {
		if ("DM_INDICADOR_FRETE_MINIMO".equals(dominio) || "DM_INDICADOR_PEDAGIO".equals(dominio)) {
			if ("P".equals(indicador)) {
				indicador = "K";
			}
		}

		String valorFormatado = formatDecimal(mask, valor, true);
		if ("V".equals(indicador)) {
			return simbolo + " " + valorFormatado;
		} else if ("A".equals(indicador) || "D".equals(indicador)) {
			return valorFormatado + "%";
		} else if ("K".equals(indicador)) {
			return valorFormatado + " kg";
		}
		return valorFormatado;
	}

	public static String formatValorComIndicador(String indicador, Number valor, String simbolo, String dominio) {
		String mask = "#,##0.00";
		if ("DM_INDICADOR_FRETE_MINIMO".equals(dominio) || "DM_INDICADOR_PEDAGIO".equals(dominio)) {
			if ("P".equals(indicador)) {
				mask = "#,###0.000";
			}
		}
		return formatValorComIndicador(mask, indicador, valor, simbolo, dominio);
	}

	/**
	 * Formata um número de telefone.
	 * 
	 * autor Maurício Moraes 02/02/2006
	 * @param telefone
	 *            O telefone a ser formatado
	 * @return
	 */
	public static String formatTelefone(String telefone) {
		StringBuffer sb = new StringBuffer(telefone);
		if(sb.length()>=8) {
			sb.insert(sb.length()-4, '-');
		}
		return sb.toString();
	}
	public static String formatTelefoneWithDdd(String telefone) {
		StringBuffer sb = new StringBuffer(telefone);
		if(sb.length() > 8) {
			sb.insert(0,'(');
			sb.insert(3,") ");
		}
		if(sb.length()>4) {
			sb.insert(sb.length()-4, '-');
		}
		return sb.toString();
	}

	/**
	 * Formata número de telefone.
	 * 
	 * padrão no 'Comportamento de telas WEB': +55 (51) 33565050
	 * 
	 * @since 09-03-2006
	 * @author FelipeF
	 * @param numero
	 * @param ddd
	 * @param ddi
	 * @return String com o número de telefone formatado.
	 */
	public static String formatTelefone(String numero, String ddd, String ddi) {
		StringBuilder sb = new StringBuilder();

		if (StringUtils.isBlank(numero))
			return "";
		if (StringUtils.isNotBlank(ddi)) {
			sb.append("+").append(ddi).append(" ");
		}
		if (StringUtils.isNotBlank(ddd)) {
			sb.append("(").append(ddd).append(") ");
		}
		sb.append(formatTelefone(numero));

		return sb.toString();
	}

	public static String formatTelefone(TelefoneEndereco telefoneEndereco) {
		return formatTelefone(telefoneEndereco.getNrTelefone(), telefoneEndereco.getNrDdd(), telefoneEndereco.getNrDdi());
	}

	/**
	 * Formata um a moeda (sigla + simbolo). 
	 * autor Moacir Zardo Junior
	 * 14/02/2006
	 * @param moeda A moeda a qual terá sua sigla e simbolo concatenados.
	 * @return String Caso a moeda seja null, retorna null.
	 */
	public static String concatSiglaSimboloMoeda(Moeda moeda){
		String retorno = null;
		if (moeda!=null){
			retorno = moeda.getSgMoeda() + " " + moeda.getDsSimbolo();  
		}
		return retorno;
	}

	/**
	 * Concatena a sgFilial com um número formatado. Utilizando pattern
	 * default com 8 zeros para formatar o número.
	 * Exemplos: 
	 * 			coleta: "POA 00000123"
	 * 			controle carga: "POA 00000999"
	 *  
	 * @author Rodrigo Antunes
	 * @param sgFilial
	 * @param number Long que será formatado
	 * @return String
	 */
	public static String formatSgFilialWithLong(String sgFilial, Long number){
		return formatSgFilialWithLong(sgFilial, number, DEFAULT_DECIMAL_FORMAT);
	}

	/**
	 * Concatena a sgFilial com um número formatado.
	 * Exemplos: 
	 * 			coleta: "POA 00000123"
	 * 			controle carga: "POA 00000999"
	 * 
	 * @author Rodrigo Antunes  
	 * @param sgFilial 
	 * @param number Long que será formatado
	 * @param pattern String com o pattern a ser utilizado para formatar o número
	 * @return String
	 */
	public static String  formatSgFilialWithLong(String sgFilial, Long number, String pattern) {
		String retorno = null;
		if (sgFilial!=null  && !"".equals(sgFilial)) {
			if (number!=null) {
				retorno = sgFilial.trim() + " " + formatLongWithZeros(number, pattern).trim();
			}
		}
		return retorno != null ? retorno : "";
	}

	/**
	 * Recebe o número e retorna o número formatado
	 * com 8 zeros a esquerda.
	 * 
	 * @author Rodrigo Antunes
	 * @param Long number
	 * @return String
	 */
	public static String formatLongWithZeros(Long number, String pattern) {
		String retorno = null;
		if (number!=null) {
			DecimalFormat df = new DecimalFormat(pattern);
			retorno = df.format(number);
		}
		return retorno;
	}

	/**
	 * Recebe o número e retorna o número formatado
	 * com 8 zeros a esquerda.
	 * 
	 * @param Integer number
	 * @return String
	 */
	public static String formatIntegerWithZeros(Integer number, String pattern) {
		return formatLongWithZeros(Long.valueOf(number.longValue()),pattern);
	}

	public static String formatLongWithZeros(Long number) {
		return formatLongWithZeros(number, DEFAULT_DECIMAL_FORMAT);
	}

	/**
	 * * Adiciona o nome do arquivo na StringBuffer e concatena o mesmo com (1024 - length do nome do arquivo), para que seja 
	 * salvo em um campo BLOB 
	 * Os primeiros 1024 bytes são reservados para o nome do arquivo nos campos BLOB 
	 * 
	 * @param strBuff
	 * @param nameFile
	 * @param detailFile
	 * @return array de byte
	 */
	public static byte[] mountFileInArrayByte(String nameFile, String detailFile){
		StringBuffer strBuffTmp = new StringBuffer();

		strBuffTmp.append(nameFile + StringUtils.repeat(" ", 1024 - nameFile.length()));
		strBuffTmp.append(detailFile);

		return strBuffTmp.toString().getBytes();
	}

	/**
     * Adiciona o nome do arquivo na StringBuffer e concatena o mesmo com (1024 - length do nome do arquivo), para que seja 
     * salvo em um campo BLOB 
     * Os primeiros 1024 bytes são reservados para o nome do arquivo nos campos BLOB 
     * 
     * Format ASCII
     * 
     * @param nameFile
     * @param detailFile
     * @return
     */
    public static byte[] mountFileInArrayByteASCII(String nameFile, String detailFile){
            StringBuffer strBuffTmp = new StringBuffer();

            strBuffTmp.append(nameFile + StringUtils.repeat(" ", 1024 - nameFile.length()));
            strBuffTmp.append(detailFile);
            
            byte[] result;

            try {
                    result =  strBuffTmp.toString().getBytes("ASCII");        
            } catch (Exception e) {
                    result = "".getBytes();
            }
            
            return result;
    }
	 /**
     * Remove os acentos de uma String
     * 
     * @param String
     * @return String
     */
	public static String removeAccents(String str) {
		if(str != null){
			str = Normalizer.normalize(str, Normalizer.Form.NFD);
			str = str.replaceAll("[^\\p{ASCII}]", "");
		}
	    return str;
	}
	
	/**
	 * Retorna um string com espaços na direita do string informado (strLine) até chegar no tamanho informado (size)
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/04/2006
	 * 
	 * @param String strLine
	 * @param int size
	 * @return String
	 * 
	 * */
	public static String fillStringWithSpace(String strLine, int size){
		if (strLine == null){
			strLine = "";
		}
		if (strLine.length() <= size){
			return StringUtils.rightPad(strLine, size);
		} else {
			return strLine.substring(0, size);
		}
	}

	/**
	 * Retorna um string com zeros na esquerda do string informado (strLine) até chegar no tamanho informado (size)
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/04/2006
	 * 
	 * @param String strLine
	 * @param int size
	 * @return String
	 * 
	 * */	
	public static String fillNumberWithZero(String strLine, int size) {
		if (strLine == null) {
			strLine = "";
		}
		if (strLine.length() <= size) {
			return StringUtils.leftPad(strLine, size, '0');
		} else {
			return strLine.substring(0, size);
		}
	}

	/**
	 * Formata a rota preço, o campo zona é obrigatório, se o mesmo estiver
	 * em branco será retornado <code>null</code>.
	 * 
	 * @param dsZona
	 * @param nmPais
	 * @param sgUnidadeFederativa
	 * @param sgFilial
	 * @param nmMunicipio
	 * @param sgAeroporto
	 * @param nmAeroporto
	 * @param dsTipoLocalizacaoMunicipio
	 * @return
	 */
	public static String formatRotaPreco(
			String dsZona,
			String nmPais,
			String sgUnidadeFederativa,
			String sgFilial,
			String nmMunicipio,
			String sgAeroporto,
			String nmAeroporto,
			String dsTipoLocalizacaoMunicipio
	) {
		if(StringUtils.isBlank(dsZona)) {
			return null;
		}

		StringBuilder rotaPreco = new StringBuilder();
		rotaPreco.append(dsZona);
		if (StringUtils.isNotBlank(nmPais)) {
			rotaPreco.append(" - ").append(nmPais);
		}
		if (StringUtils.isNotBlank(sgUnidadeFederativa)) {
			rotaPreco.append(" - ").append(sgUnidadeFederativa);
		}
		if (StringUtils.isNotBlank(sgFilial)) {
			rotaPreco.append(" - ").append(sgFilial);
		}
		if (StringUtils.isNotBlank(nmMunicipio)) {
			rotaPreco.append(" - ").append(nmMunicipio);
		}
		if (StringUtils.isNotBlank(sgAeroporto)) {
			rotaPreco.append(" - ").append(sgAeroporto);
		}
		if (StringUtils.isNotBlank(nmAeroporto)) {
			rotaPreco.append(" - ").append(nmAeroporto);
		}
		if (StringUtils.isNotBlank(dsTipoLocalizacaoMunicipio)) {
			rotaPreco.append(" - ").append(dsTipoLocalizacaoMunicipio);
		}
		return rotaPreco.toString();
	}

	/**
	 * Retorna o tempo em minutos de uma string no formato HH:MM/HHH:MM.
	 * @author Moacir Zardo Junior
	 * @since 31/05/2006 
	 * @param tempo
	 * @param escala ESCALA_HH ou ESCALA_HHH que determinam o número de dígitos na hora.
	 * @return
	 */
	public static Long converteHorasMinutosParaMinutos(String tempo, int escala){
		if (escala == ESCALA_HH) {
			return converteHorasMinutosParaMinutos(tempo, "[0-9][0-9]:[0-5][0-9]", "LMS-00066");
		} else if (escala == ESCALA_HHH) {
			return converteHorasMinutosParaMinutos(tempo, "[0-9][0-9][0-9]:[0-5][0-9]", "LMS-00068");
		}
		return null;
	}

	private static Long converteHorasMinutosParaMinutos(String tempo, String pattern, String msg) {
		if (tempo!= null && !tempo.equals("")){
			if (tempo.matches(pattern)){
				String parts[] = tempo.split(":");
				Long horas = Long.valueOf(parts[0]);
				Long minutos = Long.valueOf(parts[1]);
				return LongUtils.add(LongUtils.multiply(horas, Long.valueOf(60)) ,minutos);
			} else {
				throw new BusinessException(msg);
			}
		}
		return null;
	}

	/**
	 * Retorna o tempo string no formato HH:MM/HHH:MM a partir de um tempo em minutos.
	 * @author Moacir Zardo Junior
	 * @since 31/05/2006 
	 * @param minutos
	 * @param escala ESCALA_HH ou ESCALA_HHH que determinam o número de dígitos na hora.
	 * @return
	 */
	public static String converteMinutosParaHorasMinutos(Long minutos, int escala){
		String tempoFormatado = null;
		if (minutos!=null){
			String strHoras = LongUtils.divide(minutos, Long.valueOf(60)).toString();
			String strMinutos = LongUtils.mod(minutos, Long.valueOf(60)).toString();

			int digitosHoras = 0;
			if (escala == ESCALA_HH) {
				digitosHoras = 2;
			} else if (escala == ESCALA_HHH) {
				digitosHoras = 3;
			}
			while (strHoras.length() < digitosHoras){
				strHoras = "0"+strHoras;
			}
			while (strMinutos.length() < 2){
				strMinutos = "0"+strMinutos;
			}
			tempoFormatado = strHoras+":"+strMinutos;
		}
		return tempoFormatado;
	}

	/**
	 * Monta os dados de endereço completo.
	 *
	 * @author José Rodrigo Moraes
	 * @since 11/01/2007
	 *
	 * @param dsTipoLogradouro Descrição do tipo de logradouro (TIPO_LOGRADOURO.DS_TIPO_LOGRADOURO)
	 * @param dsEndereco Descrição do endereço (ENDERECO_PESSOA.DS_ENDERECO)
	 * @param nrEndereco Número do endereço (ENDERECO_PESSOA.NR_ENDERECO)
	 * @param dsComplemento Complemento do endereço (ENDERECO_PESSOA.DS_COMPLEMENTO)
	 * 
	 * @see EnderecoPessoa.getEnderecoCompleto()
	 * @return String do endereço completo
	 */
	public static String montaEnderecoCompleto(String dsTipoLogradouro, String dsEndereco, String nrEndereco, String dsComplemento) {
		StringBuffer strEndereco = new StringBuffer();
		if (StringUtils.isNotBlank(dsTipoLogradouro)) {
			strEndereco.append(dsTipoLogradouro + " ");
		}
		strEndereco.append( dsEndereco + ", ");
		if (StringUtils.isNotBlank(nrEndereco)) {
			strEndereco.append(nrEndereco);
		} else {
			strEndereco.append("N/D");
		}
		if (StringUtils.isNotBlank(dsComplemento)) {
			strEndereco.append(" - " + dsComplemento);
		}
		return strEndereco.toString();				
	}

	public static String formatNrFrota(String nrFrota) {
		if(StringUtils.isBlank(nrFrota)) {
			return null;
		}
		nrFrota = nrFrota.toUpperCase();
		if(nrFrota.endsWith("%")) {
			nrFrota = nrFrota.substring(0, nrFrota.length() - 1);
		}
		if(nrFrota.startsWith("T")) {
			return "T" + FormatUtils.fillNumberWithZero(nrFrota.substring(1), 5);
		} else {
			return FormatUtils.fillNumberWithZero(nrFrota, 6);
		}
	}
	
	/**
	 * Converte um IP do formato String (###.###.###.###) para um BigInteger.
	 * 
	 * @param ip IP no formato ###.###.###.### 
	 * @return BigInteger que representa o IP
	 */
	public static BigInteger convertIpToNumber(String ip) {
		
		if(ip == null || StringUtils.isBlank(ip))
			throw new IllegalArgumentException("O ip não pode ser vazio ou nulo.");
		
		String bits = new String();
		BigInteger part = null;
		String[] parts = ip.split("\\.");
		for (int i = 0; i < parts.length; i++) {
			part = new BigInteger(parts[i]);
			bits = bits.concat(convertNumberToBinary(part, 8));
}
		return convertBinaryToNumber(bits);
	}

	/**
	 * Converte um numero em seu IP correspondente, no formato ###.###.###.###
	 * 
	 * @param number BiInteger correspondente ao IP.
	 * @return IP correspondente no formato ###.###.###.###
	 */
	public static String convertNumberToIp(BigInteger number) {
		
		if(number == null)
			throw new IllegalArgumentException("O número do ip não pode ser nulo.");
		
		String ip = new String();
		String fullBinaryIp = convertNumberToBinary(number, 32);
		String binaryIp = null;
		for (int i = 0; i < 4; i++) {
			binaryIp = fullBinaryIp.substring(i * 8, (i + 1) * 8);
			ip = ip.concat(fillNumberWithZero(convertBinaryToNumber(binaryIp).toString(),3));
			if(i < 3) {
				ip = ip.concat(".");
			}
		}
		return ip;
	}
	
	/**
	 * Converte um numero BigInteger em uma String com o binario do numero.<br>
	 * É opcional definir a quantidade mínima de bits.
	 * 
	 * @param number BigInteger com o numero.
	 * @param length Se diferente de nulo, representa a quantidade de bits que devem ser retornados, ou seja, se necessário será preenchido com zeros a esquerda.
	 * @return String com o binario do numero.
	 */
	public static String convertNumberToBinary(BigInteger number, Integer length) {
		
		if(number == null)
			throw new IllegalArgumentException("O número não pode ser nulo.");
		
		String binary = Long.toString(number.longValue(), 2);
		if(length != null && length.compareTo(0) > 0) {
			binary = fillNumberWithZero(binary, length);
		}
		return binary;
	}
	
	/**
	 * Converte uma String contendo um binario em um BigInteger com o numero correspondente em decimal. 
	 * 
	 * @param binary String contendo o binario.
	 * @return BigInteger com o numero correspondente ao binario em decimal.
	 */
	public static BigInteger convertBinaryToNumber(String binary) {
		
		if(binary == null || StringUtils.isBlank(binary))
			throw new IllegalArgumentException("O binario não pode ser vazio ou nulo.");
		
		return new BigInteger(binary, 2);
	}
	
	/**
	 * retorna o mod 11 do valor informado
	 * @param val
	 * 
	 * Modificado para atender relatorio 
	 * emitirControleCargas.jrxml
	 * 
	 * LMS-400 - Validação de Chave de Viagem (Controle de Carga) - LMS X SOM
	 * 
	 * @return
	 */
	public static String mod11(String val){
		int mult = val.length()+1;
		int somatorio = 0;
		for (int i = 0; i < val.length(); i++) {
			somatorio += Integer.parseInt(val.substring(i, i + 1)) * mult;
			mult--;
		}
		int digito1 = somatorio % 11;
		if (digito1 < 2) {
			digito1 = 2;
		} else {
			digito1 = 11 - digito1;
		}
		return String.valueOf(digito1);
	}
	
	public static String formatBigDecimalWithPattern(BigDecimal number, String pattern) {
		String retorno = null;
		if (number != null) {
			DecimalFormat df = new DecimalFormat(pattern);
			retorno = df.format(number);
		}
		return retorno;
	}
	
	   /**
     * Retorna uma lista de String a partir do valor de um Parametro Geral e seu separador, 
     * 
     * Exemplo:(, ou ;)
     */
    public static List<String> asList(String valorParametro, String separator) {
        if(valorParametro == null){
            return Collections.emptyList();
        }
        
        return Arrays.asList(valorParametro.split(separator));
    }
}
