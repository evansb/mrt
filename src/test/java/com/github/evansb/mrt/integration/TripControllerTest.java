package com.github.evansb.mrt.integration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.evansb.mrt.MrtApplication;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.StreamUtils;

@Tag("integration")
@SpringBootTest(classes = MrtApplication.class, webEnvironment = MOCK)
@AutoConfigureMockMvc
public class TripControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ResourceLoader resourceLoader;

  @Value("classpath:shortcut")
  private Resource shortcut;

  @Value("classpath:crossing")
  private Resource crossing;

  @Value("classpath:exchange")
  private Resource exchange;

  @Value("classpath:night")
  private Resource night;

  @Value("classpath:justbeforeopen")
  private Resource justbeforeopen;

  @Value("classpath:nextday")
  private Resource nextday;

  @Test
  public void testShortcut() throws Exception {
    String expectedResult =
        StreamUtils.copyToString(shortcut.getInputStream(), StandardCharsets.UTF_8);

    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "DT1")
            .param("to", "DT20")
            .param("start", "2020-10-27T08:07:47.699Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(expectedResult));
  }

  @Test
  public void testClosing() throws Exception {
    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "DT1")
            .param("to", "DT20")
            .param("start", "2020-10-27T13:30:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(""));
  }

  @Test
  public void testNotOpen() throws Exception {
    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "DT1")
            .param("to", "TE22")
            .param("start", "2020-10-27T13:30:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(""));
  }

  @Test
  public void testOnlyExchange() throws Exception {
    String expectedResult =
        StreamUtils.copyToString(exchange.getInputStream(), StandardCharsets.UTF_8);

    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "NS24")
            .param("to", "NE6")
            .param("start", "2020-10-27T09:30:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(expectedResult));
  }

  @Test
  public void testNight() throws Exception {
    String expectedResult =
        StreamUtils.copyToString(night.getInputStream(), StandardCharsets.UTF_8);

    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "NS24")
            .param("to", "NE6")
            .param("start", "2020-10-27T13:30:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(expectedResult));
  }

  @Test
  public void testCrossing() throws Exception {
    String expectedResult =
        StreamUtils.copyToString(crossing.getInputStream(), StandardCharsets.UTF_8);

    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "DT1")
            .param("to", "DT20")
            .param("start", "2020-10-27T09:30:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(expectedResult));
  }

  @Test
  public void testJustBeforeOpen() throws Exception {
    String expectedResult =
        StreamUtils.copyToString(justbeforeopen.getInputStream(), StandardCharsets.UTF_8);

    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "TE8")
            .param("to", "TE11")
            .param("start", "2021-12-30T15:59:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(expectedResult));
  }

  @Test
  public void testNextDay() throws Exception {
    String expectedResult =
        StreamUtils.copyToString(nextday.getInputStream(), StandardCharsets.UTF_8);

    MockHttpServletRequestBuilder request =
        post("/v1/trip")
            .param("from", "CC10")
            .param("to", "NS1")
            .param("start", "2020-10-27T14:00:00Z");

    mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(expectedResult));
  }
}
