package com.mercurio.lms.contratacaoveiculos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTransporteRodoviarioDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.meioTransporteRodoviarioService"
 */
public class MeioTransporteRodoviarioService extends CrudService<MeioTransporteRodoviario, Long> {
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	private ConfiguracoesFacade configuracoesFacade;
	private ContatoService contatoService;

	/**
	 * Recupera uma instância de <code>MeioTransporteRodoviario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTransporteRodoviario findById(java.lang.Long id) {
		return (MeioTransporteRodoviario)super.findById(id);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		// flag que indica se a tela foi chamada pela lookup de 'Manter meios de transportes da Rota Expressa'
		boolean isLookup = false;
		if (criteria.get("isCalledByLookup") != null)
			isLookup = ((String)criteria.get("isCalledByLookup")).equals("true");

		Map oldMap = new HashMap();
		List newList = new ArrayList();
		ResultSetPage rsp = null;

		if (isLookup)
			rsp = getMeioTransporteRodoviarioDAO().findPaginatedLookup(criteria,FindDefinition.createFindDefinition(criteria));
		else
			rsp = getMeioTransporteRodoviarioDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));

		String liberado = configuracoesFacade.getMensagem("liberado");
		String bloqueado = configuracoesFacade.getMensagem("bloqueado");

		for(Iterator i = rsp.getList().iterator() ; i.hasNext() ; ) {
			TypedFlatMap row = new TypedFlatMap();

			oldMap = (Map)i.next();
			Set set = oldMap.keySet();
			for (Iterator i2 = set.iterator(); i2.hasNext();) {
				String key = ((String)i2.next());
				row.put(key.replace('_','.'),oldMap.get(key));
			}			

			MeioTransporte mt = new MeioTransporte();
			mt.setIdMeioTransporte((Long)oldMap.get("idMeioTransporte"));
			row.put("tpStatus",(bloqueioMotoristaPropService.validateBloqueiosVigentes(mt) ? bloqueado : liberado));

			if (isLookup) {
				Map infos = meioTranspProprietarioService.findInfoMeioTransporte((Long)oldMap.get("idMeioTransporte"));
				if (infos != null) {
					Set set2 = infos.keySet();
					for (Iterator i2 = set2.iterator(); i2.hasNext();) {
						String key = ((String) i2.next());
						row.put("outrasInfo." + key, infos.get(key));
					}
					row.put("outrasInfo.nrIdentificacaoFormatado",
							FormatUtils.formatIdentificacao(
									((DomainValue)infos.get("tpIdentificacaoPessoa")).getValue(), 
									(String)infos.get("nrIdentificacaoPessoa")));

					if (infos.get("dsEndereco") != null) {
						String dsEndereco = (String)infos.get("dsEndereco");
						if (infos.get("nrEndereco") != null) {
							dsEndereco = dsEndereco.concat(", " + infos.get("nrEndereco"));
						}
						if (infos.get("dsComplemento") != null) {
							dsEndereco = dsEndereco.concat(" - " + infos.get("dsComplemento"));
						}
						row.put("outrasInfo.dsEndereco",dsEndereco);
					}
					List contatos = contatoService.findContatosByIdPessoa((Long)infos.get("idProprietario"));
					if (!contatos.isEmpty()){
						row.put("outrasInfo.nmContato",((Contato)contatos.get(0)).getNmContato());
					}
				}
			}
			newList.add(row);
		}
		rsp.setList(newList);
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getMeioTransporteRodoviarioDAO().getRowCount(criteria);
	}

	public List findLookup(TypedFlatMap criteria) {
		List l = getMeioTransporteRodoviarioDAO().findLookupImpl(criteria);
		List retorno = new ArrayList(2);
		for (int i = 0 ; (i < l.size() && i < 1) ; i++) {
			Map oldMap = (Map)l.get(i);
			Map newMap = new TypedFlatMap();
			Set set = oldMap.keySet();
			for (Iterator i2 = set.iterator(); i2.hasNext();) {
				String key = ((String)i2.next());
				newMap.put(key.replace('_','.'),oldMap.get(key));
			}
			retorno.add(newMap);
		}
		return retorno;
	}

	public List findLookupRodoviario(TypedFlatMap criteria) {
		List l = getMeioTransporteRodoviarioDAO().findLookupImpl(criteria);
		List retorno = new ArrayList(2);
		for (int i = 0 ; i < l.size() && i < 1 ; i++) {
			Map oldMap = (Map)l.get(i);
			Map newMap = new TypedFlatMap();
			Set set = oldMap.keySet();
			for (Iterator i2 = set.iterator(); i2.hasNext();) {
				String key = (String)i2.next();
				newMap.put(key.replace('_','.'),oldMap.get(key));
			}
			retorno.add(newMap);
		}
		return retorno;
	}
	
	/**
	 * FindLookup que usa a mesma consulta do findPaginated de meioTransporteRodoviario.
	 * Retorna apenas idMeioTransporte,meioTransporte.nrFrota e meioTransporte.nrIdentificador.
	 * 
	 * @since 12-01-2006 
	 * @author Felipe Ferreira
	 * @param criteria
	 * @return
	 */
	public List findLookupWithProprietario(TypedFlatMap criteria) {
		List l = getMeioTransporteRodoviarioDAO().findLookupWithProprietario(criteria);
		if (l.size() == 1) {
			Map mt = new HashMap();
			mt.put("nrFrota",((Map)l.get(0)).get("meioTransporte_nrFrota"));
			mt.put("nrIdentificador",((Map)l.get(0)).get("meioTransporte_nrIdentificador"));
			Map base = new HashMap();
			base.put("meioTransporte",mt);
			base.put("idMeioTransporte",((Map)l.get(0)).get("meioTransporte_idMeioTransporte"));
			List l2 = new ArrayList(1);
			l2.add(base);
			return l2;
		}
		return l;
	}

