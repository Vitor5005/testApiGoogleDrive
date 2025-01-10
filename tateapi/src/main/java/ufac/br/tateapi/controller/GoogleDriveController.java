package ufac.br.tateapi.controller;

import java.io.IOException;
import java.io.File;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ufac.br.tateapi.model.Resposta;
import ufac.br.tateapi.service.GoogleDriveService;
@Controller
@RequestMapping("/drive")
public class GoogleDriveController {
    @Autowired
    private GoogleDriveService service;

    @PostMapping("/uploadDrive")
    public ResponseEntity<Resposta> handleFileUpload(@RequestParam("file") MultipartFile file, 
                                                     @RequestParam(value = "folderId", required = false) String folderId) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            Resposta res = new Resposta();
            res.setStatus(400);
            res.setMessage("File is empty");
            return ResponseEntity.badRequest().body(res);
        }
        if (folderId == null || folderId.isEmpty()) {
            folderId = "1H9cXePNQ0htEoiQ1Ys4Vba-pPrPBH6gP"; 
        }                                                   

        String newFileName = String.format(file.getOriginalFilename());

        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        Resposta res = service.uploadFileToDrive(tempFile, folderId, newFileName);
        System.out.println(res);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
