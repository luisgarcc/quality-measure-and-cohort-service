package com.ibm.cohort.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.ibm.cohort.engine.translation.CqlTranslationProvider;
import com.ibm.cohort.engine.translation.InJVMCqlTranslationProvider;

import ca.uhn.fhir.context.FhirContext;

public class CqlEngineFlinkExample {

	private static final LibraryInfo LIBRARY_INFO = new LibraryInfo(
			"wh-cohort-Over-the-Hill-1.0.0",
			"Over-the-Hill",
			"1.0.0"
	);
	private static final String EXPRESSION = "Initial Population";

	public static void main(String[] args) throws Exception {
		ParameterTool params = ParameterTool.fromArgs(args);

		ServerInfo serverInfo = new ServerInfo(
				params.getRequired("tenantId"),
				params.getRequired("username"),
				params.getRequired("password"),
				params.getRequired("endpoint")
		);

		List<LibraryExecution> executions = generateInput(params.getInt("numPatients"));

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<ExecutionResult> stream = env
				.fromCollection(executions)
				.map(x -> evaluate(x, serverInfo));

		stream.print();

		env.execute("cohort-engine-test");
	}

	// TODO: This caching logic helps limit the number of times we translate CQL to ELM
//	private static ThreadLocal<LibraryLoaderCache> LOADER_CACHE = ThreadLocal.withInitial(LibraryLoaderCache::new);
//	public static class LibraryLoaderCache {
//		private final Map<String, LibraryLoader> cache = new HashMap<>();
//
//		public LibraryLoader getLibraryLoader(String libraryId, CqlEngineWrapper wrapper) {
//			return cache.computeIfAbsent(libraryId, x -> {
//				MultiFormatLibrarySourceProvider sourceProvider = new FhirLibraryLibrarySourceProvider(
//						wrapper.getMeasureServerClient(),
//						libraryId
//				);
//				// TODO: If we upload ELM files, we can skip the translation process
//				CqlTranslationProvider translationProvider = new InJVMCqlTranslationProvider(sourceProvider);
//				// TODO: Thread local the loader???
//				return new TranslatingLibraryLoader(sourceProvider, translationProvider, true);
//			});
//		}
//	}

	private static ExecutionResult evaluate(LibraryExecution execution, ServerInfo serverInfo) throws Exception {
		CqlEngineWrapper wrapper = getWrapper(serverInfo);

		MultiFormatLibrarySourceProvider sourceProvider = new FhirLibraryLibrarySourceProvider(
				wrapper.getMeasureServerClient(),
				execution.getLibrary().getId()
		);
		// TODO: If we upload ELM files, we can skip the translation process
		CqlTranslationProvider translationProvider = new InJVMCqlTranslationProvider(sourceProvider);
		wrapper.setLibraryLoader(new TranslatingLibraryLoader(sourceProvider, translationProvider, true));

		// TODO: This caching logic helps limit the number of times we translate CQL to ELM
//		wrapper.setLibraryLoader(LOADER_CACHE.get().getLibraryLoader(execution.getLibrary().getId(), wrapper));

		ExecutionResult retVal = new ExecutionResult();
		wrapper.evaluate(
				execution.getLibrary().getName(),
				execution.getLibrary().getVersion(),
				null,
				Collections.singleton(EXPRESSION),
				Collections.singletonList(execution.getPatientId()),
				new EvaluationResultCallback() {
					@Override
					public void onContextBegin(String contextId) {
					}

					@Override
					public void onContextComplete(String contextId) {
					}

					@Override
					public void onEvaluationComplete(String contextId, String expression, Object result) {
						retVal.setLibraryId(execution.getLibrary().getId());
						retVal.setPatientId(execution.getPatientId());
						retVal.setResult((boolean)result);
					}
				}
		);

		if (retVal.getLibraryId() == null) {
			throw new RuntimeException("Result not set: " + execution);
		}

		return retVal;
	}

	public static CqlEngineWrapper getWrapper(ServerInfo serverInfo) {
		FhirContext fhirContext = FhirContext.forR4();
		FhirClientBuilderFactory factory = FhirClientBuilderFactory.newInstance();
		FhirClientBuilder builder = factory.newFhirClientBuilder(fhirContext);

		CqlEngineWrapper wrapper = new CqlEngineWrapper(builder);

		IBMFhirServerConfig config = serverInfo.toIbmServerConfig();
		wrapper.setDataServerConnectionProperties(config);
		wrapper.setTerminologyServerConnectionProperties(config);
		wrapper.setMeasureServerConnectionProperties(config);

		return wrapper;
	}

	private static List<LibraryExecution> generateInput(int numPatients) {
		List<LibraryExecution> retVal = new ArrayList<>();
		for (int i = 0; i < numPatients; i++) {
			String patientId = InputPatientIds.getRandomPatientId();
			retVal.add(new LibraryExecution(LIBRARY_INFO, patientId));
		}
		return retVal;
	}
}
