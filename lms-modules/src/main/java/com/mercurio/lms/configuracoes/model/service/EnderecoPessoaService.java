package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.HistoricoBoletoDMN;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.dao.EnderecoPessoaDAO;
import com.mercurio.lms.contratacaoveiculos.model.service.ExecuteWorkflowProprietarioService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de servi?o para CRUD:
 * 
 * N?o inserir documenta??o ap?s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi?o.
 * @spring.bean id="lms.configuracoes.enderecoPessoaService"
 */

@Transactional
public class EnderecoPessoaService extends CrudService<EnderecoPessoa, Long> {
	private TipoEnderecoPessoaService tipoEnderecoPessoaService; 
	private TelefoneEnderecoService telefoneEnderecoService;
	private PessoaService pessoaService;
	private EspecializacaoPessoaService especializacaoPessoaService;
	private ClienteService clienteService;
	private PaisService paisService;
	private MunicipioService municipioService;	
	private ExecuteWorkflowProprietarioService executeWorkflowProprietarioService;

	/**
	 * Recupera uma inst?ncia de <code>EnderecoPessoa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst?ncia que possui o id informado.
	 */
	public EnderecoPessoa findById(java.lang.Long id) {
		return (EnderecoPessoa)super.findById(id);
	}

	public void attach(Object object) {
		getDao().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().lock(object, LockMode.NONE);
	}
	
	/**
	 * Lista de EnderecoPessoa com atributos especificos para o endere?o
	 * completo.<BR>
	 * <i>Utilizado na Combo de EnderecoPessoa</i><BR>
	 * <strong>ATENCAO:</strong> alias default: tipoLogradouro as tl
	 * 
	 *@author Robson Edemar Gehl
	 * @author Luis Carlos Poletto
	 * @param map
	 *            criterios
	 * @return
	 */
	public List<Map<String, Object>> findEnderecoCompletoCombo(Map map) {
		return getEnderecoPessoaDAO().findEnderecoCompletoCombo(map);
	}

	/**
	 * Busca EnderecoPessoa com os atributos espec?ficos para o endere?o completo, 
	 * vigente e vigente no futuro, utilizado na Combo.
	 * 
	 * @param Map map
	 * @return List
	 */
	public List findEnderecosPessoaVigentes(Map map){
		return getEnderecoPessoaDAO().findEnderecosPessoaVigentes(map);
	}
	
	// LMSA 6786: LMSA-7253
	public EnderecoPessoa findEnderecosVigentesByIdPessoaAndCep(Long idPessoa, String cep, boolean recuperarTipoEndereco){
		return getEnderecoPessoaDAO().findEnderecosVigentesByIdPessoaAndCep(idPessoa,cep, recuperarTipoEndereco);
	}

	/**
	 * Busca os endere?os, municipios e UF da pessoa passada por par?metro. 
	 * @param map Crit?rios 
	 * @return Lista de endere?os
	 */
	public List findEnderecoMunicipioUFCombo(Map map){
		List list = getEnderecoPessoaDAO().findEnderecoCompletoCombo(map);	
		return formatComboEndereco(list);
	}

	/**
	 * Busca os endere?os, municipios e UF da pessoa passada por par?metro. 
	 * @param map Crit?rios 
	 * @return Lista de endere?os vigentes da pessoa
	 */
	public List findEnderecoMunicipioUFComboVigenteFuturo(Map map){
		List list = getEnderecoPessoaDAO().findEnderecoCompletoComboVigenteFuturo(map);	
		return formatComboEndereco(list);
	}

	/**
	 * Formata os dados da combo endere?o por UF.
	 * 
	 * @param List list
	 * @return List
	 * */
	private List formatComboEndereco(List list){
		List retorno = new ArrayList();
		for(Iterator iter = list.iterator(); iter.hasNext();){
			EnderecoPessoa endereco = (EnderecoPessoa)iter.next();
			Map mapRetorno = new HashMap(2);

			String dsEndereco = "";
			dsEndereco = endereco.getTipoLogradouro().getDsTipoLogradouro() + " " + endereco.getDsEndereco() + ", " + ((endereco.getNrEndereco()!=null)?endereco.getNrEndereco():"N/D");
			dsEndereco += ((endereco.getDsComplemento()!=null)?" - " + endereco.getDsComplemento():"");
			dsEndereco += " - " + endereco.getMunicipio().getNmMunicipio() + "/" + endereco.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();

			mapRetorno.put("idEnderecoPessoa", endereco.getIdEnderecoPessoa());
			mapRetorno.put("enderecoCompleto", dsEndereco);
			retorno.add(mapRetorno);
		}
		return retorno;
	}

	/**
	 * Formata endere?o pessoa com todas as informa??es que forem informadas.
	 * Apenas ? obrigat?rio dsEndereco e nrEndereco. 
	 * <b>OBS: ele n?o implementa bairro e cep</b> 
	 * @see formatEnderecoPessoa
	 * @param EnderecoPessoa ep
	 * @return String no padr?o: "AV. Sert?rio, 324/A43 - Porto Alegre - RS/Bra
	 */
	public String formatEnderecoPessoaCompleto(EnderecoPessoa ep) {
		return formatEnderecoPessoaCompleto(ep,Boolean.FALSE);
	}

	private String formatEnderecoPessoaCompleto(EnderecoPessoa ep,Boolean withBairro) {
		if (ep == null)
			throw new IllegalArgumentException("O m?todo 'formatEnderecoPessoaCompleto' deve receber um EnderecoPessoa.");
		
		String dsEndereco = ep.getDsEndereco();
		String nrEndereco = ep.getNrEndereco();
		if (dsEndereco == null || nrEndereco == null)
			throw new IllegalArgumentException("O método 'formatEnderecoPessoaCompleto' deve receber " +
					"um EnderecoPessoa com dsEndereco e nrEndereco.");
		
		StringBuffer sb = new StringBuffer();
		
		TipoLogradouro tl = ep.getTipoLogradouro();
		if (tl != null && StringUtils.isNotBlank(tl.getDsTipoLogradouro().getValue())) {
			sb.append(tl.getDsTipoLogradouro().getValue()).append(" ");
		}		
		
		sb.append(dsEndereco).append(", ")
			.append(nrEndereco);
		
		if (ep.getDsComplemento() != null && StringUtils.isNotBlank(ep.getDsComplemento())) {
			sb.append("/").append(ep.getDsComplemento());
		}
		
		if (withBairro.booleanValue()) {
			if (StringUtils.isNotBlank(ep.getDsBairro()))
				sb.append(" - ").append(ep.getDsBairro());
			if (StringUtils.isNotBlank(ep.getNrCep()))
				sb.append(" - ").append(ep.getNrCep());
		}

		Municipio municipio = ep.getMunicipio();
		if (municipio != null && StringUtils.isNotBlank(municipio.getNmMunicipio())) {
			sb.append(" - ").append(municipio.getNmMunicipio());
			UnidadeFederativa uf = municipio.getUnidadeFederativa();
			if (uf != null && StringUtils.isNotBlank(uf.getSgUnidadeFederativa())) {
				sb.append(" - ").append(uf.getSgUnidadeFederativa());
				Pais pais = uf.getPais();
				if (pais != null && StringUtils.isNotBlank(pais.getSgPais())) {
					sb.append("/").append(pais.getSgPais());
				}
			}
		}
		return sb.toString();
	}
	/**
	 * Formata endere?o pessoa com todas as informa??es que forem informadas.
	 * Apenas ? obrigat?rio dsEndereco e nrEndereco.
	 * 
	 * @see formatEnderecoPessoaCompleto
	 * @param EnderecoPessoa ep
	 * @return String no padr?o: "AV. Sert?rio, 324/A43 - Cidade baixa - 93710000 - Porto Alegre - RS/Bra
	 */
	public String formatEnderecoPessoa(EnderecoPessoa ep) {
		return formatEnderecoPessoaCompleto(ep,Boolean.TRUE);
	}
	
