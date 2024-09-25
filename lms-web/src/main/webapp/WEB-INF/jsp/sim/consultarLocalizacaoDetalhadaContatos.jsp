<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacaoDetalhadaContatos">
		<adsm:section caption="dadosGerais"/>
		<adsm:textbox label="filial" labelWidth="15%" width="11%" size="10" maxLength="20" dataType="text" property="filialOrigem" disabled="true"/>
        <adsm:textbox width="64%" size="40" dataType="text" property="nomeRemetente" disabled="true"/>

		<adsm:textbox width="35%" size="25" dataType="text" property="endereco" label="endereco" disabled="true"/>
		<adsm:textbox width="35%" size="25" dataType="text" property="municipio" label="municipio" disabled="true"/>
		<adsm:textbox width="35%" size="25" dataType="text" property="uf" label="uf" disabled="true"/>
		<adsm:textbox width="35%" size="25" dataType="text" property="pais" label="pais" disabled="true"/>


		<adsm:textbox width="35%" size="25" dataType="text" property="page" label="homepage" disabled="true"/>
		<adsm:textbox width="35%" size="25" dataType="text" property="mail" label="email" disabled="true"/>
		<adsm:textbox width="35%" size="25" dataType="text" property="fone" label="telefone" disabled="true"/>
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>