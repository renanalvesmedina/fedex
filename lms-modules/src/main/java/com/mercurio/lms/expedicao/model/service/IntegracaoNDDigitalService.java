package com.mercurio.lms.expedicao.model.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.financeiro.DoctoFaturaDMN;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ConnectorIntegrationCTE;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.dao.ConhecimentoDAO;
import com.mercurio.lms.expedicao.model.dao.ConnectorIntegrationCTEDAO;
import com.mercurio.lms.expedicao.model.dao.MonitoramentoDocEletronicoDAO;
import com.mercurio.lms.expedicao.model.dao.TbDatabaseInputCTEDAO;
import com.mercurio.lms.expedicao.util.EnderecoPessoaUtils;
import com.mercurio.lms.expedicao.util.XMLUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ManterParametrizacaoEnvioService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;


/**
 * @author JonasFE
 *         <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.integracaoNDDigitalService"
 */
public class IntegracaoNDDigitalService extends CrudService<TBDatabaseInputCTE, Long> {

    private static final Logger LOGGER = LogManager.getLogger(IntegracaoNDDigitalService.class);

    private static final int SCALE = 4;
    private static final String DADOS_ADIC = "</dadosAdic>";
    private static final String UTF_8 = "UTF-8";
	private TbDatabaseInputCTEDAO tbDatabaseInputCTEDAO;
    private MonitoramentoDocEletronicoDAO monitoramentoDocEletronicoDAO;
	private ConnectorIntegrationCTEDAO connectorIntegrationCTEDAO;
	private ParametroGeralService parametroGeralService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ContatoService contatoService;
	private ManterParametrizacaoEnvioService manterParametrizacaoEnvioService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private FilialService filialService;
	private ConhecimentoDAO conhecimentoDAO;

    private static final DecimalFormat formatQuatroDecimais = new DecimalFormat("0.0000", DecimalFormatSymbols.getInstance(new Locale("pt", "BR")));

	public TBDatabaseInputCTE generateIntegracaoEmissao(final MonitoramentoDocEletronico monitoramentoDocEletronico, final String xml){
		TBDatabaseInputCTE tbDatabaseInputCTE = new TBDatabaseInputCTE();
        tbDatabaseInputCTE.setDocumentData(xml.getBytes(Charset.forName(UTF_8)));
		tbDatabaseInputCTE.setKind(1);
		
		//LMS-4210
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		String job = monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial();		
		job = job.concat(getValorJobCTe(filialUsuarioLogado));
		
		tbDatabaseInputCTE.setJob(job);
		tbDatabaseInputCTE.setStatus(0);
		tbDatabaseInputCTEDAO.store(tbDatabaseInputCTE);
		return tbDatabaseInputCTE;
	}

