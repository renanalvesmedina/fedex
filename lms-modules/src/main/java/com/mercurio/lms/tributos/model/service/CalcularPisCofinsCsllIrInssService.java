package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServ;
import com.mercurio.lms.tributos.model.CalcularPisCofinsCsllIrInss;
import com.mercurio.lms.tributos.model.ImpostoCalculado;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.calcularPisCofinsCsllIrInssService"
 */
public class CalcularPisCofinsCsllIrInssService {

	private static BigDecimal ZERO = new BigDecimal(0);

	private Logger log = LogManager.getLogger(this.getClass());

	private PessoaService pessoaService;
	
	private ImpostoCalculadoService impostoCalculadoService;
	
	private AliquotaContribuicaoServService aliquotaContribuicaoServService;
	
	public List testeCalculo(TypedFlatMap map){
		try {
			Long said = null, stid = null;
			String sa = map.getString("servicoAdicional.idServicoAdicional");
			String st = map.getString("servicoTributo.idServicoTributo");
			if (sa != null && !sa.equals("")){
				said = Long.valueOf(sa);
			}
			if (st != null && !st.equals("")){
				stid = Long.valueOf(st);
			}
			
			return calcularPisCofinsCsllIrInssPessoaJudirica(
					map.getLong("pessoa.idPessoa"), map.getString("tpOperacao"), 
					map.getString("tpRecolhimento"),
					said, stid, 
					map.getYearMonthDay("dtBase"), map.getBigDecimal("vlBase")
					);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage());
		}
	}
	
	public void testeEstorno(TypedFlatMap map){
		try {
			estornarPisCofinsCsllIrInssPessoaJudirica(
					map.getLong("pessoa.idPessoa"), map.getString("tpRecolhimento"), 
					map.getYearMonthDay("dtBase"), map.getBigDecimal("vlBasePis"),
					map.getBigDecimal("vlPis"),map.getBigDecimal("vlBaseCofins"),
					map.getBigDecimal("vlCofins"),map.getBigDecimal("vlBaseCsll"),
					map.getBigDecimal("vlCsll"),map.getBigDecimal("vlBaseIr"),
					map.getBigDecimal("vlIr"),map.getBigDecimal("vlBaseInss"),
					map.getBigDecimal("vlInss")
			);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new BusinessException(e.getMessage());
		}
	}
	
	/**
	 * Realiza o cálculo dos impostos (PIS, CONFINS, CSLL, IR, INSS)
	 * Se o parâmetro blAtualizaImpostoCalculado = true, inclui / altera a tabela IMPOSTO_CALCULADO
	 * @author Robson Edemar Gehl
	 * @param idPessoa
	 * @param tpOperacao
	 * @param tpRecolhimento
	 * @param idServicoAdicional
	 * @param idServicoTributo
	 * @param dtBase
	 * @param vlBase
	 * @param blAtualizaImpostoCalculado
	 * @return Retorna uma lista com todos os impostos calculados
	 * @throws com.mercurio.adsm.framework.BusinessException(LMS-23006") não é pessoa jurídica
	 * @throws com.mercurio.adsm.framework.BusinessException(LMS-23007") deve ser informado Serviço Adicional ou Servico Tributo 
	 */
    public List<CalcularPisCofinsCsllIrInss> calcularPisCofinsCsllIrInssPessoaJudirica(Long idPessoa, String tpOperacao, String tpRecolhimento, 
    		Long idServicoAdicional, Long idServicoTributo, YearMonthDay dtBase, BigDecimal vlBase) {

    	List<CalcularPisCofinsCsllIrInss> calculos = new ArrayList<CalcularPisCofinsCsllIrInss>(5);
    	CalcularPisCofinsCsllIrInss calculo = null;
    	
    	if (!getPessoaService().validateTipoPessoa(idPessoa, "J")){
    		throw new BusinessException("LMS-23006");
    	}

    	if ( (idServicoAdicional != null && idServicoTributo != null) || (idServicoAdicional == null && idServicoTributo == null)){
    		throw new BusinessException("LMS-23007");
    	}

    	String[] impostos = {"PI", "CO", "CS", "IR", "IN"};
    	
    	ImpostoCalculado impostoCalculado = null;
    	AliquotaContribuicaoServ aliquota = null;

    	//Valores do CalcularPisCofinsCsllIrInss
    	BigDecimal vlBaseCalculoRetorno = null;
    	BigDecimal vlBaseCalculo = null;
    	BigDecimal vlBaseJaCalculado = null;
    	BigDecimal vlTotalCalculado = ZERO;
    	BigDecimal vlImposto = ZERO;
    	BigDecimal vlPcAliquota = ZERO;
    	BigDecimal vlPcBaseCalcReduzida = ZERO;
    	BigDecimal vlPiso = ZERO;
    	String obAliquotaContribuicaoServ = null;
    	
    	for (int i = 0; i < impostos.length; i++){
    		
    		impostoCalculado = getImpostoCalculadoService().findImpostoCalculoInss(idPessoa, dtBase, impostos[i]);
			
    		if (impostoCalculado == null || !impostoCalculado.getTpRecolhimento().getValue().equals(tpRecolhimento)){

    			YearMonthDay dtBaseTmp = new YearMonthDay(dtBase);
    			
    			dtBaseTmp = JTDateTimeUtils.setDay(dtBaseTmp,1);

				Pessoa pessoa = new Pessoa();
				pessoa.setIdPessoa(idPessoa);

				impostoCalculado = new ImpostoCalculado(
						ZERO, ZERO, ZERO, dtBaseTmp, 
						new DomainValue(impostos[i]), new DomainValue(tpRecolhimento), pessoa);
			}
    		
    		
    		aliquota = getAliquotaContribuicaoServService().findByUnique(idPessoa, idServicoAdicional, idServicoTributo, dtBase, impostos[i]); 
    		if (aliquota != null){
    	    	vlPcAliquota = aliquota.getPcAliquota();
    	    	vlPcBaseCalcReduzida = aliquota.getPcBaseCalcReduzida();
    	    	vlPiso = aliquota.getVlPiso();
    	    	obAliquotaContribuicaoServ = aliquota.getObAliquotaContribuicaoServ();
    		}

    		vlBaseCalculo = vlBase.multiply(vlPcBaseCalcReduzida).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);

    		// Edenilson
    		// 23/12/2005
    		// Não acumular valores já calculados de INSS e IR 
    		if (impostos[i].equals("IN") || impostos[i].equals("IR")) {
        		vlBaseJaCalculado = vlBaseCalculo;
    		}else{
        		vlBaseJaCalculado = impostoCalculado.getVlBaseCalculo().add(vlBaseCalculo);
    		}
    			

    		if (vlBaseJaCalculado.compareTo(vlPiso) >= 0 && vlBase.compareTo(vlTotalCalculado) > 0 && aliquota != null){
    			vlImposto = vlBaseJaCalculado.multiply(vlPcAliquota).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
    			if (!impostos[i].equals("IN") && !impostos[i].equals("IR")) {
    				vlImposto = vlImposto.subtract(impostoCalculado.getVlImpostoCalculado());
    			}
    			if (impostoCalculado.getVlImpostoCalculado().doubleValue() == 0.00d){
    				vlBaseCalculoRetorno = vlBaseJaCalculado; 
    			}else{
    				vlBaseCalculoRetorno = vlBaseCalculo;
    			}
    			if (vlImposto.doubleValue() == 0.00d){
    				obAliquotaContribuicaoServ = null;
    				vlPcAliquota = ZERO;
    				vlImposto = ZERO;
    				vlBaseCalculoRetorno = ZERO;
    			}else{
        			vlTotalCalculado = vlTotalCalculado.add(vlImposto);
        			if (vlBase.compareTo(vlTotalCalculado) < 0){
        				vlImposto = vlImposto.add(vlBase).subtract(vlTotalCalculado);
        				vlTotalCalculado = new BigDecimal(vlBase.doubleValue());
        			}
    			}
    		}else{
    			obAliquotaContribuicaoServ = null;
    			vlPcAliquota = ZERO;
				vlImposto = ZERO;
				vlBaseCalculoRetorno = ZERO;
    		}
    		
			impostoCalculado.setVlBaseCalculo(impostoCalculado.getVlBaseCalculo().add(vlBaseCalculo));
			impostoCalculado.setVlImpostoCalculado(impostoCalculado.getVlImpostoCalculado().add(vlImposto));

			if (vlBaseJaCalculado.compareTo(vlPiso) < 0){
				calculo = new CalcularPisCofinsCsllIrInss(
						impostos[i], ZERO, ZERO, ZERO,
						vlBaseCalculo,
						obAliquotaContribuicaoServ,
						impostoCalculado
						);
			}else{
				calculo = new CalcularPisCofinsCsllIrInss(
						impostos[i], vlBaseCalculoRetorno,	
						vlImposto, vlPcAliquota,
						vlBaseCalculo,
						obAliquotaContribuicaoServ,
						impostoCalculado
						);
			}
			calculos.add(calculo);
    	}
    	return calculos;
    }

    /**
     * Realiza o estorno dos impostos (PIS, CONFINS, CSLL, IR, INSS)
     * @param idPessoa
     * @param tpRecolhimento
     * @param dtBase
     * @param vlBasePis
     * @param vlPis
     * @param vlBaseCofins
     * @param vlCofins
     * @param vlBaseCsll
     * @param vlCsll
     * @param vlBaseIr
     * @param vlIr
     * @param vlBaseInss
     * @param vlInss
     */
    public void estornarPisCofinsCsllIrInssPessoaJudirica(Long idPessoa, String tpRecolhimento, 
    		YearMonthDay dtBase, BigDecimal vlBasePis, BigDecimal vlPis, BigDecimal vlBaseCofins, 
    		BigDecimal vlCofins, BigDecimal vlBaseCsll, BigDecimal vlCsll, 
    		BigDecimal vlBaseIr, BigDecimal vlIr, BigDecimal vlBaseInss, BigDecimal vlInss){

    	if (!getPessoaService().validateTipoPessoa(idPessoa, "J")){
    		throw new BusinessException("LMS-23006");
    	}

    	ImpostoCalculado imposto = null;

    	String[] impostos = {"PI", "CO", "CS", "IR", "IN"};

    	for (int i = 0; i < impostos.length; i++){

    		imposto = getImpostoCalculadoService().findImpostoCalculoInss(idPessoa, dtBase, impostos[i], tpRecolhimento);

    		if (imposto == null) continue;

    		switch (i){
    			//Pis
	    		case 0:{
	    			imposto.setVlBaseCalculo(imposto.getVlBaseCalculo().subtract(vlBasePis));
	    			imposto.setVlImpostoCalculado(imposto.getVlImpostoCalculado().subtract(vlPis));
	    			break;
	    		}
	    		//Cofins
	    		case 1:{
	    			imposto.setVlBaseCalculo(imposto.getVlBaseCalculo().subtract(vlBaseCofins));
	    			imposto.setVlImpostoCalculado(imposto.getVlImpostoCalculado().subtract(vlCofins));
	    			break;
	    		}
	    		//Csll
	    		case 2:{
	    			imposto.setVlBaseCalculo(imposto.getVlBaseCalculo().subtract(vlBaseCsll));
	    			imposto.setVlImpostoCalculado(imposto.getVlImpostoCalculado().subtract(vlCsll));
	    			break;
	    		}
	    		//IR
	    		case 3:{
	    			imposto.setVlBaseCalculo(imposto.getVlBaseCalculo().subtract(vlBaseIr));
	    			imposto.setVlImpostoCalculado(imposto.getVlImpostoCalculado().subtract(vlIr));
	    			break;	
	    		}
	    		//INSS
	    		case 4:{
	    			imposto.setVlBaseCalculo(imposto.getVlBaseCalculo().subtract(vlBaseInss));
	    			imposto.setVlImpostoCalculado(imposto.getVlImpostoCalculado().subtract(vlInss));
	    			break;
	    		}
	    		default:{
	    			break;
	    		}
			}
    		getImpostoCalculadoService().store(imposto);
    	}
    }
    

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public ImpostoCalculadoService getImpostoCalculadoService() {
		return impostoCalculadoService;
	}

	public void setImpostoCalculadoService(
			ImpostoCalculadoService impostoCalculadoService) {
		this.impostoCalculadoService = impostoCalculadoService;
	}

	public AliquotaContribuicaoServService getAliquotaContribuicaoServService() {
		return aliquotaContribuicaoServService;
	}

	public void setAliquotaContribuicaoServService(
			AliquotaContribuicaoServService aliquotaContribuicaoServService) {
		this.aliquotaContribuicaoServService = aliquotaContribuicaoServService;
	}
    
    
}
