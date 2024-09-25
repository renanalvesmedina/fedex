<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTarifaPostoPassagem" service="lms.municipios.tarifaPostoPassagemService">
	<adsm:form action="/municipios/manterTarifaPostoPassagem">
		<adsm:hidden property="postoPassagem.idPostoPassagem"/> 
		<adsm:textbox dataType="text" property="tpPosto" label="tipo" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false"/>
		<adsm:textbox dataType="text" property="localizacao" label="localizacaoMunicipio" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false"/>
		<adsm:textbox dataType="text" property="rodovia" label="rodovia" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false"/>
		<adsm:textbox dataType="text" property="sentido" label="sentidoCobranca" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false"/>

		<adsm:combobox property="tpFormaCobranca" label="formaCobranca" domain="DM_FORMA_COBRANCA_POSTO_PASSAGEM" width="73%" labelWidth="17%"/>
		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="TarifaPostoPassagem"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTarifaPostoPassagem" property="TarifaPostoPassagem" 
	selectionMode="check" gridHeight="200" unique="true" 
	rows="11" defaultOrder="tpFormaCobranca,dtVigenciaInicial">
		<adsm:gridColumn title="formaCobranca" property="tpFormaCobranca" width="70%" isDomain="true"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="15%"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<Script>
<!--
	// foi criado essa variavel pois estava ocorrendo erro de sincronização
	var xControl = 0;
	function desabledTabDf2() {
		tab_onShow();
		if (xControl++ > 0) {
			var tabGroup = getTabGroup(this.document);
				tabGroup.setDisabledTab("val",true);
		}
	}
//-->
</Script>