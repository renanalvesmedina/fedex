package com.mercurio.lms.vol.model.service;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vol.model.VolRecusas;
import com.mercurio.lms.vol.model.dao.VolRecusasDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volRecusasService"
 */
public class VolRecusasService extends CrudService<VolRecusas, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private FilialService filialService;
	
	//chave para criptografia do idRecusa
	private final String CHAVE = "volRecusa";

	/**
	 * Recupera uma instância de <code>VolRecusas</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public VolRecusas findById(java.lang.Long id) {
        return (VolRecusas)super.findById(id);
    }
    
    /**
     * Recupera uma filial pela sigla sem usa a lookup
     * @param sigla da filial
     * @return Instância que possui a sigla informada
     */
	public Filial findLookupFilial(String sgFilial) {
		
		return filialService.findFilialPessoaBySgFilial(sgFilial); 
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
    public java.io.Serializable store(VolRecusas bean) {
        return super.store(bean);
    }
    
    
	public ResultSetPage findPaginatedRecusaHistorico(TypedFlatMap criteria){
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		return getVolRecusasDAO().findPaginatedRecusaHistorico(criteria, fd);
	}
    
	/**
	 * Recupera recusa
	 * @param criteria
	 * @return volRecusa
	 */
    public ResultSetPage findPaginatedRecusa(TypedFlatMap criteria){
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria);
    	return getVolRecusasDAO().findPaginatedRecusa(criteria, fd);
    }
    
    public List findRecusa(TypedFlatMap criteria){
    	return getVolRecusasDAO().findRecusa(criteria);
    }
    
    /**
     * criptografa o idRecusa
     * @param idRecusa
     * @return idRecusa criptografado
     */
    public String criptografaIdRecusa(String idRecusa){
		Cipher ecipher;
		
		  // 8-byte Salt
        byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
        };
        
        // Iteration count
        int iterationCount = 19;

	    try {	
	    	KeySpec keySpec = new PBEKeySpec(CHAVE.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            
            ecipher = Cipher.getInstance(key.getAlgorithm());
	    	
            //Prepara os parametros para o ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	 
            //Encode o idRecusa em bytes usando utf-8
            byte[] utf8 = idRecusa.getBytes("UTF8");
   			//criptografa 
   			byte[] enc = ecipher.doFinal(utf8);
  
       		return Base64Util.encode(enc);
	       
	    } catch (Exception e) {
        	log.error(e);
        }
      
        return null;
	}
    
    /**
     * decriptografa o idRecusa
     * @param idRecusa
     * @return idRecusa decriptografado
     */
    public String decriptografaIdRecusa(String idRecusa){
		Cipher dcipher;

		  // 8-byte Salt
		byte[] salt = {
          (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
          (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
		};
      
		// Iteration count
		int iterationCount = 19;
		
	    try {
	    	KeySpec keySpec = new PBEKeySpec(CHAVE.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
	    	
	        dcipher = Cipher.getInstance(key.getAlgorithm());
	        
	        //Prepara os parametros para o ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
	        
	        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	        //Decode base64 to get bytes
	        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(idRecusa.trim());
	        // Decrypt
	        byte[] utf8 = dcipher.doFinal(dec);

	        // Decode using utf-8
	        return new String(utf8, "UTF8");
	    } catch (javax.crypto.NoSuchPaddingException e) {
	    	log.error(e);
	    } catch (java.security.InvalidAlgorithmParameterException e) {
	    	log.error(e);
        } catch (java.security.InvalidKeyException e) {
        	log.error(e);
        } catch (java.security.spec.InvalidKeySpecException e) {
        	log.error(e);
        } catch (java.security.NoSuchAlgorithmException e) {
        	log.error(e);
	    } catch (javax.crypto.BadPaddingException e) {
	    	log.error(e);
	    } catch (IllegalBlockSizeException e) {
	    	log.error(e);
	    } catch (UnsupportedEncodingException e) {
	    	log.error(e);
	    } catch (java.io.IOException e) {
	    	log.error(e);
	    }
	     
        return null;
	}
    
    /**
     * retorna VolRecusa pelo id do ManifestoEntregaDocumento
     * @param idManifestoEntregaDocumento
     * @return VolRecusas
     */
    public VolRecusas findByIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento){
    	List result = getVolRecusasDAO().findByIdManifestoEntregaDocumento(idManifestoEntregaDocumento);
    	VolRecusas volRecusas = null;
    	if(!result.isEmpty()){
	    	Iterator iterator = result.iterator();
	    	volRecusas = (VolRecusas)iterator.next();
    	}
    	return volRecusas;
    }
    
    public Integer getRowCountRecusaHistorico(TypedFlatMap criteria){
    	return getVolRecusasDAO().getRowCountRecusaHistorico(criteria);
    }
    
    public Integer getRowCountRecusa(TypedFlatMap criteria){
    	return getVolRecusasDAO().getRowCountRecusa(criteria);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolRecusasDAO(VolRecusasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolRecusasDAO getVolRecusasDAO() {
        return (VolRecusasDAO) getDao();
    }


	public FilialService getFilialService() {
		return filialService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
   }
