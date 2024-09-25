package com.mercurio.lms.workflow.model.service;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.lms.workflow.model.GrupoAprovadores;
import com.mercurio.lms.workflow.model.PerfilAprovadores;
import com.mercurio.lms.workflow.model.dao.GrupoAprovadoresDAO;
import com.mercurio.lms.workflow.model.dto.GrupoAprovadoresDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.grupoAprovadoresService"
 */
public class GrupoAprovadoresService extends CrudService<GrupoAprovadores, Long> {

    private static final String ID_PERFIL = "idPerfil";
    private static final String ID_PERFIL_USUARIO = "idPerfilUsuario";
    private static final Long LONG_DEFAULT = 1L;

    public void setGrupoAprovadoresDAO(GrupoAprovadoresDAO dao) {
        setDao(dao);
    }

    private GrupoAprovadoresDAO getGrupoAprovadoresDAO() {
        return (GrupoAprovadoresDAO) getDao();
    }

    public List findAllGruposAprovadores(GrupoAprovadoresDTO filter){
        return getGrupoAprovadoresDAO().findAllGruposAprovadores(filter);
    }

    public List findByNomeUsuarios(GrupoAprovadoresDTO grupoAprovadoresDTO){
        return getGrupoAprovadoresDAO().findByNomeUsuarios(grupoAprovadoresDTO);
    }

    public List findByDescGrupoAprovadores(GrupoAprovadoresDTO filter){
        return getGrupoAprovadoresDAO().findByDescGrupoAprovadores(filter);
    }

    public List findByNomeUsuarioAndDescGrupoAprovadores(GrupoAprovadoresDTO filter){
        return getGrupoAprovadoresDAO().findByNomeUsuarioAndDescGrupoAprovadores(filter);
    }

    public List findByIdUsuarioPerfilUsuario(Long idUsuario){
        return getGrupoAprovadoresDAO().findByIdUsuarioPerfilUsuario(idUsuario);
    }

    public List findByIdUsuarioAndIdPerfilUsuario(Long idUsuario, Long idPerfil){
        return getGrupoAprovadoresDAO().findByIdUsuarioAndIdPerfilUsuario(idUsuario, idPerfil);
    }

    public boolean storeGrupoAprovadores(List persistence){
        return getGrupoAprovadoresDAO().storeGrupoAprovadores(persistence);
    }

    public List getListPersistence(List perfisBase, List perfisClone, String situacao, Long idClone){
        return preparePersistenceList(perfisBase, perfisClone, situacao, idClone);
    }

    public List findUsuarioEqualLogin(String usuario){
        return getGrupoAprovadoresDAO().findUsuarioEqualLogin(usuario);
    }

    public List findSuggestUsuario(String login){
        return getGrupoAprovadoresDAO().findSuggestUsuario(login);
    }

    /**
     * Prepara uma lista de objetos do tipo GrupoAprovadores tomando como base os vinculos do usuário
     * doador (usuário que será clonado) para o usuário receptor.
     *
     * @param perfisBase
     * @param perfisClone
     * @param situacao
     * @param idClone
     * @return List com o(s) vinculo(s) que serão agregados ao usuário de destino pronta para persistir
     */
    private List preparePersistenceList(List perfisBase, List perfisClone, String situacao, Long idClone) {
        if(!perfisClone.isEmpty()) {
            return compararListasDeVinculos(perfisBase, perfisClone, situacao, idClone);
        }
        return adicionarTodosVinculosUsuario(perfisBase, situacao, idClone);

    }

    /**
     * Compara as listas de vinculos para cada usuário removendo da lista do usuário doador
     * os vinculos que já estão presentes para o usuário de destino criando uma nova lista
     * de objetos GrupoAprovadores para retorno
     *
     * @param perfisBase
     * @param perfisClone
     * @param situacao
     * @param idClone
     * @return List com o(s) vinculo(s) que serão agregados ao usuário de destino
     */
    private List compararListasDeVinculos(List perfisBase, List perfisClone, String situacao, Long idClone){
        List listPersistence = new ArrayList();

        for (Object clone : perfisClone) {
            Map mpaClone = (Map) clone;
            for(int i = 0; i < perfisBase.size(); i++){
                Object base = perfisBase.get(i);
                Map mapBase = (Map) base;
                if(mapBase.get(ID_PERFIL).equals(mpaClone.get(ID_PERFIL))){
                    perfisBase.remove(base);
                    break;
                }
            }
        }

        for (Object base : perfisBase) {
            GrupoAprovadores grupo = getValuesInObject(base, idClone, situacao);
            listPersistence.add(grupo);
        }

        return listPersistence;
    }

    /**
     * No caso do usuário de destino não possuir vinculos é criada uma nova lista de objetos
     * GrupoAprovadores com todos os vinculos do usuário doador
     *
     * @param perfisBase
     * @param situacao
     * @param idClone
     * @return List com o(s) vinculo(s) que serão agregados ao usuário de destino
     */
    private List adicionarTodosVinculosUsuario(List perfisBase, String situacao, Long idClone){
        List listPersistence = new ArrayList();
        for (Object base : perfisBase) {
            GrupoAprovadores grupo = getValuesInObject(base, idClone, situacao);
            if (!listPersistence.contains(grupo)) {
                listPersistence.add(grupo);
            }
        }
        return listPersistence;
    }

    private GrupoAprovadores getValuesInObject(Object base, Long idClone, String situacao){
        Map mapBase = (Map) base;

        PerfilAprovadores perfil = new PerfilAprovadores();
        UsuarioADSM usuario = new UsuarioADSM();

        perfil.setIdPerfil((Long) mapBase.get(ID_PERFIL));

        usuario.setIdUsuario(idClone);

        GrupoAprovadores grupo = new GrupoAprovadores();
        grupo.setIdPerfilUsuario(((Long) mapBase.get(ID_PERFIL_USUARIO)).longValue());
        grupo.setUsuario(usuario);
        grupo.setPerfil(perfil);
        grupo.setTpSituacao(situacao);

        return grupo;
    }

    public List getPersistence(Long idUsuario, Long idPerfil, String situacao){
        PerfilAprovadores perfil = new PerfilAprovadores();
        UsuarioADSM usuario = new UsuarioADSM();

        perfil.setIdPerfil(idPerfil);

        usuario.setIdUsuario(idUsuario);

        GrupoAprovadores grupo = new GrupoAprovadores();
        grupo.setIdPerfilUsuario(LONG_DEFAULT);
        grupo.setUsuario(usuario);
        grupo.setPerfil(perfil);
        grupo.setTpSituacao(situacao);

        List persistence = new ArrayList();
        persistence.add(grupo);

        return persistence;
    }

    public List<Map<String, String>> getValorDominio(){
        return getGrupoAprovadoresDAO().getValorDominio();
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
        for(Iterator i = ids.iterator(); i.hasNext();){
            getGrupoAprovadoresDAO().executeRemoveById((Long)i.next());
        }
    }

}

