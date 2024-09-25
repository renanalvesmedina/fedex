<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/manterReajustesEspecificos">
		<adsm:combobox property="tipoTabela" label="tipoTabela" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="20%" width="35%"/>
		<adsm:combobox property="tipoMeioTransporte" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="20%" width="25%"/>

		<adsm:range label="vigenciaReajuste" labelWidth="20%" width="50%">
			<adsm:textbox dataType="date" required="true" property="dataVigenciaInicial" />
			<adsm:textbox dataType="date" property="dataVigenciaFinal"/>
		</adsm:range>

		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="7" maxLength="10" labelWidth="20%" width="12%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="18" maxLength="50" disabled="true" width="60%" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
