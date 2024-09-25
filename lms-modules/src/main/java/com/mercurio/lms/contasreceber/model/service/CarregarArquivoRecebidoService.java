package com.mercurio.lms.contasreceber.model.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemOcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.OcorrenciaPreFatura;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

/**
 * @author Rafael Andrade de Oliveira
 * @since 28/04/2006
 *  
 * @see Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.carregarArquivoRecebidoService"
 */
public class CarregarArquivoRecebidoService {

	/**
	 * Constante p/ valor tipo 000 da linha
	 */
	private static int _000 = 0;
	
	/**
	 * Constante p/ valor tipo 390 da linha
	 */
	private static int _390 = 390;
	
	/**
	 * Constante p/ valor tipo 391 da linha
	 */
	private static int _391 = 391;
	
	/**
	 * Constante p/ valor tipo 392 da linha
	 */
	private static int _392 = 392;
	
	/**
	 * Constante p/ valor tipo 393 da linha
	 */
	private static int _393 = 393;
	
	/**
	 * Constante p/ valor tipo 394 da linha
	 */
	private static int _394 = 394;
	
	/**
	 * Constante p/ valor tipo 395 da linha
	 */
	private static int _395 = 395;

	/**
	 * Constante p/ valor tipo 395 da linha
	 */
	private static int _396 = 396;

	/**
	 * Constante p/ valor tipo 395 da linha
	 */
	private static int _397 = 397;

	/**
	 * Constante p/ valor tipo 395 da linha
	 */
	private static int _398 = 398;

	private Logger log = LogManager.getLogger(this.getClass());
	private ReportExecutionManager reportExecutionManager;
	
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	
	
