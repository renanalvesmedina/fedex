package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.dao.ContaBancariaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.contaBancariaService"
 */
public class ContaBancariaService extends CrudService<ContaBancaria, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma instância de <code>ContaBancaria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ContaBancaria findById(java.lang.Long id) {
		return (ContaBancaria)super.findById(id);
	}
	
	/**
	 * Utilizar o metodo findPaginatedContaBancaria. <B>(frontEnd)</B>.
	 * 
	 * @see #findPaginatedDadosBancarios(Map)
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginated(Map criteria) {
		List filter = new ArrayList();
		filter.add("idContaBancaria");
		filter.add("pessoa.idPessoa");
		filter.add("pessoa.nmPessoa");
		filter.add("pessoa.nrIdentificacao");
		filter.add("pessoa.tpIdentificacao");
		filter.add("agenciaBancaria.banco.idBanco");
		filter.add("agenciaBancaria.banco.nmBanco");
		filter.add("agenciaBancaria.banco.nrBanco");
		filter.add("agenciaBancaria.idAgenciaBancaria");
		filter.add("agenciaBancaria.nmAgenciaBancaria");
		filter.add("agenciaBancaria.nrAgenciaBancaria");
		filter.add("idContaBancaria");
		filter.add("nrContaBancaria");
		filter.add("nrContaDvConta");
		filter.add("tpConta");
		filter.add("dvContaBancaria");
		filter.add("blPreferencialDeposito");
		filter.add("dtVigenciaInicial");
		filter.add("dtVigenciaFinal");

		ResultSetPage rsp = getContaBancariaDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), filter));
		return rsp;
	}

	/**
	 * FindPaginated de DadosBancariosService, que é uma extensão de 
	 * ContaBancariaService (do modulo Contratacao Veiculos).
	 * Favor não alterar sem consultar GT1.
	 * @author luisfco
	 */
	public ResultSetPage findPaginatedDadosBancarios(Map criteria, List filter) {
		ResultSetPage rsp = getContaBancariaDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), filter));
		return rsp;
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
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			removeById((Long) iter.next());
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ContaBancaria bean) {
		return super.store(bean);
	}

	/**
	 * Store da service DadosBancarios. Favor não alterar sem consultar GT1.
	 * @param bean
	 * @return
	 * @author luisfco
	 */
	public java.io.Serializable storeDadosBancarios(ContaBancaria bean) {
		getContaBancariaDAO().store(bean);
		return bean;
	}

	/**
	 * Retorna as contas bancárias de uma pessoa.
	 * @param idPessoa
	 * @return List de contas bancáriasda pessoa.
	 * @author luisfco
	 */
	public List findContasBancariasByPessoa(Long idPessoa) {
		return getContaBancariaDAO().findContasBancariasByPessoa(idPessoa);
	}

	/**
	 * Retorna as contas bancarias vigentes de uma pessoa.
	 * @param idPessoa 
	 * @return As contas bancárias vigentes da pessoa
	 * @author luisfco
	 */
	public List findContasBancariasVigentesByPessoa(Long idPessoa) {
		return getContaBancariaDAO().findContasBancariasVigentesByPessoa(idPessoa); 
	}

	/**
	 * Verifica se existe alguma conta bancária vigente para a pessoa.
	 * @param idPessoa
	 * @return True, em caso afirmativo.
	 * @author luisfco
	 */
	public boolean existeContaBancariaVigentePessoa(Long idPessoa) {
		return getContaBancariaDAO().existeContaBancariaVigentePessoa(idPessoa); 
	}

	public boolean existeContaBancariaVigentePeriodo(Long idPessoa, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getContaBancariaDAO().existeContaBancariaVigentePeriodo(idPessoa, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Verifica a existencia de se o proprietário possui situacao 
	 * incompleta (tpSituacao = N) bem como uma liberacao vigente  
	 * @param idProprietario
	 * @return
	 * @author luisfco
	 */
	public boolean existeProprietarioIncompletoComLiberacaoVigente(Long idProprietario) {
		return getContaBancariaDAO().existeProprietarioIncompletoComLiberacaoVigente(idProprietario);
	}

	/**
	 * Busca todos os proprietarios de situacao incompleta (tpSituacao = N)
	 * e possuidores de liberacao vigente, associados a um beneficiario específico  
	 * @param idBeneficiario
	 * @return List de Proprietario.
	 * @author luisfco
	 */
	public List findProprietariosIncompletosDoBeneficiarioComLiberacaoVigente(Long idBeneficiario) {
		return getContaBancariaDAO().findProprietariosIncompletosDoBeneficiarioComLiberacaoVigente(idBeneficiario);
	}

	/**
	 * Antes de inserir deve-se verificar se a conta Bancária que está sendo inserida está marcada
	 * como preferência para depósito
	 * @param bean É um Object que representa a conta Bancária a ser salva.
	 * @return O objeto salvo.
	 */
	protected ContaBancaria beforeStore(ContaBancaria bean) {
		ContaBancaria contaBancaria = (ContaBancaria) bean;

		// Caso a dtVigenciaFinal seja nula, verifica se já não existe outra. Caso exista lança a exception 
		if (contaBancaria.getDtVigenciaFinal() == null && !validateVigenciaEmAberto(contaBancaria)){
			throw new BusinessException("LMS-27038");
		}

		//Se a data final é menor que a data atual - 1
		if (contaBancaria.getDtVigenciaFinal() != null && contaBancaria.getDtVigenciaFinal().isBefore(JTDateTimeUtils.getDataAtual().minusDays(1))){
			throw new BusinessException("LMS-27054", new Object[]{1});
		}

		if (!getContaBancariaDAO().validateIntervaloVigencia(contaBancaria)){
			throw new BusinessException("LMS-00047");
		}

		if(!getContaBancariaDAO().existeProprietarioAssociadoEBeneficiarioVigente(
				contaBancaria.getPessoa().getIdPessoa(),
				contaBancaria.getDtVigenciaInicial(),
				contaBancaria.getDtVigenciaFinal())
		) {
			throw new BusinessException("LMS-27056");
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(contaBancaria.getPessoa().getIdPessoa());

		return super.beforeStore(bean);
	}

	protected ContaBancaria beforeInsert(ContaBancaria bean) {
		ContaBancaria contaBancaria = (ContaBancaria) bean;
		if (!validateVigenciaInicial(contaBancaria)){
			throw new BusinessException("LMS-27055");
		}
		return super.beforeInsert(bean);
	}

	/**
	 * Valida Data da Vigência Inicial.<BR>
	 * Data deve ser maior ou igual ao dia de hoje.<BR> 
	 * @param contaBancaria
	 * @return
	 */
	private boolean validateVigenciaInicial(ContaBancaria contaBancaria){
		if (contaBancaria.getDtVigenciaInicial() == null) return false;
		return (JTDateTimeUtils.comparaData(contaBancaria.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) >= 0) ;
	}

	/**
	 * Validação de Contas em aberto.<BR>
	 * Quando estiver inserindo uma nova Conta Bancaria, não pode existir outra conta com data da vigência em aberto.<BR>
	 * Ao atualizar uma Conta que está com a vigencia final em aberto, não pode existir outra em aberto.<BR> 
	 * @param contaBancaria
	 * @return
	 */
	private boolean validateVigenciaEmAberto(ContaBancaria contaBancaria){
		return getContaBancariaDAO().validateVigenciaEmAberto(contaBancaria);
	}

	public boolean findDadosBancariosByFilialCiaArea(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
		return getContaBancariaDAO().findDadosBancariosByFilialCiaArea(dataInicio,  dataFim, idFilialCiaAerea);
	}

	/**
	 * Retorna a lista de todas as contas bancarias vigente.
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/08/2006
	 * 
	 * @param Long idPessoa
	 * @param YearMonthDay dtVigencia
	 * @return List
	 */
	public List findByPessoa(Long idPessoa, YearMonthDay dtVigencia){
		return getContaBancariaDAO().findByPessoa(idPessoa, dtVigencia);
	}

	protected void beforeRemoveById(Long id) {
		ContaBancaria contaBancaria = (ContaBancaria)findById(id);

		// Caso a contaBancaria seja vigente, lançar uma exception
		if (contaBancaria.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual())){
			throw new BusinessException("LMS-27089");
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(contaBancaria.getPessoa().getIdPessoa());

		super.beforeRemoveById(id);
	}

	/**
	 * Informa se é a última conta bancária da pessoa.
	 * 
	 * @author Mickaël Jalbert
	 * @since 24/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isLastContaBancariaPessoa(Long idPessoa, Long idContaBancaria){
		List lstContaBancaria = getContaBancariaDAO().findContaBancariaVigenteByPessoa(idPessoa, idContaBancaria, JTDateTimeUtils.getDataAtual());

		if (lstContaBancaria.size() == 0){
			return true;
		}

		return false;
	}   

	/**
	 * Retorna as contas bancárias vigentes da pessoa, exceto a conta em questão
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 27/06/2007
	 *
	 * @param idPessoa
	 * @param dtVigencia
	 * @return
	 *
	 */
	public List findContaBancariaVigenteByPessoa(Long idPessoa, Long IdContaBancaria, YearMonthDay dtVigencia){
		return getContaBancariaDAO().findContaBancariaVigenteByPessoa(idPessoa, IdContaBancaria, dtVigencia);
	}

	/**
     * Carrega ContaBancaria de acordo com idPessoa, dtVigenciaInicial e dtVigenciaFinal.
     *
     * @author Hector Julian Esnaola Junior
     * @since 26/10/2007
     *
     * @param idPessoa
     * @param dtVigenciaInicial
     * @param dtVigenciaFinal
     * @return
     *
     */
    public ContaBancaria findContaBancaria(
    		Long idPessoa, 
    		YearMonthDay dtVigenciaInicial, 
    		YearMonthDay dtVigenciaFinal) {
    		return getContaBancariaDAO().findContaBancaria(
    				idPessoa, 
    				dtVigenciaInicial, 
    				dtVigenciaFinal);
    }
    
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setContaBancariaDAO(ContaBancariaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ContaBancariaDAO getContaBancariaDAO() {
		return (ContaBancariaDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	/**
	 * retorna true se conta bancaria for de um proprietario ou posto conveniado
	 * @return
	 */
	public boolean findIsContaBancariaProprietarioOuPostoConveniado(ContaBancaria contaBancaria){
		return getContaBancariaDAO().findIsContaBancariaProprietarioOuPostoConveniado(contaBancaria.getIdContaBancaria());
	}
	
	/**
	 * Retorna página de resultados para a tela de listagem.
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage<ContaBancaria> findPaginatedDadosBancarios(Map<String, Object> criteria) {
		return getContaBancariaDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
	}

	public List<Map<String, Object>> findDadosBancariosPessoa(Long idPessoa) {		
		return getContaBancariaDAO().findDadosBancariosPessoa(idPessoa);
	}
}