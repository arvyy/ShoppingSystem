package lt.mif.ise.rest.controller;

import lt.mif.ise.jpa.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("api/excel/")
public class ExportImportRestController {
    @Autowired
    private ProductRepository productRepository;

    private static String UPLOAD_DIR = "uploads";

    private void saveFile(InputStream inputStream, String path){
        try {
            OutputStream outputStream = new FileOutputStream(new File(path));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, read);
            }

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importExcel(String path){
        try {
            //TODO: excel products import
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    private String upload(@RequestParam(value = "file")MultipartFile file, HttpServletRequest request){
        try {
            String fileName= file.getOriginalFilename();
            String path = request.getServletContext().getRealPath("") + fileName;
            System.out.println(path);
            saveFile(file.getInputStream(), path);
            return fileName;
        } catch(Exception e) {
            return null;
        }
    }
}