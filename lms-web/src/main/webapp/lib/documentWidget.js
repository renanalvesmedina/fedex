function changeDocumentWidgetType(args) {

   var documentTypeElement = args.documentTypeElement;
   var filialElement = args.filialElement;
   var documentNumberElement = args.documentNumberElement;
   var documentNumberCriteriaElement = args.documentNumberCriteriaElement;
   var verifierDigitElement = args.verifierDigitElement;
   var parentQualifier = args.parentQualifier;
   var documentGroup = args.documentGroup;
   var actionService = args.actionService;
   var actionServiceMethodSuffix = args.actionServiceMethodSuffix;
   
   var documentTypeValue = documentTypeElement.value;
   
   if (documentTypeElement.onchangeAfterValueChanged == undefined)
      documentTypeElement.onchangeAfterValueChanged = false;
   
   resetValue(documentNumberElement);
   if (documentNumberElement.widgetType != "lookup")
      resetValue(documentNumberElement.property);
   setDisabled(documentNumberElement, true);
   
   if (documentTypeValue != "") {
      var documentDefinition = eval(documentTypeValue + "_" + documentGroup + "_DOCUMENT_WIDGET_DEFINITION");
      changeDocumentWidget({documentDefinition:documentDefinition, filialElement:filialElement, documentNumberElement:documentNumberElement, verifierDigitElement:verifierDigitElement, parentQualifier:parentQualifier, actionService:actionService, actionServiceMethodSuffix:actionServiceMethodSuffix});
      if (documentDefinition.verifierDigit != undefined)
         resetValue(verifierDigitElement);
      disableDocumentWidget({disable:false, filialElement:filialElement, documentNumberElement:documentNumberElement, verifierDigitElement:verifierDigitElement});
   }
   else {
      if (verifierDigitElement != undefined) {
         var verifierDigitProperty = verifierDigitElement.property;
         var verifierDigitCriteriaProperty = verifierDigitElement.criteriaProperty;
         var verifierDigitCriteriaElement = document.getElementById(verifierDigitProperty + "." + verifierDigitCriteriaProperty);
         verifierDigitCriteriaElement.value = "";
      }
   
      disableDocumentWidget({disable:true, filialElement:filialElement, documentNumberElement:documentNumberElement, verifierDigitElement:verifierDigitElement});
      
      if (documentNumberCriteriaElement != undefined)
         setDisabled(documentNumberCriteriaElement, true);

   }
}

function disableDocumentWidget(args) {

   var filialElement = args.filialElement;
   var documentNumberElement = args.documentNumberElement;
   var verifierDigitElement = args.verifierDigitElement;
   var disable = args.disable;
   
   var filialProperty = filialElement.property;
   var filialCriteriaProperty = filialElement.criteriaProperty;
   var filialCriteriaElement = document.getElementById(filialProperty + "." + filialCriteriaProperty);

   var documentNumberProperty = documentNumberElement.property;
   var documentNumberCriteriaProperty = documentNumberElement.criteriaProperty;
   var documentNumberCriteriaElement = document.getElementById(documentNumberProperty + "." + documentNumberCriteriaProperty);

   setDisabled(filialElement, disable);
                                      // documentNumberElement.disabled
   setDisabled(documentNumberElement, documentNumberElement.disabled, undefined, disable);
   if (verifierDigitElement != undefined && disable == true)
      setDisabled(verifierDigitElement, disable);
}

function changeDocumentWidgetFilial(args) {

   var filialElement = args.filialElement;
   var documentNumberElement = args.documentNumberElement;
   var verifierDigitElement = args.verifierDigitElement;
   var documentDefinition = undefined;
   if (args.documentTypeElement != undefined)
      documentDefinition = eval(args.documentTypeElement.value + "_" + args.documentGroup + "_DOCUMENT_WIDGET_DEFINITION");
   
   var r = lookupChange({e:filialElement});
   
   resetValue(documentNumberElement);
   var disable = r == true && getElementValue(filialElement) == "";
   
   if (documentDefinition == undefined || documentDefinition.verifierDigit == undefined)
      setDisabled(documentNumberElement, disable, undefined, false);
   else {
      var documentNumberCriteriaElement = document.getElementById(documentNumberElement.property);
      resetValue(documentNumberCriteriaElement);
      setDisabled(documentNumberCriteriaElement, disable);
      resetValue(verifierDigitElement);
      setDisabled(verifierDigitElement, disable, undefined, false);
   }

   return r;
}

