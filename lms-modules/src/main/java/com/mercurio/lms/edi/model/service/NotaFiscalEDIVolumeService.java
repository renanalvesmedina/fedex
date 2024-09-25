package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiVolumeDAO;
import com.mercurio.lms.util.BigDecimalUtils;

import br.com.tntbrasil.integracao.domains.expedicao.VolumeDMN;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.notaFiscalEDIVolumeService"
 */

public class NotaFiscalEDIVolumeService extends CrudService<NotaFiscalEdiVolume, Long> {
	@Override
	public Serializable store(NotaFiscalEdiVolume bean) {
		if(bean.getIdNotaFiscalEdiVolume() == null){
			bean.setIdNotaFiscalEdiVolume(getNotaFiscalEdiVolumeDAO().findSequence());
		}
		return super.store(bean);
	}
	
	@Override
	public void storeAll(List<NotaFiscalEdiVolume> list) {
		for (NotaFiscalEdiVolume bean : list) {
			if (bean.getIdNotaFiscalEdiVolume() == null) {
				bean.setIdNotaFiscalEdiVolume(getNotaFiscalEdiVolumeDAO().findSequence());
			}
		}
		
		super.storeAll(list);
	}



	private NotaFiscalEdiVolumeDAO getNotaFiscalEdiVolumeDAO() {
        return (NotaFiscalEdiVolumeDAO) getDao();
    }
    
    public void setNotaFiscalEdiVolumeDAO(NotaFiscalEdiVolumeDAO dao) {
        setDao(dao);
    }
    
    public void removeByIdNotaFiscalEdi(Long id) {
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        getNotaFiscalEdiVolumeDAO().removeByIdNotaFiscalEdi(ids);
    }
    
    public List<NotaFiscalEdiVolume> findNotaFiscalEdiByUltimoIdImportado(Long idNotaFiscalEdi) {
    	return getNotaFiscalEdiVolumeDAO().findNotaFiscalEdiByUltimoIdImportado(idNotaFiscalEdi);	
    }
    
    public BigDecimal countNotaFiscalVolumeByNotaFiscalEDI(Long idNotaFiscalEdi) {    	
		return BigDecimalUtils.getBigDecimal(getNotaFiscalEdiVolumeDAO().countNotaFiscalVolumeByNotaFiscalEDI(idNotaFiscalEdi));
	}
    
    public void generateVolumes(List<VolumeDMN> volumes, NotaFiscalEdi notaFiscalEdi) {
    	
    	List<NotaFiscalEdiVolume> volumesEdi = new ArrayList<>();
    	for (VolumeDMN volumeDMN : volumes) {
    		NotaFiscalEdiVolume notaFiscalEdiVolume = new NotaFiscalEdiVolume();
    		notaFiscalEdiVolume.setNotaFiscalEdi(notaFiscalEdi);
    		notaFiscalEdiVolume.setCodigoVolume(volumeDMN.getBarcode());
    		notaFiscalEdiVolume.setCdBarraPostoAvancado(volumeDMN.getCdBarraPostoAvancado());
    		volumesEdi.add(notaFiscalEdiVolume);
		}
    	this.storeAll(volumesEdi);
    }
    
   
}
