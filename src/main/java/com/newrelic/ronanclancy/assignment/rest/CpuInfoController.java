package com.newrelic.ronanclancy.assignment.rest;

import com.newrelic.ronanclancy.assignment.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

@Log4j2
@RestController("/")
public class CpuInfoController {

  @Value("${cpu-info.file-path}")
  private String filePath;

  @Autowired
  private FileService fileService;

  /**
   * This api will read the host containers proc/cpuinfo and return it
   * in JSON format.
   *
   * @return the cpu info
   * @throws IOException the io exception
   */
  @GetMapping
  public ResponseEntity<String> getCpuInfo() throws IOException {

    final Stream<String> stream = fileService.getFileStream(filePath);
    final Iterator<String> iterator = stream.iterator();

    JSONObject jsonObjectMain = new JSONObject();
    Map<String, Object> currentProcessorMap = new HashMap<>();
    int currentProcessor = 0;

    while (iterator.hasNext()) {

      final String[] split = iterator.next().split(":");

      if (split[0].isEmpty() || !iterator.hasNext()) {

        JSONObject jsonObjectProcessor = new JSONObject(currentProcessorMap);
        jsonObjectMain.put(String.valueOf(currentProcessor), jsonObjectProcessor);
        currentProcessorMap = new HashMap<>();
        currentProcessor++;

      } else {

        if (split.length == 1) {
          currentProcessorMap.put(split[0].strip(), "");
        } else if (split.length == 2) {
          currentProcessorMap.put(split[0].strip(), split[1].strip());
        }

      }
    }
    return new ResponseEntity<>(jsonObjectMain.toString(), HttpStatus.OK);
  }
}
