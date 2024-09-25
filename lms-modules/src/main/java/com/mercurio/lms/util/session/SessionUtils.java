package com.mercurio.lms.util.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe que possui m�todos utilit�rios para manipula��o de dados da sess�o 
 * @author Rodrigo Antunes, Christian Brunkow
 *
 */
public class SessionUtils {

    /**
     * Verifica se a filial informada por parametro � uma das filiais que 
     * o usuario logado tenha acesso.
     * 
     * @param filial a ser verificada
     * @return retorna 'true' caso o usu�rio tenha permiss�o de acesso
     */
    public static boolean isFilialAllowedByUsuario(Filial filial) {
        if (filial!=null && filial.getIdFilial()!=null) {
	        List filiais = getFiliaisUsuarioLogado();
	        if (filiais!=null && !filiais.isEmpty()) {
	            Iterator iter = filiais.iterator();
	            while (iter.hasNext()) {
	                Filial filialAcessoOK = (Filial) iter.next();
	                if (filialAcessoOK.getIdFilial().longValue() == filial.getIdFilial().longValue()) {
	                    return true;
	                }
	            }
	        }
        }
        return false;
    }

    /**
	 * Busca o ultimo historico da filial associada com a sess�o do usu�rio.
	 * 
	 * @return historico filial
	 */
	public static HistoricoFilial getUltimoHistoricoFilialSessao() {
		if (SessionContext.get(SessionKey.ULT_HIST_FILIAL_KEY)!=null) {
		return (HistoricoFilial) SessionContext.get(SessionKey.ULT_HIST_FILIAL_KEY);
		}else{
			return null;
	}

	}

	/**
	 * Busca o penultimo historico da filial associada com a sess�o do usu�rio.
	 * 
	 * @return historico filial
	 */
	public static HistoricoFilial getPenultimoHistoricoFilialSessao() {
		if (SessionContext.get(SessionKey.PEN_HIST_FILIAL_KEY)!=null) {
		return (HistoricoFilial) SessionContext.get(SessionKey.PEN_HIST_FILIAL_KEY);
		}else{
			return null;
	}

	}

	/**
	 * Verifica se Filial da Sess�o � Matriz
	 * 
	 * @return
	 */
	public static boolean isFilialSessaoMatriz() {
		if (SessionContext.get(SessionKey.FILIAL_MATRIZ_KEY)!=null) {
		return ((Boolean)SessionContext.get(SessionKey.FILIAL_MATRIZ_KEY)).booleanValue();
	}
		return false;
	}

    /**
     * M�todo que busca as filiais do usu�rio logado.
     * @return list com as filiais que o usuario tem acesso.
     */
    public static List getFiliaisUsuarioLogado() {
    	List filiais=null;
    	if (SessionContext.get(SessionKey.FILIAIS_ACESSO_KEY)!=null) {
    		filiais = (List) SessionContext.get(SessionKey.FILIAIS_ACESSO_KEY);
		}
        return filiais;
    }
   
    /**
     * M�todo que busca os ids das filiais do usu�rio logado,
     * @return Lista com os ids das filiais que o usu�rio tem acesso.
     */
    public static List<Long> getIdsFiliaisUsuarioLogado() {
    	List<Filial> filiais = getFiliaisUsuarioLogado();
    	List<Long> ids = new ArrayList<>();
    	for(Filial filial : filiais) {
    		ids.add(filial.getIdFilial());
    	}
    	return ids;
    }
   
    /**
     * M�todo que busca as regionais do usu�rio logado.
     * @return list com as regionais que o usuario tem acesso.
     */
    public static List getRegionaisUsuarioLogado() {
    	if (SessionContext.get(SessionKey.REGIONAIS_ACESSO_KEY)!=null) {
        	return (List) SessionContext.get(SessionKey.REGIONAIS_ACESSO_KEY);
		}else{
			return null;
    	}

    }

    /**
     * M�todo que busca as regionais das filiais do usu�rio logado.
     * @return list com as regionais das filiais que o usuario tem acesso.
     */
    public static List getRegionaisFiliaisUsuarioLogado() {
    	if (SessionContext.get(SessionKey.REGIONAIS_FILIAIS_ACESSO_KEY)!=null) {
        return (List) SessionContext.get(SessionKey.REGIONAIS_FILIAIS_ACESSO_KEY);
		}else{
			return null;
    }

    }

    /**
     * M�todo que busca os clientes do usu�rio logado.
     * @return list com os clientes que o usuario tem acesso.
     */
    public static List getClientesUsuarioLogado() {
    	if (SessionContext.get(SessionKey.CLIENTES_ACESSO_KEY)!=null) {
        return (List) SessionContext.get(SessionKey.CLIENTES_ACESSO_KEY);
		}else{
			return null;
    }
    
    }
    
