package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.vendas.dto.ImportarTRTDTO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.dao.TrtClienteDAO;

public class ImportarTRTService {


    private static final int PRIMEIRA_LINHA = 2;

	protected Logger log = LogManager.getLogger(ImportarTRTService.class);
    
	private TrtClienteDAO trtClienteDAO; 
	private ClienteService clienteService;
	
	ConfiguracoesFacade configuracoesFacade;
	
	public List<String> executeImportar(String arquivo, YearMonthDay vigenciaInicial){
		
		List<String> erros = null;
		
		erros = importar(arquivo, vigenciaInicial);
		
		return erros;
	}

	private List<String> importar(String arquivo, YearMonthDay vigenciaInicial) {
		List<ImportarTRTDTO> linhas = parse(arquivo);
				
		List<String> erros = new ArrayList<String>();
		Integer i=PRIMEIRA_LINHA;
		Map<Long,Long> clientesInseridos = new HashMap<Long,Long>();
		Map<Long,List<Long>> clientesMunicipios = new HashMap<Long,List<Long>>();
		for(ImportarTRTDTO entry : linhas){
			List<String> errosLinha = processaLinha(vigenciaInicial, entry, i, clientesInseridos, clientesMunicipios);
			erros.addAll(errosLinha);
			i++;
		}
		return erros;
	}

	private List<String> processaLinha(YearMonthDay vigenciaInicial, ImportarTRTDTO entry, Integer index, Map<Long,Long> clientesInseridos, Map<Long, List<Long>> clientesMunicipios) {

		List<String> erros = new ArrayList<String>();
		
		Long idCliente = findIdCliente(entry);
		Cliente cliente = findCliente(idCliente);
		if(idCliente == null || cliente == null){
			List<String> parametro = new ArrayList<String>();
			parametro.add(index.toString());
			parametro.add(entry.toString());
			erros.add(configuracoesFacade.getMensagem("importarTRTErroCliente", parametro.toArray()));
		}
		
		Long municipio = findMunicipio(entry);
		if(municipio == null){
			List<String> parametro = new ArrayList<String>();
			parametro.add(index.toString());
			parametro.add(entry.toString());
			erros.add(configuracoesFacade.getMensagem("importarTRTErroMunicipio", parametro.toArray()));
		}
		if(!validateCobraTRT(entry)){
			List<String> parametro = new ArrayList<String>();
			parametro.add(index.toString());
			parametro.add(entry.toString());
			erros.add(configuracoesFacade.getMensagem("importarTRTErroCobra", parametro.toArray()));
		}
		
		if(!clientesMunicipiosUnico(clientesMunicipios, cliente.getIdCliente(), municipio)){
			throw new BusinessException("LMS-04272", new Object[]{"id: "+ municipio, "linha " + index});
		}
		
		if(erros.size() == 0){
			insereRegistro(municipio, cliente, vigenciaInicial, entry.getCobraTRT(), clientesInseridos);
			clientesMunicipios = registraClienteMunicipio(clientesMunicipios, cliente.getIdCliente(), municipio);
		}
		return erros;
	}

	private Boolean clientesMunicipiosUnico(
			Map<Long, List<Long>> clientesMunicipios, Long cliente, Long municipio) {
		List<Long> municipios = clientesMunicipios.get(cliente);
		if(municipios != null && municipios.contains(municipio)){
			return false;
		}
		return true;
	}
	
	private Map<Long, List<Long>> registraClienteMunicipio(
			Map<Long, List<Long>> clientesMunicipios, Long cliente,
			Long municipio) {
		List<Long> municipios = clientesMunicipios.get(cliente);
		if(municipios == null){
			municipios = new ArrayList<Long>();
		}
		municipios.add(municipio);
		clientesMunicipios.put(cliente,municipios);
		
		return clientesMunicipios;
	}

	private boolean validateCobraTRT(ImportarTRTDTO entry) {
		return "S".equals(entry.getCobraTRT()) ||  "N".equals(entry.getCobraTRT());
	}

	private void insereRegistro(Long municipio, Cliente cliente, YearMonthDay vigenciaInicial, String cobraTRT, Map<Long,Long> clientesInseridos) {
		Long clienteTRT;
		if(clientesInseridos.get(cliente.getIdCliente()) == null){
			
			trtClienteDAO.updateClienteTRTVigente(cliente.getIdCliente(), vigenciaInicial);
			deleteClienteTRTFuturo(cliente);
			clienteTRT = trtClienteDAO.insertCliente(cliente, vigenciaInicial);
			
			clientesInseridos.put(cliente.getIdCliente(), clienteTRT);
		}else{
			clienteTRT = clientesInseridos.get(cliente.getIdCliente());
		}
		trtClienteDAO.insertMunicipio(municipio, clienteTRT, cobraTRT);
	}

	private void deleteClienteTRTFuturo(Cliente cliente) {
		trtClienteDAO.deleteMuniciopioClienteTRTFuturo(cliente.getIdCliente());
		trtClienteDAO.deleteClienteTRTFuturo(cliente.getIdCliente());
	}

	private Cliente findCliente(Long idCliente) {
		Cliente cliente = null;
		if(idCliente == null){
			return cliente;
		}
		try {
			cliente = clienteService.findById(idCliente); 
		} catch (Exception e) {
			log.error("[ImportarTRTService] Não foi possivel encontrar cliente.", e);
		}
		return cliente;
	}

	private Long findMunicipio(ImportarTRTDTO entry) {
		Long idMunicipio = null;
		try {
			idMunicipio = trtClienteDAO.findIdMunicipio(entry.getMunicipio(), entry.getUf()); 
		} catch (Exception e) {
			log.error("[ImportarTRTService] Não foi possivel encontrar município.", e);
		}
		return idMunicipio;
		
	}

	private Long findIdCliente(ImportarTRTDTO entry) {
		Long idCliente = null;
		try {
			idCliente = trtClienteDAO.findIdCliente(entry.getCnpj()); 
		} catch (Exception e) {
			log.error("[ImportarTRTService] Não foi possivel encontrar id do cliente.", e);
		}
		return idCliente;
	}

	private List<ImportarTRTDTO> parse(String arquivo) {
		
		List<ImportarTRTDTO> result = new ArrayList<ImportarTRTDTO>();
		List<String> linhas = Arrays.asList(arquivo.split("\\r?\\n"));
		
		linhas = linhas.subList(1,linhas.size());
		
		for(String linha : linhas){
			ImportarTRTDTO dto = new ImportarTRTDTO(linha);
			result.add(dto);
		}
		
		return result;
	}
	
	public void setTrtClienteDAO(TrtClienteDAO trtClienteDAO) {
		this.trtClienteDAO = trtClienteDAO;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
