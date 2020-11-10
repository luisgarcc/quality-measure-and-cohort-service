/*
 * (C) Copyright IBM Corp. 2020, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.cohort.engine.translation;

import java.io.InputStream;
import java.util.List;

import org.cqframework.cql.cql2elm.CqlTranslator.Options;
import org.cqframework.cql.cql2elm.ModelInfoLoader;
import org.cqframework.cql.cql2elm.ModelInfoProvider;
import org.cqframework.cql.elm.execution.Library;
import org.hl7.elm_modelinfo.r1.ModelInfo;

import com.ibm.cohort.engine.LibraryFormat;

/**
 * A general interface that can be used to abstract interaction with multiple
 * implementations of a CqlTranslator such as linking directly to the translator
 * JAR or calling out to the CqlTranslationService microservice.
 */
public interface CqlTranslationProvider<T> {
	public Library translate(InputStream cql) throws Exception;

	public Library translate(InputStream cql, List<Options> options) throws Exception;

	public Library translate(InputStream cql, List<Options> options, LibraryFormat targetFormat) throws Exception;
	
	public default void registerModelInfo(ModelInfo modelInfo) {
		// Force mapping  to FHIR 4.0.1. Consider supporting different versions in the future.
		// Possibly add support for auto-loading model info files.
		modelInfo.setTargetVersion("4.0.1");
		modelInfo.setTargetUrl("http://hl7.org/fhir");
		org.hl7.elm.r1.VersionedIdentifier modelId = (new org.hl7.elm.r1.VersionedIdentifier()).withId(modelInfo.getName()).withVersion(modelInfo.getVersion());
		ModelInfoProvider modelProvider = () -> modelInfo;
		ModelInfoLoader.registerModelInfoProvider(modelId, modelProvider);
	}
	
	public default void convertAndRegisterModelInfo(T modelInfoObject) {
		registerModelInfo(convertToModelInfo(modelInfoObject));
	}
	
	public ModelInfo convertToModelInfo(T modelInfoObject);
}
