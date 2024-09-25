<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window title="consultarConfiguracaoAuditorias" service="lms.portaria.manterConfiguracaoAuditoriasAction" >
	<adsm:form action="portaria/manterConfiguracaoAuditorias" idProperty="idConfiguracaoAuditoria">
	
		<adsm:lookup property="filial" idProperty="idFilial" required="false" criteriaProperty="sgFilial" maxLength="3" service="lms.portaria.manterConfiguracaoAuditoriasAction.findLookupFilial" dataType="text" label="filial" size="3" action="/municipios/manterFiliais" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpOperacao" labelWidth="20%" label="tipoOperacao" domain="DM_TIPO_OPERACAO_AUDITORIA" width="30%" required="false" />

		<adsm:range label="horarioAuditoria" labelWidth="20%" width="80%" required="false" >
             <adsm:textbox dataType="JTTime" property="hrConfiguracaoInicial"  />
             <adsm:textbox dataType="JTTime" property="hrConfiguracaoFinal"  />
        </adsm:range>

        <adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="configuracaoAuditoria"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	
	<adsm:grid property="configuracaoAuditoria" idProperty="idConfiguracaoAuditoria" selectionMode="check"  gridHeight="190" 
			   unique="true" scrollBars="horizontal" defaultOrder="filial_.sgFilial, filial_pessoa_.nmPessoa, tpOperacao, hrConfiguracaoInicial, dtVigenciaInicial" 
			   rows="9">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="filial.sgFilial" width="80" /> 
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="70" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="tipoOperacao" property="tpOperacao" isDomain="true" width="120" align="left"/>
		<adsm:gridColumn title="horarioInicial" property="hrConfiguracaoInicial" width="100" align="center" dataType="JTTime" />
		<adsm:gridColumn title="horarioFinal" property="hrConfiguracaoFinal" width="100" align="center" dataType="JTTime" />
		<adsm:gridColumn title="quantidadeVeiculosProprios" property="qtVeiculosProprios" width="210" align="right"/>
		<adsm:gridColumn title="quantidadeVeiculosTerceiros" property="qtVeiculosTerceiros" width="210" align="right"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate" align="center"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="90" dataType="JTDate" align="center"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
