/**
 * 
 */
package com.mercurio.lms.configuracoes.swt.action;

import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.service.ControleFormularioService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Luis Carlos Poletto
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.swt.manterControleFormulariosAction"
 */
public class ManterControleFormulariosAction extends CrudAction {
	
	private ControleFormularioService controleFormularioService;
	private EmpresaService empresaService;
	private FilialService filialService;
	private ConhecimentoService conhecimentoService;

	public List findLookupControleFormulario(Map criteria) {
		return controleFormularioService.findLookup(criteria);
	}
	
	public List findLookupEmpresa(Map criteria) {
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.remove("nrIdentificacao"));
		criteria.put("pessoa", pessoa);
		List<Empresa> empresas = empresaService.findLookup(criteria);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if (empresas != null) {
			for(Empresa empresa : empresas) {
				Map<String, Object> mapEmpresa = new HashMap<String, Object>();
				String nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(empresa.getPessoa());
				
				mapEmpresa.put("idEmpresa", empresa.getIdEmpresa());
				mapEmpresa.put("nrIdentificacao", nrIdentificacaoFormatado);
				mapEmpresa.put("nmPessoa", empresa.getPessoa().getNmPessoa());
				result.add(mapEmpresa);
			}
		}
		return result;
	}
	
	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public Map findDadosSessao() {
		Map<String, Object> retorno = new HashMap<String, Object>();
		Filial filial = SessionUtils.getFilialSessao();
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("nmFantasia", filial.getPessoa().getNmFantasia());
		return retorno;
	}
	
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = controleFormularioService.findPaginated(prepareCriteria(criteria));
		if (rsp != null) {
			List<HashMap<String, Object>> controles = rsp.getList();
			if (controles != null) {
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				for (HashMap<String, Object> controle : controles) {
					Map<String, Object> mapControle = new HashMap<String, Object>();
					
					mapControle.put("idControleFormulario", controle.get("idControleFormulario"));
					mapControle.put("nrFormularioInicial", controle.get("nrFormularioInicial"));
					mapControle.put("nrFormularioFinal", controle.get("nrFormularioFinal"));
					mapControle.put("nrCodigoBarrasInicial", controle.get("nrCodigoBarrasInicial"));
					mapControle.put("nrCodigoBarrasFinal", controle.get("nrCodigoBarrasFinal"));
					mapControle.put("dtRecebimento", controle.get("dtRecebimento"));
					mapControle.put("controleFormOrigem", controle.get("controleFormOrigem"));
					mapControle.put("controleForm", controle.get("controleForm"));
					mapControle.put("cdSerie", controle.get("cdSerie"));
					
					Map tpFormulario = (Map) controle.get("tpFormulario");
					String value = (String) tpFormulario.get("value");
					String description = (String) tpFormulario.get("description");
					mapControle.put("tpFormulario", new DomainValue(value, new VarcharI18n(description), TRUE));
					
					Map tpSituacaoFormulario = (Map) controle.get("tpSituacaoFormulario");
					value = (String) tpSituacaoFormulario.get("value");
					description = (String) tpSituacaoFormulario.get("description");
					mapControle.put("tpSituacaoFormulario", new DomainValue(value, new VarcharI18n(description), TRUE));
					
					Map empresa = (Map) controle.get("empresa");
					Map pessoa = (Map) empresa.get("pessoa");
					mapControle.put("empresaNmPessoa", pessoa.get("nmPessoa"));
					
					Map filial = (Map) controle.get("filial");
					mapControle.put("sgFilial", filial.get("sgFilial"));
					mapControle.put("idFilial", filial.get("idFilial"));
					
					result.add(mapControle);
				}
				rsp.setList(result);
			}
		}
		return rsp;
	}
	
	public Integer getRowCount(Map criteria) {
		return controleFormularioService.getRowCount(prepareCriteria(criteria));
	}
	
	public void validaFaixaConhecimentosEmitidos(Map criteria) {
		if( !"E".equals(criteria.get("tpSituacaoFormulario")) ){
		if(criteria.get("nrCodigoBarrasInicial") != null && criteria.get("nrCodigoBarrasFinal") != null){
			Long rowCount = conhecimentoService.getRowCountByCodigoBarras(Long.valueOf(((Long) criteria.get("nrCodigoBarrasInicial"))/10), Long.valueOf(((Long) criteria.get("nrCodigoBarrasFinal"))/10));
			if(rowCount > 0){
				throw new BusinessException("LMS-27110");
			}
		}
	}
	}
	
	public Map findById(Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		ControleFormulario controleFormulario = controleFormularioService.findById(id);
		
		result.put("idControleFormulario", controleFormulario.getIdControleFormulario());
		if (controleFormulario.getControleFormulario() != null) {
			result.put("idControleFormularioOrigem", controleFormulario.getControleFormulario().getIdControleFormulario());
			result.put("controleForm", controleFormulario.getControleFormulario().getControleForm());
		}
		result.put("idEmpresa", controleFormulario.getEmpresa().getIdEmpresa());
		result.put("nrIdentificacao", FormatUtils.formatIdentificacao(controleFormulario.getEmpresa().getPessoa()));
		result.put("nmPessoa", controleFormulario.getEmpresa().getPessoa().getNmPessoa());
		result.put("idFilial", controleFormulario.getFilial().getIdFilial());
		result.put("sgFilial", controleFormulario.getFilial().getSgFilial());
		result.put("nmFantasia", controleFormulario.getFilial().getPessoa().getNmFantasia());
		result.put("dtRecebimento", controleFormulario.getDtRecebimento());
		result.put("tpFormulario", controleFormulario.getTpFormulario().getValue());
		result.put("tpSituacaoFormulario", controleFormulario.getTpSituacaoFormulario().getValue());
		result.put("nrFormularioInicial", controleFormulario.getNrFormularioInicial());
		result.put("nrFormularioFinal", controleFormulario.getNrFormularioFinal());
		result.put("nrSeloFiscalInicial", controleFormulario.getNrSeloFiscalInicial());
		result.put("nrSeloFiscalFinal", controleFormulario.getNrSeloFiscalFinal());
		result.put("nrCodigoBarrasInicial", controleFormulario.getNrCodigoBarrasInicial());
		result.put("nrCodigoBarrasFinal", controleFormulario.getNrCodigoBarrasFinal());
		result.put("cdSerie", controleFormulario.getCdSerie());
		result.put("nrAidf", controleFormulario.getNrAidf());
		
		return result;
	}
	
	public Map store(Map data) {
		validaFaixaConhecimentosEmitidos(data);
		
		ControleFormulario bean = new ControleFormulario();
		
		bean.setCdSerie((String) data.get("cdSerie"));
		bean.setDtRecebimento((YearMonthDay) data.get("dtRecebimento"));
		bean.setIdControleFormulario((Long) data.get("idControleFormulario"));
		bean.setNrAidf((String) data.get("nrAidf"));
		bean.setNrFormularioFinal((Long) data.get("nrFormularioFinal"));
		bean.setNrFormularioInicial((Long) data.get("nrFormularioInicial"));
		bean.setNrSeloFiscalFinal((Long) data.get("nrSeloFiscalFinal"));
		bean.setNrSeloFiscalInicial((Long) data.get("nrSeloFiscalInicial"));
		bean.setNrCodigoBarrasFinal((Long) data.get("nrCodigoBarrasFinal"));
		bean.setNrCodigoBarrasInicial((Long) data.get("nrCodigoBarrasInicial"));
		bean.setTpFormulario(new DomainValue((String) data.get("tpFormulario")));
		bean.setTpSituacaoFormulario(new DomainValue((String) data.get("tpSituacaoFormulario")));
		
		Long idControleFormularioOrigem = (Long) data.get("idControleFormularioOrigem");
		if (idControleFormularioOrigem != null) {
			ControleFormulario controleFormulario = new ControleFormulario();
			controleFormulario.setIdControleFormulario(idControleFormularioOrigem);
			bean.setControleFormulario(controleFormulario);
		}
		
		Long idEmpresa = (Long) data.get("idEmpresa");
		if (idEmpresa != null) {
			Empresa empresa = new Empresa();
			empresa.setIdEmpresa(idEmpresa);
			bean.setEmpresa(empresa);
		}
		
		Long idFilial = (Long) data.get("idFilial");
		if (idFilial != null) {
			Filial filial = new Filial();
			filial.setIdFilial(idFilial);
			bean.setFilial(filial);
		}
		
		Long idControleFormulario = (Long) controleFormularioService.store(bean);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idControleFormulario", idControleFormulario);
		return result;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		controleFormularioService.removeByIds(ids);
	}
	
	public void removeById(Long id) {
		controleFormularioService.removeById(id);
	}
	
	/*
	 * METODOS PRIVADOS
	 */
	/**
	 * Prepara os criterios de pesquisa para a service.
	 */
	private Map prepareCriteria(Map criteria) {
		Map result = new HashMap();
		result.put("_currentPage", criteria.get("_currentPage"));
    	result.put("_pageSize", criteria.get("_pageSize"));
    	result.put("_order", criteria.get("_order"));
    	
    	result.put("tpFormulario", criteria.get("tpFormulario"));
    	result.put("tpSituacaoFormulario", criteria.get("tpSituacaoFormulario"));
    	result.put("dtRecebimentoInicial", criteria.get("dtRecebimentoInicial"));
    	result.put("dtRecebimentoFinal", criteria.get("dtRecebimentoFinal"));
    	result.put("nrFormulario", criteria.get("nrFormulario"));
    	result.put("nrSeloFiscal", criteria.get("nrSeloFiscal"));
    	result.put("cdSerie", criteria.get("cdSerie"));
    	result.put("nrAidf", criteria.get("nrAidf"));
    	
    	Map<String, Object> controleFormulario = new HashMap<String, Object>();
    	controleFormulario.put("idControleFormulario", criteria.get("idControleFormulario"));
    	result.put("controleFormulario", controleFormulario);
    	
    	Map<String, Object> empresa = new HashMap<String, Object>();
    	empresa.put("idEmpresa", criteria.get("idEmpresa"));
    	result.put("empresa", empresa);
    	
    	Map<String, Object> filial = new HashMap<String, Object>();
    	filial.put("idFilial", criteria.get("idFilial"));
    	result.put("filial", filial);
		
    	return result;
	}

	/*
	 * GETTERS E SETTERS
	 */
	
	public void setControleFormularioService(ControleFormularioService controleFormularioService) {
		this.controleFormularioService = controleFormularioService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	
}
