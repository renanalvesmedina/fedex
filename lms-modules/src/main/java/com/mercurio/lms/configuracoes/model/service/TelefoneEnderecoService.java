package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.dao.TelefoneEnderecoDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.telefoneEnderecoService"
 */
public class TelefoneEnderecoService extends CrudService<TelefoneEndereco, Long> {
	private PessoaService pessoaService;
	private EspecializacaoPessoaService especializacaoPessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneContatoService telefoneContatoService;
	
	/**
	 * Recupera uma inst�ncia de <code>TelefoneEndereco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public TelefoneEndereco findById(java.lang.Long id) {
		return (TelefoneEndereco)super.findById(id);
	}

	public List find(Map criteria) {
		List orderBy = new ArrayList();
		orderBy.add("nrDdd");
		orderBy.add("nrTelefone");    	
		return this.getTelefoneEnderecoDAO().findListByCriteria(criteria, orderBy);  
	} 

	/**
	 * O m�todo � igual a findTelefoneEnderecoPadrao(Long), mas n�o pode ser chamado 
	 * diretamente a partir da tela porque ele faz um sobrecarga de 
	 * findTelefoneEnderecoPadrao(Long, Date) 
	 * 
	 * @param Lond idPessoa
	 * @return TelefoneEndereco Telefone da pessoa
	 * */	    
	public TelefoneEndereco findTelefoneEnderecoPadraoTela(Long idPessoa) {
		return this.findTelefoneEnderecoPadrao(idPessoa);
	}	    

	/**
	 * Retorno o telefone padr�o da pessoa da informada com a data atual.
	 * 
	 * Quando o tipo de pessoa � jur�dico ele retorna um telefone de tipo 'comercial'
	 * se n�o achar retornar o primeiro encontrado (duh).
	 * 
	 * Quando o tipo de pessoa � f�sico ele retorna um telefone de tipo 'residencial'
	 * se n�o achar retornar o primeiro encontrado (bis).
	 * 
	 * @param Lond idPessoa
	 * @return TelefoneEndereco Telefone da pessoa
	 * */	    
	public TelefoneEndereco findTelefoneEnderecoPadrao(Long idPessoa) {
		return this.findTelefoneEnderecoPadrao(idPessoa, null);
	}	    

	/**
	 * Retorno o telefone padr�o da pessoa da informada com a data informada.
	 * 
	 * Quando o tipo de pessoa � jur�dico ele retorna um telefone de tipo 'comercial'
	 * se n�o achar retornar o primeiro encontrado (duh).
	 * 
	 * Quando o tipo de pessoa � f�sico ele retorna um telefone de tipo 'residencial'
	 * se n�o achar retornar o primeiro encontrado (bis).
	 * 
	 * @param Lond idPessoa
	 * @param Date dataVigencia
	 * @param boolean blFetchPessoa
	 * @return TelefoneEndereco Telefone da pessoa
	 */
	public TelefoneEndereco findTelefoneEnderecoPadrao(Long idPessoa, YearMonthDay dataVigencia) {
		Pessoa pessoa = pessoaService.findById(idPessoa);    	
		if (pessoa == null) {
			return null;
		}		
		TelefoneEndereco telefoneEndereco = null;

		// buscar telefone por pessoa e tipo, passando por endere�o pessoa
		if (pessoa.getTpPessoa().getValue().equals("J")) {
			telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoByEnderecoPessoa(idPessoa, "COM", null);
		} else {
			telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoByEnderecoPessoa(idPessoa, "RES", null);			
		}

		// buscar telefone por pessoa, passando por endere�o pessoa
		if (telefoneEndereco == null) {
			telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoByEnderecoPessoa(idPessoa, null, null);			
		}

		// buscar telefone por pessoa
		if (telefoneEndereco == null) {
			telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoByPessoa(idPessoa);			
		}		
		return telefoneEndereco;    	
	}

	/**
	 * Busca um TelefoneEndereco para a tela de cadastro de pedido de coleta.
	 * Sempre busca o TelefoneEndereco com maior ID, ou seja,
	 * o �ltimo cadastrado para um dado endere�o.
	 * A prioridade � para um endere�o de coleta ("COL").
	 * @param idEnderecoPessoa
	 * @return
	 */
	public TelefoneEndereco findTelefoneEnderecoPedidoColeta(Long idEnderecoPessoa) {
		if (idEnderecoPessoa == null) {
			return null;
		}
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(idEnderecoPessoa);
		Pessoa pessoa = enderecoPessoa.getPessoa();
		TelefoneEndereco telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoPedidoColeta(idEnderecoPessoa, "COL");
		
		if (telefoneEndereco == null) {
			if (pessoa.getTpPessoa().getValue().equals("J")) {
				telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoPedidoColeta(idEnderecoPessoa, "COM");
			} else {
				telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoPedidoColeta(idEnderecoPessoa, "RES");			
			}
		}

		if (telefoneEndereco == null) {
			telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoPedidoColeta(idEnderecoPessoa, null);			
		}

		if (telefoneEndereco == null) {
			telefoneEndereco = this.getTelefoneEnderecoDAO().findTelefoneEnderecoByPessoa(pessoa.getIdPessoa());			
		}		
		return telefoneEndereco;   
	}
	