function changeDocumentWidget(args) {

   var parentQualifier = args.parentQualifier;
   var parentQualifierPrepared = (parentQualifier != undefined ? parentQualifier + "." : "");
   var documentDefinition = args.documentDefinition;
   
   var filialElement = args.filialElement;
   var filial = documentDefinition.filial;
   
   var actionService = args.actionService;
   var actionServiceMethodSuffix = args.actionServiceMethodSuffix;
   
   var filialProperty = filialElement.property;
   var filialCriteriaProperty = filialElement.criteriaProperty;
   var filialCriteriaElement = document.getElementById(filialProperty + "." + filialCriteriaProperty);
   
   filialElement.id = parentQualifierPrepared + filial.property + "." + filial.idProperty;
   filialElement.name = parentQualifierPrepared + filial.property + "." + filial.idProperty;
   
   filialCriteriaElement.id = parentQualifierPrepared + filial.property + "." + filial.criteriaProperty;
   filialCriteriaElement.name = parentQualifierPrepared + filial.property + "." + filial.criteriaProperty;
   
   filialElement.property = parentQualifierPrepared + filial.property;
   filialElement.idProperty = filial.idProperty;
   filialElement.criteriaProperty = filial.criteriaProperty;
   if (actionService != undefined)
      filialElement.service = actionService + "." + filial.actionServiceMethod + (actionServiceMethodSuffix != undefined ? actionServiceMethodSuffix : "");
   else
      filialElement.service = filial.service;
   filialElement.url = getCurrentBaseURL() + filial.url;
   
   if (filial.propertyMappings != undefined) {
      filialElement.propertyMappings = [];
      for (var i = 0; i < filial.propertyMappings.length; i++) {
         filialElement.propertyMappings[i] = {};
         for (var p in filial.propertyMappings[i])
            if (inAny(p, ["criteriaProperty", "relatedProperty"]))
               filialElement.propertyMappings[i][p] = parentQualifierPrepared + filial.propertyMappings[i][p];
            else
               filialElement.propertyMappings[i][p] = filial.propertyMappings[i][p];
      }
   }
   
   var verifierDigit = documentDefinition.verifierDigit;

   var documentNumber = documentDefinition.documentNumber;
   var documentNumberElement = args.documentNumberElement;
   var documentNumberProperty = documentNumberElement.property;
   var documentNumberCriteriaProperty = documentNumberElement.criteriaProperty;
   var documentNumberCriteriaElement = document.getElementById(documentNumberProperty + (documentNumberCriteriaProperty != undefined ? "." + documentNumberCriteriaProperty : ""));

   documentNumberElement.property = parentQualifierPrepared + documentNumber.property;
   documentNumberCriteriaElement.maxChars = documentNumber.maxChars;
   documentNumberCriteriaElement.mask = documentNumber.mask;
   var documentNumberLupaElement = document.getElementById(documentNumberProperty + "_lupa");
   documentNumberLupaElement.id = parentQualifierPrepared + documentNumber.property + "_lupa";
   var documentNumberPickerElement = document.getElementById(documentNumberProperty + "_picker");
   documentNumberPickerElement.id = parentQualifierPrepared + documentNumber.property + "_picker";

   if (verifierDigit != undefined) {
      documentNumberElement.widgetType = undefined;
      //documentNumberCriteriaElement.widgetType = undefined;
      documentNumberCriteriaElement.id = parentQualifierPrepared + documentNumber.property;
      documentNumberCriteriaElement.name = parentQualifierPrepared + documentNumber.property;
      documentNumberElement.criteriaProperty = undefined;
   }
   else {
      documentNumberElement.widgetType = "lookup";
      //documentNumberCriteriaElement.widgetType = "lookup";

      documentNumberElement.id = parentQualifierPrepared + documentNumber.property + "." + documentNumber.idProperty;
      documentNumberElement.name = parentQualifierPrepared + documentNumber.property + "." + documentNumber.idProperty;

      documentNumberCriteriaElement.id = parentQualifierPrepared + documentNumber.property + "." + documentNumber.criteriaProperty;
      documentNumberCriteriaElement.name = parentQualifierPrepared + documentNumber.property + "." + documentNumber.criteriaProperty;

      documentNumberElement.idProperty = documentNumber.idProperty;
      documentNumberElement.criteriaProperty = documentNumber.criteriaProperty;
      if (actionService != undefined)
         documentNumberElement.service = actionService + "." + documentNumber.actionServiceMethod + (actionServiceMethodSuffix != undefined ? actionServiceMethodSuffix : "");
      else
         documentNumberElement.service = documentNumber.service;
      documentNumberElement.url = getCurrentBaseURL() + documentNumber.url;
   
      if (documentNumber.propertyMappings != undefined) {
         documentNumberElement.propertyMappings = [];
         for (var i = 0; i < documentNumber.propertyMappings.length; i++) {
            documentNumberElement.propertyMappings[i] = {};
            for (var p in documentNumber.propertyMappings[i])
               if (inAny(p, ["criteriaProperty", "relatedProperty"]))
                  documentNumberElement.propertyMappings[i][p] = parentQualifierPrepared + documentNumber.propertyMappings[i][p];
               else
                  documentNumberElement.propertyMappings[i][p] = documentNumber.propertyMappings[i][p];
         }
      }
   }

   var verifierDigitElement = args.verifierDigitElement;
   if (verifierDigitElement != undefined) {

      var verifierDigitProperty = verifierDigitElement.property;
      var verifierDigitLupaElement = document.getElementById(verifierDigitProperty + "_lupa");
      var verifierDigitCriteriaProperty = verifierDigitElement.criteriaProperty;
      var verifierDigitCriteriaElement = document.getElementById(verifierDigitProperty + "." + verifierDigitCriteriaProperty);

      if (verifierDigit != undefined) {
         verifierDigitLupaElement.id = parentQualifierPrepared + verifierDigit.property + "_lupa";
         var verifierDigitPickerElement = document.getElementById(verifierDigitProperty + "_picker");
         verifierDigitPickerElement.id = parentQualifierPrepared + verifierDigit.property + "_picker";

         verifierDigitElement.id = parentQualifierPrepared + verifierDigit.property + "." + verifierDigit.idProperty;
         verifierDigitElement.name = parentQualifierPrepared + verifierDigit.property + "." + verifierDigit.idProperty;
   
         verifierDigitCriteriaElement.id = parentQualifierPrepared + verifierDigit.property + "." + verifierDigit.criteriaProperty;
         verifierDigitCriteriaElement.name = parentQualifierPrepared + verifierDigit.property + "." + verifierDigit.criteriaProperty;

         verifierDigitElement.property = parentQualifierPrepared + verifierDigit.property;
         verifierDigitElement.idProperty = verifierDigit.idProperty;
         verifierDigitElement.criteriaProperty = verifierDigit.criteriaProperty;
         if (actionService != undefined)
            verifierDigitElement.service = actionService + "." + verifierDigit.actionServiceMethod + (actionServiceMethodSuffix != undefined ? actionServiceMethodSuffix : "");
         else
            verifierDigitElement.service = verifierDigit.service;
         verifierDigitElement.url = getCurrentBaseURL() + verifierDigit.url;
         verifierDigitCriteriaElement.maxChars = verifierDigit.maxChars;
         verifierDigitCriteriaElement.mask = verifierDigit.mask;
         
         verifierDigitElement.serializable = true;
         verifierDigitCriteriaElement.serializable = true;
         
         documentNumberLupaElement.style.display = "none";
         setDisabled(verifierDigitElement, true, undefined, false);
         verifierDigitLupaElement.style.display = "inline";
         verifierDigitCriteriaElement.style.display = "inline";
         documentNumberCriteriaElement.onbeforedeactivate = function() { if (getElementValue(documentNumberCriteriaElement) == "") resetValue(verifierDigitElement); return onBeforeDeactivateElement(documentNumberCriteriaElement, undefined); };
         
         if (verifierDigit.propertyMappings != undefined) {
            verifierDigitElement.propertyMappings = [];
            for (var i = 0; i < verifierDigit.propertyMappings.length; i++) {
               verifierDigitElement.propertyMappings[i] = {};
               for (var p in verifierDigit.propertyMappings[i])
                  if (inAny(p, ["criteriaProperty", "relatedProperty"]))
                     verifierDigitElement.propertyMappings[i][p] = parentQualifierPrepared + verifierDigit.propertyMappings[i][p];
                  else
                     verifierDigitElement.propertyMappings[i][p] = verifierDigit.propertyMappings[i][p];
            }
         }
      }
      else {
         documentNumberCriteriaElement.onbeforedeactivate = function() {return lookupChange({e:documentNumberElement});};
         
         verifierDigitLupaElement.style.display = "none";
         verifierDigitCriteriaElement.style.display = "none";
         
         documentNumberLupaElement.style.display = "inline";
         
         verifierDigitElement.serializable = false;
         verifierDigitCriteriaElement.serializable = false;
      }
   }
}