	private TBDatabaseInputCTE generateIntegracaoMdfe(final String sgFilial, String sufixo, final String xml, Boolean hasContingenciaValidaByFilial){
        Filial filial = filialService.findBySgFilialAndIdEmpresa(sgFilial,
                (SessionUtils.getEmpresaSessao() != null) ? SessionUtils.getEmpresaSessao().getIdEmpresa() : Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_EMPRESA_MERCURIO")));

		StringBuilder nmJob = new StringBuilder();
		nmJob.append(sgFilial);
		nmJob.append(sufixo);
		nmJob.append(getValorJobMDFE(filial));
		
	    TBDatabaseInputCTE tbDatabaseInputCTE = new TBDatabaseInputCTE();
        tbDatabaseInputCTE.setDocumentData(xml.getBytes(Charset.forName(UTF_8)));
	    tbDatabaseInputCTE.setKind(1);
	    tbDatabaseInputCTE.setJob(nmJob.toString());
	    tbDatabaseInputCTE.setStatus(hasContingenciaValidaByFilial ? 1 : 0);
	    tbDatabaseInputCTEDAO.store(tbDatabaseInputCTE);
	    return tbDatabaseInputCTE;
	}
	
	public TBDatabaseInputCTE generateIntegracaoEmissaoMdfe(final String sgFilial, final String xml, Boolean hasContingenciaValidaByFilial){
	    return generateIntegracaoMdfe(sgFilial, "_MDFe", xml, hasContingenciaValidaByFilial);
	}
	
	public TBDatabaseInputCTE generateIntegracaoEventoMdfe(final String sgFilial, final String xml){
	    return generateIntegracaoMdfe(sgFilial, "_MDFe_EV", xml, false);
	}
	
	public TBDatabaseInputCTE generateIntegracaoAbortMdfe(final String sgFilial, final String xml){
		return generateIntegracaoMdfe(sgFilial, "_MDFe", xml, false);
	}
	
	public TBDatabaseInputCTE generateIntegracaoInutilizacao(final MonitoramentoDocEletronico monitoramentoDocEletronico, final String xml){
		TBDatabaseInputCTE tbDatabaseInputCTE = new TBDatabaseInputCTE();
		
		//LMS-4210
		Filial filial = monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem();
		String job = filial.getSgFilial();		
		job = job.concat(getValorJobCTe(filial));
		
        tbDatabaseInputCTE.setDocumentData(xml.getBytes(Charset.forName(UTF_8)));
		tbDatabaseInputCTE.setKind(1);
		tbDatabaseInputCTE.setJob(job);
		tbDatabaseInputCTE.setStatus(0);
		tbDatabaseInputCTEDAO.store(tbDatabaseInputCTE);
		return tbDatabaseInputCTE;
	}

	public TBDatabaseInputCTE generateIntegracaoCancelamento(final MonitoramentoDocEletronico monitoramentoDocEletronico, final String xml){
		TBDatabaseInputCTE tbDatabaseInputCTE = new TBDatabaseInputCTE();
		
		//LMS-4210
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		String job = monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial();
		job = job.concat(getValorJobCancelaCTe(filialUsuarioLogado));
		
        tbDatabaseInputCTE.setDocumentData(xml.getBytes(Charset.forName(UTF_8)));
		tbDatabaseInputCTE.setKind(1);
		tbDatabaseInputCTE.setJob(job);
		tbDatabaseInputCTE.setStatus(0);
		tbDatabaseInputCTEDAO.store(tbDatabaseInputCTE);
		return tbDatabaseInputCTE;
	}

	/**
	 * Grava dados da Carta de Correção na tabela TBDATABASEINPUT_CTE, conforme
	 * abaixo:
	 *   - ID = gerado a partir da sequence TBDATABASEINPUT_CTE_SQ
	 *   - DOCUMENTDATA = Dados do arquivo XML
	 *   - STATUS = Zero
	 *   - JOB = FILIAL.SG_FILIAL da filial do usuário + conteúdo do parâmetro
	 *     da filial do usuário logado "JOB_CANCELA_CTE" (quando o parâmetro
	 *     existir para a filial)
	 *   - DOCUMENTUSER = null
	 *   - KIND = "1"
	 * 
     * @param monitoramentoDocEletronico Monitoramento de documentos eletrônicos
     * @param xml                        Conteúdo XML da Carta de Correção
	 * @return Registro persistido na tabela TBDATABASEINPUT_CTE
	 */
	public TBDatabaseInputCTE generateIntegracaoCCE(final MonitoramentoDocEletronico monitoramentoDocEletronico, final String xml) {
		TBDatabaseInputCTE tbDatabaseInputCTE = new TBDatabaseInputCTE();
		
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		Filial filialOrigem = monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem();
		String job = filialOrigem.getSgFilial() + getValorJobCancelaCTe(filialUsuarioLogado);
		
        tbDatabaseInputCTE.setDocumentData(xml.getBytes(Charset.forName(UTF_8)));
		tbDatabaseInputCTE.setStatus(0);
		tbDatabaseInputCTE.setJob(job);
		tbDatabaseInputCTE.setKind(1);
		tbDatabaseInputCTEDAO.store(tbDatabaseInputCTE);
		return tbDatabaseInputCTE;
	}
	
	/**
	 * Adiciona campos que não pertencem ao XSD da receita. NDDigital não tem capacidade fornecer um XSD
	 *
	 * @param xml
	 * @return
	 */
	public StringBuffer executeAdicionarB2B(final StringBuffer xml, final Conhecimento conhecimento){

		final StringBuilder dadosAdic = adicionaDadosAdic(conhecimento);

		int index = xml.indexOf("</infCte>");
		xml.insert(index, dadosAdic);
		return xml;
	}


	/**
	 * Bloco extraído do método acima para permitir o retorno apenas das tag, que podem ser usadas 
	 * para geração do PDF, DACTE, por ex.
	 * 
	 * @param conhecimento
	 * @return tags adicionais
	 */
	public StringBuilder adicionaDadosAdic(Conhecimento conhecimento) {
		String pastaCteB2B = (String) parametroGeralService.findConteudoByNomeParametro("B2B_PASTA_XML_CTE", false);
		Cliente cliente = conhecimento.getDevedorDocServs().get(0).getCliente();
		
		StringBuilder dadosAdic = new StringBuilder();
		dadosAdic.append("<dadosAdic>");

		List<Contato> listContatos = contatoService.findContatosByIdPessoaTpContato(cliente.getIdCliente(), "CT");

		String obrigaPdfXmlGeral = (String) parametroGeralService.findConteudoByNomeParametro("OBRIGA_PDF_CTE",false);
		String obrigaPdfXmlFilial = "N";
		
		if(null != SessionUtils.getFilialSessao()) { 
			obrigaPdfXmlFilial = (String) conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "OBRIGA_PDF_CTE", false);
		}	
		
		List clientesEnvio = manterParametrizacaoEnvioService.findByCliente(cliente.getIdCliente());
		
		if (!(CollectionUtils.isNotEmpty(clientesEnvio) ||
				"S".equals(obrigaPdfXmlGeral) ||
				"S".equals(obrigaPdfXmlFilial) ||
				"E".equals(cliente.getTpCliente().getValue()) || "P".equals(cliente.getTpCliente().getValue()) ||
				"F".equals(conhecimento.getTpFrete().getValue())
				)){
            if (listContatos != null && !listContatos.isEmpty()) {
			for(Contato contato : listContatos){
			dadosAdic.append("<B2B>");
				dadosAdic.append(contato.getDsEmail());
				dadosAdic.append("</B2B>");
			}
		}else if(cliente.getPessoa().getDsEmail() != null){
			dadosAdic.append("<B2B>");
			dadosAdic.append(cliente.getPessoa().getDsEmail());
			dadosAdic.append("</B2B>");
		}
		}

		if (StringUtils.isNotEmpty(pastaCteB2B)){
			dadosAdic.append("<B2BDirectory>");
			dadosAdic.append(pastaCteB2B);
			dadosAdic.append("</B2BDirectory>");
		}

		dadosAdic.append("<FUNCAOCTE>");
		dadosAdic.append(conhecimento.getTpConhecimento().getValue());
		dadosAdic.append("</FUNCAOCTE>");
		dadosAdic.append("<SERVCTE>");
		if(conhecimento.getServico().getDsServico().toString().length() > 35){
			dadosAdic.append(conhecimento.getServico().getDsServico().toString().substring(0, 35));
		} else {
			dadosAdic.append(conhecimento.getServico().getDsServico().toString());
		}
		dadosAdic.append("</SERVCTE>");
		if(conhecimento.getFluxoFilial() != null){
			dadosAdic.append("<FLUXOCARGA>");
			if(conhecimento.getFluxoFilial().getDsFluxoFilial().length() > 30){
				dadosAdic.append(conhecimento.getFluxoFilial().getDsFluxoFilial().substring(0, 30));
		} else {
			dadosAdic.append(conhecimento.getFluxoFilial().getDsFluxoFilial());
		}
			dadosAdic.append("</FLUXOCARGA>");
		}
		dadosAdic.append("<NOSSONUMERO>");
		dadosAdic.append(StringUtils.leftPad(String.valueOf(conhecimento.getNrDoctoServico()), 9, '0'));
		dadosAdic.append("</NOSSONUMERO>");
		if(conhecimento.getTabelaPreco() != null && conhecimento.getTarifaPreco() != null){
			dadosAdic.append("<TABPRECO>");
			dadosAdic.append(conhecimento.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
			dadosAdic.append(FormatUtils.formatDecimal("00", conhecimento.getTabelaPreco().getTipoTabelaPreco().getNrVersao()));
			dadosAdic.append('-');
			dadosAdic.append(conhecimento.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
			dadosAdic.append('/');
			dadosAdic.append(conhecimento.getTarifaPreco().getCdTarifaPreco());
			dadosAdic.append("</TABPRECO>");
		}
		BigDecimal psAforado = conhecimento.getPsAforado() == null ? BigDecimal.ZERO : conhecimento.getPsAforado();
		dadosAdic.append("<CUBAGEM>");
		
		if(conhecimento.getDivisaoCliente() != null){
			/*Obtem a tabela divisao cliente*/
			TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(conhecimento.getDivisaoCliente().getIdDivisaoCliente(), conhecimento.getServico().getIdServico());
			
			/*Verifica se o atributo NrFatorCubagem possue valor e cálcula
			 * o pesoAforado. Valor obtido no volumes X nrFatorCubagem / 1000000 */
			if(tdc != null && BigDecimalUtils.hasValue(tdc.getNrFatorCubagem())){
                BigDecimal cubagem = psAforado.divide(tdc.getNrFatorCubagem(), SCALE, BigDecimal.ROUND_FLOOR);
				dadosAdic.append(formatQuatroDecimais.format(cubagem));
			}else{
				if("R".equals(conhecimento.getServico().getTpModal().getValue())){
					BigDecimal pesoMetragemCubicaRodoviario = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("FATOR_CUBAGEM_PADRAO_RODO", false);
                    BigDecimal cubagem = psAforado.divide(pesoMetragemCubicaRodoviario, SCALE, BigDecimal.ROUND_FLOOR);
					dadosAdic.append(formatQuatroDecimais.format(cubagem));
				}else if("A".equals(conhecimento.getServico().getTpModal().getValue())){
					BigDecimal pesoMetragemCubicaRodoviario = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("FATOR_CUBAGEM_PADRAO_AEREO", false);
                    BigDecimal cubagem = psAforado.divide(pesoMetragemCubicaRodoviario, SCALE, BigDecimal.ROUND_FLOOR);
					dadosAdic.append(formatQuatroDecimais.format(cubagem));
				} else {
					throw new java.lang.IllegalStateException("[CTE] tpModal nao configurado para CTE");
				}
			}
		}else{
			if("R".equals(conhecimento.getServico().getTpModal().getValue())){
				BigDecimal pesoMetragemCubicaRodoviario = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("FATOR_CUBAGEM_PADRAO_RODO", false);
                BigDecimal cubagem = psAforado.divide(pesoMetragemCubicaRodoviario, SCALE, BigDecimal.ROUND_FLOOR);
				dadosAdic.append(formatQuatroDecimais.format(cubagem));
			}else if("A".equals(conhecimento.getServico().getTpModal().getValue())){
				BigDecimal pesoMetragemCubicaRodoviario = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("FATOR_CUBAGEM_PADRAO_AEREO", false);
                BigDecimal cubagem = psAforado.divide(pesoMetragemCubicaRodoviario, SCALE, BigDecimal.ROUND_FLOOR);
				dadosAdic.append(formatQuatroDecimais.format(cubagem));
			} else {
				throw new java.lang.IllegalStateException("[CTE] tpModal nao configurado para CTE");
			}
		}

		dadosAdic.append("</CUBAGEM>");

		BigDecimal psDeclarado = conhecimento.getPsReal() == null ? BigDecimal.ZERO : conhecimento.getPsReal();
		BigDecimal psCubado = conhecimento.getPsAforado() == null ? BigDecimal.ZERO : conhecimento.getPsAforado();
		BigDecimal psAferido = conhecimento.getPsAferido() == null ? BigDecimal.ZERO : conhecimento.getPsAferido();
		BigDecimal psFaturado = conhecimento.getPsReferenciaCalculo() == null ? BigDecimal.ZERO : conhecimento.getPsReferenciaCalculo();
		
		BigDecimal cubagemDeclarada = conhecimento.getNrCubagemDeclarada() == null ? BigDecimal.ZERO : conhecimento.getNrCubagemDeclarada();
		BigDecimal cubadoDeclarado = conhecimento.getPsCubadoDeclarado() == null ? BigDecimal.ZERO : conhecimento.getPsCubadoDeclarado();
		BigDecimal cubagemAferida = conhecimento.getNrCubagemAferida() == null ? BigDecimal.ZERO : conhecimento.getNrCubagemAferida();
		BigDecimal cubadoAferido = conhecimento.getPsCubadoAferido() == null ? BigDecimal.ZERO : conhecimento.getPsCubadoAferido();
		BigDecimal cubagemAforada = conhecimento.getNrCubagemCalculo() == null ? BigDecimal.ZERO : conhecimento.getNrCubagemCalculo();
		BigDecimal cubadoAforado = conhecimento.getPsAforado() == null ? BigDecimal.ZERO : conhecimento.getPsAforado();
		
		Integer qtVolume = conhecimento.getQtVolumes() == null ? 0 : conhecimento.getQtVolumes();
		dadosAdic.append("<PSDECLARADO>");
		dadosAdic.append(formatQuatroDecimais.format(psDeclarado));
		dadosAdic.append("</PSDECLARADO>");
		dadosAdic.append("<PSCUBADO>");
		dadosAdic.append(formatQuatroDecimais.format(psCubado));
		dadosAdic.append("</PSCUBADO>");
		dadosAdic.append("<PSAFERIDO>");
		dadosAdic.append(formatQuatroDecimais.format(psAferido));
		dadosAdic.append("</PSAFERIDO>");
		dadosAdic.append("<PSFATURADO>");
		dadosAdic.append(formatQuatroDecimais.format(psFaturado));
		dadosAdic.append("</PSFATURADO>");
		dadosAdic.append("<QTVOLUME>");
		dadosAdic.append(qtVolume);
		dadosAdic.append("</QTVOLUME>");
		
		dadosAdic.append("<CUBAGEMDECLARADA>");
        dadosAdic.append(formatQuatroDecimais.format(cubagemDeclarada));
        dadosAdic.append("</CUBAGEMDECLARADA>");
        
        dadosAdic.append("<CUBADODECLARADO>");
        dadosAdic.append(formatQuatroDecimais.format(cubadoDeclarado));
        dadosAdic.append("</CUBADODECLARADO>");
        
        dadosAdic.append("<CUBAGEMAFERIDA>");
        dadosAdic.append(formatQuatroDecimais.format(cubagemAferida));
        dadosAdic.append("</CUBAGEMAFERIDA>");
        
        dadosAdic.append("<CUBADOAFERIDO>");
        dadosAdic.append(formatQuatroDecimais.format(cubadoAferido));
        dadosAdic.append("</CUBADOAFERIDO>");
        
        dadosAdic.append("<CUBAGEMAFORADA>");
        dadosAdic.append(formatQuatroDecimais.format(cubagemAforada));
        dadosAdic.append("</CUBAGEMAFORADA>");
        
        dadosAdic.append("<CUBADOAFORADO>");
        dadosAdic.append(formatQuatroDecimais.format(cubadoAforado));
        dadosAdic.append("</CUBADOAFORADO>");
        
    	dadosAdic.append("<NUMEROCAE>");
        dadosAdic.append(StringUtils.isNotBlank(conhecimento.getNrCae()) ? formataCae(conhecimento.getNrCae(), conhecimento.getFilialOrigem().getSgFilial()) : "");
        dadosAdic.append("</NUMEROCAE>");
		
		
		if(conhecimento.getDsEnderecoEntrega() != null){
			dadosAdic.append(buildLocEnt(conhecimento));
		}
		dadosAdic.append(buildForPag(conhecimento));
        dadosAdic.append(DADOS_ADIC);
		
		
		return dadosAdic;
	}
	
	private String formataCae(String cae, String siglaFilial) {
		return siglaFilial + " " + StringUtils.leftPad(cae, 8, "0");
	}

	private String buildForPag(Conhecimento conhecimento) {
		StringBuilder dadosAdic = new StringBuilder();
		dadosAdic.append("<forPag>");
		if("C".equals(conhecimento.getTpFrete().getValue())){
			dadosAdic.append("0");
		} else if("F".equals(conhecimento.getTpFrete().getValue())){
			dadosAdic.append("1");
		}
		dadosAdic.append("</forPag>");
		return dadosAdic.toString();
	}

	private String buildLocEnt(Conhecimento conhecimento) {
		StringBuilder dadosAdic = new StringBuilder();
		dadosAdic.append("<locEnt>");
		dadosAdic.append("<xLgr>");
		if(conhecimento.getTipoLogradouroEntrega() != null){
			dadosAdic.append(StringEscapeUtils.escapeXml(conhecimento.getTipoLogradouroEntrega().getDsTipoLogradouro() + " " + conhecimento.getDsEnderecoEntrega().trim()));
		}else{
			dadosAdic.append(StringEscapeUtils.escapeXml(conhecimento.getDsEnderecoEntrega().trim()));
		}
		dadosAdic.append("</xLgr>");
		dadosAdic.append("<nro>");
		if(conhecimento.getBlIndicadorEdi()){
			dadosAdic.append(EnderecoPessoaUtils.getNumeroEndereco(new StringBuilder(conhecimento.getDsEnderecoEntrega())).trim());
		}else{
			if(conhecimento.getNrEntrega() != null){
				dadosAdic.append(conhecimento.getNrEntrega().trim());
			}else{
				dadosAdic.append(EnderecoPessoaUtils.getNumeroEndereco(new StringBuilder(conhecimento.getDsEnderecoEntrega())).trim());
			}
		}
		dadosAdic.append("</nro>");
		dadosAdic.append("<xCpl>");
		if(conhecimento.getDsComplementoEntrega() != null){
			dadosAdic.append(StringEscapeUtils.escapeXml(conhecimento.getDsComplementoEntrega().trim()));
		}
		dadosAdic.append("</xCpl>");
		dadosAdic.append("<xBairro>");
		String dsBairro = conhecimento.getDsBairroEntrega();
		if(dsBairro == null){
			dadosAdic.append(parametroGeralService.findSimpleConteudoByNomeParametro("Bairro_padrao"));
		} else {
			dadosAdic.append(StringEscapeUtils.escapeXml(dsBairro.trim()));
		}
		dadosAdic.append("</xBairro>");
		dadosAdic.append("<xMun>");
		dadosAdic.append(StringEscapeUtils.escapeXml(conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipio()));
		dadosAdic.append("</xMun>");
		dadosAdic.append("<UF>");
		dadosAdic.append(conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getSgUnidadeFederativa());
		dadosAdic.append("</UF>");
		dadosAdic.append("</locEnt>");
		return dadosAdic.toString();
	}
	
	/**
     * Adiciona campos que não pertencem ao XSD da receita. NDDigital não tem capacidade fornecer um XSD
     *
     * @param xml
     * @return
     */
    public StringBuffer executeAdicionarB2BMDfe(final StringBuffer xml){
        String pastaMdfeB2B = (String) parametroGeralService.findConteudoByNomeParametro("B2B_PASTA_XML_MDFE", false);
        
        final StringBuilder dadosAdic = new StringBuilder();
        dadosAdic.append("<dadosAdic>");

        if (StringUtils.isNotEmpty(pastaMdfeB2B)){
            dadosAdic.append("<B2BDirectory>");
            dadosAdic.append(pastaMdfeB2B);
            dadosAdic.append("</B2BDirectory>");
        }

        dadosAdic.append(DADOS_ADIC);

        int index = xml.indexOf("</infMDFe>");
        xml.insert(index, dadosAdic);
        return xml;
    }
    
    private String getValorJobMDFE(Filial filialUsuarioLogado){
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findConteudoParametroFilial(filialUsuarioLogado.getIdFilial(), "JOB_MDFE", null);
		
		if(conteudoParametroFilial != null) {
			return conteudoParametroFilial.getVlConteudoParametroFilial();
		}	
		return "";
    }
    
    /**
     * Retorna o valor do parametro da filial JOB_CTE caso exista no banco, caso contrário
     * retorna vazio 
     *
     * @param filialUsuarioLogado
     * @return
     */
    private String getValorJobCTe(Filial filialUsuarioLogado) {
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findConteudoParametroFilial(filialUsuarioLogado.getIdFilial(), "JOB_CTE", null);
		
		if(conteudoParametroFilial != null) {
			return conteudoParametroFilial.getVlConteudoParametroFilial();
		}	
		return "";
    }
    
    /**
     * Retorna o valor do parametro da filial JOB_CANCELA_CTE caso exista no banco, caso contrário
     * retorna vazio 
     *
     * @param filialUsuarioLogado
     * @return
     */
    private String getValorJobCancelaCTe(Filial filialUsuarioLogado) {
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findConteudoParametroFilial(filialUsuarioLogado.getIdFilial(), "JOB_CANCELA_CTE", null);
    	
    	if(conteudoParametroFilial != null) {
			return conteudoParametroFilial.getVlConteudoParametroFilial();
		}	
		return "";
    }

	/**
	 * Busca as informações do CTE para impressão
     *
	 * @param idDoctoServico
	 * @return
	 */
	public Map<String, Object> findByDoctoServico(Long idDoctoServico) {

        MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoDAO.findMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);
        Map<String, Object> result = null;

        if (monitoramentoDocEletronico != null && monitoramentoDocEletronico.getIdEnvioDocEletronicoE() != null) {
            TBDatabaseInputCTE tbCTE = tbDatabaseInputCTEDAO.findByIdEnvioDocEletronicoE(monitoramentoDocEletronico.getIdEnvioDocEletronicoE());

            result = new HashMap<String, Object>();
            result.put("tpSituacaoDocumento", monitoramentoDocEletronico.getTpSituacaoDocumento().getValue());
            result.put("idConhecimento", idDoctoServico);
            result.put("nrProtocolo", monitoramentoDocEletronico.getNrProtocolo());

            if (tbCTE != null) {
                try {
                    return buildResultTbCTE(monitoramentoDocEletronico, result, tbCTE);
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new BusinessException(e.getMessage());
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    return null;
                }
            } else if (monitoramentoDocEletronico.getDsDadosDocumento() != null) {
                try {
                    StringBuilder xmlCTE = XMLUtils.buildXmlCTEAutenticadoSefaz(monitoramentoDocEletronico.getDsDadosDocumento());
                    result.put("xml", addNrProtocolo(xmlCTE, monitoramentoDocEletronico.getNrProtocolo()));
                    result.put("xmlOriginal", new String(monitoramentoDocEletronico.getDsDadosDocumento(), UTF_8));
                    return result;
                } catch (UnsupportedEncodingException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new BusinessException(e.getMessage());
                } catch (StringIndexOutOfBoundsException e) {
                    LOGGER.error(e.getMessage(), e);
                    return null;
                }

            }
        }
		if (result != null) {
			Map<String, Object> mapComplemento = conhecimentoDAO.findComplementosXMLCTE(idDoctoServico);
			result.put("complementoXML", mapComplemento);
		}


        return null;
    }

    private Map<String, Object> buildResultTbCTE(MonitoramentoDocEletronico monitoramentoDocEletronico, Map<String, Object> result, TBDatabaseInputCTE tbCTE) throws UnsupportedEncodingException {
        StringBuilder xml = new StringBuilder(new String(tbCTE.getDocumentData(), UTF_8));
        result.put("xml", addNrProtocolo(xml, monitoramentoDocEletronico.getNrProtocolo()));
        if (monitoramentoDocEletronico.getDsDadosDocumento() != null) {
            result.put("xmlOriginal", new String(monitoramentoDocEletronico.getDsDadosDocumento(), UTF_8));
        }
		return result;
	}

    public String findRpsByDoctoServico(Long idDoctoServico) {
        MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoDAO.findMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);
        if (monitoramentoDocEletronico != null) {
            try {
                String dsDadosDocumento = new String(monitoramentoDocEletronico.getDsDadosDocumento(), UTF_8);
                if (dsDadosDocumento.toLowerCase().indexOf("<rps>") > -1) {
                    return dsDadosDocumento;
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e);
            }
        }
        return null;
    }

