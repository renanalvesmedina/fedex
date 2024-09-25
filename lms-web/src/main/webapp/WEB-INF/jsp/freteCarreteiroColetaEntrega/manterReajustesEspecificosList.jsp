<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/manterReajustesEspecificos">
		<adsm:combobox property="tipoTabela" label="tipoTabela" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="20%" width="30%"/>
		<adsm:combobox property="tipoMeioTransporte" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" prototypeValue="" labelWidth="20%" width="30%"/>

		<adsm:textbox dataType="date" property="vigenciaReajuste" label="vigenciaReajuste" width="50%" labelWidth="20%"/>

		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="7" maxLength="10" labelWidth="20%" width="12%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/> 
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeFilial" size="18" maxLength="50" disabled="true" width="60%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true">
		<adsm:gridColumn title="tipoTabela" property="tpTab" width="20%"/>
		<adsm:gridColumn title="tipoMeioTransporte" property="tpMT" width="20%"/>
		<adsm:gridColumn title="vigencia" property="vig" width="30%" align="center"/>
		<adsm:gridColumn title="filial" property="filial" width="30%"/>

		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
