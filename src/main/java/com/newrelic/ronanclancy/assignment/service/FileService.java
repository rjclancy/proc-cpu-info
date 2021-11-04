package com.newrelic.ronanclancy.assignment.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileService {

  public Stream<String> getFileStream(final String filePath) throws IOException {
    return Files.lines(Paths.get(filePath));
  }
}
