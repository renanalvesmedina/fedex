package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServ;
import com.mercurio.lms.tributos.model.AliquotaIssMunicipioServ;
import com.mercurio.lms.tributos.model.ParametroIssMunicipio;
import com.mercurio.lms.tributos.model.ServicoOficialTributo;
import com.mercurio.lms.tributos.model.ServicoTributo;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * Classe de serviço para CRUD: Não inserir documentação após ou remover a tag
 * do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.tributos.calcularIssService"
 */
public class CalcularIssService {
    
    ServicoTributoService servicoTributoService;
    ServicoAdicionalService servicoAdicionalService;
    ServicoOficialTributoService servicoOficialTributoService;
    AliquotaIssMunicipioServService aliquotaIssMunicipioServService;
    AliquotaContribuicaoServService aliquotaContribuicaoServService;
    PessoaService pessoaService;
    MunicipioService municipioService;
    private UnidadeFederativaService unidadeFederativaService;
    private ParametroIssMunicipioService parametroIssMunicipioService;
    
    /**
     * Calcula o valor do ISS para cias aereas. não possui cliente
     * 
     * @param idMunicipioSede
     * @param idMunicipioPrestacaoServico
     * @param idServicoAdicional
     * @param idServicoTributo
     * @param dtBase
     * @param vlBase
     * @return Map com os seguintes campos: vlIss 
     *                                      vlIssEmbutido 
     *                                      pcAliquotaIss
     *                                      nrServicoOficial 
     *                                      nrServicoMunicipio 
     *                                      idMunicipioIncidencia
     *                                      blRetencaoTomador 
     *                                      obIss
     */
    public Map calcularIssCiaAerea(Long idMunicipioSede,
                           Long idMunicipioPrestacaoServico, 
                           Long idServicoAdicional,
                           Long idServicoTributo, 
                           YearMonthDay dtBase, 
                           BigDecimal vlBase) {

    	/* Chama rotina de cálculo passando null para o cliente */
    	return calcularIss(null, idMunicipioSede,
                idMunicipioPrestacaoServico, 
                idServicoAdicional,
                idServicoTributo, 
                dtBase, 
                vlBase);
    }
   
