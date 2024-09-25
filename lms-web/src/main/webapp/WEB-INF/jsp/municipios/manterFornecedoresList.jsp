<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarFornecedores">
	<adsm:form action="/municipios/manterFornecedores">
		<adsm:complement labelWidth="18%" width="82%" label="identificacao">
                  <adsm:combobox property="identificacao" width="15%" optionLabelProperty="a" optionProperty="0" service="" prototypeValue="CPF/CNPJ|CUIT/DNI|RUT|RUC" />
                  <adsm:textbox dataType="text" property="numeroIdentificacao" width="80%" size="75%"/>
        </adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.Nome" label="razaoSocial" maxLength="50" size="50" labelWidth="18%" width="60%" />
		<adsm:textbox dataType="text" property="homepage" label="homepage" maxLength="120" size="25" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="pessoa.email" label="email" maxLength="50" size="25" />
		<adsm:combobox property="situacao" label="situacao" service="" optionLabelProperty="" optionProperty="" prototypeValue="Ativo|Inativo" labelWidth="18%" width="60%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="cnpj" property="CNPJ" width="25%"/>		
		<adsm:gridColumn title="razaoSocial" property="razaoSocial" width="60%"/>
		<adsm:gridColumn title="situacao" property="situacao" width="15%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
