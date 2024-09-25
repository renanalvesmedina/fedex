<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/manterDadosComplementares">
		<adsm:lookup property="cliente.id" label="cliente" action="/" service="" dataType="text" size="11" maxLength="11" disabled="true" width="85%">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="cliente.nome"/>
			<adsm:textbox dataType="text" property="cliente.nome" disabled="true"/>
		</adsm:lookup>
		<adsm:combobox property="evento" optionLabelProperty="value" optionProperty="0" service="" label="evento"/>
		<adsm:textbox maxLength="40" size="43" dataType="text" property="descricao" label="descricao"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="evento" property="evento" width="30%" />
		<adsm:gridColumn title="descricao" property="descricao" width="70%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
