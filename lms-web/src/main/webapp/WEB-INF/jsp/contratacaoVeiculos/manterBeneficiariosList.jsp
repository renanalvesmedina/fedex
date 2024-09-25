<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterBeneficiarios">
	<adsm:form action="/contratacaoVeiculos/manterBeneficiarios" >
		<adsm:complement width="100%" label="identificacao">
            <adsm:combobox property="identificacao" optionLabelProperty="a" optionProperty="0" service="" prototypeValue="CPF/CNPJ|CUIT/DNI|RUT|RUC" />
            <adsm:textbox dataType="text" property="numeroIdentificacao" size="61"/>
        </adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nome" label="nome" maxLength="50" size="80" width="85%"/>
		<adsm:textbox dataType="text" property="pessoa.correioEletronico" label="email" maxLength="50"  size="40" width="35%"/>
		<adsm:combobox property="pessoa.status" label="situacao" prototypeValue="Ativo|Inativo" service="" optionLabelProperty="" optionProperty="" width="35%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true">
		<adsm:gridColumn width="18%" title="cpfCnpj" property="cpfCnpj" align="right" />
		<adsm:gridColumn width="74%" title="nome" property="nome" />
		<adsm:gridColumn width="8%" title="situacao" property="situacao" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
