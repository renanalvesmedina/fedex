<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAtributosMeioTransporte" service="lms.contratacaoveiculos.atributoMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterAtributosMeioTransporte" idProperty="idAtributoMeioTransporte">
		<adsm:textbox dataType="text" property="dsAtributoMeioTransporte" label="descricaoAtributo" maxLength="60" size="28" labelWidth="18%" width="32%"/>
		<adsm:combobox property="tpComponente" label="componente" domain="DM_TIPO_ATRIBUTO_MEIO_TRANSPORTE" width="32%" labelWidth="18%"/>
		<adsm:combobox property="tpInformacao" label="formato" domain="DM_TIPO_DADO_ATRIBUTO_VEICULO" width="32%" labelWidth="18%"/>
		<adsm:textbox dataType="text" property="dsGrupo" label="grupo" maxLength="60" size="28" width="32%" labelWidth="18%"/>
		<adsm:textbox dataType="integer" property="nrOrdem" label="ordem" maxLength="3" size="28" width="32%" labelWidth="18%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="32%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="atributoMeioTransporte"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idAtributoMeioTransporte" property="atributoMeioTransporte" rows="12" unique="true" defaultOrder="dsGrupo, nrOrdem">
		<adsm:gridColumn width="40%" title="descricaoAtributo" property="dsAtributoMeioTransporte" />
		<adsm:gridColumn width="15%" title="componente" property="tpComponente" isDomain="true" />
		<adsm:gridColumn width="15%" title="formato" property="tpInformacao" isDomain="true" />
		<adsm:gridColumn width="10%" title="grupo" property="dsGrupo" />
		<adsm:gridColumn width="10%" title="ordem" dataType="integer" property="nrOrdem" />
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
