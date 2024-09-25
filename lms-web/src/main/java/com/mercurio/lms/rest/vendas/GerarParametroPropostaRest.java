package com.mercurio.lms.rest.vendas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.adsm.core.util.VarcharI18nUtil;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.RegiaoGeografica;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.RegiaoGeograficaService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.rest.municipios.TipoLocalizacaoMunicipioDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.tabeladeprecos.ProdutoEspecificoDTO;
import com.mercurio.lms.rest.tabeladeprecos.ServicoSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.DestinoPropostaDTO;
import com.mercurio.lms.rest.vendas.dto.DestinoPropostaParcelaDTO;
import com.mercurio.lms.rest.vendas.dto.DestinoPropostaRegiaoDTO;
import com.mercurio.lms.rest.vendas.dto.DivisaoClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.GeracaoParametroPropostaDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaDestinoAereoColunaDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaDestinoAereoTableDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaDestinoTableDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaFiltroDTO;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.FaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.service.DestinoPropostaService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.PropostaService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.model.service.ValorFaixaProgressivaPropostaService;

@Path("/vendas/gerarParametrosProposta")
public class GerarParametroPropostaRest extends BaseCrudRest<GeracaoParametroPropostaDTO, GeracaoParametroPropostaDTO, ParametroPropostaFiltroDTO> {
    
    private static final String TP_GERACAO_CONVENCIONAL = "V";
    private static final String TP_GERACAO_MINIMO_EXCEDENTE = "M";
    private static final String TP_GERACAO_MINIMO_POR_KILO = "I";
    
    private static final String CD_TARIFA_MINIMA = "IDTarifaMinima";
    private static final String CD_ADVALOREM1 = "IDAdvalorem1";
    private static final String CD_ADVALOREM2 = "IDAdvalorem2";
    private static final String CD_FRETE_PESO = "IDFretePeso";
    private static final String CD_FRETE_QUILO = "IDFreteQuilo";

    private static final byte ORDER_FAIXA_PROGRESSIVA = 1;
    private static final byte ORDER_FRETE_QUILO = 2;
    private static final byte ORDER_PRODUTO_ESPECIFICO = 3;
    private static final byte ORDER_TARIFA_MINIMA = 4;
    private static final byte ORDER_AD_VALOREM_1 = 5;
    private static final byte ORDER_AD_VALOREM_2 = 6;
    private static final byte ORDER_TAXAS = 7;
	
	@InjectInJersey
	private SimulacaoService simulacaoService;
	
	@InjectInJersey
	private DestinoPropostaService destinoPropostaService;
	
	@InjectInJersey
	private UnidadeFederativaService unidadeFederativaService;
	
	@InjectInJersey
	private RegiaoGeograficaService regiaoGeograficaService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	
	@InjectInJersey
	private DomainService domainService;
	
	@InjectInJersey
	private PropostaService propostaService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	private DomainValueService domainValueService;

	@InjectInJersey
	private ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService;
	
	@InjectInJersey
	private AeroportoService aeroportoService;
	
	@InjectInJersey
	private TabelaPrecoService tabelaPrecoService;
	
	@InjectInJersey
	private PessoaService pessoaService;
	
	@InjectInJersey
	private ServicoService servicoService;
	
	@InjectInJersey
	private DivisaoClienteService divisaoClienteService;
	
    @Override
    protected GeracaoParametroPropostaDTO findById(Long id) {
        GeracaoParametroPropostaDTO dto = new GeracaoParametroPropostaDTO();
        
        UnidadeFederativa uf = unidadeFederativaService.findByIdPessoa(SessionUtils.getFilialSessao().getPessoa().getIdPessoa());
        dto.setUfOrigem(new UnidadeFederativaDTO(uf.getIdUnidadeFederativa(), uf.getSgUnidadeFederativa(), uf.getNmUnidadeFederativa()));
        
        return dto;
    }