    /**
     * Calcula o valor do ISS
     * 
     * @param idCliente
     * @param idMunicipioSede
     * @param idMunicipioPrestacaoServico
     * @param idServicoAdicional
     * @param idServicoTributo
     * @param dtBase
     * @param vlBase
     * @return Map com os seguintes campos: vlIss 
     *                                      vlIssEmbutido 
     *                                      pcAliquotaIss
     *                                      nrServicoOficial 
     *                                      nrServicoMunicipio 
     *                                      idMunicipioIncidencia
     *                                      blRetencaoTomador 
     *                                      obIss
     */
    public Map<String, Object> calcularIss(Long idCliente, 
                           Long idMunicipioSede,
                           Long idMunicipioPrestacaoServico, 
                           Long idServicoAdicional,
                           Long idServicoTributo, 
                           YearMonthDay dtBase, 
                           BigDecimal vlBase) {
        
        /* ITEM 1 */
        if( ( idServicoAdicional == null && idServicoTributo == null ) || ( idServicoAdicional != null && idServicoTributo != null ) ){
            throw new BusinessException("LMS-23007");
        }
        
        /* ITEM 2 */
        Long idServicoOficialTributo                = null;
        Long idMunicipioIncidencia                  = null;
        ServicoOficialTributo servicoOficialTributo = null;
        BigDecimal vlIssEmbutido                    = null;  
        BigDecimal vlIss                            = null; 
        BigDecimal pcAliquotaIss 					= null;
        
        if( idServicoAdicional == null ){         
            idServicoOficialTributo = this.getServicoTributoService().findIdentificadorServicoOficialTributo(idServicoTributo, dtBase);            
        } else {
            idServicoOficialTributo = this.getServicoAdicionalService().findIdentificadorServicoOficialTributo(idServicoAdicional, dtBase);
        }        
        
        /* ITEM 3 */
        Map<String, Object> criterios = new HashMap<String, Object>();
        
        criterios.put("idServicoOficialTributo",idServicoOficialTributo);
        criterios.put("tpSituacao","A");
        
        List servicoOficialTributoList = this.getServicoOficialTributoService().find(criterios);
        
        servicoOficialTributo = (ServicoOficialTributo) servicoOficialTributoList.get(0);                
        
        UnidadeFederativa uf = unidadeFederativaService.findUFByIdMunicipio(idMunicipioSede);
        
        /* ITEM 4 */
        if( servicoOficialTributo.getTpLocalDevido().getValue().equals("S") || (uf != null && uf.getBlIncideIss().equals(Boolean.TRUE)) ){
            idMunicipioIncidencia = idMunicipioSede;
        } else {
            idMunicipioIncidencia = idMunicipioPrestacaoServico;
        }
        
        Municipio municipio = getMunicipioService().findById(idMunicipioIncidencia); 
        
        List aliquotas = this.getAliquotaIssMunicipioServService().findAliquotaIssMunicipioServicoByCriterios(idServicoAdicional,
                                                                                                              idServicoTributo,
                                                                                                              idMunicipioIncidencia,
                                                                                                              dtBase,
                                                                                                              Boolean.FALSE);
        
        AliquotaIssMunicipioServ aliquotaIssMunicipioServ = null;
        
        if( aliquotas != null && !aliquotas.isEmpty() ){
            aliquotaIssMunicipioServ = (AliquotaIssMunicipioServ) aliquotas.get(0);
        } else {
            
        	Object[] arrayObj = new Object[4];
        	arrayObj[1] = municipio.getNmMunicipio();
        	
        	if( idServicoAdicional != null ){ 
        		ServicoAdicional sa = servicoAdicionalService.findById(idServicoAdicional);
        		arrayObj[2] = sa.getDsServicoAdicional().getValue(); 
        	} else {
        		ServicoTributo st = servicoTributoService.findById(idServicoTributo);
        		arrayObj[2] = st.getDsServicoTributo();
        	}
        	
        	arrayObj[3] = JTFormatUtils.format(dtBase);
        	
        	throw new BusinessException("LMS-23026",arrayObj);
            
        }
        
        /* ITEM 5 */
        boolean blArredondamentoIss = false;
        ParametroIssMunicipio pim = parametroIssMunicipioService.findByMunicipio(idMunicipioSede);
        if (pim != null) {
        	blArredondamentoIss = Boolean.TRUE.equals(pim.getBlArredondamentoIss());
        }
        
        /* ITEM 6 */
        
        Boolean blRetencaoTomador = null;
        Boolean blRetencaoMunicipio = null;
        
        blRetencaoTomador = servicoOficialTributo.getBlRetencaoTomadorServico();
        blRetencaoMunicipio = (aliquotaIssMunicipioServ.getBlRetencaoTomadorServico() == null ? Boolean.FALSE : aliquotaIssMunicipioServ.getBlRetencaoTomadorServico());
        
        if( blRetencaoTomador.equals(Boolean.FALSE) ){
        	if( blRetencaoMunicipio.equals(Boolean.TRUE) ){
        		blRetencaoTomador = Boolean.TRUE;
        	}
        }
        
        pcAliquotaIss = aliquotaIssMunicipioServ.getPcAliquota();
        
        RoundingMode roundingMode = blArredondamentoIss ? RoundingMode.HALF_UP : RoundingMode.DOWN;
		vlIss = vlBase.multiply(pcAliquotaIss).divide(new BigDecimal(100), 2, roundingMode);
        
        boolean bRetencaoIssCliente = false;

        /* ITEM 7 */
        AliquotaContribuicaoServ aliquotaContribuicaoServ = null;
        if (idCliente!=null){
	        aliquotaContribuicaoServ = this.getAliquotaContribuicaoServService().findAliquotaContribuicaoServ("IS", idServicoAdicional, 
	                                                                                                          idServicoTributo, 
	                                                                                                          idCliente, 
	                                                                                                          dtBase,
	                                                                                                          idMunicipioIncidencia);
        
	        bRetencaoIssCliente = aliquotaContribuicaoServ != null;
        
        }
        
        if(bRetencaoIssCliente){
            vlIssEmbutido = vlBase;
            blRetencaoTomador = Boolean.TRUE;
            idMunicipioIncidencia = idMunicipioPrestacaoServico;
            
            if (aliquotaContribuicaoServ.getPcAliquota() != null && aliquotaContribuicaoServ.getPcAliquota().signum() > 0) {
            	
        		pcAliquotaIss		  	= aliquotaContribuicaoServ.getPcAliquota();
        		
        		vlIss = vlBase.multiply(pcAliquotaIss).divide(new BigDecimal(100), 2, roundingMode);
        		
            }

        } else if( aliquotaIssMunicipioServ.getPcEmbute().intValue() == 0 ){
            vlIssEmbutido = vlBase;
            if( blRetencaoTomador.equals(Boolean.TRUE) ){
            	if( pessoaService.validateTipoPessoa(idCliente,"F") ){
            		blRetencaoTomador = Boolean.FALSE;
            	}
            }
        } else {
            
            if( blRetencaoTomador.booleanValue() ){
                if( pessoaService.validateTipoPessoa(idCliente,"J") ){
                    vlIssEmbutido = vlBase;
                } else {
                    
                    vlIssEmbutido = vlBase.add(vlBase.multiply(aliquotaIssMunicipioServ.getPcEmbute()).divide(new BigDecimal(100),2,roundingMode));

                    blRetencaoTomador = Boolean.FALSE;
                }
            } else {
                
            	vlIssEmbutido = vlBase.add(vlBase.multiply(aliquotaIssMunicipioServ.getPcEmbute()).divide(new BigDecimal(100),2,roundingMode));
                
            }
            
        }
        
        //LMS-5109
        BigDecimal vlLimiteRetencaoIss = null;
        ParametroIssMunicipio parametroIssMunicipioIncidencia = parametroIssMunicipioService.findByMunicipio(idMunicipioIncidencia);
        if(parametroIssMunicipioIncidencia == null){
        	vlLimiteRetencaoIss = BigDecimal.ZERO;
        } else {
        	vlLimiteRetencaoIss = parametroIssMunicipioIncidencia.getVlLimiteRetencaoIss();
        }
        
        if(Boolean.TRUE.equals(blRetencaoTomador) && vlBase.compareTo(vlLimiteRetencaoIss) < 0){
        	blRetencaoTomador = Boolean.FALSE;
        }
        
        
        Map retorno = new HashMap();
        
        retorno.put("vlIss",                vlIss);
        retorno.put("vlIssEmbutido",        vlIssEmbutido);
        retorno.put("pcAliquotaIss",        pcAliquotaIss);
        retorno.put("nrServicoOficial",     servicoOficialTributo.getNrServicoOficialTributo());
        
        if( aliquotaIssMunicipioServ.getIssMunicipioServico().getServicoMunicipio() != null ){
            retorno.put("nrServicoMunicipio",   aliquotaIssMunicipioServ.getIssMunicipioServico().getServicoMunicipio().getNrServicoMunicipio());
        }
        
        retorno.put("idMunicipioIncidencia",idMunicipioIncidencia);
        // Retorno adicionado para visualização do nome do município de incidencia
        retorno.put("nmMunicipioIncidencia",municipio.getNmMunicipio());
        retorno.put("blRetencaoTomador",    blRetencaoTomador);
        retorno.put("obIss",                servicoOficialTributo.getObServicoOficialTributo());
        
        return retorno;
    }

