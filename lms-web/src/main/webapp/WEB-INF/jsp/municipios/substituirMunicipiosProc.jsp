<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/substituirMunicipios">
		<adsm:section caption="municipioErrado" />
		<adsm:lookup service="" dataType="text" property="municipio.id" criteriaProperty="municipio.codigo" label="municipioSubstituir" size="6" maxLength="50" labelWidth="20%" width="11%" action="/municipios/manterMunicipios" cmd="list" >
        	<adsm:propertyMapping modelProperty="nome" formProperty="municipio"/>
        </adsm:lookup>
		<adsm:textbox dataType="text" property="municipioSubstituir" disabled="true" width="69%" required="true" />
		<adsm:textbox dataType="text" property="uf" label="uf" labelWidth="20%" width="30%" size="37" disabled="true" />
		<adsm:textbox dataType="text" property="pais" label="pais" labelWidth="20%" width="30%" size="37" disabled="true" />
		
		<adsm:checkbox property="indDistrito" label="indDistrito" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="text" property="municDistrito" label="municDistrito" labelWidth="20%" width="30%" size="37" disabled="true" />
		<adsm:textbox dataType="text" property="filial" label="filial" labelWidth="20%" width="80%" size="37" disabled="true" />


		<adsm:section caption="municipioCorreto" />
		<adsm:lookup service="" dataType="text" property="municipio.id" criteriaProperty="municipio.codigo" label="municipio" size="6" maxLength="50" labelWidth="20%" width="11%" action="/municipios/manterMunicipios" cmd="list" >
        	<adsm:propertyMapping modelProperty="nome" formProperty="municipio"/>
        </adsm:lookup>
		<adsm:textbox dataType="text" property="municipio" disabled="true" width="69%" required="true" />
		<adsm:textbox dataType="text" property="uf" label="uf" labelWidth="20%" width="30%" size="37" disabled="true" />
		<adsm:textbox dataType="text" property="pais" label="pais" labelWidth="20%" width="30%" size="37" disabled="true" />
		
		<adsm:checkbox property="indDistrito" label="indDistrito" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="text" property="municDistrito" label="municDistrito" labelWidth="20%" width="30%" size="37" disabled="true" />
		<adsm:textbox dataType="text" property="filial" label="filial" labelWidth="20%" width="80%" size="37" disabled="true" />

		<adsm:buttonBar>
			<adsm:button caption="substituir"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>