    /**
     * M�todo que busca as empresas do usu�rio logado.
     * @return list com as empresas que o usuario tem acesso.
     */
    public static List getEmpresasUsuarioLogado() {
    	if (SessionContext.get(SessionKey.EMPRESAS_ACESSO_KEY)!=null) {
        return (List) SessionContext.get(SessionKey.EMPRESAS_ACESSO_KEY);
		}else{
			return null;
    }
    
    }
    
    /**
     * M�todo que busca o usu�rio da sess�o.
     * @return usuario
     */
    public static Usuario getUsuarioLogado() {
    	try {
			Usuario usuario = null;
			if (SessionContext.getUser() != null) {
				usuario = SessionContext.getUser();
			}
			return usuario;
		}catch (Exception e){
    		return null;
		}
    }
    
    /**
     * M�todo que busca a filial da sess�o.
     * @return filial
     */
    public static Filial getFilialSessao() {
    	Filial filial=null;
    	if (SessionContext.get(SessionKey.FILIAL_KEY)!=null) {
    		filial = (Filial) SessionContext.get(SessionKey.FILIAL_KEY);
		}
        return filial;
    }

    /**
     * M�todo que busca o cliente da sess�o.
     * @return cliente
     */
    public static Cliente getClienteSessao() {
    	Cliente cliente=null;
    	if (SessionContext.get(SessionKey.CLIENTE_KEY)!=null) {
			cliente = (Cliente) SessionContext.get(SessionKey.CLIENTE_KEY);
    }
        return cliente;
    }
    
    /**
     * M�todo que busca a empresa da sess�o.
     * @return empresa
     */
    public static Empresa getEmpresaSessao() {
    	Empresa empresa=null;
    	if (SessionContext.get(SessionKey.EMPRESA_KEY)!=null) {
    		empresa = SessionContext.get(SessionKey.EMPRESA_KEY);
		}
        return empresa;
    }     

    /**
     * M�todo que busca o pa�s da sess�o.
     * @return pa�s
     */
    public static Pais getPaisSessao() {
    	Pais pais=null;
    	if (SessionContext.get(SessionKey.PAIS_KEY)!=null) {
    		pais = (Pais) SessionContext.get(SessionKey.PAIS_KEY);
		}
        return pais;
    }    
   
    /**
     * M�todo que busca a moeda da sess�o.
     * @return moeda
     */
    public static Moeda getMoedaSessao() {
    	Moeda moeda=null;
    	if (SessionContext.get(SessionKey.MOEDA_KEY)!=null) {
    		moeda = (Moeda) SessionContext.get(SessionKey.MOEDA_KEY);
		}
        return moeda;
    }     

    /**
     * M�todo que busca o setor da sess�o.
     * @return setor
     */
    public static Setor getSetor() {
    	Setor setor=null;
    	if (SessionContext.get(SessionKey.SETOR_KEY)!=null) {
    		setor = (Setor) SessionContext.get(SessionKey.SETOR_KEY);
		}
    	return setor;
    }
 
    /**
     * @see SessionKey#INTEGRATION_RUNNING_KEY
     */
    public static boolean isIntegrationRunning() {
    	HttpServletRequest httpServletRequest = HttpServletRequestHolder.getHttpServletRequest();
    	if (httpServletRequest != null) {
    		return Boolean.TRUE.equals(SessionContext.get(SessionKey.INTEGRATION_RUNNING_KEY));
    	}
		return false;
    }

    /**
     * @see SessionKey#INTEGRATION_TRIGGER_LOG_DISABLED_KEY
     */
    public static boolean isIntegrationTriggerLogDisabled() {
    	HttpServletRequest httpServletRequest = HttpServletRequestHolder.getHttpServletRequest();
    	if (httpServletRequest != null) {
    		return Boolean.TRUE.equals(SessionContext.get(SessionKey.INTEGRATION_TRIGGER_LOG_DISABLED_KEY));
    	}
    	return false;
    }
    
    /**
     * @see SessionKey#BATCH_JOB_RUNNING_KEY
     */
    public static boolean isBatchJobRunning() {
    	HttpServletRequest httpServletRequest = HttpServletRequestHolder.getHttpServletRequest();
    	if (httpServletRequest != null) {
    		return Boolean.TRUE.equals(SessionContext.get(SessionKey.BATCH_JOB_RUNNING_KEY));
    	}
    	return false;
    }
}