    public void findRpsByDoctoFaturaDMN(DoctoFaturaDMN doctoFaturaDMN) {
        MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoDAO.findMonitoramentoDocEletronicoByIdDoctoServico(doctoFaturaDMN.getIdDoctoServico());
        if (monitoramentoDocEletronico != null) {
            try {
                doctoFaturaDMN = setXmlRpsByDoctoFaturaDMN(monitoramentoDocEletronico, doctoFaturaDMN);
            } catch (UnsupportedEncodingException e) {
                LOGGER.error(e);
            }
        }
    }

    public DoctoFaturaDMN setXmlRpsByDoctoFaturaDMN(MonitoramentoDocEletronico monitoramentoDocEletronico, DoctoFaturaDMN doctoFaturaDMN)throws UnsupportedEncodingException{
		String dsDadosDocumento = new String(monitoramentoDocEletronico.getDsDadosDocumento(), UTF_8);
		if (dsDadosDocumento.toLowerCase().indexOf("<rps>") > -1 && monitoramentoDocEletronico.getNrDocumentoEletronico() != null) {
			doctoFaturaDMN.setXmlSemAssinatura(dsDadosDocumento);
			doctoFaturaDMN.setNrDoctoEletronico(monitoramentoDocEletronico.getNrDocumentoEletronico().toString());
		}
		return doctoFaturaDMN;
	}

