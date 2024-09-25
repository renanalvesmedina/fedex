package com.mercurio.lms.rest.contasareceber.excecoesclientes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.ExcecoesClienteFinanceiro;
import com.mercurio.lms.contasreceber.model.service.ExcecoesClienteFinanceiroService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contasareceber.excecoesclientes.dto.ExcecoesClientesDTO;
import com.mercurio.lms.rest.contasareceber.excecoesclientes.dto.ExcecoesClientesFilterDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.JTFormatUtils;
 
@Path("/contasareceber/excecoesClientes") 
public class ExcecoesClientesRest extends LmsBaseCrudReportRest<ExcecoesClientesDTO, ExcecoesClientesDTO, ExcecoesClientesFilterDTO> { 
 
	@InjectInJersey ExcecoesClienteFinanceiroService excecoesClienteFinanceiroService;
	
	@Override 
	protected ExcecoesClientesDTO findById(Long id) { 
		ExcecoesClienteFinanceiro entity = excecoesClienteFinanceiroService.findExcecaoById(id);
		ExcecoesClientesDTO excecoesClienteDTO = new ExcecoesClientesDTO();
		excecoesClienteDTO.setId(entity.getIdExcecaoClienteFinanceiro());
		ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO();
		clienteDTO.setIdCliente(entity.getPessoa().getIdPessoa());
		clienteDTO.setNrIdentificacao(String.valueOf(entity.getPessoa().getNrIdentificacao()));
		clienteDTO.setNmPessoa(entity.getPessoa().getNmPessoa());
		excecoesClienteDTO.setCliente(clienteDTO);
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(entity.getUsuario().getIdUsuario());
		usuarioDTO.setNmUsuario(entity.getUsuario().getNmUsuario());
		excecoesClienteDTO.setUsuario(usuarioDTO);
		excecoesClienteDTO.setTpClienteFinanceiro(entity.getTpCliente());
		excecoesClienteDTO.setTpEnvioCobrancaTerceiraProAtiva(entity.getTpEnvioCobrancaTerceiraProAtiva());
		excecoesClienteDTO.setTpEnvioFaturamento(entity.getTpEnvioFaturamento());
		excecoesClienteDTO.setTpEnvioCartaCobranca(entity.getTpEnvioCartaCobranca());
		excecoesClienteDTO.setTpEnvioSerasa(entity.getTpEnvioSerasa());
		excecoesClienteDTO.setTpEnvioCobrancaTerceira(entity.getTpEnvioCobrancaTerceira());
		excecoesClienteDTO.setObservacao(entity.getObExcecaoClienteFinanceiro());
		excecoesClienteDTO.setDataAlteracao(entity.getDhAlteracao());
		
		return excecoesClienteDTO; 
	} 
	
	@Override 
	protected Long store(ExcecoesClientesDTO bean) { 
		ExcecoesClienteFinanceiro excecoesClienteFinanceiro = null;
		if(bean.getId() == null){
			excecoesClienteFinanceiro = new ExcecoesClienteFinanceiro();
		} else {
			excecoesClienteFinanceiro = excecoesClienteFinanceiroService.findExcecaoById(bean.getId());
		}
		excecoesClienteFinanceiro.setDhAlteracao(new DateTime());
		return (Long) excecoesClienteFinanceiroService.store(bean.build(excecoesClienteFinanceiro));
	} 
	
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
	
	private void validaCampos(ExcecoesClientesFilterDTO filter) {
		if(filter.getCliente() == null && filter.getTpClienteFinanceiro() == null && filter.getTpEnvioSerasa() == null
				&& filter.getTpEnvioFaturamento() == null && filter.getTpEnvioCobrancaTerceira() == null
				&& filter.getTpEnvioCartaCobranca() == null && filter.getPeriodoEmissaoInicial() == null && filter.getPeriodoEmissaoFinal() == null
				&& filter.getTpEnvioCobrancaTerceiraProAtiva() == null){
			throw new BusinessException("LMS-00055");
		}
	}
	