	/**
	 * Execução da rotina de importação de arquivo de pré-fatura -> PROCEDA
	 * Método que chama a importação do arquivo
	 * @param tfm Parametros da tela
	 */
	public TypedFlatMap executeImportacao(TypedFlatMap tfm) {
		
		YearMonthDay dtEmissao = tfm.getYearMonthDay("dtEmissao");
		if(!faturaService.validaDtEmissao(dtEmissao)){
			throw new BusinessException("LMS-36099");
		}
		
		if( JTDateTimeUtils.comparaData(tfm.getYearMonthDay("dtVencimento"),dtEmissao) < 0){
			throw new BusinessException("LMS-36192");
		}

		/* Leitor do arquivo por linha com buffer */
		BufferedReader in = null;
		
		/* Linha corrente do arquivo lido */
		String linha = null;
		
		/* Nome do arquivo que foi feito upload */
		String nmArquivo = null;
		
		
		//verifica se o arquivo contem alguma coisa e se a linha de 200 caracters que é o padrao
		try {

			/* Pega o buffer do arquivo */
			if( tfm.getString("arquivo") == null || tfm.getString("arquivo").equalsIgnoreCase("") ){
				throw new BusinessException("LMS-36193");
			} else {
				in = getBuffer(tfm.getString("arquivo"));
			}
						
			/* Pega a primeira linha */
			linha = in.readLine();
			
			/* Tratamento da primeira linha, que começa com o nome do arquivo*/
			nmArquivo = linha.substring(0,1024); 
			linha = linha.substring(1024);
			
			//verifica se a primeira linha de dados começa por "999"
			if (linha.length() < 3 || !linha.substring(0,3).equals("000")){
				throw new BusinessException("LMS-36193");
			}
		}catch ( IOException e) {
			log.error(e);
		}
		
		/* Lista que guardam os ids para ações futuras */
		List idsDevedorDocServFat = new ArrayList(); 
		List idsFaturas = new ArrayList();
		
		/* cliente selecionado na tela */ 
		String cli = FormatUtils.filterNumber(tfm.getString("nrIdentificacao"));
		Cliente cliente = clienteService.findByIdInitLazyProperties(tfm.getLong("cliente.idCliente"), false);
		
		
		/* Atributo com o tipo da linha corrente, baseado nas constantes */
		int tipo = 0;		
		
		
		try {

			tipo = getTipoLinha(linha);
			
			/* Nova fatura a ser gerada */
			Fatura fatura = null;
			
			/* Nova ocorrencia de pre-fatura a ser gerada */
			OcorrenciaPreFatura ocorrenciaPreFatura = null;
			
			String cnpj = null;
			
			/* 
			 * Para cada linha, exite uma rotina lógica a ser executada conforme o tipo de linha.
			 * O tipo 395 é a última linha do arquivo 
			 */
			while (true) {
			
				if (tipo == _391) {
					
					cnpj = getSubstring(linha, 4, 18);
					
					/* Pula a linha do arquivo até achar o cliente */ 
					if (!Long.valueOf(cnpj).toString().equals(Long.valueOf(cli).toString())) {
						
						linha = in.readLine();
						tipo = getTipoLinha(linha);
						
						/* Enquanto não chegar na próxima separação (tipo = _000) */ 
						while (tipo != _000 && tipo != _395) {
							linha = in.readLine();
							tipo = getTipoLinha(linha);
						}
						
						/* Depois de chegar no fim do cliente, pula a linha para o próximo cliente */
						linha = in.readLine();
					}
					
				} else if (tipo == _392) {
					
					/* seta os dados da fatura a ser gerada */ 
					fatura.setNrPreFatura(getSubstring(linha, 3, 23));
					fatura.setDtEmissao(tfm.getYearMonthDay("dtEmissao"));
					fatura.setDtVencimento(tfm.getYearMonthDay("dtVencimento"));
					fatura.setBlGerarBoleto(tfm.getBoolean("gerarBoleto"));
					fatura.setCedente(cedenteService.findById(tfm.getLong("cedente.idCedente")));
					fatura.setDivisaoCliente(divisaoClienteService.findById(tfm.getLong("divisaoCliente.idDivisaoCliente")));
					
					/* seta os dados da ocorrencia de pre-fatura a ser gerada */
					ocorrenciaPreFatura.setCliente(cliente);
					ocorrenciaPreFatura.setDhImportacao(JTDateTimeUtils.getDataHoraAtual());
					ocorrenciaPreFatura.setDtEmissao(fatura.getDtEmissao());
					ocorrenciaPreFatura.setDtVencimento(fatura.getDtVencimento());
					ocorrenciaPreFatura.setNmArquivo(nmArquivo.trim());
					ocorrenciaPreFatura.setNrPreFatura(fatura.getNrPreFatura());
					
					/* grava a ocorrencia de pre-fatura */
					ocorrenciaPreFaturaService.store(ocorrenciaPreFatura);
					
				} else if (tipo == _393) {

					/* Monta array de identificacao para pesquisar várias possibilidades */
					List identificacoes = new ArrayList();
					identificacoes.add(getSubstring(linha, 154, 168));
					identificacoes.add(getSubstring(linha, 76, 90));
					identificacoes.add(getSubstring(linha, 77, 91));
					
					String[] nrIdentificacao = new String[3];
					nrIdentificacao[0] = (String) identificacoes.get(0);	
					nrIdentificacao[1] = (String) identificacoes.get(1);	
					nrIdentificacao[2] = (String) identificacoes.get(2);	
					
					/* Pesquisa pelo array de identificacao */
					List filiais = filialService.findFilialByArrayNrIdentificacaoPessoa(identificacoes);
					
					/* REGRA: Movimento com CNPJ Mercúrio inexistente. CNPJ: {0}, {1} ou {2} */
					if (filiais.isEmpty()) {
						throw new BusinessException("LMS-36114", nrIdentificacao);
					}

					Filial filial = (Filial) filiais.get(0);
					Long idFilial = filial.getIdFilial();
					Long nrDoctoServico = Long.valueOf(getSubstring(linha, 56, 68));
					String dhEmissao = getSubstring(linha, 68, 76);

					/* Busca documento de servico CTR ou CTE */
					
					DomainValue dmTpDoctoServico = domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", "CTR");
					String TpDoctoServicoMsg = dmTpDoctoServico.getValue(); 
					List doctoServicos = doctoServicoService.findDoctoServicoByParams(idFilial, nrDoctoServico, dmTpDoctoServico.getValue(), dhEmissao);
					if( doctoServicos.isEmpty() ){
						dmTpDoctoServico = domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", "CTE");
						TpDoctoServicoMsg = TpDoctoServicoMsg.concat(" ou "+dmTpDoctoServico.getValue()); 
						doctoServicos = doctoServicoService.findDoctoServicoByParams(idFilial, nrDoctoServico, dmTpDoctoServico.getValue(), dhEmissao);
						if( !doctoServicos.isEmpty() ){
							TpDoctoServicoMsg = dmTpDoctoServico.getValue(); 
						}
					}		
					String tpDoctoServico = dmTpDoctoServico.getValue();
					/* Array dos paramentros da mensagem */ 
					Object[] msgParams = new Object[] {
								TpDoctoServicoMsg + " " +
								filial.getSgFilial() + " " +
								nrDoctoServico
							};
					
					/* REGRA: Documento de serviço inexistente. Documento de serviço: {0} */
					if (doctoServicos.isEmpty()) {
						throw new BusinessException("LMS-36115", msgParams);
					}
					
					/* Cria mapa dos dados do documento de serviço encontrado */
					Object[] obj = (Object[]) doctoServicos.get(0);
					TypedFlatMap map = new TypedFlatMap();
					map.put("idFilial", obj[0]);
					map.put("idDevedorDocServFat", obj[1]);
					map.put("idDoctoServico", obj[2]);
					map.put("nrDoctoServico", obj[3]);
					map.put("nrIdentificacao", obj[4]);
					map.put("dtEmissao", obj[5]);
					map.put("tpModal", obj[6]);
					map.put("tpAbrangencia", obj[7]);
					map.put("tpSituacaoCobranca", obj[8]);
					
					/* REGRA: O documento de serviço está pendende para outra filial. Documento de serviço: {0} */
					if (map.getLong("idFilial").longValue() != SessionUtils.getFilialSessao().getIdFilial().longValue()) {
						throw new BusinessException("LMS-36119", msgParams);
					}

					/* REGRA: O documento de serviço não está pendente para este cliente. Documento de serviço: {0} */
					if (!cnpj.equals(map.getString("nrIdentificacao"))) {
						throw new BusinessException("LMS-36120", msgParams);
					}
					
					String tpSituacaoCobranca = map.get("tpSituacaoCobranca").toString(); 

					if (tpSituacaoCobranca.equals("L") || tpSituacaoCobranca.equals("F")) {

						/* Novo item de ocorrencia de pre-fatura */
						ItemOcorrenciaPreFatura itemOcorrenciaPreFatura = new ItemOcorrenciaPreFatura();
						itemOcorrenciaPreFatura.setTpDoctoServico(dmTpDoctoServico);
						itemOcorrenciaPreFatura.setSgFilial(filial.getSgFilial());
						itemOcorrenciaPreFatura.setNrDoctoServico(map.getLong("nrDoctoServico"));
						itemOcorrenciaPreFatura.setDtEmissao( map.getYearMonthDay("dtEmissao") );
						itemOcorrenciaPreFatura.setOcorrenciaPreFatura(ocorrenciaPreFatura);
						
						if (tpSituacaoCobranca.equals("L")) {
							itemOcorrenciaPreFatura.setObItemOcorrenciaPreFatura( configuracoesFacade.getMensagem("LMS-36116") );
						} else if (tpSituacaoCobranca.equals("F")) {
							itemOcorrenciaPreFatura.setObItemOcorrenciaPreFatura( configuracoesFacade.getMensagem("LMS-36118") );
						}
						
						/* Grava novo item de ocorrencia de pre-fatura */
						itemOcorrenciaPreFaturaService.store(itemOcorrenciaPreFatura);
					
					}

					String tpSituacao = null;
					Long idDoctoServico = map.getLong("idDoctoServico");					
					/* Consulta documento de servico conforme tipo e seta o tipo */
					/*
					 * Obs.: As condições abaixo estão implementadas mas funciona apenas para CTR e CTE,
					 * pois são informadas de forma fixa no codigo, alimentando a variavel "tpDoctoServico",
					 * conforme conversado com o analista joelson, o cliente irá mandar qual a posição do 
					 * arquivo essa informação deverá vir
					 * */
					if (tpDoctoServico.equals("CTR") || tpDoctoServico.equals("NFT")||
							tpDoctoServico.equals("CTE") || tpDoctoServico.equals("NTE")) {
						Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idDoctoServico, false);
						tpSituacao = conhecimento.getTpSituacaoConhecimento().getValue();
					} else if (tpDoctoServico.equals("NDN")) {
						NotaDebitoNacional notaDebitoNacional = notaDebitoNacionalService.findById(idDoctoServico);
						tpSituacao = notaDebitoNacional.getTpSituacaoNotaDebitoNac().getValue();
					} else if (tpDoctoServico.equals("NFS")||tpDoctoServico.equals("NSE")) {
						NotaFiscalServico notaFiscalServico = notaFiscalServicoService.findById(idDoctoServico);
						tpSituacao = notaFiscalServico.getTpSituacaoNf().getValue();
					} else {
						tpSituacao = "";
					}
					
					/* Case tipo = 'C' gera novo item ocorrencia de pre-fatura */
					if (tpSituacao.equals("C")) {
						ItemOcorrenciaPreFatura itemOcorrenciaPreFatura2 = new ItemOcorrenciaPreFatura();
						itemOcorrenciaPreFatura2.setTpDoctoServico(dmTpDoctoServico);
						itemOcorrenciaPreFatura2.setSgFilial(filial.getSgFilial());
						itemOcorrenciaPreFatura2.setNrDoctoServico(nrDoctoServico);
						itemOcorrenciaPreFatura2.setDtEmissao( map.getYearMonthDay("dtEmissao") );
						itemOcorrenciaPreFatura2.setObItemOcorrenciaPreFatura(configuracoesFacade.getMensagem("LMS-36117"));
						itemOcorrenciaPreFatura2.setOcorrenciaPreFatura(ocorrenciaPreFatura);
						
						/* Grava novo item de ocorrencia de pre-fatura */
						itemOcorrenciaPreFaturaService.store(itemOcorrenciaPreFatura2);
					}
					
					if (idsDevedorDocServFat.isEmpty()) {
						// Seta atributos na fatura pelo 1º documento encontrado na varredura das linhas do arquivo
						fatura.setTpModal(map.getDomainValue("tpModal"));
						fatura.setTpAbrangencia(map.getDomainValue("tpAbrangencia"));
					}

					/* adiciona id na lista */
					if (!tpSituacaoCobranca.equals("L") && !tpSituacaoCobranca.equals("F") && !tpSituacao.equals("C")) {
						idsDevedorDocServFat.add(map.getLong("idDevedorDocServFat"));
					}
					
				} else if (tipo == _000) { // 000 -> separação no arquivo (zera tudo)
					
					/* Caso tenha algum id na lista chama a rotina abaixo */
					if (idsDevedorDocServFat.size() > 0) {

						/* Grava fatura e pega o objeto Serializable pq precisa do id */ 
						Fatura novafatura = (Fatura) gerarFaturaArquivoRecebidoService.storeFaturaWithIdsDevedorDocServFat(fatura, idsDevedorDocServFat);
						
						/* adiciona id na lista */
						idsFaturas.add(novafatura.getIdFatura());
					}
					
					fatura = new Fatura();					
					ocorrenciaPreFatura = new OcorrenciaPreFatura();
					
					idsDevedorDocServFat = new ArrayList();
				} else if (tipo == _395) {
					
					/* Caso tenha algum id na lista chama a rotina abaixo */
					if (idsDevedorDocServFat.size() > 0) {
						
						/* Grava fatura e pega o objeto Serializable pq precisa do id */ 
						Fatura novafatura = (Fatura) gerarFaturaArquivoRecebidoService.storeFaturaWithIdsDevedorDocServFat(fatura, idsDevedorDocServFat);
						
						/* adiciona id na lista */
						idsFaturas.add(novafatura.getIdFatura());
					}
					
					break;
				}
				
				/* Posiciona na próxima linha do arquivo */ 
				linha = in.readLine();
				
				/* Pega o tipo de linha da linha corrente */
				tipo = getTipoLinha(linha);

			}
			
			/* Fecha e libera o buffer */
			if (in != null)
				in.close();
			
			in = null;
			
		} catch ( IOException e) {
			log.error(e);
		}

