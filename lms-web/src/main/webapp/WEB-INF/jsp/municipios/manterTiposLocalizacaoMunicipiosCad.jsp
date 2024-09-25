<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tipoLocalizacaoMunicipioService" >
	<adsm:form action="/municipios/manterTiposLocalizacaoMunicipios" idProperty="idTipoLocalizacaoMunicipio">
		<adsm:textbox dataType="text" property="dsTipoLocalizacaoMunicipio" label="descricao" required="true" width="85%" maxLength="60" size="60" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" width="85%"/>
		<adsm:combobox property="tpLocalizacao" domain="DM_TIPO_LOCALIZACAO" label="tipoLocalizacao" width="85%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
   