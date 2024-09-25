package com.mercurio.lms.workflow.model.service;

import java.util.List;

public interface WorkflowEngine {

	public abstract String executeClasseAcao(List pendenciasAbertas, List pendencias, String nmClasseAcao);

}