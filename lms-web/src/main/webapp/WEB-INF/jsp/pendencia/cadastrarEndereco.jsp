<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="cadastrarEnderecos">
	<adsm:form action="/pendencia/abrirMDA">
		<adsm:section caption="cadastrarEnderecos" />
		<adsm:lookup property="cliente.id" criteriaProperty="cliente" dataType="text" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" size="18" width="85%" disabled="true">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox property="nomeCliente" dataType="text" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="cep" label="cep" action="" labelWidth="15%" width="30%" service="" dataType="CEP" size="12" maxLength="10" required="true" />
		<adsm:textbox dataType="text" property="bairro" label="bairro" maxLength="40" size="28" width="40%"/>

		<adsm:lookup property="municipio" label="municipio" action="/municipios/manterMunicipios" service="" dataType="text" maxLength="50" required="true" labelWidth="15%" width="30%"/>
		<adsm:combobox property="uf" optionLabelProperty="value" optionProperty="0" service="" label="uf" required="true" width="40%"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:lookup action="/municipios/manterPaises" dataType="text" maxLength="30" service="" property="pais" label="pais" size="35%" labelWidth="15%" width="85%" required="true"/>

		<adsm:combobox label="endereco" optionLabelProperty="" optionProperty="" property="endereco" service="" prototypeValue="Avenida|Rua|Praça" labelWidth="15%" width="12%"/>
		<adsm:textbox dataType="text" size="70" property="endereco" maxLength="100" required="true" width="73%" />

		<adsm:textbox dataType="text" property="numero" label="numero" required="false" labelWidth="15%" width="30%" size="5" maxLength="6"/>
		<adsm:textbox dataType="text" property="complemento" label="complemento" size="28" width="40%" maxLength="40"/>

        <adsm:buttonBar freeLayout="false">
			<adsm:button caption="salvar"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>