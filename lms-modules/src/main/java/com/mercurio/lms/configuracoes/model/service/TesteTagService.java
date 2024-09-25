package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.TesteTag;
import com.mercurio.lms.configuracoes.model.dao.TesteTagDAO;
import com.mercurio.lms.tributos.model.service.BuscarCfopService;
import com.mercurio.lms.tributos.model.service.CalcularIvaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.testeTagService"
 */
public class TesteTagService extends CrudService<TesteTag, Long> {

	/**
	 * Recupera uma inst�ncia de <code>TesteTag</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    
	BuscarCfopService buscarCfopService;
	CalcularIvaService calcularIvaService;
	
	public BuscarCfopService getBuscarCfopService() {
		return buscarCfopService;
	}

	public void setBuscarCfopService(BuscarCfopService buscarCfopService) {
		this.buscarCfopService = buscarCfopService;
	}

	public TesteTag findById(java.lang.Long id) {
        return (TesteTag)super.findById(id);
    }
    
    public String aprovacao(){
		return calcularIvaService.findValorIva(Long.valueOf(1), new YearMonthDay(2003,06,06), new BigDecimal(20)).toString();
    }
    
    public Map aprovacaoErro() throws Exception{
    	throw new Exception("teste de exception");
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
    public java.io.Serializable store(TesteTag bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTesteTagDAO(TesteTagDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TesteTagDAO getTesteTagDAO() {
        return (TesteTagDAO) getDao();
    }

	public CalcularIvaService getCalucularIvaService() {
		return calcularIvaService;
	}

	public void setCalucularIvaService(CalcularIvaService calucularIvaService) {
		this.calcularIvaService = calucularIvaService;
	}
    
    
    
   }