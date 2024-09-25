package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.PrecoFreteService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.DiferencaCapitalInterior;
import com.mercurio.lms.vendas.model.FaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.dao.DestinoPropostaDAO;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * @spring.bean id="lms.vendas.destinoPropostaService"
 */
public class DestinoPropostaService extends CrudService<DestinoProposta, Long> {
    private static final String PRODUTO_ESPECIFICO = "P";
    private static final String FAIXA_PROGRESSIVA = "F";
	
	private SimulacaoService simulacaoService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private PrecoFreteService precoFreteService;
	private McdService mcdService;
	private ParametroClienteService parametroClienteService;
	private PropostaService propostaService;
	private MunicipioFilialService municipioFilialService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	private ParcelaPrecoService parcelaPrecoService;
	private UnidadeFederativaService unidadeFederativaService;
	private ParametroGeralService parametroGeralService;
	private ConfiguracoesFacade configuracoesFacade;
	private PaisService paisService;
	private DiferencaCapitalInteriorService diferencaCapitalInteriorService;
	private GrupoRegiaoService grupoRegiaoService;
	private AeroportoService aeroportoService;
	private TaxaClienteService taxaClienteService;
	private FaixaProgressivaPropostaService faixaProgressivaPropostaService;
	private ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService;

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDestinoPropostaDAO(DestinoPropostaDAO dao){
		setDao(dao);
	}

	private DestinoPropostaDAO getDestinoPropostaDAO() {
		return (DestinoPropostaDAO) getDao();
	}

	public List<DestinoProposta> findDestinosPropostaByIdSimulacao(Long idSimulacao) {
		return getDestinoPropostaDAO().findDestinosPropostaByIdSimulacao(idSimulacao);
	}

	@Override
	public Serializable store(DestinoProposta bean) {
		return super.store(bean);
	}

	@Override
	public DestinoProposta findById(Long id) {
	    return (DestinoProposta) super.findById(id);
	}
	
	/**
	 * Salva a aba percentual da proposta
	 * 
	 */
	public void storePercentualProposta(Proposta proposta, List<TypedFlatMap> destinosProposta) {
		
		propostaService.storeCancelWorkflow(proposta);
		
		Long idDestinoProposta = null;
		
		List<Long> removeIds = new ArrayList<Long>();
		
		DestinoProposta destinoProposta = null;
		for (TypedFlatMap destino : destinosProposta) {
			
			idDestinoProposta = destino.getLong("id");
			if(destino.getBoolean("isSelected")) {
				
				destinoProposta = new DestinoProposta();
				destinoProposta.setIdDestinoProposta(idDestinoProposta);
				
				/*Proposta*/
				destinoProposta.setProposta(proposta);
				
				/*Unidade federativa*/
				UnidadeFederativa unidadeFederativaDestino = new UnidadeFederativa();
				unidadeFederativaDestino.setIdUnidadeFederativa(destino.getLong("idUnidadeFederativaDestino"));
				destinoProposta.setUnidadeFederativa(unidadeFederativaDestino);
								
				if(destino.getLong("idGrupoRegiao") != null){
					
					/*Grupo região*/
					GrupoRegiao grupoRegiao = new GrupoRegiao();
					grupoRegiao.setIdGrupoRegiao(destino.getLong("idGrupoRegiao"));
					destinoProposta.setGrupoRegiao(grupoRegiao);
				}else{
					
					/*TipoLocalizacaoMunicipio*/
					TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino = new TipoLocalizacaoMunicipio();
					tipoLocalizacaoMunicipioDestino.setIdTipoLocalizacaoMunicipio(destino.getLong("idTipoLocalizacaoMunicipioDestino"));
					destinoProposta.setTipoLocalizacaoMunicipio(tipoLocalizacaoMunicipioDestino);				
				}
				
				/*Valores*/
				destinoProposta.setTpIndicadorFreteMinimo(new DomainValue("D"));				
				destinoProposta.setTpIndicadorFretePeso(new DomainValue("D"));
				destinoProposta.setTpIndicadorAdvalorem(new DomainValue("T"));
				destinoProposta.setTpDiferencaAdvalorem(new DomainValue("P"));
				destinoProposta.setVlFreteMinimo(BigDecimal.ZERO);
				destinoProposta.setVlFretePeso(BigDecimal.ZERO);
				destinoProposta.setVlAdvalorem(BigDecimal.ZERO);
				destinoProposta.setPcDiferencaFretePeso(BigDecimal.ZERO);
				destinoProposta.setPcDiferencaAdvalorem(BigDecimal.ZERO);
				
				/*Percentual*/
				destinoProposta.setPcFretePercentual(destino.getBigDecimal("pcFretePercentual"));
				destinoProposta.setVlMinimoFretePercentual(destino.getBigDecimal("vlMinimoFretePercentual"));
				destinoProposta.setVlToneladaFretePercentual(destino.getBigDecimal("vlToneladaFretePercentual"));
				destinoProposta.setPsFretePercentual(destino.getBigDecimal("psFretePercentual"));

				this.store(destinoProposta);				
				
			}else{
				if(LongUtils.hasValue(idDestinoProposta)){
					removeIds.add(idDestinoProposta);
				}
			}
			
		}/*for*/
		
		if(CollectionUtils.isNotEmpty(removeIds)){
			this.removeByIds(removeIds);
		}
		
	}
	
	/**
	 * Cria ou edita registros de DestinoProposta, FaixaProgressivaProposta e ValorFaixaProgressivaProposta.
	 * 
	 * @param idSimulacao
	 * @param listaDestinoPropostaConvencional
	 */
	public void storeDestinosConvencional(Long idSimulacao, Long idAeroportoReferencia, Boolean blFreteExpedido, Boolean blFreteRecebido, List<DestinoProposta> listaDestinoPropostaConvencional) {
		removeDestinosPropostaConvencional(listaDestinoPropostaConvencional,false);

		Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
		if(proposta == null){
			Simulacao simulacao = simulacaoService.findById(idSimulacao);
			proposta = simulacaoService.executeRotinaAereo(simulacao);
		}else{
		    List<DestinoProposta> destinosPropostaForaDeRota = findDestinosPropostaInvalidos(idAeroportoReferencia, proposta.getIdProposta());
		    removeDestinosPropostaConvencional(destinosPropostaForaDeRota, true);
		}
		Aeroporto aeroporto = new Aeroporto();
		aeroporto.setIdAeroporto(idAeroportoReferencia);
		proposta.setAeroportoReferencia(aeroporto);
		proposta.setBlFreteExpedido(blFreteExpedido);
		proposta.setBlFreteRecebido(blFreteRecebido);
		propostaService.storeCancelWorkflow(proposta);

		if (listaDestinoPropostaConvencional != null && !listaDestinoPropostaConvencional.isEmpty()) {

			for (DestinoProposta destinoProposta : listaDestinoPropostaConvencional) {
				destinoProposta.setProposta(proposta);
			}

			this.storeAll(listaDestinoPropostaConvencional);
			
			storeFaixasProgressivasProposta(idSimulacao, listaDestinoPropostaConvencional);
		
		} else {
			removePropostaNaoUtilizadaConvencional(proposta);
		}
	}

	private List<DestinoProposta> findDestinosPropostaInvalidos(Long idAeroportoReferencia,Long idProposta) {
        return getDestinoPropostaDAO().findDestinosPropostaInvalidos(idAeroportoReferencia,idProposta);
    }

    /**
	 * Como os DestinoProposta são salvo por região geográfica, não é necessário verificar se existe algum registro, de outra região, que ainda aponte para a Proposta em questão. 
	 * Caso não haja nenhum DestinoProposta utilizando a Proposta, a mesma e seu ParametroCliente são excluídos.
	 * 
	 * @param proposta
	 */
	private void removePropostaNaoUtilizadaConvencional(Proposta proposta) {
		Boolean possuiDestinosVinculados = getDestinoPropostaDAO().hasDestinoPropostaByIdProposta(proposta.getIdProposta());
		
		if(!possuiDestinosVinculados){
			removeParametroClienteAereo(proposta);
			propostaService.removeById(proposta.getIdProposta());
		}
	}

	/**
	 * Cria ou edita os registros de FaixaProgressivaProposta e ValorFaixaProgressivaProposta relacionados com a Simulacao(Proposta).
	 * 
	 * @param listaDestinoPropostaConvencional
	 */
	private void storeFaixasProgressivasProposta(Long idSimulacao, List<DestinoProposta> listaDestinoPropostaConvencional) {
		Map<String, Long> faixasProgressivaPorSimulacaoCache = new HashMap<String, Long>();
		
		for (DestinoProposta destinoProposta : listaDestinoPropostaConvencional) {
			if(destinoProposta.getListaValorFaixaProgressivaProposta() != null){
				
				if(faixasProgressivaPorSimulacaoCache.isEmpty()){
					faixasProgressivaPorSimulacaoCache = findFaixasProgressivaPropostaPorSimulacao(idSimulacao);
				}
				
				for (ValorFaixaProgressivaProposta valorFaixaProgressivaProposta : destinoProposta.getListaValorFaixaProgressivaProposta()) {
					valorFaixaProgressivaProposta.setDestinoProposta(destinoProposta);

					FaixaProgressivaProposta faixaProgressivaProposta = valorFaixaProgressivaProposta.getFaixaProgressivaProposta();
					if(faixaProgressivaProposta.getIdFaixaProgressivaProposta() == null && !faixasProgressivaPorSimulacaoCache.isEmpty()){
						if(faixaProgressivaProposta.getProdutoEspecifico() != null){
							//Caso não exista faixa progressiva proposta, para este destino proposta, verifica se já existe para a simulacao uma faixa com o produto específico igual ao selecionado.
							faixaProgressivaProposta.setIdFaixaProgressivaProposta(faixasProgressivaPorSimulacaoCache.get(faixaProgressivaProposta.getProdutoEspecifico().getIdProdutoEspecifico() + PRODUTO_ESPECIFICO));
						} else {
							//Caso não exista faixa progressiva proposta, para este destino proposta, verifica se já existe para a simulacao uma faixa com a faixa progressiva igual a selecionada.
							faixaProgressivaProposta.setIdFaixaProgressivaProposta(faixasProgressivaPorSimulacaoCache.get(faixaProgressivaProposta.getVlFaixa() + FAIXA_PROGRESSIVA));
						}
					}
					
					if(faixaProgressivaProposta.getIdFaixaProgressivaProposta() == null){
						faixaProgressivaPropostaService.store(faixaProgressivaProposta);
					}
				}
				valorFaixaProgressivaPropostaService.storeAll(destinoProposta.getListaValorFaixaProgressivaProposta());
			}
		}
	}
	