    /**
     * Adiciona o nrProtocolo ao xml
     * Optei por não utilizar parser de xml devido a performance
     */
    private String addNrProtocolo(StringBuilder xml, Long nrProtocolo) {
        int index = xml.indexOf(DADOS_ADIC);
        xml.insert(index, "<nrProtocolo>" + nrProtocolo + "</nrProtocolo>");

        return xml.toString();
    }

	public TBDatabaseInputCTE findXmlByIdDoctoServico(Long idDoctoServico) {
		return tbDatabaseInputCTEDAO.findXmlByIdDoctoServico(idDoctoServico);
	}	
	
	public void updateStatusConnectorIntegration(final Long connectorIntegrationCTEID) {
		connectorIntegrationCTEDAO.updateStatusConnectorIntegration(connectorIntegrationCTEID);
	}

	public void updateStatusReenvio(final Long idTBDatabaseInputCTE) {
		tbDatabaseInputCTEDAO.updateStatusReenvio(idTBDatabaseInputCTE);
	}
	
	public void removeByIdConnectorIntegration(ConnectorIntegrationCTE connectorIntegration) {
		connectorIntegrationCTEDAO.removeByIdConnectorIntegration(connectorIntegration);
	}

	public List<ConnectorIntegrationCTE> findRetornosPendentes(){
		return connectorIntegrationCTEDAO.findRetornosPendentes();
	}

	public void setTbDatabaseInputDAO(TbDatabaseInputCTEDAO tbDatabaseInputCTEDAO) {
		this.tbDatabaseInputCTEDAO = tbDatabaseInputCTEDAO;
	}

	public void setConnectorIntegrationDAO(ConnectorIntegrationCTEDAO connectorIntegrationCTEDAO) {
		this.connectorIntegrationCTEDAO = connectorIntegrationCTEDAO;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public void setManterParametrizacaoEnvioService(
			ManterParametrizacaoEnvioService manterParametrizacaoEnvioService) {
		this.manterParametrizacaoEnvioService = manterParametrizacaoEnvioService;
	}
	
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setConhecimentoDAO(ConhecimentoDAO conhecimentoDAO) {
		this.conhecimentoDAO = conhecimentoDAO;
	}

    public void setMonitoramentoDocEletronicoDAO(MonitoramentoDocEletronicoDAO monitoramentoDocEletronicoDAO) {
        this.monitoramentoDocEletronicoDAO = monitoramentoDocEletronicoDAO;
    }
}
