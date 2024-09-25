package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.carregamento.model.dao.FilialRotaCcDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.filialRotaCcService"
 */
public class FilialRotaCcService extends CrudService<FilialRotaCc, Long> {

	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private FilialRotaService filialRotaService;
	private FilialService filialService;
	private FluxoFilialService fluxoFilialService;
	private LocalTrocaService localTrocaService;


	public void setLocalTrocaService(LocalTrocaService localTrocaService) {
		this.localTrocaService = localTrocaService;
	}
	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

	/**
	 * Recupera uma instância de <code>FilialRotaCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public FilialRotaCc findById(java.lang.Long id) {
        return (FilialRotaCc)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FilialRotaCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFilialRotaCcDAO(FilialRotaCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FilialRotaCcDAO getFilialRotaCcDAO() {
        return (FilialRotaCcDAO) getDao();
    }
    
	/**
	 * 
	 * @param idControleCarga
	 */
    public void validateExistenciaInsercaoManualByControleCarga(Long idControleCarga) {
    	Map mapControleCarga = new HashMap();
    	mapControleCarga.put("idControleCarga", idControleCarga);
    	
    	Map map = new HashMap();
    	map.put("controleCarga", mapControleCarga);
    	map.put("blInseridoManualmente", Boolean.TRUE);
    	
    	List list = find(map);
    	if (list.isEmpty())
    		throw new BusinessException("LMS-05074");
    }
    
	/**
	 * 
	 * @param idControleCarga
	 * @param blInseridoManualmente
	 * @return
	 */
    public List findFilialRotaByIdControleCarga(Long idControleCarga, Boolean blInseridoManualmente, Long idFilial) {
    	Map map = new HashMap();

    	if (idControleCarga != null) {
	    	Map mapControleCarga = new HashMap();
	    	mapControleCarga.put("idControleCarga", idControleCarga);
	    	map.put("controleCarga", mapControleCarga);
    	}

    	if (blInseridoManualmente != null) {
    		map.put("blInseridoManualmente", blInseridoManualmente);
    	}
    	
    	if (idFilial != null) {
	    	Map mapFilial = new HashMap();
	    	mapFilial.put("idFilial", idFilial);
	    	map.put("filial", mapFilial);
    	}

    	List listaOrdem = new ArrayList();
    	listaOrdem.add("nrOrdem:asc");
    	
    	List list = getFilialRotaCcDAO().findListByCriteria(map, listaOrdem);
    	return list;
    }
    