	public String formatEnderecoPessoaComplemento(HistoricoBoletoDMN historicoBoletoDmn) {
		String dsEndereco = historicoBoletoDmn.getDsEnderecoDef();
		String nrEndereco = historicoBoletoDmn.getNrEnderecoDef();
		String dsTipoLogradouro = historicoBoletoDmn.getDsTipoLogradouroDef();
		String dsComplemento = historicoBoletoDmn.getDsComplementoDef();

		return formatEnderecoPessoaComplemento(dsTipoLogradouro, dsEndereco, nrEndereco, dsComplemento);
	}
	
	/**
	 * Formata endere?o pessoa com todas as informa??es que forem informadas.
	 * Apenas ? obrigat?rio dsEndereco e nrEndereco.
	 * 
	 * @param Long idEnderecoPessoa
	 * @return String no padr?o: "AV. Sert?rio, 324/A43"
	 */
	public String formatEnderecoPessoaComplemento(Long idEnderecoPessoa) {
		EnderecoPessoa ep = this.findById(idEnderecoPessoa);
		return formatEnderecoPessoaComplemento(ep);
	}

	/**
	 * Formata endere?o pessoa com todas as informa??es que forem informadas.
	 * Apenas ? obrigat?rio dsEndereco e nrEndereco.
	 * 
	 * @param EnderecoPessoa ep
	 * @return String no padr?o: "AV. Sert?rio, 324/A43"
	 */
	public String formatEnderecoPessoaComplemento(EnderecoPessoa ep) {
		if (ep == null)
			throw new IllegalArgumentException("O método 'formatEnderecoPessoaComplemento' deve receber um EnderecoPessoa.");

		String dsEndereco = ep.getDsEndereco();
		String nrEndereco = ep.getNrEndereco();
		String dsTipoLogradouro = ep.getTipoLogradouro().getDsTipoLogradouro().getValue();
		String dsComplemento = ep.getDsComplemento();

		return formatEnderecoPessoaComplemento(dsTipoLogradouro, dsEndereco, nrEndereco, dsComplemento);
	}

	/**
	 * Formata endere?o pessoa com todas as informa??es que forem informadas.
	 * Apenas ? obrigat?rio dsEndereco e nrEndereco.
	 * 
	 * @return String no padr?o: "AV. Sert?rio, 324/A43"
	 * 
	 * @param dsTipoLogradouro
	 * @param dsEndereco
	 * @param nrEndereco
	 * @param dsComplemento
	 * @return
	 */
	public String formatEnderecoPessoaComplemento(String dsTipoLogradouro, String dsEndereco, String nrEndereco, String dsComplemento){
		if (dsEndereco == null || nrEndereco == null)
			throw new IllegalArgumentException("O método 'formatEnderecoPessoaComplemento' deve receber " +
					"um EnderecoPessoa com dsEndereco e nrEndereco.");

		StringBuffer sb = new StringBuffer();

		if (StringUtils.isNotBlank(dsTipoLogradouro)) {
			sb.append(dsTipoLogradouro).append(" ");
		}		

		sb.append(dsEndereco).append(", ").append(nrEndereco);

		if (StringUtils.isNotBlank(dsComplemento)) {
			sb.append("/").append(dsComplemento);
		}
		return sb.toString();
	}

	/**
	 * Valida quantidade m?nima de Endereco Pessoa por Pessoa.<BR>
	 * Este servi?o verifica quantos endere?os uma pessoa possui, validando com a quantidade minima informada.<BR>
	 * O retorno ser? verdadeiro se a quantidade de endere?os for igual ou maior que o m?nimo informado. 
	 *@author Robson Edemar Gehl
	 * @param pessoa Pessoa a serem verificados seus endere?os 
	 * @param quantidadeMinima quantidade m?nima de endere?os que uma Pessoa pode ter (para verifica??o)
	 * @return Boolean.TRUE se quantidade m?nima contemplada com o parametro; Boolean.FALSE, caso contr?rio.
	 */
	public Boolean validateQuantidadeMinima(Pessoa pessoa, long quantidadeMinima){
		return getEnderecoPessoaDAO().validateQuantidadeMinima(pessoa, quantidadeMinima);
	}

	/**
	 * Valida Endereco da Pessoa (data vigencia e tipo de endere?o).<BR>
	 *@author Robson Edemar Gehl
	 * @param endereco
	 * @return Boolean.TRUE, endere?o v?lido; Boolean.FALSE, inv?lido.
	 */
	public Boolean validateEnderecoByTpEndereco(EnderecoPessoa endereco){
		if (endereco != null && endereco.getPessoa() != null) {
			// Carrega o enderecoPessoa para validar os tipoEnderecoPessoa
			EnderecoPessoa enderecoTmp = this.findById(endereco.getIdEnderecoPessoa());

			if(enderecoTmp.getTipoEnderecoPessoas() != null) {
				// Itera os tipoEnderecoPessoa do enderecoPessoa
				for (Iterator iter = enderecoTmp.getTipoEnderecoPessoas().iterator(); iter.hasNext();) {
					TipoEnderecoPessoa tep = (TipoEnderecoPessoa) iter.next();

					// Caso a pessoa seja juridica
					if (pessoaService.validateTipoPessoa(endereco.getPessoa().getIdPessoa(), "J"))
						if(tep.getTpEndereco().getValue().equals("COM"))
								return getEnderecoPessoaDAO().validateEnderecoByTpEndereco(enderecoTmp,"COM");

					// Caso a pessoa seja f?sica
					else if (pessoaService.validateTipoPessoa(endereco.getPessoa().getIdPessoa(), "F"))
						if(tep.getTpEndereco().getValue().equals("RES"))
							return getEnderecoPessoaDAO().validateEnderecoByTpEndereco(enderecoTmp,"RES");
				}
			}
			getEnderecoPessoaDAO().getAdsmHibernateTemplate().evict(enderecoTmp);
		}
		return Boolean.TRUE;
	}

	/**
	 * Insere um Tipo de Endere?o para o Endereco de Pessoa.
	 * COM: Quando for pessoa j?ridica
	 * RES: Quando for pessoa f?sica
	 * @param endereco
	 * @return
	 */
	protected Object insertTipoEnderecoPessoa(EnderecoPessoa endereco, String tpPessoa) {
		TipoEnderecoPessoa tipoEnderecoPessoa = new TipoEnderecoPessoa();
		tipoEnderecoPessoa.setEnderecoPessoa(endereco);
		if ("J".equals(tpPessoa)) {
			tipoEnderecoPessoa.setTpEndereco(new DomainValue("COM"));
		} else if ("F".equals(tpPessoa)) {
			tipoEnderecoPessoa.setTpEndereco(new DomainValue("RES"));
		}
		tipoEnderecoPessoaService.store(tipoEnderecoPessoa);
		return endereco;
	}

