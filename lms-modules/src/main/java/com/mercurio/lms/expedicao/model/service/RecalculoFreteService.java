package com.mercurio.lms.expedicao.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.edi.dto.RelatorioErrosRecalculoFreteDTO;
import com.mercurio.lms.expedicao.DocumentoServicoFacade;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.ClienteNaoProcessar;
import com.mercurio.lms.expedicao.model.ClienteProcessar;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Densidade;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.RecalculoFrete;
import com.mercurio.lms.expedicao.model.RecalculoFreteArquivoDTO;
import com.mercurio.lms.expedicao.model.dao.ClienteNaoProcessarDAO;
import com.mercurio.lms.expedicao.model.dao.ClienteProcessarDAO;
import com.mercurio.lms.expedicao.model.dao.RecalculoFreteDAO;
import com.mercurio.lms.expedicao.model.dao.RecalculoFreteFlexDAO;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.service.SubtipoTabelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.expedicao.recalculoFreteService"
 */
public class RecalculoFreteService {

	private ServicoService servicoService;
	private MunicipioService municipioService;
	private CalcularFreteService calcularFreteService;
	private UnidadeFederativaService unidadeFederativaService;
	private FilialService filialService;
	private SubtipoTabelaPrecoService subtipoTabelaPrecoService;
	private ParametroGeralService parametroGeralService;
	private ConhecimentoService conhecimentoService;
	private ConhecimentoNormalService conhecimentoNormalService;
	private NaturezaProdutoService naturezaProdutoService;
	private ClienteService clienteService;
	
	private DocumentoServicoFacade documentoServicoFacade;
	
	private ParcelaRecalculoService parcelaRecalculoService;

	private RecalculoFreteFlexDAO recalculoFreteFlexDAO;
	private RecalculoFreteDAO  recalculoFreteDAO;
	private ClienteProcessarDAO clienteProcessarDAO;
	private ClienteNaoProcessarDAO clienteNaoProcessarDAO;
	
	private static final DomainValue TP_CALCULO_PRECO = new DomainValue("N");
	private static final DomainValue TP_CONHECIMENTO  = new DomainValue("NO");
	private static final DomainValue TP_DOCUMENTO     = new DomainValue("CTR");
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeProcessamentoItem(final Long idRecalculoFrete, final Long idDoctoServico, final List<RelatorioErrosRecalculoFreteDTO> erros) {
		final Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
		final RecalculoFrete recalculoFrete = findById(idRecalculoFrete);

		Frete frete = executeRecalculoFrete(conhecimento);
		
		/*Efetua o log de informações do recálculo*/
		BigDecimal vlTotalFrete = BigDecimalUtils.defaultBigDecimal(frete.getCalculoFrete().getVlTotalParcelas());
		List<Map> parcelas;
		if(BigDecimalUtils.hasValue(vlTotalFrete)){
			parcelas = calcularFreteService.findParcelasRecalculo(frete, idDoctoServico);
		}else{
			parcelas = null;
		}
		
		storeRecalculoFrete(recalculoFrete, frete, conhecimento, vlTotalFrete, parcelas);
	}
	
	/**
	 * Recalculo de frete 
	 * 
	 * @param idConhecimento
	 * @return
	 */
	private Frete executeRecalculoFrete(final Conhecimento conhecimento){
		Session session = conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();

		Frete frete = executeRecalculoFreteConhecimento(conhecimento);

		if(frete != null){
			calcularFreteService.montarParcelasCalculo(frete.getConhecimento(),frete.getCalculoFrete());
		}
		session.evict(conhecimento);
		
		return frete;		
	}
	