    /**
     * 
     * @param idControleCarga
     * @param idFilialAnterior
     * @param idNovaFilial
     * @param idFilialPosterior
     * @param nrOrdemFilialAnterior
     * @param dhPrevisaoSaida1
     * @param dhPrevisaoChegada1
     * @param nrTempoViagem1
     * @param dhPrevisaoSaida2
     * @param dhPrevisaoChegada2
     * @param nrTempoViagem2
     */
    public void storeInclusaoFilialRota(Long idControleCarga, Long idFilialAnterior, Long idNovaFilial, Long idFilialPosterior,
    		Integer nrOrdemFilialAnterior,  
    		DateTime dhPrevisaoSaida1, DateTime dhPrevisaoChegada1, Integer nrTempoViagem1, 
    		DateTime dhPrevisaoSaida2, DateTime dhPrevisaoChegada2, Integer nrTempoViagem2) 
    {
    	ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
    	Filial filialAnterior = filialService.findById(idFilialAnterior);
    	Filial novaFilial = filialService.findById(idNovaFilial); 

    	List listaFilialRotaCc = findFilialRotaCcWithNrOrdem(idControleCarga, nrOrdemFilialAnterior, null, Boolean.TRUE);
    	for (Iterator iter = listaFilialRotaCc.iterator(); iter.hasNext();) {
    		FilialRotaCc filialRotaCc = (FilialRotaCc)iter.next();
    		filialRotaCc.setNrOrdem( Byte.valueOf( String.valueOf(filialRotaCc.getNrOrdem().intValue() + 1) ) );
    		store(filialRotaCc);
    	}

		FilialRotaCc frCc = new FilialRotaCc();
		frCc.setControleCarga(controleCarga);
		frCc.setFilial(novaFilial);
		frCc.setNrOrdem( Byte.valueOf( String.valueOf(nrOrdemFilialAnterior.intValue() + 1) ));
		frCc.setBlInseridoManualmente(Boolean.TRUE);
		store(frCc);

    	storeControleTrecho(controleCarga, filialAnterior, novaFilial, dhPrevisaoSaida1, dhPrevisaoChegada1, nrTempoViagem1, Boolean.TRUE);
    	if (idFilialPosterior != null) {
    		Filial filialPosterior = filialService.findById(idFilialPosterior);
    		storeControleTrecho(controleCarga, novaFilial, filialPosterior, dhPrevisaoSaida2, dhPrevisaoChegada2, nrTempoViagem2, Boolean.TRUE);
    		atualizaControleTrecho(idControleCarga, idFilialAnterior, idFilialPosterior);
    	}
    	
    	List listaFilialRotaCc1 = findFilialRotaCcWithNrOrdem(idControleCarga, nrOrdemFilialAnterior, Boolean.TRUE, null);
    	for (Iterator iter = listaFilialRotaCc1.iterator(); iter.hasNext();) {
    		FilialRotaCc filialRotaCc = (FilialRotaCc)iter.next();
    		ControleTrecho controleTrecho = getControleTrecho(idControleCarga, filialRotaCc.getFilial().getIdFilial());
    		DateTime dhPrevisaoSaida = null;
    		if (controleTrecho != null) {
    			dhPrevisaoSaida = controleTrecho.getDhPrevisaoSaida();
    		}
   			storeControleTrecho(controleCarga, filialRotaCc.getFilial(), novaFilial, dhPrevisaoSaida, null, null, Boolean.FALSE);
    	}

    	if (idFilialPosterior != null) {
	    	List listaFilialRotaCc2 = findFilialRotaCcWithNrOrdem(idControleCarga, Integer.valueOf(nrOrdemFilialAnterior.intValue() + 2), null, Boolean.TRUE);
	    	for (Iterator iter = listaFilialRotaCc2.iterator(); iter.hasNext();) {
	    		FilialRotaCc filialRotaCc = (FilialRotaCc)iter.next();
	    		storeControleTrecho(controleCarga, novaFilial, filialRotaCc.getFilial(), dhPrevisaoSaida2, null, null, Boolean.FALSE);
	    	}
    	}
    	
    	controleCargaService.storeAtualizaTempoViagemParaControleCarga(controleCarga);
    }

    /**
     * 
     * @param idControleCarga
     * @param idFilialAnterior
     * @param idFilialPosterior
     */
	private void atualizaControleTrecho(Long idControleCarga, Long idFilialAnterior, Long idFilialPosterior) {
		Map mapControleCarga = new HashMap();
    	mapControleCarga.put("idControleCarga", idControleCarga);
    	
    	Map mapFilialOrigem = new HashMap();
    	mapFilialOrigem.put("idFilial", idFilialAnterior);
    	
    	Map mapFilialDestino = new HashMap();
    	mapFilialDestino.put("idFilial", idFilialPosterior);
    	
    	Map map = new HashMap();
    	map.put("controleCarga", mapControleCarga);
		map.put("filialByIdFilialOrigem", mapFilialOrigem);
		map.put("filialByIdFilialDestino", mapFilialDestino);
    	
    	List listaControleTrecho = controleTrechoService.find(map);
    	if ( !listaControleTrecho.isEmpty()) {
	    	ControleTrecho controleTrecho = (ControleTrecho)listaControleTrecho.get(0);
	    	controleTrecho.setBlTrechoDireto(Boolean.FALSE);
	    	controleTrechoService.store(controleTrecho);
    	}
	}

	/**
	 * 
	 * @param controleCarga
	 * @param filialOrigem
	 * @param filialDestino
	 * @param dhPrevisaoSaida
	 * @param dhPrevisaoChegada
	 * @param nrTempoViagem
	 * @param blTrechoDireto
	 */
    private void storeControleTrecho(ControleCarga controleCarga, Filial filialOrigem, Filial filialDestino,
    		DateTime dhPrevisaoSaida, DateTime dhPrevisaoChegada, Integer nrTempoViagem, Boolean blTrechoDireto) 
    {
	    ControleTrecho controleTrecho = new ControleTrecho();
		controleTrecho.setControleCarga(controleCarga);
		controleTrecho.setFilialByIdFilialOrigem(filialOrigem);
		controleTrecho.setFilialByIdFilialDestino(filialDestino);
		controleTrecho.setDhPrevisaoSaida(dhPrevisaoSaida);
		controleTrecho.setDhPrevisaoChegada(dhPrevisaoChegada);
		controleTrecho.setNrTempoViagem(nrTempoViagem);
		controleTrecho.setNrDistancia(findDistanciaRotaEvento(filialOrigem, filialDestino, JTDateTimeUtils.getDataAtual()));
		controleTrecho.setBlTrechoDireto(blTrechoDireto);
		controleTrecho.setBlInseridoManualmente(Boolean.TRUE);
		controleTrechoService.store(controleTrecho);
    }