	/**
	 * Apaga uma entidade atrav?s do Id.
	 *
	 * @param id indica a entidade que dever? ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v?rias entidades atrav?s do Id.
	 *
	 * @param ids lista com as entidades que dever?o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			this.removeById((Long) iter.next());
		}
	}

	protected void beforeRemoveById(Long id) {
		EnderecoPessoa enderecoPessoa = (EnderecoPessoa)findById(id);

		// Se a data de vigencia inicial do EnderecoPessoa ? menor que a data atual, lan?a uma exception
		// Neste trecho n?o permite apagar os enderecoPessoa antigos e os vigentes
		if (enderecoPessoa.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual())){
			throw new BusinessException("LMS-27055");

		// Se a data de vig?ncia inicial do EnderecoPessoa ? igual a data atual, realiza outras valida??es antes de excluir
		} else if (!enderecoPessoa.getDtVigenciaInicial().isAfter(JTDateTimeUtils.getDataAtual())) {
			validateLastEndereco(enderecoPessoa.getPessoa().getIdPessoa(), enderecoPessoa.getIdEnderecoPessoa());
		}

		// REGRA 3.5 - Se ? um endere?o padr?o do futuro, tirar a data vigencia final do endere?o padr?o atual
		if (tipoEnderecoPessoaService.isComercialOrResidencial(enderecoPessoa.getIdEnderecoPessoa())){
			
			// Busca o endere?o posterior ao endere?o em quest?o.
			EnderecoPessoa enderecoPosterior = getEnderecoPessoaDAO().findMaiorEndererecoPessoa(
					enderecoPessoa.getPessoa().getIdPessoa(),
					enderecoPessoa.getDtVigenciaInicial(), 
					"F".equals(enderecoPessoa.getPessoa().getTpPessoa().getValue()) ? "RES" : "COM");
			
			// Busca o endere?o anterior ao endere?o em quest?o.
			EnderecoPessoa enderecoAnterior = findEnderecoPessoaSubstituidoByDtVigencia(
					enderecoPessoa.getPessoa().getIdPessoa(), 
					enderecoPessoa.getDtVigenciaInicial(), 
					enderecoPessoa.getPessoa().getTpPessoa().getValue());
			
			// Caso exista um endere?o posterior e um endere?o anterior ao endere?o em quest?o, deve setar 
			// na dtVigenciaFinal do endere?o anterior a vig?ncia inicial do endere?o posterior menos um dia, 
			// para que a pessoa n?o fique sem um endere?o vigente por um determinado per?odo.
			if (enderecoPosterior != null && enderecoAnterior != null) {
				enderecoAnterior.setDtVigenciaFinal(
				enderecoPosterior.getDtVigenciaInicial().minusDays(1));
				storeBasic(enderecoAnterior);
			// Caso exista um endere?o anterior ao endere?o em quest?o, abre a vig?ncia do anterior.
			} else if (enderecoAnterior != null) { 
				enderecoAnterior.setDtVigenciaFinal(null);
				storeBasic(enderecoAnterior);
				//pessoaService.storeEnderecoPessoaPadrao(enderecoPessoa.getPessoa(), enderecoAnterior);
			}
			
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(enderecoPessoa.getPessoa().getIdPessoa());

		for (TipoEnderecoPessoa tipoEnderecoPessoa: (List<TipoEnderecoPessoa>)enderecoPessoa.getTipoEnderecoPessoas()) {
			getEnderecoPessoaDAO().remove(tipoEnderecoPessoa);
			//getEnderecoPessoaDAO().getHibernateTemplate().flush();
		}
		getEnderecoPessoaDAO().getAdsmHibernateTemplate().flush();

		super.beforeRemoveById(id);
	}

	/**
	 * Valida se ? o pultimo registro, se ?, n?o pode modificar ou apagar o registro.
	 * 
	 * @author Micka?l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param enderecoPessoa
	 */
	private void validateLastEndereco(Long idPessoa, Long idEnderecoPessoa) {
		//Se ? o ?ltimo endere?o, n?o pode apagar ele se ele pretence a uma dos tipos de pessoa informado.
		if (isLastEnderecoPessoa(idPessoa, idEnderecoPessoa)){
			validateEnderecoPessoa(idPessoa);
		}
	}

	/**
	 * Se o endereco pretence a um dos tipo de pessoa informado, lan?ar uma exception.
	 * 
	 * @author Micka?l Jalbert
	 * @since 23/08/2006
	 *
	 * @param Long idPessoa
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validateEnderecoPessoa(Long idPessoa) {
		List lstCdEspecializacao = new ArrayList();

		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_EMPRESA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_AEROPORTO);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL_CIA_AEREA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CON_POSTO_PASSAGEM);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_PROPRIETARIO);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_MOTORISTA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_OPERADORA_MCT);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_EMPRESA_COBRANCA);
		lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CLIENTE);

		Short cdEspecializacao = especializacaoPessoaService.isEspecializado(idPessoa, lstCdEspecializacao);

		if (cdEspecializacao != null){
			throw new BusinessException("LMS-27068", new Object[]{especializacaoPessoaService.getLabel(cdEspecializacao)});
		}
	}	
	
	protected EnderecoPessoa beforeStore(EnderecoPessoa bean) {
		return this.beforeStore(bean, true);
	}
	
	protected EnderecoPessoa beforeStore(EnderecoPessoa bean, boolean validatePessoa) {
		EnderecoPessoa ep = (EnderecoPessoa) bean;

		ep.setBlEnderecoMigrado(Boolean.FALSE);

		/** Caso a data de vigencia final n?o seja nula, realiza algumas valida??es */
		if (ep.getDtVigenciaFinal() != null) 
			validateVigenciaFinal(ep);

		//Se a pessoa ? um cliente, n?o pode mudar os dados se a filial da sess?o ? differente da filial comercial do cliente
		//N?o valida altera??o, pois ? um endere?o novo do tipo "COL" a ser cadastrado pela tela de Pedido Coleta
		List listTiposEnderecoPessoa = ep.getTipoEnderecoPessoas();
		if (ep.getIdEnderecoPessoa() == null
				&& listTiposEnderecoPessoa != null 
				&& listTiposEnderecoPessoa.size() == 1
				&& ((TipoEnderecoPessoa)listTiposEnderecoPessoa.get(0)).getTpEndereco().getValue().equals("COL")){
		} else {
			
			if (validatePessoa){
			//Valida se o usuario logado pode alterar a pessoa
			pessoaService.validateAlteracaoPessoa(((EnderecoPessoa)bean).getPessoa().getIdPessoa());
		}
		}

		Pais pais = paisService.findPaisByIdMunicipio(ep.getMunicipio().getIdMunicipio());

		String nrCep = ep.getNrCep();
		/* - Parte da regra 3.1
		 * Se o campo CEP N?O foi informado e o pa?s informado exige a informa??o do CEP
		 * (PAIS.BL_CEP_OPCIONAL = 'N'), exibir a mensagem de erro LMS-27094.*/
		if (StringUtils.isBlank(nrCep) && !pais.getBlCepOpcional()) {
				throw new BusinessException("LMS-27094");
		}
		if(!pais.getBlCepAlfanumerico()) {
			if(!StringUtils.isNumeric(nrCep)) {
				throw new BusinessException("LMS-29121");
			}
		}

