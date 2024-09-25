package com.mercurio.lms.util.session;


public abstract class SessionKey {

	public static final String MOEDA_KEY = "MOEDA_KEY";

	public static final String EMPRESA_KEY = "EMPRESA_KEY";
	public static final String FILIAL_KEY = "FILIAL_KEY";
	public static final String CLIENTE_KEY = "CLIENTE_KEY";

	public static final String ULT_HIST_FILIAL_KEY = "ULT_HIST_FILIAL_KEY";
	public static final String PEN_HIST_FILIAL_KEY = "PEN_HIST_FILIAL_KEY";
	public static final String FILIAL_DTZ = "FILIAL_DTZ";
	
	public static final String FILIAL_MATRIZ_KEY = "FILIAL_MATRIZ_KEY";

	public static final String SETOR_KEY = "SETOR_KEY";
	public static final String PAIS_KEY = "PAIS_KEY";
	public static final String EMPRESAS_ACESSO_KEY = "EMPRESAS_ACESSO_KEY";
	public static final String FILIAIS_ACESSO_KEY = "FILIAIS_ACESSO_KEY";
	public static final String REGIONAIS_ACESSO_KEY = "REGIONAIS_ACESSO_KEY";
	public static final String CLIENTES_ACESSO_KEY = "CLIENTES_ACESSO_KEY";
	public static final String REGIONAIS_FILIAIS_ACESSO_KEY = "REGIONAIS_FILIAIS_ACESSO_KEY";
	public static final String MENU_KEY = "MENU_KEY";

	/**
	 * Indica que a integração está executando. Possui um java.lang.Boolean.
	 */
	public static final String INTEGRATION_RUNNING_KEY = "INTEGRATION_RUNNING_KEY";

	/**
	 * Indica que as triggers da integração devem efetuar log dos dados modificados pelo sistema. Possui um java.lang.Boolean.
	 */
	public static final String INTEGRATION_TRIGGER_LOG_DISABLED_KEY = "INTEGRATION_TRIGGER_LOG_DISABLED_KEY";

	/**
	 * Indica que é uma rotina batch que está executando. Possui um java.lang.Boolean.
	 */
	public static final String BATCH_JOB_RUNNING_KEY = "BATCH_JOB_RUNNING_KEY";
}