    /**
     * 
     * @param idFilialRotaCc
     * @return
     */
    public FilialRotaCc findFilialPosteriorByIdFilialRotaCc(Long idFilialRotaCc) {
    	List lista = getFilialRotaCcDAO().findFilialPosteriorByIdFilialRotaCc(idFilialRotaCc);
    	lista = new AliasToNestedBeanResultTransformer(FilialRotaCc.class).transformListResult(lista);
    	return lista.isEmpty() ? null : (FilialRotaCc)lista.get(0);
    }
    
    /**
     * 
     * @param idFilialRotaCc
     * @return
     */
    public FilialRotaCc findFilialAnteriorByIdFilialRotaCc(Long idFilialRotaCc) {
    	List lista = getFilialRotaCcDAO().findFilialAnteriorByIdFilialRotaCc(idFilialRotaCc);
    	lista = new AliasToNestedBeanResultTransformer(FilialRotaCc.class).transformListResult(lista);
    	return lista.isEmpty() ? null : (FilialRotaCc)lista.get(0);
    }
    
    /**
     * Solicitação CQPRO00006183 da Integração.
     * Método que retorna uma instancia da classe FilialRotaCC sendo esta 
     * uma filial anterior à filial recebida por parametro.
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public FilialRotaCc findFilialRotaCCAnterior(Long idControleCarga, Long idFilial){
    	return getFilialRotaCcDAO().findFilialRotaCCAnterior(idControleCarga, idFilial);
    }    

    /**
     * 
     * @param filialOrigem
     * @param filialDestino
     * @param dtAtual
     * @return
     */
	public Integer findDistanciaRotaEvento(Filial filialOrigem, Filial filialDestino, YearMonthDay dtAtual) {
		List listaFiliais = new ArrayList();
		listaFiliais.add(filialOrigem);
		listaFiliais.add(filialDestino);
		Integer nrDistancia = fluxoFilialService.findDistanciaTotalFluxoFilialOrigemDestino(listaFiliais, dtAtual);
		return nrDistancia;
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idRota
	 * @param idRotaIdaVolta
	 */
	public void generateFilialRotaCcByRotaOrRotaIdaVolta(Long idControleCarga, Long idRota, Long idRotaIdaVolta) {
		List listaFilialRota = filialRotaService.findFiliaisRotaByRotaOrRotaIdaVolta(idRota, idRotaIdaVolta);
		if (listaFilialRota.isEmpty())
			return;
		
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

		List result = new ArrayList();
		for (Iterator iter = listaFilialRota.iterator(); iter.hasNext();) {
			FilialRota fr = (FilialRota)iter.next();
			FilialRotaCc filialRotaCc = new FilialRotaCc();
			filialRotaCc.setControleCarga(controleCarga);
			filialRotaCc.setFilial(fr.getFilial());
			filialRotaCc.setNrOrdem(fr.getNrOrdem());
			filialRotaCc.setBlInseridoManualmente(Boolean.FALSE);
			result.add(filialRotaCc);
		}
		getFilialRotaCcDAO().storeAll(result);
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param nrOrdemFilialAnterior
	 * @param blMenor
	 * @param blMaior
	 * @return
	 */
	public List findFilialRotaCcWithNrOrdem(Long idControleCarga, Integer nrOrdemFilialAnterior, Boolean blMenor, Boolean blMaior) {
		List result = getFilialRotaCcDAO().findFilialRotaCcWithNrOrdem(idControleCarga, nrOrdemFilialAnterior, blMenor, blMaior);
		result = new AliasToNestedBeanResultTransformer(FilialRotaCc.class).transformListResult(result);
		return result;
	}

	
	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 */
	public void removeFilialRota(Long idControleCarga, Long idFilial) {
		if (controleTrechoService.findControleTrechoByControleCargaWithManifesto(idControleCarga, idFilial).booleanValue())
			throw new BusinessException("LMS-05075");

		localTrocaService.storeAtualizacaoLocalTrocaPeloControleTrecho(idControleCarga, idFilial);
		controleTrechoService.storeControleTrechoByControleCarga(idControleCarga, idFilial);
		controleTrechoService.removeByIdControleCargaByIdFilial(idControleCarga, idFilial);

		FilialRotaCc filialRotaCc = getFilialRotaCc(idControleCarga, idFilial);
		
		List listaFilialRotaCc = findFilialRotaCcWithNrOrdem(idControleCarga, Integer.valueOf(filialRotaCc.getNrOrdem().intValue()), null, Boolean.TRUE);
    	for (Iterator iter = listaFilialRotaCc.iterator(); iter.hasNext();) {
    		FilialRotaCc frCc = (FilialRotaCc)iter.next();
    		frCc.setNrOrdem( Byte.valueOf( String.valueOf(frCc.getNrOrdem().intValue() - 1) ) );
    		store(frCc);
    	}
		removeById(filialRotaCc.getIdFilialRotaCc());

		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
		controleCargaService.storeAtualizaTempoViagemParaControleCarga(controleCarga);
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	private FilialRotaCc getFilialRotaCc(Long idControleCarga, Long idFilial) {
		Map mapControleCarga = new HashMap();
		mapControleCarga.put("idControleCarga", idControleCarga);
	
		Map mapFilial = new HashMap();
		mapFilial.put("idFilial", idFilial);
	
		Map map = new HashMap();
		map.put("controleCarga", mapControleCarga);
		map.put("filial", mapFilial);
	
		return (FilialRotaCc)find(map).get(0);
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	private ControleTrecho getControleTrecho(Long idControleCarga, Long idFilial) {
		Map mapControleCarga = new HashMap();
		mapControleCarga.put("idControleCarga", idControleCarga);
	
		Map mapFilial = new HashMap();
		mapFilial.put("idFilial", idFilial);
	
		Map map = new HashMap();
		map.put("controleCarga", mapControleCarga);
		map.put("filialByIdFilialOrigem", mapFilial);

		List lista = controleTrechoService.find(map);
		if (lista.isEmpty())
			return null;

		return (ControleTrecho)lista.get(0);
	}
	
    /**
     * @param items
     */
    public void storeFilialRotaCc(ItemList items) {
    	getFilialRotaCcDAO().storeFilialRotaCc(items);
	}
    
    /**
     * Busca todas as FilialRotaCcs associadas ao controle de carga informado.
     * 
     * @param idControleCarga
     * @return
     */
    public List<FilialRotaCc> findByControleCarga(Long idControleCarga) {
    	return getFilialRotaCcDAO().findByControleCarga(idControleCarga);
    }

    public boolean verificaFilialAntecedePorto(Long idControleCarga, Long idFilial) {
    	
    	return verificaFilialAntecedeOuSucedePorto(idControleCarga, idFilial, true);
		
    }

    public boolean verificaFilialEhPosterioraPorto(Long idControleCarga, Long idFilial) {
    	
    	return verificaFilialAntecedeOuSucedePorto(idControleCarga, idFilial, false);
    	
    }
    
    private boolean verificaFilialAntecedeOuSucedePorto(Long idControleCarga, Long idFilial, boolean antecede) {
 
		List<FilialRotaCc> filiaisRotaCcList = findFilialRotaByIdControleCarga(idControleCarga, null, idFilial);
		
		if (filiaisRotaCcList != null && !filiaisRotaCcList.isEmpty()) {
			
			FilialRotaCc filialRotaCc = filiaisRotaCcList.get(0);
				
			FilialRotaCc filialRotaCcAnteriorPosterior;
			if (antecede) {
				filialRotaCcAnteriorPosterior = findFilialPosteriorByIdFilialRotaCc(filialRotaCc.getIdFilialRotaCc());
			} else {
				filialRotaCcAnteriorPosterior = findFilialAnteriorByIdFilialRotaCc(filialRotaCc.getIdFilialRotaCc());
			}
			
			if (filialRotaCcAnteriorPosterior != null) {
				
				Long idFilialOrigem = antecede ? idFilial : filialRotaCcAnteriorPosterior.getFilial().getIdFilial();
				Long idFilialDestino = antecede ? filialRotaCcAnteriorPosterior.getFilial().getIdFilial() : idFilial;
				
				List<Object[]> fluxos = fluxoFilialService.findFluxoFilialByFilialDestinoOrigemServico(idFilialOrigem, idFilialDestino, null, JTDateTimeUtils.getDataAtual());
				
				if (fluxos != null && ! fluxos.isEmpty()) {
					
					for (Object[] fluxoFilial: fluxos) {
						
						if (Boolean.TRUE.equals(fluxoFilial[7])) {
							
							return true;
							
						}
						
					}
					
				}
				
			}
			
		}
		
		return false;
    	
    }
    
}