package com.mercurio.lms.services;

import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = LogManager.getLogger(ConstraintViolationExceptionMapper.class);
    static final String MESSAGE_KEY = "LMS-00085";

    @Override
    public Response toResponse(final ConstraintViolationException e) {

        HashMap<String, Object> map = new HashMap<>();

        String message = RecursoMensagemCache.getMessage(MESSAGE_KEY, LocaleContextHolder.getLocale().toString());

        LOGGER.log(Level.ERROR, "{} Erro não tratado! - messageKey={}", message, MESSAGE_KEY);

        map.put("error", message);
        map.put("key", MESSAGE_KEY);

        return Response.status(Response.Status.NOT_ACCEPTABLE).entity(map).type(MediaType.APPLICATION_JSON).build();
    }
}
