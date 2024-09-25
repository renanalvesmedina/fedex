<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/vincularVersaoDescritivos">
		<adsm:lookup action="/unknownAction" service="" dataType="integer" property="cliente.codigo" criteriaProperty="cliente.nome" label="cliente" size="5" maxLength="5" required="true" disabled="true">
		   	<adsm:propertyMapping modelProperty="cliente.codigo" formProperty="cliente.nome"/>
            <adsm:textbox dataType="text" property="cliente.nome" size="29" maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:textbox maxLength="8" size="8" dataType="integer" property="numeroVersao" label="versao" required="true" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:combobox property="processo" optionLabelProperty="label" optionProperty="1" service="" label="processo" prototypeValue="" prototypeValue="" width="35%" required="true"/>
		<adsm:combobox property="evento" optionLabelProperty="label" optionProperty="1" service="" label="evento" prototypeValue="" prototypeValue="" required="true" labelWidth="18%" width="32%" />
		<adsm:combobox property="ocorrencia" optionLabelProperty="label" optionProperty="1" service="" label="ocorrencia" prototypeValue="" prototypeValue="" width="35%" required="true"/>
		<adsm:combobox property="codigoDescritivo" optionLabelProperty="label" optionProperty="1" service="" label="codigoDescritivo" prototypeValue="" prototypeValue="" labelWidth="18%" width="32%" required="true" />
		<adsm:textarea required="true" columns="100" rows="7" maxLength="250" property="descricao" label="descricao" width="85%" disabled="true"/>
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>