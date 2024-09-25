<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/entrega/consultarManifestosEntrega" height="390">
		<adsm:textbox dataType="text" size="25" property="pedidoNumero" label="pedidoNumero" labelWidth="21%" width="29%" disabled="true"/>
		<adsm:textbox dataType="text" size="25" property="dataEmissao" label="dataEmissao" labelWidth="21%" width="29%" disabled="true"/>
		<adsm:textbox dataType="text" property="volumes" label="volumes" size="25" labelWidth="21%" width="29%" disabled="true"/>
		<adsm:textbox dataType="text" property="pesoBruto" label="pesoBruto" size="25" labelWidth="21%" width="29%" disabled="true" unit="kg" />

		<adsm:section caption="destinatario"/>
			<adsm:textbox dataType="text" property="razaoSocial" label="razaoSocial" size="25" labelWidth="21%" width="79%" disabled="true" />
			<adsm:textbox dataType="text" property="endereco" label="endereco" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="municipio" label="municipio" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="contato" label="contato" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="telefone" label="telefone" size="25" labelWidth="21%" width="29%" disabled="true" />

		<adsm:section caption="informacoesAdicionais"/>
			<adsm:combobox optionLabelProperty="" optionProperty="" property="MOEDA" service="" label="moeda" labelWidth="21%" width="69%" disabled="true"/>
			<adsm:textbox dataType="text" property="valorVendas" label="valorVendas" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="valorExportacao" label="valorExportacao" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="dpeMercurio" label="dpeMercurio" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="dpeCooperada" label="dpeCooperada" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="modalBrasil" label="modalBrasil" size="25" labelWidth="21%" width="29%" disabled="true" />
			<adsm:textbox dataType="text" property="modalExterior" label="modalExterior" size="25" labelWidth="21%" width="29%" disabled="true" />

		<adsm:section caption="documentos"/>
			<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" rows="7">
				<adsm:gridColumn width="60%" title="documento" property="DOCUMENTO" align="left"/>
				<adsm:gridColumn width="40%" title="numero" property="NUMERO" align="left"/>
			</adsm:grid>
	</adsm:form>
	<adsm:buttonBar>
		<adsm:button caption="voltar"/>
	</adsm:buttonBar>
</adsm:window>