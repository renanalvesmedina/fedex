package com.mercurio.lms.vol.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.dao.GerencialDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.gerencialService"
 * 
 */
public class GerencialService {
	private VolLogEnviosSmsService volLogEnviosSmsService;
	private EventoColetaService eventoColetaService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private VolEventosCelularService volEventosCelularService;
	private GerencialDAO gerencialDAO;
	
	public void setEventoColetaService(EventoColetaService eventoColetaService) {
		this.eventoColetaService = eventoColetaService;
	}
	
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	
	public void setVolEventosCelularService(VolEventosCelularService volEventosCelularService) {
		this.volEventosCelularService = volEventosCelularService;
	}
	
	public void setVolLogEnviosSmsService(VolLogEnviosSmsService volLogEnviosSmsService) {
		this.volLogEnviosSmsService = volLogEnviosSmsService;
	}
	
	
	/**
	 * calcula a eficiencia de entrega/coleta da frota, ou seja a porcentagem de entregas/coletas já realizadas
	 * 
	 * @param totalEntregas
	 * @param totalEntregasBaixadas
	 * @param totalColetas
	 * @param totalColetasBaixadas
	 * @param totalColetasAutomaticas
	 * @return eficiencia e % da frota
	 */
	public int calculaEficiencia(Long totalEntregas, Long totalEntregasBaixadas, Long totalColetas, Long totalColetasBaixadas,
			Long totalColetasAutomaticas){
		Long eficiencia = Long.valueOf(0);
		Long total =totalEntregas + totalColetas;
		Long totalRealizado = totalEntregasBaixadas + totalColetasBaixadas + totalColetasAutomaticas;
		if(total > 0){
			eficiencia = (totalRealizado * 100) / total;	
		}
		return eficiencia.intValue();
	}
	
	/**
	 * método que calcula a frequencia das entregas/coletas
	 * @param horaEntrega
	 * @param horaColeta
	 * @param horaAtual
	 * @param totalColetaEntrega
	 * @return frequencia da frota
	 */
	public int calculaFrequencia(DateTime horaEntrega, DateTime horaColeta, DateTime horaAtual,
			Long totalColetaEntrega){
		int frequencia = 0;
		
		if(horaEntrega == null)
			horaEntrega = horaAtual;
		if(horaColeta == null)
			horaColeta = horaAtual;
		
		//verifica qual a dataHora mais recente entre a dataHora da coleta e entrega, a dataHora mais recente é passada como 
		//parâmetro para que seja feito o calculo da frequencia
		int diferencaTempo = JTDateTimeUtils.getIntervalInMinutes(horaEntrega, horaColeta);
		
		if( diferencaTempo > 0 ){
			String tempoTemp = JTDateTimeUtils.calculaDiferencaEmHoras( horaEntrega, horaAtual );
			frequencia = calculaTotalMinutos(tempoTemp) / totalColetaEntrega.intValue();	
		} else {
				 String tempoTemp = JTDateTimeUtils.calculaDiferencaEmHoras(horaColeta, horaAtual);
				 if ( tempoTemp != null) {
						frequencia = calculaTotalMinutos(tempoTemp) / totalColetaEntrega.intValue();
				}
		}
		
		return frequencia;
	}
	
