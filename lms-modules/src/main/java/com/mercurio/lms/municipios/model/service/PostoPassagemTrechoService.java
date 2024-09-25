package com.mercurio.lms.municipios.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.PostoPassagemTrecho;
import com.mercurio.lms.municipios.model.dao.PostoPassagemTrechoDAO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.postoPassagemTrechoService"
 */
public class PostoPassagemTrechoService extends CrudService<PostoPassagemTrecho, Long> {
	private VigenciaService vigenciaService;
	private PostoPassagemService postoPassagemService;
	private OrdemFilialFluxoService ordemFilialFluxoService;

	public PostoPassagemTrecho beforeStore(PostoPassagemTrecho bean) {
		PostoPassagemTrecho postoPassagemTrecho = (PostoPassagemTrecho)bean;
		if (getPostoPassagemTrechoDAO().findPostoPassagemVigenteByFiliais(postoPassagemTrecho)) {
			throw new BusinessException("LMS-00003");
		}	
		return super.beforeStore(bean);
	}

	public java.io.Serializable store(PostoPassagemTrecho postoPassagemTrecho) {
		vigenciaService.validaVigenciaBeforeStore(postoPassagemTrecho);
		return super.store(postoPassagemTrecho);
	}

	public PostoPassagemTrecho findById(Long id) {
		return (PostoPassagemTrecho) super.findById(id);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Conta o numero de postos de passagem existentes no trecho entre uma filial de origem e uma filial de destino
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param dtVigencia
	 * @return
	 */
	public Integer findQtdPostosPassagemByRotaFilialOrigemFilialDestino(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia){
		Integer sum = Integer.valueOf(0);
		List<OrdemFilialFluxo> list = ordemFilialFluxoService.findOrdensFilialFluxoByIdFilialOrigemAndIdFilialDestino(idFilialOrigem, idFilialDestino, dtVigencia);
		for (int i = 0; list.size() > 1 && i < list.size() - 1; i++) {
			OrdemFilialFluxo ordemFilialFluxoOrig = list.get(i);
			OrdemFilialFluxo ordemFilialFluxoDest = list.get(i + 1);
			sum = sum + getPostoPassagemTrechoDAO().findQtdPostosPassagemByRotaFilialOrigemFilialDestino(ordemFilialFluxoOrig.getFilial().getIdFilial(), ordemFilialFluxoDest.getFilial().getIdFilial(), dtVigencia);
	}
		return sum;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		validateFindPaginated(criteria, true);
		return getPostoPassagemTrechoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		if(!validateFindPaginated(criteria, false)) {
			return IntegerUtils.ZERO;
		}
		return getPostoPassagemTrechoDAO().getRowCount(criteria);
	}

	private boolean validateFindPaginated(Map<String, Object> criteria, boolean showException) {
		boolean isValid = true;
		if(!SessionUtils.isFilialSessaoMatriz()) {
			Long idFilialOrigem = MapUtils.getLong(criteria, "filialOrigem.idFilial");
			Long idFilialDestino = MapUtils.getLong(criteria, "filialDestino.idFilial");
			Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
			if( (idFilialOrigem != null) && (idFilialDestino != null) ) {
				if(CompareUtils.ne(idFilialOrigem, idFilialSessao) && CompareUtils.ne(idFilialDestino, idFilialSessao)) {
					isValid = false;
				}
			} else if( (idFilialOrigem != null) && (CompareUtils.ne(idFilialOrigem, idFilialSessao)) ) {
				isValid = false;
			} else if( (idFilialDestino != null) && (CompareUtils.ne(idFilialDestino, idFilialSessao)) ) {
				isValid = false;
			}
		}
		if(!isValid && showException) {
			throw new BusinessException("LMS-09026");
		}
		return isValid;
	}

	/**
	 * ROTINA: ET 29.06.01.10 Busca Postos Passagem Rota
	 * Busca Postos Passagem Rota e Retorna os Pedágios de uma Rota e seus respectivos tipos de pagamento aceito prioritários.
	 * @param List filiaisRotas
	 * @param Long idTipoMeioTransporte
	 * @param Integer qtEixosMeioTransporte
	 * @param Long idTipoMeioTranspComposto
	 * @param Integer qtEixosMeioTranspComp 
	 * @param YearMonthDay dtCalculo 
	 * @param Long idMoedaPais 
	 * @param Boolean blNaoConsiderarSemParar
	 * 
	 * @return List
	 */
	public List<TypedFlatMap> findPostosPassagemByFiliaisRota(
			List<Filial> filiaisRotas, 
			Long idTipoMeioTransporte, 
			Integer qtEixosMeioTransporte, 
			Long idTipoMeioTranspComposto, 
			Integer qtEixosMeioTranspComp, 
			YearMonthDay dtCalculo, 
			Long idMoedaPais,
			Boolean blNaoConsiderarSemParar
	) {
		List<TypedFlatMap> listaPostosPassagem = new ArrayList<TypedFlatMap>();

		if(dtCalculo == null)
			dtCalculo = JTDateTimeUtils.getDataAtual();

		if(!filiaisRotas.isEmpty()) {
			if(filiaisRotas.size()< 2)
				throw new BusinessException("LMS-29152");

			for (int i = 0; i < filiaisRotas.size() - 1; i++) {
				Filial filialOrigem = (Filial) filiaisRotas.get(i);
				int h = i + 1;

				for(int j=h; j <= h; j++){
					Filial filialDestino = (Filial)filiaisRotas.get(j);
					List<Map<String, Long>> listaPostos = getPostoPassagemTrechoDAO().findPostosPassagemByFiliais(filialOrigem.getIdFilial(),filialDestino.getIdFilial(),dtCalculo);
					for(Map<String, Long> mapPostoPassagem : listaPostos) {
						Long idPostoPassagem = mapPostoPassagem.get("idPostoPassagem");
						Integer qtEixos = null;
						if (qtEixosMeioTranspComp != null)
							qtEixos = qtEixosMeioTransporte.intValue() + qtEixosMeioTranspComp.intValue();
						else
							qtEixos = qtEixosMeioTransporte;

						TypedFlatMap typedFlatMapPostos = postoPassagemService.findVlByTpMeioTransporte(
								idPostoPassagem,
								idTipoMeioTransporte,
								qtEixos,dtCalculo,
								idMoedaPais,
								blNaoConsiderarSemParar
						);
						typedFlatMapPostos.put("idPostoPassagem", idPostoPassagem);
						listaPostosPassagem.add(typedFlatMapPostos);
					}
				}
			}
		}
		return listaPostosPassagem;
	}

	/**
	 * ROTINA: ET 29.06.01.10 Busca Valor Posto Passagem Rota
	 * Busca Valor Posto Passagem Rota e retorna o valor do pedágio na data de consulta

	 * @param List filiaisRotas
	 * @param Long idTipoMeioTransporte
	 * @param Integer qtEixosMeioTransporte
	 * @param Long idTipoMeioTranspComposto
	 * @param Integer qtEixosMeioTranspComp 
	 * @param YearMonthDay dtCalculo 
	 * @param Long idMoedaPais
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal findValorPostosPassagemRota(List<Filial> filiaisRotas, Long idTipoMeioTransporte, Integer qtEixosMeioTransporte, Long idTipoMeioTranspComposto, Integer qtEixosMeioTranspComp, YearMonthDay dtCalculo, Long idMoedaPais){
		BigDecimal valorPostoPassagemRota = BigDecimalUtils.ZERO;
		List<TypedFlatMap> postosPassagem = this.findPostosPassagemByFiliaisRota(
				filiaisRotas,
				idTipoMeioTransporte,
				qtEixosMeioTransporte,
				idTipoMeioTranspComposto,
				qtEixosMeioTranspComp,
				dtCalculo,
				idMoedaPais,
				false
		);

		for(TypedFlatMap typedFlatMap : postosPassagem) {
			if(typedFlatMap.getBigDecimal("vlPostoPassagem") != null) {
				BigDecimal valor = (BigDecimal)typedFlatMap.getBigDecimal("vlPostoPassagem");
				valorPostoPassagemRota= valorPostoPassagemRota.add(valor);
			}
		}
		return valorPostoPassagemRota;
	}

	protected void beforeRemoveById(Long id) {
		PostoPassagemTrecho postoPassagemTrecho = (PostoPassagemTrecho)getPostoPassagemTrechoDAO().getAdsmHibernateTemplate().load(PostoPassagemTrecho.class,id);
		if(postoPassagemTrecho != null)
			JTVigenciaUtils.validaVigenciaRemocao(postoPassagemTrecho);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for(int i=0; i< ids.size(); i++){
			Long idPostoPassagemTrecho =(Long) ids.get(i);
			PostoPassagemTrecho postoPassagemTrecho = (PostoPassagemTrecho)getPostoPassagemTrechoDAO().getAdsmHibernateTemplate().load(PostoPassagemTrecho.class,idPostoPassagemTrecho);
			if(postoPassagemTrecho != null)
				JTVigenciaUtils.validaVigenciaRemocao(postoPassagemTrecho);
		}
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPostoPassagemTrechoDAO(PostoPassagemTrechoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PostoPassagemTrechoDAO getPostoPassagemTrechoDAO() {
		return (PostoPassagemTrechoDAO) getDao();
	}

	public void setPostoPassagemService(PostoPassagemService postoPassagemService) {
		this.postoPassagemService = postoPassagemService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public OrdemFilialFluxoService getOrdemFilialFluxoService() {
		return ordemFilialFluxoService;
} 

	public void setOrdemFilialFluxoService(
			OrdemFilialFluxoService ordemFilialFluxoService) {
		this.ordemFilialFluxoService = ordemFilialFluxoService;
	}
} 
