package com.mercurio.lms.rest.vendas;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.vendas.dto.ParametrosClienteDTO;
import com.mercurio.lms.rest.vendas.dto.ParametrosClienteFiltroDTO;
import com.mercurio.lms.rest.vendas.dto.ParametrosClienteGridDTO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ManterClienteService;

import javax.ws.rs.Path;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

@Path("/vendas/parametrosCliente")
public class ParametrosClienteRest extends BaseCrudRest<ParametrosClienteDTO, ParametrosClienteGridDTO, ParametrosClienteFiltroDTO> {

    @InjectInJersey
    private ClienteService clienteService;
    
	@InjectInJersey
	private ManterClienteService manterClienteService;

    @Override
    protected List<ParametrosClienteGridDTO> find(ParametrosClienteFiltroDTO filter) {
        List<ParametrosClienteGridDTO> listRetorno = new ArrayList<>();
		
		ResultSetPage<TypedFlatMap> result = manterClienteService.findPaginated(this.geraFiltroPesquisa(filter));
		for (TypedFlatMap map : result.getList()) {
			listRetorno.add(this.converteParaGridDTO(map));		
		}
        return listRetorno;
    }

    private TypedFlatMap geraFiltroPesquisa(ParametrosClienteFiltroDTO param) {
		TypedFlatMap filtro = new TypedFlatMap();
		
	    if(param.getIdentificacao() != null ) {	
	    	filtro.put("pessoa.nrIdentificacao", Normalizer.normalize(param.getIdentificacao(), Form.NFD).replaceAll("[-/]", " "));
	    }
	    
	    if(param.getNumeroConta()!= null) {
			filtro.put("nrConta", param.getNumeroConta());
		}
		if(param.getNomeRazaoSocial() != null) {
			filtro.put("pessoa.nmPessoa", param.getNomeRazaoSocial() + "%");
		}
		if(param.getNomeFantasia() != null) {
			filtro.put("nmFantasia", param.getNomeFantasia()+ "%");
		}
		
		if(param.getSituacao() != null) {
			filtro.put("tpSituacao", param.getSituacao());
		}
		
		if(param.getTipoCliente() != null) {
			filtro.put("tpCliente", param.getTipoCliente());
		}
		
		if(param.getTipoPessoa() != null) {
			filtro.put("pessoa.tpPessoa", param.getTipoPessoa());
		}	
		
		filtro.put("_pageSize", "300");
		filtro.put("_currentPage", "1");
		return filtro;
	}

    private ParametrosClienteGridDTO converteParaGridDTO(TypedFlatMap map) {
		ParametrosClienteGridDTO dto = null;
		if(map != null) {
			dto = new ParametrosClienteGridDTO();
			dto.setId(map.getLong("idCliente"));
			dto.setNomeRazaoSocial(map.getString("pessoa.nmPessoa"));
			dto.setIdentificacao(map.getString("pessoa.nrIdentificacao"));
		}
		
		return dto;
	}

