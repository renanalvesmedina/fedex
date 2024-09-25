<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/manterDadosComplementares">
		<adsm:lookup property="cliente.id" label="cliente" action="/" service="" dataType="text" size="11" maxLength="11" disabled="true" width="85%" required="true">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="cliente.nome"/>
			<adsm:textbox dataType="text" property="cliente.nome" disabled="true"/>
		</adsm:lookup>
		<adsm:combobox property="evento" optionLabelProperty="value" optionProperty="0" service="" label="evento" required="true"/>
		<adsm:textbox maxLength="40" size="43" dataType="text" property="descricao" label="descricao" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>