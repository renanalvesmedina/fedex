<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/inserirParametros">

		<adsm:section caption="origem"/>
		<adsm:combobox property="zonaOrigem" optionLabelProperty="value" optionProperty="0" service="" labelWidth="16%" width="19%" label="zona" />
		<adsm:lookup action="/municipios/manterPaises" service="" dataType="integer" property="nomePaisOrigem" criteriaProperty="nomePais" label="pais" labelWidth="6%" width="25%" maxLength="30"/>
		<adsm:combobox property="ufOrigem" optionLabelProperty="value" optionProperty="0" service="" labelWidth="10%" width="30%" label="uf" labelWidth="5%" width="29%"/>
		<adsm:lookup action="/unknownAction" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filial" size="5" maxLength="5" labelWidth="16%" width="37%">
		   	<adsm:propertyMapping modelProperty="filialOrigem.codigo" formProperty="filial.nome"/>
            <adsm:textbox dataType="text" property="filialOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/municipios/manterMunicipios" service="" dataType="integer" property="municipioOrigem" criteriaProperty="nomeMunicipio" label="municipio" labelWidth="10%" width="37%" maxLength="50" size="43"/>
		<adsm:lookup action="/municipios/manterAeroportos" service="" dataType="integer" property="aeroportoOrigem.codigo" criteriaProperty="aeroporto.name" label="aeroporto" size="5" maxLength="5" width="80%" labelWidth="16%" >
		   	<adsm:propertyMapping modelProperty="aeroportoOrigem.codigo" formProperty="aeroportoOrigem.nome"/>
            <adsm:textbox dataType="text" property="aeroportoOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:combobox property="tipoLocalizacaoOrigem" optionLabelProperty="value" optionProperty="0" service="" label="tipoLocalizacao" labelWidth="16%" width="84%"/>
		<adsm:section caption="destino" />
		<adsm:combobox property="zonaOrigem" optionLabelProperty="value" optionProperty="0" service="" labelWidth="16%" width="19%" label="zona" />
		<adsm:lookup action="/municipios/manterPaises" service="" dataType="integer" property="nomePaisOrigem" criteriaProperty="nomePais" label="pais" labelWidth="6%" width="25%" maxLength="30"/>
		<adsm:combobox property="ufOrigem" optionLabelProperty="value" optionProperty="0" service="" labelWidth="10%" width="30%" label="uf" labelWidth="5%" width="29%"/>
		<adsm:lookup action="/unknownAction" service="" dataType="integer" property="filialOrigem.codigo" criteriaProperty="empresa.name" label="filial" size="5" maxLength="5" labelWidth="16%" width="37%">
		   	<adsm:propertyMapping modelProperty="filialOrigem.codigo" formProperty="filial.nome"/>
            <adsm:textbox dataType="text" property="filialOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup action="/municipios/manterMunicipios" service="" dataType="integer" property="municipioOrigem" criteriaProperty="nomeMunicipio" label="municipio" labelWidth="10%" width="37%" maxLength="50" size="43"/>
		<adsm:lookup action="/municipios/manterAeroportos" service="" dataType="integer" property="aeroportoOrigem.codigo" criteriaProperty="aeroporto.name" label="aeroporto" size="5" maxLength="5" width="80%" labelWidth="16%" >
		   	<adsm:propertyMapping modelProperty="aeroportoOrigem.codigo" formProperty="aeroportoOrigem.nome"/>
            <adsm:textbox dataType="text" property="aeroportoOrigem.nome" size="30" maxLength="45" disabled="true"/>
        </adsm:lookup>
		<adsm:combobox property="tipoLocalizacaoOrigem" optionLabelProperty="value" optionProperty="0" service="" label="tipoLocalizacao" labelWidth="16%" width="84%"/>
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>