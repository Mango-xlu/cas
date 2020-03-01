package org.apereo.cas.authentication.credential;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.util.spring.ApplicationContextProvider;

import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.mapping.MappingResults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.test.MockRequestContext;
import org.springframework.webflow.validation.DefaultValidationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This is {@link UsernamePasswordCredentialTests}.
 *
 * @author Misagh Moayyed
 * @since 6.2.0
 */
@SpringBootTest(classes = RefreshAutoConfiguration.class,
    properties = "cas.authn.policy.source-selection-enabled=true")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class UsernamePasswordCredentialTests {
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    public void verifyOperation() {
        ApplicationContextProvider.holdApplicationContext(applicationContext);
        val input = new UsernamePasswordCredential("casuser", "Mellon", StringUtils.EMPTY, Map.of());
        assertTrue(input.isValid());
        assertEquals(UsernamePasswordCredential.class, input.getClass());
        assertEquals(input, input.toCredential());

        val context = new MockRequestContext();
        val request = new MockHttpServletRequest();
        val response = new MockHttpServletResponse();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, response));

        val validationContext = new DefaultValidationContext(context, "submit", mock(MappingResults.class));
        input.validate(validationContext);
        assertTrue(context.getMessageContext().hasErrorMessages());

    }

    @Test
    public void verifyInvalid() {
        ApplicationContextProvider.holdApplicationContext(applicationContext);
        val input = new UsernamePasswordCredential(null, "Mellon", StringUtils.EMPTY, Map.of());

        val context = new MockRequestContext();
        val request = new MockHttpServletRequest();
        val response = new MockHttpServletResponse();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, response));

        val validationContext = new DefaultValidationContext(context, "whatever", mock(MappingResults.class));
        input.validate(validationContext);
        assertTrue(context.getMessageContext().hasErrorMessages());
    }
}