    /**
     * Cria um "cache" de FaixaProgressivaProposta, com as faixas de valor e os produtos específicos que já foram vinculados com a simulacao.
     * 
     * Caso exista mais de um registro de ValorFaixaProgressivaProposta, para a Simulacao em questão, que possua o mesmo valor de faixa progressiva, por exemplo (25Kg), 
     * estes registros devem apontar para o mesmo registro de FaixaProgressivaProposta. O mesmo se aplica para ProdutoEspecifico.
     * 
     * @param idSimulacao
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Long> findFaixasProgressivaPropostaPorSimulacao(Long idSimulacao){
    	Map<String, Long> retorno = new HashMap<String, Long>();
    	List<Map> faixasPorSimulacao = faixaProgressivaPropostaService.findByIdSimulacao(idSimulacao);
    	
    	for (Map<BigDecimal, Long> map : faixasPorSimulacao) {
    		if(map.get("vlFaixa") != null){
    			retorno.put(map.get("vlFaixa") + FAIXA_PROGRESSIVA, LongUtils.getLong((map.get("idFaixaProgressivaProposta"))));
    		} else {
    			retorno.put(map.get("idProdutoEspecifico") + PRODUTO_ESPECIFICO, LongUtils.getLong((map.get("idFaixaProgressivaProposta"))));
    		}
		}
    	
    	return retorno;
    }
	
	
	/**
	 * Caso exista algum DestinoProposta marcado para exclusão, apaga o mesmo e os registros de ValorFaixaProgressivaProposta relacionados com ele.
	 * Se ficar algum registro de FaixaProgressivaProposta sem "filhos" (ValorFaixaProgressivaProposta), estes também serão excluídos.
	 * 
	 * @param listaDestinoPropostaConvencional
	 */
	private void removeDestinosPropostaConvencional(List<DestinoProposta> listaDestinoPropostaConvencional, boolean skipValidations){
		List<Long> idsDestinoProposta = new ArrayList<Long>();
		List<Long> idsFaixaProgressivaProposta = new ArrayList<Long>();
		List<Long> idsValorFaixaProgressivaProposta = new ArrayList<Long>();
		
		
		if (listaDestinoPropostaConvencional != null && !listaDestinoPropostaConvencional.isEmpty()) {
			for (Iterator<DestinoProposta> iterator = listaDestinoPropostaConvencional.iterator(); iterator.hasNext();) {
				DestinoProposta destinoProposta = iterator.next();
				
				if((destinoProposta.getIdDestinoProposta() != null && destinoProposta.getMarcadoParaExclusao() != null && destinoProposta.getMarcadoParaExclusao()) || skipValidations){
					idsDestinoProposta.add(destinoProposta.getIdDestinoProposta());
					iterator.remove();
					List<ValorFaixaProgressivaProposta> valores =  valorFaixaProgressivaPropostaService.findByDestinoProposta(destinoProposta);
					if (valores != null){
					    for (ValorFaixaProgressivaProposta valorFaixaProgressivaProposta : valores) {
	                        if(valorFaixaProgressivaProposta.getIdValorFaixaProgressivaProposta() != null){
	                            idsValorFaixaProgressivaProposta.add(valorFaixaProgressivaProposta.getIdValorFaixaProgressivaProposta());
	                            
	                            if(valorFaixaProgressivaProposta.getFaixaProgressivaProposta() != null && valorFaixaProgressivaProposta.getFaixaProgressivaProposta().getIdFaixaProgressivaProposta() != null){
	                                idsFaixaProgressivaProposta.add(valorFaixaProgressivaProposta.getFaixaProgressivaProposta().getIdFaixaProgressivaProposta());
	                            }
	                        }
	                    }
					}
					
				}
			}
		}
		
		if(!idsValorFaixaProgressivaProposta.isEmpty()){
			valorFaixaProgressivaPropostaService.removeByIds(idsValorFaixaProgressivaProposta);
		}

		if(!idsDestinoProposta.isEmpty()){
			removeByIds(idsDestinoProposta);
		}
		
		if(!idsFaixaProgressivaProposta.isEmpty()){
			faixaProgressivaPropostaService.removeByIdsNaoUtilizados(idsFaixaProgressivaProposta);
		}
		
	}
	
	public void storeDestinosAereo(Long idSimulacao, List<DestinoProposta> listaDestinoPropostaAereo) {
		removeRegitrosAnteriores(idSimulacao);

		if (listaDestinoPropostaAereo != null && !listaDestinoPropostaAereo.isEmpty()) {
			Simulacao simulacao = simulacaoService.findById(idSimulacao);
			Proposta proposta = simulacaoService.executeRotinaAereo(simulacao);

			for (DestinoProposta destinoProposta : listaDestinoPropostaAereo) {
				destinoProposta.setIdDestinoProposta(null);
				destinoProposta.setProposta(proposta);
			}

			this.storeAll(listaDestinoPropostaAereo);
		}
	}

	private void removeRegitrosAnteriores(Long idSimulacao){
		removeByIdSimulacao(idSimulacao);

		Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
		if(proposta != null){
			propostaService.storeCancelWorkflow(proposta);
			removeParametroClienteAereo(proposta);
			propostaService.removeById(proposta.getIdProposta());
		}
	}
	
	private void removeParametroClienteAereo(Proposta proposta){
		if(proposta.getParametroCliente() != null){
			ParametroCliente parametroCliente = proposta.getParametroCliente();
			parametroCliente.setProposta(null);
			parametroClienteService.store(parametroCliente);
			getDestinoPropostaDAO().getAdsmHibernateTemplate().flush();
			parametroClienteService.removeById(parametroCliente.getIdParametroCliente());
		}
	}
	
	public Boolean hasDestinoPropostaByIdProposta(Long idProposta){
		return getDestinoPropostaDAO().hasDestinoPropostaByIdProposta(idProposta);
	}
	
