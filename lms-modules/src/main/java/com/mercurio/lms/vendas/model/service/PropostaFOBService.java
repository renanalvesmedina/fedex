package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.OrigemPropostaFOB;
import com.mercurio.lms.vendas.model.PropostaFOB;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.dao.PropostaFOBDAO;


/**
 * @spring.bean id="lms.vendas.propostaFOBService"
 */
public class PropostaFOBService extends CrudService<PropostaFOB, Long> {

	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private MunicipioService municipioService;
	private OrigemPropostaFOBService origemPropostaFOBService;
	private EnderecoPessoaService enderecoPessoaService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private ServicoService servicoService;
	private TabelaPrecoService tabelaPrecoService;
	
	/**
	 * Obtem a tabela FOB vigente
	 * @return List
	 */
	public TabelaPreco findTabelaPrecoFOBVigente(){
		return tabelaPrecoService.findTabelaVigente("T", "F", true, JTDateTimeUtils.getDataAtual());
	}
	
	/**
	 * Obtem todas as filias vigentes 
	 * @return
	 */
	public List<Map<String, Object>> findOrigensProposta(Map criteria){

		String idFiliais = (String)parametroGeralService.findConteudoByNomeParametro("FILIAL_PADRAO_FOB", false);

		String[] ids = idFiliais.split(",");
		List<String> lsIdFiliais = Arrays.asList(ids);

		/*Lista com retorno de dados da grid*/
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> params = null;

		if(MapUtils.getLong(criteria, "idPropostaFOB") == null){
			List<Filial> filiais = filialService.findFiliaisEmpresa();
			for (Filial filial : filiais) {
				
				Map municipio = enderecoPessoaService.findMunicipioUfByIdPessoa(filial.getIdFilial());
	
				params = new HashMap<String, Object>();
				params.put("idFilial", filial.getIdFilial());
				params.put("sgFilial", filial.getSgFilial() + " - "+ MapUtils.getString(municipio, "nmMunicipio"));			
				params.put("blIndicadorPadrao",  (lsIdFiliais.contains(filial.getIdFilial().toString()) ? "S" : "N"  )  );
	
				list.add(params);
			}
		}else{
			Long idPropostaFOB = MapUtils.getLong(criteria, "idPropostaFOB");
			List<OrigemPropostaFOB> filiais = origemPropostaFOBService.findFiliaisProposta(idPropostaFOB);
			if(CollectionUtils.isNotEmpty(filiais)){
				for (OrigemPropostaFOB origem : filiais) {
					
					Map municipio = enderecoPessoaService.findMunicipioUfByIdPessoa(origem.getFilial().getIdFilial());
					
					params = new HashMap<String, Object>();
					params.put("idFilial", origem.getFilial().getIdFilial());
					params.put("sgFilial", origem.getFilial().getSgFilial() + " - "+ MapUtils.getString(municipio, "nmMunicipio"));			
					params.put("blIndicadorPadrao",  (lsIdFiliais.contains(origem.getFilial().getIdFilial().toString()) ? "S" : "N"  )  );
		
					list.add(params);					
				}				
			}
		}

		return list;
	}
	
	/**
	 * Efetiva a proposta , salva informções
	 * na tabela ORIGEM_PROPOSTA_FOB e PROPOSTA_FOB
	 * 
	 * @param fob
	 */
	public Serializable executeEfetivarProposta(PropostaFOB fob, List origens){
		
		/*Salva a proposta fob*/
		super.store(fob);
		
		Servico servico = servicoService.findServicoBySigla("RNC");
		
		/*Salva a tabela divisao cliente com a tabela fob seleciona*/
		TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(fob.getDivisaoCliente().getIdDivisaoCliente(), servico.getIdServico());
		tdc.setTabelaPrecoFob(fob.getTabelaPreco());
		tabelaDivisaoClienteService.store(tdc);
		
		/*Salva as origens da proposta*/
		if(CollectionUtils.isNotEmpty(origens)){
			
			/*Remove todos as origens ao atualizar*/ 
			if(fob.getIdPropostaFOB() != null){
				origemPropostaFOBService.removeAll(fob.getIdPropostaFOB());
			}
			
			List<OrigemPropostaFOB> list = new ArrayList<OrigemPropostaFOB>();
			
			OrigemPropostaFOB origemPropostaFOB = null;
			Filial filial = null;
			
			LinkedList<Map> or = (LinkedList<Map>)origens;
 			for (Map origem : or) {
				if(MapUtils.getBoolean(origem, "isSelected", false)) {
					origemPropostaFOB = new OrigemPropostaFOB();
					filial = new Filial();
					filial.setIdFilial(MapUtils.getLong(origem, "id"));
					origemPropostaFOB.setFilial(filial);
					origemPropostaFOB.setPropostaFOB(fob);
					list.add(origemPropostaFOB);
				}
			}
			origemPropostaFOBService.storeAll(list);
			
		}else{
			throw new BusinessException("Selecione pelo menos um registro!");
		}
		
		return fob;
	}
	
	/**
	 * Obtem a proposta através do cliente
	 * 
	 * @param  idCliente
	 * @return List<PropostaFOB>
	 */
	public List<PropostaFOB> findByCliente(Long idCliente){
		return getPropostaFOBDAO().findByCliente(idCliente);
	}
	
	public void setPropostaFOBDAO(PropostaFOBDAO dao){
		setDao(dao);
	}
	private PropostaFOBDAO getPropostaFOBDAO() {
		return (PropostaFOBDAO) getDao();
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public OrigemPropostaFOBService getOrigemPropostaFOBService() {
		return origemPropostaFOBService;
	}

	public void setOrigemPropostaFOBService(
			OrigemPropostaFOBService origemPropostaFOBService) {
		this.origemPropostaFOBService = origemPropostaFOBService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public TabelaDivisaoClienteService getTabelaDivisaoClienteService() {
		return tabelaDivisaoClienteService;
	}

	public void setTabelaDivisaoClienteService(
			TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}	
}
