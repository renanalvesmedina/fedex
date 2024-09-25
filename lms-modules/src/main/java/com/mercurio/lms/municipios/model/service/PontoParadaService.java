package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.PontoParada;
import com.mercurio.lms.municipios.model.dao.PontoParadaDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.pontoParadaService"
 */
public class PontoParadaService extends CrudService<PontoParada, Long> {
	private EnderecoPessoaService enderecoPessoaService;
	private RodoviaService rodoviaService;
	private PessoaService pessoaService;

	/**
	 * Recupera uma instância de <code>PontoParada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PontoParada findById(java.lang.Long id) {
		return (PontoParada)super.findById(id);
	}

	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = super.findPaginated(criteria);

		List retornoList = new ArrayList();
		List<PontoParada> pontosParada = rsp.getList();
		for (PontoParada pontoParada : pontosParada) {
			EnderecoPessoa enderecoPessoa = findUltimoEnderecoPontoParada((Long)pontoParada.getIdPontoParada());

			Map pp = new HashMap();
			Map mun = new HashMap(); 
			Map uf = new HashMap();
			Map pais = new HashMap();
			Map rodo = new HashMap();

			pp.put("idPontoParada",pontoParada.getIdPontoParada());
			pp.put("nmPontoParada",pontoParada.getNmPontoParada());
			mun.put("nmMunicipio",pontoParada.getMunicipio().getNmMunicipio());
			pp.put("municipio",mun);
			uf.put("sgUnidadeFederativa",pontoParada.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			uf.put("nmUnidadeFederativa",pontoParada.getMunicipio().getUnidadeFederativa().getNmUnidadeFederativa());
			uf.put("siglaDescricao",pontoParada.getMunicipio().getUnidadeFederativa().getSiglaDescricao());
			mun.put("unidadeFederativa",uf);
			pais.put("nmPais",pontoParada.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue());
			uf.put("pais",pais);
			if (pontoParada.getRodovia() != null) {
				rodo.put("sgDsRodovia",pontoParada.getRodovia().getSgDsRodovia());
				rodo.put("sgRodovia",pontoParada.getRodovia().getSgRodovia());
			}
			pp.put("rodovia",rodo);
			pp.put("nrKm",pontoParada.getNrKm());
			pp.put("blAduana",pontoParada.getBlAduana());
			pp.put("sgAduana",pontoParada.getSgAduana());

			if (enderecoPessoa != null) {
				pp.put("nrLatitudePessoa",enderecoPessoa.getNrLatitude());
				pp.put("nrLongitudePessoa",enderecoPessoa.getNrLongitude());	
			}
			retornoList.add(pp);
		}

		rsp.setList(retornoList);
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
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PontoParada bean) {
		pessoaService.store((PontoParada)bean);

		return super.store(bean);
	}

	protected PontoParada beforeStore(PontoParada bean) {
		PontoParada p = (PontoParada) bean;

		if ( (p.getRodovia() != null) && 
				(p.getMunicipio().getUnidadeFederativa() != null) && 
					(rodoviaService.possuiUnidadeFederativaDiferente(
							p.getRodovia().getIdRodovia(), 
							p.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa()))) {	

			throw new BusinessException("LMS-29075");
		}

		if (p.getBlAduana().booleanValue()) {
			if(StringUtils.isBlank(p.getSgAduana()) || (p.getCdAduana() == null)) {
				throw new BusinessException("LMS-29076");
			}
		}

		return super.beforeStore(bean);
	}

	public List findLookupPontoParada(Map criteria) {
		return getPontoParadaDAO().findLookupByCriteria(criteria);
	}

	public EnderecoPessoa findUltimoEnderecoPontoParada(Long idPontoParada) {
		return enderecoPessoaService.getUltimoEndereco(idPontoParada);
	}

	/**
	 * @param idRotaIdaVolta
	 * @return
	 */
	public List findPontoParadaByRotaIdaVolta(Long idRotaIdaVolta) {
		List lista = getPontoParadaDAO().findPontoParadaByRotaIdaVolta(idRotaIdaVolta);
		return new AliasToNestedBeanResultTransformer(PontoParada.class).transformListResult(lista);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPontoParadaDAO(PontoParadaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PontoParadaDAO getPontoParadaDAO() {
		return (PontoParadaDAO) getDao();
	}
	public void setRodoviaService(RodoviaService rodoviaService) {
		this.rodoviaService = rodoviaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}