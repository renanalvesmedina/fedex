package com.mercurio.lms.configuracoes.swt.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;

import java.util.*;

/**
 * @spring.bean id="lms.configuracoes.swt.manterMeuPerfilAction"
 */
public class ManterMeuPerfilAction extends CrudAction {
	private FilialService filialService;
	private EmpresaService empresaService;
	private UsuarioService usuarioService;

	/*
	 * metodo que preenche a lookup filial
	 * */
	public List findLookupFiliaisByEmpresaUsuarioLogado(TypedFlatMap m) {
		List list = this.filialService.findLookupByEmpresaUsuarioLogado(m);
		List retorno = new ArrayList();
		for (Iterator i = list.iterator(); i.hasNext();) {
			Map map = (Map) i.next();

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idFilial", map.get("idFilial").toString());
			tfm.put("sgFilial", map.get("sgFilial").toString());
			tfm.put("nmFantasia", map.get("pessoa.nmFantasia").toString());
			retorno.add(tfm);
		}
		return retorno;
	}

	/*
	 * metodo que preenche a combo empresa
	 * */
	public List findEmpresasByUsuarioLogado(TypedFlatMap m) {
		List<Object[]> empresas = this.empresaService.findByUsuarioLogadoList(m);
		//necessário pois existe bug no SWT(swing) não aceita keys de do Map com "."
		List<Map<String , Object>> result = new ArrayList<Map<String , Object>>();
		if (empresas != null && !empresas.isEmpty()) {
			for (Object[] object : empresas) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("idEmpresa", object[0]);
				map.put("nmPessoa", object[1]);
				map.put("nrIdentificacao", object[2]);
				result.add(map);
			}
		}
		return result;
	}

	/*
	 * metodo que preenche a lookup empresa
	 * */
	public List findLookupEmpresa(Map criteria) {
		Map nrIdentificacao = new HashMap();
		nrIdentificacao.put("nrIdentificacao", criteria.get("nrIdentificacao"));
		criteria.put("pessoa", nrIdentificacao);
		criteria.remove("nrIdentificacao");

		List list = this.empresaService.findLookupFilial(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Empresa empresa = (Empresa)iter.next();
			Map mapEmpresa = new HashMap();

			mapEmpresa.put("idEmpresa", empresa.getIdEmpresa());
			mapEmpresa.put("nrIdentificacao", FormatUtils.formatIdentificacao(empresa.getPessoa()));
			mapEmpresa.put("nmEmpresa", empresa.getPessoa().getNmPessoa());
			mapEmpresa.put("tpEmpresa", empresa.getTpEmpresa().getValue());
			retorno.add(mapEmpresa);
		}
		return retorno;
	}

	/*inicio
	* metodo do botão colsultar que preenche a grid
	* */
	public ResultSetPage findPaginatedFiliaisByEmpresaUsuarioLogado(TypedFlatMap m) {
		ResultSetPage retorno =  this.filialService.findPaginatedByEmpresaUsuarioLogado(m);
		//necessário pois existe bug no SWT(swing) não aceita keys de do Map com "."
		List r = new ArrayList();
		for (Iterator it = retorno.getList().iterator(); it.hasNext();) {
			Map map = (Map) it.next();

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idFilial", map.get("idFilial").toString());
			tfm.put("sgFilial", map.get("sgFilial").toString());
			tfm.put("nmFantasia", map.get("pessoa.nmFantasia").toString());
			tfm.put("nrIdentificacao", map.get("empresa.pessoa.nrIdentificacao").toString());
			tfm.put("filialSgNm", map.get("filialSgNm").toString());
			r.add(tfm);
		}
		retorno.setList(r);
		return retorno;
	}

	public Integer getRowCountFiliaisByEmpresaUsuarioLogado(TypedFlatMap m) {
		return this.filialService.getRowCountByEmpresaUsuarioLogado(m);
	}
	/*fim
	 * metodo do botão colsultar que preenche a grid
	 * */

	/*
	* carrega o usuario na sessão do LMS-SWT
	* */
	public void loadMeuPerfil(TypedFlatMap m) {
		this.usuarioService.loadMeuPerfil(m);
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}