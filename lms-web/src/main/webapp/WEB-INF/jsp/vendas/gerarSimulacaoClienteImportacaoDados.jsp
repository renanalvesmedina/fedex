<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/manterPropostasCliente" >
		<adsm:combobox property="tipoArquivo" optionLabelProperty="" optionProperty="" service="" labelWidth="20%" width="80%" label="tipoArquivo" required="true" prototypeValue="Padrão Mercúrio|Padrão cliente" />
		<adsm:textbox dataType="file" property="arquivoId" label="arquivo" labelWidth="20%" width="80%" required="true"/>
		<adsm:label key="notaFiscal" width="20%" />
			<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" required="true" size="10"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" required="true" size="10"/>
		    </adsm:range>
		<adsm:label key="municipioOrigem" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" required="true" size="10"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:label key="ufOrigem" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
			</adsm:range>
		<adsm:label key="municipioDestino" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:label key="ufDestino" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:label key="valorMercadoria" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:label key="pesoReal" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:label key="pesoCubado" width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:label key="qtdeVolumes"  width="20%" />
		<adsm:label key="posicaoDe" width="9%" />
			<adsm:range width="55%">
            	<adsm:textbox property="posicaoInicial" dataType="text" size="10" required="true"/>
             	<adsm:textbox property="posicaoFinal" dataType="text" size="10" required="true"/>
		    </adsm:range>
		<adsm:buttonBar>	
			<adsm:button caption="importarArquivo" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   