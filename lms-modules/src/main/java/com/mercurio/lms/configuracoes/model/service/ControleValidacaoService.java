package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import br.com.tntbrasil.integracao.domains.vendas.ClienteSaneamento;
import br.com.tntbrasil.integracao.domains.vendas.ListaClientesBloqueadoTributacao;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.ControleValidacao;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.ControleValidacaoDAO;

/**
 * @author JoseMR Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * @spring.bean id="lms.configuracoes.bancoService"
 */

public class ControleValidacaoService extends CrudService<ControleValidacao, Long> {

	private PessoaService pessoaService;
	
	/**
	 * Recupera uma instância de <code>Banco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ControleValidacao findById(Long id) {
		return (ControleValidacao)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */	
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ControleValidacao bean) {
		return super.store(bean);
	}
	
	public void updateByNrIdentificacaoBloqueado(ListaClientesBloqueadoTributacao list) {
		List<ControleValidacao> listControle = new ArrayList<ControleValidacao>();
		ControleValidacao controleValidacao = null;
		for(ClienteSaneamento saneamento : list ){
			if( saneamento.getIdControleValidacao() == null ){
				Pessoa pessoa = pessoaService.findByNrIdentificacao(saneamento.getCnpj());
				controleValidacao = new ControleValidacao();
				controleValidacao.setPessoa(pessoa);
				controleValidacao.setDhConsulta(new DateTime());
				controleValidacao.setTpOrgao(new DomainValue("C"));
				
			}else{
				controleValidacao = findById(saneamento.getIdControleValidacao());
				controleValidacao.setTpOrgao(new DomainValue("C"));
			}
			controleValidacao.setDhEnvioConsulta(new DateTime());
			listControle.add(controleValidacao);
		}
		super.storeAll(listControle);
	}
	
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
	public void setControleValidacaoDAO(ControleValidacaoDAO dao){
		setDao( dao );
	}
	
	/**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
	public ControleValidacaoDAO getControleValidacaoDAO(){
		return (ControleValidacaoDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
}
