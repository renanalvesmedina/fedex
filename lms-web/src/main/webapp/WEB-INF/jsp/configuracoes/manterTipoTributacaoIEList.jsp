<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.manterTipoTributacaoIEAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/configuracoes/manterTipoTributacaoIE">
	
		<adsm:hidden property="inscricaoEstadual.pessoa.idPessoa" serializable="true"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:label key="branco" style="width:0"/>
		<td colspan="20" id="labelPessoa" class="FmLbRequerido"></td>
		<adsm:textbox 
					dataType="text" 
		            property="inscricaoEstadual.pessoa.nrIdentificacao" 
		            size="20" 
		            serializable="false"
		            maxLength="20" 
		            width="80%">
		              
		            <adsm:textbox 
		            			dataType="text" 
		              			property="inscricaoEstadual.pessoa.nmPessoa" 
		              			serializable="false"
		              			size="60" 
		              			maxLength="20"/>
		</adsm:textbox>
		              
		<adsm:hidden property="inscricaoEstadual.idInscricaoEstadual" serializable="true"/>
		<adsm:textbox label="ie"  
		              dataType="text" 
		              property="inscricaoEstadual.nrInscricaoEstadual" 
		              size="20" 
		              serializable="false"
		              maxLength="50" 
		              labelWidth="20%" 
		              width="30%"/>
		              
		<adsm:textbox 
					label="vigencia" 
					dataType="JTDate" 
					property="dtVigencia"
					labelWidth="20%"
					width="30%"/>	          

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoTributacaoIE"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idTipoTributacaoIE" 
			   property="tipoTributacaoIE" 
			   defaultOrder="dtVigenciaInicial" 
			   selectionMode="check" 
			   rows="13"
			   unique="true">
		
		<adsm:gridColumn title="tpSituacaoTributaria" property="tpSituacaoTributaria" width="" isDomain="true" dataType="text" />
		<adsm:gridColumn title="tipoTributacao" property="tipoTributacaoIcms.dsTipoTributacaoIcms" width="150" dataType="text"/>
		<adsm:gridColumn title="blIsencaoExportacoes" property="blIsencaoExportacoes"  width="150" align="center" renderMode="image-check"/>
		<adsm:gridColumn title="blAceitaSubstituicao" property="blAceitaSubstituicao"  width="150" align="center" renderMode="image-check"/>
		<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" dataType="JTDate" width="80"/>
		<adsm:gridColumn title="" property="dtVigenciaFinal" dataType="JTDate" width="80"/>
        
        <adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>

<script>
function myOnPageLoad_cb(data, erro){
	onPageLoad_cb();
	document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
}
</script>