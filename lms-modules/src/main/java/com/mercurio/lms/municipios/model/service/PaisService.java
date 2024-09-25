package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.dao.PaisDAO;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de servi�o para CRUD: 
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.paisService"
 */ 
public class PaisService extends CrudService<Pais, Long> {

	private FilialService filialService;
	private EnderecoPessoaService enderecoPessoaService;

	public void setFilialService(FilialService pessoaService) {
		this.filialService = pessoaService;
	}
	
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * Recupera uma inst�ncia de <code>Pais</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
	public Pais findById(java.lang.Long id) {
		return (Pais)super.findById(id);
	}

	/**
	 * Getter de Pais contido na SessionContext
	 * @return
	 */
	public Pais getSessionPais(){
		return (Pais) SessionContext.get("PAIS_KEY");
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
	 *
	 *
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
	// FIXME corrigir para retornar o ID
	public Pais store(Pais bean) {
		super.store(bean);
		return bean;
	}

	/**
	 * @author Jos� Rodrigo Moraes
	 * @since 06/07/2006
	 * 
	 * M�todo descomentado: Troca de Map para TypedFlatMap evitando assim o uso da ReflectionUtils 
	 * 
	 * Retorna uma cole��o de Pais vinculadas em MoedaPais: se uma Moeda for informada nos criterions, a cole��o ser� apenas de Pais
	 * que t�m v�nculo com a Moeda, em MoedaPais.<BR> 
	 * 
	 * @param criterions Crit�rios de pesquisa
	 * @return Lista de moedasPais
	 */	
	public List findPaisesByMoeda(TypedFlatMap criterions){	
		List list = getPaisDAO().findPaisesByMoedaLookup(criterions);
		if (list.isEmpty() && criterions.getLong("moedaPais.moeda.idMoeda") == null ){
			return null;
		} else if (list.isEmpty()){
			throw new BusinessException("LMS-00028");
		}
		return list;
	}	
	
	/**
	 * Retorna o id do pais do endere�o padr�o (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/07/2006
	 * 
	 * @param Long idPessoa
	 * @return Long
	 */
	public Long findIdPaisByIdPessoa(Long idPessoa){	
		return getPaisDAO().findIdPaisByIdPessoa(idPessoa);
	}
	
	/**
	 * Retorna a sigla do pais do endere�o padr�o (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 01/09/2006
	 * 
	 * @param Long idPessoa
	 * @return String
	 */
	public String findSgPaisByIdPessoa(Long idPessoa){	
		return getPaisDAO().findSgPaisByIdPessoa(idPessoa);
	}
	
	/**
	 * Retorna o nome do pais do pais informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 27/09/2006
	 * 
	 * @param Long idPais
	 * @return String
	 */
	public String findNmPaisById(Long idPais){	
		return getPaisDAO().findNmPaisById(idPais);
	}		
	
	/**
	 * Retorna o pais do endere�o padr�o (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 22/06/2006
	 * 
	 * @param Long idPessoa
	 * @return Pais
	 */
	public Pais findByIdPessoa(Long idPessoa){
		return getPaisDAO().findByIdPessoa(idPessoa);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setPaisDAO(PaisDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private PaisDAO getPaisDAO() {
		return (PaisDAO) getDao();
	}

	public Map findPaisUsuarioLogado() {
		Pais p = (Pais)SessionContext.get(SessionKey.PAIS_KEY);
		return getPaisDAO().findByIdPais(p.getIdPais());
	}

	public List findByNmPais(String nmPais) {
		return getPaisDAO().findByNmPais(nmPais, null);
	}

	public List findPaisLookup(Map criteria){
		String nmPais = (String) criteria.get("nmPais");
		if(StringUtils.isNotBlank(nmPais)) {
			String tpSituacao = (String) criteria.get("tpSituacao");
			return getPaisDAO().findByNmPais(nmPais, tpSituacao);
		}
		return null;
	}

	public List findByNomeUfMunicipio(String nmPais,Long idUf,Long idMunicipio) {
		return getPaisDAO().findByNomeUfMunicipio(nmPais,idUf,idMunicipio);
	}

	public Map findByIdPais(Long idPais){
		return getPaisDAO().findByIdPais(idPais);
	}

	public Pais findPaisByUsuarioEmpresa(Usuario usuario, Empresa empresa) {
		// obtem a pessoa via usuario
		Filial filial = filialService.findFilialPadraoByUsuarioEmpresa(usuario, empresa);

		// obtem o enderecopessoa padrao da pessoa
		EnderecoPessoa enderecoPessoa = this.enderecoPessoaService.findEnderecoPessoaPadrao(filial.getIdFilial());

		// retorna o pais via enderecopessoa
		return enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais();
	}

	public Pais findPaisByIdMunicipio(Long idMunicipio) {
		return getPaisDAO().findPaisByIdMunicipio(idMunicipio);
	}
	
	public Pais findPaisBySgPais(String sgPais)	{
		return getPaisDAO().findPaisBySgPais(sgPais);
	}

	public Pais findPaisByIdFilialSessao(Long idFilialsessao) {

		EnderecoPessoa enderecoPessoa = this.enderecoPessoaService.findEnderecoPessoaPadrao(idFilialsessao);

		return enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais();

	}
}
