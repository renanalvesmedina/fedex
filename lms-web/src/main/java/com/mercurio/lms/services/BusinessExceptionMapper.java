package com.mercurio.lms.services;

import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.util.dto.ErroProcessamentoEdiDto;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException businessException) {
        Object[] messageArguments = businessException.getMessageArguments();
        String msgKey = businessException.getMessageKey();
        String message = RecursoMensagemCache.getMessage(msgKey, LocaleContextHolder.getLocale().toString());

        if (messageArguments != null ){
            Object messageArgument = messageArguments[0];
            if (messageArgument instanceof ErroProcessamentoEdiDto) {
                if (messageArguments.length > 1) {
                    messageArguments = ArrayUtils.remove(messageArguments, 0);
                    message = MessageFormat.format(message, messageArguments);
                }
                ErroProcessamentoEdiDto erroProcessamentoEdiDto = (ErroProcessamentoEdiDto)messageArgument;
                erroProcessamentoEdiDto.setErro(msgKey);
                erroProcessamentoEdiDto.setDescription(message);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(erroProcessamentoEdiDto)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }

        Map<String, Object> respose = new HashMap<>();
        respose.put("erro", message);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(respose)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
