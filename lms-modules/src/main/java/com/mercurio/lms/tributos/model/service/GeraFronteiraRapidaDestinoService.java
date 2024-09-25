package com.mercurio.lms.tributos.model.service;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.tributos.model.dao.GeraFronteiraRapidaDestinoDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de serviço para CRUD: Não inserir documentação após ou remover a tag
 * do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.tributos.geraFronteiraRapidaDestinoService"
 */
public class GeraFronteiraRapidaDestinoService {
    
	private GeraFronteiraRapidaDestinoDAO geraFronteiraRapidaDestinoDAO;
	private InscricaoEstadualService inscricaoEstadualService;
    private ParametroGeralService parametroGeralService;   
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private EnderecoPessoaService enderecoPessoaService;
    private ConfiguracoesFacade configuracoesFacade;
    private EmpresaService empresaService;
    
    private ArrayList buffer  = null;
    private String dma = "ddMMyyyy";
    private String hora = "HHmmss";
    private String amd = "yyyyMMdd";
    
    private final String HEADER           = "01";
    private final String CONHECIMENTO     = "02";
    private final String NF               = "03";
    private final String FIM_CONHECIMENTO = "07";
    private final String MANIFESTO        = "08";
    private final String RODAPE           = "09";
    
    private final String MODELO_DOC_FISCAL= "08";
    
    private final boolean ESQUERDA = true;
    private final boolean DIREITA  = false;
    
