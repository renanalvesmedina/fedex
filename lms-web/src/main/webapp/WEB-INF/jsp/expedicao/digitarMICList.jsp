<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/digitarMIC" >
		<adsm:lookup action="/municipios/manterFiliais" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filialOrigem" size="5" maxLength="5" labelWidth="17%" >
		   	<adsm:propertyMapping modelProperty="filialOrigem.codigo" formProperty="filialOrigem.nome"/>
            <adsm:textbox dataType="text" property="filialOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/municipios/manterFiliais" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filialDestino" size="5" maxLength="5" labelWidth="13%">
		   	<adsm:propertyMapping modelProperty="filialDestino.codigo" formProperty="filialDestino.nome"/>
            <adsm:textbox dataType="text" property="filialDestino.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>

		<adsm:combobox property="tipoMIC" label="tipoMIC" service="" optionLabelProperty="" optionProperty="" prototypeValue="MIC|MIC/DTA" labelWidth="17%"/>

		<adsm:textbox property="numeroMIC" dataType="text" label="numeroMIC" size="10" maxLength="10" labelWidth="13%"/>

		<adsm:range label="periodoEmissao" labelWidth="17%">
	         <adsm:textbox dataType="date" property="periodoEmissaoInicial"/>
	         <adsm:textbox dataType="date" property="periodoEmissaoFinal"/>
        </adsm:range>


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true" >
		<adsm:gridColumn title="filialOrigem" property="filOri" width="25%" />
		<adsm:gridColumn title="filialDestino" property="filDes" width="25%" />
		<adsm:gridColumn title="numeroMIC" property="nro" width="20%" align="right"/>
		<adsm:gridColumn title="tipoMIC" property="tipo" width="15%" />
		<adsm:gridColumn title="dataEmissao" property="data" width="15%" />

		<adsm:buttonBar>
			<adsm:button caption="emitir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
