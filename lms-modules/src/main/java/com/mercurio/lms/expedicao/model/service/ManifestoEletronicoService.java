package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.util.mdfe.EventoMdfeXmlWrapper;
import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.carregamento.util.mdfe.MdfeXmlWrapper;
import com.mercurio.lms.carregamento.util.mdfe.XmlMdfe;
import com.mercurio.lms.carregamento.util.mdfe.type.TipoEventoMdfe;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.CIOTControleCarga;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.FilialPercursoUF;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.dao.ManifestoEletronicoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.mdfe.model.v300.AutXML;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.mdfe.model.v300.Seg;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

import br.com.tntbrasil.integracao.domains.expedicao.DocumentoDeclaracaoMdfe;

@Assynchronous
@ServiceSecurity
public class ManifestoEletronicoService extends CrudService<ManifestoEletronico, Long> {
    
	private static final String GERAR_GRUPO_PRODPRED_MDFE = "GERAR_GRUPO_PRODPRED_MDFE";

	private static final String INCLUIR_QRCODE_MDFE = "INCLUIR_QRCODE_MDFE";
	
	private static final String LINK_QRCODE_MDFE = "LINK_QRCODE_MDFE";
	
	private static final String LIMITE_HORAS_CANCELAR_MDFE = "LIMITE_HORAS_CANCELAR_MDFE";

	private static final String TP_SITUACAO_MDFE_AUTORIZADO = "A";

	private static final String TP_SITUACAO_MDFE_CANCELADO = "C";

	private static final String TP_SITUACAO_MDFE_ENCERRADO = "E";

	private Logger log = LogManager.getLogger(this.getClass());

	private ProprietarioService proprietarioService;
    
    private ParametroGeralService parametroGeralService;

    private ConteudoParametroFilialService conteudoParametroFilialService;
    
    private IntegracaoNDDigitalService integracaoNDDigitalService;

    private ControleCargaService controleCargaService;
    
    private ApoliceSeguroService apoliceSeguroService;

    private ConfiguracoesFacade configuracoesFacade;
    
    private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
    
    private ContingenciaService contingenciaService;
    
    private FilialPercursoUFService filialPercursoUFService;

    private CIOTControleCargaService ciotControleCargaService;
    
    private DoctoServicoSegurosService doctoServicoSegurosService;
    
    private DevedorDocServService devedorDocServService;
    
    private NaturezaProdutoService naturezaProdutoService;
    /**
     * Rotina GerarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
     * 
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap gerarMDFEViagem(Long idControleCarga, List<ManifestoEletronico> rejeitados) {
    	ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
    	
    	List<Conhecimento> conhecimentosControleCarga = controleCargaService.findConhecimentosControleCarga(controleCarga.getIdControleCarga());
		
	    Filial filialSessao = SessionUtils.getFilialSessao(); 
	    
		Object o = parametroGeralService.findConteudoByNomeParametroWithoutException("ID_EMPRESA_MERCURIO", false);
		Long idEmpresaMercurio = (o instanceof BigDecimal) ? ((BigDecimal)o).longValue() : Long.valueOf(o.toString());
		
	    Boolean hasContingenciaValidaByFilial = contingenciaService.hasContingenciaMdfeValida(filialSessao.getIdFilial(), idEmpresaMercurio);

		TypedFlatMap map = new TypedFlatMap();
		
		map.put("imprimeMdfeAutorizado", true);
		if (!hasContingenciaValidaByFilial) {
			map.put("aguardarAutorizacaoMdfe", true);
		}
		map.put("dhEmissao", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));
		
		List<ManifestoEletronico> manifestos = new ArrayList<ManifestoEletronico>();
		
		if (rejeitados.isEmpty()) {
			if(conhecimentosControleCarga != null && !conhecimentosControleCarga.isEmpty()){
				manifestos.addAll(gerarMDFE(controleCarga, conhecimentosControleCarga, hasContingenciaValidaByFilial, true));
			}
		} else {
			manifestos.addAll(gerarMDFERejeitados(controleCarga, rejeitados, null, hasContingenciaValidaByFilial, true, conhecimentosControleCarga));
		}
		if(manifestos.isEmpty()){
			map.put("idsManifestoEletronico", new ArrayList<Long>());
		}
		
		for (ManifestoEletronico aux: manifestos) {
			if (map.get("idsManifestoEletronico") == null) {
				map.put("nfMdfe", SessionUtils.getFilialSessao().getSgFilial() + "-" + FormatUtils.fillNumberWithZero(aux.getNrManifestoEletronico().toString(), 9));
				
				map.put("idsManifestoEletronico", new ArrayList<Long>());
			}
			
			((List<Long>)map.get("idsManifestoEletronico")).add(aux.getIdManifestoEletronico());
			
		}
		
		return map;
    }
    
    public TypedFlatMap gerarMDFE(Long idControleCarga, List<ManifestoEletronico> rejeitados) {
    	ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
    	
    	Map<String, List<Conhecimento>> conhecimentosControleCargaByUf = null;
        List<Conhecimento> conhecimentosControleCarga = controleCargaService.findConhecimentosControleCarga(controleCarga.getIdControleCarga());
        conhecimentosControleCargaByUf = MdfeConverterUtils.getConhecimentosByMunicipioEntrega(conhecimentosControleCarga);
		
	    Filial filialSessao = SessionUtils.getFilialSessao(); 
	    
		Object o = parametroGeralService.findConteudoByNomeParametroWithoutException("ID_EMPRESA_MERCURIO", false);
		Long idEmpresaMercurio = (o instanceof BigDecimal) ? ((BigDecimal)o).longValue() : Long.valueOf(o.toString());
		
	    Boolean hasContingenciaValidaByFilial = contingenciaService.hasContingenciaMdfeValida(filialSessao.getIdFilial(), idEmpresaMercurio);

		TypedFlatMap map = new TypedFlatMap();
		
		map.put("imprimeMdfeAutorizado", true);
		if (!hasContingenciaValidaByFilial) {
			map.put("aguardarAutorizacaoMdfe", true);
		}
		map.put("dhEmissao", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));
		
		List<ManifestoEletronico> manifestos = new ArrayList<ManifestoEletronico>();
		
		if (rejeitados.isEmpty()) {
			for (String uf: conhecimentosControleCargaByUf.keySet()) {
				
				List<Conhecimento> conhecimentosControleCargaList = conhecimentosControleCargaByUf.get(uf);
				
				manifestos.addAll(gerarMDFE(controleCarga, conhecimentosControleCargaList, hasContingenciaValidaByFilial, false));
				
	
			}
		} else {
			manifestos.addAll(gerarMDFERejeitados(controleCarga, rejeitados, conhecimentosControleCargaByUf, hasContingenciaValidaByFilial, false, conhecimentosControleCarga));
		}
		
		for (ManifestoEletronico aux: manifestos) {
			if (map.get("idsManifestoEletronico") == null) {
				map.put("nfMdfe", SessionUtils.getFilialSessao().getSgFilial() + "-" + FormatUtils.fillNumberWithZero(aux.getNrManifestoEletronico().toString(), 9));
				
				map.put("idsManifestoEletronico", new ArrayList<Long>());
			}
			
			((List<Long>)map.get("idsManifestoEletronico")).add(aux.getIdManifestoEletronico());
			
		}
		
		return map;
    }
    
    private int getFilialPosition(Filial filial, List<FilialRota> listFilialRotas){
		for (int i = 0; i < listFilialRotas.size(); i++) {
			if(listFilialRotas.get(i).getFilial().getIdFilial() == filial.getIdFilial()){
				return i;
			}
		}
		return 0;
	}

    /**
     * Rotina GerarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
     * @param hasContingenciaValidaByFilial 
     * 
     * @param idControleCarga
     * @return
     */
	private List<ManifestoEletronico> gerarMDFE(ControleCarga controleCarga, List<Conhecimento> conhecimentosControleCarga, Boolean hasContingenciaValidaByFilial, boolean isViagem) {
	    
	    Filial filialOrigem = controleCarga.getFilialByIdFilialAtualizaStatus(); 
	    
	    BigDecimal limiteDocumentosPorMdfe = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("QTDE_DOCS_MDFE", false);
	    boolean  isAgrupaPorUFDestino = true;
	    
    	//Passos 4 e 6
	    
	    
	    List<ManifestoEletronico> toReturn = new ArrayList<ManifestoEletronico>();
	    
		List<ManifestoEletronico> list = createMdfes(filialOrigem, controleCarga, new ArrayList<Conhecimento>(conhecimentosControleCarga), limiteDocumentosPorMdfe, hasContingenciaValidaByFilial, isViagem);
    	
        Map<String, ManifestoEletronico> mdfeMap = new HashMap<String, ManifestoEletronico>();
        for (ManifestoEletronico manifestoEletronico : list) {
            String uf = MdfeConverterUtils
                    .getUnidadeFederativaDestinoByManifesto(manifestoEletronico)
                    .getSgUnidadeFederativa();
            if(mdfeMap.containsKey(uf)){
                mdfeMap.get(uf).getConhecimentos().addAll(manifestoEletronico.getConhecimentos());
            }else{
                mdfeMap.put(uf, 
                        manifestoEletronico);
            }
        }
        list = new ArrayList<ManifestoEletronico>(mdfeMap.values());
        
    	for (ManifestoEletronico mdfe : list) {
			//Passos 5, 7 e 10 - Gerar o XML com os dados do MDFE, este XML deverá ser gravado na tabela TBDATABASEINPUT_CTE
			toReturn.add(createIntegracao(filialOrigem, mdfe, new ArrayList<Conhecimento>(mdfe.getConhecimentos()), limiteDocumentosPorMdfe, hasContingenciaValidaByFilial, isViagem, isAgrupaPorUFDestino));
			//Fim passos 5, 7 e 10
		}

	    
		return toReturn;
        //Passos 8, 9 e 11 devem ser feito na action (comitar a transação e aguardar o retorno da sefaz)
	}
	