		return super.beforeStore(bean);
	}

	protected EnderecoPessoa beforeUpdate(EnderecoPessoa bean) {
		EnderecoPessoa ep = (EnderecoPessoa) bean;
		EnderecoPessoa epTmp = findById(ep.getIdEnderecoPessoa());

		/** Caso a data inicial que est? no BD seja maior ou igual a data atual */ 
		if(validateVigenciaInicial(epTmp)) {
			/** Caso a nova data inicial seja menor que a data atual, lan?a uma exception */ 
			if(ep.getDtVigenciaInicial().compareTo(JTDateTimeUtils.getDataAtual()) < 0)
				throw new BusinessException("LMS-27055");
		}
		
		// Caso seja comercial ou residencial.
		if (tipoEnderecoPessoaService.isComercialOrResidencial(ep.getIdEnderecoPessoa())) {
			// Caso a dtVigenciaInicial tenha sido modificada.
			if (JTDateTimeUtils.comparaData(ep.getDtVigenciaInicial(),epTmp.getDtVigenciaInicial()) != 0) {
				
				// Busca o endere?o anterior ao endere?o em quest?o.
				EnderecoPessoa enderecoAnterior = findEnderecoPessoaSubstituidoByDtVigencia(
							epTmp.getPessoa().getIdPessoa(), 
							epTmp.getDtVigenciaInicial(), 
							epTmp.getPessoa().getTpPessoa().getValue());
				
				if (enderecoAnterior != null) {
					enderecoAnterior.setDtVigenciaFinal(ep.getDtVigenciaInicial().minusDays(1));
					this.storeBasic(enderecoAnterior);
				}
			}
		}
		
		getEnderecoPessoaDAO().getAdsmHibernateTemplate().evict(epTmp);
		return super.beforeUpdate(bean);
	}

	/**
	 * Realiza valida??es na data de vigencia final do endereco da pessoa
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/12/2006
	 *
	 * @param ep
	 * @throws BusinessException
	 *
	 */
	private void validateVigenciaFinal(EnderecoPessoa ep){
		/** Se a data de vigencia final ? menor que a data atual menos um dia lan?a uma exception */
		if(ep.getDtVigenciaFinal().isBefore(JTDateTimeUtils.getDataAtual().minusDays(1))){
			throw new BusinessException("LMS-27054", new Object[]{1});
		} else {
			/** valida se o endereco est? vinculado a alguma especializa??o de pessoa */
			if (isLastEnderecoPessoa(ep.getPessoa().getIdPessoa(), ep.getIdEnderecoPessoa()))
				validateEnderecoPessoa(ep.getPessoa().getIdPessoa());
		}
	}

	protected EnderecoPessoa beforeInsert(EnderecoPessoa bean) {
		EnderecoPessoa ep = (EnderecoPessoa) bean;

		/** Valida se a vigenciaInicial ? menor que a data atual, caso seja, lan?a a exception */
		if (!validateVigenciaInicial(ep)){
			throw new BusinessException("LMS-27055");
		}

		return super.beforeInsert(bean);
	}

	/**
	 * Valida Data da Vig?ncia Inicial.<BR>
	 * Data deve ser maior ou igual ao dia de hoje.<BR> 
	 * @param contaBancaria
	 * @return
	 */
	private boolean validateVigenciaInicial(EnderecoPessoa ep){
		if (ep.getDtVigenciaInicial() == null) return false;
		return (JTDateTimeUtils.comparaData(ep.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) >= 0) ;
	}

	/**
	 * @see store(EnderecoPessoa, Long)
	 */
	public java.io.Serializable store(EnderecoPessoa enderecoPessoa) {
		return store(enderecoPessoa, null);
	}
	
	// LMSA-6786
	public java.io.Serializable storeComUsuario(EnderecoPessoa enderecoPessoa, Usuario usuarioIntegracao) {
		return store(enderecoPessoa, null, true, usuarioIntegracao);
	}
	
	/**
	 * Salva o enderecoPessoa informado usando as regras de neg?cio dos m?todos padr?o.
	 * 
	 * @author Micka?l Jalbert
	 * @since 09/03/2007
	 * @param enderecoPessoa
	 * @param idEnderecoPessoaSubstituido
	 * @return
	 */
	public java.io.Serializable store(EnderecoPessoa enderecoPessoa, Long idEnderecoPessoaSubstituido, boolean validatePessoa) {
		return store(enderecoPessoa, idEnderecoPessoaSubstituido, validatePessoa, null);
	}
	public java.io.Serializable store(EnderecoPessoa enderecoPessoa, Long idEnderecoPessoaSubstituido, boolean validatePessoa, Usuario usuarioAtualizacao) {
		boolean isUpdate = enderecoPessoa.getIdEnderecoPessoa() != null;

		//Se ? uma substitui??o, fechar a vigencia
		if (idEnderecoPessoaSubstituido != null){
			EnderecoPessoa enderecoPessoaSubstituido = findById(idEnderecoPessoaSubstituido);
			enderecoPessoaSubstituido.setDtVigenciaFinal(enderecoPessoa.getDtVigenciaInicial().minusDays(1));
			storeBasic(enderecoPessoaSubstituido);
			enderecoPessoa.setDtVigenciaFinal(null);
		}

		if (isUpdate){
			enderecoPessoa.setUsuarioAlteracao(usuarioAtualizacao == null ? SessionUtils.getUsuarioLogado() : usuarioAtualizacao);
		}else{
			enderecoPessoa.setUsuarioInclusao(usuarioAtualizacao == null ? SessionUtils.getUsuarioLogado() : usuarioAtualizacao);
		}

		beforeStore(enderecoPessoa,validatePessoa);
		this.getEnderecoPessoaDAO().store(enderecoPessoa);
		afterStore(enderecoPessoa, isUpdate);

		// retorna o Id do POJO salvo ou atualizado.
		return this.getEnderecoPessoaDAO().getIdentifier(enderecoPessoa);
	}

	public java.io.Serializable store(EnderecoPessoa enderecoPessoa, Long idEnderecoPessoaSubstituido){
		return this.store( enderecoPessoa, idEnderecoPessoaSubstituido,true);
	}

	/**
	 * M?todo invocado ap?s o store
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/09/2006
	 *
	 * @param enderecoPessoa
	 *
	 */
	protected void afterStore(EnderecoPessoa enderecoPessoa, boolean isUpdate) {
		Pessoa pessoa = pessoaService.findById(enderecoPessoa.getPessoa().getIdPessoa());
		Integer countEndPesVig = null; 
		boolean geraMensagem = false;

		/** Caso o tpPessoa seja null, seta o mesmo para pessoa jur?dica (J). */
		if(pessoa.getTpPessoa() == null)
			pessoa.setTpPessoa(new DomainValue("J"));
		
		/** Caso o tpPessoa seja F, busca enderecoPessoa vigentes que o tpEndereco seja RES */
		if(pessoa.getTpPessoa().getValue().equals("F"))
			countEndPesVig = getEnderecoPessoaDAO().countEnderecoPessoaVigenteByTpEndereco(enderecoPessoa.getPessoa().getIdPessoa(), "RES", enderecoPessoa.getDtVigenciaInicial(), enderecoPessoa.getDtVigenciaFinal());
		/** Caso o tpPessoa seja J, busca enderecoPessoa vigentes que o tpEndereco seja COM */
		else
			countEndPesVig = getEnderecoPessoaDAO().countEnderecoPessoaVigenteByTpEndereco(enderecoPessoa.getPessoa().getIdPessoa(), "COM", enderecoPessoa.getDtVigenciaInicial(), enderecoPessoa.getDtVigenciaFinal());

		if(isUpdate) {
			//TipoEnderecoPessoa tipoEnderecoPessoa = tipoEnderecoPessoaService.findTipoEnderecoPessoaByEnderecoPessoa(enderecoPessoa.getIdEnderecoPessoa());
			geraMensagem = tipoEnderecoPessoaService.isComercialOrResidencial(enderecoPessoa.getIdEnderecoPessoa());
		}

		//Verifica se existe um endere?o do tipo COL n?o salvo;
		TipoEnderecoPessoa tipoEnderecoColeta = null;
		List listTiposEnderecoPessoa = new ArrayList();
		if(enderecoPessoa.getTipoEnderecoPessoas() != null)
		{
			listTiposEnderecoPessoa = enderecoPessoa.getTipoEnderecoPessoas();
		}

		for (Object object : listTiposEnderecoPessoa) {
			TipoEnderecoPessoa tep = (TipoEnderecoPessoa)object;
			if (tep.getTpEndereco().getValue().equalsIgnoreCase("COL")
					&& tep.getIdTipoEnderecoPessoa() == null){
				tep.setEnderecoPessoa(enderecoPessoa);
				tipoEnderecoPessoaService.store(tep);
				tipoEnderecoColeta = tep;
				break;
			}
		}

		// Caso countEndPesVig == 0, insere um tipo de endereco para a pessoa de acordo com o tpPessoa da pessoa,
		// pois a pessoa deve ter um endere?o COM ou RES vigente
		if(countEndPesVig.equals(Integer.valueOf(0)) && tipoEnderecoColeta == null)
			this.insertTipoEnderecoPessoa(enderecoPessoa, pessoa.getTpPessoa().getValue());	
		// Caso countEndPesVig > 1, lan?a a exce??o, pois existe mais de um tipo de endereco (comercial ou residencial) 
		// para esta pessoa 
		else if(countEndPesVig.compareTo(Integer.valueOf(1)) > 0 && isUpdate && geraMensagem)
			throw new BusinessException("LMS-27045");	

		/** Realiza o store do endere?o padr?o */
		pessoaService.storeEnderecoPessoaAtual(pessoa, enderecoPessoa);
		getDao().getAdsmHibernateTemplate().flush();
		clienteService.executeChangeCliente(pessoa.getIdPessoa(), enderecoPessoa.getUsuarioAlteracao());
		
		// Atualiza UF da Inscrição estadual
		this.atualizaUfInscricaoEstadual(enderecoPessoa, pessoa);
	}


	/**
	 * Na tela "Manter endere?os da pessoa" e na rotina "Atualiza??o autom?tica do endere?o padr?o", caso o endere?o que esteja 
	 * sendo atualizado (inserido OU alterado) seja o endere?o padr?o da pessoa (PESSOA.ID_ENDERECO_PESSOA) e a pessoa 
	 * seja uma "Pessoa f?sica" e a inscri??o estadual padr?o seja "ISENTO" e esteja "Ativa", caso a UF do endere?o informado seja
	 * diferente da UF da Inscri??o Estadual "ISENTO", atualizar a UF da Inscri??o Estadual com a UF do endere?o informado.
	 * 
	 * @author Aleksander Kostylew
	 * @since 10/02/2010
	 * 
	 * @param EnderecoPessoa enderecoPessoa (Endere?o novo)
	 * @param Pessoa pessoa
	 * @return void
	 */		
	public void atualizaUfInscricaoEstadual(EnderecoPessoa enderecoPessoa, Pessoa pessoa) {
		// Se idEnderecoPessoa (Endere?o padr?o da pessoa) ? o que est? sendo substituido
		if (enderecoPessoa != null
			&& enderecoPessoa.getIdEnderecoPessoa() != null				
			&& pessoa.getEnderecoPessoa() != null
			&& pessoa.getEnderecoPessoa().getIdEnderecoPessoa().longValue() == enderecoPessoa.getIdEnderecoPessoa().longValue()) {			
			InscricaoEstadual inscrestadualTmp = null;
			
			if (pessoa.getTpPessoa() != null
				&& pessoa.getTpPessoa().getValue().equals("F")) { // pessoa F?sica
				List <InscricaoEstadual> inscrEstadualList = (List<InscricaoEstadual>)pessoa.getInscricaoEstaduais();
				
				if (inscrEstadualList != null) {
				for (InscricaoEstadual inscEst : inscrEstadualList) {
					if (inscEst.getNrInscricaoEstadual().equals("ISENTO") // Isento
					    && inscEst.getTpSituacao().getValue().equals("A")  // Ativo
					    && inscEst.getBlIndicadorPadrao() == true) {  // Padr?o
						inscrestadualTmp = inscEst;
						break;
					}
				}
				
				if (inscrestadualTmp != null) {
					Municipio mun = municipioService.findById(enderecoPessoa.getMunicipio().getIdMunicipio());					
					UnidadeFederativa unidFedPessoa = mun.getUnidadeFederativa();
						
					// Se Unidade Federativa da Inscri??o estadual for diferente da Unidade Feredativa
					// do endere?o padrao da pessoa					
					if (inscrestadualTmp.getUnidadeFederativa().getIdUnidadeFederativa() !=
								unidFedPessoa.getIdUnidadeFederativa()) {						
						// Inscri??o estadual da Unidade Federativa recebe Unidade Feredativa do endere?o padrao da pessoa
						inscrestadualTmp.setUnidadeFederativa(unidFedPessoa);
					}
				}
			}			
		}		
	}
	}
	
	
	/**
	 * Informa se ? o ?ltimo contato da pessoa.
	 * 
	 * @author Micka?l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param Long idPessoa
	 * @param Long idEnderecoPessoa
	 * @return boolean
	 */
	public boolean isLastEnderecoPessoa(Long idPessoa, Long idEnderecoPessoa){
		List lstEndereco = findTodosByIdPessoa(idPessoa, JTDateTimeUtils.getDataAtual(), idEnderecoPessoa);
		
		if (lstEndereco.size() < 1){
			return true;
		}
		
		return false;
	}	

	public EnderecoPessoa getUltimoEndereco(Long idPessoa) {
		return getEnderecoPessoaDAO().getUltimoEndereco(idPessoa);
	}

	public EnderecoPessoa getPrimeiroEndereco(Long idAeroporto) {
		return getEnderecoPessoaDAO().getPrimeiroEndereco(idAeroporto);	
	}

	/**
	 * Retorna o endere?o de cobran?a da pessoa.
	 * Caso n?o encontre retorna o enrede?o padr?o
	 * 
	 * @param Long idPessoa
	 * @param Date dataVigencia
	 * @return EnderecoPessoa Endere?o da pessoa
	 * */	
	public EnderecoPessoa findEnderecoPessoaCobranca(Long idPessoa, YearMonthDay dataVigencia) {
		EnderecoPessoa enderecoPessoa = null;
		
		enderecoPessoa = this.getEnderecoPessoaDAO().findEnderecoPessoaSpecific(idPessoa, "COB", dataVigencia);			
		
		if (enderecoPessoa == null)
			return findEnderecoPessoaPadrao(idPessoa);
		else		
			return enderecoPessoa;			
	}	

	/**
	 * Retorna o endere?o padr?o da pessoa da informada.
	 * 
	 * @author Micka?l Jalbert
	 * @since 08/08/2006
	 * 
	 * @param Lond idPessoa
	 * @return EnderecoPessoa Endere?o da pessoa
	 *
	 */
	public EnderecoPessoa findEnderecoPessoaPadrao(Long idPessoa) {
		if(idPessoa == null) {
			throw new IllegalArgumentException("Parâmetro idPessoa não pode ser nulo.");
		}
		return getEnderecoPessoaDAO().findEnderecoPessoaPadrao(idPessoa);	
	}
	
	/**
	 * Busca o endere?o comercial ou residencial que foi substituido, ou seja, 
	 * com a maior data de vig?ncia final menor do que a data passada por par?metro, 
	 * usado na exclus?o
	 * @param idPessoa
	 * @param dtVigencia
	 * @param tpPessoa
	 * @return
	 */
	private EnderecoPessoa findEnderecoPessoaSubstituidoByDtVigencia(Long idPessoa, YearMonthDay dtVigencia, String tpPessoa) {
		//Caso o tpPessoa seja F, busca enderecoPessoa que o tpEndereco seja RES
		//Caso o tpPessoa seja J, busca enderecoPessoa que o tpEndereco seja COM
		String tpEndereco = "F".equals(tpPessoa) ? "RES" : "COM";
		return getEnderecoPessoaDAO().findEnderecoPessoaSubstituidoByDtVigencia(idPessoa, tpEndereco, dtVigencia);
	}
	
	/**
	 * Busca o enderecoPessoa substituido.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/08/2007
	 *
	 * @param idPessoa
	 * @param dtVigenciaInicial
	 * @return
	 *
	 */
	public EnderecoPessoa findEnderecoSubstituido(Long idPessoa, YearMonthDay dtVigenciaInicial){
		return getEnderecoPessoaDAO().findEnderecoSubstituido(idPessoa, dtVigenciaInicial);
	}

	/**
	 * Retorno o endere?o padr?o da pessoa da informada com a data informada.
	 * 
	 * Quando o tipo de pessoa ? jur?dico ele retorna um endere?o de tipo 'comercial'
	 * se n?o achar retornar o primeiro encontrado (duh).
	 * 
	 * Quando o tipo de pessoa ? f?sico ele retorna um endere?o de tipo 'residencial'
	 * se n?o achar retornar o primeiro encontrado (bis).
	 * 
	 * @param Lond idPessoa
	 * @param Date dataVigencia
	 * @return EnderecoPessoa Endere?o da pessoa
	 */	
	public EnderecoPessoa findEnderecoPessoaPadrao(Long idPessoa, YearMonthDay dataVigencia) {
		EnderecoPessoa enderecoPessoa = null;
		Pessoa pessoa = pessoaService.findById(idPessoa);

		if (pessoa == null) {
			return null;
		}

		if (pessoa.getTpPessoa() != null ) {
			if (pessoa.getTpPessoa().getValue().equals("J"))
				enderecoPessoa = this.getEnderecoPessoaDAO().findEnderecoPessoaSpecific(idPessoa, "COM", dataVigencia);
			else if (pessoa.getTpPessoa().getValue().equals("F")) 
				enderecoPessoa = this.getEnderecoPessoaDAO().findEnderecoPessoaSpecific(idPessoa, "RES", dataVigencia);			
		} 
		
		if (enderecoPessoa==null) {
			enderecoPessoa = this.getEnderecoPessoaDAO().findEnderecoPessoaSpecific(idPessoa, null, dataVigencia);			
		}
		return enderecoPessoa;	
	}
	
	/**
	 * Retorna o EnderecoPessoa passando o ID da Pessoa
	 */	
	public List findEnderecoPessoaByIdPessoaByTipoEnderecoPessoa(Long idPessoa, String tipoEnderecoPessoa, YearMonthDay yearMonthDay) {
		DetachedCriteria dc = DetachedCriteria.forClass(EnderecoPessoa.class);
		dc.add( Restrictions.eq("pessoa.idPessoa", idPessoa));
		if(tipoEnderecoPessoa != null) {
			dc.createCriteria("tipoEnderecoPessoas")
				.add(Restrictions.eq("tpEndereco", tipoEnderecoPessoa));
		}

		if(yearMonthDay != null) {
			dc.add( Restrictions.le("dtVigenciaInicial", yearMonthDay));
			dc.add( Restrictions.ge("dtVigenciaFinal", yearMonthDay));
		}

		dc.setFetchMode("municipio", FetchMode.JOIN);
		dc.setFetchMode("municipio.unidadeFederativa", FetchMode.JOIN);
		dc.setFetchMode("tipoLogradouro", FetchMode.JOIN);

		return getEnderecoPessoaDAO().findByDetachedCriteria(dc);		
	}	
	
	/**
	 * Retorno o TipoEnderecoPessoa passando o ID do EnderecoPessoa
	 */	
	@SuppressWarnings("rawtypes")
	public List findTipoEnderecoPessoaByIdEnderecoPessoa(Long idEnderecoPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(TipoEnderecoPessoa.class);
		dc.add( Restrictions.eq("enderecoPessoa.idEnderecoPessoa", idEnderecoPessoa));
		return getEnderecoPessoaDAO().findByDetachedCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public TipoEnderecoPessoa findTipoEnderecoPessoaByIdEnderecoPessoaAndTipo(Long idEnderecoPessoa, String tpEndereco) {
		DetachedCriteria dc = DetachedCriteria.forClass(TipoEnderecoPessoa.class);
		dc.add( Restrictions.eq("enderecoPessoa.idEnderecoPessoa", idEnderecoPessoa));
		dc.add( Restrictions.eq("tpEndereco", tpEndereco));
		List<TipoEnderecoPessoa> list = getEnderecoPessoaDAO().findByDetachedCriteria(dc);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	public void storeBasic(EnderecoPessoa ep) {
		getEnderecoPessoaDAO().store(ep);		
	}

	public java.io.Serializable storeBasico(EnderecoPessoa ep) {
		return super.store(ep);		
	}
	
	/**
	 * M?todo que salva o EnderecoPessoa e seu respectivo TipoEnderecoPessoa e TelefoneEndereco;
	 * 
	 * @param bean
	 * @param tipoEndereco
	 * @param telefoneEndereco
	 * @return
	 */
	public java.io.Serializable storeCompleto(EnderecoPessoa bean, TipoEnderecoPessoa tipoEndereco, TelefoneEndereco telefoneEndereco) {
		Serializable masterId = null;
		
		// Salva EnderecoPessoa
		masterId = this.store(bean);
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		enderecoPessoa.setIdEnderecoPessoa(Long.valueOf(masterId.toString()));
		enderecoPessoa.setPessoa(bean.getPessoa());
		
		// Testa se o TipoEnderecoPessoa tem um EnderecoPessaoa relacionado
		if(tipoEndereco.getEnderecoPessoa() == null) {
			tipoEndereco.setEnderecoPessoa(enderecoPessoa);
		}
		
		// Salva TipoEnderecoPessoa para o EnderecoPessoa em quest?o
		this.tipoEnderecoPessoaService.store(tipoEndereco);

		// Testa se o TelefoneEndereco tem um EnderecoPessaoa relacionado
		if(telefoneEndereco.getEnderecoPessoa() == null) {
			telefoneEndereco.setEnderecoPessoa(enderecoPessoa);
		}
		
		// Salva TelefoneEndereco para o EnderecoPessoa em quest?o
		this.telefoneEnderecoService.store(telefoneEndereco);
				
		return masterId;		
	}

	public Map findByIdPessoaTpEndereco(Long idPessoa, String tpEndereco) {
		return getEnderecoPessoaDAO().findEnderecoPessoaByPessoaTpEndereco(idPessoa, tpEndereco);
	}

	public List findByIdPessoaTpEnderecoLocalEntrega(List idPessoa, String tpEndereco) {
		return getEnderecoPessoaDAO().findEnderecoPessoaByPessoaTpEnderecoLocalEntrega(idPessoa, tpEndereco);
	}

	public Map findByIdPessoaPrioridade(Long idPessoa, String[] prioridade) {
		return getEnderecoPessoaDAO().findEnderecoPessoaByPessoaPrioridade(idPessoa, prioridade);
	}

	public Map findByPessoaTipoEndereco(Long idPessoa, String tpEndereco){
		return getEnderecoPessoaDAO().findByPessoaTipoEndereco(idPessoa, tpEndereco);
	}

	public Map findByPessoaTipoEndereco(Long idPessoa, String tpEndereco,boolean siglaDescricaoUF) {
		Map map = getEnderecoPessoaDAO().findByPessoaTipoEndereco(idPessoa, tpEndereco);
		String sigla = (String)map.remove("endereco_sgUnidadeFederativa");
		if(sigla != null){
			if(!siglaDescricaoUF){
				map.put("endereco_siglaDescricaoUf", sigla);
			}
			else{
				map.put("endereco_siglaDescricaoUf", sigla + "-" + map.remove("endereco_nmUnidadeFederativa"));
			}
		}
		VarcharI18n tipoLogradouro = (VarcharI18n)map.remove("endereco_dsTipoLogradouro");
		if(tipoLogradouro != null)
			map.put("endereco_dsEndereco", tipoLogradouro.getValue() + " " + map.get("endereco_dsEndereco"));
		return AliasToNestedMapResultTransformer.getInstance().transformeTupleMap(map);
	}
    
    public Long findIdMunicipioByIdPessoa(Long idPessoa) {
        return getEnderecoPessoaDAO().findIdMunicipioByIdPessoa(idPessoa);
    }

	/**
	 * Utilizar EnderecoPessoaService.findEnderecoPessoaPadrao
	 * 
	 * @param idPessoa
	 * @return
	 */
	@Deprecated public Map findMunicipioUfByIdPessoa(Long idPessoa) {
		return getEnderecoPessoaDAO().findMunicipioUfByIdPessoa(idPessoa);
	}

	public List findEnderecosVigentesByIdPessoa(Long idPessoa){
		return getEnderecoPessoaDAO().findEnderecosVigentesByIdPessoa(idPessoa);
	}
	
	public Map findMunicipioByIdPessoaTpEndereco(Long idPessoa, String tpEndereco) {
		return getEnderecoPessoaDAO().findMunicipioByIdPessoaTpEndereco(idPessoa, tpEndereco);
	}
	
	public java.io.Serializable getRowCountByPessoaUfFronteiraRapida(Long idPessoa, Boolean blFronteiraRapida) {
		return getEnderecoPessoaDAO().getRowCountByPessoaUfFronteiraRapida(idPessoa, blFronteiraRapida);
	}

	public Map findEnderecoByIdPessoaTpPessoa(String tpPessoa, Long idPessoa) {
		String tpEndereco = "COM";
		if("F".equalsIgnoreCase(tpPessoa))
			tpEndereco = "RES";
		return findByPessoaTipoEndereco(idPessoa, tpEndereco);
	}

	/**
	 * M?todo responsavel por buscar o EnderecoPessoa de uma pessoa
	 * 
	 * @param idPessoa
	 * @return EnderecoPessoa
	 */
	public Map findEnderecoCobrancaByPessoa(Long idPessoa){
		return getEnderecoPessoaDAO().findEnderecoCobrancaByPessoa(idPessoa);
	}
	
	public boolean findEnderecoByFilialCiaArea(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
		return getEnderecoPessoaDAO().findEnderecoByFilialCiaArea(dataInicio, dataFim, idFilialCiaAerea);
	}

	/**
	 * Busca o EnderecoPessoa pelo id, fazendo fetch em TipoEnderecoPessoas
	 * @param idEnderecoPessoa
	 * @return
	 */
	public EnderecoPessoa findByIdFetchTipoEnderecoPessoas(Long idEnderecoPessoa){
		return getEnderecoPessoaDAO().findByIdFetchTipoEnderecoPessoas(idEnderecoPessoa);
	}

	/**
	 * Retorna o endere?o padr?o (pessoa.enderecoPessoa) da pessoa informada.
	 * 
	 * @author Giuliano
	 * @since 08/08/2006
	 * 
	 * @param Long idPessoa
	 * @return EnderecoPessoa
	 */
	public EnderecoPessoa findByIdPessoa(Long idPessoa){
		return getEnderecoPessoaDAO().findByIdPessoa(idPessoa);
	}
	
	/**
	 * Retorna todos os endere?os da pessoa informada
	 * 
	 * @author Micka?l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param Long
	 *            idPessoa
	 * @return List
	 */	
	public List findTodosByIdPessoa(Long idPessoa, YearMonthDay dtVigencia, Long idEnderecoPessoa){
		return getEnderecoPessoaDAO().findTodosByIdPessoa(idPessoa, dtVigencia, idEnderecoPessoa);
	}	
	public List findEnderecoPessoaByIdPessoa(Long idPessoa) {
		return getEnderecoPessoaDAO().findEnderecoPessoaByIdPessoa(idPessoa);
	}

	public Map<String, Object> loadCoordenadasTemporariaByIdPessoa(Long idPessoa) {
		EnderecoPessoa enderecoPessoa = findUltimoEnderecoValidoByIdPessoa(idPessoa);
		
		if (enderecoPessoa == null) {
			return setarCoordenadas(null);
		}
		
		Map<String, Object> mapCoordenadas = setarCoordenadas(enderecoPessoa);
		
		return mapCoordenadas;
	}

	private Map<String, Object> setarCoordenadas(EnderecoPessoa enderecoPessoa) {
		BigDecimal nrLatitudeTmp = (enderecoPessoa == null ? BigDecimal.ZERO : enderecoPessoa.getNrLatitudeTmp());
		BigDecimal nrLongitudeTmp = (enderecoPessoa == null ? BigDecimal.ZERO : enderecoPessoa.getNrLongitudeTmp());

		Map<String, Object> mapCoordenadas = new HashMap<String, Object>();
		mapCoordenadas.put("nrLatitudeTmp", nrLatitudeTmp);
		mapCoordenadas.put("nrLongitudeTmp", nrLongitudeTmp);
		mapCoordenadas.put("qualidade", (enderecoPessoa == null ? null : getNrQualidade(nrLatitudeTmp, nrLongitudeTmp)));
		return mapCoordenadas;
	}

	private Long getNrQualidade(BigDecimal nrLatitudeTmp, BigDecimal nrLongitudeTmp) {
		if (BigDecimalUtils.hasValue(nrLatitudeTmp) && BigDecimalUtils.hasValue(nrLongitudeTmp)) {
			return LongUtils.ONE;
		}
		
		return null;
	}

	private EnderecoPessoa findUltimoEnderecoValidoByIdPessoa(Long idPessoa) {
		return getEnderecoPessoaDAO().findUltimoEnderecoValidoByIdPessoa(idPessoa);
	}

	public void setEnderecoPessoaDAO(EnderecoPessoaDAO dao) {
		setDao( dao );
	}
	private EnderecoPessoaDAO getEnderecoPessoaDAO() {
		return (EnderecoPessoaDAO) getDao();
	}
	public void setTipoEnderecoPessoaService(TipoEnderecoPessoaService tipoEnderecoPessoaService) {
		this.tipoEnderecoPessoaService = tipoEnderecoPessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setEspecializacaoPessoaService(EspecializacaoPessoaService especializacaoPessoaService) {
		this.especializacaoPessoaService = especializacaoPessoaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setMunicipio(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	
	public ExecuteWorkflowProprietarioService getExecuteWorkflowProprietarioService() {
		return executeWorkflowProprietarioService;
	}

	public void setExecuteWorkflowProprietarioService(
			ExecuteWorkflowProprietarioService executeWorkflowProprietarioService) {
		this.executeWorkflowProprietarioService = executeWorkflowProprietarioService;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		validateFiltrosFindPaginated(criteria);
		return super.findPaginated(criteria);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getRowCount(Map criteria) {
		validateFiltrosFindPaginated(criteria);
		return super.getRowCount(criteria);
	}

	@SuppressWarnings("rawtypes")
	private void validateFiltrosFindPaginated(Map criteria) {
		if (criteria != null) {
			TypedFlatMap flatMap = new TypedFlatMap();
			ReflectionUtils.flatMap(flatMap, criteria);
			Validate.notEmpty(flatMap.getString("pessoa.idPessoa"), "Ocorreu um erro inesperado: 'pessoa.idPessoa' não pode ser nulo!");
		}
	}

	public boolean hasEnderecoPessoaPadrao(Long idPessoa) {
		return getEnderecoPessoaDAO().hasEnderecoPessoaPadrao(idPessoa);
	}
	
	public ResultSetPage findPaginatedEnderecoPessoa(Map criteria) {		
		return super.findPaginated(criteria);
	}

	public Integer getRowCountEnderecoPessoa(Map criteria) {		
		return super.getRowCount(criteria);
	}
		
	/**
	 * Retorna o endere?o comercial/residencial vigente para uma pessoa.
	 * 
	 * @param idPessoa
	 * @param dtVigenciaInicial
	 * 
	 * @return EnderecoPessoa
	 */
	public EnderecoPessoa findEnderecoPessoaSubstituir(Long idPessoa, YearMonthDay dtVigenciaInicial){
		return getEnderecoPessoaDAO().findEnderecoPessoaSubstituir(idPessoa, dtVigenciaInicial);
	}
	
	/**
	 * Salva o enderecoPessoa informado usando as regras de neg?cio dos m?todos padr?o.
	 * 
	 * @param enderecoPessoa
	 * @param idEnderecoPessoaSubstituido
	 * @param tipoEndereco
	 * 
	 * @return java.io.Serializable
	 */
	public java.io.Serializable storeEnderecoPessoa(EnderecoPessoa enderecoPessoa,
			Long idEnderecoPessoaSubstituido,
			Map<String, Object> tipoEndereco) {
		Long idEnderecoPessoa = (Long) this.store(enderecoPessoa, idEnderecoPessoaSubstituido, true);
		
		/*
		 * Executa a gera??o do workflow.
		 */
		getExecuteWorkflowProprietarioService().executeWorkflowPendencia(enderecoPessoa.getPessoa(), ExecuteWorkflowProprietarioService.ENDERECO, false);
		
		getEnderecoPessoaDAO().getHibernateTemplate().flush();
		getEnderecoPessoaDAO().getHibernateTemplate().refresh(enderecoPessoa);
		
		/* 
		 * Depois de atualizar a pessoa, realiza a altera??o do tipo de endere?o.
		 */
		if(idEnderecoPessoaSubstituido == null){
			updateTipoEnderecoPessoa(enderecoPessoa, tipoEndereco);
		}
		
		return idEnderecoPessoa;
	}
	
	private void updateTipoEnderecoPessoa(EnderecoPessoa enderecoPessoa, Map<String, Object> listTipoEndereco){
		List<Map<String, Object>> added = (List<Map<String, Object>>) listTipoEndereco.get("added");
		List<Map<String, Object>> removed = (List<Map<String, Object>>) listTipoEndereco.get("removed");
				
		if(!added.isEmpty()){			
			List<TipoEnderecoPessoa> toAdd = new ArrayList<TipoEnderecoPessoa>();
			for (Map<String, Object> tipoEndereco : added) {
				TipoEnderecoPessoa tipoEnderecoPessoa = new TipoEnderecoPessoa();
				tipoEnderecoPessoa.setEnderecoPessoa(enderecoPessoa);
				tipoEnderecoPessoa.setTpEndereco(new DomainValue(MapUtils.getString(MapUtils.getMap(tipoEndereco, "tpEndereco"), "value")));
				
				toAdd.add(tipoEnderecoPessoa);
			}
			
			tipoEnderecoPessoaService.storeAllTipoEnderecoPessoa(toAdd);
		}
		
		if(!removed.isEmpty()){
			List<Long> toRemove = new ArrayList<Long>();
			
			for (Map<String, Object> tipoEndereco : removed) {
				toRemove.add(MapUtils.getLong(MapUtils.getMap(tipoEndereco, "tpEndereco"), "enderecoPessoaId"));
			}
			
			tipoEnderecoPessoaService.removeByIds(toRemove);
		}
	}

	public List<Map<String, Object>> findEnderecosPessoa(Long idPessoa) {
		return getEnderecoPessoaDAO().findEnderecosPessoa(idPessoa);
	}
	
	public Long findEnderecoPessoaCobranca(Long idPessoa) {
		return getEnderecoPessoaDAO().findEnderecoPessoaCobranca(idPessoa);
	}
	
	public EnderecoPessoa findEnderecoPeriodo(Long idPessoa, YearMonthDay dtVigenciaInicial){
		return getEnderecoPessoaDAO().findEnderecoPeriodo(idPessoa, dtVigenciaInicial);
	}

	public String getEnderecoCompleto(Long idPessoa) {
		return  getEnderecoPessoaDAO().getEnderecoCompleto(idPessoa);
	}

	public String getEnderecoCompletoPorEnderecoIdPessoa(Long idEnderecoPessoa) {
		return  getEnderecoPessoaDAO().getEnderecoCompletoByEnderecoIdPessoa(idEnderecoPessoa);
	}

}
