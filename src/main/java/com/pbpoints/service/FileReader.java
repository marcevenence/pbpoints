package com.pbpoints.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FileReader {

    private static final String finalPath = System.getProperty("user.home") + "/git";

    public static void main(String[] args) throws IOException, InterruptedException {
        //  Instancio el servicio que va a monitorear mi fileSystem
        WatchService watchService = FileSystems.getDefault().newWatchService();
        // Directorio que voy a monitorear
        Path path = Paths.get(finalPath);

        // Eventos que va a estar monitoreando
        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        );

        // reacciones ante estimulos en ese directorio
        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                // logueo el tipo de evento, y el nombre del fichero afectado
                System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                // Si no es una eliminacion, logueo el contenido del archivo
                if (event.kind() != StandardWatchEventKinds.ENTRY_DELETE) {
                    Charset charset = Charset.forName("UTF-8");

                    List<String> lines = Files.readAllLines(Paths.get(finalPath + "/" + event.context().toString()), charset);
                    for (String line : lines) {
                        System.out.println(line);
                    }
                    // parsear la lista de string a DTO con jackson
                    // https://www.baeldung.com/jackson-xml-serialization-and-deserialization
                }
            }
            key.reset();
        }
    }
}
