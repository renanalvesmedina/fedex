package com.mercurio.lms.carregamento.model.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.joda.time.DateTime;

import com.mercurio.lms.carregamento.model.ArquivoLogCompactado;
import com.mercurio.lms.carregamento.model.ArquivoLogDescompactado;
import com.mercurio.lms.carregamento.model.ArquivoLogRegistro;
import com.mercurio.lms.edi.util.FtpConnection;
import com.mercurio.lms.edi.util.FtpEsales;

public class DirectoryWatcherService {
	private static final String DIRETORIO_JA_LIDO = "ENCERRADOS/";

	private String directory;
	private FileListenerService fileListenerService;
	private FtpConnection ftp = null;
	private String nomeDoArquivo = "";
	private Map<String, InputStream> mapFiles = null;
	private Map<String, ArquivoLogDescompactado> mapFilesDescompactado = null;
	private List<ArquivoLogDescompactado> arquivosLogDescompactados;
	private LogProcessamento logProcessamento = new LogProcessamento();

	private ArquivoLogRegistroService arquivoLogRegistroService;
	private ArquivoLogCompactadoService arquivoLogCompactadoService;
	private ArquivoLogDescompactadoService arquivoLogDescompactadoService;

	private static Log log = LogFactory.getLog(DirectoryWatcherService.class);

	private void createConnection() {
		try {
			ftp = new FtpConnection();
			ftp.connect(FtpEsales.FTP_HOST, directory, FtpEsales.FTP_USER, FtpEsales.FTP_PASSWORD, FtpEsales.FTP_OS);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Erro ao conectar no FTP Servidor: %s, Diretório: %s, Usuario:%s", FtpEsales.FTP_HOST, directory, FtpEsales.FTP_USER));
		}
	}

	/**
	 * Start the monitoring of this directory.
	 */
	public void execute(String directory) {
		// evita concorrencia (duas threads tentando usar mesmo mapFiles)
		if (mapFiles != null && !mapFiles.isEmpty()) {
			return;
		}
		mapFiles = new HashMap<String, InputStream>();
		mapFilesDescompactado = new HashMap<String, ArquivoLogDescompactado>();
		this.directory = directory;
		logProcessamento.clear();
		createConnection();

		lerArquivosNaoCompactados();

		String resp = lerArquivosCompactados();
		if (resp.equals("ok")) {
			executarArquivos();
			if (!logProcessamento.isEmpty()) {
				finalizarProcesso(".log", new ByteArrayInputStream(logProcessamento.getBytes()), nomeDoArquivo);
			}
		}

		if (ftp != null) {
			ftp.disconnect();
		}
	}

	private String getDataHora() {
		Calendar calendario = Calendar.getInstance();
		String dia = putZero(calendario.get(Calendar.DAY_OF_MONTH));
		String mes = putZero(calendario.get(Calendar.MONTH) + 1);
		String ano = Integer.toString(calendario.get(Calendar.YEAR));
		String hora = putZero(calendario.get(Calendar.HOUR_OF_DAY));
		String minu = putZero(calendario.get(Calendar.MINUTE));
		String segu = putZero(calendario.get(Calendar.SECOND));
		String mili = Integer.toString(calendario.get(Calendar.MILLISECOND));

		return dia + mes + ano + "-" + hora + minu + segu + mili;
	}

	private String putZero(Integer valor) {
		String retorno = "";
		if (valor < 10) {
			retorno = "0" + valor;
		} else {
			retorno = "" + valor;
		}

		return retorno;
	}

	public void setFileListenerService(FileListenerService fileListenerService) {
		this.fileListenerService = fileListenerService;
	}

