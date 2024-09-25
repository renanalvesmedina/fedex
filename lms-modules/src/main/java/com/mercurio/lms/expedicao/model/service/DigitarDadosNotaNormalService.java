package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.municipios.model.Filial;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Densidade;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.gm.model.service.CarregamentoService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.digitarDadosNotaNormalService"
 */
public class DigitarDadosNotaNormalService {

	private DigitarNotaService digitarNotaService;
	private ParametroGeralService parametroGeralService;
	private CalcularFreteService calcularFreteService;
	private VolumeService volumeService;
	private CarregamentoService carregamentoService;
	private ConhecimentoNormalService conhecimentoNormalService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService; 
	private ConteudoParametroFilialService conteudoParametroFilialService;
	

	/**
	 * Carrega e Valida Dados <b>Primeira Fase</b>
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map executeCtrcPrimeiraFase(Map parameters) {
		Conhecimento conhecimento = digitarNotaService.createConhecimentoPersistente(parameters, new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
		
		/*CQPRO00028791 - Projeto Merger - Informar o Tipo do documento*/
		Filial filial = SessionUtils.getFilialSessao();
		String tpDocumento = digitarNotaService.findTpDocumento(conhecimento, filial);
		conhecimento.setTpDocumentoServico(new DomainValue(tpDocumento));
		conhecimento.setTpDoctoServico(new DomainValue(tpDocumento));
		
		Frete frete = new Frete(); 

		//Validação de remetente e destinatário iguais:
		//Verificar se o Remetente e o destinatário são iguais, se forem iguais, deverá ter sido
		//informado um consignatário ou um endereço de entrega, se nenhum dos dois houver
		//sido informado visualizar mensagem LMS-04240 retornando à aba Dados Gerais.
		if (conhecimento.getClienteByIdClienteRemetente().getIdCliente().equals(conhecimento.getClienteByIdClienteDestinatario().getIdCliente())) {
			if ((conhecimento.getDsLocalEntrega() == null && 
					conhecimento.getDsEnderecoEntrega() == null && 
					conhecimento.getClienteByIdClienteConsignatario() == null) || 
					(conhecimento.getClienteByIdClienteConsignatario() != null && conhecimento.getClienteByIdClienteConsignatario().getIdCliente().equals(conhecimento.getClienteByIdClienteRemetente().getIdCliente()))) {
				throw new BusinessException("LMS-04240");
			}
		}			

