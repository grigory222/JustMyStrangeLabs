package ru.itmo.tpo.domain;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("ru/itmo/tpo/domain")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ru.itmo.tpo.domain")
public class ArthurCucumberTest {
}
