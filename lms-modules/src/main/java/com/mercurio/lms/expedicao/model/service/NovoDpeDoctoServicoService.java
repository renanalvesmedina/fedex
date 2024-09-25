package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NovoDpeDoctoServico;
import com.mercurio.lms.expedicao.model.dao.NovoDpeDoctoServicoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.expedicao.novoDpeDoctoServicoService"
 */
public class NovoDpeDoctoServicoService extends CrudService<NovoDpeDoctoServico, Long> {

    public NovoDpeDoctoServico findByIdDoctoServico(Long idDoctoServico) {
        return getNovoDpeDoctoServicoDAO().findByIdDoctoServico(idDoctoServico);
    }
    
    public void executeAtivaBlAtendido(Long idDoctoServico, DateTime dhEvento) {
        NovoDpeDoctoServico entity = getNovoDpeDoctoServicoDAO().findByIdDoctoServico(idDoctoServico);
        entity.setBlAtendido(Boolean.TRUE);
        entity.setNovoDtPrevEntrega(new YearMonthDay(dhEvento));
        store(entity);
    }

    @Override
    public NovoDpeDoctoServico findById(java.lang.Long id) {
        return (NovoDpeDoctoServico) super.findById(id);
    }

    @Override
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    @ParametrizedAttribute(type = java.lang.Long.class)
    @Override
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    @Override
    public java.io.Serializable store(NovoDpeDoctoServico bean) {
        return super.store(bean);
    }

    public void setNovoDpeDoctoServicoDAO(NovoDpeDoctoServicoDAO dao) {
        setDao(dao);
    }

    private NovoDpeDoctoServicoDAO getNovoDpeDoctoServicoDAO() {
        return (NovoDpeDoctoServicoDAO) getDao();
    }

}
