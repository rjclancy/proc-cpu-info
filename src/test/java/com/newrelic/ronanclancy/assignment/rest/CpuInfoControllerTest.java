package com.newrelic.ronanclancy.assignment.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.ronanclancy.assignment.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CpuInfoControllerTest {

  @Mock
  private FileService fileService;

  @InjectMocks
  private CpuInfoController cpuInfoController;

  @Test
  public void testGetCpuInfo() throws IOException {

    final URL resource = getClass().getClassLoader().getResource("Sample.txt");
    final Stream<String> lines = Files.lines(Paths.get(resource.getFile()));
    when(fileService.getFileStream(any())).thenReturn(lines);

    final ResponseEntity<String> cpuInfo = cpuInfoController.getCpuInfo();

    assertThat(cpuInfo).isNotNull();
    assertThat(cpuInfo.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(isJSONValid(cpuInfo.getBody())).isTrue();
  }

  public static boolean isJSONValid(String jsonInString) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(jsonInString);
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}