	@Override 
	protected List<ExcecoesClientesDTO> find(ExcecoesClientesFilterDTO filter) { 
		validaCampos(filter);
		
		List<ExcecoesClienteFinanceiro> excecaoClienteBD = excecoesClienteFinanceiroService.findAll(convertFilterToTypedFlatMap(filter));
		List<ExcecoesClientesDTO> excecoesClientes = new ArrayList<ExcecoesClientesDTO>();
		
		for(ExcecoesClienteFinanceiro e : excecaoClienteBD) {
			ExcecoesClientesDTO excecoesCliente = new ExcecoesClientesDTO();
			excecoesCliente.setId(e.getIdExcecaoClienteFinanceiro());
			ClienteSuggestDTO cliente = new ClienteSuggestDTO();
			cliente.setId(e.getPessoa().getIdPessoa());
			cliente.setNmPessoa(e.getPessoa().getNmPessoa());
			cliente.setNrIdentificacao(e.getPessoa().getNrIdentificacao());
			excecoesCliente.setCliente(cliente);
			excecoesCliente.setTpClienteFinanceiro(e.getTpCliente());
			excecoesCliente.setTpEnvioFaturamento(e.getTpEnvioFaturamento());
			excecoesCliente.setTpEnvioCartaCobranca(e.getTpEnvioCartaCobranca());
			excecoesCliente.setTpEnvioSerasa(e.getTpEnvioSerasa());
			excecoesCliente.setTpEnvioCobrancaTerceira(e.getTpEnvioCobrancaTerceira());
			excecoesCliente.setTpEnvioCobrancaTerceiraProAtiva(e.getTpEnvioCobrancaTerceiraProAtiva());
			excecoesCliente.setObservacao(e.getObExcecaoClienteFinanceiro());
			
			if (e.getDhAlteracao() != null) { 
				excecoesCliente.setDataAlteracaoFormatada(JTFormatUtils.format(e.getDhAlteracao(), "dd/MM/yyyy HH:mm"));
			}
			excecoesCliente.setDataAlteracao(e.getDhAlteracao());
			
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setId(e.getUsuario().getIdUsuario());
			usuarioDTO.setNmUsuario(e.getUsuario().getNmUsuario());
			excecoesCliente.setUsuario(usuarioDTO);
			excecoesClientes.add(excecoesCliente);
		}
		
		return excecoesClientes; 
	} 
 
	@Override 
	protected Integer count(ExcecoesClientesFilterDTO filter) {  
		return 1; 
	}

	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("cliente", "cliente"));
		list.add(getColumn("tpCliente", "tpCliente"));
		list.add(getColumn("tpEnvioFaturamento", "tpEnvioFaturamento"));
		list.add(getColumn("tpEnvioCobranca", "tpEnvioCobranca"));
		list.add(getColumn("tpEnvioSerasa", "tpEnvioSerasa"));
		list.add(getColumn("tipoEnvioCobrancaTerceirizadaProAtiva", "tpEnvioCobrancaTerceiraProAtiva"));
		list.add(getColumn("tpEnvioCobrancaTerceira", "tpEnvioCobrancaTerceira"));
		list.add(getColumn("dataHoraAlteracao", "dataHoraAlteracao"));
		list.add(getColumn("usuarioAlteracao", "usuarioAlteracao"));
		return list;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(ExcecoesClientesFilterDTO filter) {
		return convertObjectToMap(excecoesClienteFinanceiroService.findAll(convertFilterToTypedFlatMap(filter)));
	} 
	
	private TypedFlatMap convertFilterToTypedFlatMap(ExcecoesClientesFilterDTO filter) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getId() != null) tfm.put("id_excecao_cliente_financeiro", filter.getId());
		if (filter.getCliente() != null) tfm.put("id_cliente", filter.getCliente().getIdCliente());
		if (filter.getTpClienteFinanceiro() != null) tfm.put("tp_cliente", filter.getTpClienteFinanceiro());
		if (filter.getTpEnvioSerasa() != null) tfm.put("tp_envio_serasa", filter.getTpEnvioSerasa());
		if (filter.getTpEnvioFaturamento() != null) tfm.put("tp_envio_faturamento", filter.getTpEnvioFaturamento());
		if (filter.getTpEnvioCobrancaTerceira() != null) tfm.put("tp_envio_cobranca_terceira", filter.getTpEnvioCobrancaTerceira());
		if (filter.getTpEnvioCobrancaTerceiraProAtiva() != null) tfm.put("tp_envio_cobranca_pro_ativa", filter.getTpEnvioCobrancaTerceiraProAtiva());
		if (filter.getTpEnvioCartaCobranca() != null) tfm.put("tp_envio_carta_cobranca", filter.getTpEnvioCartaCobranca());
		if (filter.getPeriodoEmissaoInicial() != null) tfm.put("periodo_emissao_inicial", filter.getPeriodoEmissaoInicial());
		if (filter.getPeriodoEmissaoFinal() != null) tfm.put("periodo_emissao_final", filter.getPeriodoEmissaoFinal());
		
		return tfm;
	}
	
	private List<Map<String, Object>> convertObjectToMap(List<ExcecoesClienteFinanceiro> listaExcecoes) {
		List<Map<String, Object>> excecoesMap = new ArrayList<Map<String,Object>>();
		for(ExcecoesClienteFinanceiro e : listaExcecoes){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cliente", e.getPessoa().getNmPessoa());
			map.put("tpCliente", e.getTpCliente().getDescriptionAsString());
			map.put("tpEnvioFaturamento", e.getTpEnvioFaturamento().getDescriptionAsString());
			map.put("tpEnvioCobrancaTerceiraProAtiva", e.getTpEnvioCobrancaTerceiraProAtiva().getDescriptionAsString());
			map.put("tpEnvioCobranca", e.getTpEnvioCartaCobranca().getDescriptionAsString());
			map.put("tpEnvioSerasa", e.getTpEnvioSerasa().getDescriptionAsString());
			map.put("tpEnvioCobrancaTerceira", e.getTpEnvioCobrancaTerceira().getDescriptionAsString());
			map.put("dataHoraAlteracao", e.getDhAlteracao());
			map.put("usuarioAlteracao", e.getUsuario().getNmUsuario());
			
			excecoesMap.add(map);
		}
		return excecoesMap;
	}
 
} 
