package br.com.jordihofc.fileuploder.file;

import org.springframework.web.multipart.MultipartFile;

public record FileUpload(MultipartFile data) {
}
