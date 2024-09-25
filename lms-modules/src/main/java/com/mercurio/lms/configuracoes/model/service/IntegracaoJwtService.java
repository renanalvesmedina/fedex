package com.mercurio.lms.configuracoes.model.service;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.core.web.ServletContextHolder;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContext;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@ServiceSecurity
public class IntegracaoJwtService {

    public static final String REST_JWT_SERVICE_GERAR = "tnt/gerarTokenLMS";
    public static final String REST_JWT_SERVICE_VALIDAR = "tnt/validar";
    public static final String REST_JWT_SERVICE_REFRESH = "tnt/revalidar";
    public static final String REST_JWT_SERVICE_USER = "/tnt/getuser";
    public static final String REST_JWT_SERVICE_BRANCH_ID = "/tnt/getbranch";
    public static final String REST_JWT_SERVICE_COMPANY_ID = "/tnt/getcompany";
    public static final String ERROR = "error";
    private static final Logger LOGGER = LogManager.getLogger(IntegracaoJwtService.class);

    private ConfiguracoesFacade configuracoesFacade;
    private UsuarioService usuarioService;
    private EmpresaService empresaService;
    private FilialService filialService;
    private HistoricoFilialService historicoFilialService;

    private WebTarget getJwtServiceHost(String path){
        ApplicationContext applicationContext = getApplicationContext();
        configuracoesFacade = (ConfiguracoesFacade) applicationContext.getBean("lms.configuracoesFacade");
        String jwtServiceHost = (String) configuracoesFacade.getValorParametro("JWT_SERVICE_HOST");
        Client client = ClientBuilder.newClient();
        return client.target(jwtServiceHost).path(path);
    }

    private ApplicationContext getApplicationContext(){

        final ServletContext servletContext = ServletContextHolder.getServletContext();
        ApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        webApplicationContext = (ApplicationContext) webApplicationContext.getBean("com.mercurio.adsm.beanfactory");

        return webApplicationContext;
    }

    @Transactional
    public Map<String, Object> montarUsuarioAutenticado(AutenticacaoDMN autenticacaoDMN){
        Map<String, Object> dados = new HashMap<>();
        if(!autenticacaoDMN.isAutenticado()){
            dados.put(ERROR, autenticacaoDMN.getMensagem());
            return dados;
        }
        autenticacaoDMN.setTpFrontend("S");
        Usuario usuario = findUsuario(autenticacaoDMN);

        if (usuario == null) {
            dados.put(ERROR, "ADSM_INVALID_USER_EXCEPTION_KEY");
            return dados;
        }
        Empresa empresa = empresaService.findEmpresaPadraoByUsuario(usuario);
        if (empresa == null) {
            dados.put(ERROR, "LMS_EMPRESA_PADRAO_INVALIDA");
            return dados;
        }
        Filial filial = filialService.findFilialPadraoByUsuarioEmpresa(usuario, empresa);
        if (filial == null) {
            dados.put(ERROR, "LMS_FILIAL_PADRAO_INVALIDA");
            return dados;
        }
        return montarDados(autenticacaoDMN, usuario, filial, empresa);
    }

    public Map<String, Object> montarDados(AutenticacaoDMN autenticacaoDMN, Usuario usuario, Filial filial, Empresa empresa){
        Map<String, Object> dados = new HashMap<>();
        LocalDate now = new LocalDate();
        LocalDate dtImplantacaoLMS = filial.getDtImplantacaoLMS().toLocalDate();
        try {
            dados.put("nmUsuario", usuario.getNmUsuario());
            dados.put("login", usuario.getLogin());
            dados.put("filial", filial.getPessoa().getNmFantasia());
            dados.put("empresa", empresa.getPessoa().getNmPessoa());
            dados.put("idFilial", filial.getIdFilial());
            dados.put("idEmpresa", empresa.getIdEmpresa());
            dados.put("sgFilial", filial.getSgFilial());
            dados.put("blSorter", filial.getBlSorter());
            dados.put("token", gerarToken(autenticacaoDMN, filial, empresa));
            dados.put("dsEmail", usuario.getDsEmail());
            dados.put("lmsImplantadoFilial", dtImplantacaoLMS.isBefore(now) || now.equals(dtImplantacaoLMS));
            return dados;
        } catch (Exception e) {
            LOGGER.error(e);
            return new HashMap<>();
        }
    }

