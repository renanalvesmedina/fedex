<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contratacaoVeiculos/manterConteudosAtributosModelo">
				<adsm:textbox dataType="text" property="meioTransporte.descricao" label="meioTransporte" maxLength="50" labelWidth="23%" width="27%" size="30" disabled="true" />
		<adsm:textbox dataType="text" property="tipoMeioTransporte.descricao" label="tipoMeioTransporte" maxLength="50" labelWidth="23%" width="27%" size="30" disabled="true" />
		<adsm:textbox dataType="text" property="marca.descricao" label="marcaMeioTransporte" maxLength="30" labelWidth="23%" width="27%" size="30" disabled="true" />
		<adsm:textbox dataType="text" property="modelo.descricao" label="modeloMeioTransporte" maxLength="30" labelWidth="23%" width="27%" size="30" disabled="true" />		
		<adsm:textbox dataType="text" property="atributoMeioTransporte.descricao" label="atributo" maxLength="50" labelWidth="23%" size="30" width="27%" disabled="true" cellStyle="vertical-Align:bottom" />
		<adsm:textbox dataType="text" property="descricaoConteudoAtributo" required="true" label="descricaoConteudoAtributo" maxLength="50" labelWidth="23%" size="30" width="27%" cellStyle="vertical-Align:bottom" />
		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   