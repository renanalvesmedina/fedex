package com.mercurio.lms.entrega.model.service;

import static com.mercurio.lms.util.zebra.enums.ZebraFontEnum.FONT_R;
import static com.mercurio.lms.util.zebra.enums.ZebraFontEnum.FONT_U;
import static com.mercurio.lms.util.zebra.enums.ZebraFontEnum.FONT_V;
import static com.mercurio.lms.util.zebra.enums.ZebraTextJustifEnum.CENTER;
import static com.mercurio.lms.util.zebra.enums.ZebraTextJustifEnum.LEFT;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.entrega.model.DoctoServicoIroad;
import com.mercurio.lms.entrega.model.dao.DoctoServicoIroadDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.expedicao.model.service.ImpressoraService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.zebra.ZebraPrinterUtil;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.DoctoServicoIroad"
 */
public class DoctoServicoIroadService extends CrudService<DoctoServicoIroad, Long> {

    private ImpressoraService impressoraService;
    private DomainValueService domainValueService;
    
	/**
	 * Recupera uma instância de <code>DoctoServicoIroad</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public DoctoServicoIroad findById(java.lang.Long id) {
        return (DoctoServicoIroad)super.findById(id);
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
    public java.io.Serializable store(DoctoServicoIroad bean) {
        return super.store(bean);
    }

    public void findImprimirEtiquetaIroad(Long idImpressora, String codigoBarras) throws Exception{
        Impressora impressora = impressoraService.findById(idImpressora);
        if(impressora == null || impressora.getNrIp() == null) {
            DomainValue tipoImpressora = domainValueService.findDomainValueByValue("DM_TIPO_IMPRESSORA", "E");
            throw new BusinessException("LMS-04578", new Object[] {tipoImpressora.getDescriptionAsString()});
        }
        
        final List listDocIroad = getDoctoServicoIroadDAO().findDoctoServicoIroadByNrVolumeEmbarque(codigoBarras);
        if (!CollectionUtils.isEmpty(listDocIroad)) {
            Map docIroad = (Map) listDocIroad.get(0);
            
            String nrSequenciaRota = (String) docIroad.get("nrSequenciaRota");
            String dsRotaIroad = (String) docIroad.get("dsRotaIroad");
            if(dsRotaIroad.length() > 10){
                dsRotaIroad = dsRotaIroad.substring(0, 10);
            }
            
            String data = "";
            String hora = "";
            DateTime dhInclusao = (DateTime) docIroad.get("dhInclusao");
            if(dhInclusao != null){
                data = JTDateTimeUtils.formatDateTimeToString(dhInclusao, JTDateTimeUtils.DATETIME_WITH_WITHOUT_TIME_PATTERN);
                hora = JTDateTimeUtils.formatDateTimeToString(dhInclusao, JTDateTimeUtils.DATETIME_WITH_WITHOUT_DATE_PATTERN);
            }
            
            String dsLocalizacaoMercadoria = null;
            if (docIroad.get("dsLocalizacaoMercadoria") != null && StringUtils.isNotBlank(((VarcharI18n)docIroad.get("dsLocalizacaoMercadoria")).toString())){
                dsLocalizacaoMercadoria = ((VarcharI18n)docIroad.get("dsLocalizacaoMercadoria")).getValue();
            }
            
            String filialLocalizacao = (String) docIroad.get("filialLocalizacao");

            ZebraPrinterUtil zebra = generateEtiquetaIroad(nrSequenciaRota, dsRotaIroad, data, hora, dsLocalizacaoMercadoria, filialLocalizacao);
            zebra.print(FormatUtils.convertNumberToIp(BigInteger.valueOf(impressora.getNrIp())), impressora.getNrPort()); //"172.016.006.011" 9100  
        }else{
            throw new BusinessException("LMS-09170");
        }
    }

    private ZebraPrinterUtil generateEtiquetaIroad(String nrSequenciaRota, String dsRotaIroad, String data, String hora, String dsLocalizacaoMercadoria, String filialLocalizacao) throws Exception {
        ZebraPrinterUtil zebra = new ZebraPrinterUtil("E");
        
        zebra.drawVerticalLine(150, 100, 400, 4);
        zebra.drawHorizontalLine(150, 100, 530, 4);
        zebra.drawVerticalLine(680, 100, 400, 4);
        
        zebra.drawText("Sequencia", FONT_R, 155, 115, 250, 2, LEFT);
        zebra.drawText(nrSequenciaRota, FONT_U, 155, 170, 250, 2, CENTER);
        
        zebra.drawVerticalLine(430, 100, 150, 4);
        
        zebra.drawText(data, FONT_R, 447, 145, 230, 2, CENTER);
        zebra.drawText(hora, FONT_R, 447, 180, 230, 2, CENTER);
        
        zebra.drawHorizontalLine(150, 245, 530, 4);

        zebra.drawText(dsLocalizacaoMercadoria +" "+ filialLocalizacao, FONT_R, 155, 255, 530, 1, CENTER);
        
        zebra.drawHorizontalLine(150, 285, 530, 4);
        
        zebra.drawText(dsRotaIroad, FONT_V, 150, 360, 530, 2, CENTER);
        
        zebra.drawHorizontalLine(150, 500, 530, 4);
        
        return zebra;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDoctoServicoIroadDAO(DoctoServicoIroadDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DoctoServicoIroadDAO getDoctoServicoIroadDAO() {
        return (DoctoServicoIroadDAO) getDao();
    }
    
    public void setImpressoraService(ImpressoraService impressoraService) {
        this.impressoraService = impressoraService;
    }

    public Long findDoctoServico(String cdFilial, String nrDoctoServico) {
		Long idDoctoServico = null;
		
		if (StringUtils.isEmpty(cdFilial) || StringUtils.isEmpty(nrDoctoServico)){
			throw new BusinessException("LMS-09167");
		}else{
			
			idDoctoServico = getDoctoServicoIroadDAO().findDoctoServicoUpload(cdFilial,nrDoctoServico);
			
		}
		
		return idDoctoServico;
	}
	
	public DoctoServicoIroad findByDoctoServico(DoctoServico doctoServico){
		return getDoctoServicoIroadDAO().findByDoctoServico(doctoServico);
	}

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }
}