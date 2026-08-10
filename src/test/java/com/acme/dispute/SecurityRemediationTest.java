package com.acme.dispute;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "demo.users.analyst-a-password=test-a-password",
        "demo.users.analyst-b-password=test-b-password",
        "demo.users.admin-password=test-admin-password",
        "app.upload-dir=build/test-secure-uploads"
})
class SecurityRemediationTest {

    @Autowired
    MockMvc mvc;

    @Test
    void anonymousDisputeAccessIsRejected() throws Exception {
        mvc.perform(get("/api/disputes/1")).andExpect(status().isUnauthorized());
    }

    @Test
    void assignedAnalystCanAccessOwnDisputeWithoutFullPan() throws Exception {
        mvc.perform(get("/api/disputes/1").with(httpBasic("analystA", "test-a-password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardLastFour").value("1111"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("4111111111111111"))));
    }

    @Test
    void analystCannotAccessAnotherAnalystsDispute() throws Exception {
        mvc.perform(get("/api/disputes/2").with(httpBasic("analystA", "test-a-password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void sqlLikeInputIsHandledAsData() throws Exception {
        mvc.perform(get("/api/disputes/search")
                        .param("email", "x' OR '1'='1")
                        .with(httpBasic("analystA", "test-a-password")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unsupportedUploadIsRejected() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "../../script.sh",
                "application/x-sh", "echo bad".getBytes());
        mvc.perform(multipart("/api/evidence/upload").file(file)
                        .with(httpBasic("analystA", "test-a-password")))
                .andExpect(status().isBadRequest());
    }
}
