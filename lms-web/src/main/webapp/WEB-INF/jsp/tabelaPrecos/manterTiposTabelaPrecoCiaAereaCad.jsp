<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/tabelaPrecos/manterTiposTabelaPrecoCiaAerea">
		<adsm:lookup action="/unknownAction" service="" dataType="integer" property="ciaAerea.codigo" criteriaProperty="ciaAerea.name" label="ciaAerea" size="5" maxLength="5" required="true">
		   	<adsm:propertyMapping modelProperty="ciaAerea.codigo" formProperty="ciaAerea.nome"/>
            <adsm:textbox dataType="text" property="ciaAerea.nome" size="28" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/unknownAction" service="" dataType="integer" property="cliente.codigo" criteriaProperty="cliente.name" label="cliente" size="5" maxLength="5">
		   	<adsm:propertyMapping modelProperty="cliente.codigo" formProperty="cliente.nome"/>
            <adsm:textbox dataType="text" property="cliente.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:textbox maxLength="20" size="23" dataType="text" property="descricao" label="descricao" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>