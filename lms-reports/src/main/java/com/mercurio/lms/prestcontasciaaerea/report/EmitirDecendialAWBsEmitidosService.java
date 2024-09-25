package com.mercurio.lms.prestcontasciaaerea.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.prestcontasciaaerea.model.service.FaturamentoCiaAereaService;
import com.mercurio.lms.tributos.model.CalcularPisCofinsCsllIrInss;
import com.mercurio.lms.tributos.model.service.CalcularIssService;
import com.mercurio.lms.tributos.model.service.CalcularPisCofinsCsllIrInssService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * @author joseMr
 *
 * @spring.bean id="lms.prestcontasciaaerea.emitirDecendialAWBsEmitidosService"
 * @spring.property name="reportName" value="com/mercurio/lms/prestcontasciaaerea/report/emitirDecendialAWBSEmitidos.jasper"
 */
public class EmitirDecendialAWBsEmitidosService extends ReportServiceSupport {
    
    private ParametroGeralService geralService;
    private CalcularPisCofinsCsllIrInssService irInssService;
    private CalcularIssService issService;
    private EnderecoPessoaService enderecoPessoaService;
    private ConfiguracoesFacade configuracoesFacade;
    private FaturamentoCiaAereaService faturamentoCiaAereaService;
    


    private AwbService awbService;

	/** 
     * Método de execução do relatório Decendial dos AWBs Emitidos
     * @param parameters Parâmetros de filtro do relatório
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
        

		TypedFlatMap tfm = (TypedFlatMap) parameters;
        
        final String opcaoImpressao = tfm.getString("opcaoImpressao");
        
        YearMonthDay dtInicial = tfm.getYearMonthDay("dtInicial");
        YearMonthDay dtFinal   = tfm.getYearMonthDay("dtFinal");
        
        tfm.put("dtInicial", dtInicial);
        
        SqlTemplate sql = montaQuery(tfm);
        
        sql.addFilterSummary("companhiaAerea",          tfm.getString("nmCompanhiaAerea"));
        sql.addFilterSummary("opcaoImpressao",          tfm.getString("dsOpcaoImpressao"));    
        // campo filial passa a ser opcional
        if(tfm.getLong("filial.idFilial") != null){
        sql.addFilterSummary("filialPrestadoraContas",  tfm.getString("siglaFilial") + " - " + tfm.getString("nmFilialPrestadoraContas"));
        }
        sql.addFilterSummary("periodoVendasInicial",    JTFormatUtils.format(dtInicial));
        sql.addFilterSummary("periodoVendasFinal",      JTFormatUtils.format(dtFinal));
        
        if( tfm.containsKey("cliente.idCliente") && !tfm.getString("cliente.idCliente").equals("") ){
          sql.addFilterSummary("remetenteAWB", (tfm.getString("nrIdentificacao") + " - " + tfm.getString("nmRemetente")));            
        }
        
		Map dados = (Map) this.getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(this.getJdbcTemplate(), sql.getCriteria()), new ResultSetExtractor() {
            
            private static final String VARIG = "ID_VARIG";
            private static final String GOL   = "ID_GOL";
            private static final String TAM   = "ID_TAM";

            //chaves para os mapas
            /** LONG */
            private static final String AWB_INICIAL   = "AWB_INICIAL";
            /** LONG */
            private static final String AWB_FINAL   = "AWB_FINAL";
            /** String */
            private static final String AWBDV_INICIAL   = "AWBDV_INICIAL";
            /** LONG */
            private static final String NR_IDENTIFICACAO   = "NR_IDENTIFICACAO";
            /** LIST */
            private static final String INTERVALOS   = "DSINTERVALOSAWBS_";
            
