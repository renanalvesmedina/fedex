package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.AliquotaIssMunicipioServ;
import com.mercurio.lms.tributos.model.ParametroIssMunicipio;
import com.mercurio.lms.tributos.model.dao.AliquotaIssMunicipioServDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.aliquotaIssMunicipioServService"
 */
public class AliquotaIssMunicipioServService extends CrudService<AliquotaIssMunicipioServ, Long> {
	
    private ParametroIssMunicipioService parametroIssMunicipioService;


	/**
	 * Recupera uma instância de <code>AliquotaIssMunicipioServ</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public AliquotaIssMunicipioServ findById(java.lang.Long id) {
        return (AliquotaIssMunicipioServ)super.findById(id);
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
	 *
	 *
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
    public java.io.Serializable store(AliquotaIssMunicipioServ bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAliquotaIssMunicipioServDAO(AliquotaIssMunicipioServDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AliquotaIssMunicipioServDAO getAliquotaIssMunicipioServDAO() {
        return (AliquotaIssMunicipioServDAO) getDao();
    }
    
    /**
     * Antes de salvar uma AliquotaIssMunicipioServ deve-se verificar se existe 
     * algum conflito de vigências.
     * @param Object Objeto AliquotaIssMunicipioServ
     * @return Objeto salvo
     */
    public AliquotaIssMunicipioServ beforeStore(AliquotaIssMunicipioServ bean) {
        
        if( this.getAliquotaIssMunicipioServDAO().existeConflitoVigencias((AliquotaIssMunicipioServ)bean) ){
            throw new BusinessException("LMS-00047");
        }
        
        return super.beforeStore(bean);
    }
    
    /**
     * Busca as aliquotasIssMunicipioServico de acordo com os critérios de pesquisa e que esteja vigente de acordo com a data base
     * @param idServicoAdicional Identificador do Serviço Adicional
     * @param idServicoTributo Identificador do Serviço Tributo
     * @param idMunicipioIncidencia Identificador do Município de Incidência
     * @param dtBase Data Base
     * @param isCarreteiro Booleano que indica se a pesquisa está sendo feita para cálculo do ISS <code>false</code> ou do ISS Carreteiro <code>true</code>
     * @return Lista de aliquotasIssMunicipio com os seguintes dados:
     *          - AliquotaIssMunicipioServ.idAliquotaIssMunicipioServ
     *          - AliquotaIssMunicipioServ.pcAliquota
     *          - AliquotaIssMunicipioServ.pcEmbute
     *          - AliquotaIssMunicipioServ.IssMunicipioServico.idIssMunicipioServico
     *          - AliquotaIssMunicipioServ.IssMunicipioServico.ServicoMunicipio.idServicoMunicipio
     *          - AliquotaIssMunicipioServ.IssMunicipioServico.ServicoMunicipio.nrServicoMunicipio
     */
    public List findAliquotaIssMunicipioServicoByCriterios(Long idServicoAdicional, Long idServicoTributo, Long idMunicipioIncidencia, YearMonthDay dtBase, Boolean isCarreteiro){
        
        return this.getAliquotaIssMunicipioServDAO().findAliquotaIssMunicipioServicoByCriterios(idServicoAdicional, idServicoTributo, idMunicipioIncidencia, dtBase, isCarreteiro);
        
    }
    
    /**
     * De acordo com o serviço (SERVICO_ADICIONAL ou SERVICO_TRIBUTO) 
     * verifica SERVICO_OFICIAL_TRIBUTO.TP_LOCAL_DEVIDO (L, S)
     * Se L utilizar o parâmetro idMunicipioPrestacao, se S, idMunicipioSede
     * para buscar ALIQUOTA_ISS_MUNICIPIO_SERVICE.BL_EMITE_NF_SERVICO
     * vigente na data atual
     * @param idServicoAdicional
     * @param idServicoTributo
     * @param idMunicipioSede - municipio da filial prestadora do serviço
     * @param idMunicipioPrestacao - municipio onde o serviço está sendo prestado
     * @return true quando emite nf
     * @throws com.mercurio.adsm.framework.BusinessException(LMS-23007") deve ser informado Serviço Adicional ou Servico Tributo
     */
    public TypedFlatMap findEmiteNfServico(Long idServicoAdicional, Long idServicoTributo, Long idMunicipioSede   , Long idMunicipioPrestacao){
    	//Regra 1
    	if ( (idServicoAdicional != null && idServicoTributo != null) || (idServicoAdicional == null && idServicoTributo == null)){
    		throw new BusinessException("LMS-23007");
    	}
    	
        YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
        
        //Regra 5
        AliquotaIssMunicipioServ aliquotaIssMunicipioServ = this.getAliquotaIssMunicipioServDAO().findAliquotaIssMunicipioServByNfServico(idServicoAdicional, idServicoTributo, idMunicipioSede, dtAtual);
        
        TypedFlatMap retorno = new TypedFlatMap();
        
        if( aliquotaIssMunicipioServ == null || !aliquotaIssMunicipioServ.getBlEmiteNfServico() ){
        	retorno.put("BlEmiteNota", Boolean.FALSE);
        } else {
        	retorno.put("BlEmiteNota", Boolean.TRUE);
        }
        
        //Regra 6
        ParametroIssMunicipio parametroIssMunicipio = parametroIssMunicipioService.findByMunicipio(idMunicipioSede);
        
        if (parametroIssMunicipio == null || parametroIssMunicipio.getDtEmissaoNotaFiscalEletronica() == null) {
        	retorno.put("BlEmiteNFeletronica", Boolean.FALSE);
        } else if (CompareUtils.ge(dtAtual,  JTDateTimeUtils.convertDataStringToYearMonthDay(parametroIssMunicipio.getDtEmissaoNotaFiscalEletronica().toString(), "yyyy-MM-dd"))) {
        	retorno.put("BlEmiteNFeletronica", Boolean.TRUE);
        } else {
        	retorno.put("BlEmiteNFeletronica", Boolean.FALSE);
        }

    	return retorno;
    	
    }

	public void setParametroIssMunicipioService(
			ParametroIssMunicipioService parametroIssMunicipioService) {
		this.parametroIssMunicipioService = parametroIssMunicipioService;
	}
    
}