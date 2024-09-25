package com.mercurio.lms.edi.model.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.net.io.Util;

import com.mercurio.lms.edi.model.Edi945dnn;
import com.mercurio.lms.edi.model.Edi945dnnLabes;
import com.mercurio.lms.edi.util.ConstantesPdf2HashMap;
import com.mercurio.lms.edi.util.FtpConnection;
import com.mercurio.lms.edi.util.FtpEsales;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeracaoAppleNotFis {

	private static final Logger LOGGER = LogManager.getLogger(GeracaoAppleNotFis.class);

	public static String geracaoNotFisLMS(Map<String, String> mapa, Edi945dnn edi945dnn, String ftpFolderNotfis, String ftpFolderNotfisEsales) throws IOException {
		
		FtpConnection conn = new FtpConnection();
		conn.connect(FtpEsales.FTP_HOST, ftpFolderNotfis, FtpEsales.FTP_USER, FtpEsales.FTP_PASSWORD, FtpEsales.FTP_OS);

		Calendar today = Calendar.getInstance();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyHHmm");
		SimpleDateFormat formato2 = new SimpleDateFormat("ddMMHHmm");
		
		String dnnNumber = mapa.get("NFREG").toString();
		String espacos = "                                                                                                               ";
		String nomeArquivo = "Syncreon_"+dnnNumber+".txt";
		
		OutputStream ou = conn.getFTPClient().storeFileStream(nomeArquivo); 
		BufferedOutputStream buffWriter = new BufferedOutputStream(ou);

		try {
			IntregracaoAppleOutBoundService.escreve(buffWriter, "000" + "TNT BRASIL                            ".substring(0, 35) +
			                            "APPLE COMPUTER BRASIL LTDA            ".substring(0, 35) + 
			                            formato.format(today.getTime())) ;
			IntregracaoAppleOutBoundService.escreve(buffWriter, "310" + "NOTFI" +
                    formato2.format(today.getTime()) + "0");
			boolean b311 = false;
			if (!b311) {
				b311 = true;
				/* ***********************************************************************************
				 * 311
				 ************************************************************************************* */
				StringBuffer registro311 = new StringBuffer();
				registro311.append((soNumeros(mapa.get(ConstantesPdf2HashMap.CNPJ_EMITENTE).toString()) + "       ").substring(0, 14));
				//Inscrição estadual remetente
				registro311.append("407262644118   ");
				
				//Endereço Remetente
				registro311.append("ROD. V.PREF. HERMENEG. TONOLLI 1500.             ".substring(0, 40));
				//Cidade Remetente
				registro311.append("JUNDIAI                                          ".substring(0, 35));
				//CEP Remetente
				registro311.append("13213086 ");
				//UF Remetente
				registro311.append("SP       ");
				//Data de Embarque
				registro311.append(mapa.get(ConstantesPdf2HashMap.DATA_EMISSAO).toString().substring(0,2)+
								   mapa.get(ConstantesPdf2HashMap.DATA_EMISSAO).toString().substring(3,5)+
								   mapa.get(ConstantesPdf2HashMap.DATA_EMISSAO).toString().substring(6,10));
				//Remetente
				registro311.append(mapa.get(ConstantesPdf2HashMap.RAZAO_SOCIAL_EMITENTE));
				IntregracaoAppleOutBoundService.escreve(buffWriter, "311" + registro311.toString());
				
			}
			
			/* ***********************************************************************************
			 * 312
			 ************************************************************************************* */
			StringBuffer registro312 = new StringBuffer();
			//Nome Destinatario
			registro312.append((mapa.get(ConstantesPdf2HashMap.NOME_RAZAO_SOCIAL_DESTINATARIO).toString() + espacos).substring(0, 40));
			//CNPJ CPF Destinatario
			registro312.append((soNumeros(mapa.get(ConstantesPdf2HashMap.CNPJ_CPF_DESTINATARIO).toString()) + espacos).substring(0, 14));
			//INSC Destinatario // Falar com Cleveland
			String inscricao = mapa.get(ConstantesPdf2HashMap.INSCRICAO_ESTADUAL_DESTINATARIO_TO_PUT_AND_GET_MAP).toString().trim();
			if ((inscricao == null) || (inscricao.length() == 0)) {
				inscricao = "ISENTO";
			}
			
			registro312.append((inscricao + espacos).substring(0,15));			
			//Endereço Destinatario // Falar com Cleveland
			registro312.append((mapa.get(ConstantesPdf2HashMap.ENDERECO_DESTINATARIO_TO_PUT_AND_GET_MAP).toString() + espacos).substring(0, 40));
			//Bairro
			registro312.append((mapa.get(ConstantesPdf2HashMap.BAIRRO_DISTRITO_DESTINATARIO_TO_PUT_AND_GET_MAP).toString() + espacos).substring(0, 20));
			//Cidade Destinatario // Falar com Cleveland - Ta junto da Razão Social
			registro312.append((mapa.get(ConstantesPdf2HashMap.MUNICIPIO_DESTINATARIO_TO_PUT_AND_GET_MAP).toString() + espacos).substring(0,35));			
			//CEP  Destinatario 
			registro312.append((mapa.get(ConstantesPdf2HashMap.CEP_DESTINATARIO_TO_PUT_AND_GET_MAP).toString().substring(0,5)+
					   mapa.get(ConstantesPdf2HashMap.CEP_DESTINATARIO_TO_PUT_AND_GET_MAP).toString().substring(6,9) + "      ").substring(0,9));
			registro312.append(espacos.substring(0,9));
			registro312.append((mapa.get(ConstantesPdf2HashMap.UF_DESTINATARIO_TO_PUT_AND_GET_MAP).toString()+ "          ").substring(0,9));
			IntregracaoAppleOutBoundService.escreve(buffWriter, "312" + registro312.toString());

			/* ***********************************************************************************
			 * 313
			 ************************************************************************************* */
			StringBuffer registro313 = new StringBuffer();
			//Embarcadora
			registro313.append(espacos.substring(0, 15));
			//Rota
			registro313.append(espacos.substring(0, 7));
			//MEIO DE TRANSPORTE
			if (edi945dnn.getTransportWay().equals("AIR")) {
				registro313.append("2"); // Aéreo
			} else if (edi945dnn.getTransportWay().equals("SEA")) {
					registro313.append("3"); // Marítimo
			} else {
				registro313.append("1"); // Rodoviário
			}	
			//TIPO DO TRANSPORTE DA CARGA/TIPO DE CARGA
			registro313.append("  ");
			//CONDIÇÃO DE FRETE = CIF FOB
			String tipo = "C";
			if (mapa.get("FRETE POR CONTA").toString() != "E") {
				tipo = "F";
			}
			registro313.append((tipo+ "          ").substring(0,1));
			//Série da Nota
			registro313.append((mapa.get(ConstantesPdf2HashMap.SERIE_NFE).toString()+ "          ").substring(0,3));
			//Número da Nota // Falar com Cleveland - Achei dificil de pegar - e perigoso perco o primeiro digito
			int indNota = mapa.get(ConstantesPdf2HashMap.FATURA_DUPLICATA).toString().indexOf("Num.:");
			String nota = mapa.get(ConstantesPdf2HashMap.FATURA_DUPLICATA).toString();
			registro313.append((nota.substring(indNota+6, indNota+14)).substring(0,8));
			//Data de Emissão
			registro313.append(mapa.get(ConstantesPdf2HashMap.DATA_EMISSAO).toString().substring(0,2)+
							   mapa.get(ConstantesPdf2HashMap.DATA_EMISSAO).toString().substring(3,5)+
							   mapa.get(ConstantesPdf2HashMap.DATA_EMISSAO).toString().substring(6,10));
						
			//NATUREZA (TIPO) DA MERCADORIA / ESPÉCIE DE ACONDICIONAMENTO
			registro313.append(espacos.substring(0, 30));
			//Volumes
			Integer volumesValor = Integer.valueOf(mapa.get(ConstantesPdf2HashMap.QUANTIDADE_VOLUMES_TRANSPORTADOS).toString().trim());
			volumesValor = volumesValor * 100;
			String volumes = volumesValor.toString();

			String pesoBruto = mapa.get(ConstantesPdf2HashMap.PESO_BRUTO_VOLUMES_TRANSPORTADOS).toString();
			int pos = pesoBruto.indexOf(",");
			if (pos > -1) {
				pos+=3;
				pesoBruto = pesoBruto.substring(0, (pos > pesoBruto.length() ? pesoBruto.length(): pos));
			} else {
				pesoBruto = pesoBruto + "00";
			}
				
			registro313.append(("000000000000000000".substring(0, 7-volumes.length()) + volumes).substring(0, 7));
			//Valor total da nota
			String totalnota = soNumeros(mapa.get(ConstantesPdf2HashMap.VALOR_TOTAL_NOTA).toString());
			registro313.append(("000000000000000000".substring(0, 15-totalnota.length()) + totalnota).substring(0, 15));
			//Peso Bruto da nota
			String pesonota = soNumeros(pesoBruto);
			registro313.append(("000000000000000000".substring(0, 7-pesonota.length()) + pesonota).substring(0, 7));
			//Peso Cubagem
			registro313.append(espacos.substring(0, 7));
			//Espaco Divisao	
			registro313.append("                                                                                                             ");
			registro313.append(edi945dnn.getDivisao());
						
			IntregracaoAppleOutBoundService.escreve(buffWriter, "313" + registro313.toString());
			

			/* ***********************************************************************************
			 * 314
			 ************************************************************************************* */
			int max314 = 0;
			StringBuffer registro314 = new StringBuffer();
			for (Edi945dnnLabes label: edi945dnn.getDnnLabels().values()) {
				//Quantidade de Volumes
				registro314.append("0000100");
				//Espécie de Acondicionamento
				registro314.append(espacos.substring(0,15));
				//Mercadoria da Nota Fiscal
				registro314.append(("00" + label.getLabel() + espacos).substring(0, 30));
				max314++;
				
				if (max314 == 4) {
					IntregracaoAppleOutBoundService.escreve(buffWriter, "314" + registro314.toString());
					max314 = 0;
					registro314 = new StringBuffer();
				}
				
			}
			if ((max314 > 0) && (registro314.toString().trim().length() > 0)) {
				IntregracaoAppleOutBoundService.escreve(buffWriter, "314" + registro314.toString());
			}
			
			
			/* ***********************************************************************************
			 * 318
			 ************************************************************************************* */
			StringBuffer registro318 = new StringBuffer();
			//Valor total da nota
			totalnota = soNumeros(mapa.get(ConstantesPdf2HashMap.VALOR_TOTAL_NOTA).toString());
			registro318.append(("000000000000000000".substring(0, 15-totalnota.length()) + totalnota).substring(0, 15));
			//Peso Bruto da nota
			pesonota = soNumeros(pesoBruto);
			registro318.append(("000000000000000000".substring(0, 15-pesonota.length()) + pesonota).substring(0, 15));
			//Peso Cubagem
			registro318.append("                   ".substring(0, 15));
			//Volumes
			volumes = soNumeros(mapa.get(ConstantesPdf2HashMap.QUANTIDADE_VOLUMES_TRANSPORTADOS).toString());
			registro318.append(("000000000000000000".substring(0, 15-volumes.length()) + volumes).substring(0, 15));
			
			IntregracaoAppleOutBoundService.escreve(buffWriter, "318" + registro318.toString());

			/* ***********************************************************************************
			 * 329
			 ************************************************************************************* */
			StringBuffer registro329 = new StringBuffer();
			registro329.append((edi945dnn.getReferenceID() + "                        ").substring(0, 20)); 
			registro329.append((edi945dnn.getEdi945().getInterchange() + "                                                                                            ").substring(0, 60)); 
			registro329.append((edi945dnn.getEdi945().getInterchange().substring(60) + "                                                                              ").substring(0, 60)); 
			registro329.append((edi945dnn.getEdi945().getFunctional() + "                                                                                             ").substring(0, 60));
			registro329.append((edi945dnn.getDivisao() + "                                                                                             ").substring(0, 60));
			IntregracaoAppleOutBoundService.escreve(buffWriter, "329" + registro329.toString());
			
			buffWriter.close();

		} catch (IOException e) {
			LOGGER.error(e);
		}
		conn.disconnect();
		
		//Busca arquivo salvo
		conn.connect(FtpEsales.FTP_HOST, ftpFolderNotfis, FtpEsales.FTP_USER, FtpEsales.FTP_PASSWORD, FtpEsales.FTP_OS);
		InputStream is = conn.getFTPClient().retrieveFileStream(ftpFolderNotfis + nomeArquivo);
		conn.disconnect();

		//Grava copia do arquvivo no notfis
		conn.connect(FtpEsales.FTP_HOST, ftpFolderNotfisEsales, FtpEsales.FTP_USER, FtpEsales.FTP_PASSWORD, FtpEsales.FTP_OS);
		ou = conn.getFTPClient().storeFileStream(nomeArquivo); 
		Util.copyStream(is, ou);
		ou.close();
		is.close();
		conn.disconnect();

		return nomeArquivo;
	}
	
	private static String soNumeros(String object) {
		String retorno = "";
		byte[] obj = object.getBytes();
		
		for (int i = 0; i < object.length(); i++) {
			if ((obj[i] != '.') && (obj[i] != '-') && (obj[i] != '/') && (obj[i] != ',')) {
				retorno += (char) obj[i];
			}
		}
		return retorno;
	}

	}
