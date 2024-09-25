package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.constants.TipoCalculoDesconto;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.DescontoRfcDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.ParcelaDescontoRfcDTO;

public class DescontoHelper {
	
	private static final String LMS_25105 = "LMS-25105";
	private static final int PC_100 = 100;
	private static final String ENTRADA_NULA = "A entrada não pode ser nula";
	private static final String LMS_25098 = "LMS-25098";
	private static final String LMS_25097 = "LMS-25097";
	private static final String LMS_25096 = "LMS-25096";
	private static final String LMS_25095 = "LMS-25095";
	private static final String LMS_25094 = "LMS-25094";
	private static final String LMS_25093 = "LMS-25093";
	private static final String LMS_25092 = "LMS-25092";
	private static final String LMS_25091 = "LMS-25091";
	private static final String LMS_25090 = "LMS-25090";
	private static final String LMS_25089 = "LMS-25089";
	private static final String LMS_25088 = "LMS-25088";
	private static final String LMS_25108 = "LMS-25108";
	public  static final String  NR_PARCELAS = "NR_PARCELAS";
	public  static final String  PERC_MIN_DESCONTO = "PERC_MIN_DESCONTO";
	
	private ParametroGeralService parametroGeralService;
	private ConfiguracoesFacade configuracoesFacade;
	

	public  DescontoRfcDTO gerarParcelas(DescontoRfcDTO descontoParcelasDTO) {
		validarEntrada(descontoParcelasDTO);
		
		validaParametrosCalculo(descontoParcelasDTO);
		
		validaValorDesconto(descontoParcelasDTO);
		
		validaValorFixo(descontoParcelasDTO);
		
		validaValorParcela(descontoParcelasDTO);
		
		validaValorPercentual(descontoParcelasDTO);		
		
		List<ParcelaDescontoRfcDTO> parcelas = getParcelas(descontoParcelasDTO);
		
		descontoParcelasDTO.setParcelas(parcelas);
		
		ajustaRetornoParcelaPrioritaria(descontoParcelasDTO);
		
		return descontoParcelasDTO;
	}


	private  List<ParcelaDescontoRfcDTO> getParcelas(DescontoRfcDTO descontoParcelasDTO) {
		TipoCalculoDesconto tipoCalculo = identificaCalculo(descontoParcelasDTO);
		switch (tipoCalculo) {
			case PERCENTUAL:
				return gerarParcelasCalculoPercentual(descontoParcelasDTO);				
			case QTD_PARCELAS:
				return gerarParcelasCalculoQtdParcelas(descontoParcelasDTO);		
			case VL_FIXO:
				return gerarParcelasCalculoValorFixo(descontoParcelasDTO);		
			default:
				return new ArrayList<ParcelaDescontoRfcDTO>();
		}		
	}

	private  List<ParcelaDescontoRfcDTO> gerarParcelasCalculoValorFixo(DescontoRfcDTO descontoParcelasDTO) {
		BigDecimal valor = descontoParcelasDTO.getVlTotalDesconto();
		BigDecimal valorFixo = BigDecimal.ZERO;
		
		if(descontoParcelasDTO.isPrioritario()){
			valorFixo = valor;
		}else{			
			valorFixo = descontoParcelasDTO.getVlFixoParcela().setScale(2);
		}
		
		YearMonthDay dataInicio = descontoParcelasDTO.getDtInicioDesconto();
		Integer intervalo = descontoParcelasDTO.getQtDias();
		
		BigDecimal result =  valor.divide(valorFixo,2,RoundingMode.HALF_UP);
		
		Integer vezes = result.intValue();
		
		return criaParcelas(valor, dataInicio, intervalo, valorFixo,vezes);
	}

	private  void validarQuantidadeParcelas(int vezes) {		
		int nVezes = getParametroQuantidadeParcelas().intValue();
		
		if(vezes > nVezes){
			throw new BusinessException(LMS_25088, new Object[]{nVezes});
		}
	}

	private BigDecimal getParametroQuantidadeParcelas() {
		return (BigDecimal) getParametro(NR_PARCELAS);
	}
	
	private BigDecimal getParametroPorcentageMinima() {
		return (BigDecimal) getParametro(PERC_MIN_DESCONTO);
	}

	private Object getParametro(String parametro) {
		return parametroGeralService.findConteudoByNomeParametro(parametro, false);
	}

	private  List<ParcelaDescontoRfcDTO> gerarParcelasCalculoQtdParcelas(DescontoRfcDTO descontoParcelasDTO) {
		BigDecimal valor = descontoParcelasDTO.getVlTotalDesconto();
		YearMonthDay dataInicio = descontoParcelasDTO.getDtInicioDesconto();
		Integer intervalo = descontoParcelasDTO.getQtDias();
				
		BigDecimal qtdParcelas = new BigDecimal(descontoParcelasDTO.getQtParcelas());
		
		BigDecimal valorFixo = valor.divide(qtdParcelas,2,RoundingMode.HALF_UP);
		
		Integer vezes = descontoParcelasDTO.getQtParcelas();
		return criaParcelas(valor, dataInicio, intervalo, valorFixo,vezes);
	}

