package com.mercurio.lms.vol.action;

import com.mercurio.lms.vol.action.RecusaAction;
import com.mercurio.lms.vol.model.service.VolRecusasService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Daniel.Maria
 */
public class RecusaActionTest {
    RecusaAction recusaAction;
    
    public RecusaActionTest() {
        recusaAction = new RecusaAction();
        recusaAction.setVolRecusasService(new VolRecusasService());
    }
    
    @Test
    public void testeDescriptografaIdRecusaComIdsValidos(){
        List<String> idsRecusa = Arrays.asList("125183", "125284", "125223");
        for (String idRecusa : idsRecusa) {
            Long descriptografiaId = recusaAction.descriptografaIdRecusa(idRecusa);
            Assert.assertEquals(Long.parseLong(idRecusa), descriptografiaId, 0.1);
        }
    }
    
    @Test
    public void testeDescriptografaIdRecusaComIdsInvalidos(){
        List<Long> idsRecusa = Arrays.asList(125183L, 125284L, 125223L);
        List<String> idsRecusaCriptografados = Arrays.asList("4xjbdvuNByE=", "1QmvxPcauSE=", "ql9+fF8b8ug=");
        for (int i = 0; i < idsRecusaCriptografados.size(); i++) {
            Assert.assertEquals(idsRecusa.get(i), recusaAction.descriptografaIdRecusa(idsRecusaCriptografados.get(i)), 0.1);
        }
    }
}
