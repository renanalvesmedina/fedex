<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/manterGerenciasRegionaisCliente" >
		<adsm:masterLink showSaveAll="true">
			<adsm:masterLinkItem property="cliente" label="cliente" />
			<adsm:masterLinkItem property="gerenciaRegional" label="gerenciaRegional" />
		</adsm:masterLink>

		<adsm:lookup action="/municipios/manterMunicipios" label="municipio" service="" dataType="integer" property="nomeMunicipio" criteriaProperty="nomeMunicipio" maxLength="30" disabled="true" labelWidth="17%" width="33%" required="true"/>
		<adsm:combobox property="uf" optionLabelProperty="uf" optionProperty="1" service="" label="uf" prototypeValue="" disabled="true" required="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novaRegiao"/>
			<adsm:button caption="salvarRegiao"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true" >
		<adsm:gridColumn title="municipio" property="municipio" width="94%" />
		<adsm:gridColumn title="uf" property="uf" width="6%" />
		<adsm:buttonBar>
			<adsm:button caption="excluirRegiao"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
