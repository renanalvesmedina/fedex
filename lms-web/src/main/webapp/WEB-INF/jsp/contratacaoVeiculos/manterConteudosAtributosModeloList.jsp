<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterConteudosAtributosModelo">
	<adsm:form action="/contratacaoVeiculos/manterConteudosAtributosModelo">
		<adsm:textbox dataType="text" property="meioTransporte.descricao" label="meioTransporte" maxLength="50" labelWidth="23%" width="27%" size="30" disabled="true" />
		<adsm:textbox dataType="text" property="tipoMeioTransporte.descricao" label="tipoMeioTransporte" maxLength="50" labelWidth="23%" width="27%" size="30" disabled="true" />
		<adsm:textbox dataType="text" property="marca.descricao" label="marcaMeioTransporte" maxLength="30" labelWidth="23%" width="27%" size="30" disabled="true" />
		<adsm:textbox dataType="text" property="modelo.descricao" label="modeloMeioTransporte" maxLength="30" labelWidth="23%" width="27%" size="30" disabled="true" />		
		<adsm:textbox dataType="text" property="atributoMeioTransporte.descricao" label="atributo" maxLength="50" labelWidth="23%" size="30" width="27%" disabled="true" cellStyle="vertical-Align:bottom" />
		<adsm:textbox dataType="text" property="descricao" label="descricaoConteudoAtributo" maxLength="50" labelWidth="23%" size="30" width="27%" cellStyle="vertical-Align:bottom" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true">
		<adsm:gridColumn width="12%" title="meioTransporte" property="meioTransporte" />
		<adsm:gridColumn width="18%" title="tipoMeioTransporte" property="tipoMeioTransporte" />
		<adsm:gridColumn width="18%" title="marcaMeioTransporte" property="marca" />
		<adsm:gridColumn width="18%" title="modeloMeioTransporte" property="modelo" />
		<adsm:gridColumn width="18%" title="atributo" property="atributo" />
		<adsm:gridColumn width="18%" title="descricaoConteudoAtributo" property="descricao" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
