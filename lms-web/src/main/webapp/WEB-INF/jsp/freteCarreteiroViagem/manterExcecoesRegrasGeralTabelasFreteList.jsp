<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProprietarios">
	<adsm:form action="/contratacaoVeiculos/manterProprietarios" height="108" >
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="7" maxLength="10" labelWidth="16%" width="12%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeFilial" size="40" maxLength="50" disabled="true" width="57%" />
		<adsm:complement labelWidth="16%" width="84%" label="identificacao">
            <adsm:combobox property="identificacao" optionLabelProperty="a" optionProperty="0" service="" prototypeValue="CPF/CNPJ|CUIT/DNI|RUT|RUC" />
            <adsm:textbox dataType="text" property="numeroIdentificacao" size="40"/>
        </adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nome" label="nome" maxLength="50" size="42" labelWidth="16%" width="34%" />
		<adsm:combobox property="pessoa.situacao" label="situacao" prototypeValue="Ativo|Inativo" service="" optionLabelProperty="" optionProperty="" labelWidth="16%" width="34%" required="true"/>		
		<adsm:textbox dataType="text" property="numeroInss" label="numeroInss" maxLength="11" size="30" labelWidth="16%" width="34%" />
		<adsm:lookup service="" dataType="text" property="" criteriaProperty="" label="municipioInss" size="38" labelWidth="16%" width="34%" action="/municipios/manterMunicipios.do" />
		<adsm:combobox property="localPagamento" label="localPagamento" prototypeValue="Filial|Matriz" service="" optionLabelProperty="" optionProperty="" labelWidth="16%" width="34%" />
		<adsm:combobox property="periodoPagamento" label="periodoPagamento" prototypeValue="Diário|Semanal|Decendial|Quinzenal|Mensal" service="" optionLabelProperty="" optionProperty="" labelWidth="16%" width="34%" />
		<adsm:combobox property="diaSemana.sequencia" label="diaSemana" service="" optionLabelProperty="diaSemana.nome" optionProperty="diaSemana.sequencia" labelWidth="16%" width="34%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true">
		<adsm:gridColumn width="25%" title="filial" property="filial" />
		<adsm:gridColumn width="18%" title="cpfCnpj" property="cpfCnpj" align="right" />
		<adsm:gridColumn width="29%" title="nome" property="nome" />
		<adsm:gridColumn width="10%" title="pagamento" property="pagamento" />
		<adsm:gridColumn width="10%" title="periodo" property="periodo" />
		<adsm:gridColumn width="8%" title="situacao" property="situacao" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