            /** BIGDECIMAL */
            private static final String VLFRETEPESOCIF   = "VLFRETEPESOCIF";
            /** BIGDECIMAL */
            private static final String VLFRETEPESOFOB   = "VLFRETEPESOFOB";
            /** BIGDECIMAL */
            private static final String VLTAXATERRESTREDESTINOCIF   = "VLTAXATERRESTREDESTINOCIF";
            /** BIGDECIMAL */
            private static final String VLTAXATERRESTREDESTINOFOB   = "VLTAXATERRESTREDESTINOFOB";
            /** BIGDECIMAL */
            private static final String VLTAXACOMBUSTIVELCIF   = "VLTAXACOMBUSTIVELCIF";
            /** BIGDECIMAL */
            private static final String VLTAXACOMBUSTIVELFOB   = "VLTAXACOMBUSTIVELFOB";
                
            /** LINKEDLIST */
            private static final String DEMONSTRATIVOVENDAS   = "DEMONSTRATIVOVENDAS";
            /** LINKEDLIST */
            private static final String DEMONSTRATIVOCONTAS   = "DEMONSTRATIVOCONTAS";
            /** LINKEDLIST */
            private static final String AWBSCANCELADOS   = "AWBSCANCELADOS";
                
                
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                
            	// relatorio passa a quebrar por filial - o map deve manter a ordenacao.
            	Map mapaToReturm = new  LinkedHashMap();
                
                //intervalos
                long qtAwbCifGol                        = 0;
                long qtAwbCifOutras                     = 0;
                long qtAwbFob                           = 0;
                BigDecimal psTotalCif                   = new BigDecimal(0);
                BigDecimal psTotalFob                   = new BigDecimal(0);
                
                boolean iniciaSequencia = true;
                
