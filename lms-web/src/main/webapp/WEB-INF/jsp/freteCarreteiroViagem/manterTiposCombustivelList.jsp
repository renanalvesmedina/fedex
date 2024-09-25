<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposCombustivel" service="lms.fretecarreteiroviagem.manterTiposCombustivelAction">
	<adsm:form action="/freteCarreteiroViagem/manterTiposCombustivel">
        <adsm:textbox dataType="text" property="dsTipoCombustivel" label="descricao" size="40" maxLength="60" width="85%"/>
        <adsm:combobox domain="DM_STATUS" property="tpSituacao" label="situacao"/>
		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="TipoCombustivel"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoCombustivel" property="TipoCombustivel" selectionMode="check" unique="true"
			   defaultOrder="dsTipoCombustivel" rows="13">
		<adsm:gridColumn title="descricao" property="dsTipoCombustivel" width="70%"/>
		<adsm:gridColumn title="situacao"  property="tpSituacao" isDomain="true" width="30%"/>
		 
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>