package com.mercurio.lms.expedicao.util.nfe;

import com.mercurio.lms.edi.dto.LogErrosEdiDTO;
import com.mercurio.lms.edi.model.LogErrosEDI;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class LogErroEdiUtil {

    public List<LogErrosEDI> toListLogErrosEDI(List<LogErrosEdiDTO> listLogErrosEdiDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<LogErrosEDI>>(){}.getType();
        return modelMapper.map(listLogErrosEdiDTO, listType);
    }
}
