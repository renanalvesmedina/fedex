package com.mercurio.lms.util;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class ArquivoUtils {

	private static final Logger LOGGER = LogManager.getLogger(ArquivoUtils.class);
	private static final String ZIP_EXTENSION = ".zip";
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * @deprecated Utilizar nos novos
	 *             {@link org.hibernate.annotations.Formula @Formula}
	 *             no mapeamento da entidade, como em
	 *             {@link com.mercurio.lms.expedicao.model.OrdemServicoAnexo#nmArquivo}
	 * 
	 * @param arquivoBytes
	 * @return
	 */
	@Deprecated
	public static String getNomeArquivo(byte[] arquivoBytes) {
		String nomeArquivo = extrairNomeDoBlob(arquivoBytes);
		if ("".equals(nomeArquivo)) {
			nomeArquivo = findNomeArquivoSubString(arquivoBytes);
		}

		return nomeArquivo;
	}

	/**
	 * @deprecated Utilizar nos novos
	 *             {@link org.hibernate.annotations.Formula @Formula}
	 *             no mapeamento da entidade, como em
	 *             {@link com.mercurio.lms.expedicao.model.OrdemServicoAnexo#nmArquivo}
	 * 
	 * @param locator
	 * @return
	 */
	@Deprecated
	public static String extrairNomeDoBlob(byte[] data) {
		final byte[] buffer = Arrays.copyOf(data, BUFFER_SIZE);
		final String fileNameWithSpaces = new String(buffer, ISO_8859_1);
		return fileNameWithSpaces.trim();
	}

	public static String findNomeArquivoSubString(byte[] arquivo) {
		String nomeArquivo = "";
		try {
			ByteArrayInputStream arquivoInput = new ByteArrayInputStream(arquivo );
			BufferedReader arquivoBuffer = new BufferedReader(new InputStreamReader(arquivoInput, ISO_8859_1));
			String ln = arquivoBuffer.readLine();
			nomeArquivo = ln.substring(0, BUFFER_SIZE).trim();							
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return nomeArquivo;
	}

	public static byte[] toByteArray(Blob fromBlob) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			return toByteArrayImpl(fromBlob, baos);
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
		byte[] buf = new byte[4000];
		try (InputStream is = fromBlob.getBinaryStream()) {
			for (; ; ) {
				int dataSize = is.read(buf);

				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		}
		return baos.toByteArray();
	}
	
	public static String getNomeArquivoZip(String nomeArquivoOriginal){
		String nomeArquivoZip = nomeArquivoOriginal;
		nomeArquivoZip = nomeArquivoZip.substring(0, nomeArquivoZip.lastIndexOf("."));
		nomeArquivoZip += ZIP_EXTENSION;
		return nomeArquivoZip;
	}
	
	/**
	 * Cria e retorna uma arquivo compactado, em formato ZIP.
	 * O arquivo retornado terá o mesmo nome do arquivo original, somente trocando a extensão.
	 * 
	 * @param arquivoOriginal Arquivo que se deseja compactar.
	 * @return arquivoCompactado Novo arquivo compactado em formato ZIP.
	 */
	public static File compactarArquivo(File arquivoOriginal) {
		try {
			String nomeArquivoCompactado = getNomeArquivoZip(arquivoOriginal.getName());
			String caminhoNomeArquivoCompactado = new ReportExecutionManager().generateOutputDir().getAbsolutePath() + "/" + nomeArquivoCompactado;
			File arquivoCompactado = new File(caminhoNomeArquivoCompactado);

			FileOutputStream fileOutputStream = new FileOutputStream(arquivoCompactado);
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

			ZipEntry zipEntry = new ZipEntry(arquivoOriginal.getName());
			zipOutputStream.putNextEntry(zipEntry);

			FileInputStream fileInputStream = new FileInputStream(arquivoOriginal);
			byte[] buf = new byte[BUFFER_SIZE];
			int bytesRead;

			while ((bytesRead = fileInputStream.read(buf)) > 0) {
				zipOutputStream.write(buf, 0, bytesRead);
			}

			zipOutputStream.closeEntry();
			zipOutputStream.close();
			fileOutputStream.close();
			fileInputStream.close();

			return arquivoCompactado;
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
	}
}
