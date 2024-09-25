package com.mercurio.lms.franqueado.swt.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.AnexoLancamentoFranqueado;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.service.AnexoLancamentoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.ContaContabilFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FranquiaService;
import com.mercurio.lms.franqueados.model.service.LancamentoFranqueadoService;
import com.mercurio.lms.util.ArquivoUtils;


public class ManterLancamentoFranqueadoAction extends MasterDetailAction{

	private static final String ANEXOS_LANCAMENTO_FRANQUEADO = "listaAnexos";

	private Logger log = LogManager.getLogger(this.getClass());
	private ContaContabilFranqueadoService contaContabilFranqueadoService;
	private FranquiaService franquiaService;
	private AnexoLancamentoFranqueadoService anexoLancamentoFranqueadoService;
	private ParametroGeralService parametroGeralService;
	
	@Override
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		/**
		 * Declaracao da classe pai
		 */		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(LancamentoFranqueado.class,true);
		

		ItemListConfig itemInit = new ItemListConfig() {

			private static final long serialVersionUID = 1L;
			
			@Override
			public List initialize(Long masterId, Map parameters) {
				return anexoLancamentoFranqueadoService.findByLancamentoFrq(masterId);			
			}

			@Override
			public Integer getRowCount(Long masterId, Map parameters) {
				return anexoLancamentoFranqueadoService.findByLancamentoFrq(masterId).size();
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap map = new TypedFlatMap(parameters);
				AnexoLancamentoFranqueado anexo = (AnexoLancamentoFranqueado) bean;
				
				anexo.setIdAnexoLancamentoFrq(map.getLong("idAnexoLancamentoFrq"));
	
				anexo.setDsAnexo(map.getString("dsAnexo"));

				Long idMaster = map.getLong("idLancamentoFrq")!=null?map.getLong("idLancamentoFrq"):map.getLong("masterId");
				if(idMaster != null){
					LancamentoFranqueado lancamento = getService().findById(idMaster);
					anexo.setLancamento(lancamento);
				}
				
				try {
					anexo.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
				} catch (IOException e) {
					log.error(e);
					throw new InfrastructureException(e.getMessage());
				}
				
				return anexo;
			}
		};
		
		
		Comparator anexoComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				AnexoLancamentoFranqueado anexo1 = (AnexoLancamentoFranqueado)obj1;
				AnexoLancamentoFranqueado anexo2 = (AnexoLancamentoFranqueado)obj2;
				
				if (anexo1.getDsAnexo()==null) return -1;
				if (anexo2.getDsAnexo()==null) return 1;
				