                while(rs.next()){    
                	long idFilial       = Long.valueOf(rs.getLong("ID_FILIAL"));
                    
                	Map mapa = new HashMap();
                	if(mapaToReturm.containsKey(idFilial)){
                		mapa = (HashMap) mapaToReturm.get(idFilial);
                	} else {
                		mapaToReturm.put(idFilial,mapa);
                    
                		// inicializa valores padrão
                		mapa.put(this.VLFRETEPESOCIF, BigDecimal.ZERO);
                		mapa.put(this.VLFRETEPESOFOB, BigDecimal.ZERO);
                		mapa.put(this.VLTAXATERRESTREDESTINOCIF, BigDecimal.ZERO);
                		mapa.put(this.VLTAXATERRESTREDESTINOFOB, BigDecimal.ZERO);
                		mapa.put(this.VLTAXACOMBUSTIVELCIF, BigDecimal.ZERO);
                		mapa.put(this.VLTAXACOMBUSTIVELFOB, BigDecimal.ZERO);
                    
                		mapa.put(this.DEMONSTRATIVOVENDAS, new LinkedList());
                		mapa.put(this.DEMONSTRATIVOCONTAS, new LinkedList());
                		mapa.put(this.AWBSCANCELADOS, new LinkedList());
                		
                    	mapa.put(this.INTERVALOS, new ArrayList<Map>());
                    	iniciaSequencia = true;
                    }
                    
                	long awbCurrent = rs.getLong("NR_AWB");
                	String awbStatus = rs.getString("TP_STATUS_AWB");
                	String awbTpFrete = rs.getString("TP_FRETE");
                	String awbDivCurrent  = rs.getString("DV_AWB");
                    mapa.put(this.NR_IDENTIFICACAO, rs.getString("NR_IDENTIFICACAO"));
                    BigDecimal fretePeso = rs.getBigDecimal("VL_FRETE_PESO");
                    BigDecimal taxaTerOrigem  = rs.getBigDecimal("VL_TAXA_COMBUSTIVEL");
                    BigDecimal taxaTerDestino = rs.getBigDecimal("VL_TAXA_TERRESTRE");
                    BigDecimal psTotal        = rs.getBigDecimal("PS_TOTAL"); 
                    mapa.put("NM_CIA_AEREA",rs.getString("NM_CIA_AEREA"));
                    mapa.put("NM_FILIAL",rs.getString("NM_FILIAL"));
                    
                    mapa.put("ID_CIA_AEREA",rs.getLong("ID_EMPRESA"));
                    mapa.put("ID_FILIAL",idFilial);
                    
                    if (! mapa.containsKey(this.AWB_FINAL) ) {
                    	mapa.put(this.AWB_FINAL, awbCurrent);
                    }
                    
                    //Verifica quebra de intervalo
                    //Intervalo unico (sem quebras) é incluido após loop
                    //Se o número atual é maior que o número anterior + 1: sequencia quebrada
                    if (awbCurrent > (((Long)mapa.get(this.AWB_FINAL)) +1) ){
                    	mapa.put(this.AWB_INICIAL, awbCurrent);
                    	mapa.put(this.AWBDV_INICIAL, awbDivCurrent);
                        
                        Map valIntervalos = new HashMap();
                        valIntervalos.put("AWBS_DE"  ,((((Long)mapa.get(this.AWB_INICIAL)) ).toString() + "-" + mapa.get(this.AWBDV_INICIAL) ));
                        valIntervalos.put("AWBS_ATE" ,((((Long)mapa.get(this.AWB_INICIAL)) ).toString() + "-" + awbDivCurrent));
                        valIntervalos.put("AWBS_QTDE", Long.valueOf(1));
                        
                        //Intervalo
                        List<Map> intervalos = (List<Map>) mapa.get(this.INTERVALOS);
                        intervalos.add( valIntervalos );
                        //Inicia novo intervalo
                    	iniciaSequencia = true;
                    } else {
                        
                    	List<Map> intervalos = ((List<Map>)mapa.get(this.INTERVALOS));
                    	if(iniciaSequencia){
                    		iniciaSequencia = false;
                    		
                            //Inicializacao, primeira iterecao
                        	mapa.put(this.AWB_INICIAL, awbCurrent);
                        	mapa.put(this.AWBDV_INICIAL, awbDivCurrent);

                    		Map valIntervalos = new HashMap();
                            valIntervalos.put("AWBS_DE"  ,((((Long)mapa.get(this.AWB_INICIAL)) ).toString() + "-" + mapa.get(this.AWBDV_INICIAL) ));
                            valIntervalos.put("AWBS_ATE" ,((((Long)mapa.get(this.AWB_INICIAL)) ).toString() + "-" + awbDivCurrent));
                            valIntervalos.put("AWBS_QTDE", Long.valueOf(1));
                            intervalos.add(valIntervalos);
                    		
                    } else {
                    		long qtd = ((Long)intervalos.get(intervalos.size()-1).get("AWBS_QTDE")).intValue();
                    		intervalos.get(intervalos.size()-1).put("AWBS_QTDE",++qtd);
                    		intervalos.get(intervalos.size()-1).put("AWBS_ATE" ,(awbCurrent + "-" + awbDivCurrent));
                    }
                    }

                    //Final do intervalo e' sempre o nrAwb corrente
                    mapa.put(this.AWB_FINAL, awbCurrent);

                    if (awbStatus != null && awbStatus.equals("C")){
                        Map valAWBCancelado = new HashMap();                        
                        valAWBCancelado.put("AWB_CANCELADO",(awbCurrent + "-" + awbDivCurrent));
                        ((LinkedList)  mapa.get(this.AWBSCANCELADOS)).add(valAWBCancelado);
                    }
                   
                    if(awbStatus != null && !awbStatus.equals("C")){
	                    if (awbTpFrete != null && awbTpFrete.equals("C")){                    	
	                    	
	                    	mapa.put(this.VLFRETEPESOCIF,
	                    			fretePeso != null ? ((BigDecimal) mapa.get(this.VLFRETEPESOCIF)).add(fretePeso) : ((BigDecimal) mapa.get(this.VLFRETEPESOCIF)) );
	                        
	                        if(  ((Long)mapa.get("ID_CIA_AEREA")).toString().equals(buscaIDEmpresa(GOL)) ){
	                            qtAwbCifGol++;
	                        } else {
	                            qtAwbCifOutras++;
	                        }
	                        
	                        psTotalCif = psTotal != null ? psTotalCif.add(psTotal) : psTotalCif.add(new BigDecimal(0));
	                        
	                        mapa.put(this.VLTAXATERRESTREDESTINOCIF,
	                        		taxaTerDestino != null ? ((BigDecimal)mapa.get(this.VLTAXATERRESTREDESTINOCIF)).add(taxaTerDestino) : ((BigDecimal)mapa.get(this.VLTAXATERRESTREDESTINOCIF)) );
	                        
	                        mapa.put(this.VLTAXACOMBUSTIVELCIF ,
	                        		taxaTerOrigem != null ? ((BigDecimal)mapa.get(this.VLTAXACOMBUSTIVELCIF)).add(taxaTerOrigem) : ((BigDecimal)mapa.get(this.VLTAXACOMBUSTIVELCIF)) );
	                    }else{
	                    	mapa.put(this.VLFRETEPESOFOB ,
	                    			fretePeso != null ? ((BigDecimal)mapa.get(this.VLFRETEPESOFOB)).add(fretePeso) : ((BigDecimal)mapa.get(this.VLFRETEPESOFOB)));
	                    	
	                        qtAwbFob++;
	                        psTotalFob = psTotal!= null ? psTotalFob.add(psTotal) : psTotalFob.add(new BigDecimal(0));
	                        
	                        mapa.put(this.VLTAXATERRESTREDESTINOFOB ,
	                        		taxaTerDestino != null ? ((BigDecimal)mapa.get(this.VLTAXATERRESTREDESTINOFOB)).add(taxaTerDestino) : ((BigDecimal)mapa.get(this.VLTAXATERRESTREDESTINOFOB)));
	                        
	                        mapa.put(this.VLTAXACOMBUSTIVELFOB,
	                        		taxaTerOrigem != null ? ((BigDecimal)mapa.get(this.VLTAXACOMBUSTIVELFOB)).add(taxaTerOrigem) : ((BigDecimal)mapa.get(this.VLTAXACOMBUSTIVELFOB)));
	                    }
                    }
                
               
                        
                    Map valDemonstrativoVendas = new HashMap();
                    
                    valDemonstrativoVendas.put("FRETE_FOB",             mapa.get(this.VLFRETEPESOFOB));
                    valDemonstrativoVendas.put("TAXA_TER_ORIGEM_FOB",   mapa.get(this.VLTAXACOMBUSTIVELFOB));
                    valDemonstrativoVendas.put("TAXA_TER_DESTINO_FOB",  mapa.get(this.VLTAXATERRESTREDESTINOFOB));
                    valDemonstrativoVendas.put("AD_VALOREM_FOB",        new BigDecimal(0));
                    valDemonstrativoVendas.put("FRAP_DEVOLVIDO_FOB",    new BigDecimal(0));
                    
                    if( mapa.get("NM_FILIAL").equals(buscaIDEmpresa(GOL)) ){
                    
                        valDemonstrativoVendas.put("FRETE_CC",              mapa.get(this.VLFRETEPESOCIF));
                        valDemonstrativoVendas.put("TAXA_TER_ORIGEM_CC",    mapa.get(this.VLTAXACOMBUSTIVELCIF));
                        valDemonstrativoVendas.put("TAXA_TER_DESTINO_CC",   mapa.get(this.VLTAXATERRESTREDESTINOCIF));
                        valDemonstrativoVendas.put("AD_VALOREM_CC",         new BigDecimal(0));
                        valDemonstrativoVendas.put("FRAP_DEVOLVIDO_CC",     new BigDecimal(0));
                        
                        valDemonstrativoVendas.put("FRETE_PAGOS",           new BigDecimal(0));
                        valDemonstrativoVendas.put("TAXA_TER_ORIGEM_PAGOS", new BigDecimal(0));
                        valDemonstrativoVendas.put("TAXA_TER_DESTINO_PAGOS",new BigDecimal(0));
                        valDemonstrativoVendas.put("AD_VALOREM_PAGOS",      new BigDecimal(0));
                        valDemonstrativoVendas.put("FRAP_DEVOLVIDO_PAGOS",  new BigDecimal(0));
                    
                    } else {
                       
                        valDemonstrativoVendas.put("FRETE_PAGOS",           mapa.get(this.VLFRETEPESOCIF));
                        valDemonstrativoVendas.put("TAXA_TER_ORIGEM_PAGOS", mapa.get(this.VLTAXACOMBUSTIVELCIF));
                        valDemonstrativoVendas.put("TAXA_TER_DESTINO_PAGOS",mapa.get(this.VLTAXATERRESTREDESTINOCIF));
                        valDemonstrativoVendas.put("AD_VALOREM_PAGOS",      new BigDecimal(0));
                        valDemonstrativoVendas.put("FRAP_DEVOLVIDO_PAGOS",  new BigDecimal(0));
                        
                        valDemonstrativoVendas.put("FRETE_CC",              new BigDecimal(0));
                        valDemonstrativoVendas.put("TAXA_TER_ORIGEM_CC",    new BigDecimal(0));
                        valDemonstrativoVendas.put("TAXA_TER_DESTINO_CC",   new BigDecimal(0));
                        valDemonstrativoVendas.put("AD_VALOREM_CC",         new BigDecimal(0));
                        valDemonstrativoVendas.put("FRAP_DEVOLVIDO_CC",     new BigDecimal(0));
                    
                    }
                    
                    valDemonstrativoVendas.put("QTY_FRETE_FOB",     Long.valueOf(qtAwbFob));
                    valDemonstrativoVendas.put("QTY_FRETE_CC",      Long.valueOf(qtAwbCifGol));
                    valDemonstrativoVendas.put("QTY_FRETE_PAGOS",   Long.valueOf(qtAwbCifOutras));
                    
                    ((LinkedList) mapa.get(this.DEMONSTRATIVOVENDAS)).add(valDemonstrativoVendas);
                    
                    
					
                    // contas
                    Map contas = new HashMap();
                    
                    BigDecimal vlTotalFrete = ((BigDecimal)mapa.get(this.VLFRETEPESOCIF)).add((BigDecimal)mapa.get(this.VLFRETEPESOFOB));                
                    
                    Long idServicoTributo = null;
                    idServicoTributo = Long.valueOf(((BigDecimal)configuracoesFacade.getValorParametro("ID_AGENCIAMENTO_CARGA_AEREA")).longValue());
                    
                    BigDecimal totalFreteContas = ((BigDecimal)mapa.get(this.VLFRETEPESOCIF)).add((BigDecimal)mapa.get(this.VLFRETEPESOFOB));
                    BigDecimal comissaoFrete    = totalFreteContas.multiply( getFaturamentoCiaAereaService().findPcComissaoCiaAerea( rs.getLong("ID_EMPRESA") , idFilial) ).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
                    
                    BigDecimal irSobreComissao  = new BigDecimal(0);
                    BigDecimal vlIss            = new BigDecimal(0);
    
                    if( ((Long)mapa.get("ID_CIA_AEREA")).toString().equals(buscaIDEmpresa(VARIG)) ){
                        
                        List list = getIrInssService().calcularPisCofinsCsllIrInssPessoaJudirica( (Long)idFilial,
                                                                                                  "I",
                                                                                                  "ME",
                                                                                                  null,
                                                                                                  idServicoTributo,
                                                                                                  JTDateTimeUtils.getDataAtual(),
                                                                                                  vlTotalFrete);
                        Iterator iter = list.iterator();
                        CalcularPisCofinsCsllIrInss inss = null;
                        
                        while (iter.hasNext()){
                            inss = (CalcularPisCofinsCsllIrInss) iter.next();
                            if ("IR".equals(inss.getTpImpostoCalculado())){
                                break;
                            }
                        }
                        
                        if (inss != null){
                            irSobreComissao = inss.getVlImposto();
                        }
                                            
                    } else if( ((Long)mapa.get("ID_CIA_AEREA")).toString().equals(buscaIDEmpresa(GOL)) || ((Long)mapa.get("ID_CIA_AEREA")).toString().equals(buscaIDEmpresa(TAM))){

                        if( mapa.get(this.NR_IDENTIFICACAO) != null && !mapa.get(this.NR_IDENTIFICACAO).equals("95591723011587") && ! ((Long)mapa.get("ID_CIA_AEREA")).toString().equals(buscaIDEmpresa(GOL))) {
                            
                            EnderecoPessoa enderecoPessoa = getEnderecoPessoaService().findEnderecoPessoaPadrao( (Long) idFilial);
                            
                            Map map = getIssService().calcularIss( (Long) idFilial,
                                                                   enderecoPessoa.getMunicipio().getIdMunicipio(), 
                                                                   enderecoPessoa.getMunicipio().getIdMunicipio(), 
                                                                   null, 
                                                                   idServicoTributo,
                                                                   JTDateTimeUtils.getDataAtual(), 
                                                                   vlTotalFrete);
                            
                            if (map != null && map.containsKey("vlIss")){
                                vlIss = (BigDecimal) map.get("vlIss");
                            }                        
                            
                        }
                    }
                    
                    contas.put("TOTAL_FRETE", totalFreteContas);
                    contas.put("IR", irSobreComissao);
                    contas.put("ISS", vlIss);
                    contas.put("COMISSAO_FRETE", comissaoFrete);                    
                    contas.put("VALOR_AGENTE", totalFreteContas.add(irSobreComissao).subtract(comissaoFrete));
                    
                    ((Collection)mapa.get(this.DEMONSTRATIVOCONTAS)).add(contas);
                    
                
                }
                
