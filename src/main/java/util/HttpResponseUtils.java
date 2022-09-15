package util;

import http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HttpResponseUtils {

    public static byte[] fileToByte(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    };

    public static byte[] headerToByte(HashMap<String, String> header) {
        return headerToString(header).getBytes();
    }

    public static String headerToString(HashMap<String, String> header) {
        return header.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\r\n")) + "\r\n\r\n";
    }

    public static byte[] statusLineToByte(String version, HttpStatus status) {
        return String.format("HTTP/%s %d %s\r\n", version, status.getStatusCode(), status.getStatusMessage()).getBytes();
    }
}