function changeDocumentWidgetTypeOnDataLoad(args) {

	var filialElement = args.filialElement;
	var documentNumberElement = args.documentNumberElement;
	var verifierDigitElement = args.verifierDigitElement;
	var data = args.data;
	var documentDefinitions = args.documentDefinitions;
	var documentGroup = args.documentGroup;
	var parentQualifier = args.parentQualifier;
	var actionService = args.actionService;
    var actionServiceMethodSuffix = args.actionServiceMethodSuffix;

	var documentDefinitionFound = false;
	var documentDefinition;
	for (var i = 0; i < documentDefinitions.length; i++) {
		documentDefinition = documentDefinitions[i];

		var fullQualifierName = documentDefinition.documentNumber.property;
		var value = getNestedBeanPropertyValue(data, fullQualifierName);
		if (value != undefined) {
		   documentDefinitionFound = true;
		   break;
		}
	}
	
	if (documentDefinitionFound) {
	   changeDocumentWidget({documentDefinition:eval(documentDefinition.type + "_" + documentGroup + "_DOCUMENT_WIDGET_DEFINITION"), filialElement:filialElement, documentNumberElement:documentNumberElement, verifierDigitElement:verifierDigitElement, parentQualifier:parentQualifier, actionService:actionService, actionServiceMethodSuffix:actionServiceMethodSuffix});
	   disableDocumentWidget({disable:false, filialElement:filialElement, documentNumberElement:documentNumberElement, verifierDigitElement:verifierDigitElement});
	}
}

function changeDocumentWidgetVerifierDigit(args) {

   var documentNumberCriteriaElement = args.documentNumberCriteriaElement;
   var verifierDigitElement = args.verifierDigitElement;
   
   var verifierDigitCriteriaElement = document.getElementById(verifierDigitElement.property + '.' + verifierDigitElement.criteriaProperty);
   
   if (documentNumberCriteriaElement.value == "" || verifierDigitCriteriaElement.value == "")
      return true;
   
   if (validaDigitoVerificadorCTRC(documentNumberCriteriaElement.value, verifierDigitCriteriaElement.value) == false) {
      alert(erInvalidCTRCVerifierDigit);
      return false;
   }

   return lookupChange({e:verifierDigitElement});
}

function getDigitoVerificadorCTRC(nrConhecimento) {
	var nr = parseInt(stringToNumber(nrConhecimento), 10);
	dvConhecimento = nr % 11;
	return (dvConhecimento == 10) ? 0 : dvConhecimento;
}

function validaDigitoVerificadorCTRC(nrConhecimento, dvConhecimento) {
	var dv = getDigitoVerificadorCTRC(nrConhecimento);
	return (dv == dvConhecimento);
}
