package com.mercurio.lms.sgr.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.FaixaValorNaturezaImpedida;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;
import com.mercurio.lms.sgr.model.service.ExigenciaFaixaValorService;
import com.mercurio.lms.sgr.model.service.ExigenciaGerRiscoService;
import com.mercurio.lms.sgr.model.service.FaixaDeValorService;
import com.mercurio.lms.sgr.model.service.FaixaValorNaturezaImpedidaService;
import com.mercurio.lms.sgr.model.service.TipoExigenciaGerRiscoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.manterProcedimentosFaixasValoresAction"
 */

public class ManterProcedimentosFaixasValoresAction extends CrudAction {
	
	private static final String BL_AREA_RISCO = "blAreaRisco";
	private static final String BL_EXIGE_QUANTIDADE = "blExigeQuantidade";
	private static final String DS_COMPLETA = "dsCompleta";
	private static final String DS_RESUMIDA = "dsResumida";
	private static final String EXIGENCIA_GER_RISCO = "exigenciaGerRisco";
	private static final String EXIGENCIAS_GER_RISCO = "exigenciasGerRisco";
	private static final String EXIGENCIA_GER_RISCO_ID_EXIGENCIA_GER_RISCO = "exigenciaGerRisco.idExigenciaGerRisco";
	private static final String FAIXA_DE_VALOR_ID_FAIXA_DE_VALOR = "faixaDeValor.idFaixaDeValor";
	private static final String FILIAL_INICIO_ID_FILIAL = "filialInicio.idFilial";
	private static final String ID_EXIGENCIA_FAIXA_VALOR = "idExigenciaFaixaValor";
	private static final String ID_EXIGENCIA_GER_RISCO = "idExigenciaGerRisco";
	private static final String ID_FAIXA_VALOR_NATUREZA = "idFaixaValorNatureza";
	private static final String ID_FILIAL = "idFilial";
	private static final String ID_TIPO_EXIGENCIA_GER_RISCO = "idTipoExigenciaGerRisco";
	private static final String NM_FANTASIA = "nmFantasia";
	private static final String QT_EXIGIDA = "qtExigida";
	private static final String SG_FILIAL = "sgFilial";
	private static final String TIPO_EXIGENCIA_GER_RISCO = "tipoExigenciaGerRisco";
	private static final String TIPO_EXIGENCIA_GER_RISCO_ID_TIPO_EXIGENCIA_GER_RISCO = "tipoExigenciaGerRisco.idTipoExigenciaGerRisco";
	private static final String TP_EXIGENCIA = "tpExigencia";
	private static final String VL_KM_FRANQUIA = "vlKmFranquia";

	private ExigenciaGerRiscoService exigenciaGerRiscoService;
	private TipoExigenciaGerRiscoService tipoExigenciaGerRiscoService;
	private ExigenciaFaixaValorService exigenciaFaixaValorService;
	private FilialService filialService;
	private FaixaDeValorService faixaDeValorService;
	private FaixaValorNaturezaImpedidaService faixaValorNaturezaImpedidaService;
	private NaturezaProdutoService naturezaProdutoService;
	
	public ExigenciaGerRiscoService getExigenciaGerRiscoService() {
		return exigenciaGerRiscoService;
	}

	public void setExigenciaGerRiscoService(
			ExigenciaGerRiscoService exigenciaGerRiscoService) {
		this.exigenciaGerRiscoService = exigenciaGerRiscoService;
	}
	
	public void setTipoExigenciaGerRiscoService(TipoExigenciaGerRiscoService tipoExigenciaGerRiscoService) {
		this.tipoExigenciaGerRiscoService = tipoExigenciaGerRiscoService;
	}
	