    /**
     * Calcula o ISS para Carreteiros
     * 
     * @param idCliente
     * @param idMunicipioSede
     * @param idMunicipioPrestacaoServico
     * @param idServicoTributo
     * @param dtBase
     * @param vlBase
     * @return Map com os seguintes campos: vlIss 
     *                                      pcAliquotaIss
     */
    public Map calcularIssCarreteiro(Long idCliente, 
                                     Long idMunicipioSede,
                                     Long idMunicipioPrestacaoServico, 
                                     Long idServicoTributo,
                                     YearMonthDay dtBase, 
                                     BigDecimal vlBase) {
        
        Long idServicoOficialTributo                = null;
        Long idMunicipioIncidencia                  = null;
        ServicoOficialTributo servicoOficialTributo = null;  
        BigDecimal vlIss                            = null;
        BigDecimal pcAliquotaIss                    = null;
        
        Map retorno = new HashMap();
        
        boolean blArredondamentoIss = false;
        ParametroIssMunicipio pim = parametroIssMunicipioService.findByMunicipio(idMunicipioSede);
        if (pim != null) {
        	blArredondamentoIss = Boolean.TRUE.equals(pim.getBlArredondamentoIss());
        }
        
        if(this.getPessoaService().validateTipoPessoa(idCliente,"F")){
            vlIss = new BigDecimal(0);
            pcAliquotaIss = new BigDecimal(0);
            retorno.put("vlIss", vlIss);        
            retorno.put("pcAliquotaIss",pcAliquotaIss);
        } else {
        
            idServicoOficialTributo = this.getServicoTributoService().findIdentificadorServicoOficialTributo(idServicoTributo, dtBase);            
            
            Map criterios = new HashMap();
            
            criterios.put("idServicoOficialTributo",idServicoOficialTributo);
            criterios.put("tpSituacao","A");
            
            List servicoOficialTributoList = this.getServicoOficialTributoService().find(criterios);
            
            servicoOficialTributo = (ServicoOficialTributo) servicoOficialTributoList.get(0);            
            
            if( servicoOficialTributo.getTpLocalDevido().getValue().equals("S") ){
                idMunicipioIncidencia = idMunicipioSede;
            } else {
                idMunicipioIncidencia = idMunicipioPrestacaoServico;
            }        
            
            List aliquotas = this.getAliquotaIssMunicipioServService().findAliquotaIssMunicipioServicoByCriterios(null,
                                                                                                                  idServicoTributo,
                                                                                                                  idMunicipioIncidencia,
                                                                                                                  dtBase,
                                                                                                                  Boolean.TRUE);
            
            AliquotaIssMunicipioServ aliquotaIssMunicipioServ = null;
            
            if( aliquotas != null && !aliquotas.isEmpty() ){
                aliquotaIssMunicipioServ = (AliquotaIssMunicipioServ) aliquotas.get(0);
                pcAliquotaIss = aliquotaIssMunicipioServ.getPcAliquota();
            } else {
                pcAliquotaIss = new BigDecimal(0);                
            }
            
            RoundingMode roundingMode = blArredondamentoIss ? RoundingMode.HALF_UP : RoundingMode.DOWN;
            vlIss = vlBase.multiply(pcAliquotaIss).divide(new BigDecimal(100), 2, roundingMode);
            
            retorno.put("vlIss",        vlIss);        
            retorno.put("pcAliquotaIss",pcAliquotaIss);
            
        }
        
        return retorno;
    }
    
