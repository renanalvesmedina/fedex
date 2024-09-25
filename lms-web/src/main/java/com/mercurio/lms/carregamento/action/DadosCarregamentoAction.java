package com.mercurio.lms.carregamento.action;

import java.util.ArrayList;
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
 * @spring.bean id="lms.carregamento.dadosCarregamentoAction"
 */

public class DadosCarregamentoAction {

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

    public TypedFlatMap findDadosCarregamento(TypedFlatMap map) {
    	Long idControleCarga = map.getLong("idControleCarga");
    	Long idFilial = map.getLong("idFilial");
    	TypedFlatMap mapRetorno = carregamentoDescargaService.findCarregamentoDescargaByControleCarga(idControleCarga, idFilial, "C");
    	return mapRetorno;
    }

    public ResultSetPage findPaginatedFotos(Map map) {
    	String strIdCarregamentoDescarga = (String)((Map)map.get("carregamentoDescarga")).get("idCarregamentoDescarga");
    	if (strIdCarregamentoDescarga == null || strIdCarregamentoDescarga.equals(""))
    		return ResultSetPage.EMPTY_RESULTSET;

    	ResultSetPage rsp = fotoCarregmtoDescargaService.findPaginated(map);
    	List retorno = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		FotoCarregmtoDescarga bean = (FotoCarregmtoDescarga)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFotoCarregmtoDescarga", bean.getIdFotoCarregmtoDescarga());
    		typedFlatMap.put("dsFoto", bean.getDsFoto());
    		typedFlatMap.put("foto.foto", bean.getFoto().getIdFoto());
    		retorno.add(typedFlatMap);
    	}
    	rsp.setList(retorno);
    	return rsp;
    }


    public Integer getRowCountFotos(Map map) {
    	String strIdCarregamentoDescarga = (String)((Map)map.get("carregamentoDescarga")).get("idCarregamentoDescarga");
    	if (strIdCarregamentoDescarga == null || strIdCarregamentoDescarga.equals(""))
    		return Integer.valueOf(0);

    	return fotoCarregmtoDescargaService.getRowCount(map);
    }
    
    
    public FotoCarregmtoDescarga findByIdFotoCarregmtoDescarga(Long id) {
    	return fotoCarregmtoDescargaService.findById(id);
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
				map.put("usuario.nrMatricula", bean.getUsuario().getNrMatricula());
	        }
			else
			if (bean.getPessoa() != null) {
				map.put("nmIntegranteEquipe", bean.getPessoa().getNmPessoa());
				map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(bean.getPessoa()) );
				map.put("pessoa.dhInclusao", bean.getPessoa().getDhInclusao() );
	        }

			if (bean.getCargoOperacional() != null) {
	        	map.put("cargoOperacional.dsCargo", bean.getCargoOperacional().getDsCargo());
	        }
	        if (bean.getEmpresa() != null) {
	        	map.put("empresa.pessoa.nmPessoa", bean.getEmpresa().getPessoa().getNmPessoa());
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