	private void lerArquivosNaoCompactados() {
		try {
			File[] files = ftp.listFiles(new OutherFileFilter());
			if (files != null && files.length > 0) {
				logProcessamento.begin("Inicio da leitura dos Arquivos não compactados.");
				for (File f : files) {
					if (f.isFile()) {
						logProcessamento.addLog("Arquivo: " + f.getName() + " Tamanho: " + f.length());
						FileInputStream fis = new FileInputStream(f);
						InputStream is = copyStream(fis);
						mapFiles.put(f.getName(), is);
						ftp.deleteFile(f.getName());
					}
				}
				logProcessamento.end("Fim da leitura dos Arquivos não compactados.");
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro ao Acessar os arquivos do FTP");
		}
	}

	private String lerArquivosCompactados() {
		File[] files = null;
		try {
			files = ftp.listFiles(new TarFileFilter());
		} catch (IOException e) {
			throw new RuntimeException("Erro ao listar os arquivos do FTP");
		}

		if (files != null && files.length > 0) {
			// Inicio da execução de leitura
			String dsErroArquivo = "";

			ArquivoLogRegistro arquivoLogRegistro = new ArquivoLogRegistro();
			
			Format formatter = new SimpleDateFormat("ddMMyyy-HHmmssSSS");
			String dataFormatada = formatter.format(new DateTime().toDate());			
			arquivoLogRegistro.setDhInicioLeitura(new DateTime());
			nomeDoArquivo = dataFormatada;
			arquivoLogRegistro.setDsArquivoLog(dataFormatada);
			
			logarMpC0(arquivoLogRegistro);

			logProcessamento.begin("Inicio da leitura dos Arquivos compactados.");
			apagarArquivos(files);
			for (File f : files) {
				if (f.isFile()) {
					logProcessamento.begin("Descompactando Arquivo: " + f.getName() + " Tamanho: " + f.length());

					ArquivoLogCompactado arquivoLogCompactado = new ArquivoLogCompactado();
					arquivoLogCompactado.setArquivoLogRegistro(arquivoLogRegistro);
					arquivoLogCompactado.setDsArquivoCompactado(f.getName() == "" ? String.valueOf(0) : f.getName());
					arquivoLogCompactado.setNrTamanhoTotalKb(f.length());
					logarMpC1(arquivoLogCompactado);

					Integer count = 0;
					TarInputStream tarIs;
					String exception = "";
					try {
						tarIs = new TarInputStream(new FileInputStream(f));
						TarEntry entry = tarIs.getNextEntry();
						Long nrTamanhoTotalKb = 0L;
						while (entry != null) {
							ArquivoLogDescompactado arquivoLogDescompactadoComErro = new ArquivoLogDescompactado();
							String fileNameDescompactado = "";
							Long tamanhoArquivoDescompactado = 0L;

							ArquivoLogDescompactado arquivoLogDescompactadoSemErro = new ArquivoLogDescompactado();
							arquivoLogDescompactadoSemErro.setArquivoLogCompactado(arquivoLogCompactado);
							String fileName = "";
							String dsErroArquivoDescompactado = "";
							String exceptionDescompactado = "";
							try {
								if (!entry.isDirectory()) {
									count++;
									fileName = entry.getName();

									arquivoLogDescompactadoSemErro.setDsArquivoDescompactado(fileName == null || fileName.equals("") ? "" : fileName.substring(fileName.indexOf(".") + 1));
									arquivoLogDescompactadoSemErro.setNrTamanhoTotalKb(entry.getSize() > 0 ? entry.getSize() : 0L);

									fileNameDescompactado = fileName;
									tamanhoArquivoDescompactado = entry.getSize();

									Pattern p = Pattern.compile("^(.*\\/)(.*$)");
									Matcher a = p.matcher(fileName);

									if (a.find()) {
										fileName = a.group(2);
									}

									logProcessamento.addLog("Descompactando ->" + fileName + " Tamanho: " + entry.getSize());
									mapFiles.put(fileName, copyStream(tarIs));
									mapFilesDescompactado.put(fileName, arquivoLogDescompactadoSemErro);

									nrTamanhoTotalKb = nrTamanhoTotalKb + entry.getSize();

									logarMpC2(arquivoLogDescompactadoSemErro);
								}
							} catch (FileNotFoundException e) {
								dsErroArquivoDescompactado = String.valueOf(e);
								exceptionDescompactado = "Arquivos não encontrado";
								log.error(exceptionDescompactado, e);
							} catch (IOException e) {
								dsErroArquivoDescompactado = String.valueOf(e);
								exceptionDescompactado = "Erro ao descompactar o arquivo";
								log.error(exceptionDescompactado, e);
							}

							if (!exceptionDescompactado.equals("")) {
								if (mapFiles.remove(fileName) != null) {
									mapFiles.remove(fileName);
									mapFilesDescompactado.remove(fileName);
								}

								arquivoLogDescompactadoComErro.setDsArquivoDescompactado(fileNameDescompactado == null || fileNameDescompactado.equals("") ? "" : fileNameDescompactado.substring(fileNameDescompactado.indexOf(".") + 1));
								arquivoLogDescompactadoComErro.setNrTamanhoTotalKb(tamanhoArquivoDescompactado);
								arquivoLogDescompactadoComErro.setArquivoLogCompactado(arquivoLogCompactado);
								arquivoLogDescompactadoComErro.setDsErroArquivo(dsErroArquivoDescompactado);
								logarMpC2(arquivoLogDescompactadoComErro);

							}
							entry = tarIs.getNextEntry();
						}

						arquivoLogCompactado.setQtTotalArquivoDescompactado(count == null || count == null ? 0 : count);
						arquivoLogCompactado.setNrTamanhoTotalKb(nrTamanhoTotalKb);
					
						logarMpC1(arquivoLogCompactado);
					} catch (FileNotFoundException e) {
						dsErroArquivo = String.valueOf(e);
						exception = "Arquivos não encontrado";
						log.error(exception, e);
					} catch (IOException e) {
						dsErroArquivo = String.valueOf(e);
						exception = "Erro ao descompactar o arquivo";
						log.error(exception, e);
					}

					if (!exception.equals("")) {
						arquivoLogRegistro.setDsErroArquivo(dsErroArquivo);
						arquivoLogRegistro.setQtTotalArquivoCompactado(files.length);
						arquivoLogRegistro.setDhFimLeitura(new DateTime());
						logarMpC0(arquivoLogRegistro);

						arquivoLogCompactado.setDsErroArquivo(dsErroArquivo);
						arquivoLogCompactado.setQtTotalArquivoDescompactado(count == null || count == null ? 0 : count);
						logarMpC1(arquivoLogCompactado);

						return exception;
					}
					logProcessamento.end("Descompactado total de : " + count + " arquivos.");
				}
			}
			logProcessamento.end("Fim da leitura dos Arquivos compactados.");

			// Fim da execução de leitura
			arquivoLogRegistro.setQtTotalArquivoCompactado(files == null ? 0 : files.length);
			arquivoLogRegistro.setDhFimLeitura(new DateTime());

			logarMpC0(arquivoLogRegistro);
			return "ok";
		}

		return "nothing";
	}

	private void apagarArquivos(File[] files) {
		for (File f : files) {
			if (f.isFile()) {
				try {
					ftp.deleteFile(f.getName());
				} catch (IOException e) {
					throw new RuntimeException("Erro ao deletar arquivo do FTP");
				}
			}
		}
	}

	private void executarArquivos() {
		if (!mapFiles.isEmpty()) {
			logProcessamento.begin("In" + "icio do processamento dos arquivos.");
			for (String key : mapFiles.keySet()) {
				logProcessamento.begin("Processando Arquivo: " + key);
				fileListenerService.setLogProcessamento(logProcessamento);
				fileListenerService.execute(mapFiles.get(key), mapFilesDescompactado.get(key));
				finalizarProcesso(key, mapFiles.get(key), nomeDoArquivo);
				logProcessamento.end("Fim do Processamento.");
			}
			logProcessamento.end("Fim do processamento dos arquivos.");
			mapFiles = null;
		}
	}

	private void finalizarProcesso(String fileName, InputStream is, String dataHora) {
		String file = DIRETORIO_JA_LIDO + dataHora + fileName;
		try {
			is.reset();
			ftp.storeFile(file, is);
		} catch (FileNotFoundException e) {
			mapFiles = null;
			throw new RuntimeException("Arquivo não encontrado");
		} catch (IOException e) {
			mapFiles = null;
			throw new RuntimeException("Erro ao deletar o arquivo");
		}
	}

	private InputStream copyStream(InputStream is) throws IOException {
		if (is != null) {
			OutputStream out = new ByteArrayOutputStream();
			if (is instanceof TarInputStream) {
				((TarInputStream) is).copyEntryContents(out);
				out.flush();
			} else {
				byte[] buffer = new byte[1024];
				try {
					int n;
					while ((n = is.read(buffer)) != -1) {
						out.write(buffer, 0, n);
					}
				} finally {
					is.close();
				}
			}
			return new ByteArrayInputStream(out.toString().getBytes());
		} else {
			return new ByteArrayInputStream(new byte[] {});
		}
	}

	static public void main(String[] args) {
		DirectoryWatcherService dw = new DirectoryWatcherService();
		dw.setFileListenerService(new FileListenerService());
		dw.execute("/@NOTFIS/GMMapa_homolog/teste");
	}

	public class LogProcessamento {
		StringBuilder sb = new StringBuilder();
		StringBuilder ident = new StringBuilder();

		public void begin() {
			addLog(null);
			ident.append("\t");
		}

		public void begin(String begin) {
			if (begin != null) {
				addLog(begin);
			}
			ident.append("\t");
		}

		public void addLog(String log) {
			sb.append(ident);
			sb.append(log);
			sb.append("\n");
		}

		public void end() {
			if (ident.length() > 0) {
				ident.setLength(ident.length() - 1);
			}
			addLog(null);
		}

		public void end(String end) {
			if (ident.length() > 0) {
				ident.setLength(ident.length() - 1);
			}
			if (end != null) {
				addLog(end);
			}
		}

		public byte[] getBytes() {
			return sb.toString().getBytes();
		}

		public String toString() {
			return sb.toString();
		}

		public void clear() {
			sb.setLength(0);
			ident.setLength(0);
		}

		public boolean isEmpty() {
			return sb.length() == 0;
		}
	}

	/**
	 * Método responsável por chamar o serviço de CRUD do arquivoLogRegistro.
	 * Nele é gerado um log no banco, sobre os arquivos que são processados. O
	 * metodos logarMpC0, logarMpC1 e logarMpC2 são usados no metodo
	 * lerArquivosCompactados, a função desses logs é gravar possíveis erros que
	 * ocorram durante o processamento dos arquivos.
	 * 
	 * Demanda LMS-1236
	 */
	public ArquivoLogRegistro logarMpC0(ArquivoLogRegistro arquivoLogRegistro) {
		arquivoLogRegistroService.store(arquivoLogRegistro);
		return arquivoLogRegistro;
	}

	/**
	 * Método responsável por chamar o serviço de CRUD do arquivoLogCompactado.
	 * Nele é gerado um log no banco, sobre os arquivos compactados lidos.
	 * 
	 * Demanda LMS-1236
	 */
	private ArquivoLogCompactado logarMpC1(ArquivoLogCompactado arquivoLogCompactado) {
		arquivoLogCompactadoService.store(arquivoLogCompactado);
		return arquivoLogCompactado;
	}

	/**
	 * Método responsável por chamar o serviço de CRUD do
	 * arquivoLogDescompactado. Nele é gerado um log no banco, sobre os arquivos
	 * descompactados durante o processamento.
	 * 
	 * Demanda LMS-1236
	 */
	private void logarMpC2(ArquivoLogDescompactado arquivoLogDescompactado) {
		arquivoLogDescompactadoService.store(arquivoLogDescompactado);
	}

	// GETTERS E SETTERS
	public void setArquivoLogRegistroService(ArquivoLogRegistroService arquivoLogRegistroService) {
		this.arquivoLogRegistroService = arquivoLogRegistroService;
	}

	public void setArquivoLogCompactadoService(ArquivoLogCompactadoService arquivoLogCompactadoService) {
		this.arquivoLogCompactadoService = arquivoLogCompactadoService;
	}

	public void setArquivoLogDescompactadoService(ArquivoLogDescompactadoService arquivoLogDescompactadoService) {
		this.arquivoLogDescompactadoService = arquivoLogDescompactadoService;
	}
}

class TarFileFilter implements FileFilter {
	public boolean accept(File file) {
		String f = file.getName().toLowerCase();
		return (f.indexOf(".tar") > 0);
	}
}

class OutherFileFilter implements FileFilter {
	public boolean accept(File file) {
		String f = file.getName().toLowerCase();
		return (f.indexOf(".tar") < 0);
	}

}
