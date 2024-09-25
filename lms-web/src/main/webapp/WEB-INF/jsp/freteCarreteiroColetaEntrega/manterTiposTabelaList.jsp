<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposTabela" service="lms.fretecarreteirocoletaentrega.manterTiposTabelaAction">
	<adsm:form action="/freteCarreteiroColetaEntrega/manterTiposTabela">
		<adsm:textbox dataType="text" property="dsTipoTabelaColetaEntrega" label="descricaoTipoTabela" maxLength="60" size="50" labelWidth="18%" width="75%" />
		<adsm:combobox property="blNormal" domain="DM_SIM_NAO" label="normal" labelWidth="18%" width="75%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="75%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="TipoTabelaColetaEntrega"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoTabelaColetaEntrega" property="TipoTabelaColetaEntrega" defaultOrder="dsTipoTabelaColetaEntrega" unique="true" rows="12">
		<adsm:gridColumn width="80%" title="descricaoTipoTabela" property="dsTipoTabelaColetaEntrega"/>
		<adsm:gridColumn width="10%" title="normal" property="blNormal" renderMode="image-check"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>