    private final int TAMANHO_LINHA = 400;
   
	
	/**
     * Gera os dados para o arquivo de Fronteira Rápida
     * @author JoseMR
     * @param idManifesto - Manifesto de viagem nacional
     */
	public String generateFronteiraRapidaDestino(Long idManifesto){
        
        buffer = new ArrayList();
        StringBuffer linha = new StringBuffer();

		Long idCliente         = null;
        Long   nrIdentificacao = null;
        String nomeArquivo     = null;
        String diretorio       = null;
        List dadosPrincipais   = null;
        Map map                = null;
        String vlCampoComplementar = null;     
        String cdEmpFronteiraRapida = null;
        
        
        Empresa empresa        = null;
        InscricaoEstadual ie   = null;
        
        int qtdRegistros       = 0;
		
		// Regra 1 - Dados principais
        dadosPrincipais = getGeraFronteiraRapidaDestinoDAO().findInformacoesPrincipais(idManifesto);
        
        //se não retornou dados não gera o arquivo
        if( dadosPrincipais == null || dadosPrincipais.isEmpty() ){
        	throw new BusinessException("LMS-23013");
        }
        
        map = (Map) dadosPrincipais.get(0);
        
		// Busca o valor do campo complementar para fazer parte do nome do arquivo a ser gerado
        vlCampoComplementar = getGeraFronteiraRapidaDestinoDAO().findInformacoesNomeArquivo((Long) map.get("idCliente"));

        //se não retornou dados não gera o arquivo            
        if( vlCampoComplementar == null ){
        	throw new BusinessException("LMS-23014");
        }
        
        empresa =  (Empresa) SessionContext.get(SessionKey.EMPRESA_KEY);
        
        empresa = empresaService.findById(empresa.getIdEmpresa());
        
        cdEmpFronteiraRapida = (String)configuracoesFacade.getValorParametro((Long)map.get("idFilialDestino"), "CD_EMP_FRONT_RAPIDA");

        diretorio = (String)configuracoesFacade.getValorParametro("DIRETORIO_FRONTEIRA_RAPIDA");
        
        String nrManifesto = FormatUtils.completaDados((String) map.get("nrManifestoOrigem"),"0",8,0,ESQUERDA);
        String sgFilialOrigem = (String) map.get("sgFilialOrigem");
        
        nomeArquivo = vlCampoComplementar + nrManifesto + sgFilialOrigem + ".txt";
        
        // Montagem do Header do arquivo
        
        linha.append(HEADER);                                               						  //TAMANHO : 2   EXEMPLO : 01
        linha.append(JTDateTimeUtils.getDataAtual().toString(dma));                                   //TAMANHO : 8   EXEMPLO : DDMMYYYY
        linha.append(JTDateTimeUtils.getDataHoraAtual().toString(hora));                              //TAMANHO : 6   EXEMPLO : HHMMSS
        linha.append(FormatUtils.completaDados((String) map.get("pfNrIdentificacao")    , " ", 19, 0, DIREITA));  //TAMANHO : 19
        linha.append(FormatUtils.completaDados(empresa.getPessoa().getNmPessoa()        , " ", 40, 0, DIREITA));  //TAMANHO : 40
        linha.append(FormatUtils.completaDados(cdEmpFronteiraRapida						, " ",  3, 0, DIREITA));  //TAMANHO : 3
        linha.append(FormatUtils.completaDados((String) map.get("pcNmPessoa")           , " ", 40, 0, DIREITA));  //TAMANHO : 40
        
        if( linha.toString().length() < TAMANHO_LINHA ){
        	buffer.add(FormatUtils.completaDados(linha.toString()," ", TAMANHO_LINHA, 0, DIREITA));	
        } else {
        	buffer.add(linha.toString());
        }
        
        qtdRegistros++;
        
        // Regra 2 - Conhecimento
        
		List conhecimentos = getGeraFronteiraRapidaDestinoDAO().findConhecimentos(idManifesto);
        
        int qtdNotasFiscais = 0;        
        String rota = StringUtils.repeat(" ", 12);
        
        // Montagem do Conhecimento
        
        for (Iterator iter = conhecimentos.iterator(); iter.hasNext();) {
            
            Map mapConhecimento = (HashMap) iter.next();
            
            linha = new StringBuffer();
            
            String serie = (String) configuracoesFacade.getValorParametro((Long)mapConhecimento.get("serie"),"SG_ANTIGA_FILIAL");
            
            String vlParcela = getGeraFronteiraRapidaDestinoDAO().findConhecimentoParcelaDesconto(Long.valueOf((String) mapConhecimento.get("idConhecimento")));
        
            linha.append(CONHECIMENTO);                                                                                         //TAMANHO : 2   EXEMPLO : 02
            linha.append(FormatUtils.completaDados(cdEmpFronteiraRapida									, " ",  3, 0, DIREITA )); //TAMANHO : 3
            linha.append(FormatUtils.completaDados(serie                                                , " ",  5, 0, DIREITA )); //TAMANHO : 5
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("nrConhecimento")       , "0", 16, 0, ESQUERDA)); //TAMANHO : 16
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("cnpjTransportadora")   , " ", 19, 0, DIREITA )); //TAMANHO : 19
            YearMonthDay data = (YearMonthDay)mapConhecimento.get("dtEmissao");
            linha.append(data.toString(dma));                                               									  //TAMANHO : 8   EXEMPLO : DDMMYYYY
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("vlFrete")              , "0", 11, 2, ESQUERDA)); //TAMANHO : 11
            linha.append(FormatUtils.completaDados(vlParcela                                            , "0", 11, 2, ESQUERDA)); //TAMANHO : 11
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("vlBaseICMS")           , "0", 11, 2, ESQUERDA)); //TAMANHO : 11
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("aliquotaICMS")         , "0", 11, 2, ESQUERDA)); //TAMANHO : 11
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("vlICMS")               , "0", 11, 2, ESQUERDA)); //TAMANHO : 11
            linha.append(FormatUtils.completaDados(rota                                                 , " ", 12, 0, DIREITA )); //TAMANHO : 12
            
            if( linha.toString().length() < TAMANHO_LINHA ){
            	buffer.add(FormatUtils.completaDados(linha.toString()," ", TAMANHO_LINHA, 0, DIREITA));	
            } else {
            	buffer.add(linha.toString());
            }
            qtdRegistros++;
            
            List notasFiscais = getGeraFronteiraRapidaDestinoDAO().findNotasFiscaisConhecimento(idManifesto, Long.valueOf((String)mapConhecimento.get("idConhecimento")));
            
            // Montagem das Notas Fiscais
            
            StringBuffer linhaNF = null;
            
            BigDecimal valorTotalMercadorias = new BigDecimal(0);
            
            for (Iterator iterNF = notasFiscais.iterator(); iterNF.hasNext();) {
                
                HashMap mapNF = (HashMap) iterNF.next();
                
                qtdNotasFiscais++;
                linhaNF = new StringBuffer();
                
                String nrNotaFiscal = FormatUtils.completaDados((String) mapNF.get("nrDocumento"), "0", 6, 0, ESQUERDA );
                nrNotaFiscal = FormatUtils.completaDados(nrNotaFiscal, " ", 16, 0, DIREITA );
                
                linhaNF.append(NF);                                                     						  //TAMANHO : 2   EXEMPLO : 03
                linhaNF.append(FormatUtils.completaDados(cdEmpFronteiraRapida						, " ",  3, 0, DIREITA )); //TAMANHO : 3
                linhaNF.append(FormatUtils.completaDados((String) mapNF.get("serie")                , " ",  5, 0, DIREITA )); //TAMANHO : 5
                linhaNF.append(nrNotaFiscal); 																				  //TAMANHO : 16
                linhaNF.append(FormatUtils.completaDados((String) mapNF.get("remetenteConhecimento"), " ", 19, 0, DIREITA )); //TAMANHO : 19
                
                if( mapNF.get("vlTotalMercadoria") != null ){
                	valorTotalMercadorias = valorTotalMercadorias.add((BigDecimal) mapNF.get("vlTotalMercadoria"));
                }
                
                if( linha.toString().length() < TAMANHO_LINHA ){
                	buffer.add(FormatUtils.completaDados(linhaNF.toString()," ", TAMANHO_LINHA, 0, DIREITA));	
                } else {
                	buffer.add(linhaNF.toString());
                }
                
                qtdRegistros++;
                
            }            
            
            // Conhecimento de transporte - complemento

            String tpFrete = (String) mapConhecimento.get("tpFrete");
            
            if( tpFrete.equals("C") ){
                idCliente       = (mapConhecimento.get("idDestinatario")      != null ? Long.valueOf((String) mapConhecimento.get("idDestinatario")) : null);
                nrIdentificacao = (Long) (mapConhecimento.get("nrIdentDestinatario") != null ? mapConhecimento.get("nrIdentDestinatario") : null);
            } else {
                idCliente       = (mapConhecimento.get("idRemetente")         != null ? Long.valueOf((String) mapConhecimento.get("idRemetente")) : null);
                nrIdentificacao = (Long) (mapConhecimento.get("nrIdentRemetente")    != null ? mapConhecimento.get("nrIdentRemetente") : null);
            }
            
            ie = getInscricaoEstadualService().findByPessoaIndicadorPadrao(idCliente, Boolean.TRUE);            
            
            String inscricaoEstadual = null;
            String sgUF              = null;
            
            if( ie == null ){
                
                EnderecoPessoa ep = getEnderecoPessoaService().findEnderecoPessoaPadrao(idCliente);
                
                inscricaoEstadual = "ISENTO";
                sgUF = ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
                
            } else {
                
                inscricaoEstadual = ie.getNrInscricaoEstadual();
                sgUF = ie.getUnidadeFederativa().getSgUnidadeFederativa();
                
            }
            
            linha = new StringBuffer();
            
            linha.append(FIM_CONHECIMENTO);                                                                     //TAMANHO : 2   EXEMPLO : 07
            linha.append(MODELO_DOC_FISCAL);                                                                    //TAMANHO : 2   EXEMPLO : 08
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("cfop")             , " ",  4, 0, DIREITA));  //TAMANHO : 4
            linha.append(tpFrete.equals("C") ? "1" : "2" );                                                     //TAMANHO : 1
            linha.append(FormatUtils.completaDados((String) mapConhecimento.get("vlFrete")          , "0", 13, 2, ESQUERDA)); //TAMANHO : 13
            linha.append(FormatUtils.completaDados(valorTotalMercadorias							, "0", 13, 2, ESQUERDA)); //TAMANHO : 13
            linha.append(JTDateTimeUtils.getDataAtual().toString(amd));                                                               //TAMANHO : 8
            linha.append(FormatUtils.completaDados(nrIdentificacao                                  , " ", 14, 0, DIREITA));  //TAMANHO : 14
            linha.append(FormatUtils.completaDados(inscricaoEstadual                                , " ", 14, 0, DIREITA));  //TAMANHO : 14
            linha.append(FormatUtils.completaDados(sgUF                                             , " ",  2, 0, DIREITA));  //TAMANHO : 2
            
            if( linha.toString().length() < TAMANHO_LINHA ){
            	buffer.add(FormatUtils.completaDados(linha.toString()," ", TAMANHO_LINHA, 0, DIREITA));	
            } else {
            	buffer.add(linha.toString());
            }
            
            qtdRegistros++;
            
        }
        
        // Regra 3 - Manifesto / motorista / veículo / rota        
		Map mapManifesto = getGeraFronteiraRapidaDestinoDAO().findManifestoMotoristaVeiculo(idManifesto);        
        
        String ieSubstituto   = StringUtils.repeat(" ", 14);
        String controleLacres = "00001";
        String passeSintegra  = StringUtils.repeat(" ", 12);
        String tipoVeiculo    = "U";
        String sequencialEtin = "01";
        String tipoPassagem   = "D";
        
        linha = new StringBuffer();
        
		linha.append(MANIFESTO);                                                                                               //TAMANHO : 2   EXEMPLO : 08
		linha.append(FormatUtils.completaDados((String) mapManifesto.get("nrManifestoCarga")                    , "0", 14, 0, ESQUERDA));  //TAMANHO : 14
        linha.append(ieSubstituto);                                                                                            //TAMANHO : 14
        linha.append(FormatUtils.completaDados((String) map.get("sgUF")                                         , " ",  2, 0, ESQUERDA));  //TAMANHO : 2
        linha.append(controleLacres);                                                                                          //TAMANHO : 5
        linha.append(FormatUtils.completaDados((Integer.valueOf(qtdNotasFiscais))                                   , "0",  5, 0, ESQUERDA));  //TAMANHO : 5
        linha.append(FormatUtils.completaDados(((BigDecimal) mapManifesto.get("vlTotalMercadorias"))            , "0", 13, 2, ESQUERDA));  //TAMANHO : 13        
        linha.append(FormatUtils.completaDados(((BigDecimal) mapManifesto.get("pesoTotalCargaKg"))              , "0",  9, 3, ESQUERDA));  //TAMANHO : 9
        linha.append(passeSintegra);                                                                                           //TAMANHO : 12
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("nrHabilitacao")                       , "0", 11, 0, ESQUERDA));  //TAMANHO : 11
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("nrRG")                                , " ", 15, 0, DIREITA ));  //TAMANHO : 15
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("orgaoExpedicaoRG")                    , " ",  6, 0, DIREITA ));  //TAMANHO : 6
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("UFEmissaoRG")                         , " ",  2, 0, ESQUERDA));  //TAMANHO : 2
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("nrCPF")                               , " ", 11, 0, DIREITA ));  //TAMANHO : 11
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("nomeMotorista")                       , " ", 35, 0, DIREITA ));  //TAMANHO : 35
        linha.append(tipoVeiculo);                                                                                             //TAMANHO : 2
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("renavamVeiculo")                      , " ",  9, 0, DIREITA ));  //TAMANHO : 9
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("placaVeiculo")                        , " ",  7, 0, DIREITA ));  //TAMANHO : 7
        linha.append(FormatUtils.completaDados(((BigDecimal) mapManifesto.get("taraVeiculoKg"))                 , "0",  9, 3, ESQUERDA));  //TAMANHO : 9
        linha.append(FormatUtils.completaDados(((BigDecimal) mapManifesto.get("capacidadeVeiculoKg"))           , "0",  9, 3, ESQUERDA));  //TAMANHO : 9
        linha.append(FormatUtils.completaDados(((BigDecimal) mapManifesto.get("capacidadeVeiculoM3"))           , "0",  9, 3, ESQUERDA));  //TAMANHO : 9
        linha.append(FormatUtils.completaDados((String) mapManifesto.get("nrLacre")                             , "0", 12, 0, ESQUERDA));  //TAMANHO : 12
        linha.append(sequencialEtin);                                                                                          //TAMANHO : 2
        linha.append(tipoPassagem);                                                                                            //TAMANHO : 1        
        linha.append(FormatUtils.completaDados((String) map.get("sgUF")                                         , " ",  2, 0, ESQUERDA));  //TAMANHO : 2
        
        if( linha.toString().length() < TAMANHO_LINHA ){
        	buffer.add(FormatUtils.completaDados(linha.toString()," ", TAMANHO_LINHA, 0, DIREITA));	
        } else {
        	buffer.add(linha.toString());
        }
        
        qtdRegistros++;
        
        // Regra 4 - Rodapé
        
        linha = new StringBuffer();
        
        linha.append(RODAPE);                                                                                                    //TAMANHO : 2   EXEMPLO : 09
        linha.append(FormatUtils.completaDados((Integer.valueOf(++qtdRegistros)).toString()                           , "0", 11, 0, ESQUERDA));  //TAMANHO : 11
        
        if( linha.toString().length() < TAMANHO_LINHA ){
        	buffer.add(FormatUtils.completaDados(linha.toString()," ", TAMANHO_LINHA, 0, DIREITA));	
        } else {
        	buffer.add(linha.toString());
        }
        

        // Geração do Arquivo
        FileOutputStream out = null;
        
        PrintStream p = null;
        
        try {
            out = new FileOutputStream(diretorio + nomeArquivo);
            
            p = new PrintStream(out);
            
            for (Iterator iter = buffer.iterator(); iter.hasNext();) {
                String element = (String) iter.next();
                p.println(element);                
            }
            
            p.close();            
            
        } catch ( Exception e) {
        }
        
        Object[] params = new Object[]{nomeArquivo,diretorio};
        
        return configuracoesFacade.getMensagem("LMS-23012",params);		        
        
	}

	public InscricaoEstadualService getInscricaoEstadualService() {
		return inscricaoEstadualService;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

    public GeraFronteiraRapidaDestinoDAO getGeraFronteiraRapidaDestinoDAO() {
		return geraFronteiraRapidaDestinoDAO;
	}

	public void setGeraFronteiraRapidaDestinoDAO(GeraFronteiraRapidaDestinoDAO geraFronteiraRapidaDestinoDAO) {
		this.geraFronteiraRapidaDestinoDAO = geraFronteiraRapidaDestinoDAO;
	}

    /**
     * @return Returns the parametroGeralService.
     */
    public ParametroGeralService getParametroGeralService() {
        return parametroGeralService;
    }

    /**
     * @param parametroGeralService The parametroGeralService to set.
     */
    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }
   
    public ConteudoParametroFilialService getConteudoParametroFilialService() {
        return conteudoParametroFilialService;
    }

    public void setConteudoParametroFilialService(
            ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public EnderecoPessoaService getEnderecoPessoaService() {
        return enderecoPessoaService;
    }

    public void setEnderecoPessoa(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

}