	/**
	 * Calculo frete flex
	 * 
	 * @param idConhecimento
	 * @return
	 */
	public Frete executeRecalculoFreteConhecimento(final Conhecimento conhecimento) {
        return executeRecalculoFreteConhecimento(conhecimento, false);
    }
				
	
	/**
	 * Calculo frete flex
	 * 
	 * @param idConhecimento
	 * @return
	 */
	public Frete executeRecalculoFreteConhecimento(final Conhecimento conhecimento, boolean recalculoSorter) {
				
		/*Monta o objeto conhecimento para cálculo*/
		Conhecimento conhecimentoNovo =	ConhecimentoUtils.cloneConhecimento(conhecimento);
		getRecalculoFreteDAO().getAdsmHibernateTemplate().initialize(conhecimentoNovo.getClienteByIdClienteRemetente());
		getRecalculoFreteDAO().getSessionFactory().getCurrentSession().evict(conhecimentoNovo.getClienteByIdClienteRemetente());

	    if (recalculoSorter) {
	        conhecimentoNovo.setIdDoctoServico(conhecimento.getIdDoctoServico());
	    }
	    
		conhecimentoNovo.setTpCalculoPreco(new DomainValue(ConstantesExpedicao.CALCULO_NORMAL));

		conhecimentoNovo.getClienteByIdClienteRemetente().setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_ESPECIAL));
				
		/*Monta o objeto para calcular o frete*/
		CalculoFrete calculoFrete = new CalculoFrete();
		calculoFrete.setRecalculoFrete(true);
		
		//LMS-2354
		calculoFrete.setBlRecalculoFreteSorter(recalculoSorter);
		
		calculoFrete.setDhEmissaoDocRecalculo(conhecimento.getDhEmissao());
		calculoFrete.setPsReferenciaCalculo(conhecimento.getPsReferenciaCalculo());
		calculoFrete.setTarifaPreco(conhecimento.getTarifaPreco());
		calculoFrete.setParametroCliente(conhecimento.getParametroCliente());
		calculoFrete.setTabelaPrecoRecalculo(conhecimento.getTabelaPreco());
		calculoFrete.setIdServico(conhecimento.getServico().getIdServico());
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.FALSE);
		calculoFrete.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());
		calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());
		calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente() != null ? conhecimento.getDivisaoCliente().getIdDivisaoCliente() : null);
		calculoFrete.setClienteBase(conhecimento.getClienteByIdClienteBaseCalculo());
		
		/*Monta o objeto Frete para calculo*/
		Frete frete = new Frete(); 
		frete.setConhecimento(conhecimentoNovo);
		frete.setCalculoFrete(calculoFrete);

		/*Configura os dados do calculo de frete atraves do conhecimento*/
		conhecimentoNormalService.configureCalculoFrete(frete.getConhecimento(),frete.getCalculoFrete());

		/*Executa o cálculo do frete*/			
		documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(frete.getCalculoFrete());		

		/*Copia os dados do calculo de frete para o conhecimento*/
		CalculoFreteUtils.copyResult(frete.getConhecimento(), frete.getCalculoFrete()); 
		return frete;

	}
	
	public void storeRecalculoFrete(final RecalculoFrete recalculoFrete, final Frete frete, final Conhecimento conhecimento, final BigDecimal vlTotalFrete, final List<Map> parcelas){
		Long idDoctoRecalculo = getRecalculoFreteDAO().storeDoctoRecalculo(recalculoFrete, frete, conhecimento);
		if(parcelas != null){
			parcelaRecalculoService.storeParcelasRecalculo(idDoctoRecalculo, recalculoFrete.getIdRecalculoFrete(), parcelas);
		}
	}
	
	public RecalculoFrete findById(Long id) {
		return getRecalculoFreteDAO().findById(id);
	}

	public List<RecalculoFrete> findInProcess(RecalculoFrete recalculoFrete) {
		return getRecalculoFreteDAO().findInProcess(recalculoFrete);
	}

	/**
	 * Persiste o objeto RecalculoFrete na tabela RECALCULO_FRETE
	 * @param recalculoFrete
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeRecalculoFrete(RecalculoFrete recalculoFrete, boolean check){

		getRecalculoFreteDAO().store(recalculoFrete);

		if(check){
			if(CollectionUtils.isNotEmpty(findInProcess(recalculoFrete))){
				throw new BusinessException("LMS-01202");
			}

			getRecalculoFreteDAO().store(recalculoFrete);
		}
	}

	public List<Map<String, Object>> findClientesProcessar(Long idRecalculoFrete){

		List<Map<String, Object>> listCliente = new ArrayList<Map<String, Object>>();

		List<ClienteProcessar> list = getClienteProcessarDAO().findClientesProcessar(idRecalculoFrete);
		if(CollectionUtils.isNotEmpty(list)){
			for(ClienteProcessar cliente : list){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("idCliente", cliente.getCliente().getIdCliente());
				param.put("nrIdentificacao", cliente.getCliente().getPessoa().getNrIdentificacao());
				listCliente.add(param);
			}
		}
		return listCliente;
	}

	public List<Map<String, Object>> findClientesNaoProcessar(Long idRecalculoFrete){

		List<Map<String, Object>> listCliente = new ArrayList<Map<String, Object>>();

		List<ClienteNaoProcessar> list = getClienteNaoProcessarDAO().findClientesNaoProcessar(idRecalculoFrete);
		if(CollectionUtils.isNotEmpty(list)){
			for(ClienteNaoProcessar cliente : list){
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("idCliente", cliente.getCliente().getIdCliente());
				param.put("nrIdentificacao", cliente.getCliente().getPessoa().getNrIdentificacao());
				listCliente.add(param);
			}
		}
		return listCliente;
	}

	/**
	 * Paginação da tela de Recalculo
	 * @param paginatedQuery
	 * @return
	 */
	public ResultSetPage<RecalculoFrete> findPaginated(PaginatedQuery paginatedQuery) {
		return getRecalculoFreteDAO().findPaginated(paginatedQuery);
	}


	/**
	 * Obtem a filial de atendimento do municipio
	 *
	 * @param idMunicipio
	 * @return Filial
	 */
	public Filial findFilial(Long idMunicipio){
		return filialService.findById(getRecalculoFreteFlexDAO().findIdFilial(idMunicipio));
	}

	/**
	 * Obtem informações de Municipio e Endereço do cliente
	 *
	 * @param  nrIdentificacao
	 * @param  cdIBGE
	 * @return Cliente
	 */
	public Cliente findDadosCliente(String nrIdentificacao, String cdIBGE, String sgUnidadeFederativa ){
		Cliente cliente  = findCliente(nrIdentificacao);
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();

		List<Map<String, Object>> listUF = getUnidadeFederativaService().findUfBySgAndPais(sgUnidadeFederativa, 30L);

		if(listUF != null && !listUF.isEmpty()){
			Long idUnidadeFederativa = MapUtils.getLong((Map<String, Object>)listUF.get(0), "idUnidadeFederativa");
			enderecoPessoa.setMunicipio(findMunicipio(LongUtils.getLong(cdIBGE),idUnidadeFederativa));
			UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
			unidadeFederativa.setIdUnidadeFederativa(idUnidadeFederativa);
			enderecoPessoa.getMunicipio().setUnidadeFederativa(unidadeFederativa);
		}

		cliente.getPessoa().setEnderecoPessoa(enderecoPessoa);
		return cliente;
	}

	/**
	 * Cria o cliente
	 *
	 * @param  nrIdentificacao
	 * @param  nmMunicipio
	 * @return Cliente
	 */
	public Cliente findCliente(String nrIdentificacao){
		Cliente cliente = new Cliente();
		Pessoa  pessoa  = new Pessoa();
		Long idPessoa = getRecalculoFreteFlexDAO().findIdPessoa(nrIdentificacao);
		
		if(idPessoa == null){
			throw new BusinessException("LMS-01180");
		}
		
		pessoa.setIdPessoa(idPessoa);
		pessoa.setNrIdentificacao(nrIdentificacao);
		cliente.setIdCliente(idPessoa);
		cliente.setPessoa(pessoa);
		

		Map map = clienteService.findTpClienteIdFilialComercial(idPessoa);
		cliente.setTpCliente(new DomainValue(String.valueOf(map.get("tpCliente"))));
		
		return cliente;
	
	}

	/**
	 * Obtem o municipio através do código do IBGE
	 *
	 * @param  cdIBGE
	 * @return Municipio
	 */
	private Municipio findMunicipio(Long cdIBGE, Long idUF){
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(getRecalculoFreteFlexDAO().findIdMunicipio(cdIBGE,idUF));
		return municipio;
	}

	/**
	 * Salva dados do recalculo através de informações
	 * arquivo importado
	 *
	 */
	public void storeDoctoRecalculo(final RecalculoFrete recalculoFrete, final Frete frete, final RecalculoFreteArquivoDTO recalculoFreteArquivo){
		Long idDoctoRecalculo = getRecalculoFreteDAO().storeDoctoRecalculo(recalculoFrete, frete, recalculoFreteArquivo);
		if( CollectionUtils.isNotEmpty(frete.getConhecimento().getParcelaDoctoServicos()) ){
			for(ParcelaDoctoServico pds : frete.getConhecimento().getParcelaDoctoServicos()){
				parcelaRecalculoService.storeParcelasRecalculo(idDoctoRecalculo, recalculoFrete.getIdRecalculoFrete(), pds.getParcelaPreco().getIdParcelaPreco(), pds.getVlParcela());
			}
		}
	}

	public List<RecalculoFreteArquivoDTO> getFile(String host, String username, String password, String folder){

		FTPClient ftp = new FTPClient();
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		ftp.configure(conf);
		
		List<RecalculoFreteArquivoDTO> list = new ArrayList<RecalculoFreteArquivoDTO>();

		try {
			try {
				ftp.connect(host);
				checkReply(ftp, "FTP server refused connection.");

				ftp.login(username, password);
				checkReply(ftp, "FTP server refused login.");

				ftp.changeWorkingDirectory(folder);
				FTPFile[] files = ftp.listFiles("*.csv");
				checkReply(ftp, "FTP server refused listing names.");

				for (FTPFile file: files) {
					try {
						if (file.isFile() && file.getName().toLowerCase().endsWith("csv")) {

							if(file.getName().toLowerCase().contains("recalculo.csv")){
								InputStream is = ftp.retrieveFileStream("recalculo.csv");
								
								BufferedReader reader = new BufferedReader(new InputStreamReader(is));
								String line = null;
								int count = 0;
								while((line = reader.readLine()) != null){

									count++;
									if(count == 1 || line.split(RecalculoFreteArquivoDTO.DEFAULT_SEPARATOR).length != 29){
										continue;
									}
									
									if(!line.contains(RecalculoFreteArquivoDTO.DEFAULT_SEPARATOR)){
										throw new BusinessException("Layout do arquivo csv inválido, o mesmo deve possuir os dados separados por ; ");
									}

									list.add(new RecalculoFreteArquivoDTO(line));
								}
							}

						}
					} catch (Throwable e) {
						throw new BusinessException("Excessao ao processar arquivo do FTP");
					}
				}
			} finally {
				ftp.disconnect();
			}
			
			if(list.isEmpty()){
				throw new BusinessException("Arquivo recalculo.csv não encontrado no FTP ou está vazio");
			}
			
			return list;
		} catch (Exception e) {
			throw new BusinessException("Excessao na conexao com o FTP");
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeProcessamentoItemArquivo(final RecalculoFrete recalculoFrete, final RecalculoFreteArquivoDTO recalculo) {
		Conhecimento ctrc = new Conhecimento();
		ctrc.setPsReal(recalculo.getPsReal());
		ctrc.setPsAferido(recalculo.getPsAferido());
		ctrc.setPsAforado(recalculo.getPsCubado());
		ctrc.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		ctrc.setTpDocumentoServico(TP_DOCUMENTO);
		ctrc.setTpDoctoServico(TP_DOCUMENTO);
		ctrc.setVlMercadoria(recalculo.getVlMercadoria());
		ctrc.setQtVolumes(recalculo.getQtVolumes());
		ctrc.setTpConhecimento(TP_CONHECIMENTO);
		ctrc.setTpCalculoPreco(TP_CALCULO_PRECO);
		ctrc.setTpFrete(new DomainValue(recalculo.getTpFrete()));
		ctrc.setTpDevedorFrete(new DomainValue(recalculo.getTpDevedorFrete()));
		ctrc.setBlReembolso(Boolean.FALSE);
		ctrc.setBlIndicadorEdi(Boolean.FALSE);
		ctrc.setDensidade(new Densidade());
		ctrc.setBlIndicadorEdi(Boolean.FALSE);
		
		NaturezaProduto naturezaProduto = naturezaProdutoService.findById(11l);
		
		ctrc.setNaturezaProduto(naturezaProduto);
		
		ctrc.setServico(servicoService.findServicoBySigla(recalculo.getTpServico()));
		
		DoctoServicoDadosCliente dadosCliente = new DoctoServicoDadosCliente();
		
		/*Remetente*/
		Cliente remetente = findDadosCliente(recalculo.getNrIdentificacaoRemetente(), recalculo.getCdIBGEMunicipioOrigem(), recalculo.getSgEstadoOrigem());
		if(remetente == null || remetente.getIdCliente() == null){
			throw new BusinessException("LMS-01180");
		}
		
		dadosCliente.setTpSituacaoTributariaRemetente(recalculo.getTpSituacaoTributariaRemetente());
		
		ctrc.setClienteByIdClienteRemetente(remetente);
		ctrc.setMunicipioByIdMunicipioColeta(remetente.getPessoa().getEnderecoPessoa().getMunicipio());
		
		/*Filial origem*/
		Filial filialOrigem = findFilial(remetente.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio());
		ctrc.setFilialByIdFilialOrigem(filialOrigem);
		
		/*Destinatário*/
		Cliente destinatario = findDadosCliente(recalculo.getNrIdentificacaoDestinatario(),recalculo.getCdIBGEMunicipioDestino(),recalculo.getSgEstadoDestino());
		if(destinatario == null || destinatario.getIdCliente() == null){
			throw new BusinessException("LMS-01184");
		}
		
		dadosCliente.setTpSituacaoTributariaDestinatario(recalculo.getTpSituacaoTributariaDestinatario());
		
		ctrc.setClienteByIdClienteDestinatario(destinatario);
		ctrc.setMunicipioByIdMunicipioEntrega(destinatario.getPessoa().getEnderecoPessoa().getMunicipio());
		
		if (ctrc.getTpFrete() != null){
			if (ctrc.getTpFrete().getValue().equals("C")){
				ctrc.setClienteByIdClienteBaseCalculo(ctrc.getClienteByIdClienteRemetente());
			}else{
				ctrc.setClienteByIdClienteBaseCalculo(ctrc.getClienteByIdClienteDestinatario());
			}
		}
		
		/*Filial destino*/
		Filial filialDestino = findFilial(destinatario.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio());
		ctrc.setFilialByIdFilialDestino(filialDestino);
		
		/*Devedor do frete*/
		DevedorDocServ devedor = new DevedorDocServ();
		devedor.setDoctoServico(ctrc);
		if(ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(recalculo.getTpDevedorFrete())){
			devedor.setCliente(remetente);
			devedor.setFilial(filialOrigem);
		}else{
			devedor.setCliente(destinatario);
			devedor.setFilial(filialDestino);
		}
		List<DevedorDocServ> listDevedor = new ArrayList<DevedorDocServ>();
		listDevedor.add(devedor);
		ctrc.setDevedorDocServs(listDevedor);
		
		/*Informa os dados do cliente*/
		ctrc.setDadosCliente(dadosCliente);
		
		/* Busca a divisao do cliente ou o parametro padrao no caso de cliente potencial ou eventual*/
		Long idDivisaoCliente = null;
		if (devedor.getCliente() != null){
			List idsDivisaoCliente = findIdsDivisaoCliente(devedor.getCliente().getPessoa().getIdPessoa());
			if (idsDivisaoCliente != null && !idsDivisaoCliente.isEmpty()){
				if(idsDivisaoCliente.get(0) instanceof BigDecimal){
					idDivisaoCliente = ((BigDecimal) idsDivisaoCliente.get(0)).longValue();
				} else {
					Map data = (Map) idsDivisaoCliente.get(0);
					idDivisaoCliente = ((BigDecimal) data.get("ID_DIVISAO_CLIENTE")).longValue();
				}
			}
		}
		//Se nao existe divisao para o cliente, trata como eventual utilizando os parametros padrao.
		if (idDivisaoCliente == null){
			ctrc.setParametroCliente(ParametroClienteUtils.getParametroClientePadrao());
		}else{
			/*se existe divisao, passa ela para o ctrc que o algoritmo de calculo ira buscar o parametro
			 * correto baseado no id desta divisao.*/
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(idDivisaoCliente);
			ctrc.setDivisaoCliente(divisaoCliente);
		}
		
		/*Armazena informações do recálculo de frete*/
		Frete frete = calcularFreteService.executeRecalculoFreteTabela(ctrc, false);
		
		/*Salva as informações na tabela DOCTO_RECALCULO*/
		storeDoctoRecalculo(recalculoFrete, frete, recalculo);
	}

	public List findIdsDivisaoCliente(Long idCliente){
		return getRecalculoFreteDAO().findIdsDivisaoCliente(idCliente);
	}

	private void checkReply(FTPClient ftp, String message) throws IOException {
		String replyString = ftp.getReplyString();
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			throw new IOException("Erro na conexao FTP (" + message + ")  " + reply + " - " + replyString);
		}
	}

	/**
	 * Obtem o numero do processo em recálculo
	 *
	 * @return Long
	 */
	public Long generateNrProcessoRecalculo(){
		ParametroGeral param = parametroGeralService.findByNomeParametro("PROCESSO_RECALCULO", true);

		Long nrProcesso = LongUtils.getLong(param.getDsConteudo()); 
		nrProcesso += 1;

		param.setDsConteudo(nrProcesso.toString());

		parametroGeralService.store(param);

		return nrProcesso;
	}

	/**
	 * Obtem a lista de CTRC que devem ser recalculados
	 * através dos parametros passados
	 *
	 * @param rec
	 * @return
	 */
	public List<ListOrderedMap> findDocsRecalculo(RecalculoFrete rec){
		return getRecalculoFreteFlexDAO().findDocsRecalculo(rec);
	}

	/**
	 * Obtem o Sub Tipo tabela Preco
	 * @param parameters
	 * @return
	 */
	public List<Map> findByTpTipoTabelaPreco(Map<String, Object> parameters){
		return subtipoTabelaPrecoService.findByTpTipoTabelaPreco(parameters);
	}

	/**
	 * Salva os clientes a processar
	 * @param clienteProcessar
	 */
	public void storeClienteProcessar(ClienteProcessar clienteProcessar){
		getClienteProcessarDAO().store(clienteProcessar);
	}

	/**
	 * Salva os clientes a não processar
	 * @param clienteNaoProcessar
	 */
	public void storeClienteNaoProcessar(ClienteNaoProcessar clienteNaoProcessar){
		getClienteNaoProcessarDAO().store(clienteNaoProcessar);
	}

	/**
	 * Gera os resultados do recalculo
	 *
	 * @param idRecalculoFrete
	 */
	public String executeReport(Long idRecalculoFrete) {

		StringBuilder dataFile = new StringBuilder();

		List<Object[]> list = getRecalculoFreteDAO().findDocRecalculoByRecalculoFrete(idRecalculoFrete);
		if(CollectionUtils.isNotEmpty(list)){
			dataFile.append("Origem;Documento;Município Destinatário;CNPJ;Cliente Remetente;Peso Declarado;Peso Cubado;Peso Aferido;Peso Faturado;Volumes;Valor Mercadoria;Valor Frete;Valor Parcela;Parcela Preço\n");
			for (Object[] dt : list) {
				dataFile.append(dt[0]).append(";").append(dt[1]).append(";").append(dt[2]).append(";").append(dt[3]).append(";").append(dt[4]).append(";"+dt[5]).append(";"+dt[6]).append(";"+dt[7]).append(";"+dt[8]).append(";").append(dt[9]).append(";").append(dt[10]).append(";").append(dt[11]).append(";").append(dt[12]).append(";").append(dt[13]).append("\n");
			}
		}

		return dataFile.toString();
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}


	public MunicipioService getMunicipioService() {
		return municipioService;
	}


	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}


	public CalcularFreteService getCalcularFreteService() {
		return calcularFreteService;
	}


	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}


	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public SubtipoTabelaPrecoService getSubtipoTabelaPrecoService() {
		return subtipoTabelaPrecoService;
	}

	public void setSubtipoTabelaPrecoService(
			SubtipoTabelaPrecoService subtipoTabelaPrecoService) {
		this.subtipoTabelaPrecoService = subtipoTabelaPrecoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public RecalculoFreteFlexDAO getRecalculoFreteFlexDAO() {
		return recalculoFreteFlexDAO;
	}

	public void setRecalculoFreteFlexDAO(RecalculoFreteFlexDAO recalculoFreteFlexDAO) {
		this.recalculoFreteFlexDAO = recalculoFreteFlexDAO;
	}

	public RecalculoFreteDAO getRecalculoFreteDAO() {
		return recalculoFreteDAO;
	}

	public void setRecalculoFreteDAO(RecalculoFreteDAO recalculoFreteDAO) {
		this.recalculoFreteDAO = recalculoFreteDAO;
	}

	public ClienteProcessarDAO getClienteProcessarDAO() {
		return clienteProcessarDAO;
	}

	public void setClienteProcessarDAO(ClienteProcessarDAO clienteProcessarDAO) {
		this.clienteProcessarDAO = clienteProcessarDAO;
	}

	public ClienteNaoProcessarDAO getClienteNaoProcessarDAO() {
		return clienteNaoProcessarDAO;
	}

	public void setClienteNaoProcessarDAO(
			ClienteNaoProcessarDAO clienteNaoProcessarDAO) {
		this.clienteNaoProcessarDAO = clienteNaoProcessarDAO;
	}

	public void setParcelaRecalculoService(
			ParcelaRecalculoService parcelaRecalculoService) {
		this.parcelaRecalculoService = parcelaRecalculoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setConhecimentoNormalService(
			ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}

	public void setDocumentoServicoFacade(DocumentoServicoFacade documentoServicoFacade) {
		this.documentoServicoFacade = documentoServicoFacade;
	}

	public void setNaturezaProdutoService(
			NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

}