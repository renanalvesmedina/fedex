package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.ComiteNivelMarkup;
import com.mercurio.lms.vendas.model.service.ComiteNivelMarkupService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.workflow.model.TipoEvento;
import com.mercurio.lms.workflow.model.service.TipoEventoService;

public class ManterNivelWorkflowPropostaAction extends CrudAction {
	
	private TabelaPrecoService tabelaPrecoService;
	private ParcelaPrecoService parcelaPrecoService;
	private TipoEventoService tipoEventoService;
	private ParametroGeralService parametroGeralService;
	
	public TypedFlatMap storeParaTodasParcelas(TypedFlatMap parametros){
		TypedFlatMap result = ((ComiteNivelMarkupService)defaultService).storeParaTodasParcelas(parametros);
		return result;
	}
	
	public List<TipoEvento> findTipoEventoCombo() {
		List<Short> listNrEventos = new ArrayList<Short>();
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(ConstantesVendas.PG_PRIORIDADE_EVENTO_WORKFLOW);
		if (parametroGeral != null) {
			String dsConteudo = parametroGeral.getDsConteudo();
			String[] tmp = dsConteudo.split(";");
			for (String nrEvento : tmp) {
				listNrEventos.add(Short.valueOf(nrEvento.substring(IntegerUtils.ZERO, nrEvento.indexOf("-"))));
			}
		}
		
		return tipoEventoService.findTipoEventoCombo(listNrEventos);	
	}
	
	public List<ParcelaPreco> findParcelaCombo(TypedFlatMap parametros) {
		Long idTabelaPreco = Long.valueOf((String) parametros.get("idTabelaPreco"));
		return getTabelaPrecoService().findParcelasByTabelaPreco(idTabelaPreco);
	}
	
	public List findLookupTabelaPreco(Map criteria) {
		return getTabelaPrecoService().findLookup(criteria);
	}
	
	public void removeById(java.lang.Long id) {
    	((ComiteNivelMarkupService)defaultService).removeById(id);
    }

	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ComiteNivelMarkupService)defaultService).removeByIds(ids);
    }

    public ComiteNivelMarkup findById(java.lang.Long id) {
    	return ((ComiteNivelMarkupService)defaultService).findById(id);
    }
	
    public Serializable store(ComiteNivelMarkup bean) {
    	return super.store(bean);
    }
    
    /* Injeção de dependências */
    public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}
	
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	public ParcelaPrecoService getParcelaPrecoService() {
		return parcelaPrecoService;
	}
	
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
	public void setComiteNivelMarkupService(ComiteNivelMarkupService service) {
		this.setDefaultService(service);
	}

	public TipoEventoService getTipoEventoService() {
		return tipoEventoService;
	}

	public void setTipoEventoService(TipoEventoService tipoEventoService) {
		this.tipoEventoService = tipoEventoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

}
