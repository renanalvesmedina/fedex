<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tipoPagamPostoPassagemService">
	<adsm:form idProperty="idTipoPagamPostoPassagem" action="/municipios/manterTipoPagamentoPostoPassagem">
		<adsm:textbox dataType="text" property="dsTipoPagamPostoPassagem" label="descricao" maxLength="60" labelWidth="18%" size="60" width="82%"/>
		<adsm:combobox property="blCartaoPedagio" label="cartaoPedagio" domain="DM_SIM_NAO"  labelWidth="18%" width="82%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="82%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoPagamPostoPassagem"/> 
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoPagamPostoPassagem" property="tipoPagamPostoPassagem" selectionMode="check" defaultOrder="dsTipoPagamPostoPassagem" unique="true" rows="12">
		<adsm:gridColumn title="descricao" property="dsTipoPagamPostoPassagem" width="75%" />
		<adsm:gridColumn title="cartaoPedagio" property="blCartaoPedagio" width="15%" renderMode="image-check"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" />	
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>