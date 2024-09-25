package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PermissoEmpresaPais;
import com.mercurio.lms.municipios.model.dao.PermissoEmpresaPaisDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.municipios.permissoEmpresaPaisService"
 */
public class PermissoEmpresaPaisService extends CrudService<PermissoEmpresaPais, Long> {

	private EmpresaService empresaService;
	private VigenciaService vigenciaService;

	protected PermissoEmpresaPais beforeStore(PermissoEmpresaPais bean) {
		PermissoEmpresaPais permissoEmpresaPais = (PermissoEmpresaPais) bean;

		Empresa empresa = empresaService.findById(permissoEmpresaPais.getEmpresa().getIdEmpresa());
		
		if (empresa.getTpSituacao().getValue().equalsIgnoreCase("I"))
			throw new BusinessException("LMS-29023");

		if (permissoEmpresaPais.getPaisByIdPaisOrigem().getIdPais().intValue() == permissoEmpresaPais
				.getPaisByIdPaisDestino().getIdPais().intValue())
			throw new BusinessException("LMS-29007");

		Long idEmpresa = permissoEmpresaPais.getEmpresa().getIdEmpresa();
		Long idPaisOrigem = permissoEmpresaPais.getPaisByIdPaisOrigem()
				.getIdPais();
		Long idPaisDestino = permissoEmpresaPais.getPaisByIdPaisDestino()
				.getIdPais();

		if (getPermissoEmpresaPaisDAO().verificaPermissoEmpresaPaises(
				idEmpresa, idPaisOrigem, idPaisDestino,
				permissoEmpresaPais.getIdPermissoEmpresaPais(),
				permissoEmpresaPais.getDtVigenciaInicial(),
				permissoEmpresaPais.getDtVigenciaFinal()))
			throw new BusinessException("LMS-00003");
		if (getPermissoEmpresaPaisDAO().verificaPermissoEmpresaPaises(
				idEmpresa, idPaisDestino, idPaisOrigem,
				permissoEmpresaPais.getIdPermissoEmpresaPais(),
				permissoEmpresaPais.getDtVigenciaInicial(),
				permissoEmpresaPais.getDtVigenciaFinal()))
			throw new BusinessException("LMS-00003");

		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>PermissoEmpresaPais</code> a partir do
	 * ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws //
	 *             
	 */
	public PermissoEmpresaPais findById(java.lang.Long id) {
		return (PermissoEmpresaPais) super.findById(id);
	}

	public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
		PermissoEmpresaPais permissoEmpresa = (PermissoEmpresaPais) super
				.findById(id);

		TypedFlatMap mapPermissoEmpresa = new TypedFlatMap();

		Integer acaoVigencia = JTVigenciaUtils
				.getIntegerAcaoVigencia(permissoEmpresa);

		mapPermissoEmpresa.put("acaoVigenciaAtual", acaoVigencia);

		final Empresa empresa = permissoEmpresa.getEmpresa();
		mapPermissoEmpresa.put("empresa.idEmpresa", empresa.getIdEmpresa());
		mapPermissoEmpresa.put("empresa.pessoa.nmPessoa", empresa.getPessoa()
				.getNmPessoa());
		mapPermissoEmpresa.put("empresa.tpSituacao", empresa.getTpSituacao());
		mapPermissoEmpresa.put("empresa.pessoa.nrIdentificacao", FormatUtils
				.formatIdentificacao(empresa.getPessoa().getTpIdentificacao(),
						empresa.getPessoa().getNrIdentificacao()));
		mapPermissoEmpresa.put("empresa.pessoa.nrIdentificacaoFormatado",
				FormatUtils.formatIdentificacao(empresa.getPessoa()
						.getTpIdentificacao(), empresa.getPessoa()
						.getNrIdentificacao()));

		final Pais paisByIdPaisOrigem = permissoEmpresa.getPaisByIdPaisOrigem();
		mapPermissoEmpresa.put("paisByIdPaisOrigem.idPais", paisByIdPaisOrigem
				.getIdPais());
		mapPermissoEmpresa.put("paisByIdPaisOrigem.nmPais", paisByIdPaisOrigem
				.getNmPais().getValue());

		final Pais paisByIdPaisDestino = permissoEmpresa
				.getPaisByIdPaisDestino();
		mapPermissoEmpresa.put("paisByIdPaisDestino.idPais",
				paisByIdPaisDestino.getIdPais());
		mapPermissoEmpresa.put("paisByIdPaisDestino.nmPais",
				paisByIdPaisDestino.getNmPais().getValue());

		mapPermissoEmpresa.put("nrPermisso", permissoEmpresa.getNrPermisso());
		mapPermissoEmpresa.put("nrPermissoMic", permissoEmpresa
				.getNrPermissoMic());
		mapPermissoEmpresa.put("dtVigenciaInicial", permissoEmpresa
				.getDtVigenciaInicial());
		mapPermissoEmpresa.put("dtVigenciaFinal", permissoEmpresa
				.getDtVigenciaFinal());
		mapPermissoEmpresa.put("idPermissoEmpresaPais", permissoEmpresa
				.getIdPermissoEmpresaPais());

		return mapPermissoEmpresa;
	}

	private void validaRemoveById(Long id) {
		PermissoEmpresaPais permissoEmpresa = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(permissoEmpresa);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		validaRemoveById((Long) id);
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (int i = 0; i < ids.size(); i++)
			validaRemoveById((Long) ids.get(i));
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PermissoEmpresaPais bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map storeMap(Map bean) {
		PermissoEmpresaPais permissoEmpresaPais = new PermissoEmpresaPais();

		ReflectionUtils.copyNestedBean(permissoEmpresaPais, bean);

		vigenciaService.validaVigenciaBeforeStore(permissoEmpresaPais);
		super.store(permissoEmpresaPais);
		Integer acaoVigencia = JTVigenciaUtils
				.getIntegerAcaoVigencia(permissoEmpresaPais);
		bean.put("acaoVigenciaAtual", acaoVigencia);
		bean.put("idPermissoEmpresaPais", permissoEmpresaPais
				.getIdPermissoEmpresaPais());
		return bean;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setPermissoEmpresaPaisDAO(PermissoEmpresaPaisDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private PermissoEmpresaPaisDAO getPermissoEmpresaPaisDAO() {
		return (PermissoEmpresaPaisDAO) getDao();
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public PermissoEmpresaPais findByFilialOrigemDestino(Long idPaisOrigem, Long idPaisDestino){
		return getPermissoEmpresaPaisDAO().findByFilialOrigemDestino(idPaisOrigem, idPaisDestino);
	}
}