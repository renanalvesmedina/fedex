package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import com.mercurio.lms.mdfe.model.v300.Seg;

public class SegConverter {
	
    List<Seg> segList;
    
    public SegConverter(List<Seg> segList) {
        super();
        this.segList = segList;
    }

    public Seg[] convert() {
        return segList.toArray(new Seg[segList.size()]);
    }
    
}
