package com.lucca.fileSorter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JobSchedulerService {

    @Autowired
    FileService fileService;

    @Scheduled(fixedDelay = 10000 )
    public void processFiles() throws IOException {
        fileService.processFiles();
    }
}
