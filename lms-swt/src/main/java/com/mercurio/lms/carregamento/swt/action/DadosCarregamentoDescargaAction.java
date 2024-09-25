package com.mercurio.lms.carregamento.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.FotoCarregmtoDescarga;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.FotoCarregmtoDescargaService;
import com.mercurio.lms.carregamento.model.service.IntegranteEqOperacService;
import com.mercurio.lms.util.FormatUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.swt.dadosCarregamentoDescargaAction"
 */

public class DadosCarregamentoDescargaAction {

	private CarregamentoDescargaService carregamentoDescargaService;
	private FotoCarregmtoDescargaService fotoCarregmtoDescargaService;
	private IntegranteEqOperacService integranteEqOperacService;


	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public void setFotoCarregmtoDescargaService(FotoCarregmtoDescargaService fotoCarregmtoDescargaService) {
		this.fotoCarregmtoDescargaService = fotoCarregmtoDescargaService;
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}


    public TypedFlatMap findDadosCarregamentoDescarga(TypedFlatMap map) {
    	Long idControleCarga = map.getLong("idControleCarga");
    	Long idFilial = map.getLong("idFilial");
    	String tpOperacao = map.getString("tpOperacao");
    	TypedFlatMap mapResult = carregamentoDescargaService.findCarregamentoDescargaByControleCarga(idControleCarga, idFilial, tpOperacao);
    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	if (mapResult != null) {
    		mapRetorno.put("idCarregamentoDescarga", mapResult.get("idCarregamentoDescarga"));
    		mapRetorno.put("sgFilial", mapResult.get("filial.sgFilial"));
    		mapRetorno.put("nmFantasiaFilial", mapResult.get("filial.pessoa.nmFantasia"));
    		mapRetorno.put("dsEquipe", mapResult.get("dsEquipe"));
    		mapRetorno.put("dhEvento", mapResult.get("dhEvento"));
    		mapRetorno.put("dhInicioOperacao", mapResult.get("dhInicioOperacao"));
    		mapRetorno.put("dhFimOperacao", mapResult.get("dhFimOperacao"));
    		mapRetorno.put("idEquipeOperacao", mapResult.get("equipeOperacao.idEquipeOperacao"));
    		mapRetorno.put("obOperacao", mapResult.get("obOperacao"));
    	}
    	return mapRetorno;
    }

    public ResultSetPage findPaginatedFotos(TypedFlatMap criteria) {
    	Long idCarregamentoDescarga = criteria.getLong("idCarregamentoDescarga");
    	if (idCarregamentoDescarga == null)
    		return ResultSetPage.EMPTY_RESULTSET;

		Map mapCarregamentoDescarga = new HashMap();
		mapCarregamentoDescarga.put("idCarregamentoDescarga", idCarregamentoDescarga);

		criteria.put("carregamentoDescarga", mapCarregamentoDescarga);
		criteria.remove("idCarregamentoDescarga");
    	
    	ResultSetPage rsp = fotoCarregmtoDescargaService.findPaginated(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		FotoCarregmtoDescarga bean = (FotoCarregmtoDescarga)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFotoCarregmtoDescarga", bean.getIdFotoCarregmtoDescarga());
    		typedFlatMap.put("dsFoto", bean.getDsFoto());
    		typedFlatMap.put("foto", bean.getFoto().getFoto());
    		retorno.add(typedFlatMap);
    	}
    	rsp.setList(retorno);
    	return rsp;
    }


    public Integer getRowCountFotos(TypedFlatMap criteria) {
    	Long idCarregamentoDescarga = criteria.getLong("idCarregamentoDescarga");
    	if (idCarregamentoDescarga == null)
    		return Integer.valueOf(0);

		Map mapCarregamentoDescarga = new HashMap();
		mapCarregamentoDescarga.put("idCarregamentoDescarga", idCarregamentoDescarga);

		criteria.put("carregamentoDescarga", mapCarregamentoDescarga);
		criteria.remove("idCarregamentoDescarga");

    	return fotoCarregmtoDescargaService.getRowCount(criteria);
    }
    
    
    public TypedFlatMap findByIdFotoCarregmtoDescarga(Long id) {
    	FotoCarregmtoDescarga fcd = fotoCarregmtoDescargaService.findById(id);
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("idFotoCarregmtoDescarga", fcd.getIdFotoCarregmtoDescarga());
    	map.put("dsFoto", fcd.getDsFoto());
    	return map;
    }
    
    
	public ResultSetPage findPaginatedIntegranteEqOperac(TypedFlatMap criteria) {
		Long idEquipeOperacao = criteria.getLong("idEquipeOperacao");
		if (idEquipeOperacao == null)
			return ResultSetPage.EMPTY_RESULTSET;

		ResultSetPage rsp = integranteEqOperacService.findPaginatedByIdEquipeOperacao(idEquipeOperacao, FindDefinition.createFindDefinition(criteria));
		
		List retorno = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			IntegranteEqOperac bean = (IntegranteEqOperac)iter.next();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idIntegranteEqOperac", bean.getIdIntegranteEqOperac());
			map.put("tpIntegrante", bean.getTpIntegrante());

			if (bean.getUsuario() != null) {
				map.put("nmIntegranteEquipe", bean.getUsuario().getNmUsuario());
				map.put("nrMatricula", bean.getUsuario().getNrMatricula());
	        }
			else
			if (bean.getPessoa() != null) {
				map.put("nmIntegranteEquipe", bean.getPessoa().getNmPessoa());
				map.put("nrIdentificacaoFormatadoIntegrante", FormatUtils.formatIdentificacao(bean.getPessoa()) );
				map.put("dhInclusao", bean.getPessoa().getDhInclusao() );
	        }

			if (bean.getCargoOperacional() != null) {
	        	map.put("dsCargo", bean.getCargoOperacional().getDsCargo());
	        }
	        if (bean.getEmpresa() != null) {
	        	map.put("nmPessoaEmpresa", bean.getEmpresa().getPessoa().getNmPessoa());
	        }
			retorno.add(map);
		}
		rsp.setList(retorno);
		return rsp;
	}

	
	public Integer getRowCountIntegranteEqOperac(TypedFlatMap criteria){
		Long idEquipeOperacao = criteria.getLong("idEquipeOperacao");
		if (idEquipeOperacao == null)
			return Integer.valueOf(0);

		return integranteEqOperacService.getRowCountByIdEquipeOperacao(idEquipeOperacao);
	}
}