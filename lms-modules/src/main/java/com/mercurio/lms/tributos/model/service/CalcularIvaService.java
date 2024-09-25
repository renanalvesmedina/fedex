package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.calcularIvaService"
 */
public class CalcularIvaService {

	private Logger log = LogManager.getLogger(this.getClass());
	private AliquotaIvaService aliquotaIvaService;
	
	public BigDecimal teste(TypedFlatMap map){
			try {
				return findValorIva(
						map.getLong("pais.idPais"),
						map.getYearMonthDay("dtEmissaoFatura"),
						map.getBigDecimal("vlBaseCalculo")
					);
			} catch (BusinessException e) {
				throw e;
			} catch (Exception e) {
				log.error(e);
				throw new BusinessException(e.getMessage());
			}
	}
	
    /**
     * Retorna o valor calculado do IVA, de acordo com parametros.<BR>
     * O valor do IVA � igual a:  vlBaseCalculo * ('percentual da aliquota' / 100).<BR>
     * Aten��o: <BR>
     * <ul>
     * 	<li>todos os parametros s�o obrigat�rios, caso algum n�o seja informado o retorno do m�todo � <i>null</i></li>
     * 	<li>se n�o encontrado um valor vigente (de acordo com parametro informado) para a aliquota, esta ser� zero.</li>
     * </ul>
     * @author Robson Edemar Gehl
     * @param idPaisDestino Pa�s de destino da mercadoria
     * @param dtEmissaoFatura Data de emiss�o da fatura
     * @param vlBaseCalculo Valor base para o c�lculo
     *
     * @return valor do IVA de acordo com os pr�metros
     */
    public BigDecimal findValorIva(Long idPaisDestino, YearMonthDay dtEmissaoFatura, BigDecimal vlBaseCalculo) {

    	if (idPaisDestino == null || dtEmissaoFatura == null || vlBaseCalculo == null){
    		return null;
    	}
    	
    	BigDecimal vlIva = null;
    	
    	BigDecimal pcAliquota = getAliquotaIvaService().findAliquotaVigente(idPaisDestino, dtEmissaoFatura);
    	
    	if (pcAliquota == null){
    		pcAliquota = new BigDecimal(0);
    	}

    	BigDecimal vlDivision = pcAliquota.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
		vlIva = vlBaseCalculo.multiply(vlDivision).setScale(2, BigDecimal.ROUND_HALF_UP);
    	
    	return vlIva;
    }

	public AliquotaIvaService getAliquotaIvaService() {
		return aliquotaIvaService;
	}

	public void setAliquotaIvaService(AliquotaIvaService aliquotaIvaService) {
		this.aliquotaIvaService = aliquotaIvaService;
	}
    
   }
