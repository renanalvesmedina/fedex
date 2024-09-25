package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.contratacaoveiculos.model.dao.PostoConveniadoDAO;
import com.mercurio.lms.municipios.model.service.FilialService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.postoConveniadoService"
 */
public class PostoConveniadoService extends CrudService<PostoConveniado, Long> {
	private PessoaService pessoaService;
	private FilialService filialService;
	private ContaBancariaService contaBancariaService;

	
	public PostoConveniado findById(java.lang.Long id) {
		return (PostoConveniado)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}


	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades quee deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List ids) {
		for (Iterator iterIds = ids.iterator(); iterIds.hasNext();) {
			Long id = (Long) iterIds.next();
			this.removeById(id);
		}
	}
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PostoConveniado bean) {
		pessoaService.store(bean.getPessoa());
		return super.store(bean);
	}
	
	public PostoConveniado findPostoConveniadoByIdFilial(Long idFilial){
		return filialService.findById(idFilial).getPostoConveniado();
	}
	
	public Map findDadosBancariosPostoConveniado(Long idPostoConveniado){
		PostoConveniado postoConveniado = findById(idPostoConveniado);
		List<ContaBancaria> listContasBancarias = contaBancariaService.findContasBancariasVigentesByPessoa(idPostoConveniado);
		
		Map map = new HashMap();
		
		map.put("tpIdentificacaoBeneficiario", postoConveniado.getPessoa().getTpIdentificacao());
		map.put("nrIdentificacaoBeneficiario", postoConveniado.getPessoa().getNrIdentificacao());
		map.put("nmPessoaBeneficiario", postoConveniado.getPessoa().getNmPessoa());
		if(!listContasBancarias.isEmpty()){
			ContaBancaria contaBancaria = listContasBancarias.get(0);
			map.put("nrContaBancaria", contaBancaria.getNrContaBancaria());
			map.put("dvContaBancaria", contaBancaria.getDvContaBancaria());
			map.put("nrAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNrAgenciaBancaria());
			map.put("nmAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNmAgenciaBancaria());
			map.put("nrBanco", contaBancaria.getAgenciaBancaria().getBanco().getNrBanco());
			map.put("nmBanco", contaBancaria.getAgenciaBancaria().getBanco().getNmBanco());
		}
		
		return map;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPostoConveniadoDAO(PostoConveniadoDAO dao) {
		setDao( dao );
	}
	
	public PostoConveniadoDAO getPostoConveniadoDAO() {
		return  (PostoConveniadoDAO)getDao();
	}

	/**
	 * @param pessoaService The pessoaService to set.
	 */
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setContaBancariaService(ContaBancariaService contaBancariaService) {
		this.contaBancariaService = contaBancariaService;
	}


}