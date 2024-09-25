package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ComiteNivelMarkup;
import com.mercurio.lms.vendas.model.dao.ComiteNivelMarkupDAO;
import com.mercurio.lms.workflow.model.Comite;
import com.mercurio.lms.workflow.model.EventoWorkflow;


public class ComiteNivelMarkupService extends CrudService<ComiteNivelMarkup, Long> {
	
	private TabelaPrecoService tabelaPrecoService;
	
		
	 @Override
	public ResultSetPage findPaginated(Map criteria) {
    	FilterResultSetPage filterRs = new FilterResultSetPage(super.findPaginated(criteria)) {
			@Override
			public Map filterItem(Object item) {
				ComiteNivelMarkup cnm = (ComiteNivelMarkup)item;
				TypedFlatMap retorno = new TypedFlatMap();
				retorno.put("idComiteNivelMarkup", cnm.getIdComiteNivelMarkup());
		        retorno.put("pcVariacao", cnm.getPcVariacao());
		        ParcelaPreco pp = cnm.getParcelaPreco();
		        retorno.put("parcelaPreco.nmParcelaPreco", pp.getNmParcelaPreco());
		        retorno.put("parcelaPreco.dsParcelaPreco", pp.getDsParcelaPreco());
		        EventoWorkflow ew = cnm.getEventoWorkflow();
		        if (ew != null && ew.getTipoEvento() != null) {
		        	retorno.put("eventoWorkflow.dsTipoEvento", ew.getTipoEvento().getDsTipoEvento());
				}
		        retorno.put("tpSituacao", this.getAtivo(cnm.getDtVigenciaInicial(), cnm.getDtVigenciaFinal()));
				return retorno;
			}

			private DomainValue getAtivo(YearMonthDay dtInicial, YearMonthDay dtFinal){
				YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
				String description = "Inativo";
				
				if((dtAtual.isBefore(dtFinal) && dtAtual.isAfter(dtInicial)) || (dtAtual.isEqual(dtFinal) || dtAtual.isEqual(dtInicial))){
					description = "Ativo";
				} 
				
				DomainValue retorno = new DomainValue();
				retorno.setDescription(new VarcharI18n(description));
				return retorno;
			}
    	};
               
        return (ResultSetPage) filterRs.doFilter();
	}

	 public List<Map<String, Object>> findNiveisMarkupVigentesNaoIsentoByIdTabelaPreco(Long idTabelaPreco){
		 return getComiteNivelMarkupDAO().findNiveisMarkupVigentesNaoIsentoByIdTabelaPreco(idTabelaPreco);
	 }
	 
	/**
	 * Recupera uma instância de <code>ComiteNivelMarkup</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	@Override
	public ComiteNivelMarkup findById(java.lang.Long id) {
		return (ComiteNivelMarkup) super.findById(id); 
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(ComiteNivelMarkup bean) {
		if (this.existePercentualParaVigenciaInformada(bean)) {
			throw new BusinessException("LMS-01235");
		}
		return super.store(bean);
	}

	public boolean existePercentualParaVigenciaInformada(ComiteNivelMarkup bean) {

		List<ComiteNivelMarkup> comites = this.getComiteNivelMarkupDAO().findByTabelaParcelaComiteVigencia(bean.getIdTabelaPreco(),
						bean.getIdParcelaPreco(), bean.getIdComite(),
						bean.getDtVigenciaInicial(), bean.getDtVigenciaFinal());
		if (CollectionUtils.isEmpty(comites)) {
			return false;
		}
		for (ComiteNivelMarkup comite : comites) {
			if (comite != null && !comite.getIdComiteNivelMarkup().equals(bean.getIdComiteNivelMarkup())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setComiteNivelMarkupDAO(ComiteNivelMarkupDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ComiteNivelMarkupDAO getComiteNivelMarkupDAO() {
		return (ComiteNivelMarkupDAO) getDao();
	}
	
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	public List<ComiteNivelMarkup> findByTabelaParcelaComiteVigencia (Long idTabelaPreco, Long idParcelaPreco, Long idEventoWorkflow,
																YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return this.getComiteNivelMarkupDAO().findByTabelaParcelaComiteVigencia(idTabelaPreco, idParcelaPreco, idEventoWorkflow, dtVigenciaInicial, dtVigenciaFinal);
	}
	
	public TypedFlatMap storeParaTodasParcelas(TypedFlatMap parametros){
		TypedFlatMap result = new TypedFlatMap();
		
		Long idTabelaPreco = parametros.getLong("tabelaPreco.idTabelaPreco");
		Long idEventoWorkflow = parametros.getLong("eventoWorkflow.idEventoWorkflow");
		BigDecimal pcVariacao = parametros.getBigDecimal("pcVariacao");
		YearMonthDay dtVigenciaInicial = parametros.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal = parametros.getYearMonthDay("dtVigenciaFinal");
		
		List<ComiteNivelMarkup> comitesMarkupNovos = this.criarComiteNivelMarkupByParcelas(idTabelaPreco, idEventoWorkflow, pcVariacao, dtVigenciaInicial, dtVigenciaFinal);
		

		List<ComiteNivelMarkup> comitesMarkupBanco = this.findByTabelaParcelaComiteVigencia(idTabelaPreco, null, idEventoWorkflow, dtVigenciaInicial, dtVigenciaFinal);
		if (CollectionUtils.isNotEmpty(comitesMarkupNovos)) {
			for (ComiteNivelMarkup novo : comitesMarkupNovos) {
				if(comitesMarkupBanco.contains(novo)){
					throw new BusinessException("LMS-01235");
				}
			}
		}
			
		
		this.storeAll(comitesMarkupNovos);
		return result;
	}

	private List<ComiteNivelMarkup> criarComiteNivelMarkupByParcelas(Long idTabelaPreco, Long idEventoWorkflow, BigDecimal pcVariacao,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		TabelaPreco tabelaPreco = new TabelaPreco();
		tabelaPreco.setIdTabelaPreco(idTabelaPreco);
		EventoWorkflow eventoWorkflow = new EventoWorkflow();
		eventoWorkflow.setIdEventoWorkflow(idEventoWorkflow);
		
		List<ParcelaPreco> listParcelas = this.tabelaPrecoService.findParcelasByTabelaPreco(idTabelaPreco);
		
		List<ComiteNivelMarkup> listRetorno = new ArrayList<ComiteNivelMarkup>();
		for (ParcelaPreco parcelaPreco : listParcelas) {
			ComiteNivelMarkup comiteNivelMarkup = new ComiteNivelMarkup();
			comiteNivelMarkup.setTabelaPreco(tabelaPreco);
			comiteNivelMarkup.setEventoWorkflow(eventoWorkflow);
			comiteNivelMarkup.setPcVariacao(pcVariacao);
			comiteNivelMarkup.setDtVigenciaInicial(dtVigenciaInicial);
			comiteNivelMarkup.setDtVigenciaFinal(dtVigenciaFinal);
			
			comiteNivelMarkup.setParcelaPreco(parcelaPreco);
			listRetorno.add(comiteNivelMarkup);
		}
		
		return listRetorno;
	}
	
}