        		return anexo1.getDsAnexo().compareTo(anexo2.getDsAnexo());  		
			}    		
    	};

		config.addItemConfig(ANEXOS_LANCAMENTO_FRANQUEADO, AnexoLancamentoFranqueado.class, itemInit, anexoComparator);
		return config;
	}
	
    /**
     *  Executa a importação arquivo CSV
     *  @param parameters
     */
    @SuppressWarnings({ "unchecked" })
    public Map executeImportacao(TypedFlatMap parameters) {
    	
    	List<LancamentoFranqueado> carga = getService().executeImportacao(parameters);
    	Map rowData = new HashMap();
    	rowData.put("listaLancamentos", carga);
 
		return rowData;
    }
	
	public Serializable storeLancamentoFranqueado(TypedFlatMap map) {
		LancamentoFranqueado lancamentoFranqueado = new LancamentoFranqueado();
		lancamentoFranqueado.setIdLancamentoFrq(map.getLong("idLancamentoFrq"));
		
		Franquia franquia = new Franquia();
		ContaContabilFranqueado contaContabilFranqueado = new ContaContabilFranqueado();
		
		franquia = franquiaService.findById(map.getLong("idFranquia"));
		contaContabilFranqueado = contaContabilFranqueadoService.findById(map.getLong("idContaContabilFrq"));
				
		lancamentoFranqueado.setCdDoctoInternacional(map.getShort("cdDoctoInternacional"));
		lancamentoFranqueado.setContaContabilFranqueado(contaContabilFranqueado);
		lancamentoFranqueado.setDtCompetencia(map.getYearMonthDay("dtCompetencia"));
		lancamentoFranqueado.setFranquia(franquia);
		lancamentoFranqueado.setNrDoctoInternacional(map.getInteger("nrDoctoInternacional"));
		lancamentoFranqueado.setObLancamento(map.getString("obLancamento"));
		lancamentoFranqueado.setSgDoctoInternacional(map.getString("sgDoctoInternacional"));
		lancamentoFranqueado.setVlLancamento(map.getBigDecimal("vlLancamento"));
		lancamentoFranqueado.setNrParcelas(map.getInteger("nrParcelas"));
		lancamentoFranqueado.setDsLancamento(map.getString("dsLancamento"));
		
    	return getService().store(lancamentoFranqueado);
	}
	
	
	public Object findByIdAnexoLancamentoFrq(MasterDetailKey key) { 
		return (AnexoLancamentoFranqueado)findItemById(key, ANEXOS_LANCAMENTO_FRANQUEADO);
	}
	
	public TypedFlatMap findById(Long id) {
		if(id!=null){
			LancamentoFranqueado lancamentoFranqueado = getService().findById(id);
		
			putMasterInSession(lancamentoFranqueado);
		
			return mountLancamentoMap(lancamentoFranqueado);
		}
		return new TypedFlatMap();
	}

	private boolean isDesabilitarCampos(LancamentoFranqueado lancamentoFranqueado) {
		return getService().isDesabilitarCampos(lancamentoFranqueado);
	}

	public TypedFlatMap store(TypedFlatMap map) {
		Long idLancamento = map.getLong("idLancamentoFrq");
		
		MasterEntry entry = getMasterFromSession(idLancamento, false);
		LancamentoFranqueado lancamento;
		
		if(idLancamento != null){
			lancamento = (LancamentoFranqueado) entry.getMaster();
			lancamento =  mountLancamento(map,lancamento);
		} else {
			lancamento = mountLancamento(map);
		}
		
		ItemList listaAnexos = entry.getItems(ANEXOS_LANCAMENTO_FRANQUEADO);
		List newers = listaAnexos.getNewOrModifiedItems();
		List removed = listaAnexos.getRemovedItems();
		
		LancamentoFranqueado lancamentoFranqueado = (LancamentoFranqueado) getService().store(lancamento,newers,removed);
		
		TypedFlatMap result = new TypedFlatMap();
		if(lancamentoFranqueado != null){
			result = mountLancamentoMap(lancamentoFranqueado);
			loadItemList(idLancamento, ANEXOS_LANCAMENTO_FRANQUEADO, result);
			
			putMasterInSession(lancamentoFranqueado);
		}
		
		return result;
	}

	public void removeById(Long id) {
		if(id != null)
			getService().removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getService().removeByIds(ids);
	}
	
	public List<ContaContabilFranqueado> findContaContabilFranqueado() {
		return contaContabilFranqueadoService.findByVigencia(new YearMonthDay());
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeAnexos(List ids){
		List<Throwable> exceptions = new ArrayList<Throwable>();
		List removable = new ArrayList<Long>();
		for (Object idO : ids) {
			Long id = (Long)idO;
			if(id!=null && id.intValue() >0){
				AnexoLancamentoFranqueado anexo = anexoLancamentoFranqueadoService.findById(id);
				if(anexo != null){
					LancamentoFranqueado lancamento = getService().findById(anexo.getLancamento().getIdLancamentoFrq());
	
					YearMonthDay competenciaAtual = new YearMonthDay();
					
					int competenciaMonth = lancamento.getDtCompetencia().getMonthOfYear();
					if(competenciaMonth < competenciaAtual.getMonthOfYear()){
						exceptions.add(new BusinessException("LMS-46102",new Object[]{anexo.getDsAnexo()}));
						continue;
					}
					
					ContaContabilFranqueado conta = contaContabilFranqueadoService.findById(lancamento.getContaContabilFranqueado().getIdContaContabilFrq());
	
					if(!conta.getBlPermiteAlteracao()){
						exceptions.add(new BusinessException("LMS-46104",new Object[]{conta.getDsContaContabil()}));
						continue;
					}
				
					removable.add(id);
				}
			} else {
				removable.add(id);
			}
			
			
		}
		
		if(exceptions.isEmpty()){
			removeItemByIds(ids, ANEXOS_LANCAMENTO_FRANQUEADO);
		} else {
			throw new BusinessException(exceptions);
		}
	}
	
	public Serializable insertAnexos(TypedFlatMap parameters){
		Long masterId = parameters.getLong("masterId");
		
		if(masterId == null || masterId == 0){
			return saveItemInstance(parameters, ANEXOS_LANCAMENTO_FRANQUEADO);
		}
	
		LancamentoFranqueado lancamento = getService().findById(masterId);
		
		YearMonthDay competenciaAtual = new YearMonthDay();
		
		int competenciaMonth = lancamento.getDtCompetencia().getMonthOfYear();
		if(competenciaMonth < competenciaAtual.getMonthOfYear()){
			throw new BusinessException("LMS-46102");
		}
		
		ContaContabilFranqueado conta = contaContabilFranqueadoService.findById((Long)lancamento.getContaContabilFranqueado().getIdContaContabilFrq());

		if(!conta.getBlPermiteAlteracao()){
			throw new BusinessException("LMS-46104");
		}

		return saveItemInstance(parameters, ANEXOS_LANCAMENTO_FRANQUEADO);
	} 
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedAnexos(TypedFlatMap parameters) {
		ResultSetPage rs  = (ResultSetPage) findPaginatedItemList(parameters, ANEXOS_LANCAMENTO_FRANQUEADO);
		
		FilterResultSetPage filter = new FilterResultSetPage(rs) {
			public Map filterItem(Object item) {		 	
				AnexoLancamentoFranqueado anexo = (AnexoLancamentoFranqueado) item;
				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("idAnexoLancamentoFrq", anexo.getIdAnexoLancamentoFrq());
				if(anexo.getDcArquivo() != null){
					tfm.put("nmArquivo",ArquivoUtils.getNomeArquivo(anexo.getDcArquivo()));
				}
				tfm.put("dsAnexo",anexo.getDsAnexo());
				tfm.put("dcArquivo",anexo.getDcArquivo());
				
				if(anexo.getIdAnexoLancamentoFrq() == null ||  anexo.getIdAnexoLancamentoFrq() < 0) {
					tfm.put("arquivo", false);
				}
				
				
				return tfm;
		 	};
		 };
		
		return (ResultSetPage) filter.doFilter();
	}
	
	public Integer getRowCountAnexos(TypedFlatMap parameters) {
		return getRowCountItemList(parameters, ANEXOS_LANCAMENTO_FRANQUEADO);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map executeDownloadArquivo(TypedFlatMap parameters) {
		Long idAnexoLancamentoFranqueado = parameters.getLong("idAnexoLancamentoFrq");
		List anexos = null;
		
		Map criteria = new HashMap();
		if(idAnexoLancamentoFranqueado != null) {
			criteria.put("idAnexoLancamentoFrq", idAnexoLancamentoFranqueado);
			anexos = anexoLancamentoFranqueadoService.find(criteria);
		}
		
		Map retorno = new HashMap();
		if(anexos != null && !anexos.isEmpty()) {
			retorno.put("dcArquivo", Base64Util.encode(((AnexoLancamentoFranqueado)anexos.get(0)).getDcArquivo()));
		}
		
		return retorno;
	}

	private TypedFlatMap mountLancamentoMap(LancamentoFranqueado lancamentoFranqueado) {
		TypedFlatMap retorno = new TypedFlatMap();
		if(lancamentoFranqueado != null){
			retorno.put("idLancamentoFrq", lancamentoFranqueado.getIdLancamentoFrq());
			if(lancamentoFranqueado.getFranquia() != null){
				retorno.put("idFilial", lancamentoFranqueado.getFranquia().getIdFranquia());
	
				if(lancamentoFranqueado.getFranquia().getFilial() != null){
					retorno.put("sgFilial", lancamentoFranqueado.getFranquia().getFilial().getSgFilial());
					if(lancamentoFranqueado.getFranquia().getFilial().getPessoa() != null){
						retorno.put("nmFantasia", lancamentoFranqueado.getFranquia().getFilial().getPessoa().getNmFantasia());
					}
				}
			}
			
			if(lancamentoFranqueado.getContaContabilFranqueado() != null){
				retorno.put("idContaContabilFrq", lancamentoFranqueado.getContaContabilFranqueado().getIdContaContabilFrq());
				retorno.put("dsContaContabil", lancamentoFranqueado.getContaContabilFranqueado().getDsContaContabil());
			}
			
			retorno.put("sgDoctoInternacional", lancamentoFranqueado.getSgDoctoInternacional());
			retorno.put("cdDoctoInternacional", lancamentoFranqueado.getCdDoctoInternacional());
			retorno.put("nrDoctoInternacional", lancamentoFranqueado.getNrDoctoInternacional());
			retorno.put("vlLancamento", lancamentoFranqueado.getVlLancamento());
			retorno.put("tpSituacaoAprovacao", lancamentoFranqueado.getTpSituacaoPendencia().getValue());
			retorno.put("obLancamento", lancamentoFranqueado.getObLancamento());
			retorno.put("dsLancamento", lancamentoFranqueado.getDsLancamento());
			retorno.put("desabilitarCampos", isDesabilitarCampos(lancamentoFranqueado));
			retorno.put("dtCompetencia", lancamentoFranqueado.getDtCompetencia());
			retorno.put("nrParcelas", lancamentoFranqueado.getNrParcelas());
			
			retorno.put("idPendencia", lancamentoFranqueado.getPendencia() != null ? lancamentoFranqueado.getPendencia().getIdPendencia() : null);
			
		}
		return retorno;
	}
	
	private LancamentoFranqueado mountLancamento(TypedFlatMap map) {
		return mountLancamento(map,null);
	}
	
	private LancamentoFranqueado mountLancamento(TypedFlatMap map, LancamentoFranqueado lancamentoFranqueado) {
		LancamentoFranqueado lancamento = lancamentoFranqueado==null?new LancamentoFranqueado():lancamentoFranqueado;
		
		if(map != null && !map.isEmpty()){
			if(map.getLong("idContaContabilFrq")!= null){
				ContaContabilFranqueado conta = contaContabilFranqueadoService.findById(map.getLong("idContaContabilFrq"));
				lancamento.setContaContabilFranqueado(conta);
			}

			if(map.getLong("idFranquia")!=null){
				Franquia franquia = franquiaService.findById(map.getLong("idFranquia"));
				lancamento.setFranquia(franquia);
			}
			
			if(map.getYearMonthDay("dtCompetencia") !=null){
				lancamento.setDtCompetencia(map.getYearMonthDay("dtCompetencia"));
			}
			
			if(map.getYearMonthDay("dtCompetencia") !=null){
				lancamento.setDtCompetencia(map.getYearMonthDay("dtCompetencia"));
			}
			
			if(map.getString("obLancamento") !=null){
				lancamento.setObLancamento(map.getString("obLancamento"));
			}
			
			if(map.getString("dsLancamento") !=null){
				lancamento.setDsLancamento(map.getString("dsLancamento"));
			}
			
			if(map.getString("dsLancamento") !=null){
				lancamento.setDsLancamento(map.getString("dsLancamento"));
			}
			
			if(map.getString("sgDoctoInternacional") !=null){
				lancamento.setSgDoctoInternacional(map.getString("sgDoctoInternacional"));
			}
			
			if(map.getString("nrDoctoInternacional") !=null){
				lancamento.setNrDoctoInternacional(map.getInteger("nrDoctoInternacional"));
			}
			
			if(map.getString("cdDoctoInternacional") !=null){
				lancamento.setCdDoctoInternacional(map.getShort("cdDoctoInternacional"));
			}
			
			if(map.getBigDecimal("vlLancamento") !=null){
				lancamento.setVlLancamento(map.getBigDecimal("vlLancamento"));
			}
			
			if(map.getBigDecimal("nrParcelas") !=null){
				lancamento.setNrParcelas(map.getInteger("nrParcelas"));
			}
		}

		
		return lancamento;
	}

	public List getComboBoxNrParcelas() {
		ArrayList list = new ArrayList();
		
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("NR_PARCELAS_ANTECIPACAO_CREDITO", false);
		if (parametroGeral != null && parametroGeral.getDsConteudo() != null ) {
			String [] dsConteudo = parametroGeral.getDsConteudo().split(";");
			for (String vlConteudo : dsConteudo) {
				Map<String, Object> parcela = new HashMap<String, Object>();
				parcela.put("nrParcelas", vlConteudo);
				list.add(parcela);
			}
		}
		
    	return list;
    }
	
	public Boolean hasParcelas(TypedFlatMap map){
		if(!map.isEmpty() && map.containsKey("idContaContabil")){
			ContaContabilFranqueado conta = contaContabilFranqueadoService.findById(map.getLong("idContaContabil"));
				
			if(conta.getTpContaContabil().getValue().equals(ConstantesFranqueado.CONTA_CONTABIL_CREDITO_ANTECIPACAO)){
				return true;
			}
		}
		return false;
	}
    
	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setContaContabilFranqueadoService(
			ContaContabilFranqueadoService contaContabilFranqueadoService) {
		this.contaContabilFranqueadoService = contaContabilFranqueadoService;
	}
	
	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}

	public void setAnexoLancamentoFranqueadoService(
			AnexoLancamentoFranqueadoService anexoLancamentoFranqueadoService) {
		this.anexoLancamentoFranqueadoService = anexoLancamentoFranqueadoService;
	}

	public LancamentoFranqueadoService getService() {
		return (LancamentoFranqueadoService) this.getMasterService();
	}

	public void setLancamentoFranqueadoService(
			LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.setMasterService(lancamentoFranqueadoService);
	}

}