	/**
	 * esse método que recebe a diferença em horas/min entre o primeiro evento (coleta/entrega) e a hora atual
	 * e converte tudo para minutos
	 * @param tempoTemp
	 * @return tempo total convertido para minutos
	 */
	private int calculaTotalMinutos(String tempoTemp){
		int horas = 0;
		int minutos = 0;
		int tempoTotalMinutos = 0;
		
		if(tempoTemp.contains("h")){
			String tempo[] = tempoTemp.split("h");
			horas = Integer.parseInt(tempo[0].trim()) * 60;
			String minutosString[] = tempo[1].split("min");
			minutos = Integer.parseInt(minutosString[0].trim());
		}else{
			String minutosString[] = tempoTemp.split("min");
			minutos = Integer.parseInt(minutosString[0]);
		}
		
		tempoTotalMinutos =  horas + minutos;
		
		return tempoTotalMinutos;
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setGerencialDAO(GerencialDAO dao) {
        this.gerencialDAO = dao;
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private GerencialDAO getGerencialDAO() {
        return this.gerencialDAO;
    }
    
    public List findAcompanhamentoGerencial(TypedFlatMap criteria) {
    	return getGerencialDAO().findAcompanhamentoGerencial(criteria);
	}
    
    public List<Map<String, Object>> findDetalhamentoGerencial(TypedFlatMap criteria) {
    	return getGerencialDAO().findDetalhamentoGerencial(criteria);
    }
   
    public Integer findTotalEntregas(Long idMeioTransporte) {
    	return getGerencialDAO().findTotalEntregas(idMeioTransporte);
    }
    public Integer findTotalEntregasRealizadas(Long idMeioTransporte) {
    	return getGerencialDAO().findTotalEntregasRealizadas(idMeioTransporte);
    }
    public Integer findTotalColetas(Long idMeioTransporte) {
    	return getGerencialDAO().findTotalColetas(idMeioTransporte);
    }
    public Integer findTotalColetasRealizadas(Long idMeioTransporte) {
    	return getGerencialDAO().findTotalColetasRealizadas(idMeioTransporte);
    }
    public Integer findTotalColetasAutomaticas(Long idMeioTransporte) {
    	return getGerencialDAO().findTotalColetasAutomaticas(idMeioTransporte);
    }
    public TypedFlatMap findChamadoFrota(Long idMeioTransporte) {
    	return getGerencialDAO().findChamadoFrota(idMeioTransporte);
    }
    
    public Boolean findFrotaComAtraso(Long idMeioTransporte) {
    	DateTime dhEnvio = this.volLogEnviosSmsService.findDhEnvioByMeioTransporte(idMeioTransporte);
    	DateTime horaAtual = JTDateTimeUtils.getDataHoraAtual();
    	if (dhEnvio == null ) {
    		return Boolean.FALSE;
    	} else if (JTDateTimeUtils.getIntervalInHours(dhEnvio,horaAtual) > 1) {
        		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
     }
	
	public Boolean findColetasComAtraso(Long idMeioTransporte, DateTime horaAtual) {
		DateTime dhEvento = this.eventoColetaService.findDhEventoByMeioTransporte(idMeioTransporte);
    	if (dhEvento != null) {
    		return (JTDateTimeUtils.getIntervalInHours(dhEvento,horaAtual) > 1 ? Boolean.TRUE : Boolean.FALSE);
    	}
    	return Boolean.FALSE;
	}
    
	public Boolean findEntregasComAtraso(Long idMeioTransporte, DateTime horaAtual) {
		DateTime dhOcorrencia = this.manifestoEntregaDocumentoService.findDhOcorrenciaByMeioTransporte(idMeioTransporte);
		if (dhOcorrencia != null) {
			return (JTDateTimeUtils.getIntervalInHours(dhOcorrencia,horaAtual) > 1 ? Boolean.TRUE : Boolean.FALSE);
		}
		return Boolean.FALSE;
	}
	
	public List findDhEnvioColetaByMeioTransporte(Long idMeioTransporte) {
		return this.volLogEnviosSmsService.findDhEnvioColetaByMeioTransporte(idMeioTransporte);
	}
	
	public List findColetasNaoExecutadas(Long idMeioTransporte){
		return this.getGerencialDAO().findColetasNaoExecutadas(idMeioTransporte);
	}
	
	/**
	 * retorna as entregas não executadas e que possuem ou não agendamento de entrega
	 * @param idMeioTransporte
	 * @return
	 */
	public List findEntregasNaoExecutadasComAgendamento(Long idMeioTransporte){
		return this.getGerencialDAO().findEntregasNaoExecutadasComAgendamento(idMeioTransporte);
	}
	
	public Integer findTotalTratativasByMeioTransporte(Long idMeioTransporte) {
    	return this.volEventosCelularService.findTotalTratativasByMeioTransporte(idMeioTransporte);
    }
	
	public ResultSetPage findPaginatedEventos(TypedFlatMap criteria) {
		return getGerencialDAO().findPaginatedEventos(criteria,FindDefinition.createFindDefinition(criteria));
	}
	public Integer getRowCountEventos(TypedFlatMap criteria) {
		return getGerencialDAO().getRowCountEventos(criteria);
	}
	
	public ResultSetPage findPaginatedTratativas(TypedFlatMap criteria) {
		return getGerencialDAO().findPaginatedTratativas(criteria,FindDefinition.createFindDefinition(criteria));
	}
	public Integer getRowCountTratativas(TypedFlatMap criteria) {
		return getGerencialDAO().getRowCountTratativas(criteria);
	}
	public List findComboDocumentos(Long idMeioTransporte) {
		return getGerencialDAO().findComboDocumentos(idMeioTransporte);
	}
}