		faturaService.flush();
		
		/* Caso tenha algum id na lista chama o relatório de faturas nacionais */
		TypedFlatMap reportLocation = new TypedFlatMap();
		if (idsFaturas.size() > 0) {
			try {
				String importacaoPrefaturas = "";
				
				for (Iterator iter = idsFaturas.iterator(); iter.hasNext();) {
					Long id = (Long) iter.next();
					importacaoPrefaturas += id.toString();
					
					if (iter.hasNext())
						importacaoPrefaturas += ", ";						
				}
				
				TypedFlatMap parameters = new TypedFlatMap();
				parameters.put("importacaoPreFaturas", importacaoPrefaturas);
				parameters.put("tpFormatoRelatorio", "pdf");

				reportLocation.put("reportLocator",this.reportExecutionManager.generateReportLocator("lms.contasreceber.emitirFaturasNacionaisService", parameters));
				
			} catch (Exception e) {
				throw new InfrastructureException(e);
			}
		} else {
			/* Nenhuma fatura gerada... */
			/* REGRA: Arquivo não pertence a este cliente. */
			reportLocation.put("LMS_36112",configuracoesFacade.getMensagem("LMS-36112"));			
		}
		
		return reportLocation;
	}
		
	/**
	 * Cria o buffer para leitura linha a linha do arquivo selecionado
	 * @param arquivo campo da tela
	 * @return BufferedReader
	 */
	private BufferedReader getBuffer(String arquivo) {
		
		ByteArrayInputStream ba = null;
		BufferedReader buff = null;
		
		try {
			ba = new ByteArrayInputStream(Base64Util.decode(arquivo));
			buff = new BufferedReader(new InputStreamReader(ba));
		} catch (IOException e) {
			throw new BusinessException("Erro ao ler arquivo", e);
		}
		
		return buff;
	}

	/**
	 * Pega o tipo de linha conforme a linha corrente.
	 * @param String linha
	 * @return Tipo da linha
	 */
	private int getTipoLinha(String linha) {
		if (linha == null ){
			return _395;
		}else if ( linha.trim() == ""){ // caso a linha esteja em branco
			return _395;
		}
		
		String codigo = linha.substring(0,3); 
		
		int tipo = Integer.parseInt(codigo);
		
		switch (tipo) {
			case 0: 
				return _000; 
			case 390:
				return _390; 
			case 391: 
				return _391;
			case 392: 
				return _392;
			case 393: 
				return _393;
			case 394:
				return _394;
			case 396:
				return _396;
			case 397:
				return _397;
			case 398:
				return _398;
			default: 
				return _395;
		}
	}
	
	/**
	 * Pega pedaço da linha corrente, baseado nas colunas inicial e final passadas
	 * @param String linha corrente
	 * @param primeira Nº da coluna inicial
	 * @param ultima Nº da coluna final+1
	 * @return Texto desejado conforme posições passadas
	 */
	private String getSubstring(String linha, int primeira, int ultima) {
		String retorno = null;
		
		/* Retorno em branco em caso de erro de substring (erro com as posicoes) */ 
		try {
			retorno = linha.substring(primeira, ultima).trim();
		} catch (Exception e) {
			return "";
		}
		
		return retorno;
	}

	/**
	 * IoC -> cedenteService
	 */
	CedenteService cedenteService;
	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}
	
	/**
	 * IoC -> clienteService
	 */
	ClienteService clienteService;
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	/**
	 * IoC -> filialService
	 */
	FilialService filialService;
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	/**
	 * IoC -> ocorrenciaPreFaturaService
	 */
	OcorrenciaPreFaturaService ocorrenciaPreFaturaService;
	public void setOcorrenciaPreFaturaService(OcorrenciaPreFaturaService ocorrenciaPreFaturaService) {
		this.ocorrenciaPreFaturaService = ocorrenciaPreFaturaService;
	}
	
	/**
	 * IoC -> itemOcorrenciaPreFaturaService
	 */
	ItemOcorrenciaPreFaturaService itemOcorrenciaPreFaturaService;
	public void setItemOcorrenciaPreFaturaService(ItemOcorrenciaPreFaturaService itemOcorrenciaPreFaturaService) {
		this.itemOcorrenciaPreFaturaService = itemOcorrenciaPreFaturaService;
	}
	
	/**
	 * IoC -> faturaService
	 */
	FaturaService faturaService;
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	
	/**
	 * IoC -> gerarFaturaArquivoRecebidoService
	 */
	GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService;
	public void setGerarFaturaArquivoRecebidoService(GerarFaturaArquivoRecebidoService gerarFaturaArquivoRecebidoService) {
		this.gerarFaturaArquivoRecebidoService = gerarFaturaArquivoRecebidoService;
	}
	
	/**
	 * IoC -> doctoServicoService
	 */
	DoctoServicoService doctoServicoService;
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	/**
	 * IoC -> conhecimentoService
	 */
	ConhecimentoService conhecimentoService;
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	/**
	 * IoC -> notaDebitoNacionalService
	 */
	NotaDebitoNacionalService notaDebitoNacionalService;
	public void setNotaDebitoNacionalService(NotaDebitoNacionalService notaDebitoNacionalService) {
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}
	
	/**
	 * IoC -> notaFiscalServicoService
	 */
	NotaFiscalServicoService notaFiscalServicoService;
	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}
	
	/**
	 * IoC -> domainValueService
	 */
	DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	/**
	 * IoC -> configuracoesFacade
	 */
	ConfiguracoesFacade configuracoesFacade;
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	/**
	 * IoC -> divisaoClienteService
	 */
	DivisaoClienteService divisaoClienteService;
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	/**
	 * Lista os cedentes na combo da tela
	 * @param criteria Filtros da tela
	 * @return List
	 */
	public List findComboCedentes(TypedFlatMap criteria){
		return this.cedenteService.findComboByActiveValues(criteria);
	}	
	
	/**
	 * Lista divisoes de cliente na combo da tela
	 * @param criteria Filtros da tela
	 * @return Lista de Divisoes do Cliente
	 */
	public List findComboDivisaoCliente(TypedFlatMap criteria){
		return this.divisaoClienteService.find(criteria);
	}
	
	/**
	 * Lookup de cliente 
	 * @param map Filtros da tela
	 * @return lista de Clientes
	 */
	public List findLookupCliente(Map map){
		return this.clienteService.findLookup(map);
	}
	
	/**
	 * @author José Rodrigo Moraes
	 * @since  17/08/2006
	 * 
	 * Se o cliente possui um cedente, retorna este para a combo "Cedente", 
	 * caso contrário, traz o cedente da filial de cobrança do cliente, ou seja utilizar 
	 * FILIAL.ID_CEDENTE_BLOQUETO onde FILIAL.ID_FILIAL = CLIENTE.ID_FILIAL_COBRANCA.
	 * 
	 * @param tfm IdCliente
	 * @return Cedente
	 */
	public TypedFlatMap findCedenteByCliente(Long idCliente){
		TypedFlatMap retorno = new TypedFlatMap();
		
		Cedente cedente = cedenteService.findByIdCliente(idCliente);
		
		if( cedente != null ){
			retorno = montaCedente(cedente);
		} else {			
			retorno = montaCedente(cedenteService.findCedenteByFilialCobrancaCliente(idCliente));
		}
		
		return retorno;
	}

	private TypedFlatMap montaCedente(Cedente cedente) {
		TypedFlatMap tfm = new TypedFlatMap();
		
		if( cedente == null ){
			tfm = null;
		} else {			
			tfm.put("cedente.idCedente",cedente.getIdCedente());
			tfm.put("cedente.dsCedente",cedente.getDsCedente());
		}
		
		return tfm;
	}
	
	
}