    public String gerarToken(AutenticacaoDMN autenticacaoDMN, Filial filial, Empresa empresa)throws Exception{
        Map<String, Object> dados = new HashMap<>();
        dados.put("user", autenticacaoDMN);
        dados.put("branchId", filial.getIdFilial());
        dados.put("companyId", empresa.getIdEmpresa());
        ObjectMapper mapper = new ObjectMapper();
        WebTarget target = getJwtServiceHost(REST_JWT_SERVICE_GERAR);
        String token = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(mapper.writeValueAsString(dados), MediaType.APPLICATION_JSON), String.class);
        if(token == null){
            throw new NotAuthorizedException("Token inválido");
        }
        return token;
    }

    public boolean validarToken(String token){
        try {
            WebTarget target = getJwtServiceHost(REST_JWT_SERVICE_VALIDAR);
            if (target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(token, MediaType.APPLICATION_JSON), String.class) != null) {
                return true;
            }
        }catch (NullPointerException e){
            LOGGER.error(e);
            return false;
        }
        return false;
    }

    public String refreshToken(String token){
        synchronized (this) {
            if (token == null) {
                return null;
            }
            try {
                WebTarget target = getJwtServiceHost(REST_JWT_SERVICE_REFRESH);
                return target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(token, MediaType.APPLICATION_JSON), String.class);
            }catch (NullPointerException e){
                LOGGER.error(e);
                return null;
            }
        }
    }

    public AutenticacaoDMN findAutenticacaoDMN(String token){
        WebTarget target = getJwtServiceHost(REST_JWT_SERVICE_USER);
        return target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(token, MediaType.APPLICATION_JSON), AutenticacaoDMN.class);
    }

    public Long getIdFilialByToken(String token){
        WebTarget target = getJwtServiceHost(REST_JWT_SERVICE_BRANCH_ID);
        return Long.parseLong(target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(token, MediaType.APPLICATION_JSON), String.class));
    }

    public Long getIdEmpresaByToken(String token){
        WebTarget target = getJwtServiceHost(REST_JWT_SERVICE_COMPANY_ID);
        return  Long.parseLong(target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(token, MediaType.APPLICATION_JSON), String.class));
    }

    @Transactional
    protected Usuario findUsuario(AutenticacaoDMN autenticacaoDMN){
        if(autenticacaoDMN.getLoginOkta() != null && autenticacaoDMN.getLoginOkta()) {
            return usuarioService.findUsuarioByLoginIdFedex(autenticacaoDMN.getLoginIdFedex());
        }else{
           return usuarioService.findUsuarioByLogin(autenticacaoDMN.getLogin());
        }
    }

    @Transactional
    public Empresa getEmpresaSessao(Long id) {
        return empresaService.findEmpresaLogadoById(id);
    }

    @Transactional
    public Filial getFilialSessao(Long id) {
        return filialService.findFilialLogadoById(id);
    }

    @Transactional
    public HistoricoFilial getUltimoHistoricoFilialSessao(Long idFilial) {
        return historicoFilialService.findUltimoHistoricoFilial(idFilial);
    }

    @Transactional
    public Usuario getUsuarioSessao(AutenticacaoDMN autenticacaoDMN) {
        return findUsuario(autenticacaoDMN);
    }

    @Transactional
    public Usuario getUsuarioSessaoByToken(String token) {
        return findUsuario(findAutenticacaoDMN(token));
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setEmpresaService(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
        this.historicoFilialService = historicoFilialService;
    }
}