                mapaToReturm.put("psTotal",               psTotalCif.add(psTotalFob));
                
                return mapaToReturm;
            }                      
            
        });
		
		Map parametersReport = new HashMap();
		List listaPrincipal  = new ArrayList();
        
		for (Object key : dados.keySet()) {
			// se o valor da chave é um map, então esta tratando de um registro do relatorio, 
			// caso contrário, esta com um parametro
			Object value = dados.get(key); 
			if( value instanceof Map ){
				Map valueMap =(Map) value; 
        	
				JRDataSource dsAWBsCancelados = null;
	            //caso não tenha dados ira mostrar somente o titulo do subrelatorio
	            if ( ((List)valueMap.get("AWBSCANCELADOS")).size() == 0 ){
	            	dsAWBsCancelados        =  new JREmptyDataSource();
	            }else{
	            	dsAWBsCancelados        = new JRMapCollectionDataSource((Collection) valueMap.get("AWBSCANCELADOS"));
	            }
        	
        
	            valueMap.put("dsDemonstrativoICMS" ,  new JRMapCollectionDataSource(montaDadosDemonstrativoICMS( (Long) valueMap.get("ID_FILIAL"), tfm)));
	            valueMap.put("dsIntervalosAWBs", new JRMapCollectionDataSource( ((Collection)valueMap.get("DSINTERVALOSAWBS_"))));
				valueMap.put("dsDemonstrativoVendas", new JRMapCollectionDataSource( ((Collection)valueMap.get("DEMONSTRATIVOVENDAS"))));
				valueMap.put("dsDemonstrativoContas", new JRMapCollectionDataSource( ((Collection)valueMap.get("DEMONSTRATIVOCONTAS"))));
				valueMap.put("dsAWBsCancelados",      dsAWBsCancelados);
            
				listaPrincipal.add(valueMap);
            }else{
				parameters.put(key, value);
            }
		}
            
            parametersReport.put("psTotal",               (BigDecimal) dados.get("psTotal"));
            
        parametersReport.put("dsOpcaoImpressao",            tfm.getString("dsOpcaoImpressao"));

            // seta a opcao de impressao para definir o título
            parametersReport.put("opcaoImpressao", opcaoImpressao);
        parametersReport.put("usuarioEmissor",      SessionUtils.getUsuarioLogado().getNmUsuario());        
        parametersReport.put("parametrosPesquisa",  sql.getFilterSummary());

        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tfm.getString("tpFormatoRelatorio"));
        
        JRMapCollectionDataSource jrPrincipal = new JRMapCollectionDataSource(listaPrincipal);        
		
		return createReportDataObject(jrPrincipal,parametersReport); 		
	
	}
    
    /**
     * Busca os dados para o subrelatório de Demonstrativo do ICMS
     * @param tfm Critérios de pesquisa
     * @return List contendo os dados do subrelatório
     * @throws Exception
     */
    private List montaDadosDemonstrativoICMS(Long idFilial, TypedFlatMap tfm) throws Exception {
        
        List retorno = new ArrayList();
        
        List lista = getAwbService().findPrestacaoContasICMS(idFilial, 
                                                             tfm.getLong("empresa.idEmpresa"), 
                                                             tfm.getYearMonthDay("dtInicial"),
                                                             tfm.getYearMonthDay("dtFinal"));
        
        Map map = null;
        
        for (Iterator iter = lista.iterator(); iter.hasNext();) {
            
            map = (Map) iter.next();
            
    		BigDecimal pcAliquotaICMS = new BigDecimal(0);
    		BigDecimal vlFrete = new BigDecimal(0); 
			BigDecimal vlTaxa = new BigDecimal(0);
			BigDecimal vlICMS = new BigDecimal(0);
    		
			if (map.get("pcAliquotaICMS")!=null) {
				pcAliquotaICMS = (BigDecimal)map.get("pcAliquotaICMS");
			}

			if (map.get("vlFrete")!=null) {
				vlFrete = (BigDecimal)map.get("vlFrete");
			}
			
			if (map.get("vlTaxa")!=null) {
				vlTaxa = (BigDecimal)map.get("vlTaxa");
			}

			if (map.get("vlICMS")!=null) {
				vlICMS = (BigDecimal)map.get("vlICMS");
			}

            map = new HashMap();
            
            map.put("PC_ALIQUOTA", pcAliquotaICMS);
            map.put("VL_FRETE", vlFrete);
            map.put("VL_TAXA", vlTaxa);
            map.put("VL_ICMS", vlICMS);
            
            retorno.add(map);            
            
        }
        
        return retorno;
    }

    /**
     * Monta a query principal do relatório. Gera dados para todos os relatórios e subrelatórios
     * @param tfm Critérios de pesquisa
     * @return SqlTemplate contendo a query e os parâmetros
     * @throws Exception
     */
    private SqlTemplate montaQuery(TypedFlatMap tfm) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection("CFM.ID_CIA_FILIAL_MERCURIO AS ID_CIA_AEREA, " +
                          "P.NM_PESSOA AS NM_CIA_AEREA, " +
                          "F.ID_FILIAL AS ID_FILIAL, " +
                          "F.SG_FILIAL || ' - ' || FP.NM_FANTASIA AS NM_FILIAL, " +
                          "AWB.NR_AWB, " +
                          "AWB.DV_AWB, " +
                          "AWB.TP_STATUS_AWB, " +
                          "AWB.TP_FRETE, " +  
                          "AWB.PS_TOTAL, " +
                          "AWB.PC_ALIQUOTA_ICMS, " +
                          "AWB.VL_ICMS, " +
                          "AWB.VL_FRETE_PESO, " +
                          "AWB.VL_TAXA_COMBUSTIVEL, " +
                          "AWB.VL_TAXA_TERRESTRE, " +
                          "E.ID_EMPRESA," +
                          "FP.NR_IDENTIFICACAO");
        
        sql.addFrom("CIA_FILIAL_MERCURIO CFM, " +
                    "EMPRESA E, " +
                    "FILIAL F, " +
                    "PESSOA P, " +
                    "PESSOA FP, " +
                    "AWB AWB");
        
        sql.addJoin("AWB.ID_CIA_FILIAL_MERCURIO","CFM.ID_CIA_FILIAL_MERCURIO");
        sql.addJoin("CFM.ID_EMPRESA"            ,"E.ID_EMPRESA");
        sql.addJoin("E.ID_EMPRESA"              ,"P.ID_PESSOA");
        sql.addJoin("CFM.ID_FILIAL"             ,"F.ID_FILIAL");
        sql.addJoin("F.ID_FILIAL"               ,"FP.ID_PESSOA");
        
        sql.addCriteria("E.ID_EMPRESA","=",tfm.getLong("empresa.idEmpresa"));
        
        if (tfm.getLong("filial.idFilial") != null){
        sql.addCustomCriteria("((F.ID_FILIAL              =  ? AND " +
                              "  F.ID_FILIAL_RESPONSAVEL_AWB is null) OR " +
                              "  F.ID_FILIAL_RESPONSAVEL_AWB = ? )");
        sql.addCriteriaValue(tfm.getLong("filial.idFilial"));
        sql.addCriteriaValue(tfm.getLong("filial.idFilial"));
        }
        
        sql.addCustomCriteria("(AWB.TP_LOCAL_EMISSAO      = ? OR  ? = 'R')");
        sql.addCriteriaValue(tfm.getString("opcaoImpressao"));
        sql.addCriteriaValue(tfm.getString("opcaoImpressao"));
        
        sql.addCustomCriteria("AWB.TP_STATUS_AWB IN ('E', 'C')");        
        
        if( tfm.containsKey("cliente.idCliente") && !tfm.getString("cliente.idCliente").equals("") ){ 
            sql.addCriteria("AWB.ID_CLIENTE_EXPEDIDOR","=",tfm.getLong("cliente.idCliente"));
        }
        
        sql.addGroupBy("CFM.ID_CIA_FILIAL_MERCURIO," +                       
                       "P.NM_PESSOA, " +
                       "F.ID_FILIAL, " +
                       "F.SG_FILIAL || ' - ' || FP.NM_FANTASIA, " +  
                       "AWB.NR_AWB, " +
                       "AWB.DV_AWB, " +
                       "AWB.TP_STATUS_AWB, " +
                       "AWB.TP_FRETE, " +
                       "AWB.PS_TOTAL, " +
                       "AWB.PC_ALIQUOTA_ICMS, " +
                       "AWB.VL_ICMS, " +
                       "AWB.VL_FRETE_PESO, " +
                       "AWB.VL_TAXA_COMBUSTIVEL, " +
                       "AWB.VL_TAXA_TERRESTRE, " +
                       "E.ID_EMPRESA," +
                       "FP.NR_IDENTIFICACAO");
        
        sql.addOrderBy("F.SG_FILIAL || ' - ' || FP.NM_FANTASIA, AWB.NR_AWB");
        
        return sql;
    }  
    
    /**
     * Busca o conteúdo (normalmente, identificador) de uma determinada empresa na tabela de parâmetros gerais
     * @param empresa Nome da companhia aérea, exemplo : VARIG, TAM, GOL
     * @return String do conteúdo
     */
    private String buscaIDEmpresa(String empresa){
        return configuracoesFacade.getValorParametro(empresa).toString();
    }

    /**
     * @return Returns the geralService.
     */
    public ParametroGeralService getGeralService() {
        return geralService;
    }

    /**
     * @param geralService The geralService to set.
     */
    public void setGeralService(ParametroGeralService geralService) {
        this.geralService = geralService;
    }


    /**
     * @return Returns the awbService.
     */
    public AwbService getAwbService() {
        return awbService;
    }


    /**
     * @param awbService The awbService to set.
     */
    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }


    /**
     * @return Returns the irInssService.
     */
    public CalcularPisCofinsCsllIrInssService getIrInssService() {
        return irInssService;
    }


    /**
     * @param irInssService The irInssService to set.
     */
    public void setIrInssService(CalcularPisCofinsCsllIrInssService irInssService) {
        this.irInssService = irInssService;
    }


    /**
     * @return Returns the issService.
     */
    public CalcularIssService getIssService() {
        return issService;
    }


    /**
     * @param issService The issService to set.
     */
    public void setIssService(CalcularIssService issService) {
        this.issService = issService;
    }


    /**
     * @return Returns the enderecoPessoaService.
     */
    public EnderecoPessoaService getEnderecoPessoaService() {
        return enderecoPessoaService;
    }


    /**
     * @param enderecoPessoaService The enderecoPessoaService to set.
     */
    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
    
    public FaturamentoCiaAereaService getFaturamentoCiaAereaService() {
		return faturamentoCiaAereaService;
}

	public void setFaturamentoCiaAereaService(
			FaturamentoCiaAereaService faturamentoCiaAereaService) {
		this.faturamentoCiaAereaService = faturamentoCiaAereaService;
	}
}