	/**
	 * Retorna a lista de telefones da pessoa informada
	 * 
	 * author Micka�l Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return List
	 * */
	public List findByIdPessoa(Long idPessoa) {
		return getTelefoneEnderecoDAO().findByIdPessoa(idPessoa);
	}

	/**
	 * retorna o primeiro telefone da pessoa informada 
	 * @author Luciano Silva
	 * @param idPessoa
	 * @return TelefoneEndereco Telefone da pessoa
	 */
	public TelefoneEndereco findTelefoneByIdPessoa(Long idPessoa) {
		return getTelefoneEnderecoDAO().findTelefoneEnderecoByPessoa( idPessoa );
	}

	/**
	 * Retorno o telefone padr�o do endere�o informado com o tipo de uso.
	 * 
	 * @param Long idEnderecoPessoa
	 * @param String tpUso
	 * @return TelefoneEndereco Telefone do endereco informado
	 * */		
	public TelefoneEndereco findTelefoneEnderecoPadraoPorTpUso(Long idEnderecoPessoa, String tpUso) {
		return this.getTelefoneEnderecoDAO().findTelefoneEnderecoPadraoPorTpUso(idEnderecoPessoa, tpUso, null);    	
	}

	public TelefoneEndereco findTelefoneEnderecoPadraoPorTpUso(Long idEnderecoPessoa, String tpUso, String tpTelefone) {
		return this.getTelefoneEnderecoDAO().findTelefoneEnderecoPadraoPorTpUso(idEnderecoPessoa, tpUso, tpTelefone);    	
	} 

	/**
	 * Chama o m�todo respectivo do DAO que realiza a consulta no banco
	 * 
	 * autor Pedro Henrique Jatob�
	 * 27/12/2005
	 * @param idPessoa
	 * @param tpTelefone
	 * @param idEnderecoPessoa
	 * @return
	 */
	public Map findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(Long idPessoa, String tpTelefone, Long idEnderecoPessoa) {
		return getTelefoneEnderecoDAO().findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(idPessoa,tpTelefone,idEnderecoPessoa);
	}

	/**
	 * 
	 * @param idPessoa
	 * @param tpTelefone
	 * @return
	 */
	public TypedFlatMap findTelefoneEnderecoByIdPessoaTpTelefone(Long idPessoa, String tpTelefone) {
		return findTelefoneEnderecoByIdPessoaTpTelefone(idPessoa, tpTelefone, null);
	}

	/**
	 * 
	 * @param idPessoa
	 * @param tpTelefone
	 * @param tpUso
	 * @return
	 */
	public TypedFlatMap findTelefoneEnderecoByIdPessoaTpTelefone(Long idPessoa, String tpTelefone, String tpUso) {
		return getTelefoneEnderecoDAO().findByIdPessoaTpTelefone(idPessoa, tpTelefone, tpUso);
	}	

	/**
	 * Para um dado <code>EnderecoPessoa</code>, tenta encontrar algum objeto do tipo <code>TelefoneEndereco</code>
	 * que seja de um dos tipos de telefone passado no array de strings (Ex. "C" (comercial), "R" (residencial),...)
	 * Os tipos s�o procurados na ordem do array.
	 * Caso n�o encontre, retorna null.
	 * Moacir Zardo Junior - 28-11-2005 
	 * @param enderecoPessoa
	 * @param tiposTelefone
	 * @return TelefoneEndereco
	 */
	public TelefoneEndereco getTelefonePorTiposTelefone(EnderecoPessoa enderecoPessoa, String[] tiposTelefone){
		List telefoneEnderecos = enderecoPessoa.getTelefoneEnderecos();
		TelefoneEndereco telefoneEndereco = null;
		if (telefoneEnderecos!=null){
			for (int i = 0; i < tiposTelefone.length; i++) {
				for (Iterator iterator = telefoneEnderecos.iterator(); iterator.hasNext();) {
					telefoneEndereco = (TelefoneEndereco) iterator.next();
					if (telefoneEndereco.getTpTelefone().getValue().equalsIgnoreCase(tiposTelefone[i])){
						return telefoneEndereco;
					}
				}
			}
		}
		return telefoneEndereco;
	}