	public void setExigenciaFaixaValorService(ExigenciaFaixaValorService exigenciaFaixaValorService) {
		this.exigenciaFaixaValorService = exigenciaFaixaValorService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setFaixaDeValorService(FaixaDeValorService faixaDeValorService) {
		this.faixaDeValorService = faixaDeValorService;
	}
	
	public void setFaixaDeValor(FaixaDeValorService faixaDeValorService) {
		this.defaultService = faixaDeValorService;
	}
    
	public void setFaixaValorNaturezaImpedidaService(FaixaValorNaturezaImpedidaService faixaValorNaturezaImpedidaService) {
		this.faixaValorNaturezaImpedidaService = faixaValorNaturezaImpedidaService;
	}

	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

	public void removeById(java.lang.Long id) {
        ((FaixaDeValorService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((FaixaDeValorService)defaultService).removeByIds(ids);
    }

    public FaixaDeValor findById(java.lang.Long id) {
    	return ((FaixaDeValorService)defaultService).findById(id);
    }
    
	/**
	 * Realiza uma pesquisa de Exig�ncias baseada no Tipo de Exig�ncia e retorna
     * para a tela um map com a lista de exig�ncias  
     * utilizado em listbox
	 */
	public Map findExigenciasByTipoExigenciaOrdenadorPorDescricao(Map criteria) {
	    // busca a lista de exig�ncias
        List result = exigenciaGerRiscoService.findOrdenadoPorNivel(criteria);
        Map map = null;
        List listRetorno = new ArrayList();
        for (Iterator iter = result.iterator(); iter.hasNext();) {
			ExigenciaGerRisco exigenciaGerRisco = (ExigenciaGerRisco) iter.next();
			map = new HashMap();
			map.put(ID_EXIGENCIA_GER_RISCO, exigenciaGerRisco.getIdExigenciaGerRisco());
			map.put(DS_RESUMIDA, exigenciaGerRisco.getDsResumida());
			map.put(DS_COMPLETA, exigenciaGerRisco.getDsCompleta());
			listRetorno.add(map);
		}
        Map mapRetorno = new HashMap();        
        mapRetorno.put(EXIGENCIA_GER_RISCO, listRetorno);
        return mapRetorno;
	}

	/**
	 * LMS-6848 - Busca contagem de {@link ExigenciaFaixaValor} para determinada
	 * {@link FaixaDeValor}.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"faixaDeValor.idFaixaDeValor"</tt>
	 * @return Contagem de {@link ExigenciaFaixaValor} para determinada
	 *         {@link FaixaDeValor}
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCountExigenciaFaixaValor(Map criteria) {
		return exigenciaFaixaValorService.getRowCount(criteria);
	}

	/**
	 * LMS-6848 - Busca p�gina de registros de {@link ExigenciaFaixaValor} para
	 * determinada {@link FaixaDeValor}.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"faixaDeValor.idFaixaDeValor"</tt>
	 * @return Registros de {@link ExigenciaFaixaValor} para determinada
	 *         {@link FaixaDeValor}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage<ExigenciaFaixaValor> findPaginatedExigenciaFaixaValor(Map criteria) {
		return exigenciaFaixaValorService.findPaginated(criteria);
	}

	/**
	 * LMS-6848 - Busca registro de {@link ExigenciaFaixaValor} incluindo
	 * refer�ncias para {@link ExigenciaGerRisco}, {@link TipoExigenciaGerRisco}
	 * e {@link Filial}, retornando mapa com dados para preenchimento da p�gina.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"idExigenciaFaixaValor"</tt>
	 * @return Mapa com registro de {@link ExigenciaFaixaValor} e
	 *         relacionamentos
	 */
	public Map<String, Object> findExigenciaFaixaValor(Map<String, Object> criteria) {
		Long idExigenciaFaixaValor = MapUtils.getLong(criteria, ID_EXIGENCIA_FAIXA_VALOR);
		ExigenciaFaixaValor exigenciaFaixaValor = exigenciaFaixaValorService.findById(idExigenciaFaixaValor);
		ExigenciaGerRisco exigenciaGerRisco = exigenciaFaixaValor.getExigenciaGerRisco();
		TipoExigenciaGerRisco tipoExigenciaGerRisco = exigenciaGerRisco.getTipoExigenciaGerRisco();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(ID_EXIGENCIA_FAIXA_VALOR, idExigenciaFaixaValor);
		result.put(ID_TIPO_EXIGENCIA_GER_RISCO, tipoExigenciaGerRisco.getIdTipoExigenciaGerRisco());
		result.put(ID_EXIGENCIA_GER_RISCO, exigenciaGerRisco.getIdExigenciaGerRisco());
		Integer qtExigida = exigenciaFaixaValor.getQtExigida();
		if (qtExigida != null) {
			result.put(QT_EXIGIDA, qtExigida);
		}
		Filial filialInicio = exigenciaFaixaValor.getFilialInicio();
		if (filialInicio != null) {
			Long idFilial = filialInicio.getIdFilial();
			filialInicio = filialService.findById(idFilial);
			result.put(ID_FILIAL, idFilial);
			result.put(SG_FILIAL, filialInicio.getSgFilial());
			result.put(NM_FANTASIA, filialInicio.getPessoa().getNmFantasia());
		}
		Integer vlKmFranquia = exigenciaFaixaValor.getVlKmFranquia();
		if (vlKmFranquia != null) {
			result.put(VL_KM_FRANQUIA, vlKmFranquia);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ID_TIPO_EXIGENCIA_GER_RISCO, tipoExigenciaGerRisco.getIdTipoExigenciaGerRisco());
		result.put(TIPO_EXIGENCIA_GER_RISCO, tipoExigenciaGerRiscoOnChange(map));
		return result;
	}

	/**
	 * LMS-6848 - Trata altera��o na refer�ncia de {@link TipoExigenciaGerRisco}
	 * no registro de {@link ExigenciaFaixaValor}, retornando atributos para
	 * controle da tela e registros de {@link ExigenciaGerRisco} relacionados
	 * para preenchimento de op��es na combobox.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"idTipoExigenciaGerRisco"</tt>
	 * @return Mapa com atributos de {@link TipoExigenciaGerRisco} e lista de
	 *         {@link ExigenciaGerRisco}
	 */
	public Map<String, Object> tipoExigenciaGerRiscoOnChange(Map<String, Object> criteria) {
		Long idTipoExigenciaGerRisco = MapUtils.getLong(criteria, ID_TIPO_EXIGENCIA_GER_RISCO);
		TipoExigenciaGerRisco tipoExigenciaGerRisco = tipoExigenciaGerRiscoService.findById(idTipoExigenciaGerRisco);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(TP_EXIGENCIA, tipoExigenciaGerRisco.getTpExigencia().getValue());
		result.put(BL_EXIGE_QUANTIDADE, tipoExigenciaGerRisco.getBlExigeQuantidade() ? true : null);

        List<TypedFlatMap> exigenciasGerRisco = new ArrayList<TypedFlatMap>();
        criteria.put(TIPO_EXIGENCIA_GER_RISCO_ID_TIPO_EXIGENCIA_GER_RISCO, idTipoExigenciaGerRisco);
        @SuppressWarnings("unchecked")
		List<ExigenciaGerRisco> list = exigenciaGerRiscoService.findOrdenadoPorNivel(criteria);
		for (ExigenciaGerRisco exigenciaGerRisco : list) {
        	TypedFlatMap map = new TypedFlatMap();
        	map.put(ID_EXIGENCIA_GER_RISCO, exigenciaGerRisco.getIdExigenciaGerRisco());
        	map.put(DS_RESUMIDA, exigenciaGerRisco.getDsResumida());
        	map.put(DS_COMPLETA, exigenciaGerRisco.getDsCompleta());
        	map.put(BL_AREA_RISCO, exigenciaGerRisco.getBlAreaRisco() ? true : null);
        	exigenciasGerRisco.add(map);
        }
        result.put(EXIGENCIAS_GER_RISCO, exigenciasGerRisco);
		return result;
	}

	/**
	 * LMS-6848 - Persiste {@link ExigenciaFaixaValor} com dados vindo do
	 * formul�rio, incluindo refer�ncias para {@link FaixaDeValor},
	 * {@link ExigenciaGerRisco} e {@link Filial}.
	 * 
	 * @param exigenciaFaixaValorMap
	 *            Mapa com dados de {@link ExigenciaFaixaValor}
	 */
	public void storeExigenciaFaixaValor(TypedFlatMap exigenciaFaixaValorMap) {
		Long idExigenciaFaixaValor = exigenciaFaixaValorMap.getLong(ID_EXIGENCIA_FAIXA_VALOR);
		Long idFaixaDeValor = exigenciaFaixaValorMap.getLong(FAIXA_DE_VALOR_ID_FAIXA_DE_VALOR);
		Long idExigenciaGerRisco = exigenciaFaixaValorMap.getLong(EXIGENCIA_GER_RISCO_ID_EXIGENCIA_GER_RISCO);
		Integer qtExigida = exigenciaFaixaValorMap.getInteger(QT_EXIGIDA);
		Long idFilialInicio = exigenciaFaixaValorMap.getLong(FILIAL_INICIO_ID_FILIAL);
		Integer vlKmFranquia = exigenciaFaixaValorMap.getInteger(VL_KM_FRANQUIA);

		FaixaDeValor faixaDeValor = faixaDeValorService.findById(idFaixaDeValor);
		ExigenciaGerRisco exigenciaGerRisco = exigenciaGerRiscoService.findById(idExigenciaGerRisco);
		Filial filialInicio = idFilialInicio == null ? null : filialService.findById(idFilialInicio);

		ExigenciaFaixaValor exigenciaFaixaValor = new ExigenciaFaixaValor();
		exigenciaFaixaValor.setIdExigenciaFaixaValor(idExigenciaFaixaValor);
		exigenciaFaixaValor.setFaixaDeValor(faixaDeValor);
		exigenciaFaixaValor.setExigenciaGerRisco(exigenciaGerRisco);
		exigenciaFaixaValor.setQtExigida(qtExigida);
		exigenciaFaixaValor.setFilialInicio(filialInicio);
		exigenciaFaixaValor.setVlKmFranquia(vlKmFranquia);
		exigenciaFaixaValorService.store(exigenciaFaixaValor);
	}

	/**
	 * LMS-6848 - Remove da base de dados registros de
	 * {@link ExigenciaFaixaValor} espec�ficos.
	 * 
	 * @param ids
	 *            Lista de id's para remo��o de {@link ExigenciaFaixaValor}
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeExigenciasFaixaValor(List<Long> ids) {
		exigenciaFaixaValorService.removeByIds(ids);
	}

	/**
	 * LMS-6849 - Busca contagem de {@link FaixaValorNaturezaImpedida} para
	 * determinada {@link FaixaDeValor}.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"faixaDeValor.idFaixaDeValor"</tt>
	 * @return Contagem de {@link FaixaValorNaturezaImpedida} para determinada
	 *         {@link FaixaDeValor}
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCountFaixaValorNaturezaImpedida(Map criteria) {
		return faixaValorNaturezaImpedidaService.getRowCount(criteria);
	}

	/**
	 * LMS-6849 - Busca p�gina de registros de
	 * {@link FaixaValorNaturezaImpedida} para determinada {@link FaixaDeValor}.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"faixaDeValor.idFaixaDeValor"</tt>
	 * @return Registros de {@link FaixaValorNaturezaImpedida} para determinada
	 *         {@link FaixaDeValor}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage<FaixaValorNaturezaImpedida> findPaginatedFaixaValorNaturezaImpedida(Map criteria) {
		return faixaValorNaturezaImpedidaService.findPaginated(criteria);
	}

	/**
	 * LMS-6849 - Busca registro de {@link FaixaValorNaturezaImpedida}
	 * retornando mapa com dados para preenchimento da p�gina.
	 * 
	 * @param criteria
	 *            Mapa incluindo <tt>"idFaixaValorNatureza"</tt>
	 * @return Mapa com registro de {@link FaixaValorNaturezaImpedida}
	 */
	public TypedFlatMap findFaixaValorNaturezaImpedida(TypedFlatMap criteria) {
		Long idFaixaValorNatureza = criteria.getLong(ID_FAIXA_VALOR_NATUREZA);
		FaixaValorNaturezaImpedida faixaValorNaturezaImpedida = faixaValorNaturezaImpedidaService.findById(idFaixaValorNatureza);
		TypedFlatMap map = new TypedFlatMap();
		map.put(ID_FAIXA_VALOR_NATUREZA, faixaValorNaturezaImpedida.getIdFaixaValorNatureza());
		map.put("idNaturezaProduto", faixaValorNaturezaImpedida.getNaturezaProduto().getIdNaturezaProduto());
		map.put("vlLimitePermitido", faixaValorNaturezaImpedida.getVlLimitePermitido());
		return map;
	}

	/**
	 * LMS-6849 - Persiste {@link FaixaValorNaturezaImpedida} com dados vindo do
	 * formul�rio, incluindo refer�ncias para {@link FaixaDeValor} e
	 * {@link NaturezaProduto}.
	 * 
	 * @param exigenciaFaixaValorMap
	 *            Mapa com dados de {@link FaixaValorNaturezaImpedida}
	 */
	public void storeFaixaValorNaturezaImpedida(TypedFlatMap faixaValorNaturezaImpedidaMap) {
		Long idFaixaValorNatureza = faixaValorNaturezaImpedidaMap.getLong(ID_FAIXA_VALOR_NATUREZA);
		Long idFaixaDeValor = faixaValorNaturezaImpedidaMap.getLong(FAIXA_DE_VALOR_ID_FAIXA_DE_VALOR);
		Long idNaturezaProduto = faixaValorNaturezaImpedidaMap.getLong("naturezaProduto.idNaturezaProduto");
		BigDecimal vlLimitePermitido = faixaValorNaturezaImpedidaMap.getBigDecimal("vlLimitePermitido");

		FaixaDeValor faixaDeValor = faixaDeValorService.findById(idFaixaDeValor);
		NaturezaProduto naturezaProduto = naturezaProdutoService.findById(idNaturezaProduto);

		FaixaValorNaturezaImpedida faixaValorNaturezaImpedida = new FaixaValorNaturezaImpedida();
		faixaValorNaturezaImpedida.setIdFaixaValorNatureza(idFaixaValorNatureza);
		faixaValorNaturezaImpedida.setFaixaDeValor(faixaDeValor);
		faixaValorNaturezaImpedida.setNaturezaProduto(naturezaProduto);
		faixaValorNaturezaImpedida.setVlLimitePermitido(vlLimitePermitido);
		faixaValorNaturezaImpedidaService.store(faixaValorNaturezaImpedida);
	}

	/**
	 * LMS-6849 - Remove da base de dados registros de
	 * {@link FaixaValorNaturezaImpedida} espec�ficos.
	 * 
	 * @param ids
	 *            Lista de id's para remo��o de
	 *            {@link FaixaValorNaturezaImpedida}
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeFaixaValorNaturezaImpedida(List<Long> ids) {
		faixaValorNaturezaImpedidaService.removeByIds(ids);
	}

}
