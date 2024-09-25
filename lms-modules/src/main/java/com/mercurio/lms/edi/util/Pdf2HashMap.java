package com.mercurio.lms.edi.util;


import static com.mercurio.lms.edi.util.ConstantesPdf2HashMap.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.lms.carregamento.model.service.FileListenerService;


public class Pdf2HashMap {
	private static Log log = LogFactory.getLog(Pdf2HashMap.class);

	private String pdfFile = null; 
	private static final String LINES_SEPARATOR =  VMProperties.LINE_SEPARATOR.getValue();
	private FtpConnection conn = null;
	private InputStream inputStream = null;
	
	public Pdf2HashMap() {
		//
	}
	
	public Pdf2HashMap(String urlFile) {
		this.pdfFile = urlFile; 
	}

	public void setUrlFile(String urlFile) {
		this.pdfFile = urlFile;
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	/**
	 * Coleta o conteúdo de um arquivo PDF
	 * @return
	 */
	private String getConteudo() {
		String retorno = null;
		try {
			if(inputStream == null){
				if (conn == null) {
					File f = new File(this.pdfFile);
					inputStream = new FileInputStream(f);
				} else {
					inputStream = conn.getFTPClient().retrieveFileStream(this.pdfFile);
				}
			}

			PDDocument pdfDocument = null;
			try{
				PDFParser parser = new PDFParser(inputStream);
				parser.parse();
				pdfDocument = parser.getPDDocument();
				PDFTextStripper stripper = new PDFTextStripper();
				retorno = stripper.getText(pdfDocument);
			} catch (IOException e){
				retorno = "ERRO: Não é possível abrir a stream" + e;
			} catch (Throwable e){
				retorno = "ERRO: Um erro ocorreu enquanto tentava obter o conteúdo do PDF" + e;
			} finally{
					inputStream.close();
				if (pdfDocument != null){
					try{
						pdfDocument.close();
					} catch (IOException e){
						retorno = "ERRO: Não foi possível fechar o PDF." + e;
					}
				}
			}
		} catch(IOException e){
			log.error("ERRO: " + e.getMessage());
		}
		return retorno;
	}	

	/**
	 * Gera um Map com todo o conteúdo de um PDF
	 * @return
	 * @throws Exception 
	 */
	public Map<String, String> generateHashMap() throws Exception {
		
		String str = getConteudo();
		
		if (str.indexOf("ERRO") > -1) {
			throw new Exception(str);
		}
		
		Map<String, String> hashMap = new HashMap<String, String>();
		
		//# Cabeçalho, dados do Emitente
		String [] cabecalho1 = str.substring(str.indexOf(LINHA_PONTILHADA_CABECALHO), str.indexOf("DANFE")).split(LINES_SEPARATOR);
		String cabecalho2 = str.substring(str.indexOf("DANFE"), str.indexOf(DESTINATARIO_REMETENTE));
		
		hashMap.put(RAZAO_SOCIAL_EMITENTE, getRazaoSocialEmitente(cabecalho1));
		hashMap.put(ENDERECO_EMITENTE_TO_PUT_AND_GET_MAP, getEnderecoEmitente(cabecalho1));
		hashMap.put(BAIRRO_DISTRITO_EMITENTE_TO_PUT_AND_GET_MAP, getBairroDistritoEmitente(cabecalho1));
		hashMap.put(MUNICIPIO_EMITENTE_TO_PUT_AND_GET_MAP, getMunicipioEmitente(cabecalho1));
		hashMap.put(UF_EMITENTE_TO_PUT_AND_GET_MAP, getUfEmitente(cabecalho1));
		hashMap.put(CEP_EMITENTE_TO_PUT_AND_GET_MAP, getCepEmitente(cabecalho1));
		hashMap.put(TELEFONE_EMITENTE, getTelefoneEmitente(cabecalho1));
		hashMap.put(FAX_EMITENTE, getFaxEmitente(cabecalho1));
		hashMap.put(ENTRADA_SAIDA, getEntradaSaida(str));
		hashMap.put(NATUREZA_OPERACAO_EMITENTE, getNaturezaOperacaoEmitente(cabecalho2));
		hashMap.put(CHAVE_ACESSO, getChaveAcesso(cabecalho2));
		hashMap.put(INSCRICAO_ESTADUAL_EMITENTE_TO_PUT_AND_GET_MAP, getIEEmitente(cabecalho2));
		hashMap.put(INSCRICAO_ESTADUAL_SUBSTITUICAO_TRIB_EMITENTE, getIESTEmitente(cabecalho2));
		hashMap.put(CNPJ_EMITENTE, getCnpjEmitente(cabecalho2));
		hashMap.put(DADOS_NFE, getDadosNfe(cabecalho2));
		hashMap.put(NUMERO_NFE, getNumeroNfe(cabecalho2));
		hashMap.put(SERIE_NFE, getSerieNfe(cabecalho2));
		
		//# Dados Destinatario
		String dadosDestinatario = str.substring(str.indexOf(DESTINATARIO_REMETENTE), str.indexOf(FATURA_DUPLICATA));		
		hashMap.put(NOME_RAZAO_SOCIAL_DESTINATARIO, getNomeRazaoSocialDestinatario(dadosDestinatario));
		hashMap.put(CNPJ_CPF_DESTINATARIO, getCnpjCpfDestinatario(dadosDestinatario));
		hashMap.put(DATA_EMISSAO, getDataEmissao(dadosDestinatario));
		hashMap.put(ENDERECO_DESTINATARIO_TO_PUT_AND_GET_MAP, getEnderecoDestinatario(dadosDestinatario));
		hashMap.put(BAIRRO_DISTRITO_DESTINATARIO_TO_PUT_AND_GET_MAP, getBairroDistritoDestinatario(dadosDestinatario));
		hashMap.put(CEP_DESTINATARIO_TO_PUT_AND_GET_MAP, getCepDestinatario(dadosDestinatario));
		hashMap.put(DATA_ENTRADA_SAIDA, getDataEntradaSaida(dadosDestinatario));
		hashMap.put(MUNICIPIO_DESTINATARIO_TO_PUT_AND_GET_MAP, getMunicipioDestinatario(dadosDestinatario));
		hashMap.put(TELEFONE_FAX_DESTINATARIO, getTelefoneDestinatario(dadosDestinatario));
		hashMap.put(UF_DESTINATARIO_TO_PUT_AND_GET_MAP, getUfDestinatario(dadosDestinatario));
		hashMap.put(INSCRICAO_ESTADUAL_DESTINATARIO_TO_PUT_AND_GET_MAP, getIEDestinatario(dadosDestinatario));
		hashMap.put(HORA_SAIDA, getHoraSaida(dadosDestinatario));
		
		//# Dados Fatura
		String dadosFatura = str.substring(str.indexOf(FATURA_DUPLICATA), str.length()).trim();
		hashMap.put(FATURA_DUPLICATA, getFaturaDuplicata(dadosFatura));
		
		//# Dados Calculo Imposto
		String calculoImposto = str.substring(str.indexOf(CALCULO_IMPOSTO), str.indexOf(TRANSPORTADOR_VOLUMES_TRANSPORTADOS));
		hashMap.put(BASE_CALCULO_ICMS, getBaseCalculoIcms(calculoImposto));
		hashMap.put(VALOR_ICMS, getValorIcms(calculoImposto));
		hashMap.put(VALOR_ICMS_SUBSTITUICAO, getBaseCalculoIcmsSubstituicao(calculoImposto));
		hashMap.put(VALOR_ICMS_SUBSTITUICAO, getValorIcmsSubstituicao(calculoImposto));
		hashMap.put(VALOR_TOTAL_PRODUTOS, getValorTotalProdutos(calculoImposto));
		hashMap.put(VALOR_FRETE, getValorFrete(calculoImposto));
		hashMap.put(VALOR_SEGURO, getValorSeguro(calculoImposto));
		hashMap.put(DESCONTO, getDesconto(calculoImposto));
		hashMap.put(OUTRAS_DESPESAS_ACESSORIAS, getOutrasDespesasAcessorias(calculoImposto));
		hashMap.put(VALOR_IPI, getValorIpi(calculoImposto));
		hashMap.put(VALOR_TOTAL_NOTA, getValorTotalNota(calculoImposto));
				
		//# Dados Transportador
		String dadosTransportador = str.substring(str.indexOf(TRANSPORTADOR_VOLUMES_TRANSPORTADOS), str.indexOf(DADOS_PRODUTOS_SERVICOS));
		hashMap.put(RAZAO_SOCIAL_TRANSPORTADOR, getRazaoSocialTransportador(dadosTransportador));
		hashMap.put(FRETE_POR_CONTA, getFretePorConta(str));
		hashMap.put(CODIGO_ANTT, getCodigoAntt(dadosTransportador));
		hashMap.put(PLACA_VEICULO, getPlacaVeiculo(dadosTransportador));
		hashMap.put(UF_PLACA_VEICULO_TRANSPORTADOR_TO_PUT_AND_GET_MAP, getUfVeiculo(dadosTransportador));
		hashMap.put(CNPJ_CPF_TRANSPORTADOR, getCnpjCpfTransportador(dadosTransportador));
		hashMap.put(ENDERECO_TRANSPORTADOR_TO_PUT_AND_GET_MAP, getEnderecoTransportador(dadosTransportador));
		hashMap.put(MUNICIPIO_TRANSPORTADOR_TO_PUT_AND_GET_MAP, getMunicipioTransportador(dadosTransportador));
		hashMap.put(UF_TRANSPORTADOR_TO_PUT_AND_GET_MAP, getUfTransportador(dadosTransportador));
		hashMap.put(INSCRICAO_ESTADUAL_TRANSPORTADOR_TO_PUT_AND_GET_MAP, getIETransportador(dadosTransportador));		
		hashMap.put(QUANTIDADE_VOLUMES_TRANSPORTADOS, getQuantidadeVolumesTransportados(dadosTransportador));
		hashMap.put(ESPECIE_VOLUMES_TRANSPORTADOS, getEspecieVolumesTransportados(dadosTransportador));
		hashMap.put(MARCA_VOLUMES_TRANSPORTADOS, getMarcaVolumesTransportados(dadosTransportador));
		hashMap.put(NUMERO_VOLUMES_TRANSPORTADOS, getNumeroVolumesTransportados(dadosTransportador));
		hashMap.put(PESO_BRUTO_VOLUMES_TRANSPORTADOS, getPesoBrutoVolumesTransportados(dadosTransportador));
		hashMap.put(PESO_LIQUIDO_VOLUMES_TRANSPORTADOS, getPesoLiquidoVolumesTransportados(dadosTransportador));

		//# Dados Adicionais
		String dadosAdicionais = str.substring(str.indexOf(DADOS_ADICIONAIS), str.length());
		hashMap.put(ORDEM_COMPRA, getOrdemCompraDadosAdicionais(dadosAdicionais));		
		hashMap.put(NFREG, getNfreg(dadosAdicionais));
		
		return hashMap;
	}

	private String getNfreg(String str) {
		String nfreg = str.substring(str.indexOf(NFREG), str.length()); 
		return nfreg.substring(nfreg.indexOf(NFREG) + NFREG.length(), nfreg.indexOf(LINES_SEPARATOR)).trim();
	}
	
	private String getOrdemCompraDadosAdicionais(String str) {
		if (str.indexOf(ORDEM_COMPRA) > -1) {
			String ordemCompra = str.substring(str.indexOf(ORDEM_COMPRA), str.length());
			ordemCompra = ordemCompra.substring(ordemCompra.indexOf(ORDEM_COMPRA) + ORDEM_COMPRA.length(), ordemCompra.indexOf(LINES_SEPARATOR)).trim();
			return ordemCompra.replaceAll(":", "");
		}
		return null;
	}
	
	private String getFaxEmitente(String [] str) {
		String telefone = str[6].trim();
		return telefone.substring(telefone.indexOf("Fax:") + "Fax:".length(), telefone.length()).trim();
	}
	
	private String getTelefoneEmitente(String [] str) {
		String telefone = str[6].trim();
		return telefone.substring(telefone.indexOf(" "), telefone.indexOf("/")).trim();
	}
	
	private String getCepEmitente(String [] str) {
		return str[5].trim();
	}
	
	private String getUfEmitente(String [] str) {
		String uf = str[4];
		return uf.substring(uf.indexOf("/") + 1, uf.length()).trim();
	}
	
	private String getMunicipioEmitente(String [] str) {
		String municipio = str[4];
		return municipio.substring(0, municipio.indexOf("/")).trim();
	}
	
	private String getBairroDistritoEmitente(String [] str) {
		return str[3].trim();
	}
	
	private String getEnderecoEmitente(String [] str) {
		return str[2].trim();
	}
	
	private String getRazaoSocialEmitente(String [] str) {
		return str[1].trim();
	}

	private String getEntradaSaida(String str) {
		String entradaSaida = str.substring(str.indexOf("COD.PROD") - 5, str.indexOf("COD.PROD")).trim(); 
		return "0".equals(entradaSaida) ? "E" : "S";
	}
	
	private String getNumeroNfe(String str) {
		return str.substring(str.indexOf(NUMERO_NFE) + NUMERO_NFE.length(), str.indexOf(SERIE_NFE)).trim();
	}
	
	private String getSerieNfe(String str) {
		return str.substring(str.indexOf(SERIE_NFE) + SERIE_NFE.length(), str.indexOf(FOLHA)).trim();
	}
	
	private String getNaturezaOperacaoEmitente(String str) {
		return str.substring(str.indexOf(NATUREZA_OPERACAO_EMITENTE) + NATUREZA_OPERACAO_EMITENTE.length(), str.indexOf(CHAVE_ACESSO)).trim();
	}
	
	private String getChaveAcesso(String str) {
		return str.substring(str.indexOf(CHAVE_ACESSO) + CHAVE_ACESSO.length(), str.indexOf(INSCRICAO_ESTADUAL_EMITENTE_TO_READ_FROM_PDF)).trim();
	}
	
	private String getIEEmitente(String str) {
		return str.substring(str.indexOf(INSCRICAO_ESTADUAL_EMITENTE_TO_READ_FROM_PDF) + INSCRICAO_ESTADUAL_EMITENTE_TO_READ_FROM_PDF.length(), str.indexOf(INSCRICAO_ESTADUAL_SUBSTITUICAO_TRIB_EMITENTE)).trim();
	}
	
	private String getIESTEmitente(String str) {
		return str.substring(str.indexOf(INSCRICAO_ESTADUAL_SUBSTITUICAO_TRIB_EMITENTE) + INSCRICAO_ESTADUAL_SUBSTITUICAO_TRIB_EMITENTE.length(), str.indexOf(CNPJ_EMITENTE)).trim();
	}
	
	private String getCnpjEmitente(String str) {
		return str.substring(str.indexOf(CNPJ_EMITENTE) + CNPJ_EMITENTE.length(), str.indexOf(DADOS_NFE)).trim();
	}
	
	private String getDadosNfe(String str) {
		return str.substring(str.indexOf(DADOS_NFE) + DADOS_NFE.length(), str.length()).trim();
	}
	
	private String getRazaoSocialTransportador(String str) {
		return str.substring(str.indexOf(RAZAO_SOCIAL_TRANSPORTADOR) + RAZAO_SOCIAL_TRANSPORTADOR.length(), str.indexOf(FRETE_POR_CONTA)).trim();
	}
	
	private String getFretePorConta(String str) {
		String fretePorConta = str.substring(str.indexOf(DADOS_PRODUTOS_SERVICOS) + DADOS_PRODUTOS_SERVICOS.length(), str.indexOf(DADOS_ADICIONAIS)).trim();
		return "0".equals(fretePorConta) ? "E" : "D";
	}
	
	private String getCodigoAntt(String str) {
		return str.substring(str.indexOf(CODIGO_ANTT) + CODIGO_ANTT.length(), str.indexOf(PLACA_VEICULO)).trim();
	}
	
	private String getPlacaVeiculo(String str) {
		return str.substring(str.indexOf(PLACA_VEICULO) + PLACA_VEICULO.length(), str.indexOf(UF_PLACA_VEICULO_TRANSPORTADOR_TO_READ_FROM_PDF)).trim();
	}
	
	private String getUfVeiculo(String str) {
		return str.substring(str.indexOf(UF_PLACA_VEICULO_TRANSPORTADOR_TO_READ_FROM_PDF) + UF_PLACA_VEICULO_TRANSPORTADOR_TO_READ_FROM_PDF.length(), str.indexOf(CNPJ_CPF_TRANSPORTADOR)).trim();
	}
	
	private String getCnpjCpfTransportador(String str) {
		return str.substring(str.indexOf(CNPJ_CPF_TRANSPORTADOR) + CNPJ_CPF_TRANSPORTADOR.length(), str.indexOf(ENDERECO_TRANSPORTADOR_TO_READ_FROM_PDF)).trim();
	}
	
	private String getEnderecoTransportador(String str) {
		return str.substring(str.indexOf(ENDERECO_TRANSPORTADOR_TO_READ_FROM_PDF) + ENDERECO_TRANSPORTADOR_TO_READ_FROM_PDF.length(), str.indexOf(MUNICIPIO_TRANSPORTADOR_TO_READ_FROM_PDF)).trim();
	}
	
	private String getMunicipioTransportador(String str) {
		String string = str.substring(str.indexOf(MUNICIPIO_TRANSPORTADOR_TO_READ_FROM_PDF), str.indexOf(INSCRICAO_ESTADUAL_TRANSPORTADOR_TO_READ_FROM_PDF)).trim();
		return string.substring(string.indexOf(MUNICIPIO_TRANSPORTADOR_TO_READ_FROM_PDF) + MUNICIPIO_TRANSPORTADOR_TO_READ_FROM_PDF.length(), string.indexOf(UF_TRANSPORTADOR_TO_READ_FROM_PDF)).trim();
	}
	
	private String getUfTransportador(String str) {
		String string = str.substring(str.indexOf(MUNICIPIO_TRANSPORTADOR_TO_READ_FROM_PDF), str.indexOf(INSCRICAO_ESTADUAL_TRANSPORTADOR_TO_READ_FROM_PDF)).trim();
		return string.substring(string.indexOf(UF_TRANSPORTADOR_TO_READ_FROM_PDF) + UF_TRANSPORTADOR_TO_READ_FROM_PDF.length(), string.length()).trim();
	}
	
	private String getIETransportador(String str) {
		return str.substring(str.indexOf(INSCRICAO_ESTADUAL_TRANSPORTADOR_TO_READ_FROM_PDF) + INSCRICAO_ESTADUAL_TRANSPORTADOR_TO_READ_FROM_PDF.length(), str.indexOf(QUANTIDADE_VOLUMES_TRANSPORTADOS)).trim();
	}
	
	private String getQuantidadeVolumesTransportados(String str) {
		return str.substring(str.indexOf(QUANTIDADE_VOLUMES_TRANSPORTADOS) + QUANTIDADE_VOLUMES_TRANSPORTADOS.length(), str.indexOf(ESPECIE_VOLUMES_TRANSPORTADOS)).trim();
	}
	
	private String getEspecieVolumesTransportados(String str) {
		return str.substring(str.indexOf(ESPECIE_VOLUMES_TRANSPORTADOS) + ESPECIE_VOLUMES_TRANSPORTADOS.length(), str.indexOf(MARCA_VOLUMES_TRANSPORTADOS)).trim();
	}
	
	private String getMarcaVolumesTransportados(String str) {
		return str.substring(str.indexOf(MARCA_VOLUMES_TRANSPORTADOS) + MARCA_VOLUMES_TRANSPORTADOS.length(), str.indexOf(NUMERO_VOLUMES_TRANSPORTADOS)).trim();
	}
	
	private String getNumeroVolumesTransportados(String str) {
		return str.substring(str.indexOf(NUMERO_VOLUMES_TRANSPORTADOS) + NUMERO_VOLUMES_TRANSPORTADOS.length(), str.indexOf(PESO_BRUTO_VOLUMES_TRANSPORTADOS)).trim();
	}
	
	private String getPesoBrutoVolumesTransportados(String str) {
		String pesoBruto = str.substring(str.indexOf(PESO_BRUTO_VOLUMES_TRANSPORTADOS) + PESO_BRUTO_VOLUMES_TRANSPORTADOS.length(), str.indexOf(PESO_LIQUIDO_VOLUMES_TRANSPORTADOS)).trim();
		return pesoBruto.substring(0, pesoBruto.indexOf(" "));
	}
	
	private String getPesoLiquidoVolumesTransportados(String str) {
		String pesoLiquido = str.substring(str.indexOf(PESO_LIQUIDO_VOLUMES_TRANSPORTADOS) + PESO_LIQUIDO_VOLUMES_TRANSPORTADOS.length(), str.length()).trim();
		return pesoLiquido.substring(0, pesoLiquido.indexOf(" "));
	}
	
	private String getBaseCalculoIcms(String str) {
		return str.substring(str.indexOf(BASE_CALCULO_ICMS) + BASE_CALCULO_ICMS.length(), str.indexOf(VALOR_ICMS)).trim();
	}
	
	private String getValorIcms(String str) {
		return str.substring(str.indexOf(VALOR_ICMS) + VALOR_ICMS.length(), str.indexOf(BASE_CALCULO_ICMS_SUBSTITUICAO)).trim();
	}
	
	private String getBaseCalculoIcmsSubstituicao(String str) {
		return str.substring(str.indexOf(BASE_CALCULO_ICMS_SUBSTITUICAO) + BASE_CALCULO_ICMS_SUBSTITUICAO.length(), str.indexOf(VALOR_ICMS_SUBSTITUICAO)).trim();
	}
	
	private String getValorIcmsSubstituicao(String str) {
		return str.substring(str.indexOf(VALOR_ICMS_SUBSTITUICAO) + VALOR_ICMS_SUBSTITUICAO.length(), str.indexOf(VALOR_TOTAL_PRODUTOS)).trim();
	}
	
	private String getValorTotalProdutos(String str) {
		return str.substring(str.indexOf(VALOR_TOTAL_PRODUTOS) + VALOR_TOTAL_PRODUTOS.length(), str.indexOf(VALOR_FRETE)).trim();
	}
	
	private String getValorFrete(String str) {
		return str.substring(str.indexOf(VALOR_FRETE) + VALOR_FRETE.length(), str.indexOf(VALOR_SEGURO)).trim();
	}
	
	private String getValorSeguro(String str) {
		return str.substring(str.indexOf(VALOR_SEGURO) + VALOR_SEGURO.length(), str.indexOf(DESCONTO)).trim();
	}
	
	private String getDesconto(String str) {
		return str.substring(str.indexOf(DESCONTO) + DESCONTO.length(), str.indexOf(OUTRAS_DESPESAS_ACESSORIAS)).trim();
	}
	
	private String getOutrasDespesasAcessorias(String str) {
		return str.substring(str.indexOf(OUTRAS_DESPESAS_ACESSORIAS) + OUTRAS_DESPESAS_ACESSORIAS.length(), str.indexOf(VALOR_IPI)).trim();
	}
	
	private String getValorIpi(String str) {
		return str.substring(str.indexOf(VALOR_IPI) + VALOR_IPI.length(), str.indexOf(VALOR_TOTAL_NOTA)).trim();
	}
	
	private String getValorTotalNota(String str) {
		return str.substring(str.indexOf(VALOR_TOTAL_NOTA) + VALOR_TOTAL_NOTA.length(), str.length()).trim();
	}
	
	private String getFaturaDuplicata(String str) {
		str = str.substring(str.indexOf(LINES_SEPARATOR), str.length()).trim();
		str = str.substring(0, str.indexOf(LINES_SEPARATOR)).trim();
		return str;
	}
	
	private String getHoraSaida(String str) {
		return str.substring(str.indexOf(HORA_SAIDA) + HORA_SAIDA.length(), str.length()).trim();
	}
	
	private String getUfDestinatario(String str) {
		return str.substring(str.indexOf(UF_DESTINATARIO_TO_READ_FROM_PDF) + UF_DESTINATARIO_TO_READ_FROM_PDF.length(), str.indexOf(INSCRICAO_ESTADUAL_DESTINATARIO_TO_READ_FROM_PDF)).trim();
	}
	
	private String getTelefoneDestinatario(String str) {
		return str.substring(str.indexOf(TELEFONE_FAX_DESTINATARIO) + TELEFONE_FAX_DESTINATARIO.length(), str.indexOf(UF_DESTINATARIO_TO_READ_FROM_PDF)).trim();
	}
	
	private String getMunicipioDestinatario(String str) {
		return str.substring(str.indexOf(MUNICIPIO_DESTINATARIO_TO_READ_FROM_PDF) + MUNICIPIO_DESTINATARIO_TO_READ_FROM_PDF.length(), str.indexOf(TELEFONE_FAX_DESTINATARIO)).trim();
	}
	
	private String getDataEntradaSaida(String str) {
		return str.substring(str.indexOf(DATA_ENTRADA_SAIDA) + DATA_ENTRADA_SAIDA.length(), str.indexOf(MUNICIPIO_DESTINATARIO_TO_READ_FROM_PDF)).trim();
	}
	
	private String getCepDestinatario(String str) {
		return str.substring(str.indexOf(CEP_DESTINATARIO_TO_READ_FROM_PDF) + CEP_DESTINATARIO_TO_READ_FROM_PDF.length(), str.indexOf(DATA_ENTRADA_SAIDA)).trim();
	}
	
	private String getBairroDistritoDestinatario(String str) {
		return str.substring(str.indexOf(BAIRRO_DISTRITO_DESTINATARIO_TO_READ_FROM_PDF) + BAIRRO_DISTRITO_DESTINATARIO_TO_READ_FROM_PDF.length(), str.indexOf(CEP_DESTINATARIO_TO_READ_FROM_PDF)).trim();
	}
	
	private String getEnderecoDestinatario(String str) {
		return str.substring(str.indexOf(ENDERECO_DESTINATARIO_TO_READ_FROM_PDF) + ENDERECO_DESTINATARIO_TO_READ_FROM_PDF.length(), str.indexOf(BAIRRO_DISTRITO_DESTINATARIO_TO_READ_FROM_PDF)).trim();
	}
	
	private String getDataEmissao(String str) {
		return str.substring(str.indexOf(DATA_EMISSAO) + DATA_EMISSAO.length(), str.indexOf(ENDERECO_DESTINATARIO_TO_READ_FROM_PDF)).trim();
	}
	
	private String getCnpjCpfDestinatario(String str) {
		return str.substring(str.indexOf(CNPJ_CPF_DESTINATARIO) + CNPJ_CPF_DESTINATARIO.length(), str.indexOf(DATA_EMISSAO)).trim();
	}
	
	private String getNomeRazaoSocialDestinatario(String str) {
		return str.substring(str.indexOf(NOME_RAZAO_SOCIAL_DESTINATARIO) + NOME_RAZAO_SOCIAL_DESTINATARIO.length(), str.indexOf(CNPJ_CPF_DESTINATARIO)).trim();
	}
	
	private String getIEDestinatario(String str) {
		return str.substring(str.indexOf(INSCRICAO_ESTADUAL_DESTINATARIO_TO_READ_FROM_PDF) + INSCRICAO_ESTADUAL_DESTINATARIO_TO_READ_FROM_PDF.length(), str.indexOf(HORA_SAIDA)).trim();
	}

	public void setConn(FtpConnection conn) {
		this.conn = conn;
}

	public FtpConnection getConn() {
		return conn;
	}
}