	protected void beforeRemoveById(Long id) {
		TelefoneEndereco telefoneEndereco = (TelefoneEndereco)findById(id);
		Short cdEspecializacao = null;

		//Se � o �ltimo telefone, n�o pode apagar ele se ele pretence a uma dos tipos de pessoa informado.
		if (isLastTelefonePessoa(telefoneEndereco.getPessoa().getIdPessoa())){
			List lstCdEspecializacao = new ArrayList();
			
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CLIENTE_ESPECIAL);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL_CIA_AEREA);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CON_POSTO_PASSAGEM);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_PROPRIETARIO);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_MOTORISTA);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_OPERADORA_MCT);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_DESPACHANTE);
			
			cdEspecializacao = especializacaoPessoaService.isEspecializado(telefoneEndereco.getPessoa().getIdPessoa(), lstCdEspecializacao);
		}

		//Se s� tem 6 telefones e que a pessoa � um motorista eventual, lan�ar uma exception
		if (cdEspecializacao == null && isSixthTelefonePessoa(telefoneEndereco.getPessoa().getIdPessoa())){
			List lstCdEspecializacao = new ArrayList();
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_MOTORISTA_EVENTUAL);
			cdEspecializacao = especializacaoPessoaService.isEspecializado(telefoneEndereco.getPessoa().getIdPessoa(), lstCdEspecializacao);
		}		

		if (cdEspecializacao != null) {
			throw new BusinessException("LMS-27066", new Object[]{especializacaoPessoaService.getLabel(cdEspecializacao)});
		}		

		//Valida se o usuario logado pode alterar a pessoa
    	pessoaService.validateAlteracaoPessoa(telefoneEndereco.getPessoa().getIdPessoa());

		super.beforeRemoveById(id);
	}

	/**
	 * Informa se � o �ltimo telefone endere�o da pessoa.
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isLastTelefonePessoa(Long idPessoa){
		List lstTelefone = findByIdPessoa(idPessoa);
		return (lstTelefone.size() < 2);
	}

	/**
	 * Informa se s� existe seis telefone endere�o da pessoa.
	 * 
	 * @author Micka�l Jalbert
	 * @since 25/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isSixthTelefonePessoa(Long idPessoa){
		List lstTelefone = findByIdPessoa(idPessoa);

		// Caso tenha 6 ou menos que 6 telefones, retorna true 
		return (lstTelefone.size() < 7);
	}	

	/**
	 * M�todo que busca a pagina��o de telefone endere�o.
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedTelefoneEndereco(TypedFlatMap criteria) {
		return this.getTelefoneEnderecoDAO().findPaginatedTelefoneEndereco(criteria, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * M�todo que busca a quantidade de telefone endere�o.
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountTelefoneEndereco(TypedFlatMap criteria) {
		return this.getTelefoneEnderecoDAO().getRowCountTelefoneEndereco(criteria, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			removeById((Long) iter.next());
		}
	}
	
	/**
	 * Exclui TelefoneEnderecos de acordo com a lista de ids.
	 * TODO - m�todo solicitado pela integra��o.
	 * 
	 * Hector Julian Esnaola Junior
	 * 14/02/2008
	 *
	 * @param ids
	 *
	 * void
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsDefault(List ids) {
		super.removeByIds(ids);
	}

	@Override
	protected TelefoneEndereco beforeStore(TelefoneEndereco bean) {
		TelefoneEndereco te = (TelefoneEndereco) bean;
		//Celular ou R�dio
		if( (te.getTpTelefone() != null && te.getTpTelefone().getValue().equalsIgnoreCase("E")) ){
			if( te.getEnderecoPessoa() != null ){			
				throw new BusinessException("LMS-27085");
			}
		}		

		//Se a pessoa � um cliente, n�o pode mudar os dados se a filial da sess�o � differente da filial comercial do cliente
		//N�o valida altera��o, pois � um endere�o novo do tipo "COL" a ser cadastrado pela tela de Pedido Coleta		
		List listTiposEnderecoPessoa = null;
		if ( te.getEnderecoPessoa() != null ){
			listTiposEnderecoPessoa = te.getEnderecoPessoa().getTipoEnderecoPessoas();
		}
		if ( listTiposEnderecoPessoa != null
				&& listTiposEnderecoPessoa.size() == 1
				&& ((TipoEnderecoPessoa)listTiposEnderecoPessoa.get(0)).getTpEndereco().getValue().equals("COL")
		){
		} else {
			//Valida se o usuario logado pode alterar a pessoa
			pessoaService.validateAlteracaoPessoa(te.getPessoa().getIdPessoa());
		}
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TelefoneEndereco bean) {
		return super.store(bean);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getTelefoneEnderecoDAO().findPaginated(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getTelefoneEnderecoDAO().getRowCount(criteria);
	}

	/**
	 * Carrega o TelefoneEndereco de acordo com os filtros
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/12/2006
	 *
	 * @param idPessoa
	 * @param ddd
	 * @param nrTelefone
	 * @return
	 *
	 */
	public TelefoneEndereco findByDddNrTelefone( Long idPessoa, String ddd, String nrTelefone ){
		return getTelefoneEnderecoDAO().findByDddNrTelefone(idPessoa, ddd, nrTelefone);
	}

	public List<TelefoneEndereco> findByEnderecoPessoa(Long idPessoa, Long idEnderecoPessoa) {
		return getTelefoneEnderecoDAO().findByEnderecoPessoa(idPessoa, idEnderecoPessoa);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setTelefoneEnderecoDAO(TelefoneEnderecoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private TelefoneEnderecoDAO getTelefoneEnderecoDAO() {
		return (TelefoneEnderecoDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setEspecializacaoPessoaService(EspecializacaoPessoaService especializacaoPessoaService) {
		this.especializacaoPessoaService = especializacaoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public TelefoneContatoService getTelefoneContatoService() {
		return telefoneContatoService;
	}

	public void setTelefoneContatoService(
			TelefoneContatoService telefoneContatoService) {
		this.telefoneContatoService = telefoneContatoService;
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedTelefoneEnderecoPessoa(TypedFlatMap tfm) {
		return super.findPaginated(tfm);
	}

	public Integer getRowCountTelefoneEnderecoPessoa(TypedFlatMap tfm) {
		return super.getRowCount(tfm);
	}
	
	/**
	 * 
	 * @param bean
	 * @param listContatos
	 * 
	 * @return java.io.Serializable
	 */
	public java.io.Serializable store(TelefoneEndereco bean, Map<String, Object> listContatos) {
		Long idTelefoneEndereco = (Long) this.store(bean);
		
		/* 
		 * Depois de atualizar o telefone, realiza dos contatos.
		 */
		updateContatoPessoa(idTelefoneEndereco, listContatos);
		
		return idTelefoneEndereco;
	}
	
	/**
	 * Este m�todo foi criado para substituir uma tela de CRUD, baseado em um
	 * componente select multiple values, para dizer os contatos de um telefone.
	 * 
	 * @param toAdd
	 * @param toRemove
	 */
	@SuppressWarnings("unchecked")
	private void updateContatoPessoa(Long idTelefoneEndereco, Map<String, Object> listContatos){	
		if(listContatos == null || listContatos.isEmpty()){
			return;
		}
		
		List<Map<String, Object>> added = (List<Map<String, Object>>) listContatos.get("added");
		List<Map<String, Object>> removed = (List<Map<String, Object>>) listContatos.get("removed");
		List<Map<String, Object>> updated = (List<Map<String, Object>>) listContatos.get("updated");
		
		if(!removed.isEmpty()){
			List<Long> toRemove = new ArrayList<Long>();
			
			for (Map<String, Object> contatoPessoa : removed) {
				toRemove.add(MapUtils.getLong(MapUtils.getMap(contatoPessoa, "contato"), "idTelefoneContato"));
			}
			
			telefoneContatoService.removeByIds(toRemove);
		}
		
		if(!updated.isEmpty()){
			added.addAll(updated);
		}
		
		if(!added.isEmpty()){
			TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
			telefoneEndereco.setIdTelefoneEndereco(idTelefoneEndereco);
			
			List<TelefoneContato> toAdd = new ArrayList<TelefoneContato>();
			for (Map<String, Object> contatoPessoa : added) {
				TelefoneContato telefoneContato = new TelefoneContato();
				telefoneContato.setIdTelefoneContato(MapUtils.getLong(MapUtils.getMap(contatoPessoa, "contato"), "idTelefoneContato"));
				telefoneContato.setTelefoneEndereco(telefoneEndereco);
				telefoneContato.setNrRamal(MapUtils.getString(contatoPessoa, "nrRamal"));
				
				Contato contato = new Contato();
				contato.setIdContato(MapUtils.getLong(MapUtils.getMap(contatoPessoa, "contato"), "idContato"));
				telefoneContato.setContato(contato);				
								
				toAdd.add(telefoneContato);
			}
			
			telefoneContatoService.storeAll(toAdd);
		}		
	}

	public List<Map<String, Object>> findTelefonePessoa(Long idPessoa) {		
		return getTelefoneEnderecoDAO().findTelefonePessoa(idPessoa);
	}
	
	public TelefoneEndereco findByPessoaEnderecoVigente(Pessoa pessoa){
		return getTelefoneEnderecoDAO().findByPessoaEnderecoVigente(pessoa);
	}
}