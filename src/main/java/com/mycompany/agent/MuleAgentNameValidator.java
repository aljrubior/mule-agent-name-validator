package com.mycompany.agent;

import com.mulesoft.agent.exception.ArtifactValidationException;
import com.mulesoft.agent.services.ArtifactValidator;
import com.mulesoft.agent.services.EncryptionService;
import com.mycompany.agent.exceptions.InvalidNameException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Named("MuleAgentNameValidator")
@Singleton
public class MuleAgentNameValidator implements ArtifactValidator {

    private static final Logger LOGGER = LogManager.getLogger(MuleAgentNameValidator.class);

    // Properties provided by Mule Agent Plugin
    public static final String APPLICATION_NAME_KEY = "_APPLICATION_NAME";

    // Properties configured in the Artifact Validator service
    public static final String APPLICATION_NAME_PATTERN_KEY = "namePattern";

    public String getType() {
        return "nameValidator";
    }

    public String getName() {
        return "defaultNameValidator";
    }

    @Inject
    EncryptionService encryptionService;

    public void validate(Map<String, Object> args) throws ArtifactValidationException {

        String applicationName = (String) args.get(APPLICATION_NAME_KEY);

        String namePattern = (String) args.get(APPLICATION_NAME_PATTERN_KEY);

        boolean matches = Pattern.matches(namePattern, applicationName);

        if (!matches) {
            String message =
                    format("Application '%s' has an invalid name. Reason: Name '%s' not match the pattern: '%s'.",
                            applicationName,
                            applicationName,
                            namePattern);
            LOGGER.error(message);
            throw new InvalidNameException(message);
        }
    }
}
