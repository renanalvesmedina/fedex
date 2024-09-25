package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;

public interface ICalculoFranquiado {

	void setIdFranquia(Long idFranquia);
	
	void setParametrosFranqueado(CalculoFranqueadoParametros parametrosFranqueado);

	void setDocumento(DocumentoFranqueadoDTO documento);

	Serializable getDocumentoFranqueado();
	
	void executarCalculo();

}
