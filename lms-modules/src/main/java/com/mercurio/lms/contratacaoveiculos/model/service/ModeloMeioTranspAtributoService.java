package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo;
import com.mercurio.lms.contratacaoveiculos.model.dao.ModeloMeioTranspAtributoDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contratacaoveiculos.modeloMeioTranspAtributoService"
 */
public class ModeloMeioTranspAtributoService extends CrudService<ModeloMeioTranspAtributo, Long> {
	private ConteudoAtributoModeloService conteudoAtributoModeloService;

	public ConteudoAtributoModeloService getConteudoAtributoModeloService() {
		return conteudoAtributoModeloService;
	}

	public void setConteudoAtributoModeloService(
			ConteudoAtributoModeloService conteudoAtributoModeloService) {
		this.conteudoAtributoModeloService = conteudoAtributoModeloService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 *      Um mesmo atributo não pode estar associado mais de uma vez ao mesmo
	 *      Modelo. Caso aconteça deve mostrar a mensagem LMS-00002 e abortar a
	 *      operação.
	 */
	protected ModeloMeioTranspAtributo beforeStore(ModeloMeioTranspAtributo bean) {
		ModeloMeioTranspAtributo modeloMeioTranspAtributo = (ModeloMeioTranspAtributo) bean;
		Long idAtributo = modeloMeioTranspAtributo.getAtributoMeioTransporte()
				.getIdAtributoMeioTransporte();
		Long idTipo = modeloMeioTranspAtributo.getTipoMeioTransporte().getIdTipoMeioTransporte();
		if (getModeloMeioTranspAtributoDAO().verificaAtributoModelo(idAtributo,
				idTipo,
				modeloMeioTranspAtributo.getIdModeloMeioTranspAtributo()))
			throw new BusinessException("LMS-00002");	
		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>ModeloMeioTranspAtributo</code> a
	 * partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws //
	 *             
	 */
	public ModeloMeioTranspAtributo findById(java.lang.Long id) {

		return (ModeloMeioTranspAtributo) super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
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
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * 
	 * Foi implementado dessa forma, devido a list do bean, pois se na list for
	 * deletado um objeto, o hibernate nao deleta do banco, apenas atualiza os
	 * existentes. o seguinte é feito: é salvo o proprio objeto que vem do
	 * banco, setando os atributos do bean no objeto do banco.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public TypedFlatMap store(ModeloMeioTranspAtributo bean) {
		 
		ModeloMeioTranspAtributo modelo = null;
		
		if (bean.getIdModeloMeioTranspAtributo() != null) {
			modelo = findById(bean.getIdModeloMeioTranspAtributo());
			
			modelo.setBlOpcional(bean.getBlOpcional());
			modelo.setAtributoMeioTransporte(bean.getAtributoMeioTransporte());
			modelo.setTpSituacao(bean.getTpSituacao());
			
			List lista = new ArrayList();
			
			if (!modelo.getConteudoAtributoModelos().isEmpty()) {
				
				for (Iterator iter = modelo.getConteudoAtributoModelos().iterator(); iter.hasNext();) {
					ConteudoAtributoModelo conteudoAtributoModeloBanco = (ConteudoAtributoModelo) iter.next();
					getModeloMeioTranspAtributoDAO().getAdsmHibernateTemplate().evict(conteudoAtributoModeloBanco);
					
					if (bean.getConteudoAtributoModelos() == null || bean.getConteudoAtributoModelos().isEmpty()) {
						iter.remove();
					} else {
						boolean removido = true;
						for (Iterator iterBean = bean.getConteudoAtributoModelos().iterator(); iterBean.hasNext();) {
							ConteudoAtributoModelo conteudoAtributoModelo = (ConteudoAtributoModelo) iterBean.next();
							if (conteudoAtributoModelo.getDsConteudoAtributoModelo() != null) {
								if (conteudoAtributoModelo.getDsConteudoAtributoModelo().getValue().equalsIgnoreCase(conteudoAtributoModeloBanco.getDsConteudoAtributoModelo().getValue())) {
									removido = false;
									iterBean.remove();
								} else if (conteudoAtributoModelo.getIdConteudoAtributoModelo() != null 
											&& conteudoAtributoModelo.getIdConteudoAtributoModelo().equals(conteudoAtributoModeloBanco.getIdConteudoAtributoModelo())){
									iter.remove();
									removido = false;
								}
							}							
						}
						
						if (removido) {
							iter.remove();
						}						
					}
				}

				if (bean.getConteudoAtributoModelos() != null) {
					for (Iterator iterBeanNovo = bean.getConteudoAtributoModelos().iterator(); iterBeanNovo.hasNext();) {
						ConteudoAtributoModelo conteudoAtributoModelo = (ConteudoAtributoModelo) iterBeanNovo.next();
						if(conteudoAtributoModelo.getIdConteudoAtributoModelo() == null){
							conteudoAtributoModelo.setIdConteudoAtributoModelo(null);
							conteudoAtributoModelo.setModeloMeioTranspAtributo(bean);
						}
						
						if(conteudoAtributoModelo.getDsConteudoAtributoModelo()!= null){
							lista.add(conteudoAtributoModelo);
						}
					}
				}
				if (!lista.isEmpty()) {
					modelo.getConteudoAtributoModelos().addAll(lista);
				}


			} else {
				if (bean.getConteudoAtributoModelos()!= null && !bean.getConteudoAtributoModelos().isEmpty()){
					for (Iterator iter = bean.getConteudoAtributoModelos().iterator(); iter.hasNext();){
						ConteudoAtributoModelo contAtribModel = (ConteudoAtributoModelo)iter.next();
						if(contAtribModel != null){
							contAtribModel.setIdConteudoAtributoModelo(null);
							if(contAtribModel.getDsConteudoAtributoModelo().equals(""))
								iter.remove();
						}	
					}
				
				modelo.getConteudoAtributoModelos().addAll(
						bean.getConteudoAtributoModelos());
				}
			}
			
						
		} else {
			if (bean.getConteudoAtributoModelos()!= null && !bean.getConteudoAtributoModelos().isEmpty()){
				for (Iterator iter = bean.getConteudoAtributoModelos().iterator(); iter.hasNext();){
					ConteudoAtributoModelo contAtribModel = (ConteudoAtributoModelo)iter.next();
					if(contAtribModel != null){
						contAtribModel.setIdConteudoAtributoModelo(null);
						contAtribModel.setModeloMeioTranspAtributo(bean);
						if(contAtribModel.getDsConteudoAtributoModelo()== null)
							iter.remove();
					}	
				}
			}
			
			modelo = bean;
		}
		
		super.store(modelo);
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idModeloMeioTranspAtributo", modelo.getIdModeloMeioTranspAtributo());
		retorno.put("conteudoAtributoModelos", transformaLista(modelo.getConteudoAtributoModelos()));
				
		return retorno;

	}

	private List transformaLista(List conteudos){
		List retorno = null;
		
		if (conteudos != null){
			retorno = new ArrayList();
			
			for (Iterator iter = conteudos.iterator(); iter.hasNext();) {
				ConteudoAtributoModelo cam = (ConteudoAtributoModelo) iter.next();
				TypedFlatMap map = new TypedFlatMap();
				map.put("idConteudoAtributoModelo", cam.getIdConteudoAtributoModelo());
				map.put("dsConteudoAtributoModelo", cam.getDsConteudoAtributoModelo());
				
				retorno.add(map);
			}
		}
		
		return retorno;
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setModeloMeioTranspAtributoDAO(
			ModeloMeioTranspAtributoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private ModeloMeioTranspAtributoDAO getModeloMeioTranspAtributoDAO() {
		return (ModeloMeioTranspAtributoDAO) getDao();
	}

	/**
	 * Encontra atributos de um determinado modelo.
	 * @param idModelo
	 * @return
	 */
	public List findAtributosByModelo(Long idModelo) {
		return getModeloMeioTranspAtributoDAO().findAtributosByModelo(idModelo);
	}
	
	/**
	 * Solicitação CQPRO00006051 da integração.
	 * Método que retorna uma instancia da classe ModeloMeioTranspAtributo de acordo com os parâmetros passados.
	 * @param idAtributoMeioTransporte
	 * @param idTipoMeioTransporte 
	 * @return
	 */
	public ModeloMeioTranspAtributo findAtributoTipoMeioTransporte(Long idAtributoMeioTransporte , Long idTipoMeioTransporte){
		return getModeloMeioTranspAtributoDAO().findAtributoTipoMeioTransporte(idAtributoMeioTransporte, idTipoMeioTransporte);
	}

}