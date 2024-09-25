package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.tributos.model.AliquotaInssPessoaFisica;
import com.mercurio.lms.tributos.model.ImpostoCalculado;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.calcularInssService"
 */
public class CalcularInssService {

	private PessoaService pessoaService;
	
	private ImpostoCalculadoService impostoCalculadoService;
	
	private DescontoInssCarreteiroService descontoInssCarreteiroService;
	
	private AliquotaInssPessoaFisicaService aliquotaInssPessoaFisicaService;
	
	public void estornar(TypedFlatMap map){
		try {
			estornoInssPessoaFisica(
					map.getLong("pessoaEstorno.idPessoa"),
					map.getYearMonthDay("dtBaseEstorno"),
					map.getBigDecimal("vlBaseEstorno"),
					map.getBigDecimal("vlInssEstorno")
					);
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
	public Map calcular(TypedFlatMap map){
		try {
			return findValorInss(
					map.getLong("pessoa.idPessoa"), 
					map.getYearMonthDay("dtBase"), 
					map.getBigDecimal("vlBase"));
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}
	
    /**
     *  Realiza o c�lculo do valor do INSS.<BR>
     *  Retorna a al�quota no INSS, o valor calculado, teto m�ximo e valor 
     * recolhido por outras empresas.<BR> 
     * @param idPessoa pessoa a ser tributada
     * @param dtBase data base para c�lculo do imposto
     * @param vlBase valor base para o c�lculo
     * @return {pcAliquotaInss, vlInss, vlSalarioBase, vlOutrasFontes}
     * @throws BusinessException("LMS-23004") n�o � pessoa f�sica
     */
	public Map findValorInss(Long idPessoa, YearMonthDay dtBase, BigDecimal vlBase) {
		
		Map map = new HashMap();
		
		if (!getPessoaService().validateTipoPessoa(idPessoa, "F")){
			throw new BusinessException("LMS-23004");
		}
		
		YearMonthDay dtBaseTmp = new YearMonthDay(dtBase);
		
		dtBaseTmp = JTDateTimeUtils.setDay(dtBaseTmp,1);
		
		ImpostoCalculado imposto = getImpostoCalculadoService().findImpostoCalculoInss(idPessoa, dtBaseTmp, "IN");
		if (imposto == null){
	    	Pessoa pessoa = new Pessoa();
			pessoa.setIdPessoa(idPessoa);
			imposto = new ImpostoCalculado(
					new BigDecimal(0), 
					new BigDecimal(0), 
					new BigDecimal(0), 
					dtBaseTmp, 
					new DomainValue("IN"), 
					new DomainValue("ME"), 
					pessoa);
		}

		BigDecimal sumVlINSS = getDescontoInssCarreteiroService().findTotalValorINSS(idPessoa, dtBase);

		if (sumVlINSS == null) sumVlINSS = new BigDecimal(0);

		AliquotaInssPessoaFisica aliquota = getAliquotaInssPessoaFisicaService().findAliquotaVigente(dtBase);
		
		
		/*
		 * Edenilson
		 * Quando n�o possui aliquota vigente, retorna valores zerados. 
		 */
		if (aliquota == null) {
			
			map.put("pcAliquotaInss", Long.valueOf(0));
			map.put("vlSalarioBase", Long.valueOf(0));
			map.put("vlOutrasFontes", Long.valueOf(0));
			map.put("vlInss", Long.valueOf(0));
			return map;
			
		}
		
		map.put("pcAliquotaInss", aliquota.getPcAliquota());
		map.put("vlSalarioBase", aliquota.getVlSalarioBase());
		
		BigDecimal vlImpostoJaCalculado = imposto.getVlImpostoCalculado().add(sumVlINSS);
		
		map.put("vlOutrasFontes", vlImpostoJaCalculado);
		
		if (aliquota.getPcBaseCalcReduzida().compareTo(new BigDecimal(0)) > 0){
			vlBase = vlBase.multiply( aliquota.getPcBaseCalcReduzida() ).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
		}

		BigDecimal vlInss = vlBase.multiply( aliquota.getPcAliquota() ).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
		
		int compareInssImposto = vlInss.add(vlImpostoJaCalculado).compareTo(aliquota.getVlMaximoRecolhimento());
		if ( compareInssImposto > 0 ){ 
			/*
			 * Edenilson
			 * Quando a soma do imposto j� calculado com o valor do imposto a ser lan�ado
			 * exceder o valor m�ximo de recolhimento, o valor do imposto a ser lan�ado deve ser zerado.  
			 * */
			vlInss = aliquota.getVlMaximoRecolhimento().subtract(vlImpostoJaCalculado);
			if (vlInss.compareTo(BigDecimal.ZERO)<0){
				vlInss = BigDecimal.ZERO;
			}
		}else if (compareInssImposto == 0){
			vlInss = BigDecimal.ZERO;
		}
		imposto.setVlBaseCalculo( imposto.getVlBaseCalculo().add(vlBase) );
		imposto.setVlImpostoCalculado( imposto.getVlImpostoCalculado().add(vlInss) );

		getImpostoCalculadoService().store(imposto);
		map.put("vlInss", vlInss);
    	
		return map;
    }
	
	/**
	 * Estorno de INSS de Pessoa Fisica.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPessoa
	 * @param dtBase
	 * @param vlBase
	 * @param vlInss
	 * @throws BusinessException("LMS-23004") n�o � pessoa f�sica
	 */
	public void estornoInssPessoaFisica(Long idPessoa, YearMonthDay dtBase, BigDecimal vlBase, BigDecimal vlInss){
		
		if (!getPessoaService().validateTipoPessoa(idPessoa, "F")){
			throw new BusinessException("LMS-23004");
		}
		
		//Aliquota utilizada para calculos de INSS
		AliquotaInssPessoaFisica aliquota = getAliquotaInssPessoaFisicaService().findAliquotaVigente(dtBase);
		
		if (aliquota != null && new BigDecimal(0).compareTo(aliquota.getPcBaseCalcReduzida()) < 0){
			vlBase = vlBase.multiply( aliquota.getPcBaseCalcReduzida() ).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);
		}

		//Imposto Calculado da Pessoa Fisica
		ImpostoCalculado imposto = getImpostoCalculadoService().findImpostoCalculoInss(idPessoa, dtBase, "IN");
		if (imposto != null){
			imposto.setVlBaseCalculo( imposto.getVlBaseCalculo().subtract(vlBase) );
			imposto.setVlImpostoCalculado( imposto.getVlImpostoCalculado().subtract(vlInss) );
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

	public DescontoInssCarreteiroService getDescontoInssCarreteiroService() {
		return descontoInssCarreteiroService;
	}

	public void setDescontoInssCarreteiroService(
			DescontoInssCarreteiroService descontoInssCarreteiroService) {
		this.descontoInssCarreteiroService = descontoInssCarreteiroService;
	}

	public AliquotaInssPessoaFisicaService getAliquotaInssPessoaFisicaService() {
		return aliquotaInssPessoaFisicaService;
	}

	public void setAliquotaInssPessoaFisicaService(
			AliquotaInssPessoaFisicaService aliquotaInssPessoaFisicaService) {
		this.aliquotaInssPessoaFisicaService = aliquotaInssPessoaFisicaService;
	}
	
}