	private  List<ParcelaDescontoRfcDTO> criaParcelas( BigDecimal valor,	YearMonthDay dataInicio, Integer intervalo, BigDecimal valorFixo,Integer vezes) {
		validarQuantidadeParcelas(vezes);
		
		List<ParcelaDescontoRfcDTO> parcelas = new ArrayList<ParcelaDescontoRfcDTO>();
		
		for (int i = 0; i < vezes; i++) {
		
			ParcelaDescontoRfcDTO item = new ParcelaDescontoRfcDTO(i+1,dataInicio,valorFixo);
			parcelas.add(item);
			
			dataInicio = dataInicio.plusDays(intervalo);
			valor = (valor.subtract(valorFixo)).setScale(2,RoundingMode.HALF_UP); 
		}
		
		if(valor.compareTo(BigDecimal.ZERO) != 0){
			ParcelaDescontoRfcDTO item = parcelas.get(vezes-1);
			item.setValor(item.getValor().add(valor).setScale(2,RoundingMode.HALF_UP));
		}
		return parcelas;
	}

	private  List<ParcelaDescontoRfcDTO> gerarParcelasCalculoPercentual(DescontoRfcDTO descontoParcelasDTO) {
		BigDecimal valor = descontoParcelasDTO.getVlTotalDesconto().setScale(2,RoundingMode.HALF_UP);
		YearMonthDay dataInicio = descontoParcelasDTO.getDtInicioDesconto();
		Integer intervalo = descontoParcelasDTO.getQtDias();
				
		
		BigDecimal valorFixo = valor.multiply(descontoParcelasDTO.getPcDesconto().divide(new BigDecimal(PC_100),4,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP); 
		
		int vezes = valor.divide(valorFixo,RoundingMode.HALF_UP).intValue();
		
		return criaParcelas(valor, dataInicio, intervalo, valorFixo,vezes);
	}

	private  void validarEntrada(DescontoRfcDTO descontoParcelasDTO) {
		if(descontoParcelasDTO == null){
			throw new IllegalArgumentException(ENTRADA_NULA);
		}
		
		if(descontoParcelasDTO.getDtInicioDesconto() == null){
			throw new BusinessException(LMS_25089);
		}		
		
		if(descontoParcelasDTO.getQtDias() == null){
			throw new BusinessException(LMS_25090);
		}		
		if(descontoParcelasDTO.getVlTotalDesconto().compareTo(BigDecimal.ZERO) < 0 ){
			throw new BusinessException(LMS_25108);
		}		
		
	}

	private void validaValorPercentual(DescontoRfcDTO descontoParcelasDTO) {
		if(descontoParcelasDTO.getPcDesconto() != null){
			BigDecimal parametroPercentualMinimo = getParametroPorcentageMinima().setScale(2,RoundingMode.HALF_UP); 
			if(descontoParcelasDTO.getPcDesconto().compareTo(parametroPercentualMinimo) < 0){
				throw new BusinessException(LMS_25095, new Object[]{parametroPercentualMinimo});
			}
			if(descontoParcelasDTO.getPcDesconto().compareTo(new BigDecimal(100).setScale(2,RoundingMode.HALF_UP)) > 0){
				throw new BusinessException(LMS_25096);
			}
		}
	}

	private void validaValorParcela(DescontoRfcDTO descontoParcelasDTO) {
		if(descontoParcelasDTO.getQtParcelas() != null && descontoParcelasDTO.getQtParcelas() < 1){
			throw new BusinessException(LMS_25094);
		}
	}

	private void validaValorFixo(DescontoRfcDTO descontoParcelasDTO) {
		if(descontoParcelasDTO.getVlFixoParcela() != null && descontoParcelasDTO.getVlFixoParcela().compareTo(descontoParcelasDTO.getVlTotalDesconto()) > 0){
			throw new BusinessException(LMS_25093);
		}
	}
	
	private  void validaValorDesconto(DescontoRfcDTO descontoParcelasDTO) {
		if(descontoParcelasDTO.getVlTotalDesconto() == null){
			throw new BusinessException(LMS_25091);
		}
		
		if(BigDecimal.ZERO.equals(descontoParcelasDTO.getVlTotalDesconto())){
			throw new BusinessException(LMS_25092);
		}
	}

	private  void validaParametrosCalculo(DescontoRfcDTO descontoParcelasDTO) {
		int count = 0;
		if(descontoParcelasDTO.getPcDesconto() != null){
			count++;
		}
		if(descontoParcelasDTO.getQtParcelas() != null){
			count++;
		}
		
		if(descontoParcelasDTO.getVlFixoParcela() != null){
			count++;
		}
		
		if(count == 0){
			if(descontoParcelasDTO.isPrioritario()){
				return;
			}
			throw new BusinessException(LMS_25097);
		}else if(count != 1){
			throw new BusinessException(LMS_25098);
		}
	}
	
	private  TipoCalculoDesconto identificaCalculo(DescontoRfcDTO descontoParcelasDTO) {	
		if(descontoParcelasDTO.getPcDesconto() != null){
			return TipoCalculoDesconto.PERCENTUAL;
		}
		if(descontoParcelasDTO.getQtParcelas() != null){
			return TipoCalculoDesconto.QTD_PARCELAS;
		}
		
		return TipoCalculoDesconto.VL_FIXO;
	}
	
	private void ajustaRetornoParcelaPrioritaria(DescontoRfcDTO descontoParcelasDTO) {
		if(descontoParcelasDTO.isPrioritario()){
			descontoParcelasDTO.getParcelas().get(0).setDescricaoParcela(configuracoesFacade.getMensagem(LMS_25105));
		}
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	

}