	public boolean isAgrupaPorUFDestino(Long idFilialUsuario) {
		String indicadorAgrupaUF = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "MAN_POR_UF_DEST", false);
		return "S".equals(indicadorAgrupaUF);
    }

	/**
	 * Rotina GerarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
	 * @param conhecimentosControleCargaByUf 
	 * @param hasContingenciaValidaByFilial 
	 * 
	 * @param idControleCarga
	 * @return
	 */
	private List<ManifestoEletronico>  gerarMDFERejeitados(ControleCarga controleCarga, List<ManifestoEletronico> rejeitados, Map<String, List<Conhecimento>> conhecimentosControleCargaByUf, Boolean hasContingenciaValidaByFilial, boolean isViagem, List<Conhecimento> conhecimentosControleCarga) {

        
        boolean  isAgrupaPorUFDestino = true;

		TypedFlatMap map = new TypedFlatMap();

		map.put("imprimeMdfeAutorizado", true);
		map.put("aguardarAutorizacaoMdfe", true);
		map.put("dhEmissao", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));

		Filial filialOrigem = controleCarga.getFilialByIdFilialAtualizaStatus(); 
		
		BigDecimal limiteDocumentosPorMdfe = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("QTDE_DOCS_MDFE", false);
		
		List<ManifestoEletronico> toReturn = new ArrayList<ManifestoEletronico>();
		
    	for (ManifestoEletronico mdfe : rejeitados) {
    		mdfe.setConhecimentos(new ArrayList<Conhecimento>());
            for (Conhecimento conhecimento : conhecimentosControleCarga) {
                String ufMdfe = MdfeConverterUtils
                        .getUnidadeFederativaDestinoByManifesto(mdfe)
                        .getSgUnidadeFederativa();
                String ufConhecimento = MdfeConverterUtils
                        .getUfDestinoConhecimento(conhecimento)
                        .getSgUnidadeFederativa();
                if(StringUtils.equalsIgnoreCase(ufMdfe, ufConhecimento)){
                    mdfe.getConhecimentos().add(conhecimento);
    		}
            }
	    	
			//Passos 5, 7 e 10 - Gerar o XML com os dados do MDFE, este XML deverá ser gravado na tabela TBDATABASEINPUT_CTE
			toReturn.add(createIntegracao(filialOrigem, mdfe, new ArrayList<Conhecimento>(mdfe.getConhecimentos()), limiteDocumentosPorMdfe, hasContingenciaValidaByFilial, isViagem, isAgrupaPorUFDestino));
			//Fim passos 5, 7 e 10
		}
		
		return toReturn;
		
		
		//Passos 8, 9 e 11 devem ser feito na action (comitar a transação e aguardar o retorno da sefaz)
		
	}

	/**
	 * Passos 5, 7 e 10 da rotina GerarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
	 * 
	 * @param map
	 * @param filialSessao
	 * @param list
	 * @param conhecimentosControleCarga
	 * @param limiteDocumentosPorMdfe
	 * @param hasContingenciaValidaByFilial 
	 */
	private ManifestoEletronico createIntegracao(Filial filialorigem,
			ManifestoEletronico mdfe,
			List<Conhecimento> conhecimentosControleCarga,
			BigDecimal limiteDocumentosPorMdfe, Boolean hasContingenciaValidaByFilial, boolean isViagem, boolean isAgrupaPorUFDestino) {
		
		//
		populateConhecimentosMdfe(mdfe, conhecimentosControleCarga, limiteDocumentosPorMdfe.intValue());
		
        // Passo 5 - Gerar a observação do seguro
//        String obsSeguro = gerarObsSeguro(mdfe.getConhecimentos());
//        mdfe.setDsObservacao(obsSeguro);

		// Gera o XML da MDFe
		XmlMdfe xmlMdfe = gerarXMLEmissao(mdfe, hasContingenciaValidaByFilial, isViagem, isAgrupaPorUFDestino);

		TBDatabaseInputCTE databaseInput = storeTBDatabaseInputCteForEmissaoMdfe(xmlMdfe.getXml(), filialorigem, hasContingenciaValidaByFilial);

		mdfe.setNrChave(xmlMdfe.getChave());
        mdfe.setTpSituacao(hasContingenciaValidaByFilial ? new DomainValue("T") : new DomainValue("E"));
		mdfe.setIdEnvioE(databaseInput.getId());
		mdfe.setDsDados(databaseInput.getDocumentData());

		store(mdfe);

		return mdfe;
		
	}
        
	/**
	 * 
	 * Passos 4 e 6 da rotina GerarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
	 * 
	 * @param filialSessao
	 * @param controleCarga
	 * @param conhecimentos
	 * @param limiteDocumentosPorMdfe
	 * @param hasContingenciaValidaByFilial 
	 * @return
	 */
	private List<ManifestoEletronico> createMdfes(Filial filialOrigem, ControleCarga controleCarga, List<Conhecimento> conhecimentos, BigDecimal limiteDocumentosPorMdfe, Boolean hasContingenciaValidaByFilial, boolean isViagem) {

		List<ManifestoEletronico> mdfes = new ArrayList<ManifestoEletronico>();
        
		BigDecimal qtdMdfes = new BigDecimal(1);
		Integer qtdDocumentoCc = conhecimentos.size();
        
		if (qtdDocumentoCc.intValue() > 0) {
			qtdMdfes = new BigDecimal(qtdDocumentoCc).divide(limiteDocumentosPorMdfe, 0, RoundingMode.CEILING);
	}
	
		for (int i = 0; i < qtdMdfes.intValue(); i++) {

			// Passo 6 - Gravar o registro na tabela MANIFESTO_ELETRONICO:

            for (Conhecimento conhecimento : conhecimentos) {
                Long nrProximoMdfe = conteudoParametroFilialService.generateProximoValorParametroSequencial(filialOrigem.getIdFilial(), "NR_MDFE", true);
                Filial filialDestino = controleCarga.getFilialByIdFilialDestino();
                if(isViagem){
                	filialDestino = MdfeConverterUtils.getFilialProximoDestinoControleCarga(controleCarga);
                }
                
                ManifestoEletronico mdfe = populateMdfe(filialOrigem, controleCarga, nrProximoMdfe, hasContingenciaValidaByFilial, filialDestino);
                mdfe.setConhecimentos(new ArrayList<Conhecimento>(java.util.Arrays.asList(conhecimento)));
			mdfes.add(mdfe);
		}
		}
		
		return mdfes;
		
	}
	
	/**
	 * Rotina GerarMDFEContingencia da ET 05.01.01.02 - Emitir Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public TypedFlatMap gerarMDFEContingencia(Long idControleCarga, boolean isViagem){
		TypedFlatMap map = new TypedFlatMap();
		map.put("imprimeMdfeAutorizado", true);
		//map.put("aguardarAutorizacaoMdfe", false);
		map.put("dhEmissao", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));

		Filial filialSessao = SessionUtils.getFilialSessao(); 
		
	    ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

		List<ManifestoEletronico> listMdfe = getManifestoEletronicoDAO().findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, null, ConstantesExpedicao.TP_MDFE_ENVIADO);
		
	    BigDecimal limiteDocumentosPorMdfe = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("QTDE_DOCS_MDFE", false);

		Map<String, List<Conhecimento>> mapListDoctoServico = MdfeConverterUtils.getConhecimentos(controleCargaService.findConhecimentosControleCarga(idControleCarga));

	    for (ManifestoEletronico mdfe: listMdfe) {
	    	
	    	List<Conhecimento> listDoctoServico = getFirstListDoctoServico(mapListDoctoServico);
	    	
			StringBuilder xmlAbort = new StringBuilder();
			xmlAbort.append("<abort><chave>").append(mdfe.getNrChave()).append("</chave></abort>");
			
			integracaoNDDigitalService.generateIntegracaoAbortMdfe(filialSessao.getSgFilial(), xmlAbort.toString());

			populateConhecimentosMdfe(mdfe, listDoctoServico, limiteDocumentosPorMdfe.intValue());
			
	        // Passo 5 - Gerar a observação do seguro
//	        String obsSeguro = gerarObsSeguro(mdfe.getConhecimentos());
//	        mdfe.setDsObservacao(obsSeguro);

	        XmlMdfe xmlMdfe = gerarXMLEmissao(mdfe, true, isViagem, false);
			
			TBDatabaseInputCTE databaseInput = storeTBDatabaseInputCteForEmissaoMdfe(xmlMdfe.getXml(), filialSessao, true);
			
			mdfe.setNrChave(xmlMdfe.getChave());
	        mdfe.setTpSituacao(new DomainValue("T"));
			mdfe.setIdEnvioE(databaseInput.getId());
			mdfe.setDsDados(databaseInput.getDocumentData());
			
			store(mdfe);
			
			List<Long> idsManifestoEletronico = (List<Long>) map.get("idsManifestoEletronico");
			
			if (idsManifestoEletronico == null) {
				idsManifestoEletronico = new ArrayList<Long>();
				
				map.put("idsManifestoEletronico", idsManifestoEletronico);
				
				map.put("nfMdfe", filialSessao.getSgFilial() + "-" + FormatUtils.fillNumberWithZero(mdfe.getNrManifestoEletronico().toString(), 9));
			}

			idsManifestoEletronico.add(mdfe.getIdManifestoEletronico());
		}
		
		return map;
		
	}
	
	private List<Conhecimento> getFirstListDoctoServico(Map<String, List<Conhecimento>> mapListDoctoServico) {

		String uf = mapListDoctoServico.keySet().iterator().next();
		if (mapListDoctoServico.get(uf).isEmpty()) {
			mapListDoctoServico.remove(uf);
			uf = mapListDoctoServico.keySet().iterator().next();
		}
		
		return mapListDoctoServico.get(uf);
		
	}

	/**
	 * Divide os conhecimentos do controle de carga, em vários MDF-e´s de acordo com o parâmetro geral.
	 * @param mdfe
	 * @param conhecimentos
	 * @param limiteDocumentosPorMdfe
	 */
	private void populateConhecimentosMdfe(ManifestoEletronico mdfe, List<Conhecimento> conhecimentos, int limiteDocumentosPorMdfe) {
		List<Conhecimento> conhecimentosPorMdfe = new ArrayList<Conhecimento>();
		for (int i = 0; i < limiteDocumentosPorMdfe; i++) {
			if (conhecimentos.isEmpty()) {
				break;
			}
			conhecimentosPorMdfe.add(conhecimentos.remove(0));
		}
		mdfe.setConhecimentos(conhecimentosPorMdfe);
	}
	
	public boolean validaGeracaoMDFEManifestoAereo(long idControleCarga){
		YearMonthDay dtImplantacaoAereo = null;
		try{
			dtImplantacaoAereo = (YearMonthDay)conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "DT_IMPLANTACAO_AEREO", false);
		}catch(IllegalStateException ise){
			dtImplantacaoAereo = null;
		}		

		if((dtImplantacaoAereo == null || dtImplantacaoAereo.isAfter(JTDateTimeUtils.getDataAtual()))
				&& controleCargaService.hasManifestoAereoControleCarga(idControleCarga)){
			return false;
		}
		
		return true;
	}

	/**
	 * Validações iniciais da rotina GerarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
	 * @param isMdfe 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TypedFlatMap validaEmissaoMdfe(Long idControleCarga, boolean contingenciaConfirmada) {
		Filial filialSessao = SessionUtils.getFilialSessao();
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		
		if (!controleCargaService.hasConhecimentoControleCarga(idControleCarga) || !this.validaGeracaoMDFEManifestoAereo(idControleCarga)) {
			TypedFlatMap map = new TypedFlatMap();
			map.put("pularGeracaoMdfe", true);
			return map;
		}		
		
		//05.01.01.02 Emitir Controle de Carga - GerarMDFE - PASSO 1
		//Verificar se existe uma contingência em aberto:
		//Se existir uma contingência em aberto na tabela CONTINGENCIA para a filial do usuário logado 
		//(CONTINGENCIA.ID_FILIAL = ID da filial do usuário logado, 
		//TP_CONTINGENCIA = ‘M’ e 
		//TP_SITUACAO = ‘A’) 
		//visualizar a mensagem de alerta “LMS-04391”, aguardando confirmação.

	    //Faz uma busca de contingência, só apresenta a mensagem se a contingencia é específica da filial
	    Boolean hasContingenciaValida = contingenciaService.hasContingenciaMdfeValida(filialSessao.getIdFilial());

	    if (hasContingenciaValida && !contingenciaConfirmada) {
			TypedFlatMap map = new TypedFlatMap();
			map.put("hasContingencia", true);
			map.put("confirm_key", "LMS-04391");
			map.put("confirm_message", configuracoesFacade.getMensagem("LMS-04391"));
			return map;
			
		}
		


		// Passo 3 - Verificar se já existe MDF-e pendente para o Controle de Carga

	    //Faz uma nova busca de contingência, considerando contingencias cadastradas na matriz
		Object o = parametroGeralService.findConteudoByNomeParametroWithoutException("ID_EMPRESA_MERCURIO", false);
		Long idEmpresaMercurio = (o instanceof BigDecimal) ? ((BigDecimal)o).longValue() : Long.valueOf(o.toString());
		
	    hasContingenciaValida = contingenciaService.hasContingenciaMdfeValida(filialSessao.getIdFilial(), idEmpresaMercurio);

		// Verifica se já existe um MDF-e para este Controle de Carga, aguardando autorização do sefaz
        boolean existeManifestoAguardandoAutorizacao = !findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial(), ConstantesExpedicao.TP_MDFE_ENVIADO).isEmpty();
        if (existeManifestoAguardandoAutorizacao) {
        	if (hasContingenciaValida) {
    			TypedFlatMap map = new TypedFlatMap();
    			map.put("executarGerarMDFEContigencia", true);
    			return map;
        	} else {
        		throw new BusinessException("LMS-05355");
        	}
	    }

		// Buscar os MDF-e rejeitados
		List<ManifestoEletronico> rejeitados = findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial(), ConstantesExpedicao.TP_MDFE_REJEITADO);


		//Se retornar algum registo rejeitado passar ao item 10
		TypedFlatMap map = new TypedFlatMap();
		map.put("rejeitados", rejeitados);
		if (! rejeitados.isEmpty()) {

			return map;
		}

		// Se o MDF-e já foi autorizado pelo sefaz, vai direto para a emissão do mdfe
		List<ManifestoEletronico> autorizados = findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial(), new String[] {ConstantesExpedicao.TP_MDFE_AUTORIZADO, ConstantesExpedicao.TP_MDFE_ENVIADO_CONTINGENCIA});

		if (!autorizados.isEmpty()) {

					map.put("imprimeMdfeAutorizado", true);

			for (ManifestoEletronico mdfe : autorizados) {

				if (map.get("idsManifestoEletronico") == null) {
					map.put("nfMdfe", controleCarga.getFilialByIdFilialAtualizaStatus().getSgFilial() + "-" + FormatUtils.fillNumberWithZero(autorizados.get(0).getNrManifestoEletronico().toString(), 9));

					map.put("idsManifestoEletronico", new ArrayList<Long>());
				}
				
					((List<Long>) map.get("idsManifestoEletronico")).add(mdfe.getIdManifestoEletronico());


				}

			return map;

				}
		// Fim passo 1

			
			// Passo 2 - Encerrar o MDFE ainda não encerrado vinculado ao Controle de Carga
			if (hasMdfesAutorizados(idControleCarga)) {
				map = new TypedFlatMap();
				map.put("encerrarMdfesAutorizados", true);
			}
			// Fim passo 2

		return map;
	}

	private ManifestoEletronico populateMdfe(Filial filialOrigem, ControleCarga cc, Long nrProximoMdfe, Boolean hasContingenciaValidaByFilial, Filial filialDestino) {
        ManifestoEletronico mdfe = new ManifestoEletronico();
        
        mdfe.setFilialOrigem(filialOrigem);
        mdfe.setFilialDestino(filialDestino);
        
        mdfe.setTpSituacao(hasContingenciaValidaByFilial ? new DomainValue("T") : new DomainValue("E"));
        mdfe.setNrManifestoEletronico(nrProximoMdfe);
        mdfe.setControleCarga(cc);
        mdfe.setDhEmissao(JTDateTimeUtils.getDataHoraAtual(filialOrigem));
        
        return mdfe;
    }

    private String gerarObsSeguro(List<Conhecimento> conhecimentosPorMdfe) {
        StringBuilder toReturn = new StringBuilder();

        List<ApoliceSeguro> retornaApolices = apoliceSeguroService.retornaApolices("R", new Date());
        
        String[] parametrosTNT = {"", "", ""};
        
        //1) Buscar dados da(s) seguradora(s) TNT
        
        boolean variasApolices = !retornaApolices.isEmpty() && retornaApolices.size()>1;
        boolean mesmaSeguradora = true;
        ApoliceSeguro apoliceDeMaiorValor = null;
        
        for (ApoliceSeguro as : retornaApolices) {
            apoliceDeMaiorValor = (apoliceDeMaiorValor != null && apoliceDeMaiorValor.getVlLimiteApolice().doubleValue() > as.getVlLimiteApolice().doubleValue()) ? apoliceDeMaiorValor : as ;  
            if(!apoliceDeMaiorValor.getSeguradora().getIdSeguradora().equals(as.getSeguradora().getIdSeguradora()))
                mesmaSeguradora = false;
        }
        
        if(variasApolices){
            if(mesmaSeguradora){
                for (ApoliceSeguro as : retornaApolices) 
                    parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" "+as.getNrApolice()+", ";
                parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa())+" ";
                parametrosTNT[2] += FormatUtils.formatDecimal("#,###,###,##0.00", apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
            }else{
                for (ApoliceSeguro as : retornaApolices) 
                    parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" "+as.getNrApolice()+" "+apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa());
                parametrosTNT[2] += FormatUtils.formatDecimal("#,###,###,##0.00", apoliceDeMaiorValor.getVlLimiteApolice().doubleValue())+".";
            }
        }else{
            for (ApoliceSeguro as : retornaApolices) 
                parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" " +as.getNrApolice();
            parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa())+" ";
            parametrosTNT[2] += FormatUtils.formatDecimal("#,###,###,##0.00", apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
        }
        
        String mensagemSegurosTNT = configuracoesFacade.getMensagem("LMS-04410", parametrosTNT ).replace("  ", " ").replace(", ,", ", ");
        
        toReturn.append(mensagemSegurosTNT);
        
        //2) Buscar Seguradora Cliente(s)
        
        Map<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>> segurosClientes = new HashMap<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>>();
        
        for(DoctoServico c: conhecimentosPorMdfe){
            
            Cliente cli = c.getClienteByIdClienteRemetente();
            
            for(Object obj : cli.getSeguroClientes()){
                SeguroCliente sc = (SeguroCliente) obj;
                if("N".equals(sc.getTpAbrangencia().getValue())//Nacional
                        && sc.getDtVigenciaInicial().toDateMidnight().toDate().before(new Date())//esta vigente?
                        && sc.getDtVigenciaFinal().toDateMidnight().toDate().after(new Date())){
                    
                    Map<ReguladoraSeguro, List<SeguroCliente>> seguros = null;
                    if(!segurosClientes.containsKey(cli)){
                        seguros = new HashMap<ReguladoraSeguro, List<SeguroCliente>>();
                        segurosClientes.put(cli, seguros);
                    }else{
                        seguros = segurosClientes.get(cli);
                    }

                    if(!seguros.containsKey(sc.getReguladoraSeguro()))
                        seguros.put(sc.getReguladoraSeguro(), new ArrayList<SeguroCliente>());

                    if(!seguros.get(sc.getReguladoraSeguro()).contains(sc))
                        seguros.get(sc.getReguladoraSeguro()).add(sc);

                }
            }
        }
        
        //imprime os valores no formulario
        if(!segurosClientes.isEmpty()){
            
            Iterator<Cliente> iteratorClientes = segurosClientes.keySet().iterator();
            
            while(iteratorClientes.hasNext()){
                String[] param = {"", "", ""}; 
                BigDecimal valor = BigDecimal.ZERO;
                Cliente cliente = iteratorClientes.next();
                param[0] = (cliente.getPessoa().getNmFantasia()!=null?cliente.getPessoa().getNmFantasia():cliente.getPessoa().getNmPessoa())+" CNPJ "+FormatUtils.formatIdentificacao(cliente.getPessoa());
                
                Map<ReguladoraSeguro, List<SeguroCliente>> map = segurosClientes.get(cliente);
                Iterator<ReguladoraSeguro> iteratorSeguros = map.keySet().iterator();
                while(iteratorSeguros.hasNext()){
                    
                    ReguladoraSeguro next = iteratorSeguros.next();
                    List<SeguroCliente> list = map.get(next);
                    
                    for (SeguroCliente seguroCliente : list) { 
                        param[1] += seguroCliente.getTipoSeguro().getSgTipo()+" "+seguroCliente.getDsApolice() + (!isLast(list, seguroCliente)?", ":"");
                        valor = seguroCliente.getVlLimite().doubleValue() > valor.doubleValue() ? seguroCliente.getVlLimite() : valor; 
                    }

                }
                //param[2] = NumberFormat.getCurrencyInstance().format(valor).replace("R$ ", "");
                param[2] = FormatUtils.formatDecimal("#,###,###,##0.00", valor);
                
                //LMS-04411 "Apólice(s) própria(s): {0} apólice(s) de seguro(s) {1}, averbação de R$ {2}."
                String mensagemSeguroClientes = configuracoesFacade.getMensagem("LMS-04411", param); 
             
                toReturn.append(";").append(mensagemSeguroClientes);
                
            }

        }
        
        return toReturn.toString();
    }

	private boolean hasMdfesAutorizados(Long idControleCarga) {
        return !getManifestoEletronicoDAO().findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, null, ConstantesExpedicao.TP_MDFE_AUTORIZADO).isEmpty();
	}
    
	public TypedFlatMap encerrarMdfesAutorizados(Long idControleCarga) {
	    TypedFlatMap map = new TypedFlatMap();
	    
	    List<ManifestoEletronico> autorizados = getManifestoEletronicoDAO().findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, null, ConstantesExpedicao.TP_MDFE_AUTORIZADO);
        for (ManifestoEletronico mdfe: autorizados) {
            encerrarMdfe(mdfe);
            map.put("idManifestoEletronicoEncerrado", mdfe.getIdManifestoEletronico());
            map.put("dhEncerramento", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));
        }
        
        return map;
    }

	/**
     * Rotina EncerrarMDFE da ET 05.01.01.02 - Emitir Controle de Carga
	 * 
	 * @param mdfe
	 */
    private void encerrarMdfe(ManifestoEletronico mdfe) {
        StringBuffer xmlMdfe = gerarXMLEvento(mdfe, mdfe.getFilialOrigem(), TipoEventoMdfe.ENCERRAMENTO);
        
        TBDatabaseInputCTE databaseInput = storeTBDatabaseInputCteForEventoMdfe(xmlMdfe, mdfe.getFilialOrigem());
        
        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_ENCERRADO_ENVIADO));
        mdfe.setIdEnvioF(databaseInput.getId());
        getManifestoEletronicoDAO().store(mdfe);
        
    }

    public TypedFlatMap cancelarMdfe(ManifestoEletronico mdfe, boolean validarCancelamento) {
    	if(validarCancelamento){
    		validarCancelamentoMdfe(mdfe, mdfe.getControleCarga());
    	}
        Filial filialSessao = SessionUtils.getFilialSessao();

        StringBuffer xmlMdfe = gerarXMLEvento(mdfe, filialSessao, TipoEventoMdfe.CANCELAMENTO);
        
        TBDatabaseInputCTE databaseInput = storeTBDatabaseInputCteForEventoMdfe(xmlMdfe, filialSessao);
        
        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_CANCELADO_ENVIADO));
        mdfe.setIdEnvioC(databaseInput.getId());

        getManifestoEletronicoDAO().store(mdfe);
        
        TypedFlatMap map = new TypedFlatMap();
        map.put("idManifestoEletronicoCancelado", mdfe.getIdManifestoEletronico());
        map.put("dhEncerramento", JTDateTimeUtils.getDataHoraAtual().toString("yyyy-MM-dd HH:mm:ss"));
        return map;
    }
    
    private boolean validarCancelamentoMdfe(ManifestoEletronico mdfe, ControleCarga controleCarga){
    	DateTime dhEmissaoMdfe = mdfe.getDhEmissao();
    	
    	Integer diffDatesInMinutes = Minutes.minutesBetween(dhEmissaoMdfe, JTDateTimeUtils.getDataHoraAtual()).getMinutes();
    	double diffDatesInHours = diffDatesInMinutes / 60.0;
    	BigDecimal limiteHorasCancelarMdfe = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(LIMITE_HORAS_CANCELAR_MDFE, false);
    	
    	if(limiteHorasCancelarMdfe != null && diffDatesInHours > limiteHorasCancelarMdfe.doubleValue()){
    		throw new BusinessException("LMS-05408", new Object[]{mdfe.getNrManifestoEletronico(), limiteHorasCancelarMdfe});
    	}
    	return true;
    }
    
    //LMS-3544
    @SuppressWarnings("rawtypes")
    public Map cancelarMdfe(Long idControleCarga) {
        
        Filial filialSessao = SessionUtils.getFilialSessao();
        List<ManifestoEletronico> manifestoEletronicos = getManifestoEletronicoDAO().findManifestoEletronicoByControleCargaAndTpSituacao(
        		idControleCarga, filialSessao.getIdFilial(), ConstantesExpedicao.TP_MDFE_AUTORIZADO);
        
        for (ManifestoEletronico manifestoEletronico : manifestoEletronicos) {
        	validarCancelamentoMdfe(manifestoEletronico, manifestoEletronico.getControleCarga());
		}
        
		Map map = new HashMap();
        for (ManifestoEletronico manifestoEletronico : manifestoEletronicos) {
			map = cancelarMdfe(manifestoEletronico, false);
		}
        
        return map;
    }
    
    private TBDatabaseInputCTE storeTBDatabaseInputCteForEmissaoMdfe(StringBuffer xml, Filial filialSessao, Boolean hasContingenciaValidaByFilial) {
		
	    //Adiciona as tags da NDD ao xml (não estão no xsd da sefaz)
	    StringBuffer xmlToStore = integracaoNDDigitalService.executeAdicionarB2BMDfe(xml);
	    
	    //Grava o registro na tabela TBDATABASEINPUT_CTE para integração pela NDD
		return integracaoNDDigitalService.generateIntegracaoEmissaoMdfe(filialSessao.getSgFilial(), xmlToStore.toString(), hasContingenciaValidaByFilial);
		
	}

    private TBDatabaseInputCTE storeTBDatabaseInputCteForEventoMdfe(StringBuffer xml, Filial filialSessao) {
        
        //Grava o registro na tabela TBDATABASEINPUT_CTE para integração pela NDD
        return integracaoNDDigitalService.generateIntegracaoEventoMdfe(filialSessao.getSgFilial(), xml.toString());
        
    }
    
	private XmlMdfe gerarXMLEmissao(ManifestoEletronico mdfe, Boolean hasContingenciaValidaByFilial, boolean isViagem, boolean isAgrupaPorUFDestino) {
	    
	    Proprietario proprietarioFilialOrigem = proprietarioService.findByIdReturnNull(mdfe.getFilialOrigem().getIdFilial());
	    
	    MeioTransporte veiculo = mdfe.getControleCarga().getMeioTransporteByIdTransportado(); 
	    
	    
	    
        if(proprietarioFilialOrigem == null){
            proprietarioFilialOrigem = proprietarioService.findByIdReturnNull(Long.valueOf(parametroGeralService.findSimpleConteudoByNomeParametro("ID_EMPRESA_MERCURIO")));
            if(proprietarioFilialOrigem == null){
                throw new IllegalStateException("[MDFe] Sem proprietario para filial");
            }
        }

	    String nrVersaoLayout = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(mdfe.getFilialOrigem().getIdFilial(), "NR_VERSAO_MDFE", false);
	    
	    String retiraZeroInicialIe = (String) parametroGeralService.findConteudoByNomeParametro("RETIRA_ZERO_INICIAL_IE", false);
	    
	    String bairroPadrao = (String) parametroGeralService.findConteudoByNomeParametro("Bairro_padrao", false);
	    
	    String ambienteMdfe = (String) parametroGeralService.findConteudoByNomeParametro("AMBIENTE_MDFE", false);
	    
		List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes = monitoramentoDocEletronicoService.findMonitoramentosByControleCarga(mdfe.getControleCarga().getIdControleCarga());
		
		CIOT ciot = null;
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findByIdControleCargaIdMeioTransporte(mdfe.getControleCarga().getIdControleCarga(), mdfe.getControleCarga().getMeioTransporteByIdTransportado().getIdMeioTransporte());
	    if(ciotControleCarga != null){
	    	ciot = ciotControleCarga.getCiot();
	    }
	    String cnpjCiot = parametroGeralService.findConteudoByNomeParametro("CNPJ_NDD_MDFE", false).toString();
	    
		Object odtImpTagXMun = parametroGeralService.findConteudoByNomeParametro("DT_IMP_TAG_xMun", false);
	    YearMonthDay dtImpTagXMun = null;
	    if (odtImpTagXMun != null && odtImpTagXMun instanceof Date) {
	        dtImpTagXMun = new YearMonthDay(((Date)odtImpTagXMun).getTime());
	    }
	    boolean utilizarTagXMunCarrega = dtImpTagXMun == null || JTDateTimeUtils.comparaData(JTDateTimeUtils.getDataAtual(), dtImpTagXMun) >= 0;

	    
	    Object odtImpTagVeicTracao = parametroGeralService.findConteudoByNomeParametro("DT_IMP_TAG_veicTracao", false);
	    YearMonthDay dtImpTagVeicTracao = null;
	    if (odtImpTagVeicTracao != null && odtImpTagVeicTracao instanceof Date) {
	    	dtImpTagVeicTracao = new YearMonthDay(((Date)odtImpTagVeicTracao).getTime());
	    }
	    boolean utilizarTagVeicTracao = dtImpTagVeicTracao == null || JTDateTimeUtils.comparaData(JTDateTimeUtils.getDataAtual(), dtImpTagVeicTracao) >= 0;

        Filial origem = mdfe.getFilialOrigem();
        Filial destino = MdfeConverterUtils.getFilialDestinoByManifesto(mdfe);
	    
        List<FilialPercursoUF> fp = filialPercursoUFService.findFiliaisPercursoByIdFilialOrigemAndIdFilialDestino(origem.getIdFilial(), destino.getIdFilial());
        
	    List<String> filiaisPercurso = this.gerarPercursoUnidadeFederativa(fp, origem, destino);
	    
	    List<Seg> segList = doctoServicoSegurosService.gerarSegurosMdfe(mdfe);
	    
	    List<InfContratante> infContratanteList = new ArrayList<InfContratante>();
	    
	    if (!"P".equals(veiculo.getTpVinculo().getValue())) {
	    	InfContratante infContratante = new   InfContratante();	
	    	
	    	Pessoa emitente = mdfe.getFilialOrigem().getPessoa();
	    	
	    	infContratante.setCNPJ(FormatUtils.fillNumberWithZero(emitente.getNrIdentificacao(), 14));  
	    	infContratanteList.add(infContratante);
        }else{
    	  infContratanteList = devedorDocServService.gerarInfContratanteMdfe(mdfe);
        } 
	    
	    String incluirQrcodeMdfe = (String) conteudoParametroFilialService.findConteudoByNomeParametro(origem.getIdFilial(), INCLUIR_QRCODE_MDFE, false);
	    String linkQrCodeMdfe = "";
	    if("S".equalsIgnoreCase(incluirQrcodeMdfe)){
	    	linkQrCodeMdfe = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(origem.getIdFilial(), LINK_QRCODE_MDFE, false);
	    }
	    
	    List<AutXML> autXMLList = gerarAutXMLList();
	    
	    String incluirGrupoProdPredMdfe = (String) parametroGeralService.findConteudoByNomeParametro(GERAR_GRUPO_PRODPRED_MDFE, false);
	    String dsProdutoPredominante = naturezaProdutoService.findDsNaturezaProdutoByIdControleCarga(mdfe.getControleCarga().getIdControleCarga());
	    String cepCarregadoMDFE = origem.getPessoa().getEnderecoPessoa().getNrCep();
	    String cepDescarregadoMDFE = isViagem ? destino.getPessoa().getEnderecoPessoa().getNrCep() : mdfe.getConhecimentos().get(0).getNrCepEntrega();
	    
		MdfeXmlWrapper mdfeXmlWrapper = new MdfeXmlWrapper(mdfe, proprietarioFilialOrigem, nrVersaoLayout, retiraZeroInicialIe, bairroPadrao, ambienteMdfe, 
				monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao, hasContingenciaValidaByFilial, filiaisPercurso, 
				ciot, cnpjCiot, segList, infContratanteList, incluirQrcodeMdfe, linkQrCodeMdfe, autXMLList, 
				incluirGrupoProdPredMdfe, dsProdutoPredominante, cepCarregadoMDFE, cepDescarregadoMDFE);
		
		XmlMdfe xmlMdfe;
		
		try {
		    xmlMdfe = mdfeXmlWrapper.generate(isViagem, isAgrupaPorUFDestino);
		} catch (Exception e) {
		    log.error(e);
		    throw new BusinessException("LMS-05361", new Object[]{e.getMessage()});
		}
		
		return xmlMdfe;
		
	}

	private List<AutXML> gerarAutXMLList(){
        List<AutXML> retorno = new ArrayList<AutXML>();
        
        AutXML autXML = new AutXML();
        autXML.setCNPJ(parametroGeralService.findConteudoByNomeParametro("CNPJ_ANTT_MDFE", false).toString());
        retorno.add(autXML);

        return retorno;
	}
	
    /**
     * LMSA-8140: Método responsável por gerar uma lista de siglas do percurso
     * das filiais por uf.
     *
     * @param fp Filiais de percurso cadastrada.
     * @param origem Filial de origem
     * @param destino Filial de destino
     *
     * @return Retorna as siglas das rotas das unidade federativa.
     *
     * @author leonardo.carmona
     */
    protected List<String> gerarPercursoUnidadeFederativa(List<FilialPercursoUF> fp, Filial origem, Filial destino) {
        List<String> filiaisPercurso = new ArrayList<String>();
        String ufOrigem = origem.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
        String ufDestino = destino.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

        for (FilialPercursoUF filialPercursoUF : fp) {
            filiaisPercurso.add(filialPercursoUF.getUnidadeFederativa().getSgUnidadeFederativa());
        }

        // Remove o primeiro percurso se for a uf de origem.
        if (filiaisPercurso.size() > 0 && filiaisPercurso.get(0).equals(ufOrigem)) {
            filiaisPercurso.remove(0);
        }

        // Remove o último percurso se for a uf de destino.
        if (filiaisPercurso.size() > 0 && filiaisPercurso.get(filiaisPercurso.size() - 1).equals(ufDestino)) {
            filiaisPercurso.remove(filiaisPercurso.size() - 1);
        }

        return filiaisPercurso;
    }
	
    private StringBuffer gerarXMLEvento(ManifestoEletronico mdfe, Filial filialSessao, TipoEventoMdfe tipoEventoMdfe) {
        
        String nrVersaoLayout = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(mdfe.getFilialOrigem().getIdFilial(), "NR_VERSAO_MDFE", false);

        String ambienteMdfe = (String) parametroGeralService.findConteudoByNomeParametro("AMBIENTE_MDFE", false);
        
        String justificativaCancelamentoMdfe = (String) parametroGeralService.findConteudoByNomeParametro("JUST_CANC_MDFE", false);
        
        EventoMdfeXmlWrapper eventoMdfeXmlWrapper = new EventoMdfeXmlWrapper(mdfe, tipoEventoMdfe, nrVersaoLayout, ambienteMdfe, filialSessao, justificativaCancelamentoMdfe);
        StringBuffer xmlMdfe;
        
        try {
            xmlMdfe = eventoMdfeXmlWrapper.generate();
            if (xmlMdfe.indexOf("<evEncMDFe>") > -1) {
                xmlMdfe.insert(xmlMdfe.indexOf("<evEncMDFe>")+10, " xmlns=\"http://www.portalfiscal.inf.br/mdfe\"");
				xmlMdfe = this.adicionarTagB2BMDfe(xmlMdfe);
            }
            if (xmlMdfe.indexOf("<evCancMDFe>") > -1) {
                xmlMdfe.insert(xmlMdfe.indexOf("<evCancMDFe>")+11, " xmlns=\"http://www.portalfiscal.inf.br/mdfe\"");
				xmlMdfe = this.adicionarTagB2BMDfe(xmlMdfe);
            }
            System.out.println(xmlMdfe.toString());
        } catch (Exception e) {
            log.error(e);
            throw new BusinessException("LMS-05361", new Object[]{e.getMessage()});
        }
        
        return xmlMdfe;
        
    }
    
    public Integer verificaSituacao(String tpSituacao, Long idFilial) {
    	boolean reemitir = false;
    	boolean cancelar = false;
    	
    	if(ConstantesExpedicao.TP_MDFE_AUTORIZADO.equals(tpSituacao) || 
    			ConstantesExpedicao.TP_MDFE_ENCERRADO_ENVIADO.equals(tpSituacao) ||
    			ConstantesExpedicao.TP_MDFE_ENCERRADO.equals(tpSituacao) ||
    			ConstantesExpedicao.TP_MDFE_ENCERRADO_REJEITADO.equals(tpSituacao) ||
    			ConstantesExpedicao.TP_MDFE_ENVIADO_CONTINGENCIA.equals(tpSituacao)){
    		reemitir = true;
    	}
    	
    	Filial filial = SessionUtils.getFilialSessao();
    	if( ( ConstantesExpedicao.TP_MDFE_AUTORIZADO.equals(tpSituacao) || 
    			ConstantesExpedicao.TP_MDFE_ENCERRADO_REJEITADO.equals(tpSituacao) ) 
    			&& filial.getIdFilial().equals(idFilial)){
    		cancelar = true;
    	}
    	
    	if(reemitir && cancelar){
    		return 2;
    	}else if(reemitir){
    		return 1;
    	}else if(cancelar){
    		return 0;
    	}
    	
		return null;
	}
    
    @AssynchronousMethod(name = "expedicao.EncerramentoAutomaticoMDFe", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ALWAYS)
    public void executeEncerramentoDiarioAutomaticoMDFe(){
    	BigDecimal limiteDiasMDFe =(BigDecimal) parametroGeralService.findConteudoByNomeParametro("LIMITE_DIAS_ENCERRAMENTO_MDFE", false);
    	BigDecimal limiteDiasMDFeDuasUFs = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("LIMITE_DIAS_ENCERRAMENTO_MDFE_2_UF", false);
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	
		List<ManifestoEletronico> manifestos = getManifestoEletronicoDAO().findManifestosParaEncerramentoAutomatico(
				dataAtual.minusDays(limiteDiasMDFe.intValue()), dataAtual.minusDays(limiteDiasMDFeDuasUFs.intValue()));
    	for (ManifestoEletronico manifestoEletronico : manifestos) {
    		encerrarMdfe(manifestoEletronico);
		}
    }
    
    public ManifestoEletronico storeDocumentoMdfe(DocumentoDeclaracaoMdfe documentoDeclaracaoMdfe) {
	String nrChave = documentoDeclaracaoMdfe.getNrChave();
	String situacao = documentoDeclaracaoMdfe.getSituacao();
	byte[] xml = documentoDeclaracaoMdfe.getXml().getBytes(Charset.forName("UTF-8"));

	ManifestoEletronico manifestoEletronico = findByNrChave(nrChave);
        
	if(manifestoEletronico == null || situacao == null){
		return null;
	}

	if (TP_SITUACAO_MDFE_AUTORIZADO.equals(situacao)) {
		manifestoEletronico.setDsDadosAutorizacao(xml);
	} else if (TP_SITUACAO_MDFE_CANCELADO.equals(situacao)) {
		manifestoEletronico.setDsDadosCancelamento(xml);
    	} else if (TP_SITUACAO_MDFE_ENCERRADO.equals(situacao)) {
		manifestoEletronico.setDsDadosEncerramento(xml);
	}
    	
	store(manifestoEletronico);
 	return manifestoEletronico;
    }
    
    
   /**
     * O Objeto é o último ítem da lista?
     * @param list
     * @param item
     * @return
     */
    @SuppressWarnings("rawtypes")
    private boolean isLast(List list, Object item){
        return (list.indexOf(item))==(list.size()-1);
    }
    
	public List<ManifestoEletronico> findManifestoEletronicoByControleCargaAndTpSituacao(Long idControleCarga,Long idFilialLogada, String... tpSituacao) {
		return getManifestoEletronicoDAO().findManifestoEletronicoByControleCargaAndTpSituacao(idControleCarga, idFilialLogada, tpSituacao);
	}

	public List<ManifestoEletronico> findManifestoEletronicoByTpSituacao(Long idFilial, String... tpSituacao) {
		return getManifestoEletronicoDAO().findManifestoEletronicoByTpSituacao(idFilial, tpSituacao);
	}
	
	public ResultSetPage<ManifestoEletronico> findPaginatedManifestoEletronicoByControleCarga(TypedFlatMap criteria) {
		return getManifestoEletronicoDAO().findPaginatedManifestoEletronicoByControleCarga(criteria);
	}
	
    public ManifestoEletronico findByNrChave(String chave) {
        return getManifestoEletronicoDAO().findByNrChave(chave);
    }

	public java.io.Serializable store(ManifestoEletronico bean) {
		return super.store(bean);
	}
	
	@Override
	public Serializable findById(Long id) {
	    return super.findById(id);
	}
	
	public List<ManifestoEletronico> findByIds(List<Long> ids) {
		return getManifestoEletronicoDAO().findByIds(ids);
	}
	
	/**
	 * 04.01.01.32 Manter Contingencias - ReenviaMDFeConting - PASSO 2
	 * 
	 * @param mdfe
	 * @return
	 */
	public void storeReenvio(ManifestoEletronico mdfe) {
		//Para cada registro selecionado, fazer:
		//- Acessar na tabela TBDATABASEINPUT_CTE o registro de envio para o registro selecionado acima 
		//  (MANIFESTO_ELETRONICO.ID_ENVIO_E = TBDATABASEINPUT_CTE.ID) 
		//  e alterar o campo TBDATABASEINPUT_CTE.STATUS para “0” (Não processado);
		integracaoNDDigitalService.updateStatusReenvio(mdfe.getIdEnvioE());
		
	}

	public StringBuffer adicionarTagB2BMDfe(final StringBuffer xml){
		String pastaMdfeB2B = (String) parametroGeralService.findConteudoByNomeParametro("B2B_PASTA_XML_MDFE", false);

		final StringBuilder dadosAdic = new StringBuilder();
		dadosAdic.append("<dadosAdic>");

		if (StringUtils.isNotEmpty(pastaMdfeB2B)){
			dadosAdic.append("<B2BDirectory>");
			dadosAdic.append(pastaMdfeB2B);
			dadosAdic.append("</B2BDirectory>");
		}

		dadosAdic.append("</dadosAdic>");

		int index = xml.indexOf("</eventoMDFe");
		xml.insert(index, dadosAdic);
		return xml;
	}

	public void setManifestoEletronicoDAO(ManifestoEletronicoDAO dao) {
		setDao( dao );
	}

	private ManifestoEletronicoDAO getManifestoEletronicoDAO() {
		return (ManifestoEletronicoDAO) getDao();
	}
	
	public void setProprietarioService(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }
	
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }
	
	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
        this.integracaoNDDigitalService = integracaoNDDigitalService;
    }
	
	public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }
	
	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
        this.apoliceSeguroService = apoliceSeguroService;
    }
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }
	
	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
        this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
    }
	
	public void setContingenciaService(ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}

	public void setFilialPercursoUFService(FilialPercursoUFService filialPercursoUFService) {
		this.filialPercursoUFService = filialPercursoUFService;
	}

	public void setCiotControleCargaService(CIOTControleCargaService ciotControleCargaService) {
		this.ciotControleCargaService = ciotControleCargaService;
	}

	public void setDoctoServicoSegurosService(DoctoServicoSegurosService doctoServicoSegurosService) {
		this.doctoServicoSegurosService = doctoServicoSegurosService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public void setNaturezaProdutoService(
			NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

}
