package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.mdfe.model.v300.InfPercurso;
import com.mercurio.lms.mdfe.model.v300.types.TUf;

public class InfPercursoConverter {
    
    private final List<String> filiaisPercurso;
    
    public InfPercursoConverter(List<String> filiaisPercurso) {
        super();
        this.filiaisPercurso = filiaisPercurso;
    }

    public InfPercurso[] convert() {
    	InfPercurso[] infPercursos = new InfPercurso[filiaisPercurso.size()];
    	List<InfPercurso> infPercursosAux = new ArrayList<InfPercurso>();

    	if(filiaisPercurso != null && !filiaisPercurso.isEmpty()){
    		infPercursos = new InfPercurso[filiaisPercurso.size()];
    		
    		for (String uf : filiaisPercurso) {
				InfPercurso infPercurso = new InfPercurso();
				infPercurso.setUFPer(TUf.valueOf(uf));
				infPercursosAux.add(infPercurso);
			}
    		
    		infPercursosAux.toArray(infPercursos);
    	}
    	
    	return infPercursos;
    }
    
}
