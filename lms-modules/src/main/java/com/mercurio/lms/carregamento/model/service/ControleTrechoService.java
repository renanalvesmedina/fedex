package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.dao.ControleTrechoDAO;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.controleTrechoService"
 */
public class ControleTrechoService extends CrudService<ControleTrecho, Long> {

	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private FluxoFilialService fluxoFilialService;
	private RotaService rotaService;
	private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}
	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}
	public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
		this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
	}

	/**
	 * Recupera uma instância de <code>ControleTrecho</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ControleTrecho findById(java.lang.Long id) {
        return (ControleTrecho)super.findById(id);
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
    public java.io.Serializable store(ControleTrecho bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setControleTrechoDAO(ControleTrechoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ControleTrechoDAO getControleTrechoDAO() {
        return (ControleTrechoDAO) getDao();
    }
    
    public ControleTrecho findControleTrechoWithControleCargaById(Long idControleTrecho) {
    	return getControleTrechoDAO().findControleTrechoWithControleCargaById(idControleTrecho);
    }
    
    /**
     * Obtém o ControleTrecho com a menor previsão de chegada para os parâmetros informados.
     * @param idControleCarga
     * @param idFilialOrigem
     * @param blTrechoDireto
     * @return
     * @author luisfco
     */
    public ControleTrecho findControleTrechoByBlTrechoDireto(Long idControleCarga, Long idFilialOrigem, Boolean blTrechoDireto) {
		return getControleTrechoDAO().findControleTrechoByBlTrechoDireto(idControleCarga, idFilialOrigem, blTrechoDireto);
    }
    
    /**
     * Solicitação CQPRO00006173 da Integração.
     * Método que retorna mais de uma instancia da classe ControleTrecho
     * conforme parametros recebidos.
     * @param idFilialDestino
     * @param idControleCarga
     * @return
     */
    public List findControleTrecho(Long idFilialDestino, Long idControleCarga){
		return getControleTrechoDAO().findControleTrecho(idFilialDestino, idControleCarga);
    }
    
    /**
     * 
     * @param idControleCarga
     * @param idRotaIdaVolta
     * @param idRota
     */
    public List findTrechosByTrechosRota(Long idControleCarga, Long idRotaIdaVolta, Long idRota, 
    		DateTime dhPrevisaoSaida, Boolean blTrechoDireto, Long idFilialOrigem, Long idFilialDestino) 
    {
    	if (idControleCarga != null) {
    		List result = findControleTrechoByControleCarga(idControleCarga, blTrechoDireto, idFilialOrigem, idFilialDestino);
	    	if (!result.isEmpty())
	    		return result;
    	}

    	if (idRotaIdaVolta == null && idRota == null) {
    		throw new BusinessException("LMS-05054");
    	}
    	List retorno = new ArrayList();

    	ControleCarga controleCarga = new ControleCarga();
    	controleCarga.setIdControleCarga(idControleCarga);

    	if (idRotaIdaVolta != null)
    		findControleTrechoByIdRotaIdaVolta(idRotaIdaVolta, retorno, controleCarga, dhPrevisaoSaida.toYearMonthDay());
    	else
    	if (idRota != null)
    		findControleTrechoByIdRota(idRota, retorno, controleCarga);

    	Collections.sort(retorno, new Comparator() {
    		public int compare(Object obj1, Object obj2) {
    			ControleTrecho ct1 = (ControleTrecho)obj1;
    			ControleTrecho ct2 = (ControleTrecho)obj2;
    			if (ct1.getDhPrevisaoSaida() != null && ct2.getDhPrevisaoSaida() != null) {
    				int valor = Long.valueOf(ct1.getDhPrevisaoSaida().getMillis()).compareTo( Long.valueOf(ct2.getDhPrevisaoSaida().getMillis()) );
    				if (valor == 0) {
    					if (ct1.getDhPrevisaoChegada() != null && ct2.getDhPrevisaoChegada() != null)
    						valor = Long.valueOf(ct1.getDhPrevisaoChegada().getMillis()).compareTo( ct2.getDhPrevisaoChegada().getMillis() );
    				}
    				return valor;
    			}
   				return -1;
    		}
    	});

    	return retorno;
    }

    
    /**
     * 
     * @param idRotaIdaVolta
     * @param retorno
     * @param controleCarga
     * @param dtSaidaRota
     */
	private void findControleTrechoByIdRotaIdaVolta(Long idRotaIdaVolta, List retorno, ControleCarga controleCarga, YearMonthDay dtSaidaRota) {
		List result = trechoRotaIdaVoltaService.findToTrechosByTrechosRota(idRotaIdaVolta, dtSaidaRota);
		int nrTrecho = 1;
		DateTime saida = null, chegada = null, calculo = null;
		Integer viagem = null, diferenca = null;
		
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
    		TypedFlatMap tfm = (TypedFlatMap)iter.next();

    		ControleTrecho controleTrecho = new ControleTrecho();
    		controleTrecho.setIdControleTrecho(null);
    		controleTrecho.setControleCarga(controleCarga);
    		controleTrecho.setFilialByIdFilialOrigem( filialService.findById(tfm.getLong("idFilialOrigem")) );
    		controleTrecho.setFilialByIdFilialDestino( filialService.findById(tfm.getLong("idFilialDestino")) );
    		controleTrecho.setTrechoRotaIdaVolta( trechoRotaIdaVoltaService.findById(tfm.getLong("idTrechoRotaIdaVolta")) );

    		controleTrecho.setDhPrevisaoSaida(tfm.getTimeOfDay("hrSaida") == null ? null : 
    			tfm.getYearMonthDay("dtSaida").toDateTime(tfm.getTimeOfDay("hrSaida"), JTDateTimeUtils.getUserDtz()));

    		controleTrecho.setDhPrevisaoChegada(controleTrecho.getDhPrevisaoSaida() == null ? null : 
    			controleTrecho.getDhPrevisaoSaida().plusMinutes( tfm.getInteger("nrTempoViagem").intValue() ));
    		controleTrecho.setNrTempoViagem(tfm.getInteger("nrTempoViagem"));
    		controleTrecho.setNrTempoOperacao(tfm.getInteger("nrTempoOperacao"));
    		controleTrecho.setNrDistancia(tfm.getInteger("nrDistancia"));
    		controleTrecho.setDhSaida(null);
    		controleTrecho.setDhChegada(null);
			controleTrecho.setBlInseridoManualmente(Boolean.FALSE);

			if (tfm.getInteger("nrOrdemOrigem").intValue() + 1 == tfm.getInteger("nrOrdemDestino").intValue()) {
				controleTrecho.setBlTrechoDireto(Boolean.TRUE);

				//LMS-5276
				if (nrTrecho == 1) {
					//Seta valores do primeiro trecho direto
					saida = controleTrecho.getDhPrevisaoSaida();
					chegada = controleTrecho.getDhPrevisaoChegada();
					viagem = controleTrecho.getNrTempoViagem();

				} else {
					//calcula os tempos de chegada e saida a partir do trecho direto anterior

					// Saida
					calculo = saida.plusMinutes(viagem.intValue() + controleTrecho.getNrTempoOperacao().intValue());
					diferenca = Days.daysBetween(saida.toDateMidnight(), calculo.toDateMidnight()).getDays();
					
					if (diferenca.intValue() > 0) {
						calculo = controleTrecho.getDhPrevisaoSaida().plusDays(diferenca.intValue());
						controleTrecho.setDhPrevisaoSaida(calculo);
					}
					//LMSA-2506
					if(saida.isAfter(controleTrecho.getDhPrevisaoSaida())){
						calculo = controleTrecho.getDhPrevisaoSaida().plusDays(1);
						controleTrecho.setDhPrevisaoSaida(calculo);
					}

					// Chegada
					calculo = controleTrecho.getDhPrevisaoSaida().plusMinutes(controleTrecho.getNrTempoViagem().intValue());
						controleTrecho.setDhPrevisaoChegada(calculo);

					viagem = controleTrecho.getNrTempoViagem();

				}

				nrTrecho++;

			} else {
				controleTrecho.setBlTrechoDireto(Boolean.FALSE);

			}

    		retorno.add(controleTrecho);
    	}

	}
	
	
	/**
	 * 
	 * @param idRota
	 * @param retorno
	 * @param controleCarga
	 */
	private void findControleTrechoByIdRota(Long idRota, List retorno, ControleCarga controleCarga) {
		
		Rota rota = rotaService.findToTrechosById(idRota);
    	
		List<FilialRota> lista = rota.getFilialRotas();
    	
    	Collections.sort(lista, new Comparator<FilialRota>() {
    		public int compare(FilialRota current, FilialRota next) {    			
    			return current.getNrOrdem().compareTo(next.getNrOrdem());
    		}
		});
    	
    	if (lista != null && !lista.isEmpty()) {
    		BigDecimal vlNrTempoDistanciaPadrao = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("RELACAO_TEMPO_DISTANCIA_PADRAO", false);

	    	for (int i=0; i<lista.size()-1; i++) {
	    		FilialRota filialRotaOrigem = (FilialRota)lista.get(i);
	    		for (int j=i+1; j<lista.size(); j++) {
	    			FilialRota filialRotaDestino = (FilialRota)lista.get(j);
	        		ControleTrecho controleTrecho = new ControleTrecho();
	        		controleTrecho.setIdControleTrecho(null);
	        		controleTrecho.setControleCarga(controleCarga);
	        		controleTrecho.setFilialByIdFilialOrigem(filialRotaOrigem.getFilial());
	        		controleTrecho.setFilialByIdFilialDestino(filialRotaDestino.getFilial());
	        		controleTrecho.setTrechoRotaIdaVolta(null);
	        		controleTrecho.setDhPrevisaoSaida(null);
	        		controleTrecho.setDhPrevisaoChegada(null);

	        		TypedFlatMap tfm = findNrDistanciaAndNrTempoViagem(
	        				filialRotaOrigem.getFilial(), filialRotaDestino.getFilial(), vlNrTempoDistanciaPadrao);

	        		controleTrecho.setNrTempoViagem(tfm.getInteger("nrTempoViagem"));
	        		controleTrecho.setNrDistancia(tfm.getInteger("nrDistancia"));
	        		controleTrecho.setNrTempoOperacao(null);
	        		controleTrecho.setDhSaida(null);
	        		controleTrecho.setDhChegada(null);
	    			controleTrecho.setBlInseridoManualmente(Boolean.FALSE);
	    			if (filialRotaOrigem.getNrOrdem().intValue() + 1 == filialRotaDestino.getNrOrdem().intValue())
	    				controleTrecho.setBlTrechoDireto(Boolean.TRUE);
	    			else
	    				controleTrecho.setBlTrechoDireto(Boolean.FALSE);

	        		retorno.add(controleTrecho);
	    		}
	    	}
    	}
	}


	/**
	 * Calcula a distancia e o tempo de viagem de um trecho.
	 * 
	 * @param filialOrigem
	 * @param filialDestino
	 * @param vlNrTempoDistanciaPadrao
	 * @return TypedFlatMap contendo dois valores: nrDistancia(Integer) e nrTempoViagem(Integer).
	 */
	public TypedFlatMap findNrDistanciaAndNrTempoViagem(Filial filialOrigem, Filial filialDestino, BigDecimal vlNrTempoDistanciaPadrao) 
	{
		List listaFiliais = new ArrayList();
		listaFiliais.add(filialOrigem);
		listaFiliais.add(filialDestino);
		Integer nrDistancia = fluxoFilialService.findDistanciaTotalFluxoFilialOrigemDestino(listaFiliais, JTDateTimeUtils.getDataAtual());

		BigDecimal vlNrTempoViagem = BigDecimalUtils.ZERO;
		BigDecimal vlNrDistancia = new BigDecimal(nrDistancia.longValue());
		if (vlNrDistancia.compareTo(BigDecimalUtils.ZERO) != 0 && vlNrTempoDistanciaPadrao.compareTo(BigDecimalUtils.ZERO) != 0) {
			vlNrDistancia = vlNrDistancia.multiply(new BigDecimal(60));
			vlNrTempoViagem = vlNrDistancia.divide(vlNrTempoDistanciaPadrao, 0, BigDecimal.ROUND_HALF_DOWN);
		}
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("nrDistancia", nrDistancia);
		tfm.put("nrTempoViagem", Integer.valueOf(vlNrTempoViagem.intValue()));
		return tfm;
	}


	/**
	 * Obtém o ControleTrecho a partir do ID do Controlecarga, do ID da Filial de Origem e o ID da Filial de Destino.
	 * 
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return ControleTrecho
	 */
    public ControleTrecho findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(Long idControleCarga, 
    																				Long idFilialOrigem, Long idFilialDestino) {
		return this.getControleTrechoDAO()
				.findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(
						idControleCarga, idFilialOrigem, idFilialDestino);
    }
    
    /**
     * Obtém a contagem dos controles de trecho de acordo com os parâmetros recebidos.
     * @param idControleCarga
     * @param idFilialOrigem
     * @param idFilialDestino
     * @return
     */
    public Integer getRowCountControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		return getControleTrechoDAO().getRowCountControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(idControleCarga, idFilialOrigem, idFilialDestino);
    }
    

    /**
     * @param items
     */
    public void storeControleTrecho(ItemList items) {
    	getControleTrechoDAO().storeControleTrecho(items);
	}
    
    
	/**
	 * 
	 * @param idControleCarga
	 * @param blTrechoDireto
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public List findControleTrechoByControleCarga(	Long idControleCarga, Boolean blTrechoDireto, 
													Long idFilialOrigem, Long idFilialDestino) 
	{
		List lista = getControleTrechoDAO().
				findControleTrechoByControleCarga(idControleCarga, blTrechoDireto, idFilialOrigem,idFilialDestino);
		return new AliasToNestedBeanResultTransformer(ControleTrecho.class).transformListResult(lista);
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @return
	 */
	public List findControleTrechoDiretoByControleCarga(Long idControleCarga, Long idFilialOrigem) {
		List lista = getControleTrechoDAO().findControleTrechoDiretoByControleCarga(idControleCarga, idFilialOrigem);
		return new AliasToTypedFlatMapResultTransformer().transformListResult(lista);
	}
	

	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 */
	public void removeByIdControleCargaByIdFilial(Long idControleCarga, Long idFilial) {
		getControleTrechoDAO().removeByIdControleCargaByIdFilial(idControleCarga, idFilial);
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 */
	public void storeControleTrechoByControleCarga(Long idControleCarga, Long idFilial) {
		getControleTrechoDAO().storeControleTrechoByControleCarga(idControleCarga, idFilial);
	}
	
    /**
     * Verifica se existe algum registro em ControleTrecho associado a um Manifesto de acordo com os parâmetros.
     * 
     * @param idControleCarga
     * @param idFilial
     * @return Retorna TRUE se encontrar algum registro, caso contrário, FALSE.
     */
    public Boolean findControleTrechoByControleCargaWithManifesto(Long idControleCarga, Long idFilial) {
    	return getControleTrechoDAO().findControleTrechoByControleCargaWithManifesto(idControleCarga, idFilial);
    }
    
	/**
	 * Método criado e utilizado somente pela a Integração.
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @return
	 */
	public List findControleTrechoDireto(Long idControleCarga, Long idFilialOrigem) {
		List lista = getControleTrechoDAO().findControleTrechoDireto(idControleCarga, idFilialOrigem);
		return lista;
	}
	
	public void updateHoraSaidaByControleCargaIdAndFilialOrigem(DateTime horaSaida, Long controleCargaId, Long idFilialOrigem) {
		getControleTrechoDAO().updateDhSaidaByControleCargaIdAndIdFilialOrigem(horaSaida, controleCargaId, idFilialOrigem);
	}

	public ControleTrecho findControleTrechoByNrControleCargaAndSgFilialOrigem(String sgFilialOrigem,  Long nrControleCarga) {
		return this.getControleTrechoDAO().findControleTrechoByNrControleCargaAndSgFilialOrigem(sgFilialOrigem, nrControleCarga);
	}

}