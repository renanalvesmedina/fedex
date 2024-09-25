package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.sgr.dto.VirusCargaDTO;
import com.mercurio.lms.rest.sgr.dto.VirusCargaFilterDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.sgr.model.VirusCarga;
import com.mercurio.lms.sgr.model.service.VirusCargaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

@Path("/sgr/virusCarga")
public class VirusCargaRest extends LmsBaseCrudReportRest<VirusCargaDTO, VirusCargaDTO, VirusCargaFilterDTO> {

	@InjectInJersey
	VirusCargaService virusCargaService;

	@InjectInJersey
	ClienteService clienteService;

	@GET
	@Path("findChave/{nrChave}")
	public Response findChave(@PathParam("nrChave") String nrChave) {
		VirusCargaDTO dto = new VirusCargaDTO();
		if (nrChave.length() != 44 || !nrChave.substring(20, 22).equals("55")) {
        	throw new BusinessException("LMS-04400");
        } else {
        	dto.setDsSerie(nrChave.substring(22, 25));
    		dto.setNrNotaFiscal(Long.parseLong(nrChave.substring(25, 34)));
    		ClienteSuggestDTO clienteDTO = getClienteDTO(nrChave);
    		dto.setCliente(clienteDTO);
        }
		return Response.ok(dto).build();
	}

	private ClienteSuggestDTO getClienteDTO(String nrChave) {
		ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO();
		List<Cliente> resultCliente = clienteService.findLookupClienteCustom(nrChave.substring(6, 20), null);
		if(!resultCliente.isEmpty()){		
			Cliente cliente = resultCliente.get(0);
			clienteDTO.setIdCliente(cliente.getIdCliente());
			clienteDTO.setNmPessoa(cliente.getPessoa().getNmPessoa());
			clienteDTO.setNrIdentificacao(cliente.getPessoa().getNrIdentificacao());
		}
		return clienteDTO;
	}

	@Override
	protected VirusCargaDTO findById(Long id) {
		VirusCarga virusCarga = virusCargaService.findById(id);
		return VirusCargaDTOBuilder.buildVirusCargaDTO(virusCarga);
	}

	@Override
	protected Long store(VirusCargaDTO bean) {
		VirusCarga virusCarga = new VirusCarga();
		virusCarga.setIdVirusCarga(bean.getId());
		virusCarga.setDhAtivacao(bean.getDhAtivacao());
		virusCarga.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		virusCarga.setCliente(new Cliente(bean.getCliente().getIdCliente()));
		virusCarga.setNrChave(bean.getNrChave());
		virusCarga.setDsSerie(bean.getDsSerie());
		virusCarga.setNrIscaCarga(bean.getNrIscaCarga());
		virusCarga.setNrNotaFiscal(bean.getNrNotaFiscal());
		virusCarga.setNrVolume(bean.getNrVolume());
		return (Long) virusCargaService.store(virusCarga);
	}

	@Override
	protected void removeById(Long id) {
		virusCargaService.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		virusCargaService.removeByIds(ids);
	}

	@Override
	protected Integer count(VirusCargaFilterDTO filter) {
		TypedFlatMap criteria = convertFilterToTypedFlatMap(filter);
		criteria.put("tpCancelado", Boolean.TRUE);
		return virusCargaService.getRowCount(criteria);
	}
	
	@Override
	protected List<VirusCargaDTO> find(VirusCargaFilterDTO filter) {
		TypedFlatMap criteria = convertFilterToTypedFlatMap(filter);
		criteria.put("tpCancelado", Boolean.TRUE);
		List<VirusCarga> list = virusCargaService.find(criteria);
		return VirusCargaDTOBuilder.convertVirusCargaDTO(list);
	}

	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("chaveNfe", "nrChave"));
		list.add(getColumn("cliente", "nmPessoa"));
		list.add(getColumn("notaFiscal", "nrNotaFiscal"));
		list.add(getColumn("numSerieNf", "dsSerie"));
		list.add(getColumn("volume", "nrVolume"));
		list.add(getColumn("virusCarga", "nrIscaCarga"));
		list.add(getColumn("dataAtivacao", "dhAtivacao"));
		return list;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(VirusCargaFilterDTO filter) {
		return convertObjectToMap(virusCargaService.find(convertFilterToTypedFlatMap(filter)));
	}

	private TypedFlatMap convertFilterToTypedFlatMap(VirusCargaFilterDTO filter) {
		TypedFlatMap map = super.getTypedFlatMapWithPaginationInfo(filter);
		putIfNotNull(filter.getControleCarga() != null ? filter.getControleCarga().getIdControleCarga() : null, map, "idControleCarga");
		putIfNotNull(filter.getDhAtivacaoInicial(), map, "dhAtivacaoInicial");
		putIfNotNull(filter.getDhAtivacaoFinal() != null ? filter.getDhAtivacaoFinal().plusDays(1) : null, map, "dhAtivacaoFinal");
		putIfNotNull(filter.getDhInclusaoInicial(), map, "dhInclusaoInicial");
		putIfNotNull(filter.getDhInclusaoFinal() != null ? filter.getDhInclusaoFinal().plusDays(1) : null, map, "dhInclusaoFinal");
		putIfNotNull(filter.getCliente() != null ? filter.getCliente().getIdCliente() : null, map, "idCliente");
		putIfNotNull(filter.getNrChave(), map, "nrChave");
		putIfNotNull(filter.getNrIscaCarga(), map, "nrIscaCarga");
		putIfNotNull(filter.getNrNotaFiscal(), map, "nrNotaFiscal");
		putIfNotNull(filter.getDsSerie(), map, "dsSerie");
		putIfNotNull(filter.getNrVolume(), map, "nrVolume");
		return map;
	}

	private void putIfNotNull(Object object, TypedFlatMap map, String key) {
		if (object != null) {
			map.put(key, object);
		}
	}

	private List<Map<String, Object>> convertObjectToMap(List<VirusCarga> list) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (VirusCarga virusCarga : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nrChave", virusCarga.getNrChave());
			map.put("nmPessoa", virusCarga.getCliente().getPessoa().getNmPessoa());
			map.put("nrNotaFiscal", virusCarga.getNrNotaFiscal());
			map.put("dsSerie", virusCarga.getDsSerie());
			map.put("nrVolume", virusCarga.getNrVolume());
			map.put("nrIscaCarga", virusCarga.getNrIscaCarga());
			map.put("dhAtivacao", virusCarga.getDhAtivacao());
			result.add(map);
		}
		return result;
	}

}
