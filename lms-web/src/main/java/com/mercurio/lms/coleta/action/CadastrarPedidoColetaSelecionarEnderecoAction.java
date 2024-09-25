package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.cadastrarPedidoColetaSelecionarEnderecoAction"
 */

public class CadastrarPedidoColetaSelecionarEnderecoAction extends CrudAction {

	public void setService(EnderecoPessoaService enderecoPessoaService) {
		this.defaultService = enderecoPessoaService;
	}
	
	public YearMonthDay getDataAtual() {
		return JTDateTimeUtils.getDataAtual();
	}
	
	public ResultSetPage findPaginatedEnderecos(Map criteria){
		List retorno = new ArrayList();
		ResultSetPage resultSetPage = this.defaultService.findPaginated(criteria);
		List result = resultSetPage.getList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			EnderecoPessoa enderecoPessoa = (EnderecoPessoa) iter.next();
			map.put("idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
			map.put("municipio.idMunicipio", enderecoPessoa.getMunicipio().getIdMunicipio());
			map.put("municipio.nmMunicipio", enderecoPessoa.getMunicipio().getNmMunicipio());
			map.put("municipio.unidadeFederativa.sgUnidadeFederativa", enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			map.put("dsTipoLogradouro", enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro());
			map.put("dsEndereco", enderecoPessoa.getDsEndereco());
			map.put("nrEndereco", enderecoPessoa.getNrEndereco());
			map.put("dsComplemento", enderecoPessoa.getDsComplemento());
			map.put("dsBairro", enderecoPessoa.getDsBairro());
			map.put("nrCep", enderecoPessoa.getNrCep());
			map.put("dtVigenciaInicial", enderecoPessoa.getDtVigenciaInicial());
			map.put("dtVigenciaFinal", enderecoPessoa.getDtVigenciaFinal());
			retorno.add(map);
		}
		resultSetPage.setList(retorno);
		return resultSetPage;
	}
	
    /**
     * row count para findPaginatedEnderecos
     * @param map
     * @return
     */
    public Integer getRowCountEnderecos(Map map) {
        return this.defaultService.getRowCount(map);
    }
	
}
