<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/entrega/emitirExtratoOcorrenciasEntrega">		
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="6" maxLength="3" labelWidth="20%" width="11%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="18" maxLength="50" disabled="true" width="69%" required="true" />
		<adsm:combobox property="servico.nome" label="servico" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:combobox property="rotaColetaEntrega.nome" label="rotaColetaEntrega" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:combobox property="ocorrenciaEntrega.id" label="ocorrenciaEntrega" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:range label="periodoOcorrencia" labelWidth="20%" width="80%" required="true" >
			<adsm:textbox dataType="dateTime" property="periodoInicial"/> 
			<adsm:textbox dataType="dateTime" property="periodoFinal"/>
		</adsm:range>	
		<adsm:range label="periodoHorario" labelWidth="20%" required="true" width="80%" >
			<adsm:textbox dataType="dateTime" property="periodoInicial"/> 
			<adsm:textbox dataType="dateTime" property="periodoFinal"/>
		</adsm:range>	
        <adsm:textbox dataType="time" property="horarioFragmentadoPor" label="horarioFragmentadoPor" size="8" labelWidth="20%" width="80%" required="true" />	
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/entrega/emitirExtratoOcorrenciasEntrega.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>