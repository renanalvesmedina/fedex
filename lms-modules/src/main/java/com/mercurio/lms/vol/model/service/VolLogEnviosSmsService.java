package com.mercurio.lms.vol.model.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolLogEnviosSms;
import com.mercurio.lms.vol.model.VolOperadorasTelefonia;
import com.mercurio.lms.vol.model.dao.VolLogEnviosSmsDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volLogEnviosSmsService"
 */
@Deprecated
public class VolLogEnviosSmsService extends CrudService<VolLogEnviosSms, Long> {
     private VolOperadorasTelefoniaService volOperadorasTelefoniaService;

	/**
	 * Recupera uma instância de <code>VolLogEnviosSms</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public VolLogEnviosSms findById(java.lang.Long id) {
        return (VolLogEnviosSms)super.findById(id);
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
    public java.io.Serializable store(VolLogEnviosSms bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolLogEnviosSmsDAO(VolLogEnviosSmsDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolLogEnviosSmsDAO getVolLogEnviosSmsDAO() {
        return (VolLogEnviosSmsDAO) getDao();
    }
    
    public String executeEnvioSmsClaro(final String sDsUrl,final String dsNumero,final String sTexto,final String sUsuario,final String sPwd) {
    
    	try {
           String sUrlNew = sDsUrl.replace("#USER#",sUsuario).replace("#PWD#",sPwd).replace("#TEXT#",sTexto).replace("#DESTINO#",dsNumero);
    	   String sUrl = sUrlNew.substring(0,sUrlNew.indexOf("?"));
    	   String sParameters = sUrlNew.substring(sUrlNew.indexOf("?")+1);
    	
           HttpURLConnection conn = null;     
         
     
   		   URL url = new URL(sUrl);
   		   conn = (HttpURLConnection) url.openConnection();
   		   conn.setRequestMethod("POST");
   		   conn.setUseCaches(false);
   		   conn.setDoInput(true);
   		   conn.setDoOutput(true);
   		   DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
   		   printout.writeBytes(sParameters);
   		   printout.flush();
   		   printout.close();

   		   /* pega resposta da operadora */
   		   BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
   		   String input;
   		   StringBuffer response = new StringBuffer(256);

   		   while ((input = in.readLine()) != null) {
   			   response.append(input + "\r");
   		   }
   		   return response.toString();
   	   } catch (Exception e) {
   		throw new InfrastructureException(e);
   	}
    }
  
    public String executeEnvioSms(final Long idOperadora, final String dsNumero ,final String sTexto) {
    	VolOperadorasTelefonia volOperadorasTelefonia = getVolOperadorasTelefoniaService().findById(idOperadora);
    	
    	String sUrl = volOperadorasTelefonia.getDsUrlSms();
    	String response = null;
        /* Implementação da funcionalidades para Claro */
        if (sUrl.indexOf("claro.com.br")>-1) {
        	response = executeEnvioSmsClaro(sUrl,dsNumero,sTexto,volOperadorasTelefonia.getDsUsuario(),volOperadorasTelefonia.getDsSenha());
        }
       return response;
    }
    /**
     * Atualiza chamados pendentes do celular
     * @param idEquipamento
     */
    public void updatePendentes(Long idEquipamento){
    	if (idEquipamento==null)
    		return;
    	getVolLogEnviosSmsDAO().updatePendentes(idEquipamento);
    }
	public VolOperadorasTelefoniaService getVolOperadorasTelefoniaService() {
		return volOperadorasTelefoniaService;
	}

	public void setVolOperadorasTelefoniaService(
			VolOperadorasTelefoniaService volOperadorasTelefoniaService) {
		this.volOperadorasTelefoniaService = volOperadorasTelefoniaService;
	}

	public ResultSetPage findPaginatedMeioTransporte(TypedFlatMap criteria) {
		ResultSetPage rsp = getVolLogEnviosSmsDAO().findPaginatedMeioTransporte(criteria,FindDefinition.createFindDefinition(criteria)); 
		return rsp;
	}
	               
	public Integer getRowCountMeioTransporte(TypedFlatMap criteria) {
		return getVolLogEnviosSmsDAO().getRowCountMeioTransporte(criteria);
	}
	
	public List findMeioTransporte(TypedFlatMap criteria) {
		return getVolLogEnviosSmsDAO().findMeioTransporte(criteria); 
	}
	
	public List findByIdEquipamento(java.lang.Long id) {
		return getVolLogEnviosSmsDAO().findByIdEquipamento(id);
	}
	
	public ResultSetPage findPaginatedHistorico(TypedFlatMap criteria) {
		ResultSetPage rsp = getVolLogEnviosSmsDAO().findPaginatedHistorico(criteria,FindDefinition.createFindDefinition(criteria)); 
		return rsp;
	}
	               
	public Integer getRowCountHistorico(TypedFlatMap criteria) {
		return getVolLogEnviosSmsDAO().getRowCountHistorico(criteria);
	}
	
	public DateTime findDhEnvioByMeioTransporte(Long idMeioTransporte) {
		return getVolLogEnviosSmsDAO().findDhEnvioByMeioTransporte(idMeioTransporte);
	}
	public List findDhEnvioColetaByMeioTransporte(Long idMeioTransporte) {
		return getVolLogEnviosSmsDAO().findDhEnvioColetaByMeioTransporte(idMeioTransporte);
	}
}