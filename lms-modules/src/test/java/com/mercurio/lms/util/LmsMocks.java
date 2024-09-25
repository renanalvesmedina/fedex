package com.mercurio.lms.util;

import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LmsMocks {
    
    public static void mockDefaultContext() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletRequestHolder.setHttpServletRequest(request);
        HttpSession httpSession = mock(HttpSession.class);

        when(HttpServletRequestHolder.getHttpServletRequest().getSession(false)).thenReturn(httpSession);
        
        Filial f = new Filial();
        f.setDsTimezone("UTC");
        SessionContext.set(SessionKey.FILIAL_KEY,f);
        when(SessionContext.get(SessionKey.FILIAL_KEY)).thenReturn(f);
        when(SessionUtils.getFilialSessao()).thenReturn(f);
    }
    
}
