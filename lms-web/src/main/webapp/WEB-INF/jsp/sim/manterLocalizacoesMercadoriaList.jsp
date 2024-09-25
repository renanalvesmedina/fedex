<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterLocalizacoesMercadoriaAction">
	<adsm:form action="/sim/manterLocalizacoesMercadoria" idProperty="idLocalizacaoMercadoria">
		<adsm:textbox label="codigo" property="cdLocalizacaoMercadoria" dataType="integer" maxValue="999" maxLength="3" minValue="0" width="79%" labelWidth="21%" size="5"/>
		<adsm:textbox label="localizacaoDeMercadoria" property="dsLocalizacaoMercadoria" dataType="text" maxLength="60" width="79%" labelWidth="21%" size="40"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="21%" width="79%" />

	 	<adsm:buttonBar freeLayout="true">
	   		<adsm:findButton callbackProperty="localizacaoMercadoria" />
	   		<adsm:resetButton />
	    </adsm:buttonBar>
	</adsm:form>

    <adsm:grid idProperty="idLocalizacaoMercadoria" property="localizacaoMercadoria" rows="12" unique="true" defaultOrder="cdLocalizacaoMercadoria, tpSituacao">
		<adsm:gridColumn property="cdLocalizacaoMercadoria" title="codigo" dataType="integer" width="10%"/>
		<adsm:gridColumn property="faseProcesso.dsFase" title="faseProcesso" width="25%"/>
		<adsm:gridColumn property="dsLocalizacaoMercadoria" title="localizacaoDeMercadoria" width="40%"/>
		<adsm:gridColumn property="blArmazenagem" title="armazenagem" renderMode="image-check" width="15%"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>		
    </adsm:grid>
	
</adsm:window>