	/**
	 * FindLookup que usa a mesma consulta do findPaginated de meioTransporteRodoviario.
	 * Retorna diversas informações do meio de transporte.
	 * 
	 * @since 21-03-2006 
	 * @author Felipe Ferreira
	 * @param criteria
	 * @return
	 */
	public List findLookupCompleteWithProprietario(TypedFlatMap criteria) {
		List l = getMeioTransporteRodoviarioDAO().findLookupWithProprietario(criteria);
		if (l.size() == 1) {
			Map mDados = ((Map)l.get(0));

			Map mt = new HashMap();
			mt.put("nrFrota",mDados.get("meioTransporte_nrFrota"));
			mt.put("nrIdentificador",mDados.get("meioTransporte_nrIdentificador"));

			Map base = new HashMap();
			Map modelo = new HashMap();
			Map marca = new HashMap();

			modelo.put("marcaMeioTransporte",marca);
			modelo.put("dsModeloMeioTransporte",mDados.get("meioTransporte_modeloMeioTransporte_dsModeloMeioTransporte"));
			marca.put("dsMarcaMeioTransporte",mDados.get("meioTransporte_modeloMeioTransporte_marcaMeioTransporte_dsMarcaMeioTransporte"));
			mt.put("modeloMeioTransporte",modelo);

			base.put("meioTransporte",mt);
			base.put("idMeioTransporte",((Map)l.get(0)).get("meioTransporte_idMeioTransporte"));

			List l2 = new ArrayList(1);
			l2.add(base);
			return l2;
		}
		return l;
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
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MeioTransporteRodoviario bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMeioTransporteRodoviarioDAO(MeioTransporteRodoviarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MeioTransporteRodoviarioDAO getMeioTransporteRodoviarioDAO() {
		return (MeioTransporteRodoviarioDAO) getDao();
	}
	
	public String calculaCapacidade(TypedFlatMap values) {
		BigDecimal vlAlturaBau = values.getBigDecimal("vlAlturaBau");
		BigDecimal vlProfundidadeBau = values.getBigDecimal("vlProfundidadeBau");
		BigDecimal vlLarguraBau = values.getBigDecimal("vlLarguraBau");

		BigDecimal result = new BigDecimal(0);

		if (vlAlturaBau == null || vlProfundidadeBau == null || vlLarguraBau == null)
			return "";

		result = vlAlturaBau.multiply(vlProfundidadeBau.multiply(vlLarguraBau)).setScale(2,BigDecimal.ROUND_UP);

		return FormatUtils.formatDecimal("#,##0.00", result);
	}

// #############################################################################################################################
// ROTINA CRIADA EM 03-01-2006: Verificar Estado de Meio de Transporte
// #############################################################################################################################
		 
	/**
	 * <b>Verificar Estado Meio Transporte:</b><br>
	 * Executa validações extras no meio de transporte (utilizada no carregamento).
	 * 
	 * @param idMeioTransporte
	 * @return 
	 */
	public void validateEstadoMeioTransporte(Long idMeioTransporte) {
		Map mtr = getMeioTransporteRodoviarioDAO().findMeioTransporteToEstadoLiberacao(idMeioTransporte);
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		if (mtr == null || mtr.get("dtVencimentoSeguro") == null ||
				dataAtual.compareTo((YearMonthDay)mtr.get("dtVencimentoSeguro")) >= 0)
			throw new BusinessException("LMS-26032");
		
		//findProprietarioByMeioTransporte retorna NULL quando não encontra um proprietário vigente para o meio de transporte.
		Map prop = meioTranspProprietarioService.findProprietarioByMeioTransporte(idMeioTransporte);
		if (prop == null || prop.get("proprietario") == null)
			throw new BusinessException("LMS-26033");
		
	}

	/**
	 * Métiodo que busca o MeioTransporteRodoviario com o ID do MeioTransporte
	 * @param idMeioTransporte
	 * @return
	 */
	public MeioTransporteRodoviario findMeioTransporteRodoviarioByIdMeioTransporte(Long idMeioTransporte) {
		return this.getMeioTransporteRodoviarioDAO().findMeioTransporteRodoviarioByIdMeioTransporte(idMeioTransporte);		
	}

	/**
	 * 
	 * @param idMeioTransporte
	 * @return
	 */
	public Map findDadosTipoMeioTransporte(Long idMeioTransporte) {
		return getMeioTransporteRodoviarioDAO().findDadosTipoMeioTransporte(idMeioTransporte);
	}

	/**
	 * Find da lookup de tipo de Meio de Transporte quando é a tela ManterMeiosTransporteRodo é
	 * chamada como uma lookup e é necessário restringir apenas o tipo de meio de transporte criterio
	 * e seu composto.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findListByIdWithComposto(TypedFlatMap criteria) {
		Long id = criteria.getLong("idTipoMeioTransporteForCombo");

		TipoMeioTransporte tmt = (TipoMeioTransporte)getMeioTransporteRodoviarioDAO().getAdsmHibernateTemplate()
				.load(TipoMeioTransporte.class,id);
		TipoMeioTransporte tmtCompos = tmt.getTipoMeioTransporte();

		List retorno = new ArrayList(2);

		if (tmt != null) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idTipoMeioTransporte",tmt.getIdTipoMeioTransporte());
			tfm.put("dsTipoMeioTransporte",tmt.getDsTipoMeioTransporte());
			retorno.add(tfm);
		}

		if (tmtCompos != null) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idTipoMeioTransporte",tmtCompos.getIdTipoMeioTransporte());
			tfm.put("dsTipoMeioTransporte",tmtCompos.getDsTipoMeioTransporte());
			retorno.add(tfm);
		}

		return retorno;
	}
	
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
}