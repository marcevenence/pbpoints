package com.pbpoints.service.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pbpoints.domain.UserExtra;
import com.pbpoints.repository.UserExtraRepository;
import com.pbpoints.repository.UserRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class DataBackupService {

    private final Logger log = LoggerFactory.getLogger(DataBackupService.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserExtraRepository userExtraRepository;

    @Autowired
    private UserRepository userRepository;

    public DataBackupService() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Async
    @Scheduled(cron = "${application.cronDataExport}")
    public void exportUsers() throws IOException {
        log.info("Inicio proceso de export de users");

        List<UserExtra> usersExtra = userExtraRepository.findAll(Sort.by("id"));
        if (!CollectionUtils.isEmpty(usersExtra)) {
            Path path = Paths.get("./dataExport/");
            Files.createDirectories(path);
            log.info("Cantidad de users a exportar: {}", usersExtra.size());
            Files.write(Path.of(path + "/user.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(usersExtra));
            log.info("Users exportados correctamente");
        } else {
            log.info("No hay usuarios para exportar");
        }
        log.info("Fin proceso de export de users");
    }
}