    @Override
    protected Long store(GeracaoParametroPropostaDTO bean) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void removeById(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void removeByIds(List<Long> ids) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected List<GeracaoParametroPropostaDTO> find(ParametroPropostaFiltroDTO filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Integer count(ParametroPropostaFiltroDTO filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @POST
	@Path("/storeDestinosProposta")
	public Response storeDestinosProposta(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO) {
		
		this.validarListDestinos(geracaoParametroPropostaDTO.getListDestinosProposta());
		
		Proposta proposta = new Proposta();
		Long idProposta = geracaoParametroPropostaDTO.getIdProposta();
		if(LongUtils.hasValue(idProposta)) {
			proposta = propostaService.findById(idProposta);
		}

		proposta.setTpIndicadorMinFretePeso(geracaoParametroPropostaDTO.getTpIndicadorMinFretePeso());
		proposta.setVlMinFretePeso(geracaoParametroPropostaDTO.getVlMinFretePeso());
		proposta.setTpIndicadorFreteMinimo(geracaoParametroPropostaDTO.getTpIndicadorFreteMinimo());
		proposta.setVlFreteMinimo(geracaoParametroPropostaDTO.getVlFreteMinimo());
		proposta.setTpIndicadorFretePeso(geracaoParametroPropostaDTO.getTpIndicadorFretePeso());
		proposta.setVlFretePeso(geracaoParametroPropostaDTO.getVlFretePeso());
		proposta.setTpIndicadorAdvalorem(geracaoParametroPropostaDTO.getTpIndicadorAdvalorem());
		proposta.setVlAdvalorem(geracaoParametroPropostaDTO.getVlAdvalorem());
		proposta.setPcDiferencaFretePeso(geracaoParametroPropostaDTO.getPcDiferencaFretePeso());
		proposta.setPcDiferencaAdvalorem(geracaoParametroPropostaDTO.getPcDiferencaAdvalorem());
		proposta.setBlPagaPesoExcedente(geracaoParametroPropostaDTO.isBlPagaPesoExcedente());
		proposta.setBlFreteExpedido(geracaoParametroPropostaDTO.isBlFreteExpedido());
		proposta.setBlFreteRecebido(geracaoParametroPropostaDTO.isBlFreteRecebido());

		/*Solicitacao para o branch RPP - 01.04.01.07*/
		proposta.setBlPagaCubagem(Boolean.TRUE);
		proposta.setPcPagaCubagem(BigDecimalUtils.HUNDRED);						
		
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		unidadeFederativa.setIdUnidadeFederativa(geracaoParametroPropostaDTO.getUfOrigem().getIdUnidadeFederativa());
		proposta.setUnidadeFederativaByIdUfOrigem(unidadeFederativa);

		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio = new TipoLocalizacaoMunicipio();
		tipoLocalizacaoMunicipio.setIdTipoLocalizacaoMunicipio(geracaoParametroPropostaDTO.getTipoLocalizacaoMunicipioOrigem().getIdTipoLocalizacaoMunicipio());
		proposta.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(tipoLocalizacaoMunicipio);

		Simulacao simulacao = simulacaoService.findById(geracaoParametroPropostaDTO.getIdSimulacao());
		if(geracaoParametroPropostaDTO.getTpDiferencaAdvalorem() != null){
			simulacao.setTpDiferencaAdvalorem(geracaoParametroPropostaDTO.getTpDiferencaAdvalorem());
			simulacaoService.store(simulacao);
		}		
		
		proposta.setSimulacao(simulacao);

		/** Gera destinos */
		this.storeDestinosProposta(proposta, geracaoParametroPropostaDTO.getListDestinosProposta());

		return Response.ok(proposta.getIdProposta()).build();
	}
	
	private void storeDestinosProposta(Proposta proposta, List<ParametroPropostaDestinoTableDTO> destinosProposta) {
		
		List<DestinoProposta> listDestinoPropostaStore = new ArrayList<DestinoProposta>();
		
		List<Long> removeIds = new ArrayList<Long>();
		for (ParametroPropostaDestinoTableDTO destino : destinosProposta) {
			Long idDestinoProposta = destino.getIdDestinoProposta();
			/** Salva itens que foram selecionados */
			if(destino.getChecked() != null && destino.getChecked()) {
				
				DestinoProposta destinoProposta = new DestinoProposta();
				destinoProposta.setIdDestinoProposta(idDestinoProposta);
				if(destino.getTpIndicadorFreteMinimo() == null 
						|| destino.getTpIndicadorFreteMinimo().getValue() == null
						|| ("").equals(destino.getTpIndicadorFreteMinimo().getValue())) {					
					throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("percentualMinimo")});
				} else {
					destinoProposta.setTpIndicadorFreteMinimo(destino.getTpIndicadorFreteMinimo());
				}
				destinoProposta.setVlFreteMinimo(destino.getVlFreteMinimo());
				if(destino.getTpIndicadorFretePeso() == null 
						|| destino.getTpIndicadorFretePeso().getValue() == null
						|| ("").equals(destino.getTpIndicadorFretePeso().getValue())) {
					throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("fretePeso")});
				} else {
					destinoProposta.setTpIndicadorFretePeso(destino.getTpIndicadorFretePeso());
				}
				destinoProposta.setVlFretePeso(destino.getVlFretePeso());
				if(destino.getTpIndicadorAdvalorem() == null 
						|| destino.getTpIndicadorAdvalorem().getValue() == null
						|| ("").equals(destino.getTpIndicadorAdvalorem().getValue())){
					throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("adValorem")});
				} else {
					destinoProposta.setTpIndicadorAdvalorem(destino.getTpIndicadorAdvalorem());
				}
				destinoProposta.setVlAdvalorem(destino.getVlAdvalorem());
				destinoProposta.setPcDiferencaFretePeso(BigDecimalUtils.defaultBigDecimal(destino.getPcDiferencaFretePeso()));
				destinoProposta.setPcDiferencaAdvalorem(destino.getPcDiferencaAdvalorem());
				if(destino.getTpDiferencaAdvalorem() == null 
						|| destino.getTpDiferencaAdvalorem().getValue() == null
						|| ("").equals(destino.getTpDiferencaAdvalorem().getValue())){
					throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("tipoDiferenca")});
				} else {
					destinoProposta.setTpDiferencaAdvalorem(destino.getTpDiferencaAdvalorem());
				}

				if (destinoProposta.getTpDiferencaAdvalorem() != null && "T".equals(destinoProposta.getTpDiferencaAdvalorem().getValue())){
					BigDecimal vlAdValorem = destinoProposta.getVlAdvalorem().add(destinoProposta.getPcDiferencaAdvalorem());
					if (BigDecimalUtils.ltZero(vlAdValorem)){
						throw new BusinessException("LMS-30064");
					}
				}

				UnidadeFederativa unidadeFederativaDestino = new UnidadeFederativa();
				unidadeFederativaDestino.setIdUnidadeFederativa(destino.getIdUnidadeFederativaDestino());
				destinoProposta.setUnidadeFederativa(unidadeFederativaDestino);

				if(destino.getIdTipoLocalizacaoMunicipioDestino() != null){
					TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino = new TipoLocalizacaoMunicipio();
					tipoLocalizacaoMunicipioDestino.setIdTipoLocalizacaoMunicipio(destino.getIdTipoLocalizacaoMunicipioDestino());
					destinoProposta.setTipoLocalizacaoMunicipio(tipoLocalizacaoMunicipioDestino);
				}

				if(destino.getIdGrupoRegiao() != null){
					GrupoRegiao grupoRegiao = new GrupoRegiao();
					grupoRegiao.setIdGrupoRegiao(destino.getIdGrupoRegiao());
					destinoProposta.setGrupoRegiao(grupoRegiao);
				}
				
				destinoProposta.setProposta(proposta);
				listDestinoPropostaStore.add(destinoProposta);

			/** Se não esta selecionado e possui ID, item deve ser excluido */
			} else if(LongUtils.hasValue(idDestinoProposta)) {
				removeIds.add(idDestinoProposta);
			}
		}
		
		this.destinoPropostaService.storeDestinosProposta(proposta, listDestinoPropostaStore, removeIds);
	}
	
	private void validarListDestinos(List<ParametroPropostaDestinoTableDTO> listDestinosProposta) {
		for (ParametroPropostaDestinoTableDTO destino : listDestinosProposta) {
			if(destino.getChecked() != null && destino.getChecked()){
				validateCamposObrigatorios(destino);
				validateTpPercentualMinimo(destino);
				validateTpFretePeso(destino);
				validateTpAdvalorem(destino);
			}
		}
	}
	
	private void validateCamposObrigatorios(ParametroPropostaDestinoTableDTO destino) {
		
		// % minimo 
		if(destino.getTpIndicadorFreteMinimo() == null 
				|| destino.getTpIndicadorFreteMinimo().getValue() == null
				|| ("").equals(destino.getTpIndicadorFreteMinimo().getValue())
				|| destino.getVlFreteMinimo() == null) {					
			throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("percentualMinimo")});
		}
		
		// frete peso 
		if(destino.getTpIndicadorFretePeso() == null 
				|| destino.getTpIndicadorFretePeso().getValue() == null 
				|| ("").equals(destino.getTpIndicadorFretePeso().getValue())
				|| destino.getVlFretePeso() == null) {					
			throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("fretePeso")});
		} 
		
		// advalorem 
		if(destino.getTpIndicadorAdvalorem() == null 
				|| destino.getTpIndicadorAdvalorem().getValue() == null
				|| ("").equals(destino.getTpIndicadorAdvalorem().getValue())
				|| destino.getVlAdvalorem() == null) {					
			throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("adValorem")});
		}
		
		// tipo diferenca 
		if(destino.getTpDiferencaAdvalorem() == null 
				|| destino.getTpDiferencaAdvalorem().getValue() == null
				|| ("").equals(destino.getTpDiferencaAdvalorem().getValue())) {					
			throw new BusinessException("LMS-01264", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("tipoDiferenca")});
		}
	}

	private void validateTpPercentualMinimo(ParametroPropostaDestinoTableDTO destino) {
		validateVlPercentualMinimo(destino);
	}
	
	private void validateVlPercentualMinimo(ParametroPropostaDestinoTableDTO destino) {
		DomainValue tpPercentualMinimo = destino.getTpIndicadorFreteMinimo();
		BigDecimal vlFreteMinimo = destino.getVlFreteMinimo();
		if(BigDecimalUtils.ltZero(vlFreteMinimo)) {
			throw new BusinessException("LMS-01265", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("percentualMinimo")});
		}
		if(isDesconto(tpPercentualMinimo)) {
			if(!isPercent(vlFreteMinimo)) {
				throw new BusinessException("LMS-01265", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("percentualMinimo")});
			}
		}
	}
	
	private void validateTpFretePeso(ParametroPropostaDestinoTableDTO destino) {
		validateVlFretePeso(destino);
		DomainValue tpFretePeso =  destino.getTpIndicadorFretePeso();
		if(isValor(tpFretePeso)) {
			DomainValue tpIndicadorFreteMinimo = destino.getTpIndicadorFreteMinimo();
			BigDecimal vlFreteMinimo = destino.getVlFreteMinimo();
			if( !(isDesconto(tpIndicadorFreteMinimo) && BigDecimalUtils.isZero(vlFreteMinimo)) ) {
				throw new BusinessException("LMS-01040", new Object[]{configuracoesFacade.getMensagem("tpIndicadorFretePeso")});
			}
		}
		validateVlFretePeso(destino);
	}
	
	private void validateVlFretePeso(ParametroPropostaDestinoTableDTO destino) {
		DomainValue tpFretePeso =  destino.getTpIndicadorFretePeso();
		BigDecimal vlFretePeso = destino.getVlFretePeso();
		if(isDesconto(tpFretePeso)) {
			if(!isPercent(vlFretePeso)) {
				throw new BusinessException("LMS-01265", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("tpIndicadorFretePeso")});
			}
		} else if((isAcrecimo(tpFretePeso) || isValor(tpFretePeso)) && BigDecimalUtils.ltZero(vlFretePeso)) {
			throw new BusinessException("LMS-01265", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("tpIndicadorFretePeso")});
		}
	}
	
	private void validateTpAdvalorem(ParametroPropostaDestinoTableDTO destino) {
		validateVlAdvalorem(destino);
	}
	
	private void validateVlAdvalorem(ParametroPropostaDestinoTableDTO destino) {
		DomainValue tpAdvalorem =  destino.getTpIndicadorAdvalorem();
		BigDecimal vlAdvalorem = destino.getVlAdvalorem();
		if(isDesconto(tpAdvalorem)) {
			if(!isPercent(vlAdvalorem)) {
				throw new BusinessException("LMS-01265", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("tpIndicadorAdvalorem")});
			}
		} else if((isAcrecimo(tpAdvalorem) || isValor(tpAdvalorem)) && BigDecimalUtils.ltZero(vlAdvalorem)) {
			throw new BusinessException("LMS-01265", new Object[]{destino.getSiglaDescricao(), configuracoesFacade.getMensagem("tpIndicadorAdvalorem")});
		}
	}
	
	private boolean isDesconto(DomainValue indicador) {
		return ("D".equals(indicador.getValue()));
	}
	
	private boolean isValor(DomainValue indicador) {
		return ("V".equals(indicador.getValue()));
	}
	
	private boolean isAcrecimo(DomainValue indicador) {
		return ("A".equals(indicador.getValue()));
	}
	
	private boolean isPercent(BigDecimal valor) {
		return !( (BigDecimal.ZERO.compareTo(valor) > 0) || (BigDecimalUtils.HUNDRED.compareTo(valor) < 0) );
	}
	
	
	@POST
	@SuppressWarnings("rawtypes")
	@Path("findDominioGenericoCombo")
	public Map findDominioGenericoCombo(Map<String, Object> criteria) {
		Domain domain = domainService.findByName((String)criteria.get("nmDomain"));
		TypedFlatMap retorno = new TypedFlatMap();
		
		List<Map<String, Object>> listaValores = new ArrayList<Map<String, Object>>();
		for (Object domainValueObject : domain.getDomainValues()) {
			DomainValue domainValue = (DomainValue) domainValueObject;
			Map<String, Object> domainValueMap = new HashMap<String, Object>();
			domainValueMap.put("value", domainValue.getValue());
			domainValueMap.put("description", domainValue.getDescriptionAsString());
			domainValueMap.put("descriptionAsString", domainValue.getDescriptionAsString());
			listaValores.add(domainValueMap);
		}
		
		retorno.put("dominioGenerico", listaValores);
		return retorno;
	}
	
	@POST
	@Path("storeDestinosAereo")
	public Response storeDestinosAereo(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO) {
		List<DestinoProposta> listaDestinoProposta = convertToDestinoProposta(geracaoParametroPropostaDTO.getListaParametroPropostaDestinoAereoTableDTO()); 
		destinoPropostaService.storeDestinosAereo(geracaoParametroPropostaDTO.getId(), listaDestinoProposta);
		
		return Response.ok().build();
	}
	
	private List<DestinoProposta> convertToDestinoProposta(List<ParametroPropostaDestinoAereoTableDTO> listaParametroPropostaDestinoAereoTableDTO){
		List<DestinoProposta> listaDestinoProposta = new ArrayList<DestinoProposta>();
		
		for (ParametroPropostaDestinoAereoTableDTO parametroPropostaDestinoAereoTableDTO : listaParametroPropostaDestinoAereoTableDTO) {
			
			if(parametroPropostaDestinoAereoTableDTO.getChecked() != null && parametroPropostaDestinoAereoTableDTO.getChecked()){
			DestinoProposta destinoProposta = new DestinoProposta();
			destinoProposta.setIdDestinoProposta(parametroPropostaDestinoAereoTableDTO.getIdDestinoProposta());
			
			Proposta proposta = new Proposta();
			proposta.setIdProposta(parametroPropostaDestinoAereoTableDTO.getIdProposta());
			destinoProposta.setProposta(proposta);
			
			UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
			unidadeFederativa.setIdUnidadeFederativa(parametroPropostaDestinoAereoTableDTO.getIdUnidadeFederativa()); 
			destinoProposta.setUnidadeFederativa(unidadeFederativa);

			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(parametroPropostaDestinoAereoTableDTO.getIdAeroporto());
			destinoProposta.setAeroportoByIdAeroporto(aeroporto);
			destinoProposta.setRotaPreco(new RotaPreco(parametroPropostaDestinoAereoTableDTO.getIdRotaPreco()));
			
			// Coluna Valor Mínimo
			ParametroPropostaDestinoAereoColunaDTO colunaValorMinimo = parametroPropostaDestinoAereoTableDTO.getColunaValorMinimo();
			destinoProposta.setTpIndicadorFreteMinimo(colunaValorMinimo.getTpIndicador());
			destinoProposta.setVlFreteMinimo(colunaValorMinimo.getValor());
			
			// Coluna Valor por Kg
			ParametroPropostaDestinoAereoColunaDTO colunaValoPorKg = parametroPropostaDestinoAereoTableDTO.getColunaValoPorKg();
			destinoProposta.setTpIndicadorFretePeso(colunaValoPorKg.getTpIndicador());
			destinoProposta.setVlFretePeso(colunaValoPorKg.getValor());
			destinoProposta.setPcDiferencaFretePeso(colunaValoPorKg.getPercentual());
			
			// Coluna Peso Mínimo
			ParametroPropostaDestinoAereoColunaDTO colunaPesoMinimo = parametroPropostaDestinoAereoTableDTO.getColunaPesoMinimo();
			destinoProposta.setPsMinimoFretePeso(colunaPesoMinimo.getValor());
			
			// Coluna Produto Específico
			if(parametroPropostaDestinoAereoTableDTO.getColunaProdutoEspecifico() != null){
				ParametroPropostaDestinoAereoColunaDTO colunaProdutoEspecifico = parametroPropostaDestinoAereoTableDTO.getColunaProdutoEspecifico();
				destinoProposta.setTpIndicadorProdutoEspecifico(colunaProdutoEspecifico.getTpIndicador());
				destinoProposta.setVlProdutoEspecifico(colunaProdutoEspecifico.getValor());
				destinoProposta.setPcDiferencaProdutoEspecifico(colunaProdutoEspecifico.getPercentual());
			}
			
			// Coluna Advalorem 1
			ParametroPropostaDestinoAereoColunaDTO colunaAdValorem1 = parametroPropostaDestinoAereoTableDTO.getColunaAdValorem1();
			destinoProposta.setTpIndicadorAdvalorem(colunaAdValorem1.getTpIndicador());
			destinoProposta.setVlAdvalorem(colunaAdValorem1.getValor());
			destinoProposta.setPcDiferencaAdvalorem(colunaAdValorem1.getPercentual());
			destinoProposta.setTpDiferencaAdvalorem(new DomainValue("P"));
			
			// Coluna Advalorem 2
			ParametroPropostaDestinoAereoColunaDTO colunaAdValorem2 = parametroPropostaDestinoAereoTableDTO.getColunaAdValorem2();
			destinoProposta.setTpIndicadorAdvalorem2(colunaAdValorem2.getTpIndicador());
				destinoProposta.setVlAdvalorem2(colunaAdValorem2.getValor());
			
			// Coluna Taxa coleta urbana convencional
			ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbConv = parametroPropostaDestinoAereoTableDTO.getColunaTxColUrbConv();
			destinoProposta.setTpIndicadorTaxaColetaUrbanaConvencional(colunaTxColUrbConv.getTpIndicador());
			destinoProposta.setVlTaxaColetaUrbanaConvencional(colunaTxColUrbConv.getValor());
				destinoProposta.setPsMinimoTaxaColetaUrbanaConvencional(colunaTxColUrbConv.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaColetaUrbanaConvencional(colunaTxColUrbConv.getVlExcedente());
			
			// Coluna Taxa coleta interior convencional
			ParametroPropostaDestinoAereoColunaDTO colunaTxColIntConv = parametroPropostaDestinoAereoTableDTO.getColunaTxColIntConv();
			destinoProposta.setTpIndicadorTaxaColetaInteriorConvencial(colunaTxColIntConv.getTpIndicador());
			destinoProposta.setVlTaxaColetaInteriorConvencional(colunaTxColIntConv.getValor());
				destinoProposta.setPsMinimoTaxaColetaInteriorConvencional(colunaTxColIntConv.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaColetaInteriorConvencional(colunaTxColIntConv.getVlExcedente());
			
			// Coluna Taxa coleta urbana emergencial
			ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbEm = parametroPropostaDestinoAereoTableDTO.getColunaTxColUrbEm();
			destinoProposta.setTpIndicadorTaxaColetaUrbanaEmergencial(colunaTxColUrbEm.getTpIndicador());
			destinoProposta.setVlTaxaColetaUrbanaEmergencial(colunaTxColUrbEm.getValor());
				destinoProposta.setPsMinimoTaxaColetaUrbanaEmergencial(colunaTxColUrbEm.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaColetaUrbanaEmergencial(colunaTxColUrbEm.getVlExcedente());
			
			// Coluna Taxa coleta interior emergencial
			ParametroPropostaDestinoAereoColunaDTO colunaTxColIntEm = parametroPropostaDestinoAereoTableDTO.getColunaTxColIntEm();
			destinoProposta.setTpIndicadorTaxaColetaInteriorEmergencial(colunaTxColIntEm.getTpIndicador());
			destinoProposta.setVlTaxaColetaInteriorEmergencial(colunaTxColIntEm.getValor());
				destinoProposta.setPsMinimoTaxaColetaInteriorEmergencial(colunaTxColIntEm.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaColetaInteriorEmergencial(colunaTxColIntEm.getVlExcedente());
			
			// Coluna Taxa entrega urbana convencional
			ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbConv = parametroPropostaDestinoAereoTableDTO.getColunaTxEntUrbConv();
			destinoProposta.setTpIndicadorTaxaEntregaUrbanaConvencional(colunaTxEntUrbConv.getTpIndicador());
			destinoProposta.setVlTaxaEntregaUrbanaConvencional(colunaTxEntUrbConv.getValor());
				destinoProposta.setPsMinimoTaxaEntregaUrbanaConvencional(colunaTxEntUrbConv.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaEntregaUrbanaConvencional(colunaTxEntUrbConv.getVlExcedente());
			
			// Coluna Taxa entrega interior convencional
			ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntConv = parametroPropostaDestinoAereoTableDTO.getColunaTxEntIntConv();
			destinoProposta.setTpIndicadorTaxaEntregaInteriorConvencional(colunaTxEntIntConv.getTpIndicador());
			destinoProposta.setVlTaxaEntregaInteriorConvencional(colunaTxEntIntConv.getValor());
				destinoProposta.setPsMinimoTaxaEntregaInteriorConvencional(colunaTxEntIntConv.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaEntregaInteriorConvencional(colunaTxEntIntConv.getVlExcedente());
			
			// Coluna Taxa entrega urbana emergencial
			ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbEm = parametroPropostaDestinoAereoTableDTO.getColunaTxEntUrbEm();
			destinoProposta.setTpIndicadorTaxaEntregaUrbanaEmergencial(colunaTxEntUrbEm.getTpIndicador());
			destinoProposta.setVlTaxaEntregaUrbanaEmergencial(colunaTxEntUrbEm.getValor());
				destinoProposta.setPsMinimoTaxaEntregaUrbanaEmergencial(colunaTxEntUrbEm.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaEntregaUrbanaEmergencial(colunaTxEntUrbEm.getVlExcedente());
			
			// Coluna Taxa entrega interior emergencial
			ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntEm = parametroPropostaDestinoAereoTableDTO.getColunaTxEntIntEm();
			destinoProposta.setTpIndicadorTaxaEntregaInteriorEmergencial(colunaTxEntIntEm.getTpIndicador());
			destinoProposta.setVlTaxaEntregaInteriorEmergencial(colunaTxEntIntEm.getValor());			
				destinoProposta.setPsMinimoTaxaEntregaInteriorEmergencial(colunaTxEntIntEm.getPsMinimo());
				destinoProposta.setVlExcedenteTaxaEntregaInteriorEmergencial(colunaTxEntIntEm.getVlExcedente());
			
			listaDestinoProposta.add(destinoProposta);
		}
		}
		
		return listaDestinoProposta;
	}
	
	
	@POST
	@Path("generateDestinosAereo")
	public Response generateDestinosAereo(TypedFlatMap criteria) {
		List<Map<String, Object>> destinosProposta = destinoPropostaService.generateDestinosAereo(criteria);
		return getReturnFind(convertToParametroPropostaDestinoAereoTableDTO(destinosProposta), destinosProposta.size());
	}

	private List<ParametroPropostaDestinoAereoTableDTO> convertToParametroPropostaDestinoAereoTableDTO(List<Map<String, Object>> list) {
		List<ParametroPropostaDestinoAereoTableDTO> listParametroPropostaDestinoAereoTableDTO = new ArrayList<ParametroPropostaDestinoAereoTableDTO>();

		List<DomainValue> domainValuesParametroCliente = domainValueService.findDomainValues("DM_INDICADOR_PARAMETRO_CLIENTE", true);
		
		
		for (Map<String, Object> map : list) {
			ParametroPropostaDestinoAereoTableDTO dto = new ParametroPropostaDestinoAereoTableDTO();
			
			dto.setDsDestino((String) map.get("DS_DESTINO"));
			dto.setIdDestinoProposta((Long) map.get("ID_DESTINO_PROPOSTA"));
			dto.setIdProposta((Long) map.get("ID_PROPOSTA"));
			dto.setIdUnidadeFederativa((Long) map.get("ID_UNIDADE_FEDERATIVA"));
			dto.setIdAeroporto((Long) map.get("ID_AEROPORTO"));
			dto.setIdRotaPreco((Long) map.get("ID_ROTA_PRECO"));
			
			if(dto.getIdDestinoProposta() != null){
				dto.setChecked(true);
			}
			
			buildColunaValorMinimo(map, dto, domainValuesParametroCliente);
			
			buildColunaValorPorKg(map, dto, domainValuesParametroCliente);

			buildColunaPesoMinimo(map, dto);
			
			buildColunaProdutoEspecifico(map, dto,domainValuesParametroCliente);
			
			buildColunasAdValorem(map, dto,domainValuesParametroCliente);
			
            buildColunasTaxasColetaEntrega(map, dto,domainValuesParametroCliente);
            
			listParametroPropostaDestinoAereoTableDTO.add(dto);
		}
		
		return listParametroPropostaDestinoAereoTableDTO;
	}


	private DomainValue getDomainValueFromList(List<DomainValue> domainList, String value){
	    for (DomainValue domainValue : domainList){
	        if (domainValue.getValue().equals(value)){
	            //Cria novo objeto pois o DomainValue retornado da query é um proxy e entra em loop infinito no jersey por referencia circular
	            DomainValue domain = new DomainValue();
	            domain.setValue(domainValue.getValue());
	            domain.setDescription(domainValue.getDescription());
	            return domain;
	        }
	    }
	    return null;
	}
	    
	private void buildColunaPesoMinimo(Map<String, Object> map, ParametroPropostaDestinoAereoTableDTO dto) {
		ParametroPropostaDestinoAereoColunaDTO colunaPesoMinimo = new ParametroPropostaDestinoAereoColunaDTO();
		colunaPesoMinimo.setVlOriginal((BigDecimal)getDefauldValueFromMap(map, "VL_ORIGINAL_PS_MINIMO_FRETE", BigDecimal.ZERO));
		colunaPesoMinimo.setValor((BigDecimal)getDefauldValueFromMap(map, "PS_MINIMO_FRETE_PESO", colunaPesoMinimo.getVlOriginal()));
		colunaPesoMinimo.setTpIndicador(new DomainValue("P", new VarcharI18n("Peso"), true));
		colunaPesoMinimo.setPercentual(BigDecimal.ZERO);
		dto.setColunaPesoMinimo(colunaPesoMinimo);
	}
	
	
	private  ParametroPropostaDestinoAereoColunaDTO populateTaxasDestinoAereoColunaDTO(Map<String, Object> map, List<DomainValue> domainValuesParametroCliente, 
			String vlOrginalKey, String vlKey, String tpIndicadorKey, String psMinimoOriginalKey, String psMinimoKey, String vlExcedenteOriginalKey, String vlExcedenteKey){
		
		ParametroPropostaDestinoAereoColunaDTO parametroPropostaDestinoAereoColunaDTO = new ParametroPropostaDestinoAereoColunaDTO();
		parametroPropostaDestinoAereoColunaDTO.setVlOriginal((BigDecimal)getDefauldValueFromMap(map, vlOrginalKey, BigDecimal.ZERO));
		parametroPropostaDestinoAereoColunaDTO.setValor((BigDecimal)getDefauldValueFromMap(map, vlKey, parametroPropostaDestinoAereoColunaDTO.getVlOriginal()));
		parametroPropostaDestinoAereoColunaDTO.setTpIndicador(getDomainValueFromList(domainValuesParametroCliente, (String)getDefauldValueFromMap(map, tpIndicadorKey, "T")));
		parametroPropostaDestinoAereoColunaDTO.setPercentual(parametroPropostaDestinoAereoColunaDTO.calculaDescontoAcrescimo());
		parametroPropostaDestinoAereoColunaDTO.setPsMinimoOriginal((BigDecimal)getDefauldValueFromMap(map, psMinimoOriginalKey, BigDecimal.ZERO));
		parametroPropostaDestinoAereoColunaDTO.setPsMinimo((BigDecimal)getDefauldValueFromMap(map, psMinimoKey, parametroPropostaDestinoAereoColunaDTO.getPsMinimoOriginal()));
		parametroPropostaDestinoAereoColunaDTO.setVlExcedenteOriginal((BigDecimal)getDefauldValueFromMap(map, vlExcedenteOriginalKey, BigDecimal.ZERO));
		parametroPropostaDestinoAereoColunaDTO.setVlExcedente((BigDecimal)getDefauldValueFromMap(map, vlExcedenteKey, parametroPropostaDestinoAereoColunaDTO.getVlExcedenteOriginal()));
		
		return parametroPropostaDestinoAereoColunaDTO;
	}
	
    private void buildColunasTaxasColetaEntrega(Map<String, Object> map, ParametroPropostaDestinoAereoTableDTO dto, List<DomainValue> domainValuesParametroCliente) {
		ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbConv = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXCOL_URB_CONV", "VL_TX_COL_URB_CONV", "TP_INDIC_TX_COL_URB_CONV", "PS_TAX_ORIG_TXCOL_URB_CONV", "PS_MIN_TX_COL_URB_CONV",
				"VL_EXC_ORIG_TXCOL_URB_CONV", "VL_EXCED_TX_COL_URB_CONV");
        dto.setColunaTxColUrbConv(colunaTxColUrbConv);
        
        ParametroPropostaDestinoAereoColunaDTO colunaTxColIntConv = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXCOL_INT_CONV", "VL_TX_COL_INT_CONV", "TP_INDIC_TX_COL_INT_CONV", "PS_TAX_ORIG_TXCOL_URB_CONV", "PS_MIN_TX_COL_INT_CONV",
				"VL_EXC_ORIG_TXCOL_INT_CONV", "VL_EXCED_TX_COL_INT_CONV");
        dto.setColunaTxColIntConv(colunaTxColIntConv);
        
        ParametroPropostaDestinoAereoColunaDTO colunaTxColUrbEm = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXCOL_URB_EME", "VL_TX_COL_URB_EME", "TP_INDIC_TX_COL_URB_EME", "PS_TAX_ORIG_TXCOL_URB_EME", "PS_MIN_TX_COL_URB_EME",
				"VL_EXC_ORIG_TXCOL_URB_EME", "VL_EXCED_TX_COL_URB_EME"); 
        dto.setColunaTxColUrbEm(colunaTxColUrbEm);
        
        ParametroPropostaDestinoAereoColunaDTO colunaTxColIntEm = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXCOL_INT_EME", "VL_TX_COL_INT_EME", "TP_INDIC_TX_COL_INT_EME", "PS_TAX_ORIG_TXCOL_INT_EME", "PS_MIN_TX_COL_INT_EME",
				"VL_EXC_ORIG_TXCOL_INT_EME", "VL_EXCED_TX_COL_INT_EME"); 
        dto.setColunaTxColIntEm(colunaTxColIntEm);
        
        ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbConv = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXENT_URB_CONV", "VL_TX_ENT_URB_CONV", "TP_INDIC_TX_ENT_URB_CONV", "PS_TAX_ORIG_TXENT_URB_CONV", "PS_MIN_TX_ENT_URB_CONV",
				"VL_EXC_ORIG_TXENT_URB_CONV", "VL_EXCED_TX_ENT_URB_CONV"); 
        dto.setColunaTxEntUrbConv(colunaTxEntUrbConv);

        ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntConv = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXENT_INT_CONV", "VL_TX_ENT_INT_CONV", "TP_INDIC_TX_ENT_INT_CONV", "PS_TAX_ORIG_TXENT_INT_CONV", "PS_MIN_TX_ENT_INT_CONV",
				"VL_EXC_ORIG_TXENT_INT_CONV", "VL_EXCED_TX_ENT_INT_CONV"); 
        dto.setColunaTxEntIntConv(colunaTxEntIntConv);

        ParametroPropostaDestinoAereoColunaDTO colunaTxEntUrbEm = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXENT_URB_EME", "VL_TX_ENT_URB_EME", "TP_INDIC_TX_ENT_URB_EME", "PS_TAX_ORIG_TXENT_URB_EME", "PS_MIN_TX_ENT_URB_EME",
				"VL_EXC_ORIG_TXENT_URB_EME", "VL_EXCED_TX_ENT_URB_EME"); 
        dto.setColunaTxEntUrbEm(colunaTxEntUrbEm);

        ParametroPropostaDestinoAereoColunaDTO colunaTxEntIntEm = populateTaxasDestinoAereoColunaDTO(map, domainValuesParametroCliente,
				"VL_ORIGINAL_TXENT_INT_EME", "VL_TX_ENT_INT_EME", "TP_INDIC_TX_ENT_INT_EME", "PS_TAX_ORIG_TXENT_INT_EME", "PS_MIN_TX_ENT_INT_EME",
				"VL_EXC_ORIG_TXENT_INT_EME", "VL_EXCED_TX_ENT_INT_EME");
        dto.setColunaTxEntIntEm(colunaTxEntIntEm);
    }

    private void buildColunasAdValorem(Map<String, Object> map, ParametroPropostaDestinoAereoTableDTO dto, List<DomainValue> domainValuesParametroCliente) {
        ParametroPropostaDestinoAereoColunaDTO colunaAdvalorem1DTO = new ParametroPropostaDestinoAereoColunaDTO();
        colunaAdvalorem1DTO.setVlOriginal((BigDecimal)getDefauldValueFromMap(map, "VL_ORIGINAL_ADVALOREM1", BigDecimal.ZERO));
        colunaAdvalorem1DTO.setValor((BigDecimal)getDefauldValueFromMap(map, "VL_ADVALOREM", colunaAdvalorem1DTO.getVlOriginal()));
        colunaAdvalorem1DTO.setTpIndicador(getDomainValueFromList(domainValuesParametroCliente, (String)getDefauldValueFromMap(map, "TP_INDICADOR_ADVALOREM", "T")));
        colunaAdvalorem1DTO.setPercentual(colunaAdvalorem1DTO.calculaDescontoAcrescimo());
        dto.setColunaAdValorem1(colunaAdvalorem1DTO);
        
        ParametroPropostaDestinoAereoColunaDTO colunaAdvalorem2DTO = new ParametroPropostaDestinoAereoColunaDTO();
        colunaAdvalorem2DTO.setVlOriginal((BigDecimal)getDefauldValueFromMap(map, "VL_ORIGINAL_ADVALOREM2", BigDecimal.ZERO));
        colunaAdvalorem2DTO.setValor((BigDecimal)getDefauldValueFromMap(map, "VL_ADVALOREM2", colunaAdvalorem2DTO.getVlOriginal()));
        colunaAdvalorem2DTO.setTpIndicador(getDomainValueFromList(domainValuesParametroCliente, (String)getDefauldValueFromMap(map, "TP_INDICADOR_ADVALOREM2", "T")));
        colunaAdvalorem2DTO.setPercentual(colunaAdvalorem2DTO.calculaDescontoAcrescimo());
        dto.setColunaAdValorem2(colunaAdvalorem2DTO);
    }

    private void buildColunaProdutoEspecifico(Map<String, Object> map, ParametroPropostaDestinoAereoTableDTO dto, List<DomainValue> domainValuesParametroCliente) {
        ParametroPropostaDestinoAereoColunaDTO colunaProdutoEspecificoDTO = new ParametroPropostaDestinoAereoColunaDTO();
        colunaProdutoEspecificoDTO.setVlOriginal((BigDecimal)getDefauldValueFromMap(map, "VL_ORIGINAL_PD_ESPECIF", BigDecimal.ZERO));
        colunaProdutoEspecificoDTO.setValor((BigDecimal)getDefauldValueFromMap(map, "VL_PD_ESPECIF", colunaProdutoEspecificoDTO.getVlOriginal()));
        colunaProdutoEspecificoDTO.setTpIndicador(getDomainValueFromList(domainValuesParametroCliente, (String)getDefauldValueFromMap(map, "TP_INDICADOR_PD_ESPECIF", "T")));
        colunaProdutoEspecificoDTO.setPercentual(colunaProdutoEspecificoDTO.calculaDescontoAcrescimo());
        dto.setColunaProdutoEspecifico(colunaProdutoEspecificoDTO);
    } 

    private void buildColunaValorPorKg(Map<String, Object> map, ParametroPropostaDestinoAereoTableDTO dto, List<DomainValue> domainValuesParametroCliente) {
        
        DomainValue domainValue = getDomainValueFromList(domainValuesParametroCliente, (String)getDefauldValueFromMap(map, "TP_INDICADOR_FRETE_PESO", "D"));
        
        ParametroPropostaDestinoAereoColunaDTO colunaValoPorKgDTO = new ParametroPropostaDestinoAereoColunaDTO();
        colunaValoPorKgDTO.setVlOriginal((BigDecimal)getDefauldValueFromMap(map, "VL_ORIGINAL_FRETE_PESO", BigDecimal.ZERO));
        colunaValoPorKgDTO.setValor((BigDecimal)getDefauldValueFromMap(map, "VL_FRETE_PESO", colunaValoPorKgDTO.getVlOriginal()));
        colunaValoPorKgDTO.setTpIndicador(domainValue);
        colunaValoPorKgDTO.setPercentual(colunaValoPorKgDTO.calculaDescontoAcrescimo());
        dto.setColunaValoPorKg(colunaValoPorKgDTO);
    }

    private void buildColunaValorMinimo(Map<String, Object> map,
            ParametroPropostaDestinoAereoTableDTO dto, List<DomainValue> domainValuesParametroCliente) {
        
        DomainValue domainValue = getDomainValueFromList(domainValuesParametroCliente, (String)getDefauldValueFromMap(map, "TP_INDICADOR_FRETE_MINIMO", "D"));
        
        ParametroPropostaDestinoAereoColunaDTO colunaValorMinimoDTO = new ParametroPropostaDestinoAereoColunaDTO();
        BigDecimal vlOriginal = (BigDecimal)getDefauldValueFromMap(map, "VL_ORIGINAL_FRETE_MINIMO", BigDecimal.ZERO);
        colunaValorMinimoDTO.setVlOriginal(BigDecimalUtils.round(vlOriginal));
        colunaValorMinimoDTO.setValor((BigDecimal)getDefauldValueFromMap(map, "VL_FRETE_MINIMO", colunaValorMinimoDTO.getVlOriginal()));
        colunaValorMinimoDTO.setTpIndicador(domainValue);
        colunaValorMinimoDTO.setPercentual(colunaValorMinimoDTO.calculaDescontoAcrescimo());
        dto.setColunaValorMinimo(colunaValorMinimoDTO);
    }
	
	@SuppressWarnings("rawtypes")
	private Object getDefauldValueFromMap( Map map, String key, Object defaultValue){
	    Object object = defaultValue;
	    if (map.containsKey(key) && map.get(key)!=null){
	        return map.get(key);
	    }
	    return object;
	}
	
	
	@POST
	@Path("generateDestinosProposta")
	@SuppressWarnings({ "unchecked" })
	public Response generateDestinosProposta(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO) {
		TypedFlatMap criteria = getFiltrosParametroProposta(geracaoParametroPropostaDTO);
		List<Map<String, Object>> toReturn = destinoPropostaService.generateDestinosProposta(criteria);
		return getReturnFind(convertToParametroPropostaDestinoTableDTO(geracaoParametroPropostaDTO, toReturn), toReturn.size());
	}

	private List<ParametroPropostaDestinoTableDTO> convertToParametroPropostaDestinoTableDTO(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO, List<Map<String, Object>> list) {
		List<ParametroPropostaDestinoTableDTO> listParametroPropostaDestinoDto = new ArrayList<ParametroPropostaDestinoTableDTO>();
		
		for (Map<String, Object> parametroPropostaMap : list) {
			ParametroPropostaDestinoTableDTO parametroPropostaDestinoTableDTO = new ParametroPropostaDestinoTableDTO();
			parametroPropostaDestinoTableDTO.setIdDestinoProposta((Long) parametroPropostaMap.get("idDestinoProposta"));
			parametroPropostaDestinoTableDTO.setSiglaDescricao((String) parametroPropostaMap.get("siglaDescricao"));
			parametroPropostaDestinoTableDTO.setTpDiferencaAdvalorem(new DomainValue(parametroPropostaMap.get("tpDiferencaAdvalorem") != null ? (String) parametroPropostaMap.get("tpDiferencaAdvalorem") : ""));
			parametroPropostaDestinoTableDTO.setIdUnidadeFederativaDestino((Long) parametroPropostaMap.get("idUnidadeFederativaDestino"));
			parametroPropostaDestinoTableDTO.setPcDiferencaFretePeso((BigDecimal) parametroPropostaMap.get("pcDiferencaFretePeso"));
			parametroPropostaDestinoTableDTO.setPcDiferencaAdvalorem((BigDecimal) parametroPropostaMap.get("pcDiferencaAdvalorem"));
			parametroPropostaDestinoTableDTO.setIdTipoLocalizacaoMunicipioDestino((Long) parametroPropostaMap.get("idTipoLocalizacaoMunicipioDestino"));
			
			if(parametroPropostaDestinoTableDTO.getIdDestinoProposta() == null){
				parametroPropostaDestinoTableDTO.setChecked(false);
				parametroPropostaDestinoTableDTO.setTpIndicadorFreteMinimo(geracaoParametroPropostaDTO.getTpIndicadorFreteMinimo());
				parametroPropostaDestinoTableDTO.setVlFreteMinimo(geracaoParametroPropostaDTO.getVlFreteMinimo());
				parametroPropostaDestinoTableDTO.setTpIndicadorFretePeso(geracaoParametroPropostaDTO.getTpIndicadorFretePeso());
				parametroPropostaDestinoTableDTO.setVlFretePeso(geracaoParametroPropostaDTO.getVlFretePeso());
				parametroPropostaDestinoTableDTO.setTpIndicadorAdvalorem(geracaoParametroPropostaDTO.getTpIndicadorAdvalorem());
				parametroPropostaDestinoTableDTO.setVlAdvalorem(geracaoParametroPropostaDTO.getVlAdvalorem());
			} else {
				parametroPropostaDestinoTableDTO.setChecked(true);
				parametroPropostaDestinoTableDTO.setTpIndicadorFreteMinimo(new DomainValue(parametroPropostaMap.get("tpIndicadorFreteMinimo") != null ? (String) parametroPropostaMap.get("tpIndicadorFreteMinimo") : ""));
				parametroPropostaDestinoTableDTO.setVlFreteMinimo((BigDecimal) parametroPropostaMap.get("vlFreteMinimo"));
				parametroPropostaDestinoTableDTO.setTpIndicadorFretePeso(new DomainValue(parametroPropostaMap.get("tpIndicadorFretePeso") != null ? (String) parametroPropostaMap.get("tpIndicadorFretePeso") : ""));
				parametroPropostaDestinoTableDTO.setVlFretePeso((BigDecimal) parametroPropostaMap.get("vlFretePeso"));
				parametroPropostaDestinoTableDTO.setTpIndicadorAdvalorem(new DomainValue(parametroPropostaMap.get("tpIndicadorAdvalorem") != null ? (String) parametroPropostaMap.get("tpIndicadorAdvalorem") : ""));
				parametroPropostaDestinoTableDTO.setVlAdvalorem((BigDecimal) parametroPropostaMap.get("vlAdvalorem"));
			}
			
			if("T".equals(parametroPropostaDestinoTableDTO.getTpIndicadorAdvalorem().getValue())){
				parametroPropostaDestinoTableDTO.setVlAdvaloremDisabled(Boolean.TRUE);
			}
			
			listParametroPropostaDestinoDto.add(parametroPropostaDestinoTableDTO);
		}
		
		return listParametroPropostaDestinoDto;
	}
	
	private TypedFlatMap getFiltrosParametroProposta(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO) {
		TypedFlatMap criteria = new TypedFlatMap();
		
		criteria.put("simulacao.idSimulacao", geracaoParametroPropostaDTO.getIdSimulacao());
		criteria.put("pcDiferencaFretePeso", geracaoParametroPropostaDTO.getPcDiferencaFretePeso());
		criteria.put("blPagaPesoExcedente", geracaoParametroPropostaDTO.isBlPagaPesoExcedente());
		criteria.put("isGenerate", geracaoParametroPropostaDTO.isGenerate());
		criteria.put("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", geracaoParametroPropostaDTO.getUfOrigem().getIdUnidadeFederativa());
		criteria.put("pcDiferencaAdvalorem", geracaoParametroPropostaDTO.getPcDiferencaAdvalorem());
		criteria.put("vlFretePeso", geracaoParametroPropostaDTO.getVlFretePeso());
		criteria.put("vlAdvalorem", geracaoParametroPropostaDTO.getVlAdvalorem());
		criteria.put("tpIndicadorMinFretePeso", geracaoParametroPropostaDTO.getTpIndicadorMinFretePeso().getValue());
		criteria.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", geracaoParametroPropostaDTO.getTipoLocalizacaoMunicipioOrigem().getIdTipoLocalizacaoMunicipio());
		criteria.put("idProposta", geracaoParametroPropostaDTO.getIdProposta());
		criteria.put("tpIndicadorFretePeso", geracaoParametroPropostaDTO.getTpIndicadorFretePeso().getValue());
		criteria.put("vlMinFretePeso", geracaoParametroPropostaDTO.getVlMinFretePeso());
		criteria.put("tpDiferencaAdvalorem", geracaoParametroPropostaDTO.getTpDiferencaAdvalorem().getValue());
		criteria.put("tpIndicadorFreteMinimo", geracaoParametroPropostaDTO.getTpIndicadorFreteMinimo().getValue());
		criteria.put("tpIndicadorAdvalorem", geracaoParametroPropostaDTO.getTpIndicadorAdvalorem().getValue());
		criteria.put("blFreteExpedido", geracaoParametroPropostaDTO.isBlFreteExpedido());
		criteria.put("vlFreteMinimo", geracaoParametroPropostaDTO.getVlFreteMinimo());
		criteria.put("blFreteRecebido", geracaoParametroPropostaDTO.isBlFreteRecebido());
		criteria.put("disableAll", geracaoParametroPropostaDTO.isDisableAll());
		
		return criteria;
	}
	
	
	@POST
	@Path("findComboUnidadesFederativas")
	public Response findComboUnidadesFederativas(){
	    List<UnidadeFederativaDTO> dtos = new ArrayList<UnidadeFederativaDTO>();
	    
	    List<TypedFlatMap> list = unidadeFederativaService.findUnidadeFederativa("BRA", ConstantesExpedicao.TP_EMPRESA_MERCURIO);
	    for (TypedFlatMap ufMap : list){
	        dtos.add(new UnidadeFederativaDTO(ufMap.getLong("idUnidadeFederativa"), ufMap.getString("sgUnidadeFederativa"), ufMap.getString("nmUnidadeFederativa")));
	    }
	    
	    return Response.ok(dtos).build();
	}
	
	@POST
	@Path("findComboTipoLocalizacaoMunicipio")
	public Response findComboTipoLocalizacaoMunicipio(){
	    List<TipoLocalizacaoMunicipioDTO> dtos = new ArrayList<TipoLocalizacaoMunicipioDTO>();
	    List retorno = null;
	    Map criteria = new HashMap<String, Object>();
        if("MTZ".equals(SessionUtils.getFilialSessao().getSgFilial())){
            criteria.put("tpLocalizacao", "P");
            retorno=tipoLocalizacaoMunicipioService.find(criteria);
            for (TipoLocalizacaoMunicipio entity: (List<TipoLocalizacaoMunicipio>)retorno){
                dtos.add(new TipoLocalizacaoMunicipioDTO(entity.getIdTipoLocalizacaoMunicipio(), entity.getDsTipoLocalizacaoMunicipio().getValue()));
            }
        }else{
            ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("TIPO_PROPOSTA_FILIAL", false);
            String [] sTipos=parametroGeral.getDsConteudo().split(";");
            Long[] lTipos = new Long[sTipos.length];
            for (int i = 0; i < sTipos.length; i++) {
                lTipos[i] = Long.parseLong(sTipos[i]);
            }
            retorno=tipoLocalizacaoMunicipioService.findByTipoPropostaFilial(lTipos);
            for (TipoLocalizacaoMunicipio entity: (List<TipoLocalizacaoMunicipio>)retorno){
                dtos.add(new TipoLocalizacaoMunicipioDTO(entity.getIdTipoLocalizacaoMunicipio(), entity.getDsTipoLocalizacaoMunicipio().getValue()));
            }
        }
	    return Response.ok(dtos).build();
	}


	@POST
	@Path("findPropostaByIdSimulacao")
	public Response findPropostaByIdSimulacao(Long idSimulacao){
	    GeracaoParametroPropostaDTO dto = new GeracaoParametroPropostaDTO();
	    
	    Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
	    dto.setIdSimulacao(idSimulacao);
	    Simulacao simulacao = simulacaoService.findById(idSimulacao);
	    dto.setTpGeracaoProposta(simulacao.getTpGeracaoProposta());
	    dto.setNrSimulacao(simulacao.getNrSimulacao());
	    
	    TabelaPreco t = tabelaPrecoService.findByIdTabelaPrecoNotLazy(simulacao.getTabelaPreco().getIdTabelaPreco());
	    TabelaPrecoSuggestDTO tabelaPrecoDto = new TabelaPrecoSuggestDTO();
	    tabelaPrecoDto.setIdTabelaPreco(t.getIdTabelaPreco());
	    tabelaPrecoDto.setNomeTabela(t.getTabelaPrecoString());
	    tabelaPrecoDto.setDescricao(t.getDsDescricao());
	    
	    ClienteSuggestDTO clienteDto = new ClienteSuggestDTO();
	    clienteDto.setIdCliente(simulacao.getClienteByIdCliente().getIdCliente());
	    Pessoa p = pessoaService.findById(simulacao.getClienteByIdCliente().getIdCliente());
	    clienteDto.setNmPessoa(p.getNmPessoa());
	    clienteDto.setNrIdentificacao(p.getNrIdentificacao());
	    dto.setCliente(clienteDto);
	    
	    ServicoSuggestDTO servicoDto = new ServicoSuggestDTO();
	    Servico s = servicoService.findById(simulacao.getServico().getIdServico());
	    servicoDto.setIdServico(s.getIdServico());
	    servicoDto.setNomeServico(s.getNomeServico());
	    dto.setServico(servicoDto);
	    
	    DivisaoClienteSuggestDTO divisaoDto = new DivisaoClienteSuggestDTO();
	    DivisaoCliente d = divisaoClienteService.findById(simulacao.getDivisaoCliente().getIdDivisaoCliente());
	    divisaoDto.setId(d.getIdDivisaoCliente());
	    divisaoDto.setNmDivisaoCliente(d.getDsDivisaoCliente());
	    
	    
	    dto.setDivisaoCliente(divisaoDto);
	    
	    dto.setTabelaPreco(tabelaPrecoDto);
	    if (simulacao.getProdutoEspecifico() != null){
	        dto.setProdutoEspecifico(new ProdutoEspecificoDTO(simulacao.getProdutoEspecifico().getIdProdutoEspecifico(), null, null));
	    }
	    
	    
	    if (proposta !=null){
	          dto.setIdProposta(proposta.getIdProposta());  
	          dto.setTpIndicadorMinFretePeso(proposta.getTpIndicadorMinFretePeso());  
	          dto.setVlMinFretePeso(proposta.getVlMinFretePeso());  
	          dto.setTpIndicadorFreteMinimo(proposta.getTpIndicadorFreteMinimo());
	          dto.setVlFreteMinimo(proposta.getVlFreteMinimo());
	          dto.setTpIndicadorFretePeso(proposta.getTpIndicadorFretePeso());
	          dto.setVlFretePeso(proposta.getVlFretePeso());;
	          dto.setPcDiferencaFretePeso(proposta.getPcDiferencaFretePeso());
	          dto.setBlPagaPesoExcedente(proposta.getBlPagaPesoExcedente());

	          dto.setTpIndicadorAdvalorem(proposta.getTpIndicadorAdvalorem());
	          dto.setVlAdvalorem(proposta.getVlAdvalorem());

	          if (proposta.getSimulacao()!=null){
	              dto.setTpDiferencaAdvalorem(proposta.getSimulacao().getTpDiferencaAdvalorem());
	          }
	          dto.setPcDiferencaAdvalorem(proposta.getPcDiferencaAdvalorem());
	          
	          
	          if (proposta.getUnidadeFederativaByIdUfOrigem()!=null){
	              UnidadeFederativa uf = proposta.getUnidadeFederativaByIdUfOrigem();
	              dto.setUfOrigem(new UnidadeFederativaDTO(uf.getIdUnidadeFederativa(), uf.getSgUnidadeFederativa(), uf.getNmUnidadeFederativa()));
	          }
	          
	          if (proposta.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null){
	              TipoLocalizacaoMunicipio tpLocalizacao = proposta.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem();
	              dto.setTipoLocalizacaoMunicipioOrigem(new TipoLocalizacaoMunicipioDTO(tpLocalizacao.getIdTipoLocalizacaoMunicipio(),
	                      tpLocalizacao.getDsTipoLocalizacaoMunicipio().getValue()));
	          }
	          
	          Aeroporto aeroporto = null;
	          if (proposta.getAeroportoReferencia()!=null){
	              aeroporto = aeroportoService.findById(proposta.getAeroportoReferencia().getIdAeroporto());
	          }else{
	              aeroporto = aeroportoService.findAeroportoAtendeCliente(simulacao.getClienteByIdCliente().getIdCliente());
	          }
            if (aeroporto != null) {
                dto.setAeroportoReferencia(new AeroportoSuggestDTO(aeroporto.getIdAeroporto(), aeroporto.getSgAeroporto(),
                        aeroporto.getPessoa().getNmPessoa()));
            }
	          
	            
	          
	          dto.setBlFreteExpedido(proposta.getBlFreteExpedido());
	          dto.setBlFreteRecebido(proposta.getBlFreteRecebido());
	    }else{
	        UnidadeFederativa uf = unidadeFederativaService.findByIdPessoa(SessionUtils.getFilialSessao().getPessoa().getIdPessoa());
	        dto.setUfOrigem(new UnidadeFederativaDTO(uf.getIdUnidadeFederativa(), uf.getSgUnidadeFederativa(), uf.getNmUnidadeFederativa()));
	        Aeroporto aeroporto = aeroportoService.findAeroportoAtendeCliente(simulacao.getClienteByIdCliente().getIdCliente());
	        if (aeroporto != null){
	            dto.setAeroportoReferencia(new AeroportoSuggestDTO(aeroporto.getIdAeroporto(), aeroporto.getSgAeroporto(), aeroporto.getPessoa().getNmPessoa()));
	        }
	        
	    }
	    
	    return Response.ok(dto).build();
	}
	
	
	@POST
	@Path("findComboRegioesPropostaConvencional")
	public Response findComboRegioesPropostaConvencional(){
	    List<RegiaoGeografica> regioes = regiaoGeograficaService.findByPaisSessao(); 
	    
	    List<DestinoPropostaRegiaoDTO> regioesDTO = new ArrayList<DestinoPropostaRegiaoDTO>();  
	    for (RegiaoGeografica regiao:regioes){
	        regioesDTO.add(new DestinoPropostaRegiaoDTO(regiao.getIdRegiaoGeografica(),regiao.getDsRegiaoGeografica().getValue()));
	    }
	    
	    return Response.ok(regioesDTO).build();
	}

	@POST
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Path("findDestinosRegiaoConvencional")
	public Response findDestinosRegiaoConvencional(Map criteria){
	    List<Map<String,Object>> destinosRegiao = destinoPropostaService.findDestinosPropostaConvencionalAereo(criteria);
	    return Response.ok(extractDestinosDTO(destinosRegiao, LongUtils.getLong(criteria.get("idSimulacao")))).build();
	}

    private List<DestinoPropostaDTO> extractDestinosDTO(List<Map<String, Object>> destinosRegiao, Long idSimulacao) {
        List<DestinoPropostaDTO> dtos = new ArrayList<DestinoPropostaDTO>();
	    for (Map<String,Object> map: destinosRegiao){
	        DestinoPropostaDTO dto =  new DestinoPropostaDTO((String)map.get("DS_DESTINO"));
	        dto.setIdDestinoProposta((Long)map.get("ID_DESTINO_PROPOSTA"));
	        
	        if(dto.getIdDestinoProposta() != null){
	        	dto.setBlGeraDestinoProposta(true);
	        } else {
	        	dto.setBlGeraDestinoProposta(false);
	        }
	        
	        dto.setIdRotaPreco((Long)map.get("ID_ROTA_PRECO"));
	        dto.setId((Long)getDefauldValueFromMap(map, "ID_ROTA_PRECO", null));
	        dto.setIdSimulacao(idSimulacao);
	        dto.setIdAeroporto((Long)map.get("ID_AEROPORTO"));
	        dto.setIdUnidadeFederativa((Long)map.get("ID_UNIDADE_FEDERATIVA"));
	        dto.setRegiao(new DestinoPropostaRegiaoDTO((Long)map.get("ID_REGIAO_GEOGRAFICA"), (String)map.get("DS_REGIAO_GEOGRAFICA")));
	        dtos.add(dto);
	    }
        return dtos;
    }
	
    @POST
	@Path("storeDestinosConvencional")
	public Response storeDestinosConvencional(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO) {
		List<DestinoProposta> listaDestinoProposta = convertFromDestinoPropostaDTOToDestinoProposta(geracaoParametroPropostaDTO, geracaoParametroPropostaDTO.getListaDestinoPropostaDTO());
		AeroportoSuggestDTO aeroporto = geracaoParametroPropostaDTO.getAeroportoReferencia();
		destinoPropostaService.storeDestinosConvencional(geracaoParametroPropostaDTO.getIdSimulacao(), aeroporto.getIdAeroporto(), 
		        geracaoParametroPropostaDTO.isBlFreteExpedido(), geracaoParametroPropostaDTO.isBlFreteRecebido() , listaDestinoProposta);
		return Response.ok().build();
	}
    
    private Boolean isRegistroParaExclusao(DestinoPropostaDTO destinoPropostaDTO){
    	Boolean retorno = Boolean.FALSE;
    	
    	if(destinoPropostaDTO.getIdDestinoProposta() != null  
    			&& (destinoPropostaDTO.getBlGeraDestinoProposta() == null 
    				|| BooleanUtils.isFalse(destinoPropostaDTO.getBlGeraDestinoProposta()))){
    		retorno = Boolean.TRUE;
    	}
    	
    	return retorno;
    }
    
    private Boolean isRegistroExistenteNaoModificado(DestinoPropostaDTO destinoPropostaDTO){
    	Boolean retorno = Boolean.FALSE;
    	
    	boolean semParcelas = destinoPropostaDTO.getParcelas() == null || destinoPropostaDTO.getParcelas().isEmpty();
    	boolean geraDestino = destinoPropostaDTO.getBlGeraDestinoProposta() != null && BooleanUtils.isTrue(destinoPropostaDTO.getBlGeraDestinoProposta());
    	
    	if(destinoPropostaDTO.getIdDestinoProposta() != null 
    			&& geraDestino
    			&& semParcelas){
    		retorno = Boolean.TRUE;
    	}
    	
    	return retorno;
    }
    
    private Boolean isRegistroNaoModificado(DestinoPropostaDTO destinoPropostaDTO){
    	Boolean retorno = Boolean.FALSE;
    	
    	boolean naoGeraDestino = destinoPropostaDTO.getBlGeraDestinoProposta() == null || BooleanUtils.isFalse(destinoPropostaDTO.getBlGeraDestinoProposta());
    	boolean semParcelas = destinoPropostaDTO.getParcelas() == null || destinoPropostaDTO.getParcelas().isEmpty()  ;
    	
    	if(destinoPropostaDTO.getIdDestinoProposta() == null 
    			&& naoGeraDestino){
    		retorno = Boolean.TRUE;
    	}
    	
    	return retorno;
    }
    
    private List<DestinoProposta> convertFromDestinoPropostaDTOToDestinoProposta(GeracaoParametroPropostaDTO geracaoParametroPropostaDTO, List<DestinoPropostaDTO> listaDestinoPropostaDTO){
    	Simulacao simulacao = new Simulacao();
		simulacao.setIdSimulacao(geracaoParametroPropostaDTO.getIdSimulacao());
		String tpGeracaoProposta = geracaoParametroPropostaDTO.getTpGeracaoProposta().getValue();
    	
    	List<DestinoProposta> listaDestinoProposta = new ArrayList<DestinoProposta>();
		for (DestinoPropostaDTO destinoPropostaDTO : listaDestinoPropostaDTO) {
		    boolean isMarcadoParaExclusao = isRegistroParaExclusao(destinoPropostaDTO);
		    
			if(isRegistroExistenteNaoModificado(destinoPropostaDTO) || isRegistroNaoModificado(destinoPropostaDTO)){
				continue;
			}

			DestinoProposta destinoProposta = new DestinoProposta();
			destinoProposta.setIdDestinoProposta(destinoPropostaDTO.getIdDestinoProposta());
			listaDestinoProposta.add(destinoProposta);
			
			
            destinoProposta.setMarcadoParaExclusao(isMarcadoParaExclusao);
            
			
			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(destinoPropostaDTO.getIdAeroporto());
			destinoProposta.setAeroportoByIdAeroporto(aeroporto);
			destinoProposta.setRotaPreco(new RotaPreco(destinoPropostaDTO.getIdRotaPreco()));    
			
			UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
			unidadeFederativa.setIdUnidadeFederativa(destinoPropostaDTO.getIdUnidadeFederativa()); 
			destinoProposta.setUnidadeFederativa(unidadeFederativa);
			
			// Campos obrigatórios que não são populados neste tipo de proposta.
			destinoProposta.setPcDiferencaFretePeso(BigDecimal.ZERO);
			

		    destinoProposta.setTpIndicadorFretePeso(new DomainValue("T"));
            destinoProposta.setVlFretePeso(BigDecimal.ZERO);
            destinoProposta.setPcDiferencaFretePeso(BigDecimal.ZERO);
            
            destinoProposta.setPsMinimoFretePeso(BigDecimal.ZERO);
            destinoProposta.setVlProdutoEspecifico(BigDecimal.ZERO);
            destinoProposta.setTpIndicadorFreteMinimo(new DomainValue("T"));
            destinoProposta.setVlFreteMinimo(BigDecimal.ZERO);
            destinoProposta.setVlAdvalorem(BigDecimal.ZERO);
            destinoProposta.setTpIndicadorAdvalorem(new DomainValue("T"));
            destinoProposta.setTpDiferencaAdvalorem(new DomainValue("P"));
            destinoProposta.setPcDiferencaAdvalorem(BigDecimal.ZERO);
             
            
			
			destinoProposta.setListaValorFaixaProgressivaProposta(new ArrayList<ValorFaixaProgressivaProposta>());
			
			if (destinoPropostaDTO.getParcelas() != null && !destinoPropostaDTO.getParcelas().isEmpty()){
			    for(DestinoPropostaParcelaDTO destinoPropostaParcelaDTO : destinoPropostaDTO.getParcelas()){
	                String cdParcelaPreco = destinoPropostaParcelaDTO.getCdParcelaPreco();
	                
	                if(CD_FRETE_PESO.equals(cdParcelaPreco)){
	                    if (TP_GERACAO_CONVENCIONAL.equals(tpGeracaoProposta)){
	                        popularValorFaixaProgressivaProposta(simulacao, destinoProposta, destinoPropostaParcelaDTO);
	                    }else{
	                        if(destinoPropostaParcelaDTO.getIdProdutoEspecifico() != null){
	                            destinoProposta.setTpIndicadorProdutoEspecifico(destinoPropostaParcelaDTO.getTpIndicador());
	                            destinoProposta.setVlProdutoEspecifico(destinoPropostaParcelaDTO.getVlCalculado());
	                        }
	                    }
	                    
	                } else if(CD_TARIFA_MINIMA.equals(cdParcelaPreco)){
	                    destinoProposta.setVlFreteMinimo(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setTpIndicadorFreteMinimo(destinoPropostaParcelaDTO.getTpIndicador());
	                } else if(CD_FRETE_QUILO.equals(cdParcelaPreco)){
	                    destinoProposta.setVlFretePeso(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setTpIndicadorFretePeso(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setPsMinimoFretePeso(destinoPropostaParcelaDTO.getPsMinimo());
	                } else if(CD_ADVALOREM1.equals(cdParcelaPreco)){
	                    destinoProposta.setVlAdvalorem(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setTpIndicadorAdvalorem(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setPcDiferencaAdvalorem(destinoPropostaParcelaDTO.getVlPercentual());
	                    destinoProposta.setTpDiferencaAdvalorem(new DomainValue("P"));
	                    
	                } else if(CD_ADVALOREM2.equals(cdParcelaPreco)){
	                    destinoProposta.setVlAdvalorem2(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setTpIndicadorAdvalorem2(destinoPropostaParcelaDTO.getTpIndicador());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaColetaInteriorConvencial(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaColetaInteriorConvencional(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaColetaInteriorConvencional(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaColetaInteriorConvencional(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaColetaInteriorEmergencial(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaColetaInteriorEmergencial(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaColetaInteriorEmergencial(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaColetaInteriorEmergencial(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaColetaUrbanaConvencional(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaColetaUrbanaConvencional(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaColetaUrbanaConvencional(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaColetaUrbanaConvencional(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaColetaUrbanaEmergencial(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaColetaUrbanaEmergencial(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaColetaUrbanaEmergencial(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaColetaUrbanaEmergencial(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaEntregaInteriorConvencional(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaEntregaInteriorConvencional(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaEntregaInteriorConvencional(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaEntregaInteriorConvencional(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaEntregaInteriorEmergencial(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaEntregaInteriorEmergencial(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaEntregaInteriorEmergencial(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaEntregaInteriorEmergencial(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaEntregaUrbanaConvencional(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaEntregaUrbanaConvencional(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaEntregaUrbanaConvencional(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaEntregaUrbanaConvencional(destinoPropostaParcelaDTO.getVlExcedente());
	                    
	                } else if(ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA.equals(cdParcelaPreco)){
	                    destinoProposta.setTpIndicadorTaxaEntregaUrbanaEmergencial(destinoPropostaParcelaDTO.getTpIndicador());
	                    destinoProposta.setVlTaxaEntregaUrbanaEmergencial(destinoPropostaParcelaDTO.getVlCalculado());
	                    destinoProposta.setPsMinimoTaxaEntregaUrbanaEmergencial(destinoPropostaParcelaDTO.getPsMinimo());
	                    destinoProposta.setVlExcedenteTaxaEntregaUrbanaEmergencial(destinoPropostaParcelaDTO.getVlExcedente());
	                }
	            }
			}
		}
		
		return listaDestinoProposta;
    }

	private void popularValorFaixaProgressivaProposta(Simulacao simulacao, DestinoProposta destinoProposta,	DestinoPropostaParcelaDTO destinoPropostaParcelaDTO) {
		FaixaProgressivaProposta faixaProgressivaProposta = new FaixaProgressivaProposta();
		faixaProgressivaProposta.setSimulacao(simulacao);
		faixaProgressivaProposta.setIdFaixaProgressivaProposta(destinoPropostaParcelaDTO.getIdFaixaProgressivaProposta());
		
		if(destinoPropostaParcelaDTO.getIdProdutoEspecifico() != null){
			ProdutoEspecifico produtoEspecifico = new ProdutoEspecifico();
			produtoEspecifico.setIdProdutoEspecifico(destinoPropostaParcelaDTO.getIdProdutoEspecifico());
			faixaProgressivaProposta.setProdutoEspecifico(produtoEspecifico);
			
		} else {
			faixaProgressivaProposta.setVlFaixa(destinoPropostaParcelaDTO.getVlFaixaProgressiva());
		}
		
		ValorFaixaProgressivaProposta valorFaixaProgressivaProposta = new ValorFaixaProgressivaProposta();
		valorFaixaProgressivaProposta.setIdValorFaixaProgressivaProposta(destinoPropostaParcelaDTO.getIdValorFaixaProgressiva());
		valorFaixaProgressivaProposta.setFaixaProgressivaProposta(faixaProgressivaProposta);
		valorFaixaProgressivaProposta.setTpIndicador(destinoPropostaParcelaDTO.getTpIndicador());
		valorFaixaProgressivaProposta.setVlFixo(destinoPropostaParcelaDTO.getVlCalculado());
		
		destinoProposta.getListaValorFaixaProgressivaProposta().add(valorFaixaProgressivaProposta);
	}
    
    
    /**
     * Busca as parcelas para um determinado destino da Proposta Convencional Aérea.
     * 
     * @param criteria Map contendo idDestinoProposta, idTabelaPreco e idRotaPreco
     * @return
     */
    @POST
    @Path("findParcelasDestinoConvencionalAereo")
    public Response findParcelasDestinoConvencionalAereo(Map criteria){
        DestinoProposta destinoProposta = null;
        if (criteria.get("idDestinoProposta") != null){
            destinoProposta = destinoPropostaService.findById(LongUtils.getLong(criteria.get("idDestinoProposta")));
        }
        
        List<ValorFaixaProgressivaProposta> valoresFaixaFretePeso = null;
        List<ValorFaixaProgressivaProposta> valoresFaixaProdutoEspecifico = null;
        
        if (destinoProposta!=null){
            valoresFaixaFretePeso = valorFaixaProgressivaPropostaService.findByDestinoPropostaFretePeso(destinoProposta);
            valoresFaixaProdutoEspecifico = valorFaixaProgressivaPropostaService.findByDestinoPropostaProdutoEspecifico(destinoProposta);
        }
        List<DestinoPropostaParcelaDTO> parcelas = new ArrayList<DestinoPropostaParcelaDTO>();
        List<Map<String,Object>> parcelasList = destinoPropostaService.findParcelasDestinoConvencionalAereo(criteria);
        
        
        for (Map<String,Object> parcela:parcelasList){
            DestinoPropostaParcelaDTO dto = new DestinoPropostaParcelaDTO();
            String cdParcela = (String)getDefauldValueFromMap(parcela, "CD_PARCELA_PRECO", null);
            dto.setVlOriginal((BigDecimal)getDefauldValueFromMap(parcela, "VL_ORIGINAL", BigDecimal.ZERO));
            dto.setOrder(ORDER_TAXAS);
            dto.setCdParcelaPreco(cdParcela);

            Long idProdutoEspecifico = (Long)getDefauldValueFromMap(parcela, "ID_PRODUTO_ESPECIFICO", null);
            if (CD_FRETE_PESO.equals(cdParcela) && idProdutoEspecifico == null){
                dto.setVlFaixaProgressiva((BigDecimal)getDefauldValueFromMap(parcela, "VL_FAIXA_PROGRESSIVA", BigDecimal.ZERO));
                setValuesForFretePeso(dto,parcela,destinoProposta,valoresFaixaFretePeso);
            }else if (CD_FRETE_PESO.equals(cdParcela) && idProdutoEspecifico != null){
                dto.setIdProdutoEspecifico(idProdutoEspecifico);
                if (criteria.containsKey("idProdutoEspecifico")){
                    setValuesForProdutoEspecifico(dto, parcela, destinoProposta);
                }else{
                    setValuesForProdutoEspecifico(dto,parcela,destinoProposta,valoresFaixaProdutoEspecifico);
                }
            }else if (CD_TARIFA_MINIMA.equals(cdParcela)){
                setValuesForTarifaMinima(dto,parcela,destinoProposta);
            }else if (CD_ADVALOREM1.equals(cdParcela)){
                setValuesForAdValorem1(dto,parcela,destinoProposta);
            }else if (CD_ADVALOREM2.equals(cdParcela)){
                setValuesForAdValorem2(dto,parcela,destinoProposta);
            }else if (CD_FRETE_QUILO.equals(cdParcela)){
                setVAluesForFreteQuilo(dto,parcela,destinoProposta, (String)criteria.get("tpGeracaoProposta"));
            }else{
                setValuesForTaxas(dto,parcela,destinoProposta);
            }
            
            dto.setVlPercentual(dto.calculaDescontoAcrescimo());
            parcelas.add(dto);
        }
        
        Collections.sort(parcelas);
        
        return Response.ok(parcelas).build();
    }
    
    private void setVAluesForFreteQuilo(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela,
            DestinoProposta destinoProposta, String tpGeracaoProposta) {
        dto.setDsParcela(getVarcharI18nValue((String)getDefauldValueFromMap(parcela, "NM_PARCELA", "")));
        dto.setOrder(ORDER_FRETE_QUILO);
        dto.setPsMinimoOriginal((BigDecimal)getDefauldValueFromMap(parcela, "PS_MINIMO", BigDecimal.ZERO));
        if (destinoProposta!=null){
            setValues(dto, destinoProposta.getTpIndicadorFretePeso(),destinoProposta.getVlFretePeso(), null, destinoProposta.getPsMinimoFretePeso());
        }else{
            dto.setVlCalculado(dto.getVlOriginal());
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setPsMinimo(dto.getPsMinimoOriginal());
        }
        
    }

    private void setValuesForTaxas(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela, DestinoProposta destinoProposta) {
        dto.setDsParcela(getVarcharI18nValue((String)getDefauldValueFromMap(parcela, "NM_PARCELA", "")));
        dto.setOrder(ORDER_TAXAS);
        dto.setPsMinimoOriginal((BigDecimal)getDefauldValueFromMap(parcela, "PS_MINIMO", BigDecimal.ZERO));
        dto.setVlExcedenteOriginal((BigDecimal)getDefauldValueFromMap(parcela, "VL_EXCEDENTE", BigDecimal.ZERO));
        if (destinoProposta!=null){
            String cdParcelaPreco = (String)parcela.get("CD_PARCELA_PRECO");
            if ("IDEntregaInteriorEmergencia".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaEntregaInteriorEmergencial(),
                        destinoProposta.getVlTaxaEntregaInteriorEmergencial(), null, null);
            }else if ("IDColetaInteriorEmergencia".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaColetaInteriorEmergencial(),
                        destinoProposta.getVlTaxaColetaInteriorEmergencial(), null, null);
            }else if ("IDColetaInteriorConvencional".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaColetaInteriorConvencial(), 
                        destinoProposta.getVlTaxaColetaInteriorConvencional(), 
                        destinoProposta.getVlExcedenteTaxaColetaInteriorConvencional(), 
                        destinoProposta.getPsMinimoTaxaColetaInteriorConvencional());
            }else if ("IDEntregaInteriorConvencional".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaEntregaInteriorConvencional(),
                        destinoProposta.getVlTaxaEntregaInteriorConvencional(), 
                        destinoProposta.getVlExcedenteTaxaEntregaInteriorConvencional(), 
                        destinoProposta.getPsMinimoTaxaEntregaInteriorConvencional());
            }else if ("IDColetaUrbanaConvencional".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaColetaUrbanaConvencional(),
                        destinoProposta.getVlTaxaColetaUrbanaConvencional(),
                        destinoProposta.getVlExcedenteTaxaEntregaUrbanaConvencional(),
                        destinoProposta.getPsMinimoTaxaColetaUrbanaConvencional());
            }else if ("IDColetaUrbanaEmergencia".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaColetaUrbanaEmergencial(),
                        destinoProposta.getVlTaxaColetaUrbanaEmergencial(), 
                        destinoProposta.getVlExcedenteTaxaColetaUrbanaEmergencial(),
                        destinoProposta.getPsMinimoTaxaColetaUrbanaEmergencial());
            }else if ("IDEntregaUrbanaConvencional".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaEntregaUrbanaConvencional(), 
                        destinoProposta.getVlTaxaEntregaUrbanaConvencional(), 
                        destinoProposta.getVlExcedenteTaxaEntregaUrbanaConvencional(), 
                        destinoProposta.getPsMinimoTaxaEntregaUrbanaConvencional());
            }else if ("IDEntregaUrbanaEmergencia".equals(cdParcelaPreco)){
                setValues(dto, destinoProposta.getTpIndicadorTaxaEntregaUrbanaEmergencial(), 
                        destinoProposta.getVlTaxaEntregaUrbanaEmergencial(), 
                        destinoProposta.getVlExcedenteTaxaEntregaUrbanaEmergencial(), 
                        destinoProposta.getPsMinimoTaxaEntregaUrbanaEmergencial());
            }
        }else {
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
            dto.setVlExcedente(dto.getVlExcedenteOriginal());
            dto.setPsMinimo(dto.getPsMinimoOriginal());
        }
    }

    private void setValues(DestinoPropostaParcelaDTO dto, DomainValue tpIndicador, BigDecimal vlCalculado, BigDecimal vlExcedente, BigDecimal psMinimo){
        dto.setTpIndicador(tpIndicador != null ? getNewDomainValue(tpIndicador):getDefaultDomainValue());
        dto.setVlCalculado(vlCalculado!=null ? vlCalculado:BigDecimal.ZERO);
        dto.setVlExcedente(vlExcedente!=null ? vlExcedente:dto.getVlExcedenteOriginal());
        dto.setPsMinimo(psMinimo!=null ? psMinimo:dto.getPsMinimoOriginal());
    }
    
    /**
     * Busca os valores de produtos especificos a partr da estrutura filha da destino proposta (ValorFaixaProgressivaProposta)
     * 
     * @param dto
     * @param parcela
     * @param destinoProposta
     * @param valoresFaixaProdutoEspecifico
     */
    private void setValuesForProdutoEspecifico(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela,
            DestinoProposta destinoProposta, List<ValorFaixaProgressivaProposta> valoresFaixaProdutoEspecifico) {
        
        Long idProdutoEspecifico = (Long)getDefauldValueFromMap(parcela, "ID_PRODUTO_ESPECIFICO", null);
        
        dto.setDsParcela((String)getDefauldValueFromMap(parcela, "NM_PARCELA", ""));
        dto.setOrder(ORDER_PRODUTO_ESPECIFICO);
        
        if (destinoProposta!=null && idProdutoEspecifico !=null){
            for (ValorFaixaProgressivaProposta valorFaixa:valoresFaixaProdutoEspecifico){
                if (idProdutoEspecifico.equals(valorFaixa.getFaixaProgressivaProposta().getProdutoEspecifico().getIdProdutoEspecifico())){
                    setValues(dto, valorFaixa.getTpIndicador(), valorFaixa.getVlFixo(), null, null);
                    dto.setIdValorFaixaProgressiva(valorFaixa.getIdValorFaixaProgressivaProposta());
                    dto.setIdFaixaProgressivaProposta(valorFaixa.getFaixaProgressivaProposta().getIdFaixaProgressivaProposta());
                }
            }
        }else {
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
        }
    }
    
    /**
     * Busca os valores para o produto específico a partir do destino proposta.
     * 
     * @param dto
     * @param parcela
     * @param destinoProposta
     */
    private void setValuesForProdutoEspecifico(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela,DestinoProposta destinoProposta){
        dto.setDsParcela((String)getDefauldValueFromMap(parcela, "NM_PARCELA", ""));
        dto.setOrder(ORDER_PRODUTO_ESPECIFICO);
        if (destinoProposta!=null){
            setValues(dto, destinoProposta.getTpIndicadorProdutoEspecifico(), destinoProposta.getVlProdutoEspecifico(), null, null);
        }else{
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
        }
            
    }

    private void setValuesForFretePeso(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela, DestinoProposta destinoProposta,
            List<ValorFaixaProgressivaProposta> valoresFaixaFretePeso) {
        
        BigDecimal vlFaixaProgressiva = (BigDecimal)getDefauldValueFromMap(parcela, "VL_FAIXA_PROGRESSIVA", BigDecimal.ZERO);
        String dsParcela = getVarcharI18nValue((String)getDefauldValueFromMap(parcela, "NM_PARCELA", ""));
        dto.setDsParcela(dsParcela + ": " + vlFaixaProgressiva.toPlainString());
        dto.setOrder(ORDER_FAIXA_PROGRESSIVA);
        
        if (destinoProposta!=null){
            for (ValorFaixaProgressivaProposta valorFaixa:valoresFaixaFretePeso){
                if (vlFaixaProgressiva.equals(valorFaixa.getFaixaProgressivaProposta().getVlFaixa())){
                    setValues(dto, valorFaixa.getTpIndicador(), valorFaixa.getVlFixo(), null, null);
                    dto.setIdValorFaixaProgressiva(valorFaixa.getIdValorFaixaProgressivaProposta());
                    dto.setIdFaixaProgressivaProposta(valorFaixa.getFaixaProgressivaProposta().getIdFaixaProgressivaProposta());
                }
            }
        }else {
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
        }
    }

    private void setValuesForTarifaMinima(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela,
            DestinoProposta destinoProposta) {
        dto.setDsParcela(getVarcharI18nValue(getDefauldValueFromMap(parcela, "NM_PARCELA", "")));
        dto.setOrder(ORDER_TARIFA_MINIMA);
        if (destinoProposta!=null){
            setValues(dto, destinoProposta.getTpIndicadorFreteMinimo(), destinoProposta.getVlFreteMinimo(), null, null);
        }else {
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
        }
        
    }

    private void setValuesForAdValorem1(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela,
            DestinoProposta destinoProposta) {
        dto.setDsParcela(getVarcharI18nValue(getDefauldValueFromMap(parcela, "NM_PARCELA", "")));
        dto.setOrder(ORDER_AD_VALOREM_1);
        if (destinoProposta!=null){
            setValues(dto, destinoProposta.getTpIndicadorAdvalorem(), destinoProposta.getVlAdvalorem(), null, null);
        }else {
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
        }
    }
    
    private void setValuesForAdValorem2(DestinoPropostaParcelaDTO dto, Map<String, Object> parcela,
            DestinoProposta destinoProposta) {
        dto.setDsParcela(getVarcharI18nValue(getDefauldValueFromMap(parcela, "NM_PARCELA", "")));
        dto.setOrder(ORDER_AD_VALOREM_2);
        if (destinoProposta!=null){
            setValues(dto, destinoProposta.getTpIndicadorAdvalorem2(), destinoProposta.getVlAdvalorem2(), null, null);
        }else {
            dto.setTpIndicador(getDefaultDomainValue());
            dto.setVlCalculado(dto.getVlOriginal());
        }
    }
   
    /**
     * Retorna um string removendo os caracteres de internacionalizacao do ADSM utilizando o Locale correto.
     * 
     * @param dataObject String contendo os valores internacionalizados separados por tokens.
     * @return String
     */
    private String getVarcharI18nValue(Object dataObject){
        return VarcharI18nUtil.createVarcharI18n(dataObject).getValue();
    }
    
    private DomainValue getDefaultDomainValue(){
        return getDomainValue("T", "Tabela");
    }
    
    /**
     * Retorna um novo domainValue para evitar erro de Lazy na camada rest.
     * 
     * @param domainValue
     * @return
     */
    private DomainValue getNewDomainValue(DomainValue domainValue){
        return getDomainValue(domainValue.getValue(), domainValue.getDescriptionAsString());
    }
    
    private DomainValue getDomainValue(String value, String description){
        DomainValue d = new DomainValue();
        d.setValue(value);
        d.setDescription(new VarcharI18n(description));
        return d;
    }
    
    @POST
    @Path("findAeroportoAtendeCliente")
    public Response findAeroportoAtendeCliente(Long idCliente){
        AeroportoSuggestDTO dto = null;
        Aeroporto aeroporto = aeroportoService.findAeroportoAtendeCliente(idCliente);
        if (aeroporto != null){
            dto = new AeroportoSuggestDTO(aeroporto.getIdAeroporto(), aeroporto.getSgAeroporto(), aeroporto.getPessoa().getNmPessoa());
        }
        return Response.ok(dto).build();
    }
	
}
