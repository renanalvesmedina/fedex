package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

public class ConferirConhecimentoService extends CrudService {
	
	private ConhecimentoService conhecimentoService;	
	
	public Map<String, Object> findConhecimentoByBarCode(String barCode){			
		
		Map<String, Object> criteria = new HashMap();
		criteria.put("nrCodigoBarras", barCode);

		Conhecimento conhecimento = null;
		
		List listaConhecimento = getConhecimentoService().find(criteria);
		
		if(listaConhecimento.size() == 1){
			conhecimento = (Conhecimento)listaConhecimento.get(0);
		}
		else if(listaConhecimento.size() == 0){
			throw new BusinessException("LMS-45028");
//			LMS-45028 - Conhecimento não foi encontrado.
		}
		else{
			throw new BusinessException("LMS-45031");		
//			LMS-45031 - Erro ao buscar o conhecimento.
		}
		
		//CRIA OBJETOS PARA BUSCA DAS INFORMAÇÕES =======
		LocalizacaoMercadoria localizacaoMercadoria = conhecimento.getLocalizacaoMercadoria();
		Filial filialLocalizacao = conhecimento.getFilialLocalizacao();
		Filial filialDestino = conhecimento.getFilialByIdFilialDestino();
		Pessoa remetente = conhecimento.getClienteByIdClienteRemetente().getPessoa();
		Pessoa destinatario = conhecimento.getClienteByIdClienteDestinatario().getPessoa();
		List<NotaFiscalConhecimento> lsNotaFiscalConhecimentos = conhecimento.getNotaFiscalConhecimentos();
		RotaColetaEntrega rotaColetaEntrega = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaReal();
		
		if(rotaColetaEntrega == null){
			rotaColetaEntrega = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid();
		}
		
		//===============================================
		
		//INICIALIZA AS VARIÁVAEIS COM VALORES DEFAULT ==
		BigDecimal psAferido = conhecimento.getPsAferido();
		BigDecimal psCubado = conhecimento.getPsReal();
		String dtPrevEntrega = conhecimento.getDtPrevEntrega().toString();
		Long idDoctoServico = conhecimento.getIdDoctoServico();
		Short cdLocMercadoria = 0;
		String locMercadoria = "";
		String locFilial = "";
		String destino = "";
		Short nrRota = 0;
		String dsRota = "";
		String sRemetente = "";
		String sDestinatario = "";
		List<Integer> notasFiscais = new ArrayList();
		Long totalVolume = 0L; 
		 
		//===============================================
		
		//VERIFICA SE OBTEVE ALGUM OBJETO NULO E ATRIBUI A INFORMAÇÃO A VARIAVEL		
		if(localizacaoMercadoria != null){
			cdLocMercadoria = localizacaoMercadoria.getCdLocalizacaoMercadoria();
			locMercadoria = localizacaoMercadoria.getDsLocalizacaoMercadoria().getValue() +
			(conhecimento.getObComplementoLocalizacao() != null ? " " + conhecimento.getObComplementoLocalizacao() : "");
		}
		if(filialLocalizacao != null){
			locFilial = filialLocalizacao.getSgFilial();
		}
		if(filialDestino != null){
			destino = filialDestino.getSiglaNomeFilial();
		}
		if(rotaColetaEntrega != null){
			nrRota = rotaColetaEntrega.getNrRota();
			dsRota = rotaColetaEntrega.getDsRota();
		}
		if(remetente != null){
			sRemetente = remetente.getNmPessoa();
		}
		if(destinatario != null){
			sDestinatario = destinatario.getNmPessoa();
		}
		List listVol;
		if(lsNotaFiscalConhecimentos != null){
			for (NotaFiscalConhecimento nfConhecimento : lsNotaFiscalConhecimentos) {
				
				notasFiscais.add(nfConhecimento.getNrNotaFiscal());
			}
		}	
		//=========================================================
				
		//CRIA O MAPA PARA RETORNAR COM OS VALORES OBTIDOS ========
		Map<String, Object> map = new HashMap();
		
		map.put("idDoctoServico", idDoctoServico);
		map.put("psAferido", psAferido);
		map.put("psCubado", psCubado);
		map.put("dtPrevEntrega", dtPrevEntrega);
		map.put("cdLocMercadoria", cdLocMercadoria);
		map.put("locMercadoria", locMercadoria);
		map.put("locFilial", locFilial);
		map.put("destino", destino);
		map.put("nrRota", nrRota);
		map.put("dsRota", dsRota);
		map.put("sRemetente", sRemetente);
		map.put("sDestinatario", sDestinatario);
		map.put("notasFiscais", notasFiscais);
		map.put("qtVolumes", conhecimento.getQtVolumes());
		//=========================================================
				
		return map;		
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

}
