<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/vincularVersaoDescritivos">
		<adsm:lookup action="/unknownAction" service="" dataType="integer" property="cliente.codigo" criteriaProperty="cliente.nome" label="cliente" size="5" maxLength="5" disabled="true">
		   	<adsm:propertyMapping modelProperty="cliente.codigo" formProperty="cliente.nome"/>
            <adsm:textbox dataType="text" property="cliente.nome" size="29" maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:textbox maxLength="8" size="8" dataType="integer" property="numeroVersao" label="versao" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:combobox property="processo" optionLabelProperty="label" optionProperty="1" service="" label="processo" prototypeValue="" prototypeValue="" width="35%" />
		<adsm:combobox property="evento" optionLabelProperty="label" optionProperty="1" service="" label="evento" prototypeValue="" prototypeValue="" width="35%" labelWidth="18%" width="32%" />
		<adsm:combobox property="ocorrencia" optionLabelProperty="label" optionProperty="1" service="" label="ocorrencia" prototypeValue="" prototypeValue="" width="35%" />
		<adsm:combobox property="codigoDescritivo" optionLabelProperty="label" optionProperty="1" service="" label="codigoDescritivo" prototypeValue="" prototypeValue="" labelWidth="18%" width="32%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="cliente" property="cliente" width="20%" />
		<adsm:gridColumn title="versao" property="versao" width="7%" />
		<adsm:gridColumn title="processo" property="processo" width="20%" />
		<adsm:gridColumn title="evento" property="evento" width="20%" />
		<adsm:gridColumn title="ocorrencia" property="ocorrencia" width="18%" />
		<adsm:gridColumn title="codigoDescritivo" property="codigoDescritivo" width="15%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
