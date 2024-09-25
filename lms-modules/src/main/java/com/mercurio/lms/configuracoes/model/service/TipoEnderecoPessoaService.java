package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.dao.TipoEnderecoPessoaDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.tipoEnderecoPessoaService"
 */
public class TipoEnderecoPessoaService extends CrudService<TipoEnderecoPessoa, Long> {
	private EnderecoPessoaService enderecoPessoaService;
	private PessoaService pessoaService;
	private DomainValueService domainValueService;

	/**
	 * Recupera uma inst�ncia de <code>TipoEnderecoPessoa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public TipoEnderecoPessoa findById(java.lang.Long id) {
		return (TipoEnderecoPessoa)super.findById(id);
	}

	/**
	 * Carrega a combo de tipo de endere�o
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 02/07/2007
	 *
	 * @param criteria
	 * @return
	 */
	public List findTipoEnderecoPessoa(Map criteria){
		List dominiosValidos = new ArrayList();
		dominiosValidos.add("COB");
		dominiosValidos.add("COL");
		dominiosValidos.add("ENT");
		dominiosValidos.add("MDA");

		Pessoa pessoa =  pessoaService.findById(Long.valueOf(criteria.get("idPessoa").toString()));
		// Caso o endere�o pesso seja de uma pessoa fisica, carrega o tipo RES
		if ( "F".equals(pessoa.getTpPessoa().getValue()) ) {
			 dominiosValidos.add("RES");
		// Caso o endere�o pesso seja de uma pessoa juridica, carrega o tipo COm
		} else {
			dominiosValidos.add("COM");
		}

		List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_ENDERECO", dominiosValidos);
		return retorno;
	}

	/**
	 * Criado para buscar o TipoEnderecoPessoa sem os dados de pessoa j� recebidos da tela que o chamou
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 05/12/2006
	 *
	 * @param id
	 * @return
	 */
	public List findByIdCustomized(Long id){
		List lista = null;
		TypedFlatMap tfm = null;		
		TipoEnderecoPessoa tep = getTipoEnderecoPessoaDAO().findByIdCustomized(id);

		if( tep != null ){
			tfm = new TypedFlatMap();
			tfm.put("idTipoEnderecoPessoa",tep.getIdTipoEnderecoPessoa());
			tfm.put("enderecoPessoa.idEnderecoPessoa",tep.getEnderecoPessoa().getIdEnderecoPessoa());
			tfm.put("tpEndereco",tep.getTpEndereco());
			lista = new ArrayList();
			lista.add(tfm);
		}

		return lista;
	}

	@Override
	protected void beforeRemoveById(Long id) {
		TipoEnderecoPessoa tep = (TipoEnderecoPessoa) findById(id);

		validateTipoEndereco(tep);

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(pessoaService.findIdPessoaByIdTipoEnderecoPessoa((Long)id));

		super.beforeRemoveById(id);
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 06/03/2007
	 *
	 * @param tep
	 * @throws BusinessException
	 */
	private void validateTipoEndereco(TipoEnderecoPessoa tep) throws BusinessException {
		// Caso o tipo de endere�o seja de cobranca ou de residencia, lan�a a exception REGRA 3.3
		if( "RES".equals(tep.getTpEndereco().getValue()) || "COM".equals(tep.getTpEndereco().getValue()) ){
			throw new BusinessException("LMS-27091");

		// Caso seja o �ltimo tipo endere�o do endereco pessoa, lan�a a exception
		} else {
			List lst = enderecoPessoaService.findTipoEnderecoPessoaByIdEnderecoPessoa( tep.getEnderecoPessoa().getIdEnderecoPessoa() );
			if(lst != null && lst.size() <= 1) {
				throw new BusinessException("LMS-27090");
			}
		}
	}

	@Override
	protected void beforeRemoveByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long idTipoEndereco = (Long) iter.next();
			beforeRemoveById(idTipoEndereco);
		}
		super.beforeRemoveByIds(ids);
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
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TipoEnderecoPessoa bean) {
		return super.store(bean);
	}
	
	/**
	 * Executa a grava��o de v�rios objetos TipoEnderecoPessoa.</br>
	 * 
	 * <b>Como o store padr�o possui valida��es de neg�cio, � necess�rio que para cada
	 * item a ser salvo, sejam executadas estas valida��es.
	 */
	public void storeAllTipoEnderecoPessoa(List<TipoEnderecoPessoa> listTipoEnderecoPessoa) {
		for (TipoEnderecoPessoa tipoEnderecoPessoa : listTipoEnderecoPessoa) {
			validarTipoEnderecoPessoa(tipoEnderecoPessoa);
		}
		
		super.storeAll(listTipoEnderecoPessoa);
	}

	/** 
	 * Validar informa��es antes de efetuar grava��o
	 * 
	 */
	protected TipoEnderecoPessoa beforeStore(TipoEnderecoPessoa bean) {
		validarTipoEnderecoPessoa(bean);

		return super.beforeStore(bean);
	}

	/**
	 * Executa a valida��o para verificar se o tipo endere�o pessoa j� existe.
	 * 
	 * @param tipoEnderecoPessoa
	 */
	private void validarTipoEnderecoPessoa(TipoEnderecoPessoa tipoEnderecoPessoa){
		Long idTipoEnderecoPessoa = tipoEnderecoPessoa.getIdTipoEnderecoPessoa();
		
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(tipoEnderecoPessoa.getEnderecoPessoa().getIdEnderecoPessoa());

		YearMonthDay dtVigenciaInicial = enderecoPessoa.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = enderecoPessoa.getDtVigenciaFinal();		
		Long idPessoa = tipoEnderecoPessoa.getEnderecoPessoa().getPessoa().getIdPessoa();
		String tpEndereco = tipoEnderecoPessoa.getTpEndereco().getValue();

		this.getTipoEnderecoPessoaDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().evict(enderecoPessoa);

		if (!getTipoEnderecoPessoaDAO().validateTpEndereco(idTipoEnderecoPessoa,dtVigenciaInicial,dtVigenciaFinal,idPessoa,tpEndereco)) {
			throw new BusinessException("LMS-27045");	
		}
	}
	
	/**
	 * O controle de conex�o me obriga a criar um m�todo que come�a por execute
	 */
	public boolean executeIsComercialOrResidencial(Long idEnderecoPessoa){
		return getTipoEnderecoPessoaDAO().isComercialOrResidencial(idEnderecoPessoa);
	}	

	/**
	 * Retorna 'true' de o endereco informado tem o tipo comercial ou residencial
	 * 
	 * @author Micka�l Jalbert
	 * @since 09/03/2007
	 * @param idEnderecoPessoa
	 * @return
	 */
	public boolean isComercialOrResidencial(Long idEnderecoPessoa){
		return getTipoEnderecoPessoaDAO().isComercialOrResidencial(idEnderecoPessoa);
	}

	public TipoEnderecoPessoa findTipoEnderecoPessoaByEnderecoPessoa(Long idEnderecoPessoa) {		
		return getTipoEnderecoPessoaDAO().findTipoEnderecoPessoaByEnderecoPessoa(idEnderecoPessoa);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setTipoEnderecoPessoaDAO(TipoEnderecoPessoaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private TipoEnderecoPessoaDAO getTipoEnderecoPessoaDAO() {
		return (TipoEnderecoPessoaDAO) getDao();
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}