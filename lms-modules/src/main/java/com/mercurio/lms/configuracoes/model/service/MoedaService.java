package com.mercurio.lms.configuracoes.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.dao.MoedaDAO;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de servi�o para CRUD:
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.moedaService"
 */
public class MoedaService extends CrudService<Moeda, Long> {


	private EnderecoPessoaService enderecoPessoaService;
	private FilialService filialService;

	/**
	 * Recupera uma inst�ncia de <code>Moeda</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Moeda findById(java.lang.Long id) {
		return (Moeda)super.findById(id);
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
	public java.io.Serializable store(Moeda bean) {
		return super.store(bean);
	}

	/**
	 * Retorna uma cole��o de Moeda vinculadas em MoedaPais: se um Pais for informado nos criterions, a cole��o ser� apenas de Moeda
	 * que t�m v�nculo com o Pais, em MoedaPais.<BR> 
	 * @param criterions
	 * @return
	 * @deprecated Usar a ConfiguracoesFacade.getMoeda()
	 */
	public List findMoedasByPais(Map criterions){
		criterions.put("idPais",ReflectionUtils.getNestedBeanPropertyValue(criterions,"moedaPais.pais.idPais"));
		criterions.remove("moedaPais.pais.idPais");
		List list = getMoedaDAO().findMoedasByPais(criterions);
		if (list.isEmpty() && StringUtils.isBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criterions,"moedaPais.pais.idPais"))){
			return null;
		} else if (list.isEmpty()) {
			throw new BusinessException("LMS-00027");
		}
		return list;
	}

	/**
	 * Retorna uma cole��o de Moeda vinculadas em MoedaPais: se um Pais for informado nos criterions, a cole��o ser� apenas de Moeda
	 * que t�m v�nculo com o Pais, em MoedaPais.<BR> 
	 * @param criterions
	 * @return
	 * @deprecated Usar a ConfiguracoesFacade.getMoeda()
	 */
	public List findMoedasByPaisCombo(Map criterions) {
		criterions.put("idPais",ReflectionUtils.getNestedBeanPropertyValue(criterions,"pais.idPais")); 
		criterions.remove("moedaPais.pais.idPais");
		List list = getMoedaDAO().findMoedasByPais(criterions);
		return list;
	}

	/**
	 * Traz apenas as moedas conforme o Pa�s do usu�rio logado e que n�o estejam inativas, 
	 * tanto na tabela MOEDA quanto na MOEDA_PAIS.
	 * @param criterios
	 * @return
	 */
	public List findMoedasAtivasByPaisUsuario(Map criterios){
		Moeda moeda = (Moeda) SessionContext.get(SessionKey.MOEDA_KEY);
		Pais pais = (Pais) SessionContext.get(SessionKey.PAIS_KEY);
		criterios = new HashMap();
		criterios.put("idMoeda",moeda.getIdMoeda());
		criterios.put("idPais",pais.getIdPais());
		return this.getMoedaDAO().findMoedasAtivasByPaisUsuario(criterios);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setMoedaDAO(MoedaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private MoedaDAO getMoedaDAO() {
		return (MoedaDAO) getDao();
	}

	public Moeda findMoedaByUsuarioEmpresa(com.mercurio.lms.configuracoes.model.Usuario usuario, Empresa empresa) {
		// obtem a Filial(pessoa) via usuario
		Filial filial = filialService.findFilialPadraoByUsuarioEmpresa(usuario, empresa); 

		// obtem o enderecopessoa padrao da pessoa
		EnderecoPessoa enderecoPessoa = this.enderecoPessoaService.findEnderecoPessoaPadrao(filial.getIdFilial());
		if(null == enderecoPessoa)
			throw new BusinessException("filialSemEndereco");

		Pais pais = enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais();
		if(null == pais)
			throw new BusinessException("enderecoFilialSemPais");

		// retorna a moeda padrao do pais
		return getMoedaDAO().findMoedaPadraoByPais(pais.getIdPais());
	}

	/**
	 * M�todo que retorna as moedas do pais informado, ativo.
	 * 
	 * @param Long idPais
	 * @param Boolean blAtivo
	 * @return List
	 * */
	public List<Moeda> findMoedaByPais(Long idPais, Boolean blAtivo){
		String strAtivo = null;
		if (blAtivo.equals(Boolean.TRUE)){
			strAtivo = "A";
		}
		return getMoedaDAO().findMoedaByPais(idPais, strAtivo);
	}

	/**
	 * Retorna a moeda pelo c�digo num�rico ISO
	 * 
	 * @param nrIsoCode
	 * @return Moeda
	 */
	public Moeda findMoedaByNrIsoCode(Short nrIsoCode) {
		return getMoedaDAO().findMoedaByNrIsoCode(nrIsoCode);
	}

	/**
	 * M�todo para a obtencao de moedas, ativas ou n�o, ordenadas por (sigla + simbolo).<br>
	 * Recomendado o uso em combos que apresentem (sigla + simbolo).
	 * @param somenteAtivos
	 * @return
	 * @author luisfco
	 */
	public List findMoedaOrderBySgSimbolo(boolean somenteAtivos) {
		return getMoedaDAO().findMoedaOrderBySgSimbolo(somenteAtivos);
	}

	/**
	 * Retorna a moeda padr�o do pais da sigla informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 18/05/2006
	 * 
	 * @param String sgPais
	 * @return Moeda

	 */
	public Moeda findMoedaPadraoBySiglaPais(String sgPais) {
		return getMoedaDAO().findMoedaPadraoBySiglaPais(sgPais);
	}

	public Moeda findMoedaPadraoByPais(Long idPais) {
		return getMoedaDAO().findMoedaPadraoByPais(idPais);
	}

	/**
	 * 
	 * Informa o id do DepositoCcorrente e retorna a moeda do primeiro itemDepositoCcorrente
	 * 
	 * @author Micka�l Jalbert
	 * @since 18/05/2006
	 * 
	 * @param Long idDepositcoCcorrente
	 * @return Moeda
	 * */
	public Moeda findMoedoByDepositoCcorrente(Long idDepositcoCcorrente){		
		return getMoedaDAO().findMoedoByDepositoCcorrente(idDepositcoCcorrente);
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setFilialService(FilialService pessoaService) {
		this.filialService = pessoaService;
	}
}