    @Override
    protected ParametrosClienteDTO findById(Long id) {
        Cliente cliente = clienteService.findById(id);
        
        if (cliente == null) {
            return null;
        }
        
        ParametrosClienteDTO paraCliente = new ParametrosClienteDTO();

        paraCliente.setId(id);
        paraCliente.setNomeRazaoSocial(cliente.getPessoa().getNmPessoa());
        paraCliente.setTipoIdentificacao(cliente.getPessoa().getTpIdentificacao());
        paraCliente.setIdentificacao(cliente.getPessoa().getNrIdentificacao());
        paraCliente.setNomeFantasia(cliente.getPessoa().getNmFantasia());
        paraCliente.setTipoPessoa(cliente.getPessoa().getTpPessoa());
        paraCliente.setNumeroConta(cliente.getNrConta() == null ? null : cliente.getNrConta().toString());
        paraCliente.setSituacao(cliente.getTpSituacao());
        paraCliente.setTipoCliente(cliente.getTpCliente());
        paraCliente.setExigeComprovanteEntrega(cliente.getBlObrigaComprovanteEntrega());
        paraCliente.setNfeConjulgada(cliente.getBlNfeConjulgada());
        paraCliente.setObrigaRG(cliente.getBlObrigaRg());
        paraCliente.setObrigaQuiz(cliente.getBlObrigaQuizBaixa());
        paraCliente.setPemiteBaixasParciais(cliente.getBlPermiteBaixaParcial());
        paraCliente.setPermiteBaixaPorVolume(cliente.getBlObrigaBaixaPorVolume());
        paraCliente.setObrigaParentesco(cliente.getBlObrigaParentesco());
        paraCliente.setPermiteProdutoPerigoso(cliente.getBlProdutoPerigoso());
        paraCliente.setPermiteProdutoControladoPoliciaCivil(cliente.getBlControladoPoliciaCivil());
        paraCliente.setPermiteProdutoControladoPoliciaFederal(cliente.getBlControladoPoliciaFederal());
        paraCliente.setPermiteProdutoControladoExercito(cliente.getBlControladoExercito());
		paraCliente.setNaoPermiteSubcontratacao(cliente.getBlNaoPermiteSubcontratacao());
        paraCliente.setEnviaDocsFaturamentoNas(cliente.getBlEnviaDocsFaturamentoNas());
        paraCliente.setValidaCobrancDifTdeDest(
                this.ofNullToFalse(cliente.getBlValidaCobrancDifTdeDest()));
        paraCliente.setCobrancaTdeDiferenciada(
                this.ofNullToFalse(cliente.getBlCobrancaTdeDiferenciada()));
        paraCliente.setDificuldadeEntrega(this.ofNullToFalse(cliente.getBlDificuldadeEntrega()));

        return paraCliente;
    }

    @Override
    protected void removeById(Long arg0) {
        throw new InfrastructureException("Método não deve ser utilizado!");
    }

    @Override
    protected Long store(ParametrosClienteDTO parametrosClienteDTO) {
        Cliente cliente = clienteService.findById(parametrosClienteDTO.getId());

        if (cliente == null) {
            return null;
        }

        cliente.setBlObrigaComprovanteEntrega(parametrosClienteDTO.getExigeComprovanteEntrega());
        cliente.setBlObrigaQuizBaixa(parametrosClienteDTO.getObrigaQuiz());
        cliente.setBlNfeConjulgada(parametrosClienteDTO.getNfeConjulgada());
        cliente.setBlObrigaRg(parametrosClienteDTO.getObrigaRG());
        cliente.setBlPermiteBaixaParcial(parametrosClienteDTO.getPemiteBaixasParciais());
        cliente.setBlObrigaBaixaPorVolume(parametrosClienteDTO.getPermiteBaixaPorVolume());
        cliente.setBlObrigaParentesco(parametrosClienteDTO.getObrigaParentesco());
        cliente.setBlProdutoPerigoso(parametrosClienteDTO.getPermiteProdutoPerigoso());
        cliente.setBlControladoPoliciaCivil(parametrosClienteDTO.getPermiteProdutoControladoPoliciaCivil());
        cliente.setBlControladoPoliciaFederal(parametrosClienteDTO.getPermiteProdutoControladoPoliciaFederal());
        cliente.setBlControladoExercito(parametrosClienteDTO.getPermiteProdutoControladoExercito());
        cliente.setBlNaoPermiteSubcontratacao(parametrosClienteDTO.getNaoPermiteSubcontratacao());
        cliente.setBlEnviaDocsFaturamentoNas(parametrosClienteDTO.getEnviaDocsFaturamentoNas());
        cliente.setBlValidaCobrancDifTdeDest(parametrosClienteDTO.getValidaCobrancDifTdeDest());
        cliente.setBlCobrancaTdeDiferenciada(parametrosClienteDTO.getCobrancaTdeDiferenciada());

        clienteService.store(cliente);

        return cliente.getIdCliente();
    }

    @Override
    protected Integer count(ParametrosClienteFiltroDTO filter) {
        return manterClienteService.getRowCount(this.geraFiltroPesquisa(filter));
    }

    @Override
    protected void removeByIds(List<Long> arg0) {
        throw new InfrastructureException("Método não deve ser utilizado!");
    }

    private Boolean ofNullToFalse(Boolean field) {
        return field == null ? Boolean.FALSE : field;
    }

}
