package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.RotaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Markup;
import com.mercurio.lms.vendas.model.MarkupFaixaProgressiva;
import com.mercurio.lms.vendas.model.ValorMarkupFaixaProgressiva;
import com.mercurio.lms.vendas.model.ValorMarkupPrecoFrete;
import com.mercurio.lms.vendas.model.dao.MarkupDAO;


public class MarkupService extends CrudService<Markup, Long> {
	
	private static final String ROTA = "Rota ";
	private static final String TARIFA = "Tarifa ";
	
	private RotaPrecoService rotaPrecoService;
	private TarifaPrecoService tarifaPrecoService;
	private ParcelaPrecoService parcelaPrecoService;
	
	/**
	 * Recupera uma instância de <code>Markup</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	@Override
	public Markup findById(java.lang.Long id) {
		return (Markup) super.findById(id); 
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
	public java.io.Serializable store(Markup bean) {
		return super.store(bean);
	}
	
	public void executeValidaMarkupsPrecoFrete(List<ValorMarkupPrecoFrete> listTela, Long idTabelaPreco) {
		Set<ValorMarkupPrecoFrete> listValores = new HashSet<ValorMarkupPrecoFrete>();
		listValores.addAll(listTela);
		List<ValorMarkupPrecoFrete> salvos = this.findValorMarkupPrecoFrete(idTabelaPreco, false, false);
		for (ValorMarkupPrecoFrete markup : salvos) {
			if(!listTela.contains(markup)){
				listValores.add(markup);
			}
		}
		
		//remover da lista o que nao tem valor, pois serao deletados e nao devem participar da validacao
		Set<ValorMarkupPrecoFrete> listValoresRemover = new HashSet<ValorMarkupPrecoFrete>();
		for (ValorMarkupPrecoFrete markup : listValores) {
			if(null == markup.getVlMarkup()){
				listValoresRemover.add(markup);
			}
		}
		if(!listValoresRemover.isEmpty()){
			listValores.removeAll(listValoresRemover);
		}
		
		Map<String, List<Interval>> markupsPorRotaTarifa = preparaIntervalosPrecoFrete(listValores);
		validaVigencia(markupsPorRotaTarifa);
	}
	
	private Map<String, List<Interval>> preparaIntervalosPrecoFrete(Set<ValorMarkupPrecoFrete> list) {
		Map<String, List<Interval>> markupsPorRotaTarifa = new HashMap<String, List<Interval>>();
		for (ValorMarkupPrecoFrete valorMarkup : list) {
			
			String chave = this.obtemChavePrecoFrete(valorMarkup);
					
			if (!markupsPorRotaTarifa.containsKey(chave)) {
				markupsPorRotaTarifa.put(chave, new ArrayList<Interval>());
			}
			markupsPorRotaTarifa.get(chave).add(
					new Interval(
							valorMarkup.getDtVigenciaInicial().toDateTime(new TimeOfDay(0, 0, 0)), 
							valorMarkup.getDtVigenciaFinal() == null ? JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTime(new TimeOfDay(23,59,59)) : valorMarkup.getDtVigenciaFinal().toDateTime(new TimeOfDay(23,59,59))
							));
		}
		return markupsPorRotaTarifa;
	}
	
	private String obtemChavePrecoFrete(ValorMarkupPrecoFrete valorMarkup){
		
		Long idParcela = valorMarkup.getPrecoFrete().getTabelaPrecoParcela().getIdParcelaPreco();
		
		Long idRotaTarifa = valorMarkup.getRotaPreco() != null ? valorMarkup.getRotaPreco().getIdRotaPreco() : null;
		if(idRotaTarifa != null){
			return ROTA+idParcela+";"+idRotaTarifa;
		}else{
			idRotaTarifa = valorMarkup.getTarifaPreco() != null ? valorMarkup.getTarifaPreco().getIdTarifaPreco() : null;
			return TARIFA+idParcela+";"+idRotaTarifa;
		}
	}
	
	public void executeValidaMarkupsMinimoProgressivo(List<MarkupFaixaProgressiva> listTela, Long idTabelaPreco) {
		Set<ValorMarkupFaixaProgressiva> listValores = new HashSet<ValorMarkupFaixaProgressiva>();
		for (MarkupFaixaProgressiva markupFaixaProgressiva : listTela) {
			listValores.addAll(markupFaixaProgressiva.getValoresMarkupFaixaProgressiva());
		}
		List<ValorMarkupFaixaProgressiva> salvos = this.findValorMarkupFaixaProgressiva(idTabelaPreco, false, false);
		for (ValorMarkupFaixaProgressiva markup : salvos) {
			if(!listTela.contains(markup)){
				listValores.add(markup);
			}
		}
		
		//remover da lista o que nao tem valor, pois serao deletados e nao devem participar da validacao
		Set<ValorMarkupFaixaProgressiva> listValoresRemover = new HashSet<ValorMarkupFaixaProgressiva>();
		for (ValorMarkupFaixaProgressiva markup : listValores) {
			if(null == markup.getVlMarkup()){
				listValoresRemover.add(markup);
			}
		}
		if(!listValoresRemover.isEmpty()){
			listValores.removeAll(listValoresRemover);
		}
		
		Map<String, List<Interval>> markupsPorRotaTarifa = preparaIntervalosFaixaProgressiva(listValores);
		validaVigencia(markupsPorRotaTarifa);
	}

	private void validaVigencia(Map<String, List<Interval>> markupsPorRotaTarifa) {
		for (String chave : markupsPorRotaTarifa.keySet()) {
			if(!validaVigencia(markupsPorRotaTarifa.get(chave))){
				
				String param = "";
				String[] chaveTemp = chave.split(";");
				Long idRotaTarifa = Long.valueOf(chaveTemp[1]);
				
				if(chave.contains(ROTA)){
					RotaPreco rotaPreco = rotaPrecoService.findById(idRotaTarifa);
					param = ROTA + rotaPreco.getOrigemString() +" > " + rotaPreco.getDestinoString();
				}else{
					TarifaPreco tarifaPreco = tarifaPrecoService.findById(idRotaTarifa);
					param = TARIFA + tarifaPreco.getCdTarifaPreco();
				}
				
				throw new BusinessException("LMS-46116", new Object[] {param});
			}
		}
	}
	
	private Map<String, List<Interval>> preparaIntervalosFaixaProgressiva(Set<ValorMarkupFaixaProgressiva> list) {
		Map<String, List<Interval>> markupsPorRotaTarifa = new HashMap<String, List<Interval>>();
		for (ValorMarkupFaixaProgressiva valorMarkup : list) {
			
			String chave = obtemChaveFaixaProgressiva(valorMarkup);
					
			if (!markupsPorRotaTarifa.containsKey(chave)) {
				markupsPorRotaTarifa.put(chave, new ArrayList<Interval>());
			}
			markupsPorRotaTarifa.get(chave).add(
					new Interval(
							valorMarkup.getDtVigenciaInicial().toDateTime(new TimeOfDay(0, 0, 0)), 
							valorMarkup.getDtVigenciaFinal() == null ? JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTime(new TimeOfDay(23,59,59)) : valorMarkup.getDtVigenciaFinal().toDateTime(new TimeOfDay(23,59,59))
							));
		}
		return markupsPorRotaTarifa;
	}
	
	private String obtemChaveFaixaProgressiva(ValorMarkupFaixaProgressiva valorMarkup){
		Long idFaixa = valorMarkup.getFaixaProgressiva().getIdFaixaProgressiva();
		Long idParcela = valorMarkup.getFaixaProgressiva().getTabelaPrecoParcela().getIdParcelaPreco();
		Long idRotaTarifa = valorMarkup.getRotaPreco() != null ? valorMarkup.getRotaPreco().getIdRotaPreco() : null;
		if(idRotaTarifa != null){
			return ROTA+idFaixa+idParcela+";"+idRotaTarifa;
		}else{
			idRotaTarifa = valorMarkup.getTarifaPreco() != null ? valorMarkup.getTarifaPreco().getIdTarifaPreco() : null;
			return TARIFA+idFaixa+idParcela+";"+idRotaTarifa;
		}
	}
	
	public void executeValidaMarkupsGeneralidade(List<Markup> listTela, Long idTabelaPreco) {
		Set<Markup> listValores = new HashSet<Markup>();
		listValores.addAll(listTela);
		List<Markup> salvos = this.findMarkupsGeneralidade(idTabelaPreco, false, false);
		for (Markup markup : salvos) {
			if(!listTela.contains(markup)){
				listValores.add(markup);
			}
		}
		Map<Long, List<Interval>> markupsPorParcela = preparaIntervalos(listValores);
		for (Long idParcela : markupsPorParcela.keySet()) {
			if(!validaVigencia(markupsPorParcela.get(idParcela))){
				ParcelaPreco parcela = parcelaPrecoService.findById(idParcela);
				throw new BusinessException("LMS-46116", new Object[] { "Parcela " + parcela.getNmParcelaPreco().toString() });
			}
		}
	}

	private Map<Long, List<Interval>> preparaIntervalos(Set<Markup> list) {
		Map<Long, List<Interval>> markupsPorParcela = new HashMap<Long, List<Interval>>();
		for (Markup markup : list) {
			Long idParcela = markup.getIdParcelaPreco() == null ? Long.MIN_VALUE : markup.getIdParcelaPreco();
			if (!markupsPorParcela.containsKey(idParcela)) {
				markupsPorParcela.put(idParcela, new ArrayList<Interval>());
			}
			if (markup.getDtVigenciaFinal() == null) {
				markup.setDtVigenciaFinal(new YearMonthDay(4000, 1, 1));
			}
			markupsPorParcela.get(idParcela).add(
					new Interval(
							markup.getDtVigenciaInicial().toDateTime(new TimeOfDay(0, 0, 0)), 
							markup.getDtVigenciaFinal().toDateTime(new TimeOfDay(23,59,59))));
		}
		return markupsPorParcela;
	}

	public boolean validaVigencia(List<Interval> intervalos) {
		List<Interval> analisados = new ArrayList<Interval>();
		analisados.add(intervalos.get(0));
		for (int i = 1; i < intervalos.size(); i++) {
			Interval intervalo = intervalos.get(i);
			for (Interval analisado: analisados) {
				if (verificaIntervalo(intervalo, analisado)) {
					return false;
				}
			}
			analisados.add(intervalo);
		}
		return true;
	}

	private boolean verificaIntervalo(Interval intervalo, Interval analisado) {
		return analisado.contains(intervalo) || //verifica se o intervalo está contido em analisado
				intervalo.contains(analisado) || //verifica se analisado está contido em intervalo
				analisado.contains(intervalo.getStart()) || //verifica se a data inicial de intervalo está dentro de analisado 
				analisado.contains(intervalo.getEnd()); //verifica se a data final de intervalo está dentro de analisado
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMarkupDAO(MarkupDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MarkupDAO getMarkupDAO() {
		return (MarkupDAO) getDao();
	}
	
	public Markup findMarkupGeral(Long idTabelaPreco) {
		return getMarkupDAO().findMarkupGeral(idTabelaPreco);
	}
	
	public List<Markup> findMarkupsGeneralidadeVigentes(Long idTabelaPreco) {
		return getMarkupDAO().findMarkupsGeneralidade(idTabelaPreco, true, false);
	}
	
	public List<Markup> findMarkupsGeneralidadeNotVigentes(Long idTabelaPreco) {
		return getMarkupDAO().findMarkupsGeneralidade(idTabelaPreco, false, true);
	}
	
	public List<Markup> findMarkupsGeneralidade(Long idTabelaPreco, boolean vigentes, boolean notVigentes) {
		return getMarkupDAO().findMarkupsGeneralidade(idTabelaPreco, vigentes, notVigentes);
	}
	
	public List<ValorMarkupFaixaProgressiva> findValorMarkupFaixaProgressivaVigentes(Long idTabelaPreco) {
		return this.findValorMarkupFaixaProgressiva(idTabelaPreco, true, false);
	}
	
	public List<ValorMarkupFaixaProgressiva> findValorMarkupFaixaProgressivaNotVigentes(Long idTabelaPreco) {
		return this.findValorMarkupFaixaProgressiva(idTabelaPreco, false, true);
	}
	
	public List<ValorMarkupFaixaProgressiva> findValorMarkupFaixaProgressiva(Long idTabelaPreco, boolean vigentes, boolean notVigentes) {
		return getMarkupDAO().findValorMarkupFaixaProgressiva(idTabelaPreco, vigentes, notVigentes);
	}
	
	public List<ValorMarkupPrecoFrete> findValorMarkupPrecoFreteVigentes(Long idTabelaPreco) {
		return this.findValorMarkupPrecoFrete(idTabelaPreco, true, false);
	}
	
	public List<ValorMarkupPrecoFrete> findValorMarkupPrecoFreteNotVigentes(Long idTabelaPreco) {
		return this.findValorMarkupPrecoFrete(idTabelaPreco, false, true);
	}
	
	public List<ValorMarkupPrecoFrete> findValorMarkupPrecoFrete(Long idTabelaPreco, boolean vigentes, boolean notVigentes) {
		return getMarkupDAO().findValorMarkupPrecoFrete(idTabelaPreco, vigentes, notVigentes);
	}
	
	@SuppressWarnings("unchecked")
	public List<Markup> findTodosByIdTabelaPreco(Long idTabelaPreco) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("tabelaPreco.idTabelaPreco", idTabelaPreco);
		return this.find(parametros);
	}
	
	public void removeAllMarkupFaixaProgressiva(List<Long> idsValorMarkupFaixaProgressiva) {
		List<MarkupFaixaProgressiva> listMarkupFaixaProgressiva = getMarkupDAO().findMarkupFaixaProgressivaByIdsValorMarkupFaixaProgressiva(idsValorMarkupFaixaProgressiva);
		getMarkupDAO().removeAllMarkupFaixaProgressiva(listMarkupFaixaProgressiva);
	}
	
	public void removePrecosFrete(List<Long> idsValorMarkupPrecoFrete) {
		getMarkupDAO().removePrecosFrete(idsValorMarkupPrecoFrete);
	}

	public void executeSubmitAllPrecoFrete(List<ValorMarkupPrecoFrete> entidadesPrecoFrete, Long idTabelaPreco) {
		if (CollectionUtils.isEmpty(entidadesPrecoFrete)) {
			return;
		}
		
		List<Long> listIdsDeletar = new ArrayList<Long>();
		List<ValorMarkupPrecoFrete> listSalvar = new ArrayList<ValorMarkupPrecoFrete>();
		
		//verificar se deve ser deletado o registro
		for (ValorMarkupPrecoFrete valorMarkupPrecoFrete : entidadesPrecoFrete) {
			if(valorMarkupPrecoFrete.getVlMarkup() == null){
				listIdsDeletar.add(valorMarkupPrecoFrete.getIdValorMarkupPrecoFrete());
			}else{
				listSalvar.add(valorMarkupPrecoFrete);
			}
		}
		
		if (!CollectionUtils.isEmpty(listIdsDeletar)) {
			getMarkupDAO().removePrecosFrete(listIdsDeletar);
		}
		if (!CollectionUtils.isEmpty(listSalvar)) {
			getMarkupDAO().storeAllPrecoFrete(listSalvar);
		}
	}

	public void executeSubmitAllFaixaProgressiva(List<MarkupFaixaProgressiva> entidadesFaixaProgressiva, Long idTabelaPreco) {
		if (CollectionUtils.isEmpty(entidadesFaixaProgressiva)) {
			return;
		}
		
		List<Long> listIdsDeletar = new ArrayList<Long>();
		List<ValorMarkupFaixaProgressiva> listRemoverDoPai = new ArrayList<ValorMarkupFaixaProgressiva>();

		//verificar se deve ser deletado o registro
		for (MarkupFaixaProgressiva markupFaixaProgressiva : entidadesFaixaProgressiva) {
			for (ValorMarkupFaixaProgressiva valorMarkupFaixaProgressiva : markupFaixaProgressiva.getValoresMarkupFaixaProgressiva()) {
				if(valorMarkupFaixaProgressiva.getVlMarkup() == null){
					listIdsDeletar.add(valorMarkupFaixaProgressiva.getIdValorMarkupFaixaProgressiva());
					listRemoverDoPai.add(valorMarkupFaixaProgressiva);
				}
			}
			markupFaixaProgressiva.getValoresMarkupFaixaProgressiva().removeAll(listRemoverDoPai);
		}
		getMarkupDAO().storeAllFaixaProgressiva(entidadesFaixaProgressiva);	
		
		if (!CollectionUtils.isEmpty(listIdsDeletar)) {
			getMarkupDAO().removeValorMarkupFaixaProgressiva(listIdsDeletar);
		}
	}
	
	public void executeSubmitAllGeneralidade(List<Markup> list, Long idTabelaPreco) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		List<Long> listDeletar = new ArrayList<Long>();
		List<Markup> listSalvar = new ArrayList<Markup>();
		
		//verificar se deve ser deletado o registro
		for (Markup markup : list) {
			if(markup.getVlMarkup() == null){
				listDeletar.add(markup.getIdMarkup());
			}else{
				listSalvar.add(markup);
			}
		}
		if (!CollectionUtils.isEmpty(listDeletar)) {
			super.removeByIds(listDeletar);
		}
		if (!CollectionUtils.isEmpty(listSalvar)) {
			super.storeAll(listSalvar);
		}
    }

	public void executeSubmitMarkupGeral(Markup markupGeral) {
		if (markupGeral == null) {
			return;
		}
		//verificar se deve ser deletado o registro
		if(markupGeral.getVlMarkup() == null){
			super.removeById(markupGeral.getIdMarkup());
			return;
		}
		
    	super.store(markupGeral);
		
	}
	
	public void setRotaPrecoService(RotaPrecoService rotaPrecoService) {
		this.rotaPrecoService = rotaPrecoService;
	}
	public void setTarifaPrecoService(TarifaPrecoService tarifaPrecoService) {
		this.tarifaPrecoService = tarifaPrecoService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

}