	/**
	 * Salva proposta e DestinosProposta
	 * @author Andre Valadas
	 * @param proposta
	 * @param destinosProposta
	 */
	public void storeDestinosProposta(Proposta proposta, List<TypedFlatMap> destinosProposta) {
		propostaService.storeCancelWorkflow(proposta);

		List<Long> removeIds = new ArrayList<Long>();
		for (TypedFlatMap destino : destinosProposta) {
			Long idDestinoProposta = destino.getLong("id");
			/** Salva itens que foram selecionados */
			if(destino.getBoolean("isSelected")) {
				DestinoProposta destinoProposta = new DestinoProposta();
				destinoProposta.setIdDestinoProposta(idDestinoProposta);
				if(destino.getDomainValue("tpIndicadorFreteMinimo").getValue() == null || 
						destino.getDomainValue("tpIndicadorFreteMinimo").getValue().equals("")) {					
					throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("percentualMinimo")});					
				} else {
				destinoProposta.setTpIndicadorFreteMinimo(destino.getDomainValue("tpIndicadorFreteMinimo"));
				}
				destinoProposta.setVlFreteMinimo(destino.getBigDecimal("vlFreteMinimo"));
				if(destino.getDomainValue("tpIndicadorFretePeso").getValue() == null ||
						destino.getDomainValue("tpIndicadorFretePeso").getValue().equals("")) {
					throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("fretePeso")});
				} else {
				destinoProposta.setTpIndicadorFretePeso(destino.getDomainValue("tpIndicadorFretePeso"));
				}
				destinoProposta.setVlFretePeso(destino.getBigDecimal("vlFretePeso"));
				if(destino.getDomainValue("tpIndicadorAdvalorem").getValue() == null ||
						destino.getDomainValue("tpIndicadorAdvalorem").getValue().equals("")){
					throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("adValorem")});
				} else {
				destinoProposta.setTpIndicadorAdvalorem(destino.getDomainValue("tpIndicadorAdvalorem"));
				}
				destinoProposta.setVlAdvalorem(destino.getBigDecimal("vlAdvalorem"));
				destinoProposta.setPcDiferencaFretePeso(BigDecimalUtils.defaultBigDecimal(destino.getBigDecimal("pcDiferencaFretePeso")));
				destinoProposta.setPcDiferencaAdvalorem(destino.getBigDecimal("pcDiferencaAdvalorem"));
				if(destino.getDomainValue("tpDiferencaAdvalorem").getValue() == null ||
						destino.getDomainValue("tpDiferencaAdvalorem").getValue().equals("")){
					throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("tipoDiferenca")});
				} else {
				destinoProposta.setTpDiferencaAdvalorem(destino.getDomainValue("tpDiferencaAdvalorem"));
				}

				if ("T".equals(destinoProposta.getTpDiferencaAdvalorem().getValue())){
					BigDecimal vlAdValorem = destinoProposta.getVlAdvalorem().add(destinoProposta.getPcDiferencaAdvalorem());
					
					if (vlAdValorem.compareTo(BigDecimal.ZERO) < 0){
						throw new BusinessException("LMS-30064");
					}
				}

				UnidadeFederativa unidadeFederativaDestino = new UnidadeFederativa();
				unidadeFederativaDestino.setIdUnidadeFederativa(destino.getLong("idUnidadeFederativaDestino"));
				destinoProposta.setUnidadeFederativa(unidadeFederativaDestino);

				if(destino.getLong("idTipoLocalizacaoMunicipioDestino") != null){
				TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino = new TipoLocalizacaoMunicipio();
				tipoLocalizacaoMunicipioDestino.setIdTipoLocalizacaoMunicipio(destino.getLong("idTipoLocalizacaoMunicipioDestino"));
				destinoProposta.setTipoLocalizacaoMunicipio(tipoLocalizacaoMunicipioDestino);
				}

				if(destino.getLong("idGrupoRegiao") != null){
					GrupoRegiao grupoRegiao = new GrupoRegiao();
					grupoRegiao.setIdGrupoRegiao(destino.getLong("idGrupoRegiao"));
					destinoProposta.setGrupoRegiao(grupoRegiao);
				}
				
				destinoProposta.setProposta(proposta);
				this.store(destinoProposta);

			/** Se não esta selecionado e possui ID, item deve ser excluido */
			} else if(LongUtils.hasValue(idDestinoProposta)) {
				removeIds.add(idDestinoProposta);
			}
		}
		/** Verifica se existem registro a Excluir */
		if(!removeIds.isEmpty()) {
			this.removeByIds(removeIds);
		}
	}
	
	public void storeDestinosProposta(Proposta proposta, List<DestinoProposta> destinosProposta, List<Long> removeIds) {
		propostaService.storeCancelWorkflow(proposta);

		this.storeAll(destinosProposta);
		
		/** Verifica se existem registro a Excluir */
		if(!removeIds.isEmpty()) {
			this.removeByIds(removeIds);
		}
	}

	public void removeByIdSimulacao(Long idSimulacao) {
		getDestinoPropostaDAO().removeByIdSimulacao(idSimulacao);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Gera a toda paremetrização do cliente, com referencia aos Destinos Proposta
	 * @param idSimulacao
	 */
	public List generateParametrosPropostaResumo(Long idSimulacao, Long idTipoLocalizacaoOrigem) {

		List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();		

		Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
		
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Capital", "O");
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioGrandeCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Grande Capital", "O");
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioInterior = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Interior", "O");
		ParcelaPreco parcelaPrecoDespacho = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_DESPACHO);
		
		

		List<DestinoProposta> destinosProposta = findDestinosPropostaByIdSimulacao(idSimulacao);
		for (DestinoProposta destinoProposta : destinosProposta) {

			Simulacao simulacao = proposta.getSimulacao();
			/** Busca a referencia do UNICO ParametroCliente cadastrado, quando tipo Geração for Capital/Interior */
			ParametroCliente parametroClienteBase = proposta.getParametroCliente();

			/** Busca Tarifa de Preço entre Origem e Destino */
			Object[] municipioFilialDestino = municipioFilialService
				.findMunicipioFilialVigenteByMunicipioPadraoMCD(destinoProposta.getUnidadeFederativa().getMunicipio().getIdMunicipio());			
			
			if(municipioFilialDestino != null){
				Long idTarifaPreco = getTarifaPreco(proposta, destinoProposta, simulacao);
				
				PrecoFrete precoFrete = precoFreteService.findPrecoFrete(simulacao.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_FRETE_QUILO, idTarifaPreco);
				BigDecimal vlFreteQuilo = precoFrete.getVlPrecoFrete();
				BigDecimal vlCheioFreteQuilo = precoFrete.getVlPrecoFrete();
	
				precoFrete = precoFreteService.findPrecoFrete(simulacao.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM, idTarifaPreco);
				BigDecimal vlAdvalorem = precoFrete.getVlPrecoFrete();
				BigDecimal vlCheioAdvalorem = precoFrete.getVlPrecoFrete();
	
				/** Verifica se Filial da sessão atende a Capital */
				Filial filialSessao = SessionUtils.getFilialSessao();
				UnidadeFederativa ufSessao = unidadeFederativaService.findByIdPessoa(filialSessao.getPessoa().getIdPessoa());
				Municipio municipioCapital = ufSessao.getMunicipio();
				MunicipioFilial municipioFilialCapital = municipioFilialService.findMunicipioFilial(municipioCapital.getIdMunicipio(), filialSessao.getIdFilial());
				if(municipioFilialCapital == null) {
					/** Frete Quilo */
					BigDecimal vlAcrescimoInterior = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("DIFERENCA_FRETE_PESO_CAP_INT", false);
					if(BigDecimalUtils.hasValue(vlAcrescimoInterior)) {
						vlFreteQuilo = BigDecimalUtils.acrescimo(vlFreteQuilo, vlAcrescimoInterior);
					}
				}
	
				Boolean grupoRegiaoProposta = (destinoProposta.getTipoLocalizacaoMunicipio() == null && destinoProposta.getGrupoRegiao() !=null);
				
				/** TipoLocalizacaoMunicipio Interior */
				if(grupoRegiaoProposta || tipoLocalizacaoMunicipioInterior.getIdTipoLocalizacaoMunicipio().equals(destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio())) {

					/** Frete Quilo */
					vlFreteQuilo = BigDecimalUtils.acrescimo(vlFreteQuilo, destinoProposta.getPcDiferencaFretePeso());
					/** Frete Advalorem */
					if("P".equals(destinoProposta.getTpDiferencaAdvalorem().getValue())){						
					vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvalorem, destinoProposta.getPcDiferencaAdvalorem());
					}else{
						if("T".equals(destinoProposta.getTpDiferencaAdvalorem().getValue())){
							vlAdvalorem = BigDecimalUtils.add(vlAdvalorem, destinoProposta.getPcDiferencaAdvalorem());
				}
					}
				}
	
				/** Frete Advalorem */
				String tpIndicador = destinoProposta.getTpIndicadorAdvalorem().getValue();
				if("V".equals(tpIndicador)) {
					vlAdvalorem = destinoProposta.getVlAdvalorem();
				} else if("D".equals(tpIndicador)) {
					vlAdvalorem = BigDecimalUtils.desconto(vlAdvalorem, destinoProposta.getVlAdvalorem());
				} else if("A".equals(tpIndicador)) {
					vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvalorem, destinoProposta.getVlAdvalorem());
				} else if("P".equals(tpIndicador)) {
					vlAdvalorem = BigDecimalUtils.add(vlAdvalorem, destinoProposta.getVlAdvalorem());
				}
				
				/*Percentual desconto AdValorem*/
				BigDecimal pcDescontoAdvalorem = BigDecimal.ZERO; 
				if(CompareUtils.gt(vlAdvalorem, vlCheioAdvalorem)){
					pcDescontoAdvalorem = BigDecimalUtils.HUNDRED.multiply(vlCheioAdvalorem.subtract(vlAdvalorem)).multiply(vlCheioAdvalorem);
				}
				
				/** Valor de DESPACHO*/
				BigDecimal vlDespacho = BigDecimal.ZERO;
				if(proposta.getBlPagaPesoExcedente() && "P".equals(proposta.getTpIndicadorMinFretePeso().getValue())) {
					/** Caso exista a parcela de DESPACHO */
					List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(simulacao.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_DESPACHO);
					if(!tabelaPrecoParcelas.isEmpty()) {
						TabelaPrecoParcela tabelaPrecoParcela = tabelaPrecoParcelas.get(0);
						vlDespacho = tabelaPrecoParcela.getGeneralidade().getVlGeneralidade();
	
						/** TipoLocalizacaoMunicipio Interior */
						if(grupoRegiaoProposta || tipoLocalizacaoMunicipioInterior.getIdTipoLocalizacaoMunicipio().equals(destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio())) {
							/** Acrescenta ao vlDespacho */
							vlDespacho = BigDecimalUtils.acrescimo(vlDespacho, destinoProposta.getPcDiferencaFretePeso());
						}
					}
				}
	
				/** Minimo Frete Quilo */
				BigDecimal vlMinimoFreteQuilo = proposta.getVlMinFretePeso().multiply(vlFreteQuilo);
				BigDecimal vlCheioMinimoFreteQuilo = proposta.getVlMinFretePeso().multiply(vlCheioFreteQuilo);
				if(proposta.getBlPagaPesoExcedente() && "P".equals(proposta.getTpIndicadorMinFretePeso().getValue())) {
					
					/** Inclui o vlDespacho ao vlMinimoFreteQuilo */
					vlMinimoFreteQuilo = vlMinimoFreteQuilo.add(vlDespacho);
	
					/** Verifica se deve ser gerada a Generalidade de DESPACHO Padrao, caso nao tenha sido cadastrado uma */
					generateGeneralidadeClienteDefault(parametroClienteBase, parcelaPrecoDespacho);
				}

				/** tpIndicador Frete Minimo do DestinoProposta */
				tpIndicador = destinoProposta.getTpIndicadorFreteMinimo().getValue();
				if("D".equals(tpIndicador)) {
					vlMinimoFreteQuilo = BigDecimalUtils.desconto(vlMinimoFreteQuilo, destinoProposta.getVlFreteMinimo());
				} else if("A".equals(tpIndicador)) {
					vlMinimoFreteQuilo = BigDecimalUtils.acrescimo(vlMinimoFreteQuilo, destinoProposta.getVlFreteMinimo());
				}

				/** Frete Quilo */
				tpIndicador = destinoProposta.getTpIndicadorFretePeso().getValue();
				if("D".equals(tpIndicador)) {
					vlFreteQuilo = BigDecimalUtils.desconto(vlFreteQuilo, destinoProposta.getVlFretePeso());
				} else if("A".equals(tpIndicador)) {
					vlFreteQuilo = BigDecimalUtils.acrescimo(vlFreteQuilo, destinoProposta.getVlFretePeso());
				}
				
				/*PREENCHE O MAPA */ 
				Map<String, Object> field = new HashMap<String, Object>();
				
				
				/*SIGLA DESCRIÇÃO*/
				if(!idTipoLocalizacaoOrigem.equals(tipoLocalizacaoMunicipioInterior.getIdTipoLocalizacaoMunicipio())
						&& (destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio().equals(tipoLocalizacaoMunicipioCapital.getIdTipoLocalizacaoMunicipio())
							|| destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio().equals(tipoLocalizacaoMunicipioGrandeCapital.getIdTipoLocalizacaoMunicipio()))) {
					field.put("siglaDescricao", destinoProposta.getUnidadeFederativa().getSgUnidadeFederativa().concat(" - ").concat(idTipoLocalizacaoOrigem.equals(tipoLocalizacaoMunicipioGrandeCapital.getIdTipoLocalizacaoMunicipio()) ? "Grande Capital" : "Capital"));
				} else if(destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio().equals(tipoLocalizacaoMunicipioInterior.getIdTipoLocalizacaoMunicipio())) {
					field.put("siglaDescricao", destinoProposta.getUnidadeFederativa().getSgUnidadeFederativa().concat(" - Interior"));
				}			
							
				field.put("idDestinoProposta", destinoProposta.getIdDestinoProposta());
				
				/*FRETE MÍNIMO*/
				field.put("tpIndicadorFreteMinimo", destinoProposta.getTpIndicadorFreteMinimo().getValue());
				field.put("vlParametroFreteMinimo", destinoProposta.getVlFreteMinimo());
				field.put("vlFreteMinimo", vlMinimoFreteQuilo);			
				field.put("vlCheioFreteMinimo", vlCheioMinimoFreteQuilo);

				/*FRETE PESO*/
				field.put("tpIndicadorFretePeso", destinoProposta.getTpIndicadorFretePeso().getValue());
				field.put("vlParametroFretePeso", destinoProposta.getVlFretePeso());
				field.put("vlFretePeso", vlFreteQuilo);
				field.put("vlCheioFretePeso", vlCheioFreteQuilo);
				field.put("pcDiferencaFretePeso", destinoProposta.getPcDiferencaFretePeso());

				/*AD VALOREM*/
				field.put("tpIndicadorAdvalorem", destinoProposta.getTpIndicadorAdvalorem().getValue());
				field.put("vlParametroAdvalorem", destinoProposta.getVlAdvalorem());
				field.put("vlAdvalorem", vlAdvalorem);						
				field.put("vlCheioAdvalorem", vlCheioAdvalorem);						
				field.put("pcDiferencaAdvalorem", destinoProposta.getPcDiferencaAdvalorem());
				
				if(destinoProposta.getTpDiferencaAdvalorem() != null){
					field.put("tpDiferencaAdvalorem", destinoProposta.getTpDiferencaAdvalorem().getValue());
				}
				
				field.put("pcDescontoAdvalorem", pcDescontoAdvalorem);
							
				field.put("idUnidadeFederativaDestino", destinoProposta.getUnidadeFederativa().getIdUnidadeFederativa());
				field.put("idTipoLocalizacaoMunicipioDestino", destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio());
				
				retorno.add(field);
			}
		}//DestinoProposta
		return retorno;
	}

	/**
	 * Gera a toda paremetrização do cliente, com referencia aos Destinos Proposta
	 * @author Andre Valadas
	 * @param idSimulacao
	 */
	public void generateParametrosProposta(Long idSimulacao) {
		/**
		 * Valida e EXCLUI qualquer parametrização anteriormente inserida por
		 * essa PROPOSTA
		 */
		Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
		parametroClienteService.removeBySimulacao(proposta.getSimulacao());

		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Capital", "O");
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioGrandeCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Grande Capital", "O");
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioInterior = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Interior", "O");
		ParcelaPreco parcelaPrecoDespacho = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_DESPACHO);
		TabelaPreco tabelaPreco = proposta.getSimulacao().getTabelaPreco();

		Filial filialSessao = SessionUtils.getFilialSessao();
		
		
		boolean isPropostaMinimo = ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(proposta.getSimulacao().getTpGeracaoProposta().getValue()) ||
		            ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_VALOR_KG.equals(proposta.getSimulacao().getTpGeracaoProposta().getValue()) ||
		            ConstantesVendas.TP_PROPOSTA_CONVENCIONAL.equals(proposta.getSimulacao().getTpGeracaoProposta().getValue());

		List<DestinoProposta> destinosProposta = findDestinosPropostaByIdSimulacao(idSimulacao);
		for (DestinoProposta destinoProposta : destinosProposta) {

			Simulacao simulacao = proposta.getSimulacao();
			/**
			 * Busca a referencia do UNICO ParametroCliente cadastrado, quando
			 * tipo Geração for Capital/Interior
			 */
			ParametroCliente parametroClienteBase = proposta.getParametroCliente();

			BigDecimal vlFreteQuilo = BigDecimal.ZERO;
			BigDecimal vlAdvalorem = BigDecimal.ZERO;
			BigDecimal vlMinimoFreteQuilo = BigDecimal.ZERO;

			/** Se TpGeracaoProposta diferente de "O"( promocional) */
			if (!"O".equals(simulacao.getTpGeracaoProposta().getValue())) {

				/** Busca Tarifa de Preço entre Origem e Destino */
				Long idTarifaPreco = getTarifaPreco(proposta, destinoProposta, simulacao);

				if (!isPropostaMinimo){
				    
				    PrecoFrete precoFrete = precoFreteService.findPrecoFrete(simulacao.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_FRETE_QUILO,
				            idTarifaPreco);
				    if (precoFrete == null) {
				        throw new BusinessException("LMS-01196");
				    }
				    vlFreteQuilo = precoFrete.getVlPrecoFrete();
				    
				    precoFrete = precoFreteService.findPrecoFrete(simulacao.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM, idTarifaPreco);
				    if (precoFrete == null) {
				        throw new BusinessException("LMS-01196");
				    }
				    vlAdvalorem = precoFrete.getVlPrecoFrete();
				    
				    /** Verifica se Filial da sessão atende a Capital */
				    UnidadeFederativa ufSessao = unidadeFederativaService.findByIdPessoa(filialSessao.getPessoa().getIdPessoa());
				    Municipio municipioCapital = ufSessao.getMunicipio();
				    MunicipioFilial municipioFilialCapital = municipioFilialService.findMunicipioFilial(municipioCapital.getIdMunicipio(),
				            filialSessao.getIdFilial());
				    if (municipioFilialCapital == null) {
				        /** Frete Quilo */
				        BigDecimal vlAcrescimoInterior = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("DIFERENCA_FRETE_PESO_CAP_INT", false);
				        if (BigDecimalUtils.hasValue(vlAcrescimoInterior)) {
				            vlFreteQuilo = BigDecimalUtils.acrescimo(vlFreteQuilo, vlAcrescimoInterior);
				        }
				    }
				    
				    /**
				     * caso não existir um TipoLocalizacaoMunicipio e existir um
				     * grupo região ou TipoLocalizacaoMunicipio Interior
				     */
				    if (destinoProposta.getTipoLocalizacaoMunicipio() != null
				            && tipoLocalizacaoMunicipioInterior.getIdTipoLocalizacaoMunicipio().equals(
				                    destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio()) || destinoProposta.getGrupoRegiao() != null) {
				        /** Frete Quilo */
				        vlFreteQuilo = BigDecimalUtils.acrescimo(vlFreteQuilo, destinoProposta.getPcDiferencaFretePeso());
				        /** Frete Advalorem */
				        if ("P".equals(destinoProposta.getTpDiferencaAdvalorem().getValue())) {
				            vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvalorem, destinoProposta.getPcDiferencaAdvalorem());
				        } else {
				            if ("T".equals(destinoProposta.getTpDiferencaAdvalorem().getValue())) {
				                vlAdvalorem = BigDecimalUtils.add(vlAdvalorem, destinoProposta.getPcDiferencaAdvalorem());
				            }
				        }
				    }
				    
				    /** Frete Advalorem */
				    String tpIndicador = destinoProposta.getTpIndicadorAdvalorem().getValue();
				    if ("V".equals(tpIndicador)) {
				        vlAdvalorem = destinoProposta.getVlAdvalorem();
				    } else if ("D".equals(tpIndicador)) {
				        vlAdvalorem = BigDecimalUtils.desconto(vlAdvalorem, destinoProposta.getVlAdvalorem());
				    } else if ("A".equals(tpIndicador)) {
				        vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvalorem, destinoProposta.getVlAdvalorem());
				    } else if ("P".equals(tpIndicador)) {
				        vlAdvalorem = BigDecimalUtils.add(vlAdvalorem, destinoProposta.getVlAdvalorem());
				        if (vlAdvalorem.compareTo(BigDecimal.ZERO) < 0) {
				            throw new BusinessException("LMS-30064");
				        }
				    }
				}

				/** Valor de DESPACHO */
				BigDecimal vlDespacho = BigDecimal.ZERO;
				if (proposta.getBlPagaPesoExcedente() && "P".equals(proposta.getTpIndicadorMinFretePeso().getValue())) {
					/** Caso exista a parcela de DESPACHO */
					List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(simulacao.getTabelaPreco()
							.getIdTabelaPreco(), ConstantesExpedicao.CD_DESPACHO);
					if (!tabelaPrecoParcelas.isEmpty()) {
						TabelaPrecoParcela tabelaPrecoParcela = tabelaPrecoParcelas.get(0);
						vlDespacho = tabelaPrecoParcela.getGeneralidade().getVlGeneralidade();

						/**
						 * caso não existir um TipoLocalizacaoMunicipio e
						 * existir um grupo região ou TipoLocalizacaoMunicipio
						 * Interior
						 */
						if (destinoProposta.getTipoLocalizacaoMunicipio() != null
								&& tipoLocalizacaoMunicipioInterior.getIdTipoLocalizacaoMunicipio().equals(
										destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio())
								|| destinoProposta.getGrupoRegiao() != null) {
							/** Acrescenta ao vlDespacho */
							vlDespacho = BigDecimalUtils.acrescimo(vlDespacho, destinoProposta.getPcDiferencaFretePeso());
						}
					}
				}

				/** Frete Quilo */
				if (isPropostaMinimo){
				    vlFreteQuilo = destinoProposta.getVlFretePeso();
				}else{
				    String tpIndicador = destinoProposta.getTpIndicadorFretePeso().getValue();
	                if ("D".equals(tpIndicador)) {
	                    vlFreteQuilo = BigDecimalUtils.desconto(vlFreteQuilo, destinoProposta.getVlFretePeso());
	                } else if ("A".equals(tpIndicador)) {
	                    vlFreteQuilo = BigDecimalUtils.acrescimo(vlFreteQuilo, destinoProposta.getVlFretePeso());
	                }
				}
				
				
				
				/** Minimo Frete Quilo */
				vlMinimoFreteQuilo = BigDecimal.ZERO;
				if (isPropostaMinimo){
				    vlMinimoFreteQuilo = destinoProposta.getVlFreteMinimo();
				}else{
				    if ("P".equals(proposta.getTpIndicadorMinFretePeso().getValue())) {
				        vlMinimoFreteQuilo = proposta.getVlMinFretePeso().multiply(vlFreteQuilo);
				        if (tabelaPreco.getPcDescontoFreteMinimo() != null && tabelaPreco.getPcDescontoFreteMinimo().compareTo(new BigDecimal(0)) > 0) {
				            vlMinimoFreteQuilo = vlMinimoFreteQuilo.subtract((vlMinimoFreteQuilo.multiply(tabelaPreco.getPcDescontoFreteMinimo()))
				                    .multiply(new BigDecimal(0.01)));
				        }
				    }
				    if (proposta.getBlPagaPesoExcedente() && "P".equals(proposta.getTpIndicadorMinFretePeso().getValue())) {
				        /** Inclui o vlDespacho ao vlMinimoFreteQuilo */
				        vlMinimoFreteQuilo = vlMinimoFreteQuilo.add(vlDespacho);
				        
				        /** tpIndicador do DestinoProposta */
				        String tpIndicador = destinoProposta.getTpIndicadorFreteMinimo().getValue();
				        if ("D".equals(tpIndicador)) {
				            vlMinimoFreteQuilo = BigDecimalUtils.desconto(vlMinimoFreteQuilo, destinoProposta.getVlFreteMinimo());
				        } else if ("A".equals(tpIndicador)) {
				            vlMinimoFreteQuilo = BigDecimalUtils.acrescimo(vlMinimoFreteQuilo, destinoProposta.getVlFreteMinimo());
				        }
				        
				        /**
				         * Verifica se deve ser gerada a Generalidade de DESPACHO
				         * Padrao, caso nao tenha sido cadastrado uma
				         */
				        generateGeneralidadeClienteDefault(parametroClienteBase, parcelaPrecoDespacho);
				    }
				}
				
			}

			/** Parametro Cliente */
			ParametroCliente pc = ParametroClienteUtils.getParametroClientePadrao();
			ParametroClienteUtils.copyParametroClienteCompleto(parametroClienteBase, pc);
			pc.setTpIndicadorPercMinimoProgr(new DomainValue("D"));
			pc.setVlPercMinimoProgr(BigDecimal.ZERO);

			if ("P".equals(proposta.getSimulacao().getTpGeracaoProposta().getValue()) || "O".equals(proposta.getSimulacao().getTpGeracaoProposta().getValue())) {
				pc.setTpIndicadorMinFretePeso(new DomainValue("T"));
				pc.setTpIndicadorFretePeso(new DomainValue("T"));
				pc.setVlMinFretePeso(BigDecimal.ZERO);
				pc.setVlFretePeso(BigDecimal.ZERO);

				pc.setTpIndicadorAdvalorem2(new DomainValue("T"));
				pc.setTpIndicadorAdvalorem(new DomainValue("T"));
				pc.setVlAdvalorem(BigDecimal.ZERO);
				pc.setVlAdvalorem2(BigDecimal.ZERO);
				pc.setVlMinimoFreteQuilo(BigDecimal.ZERO);
			} else {
				
				pc.setTpIndicadorFretePeso(new DomainValue("V"));
				pc.setVlFretePeso(vlFreteQuilo);
				pc.setVlMinimoFreteQuilo(vlMinimoFreteQuilo); //OK
				
				if (isPropostaMinimo){
				    pc.setTpIndicadorMinFretePeso(new DomainValue("P"));
				    pc.setVlMinFretePeso(destinoProposta.getPsMinimoFretePeso());  
				    if ("V".equals(proposta.getSimulacao().getTpGeracaoProposta().getValue())){
				        pc.setTpTarifaMinima(new DomainValue("V"));
				        pc.setVlTarifaMinima(destinoProposta.getVlFreteMinimo());
				        pc.setVlMinimoFreteQuilo(BigDecimal.ZERO);
				    }
				    
				    pc.setTpIndicadorAdvalorem2(new DomainValue("V"));
	                pc.setTpIndicadorAdvalorem(new DomainValue("V"));
	                pc.setVlAdvalorem(destinoProposta.getVlAdvalorem());
	                pc.setVlAdvalorem2(destinoProposta.getVlAdvalorem2());
	                
				}else{
				    pc.setTpIndicadorMinFretePeso(proposta.getTpIndicadorMinFretePeso());
				    pc.setVlMinFretePeso(proposta.getVlMinFretePeso());
				    pc.setTpIndicadorAdvalorem2(new DomainValue("T"));
	                pc.setTpIndicadorAdvalorem(new DomainValue("V"));
	                pc.setVlAdvalorem(vlAdvalorem);
	                pc.setVlAdvalorem2(BigDecimal.ZERO);
				}
				
			}

			pc.setTpIndicadorValorReferencia(new DomainValue("T"));
			pc.setVlValorReferencia(BigDecimal.ZERO);
			if (isPropostaMinimo){
                pc.setVlTblEspecifica(destinoProposta.getVlProdutoEspecifico());
                pc.setTpIndicVlrTblEspecifica(new DomainValue("V"));
			}else{
			    pc.setVlTblEspecifica(BigDecimal.ZERO);
			    pc.setTpIndicVlrTblEspecifica(new DomainValue("T"));
			}
			pc.setPcDescontoFreteTotal(BigDecimal.ZERO);
			pc.setVlFreteVolume(BigDecimal.ZERO);
			pc.setBlPagaCubagem(proposta.getBlPagaCubagem());
			pc.setPcPagaCubagem(proposta.getPcPagaCubagem());
			
			if (ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(simulacao.getTpGeracaoProposta().getValue())){
			    pc.setBlPagaPesoExcedente(Boolean.TRUE);
			}else{
			    pc.setBlPagaPesoExcedente(proposta.getBlPagaPesoExcedente());
			}
			pc.setTabelaDivisaoCliente(null);
			pc.setClienteByIdClienteRedespacho(null);
			pc.setFilialByIdFilialMercurioRedespacho(null);

			/** Percentual */
			pc.setPcFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getPcFretePercentual()));
			pc.setVlMinimoFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getVlMinimoFretePercentual()));
			pc.setVlToneladaFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getVlToneladaFretePercentual()));
			pc.setPsFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getPsFretePercentual()));

			/** Filial/TipoLocalizacao/Zona/Pais/UF */
			pc.setFilialByIdFilialOrigem(null);
			pc.setFilialByIdFilialDestino(null);
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(null);
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(null);

			if (destinoProposta.getRotaPreco() != null){
			    RotaPreco rp = destinoProposta.getRotaPreco();
			    pc.setAeroportoByIdAeroportoDestino(rp.getAeroportoByIdAeroportoDestino());
			    pc.setAeroportoByIdAeroportoOrigem(rp.getAeroportoByIdAeroportoOrigem());
			    pc.setUnidadeFederativaByIdUfDestino(rp.getUnidadeFederativaByIdUfDestino());
			    pc.setUnidadeFederativaByIdUfOrigem(rp.getUnidadeFederativaByIdUfOrigem());
			} else {
			    pc.setAeroportoByIdAeroportoDestino(destinoProposta.getAeroportoByIdAeroporto());
			}
			
			pc.setMunicipioByIdMunicipioOrigem(null);
			pc.setMunicipioByIdMunicipioDestino(null);

			pc.setSimulacao(simulacao);
			pc.setCotacao(null);
			pc.setProposta(null);

			pc.setPcReajFretePeso(null);
			pc.setPcReajVlMinimoFreteQuilo(null);
			pc.setPcReajVlFreteVolume(null);
			pc.setPcReajAdvalorem(null);
			pc.setPcReajAdvalorem2(null);
			pc.setPcReajVlMinimoFretePercen(null);
			pc.setPcReajMinimoGris(null);
			pc.setPcReajTarifaMinima(null);
			pc.setPcReajVlTarifaEspecifica(null);
			pc.setPcReajVlToneladaFretePerc(null);
			pc.setPcReajPedagio(null);
			pc.setTabelaPreco(simulacao.getTabelaPreco());

			if (destinoProposta.getGrupoRegiao() != null) {
				pc.setGrupoRegiaoDestino(destinoProposta.getGrupoRegiao());
			}

			if (isPropostaMinimo){
			    
			    pc.setValoresFaixaProgressivaProposta(new ArrayList<ValorFaixaProgressivaProposta>());
			    
    	        List<ValorFaixaProgressivaProposta> valoresFaixaProgressiva = valorFaixaProgressivaPropostaService.findByDestinoProposta(destinoProposta);
    	        if (valoresFaixaProgressiva != null){
    	            
    	            for (ValorFaixaProgressivaProposta valor:valoresFaixaProgressiva){
    	                ValorFaixaProgressivaProposta valorFaixaParametro = new ValorFaixaProgressivaProposta();
    	                valorFaixaParametro.setDestinoProposta(null);
    	                valorFaixaParametro.setParametroCliente(pc);
    	                valorFaixaParametro.setFaixaProgressivaProposta(valor.getFaixaProgressivaProposta());
    	                valorFaixaParametro.setTpIndicador(new DomainValue("V"));
    	                valorFaixaParametro.setVlFixo(valor.getVlFixo());
    	                
    	                pc.getValoresFaixaProgressivaProposta().add(valorFaixaParametro);
    	            }
    	        }
			    
			}
			
			String tpModal = simulacao.getServico().getTpModal().getValue();
			
			/** Gera Parametro para Frete Expedido */
			if (proposta.getBlFreteExpedido() || "A".equals(tpModal)) {
				/** Filial */
			    
			    if (ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(simulacao.getTpGeracaoProposta().getValue()) 
			    		|| ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_VALOR_KG.equals(simulacao.getTpGeracaoProposta().getValue())
			    		|| ConstantesVendas.TP_PROPOSTA_CONVENCIONAL.equals(simulacao.getTpGeracaoProposta().getValue()) ) {
			        pc.setFilialByIdFilialOrigem(null);
			        
			    }else{
			        pc.setFilialByIdFilialOrigem(filialSessao);
	                pc.setUnidadeFederativaByIdUfOrigem(proposta.getUnidadeFederativaByIdUfOrigem());
	                pc.setUnidadeFederativaByIdUfDestino(destinoProposta.getUnidadeFederativa());
			    }
				
				/** TipoLocalizacao */
				if (tipoLocalizacaoMunicipioCapital != null && destinoProposta.getTipoLocalizacaoMunicipio() != null) {
					if (tipoLocalizacaoMunicipioCapital.getIdTipoLocalizacaoMunicipio().equals(
							destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio())
							|| tipoLocalizacaoMunicipioGrandeCapital.getIdTipoLocalizacaoMunicipio().equals(
									destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio())) {
						pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(destinoProposta.getTipoLocalizacaoMunicipio());
					}
				}
				/** Grupo Região */
				if (destinoProposta.getGrupoRegiao() != null) {
					pc.setGrupoRegiaoDestino(destinoProposta.getGrupoRegiao());
				}
				

				/** Parametro relacionado a Proposta */
				parametroClienteService.store(pc);
				
				if (isPropostaMinimo){
				    generateTaxasParametroCliente(pc,destinoProposta);
				}
			}
			

			
			/** Gera Parametro para Frete Recebido(Rota inversa) */
			if (proposta.getBlFreteRecebido() && !"A".equals(tpModal)) {
				/** Filial */
				Filial filialOrigem = null;
				TipoLocalizacaoMunicipio tpLocalizacaoMunicipio = (destinoProposta != null && destinoProposta.getTipoLocalizacaoMunicipio() != null) ? destinoProposta
						.getTipoLocalizacaoMunicipio() : null;
				boolean isGrandeCapital = tipoLocalizacaoMunicipioGrandeCapital != null && tpLocalizacaoMunicipio != null
						&& tipoLocalizacaoMunicipioGrandeCapital.getIdTipoLocalizacaoMunicipio().equals(tpLocalizacaoMunicipio.getIdTipoLocalizacaoMunicipio());
				boolean isCapital = tipoLocalizacaoMunicipioCapital != null && tpLocalizacaoMunicipio != null
						&& tipoLocalizacaoMunicipioCapital.getIdTipoLocalizacaoMunicipio().equals(tpLocalizacaoMunicipio.getIdTipoLocalizacaoMunicipio());

				if (isGrandeCapital || isCapital) {
					MunicipioFilial municipioFilial = (MunicipioFilial) municipioFilialService.findMunicipioFilialVigenteByIdMunicipio(
							destinoProposta.getUnidadeFederativa().getMunicipio().getIdMunicipio()).get(0);
					filialOrigem = municipioFilial.getFilial();
				}
				Filial filialDestino = filialSessao;

				/** 3.Valida para não incluir parametroCliente com Rotas IGUAIS */
				if (filialOrigem == null || !filialOrigem.equals(filialDestino)) {
					ParametroCliente parametroCliente = new ParametroCliente();
					ParametroClienteUtils.copyParametroClienteCompleto(pc, parametroCliente);
					parametroCliente.setSimulacao(simulacao);

					/** Percentual */
					parametroCliente.setPcFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getPcFretePercentual()));
					parametroCliente.setVlMinimoFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getVlMinimoFretePercentual()));
					parametroCliente.setVlToneladaFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getVlToneladaFretePercentual()));
					parametroCliente.setPsFretePercentual(BigDecimalUtils.defaultBigDecimal(destinoProposta.getPsFretePercentual()));

					/** Grupo regiao */
					if (destinoProposta.getGrupoRegiao() != null) {
						parametroCliente.setGrupoRegiaoOrigem(destinoProposta.getGrupoRegiao());
					}

					/** Zona */
					parametroCliente.setZonaByIdZonaOrigem(pc.getZonaByIdZonaDestino());
					parametroCliente.setZonaByIdZonaDestino(pc.getZonaByIdZonaOrigem());
					/** Pais */
					parametroCliente.setPaisByIdPaisOrigem(pc.getPaisByIdPaisDestino());
					parametroCliente.setPaisByIdPaisDestino(pc.getPaisByIdPaisOrigem());
					/** UF */
					parametroCliente.setUnidadeFederativaByIdUfOrigem(destinoProposta.getUnidadeFederativa());
					parametroCliente.setUnidadeFederativaByIdUfDestino(proposta.getUnidadeFederativaByIdUfOrigem());
					/** Filial */
					parametroCliente.setFilialByIdFilialOrigem(filialOrigem);
					/** #Quest 11725 */
					parametroCliente.setFilialByIdFilialDestino(filialDestino);
					/** TipoLocalizacao */
					parametroCliente.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(null);

					/** Parametro relacionado a Proposta */
					parametroClienteService.store(parametroCliente);
				}
			}
		}
	}

	private void generateTaxasParametroCliente(ParametroCliente parametroCliente, DestinoProposta destinoProposta){
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaColetaUrbanaConvencional(), destinoProposta.getPsMinimoTaxaColetaUrbanaConvencional(), destinoProposta.getVlExcedenteTaxaColetaUrbanaConvencional(), "IDColetaUrbanaConvencional");
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaColetaUrbanaEmergencial(), destinoProposta.getPsMinimoTaxaColetaUrbanaEmergencial(), destinoProposta.getVlExcedenteTaxaColetaUrbanaEmergencial(), "IDColetaUrbanaEmergencia");
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaEntregaUrbanaConvencional(), destinoProposta.getPsMinimoTaxaEntregaUrbanaConvencional(), destinoProposta.getVlExcedenteTaxaEntregaUrbanaConvencional(),  "IDEntregaUrbanaConvencional");
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaEntregaUrbanaEmergencial(), destinoProposta.getPsMinimoTaxaEntregaUrbanaEmergencial(), destinoProposta.getVlExcedenteTaxaEntregaUrbanaEmergencial(), "IDEntregaUrbanaEmergencia");
	    
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaColetaInteriorConvencional(), destinoProposta.getPsMinimoTaxaColetaInteriorConvencional(), destinoProposta.getVlExcedenteTaxaColetaInteriorConvencional(), "IDColetaInteriorConvencional");
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaColetaInteriorEmergencial(), destinoProposta.getPsMinimoTaxaColetaInteriorEmergencial(), destinoProposta.getVlExcedenteTaxaColetaInteriorEmergencial(), "IDColetaInteriorEmergencia");
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaEntregaInteriorConvencional(), destinoProposta.getPsMinimoTaxaEntregaInteriorConvencional(), destinoProposta.getVlExcedenteTaxaEntregaInteriorConvencional(), "IDEntregaInteriorConvencional");
	    storeTaxaCliente(parametroCliente, destinoProposta.getVlTaxaEntregaInteriorEmergencial(), destinoProposta.getPsMinimoTaxaEntregaInteriorEmergencial(), destinoProposta.getVlExcedenteTaxaEntregaInteriorEmergencial(), "IDEntregaInteriorEmergencia");
	}
	
	private void storeTaxaCliente(ParametroCliente pc, BigDecimal vlTaxa, BigDecimal psMinimo, BigDecimal vlExcedente, String cdParcelaPreco){
	    if (vlTaxa == null){
	        return;
	    }
	    
	    TaxaCliente taxaCliente = new TaxaCliente();
        ParcelaPreco parcelaPreco = null;
        parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(cdParcelaPreco);
        taxaCliente.setParametroCliente(pc);
        taxaCliente.setParcelaPreco(parcelaPreco);
        taxaCliente.setVlTaxa(vlTaxa);
        taxaCliente.setPsMinimo(psMinimo);
        taxaCliente.setVlExcedente(vlExcedente);
        taxaCliente.setTpTaxaIndicador(new DomainValue("V"));
        taxaClienteService.store(taxaCliente);
	}
	
	
	private Long getTarifaPreco(Proposta proposta, DestinoProposta destinoProposta, Simulacao simulacao) {
		TabelaPreco tabelaPreco =  proposta.getSimulacao().getTabelaPreco();
		String tpTabelaPreco = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
		String sbTpTabelaPreco = tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();
		boolean divisaoTpTabela = "D".equals(tpTabelaPreco) 
							   || "@".equals(tpTabelaPreco) 
							   || "B".equals(tpTabelaPreco) 
							   || "I".equals(tpTabelaPreco) 
							   || "F".equals(sbTpTabelaPreco) 
							   || "P".equals(sbTpTabelaPreco);
		
		Long idTarifaPreco = null;
		Long idMunicipioOrigem = proposta.getUnidadeFederativaByIdUfOrigem().getMunicipio().getIdMunicipio();
		Long idMunicipioDestino = destinoProposta.getUnidadeFederativa().getMunicipio().getIdMunicipio();
		Long idServico = simulacao.getServico().getIdServico();
		
		if(divisaoTpTabela){
			idTarifaPreco = mcdService.findTarifaMunicipios(idMunicipioOrigem, idMunicipioDestino, idServico);
		}else{
			/*CQPRO00025223 - Vendas Brasil*/
			String paramDtImplantacao = (String)parametroGeralService.findConteudoByNomeParametro("IMPLANTACAO_VENDA_BRASIL", false);
			if(paramDtImplantacao == null){
				throw new BusinessException("Parametro IMPLANTACAO_VENDA_BRASIL não encontrado");
			}
			
			YearMonthDay dtImplantacaoVendasBrasil = JTDateTimeUtils.convertDataStringToYearMonthDay(paramDtImplantacao);
			if(dtImplantacaoVendasBrasil != null){
				/*Se a tabela utilizada possuir a data de vigência inicial maior ou igual a data no
				parâmetro geral IMPLANTACAO_VENDA_BRASIL deverá ser utilizado a tarifa atual
				senão a tarifa antiga*/
				TarifaPreco[] tarifas = mcdService.findTarifasPrecoLMS(idMunicipioOrigem, idMunicipioDestino, idServico);
				if(JTDateTimeUtils.comparaData(tabelaPreco.getDtVigenciaInicial(), dtImplantacaoVendasBrasil) >= 0){
					idTarifaPreco = tarifas[0].getIdTarifaPreco();
				}else{
					idTarifaPreco = tarifas[1].getIdTarifaPreco();
				}
			}
		}
		return idTarifaPreco;
	}
	
	
	/**
	 * Gera Generalidade de DESPACHO, caso nao tenha sido cadastrado uma
	 * @param parametroCliente
	 * @param parcelaPreco
	 */
	private void generateGeneralidadeClienteDefault(ParametroCliente parametroCliente, ParcelaPreco parcelaPreco) {
		Boolean existDespacho = Boolean.FALSE;
		List<GeneralidadeCliente> generalidadeClientes = parametroCliente.getGeneralidadeClientes();
			for (GeneralidadeCliente generalidadeCliente : generalidadeClientes) {
				if(ConstantesExpedicao.CD_DESPACHO.equals(generalidadeCliente.getParcelaPreco().getCdParcelaPreco())) {
					existDespacho = Boolean.TRUE;
					break;
				}
			}

		/** Verifica se deve incluir Generalidade Padrao de DESPACHO */
		if(!existDespacho) {
			GeneralidadeCliente generalidadeCliente = new GeneralidadeCliente();
			generalidadeCliente.setParametroCliente(parametroCliente);
			generalidadeCliente.setParcelaPreco(parcelaPreco);
			generalidadeCliente.setTpIndicador(new DomainValue("V"));
			generalidadeCliente.setVlGeneralidade(BigDecimal.ZERO);
			generalidadeCliente.setTpIndicadorMinimo(new DomainValue("T"));
			generalidadeCliente.setVlMinimo(BigDecimal.ZERO);
			generalidadeCliente.setPcReajMinimo(BigDecimal.ZERO);
			generalidadeClientes.add(generalidadeCliente);
		}
		parametroCliente.setGeneralidadeClientes(generalidadeClientes);
	}
	
	
	public List<Map<String, Object>> generateDestinosAereo(TypedFlatMap criteria) {
	    Proposta proposta = propostaService.findByIdSimulacao(criteria.getLong("idSimulacao"));
	    Simulacao simulacao = simulacaoService.findById(criteria.getLong("idSimulacao"));
	    if (proposta != null){
	        criteria.put("idProposta", proposta.getIdProposta());
	    }
        criteria.put("idTabelaPreco", simulacao.getTabelaPreco().getIdTabelaPreco());
        if (simulacao.getProdutoEspecifico()!= null){
            criteria.put("idProdutoEspecifico", simulacao.getProdutoEspecifico().getIdProdutoEspecifico());
        }
        Aeroporto aeroporto = aeroportoService.findAeroportoAtendeCliente(simulacao.getClienteByIdCliente().getIdCliente());
        if (aeroporto!=null){
            criteria.put("idAeroportoOrigem", aeroporto.getIdAeroporto());
        }
		
		return getDestinoPropostaDAO().findDestinosPropostaAereoByIdProposta(criteria);
	}
	
	/**
	 * Monta todos destinos pelas UFs atendidas pela empresa, carregando os dados das mesmas quando já salvas 
	 * @author Andre Valadas
	 * @param criteria
	 * @return
	 */
	public List generateDestinosProposta(TypedFlatMap criteria) {
		
		Long    idSimulacao = criteria.getLong("simulacao.idSimulacao");
		Long    idTipoLocalizacaoOrigem  = criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		Boolean isGenerate = criteria.getBoolean("isGenerate");
		
		BigDecimal pcDiferencaFretePeso = criteria.getBigDecimal("pcDiferencaFretePeso");		
		BigDecimal vlPercentualCalculado = BigDecimal.ZERO; 
		
		/*Lista com retorno de dados da grid*/
		List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
				
		/*Origem*/
		TipoLocalizacaoMunicipio tipoLocalizacaoOrigem = tipoLocalizacaoMunicipioService.findById(idTipoLocalizacaoOrigem);
				
		/*Lista de DestinoProposta por Simulação */
		List<DestinoProposta> destinosPropostas = new ArrayList<DestinoProposta>();
		if(idSimulacao != null) {
			 destinosPropostas = this.findDestinosPropostaByIdSimulacao(idSimulacao);
		}		
		
		/*Gerar a Grid de Destinos*/
		if(BooleanUtils.isFalse(isGenerate) && CollectionUtils.isNotEmpty(destinosPropostas)){
			
			String descricaoDestino = null;
			for (DestinoProposta destinoProposta : destinosPropostas) {

				Map<String, Object> field = this.populateDestinoProposta(destinoProposta);
				
				if(destinoProposta.getGrupoRegiao() != null){
					descricaoDestino = destinoProposta.getGrupoRegiao().getDsGrupoRegiao();
				}else{
					descricaoDestino = destinoProposta.getTipoLocalizacaoMunicipio().getDsTipoLocalizacaoMunicipio().getValue();
				}
				
				field.put("siglaDescricao", destinoProposta.getUnidadeFederativa().getSgUnidadeFederativa().concat(" - ").concat(descricaoDestino));

			toReturn.add(field);
		}
			
		}else{
			
			/*Parametros para montagem da grid*/
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Capital", "O");
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioGrandeCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Grande Capital", "O");
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioInterior = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Interior", "O");
			
			Map<String, Object> field = null;

			Simulacao simulacao = simulacaoService.findById(idSimulacao);
			
			TabelaPreco tabelaPreco = simulacao.getTabelaPreco();

			Pais pais = paisService.findPaisBySgPais("BRA");
			List<UnidadeFederativa> unidadesFederativas = unidadeFederativaService.findUfsByPais(pais.getIdPais(), "A"); 
			for (UnidadeFederativa uf : unidadesFederativas) {

				field = new HashMap<String, Object>();
				
				/*Capital*/
				if("Capital/Interior".equals(tipoLocalizacaoOrigem.getDsTipoLocalizacaoMunicipio().getValue())){
					
					toReturn.add(findRegiaoDestino("Capital", tabelaPreco, uf, tipoLocalizacaoMunicipioCapital, criteria, destinosPropostas));
					
					toReturn.add(findRegiaoDestino("Interior", tabelaPreco, uf, tipoLocalizacaoMunicipioInterior, criteria, destinosPropostas));
					
				/*Grande Capital*/
				}else if("Grande Capital/Interior".equals(tipoLocalizacaoOrigem.getDsTipoLocalizacaoMunicipio().getValue())){
					
					toReturn.add(findRegiaoDestino("Grande Capital", tabelaPreco, uf, tipoLocalizacaoMunicipioGrandeCapital, criteria, destinosPropostas));
					
					toReturn.add(findRegiaoDestino("Interior", tabelaPreco, uf, tipoLocalizacaoMunicipioInterior, criteria, destinosPropostas));
					
				/*Capital/Interiores*/
				}else if("Capital/Interiores".equals(tipoLocalizacaoOrigem.getDsTipoLocalizacaoMunicipio().getValue())){
					
					/*Obtem Região*/
					
					toReturn.add(findRegiaoDestino("Capital", tabelaPreco, uf, tipoLocalizacaoMunicipioCapital, criteria, destinosPropostas));
					
					/*Obtem os grupos regiões*/
					toReturn.addAll(populateGruporegiao(tabelaPreco, uf, criteria));

				/*Grande Capital/Interiores*/	
				}else if("Grande Capital/Interiores".equals(tipoLocalizacaoOrigem.getDsTipoLocalizacaoMunicipio().getValue())){

					/*Obtem Região*/
					toReturn.add(findRegiaoDestino("Grande Capital/Interiores", tabelaPreco, uf, tipoLocalizacaoMunicipioGrandeCapital, criteria, destinosPropostas));
					
					/*Obtem os grupos regiões*/
					toReturn.addAll(populateGruporegiao(tabelaPreco, uf, criteria));
				
				/*Grupo Região*/	
				}else if("Interiores".equals(tipoLocalizacaoOrigem.getDsTipoLocalizacaoMunicipio().getValue())){
					
					/*Obtem os grupos regiões*/
					toReturn.addAll(populateGruporegiao(tabelaPreco, uf, criteria));
				
				/*Interior*/
				}else{
					
					toReturn.add(findRegiaoDestino("Interior", tabelaPreco, uf, tipoLocalizacaoMunicipioInterior, criteria, destinosPropostas));					
				}
				
			}/*for*/
		}
		
		return toReturn;
	}
	
	public Map<String, Object> populateDestinoProposta(DestinoProposta destinoProposta) {
		Map<String, Object> field = new HashMap<String, Object>();
		field.put("idDestinoProposta", destinoProposta.getIdDestinoProposta());
		field.put("tpIndicadorFreteMinimo", destinoProposta.getTpIndicadorFreteMinimo().getValue());
		field.put("vlFreteMinimo", destinoProposta.getVlFreteMinimo());
		field.put("tpIndicadorFretePeso", destinoProposta.getTpIndicadorFretePeso().getValue());
		field.put("vlFretePeso", destinoProposta.getVlFretePeso());
		field.put("tpIndicadorAdvalorem", destinoProposta.getTpIndicadorAdvalorem().getValue());
		field.put("vlAdvalorem", destinoProposta.getVlAdvalorem());
		field.put("pcDiferencaFretePeso", destinoProposta.getPcDiferencaFretePeso());
		field.put("pcDiferencaAdvalorem", destinoProposta.getPcDiferencaAdvalorem());
		field.put("tpDiferencaAdvalorem", destinoProposta.getTpDiferencaAdvalorem().getValue());
		field.put("idUnidadeFederativaDestino", destinoProposta.getUnidadeFederativa().getIdUnidadeFederativa());
		
		if(destinoProposta.getTipoLocalizacaoMunicipio() != null){
		field.put("idTipoLocalizacaoMunicipioDestino", destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio());
		}
		
		if(destinoProposta.getGrupoRegiao() != null){
			field.put("idGrupoRegiao", destinoProposta.getGrupoRegiao().getIdGrupoRegiao());
		}
		
		field.put("isSelected", Boolean.TRUE);
		return field;
	}
	
	/**
	 * Obtem dados região destino
	 * 
	 * @param tabelaPreco
	 * @param uf
	 * @param tipoLocalizacaoMunicipio
	 * @param criteria
	 * @param destinosPropostas
	 * @return
	 */
	private Map<String, Object> findRegiaoDestino(String descricao, TabelaPreco tabelaPreco, UnidadeFederativa uf, TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio, TypedFlatMap criteria, List<DestinoProposta> destinosPropostas){

		Map<String, Object> field = verifyDestinoProposta(destinosPropostas, uf.getIdUnidadeFederativa(), tipoLocalizacaoMunicipio.getIdTipoLocalizacaoMunicipio());
		field.put("siglaDescricao", uf.getSgUnidadeFederativa().concat(" - ").concat(descricao));
		
		if(BigDecimalUtils.isZero(criteria.getBigDecimal("pcDiferencaFretePeso")) && BigDecimalUtils.isZero(criteria.getBigDecimal("pcDiferencaAdvalorem"))){
			
			DiferencaCapitalInterior dci = diferencaCapitalInteriorService.findPercCapitalInterior(uf.getIdUnidadeFederativa());
			
			field.put("pcDiferencaFretePeso", dci == null ? BigDecimal.ZERO : dci.getPcDiferencaPadrao());
			field.put("pcDiferencaAdvalorem", dci == null ? BigDecimal.ZERO : dci.getPcDiferencaPadraoAdvalorem());
			}else{
			field.put("pcDiferencaFretePeso", criteria.getBigDecimal("pcDiferencaFretePeso"));
			field.put("pcDiferencaAdvalorem", criteria.getBigDecimal("pcDiferencaAdvalorem"));
			}
			
		field.put("tpDiferencaAdvalorem", criteria.getString("tpDiferencaAdvalorem"));

		return field;
	}
	
	/**
	 * Verifica se existe algum destinoProposta salvo, carregando seus dados
	 * @param destinosPropostas
	 * @param idUnidadeFederativa
	 * @param idTipoLocalizacaoMunicipio
	 * @return
	 */
	private Map<String, Object> verifyDestinoProposta(List<DestinoProposta> destinosPropostas, Long idUnidadeFederativa, Long idTipoLocalizacaoMunicipio) {
		for (DestinoProposta destinoProposta : destinosPropostas) {
			/** Se UF e TipoLocalizacao conincidirem, registro deve ser carregado */
			if(destinoProposta.getUnidadeFederativa().getIdUnidadeFederativa().equals(idUnidadeFederativa) 
				&& destinoProposta.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio().equals(idTipoLocalizacaoMunicipio)) {
				return this.populateDestinoProposta(destinoProposta);
			}
		}
		Map<String, Object> field = new HashMap<String, Object>();
		field.put("idUnidadeFederativaDestino", idUnidadeFederativa);
		field.put("idTipoLocalizacaoMunicipioDestino", idTipoLocalizacaoMunicipio);
		return field;
	}
	
	public List findDestinosPropostaConvencionalAereo(Map criteria){
	    Long idSimulacao = LongUtils.getLong(criteria.get("idSimulacao"));
	    
	    Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
        Simulacao simulacao = simulacaoService.findById(idSimulacao);
        if (proposta != null){
            criteria.put("idProposta", proposta.getIdProposta());
        }
        criteria.put("idTabelaPreco", simulacao.getTabelaPreco().getIdTabelaPreco());
        if (simulacao.getProdutoEspecifico()!= null){
            criteria.put("idProdutoEspecifico", simulacao.getProdutoEspecifico().getIdProdutoEspecifico());
        }
        
        if (criteria.get("idAeroportoOrigem") == null){
            Aeroporto aeroporto = aeroportoService.findAeroportoAtendeCliente(simulacao.getClienteByIdCliente().getIdCliente());
            if (aeroporto!=null){
                criteria.put("idAeroportoOrigem", aeroporto.getIdAeroporto());
            }
        }
        
        criteria.put("tpGeracaoProposta", simulacao.getTpGeracaoProposta().getValue());
        
        List retorno = getDestinoPropostaDAO().findDestinosPropostaConvencionalAereo(criteria);
        
        if ((retorno == null || retorno.isEmpty()) && Boolean.FALSE.equals(MapUtilsPlus.getBoolean(criteria, "isGenerate"))) {
        	criteria.put("isGenerate", true);
        	retorno = getDestinoPropostaDAO().findDestinosPropostaConvencionalAereo(criteria);
        }
        
        return retorno;
	}
	
	/**
	 * Monta  a linha de grupos regiões através da tabela preco e unidade federativa
	 * 
	 * @param tabelaPreco
	 * @param unidadeFederativa
	 * @return List
	 */
	public List<Map<String, Object>> populateGruporegiao(TabelaPreco tabelaPreco , UnidadeFederativa unidadeFederativa, TypedFlatMap criteria){
		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
		
		List<GrupoRegiao> grupoRegiaoList = grupoRegiaoService.findGruposRegiao(tabelaPreco.getIdTabelaPreco(),unidadeFederativa.getIdUnidadeFederativa());
		
		Map<String, Object> field = null;
		
		if(CollectionUtils.isNotEmpty(grupoRegiaoList)){
			for (GrupoRegiao gr : grupoRegiaoList) {
				
				field = new HashMap<String, Object>();
				field.put("siglaDescricao", unidadeFederativa.getSgUnidadeFederativa().concat(" - ").concat(gr.getDsGrupoRegiao()));
				field.put("pcFretePercentual", BigDecimalUtils.defaultBigDecimal(criteria.getBigDecimal("pcFretePercentual")));					
				field.put("vlMinimoFretePercentual", BigDecimalUtils.defaultBigDecimal(criteria.getBigDecimal("vlMinimoFretePercentual")));						
				field.put("vlToneladaFretePercentual", BigDecimalUtils.defaultBigDecimal(criteria.getBigDecimal("vlToneladaFretePercentual")));						
				field.put("psFretePercentual", BigDecimalUtils.defaultBigDecimal(criteria.getBigDecimal("psFretePercentual")));						
				field.put("idGrupoRegiao", gr.getIdGrupoRegiao());
				field.put("idUnidadeFederativaDestino", unidadeFederativa.getIdUnidadeFederativa());

				if(BigDecimalUtils.hasValue(gr.getVlAjustePadrao())){				
					if("D".equals(gr.getTpAjuste().getValue())){
						field.put("pcDiferencaFretePeso", gr.getVlAjustePadrao().multiply(new BigDecimal(-1)));
				}else{
						field.put("pcDiferencaFretePeso", gr.getVlAjustePadrao());
					}
				}else{
					field.put("pcDiferencaFretePeso", BigDecimal.ZERO);									
				}
				
				if(BigDecimalUtils.hasValue(gr.getVlAjustePadraoAdvalorem())){				
					if("D".equals(gr.getTpAjusteAdvalorem().getValue())){
						field.put("pcDiferencaAdvalorem", gr.getVlAjustePadraoAdvalorem().multiply(new BigDecimal(-1)));	
					}
					else{
						field.put("pcDiferencaAdvalorem", gr.getVlAjustePadraoAdvalorem());
					}						
				}else{
					field.put("pcDiferencaAdvalorem", BigDecimal.ZERO);									
				}
				
				field.put("tpDiferencaAdvalorem", gr.getTpValorAjusteAdvalorem().getValue());	
				
								
				toReturn.add(field);
			}
		}
		
		return toReturn;
	}
	
	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}

	public void setTipoLocalizacaoMunicipioService(TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}

	public void setPrecoFreteService(PrecoFreteService precoFreteService) {
		this.precoFreteService = precoFreteService;
	}

	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}

	public void setPropostaService(PropostaService propostaService) {
		this.propostaService = propostaService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setTabelaPrecoParcelaService(TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	public void setDiferencaCapitalInteriorService(DiferencaCapitalInteriorService diferencaCapitalInteriorService) {
		this.diferencaCapitalInteriorService = diferencaCapitalInteriorService;
	}
	
	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}

    public void setAeroportoService(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }

    public void setTaxaClienteService(TaxaClienteService taxaClienteService) {
        this.taxaClienteService = taxaClienteService;
    }

    public List<Map<String, Object>> findParcelasDestinoConvencionalAereo(Map criteria) {
        return getDestinoPropostaDAO().findParcelasDestinoConvencionalAereo(criteria);
    }

    public void setFaixaProgressivaPropostaService(FaixaProgressivaPropostaService faixaProgressivaPropostaService) {
        this.faixaProgressivaPropostaService = faixaProgressivaPropostaService;
    }

    public void setValorFaixaProgressivaPropostaService(ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService) {
        this.valorFaixaProgressivaPropostaService = valorFaixaProgressivaPropostaService;
    }
}