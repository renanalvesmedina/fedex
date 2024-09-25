package com.mercurio.lms.util.model.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author leandroc
 * @spring.bean id="lms.utils.assynchronousProcessService"
 */
public class AssynchronousProcessService {

	private static Map<Double, Object> tokenMap = new HashMap<Double, Object>();

	/**
	 * Insere um objeto no mapa.
	 * 
	 * @param valorRandomico (número randômico) da posição do objeto que foi inserido.
	 * @param object objeto a ser inserido.
	 * @return chave (número randômico) da posição do objeto que foi inserido.
	 */
    public Double generateToken(Double valorRandomico, Object object) {
    	if (!tokenMap.containsKey(valorRandomico)) {
    		tokenMap.put(valorRandomico, object);
    	}
    	else {
    		return generateToken(object);
    	}
    	return valorRandomico;
	}

	
    /**
	 * Insere um objeto no mapa.
	 * 
	 * @param object objeto a ser inserido.
	 * @return chave (número randômico) da posição do objeto que foi inserido.
	 */
    public Double generateToken(Object object) {
    	double valorRandomico = Math.random();
    	return generateToken(valorRandomico, object);
	}

    /**
     * Retorna um objeto referente ao número randômico recebido.
     * 
     * @param valorToken
     * @return
     */
    public Object getToken(Double valorToken) {
    	return tokenMap.get(valorToken);
	}

    /**
     * Atualiza o objeto recebido por parâmetro no mapa.
     * 
     * @param valorToken
     * @param object
     */
    public void updateToken(Double valorToken, Object object) {
    	tokenMap.put(valorToken, object);
	}
    
    /**
     * Remove o token do mapa.
     * 
     * @param valorToken
     */
    public void removeToken(Double valorToken) {
    	tokenMap.remove(valorToken);
	}
}