    /**
     * @return Returns the servicoAdicionalService.
     */
    public ServicoAdicionalService getServicoAdicionalService() {
        return servicoAdicionalService;
    }

    /**
     * @param servicoAdicionalService The servicoAdicionalService to set.
     */
    public void setServicoAdicionalService(
            ServicoAdicionalService servicoAdicionalService) {
        this.servicoAdicionalService = servicoAdicionalService;
    }

    /**
     * @return Returns the servicoTributoService.
     */
    public ServicoTributoService getServicoTributoService() {
        return servicoTributoService;
    }

    /**
     * @param servicoTributoService The servicoTributoService to set.
     */
    public void setServicoTributoService(ServicoTributoService servicoTributoService) {
        this.servicoTributoService = servicoTributoService;
    }

    /**
     * @return Returns the servicoOficialTributoService.
     */
    public ServicoOficialTributoService getServicoOficialTributoService() {
        return servicoOficialTributoService;
    }

    /**
     * @param servicoOficialTributoService The servicoOficialTributoService to set.
     */
    public void setServicoOficialTributoService(
            ServicoOficialTributoService servicoOficialTributoService) {
        this.servicoOficialTributoService = servicoOficialTributoService;
    }

    /**
     * @return Returns the aliquotaIssMunicipioServService.
     */
    public AliquotaIssMunicipioServService getAliquotaIssMunicipioServService() {
        return aliquotaIssMunicipioServService;
    }

    /**
     * @param aliquotaIssMunicipioServService The aliquotaIssMunicipioServService to set.
     */
    public void setAliquotaIssMunicipioServService(
            AliquotaIssMunicipioServService aliquotaIssMunicipioServService) {
        this.aliquotaIssMunicipioServService = aliquotaIssMunicipioServService;
    }

    /**
     * @return Returns the aliquotaContribuicaoServService.
     */
    public AliquotaContribuicaoServService getAliquotaContribuicaoServService() {
        return aliquotaContribuicaoServService;
    }

    /**
     * @param aliquotaContribuicaoServService The aliquotaContribuicaoServService to set.
     */
    public void setAliquotaContribuicaoServService(
            AliquotaContribuicaoServService aliquotaContribuicaoServService) {
        this.aliquotaContribuicaoServService = aliquotaContribuicaoServService;
    }

    /**
     * @return Returns the pessoaService.
     */
    public PessoaService getPessoaService() {
        return pessoaService;
    }

    /**
     * @param pessoaService The pessoaService to set.
     */
    public void setPessoaService(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    /**
     * @return Returns the municipioService.
     */
    public MunicipioService getMunicipioService() {
        return municipioService;
    }

    /**
     * @param municipioService The municipioService to set.
     */
    public void setMunicipioService(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setParametroIssMunicipioService(
			ParametroIssMunicipioService parametroIssMunicipioService) {
		this.parametroIssMunicipioService = parametroIssMunicipioService;
	}

}