		// Densidade
		Densidade densidade = new Densidade();
		List list = digitarNotaService.findDensidade();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map)list.get(i);
				String tpDensidade = map.get("tpDensidade").toString();
				if ("A".equals(tpDensidade)) {
					densidade.setIdDensidade((Long) map.get("idDensidade"));
					densidade.setTpDensidade(new DomainValue(map.get("tpDensidade").toString()));
					densidade.setVlFator((BigDecimal) map.get("vlFator"));
					conhecimento.setDensidade(densidade);
					break;
				}
			}
		}		

		CalculoFrete calculoFrete = ExpedicaoUtils.newCalculoFrete(conhecimento.getTpDocumentoServico());

		calculoFrete.setIdServico(conhecimento.getServico().getIdServico());
		calculoFrete.setBlCalculaParcelas(conhecimento.getBlParcelas());
		calculoFrete.setBlCalculoFreteTabelaCheia(Boolean.FALSE);
		calculoFrete.setBlCalculaServicosAdicionais(conhecimento.getBlServicosAdicionais());

		WarningCollectorUtils.remove();

		try {
			conhecimentoNormalService.validateCtrcPrimeiraFase(conhecimento, calculoFrete);
		} catch (BusinessException businessException) {
			throw businessException;
		}

		DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
		DateTime dhChegada = new DateTime(parameters.get("dhChegada"));
		// TODO: 27/01/2023 Verificar regra 
		if(dhChegada != null) {
			if(sysDate.compareTo(dhChegada) < 0) {
				//throw new BusinessException("LMS-00074");
			}

			BigDecimal tmpMinimoDataColeta = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("TMP_MINIMO_DATA_COLETA", false);

			if (tmpMinimoDataColeta != null) {
				sysDate = sysDate.minusHours(tmpMinimoDataColeta.intValue());
				if (sysDate.compareTo(dhChegada) > 0) {
					//throw new BusinessException("LMS-04250");
				}
			}
		}
		frete.setConhecimento(conhecimento);
		frete.setCalculoFrete(calculoFrete);
		
		parameters.put("conhecimento", frete.getConhecimento());
		parameters.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, frete.getCalculoFrete());
		
		calcularFreteService.validaFreteNormal(frete);

		String validaChaveNFE = (String) conteudoParametroFilialService
				.findConteudoByNomeParametro
					(SessionUtils.getFilialSessao().getIdFilial(), "VALIDA_CHAVE_NFE", false);
		//Busca notas com a mesma chave
		if (!Boolean.TRUE.equals(conhecimento.getBlRedespachoIntermediario()) && "S".equals(validaChaveNFE)){
			validateNotasJaDigitadasNaFilial(conhecimento);
		}
		
		
		return parameters;
	}

    private void validateNotasJaDigitadasNaFilial(Conhecimento conhecimento) {
        List<NotaFiscalConhecimento> notas = conhecimento.getNotaFiscalConhecimentos();
		List<String> chaves = new ArrayList<String>();
		for (NotaFiscalConhecimento nota: notas){
		    if (nota.getNrChave() != null){
		        chaves.add(nota.getNrChave());
		    }
		}
		
		//Busca notas digitadas na filial
		if (chaves.size() > 0){
			List<NotaFiscalConhecimento> notasNaFilial = notaFiscalConhecimentoService.findByNrChavesNaFilial(chaves, conhecimento.getFilialOrigem());
			if (notasNaFilial != null && !notasNaFilial.isEmpty()){
			    StringBuilder nrNotas = new StringBuilder();
			    for (NotaFiscalConhecimento nota:notasNaFilial){
			    	if (!nrNotas.toString().contains(nota.getNrNotaFiscal().toString())){
				        if (!nrNotas.toString().isEmpty()){
				            nrNotas.append(", ");
				        }
			        	nrNotas.append(nota.getNrNotaFiscal().toString());
			        }
			    }
			    throw new BusinessException("LMS-04567", new Object[]{nrNotas.toString()});
			}
		}
		
    }

	/**
	 * Validações <b>Segunda Fase</b>
	 * @return
	 */ 
	public Map executeCtrcSegundaFase(Map parameters) {

		Map<String, Object> result = new HashMap<String, Object>();
		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		
		if (parameters.get("idDivisaoCliente") != null) {
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente((Long)parameters.get("idDivisaoCliente"));
			conhecimento.setDivisaoCliente(divisaoCliente);
			((CalculoFrete)parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION)).setIdDivisaoCliente((Long)parameters.get("idDivisaoCliente"));
		}		
		
		Frete frete = new Frete(); 
		frete.setConhecimento(conhecimento);
		frete.setCalculoFrete((CalculoFrete)parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION));
		calcularFreteService.calcularPesoAforado(conhecimento);
		
		// LMS-4418
		if (BigDecimalUtils.hasValue(conhecimento.getPsAforado())
				&& !conhecimento.getPsAforado().equals(conhecimento.getPsReal())) {
			conhecimento.setPsCubadoDeclarado(conhecimento.getPsAforado());
		} else {
			conhecimento.setPsCubadoDeclarado(null);
		}
		conhecimento.setPsCubadoAferido(null);
		
		conhecimento.getDadosCliente().setBlCotacaoRemetente((Boolean) parameters.get("blCotacaoRemetente"));
		
		result.put(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, parameters.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION));
		result.put("conhecimento", conhecimento);
		result.put("idFilialDestino", conhecimento.getFilialByIdFilialDestino().getIdFilial());
		result.put("sgFilialDestino", conhecimento.getFilialByIdFilialDestino().getSgFilial());
		
		WarningCollectorUtils.putAll(result);
		
		return result;
	}
	
	public void setDigitarNotaService(DigitarNotaService digitarNotaService) {
		this.digitarNotaService = digitarNotaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
		this.calcularFreteService = calcularFreteService;
	}

	public VolumeService getVolumeService() {
		return volumeService;
	}

	public void setVolumeService(VolumeService volumeService) {
		this.volumeService = volumeService;
	}

	public CarregamentoService getCarregamentoService() {
		return carregamentoService;
	}

	public void setCarregamentoService(CarregamentoService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}

	public DigitarNotaService getDigitarNotaService() {
		return digitarNotaService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public CalcularFreteService getCalcularFreteService() {
		return calcularFreteService;
	}
	
	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }
    
    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
}
