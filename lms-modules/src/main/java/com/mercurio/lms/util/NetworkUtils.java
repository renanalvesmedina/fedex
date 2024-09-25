package com.mercurio.lms.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.VMProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkUtils {
	private static final Logger LOGGER = LogManager.getLogger(NetworkUtils.class);
	private static final String ARP_COMMAND_WINDOWS = "arp -a";
	private static final String ARP_COMMAND_LINUX = "/sbin/arp -a";

	/**
	 * Função simples para testar a classe. A primeira String do array
	 * args deve ser o ip que deseja-se obter o MAC Address
	 * @param args
	 */
	private static void main(String[] args) {
		String ip = args[0];
		try {
			System.out.println("IP:" + ip);
			System.out.println("MAC: " + getMacAddress(ip));
		} catch(Throwable t) {
			LOGGER.error(t);
		}
	}	

	/**
	 * Função que obtem o MAC Address de uma maquina a partir de um IP especificado
	 * Para o correto funcionamento desta função, o usuário que estiver executando a
	 * classe (ou o AS no caso) deve ter direito de executar o comando ARP
	 * @param ip
	 * @return MAC Address da maquina
	 * @throws IOException
	 */
	public static String getMacAddress(String ip) {
		String os = VMProperties.OS_NAME.getValue();
		try {
			String arpOutput = "";
			if(os.startsWith("Windows")) {
				arpOutput = runCommand(ARP_COMMAND_WINDOWS);
				return windowsParseMacAddress(arpOutput, ip);
			} else if(os.startsWith("Linux")) {
				arpOutput = runCommand(ARP_COMMAND_LINUX);
				return linuxParseMacAddress(arpOutput, ip);
			} else {
				throw new InfrastructureException("unknown operating system: " + os);
			}
		} catch(Exception e) {
			LOGGER.error(e);
			throw new InfrastructureException(e.getMessage());
		}
	}

	/**
	 * Função interna que extrai o MAC Address a partir do retorno do
	 * comando ARP em sistemas Linux
	 * @param arpOutput
	 * @param ip
	 * @return
	 * @throws ParseException
	 */
	private static String linuxParseMacAddress(String arpOutput, String ip) throws ParseException {
		ip += ") at ";
		arpOutput = arpOutput.substring(arpOutput.indexOf(ip) + ip.length());
		arpOutput = arpOutput.substring(0, arpOutput.indexOf(' '));
		arpOutput = arpOutput.replace(":", "-");
		return arpOutput;
	}

	/**
	 * Função interna que extrai o MAC Address a partir do retorno do
	 * comando ARP em sistemas Windows
	 * @param arpOutput
	 * @param ip
	 * @return
	 * @throws ParseException
	 */
	private static String windowsParseMacAddress(String arpOutput, String ip) throws ParseException, IOException {
		arpOutput = arpOutput.substring(arpOutput.indexOf(ip) + ip.length()).trim();
		arpOutput = arpOutput.substring(0, arpOutput.indexOf(' '));
		return arpOutput;
	}

	/**
	 * Função que executa o comando
	 * @return
	 * @throws IOException
	 */
	private final static String runCommand(String command) throws IOException {
		Process p = Runtime.getRuntime().exec(command);
		InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

		StringBuffer buffer= new StringBuffer();
		for (;;) {
			int c = stdoutStream.read();
			if (c == -1) break;
			buffer.append((char)c);
		}
		String outputText = buffer.toString();
		stdoutStream.close();
		return outputText;
	}
}
