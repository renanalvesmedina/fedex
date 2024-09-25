package com.mercurio.lms.municipios.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.dao.HistoricoFilialDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.historicoFilialService"
 */
public class HistoricoFilialService extends CrudService<HistoricoFilial, Long> {


	/**
	 * Recupera uma inst�ncia de <code>HistoricoFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public HistoricoFilial findById(Long id) {
        return (HistoricoFilial)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(HistoricoFilial bean) {
        return super.store(bean);
    }
  
    public HistoricoFilial findUltimoHistoricoFilial(Long idFilial) {
    	return getHistoricoFilialDAO().findUltimoHistoricoFilial(idFilial);
    }
    public HistoricoFilial getPenultimoHistoricoFilial(Long idFilial) {
    	return getHistoricoFilialDAO().getPenultimoHistoricoFilial(idFilial);
    }
    
    /**
     * Consulta o hit�rico vigente da filial informada por par�metro.
     * @param idFilial
     * @return bean entidade HistoricoFilial
     */
    public HistoricoFilial findHistoricoFilialVigente(Long idFilial) {
    	return getHistoricoFilialDAO().findHistoricoFilialVigente(idFilial);
    }
    

	/**
     * Verifica o tipo vigente da filial em determinada data
     * 
     * @param idFilial
     * @param data
     * @param tpFilial
     * @return true se o tipo indicado da filial era vigente na data informada
     */
	public Boolean findTpFilialVigenteData(Long idFilial, YearMonthDay data, String tpFilial) {
		return getHistoricoFilialDAO().findTpFilialVigenteData(idFilial, data, tpFilial);

	}

	/**
	 * recebe duas datas de vigencia: inicial e final e verifica se existem historicos filiais que abrangem a vigencia
	 * exemplo
	 * historico 1 : 01/01/2010 - 31/01/2010
	 * historico 1 : 01/02/2010 - 20/02/2010
	 * 
	 *   parametro 15/01/2010 - 15/02/2010 � v�lido
	 *   parametro 15/01/2010 - 21/02/2010 n�o � valido
	 * 
	 * @author DanielT
	 * @param idFilial
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @throws caso nao estiverem nesse intervalo gera uma exce��o
	 */
	public boolean verificaExistenciaHistoricoFilial(Long idFilial,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		// TODO Auto-generated method stub
		return getHistoricoFilialDAO().verificaExistenciaHistoricoFilial(idFilial,dtVigenciaInicial,dtVigenciaFinal);
	}
	
    public boolean verificaVigenciasEmHistoricoFilial(Long idFilial,YearMonthDay dtVigenciaInicial,YearMonthDay dtVigenciaFinal) {
    	return getHistoricoFilialDAO().verificaVigenciasEmHistoricoFilial(idFilial,dtVigenciaInicial,dtVigenciaFinal);
    }
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setHistoricoFilialDAO(HistoricoFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private HistoricoFilialDAO getHistoricoFilialDAO() {
        return (HistoricoFilialDAO) getDao();
    }

    public Filial findFilialMatriz(Long idEmpresa) {
    	return getHistoricoFilialDAO().findFilialMatriz(idEmpresa);
    }

    /**
     * Valida se a filial (filial do usu�rio) � MTZ (matriz)
     * @param idFilial Identificador da filial do usu�rio
     * @return <code>true</code> se a filial do usu�rio for MTZ, </code>false</code> caso contr�rio
     */
    public boolean validateFilialUsuarioMatriz(Long idFilial){
    	return getHistoricoFilialDAO().validateFilialUsuarioMatriz(idFilial);
    }
    
    /**
     * Valida se a filial passada por par�metro � uma sucursal.
     * 
     * @return
     */
    public Boolean validateFilalIsSucursal(Long idFilial){
    	Boolean isSucursal = false;
    	
		if ("SU".equalsIgnoreCase(
				this.findHistoricoFilialVigente(
						idFilial).getTpFilial().getValue())) {
			isSucursal = true;
		}
		
		return isSucursal;
    }
    /**
     * @author Jos� Rodrigo Moraes
     * @since 12/07/2006
     * 
     * Valida se a filial informada � do tipo informado.
     * Verifica na tabela HISTORICO_FILIAL se a filial em quest�o �, por exemplo, Matriz, sucursal, etc.... 
     * 
     * @param idFilial Identificador da filial
     * @param tpFilial Tipo da filial - DM_TIPO_FILIAL
     * @return <code>true</code> Se a filial informada for do tipo informado e <code>false</code> caso contr�rio
     * 
     */
    public boolean validateFilialByTpFilial(Long idFilial, String tpFilial){
    	return getHistoricoFilialDAO().validateFilialByTpFilial(idFilial, tpFilial);
    }

	public List<HistoricoFilial> findHistoricosFilialByEmpresa(Long idEmpresa, String tpEmpresa) {
		return getHistoricoFilialDAO().findHistoricosFilialByEmpresa(idEmpresa, tpEmpresa);
	}

}