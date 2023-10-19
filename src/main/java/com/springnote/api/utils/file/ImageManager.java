package com.springnote.api.utils.file;

import com.springnote.api.utils.context.RequestIdContext;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ImageManager {
    @Value("${springnote.filesave.path}")
    private String savePath;

    private final RequestIdContext requestIdContext;

//    https://www.baeldung.com/java-image-compression-lossy-lossless
    public File optimize(MultipartFile file, String name, String format) throws IOException {


        var inputImage = ImageIO.read(file.getInputStream());

        var writers = ImageIO.getImageWritersByFormatName(format);
        var writer = writers.next();

        var output = new File(savePath+name+"."+format);
        var outputStream = ImageIO.createImageOutputStream(output);
        writer.setOutput(outputStream);

        var params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(0.5f);

        writer.write(null, new IIOImage(inputImage, null, null), params);

        outputStream.close();
        writer.dispose();

        return output;
    }

    public File find(String fileName){

        return new File(savePath+fileName);
    }

    public boolean delete(String fileName){
        var file = new File(savePath+fileName);
        if(file.exists()){
            return file.delete();
        }
        return false;
    }

}
