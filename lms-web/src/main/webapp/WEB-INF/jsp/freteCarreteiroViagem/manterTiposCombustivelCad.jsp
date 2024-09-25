<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterTiposCombustivelAction">
	<adsm:form action="/freteCarreteiroViagem/manterTiposCombustivel" idProperty="idTipoCombustivel">
        <adsm:textbox dataType="text" property="dsTipoCombustivel" label="descricao" size="40" maxLength="60" width="70%" required="true"/>
        <adsm:combobox domain="DM_STATUS" property="tpSituacao" label="situacao" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="valoresCombustivel" action="/freteCarreteiroViagem/manterValoresCombustivel" cmd="main">
				<adsm:linkProperty src="idTipoCombustivel,dsTipoCombustivel" target="tipoCombustivel.idTipoCombustivel"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   