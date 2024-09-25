<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/emitirPerformanceHorarioRotaCE">
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="6" maxLength="3"  width="11%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeFilial" size="21" maxLength="50" disabled="true" width="74%" />
		<adsm:combobox property="rota" label="rota" service="" optionLabelProperty="" optionProperty=""/>
		<adsm:range label="periodo"  width="60%" required="true" >
			<adsm:textbox dataType="JTDate" property="periodoI"/>
			<adsm:textbox dataType="JTDate" property="periodoF"/>
		</adsm:range>
		<adsm:combobox property="ATRASO" prototypeValue="Sim|Não" label="somenteAtraso" service="" optionLabelProperty="" optionProperty="" width="30%"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="municipios/emitirPerformanceHorarioRotaCE.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>